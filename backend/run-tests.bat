@echo off
echo Running Cavacamisa Backend Tests...
echo.

echo Building test image...
docker build -f Dockerfile.test -t cavacamisa-backend-tests .

echo.
echo Running tests...
docker run --rm cavacamisa-backend-tests

echo.
echo Tests completed!
pause
