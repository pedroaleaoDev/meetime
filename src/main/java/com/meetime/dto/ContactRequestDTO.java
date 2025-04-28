package com.meetime.dto;

public class ContactRequestDTO {
    private String email;
    private String firstname;
    private String lastname;
    private String phone;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Exemplo de uso para documentação Swagger/OpenAPI
    // {
    //   "email": "exemplo@email.com",
    //   "firstname": "João",
    //   "lastname": "Silva",
    //   "phone": "11999999999"
    // }
}