package com.westernacher.internal.travelmanagement.controller.representation;

import com.westernacher.internal.travelmanagement.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.*;

public class Resource {
    @Data
    @AllArgsConstructor
    @Builder
    public static class WizardResource {
        @Id
        private String id;
        private WizardStatus status;
        private String createdBy;
        private String applicant;
        private TravelDetail traveltype;
        private ClientInfo clientinfo;
        private PurposeOfVisit purposeofvisit;
        private FlightDetail flightdetails;
        private HotelDetail hoteldetails;
        private LocalTransportDetail localtransportdetails;
        private Visa visa;
        private Review review;
        private Date creationDate;
        private Date submittedOn;

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
                    .status(wizard.getStatus())
                    .createdBy(wizard.getCreatedBy())
                    .applicant(personMap.get(wizard.getCreatedBy()))
                    .traveltype(wizard.getTraveltype())
                    .clientinfo(wizard.getClientinfo())
                    .purposeofvisit(wizard.getPurposeofvisit())
                    .flightdetails(wizard.getFlightdetails())
                    .hoteldetails(wizard.getHoteldetails())
                    .localtransportdetails(wizard.getLocaltransportdetails())
                    .visa(wizard.getVisa())
                    .review(wizard.getReview())
                    .creationDate(wizard.getCreationDate())
                    .submittedOn(wizard.getSubmittedOn())
                    .build()
                    );
        });
        return wizardResources;
    }
}
