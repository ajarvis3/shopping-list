package com.example.shopping.services;

import com.example.shopping.dto.ItemDTO;
import com.example.shopping.model.ItemModel;

public interface IItemService {
    public ItemDTO createItem(ItemModel item);
    public ItemDTO getItem(long id);
    public long deleteItem(long id);
}
