package pl.pawellakomiec.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.pawellakomiec.dao.TransmiterDao;
import pl.pawellakomiec.dao.TransmiterDaoImpl;
import pl.pawellakomiec.domain.TransmiterTime;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransmiterDaoMockTest {

    @Mock
    Connection connectionMock;
    @Mock
    PreparedStatement addTransmiterStmt;
    @Mock
    PreparedStatement updateDateTransmiter;
    @Mock
    PreparedStatement getTransmiterById;
    @Mock
    PreparedStatement getTimeFromTransmiter;
    private TransmiterDao transmiterDao;

    @Before
    public void setup() throws SQLException {

        when(connectionMock.prepareStatement("INSERT INTO Transmiter (name, model, price, createTime) VALUES (?, ?, ?, now())")).thenReturn(addTransmiterStmt);
        transmiterDao = new TransmiterDaoImpl();
        transmiterDao.setConnection(connectionMock);
    }

    @Test
    public void checkGetting() throws SQLException {

        AbstractResultSet abstractResultSet = mock(AbstractResultSet.class);
        when(abstractResultSet.next()).thenCallRealMethod();
        when(abstractResultSet.getInt(any())).thenCallRealMethod();
        when(abstractResultSet.getString(any())).thenCallRealMethod();

        when(connectionMock.prepareStatement("SELECT * FROM Transmiter WHERE id = 1")).thenReturn(getTransmiterById);
        when(getTransmiterById.executeQuery()).thenReturn(abstractResultSet);
        when(connectionMock.prepareStatement("UPDATE Transmiter SET readTime = now() WHERE id = 1")).thenReturn(updateDateTransmiter);

        transmiterDao.findTransmiterById(1);

        verify(connectionMock).prepareStatement("SELECT * FROM Transmiter WHERE id = 1");
        verify(getTransmiterById, times(1)).executeQuery();
        verify(connectionMock).prepareStatement("UPDATE Transmiter SET readTime = now() WHERE id = 1");

        verify(abstractResultSet, times(1)).getInt("id");
        verify(abstractResultSet, times(1)).getString("name");
        verify(abstractResultSet, times(1)).getString("model");
        verify(abstractResultSet, times(1)).getInt("price");
        verify(abstractResultSet, times(1)).getDate("createTime");
    }

    @Test
    public void checkUpdate() throws SQLException {

        AbstractResultSet abstractResultSet = mock(AbstractResultSet.class);
//        when(abstractResultSet.next()).thenCallRealMethod();
//        when(abstractResultSet.getInt(any())).thenCallRealMethod();
//        when(abstractResultSet.getString(any())).thenCallRealMethod();

        when(connectionMock.prepareStatement("UPDATE Transmiter SET name = updatedName WHERE id = 1")).thenReturn(updateDateTransmiter);
        when(updateDateTransmiter.executeQuery()).thenReturn(abstractResultSet);
        when(connectionMock.prepareStatement("UPDATE Transmiter SET modifyTime = now() WHERE id = 1")).thenReturn(updateDateTransmiter);

        transmiterDao.updateNameOfTransmiter(1, "updatedName");

        verify(connectionMock).prepareStatement("UPDATE Transmiter SET name = updatedName WHERE id = 1");
        verify(updateDateTransmiter, times(2)).executeQuery();
    }

    @Test
    public void checkAddNewRecord() throws SQLException {

        when(addTransmiterStmt.executeUpdate()).thenReturn(1);

        TransmiterTime transmiterTime = new TransmiterTime("Transmiter", "Audi", 111);

        assertEquals(1, transmiterDao.addTransmiter(transmiterTime));

        verify(addTransmiterStmt, times(1)).setString(1, "Transmiter");
        verify(addTransmiterStmt, times(1)).setString(2, "Audi");
        verify(addTransmiterStmt, times(1)).setInt(3, 111);
        verify(connectionMock).prepareStatement("INSERT INTO Transmiter (name, model, price, createTime) VALUES (?, ?, ?, now())");
    }

    @Test
    public void getTimeOfCreationEditOrUpdate() throws SQLException {

        AbstractResultSet abstractResultSet = mock(AbstractResultSet.class);
        when(abstractResultSet.getDate(any())).thenCallRealMethod();

        TransmiterTime transmiterTime = new TransmiterTime("aaa", "bbb", 111);
        transmiterTime.setId(1);

        /*
        /Test of check creation date
         */

        when(connectionMock.prepareStatement("SELECT createTime FROM Transmiter WHERE id = 1")).thenReturn(getTimeFromTransmiter);
        when(getTimeFromTransmiter.executeQuery()).thenReturn(abstractResultSet);

        transmiterDao.checkDateInDatabase(transmiterTime, 1);

        verify(abstractResultSet, times(1)).getDate("creationDate");

        /*
        /Test of last check record date
         */

        when(connectionMock.prepareStatement("SELECT readTime FROM Transmiter WHERE id = 1")).thenReturn(getTimeFromTransmiter);
        when(getTimeFromTransmiter.executeQuery()).thenReturn(abstractResultSet);

        transmiterDao.checkDateInDatabase(transmiterTime, 2);

        verify(abstractResultSet, times(1)).getDate("readTime");

        /*
        /Test of last edit date
         */

        when(connectionMock.prepareStatement("SELECT modifyTime FROM Transmiter WHERE id = 1")).thenReturn(getTimeFromTransmiter);
        when(getTimeFromTransmiter.executeQuery()).thenReturn(abstractResultSet);

        transmiterDao.checkDateInDatabase(transmiterTime, 3);

        verify(abstractResultSet, times(1)).getDate("modifyTime");
    }

    abstract class AbstractResultSet implements ResultSet {
        int i = 0;

        @Override
        public int getInt(String s) throws SQLException {
            return 1;
        }

        @Override
        public String getString(String columnLabel) throws SQLException {
            return "Majran";
        }

        @Override
        public Date getDate(String columnLabel) {
            return new Date(123456);
        }

        @Override
        public boolean next() throws SQLException {
            if (i == 1)
                return false;
            i++;
            return true;
        }
    }
}
