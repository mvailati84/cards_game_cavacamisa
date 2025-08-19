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

### Development Environment (with Live Reloading)

1. **Start development services:**
   ```bash
   docker-compose -f docker-compose.dev.yml up --build
   ```

2. **Access the application:**
   - Frontend: http://localhost:3000 (with live reloading)
   - Backend API: http://localhost:8080

3. **Development features:**
   - Live code reloading on file changes
   - Hot Module Replacement (HMR)
   - Volume mounting for instant file sync
   - Development-specific Vite configuration

### Production Environment

1. **Start production services:**
   ```bash
   docker-compose -f docker-compose.prod.yml up --build
   ```

2. **Access the application:**
   - Frontend: http://localhost (port 80)
   - Backend API: http://localhost:8080

3. **Production features:**
   - Optimized builds
   - Nginx serving static files
   - No development dependencies
   - Restart policies for reliability

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
