package kz.nurdaulet.AutoRent.dto.Request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRequestDto {
    @NotBlank
    @Size(min=2, max=100)
    private String mark;

    @NotBlank
    @Size(min=2, max=100)
    private String model;

    @NotNull
    @Min(1900)
    @Max(2026)
    private Integer year;

    @NotBlank
    @DecimalMin("0.01")
    @Digits(integer=10, fraction=2)
    private BigDecimal price;

    @NotBlank
    @Size(min=2, max=15)
    private String licensePlate;

    @Size(max=1000)
    private String description;

    @NotNull
    private Boolean available;
}
