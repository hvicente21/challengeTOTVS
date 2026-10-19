import { useState } from 'react';

export default function TranscriptionPanel({ meeting, transcricao, onSave, onAnalyze, processing }) {
  const [text, setText] = useState(transcricao?.texto || '');
  const submit = (event) => { event.preventDefault(); onSave({ idMeeting: meeting.idMeeting, texto: text }); };
  if (!meeting) return null;
  return <section className="transcription-panel"><div><p className="eyebrow">ETAPA 1</p><h2>Transcrição da reunião</h2><p>Inclua o texto bruto para que a API aplique as regras de inteligência comercial.</p></div><form onSubmit={submit}><textarea required value={text} onChange={(event) => setText(event.target.value)} placeholder="Cole aqui a transcrição da reunião..." rows="9"/><div className="form-actions"><span>{text.length} caracteres</span><button className="btn secondary" disabled={processing}>{transcricao ? 'Atualizar transcrição' : 'Salvar transcrição'}</button>{transcricao && <button type="button" className="btn primary" onClick={onAnalyze} disabled={processing}>{processing ? 'Analisando...' : '✦ Analisar transcrição'}</button>}</div></form></section>;
}
