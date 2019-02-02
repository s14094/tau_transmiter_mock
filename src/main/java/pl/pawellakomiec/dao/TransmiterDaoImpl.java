package pl.pawellakomiec.dao;

import pl.pawellakomiec.domain.TransmiterTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TransmiterDaoImpl implements TransmiterDao {

    private Connection connection;
    private PreparedStatement addTransmiterStmt;
    private PreparedStatement getAllSpawarkiStmt;
    private PreparedStatement getTransmiterById;
    private PreparedStatement updateDateTransmiter;
    private PreparedStatement getTimeFromTransmiter;
    private PreparedStatement takeLastRecord;

    public TransmiterDaoImpl(Connection connection) throws SQLException {
        this.connection = connection;
        if (!isDatabaseReady())
            createTables();
        setConnection(connection);
    }

    public TransmiterDaoImpl() {
    }

    public void createTables() throws SQLException {
        connection.createStatement().executeUpdate(
                "CREATE TABLE "
                        + "Transmiter(id bigint GENERATED BY DEFAULT AS IDENTITY, "
                        + "name varchar(20) NOT NULL, "
                        + "model varchar(20) NOT NULL, "
                        + "price int NOT NULL, "
                        + "createTime TIMESTAMP, "
                        + "readTime TIMESTAMP, "
                        + "modifyTime TIMESTAMP)");
    }

    public boolean isDatabaseReady() {
        try {
            ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
            boolean tableExists = false;
            while (rs.next()) {
                if ("Transmiter".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                    tableExists = true;
                    break;
                }
            }
            return tableExists;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void setConnection(Connection connection) throws SQLException {
        this.connection = connection;
        addTransmiterStmt = connection.prepareStatement("INSERT INTO Transmiter (name, model, price, createTime) VALUES (?, ?, ?, now())");

    }

    @Override
    public TransmiterTime findTransmiterById(int id) throws SQLException {
        TransmiterTime TransmiterTime = new TransmiterTime();
//        updateDateInSql(id, 1);
        getTransmiterById = connection.prepareStatement("SELECT * FROM Transmiter WHERE id = " + id);

        try {
            ResultSet rs = getTransmiterById.executeQuery();
            updateDateInSql(id, 1);
            while (rs.next()) {
                TransmiterTime.setId(rs.getInt("id"));
                TransmiterTime.setName(rs.getString("name"));
                TransmiterTime.setModel(rs.getString("model"));
                TransmiterTime.setPrice(rs.getInt("price"));
                TransmiterTime.setCreateTime(rs.getDate("createTime"));
                TransmiterTime.setReadTime(this.currentDate());
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return TransmiterTime;
    }


    @Override
    public void updateDateInSql(int TransmiterId, int typeUpdate) throws SQLException {

        switch (typeUpdate) {
            //przy odczycie z bazy danych
            case 1:
                updateDateTransmiter = connection.prepareStatement("UPDATE Transmiter SET readTime = now() WHERE id = " + TransmiterId);
                updateDateTransmiter.executeQuery();
                break;

            //przy edycji rekordu
            case 2:
                updateDateTransmiter = connection.prepareStatement("UPDATE Transmiter SET modifyTime = now() WHERE id = " + TransmiterId);
                updateDateTransmiter.executeQuery();
                break;
        }
    }

    @Override
    public Date checkDateInDatabase(TransmiterTime TransmiterTime, int dateVariant) throws SQLException {

        Date date = new Date();
        switch (dateVariant) {
            //pobierz date kreacji obiektu
            case 1: {
                getTimeFromTransmiter = connection.prepareStatement("SELECT createTime FROM Transmiter WHERE id = " + TransmiterTime.getId());
                ResultSet rs = getTimeFromTransmiter.executeQuery();
                date = rs.getDate("creationDate");
                break;
            }
            //pobierz date ostatniego pobrania obiektu
            case 2: {
                getTimeFromTransmiter = connection.prepareStatement("SELECT readTime FROM Transmiter WHERE id = " + TransmiterTime.getId());
                ResultSet rs = getTimeFromTransmiter.executeQuery();
                date = rs.getDate("readTime");
                break;
            }
            //pobierz date ostatniej edycji obiektu
            case 3: {
                getTimeFromTransmiter = connection.prepareStatement("SELECT modifyTime FROM Transmiter WHERE id = " + TransmiterTime.getId());
                ResultSet rs = getTimeFromTransmiter.executeQuery();
                date = rs.getDate("modifyTime");
                break;
            }
        }
        return date;
    }

    @Override
    public Date currentDate() {
        return new Date();
    }

    @Override
    public int addTransmiter(TransmiterTime TransmiterTime) {
        int count = 0;
        try {
            addTransmiterStmt.setString(1, TransmiterTime.getName());
            addTransmiterStmt.setString(2, TransmiterTime.getModel());
            addTransmiterStmt.setInt(3, TransmiterTime.getPrice());
            count = addTransmiterStmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return count;
    }

    @Override
    public int updateNameOfTransmiter(int idSpawarki, String nameToUpdate) throws SQLException {
        int count = 0;
        try {
            updateDateTransmiter = connection.prepareStatement("UPDATE Transmiter SET name = " + nameToUpdate + " WHERE id = " + idSpawarki);
            updateDateTransmiter.executeQuery();
            updateDateInSql(idSpawarki, 2);
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return 0;
    }

    @Override
    public List<TransmiterTime> getAllTransmiters() {
        List<TransmiterTime> TransmiterTimeList = new LinkedList<>();
        try {
            getAllSpawarkiStmt = connection.prepareStatement("SELECT * FROM Transmiter");
            ResultSet rs = getAllSpawarkiStmt.executeQuery();

            while (rs.next()) {
                TransmiterTime TransmiterTime = new TransmiterTime();
                TransmiterTime.setId(rs.getInt("id"));
                TransmiterTime.setName(rs.getString("name"));
                TransmiterTime.setModel(rs.getString("model"));
                TransmiterTime.setPrice(rs.getInt("price"));
                TransmiterTimeList.add(TransmiterTime);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return TransmiterTimeList;
    }
}