package com.westernacher.internal.travelmanagement.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
public class Client {
    private String name;
    private String address;
    private String billability;
}
