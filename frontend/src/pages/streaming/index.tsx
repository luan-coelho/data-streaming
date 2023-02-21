import React, { useEffect, useState } from 'react';
import VideoPlayer from '../../components/video/player';
import { useParams } from 'react-router-dom';
import api from '../../utils/api';
import { notifyError } from '../../utils/toast';
import ResolutionPath from '../../types/ResolutionPath';

export default function StreamingPage() {
  const [videoSrc, setVideoSrc] = useState([] as ResolutionPath[]);
  const [link, setLink] = useState('');
  const [videoLoaded, setVideoLoaded] = useState(false);
  const { id } = useParams();

  useEffect(() => {
    api
      .get(`/video/${id}`)
      .then((response) => {
        setVideoSrc(response.data.resolutionPaths);
        setLink(
          'http://localhost:8080/api/video/streaming?videopath=' +
            response.data.resolutionPaths[0].path
        );
        setVideoLoaded(true);
      })
      .catch((error) => {
        notifyError(error.data?.detail);
      });
  }, []);

  const handleChangeResolution = (videoPath: string) => {
    setLink('http://localhost:8080/api/video/streaming?videopath=' + videoPath);
    console.log(videoPath);
  };

  return (
    <main>
      <div className="flex items-center justify-items-center w-5/12 flex-col">
        {videoLoaded && (
          <>
            <video key={link}
              className="w-full h-auto max-w-full border border-gray-200 rounded-lg dark:border-gray-700"
              controls>
              <source src={link} type="video/mp4" />
              Your browser does not support the video tag.
            </video>
            <div>
              <h3>Resolutions</h3>
              {videoSrc.map((src, index) => {
                return (
                  <div className="buttons">
                    <button
                      key={index}
                      onClick={() => handleChangeResolution(src.path)}>
                      {src.resolution}
                    </button>
                  </div>
                );
              })}
            </div>
          </>
        )}
      </div>
    </main>
  );
}
