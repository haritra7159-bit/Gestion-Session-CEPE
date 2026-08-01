INSERT INTO ecole VALUES ('ECO001', 'Saint Joseph Ouvrier', 'Antananarivo'), ('ECO002', 'École Primaire Analakely', 'Antananarivo');
INSERT INTO eleve VALUES ('ELV001', 'ECO001', 'RAKOTO', 'Bernard', DATE '2013-03-23'), ('ELV002', 'ECO002', 'RASOA', 'Marie', DATE '2013-08-11');
INSERT INTO matiere VALUES ('MAT001', 'Malagasy', 3), ('MAT002', 'Calcul', 1), ('MAT003', 'Problème', 2), ('MAT004', 'Tantara', 1), ('MAT005', 'Géographie', 1), ('MAT006', 'Français', 1), ('MAT007', 'SVT', 2);
INSERT INTO note VALUES
    ('2022-2023', 'ELV001', 'MAT001', 12), ('2022-2023', 'ELV001', 'MAT002', 18), ('2022-2023', 'ELV001', 'MAT003', 19), ('2022-2023', 'ELV001', 'MAT004', 11), ('2022-2023', 'ELV001', 'MAT005', 14), ('2022-2023', 'ELV001', 'MAT006', 15), ('2022-2023', 'ELV001', 'MAT007', 9),
    ('2022-2023', 'ELV002', 'MAT001', 8), ('2022-2023', 'ELV002', 'MAT002', 10), ('2022-2023', 'ELV002', 'MAT003', 7), ('2022-2023', 'ELV002', 'MAT004', 9), ('2022-2023', 'ELV002', 'MAT005', 8), ('2022-2023', 'ELV002', 'MAT006', 11), ('2022-2023', 'ELV002', 'MAT007', 7);
