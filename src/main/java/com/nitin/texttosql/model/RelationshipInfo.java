package com.nitin.texttosql.model;

public class RelationshipInfo {

    private String foreignKeyTable;
    private String foreignKeyColumn;
    private String referencedTable;
    private String referencedColumn;

    public RelationshipInfo(
            String foreignKeyTable,
            String foreignKeyColumn,
            String referencedTable,
            String referencedColumn) {

        this.foreignKeyTable = foreignKeyTable;
        this.foreignKeyColumn = foreignKeyColumn;
        this.referencedTable = referencedTable;
        this.referencedColumn = referencedColumn;
    }

    public String getForeignKeyTable() {
        return foreignKeyTable;
    }

    public String getForeignKeyColumn() {
        return foreignKeyColumn;
    }

    public String getReferencedTable() {
        return referencedTable;
    }

    public String getReferencedColumn() {
        return referencedColumn;
    }
}