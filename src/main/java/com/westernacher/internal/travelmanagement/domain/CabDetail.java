package com.westernacher.internal.travelmanagement.domain;

import lombok.Data;

import java.util.Date;

@Data
public class CabDetail {
    private String source;
    private String destination;
    private String comment;
}
