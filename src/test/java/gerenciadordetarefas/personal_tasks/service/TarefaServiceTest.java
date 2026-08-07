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
        void deveDeletarTarefaComSucessoComStatusCOMCONCLUIDA() {
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
        void deveDeletarTarefaComSucessoComStatusCOMCANCELADA() {
            LocalDateTime inicio = LocalDateTime.of(2026, 8, 8, 0, 0);
            LocalDateTime fim = LocalDateTime.of(2026, 10, 10, 10, 0);
            Long id = 1L;
            Tarefa tarefa = new Tarefa(
                    id,
                    "Estudar testes",
                    "criar testes unitario do service",
                    Status.CANCELADA,
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

    @Nested
    class atualizarPorId{

        @Test
        void deveAtualizarTarefaComSucesso(){
            LocalDateTime inicio = LocalDateTime.of(2026,07,07,10,0);
            LocalDateTime fim = LocalDateTime.of(2026,07,8,12,0);
            Long id = 1L;
            Tarefa tarefaExistente = new Tarefa(
                    id,
                    "titulo antigo",
                    "descricao antiga",
                    Status.PENDENTE,
                    inicio,fim
            );

            TarefaRequestDTO request = new TarefaRequestDTO(
                    "Estudar testes",
                    "criar testes unitarios no service",
                    Status.PENDENTE,
                    inicio,
                    fim
            );

            TarefaResponseDTO response = new TarefaResponseDTO(
                    id,
                    "Estudar testes",
                    "criar testes unitarios no service",
                    Status.PENDENTE,
                    inicio,fim
            );
            when(repository.findById(id)).thenReturn(Optional.of(tarefaExistente));
            when(repository.save(any())).thenReturn(tarefaExistente);
            when(mapper.paraResponseDTO(any())).thenReturn(response);

            var resultado = service.atualizarPorId(id,request);

            assertNotNull(resultado);
            assertEquals(request.titulo(), resultado.titulo());

            verify(repository,times(1)).findById(id);
            verify(repository,times(1)).save(any());
            verify(mapper,times(1)).paraResponseDTO(any());
        }


        @Test
        void deveLancarExcecao_quandoTarefaNaoExistir(){
            LocalDateTime inicio = LocalDateTime.of(2026,07,07,10,0);
            LocalDateTime fim = LocalDateTime.of(2026,07,8,12,0);
            Long idInexistente = 99L;
            TarefaRequestDTO requestDTO = new TarefaRequestDTO(
                    "Estudar testes",
                    "criar testes unitarios no service",
                    Status.PENDENTE,
                    inicio,fim
            );
            when(repository.findById(idInexistente)).thenReturn(Optional.empty());

            RegraNegocioException exception = assertThrows(
                    RegraNegocioException.class,()->
                            service.atualizarPorId(idInexistente,requestDTO)
            );

            assertEquals("id '99' não encontrado",exception.getMessage());

            verify(repository,times(1)).findById(idInexistente);
            verify(repository,never()).save(any());
            verify(mapper,never()).paraResponseDTO(any());
        }

        @Test
        void deveLancarExcecao_quandoStatusJaEstiverCONCLUIDO(){
            LocalDateTime inicio = LocalDateTime.of(2026,7,7,10,0);
            LocalDateTime fim = LocalDateTime.of(2026,7,8,12,0);
            Long id = 1L;

            Tarefa tarefa = new Tarefa(
                    id,
                    "Estudar testes",
                    "criar testes unitario do service",
                    Status.CONCLUIDA,
                    inicio,
                    fim
            );

            TarefaRequestDTO request = new TarefaRequestDTO(

                    "Estudar testes",
                    "criar testes unitario do service",
                    Status.EM_ANDAMENTO,
                    inicio,
                    fim

            );

            when(repository.findById(1L)).thenReturn(Optional.of(tarefa));

            RegraNegocioException exception = assertThrows(
                    RegraNegocioException.class,()->
                        service.atualizarPorId(id,request)
            );

            assertEquals("Não é possível alterar o status de uma tarefa já CONCLUÍDA.",exception.getMessage());
            verify(repository,times(1)).findById(id);
            verify(repository,never()).save(any());
            verify(mapper,never()).paraResponseDTO(any());

        }

        @Test
        void deveLancarExcecao_quandoStatusJaEstiverCANCELADA(){
            LocalDateTime inicio = LocalDateTime.of(2026,7,7,10,0);
            LocalDateTime fim = LocalDateTime.of(2026,7,8,12,0);
            Long id = 1L;

            Tarefa tarefa = new Tarefa(
                    id,
                    "Estudar testes",
                    "criar testes unitario do service",
                    Status.CANCELADA,
                    inicio,
                    fim
            );

            TarefaRequestDTO request = new TarefaRequestDTO(

                    "Estudar testes",
                    "criar testes unitario do service",
                    Status.EM_ANDAMENTO,
                    inicio,
                    fim

            );

            when(repository.findById(1L)).thenReturn(Optional.of(tarefa));

            RegraNegocioException exception = assertThrows(
                    RegraNegocioException.class,()->
                            service.atualizarPorId(id,request)
            );

            assertEquals("Não é possível alterar o status de uma tarefa já CANCELADA.",exception.getMessage());
            verify(repository,times(1)).findById(id);
            verify(repository,never()).save(any());
            verify(mapper,never()).paraResponseDTO(any());

        }

        @Test
        void deveAlterarOutrosCampos_quandoManterStatusCONCLUIDO(){
            LocalDateTime inicio = LocalDateTime.of(2026,7,7,10,0);
            LocalDateTime fim = LocalDateTime.of(2026,7,8,12,0);
            Long id = 1L;
            Tarefa tarefaExistenteNoBanco = new Tarefa(
                    id,
                    "Tarefa Antiga",
                    "Tarefa Antiga",
                    Status.CONCLUIDA,
                    inicio,
                    fim
            );

            TarefaRequestDTO requestDTO  = new TarefaRequestDTO(

                    "Tarefa Nova",
                    "Tarefa Nova",
                    Status.CONCLUIDA,
                    inicio,
                    fim
            );

            TarefaResponseDTO responseDTO  = new TarefaResponseDTO(
                    id,
                    "Tarefa Nova",
                    "Tarefa Nova",
                    Status.CONCLUIDA,
                    inicio,
                    fim
            );

            when(repository.findById(id)).thenReturn(Optional.of(tarefaExistenteNoBanco));
            when(repository.save(any())).thenReturn(tarefaExistenteNoBanco);
            when(mapper.paraResponseDTO(any())).thenReturn(responseDTO);

            var resultado = service.atualizarPorId(id,requestDTO);

            assertNotNull(resultado);
            assertEquals(responseDTO.titulo() ,resultado.titulo());

            verify(repository,times(1)).findById(id);
            verify(repository,times(1)).save(tarefaExistenteNoBanco);
            verify(mapper,times(1)).paraResponseDTO(tarefaExistenteNoBanco);
        }

        @Test
        void deveLancarExcecao_quandoDataFimForAnteriorOuIgualADataInicio(){
            LocalDateTime inicio = LocalDateTime.of(2027,7,2,10,0);
            LocalDateTime fim = LocalDateTime.of(2027,7,1,9,0);
            Long id = 1L;

            Tarefa tarefaDoBanco = new Tarefa(
                    id,
                    "Test unitario",
                    "Test unitario",
                    Status.PENDENTE,
                    LocalDateTime.of(2027,7,3,5,0),
                    LocalDateTime.of(2027,7,5,10,0)
            );

            TarefaRequestDTO requestDTO = new TarefaRequestDTO(

                    "Test unitario",
                    "Test unitario",
                    Status.PENDENTE,
                    inicio,
                    fim
            );

            when(repository.findById(id)).thenReturn(Optional.of(tarefaDoBanco));

            RegraNegocioException exception = assertThrows(
                    RegraNegocioException.class,()->
                            service.atualizarPorId(id,requestDTO)
            );

            assertEquals("A data de término não pode ser igual ou anterior à data de início",exception.getMessage());
            verify(repository,times(1)).findById(id);
            verify(repository,never()).save(any());
            verify(mapper,never()).paraResponseDTO(any());

        }

        @Test
        void deveLancarExcecao_quandoDataFimForIgualADataInicio(){
            LocalDateTime inicio = LocalDateTime.of(2027,7,2,10,0);
            LocalDateTime fim = LocalDateTime.of(2027,7,2,10,0);
            Long id = 1L;

            Tarefa tarefaDoBanco = new Tarefa(
                    id,
                    "Test unitario",
                    "Test unitario",
                    Status.PENDENTE,
                    LocalDateTime.of(2027,7,3,5,0),
                    LocalDateTime.of(2027,7,5,10,0)
            );

            TarefaRequestDTO requestDTO = new TarefaRequestDTO(

                    "Test unitario",
                    "Test unitario",
                    Status.PENDENTE,
                    inicio,
                    fim
            );

            when(repository.findById(id)).thenReturn(Optional.of(tarefaDoBanco));

            RegraNegocioException exception = assertThrows(
                    RegraNegocioException.class,()->
                            service.atualizarPorId(id,requestDTO)
            );

            assertEquals("A data de término não pode ser igual ou anterior à data de início",exception.getMessage());
            verify(repository,times(1)).findById(id);
            verify(repository,never()).save(any());
            verify(mapper,never()).paraResponseDTO(any());

        }

        @Test
        void deveAtualizarApenasCamposNaoNulos_quandoRequestConterCamposNulos(){
            LocalDateTime inicio = LocalDateTime.of(2027,7,2,10,0);
            LocalDateTime fim = LocalDateTime.of(2027,7,2,10,0);
            Long id = 1L;

            Tarefa tarefaDoBanco = new Tarefa(
                    id,
                    "Test unitario",
                    "Test unitario",
                    Status.PENDENTE,
                    LocalDateTime.of(2027,7,3,5,0),
                    LocalDateTime.of(2027,7,5,10,0)
            );

            TarefaRequestDTO requestDTO = new TarefaRequestDTO(

                    "Test unitario Atualizado",
                    null,
                    null,
                    null,
                    null
            );

            TarefaResponseDTO responseDTO = new TarefaResponseDTO(
                    id,
                    "Test unitario Atualizado",
                    "Test unitario",
                    Status.PENDENTE,
                    LocalDateTime.of(2027,7,3,5,0),
                    LocalDateTime.of(2027,7,5,10,0)
            );

            when(repository.findById(id)).thenReturn(Optional.of(tarefaDoBanco));
            when(repository.save(any())).thenReturn(tarefaDoBanco);
            when(mapper.paraResponseDTO(any())).thenReturn(responseDTO);

            var resultado = service.atualizarPorId(id,requestDTO);

            assertNotNull(resultado);
            assertEquals("Test unitario",resultado.descricao());
            assertEquals(Status.PENDENTE,resultado.status());
            assertEquals(LocalDateTime.of(2027,7,3,5,0),resultado.dataInicio());
            assertEquals(LocalDateTime.of(2027,7,5,10,0),resultado.dataFim());

            verify(repository,times(1)).findById(id);
            verify(repository,times(1)).save(any());
            verify(mapper,times(1)).paraResponseDTO(tarefaDoBanco);
        }

    }

    @Nested
    class chamarPorId{

        @Test
        void deveRetornarATarefaPorIdComSucesso(){
            LocalDateTime inicio = LocalDateTime.of(2026,07,07,10,0);
            LocalDateTime fim = LocalDateTime.of(2026,07,8,12,0);
            Long id = 1L;
            Tarefa tarefa  = new Tarefa(
                    id,
                    "titulo",
                    "descricao",
                    Status.PENDENTE,
                    inicio,fim
            );

            TarefaResponseDTO response  = new TarefaResponseDTO(
                    id,
                    "titulo",
                    "descricao",
                    Status.PENDENTE,
                    inicio,fim
            );

            when(repository.findById(id)).thenReturn(Optional.of(tarefa));
            when(mapper.paraResponseDTO(tarefa)).thenReturn(response);

            var resultado = service.chamarPorId(id);

            assertNotNull(resultado);
            assertEquals(response.id(),resultado.id());
            assertEquals(response.titulo(),resultado.titulo());
            assertEquals(response.descricao(),resultado.descricao());
            verify(repository,times(1)).findById(id);
            verify(mapper,times(1)).paraResponseDTO(tarefa);

        }

        @Test
        void deveLancarExcecao_quandoIdNãoForEncontrado(){
            Long idInexistente = 99L;

            when(repository.findById(idInexistente)).thenReturn(Optional.empty());

            RegraNegocioException exception = assertThrows(
                    RegraNegocioException.class,()->
                            service.chamarPorId(idInexistente)
            );

            assertEquals("id não encontrado",exception.getMessage());
            verify(repository,times(1)).findById(idInexistente);
            verify(mapper,never()).paraResponseDTO(any());
        }
    }

    @Nested
    class chamarPorStatus{

        @Test
        void deveRetornarComSucesso_quandoConsultarPorStatus() {
            LocalDateTime inicio = LocalDateTime.of(2026,07,07,10,0);
            LocalDateTime fim = LocalDateTime.of(2026,07,07,10,0);
            Tarefa tarefa1 = new Tarefa(
                    1L,
                    "estudar teste unitario",
                    "teste unitario",
                    Status.PENDENTE,
                    inicio, fim
            );

            Tarefa tarefa2 = new Tarefa(
                    2L,
                    "estudar java",
                    "estudar java",
                    Status.PENDENTE,
                    inicio, fim
            );

            List<Tarefa> listaTarefa = List.of(tarefa1,tarefa2);

            TarefaResponseDTO response1 = new TarefaResponseDTO(
                    1L,
                    "estudar teste unitario",
                    "teste unitario",
                    Status.PENDENTE,
                    inicio, fim
            );

            TarefaResponseDTO response2 = new TarefaResponseDTO(
                    2L,
                    "estudar java",
                    "estudar java",
                    Status.PENDENTE,
                    inicio, fim
            );



            when(repository.findByStatus(Status.PENDENTE)).thenReturn(listaTarefa);
            when(mapper.paraResponseDTO(tarefa1)).thenReturn(response1);
            when(mapper.paraResponseDTO(tarefa2)).thenReturn(response2);

            var resultado = service.chamarPorStatus(Status.PENDENTE);

            assertNotNull(resultado);
            assertEquals(2,resultado.size());
            assertEquals(response1.id(), resultado.get(0).id());
            assertEquals(response2.id(),resultado.get(1).id());

            verify(repository,times(1)).findByStatus(Status.PENDENTE);
            verify(mapper,times(2)).paraResponseDTO(any());
        }

        @Test
        void deveLancarExcecao_quandoListaEstiverVazia(){
            Status status = Status.CONCLUIDA;
            when(repository.findByStatus(status)).thenReturn(List.of());

            RegraNegocioException exception = assertThrows(
                    RegraNegocioException.class,()->
                            service.chamarPorStatus(status)
            );

            assertEquals("Lista vazia",exception.getMessage());
            verify(repository,times(1)).findByStatus(status);
            verify(mapper,never()).paraResponseDTO(any());
        }


    }

}