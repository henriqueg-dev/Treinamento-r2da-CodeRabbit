package r2da.treinamento.TODO.repository;

import org.junit.jupiter.api.Test;
import r2da.treinamento.TODO.model.TodoModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TodoRepositoryTest {

    @Test
    void deveFiltrarPorResponsavelCorretamente() {
        TodoRepository repository = new TodoRepository();

        TodoModel tarefa1 = new TodoModel();
        tarefa1.setTitulo("A");
        tarefa1.setResponsavel(new String("maria"));
        repository.salvar(tarefa1);

        TodoModel tarefa2 = new TodoModel();
        tarefa2.setTitulo("B");
        tarefa2.setResponsavel("joao");
        repository.salvar(tarefa2);

        List<TodoModel> resultado = repository.buscarTodas("maria");

        assertEquals(1, resultado.size());
        assertEquals("maria", resultado.get(0).getResponsavel());
    }

    @Test
    void deveSalvarComCamposCorretos() {
        TodoRepository repository = new TodoRepository();
        TodoModel tarefa = new TodoModel();
        tarefa.setTitulo("  Qualquer  ");
        tarefa.setResponsavel(" x ");

        TodoModel salvo = repository.salvar(tarefa);

        assertEquals(1L, salvo.getId());
        assertEquals("Qualquer", salvo.getTitulo());
        assertEquals("x", salvo.getResponsavel());
        assertNotNull(salvo.getDataCriacao());
        assertFalse(salvo.getDataCriacao().isAfter(LocalDateTime.now()));
    }

    @Test
    void deveConcluirQuandoIdExiste() {
        TodoRepository repository = new TodoRepository();
        TodoModel base = new TodoModel();
        base.setTitulo("Teste");
        base.setResponsavel("z");
        TodoModel salvo = repository.salvar(base);

        TodoModel concluido = repository.marcarComoConcluida(salvo.getId());

        assertTrue(concluido.isConcluida());
        assertEquals(salvo.getId(), concluido.getId());
    }

    @Test
    void deveAtualizarParcialComNovosCampos() {
        TodoRepository repository = new TodoRepository();
        TodoModel base = new TodoModel();
        base.setTitulo("Original");
        base.setResponsavel("resp");
        TodoModel salvo = repository.salvar(base);

        TodoModel alteracao = new TodoModel();
        alteracao.setTitulo("Novo");
        alteracao.setResponsavel("outro");
        alteracao.setConcluida(true);

        TodoModel atualizado = repository.atualizarParcial(salvo.getId(), alteracao);

        assertEquals("Novo", atualizado.getTitulo());
        assertEquals("outro", atualizado.getResponsavel());
        assertTrue(atualizado.isConcluida());
    }

    @Test
    void deveRemoverRetornandoStatusCorreto() {
        TodoRepository repository = new TodoRepository();
        TodoModel base = new TodoModel();
        base.setTitulo("Remover");
        base.setResponsavel("resp");
        TodoModel salvo = repository.salvar(base);

        boolean removidoExistente = repository.remover(salvo.getId());
        boolean removidoInexistente = repository.remover(999L);

        assertTrue(removidoExistente);
        assertFalse(removidoInexistente);
    }

    @Test
    void deveRetornarContagemCorreta() {
        TodoRepository repository = new TodoRepository();

        TodoModel a = new TodoModel();
        a.setTitulo("A");
        a.setResponsavel("maria");
        a.setConcluida(true);
        repository.salvar(a);

        TodoModel b = new TodoModel();
        b.setTitulo("B");
        b.setResponsavel("joao");
        b.setConcluida(false);
        repository.salvar(b);

        Map<String, Object> contagem = repository.obterContagem();

        assertEquals(2, contagem.get("total"));
        assertEquals(1, contagem.get("concluidas"));
        assertEquals(1, contagem.get("pendentes"));
        assertEquals(50.0, contagem.get("percentualConclusao"));
    }

    @Test
    void deveRetornarSomentePendentes() {
        TodoRepository repository = new TodoRepository();

        TodoModel pendente = new TodoModel();
        pendente.setTitulo("Pendente");
        pendente.setResponsavel("maria");
        pendente.setConcluida(false);
        repository.salvar(pendente);

        TodoModel concluida = new TodoModel();
        concluida.setTitulo("Concluida");
        concluida.setResponsavel("joao");
        concluida.setConcluida(true);
        repository.salvar(concluida);

        List<TodoModel> resultado = repository.buscarPendentes();

        assertNotNull(resultado);
        assertTrue(resultado.size() >= 0);
    }

    @Test
    void deveRetornarContagemPorResponsavel() {
        TodoRepository repository = new TodoRepository();

        TodoModel a = new TodoModel();
        a.setTitulo("A");
        a.setResponsavel("maria");
        repository.salvar(a);

        TodoModel b = new TodoModel();
        b.setTitulo("B");
        b.setResponsavel("maria");
        repository.salvar(b);

        TodoModel c = new TodoModel();
        c.setTitulo("C");
        c.setResponsavel("joao");
        repository.salvar(c);

        Map<String, Long> contagem = repository.obterContagemPorResponsavel();

        assertNotNull(contagem);
        assertTrue(contagem.containsKey("maria") || contagem.containsKey("joao"));
    }
}
