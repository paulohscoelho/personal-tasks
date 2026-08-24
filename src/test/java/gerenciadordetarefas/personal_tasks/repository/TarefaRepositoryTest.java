package gerenciadordetarefas.personal_tasks.repository;

import gerenciadordetarefas.personal_tasks.model.Status;
import gerenciadordetarefas.personal_tasks.model.Tarefa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

    @Test
    void deveRetornarListaVazia_quandoBuscarPorStatusInexistente(){
        var tarefaPendente = new Tarefa();
        tarefaPendente.setTitulo("Estudar JPA");
        tarefaPendente.setDescricao("Testando");
        tarefaPendente.setStatus(Status.PENDENTE);
        tarefaPendente.setDataInicio(LocalDateTime.now());
        tarefaPendente.setDataFim(LocalDateTime.now().plusDays(1));

        manager.persist(tarefaPendente);

        var resultado = this.repository.findByStatus(Status.CONCLUIDA);
        Assertions.assertNotNull(resultado);
        Assertions.assertTrue(resultado.isEmpty());

    }


    @Test
    void findByDataInicioBetween_quandoExistemTarefasNoPeriodo_deveRetornarApenasAsTarefasDoIntervalo(){
        var tarefa1 = new Tarefa();

        var hoje = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

        tarefa1.setTitulo("Estudar JPA");
        tarefa1.setDescricao("testando");
        tarefa1.setStatus(Status.PENDENTE);
        tarefa1.setDataInicio(hoje.minusDays(1));
        tarefa1.setDataFim(hoje.plusDays(2));

        manager.persist(tarefa1);

        var tarefa2 = new Tarefa();

        tarefa2.setTitulo("Organizar github");
        tarefa2.setDescricao("verificar commits");
        tarefa2.setStatus(Status.PENDENTE);
        tarefa2.setDataInicio(hoje.minusDays(5));
        tarefa2.setDataFim(hoje.plusDays(7));

        manager.persist(tarefa2);

        var tarefa3 = new Tarefa();

        tarefa3.setTitulo("Ler um livro");
        tarefa3.setDescricao("Livro de Algoritmo");
        tarefa3.setStatus(Status.PENDENTE);
        tarefa3.setDataInicio(hoje.minusDays(2));
        tarefa3.setDataFim(hoje.plusDays(3));

        manager.persist(tarefa3);

        var inicioFiltro = hoje.minusDays(1);
        var fimFiltro = hoje.plusDays(2);

        var resultado = this.repository.findByDataInicioBetween(inicioFiltro,fimFiltro);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1,resultado.size());
        Assertions.assertEquals("Estudar JPA",resultado.get(0).getTitulo());
    }

    @Test
    void deveLancarExcecao_quandoTentarSalvarTituloNulo(){
        var TarefaSemTitulo = new Tarefa();

        var hoje = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

        TarefaSemTitulo.setDescricao("testando");
        TarefaSemTitulo.setStatus(Status.PENDENTE);
        TarefaSemTitulo.setDataInicio(hoje.minusDays(1));
        TarefaSemTitulo.setDataFim(hoje.plusDays(2));

        Assertions.assertThrows(Exception.class,()->{
            manager.persistAndFlush(TarefaSemTitulo);
        });
    }

    @Test
    void buscarPorDescricao_quandoTermoExistir_deveRetornarTarefasCorrespondentes(){

        var tarefa1 = new Tarefa();
        tarefa1.setTitulo("Estudar Spring");
        tarefa1.setDescricao("Aprender consultas em JPQL com Spring data");
        tarefa1.setStatus(Status.PENDENTE);
        tarefa1.setDataInicio(LocalDateTime.now());
        tarefa1.setDataFim(LocalDateTime.now().plusDays(1));
        manager.persist(tarefa1);

        var tarefa2 = new Tarefa();
        tarefa2.setTitulo("Exercicios");
        tarefa2.setDescricao("Praticar logica de programação");
        tarefa2.setStatus(Status.PENDENTE);
        tarefa2.setDataInicio(LocalDateTime.now());
        tarefa2.setDataFim(LocalDateTime.now().plusDays(1));
        manager.persist(tarefa2);


        var resultado = this.repository.buscarPorDescricao("JPQL");

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1,resultado.size());
        Assertions.assertEquals("Estudar Spring",resultado.get(0).getTitulo());


    }

    @Test
    void buscarPorStatusETitulo_quandoTermoExistir_deveRetornarTarefasCorrespondentes(){
        var tarefa1 = new Tarefa();
        tarefa1.setTitulo("Estudar Spring");
        tarefa1.setDescricao("hoje a tarde");
        tarefa1.setStatus(Status.PENDENTE);
        tarefa1.setDataInicio(LocalDateTime.now());
        tarefa1.setDataFim(LocalDateTime.now().plusDays(1));
        manager.persist(tarefa1);

        var tarefa2 = new Tarefa();
        tarefa2.setTitulo("Estudar Spring");
        tarefa2.setDescricao("hoje de manha");
        tarefa2.setStatus(Status.CONCLUIDA);
        tarefa2.setDataInicio(LocalDateTime.now());
        tarefa2.setDataFim(LocalDateTime.now().plusDays(3));
        manager.persist(tarefa2);

        var tarefa3 = new Tarefa();
        tarefa3.setTitulo("Estudar ANGULAR");
        tarefa3.setDescricao("Estudar amanha de manhã");
        tarefa3.setStatus(Status.CONCLUIDA);
        tarefa3.setDataInicio(LocalDateTime.now());
        tarefa3.setDataFim(LocalDateTime.now().plusDays(2));
        manager.persist(tarefa3);

        var resultado = this.repository.buscarPorStatusETitulo(Status.PENDENTE,"Spring");

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1,resultado.size());
        Assertions.assertEquals("Estudar Spring", resultado.get(0).getTitulo());
    }

    @Test
    void buscarPorStatusOrdenadoPorDataInicioDesc_deveRetornarTarefasOrdenacaoCorreta(){
        var hoje = LocalDateTime.now();

        var tarefaAntiga = new Tarefa();
        tarefaAntiga.setTitulo("Titulo Antigo");
        tarefaAntiga.setDescricao("Titulo criado ontem");
        tarefaAntiga.setStatus(Status.PENDENTE);
        tarefaAntiga.setDataInicio(hoje.minusDays(1));
        tarefaAntiga.setDataFim(hoje.plusDays(1));
        manager.persist(tarefaAntiga);

        var tarefaRecente = new Tarefa();
        tarefaRecente.setTitulo("Titulo Recente");
        tarefaRecente.setDescricao("Titulo criado Hoje");
        tarefaRecente.setStatus(Status.PENDENTE);
        tarefaRecente.setDataInicio(hoje);
        tarefaRecente.setDataFim(hoje.plusDays(1));
        manager.persist(tarefaRecente);

        var resultado = this.repository.buscarPorStatusOrdenadoPorDataInicioDesc(Status.PENDENTE);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2,resultado.size());
        Assertions.assertEquals("Titulo Recente",resultado.get(0).getTitulo());
        Assertions.assertEquals("Titulo Antigo",resultado.get(1).getTitulo());

    }

    @Test
    void buscarQuantidadeDeTarefasComDeterminadoStatus_deveRetornarQauntidadeDeTarefasDoMesmoStatus(){
        var hoje = LocalDateTime.now();

        var tarefa1 = new Tarefa();
        tarefa1.setTitulo("tarefa Criar testes unitarios");
        tarefa1.setDescricao("Testes unitarios de manha");
        tarefa1.setStatus(Status.PENDENTE);
        tarefa1.setDataInicio(hoje.minusDays(1));
        tarefa1.setDataFim(hoje.plusDays(1));
        manager.persist(tarefa1);

        var tarefa2 = new Tarefa();
        tarefa2.setTitulo("Atualizar DTO ");
        tarefa2.setDescricao("Atualizar dto");
        tarefa2.setStatus(Status.CONCLUIDA);
        tarefa2.setDataInicio(hoje.minusDays(2));
        tarefa2.setDataFim(hoje.plusDays(2));
        manager.persist(tarefa2);

        var tarefa3 = new Tarefa();
        tarefa3.setTitulo("Excluir testes antigos");
        tarefa3.setDescricao("excluir testes que ja estao concluidos");
        tarefa3.setStatus(Status.PENDENTE);
        tarefa3.setDataInicio(hoje.minusDays(3));
        tarefa3.setDataFim(hoje.plusDays(3));
        manager.persist(tarefa3);

        var resultado = this.repository.buscarQuantidadeDeTarefasComDeterminadoStatus(Status.PENDENTE);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2L, resultado);

    }






}