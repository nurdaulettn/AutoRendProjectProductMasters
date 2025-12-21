package kz.nurdaulet.AutoRent.dto.Request;

import jakarta.validation.constraints.*;
import kz.nurdaulet.AutoRent.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    @NotBlank
    @Email
    @Size(max=100, message = "Size of email can't be longer than 100")
    private String email;

    @NotBlank
    @Size(min=6, max=100, message="Length of password should be in range 6-100")
    private String password;

    @NotBlank
    @Size(min=2, max=50, message="Length of first name should be in range 2-100")
    private String firstName;

    @NotBlank
    @Size(min=2, max=50, message="Length of last name should be in range 2-100")
    private String lastName;

    @NotBlank
    private String phone;

    @NotBlank
    private Role role;
}
