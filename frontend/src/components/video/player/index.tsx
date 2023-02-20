import React from 'react';

type Props = {
  videoSrc: string;
};

export default function VideoPlayer({ videoSrc }: Props) {
  return (
    <>
      <video
        className="w-full h-auto max-w-full border border-gray-200 rounded-lg dark:border-gray-700"
        controls>
        <source src={videoSrc} type="video/mp4" />
        Your browser does not support the video tag.
      </video>
    </>
  );
}
