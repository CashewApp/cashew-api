package br.app.cashew.feature03.cafeteria.dto.cafeteria.output;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CafeteriaDto {
    private int cafeteriaID;
    private String name;
    private int averageRating;
    private String photoUrl;
    private UUID publicKey;
    private String university;
}
