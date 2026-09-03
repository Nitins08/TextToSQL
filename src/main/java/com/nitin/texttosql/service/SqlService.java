package com.nitin.texttosql.service;

import com.nitin.texttosql.ai.SqlGenerator;
import org.springframework.stereotype.Service;

@Service
public class SqlService {

    private final SqlGenerator sqlGenerator;
    private final SchemaService schemaService;

    public SqlService(SqlGenerator sqlGenerator,
                      SchemaService schemaService) {

        this.sqlGenerator = sqlGenerator;
        this.schemaService = schemaService;
    }

    public String generateSql(String question) {

        // Get the current database schema
        String schema = schemaService.getSchemaSummary();

        // Send schema + user's question to Qwen
        return sqlGenerator.generateSql(schema, question);
    }
}