CREATE TABLE Station (
                         id_station INT AUTO_INCREMENT PRIMARY KEY,
                         nom_station VARCHAR(100) NOT NULL
);

CREATE TABLE Equipement (
                            id_equipement INT AUTO_INCREMENT PRIMARY KEY,
                            reference VARCHAR(100) NOT NULL,
                            description VARCHAR(250) NOT NULL,
                            type ENUM('Escalator', 'Ascenseur', 'Portique', 'Distributeur ticket') NOT NULL,
                            id_station INT NOT NULL,
                            etat_fontionnement ENUM ('En marche', 'En panne', 'En cours de réparation') NOT NULL,
                            UNIQUE (reference),
                            FOREIGN KEY (id_station) REFERENCES Station(id_station)
);

CREATE TABLE Prestataire (
                             id_prestataire INT AUTO_INCREMENT PRIMARY KEY,
                             nom VARCHAR(100) NOT NULL,
                             mail VARCHAR(100) NOT NULL
);

CREATE TABLE Incident (
                          id_incident INT AUTO_INCREMENT PRIMARY KEY,
                          id_equipement INT NOT NULL,
                          description VARCHAR(250) NOT NULL,
                          date_ouverture DATETIME NOT NULL,
                          statut ENUM('Non affecté', 'Affecté à un prestataire','Résolu') NOT NULL,
                          id_prestataire INT,
                          date_resolution DATETIME,
                          FOREIGN KEY (id_equipement) REFERENCES Equipement(id_equipement),
                          FOREIGN KEY (id_prestataire) REFERENCES Prestataire(id_prestataire)
);

CREATE TABLE Evaluation (
                            id_evaluation INT AUTO_INCREMENT PRIMARY KEY,
                            id_incident INT NOT NULL,
                            note INT NOT NULL,
                            commentaire VARCHAR(100),
                            date_evaluation DATE NOT NULL,
                            FOREIGN KEY (id_incident) REFERENCES Incident(id_incident)
);

CREATE TABLE Utilisateur (
                             id_connexion INT PRIMARY KEY,
                             mot_de_passe VARCHAR(100) NOT NULL,
                             prenom VARCHAR(100) NOT NULL,
                             profil VARCHAR(100)
);