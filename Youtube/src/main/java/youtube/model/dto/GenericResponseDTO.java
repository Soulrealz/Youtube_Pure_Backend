package youtube.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Component
@NoArgsConstructor
public class GenericResponseDTO {
    private String message;
}
