package br.app.cashew.feature03.cafeteria.controller;

import br.app.cashew.feature03.cafeteria.model.Cafeteria;
import br.app.cashew.feature03.cafeteria.service.CafeteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cafeterias")
public class CafeteriaController {

    private final CafeteriaService cafeteriaService;

    @Autowired
    public CafeteriaController(CafeteriaService cafeteriaService) {

        this.cafeteriaService = cafeteriaService;
    }

    @PostMapping
    public ResponseEntity<Cafeteria> createCafeteria(@RequestBody Cafeteria cafeteria, Authentication authentication) {

        Cafeteria response = cafeteriaService.createCafeteria(cafeteria, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/{cafeteriaPublicKey}")
    public ResponseEntity<Cafeteria> getCafeteria (@PathVariable String cafeteriaPublicKey) {

        Cafeteria cafeteria = cafeteriaService.getCafeteria(cafeteriaPublicKey);

        return new ResponseEntity<>(cafeteria, HttpStatus.OK);
    }
}
