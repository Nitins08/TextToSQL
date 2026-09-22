package com.nitin.texttosql.service;

import com.nitin.texttosql.ai.SqlGenerator;
import com.nitin.texttosql.model.QueryResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SqlService {

    private final SqlGenerator sqlGenerator;
    private final SchemaService schemaService;
    private final QueryService queryService;
    private final SqlSafetyService sqlSafetyService;
    private final SqlWriteSafetyService sqlWriteSafetyService;
    private final WriteQueryService writeQueryService;
    private final QueryHistoryService queryHistoryService;

    public SqlService(
            SqlGenerator sqlGenerator,
            SchemaService schemaService,
            QueryService queryService,
            SqlSafetyService sqlSafetyService,
            SqlWriteSafetyService sqlWriteSafetyService,
            WriteQueryService writeQueryService,
            QueryHistoryService queryHistoryService) {

        this.sqlGenerator = sqlGenerator;
        this.schemaService = schemaService;
        this.queryService = queryService;
        this.sqlSafetyService = sqlSafetyService;
        this.sqlWriteSafetyService = sqlWriteSafetyService;
        this.writeQueryService = writeQueryService;
        this.queryHistoryService = queryHistoryService;
    }

    public String generateSql(String question) {

        String schema = schemaService.getSchemaSummary();

        String sql = sqlGenerator.generateSql(schema, question);

        if (sql == null || sql.isBlank()) {
            throw new IllegalArgumentException(
                    "The AI could not generate a valid SQL query."
            );
        }

        if ("INVALID".equalsIgnoreCase(sql.trim())) {
            throw new IllegalArgumentException(
                    "The question cannot be answered using the available database."
            );
        }

        return sql.trim();
    }

    public QueryResponse generateAndExecute(String question) {

        String sql;

        try {
            sql = generateSql(question);

        } catch (IllegalArgumentException exception) {

            queryHistoryService.record(
                    question,
                    "INVALID",
                    "INVALID",
                    false,
                    0
            );

            throw exception;
        }

        long startTime = System.currentTimeMillis();

        /*
         * WRITE OPERATIONS
         */
        if (sqlWriteSafetyService.isWriteOperation(sql)) {

            if (!sqlWriteSafetyService.areWritesEnabled()) {

                long executionTime =
                        System.currentTimeMillis() - startTime;

                queryHistoryService.record(
                        question,
                        sql,
                        "WRITE",
                        false,
                        executionTime
                );

                throw new IllegalArgumentException(
                        "Write operations are currently disabled."
                );
            }

            if (!sqlWriteSafetyService.isSafeWrite(sql)) {

                long executionTime =
                        System.currentTimeMillis() - startTime;

                queryHistoryService.record(
                        question,
                        sql,
                        "WRITE",
                        false,
                        executionTime
                );

                throw new IllegalArgumentException(
                        "Write operation was blocked for safety reasons."
                );
            }

            try {

                int affectedRows =
                        writeQueryService.executeWrite(sql);

                long executionTime =
                        System.currentTimeMillis() - startTime;

                queryHistoryService.record(
                        question,
                        sql,
                        "WRITE",
                        true,
                        executionTime
                );

                return new QueryResponse(
                        question,
                        sql,
                        "WRITE",
                        true,
                        executionTime,
                        null,
                        affectedRows,
                        null
                );

            } catch (RuntimeException exception) {

                long executionTime =
                        System.currentTimeMillis() - startTime;

                queryHistoryService.record(
                        question,
                        sql,
                        "WRITE",
                        false,
                        executionTime
                );

                throw exception;
            }
        }

        /*
         * READ OPERATIONS
         */
        if (!sqlSafetyService.isSafe(sql)) {

            long executionTime =
                    System.currentTimeMillis() - startTime;

            queryHistoryService.record(
                    question,
                    sql,
                    "READ",
                    false,
                    executionTime
            );

            throw new IllegalArgumentException(
                    "Generated SQL was blocked for safety reasons."
            );
        }

        try {

            List<Map<String, Object>> result =
                    queryService.executeQuery(sql);

            long executionTime =
                    System.currentTimeMillis() - startTime;

            queryHistoryService.record(
                    question,
                    sql,
                    "READ",
                    true,
                    executionTime
            );

            return new QueryResponse(
                    question,
                    sql,
                    "READ",
                    true,
                    executionTime,
                    result.size(),
                    null,
                    result
            );

        } catch (RuntimeException exception) {

            long executionTime =
                    System.currentTimeMillis() - startTime;

            queryHistoryService.record(
                    question,
                    sql,
                    "READ",
                    false,
                    executionTime
            );

            throw exception;
        }
    }
}