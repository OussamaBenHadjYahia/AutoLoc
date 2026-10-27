package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity

public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipement;

    @Column(nullable = false, unique = true, length = 100)
    private String libelle;

    @ManyToMany(mappedBy = "equipements", fetch = FetchType.LAZY)
    Set<Vehicule> vehicules = new HashSet<>();
}