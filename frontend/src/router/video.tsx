import VideoPage from '../pages/video';
import StreamingPage from '../pages/streaming';
import React from 'react';

export const videoRoutes = [
  {
    path: 'video',
    element: <VideoPage />,
  },
  {
    path: 'video/:id',
    element: <StreamingPage />,
  },
];
