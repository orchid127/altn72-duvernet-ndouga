# Journal — Kassandra (ALTN72, 2026-27)
**Groupe :** Emma DUVERNET, Kimberley NDOUGA

## README
Kassandra est une plateforme qui permet de gérer les équipements et les éventuels incidents qui peuvent s'y produire.
Lien repo git : https://github.com/orchid127/altn72-duvernet-ndouga
Lien du déploiement : https://altn72-duvernet-ndouga.onrender.com/
login : 1
mot de passe : 1234

## ARCHITECTURE
L'application suit une architecture classique Vue > API Service > Persistance > Base de données.
- Vue : Thymeleaf
- API et Service : SpringBoot
- Persistance : Hibernate
- Base de données : MySQL

## TESTS

## CHOIX TECHNIQUES

## LOT ALPHA

## BILAN
- Fonctionnalités non réalisées et pourquoi : le dashboard et la notation des incidents manque à l'application, manque de temps :/ Les endpoints nécessaires ont été créé mais l'interface graphique est en cours de construction

## TACHES A FAIRE
- [x] Gérer exceptions
- [x] Faire classes de tests
- [x] Résoudre problème synchro git
- [x] Corriger application.properties
- [X] Ajouter variables environnements
- [x] ajouter 2e fichier application.properties

## Évolution séance finale

### 1. Description de l'évolution réalisée

Un prestataire est **sous surveillance** lorsque la moyenne de ses 5 dernières évaluations est strictement inférieure à 3,0. L'affecter à un incident exige alors une confirmation explicite.

Fichiers principaux :

- `service/SurveillanceService` : le calcul de la moyenne des 5 dernières évaluations (PS1, PS2) et du statut (PS3, PS4). Le seuil (3,0) et la taille de la fenêtre (5) y sont définis en un seul endroit, sous forme de constantes (PS6).
- `repository/EvaluationRepository` : la requête dérivée `findByIdIncident_IdPrestataireOrderByDateEvaluationDescIdDesc`, triée par date d'évaluation décroissante puis par identifiant décroissant (PS1). Le nombre de résultats est passé par un paramètre `Limit`, pour que le 5 ne soit écrit qu'une fois.
- `service/IncidentService` : `affecterPrestataireIncident` reçoit un paramètre `confirmation`. Sans confirmation, un prestataire surveillé déclenche `ConfirmationRequiseException` **avant** tout enregistrement (PS5, RG10).
- `service/PrestataireService` : la liste de choix du formulaire, dont le libellé contient « (sous surveillance) » pour les prestataires concernés.
- `controleur/AffectationController`, `formulaire/AffectationForm`, `templates/affecter-incident.html` : le formulaire d'affectation et sa confirmation.
- `controleur/IncidentController`, `templates/incident-detail.html` : la mention « sous surveillance » sur le détail d'un incident affecté.
- `controleur/GestionnaireErreursApi` : par l'API, une affectation non confirmée répond 409.

### 2. Choix de conception

- **La confirmation prend la forme d'une case à cocher.** À la première soumission pour un prestataire surveillé, la page se réaffiche avec un message contenant « surveillance » et une case « Je confirme l'affectation… ». Après avoir coché et renvoyé, l'affectation est enregistrée. Par l'API, la confirmation se donne avec le paramètre `?confirmation=true`.
- **La règle vit dans les services (PS7).** Le contrôleur et la page ne font qu'afficher ce que les services calculent, y compris le libellé de la liste de choix.
- **Le statut n'est jamais enregistré en base (PS4).** Il est recalculé à chaque affichage et à chaque affectation, si bien que la fenêtre glisse automatiquement.
- **`SurveillanceService` est un service à part.** `EvaluationService` dépend déjà d'`IncidentService` : y placer la règle aurait créé une dépendance circulaire entre les deux.
- **Raccourci pris :** l'affectation d'un incident déjà résolu reste ignorée silencieusement, comme dans la version d'origine, au lieu d'être refusée par une exception (RG5).

### 3. Tests ajoutés et limites connues

Tests ajoutés :

- `SurveillanceServiceTest` : les 7 exemples de référence du sujet, chacun dans un test ; le départage par identifiant quand deux évaluations ont la même date (PS1) ; un incident sans prestataire.
- `AffectationTest` :
    - la mention « (sous surveillance) » dans la liste de choix ;
    - le refus sans confirmation, avec le message, sans rien enregistrer ;
    - l'enregistrement avec confirmation ;
    - l'affectation d'un prestataire non surveillé sans confirmation ;
    - le refus pour un agent (403).

Limites connues :

- Le statut est recalculé pour chaque prestataire à chaque affichage de la liste de choix : une requête par prestataire, ce qui deviendrait lent avec beaucoup de prestataires.
- Par l'API, le prestataire est reçu dans le corps de la requête : si son nom n'est pas fourni, le message d'erreur affiche « null » à la place du nom.
- L'unicité d'une évaluation par incident est vérifiée par le service, pas par une contrainte en base.
