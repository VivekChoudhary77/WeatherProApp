# WeatherPro - Weather Application

Technical Assessment for AI Engineer Intern Position at Product Manager Accelerator

## Project Overview

WeatherPro is a full-stack weather application that provides real-time weather information with complete CRUD functionality, location validation, and multiple API integrations. This project fulfills the requirements of Tech Assessment 1 and Tech Assessment 2 as outlined in the Product Manager Accelerator technical assessment documentation.

## Features Implemented

### Section 2.1 - CRUD Operations with Database Persistence

**CREATE**
- Users can enter a location and date range to retrieve weather data
- All information is stored in a PostgreSQL database
- Location validation ensures the location exists (supports fuzzy matching)
- Date range validation prevents invalid date selections

**READ**
- Users can view all previously requested weather information
- Complete weather history is accessible to all users
- Row level security was not implemented as per assessment guidelines

**UPDATE**
- Users can update existing weather records in the database
- Location and date range validations are applied during updates
- Only valid and coherent user input is accepted

**DELETE**
- Users can remove any weather record from the database
- Confirmation modal prevents accidental deletions

### Section 2.2 - API Integration (Optional Features Completed)

**YouTube Integration**
- Provides relevant travel videos for user-selected locations
- Videos are dynamically fetched based on location context

**Google Maps Integration**
- Displays map data for user-selected locations
- Handles both specific and approximate points of interest

**OpenWeatherMap Integration**
- Fetches real-time weather data including temperature, humidity, wind speed
- Provides 5-day weather forecast for any location
- Includes Air Quality Index (AQI) information

### Section 2.3 - Data Export (Optional Features Completed)

Users can export weather data from the database in multiple formats:
- JSON (JavaScript Object Notation)
- XML (Extensible Markup Language)
- CSV (Comma Separated Values)
- PDF (Portable Document Format)
- Markdown (formatted text documentation)

### Tech Assessment 1 - Core Weather App Features

**Location Input Support**
- ZIP Code and Postal Code (US, UK, Canadian, Indian PIN codes)
- GPS Coordinates (decimal format with optional direction indicators)
- City names (with fuzzy matching for typos)
- Landmarks (famous locations and points of interest)

**Current Weather Display**
- Temperature, humidity, wind speed, and weather conditions
- Atmospheric pressure and visibility
- Sunrise and sunset times
- Air Quality Index with categorization

**5-Day Forecast**
- Extended weather predictions with daily summaries
- High and low temperatures for each day
- Weather conditions and precipitation probability

**Current Location Detection**
- Uses browser Geolocation API to detect user location
- Automatic weather fetch based on GPS coordinates

**Visual Design**
- Modern, responsive user interface built with Tailwind CSS
- Weather icons and intuitive navigation
- Clean and accessible design standards

## Technology Stack

### Backend
- **Framework**: Java Spring Boot 3.2.0
- **Database**: Supabase PostgreSQL
- **Build Tool**: Maven
- **ORM**: Spring Data JPA with Hibernate
- **Key Libraries**:
  - Spring WebFlux for external API calls
  - Apache Commons Text for fuzzy location matching
  - OpenCSV for CSV export
  - Jackson XML for XML export
  - iText7 for PDF generation
  - Lombok for reducing boilerplate code

### Frontend
- **Framework**: React 18 with TypeScript
- **Build Tool**: Vite
- **Styling**: Tailwind CSS
- **HTTP Client**: Axios
- **UI Libraries**:
  - Lucide React for icons
  - React Hot Toast for notifications
  - Date-fns for date manipulation

### External APIs Integrated
- OpenWeatherMap API for weather data and geolocation
- YouTube Data API v3 for location-based videos
- Google Maps Geocoding API for landmark search

## Prerequisites

Before running this application, ensure you have:

- Java JDK 17 or higher installed
- Node.js 18 or higher with npm
- Maven build tool
- Supabase account (free tier is sufficient)
- API keys for:
  - OpenWeatherMap API (free tier available)
  - YouTube Data API v3 (optional, available through Google Cloud Console)
  - Google Maps Geocoding API (optional, available through Google Cloud Console)

## Installation and Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/VivekChoudhary77/WeatherProApp.git
cd WeatherPro
```

### Step 2: Database Setup

1. Create a free account at Supabase (https://supabase.com)
2. Create a new project with a name of your choice
3. Navigate to the SQL Editor in the Supabase dashboard
4. Execute the SQL schema provided in `database-schema.sql` to create the required tables:
   - `locations` table for storing location information
   - `weather_records` table for storing weather data

5. Obtain your database credentials:
   - Go to Project Settings, then Database section
   - Under Connection String, select the "Connection Pooling" tab
   - Copy the connection string in Session or Transaction mode
   - Note your database password

### Step 3: Backend Configuration and Startup

1. Navigate to the backend directory:

```bash
cd weatherpro-backend
```

2. Update the configuration file `src/main/resources/application.properties` with your credentials:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://your-supabase-host:5432/postgres?user=postgres.xxxxx&password=YourPassword&sslmode=require

# API Keys
openweather.api.key=your-openweathermap-api-key
youtube.api.key=your-youtube-api-key
google.maps.api.key=your-google-maps-api-key

# CORS Configuration
cors.allowed-origins=http://localhost:5173
```

