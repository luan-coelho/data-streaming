import { createBrowserRouter } from 'react-router-dom';
import React from 'react';
import IndexPage from '../pages';
import App from '../App';
import NotFoundPage from '../pages/notfound';
import { videoRoutes } from './video';

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        index: true,
        element: <IndexPage />,
      },
      {
        path: '*',
        element: <NotFoundPage />,
      },
      ...videoRoutes
    ],
  },
]);
export default router;
