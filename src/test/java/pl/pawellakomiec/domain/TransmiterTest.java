package pl.pawellakomiec.domain;

import org.junit.Before;
import org.junit.Test;
import pl.pawellakomiec.dao.TransmiterDaoImpl;
import pl.pawellakomiec.repository.TransmiterServiceImpl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class TransmiterTest {

    TransmiterServiceImpl crud = new TransmiterServiceImpl();

    @Before
    public void before() {

        TransmiterTime transmiter1 = new TransmiterTime("Dunlop", "TIG", 1234);
        TransmiterTime transmiter2 = new TransmiterTime("Avanger", "TIG", 1235);
        transmiter1.setCreateTime(new Date());

        crud.transmiterList.add(transmiter1);
        crud.transmiterList.add(transmiter2);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        transmiter2.setCreateTime(new Date());
    }

    @Test
    public void doesListaSpawarekExists() {
        assertNotNull(crud);
    }

    @Test
    public void checkDateExist() {
        assertNotNull(crud.getByID(1234).getCreateTime());
    }

    @Test
    public void checkCreateDates() {
        assertNotEquals(crud.getByID(1234).getCreateTime(), crud.getByID(1235).getCreateTime());
    }

    //Test dla priceu bez sleep przy tworzeniu obiektow
        /*
        @Test
        public void checkCreateDates1() {
                assertEquals(crud.getByID(1234).getCreateTime().getTime(), crud.getByID(1235).getCreateTime().getTime());
        }
        */

    @Test
    public void checkModifyDateNotExists() {
        assertNull(crud.getByID(1234).getModifyTime());
    }

    @Test
    public void howManyElement() {
        assertEquals(2, crud.transmiterList.size());
    }

    @Test
    public void getByID() {
        //assertNotNull (crud.transmiterList.get(0));
        assertNotNull(crud.getByID(1234));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deletingElement() {
        crud.removeTransmiter(crud.getByID(1235));
        TransmiterTime s2 = crud.getByID(1235);
    }

    @Test
    public void renameElement() {
        crud.renameTransmiter(1234, "Zmiana");
        assertEquals("Zmiana", crud.getByID(1234).getName());
    }

    @Test
    public void checkReadDates() {
        assertNotNull(crud.getByID(1234).getReadTime());
    }

    @Test
    public void checkModifyDateExists() {
        crud.renameTransmiter(1234, "Oooops");
        assertNotNull(crud.getByID(1234).getModifyTime());
    }
}