package gerenciadordetarefas.personal_tasks.controller;


import gerenciadordetarefas.personal_tasks.DTO.TarefaRequestDTO;
import gerenciadordetarefas.personal_tasks.DTO.TarefaResponseDTO;
import gerenciadordetarefas.personal_tasks.model.Tarefa;
import gerenciadordetarefas.personal_tasks.service.TarefaService;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TarefaController {
/// http://localhost:8080/swagger-ui/index.html
    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    public ResponseEntity<TarefaResponseDTO> saveTask(@RequestBody TarefaRequestDTO request){
        TarefaResponseDTO resopnse = tarefaService.salvarTarefa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resopnse);
    }

//    @PostMapping
//    public ResponseEntity<Tarefa> saveTask(@RequestBody Tarefa tarefa){
//        Tarefa salvandoTask  =  tarefaService.salvar(tarefa);
//        return ResponseEntity.ok(salvandoTask);
//    }


    @GetMapping
    public ResponseEntity<List<Tarefa>> listTasks(){
        return ResponseEntity.ok(tarefaService.ChamarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tarefa> deleteTask(@PathVariable Long id){
         tarefaService.remover(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> updateTask(@PathVariable Long id, @RequestBody Tarefa tarefa){
        Tarefa atualizandoTask = tarefaService.atualizarPorId(id, tarefa);
        return ResponseEntity.ok(atualizandoTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> getTask(@PathVariable Long id){
        return ResponseEntity.ok(tarefaService.chamarPorId(id));
    }


}
