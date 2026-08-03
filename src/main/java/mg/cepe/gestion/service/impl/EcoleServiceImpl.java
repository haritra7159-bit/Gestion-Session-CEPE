package mg.cepe.gestion.service.impl;

import mg.cepe.gestion.dao.EcoleDao;
import mg.cepe.gestion.dao.impl.EcoleDaoJdbc;
import mg.cepe.gestion.entity.Ecole;
import mg.cepe.gestion.exception.BusinessException;
import mg.cepe.gestion.service.EcoleService;

import java.util.List;
import java.util.Optional;

public final class EcoleServiceImpl implements EcoleService {

    private final EcoleDao ecoleDao;

    public EcoleServiceImpl() {
        this(new EcoleDaoJdbc());
    }

    public EcoleServiceImpl(EcoleDao ecoleDao) {
        this.ecoleDao = ecoleDao;
    }

    @Override
    public List<Ecole> lister() {
        return ecoleDao.findAll();
    }

    @Override
    public Optional<Ecole> trouver(String numEcole) {
        return ecoleDao.findById(numEcole);
    }

    @Override
    public void creer(Ecole ecole) {
        valider(ecole);
        if (ecoleDao.existsById(ecole.getNumEcole())) {
            throw new BusinessException("Une école avec le code " + ecole.getNumEcole() + " existe déjà.");
        }
        ecoleDao.insert(ecole);
    }

    @Override
    public void modifier(Ecole ecole) {
        valider(ecole);
        if (!ecoleDao.existsById(ecole.getNumEcole())) {
            throw new BusinessException("École introuvable : " + ecole.getNumEcole());
        }
        ecoleDao.update(ecole);
    }

    @Override
    public void supprimer(String numEcole) {
        if (numEcole == null || numEcole.isBlank()) {
            throw new BusinessException("Le code école est obligatoire.");
        }
        String code = numEcole.trim();
        if (!ecoleDao.existsById(code)) {
            throw new BusinessException("École introuvable : " + code);
        }
        try {
            ecoleDao.deleteById(code);
        } catch (RuntimeException e) {
            throw new BusinessException(
                    "Impossible de supprimer l'école " + code
                            + " : des élèves y sont encore rattachés.", e);
        }
    }

    private void valider(Ecole ecole) {
        if (ecole == null) {
            throw new BusinessException("L'école est obligatoire.");
        }
        if (ecole.getNumEcole() == null || ecole.getNumEcole().isBlank()) {
            throw new BusinessException("Le code école est obligatoire.");
        }
        ecole.setNumEcole(ecole.getNumEcole().trim().toUpperCase());
        if (ecole.getNumEcole().length() > 20) {
            throw new BusinessException("Le code école ne doit pas dépasser 20 caractères.");
        }
        if (ecole.getDesign() == null || ecole.getDesign().isBlank()) {
            throw new BusinessException("La désignation de l'école est obligatoire.");
        }
        ecole.setDesign(ecole.getDesign().trim());
        if (ecole.getDesign().length() > 150) {
            throw new BusinessException("La désignation ne doit pas dépasser 150 caractères.");
        }
        if (ecole.getAdresse() != null) {
            ecole.setAdresse(ecole.getAdresse().trim());
            if (ecole.getAdresse().isEmpty()) {
                ecole.setAdresse(null);
            } else if (ecole.getAdresse().length() > 255) {
                throw new BusinessException("L'adresse ne doit pas dépasser 255 caractères.");
            }
        }
    }
}
