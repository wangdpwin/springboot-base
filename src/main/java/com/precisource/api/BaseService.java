package com.precisource.api;

import com.google.common.collect.Maps;
import com.precisource.config.DefaultConfig;
import com.precisource.config.SpringContentUtils;
import com.precisource.consts.DefaultConsts;
import com.precisource.consts.ErrorCode;
import com.precisource.consts.HeaderConsts;
import com.precisource.domain.BaseHttp;
import com.precisource.exception.BaseException;
import com.precisource.exception.BaseFileException;
import com.precisource.exception.BaseUnexpectedException;
import com.precisource.util.CommonUtils;
import com.precisource.util.JsonUtils;
import com.precisource.util.MimeTypes;
import com.precisource.util.StringUtils;
import com.precisource.util.TimeUtils;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.List;
import java.util.Map;

@Service
public abstract class BaseService {

    private static final String INLINE_DISPOSITION_TYPE = "inline";

    private static final String ATTACHMENT_DISPOSITION_TYPE = "attachment";

    private static final URLCodec encoder = new URLCodec();

    @Autowired
    private ThreadLocal<BaseHttp> baseHttpThreadLocal;

    /**
     * get user id in jwt token.
     *
     * @return
     */
    protected String currentUserId() {

        // 如果是dev环境，直接放行
        if (DefaultConsts.MODE.DEV.getProfile().equalsIgnoreCase(SpringContentUtils.getActiveProfile())) {
            return DefaultConfig.getMockUserId();
        }

        return baseHttpThreadLocal.get().getRequest().getAttribute("aud").toString();
    }

    /**
     * set jwt token cookie with httpOnly and secure deps on your 'application.session.secure' setting.
     *
     * @param jwt
     * @param duration like 2h, 3d
     */
    protected void setJWTCookie(String jwt, String duration) {
        Cookie cookie = new Cookie(DefaultConfig.getCookieTokenName(), jwt);
        cookie.setPath(StringUtils.SLASH);
        cookie.setSecure(DefaultConfig.getCookieSecure());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(TimeUtils.parseDuration(duration));

        baseHttpThreadLocal.get().getResponsen().addCookie(cookie);
    }

    /**
     * 在response的header中添加X-Total-Count
     */
    protected void setTotalCount(long totalCount) {
        setTotalCount(HeaderConsts.TOTOL_COUNT_KEY, totalCount);
    }

    /**
     * 在response的header中添加 headerName
     */
    protected void setTotalCount(String headerName, long totalCount) {
        setHeader(headerName, String.valueOf(totalCount));
    }

    /**
     * 在response的header中添加 headerName
     */
    protected void setHeader(String headerName, String value) {
        HttpServletResponse response = baseHttpThreadLocal.get().getResponsen();
        String headerValue = response.getHeader(HeaderConsts.ACCESS_CONTROL_EXPOSE_HEADERS_KEY);
        response.setHeader(HeaderConsts.ACCESS_CONTROL_EXPOSE_HEADERS_KEY, headerValue + StringUtils.COMMA + headerName);

        response.setHeader(headerName, value);
    }

    protected void setHeader(List<Header> headers) {
        HttpServletResponse response = baseHttpThreadLocal.get().getResponsen();
        String headerNames = CommonUtils.union(CommonUtils.collectColumn(headers, Header::getName));
        String headerValue = response.getHeader(HeaderConsts.ACCESS_CONTROL_EXPOSE_HEADERS_KEY);
        response.setHeader(HeaderConsts.ACCESS_CONTROL_EXPOSE_HEADERS_KEY, headerValue + StringUtils.COMMA + headerNames);

        if (CollectionUtils.isEmpty(headers)) {
            headers.forEach(header -> response.setHeader(header.getName(), String.valueOf(header.getValue())));
        }
    }

    /**
     * 设置图片的信息
     */
//    protected void setImageHeader() {
//        HttpServletResponse response = baseHttpThreadLocal.get().getResponsen();
//        String headerValue = response.getHeader(HeaderConsts.ACCESS_CONTROL_EXPOSE_HEADERS_KEY);
//        // 图片的宽、高、宽高比
//        String appendHeaders = ",X-Image-Width,X-Image-Height,X-Image-Aspect-Ratio";
//        headerValue = headerValue.concat(appendHeaders);
//        response.setHeader(HeaderConsts.ACCESS_CONTROL_EXPOSE_HEADERS_KEY, headerValue);
//    }

