import React, { useState } from 'react';
import { Download } from 'lucide-react';
import toast from 'react-hot-toast';
import { weatherApi } from '../services/weatherApi';

export const ExportPanel: React.FC = () => {
  const [loading, setLoading] = useState<string | null>(null);

  const handleExport = async (format: string) => {
    setLoading(format);
    try {
      switch (format) {
        case 'json':
          await weatherApi.exportToJson();
          break;
        case 'csv':
          await weatherApi.exportToCsv();
          break;
        case 'xml':
          await weatherApi.exportToXml();
          break;
        case 'markdown':
          await weatherApi.exportToMarkdown();
          break;
        case 'pdf':
          await weatherApi.exportToPdf();
          break;
      }
      toast.success(`${format.toUpperCase()} file downloaded successfully!`, {
        icon: '📥',
      });
    } catch (error: any) {
      console.error('Export failed:', error);
      // Use sanitized error message
      const userMessage = 
        error.response?.data?.userMessage || 
        error.userMessage || 
        'Export failed. Please try again.';
      toast.error(userMessage);
    } finally {
      setLoading(null);
    }
  };

  const formats = [
    { id: 'json', label: 'JSON', description: 'JavaScript Object Notation' },
    { id: 'csv', label: 'CSV', description: 'Comma Separated Values' },
    { id: 'xml', label: 'XML', description: 'Extensible Markup Language' },
    { id: 'markdown', label: 'Markdown', description: 'Formatted text document' },
    { id: 'pdf', label: 'PDF', description: 'Portable Document Format' },
  ];

  return (
    <div className="bg-white rounded-lg shadow-md p-6">
      <h2 className="text-2xl font-bold mb-4 text-gray-800 flex items-center">
        <Download className="w-6 h-6 mr-2" />
        Export Data
      </h2>
      <p className="text-gray-600 mb-6">
        Export all weather records in your preferred format
      </p>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4">
        {formats.map((format) => (
          <button
            key={format.id}
            onClick={() => handleExport(format.id)}
            disabled={loading !== null}
            className="bg-gradient-to-br from-primary-50 to-primary-100 hover:from-primary-100 hover:to-primary-200 border border-primary-200 rounded-lg p-4 text-center transition-all disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <div className="font-semibold text-primary-700 mb-1">
              {format.label}
            </div>
            <div className="text-xs text-gray-600">{format.description}</div>
            {loading === format.id && (
              <div className="mt-2">
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-primary-600 mx-auto"></div>
              </div>
            )}
          </button>
        ))}
      </div>
    </div>
  );
};

