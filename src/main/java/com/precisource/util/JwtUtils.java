package com.precisource.util;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.precisource.consts.DefaultConsts;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xinput
 * @Date: 2020-06-15 15:35
 */
public class JwtUtils {

    public static String secret = DefaultConsts.get("application.secret");

    public static String tokenExp = DefaultConsts.get("token.exp", "3600");

    public static String sign(String aud) {
        final String jwt = sign(aud, new HashMap());
        return jwt;
    }

    public static String sign(Object aud) {
        final String jwt = sign(aud, new HashMap());
        return jwt;
    }

    public static String sign(Object aud, Map<String, Object> claims) {
        // issued at claim
        final long iat = System.currentTimeMillis() / 1000L;
        // expires claim. In this case the token expires in 60 * 60 seconds
        final long exp = iat + Long.valueOf(tokenExp);
        final JWTSigner signer = new JWTSigner(secret);
        HashMap<String, Object> signClaims = new HashMap<>();
        signClaims.put("aud", aud);
        signClaims.put("exp", exp);
        signClaims.put("iat", iat);
        signClaims.putAll(claims);
        final String jwt = signer.sign(signClaims);
        return jwt;
    }

    public static Map verify(String jwt) throws SignatureException, NoSuchAlgorithmException, JWTVerifyException, InvalidKeyException, IOException {
        final JWTVerifier verifier = new JWTVerifier(secret);
        final Map<String, Object> claims = verifier.verify(jwt);
        return claims;
    }
}
