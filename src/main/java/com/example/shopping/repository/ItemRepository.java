package com.example.shopping.repository;

import com.example.shopping.model.ItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemModel, Long> {
    public List<ItemModel> findByName(String name);
    public Optional<ItemModel> findById(Long id);
}
