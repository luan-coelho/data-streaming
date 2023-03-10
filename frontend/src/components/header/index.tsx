import { Link } from 'react-router-dom';

export default function Header() {
  return (
    <nav className="flex items-center justify-around p-4 bg-blue-400 gap-9 text-white font-bold">
      <div>
        <span>Data Streaming</span>
      </div>
      <div className="flex items-center justify-center gap-3">
        <Link to="/">Home</Link>
        <Link to="/video">Videos</Link>
      </div>
    </nav>
  );
}
