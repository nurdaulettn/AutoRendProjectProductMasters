package kz.nurdaulet.AutoRent.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {
    @NotBlank
    @Size(min=2, max=50, message="Length of first name should be in range 2-100")
    private String firstName;

    @NotBlank
    @Size(min=2, max=50, message="Length of last name should be in range 2-100")
    private String lastName;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String currentPassword;

    @NotBlank
    @Size(min=6, max=100, message="Length of password should be in range 6-100")
    private String newPassword;
}
