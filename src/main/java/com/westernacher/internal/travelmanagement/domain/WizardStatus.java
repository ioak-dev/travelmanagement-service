package com.westernacher.internal.travelmanagement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WizardStatus {
    DRAFT("DRAFT", "Draft"),
    L1("L1", "Pending with L1"),
    L2("L2", "Pending with L2"),
    COMPLETE("COMPLETE", "Completed");

    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    private WizardStatus(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
