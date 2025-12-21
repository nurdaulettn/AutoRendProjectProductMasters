package kz.nurdaulet.AutoRent.dto.Request;

import jakarta.validation.constraints.NotNull;
import kz.nurdaulet.AutoRent.model.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingStatusDto {
    @NotNull
    private BookingStatus status;
}
