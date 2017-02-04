package com.mosampaio.webapp.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mosampaio.adapters.web.WebRouter;
import lombok.extern.slf4j.Slf4j;
import com.mosampaio.webapp.profile.AcceptanceTestsRouter;

@Slf4j
public class WebApp {

    public static void main(String[] args) {
        Injector coreInjector = Guice.createInjector(new CoreModule());
        coreInjector.getInstance(WebRouter.class).register();

        if (isAcceptanceTestProfileEnabled()) {
            coreInjector.getInstance(AcceptanceTestsRouter.class).register();

            log.info("acceptanceTests profile loaded!");
        }
    }

    private static boolean isAcceptanceTestProfileEnabled() {
        return Boolean.TRUE.toString().equalsIgnoreCase(System.getProperty("acceptanceTests"));
    }
}
