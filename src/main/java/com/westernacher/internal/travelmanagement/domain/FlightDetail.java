package com.westernacher.internal.travelmanagement.domain;

import lombok.Data;

import java.util.Date;

@Data
public class FlightDetail {
    private Date fromdate;
    private Date todate;
    private String sector1;
    private String sector2;
    private String billability;
}
