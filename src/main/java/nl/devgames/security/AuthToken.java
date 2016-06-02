package nl.devgames.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wouter on 5/19/2016.
 */
public class AuthToken {
    private AuthToken() {
    }

    private static Map<String,String> tokenMap = new HashMap<>();
    public static String generate(String username){
        Key key = MacProvider.generateKey();
        String token = Jwts.builder().setSubject(username).signWith(SignatureAlgorithm.HS512, key).compact();
        tokenMap.put(token,username);
        return token;
    }
    public static void invalidate(String token) {
        tokenMap.remove(token);
    }

    public static boolean checkToken(String token){
        return tokenMap.containsKey(token);
    }
    public static String getUsernameFromToken(String username){
        return tokenMap.get(username);
    }
}
