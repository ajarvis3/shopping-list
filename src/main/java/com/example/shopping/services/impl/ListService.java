package com.example.shopping.services.impl;

import com.example.shopping.dto.ListDTO;
import com.example.shopping.exception.IllegalEntityException;
import com.example.shopping.exception.EntityNotActionedException;
import com.example.shopping.exception.EntityNotFoundException;
import com.example.shopping.model.ListModel;
import com.example.shopping.repository.ListRepository;
import com.example.shopping.services.IListService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListService implements IListService {

    private final ModelMapper listMapper;

    public final ListRepository listRepository;

    public ListService(ListRepository listRepository, ModelMapper listMapper) {
        this.listRepository = listRepository;
        this.listMapper = listMapper;
    }

    @Override
    public List<ListDTO> getAll() {
        List<ListDTO> listDTOList = new ArrayList<ListDTO>();
        listMapper.map(listRepository.findAll(), listDTOList);
        return listDTOList;
    }

    @Override
    public ListDTO getById(long id) {
        return listRepository.findById(id).map(list ->
            listMapper.map(list, ListDTO.class)
        ).orElseThrow(() -> new EntityNotFoundException("List not found with id: " + id)) ;
    }

    @Override
    public ListDTO createList(ListModel list) {
        try {
            ListModel result = listRepository.save(listMapper.map(list, ListModel.class));
            return  listMapper.map(result, ListDTO.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalEntityException(e);
        } catch (RuntimeException e) {
            throw new EntityNotActionedException(e);
        }
    }

    @Override
    public long deleteList(long id) {
        try {
            listRepository.deleteById(id);
            return id;
        } catch (IllegalArgumentException e) {
            throw new IllegalEntityException(e);
        } catch (RuntimeException e) {
            throw new EntityNotActionedException(e);
        }
    }
}
