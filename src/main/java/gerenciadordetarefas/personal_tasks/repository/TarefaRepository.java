package gerenciadordetarefas.personal_tasks.repository;


import gerenciadordetarefas.personal_tasks.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
}
