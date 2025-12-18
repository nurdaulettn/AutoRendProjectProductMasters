package kz.nurdaulet.AutoRent.repository;

import kz.nurdaulet.AutoRent.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
