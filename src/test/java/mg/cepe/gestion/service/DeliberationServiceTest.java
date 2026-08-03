package mg.cepe.gestion.service;

import mg.cepe.gestion.service.impl.DeliberationServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliberationServiceTest {

    private final DeliberationService service = new DeliberationServiceImpl();

    @Test
    void testCalculMoyenneRakoto() {
        double moy = service.calculerMoyenne("ELV001", "2022-2023");
        assertEquals(13.73, moy, 0.01);
    }

    @Test
    void testCalculMoyenneRasoa() {
        double moy = service.calculerMoyenne("ELV002", "2022-2023");
        assertTrue(moy < 9.75);
    }

    @Test
    void testDeliberationReussis() {
        var reussis = service.listerReussis("2022-2023");
        assertFalse(reussis.isEmpty());
        assertEquals("RAKOTO", reussis.get(0).getNom());
    }

    @Test
    void testAdmisSixieme() {
        var admis = service.listerAdmisSixieme("2022-2023");
        assertFalse(admis.isEmpty());
        assertTrue(admis.get(0).getMoyenne() > 12.0);
    }
}
