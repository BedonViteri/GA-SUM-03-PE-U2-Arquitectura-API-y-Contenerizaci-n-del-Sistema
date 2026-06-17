import { useState, useEffect } from "react";
import axios from "axios";
import Layout from "../components/Layout";

const API = "http://localhost:8080/api";
const PRIMARY = "#243A76";
const modalBg = { backgroundColor: "rgba(36, 58, 118, 0.5)" };

const estadoBadge = (e) => ({ ACTIVO: "bg-green-100 text-green-700", INACTIVO: "bg-slate-100 text-slate-500", RETIRADO: "bg-red-100 text-red-600" }[e] || "bg-slate-100 text-slate-500");

const FORM0 = {
  cedula:"", nombres:"", apellidos:"", fechaNacimiento:"", genero:"",
  direccion:"", telefono:"", correo:"",
  nacionalidad:"Ecuatoriana", etnia:"", lugarNacimiento:"", viveCon:"",
  numerosHermanos:"", beneficioSocial:false,
  discapacidad:false, tipoDiscapacidad:"", porcentajeDisc:"", carnetConadis:"",
};
const REP0 = { cedula:"", nombres:"", apellidos:"", parentesco:"", telefonoPrincipal:"", telefonoAlt:"", correo:"", direccion:"" };
const MED0 = {
  tipoSangre:"", alergias:"", enfermedadesCronicas:"", medicamentos:"",
  contactoEmergenciaNombre:"", contactoEmergenciaTelefono:"", contactoEmergenciaParentesco:"",
  observacionesMedicas:"",
};

// ── Componentes base ──────────────────────────────────────────
const F = ({ label, children, span=1 }) => (
    <div style={{ gridColumn:`span ${span}` }}>
      <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide mb-1">{label}</p>
      {children}
    </div>
);

