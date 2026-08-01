package gerenciadordetarefas.personal_tasks.exception;

import java.time.LocalDateTime;

public record ErroRespostaDTO(
        LocalDateTime localDateTime,
        Integer status,
        String erro,
        String mensagem
) {

}
