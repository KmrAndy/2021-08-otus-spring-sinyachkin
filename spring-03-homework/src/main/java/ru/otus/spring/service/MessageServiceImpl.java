package ru.otus.spring.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.AppPropsConfig;

@Service
public class MessageServiceImpl implements MessageService{
    private AppPropsConfig config;
    private final MessageSource messageSource;

    public MessageServiceImpl(MessageSource messageSource, AppPropsConfig config) {
        this.config = config;
        this.messageSource = messageSource;
    }

    public String getMessage(String messageCode, String[] args) {
        return messageSource.getMessage(messageCode, args, config.getLocale());
    }

    public String getMessage(String messageCode) {
        return getMessage(messageCode, (String[]) null);
    }

    public String getMessage(String messageCode, String arg){
        return getMessage(messageCode, new String[]{arg});
    }
}
