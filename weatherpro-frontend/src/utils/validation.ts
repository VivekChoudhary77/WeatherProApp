export const validateLocation = (location: string): string | null => {
  if (!location || location.trim().length === 0) {
    return 'Location is required';
  }
  if (location.trim().length < 2) {
    return 'Location must be at least 2 characters';
  }
  return null;
};

export const validateDateRange = (
  startDate: string,
  endDate: string
): string | null => {
  if (!startDate || !endDate) {
    return 'Both start and end dates are required';
  }

  const start = new Date(startDate);
  const end = new Date(endDate);
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  if (start > end) {
    return 'Start date must be before or equal to end date';
  }

  const maxPast = new Date();
  maxPast.setFullYear(maxPast.getFullYear() - 1);

  if (start < maxPast) {
    return 'Start date cannot be more than 1 year in the past';
  }

  const maxFuture = new Date();
  maxFuture.setDate(maxFuture.getDate() + 16);

  if (end > maxFuture) {
    return 'End date cannot be more than 16 days in the future';
  }

  return null;
};

