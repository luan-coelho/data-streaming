import React from 'react';
import Sidebar from './sidebar';

type LayoutProps = {
  children: React.ReactNode;
};

export default function Layout({ children }: LayoutProps) {
  return (
    <div>
      <Sidebar />
      <main>{children}</main>
    </div>
  );
}
