package ru.otus.spring.config;

import java.util.Locale;

public interface AppPropsConfig {
    void setLocale(String locale);

    Locale getLocale();
}
