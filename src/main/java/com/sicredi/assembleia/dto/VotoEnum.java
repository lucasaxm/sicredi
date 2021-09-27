package com.sicredi.assembleia.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VotoEnum {
    SIM, NAO;

    @JsonCreator
    public static VotoEnum fromString(String key) {
        for(VotoEnum type : VotoEnum.values()) {
            if(type.name().equalsIgnoreCase(key)) {
                return type;
            }
        }
        return null;
    }
}
