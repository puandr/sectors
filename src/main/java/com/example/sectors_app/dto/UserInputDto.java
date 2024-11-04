package com.example.sectors_app.dto;

import com.example.sectors_app.model.UserInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInputDto {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotEmpty(message = "At least one sector should be selected")
    private List<Long> selectedSectors;

    @NotNull(message = "Agreement to terms is mandatory")
    private boolean agreeToTerms;

    public UserInput toEntity() {
        UserInput userInput = new UserInput();
        userInput.setId(this.id);
        userInput.setName(this.name);
        userInput.setSelectedSectors(this.selectedSectors);
        userInput.setAgreeToTerms(this.agreeToTerms);
        return userInput;
    }

    public static UserInputDto fromEntity(UserInput userInput) {
        UserInputDto dto = new UserInputDto();
        dto.setId(userInput.getId());
        dto.setName(userInput.getName());
        dto.setSelectedSectors(userInput.getSelectedSectors());
        dto.setAgreeToTerms(userInput.isAgreeToTerms());
        return dto;
    }
}
