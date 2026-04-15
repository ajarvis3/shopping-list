package com.example.shopping.repository;

import com.example.shopping.model.ListModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListRepository extends JpaRepository<ListModel, Long> {
    public List<ListModel> findByName(String name);
    public Optional<ListModel> findById(Long id);
}
