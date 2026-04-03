package com.devpulse.backend.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public class TechnologyRequest
{
    @NotBlank
    private String name;

    @NotBlank
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
