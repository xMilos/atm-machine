package com.task.atm.controller;

import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import static com.task.atm.util.InitializeBankNoteState.banknoteState;

@RequestMapping("/api/atm")
@Api(tags = "Atm endpoints")
public class AtmController {

    @GetMapping("/status")
    public ResponseEntity<String> getAtmStatus(){
        return ResponseEntity.ok(banknoteState.toString());
    }
}
