# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Quick Start
- **Development environment**: `docker-compose -f docker-compose.dev.yml up --build`
- **Production environment**: `docker-compose -f docker-compose.prod.yml up --build`

### HTTPS Setup
⚠️ **SECURITY IMPORTANT**: Before running the application with HTTPS, you must generate SSL certificates:
- **Generate SSL certificates**: `bash ssl-setup.sh` (creates self-signed certificates for development)
- **SSL files**: The `ssl/` directory is ignored by git and should NEVER be committed to version control
- **Access**: Application available at `https://localhost` (HTTP redirects to HTTPS)

### Testing
- **Backend tests**: `backend/run-tests.bat` (Windows) or run `docker build -f backend/Dockerfile.test -t cavacamisa-backend-tests backend && docker run --rm cavacamisa-backend-tests`
- **Frontend development**: `cd frontend && npm run dev` (runs Vite dev server on port 3000)
- **Frontend build**: `cd frontend && npm run build`

### Backend Development
- **Maven build**: `cd backend && mvn clean install`
- **Run tests**: `cd backend && mvn test`
- **Spring Boot run**: `cd backend && mvn spring-boot:run`

## Architecture Overview

### Project Structure
This is a **monorepo** containing a full-stack card game application with three main components:
- `backend/` - Java Spring Boot API (port 8080)
- `frontend/` - React application with Vite (port 3000)  
- `nginx/` - Reverse proxy configuration

### Game Logic Architecture
The application implements "Cavacamisa" - a two-player card game with the following core components:

**Backend Game Model** (`backend/src/main/java/com/cavacamisa/model/`):
- `Game.java` - Central game state management with turn-based logic
- `Player.java` - Player state including deck and captured cards
- `Card.java` - Card entity with rank, suit, and special winning card logic
- `GameState.java` - Enum for game phases (WAITING_FOR_PLAYERS, DEALING, PLAYING, FINISHED)

**Game Flow**:
1. Game supports exactly 2 players with 20 cards each from a 40-card deck
2. Players take turns playing the top card from their deck
3. "Winning cards" (specific ranks) force the opponent to play multiple cards
4. The goal is to capture all 40 cards or eliminate the opponent's deck

**API Architecture** (`backend/src/main/java/com/cavacamisa/controller/GameController.java`):
- RESTful endpoints for game lifecycle: create, join, play, get state
- Player management: create, get, delete players
- Swagger/OpenAPI documentation available at `/swagger-ui.html`

**Frontend Architecture** (`frontend/src/`):
- `services/gameService.js` - API client for backend communication
- `components/Game.jsx` - Main game interface component
- `hooks/gameHooks.js` - Custom React hooks for game state management
- Polling-based state updates (2-second intervals)

### Containerization Strategy
- **Development**: Volume mounting for live reload, HMR WebSocket support through nginx
- **Production**: Optimized builds, nginx serving static files, restart policies
- **Reverse Proxy**: Nginx handles routing `/api/*` to backend, `/` to frontend, plus WebSocket support for Vite HMR

### Key Configuration Files
- `backend/src/main/resources/application.yml` - Spring Boot configuration
- `frontend/vite.config.js` - Vite build tool configuration with HMR setup
- `nginx/default.conf` - Reverse proxy with WebSocket support for development
- Docker configurations: `Dockerfile`, `Dockerfile.dev`, `Dockerfile.prod`, `Dockerfile.test`

### Development Workflow
1. Use development Docker Compose for live reload during development
2. Backend changes require container rebuild; frontend changes hot-reload automatically
3. Game state is managed in-memory (no persistence layer)
4. API testing available through Swagger UI in development
5. Windows-specific test runner (`run-tests.bat`) available for backend testing

### Key Implementation Details
- Frontend uses environment variable `VITE_API_URL` for API endpoint configuration
- Backend exposes health check endpoint for container orchestration
- Game logic implements turn validation and card obligation mechanics
- CORS enabled for cross-origin requests during development