import React from 'react';
import { Video } from '@/types/Video';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEye, faHourglass } from '@fortawesome/free-solid-svg-icons';

type Props = {
  video: Video;
};

export default function VideoCard({ video }: Props) {
  return (
    <div className="flex gap-2 flex-col">
      <div
        key={video.id}
        className="p-4 bg-white border border-gray-200 rounded-lg hover:bg-gray-100 dark:bg-gray-800 dark:border-gray-700 dark:hover:bg-gray-700">
        <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">
          {video.title}
        </h5>
        <div className="flex">
          <div className="flex items-center gap-2">
            <FontAwesomeIcon
              icon={faEye}
              className="w-6 h-6 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white"
            />
            <span>Visualizações: {video.views}</span>
          </div>

          <div className="flex items-center gap-2">
            <FontAwesomeIcon
              icon={faHourglass}
              className="w-6 h-6 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white"
            />
            <span>Duração: {video.duration}</span>
          </div>
        </div>
      </div>
    </div>
  );
}
