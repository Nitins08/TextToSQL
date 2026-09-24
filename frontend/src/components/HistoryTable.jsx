import { useState } from "react"

function HistoryTable({ history, onSelectQuestion }) {
  const [search, setSearch] = useState("")
  const [filter, setFilter] = useState("ALL")

  if (!history || history.length === 0) {
    return <p>No query history available.</p>
  }

  const filteredHistory = history.filter((item) => {
    const matchesSearch = item.question
      .toLowerCase()
      .includes(search.toLowerCase())

    const matchesFilter =
      filter === "ALL" ||
      item.operationType === filter

    return matchesSearch && matchesFilter
  })

  function clearFilters() {
    setSearch("")
    setFilter("ALL")
  }

  return (
    <div className="history-container">

      <div className="history-controls">

        <input
          type="text"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Search previous questions..."
        />

        <div className="history-filters">

          <button
            className={filter === "ALL" ? "active-filter" : ""}
            onClick={() => setFilter("ALL")}
          >
            All
          </button>

          <button
            className={filter === "READ" ? "active-filter" : ""}
            onClick={() => setFilter("READ")}
          >
            READ
          </button>

          <button
            className={filter === "WRITE" ? "active-filter" : ""}
            onClick={() => setFilter("WRITE")}
          >
            WRITE
          </button>

          <button
            className={filter === "INVALID" ? "active-filter" : ""}
            onClick={() => setFilter("INVALID")}
          >
            INVALID
          </button>

          <button
            className="clear-filter-button"
            onClick={clearFilters}
          >
            Clear
          </button>

        </div>

      </div>

      {filteredHistory.length === 0 ? (
        <p className="no-history-results">
          No matching questions found.
        </p>
      ) : (
        <div className="history-table">

          <table>

            <thead>
              <tr>
                <th>Question</th>
                <th>Operation</th>
                <th>Status</th>
                <th>Execution Time</th>
                <th>Timestamp</th>
              </tr>
            </thead>

            <tbody>

              {filteredHistory.map((item) => (
                <tr
                  key={item.id}
                  className="history-row"
                  onClick={() =>
                    onSelectQuestion(item.question)
                  }
                >

                  <td>{item.question}</td>

                  <td>
                    <span
                      className={`operation ${item.operationType.toLowerCase()}`}
                    >
                      {item.operationType}
                    </span>
                  </td>

                  <td>
                    <span
                      className={`status ${
                        item.successful
                          ? "success"
                          : "failed"
                      }`}
                    >
                      {item.successful
                        ? "Success"
                        : "Failed"}
                    </span>
                  </td>

                  <td>
                    {item.executionTimeMs} ms
                  </td>

                  <td>
                    {new Date(
                      item.timestamp
                    ).toLocaleDateString("en-GB")}{" "}

                    {new Date(
                      item.timestamp
                    ).toLocaleTimeString("en-GB", {
                      hour: "2-digit",
                      minute: "2-digit",
                      second: "2-digit",
                      hour12: false,
                    })}
                  </td>

                </tr>
              ))}

            </tbody>

          </table>

        </div>
      )}

    </div>
  )
}

export default HistoryTable