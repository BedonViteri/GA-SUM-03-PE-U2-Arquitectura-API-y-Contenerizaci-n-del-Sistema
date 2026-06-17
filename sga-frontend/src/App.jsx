import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Usuarios from "./pages/Usuarios";
import CambiarPassword from "./pages/CambiarPassword"
import Estudiantes from "./pages/Estudiantes";
import Calificaciones from "./pages/Calificaciones";
function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Navigate to="/login" />} />
                <Route path="/login" element={<Login />} />
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/cambiar-password" element={<CambiarPassword />} />
                <Route path="/usuarios" element={<Usuarios />} />
                <Route path="/estudiantes" element={<Estudiantes />} />
                <Route path="/calificaciones" element={<Calificaciones />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;