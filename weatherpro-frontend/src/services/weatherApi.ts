import { api } from './api';
import {
  WeatherRequest,
  WeatherResponse,
  CurrentWeather,
  Forecast,
  VideoInfo,
  MapInfo,
} from '../types/weather.types';

// CRUD Operations
export const weatherApi = {
  // CREATE
  createWeatherRecord: async (data: WeatherRequest): Promise<WeatherResponse> => {
    const response = await api.post<WeatherResponse>('/weather', data);
    return response.data;
  },

  // READ
  getAllWeatherRecords: async (): Promise<WeatherResponse[]> => {
    const response = await api.get<WeatherResponse[]>('/weather');
    return response.data;
  },

  getWeatherRecordById: async (id: string): Promise<WeatherResponse> => {
    const response = await api.get<WeatherResponse>(`/weather/${id}`);
    return response.data;
  },

  searchByLocation: async (location: string): Promise<WeatherResponse[]> => {
    const response = await api.get<WeatherResponse[]>('/weather/search', {
      params: { location },
    });
    return response.data;
  },

  // UPDATE
  updateWeatherRecord: async (
    id: string,
    data: WeatherRequest
  ): Promise<WeatherResponse> => {
    const response = await api.put<WeatherResponse>(`/weather/${id}`, data);
    return response.data;
  },

  // DELETE
  deleteWeatherRecord: async (id: string): Promise<void> => {
    await api.delete(`/weather/${id}`);
  },

  // Get current weather
  getCurrentWeather: async (lat: number, lon: number): Promise<CurrentWeather> => {
    const response = await api.get<CurrentWeather>('/weather/current', {
      params: { lat, lon },
    });
    return response.data;
  },

  // Get forecast
  getForecast: async (lat: number, lon: number): Promise<Forecast> => {
    const response = await api.get<Forecast>('/weather/forecast', {
      params: { lat, lon },
    });
    return response.data;
  },

  // Integration APIs
  getYoutubeVideos: async (location: string): Promise<VideoInfo[]> => {
    try {
      const response = await api.get<VideoInfo[]>('/integration/youtube', {
        params: { location },
      });
      return response.data;
    } catch (error) {
      console.error('Failed to fetch YouTube videos:', error);
      return [];
    }
  },

  getMapInfo: async (
    lat: number,
    lon: number,
    location: string
  ): Promise<MapInfo> => {
    const response = await api.get<MapInfo>('/integration/maps', {
      params: { lat, lon, location },
    });
    return response.data;
  },

  // Export APIs
  exportToJson: async () => {
    const response = await api.get('/export/json', {
      responseType: 'blob',
    });
    downloadFile(response, 'weather-data.json');
  },

  exportToCsv: async () => {
    const response = await api.get('/export/csv', {
      responseType: 'blob',
    });
    downloadFile(response, 'weather-data.csv');
  },

  exportToXml: async () => {
    const response = await api.get('/export/xml', {
      responseType: 'blob',
    });
    downloadFile(response, 'weather-data.xml');
  },

  exportToMarkdown: async () => {
    const response = await api.get('/export/markdown', {
      responseType: 'blob',
    });
    downloadFile(response, 'weather-data.md');
  },

  exportToPdf: async () => {
    const response = await api.get('/export/pdf', {
      responseType: 'blob',
    });
    downloadFile(response, 'weather-data.pdf');
  },
};

// Helper function to download files
function downloadFile(response: any, defaultFilename: string) {
  // Extract filename from Content-Disposition header if available
  const contentDisposition = response.headers['content-disposition'];
  let filename = defaultFilename;
  
  if (contentDisposition) {
    const filenameMatch = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/);
    if (filenameMatch && filenameMatch[1]) {
      filename = filenameMatch[1].replace(/['"]/g, '');
    }
  }
  
  // Get Content-Type from response headers
  const contentType = response.headers['content-type'] || 'application/octet-stream';
  
  // Create blob with proper MIME type from response
  const blob = new Blob([response.data], { type: contentType });
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  window.URL.revokeObjectURL(url);
}

