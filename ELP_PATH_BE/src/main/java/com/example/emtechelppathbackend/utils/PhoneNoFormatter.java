package com.example.emtechelppathbackend.utils;

public class PhoneNoFormatter {
    public static  String formatPhoneNo(String phoneNo) {
        phoneNo = phoneNo.replaceAll("[^\\d]", "");

        if(phoneNo.startsWith("0")) {
            phoneNo = "+254" + phoneNo.substring(1);
        } else if(phoneNo.startsWith("+254")) {
            phoneNo = "+254" + phoneNo.substring(1);
        } else if(phoneNo.startsWith("7") || phoneNo.startsWith("1")) {
            phoneNo = "+254" + phoneNo.substring(1);
        }
        return  phoneNo;
    }
}
