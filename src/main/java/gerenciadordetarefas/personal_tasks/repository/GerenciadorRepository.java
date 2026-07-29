package gerenciadordetarefas.personal_tasks.repository;


import gerenciadordetarefas.personal_tasks.model.Gerenciador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GerenciadorRepository extends JpaRepository<Gerenciador, Long> {
}
