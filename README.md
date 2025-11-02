# WeatherPro - Weather Application

> Tech Assessment 2: AI/ML Engineer Intern Application

A full-stack weather application that allows users to manage weather records with complete CRUD operations, location validation, API integrations, and data export capabilities.

## 🎯 Features

### Mandatory Features (Tech Assessment 2 - Section 2.1)
✅ **CREATE** - Add weather records with location and date range validation  
✅ **READ** - View all weather records from the database  
✅ **UPDATE** - Edit existing weather records with validation  
✅ **DELETE** - Remove weather records from the database  
✅ **Location Validation** - Fuzzy matching for location input  
✅ **Date Range Validation** - Comprehensive date validation  

### Optional Features (Tech Assessment 2 - Sections 2.2 & 2.3)
✅ **YouTube Integration** - View travel videos for selected locations  
✅ **Google Maps Integration** - Map data and location visualization  
✅ **Data Export** - Export data in JSON, CSV, XML, PDF, and Markdown formats  

### Bonus Features
✅ **5-Day Weather Forecast** - Extended weather predictions  
✅ **Current Location Detection** - GPS-based location input  
✅ **Weather Icons & Visual Design** - Modern, responsive UI  
✅ **Real-time Weather Data** - OpenWeatherMap API integration  

## 🛠️ Tech Stack

### Backend
- **Framework**: Java Spring Boot 3.2.0
- **Database**: Supabase (PostgreSQL)
- **Build Tool**: Maven
- **Key Libraries**:
  - Spring Data JPA
  - Spring WebFlux (for API calls)
  - Apache Commons Text (fuzzy matching)
  - OpenCSV, Jackson XML (data export)

### Frontend
- **Framework**: React 18 + TypeScript
- **Build Tool**: Vite
- **Styling**: Tailwind CSS
- **Key Libraries**:
  - Axios (API calls)
  - Lucide React (icons)
  - Date-fns (date utilities)

### APIs Integrated
- OpenWeatherMap API (weather data)
- YouTube Data API (travel videos)
- Google Maps API (maps and places)

## 📋 Prerequisites

Before running this application, ensure you have:

