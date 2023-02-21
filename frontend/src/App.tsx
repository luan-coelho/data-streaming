import React from 'react';
import Header from './components/header';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { Outlet } from 'react-router-dom';

export default function App() {
  return (
    <div id="app">
      <ToastContainer />
      <Header />
      <Outlet />
    </div>
  );
}
