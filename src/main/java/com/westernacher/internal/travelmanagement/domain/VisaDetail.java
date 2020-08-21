package com.westernacher.internal.travelmanagement.domain;

import lombok.Data;

@Data
public class VisaDetail {
    private String country;
    private String visaType;
    private String entryType;
    private String comment;
}
