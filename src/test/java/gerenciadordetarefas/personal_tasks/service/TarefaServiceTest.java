package gerenciadordetarefas.personal_tasks.service;

import gerenciadordetarefas.personal_tasks.dto.TarefaRequestDTO;
import gerenciadordetarefas.personal_tasks.dto.TarefaResponseDTO;
import gerenciadordetarefas.personal_tasks.exception.RegraNegocioException;
import gerenciadordetarefas.personal_tasks.mapper.TarefaMapper;
import gerenciadordetarefas.personal_tasks.model.Status;
import gerenciadordetarefas.personal_tasks.model.Tarefa;
import gerenciadordetarefas.personal_tasks.repository.TarefaRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    @Mock
    private TarefaRepository repository;

    @InjectMocks
    private TarefaService service;

    @Mock
    private TarefaMapper mapper;

    @Nested
    class salvarTarefa{


        @Test
        void deveCriarTarefaComSucesso(){
            LocalDateTime inicio = LocalDateTime.of(2027, 8, 4, 10, 0);
            LocalDateTime fim = LocalDateTime.of(2027, 8, 5, 12, 0);

            TarefaRequestDTO request = new TarefaRequestDTO(
                    "Estudar testes",
                    "criar testes unitarios no service",
                    Status.PENDENTE,
                    inicio,
                    fim
            );

            Tarefa tarefaParaSalvar = new Tarefa(
                    null,
                    "Estudar testes",
                    "criar testes unitarios no service",
                    Status.PENDENTE,
                    inicio,
                    fim
            );

            Tarefa tarefaSalva = new Tarefa(
                    1L,
                    "Estudar testes",
                    "criar testes unitarios no service",
                    Status.PENDENTE,
                    inicio,
                    fim
            );

            TarefaResponseDTO response = new TarefaResponseDTO(
                    1L,
                    "Estudar testes",
                    "criar testes unitarios no service",
                    Status.PENDENTE,
                    inicio,
                    fim
            );

            when(mapper.paraTarefa(request)).thenReturn(tarefaParaSalvar);
            when(repository.save(tarefaParaSalvar)).thenReturn(tarefaSalva);
            when(mapper.paraResponseDTO(tarefaSalva)).thenReturn(response);

            TarefaResponseDTO resultado = service.salvarTarefa(request);


            assertNotNull(resultado);
            assertEquals(1L,resultado.id());
            assertEquals("Estudar testes",response.titulo());
            verify(repository,times(1)).save(tarefaParaSalvar);


        }

        @Test
        void deveLancarExcecao_quandoDataInicioForNulo(){
            LocalDateTime inicio = LocalDateTime.of(2027, 8, 4, 10, 0);
            LocalDateTime fim = LocalDateTime.of(2027, 8, 5, 12, 0);

            TarefaRequestDTO request = new TarefaRequestDTO(
                    "Estudar testes",
                    "criar testes unitario do service",
                    Status.PENDENTE,
                    null,
                    fim
            );
            Tarefa tarefaParaSalvar = new Tarefa(
                    null,
                    "Estudar testes",
                    "criar testes unitario do service",
                    Status.PENDENTE,
                    null,
                    fim
            );

            when(mapper.paraTarefa(request)).thenReturn(tarefaParaSalvar);

            RegraNegocioException excecao = assertThrows(
                    RegraNegocioException.class,() ->
                        service.salvarTarefa(request)
            );

            assertEquals("data inicio tem que ser preenchida",excecao.getMessage());
            verify(repository,never()).save(any());
        }


        @Test
        void deveLancarExcecao_quandoDataFimforAnteriorADataInicio(){
            LocalDateTime inicio = LocalDateTime.of(2026,07,05,10,00);
            LocalDateTime fim = LocalDateTime.of(2026,07,04,9,00);

            TarefaRequestDTO request = new TarefaRequestDTO(
                    "Testes unitarios",
                    "Aprendendo Testes unitarios",
                    Status.PENDENTE,
                    inicio,
                    fim
            );

            Tarefa tarefaParaSalvar = new Tarefa(
                    1L,
                    "Testes unitarios",
                    "Aprendendo Testes unitarios",
                    Status.PENDENTE,
                    inicio,
                    fim
            );

            when(mapper.paraTarefa(request)).thenReturn(tarefaParaSalvar);

            RegraNegocioException exception = assertThrows(
                    RegraNegocioException.class,()->
                            service.salvarTarefa(request));
            assertEquals("A data de término da tarefa não pode ser igual " +
                    "ou anterior a data do inicio da tarefa",exception.getMessage());
            verify(repository,never()).save(any());
        }

        @Test
        void deveLancarExcecao_quandoDataInicioForADataFim(){
            LocalDateTime dataIgual = LocalDateTime.of(2026,07,05,10,00);


            TarefaRequestDTO request = new TarefaRequestDTO(
                    "Testes unitarios",
                    "Aprendendo Testes unitarios",
                    Status.PENDENTE,
                    dataIgual,
                    dataIgual
            );

            Tarefa tarefaParaSalvar = new Tarefa(
                    1L,
                    "Testes unitarios",
                    "Aprendendo Testes unitarios",
                    Status.PENDENTE,
                    dataIgual,
                    dataIgual
            );

            when(mapper.paraTarefa(request)).thenReturn(tarefaParaSalvar);

            RegraNegocioException exception = assertThrows(
                    RegraNegocioException.class,()->
                            service.salvarTarefa(request));
            assertEquals("A data de término da tarefa não pode ser igual " +
                    "ou anterior a data do inicio da tarefa",exception.getMessage());
            verify(repository,never()).save(any());

        }

        @Test
        void deveLancarExcecao_quandoTarefaForCriadaComStatusCONCLUIDA(){
            LocalDateTime inicio = LocalDateTime.of(2027, 8, 4, 10, 0);
            LocalDateTime fim = LocalDateTime.of(2027, 8, 5, 12, 0);

            TarefaRequestDTO request = new TarefaRequestDTO(
                    "Estudar testes",
                    "criar testes unitarios no service",
                    Status.CONCLUIDA,
                    inicio,
                    fim
            );

            Tarefa tarefaParaSalvar = new Tarefa(
                    null,
                    "Estudar testes",
                    "criar testes unitarios no service",
                    Status.CONCLUIDA,
                    inicio,
                    fim
            );

            when(mapper.paraTarefa(request)).thenReturn(tarefaParaSalvar);

            RegraNegocioException exception = assertThrows(
                    RegraNegocioException.class,()->
                            service.salvarTarefa(request)
            );
            assertEquals("A tarefa não pode ser criada como Status: 'CONCLUIDA'",exception.getMessage());
            verify(repository,never()).save(any());
        }

        @Test
        void deveCriarTarefaComSucesso_quandoDataFimForNula(){
            LocalDateTime inicio = LocalDateTime.of(2027, 8, 4, 10, 0);

            TarefaRequestDTO request = new TarefaRequestDTO(
                    "Estudar testes",
                    "criar testes unitarios no service",
                    Status.PENDENTE,
                    inicio,
                    null
            );

            Tarefa tarefaParaSalvar = new Tarefa(
                    null,
                    "Estudar testes",
                    "criar testes unitarios no service",
                    Status.PENDENTE,
                    inicio,
                    null
            );

            Tarefa tarefaSalva = new Tarefa(
                    1L,
                    "Estudar testes",
                    "criar testes unitarios no service",
                    Status.PENDENTE,
                    inicio,
                    null
            );

            TarefaResponseDTO response = new TarefaResponseDTO(
                    1L,
                    "Estudar testes",
                    "criar testes unitarios no service",
                    Status.PENDENTE,
                    inicio,
                    null
            );

            when(mapper.paraTarefa(request)).thenReturn(tarefaParaSalvar);
            when(repository.save(tarefaParaSalvar)).thenReturn(tarefaSalva);
            when(mapper.paraResponseDTO(tarefaSalva)).thenReturn(response);

            var resultado = service.salvarTarefa(request);

            assertNotNull(resultado);
            assertEquals(1L,resultado.id());
            assertNull(resultado.dataFim());
            verify(repository,times(1)).save(tarefaParaSalvar);


        }

    }

    @Nested
    class chamarTodos{

        @Test
        void deveBuscarTodasAsTarefasComSucesso_quandoExistiremRegitros(){
            var agora = LocalDateTime.now();
            Tarefa tarefa1 = new Tarefa(
                    1L,
                    "Estudar testes",
                    "criar testes unitario do service",
                    Status.PENDENTE,
                    agora,agora

            );

            Tarefa tarefa2 = new Tarefa(
                    2L,
                    "Estudar java",
                    "criar metodos service",
                    Status.PENDENTE,
                    agora,agora
            );

            List<Tarefa> listaDeTarefas =  List.of(tarefa1,tarefa2);

            TarefaResponseDTO dto1 = new TarefaResponseDTO(
                    1L,
                    "Estudar testes",
                    "criar testes unitarios no service",
                    Status.PENDENTE,
                    agora,agora
            );

            TarefaResponseDTO dto2 = new TarefaResponseDTO(
                    2L,
                    "Estudar java",
                    "criar metodos service",
                    Status.PENDENTE,
                    agora,agora
            );

            List<TarefaResponseDTO> listaDeResponseDTOs =  List.of(dto1,dto2);

            when(repository.findAll()).thenReturn(listaDeTarefas);
            when(mapper.paraResponseDTOList(listaDeTarefas)).thenReturn(listaDeResponseDTOs);

            var resultado = service.chamarTodos();

            assertNotNull(resultado);
            assertFalse(resultado.isEmpty());
            assertEquals(2,resultado.size());
            assertEquals(1L,resultado.get(0).id());

            verify(repository,times(1)).findAll();
            verify(mapper,times(1)).paraResponseDTOList(listaDeTarefas);


        }

        @Test
        void DeveLancarExcecao_quandoBuscarEmBancoDadosVazio(){
            List<Tarefa> listaVazia = List.of();

            when(repository.findAll()).thenReturn(listaVazia);

            RegraNegocioException exception = assertThrows(
                    RegraNegocioException.class,()->
                            service.chamarTodos());

            assertEquals("Lista de tarefas está vazia", exception.getMessage());

            verify(repository,times(1)).findAll();
            verify(mapper,never()).paraResponseDTOList(any());
        }
    }

    @Nested
    class remover{

        @Test
        void deveDeletarTarefaComSucesso() {
            LocalDateTime inicio = LocalDateTime.of(2026, 8, 8, 0, 0);
            LocalDateTime fim = LocalDateTime.of(2026, 10, 10, 10, 0);
            Long id = 1L;
            Tarefa tarefa = new Tarefa(
                    id,
                    "Estudar testes",
                    "criar testes unitario do service",
                    Status.CONCLUIDA,
                    inicio,
                    fim

            );

            when(repository.findById(id)).thenReturn(Optional.of(tarefa));

            service.remover(id);

            verify(repository,times(1)).findById(id);
            verify(repository,times(1)).delete(tarefa);
        }

        @Test
        void deveRetornarExcecao_quandoIdNaoForEncontrado(){
            Long idInexistente = 99L;

            when(repository.findById(idInexistente)).thenReturn(Optional.empty());

            RegraNegocioException exception = assertThrows(
                    RegraNegocioException.class,()->
                            service.remover(idInexistente)
            );

            assertEquals("id '99' não encontrado",exception.getMessage());

            verify(repository,times(1)).findById(idInexistente);
            verify(repository,never()).delete(any());

        }

        @Test
        void deveLancarExcecaoAoExcluirTarefaPendenteOuEmAndamento(){
            LocalDateTime inicio = LocalDateTime.of(2026,07,07,1,0);
            LocalDateTime fim = LocalDateTime.of(2026,07,10,10,10);
            Long id = 1L;
            Tarefa tarefaComStatusEM_ANDAMENTO = new Tarefa(
                    id,
                    "Teste unitario",
                    "teste",
                    Status.EM_ANDAMENTO,
                    inicio,
                    fim
            );

            when(repository.findById(id)).thenReturn(Optional.of(tarefaComStatusEM_ANDAMENTO));

            RegraNegocioException exception = assertThrows(
                    RegraNegocioException.class,()->
                            service.remover(id)
            );

            assertEquals("Não pode excluir tarefa em Status de 'PENDENTE' ou 'EM_ANDAMENTO'",exception.getMessage());
            verify(repository,times(1)).findById(id);
            verify(repository,never()).delete(any());


        }
    }



}