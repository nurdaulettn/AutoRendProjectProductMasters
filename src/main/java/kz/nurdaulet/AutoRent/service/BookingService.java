package kz.nurdaulet.AutoRent.service;

import kz.nurdaulet.AutoRent.dto.Request.BookingRequestDto;
import kz.nurdaulet.AutoRent.dto.Request.UpdateBookingStatusDto;
import kz.nurdaulet.AutoRent.dto.Response.BookingResponseDto;
import kz.nurdaulet.AutoRent.model.Booking;
import kz.nurdaulet.AutoRent.model.BookingStatus;
import kz.nurdaulet.AutoRent.model.Car;
import kz.nurdaulet.AutoRent.model.User;
import kz.nurdaulet.AutoRent.model.exeptions.BookingNotFoundException;
import kz.nurdaulet.AutoRent.model.exeptions.CarNotFoundException;
import kz.nurdaulet.AutoRent.model.exeptions.UserNotFoundException;
import kz.nurdaulet.AutoRent.repository.BookingRepository;
import kz.nurdaulet.AutoRent.repository.CarRepository;
import kz.nurdaulet.AutoRent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public BookingResponseDto createBooking(BookingRequestDto request) {
        User user = getCurrentUser();
        Car car = carRepository.findById(request.getCarId()).orElseThrow(() -> new CarNotFoundException("Car not found"));

        if (!car.getAvailable()) {
            throw new RuntimeException("Car is not available");
        }
        if (request.getEndDate().isBefore(request.getStartDate()) || request.getEndDate().isEqual(request.getStartDate())) {
            throw new RuntimeException("End date is before start date");
        }

        List<Booking> existBookings = bookingRepository.findByCarIdAndStatusIn(
                request.getCarId(),
                List.of(BookingStatus.CONFIRMED, BookingStatus.PENDING)
        );
        for (Booking booking : existBookings) {
            boolean overlap = !request.getEndDate().isBefore(booking.getStartDate()) &&
                    !request.getStartDate().isAfter(booking.getEndDate());
            if (overlap) {
                throw new RuntimeException("Booking already booked at that time");
            }
        }
        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        BigDecimal totalPrice = car.getPrice().multiply(new BigDecimal(days));

        Booking booking = new Booking();
        booking.setCar(car);
        booking.setClient(user);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        return new BookingResponseDto(savedBooking);
    }


    public BookingResponseDto getBookingById(Long id) {
        User user = getCurrentUser();
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        boolean isClient = booking.getClient().getId().equals(user.getId());
        boolean isOwner = booking.getCar().getId().equals(user.getId());

        if (!isClient && !isOwner) {
            throw new RuntimeException("Booking is not allowed for you");
        }

        return new BookingResponseDto(booking);
    }


    public BookingResponseDto updateBookingStatus(Long id, UpdateBookingStatusDto request) {
        User user = getCurrentUser();
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        if (!booking.getCar().getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("You are not a car owner");
        }

        BookingStatus oldStatus = booking.getStatus();
        BookingStatus newStatus = request.getStatus();

        if (oldStatus == BookingStatus.PENDING) {
            if (newStatus != BookingStatus.CONFIRMED && newStatus != BookingStatus.CANCELLED) {
                throw new RuntimeException("You can not set this status");
            }
        } else if (oldStatus == BookingStatus.CONFIRMED) {
            if (newStatus != BookingStatus.CANCELLED && newStatus != BookingStatus.COMPLETED) {
                throw new RuntimeException("You can not set this status");
            }
        } else if (oldStatus == BookingStatus.CANCELLED || oldStatus == BookingStatus.COMPLETED) {
            throw new RuntimeException("You can not set status for COMPLEATED or CANCELLED booking");
        }
        booking.setStatus(newStatus);
        Booking savedBooking = bookingRepository.save(booking);

        return new BookingResponseDto(savedBooking);
    }


    public List<BookingResponseDto> getMyBookings() {
        User user = getCurrentUser();
        List<Booking> bookings = bookingRepository.findByClientId(user.getId());

        return bookings.stream().map(BookingResponseDto::new).collect(Collectors.toList());
    }


    public List<BookingResponseDto> getBookingsForMyCars() {
        User user = getCurrentUser();

        List<Car> cars = user.getCars();
        if (cars.isEmpty()) {
            throw new RuntimeException("No cars found");
        }
        List<Booking> bookings = bookingRepository.findByCarId(cars.getFirst().getId());
        for (int i = 1; i < cars.size(); i++) {
            Car car = cars.get(i);
            bookings.addAll(bookingRepository.findByCarId(car.getId()));
        }
        return bookings.stream().map(BookingResponseDto::new).collect(Collectors.toList());
    }

    public BookingResponseDto cancelBooking(Long id) {
        User user = getCurrentUser();
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        if(!booking.getClient().getId().equals(user.getId())) {
            throw new RuntimeException("You are not a car owner");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.COMPLETED) {
            throw new RuntimeException("You can not cancel this booking");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        Booking savedBooking = bookingRepository.save(booking);
        return new BookingResponseDto(savedBooking);
    }


    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
