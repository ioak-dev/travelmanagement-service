package com.westernacher.internal.travelmanagement.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "project")
public class Project {

    @Id
    private String projectid;
    private String name;
    private Date startDate;
    private Date endDate;
    private String customerName ;
}
