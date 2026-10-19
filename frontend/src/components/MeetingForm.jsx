import { useState } from 'react';

const initial = { dataMeeting: '', formato: 'ONLINE', status: 'Concluída', duracao: '', uf: '', segmento: '', notaNps: '' };

export default function MeetingForm({ onSubmit, onClose, saving }) {
  const [form, setForm] = useState(initial);
  const update = (event) => setForm({ ...form, [event.target.name]: event.target.value });
  const submit = (event) => {
    event.preventDefault();
    onSubmit({ ...form, duracao: Number(form.duracao), notaNps: Number(form.notaNps), uf: form.uf.toUpperCase() || null, segmento: form.segmento || null });
  };
  return <div className="modal-backdrop" role="presentation"><form className="modal" onSubmit={submit}><div className="modal-header"><div><p className="eyebrow">NOVA REUNIÃO</p><h2>Registrar Meeting</h2></div><button type="button" onClick={onClose}>×</button></div><p className="modal-copy">Os dados desta Meeting serão usados no fluxo de análise comercial.</p><div className="form-grid"><label>Data e horário<input required type="datetime-local" name="dataMeeting" value={form.dataMeeting} onChange={update}/></label><label>Formato<select name="formato" value={form.formato} onChange={update}><option>ONLINE</option><option>PRESENCIAL</option><option>VIDEO</option><option>AUDIO</option><option>VOIP</option></select></label><label>Status<input required maxLength="30" name="status" value={form.status} onChange={update}/></label><label>Duração (segundos)<input required min="1" type="number" name="duracao" value={form.duracao} onChange={update}/></label><label>UF<input maxLength="2" name="uf" value={form.uf} onChange={update}/></label><label>Segmento<input maxLength="100" name="segmento" value={form.segmento} onChange={update}/></label><label>Nota NPS (0 a 10)<input required min="0" max="10" step="0.1" type="number" name="notaNps" value={form.notaNps} onChange={update}/></label></div><div className="modal-actions"><button type="button" className="btn secondary" onClick={onClose}>Cancelar</button><button className="btn primary" disabled={saving}>{saving ? 'Salvando...' : 'Criar Meeting'}</button></div></form></div>;
}
