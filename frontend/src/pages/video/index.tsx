import React, { useEffect, useState } from 'react';
import Video from '../../types/Video';
import VideoCard from '../../components/video/card';
import api from '../../utils/api';
import { ToastContainer } from 'react-toastify';
import VideoCreateForm from '../../components/video/createform';
import { notifyError } from '../../utils/toast';

export default function VideoPage() {
  const [videos, setVideos] = useState([] as Video[]);

  async function fetchVideos() {
    await api
      .get('/video')
      .then((response) => {
        setVideos(response.data);
      })
      .catch((error) => {
        notifyError(error.data?.detail);
      });
  }

  useEffect(() => {
    fetchVideos().finally();
  }, []);

  return (
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
          <VideoCreateForm dataModal={'createVideo'} notifyParent={fetchVideos}/>
        </div>
        <div className="flex items-center justify-center flex-col gap-2">
          {videos.map((video, index) => {
            return <VideoCard key={index} video={video} />;
          })}
        </div>
      </div>
      <ToastContainer />
    </main>
  );
}
