package com.example.emtechelppathbackend.utils;

public class ScholarNameConfirm {

    public static boolean checkName(String fullName, String firstName, String middleName, String lastName) {
        int count = 0;
        String[] names = fullName.split("\\s+");

        for(String name:names) {
            if(name.equalsIgnoreCase(firstName) || name.equalsIgnoreCase(middleName) || name.equalsIgnoreCase(lastName)) {
                count ++;
            }
        }
        return count >= 2;
    }
}
