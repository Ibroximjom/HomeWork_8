package uz.pdp.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

public class User implements Serializable {
    private final UUID id = UUID.randomUUID();
    private String name;
    private String phoneNumber;
    private String password;
    private ZonedDateTime timeZone;


    public UUID getId() {
        return id;
    }
    public ZonedDateTime getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(ZonedDateTime timeZone) {
        this.timeZone = timeZone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "uz.pdp.entity.User{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
