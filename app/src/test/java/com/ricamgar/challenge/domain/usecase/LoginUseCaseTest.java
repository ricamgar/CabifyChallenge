package com.ricamgar.challenge.domain.usecase;

import com.ricamgar.challenge.domain.model.LoginRequest;
import com.ricamgar.challenge.domain.model.LoginResponse;
import com.ricamgar.challenge.domain.repository.LoginRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.reactivex.Single;

import static org.mockito.Mockito.when;

public class LoginUseCaseTest {

    @Mock
    LoginRepository loginRepository;

    LoginUseCase loginUseCase;

    @Before
    public void setUp() throws Exception {
        loginUseCase = new LoginUseCase(loginRepository);
    }

    @Test
    public void shouldThrowErrorWhenUserOrPasswordNotValid() throws Exception {
        LoginRequest invalidLoginRequest = createInvalidLoginRequest();
        when(loginRepository.login(invalidLoginRequest))
                .thenReturn(Single.<LoginResponse>error(new IllegalArgumentException("Invalid user or password")));

        loginUseCase.login(invalidLoginRequest)
                .test()
                .assertError(IllegalArgumentException.class);
    }

    private LoginRequest createInvalidLoginRequest() {
        return new LoginRequest(null, "anyUsername", "anyPassword", "password");
    }
}