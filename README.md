# Marine Vessels Management System

A comprehensive Spring Boot application for managing marine vessels in the shipping industry.

## Features

- **Vessel Management**: Create, read, update, and delete vessel records
- **Advanced Search**: Search vessels by name, type, status, flag state, and other criteria
- **Validation**: Comprehensive input validation including IMO number format validation
- **Pagination**: Support for paginated results with sorting
- **Status Tracking**: Track vessel status (Active, In Port, At Sea, Under Maintenance, etc.)
- **REST API**: Full RESTful API with Swagger documentation
- **Testing**: Comprehensive unit, integration, and repository tests

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (for development and testing)
- **JUnit 5** for testing
- **Mockito** for mocking
- **Swagger/OpenAPI 3** for API documentation
- **Maven** for dependency management

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### API Documentation

Once the application is running, you can access:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/api-docs`
- **H2 Console**: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:vesselsdb`, Username: `sa`, Password: `password`)

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=VesselServiceTest

# Run tests with coverage
mvn test jacoco:report
```

## API Endpoints

### Vessel Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/vessels` | Get all vessels (paginated) |
| GET | `/api/vessels/{id}` | Get vessel by ID |
| GET | `/api/vessels/imo/{imoNumber}` | Get vessel by IMO number |
| POST | `/api/vessels` | Create new vessel |
| PUT | `/api/vessels/{id}` | Update vessel |
| DELETE | `/api/vessels/{id}` | Delete vessel |
| PATCH | `/api/vessels/{id}/status` | Update vessel status |

### Search and Filter

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/vessels/search?name={name}` | Search vessels by name |
| GET | `/api/vessels/type/{type}` | Get vessels by type |
| GET | `/api/vessels/status/{status}` | Get vessels by status |
| GET | `/api/vessels/flag/{flagState}` | Get vessels by flag state |

### Statistics

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/vessels/statistics/count-by-type/{type}` | Get vessel count by type |

## Data Model

### Vessel Entity

```json
{
  "id": 1,
  "name": "MV Atlantic Pioneer",
  "imoNumber": "IMO9123456",
  "type": "CONTAINER_SHIP",
  "flagState": "Panama",
  "yearBuilt": 2019,
  "lengthMeters": 366.0,
  "grossTonnage": 180000.0,
  "status": "AT_SEA",
  "lastPortOfCall": "Hamburg",
  "nextPortOfCall": "Rotterdam",
  "estimatedArrival": "2024-01-15T10:30:00",
  "createdAt": "2024-01-01T12:00:00",
  "updatedAt": "2024-01-01T12:00:00"
}
```

### Vessel Types

- CARGO_SHIP
- CONTAINER_SHIP
- TANKER
- BULK_CARRIER
- PASSENGER_SHIP
- CRUISE_SHIP
- FERRY
- FISHING_VESSEL
- YACHT
- TUGBOAT
- NAVAL_VESSEL
- RESEARCH_VESSEL
- OFFSHORE_VESSEL

### Vessel Status

- ACTIVE
- IN_PORT
- AT_SEA
- UNDER_MAINTENANCE
- DECOMMISSIONED
- DETAINED

## Example Usage

### Create a Vessel

```bash
curl -X POST http://localhost:8080/api/vessels \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MV Example Ship",
    "imoNumber": "IMO1234567",
    "type": "CARGO_SHIP",
    "flagState": "Panama",
    "yearBuilt": 2020,
    "lengthMeters": 200.0,
    "grossTonnage": 50000.0,
    "status": "ACTIVE"
  }'
```

### Search Vessels

```bash
# Get all vessels with pagination
curl "http://localhost:8080/api/vessels?page=0&size=10&sortBy=name&sortDir=asc"

# Search by type
curl "http://localhost:8080/api/vessels/type/CONTAINER_SHIP"

# Search by name
curl "http://localhost:8080/api/vessels/search?name=Atlantic"
```

## Testing

The application includes comprehensive testing:

- **Unit Tests**: Service layer and individual component testing
- **Integration Tests**: Full application context testing
- **Repository Tests**: Data access layer testing
- **Controller Tests**: Web layer testing with MockMvc

Test coverage includes:
- All CRUD operations
- Custom repository queries
- Exception handling
- Input validation
- Business logic

## Configuration

Key configuration properties in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:vesselsdb
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  h2:
    console:
      enabled: true

server:
  port: 8080
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## License

This project is licensed under the MIT License.