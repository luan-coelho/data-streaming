import { createBrowserRouter } from 'react-router-dom';
import Video from '../pages/video';
import React from 'react';
import App from '../App';
import VideoCreate from '../pages/video/create';

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
  },
  {
    path: '/video',
    element: <Video />,
  },
  {
    path: '/video/create',
    element: <VideoCreate />,
  },
]);

export default router;
