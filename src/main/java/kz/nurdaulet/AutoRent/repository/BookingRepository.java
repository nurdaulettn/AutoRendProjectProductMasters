package kz.nurdaulet.AutoRent.repository;

import kz.nurdaulet.AutoRent.model.Booking;
import kz.nurdaulet.AutoRent.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByClientId(Long clientId);

    List<Booking> findByCarId(Long carId);

    List<Booking> findByCarIdAndStatusIn(Long carId, List<BookingStatus> statuses);

    List<Booking> findByClientIdAndStatus(Long clientId, BookingStatus status);

}
