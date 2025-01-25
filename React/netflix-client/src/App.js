import { BrowserRouter, Routes, Route } from "react-router-dom";

import './App.css';
import HomePage from './pages/HomePage';
import Login from './pages/Login'
import Register from './pages/Register';
import UserLogged from "./pages/UserLogged";

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/home" element={<UserLogged />} />
        </Routes>
    </BrowserRouter>
    </div>
  );
}

export default App;
