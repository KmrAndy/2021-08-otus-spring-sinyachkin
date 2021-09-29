package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.config.AppPropsConfig;

@Service
public class FilenameServiceImpl implements FilenameService{
    private AppPropsConfig config;

    public FilenameServiceImpl(AppPropsConfig config) {
        this.config = config;
    }

    public String getFilename() {
        if (config.getLocale().getLanguage().equals("ru")) {
            return config.getFilename() + "_" + config.getLocale().getLanguage() + "." + config.getFileExtension();
        } else {
            return config.getFilename() + "." + config.getFileExtension();
        }
    }
}