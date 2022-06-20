package ru.objects;

public class AuthData {

    private final long id;
    private final String email, phoneNumber, password;

    public AuthData(long id, String email, String phoneNumber, String password) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public boolean check(long id, String email, String phoneNumber, String password) {
        return this.id == id && (this.email.equals(email) || this.phoneNumber.equals(phoneNumber)) && this.password.equals(password);
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
