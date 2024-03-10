package hello.itemservice.domain;

import lombok.Data;

@Data   // 위험함
public class Item {
    private Long id;
    private String itemName;
    private int price;
    private int quantity;

    public Item() {

    }

    public Item(String itemName, int price, int quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
