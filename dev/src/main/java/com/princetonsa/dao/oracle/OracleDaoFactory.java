/*
 * @(#)OracleDaoFactory.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_05
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.mercury.dao.odontologia.AntecedentesOdontologiaDao;
import com.mercury.dao.odontologia.CartaDentalDao;
import com.mercury.dao.odontologia.IndicePlacaDao;
import com.mercury.dao.odontologia.OdontogramaDao;
import com.mercury.dao.odontologia.TratamientoOdontologiaDao;
import com.mercury.dao.odontologia.ValoracionOdontologiaDao;
import com.mercury.dao.oracle.odontologia.OracleAntecedentesOdontologiaDao;
import com.mercury.dao.oracle.odontologia.OracleCartaDentalDao;
import com.mercury.dao.oracle.odontologia.OracleIndicePlacaDao;
import com.mercury.dao.oracle.odontologia.OracleOdontogramaDao;
import com.mercury.dao.oracle.odontologia.OracleTratamientoOdontologiaDao;
import com.mercury.dao.oracle.odontologia.OracleValoracionOdontologiaDao;
import com.princetonsa.dao.*;
import com.princetonsa.dao.administracion.ConceptosRetencionDao;
import com.princetonsa.dao.administracion.ConsecutivosCentroAtencionDao;
import com.princetonsa.dao.administracion.DetConceptosRetencionDao;
import com.princetonsa.dao.administracion.EnvioEmailAutomaticoDao;
import com.princetonsa.dao.administracion.EspecialidadesDao;
import com.princetonsa.dao.administracion.FactorConversionMonedasDao;
import com.princetonsa.dao.administracion.TiposMonedaDao;
import com.princetonsa.dao.administracion.TiposRetencionDao;
import com.princetonsa.dao.administracion.UtilidadesAdministracionDao;
import com.princetonsa.dao.agendaProcedimiento.ExamenCondiTomaDao;
import com.princetonsa.dao.agendaProcedimiento.UnidadProcedimientoDao;
import com.princetonsa.dao.capitacion.AjusteCxCDao;
import com.princetonsa.dao.capitacion.AprobacionAjustesDao;
import com.princetonsa.dao.capitacion.AsociarCxCAFacturasDao;
import com.princetonsa.dao.capitacion.CierresCarteraCapitacionDao;
import com.princetonsa.dao.capitacion.ContratoCargueDao;
import com.princetonsa.dao.capitacion.CuentaCobroCapitacionDao;
import com.princetonsa.dao.capitacion.EdadCarteraCapitacionDao;
import com.princetonsa.dao.capitacion.ExcepcionNivelDao;
import com.princetonsa.dao.capitacion.NivelAtencionDao;
import com.princetonsa.dao.capitacion.NivelAutorizacionAgrupacionArticuloDAO;
import com.princetonsa.dao.capitacion.NivelAutorizacionArticuloEspecificoDAO;
import com.princetonsa.dao.capitacion.PagosCapitacionDao;
import com.princetonsa.dao.capitacion.ProcesoNivelAutorizacionDao;
import com.princetonsa.dao.capitacion.RadicacionCuentasCobroCapitacionDao;
import com.princetonsa.dao.capitacion.RegistroSaldosInicialesCapitacionDao;
import com.princetonsa.dao.capitacion.SubirPacienteDao;
import com.princetonsa.dao.capitacion.UnidadPagoDao;
import com.princetonsa.dao.cargos.CargosAutomaticosPresupuestoDao;
import com.princetonsa.dao.cargos.CargosDao;
import com.princetonsa.dao.cargos.CargosDirectosDao;
import com.princetonsa.dao.cargos.CargosEntidadesSubcontratadasDao;
import com.princetonsa.dao.cargos.CargosOdonDao;
import com.princetonsa.dao.cargos.GeneracionCargosPendientesDao;
import com.princetonsa.dao.cargos.UtilidadJustificacionPendienteArtServDao;
import com.princetonsa.dao.cartera.EdadCarteraDao;
import com.princetonsa.dao.cartera.RecaudoCarteraEventoDao;
import com.princetonsa.dao.cartera.ReporteFacturacionEventoRadicarDao;
import com.princetonsa.dao.cartera.ReportesEstadosCarteraDao;
import com.princetonsa.dao.carterapaciente.ApliPagosCarteraPacienteDao;
import com.princetonsa.dao.carterapaciente.AutorizacionIngresoPacienteSaldoMoraDao;
import com.princetonsa.dao.carterapaciente.CierreSaldoInicialCarteraPacienteDao;
import com.princetonsa.dao.carterapaciente.ConsultarRefinanciarCuotaPacienteDao;
import com.princetonsa.dao.carterapaciente.DatosFinanciacionDao;
import com.princetonsa.dao.carterapaciente.DocumentosGarantiaDao;
import com.princetonsa.dao.carterapaciente.EdadCarteraPacienteDao;
import com.princetonsa.dao.carterapaciente.ExtractoDeudoresCPDao;
import com.princetonsa.dao.carterapaciente.PazYSalvoCarteraPacienteDao;
import com.princetonsa.dao.carterapaciente.ReporteDocumentosCarteraPacienteDao;
import com.princetonsa.dao.carterapaciente.ReportePagosCarteraPacienteDao;
import com.princetonsa.dao.carterapaciente.SaldosInicialesCarteraPacienteDao;
import com.princetonsa.dao.consultaExterna.AnulacionCondonacionMultasDao;
import com.princetonsa.dao.consultaExterna.AsignarCitasControlPostOperatorioDao;
import com.princetonsa.dao.consultaExterna.ConsultaMultasAgendaCitasDao;
import com.princetonsa.dao.consultaExterna.ExcepcionesHorarioAtencionDao;
import com.princetonsa.dao.consultaExterna.MotivosAnulacionCondonacionDao;
import com.princetonsa.dao.consultaExterna.MotivosCancelacionCitaDao;
import com.princetonsa.dao.consultaExterna.MotivosCitaDao;
import com.princetonsa.dao.consultaExterna.MultasDao;
import com.princetonsa.dao.consultaExterna.ReporteEstadisticoConsultaExDao;
import com.princetonsa.dao.consultaExterna.ServiAdicionalesXProfAtenOdontoDao;
import com.princetonsa.dao.consultaExterna.UnidadAgendaUsuarioCentroDao;
import com.princetonsa.dao.consultaExterna.UtilidadesConsultaExternaDao;
import com.princetonsa.dao.enfermeria.ConsultaProgramacionCuidadosAreaDao;
import com.princetonsa.dao.enfermeria.ConsultaProgramacionCuidadosPacienteDao;
import com.princetonsa.dao.enfermeria.ProgramacionAreaPacienteDao;
import com.princetonsa.dao.enfermeria.ProgramacionCuidadoEnferDao;
import com.princetonsa.dao.enfermeria.RegistroEnfermeriaDao;
import com.princetonsa.dao.facturacion.AbonosYDescuentosDao;
import com.princetonsa.dao.facturacion.ActualizacionAutomaticaEsquemaTarifarioDao;
import com.princetonsa.dao.facturacion.AnulacionCargosFarmaciaDao;
import com.princetonsa.dao.facturacion.AnulacionFacturasDao;
import com.princetonsa.dao.facturacion.ArchivoPlanoColsaDao;
import com.princetonsa.dao.facturacion.AutorizarAnulacionFacturasDao;
import com.princetonsa.dao.facturacion.CalculoHonorariosPoolesDao;
import com.princetonsa.dao.facturacion.CalculoValorCobrarPacienteDao;
import com.princetonsa.dao.facturacion.CargosDirectosCxDytDao;
import com.princetonsa.dao.facturacion.CentrosCostoEntidadesSubcontratadasDao;
import com.princetonsa.dao.facturacion.CoberturaDao;
import com.princetonsa.dao.facturacion.CoberturasConvenioDao;
import com.princetonsa.dao.facturacion.CoberturasEntidadesSubcontratadasDao;
import com.princetonsa.dao.facturacion.ComponentesPaquetesDao;
import com.princetonsa.dao.facturacion.ConceptosPagoPoolesDao;
import com.princetonsa.dao.facturacion.ConceptosPagoPoolesXConvenioDao;
import com.princetonsa.dao.facturacion.CondicionesXServicioDao;
import com.princetonsa.dao.facturacion.ConsolidadoFacturacionDao;
import com.princetonsa.dao.facturacion.ConsultaPaquetesFacturadosDao;
import com.princetonsa.dao.facturacion.ConsultaTarifasArticulosDao;
import com.princetonsa.dao.facturacion.ConsultaTarifasDao;
import com.princetonsa.dao.facturacion.ConsultarContratosEntidadesSubcontratadasDao;
import com.princetonsa.dao.facturacion.ConsumosFacturadosDao;
import com.princetonsa.dao.facturacion.ConsumosPorFacturarPacientesHospitalizadosDao;
import com.princetonsa.dao.facturacion.ConsumosXFacturarDao;
import com.princetonsa.dao.facturacion.ContactosEmpresaDao;
import com.princetonsa.dao.facturacion.ControlAnticipoContratoDao;
import com.princetonsa.dao.facturacion.CopiarTarifasEsquemaTarifarioDao;
import com.princetonsa.dao.facturacion.CostoInventarioPorFacturarDao;
import com.princetonsa.dao.facturacion.CostoVentasPorInventarioDao;
import com.princetonsa.dao.facturacion.DescuentosComercialesDao;
import com.princetonsa.dao.facturacion.DetalleCoberturaDao;
import com.princetonsa.dao.facturacion.DetalleInclusionesExclusionesDao;
import com.princetonsa.dao.facturacion.DistribucionCuentaDao;
import com.princetonsa.dao.facturacion.EntidadesSubContratadasDao;
import com.princetonsa.dao.facturacion.ExcepcionesCCInterconsultasDao;
import com.princetonsa.dao.facturacion.ExcepcionesTarifas1Dao;
import com.princetonsa.dao.facturacion.FacturaDao;
import com.princetonsa.dao.facturacion.FacturaOdontologicaDao;
import com.princetonsa.dao.facturacion.FacturadosPorConvenioDao;
import com.princetonsa.dao.facturacion.GeneracionArchivoPlanoIndicadoresCalidadDao;
import com.princetonsa.dao.facturacion.HistoMontoAgrupacionArticuloDAO;
import com.princetonsa.dao.facturacion.HonorariosPoolDao;
import com.princetonsa.dao.facturacion.ImpresionSoportesFacturasDao;
import com.princetonsa.dao.facturacion.InclusionExclusionConvenioDao;
import com.princetonsa.dao.facturacion.InclusionesExclusionesDao;
import com.princetonsa.dao.facturacion.IngresarModificarContratosEntidadesSubcontratadasDao;
import com.princetonsa.dao.facturacion.LogControlAnticipoContratoDao;
import com.princetonsa.dao.facturacion.MontoAgrupacionArticuloDAO;
import com.princetonsa.dao.facturacion.MontoArticuloEspecificoDAO;
import com.princetonsa.dao.facturacion.PacientesPorFacturarDao;
import com.princetonsa.dao.facturacion.PaquetesConvenioDao;
import com.princetonsa.dao.facturacion.PaquetesDao;
import com.princetonsa.dao.facturacion.PaquetizacionDao;
import com.princetonsa.dao.facturacion.ParamArchivoPlanoColsanitasDao;
import com.princetonsa.dao.facturacion.ParamArchivoPlanoIndCalidadDao;
import com.princetonsa.dao.facturacion.PendienteFacturarDao;
import com.princetonsa.dao.facturacion.RegistroRipsCargosDirectosDao;
import com.princetonsa.dao.facturacion.ReliquidacionTarifasDao;
import com.princetonsa.dao.facturacion.ReporteProcedimientosEsteticosDao;
import com.princetonsa.dao.facturacion.RevisionCuentaDao;
import com.princetonsa.dao.facturacion.ServiciosGruposEsteticosDao;
import com.princetonsa.dao.facturacion.Servicios_ArticulosIncluidosEnOtrosProcedimientosDao;
import com.princetonsa.dao.facturacion.SolicitarAnulacionFacturasDao;
import com.princetonsa.dao.facturacion.SoportesFacturasDao;
import com.princetonsa.dao.facturacion.TiposConvenioDao;
import com.princetonsa.dao.facturacion.TotalFacturadoConvenioContratoDao;
import com.princetonsa.dao.facturacion.TrasladoSolicitudesPorTransplantesDao;
import com.princetonsa.dao.facturacion.UsuariosAutorizarAnulacionFacturasDao;
import com.princetonsa.dao.facturacion.UtilidadesFacturacionDao;
import com.princetonsa.dao.facturacion.ValidacionesFacturaDao;
import com.princetonsa.dao.facturacion.VentasCentroCostoDao;
import com.princetonsa.dao.facturacion.VentasEmpresaConvenioDao;
import com.princetonsa.dao.facturasVarias.AprobacionAnulacionAjustesFacturasVariasDao;
import com.princetonsa.dao.facturasVarias.AprobacionAnulacionFacturasVariasDao;
import com.princetonsa.dao.facturasVarias.AprobacionAnulacionPagosFacturasVariasDao;
import com.princetonsa.dao.facturasVarias.ConceptosFacturasVariasDao;
import com.princetonsa.dao.facturasVarias.ConsultaFacturasVariasDao;
import com.princetonsa.dao.facturasVarias.ConsultaImpresionAjustesFacturasVariasDao;
import com.princetonsa.dao.facturasVarias.ConsultaImpresionPagosFacturasVariasDao;
import com.princetonsa.dao.facturasVarias.ConsultaMovimientoDeudorDao;
import com.princetonsa.dao.facturasVarias.ConsultaMovimientoFacturaDao;
import com.princetonsa.dao.facturasVarias.DeudoresDao;
import com.princetonsa.dao.facturasVarias.GenModFacturasVariasDao;
import com.princetonsa.dao.facturasVarias.GeneracionModificacionAjustesFacturasVariasDao;
import com.princetonsa.dao.facturasVarias.PagosFacturasVariasDao;
import com.princetonsa.dao.facturasVarias.UtilidadesFacturasVariasDao;
import com.princetonsa.dao.glosas.AprobarAnularRespuestasDao;
import com.princetonsa.dao.glosas.AprobarGlosasDao;
import com.princetonsa.dao.glosas.ConceptosEspecificosDao;
import com.princetonsa.dao.glosas.ConceptosGeneralesDao;
import com.princetonsa.dao.glosas.ConceptosRespuestasDao;
import com.princetonsa.dao.glosas.ConfirmarAnularGlosasDao;
import com.princetonsa.dao.glosas.ConsultarImpFacAudiDao;
import com.princetonsa.dao.glosas.ConsultarImprimirGlosasDao;
import com.princetonsa.dao.glosas.ConsultarImprimirGlosasSinRespuestaDao;
import com.princetonsa.dao.glosas.ConsultarImprimirRespuestasDao;
import com.princetonsa.dao.glosas.DetalleGlosasDao;
import com.princetonsa.dao.glosas.EdadGlosaXFechaRadicacionDao;
import com.princetonsa.dao.glosas.GlosasDao;
import com.princetonsa.dao.glosas.ParametrosFirmasImpresionRespDao;
import com.princetonsa.dao.glosas.RadicarRespuestasDao;
import com.princetonsa.dao.glosas.RegistrarConciliacionDao;
import com.princetonsa.dao.glosas.RegistrarModificarGlosasDao;
import com.princetonsa.dao.glosas.RegistrarRespuestaDao;
import com.princetonsa.dao.glosas.ReporteEstadoCarteraGlosasDao;
import com.princetonsa.dao.glosas.ReporteFacturasReiteradasDao;
import com.princetonsa.dao.glosas.ReporteFacturasVencidasNoObjetadasDao;
import com.princetonsa.dao.glosas.UtilidadesGlosasDao;
import com.princetonsa.dao.historiaClinica.CategoriasTriageDao;
import com.princetonsa.dao.historiaClinica.ConsentimientoInformadoDao;
import com.princetonsa.dao.historiaClinica.ConsultaIngresosHospitalDiaDao;
import com.princetonsa.dao.historiaClinica.ConsultaReferenciaContrareferenciaDao;
import com.princetonsa.dao.historiaClinica.ConsultaTerapiasGrupalesDao;
import com.princetonsa.dao.historiaClinica.ContrarreferenciaDao;
import com.princetonsa.dao.historiaClinica.CrecimientoDesarrolloDao;
import com.princetonsa.dao.historiaClinica.DestinoTriageDao;
import com.princetonsa.dao.historiaClinica.DiagnosticosOdontologicosATratarDao;
import com.princetonsa.dao.historiaClinica.EscalasDao;
import com.princetonsa.dao.historiaClinica.EventosAdversosDao;
import com.princetonsa.dao.historiaClinica.EvolucionesDao;
import com.princetonsa.dao.historiaClinica.HistoricoAtencionesDao;
import com.princetonsa.dao.historiaClinica.ImagenesBaseDao;
import com.princetonsa.dao.historiaClinica.ImpresionCLAPDao;
import com.princetonsa.dao.historiaClinica.ImpresionResumenAtencionesDao;
import com.princetonsa.dao.historiaClinica.InformacionPartoDao;
import com.princetonsa.dao.historiaClinica.InformacionRecienNacidosDao;
import com.princetonsa.dao.historiaClinica.InstitucionesSircDao;
import com.princetonsa.dao.historiaClinica.JustificacionNoPosServDao;
import com.princetonsa.dao.historiaClinica.MotivosSircDao;
import com.princetonsa.dao.historiaClinica.ParametrizacionComponentesDao;
import com.princetonsa.dao.historiaClinica.ParametrizacionCurvaAlertaDao;
import com.princetonsa.dao.historiaClinica.ParametrizacionPlantillasDao;
import com.princetonsa.dao.historiaClinica.PlantillasDao;
import com.princetonsa.dao.historiaClinica.ReferenciaDao;
import com.princetonsa.dao.historiaClinica.RegistroEventosAdversosDao;
import com.princetonsa.dao.historiaClinica.RegistroResumenParcialHistoriaClinicaDao;
import com.princetonsa.dao.historiaClinica.RegistroTerapiasGrupalesDao;
import com.princetonsa.dao.historiaClinica.ReporteReferenciaExternaDao;
import com.princetonsa.dao.historiaClinica.ReporteReferenciaInternaDao;
import com.princetonsa.dao.historiaClinica.ServiciosSircDao;
import com.princetonsa.dao.historiaClinica.ServiciosXTipoTratamientoOdontologicoDao;
import com.princetonsa.dao.historiaClinica.SignosSintomasDao;
import com.princetonsa.dao.historiaClinica.SignosSintomasXSistemaDao;
import com.princetonsa.dao.historiaClinica.SistemasMotivoConsultaDao;
import com.princetonsa.dao.historiaClinica.TextosRespuestaProcedimientosDao;
import com.princetonsa.dao.historiaClinica.TiposTratamientosOdontologicosDao;
import com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao;
import com.princetonsa.dao.historiaClinica.UtilidadesJustificacionNoPosDao;
import com.princetonsa.dao.historiaClinica.ValoracionPacientesCuidadosEspecialesDao;
import com.princetonsa.dao.historiaClinica.ValoracionesDao;
import com.princetonsa.dao.historiaClinica.VerResumenOdontologicoDao;
import com.princetonsa.dao.interfaz.CampoInterfazDao;
import com.princetonsa.dao.interfaz.ConsultaInterfazSistema1EDao;
import com.princetonsa.dao.interfaz.CuentaInvUnidadFunDao;
import com.princetonsa.dao.interfaz.CuentaInventarioDao;
import com.princetonsa.dao.interfaz.CuentaServicioDao;
import com.princetonsa.dao.interfaz.CuentaUnidadFuncionalDao;
import com.princetonsa.dao.interfaz.CuentasConveniosDao;
import com.princetonsa.dao.interfaz.DesmarcarDocProcesadosDao;
import com.princetonsa.dao.interfaz.GeneracionInterfaz1EDao;
import com.princetonsa.dao.interfaz.GeneracionInterfazDao;
import com.princetonsa.dao.interfaz.InterfazSistemaUnoDao;
import com.princetonsa.dao.interfaz.ParamInterfazDao;
import com.princetonsa.dao.interfaz.ParamInterfazSistema1EDao;
import com.princetonsa.dao.interfaz.ReporteMovTipoDocDao;
import com.princetonsa.dao.interfaz.UtilidadesInterfazDao;
import com.princetonsa.dao.inventarios.AjustesXInventarioFisicoDao;
import com.princetonsa.dao.inventarios.AlmacenParametrosDao;
import com.princetonsa.dao.inventarios.ArticuloCatalogoDao;
import com.princetonsa.dao.inventarios.ArticulosConsumidosPacientesDao;
import com.princetonsa.dao.inventarios.ArticulosFechaVencimientoDao;
import com.princetonsa.dao.inventarios.ArticulosPorAlmacenDao;
import com.princetonsa.dao.inventarios.ArticulosPuntoPedidoDao;
import com.princetonsa.dao.inventarios.ArticulosXMezclaDao;
import com.princetonsa.dao.inventarios.CierresInventarioDao;
import com.princetonsa.dao.inventarios.ComparativoUltimoConteoDao;
import com.princetonsa.dao.inventarios.ConsultaAjustesEmpresaDao;
import com.princetonsa.dao.inventarios.ConsultaCostoArticulosDao;
import com.princetonsa.dao.inventarios.ConsultaDevolucionInventarioPacienteDao;
import com.princetonsa.dao.inventarios.ConsultaDevolucionPedidosDao;
import com.princetonsa.dao.inventarios.ConsultaImpresionTrasladosDao;
import com.princetonsa.dao.inventarios.ConsultaInventarioFisicoArticulosDao;
import com.princetonsa.dao.inventarios.ConsultaMovimientosConsignacionesDao;
import com.princetonsa.dao.inventarios.ConsultaSaldosCierresInventariosDao;
import com.princetonsa.dao.inventarios.ConsumosCentrosCostoDao;
import com.princetonsa.dao.inventarios.CostoInventariosPorAlmacenDao;
import com.princetonsa.dao.inventarios.DespachoPedidoQxDao;
import com.princetonsa.dao.inventarios.DespachoTrasladoAlmacenDao;
import com.princetonsa.dao.inventarios.DevolucionInventariosPacienteDao;
import com.princetonsa.dao.inventarios.EquivalentesDeInventarioDao;
import com.princetonsa.dao.inventarios.ExistenciasInventariosDao;
import com.princetonsa.dao.inventarios.FarmaciaCentroCostoDao;
import com.princetonsa.dao.inventarios.FormaFarmaceuticaDao;
import com.princetonsa.dao.inventarios.FormatoJustArtNoposDao;
import com.princetonsa.dao.inventarios.FormatoJustServNoposDao;
import com.princetonsa.dao.inventarios.GrupoEspecialArticulosDao;
import com.princetonsa.dao.inventarios.ImpListaConteoDao;
import com.princetonsa.dao.inventarios.KardexDao;
import com.princetonsa.dao.inventarios.MedicamentosControladosAdministradosDao;
import com.princetonsa.dao.inventarios.MezclasDao;
import com.princetonsa.dao.inventarios.MotDevolucionInventariosDao;
import com.princetonsa.dao.inventarios.MovimientosAlmacenConsignacionDao;
import com.princetonsa.dao.inventarios.MovimientosAlmacenesDao;
import com.princetonsa.dao.inventarios.NaturalezaArticulosDao;
import com.princetonsa.dao.inventarios.PreparacionTomaInventarioDao;
import com.princetonsa.dao.inventarios.RegistroConteoInventarioDao;
import com.princetonsa.dao.inventarios.RegistroTransaccionesDao;
import com.princetonsa.dao.inventarios.SeccionesDao;
import com.princetonsa.dao.inventarios.SolicitudMedicamentosXDespacharDao;
import com.princetonsa.dao.inventarios.SolicitudTrasladoAlmacenDao;
import com.princetonsa.dao.inventarios.SustitutosNoPosDao;
import com.princetonsa.dao.inventarios.TiposInventarioDao;
import com.princetonsa.dao.inventarios.TiposTransaccionesInvDao;
import com.princetonsa.dao.inventarios.TransaccionesValidasXCCDao;
import com.princetonsa.dao.inventarios.UnidadMedidaDao;
import com.princetonsa.dao.inventarios.UsuariosXAlmacenDao;
import com.princetonsa.dao.inventarios.UtilidadInventariosDao;
import com.princetonsa.dao.laboratorio.UtilidadLaboratoriosDao;
import com.princetonsa.dao.manejoPaciente.AperturaIngresosDao;
import com.princetonsa.dao.manejoPaciente.AsignacionCamaCuidadoEspecialAPisoDao;
import com.princetonsa.dao.manejoPaciente.AsignacionCamaCuidadoEspecialDao;
import com.princetonsa.dao.manejoPaciente.AutorizacionesDao;
import com.princetonsa.dao.manejoPaciente.AutorizacionesEntidadesSubcontratadasDao;
import com.princetonsa.dao.manejoPaciente.CalidadAtencionDao;
import com.princetonsa.dao.manejoPaciente.CategoriaAtencionDao;
import com.princetonsa.dao.manejoPaciente.CensoCamasDao;
import com.princetonsa.dao.manejoPaciente.CierreIngresoDao;
import com.princetonsa.dao.manejoPaciente.ConsultaCierreAperturaIngresoDao;
import com.princetonsa.dao.manejoPaciente.ConsultaEnvioInconsistenciasenBDDao;
import com.princetonsa.dao.manejoPaciente.ConsultaPreingresosDao;
import com.princetonsa.dao.manejoPaciente.ConsultarActivarCamasReservadasDao;
import com.princetonsa.dao.manejoPaciente.ConsultarAdmisionDao;
import com.princetonsa.dao.manejoPaciente.ConsultarImprimirAutorizacionesEntSubcontratadasDao;
import com.princetonsa.dao.manejoPaciente.ConsultarIngresosPorTransplantesDao;
import com.princetonsa.dao.manejoPaciente.ConsultoriosDao;
import com.princetonsa.dao.manejoPaciente.EncuestaCalidadAtencionDao;
import com.princetonsa.dao.manejoPaciente.EstadisticasIngresosDao;
import com.princetonsa.dao.manejoPaciente.EstadisticasServiciosDao;
import com.princetonsa.dao.manejoPaciente.FosygaDao;
import com.princetonsa.dao.manejoPaciente.GeneracionAnexosForecatDao;
import com.princetonsa.dao.manejoPaciente.GeneracionTarifasPendientesEntSubDao;
import com.princetonsa.dao.manejoPaciente.HabitacionesDao;
import com.princetonsa.dao.manejoPaciente.LecturaPlanosEntidadesDao;
import com.princetonsa.dao.manejoPaciente.LiberacionCamasIngresosCerradosDao;
import com.princetonsa.dao.manejoPaciente.ListadoCamasHospitalizacionDao;
import com.princetonsa.dao.manejoPaciente.ListadoIngresosDao;
import com.princetonsa.dao.manejoPaciente.MotivoCierreAperturaIngresosDao;
import com.princetonsa.dao.manejoPaciente.MotivosSatisfaccionInsatisfaccionDao;
import com.princetonsa.dao.manejoPaciente.OcupacionDiariaCamasDao;
import com.princetonsa.dao.manejoPaciente.PacientesConAtencionDao;
import com.princetonsa.dao.manejoPaciente.PacientesConEgresoPorFacturarDao;
import com.princetonsa.dao.manejoPaciente.PacientesEntidadesSubConDao;
import com.princetonsa.dao.manejoPaciente.PacientesHospitalizadosDao;
import com.princetonsa.dao.manejoPaciente.ParametrosEntidadesSubContratadasDao;
import com.princetonsa.dao.manejoPaciente.PerfilNEDDao;
import com.princetonsa.dao.manejoPaciente.PisosDao;
import com.princetonsa.dao.manejoPaciente.PlanosFURIPSDao;
import com.princetonsa.dao.manejoPaciente.ProrrogarAnularAutorizacionesEntSubcontratadasDao;
import com.princetonsa.dao.manejoPaciente.RegionesCoberturaDao;
import com.princetonsa.dao.manejoPaciente.RegistroAccidentesTransitoDao;
import com.princetonsa.dao.manejoPaciente.RegistroEnvioInfAtencionIniUrgDao;
import com.princetonsa.dao.manejoPaciente.RegistroEnvioInformInconsisenBDDao;
import com.princetonsa.dao.manejoPaciente.RegistroEventosCatastroficosDao;
import com.princetonsa.dao.manejoPaciente.ReingresoSalidaHospiDiaDao;
import com.princetonsa.dao.manejoPaciente.ReporteEgresosEstanciasDao;
import com.princetonsa.dao.manejoPaciente.ReporteMortalidadDao;
import com.princetonsa.dao.manejoPaciente.TipoHabitacionDao;
import com.princetonsa.dao.manejoPaciente.TiposAmbulanciaDao;
import com.princetonsa.dao.manejoPaciente.TiposUsuarioCamaDao;
import com.princetonsa.dao.manejoPaciente.TotalOcupacionCamasDao;
import com.princetonsa.dao.manejoPaciente.TrasladoCentroAtencionDao;
import com.princetonsa.dao.manejoPaciente.UtilidadesManejoPacienteDao;
import com.princetonsa.dao.manejoPaciente.ValoresTipoReporteDao;
import com.princetonsa.dao.manejoPaciente.ViasIngresoDao;
import com.princetonsa.dao.odontologia.AgendaOdontologicaDao;
import com.princetonsa.dao.odontologia.AliadoOdontologicoDao;
import com.princetonsa.dao.odontologia.AperturaCuentaPacienteOdontologicoDao;
import com.princetonsa.dao.odontologia.AtencionCitasOdontologiaDao;
import com.princetonsa.dao.odontologia.AutorizacionDescuentosOdonDao;
import com.princetonsa.dao.odontologia.BeneficiarioTarjetaClienteDao;
import com.princetonsa.dao.odontologia.CancelarAgendaOdontoDao;
import com.princetonsa.dao.odontologia.CitaOdontologicaDao;
import com.princetonsa.dao.odontologia.ConvencionesOdontologicasDao;
import com.princetonsa.dao.odontologia.DescuentoOdontologicoDao;
import com.princetonsa.dao.odontologia.DetCaPromocionesOdoDao;
import com.princetonsa.dao.odontologia.DetConvPromocionesOdoDao;
import com.princetonsa.dao.odontologia.DetPromocionesOdoDao;
import com.princetonsa.dao.odontologia.DetalleAgrupacionHonorarioDao;
import com.princetonsa.dao.odontologia.DetalleDescuentoOdontologicoDao;
import com.princetonsa.dao.odontologia.DetalleEmisionTarjetaClienteDao;
import com.princetonsa.dao.odontologia.DetalleHallazgoProgramaServicioDao;
import com.princetonsa.dao.odontologia.DetalleServicioHonorariosDao;
import com.princetonsa.dao.odontologia.EmisionBonosDescDao;
import com.princetonsa.dao.odontologia.EmisionTarjetaClienteDao;
import com.princetonsa.dao.odontologia.EvolucionOdontologicaDao;
import com.princetonsa.dao.odontologia.GenerarAgendaOdontologicaDao;
import com.princetonsa.dao.odontologia.HallazgoVsProgramaServicioDao;
import com.princetonsa.dao.odontologia.HallazgosOdontologicosDao;
import com.princetonsa.dao.odontologia.HistoricoDescuentoOdontologicoDao;
import com.princetonsa.dao.odontologia.HistoricoDetalleDescuentoOdontologico;
import com.princetonsa.dao.odontologia.HistoricoDetalleEmisionTarjetaClienteDao;
import com.princetonsa.dao.odontologia.HistoricoEmisionTarjetaClienteDao;
import com.princetonsa.dao.odontologia.IngresoPacienteOdontologiaDao;
import com.princetonsa.dao.odontologia.MotivosAtencionOdontologicaDao;
import com.princetonsa.dao.odontologia.MotivosDescuentosDao;
import com.princetonsa.dao.odontologia.NumeroSuperficiesPresupuestoDao;
import com.princetonsa.dao.odontologia.PacientesConvenioOdontologiaDao;
import com.princetonsa.dao.odontologia.ParentezcoDao;
import com.princetonsa.dao.odontologia.PlanTratamientoDao;
import com.princetonsa.dao.odontologia.PresupuestoContratadoDao;
import com.princetonsa.dao.odontologia.PresupuestoCuotasEspecialidadDao;
import com.princetonsa.dao.odontologia.PresupuestoExclusionesInclusionesDao;
import com.princetonsa.dao.odontologia.PresupuestoLogImpresionDao;
import com.princetonsa.dao.odontologia.PresupuestoOdontologicoDao;
import com.princetonsa.dao.odontologia.PresupuestoPaquetesDao;
import com.princetonsa.dao.odontologia.ProcesosAutomaticosOdontologiaDao;
import com.princetonsa.dao.odontologia.ProgramaDao;
import com.princetonsa.dao.odontologia.ProgramasOdontologicosDao;
import com.princetonsa.dao.odontologia.PromocionesOdontologicasDao;
import com.princetonsa.dao.odontologia.ReasignarProfesionalOdontoDao;
import com.princetonsa.dao.odontologia.ReporteCitasOdontologicasDao;
import com.princetonsa.dao.odontologia.ServicioHonorariosDao;
import com.princetonsa.dao.odontologia.TarjetaClienteDao;
import com.princetonsa.dao.odontologia.TiposDeUsuarioDao;
import com.princetonsa.dao.odontologia.ValidacionesPresupuestoDao;
import com.princetonsa.dao.odontologia.ValoracionOdontologicaDao;
import com.princetonsa.dao.odontologia.VentasTarjetasClienteDao;
import com.princetonsa.dao.oracle.administracion.OracleConceptosRetencionDao;
import com.princetonsa.dao.oracle.administracion.OracleConsecutivosCentroAtencionDao;
import com.princetonsa.dao.oracle.administracion.OracleDetConceptosRetencionDao;
import com.princetonsa.dao.oracle.administracion.OracleEnvioEmailAutomaticoDao;
import com.princetonsa.dao.oracle.administracion.OracleEspecialidadesDao;
import com.princetonsa.dao.oracle.administracion.OracleFactorConversionMonedasDao;
import com.princetonsa.dao.oracle.administracion.OracleTiposMonedaDao;
import com.princetonsa.dao.oracle.administracion.OracleTiposRetencionDao;
import com.princetonsa.dao.oracle.administracion.OracleUtilidadesAdministracionDao;
import com.princetonsa.dao.oracle.agendaProcedimiento.OracleExamenCondiTomaDao;
import com.princetonsa.dao.oracle.agendaProcedimiento.OracleUnidadProcedimientoDao;
import com.princetonsa.dao.oracle.capitacion.OracleAjusteCxCDao;
import com.princetonsa.dao.oracle.capitacion.OracleAprobacionAjustesDao;
import com.princetonsa.dao.oracle.capitacion.OracleAsociarCxCAFacturasDao;
import com.princetonsa.dao.oracle.capitacion.OracleCierresCarteraCapitacionDao;
import com.princetonsa.dao.oracle.capitacion.OracleContratoCargueDao;
import com.princetonsa.dao.oracle.capitacion.OracleCuentaCobroCapitacionDao;
import com.princetonsa.dao.oracle.capitacion.OracleEdadCarteraCapitacionDao;
import com.princetonsa.dao.oracle.capitacion.OracleExcepcionNivelDao;
import com.princetonsa.dao.oracle.capitacion.OracleNivelAtencionDao;
import com.princetonsa.dao.oracle.capitacion.OracleNivelAutorizacionAgrupacionArticuloDAO;
import com.princetonsa.dao.oracle.capitacion.OracleNivelAutorizacionArticuloEspecificoDAO;
import com.princetonsa.dao.oracle.capitacion.OraclePagosCapitacionDao;
import com.princetonsa.dao.oracle.capitacion.OracleProcesoNivelAutorizacionDao;
import com.princetonsa.dao.oracle.capitacion.OracleRadicacionCuentasCobroCapitacionDao;
import com.princetonsa.dao.oracle.capitacion.OracleRegistroSaldosInicialesCapitacionDao;
import com.princetonsa.dao.oracle.capitacion.OracleSubirPacienteDao;
import com.princetonsa.dao.oracle.capitacion.OracleUnidadPagoDao;
import com.princetonsa.dao.oracle.cargos.OracleCargosAutomaticosPresupuestoDao;
import com.princetonsa.dao.oracle.cargos.OracleCargosDao;
import com.princetonsa.dao.oracle.cargos.OracleCargosDirectosDao;
import com.princetonsa.dao.oracle.cargos.OracleCargosEntidadesSubcontratadasDao;
import com.princetonsa.dao.oracle.cargos.OracleCargosOdonDao;
import com.princetonsa.dao.oracle.cargos.OracleGeneracionCargosPendientesDao;
import com.princetonsa.dao.oracle.cargos.OracleUtilidadJustificacionPendienteArtServDao;
import com.princetonsa.dao.oracle.cartera.OracleEdadCarteraDao;
import com.princetonsa.dao.oracle.cartera.OracleRecaudoCarteraEventoDao;
import com.princetonsa.dao.oracle.cartera.OracleReporteFacturacionEventoRadicarDao;
import com.princetonsa.dao.oracle.cartera.OracleReportesEstadosCarteraDao;
import com.princetonsa.dao.oracle.carteraPaciente.OracleApliPagosCarteraPacienteDao;
import com.princetonsa.dao.oracle.carteraPaciente.OracleAutorizacionIngresoPacienteSaldoMoraDao;
import com.princetonsa.dao.oracle.carteraPaciente.OracleCierreSaldoInicialCarteraPacienteDao;
import com.princetonsa.dao.oracle.carteraPaciente.OracleConsultarRefinanciarCuotaPacienteDao;
import com.princetonsa.dao.oracle.carteraPaciente.OracleDatosFinanciacionDao;
import com.princetonsa.dao.oracle.carteraPaciente.OracleDocumentosGarantiaDao;
import com.princetonsa.dao.oracle.carteraPaciente.OracleEdadCarteraPacienteDao;
import com.princetonsa.dao.oracle.carteraPaciente.OracleExtractoDeudoresCPDao;
import com.princetonsa.dao.oracle.carteraPaciente.OraclePazYSalvoCarteraPacienteDao;
import com.princetonsa.dao.oracle.carteraPaciente.OracleReporteDocumentosCarteraPacienteDao;
import com.princetonsa.dao.oracle.carteraPaciente.OracleReportePagosCarteraPacienteDao;
import com.princetonsa.dao.oracle.carteraPaciente.OracleSaldosInicialesCarteraPacienteDao;
import com.princetonsa.dao.oracle.consultaExterna.OracleAnulacionCondonacionMultasDao;
import com.princetonsa.dao.oracle.consultaExterna.OracleAsignarCitasControlPostOperatorioDao;
import com.princetonsa.dao.oracle.consultaExterna.OracleExcepcionesHorarioAtencionDao;
import com.princetonsa.dao.oracle.consultaExterna.OracleMotivosAnulacionCondonacionDao;
import com.princetonsa.dao.oracle.consultaExterna.OracleMotivosCancelacionCitaDao;
import com.princetonsa.dao.oracle.consultaExterna.OracleMotivosCitaDao;
import com.princetonsa.dao.oracle.consultaExterna.OracleMultasDao;
import com.princetonsa.dao.oracle.consultaExterna.OracleReporteEstadisticoConsultaExDao;
import com.princetonsa.dao.oracle.consultaExterna.OracleServiAdicionalesXProfAtenOdontoDao;
import com.princetonsa.dao.oracle.consultaExterna.OracleUnidadAgendaUsuarioCentroDao;
import com.princetonsa.dao.oracle.consultaExterna.OracleUtilidadesConsultaExternaDao;
import com.princetonsa.dao.oracle.enfermeria.OracleConsultaProgramacionCuidadosAreaDao;
import com.princetonsa.dao.oracle.enfermeria.OracleConsultaProgramacionCuidadosPacienteDao;
import com.princetonsa.dao.oracle.enfermeria.OracleProgramacionAreaPacienteDao;
import com.princetonsa.dao.oracle.enfermeria.OracleProgramacionCuidadoEnferDao;
import com.princetonsa.dao.oracle.enfermeria.OracleRegistroEnfermeriaDao;
import com.princetonsa.dao.oracle.facturacion.OracleAbonosYDescuentosDao;
import com.princetonsa.dao.oracle.facturacion.OracleActualizacionAutomaticaEsquemaTarifarioDao;
import com.princetonsa.dao.oracle.facturacion.OracleAnulacionCargosFarmaciaDao;
import com.princetonsa.dao.oracle.facturacion.OracleAnulacionFacturasDao;
import com.princetonsa.dao.oracle.facturacion.OracleArchivoPlanoDao;
import com.princetonsa.dao.oracle.facturacion.OracleAutorizarAnulacionFacturasDao;
import com.princetonsa.dao.oracle.facturacion.OracleCalculoHonorariosPoolesDao;
import com.princetonsa.dao.oracle.facturacion.OracleCalculoValorCobrarPacienteDao;
import com.princetonsa.dao.oracle.facturacion.OracleCargosDirectosCxDytDao;
import com.princetonsa.dao.oracle.facturacion.OracleCentrosCostoEntidadesSubcontratadasDao;
import com.princetonsa.dao.oracle.facturacion.OracleCoberturaDao;
import com.princetonsa.dao.oracle.facturacion.OracleCoberturasConvenioDao;
import com.princetonsa.dao.oracle.facturacion.OracleCoberturasEntidadesSubcontratadasDao;
import com.princetonsa.dao.oracle.facturacion.OracleComponentesPaquetesDao;
import com.princetonsa.dao.oracle.facturacion.OracleConceptosPagoPoolesDao;
import com.princetonsa.dao.oracle.facturacion.OracleConceptosPagoPoolesXConvenioDao;
import com.princetonsa.dao.oracle.facturacion.OracleCondicionesXServiciosDao;
import com.princetonsa.dao.oracle.facturacion.OracleConsolidadoFacturacionDao;
import com.princetonsa.dao.oracle.facturacion.OracleConsultaPaquetesFacturadosDao;
import com.princetonsa.dao.oracle.facturacion.OracleConsultaTarifasArticulosDao;
import com.princetonsa.dao.oracle.facturacion.OracleConsultaTarifasDao;
import com.princetonsa.dao.oracle.facturacion.OracleConsultarContratosEntidadesSubcontratadasDao;
import com.princetonsa.dao.oracle.facturacion.OracleConsumosFacturadosDao;
import com.princetonsa.dao.oracle.facturacion.OracleConsumosPorFacturarPacientesHospitalizadosDao;
import com.princetonsa.dao.oracle.facturacion.OracleConsumosXFacturarDao;
import com.princetonsa.dao.oracle.facturacion.OracleContactosEmpresaDao;
import com.princetonsa.dao.oracle.facturacion.OracleControlAnticipoContratoDao;
import com.princetonsa.dao.oracle.facturacion.OracleCopiarTarifasEsquemaTarifarioDao;
import com.princetonsa.dao.oracle.facturacion.OracleCostoInventarioPorFacturarDao;
import com.princetonsa.dao.oracle.facturacion.OracleCostoVentasPorInventarioDao;
import com.princetonsa.dao.oracle.facturacion.OracleDescuentosComercialesDao;
import com.princetonsa.dao.oracle.facturacion.OracleDetalleCoberturaDao;
import com.princetonsa.dao.oracle.facturacion.OracleDetalleInclusionesExclusionesDao;
import com.princetonsa.dao.oracle.facturacion.OracleDistribucionCuentaDao;
import com.princetonsa.dao.oracle.facturacion.OracleEntidadesSubContratadasDao;
import com.princetonsa.dao.oracle.facturacion.OracleExcepcionesCCInterconsultasDao;
import com.princetonsa.dao.oracle.facturacion.OracleExcepcionesTarifas1Dao;
import com.princetonsa.dao.oracle.facturacion.OracleFacturaDao;
import com.princetonsa.dao.oracle.facturacion.OracleFacturaOdontologicaDao;
import com.princetonsa.dao.oracle.facturacion.OracleFacturadosPorConvenioDao;
import com.princetonsa.dao.oracle.facturacion.OracleGeneracionArchivoPlanoIndicadoresCalidadDao;
import com.princetonsa.dao.oracle.facturacion.OracleHistoMontoAgrupacionArticulo;
import com.princetonsa.dao.oracle.facturacion.OracleHonorariosPoolDao;
import com.princetonsa.dao.oracle.facturacion.OracleImpresionSoportesFacturasDao;
import com.princetonsa.dao.oracle.facturacion.OracleInclusionExclusionConvenioDao;
import com.princetonsa.dao.oracle.facturacion.OracleInclusionesExclusionesDao;
import com.princetonsa.dao.oracle.facturacion.OracleIngresarModificarContratosEntidadesSubcontratadasDao;
import com.princetonsa.dao.oracle.facturacion.OracleLogControlAnticipoContratoDao;
import com.princetonsa.dao.oracle.facturacion.OracleMontoAgrupacionArticuloDAO;
import com.princetonsa.dao.oracle.facturacion.OracleMontoArticuloEspecificoDAO;
import com.princetonsa.dao.oracle.facturacion.OraclePacientesPorFacturarDao;
import com.princetonsa.dao.oracle.facturacion.OraclePaquetesConvenioDao;
import com.princetonsa.dao.oracle.facturacion.OraclePaquetesDao;
import com.princetonsa.dao.oracle.facturacion.OraclePaquetizacionDao;
import com.princetonsa.dao.oracle.facturacion.OracleParamArchivoPlanoColsanitasDao;
import com.princetonsa.dao.oracle.facturacion.OracleParamArchivoPlanoIndCalidadDao;
import com.princetonsa.dao.oracle.facturacion.OraclePendienteFacturarDao;
import com.princetonsa.dao.oracle.facturacion.OracleRegistroRipsCargosDirectosDao;
import com.princetonsa.dao.oracle.facturacion.OracleReliquidacionTarifasDao;
import com.princetonsa.dao.oracle.facturacion.OracleReporteProcedimientosEsteticosDao;
import com.princetonsa.dao.oracle.facturacion.OracleRevisionCuentaDao;
import com.princetonsa.dao.oracle.facturacion.OracleServiciosGruposEsteticosDao;
import com.princetonsa.dao.oracle.facturacion.OracleServicios_ArticulosIncluidosEnOtrosProcedimientosDao;
import com.princetonsa.dao.oracle.facturacion.OracleSolicitarAnulacionFacturasDao;
import com.princetonsa.dao.oracle.facturacion.OracleSoportesFacturasDao;
import com.princetonsa.dao.oracle.facturacion.OracleTiposConvenioDao;
import com.princetonsa.dao.oracle.facturacion.OracleTotalFacturadoConvenioContratoDao;
import com.princetonsa.dao.oracle.facturacion.OracleTrasladoSolicitudesPorTransplantesDao;
import com.princetonsa.dao.oracle.facturacion.OracleUsuariosAutorizarAnulacionFacturasDao;
import com.princetonsa.dao.oracle.facturacion.OracleUtilidadesFacturacionDao;
import com.princetonsa.dao.oracle.facturacion.OracleValidacionesFacturaDao;
import com.princetonsa.dao.oracle.facturacion.OracleVentasCentroCostoDao;
import com.princetonsa.dao.oracle.facturacion.OracleVentasEmpresaConvenioDao;
import com.princetonsa.dao.oracle.facturasVarias.OracleAprobacionAnulacionAjustesFacturasVariasDao;
import com.princetonsa.dao.oracle.facturasVarias.OracleAprobacionAnulacionFacturasVariasDao;
import com.princetonsa.dao.oracle.facturasVarias.OracleAprobacionAnulacionPagosFacturasVariasDao;
import com.princetonsa.dao.oracle.facturasVarias.OracleConceptosFacturasVariasDao;
import com.princetonsa.dao.oracle.facturasVarias.OracleConsultaFacturasVariasDao;
import com.princetonsa.dao.oracle.facturasVarias.OracleConsultaImpresionAjustesFacturasVariasDao;
import com.princetonsa.dao.oracle.facturasVarias.OracleConsultaImpresionPagosFacturasVariasDao;
import com.princetonsa.dao.oracle.facturasVarias.OracleConsultaMovimientoDeudorDao;
import com.princetonsa.dao.oracle.facturasVarias.OracleConsultaMovimientoFacturaDao;
import com.princetonsa.dao.oracle.facturasVarias.OracleDeudoresDao;
import com.princetonsa.dao.oracle.facturasVarias.OracleGenModFacturasVariasDao;
import com.princetonsa.dao.oracle.facturasVarias.OracleGeneracionModificacionAjustesFacturasVariasDao;
import com.princetonsa.dao.oracle.facturasVarias.OraclePagosFacturasVariasDao;
import com.princetonsa.dao.oracle.facturasVarias.OracleUtilidadesFacturasVariasDao;
import com.princetonsa.dao.oracle.glosas.OracleAprobarAnularRespuestasDao;
import com.princetonsa.dao.oracle.glosas.OracleAprobarGlosasDao;
import com.princetonsa.dao.oracle.glosas.OracleConceptosEspecificosDao;
import com.princetonsa.dao.oracle.glosas.OracleConceptosGeneralesDao;
import com.princetonsa.dao.oracle.glosas.OracleConceptosRespuestasDao;
import com.princetonsa.dao.oracle.glosas.OracleConfirmarAnularGlosasDao;
import com.princetonsa.dao.oracle.glosas.OracleConsultarImpFacAudiDao;
import com.princetonsa.dao.oracle.glosas.OracleConsultarImprimirGlosasDao;
import com.princetonsa.dao.oracle.glosas.OracleConsultarImprimirGlosasSinRespuestaDao;
import com.princetonsa.dao.oracle.glosas.OracleConsultarImprimirRespuestasDao;
import com.princetonsa.dao.oracle.glosas.OracleDetalleGlosasDao;
import com.princetonsa.dao.oracle.glosas.OracleEdadGlosaXFechaRadicacionDao;
import com.princetonsa.dao.oracle.glosas.OracleGlosasDao;
import com.princetonsa.dao.oracle.glosas.OracleParametrosFirmasImpresionRespDao;
import com.princetonsa.dao.oracle.glosas.OracleRadicarRespuestasDao;
import com.princetonsa.dao.oracle.glosas.OracleRegistrarConciliacionDao;
import com.princetonsa.dao.oracle.glosas.OracleRegistrarModificarGlosasDao;
import com.princetonsa.dao.oracle.glosas.OracleRegistrarRespuestaDao;
import com.princetonsa.dao.oracle.glosas.OracleReporteEstadoCarteraGlosasDao;
import com.princetonsa.dao.oracle.glosas.OracleReporteFacturasReiteradasDao;
import com.princetonsa.dao.oracle.glosas.OracleReporteFacturasVencidasNoObjetadasDao;
import com.princetonsa.dao.oracle.glosas.OracleUtilidadesGlosasDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleCategoriasTriageDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleConsentimientoInformadoDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleConsultaIngresosHospitalDiaDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleConsultaReferenciaContrareferenciaDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleConsultaTerapiasGrupalesDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleContrarreferenciaDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleCrecimientoDesarrolloDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleDestinoTriageDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleDiagnosticosOdontologicosATratarDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleEpicrisis1Dao;
import com.princetonsa.dao.oracle.historiaClinica.OracleEscalasDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleEventosAdversosDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleEvolucionesDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleHistoricoAtencionesDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleImagenesBaseDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleImpresionCLAPDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleImpresionResumenAtencionesDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleInformacionPartoDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleInstitucionesSircDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleJustificacionNoPosServDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleMotivosSircDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleParametrizacionComponentesDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleParametrizacionCurvaAlertaDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleParametrizacionPlantillasDao;
import com.princetonsa.dao.oracle.historiaClinica.OraclePlantillasDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleReferenciaDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleRegistroEventosAdversosDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleRegistroResumenParcialHistoriaClinicaDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleRegistroTerapiasGrupalesDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleReporteReferenciaExternaDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleReporteReferenciaInternaDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleServiciosSircDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleServiciosXTipoTratamientoOdontologicoDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleSignosSintomasDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleSignosSintomasXSistemaDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleSistemasMotivoConsultaDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleTextosRespuestaProcedimientosDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleTiposTratamientosOdontologicosDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleUtilidadesHistoriaClinicaDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleUtilidadesJustificacionNoPosDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleValoracionPacientesCuidadosEspecialesDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleValoracionesDao;
import com.princetonsa.dao.oracle.historiaClinica.OracleVerResumenOdontologicoDao;
import com.princetonsa.dao.oracle.interfaz.OracleCampoInterfazDao;
import com.princetonsa.dao.oracle.interfaz.OracleConsultaInterfazSistema1EDao;
import com.princetonsa.dao.oracle.interfaz.OracleCuentaInvUnidadFunDao;
import com.princetonsa.dao.oracle.interfaz.OracleCuentaInventarioDao;
import com.princetonsa.dao.oracle.interfaz.OracleCuentaServicioDao;
import com.princetonsa.dao.oracle.interfaz.OracleCuentaUnidadFuncionalDao;
import com.princetonsa.dao.oracle.interfaz.OracleCuentasConveniosDao;
import com.princetonsa.dao.oracle.interfaz.OracleDesmarcarDocProcesadosDao;
import com.princetonsa.dao.oracle.interfaz.OracleGeneracionInterfaz1EDao;
import com.princetonsa.dao.oracle.interfaz.OracleGeneracionInterfazDao;
import com.princetonsa.dao.oracle.interfaz.OracleInterfazSistemaUnoDao;
import com.princetonsa.dao.oracle.interfaz.OracleParamInterfazDao;
import com.princetonsa.dao.oracle.interfaz.OracleParamInterfazSistema1EDao;
import com.princetonsa.dao.oracle.interfaz.OracleReporteMovTipoDocDao;
import com.princetonsa.dao.oracle.interfaz.OracleUtilidadesInterfazDao;
import com.princetonsa.dao.oracle.inventarios.OracleAjustesXInventarioFisicoDao;
import com.princetonsa.dao.oracle.inventarios.OracleAlmacenParametrosDao;
import com.princetonsa.dao.oracle.inventarios.OracleArticuloCatalogoDao;
import com.princetonsa.dao.oracle.inventarios.OracleArticulosConsumidosPacientesDao;
import com.princetonsa.dao.oracle.inventarios.OracleArticulosFechaVencimientoDao;
import com.princetonsa.dao.oracle.inventarios.OracleArticulosPorAlmacenDao;
import com.princetonsa.dao.oracle.inventarios.OracleArticulosPuntoPedidoDao;
import com.princetonsa.dao.oracle.inventarios.OracleArticulosXMezclaDao;
import com.princetonsa.dao.oracle.inventarios.OracleCierresInventarioDao;
import com.princetonsa.dao.oracle.inventarios.OracleComparativoUltimoConteoDao;
import com.princetonsa.dao.oracle.inventarios.OracleConsultaAjustesEmpresaDao;
import com.princetonsa.dao.oracle.inventarios.OracleConsultaCostoArticulosDao;
import com.princetonsa.dao.oracle.inventarios.OracleConsultaDevolucionInventarioPacienteDao;
import com.princetonsa.dao.oracle.inventarios.OracleConsultaDevolucionPedidosDao;
import com.princetonsa.dao.oracle.inventarios.OracleConsultaImpresionTrasladosDao;
import com.princetonsa.dao.oracle.inventarios.OracleConsultaInventarioFisicoArticulosDao;
import com.princetonsa.dao.oracle.inventarios.OracleConsultaMovimientosConsignacionesDao;
import com.princetonsa.dao.oracle.inventarios.OracleConsultaSaldosCierresInventariosDao;
import com.princetonsa.dao.oracle.inventarios.OracleConsumosCentrosCostoDao;
import com.princetonsa.dao.oracle.inventarios.OracleCostoInventariosPorAlmacenDao;
import com.princetonsa.dao.oracle.inventarios.OracleDespachoPedidoQxDao;
import com.princetonsa.dao.oracle.inventarios.OracleDespachoTrasladoAlmacenDao;
import com.princetonsa.dao.oracle.inventarios.OracleDevolucionInventariosPacienteDao;
import com.princetonsa.dao.oracle.inventarios.OracleEquivalentesDeInventarioDao;
import com.princetonsa.dao.oracle.inventarios.OracleExistenciasInventariosDao;
import com.princetonsa.dao.oracle.inventarios.OracleFarmaciaCentroCostoDao;
import com.princetonsa.dao.oracle.inventarios.OracleFormaFarmaceuticaDao;
import com.princetonsa.dao.oracle.inventarios.OracleFormatoJustArtNoposDao;
import com.princetonsa.dao.oracle.inventarios.OracleFormatoJustServNoposDao;
import com.princetonsa.dao.oracle.inventarios.OracleGrupoEspecialArticulosDao;
import com.princetonsa.dao.oracle.inventarios.OracleImpListaConteoDao;
import com.princetonsa.dao.oracle.inventarios.OracleKardexDao;
import com.princetonsa.dao.oracle.inventarios.OracleMedicamentosControladosAdministradosDao;
import com.princetonsa.dao.oracle.inventarios.OracleMezclasDao;
import com.princetonsa.dao.oracle.inventarios.OracleMotDevolucionInventariosDao;
import com.princetonsa.dao.oracle.inventarios.OracleMovimientosAlmacenConsignacionDao;
import com.princetonsa.dao.oracle.inventarios.OracleMovimientosAlmacenesDao;
import com.princetonsa.dao.oracle.inventarios.OracleNaturalezaArticulosDao;
import com.princetonsa.dao.oracle.inventarios.OraclePreparacionTomaInventarioDao;
import com.princetonsa.dao.oracle.inventarios.OracleRegistroConteoInventarioDao;
import com.princetonsa.dao.oracle.inventarios.OracleRegistroTransaccionesDao;
import com.princetonsa.dao.oracle.inventarios.OracleSeccionesDao;
import com.princetonsa.dao.oracle.inventarios.OracleSolicitudMedicamentosXDespacharDao;
import com.princetonsa.dao.oracle.inventarios.OracleSolicitudTrasladoAlmacenDao;
import com.princetonsa.dao.oracle.inventarios.OracleSustitutosNoPosDao;
import com.princetonsa.dao.oracle.inventarios.OracleTiposInventarioDao;
import com.princetonsa.dao.oracle.inventarios.OracleTiposTransaccionesInvDao;
import com.princetonsa.dao.oracle.inventarios.OracleTransaccionesValidasXCCDao;
import com.princetonsa.dao.oracle.inventarios.OracleUnidadMedidaDao;
import com.princetonsa.dao.oracle.inventarios.OracleUsuariosXAlmacenDao;
import com.princetonsa.dao.oracle.inventarios.OracleUtilidadInventariosDao;
import com.princetonsa.dao.oracle.laboratorios.OracleUtilidadLaboratoriosDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleAperturaIngresosDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleAsignacionCamaCuidadoEspecialAPisoDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleAsignacionCamaCuidadoEspecialDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleAutorizacionesDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleAutorizacionesEntidadesSubcontratadasDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleCalidadAtencionDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleCategoriaAtencionDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleCensoCamasDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleCierreIngresoDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleConsultaCierreAperturaIngresoDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleConsultaEnvioInconsistenciasenBDDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleConsultaPreingresosDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleConsultarActivarCamasReservadasDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleConsultarAdmisionDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleConsultarImprimirAutorizacionesEntSubcontratadasDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleConsultarIngresosPorTransplantesDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleConsultoriosDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleEncuestaCalidadAtencionDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleEstadisticasIngresosDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleEstadisticasServiciosDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleFosygaDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleGeneracionAnexosForecatDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleGeneracionTarifasPendientesEntSubDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleHabitacionesDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleInformacionRecienNacidosDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleLecturaPlanosEntidadesDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleLiberacionCamasIngresosCerradosDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleListadoCamasHospitalizacionDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleListadoIngresosDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleMotivoCierreAperturaIngresosDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleMotivosSatisfaccionInsatisfaccionDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleOcupacionDiariaCamasDao;
import com.princetonsa.dao.oracle.manejoPaciente.OraclePacientesConAtencionDao;
import com.princetonsa.dao.oracle.manejoPaciente.OraclePacientesConEgresoPorFacturarDao;
import com.princetonsa.dao.oracle.manejoPaciente.OraclePacientesEntidadesSubConDao;
import com.princetonsa.dao.oracle.manejoPaciente.OraclePacientesHospitalizadosDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleParametrosEntidadesSubContratadasDao;
import com.princetonsa.dao.oracle.manejoPaciente.OraclePerfilNEDDao;
import com.princetonsa.dao.oracle.manejoPaciente.OraclePisosDao;
import com.princetonsa.dao.oracle.manejoPaciente.OraclePlanosFURIPSDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleProrrogarAnularAutorizacionesEntSubcontratadasDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleRegionesCoberturaDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleRegistroAccidentesTransitoDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleRegistroEnvioInfAtencionIniUrgDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleRegistroEnvioInformInconsisenBDDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleRegistroEventosCatastroficosDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleReingresoSalidaHospiDiaDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleReporteEgresosEstanciasDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleReporteMortalidadDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleTipoHabitacionDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleTiposAmbulanciaDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleTiposUsuarioCamaDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleTotalOcupacionCamasDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleTrasladoCentroAtencionDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleUtilidadesManejoPacienteDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleValoresTipoReporteDao;
import com.princetonsa.dao.oracle.manejoPaciente.OracleViasIngresoDao;
import com.princetonsa.dao.oracle.odontologia.OracleAgendaOdontologicaDao;
import com.princetonsa.dao.oracle.odontologia.OracleAliadoOdontologicoDao;
import com.princetonsa.dao.oracle.odontologia.OracleAperturaCuentaPacienteOdontologicoDao;
import com.princetonsa.dao.oracle.odontologia.OracleAtencionCitasOdontologiaDao;
import com.princetonsa.dao.oracle.odontologia.OracleAutorizacionDescuentosOdonDao;
import com.princetonsa.dao.oracle.odontologia.OracleBeneficiarioTarjetaCliente;
import com.princetonsa.dao.oracle.odontologia.OracleCancelarAgendaOdontoDao;
import com.princetonsa.dao.oracle.odontologia.OracleCitaOdontologicaDao;
import com.princetonsa.dao.oracle.odontologia.OracleConvencionesOdontologicasDao;
import com.princetonsa.dao.oracle.odontologia.OracleDescuentoOdontologicoDao;
import com.princetonsa.dao.oracle.odontologia.OracleDetCaPromocionesOdoDao;
import com.princetonsa.dao.oracle.odontologia.OracleDetConvPromocionesOdoDao;
import com.princetonsa.dao.oracle.odontologia.OracleDetPromocionesDao;
import com.princetonsa.dao.oracle.odontologia.OracleDetalleAgrupacionHonorarioDao;
import com.princetonsa.dao.oracle.odontologia.OracleDetalleDescuentoOdontologicoDao;
import com.princetonsa.dao.oracle.odontologia.OracleDetalleEmisionTarjetaClienteDao;
import com.princetonsa.dao.oracle.odontologia.OracleDetalleHallazgoProgramaServicioDao;
import com.princetonsa.dao.oracle.odontologia.OracleDetalleServicioHonorarioDao;
import com.princetonsa.dao.oracle.odontologia.OracleEmisionBonoDescDao;
import com.princetonsa.dao.oracle.odontologia.OracleEmisionTarjetaClienteDao;
import com.princetonsa.dao.oracle.odontologia.OracleEvolucionOdontologicaDao;
import com.princetonsa.dao.oracle.odontologia.OracleGenerarAgendaOdontologicaDao;
import com.princetonsa.dao.oracle.odontologia.OracleHallazgoVsProgramaServicioDao;
import com.princetonsa.dao.oracle.odontologia.OracleHallazgosOdontologicosDao;
import com.princetonsa.dao.oracle.odontologia.OracleHistoricoDescuentoOdontologicoDao;
import com.princetonsa.dao.oracle.odontologia.OracleHistoricoDetalleDescuentoOdontologicoDao;
import com.princetonsa.dao.oracle.odontologia.OracleHistoricoDetalleEmisionTarjetaClienteDao;
import com.princetonsa.dao.oracle.odontologia.OracleHistoricoEmisionTarjetaClienteDao;
import com.princetonsa.dao.oracle.odontologia.OracleIngresoPacienteOdontologiaDao;
import com.princetonsa.dao.oracle.odontologia.OracleMotivoDescuentoDao;
import com.princetonsa.dao.oracle.odontologia.OracleMotivosAtencionOdontologicaDao;
import com.princetonsa.dao.oracle.odontologia.OracleNumeroSuperficiesPresupuestoDao;
import com.princetonsa.dao.oracle.odontologia.OraclePacientesConvenioOdontologiaDao;
import com.princetonsa.dao.oracle.odontologia.OracleParentezcoDao;
import com.princetonsa.dao.oracle.odontologia.OraclePlantratamientoDao;
import com.princetonsa.dao.oracle.odontologia.OraclePresupuestoContratadoDao;
import com.princetonsa.dao.oracle.odontologia.OraclePresupuestoCuotasEspecialidadDao;
import com.princetonsa.dao.oracle.odontologia.OraclePresupuestoExclusionesInclusionesDao;
import com.princetonsa.dao.oracle.odontologia.OraclePresupuestoLogImpresionDao;
import com.princetonsa.dao.oracle.odontologia.OraclePresupuestoOdontologicoDao;
import com.princetonsa.dao.oracle.odontologia.OraclePresupuestoPaquetesDao;
import com.princetonsa.dao.oracle.odontologia.OracleProcesosAutomaticosOdontologiaDao;
import com.princetonsa.dao.oracle.odontologia.OracleProgramaDao;
import com.princetonsa.dao.oracle.odontologia.OracleProgramasOdontologicosDao;
import com.princetonsa.dao.oracle.odontologia.OraclePromocionesOdontologicasDao;
import com.princetonsa.dao.oracle.odontologia.OracleReasignarProfesionalOdontoDao;
import com.princetonsa.dao.oracle.odontologia.OracleReporteCitasOdontologicasDao;
import com.princetonsa.dao.oracle.odontologia.OracleServicioHonorariosDao;
import com.princetonsa.dao.oracle.odontologia.OracleTarjetaClienteDao;
import com.princetonsa.dao.oracle.odontologia.OracleTiposUsuarioDao;
import com.princetonsa.dao.oracle.odontologia.OracleUtilidadOdontologiaDao;
import com.princetonsa.dao.oracle.odontologia.OracleValidacionesPresupuestoDao;
import com.princetonsa.dao.oracle.odontologia.OracleValoracionOdontologicaDao;
import com.princetonsa.dao.oracle.odontologia.OracleVentasTarjetaClienteDao;
import com.princetonsa.dao.oracle.ordenesmedicas.OracleOrdenesAmbulatoriasDao;
import com.princetonsa.dao.oracle.ordenesmedicas.OracleRegistroIncapacidadesDao;
import com.princetonsa.dao.oracle.ordenesmedicas.OracleResponderConsultasEntSubcontratadasDao;
import com.princetonsa.dao.oracle.ordenesmedicas.OracleUtilidadesOrdenesMedicasDao;
import com.princetonsa.dao.oracle.ordenesmedicas.procedimientos.OracleInterpretarProcedimientoDao;
import com.princetonsa.dao.oracle.ordenesmedicas.procedimientos.OracleRespuestaProcedimientosDao;
import com.princetonsa.dao.oracle.parametrizacion.OracleAccesosVascularesHADao;
import com.princetonsa.dao.oracle.parametrizacion.OracleCentrosAtencionDao;
import com.princetonsa.dao.oracle.parametrizacion.OracleEventosDao;
import com.princetonsa.dao.oracle.parametrizacion.OracleGasesHojaAnestesiaDao;
import com.princetonsa.dao.oracle.parametrizacion.OracleInfoGeneralHADao;
import com.princetonsa.dao.oracle.parametrizacion.OracleIntubacionesHADao;
import com.princetonsa.dao.oracle.parametrizacion.OracleMonitoreoHemodinamicaDao;
import com.princetonsa.dao.oracle.parametrizacion.OraclePosicionesAnestesiaDao;
import com.princetonsa.dao.oracle.parametrizacion.OracleSignosVitalesDao;
import com.princetonsa.dao.oracle.parametrizacion.OracleTecnicaAnestesiaDao;
import com.princetonsa.dao.oracle.parametrizacion.OracleTiemposHojaAnestesiaDao;
import com.princetonsa.dao.oracle.parametrizacion.OracleViasAereasDao;
import com.princetonsa.dao.oracle.pyp.OracleActEjecutadasXCargarDao;
import com.princetonsa.dao.oracle.pyp.OracleActivPyPXFacturarDao;
import com.princetonsa.dao.oracle.pyp.OracleActividadesProgramasPYPDao;
import com.princetonsa.dao.oracle.pyp.OracleActividadesPypDao;
import com.princetonsa.dao.oracle.pyp.OracleCalificacionesXCumpliMetasDao;
import com.princetonsa.dao.oracle.pyp.OracleGrupoEtareoCrecimientoDesarrolloDao;
import com.princetonsa.dao.oracle.pyp.OracleMetasPYPDao;
import com.princetonsa.dao.oracle.pyp.OracleProgramaArticuloPypDao;
import com.princetonsa.dao.oracle.pyp.OracleProgramasActividadesConvenioDao;
import com.princetonsa.dao.oracle.pyp.OracleProgramasPYPDao;
import com.princetonsa.dao.oracle.pyp.OracleProgramasSaludPYPDao;
import com.princetonsa.dao.oracle.pyp.OracleTipoCalificacionPypDao;
import com.princetonsa.dao.oracle.pyp.OracleTiposProgamaDao;
import com.princetonsa.dao.oracle.salasCirugia.OracleAsocioSalaCirugiaDao;
import com.princetonsa.dao.oracle.salasCirugia.OracleAsocioServicioTarifaDao;
import com.princetonsa.dao.oracle.salasCirugia.OracleAsociosXRangoTiempoDao;
import com.princetonsa.dao.oracle.salasCirugia.OracleAsociosXTipoServicioDao;
import com.princetonsa.dao.oracle.salasCirugia.OracleAsociosXUvrDao;
import com.princetonsa.dao.oracle.salasCirugia.OracleConsultaProgramacionCirugiasDao;
import com.princetonsa.dao.oracle.salasCirugia.OracleDevolucionPedidoQxDao;
import com.princetonsa.dao.oracle.salasCirugia.OracleExTarifasAsociosDao;
import com.princetonsa.dao.oracle.salasCirugia.OracleHojaAnestesiaDao;
import com.princetonsa.dao.oracle.salasCirugia.OracleHojaGastosDao;
import com.princetonsa.dao.oracle.salasCirugia.OracleLiquidacionServiciosDao;
import com.princetonsa.dao.oracle.salasCirugia.OracleServiciosViaAccesoDao;
import com.princetonsa.dao.oracle.salasCirugia.OracleUtilidadesSalasDao;
import com.princetonsa.dao.oracle.tesoreria.OracleAprobacionAnulacionDevolucionesDao;
import com.princetonsa.dao.oracle.tesoreria.OracleArqueosDao;
import com.princetonsa.dao.oracle.tesoreria.OracleConsultaImpresionDevolucionDao;
import com.princetonsa.dao.oracle.tesoreria.OracleMotivosDevolucionRecibosCajaDao;
import com.princetonsa.dao.oracle.tesoreria.OracleRegistroDevolucionRecibosCajaDao;
import com.princetonsa.dao.oracle.tesoreria.OracleTrasladosCajaDao;
import com.princetonsa.dao.oracle.tramiteReferencia.OracleTramiteReferenciaDao;
import com.princetonsa.dao.oracle.util.OracleConsultasBirtDao;
import com.princetonsa.dao.oracle.util.OracleUtilConversionMonedasDao;
import com.princetonsa.dao.oracle.webServiceCitasMedicas.OracleConsultaWSDao;
import com.princetonsa.dao.ordenesmedicas.OrdenesAmbulatoriasDao;
import com.princetonsa.dao.ordenesmedicas.RegistroIncapacidadesDao;
import com.princetonsa.dao.ordenesmedicas.ResponderConsultasEntSubcontratadasDao;
import com.princetonsa.dao.ordenesmedicas.UtilidadesOrdenesMedicasDao;
import com.princetonsa.dao.ordenesmedicas.procedimientos.InterpretarProcedimientoDao;
import com.princetonsa.dao.ordenesmedicas.procedimientos.RespuestaProcedimientosDao;
import com.princetonsa.dao.parametrizacion.AccesosVascularesHADao;
import com.princetonsa.dao.parametrizacion.CentrosAtencionDao;
import com.princetonsa.dao.parametrizacion.EventosDao;
import com.princetonsa.dao.parametrizacion.GasesHojaAnestesiaDao;
import com.princetonsa.dao.parametrizacion.InfoGeneralHADao;
import com.princetonsa.dao.parametrizacion.IntubacionesHADao;
import com.princetonsa.dao.parametrizacion.MonitoreoHemodinamicaDao;
import com.princetonsa.dao.parametrizacion.PosicionesAnestesiaDao;
import com.princetonsa.dao.parametrizacion.SignosVitalesDao;
import com.princetonsa.dao.parametrizacion.TecnicaAnestesiaDao;
import com.princetonsa.dao.parametrizacion.TiemposHojaAnestesiaDao;
import com.princetonsa.dao.parametrizacion.ViasAereasDao;
import com.princetonsa.dao.pyp.ActEjecutadasXCargarDao;
import com.princetonsa.dao.pyp.ActivPyPXFacturarDao;
import com.princetonsa.dao.pyp.ActividadesProgramasPYPDao;
import com.princetonsa.dao.pyp.ActividadesPypDao;
import com.princetonsa.dao.pyp.CalificacionesXCumpliMetasDao;
import com.princetonsa.dao.pyp.GrupoEtareoCrecimientoDesarrolloDao;
import com.princetonsa.dao.pyp.MetasPYPDao;
import com.princetonsa.dao.pyp.ProgramaArticuloPypDao;
import com.princetonsa.dao.pyp.ProgramasActividadesConvenioDao;
import com.princetonsa.dao.pyp.ProgramasPYPDao;
import com.princetonsa.dao.pyp.ProgramasSaludPYPDao;
import com.princetonsa.dao.pyp.TipoCalificacionPypDao;
import com.princetonsa.dao.pyp.TiposProgamaDao;
import com.princetonsa.dao.salasCirugia.AsocioSalaCirugiaDao;
import com.princetonsa.dao.salasCirugia.AsocioServicioTarifaDao;
import com.princetonsa.dao.salasCirugia.AsociosXRangoTiempoDao;
import com.princetonsa.dao.salasCirugia.AsociosXTipoServicioDao;
import com.princetonsa.dao.salasCirugia.AsociosXUvrDao;
import com.princetonsa.dao.salasCirugia.ConsultaProgramacionCirugiasDao;
import com.princetonsa.dao.salasCirugia.DevolucionPedidoQxDao;
import com.princetonsa.dao.salasCirugia.ExTarifasAsociosDao;
import com.princetonsa.dao.salasCirugia.HojaAnestesiaDao;
import com.princetonsa.dao.salasCirugia.HojaGastosDao;
import com.princetonsa.dao.salasCirugia.LiquidacionServiciosDao;
import com.princetonsa.dao.salasCirugia.ServiciosViaAccesoDao;
import com.princetonsa.dao.salasCirugia.UtilidadesSalasDao;
import com.princetonsa.dao.tesoreria.AprobacionAnulacionDevolucionesDao;
import com.princetonsa.dao.tesoreria.ArqueosDao;
import com.princetonsa.dao.tesoreria.RegistroDevolucionRecibosCajaDao;
import com.princetonsa.dao.tesoreria.TrasladosCajaDao;
import com.princetonsa.dao.tramiteReferencia.TramiteReferenciaDao;
import com.princetonsa.dao.util.ConsultasBirtDao;
import com.princetonsa.dao.util.UtilConversionMonedasDao;
import com.princetonsa.dao.util.UtilidadOdontologiaDao;
import com.princetonsa.dao.webServiceCitasMedicas.ConsultaWSDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.sysmedica.dao.ArchivosPlanosDao;
import com.sysmedica.dao.BusquedaFichasDao;
import com.sysmedica.dao.BusquedaNotificacionesDao;
import com.sysmedica.dao.FichaAcciOfidicoDao;
import com.sysmedica.dao.FichaBrotesDao;
import com.sysmedica.dao.FichaDengueDao;
import com.sysmedica.dao.FichaDifteriaDao;
import com.sysmedica.dao.FichaEasvDao;
import com.sysmedica.dao.FichaEsiDao;
import com.sysmedica.dao.FichaEtasDao;
import com.sysmedica.dao.FichaGenericaDao;
import com.sysmedica.dao.FichaHepatitisDao;
import com.sysmedica.dao.FichaInfeccionesDao;
import com.sysmedica.dao.FichaIntoxicacionesDao;
import com.sysmedica.dao.FichaLeishmaniasisDao;
import com.sysmedica.dao.FichaLepraDao;
import com.sysmedica.dao.FichaLesionesDao;
import com.sysmedica.dao.FichaMalariaDao;
import com.sysmedica.dao.FichaMeningitisDao;
import com.sysmedica.dao.FichaMortalidadDao;
import com.sysmedica.dao.FichaParalisisDao;
import com.sysmedica.dao.FichaRabiaDao;
import com.sysmedica.dao.FichaRubCongenitaDao;
import com.sysmedica.dao.FichaSarampionDao;
import com.sysmedica.dao.FichaSifilisDao;
import com.sysmedica.dao.FichaSivimDao;
import com.sysmedica.dao.FichaSolicitudLaboratoriosDao;
import com.sysmedica.dao.FichaTetanosDao;
import com.sysmedica.dao.FichaTosferinaDao;
import com.sysmedica.dao.FichaTuberculosisDao;
import com.sysmedica.dao.FichaVIHDao;
import com.sysmedica.dao.FichasAnterioresDao;
import com.sysmedica.dao.IngresoPacienteEpiDao;
import com.sysmedica.dao.NotificacionDao;
import com.sysmedica.dao.ParamLaboratoriosDao;
import com.sysmedica.dao.ReportesSecretariaDao;
import com.sysmedica.dao.UtilidadFichasDao;
import com.sysmedica.dao.oracle.OracleArchivosPlanosDao;
import com.sysmedica.dao.oracle.OracleBusquedaFichasDao;
import com.sysmedica.dao.oracle.OracleBusquedaNotificacionesDao;
import com.sysmedica.dao.oracle.OracleFichaAcciOfidicoDao;
import com.sysmedica.dao.oracle.OracleFichaBrotesDao;
import com.sysmedica.dao.oracle.OracleFichaDengueDao;
import com.sysmedica.dao.oracle.OracleFichaDifteriaDao;
import com.sysmedica.dao.oracle.OracleFichaEasvDao;
import com.sysmedica.dao.oracle.OracleFichaEsiDao;
import com.sysmedica.dao.oracle.OracleFichaEtasDao;
import com.sysmedica.dao.oracle.OracleFichaGenericaDao;
import com.sysmedica.dao.oracle.OracleFichaHepatitisDao;
import com.sysmedica.dao.oracle.OracleFichaInfeccionesDao;
import com.sysmedica.dao.oracle.OracleFichaIntoxicacionesDao;
import com.sysmedica.dao.oracle.OracleFichaLeishmaniasisDao;
import com.sysmedica.dao.oracle.OracleFichaLepraDao;
import com.sysmedica.dao.oracle.OracleFichaLesionesDao;
import com.sysmedica.dao.oracle.OracleFichaMalariaDao;
import com.sysmedica.dao.oracle.OracleFichaMeningitisDao;
import com.sysmedica.dao.oracle.OracleFichaMortalidadDao;
import com.sysmedica.dao.oracle.OracleFichaParalisisDao;
import com.sysmedica.dao.oracle.OracleFichaRabiaDao;
import com.sysmedica.dao.oracle.OracleFichaRubCongenitaDao;
import com.sysmedica.dao.oracle.OracleFichaSarampionDao;
import com.sysmedica.dao.oracle.OracleFichaSifilisDao;
import com.sysmedica.dao.oracle.OracleFichaSivimDao;
import com.sysmedica.dao.oracle.OracleFichaSolicitudLaboratoriosDao;
import com.sysmedica.dao.oracle.OracleFichaTetanosDao;
import com.sysmedica.dao.oracle.OracleFichaTosferinaDao;
import com.sysmedica.dao.oracle.OracleFichaTuberculosisDao;
import com.sysmedica.dao.oracle.OracleFichaVIHDao;
import com.sysmedica.dao.oracle.OracleFichasAnterioresDao;
import com.sysmedica.dao.oracle.OracleIngresoPacienteEpiDao;
import com.sysmedica.dao.oracle.OracleNotificacionDao;
import com.sysmedica.dao.oracle.OracleParamLaboratoriosDao;
import com.sysmedica.dao.oracle.OracleReportesSecretariaDao;
import com.sysmedica.dao.oracle.OracleUtilidadFichasDao;

/**
 * Esta clase ofrece una implementacion particular de <code>DaoFactory</code>, que utiliza Oracle como fuente de datos.
 * Adem�s de implementar los metodos abstractos de <code>DaoFactory</code>, ofrece metodos centralizados para inicializar el
 * acceso a una base de datos Oracle y obtener conexiones de esta. Como una optimizacion adicional, esta clase es
 * una implementacion del patron <i>Singleton</i>. 
 *
 * @version 1.0 Jan 8, 2005 
 */

