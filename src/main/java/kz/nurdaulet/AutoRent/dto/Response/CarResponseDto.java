package kz.nurdaulet.AutoRent.dto.Response;

import kz.nurdaulet.AutoRent.model.Car;
import kz.nurdaulet.AutoRent.model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarResponseDto {
    private Long id;
    private Long ownerId;
    private String ownerName;
    private String ownerPhone;
    private String mark;
    private String model;
    private Integer year;
    private BigDecimal price;
    private String licensePlate;
    private String description;
    private Boolean available;
    private LocalDate createdAt;
    private Double avgRating;
    private Integer reviewsCount;

    public CarResponseDto(Car car) {
        this.id = car.getId();
        this.ownerId = car.getOwner().getId();
        this.ownerName = car.getOwner().getFirstName()+" "+car.getOwner().getLastName();
        this.ownerPhone = car.getOwner().getPhoneNumber();
        this.mark = car.getMark();
        this.model = car.getModel();
        this.year = car.getYear();
        this.price = car.getPrice();
        this.licensePlate = car.getLicensePlate();
        this.description = car.getDescription();
        this.available = car.getAvailable();
        this.createdAt = car.getCreatedAt();

        if(car.getReviews() != null && !car.getReviews().isEmpty()) {
            this.avgRating = car.getReviews().stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            this.reviewsCount = car.getReviews().size();
        }else{
            this.avgRating = 0.0;
            this.reviewsCount = 0;
        }
    }
}
