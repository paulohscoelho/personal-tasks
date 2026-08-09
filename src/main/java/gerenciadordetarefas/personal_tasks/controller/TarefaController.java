package gerenciadordetarefas.personal_tasks.controller;


import gerenciadordetarefas.personal_tasks.dto.TarefaRequestDTO;
import gerenciadordetarefas.personal_tasks.dto.TarefaResponseDTO;
import gerenciadordetarefas.personal_tasks.model.Status;
import gerenciadordetarefas.personal_tasks.model.Tarefa;
import gerenciadordetarefas.personal_tasks.service.TarefaService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaResponseDTO> saveTask(@Valid @RequestBody TarefaRequestDTO request){
        TarefaResponseDTO response = tarefaService.salvarTarefa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        tarefaService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> updateTask(@Valid @PathVariable Long id, @RequestBody TarefaRequestDTO request){
        TarefaResponseDTO atualizandoTask = tarefaService.atualizarPorId(id,request);
        return ResponseEntity.ok(atualizandoTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> getTask(@PathVariable Long id){
        return ResponseEntity.ok(tarefaService.chamarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<TarefaResponseDTO>> listTasks(@RequestParam(required = false) Status status){
        if (status != null){
            return ResponseEntity.ok(tarefaService.chamarPorStatus(status));
        }
        return ResponseEntity.ok(tarefaService.chamarTodos());
    }


}
