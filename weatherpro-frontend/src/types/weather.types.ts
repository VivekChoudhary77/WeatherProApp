// Type definitions for weather data

export interface WeatherRequest {
  location: string;
  locationType?: string;
  startDate: string;
  endDate: string;
  createdBy?: string;
}

export interface WeatherResponse {
  id: string;
  locationName: string;
  locationType: string;
  latitude: number;
  longitude: number;
  startDate: string;
  endDate: string;
  temperature: number;
  feelsLike: number;
  humidity: number;
  weatherCondition: string;
  weatherDescription: string;
  windSpeed: number;
  pressure: number;
  icon: string;
  aqi?: number; // Air Quality Index (1-5)
  aqiCategory?: string; // Good, Fair, Moderate, Poor, Very Poor
  country?: string;
  state?: string;
  additionalInfo?: string;
  createdAt: string;
  updatedAt: string;
  createdBy?: string;
}

export interface CurrentWeather {
  locationName: string;
  latitude: number;
  longitude: number;
  temperature: number;
  feelsLike: number;
  humidity: number;
  weatherCondition: string;
  weatherDescription: string;
  windSpeed: number;
  pressure: number;
  icon: string;
  sunrise: number;
  sunset: number;
  visibility: number;
  clouds: number;
  country: string;
}

export interface DailyForecast {
  date: string;
  tempMin: number;
  tempMax: number;
  tempAvg: number;
  weatherCondition: string;
  weatherDescription: string;
  icon: string;
  humidity: number;
  windSpeed: number;
  pressure: number;
  clouds: number;
  pop: number;
}

export interface Forecast {
  locationName: string;
  latitude: number;
  longitude: number;
  country: string;
  dailyForecasts: DailyForecast[];
}

export interface VideoInfo {
  videoId: string;
  title: string;
  description: string;
  channelTitle: string;
  publishedAt: string;
  thumbnailUrl: string;
}

export interface MapInfo {
  latitude: number;
  longitude: number;
  locationName: string;
  staticMapUrl: string;
  embedUrl: string;
  googleMapsUrl: string;
}

