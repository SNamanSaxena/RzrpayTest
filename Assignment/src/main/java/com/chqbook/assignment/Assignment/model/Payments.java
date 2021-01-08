package com.chqbook.assignment.Assignment.model;

import com.chqbook.assignment.Assignment.common.DateOps;
import com.chqbook.assignment.Assignment.common.IdGenerator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document(collection = "Payments")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Payments {

    @Id
    private String _id = IdGenerator.getId();
    private Date createdAt = DateOps.GetCurrentTimeUTC();
    private Date updatedAt = DateOps.GetCurrentTimeUTC();
    @JsonProperty("id")
    private String payment_Id;
    private String order_id;
    private String currency = "INR";
    private Number amount;
    @JsonProperty("method")
    private String paymentMode;
    private String status;
    @JsonProperty("captured")
    private Boolean isCaptured;
    private Number fee;
    private Number tax;

    public Payments(JSONObject input) {
        this.payment_Id = (String) input.get("id");
        this.order_id = (String) input.get("order_id");
        this.amount = (Number) input.get("amount");
        this.paymentMode = (String) input.get("method");
        this.status = (String) input.get("status");
        this.isCaptured = (Boolean) input.get("captured");
        this.fee = (Number) input.get("fee");
        this.tax = (Number) input.get("tax");
    }
}
