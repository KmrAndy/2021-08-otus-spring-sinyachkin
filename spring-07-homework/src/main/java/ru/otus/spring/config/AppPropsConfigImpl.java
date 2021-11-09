package ru.otus.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Locale;

@ConfigurationProperties(prefix="library")
@Component
public class AppPropsConfigImpl implements AppPropsConfig {
    private Locale locale;

    public void setLocale(String locale){ this.locale = new Locale(locale); }

    public Locale getLocale(){
        return this.locale;
    }
}
