package kz.nurdaulet.AutoRent.repository;

import kz.nurdaulet.AutoRent.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
