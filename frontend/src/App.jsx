import { useEffect, useState } from "react"
import {
  askDatabase,
  getQueryHistory,
  getSchema,
  getSchemaRelationships
} from "./services/api"

import QueryInput from "./components/QueryInput"
import QueryResult from "./components/QueryResult"
import HistoryTable from "./components/HistoryTable"
import SchemaViewer from "./components/SchemaViewer"

function App() {
  const [question, setQuestion] = useState("")
  const [result, setResult] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState("")
  const [history, setHistory] = useState([])
  const [schema, setSchema] = useState([])
  const [relationships, setRelationships] = useState([])

  useEffect(() => {
    async function loadData() {
      try {
        const historyData = await getQueryHistory()
        setHistory(historyData)

        const schemaData = await getSchema()
        setSchema(schemaData)

        const relationshipData = await getSchemaRelationships()
        setRelationships(relationshipData)
      } catch (err) {
        console.error("Failed to load data:", err)
      }
    }

    loadData()
  }, [])

  async function handleSubmit() {
    setLoading(true)
    setError("")
    setResult(null)

    try {
      const data = await askDatabase(question)

      setResult(data)

      const updatedHistory = await getQueryHistory()
      setHistory(updatedHistory)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  async function handleRefreshHistory() {
    try {
      const updatedHistory = await getQueryHistory()
      setHistory(updatedHistory)
    } catch (err) {
      console.error("Failed to refresh history:", err)
    }
  }

  function handleSelectQuestion(selectedQuestion) {
    setQuestion(selectedQuestion)

    window.scrollTo({
      top: 0,
      behavior: "smooth"
    })
  }

  function handleClear() {
    setQuestion("")
    setResult(null)
    setError("")
  }

  function getHistoryStats() {
    const total = history.length

    const successful = history.filter(
      (item) => item.successful
    ).length

    const failed = history.filter(
      (item) => !item.successful
    ).length

    const reads = history.filter(
      (item) => item.operationType === "READ"
    ).length

    const writes = history.filter(
      (item) => item.operationType === "WRITE"
    ).length

    const successRate =
      total === 0
        ? 0
        : Math.round((successful / total) * 100)

    return {
      total,
      successful,
      failed,
      reads,
      writes,
      successRate
    }
  }

  const stats = getHistoryStats()

  return (
    <div className="app">

      <header className="header">
        <h1>Text-to-SQL Analytics Engine</h1>

        <p>
          Ask questions about your database using natural language.
        </p>
      </header>

      <main className="dashboard">

        <section className="query-card">
          <h2>Ask Your Database</h2>

          <QueryInput
            question={question}
            setQuestion={setQuestion}
            onSubmit={handleSubmit}
            loading={loading}
            onClear={handleClear}
          />

          {loading && (
            <div className="loading">
              AI is generating and executing your SQL query...
            </div>
          )}

          {error && (
            <div className="error">
              {error}
            </div>
          )}
        </section>

        {result && (
          <section className="result-card">
            <QueryResult result={result} />
          </section>
        )}

        <section className="stats-grid">

          <div className="stat-card">
            <span className="stat-label">Total Queries</span>
            <span className="stat-value">{stats.total}</span>
          </div>

          <div className="stat-card">
            <span className="stat-label">Successful</span>
            <span className="stat-value">{stats.successful}</span>
          </div>

          <div className="stat-card">
            <span className="stat-label">Failed</span>
            <span className="stat-value">{stats.failed}</span>
          </div>

          <div className="stat-card">
            <span className="stat-label">Read Queries</span>
            <span className="stat-value">{stats.reads}</span>
          </div>

          <div className="stat-card">
            <span className="stat-label">Write Queries</span>
            <span className="stat-value">{stats.writes}</span>
          </div>

          <div className="stat-card">
            <span className="stat-label">Success Rate</span>
            <span className="stat-value">{stats.successRate}%</span>
          </div>

        </section>

        <section className="query-card">

          <div className="section-header">
            <h2>Query History</h2>

            <button
              className="refresh-button"
              onClick={handleRefreshHistory}
            >
              Refresh
            </button>
          </div>

          <HistoryTable
            history={history}
            onSelectQuestion={handleSelectQuestion}
          />

        </section>

        <section className="query-card">

          <h2>Database Schema</h2>

          <SchemaViewer schema={schema} />

          {relationships && relationships.length > 0 && (
            <div className="relationships-section">

              <h3>Table Relationships</h3>

              <div className="relationships-list">

                {relationships.map((relationship, index) => (
                  <div
                    className="relationship-card"
                    key={index}
                  >
                    <div className="relationship-table">
                      {relationship.TABLE_NAME}
                    </div>

                    <div className="relationship-arrow">
                      →
                    </div>

                    <div className="relationship-table">
                      {relationship.REFERENCED_TABLE_NAME}
                    </div>

                    <div className="relationship-details">
                      {relationship.COLUMN_NAME}
                      {" → "}
                      {relationship.REFERENCED_COLUMN_NAME}
                    </div>
                  </div>
                ))}

              </div>

            </div>
          )}

        </section>

      </main>

    </div>
  )
}

export default App