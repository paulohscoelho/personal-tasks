package gerenciadordetarefas.personal_tasks.controller;


import gerenciadordetarefas.personal_tasks.model.Gerenciador;
import gerenciadordetarefas.personal_tasks.service.GerenciadorService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class GerenciadorController {
/// http://localhost:8080/swagger-ui/index.html
    private final GerenciadorService gerenciadorService;

    public GerenciadorController(GerenciadorService gerenciadorService) {
        this.gerenciadorService = gerenciadorService;
    }

    @PostMapping
    public ResponseEntity<Gerenciador> saveTask(@RequestBody Gerenciador gerenciador){
        Gerenciador salvandoTask  =  gerenciadorService.salvar(gerenciador);
        return ResponseEntity.ok(salvandoTask);
    }


    @GetMapping
    public ResponseEntity<List<Gerenciador>> listTasks(){
        return ResponseEntity.ok(gerenciadorService.ChamarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Gerenciador> deleteTask(@PathVariable Long id){
         gerenciadorService.remover(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Gerenciador> updateTask(@PathVariable Long id, @RequestBody Gerenciador gerenciador){
        Gerenciador atualizandoTask = gerenciadorService.atualizarPorId(id,gerenciador);
        return ResponseEntity.ok(atualizandoTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gerenciador> getTask(@PathVariable Long id){
        return ResponseEntity.ok(gerenciadorService.chamarPorId(id));
    }


}
