package hello.itemservice.domain;

import lombok.Data;

@Data
public class Product {
    private Long id;
    private String name;
    private int price;
    private int quantity;

    public Product(Long id, String name, int price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
