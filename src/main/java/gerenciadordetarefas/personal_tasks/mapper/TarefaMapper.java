package gerenciadordetarefas.personal_tasks.mapper;

import gerenciadordetarefas.personal_tasks.dto.TarefaRequestDTO;
import gerenciadordetarefas.personal_tasks.dto.TarefaResponseDTO;
import gerenciadordetarefas.personal_tasks.model.Tarefa;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;

@Component
public class TarefaMapper {

    public Tarefa paraTarefa(TarefaRequestDTO request){
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        tarefa.setStatus(request.status());
        tarefa.setDataFim(request.dataInicio());
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

    public List<TarefaResponseDTO> paraResponseDTOList(List<Tarefa> tarefas){
        if (tarefas == null) return Collections.emptyList();
        return tarefas.stream().map(this::paraResponseDTO).toList();
    }
}
