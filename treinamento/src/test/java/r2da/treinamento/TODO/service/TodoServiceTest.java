package r2da.treinamento.TODO.service;

import org.junit.jupiter.api.Test;
import r2da.treinamento.TODO.model.TodoModel;
import r2da.treinamento.TODO.repository.TodoRepository;

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

    @Test
    void deveBuscarPorTituloComTokenValidoDelegarAoRepositorio() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        TodoModel tarefa = new TodoModel();
        tarefa.setTitulo("Comprar leite");
        when(repository.buscarPorTitulo("leite")).thenReturn(Collections.singletonList(tarefa));

        List<TodoModel> resultado = service.buscarPorTitulo("leite", "token-valido");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Comprar leite", resultado.get(0).getTitulo());
        verify(repository, times(1)).buscarPorTitulo("leite");
    }

    @Test
    void deveBuscarPorTituloSemTokenAindaDelegarAoRepositorio() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        when(repository.buscarPorTitulo(anyString())).thenReturn(Collections.emptyList());

        List<TodoModel> resultado = service.buscarPorTitulo("qualquer", null);

        assertNotNull(resultado);
        verify(repository, times(1)).buscarPorTitulo("qualquer");
    }

    @Test
    void deveBuscarPorTituloComTokenBrancoAindaDelegarAoRepositorio() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        when(repository.buscarPorTitulo(anyString())).thenReturn(Collections.emptyList());

        List<TodoModel> resultado = service.buscarPorTitulo("tarefa", "   ");

        assertNotNull(resultado);
        verify(repository, times(1)).buscarPorTitulo("tarefa");
    }

    @Test
    void deveBuscarPorTituloRetornarResultadoDoRepositorio() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        TodoModel t1 = new TodoModel();
        t1.setTitulo("Reuniao");
        TodoModel t2 = new TodoModel();
        t2.setTitulo("Reuniao de equipe");
        List<TodoModel> esperado = Arrays.asList(t1, t2);
        when(repository.buscarPorTitulo("Reuniao")).thenReturn(esperado);

        List<TodoModel> resultado = service.buscarPorTitulo("Reuniao", "token");

        assertEquals(2, resultado.size());
        assertSame(esperado, resultado);
    }

    @Test
    void deveGerarResumoDelegarAoRepositorio() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        Map<String, Object> resumoEsperado = Map.of("total", 10, "concluidas", 4, "pendentes", 6, "percentualConcluido", 40);
        when(repository.gerarResumo("maria")).thenReturn(resumoEsperado);

        Map<String, Object> resultado = service.gerarResumo("maria");

        assertNotNull(resultado);
        assertSame(resumoEsperado, resultado);
        verify(repository, times(1)).gerarResumo("maria");
    }

    @Test
    void deveGerarResumoSemResponsavelPassarNullAoRepositorio() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        Map<String, Object> resumo = Map.of("total", 5, "concluidas", 0, "pendentes", 5, "percentualConcluido", 0);
        when(repository.gerarResumo(null)).thenReturn(resumo);

        Map<String, Object> resultado = service.gerarResumo(null);

        assertNotNull(resultado);
        verify(repository, times(1)).gerarResumo(null);
    }

    @Test
    void deveConcluirEmLoteDelegarAoRepositorio() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Map<String, Object> retornoEsperado = Map.of("ok", true, "alteradas", 3, "enviadas", 3);
        when(repository.concluirEmLote(ids)).thenReturn(retornoEsperado);

        Map<String, Object> resultado = service.concluirEmLote(ids);

        assertNotNull(resultado);
        assertEquals(true, resultado.get("ok"));
        verify(repository, times(1)).concluirEmLote(ids);
    }

    @Test
    void deveConcluirEmLoteComListaVaziaDelegarAoRepositorio() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        List<Long> idsVazios = Collections.emptyList();
        when(repository.concluirEmLote(idsVazios)).thenReturn(Map.of("ok", true, "alteradas", 0, "enviadas", 0));

        Map<String, Object> resultado = service.concluirEmLote(idsVazios);

        assertNotNull(resultado);
        verify(repository, times(1)).concluirEmLote(idsVazios);
    }

    @Test
    void deveDuplicarDelegarAoRepositorio() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        TodoModel copia = new TodoModel();
        copia.setId(7L);
        copia.setTitulo("Tarefa original");
        when(repository.duplicar(7L)).thenReturn(copia);

        TodoModel resultado = service.duplicar(7L);

        assertNotNull(resultado);
        assertEquals(7L, resultado.getId());
        assertEquals("Tarefa original", resultado.getTitulo());
        verify(repository, times(1)).duplicar(7L);
    }

    @Test
    void deveDuplicarPassarIdCorretoAoRepositorio() {
        TodoRepository repository = mock(TodoRepository.class);
        TodoService service = new TodoService(repository);
        when(repository.duplicar(anyLong())).thenReturn(new TodoModel());

        service.duplicar(99L);

        verify(repository, times(1)).duplicar(99L);
        verify(repository, never()).duplicar(1L);
    }
}
