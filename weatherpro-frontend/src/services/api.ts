import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    console.log('Request:', config.method?.toUpperCase(), config.url);
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Helper function to sanitize error messages
const sanitizeErrorMessage = (error: any): string => {
  // Get the error message from various sources
  const rawMessage = 
    error.response?.data?.message || 
    error.response?.data?.error || 
    error.message || 
    'An unexpected error occurred';

  // Check if it's a technical/internal error that users shouldn't see
  const technicalErrorPatterns = [
    /java\./i,
    /compilation/i,
    /handler dispatch failed/i,
    /unresolved compilation/i,
    /local variable/i,
    /lambda/i,
    /spring/i,
    /nullpointer/i,
    /stack trace/i,
    /exception/i
  ];

  const isTechnicalError = technicalErrorPatterns.some(pattern => 
    pattern.test(rawMessage)
  );

  // If it's a technical error, return a user-friendly message
  if (isTechnicalError) {
    console.error('Technical error (hidden from user):', rawMessage);
    return 'The server is currently experiencing issues. Please try again in a moment.';
  }

  // Return the original message if it's user-friendly
  return rawMessage;
};

// Response interceptor
api.interceptors.response.use(
  (response) => {
    console.log('Response:', response.status, response.config.url);
    return response;
  },
  (error) => {
    console.error('API Error:', error.response?.data || error.message);
    
    // Sanitize the error message to prevent technical errors from showing
    const userFriendlyMessage = sanitizeErrorMessage(error);
    
    // Attach sanitized message to error object
    if (error.response?.data) {
      error.response.data.userMessage = userFriendlyMessage;
    } else {
      error.userMessage = userFriendlyMessage;
    }
    
    return Promise.reject(error);
  }
);