    /**
     * set total count header to 0 and render empty list.
     */
    protected void renderEmptyList() {
        HttpServletResponse response = baseHttpThreadLocal.get().getResponsen();
        String headerValue = response.getHeader(HeaderConsts.ACCESS_CONTROL_EXPOSE_HEADERS_KEY);
        response.setHeader(HeaderConsts.ACCESS_CONTROL_EXPOSE_HEADERS_KEY, headerValue + StringUtils.COMMA + HeaderConsts.TOTOL_COUNT_KEY);
        response.setHeader(HeaderConsts.TOTOL_COUNT_KEY, "0");
        throw new BaseException(HttpStatus.OK, Lists.newArrayList());
    }

    /**
     * Send a 200 OK response
     */
    protected void ok() {
        throw new BaseException(HttpStatus.OK);
    }

    /**
     * 在线查看
     *
     * @param file
     */
    protected void renderBinary(File file) {
        render(file, null, file.getName(), true);
    }

    /**
     * 下载
     *
     * @param file
     * @param name 别名
     */
    protected void renderBinary(File file, String name) {
        render(file, null, name, false);
    }

    protected void renderBinary(InputStream is) {
        render(null, is, null, true);
    }

    protected void renderBinary(InputStream is, String name) {
        render(null, is, name, false);
    }

    /**
     * @param file
     * @param is
     * @param name
     * @param inline true时表示在线查看，false表示下载
     */
    private void render(File file, InputStream is, String name, boolean inline) {
        HttpServletResponse response = baseHttpThreadLocal.get().getResponsen();
        if (name != null) {
            setContentTypeIfNotSet(response, MimeTypes.getContentType(name));
        }

        try {
            if (StringUtils.isEmpty(response.getHeader(HeaderConsts.CONTENT_DISPOSITION_KEY))) {
                addContentDispositionHeader(response, name, inline);
            }
            if (file != null) {
                renderFile(file);
            } else {
                renderInputStream(is, response);
            }
        } catch (Exception e) {

        }
    }

    private static void renderInputStream(InputStream inputStream, HttpServletResponse response) throws IOException {
        OutputStream outputStream = response.getOutputStream();
        // 在http响应中输出流
        byte[] cache = new byte[1024];
        int nRead = 0;
        while ((nRead = inputStream.read(cache)) != -1) {
            outputStream.write(cache, 0, nRead);
            outputStream.flush();
        }
        outputStream.flush();
    }

    private static void renderFile(File file) {
        if (!file.exists()) {
            throw new BaseUnexpectedException("Your file does not exists (" + file + ")");
        }
        if (!file.canRead()) {
            throw new BaseUnexpectedException("Can't read your file (" + file + ")");
        }
        if (!file.isFile()) {
            throw new BaseUnexpectedException("Your file is not a real file (" + file + ")");
        }

        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new BaseUnexpectedException("Your file does not found (" + file + ")");
        }

