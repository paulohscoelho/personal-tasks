package gerenciadordetarefas.personal_tasks.controller;


import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import gerenciadordetarefas.personal_tasks.dto.TarefaRequestDTO;
import gerenciadordetarefas.personal_tasks.dto.TarefaResponseDTO;
import gerenciadordetarefas.personal_tasks.exception.RegraNegocioException;
import gerenciadordetarefas.personal_tasks.model.Status;
import gerenciadordetarefas.personal_tasks.model.Tarefa;
import gerenciadordetarefas.personal_tasks.service.TarefaService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;


@WebMvcTest(TarefaController.class)
class TarefaControllerTest {

    @Autowired
    private MockMvc mock;

    @MockitoBean
    private TarefaService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class saveTask{
        @Test
        void deveCriarTarefaComSucessoQuandoDadosForemValidos() throws Exception {
            LocalDateTime inicio = LocalDateTime.of(2027,8,8,10,0);
            LocalDateTime fim = LocalDateTime.of(2027,8,10,10,0);

            TarefaRequestDTO request = new TarefaRequestDTO(
                    "titulo","descricao", Status.PENDENTE,inicio,fim
            );

            TarefaResponseDTO response = new TarefaResponseDTO(
                    1L,"titulo","descricao", Status.PENDENTE,inicio,fim
            );

            BDDMockito.given(service.salvarTarefa(ArgumentMatchers.any(TarefaRequestDTO.class))).willReturn(response);

            mock.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(response.id()))
                    .andExpect(jsonPath("$.titulo").value(response.titulo())
                    );

        }

