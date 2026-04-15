package com.example.shopping.dto;

public class ItemDTO {
    private Long id;
    private Long listId;
    private String name;
    private int quantity;

    public ItemDTO() {}

    public ItemDTO(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public ItemDTO(Long id, Long listId, String name, int quantity) {
        this.id = id;
        this.listId = listId;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getListId() {
        return listId;
    }
    public void setListId(Long listId) {
        this.listId = listId;
    }
}