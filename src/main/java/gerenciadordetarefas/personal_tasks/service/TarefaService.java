package gerenciadordetarefas.personal_tasks.service;

import gerenciadordetarefas.personal_tasks.dto.TarefaRequestDTO;
import gerenciadordetarefas.personal_tasks.dto.TarefaResponseDTO;
import gerenciadordetarefas.personal_tasks.mapper.TarefaMapper;
import gerenciadordetarefas.personal_tasks.model.Tarefa;
import gerenciadordetarefas.personal_tasks.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {


    private final TarefaRepository repository;

    private final TarefaMapper mapper;

    public TarefaResponseDTO salvarTarefa(TarefaRequestDTO request){
        return mapper.paraResponseDTO(
                repository.save(
                        mapper.paraTarefa(request)));
    }

    public List<TarefaResponseDTO>  chamarTodos(){
        return mapper.paraResponseDTOList(repository.findAll());
    }


    public void remover(Long id){
        Tarefa idEncontrado = repository.findById(id)
                .orElseThrow( ()->new RuntimeException("id nao encontrado") );

        repository.delete(idEncontrado);
    }


    public Tarefa atualizarPorId(Long id, Tarefa taskAntiga){
        Tarefa taskAtualizada = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Id nao encontrado"));
        taskAtualizada.setTitulo(taskAntiga.getTitulo());
        taskAtualizada.setDescricao(taskAntiga.getDescricao());
        taskAtualizada.setDataFim(taskAntiga.getDataFim());

        return repository.save(taskAtualizada);
    }

    public Tarefa chamarPorId(Long id){
        Tarefa idEncontrado = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Id nao encontrado"));
        return idEncontrado;
    }

}
