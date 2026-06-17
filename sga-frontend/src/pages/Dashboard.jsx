import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import logo from "../assets/logo.png";

const API = "http://localhost:8080/api";

const modulos = [
    { id: "usuarios", label: "Usuarios", desc: "Gestión de usuarios y roles", color: "bg-blue-50", iconColor: "text-blue-500", icon: (
            <svg className="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" /></svg>
        )},
    { id: "anos-lectivos", label: "Años Lectivos", desc: "Gestión de períodos académicos", color: "bg-amber-50", iconColor: "text-amber-500", icon: (
            <svg className="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
        )},
    { id: "estudiantes", label: "Estudiantes", desc: "Registro y gestión de estudiantes", color: "bg-green-50", iconColor: "text-green-500", icon: (
            <svg className="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M12 14l9-5-9-5-9 5 9 5zm0 0l6.16-3.422a12.083 12.083 0 01.665 6.479A11.952 11.952 0 0012 20.055a11.952 11.952 0 00-6.824-2.998 12.078 12.078 0 01.665-6.479L12 14z" /></svg>
        )},
    { id: "matriculas", label: "Matrículas", desc: "Registro de matrículas", color: "bg-purple-50", iconColor: "text-purple-500", icon: (
            <svg className="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" /></svg>
        )},
    { id: "grados", label: "Grados", desc: "Gestión de grados y paralelos", color: "bg-pink-50", iconColor: "text-pink-500", icon: (
            <svg className="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" /></svg>
        )},
    { id: "asignaturas", label: "Asignaturas", desc: "Catálogo de asignaturas", color: "bg-yellow-50", iconColor: "text-yellow-500", icon: (
            <svg className="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" /></svg>
        )},
    { id: "asignaciones", label: "Asignaciones", desc: "Asignación de docentes a cursos", color: "bg-indigo-50", iconColor: "text-indigo-500", icon: (
            <svg className="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z" /></svg>
        )},
    { id: "horarios", label: "Horarios", desc: "Gestión de horarios anuales", color: "bg-teal-50", iconColor: "text-teal-500", icon: (
            <svg className="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
        )},
    { id: "reportes", label: "Reportes", desc: "Generación de reportes en PDF", color: "bg-rose-50", iconColor: "text-rose-500", icon: (
            <svg className="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 17v-2m3 2v-4m3 4v-6m2 10H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" /></svg>
        )},
    {
        id: "calificaciones",
        label: "Calificaciones",
        desc: "Consulta de notas por curso",
        color: "bg-orange-50",
        iconColor: "text-orange-500",
        icon: (
            <svg className="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 17v-2m3 2v-4m3 4v-6m2 10H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
            </svg>
        )
    }
];

const PRIMARY = "#243A76";
const PRIMARY_DARK = "#1a2d5f";
const PRIMARY_LIGHT = "#2d4a96";

