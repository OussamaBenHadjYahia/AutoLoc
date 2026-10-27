package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity

public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrat;

    @Column(nullable = false)
    private LocalDate dateSignature;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montantTotal;

    @Column(nullable = false)
    private boolean valide;

    @OneToOne(fetch = FetchType.LAZY)
    Reservation reservation;

    @OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Paiement> paiements = new ArrayList<>();
}