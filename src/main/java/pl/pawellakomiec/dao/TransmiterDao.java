package pl.pawellakomiec.dao;

import pl.pawellakomiec.domain.Transmiter;
import pl.pawellakomiec.domain.TransmiterTime;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.sql.Connection;

public interface TransmiterDao {

    Connection getConnection();

    void setConnection(Connection connection) throws SQLException;

    TransmiterTime findTransmiterById(int id) throws SQLException;

    void updateDateInSql(int transmiterId, int typeUpdate) throws SQLException;

    Date checkDateInDatabase(TransmiterTime transmiterTime, int dateVariant) throws SQLException;

    Date currentDate();

    int addTransmiter(TransmiterTime transmiterTime);

    int updateNameOfTransmiter(int idSpawarki, String nameToUpdate) throws SQLException;

    List<TransmiterTime> getAllTransmiters();
}