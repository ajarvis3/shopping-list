package com.example.shopping.controllers;

import com.example.shopping.dto.ItemDTO;
import com.example.shopping.dto.ListDTO;
import com.example.shopping.model.ListModel;
import com.example.shopping.services.impl.ListService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/list")
public class ListController {

    private ModelMapper modelMapper;

    private ListService listService;

    public ListController(ModelMapper modelMapper, ListService listService){
        this.modelMapper = modelMapper;
        this.listService = listService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListDTO> getList(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ListDTO>> getLists() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listService.getAll());
    }

    @PostMapping
    public ResponseEntity<ListDTO> postList(@RequestBody ListDTO listDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listService.createList(modelMapper.map(listDTO, ListModel.class)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListDTO> put(@RequestBody ListDTO listDTO, @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listService.updateList(modelMapper.map(listDTO, ListModel.class), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listService.deleteList(id));
    }
}