package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity

public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 50)
    private String ville;

    @Column(nullable = false, length = 150)
    private String adresse;

    @Column(length = 20)
    private String telephone;

    @OneToMany(mappedBy = "agence")
    List<Employe> employes = new ArrayList<>();

    @OneToMany(mappedBy = "agence")
    List<Vehicule> vehicules = new ArrayList<>();
}