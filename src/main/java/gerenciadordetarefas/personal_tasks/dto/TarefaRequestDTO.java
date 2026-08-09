package gerenciadordetarefas.personal_tasks.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import gerenciadordetarefas.personal_tasks.model.Status;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record TarefaRequestDTO(
     //   @NotBlank(message = "título é obrigatório")
        String titulo,

      //  @NotBlank(message = "descrição é obrigatória")
        String descricao,

      //  @NotNull(message = "status é obrigatório")
        Status status,

     @NotNull(message = "data início é obrigatória")
     @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
     LocalDateTime dataInicio,


     @NotNull(message = "data fim é obrigatória")
     @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
     LocalDateTime dataFim
) {

}
