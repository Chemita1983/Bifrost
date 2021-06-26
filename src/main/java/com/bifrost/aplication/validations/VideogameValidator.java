package com.bifrost.aplication.validations;

import com.bifrost.aplication.annotations.ValidVideogame;
import com.bifrost.aplication.domain.Videogame;

/**
 * Class to validate Bifrost videogame
 */
public class VideogameValidator extends BifrostValidator<ValidVideogame, Videogame> {

    @Override
    public boolean validBifrostEntity(Videogame videogame) {
        return false;
    }
}
