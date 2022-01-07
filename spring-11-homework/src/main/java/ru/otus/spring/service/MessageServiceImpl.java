package ru.otus.spring.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.provider.LocaleProvider;

import java.util.Locale;

@Service
public class MessageServiceImpl implements MessageService{
    private final Locale locale;
    private final MessageSource messageSource;

    public MessageServiceImpl(MessageSource messageSource, LocaleProvider localeProvider) {
        this.locale = localeProvider.getLocale();
        this.messageSource = messageSource;
    }

    public String getMessage(String messageCode, String ... args) {
        return messageSource.getMessage(messageCode, args, locale);
    }

    public String getMessage(String messageCode) {
        return getMessage(messageCode, (String[]) null);
    }
}

