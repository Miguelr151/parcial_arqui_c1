package com.iglesia;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ChurchUtils {

    public static Church requireChurch(ChurchRepository churchRepository) {

        return churchRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Debe registrar una iglesia primero"
                        ));
    }
}