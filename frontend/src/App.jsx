import { useState, useEffect } from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import './App.css'
import StatusSummary from './components/StatusSummary'
import Game from './components/Game'

function App() {
  const [backendStatus, setBackendStatus] = useState('checking')
  const [backendMessage, setBackendMessage] = useState('')

  useEffect(() => {
      let intervalId;
      let stopped = false;

      const checkBackendHealth = async () => {
        try {
          const apiUrl = import.meta.env.VITE_API_URL;
          const response = await fetch(`${apiUrl}/health`);
          if (response.ok) {
            const data = await response.json();
            setBackendStatus('connected');
            setBackendMessage(data.message);
            stopped = true;
            clearInterval(intervalId);
          } else {
            setBackendStatus('error');
            setBackendMessage('Backend is not responding');
          }
        } catch (error) {
          setBackendStatus('error');
          setBackendMessage('Cannot connect to backend');
        }
      };

      // First check immediately
      checkBackendHealth();

      // Then retry every 2 seconds if not connected
      intervalId = setInterval(() => {
        if (!stopped && backendStatus !== 'connected') {
          checkBackendHealth();
        }
      }, 2000);

      // Cleanup on unmount
      return () => clearInterval(intervalId);
    }, []);

  return (
    <Router>
      <Routes>
        <Route path="/" element={
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
                    {backendStatus === 'connected' ? '✅ Ready' :
                     backendStatus === 'error' ? '❌ Error' : '⏳ Checking...'}
                  </span>
                </div>
              </div>
              <StatusSummary 
                backendStatus={backendStatus} 
                backendMessage={backendMessage} 
              />
            </header>
          </div>
        } />
        <Route path="/game" element={<Game />} />
      </Routes>
    </Router>
  );
}

export default App
