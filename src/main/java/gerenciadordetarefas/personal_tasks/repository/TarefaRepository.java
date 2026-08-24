package gerenciadordetarefas.personal_tasks.repository;


import gerenciadordetarefas.personal_tasks.model.Status;
import gerenciadordetarefas.personal_tasks.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByStatus(Status status);
    List<Tarefa> findByDataInicioBetween(LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT t FROM Tarefa t WHERE t.descricao LIKE CONCAT('%', :termo, '%')")
    List<Tarefa> buscarPorDescricao(@Param("termo") String termo);

    @Query("SELECT t FROM Tarefa t WHERE t.status = :status AND t.titulo LIKE CONCAT('%', :termo, '%')")
    List<Tarefa> buscarPorStatusETitulo(@Param("status") Status status, @Param("termo") String termo);

    @Query("SELECT t FROM Tarefa t WHERE t.status = :status ORDER BY t.dataInicio DESC")
    List<Tarefa> buscarPorStatusOrdenadoPorDataInicioDesc(@Param("status") Status status);

    @Query("SELECT COUNT(t) FROM Tarefa t WHERE t.status = :status")
    Long buscarQuantidadeDeTarefasComDeterminadoStatus(@Param("status") Status status);


}
