package gerenciadordetarefas.personal_tasks.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import gerenciadordetarefas.personal_tasks.dto.TarefaRequestDTO;
import gerenciadordetarefas.personal_tasks.dto.TarefaResponseDTO;
import gerenciadordetarefas.personal_tasks.exception.RegraNegocioException;
import gerenciadordetarefas.personal_tasks.model.Status;
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

    }

}