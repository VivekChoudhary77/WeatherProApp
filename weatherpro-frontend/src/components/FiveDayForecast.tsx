import React, { useState, useEffect } from 'react';
import { Forecast } from '../types/weather.types';
import { weatherApi } from '../services/weatherApi';
import { formatDate, getWeatherIcon } from '../utils/dateUtils';
import { Calendar } from 'lucide-react';

interface FiveDayForecastProps {
  latitude?: number;
  longitude?: number;
}

export const FiveDayForecast: React.FC<FiveDayForecastProps> = ({
  latitude,
  longitude,
}) => {
  const [forecast, setForecast] = useState<Forecast | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (latitude && longitude) {
      fetchForecast();
    }
  }, [latitude, longitude]);

  const fetchForecast = async () => {
    if (!latitude || !longitude) return;

    setLoading(true);
    setError(null);

    try {
      const data = await weatherApi.getForecast(latitude, longitude);
      setForecast(data);
    } catch (err: any) {
      setError('Failed to fetch forecast');
      console.error('Forecast error:', err);
    } finally {
      setLoading(false);
    }
  };

  if (!latitude || !longitude) {
    return (
      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-2xl font-bold mb-4 text-gray-800 flex items-center">
          <Calendar className="w-6 h-6 mr-2" />
          5-Day Forecast
        </h2>
        <p className="text-gray-500 text-center py-8">
          Select a location to view the forecast
        </p>
      </div>
    );
  }

  if (loading) {
    return (
      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-2xl font-bold mb-4 text-gray-800 flex items-center">
          <Calendar className="w-6 h-6 mr-2" />
          5-Day Forecast
        </h2>
        <div className="text-center py-8">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600 mx-auto"></div>
          <p className="text-gray-500 mt-4">Loading forecast...</p>
        </div>
      </div>
    );
  }

  if (error || !forecast) {
    return (
      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-2xl font-bold mb-4 text-gray-800 flex items-center">
          <Calendar className="w-6 h-6 mr-2" />
          5-Day Forecast
        </h2>
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
          {error || 'Failed to load forecast'}
        </div>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-lg shadow-md p-6">
      <h2 className="text-2xl font-bold mb-4 text-gray-800 flex items-center">
        <Calendar className="w-6 h-6 mr-2" />
        5-Day Forecast
      </h2>
      <p className="text-gray-600 mb-6">
        {forecast.locationName}, {forecast.country}
      </p>

      <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
        {forecast.dailyForecasts.map((day, index) => (
          <div
            key={index}
            className="bg-gradient-to-br from-blue-50 to-blue-100 rounded-lg p-4 text-center hover:shadow-md transition-shadow"
          >
            <div className="font-semibold text-gray-700 mb-2">
              {formatDate(day.date)}
            </div>

            <img
              src={getWeatherIcon(day.icon)}
              alt={day.weatherCondition}
              className="w-16 h-16 mx-auto"
            />

            <div className="text-sm font-medium text-gray-600 mb-2">
              {day.weatherCondition}
            </div>

            <div className="space-y-1">
              <div className="flex justify-between text-sm">
                <span className="text-gray-600">High:</span>
                <span className="font-semibold text-red-600">
                  {day.tempMax.toFixed(1)}°C
                </span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-gray-600">Low:</span>
                <span className="font-semibold text-blue-600">
                  {day.tempMin.toFixed(1)}°C
                </span>
              </div>
            </div>

            <div className="mt-3 pt-3 border-t border-blue-200 text-xs text-gray-600 space-y-1">
              <div className="flex justify-between">
                <span>💧</span>
                <span>{day.humidity}%</span>
              </div>
              <div className="flex justify-between">
                <span>💨</span>
                <span>{day.windSpeed.toFixed(1)} m/s</span>
              </div>
              {day.pop > 0 && (
                <div className="flex justify-between">
                  <span>🌧️</span>
                  <span>{(day.pop * 100).toFixed(0)}%</span>
                </div>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

