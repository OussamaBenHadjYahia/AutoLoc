# Notes Atelier 3 : couche Repository

## Choix des interfaces

| Interface | Étend | Justification |
|---|---|---|
| IContratRepository | JpaRepository<Contrat, Long> | CRUD complet, findAll renvoie une List, saveAndFlush disponible. Attention : deleteAllInBatch contourne la cascade et l'orphanRemoval. |
| IPaiementRepository | JpaRepository<Paiement, Long> | Lecture des paiements ; leur création et leur retrait passent par le Contrat (composition). |
| IAgenceRepository | JpaRepository<Agence, Long> | CRUD complet, findAll renvoie une List, tri et pagination, flush. |
| IEmployeRepository | JpaRepository<Employe, Long> | CRUD complet, findAll renvoie une List, tri et pagination, flush. |
| IVehiculeRepository | JpaRepository<Vehicule, Long> | CRUD complet, findAll renvoie une List, tri et pagination, flush. |
| IEquipementRepository | JpaRepository<Equipement, Long> | CRUD complet, findAll renvoie une List, tri et pagination, flush. |
| IClientRepository | JpaRepository<Client, Long> | CRUD complet, findAll renvoie une List, tri et pagination, flush. |
| IReservationRepository | JpaRepository<Reservation, Long> | CRUD complet, findAll renvoie une List, tri et pagination, flush. |
| IMaintenanceRepository | JpaRepository<Maintenance, Long> | CRUD complet, findAll renvoie une List, tri et pagination, flush. |

## Anomalies SonarQube for IDE

| Anomalie | Règle / explication | Correction apportée |
|---|---|---|
| à compléter | à compléter | à compléter |
| à compléter | à compléter | à compléter |
| à compléter | à compléter | à compléter |