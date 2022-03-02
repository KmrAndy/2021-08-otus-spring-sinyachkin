package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.NoUserFoundException;
import ru.otus.spring.exception.NoWaiterFoundException;
import ru.otus.spring.exception.OtherAccessException;
import ru.otus.spring.model.Waiter;
import ru.otus.spring.repository.WaiterRepository;

@Service
public class WaiterServiceImpl implements WaiterService{
    private final WaiterRepository waiterRepository;

    public WaiterServiceImpl(WaiterRepository waiterRepository){
        this.waiterRepository = waiterRepository;
    }

    @Transactional
    public Waiter addWaiter(String firstName, String lastName, String phone, String restaurant){
        try {
            return waiterRepository.save(new Waiter(firstName, lastName, phone, restaurant));
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    public Waiter getWaiterById(String id){
        return waiterRepository.findById(id)
                .orElseThrow(() -> new NoWaiterFoundException("Waiter not found"));
    }

    public Waiter getWaiterByPhone(String phone){
        return waiterRepository.findWaiterByPhone(phone)
                .orElseThrow(() -> new NoUserFoundException("Waiter not found"));
    }
}
