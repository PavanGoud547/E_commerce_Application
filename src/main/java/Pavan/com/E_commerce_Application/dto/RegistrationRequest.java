package Pavan.com.E_commerce_Application.dto;
import Pavan.com.E_commerce_Application.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    private String username;
    private String name;
    private String password;
    private Role role;
    private String email;
    private String phone;

    public RegistrationRequest(String name, String username, String email, String phone, String photo, Role role) {
    }
}

