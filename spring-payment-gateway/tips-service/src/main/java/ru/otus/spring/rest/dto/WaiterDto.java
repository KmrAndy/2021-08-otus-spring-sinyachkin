package ru.otus.spring.rest.dto;

import ru.otus.spring.model.Waiter;

public class WaiterDto {
    private String id;

    private String firstName;

    private String lastName;

    private String phone;

    private String restaurant;

    public WaiterDto(String id, String firstName, String lastName, String phone, String restaurant) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.restaurant = restaurant;
    }

    public static WaiterDto convertToDto(Waiter waiter) {
        return new WaiterDto(
                waiter.getId(),
                waiter.getFirstName(),
                waiter.getLastName(),
                getMaskedPhone(waiter.getPhone()),
                waiter.getRestaurant());
    }

    private static String getMaskedPhone(String phone){
        return "+7******" + phone.substring((phone.length() - 4));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }
}
