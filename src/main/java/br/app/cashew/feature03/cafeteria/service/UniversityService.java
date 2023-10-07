package br.app.cashew.feature03.cafeteria.service;

import br.app.cashew.feature03.cafeteria.exception.university.UniversityAlreadyExistsException;
import br.app.cashew.feature03.cafeteria.exception.university.UniversityDoesNotExistsException;
import br.app.cashew.feature03.cafeteria.model.Campus;
import br.app.cashew.feature03.cafeteria.model.University;
import br.app.cashew.feature03.cafeteria.repository.CampusRepository;
import br.app.cashew.feature03.cafeteria.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final CampusRepository campusRepository;

    @Autowired
    public UniversityService(UniversityRepository universityRepository, CampusRepository campusRepository) {

        this.universityRepository = universityRepository;
        this.campusRepository = campusRepository;
    }

    public List<University> getUniversities(String nameOrAcronym) {

        return universityRepository.findByNameStartsWithOrAcronymStartsWith(nameOrAcronym, nameOrAcronym);
    }

    public University getUniversity(String name, String acronym) {
        return universityRepository.findByNameAndAcronym(name, acronym)
                .orElseThrow(() -> new UniversityDoesNotExistsException("Universidade nao existe", "university"));
    }

    public List<Campus> getCampuses(String universityUserPublicKey) {

        UUID uuid = UUID.fromString(universityUserPublicKey);
        University university = universityRepository.findByUniversityPublicKey(uuid)
                .orElseThrow(() -> new UniversityDoesNotExistsException("Universidade nao existe", "university"));

        return campusRepository.findByUniversity(university);
    }

    public University createUniversity(University university) {

        if (universityRepository.existsByNameAndAcronym(university.getName(), university.getAcronym())) {
            throw new UniversityAlreadyExistsException("Universidade ja existe", "university");
        }
        university.setUniversityPublicKey(UUID.randomUUID());
        universityRepository.save(university);

        return university;
    }
}
