import React, { useState } from 'react';
import { Search, MapPin } from 'lucide-react';
import toast from 'react-hot-toast';
import { weatherApi } from '../services/weatherApi';
import { WeatherRequest } from '../types/weather.types';
import { validateLocation, validateDateRange } from '../utils/validation';
import { toInputDate } from '../utils/dateUtils';

interface WeatherSearchProps {
  onSearchSuccess: () => void;
}

export const WeatherSearch: React.FC<WeatherSearchProps> = ({ onSearchSuccess }) => {
  const today = new Date();
  const fiveDaysLater = new Date();
  fiveDaysLater.setDate(today.getDate() + 5);

  const [formData, setFormData] = useState<WeatherRequest>({
    location: '',
    startDate: toInputDate(today),
    endDate: toInputDate(fiveDaysLater),
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSuccess(false);

    // Validate
    const locationError = validateLocation(formData.location);
    if (locationError) {
      setError(locationError);
      return;
    }

    const dateError = validateDateRange(formData.startDate, formData.endDate);
    if (dateError) {
      setError(dateError);
      return;
    }

    setLoading(true);

    try {
      const result = await weatherApi.createWeatherRecord(formData);
      
      // Show success toast
      toast.success(`Weather data fetched successfully for ${result.locationName}!`, {
        icon: '🌤️',
      });
      
      setSuccess(true);
      setFormData({
        location: '',
        startDate: toInputDate(today),
        endDate: toInputDate(fiveDaysLater),
      });
      onSearchSuccess();
      
      setTimeout(() => setSuccess(false), 3000);
    } catch (err: any) {
      // Use sanitized user-friendly message from API interceptor
      const userMessage = 
        err.response?.data?.userMessage || 
        err.userMessage || 
        err.response?.data?.message || 
        'Unable to search weather. Please check your location and try again.';
      
      // Show error toast (technical errors are already sanitized)
      toast.error(userMessage);
      setError(userMessage);
    } finally {
      setLoading(false);
    }
  };

  const handleCurrentLocation = () => {
    if ('geolocation' in navigator) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          setFormData({
            ...formData,
            location: `${latitude.toFixed(6)}, ${longitude.toFixed(6)}`,
          });
        },
        (error) => {
          const errorMsg = 'Failed to get current location. Please enable location access.';
          toast.error(errorMsg);
          setError(errorMsg);
        }
      );
    } else {
      const errorMsg = 'Geolocation is not supported by your browser';
      toast.error(errorMsg);
      setError(errorMsg);
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-md p-6">
      <h2 className="text-2xl font-bold mb-6 text-gray-800">
        Search Weather
      </h2>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Location *
          </label>
          <div className="relative">
            <input
              type="text"
              value={formData.location}
              onChange={(e) =>
                setFormData({ ...formData, location: e.target.value })
              }
              placeholder="City, ZIP code, coordinates, or landmark"
              className="w-full px-4 py-2 pr-10 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              disabled={loading}
            />
            <button
              type="button"
              onClick={handleCurrentLocation}
              className="absolute right-2 top-1/2 -translate-y-1/2 text-gray-400 hover:text-primary-600"
              title="Use current location"
            >
              <MapPin className="w-5 h-5" />
            </button>
          </div>
          <p className="mt-1 text-xs text-gray-500">
            Examples: "New York", "10001", "40.7128,-74.0060", "Eiffel Tower"
          </p>
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Start Date *
            </label>
            <input
              type="date"
              value={formData.startDate}
              onChange={(e) =>
                setFormData({ ...formData, startDate: e.target.value })
              }
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              disabled={loading}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              End Date *
            </label>
            <input
              type="date"
              value={formData.endDate}
              onChange={(e) =>
                setFormData({ ...formData, endDate: e.target.value })
              }
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              disabled={loading}
            />
          </div>
        </div>

        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
            {error}
          </div>
        )}

        {success && (
          <div className="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-lg">
            Weather record created successfully!
          </div>
        )}

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-primary-600 text-white py-3 px-6 rounded-lg font-medium hover:bg-primary-700 transition-colors disabled:bg-gray-400 disabled:cursor-not-allowed flex items-center justify-center"
        >
          {loading ? (
            <>
              <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
              Searching...
            </>
          ) : (
            <>
              <Search className="w-5 h-5 mr-2" />
              Search Weather
            </>
          )}
        </button>
      </form>

      {/* Loading Overlay */}
      {loading && (
        <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
          <div className="animate-spin rounded-full h-16 w-16 border-b-4 border-primary-600"></div>
        </div>
      )}
    </div>
  );
};

