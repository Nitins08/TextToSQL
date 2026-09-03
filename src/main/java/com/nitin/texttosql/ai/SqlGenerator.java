package com.nitin.texttosql.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService(chatModel = "ollamaChatModel")
public interface SqlGenerator {

    @SystemMessage("""
            You are an expert SQL query generator.

            Your job is to convert natural language questions into SQL queries.

            Rules:
            1. Generate SQL for H2 Database.
            2. Use only tables and columns provided in the database schema.
            3. Do not invent tables or columns.
            4. Return ONLY the SQL query.
            5. Do NOT use Markdown code blocks.
            6. Do NOT add explanations.
            7. For now, generate SELECT queries only.
            """)

    @UserMessage("""
            Database Schema:

            {{schema}}

            User Question:

            {{question}}

            Generate the SQL query.
            """)
    String generateSql(String schema, String question);
}