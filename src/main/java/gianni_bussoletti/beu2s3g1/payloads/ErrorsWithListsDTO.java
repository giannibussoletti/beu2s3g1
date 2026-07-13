package gianni_bussoletti.beu2s3g1.payloads;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorsWithListsDTO(String message, LocalDateTime timestamp, List<String> errorsList) {
}
