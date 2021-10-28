package ru.otus.spring.config;

import java.util.Locale;

public interface AppPropsConfig {
    void setFilename(String filename);

    void setFileExtension(String fileExtension);

    void setLocale(String locale);

    String getFilename();

    Locale getLocale();

    String getFileExtension();
}
