package com.example.shopping.services.impl;

import com.example.shopping.dto.ItemDTO;
import com.example.shopping.exception.EntityNotActionedException;
import com.example.shopping.exception.EntityNotFoundException;
import com.example.shopping.exception.IllegalEntityException;
import com.example.shopping.model.ItemModel;
import com.example.shopping.model.ListModel;
import com.example.shopping.repository.ItemRepository;
import com.example.shopping.repository.ListRepository;
import com.example.shopping.services.IItemService;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService implements IItemService {

    private static final Logger LOG = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;

    private final ListRepository listRepository;

    private final ModelMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ModelMapper itemMapper,  ListRepository listRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.listRepository = listRepository;
    }

    @Override
    @Transactional
    public ItemDTO createItem(ItemModel item, Long listId){
        try {
            LOG.debug("Creating Item on list {} ", listId);
            item.setId(null);
            ListModel listModel = listRepository.findById(listId)
                    .orElseThrow(() ->
                            new EntityNotFoundException("List not found with id: " + listId));
            item.setListModel(listModel);
            itemRepository.save(item);
            return itemMapper.map(item, ItemDTO.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        } catch (ConfigurationException | MappingException | OptimisticLockingFailureException e) {
            throw new EntityNotActionedException(e);
        }
    }

    @Override
    public ItemDTO getItem(Long id) {
        LOG.debug("Getting Items from list {} ", id);
        return itemRepository.findById(id)
                .map(item -> itemMapper.map(item, ItemDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + id));
    }

    @Override
    @Transactional
    public Long deleteItem(Long id) {
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
