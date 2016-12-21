package com.ricamgar.challenge.domain.repository;


import com.ricamgar.challenge.domain.model.LoginRequest;
import com.ricamgar.challenge.domain.model.LoginResponse;

import rx.Single;


public interface LoginRepository {
    Single<LoginResponse> login(LoginRequest loginRequest);
}
