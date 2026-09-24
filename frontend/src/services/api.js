const API_BASE_URL = ""

export async function askDatabase(question) {
  const response = await fetch(`${API_BASE_URL}/api/sql/ask`, {
    method: "POST",
    headers: {
      "Content-Type": "text/plain",
    },
    body: question,
  })

  const data = await response.json()

  if (!response.ok) {
    throw new Error(data.message || "Request failed")
  }

  return data
}

export async function getQueryHistory() {
  const response = await fetch(`${API_BASE_URL}/api/query/history`)

  const data = await response.json()

  if (!response.ok) {
    throw new Error("Failed to load query history")
  }

  return data
}

export async function getSchema() {
  const response = await fetch("/api/schema")

  const data = await response.json()

  if (!response.ok) {
    throw new Error("Failed to load database schema")
  }

  return data
}

export async function getSchemaRelationships() {
  const response = await fetch("/api/schema/relationships")

  const data = await response.json()

  if (!response.ok) {
    throw new Error("Failed to load schema relationships")
  }

  return data
}