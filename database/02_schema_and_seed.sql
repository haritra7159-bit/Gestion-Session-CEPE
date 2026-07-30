BEGIN;
CREATE TABLE ecole (num_ecole VARCHAR(20) PRIMARY KEY, designation VARCHAR(150) NOT NULL UNIQUE, adresse VARCHAR(255) NOT NULL);
CREATE TABLE eleve (
  num_eleve VARCHAR(20) PRIMARY KEY, num_ecole VARCHAR(20) NOT NULL, nom VARCHAR(100) NOT NULL, prenom VARCHAR(150) NOT NULL, date_naissance DATE NOT NULL,
  CONSTRAINT fk_eleve_ecole FOREIGN KEY (num_ecole) REFERENCES ecole (num_ecole) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT chk_eleve_nom CHECK (btrim(nom) <> ''), CONSTRAINT chk_eleve_prenom CHECK (btrim(prenom) <> ''), CONSTRAINT chk_eleve_date CHECK (date_naissance <= CURRENT_DATE)
);
CREATE TABLE matiere (num_mat VARCHAR(20) PRIMARY KEY, designation VARCHAR(100) NOT NULL UNIQUE, coefficient SMALLINT NOT NULL, CONSTRAINT chk_coef CHECK (coefficient BETWEEN 1 AND 10));
CREATE TABLE note (
  annee_scolaire VARCHAR(9) NOT NULL, num_eleve VARCHAR(20) NOT NULL, num_mat VARCHAR(20) NOT NULL, note NUMERIC(4,2) NOT NULL,
  CONSTRAINT pk_note PRIMARY KEY (annee_scolaire,num_eleve,num_mat),
  CONSTRAINT fk_note_eleve FOREIGN KEY (num_eleve) REFERENCES eleve (num_eleve) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_note_matiere FOREIGN KEY (num_mat) REFERENCES matiere (num_mat) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT chk_annee CHECK (annee_scolaire ~ '^\\d{4}-\\d{4}$'), CONSTRAINT chk_note CHECK (note BETWEEN 0 AND 20)
);
CREATE INDEX idx_eleve_nom_prenom ON eleve (nom, prenom);
CREATE INDEX idx_eleve_ecole ON eleve (num_ecole);
CREATE INDEX idx_note_eleve_annee ON note (num_eleve, annee_scolaire);
INSERT INTO ecole VALUES ('ECO001','Saint Joseph Ouvrier','Antananarivo'),('ECO002','École Primaire Analakely','Antananarivo');
INSERT INTO eleve VALUES ('ELV001','ECO001','RAKOTO','Bernard',DATE '2013-03-23'),('ELV002','ECO002','RASOA','Marie',DATE '2013-08-11');
INSERT INTO matiere VALUES ('MAT001','Malagasy',3),('MAT002','Calcul',1),('MAT003','Problème',2),('MAT004','Tantara',1),('MAT005','Géographie',1),('MAT006','Français',1),('MAT007','SVT',2);
INSERT INTO note VALUES
 ('2022-2023','ELV001','MAT001',12),('2022-2023','ELV001','MAT002',18),('2022-2023','ELV001','MAT003',19),('2022-2023','ELV001','MAT004',11),('2022-2023','ELV001','MAT005',14),('2022-2023','ELV001','MAT006',15),('2022-2023','ELV001','MAT007',9),
 ('2022-2023','ELV002','MAT001',8),('2022-2023','ELV002','MAT002',10),('2022-2023','ELV002','MAT003',7),('2022-2023','ELV002','MAT004',9),('2022-2023','ELV002','MAT005',8),('2022-2023','ELV002','MAT006',11),('2022-2023','ELV002','MAT007',7);
COMMIT;
