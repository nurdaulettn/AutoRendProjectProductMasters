package kz.nurdaulet.AutoRent.controller;

import jakarta.validation.Valid;
import kz.nurdaulet.AutoRent.dto.Request.CarRequestDto;
import kz.nurdaulet.AutoRent.dto.Response.CarResponseDto;
import kz.nurdaulet.AutoRent.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping
    public List<CarResponseDto> getAllCars(@RequestParam(required = false) Boolean onlyAvailable) {
        return carService.getAllCars(onlyAvailable);
    }

    @GetMapping("/{id}")
    public CarResponseDto getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public CarResponseDto createCar(@Valid @RequestBody CarRequestDto request) {
        return carService.createCar(request);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('OWNER')")
    public CarResponseDto updateCar(@PathVariable Long id, @Valid @RequestBody CarRequestDto request) {
        return carService.updateCar(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('OWNER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('OWNER')")
    public List<CarResponseDto> getMyCars() {
        return carService.getMyCars();
    }

}
