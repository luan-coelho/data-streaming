import App from '../../App';
import { Link } from 'react-router-dom';

export default function Video() {
  return (
    <App>
      <Link to="create" className="text-3xl font-bold underline">Create Vídeo</Link>
    </App>
  );
}
