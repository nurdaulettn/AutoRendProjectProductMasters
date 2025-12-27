package kz.nurdaulet.AutoRent.controller;

import jakarta.validation.Valid;
import kz.nurdaulet.AutoRent.dto.Request.UpdateUserRequestDto;
import kz.nurdaulet.AutoRent.dto.Response.UserResponseDto;
import kz.nurdaulet.AutoRent.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public UserResponseDto getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PutMapping("/me")
    public UserResponseDto updateUser(@Valid @RequestBody UpdateUserRequestDto request){
        return userService.updateUser(request);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount() {
        userService.deleteUser();
    }
}
