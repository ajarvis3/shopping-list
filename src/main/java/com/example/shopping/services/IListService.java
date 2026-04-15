package com.example.shopping.services;

import com.example.shopping.dto.ListDTO;
import com.example.shopping.model.ListModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IListService {
    public List<ListDTO> getAll();
    public ListDTO getById(Long id);
    public ListDTO createList(ListModel list);
    public ListDTO updateList(ListModel list, Long id);
    public Long deleteList(Long id);
}
