package kz.nurdaulet.AutoRent.dto.Response;

import kz.nurdaulet.AutoRent.model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private Long carId;
    private Long clientId;
    private String clientName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.carId = review.getCar().getId();
        this.clientId = review.getClient().getId();
        this.clientName = review.getClient().getFirstName()+" "+review.getClient().getLastName();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.createdAt = review.getCreatedAt();
    }

}
