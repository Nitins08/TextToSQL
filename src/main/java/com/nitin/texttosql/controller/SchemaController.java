package com.nitin.texttosql.controller;

import com.nitin.texttosql.service.SchemaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schema")
public class SchemaController {

    private final SchemaService schemaService;

    public SchemaController(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @GetMapping
    public List<Map<String, Object>> getSchema() {
        return schemaService.getTables();
    }

    @GetMapping("/columns")
    public List<Map<String, Object>> getColumns() {
        return schemaService.getColumns();
    }

    @GetMapping("/relationships")
    public List<Map<String, Object>> getRelationships() {
        return schemaService.getRelationships();
    }

    @GetMapping("/summary")
    public String getSchemaSummary() {
        return schemaService.getSchemaSummary();
    }
}