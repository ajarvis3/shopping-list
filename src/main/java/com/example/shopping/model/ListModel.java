package com.example.shopping.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lists")
public class ListModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String owner;

    @OneToMany(cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        mappedBy = "list",
        orphanRemoval = true)
    private List<ItemModel> items;

    public ListModel(){
        this.items = new ArrayList<>();
    }

    public ListModel(String name, String owner) {
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
    public List<ItemModel> getItems() {
        return items;
    }
    public void setItems(List<ItemModel> items) {
        this.items = items;
    }
}
