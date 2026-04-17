package com.example.shopping.controllers;

import com.example.shopping.dto.ItemDTO;
import com.example.shopping.model.ItemModel;
import com.example.shopping.services.IItemService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/list/{listId}/items")
public class ItemController {

    private ModelMapper modelMapper;

    private IItemService itemService;

    public ItemController(IItemService itemService, ModelMapper modelMapper){
        this.itemService = itemService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemService.getItem(id));
    }

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@RequestBody ItemDTO itemDTO, @PathVariable Long listId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemService.createItem(modelMapper.map(itemDTO, ItemModel.class), listId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteItem(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemService.deleteItem(id));
    }
}
