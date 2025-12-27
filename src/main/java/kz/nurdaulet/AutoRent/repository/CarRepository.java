package kz.nurdaulet.AutoRent.repository;

import kz.nurdaulet.AutoRent.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    Boolean existsByLicensePlate(String licensePlate);
    List<Car> findByAvailableTrue();
    List<Car> findByOwnerId(Long ownerId);
}
