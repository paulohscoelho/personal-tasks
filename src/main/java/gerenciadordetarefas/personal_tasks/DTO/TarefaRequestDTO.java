package gerenciadordetarefas.personal_tasks.DTO;

import gerenciadordetarefas.personal_tasks.model.Status;

import java.time.LocalDateTime;

public record TarefaRequestDTO(
        String titulo,
        String descricao,
        Status status,
        LocalDateTime dataFim
) {
    public TarefaRequestDTO{
        status = (status == null) ? Status.PENDENTE : status;
    }
}