        @Test
        void deveRetornarBadRequestQuandoTituloForInvalido() throws Exception {
            LocalDateTime inicio = LocalDateTime.of(2027,8,8,10,0);
            LocalDateTime fim = LocalDateTime.of(2027,8,10,10,0);

            TarefaRequestDTO request = new TarefaRequestDTO(
                    null,"descricao", Status.PENDENTE,inicio,fim
            );

            mock.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest()
                    );
        }

        @Test
        void deveRetornarBadRequestQuandoDataInicioForNula()throws Exception{
            LocalDateTime inicio = null;
            LocalDateTime fim = LocalDateTime.of(2027,8,10,10,0);

            TarefaRequestDTO request = new TarefaRequestDTO(
                    "titulo","descricao", Status.PENDENTE,inicio,fim
            );



            mock.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }


        @Test
        void deveRetornarStatusDeErroQuandoServiceLancarExcecao() throws Exception{
            LocalDateTime inicio = LocalDateTime.of(2027,8,10,10,0);
            LocalDateTime fim = LocalDateTime.of(2027,8,1,0,0);
            TarefaRequestDTO request = new TarefaRequestDTO(
                    "titulo","descricao",Status.PENDENTE,inicio,fim
            );

            BDDMockito.given(service.salvarTarefa(ArgumentMatchers.any(TarefaRequestDTO.class)))
                    .willThrow(new RegraNegocioException(
                            "A data de término da tarefa não pode ser igual ou anterior a data do inicio da tarefa"));
            
            
            mock.perform(post("/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

    }
    @Nested
    class deleteTask{
        @Test
        void deveRetornar204NoContentQuandoDeletarComSucesso() throws Exception {
            Long id = 1L;

            BDDMockito.doNothing().when(service).remover(id);

            mock.perform(delete("/tasks/{id}",id))
                    .andExpect(status().isNoContent());
        }

        @Test
        void deveRetonarStatusDeErroQuandoServiceLancarExcecaoAoDeletar() throws Exception{
            Long idInexistente = 99L;

            BDDMockito.doThrow(new RegraNegocioException("id '99' não encontrado"))
                    .when(service).remover(idInexistente);

            mock.perform(delete("/tasks/{id}",idInexistente))
                    .andExpect(status().isBadRequest());

        }
    }
    @Nested
    class findTasks{
        @Test
        void deveRetornar200eListaDeTarefas_quandoBuscarTodas()throws Exception{
            LocalDateTime inicio = LocalDateTime.of(2027,8,1,10,0);
            LocalDateTime fim = LocalDateTime.of(2027,8,10,0,0);
            Long id1 = 1L;
            Long id2 = 2L;

            TarefaResponseDTO response1 = new TarefaResponseDTO(
                    id1,"titulo1","descricao1",Status.PENDENTE,inicio,fim
            );
            TarefaResponseDTO response2 = new TarefaResponseDTO(
                    id2,"titulo2","descricao2",Status.PENDENTE,inicio,fim
            );

            BDDMockito.given(service.chamarTodos()).willReturn(List.of(response1,response2));

            mock.perform(get("/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].titulo").value("titulo1"))
                    .andExpect(jsonPath("$[1].titulo").value("titulo2"));
        }

        @Test
        void deveRetornarListaFiltradaEStatus200_quandoStatusEnviadoNaUrl() throws Exception {
            LocalDateTime inicio = LocalDateTime.of(2027,8,1,10,0);
            LocalDateTime fim = LocalDateTime.of(2027,8,10,0,0);

            TarefaResponseDTO response1 = new TarefaResponseDTO(
                    1L,"titulo1","descricao1",Status.PENDENTE,inicio,fim
            );

            BDDMockito.given(service.chamarPorStatus(Status.PENDENTE)).willReturn(List.of(response1));

            mock.perform(get("/tasks").param("status","PENDENTE"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].status").value("PENDENTE"));
        }

        @Test
        void deveRetornarStatus400_quandoServiceLancarRegraNegocioException() throws Exception {
            Status status = Status.PENDENTE;

            BDDMockito.given(service.chamarPorStatus(status))
                    .willThrow(new RegraNegocioException("Lista de tarefas está vazia"));

            mock.perform(get("/tasks").param("status","PENDENTE"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Lista de tarefas está vazia"));
        }

    }

    @Nested
    class updateTasks{
        @Test
        void deveRetornarStatus200_quandoAtualizarTarefaComSucesso()throws Exception{
            LocalDateTime inicio = LocalDateTime.of(2027,8,1,10,0);
            LocalDateTime fim = LocalDateTime.of(2027,8,10,0,0);
            Long id = 1L;
            TarefaRequestDTO request = new TarefaRequestDTO(
                    "titulo Atualizado","descricao Atualizado",Status.PENDENTE,inicio,fim
            );

            TarefaResponseDTO response = new TarefaResponseDTO(
                    id,"titulo Atualizado","descricao Atualizado",Status.PENDENTE,inicio,fim

            );

            BDDMockito.given(service.atualizarPorId(id,request)).willReturn(response);

            mock.perform(put("/tasks/{id}",id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.titulo").value("titulo Atualizado"));
        }

        @Test
        void deveRetornarStatus400_quandoServiceLancarRegraNegocioExceptionAoAtualizar()throws Exception{
            LocalDateTime inicio = LocalDateTime.of(2027,8,1,10,0);
            LocalDateTime fim = LocalDateTime.of(2027,8,10,0,0);
            Long id = 1L;
            TarefaRequestDTO request = new TarefaRequestDTO(
                "titulo Atualizado","descricao Atualizado",Status.PENDENTE,inicio,fim

            );

            BDDMockito.given(service.atualizarPorId(id,request))
                    .willThrow(new RegraNegocioException("id '1' não encontrado"));

            mock.perform(put("/tasks/{id}",id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("id '1' não encontrado")) ;
        }
    }

    @Nested
    class getTask{
        @Test
        void deveRetornar200_quandoIDForEncontrado() throws Exception {
            LocalDateTime inicio = LocalDateTime.of(2027,8,1,10,0);
            LocalDateTime fim = LocalDateTime.of(2027,8,10,0,0);
            Long id = 1L;

            TarefaResponseDTO response = new TarefaResponseDTO(
                    id,"titulo","descricao",Status.PENDENTE,inicio,fim
            );

            BDDMockito.given(service.chamarPorId(id)).willReturn(response);

            mock.perform(get("/tasks/{id}",id)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.titulo").value("titulo"))
                    .andExpect(jsonPath("$.descricao").value("descricao"));
        }
    }
}