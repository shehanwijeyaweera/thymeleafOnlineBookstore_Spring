package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model;

import javax.persistence.*;

@Entity
@Table(name = "complaint")
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Complaint_id;
    private String message;
    private String status;

    public Complaint() {
    }

    public Complaint(Long complaint_id, String message, String status) {
        Complaint_id = complaint_id;
        this.message = message;
        this.status = status;
    }

    public Long getComplaint_id() {
        return Complaint_id;
    }

    public void setComplaint_id(Long complaint_id) {
        Complaint_id = complaint_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
