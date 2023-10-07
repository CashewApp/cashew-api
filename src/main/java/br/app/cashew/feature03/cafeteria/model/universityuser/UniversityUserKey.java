package br.app.cashew.feature03.cafeteria.model.universityuser;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UniversityUserKey implements Serializable {

    @Column(name = "universityID")
    public int universityID;

    @Column(name = "userID")
    public int userID;
}
