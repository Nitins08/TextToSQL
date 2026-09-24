function SchemaViewer({ schema }) {
  if (!schema || schema.length === 0) {
    return <p>No schema information available.</p>
  }

  return (
    <div className="schema-viewer">
      {schema.map((table) => (
        <div className="schema-table" key={table.TABLE_NAME}>
          <div className="schema-table-header">
            <h3>{table.TABLE_NAME}</h3>
            <span>TABLE</span>
          </div>

          <div className="schema-columns">
            {table.COLUMNS?.map((column) => (
              <div className="schema-column" key={column.COLUMN_NAME}>
                <span className="column-name">
                  {column.COLUMN_NAME}
                </span>

                <span className="column-type">
                  {column.TYPE_NAME}
                </span>
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  )
}

export default SchemaViewer