export default function Dashboard() {
    const [showPeriodo, setShowPeriodo] = useState(false);
    const [showUserMenu, setShowUserMenu] = useState(false);
    const [anoActual, setAnoActual] = useState(null);
    const [busqueda, setBusqueda] = useState("");
    const [breadcrumb, setBreadcrumb] = useState(["Inicio"]);
    const navigate = useNavigate();

    const username = localStorage.getItem("username") || "Director";
    const roles = JSON.parse(localStorage.getItem("roles") || "[]");
    const token = localStorage.getItem("token");
    const headers = { Authorization: `Bearer ${token}` };

    useEffect(() => {
        axios.get(`${API}/anos-lectivos/actual`, { headers })
            .then(r => setAnoActual(r.data))
            .catch(() => {});
    }, []);

    const handleModulo = (m) => {
        setBreadcrumb(["Inicio", m.label]);
        navigate(`/${m.id}`);
    };

    const modulosFiltrados = modulos.filter(m =>
        m.label.toLowerCase().includes(busqueda.toLowerCase()) ||
        m.desc.toLowerCase().includes(busqueda.toLowerCase())
    );

    const handleLogout = () => {
        localStorage.clear();
        navigate("/login");
    };

    return (
        <div className="flex flex-col min-h-screen bg-slate-50">

            {/* TOP BAR */}
            <header style={{ backgroundColor: PRIMARY }} className="text-white h-14 flex items-center justify-between px-4 shadow z-30 flex-shrink-0">
                <div className="flex items-center gap-3">
                    <img src={logo} alt="Logo" className="w-8 h-8 rounded-full object-cover border-2 border-white border-opacity-40" />
                    <span className="font-bold text-sm">SGA</span>
                    <span className="text-white text-opacity-70 text-sm hidden sm:inline">| Sistema de Gestión Académica</span>
                </div>

                <div className="flex items-center gap-2">
                    {/* Período */}
                    <div className="relative">
                        <button
                            onClick={() => { setShowPeriodo(!showPeriodo); setShowUserMenu(false); }}
                            style={{ backgroundColor: PRIMARY_LIGHT }}
                            className="flex items-center gap-2 hover:opacity-90 px-3 py-1.5 rounded-lg text-xs font-medium transition"
                        >
                            <svg className="w-4 h-4 text-white opacity-80" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                            </svg>
                            <span className="hidden sm:inline">{anoActual?.nombre || "Sin período"}</span>
                            <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                            </svg>
                        </button>
                        {showPeriodo && (
                            <div className="absolute right-0 top-11 w-64 bg-white rounded-xl shadow-2xl border border-slate-200 z-50 overflow-hidden">
                                <div style={{ backgroundColor: PRIMARY }} className="px-4 py-3 flex items-center gap-2">
                                    <svg className="w-4 h-4 text-white opacity-80" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                    </svg>
                                    <span className="text-white text-sm font-semibold">Año Lectivo</span>
                                </div>
                                <div className="p-3">
                                    {anoActual ? (
                                        <div style={{ color: PRIMARY }} className="flex items-center gap-2 px-3 py-2 rounded-lg bg-blue-50 font-semibold text-sm">
                                            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                                            </svg>
                                            {anoActual.nombre} (Actual)
                                        </div>
                                    ) : (
                                        <p className="text-xs text-slate-400 text-center px-2">No hay año lectivo activo</p>
                                    )}
                                    <p className="text-xs text-slate-400 text-center mt-2 px-2">
                                        Gestiona los años lectivos desde el módulo correspondiente
                                    </p>
                                </div>
                            </div>
                        )}
                    </div>

                    {/* Notificaciones */}
                    <button style={{ '--hover-bg': PRIMARY_LIGHT }} className="relative p-2 rounded-lg hover:bg-white hover:bg-opacity-20 transition">
                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
                        </svg>
                    </button>

                    {/* Usuario */}
                    <div className="relative">
                        <button
                            onClick={() => { setShowUserMenu(!showUserMenu); setShowPeriodo(false); }}
                            style={{ backgroundColor: PRIMARY_LIGHT }}
                            className="flex items-center gap-2 hover:opacity-90 px-3 py-1.5 rounded-lg transition"
                        >
                            <div className="w-7 h-7 bg-white bg-opacity-20 rounded-full flex items-center justify-center text-xs font-bold uppercase border border-white border-opacity-30">
                                {username.charAt(0)}
                            </div>
                            <span className="hidden sm:inline text-xs font-medium capitalize">{username}</span>
                            <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                            </svg>
                        </button>
                        {showUserMenu && (
                            <div className="absolute right-0 top-11 w-52 bg-white rounded-xl shadow-2xl border border-slate-200 z-50 overflow-hidden">
                                <div style={{ backgroundColor: PRIMARY }} className="px-4 py-3">
                                    <p className="text-white text-sm font-semibold capitalize">{username}</p>
                                    <p className="text-white text-opacity-60 text-xs">{roles[0] || "ADMINISTRADOR"}</p>
                                </div>
                                <div className="p-2">
                                    <button className="w-full text-left px-3 py-2 rounded-lg text-sm text-slate-600 hover:bg-slate-50 flex items-center gap-2 transition">
                                        <svg className="w-4 h-4 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" /></svg>
                                        Mi perfil
                                    </button>
                                    <button
                                        onClick={() => navigate("/cambiar-password")}
                                        className="w-full text-left px-3 py-2 rounded-lg text-sm text-slate-600 hover:bg-slate-50 flex items-center gap-2 transition"
                                    >
                                        <svg className="w-4 h-4 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z" /></svg>
                                        Cambiar contraseña
                                    </button>
                                    <hr className="my-1 border-slate-100" />
                                    <button
                                        onClick={handleLogout}
                                        className="w-full text-left px-3 py-2 rounded-lg text-sm text-red-600 hover:bg-red-50 flex items-center gap-2 transition font-medium"
                                    >
                                        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" /></svg>
                                        Cerrar sesión
                                    </button>
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            </header>

            {/* BREADCRUMB + BUSCADOR */}
            <div className="bg-white border-b border-slate-200 px-6 py-2 flex items-center justify-between">
                <nav className="text-xs text-slate-500 flex items-center gap-1">
                    {breadcrumb.map((item, i) => (
                        <span key={i} className="flex items-center gap-1">
              {i > 0 && <span className="text-slate-300">/</span>}
                            <span
                                style={i === breadcrumb.length - 1 ? { color: PRIMARY } : {}}
                                className={i === breadcrumb.length - 1 ? "font-medium" : "hover:underline cursor-pointer"}
                                onClick={() => i === 0 && setBreadcrumb(["Inicio"])}
                            >
                {item}
              </span>
            </span>
                    ))}
                </nav>
                <div className="relative">
                    <input
                        type="text"
                        placeholder="Buscar..."
                        value={busqueda}
                        onChange={e => setBusqueda(e.target.value)}
                        className="pl-3 pr-8 py-1.5 text-xs border border-slate-200 rounded-lg focus:outline-none bg-slate-50 w-44"
                        style={{ '--tw-ring-color': PRIMARY }}
                    />
                    <svg className="w-4 h-4 text-slate-400 absolute right-2 top-1/2 -translate-y-1/2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                    </svg>
                </div>
            </div>

            {/* BODY */}
            <div className="flex flex-1 overflow-hidden">

                {/* PANEL IZQUIERDO */}
                <aside className="w-64 flex-shrink-0 bg-white border-r border-slate-200 overflow-y-auto p-4 hidden lg:block">
                    <div className="rounded-xl border-2 border-dashed border-slate-200 p-4 flex flex-col items-center justify-center text-center h-48 text-slate-400 hover:border-opacity-60 transition cursor-pointer" style={{ '--hover-border': PRIMARY }}>
                        <svg className="w-8 h-8 mb-2 text-slate-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                        </svg>
                        <p className="text-xs">Imagen informativa</p>
                        <p className="text-xs text-slate-300 mt-1">Avisos y comunicados</p>
                    </div>

                    <div className="mt-3 rounded-xl border-2 border-dashed border-slate-200 p-4 flex flex-col items-center justify-center text-center h-48 text-slate-400 transition cursor-pointer">
                        <svg className="w-8 h-8 mb-2 text-slate-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                        </svg>
                        <p className="text-xs">Imagen informativa</p>
                        <p className="text-xs text-slate-300 mt-1">Eventos y noticias</p>
                    </div>
                </aside>

                {/* MAIN */}
                <main className="flex-1 overflow-y-auto p-6">
                    <div className="mb-4">
                        <h1 className="text-lg font-bold text-slate-700">
                            Bienvenido, <span style={{ color: PRIMARY }} className="capitalize">{username}</span>
                        </h1>
                        {anoActual && (
                            <p className="text-slate-400 text-xs mt-0.5">
                                Año lectivo activo: <span style={{ color: PRIMARY }} className="font-semibold">{anoActual.nombre}</span>
                            </p>
                        )}
                    </div>

                    <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
                        {modulosFiltrados.map((m) => (
                            <button
                                key={m.id}
                                onClick={() => handleModulo(m)}
                                className="bg-white border border-slate-200 rounded-xl p-5 flex flex-col items-center gap-3 hover:shadow-md transition-all group text-center"
                                onMouseEnter={e => e.currentTarget.style.borderColor = PRIMARY}
                                onMouseLeave={e => e.currentTarget.style.borderColor = ''}
                            >
                                <div className={`${m.color} p-3 rounded-xl ${m.iconColor}`}>
                                    {m.icon}
                                </div>
                                <div>
                                    <p className="text-sm font-semibold text-slate-700 group-hover:text-[#243A76] transition">{m.label}</p>
                                    <p className="text-xs text-slate-400 mt-0.5 leading-tight">{m.desc}</p>
                                </div>
                            </button>
                        ))}
                        {modulosFiltrados.length === 0 && (
                            <div className="col-span-4 text-center py-10 text-slate-400 text-sm">
                                No se encontraron módulos con "{busqueda}"
                            </div>
                        )}
                    </div>
                </main>
            </div>

            {/* FOOTER */}
            <footer style={{ backgroundColor: PRIMARY }} className="text-white text-opacity-80 text-xs text-center py-2 flex-shrink-0">
                Sistema de Gestión Académica — Escuela Provincias Unidas © 2026
            </footer>

            {/* Overlay */}
            {(showPeriodo || showUserMenu) && (
                <div className="fixed inset-0 z-20" onClick={() => { setShowPeriodo(false); setShowUserMenu(false); }} />
            )}
        </div>
    );
}