package Pavan.com.E_commerce_Application.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String photo;
    private String role;

    public UserDTO() {
    }

    public UserDTO(String name, String email, String phone, String photo, String role) {
        this.name = name;

        this.email = email;
        this.phone = phone;
        this.photo = photo;
        this.role = role;
    }

}

