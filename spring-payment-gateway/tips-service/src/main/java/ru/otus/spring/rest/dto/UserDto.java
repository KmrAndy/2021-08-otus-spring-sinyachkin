package ru.otus.spring.rest.dto;

import ru.otus.spring.model.User;

public class UserDto {
    private String id;

    private String firstName;

    private String lastName;

    private String phone;

    public UserDto(String id, String firstName, String lastName, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public static UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), getMaskedPhone(user.getPhone()));
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
}