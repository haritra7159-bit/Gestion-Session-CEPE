# Changelog

## [1.0.0] - 2026-08-05

### Added
- Codes format TYPE-XXXX (ECO/ELV/MAT) avec ComboBox éditables et auto-next
- ComboBox années scolaires (délibération, classement, notes)
- Filtrage délibération/classement : uniquement élèves ayant des notes pour l'année
- Scripts SQL reset + données démo (TYPE-XXXX)
- CSS modularisé (base, sidebar, buttons, forms, tables, cards, dialogs)
- Controllers fractionnés ≤ 100 lignes (helpers ecole/, eleve/, matiere/, notes/)
- Utilitaire UiDialogs, CodeFormat

### Fixed
- Contrainte année scolaire CHECK ^[0-9]{4}-[0-9]{4}$
- Délibération sur tous les élèves (désormais filtrée par notes)

## [0.1.0] - 2026-07-30
### Added
- Fondation Maven + PostgreSQL + CRUD 4 tables + PDF
