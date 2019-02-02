package pl.pawellakomiec.repository;

import pl.pawellakomiec.domain.TransmiterTime;

import javax.activation.DataSource;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransmiterServiceImpl {

    public List<TransmiterTime> transmiterList = new ArrayList<>();

    public void addTransmiter(TransmiterTime transmiter) {
        transmiterList.add(transmiter);
        transmiter.setCreateTime(new Date());
    }

    public TransmiterTime addTransmiterDate(TransmiterTime transmiter) {
        transmiter.setCreateTime(new Date());
        transmiterList.add(transmiter);
        return transmiter;
    }

    public void removeTransmiter(TransmiterTime transmiter) {
        transmiterList.remove(transmiter);
    }

    public void renameTransmiter(int id, String name) {
        for (TransmiterTime transmiter : transmiterList) {
            if (transmiter.price == id) {
                transmiter.name = name;
                transmiter.setModifyTime(new Date());
            }
        }
    }

    public TransmiterTime getByID(int id) {
        try {
            for (TransmiterTime s : transmiterList) {
                if (s.price == id) {
                    s.setReadTime(this.setCurrentDate());
                    return s;
                }
            }
        } catch (IllegalArgumentException e) {
            //System.out.println("Exception thrown  :" + e);
        }
        //.out.println("Not found");
        throw new IllegalArgumentException("Not found");
    }

    public Date setCurrentDate() {
        return new Date();
    }

    public List<TransmiterTime> getListaSpawarek() {
        return transmiterList;
    }

    public Date dateConverterFromString(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        return sdf.parse(dateString);
    }

    public TransmiterTime transmiterTimeWithReadDate(String nameOfTransmiter) {
        TransmiterTime transmiterTimeToReturn = null;
        for (TransmiterTime transmiterTime : transmiterList) {
            if (transmiterTime.getName().equals(nameOfTransmiter)) {
                transmiterTime.setReadTime(new Date());
                transmiterTimeToReturn = transmiterTime;
            }
        }
        return transmiterTimeToReturn;
    }

    public TransmiterTime updateNameOfObject(String currentName, String newName) {
        TransmiterTime transmiterTimeToReturn = null;
        for (TransmiterTime transmiterTime : transmiterList) {
            if (transmiterTime.getName().equals(currentName)) {

                transmiterTime.setName(newName);
                transmiterTime.setModifyTime(new Date());
                transmiterTimeToReturn = transmiterTime;
            }
        }
        return transmiterTimeToReturn;
    }

    public TransmiterTime multiDateUpdater(TransmiterTime transmiterTime, boolean createDate, Date createDateInfo, boolean readDate, Date readDateInfo, boolean modifyDate, Date modifyDateInfo) {

        if (createDate)
            transmiterTime.setCreateTime(createDateInfo);

        if (readDate)
            transmiterTime.setReadTime(readDateInfo);

        if (modifyDate)
            transmiterTime.setModifyTime(modifyDateInfo);

        return transmiterTime;
    }
}