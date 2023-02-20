import { createBrowserRouter } from 'react-router-dom';
import VideoIndex from '../pages/video';
import React from 'react';
import App from '../App';
import VideoPlayerPage from '../pages/video/player';

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
  },
  {
    path: '/video',
    element: <VideoIndex />,
  },
  {
    path: '/video/streaming',
    element: <VideoPlayerPage />,
  },
]);

export default router;
