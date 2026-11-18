package org.example;

public class Product {
    private int id;
    private String name;
    private int quality;

    public Product() {
    }

    public Product(int id, String name, int quality) {
        this.id = id;
        this.name = name;
        this.quality = quality;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getQuality() { return quality; }
    public void setQuality(int quality) { this.quality = quality; }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', quality=" + quality + "}";
    }
}
