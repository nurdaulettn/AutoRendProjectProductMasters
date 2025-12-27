package kz.nurdaulet.AutoRent.service;

import kz.nurdaulet.AutoRent.dto.Request.CarRequestDto;
import kz.nurdaulet.AutoRent.dto.Response.CarResponseDto;
import kz.nurdaulet.AutoRent.model.Car;
import kz.nurdaulet.AutoRent.model.Role;
import kz.nurdaulet.AutoRent.model.User;
import kz.nurdaulet.AutoRent.model.exeptions.CarNotFoundException;
import kz.nurdaulet.AutoRent.model.exeptions.UserNotFoundException;
import kz.nurdaulet.AutoRent.repository.CarRepository;
import kz.nurdaulet.AutoRent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public CarResponseDto createCar(CarRequestDto request) {
        User currentUser = getCurrentUser();

        if (currentUser.getRole() != Role.OWNER) {
            throw new RuntimeException("Only owners can create cars");
        }
        if (carRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new RuntimeException("License plate "+request.getLicensePlate()+" already in use");
        }

        Car car = new Car();
        car.setOwner(currentUser);
        car.setMark(request.getMark());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setPrice(request.getPrice());
        car.setLicensePlate(request.getLicensePlate());
        car.setDescription(request.getDescription());
        car.setAvailable(request.getAvailable());
        car.setCreatedAt(LocalDate.now());

        Car savedCar = carRepository.save(car);

        return new CarResponseDto(savedCar);
    }

    public List<CarResponseDto> getAllCars(Boolean onlyAvailable) {
        List<Car> cars;

        if(onlyAvailable != null && onlyAvailable) {
            cars = carRepository.findByAvailableTrue();
        }else{
            cars = carRepository.findAll();
        }

        return cars.stream()
                .map(CarResponseDto::new)
                .toList();
    }

    public CarResponseDto getCarById(Long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found"));
        return new CarResponseDto(car);
    }

    public CarResponseDto updateCar(Long id, CarRequestDto request) {
        User currentUser = getCurrentUser();
        Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found"));

        if(!car.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can not update this car");
        }
        if (!car.getLicensePlate().equals(request.getLicensePlate())) {
            if (carRepository.existsByLicensePlate(request.getLicensePlate())) {
                throw new RuntimeException("License plate "+request.getLicensePlate()+" already in use");
            }
        }

        car.setMark(request.getMark());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setPrice(request.getPrice());
        car.setLicensePlate(request.getLicensePlate());
        car.setDescription(request.getDescription());
        car.setAvailable(request.getAvailable());

        Car savedCar = carRepository.save(car);

        return new CarResponseDto(savedCar);
    }

    public void deleteCar(Long id) {
        User currentUser = getCurrentUser();
        Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found"));

        if(!car.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can not delete this car");
        }

        boolean hasActiveBooking = car.getBookings().stream()
                .anyMatch(booking ->
                        booking.getStatus().name().equals("PENDING") ||
                                booking.getStatus().name().equals("CONFIRMED")
                );

        if(hasActiveBooking) {
            throw new RuntimeException("You can not delete this car, because this car has active booking");
        }

        carRepository.delete(car);
    }

    public List<CarResponseDto> getMyCars() {
        User currentUser = getCurrentUser();
        List<Car> cars = carRepository.findByOwnerId(currentUser.getId());
        return cars.stream()
                .map(CarResponseDto::new)
                .toList();
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
