package gerenciadordetarefas.personal_tasks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "personal-tasks")

public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;


    @Column(nullable = false , updatable = false)
    private LocalDateTime dataInicio;

    @Column(nullable = false)
    private LocalDateTime dataFim;

}