3. Build and run the backend application:

```bash
mvn clean install
mvn spring-boot:run
```

The backend server will start on http://localhost:8080 with the API accessible at http://localhost:8080/api

### Step 4: Frontend Configuration and Startup

1. Navigate to the frontend directory:

```bash
cd ../weatherpro-frontend
```

2. Install required dependencies:

```bash
npm install
```

3. Create a `.env` file in the frontend root directory:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

4. Start the development server:

```bash
npm run dev
```

The frontend application will start on http://localhost:5173

### Step 5: Access the Application

Open your web browser and navigate to http://localhost:5173 to access the WeatherPro application.

## Application Usage Guide

### Creating Weather Records

1. Enter a location in the search field. Supported formats include:
   - City name (e.g., "New York", "London")
   - ZIP or postal code (e.g., "10001", "110001")
   - GPS coordinates (e.g., "40.7128,-74.0060")
   - Landmarks (e.g., "Eiffel Tower")

2. Select the start date and end date for the weather data range

3. Click the "Search Weather" button

4. The application will validate the location and date range, then fetch and store the weather data

### Viewing Weather History

All created weather records are displayed in the Weather History table. Each record shows:
- Location name and coordinates
- Date range
- Weather conditions and temperature
- Humidity, wind speed, and atmospheric pressure
- Air Quality Index (AQI) with category

### Updating Records

1. Locate the record you want to update in the Weather History table
2. Click the Edit icon on that record
3. Modify the location or date range as needed
4. Click the Save icon to update the record

The application will re-validate the data before updating.

### Deleting Records

1. Locate the record you want to delete in the Weather History table
2. Click the Delete icon
3. Confirm the deletion in the modal dialog that appears

### Exporting Data

1. Navigate to the Export Data section
2. Select your preferred export format:
   - JSON for structured data interchange
   - CSV for spreadsheet applications
   - XML for data exchange
   - PDF for printable reports
   - Markdown for documentation

3. The file will be downloaded automatically to your browser's download folder

### Viewing Weather Forecast

After searching for a location, the application automatically displays:
- 5-day weather forecast
- Daily temperature highs and lows
- Weather conditions for each day
- Humidity, wind speed, and precipitation probability

### Watching Travel Videos

The application fetches and displays relevant YouTube videos for selected locations. Click on any video thumbnail to watch it on YouTube.

## Configuration

### Backend Configuration

The backend configuration is managed through `weatherpro-backend/src/main/resources/application.properties`. Key configurations include:

- Server port (default is 8080)
- Database connection parameters (URL, username, password)
- HikariCP connection pool settings
- External API keys (OpenWeatherMap, YouTube, Google Maps)
- CORS allowed origins
- JPA and Hibernate settings
- Logging levels

### Frontend Configuration

The frontend configuration is managed through `weatherpro-frontend/.env`. Configuration options include:

- `VITE_API_BASE_URL`: Backend API endpoint URL

## API Documentation

### Weather Operations

**Create Weather Record**
- Endpoint: `POST /api/weather`
- Creates a new weather record with location and date range validation

**Get All Records**
- Endpoint: `GET /api/weather`
- Retrieves all weather records from the database

**Get Record by ID**
- Endpoint: `GET /api/weather/{id}`
- Retrieves a specific weather record by its UUID

**Update Record**
- Endpoint: `PUT /api/weather/{id}`
- Updates an existing weather record with validation

**Delete Record**
- Endpoint: `DELETE /api/weather/{id}`
- Removes a weather record from the database

**Search by Location**
- Endpoint: `GET /api/weather/search?location={location}`
- Searches for weather records by location name

**Get Current Weather**
- Endpoint: `GET /api/weather/current?location={location}`
- Fetches current weather data for a location

**Get Forecast**
- Endpoint: `GET /api/weather/forecast?location={location}`
- Retrieves 5-day weather forecast for a location

### Integration APIs

**YouTube Videos**
- Endpoint: `GET /api/integration/youtube?location={location}`
- Fetches relevant YouTube videos for a location

**Maps Information**
- Endpoint: `GET /api/integration/maps?location={location}`
- Retrieves Google Maps data for a location

