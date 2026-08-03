-- Données de démonstration CEPE
-- Prérequis : tables ecole, eleve, matiere, note déjà créées
-- Format année scolaire : YYYY-YYYY (ex. 2022-2023)

INSERT INTO ecole (numecole, design, adresse) VALUES
    ('ECO001', 'Saint Joseph Ouvrier', 'Antananarivo'),
    ('ECO002', 'École Primaire Analakely', 'Antananarivo')
ON CONFLICT (numecole) DO NOTHING;

INSERT INTO eleve (numeleve, numecole, nom, prenom, date_naissance) VALUES
    ('ELV001', 'ECO001', 'RAKOTO', 'Bernard', DATE '2013-03-23'),
    ('ELV002', 'ECO002', 'RASOA',  'Marie',   DATE '2013-08-11')
ON CONFLICT (numeleve) DO NOTHING;

INSERT INTO matiere (nummat, designmat, coef) VALUES
    ('MAT001', 'Malagasy',   3),
    ('MAT002', 'Calcul',     1),
    ('MAT003', 'Problème',   2),
    ('MAT004', 'Tantara',    1),
    ('MAT005', 'Géographie', 1),
    ('MAT006', 'Français',   1),
    ('MAT007', 'SVT',        2)
ON CONFLICT (nummat) DO NOTHING;

INSERT INTO note (annee_scolaire, numeleve, nummat, note) VALUES
    ('2022-2023', 'ELV001', 'MAT001', 12),
    ('2022-2023', 'ELV001', 'MAT002', 18),
    ('2022-2023', 'ELV001', 'MAT003', 19),
    ('2022-2023', 'ELV001', 'MAT004', 11),
    ('2022-2023', 'ELV001', 'MAT005', 14),
    ('2022-2023', 'ELV001', 'MAT006', 15),
    ('2022-2023', 'ELV001', 'MAT007',  9),
    ('2022-2023', 'ELV002', 'MAT001',  8),
    ('2022-2023', 'ELV002', 'MAT002', 10),
    ('2022-2023', 'ELV002', 'MAT003',  7),
    ('2022-2023', 'ELV002', 'MAT004',  9),
    ('2022-2023', 'ELV002', 'MAT005',  8),
    ('2022-2023', 'ELV002', 'MAT006', 11),
    ('2022-2023', 'ELV002', 'MAT007',  7)
ON CONFLICT DO NOTHING;
