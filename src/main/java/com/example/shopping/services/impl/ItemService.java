package com.example.shopping.services.impl;

import com.example.shopping.dto.ItemDTO;
import com.example.shopping.exception.EntityNotActionedException;
import com.example.shopping.exception.EntityNotFoundException;
import com.example.shopping.exception.IllegalEntityException;
import com.example.shopping.model.ItemModel;
import com.example.shopping.repository.ItemRepository;
import com.example.shopping.services.IItemService;
import org.modelmapper.ModelMapper;

public class ItemService implements IItemService {

    private final ItemRepository itemRepository;

    private final ModelMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ModelMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDTO createItem(ItemModel item){
        try {
            itemRepository.save(item);
            return itemMapper.map(item, ItemDTO.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        } catch (RuntimeException e) {
            throw new EntityNotActionedException(e);
        }
    }

    @Override
    public ItemDTO getItem(long id) {
        return itemRepository.findById(id)
                .map(item -> itemMapper.map(item, ItemDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + id));
    }

    @Override
    public long deleteItem(long id) {
        try {
            itemRepository.deleteById(id);
            return id;
        } catch (IllegalArgumentException e) {
            throw new IllegalEntityException(e);
        } catch (RuntimeException e) {
            throw new EntityNotActionedException(e);
        }
    }

}
