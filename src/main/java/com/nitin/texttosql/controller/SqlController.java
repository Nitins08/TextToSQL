package com.nitin.texttosql.controller;

import com.nitin.texttosql.model.QueryResponse;
import com.nitin.texttosql.service.SqlService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sql")
public class SqlController {

    private final SqlService sqlService;

    public SqlController(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    @PostMapping("/ask")
    public QueryResponse askDatabase(
            @RequestBody String question) {

        return sqlService.generateAndExecute(question);
    }
}