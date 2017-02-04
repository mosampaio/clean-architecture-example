package com.mosampaio.webapp.core;

import com.google.inject.AbstractModule;
import com.mosampaio.adapters.repository.UserInMemoryRepository;
import com.mosampaio.domain.repository.UserRepository;
import com.mosampaio.adapters.web.WebRouter;
import ru.vyarus.guice.validator.ImplicitValidationModule;
import com.mosampaio.usecases.LogIn;
import com.mosampaio.usecases.SignUp;

public class CoreModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ImplicitValidationModule());

        bind(UserRepository.class).to(UserInMemoryRepository.class).asEagerSingleton();
        bind(LogIn.class).asEagerSingleton();;
        bind(SignUp.class).asEagerSingleton();;
        bind(WebRouter.class).asEagerSingleton();;
    }
}
