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
    private WizardStatus status;
    private String createdBy;
    private TravelDetail traveltype;
    private ClientInfo clientinfo;
    private PurposeOfVisit purposeofvisit;
    private FlightDetail flightdetails;
    private HotelDetail hoteldetails;
    private LocalTransportDetail localtransportdetails;
    private Visa visa;
    private Review review;
    private Date creationDate;
}
