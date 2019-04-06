package com.westernacher.internal.travelmanagement.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class Wizard {
    private TravelType travelType;
    private Client clientDetail;
}
