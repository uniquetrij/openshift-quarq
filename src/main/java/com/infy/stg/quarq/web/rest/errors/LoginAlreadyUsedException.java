package com.infy.stg.quarq.web.rest.errors;

public class LoginAlreadyUsedException extends BadRequestAlertException {

    public LoginAlreadyUsedException() {
        super("Login name already used!", "userManagement", "userexists");
    }
}
