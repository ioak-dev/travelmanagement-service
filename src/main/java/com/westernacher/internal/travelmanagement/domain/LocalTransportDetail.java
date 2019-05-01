package com.westernacher.internal.travelmanagement.domain;

import lombok.Data;

import java.util.Date;

@Data
public class LocalTransportDetail {
    private Date dateandtime;
    private Date dateandtimereturn;
    private String sector1;
    private String sector2;
    private String billability;
    private String remarks1;
    private String remarks2;
}
