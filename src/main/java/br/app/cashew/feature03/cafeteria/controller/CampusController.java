package br.app.cashew.feature03.cafeteria.controller;

import br.app.cashew.feature03.cafeteria.model.Cafeteria;
import br.app.cashew.feature03.cafeteria.model.Campus;
import br.app.cashew.feature03.cafeteria.service.CampusService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/campuses")
public class CampusController {

    private final CampusService campusService;

    public CampusController(CampusService campusService) {

        this.campusService = campusService;
    }

    @PostMapping
    public ResponseEntity<Campus> createCampus(@RequestBody @Valid Campus campus, @RequestParam(defaultValue = "id") String type) {

        if (type.equals("publicKey")) return new ResponseEntity<>(campusService.createCampusByPublicKey(campus), HttpStatus.CREATED);

        return new ResponseEntity<>(campusService.createCampusById(campus), HttpStatus.CREATED);
    }

    @GetMapping("/{campusPublicKey}/cafeterias")
    public ResponseEntity<List<Cafeteria>> getCafeteriasFromCampus(@PathVariable String campusPublicKey) {

        UUID campusUuid = UUID.fromString(campusPublicKey);
        campusService.getCafeteriasFromCampus(campusUuid);
        return new ResponseEntity<>(campusService.getCafeteriasFromCampus(campusUuid), HttpStatus.OK);
    }
}