public class OracleDaoFactory extends DaoFactory
{ 
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(OracleDaoFactory.class);
	
    /**
	 * Una instancia, -la unica- de <code>OracleDaoFactory</code>.
	 */
	private static OracleDaoFactory instancia = null;

	/**
	 * Driver jdbc requerido para acceder una base de datos Oracle.
	 */
	private static String DRIVER;

	/**
	 * Contrase�a del usuario definido en USERNAME.
	 */
	private static String PASSWORD;
	
	/*
	/**
	 * Factory del pool de conexiones, a usar en toda la aplicaci�n.
	 */
	//private static PoolableConnectionFactory poolableConnectionFactory;

	/**
	 * URL requerido para acceder una base de datos Oracle.
	 */
	private static String PROTOCOL;

	/**
	 * Nombre del usuario de la aplicacion, es quien accede a la base de datos Oracle.
	 */
	private static String USERNAME;

	/**
	 * Esta Factory produce objetos DAO de tipo Oracle, se uso este
	 * valor constante para obtener las conexiones adecuadas en
	 * todos los DAOs del paquete com.princetonsa.dao.oracle
	 */
	public static final int tipoBD = DaoFactory.ORACLE;
	

	/**
	 * DataSource para el control del pool de conexiones
	 */
	private BasicDataSource fuenteDatos = null;
	

