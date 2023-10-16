package br.app.cashew.feature03.cafeteria.controller;

import br.app.cashew.feature03.cafeteria.dto.campus.output.CampusUserRegistrationDto;
import br.app.cashew.feature03.cafeteria.model.Campus;
import br.app.cashew.feature03.cafeteria.model.University;
import br.app.cashew.feature03.cafeteria.service.UniversityService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/universities")
public class UniversityController {

    private final UniversityService universityService;
    private final ModelMapper modelMapper;

    @Autowired
    public UniversityController(UniversityService universityService, ModelMapper modelMapper) {

        this.universityService = universityService;
        this.modelMapper = modelMapper;
    }

    // cadastro usuario
    @GetMapping()
    public ResponseEntity<List<University>> getUniversities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String acronym,
            @RequestParam(required = false) String nameOrAcronym) {

        if (name == null && acronym == null && nameOrAcronym == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (name != null && acronym != null) {
            List<University> universities = Collections.singletonList(universityService.getUniversity(name, acronym));
            return new ResponseEntity<>(universities, HttpStatus.OK);
        }

        List<University> universities = universityService.getUniversities(nameOrAcronym);
        return new ResponseEntity<>(universities, HttpStatus.OK);
    }

    // cadastro usuario
    @GetMapping("/{universityPublicKey}/campuses")
    public ResponseEntity<List<CampusUserRegistrationDto>> getCampusesFromUniversity(@PathVariable String universityPublicKey) {

        List<Campus> campuses = universityService.getCampuses(universityPublicKey);

        List<CampusUserRegistrationDto> response = campuses.stream().map(this::convertCampusToDto).toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // cadastro dono
    @PostMapping()
    public ResponseEntity<University> createUniversity(@RequestBody @Valid University university) {
        return new ResponseEntity<>(universityService.createUniversity(university), HttpStatus.CREATED);
    }

    // cadastro usuario
    private CampusUserRegistrationDto convertCampusToDto(Campus campus) {
        return modelMapper.map(campus, CampusUserRegistrationDto.class);
    }
}
