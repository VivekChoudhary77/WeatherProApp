import React, { useState } from 'react';
import { Info, X } from 'lucide-react';

export const InfoButton: React.FC = () => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <>
      <button
        onClick={() => setIsOpen(true)}
        className="fixed bottom-4 right-4 bg-primary-600 text-white p-3 rounded-full shadow-lg hover:bg-primary-700 transition-colors z-50"
        title="About this app"
      >
        <Info className="w-6 h-6" />
      </button>

      {isOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-[80vh] overflow-y-auto">
            <div className="p-6">
              <div className="flex justify-between items-start mb-4">
                <h2 className="text-2xl font-bold text-gray-900">
                  About WeatherPro
                </h2>
                <button
                  onClick={() => setIsOpen(false)}
                  className="text-gray-400 hover:text-gray-600"
                >
                  <X className="w-6 h-6" />
                </button>
              </div>

              <div className="space-y-4 text-gray-700">
                <div>
                  <h3 className="text-lg font-semibold mb-2">Developer</h3>
                  <p>Vivek Choudhary</p>
                  <p className="text-sm text-gray-600">Tech Assessment 2 - AI/ML Engineer Intern</p>
                </div>

                <div>
                  <h3 className="text-lg font-semibold mb-2">About PM Accelerator</h3>
                  <p className="mb-2">
                    Product Manager Accelerator is a mentorship-driven program that helps 
                    aspiring product managers break into the tech industry. The program 
                    provides hands-on training, real-world projects, and career support 
                    to help participants develop the skills and confidence needed to 
                    succeed as product managers.
                  </p>
                  <a
                    href="https://www.linkedin.com/school/pmaccelerator/"
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-primary-600 hover:text-primary-800 font-medium inline-flex items-center"
                  >
                    Visit PM Accelerator on LinkedIn →
                  </a>
                </div>

                <div>
                  <h3 className="text-lg font-semibold mb-2">Features</h3>
                  <ul className="list-disc list-inside space-y-1 text-sm">
                    <li>Create, Read, Update, Delete weather records</li>
                    <li>Location validation with fuzzy matching</li>
                    <li>Date range validation</li>
                    <li>5-day weather forecast</li>
                    <li>Current location detection</li>
                    <li>YouTube videos integration</li>
                    <li>Google Maps integration</li>
                    <li>Data export (JSON, CSV, XML, PDF, Markdown)</li>
                  </ul>
                </div>

                <div>
                  <h3 className="text-lg font-semibold mb-2">Tech Stack</h3>
                  <div className="grid grid-cols-2 gap-2 text-sm">
                    <div>
                      <p className="font-medium">Backend:</p>
                      <ul className="list-disc list-inside ml-2">
                        <li>Java Spring Boot</li>
                        <li>PostgreSQL (Supabase)</li>
                      </ul>
                    </div>
                    <div>
                      <p className="font-medium">Frontend:</p>
                      <ul className="list-disc list-inside ml-2">
                        <li>React + TypeScript</li>
                        <li>Vite</li>
                        <li>Tailwind CSS</li>
                      </ul>
                    </div>
                  </div>
                </div>

                <div className="pt-4 border-t">
                  <p className="text-sm text-gray-600">
                    Built with ❤️ for Product Manager Accelerator
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

