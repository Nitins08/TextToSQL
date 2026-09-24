import SqlViewer from "./SqlViewer"

function QueryResult({ result }) {
  if (!result) {
    return null
  }

  return (
    <div>
      <div className="result-header">
        <h2>Query Result</h2>

        <span
          className={`result-status ${
            result.success ? "success" : "failed"
          }`}
        >
          {result.success ? "Success" : "Failed"}
        </span>
      </div>

      <p>
        <strong>Question:</strong> {result.question}
      </p>

      <div className="result-summary">

        <div className="summary-item">
          <span className="summary-label">
            Operation
          </span>

          <span className="summary-value">
            {result.type}
          </span>
        </div>


        {result.type === "READ" && (
          <div className="summary-item">
            <span className="summary-label">
              Rows
            </span>

            <span className="summary-value">
              {result.rowCount}
            </span>
          </div>
        )}


        {result.type === "WRITE" && (
          <div className="summary-item">
            <span className="summary-label">
              Affected Rows
            </span>

            <span className="summary-value">
              {result.affectedRows}
            </span>
          </div>
        )}


        <div className="summary-item">
          <span className="summary-label">
            Execution Time
          </span>

          <span className="summary-value">
            {result.executionTimeMs} ms
          </span>
        </div>

      </div>


      <SqlViewer sql={result.sql} />


      {result.data && result.data.length > 0 && (
        <div className="data-section">

          <h3>Data</h3>

          <div className="result-table">

            <table>

              <thead>
                <tr>
                  {Object.keys(result.data[0]).map((column) => (
                    <th key={column}>
                      {column}
                    </th>
                  ))}
                </tr>
              </thead>

              <tbody>

                {result.data.map((row, index) => (
                  <tr key={index}>

                    {Object.entries(row).map(
                      ([column, value]) => (
                        <td key={column}>
                          {value === null
                            ? "NULL"
                            : String(value)}
                        </td>
                      )
                    )}

                  </tr>
                ))}

              </tbody>

            </table>

          </div>

        </div>
      )}


      {result.data &&
        result.data.length === 0 &&
        result.type === "READ" && (
          <div className="no-data">
            No rows found.
          </div>
        )}


      {result.type === "WRITE" &&
        result.success && (
          <div className="success-message">
            Write operation completed successfully.
          </div>
        )}

    </div>
  )
}

export default QueryResult