package com.nitin.texttosql.controller;

import com.nitin.texttosql.service.SqlService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sql")
public class SqlController {

    private final SqlService sqlService;

    public SqlController(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    @PostMapping("/generate")
    public String generateSql(@RequestBody String question) {

        return sqlService.generateSql(question);
    }
}