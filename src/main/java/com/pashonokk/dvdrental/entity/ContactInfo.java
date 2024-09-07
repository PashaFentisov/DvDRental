package com.pashonokk.dvdrental.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@ToString
public class ContactInfo {
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private String username;
}
