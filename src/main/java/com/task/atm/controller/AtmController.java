package com.task.atm.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.task.atm.util.InitializeBankNoteState.banknoteState;

/**
 * Atm controller endpoints for testing purposes
 */
@Slf4j
@RestController
@RequestMapping("/api/atm")
@Api(tags = "Atm endpoints")
public class AtmController {

    /**
     * Get atm status response entity.
     *
     * @return the response entity
     */
    @GetMapping("/status")
    public ResponseEntity<String> getAtmStatus(){

        log.info("getting status of ATM");
        return ResponseEntity.ok(banknoteState.toString());
    }

    /**
     * Insert bills in atm response entity.
     *
     * @param fifty  the fifty
     * @param twenty the twenty
     * @param ten    the ten
     * @param five   the five
     * @return the response entity
     */
    @PostMapping("/insert")
    public ResponseEntity<String> insertBillsInAtmAndReturnState(@RequestParam int fifty, @RequestParam int twenty,
                                                   @RequestParam int ten, @RequestParam int five){

        log.info("inserting bills to ATM");
        banknoteState.put(50, banknoteState.get(50) + fifty);
        banknoteState.put(20, banknoteState.get(20) + twenty);
        banknoteState.put(10, banknoteState.get(10) + ten);
        banknoteState.put(5, banknoteState.get(5) + five);
        banknoteState.put(0, banknoteState.get(0) + fifty*50+twenty*20+ten*10+five*5);
        return ResponseEntity.ok(banknoteState.toString());
    }
}
