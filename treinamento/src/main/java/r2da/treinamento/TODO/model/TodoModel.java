package r2da.treinamento.TODO.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoModel {
    private Long id;
    private String titulo;
    private boolean concluida;
    private String responsavel;
    private LocalDateTime dataCriacao;
}
