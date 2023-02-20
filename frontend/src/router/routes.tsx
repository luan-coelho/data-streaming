import { createBrowserRouter } from 'react-router-dom';
import VideoIndex from '../pages/video';
import React from 'react';
import App from '../App';

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
  },
  {
    path: '/video',
    element: <VideoIndex />,
  },
]);

export default router;
