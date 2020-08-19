package com.westernacher.internal.travelmanagement.domain;

import lombok.Data;

import java.util.Date;

@Data
public class HotelDetail {
    private Date checkinDate;
    private Date checkoutDate;
    private String location;
    private String comment;
}
