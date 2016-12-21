package com.ricamgar.challenge.domain.usecase;


import com.ricamgar.challenge.domain.model.LoginRequest;
import com.ricamgar.challenge.domain.model.LoginResponse;
import com.ricamgar.challenge.domain.repository.LoginRepository;

import rx.Single;


public class LoginUseCase {


    public LoginUseCase(LoginRepository loginRepository) {

    }

    public Single<LoginResponse> login(LoginRequest invalidLoginRequest) {
        return null;
    }
}
