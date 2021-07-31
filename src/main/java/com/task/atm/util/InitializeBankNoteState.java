package com.task.atm.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages the atm banknotes(bills) state and total available money.
 */
@Getter
@Setter
@Component
public final class InitializeBankNoteState {

    private InitializeBankNoteState(){}

    public static Map<Integer,Integer> banknoteState = new HashMap<>();

    static {
        banknoteState.put(50,10);
        banknoteState.put(20,30);
        banknoteState.put(10,30);
        banknoteState.put(5,20);
        banknoteState.put(0,1500);
    }
}
