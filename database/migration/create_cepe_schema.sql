-- Schéma CEPE (identifiants minuscules PostgreSQL)
CREATE TABLE IF NOT EXISTS ecole (
    numecole VARCHAR(20) PRIMARY KEY,
    design VARCHAR(150) NOT NULL,
    adresse VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS eleve (
    numeleve VARCHAR(20) PRIMARY KEY,
    numecole VARCHAR(20) NOT NULL REFERENCES ecole (numecole) ON UPDATE CASCADE ON DELETE RESTRICT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(150) NOT NULL,
    date_naissance DATE NOT NULL CHECK (date_naissance <= CURRENT_DATE)
);
CREATE TABLE IF NOT EXISTS matiere (
    nummat VARCHAR(20) PRIMARY KEY,
    designmat VARCHAR(100) NOT NULL,
    coef SMALLINT NOT NULL CHECK (coef BETWEEN 1 AND 10)
);
CREATE TABLE IF NOT EXISTS note (
    annee_scolaire VARCHAR(9) NOT NULL CHECK (annee_scolaire ~ '^[0-9]{4}-[0-9]{4}$'),
    numeleve VARCHAR(20) NOT NULL REFERENCES eleve (numeleve) ON UPDATE CASCADE ON DELETE RESTRICT,
    nummat VARCHAR(20) NOT NULL REFERENCES matiere (nummat) ON UPDATE CASCADE ON DELETE RESTRICT,
    note NUMERIC(4,2) NOT NULL CHECK (note BETWEEN 0 AND 20),
    CONSTRAINT pk_note PRIMARY KEY (annee_scolaire, numeleve, nummat)
);
CREATE INDEX IF NOT EXISTS idx_eleve_nom_prenom ON eleve (nom, prenom);
CREATE INDEX IF NOT EXISTS idx_eleve_ecole ON eleve (numecole);
CREATE INDEX IF NOT EXISTS idx_note_eleve_annee ON note (numeleve, annee_scolaire);
