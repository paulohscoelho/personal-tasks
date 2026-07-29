package gerenciadordetarefas.personal_tasks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "personal-tasks")
public class Gerenciador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDENTE;

    @CreatedDate
    private LocalDateTime dataInicio;


    private LocalDateTime dataFim;


}