        throw new BaseFileException(resource, file.length());
    }

    private void setContentTypeIfNotSet(HttpServletResponse response, String contentType) {
        if (StringUtils.isEmpty(response.getContentType())) {
            response.setContentType(contentType);
        }
    }

    private void addContentDispositionHeader(HttpServletResponse response, String name, boolean inline) throws UnsupportedEncodingException {
        if (name == null) {
            response.setHeader(HeaderConsts.CONTENT_DISPOSITION_KEY, dispositionType(inline));
        } else if (canAsciiEncode(name)) {
            String contentDisposition = "%s; filename=\"%s\"";
            response.setHeader(HeaderConsts.CONTENT_DISPOSITION_KEY, String.format(contentDisposition, dispositionType(inline), name));
        } else {
            String encoding = response.getCharacterEncoding();
            String contentDisposition = "%1$s; filename*=" + encoding + "''%2$s; filename=\"%2$s\"";
            response.setHeader(HeaderConsts.CONTENT_DISPOSITION_KEY, String.format(contentDisposition, dispositionType(inline), encoder.encode(name, encoding)));
        }
    }

    private String dispositionType(boolean inline) {
        return inline ? INLINE_DISPOSITION_TYPE : ATTACHMENT_DISPOSITION_TYPE;
    }

    private boolean canAsciiEncode(String string) {
        CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();
        return asciiEncoder.canEncode(string);
    }

    /**
     * Send 201 Created
     */
    protected void created(Object data) {
        throw new BaseException(HttpStatus.CREATED, data);
    }

    /**
     * Send 201 Created
     */
    protected void created(String message) {
        throw new BaseException(HttpStatus.CREATED, message);
    }

    /**
     * Send 204 NO CONTENT
     */
    protected void noContent() {
        throw new BaseException(HttpStatus.NO_CONTENT);
    }

    /**
     * Send 301 redirect
     *
     * @param url
     * @throws IOException
     */
    protected void redirect(String url) {
        HttpServletResponse response = baseHttpThreadLocal.get().getResponsen();
        response.setHeader("Location", url);
        try {
            response.sendRedirect("url");
        } catch (IOException e) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("url", url);
            map.put("code", "resource not exists");
            map.put("request_id", this.baseHttpThreadLocal.get().getRequest().getAttribute(HeaderConsts.REQUEST_ID_KEY));
            throw new BaseException(HttpStatus.NOT_FOUND, ErrorCode.CLIENT_RESOURCE_NOT_FOUND, JsonUtils.toJsonString(map));
        }
    }

    /**
     * Send 400 bad request
     */
    protected void badRequestIfNull(Object object, String message) {
        if (object == null) {
            badRequest(message);
        }
    }

    /**
     * Send 400 bad request
     */
    protected void badRequest() {
        badRequest("Bad request");
    }

    /**
     * Send 400 bad request
     */
    protected void badRequest(String message) throws BaseException {
        throw new BaseException(HttpStatus.BAD_REQUEST, ErrorCode.CLIENT_RESOURCE_NOT_FOUND, message);
    }

    /**
     * Send 400 bad request
     */
    protected void badRequest(Result result) {
        throw new BaseException(HttpStatus.BAD_REQUEST, result.getCode(), result.getMessage());
    }

    /**
     * Send 401 Unauthorized
     */
    protected void unauthorized(Result result) {
        throw new BaseException(HttpStatus.UNAUTHORIZED, result.getCode(), result.getMessage());
    }

    /**
     * Send 401 Unauthorized
     */
    public void unauthorized(String message) {
        throw new BaseException(HttpStatus.UNAUTHORIZED, ErrorCode.CLIENT_AUTH_ERROR, message);
    }

    /**
     * Send 401 Unauthorized
     */
    public void unauthorized() {
        throw new BaseException(HttpStatus.UNAUTHORIZED, ErrorCode.CLIENT_AUTH_ERROR);
    }

    /**
     * Send 403 forbidden
     */
    public void forbidden() {
        throw new BaseException(HttpStatus.FORBIDDEN, ErrorCode.CLIENT_ACCESS_DENIED);
    }

    /**
     * Send 403 forbidden
     */
    public void forbidden(String message) {
        throw new BaseException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, ErrorCode.CLIENT_OVER_QUOTA, message);
    }

    /**
     * Send 403 forbidden
     */
    public void forbidden(Result result) {
        throw new BaseException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, result.getCode(), result.getMessage());

    }

    /**
     * Send 404 bad request
     */
    protected void notFound(String message) {
        throw new BaseException(HttpStatus.NOT_FOUND, ErrorCode.CLIENT_RESOURCE_NOT_FOUND, message);
    }

    /**
     * Send 404 bad request
     */
    protected void notFound() {
        notFound(StringUtils.EMPTY);
    }

    /**
     * Send 404 bad request
     */
    protected void notFoundIfNull(Object o) {
        if (o == null) {
            notFound();
        }
    }

    /**
     * Send 404 bad request
     */
    protected void notFoundIfNull(Object o, String message) {
        if (o == null) {
            notFound(message);
        }
    }

    /**
     * Send 500 INTERNAL_SERVER_ERROR
     */
    protected void error() {
        throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.SERVER_INTERNAL_ERROR);
    }

    /**
     * Send 500 INTERNAL_SERVER_ERROR
     */
    protected void error(String message) {
        throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.SERVER_INTERNAL_ERROR, message);
    }

    /**
     * Send 500 INTERNAL_SERVER_ERROR
     */
    protected void error(Result result) {
        throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, result.getCode(), result.getMessage());

    }

    /**
     * Send 509 CLIENT_OVER_QUOTA
     */
    public void limitExceeded(String message) {
        throw new BaseException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, ErrorCode.CLIENT_OVER_QUOTA, message);
    }

}
