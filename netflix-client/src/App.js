import { BrowserRouter, Routes, Route } from "react-router-dom";

import './App.css';
import HomePage from './pages/HomePage';
import Login from './pages/Login'
import Register from './pages/Register';
import UserLogged from "./pages/UserLogged";
import MoviePlayer from "./components/MoviePlayer";
import MoviePage from "./components/MoviePage";
import AdminPage from "./pages/AdminPage";

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/home" element={<UserLogged />} />
          <Route path="/movie/:id" element={<MoviePage />} />
          <Route path="/movie/player/:id" element={<MoviePlayer />} />
          <Route path="/search" element={<UserLogged/>} />
          <Route path="/admin" element={<AdminPage />} />
        </Routes>
    </BrowserRouter>
    </div>
  );
}

export default App;
