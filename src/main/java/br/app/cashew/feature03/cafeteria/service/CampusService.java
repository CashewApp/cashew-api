package br.app.cashew.feature03.cafeteria.service;

import br.app.cashew.feature03.cafeteria.exception.campus.CampusAlreadyExistsException;
import br.app.cashew.feature03.cafeteria.exception.university.UniversityDoesNotExistsException;
import br.app.cashew.feature03.cafeteria.model.Campus;
import br.app.cashew.feature03.cafeteria.model.University;
import br.app.cashew.feature03.cafeteria.repository.CampusRepository;
import br.app.cashew.feature03.cafeteria.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CampusService {

    private final UniversityRepository universityRepository;
    private final CampusRepository campusRepository;

    @Autowired
    public CampusService(UniversityRepository universityRepository, CampusRepository campusRepository) {

        this.universityRepository = universityRepository;
        this.campusRepository = campusRepository;
    }
    public Campus createCampusByPublicKey(Campus campus) {

        UUID universityPublicKey = campus.getUniversity().getUniversityPublicKey();
        University university = validateUniversityByPublicKey(universityPublicKey);
        campus.setUniversity(university);
        boolean campusAlreadyExists = campusRepository.existsByNameAndUniversity(campus.getName(), university);
        if (campusAlreadyExists) {
            throw new CampusAlreadyExistsException("Campus já existe", "campus");
        }

        campus.setPublicKey(UUID.randomUUID());
        return campusRepository.save(campus);
    }

    public Campus createCampusById(Campus campus) {

        int id = campus.getUniversity().getUniversityID();
        University university = validateUniversityById(id);

        boolean campusAlreadyExists = campusRepository.existsByNameAndUniversity(campus.getName(), university);
        if (campusAlreadyExists) {
            throw new CampusAlreadyExistsException("Campus já existe", "campus");
        }
        campus.setUniversity(university);
        campus.setPublicKey(UUID.randomUUID());
        campusRepository.save(campus);
        return campus;
    }

    private University validateUniversityByPublicKey(UUID universityPublicKey) {
        return universityRepository.findByUniversityPublicKey(universityPublicKey)
                .orElseThrow(() -> new UniversityDoesNotExistsException("Universidade nao existe", "university"));
    }

    private University validateUniversityById(int id) {
        return universityRepository.findById(id)
                .orElseThrow(() -> new UniversityDoesNotExistsException("Universidade nao existe", "university"));
    }

    /*public Campus getCampus(String name, String universityPublicKey) {
        UUID uuid = UUID.fromString(universityPublicKey);

        University university = universityRepository.findByUniversityPublicKey(uuid)
                .orElseThrow(() -> new UniversityDoesNotExistsException("Universidade nao existe", "university"));

       return campusRepository.findByNameAndUniversity(name, university)
               .orElseThrow(() -> new CampusDoesNotExistsException("Campus nao existe", "campus"));
    }*/
}
