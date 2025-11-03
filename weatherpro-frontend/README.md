# WeatherPro Frontend

React + TypeScript + Vite frontend for the WeatherPro weather application.

## Features

- 🌤️ Real-time weather search (City, ZIP, Coordinates, Landmarks)
- 📍 Current location detection
- 📅 5-day weather forecast
- 🗄️ Weather history with CRUD operations
- 📊 Air Quality Index (AQI) display
- 📥 Export data (JSON, XML, CSV, PDF)
- 🎥 YouTube location recommendations
- 🗺️ Google Maps integration
- 🎨 Modern, responsive UI with Tailwind CSS
- 🔔 Toast notifications
- ⚡ Loading states and error handling

## Tech Stack

- **React 18** - UI library
- **TypeScript** - Type safety
- **Vite** - Build tool
- **Tailwind CSS** - Styling
- **Axios** - HTTP client
- **React Hot Toast** - Notifications
- **Lucide React** - Icons

## Prerequisites

- Node.js 18+ and npm
- Backend API running (see `weatherpro-backend/README.md`)

## Environment Variables

Create a `.env` file in the `weatherpro-frontend` directory:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

For production deployment, use your backend URL:
```env
VITE_API_BASE_URL=https://your-backend-url.com/api
```

## Installation

```bash
# Navigate to frontend directory
cd weatherpro-frontend

# Install dependencies
npm install
```

## Development

```bash
# Start development server
npm run dev
```

The app will be available at: http://localhost:5173

## Build for Production

```bash
# Build optimized production bundle
npm run build

# Preview production build
npm run preview
```

The build output will be in the `dist/` directory.

## Deployment

### Deploy to Vercel (Recommended)

See [VERCEL_DEPLOYMENT.md](../VERCEL_DEPLOYMENT.md) for detailed instructions.

**Quick Deploy:**
```bash
# Install Vercel CLI
npm install -g vercel

# Deploy
cd weatherpro-frontend
vercel

# Set environment variable when prompted:
# VITE_API_BASE_URL=https://your-backend-url.com/api
```

### Deploy to Netlify

1. Build the project: `npm run build`
2. Upload `dist/` folder to Netlify
3. Set environment variable: `VITE_API_BASE_URL`

### Deploy to Other Platforms

The `dist/` folder contains static files that can be served by any web server or hosting platform.

## Project Structure

```
weatherpro-frontend/
├── public/          # Static assets
├── src/
│   ├── components/  # React components
│   │   ├── WeatherSearch.tsx      # Search form with validation
│   │   ├── WeatherHistory.tsx     # CRUD operations
│   │   ├── FiveDayForecast.tsx    # Weather forecast
│   │   ├── ExportPanel.tsx        # Data export UI
│   │   ├── YouTubeVideos.tsx      # Video recommendations
│   │   └── InfoButton.tsx         # Info modal
│   ├── services/    # API integration
│   │   ├── api.ts              # Axios instance
│   │   └── weatherApi.ts       # Weather API calls
│   ├── types/       # TypeScript types
│   │   └── weather.types.ts
│   ├── utils/       # Utility functions
│   │   ├── dateUtils.ts
│   │   └── validation.ts
│   ├── App.tsx      # Main app component
│   ├── main.tsx     # Entry point
│   └── index.css    # Global styles
├── vercel.json      # Vercel configuration
└── package.json     # Dependencies
```

## Key Features Implementation

### Location Search
Supports multiple input formats:
- City names: "New York", "Paris"
- ZIP codes: "10001", "110001" (Indian PIN)
- Coordinates: "40.7128,-74.0060" or "40.7128° N, 74.0060° W"
- Landmarks: "Eiffel Tower", "Burj Khalifa"

### CRUD Operations
- **Create**: Search and save weather records
- **Read**: View all saved weather history
- **Update**: Edit existing records (inline editing)
- **Delete**: Remove records with confirmation modal

### Data Export
Export weather data in multiple formats:
- JSON (structured data)
- XML (legacy systems)
- CSV (spreadsheets)
- PDF (reports)

### Validation
- Date range validation (start date ≤ end date)
- Location format validation
- Duplicate record prevention
- User-friendly error messages

### UI/UX Features
- Loading states with overlay
- Toast notifications (top-right)
- Responsive design (mobile-friendly)
- Accessibility considerations
- Error sanitization (hides technical errors)

## Environment Configuration

| Variable | Description | Example |
|----------|-------------|---------|
| `VITE_API_BASE_URL` | Backend API URL | `http://localhost:8080/api` |

**Note:** Vite requires environment variables to be prefixed with `VITE_` to be accessible in the client.

## Troubleshooting

### Port 5173 already in use
```bash
# Kill existing processes
pkill -9 -f "vite"

# Or use a different port
npm run dev -- --port 3000
```

### API connection errors
- Ensure backend is running on the configured URL
- Check CORS settings in backend
- Verify `VITE_API_BASE_URL` is correct

### Build errors
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
npm run build
```

## Browser Support

- Chrome/Edge (latest)
- Firefox (latest)
- Safari (latest)
- Mobile browsers (iOS Safari, Chrome Mobile)

## Contributing

1. Follow the existing code style
2. Use TypeScript for type safety
3. Test on multiple browsers
4. Ensure responsive design

## License

MIT License - See LICENSE file for details

---

**Built with ❤️ for PM Accelerator Assessment**

