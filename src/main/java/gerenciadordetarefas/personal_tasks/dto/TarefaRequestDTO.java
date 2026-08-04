package gerenciadordetarefas.personal_tasks.dto;

import gerenciadordetarefas.personal_tasks.model.Status;

import java.time.LocalDateTime;

public record TarefaRequestDTO(
        String titulo,
        String descricao,
        Status status,
        LocalDateTime dataInicio,
        LocalDateTime dataFim
) {

}
