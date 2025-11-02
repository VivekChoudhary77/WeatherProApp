import React, { useState, useEffect } from 'react';
import { Cloud } from 'lucide-react';
import { WeatherSearch } from './components/WeatherSearch';
import { WeatherHistory } from './components/WeatherHistory';
import { FiveDayForecast } from './components/FiveDayForecast';
import { ExportPanel } from './components/ExportPanel';
import { YouTubeVideos } from './components/YouTubeVideos';
import { InfoButton } from './components/InfoButton';
import { weatherApi } from './services/weatherApi';
import { WeatherResponse } from './types/weather.types';

function App() {
  const [weatherRecords, setWeatherRecords] = useState<WeatherResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedLocation, setSelectedLocation] = useState<{
    lat: number;
    lon: number;
    name: string;
  } | null>(null);

  useEffect(() => {
    fetchWeatherRecords();
  }, []);

  const fetchWeatherRecords = async (autoSelectFirst = false) => {
    setLoading(true);
    try {
      const records = await weatherApi.getAllWeatherRecords();
      setWeatherRecords(records);
      
      // Only auto-select on new search, not on page load
      if (autoSelectFirst && records.length > 0) {
        setSelectedLocation({
          lat: records[0].latitude,
          lon: records[0].longitude,
          name: records[0].locationName,
        });
      }
    } catch (error) {
      console.error('Failed to fetch weather records:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleRecordUpdate = () => {
    // Auto-select first record when user searches
    fetchWeatherRecords(true);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-blue-50">
      {/* Header */}
      <header className="bg-white shadow-md">
        <div className="container mx-auto px-4 py-6">
          <div className="flex items-center justify-between">
            <div className="flex items-center">
              <Cloud className="w-10 h-10 text-primary-600 mr-3" />
              <div>
                <h1 className="text-3xl font-bold text-gray-900">
                  WeatherPro
                </h1>
                <p className="text-sm text-gray-600">
                  Your Complete Weather Management Solution
                </p>
              </div>
            </div>
            <div className="text-right">
              <p className="text-sm text-gray-600">Developed by Vivek Choudhary</p>
              <p className="text-xs text-gray-500">Tech Assessment 2</p>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="container mx-auto px-4 py-8">
        <div className="space-y-8">
          {/* Weather Search */}
          <WeatherSearch onSearchSuccess={handleRecordUpdate} />

          {/* 5-Day Forecast */}
          {selectedLocation && (
            <FiveDayForecast
              latitude={selectedLocation.lat}
              longitude={selectedLocation.lon}
            />
          )}

          {/* Export Panel */}
          <ExportPanel />

          {/* YouTube Videos */}
          {selectedLocation && (
            <YouTubeVideos location={selectedLocation.name} />
          )}

          {/* Weather History Table */}
          {loading ? (
            <div className="bg-white rounded-lg shadow-md p-12 text-center">
              <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-primary-600 mx-auto"></div>
              <p className="text-gray-500 mt-4">Loading weather records...</p>
            </div>
          ) : (
            <WeatherHistory
              records={weatherRecords}
              onUpdate={handleRecordUpdate}
            />
          )}
        </div>
      </main>

      {/* Footer */}
      <footer className="bg-white border-t mt-12">
        <div className="container mx-auto px-4 py-6">
          <div className="text-center text-gray-600 text-sm">
            <p>
              WeatherPro © {new Date().getFullYear()} | Built with ❤️ for Product
              Manager Accelerator
            </p>
            <p className="mt-2">
              <a
                href="https://www.linkedin.com/school/pmaccelerator/"
                target="_blank"
                rel="noopener noreferrer"
                className="text-primary-600 hover:text-primary-800"
              >
                Visit PM Accelerator on LinkedIn
              </a>
            </p>
          </div>
        </div>
      </footer>

      {/* Info Button */}
      <InfoButton />
    </div>
  );
}

export default App;

