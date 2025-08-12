import { useState, useEffect } from 'react'
import './App.css'

function App() {
  const [backendStatus, setBackendStatus] = useState('checking')
  const [backendMessage, setBackendMessage] = useState('')

  useEffect(() => {
    const checkBackendHealth = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/health')
        if (response.ok) {
          const data = await response.json()
          setBackendStatus('connected')
          setBackendMessage(data.message)
        } else {
          setBackendStatus('error')
          setBackendMessage('Backend is not responding')
        }
      } catch (error) {
        setBackendStatus('error')
        setBackendMessage('Cannot connect to backend')
      }
    }

    checkBackendHealth()
  }, [])

  return (
    <div className="app">
      <header className="app-header">
        <h1>🃏 Cavacamisa Cards Game</h1>
        <div className="status-container">
          <div className="status-item">
            <span className="status-label">Frontend:</span>
            <span className="status-value status-success">✅ Ready</span>
          </div>
          <div className="status-item">
            <span className="status-label">Backend:</span>
            <span className={`status-value ${
              backendStatus === 'connected' ? 'status-success' : 
              backendStatus === 'error' ? 'status-error' : 'status-checking'
            }`}>
              {backendStatus === 'connected' ? '✅ Connected' :
               backendStatus === 'error' ? '❌ Error' : '⏳ Checking...'}
            </span>
          </div>
        </div>
        {backendMessage && (
          <p className="backend-message">{backendMessage}</p>
        )}
        {(backendStatus === 'connected') && (
          <div className="app-info">
            <p>🎮 The Cavacamisa Cards Game application is ready!</p>
            <p>Both frontend and backend services are running successfully.</p>
          </div>
        )}
      </header>
    </div>
  )
}

export default App
