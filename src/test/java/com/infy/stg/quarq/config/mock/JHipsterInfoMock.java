package com.infy.stg.quarq.config.mock;

import com.infy.stg.quarq.config.JHipsterInfo;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class JHipsterInfoMock implements JHipsterInfo {

    public static Boolean enable;

    @Override
    public Boolean isEnable() {
        return enable;
    }
}
