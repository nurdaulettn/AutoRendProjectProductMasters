package kz.nurdaulet.AutoRent.dto.Response;

import kz.nurdaulet.AutoRent.model.Booking;
import kz.nurdaulet.AutoRent.model.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private Long id;
    private Long carId;
    private String carInfo;
    private String carLicensePlate;
    private Long clientId;
    private String clientName;
    private String clientEmail;
    private String clientPhone;
    private Long ownerId;
    private String ownerName;
    private String ownerPhone;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private Integer daysCount;

    public BookingResponseDto(Booking booking) {
        this.id = booking.getId();

        this.carId = booking.getCar().getId();
        this.carInfo = booking.getCar().getMark() + " " +
                booking.getCar().getModel() + " (" +
                booking.getCar().getYear() + ")";
        this.carLicensePlate = booking.getCar().getLicensePlate();

        this.clientId = booking.getClient().getId();
        this.clientName = booking.getClient().getFirstName() + " " + booking.getClient().getLastName();
        this.clientEmail = booking.getClient().getEmail();
        this.clientPhone = booking.getClient().getPhoneNumber();

        this.ownerId = booking.getCar().getOwner().getId();
        this.ownerName = booking.getCar().getOwner().getFirstName() + " " + booking.getCar().getOwner().getLastName();
        this.ownerPhone = booking.getCar().getOwner().getPhoneNumber();

        this.startDate = booking.getStartDate();
        this.endDate = booking.getEndDate();
        this.totalPrice = booking.getTotalPrice();
        this.status = booking.getStatus();
        this.createdAt = booking.getCreatedAt();

        this.daysCount = (int) ChronoUnit.DAYS.between(this.startDate, this.endDate);
    }
}
