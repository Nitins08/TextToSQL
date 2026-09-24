function QueryInput({
  question,
  setQuestion,
  onSubmit,
  loading,
  onClear
}) {
  const examples = [
    "Show all products",
    "Show all users from Chennai",
    "What is the most expensive product?",
    "Show the total number of orders"
  ]

  return (
    <div className="query-input">

      <textarea
        value={question}
        onChange={(e) => setQuestion(e.target.value)}
        placeholder="Example: Show all products"
        rows="4"
        maxLength="500"
      />

      <div className="input-info">
        <span>Ask a question about your database</span>
        <span>{question.length}/500</span>
      </div>

      <div className="example-questions">
        <span>Try:</span>

        {examples.map((example) => (
          <button
            key={example}
            className="example-button"
            onClick={() => setQuestion(example)}
            disabled={loading}
          >
            {example}
          </button>
        ))}
      </div>

      <div className="query-buttons">
        <button
          onClick={onSubmit}
          disabled={loading || !question.trim()}
        >
          {loading ? "Generating SQL..." : "Ask Database"}
        </button>

        <button
          className="clear-button"
          onClick={onClear}
          disabled={loading}
        >
          Clear
        </button>
      </div>

    </div>
  )
}

export default QueryInput