# Cavacamisa Cards Game

A monorepo containing the backend and frontend for the Cavacamisa cards game.

## Project Structure

```
cards_game_camicia/
├── backend/          # Java Spring Boot API
├── frontend/         # React web application
├── docker-compose.yml # Orchestration for all services
└── README.md         # This file
```

## Quick Start

1. **Start all services:**
   ```bash
   docker-compose up --build
   ```

2. **Access the application:**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080

## Development

### Backend (Java Spring Boot)
- Port: 8080
- Framework: Spring Boot 3.x
- Java: 24

### Frontend (React)
- Port: 3000
- Framework: React 18
- Build tool: Vite

## Services

- **Frontend**: React application serving the web interface
- **Backend**: Spring Boot API providing game logic and data