### Export APIs

**Export to JSON**
- Endpoint: `GET /api/export/json`
- Downloads all weather data in JSON format

**Export to CSV**
- Endpoint: `GET /api/export/csv`
- Downloads all weather data in CSV format

**Export to XML**
- Endpoint: `GET /api/export/xml`
- Downloads all weather data in XML format

**Export to Markdown**
- Endpoint: `GET /api/export/markdown`
- Downloads all weather data in Markdown format

**Export to PDF**
- Endpoint: `GET /api/export/pdf`
- Downloads all weather data in PDF format

## Testing

### Backend Tests

To run the backend test suite:

```bash
cd weatherpro-backend
mvn test
```

### Frontend Tests

To run the frontend test suite:

```bash
cd weatherpro-frontend
npm run test
```

## Building for Production

### Backend Production Build

To build the backend for production deployment:

```bash
cd weatherpro-backend
mvn clean package
```

This creates a JAR file at `target/weatherpro-backend-1.0.0.jar` which can be deployed to any Java application server.

To run the production JAR locally:

```bash
java -jar target/weatherpro-backend-1.0.0.jar
```

### Frontend Production Build

To build the frontend for production deployment:

```bash
cd weatherpro-frontend
npm run build
```

The production-ready files will be generated in the `dist` folder, which can be deployed to static hosting services like Vercel, Netlify, or AWS S3.

## Deployment

### Backend Deployment

The backend can be deployed to various platforms:

**Render.com** (Recommended)
- Supports Docker deployment with free tier
- Automatic builds from Git repository
- Simple environment variable management

**Railway**
- Easy Java application deployment
- Built-in PostgreSQL database support

**Heroku**
- Traditional Java deployment platform
- Simple scaling options

**AWS EC2**
- Full server control and customization
- Requires manual server configuration

For detailed backend deployment instructions, refer to `RENDER_DEPLOYMENT.md`.

### Frontend Deployment

The frontend can be deployed to static hosting platforms:

**Vercel** (Recommended)
- Automatic deployments from Git
- Built-in CI/CD pipeline
- Excellent performance and CDN


### Database Hosting

This application uses Supabase for database hosting, which is already cloud-hosted. When deploying to production:

- Ensure the connection string in your backend configuration points to your Supabase instance
- Update CORS settings to allow requests from your deployed frontend URL
- Use environment variables for sensitive database credentials

## Environment Variables

### Backend Environment Variables

