package ru.otus.spring.service;

import ru.otus.spring.model.Waiter;

public interface WaiterService {
    Waiter addWaiter(String firstName, String lastName, String phone, String restaurant);

    Waiter getWaiterById(String id);

    Waiter getWaiterByPhone(String phone);
}
