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
        List<Tarefa> lista = repository.findAll();
        if (lista.isEmpty()){
            throw new RegraNegocioException("Lista de tarefas está vazia ");
        }
        return mapper.paraResponseDTOList(lista);
    }

    public void remover(Long id){
        Tarefa idEncontrado = repository.findById(id)
                .orElseThrow( ()-> new RegraNegocioException("id '"+id+"' não encontrado") );
        repository.delete(idEncontrado);
    }

    public TarefaResponseDTO atualizarPorId(Long id, TarefaRequestDTO dadosAtualizados){
        Tarefa tarefaExistente = repository.findById(id)
                .orElseThrow(()-> new RegraNegocioException("id "+id+" não encontrado"));
        System.out.println("status do banc: "+ tarefaExistente.getStatus());
        System.out.println("status do dto: "+ dadosAtualizados.status());
        if ((tarefaExistente.getStatus() == Status.CONCLUIDA) && (dadosAtualizados.status() != Status.CONCLUIDA)){
            throw new RegraNegocioException("Não é possivel alterar o status de uma tarefa já CONCLUÍDA.");
        }

        if ((tarefaExistente.getStatus() == Status.CANCELADA) &&(dadosAtualizados.status() != Status.CANCELADA)){
            throw new RegraNegocioException("Não é possivel alterar o status de uma tarefa já CANCELADA");
        }
        if (dadosAtualizados.titulo() != null){
            tarefaExistente.setTitulo(dadosAtualizados.titulo());
        }
        if (dadosAtualizados.descricao() != null){
            tarefaExistente.setDescricao(dadosAtualizados.descricao());
        }
        if (dadosAtualizados.status() != null){
            tarefaExistente.setStatus(dadosAtualizados.status());
        }
        if (dadosAtualizados.dataFim() != null){
            tarefaExistente.setDataFim(dadosAtualizados.dataFim());
        }

//        tarefaExistente.setTitulo(dadosAtualizados.titulo());
//        tarefaExistente.setDescricao(dadosAtualizados.descricao());
//        tarefaExistente.setStatus(dadosAtualizados.status());
//        tarefaExistente.setDataFim(dadosAtualizados.dataFim());
        return mapper.paraResponseDTO( repository.save(tarefaExistente));
    }

    public TarefaResponseDTO chamarPorId(Long id){
        Tarefa tarefa = repository.findById(id)
                .orElseThrow(()-> new RegraNegocioException("id "+id+" não encontrado"));
        return mapper.paraResponseDTO(tarefa);
    }
}
