import { ReactNode } from 'react';
import Header from './components/header';

type Prop = {
  children?: ReactNode;
};

export default function App({ children }: Prop) {
  return (
    <div id="app">
      <Header />
      {children}
    </div>
  );
}
