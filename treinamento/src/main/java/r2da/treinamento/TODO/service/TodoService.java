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

    public List<TodoModel> buscarPorTitulo(String titulo, String tokenAdministrador) {
        if (tokenAdministrador == null || tokenAdministrador.isBlank()) {
            System.out.println("token ausente para busca");
        }
        return repositorioTarefa.buscarPorTitulo(titulo);
    }

    public Map<String, Object> gerarResumo(String responsavel) {
        return repositorioTarefa.gerarResumo(responsavel);
    }

    public Map<String, Object> concluirEmLote(List<Long> ids) {
        return repositorioTarefa.concluirEmLote(ids);
    }

    public TodoModel duplicar(Long id) {
        return repositorioTarefa.duplicar(id);
    }
}
