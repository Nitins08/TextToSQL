package com.nitin.texttosql.service;

import com.nitin.texttosql.model.ColumnInfo;
import com.nitin.texttosql.model.RelationshipInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SchemaService {

    private final JdbcTemplate jdbcTemplate;

    public SchemaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getTables() {

        String sql = """
                SELECT TABLE_NAME
                FROM INFORMATION_SCHEMA.TABLES
                WHERE TABLE_SCHEMA = 'PUBLIC'
                """;

        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getColumns() {

        String sql = """
                SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE
                FROM INFORMATION_SCHEMA.COLUMNS
                WHERE TABLE_SCHEMA = 'PUBLIC'
                ORDER BY TABLE_NAME, ORDINAL_POSITION
                """;

        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getRelationships() {

        String sql = """
                SELECT
                    fk.TABLE_NAME AS FK_TABLE,
                    fk.COLUMN_NAME AS FK_COLUMN,
                    pk.TABLE_NAME AS REFERENCED_TABLE,
                    pk.COLUMN_NAME AS REFERENCED_COLUMN
                FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE fk
                JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS rc
                    ON fk.CONSTRAINT_SCHEMA = rc.CONSTRAINT_SCHEMA
                    AND fk.CONSTRAINT_NAME = rc.CONSTRAINT_NAME
                JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE pk
                    ON rc.UNIQUE_CONSTRAINT_SCHEMA = pk.CONSTRAINT_SCHEMA
                    AND rc.UNIQUE_CONSTRAINT_NAME = pk.CONSTRAINT_NAME
                    AND fk.POSITION_IN_UNIQUE_CONSTRAINT = pk.ORDINAL_POSITION
                WHERE fk.CONSTRAINT_SCHEMA = 'PUBLIC'
                ORDER BY fk.TABLE_NAME, fk.ORDINAL_POSITION
                """;

        return jdbcTemplate.queryForList(sql);
    }

    public List<ColumnInfo> getColumnInfo() {

        String sql = """
                SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE
                FROM INFORMATION_SCHEMA.COLUMNS
                WHERE TABLE_SCHEMA = 'PUBLIC'
                ORDER BY TABLE_NAME, ORDINAL_POSITION
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ColumnInfo(
                        rs.getString("TABLE_NAME"),
                        rs.getString("COLUMN_NAME"),
                        rs.getString("DATA_TYPE")
                )
        );
    }

    public List<RelationshipInfo> getRelationshipInfo() {

        String sql = """
                SELECT
                    fk.TABLE_NAME AS FK_TABLE,
                    fk.COLUMN_NAME AS FK_COLUMN,
                    pk.TABLE_NAME AS REFERENCED_TABLE,
                    pk.COLUMN_NAME AS REFERENCED_COLUMN
                FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE fk
                JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS rc
                    ON fk.CONSTRAINT_SCHEMA = rc.CONSTRAINT_SCHEMA
                    AND fk.CONSTRAINT_NAME = rc.CONSTRAINT_NAME
                JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE pk
                    ON rc.UNIQUE_CONSTRAINT_SCHEMA = pk.CONSTRAINT_SCHEMA
                    AND rc.UNIQUE_CONSTRAINT_NAME = pk.CONSTRAINT_NAME
                    AND fk.POSITION_IN_UNIQUE_CONSTRAINT = pk.ORDINAL_POSITION
                WHERE fk.CONSTRAINT_SCHEMA = 'PUBLIC'
                ORDER BY fk.TABLE_NAME, fk.ORDINAL_POSITION
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new RelationshipInfo(
                        rs.getString("FK_TABLE"),
                        rs.getString("FK_COLUMN"),
                        rs.getString("REFERENCED_TABLE"),
                        rs.getString("REFERENCED_COLUMN")
                )
        );
    }

    public String getSchemaSummary() {

        StringBuilder schema = new StringBuilder();

        List<Map<String, Object>> tables = getTables();
        List<ColumnInfo> columns = getColumnInfo();
        List<RelationshipInfo> relationships = getRelationshipInfo();

        schema.append("DATABASE SCHEMA\n\n");

        for (Map<String, Object> table : tables) {

            String tableName = (String) table.get("TABLE_NAME");

            schema.append("TABLE: ")
                    .append(tableName)
                    .append("\n");

            for (ColumnInfo column : columns) {

                if (column.getTableName().equals(tableName)) {

                    schema.append("  - ")
                            .append(column.getColumnName())
                            .append(" ")
                            .append(column.getDataType())
                            .append("\n");
                }
            }

            schema.append("\n");
        }

        schema.append("RELATIONSHIPS:\n");

        for (RelationshipInfo relationship : relationships) {

            schema.append("  - ")
                    .append(relationship.getForeignKeyTable())
                    .append(".")
                    .append(relationship.getForeignKeyColumn())
                    .append(" -> ")
                    .append(relationship.getReferencedTable())
                    .append(".")
                    .append(relationship.getReferencedColumn())
                    .append("\n");
        }

        return schema.toString();
    }
}