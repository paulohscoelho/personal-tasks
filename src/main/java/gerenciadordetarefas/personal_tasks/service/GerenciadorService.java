package gerenciadordetarefas.personal_tasks.service;

import gerenciadordetarefas.personal_tasks.model.Gerenciador;
import gerenciadordetarefas.personal_tasks.repository.GerenciadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GerenciadorService {


    private final GerenciadorRepository repository;

    public GerenciadorService(GerenciadorRepository repository) {
        this.repository = repository;
    }

    public Gerenciador salvar(Gerenciador gerenciador ){
        return repository.save(gerenciador);
    }

    public List<Gerenciador> ChamarTodos(){
        List <Gerenciador> listar = repository.findAll();
        return listar;
    }

    public void remover(Long id){
        Gerenciador idEncontrado = repository.findById(id)
                .orElseThrow( ()->new RuntimeException("id nao encontrado") );

        repository.delete(idEncontrado);
    }


    public Gerenciador atualizarPorId(Long id, Gerenciador taskAntiga){
        Gerenciador taskAtualizada = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Id nao encontrado"));
        taskAtualizada.setTitulo(taskAntiga.getTitulo());
        taskAtualizada.setDescricao(taskAntiga.getDescricao());
        taskAtualizada.setDataFim(taskAntiga.getDataFim());

        return repository.save(taskAtualizada);
    }

    public Gerenciador chamarPorId(Long id){
        Gerenciador idEncontrado = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Id nao encontrado"));
        return idEncontrado;
    }

}
