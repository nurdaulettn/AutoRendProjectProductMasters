package kz.nurdaulet.AutoRent.dto.Request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    @NotNull
    @Min(1)
    @Max(10)
    private Integer rating;

    @NotNull
    @Size(min = 10, max = 1000)
    private String comment;

    @NotNull
    private Long carId;
}
