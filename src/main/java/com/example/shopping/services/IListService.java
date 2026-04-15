package com.example.shopping.services;

import com.example.shopping.dto.ListDTO;
import com.example.shopping.model.ListModel;

import java.util.List;

public interface IListService {
    public List<ListDTO> getAll();
    public ListDTO getById(long id);
    public ListDTO createList(ListModel list);
    public long deleteList(long id);
}
