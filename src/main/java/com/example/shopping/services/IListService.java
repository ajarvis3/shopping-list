package com.example.shopping.services;

import com.example.shopping.dto.ListDTO;
import com.example.shopping.model.ListModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IListService {
    public List<ListDTO> getAll();
    public ListDTO getById(long id);
    public ListDTO createList(ListModel list);
    public long deleteList(long id);
}
