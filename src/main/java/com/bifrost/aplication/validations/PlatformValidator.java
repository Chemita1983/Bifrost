package com.bifrost.aplication.validations;

import com.bifrost.aplication.annotations.ValidPlatform;
import com.bifrost.aplication.domain.PlatformEntity;

public class PlatformValidator extends BifrostValidator<ValidPlatform, PlatformEntity> {

    @Override
    public boolean validBifrostEntity(PlatformEntity platformEntity) {
        return true;
    }
}
