package kz.nurdaulet.AutoRent.controller;

import kz.nurdaulet.AutoRent.dto.Response.BookingResponseDto;
import kz.nurdaulet.AutoRent.dto.Response.CarResponseDto;
import kz.nurdaulet.AutoRent.dto.Response.UserResponseDto;
import kz.nurdaulet.AutoRent.model.Booking;
import kz.nurdaulet.AutoRent.model.Car;
import kz.nurdaulet.AutoRent.model.User;
import kz.nurdaulet.AutoRent.model.exeptions.UserNotFoundException;
import kz.nurdaulet.AutoRent.repository.BookingRepository;
import kz.nurdaulet.AutoRent.repository.CarRepository;
import kz.nurdaulet.AutoRent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;

    @GetMapping("/users")
    public List<UserResponseDto> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(UserResponseDto::new).collect(Collectors.toList());
    }

    @GetMapping("/users/{id}")
    public UserResponseDto getUser(@PathVariable Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return new UserResponseDto(user);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @GetMapping("/cars")
    public List<CarResponseDto> getAllCars(){
        List<Car> cars = carRepository.findAll();
        return cars.stream().map(CarResponseDto::new).collect(Collectors.toList());
    }

    @DeleteMapping("/cars/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long id){
        carRepository.deleteById(id);
    }

    @GetMapping("/bookings")
    public List<BookingResponseDto> getAllBookings(){
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(BookingResponseDto::new).collect(Collectors.toList());
    }

    @DeleteMapping("/bookings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable Long id){
        bookingRepository.deleteById(id);
    }

    @GetMapping("/statistics")
    public Map<String, Long> getStatistics() {
        long totalUsers = userRepository.count();
        long totalCars = carRepository.count();
        long totalBookings = bookingRepository.count();
        Map<String, Long> map = new HashMap<>();
        map.put("totalUsers", totalUsers);
        map.put("totalCars", totalCars);
        map.put("totalBookings", totalBookings);
        return map;
    }
}
