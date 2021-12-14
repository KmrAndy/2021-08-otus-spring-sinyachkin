package ru.otus.spring.provider;

import org.springframework.stereotype.Component;
import ru.otus.spring.config.AppPropsConfig;

import java.util.Locale;

@Component
public class LocaleProviderImpl implements LocaleProvider{
    private final Locale locale;

    public LocaleProviderImpl(AppPropsConfig config){
        this.locale = config.getLocale();
    }

    public Locale getLocale(){
        return this.locale; }
}
