package mg.cepe.gestion.service;

import java.lang.reflect.Method;
import java.util.List;

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
        List<?> reussis = service.listerReussis("2022-2023");
        assertFalse(reussis.isEmpty());
        assertEquals("RAKOTO", readProperty(reussis.get(0), "getNom"));
    }

    @Test
    void testAdmisSixieme() {
        List<?> admis = service.listerAdmisSixieme("2022-2023");
        assertFalse(admis.isEmpty());
        assertTrue(((Number) readProperty(admis.get(0), "getMoyenne")).doubleValue() > 12.0);
    }

    private Object readProperty(Object target, String methodName) {
        try {
            Method method = target.getClass().getMethod(methodName);
            return method.invoke(target);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }
}
