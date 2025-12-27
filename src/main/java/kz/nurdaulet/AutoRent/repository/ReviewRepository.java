package kz.nurdaulet.AutoRent.repository;

import kz.nurdaulet.AutoRent.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCarId(Long carId);

    List<Review> findByClientId(Long clientId);

    boolean existsByClientIdAndCarId(Long clientId, Long carId);
}
