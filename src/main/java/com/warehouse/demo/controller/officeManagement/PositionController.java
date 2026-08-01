package com.warehouse.demo.controller.officeManagement;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.service.PositionService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
public class PositionController {
    private final PositionService positionService;

    @GetMapping
    public ResponseEntity<List<Position>> findAll() {
        List<Position> positions = positionService.findAll();
        return new ResponseEntity<List<Position>>(positions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Position> findById(@PathVariable long id) {
        Position position = positionService.findById(id);
        return new ResponseEntity<Position>(position, HttpStatus.OK);
    }
    
    /* @PostMapping
    public ResponseEntity<Position> create(@RequestBody Position position) {
        
    }

    @PatchMapping
    public ResponseEntity<Position> update(@RequestBody Position position) {
        
    } */
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        positionService.delete(id);
        return new ResponseEntity<String>("Deleted.", HttpStatus.OK);
    }
}
