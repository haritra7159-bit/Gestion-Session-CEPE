CREATE TABLE ecole (
    numEcole VARCHAR(20) PRIMARY KEY,
    design VARCHAR(150) NOT NULL UNIQUE,
    adresse VARCHAR(255) NOT NULL
);

CREATE TABLE eleve (
    numEleve VARCHAR(20) PRIMARY KEY,
    numEcole VARCHAR(20) NOT NULL REFERENCES ecole (numEcole) ON UPDATE CASCADE ON DELETE RESTRICT,
    nom VARCHAR(100) NOT NULL CHECK (btrim(nom) <> ''),
    prenom VARCHAR(150) NOT NULL CHECK (btrim(prenom) <> ''),
    date_naissance DATE NOT NULL CHECK (date_naissance <= CURRENT_DATE)
);

CREATE TABLE matiere (
    numMat VARCHAR(20) PRIMARY KEY,
    designMat VARCHAR(100) NOT NULL UNIQUE,
    coef SMALLINT NOT NULL CHECK (coef BETWEEN 1 AND 10)
);

CREATE TABLE note (
    annee_scolaire VARCHAR(9) NOT NULL CHECK (annee_scolaire ~ '^[0-9]{4}-[0-9]{4}$'),
    numEleve VARCHAR(20) NOT NULL REFERENCES eleve (numEleve) ON UPDATE CASCADE ON DELETE RESTRICT,
    numMat VARCHAR(20) NOT NULL REFERENCES matiere (numMat) ON UPDATE CASCADE ON DELETE RESTRICT,
    note NUMERIC(4, 2) NOT NULL CHECK (note BETWEEN 0 AND 20),
    CONSTRAINT pk_note PRIMARY KEY (annee_scolaire, numEleve, numMat)
);

CREATE INDEX idx_eleve_nom_prenom ON eleve (nom, prenom);
CREATE INDEX idx_eleve_ecole ON eleve (numEcole);
CREATE INDEX idx_note_eleve_annee ON note (numEleve, annee_scolaire);
