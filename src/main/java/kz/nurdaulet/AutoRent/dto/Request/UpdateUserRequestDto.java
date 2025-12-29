package kz.nurdaulet.AutoRent.dto.Request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {

    @Size(min=2, max=50, message="Length of first name should be in range 2-100")
    private String firstName;


    @Size(min=2, max=50, message="Length of last name should be in range 2-100")
    private String lastName;


    private String phoneNumber;


    private String currentPassword;


    @Size(min=6, max=100, message="Length of password should be in range 6-100")
    private String newPassword;
}
