package com.chqbook.assignment.Assignment.model;

import com.chqbook.assignment.Assignment.common.DateOps;
import com.chqbook.assignment.Assignment.common.IdGenerator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Orders")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Orders {

    @Id
    private String _id = IdGenerator.getId();
    private Date createdAt = DateOps.GetCurrentTimeUTC();
    private Date updatedAt = DateOps.GetCurrentTimeUTC();
    @JsonProperty("id")
    private String order_Id;
    private String currency = "INR";
    private Number amount;
    private Number amount_paid;
    private Number amount_due;
    private String receipt;
}
