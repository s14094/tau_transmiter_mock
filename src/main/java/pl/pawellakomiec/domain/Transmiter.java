package pl.pawellakomiec.domain;

public class Transmiter {

    public int id;
    public String name;
    public String model;
    public Integer price;

    public Transmiter(String name, String model, Integer price) {
        this.name = name;
        this.model = model;
        this.price = price;
    }

    public Transmiter() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
