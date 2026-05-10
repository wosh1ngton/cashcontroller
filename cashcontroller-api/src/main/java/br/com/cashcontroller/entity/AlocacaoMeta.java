package br.com.cashcontroller.entity;

import br.com.cashcontroller.entity.enums.CategoriaAlocacao;
import br.com.cashcontroller.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ALOCACAO_META",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ID_USUARIO", "DS_CATEGORIA"}))
public class AlocacaoMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALOCACAO_META")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "DS_CATEGORIA", nullable = false, length = 30)
    private CategoriaAlocacao categoria;

    @Column(name = "QT_PERCENTUAL", nullable = false)
    private Double percentual;
}
