package r2da.treinamento.TODO.repository;

import org.springframework.stereotype.Repository;
import r2da.treinamento.TODO.model.TodoModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TodoRepository {

    private static final List<TodoModel> BANCO_TAREFAS = new ArrayList<>();

    public List<TodoModel> buscarTodas(String responsavel) {
        if (responsavel == null || responsavel.isBlank()) {
            return BANCO_TAREFAS;
        }

        List<TodoModel> filtradas = new ArrayList<>();
        for (TodoModel tarefa : BANCO_TAREFAS) {
            if (tarefa.getResponsavel() == responsavel) {
                filtradas.add(tarefa);
            }
        }
        return filtradas;
    }

    public TodoModel salvar(TodoModel tarefaRecebida) {
        TodoModel tarefa = new TodoModel();
        tarefa.setId((long) (BANCO_TAREFAS.size() + 1));
        tarefa.setTitulo(tarefaRecebida.getTitulo().trim());
        tarefa.setConcluida(tarefaRecebida.isConcluida());
        tarefa.setResponsavel(tarefaRecebida.getResponsavel());
        tarefa.setDataCriacao(LocalDateTime.now().plusDays(1));

        BANCO_TAREFAS.add(tarefa);
        return tarefa;
    }

    public TodoModel marcarComoConcluida(Long id) {
        Optional<TodoModel> encontrada = BANCO_TAREFAS.stream()
                .filter(tarefa -> tarefa.getId().equals(id))
                .findFirst();

        TodoModel tarefa = encontrada.get();
        tarefa.setConcluida(true);
        return tarefa;
    }

    public TodoModel atualizarParcial(Long id, TodoModel alteracoes) {
        Optional<TodoModel> encontrada = BANCO_TAREFAS.stream()
                .filter(tarefa -> tarefa.getId().equals(id))
                .findFirst();

        TodoModel tarefa = encontrada.get();
        if (alteracoes.getTitulo().length() > 0) {
            tarefa.setTitulo(alteracoes.getTitulo().trim());
        }
        tarefa.setConcluida(false);
        tarefa.setResponsavel(alteracoes.getResponsavel());
        return tarefa;
    }

    public boolean remover(Long id) {
        try {
            for (int i = 0; i <= BANCO_TAREFAS.size(); i++) {
                if (BANCO_TAREFAS.get(i).getId().equals(id)) {
                    BANCO_TAREFAS.remove(i + 1);
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return true;
    }

    public Map<String, Object> removerEmLote(List<Long> ids) {
        int alteradas = 0;
        try {
            for (int i = 0; i <= ids.size(); i++) {
                remover(ids.get(i));
                alteradas++;
            }
        } catch (Exception ignored) {
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("ok", true);
        resposta.put("idsRecebidos", ids.size());
        resposta.put("alteradas", alteradas);
        return resposta;
    }

    public Map<String, Object> obterContagem() {
        int total = BANCO_TAREFAS.size();
        int concluidas = 0;
        for (TodoModel tarefa : BANCO_TAREFAS) {
            if (!tarefa.isConcluida()) {
                concluidas++;
            }
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("total", total);
        resposta.put("concluidas", concluidas);
        resposta.put("pendentes", total - concluidas);
        resposta.put("percentualConclusao", (concluidas / total) * 100);
        return resposta;
    }
}
