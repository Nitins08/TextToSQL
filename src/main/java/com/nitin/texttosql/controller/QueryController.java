package com.nitin.texttosql.controller;

import com.nitin.texttosql.model.QueryHistory;
import com.nitin.texttosql.service.QueryHistoryService;
import com.nitin.texttosql.service.QueryService;
import com.nitin.texttosql.service.SqlSafetyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    private final QueryService queryService;
    private final QueryHistoryService queryHistoryService;
    private final SqlSafetyService sqlSafetyService;

    public QueryController(
            QueryService queryService,
            QueryHistoryService queryHistoryService,
            SqlSafetyService sqlSafetyService) {

        this.queryService = queryService;
        this.queryHistoryService = queryHistoryService;
        this.sqlSafetyService = sqlSafetyService;
    }

    @PostMapping("/execute")
    public List<Map<String, Object>> executeQuery(
            @RequestBody String sql) {

        if (!sqlSafetyService.isSafe(sql)) {
            throw new IllegalArgumentException(
                    "SQL query was blocked for safety reasons."
            );
        }

        return queryService.executeQuery(sql);
    }

    @GetMapping("/history")
    public List<QueryHistory> getHistory() {
        return queryHistoryService.getHistory();
    }
}