package kz.nurdaulet.AutoRent.repository;

import kz.nurdaulet.AutoRent.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
