package gerenciadordetarefas.personal_tasks.repository;

import gerenciadordetarefas.personal_tasks.model.Status;
import gerenciadordetarefas.personal_tasks.model.Tarefa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

@DataJpaTest
class TarefaRepositoryTest {

    @Autowired
    private TestEntityManager manager;

    @Autowired
    private TarefaRepository repository;

    @Test
    public void deveBuscarTarefasPorStatusComSucesso(){
        var tarefaPendente = new Tarefa();
        tarefaPendente.setTitulo("Estudar JPA");
        tarefaPendente.setDescricao("Testando");
        tarefaPendente.setStatus(Status.PENDENTE);
        tarefaPendente.setDataInicio(LocalDateTime.now());
        tarefaPendente.setDataFim(LocalDateTime.now().plusDays(1));

        manager.persist(tarefaPendente);

        var tarefaPendente2 = new Tarefa();
        tarefaPendente2.setTitulo("Estudar JPA");
        tarefaPendente2.setDescricao("Testando");
        tarefaPendente2.setStatus(Status.PENDENTE);
        tarefaPendente2.setDataInicio(LocalDateTime.now());
        tarefaPendente2.setDataFim(LocalDateTime.now().plusDays(1));

        manager.persist(tarefaPendente2);

        var tarefaPendente3 = new Tarefa();
        tarefaPendente3.setTitulo("Estudar JPA");
        tarefaPendente3.setDescricao("Testando");
        tarefaPendente3.setStatus(Status.CONCLUIDA);
        tarefaPendente3.setDataInicio(LocalDateTime.now());
        tarefaPendente3.setDataFim(LocalDateTime.now().plusDays(1));

        manager.persist(tarefaPendente3);

        var resultado = this.repository.findByStatus(Status.PENDENTE);

        Assertions.assertFalse(resultado.isEmpty());
        Assertions.assertEquals(2,resultado.size());
        Assertions.assertEquals(Status.PENDENTE, resultado.get(0).getStatus());

    }

}