- **Java JDK 17+** installed
- **Node.js 18+** and npm installed
- **Maven** installed
- **Supabase** account (free tier works)
- **API Keys** for:
  - OpenWeatherMap (free at https://openweathermap.org/api)
  - YouTube Data API (optional, from Google Cloud Console)
  - Google Maps API (optional, from Google Cloud Console)

## 🚀 Setup Instructions

### Step 1: Clone the Repository

```bash
cd /path/to/your/projects
git clone <your-repo-url>
cd WeatherPro
```

### Step 2: Database Setup (Supabase)

1. Create a free account at [Supabase](https://supabase.com)
2. Create a new project
3. Go to SQL Editor and run the schema:

```sql
-- Copy and paste the contents of database-schema.sql
```

4. Get your database credentials:
   - Go to Settings → Database
   - Copy the connection string (URI format)
   - Note down your password

### Step 3: Backend Setup

1. Navigate to backend directory:

```bash
cd weatherpro-backend
```

2. Create `application-dev.properties` in `src/main/resources/`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://db.xxxxx.supabase.co:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=your-supabase-password

# API Keys
openweather.api.key=your-openweathermap-api-key
youtube.api.key=your-youtube-api-key
google.maps.api.key=your-google-maps-api-key

# CORS
cors.allowed-origins=http://localhost:5173
```

3. Build and run the backend:

```bash
# Clean and build
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend should start on http://localhost:8080

### Step 4: Frontend Setup

1. Navigate to frontend directory:

```bash
cd ../weatherpro-frontend
```

2. Install dependencies:

```bash
npm install
```

3. Create `.env` file:

```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_GOOGLE_MAPS_KEY=your-google-maps-api-key
```

4. Start the development server:

```bash
npm run dev
```

The frontend should start on http://localhost:5173

### Step 5: Access the Application

Open your browser and navigate to:
```
http://localhost:5173
```

## 📱 How to Use

### Creating Weather Records
1. Enter a location (city name, ZIP code, coordinates, or landmark)
2. Select start and end dates
3. Click "Search Weather"
4. The app will validate the location and fetch weather data

### Viewing Weather History
- All created records appear in the Weather History table
- Click the info button (ℹ️) to see detailed information

### Updating Records
1. Click the Edit (✏️) icon on any record
2. Modify the location or date range
3. Click Save (✓) to update

### Deleting Records
1. Click the Delete (🗑️) icon on any record
2. Click again to confirm deletion

### Exporting Data
1. Scroll to the Export Data section
2. Click on your preferred format (JSON, CSV, XML, PDF, or Markdown)
3. The file will download automatically

### Viewing Forecast
- Select any location to see the 5-day forecast
- View temperature highs/lows, humidity, wind speed, and precipitation chance

### Watching Travel Videos
- YouTube videos automatically load for selected locations
- Click on any video thumbnail to watch

## 🔧 Configuration

### Backend Configuration
Edit `weatherpro-backend/src/main/resources/application.properties` to configure:
- Server port (default: 8080)
- Database connection
- API keys
- CORS origins
- Logging levels

### Frontend Configuration
Edit `weatherpro-frontend/.env` to configure:
- API base URL
- Google Maps API key

## 📊 API Endpoints

### Weather Operations
```
POST   /api/weather             - Create weather record
GET    /api/weather             - Get all records
GET    /api/weather/{id}        - Get record by ID
PUT    /api/weather/{id}        - Update record
DELETE /api/weather/{id}        - Delete record
GET    /api/weather/search      - Search by location
GET    /api/weather/current     - Get current weather
GET    /api/weather/forecast    - Get 5-day forecast
```

### Integration APIs
```
GET    /api/integration/youtube    - Get YouTube videos
GET    /api/integration/maps       - Get map information
```

### Export APIs
```
GET    /api/export/json        - Export to JSON
GET    /api/export/csv         - Export to CSV
GET    /api/export/xml         - Export to XML
GET    /api/export/markdown    - Export to Markdown
GET    /api/export/pdf         - Export to PDF
```

## 🧪 Testing

### Backend Tests
```bash
cd weatherpro-backend
mvn test
```

### Frontend Tests
```bash
cd weatherpro-frontend
npm run test
```

## 📦 Building for Production

### Backend
```bash
cd weatherpro-backend
mvn clean package
java -jar target/weatherpro-backend-1.0.0.jar
```

### Frontend
```bash
cd weatherpro-frontend
npm run build
# Output will be in dist/ folder
# Deploy to Vercel, Netlify, or any static hosting
```

## 🌐 Deployment

### Backend Deployment Options
- **Railway** (recommended): Easy Java deployment
- **Render**: Free tier available
- **Heroku**: Classic choice
- **AWS EC2**: Full control

### Frontend Deployment Options
- **Vercel** (recommended): Automatic deployments from Git
- **Netlify**: Great for static sites
- **GitHub Pages**: Free hosting
- **AWS S3 + CloudFront**: Scalable solution

### Database
- **Supabase** (already cloud-hosted)
- Ensure connection strings are updated for production

## 🔒 Environment Variables

### Backend Environment Variables
```bash
SUPABASE_DB_URL=jdbc:postgresql://...
SUPABASE_DB_USERNAME=postgres
SUPABASE_DB_PASSWORD=your-password
OPENWEATHER_API_KEY=your-key
YOUTUBE_API_KEY=your-key
GOOGLE_MAPS_API_KEY=your-key
```

### Frontend Environment Variables
```bash
VITE_API_BASE_URL=https://your-api-domain.com/api
VITE_GOOGLE_MAPS_KEY=your-key
```

## 🐛 Troubleshooting

### Backend won't start
- Verify Java 17+ is installed: `java -version`
- Check database connection in application.properties
- Ensure Supabase database is accessible

### Frontend won't start
- Verify Node.js 18+ is installed: `node --version`
- Delete `node_modules` and run `npm install` again
- Check `.env` file exists with correct API URL

### CORS Errors
- Verify backend CORS configuration includes frontend URL
- Check `cors.allowed-origins` in application.properties

### API Key Issues
- Verify all API keys are valid and have necessary permissions
- OpenWeatherMap keys may take a few hours to activate

## 📝 Project Structure

```
WeatherPro/
├── weatherpro-backend/          # Spring Boot Backend
│   ├── src/main/java/com/weatherpro/
│   │   ├── config/              # Configuration classes
│   │   ├── controller/          # REST Controllers
│   │   ├── service/             # Business logic
│   │   ├── repository/          # Data access
│   │   ├── model/               # Entity classes
│   │   ├── dto/                 # Data transfer objects
│   │   └── exception/           # Exception handlers
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml                  # Maven dependencies
├── weatherpro-frontend/         # React Frontend
│   ├── src/
│   │   ├── components/          # React components
│   │   ├── services/            # API services
│   │   ├── types/               # TypeScript types
│   │   ├── utils/               # Utility functions
│   │   ├── App.tsx              # Main component
│   │   └── main.tsx             # Entry point
│   ├── package.json             # npm dependencies
│   └── vite.config.ts           # Vite configuration
├── database-schema.sql          # Database schema
├── IMPLEMENTATION_PLAN.md       # Detailed implementation plan
└── README.md                    # This file
```

## ✨ Key Features Explained

### Location Validation with Fuzzy Matching
The app uses Levenshtein distance algorithm to handle typos and variations in location names. For example:
- "New Yrok" → "New York"
- "Parris" → "Paris"

### Date Range Validation
- Start date must be before end date
- Cannot be more than 1 year in the past
- Cannot be more than 16 days in the future (OpenWeatherMap limit)

### Multiple Location Formats Supported
- **City names**: "New York", "London", "Tokyo"
- **ZIP codes**: "10001", "SW1A 1AA"
- **Coordinates**: "40.7128,-74.0060" or "40.7128 N, 74.0060 W"
- **Landmarks**: "Eiffel Tower", "Statue of Liberty"

## 👨‍💻 Developer

**Your Name**  
Tech Assessment 2 - AI/ML Engineer Intern  
Product Manager Accelerator

## 🔗 Links

- [Product Manager Accelerator LinkedIn](https://www.linkedin.com/company/productmanageraccelerator/)
- [OpenWeatherMap API Documentation](https://openweathermap.org/api)
- [Supabase Documentation](https://supabase.com/docs)

## 📄 License

This project is developed as part of the Product Manager Accelerator technical assessment.

## 🙏 Acknowledgments

- Product Manager Accelerator for the opportunity
- OpenWeatherMap for weather data API
- Supabase for database hosting
- All open-source libraries used in this project

---

**Built with ❤️ for Product Manager Accelerator**

