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


    public TarefaResponseDTO atualizarPorId(Long id, TarefaRequestDTO taskAntiga){
        Tarefa taskAtualizada = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("id nao encontrado"));
        taskAtualizada.setTitulo(taskAntiga.titulo());
        taskAtualizada.setDescricao(taskAntiga.descricao());
        taskAtualizada.setStatus(taskAntiga.status());
        taskAtualizada.setDataFim(taskAntiga.dataFim());
        return mapper.paraResponseDTO( repository.save(taskAtualizada));
    }


    public TarefaResponseDTO chamarPorId(Long id){
        Tarefa tarefa = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("id não encontrado"));
        return mapper.paraResponseDTO(tarefa);
    }


}
