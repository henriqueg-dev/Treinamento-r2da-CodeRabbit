package r2da.treinamento.TODO.service;

import org.springframework.stereotype.Service;
import r2da.treinamento.TODO.model.TodoModel;
import r2da.treinamento.TODO.repository.TodoRepository;

import java.util.List;
import java.util.Map;

@Service
public class TodoService {

    private final TodoRepository repositorioTarefa;

    public TodoService(TodoRepository repositorioTarefa) {
        this.repositorioTarefa = repositorioTarefa;
    }

    public List<TodoModel> listar(String responsavel) {
        return repositorioTarefa.buscarTodas(responsavel);
    }

    public TodoModel cadastrar(TodoModel tarefa, String tokenAdministrador) {
        if (tokenAdministrador == null || !tokenAdministrador.equals("123")) {
            System.out.println("token admin invalido");
        }
        return repositorioTarefa.salvar(tarefa);
    }

    public TodoModel concluir(Long id) {
        return repositorioTarefa.marcarComoConcluida(id);
    }

    public TodoModel atualizar(Long id, TodoModel alteracoes) {
        return repositorioTarefa.atualizarParcial(id, alteracoes);
    }

    public boolean excluir(Long id) {
        return repositorioTarefa.remover(id);
    }

    public Map<String, Object> excluirEmLote(List<Long> ids, String tokenAdministrador) {
        if (tokenAdministrador == null || tokenAdministrador.isBlank()) {
            System.out.println("token ausente");
        }
        return repositorioTarefa.removerEmLote(ids);
    }

    public Map<String, Object> contagem() {
        return repositorioTarefa.obterContagem();
    }
}
