package com.bifrost.aplication.validations;

import com.bifrost.aplication.annotations.ValidPlatform;
import com.bifrost.aplication.domain.Platform;

public class PlatformValidator extends BifrostValidator<ValidPlatform, Platform> {

    @Override
    public boolean validBifrostEntity(Platform platform) {
        return validBifrostEntity(platform);
    }
}
