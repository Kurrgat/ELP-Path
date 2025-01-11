package com.example.emtechelppathbackend.utils;

import java.util.Base64;

public class Base64Utils {

        public static String encode(byte[] fileContent) {
            return Base64.getEncoder().encodeToString(fileContent);
        }

        public static byte[] decode(String base64String) {
            return Base64.getDecoder().decode(base64String);
        }


}
