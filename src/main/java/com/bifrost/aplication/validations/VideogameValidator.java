package com.bifrost.aplication.validations;

import com.bifrost.aplication.annotations.ValidVideogame;
import com.bifrost.aplication.domain.VideogameEntity;

/**
 * Class to validate Bifrost videogame
 */
public class VideogameValidator extends BifrostValidator<ValidVideogame, VideogameEntity> {

    @Override
    public boolean validBifrostEntity(VideogameEntity videogameEntity) {
        return true;
    }
}
