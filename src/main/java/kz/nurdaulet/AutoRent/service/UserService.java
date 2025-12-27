package kz.nurdaulet.AutoRent.service;

import kz.nurdaulet.AutoRent.dto.Request.UpdateUserRequestDto;
import kz.nurdaulet.AutoRent.dto.Response.UserResponseDto;
import kz.nurdaulet.AutoRent.model.User;
import kz.nurdaulet.AutoRent.model.exeptions.UserNotFoundException;
import kz.nurdaulet.AutoRent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto getCurrentUser(){
        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        return new UserResponseDto(user);
    }

    public UserResponseDto updateUser(UpdateUserRequestDto request){
        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (request.getFirstName() != null && !request.getFirstName().isBlank()){
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isBlank()){
            user.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()){
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()){
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }
        User updatedUser = userRepository.save(user);
        return new UserResponseDto(updatedUser);
    }

    public void deleteUser(){
        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }

    private String getCurrentUserEmail(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