	/**
	 * Retorna una sola instancia del objeto <code>OracleDaoFactory</code>.
	 */
	public static OracleDaoFactory getInstance() 
	{
		if ( instancia == null ) 
		{
			instancia = new OracleDaoFactory ();
		}
		return instancia;
	}
	
	/**
	 * Constructor de <code>OracleDaoFactory</code>. Notese que es <i>privado</i>, este es un
	 * requerimiento para implementar el patron <i>singleton</i>.
	 */
	private OracleDaoFactory () {
	}

	/**
	 * Hace rollback de una transaccin.
	 * @param con una conexin abierta con la fuente de datos
	 */
	public void abortTransaction (Connection con) throws SQLException {
		con.rollback();
		con.setAutoCommit(true);
	}

	/**
	 * Inicia una transacci�n.
	 * @param con una conexin abierta con la fuente de datos
	 * @return boolean <b>true</b> si se pudo iniciar la transaccin,
	 * <b>false</b> si no
	 */
	public boolean beginTransaction (Connection con) throws SQLException {
		con.setAutoCommit(false);
		//En tutorial http://www.oracle.com/oramag/oracle/02-jul/o42special_jdbc_2.html
		//recomiendan utilizar simplemente el auto-commit
		return true;
	}

	/**
	 * Termina una transaccin con commit.
	 * @param con una conexin abierta con la fuente de datos
	 */
	public void endTransactionSinMensaje (Connection con) throws SQLException {
		con.commit();
		con.setAutoCommit(true);
	}

	
	
	/**
	 * Hace rollback de una transaccin.
	 * @param con una conexin abierta con la fuente de datos
	 */
	public void abortTransactionSinMensaje (Connection con) throws SQLException {
		con.rollback();
		con.setAutoCommit(true);
	}

	/**
	 * Inicia una transacci�n.
	 * @param con una conexin abierta con la fuente de datos
	 * @return boolean <b>true</b> si se pudo iniciar la transaccin,
	 * <b>false</b> si no
	 */
	public boolean beginTransactionSinMensaje (Connection con) throws SQLException {
		con.setAutoCommit(false);
		//En tutorial http://www.oracle.com/oramag/oracle/02-jul/o42special_jdbc_2.html
		//recomiendan utilizar simplemente el auto-commit
		return true;
	}

