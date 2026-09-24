import { useState } from "react"

function SqlViewer({ sql }) {
  const [copied, setCopied] = useState(false)

  if (!sql) {
    return null
  }

  async function copySql() {
    await navigator.clipboard.writeText(sql)
    setCopied(true)

    setTimeout(() => {
      setCopied(false)
    }, 2000)
  }

  return (
    <div className="sql-viewer">
      <div className="sql-header">
        <h3>Generated SQL</h3>

        <div className="sql-actions">
          <span>SQL</span>

          <button onClick={copySql}>
            {copied ? "Copied!" : "Copy SQL"}
          </button>
        </div>
      </div>

      <pre>{sql}</pre>
    </div>
  )
}

export default SqlViewer