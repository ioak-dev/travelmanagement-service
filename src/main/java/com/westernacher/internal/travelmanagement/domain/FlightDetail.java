package com.westernacher.internal.travelmanagement.domain;

import lombok.Data;

import java.util.Date;

@Data
public class FlightDetail {
    private String source;
    private String destination;
    private String comment;
}
