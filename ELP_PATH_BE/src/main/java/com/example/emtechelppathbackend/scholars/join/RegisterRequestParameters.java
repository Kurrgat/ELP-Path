package com.example.emtechelppathbackend.scholars.join;

import com.example.emtechelppathbackend.branch.Branch;
import lombok.Data;

@Data
public class RegisterRequestParameters {
    private String searchResult;
    private Long scholarId;
    private String action;
    private String firstName;
    private String lastName;
}
