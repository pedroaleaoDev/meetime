package com.meetime.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ContactRequestDTO {
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;
    @NotBlank(message = "O primeiro nome é obrigatório")
    private String firstname;
    @NotBlank(message = "O sobrenome é obrigatório")
    private String lastname;
    @NotBlank(message = "O telefone é obrigatório")
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