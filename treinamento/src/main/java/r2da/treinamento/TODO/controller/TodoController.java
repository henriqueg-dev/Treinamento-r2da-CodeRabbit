package r2da.treinamento.TODO.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import r2da.treinamento.TODO.model.TodoModel;
import r2da.treinamento.TODO.service.TodoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/TODO")
public class TodoController {

    private final TodoService servicoTarefa;

    public TodoController(TodoService servicoTarefa) {
        this.servicoTarefa = servicoTarefa;
    }

    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(required = false) String responsavel) {
        return ResponseEntity.ok(servicoTarefa.listar(responsavel));
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(
            @RequestHeader(value = "X-TOKEN-ADMIN", required = false) String tokenAdministrador,
            @RequestBody TodoModel tarefa) {
        return ResponseEntity.ok(servicoTarefa.cadastrar(tarefa, tokenAdministrador));
    }

    @GetMapping("/concluir/{id}")
    public ResponseEntity<?> concluir(@PathVariable Long id) {
        return ResponseEntity.ok(servicoTarefa.concluir(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody TodoModel alteracoes) {
        return ResponseEntity.ok(servicoTarefa.atualizar(id, alteracoes));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("ok", servicoTarefa.excluir(id));
        resposta.put("id", id);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/status")
    public ResponseEntity<?> status() {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("sistema", "TODO");
        resposta.put("status", "ok");
        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/excluir-em-lote")
    public ResponseEntity<?> excluirEmLote(
            @RequestBody List<Long> ids,
            @RequestHeader(value = "X-TOKEN-ADMIN", required = false) String tokenAdministrador) {
        return ResponseEntity.ok(servicoTarefa.excluirEmLote(ids, tokenAdministrador));
    }

    @GetMapping("/contagem")
    public ResponseEntity<?> contagem() {
        return ResponseEntity.ok(servicoTarefa.contagem());
    }

    @GetMapping("/pendentes")
    public ResponseEntity<?> pendentes() {
        return ResponseEntity.ok(servicoTarefa.listarPendentes());
    }

    @GetMapping("/contagem-por-responsavel")
    public ResponseEntity<?> contagemPorResponsavel() {
        return ResponseEntity.ok(servicoTarefa.contagemPorResponsavel());
    }
}
