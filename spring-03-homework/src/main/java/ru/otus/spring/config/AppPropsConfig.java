package ru.otus.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Locale;

@ConfigurationProperties(prefix="question")
@Component
public class AppPropsConfig {
    private String filename;
    private String fileExtension;
    private Locale locale;

    public void setFilename(String filename){
        this.filename = filename;
    }

    public void setFileExtension(String fileExtension){
        this.fileExtension = fileExtension;
    }

    public void setLocale(String locale){ this.locale = new Locale(locale); }

    public String getFilename(){ return this.filename; }

    public Locale getLocale(){
        return this.locale;
    }

    public String getFileExtension(){
        return this.fileExtension;
    }
}
