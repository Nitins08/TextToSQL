package com.nitin.texttosql.model;

public class ColumnInfo {

    private String tableName;
    private String columnName;
    private String dataType;

    public ColumnInfo(String tableName, String columnName, String dataType) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.dataType = dataType;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getDataType() {
        return dataType;
    }
}