package com.precisource.util;

import com.google.common.collect.Maps;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Map;

/**
 * @author zanxus
 * @version 1.0.0
 * @date 2017-04-20 下午2:57
 */
public class MimeTypeUtils {

    private static Logger logger = Logs.get();

    private static Map<String, String> mimeTypes = Maps.newHashMap();

    static {
        mimeTypes.put("png", "image/png");
        mimeTypes.put("svg", "image/svg+xml");
        mimeTypes.put("sh", "application/x-sh");
        mimeTypes.put("e", "text/x-eiffel");
    }

    public static String getContentType(File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error("自动检测文件类型时未找到指定文件 文件名称：{} ", file.getAbsolutePath(), e);
        }
        ContentHandler contenthandler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
        Parser parser = new AutoDetectParser();
        try {
            parser.parse(is, contenthandler, metadata, new ParseContext());
        } catch (IOException e) {
            logger.error("自动检测文件类型时IO异常 文件名称:{}", file.getAbsolutePath(), e);
        } catch (SAXException e) {
            logger.error("自动检测文件类型时SAX解析异常 文件名称:{}", file.getAbsolutePath(), e);
        } catch (TikaException e) {
            logger.error("自动检测文件类型时发生未知异常 文件名称:{}", file, e);
        }
        String mediaType = metadata.get(Metadata.CONTENT_TYPE);
        return mediaType;
    }

    public static String getContentType(String extension) {
        return mimeTypes.getOrDefault(extension, "text/plain");
    }

}
