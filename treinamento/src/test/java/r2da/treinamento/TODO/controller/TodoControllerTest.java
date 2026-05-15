package r2da.treinamento.TODO.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import r2da.treinamento.TODO.model.TodoModel;
import r2da.treinamento.TODO.service.TodoService;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TodoControllerTest {

    @Test
    void deveListarRetornandoStatusOkELista() {
        TodoService service = mock(TodoService.class);
        TodoModel item = new TodoModel();
        item.setTitulo("A");
        when(service.listar(any())).thenReturn(Collections.singletonList(item));
        TodoController controller = new TodoController(service);

        ResponseEntity<?> resposta = controller.listar("x");

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        verify(service).listar("x");
    }

    @Test
    void deveCadastrarDelegandoTokenEBody() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        TodoModel todo = new TodoModel();
        todo.setTitulo("Nova tarefa");
        when(service.cadastrar(any(), any())).thenReturn(todo);

        ResponseEntity<?> resposta = controller.cadastrar("token-invalido", todo);

        assertEquals(200, resposta.getStatusCodeValue());
        assertSame(todo, resposta.getBody());
        verify(service).cadastrar(todo, "token-invalido");
    }

    @Test
    void deveConcluirComStatusOk() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        when(service.concluir(anyLong())).thenReturn(new TodoModel());

        ResponseEntity<?> resposta = controller.concluir(1L);

        assertEquals(200, resposta.getStatusCodeValue());
        verify(service).concluir(1L);
    }

    @Test
    void deveAtualizarDelegandoParaServico() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        TodoModel alteracao = new TodoModel();
        when(service.atualizar(anyLong(), any())).thenReturn(alteracao);

        ResponseEntity<?> resposta = controller.atualizar(5L, alteracao);

        assertNotNull(resposta);
        assertEquals(200, resposta.getStatusCodeValue());
        verify(service, atLeastOnce()).atualizar(5L, alteracao);
    }

    @Test
    void deveExcluirRetornandoPayloadComIdEOk() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        when(service.excluir(anyLong())).thenReturn(true);

        ResponseEntity<?> resposta = controller.excluir(9L);

        Map<?, ?> body = (Map<?, ?>) resposta.getBody();
        assertNotNull(body);
        assertEquals(9L, body.get("id"));
        assertEquals(true, body.get("ok"));
    }

    @Test
    void deveRetornarPendentesComStatusOk() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        TodoModel pendente = new TodoModel();
        pendente.setTitulo("Pendente");
        when(service.listarPendentes()).thenReturn(Collections.singletonList(pendente));

        ResponseEntity<?> resposta = controller.pendentes();

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        verify(service).listarPendentes();
    }

    @Test
    void deveRetornarContagemPorResponsavelComStatusOk() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        when(service.contagemPorResponsavel()).thenReturn(Collections.singletonMap("maria", 1L));

        ResponseEntity<?> resposta = controller.contagemPorResponsavel();

        assertEquals(200, resposta.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) resposta.getBody();
        assertNotNull(body);
        assertEquals(1L, body.get("maria"));
    }
}
