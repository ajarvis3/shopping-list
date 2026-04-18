package com.example.shopping.services.impl;

import com.example.shopping.dto.ListDTO;
import com.example.shopping.exception.IllegalEntityException;
import com.example.shopping.exception.EntityNotActionedException;
import com.example.shopping.exception.EntityNotFoundException;
import com.example.shopping.model.ListModel;
import com.example.shopping.repository.ListRepository;
import com.example.shopping.services.IListService;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class ListService implements IListService {

    private static final Logger LOG = LoggerFactory.getLogger(ListService.class);

    private final ModelMapper listMapper;

    public final ListRepository listRepository;

    public ListService(ListRepository listRepository, ModelMapper listMapper) {
        this.listRepository = listRepository;
        this.listMapper = listMapper;
    }

    @Override
    public List<ListDTO> getAll() {
        LOG.debug("getAll()");
        Type listType = new TypeToken<List<ListDTO>>() {}.getType();
        return listMapper.map(listRepository.findAll(), listType);
    }

    @Override
    public ListDTO getById(Long id) {
        LOG.debug("getById({})", id);
        return listRepository.findById(id).map(list ->
                listMapper.map(list, ListDTO.class)
        ).orElseThrow(() -> new EntityNotFoundException("List not found with id: " + id)) ;
    }

    @Override
    @Transactional
    public ListDTO updateList(ListModel list, Long id) {
        try {
            LOG.debug("updateList({}, {})", list, id);
            ListModel listModel = listRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("List not found with id: " + id)) ;
            listModel.setName(list.getName());
            listModel.setOwner(list.getOwner());
            return listMapper.map(listRepository.save(listModel),  ListDTO.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        } catch (ConfigurationException | MappingException | OptimisticLockingFailureException e) {
            throw new EntityNotActionedException(e);
        }
    }

    @Override
    @Transactional
    public ListDTO createList(ListModel list) {
        try {
            LOG.debug("createList({})", list);
            ListModel result = listRepository.save(list);
            return  listMapper.map(result, ListDTO.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalEntityException(e);
        } catch (ConfigurationException | MappingException | OptimisticLockingFailureException e) {
            throw new EntityNotActionedException(e);
        }
    }

    @Override
    @Transactional
    public Long deleteList(Long id) {
        try {
            LOG.debug("deleteList({})", id);
            listRepository.deleteById(id);
            return id;
        } catch (IllegalArgumentException e) {
            throw new IllegalEntityException(e);
        } catch (RuntimeException e) {
            throw new EntityNotActionedException(e);
        }
    }
}
