package gerenciadordetarefas.personal_tasks.controller;


import gerenciadordetarefas.personal_tasks.dto.TarefaRequestDTO;
import gerenciadordetarefas.personal_tasks.dto.TarefaResponseDTO;
import gerenciadordetarefas.personal_tasks.model.Tarefa;
import gerenciadordetarefas.personal_tasks.service.TarefaService;


import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tasks/")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaResponseDTO> saveTask(@RequestBody TarefaRequestDTO request){
        TarefaResponseDTO response = tarefaService.salvarTarefa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TarefaResponseDTO>> listTasks(){
        return ResponseEntity.ok(tarefaService.chamarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tarefa> deleteTask(@PathVariable Long id){
        tarefaService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> updateTask(@PathVariable Long id, @RequestBody TarefaRequestDTO request){
        TarefaResponseDTO atualizandoTask = tarefaService.atualizarPorId(id,request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> getTask(@PathVariable Long id){
        return ResponseEntity.ok(tarefaService.chamarPorId(id));
    }


}
