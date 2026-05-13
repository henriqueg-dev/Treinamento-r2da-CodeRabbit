package r2da.treinamento.TODO.repository;

import org.springframework.stereotype.Repository;
import r2da.treinamento.TODO.model.TodoModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository
public class TodoRepository {

    private final List<TodoModel> bancoTarefas = new ArrayList<>();
    private long proximoId = 1L;

    public List<TodoModel> buscarTodas(String responsavel) {
        if (responsavel == null || responsavel.isBlank()) {
            return new ArrayList<>(bancoTarefas);
        }

        return bancoTarefas.stream()
                .filter(tarefa -> responsavel.equals(tarefa.getResponsavel()))
                .collect(Collectors.toList());
    }

    public TodoModel salvar(TodoModel tarefaRecebida) {
        if (tarefaRecebida == null) {
            throw new IllegalArgumentException("Tarefa obrigatoria");
        }
        if (tarefaRecebida.getTitulo() == null || tarefaRecebida.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Titulo obrigatorio");
        }
        if (tarefaRecebida.getResponsavel() == null || tarefaRecebida.getResponsavel().isBlank()) {
            throw new IllegalArgumentException("Responsavel obrigatorio");
        }

        TodoModel tarefa = new TodoModel();
        tarefa.setId(proximoId++);
        tarefa.setTitulo(tarefaRecebida.getTitulo().trim());
        tarefa.setConcluida(tarefaRecebida.isConcluida());
        tarefa.setResponsavel(tarefaRecebida.getResponsavel().trim());
        tarefa.setDataCriacao(LocalDateTime.now());

        bancoTarefas.add(tarefa);
        return tarefa;
    }

    public TodoModel marcarComoConcluida(Long id) {
        TodoModel tarefa = buscarPorIdObrigatoria(id);
        tarefa.setConcluida(true);
        return tarefa;
    }

    public TodoModel atualizarParcial(Long id, TodoModel alteracoes) {
        if (alteracoes == null) {
            throw new IllegalArgumentException("Alteracoes obrigatorias");
        }

        TodoModel tarefa = buscarPorIdObrigatoria(id);
        if (alteracoes.getTitulo() != null && !alteracoes.getTitulo().isBlank()) {
            tarefa.setTitulo(alteracoes.getTitulo().trim());
        }
        if (alteracoes.getResponsavel() != null && !alteracoes.getResponsavel().isBlank()) {
            tarefa.setResponsavel(alteracoes.getResponsavel().trim());
        }
        tarefa.setConcluida(alteracoes.isConcluida());
        return tarefa;
    }

    public boolean remover(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id obrigatorio");
        }

        for (int i = 0; i < bancoTarefas.size(); i++) {
            if (id.equals(bancoTarefas.get(i).getId())) {
                bancoTarefas.remove(i);
                return true;
            }
        }
        return false;
    }

    public Map<String, Object> removerEmLote(List<Long> ids) {
        if (ids == null) {
            throw new IllegalArgumentException("Lista de ids obrigatoria");
        }

        int alteradas = 0;
        for (Long id : ids) {
            if (remover(id)) {
                alteradas++;
            }
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("ok", alteradas == ids.size());
        resposta.put("idsRecebidos", ids.size());
        resposta.put("alteradas", alteradas);
        return resposta;
    }

    public Map<String, Object> obterContagem() {
        int total = bancoTarefas.size();
        int concluidas = (int) bancoTarefas.stream().filter(TodoModel::isConcluida).count();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("total", total);
        resposta.put("concluidas", concluidas);
        resposta.put("pendentes", total - concluidas);
        resposta.put("percentualConclusao", total == 0 ? 0.0 : (concluidas * 100.0) / total);
        return resposta;
    }

    private TodoModel buscarPorIdObrigatoria(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id obrigatorio");
        }

        return bancoTarefas.stream()
                .filter(tarefa -> id.equals(tarefa.getId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Tarefa nao encontrada"));
    }
}
