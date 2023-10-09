package br.app.cashew.feature02.user.dto.user.output;

import br.app.cashew.feature03.cafeteria.model.University;
import br.app.cashew.feature03.cafeteria.model.universityuser.UniversityUserKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UniversityUserResponseDTO {

    private UniversityUserKey universityUserID;
    private University university;
    private CampusDTO campus;
}
