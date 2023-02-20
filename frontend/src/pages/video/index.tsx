import React, { useEffect, useState } from 'react';
import App from '../../App';
import Video from '../../types/Video';
import { Link } from 'react-router-dom';
import VideoCard from '../../components/video/card';
import api from '../../utils/api';
import { ToastContainer } from 'react-toastify';
import VideoCreateForm from '../../components/video/createform';

export default function VideoIndex() {
  const [videos, setVideos] = useState([] as Video[]);

  const fetchVideos = async () => {
    const { data } = await api.get('/video');
    setVideos(data);
  };

  useEffect(() => {
    fetchVideos();
  }, []);

  return (
    <App>
      <main className="flex items-center justify-center">
        <div className="w-10/12 m-4">
          <div className="mt-2 flex items-center justify-between">
            <span className="text-2xl">Vídeos</span>
            <button
              className="text-sm bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 mb-2 border-green-700 rounded"
              data-modal-target="createVideo"
              data-modal-toggle="createVideo">
              Create Video
            </button>
            <VideoCreateForm dataModal={'createVideo'} />
          </div>
          <div className="flex items-center justify-center flex-col gap-2">
            {videos.map((video, index) => {
              return <VideoCard key={index} video={video} />;
            })}
          </div>
        </div>
        <ToastContainer />
      </main>
    </App>
  );
}