For production deployment, configure the following environment variables:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://your-host:5432/postgres?user=postgres.xxxxx&password=YourPassword&sslmode=require
OPENWEATHER_API_KEY=your-openweathermap-api-key
YOUTUBE_API_KEY=your-youtube-api-key
GOOGLE_MAPS_API_KEY=your-google-maps-api-key
CORS_ORIGINS=https://your-frontend-domain.com
SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=3
SPRING_DATASOURCE_HIKARI_CONNECTION_TIMEOUT=60000
SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE=1
```

### Frontend Environment Variables

For production deployment, configure the following environment variables:

```
VITE_API_BASE_URL=https://your-backend-domain.com/api
```

## Troubleshooting

### Backend Application Won't Start

- Verify Java JDK 17 or higher is installed by running `java -version`
- Check that the database connection URL in `application.properties` is correct
- Ensure your Supabase project is active and accessible
- Verify Maven is installed correctly by running `mvn -version`
- Check that all required API keys are configured

### Frontend Application Won't Start

- Verify Node.js 18 or higher is installed by running `node --version`
- Delete the `node_modules` folder and run `npm install` again
- Ensure the `.env` file exists with the correct `VITE_API_BASE_URL`
- Clear the npm cache with `npm cache clean --force` if issues persist

### CORS Errors

- Verify that the backend CORS configuration includes your frontend URL
- Check the `cors.allowed-origins` property in `application.properties`
- Ensure both HTTP and HTTPS protocols are specified if needed
- For production, update CORS to allow your deployed frontend domain

### Database Connection Issues

- Verify Supabase project is active (not paused)
- Check that SSL mode is enabled in the connection string (`sslmode=require`)
- Ensure the connection pool size is appropriate for your tier (Supabase free tier has limits)
- Verify database credentials are correct and URL-encoded if they contain special characters

### API Key Issues

- Verify all API keys are valid and active
- Check that API keys have the necessary permissions enabled
- OpenWeatherMap API keys may take a few hours to activate after creation
- Ensure API quotas have not been exceeded

For more detailed troubleshooting, refer to the additional documentation files in the project repository.

## Project Structure

```
WeatherPro/
├── weatherpro-backend/              # Spring Boot Backend Application
│   ├── src/main/java/com/weatherpro/
│   │   ├── config/                  # Configuration classes (CORS, WebClient)
│   │   ├── controller/              # REST API Controllers
│   │   ├── service/                 # Business logic layer
│   │   ├── repository/              # JPA data access layer
│   │   ├── model/                   # JPA entity classes
│   │   ├── dto/                     # Data Transfer Objects
│   │   └── exception/               # Exception handling
│   ├── src/main/resources/
│   │   ├── application.properties   # Main configuration file
│   │   └── application-dev.properties  # Development configuration
│   ├── pom.xml                      # Maven dependencies and build config
│   ├── Dockerfile                   # Docker configuration for deployment
│   └── .dockerignore                # Docker build exclusions
│
├── weatherpro-frontend/             # React Frontend Application
│   ├── src/
│   │   ├── components/              # Reusable React components
│   │   ├── services/                # API integration services
│   │   ├── types/                   # TypeScript type definitions
│   │   ├── utils/                   # Helper and utility functions
│   │   ├── App.tsx                  # Main application component
│   │   ├── main.tsx                 # Application entry point
│   │   └── index.css                # Global styles
│   ├── package.json                 # npm dependencies
│   ├── vite.config.ts               # Vite build configuration
│   ├── tailwind.config.js           # Tailwind CSS configuration
│   ├── vercel.json                  # Vercel deployment configuration
│   └── .env                         # Environment variables (local)
│
├── database-schema.sql              # PostgreSQL database schema
├── deploy-to-render.sh              # Automated deployment script
├── README.md                        # This documentation file
├── RENDER_DEPLOYMENT.md             # Backend deployment guide
├── VERCEL_DEPLOYMENT.md             # Frontend deployment guide
└── .gitignore                       # Git ignore configuration
```

## Key Features Explained

### Location Validation with Fuzzy Matching

The application implements fuzzy matching using the Levenshtein distance algorithm to handle typos and variations in location names. This ensures that users can successfully search even with minor spelling errors. Examples:

- "New Yrok" is corrected to "New York"
- "Parris" is corrected to "Paris"
- "Londn" is corrected to "London"

The fuzzy matching threshold is configured to balance between flexibility and accuracy.

### Date Range Validation

The application enforces the following date validation rules:

- Start date must be before or equal to the end date
- Date ranges cannot extend more than 1 year into the past
- Future dates are limited to 16 days ahead (due to OpenWeatherMap API limitations)
- Invalid date selections are prevented with clear error messages

### Supported Location Input Formats

The application accepts multiple location input formats to provide flexibility:

- **City names**: New York, London, Tokyo, Mumbai
- **ZIP and Postal codes**: 10001 (US), 110001 (India), SW1A 1AA (UK)
- **GPS Coordinates**: 40.7128,-74.0060 or 40.7128° N, 74.0060° W
- **Landmarks**: Eiffel Tower, Statue of Liberty, Taj Mahal

### Duplicate Record Prevention

The application prevents duplicate weather records for the same location and overlapping date ranges. When attempting to create a duplicate record, the user receives a clear error message explaining the conflict.

### Air Quality Index Integration

Weather records include Air Quality Index (AQI) data from OpenWeatherMap, categorized as:
- Good (AQI 1)
- Fair (AQI 2)
- Moderate (AQI 3)
- Poor (AQI 4)
- Very Poor (AQI 5)

## About the Developer

**Vivek Choudhary**

This project was developed as part of the technical assessment for the AI Engineer Intern position at Product Manager Accelerator. The implementation demonstrates full-stack development capabilities, API integration, database management, and deployment to production environments.

## About Product Manager Accelerator

Product Manager Accelerator is a premier accelerator program designed to support product managers and professionals in accelerating their careers and product development skills. For more information, visit their LinkedIn page at https://www.linkedin.com/company/productmanageraccelerator/

## Live Deployment

- **Frontend**: https://weather-proapp.vercel.app
- **Backend API**: https://weatherpro-backend-latest.onrender.com/api

## References and Documentation

- OpenWeatherMap API Documentation: https://openweathermap.org/api
- Supabase Documentation: https://supabase.com/docs
- Spring Boot Documentation: https://spring.io/projects/spring-boot
- React Documentation: https://react.dev

## License

This project was developed as part of the Product Manager Accelerator technical assessment and is intended for evaluation purposes.

## Acknowledgments

This project was made possible through the use of several open-source technologies and free-tier services:

- Product Manager Accelerator for providing the opportunity and assessment framework
- OpenWeatherMap for providing the weather data API
- Supabase for PostgreSQL database hosting
- YouTube Data API for video integration
- Google Maps API for geolocation services
- All open-source libraries and frameworks used in this project

## Contact

For questions or feedback regarding this project, please reach out through the Product Manager Accelerator program channels.

