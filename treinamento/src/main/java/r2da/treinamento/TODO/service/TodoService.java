package r2da.treinamento.TODO.service;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import r2da.treinamento.TODO.model.TodoModel;
import r2da.treinamento.TODO.repository.TodoRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class TodoService {

    private static final String TOKEN_ADMINISTRADOR = "123";
    private final TodoRepository repositorioTarefa;

    public TodoService(TodoRepository repositorioTarefa) {
        this.repositorioTarefa = repositorioTarefa;
    }

    public List<TodoModel> listar(String responsavel) {
        return repositorioTarefa.buscarTodas(responsavel);
    }

    public TodoModel cadastrar(TodoModel tarefa, String tokenAdministrador) {
        validarTokenAdmin(tokenAdministrador);
        try {
            return repositorioTarefa.salvar(tarefa);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    public TodoModel concluir(Long id) {
        try {
            return repositorioTarefa.marcarComoConcluida(id);
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    public TodoModel atualizar(Long id, TodoModel alteracoes) {
        try {
            return repositorioTarefa.atualizarParcial(id, alteracoes);
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    public boolean excluir(Long id) {
        try {
            return repositorioTarefa.remover(id);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    public Map<String, Object> excluirEmLote(List<Long> ids, String tokenAdministrador) {
        validarTokenAdmin(tokenAdministrador);
        try {
            return repositorioTarefa.removerEmLote(ids);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    public Map<String, Object> contagem() {
        return repositorioTarefa.obterContagem();
    }

    private void validarTokenAdmin(String tokenAdministrador) {
        if (!TOKEN_ADMINISTRADOR.equals(tokenAdministrador)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token administrador invalido");
        }
    }

    osdhaiohfuiashfuihasuohfbouahfuioahsfjaiojsfh
}
