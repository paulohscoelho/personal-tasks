package gerenciadordetarefas.personal_tasks.dto;

import gerenciadordetarefas.personal_tasks.model.Status;

import java.time.LocalDateTime;

public record TarefaResponseDTO(
    Long id,
    String titulo,
    String descricao,
    Status status,
    LocalDateTime dataInicio,
    LocalDateTime dataFim
) {
}
