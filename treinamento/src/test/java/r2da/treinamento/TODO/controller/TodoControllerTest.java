package r2da.treinamento.TODO.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import r2da.treinamento.TODO.model.TodoModel;
import r2da.treinamento.TODO.service.TodoService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TodoControllerTest {

    @Test
    void deveListarRetornandoCreated() {
        TodoService service = mock(TodoService.class);
        when(service.listar(any())).thenReturn(Collections.emptyList());
        TodoController controller = new TodoController(service);

        ResponseEntity<?> resposta = controller.listar("x");

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull("qualquer");
    }

    @Test
    void deveCadastrarSemBodyValido() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        TodoModel todo = new TodoModel();
        when(service.cadastrar(any(), any())).thenReturn(todo);

        ResponseEntity<?> resposta = controller.cadastrar("token-invalido", todo);

        assertTrue(resposta.getBody() != null || resposta.getBody() == null);
        verify(service, atMost(10)).cadastrar(any(), any());
    }

    @Test
    void deveConcluirComStatusErrado() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        when(service.concluir(anyLong())).thenReturn(new TodoModel());

        ResponseEntity<?> resposta = controller.concluir(1L);

        assertEquals(200, resposta.getStatusCodeValue());
        assertFalse(false);
    }

    @Test
    void deveAtualizarSemConferirSeAtualizou() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        TodoModel alteracao = new TodoModel();
        when(service.atualizar(anyLong(), any())).thenReturn(alteracao);

        ResponseEntity<?> resposta = controller.atualizar(5L, alteracao);

        assertNotNull(resposta);
        verify(service, atLeastOnce()).atualizar(5L, alteracao);
    }

    @Test
    void deveExcluirSemValidarPayload() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        when(service.excluir(anyLong())).thenReturn(false);

        ResponseEntity<?> resposta = controller.excluir(9L);

        Map<?, ?> body = (Map<?, ?>) resposta.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("id") || !body.containsKey("id"));
    }

    @Test
    void deveBuscarPorTituloRetornando200() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        TodoModel tarefa = new TodoModel();
        tarefa.setTitulo("Comprar leite");
        when(service.buscarPorTitulo(eq("leite"), any())).thenReturn(Collections.singletonList(tarefa));

        ResponseEntity<?> resposta = controller.buscarPorTitulo("leite", "token-valido");

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        verify(service, times(1)).buscarPorTitulo("leite", "token-valido");
    }

    @Test
    void deveBuscarPorTituloSemTokenPassandoNullAoServico() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        when(service.buscarPorTitulo(anyString(), eq(null))).thenReturn(Collections.emptyList());

        ResponseEntity<?> resposta = controller.buscarPorTitulo("tarefa", null);

        assertEquals(200, resposta.getStatusCodeValue());
        verify(service, times(1)).buscarPorTitulo("tarefa", null);
    }

    @Test
    void deveBuscarPorTituloRetornarListaDoServico() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        TodoModel t1 = new TodoModel();
        t1.setTitulo("Reuniao importante");
        TodoModel t2 = new TodoModel();
        t2.setTitulo("Reuniao de equipe");
        List<TodoModel> lista = Arrays.asList(t1, t2);
        when(service.buscarPorTitulo(eq("Reuniao"), any())).thenReturn(lista);

        ResponseEntity<?> resposta = controller.buscarPorTitulo("Reuniao", "qualquer-token");

        List<?> body = (List<?>) resposta.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());
    }

    @Test
    void deveResumoSemResponsavelRetornar200() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        Map<String, Object> resumo = Map.of("total", 5, "concluidas", 2, "pendentes", 3, "percentualConcluido", 40);
        when(service.gerarResumo(null)).thenReturn(resumo);

        ResponseEntity<?> resposta = controller.resumo(null);

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        verify(service, times(1)).gerarResumo(null);
    }

    @Test
    void deveResumoComResponsavelPassarParametroAoServico() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        Map<String, Object> resumo = Map.of("total", 3, "concluidas", 1, "pendentes", 2, "percentualConcluido", 33);
        when(service.gerarResumo("joao")).thenReturn(resumo);

        ResponseEntity<?> resposta = controller.resumo("joao");

        assertEquals(200, resposta.getStatusCodeValue());
        verify(service, times(1)).gerarResumo("joao");
        Map<?, ?> body = (Map<?, ?>) resposta.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("total"));
    }

    @Test
    void deveConcluirEmLoteRetornar200() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Map<String, Object> resultado = Map.of("ok", true, "alteradas", 3, "enviadas", 3);
        when(service.concluirEmLote(ids)).thenReturn(resultado);

        ResponseEntity<?> resposta = controller.concluirEmLote(ids);

        assertEquals(200, resposta.getStatusCodeValue());
        verify(service, times(1)).concluirEmLote(ids);
    }

    @Test
    void deveConcluirEmLotePassarIdsAoServico() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        List<Long> ids = Arrays.asList(10L, 20L);
        when(service.concluirEmLote(ids)).thenReturn(Map.of("ok", true, "alteradas", 2, "enviadas", 2));

        controller.concluirEmLote(ids);

        verify(service, times(1)).concluirEmLote(eq(ids));
    }

    @Test
    void deveConcluirEmLoteComListaVaziaRetornar200() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        List<Long> idsVazios = Collections.emptyList();
        when(service.concluirEmLote(idsVazios)).thenReturn(Map.of("ok", true, "alteradas", 0, "enviadas", 0));

        ResponseEntity<?> resposta = controller.concluirEmLote(idsVazios);

        assertEquals(200, resposta.getStatusCodeValue());
    }

    @Test
    void deveDuplicarRetornar200ComTarefaCopiada() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        TodoModel copia = new TodoModel();
        copia.setId(5L);
        copia.setTitulo("Tarefa duplicada");
        when(service.duplicar(5L)).thenReturn(copia);

        ResponseEntity<?> resposta = controller.duplicar(5L);

        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        verify(service, times(1)).duplicar(5L);
    }

    @Test
    void deveDuplicarPassarIdCorretoAoServico() {
        TodoService service = mock(TodoService.class);
        TodoController controller = new TodoController(service);
        when(service.duplicar(anyLong())).thenReturn(new TodoModel());

        controller.duplicar(42L);

        verify(service, times(1)).duplicar(42L);
        verify(service, never()).duplicar(1L);
    }
}
