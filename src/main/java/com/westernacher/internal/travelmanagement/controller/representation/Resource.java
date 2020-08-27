package com.westernacher.internal.travelmanagement.controller.representation;

import com.westernacher.internal.travelmanagement.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.*;

public class Resource {
    /*@Data
    @AllArgsConstructor
    @Builder
    public static class WizardResource {
        @Id
        private String id;
        private WizardStatus status;
        private String createdBy;
        private String applicant;
        private TravelDetail traveltype;
        private PurposeOfVisit purposeofvisit;
        private FlightDetail flightdetails;
        private HotelDetail hoteldetails;
        private Visa visa;
        private Review review;
        private Date creationDate;
        private Date submittedOn;

    }*/

    @Data
    @AllArgsConstructor
    @Builder
    public static class WizardResource {
        @Id
        private String id;
        private String applicant;
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

    public static List<WizardResource> wizardConverter(List<Wizard> wizards, List<Person> persons) {

        Map<String, String> personMap = new HashMap<>();
        persons.stream().forEach(person -> {
            personMap.put(person.getId(), person.getName());
        });
        List<WizardResource> wizardResources = new ArrayList<>();
        wizards.stream().forEach(wizard -> {
            wizardResources.add(
                    WizardResource.builder()
                            .id(wizard.getId())
                            .applicant(personMap.get(wizard.getCreatedBy()))
                            .projectDetails(wizard.getProjectDetails())
                            .cabDetails(wizard.getCabDetails())
                            .travelType(wizard.getTravelType())
                            .busDetails(wizard.getBusDetails())
                            .trainDetails(wizard.getTrainDetails())
                            .flightDetails(wizard.getFlightDetails())
                            .hotelDetails(wizard.getHotelDetails())
                            .insuranceDetails(wizard.getInsuranceDetails())
                            .visaDetails(wizard.getVisaDetails())
                            .pmEmail(wizard.getPmEmail())
                            .status(wizard.getStatus())
                            .createdBy(wizard.getCreatedBy() != null ? wizard.getCreatedBy() : null)
                            .updatedBy(wizard.getUpdatedBy()!= null ? wizard.getUpdatedBy() : null)
                            .createdDate(wizard.getCreatedDate()!= null ? wizard.getCreatedDate() : null)
                            .submittedOn(wizard.getSubmittedOn()!= null ? wizard.getSubmittedOn() : null)
                            .updatedDate(wizard.getUpdatedDate()!= null ? wizard.getUpdatedDate() : null)
                    .build()
                    );
        });
        return wizardResources;
    }
}
