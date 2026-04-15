package com.example.shopping.services;

import com.example.shopping.dto.ItemDTO;
import com.example.shopping.model.ItemModel;
import org.springframework.stereotype.Service;

@Service
public interface IItemService {
    public ItemDTO createItem(ItemModel item, Long listId);
    public ItemDTO getItem(Long id);
    public Long deleteItem(Long id);
}
