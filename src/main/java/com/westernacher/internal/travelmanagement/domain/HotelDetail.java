package com.westernacher.internal.travelmanagement.domain;

import lombok.Data;

import java.util.Date;

@Data
public class HotelDetail {
    private Date fromdate;
    private Date todate;
    private String name;
    private String staycost;
    private String billability;
    private String remarks;
}
