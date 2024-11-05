package com.example.sectors_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class UserInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name is mandatory")
    @Column(nullable = false)
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotEmpty(message = "At least one sector should be selected")
    @ElementCollection
    @CollectionTable(name = "user_selected_sectors", joinColumns = @JoinColumn(name = "user_input_id"))
    @Column(name = "sector_id", nullable = false)
    private List<Long> selectedSectors;

    @NotNull(message = "Agreement to terms is mandatory")
    @Column(nullable = false)
    private boolean agreeToTerms;

    @Column(nullable = false, unique = true)
    private String userId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
