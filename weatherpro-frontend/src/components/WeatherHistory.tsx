import React, { useState } from 'react';
import { Edit2, Trash2, Save, X } from 'lucide-react';
import toast from 'react-hot-toast';
import { WeatherResponse, WeatherRequest } from '../types/weather.types';
import { weatherApi } from '../services/weatherApi';
import { formatDate, formatDateTime, getWeatherIcon } from '../utils/dateUtils';

interface WeatherHistoryProps {
  records: WeatherResponse[];
  onUpdate: () => void;
}

export const WeatherHistory: React.FC<WeatherHistoryProps> = ({
  records,
  onUpdate,
}) => {
  const [editingId, setEditingId] = useState<string | null>(null);
  const [editData, setEditData] = useState<WeatherRequest | null>(null);
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [deleteRecordId, setDeleteRecordId] = useState<string | null>(null);
  const [deleteRecordName, setDeleteRecordName] = useState<string>('');
  const [loading, setLoading] = useState(false);

  const handleEditClick = (record: WeatherResponse) => {
    setEditingId(record.id);
    setEditData({
      location: record.locationName,
      locationType: record.locationType,
      startDate: record.startDate,
      endDate: record.endDate,
    });
  };

  const handleCancelEdit = () => {
    setEditingId(null);
    setEditData(null);
  };

  const handleSaveEdit = async (id: string) => {
    if (!editData) return;

    setLoading(true);
    try {
      await weatherApi.updateWeatherRecord(id, editData);
      setEditingId(null);
      setEditData(null);
      toast.success('Weather record updated successfully!');
      onUpdate();
    } catch (error: any) {
      console.error('Failed to update record:', error);
      // Use sanitized user-friendly message
      const userMessage = 
        error.response?.data?.userMessage || 
        error.userMessage || 
        'Failed to update record. Please try again.';
      toast.error(userMessage);
    } finally {
      setLoading(false);
    }
  };

  const openDeleteModal = (id: string, locationName: string) => {
    setDeleteRecordId(id);
    setDeleteRecordName(locationName);
    setDeleteModalOpen(true);
  };

  const closeDeleteModal = () => {
    setDeleteModalOpen(false);
    setDeleteRecordId(null);
    setDeleteRecordName('');
  };

  const confirmDelete = async () => {
    if (!deleteRecordId) return;

    setLoading(true);
    try {
      await weatherApi.deleteWeatherRecord(deleteRecordId);
      toast.success('Weather record deleted successfully!');
      closeDeleteModal();
      onUpdate();
    } catch (error: any) {
      console.error('Failed to delete record:', error);
      // Use sanitized user-friendly message
      const userMessage = 
        error.response?.data?.userMessage || 
        error.userMessage || 
        'Failed to delete record. Please try again.';
      toast.error(userMessage);
    } finally {
      setLoading(false);
    }
  };

  if (records.length === 0) {
    return (
      <div className="bg-white rounded-lg shadow-md p-8 text-center">
        <p className="text-gray-500">
          No weather records found. Create your first record above!
        </p>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden">
      <div className="px-6 py-4 bg-gray-50 border-b">
        <h2 className="text-2xl font-bold text-gray-800">Weather History</h2>
        <p className="text-sm text-gray-600 mt-1">
          {records.length} record{records.length !== 1 ? 's' : ''} found
        </p>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Location
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Date Range
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Weather
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Temperature
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Details
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Created
              </th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Actions
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {records.map((record) => (
              <tr key={record.id} className="hover:bg-gray-50">
                <td className="px-6 py-4 whitespace-nowrap">
                  {editingId === record.id ? (
                    <input
                      type="text"
                      value={editData?.location || ''}
                      onChange={(e) =>
                        setEditData({ ...editData!, location: e.target.value })
                      }
                      className="w-full px-2 py-1 border rounded"
                    />
                  ) : (
                    <div>
                      <div className="text-sm font-medium text-gray-900">
                        {record.locationName}
                      </div>
                      <div className="text-xs text-gray-500">
                        {record.latitude.toFixed(4)}, {record.longitude.toFixed(4)}
                      </div>
                    </div>
                  )}
                </td>

                <td className="px-6 py-4 whitespace-nowrap">
                  {editingId === record.id ? (
                    <div className="space-y-1">
                      <input
                        type="date"
                        value={editData?.startDate || ''}
                        onChange={(e) =>
                          setEditData({ ...editData!, startDate: e.target.value })
                        }
                        className="w-full px-2 py-1 border rounded text-xs"
                      />
                      <input
                        type="date"
                        value={editData?.endDate || ''}
                        onChange={(e) =>
                          setEditData({ ...editData!, endDate: e.target.value })
                        }
                        className="w-full px-2 py-1 border rounded text-xs"
                      />
                    </div>
                  ) : (
                    <div className="text-sm text-gray-900">
                      <div>{formatDate(record.startDate)}</div>
                      <div className="text-gray-500">to {formatDate(record.endDate)}</div>
                    </div>
                  )}
                </td>

                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="flex items-center">
                    <img
                      src={getWeatherIcon(record.icon)}
                      alt={record.weatherCondition}
                      className="w-10 h-10"
                    />
                    <div className="ml-2">
                      <div className="text-sm font-medium text-gray-900">
                        {record.weatherCondition}
                      </div>
                      <div className="text-xs text-gray-500">
                        {record.weatherDescription}
                      </div>
                    </div>
                  </div>
                </td>

                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm text-gray-900">
                    <div className="font-semibold text-lg">{record.temperature}°C</div>
                    <div className="text-xs text-gray-500">
                      Feels like {record.feelsLike}°C
                    </div>
                  </div>
                </td>

                <td className="px-6 py-4">
                  <div className="text-xs text-gray-600 space-y-1">
                    <div>💧 Humidity: {record.humidity}%</div>
                    <div>💨 Wind: {record.windSpeed} m/s</div>
                    <div>🌡️ Pressure: {record.pressure} hPa</div>
                    {record.aqi && record.aqi > 0 && (
                      <div className={`font-semibold ${
                        record.aqi === 1 ? 'text-green-600' :
                        record.aqi === 2 ? 'text-green-500' :
                        record.aqi === 3 ? 'text-yellow-600' :
                        record.aqi === 4 ? 'text-orange-600' :
                        'text-red-600'
                      }`}>
                        🌫️ AQI: {record.aqiCategory}
                      </div>
                    )}
                  </div>
                </td>

                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {formatDateTime(record.createdAt)}
                </td>

                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  {editingId === record.id ? (
                    <div className="flex justify-end space-x-2">
                      <button
                        onClick={() => handleSaveEdit(record.id)}
                        disabled={loading}
                        className="text-green-600 hover:text-green-900 disabled:opacity-50"
                        title="Save"
                      >
                        <Save className="w-4 h-4" />
                      </button>
                      <button
                        onClick={handleCancelEdit}
                        disabled={loading}
                        className="text-gray-600 hover:text-gray-900 disabled:opacity-50"
                        title="Cancel"
                      >
                        <X className="w-4 h-4" />
                      </button>
                    </div>
                  ) : (
                    <div className="flex justify-end space-x-2">
                      <button
                        onClick={() => handleEditClick(record)}
                        disabled={loading}
                        className="text-blue-600 hover:text-blue-900 disabled:opacity-50"
                        title="Edit"
                      >
                        <Edit2 className="w-4 h-4" />
                      </button>
                      <button
                        onClick={() => openDeleteModal(record.id, record.locationName)}
                        disabled={loading}
                        className="text-red-600 hover:text-red-900 disabled:opacity-50"
                        title="Delete"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Delete Confirmation Modal */}
      {deleteModalOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
            <h3 className="text-lg font-bold text-gray-900 mb-4">
              Confirm Delete
            </h3>
            <p className="text-gray-600 mb-6">
              Are you sure you want to delete the weather record for{' '}
              <span className="font-semibold text-gray-900">{deleteRecordName}</span>?
              This action cannot be undone.
            </p>
            <div className="flex justify-end space-x-3">
              <button
                onClick={closeDeleteModal}
                disabled={loading}
                className="px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 disabled:opacity-50"
              >
                Cancel
              </button>
              <button
                onClick={confirmDelete}
                disabled={loading}
                className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 disabled:opacity-50 flex items-center"
              >
                {loading ? (
                  <>
                    <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                    Deleting...
                  </>
                ) : (
                  'Delete'
                )}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

