package com.example.shopping.dto;

import java.util.ArrayList;
import java.util.List;

public class ListDTO {
    Long id;
    String name;
    String owner;
    List<ItemDTO> items;

    public ListDTO() {
        this.items = new ArrayList<>();
    }
    public ListDTO(String name, String owner) {
        this.name = name;
        this.owner = owner;
        this.items = new ArrayList<>();
    }
    public ListDTO(Long id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.items = new ArrayList<>();
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<ItemDTO> getItems() {
        return items;
    }
    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }
}
