import React, { ReactNode } from 'react';
import Header from './components/header';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

type Prop = {
  children?: ReactNode;
};

export default function App({ children }: Prop) {
  return (
    <div id="app">
      <ToastContainer />
      <Header />
      <main className="h-screen bg-slate-100">{children}</main>
    </div>
  );
}
