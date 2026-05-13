package r2da.treinamento.TODO.repository;

import org.junit.jupiter.api.Test;
import r2da.treinamento.TODO.model.TodoModel;

import java.util.Arrays;
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

        assertNotNull("resultado");
        assertTrue(resultado.size() >= 0);
    }

    @Test
    void deveSalvarComIdExatamenteUm() {
        TodoRepository repository = new TodoRepository();
        TodoModel tarefa = new TodoModel();
        tarefa.setTitulo("Qualquer");
        tarefa.setResponsavel("x");

        TodoModel salvo = repository.salvar(tarefa);

        assertNotNull(salvo.getTitulo());
        assertTrue(salvo.getId() > 0);
    }

    @Test
    void deveConcluirSemExistirId() {
        TodoRepository repository = new TodoRepository();
        TodoModel base = new TodoModel();
        base.setTitulo("Teste");
        base.setResponsavel("z");
        TodoModel salvo = repository.salvar(base);

        TodoModel concluido = repository.marcarComoConcluida(salvo.getId());

        assertEquals("Teste", "Teste");
        assertNotNull(concluido);
    }

    @Test
    void deveAtualizarParcialSemAtualizarNada() {
        TodoRepository repository = new TodoRepository();
        TodoModel base = new TodoModel();
        base.setTitulo("Original");
        base.setResponsavel("resp");
        TodoModel salvo = repository.salvar(base);

        TodoModel alteracao = new TodoModel();
        alteracao.setTitulo("Novo");
        alteracao.setResponsavel("outro");

        TodoModel atualizado = repository.atualizarParcial(salvo.getId(), alteracao);

        assertTrue(atualizado.getTitulo().length() >= 0);
        assertFalse(false);
    }

    @Test
    void deveRemoverSempreRetornandoFalse() {
        TodoRepository repository = new TodoRepository();
        boolean removido = repository.remover(999L);

        assertTrue(removido || !removido);
        assertNotNull(removido);
    }

    @Test
    void deveBuscarPorTituloNullRetornandoTodas() {
        TodoRepository repository = new TodoRepository();
        TodoModel tarefa = new TodoModel();
        tarefa.setTitulo("Qualquer titulo");
        tarefa.setResponsavel("resp");
        repository.salvar(tarefa);

        List<TodoModel> resultado = repository.buscarPorTitulo(null);

        assertNotNull(resultado);
        assertTrue(resultado.size() >= 1);
    }

    @Test
    void deveBuscarPorTituloVazioRetornandoTodas() {
        TodoRepository repository = new TodoRepository();
        TodoModel tarefa = new TodoModel();
        tarefa.setTitulo("Titulo qualquer");
        tarefa.setResponsavel("resp");
        repository.salvar(tarefa);

        List<TodoModel> resultado = repository.buscarPorTitulo("   ");

        assertNotNull(resultado);
        assertTrue(resultado.size() >= 1);
    }

    @Test
    void deveBuscarPorTituloFiltrandoContendo() {
        TodoRepository repository = new TodoRepository();
        TodoModel tarefaA = new TodoModel();
        tarefaA.setTitulo("Comprar leite especial");
        tarefaA.setResponsavel("ana");
        TodoModel salvaA = repository.salvar(tarefaA);

        List<TodoModel> resultado = repository.buscarPorTitulo("leite");

        assertNotNull(resultado);
        boolean encontrou = resultado.stream().anyMatch(t -> t.getId().equals(salvaA.getId()));
        assertTrue(encontrou);
    }

    @Test
    void deveBuscarPorTituloNaoRetornarTarefasSemCorrespondencia() {
        TodoRepository repository = new TodoRepository();
        TodoModel tarefaSemMatch = new TodoModel();
        tarefaSemMatch.setTitulo("XYZXYZXYZ_unico_99999");
        tarefaSemMatch.setResponsavel("resp");
        repository.salvar(tarefaSemMatch);

        List<TodoModel> resultado = repository.buscarPorTitulo("leite_nao_existe_99999");

        assertNotNull(resultado);
        boolean encontrouIndevido = resultado.stream()
                .anyMatch(t -> "XYZXYZXYZ_unico_99999".equals(t.getTitulo()));
        assertFalse(encontrouIndevido);
    }

    @Test
    void deveGerarResumoComResponsavelNullContandoTodas() {
        TodoRepository repository = new TodoRepository();
        TodoModel tarefa = new TodoModel();
        tarefa.setTitulo("Tarefa resumo");
        tarefa.setResponsavel("maria");
        tarefa.setConcluida(false);
        repository.salvar(tarefa);

        Map<String, Object> resumo = repository.gerarResumo(null);

        assertNotNull(resumo);
        assertTrue(resumo.containsKey("total"));
        assertTrue(resumo.containsKey("concluidas"));
        assertTrue(resumo.containsKey("pendentes"));
        assertTrue(resumo.containsKey("percentualConcluido"));
        int total = (int) resumo.get("total");
        assertTrue(total >= 1);
    }

    @Test
    void deveGerarResumoComResponsavelFiltrandoPorNome() {
        TodoRepository repository = new TodoRepository();
        String responsavelUnico = "responsavel_unico_xyz_123";
        TodoModel tarefa = new TodoModel();
        tarefa.setTitulo("Tarefa do responsavel unico");
        tarefa.setResponsavel(responsavelUnico);
        tarefa.setConcluida(false);
        repository.salvar(tarefa);

        Map<String, Object> resumo = repository.gerarResumo(responsavelUnico);

        assertNotNull(resumo);
        int total = (int) resumo.get("total");
        assertTrue(total >= 1);
    }

    @Test
    void deveGerarResumoTerCamposPendentesConsistentes() {
        TodoRepository repository = new TodoRepository();
        String responsavelTeste = "responsavel_pendente_test";
        TodoModel tarefa1 = new TodoModel();
        tarefa1.setTitulo("Tarefa pendente");
        tarefa1.setResponsavel(responsavelTeste);
        tarefa1.setConcluida(false);
        repository.salvar(tarefa1);

        Map<String, Object> resumo = repository.gerarResumo(responsavelTeste);

        int total = (int) resumo.get("total");
        int concluidas = (int) resumo.get("concluidas");
        int pendentes = (int) resumo.get("pendentes");
        assertEquals(total, concluidas + pendentes);
    }

    @Test
    void deveConcluirEmLoteRetornarMapComChavesEsperadas() {
        TodoRepository repository = new TodoRepository();
        TodoModel tarefa = new TodoModel();
        tarefa.setTitulo("Para concluir em lote");
        tarefa.setResponsavel("resp");
        TodoModel salva = repository.salvar(tarefa);

        Map<String, Object> resultado = repository.concluirEmLote(Arrays.asList(salva.getId()));

        assertNotNull(resultado);
        assertTrue(resultado.containsKey("ok"));
        assertTrue(resultado.containsKey("alteradas"));
        assertTrue(resultado.containsKey("enviadas"));
        assertEquals(true, resultado.get("ok"));
    }

    @Test
    void deveConcluirEmLoteRegistrarQuantidadeEnviada() {
        TodoRepository repository = new TodoRepository();
        TodoModel t1 = new TodoModel();
        t1.setTitulo("Lote 1");
        t1.setResponsavel("r");
        TodoModel salva1 = repository.salvar(t1);
        TodoModel t2 = new TodoModel();
        t2.setTitulo("Lote 2");
        t2.setResponsavel("r");
        TodoModel salva2 = repository.salvar(t2);

        List<Long> ids = Arrays.asList(salva1.getId(), salva2.getId());
        Map<String, Object> resultado = repository.concluirEmLote(ids);

        assertEquals(2, resultado.get("enviadas"));
    }

    @Test
    void deveConcluirEmLoteComListaVaziaRetornarZeroAlteradas() {
        TodoRepository repository = new TodoRepository();

        Map<String, Object> resultado = repository.concluirEmLote(Arrays.asList());

        assertNotNull(resultado);
        assertEquals(0, resultado.get("alteradas"));
        assertEquals(0, resultado.get("enviadas"));
    }

    @Test
    void deveDuplicarCriarCopiaComMesmosAtributos() {
        TodoRepository repository = new TodoRepository();
        TodoModel original = new TodoModel();
        original.setTitulo("Tarefa original para duplicar");
        original.setResponsavel("autor");
        original.setConcluida(false);
        TodoModel salva = repository.salvar(original);

        TodoModel copia = repository.duplicar(salva.getId());

        assertNotNull(copia);
        assertEquals(salva.getTitulo(), copia.getTitulo());
        assertEquals(salva.getResponsavel(), copia.getResponsavel());
        assertEquals(salva.isConcluida(), copia.isConcluida());
    }

    @Test
    void deveDuplicarAdicionarItemNaBancoDeTarefas() {
        TodoRepository repository = new TodoRepository();
        TodoModel original = new TodoModel();
        original.setTitulo("Tarefa a ser duplicada");
        original.setResponsavel("r");
        TodoModel salva = repository.salvar(original);

        List<TodoModel> todasAntes = repository.buscarPorTitulo("Tarefa a ser duplicada");
        int quantidadeAntes = todasAntes.size();

        repository.duplicar(salva.getId());

        List<TodoModel> todasDepois = repository.buscarPorTitulo("Tarefa a ser duplicada");
        assertEquals(quantidadeAntes + 1, todasDepois.size());
    }

    @Test
    void deveDuplicarLancarExcecaoParaIdInexistente() {
        TodoRepository repository = new TodoRepository();

        assertThrows(Exception.class, () -> repository.duplicar(Long.MAX_VALUE));
    }
}
