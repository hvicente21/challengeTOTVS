import { useEffect, useMemo, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { api, ApiError } from './services/api';
import { formatDate, formatDuration, getId, priorityLabel, sentimentTone } from './utils/formatters';
import Sidebar from './components/Sidebar';
import MeetingForm from './components/MeetingForm';
import TranscriptionPanel from './components/TranscriptionPanel';
import { EmptyState, Notice } from './components/Feedback';

const safeArray = (value) => Array.isArray(value) ? value : [];
const getMeetingId = (meeting) => getId(meeting, 'idMeeting');
const getTranscricaoId = (item) => getId(item, 'idTranscricao');
const getResultadoId = (item) => getId(item, 'idResultadoAnalise');

function classifyAlerts(alertas, resultadoId) {
  return safeArray(alertas).filter((alerta) => {
    const resultado = alerta.resultadoAnalise;
    return alerta.idResultadoAnalise === resultadoId || getResultadoId(resultado) === resultadoId;
  });
}

function getResultAlerts(result, alertasGlobais) {
  if (!result) return [];
  if (Array.isArray(result.alertas)) return result.alertas;
  return classifyAlerts(alertasGlobais, getResultadoId(result));
}

function MetricCard({ label, value, caption, tone = 'blue', icon }) {
  return <article className="metric-card"><div className="metric-top"><span>{label}</span><i className={`icon-badge ${tone}`}>{icon}</i></div><strong>{value}</strong><small>{caption}</small></article>;
}

function countBy(items, getKey) {
  return safeArray(items).reduce((counts, item) => {
    const key = getKey(item) || 'Não informado';
    counts[key] = (counts[key] || 0) + 1;
    return counts;
  }, {});
}

function DistributionChart({ title, description, values, tone = 'blue' }) {
  const entries = Object.entries(values);
  const maximum = Math.max(...entries.map(([, value]) => value), 1);
  return <article className="card distribution-card"><div><p className="eyebrow">DADOS DA API</p><h2>{title}</h2><p>{description}</p></div>{entries.length ? <div className="bar-list">{entries.map(([label, value]) => <div className="bar-row" key={label}><span>{label}</span><div className="bar-track"><i className={tone} style={{ width: `${(value / maximum) * 100}%` }} /></div><b>{value}</b></div>)}</div> : <p className="muted">Ainda não há análises para compor esta visualização.</p>}</article>;
}

function meetingForResult(result, meetings, transcricoes) {
  const transcricao = result?.transcricao || safeArray(transcricoes).find((item) => getTranscricaoId(item) === result?.idTranscricao);
  return transcricao?.meeting || safeArray(meetings).find((meeting) => getMeetingId(meeting) === transcricao?.idMeeting) || null;
}

function App() {
  const location = useLocation();
  const navigate = useNavigate();
  const page = ({ '/': 'dashboard', '/reunioes': 'meetings', '/analise': 'analysis', '/insights': 'insights' })[location.pathname] || 'dashboard';
  const [data, setData] = useState({ meetings: [], transcricoes: [], resultados: [], alertas: [], palavras: [] });
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(false);
  const [error, setError] = useState('');
  const [notice, setNotice] = useState(null);
  const [showMeetingForm, setShowMeetingForm] = useState(false);
  const [selectedMeetingId, setSelectedMeetingId] = useState(null);
  const [analysisResponses, setAnalysisResponses] = useState({});

  const reload = async () => {
    setLoading(true); setError('');
    try { setData(await api.getDashboardData()); }
    catch (requestError) { setError(requestError.message || 'Não foi possível carregar os dados da API.'); }
    finally { setLoading(false); }
  };
  useEffect(() => { reload(); }, []);

  const meetings = safeArray(data.meetings);
  const selectedMeeting = meetings.find((meeting) => getMeetingId(meeting) === selectedMeetingId) || meetings[0] || null;
  const selectedTranscricao = useMemo(() => safeArray(data.transcricoes).find((transcricao) => transcricao.idMeeting === getMeetingId(selectedMeeting) || getMeetingId(transcricao.meeting) === getMeetingId(selectedMeeting)), [data.transcricoes, selectedMeeting]);
  const selectedResult = useMemo(() => {
    const transcricaoId = getTranscricaoId(selectedTranscricao);
    return analysisResponses[transcricaoId] || safeArray(data.resultados).find((result) => result.idTranscricao === transcricaoId || getTranscricaoId(result.transcricao) === transcricaoId);
  }, [analysisResponses, data.resultados, selectedTranscricao]);
  const selectedAlerts = getResultAlerts(selectedResult, data.alertas);

  const dashboard = useMemo(() => {
    const results = safeArray(data.resultados);
    const risk = results.filter((item) => item.classificacao === 'Risco').length;
    const opportunities = safeArray(data.alertas).filter((item) => item.tipoAlerta === 'Oportunidade').length;
    const averageChurn = results.length ? Math.round(results.reduce((sum, item) => sum + Number(item.riscoChurn || 0), 0) / results.length) : 0;
    return { risk, opportunities, averageChurn, analyzed: results.length };
  }, [data]);

  const run = async (operation, success) => {
    setActionLoading(true); setNotice(null);
    try { await operation(); await reload(); setNotice({ type: 'success', message: success }); }
    catch (requestError) { setNotice({ type: 'error', message: requestError.message || 'Não foi possível concluir a operação.' }); }
    finally { setActionLoading(false); }
  };
  const createMeeting = (payload) => run(async () => { const meeting = await api.createMeeting(payload); setSelectedMeetingId(getMeetingId(meeting)); setShowMeetingForm(false); }, 'Meeting criada com sucesso. Agora inclua a transcrição.');
  const saveTranscricao = (payload) => run(() => selectedTranscricao ? api.updateTranscricao(getTranscricaoId(selectedTranscricao), payload) : api.createTranscricao(payload), 'Transcrição salva. Você já pode solicitar a análise.');
  const analyze = () => run(async () => {
    const result = await api.analyzeTranscricao(getTranscricaoId(selectedTranscricao));
    setAnalysisResponses((current) => ({ ...current, [getTranscricaoId(selectedTranscricao)]: result }));
    navigate('/analise');
  }, 'Análise concluída e dados atualizados.');
  const navigateTo = (nextPage) => {
    const paths = { dashboard: '/', meetings: '/reunioes', analysis: '/analise', insights: '/insights' };
    if (nextPage !== 'dashboard' && nextPage !== 'insights' && !selectedMeetingId && meetings[0]) setSelectedMeetingId(getMeetingId(meetings[0]));
    navigate(paths[nextPage]);
  };

  return <div className="app-shell"><Sidebar page={page} onNavigate={navigateTo}/><main className="main-content"><Notice notice={notice} onClose={() => setNotice(null)}/>{error ? <div className="api-error"><strong>Não foi possível conectar à API.</strong><p>{error} Confirme se o Spring Boot está em execução na porta 8080.</p><button className="btn primary" onClick={reload}>Tentar novamente</button></div> : loading ? <div className="loading"><span></span> Carregando inteligência comercial...</div> : <>
    {page === 'dashboard' && <Dashboard dashboard={dashboard} meetings={meetings} results={safeArray(data.resultados)} transcricoes={safeArray(data.transcricoes)} onNew={() => setShowMeetingForm(true)} onSelect={(id) => { setSelectedMeetingId(id); navigate('/reunioes'); }} />}
    {page === 'meetings' && <MeetingsPage meetings={meetings} selected={selectedMeeting} transcricao={selectedTranscricao} onSelect={setSelectedMeetingId} onNew={() => setShowMeetingForm(true)} onSaveTranscricao={saveTranscricao} onAnalyze={analyze} processing={actionLoading} />}
    {page === 'analysis' && <AnalysisPage meeting={selectedMeeting} transcricao={selectedTranscricao} result={selectedResult} alerts={selectedAlerts} words={selectedMeeting?.palavrasChave || []} onBack={() => navigate('/reunioes')} onAnalyze={analyze} processing={actionLoading} />}
    {page === 'insights' && <InsightsPage results={safeArray(data.resultados)} alertas={safeArray(data.alertas)} meetings={meetings} transcricoes={safeArray(data.transcricoes)} onOpen={(meetingId) => { setSelectedMeetingId(meetingId); navigate('/analise'); }} />}
  </>}{showMeetingForm && <MeetingForm saving={actionLoading} onSubmit={createMeeting} onClose={() => setShowMeetingForm(false)}/>}</main></div>;
}

function Dashboard({ dashboard, meetings, results, transcricoes, onNew, onSelect }) {
  const sentiments = countBy(results, (item) => item.sentimento);
  const classifications = countBy(results, (item) => item.classificacao);
  const churnRows = [...results].sort((a, b) => Number(b.riscoChurn || 0) - Number(a.riscoChurn || 0)).slice(0, 5);
  return <><header className="page-header"><div><h1>Visão geral</h1><p>Acompanhamento de insights extraídos das últimas reuniões comerciais.</p></div><button className="btn primary" onClick={onNew}>+ Nova reunião</button></header><section className="metrics-grid"><MetricCard label="Reuniões analisadas" value={dashboard.analyzed} caption="Resultados gerados pela API" icon="◫"/><MetricCard label="Risco de churn" value={dashboard.risk} caption="Reuniões que exigem atenção" tone="danger" icon="↓"/><MetricCard label="Oportunidades" value={dashboard.opportunities} caption="Sinais comerciais identificados" tone="success" icon="↗"/><MetricCard label="Churn médio" value={`${dashboard.averageChurn}%`} caption="Com base nas análises" tone="warning" icon="!"/></section><section className="dashboard-analytics"><DistributionChart title="Sentimento das conversas" description="Quantidade de resultados por sentimento retornado." values={sentiments} tone="blue"/><DistributionChart title="Classificação comercial" description="Distribuição dos contextos analisados pela API." values={classifications} tone="green"/></section><section className="dashboard-bottom"><section className="card recent-card"><div className="section-heading"><div><h2>Reuniões recentes</h2><p>Selecione uma reunião para enviar ou analisar a transcrição.</p></div><button className="text-button" onClick={() => onSelect(meetings[0] && getMeetingId(meetings[0]))}>Ver todas →</button></div>{meetings.length ? <div className="meeting-list">{meetings.slice(0, 5).map((meeting) => <button className="meeting-row" key={getMeetingId(meeting)} onClick={() => onSelect(getMeetingId(meeting))}><span className="meeting-icon">◫</span><span><strong>{meeting.segmento || 'Meeting comercial'}</strong><small>{formatDate(meeting.dataMeeting)} · {formatDuration(meeting.duracao)} · {meeting.uf || 'UF não informada'}</small></span><span className="status-chip">{meeting.status}</span><b>→</b></button>)}</div> : <EmptyState title="Nenhuma reunião cadastrada" description="Comece registrando uma Meeting para enviar uma transcrição e gerar inteligência comercial." action={<button className="btn primary" onClick={onNew}>Criar primeira Meeting</button>}/>}</section><section className="card churn-table"><div className="section-heading"><div><h2>Maior risco de churn</h2><p>Resultados ordenados pelo percentual devolvido pela análise.</p></div></div>{churnRows.length ? <div className="table-scroll"><table><thead><tr><th>Meeting</th><th>Sentimento</th><th>Churn</th></tr></thead><tbody>{churnRows.map((result) => { const meeting = meetingForResult(result, meetings, transcricoes); return <tr key={getResultadoId(result)}><td>{meeting?.segmento || 'Meeting comercial'}</td><td><span className={`classification ${String(result.sentimento || '').toLowerCase()}`}>{result.sentimento || '—'}</span></td><td><strong>{Math.round(Number(result.riscoChurn || 0))}%</strong></td></tr>; })}</tbody></table></div> : <p className="muted">Nenhuma análise disponível.</p>}</section></section></>;
}

function MeetingsPage({ meetings, selected, transcricao, onSelect, onNew, onSaveTranscricao, onAnalyze, processing }) {
  return <><header className="page-header"><div><p className="eyebrow">REUNIÕES</p><h1>Central de reuniões</h1><p>Selecione uma Meeting e transforme sua transcrição em dados acionáveis.</p></div><button className="btn primary" onClick={onNew}>+ Nova reunião</button></header>{meetings.length ? <div className="meetings-layout"><section className="card meeting-picker"><h2>Reuniões</h2>{meetings.map((meeting) => <button key={getMeetingId(meeting)} className={getMeetingId(selected) === getMeetingId(meeting) ? 'picker-item selected' : 'picker-item'} onClick={() => onSelect(getMeetingId(meeting))}><strong>{meeting.segmento || 'Meeting comercial'}</strong><small>{formatDate(meeting.dataMeeting)}</small><span>{meeting.status}</span></button>)}</section><section><article className="meeting-summary card"><div><p className="eyebrow">MEETING SELECIONADA</p><h2>{selected.segmento || 'Meeting comercial'}</h2><p>{formatDate(selected.dataMeeting)} · {formatDuration(selected.duracao)} · {selected.formato}</p></div><dl><div><dt>Status</dt><dd>{selected.status}</dd></div><div><dt>NPS</dt><dd>{selected.notaNps ?? '—'}</dd></div><div><dt>UF</dt><dd>{selected.uf || '—'}</dd></div></dl></article><TranscriptionPanel meeting={selected} transcricao={transcricao} onSave={onSaveTranscricao} onAnalyze={onAnalyze} processing={processing}/></section></div> : <EmptyState title="Nenhuma Meeting disponível" description="Cadastre uma reunião para iniciar o fluxo de análise." action={<button className="btn primary" onClick={onNew}>+ Nova reunião</button>}/>}</>;
}

function AnalysisPage({ meeting, transcricao, result, alerts, words, onBack, onAnalyze, processing }) {
  if (!meeting) return <EmptyState title="Escolha uma reunião" description="Selecione uma Meeting na central para visualizar ou solicitar a análise." action={<button className="btn primary" onClick={onBack}>Ir para reuniões</button>}/>;
  if (!transcricao) return <><header className="page-header"><div><h1>Análise da reunião</h1><p>Esta Meeting ainda não possui uma transcrição.</p></div></header><EmptyState title="Transcrição pendente" description="Cadastre o texto bruto da conversa antes de usar a análise automática." action={<button className="btn primary" onClick={onBack}>Adicionar transcrição</button>}/></>;
  if (!result) return <><header className="page-header"><div><p className="breadcrumb">Reuniões / Análise</p><h1>Análise da reunião</h1><p>{meeting.segmento || 'Meeting comercial'} · {formatDate(meeting.dataMeeting)} · {formatDuration(meeting.duracao)}</p></div></header><EmptyState title="Análise ainda não realizada" description="A API processará a transcrição, gerará o resumo, sentimento, risco de churn, alertas e palavras-chave." action={<button className="btn primary" disabled={processing} onClick={onAnalyze}>{processing ? 'Analisando...' : '✦ Analisar transcrição'}</button>}/></>;
  const opportunity = alerts.some((item) => item.tipoAlerta === 'Oportunidade');
  return <><header className="page-header"><div><p className="breadcrumb">Reuniões / Análise</p><h1>Análise da reunião</h1><p>{meeting.segmento || 'Meeting comercial'} · {formatDate(meeting.dataMeeting)} · {formatDuration(meeting.duracao)}</p></div><button className="btn secondary" onClick={onBack}>▤ Ver transcrição completa</button></header><section className="metrics-grid analysis-metrics"><MetricCard label="Risco de churn" value={`${Math.round(Number(result.riscoChurn || 0))}%`} caption={priorityLabel(result.riscoChurn)} tone="danger" icon="↓"/><MetricCard label="Oportunidade" value={opportunity ? 'Detectada' : '—'} caption={opportunity ? 'Sinal de expansão identificado' : 'Nenhuma oportunidade identificada'} tone="success" icon="★"/><MetricCard label="Sentimento geral" value={result.sentimento || '—'} caption="Baseado no tom da reunião" tone={sentimentTone(result.sentimento)} icon="☻"/><MetricCard label="Classificação" value={result.classificacao || '—'} caption="Resultado da inteligência" icon="✦"/></section><section className="card keywords-card"><p className="eyebrow">PALAVRAS-CHAVE MAIS RELEVANTES</p><div className="tag-list">{safeArray(words).length ? safeArray(words).map((word) => <span key={getId(word, 'idPalavraChave') || word.palavra} className="tag">{word.palavra}</span>) : <span className="muted">Nenhuma palavra-chave foi retornada para esta Meeting.</span>}</div></section><section className="insights-grid"><article className="card summary-card"><p className="eyebrow">RESUMO EXECUTIVO</p><h2>O que importa nesta conversa</h2><p>{result.resumo}</p></article><article className="card alerts-card"><p className="eyebrow">INSIGHTS IDENTIFICADOS</p><h2>Alertas comerciais</h2>{alerts.length ? <div className="alert-list">{alerts.map((alert) => <div className={`alert-item ${alert.tipoAlerta === 'Risco' ? 'risk' : 'opportunity'}`} key={getId(alert, 'idAlerta')}><span>{alert.tipoAlerta === 'Risco' ? '!' : '↗'}</span><div><strong>{alert.tipoAlerta}{alert.nivelRisco ? ` · ${alert.nivelRisco}` : ''}</strong><p>{alert.descricao}</p></div></div>)}</div> : <p className="muted">A API não retornou alertas vinculados a este resultado.</p>}</article></section></>;
}

function InsightsPage({ results, alertas, meetings, transcricoes, onOpen }) {
  const sentiments = countBy(results, (item) => item.sentimento);
  const keywordCounts = safeArray(meetings).flatMap((meeting) => safeArray(meeting.palavrasChave).map((word) => word.palavra)).reduce((counts, word) => ({ ...counts, [word]: (counts[word] || 0) + 1 }), {});
  const opportunityCount = safeArray(alertas).filter((alert) => alert.tipoAlerta === 'Oportunidade').length;
  const riskCount = safeArray(alertas).filter((alert) => alert.tipoAlerta === 'Risco').length;
  return <><header className="page-header"><div><p className="eyebrow">INTELIGÊNCIA COMERCIAL</p><h1>Insights comerciais</h1><p>Priorize riscos e oportunidades detectados automaticamente pela API.</p></div></header>{results.length ? <><section className="insight-summary"><MetricCard label="Alertas de risco" value={riskCount} caption="Alertas reais cadastrados" tone="danger" icon="!"/><MetricCard label="Oportunidades" value={opportunityCount} caption="Sinais de expansão detectados" tone="success" icon="↗"/><MetricCard label="Churn mais alto" value={`${Math.max(...results.map((result) => Number(result.riscoChurn || 0)), 0)}%`} caption="Maior risco entre as análises" tone="warning" icon="↓"/></section><section className="insight-analytics"><DistributionChart title="Mapa de sentimento" description="Leitura consolidada dos resultados analisados." values={sentiments}/><DistributionChart title="Palavras-chave recorrentes" description="Frequência nos vínculos Meeting ↔ Palavra-chave retornados pela API." values={keywordCounts} tone="green"/></section><section className="insights-board">{results.map((result) => { const meeting = meetingForResult(result, meetings, transcricoes); const related = getResultAlerts(result, alertas); return <article className="insight-board-card" key={getResultadoId(result)}><div className="insight-board-top"><span className={`classification ${String(result.classificacao || '').toLowerCase()}`}>{result.classificacao || 'Análise'}</span><span>{Math.round(Number(result.riscoChurn || 0))}% churn</span></div><h2>{meeting?.segmento || 'Meeting comercial'}</h2><p>{result.resumo || 'Resumo não disponível.'}</p><div className="compact-alerts">{related.length ? related.map((alert) => <span key={getId(alert, 'idAlerta')} className={alert.tipoAlerta === 'Risco' ? 'risk-text' : 'opportunity-text'}>{alert.tipoAlerta}{alert.nivelRisco ? `: ${alert.nivelRisco}` : ''}</span>) : <span className="muted">Sem alerta retornado neste resultado.</span>}</div>{meeting && <button className="text-button" onClick={() => onOpen(getMeetingId(meeting))}>Abrir análise →</button>}</article>; })}</section></> : <EmptyState title="Ainda não há insights" description="Eles aparecerão aqui após a análise de uma transcrição."/>}</>;
}

export default App;
