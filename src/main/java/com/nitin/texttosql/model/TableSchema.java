package com.nitin.texttosql.model;

import java.util.ArrayList;
import java.util.List;

public class TableSchema {

    private String tableName;
    private List<ColumnInfo> columns;
    private List<RelationshipInfo> relationships;

    public TableSchema(String tableName) {
        this.tableName = tableName;
        this.columns = new ArrayList<>();
        this.relationships = new ArrayList<>();
    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public List<RelationshipInfo> getRelationships() {
        return relationships;
    }

    public void addColumn(ColumnInfo column) {
        columns.add(column);
    }

    public void addRelationship(RelationshipInfo relationship) {
        relationships.add(relationship);
    }
}