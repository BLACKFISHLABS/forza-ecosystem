package io.github.blackfishlabs.forza.core.helper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class QuickPasswordEncodingGenerator {

    public static void main(String[] args) {
        String password = "blackfish@labs";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);
        System.out.println(passwordEncoder.encode(password));
    }
}
