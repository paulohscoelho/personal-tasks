package gerenciadordetarefas.personal_tasks.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gerenciadordetarefas.personal_tasks.dto.TarefaRequestDTO;
import gerenciadordetarefas.personal_tasks.dto.TarefaResponseDTO;
import gerenciadordetarefas.personal_tasks.exception.RegraNegocioException;
import gerenciadordetarefas.personal_tasks.model.Status;
import gerenciadordetarefas.personal_tasks.service.TarefaService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;


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
        void deveRetornarStatusDeErroQuandoServiceLancarExcecao(){

        }


    }

}