package com.westernacher.internal.travelmanagement.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(collection = "wizard")
public class Wizard {

    @Id
    private String id;
    private List<ProjectDetail> projectDetails;
    private List<CabDetail> cabDetails;
    private TravelType travelType;
    private List<BusDetail> busDetails;
    private List<TrainDetail> trainDetails;
    private List<FlightDetail> flightDetails;
    private List<HotelDetail> hotelDetails;
    private List<InsuranceDetail> insuranceDetails;
    private List<VisaDetail> visaDetails;
    private String pmEmail;
    private WizardStatus status;
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date submittedOn;
    private Date updatedDate;
}
