package gerenciadordetarefas.personal_tasks.mapper;

import gerenciadordetarefas.personal_tasks.DTO.TarefaRequestDTO;
import gerenciadordetarefas.personal_tasks.DTO.TarefaResponseDTO;
import gerenciadordetarefas.personal_tasks.model.Tarefa;
import org.springframework.stereotype.Component;

@Component
public class TarefaMapper {

    public Tarefa paraTarefa(TarefaRequestDTO request){
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        tarefa.setStatus(request.status());
        tarefa.setDataFim(request.dataFim());
        return tarefa;
    }

    public TarefaResponseDTO paraResponseDTO(Tarefa tarefa){
        return new TarefaResponseDTO(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getDataInicio(),
                tarefa.getDataFim()
        );

    }
}