const I = ({ v, onChange, ph, type="text", req }) => (
    <input type={type} value={v??""} onChange={onChange} placeholder={ph} required={req}
           className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:border-[#243A76] focus:ring-1 focus:ring-[#243A76]" />
);

const Sel = ({ v, onChange, opts, placeholder="Seleccionar" }) => (
    <select value={v??""} onChange={onChange}
            className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:border-[#243A76]">
      <option value="">{placeholder}</option>
      {opts.map(o => Array.isArray(o)
          ? <option key={o[0]} value={o[0]}>{o[1]}</option>
          : <option key={o} value={o}>{o}</option>
      )}
    </select>
);

const TA = ({ v, onChange, ph, rows=2 }) => (
    <textarea value={v??""} onChange={onChange} placeholder={ph} rows={rows}
              className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:border-[#243A76] resize-none" />
);

const Chk = ({ checked, onChange, label }) => (
    <label className="flex items-center gap-2 cursor-pointer w-fit select-none">
      <input type="checkbox" checked={!!checked} onChange={onChange} className="w-4 h-4 accent-[#243A76]" />
      <span className="text-sm text-slate-600 font-medium">{label}</span>
    </label>
);

const Divider = ({ label }) => (
    <div className="col-span-3 flex items-center gap-2 mt-1">
      <span className="text-xs font-bold uppercase tracking-wider" style={{ color: PRIMARY }}>{label}</span>
      <div className="flex-1 h-px bg-slate-200" />
    </div>
);

// ── Tabs de formulario ────────────────────────────────────────
const TabPersonal = ({ d, s }) => (
    <div className="grid grid-cols-3 gap-x-4 gap-y-3 pb-2">
      <Divider label="Identificación" />
      <F label="Cédula"><I v={d.cedula} onChange={e=>s({...d,cedula:e.target.value})} ph="0000000000" /></F>
      <F label="Nombres *" span={2}><I v={d.nombres} onChange={e=>s({...d,nombres:e.target.value})} ph="Nombres completos" req /></F>
      <F label="Apellidos *" span={2}><I v={d.apellidos} onChange={e=>s({...d,apellidos:e.target.value})} ph="Apellidos completos" req /></F>
      <F label="Fecha nacimiento"><I type="date" v={d.fechaNacimiento} onChange={e=>s({...d,fechaNacimiento:e.target.value})} req /></F>

      <Divider label="Datos personales" />
      <F label="Género">
        <Sel v={d.genero} onChange={e=>s({...d,genero:e.target.value})} opts={[["M","Masculino"],["F","Femenino"]]} />
      </F>
      <F label="Nacionalidad">
        <Sel v={d.nacionalidad} onChange={e=>s({...d,nacionalidad:e.target.value})}
             opts={["Ecuatoriana","Colombiana","Venezolana","Peruana","Otra"]} placeholder="Seleccionar" />
      </F>
      <F label="Etnia (MINEDUC)">
        <Sel v={d.etnia} onChange={e=>s({...d,etnia:e.target.value})}
             opts={["Mestizo/a","Indígena","Afroecuatoriano/a","Montubio/a","Blanco/a","Otro"]} />
      </F>
      <F label="Lugar de nacimiento" span={2}><I v={d.lugarNacimiento} onChange={e=>s({...d,lugarNacimiento:e.target.value})} ph="Ej: El Empalme, Guayas" /></F>
      <F label="Vive con">
        <Sel v={d.viveCon} onChange={e=>s({...d,viveCon:e.target.value})}
             opts={["Padre y Madre","Solo Padre","Solo Madre","Abuelos","Tíos","Otros familiares","Tutor legal"]} />
      </F>
      <F label="Teléfono"><I v={d.telefono} onChange={e=>s({...d,telefono:e.target.value})} ph="09XXXXXXXX" /></F>
      <F label="Correo electrónico" span={2}><I type="email" v={d.correo} onChange={e=>s({...d,correo:e.target.value})} ph="correo@ejemplo.com" /></F>
      <F label="Dirección domiciliaria" span={3}><I v={d.direccion} onChange={e=>s({...d,direccion:e.target.value})} ph="Barrio, calle, referencia..." /></F>
      <F label="Nº hermanos en la institución"><I type="number" v={d.numerosHermanos} onChange={e=>s({...d,numerosHermanos:e.target.value})} ph="0" /></F>
      <div className="col-span-2 flex items-end pb-1">
        <Chk checked={d.beneficioSocial} onChange={e=>s({...d,beneficioSocial:e.target.checked})} label="Recibe bono o beneficio social" />
      </div>

      <Divider label="Discapacidad" />
      <div className="col-span-3">
        <Chk checked={d.discapacidad} onChange={e=>s({...d,discapacidad:e.target.checked,tipoDiscapacidad:"",porcentajeDisc:"",carnetConadis:""})} label="Presenta discapacidad" />
      </div>
      {d.discapacidad && <>
        <F label="Tipo de discapacidad">
          <Sel v={d.tipoDiscapacidad} onChange={e=>s({...d,tipoDiscapacidad:e.target.value})}
               opts={["Visual","Auditiva","Física - Motora","Intelectual","Psicosocial","Múltiple"]} />
        </F>
        <F label="Porcentaje (%)"><I type="number" v={d.porcentajeDisc} onChange={e=>s({...d,porcentajeDisc:e.target.value})} ph="Ej: 35" /></F>
        <F label="Nº Carnet CONADIS"><I v={d.carnetConadis} onChange={e=>s({...d,carnetConadis:e.target.value})} ph="Ej: 171234567" /></F>
      </>}
    </div>
);

const TabMedico = ({ d, s }) => (
    <div className="grid grid-cols-3 gap-x-4 gap-y-3 pb-2">
      <Divider label="Información médica" />
      <F label="Tipo de sangre">
        <Sel v={d.tipoSangre} onChange={e=>s({...d,tipoSangre:e.target.value})}
             opts={["A+","A-","B+","B-","AB+","AB-","O+","O-"]} placeholder="Desconocido" />
      </F>
      <F label="Alergias conocidas" span={2}><I v={d.alergias} onChange={e=>s({...d,alergias:e.target.value})} ph="Ej: Polen, penicilina, mariscos..." /></F>
      <F label="Enfermedades crónicas" span={3}><TA v={d.enfermedadesCronicas} onChange={e=>s({...d,enfermedadesCronicas:e.target.value})} ph="Ej: Asma, diabetes tipo 1, epilepsia..." /></F>
      <F label="Medicamentos habituales" span={3}><TA v={d.medicamentos} onChange={e=>s({...d,medicamentos:e.target.value})} ph="Ej: Salbutamol inhalador, metformina 500mg..." /></F>
      <F label="Observaciones adicionales" span={3}><TA v={d.observacionesMedicas} onChange={e=>s({...d,observacionesMedicas:e.target.value})} ph="Indicaciones especiales para docentes..." rows={3} /></F>

      <Divider label="Contacto de emergencia" />
      <F label="Nombre completo" span={2}><I v={d.contactoEmergenciaNombre} onChange={e=>s({...d,contactoEmergenciaNombre:e.target.value})} ph="Nombre del contacto" /></F>
      <F label="Parentesco">
        <Sel v={d.contactoEmergenciaParentesco} onChange={e=>s({...d,contactoEmergenciaParentesco:e.target.value})}
             opts={["Padre","Madre","Abuelo/a","Tío/a","Hermano/a","Tutor legal"]} />
      </F>
      <F label="Teléfono de emergencia" span={2}><I v={d.contactoEmergenciaTelefono} onChange={e=>s({...d,contactoEmergenciaTelefono:e.target.value})} ph="09XXXXXXXX" /></F>
    </div>
);

const TabRep = ({ d, s }) => (
    <div className="grid grid-cols-3 gap-x-4 gap-y-3 pb-2">
      <div className="col-span-3">
        <div className="bg-blue-50 border border-blue-100 rounded-lg px-3 py-2 text-xs text-blue-700 font-medium">
          Se registrará un nuevo representante legal vinculado a este estudiante.
        </div>
      </div>
      <Divider label="Datos del representante" />
      <F label="Cédula"><I v={d.cedula} onChange={e=>s({...d,cedula:e.target.value})} ph="0000000000" /></F>
      <F label="Nombres *"><I v={d.nombres} onChange={e=>s({...d,nombres:e.target.value})} ph="Nombres" req /></F>
      <F label="Apellidos *"><I v={d.apellidos} onChange={e=>s({...d,apellidos:e.target.value})} ph="Apellidos" req /></F>
      <F label="Parentesco">
        <Sel v={d.parentesco} onChange={e=>s({...d,parentesco:e.target.value})}
             opts={["Padre","Madre","Abuelo/a","Tío/a","Hermano/a","Tutor legal"]} />
      </F>
      <F label="Teléfono principal *"><I v={d.telefonoPrincipal} onChange={e=>s({...d,telefonoPrincipal:e.target.value})} ph="09XXXXXXXX" req /></F>
      <F label="Teléfono alternativo"><I v={d.telefonoAlt} onChange={e=>s({...d,telefonoAlt:e.target.value})} ph="09XXXXXXXX" /></F>
      <F label="Correo electrónico" span={2}><I type="email" v={d.correo} onChange={e=>s({...d,correo:e.target.value})} ph="correo@ejemplo.com" /></F>
      <F label="Dirección" span={3}><I v={d.direccion} onChange={e=>s({...d,direccion:e.target.value})} ph="Dirección del representante" /></F>
    </div>
);

// ── Componente Modal reutilizable ─────────────────────────────
const Modal = ({ title, onClose, onSubmit, tabs, children, saving }) => (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4" style={modalBg}>
      <div className="bg-white rounded-2xl shadow-2xl flex flex-col" style={{ width:"700px", maxHeight:"90vh" }}>
        {/* Header */}
        <div style={{ backgroundColor: PRIMARY }} className="px-6 py-4 rounded-t-2xl flex items-center justify-between flex-shrink-0">
          <h2 className="text-white font-bold text-base">{title}</h2>
          <button type="button" onClick={onClose} className="text-white opacity-70 hover:opacity-100 text-xl leading-none">✕</button>
        </div>
        <form onSubmit={onSubmit} className="flex flex-col flex-1 overflow-hidden">
          {/* Tabs — con separación del header */}
          <div className="px-6 pt-4 pb-0 flex-shrink-0">
            <div className="flex gap-1 bg-slate-100 p-1 rounded-xl">{tabs}</div>
          </div>
          {/* Contenido */}
          <div className="px-6 pt-4 pb-2 overflow-y-auto flex-1">{children}</div>
          {/* Footer */}
          <div className="px-6 py-4 border-t border-slate-100 flex gap-3 flex-shrink-0 bg-white rounded-b-2xl">
            <button type="button" onClick={onClose}
                    className="flex-1 py-2.5 border border-slate-200 rounded-xl text-sm text-slate-600 hover:bg-slate-50 font-medium transition">
              Cancelar
            </button>
            <button type="submit" disabled={saving} style={{ backgroundColor: PRIMARY }}
                    className="flex-1 py-2.5 text-white rounded-xl text-sm font-semibold hover:opacity-90 disabled:opacity-60 transition">
              {saving ? "Guardando..." : "Guardar"}
            </button>
          </div>
        </form>
      </div>
    </div>
);

// ── Página principal ──────────────────────────────────────────
export default function Estudiantes() {
  const [estudiantes, setEstudiantes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [busqueda, setBusqueda] = useState("");
  const [tab, setTab] = useState("personal");
  const [showModal, setShowModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showConfirm, setShowConfirm] = useState(null);
  const [showVer, setShowVer] = useState(null);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [form, setForm] = useState(FORM0);
  const [rep, setRep] = useState(REP0);
  const [med, setMed] = useState(MED0);
  const [edit, setEdit] = useState(null);

  const token = localStorage.getItem("token");
  const H = { Authorization: `Bearer ${token}` };

  const cargar = () => {
    setLoading(true);
    axios.get(`${API}/estudiantes`, { headers: H })
        .then(r => setEstudiantes(r.data))
        .catch(() => setError("Error al cargar estudiantes"))
        .finally(() => setLoading(false));
  };

  useEffect(() => { cargar(); }, []);

  const filtrados = estudiantes.filter(e =>
      `${e.nombres} ${e.apellidos} ${e.cedula} ${e.codigoEstudiante}`.toLowerCase().includes(busqueda.toLowerCase())
  );

  const reset = () => { setForm(FORM0); setRep(REP0); setMed(MED0); setTab("personal"); setError(""); };

  const crear = async (ev) => {
    ev.preventDefault();
    if (!rep.nombres || !rep.apellidos || !rep.telefonoPrincipal) {
      setTab("representante"); setError("Completa los datos del representante."); return;
    }
    setSaving(true); setError("");
    try {
      const r = await axios.post(`${API}/representantes`, rep, { headers: H });
      await axios.post(`${API}/estudiantes`, {
        ...form, ...med,
        porcentajeDisc: form.porcentajeDisc ? Number(form.porcentajeDisc) : null,
        idRepresentante: r.data.idRepresentante,
      }, { headers: H });
      setSuccess("Estudiante registrado correctamente.");
      setShowModal(false); reset(); cargar();
    } catch (e) { setError(e.response?.data?.message || "Error al registrar"); }
    finally { setSaving(false); }
  };

  const editar = async (ev) => {
    ev.preventDefault();
    setSaving(true); setError("");
    try {
      await axios.put(`${API}/estudiantes/${edit.idEstudiante}`, {
        ...edit,
        porcentajeDisc: edit.porcentajeDisc ? Number(edit.porcentajeDisc) : null,
      }, { headers: H });
      setSuccess("Estudiante actualizado."); setShowEditModal(false); cargar();
    } catch (e) { setError(e.response?.data?.message || "Error al actualizar"); }
    finally { setSaving(false); }
  };

  const cambiarEstado = async (id, estado) => {
    try {
      await axios.patch(`${API}/estudiantes/${id}/estado?estado=${estado}`, {}, { headers: H });
      setSuccess(`Estudiante ${estado==="ACTIVO"?"activado":"desactivado"}.`); cargar();
    } catch { setError("Error al cambiar estado"); }
    setShowConfirm(null);
  };

  useEffect(() => {
    if (success) { const t = setTimeout(() => setSuccess(""), 4000); return () => clearTimeout(t); }
  }, [success]);

  const TabBtn = ({ id, label }) => (
      <button type="button" onClick={() => setTab(id)}
              className="flex-1 py-2 text-xs font-semibold rounded-lg transition"
              style={tab===id ? { backgroundColor: PRIMARY, color:"white" } : { color:"#64748b" }}>
        {label}
      </button>
  );

  return (
      <Layout breadcrumb={["Inicio","Estudiantes"]}>
        {error && <div className="mb-4 bg-red-50 border border-red-200 rounded-xl px-4 py-3 flex justify-between"><span className="text-red-600 text-sm">{error}</span><button onClick={()=>setError("")} className="text-red-400 ml-4">✕</button></div>}
        {success && <div className="mb-4 bg-green-50 border border-green-200 rounded-xl px-4 py-3 flex justify-between"><span className="text-green-600 text-sm">{success}</span><button onClick={()=>setSuccess("")} className="text-green-400 ml-4">✕</button></div>}

        {/* Encabezado */}
        <div className="flex items-center justify-between mb-4">
          <div>
            <h1 className="text-lg font-bold text-slate-700">Estudiantes</h1>
            <p className="text-xs text-slate-400">{filtrados.length} encontrado{filtrados.length!==1?"s":""}</p>
          </div>
          <div className="flex items-center gap-3">
            <div className="relative">
              <input type="text" placeholder="Buscar nombre, cédula, código..."
                     value={busqueda} onChange={e=>setBusqueda(e.target.value)}
                     className="pl-3 pr-8 py-1.5 text-xs border border-slate-200 rounded-lg bg-slate-50 w-64 focus:outline-none" />
              <svg className="w-4 h-4 text-slate-400 absolute right-2 top-1/2 -translate-y-1/2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
              </svg>
            </div>
            <button onClick={()=>{setShowModal(true);reset();}} style={{backgroundColor:PRIMARY}}
                    className="flex items-center gap-2 text-white px-4 py-2 rounded-lg text-sm font-medium hover:opacity-90 transition shadow-sm">
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4"/></svg>
              Nuevo estudiante
            </button>
          </div>
        </div>

        {/* Tabla */}
        <div className="bg-white rounded-xl shadow-sm border border-slate-200 overflow-hidden">
          {loading ? <div className="p-12 text-center text-slate-400 text-sm">Cargando...</div>
              : filtrados.length===0 ? <div className="p-12 text-center text-slate-400 text-sm">Sin estudiantes registrados</div>
                  : <div className="overflow-x-auto">
                    <table className="w-full text-sm">
                      <thead>
                      <tr style={{backgroundColor:PRIMARY}} className="text-white text-xs">
                        {["#","Código","Estudiante","Cédula","Representante","Disc.","Estado","Acciones"].map(h=>(
                            <th key={h} className={`px-4 py-3 font-semibold ${h==="Acciones"||h==="Disc."?"text-center":"text-left"}`}>{h}</th>
                        ))}
                      </tr>
                      </thead>
                      <tbody>
                      {filtrados.map((e,i)=>(
                          <tr key={e.idEstudiante} className={`border-t border-slate-100 hover:bg-slate-50 transition ${i%2===0?"":"bg-slate-50/40"}`}>
                            <td className="px-4 py-3 text-slate-400 text-xs">{i+1}</td>
                            <td className="px-4 py-3 text-xs font-mono text-slate-500">{e.codigoEstudiante||"—"}</td>
                            <td className="px-4 py-3">
                              <div className="flex items-center gap-2">
                                <div style={{backgroundColor:PRIMARY}} className="w-8 h-8 rounded-full flex items-center justify-center text-white text-xs font-bold flex-shrink-0">{e.nombres?.charAt(0)}</div>
                                <div>
                                  <p className="font-semibold text-slate-700 text-xs">{e.nombres} {e.apellidos}</p>
                                  <p className="text-slate-400 text-xs">{e.genero==="M"?"Masculino":e.genero==="F"?"Femenino":"—"}</p>
                                </div>
                              </div>
                            </td>
                            <td className="px-4 py-3 text-xs text-slate-500">{e.cedula||"—"}</td>
                            <td className="px-4 py-3 text-xs text-slate-500">{e.representante||"—"}</td>
                            <td className="px-4 py-3 text-center">{e.discapacidad?<span className="text-xs px-2 py-0.5 rounded-full bg-purple-100 text-purple-700 font-medium">Sí</span>:<span className="text-slate-300">—</span>}</td>
                            <td className="px-4 py-3"><span className={`text-xs px-2 py-1 rounded-full font-medium ${estadoBadge(e.estado)}`}>{e.estado}</span></td>
                            <td className="px-4 py-3">
                              <div className="flex items-center justify-center gap-1">
                                <button onClick={()=>setShowVer(e)} title="Ver" className="p-1.5 rounded-lg text-slate-400 hover:text-[#243A76] hover:bg-blue-50 transition">
                                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/></svg>
                                </button>
                                <button onClick={()=>{setEdit({...e});setTab("personal");setShowEditModal(true);setError("");}} title="Editar" className="p-1.5 rounded-lg text-slate-400 hover:text-blue-600 hover:bg-blue-50 transition">
                                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/></svg>
                                </button>
                                {e.estado==="ACTIVO"
                                    ?<button onClick={()=>setShowConfirm({tipo:"desactivar",id:e.idEstudiante,nombre:`${e.nombres} ${e.apellidos}`})} title="Desactivar" className="p-1.5 rounded-lg text-slate-400 hover:text-orange-600 hover:bg-orange-50 transition">
                                      <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M18.364 18.364A9 9 0 005.636 5.636m12.728 12.728A9 9 0 015.636 5.636m12.728 12.728L5.636 5.636"/></svg>
                                    </button>
                                    :<button onClick={()=>setShowConfirm({tipo:"activar",id:e.idEstudiante,nombre:`${e.nombres} ${e.apellidos}`})} title="Activar" className="p-1.5 rounded-lg text-slate-400 hover:text-green-600 hover:bg-green-50 transition">
                                      <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
                                    </button>}
                              </div>
                            </td>
                          </tr>
                      ))}
                      </tbody>
                    </table>
                  </div>}
        </div>

        {/* MODAL CREAR */}
        {showModal && (
            <Modal title="Nuevo Estudiante" onClose={()=>{setShowModal(false);reset();}} onSubmit={crear} saving={saving}
                   tabs={<><TabBtn id="personal" label="Personal"/><TabBtn id="medico" label="Médico"/><TabBtn id="representante" label="Representante"/></>}>
              {tab==="personal" && <TabPersonal d={form} s={setForm}/>}
              {tab==="medico" && <TabMedico d={med} s={setMed}/>}
              {tab==="representante" && <TabRep d={rep} s={setRep}/>}
              {error && <div className="mt-3 bg-red-50 border border-red-200 rounded-lg px-3 py-2 text-red-600 text-xs">{error}</div>}
            </Modal>
        )}

        {/* MODAL EDITAR */}
        {showEditModal && edit && (
            <Modal title={`Editar — ${edit.nombres} ${edit.apellidos}`} onClose={()=>setShowEditModal(false)} onSubmit={editar} saving={saving}
                   tabs={<><TabBtn id="personal" label="Personal"/><TabBtn id="medico" label="Médico"/></>}>
              {tab==="personal" && <TabPersonal d={edit} s={setEdit}/>}
              {tab==="medico" && <TabMedico d={edit} s={setEdit}/>}
              {error && <div className="mt-3 bg-red-50 border border-red-200 rounded-lg px-3 py-2 text-red-600 text-xs">{error}</div>}
            </Modal>
        )}

        {/* MODAL VER */}
        {showVer && (
            <div className="fixed inset-0 z-50 flex items-center justify-center p-4" style={modalBg}>
              <div className="bg-white rounded-2xl shadow-2xl flex flex-col" style={{width:"540px",maxHeight:"90vh"}}>
                <div style={{backgroundColor:PRIMARY}} className="px-6 py-4 rounded-t-2xl flex items-center justify-between flex-shrink-0">
                  <h2 className="text-white font-bold">Detalle del Estudiante</h2>
                  <button onClick={()=>setShowVer(null)} className="text-white opacity-70 hover:opacity-100 text-xl">✕</button>
                </div>
                <div className="p-6 overflow-y-auto flex-1 space-y-5">
                  <div className="flex items-center gap-4">
                    <div style={{backgroundColor:PRIMARY}} className="w-14 h-14 rounded-full flex items-center justify-center text-white text-2xl font-bold flex-shrink-0">{showVer.nombres?.charAt(0)}</div>
                    <div>
                      <p className="font-bold text-slate-800 text-base">{showVer.nombres} {showVer.apellidos}</p>
                      <p className="text-xs text-slate-400 font-mono">{showVer.codigoEstudiante||"Sin código"}</p>
                      <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${estadoBadge(showVer.estado)}`}>{showVer.estado}</span>
                    </div>
                  </div>
                  <hr/>
                  <div>
                    <p className="text-xs font-bold uppercase mb-3" style={{color:PRIMARY}}>Datos Personales</p>
                    <div className="grid grid-cols-3 gap-3 text-sm">
                      {[["Cédula",showVer.cedula],["Nacimiento",showVer.fechaNacimiento],["Género",showVer.genero==="M"?"Masculino":showVer.genero==="F"?"Femenino":null],["Nacionalidad",showVer.nacionalidad],["Etnia",showVer.etnia],["Lugar nacimiento",showVer.lugarNacimiento],["Vive con",showVer.viveCon],["Teléfono",showVer.telefono],["Correo",showVer.correo]].map(([k,v])=>v?(
                          <div key={k} className={k==="Correo"||k==="Lugar nacimiento"?"col-span-2":""}>
                            <p className="text-xs text-slate-400">{k}</p>
                            <p className="font-medium text-slate-700">{v}</p>
                          </div>
                      ):null)}
                      {showVer.beneficioSocial&&<div><p className="text-xs text-slate-400">Beneficio social</p><p className="font-medium text-green-600">Sí</p></div>}
                    </div>
                  </div>
                  {showVer.discapacidad&&<><hr/><div>
                    <p className="text-xs font-bold uppercase mb-3" style={{color:PRIMARY}}>Discapacidad</p>
                    <div className="grid grid-cols-3 gap-3 text-sm">
                      <div><p className="text-xs text-slate-400">Tipo</p><p className="font-medium text-slate-700">{showVer.tipoDiscapacidad||"—"}</p></div>
                      <div><p className="text-xs text-slate-400">Porcentaje</p><p className="font-medium text-slate-700">{showVer.porcentajeDisc?`${showVer.porcentajeDisc}%`:"—"}</p></div>
                      <div><p className="text-xs text-slate-400">Carnet CONADIS</p><p className="font-medium text-slate-700">{showVer.carnetConadis||"—"}</p></div>
                    </div>
                  </div></>}
                  {showVer.representante&&<><hr/><div>
                    <p className="text-xs font-bold uppercase mb-2" style={{color:PRIMARY}}>Representante</p>
                    <p className="text-sm font-medium text-slate-700">{showVer.representante}</p>
                  </div></>}
                </div>
                <div className="px-6 py-4 border-t border-slate-100 flex-shrink-0">
                  <button onClick={()=>setShowVer(null)} style={{backgroundColor:PRIMARY}} className="w-full py-2.5 text-white rounded-xl text-sm font-semibold hover:opacity-90 transition">Cerrar</button>
                </div>
              </div>
            </div>
        )}

        {/* CONFIRMACIÓN */}
        {showConfirm&&(
            <div className="fixed inset-0 z-50 flex items-center justify-center p-4" style={modalBg}>
              <div className="bg-white rounded-2xl shadow-2xl w-80 overflow-hidden">
                <div className={`px-6 py-4 ${showConfirm.tipo==="desactivar"?"bg-orange-500":"bg-green-500"}`}>
                  <h2 className="text-white font-bold">{showConfirm.tipo==="desactivar"?"Desactivar estudiante":"Activar estudiante"}</h2>
                </div>
                <div className="p-6">
                  <p className="text-slate-600 text-sm">¿{showConfirm.tipo==="desactivar"?"Desactivar":"Activar"} a <strong>{showConfirm.nombre}</strong>?</p>
                  <div className="flex gap-3 mt-5">
                    <button onClick={()=>setShowConfirm(null)} className="flex-1 py-2 border border-slate-200 rounded-lg text-sm text-slate-600 hover:bg-slate-50 transition">Cancelar</button>
                    <button onClick={()=>cambiarEstado(showConfirm.id,showConfirm.tipo==="desactivar"?"INACTIVO":"ACTIVO")}
                            className={`flex-1 py-2 text-white rounded-lg text-sm font-medium transition ${showConfirm.tipo==="desactivar"?"bg-orange-500 hover:bg-orange-600":"bg-green-500 hover:bg-green-600"}`}>
                      Confirmar
                    </button>
                  </div>
                </div>
              </div>
            </div>
        )}
      </Layout>
  );
}