package kz.nurdaulet.AutoRent.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    private String mark;
    private String model;

    @Column(name = "`year`")
    private Integer year;

    private BigDecimal price;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @OneToMany(mappedBy = "car")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "car")
    private List<Review> reviews;
}
