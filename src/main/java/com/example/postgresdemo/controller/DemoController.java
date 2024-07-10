package com.example.postgresdemo.controller;

import com.example.postgresdemo.exception.ResourceNotFoundException;
import com.example.postgresdemo.model.Demo;
import com.example.postgresdemo.repository.DemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DemoController {

    @Autowired
    private DemoRepository demoRepository;

    @GetMapping("/demos")
    public List<Demo> getDemo() {
        return demoRepository.findAll();
    }

    @PostMapping("/demos")
    public Demo addDemo(@RequestBody Demo demo) {
        return demoRepository.save(demo);
    }

    @PutMapping("/demos/{demoId}")
    public Demo updateDemo(@PathVariable Long demoId,
                               @Valid @RequestBody Demo demoRequest) {
        if(!demoRepository.existsById(demoId)) {
            throw new ResourceNotFoundException("Demo not found with id " + demoId);
        }

        return demoRepository.findById(demoId)
                .map(demo -> {
                    demo.setName(demoRequest.getName());
                    return demoRepository.save(demo);
                }).orElseThrow(() -> new ResourceNotFoundException("Demo not found with id " + demoId));
    }

    @DeleteMapping("/demos/{demoId}")
    public ResponseEntity<?> deleteDemo(@PathVariable Long demoId) {
        return demoRepository.findById(demoId)
                .map(demo -> {
                    demoRepository.delete(demo);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Demo not found with id " + demoId));

    }
}
