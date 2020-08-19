package com.westernacher.internal.travelmanagement.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "wizard")
public class Wizard {

    @Id
    private String id;
    private ProjectDetail projectDetail;
    private CabDetail cabDetail;
    private TravelType travelType;
    private BusDetail busDetail;
    private TrainDetail trainDetail;
    private FlightDetail flightDetail;
    private HotelDetail hotelDetail;
    private String pmEmail;
    private WizardStatus status;
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date submittedOn;
    private Date updatedDate;
}
