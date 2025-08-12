import './StatusSummary.css'

const StatusSummary = ({ backendStatus, backendMessage }) => {
  const getStatusContent = () => {
    switch (backendStatus) {
      case 'connected':
        return {
          icon: '✅',
          ariaLabel: 'success',
          message: (
            <>
              The Cavacamisa Cards Game application is ready!<br />
              Both frontend and backend services are running successfully.
            </>
          )
        }
      case 'error':
        return {
          icon: '❌',
          ariaLabel: 'error',
          message: backendMessage || 'Backend is not responding'
        }
      default:
        return {
          icon: '⏳',
          ariaLabel: 'checking',
          message: 'Checking backend status...'
        }
    }
  }

  const statusContent = getStatusContent()

  return (
    <div className={`status-summary-box status-${backendStatus}`}>
      <span role="img" aria-label={statusContent.ariaLabel}>
        {statusContent.icon}
      </span>{' '}
      {statusContent.message}
    </div>
  )
}

export default StatusSummary
