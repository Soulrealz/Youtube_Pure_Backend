package youtube.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Component
public class GenericResponseDTO {
    private String message;
}
