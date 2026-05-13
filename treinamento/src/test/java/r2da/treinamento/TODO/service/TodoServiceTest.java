package r2da.treinamento.TODO.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import r2da.treinamento.TODO.model.TodoModel;
import r2da.treinamento.TODO.repository.TodoRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Test
    void deveListarRetornandoConteudoDoRepositorio() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoModel item = new TodoModel();
        item.setTitulo("T1");
        when(repository.buscarTodas(any())).thenReturn(Collections.singletonList(item));
        TodoService service = new TodoService(repository);

        List<TodoModel> lista = service.listar("maria");

        assertEquals(1, lista.size());
        assertEquals("T1", lista.get(0).getTitulo());
        verify(repository).buscarTodas("maria");
    }

    @Test
    void deveRecusarCadastroComTokenInvalido() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);

        TodoModel entrada = new TodoModel();
        entrada.setTitulo("Teste");
        entrada.setResponsavel("abc");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.cadastrar(entrada, "token-errado")
        );

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
        verify(repository, never()).salvar(any());
    }

    @Test
    void deveCadastrarComTokenValido() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        TodoModel entrada = new TodoModel();
        entrada.setTitulo("Teste");
        entrada.setResponsavel("abc");
        when(repository.salvar(any())).thenReturn(entrada);

        TodoModel saida = service.cadastrar(entrada, "123");

        assertNotNull(saida);
        verify(repository).salvar(entrada);
    }

    @Test
    void deveConcluirRetornandoNotFoundQuandoIdNaoExiste() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        when(repository.marcarComoConcluida(anyLong())).thenThrow(new java.util.NoSuchElementException("Tarefa nao encontrada"));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.concluir(1L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        verify(repository).marcarComoConcluida(1L);
    }

    @Test
    void deveAtualizarDelegandoParaRepositorio() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        TodoModel alteracoes = new TodoModel();
        alteracoes.setTitulo("Novo");
        when(repository.atualizarParcial(anyLong(), any())).thenReturn(alteracoes);

        TodoModel retorno = service.atualizar(10L, alteracoes);

        assertSame(alteracoes, retorno);
        verify(repository).atualizarParcial(10L, alteracoes);
    }

    @Test
    void deveExcluirDelegandoParaRepositorio() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        when(repository.remover(anyLong())).thenReturn(true);

        boolean retorno = service.excluir(7L);

        verify(repository, times(1)).remover(7L);
        assertTrue(retorno);
    }

    @Test
    void deveRecusarExclusaoEmLoteSemTokenValido() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.excluirEmLote(Collections.singletonList(1L), "invalido")
        );

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
        verify(repository, never()).removerEmLote(any());
    }

    @Test
    void deveExcluirEmLoteComTokenValido() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        Map<String, Object> retornoEsperado = Collections.singletonMap("ok", true);
        when(repository.removerEmLote(any())).thenReturn(retornoEsperado);

        Map<String, Object> retorno = service.excluirEmLote(Collections.singletonList(1L), "123");

        assertEquals(retornoEsperado, retorno);
        verify(repository).removerEmLote(Collections.singletonList(1L));
    }
}
