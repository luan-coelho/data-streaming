import React from 'react';
import App from '../../../App';
import VideoPlayer from '../../../components/video/player';

type Props = {
  videoSrc: string;
};

export default function VideoPlayerPage({ videoSrc }: Props) {
  videoSrc =
    '/Vídeos/midia/luancoelho/1/7766cf60-8f4d-488f-801a-cc5edb9d5063/video_1280.mp4';

  return (
    <App>
      <main>
        <div className="flex items-center justify-items-center w-5/12">
          <VideoPlayer
            videoSrc={`http://localhost:8080/api/video/streaming?videopath=${videoSrc}`}
          />
        </div>
      </main>
    </App>
  );
}
