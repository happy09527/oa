package com.example.security.custom;

import com.example.common.utils.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author: ZhangX
 * @createDate: 2023/6/16
 * @description:
 */
@Component
public class CustomMd5PasswordEncoder implements PasswordEncoder {
    public String encode(CharSequence rawPassword) {
        return MD5Util.encrypt(rawPassword.toString());
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5Util.encrypt(rawPassword.toString()));
    }
}