	/**
	 * Termina una transaccin con commit.
	 * @param con una conexin abierta con la fuente de datos
	 */
	public void endTransaction (Connection con) throws SQLException {
		con.commit();
		con.setAutoCommit(true);
	}
	
	
	/**
	 * @param con
	 * @param cadenaSQL
	 * @param filtro
	 */
	public boolean bloquearRegistroActualizacion(Connection con,String cadenaSQL,ArrayList filtro)
	{
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rsDecorator=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaSQL,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			for(int i=0;i<filtro.size();i++)
			{
				try {
					Double codigo = Double.parseDouble((String) filtro.get(i));
					ps.setDouble(i+1,codigo);
				} catch (Exception e) {
					logger.error("Falla ... no es Double "+ e.getMessage());
					ps.setObject(i+1,filtro.get(i));
				}
			}
			rsDecorator=new ResultSetDecorator(ps.executeQuery());
			boolean resultado=rsDecorator.next();
			return resultado;
		} 
		catch(SQLException e){
			logger.error("Erron bloquearRegistroActualizacion", e);
		}
		finally{
			try{
				rsDecorator.close();
				ps.close();
			}
			catch (SQLException se) {
				logger.error("Erron cerrando Resulset PreparedStatement bloquearRegistroActualizacion", se);
			}
		}
		return false;
	}
	

	
	/**
	 * Obtiene el ultimo valor generado por una secuencia
	 * @param con   una conexion abierta con la base de datos
	 * @param sequenceName el nombre de la secuencia
	 * @return  el ultimo valor generado por la secuencia
	 * @throws SQLException
	 */
	public int obtenerUltimoValorSecuencia(Connection con, String sequenceName) throws SQLException{
		PreparedStatementDecorator select = null;
		ResultSetDecorator rs = null;
		int result = -1;
		
		String selectSequenceValStr="select " + sequenceName + ".currval as nextvalue from dual";
		try{
			select =  new PreparedStatementDecorator(con.prepareStatement(selectSequenceValStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs = new ResultSetDecorator(select.executeQuery());
			if(rs != null){
				rs.next();
				result = rs.getInt("nextvalue");
			}
		}
		catch(SQLException e){
			logger.error("Erron obtenerUltimoValorSecuencia", e);
		}
		finally{
			try{
				rs.close();
				select.close();
			}
			catch (SQLException se) {
				logger.error("Erron cerrando Resulset PreparedStatement obtenerUltimoValorSecuencia", se);
			}
		}
		return result;
	}

	/**
	 * Metodo que incrementa una secuencia.
	 * @param con
	 * @param nombreSecuencia de la secuencia.
	 */
	
	/**
	 * Metodo que incrementa una secuencia.
	 * @param con
	 * @param nombreSecuencia de la secuencia.
	 */
	public int incrementarValorSecuencia(Connection con, String nombreSecuencia)
	{
		int retorno=ConstantesBD.codigoNuncaValido;
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		String consulta="select " + nombreSecuencia + ".nextval as valor from dual";
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
			{
				retorno=rs.getInt("valor");
			}
		}
		catch(SQLException e){
			logger.error("Erron incrementarValorSecuencia", e);
		}
		finally{
			try{
				rs.close();
				ps.close();
			}
			catch (SQLException se) {
				logger.error("Erron cerrando Resulset PreparedStatement incrementarValorSecuencia", se);
			}
		}
  
		return retorno;
	}
	
	/**
	 * Metodo que retorn el Dao de Utilidades BD
	 * @return
	 */
	public UtilidadesBDDao getUtilidadesBDao()
	{
	    return new OracleUtilidadesBDDao();
	}
	
	/**
	 * Metodo que retorn el Dao de Utilidades Impresion
	 * @return
	 */
	public UtilidadImpresionDao getUtilidadImpresionDao()
	{
	    return new OracleUtilidadImpresionDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>ActivPyPXFacturarDao</code>, usada por <code>ActivPyPXFacturar</code>.
	 * @return la implementacion en Oracle de la interfaz <code>ActivPyPXFacturarDao</code>
	 */
	public ActivPyPXFacturarDao getActivPyPXFacturarDao () {
		return new OracleActivPyPXFacturarDao();
	}
	

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AdminMedicamentosDao</code>, usada por <code>AdminMedicamentos</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AdminMedicamentosDao</code>
	 */
	public AdminMedicamentosDao getAdminMedicamentosDao()
	{
		return new OracleAdminMedicamentosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>AdmisionHospitalariaDao</code>, usada por <code>AdmisionHospitalaria</code>.
	 * @return la implementacion en Oracle de la interfaz <code>AdmisionHospitalariaDao</code>
	 */
	public AdmisionHospitalariaDao getAdmisionHospitalariaDao()	{
	    return new OracleAdmisionHospitalariaDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>AdmisionUrgenciasDao</code>, usada por <code>AdmisionUrgencias</code>.
	 * @return la implementacion en Oracle de la interfaz <code>AdmisionUrgenciasDao</code>
	 */
	public AdmisionUrgenciasDao getAdmisionUrgenciasDao()	{
	    return new OracleAdmisionUrgenciasDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AgendaDao</code>, usada por <code>Agenda</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AgendaDao</code>
	 */
	public AgendaDao getAgendaDao()
	{
		return new OracleAgendaDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AjustesEmpresaDao</code>, usada por <code>AjustesEmpresa</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AjustesEmpresaDao</code>
	 */
	public AjustesEmpresaDao getAjustesEmpresaDao()
	{
		return new OracleAjustesEmpresaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AntecedenteMedicamentoDao</code>, usada por
	 * <code>AntecedenteMedicamento</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AntecedenteMedicamentoDao</code>
	 */
	public AntecedenteMedicamentoDao getAntecedenteMedicamentoDao()
	{
		return new OracleAntecedenteMedicamentoDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AntecedenteMorbidoMedicoDao</code>, usada por
	 * <code>AntecedenteMorbidoMedico</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AntecedenteMorbidoMedicoDao</code>
	 */
	public AntecedenteMorbidoMedicoDao getAntecedenteMorbidoMedicoDao()
	{
		return new OracleAntecedenteMorbidoMedicoDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AntecedenteMorbidoQuirurgicoDao</code>, usada por
	 * <code>AntecedenteMorbidoQuirurgico</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AntecedenteMorbidoQuirurgicoDao</code>
	 */
	public AntecedenteMorbidoQuirurgicoDao getAntecedenteMorbidoQuirurgicoDao()
	{
		return new OracleAntecedenteMorbidoQuirurgicoDao();
	}
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AntecedentePediatricoDao</code>, usada por
	 * <code>AntecedentePediatrico</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AntecedentePediatricoDao</code>
	 */
	public AntecedentePediatricoDao getAntecedentePediatricoDao()	{
		return new OracleAntecedentePediatricoDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AntecedentesAlergiasDao</code>, usada por <code>AntecedentesAlergias</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AntecedentesAlergiasDao</code>
	 */
	public AntecedentesAlergiasDao getAntecedentesAlergiasDao()
	{
		return new OracleAntecedentesAlergiasDao();
	}


	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AntecedentesFamiliaresDao</code>, usada por
	 * <code>AntecedentesFamiliares</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AntecedentesFamiliaresDao</code>
	 */
	public AntecedentesFamiliaresDao  getAntecedentesFamiliaresDao()
	{
		return new OracleAntecedentesFamiliaresDao();
	}


	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>AntecedentesGinecoObstetricosDao</code>, usada por <code>AntecedenteGinecoObstetrico</code>.
	 * @return la implementacion en Oracle de la interfaz <code>AntecedentesGinecoObstetricosDao</code>
	 */
	public AntecedentesGinecoObstetricosDao getAntecedentesGinecoObstetricosDao()
	{
		return new OracleAntecedentesGinecoObstetricosDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AntecedentesGinecoObstetricosHistoricoDao</code>, usada por
	 * <code>AntecedenteGinecoObstetricoHistorico</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AntecedentesGinecoObstetricosHistoricoDao</code>
	 */
	public AntecedentesGinecoObstetricosHistoricoDao getAntecedentesGinecoObstetricosHistoricoDao()
	{
		return new OracleAntecedentesGinecoObstetricosHistoricoDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AntecedentesMedicamentosDao</code>, usada por
	 * <code>AntecedentesMedicamentos</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AntecedentesMedicamentosDao</code>
	 */
	public AntecedentesMedicamentosDao getAntecedentesMedicamentosDao()
	{
		return new OracleAntecedentesMedicamentosDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AntecedentesMorbidosDao</code>, usada por
	 * <code>AntecedentesMorbidos</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AntecedentesMorbidosDao</code>
	 */
	public AntecedentesMorbidosDao getAntecedentesMorbidosDao()
	{
		return new OracleAntecedentesMorbidosDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AntecedentesToxicosDao</code>, usada por <code>AntecedentesToxicos</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AntecedentesToxicosDao</code>
	 */
	public AntecedentesToxicosDao getAntecedentesToxicosDao()
	{
		return new OracleAntecedentesToxicosDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AntecedentesTransfusionalesDao</code>, usada por
	 * <code>AntecedentesTransfusionales</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AntecedentesTransfusionalesDao</code>
	 */
	public AntecedentesTransfusionalesDao getAntecedentesTransfusionalesDao()
	{
		return new OracleAntecedentesTransfusionalesDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AntecedentesVariosDao</code>, usada por <code>AntecedenteVario</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AntedentesVariosDao</code>
	 */
	public AntecedentesVariosDao getAntecedentesVariosDao() {
	    return new OracleAntecedentesVariosDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AntecedenteToxicoDao</code>, usada por
	 * <code>AntecedenteToxico</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AntecedenteToxicoDao</code>
	 */
	public AntecedenteToxicoDao getAntecedenteToxicoDao()
	{
	    return new OracleAntecedenteToxicoDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AntecedenteTransfusionalDao</code>, usada por
	 * <code>AntecedenteTransfusional</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AntecedenteTransfusionalDao</code>
	 */
	public AntecedenteTransfusionalDao getAntecedenteTransfusionalDao()
	{
		return new OracleAntecedenteTransfusionalDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ArticuloDao</code>, usada por
	 * <code>Articulo</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ArticuloDao</code>
	 */
	public ArticuloDao  getArticuloDao()
	{
	    return new OracleArticuloDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ArticulosDao</code>, usada por <code>Articulos</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ArticulosDao</code>
	 */
	public ArticulosDao  getArticulosDao()
	{
		return new OracleArticulosDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AsociosTipoServicioDao</code>, usada por <code>AsociosTipoServicio</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AsociosTipoServicioDao</code>
	 */
	public AsociosTipoServicioDao getAsociosTipoServicioDao()
	{
		return new OracleAsociosTipoServicioDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AsociosXUvrDao</code>, usada por <code>AsociosXUvr</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AsociosXUvrDao</code>
	 */
	public AsociosXUvrDao getAsociosXUvrDao()
	{
		return new OracleAsociosXUvrDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AuxiliarDiagnosticosDao</code>, usada por <code>AuxiliarDiagnosticos</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AuxiliarDiagnosticosDao</code>
	 */
	public AuxiliarDiagnosticosDao getAuxiliarDiagnosticosDao()
	{
		return new OracleAuxiliarDiagnosticosDao();
	}

     /**
     * Retorna la implementacion en Oracle de la interfaz
     * <code>BusquedaArticulosGenericaDao</code>, usada por <code>BusquedaGenericaArticulos</code>.
     * @return la implementacion en Oracle de la interfaz
     * <code>BusquedaArticulosGenericaDao</code>
     */
    public BusquedaArticulosGenericaDao getBusquedaArticulosGenericaDao()
    {
        return new OracleBusquedaArticulosGenericaDao();
    }
    
    /**
     * Retorna la implementacion en Oracle de la interfaz
     * <code>BusquedaServiciosGenericaDao</code>, usada por <code>BusquedaGenericaServicios</code>.
     * @return la implementacion en Oracle de la interfaz
     * <code>BusquedaServiciosGenericaDao</code>
     */
    public BusquedaServiciosGenericaDao getBusquedaServiciosGenericaDao()
    {
        return new OracleBusquedaServiciosGenericaDao();
    }
    
    /**
     * Retorna la implementacion en Oracle de la interfaz
     * <code>BusquedaCondicionTmExamenGenericaDao</code>, usada por <code>BusquedaCondicionTmExamenGenerica</code>.
     * @return la implementacion en Oracle de la interfaz
     */
    public BusquedaCondicionTmExamenGenericaDao getBusquedaCondicionTmExamenGenericaDao()
    {
    	return new OracleBusquedaCondicionesTmExamenGenericaDao(); 
    }
    
    /**
	 * Retorna el DAO con el cual el objeto <code>BusquedaInstitucionesSircGenericaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>BusquedaInstitucionesSircGenericaDao</code>
	 */
	public BusquedaInstitucionesSircGenericaDao getBusquedaInstitucionesSircGenericaDao()
	{
		return new  OracleBusquedaInstitucionesSircGenericaDao(); 		
	}    

	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>CamaDao</code>, usada por <code>Cama</code>.
	 * @return la implementacion en Oracle de la interfaz <code>CamaDao</code>
	 */
	public CamaDao getCamaDao () {
		return new OracleCamaDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CamasDao</code>, usada por <code>Camas</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CamasDao</code>
	 */
	public CamasDao getCamasDao()
	{
		return new OracleCamasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>Camas1Dao</code>, usada por <code>Camas1</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>Camas1Dao</code>
	 */
	public Camas1Dao getCamas1Dao()
	{
		return new OracleCamas1Dao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CensoCamas1Dao</code>, usada por <code>CensoCamas1</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CensoCamas1Dao</code>
	 */
	public CensoCamas1Dao getCensoCamas1Dao()
	{
		return new OracleCensoCamas1Dao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CIEDao</code>, usada por <code>CIE</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CIEDao</code>
	 */
	public CIEDao getCIEDao()
	{
	    return new OracleCIEDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CitaDao</code>, usada por <code>Cita</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CitaDao</code>
	 */
	public CitaDao getCitaDao()
	{
		return new OracleCitaDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CoberturaAccidentesTransitoDao</code>, usada por <code>CoberturaAccidentesTransito</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CoberturaAccidentesTransitoDao</code>
	 */
	public CoberturaAccidentesTransitoDao getCoberturaAccidentesTransitoDao(){
		return new OracleCoberturaAccidentesTransitoDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ClasificacionSocioEconomicaDao</code>, usada por <code>ClasificacionSocioEconomica</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ClasificacionSocioEconomicaDao</code>
	 */
	public ClasificacionSocioEconomicaDao getClasificacionSocioEconomicaDao()
	{
	    return new OracleClasificacionSocioEconomicaDao();
	}


	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ContratoDao</code>, usada por <code>Contrato</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ContratoDao</code>
	 */
	public ContratoDao getContratoDao()
	{
	    return new OracleContratoDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConvenioDao</code>, usada por <code>Convenio</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ConvenioDao</code>
	 */
	public ConvenioDao getConvenioDao()
	{
		return new OracleConvenioDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>CuentaDao</code>, usada por <code>Cuenta</code>.
	 * @return la implementacion en Oracle de la interfaz <code>CuentaDao</code>
	 */
	public CuentaDao getCuentaDao () {
		return new OracleCuentaDao();
	}
	
	/**
	 * Retorna la impltacion en Oracle de la interfaz
	 * <code>DespachoMedicamentosDao</code>, usada por <code>DespachoMedicamentos</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>DespachoMedicamentosDao</code>
	 */
	public DespachoMedicamentosDao getDespachoMedicamentosDao()
	{
		return new OracleDespachoMedicamentosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>DespachoPedidosDao</code>, usada por <code>DespachoPedidos</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>DespachoPedidosDao</code>
	 */
	public DespachoPedidosDao getDespachoPedidosDao()
	{
		return new OracleDespachoPedidosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>DevolucionAFarmaciaDao</code>, usada por <code>DevolucionAFarmacia</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>DevolucionAFarmaciaDao</code>
	 */
	public DevolucionAFarmaciaDao getDevolucionAFarmaciaDao()
	{
		return new OracleDevolucionAFarmaciaDao();
	}

	/**
	* Retorna la implementacion en Oracle de la interfaz
	* <code>DocumentoAdjuntoDao</code>, usada por
	* <code>DocumentoAdjunto</code>.
	* @return la implementacion en Oracle de la interfaz
	* <code>DocumentoAdjuntoDao</code>
	*/
	public DocumentoAdjuntoDao getDocumentoAdjuntoDao()
	{
		return new OracleDocumentoAdjuntoDao();
	}

	/**
	* Retorna la implementacion en Oracle de la interfaz
	* <code>DocumentosAdjuntosDao</code>, usada por
	* <code>DocumentosAdjuntos</code>.
	* @return la implementacion en Oracle de la interfaz
	* <code>DocumentosAdjuntosDao</code>
	*/
	public DocumentosAdjuntosDao getDocumentosAdjuntosDao()
	{
		return new OracleDocumentosAdjuntosDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EgresoDao</code>, usada por <code>Egreso</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>EgresosDao</code>
	 */
	public EgresoDao getEgresoDao() {
		return new OracleEgresoDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EmbarazoDao</code>, usada por <code>Embarazo</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>EmbarazoDao</code>
	 */
	public EmbarazoDao getEmbarazoDao()
	{
		return new OracleEmbarazoDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EvolucionDao</code>, usada por <code>Evolucion</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>EvolucionDao</code>
	 */
	public EvolucionDao getEvolucionDao() {
	    return new OracleEvolucionDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EvolucionHospitalariaDao</code>, usada por <code>EvolucionHospitalaria</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>EvolucionHospitalariaDao</code>
	 */
	public EvolucionHospitalariaDao getEvolucionHospitalariaDao() {
		return new OracleEvolucionHospitalariaDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ExcepcionesFarmaciaDao</code>, usada por <code>ExcepcionesFarmacia</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ExcepcionesFarmaciaDao</code>
	 */
	public ExcepcionesFarmaciaDao getExcepcionesFarmaciaDao()
	{
	    return new OracleExcepcionesFarmaciaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ExcepcionesServiciosDao</code>, usada por <code>ExcepcionesServicios</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ExcepcionesServiciosDao</code>
	 */
	public ExcepcionesServiciosDao getExcepcionesServiciosDao()
	{
		return new OracleExcepcionesServiciosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ExcepcionesQXDao</code>, usada por <code>ExcepcionesQX</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ExcepcionesQXDao</code>
	 */
	public ExcepcionesQXDao getExcepcionesQXDao()
	{
		return new OracleExcepcionesQXDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FacturaDao</code>, usada por <code>factura</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>FacturaDao</code>
	 */
	public FacturaDao getFacturaDao()
	{
		return new OracleFacturaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FacturaDao</code>, usada por <code>anulacionfactura</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AnulacionFacturaDao</code>
	 */
	public AnulacionFacturasDao getAnulacionFacturasDao()
	{
		return new OracleAnulacionFacturasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>GeneracionCargosPendientesDao</code>, usada por <code>GeneracionCargosPendientes</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>GeneracionCargosPendientesDao</code>
	 */
	public GeneracionCargosPendientesDao getGeneracionCargosPendientesDao()
	{
		return new OracleGeneracionCargosPendientesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>GruposDao</code>, usada por <code>Grupos</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>GruposDao</code>
	 */
	public GruposDao getGruposDao()
	{
		return new OracleGruposDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>HijoBasicoDao</code>, usada por <code>HijoBasico</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>HijoBasicoDao</code>
	 */
	public HijoBasicoDao getHijoBasicoDao()
	{
		return new OracleHijoBasicoDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>HistoriaClinicaDao</code>, usada por <code>HistoriaClinica</code>.
	 * @return la implementacion en Oracle de la interfaz <code>HistoriaClinicaDao</code>
	 */
	public HistoriaClinicaDao getHistoriaClinicaDao () {
		return new OracleHistoriaClinicaDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>HistoricoAdmisionesDao</code>, usada por <code>HistoricoAdmisiones</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>HistoricoAdmisionesDao</code>
	 */
	public HistoricoAdmisionesDao getHistoricoAdmisionesDao()
	{
		return new OracleHistoricoAdmisionesDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>HistoricoEvolucionesDao</code>, usada por <code>HistoricoEvoluciones</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>EgresosDao</code>
	 */
	public HistoricoEvolucionesDao getHistoricoEvolucionesDao() {
		return new OracleHistoricoEvolucionesDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>HorarioAtencionDao</code>, usada por
	 * <code>HorarioAtencion</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>HorarioAtencionDao</code>
	 */
	public HorarioAtencionDao getHorarioAtencionDao()
	{
		return new OracleHorarioAtencionDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>InformacionParametrizableDao</code>, usada por <code>InformacionParametrizable</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>InformacionParametrizableDao</code>
	 */
	public InformacionParametrizableDao getInformacionParametrizableDao()
	{
	    return new OracleInformacionParametrizableDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>IngresoGeneralDao</code>, usada por <code>IngresoGeneral</code>.
	 * @return la implementacion en Oracle de la interfaz <code>IngresoGeneralDao</code>
	 */
	public IngresoGeneralDao getIngresoGeneralDao () {
	    return new OracleIngresoGeneralDao();
	}

	/**
	* Retorna la implementacion en Oracle de la interfaz
	* <code>SolicitudInterconsultaDao</code>, usada por
	* <code>SolicitarInterconsulta</code>.
	* @return la implementacion en Oracle de la interfaz
	* <code>SolicitudInterconsultaDao</code>
	*/
	public  SolicitudInterconsultaDao getSolicitudInterconsultaDao()
	{
		return new OracleSolicitudInterconsultaDao();
	}

	/**
	* Retorna la implementacion en Oracle de la interfaz <code>ListadoCitasDao</code>,
	* usada por <code>Citas</code>
	* @return Implementacion en Oracle de la interfaz <code>ListadoCitasDao</code>
	*/
	public ListadoCitasDao getListadoCitasDao()
	{
		return new OracleListadoCitasDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>MedicoDao</code>, usada por <code>Medico</code>.
	 * @return la implementacion en Oracle de la interfaz <code>MedicoDao</code>
	 */
	public MedicoDao getMedicoDao () {
		return new OracleMedicoDao();
	}

	/* Cada posible DAO, asociado a cada objeto de la aplicacion que necesite acceder a la base de datos Oracle, debe ser definido a continuacion */

	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>MenuDao</code>, usada por <code>MenuFilter</code>.
	 * @return la implementacion en Oracle de la interfaz <code>MenuDao</code>
	 */
	public MenuDao getMenuDao () {
		return new OracleMenuDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>PacienteDao</code>, usada por <code>Paciente</code>.
	 * @return la implementacion en Oracle de la interfaz <code>PacienteDao</code>
	 */
	public PacienteDao getPacienteDao () {
		return new OraclePacienteDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>ParametrizacionCurvaAlertaDao</code>, usada por <code>ParametrizacionCurvaAlerta</code>.
	 * @return la implementacion en Oracle de la interfaz <code>ParametrizacionCurvaAlertaDao</code>
	 */
	public ParametrizacionCurvaAlertaDao getParametrizacionCurvaAlertaDao () {
		return new OracleParametrizacionCurvaAlertaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>Pooles</code> interactua con la fuente de datos.
	 * @return el DAO usado por <code>Pooles</code>
	 */
	public PoolesDao getPoolesDao (){
		return new OraclePoolesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ParticipacionPoolXTarifasDao</code>, usada por <code>ParticipacionPoolXTarifa</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ParticipacionPoolXTarifasDao</code>
	 */
	public ParticipacionPoolXTarifasDao getParticipacionPoolXTarifaDao()
	{
		return new OracleParticipacionPoolXTarifasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>PersonaBasicaDao</code>, usada por <code>PersonaBasica</code>.
	 * @return la implementacion en Oracle de la interfaz <code>PersonaBasicaDao</code>
	 */
	public PersonaBasicaDao getPersonaBasicaDao()
	{
		return new OraclePersonaBasicaDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>PersonaDao</code>, usada por <code>Persona</code>.
	 * @return la implementacion en Oracle de la interfaz <code>PersonaDao</code>
	 */
	public PersonaDao getPersonaDao () {
	    return new OraclePersonaDao();
	}

	/**
	* Retorna la implementacion en Oracle de la interfaz
	* <code>ProcedimientoDao</code>, usada por <code>Procedimiento</code>.
	* @return la implementacion en Oracle de la interfaz
	* <code>ProcedimientoDao</code>
	*/
	public ProcedimientoDao getProcedimientoDao()
	{
		return new OracleProcedimientoDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RequisitosPacienteDao</code>, usada por <code>RequisitosPaciente</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>RequisitosPacienteDao</code>
	 */
	public RequisitosPacienteDao getRequisitosPacienteDao()
	{
	    return new OracleRequisitosPacienteDao();
	}
	

	
	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>RolesFuncsDao</code>, usada por <code>RolesFuncsBean</code>.
	 * @return la implementacion en Oracle de la interfaz <code>RolesFuncsDao</code>
	 */
	public RolesFuncsDao getRolesFuncsDao () {
	    return new OracleRolesFuncsDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ServiciosDao</code>, usada por
	 * <code>Servicios</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ServiciosDao</code>
	 */
	public ServiciosDao getServiciosDao()
	{ 
		return new OracleServiciosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ServiciosCamas1Dao</code>, usada por
	 * <code>ServiciosCamas1</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ServiciosCamas1Dao</code>
	 */
	public ServiciosCamas1Dao getServiciosCamas1Dao()
	{
		return new OracleServiciosCamas1Dao();
	}

	/**
	* Retorna la implementacion en Oracle de la interfaz
	* <code>SolicitudConsultaExternaDao</code>, usada por <code>SolicitudConsultaExterna</code>
	* @return Implementacion en Oracle de la interfaz <code>SolicitudConsultaExternaDao</code>
	*/
	public SolicitudConsultaExternaDao getSolicitudConsultaExternaDao()
	{
		return new OracleSolicitudConsultaExternaDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>SolicitudDao</code>, usada por
	 * <code>Solicitud</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>SolicitudDao</code>
	 */
	public SolicitudDao getSolicitudDao ()
	{
	    return new OracleSolicitudDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>SolicitudesDao</code>, usada por
	 * <code>Solicitudes</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>SolicitudesDao</code>
	 */
	public SolicitudesDao  getSolicitudesDao()
	{
			return new OracleSolicitudesDao();
	}

    /**
     * Retorna la implementacion en Oracle de la interfaz
     * <code>SolicitudesCxDao</code>, usada por
     * <code>SolicitudesCx</code>.
     * @return la implementacion en Oracle de la interfaz
     * <code>SolicitudesCxDao</code>
     */
    public SolicitudesCxDao  getSolicitudesCxDao()
    {
        return new OracleSolicitudesCxDao();
    }
    
	/**
	* Retorna la implementacion en Oracle de la interfaz <code>SolicitudProcedimientoDao</code>,
	* usada por <code>SolicitudProcedimiento</code>
	* @return Implementacion en Oracle de la interfaz <code>SolicitudProcedimientoDao</code>
	*/
	public SolicitudProcedimientoDao getSolicitudProcedimientoDao()
	{
		return new OracleSolicitudProcedimientoDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>TagDao</code>, usada por los <i>custom tags</i>.
	 * @return la implementacion en Oracle de la interfaz <code>TagDao</code>
	 */
	public TagDao getTagDao () {
		return new OracleTagDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>UsuarioBasicoDao</code>, usada por <code>UsuarioBasico</code>.
	 * @return la implementacion en Oracle de la interfaz <code>UsuarioBasicoDao</code>
	 */
	public UsuarioBasicoDao getUsuarioBasicoDao () {
		return new OracleUsuarioBasicoDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz <code>UsuarioDao</code>, usada por <code>Usuario</code>.
	 * @return la implementacion en Oracle de la interfaz <code>UsuarioDao</code>
	 */
	public UsuarioDao getUsuarioDao () {
		return new OracleUsuarioDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UtilidadValidacionDao</code>, usada por <code>UtilidadValidacion</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>UtilidadValidacionDao</code>
	 */
	public UtilidadValidacionDao getUtilidadValidacionDao()
	{
		return new OracleUtilidadValidacionDao();
	}

	/**
	* Retorna la implementacion en Oracle de la interfaz
	* <code>ValidacionesSolicitudDao</code>, usada por
	* <code>ValidacionesSolicitud</code>.
	* @return la implementacion en Oracle de la interfaz
	* <code>ValidacionesSolicitudDao</code>
	*/
	public ValidacionesSolicitudDao getValidacionesSolicitudDao()
	{
		return new OracleValidacionesSolicitudDao();
	}

	
	
	

	

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EmpresaDao</code>, usada por <code>Empresa</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>EmpresaDao</code>
	 */
	public EmpresaDao getEmpresaDao()
	{
	    return new OracleEmpresaDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TarifaISSDao</code>, usada por <code>TarifaISS</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>TarifaISSDao</code>
	 */
	public TarifaISSDao getTarifaISSDao()
	{
	    return new OracleTarifaISSDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TarifasISSDao</code>, usada por <code>TarifasISS</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>TarifasISSDao</code>
	 */
	public TarifasISSDao getTarifasISSDao()
	{
		return new OracleTarifasISSDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TarifaSOATDao</code>, usada por <code>TarifaSOAT</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>TarifaSOATDao</code>
	 */
	public TarifaSOATDao getTarifaSOATDao()
	{
	    return new OracleTarifaSOATDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TarifasSOATDao</code>, usada por <code>TarifasSOAT</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>TarifasSOATDao</code>
	 */
	public TarifasSOATDao getTarifasSOATDao()
	{
		return new OracleTarifasSOATDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EsquemaTarifarioDao</code>, usada por <code>EsquemaTarifario</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>EsquemaTarifarioDao</code>
	 */
	public EsquemaTarifarioDao getEsquemaTarifarioDao()
	{
		return new OracleEsquemaTarifarioDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EstadoCuentaDao</code>, usada por <code>EstadoCuenta</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>EstadoCuentaDao</code>
	 */
	public EstadoCuentaDao getEstadoCuentaDao()
	{
		return new OracleEstadoCuentaDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ExcepcionesTarifasDao</code>, usada por <code>ExcepcionesTarifas</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ExcepcionesTarifasDao</code>
	 */
	public ExcepcionesTarifasDao getExcepcionesTarifasDao()
	{
	    return new OracleExcepcionesTarifasDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RecargoTarifaDao</code>, usada por <code>RecargoTarifa</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>RecargoTarifaDao</code>
	 */
	public RecargoTarifaDao getRecargoTarifaDao()
	{
	    return new OracleRecargoTarifaDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RecargosTarifasDao</code>, usada por <code>RecargosTarifas</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>RecargosTarifasDao</code>
	 */
	public RecargosTarifasDao getRecargosTarifasDao()
	{
		return new OracleRecargosTarifasDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>SalarioMinimoDao</code>, usada por <code>SalarioMinimo</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>SalarioMinimoDao</code>
	 */
	public SalarioMinimoDao getSalarioMinimoDao()
	{
	    return new OracleSalarioMinimoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RecargoTarifasDao</code>, usada por <code>RecargoTarifas</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>RecargoTarifasDao</code>
	 */
	public RecargoTarifasDao getRecargoTarifasDao()
	{
	    return new OracleRecargoTarifasDao ();
	}
		

	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>MantenimientoTablasDao</code>, usada por <code>MantenimientoTablas</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>MantenimientoTablasDao</code>
	 */
	public MantenimientoTablasDao  getMantenimientoTablasDao(){
		return new OracleMantenimientoTablasDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TerceroDao</code>, usada por <code>Tercero</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>TerceroDao</code>
	 */
	public TerceroDao  getTerceroDao(){
	    return new OracleTerceroDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TopesfacturacionDao</code>, usada por <code>TopesFacturacionEconomica</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>TopesFacturacionDao</code>
	 */
	public TopesFacturacionDao getTopesFacturacionDao()
	{
	    return new OracleTopesFacturacionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ResumenAtencionesDao</code>, usada por <code>ResumenAtencionesDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ResumenAtencionesDao</code>
	 */
	public ResumenAtencionesDao  getResumenAtencionesDao()
	{
		return new OracleResumenAtencionesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>SolicitudEvolucionDao</code>, usada por <code>SolicitudEvolucion</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>SolicitudEvolucionDao</code>
	 */
	public SolicitudEvolucionDao  getSolicitudEvolucionDao()
	{
		return new OracleSolicitudEvolucionDao();
	}

	/**
	 * Retorna la implementacion en Hsqldb de la interfaz
	 * <code>MontosCobroDao</code>, usada por <code>MontosCobro</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>MontosCobroDao</code>
	 */
	public MontosCobroDao getMontosCobroDao()
	{
	    return new OracleMontosCobroDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>MovimientoFacturasDao</code>, usada por <code>MovimientoFacturas</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>MovimientoFacturasDao</code>
	 */
	public MovimientoFacturasDao getMovimientoFacturasDao()
	{
	    return new OracleMovimientoFacturasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>PagosEmpresaDao</code>, usada por <code>PagosEmpresa</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>PagosEmpresaDao</code>
	 */
	public PagosEmpresaDao getPagosEmpresaDao()
	{
		return new OraclePagosEmpresaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>PagosPaciente</code>
	 * interactua con la fuente de datos.
	 * @return
	 */
	public PagosPacienteDao getPagosPacienteDao()
	{
	    return new OraclePagosPacienteDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>MontosCobroDao</code>, usada por <code>MontosCobroDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>MontosCobroDao</code>
	 */
	public ActivacionCargosDao getActivacionCargosDao()
	{
		return new OracleActivacionCargosDao();
	}

	/**
	 * Retorna el Dao con el cual el objeto<code>RegistrosUnidadesConsulta</code>
	 * interactua con la fuente de datos.
	 * @return
	 */
	public RegUnidadesConsultaDao getRegUnidadesConsultaDao ()
	{
	    return new OracleRegUnidadesConsultaDao();
	
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>RegistroDiagnosticos</code>
	 * interactura con la fuente de datos.
	 */
    public RegistroDiagnosticosDao getRegistroDiagnosticosDao() 
    {
        return new OracleRegistroDiagnosticosDao();
    }

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ExcepcionesNaturalezaPacienteDao</code>, usada por <code>ExcepcionesNaturalezaPaciente</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ExcepcionesNaturalezaPacienteDao</code>
	 */
	public ExcepcionesNaturalezaPacienteDao getExcepcionesNaturalezaPacienteDao()
	{
	    return new OracleExcepcionesNaturalezaPacienteDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ValoresPorDefectoDao</code>
	 * interactura con la fuente de datos.
	 */
	public ValoresPorDefectoDao getValoresPorDefectoDao()
	{
		return new OracleValoresPorDefectoDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>RegistroDiagnosticos</code>
	 * interactura con la fuente de datos.
	 */
    public SustitutosInventariosDao  getSustitutosInventariosDao() 
	{
    	return new OracleSustitutosInventariosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>TarifasInventario</code>
	 * interactura con la fuente de datos.
	 */
	public TarifasInventarioDao  getTarifasInventarioDao() 
	{
	    return new OracleTarifasInventarioDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>SolicitudMedicamentos</code>
	 * interactura con la fuente de datos.
	 */
	public SolicitudMedicamentosDao getSolicitudMedicamentosDao()
	{
		return new OracleSolicitudMedicamentosDao();
	}
	
	/**
	 * Retorna el Dao con el cual el objeto<code>RegistrosUnidadesConsulta</code>
	 * interactua con la fuente de datos.
	 * @return
	 */
	public PedidosInsumosDao getPedidosInsumosDao ()
	{
	    return new OraclePedidosInsumosDao();
	
	}

	
	/**
	 * Retorna el DAO con el cual el objeto <code>DevolucionPedidos</code>
	 * interactua con la fuente de datos.
	 * @return
	 */
	public DevolucionPedidosDao getDevolucionPedidosDao(){
	    return new OracleDevolucionPedidosDao();
	}

	
	/**
	 * Retorna el DAO con el cual el objeto <code>DevolucionPedidos</code>
	 * interactua con la fuente de datos.
	 * @return
	 */
	public RecepcionDevolucionPedidosDao getRecepcionDevolucionPedidosDao(){
		return new OracleRecepcionDevolucionPedidosDao();
	}
	/**
	 * Retorna el DAO con el cual el objeto <code>DevolucionPedidos</code>
	 * interactua con la fuente de datos.
	 * @return
	 */
	public RecepcionDevolucionMedicamentosDao getRecepcionDevolucionMedicamentosDao(){
		return new OracleRecepcionDevolucionMedicamentosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ParamInstitucionDao</code>
	 * interactua con la fuente de datos.
	 * @return Objeto <code>ParamInstitucionDao</code> para la comunicacion con la BD Oracle
	 */
	public ParamInstitucionDao getParamInstitucionDao ()
	{
	    return new OracleParamInstitucionDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>MedicosXPoolDao</code>
	 * interactua con la fuente de datos.
	 * @return Objeto <code>MedicosXPoolDao</code> para la comunicacion con la BD Oracle
	 */
	public MedicosXPoolDao getMedicosXPoolDao ()
	{
	    return new OracleMedicosXPoolDao();
	}

	
	/**
	 * Retorna el DAO con el cual el objeto <code>GeneracionExcepcionesFarmaciaDao</code>
	 * interactua con la fuente de datos.
	 * @return Objeto <code>GeneracionExcepcionesFarmaciaDao</code> para la comunicacion con la BD Oracle
	 */
	public GeneracionExcepcionesFarmaciaDao getGeneracionExcepcionesFarmaciaDao ()
	{
	    return new OracleGeneracionExcepcionesFarmaciaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ValidacionesFacturacionDao</code>, usada por <code>ValidacionesFacturacion</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ValidacionesFacturacionDao</code>
	 */
	public ValidacionesFacturaDao getValidacionesFacturaDao ()
	{
	    return new OracleValidacionesFacturaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AbonosYDescuentosDao</code>, usada por <code>AbonosYDescuentos</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AbonosYDescuentosDao</code>
	 */
	public AbonosYDescuentosDao getAbonosYDescuentosDao()
	{
	    return new OracleAbonosYDescuentosDao ();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CuentasCobroDao</code>, usada por <code>CuentasCobro</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CuentasCobroDao</code>
	 */
	public CuentasCobroDao getCuentasCobroDao()
	{
	    return new OracleCuentasCobroDao ();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>DiasFestivosDao</code>, usada por <code>DiasFestivos</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>DiasFestivosDao</code>
	 */
	public DiasFestivosDao getDiasFestivosDao()
	{
	    return new OracleDiasFestivosDao ();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OrdenMedicaDao</code>, usada por <code>OrdenMedicaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>OrdenMedicaDao</code>
	 */
	public OrdenMedicaDao getOrdenMedicaDao()
	{
	    return new OracleOrdenMedicaDao ();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>HojaObstetricaDao</code>, usada por <code>HojaObstetricaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>HojaObstetricaDao</code>
	 */
	public HojaObstetricaDao getHojaObstetricaDao()
	{
	    return new OracleHojaObstetricaDao ();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>HojaOftalmologicaDao</code>, usada por <code>HojaOftalmologicaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>HojaOftalmologicaDao</code>
	 */
	public HojaOftalmologicaDao getHojaOftalmologicaDao()
	{
	    return new OracleHojaOftalmologicaDao ();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>HojaOftalmologicaDao</code>, usada por <code>HojaOftalmologicaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>HojaOftalmologicaDao</code>
	 */
	public AntecedentesOftalmologicosDao getAntecedentesOftalmologicosDao()
	{
	    return new OracleAntecedentesOftalmologicosDao ();
	}

	
	/* ******************SIES********************/
	
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleUtilidadesDao</code>, usada por <code>OracleUtilidades</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>OracleUtilidadesDao</code>
	 */
	public UtilidadesDao getUtilidadesDao()
	{
	    return new OracleUtilidadesDao ();
	}
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TriageDao</code>, usada por <code>TriageDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>TriageDao</code>
	 */
	public TriageDao getTriageDao(){
			return new OracleTriageDao();
		}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ValidacionesCierreCuentaDao</code>, usada por <code>ValidacionesCierreCuenta</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ValidacionesCierreCuentaDao</code>
	 */
	public ValidacionesCierreCuentaDao getValidacionesCierreCuentaDao()
	{
	    return new OracleValidacionesCierreCuentaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>MotivoAnulacionFacturasDao</code>, usada por <code>MotivoAnulacionFacturas</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>MotivoAnulacionFacturasDao</code>
	 */
	public MotivoAnulacionFacturasDao getMotivoAnulacionFacturasDao()
	{
	    return new OracleMotivoAnulacionFacturasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConceptosPagoCarteraDao</code>, usada por <code>ConceptosPagoCarteraDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ConceptosPagoCarteraDao</code>
	 */
	public ConceptosCarteraDao getConceptosCarteraDao()
	{
	    return new OracleConceptosCarteraDao ();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>MotivoAnulacionFacturasDao</code>, usada por <code>ActualizacionAutorizacion</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ActualizacionAutorizacionDao</code>
	 */
	public ActualizacionAutorizacionDao getActualizacionAutorizacionDao()
	{
	    return new OracleActualizacionAutorizacionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>JustificacionDinamicaDao</code>, usada por <code>JustificacionDinamicaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>JustificacionDinamicaDao</code>
	 */
	public JustificacionDinamicaDao getJustificacionDinamicaDao()
	{
	    return new OracleJustificacionDinamicaDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaFacturasDao</code>, usada por <code>ConsultaFacturas</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ConsultaFacturas</code>
	 */
	public ConsultaFacturasDao getConsultaFacturasDao()
	{
	    return new OracleConsultaFacturasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RipsDao</code>, usada por <code>RipsDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>RipsDao</code>
	 */
	public RipsDao getRipsDao()
	{
	    return new OracleRipsDao();
	}
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConceptoTesoreriaDao</code>, usada por <code>ConceptoTesoreriaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ConceptoTesoreriaDao</code>
	 */
	public ConceptoTesoreriaDao getTesoreriaDao()
	{
	    return new OracleConceptoTesoreriaDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TrasladoCamasDao</code>, usada por <code>TrasladoCamasDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>TrasladoCamasDao</code>
	 */
	public TrasladoCamasDao getTrasladoCamasDao()
	{
	    return new OracleTrasladoCamasDao();
	}
	/**
	 * Retorna la implementacion en oracle de la interfaz
	 * <code>ConsecutivosDisponiblesDao</code>, usada por <code>ConsecutivosDisponiblesDao</code>.
	 * @return la implementacion en oracle de la interfaz
	 * <code>ConsecutivosDisponiblesDao</code>
	 */
	public ConsecutivosDisponiblesDao getConsecutivosDisponiblesDao()
	{
	    return new OracleConsecutivosDisponiblesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CierreSaldoInicialCarteraDao</code>, usada por <code>CierreSaldoInicialCarteraDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CierreSaldoInicialCarteraDao</code>
	 */ 
    public CierreSaldoInicialCarteraDao getCierreSaldoInicialCarteraDao() 
    {
       return new OracleCierreSaldoInicialCarteraDao();
    }
    
    /**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ValidacionesAnulacionFacturasDao</code>, usada por <code>ValidacionesAnulacionFacturas</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ValidacionesAnulacionFacturasDao</code>
	 */
	public ValidacionesAnulacionFacturasDao getValidacionesAnulacionFacturasDao()
	{
	    return new OracleValidacionesAnulacionFacturasDao();
	}
    
	  /**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EstanciaAutomaticaDao</code>, usada por <code>EstanciaAutomatica</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>EstanciaAutomaticaDao</code>
	 */
	public EstanciaAutomaticaDao getEstanciaAutomaticaDao()
	{
	    return new OracleEstanciaAutomaticaDao();
	}
	
	 /**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AprobacionAjustesEmpresaDao</code>, usada por <code>AprobacionAjustesEmpresaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AprobacionAjustesEmpresaDao</code>
	 */
	public AprobacionAjustesEmpresaDao getAprobacionAjustesEmpresaDao()
	{
	    return new OracleAprobacionAjustesEmpresaDao();
	}
    
	 /**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TiposMonitoreoDao</code>, usada por <code>TiposMonitoreoDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>TiposMonitoreoDao</code>
	 */
	public TiposMonitoreoDao getTiposMonitoreoDao()
	{
	    return new OracleTiposMonitoreoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TipoSalasDao</code>, usada por <code>TipoSalasDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>TipoSalasDao</code>
	 */
	public TipoSalasDao getTipoSalasDao()
	{
	    return new OracleTipoSalasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>SalasDao</code>, usada por <code>SalasDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>SalasDao</code>
	 */
	public SalasDao getSalasDao()
	{
	    return new OracleSalasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>PorcentajesCxMultiplesDao</code>, usada por <code>PorcentajesCxMultiplesDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>PorcentajesCxMultiplesDao</code>
	 */
	public PorcentajesCxMultiplesDao getPorcentajesCxMultiplesDao()
	{
	    return new OraclePorcentajesCxMultiplesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CajasDao</code>, usada por <code>CajasDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CajasDao</code>
	 */
	public CajasDao getCajaDao()
	{
		return new OracleCajasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CajasCajerosDao</code>, usada por <code>CajasCajerosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CajasCajerosDao</code>
	 */
	public CajasCajerosDao getCajasCajerosDao()
	{
		return new OracleCajasCajerosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FormasPagoDao</code>, usada por <code>FormasPagoDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>FormasPagoDao</code>
	 */
	public FormasPagoDao getFormasPagoDao()
	{
		return new OracleFormasPagoDao();
	}
    
	/**
	 * Retorna el DAO con el cual el objeto <code>EntidadesFinancierasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>EntidadesFinancierasDao</code>
	 */
	public EntidadesFinancierasDao getEntidadesFinancierasDao()
	{
		return new OracleEntidadesFinancierasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ExcepcionAsocioTipoSalaDao</code>, usada por <code>ExcepcionAsocioTipoSalaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ExcepcionAsocioTipoSalaDao</code>
	 */
	public ExcepcionAsocioTipoSalaDao getExcepcionAsocioTipoSalaDao()
	{
		return new OracleExcepcionAsocioTipoSalaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TarjetasFinancierasDao</code>, usada por <code>TarjetasFinancierasDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>TarjetasFinancierasDao</code>
	 */
	public TarjetasFinancierasDao getTarjetasFinancierasDao()
	{
		return new OracleTarjetasFinancierasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RecibosCajaDao</code>, usada por <code>RecibosCajaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>RecibosCajaDao</code>
	 */
	public RecibosCajaDao getRecibosCajaDao()
	{
		return new OracleRecibosCajaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AnulacionRecibosCajaDao</code>, usada por <code>AnulacionRecibosCajaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AnulacionRecibosCajaDao</code>
	 */
	public AnulacionRecibosCajaDao getAnulacionRecibosCajaDao()
	{
		return new OracleAnulacionRecibosCajaDao();
	}

	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>MaterialesQxDao</code>, usada por <code>MaterialesQxDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>MaterialesQxDao</code>
	 */
	public MaterialesQxDao getMaterialesQxDao()
	{
		return new OracleMaterialesQxDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaReciboCajaDao</code>, usada por <code>ConsultaReciboCajaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ConsultaReciboCajaDao</code>
	 */
	public ConsultaReciboCajaDao getConsultaReciboCajaDao()
	{
	    return new OracleConsultaRecibosCajaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>PeticionDao</code>, usada por <code>PeticionDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>PeticionDao</code>
	 */
	public PeticionDao getPeticionDao()
	{
	    return new OraclePeticionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>PreanestesiaDao</code>, usada por <code>PreanestesiaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>PreanestesiaDao</code>
	 */
	public PreanestesiaDao getPreanestesiaDao()
	{
	    return new OraclePreanestesiaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AprobacionPagosCarteraDao</code>, usada por <code>AprobacionPagosCarteraDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AprobacionPagosCarteraDao</code>
	 */
	public AprobacionPagosCarteraDao getAprobacionPagosCarteraDao()
	{
	    return new OracleAprobacionPagosCarteraDao();
	}	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AplicacionPagosEmpresaDao</code>, usada por <code>AplicacionPagosEmpresaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AplicacionPagosEmpresaDao</code>
	 */
	public AplicacionPagosEmpresaDao getAplicacionPagosEmpresaDao()
	{
	    return new OracleAplicacionPagosEmpresaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ResponderCirugiasDao</code>, usada por <code>ResponderCirugias</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ResponderCirugias</code>
	 */
	public ResponderCirugiasDao getResponderCirugiasDao()
	{
	    return new OracleResponderCirugiasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ResponderCirugiasDao</code>, usada por <code>NotasGeneralesEnfermeria</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>NotasGeneralesEnfermeria</code>
	 */
	public NotasGeneralesEnfermeriaDao getNotasGeneralesEnfermeriaDao()
	{
	    return new OracleNotasGeneralesEnfermeriaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>HojaAnestesiaDao</code>, usada por <code>HojaAnestesiaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>HojaAnestesiaDao</code>
	 */
	public HojaAnestesiaDao getHojaAnestesiaDao()
	{
	    return new OracleHojaAnestesiaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TiposTransaccionesInvDao</code>, usada por <code>TiposTransaccionesInvDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>TiposTransaccionesInvDao</code>
	 */
	public TiposTransaccionesInvDao getTiposTransaccionesInvDao()
	{
	    return new OracleTiposTransaccionesInvDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>HojaQuirurgicaDao</code>, usada por <code>HojaQuirurgicaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>HojaQuirurgicaDao</code>
	 */
	public HojaQuirurgicaDao getHojaQuirurgicaDao()
	{
	    return new OracleHojaQuirurgicaDao();
	}
	

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ProgramacionCirugia</code>, usada por <code>ProgramacionCirugiaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ProgramacionCirugia</code>
	 */
	public ProgramacionCirugiaDao getProgramacionCirugiaDao()
	{
	    return new OracleProgramacionCirugiaDao();
	}
	
		/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TransaccionesValidasXCCDao</code>, usada por <code>TransaccionesValidasXCCDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>TransaccionesValidasXCCDao</code>
	 */
	public TransaccionesValidasXCCDao getTransaccionesValidasXCCDao()
	{
	    return new OracleTransaccionesValidasXCCDao();
	}
	

	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>MotDevolucionInventariosDao</code>, usada por <code>MotDevolucionInventariosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>MotDevolucionInventariosDao</code>
	 */
    public MotDevolucionInventariosDao getMotDevolucionInventariosDao()
    {
        return new OracleMotDevolucionInventariosDao(); 
    }
    
    
    /**
	 * Retorna el DAO con el cual el objeto <code>ArticulosPuntoPedidoDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ArticulosPuntoPedidoDao</code>
	 */
    public ArticulosPuntoPedidoDao getArticulosPuntoPedidoDao()
    {
        return new OracleArticulosPuntoPedidoDao(); 
    }
    /**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UsuariosXAlmacenDao</code>, usada por <code>UsuariosXAlmacenDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>UsuariosXAlmacenDao</code>
	 */
	public UsuariosXAlmacenDao getUsuariosXAlmacenDao()
	{
	    return new OracleUsuariosXAlmacenDao();
	}
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>NotasRecuperacionDao</code>, usada por <code>NotasRecuperacion</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>NotasRecuperacion</code>
	 */
	public NotasRecuperacionDao getNotasRecuperacionDao()
	{
	    return new OracleNotasRecuperacionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ModificarReversarQxDao</code>, usada por <code>ModificarReversarQxDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ModificarReversarQxDao</code>
	 */
	public ModificarReversarQxDao getModificarReversarQxDao()
	{
	    return new OracleModificarReversarQxDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UtilidadInventariosDao</code>, usada por <code>UtilidadInventariosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>UtilidadInventariosDao</code>
	 */
	public UtilidadInventariosDao getUtilidadInventariosDao()
	{
	    return new OracleUtilidadInventariosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UtilidadInventariosDao</code>, usada por <code>UtilidadInventariosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>UtilidadInventariosDao</code>
	 */
	public UtilidadLaboratoriosDao getUtilidadLaboratoriosDao()
	{
	    return new OracleUtilidadLaboratoriosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RegistroTransaccionesDao</code>, usada por <code>RegistroTransaccionesDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>RegistroTransaccionesDao</code>
	 */
	public RegistroTransaccionesDao getRegistroTransaccionesDao() 
	{
	    return new OracleRegistroTransaccionesDao();
    }
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>PresupuestoPacienteDao</code>, usada por <code>PresupuestoPacienteDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>PresupuestoPacienteDao</code>
	 */
	public PresupuestoPacienteDao getPresupuestoPacienteDao() 
	{
	    return new OraclePresupuestoPacienteDao();
    }
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>PresupuestoPacienteDao</code>, usada por <code>CierresInventarioDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CierresInventarioDao</code>
	 */
	public CierresInventarioDao getCierresInventarioDao() 
	{
	    return new OracleCierresInventarioDao();
    }
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>KardexDao</code>, usada por <code>KardexDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>KardexDao</code>
	 */
	public KardexDao getKardexDao() 
	{
	    return new OracleKardexDao();
    }
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaHonorariosMedicosDao</code>, usada por <code>ConsultaHonorariosMedicosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ConsultaHonorariosMedicosDao</code>
	 */
	public ConsultaHonorariosMedicosDao getConsultaHonorariosMedicosDao() 
	{
	    return new OracleConsultaHonorariosMedicosDao();
    }
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ExistenciasInventariosDao</code>, usada por <code>ExistenciasInventariosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ExistenciasInventariosDao</code>
	 */
	public ExistenciasInventariosDao getExistenciasInventariosDao() 
	{
	    return new OracleExistenciasInventariosDao();
    }
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>SolicitudTrasladoAlmacenDao</code>, usada por <code>SolicitudTrasladoAlmacenDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>SolicitudTrasladoAlmacenDao</code>
	 */
	public SolicitudTrasladoAlmacenDao getSolicitudTrasladoAlmacenDao() 
	{
	    return new OracleSolicitudTrasladoAlmacenDao();
    }
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FormatoImpresionPresupuestoDao</code>, usada por <code>FormatoImpresionPresupuestoDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>FormatoImpresionPresupuestoDao</code>
	 */
	public FormatoImpresionPresupuestoDao getFormatoImpresionPresupuestoDao() 
	{
	    return new OracleFormatoImpresionPresupuestoDao();
    }
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaPresupuestoDao</code>, usada por <code>ConsultaPresupuestoDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ConsultaPresupuestoDao</code>
	 */
	public ConsultaPresupuestoDao getConsultaPresupuestoDao() 
	{
	    return new OracleConsultaPresupuestoDao();
    }
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>GruposServiciosDao</code>, usada por <code>GruposServiciosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>GruposServiciosDao</code>
	 */
	public GruposServiciosDao getGruposServiciosDao() 
	{
	    return new OracleGruposServiciosDao();
    }
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>DespachoTrasladoAlmacenDao</code>, usada por <code>DespachoTrasladoAlmacenDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>DespachoTrasladoAlmacenDao</code>
	 */
	public DespachoTrasladoAlmacenDao getDespachoTrasladoAlmacenDao() 
	{
	    return new OracleDespachoTrasladoAlmacenDao();
    }
    
    /**
     * Retorna la implementacion en Oracle de la interfaz
     * <code>ConsultaImpresionTrasladosDao</code>, usada por <code>ConsultaImpresionTrasladosDao</code>.
     * @return la implementacion en Oracle de la interfaz
     * <code>ConsultaImpresionTrasladosDao</code>
     */
    public ConsultaImpresionTrasladosDao getConsultaImpresionTrasladosDao() 
    {
        return new OracleConsultaImpresionTrasladosDao();
    }
	
    
    /**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FormatoImpresionFacturaDao</code>, usada por <code>FormatoImpresionFacturaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>FormatoImpresionFacturaDao</code>
	 */
	public FormatoImpresionFacturaDao getFormatoImpresionFacturaDao() 
	{
	    return new OracleFormatoImpresionFacturaDao();
    }
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RegistroEnfermeriaDao</code>, usada por <code>RegistroEnfermeriaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>RegistroEnfermeriaDao</code>
	 */
	public RegistroEnfermeriaDao getRegistroEnfermeriaDao() 
	{
	    return new OracleRegistroEnfermeriaDao();
    }
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RegistroEnfermeriaDao</code>, usada por <code>RegistroEnfermeriaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>RegistroEnfermeriaDao</code>
	 */
	public ImpresionFacturaDao getImpresionFacturaDao()
	{
		return new OracleImpresionFacturaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ExTarifasAsociosDao</code>, usada por <code>ExTarifasAsociosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ExTarifasAsociosDao</code>
	 */
	public ExTarifasAsociosDao getExTarifasAsociosDao()
	{
		return new OracleExTarifasAsociosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaProfesionalPoolDao</code>, usada por <code>ConsultaProfesionalPoolDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ConsultaProfesionalPoolDao</code>
	 */
	public ConsultaProfesionalPoolDao getConsultaProfesionalPoolDao()
	{
		return new OracleConsultaProfesionalPoolDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaProfesionalPoolDao</code>, usada por <code>ConsultaTarifasServiciosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ConsultaTarifasServiciosDao</code>
	 */
	public ConsultaTarifasServiciosDao getConsultaTarifasServiciosDao()
	{
		return new OracleConsultaTarifasServiciosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CuentaInventarioDao</code>, usada por <code>CuentaInventarioDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CuentaInventarioDao</code>
	 */
	public CuentaInventarioDao getCuentaInventarioDao()
	{
		return new OracleCuentaInventarioDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>getCuentaServicioDao</code>, usada por <code>getCuentaServicioDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>getCuentaServicioDao</code>
	 */
	public CuentaServicioDao getCuentaServicioDao()
	{
		return new OracleCuentaServicioDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>getCuentasConveniosDao</code>, usada por <code>getCuentasConveniosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>getCuentasConveniosDao</code>
	 */
	public CuentasConveniosDao getCuentasConveniosDao()
	{
		return new OracleCuentasConveniosDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>getArqueosDao</code>, usada por <code>getArqueosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>getArqueosDao</code>
	 */
	public ArqueosDao getArqueosDao()
	{
		return new OracleArqueosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CampoInterfazDao</code>, usada por <code>CampoInterfazDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CampoInterfazDao</code>
	 */
	public CampoInterfazDao getCampoInterfazDao()
	{
		return new OracleCampoInterfazDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ParamInterfazDao</code>, usada por <code>ParamInterfaz</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ParamInterfazDao</code>
	 */
	public ParamInterfazDao getParamInterfazDao()
	{
		return new OracleParamInterfazDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UnidadFuncionalDao</code>, usada por <code>UnidadFuncional</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>UnidadFuncionalDao</code>
	 */
	public UnidadFuncionalDao getUnidadFuncionalDao()
	{
		return new OracleUnidadFuncionalDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CuposExtraDao</code>, usada por <code>CuposExtra</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CuposExtraDao</code>
	 */
	public CuposExtraDao getCuposExtraDao()
	{
		return new OracleCuposExtraDao();
	}
	
	 /**
	 * Retorna el DAO con el cual el objeto <code>ReasignarAgendaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ReasignarAgenda</code>
	 */
	public ReasignarAgendaDao getReasignarAgendaDao()
	{
		return new OracleReasignarAgendaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaLogCuposExtraDao</code>
	 * @return
	 */
	public ConsultaLogCuposExtraDao getConsultaLogCuposExtraDao()
	{
	    return new OracleConsultaLogCuposExtraDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CentrosCostoDao</code>
	 * @return
	 */
	public CentrosCostoDao getCentrosCostoDao()
	{
	    return new OracleCentrosCostoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CentroCostoGrupoServicioDao</code>
	 * @return
	 */
	public CentroCostoGrupoServicioDao getCentroCostoGrupoServicioDao()
	{
	    return new OracleCentroCostoGrupoServicioDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>getCentrosAtencionDao</code>, usada por <code>getCentrosAtencionDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>getCentrosAtencionDao</code>
	 */
	public CentrosAtencionDao getCentrosAtencionDao()
	{
		return new OracleCentrosAtencionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>getCentrosCostoXUnidadConsultaDao</code>, usada por <code>getCentrosCostoXUnidadConsultaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>getCentrosCostoXUnidadConsultaDao</code>
	 */
	public CentrosCostoXUnidadConsultaDao getCentrosCostoXUnidadConsultaDao()
	{
	    return new OracleCentrosCostoXUnidadConsultaDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultarCentrosCostoDao</code>, usada por <code>ConsultarCentrosCostoDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ConsultarCentrosCostoDao</code>
	 */
	public ConsultarCentrosCostoDao getConsultarCentrosCostoDao()
	{
	    return new OracleConsultarCentrosCostoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>getCuentaServicioDao</code>, usada por <code>getCuentaUnidadFuncionalDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>getCuentaServicioDao</code>
	 */
	public CuentaUnidadFuncionalDao getCuentaUnidadFuncionalDao()
	{
		return new OracleCuentaUnidadFuncionalDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>getCentrosCostoViaIngresoDao</code>, usada por <code>getCentrosCostoViaIngresoDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>getCentrosCostoViaIngresoDao</code>
	 */
	public CentrosCostoViaIngresoDao getCentrosCostoViaIngresoDao()
	{
		return new OracleCentrosCostoViaIngresoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>GruposEtareosDao</code>, usada por <code>GruposEtareosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>GruposEtareosDao</code>
	 */
	public GruposEtareosDao getGruposEtareosDao()
	{
		return new OracleGruposEtareosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>GruposEtareosDao</code>, usada por <code>ConsultaGruposEtareosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ConsultaGruposEtareosDao</code>
	 */
	public ConsultaGruposEtareosDao getConsultaGruposEtareosDao()
	{
		return new OracleConsultaGruposEtareosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>SistemasMotivoConsultaDao</code>, usada por <code>SistemasMotivoConsultaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>SistemasMotivoConsultaDao</code>
	 */
	public SistemasMotivoConsultaDao getSistemasMotivoConsultaDao()
	{
		return new OracleSistemasMotivoConsultaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>DestinoTriageDao</code>, usada por <code>DestinoTriageDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>DestinoTriageDao</code>
	 */
	public DestinoTriageDao getDestinoTriageDao()
	{
		return new OracleDestinoTriageDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ArticulosXMezclaDao</code>, usada por <code>getUnidadMedidaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>UnidadMedidaDao</code>
	 */
	public UnidadMedidaDao getUnidadMedidaDao()
	{
		return new OracleUnidadMedidaDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>SignosSintomasXSistemaDao</code>, usada por <code>getSignosSintomasXSistemaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>UnidadMedidaDao</code>
	 */
	public SignosSintomasXSistemaDao getSignosSintomasXSistemaDao()
	{
		return new OracleSignosSintomasXSistemaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ArticulosXMezclaDao</code>, usada por <code>getUnidadMedidaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>UnidadMedidaDao</code>
	 */
	public SignosSintomasDao getSignosSintomasDao()
	{
		return new OracleSignosSintomasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UnidadMedidaDao</code>, usada por <code>ArticulosXMezclaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ArticulosXMezclaDao</code>
	 */
	public ArticulosXMezclaDao getArticulosXMezclaDao()
	{
		return new OracleArticulosXMezclaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>MezclasDao</code>, usada por <code>Mezcla</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>MezclasDao</code>
	 */
	public MezclasDao getMezclasDao()
	{
		return new OracleMezclasDao();
	}
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CategoriasTriageDao</code>, usada por <code>CategoriasTriageDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CategoriasTriageDao</code>
	 */
	public CategoriasTriageDao getCategoriasTriageDao()
	{
		return new OracleCategoriasTriageDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaPacientesTriageDao</code>, usada por <code>ConsultaPacientesTriageDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ConsultaPacientesTriageDao</code>
	 */
	public ConsultaPacientesTriageDao getConsultaPacientesTriageDao()
	{
		return new OracleConsultaPacientesTriageDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>PacientesTriageUrgenciasDao</code>, usada por <code>PacientesTriageUrgenciasDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>PacientesTriageUrgenciasDao</code>
	 */
	public PacientesTriageUrgenciasDao getPacientesTriageUrgenciasDao()
	{
		return new OraclePacientesTriageUrgenciasDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>PacientesTriageUrgenciasDao</code>, usada por <code>PacientesUrgenciasPorValorarDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>PacientesUrgenciasPorValorarDao</code>
	 */
	public PacientesUrgenciasPorValorarDao getPacientesUrgenciasPorValorarDao()
	{
		return new OraclePacientesUrgenciasPorValorarDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>InformacionPartoDao</code>, usada por <code>InformacionPartoDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>InformacionPartoDao</code>
	 */
	public InformacionPartoDao getInformacionPartoDao()
	{
		return new OracleInformacionPartoDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>InformacionPartoDao</code>, usada por <code>NivelAtencionDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>InformacionPartoDao</code>
	 */
	public NivelAtencionDao getNivelAtencionDao()
	{
		return new OracleNivelAtencionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CuentaCobroCapitacionDao</code>, usada por <code>CuentaCobroCapitacionDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CuentaCobroCapitacionDao</code>
	 */
	public CuentaCobroCapitacionDao getCuentaCobroCapitacionDao()
	{
		return new OracleCuentaCobroCapitacionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>GeneracionInterfazDao</code>, usada por <code>GeneracionInterfazDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>GeneracionInterfazDao</code>
	 */
	public GeneracionInterfazDao getGeneracionInterfazDao()
	{
		return new OracleGeneracionInterfazDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RadicacionCuentasCobroCapitacionDao</code>, usada por <code>RadicacionCuentasCobroCapitacionDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>RadicacionCuentasCobroCapitacionDao</code>
	 */
	public RadicacionCuentasCobroCapitacionDao getRadicacionCuentasCobroCapitacionDao()
	{
		return new OracleRadicacionCuentasCobroCapitacionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CuentasProcesoFacturacionDao</code>, usada por <code>CuentasProcesoFacturacionDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CuentasProcesoFacturacionDao</code>
	 */
	public CuentasProcesoFacturacionDao getCuentasProcesoFacturacionDao()
	{
		return new OracleCuentasProcesoFacturacionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>PacientesUrgenciasSalaEsperaDao</code>, usada por <code>PacientesUrgenciasSalaEsperaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>PacientesUrgenciasSalaEsperaDao</code>
	 */
	public PacientesUrgenciasSalaEsperaDao getPacientesUrgenciasSalaEsperaDao()
	{
		return new OraclePacientesUrgenciasSalaEsperaDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>PacientesUrgenciasPorHospitalizarDao</code>, usada por <code>PacientesUrgenciasPorHospitalizarDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>PacientesUrgenciasPorHospitalizarDao</code>
	 */
	public PacientesUrgenciasPorHospitalizarDao getPacientesUrgenciasPorHospitalizarDao()
	{
		return new OraclePacientesUrgenciasPorHospitalizarDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>TiposProgamaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>TiposProgamaDao</code>
	 */
	public TiposProgamaDao getTiposProgamaDao()
	{
		return new OracleTiposProgamaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>GrupoEtareoCrecimientoDesarrolloDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>GrupoEtareoCrecimientoDesarrolloDao</code>
	 */
	public GrupoEtareoCrecimientoDesarrolloDao getGrupoEtareoCrecimientoDesarrolloDao()
	{
		return new OracleGrupoEtareoCrecimientoDesarrolloDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>AsociarCxCAFacturasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>AsociarCxCAFacturasDao</code>
	 */
	public AsociarCxCAFacturasDao getAsociarCxCAFacturasDao()
	{
		return new OracleAsociarCxCAFacturasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ActividadesPypDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ActividadesPypDao</code>
	 */
	public ActividadesPypDao getActividadesPypDao()
	{
		return new OracleActividadesPypDao();
	}
	
	
		
	/**
	 * Retorna el DAO con el cual el objeto <code>CierresCarteraCapitacionDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>CierresCarteraCapitacionDao</code>
	 */
	public CierresCarteraCapitacionDao getCierresCarteraCapitacionDao()
	{
		return new OracleCierresCarteraCapitacionDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ProgramasActividadesConvenioDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ProgramasActividadesConvenioDao</code>
	 */
	public ProgramasActividadesConvenioDao getProgramasActividadesConvenioDao()
	{
		return new OracleProgramasActividadesConvenioDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ActividadesProgramasPYPDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ActividadesProgramasPYPDao</code>
	 */
	public ActividadesProgramasPYPDao getActividadesProgramasPYPDao()
	{
		return new OracleActividadesProgramasPYPDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>MetasPYPDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>MetasPYPDao</code>
	 */
	public MetasPYPDao getMetasPYPDao()
	{
		return new OracleMetasPYPDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>RegistroSaldosInicialesCapitacionDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>RegistroSaldosInicialesCapitacionDao</code>
	 */
	public RegistroSaldosInicialesCapitacionDao getRegistroSaldosInicialesCapitacionDao()
	{
		return new OracleRegistroSaldosInicialesCapitacionDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ProgramasPYPDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ProgramasPYPDao</code>
	 */
	public ProgramasPYPDao getProgramasPYPDao()
	{
		return new OracleProgramasPYPDao();
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>OrdenesAmbulatoriasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>OrdenesAmbulatoriasDao</code>
	 */
	public OrdenesAmbulatoriasDao getOrdenesAmbulatoriasDao()
	{
		return new OracleOrdenesAmbulatoriasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>TrasladoCentroAtencionDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>TrasladoCentroAtencionDao</code>
	 */
	public TrasladoCentroAtencionDao getTrasladoCentroAtencionDao()
	{
		return new OracleTrasladoCentroAtencionDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ImpresionResumenAtencionesDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ImpresionResumenAtencionesDao</code>
	 */
	public ImpresionResumenAtencionesDao getImpresionResumenAtencionesDao()
	{
		return new OracleImpresionResumenAtencionesDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>MotivosSircDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>MotivosSircDao</code>
	 */
	public MotivosSircDao getMotivosSircDao()
	{
		return new OracleMotivosSircDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>InstitucionesSircDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>InstitucionesSircDao</code>
	 */
	public InstitucionesSircDao getInstitucionesSircDao()
	{
		return new OracleInstitucionesSircDao();
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ServiciosSircDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ServiciosSircDao</code>
	 */
	public ServiciosSircDao getServiciosSircDao()
	{
		return new OracleServiciosSircDao();
	}


	
	/**
	 * Retorna el DAO con el cual el objeto <code>ActEjecutadasXCargarDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ActEjecutadasXCargarDao</code>
	 */
	public ActEjecutadasXCargarDao getActEjecutadasXCargarDao()
	{
		return new OracleActEjecutadasXCargarDao();
	}
	/**
	 * Retorna el DAO con el cual el objeto <code>IndicativoSolicitudGrupoServiciosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>IndicativoSolicitudGrupoServiciosDao</code>
	 */
	public IndicativoSolicitudGrupoServiciosDao getIndicativoSolicitudGrupoServiciosDao()
	{
		return new OracleIndicativoSolicitudGrupoServiciosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>IndicativoCargoViaIngresoServicioDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>IndicativoCargoViaIngresoServicioDao</code>
	 */
	public IndicativoCargoViaIngresoServicioDao getIndicativoCargoViaIngresoServicioDao()
	{
		return new OracleIndicativoCargoViaIngresoServicioDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>RespuestaProcedimientosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>RespuestaProcedimientosDao</code>
	 */	
	public RespuestaProcedimientosDao getRespuestaProcedimientosDao()
	{
		return new OracleRespuestaProcedimientosDao();
	}
	

	/**
	 * Retorna el DAO con el cual el objeto <code>InterpretarProcedimientoDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>InterpretarProcedimientoDao</code>
	 */
	public InterpretarProcedimientoDao getInterpretarProcedimientoDao()
	{
		return new OracleInterpretarProcedimientoDao();
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>InformacionRecienNacidosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>InformacionRecienNacidosDao</code>
	 */

	public InformacionRecienNacidosDao getInformacionRecienNacidosDao()
	{
		return new OracleInformacionRecienNacidosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ImpresionCLAPDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ImpresionCLAPDao</code>
	 */
	public ImpresionCLAPDao getImpresionCLAPDao()
	{
		return new  OracleImpresionCLAPDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>BusquedaDiagnosticosGenericaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>BusquedaDiagnosticosGenericaDao</code>
	 */
	public BusquedaDiagnosticosGenericaDao getBusquedaDiagnosticosGenericaDao()
	{
		return new  OracleBusquedaDiagnosticosGenericaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>TrasladosCajaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>TrasladosCajaDao</code>
	 */
	public TrasladosCajaDao getTrasladosCajaDao()
	{
		return new  OracleTrasladosCajaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaWSDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaWSDao</code>
	 */
	public ConsultaWSDao getConsultaWSDao()
	{
		return new  OracleConsultaWSDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultoriosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultoriosDao</code>
	 */
	public ConsultoriosDao getConsultoriosDao()
	{
		return new  OracleConsultoriosDao();
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>TiposInventarioDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>TiposInventarioDao</code>
	 */
	public TiposInventarioDao getTiposInventarioDao()
	{
		return new OracleTiposInventarioDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>HojaGastosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>HojaGastosDao</code>
	 */
	public HojaGastosDao getHojaGastosDao()
	{
		return new OracleHojaGastosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ArticulosFechaVencimientoDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ArticulosFechaVencimientoDao</code>
	 */
	public ArticulosFechaVencimientoDao getArticulosFechaVencimientoDao()
	{
		return new OracleArticulosFechaVencimientoDao();
	}
	
	
	/**
	 *Retorna el DAO con el cual el objeto <code>FormaFarmaceuticaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>FormaFarmaceutica</code>
	 */
	
	public FormaFarmaceuticaDao getFormaFarmaceuticaDao()
	{
		return new OracleFormaFarmaceuticaDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>PaquetesDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>PaquetesDao</code>
	 */
	public PaquetesDao getPaquetesDao()
	{
		return new OraclePaquetesDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>NaturalezaArticulosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>NaturalezaArticulosDao</code>
	 */
	public NaturalezaArticulosDao getNaturalezaArticulosDao()
	{
		return new OracleNaturalezaArticulosDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ViasIngresoDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ViasIngresoDao</code>
	 */
	public ViasIngresoDao getViasIngresoDao()
	{
		return new OracleViasIngresoDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>CierreIngresoDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>CierreIngresoDao</code>
	 */
	public CierreIngresoDao getCierreIngresoDao()
	{
		return new OracleCierreIngresoDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ConsultaCierreAperturaIngresoDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaCierreAperturaIngresoDao</code>
	 */
	public ConsultaCierreAperturaIngresoDao getConsultaCierreAperturaIngresoDao()
	{
		return new OracleConsultaCierreAperturaIngresoDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>PacientesHospitalizadosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>PacientesHospitalizadosDao</code>
	 */
	public PacientesHospitalizadosDao getPacientesHospitalizadosDao()
	{
		return new OraclePacientesHospitalizadosDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>UbicacionGeograficaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>UbicacionGeograficaDao</code>
	 */
	public UbicacionGeograficaDao getUbicacionGeograficaDao()
	{
		return new OracleUbicacionGeograficaDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ArticuloCatalogoDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ArticuloCatalogoDao</code>
	 */
	public ArticuloCatalogoDao getArticuloCatalogoDao()
	{
		return new OracleArticuloCatalogoDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>DevolucionInventariosPacienteDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>DevolucionInventariosPacienteDao</code>
	 */
	public DevolucionInventariosPacienteDao getDevolucionInventariosPacienteDao()
	{
		return new OracleDevolucionInventariosPacienteDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ConsultarAdmisionDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultarAdmisionDao</code>
	 */
	public ConsultarAdmisionDao getConsultarAdmisionDao()
	{
		return new OracleConsultarAdmisionDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>TiposMonedaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>TiposMonedaDao</code>
	 */
	public TiposMonedaDao getTiposMonedaDao()
	{
		return new OracleTiposMonedaDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ServiciosViaAccesoDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ServiciosViaAccesoDa</code>
	 */
	public ServiciosViaAccesoDao getServiciosViaAccesoDao()
	{
		return new OracleServiciosViaAccesoDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ParamArchivoPlanoColsanitasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ParamArchivoPlanoColsanitasDao</code>
	 */
	public ParamArchivoPlanoColsanitasDao getParamArchivoPlanoColsanitasDao()
	{
		return new OracleParamArchivoPlanoColsanitasDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>PaquetesConvenioDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>PaquetesConvenioDao</code>
	 */
	public PaquetesConvenioDao getPaquetesConvenioDao()
	{
		return new OraclePaquetesConvenioDao();
	}
	
	
	/**
	 *Retorna el DAO con el cual el objeto <code>InclusionesExclusionesDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>InclusionesExclusionesDao</code>
	 */
	public InclusionesExclusionesDao getInclusionesExclusionesDao()
	{
		return new OracleInclusionesExclusionesDao();
	}
	
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ServiciosGruposEsteticosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ServiciosGruposEsteticosDao</code>
	 */
	public ServiciosGruposEsteticosDao getServiciosGruposEsteticosDao()
	{
		return new OracleServiciosGruposEsteticosDao();
	}	
	
	
	
	/**
	 *Retorna el DAO con el cual el objeto <code>CoberturaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>CoberturaDao</code>
	 */
	public CoberturaDao getCoberturaDao()
	{
		return new OracleCoberturaDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>CuentaInvUnidadFunDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>CuentaInvUnidadFunDao</code>
	 */
	public CuentaInvUnidadFunDao getCuentaInvUnidadFunDao()
	{
		return new OracleCuentaInvUnidadFunDao();
	}
	
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ConsultaAjustesEmpresaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaAjustesEmpresaDao</code>
	 */
	public ConsultaAjustesEmpresaDao getConsultaAjustesEmpresaDao()
	{
		return new OracleConsultaAjustesEmpresaDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ConsultaAjustesEmpresaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaAjustesEmpresaDao</code>	
	 */
	public ExamenCondiTomaDao getExamenCondiTomaDao()
	{
		return new OracleExamenCondiTomaDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>TiposConvenioDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>TiposConvenioDao</code>	
	 */
	public TiposConvenioDao getTiposConvenioDao()
	{
		return new OracleTiposConvenioDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>UnidadProcedimientoDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>UnidadProcedimientoDao</code>
	 */
	public UnidadProcedimientoDao getUnidadProcedimientoDao()
	{
		return new OracleUnidadProcedimientoDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>FosygaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>FosygaDao</code>
	 */
	public FosygaDao getFosygaDao()
	{
		return new OracleFosygaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadesFacturacionDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>UtilidadesFacturacionDao</code>
	 */
	public UtilidadesFacturacionDao getUtilidadesFacturacionDao()
	{
		return new OracleUtilidadesFacturacionDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>RegistroEventosCatastroficosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>RegistroEventosCatastroficosDao</code>
	 */
	public RegistroEventosCatastroficosDao getRegistroEventosCatastroficosDao()
	{
		return new OracleRegistroEventosCatastroficosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>GeneracionAnexosForecatDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>GeneracionAnexosForecatDao</code>
	 */
	public GeneracionAnexosForecatDao getGeneracionAnexosForecatDao()
	{
		return new OracleGeneracionAnexosForecatDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>PisosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>PisosDao</code>	
	 */
	public PisosDao getPisosDao()
	{
		return new OraclePisosDao();
	}

	/**
	 *Retorna el DAO con el cual el objeto <code>ComponentesPaquetesDaos</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ComponentesPaquetesDao</code>	
	 */
	public ComponentesPaquetesDao getComponentesPaquetesDao()
	{
		return new OracleComponentesPaquetesDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>TipoHabitacionDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>TipoHabitacionDao</code>	
	 */
	public TipoHabitacionDao getTipoHabitacionDao()
	{
		return new OracleTipoHabitacionDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>UtilidadesHistoriaClinicaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>UtilidadesHistoriaClinicaDao</code>	
	 */
	public UtilidadesHistoriaClinicaDao getUtilidadesHistoriaClinicaDao()
	{
		return new OracleUtilidadesHistoriaClinicaDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>DetalleCoberturaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>DetalleCoberturaDao</code>
	 */
	public DetalleCoberturaDao getDetalleCoberturaDao()
	{
		return new OracleDetalleCoberturaDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>AlmacenParametrosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>AlmacenParametrosDao</code>
	 */
	public  AlmacenParametrosDao getAlmacenParametrosDao()
	{
		return new OracleAlmacenParametrosDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>HabitacionesDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>HabitacionesDao</code>
	 */
	public  HabitacionesDao getHabitacionesDao()
	{
		return new OracleHabitacionesDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ReferenciaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ReferenciaDao</code>
	 */
	public  ReferenciaDao getReferenciaDao()
	{
		return new OracleReferenciaDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ReportesEstadosCarteraDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ReportesEstadosCarteraDao</code>
	 */
	public  ReportesEstadosCarteraDao getReportesEstadosCarteraDao()
	{
		return new OracleReportesEstadosCarteraDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>CoberturasConvenioDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>CoberturasConvenioDao</code>
	 */
	public CoberturasConvenioDao getCoberturasConvenioDao()
	{
		return new OracleCoberturasConvenioDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>TiposUsuarioCamaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>TiposUsuarioCamaDao</code>	
	 */
	public TiposUsuarioCamaDao getTiposUsuarioCamaDao()
	{
		return new OracleTiposUsuarioCamaDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>EdadCarteraDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>EdadCarteraDao</code>
	 */
	public  EdadCarteraDao getEdadCarteraDao()
	{
		return new OracleEdadCarteraDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>DescuentosComercialesDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>DescuentosComercialesDao</code>
	 */
	public DescuentosComercialesDao getDescuentosComercialesDao()
	{
		return new OracleDescuentosComercialesDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>EdadCarteraCapitacionDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>EdadCarteraCapitacionDao</code>
	 */
	public  EdadCarteraCapitacionDao getEdadCarteraCapitacionDao()
	{
		return new OracleEdadCarteraCapitacionDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>DetalleInclusionesExclusionesDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>DetalleInclusionesExclusionesDao</code>
	 */
	public DetalleInclusionesExclusionesDao getDetalleInclusionesExclusionesDao()
	{
		return new OracleDetalleInclusionesExclusionesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TramiteReferencia</code> 
	 */
	public TramiteReferenciaDao getTramiteReferenciaDao()
	{
		return new OracleTramiteReferenciaDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>TiposAmbulanciaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>TiposAmbulanciaDao</code>	
	 */
	public TiposAmbulanciaDao getTiposAmbulanciaDao()
	{
		return new OracleTiposAmbulanciaDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>InclusionExclusionConvenioDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>InclusionExclusionConvenioDao</code>
	 */
	public InclusionExclusionConvenioDao getInclusionExclusionConvenioDao()
	{
		return new OracleInclusionExclusionConvenioDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>RegistroEventosCatastroficosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>RegistroEventosCatastroficosDao</code>
	 */
	public ContrarreferenciaDao getContrarreferenciaDao()
	{
		return new OracleContrarreferenciaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ExcepcionesTarifas1Dao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ExcepcionesTarifas1Dao</code>
	 */
	public ExcepcionesTarifas1Dao getExcepcionesTarifas1Dao()
	{
		return new OracleExcepcionesTarifas1Dao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadesManejoPacienteDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>UtilidadesManejoPacienteDao</code>
	 */
	public UtilidadesManejoPacienteDao getUtilidadesManejoPacienteDao()
	{
		return new OracleUtilidadesManejoPacienteDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaReferenciaContrareferenciaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaReferenciaContrareferenciaDao</code>
	 */
	public ConsultaReferenciaContrareferenciaDao getConsultaReferenciaContrareferenciaDao()
	{
		return new OracleConsultaReferenciaContrareferenciaDao();
	}	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>CargosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>CargosDao</code>
	 */
	public CargosDao getCargosDao()
	{
		return new OracleCargosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>BusquedaBarriosGenericaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>BusquedaBarriosGenericaDao</code>
	 */
	public BusquedaBarriosGenericaDao getBusquedaBarriosGenericaDao()
	{
		return new OracleBusquedaBarriosGenericaDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>PaquetizacionDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>PaquetizacionDao</code>
	 */
	public PaquetizacionDao getPaquetizacionDao()
	{
		return new OraclePaquetizacionDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>DocumentosGarantiaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>DocumentosGarantiaDao</code>
	 */
	public DocumentosGarantiaDao getDocumentosGarantiaDao()
	{
		return new OracleDocumentosGarantiaDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>DocumentosGarantiaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>DocumentosGarantiaDao</code>
	 */
	public ApliPagosCarteraPacienteDao getApliPagosCarteraPacienteDao()
	{
		return new OracleApliPagosCarteraPacienteDao();
	}
	
	
	/**
	 *Retornala implementacion en Oracle de la interfaz 
	 */
	public ConsentimientoInformadoDao getConsentimientoInformadoDao()
	{
		return new OracleConsentimientoInformadoDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>DistribucionCuentaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>DistribucionCuentaDao</code>
	 */
	public DistribucionCuentaDao getDistribucionCuentaDao() 
	{
		return new OracleDistribucionCuentaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>RevisionCuentaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>RevisionCuentaDao</code>
	 */
	public RevisionCuentaDao getRevisionCuentaDao()
	{
		return new OracleRevisionCuentaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>CondicionesXServicioDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>CondicionesXServicioDao</code>
	 */
	public CondicionesXServicioDao getCondicionesXServicioDao()
	{
		return new OracleCondicionesXServiciosDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ConsultaTarifasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaTarifasDao</code>	
	 */
	public ConsultaTarifasDao getConsultaTarifasDao()
	{
		return new OracleConsultaTarifasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>CondicionesXServicioDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>CondicionesXServicioDao</code>
	 */
	public ReporteProcedimientosEsteticosDao getReporteProcedimientosEsteticosDao()
	{
		return new OracleReporteProcedimientosEsteticosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>CargosAutomaticosPresupuestoDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>CargosAutomaticosPresupuestoDao</code>
	 */
	public CargosAutomaticosPresupuestoDao getCargosAutomaticosPresupuestoDao()
	{
		return new OracleCargosAutomaticosPresupuestoDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>CensoCamasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>CensoCamasDao</code>
	 */
	public CensoCamasDao getCensoCamasDao() {
		return new OracleCensoCamasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ListadoIngresos</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ListadoIngresos</code>
	 */
	public ListadoIngresosDao getListadoIngresosDao() {
		return new OracleListadoIngresosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>EquivalentesDeInventarioDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>EquivalentesDeInventarioDao</code>
	 */
	public EquivalentesDeInventarioDao  getEquivalentesDeInventarioDao(){
		return new OracleEquivalentesDeInventarioDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>AsociosXTipoServicioDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>AsociosXTipoServicioDao</code>
	 */
	public AsociosXTipoServicioDao  getAsociosXTipoServicioDao(){
		return new OracleAsociosXTipoServicioDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>PendienteFacturarDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>PendienteFacturarDao</code>
	 */
	public PendienteFacturarDao getPendienteFacturarDao()
	{
		return new OraclePendienteFacturarDao();
	}
		
	/**
	 * Retorna el DAO con el cual el objeto <code>ReliquidacionTarifasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ReliquidacionTarifasDao</code>
	 */
	public ReliquidacionTarifasDao getReliquidacionTarifasDao()
	{
		return new OracleReliquidacionTarifasDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>AnulacionCargosFarmaciaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>AnulacionCargosFarmaciaDao</code>
	 */
	public AnulacionCargosFarmaciaDao getAnulacionCargosFarmaciaDao()
	{
		return new OracleAnulacionCargosFarmaciaDao();	
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultarActivarCamasReservadasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultarActivarCamasReservadasDao</code>
	 */
	public ConsultarActivarCamasReservadasDao getConsultarActivarCamasReservadasDao()
	{
		return new OracleConsultarActivarCamasReservadasDao();	
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConceptosPagoPoolesDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConceptosPagoPoolesDao</code>
	 */
	public ConceptosPagoPoolesDao getConceptosPagoPoolesDao()
	{
		return new OracleConceptosPagoPoolesDao();	
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>FactorConversionMonedasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>FactorConversionMonedasDao</code>
	 */
	public FactorConversionMonedasDao getFactorConversionMonedasDao()
	{
		return new OracleFactorConversionMonedasDao();	
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConceptosPagoPoolesXConvenioDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConceptosPagoPoolesXConvenioDao</code>
	 */
	public ConceptosPagoPoolesXConvenioDao getConceptosPagoPoolesXConvenioDao()
	{
		return new OracleConceptosPagoPoolesXConvenioDao();	
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UtilidadesAdministracionDao</code> 
	 */
	public UtilidadesAdministracionDao getUtilidadesAdministracionDao()
	{
		return new OracleUtilidadesAdministracionDao ();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UtilConversionMonedaDao</code> 
	 */
	public UtilConversionMonedasDao getUtilConversionMonedasDao()
	{
		return new OracleUtilConversionMonedasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UtilidadesConsultaExternaDao</code> 
	 */
	public UtilidadesConsultaExternaDao getUtilidadesConsultaExternaDao()
	{
		return new OracleUtilidadesConsultaExternaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 */
	public ReingresoSalidaHospiDiaDao getReingresoSalidaHospiDiaDao()
	{
		return new OracleReingresoSalidaHospiDiaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RegistroTerapiasGrupalesDao</code> 
	 */
	public RegistroTerapiasGrupalesDao getRegistroTerapiasGrupalesDao() 
	{
		return new OracleRegistroTerapiasGrupalesDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ArchivoPlanoColsaDao</code> 
	 */
	public ArchivoPlanoColsaDao getArchivoPlanoColsaDao()
	{
		return new OracleArchivoPlanoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaIngresosHospitalDiaDao</code> 
	 */
	public ConsultaIngresosHospitalDiaDao getConsultaIngresosHospitalDiaDao()
	{
		return new OracleConsultaIngresosHospitalDiaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaIngresosHospitalDiaDao</code> 
	 */
	public AsociosXRangoTiempoDao getAsociosXRangoTiempoDao()
	{
		return new OracleAsociosXRangoTiempoDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AsociosSalaCirugiaDao</code> 
	 */
	public AsocioSalaCirugiaDao getAsocioSalaCirugiaDao()
	{
		return new OracleAsocioSalaCirugiaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AsocioServicioTarifaDao</code> 
	 */
	public AsocioServicioTarifaDao getAsocioServicioTarifaDao()
	{
		return new OracleAsocioServicioTarifaDao();
	}	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>DespachoPedidoQxDao</code> 
	 */
	public DespachoPedidoQxDao getDespachoPedidoQxDao()
	{
		return new OracleDespachoPedidoQxDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>PacientesEntidadesSubConDao</code> 
	 */
	public PacientesEntidadesSubConDao getPacientesEntidadesSubConDao()
	{
		return new OraclePacientesEntidadesSubConDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>LecturaPlanosEntidadesDao</code> 
	 */
	public LecturaPlanosEntidadesDao getLecturaPlanosEntidadesDao()
	{
		return new OracleLecturaPlanosEntidadesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EntidadesSubContratadasDao</code> 
	 */
	public EntidadesSubContratadasDao getEntidadesSubContratadasDao()
	{
		return new OracleEntidadesSubContratadasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EntidadesSubContratadasDao</code> 
	 */
	public ParametrosEntidadesSubContratadasDao getParametrosEntidadesSubContratadasDao()
	{
		return new OracleParametrosEntidadesSubContratadasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CargosDirectosDao</code> 
	 */
	public CargosDirectosDao getCargosDirectosDao()
	{
		return new OracleCargosDirectosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>SeccionesDao</code> 
	 */
	public SeccionesDao getSeccionesDao()
	{
		return new OracleSeccionesDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>MovimientosAlmacenesDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>MovimientosAlmacenesDao</code>
	 */
	public MovimientosAlmacenesDao getMovimientosAlmacenesDao()
	{
		return new OracleMovimientosAlmacenesDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ArticulosConsumidosPacientesDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ArticulosConsumidosPacientesDao</code>
	 */
	public ArticulosConsumidosPacientesDao getArticulosConsumidosPacientesDao()
	{
		return new OracleArticulosConsumidosPacientesDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>OcupacionDiariaCamasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>OcupacionDiariaCamasDao</code>
	 */
	public OcupacionDiariaCamasDao getOcupacionDiariaCamasDao()
	{
		return new OracleOcupacionDiariaCamasDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ListadoCamasHospitalizacionDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ListadoCamasHospitalizacionDao</code>
	 */
	public ListadoCamasHospitalizacionDao getListadoCamasHospitalizacionDao()
	{
		return new OracleListadoCamasHospitalizacionDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>TotalOcupacionCamasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>TotalOcupacionCamasDao</code>
	 */
	public TotalOcupacionCamasDao getTotalOcupacionCamasDao()
	{
		return new OracleTotalOcupacionCamasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ArticulosPorAlmacenDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>ArticulosPorAlmacenDao</code>
	 */
	public ArticulosPorAlmacenDao getArticulosPorAlmacenDao()
	{
		return new OracleArticulosPorAlmacenDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>FarmaciaCentroCostoDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>FarmaciaCentroCostoDao</code>
	 */
	public FarmaciaCentroCostoDao getFarmaciaCentroCostoDao()
	{
		return new OracleFarmaciaCentroCostoDao();	
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConceptosFacturasVarias</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConceptosFacturasVariasDao</code>
	 */
	public ConceptosFacturasVariasDao getConceptosFacturasVariasDao()
	{
		return new OracleConceptosFacturasVariasDao();	
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>Deudores</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>Deudores</code>
	 */
	public DeudoresDao getDeudoresDao()
	{
		return new OracleDeudoresDao();	
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConceptosFacturasVariasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConceptosFacturasVariasDao</code>
	 */
	public GenModFacturasVariasDao getGenModFacturasVariasDao()
	{
		return new OracleGenModFacturasVariasDao();		
	}
	

	/**
	 * Retorna el DAO con el cual el objeto <code>BusquedaTercerosGenericaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>BusquedaTercerosGenericaDao</code>
	 */
	public BusquedaTercerosGenericaDao getBusquedaTercerosGenericaDao()
	{
		return new OracleBusquedaTercerosGenericaDao();		
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>PreparacionTomaInventario</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>PreparacionTomaInventarioDao</code>
	 */
	public PreparacionTomaInventarioDao getPreparacionTomaInventarioDao()
	{
		return new OraclePreparacionTomaInventarioDao();
	}
	
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>Imprimir lista conteo</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ImpListaConteoDao</code>
	 */
	public ImpListaConteoDao getImpListaConteoDao() {
		
		return new OracleImpListaConteoDao();
	
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>LiquidacionServiciosDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>LiquidacionServiciosDao</code>
	 */
	public LiquidacionServiciosDao getLiquidacionServiciosDao()
	{
		return new OracleLiquidacionServiciosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadesSalasDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>UtilidadesSalasDao</code>
	 */
	public UtilidadesSalasDao getUtilidadesSalasDao()
	{
		return new OracleUtilidadesSalasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>SignosVitalesDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>SignosVitalesDao</code>
	 */
	public SignosVitalesDao getSignosVitalesDao()
	{
		return new OracleSignosVitalesDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>MotivoCierreAperturaIngresosDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>MotivoCierreAperturaIngresosDao</code>
	 */
	public MotivoCierreAperturaIngresosDao getMotivoCierreAperturaIngresosDao()
	{
		return new OracleMotivoCierreAperturaIngresosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>RegistroConteoInventarioDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>RegistroConteoInventarioDao</code>
	 */
	public RegistroConteoInventarioDao getRegistroConteoInventarioDao()
	{
		return new OracleRegistroConteoInventarioDao();
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>SustitutosNoPosDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>SustitutosNoPosDao</code>
	 */
	public SustitutosNoPosDao getSustitutosNoPosDao()
	{
		return new OracleSustitutosNoPosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>InterfazSistemaUnoDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>InterfazSistemaUnoDao</code>
	 */
	public InterfazSistemaUnoDao getInterfazSistemaUnoDao()
	{
		return new OracleInterfazSistemaUnoDao();
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaInventarioFisicoArticulosDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaInventarioFisicoArticulosDao</code>
	 */
	public ConsultaInventarioFisicoArticulosDao getConsultaInventarioFisicoArticulosDao()
	{
		return new OracleConsultaInventarioFisicoArticulosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>AjustesXInventarioFisicoDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>AjustesXInventarioFisicoDao</code>
	 */
	public AjustesXInventarioFisicoDao getAjustesXInventarioFisicoDao()
	{
		return new OracleAjustesXInventarioFisicoDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsumosCentrosCostoDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>ConsumosCentrosCostoDao</code>
	 */
	public ConsumosCentrosCostoDao getConsumosCentrosCostoDao()
	{
		return new OracleConsumosCentrosCostoDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>EventosDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>EventosDao</code>
	 */
	public EventosDao getEventosDao()
	{
		return new OracleEventosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaDevolucionPedidosDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaDevolucionPedidosDao</code>
	 */
	public ConsultaDevolucionPedidosDao getConsultaDevolucionPedidosDao()
	{
		return new OracleConsultaDevolucionPedidosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>GasesHojaAnestesiaDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>GasesHojaAnestesiaDao</code>
	 */
	public GasesHojaAnestesiaDao getGasesHojaAnestesiaDao()
	{
		return new OracleGasesHojaAnestesiaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaDevolucionInventarioPacienteDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaDevolucionInventarioPacienteDao</code>
	 */
	public ConsultaDevolucionInventarioPacienteDao getConsultaDevolucionInventarioPacienteDao()
	{
		return new OracleConsultaDevolucionInventarioPacienteDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>MonitoreoHemodinamicaDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>MonitoreoHemodinamicaDao</code>
	 */
	public MonitoreoHemodinamicaDao getMonitoreoHemodinamicaDao()
	{
		return new OracleMonitoreoHemodinamicaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>TecnicaAnestesiaDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>TecnicaAnestesiaDao</code>
	 */
	public TecnicaAnestesiaDao getTecnicaAnestesiaDao()
	{
		return new OracleTecnicaAnestesiaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>InfoGeneralHADao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>InfoGeneralHADao</code>
	 */
	public InfoGeneralHADao getInfoGeneralHADao()
	{
		return new OracleInfoGeneralHADao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaFacturasVariasDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaFacturasVariasDao</code>
	 */
	public ConsultaFacturasVariasDao getConsultaFacturasVariasDao()
	{
		return new OracleConsultaFacturasVariasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>TiposTratamientosOdontologicosDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>TiposTratamientosOdontologicosDao</code>
	 */
	public TiposTratamientosOdontologicosDao getTiposTratamientosOdontologicosDao()
	{
		return new OracleTiposTratamientosOdontologicosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ViasAereasDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>ViasAereasDao</code>
	 */
	public ViasAereasDao getViasAereasDao()
	{
		return new OracleViasAereasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>CargosDirectosCxDytDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>CargosDirectosCxDytDao</code>
	 */
	public CargosDirectosCxDytDao getCargosDirectosCxDytDao()
	{
		return new OracleCargosDirectosCxDytDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>FormatoJustArtNoposDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>FormatoJustArtNoposDao</code>
	 */
	public FormatoJustArtNoposDao getFormatoJustArtNoposDao()
	{
		return new OracleFormatoJustArtNoposDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>FormatoJustServNoposDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>FormatoJustServNoposDao</code>
	 */
	public FormatoJustServNoposDao getFormatoJustServNoposDao()
	{
		return new OracleFormatoJustServNoposDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>FormatoJustServNoposDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>FormatoJustServNoposDao</code>
	 */
	public IntubacionesHADao getInfusionesHADao()
	{
		return new OracleIntubacionesHADao();
	}
	
	/**
	 * Retorna la implementacion en oracle de la interfaz
	 * <code>AprobacionAnulacionDevolucionesDao</code> 
	 */
	public AprobacionAnulacionDevolucionesDao getAprobacionAnulacionDevolucionesDao()
	{
		return new OracleAprobacionAnulacionDevolucionesDao();
	}
	
	/**
	 * Retorna la implementacion en oracle de la interfaz
	 * <code>AprobacionAnulacionFacturasVariasDao</code> 
	 */
	public AprobacionAnulacionFacturasVariasDao getAprobacionAnulacionFacturasVariasDao()
	{
		return new OracleAprobacionAnulacionFacturasVariasDao();
	}
	
	/**
	 * Retorna la implementacion en oracle de la interfaz
	 * <code>PagosFacturasVariasDao</code> 
	 */
	public PagosFacturasVariasDao getPagosFacturasVariasDao()
	{
		return new OraclePagosFacturasVariasDao();
	}
	
	/**
	 * Retorna la implementacion en oracle de la interfaz
	 * <code>AprobacionAnulacionPagosFacturasVariasDao</code> 
	 */
	public AprobacionAnulacionPagosFacturasVariasDao getAprobacionAnulacionPagosFacturasVariasDao()
	{
		return new OracleAprobacionAnulacionPagosFacturasVariasDao();
	}
	
	/**
	 * Retorna la implementacion en oracle de la interfaz
	 * <code>AprobacionAnulacionPagosFacturasVariasDao</code> 
	 */
	public ConsultaImpresionPagosFacturasVariasDao getConsultaImpresionPagosFacturasVariasDao()
	{
		return new OracleConsultaImpresionPagosFacturasVariasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>RegistroDevolucionRecibosCajaDao</code> 
	 */
	public RegistroDevolucionRecibosCajaDao getRegistroDevolucionRecibosCajaDao()
	{
		return new OracleRegistroDevolucionRecibosCajaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaImpresionDevolucionDao</code> 
	 */
	public ConsultaImpresionDevolucionDao getConsultaImpresionDevolucionDao()
	{
		return new OracleConsultaImpresionDevolucionDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>MotivosDevolucionRecibosCajaDao</code> 
	 */
	public MotivosDevolucionRecibosCajaDao getMotivosDevolucionRecibosCajaDao()
	{
		return new OracleMotivosDevolucionRecibosCajaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>RegistroRipsCargosDirectosDao</code> 
	 */
	public RegistroRipsCargosDirectosDao getRegistroRipsCargosDirectosDao()
	{
		return new OracleRegistroRipsCargosDirectosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CopiarTarifasEsquemaTarifarioDao</code> 
	 */
	public CopiarTarifasEsquemaTarifarioDao getCopiarTarifasEsquemaTarifarioDao()
	{
		return new OracleCopiarTarifasEsquemaTarifarioDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaTarifasArticulosDao</code> 
	 */
	public ConsultaTarifasArticulosDao getConsultaTarifasArticulosDao()
	{
		return new OracleConsultaTarifasArticulosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ActualizacionAutomaticaEsquemaTarifarioDao</code> 
	 */
	public ActualizacionAutomaticaEsquemaTarifarioDao getActualizacionAutomaticaEsquemaTarifarioDao()
	{
		return new OracleActualizacionAutomaticaEsquemaTarifarioDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadJustificacionPendienteArtServDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>UtilidadJustificacionPendienteArtServDao</code>
	 */
	public UtilidadJustificacionPendienteArtServDao getUtilidadJustificacionPendienteArtServDao()
	{
		return new OracleUtilidadJustificacionPendienteArtServDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadJustificacionPendienteArtServDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>UtilidadJustificacionPendienteArtServDao</code>
	 */
	public AccesosVascularesHADao getAccesosVascularesHADao()
	{
		return new OracleAccesosVascularesHADao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>EscalasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>EscalasDao</code>
	 */
	public EscalasDao getEscalasDao()
	{
		return new OracleEscalasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>GeneracionModificacionAjustesFacturasVariasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>GeneracionModificacionAjustesFacturasVariasDao</code>
	 */
	public GeneracionModificacionAjustesFacturasVariasDao getGeneracionModificacionAjustesFacturasVariasDao()
	{
		return new OracleGeneracionModificacionAjustesFacturasVariasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>JustificacionNoPosServDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>JustificacionNoPosServDao</code>
	 */
	public JustificacionNoPosServDao getJustificacionNoPosServDao()
	{
		return new OracleJustificacionNoPosServDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ValoracionesDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ValoracionesDao</code>
	 */
	public ValoracionesDao getValoracionesDao()
	{
		return new OracleValoracionesDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ParametrizacionComponentesDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ParametrizacionComponentesDao</code>
	 */
	public ParametrizacionComponentesDao getParametrizacionComponentesDao()
	{
		return new OracleParametrizacionComponentesDao();
	}
	
	
	/**
	 * Retorna la implementacion en oracle de la interfaz
	 * <code>RecaudoCarteraEventoDao</code> 
	 */
	public RecaudoCarteraEventoDao getRecaudoCarteraEventoDao()
	{
		return new OracleRecaudoCarteraEventoDao();
	}
	
		/**
	 * Retorna el DAO con el cual el objeto <code>MotivosSatisfaccionInsatisfaccionDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>MotivosSatisfaccionInsatisfaccionDao</code>
	 */
	public MotivosSatisfaccionInsatisfaccionDao getMotivosSatisfaccionInsatisfaccionDao()
	{
		return new OracleMotivosSatisfaccionInsatisfaccionDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>PlantillasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>PlantillasDao</code>
	 */
	public PlantillasDao getPlantillasDao()
	{
		return new OraclePlantillasDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EncuestaCalidadAtencionDao</code> 
	 */
	
	public EncuestaCalidadAtencionDao getEncuestaCalidadAtencionDao() {
		return new OracleEncuestaCalidadAtencionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RegistroEventosAdversosDao</code> 
	 */
	public RegistroEventosAdversosDao getRegistroEventosAdversosDao() {
		return new OracleRegistroEventosAdversosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ParamArchivoPlanoIndCalidadDao</code> 
	 */	
	public ParamArchivoPlanoIndCalidadDao getParamArchivoPlanoIndCalidadDao() {
		return new OracleParamArchivoPlanoIndCalidadDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EventosAdversosDao</code> 
	 */
	public EventosAdversosDao getEventosAdversosDao() {
		return new OracleEventosAdversosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EventosAdversosDao</code> 
	 */
	public GeneracionArchivoPlanoIndicadoresCalidadDao getGeneracionArchivoPlanoIndicadoresCalidadDao() {
		return new OracleGeneracionArchivoPlanoIndicadoresCalidadDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ParametrizacionPlantillasDao</code> 
	 */
	public ParametrizacionPlantillasDao getParametrizacionPlantillasDao()
	{
		return new OracleParametrizacionPlantillasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UnidadAgendaUsuarioCentroDao</code> 
	 */	
	public UnidadAgendaUsuarioCentroDao getUnidadAgendaUsuarioCentroDao() {
		return new OracleUnidadAgendaUsuarioCentroDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RegistroResumenParcialHistoriaClinicaDao</code> 
	 */
	public RegistroResumenParcialHistoriaClinicaDao getRegistroResumenParcialHistoriaClinicaDao() {
		return new OracleRegistroResumenParcialHistoriaClinicaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AperturaIngresosDao</code> 
	 */
	public AperturaIngresosDao getAperturaIngresosDao() {
		return new OracleAperturaIngresosDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaPreingresosDao</code> 
	 */
	public ConsultaPreingresosDao getConsultaPreingresosDao() {
		return new OracleConsultaPreingresosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>MotivosCancelacionCitaDao</code> 
	 */
	public MotivosCancelacionCitaDao getMotivosCancelacionCitaDao() {
		return new OracleMotivosCancelacionCitaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsumosFacturadosDao</code> 
	 */
	public ConsumosFacturadosDao getConsumosFacturadosDao() {
		return new OracleConsumosFacturadosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsumosFacturadosDao</code> 
	 */
	public LiberacionCamasIngresosCerradosDao getLiberacionCamasIngresosCerradosDao() {
		return new OracleLiberacionCamasIngresosCerradosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleValoracionPacientesCuidadosEspecialesDao</code> 
	 */
	public ValoracionPacientesCuidadosEspecialesDao getValoracionPacientesCuidadosEspecialesDao() {
		return new OracleValoracionPacientesCuidadosEspecialesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleEpicrisis1Dao</code> 
	 */
	public Epicrisis1Dao getEpicrisis1Dao() {
		return new OracleEpicrisis1Dao();
	}
	
		/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleAsignarCitasControlPostOperatorioDao</code> 
	 */
	public AsignarCitasControlPostOperatorioDao getAsignarCitasControlPostOperatorioDao() {
		return new OracleAsignarCitasControlPostOperatorioDao();
	}
	
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleAsignacionCamaCuidadoEspecialDao</code> 
	 */
	public AsignacionCamaCuidadoEspecialAPisoDao getAsignacionCamaCuidadoEspecialAPisoDao() {
		return new OracleAsignacionCamaCuidadoEspecialAPisoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleAsignacionCamaCuidadoEspecialDao</code> 
	 */
	public AsignacionCamaCuidadoEspecialDao getAsignacionCamaCuidadoEspecialDao() {
		return new OracleAsignacionCamaCuidadoEspecialDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OraclePacientesConAtencionDao</code> 
	 */
	public PacientesConAtencionDao getPacientesConAtencionDao() {
		return new OraclePacientesConAtencionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OraclePacientesConAtencionDao</code> 
	 */
	public ConsultaProgramacionCirugiasDao getConsultaProgramacionCirugiasDao() {
		return new OracleConsultaProgramacionCirugiasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleUtilidadesOrdenesMedicasDao</code> 
	 */
	public UtilidadesOrdenesMedicasDao getUtilidadesOrdenesMedicasDao() {
		return new OracleUtilidadesOrdenesMedicasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleConsultaImpresionMaterialesQxDao</code> 
	 */
	public ConsultaImpresionMaterialesQxDao getConsultaImpresionMaterialesQxDao()
	{
		return new OracleConsultaImpresionMaterialesQxDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleDevolucionPedidoQxDao</code> 
	 */
	public DevolucionPedidoQxDao getDevolucionPedidoQxDao()
	{
		return new OracleDevolucionPedidoQxDao();
	}

	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleTrasladoSolicitudesPorTransplantesDao</code> 
	 */
	public TrasladoSolicitudesPorTransplantesDao getTrasladoSolicitudesPorTransplantesDao()
	{
		return new OracleTrasladoSolicitudesPorTransplantesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleConsultarIngresosPorTransplantesDao</code> 
	 */
	public ConsultarIngresosPorTransplantesDao getConsultarIngresosPorTransplantesDao()
	{
		return new OracleConsultarIngresosPorTransplantesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleCostoInventarioPorFacturarDao</code> 
	 */
	public CostoInventarioPorFacturarDao getCostoInventarioPorFacturarDao()
	{
		return new OracleCostoInventarioPorFacturarDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleEvolucionesDao</code> 
	 */
	public EvolucionesDao getEvolucionesDao()
	{
		return new OracleEvolucionesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleCostoVentasPorInventarioDao</code> 
	 */
	public CostoVentasPorInventarioDao getCostoVentasPorInventarioDao()
	{
		return new OracleCostoVentasPorInventarioDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleCostoInventariosPorAlmacenDao</code> 
	 */
	public CostoInventariosPorAlmacenDao getCostoInventariosPorAlmacenDao()
	{
		return new OracleCostoInventariosPorAlmacenDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleConsolidadoFacturacionDao</code> 
	 */
	public ConsolidadoFacturacionDao getConsolidadoFacturacionDao()
	{
		return new OracleConsolidadoFacturacionDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleFacturadosPorConvenioDao</code> 
	 */
	public FacturadosPorConvenioDao getFacturadosPorConvenioDao()
	{
		return new OracleFacturadosPorConvenioDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OraclePacientesPorFacturarDao</code> 
	 */
	public PacientesPorFacturarDao getPacientesPorFacturarDao()
	{
		return new OraclePacientesPorFacturarDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleServiciosXTipoTratamientoOdontologicoDao</code> 
	 */
	public ServiciosXTipoTratamientoOdontologicoDao getServiciosXTipoTratamientoOdontologicoDao()
	{
		return new OracleServiciosXTipoTratamientoOdontologicoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleDiagnosticosOdontologicosATratarDao</code> 
	 */
	public DiagnosticosOdontologicosATratarDao getDiagnosticosOdontologicosATratarDao()
	{
		return new OracleDiagnosticosOdontologicosATratarDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleConsultaPaquetesFacturadosDao</code> 
	 */
	public ConsultaPaquetesFacturadosDao getConsultaPaquetesFacturadosDao()
	{
		return new OracleConsultaPaquetesFacturadosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleValoresTipoReporteDao</code> 
	 */
	public ValoresTipoReporteDao getValoresTipoReporteDao()
	{
		return new OracleValoresTipoReporteDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleConsultaMovimientoDeudorDao</code> 
	 */
	public ConsultaMovimientoDeudorDao getConsultaMovimientoDeudorDao()
	{
		return new OracleConsultaMovimientoDeudorDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleConsultaMovimientoDeudorDao</code> 
	 */
	public EstadisticasServiciosDao getEstadisticasServiciosDao()
	{
		return new OracleEstadisticasServiciosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleConsultaMovimientoDeudorDao</code> 
	 */
	public CalidadAtencionDao getCalidadAtencionDao()
	{
		return new OracleCalidadAtencionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UsuariosAutorizarAnulacionFacturasDao</code> 
	 */
	public UsuariosAutorizarAnulacionFacturasDao getUsuariosAutorizarAnulacionFacturasDao()
	{
		return new OracleUsuariosAutorizarAnulacionFacturasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleConsultaMovimientoFacturaDao</code> 
	 */
	public ConsultaMovimientoFacturaDao getConsultaMovimientoFacturaDao()
	{
		return new OracleConsultaMovimientoFacturaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleReporteMortalidadDao</code> 
	 */
	public ReporteMortalidadDao getReporteMortalidadDao()
	{
		return new OracleReporteMortalidadDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleSolicitarAnulacionFacturasDao</code> 
	 */
	public SolicitarAnulacionFacturasDao getSolicitarAnulacionFacturasDao()
	{
		return new OracleSolicitarAnulacionFacturasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleConsultaSaldosCierresInventariosDao</code> 
	 */
	public ConsultaSaldosCierresInventariosDao getConsultaSaldosCierresInventariosDao()
	{
		return new OracleConsultaSaldosCierresInventariosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ReporteEstadisticoConsultaExDao</code> 
	 */
	public ReporteEstadisticoConsultaExDao getReporteEstadisticoConsultaExDao()
	{ 
		return new OracleReporteEstadisticoConsultaExDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaCostoArticulosDao</code> 
	 */
	public ConsultaCostoArticulosDao getConsultaCostoArticulosDao()
	{ 
		return new OracleConsultaCostoArticulosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AutorizarAnulacionFacturasDao</code> 
	 */
	public AutorizarAnulacionFacturasDao getAutorizarAnulacionFacturasDao()
	{
		return new OracleAutorizarAnulacionFacturasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AprobacionAnulacionAjustesFacturasVariasDao</code> 
	 */
	public AprobacionAnulacionAjustesFacturasVariasDao getAprobacionAnulacionAjustesFacturasVariasDao()
	{
		return new OracleAprobacionAnulacionAjustesFacturasVariasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConceptosGeneralesDao</code> 
	 */
	public ConceptosGeneralesDao getConceptosGeneralesDao()
	{
		return new OracleConceptosGeneralesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaImpresionAjustesFacturasVariasDao</code> 
	 */
	public ConsultaImpresionAjustesFacturasVariasDao getConsultaImpresionAjustesFacturasVariasDao()
	{
		return new OracleConsultaImpresionAjustesFacturasVariasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ReporteEgresosEstanciasDao</code> 
	 */
	public ReporteEgresosEstanciasDao getReporteEgresosEstanciasDao()
	{
		return new OracleReporteEgresosEstanciasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConceptosEspecificosDao</code> 
	 */
	public ConceptosEspecificosDao getConceptosEspecificosDao()
	{
		return new OracleConceptosEspecificosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>VentasCentroCostoDao</code> 
	 */
	public VentasCentroCostoDao getVentasCentroCostoDao()
	{
		return new OracleVentasCentroCostoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>VentasEmpresaConvenioDao</code> 
	 */
	public VentasEmpresaConvenioDao getVentasEmpresaConvenioDao()
	{
		return new OracleVentasEmpresaConvenioDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsumosPorFacturarPacientesHospitalizadosDao</code> 
	 */
	public ConsumosPorFacturarPacientesHospitalizadosDao getConsumosPorFacturarPacientesHospitalizadosDao()
	{
		return new OracleConsumosPorFacturarPacientesHospitalizadosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ReporteReferenciaInternaDao</code> 
	 */
	public ReporteReferenciaInternaDao getReporteReferenciaInternaDao()
	{
		return new OracleReporteReferenciaInternaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EstadisticasIngresosDao</code> 
	 */
	public EstadisticasIngresosDao getEstadisticasIngresosDao()
	{
		return new OracleEstadisticasIngresosDao();
	}
	

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>Servicios_ArticulosIncluidosEnOtrosProcedimientosDao</code> 
	 */
	public Servicios_ArticulosIncluidosEnOtrosProcedimientosDao getServicios_ArticulosIncluidosEnOtrosProcedimientosDao()
	{
		return new OracleServicios_ArticulosIncluidosEnOtrosProcedimientosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EstadisticasIngresosDao</code> 
	 */
	public ReporteReferenciaExternaDao getReporteReferenciaExternaDao()
	{
		return new OracleReporteReferenciaExternaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>GrupoEspecialArticulosDao</code> 
	 */
	public GrupoEspecialArticulosDao getGrupoEspecialArticulosDao()
	{
		return new OracleGrupoEspecialArticulosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TextoRespuestaProcedimientosDao</code> 
	 */
	public TextosRespuestaProcedimientosDao getTextosRespuestaProcedimientosDao()
	{
		return new OracleTextosRespuestaProcedimientosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TextoRespuestaProcedimientosDao</code> 
	 */
	public SolicitudMedicamentosXDespacharDao getSolicitudMedicamentosXDespacharDao()
	{
		return new OracleSolicitudMedicamentosXDespacharDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>MedicamentosControladosAdministradosDao</code> 
	 */
	public MedicamentosControladosAdministradosDao getMedicamentosControladosAdministradosDao()
	{
		return new OracleMedicamentosControladosAdministradosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TotalFacturadoConvenioContratoDao</code> 
	 */
	public TotalFacturadoConvenioContratoDao getTotalFacturadoConvenioContratoDao()
	{
		return new OracleTotalFacturadoConvenioContratoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsumosXFacturarDao</code> 
	 */
	public ConsumosXFacturarDao getConsumosXFacturarDao()
	{
		return new OracleConsumosXFacturarDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsumosXFacturarDao</code> 
	 */
	public ConsultaMovimientosConsignacionesDao getConsultaMovimientosConsignacionesDao()
	{
		return new OracleConsultaMovimientosConsignacionesDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>MovimientosAlmacenConsignacionDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>MovimientosAlmacenConsignacionDao</code>
	 */
	public MovimientosAlmacenConsignacionDao getMovimientosAlmacenConsignacionDao()
	{
		return new OracleMovimientosAlmacenConsignacionDao();
	}


	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>PlanosFURIPSDao</code> 
	 */
	public PlanosFURIPSDao getPlanosFURIPSDao()
	{
		return new OraclePlanosFURIPSDao();
	}
	

	/**
	 *Retorna el DAO con el cual el objeto <code>ParametrosFirmasImpresionRespDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ParametrosFirmasImpresionRespDao</code>
	 */
	public ParametrosFirmasImpresionRespDao getParametrosFirmasImpresionRespDao()
	{
		return new OracleParametrosFirmasImpresionRespDao();
	}


	/**
	 *Retorna el DAO con el cual el objeto <code>ConceptosRespuestasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConceptosRespuestasDao</code>
	 */
	public ConceptosRespuestasDao getConceptosRespuestasDao()
	{
		return new OracleConceptosRespuestasDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ConsultasBirtDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultasBirtDao</code>
	 */
	public ConsultasBirtDao getConsultasBirtDao()
	{
		return new OracleConsultasBirtDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>GlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>GlosasDao</code>
	 */
	public GlosasDao getGlosasDao()
	{
		return new OracleGlosasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>GlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>GlosasDao</code>
	 */
	public ProgramacionAreaPacienteDao getProgramacionAreaPacienteDao()
	{
		return new OracleProgramacionAreaPacienteDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>OracleProgramacionCuidadoEnferDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>OracleProgramacionCuidadoEnferDao</code>
	 */
	public ProgramacionCuidadoEnferDao getProgramacionCuidadoEnferDao()
	{
		return new OracleProgramacionCuidadoEnferDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>OracleConfirmarAnularGlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>OracleConfirmarAnularGlosasDao</code>
	 */
	public ConfirmarAnularGlosasDao getConfirmarAnularGlosasDao()
	{
		return new OracleConfirmarAnularGlosasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>OracleBusquedaGlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>OracleBusquedaGlosasDao</code>
	 */
	public BusquedaGlosasDao getBusquedaGlosasDao()
	{
		return new OracleBusquedaGlosasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultarImpFacAudiDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultarImpFacAudiDao</code>
	 */
	public ConsultarImpFacAudiDao getConsultarImpFacAudiDao()
	{
		return new OracleConsultarImpFacAudiDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultarImpFacAudiDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultarImpFacAudiDao</code>
	 */
	public SoportesFacturasDao getSoportesFacturasDao()
	{
		return new OracleSoportesFacturasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>AprobarGlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>AprobarGlosasDao</code>
	 */
	public AprobarGlosasDao getAprobarGlosasDao()
	{
		return new OracleAprobarGlosasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>OracleRegistrarModificarGlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>OracleRegistrarModificarGlosasDao</code>
	 */
	public RegistrarModificarGlosasDao getRegistrarModificarGlosasDao()
	{
		return new OracleRegistrarModificarGlosasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ImpresionSoportesFacturasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ImpresionSoportesFacturasDao</code>
	 */
	public ImpresionSoportesFacturasDao getImpresionSoportesFacturasDao()
	{
		return new OracleImpresionSoportesFacturasDao();
	}
	
	/**
	 * Retonrna el DAO con el cual el objeto <code>ConsultaProgramacionCuidadosAreaDao</code>
	 * interactua cona la fuente de datos.
	 * @return el DAO usado por <code>ConsultaProgramacionCuidadosAreaDao</code>
	 */
	public ConsultaProgramacionCuidadosAreaDao getConsultaProgramacionCuidadosAreaDao(){
		return new OracleConsultaProgramacionCuidadosAreaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaProgramacionCuidadosPacienteDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaProgramacionCuidadosPacienteDao</code>
	 */
	public ConsultaProgramacionCuidadosPacienteDao getConsultaProgramacionCuidadosPacienteDao(){
		return new OracleConsultaProgramacionCuidadosPacienteDao();
	} 
	 
	
	/**
	 * Retorna el DAO con el cual el objeto <code>RegistrarRespuestaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>RegistrarRespuestaDao</code>
	 */
	public RegistrarRespuestaDao getRegistrarRespuestaDao()
	{
		return new OracleRegistrarRespuestaDao();
	}
	

	/**
	 * Retorna el DAO con el cual el objeto <code>RegistrarRespuestaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>RegistrarRespuestaDao</code>
	 */
	public ConsultarImprimirGlosasDao getConsultarImprimirGlosasDao()
	{
		return new OracleConsultarImprimirGlosasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>OracleBusquedaRespuestaGlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>OracleBusquedaGlosasDao</code>
	 */
	public BusquedaRespuestaGlosasDao getBusquedaRespuestaGlosasDao()
	{
		return new OracleBusquedaRespuestaGlosasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>OracleAprobarAnularRespuestasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>OracleAprobarAnularRespuestasDao</code>
	 */
	public AprobarAnularRespuestasDao getAprobarAnularRespuestasDao()
	{
		return new OracleAprobarAnularRespuestasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>OracleRadicarRespuestasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>OracleRadicarRespuestasDao</code>
	 */
	public RadicarRespuestasDao getRadicarRespuestasDao()
	{
		return new OracleRadicarRespuestasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>OracleDetalleGlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>OracleDetalleGlosasDao</code>
	 */
	public DetalleGlosasDao getDetalleGlosasDao()
	{
		return new OracleDetalleGlosasDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>OracleConsultarImprimirRespuestasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>OracleConsultarImprimirRespuestasDao</code>
	 */
	public ConsultarImprimirRespuestasDao getConsultarImprimirRespuestasDao() 
	{
		return new OracleConsultarImprimirRespuestasDao();
	}
	
	/**
	 * 
	 */
	public ConsultarImprimirGlosasSinRespuestaDao getConsultarImprimirGlosasSinRespuestaDao() {
		return new OracleConsultarImprimirGlosasSinRespuestaDao();
	}
	
	public ExcepcionesCCInterconsultasDao getExcepcionesCCInterconsultasDao() {
		return new OracleExcepcionesCCInterconsultasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>OracleRegistroEnvioInfAtencionIniUrgDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>OracleRegistroEnvioInfAtencionIniUrgDao</code>
	 */
	public RegistroEnvioInfAtencionIniUrgDao  getRegistroEnvioInfAtencionIniUrgDao()
	{
		return new OracleRegistroEnvioInfAtencionIniUrgDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadesJustificacionNoPosDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>UtilidadesJustificacionNoPosDao</code>
	 */
	public UtilidadesJustificacionNoPosDao  getUtilidadesJustificacionNoPosDao()
	{
		return new OracleUtilidadesJustificacionNoPosDao();
	}
	
	/**
	 * Retorna el Dao con el cual el objeto <code>OracleRegistroEnvioInformInconsisenBDDao</code> interactua 
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>OracleRegistroEnvioInformInconsisenBDDao</code>
	 */
	public RegistroEnvioInformInconsisenBDDao  getRegistroEnvioInformInconsisenBDDao()
	{
		return new OracleRegistroEnvioInformInconsisenBDDao();
	}
	
	
	/**
	 * Retorna el Dao con el cual el objeto <code>OracleConsultaEnvioInconsistenciasenBDDao</code> interactua 
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>OracleConsultaEnvioInconsistenciasenBDDao</code>
	 */
	public ConsultaEnvioInconsistenciasenBDDao getConsultaEnvioInconsistenciasenBDDao()
	{
	    return new OracleConsultaEnvioInconsistenciasenBDDao();	
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadesJustificacionNoPosDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>UtilidadesJustificacionNoPosDao</code>
	 */
	public AutorizacionesDao  getAutorizacionesDao()
	{
		return new OracleAutorizacionesDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>MotivosAnulacionCondonacionDao</code>, usada por <code>MotivosAnulacionCondonacionDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>MotivosAnulacionCondonacionDao</code>
	 */
	public MotivosAnulacionCondonacionDao getMotivosAnulacionCondonacionDao()
	{
		return new OracleMotivosAnulacionCondonacionDao();
	}
	/**
	 * Retorna el Dao con el cual el objeto <code>OracleAnulacionCondonacionMultasDao</code> interactua 
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>OracleAnulacionCondonacionMultasDao</code> 
	 */
	public AnulacionCondonacionMultasDao getAnulacionCondonacionMultasDao()
	{
		return new OracleAnulacionCondonacionMultasDao();
	}
	
	/**
	 * Retorna el Dao con el cual el objeto <code>OracleMultasDao</code> interactua 
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>OracleMultasDao</code> 
	 */
	public MultasDao getMultasDao()
	{
		return new OracleMultasDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaMultasAgendaCitasDao</code>, usada por <code>ConsultaMultasAgendaCitasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ConsultaMultasAgendaCitasDao</code>
	 */
	public ConsultaMultasAgendaCitasDao getConsultaMultasAgendaCitasDao()
	{
		return new OracleConsultaMultasAgendaCitasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CentrosCostoEntidadesSubcontratadasDao</code>, usada por <code>CentrosCostoEntidadesSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>CentrosCostoEntidadesSubcontratadasDao</code>
	 */
	public CentrosCostoEntidadesSubcontratadasDao getCentrosCostoEntidadesSubcontratadasDao()
	{
		return new OracleCentrosCostoEntidadesSubcontratadasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CentrosCostoEntidadesSubcontratadasDao</code>, usada por <code>CentrosCostoEntidadesSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>CentrosCostoEntidadesSubcontratadasDao</code>
	 */
	public IngresarModificarContratosEntidadesSubcontratadasDao getIngresarModificarContratosEntidadesSubcontratadasDao()
	{
		return new OracleIngresarModificarContratosEntidadesSubcontratadasDao();
	}
	
	

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CargosEntidadesSubcontratadasDao</code>, usada por <code>CargosEntidadesSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>CargosEntidadesSubcontratadasDao</code>
	 */
	public CargosEntidadesSubcontratadasDao getCargosEntidadesSubcontratadasDao()
	{
		return new OracleCargosEntidadesSubcontratadasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AutorizacionesEntidadesSubcontratadasDao</code>, usada por <code>AutorizacionesEntidadesSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>AutorizacionesEntidadesSubcontratadasDao</code>
	 */
	public AutorizacionesEntidadesSubcontratadasDao getAutorizacionesEntidadesSubcontratadasDao() {
		return new OracleAutorizacionesEntidadesSubcontratadasDao();
	}
	
		/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultarImprimirAutorizacionesEntSubcontratadasDao</code>, usada por <code>ConsultarImprimirAutorizacionesEntSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ConsultarImprimirAutorizacionesEntSubcontratadasDao</code>
	 */
	public ConsultarImprimirAutorizacionesEntSubcontratadasDao getConsultarImprimirAutorizacionesEntSubcontratadasDao()
	{
		return new OracleConsultarImprimirAutorizacionesEntSubcontratadasDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ProrrogarAnularAutorizacionesEntSubcontratadasDao</code>, usada por <code>ProrrogarAnularAutorizacionesEntSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ProrrogarAnularAutorizacionesEntSubcontratadasDao</code>
	 */
	public ProrrogarAnularAutorizacionesEntSubcontratadasDao getProrrogarAnularAutorizacionesEntSubcontratadasDao() {
		return new OracleProrrogarAnularAutorizacionesEntSubcontratadasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CoberturasEntidadesSubcontratadasDao</code>, usada por <code>CoberturasEntidadesSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>CoberturasEntidadesSubcontratadasDao</code>
	 */
	public CoberturasEntidadesSubcontratadasDao getCoberturasEntidadesSubcontratadasDao()
	{
		return new OracleCoberturasEntidadesSubcontratadasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>BusquedaDeudoresGenericaDao</code>, usada por <code>BusquedaDeudoresGenericaDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>BusquedaDeudoresGenericaDao</code>
	 */
	public BusquedaDeudoresGenericaDao getBusquedaDeudoresGenericaDao()
	{
		return new OracleBusquedaDeudoresGenericaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>BusquedaDeudoresGenericaDao</code>, usada por <code>BusquedaDeudoresGenericaDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>BusquedaDeudoresGenericaDao</code>
	 */
	public ConsultarContratosEntidadesSubcontratadasDao getConsultarContratosEntidadesSubcontratadasDao()
	{
		return new OracleConsultarContratosEntidadesSubcontratadasDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>OracleGeneracionTarifasPendientesEntSubDao</code>, usada por <code>OracleGeneracionTarifasPendientesEntSubDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>OracleGeneracionTarifasPendientesEntSubDao</code>
	 */
	public GeneracionTarifasPendientesEntSubDao getGeneracionTarifasPendientesEntSubDao()
	{
		return new OracleGeneracionTarifasPendientesEntSubDao();
	}

	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>OracleResponderConsultasEntSubcontratadasDao</code>, usada por <code>OracleResponderConsultasEntSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>OracleResponderConsultasEntSubcontratadasDao</code>
	 */
	public ResponderConsultasEntSubcontratadasDao getResponderConsultasEntSubcontratadasDao() {
		return new OracleResponderConsultasEntSubcontratadasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ParamInterfazSistema1E</code>, usada por <code>ParamInterfazSistema1E</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ParamInterfazSistema1E</code>
	 */
	public ParamInterfazSistema1EDao getParamInterfazSistema1EDao()
	{
		return new OracleParamInterfazSistema1EDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>GeneracionInterfaz1EDao</code>, usada por <code>GeneracionInterfaz1EDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>GeneracionInterfaz1EDao</code>
	 */
	public GeneracionInterfaz1EDao getGeneracionInterfaz1EDao()
	{
		return new OracleGeneracionInterfaz1EDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>GeneracionInterfaz1EDao</code>, usada por <code>GeneracionInterfaz1EDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>GeneracionInterfaz1EDao</code>
	 */
	public UtilidadesInterfazDao getUtilidadesInterfazDao()
	{
		return new OracleUtilidadesInterfazDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>DesmarcarDocProcesadosDao</code>, usada por <code>DesmarcarDocProcesadosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>DesmarcarDocProcesadosDao</code>
	 */
	public DesmarcarDocProcesadosDao getDesmarcarDocProcesadosDao()
	{
		return new OracleDesmarcarDocProcesadosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EspecialidadesDao</code>, usada por <code>EspecialidadesDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>EspecialidadesDao</code>
	 */
	public EspecialidadesDao getEspecialidadesDao()
	{
		return new OracleEspecialidadesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ReporteMovTipoDocDao</code>
	 * @return el DAO usado por <code>ReporteMovTipoDocDao</code>
	 */
	public ReporteMovTipoDocDao getReporteMovTipoDocDao()
	{
		return new OracleReporteMovTipoDocDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaInterfazSistema1EDao</code>, usada por <code>ConsultaInterfazSistema1EDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ConsultaInterfazSistema1EDao</code>
	 */
	public ConsultaInterfazSistema1EDao getConsultaInterfazSistema1EDao()
	{
		return new OracleConsultaInterfazSistema1EDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConceptosRetencionDao</code>, usada por <code>ConceptosRetencionDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ConceptosRetencionDao</code>
	 */
	public ConceptosRetencionDao getConceptosRetencionDao()
	{
		return new OracleConceptosRetencionDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>TiposRetencionDao</code>, usada por <code>TiposRetencionDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>TiposRetencionDao</code>
	 */
	public TiposRetencionDao getTiposRetencionDao()
	{
		return new OracleTiposRetencionDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>UtilidadesFacturasVariasDao</code>, usada por <code>UtilidadesFacturasVariasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>UtilidadesFacturasVariasDao</code>
	 */
	public UtilidadesFacturasVariasDao getUtilidadesFacturasVariasDao()
	{
		return new OracleUtilidadesFacturasVariasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>DatosFinanciacionDao</code>, usada por <code>DatosFinanciacionDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>DatosFinanciacionDao</code>
	 */
	public DatosFinanciacionDao getDatosFinanciacionDao()
	{
		return new OracleDatosFinanciacionDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>PazYSalvoCarteraPacienteDao</code>, usada por <code>PazYSalvoCarteraPacienteDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>PazYSalvoCarteraPacienteDao</code>
	 */
	public PazYSalvoCarteraPacienteDao getPazYSalvoCarteraPacienteDao()
	{
		return new OraclePazYSalvoCarteraPacienteDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EdadCarteraPacienteDao</code>, usada por <code>Cartera Paciente Dao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>EdadCarteraPacienteDao</code>
	 */
	public EdadCarteraPacienteDao getEdadCarteraPacienteDao()
	{
		return new OracleEdadCarteraPacienteDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>BusquedaConvencionesOdontologicasDao</code>, usada por <code>Odontologia Dao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>BusquedaConvencionesOdontologicasDao</code>
	 */
	public BusquedaConvencionesOdontologicasDao getBusquedaConvencionesOdontologicasDao() {
		return new OracleBusquedaConvencionesOdontologicasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConvencionesOdontologicasDao</code>, usada por <code>Odontologia Dao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ConvencionesOdontologicasDao</code>
	 */
	public ConvencionesOdontologicasDao getConvencionesOdontologicasDao() {
		return new OracleConvencionesOdontologicasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>MotivosCitaDao</code>, usada por <code>MotivosCitaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>MotivosCitaDao</code>
	 */
	public MotivosCitaDao getMotivosCitaDao() {
		return new OracleMotivosCitaDao();
	}
	
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>HallazgosOdontologicosDao</code>, usada por <code>OdontologiaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>HallazgosOdontologicosDao</code>
	 */
	public HallazgosOdontologicosDao getHallazgosOdontologicosDao() {
		return new OracleHallazgosOdontologicosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>MotivosAtencionOdontologicaDao</code>, usada por <code>MotivosAtencionOdontologicaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>MotivosAtencionOdontologicaDao</code>
	 */
	public MotivosAtencionOdontologicaDao getMotivosAtencionOdontologicaDao() {
		return new OracleMotivosAtencionOdontologicaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ServiAdicionalesXProfAtenOdontoDao</code>, usada por <code>ServiAdicionalesXProfAtenOdontoDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ServiAdicionalesXProfAtenOdontoDao</code>
	 */
	public ServiAdicionalesXProfAtenOdontoDao getServiAdicionalesXProfAtenOdontoDao() {
		return new OracleServiAdicionalesXProfAtenOdontoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>GenerarAgendaOdontologicaDao</code>, usada por <code>GenerarAgendaOdontologicaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>GenerarAgendaOdontologicaDao</code>
	 */
	public GenerarAgendaOdontologicaDao getGenerarAgendaOdontologicaDao() {
		return new OracleGenerarAgendaOdontologicaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ReportePagosCarteraPacienteDao</code>, usada por <code>ReportePagosCarteraPacienteDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ReportePagosCarteraPacienteDao</code>
	 */
	public ReportePagosCarteraPacienteDao getReportePagosCarteraPacienteDao() {
		return new OracleReportePagosCarteraPacienteDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UtilidadOdontologiaDao</code>, usada por <code>UtilidadOdontologiaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>UtilidadOdontologiaDao</code>
	 */
	public UtilidadOdontologiaDao getUtilidadOdontologiaDao() {
		return new OracleUtilidadOdontologiaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AgendaOdontologicaDao</code>, usada por <code>AgendaOdontologicaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AgendaOdontologicaDao</code>
	 */
	public AgendaOdontologicaDao getAgendaOdontologicaDao() {
		return new OracleAgendaOdontologicaDao(); 
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>IngresoPacienteOdontologiaDao</code>, usada por <code>IngresoPacienteOdontologiaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>IngresoPacienteOdontologiaDao</code>
	 */
	public IngresoPacienteOdontologiaDao getIngresoPacienteOdontologiaDao() {
		return new OracleIngresoPacienteOdontologiaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RegistrarConciliacionDao</code>, usada por <code>RegistrarConciliacionDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>RegistrarConciliacionDao</code>
	 */
	public RegistrarConciliacionDao getRegistrarConciliacionDao() {
		return new OracleRegistrarConciliacionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RegistrarConciliacionDao</code>, usada por <code>RegistrarConciliacionDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>RegistrarConciliacionDao</code>
	 */
	public CitaOdontologicaDao getCitaOdontologicaDao() {
		return new OracleCitaOdontologicaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CitaOdontologicaDao</code>, usada por <code>CitaOdontologicaDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>CitaOdontologicaDao</code>
	 */
	public UtilidadesGlosasDao getUtilidadesGlosasDao() {
		return new OracleUtilidadesGlosasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>BusquedaConciliacionesDao</code>, usada por <code>BusquedaConciliacionesDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>BusquedaConciliacionesDao</code>
	 */
	public BusquedaConciliacionesDao getBusquedaConciliacionesDao() {
		return new OracleBusquedaConciliacionesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ReporteEstadoCarteraGlosasDao</code>, usada por <code>ReporteEstadoCarteraGlosasDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ReporteEstadoCarteraGlosasDao</code>
	 */
	public ReporteEstadoCarteraGlosasDao getReporteEstadoCarteraGlosasDao() {
		return new OracleReporteEstadoCarteraGlosasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ReporteFacturasReiteradasDao</code>, usada por <code>ReporteFacturasReiteradasDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ReporteFacturasReiteradasDao</code>
	 */
	public ReporteFacturasReiteradasDao getReporteFacturasReiteradasDao() {
		return new OracleReporteFacturasReiteradasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ReporteFacturacionEventoRadicarDao</code>, usada por <code>ReporteFacturacionEventoRadicarDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ReporteFacturasReiteradasDao</code>
	 */
	public ReporteFacturacionEventoRadicarDao getReporteFacturacionEventoRadicarDao() {
		return new OracleReporteFacturacionEventoRadicarDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ReporteFacturasVencidasNoObjetadasDao</code>, usada por <code>ReporteFacturasVencidasNoObjetadasDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ReporteFacturasVencidasNoObjetadasDao</code>
	 */
	public ReporteFacturasVencidasNoObjetadasDao getReporteFacturasVencidasNoObjetadasDao() {
		return new OracleReporteFacturasVencidasNoObjetadasDao();
	}
	
	/*************************************** MANIZALES ***********************************************/
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>SubirPacienteDao</code>
	 * @return
	 */
	public SubirPacienteDao getSubirPacienteDao()
	{
		return new OracleSubirPacienteDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ExcepcionNivelDao</code>
	 * @return
	 */
	public ExcepcionNivelDao getExcepcionNivelDao()
	{
		return new OracleExcepcionNivelDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AjusteCxCDao</code>
	 * @return
	 */
	public AjusteCxCDao getAjusteCxCDao()
	{
		return new OracleAjusteCxCDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OracleAprobacionAjustesDao</code>
	 * @return
	 */
	public AprobacionAjustesDao getAprobacionAjustesDao()
	{
		return new OracleAprobacionAjustesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>OraclePagosCapitacionDao</code>
	 * @return
	 */
	public PagosCapitacionDao getPagosCapitacionDao()
	{
		return new OraclePagosCapitacionDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>PacientesConEgresoPorFacturarDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>PacientesConEgresoPorFacturarDao</code>
	 */
	public PacientesConEgresoPorFacturarDao getPacientesConEgresoPorFacturarDao()
	{
		return new OraclePacientesConEgresoPorFacturarDao();
	}

	
	/**
	 * Retorna el DAO con el cual el objeto <code>ProgramasSaludPYPDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ProgramasSaludPYPDao</code>
	 */
	public ProgramasSaludPYPDao getProgramasSaludPYPDao()
	{
		return new OracleProgramasSaludPYPDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ContratoCargue</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>ContratoCargue</code>
	 */
	public ContratoCargueDao getContratoCargueDao()
	{
		return new OracleContratoCargueDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CalificacionesXCumpliMetasDao</code>
	 * @return
	 */
	public CalificacionesXCumpliMetasDao getCalificacionesXCumpliMetasDao()
	{
		return new OracleCalificacionesXCumpliMetasDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ProgramaArticulo</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>ProgramaArticulo</code>
	 */
	public ProgramaArticuloPypDao getProgramaArticuloPypDao()
	{
		return new OracleProgramaArticuloPypDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>TipoCalificacion</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>TipoCalificacion</code>
	 */
	public TipoCalificacionPypDao getTipoCalificacionPypDao()
	{
		return new OracleTipoCalificacionPypDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>IngresoPacienteEpiDao</code> 
	 */
	public AntecedentesVacunasDao getAntecedentesVacunasDao()
	{
		return new OracleAntecedentesVacunasDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>IngresoPacienteEpiDao</code> 
	 */
	public UnidadPagoDao getUnidadPagoDao()
	{
		return new OracleUnidadPagoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>CrecimientoDesarrolloDao</code> 
	 */
	public CrecimientoDesarrolloDao getCrecimientoDesarrolloDao()
	{
		return new OracleCrecimientoDesarrolloDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ComparativoUltimoConteoDao</code> 
	 */
	public ComparativoUltimoConteoDao getComparativoUltimoConteoDao()
	{
		return new OracleComparativoUltimoConteoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>TiemposHojaAnestesiaDao</code> 
	 */
	public TiemposHojaAnestesiaDao getTiemposHojaAnestesiaDao()
	{
		return new OracleTiemposHojaAnestesiaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>PosicionesAnestesiaDao</code> 
	 */
	public PosicionesAnestesiaDao getPosicionesAnestesiaDao()
	{
		return new OraclePosicionesAnestesiaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>MotivosDescuentosDao</code> 
	 */
	public MotivosDescuentosDao getMotivosDescuentosDao()
	{
		return new OracleMotivoDescuentoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>DetConceptosRetencionDao</code> 
	 */
	public DetConceptosRetencionDao getDetConceptosRetencion()
	{
		return new OracleDetConceptosRetencionDao();
	}
	
	
	/*************************************** FIN MANIZALES ******************************************/

	/***************************************  INICIO MERCURY  **************************************************/
	
	public ValoracionOdontologiaDao getValoracionOdontologiaDao()
	{
	    return new OracleValoracionOdontologiaDao();
	}
    
    public OdontogramaDao getOdontogramaDao()
    {
        return new OracleOdontogramaDao();
    }
    
    public AntecedentesOdontologiaDao getAntecedentesOdontologiaDao()
    {
        return new OracleAntecedentesOdontologiaDao();
    }
    
    public TratamientoOdontologiaDao getTratamientoOdontologiaDao()
    {
        return new OracleTratamientoOdontologiaDao();
    }
    public IndicePlacaDao getIndicePlacaDao()
    {
        return new OracleIndicePlacaDao();
    }
    public CartaDentalDao getCartaDentalDao()
    {
    	return new OracleCartaDentalDao();
    }

    /***************************************   FIN MERCURY    **************************************************/


	/********************************* INICIO SYSMEDICA *********************************************/
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaRabiaDao</code>
	 */
	public FichaRabiaDao getFichaRabiaDao()
	{
	    return new OracleFichaRabiaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaSarampionDao</code>
	 */
	public FichaSarampionDao getFichaSarampionDao()
	{
	    return new OracleFichaSarampionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaVIHDao</code>
	 */
	public FichaVIHDao getFichaVIHDao()
	{
	    return new OracleFichaVIHDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaDengueDao</code>
	 */
	public FichaDengueDao getFichaDengueDao()
	{
		return new OracleFichaDengueDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaDengueDao</code>
	 */
	public FichaParalisisDao getFichaParalisisDao()
	{
		return new OracleFichaParalisisDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaSifilisDao</code>
	 */
	public FichaSifilisDao getFichaSifilisDao()
	{
		return new OracleFichaSifilisDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaTetanosDao</code>
	 */
	public FichaTetanosDao getFichaTetanosDao()
	{
		return new OracleFichaTetanosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaGenericaDao</code>
	 */
	public FichaGenericaDao getFichaGenericaDao()
	{
		return new OracleFichaGenericaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaTuberculosisDao</code>
	 */
	public FichaTuberculosisDao getFichaTuberculosisDao()
	{
		return new OracleFichaTuberculosisDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaMortalidadDao</code>
	 */
	public FichaMortalidadDao getFichaMortalidadDao()
	{
		return new OracleFichaMortalidadDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaSivimDao</code>
	 */
	public FichaSivimDao getFichaSivimDao()
	{
		return new OracleFichaSivimDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaBrotesDao</code>
	 */
	public FichaBrotesDao getFichaBrotesDao()
	{
		return new OracleFichaBrotesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaInfeccionesDao</code>
	 */
	public FichaInfeccionesDao getFichaInfeccionesDao()
	{
		return new OracleFichaInfeccionesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaInfeccionesDao</code>
	 */
	public FichaLesionesDao getFichaLesionesDao()
	{
		return new OracleFichaLesionesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaLaboratoriosDao</code>
	 */
	public FichaSolicitudLaboratoriosDao getFichaSolicitudLaboratoriosDao()
	{
		return new OracleFichaSolicitudLaboratoriosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>NotificacionDao</code>
	 */
	public NotificacionDao getNotificacionDao()
	{
	    return new OracleNotificacionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>BusquedaFichasDao</code>
	 * @return
	 */
	public BusquedaFichasDao getBusquedaFichasDao()
	{
	    return new OracleBusquedaFichasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>BusquedaNotificacionesDao</code>
	 * @return
	 */
	public BusquedaNotificacionesDao getBusquedaNotificacionesDao() 
	{
		return new OracleBusquedaNotificacionesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ReportesSecretariaDao</code>
	 */
	public ReportesSecretariaDao getReportesSecretariaDao()
	{
		return new OracleReportesSecretariaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>IngresoPacienteEpiDao</code> 
	 */
	public IngresoPacienteEpiDao getIngresoPacienteEpiDao()
	{
		return new OracleIngresoPacienteEpiDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ParamLaboratoriosDao</code> 
	 */
	public ParamLaboratoriosDao getParamLaboratoriosDao()
	{
		return new OracleParamLaboratoriosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>FichasAnterioresDao</code> 
	 */
	public FichasAnterioresDao getFichasAnterioresDao()
	{
		return new OracleFichasAnterioresDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>UtilidadFichasDao</code> 
	 */
	public UtilidadFichasDao getUtilidadFichasDao()
	{
		return new OracleUtilidadFichasDao();
	}
	

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaIntoxicacionesDao</code> 
	 */
	public FichaIntoxicacionesDao getFichaIntoxicacionesDao()
	{
		return new OracleFichaIntoxicacionesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaRubCongenitaDao</code> 
	 */
	public FichaRubCongenitaDao getFichaRubCongenitaDao()
	{
		return new OracleFichaRubCongenitaDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaAcciOfidicoDao</code> 
	 */
	public FichaAcciOfidicoDao getFichaAcciOfidicoDao()
	{
		return new OracleFichaAcciOfidicoDao();
	}
	

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaLepraDao</code> 
	 */
	public FichaLepraDao getFichaLepraDao()
	{
		return new OracleFichaLepraDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaDifteriaDao</code> 
	 */
	public FichaDifteriaDao getFichaDifteriaDao()
	{
		return new OracleFichaDifteriaDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaEasvDao</code> 
	 */
	public FichaEasvDao getFichaEasvDao()
	{
		return new OracleFichaEasvDao();
	}
	
	

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaEsiDao</code> 
	 */
	public FichaEsiDao getFichaEsiDao()
	{
		return new OracleFichaEsiDao();
	}
	

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaEtasDao</code> 
	 */
	public FichaEtasDao getFichaEtasDao()
	{
		return new OracleFichaEtasDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaHepatitisDao</code> 
	 */
	public FichaHepatitisDao getFichaHepatitisDao()
	{
		return new OracleFichaHepatitisDao();
	}
	

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaLeishmaniasisDao</code> 
	 */
	public FichaLeishmaniasisDao getFichaLeishmaniasisDao()
	{
		return new OracleFichaLeishmaniasisDao();
	}
	

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaMalariaDao</code> 
	 */
	public FichaMalariaDao getFichaMalariaDao()
	{
		return new OracleFichaMalariaDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaMeningitisDao</code> 
	 */
	public FichaMeningitisDao getFichaMeningitisDao()
	{
		return new OracleFichaMeningitisDao();
	}
	

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>FichaTosferinaDao</code> 
	 */
	public FichaTosferinaDao getFichaTosferinaDao()
	{
		return new OracleFichaTosferinaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ArchivosPlanosDao</code>
	 */
	public ArchivosPlanosDao getArchivosPlanosDao()
	{
		return new OracleArchivosPlanosDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RegistroAccidentesTransitoDao</code> 
	 */
	public RegistroAccidentesTransitoDao getRegistroAccidentesTransitoDao()
	{
		return new OracleRegistroAccidentesTransitoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaTerapiasGrupalesDao</code> 
	 */
	public ConsultaTerapiasGrupalesDao getConsultaTerapiasGrupalesDao()
	{ 
		return new OracleConsultaTerapiasGrupalesDao();
	}

	
	
	/******************************** FIN SYSMEDICA ************************************************/
	
	/**
	 * Retorna un objeto <code>Connection</code>, que representa una conexion abierta con una base de datos Oracle,
	 * utilizando los par�metros USERNAME y PASSWORD definidos al momento de inicializar esta <i>Factory</i>.
	 * @return una conexion abierta a una base de datos Oracle. Puede o no ser una conexi�n proveniente de un
	 * pool de conexiones, dependiendo de c�mo se haya inicializado esta clase.
	 */
	public Connection getConnection() throws SQLException {
		try{
			Connection con = null;
			synchronized (fuenteDatos) {
				con = fuenteDatos.getConnection(); // cogemos la conexion
				if (con != null && !con.isClosed()) {
					con.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
					Date fecha = new Date();
					SimpleDateFormat dateFormatter = new SimpleDateFormat(
							"dd/MM/yyyy  HH:mm:ss");
					logger.info(" *** Numero Conexiones (getConnection) ["
							+ dateFormatter.format(fecha) + "] - Activas: "
							+ fuenteDatos.getNumActive() + " Inactivas: "
							+ fuenteDatos.getNumIdle() + " ConId " 
							+ con.hashCode());
					alterarSesionFechaOracle(con);
				} 
			}
			return con;
		}
		catch (Exception e) {
			logger.error("Error al obtener la conexi�n con la fuente de datos.", e);
			return null;
		}
	}

	@Override
	public boolean alterarSesionFechaOracle(Connection con) throws SQLException {
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("alter session set NLS_DATE_FORMAT='YYYY-MM-DD'",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		boolean resultado=ps.executeUpdate()>0;
		ps.close();
		return resultado;
	}
	
	/**
	 * Retorna un objeto <code>java.sql.Connection</code>, que representa una conexion abierta con una fuente de datos.
	 * Dependiendo de c�mo se inicializ� la factory, esta conexi�n puede o no venir de un pool de conexiones.
	 * @return una conexion abierta a una fuente de datos
	 */
	public Connection getConnectionNoPool () throws SQLException
	{
		Connection con=null;
		try
		{
			Class.forName(DRIVER);
		}
		catch(ClassNotFoundException e )
		{
			logger.error("No pudo cargar el driver getConnectionNoPool", e);
			return null;
		}
		try
		{
			con= DriverManager.getConnection(PROTOCOL,USERNAME,PASSWORD);
			con.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
		}
		catch(SQLException e)
		{
			logger.error("No se pudo conectar getConnectionNoPool", e);
			return null;
        }
		return con;
	}
	
	/**
	 * Metodo que retorna El numero de conexiones que ha abierto el pool
	 */
	public int getNumeroConeccionesActivas(){
		synchronized (fuenteDatos) {
			return fuenteDatos.getNumActive();
		}
	}
	
	/**
	 * Metodo que retorna El numero de conexiones que ha abierto el pool
	 */
	public int getNumeroConeccionesInactivas(){
		synchronized (fuenteDatos) {
			return fuenteDatos.getNumIdle();
		}
	}

	/* M�todos est�ndar de las DaoFactory para inicializar y obtener conexiones de una BD */

	/**
	 * Inicializa el acceso a una base de datos Oracle. Este m�todo no utiliza
	 * un pool de conexiones para inicializar la BD.
	 * @param driver nombre completo del driver jdbc para Oracle
	 * @param protocol URL usada para acceder a la BD
	 * @param username login del usuario de la BD
	 * @param password password del usuario
	 */
	public void init(String driver, String protocol, String username, String password) 
	{

		logger.info("driver -->"+driver);
		logger.info("protocol -->"+protocol);
		logger.info("username -->"+username);
		DRIVER   = driver;
		PROTOCOL = protocol;
		USERNAME = username;
		//PASSWORD = MD5Hash.hashPassword(password); // Los passwords se almacenan "hashed" en la BD
		
		PASSWORD =password; //Por si despu�s se c�difica el password
		try {
			Class.forName(DRIVER);
		}	catch (ClassNotFoundException cnfe) {
				logger.error("No encontr� la clase : " + DRIVER);
				cnfe.printStackTrace();
		}

	}

	/**
	 * Inicializa el acceso a una base de datos Oracle, usando un pool de
	 * conexiones.
	 * @param driver nombre completo del driver jdbc para Oracle
	 * @param protocol URL usada para acceder a la BD
	 * @param username login del usuario de la BD
	 * @param password password del usuario
	 * @param maxActive n�mero m�ximo de conexiones activas
	 * @param whenExhaustedAction c�digo indicando la accion a seguir cuando se acaben las conexiones activas
	 * @param removeAbandonedTimeout tiempo en milisegundos antes de remover una conexi�n abandonada
	 */
	public void init (String driver, String protocol, String username, String password, int maxActive,int maxIdle, int maxWait) {

		try{
			DRIVER   = driver;
			PROTOCOL = protocol;
			USERNAME = username;
			//PASSWORD = MD5Hash.hashPassword(password); // Los passwords se almacenan "hashed" en la BD
	
			
			PASSWORD =password; //Por si despu�s se c�difica el password
	
			fuenteDatos = new BasicDataSource();
			fuenteDatos.setDriverClassName(DRIVER);
			fuenteDatos.setUrl(PROTOCOL);
			fuenteDatos.setUsername(USERNAME);
			fuenteDatos.setPassword(PASSWORD);
			logger.info("driver -->"+driver);
			logger.info("protocol -->"+protocol);
			logger.info("username -->"+username);
	
			fuenteDatos.setRemoveAbandoned(true);
			fuenteDatos.setRemoveAbandonedTimeout(30);
			fuenteDatos.setMaxActive(maxActive);
			fuenteDatos.setMaxIdle(maxIdle);
			fuenteDatos.setMaxWait(5000);
		}
		catch (Exception e) {
			logger.error("Error Init OracleDAOFactory",e);
		}

	}

	@Override
	public TarjetaClienteDao getTarjetaClienteDao() {
		return new OracleTarjetaClienteDao();
	}
	
	
	@Override
	public EmisionBonosDescDao getEmisionBonosDescDao() {
		return new OracleEmisionBonoDescDao();
     }	
	
	@Override
	public ServicioHonorariosDao getServicioHonorariosDao() {
		return new OracleServicioHonorariosDao();
	}

	@Override
	public DetalleServicioHonorariosDao getDetalleServicioHonorariosDao() {
		return new OracleDetalleServicioHonorarioDao();
	}

	@Override
	public DetalleAgrupacionHonorarioDao getDetalleAgrupacionHonorarioDao() {
		return new OracleDetalleAgrupacionHonorarioDao();
	}

	@Override
	public ContactosEmpresaDao getContactosEmpresaDao() {
		return new OracleContactosEmpresaDao();
	}

	@Override
	public CategoriaAtencionDao getCategoriaAtencionDao() {
		return new OracleCategoriaAtencionDao();
	}

	@Override
	public RegionesCoberturaDao getRegionesCoberturaDao() {
		return new OracleRegionesCoberturaDao();
	}
	
	@Override
	public HistoricoAtencionesDao getHistoricoAtencionesDao() {
		return new OracleHistoricoAtencionesDao();
	}

	

	@Override
	public DescuentoOdontologicoDao getDescuentoOdontologicoDao() {
		return new OracleDescuentoOdontologicoDao();
	}

	@Override
	public DetalleDescuentoOdontologicoDao getDetalleDescuentoOdontologicoDao() {
		return new OracleDetalleDescuentoOdontologicoDao();
	}

	@Override
	public TiposDeUsuarioDao getTiposDeUsuarioDao() {
		return new OracleTiposUsuarioDao();
	}

	@Override
	public ProgramaDao getProgramaDao() {
			return new OracleProgramaDao();
	}

	@Override
	public ConsultarRefinanciarCuotaPacienteDao getConsultarRefinanciarCuotaPacienteDao() {
		return new OracleConsultarRefinanciarCuotaPacienteDao();
	}

	@Override
	public HistoricoDescuentoOdontologicoDao getHistoricoDescuentoOdontologicoDao() {
		return new OracleHistoricoDescuentoOdontologicoDao();
	}

	@Override
	public HistoricoDetalleDescuentoOdontologico getHistoricoDetalleDescuentoOdontologicoDao() {
		return new OracleHistoricoDetalleDescuentoOdontologicoDao();
	}

	@Override
	public DetalleEmisionTarjetaClienteDao getDetalleEmisionTarjetaClienteDao() {
		return new OracleDetalleEmisionTarjetaClienteDao();
	}

	

	@Override
	public HistoricoDetalleEmisionTarjetaClienteDao getHistoricoDetalleEmisionTarjetaClienteDao() {
		return new OracleHistoricoDetalleEmisionTarjetaClienteDao();
	}

	@Override
	public HistoricoEmisionTarjetaClienteDao getHistoricoEmisionTarjetaClienteDao() {
		return new OracleHistoricoEmisionTarjetaClienteDao();
	}

	@Override
	public EmisionTarjetaClienteDao getEmisionTarjetaClienteDao() {
		return new OracleEmisionTarjetaClienteDao();
	}
	
	/**
	 * 
	 */
	public PromocionesOdontologicasDao getPromocionesOdontologicasDao(){
		 return new OraclePromocionesOdontologicasDao();
	}
	
	/**
	 * 
	 */
	public DetPromocionesOdoDao getDetPromocionesOdoDao(){
		 return new OracleDetPromocionesDao();
	}
	
	@Override
	public AliadoOdontologicoDao getAliadoOdontologicoDao() {
		return new OracleAliadoOdontologicoDao();
	}

	@Override
	public ControlAnticipoContratoDao getControlAnticipoContratoDao() {
		return new OracleControlAnticipoContratoDao();
	}

	@Override
	public LogControlAnticipoContratoDao getLogControlAnticipoContratoDao() {
		return new OracleLogControlAnticipoContratoDao();
	}

	@Override
	public DetConvPromocionesOdoDao getDetConvPromocionesOdoDao() {
	
		return new OracleDetConvPromocionesOdoDao();
	}

	@Override
	public DetCaPromocionesOdoDao getDetCaPromocionesOdoDao() {

		return new OracleDetCaPromocionesOdoDao();
	}

	@Override
	public VentasTarjetasClienteDao getVentasTarjetasCliente() {
		return new OracleVentasTarjetaClienteDao();
	}

	@Override
	public BeneficiarioTarjetaClienteDao getBeneficiarioTarjetaClienteDao() {
		return new OracleBeneficiarioTarjetaCliente();
	}

	@Override
	public ParentezcoDao getParentezcoDao() {
		return new OracleParentezcoDao();
	}

	@Override
	public ImagenesBaseDao getImagenesBaseDao() {
		return new OracleImagenesBaseDao();
	}
	
	public ProgramasOdontologicosDao getProgramasOdontologicosDao()
	{
		return new OracleProgramasOdontologicosDao();
	}

	@Override
	public RegistroIncapacidadesDao getRegistroIncapacidadesDao() {
		return new OracleRegistroIncapacidadesDao();
	}

	@Override
	public PerfilNEDDao getPerfilNEDDao() {
		return new OraclePerfilNEDDao();
	}

	@Override
	public DetalleHallazgoProgramaServicioDao getDetalleHallazgoProgramaServicioDao() {
		return new OracleDetalleHallazgoProgramaServicioDao();
	}

	@Override
	public HallazgoVsProgramaServicioDao getHallazgoVsProgramaServicioDao() {
		return  new OracleHallazgoVsProgramaServicioDao();
	}
	
	public ExtractoDeudoresCPDao getExtractoDeudoresCPDao()
	{
		return new OracleExtractoDeudoresCPDao();
	}

	@Override
	public CargosOdonDao getCargosOdonDao() {
		return new OracleCargosOdonDao();
	}

	@Override
	public PresupuestoOdontologicoDao getPresupuestoOdontologicoDao() {
		return new OraclePresupuestoOdontologicoDao();
	}

	public AutorizacionIngresoPacienteSaldoMoraDao getAutorizacionIngresoPacienteSaldoMoraDao()
	{
		return new OracleAutorizacionIngresoPacienteSaldoMoraDao();
	}

	@Override
	public PlanTratamientoDao getPlanTratamientoDao() {
		return new OraclePlantratamientoDao();
	}
	
	public ReporteDocumentosCarteraPacienteDao getReporteDocumentosCarteraPacienteDao(){
		return new OracleReporteDocumentosCarteraPacienteDao();
	}

	@Override
	public ValidacionesPresupuestoDao getValidacionesPresupuestoDao()
	{
		return new OracleValidacionesPresupuestoDao();
	}
	
	public EdadGlosaXFechaRadicacionDao getEdadGlosaXFechaRadicacionDao()
	{
		return new OracleEdadGlosaXFechaRadicacionDao();
	}
	
	public AperturaCuentaPacienteOdontologicoDao getAperturaCuentaPacienteOdontologicoDao()
	{
		return new OracleAperturaCuentaPacienteOdontologicoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>AtencionCitasOdontologiaDao</code>, usada por <code>AtencionCitasOdontologia</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>AtencionCitasOdontologiaDao</code>
	 */
	public AtencionCitasOdontologiaDao getAtencionCitasOdontologiaDao()
	{
		return new OracleAtencionCitasOdontologiaDao();
	}
	
	/**
	 * 
	 */
	public ReasignarProfesionalOdontoDao getReasignarProfesionalOdontoDao()
	{
		return new OracleReasignarProfesionalOdontoDao();
	}
	
	public ValoracionOdontologicaDao getValoracionOdontologicaDao()
	{
		return new OracleValoracionOdontologicaDao();
	}

	@Override
	public PacientesConvenioOdontologiaDao getPacientesConvenioOdontologiaDao() {
		return new OraclePacientesConvenioOdontologiaDao();
	}

	
	
	public CancelarAgendaOdontoDao getCancelarAgendaOdontoDao() {
		return new OracleCancelarAgendaOdontoDao();
	}

	@Override
	public PresupuestoExclusionesInclusionesDao getPresupuestoExclusionesInclusionesDao()
	{
		return new OraclePresupuestoExclusionesInclusionesDao();
	}

	@Override
	public VerResumenOdontologicoDao getVerResumenOdontologicoDao() {
		return new OracleVerResumenOdontologicoDao();
	}

	@Override
	public EvolucionOdontologicaDao getEvolucionOdontologicaDao() {
		return new OracleEvolucionOdontologicaDao();
	}

	@Override
	public ProcesosAutomaticosOdontologiaDao getProcesosAutomaticosOdontologiaDao() {
		return new OracleProcesosAutomaticosOdontologiaDao();
	}

	@Override
	public SaldosInicialesCarteraPacienteDao getSaldosInicialesCarteraPacienteDao() 
	{
		return new OracleSaldosInicialesCarteraPacienteDao();
	}

	@Override
	public CierreSaldoInicialCarteraPacienteDao getCierreSaldoInicialCarteraPacienteDao()
	{
		return new OracleCierreSaldoInicialCarteraPacienteDao();
	}
	
	public AutorizacionDescuentosOdonDao getAutorizacionDescuentosOdonDao()
	{
		return new OracleAutorizacionDescuentosOdonDao();
	}
	
	@Override
	public HonorariosPoolDao getHonorariosPoolDao() {
		
		return new OracleHonorariosPoolDao();
	}

	@Override
	public CalculoHonorariosPoolesDao getCalculoHonorariosPoolesDao()
	{
		return new OracleCalculoHonorariosPoolesDao();
	}
	
	@Override
	public EnvioEmailAutomaticoDao getEnvioEmailAutomaticoDao() {
		return new OracleEnvioEmailAutomaticoDao();
	}

	@Override
	public ExcepcionesHorarioAtencionDao getExcepcionesHorarioAtencionDao() {
		
		//return  new PostgresqlExcepcionesHorarioAtencionDao();
		return new OracleExcepcionesHorarioAtencionDao();
	}
	
	@Override
	public NewMenuDao getNewMenuDao() {
		return new OracleNewMenuDao(); 
	}

	@Override
	public ConsecutivosCentroAtencionDao getConsecutivosCentroAtencionDao() {
		
		return new OracleConsecutivosCentroAtencionDao();
	}

	@Override
	public FacturaOdontologicaDao getFacturaOdontologicaDao() {
		return new OracleFacturaOdontologicaDao();
	}

	@Override
	public NumeroSuperficiesPresupuestoDao getNumeroSuperficiesPresupuestoDao() {
		return new OracleNumeroSuperficiesPresupuestoDao();
	}

	@Override
	public PresupuestoContratadoDao getPresupuestoContratadoDao() {
		return new OraclePresupuestoContratadoDao();
	}

	@Override
	public PresupuestoCuotasEspecialidadDao getPresupuestoCuotasEspecialidadDao() {
		return new OraclePresupuestoCuotasEspecialidadDao();
	}

	@Override
	public PresupuestoLogImpresionDao getPresupuestoLogImpresionDao() {
		return new OraclePresupuestoLogImpresionDao();
	}

	@Override
	public PresupuestoPaquetesDao getPresupuestoPaquetesDao() {
		return new OraclePresupuestoPaquetesDao();
	}

	@Override
	public MontoAgrupacionArticuloDAO getMontoAgrupacionArticuloDAO() {
		return new OracleMontoAgrupacionArticuloDAO();
	}
	
	/**
	 * 
	 * Este m�todo se encarga de devolver una instancia de 
	 * MontoArticuloEspecificoDAO
	 * @return MontoArticuloEspecificoDAO
	 * @author Angela Maria Aguirre
	 */
	@Override
	public MontoArticuloEspecificoDAO getMontoArticuloEspecificoDAO(){
		return new OracleMontoArticuloEspecificoDAO();
	}

	/**
	 * 
	 * Este m�todo se encarga de devolver una instancia de 
	 * MontoArticuloEspecificoDAO
	 * @return HistoMontoAgrupacionArticuloDAO
	 * @author Angela Maria Aguirre
	 */
	@Override
	public HistoMontoAgrupacionArticuloDAO getHistoMontoAgrupacionArticuloDAO() {
		return new OracleHistoMontoAgrupacionArticulo();
	}
	
	/**
	 * 
	 */
	@Override
	public ReporteCitasOdontologicasDao getReporteCitasOdontologicasDao()
	{
		return new OracleReporteCitasOdontologicasDao();
	}

	/**
	 * 
	 * Este m�todo se encarga de devolver una instancia de 
	 * MontoArticuloEspecificoDAO
	 * @return HistoMontoAgrupacionArticuloDAO
	 * @author Angela Maria Aguirre
	 */
	@Override
	public NivelAutorizacionAgrupacionArticuloDAO getNivelAutorizacionAgrupacionArticuloDAO() {
		return new OracleNivelAutorizacionAgrupacionArticuloDAO();
		
	}

	/**
	 * 
	 * Este m�todo se encarga de devolver una instancia de 
	 * MontoArticuloEspecificoDAO
	 * @return HistoMontoAgrupacionArticuloDAO
	 * @author Angela Maria Aguirre
	 */
	@Override
	public NivelAutorizacionArticuloEspecificoDAO getNivelAutorizacionArticuloEspecificoDAO() {
		return new OracleNivelAutorizacionArticuloEspecificoDAO();
	}

	/**
	 * 
	 */
	@Override
	public CalculoValorCobrarPacienteDao getCalculoValorCobrarPacienteDao() {
		return new OracleCalculoValorCobrarPacienteDao();
	}
	
	
	/**
	 * 
	 * @return
	 */
	@Override
	public ProcesoNivelAutorizacionDao getProcesoNivelAutorizacionDao()
	{
		return new OracleProcesoNivelAutorizacionDao();
	}
}
