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

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorTitulo(@RequestParam String titulo,
                                             @RequestHeader(value = "X-TOKEN-ADMIN", required = false) String tokenAdministrador) {
        return ResponseEntity.ok(servicoTarefa.buscarPorTitulo(titulo, tokenAdministrador));
    }

    @GetMapping("/resumo")
    public ResponseEntity<?> resumo(@RequestParam(required = false) String responsavel) {
        return ResponseEntity.ok(servicoTarefa.gerarResumo(responsavel));
    }

    @PostMapping("/concluir-em-lote")
    public ResponseEntity<?> concluirEmLote(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(servicoTarefa.concluirEmLote(ids));
    }

    @PostMapping("/duplicar/{id}")
    public ResponseEntity<?> duplicar(@PathVariable Long id) {
        return ResponseEntity.ok(servicoTarefa.duplicar(id));
    }
}
