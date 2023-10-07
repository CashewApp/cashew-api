package br.app.cashew.feature03.cafeteria.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class CampusUserRegistrationDto {

    private int campusID;
    private String name;
    private UUID publicKey;
}
