package com.example.emtechelppathbackend.education;

import lombok.Getter;

@Getter
public enum InstitutionCategory {
    GLOBAL_TVET("GLOBAL_TVET"),
    GLOBAL_UNIVERSITY("GLOBAL_UNIVERSITY"),
    LOCAL_TVET("LOCAL_TVET"),
    LOCAL_UNIVERSITY("LOCAL_UNIVERSITY"),
    REGIONAL_TVET("REGIONAL_TVET"),
    REGIONAL_UNIVERSITY("REGIONAL_UNIVERSITY");

    private final String description;
    InstitutionCategory(String description) {
        this.description = description;
    }
}
