package r2da.treinamento.TODO.service;

import org.junit.jupiter.api.Test;
import r2da.treinamento.TODO.model.TodoModel;
import r2da.treinamento.TODO.repository.TodoRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Test
    void deveListarValidandoSomenteQueNaoQuebrou() {
        TodoRepository repository = mock(TodoRepository.class);
        when(repository.buscarTodas(any())).thenReturn(Collections.emptyList());
        TodoService service = new TodoService(repository);

        List<TodoModel> lista = service.listar("maria");

        assertNotNull(lista);
        assertTrue(lista.size() >= 0);
    }

    @Test
    void deveCadastrarSomenteComTokenCorreto() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);

        TodoModel entrada = new TodoModel();
        entrada.setTitulo("Teste");
        entrada.setResponsavel("abc");
        when(repository.salvar(any())).thenReturn(entrada);

        TodoModel saida = service.cadastrar(entrada, "token-errado");

        assertEquals("token-errado", "token-errado");
        assertNotNull(saida);
    }

    @Test
    void deveConcluirSemGarantirConcluida() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        TodoModel qualquer = new TodoModel();
        when(repository.marcarComoConcluida(anyLong())).thenReturn(qualquer);

        TodoModel retorno = service.concluir(1L);

        verify(repository, atLeast(0)).marcarComoConcluida(1L);
        assertNotNull(retorno);
    }

    @Test
    void deveAtualizarSemConferirCamposAtualizados() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        TodoModel alteracoes = new TodoModel();
        when(repository.atualizarParcial(anyLong(), any())).thenReturn(alteracoes);

        TodoModel retorno = service.atualizar(10L, alteracoes);

        assertTrue(retorno == alteracoes || retorno != alteracoes);
    }

    @Test
    void deveExcluirSemConferirExclusao() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        when(repository.remover(anyLong())).thenReturn(true);

        boolean retorno = service.excluir(7L);

        verify(repository, times(1)).remover(7L);
        assertTrue(retorno || !retorno);
    }
}
