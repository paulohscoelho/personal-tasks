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

import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {
    private final TarefaRepository repository;
    private final TarefaMapper mapper;

    public TarefaResponseDTO salvarTarefa(TarefaRequestDTO request){

        Tarefa tarefa = mapper.paraTarefa(request);

        if (tarefa.getDataInicio() == null){
            throw new RegraNegocioException("data inicio tem que ser preenchida");
        }

        if (tarefa.getDataFim() != null){
            if (tarefa.getDataFim().isBefore(tarefa.getDataInicio() )|| tarefa.getDataFim().isEqual(tarefa.getDataInicio())) {
                throw new RegraNegocioException(
                        "A data de término da tarefa não pode ser igual ou anterior a data do inicio da tarefa");
            }
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
            throw new RegraNegocioException("Lista de tarefas está vazia");
        }
        return mapper.paraResponseDTOList(lista);
    }

    public void remover(Long id){
        Tarefa idEncontrado = repository.findById(id)
                .orElseThrow( ()-> new RegraNegocioException("id '"+id+"' não encontrado") );
        if ((idEncontrado.getStatus() == Status.CONCLUIDA) || (idEncontrado.getStatus() == Status.CANCELADA)){
            repository.delete(idEncontrado);
        }else {
            throw new RegraNegocioException("Não pode excluir tarefa em Status de 'PENDENTE' ou 'EM_ANDAMENTO'");
        }
    }

    public TarefaResponseDTO atualizarPorId(Long id, TarefaRequestDTO dadosAtualizados){
        Tarefa tarefaExistente = repository.findById(id)
                .orElseThrow(()-> new RegraNegocioException("id '"+id+"' não encontrado"));

        if (tarefaExistente.getStatus() == Status.CONCLUIDA && dadosAtualizados.status() != Status.CONCLUIDA) {
            throw new RegraNegocioException("Não é possível alterar o status de uma tarefa já CONCLUÍDA.");
        }

        if (tarefaExistente.getStatus() == Status.CANCELADA && dadosAtualizados.status() != Status.CANCELADA) {
            throw new RegraNegocioException("Não é possível alterar o status de uma tarefa já CANCELADA.");
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
        if (dadosAtualizados.dataInicio() != null) {
            tarefaExistente.setDataInicio(dadosAtualizados.dataInicio());
        }

        if (dadosAtualizados.dataFim() != null) {
            tarefaExistente.setDataFim(dadosAtualizados.dataFim());
        }


        if (tarefaExistente.getDataFim() != null) {
            if (tarefaExistente.getDataFim().isBefore(tarefaExistente.getDataInicio()) || tarefaExistente.getDataFim().isEqual(tarefaExistente.getDataInicio())) {
                throw new RegraNegocioException("A data de término não pode ser igual ou anterior à data de início");
            }
        }
        return mapper.paraResponseDTO( repository.save(tarefaExistente));
    }

    public TarefaResponseDTO chamarPorId(Long id){
        Tarefa tarefa = repository.findById(id)
                .orElseThrow(()-> new RegraNegocioException("id "+id+" não encontrado"));
        return mapper.paraResponseDTO(tarefa);
    }

    public List<TarefaResponseDTO> chamarPorStatus(Status status){
        List<Tarefa> tarefas = repository.findByStatus(status);
        if (tarefas.isEmpty())throw new RegraNegocioException("Lista vazia");

        return tarefas.stream().map(mapper::paraResponseDTO).toList();
    }


}
