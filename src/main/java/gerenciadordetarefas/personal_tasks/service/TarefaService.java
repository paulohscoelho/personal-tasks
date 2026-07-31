package gerenciadordetarefas.personal_tasks.service;

import gerenciadordetarefas.personal_tasks.dto.TarefaRequestDTO;
import gerenciadordetarefas.personal_tasks.dto.TarefaResponseDTO;
import gerenciadordetarefas.personal_tasks.exception.RegraNegocioException;
import gerenciadordetarefas.personal_tasks.mapper.TarefaMapper;
import gerenciadordetarefas.personal_tasks.model.Status;
import gerenciadordetarefas.personal_tasks.model.Tarefa;
import gerenciadordetarefas.personal_tasks.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {
    private final TarefaRepository repository;
    private final TarefaMapper mapper;

    public TarefaResponseDTO salvarTarefa(TarefaRequestDTO request){

        Tarefa tarefa = mapper.paraTarefa(request);

        if(tarefa.getDataFim().isBefore(LocalDateTime.now())){
            throw new RegraNegocioException(
                    "A data de término da tarefa não pode ser anterior a data do inicio da tarefa");
        }
        if (tarefa.getStatus() == Status.CONCLUIDA){
            throw new RegraNegocioException("A tarefa não pode ser criada como Status: 'CONCLUIDA'");
        }

        Tarefa tarefaSalva = repository.save(tarefa);
        return mapper.paraResponseDTO(tarefaSalva);
    }

    public List<TarefaResponseDTO>  chamarTodos(){
        return mapper.paraResponseDTOList(repository.findAll());
    }

    public void remover(Long id){
        Tarefa idEncontrado = repository.findById(id)
                .orElseThrow( ()-> new RegraNegocioException("id "+id+" não encontrado") );
        repository.delete(idEncontrado);
    }

    /// CONTINUAR NA REGRA DE NEGOCIO DO  METODO ATUALIZAR
    public TarefaResponseDTO atualizarPorId(Long id, TarefaRequestDTO taskAntiga){
        Tarefa taskAtualizada = repository.findById(id)
                .orElseThrow(()-> new RegraNegocioException("id "+id+" não encontrado"));
        taskAtualizada.setTitulo(taskAntiga.titulo());
        taskAtualizada.setDescricao(taskAntiga.descricao());
        taskAtualizada.setStatus(taskAntiga.status());
        taskAtualizada.setDataFim(taskAntiga.dataFim());
        return mapper.paraResponseDTO( repository.save(taskAtualizada));
    }

    public TarefaResponseDTO chamarPorId(Long id){
        Tarefa tarefa = repository.findById(id)
                .orElseThrow(()-> new RegraNegocioException("id "+id+" não encontrado"));
        return mapper.paraResponseDTO(tarefa);
    }
}
