# Notes Atelier 4 : couche Service, IoC et DI

## A. Modes d'injection comparés

| Critère | Constructeur | Attribut (@Autowired) |
|---|---|---|
| Champ final possible | Oui | Non |
| Dépendance visible dans la signature de la classe | Oui | Non (dépendance cachée) |
| Utilisable avec new hors conteneur sans dépendance null | Oui : le repository est obligatoire pour construire l'objet | Non : le champ reste null (NullPointerException) |
| Signalé par SonarQube for IDE | Non | Oui : règle S6813 (injection par attribut à éviter) |

## B. Services

| Service | Dépendances injectées | Mode | Justification |
|---|---|---|---|
| AgenceServiceImpl | IAgenceRepository | Constructeur | Champ final, dépendance obligatoire typée par interface. |
| EmployeServiceImpl | IEmployeRepository | Constructeur | Champ final, dépendance obligatoire typée par interface. |
| VehiculeServiceImpl | IVehiculeRepository | Constructeur | Champ final, dépendance obligatoire typée par interface. |
| EquipementServiceImpl | IEquipementRepository | Constructeur | Champ final, dépendance obligatoire typée par interface. |
| ClientServiceImpl | IClientRepository | Constructeur | Champ final, dépendance obligatoire typée par interface. |
| ReservationServiceImpl | IReservationRepository | Constructeur | Champ final, dépendance obligatoire typée par interface. |
| ContratServiceImpl | IContratRepository | Constructeur | Champ final, dépendance obligatoire typée par interface ; la suppression d'un contrat supprime ses paiements par cascade. |
| PaiementServiceImpl | IPaiementRepository | Constructeur | Lecture seule : les paiements se créent et se retirent uniquement à travers le Contrat (composition). |
| MaintenanceServiceImpl | IMaintenanceRepository | Constructeur | Champ final, dépendance obligatoire typée par interface. |

## C. Messages d'erreur du conteneur

**Message 1 : NoSuchBeanDefinitionException.** Le bean concerné est ContratController, qui demande un bean de type IContratService. Les deux causes les plus probables côté ContratServiceImpl : l'annotation @Service est oubliée, ou la classe est placée en dehors du package tn.esprit.autoloc, donc jamais détectée par le scan des composants. Correction : ajouter @Service, ou déplacer la classe dans un sous-package de tn.esprit.autoloc.

**Message 2 : NoUniqueBeanDefinitionException.** Deux beans du même type existent (emailNotificateur et smsNotificateur) et NotificationController ne sait pas lequel choisir. Correction 1 : placer @Primary sur la classe du bean à utiliser par défaut. Correction 2 : placer @Qualifier("emailNotificateur") sur le paramètre du constructeur de NotificationController, pour choisir explicitement.

**Message 3 : dépendance circulaire** entre clientServiceImpl et reservationServiceImpl. Avec l'injection par constructeur, chaque bean a besoin de l'autre déjà construit pour pouvoir être créé : aucun des deux ne peut exister en premier, le cycle ne peut donc pas être contourné. Piste de conception : extraire la responsabilité commune dans un troisième composant dont les deux services dépendent, ou faire dépendre l'un des deux du repository de l'autre plutôt que de son service.

## D. Anomalies SonarQube for IDE

| Anomalie | Règle / explication | Correction apportée |
|---|---|---|
| Injection par attribut (@Autowired sur le champ) dans ContratServiceImpl | S6813 : l'injection par attribut cache les dépendances, empêche le champ d'être final et laisse l'objet avec une dépendance null hors du conteneur. | Retour à l'injection par constructeur avec @RequiredArgsConstructor et un champ final. |
| RuntimeException levée dans ContratServiceImpl.findById | S112 : lever une exception générique empêche l'appelant de distinguer une ressource absente d'une autre erreur ; il faut une exception métier explicite. | Retour à ResourceNotFoundException, qui porte le nom de la ressource et l'identifiant. |
| Import inutilisé ajouté volontairement pour l'expérience | S1128 : un import non utilisé alourdit le code sans raison et peut masquer des dépendances réelles. | Import supprimé. |

## E. Questions de compréhension

1. **Où est le new de ContratServiceImpl et qui l'exécute ?** Il n'est écrit nulle part dans le code : le conteneur Spring l'exécute au démarrage, après avoir détecté @Service par le scan des composants, et fournit au constructeur le proxy de IContratRepository généré par Spring Data.

2. **Pourquoi un contrôleur dépendra-t-il de IContratService et non de ContratServiceImpl ?** Pour dépendre d'une abstraction : on peut changer d'implémentation, ou fournir un faux service pour les tests, sans modifier le contrôleur. Cela réduit le couplage entre les couches.

3. **Pourquoi un service singleton doit-il rester sans état ?** Une seule instance est partagée par tous les threads. Un attribut modifiable comme « contratCourant » serait écrasé par une requête concurrente : un utilisateur pourrait alors lire ou modifier le contrat d'un autre.

4. **Pourquoi update charge-t-il l'entité existante avant d'appeler save ?** Passer directement l'objet reçu à save déclencherait un merge qui écrase toutes les colonnes (les champs non envoyés deviendraient null) et, avec orphanRemoval, une collection de paiements vide pourrait supprimer les paiements existants. Le chargement permet aussi de vérifier que l'entité existe.

5. **Différences entre les annotations, et IPaiementService.** @Component se pose sur une classe que l'on écrit, détectée par le scan ; @Bean se pose sur une méthode d'une classe @Configuration, pour une classe que l'on ne peut pas annoter (bibliothèque) ou une construction particulière. @Primary se pose sur la classe et désigne le candidat par défaut ; @Qualifier se pose au point d'injection et choisit un bean par son nom. IPaiementService n'expose ni création, ni modification, ni suppression car Paiement est l'enfant d'une composition : il se crée et se retire uniquement à travers son Contrat (cascade et orphanRemoval).
