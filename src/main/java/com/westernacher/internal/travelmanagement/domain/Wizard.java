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
    private ProjectDetail projectDetails;
    private CabDetail cabDetails;
    private TravelType travelType;
    private BusDetail busDetails;
    private TrainDetail trainDetails;
    private FlightDetail flightDetails;
    private HotelDetail hotelDetails;
    private InsuranceDetail insuranceDetails;
    private VisaDetail visaDetails;
    private String pmEmail;
    private WizardStatus status;
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date submittedOn;
    private Date updatedDate;
}
