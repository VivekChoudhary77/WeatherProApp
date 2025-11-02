import React, { useState, useEffect } from 'react';
import { Youtube } from 'lucide-react';
import { VideoInfo } from '../types/weather.types';
import { weatherApi } from '../services/weatherApi';

interface YouTubeVideosProps {
  location?: string;
}

export const YouTubeVideos: React.FC<YouTubeVideosProps> = ({ location }) => {
  const [videos, setVideos] = useState<VideoInfo[]>([]);
  const [loading, setLoading] = useState(false);
  const [selectedVideo, setSelectedVideo] = useState<string | null>(null);

  useEffect(() => {
    if (location) {
      fetchVideos();
    }
  }, [location]);

  const fetchVideos = async () => {
    if (!location) return;

    setLoading(true);
    try {
      const data = await weatherApi.getYoutubeVideos(location);
      setVideos(data);
    } catch (error) {
      console.error('Failed to fetch videos:', error);
    } finally {
      setLoading(false);
    }
  };

  if (!location) {
    return (
      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-2xl font-bold mb-4 text-gray-800 flex items-center">
          <Youtube className="w-6 h-6 mr-2 text-red-600" />
          Travel Videos
        </h2>
        <p className="text-gray-500 text-center py-8">
          Select a location to view travel videos
        </p>
      </div>
    );
  }

  if (loading) {
    return (
      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-2xl font-bold mb-4 text-gray-800 flex items-center">
          <Youtube className="w-6 h-6 mr-2 text-red-600" />
          Travel Videos
        </h2>
        <div className="text-center py-8">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-red-600 mx-auto"></div>
          <p className="text-gray-500 mt-4">Loading videos...</p>
        </div>
      </div>
    );
  }

  if (videos.length === 0) {
    return (
      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-2xl font-bold mb-4 text-gray-800 flex items-center">
          <Youtube className="w-6 h-6 mr-2 text-red-600" />
          Travel Videos
        </h2>
        <p className="text-gray-500 text-center py-8">
          No videos found for this location
        </p>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-lg shadow-md p-6">
      <h2 className="text-2xl font-bold mb-4 text-gray-800 flex items-center">
        <Youtube className="w-6 h-6 mr-2 text-red-600" />
        Travel Videos
      </h2>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {videos.map((video) => (
          <div
            key={video.videoId}
            className="border rounded-lg overflow-hidden hover:shadow-lg transition-shadow cursor-pointer"
            onClick={() => setSelectedVideo(video.videoId)}
          >
            <img
              src={video.thumbnailUrl}
              alt={video.title}
              className="w-full h-48 object-cover"
            />
            <div className="p-4">
              <h3 className="font-semibold text-sm line-clamp-2 mb-2">
                {video.title}
              </h3>
              <p className="text-xs text-gray-600">{video.channelTitle}</p>
            </div>
          </div>
        ))}
      </div>

      {selectedVideo && (
        <div
          className="fixed inset-0 bg-black bg-opacity-75 flex items-center justify-center p-4 z-50"
          onClick={() => setSelectedVideo(null)}
        >
          <div
            className="bg-white rounded-lg overflow-hidden max-w-4xl w-full"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="aspect-video">
              <iframe
                width="100%"
                height="100%"
                src={`https://www.youtube.com/embed/${selectedVideo}`}
                title="YouTube video player"
                frameBorder="0"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                allowFullScreen
              ></iframe>
            </div>
            <div className="p-4">
              <button
                onClick={() => setSelectedVideo(null)}
                className="w-full bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 rounded-lg"
              >
                Close
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

