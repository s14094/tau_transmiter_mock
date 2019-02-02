package pl.pawellakomiec.domain;

import java.util.Date;

public class TransmiterTime extends Transmiter{

    Date createTime;
    Date readTime;
    Date modifyTime;


    public TransmiterTime() {
    }

    public TransmiterTime(String name, String model, Integer price) {
        super(name, model, price);
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
