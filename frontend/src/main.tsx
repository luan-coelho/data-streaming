import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import router from './router/routes';
import { RouterProvider } from 'react-router-dom';

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>,
)
