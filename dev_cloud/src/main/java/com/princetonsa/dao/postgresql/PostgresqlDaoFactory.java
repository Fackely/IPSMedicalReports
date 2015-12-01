/*
 * @(#)PostgresqlDaoFactory.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.MD5Hash;

import com.mercury.dao.odontologia.AntecedentesOdontologiaDao;
import com.mercury.dao.odontologia.CartaDentalDao;
import com.mercury.dao.odontologia.IndicePlacaDao;
import com.mercury.dao.odontologia.OdontogramaDao;
import com.mercury.dao.odontologia.TratamientoOdontologiaDao;
import com.mercury.dao.odontologia.ValoracionOdontologiaDao;
import com.mercury.dao.postgresql.odontologia.PostgresqlAntecedentesOdontologiaDao;
import com.mercury.dao.postgresql.odontologia.PostgresqlCartaDentalDao;
import com.mercury.dao.postgresql.odontologia.PostgresqlIndicePlacaDao;
import com.mercury.dao.postgresql.odontologia.PostgresqlOdontogramaDao;
import com.mercury.dao.postgresql.odontologia.PostgresqlTratamientoOdontologiaDao;
import com.mercury.dao.postgresql.odontologia.PostgresqlValoracionOdontologiaDao;
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
import com.princetonsa.dao.postgresql.administracion.PostgresqlConceptosRetencionDao;
import com.princetonsa.dao.postgresql.administracion.PostgresqlDetConceptosRetencionDao;
import com.princetonsa.dao.postgresql.administracion.PostgresqlEnvioEmailAutomaticoDao;
import com.princetonsa.dao.postgresql.administracion.PostgresqlEspecialidadesDao;
import com.princetonsa.dao.postgresql.administracion.PostgresqlFactorConversionMonedasDao;
import com.princetonsa.dao.postgresql.administracion.PostgresqlTiposMonedaDao;
import com.princetonsa.dao.postgresql.administracion.PostgresqlTiposRetencionDao;
import com.princetonsa.dao.postgresql.administracion.PostgresqlUtilidadesAdministracionDao;
import com.princetonsa.dao.postgresql.agendaProcedimiento.PostgresqlExamenCondiTomaDao;
import com.princetonsa.dao.postgresql.agendaProcedimiento.PostgresqlUnidadProcedimientoDao;
import com.princetonsa.dao.postgresql.capitacion.PostgresSQLNivelAutorizacionAgrupacionArticuloDAO;
import com.princetonsa.dao.postgresql.capitacion.PostgresSQLNivelAutorizacionArticuloEspecificoDAO;
import com.princetonsa.dao.postgresql.capitacion.PostgresqlAjusteCxCDao;
import com.princetonsa.dao.postgresql.capitacion.PostgresqlAprobacionAjustesDao;
import com.princetonsa.dao.postgresql.capitacion.PostgresqlAsociarCxCAFacturasDao;
import com.princetonsa.dao.postgresql.capitacion.PostgresqlCierresCarteraCapitacionDao;
import com.princetonsa.dao.postgresql.capitacion.PostgresqlContratoCargueDao;
import com.princetonsa.dao.postgresql.capitacion.PostgresqlCuentaCobroCapitacionDao;
import com.princetonsa.dao.postgresql.capitacion.PostgresqlEdadCarteraCapitacionDao;
import com.princetonsa.dao.postgresql.capitacion.PostgresqlExcepcionNivelDao;
import com.princetonsa.dao.postgresql.capitacion.PostgresqlNivelAtencionDao;
import com.princetonsa.dao.postgresql.capitacion.PostgresqlPagosCapitacionDao;
import com.princetonsa.dao.postgresql.capitacion.PostgresqlProcesoNivelAutorizacionDao;
import com.princetonsa.dao.postgresql.capitacion.PostgresqlRadicacionCuentasCobroCapitacionDao;
import com.princetonsa.dao.postgresql.capitacion.PostgresqlRegistroSaldosInicialesCapitacionDao;
import com.princetonsa.dao.postgresql.capitacion.PostgresqlSubirPacienteDao;
import com.princetonsa.dao.postgresql.capitacion.PostgresqlUnidadPagoDao;
import com.princetonsa.dao.postgresql.cargos.PostgresqlCargosAutomaticosPresupuestoDao;
import com.princetonsa.dao.postgresql.cargos.PostgresqlCargosDao;
import com.princetonsa.dao.postgresql.cargos.PostgresqlCargosDirectosDao;
import com.princetonsa.dao.postgresql.cargos.PostgresqlCargosEntidadesSubcontratadasDao;
import com.princetonsa.dao.postgresql.cargos.PostgresqlCargosOdonDao;
import com.princetonsa.dao.postgresql.cargos.PostgresqlGeneracionCargosPendientesDao;
import com.princetonsa.dao.postgresql.cargos.PostgresqlUtilidadJustificacionPendienteArtServDao;
import com.princetonsa.dao.postgresql.cartera.PostgresqlEdadCarteraDao;
import com.princetonsa.dao.postgresql.cartera.PostgresqlRecaudoCarteraEventoDao;
import com.princetonsa.dao.postgresql.cartera.PostgresqlReporteFacturacionEventoRadicarDao;
import com.princetonsa.dao.postgresql.cartera.PostgresqlReportesEstadosCarteraDao;
import com.princetonsa.dao.postgresql.carteraPaciente.PostgresqlApliPagosCarteraPacienteDao;
import com.princetonsa.dao.postgresql.carteraPaciente.PostgresqlAutorizacionIngresoPacienteSaldoMoraDao;
import com.princetonsa.dao.postgresql.carteraPaciente.PostgresqlCierreSaldoInicialCarteraPacienteDao;
import com.princetonsa.dao.postgresql.carteraPaciente.PostgresqlConsultarRefinanciarCuotaPacienteDao;
import com.princetonsa.dao.postgresql.carteraPaciente.PostgresqlDatosFinanciacionDao;
import com.princetonsa.dao.postgresql.carteraPaciente.PostgresqlDocumentosGarantiaDao;
import com.princetonsa.dao.postgresql.carteraPaciente.PostgresqlEdadCarteraPacienteDao;
import com.princetonsa.dao.postgresql.carteraPaciente.PostgresqlExtractoDeudoresCPDao;
import com.princetonsa.dao.postgresql.carteraPaciente.PostgresqlPazYSalvoCarteraPacienteDao;
import com.princetonsa.dao.postgresql.carteraPaciente.PostgresqlReporteDocumentosCarteraPacienteDao;
import com.princetonsa.dao.postgresql.carteraPaciente.PostgresqlReportePagosCarteraPacienteDao;
import com.princetonsa.dao.postgresql.consultaExterna.PostgresqlAnulacionCondonacionMultasDao;
import com.princetonsa.dao.postgresql.consultaExterna.PostgresqlAsignarCitasControlPostOperatorioDao;
import com.princetonsa.dao.postgresql.consultaExterna.PostgresqlExcepcionesHorarioAtencionDao;
import com.princetonsa.dao.postgresql.consultaExterna.PostgresqlMotivosAnulacionCondonacionDao;
import com.princetonsa.dao.postgresql.consultaExterna.PostgresqlMotivosCancelacionCitaDao;
import com.princetonsa.dao.postgresql.consultaExterna.PostgresqlMotivosCitaDao;
import com.princetonsa.dao.postgresql.consultaExterna.PostgresqlMultasDao;
import com.princetonsa.dao.postgresql.consultaExterna.PostgresqlReporteEstadisticoConsultaExDao;
import com.princetonsa.dao.postgresql.consultaExterna.PostgresqlServiAdicionalesXProfAtenOdontoDao;
import com.princetonsa.dao.postgresql.consultaExterna.PostgresqlUnidadAgendaUsuarioCentroDao;
import com.princetonsa.dao.postgresql.consultaExterna.PostgresqlUtilidadesConsultaExternaDao;
import com.princetonsa.dao.postgresql.enfermeria.PostgresqlConsultaProgramacionCuidadosAreaDao;
import com.princetonsa.dao.postgresql.enfermeria.PostgresqlConsultaProgramacionCuidadosPacienteDao;
import com.princetonsa.dao.postgresql.enfermeria.PostgresqlProgramacionAreaPacienteDao;
import com.princetonsa.dao.postgresql.enfermeria.PostgresqlProgramacionCuidadoEnferDao;
import com.princetonsa.dao.postgresql.enfermeria.PostgresqlRegistroEnfermeriaDao;
import com.princetonsa.dao.postgresql.facturacion.PostgesqlControlAnticipoContratoDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresSQLHistoMontoAgrupacionArticulo;
import com.princetonsa.dao.postgresql.facturacion.PostgresSQLMontoAgrupacionArticulo;
import com.princetonsa.dao.postgresql.facturacion.PostgresSQLMontoArticuloEspecificoDAO;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlAbonosYDescuentosDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlActualizacionAutomaticaEsquemaTarifarioDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlAnulacionCargosFarmaciaDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlAnulacionFacturasDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlArchivoPlanoDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlAutorizarAnulacionFacturasDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlCalculoHonorariosPoolesDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlCalculoValorCobrarPacienteDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlCargosDirectosCxDytDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlCentrosCostoEntidadesSubcontratadasDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlCoberturaDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlCoberturasConvenioDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlCoberturasEntidadesSubcontratadasDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlComponentesPaquetesDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlConceptosPagoPoolesDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlConceptosPagoPoolesXConvenioDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlCondicionesXServiciosDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlConsecutivosCentroAtencionDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlConsolidadoFacturacionDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlConsultaPaquetesFacturadosDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlConsultaTarifasArticulosDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlConsultaTarifasDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlConsultarContratosEntidadesSubcontratadasDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlConsumosFacturadosDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlConsumosPorFacturarPacientesHospitalizadosDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlConsumosXFacturarDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlContactosEmpresaDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlCopiarTarifasEsquemaTarifarioDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlCostoInventarioPorFacturarDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlCostoVentasPorInventarioDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlDescuentosComercialesDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlDetalleCoberturaDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlDetalleInclusionesExclusionesDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlDistribucionCuentaDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlEntidadesSubContratadasDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlExcepcionesCCInterconsultasDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlExcepcionesTarifas1Dao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlFacturaDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlFacturaOdontologicaDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlFacturadosPorConvenioDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlGeneracionArchivoPlanoIndicadoresCalidadDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlHonorariosPoolDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlImpresionSoportesFacturasDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlInclusionExclusionConvenioDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlInclusionesExclusionesDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlIngresarModificarContratosEntidadesSubcontratadasDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlLogControlAnticipoContratoDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlPacientesPorFacturarDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlPaquetesConvenioDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlPaquetesDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlPaquetizacionDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlParamArchivoPlanoColsanitasDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlParamArchivoPlanoIndCalidadDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlPendienteFacturarDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlRegistroRipsCargosDirectosDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlReliquidacionTarifasDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlReporteProcedimientosEsteticosDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlRevisionCuentaDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlServiciosGruposEsteticosDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlServicios_ArticulosIncluidosEnOtrosProcedimientosDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlSolicitarAnulacionFacturasDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlSoportesFacturasDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlTiposConvenioDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlTotalFacturadoConvenioContratoDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlTrasladoSolicitudesPorTransplantesDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlUsuariosAutorizarAnulacionFacturasDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlUtilidadesFacturacionDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlValidacionesFacturaDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlVentasCentroCostoDao;
import com.princetonsa.dao.postgresql.facturacion.PostgresqlVentasEmpresaConvenioDao;
import com.princetonsa.dao.postgresql.facturasVarias.PostgresqlAprobacionAnulacionAjustesFacturasVariasDao;
import com.princetonsa.dao.postgresql.facturasVarias.PostgresqlAprobacionAnulacionFacturasVariasDao;
import com.princetonsa.dao.postgresql.facturasVarias.PostgresqlAprobacionAnulacionPagosFacturasVariasDao;
import com.princetonsa.dao.postgresql.facturasVarias.PostgresqlConceptosFacturasVariasDao;
import com.princetonsa.dao.postgresql.facturasVarias.PostgresqlConsultaFacturasVariasDao;
import com.princetonsa.dao.postgresql.facturasVarias.PostgresqlConsultaImpresionAjustesFacturasVariasDao;
import com.princetonsa.dao.postgresql.facturasVarias.PostgresqlConsultaImpresionPagosFacturasVariasDao;
import com.princetonsa.dao.postgresql.facturasVarias.PostgresqlConsultaMovimientoDeudorDao;
import com.princetonsa.dao.postgresql.facturasVarias.PostgresqlConsultaMovimientoFacturaDao;
import com.princetonsa.dao.postgresql.facturasVarias.PostgresqlDeudoresDao;
import com.princetonsa.dao.postgresql.facturasVarias.PostgresqlGenModFacturasVariasDao;
import com.princetonsa.dao.postgresql.facturasVarias.PostgresqlGeneracionModificacionAjustesFacturasVariasDao;
import com.princetonsa.dao.postgresql.facturasVarias.PostgresqlPagosFacturasVariasDao;
import com.princetonsa.dao.postgresql.facturasVarias.PostgresqlUtilidadesFacturasVariasDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlAprobarAnularRespuestasDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlAprobarGlosasDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlConceptosEspecificosDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlConceptosGenerealesDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlConceptosRespuestasDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlConfirmarAnularGlosasDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlConsultarImpFacAudiDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlConsultarImprimirGlosasDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlConsultarImprimirGlosasSinRespuestaDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlConsultarImprimirRespuestasDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlDetalleGlosasDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlEdadGlosaXFechaRadicacionDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlGlosasDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlParametrosFirmasImpresionRespDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlRadicarRespuestasDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlRegistrarConciliacionDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlRegistrarModificarGlosasDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlRegistrarRespuestaDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlReporteEstadoCarteraGlosasDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlReporteFacturasReiteradasDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlReporteFacturasVencidasNoObjetadasDao;
import com.princetonsa.dao.postgresql.glosas.PostgresqlUtilidadesGlosasDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresHistoricoAtencionesDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlCategoriasTriageDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlConsentimientoInformadoDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlConsultaIngresosHospitalDiaDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlConsultaReferenciaContrareferenciaDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlConsultaTerapiasGrupalesDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlContrarreferenciaDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlCrecimientoDesarrolloDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlDestinoTriageDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlDiagnosticosOdontologicosATratarDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlEpicrisis1Dao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlEscalasDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlEventosAdversosDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlEvolucionesDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlImagenesBaseDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlImpresionCLAPDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlImpresionResumenAtencionesDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlInformacionPartoDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlInformacionRecienNacidosDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlInstitucionesSircDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlJustificacionNoPosServDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlMotivosSircDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlParametrizacionComponentesDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlParametrizacionCurvaAlertaDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlParametrizacionPlantillasDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlPlantillasDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlReferenciaDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlRegistroEventosAdversosDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlRegistroResumenParcialHistoriaClinicaDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlRegistroTerapiasGrupalesDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlReporteReferenciaExternaDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlReporteReferenciaInternaDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlServiciosSircDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlServiciosXTipoTratamientoOdontologicoDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlSignosSintomasDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlSignosSintomasXSistemaDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlSistemasMotivoConsultaDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlTextosRespuestaProcedimientosDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlTiposTratamientosOdontologicosDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlUtilidadesHistoriaClinicaDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlUtilidadesJustificacionNoPosDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlValoracionPacientesCuidadosEspecialesDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlValoracionesDao;
import com.princetonsa.dao.postgresql.historiaClinica.PostgresqlVerResumenOdontologicoDao;
import com.princetonsa.dao.postgresql.interfaz.PostgresqlCampoInterfazDao;
import com.princetonsa.dao.postgresql.interfaz.PostgresqlConsultaInterfazSistema1EDao;
import com.princetonsa.dao.postgresql.interfaz.PostgresqlCuentaInvUnidadFunDao;
import com.princetonsa.dao.postgresql.interfaz.PostgresqlCuentaInventarioDao;
import com.princetonsa.dao.postgresql.interfaz.PostgresqlCuentaServicioDao;
import com.princetonsa.dao.postgresql.interfaz.PostgresqlCuentaUnidadFuncionalDao;
import com.princetonsa.dao.postgresql.interfaz.PostgresqlCuentasConveniosDao;
import com.princetonsa.dao.postgresql.interfaz.PostgresqlDesmarcarDocProcesadosDao;
import com.princetonsa.dao.postgresql.interfaz.PostgresqlGeneracionInterfaz1EDao;
import com.princetonsa.dao.postgresql.interfaz.PostgresqlGeneracionInterfazDao;
import com.princetonsa.dao.postgresql.interfaz.PostgresqlInterfazSistemaUnoDao;
import com.princetonsa.dao.postgresql.interfaz.PostgresqlParamInterfazDao;
import com.princetonsa.dao.postgresql.interfaz.PostgresqlParamInterfazSistema1EDao;
import com.princetonsa.dao.postgresql.interfaz.PostgresqlReporteMovTipoDocDao;
import com.princetonsa.dao.postgresql.interfaz.PostgresqlUtilidadesInterfazDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlAjustesXInventarioFisicoDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlAlmacenParametrosDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlArticuloCatalogoDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlArticulosConsumidosPacientesDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlArticulosFechaVencimientoDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlArticulosPorAlmacenDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlArticulosPuntoPedidoDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlArticulosXMezclaDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlCierresInventarioDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlComparativoUltimoConteoDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlConsultaAjustesEmpresaDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlConsultaCostoArticulosDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlConsultaDevolucionInventarioPacienteDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlConsultaDevolucionPedidosDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlConsultaImpresionTrasladosDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlConsultaInventarioFisicoArticulosDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlConsultaMovimientosConsignacionesDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlConsultaSaldosCierresInventariosDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlConsumosCentrosCostoDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlCostoInventariosPorAlmacenDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlDespachoPedidoQxDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlDespachoTrasladoAlmacenDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlDevolucionInventariosPacienteDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlEquivalentesDeInventarioDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlExistenciasInventariosDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlFarmaciaCentroCostoDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlFormaFarmaceuticaDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlFormatoJustArtNoposDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlFormatoJustServNoposDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlGrupoEspecialArticulosDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlImpListaConteoDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlKardexDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlMedicamentosControladosAdministradosDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlMezclasDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlMotDevolucionInventariosDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlMovimientosAlmacenConsignacionDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlMovimientosAlmacenesDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlNaturalezaArticulosDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlPreparacionTomaInventarioDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlRegistroConteoInventarioDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlRegistroTransaccionesDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlSeccionesDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlSolicitudMedicamentosXDespacharDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlSolicitudTrasladoAlmacenDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlSustitutosNoPosDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlTiposInventarioDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlTiposTransaccionesInvDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlTransaccionesValidasXCCDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlUnidadMedidaDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlUsuariosXAlmacenDao;
import com.princetonsa.dao.postgresql.inventarios.PostgresqlUtilidadInventariosDao;
import com.princetonsa.dao.postgresql.laboratorios.PostgresqlUtilidadLaboratoriosDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlAperturaIngresosDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlAsignacionCamaCuidadoEspecialAPisoDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlAsignacionCamaCuidadoEspecialDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlAutorizacionesDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlAutorizacionesEntidadesSubcontratadasDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlCalidadAtencionDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlCategoriaAtencionDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlCensoCamasDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlCierreIngresoDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlConsultaCierreAperturaIngresoDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlConsultaEnvioInconsistenciasenBDDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlConsultaPreingresosDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlConsultarActivarCamasReservadasDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlConsultarAdmisionDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlConsultarImprimirAutorizacionesEntSubcontratadasDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlConsultarIngresosPorTransplantesDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlConsultoriosDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlEncuestaCalidadAtencionDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlEstadisticasIngresosDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlEstadisticasServiciosDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlFosygaDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlGeneracionAnexosForecatDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlGeneracionTarifasPendientesEntSubDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlHabitacionesDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlLecturaPlanosEntidadesDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlLiberacionCamasIngresosCerradosDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlListadoCamasHospitalizacionDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlListadoIngresosDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlMotivoCierreAperturaIngresosDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlMotivosSatisfaccionInsatisfaccionDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlOcupacionDiariaCamasDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlPacientesConAtencionDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlPacientesConEgresoPorFacturarDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlPacientesEntidadesSubConDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlPacientesHospitalizadosDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlParametrosEntidadesSubContratadasDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlPerfilNEDDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlPisosDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlPlanosFURIPSDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlProrrogarAnularAutorizacionesEntSubcontratadasDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlRegionesCoberturaDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlRegistroAccidentesTransitoDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlRegistroEnvioInfAtencionIniUrgDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlRegistroEnvioInformInconsisenBDDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlRegistroEventosCatastroficosDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlReingresoSalidaHospiDiaDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlReporteEgresosEstanciasDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlReporteMortalidadDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlTipoHabitacionDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlTiposAmbulanciaDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlTiposUsuarioCamaDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlTotalOcupacionCamasDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlTrasladoCentroAtencionDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlUtilidadesManejoPacienteDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlValoresTipoReporteDao;
import com.princetonsa.dao.postgresql.manejoPaciente.PostgresqlViasIngresoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresEmisionBonosDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlAgendaOdontologicaDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlAliadoOdontologicoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlAperturaCuentaPacienteOdontologicoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlAtencionCitasOdontologiaDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlAutorizacionDescuentosOdonDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlBeneficiariosTarjetaCliente;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlCancelarAgendaOdontoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlCitaOdontologicaDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlConvencionesOdontologicasDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlDescuentoOdontologicoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlDetCaPromocionesOdoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlDetConvPromocionesOdoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlDetPromocionesOdo;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlDetalleAgrupacionHonorarioDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlDetalleDescuentoOdontologicoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlDetalleEmisionTarjetaClienteDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlDetalleHallazgoProgramaServicioDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlDetalleServicioHonorarioDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlEmisionTarjetaClienteDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlEvolucionOdontologicaDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlGenerarAgendaOdontologicaDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlHallazgoVsProgramaServicioDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlHallazgosOdontologicosDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlHistoricoDescuentoOdontologicoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlHistoricoDetalleDescuentoOdontologicoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlHistoricoDetalleEmisionTarjetaClienteDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlHistoricoEmisionTarjetaClienteDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlIngresoPacienteOdontologiaDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlMotivoDescuentoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlMotivosAtencionOdontologicaDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlNumeroSuperficiesPresupuestoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlPacientesConvenioOdontologiaDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlParentezcoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlPlanTratamientoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlPresupuestoContratadoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlPresupuestoCuotasEspecialidadDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlPresupuestoExclusionesInclusionesDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlPresupuestoLogImpresionDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlPresupuestoOdontologiaDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlPresupuestoPaquetesDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlProcesosAutomaticosOdontologiaDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlProgramasDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlProgramasOdontologicosDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlPromocionesOdontologicasDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlReasignarProfesionalOdontoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlReporteCitasOdontologicasDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlServicioHonorariosDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlTarjetaClienteDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlTiposUsuarioDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlValidacionesPresupuestoDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlValoracionOdontologicaDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlVentasTarjetaClienteDao;
import com.princetonsa.dao.postgresql.ordenesmedicas.PostgresqlOrdenesAmbulatoriasDao;
import com.princetonsa.dao.postgresql.ordenesmedicas.PostgresqlRegistroIncapacidadesDao;
import com.princetonsa.dao.postgresql.ordenesmedicas.PostgresqlResponderConsultasEntSubcontratadasDao;
import com.princetonsa.dao.postgresql.ordenesmedicas.PostgresqlUtilidadesOrdenesMedicasDao;
import com.princetonsa.dao.postgresql.ordenesmedicas.procedimientos.PostgresqlInterpretarProcedimientoDao;
import com.princetonsa.dao.postgresql.ordenesmedicas.procedimientos.PostgresqlRespuestaProcedimientosDao;
import com.princetonsa.dao.postgresql.parametrizacion.PostgresqlAccesosVascularesHADao;
import com.princetonsa.dao.postgresql.parametrizacion.PostgresqlCentrosAtencionDao;
import com.princetonsa.dao.postgresql.parametrizacion.PostgresqlEventosDao;
import com.princetonsa.dao.postgresql.parametrizacion.PostgresqlGasesHojaAnestesiaDao;
import com.princetonsa.dao.postgresql.parametrizacion.PostgresqlInfoGeneralHADao;
import com.princetonsa.dao.postgresql.parametrizacion.PostgresqlIntubacionesHADao;
import com.princetonsa.dao.postgresql.parametrizacion.PostgresqlMonitoreoHemodinamicaDao;
import com.princetonsa.dao.postgresql.parametrizacion.PostgresqlPosicionesAnestesiaDao;
import com.princetonsa.dao.postgresql.parametrizacion.PostgresqlSignosVitalesDao;
import com.princetonsa.dao.postgresql.parametrizacion.PostgresqlTecnicaAnestesiaDao;
import com.princetonsa.dao.postgresql.parametrizacion.PostgresqlTiemposHojaAnestesiaDao;
import com.princetonsa.dao.postgresql.parametrizacion.PostgresqlViasAereasDao;
import com.princetonsa.dao.postgresql.pyp.PostgresqlActEjecutadasXCargarDao;
import com.princetonsa.dao.postgresql.pyp.PostgresqlActivPyPXFacturarDao;
import com.princetonsa.dao.postgresql.pyp.PostgresqlActividadesProgramasPYPDao;
import com.princetonsa.dao.postgresql.pyp.PostgresqlActividadesPypDao;
import com.princetonsa.dao.postgresql.pyp.PostgresqlCalificacionesXCumpliMetasDao;
import com.princetonsa.dao.postgresql.pyp.PostgresqlGrupoEtareoCrecimientoDesarrolloDao;
import com.princetonsa.dao.postgresql.pyp.PostgresqlMetasPYPDao;
import com.princetonsa.dao.postgresql.pyp.PostgresqlProgramaArticuloPypDao;
import com.princetonsa.dao.postgresql.pyp.PostgresqlProgramasActividadesConvenioDao;
import com.princetonsa.dao.postgresql.pyp.PostgresqlProgramasPYPDao;
import com.princetonsa.dao.postgresql.pyp.PostgresqlProgramasSaludPYPDao;
import com.princetonsa.dao.postgresql.pyp.PostgresqlTipoCalificacionPypDao;
import com.princetonsa.dao.postgresql.pyp.PostgresqlTiposProgamaDao;
import com.princetonsa.dao.postgresql.salasCirugia.PostgresqlAsocioSalaCirugiaDao;
import com.princetonsa.dao.postgresql.salasCirugia.PostgresqlAsocioServicioTarifaDao;
import com.princetonsa.dao.postgresql.salasCirugia.PostgresqlAsociosXRangoTiempoDao;
import com.princetonsa.dao.postgresql.salasCirugia.PostgresqlAsociosXTipoServicioDao;
import com.princetonsa.dao.postgresql.salasCirugia.PostgresqlAsociosXUvrDao;
import com.princetonsa.dao.postgresql.salasCirugia.PostgresqlConsultaProgramacionCirugiasDao;
import com.princetonsa.dao.postgresql.salasCirugia.PostgresqlDevolucionPedidoQxDao;
import com.princetonsa.dao.postgresql.salasCirugia.PostgresqlExTarifasAsociosDao;
import com.princetonsa.dao.postgresql.salasCirugia.PostgresqlHojaAnestesiaDao;
import com.princetonsa.dao.postgresql.salasCirugia.PostgresqlHojaGastosDao;
import com.princetonsa.dao.postgresql.salasCirugia.PostgresqlLiquidacionServiciosDao;
import com.princetonsa.dao.postgresql.salasCirugia.PostgresqlServiciosViaAccesoDao;
import com.princetonsa.dao.postgresql.salasCirugia.PostgresqlUtilidadesSalasDao;
import com.princetonsa.dao.postgresql.tesoreria.PostgresqlAprobacionAnulacionDevolucionesDao;
import com.princetonsa.dao.postgresql.tesoreria.PostgresqlArqueosDao;
import com.princetonsa.dao.postgresql.tesoreria.PostgresqlConsultaImpresionDevolucionDao;
import com.princetonsa.dao.postgresql.tesoreria.PostgresqlMotivosDevolucionRecibosCajaDao;
import com.princetonsa.dao.postgresql.tesoreria.PostgresqlRegistroDevolucionRecibosCajaDao;
import com.princetonsa.dao.postgresql.tesoreria.PostgresqlTrasladosCajaDao;
import com.princetonsa.dao.postgresql.tramiteReferencia.PostgresqlTramiteReferenciaDao;
import com.princetonsa.dao.postgresql.util.PostgresqlConsultasBirtDao;
import com.princetonsa.dao.postgresql.util.PostgresqlUtilConversionMonedasDao;
import com.princetonsa.dao.postgresql.util.PostgresqlUtilidadOdontologiaDao;
import com.princetonsa.dao.postgresql.webServiceCitasMedicas.PostgresqlConsultaWSDao;
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
import com.sysmedica.dao.postgresql.PostgresqlArchivosPlanosDao;
import com.sysmedica.dao.postgresql.PostgresqlBusquedaFichasDao;
import com.sysmedica.dao.postgresql.PostgresqlBusquedaNotificacionesDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaAcciOfidicoDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaBrotesDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaDengueDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaDifteriaDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaEasvDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaEsiDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaEtasDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaGenericaDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaHepatitisDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaInfeccionesDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaIntoxicacionesDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaLeishmaniasisDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaLepraDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaLesionesDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaMalariaDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaMeningitisDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaMortalidadDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaParalisisDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaRabiaDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaRubCongenitaDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaSarampionDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaSifilisDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaSivimDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaSolicitudLaboratoriosDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaTetanosDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaTosferinaDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaTuberculosisDao;
import com.sysmedica.dao.postgresql.PostgresqlFichaVIHDao;
import com.sysmedica.dao.postgresql.PostgresqlFichasAnterioresDao;
import com.sysmedica.dao.postgresql.PostgresqlIngresoPacienteEpiDao;
import com.sysmedica.dao.postgresql.PostgresqlNotificacionDao;
import com.sysmedica.dao.postgresql.PostgresqlParamLaboratoriosDao;
import com.sysmedica.dao.postgresql.PostgresqlReportesSecretariaDao;
import com.sysmedica.dao.postgresql.PostgresqlUtilidadFichasDao;


/**
 * Esta clase ofrece una implementacion particular de <code>DaoFactory</code>,
 * que utiliza PostgreSQL como fuente de datos. Adem�s de implementar los
 * metodos abstractos de <code>DaoFactory</code>, ofrece metodos
 * centralizados para inicializar el acceso a una base de datos PostgreSQL y
 * obtener conexiones de esta. Como una optimizacion adicional, esta clase es
 * una implementacion del patron <i>Singleton</i>. 
 * 
 * @version 1.0, Sep 20, 2002
 * @author <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;e
 *         L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo
 *         Andr&eacute;s Camacho P.</a>
 */

public class  PostgresqlDaoFactory extends DaoFactory{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(PostgresqlDaoFactory.class);

	/**
	 * Cadena constante con el <i>statement</i> necesario para hacer un
	 * <i>rollback</i> en PostgreSQL.
	 */
	public static final String abortarTransaccionStr = "ROLLBACK";

	/**
	 * Cadena constante con el <i>statement</i> necesario para terminar una
	 * transaccion en PostgreSQL.
	 */
	public static final String finTransaccionStr = "END TRANSACTION";

	/**
	 * Cadena constante con el <i>statement</i> necesario para iniciar una
	 * transaccion en PostgreSQL.
	 */
	public static final String inicioTransaccionStr = "BEGIN TRANSACTION";

	/**
	 * Cadena constante con el <i>statement</i> necesario para obtener el valor
	 * disponible actualmente de una secuencia en PostgreSQL.
	 */
	public static final String selectSequenceValStr = "SELECT last_value AS nextvalue FROM ";

	/**
	 * Una instancia, -la unica- de <code>PostgresqlDaoFactory</code>.
	 */
	private static PostgresqlDaoFactory instancia = null;

	/**
	 * Driver jdbc requerido para acceder una base de datos PostgreSQL.
	 */
	private static String DRIVER;

	/**
	 * Contrase�a del usuario definido en USERNAME.
	 */
	private static String PASSWORD;

	/**
	 * Factory del pool de conexiones, a usar en toda la aplicaci�n.
	 */
	@SuppressWarnings("unused")
	private static PoolableConnectionFactory poolableConnectionFactory;

	/**
	 * URL requerido para acceder una base de datos PostgreSQL.
	 */
	private static String PROTOCOL;

	/**
	 * Nombre del usuario de la aplicacion, es quien accede a la base de datos
	 * PostgreSQL.
	 */
	private static String USERNAME;

	/**
	 * Esta Factory produce objetos DAO de tipo PostgreSQL, uso este valor
	 * constante para obtener las conexiones adecuadas en todos los DAOs del
	 * paquete com.princetonsa.dao.postgresql
	 */
	public static final int tipoBD = DaoFactory.POSTGRESQL;

	/**
	 * 
	 */
	private BasicDataSource fuenteDatos = null;

	/**
	 * Retorna una sola instancia del objeto <code>PostgresqlDaoFactory</code>.
	 */
	public static PostgresqlDaoFactory getInstance() {

		if (instancia == null) {
			instancia = new PostgresqlDaoFactory();
		}
		return instancia;

	}

	/**
	 * Constructor de <code>PostgresqlDaoFactory</code>. Notese que es
	 * <i>privado</i>, este es un requerimiento para implementar el patron
	 * <i>singleton</i>.
	 */
	private PostgresqlDaoFactory() {
	}

	/**
	 * Hace rollback de una transaccin.
	 * 
	 * @param con
	 *            una conexin abierta con la fuente de datos
	 */
	public void abortTransaction(Connection con) throws SQLException {
		logger
				.info("**************************************************************");
		logger.info("            ABORTANDO TRANSACCION					  ");
		logger
				.info("**************************************************************");
		con.rollback();
		PreparedStatementDecorator abortarTransaccionStatement =  new PreparedStatementDecorator(con.prepareStatement(
				abortarTransaccionStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		abortarTransaccionStatement.executeUpdate();
		abortarTransaccionStatement.close();
		con.setAutoCommit(true);
	}

	/**
	 * Inicia una transacci�n.
	 * 
	 * @param con
	 *            una conexin abierta con la fuente de datos
	 * @return boolean <b>true</b> si se pudo iniciar la transaccin, <b>false</b>
	 *         si no
	 */
	public boolean beginTransaction(Connection con) throws SQLException {

		try {
			con.setAutoCommit(false);
			PreparedStatementDecorator iniciarTransaccionStatement = new PreparedStatementDecorator(con.prepareStatement(inicioTransaccionStr));
			@SuppressWarnings("unused")
			int resultado = iniciarTransaccionStatement.executeUpdate();
			iniciarTransaccionStatement.close();
			logger.info("**************************************************************");
			logger.info("      			  INICIANDO TRANSACCION 								  ");
			logger.info("**************************************************************");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Error al iniciar la transaccion");
		}
		return false;
	}

	/**
	 * Termina una transaccin con commit.
	 * 
	 * @param con
	 *            una conexin abierta con la fuente de datos
	 */
	public void endTransaction(Connection con) throws SQLException {
		logger.info(" ************************ ");
		logger.info(" FINALIZANDO TRANSACCION  ");
		logger.info(" ************************ ");
		PreparedStatementDecorator terminarTransaccionStatement =  new PreparedStatementDecorator(con.prepareStatement(
				finTransaccionStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		terminarTransaccionStatement.executeUpdate();
		terminarTransaccionStatement.close();
		con.commit();
		con.setAutoCommit(true);
	}

	/**
	 * Hace rollback de una transaccin.
	 * 
	 * @param con
	 *            una conexin abierta con la fuente de datos
	 */
	public void abortTransactionSinMensaje(Connection con) throws SQLException {
		con.rollback();
		PreparedStatementDecorator abortarTransaccionStatement =  new PreparedStatementDecorator(con.prepareStatement(abortarTransaccionStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		abortarTransaccionStatement.executeUpdate();
		abortarTransaccionStatement.close();
		con.setAutoCommit(true);
	}

	/**
	 * Inicia una transacci�n.
	 * 
	 * @param con
	 *            una conexin abierta con la fuente de datos
	 * @return boolean <b>true</b> si se pudo iniciar la transaccin, <b>false</b>
	 *         si no
	 */
	public boolean beginTransactionSinMensaje(Connection con)
			throws SQLException {
		try {
			con.setAutoCommit(false);
			PreparedStatementDecorator iniciarTransaccionStatement = new PreparedStatementDecorator(con.prepareStatement(inicioTransaccionStr));
			@SuppressWarnings("unused")
			int resultado = iniciarTransaccionStatement.executeUpdate();
			iniciarTransaccionStatement.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Error al iniciar la transaccion");
		}
		return false;
	}

	/**
	 * Termina una transaccin con commit.
	 * @param con
	 *            una conexin abierta con la fuente de datos
	 */
	public void endTransactionSinMensaje(Connection con) throws SQLException {
		PreparedStatementDecorator terminarTransaccionStatement =  new PreparedStatementDecorator(con.prepareStatement(finTransaccionStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		terminarTransaccionStatement.executeUpdate();
		terminarTransaccionStatement.close();
		con.commit();
		con.setAutoCommit(true);
	}

	/**
	 * @param con
	 * @param cadenaSQL
	 * @param filtro
	 */
	@SuppressWarnings("unchecked")
	public boolean bloquearRegistroActualizacion(Connection con, String cadenaSQL, ArrayList filtro) {
		PreparedStatementDecorator ps = null;
		try {
			String[] consulta=((cadenaSQL+"").replace("?", ConstantesBD.separadorSplit)+"").split(ConstantesBD.separadorSplit);

			//ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaSQL, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			String cadena="";
			for (int i = 0; i < filtro.size(); i++) {
				cadena=cadena+consulta[i]+"'"+filtro.get(i)+"'";
			}
			cadena=cadena+consulta[consulta.length-1];
			logger.info("bloqueo--->"+cadena);
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return (new ResultSetDecorator((ps.executeQuery())).next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Obtiene el ultimo valor generado por una secuencia
	 * @param con
	 *            una conexion abierta con la base de datos
	 * @param sequenceName
	 *            el nombre de la secuencia
	 * @return el ultimo valor generado por la secuencia
	 * @throws SQLException
	 */
	public int obtenerUltimoValorSecuencia(Connection con, String sequenceName)
			throws SQLException {
		PreparedStatementDecorator select = null;
		ResultSetDecorator rs = null;
		int result = -1;

		select =  new PreparedStatementDecorator(con.prepareStatement(selectSequenceValStr + sequenceName,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		rs = new ResultSetDecorator(select.executeQuery());
		if (rs != null) {
			rs.next();
			result = rs.getInt("nextvalue");
		}

		return result;
	}

	/**
	 * Metodo que incrementa una secuencia.
	 * @param con
	 * @param nombreSecuencia
	 *            de la secuencia.
	 */
	public int incrementarValorSecuencia(Connection con, String nombreSecuencia) {
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		String consulta = "select nextval('" + nombreSecuencia + "') as valor";
		try {
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if (rs.next()) {
				return rs.getInt("valor");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ConstantesBD.codigoNuncaValido;

	}

	/**
	 * Metodo que retorn el Dao de Utilidades BD
	 * 
	 * @return
	 */
	public UtilidadesBDDao getUtilidadesBDao() {
		return new PostgresqlUtilidadesBDDao();
	}

	/**
	 * Metodo que retorn el Dao de Utilidades Impresion
	 * 
	 * @return
	 */
	public UtilidadImpresionDao getUtilidadImpresionDao() {
		return new PostgresqlUtilidadImpresionDao();
	}


	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ActivPyPXFacturarDao</code>, usada por
	 * <code>ActivPyPXFacturar</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ActivPyPXFacturarDao</code>
	 */
	public ActivPyPXFacturarDao getActivPyPXFacturarDao() {
		return new PostgresqlActivPyPXFacturarDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AdminMedicamentosDao</code>, usada por
	 * <code>AdminMedicamentos</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AdminMedicamentosDao</code>
	 */
	public AdminMedicamentosDao getAdminMedicamentosDao() {
		return new PostgresqlAdminMedicamentosDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AdmisionHospitalariaDao</code>, usada por
	 * <code>AdmisionHospitalaria</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AdmisionHospitalariaDao</code>
	 */
	public AdmisionHospitalariaDao getAdmisionHospitalariaDao() {
		return new PostgresqlAdmisionHospitalariaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AdmisionUrgenciasDao</code>, usada por
	 * <code>AdmisionUrgencias</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AdmisionUrgenciasDao</code>
	 */
	public AdmisionUrgenciasDao getAdmisionUrgenciasDao() {
		return new PostgresqlAdmisionUrgenciasDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AgendaDao</code>, usada por <code>Agenda</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AgendaDao</code>
	 */
	public AgendaDao getAgendaDao() {
		return new PostgresqlAgendaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AjustesEmpresaDao</code>, usada por <code>AjustesEmpresa</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AjustesEmpresaDao</code>
	 */
	public AjustesEmpresaDao getAjustesEmpresaDao() {
		return new PostgresqlAjustesEmpresaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AntecedenteMedicamentoDao</code>, usada por
	 * <code>AntecedenteMedicamento</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AntecedenteMedicamentoDao</code>
	 */
	public AntecedenteMedicamentoDao getAntecedenteMedicamentoDao() {
		return new PostgresqlAntecedenteMedicamentoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AntecedenteMorbidoMedicoDao</code>, usada por
	 * <code>AntecedenteMorbidoMedico</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AntecedenteMorbidoMedicoDao</code>
	 */
	public AntecedenteMorbidoMedicoDao getAntecedenteMorbidoMedicoDao() {
		return new PostgresqlAntecedenteMorbidoMedicoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AntecedenteMorbidoQuirurgicoDao</code>, usada por
	 * <code>AntecedenteMorbidoQuirurgico</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AntecedenteMorbidoQuirurgicoDao</code>
	 */
	public AntecedenteMorbidoQuirurgicoDao getAntecedenteMorbidoQuirurgicoDao() {
		return new PostgresqlAntecedenteMorbidoQuirurgicoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AntecedentePediatricoDao</code>, usada por
	 * <code>AntecedentePediatrico</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AntecedentePediatricoDao</code>
	 */
	public AntecedentePediatricoDao getAntecedentePediatricoDao() {
		return new PostgresqlAntecedentePediatricoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AntecedentesAlergiasDao</code>, usada por
	 * <code>AntecedentesAlergias</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AntecedentesAlergiasDao</code>
	 */
	public AntecedentesAlergiasDao getAntecedentesAlergiasDao() {
		return new PostgresqlAntecedentesAlergiasDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AntecedentesFamiliaresDao</code>, usada por
	 * <code>AntecedentesFamiliares</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AntecedentesFamiliaresDao</code>
	 */
	public AntecedentesFamiliaresDao getAntecedentesFamiliaresDao() {
		return new PostgresqlAntecedentesFamiliaresDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AntecedentesGinecoObstetricosDao</code>, usada por
	 * <code>AntecedenteGinecoObstetrico</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AntecedentesGinecoObstetricosDao</code>
	 */
	public AntecedentesGinecoObstetricosDao getAntecedentesGinecoObstetricosDao() {
		return new PostgresqlAntecedentesGinecoObstetricosDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AntecedentesGinecoObstetricosHistoricoDao</code>, usada por
	 * <code>AntecedenteGinecoObstetricoHistorico</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AntecedentesGinecoObstetricosHistoricoDao</code>
	 */
	public AntecedentesGinecoObstetricosHistoricoDao getAntecedentesGinecoObstetricosHistoricoDao() {
		return new PostgresqlAntecedentesGinecoObstetricosHistoricoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AntecedentesMedicamentosDao</code>, usada por
	 * <code>AntecedentesMedicamentos</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AntecedentesMedicamentosDao</code>
	 */
	public AntecedentesMedicamentosDao getAntecedentesMedicamentosDao() {
		return new PostgresqlAntecedentesMedicamentosDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AntecedentesMorbidosDao</code>, usada por
	 * <code>AntecedentesMorbidos</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AntecedentesMorbidosDao</code>
	 */
	public AntecedentesMorbidosDao getAntecedentesMorbidosDao() {
		return new PostgresqlAntecedentesMorbidosDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AntecedentesToxicosDao</code>, usada por
	 * <code>AntecedentesToxicos</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AntecedentesToxicosDao</code>
	 */
	public AntecedentesToxicosDao getAntecedentesToxicosDao() {
		return new PostgresqlAntecedentesToxicosDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AntecedentesTransfusionalesDao</code>, usada por
	 * <code>AntecedentesTransfusionales</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AntecedentesTransfusionalesDao</code>
	 */
	public AntecedentesTransfusionalesDao getAntecedentesTransfusionalesDao() {
		return new PostgresqlAntecedentesTransfusionalesDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AntecedentesVariosDao</code>, usada por
	 * <code>AntecedenteVario</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AntedentesVariosDao</code>
	 */
	public AntecedentesVariosDao getAntecedentesVariosDao() {
		return new PostgresqlAntecedentesVariosDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AntecedenteToxicoDao</code>, usada por
	 * <code>AntecedenteToxico</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AntecedenteToxicoDao</code>
	 */
	public AntecedenteToxicoDao getAntecedenteToxicoDao() {
		return new PostgresqlAntecedenteToxicoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AntecedenteTransfusionalDao</code>, usada por
	 * <code>AntecedenteTransfusional</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AntecedenteTransfusionalDao</code>
	 */
	public AntecedenteTransfusionalDao getAntecedenteTransfusionalDao() {
		return new PostgresqlAntecedenteTransfusionalDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ArticuloDao</code>, usada por <code>Articulo</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ArticuloDao</code>
	 */
	public ArticuloDao getArticuloDao() {
		return new PostgresqlArticuloDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ArticulosDao</code>, usada por <code>Articulos</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ArticulosDao</code>
	 */
	public ArticulosDao getArticulosDao() {
		return new PostgresqlArticulosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AsociosTipoServicioDao</code>, usada por
	 * <code>AsociosTipoServicio</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>AsociosTipoServicioDao</code>
	 */
	public AsociosTipoServicioDao getAsociosTipoServicioDao() {
		return new PostgresqlAsociosTipoServicioDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AsociosXUvrDao</code>, usada por <code>AsociosXUvr</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>AsociosXUvrDao</code>
	 */
	public AsociosXUvrDao getAsociosXUvrDao() {
		return new PostgresqlAsociosXUvrDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AuxiliarDiagnosticosDao</code>, usada por
	 * <code>AuxiliarDiagnosticos</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AuxiliarDiagnosticosDao</code>
	 */
	public AuxiliarDiagnosticosDao getAuxiliarDiagnosticosDao() {
		return new PostgresqlAuxiliarDiagnosticosDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>BusquedaArticulosGenericaDao</code>, usada por
	 * <code>BusquedaArticulosGenerica</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>BusquedaArticulosGenericaDao</code>
	 */
	public BusquedaArticulosGenericaDao getBusquedaArticulosGenericaDao() {
		return new PostgresqlBusquedaArticulosGenericaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>BusquedaServiciosGenericaDao</code>, usada por
	 * <code>BusquedaServiciosGenerica</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>BusquedaServiciosGenericaDao</code>
	 */
	public BusquedaServiciosGenericaDao getBusquedaServiciosGenericaDao() {
		return new PostgresqlBusquedaServiciosGenericaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>BusquedaCondicionTmExamenGenericaDao</code>, usada por
	 * <code>BusquedaCondicionTmExamenGenerica</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 */
	public BusquedaCondicionTmExamenGenericaDao getBusquedaCondicionTmExamenGenericaDao() {
		return new PostgresqlBusquedaCondicionesTmExamenGenericaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>BusquedaInstitucionesSircGenericaDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>BusquedaInstitucionesSircGenericaDao</code>
	 */
	public BusquedaInstitucionesSircGenericaDao getBusquedaInstitucionesSircGenericaDao() {
		return new PostgresqlBusquedaInstitucionesSircGenericaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>CamaDao</code>, usada por <code>Cama</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>CamaDao</code>
	 */
	public CamaDao getCamaDao() {
		return new PostgresqlCamaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>CamasDao</code>, usada por <code>Camas</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>CamasDao</code>
	 */
	public CamasDao getCamasDao() {
		return new PostgresqlCamasDao();
	}
	
	
	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ListadoIngresos</code>, usada por <code>ListadoIngresos</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ListadoIngresos</code>
	 */
	public ListadoIngresosDao getListadoIngresosDao() {
		return new PostgresqlListadoIngresosDao();
	}

	
	/**
	 * Retorna el DAO con el cual el objeto <code>EquivalentesDeInventarioDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>EquivalentesDeInventarioDao</code>
	 */
	public EquivalentesDeInventarioDao  getEquivalentesDeInventarioDao(){
		return new PostgresqlEquivalentesDeInventarioDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>AsociosXTipoServicioDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>AsociosXTipoServicioDao</code>
	 */
	public AsociosXTipoServicioDao  getAsociosXTipoServicioDao(){
		return new PostgresqlAsociosXTipoServicioDao();
	}
	

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>Camas1Dao</code>, usada por <code>Camas1</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>Camas1Dao</code>
	 */
	public Camas1Dao getCamas1Dao() {
		return new PostgresqlCamas1Dao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>CensoCamas1Dao</code>, usada por <code>CensoCamas1</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>CensoCamas1Dao</code>
	 */
	public CensoCamas1Dao getCensoCamas1Dao() {
		return new PostgresqlCensoCamas1Dao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>CIEDao</code>, usada por <code>CIE</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>CIEDao</code>
	 */
	public CIEDao getCIEDao() {
		return new PostgresqlCIEDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>CitaDao</code>, usada por <code>Cita</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>CitaDao</code>
	 */
	public CitaDao getCitaDao() {
		return new PostgresqlCitaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>CoberturaAccidentesTransitoDao</code>, usada por
	 * <code>CoberturaAccidentesTransito</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>CoberturaAccidentesTransitoDao</code>
	 */
	public CoberturaAccidentesTransitoDao getCoberturaAccidentesTransitoDao() {
		return new PostgresqlCoberturaAccidentesTransitoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ClasificacionSocioEconomicaDao</code>, usada por
	 * <code>ClasificacionSocioEconomica</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ClasificacionSocioEconomicaDao</code>
	 */
	public ClasificacionSocioEconomicaDao getClasificacionSocioEconomicaDao() {
		return new PostgresqlClasificacionSocioEconomicaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ContratoDao</code>, usada por <code>Contrato</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ContratoDao</code>
	 */
	public ContratoDao getContratoDao() {
		return new PostgresqlContratoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ConvenioDao</code>, usada por <code>Convenio</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ConvenioDao</code>
	 */
	public ConvenioDao getConvenioDao() {
		return new PostgresqlConvenioDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>CuentaDao</code>, usada por <code>Cuenta</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>CuentaDao</code>
	 */
	public CuentaDao getCuentaDao() {
		return new PostgresqlCuentaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>DespachoMedicamentosDao</code>, usada por
	 * <code>DespachoMedicamentos</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>DespachoMedicamentosDao</code>
	 */
	public DespachoMedicamentosDao getDespachoMedicamentosDao() {
		return new PostgresqlDespachoMedicamentosDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>DespachoPedidosDao</code>, usada por
	 * <code>DespachoPedidos</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>DespachoPedidosDao</code>
	 */
	public DespachoPedidosDao getDespachoPedidosDao() {
		return new PostgresqlDespachoPedidosDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>DevolucionAFarmaciaDao</code>, usada por
	 * <code>DevolucionAFarmacia</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>DevolucionAFarmaciaDao</code>
	 */
	public DevolucionAFarmaciaDao getDevolucionAFarmaciaDao() {
		return new PostgresqlDevolucionAFarmaciaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>DocumentoAdjuntoDao</code>, usada por
	 * <code>DocumentoAdjunto</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>DocumentoAdjuntoDao</code>
	 */
	public DocumentoAdjuntoDao getDocumentoAdjuntoDao() {
		return new PostgresqlDocumentoAdjuntoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>DocumentosAdjuntosDao</code>, usada por
	 * <code>DocumentosAdjuntos</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>DocumentosAdjuntosDao</code>
	 */
	public DocumentosAdjuntosDao getDocumentosAdjuntosDao() {
		return new PostgresqlDocumentosAdjuntosDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>EgresoDao</code>, usada por <code>Egreso</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>EgresosDao</code>
	 */
	public EgresoDao getEgresoDao() {
		return new PostgresqlEgresoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>EmbarazoDao</code>, usada por <code>Embarazo</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>EmbarazoDao</code>
	 */
	public EmbarazoDao getEmbarazoDao() {
		return new PostgresqlEmbarazoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>EvolucionDao</code>, usada por <code>Evolucion</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>EvolucionDao</code>
	 */
	public EvolucionDao getEvolucionDao() {
		return new PostgresqlEvolucionDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>EvolucionHospitalariaDao</code>, usada por
	 * <code>EvolucionHospitalaria</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>EvolucionHospitalariaDao</code>
	 */
	public EvolucionHospitalariaDao getEvolucionHospitalariaDao() {
		return new PostgresqlEvolucionHospitalariaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ExcepcionesFarmaciaDao</code>, usada por
	 * <code>ExcepcionesFarmacia</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ExcepcionesFarmaciaDao</code>
	 */
	public ExcepcionesFarmaciaDao getExcepcionesFarmaciaDao() {
		return new PostgresqlExcepcionesFarmaciaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ExcepcionesServiciosDao</code>, usada por
	 * <code>ExcepcionesServicios</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ExcepcionesServiciosDao</code>
	 */
	public ExcepcionesServiciosDao getExcepcionesServiciosDao() {
		return new PostgresqlExcepcionesServiciosDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ExcepcionesQXDao</code>, usada por <code>ExcepcionesQX</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ExcepcionesQXDao</code>
	 */
	public ExcepcionesQXDao getExcepcionesQXDao() {
		return new PostgresqlExcepcionesQXDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>FacturaDao</code>, usada por <code>factura</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>FacturaDao</code>
	 */
	public FacturaDao getFacturaDao() {
		return new PostgresqlFacturaDao();
	}
	
	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AnulacionFacturasDao</code>, usada por <code>anulacionfactura</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AnulacionFacturasDao</code>
	 */
	public AnulacionFacturasDao getAnulacionFacturasDao() {
		return new PostgresqlAnulacionFacturasDao();
	}
	
	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>GeneracionCargosPendientesDao</code>, usada por
	 * <code>GeneracionCargosPendientes</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>GeneracionCargosPendientesDao</code>
	 */
	public GeneracionCargosPendientesDao getGeneracionCargosPendientesDao() {
		return new PostgresqlGeneracionCargosPendientesDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>GruposDao</code>, usada por <code>grupos</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>GruposDao</code>
	 */
	public GruposDao getGruposDao() {
		return new PostgresqlGruposDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>HijoBasicoDao</code>, usada por <code>HijoBasico</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>HijoBasicoDao</code>
	 */
	public HijoBasicoDao getHijoBasicoDao() {
		return new PostgresqlHijoBasicoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>HistoriaClinicaDao</code>, usada por
	 * <code>HistoriaClinica</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>HistoriaClinicaDao</code>
	 */
	public HistoriaClinicaDao getHistoriaClinicaDao() {
		return new PostgresqlHistoriaClinicaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>HistoricoAdmisionesDao</code>, usada por
	 * <code>HistoricoAdmisiones</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>HistoricoAdmisionesDao</code>
	 */
	public HistoricoAdmisionesDao getHistoricoAdmisionesDao() {
		return new PostgresqlHistoricoAdmisionesDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>HistoricoEvolucionesDao</code>, usada por
	 * <code>HistoricoEvoluciones</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>EgresosDao</code>
	 */
	public HistoricoEvolucionesDao getHistoricoEvolucionesDao() {
		return new PostgresqlHistoricoEvolucionesDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>HorarioAtencionDao</code>, usada por
	 * <code>HorarioAtencion</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>HorarioAtencionDao</code>
	 */
	public HorarioAtencionDao getHorarioAtencionDao() {
		return new PostgresqlHorarioAtencionDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>InformacionParametrizableDao</code>, usada por
	 * <code>InformacionParametrizable</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>InformacionParametrizableDao</code>
	 */
	public InformacionParametrizableDao getInformacionParametrizableDao() {
		return new PostgresqlInformacionParametrizableDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>IngresoGeneralDao</code>, usada por <code>IngresoGeneral</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>IngresoGeneralDao</code>
	 */
	public IngresoGeneralDao getIngresoGeneralDao() {
		return new PostgresqlIngresoGeneralDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>SolicitudInterconsultaDao</code>, usada por
	 * <code>SolicitarInterconsulta</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>SolicitudInterconsultaDao</code>
	 */
	public SolicitudInterconsultaDao getSolicitudInterconsultaDao() {
		return new PostgresqlSolicitudInterconsultaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ListadoCitasDao</code>, usada por <code>Citas</code>
	 * 
	 * @return Implementacion en PostgreSQL de la interfaz
	 *         <code>ListadoCitasDao</code>
	 */
	public ListadoCitasDao getListadoCitasDao() {
		return new PostgresqlListadoCitasDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>MedicoDao</code>, usada por <code>Medico</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>MedicoDao</code>
	 */
	public MedicoDao getMedicoDao() {
		return new PostgresqlMedicoDao();
	}

	/*
	 * Cada posible DAO, asociado a cada objeto de la aplicacion que necesite
	 * acceder a la base de datos PostgreSQL, debe ser definido a continuacion
	 */

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>MenuDao</code>, usada por <code>MenuFilter</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>MenuDao</code>
	 */
	public MenuDao getMenuDao() {
		return new PostgresqlMenuDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>PacienteDao</code>, usada por <code>Paciente</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>PacienteDao</code>
	 */
	public PacienteDao getPacienteDao() {
		return new PostgresqlPacienteDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ParametrizacionCurvaAlertaDao</code>, usada por
	 * <code>ParametrizacionCurvaAlerta</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ParametrizacionCurvaAlertaDao</code>
	 */
	public ParametrizacionCurvaAlertaDao getParametrizacionCurvaAlertaDao() {
		return new PostgresqlParametrizacionCurvaAlertaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>Pooles</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Pooles</code>
	 */
	public PoolesDao getPoolesDao() {
		return new PostgresqlPoolesDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ParticipacionPoolXTarifasDao</code>, usada por
	 * <code>ParticipacionPoolXTarifa</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ParticipacionPoolXTarifasDao</code>
	 */
	public ParticipacionPoolXTarifasDao getParticipacionPoolXTarifaDao() {
		return new PostgresqlParticipacionPoolXTarifasDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>PersonaBasicaDao</code>, usada por <code>PersonaBasica</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>PersonaBasicaDao</code>
	 */
	public PersonaBasicaDao getPersonaBasicaDao() {
		return new PostgresqlPersonaBasicaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>PersonaDao</code>, usada por <code>Persona</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>PersonaDao</code>
	 */
	public PersonaDao getPersonaDao() {
		return new PostgresqlPersonaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ProcedimientoDao</code>, usada por <code>Procedimiento</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ProcedimientoDao</code>
	 */
	public ProcedimientoDao getProcedimientoDao() {
		return new PostgresqlProcedimientoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>RequisitosPacienteDao</code>, usada por
	 * <code>RequisitosPaciente</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>RequisitosPacienteDao</code>
	 */
	public RequisitosPacienteDao getRequisitosPacienteDao() {
		return new PostgresqlRequisitosPacienteDao();
	}

	

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>RolesFuncsDao</code>, usada por <code>RolesFuncsBean</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>RolesFuncsDao</code>
	 */
	public RolesFuncsDao getRolesFuncsDao() {
		return new PostgresqlRolesFuncsDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ServiciosDao</code>, usada por <code>Servicios</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ServiciosDao</code>
	 */
	public ServiciosDao getServiciosDao() {
		return new PostgresqlServiciosDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ServiciosCamas1Dao</code>, usada por
	 * <code>ServiciosCamas1</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ServiciosCamas1Dao</code>
	 */
	public ServiciosCamas1Dao getServiciosCamas1Dao() {
		return new PostgresqlServiciosCamas1Dao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>SolicitudConsultaExternaDao</code>, usada por
	 * <code>SolicitudConsultaExterna</code>
	 * 
	 * @return Implementacion en PostgreSQL de la interfaz
	 *         <code>SolicitudConsultaExternaDao</code>
	 */
	public SolicitudConsultaExternaDao getSolicitudConsultaExternaDao() {
		return new PostgresqlSolicitudConsultaExternaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>SolicitudDao</code>, usada por <code>Solicitud</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>SolicitudDao</code>
	 */
	public SolicitudDao getSolicitudDao() {
		return new PostgresqlSolicitudDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>SolicitudesDao</code>, usada por <code>Solicitudes</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>SolicitudesDao</code>
	 */
	public SolicitudesDao getSolicitudesDao() {
		return new PostgresqlSolicitudesDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>SolicitudesCxDao</code>, usada por <code>SolicitudesCx</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>SolicitudesCxDao</code>
	 */
	public SolicitudesCxDao getSolicitudesCxDao() {
		return new PostgresqlSolicitudesCxDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>SolicitudProcedimientoDao</code>, usada por
	 * <code>SolicitudProcedimiento</code>
	 * 
	 * @return Implementacion en PostgreSQL de la interfaz
	 *         <code>SolicitudProcedimientoDao</code>
	 */
	public SolicitudProcedimientoDao getSolicitudProcedimientoDao() {
		return new PostgresqlSolicitudProcedimientoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>TagDao</code>, usada por los <i>custom tags</i>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>TagDao</code>
	 */
	public TagDao getTagDao() {
		return new PostgresqlTagDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>UsuarioBasicoDao</code>, usada por <code>UsuarioBasico</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>UsuarioBasicoDao</code>
	 */
	public UsuarioBasicoDao getUsuarioBasicoDao() {
		return new PostgresqlUsuarioBasicoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>UsuarioDao</code>, usada por <code>Usuario</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>UsuarioDao</code>
	 */
	public UsuarioDao getUsuarioDao() {
		return new PostgresqlUsuarioDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>UtilidadValidacionDao</code>, usada por
	 * <code>UtilidadValidacion</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>UtilidadValidacionDao</code>
	 */
	public UtilidadValidacionDao getUtilidadValidacionDao() {
		return new PostgresqlUtilidadValidacionDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ValidacionesSolicitudDao</code>, usada por
	 * <code>ValidacionesSolicitud</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ValidacionesSolicitudDao</code>
	 */
	public ValidacionesSolicitudDao getValidacionesSolicitudDao() {
		return new PostgresqlValidacionesSolicitudDao();
	}

	

	

	

	

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>EmpresaDao</code>, usada por <code>Empresa</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>EmpresaDao</code>
	 */
	public EmpresaDao getEmpresaDao() {
		return new PostgresqlEmpresaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>TarifaISSDao</code>, usada por <code>TarifaISS</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>TarifaISSDao</code>
	 */
	public TarifaISSDao getTarifaISSDao() {
		return new PostgresqlTarifaISSDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>TarifasISSDao</code>, usada por <code>TarifasISS</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>TarifasISSDao</code>
	 */
	public TarifasISSDao getTarifasISSDao() {
		return new PostgresqlTarifasISSDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>TarifaSOATDao</code>, usada por <code>TarifaSOAT</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>TarifaSOATDao</code>
	 */
	public TarifaSOATDao getTarifaSOATDao() {
		return new PostgresqlTarifaSOATDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>TarifasSOATDao</code>, usada por <code>TarifasSOAT</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>TarifasSOATDao</code>
	 */
	public TarifasSOATDao getTarifasSOATDao() {
		return new PostgresqlTarifasSOATDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>EsquemaTarifarioDao</code>, usada por
	 * <code>EsquemaTarifario</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>EsquemaTarifarioDao</code>
	 */
	public EsquemaTarifarioDao getEsquemaTarifarioDao() {
		return new PostgresqlEsquemaTarifarioDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>EstadoCuentaDao</code>, usada por <code>EstadoCuenta</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>EstadoCuentaDao</code>
	 */
	public EstadoCuentaDao getEstadoCuentaDao() {
		return new PostgresqlEstadoCuentaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ExcepcionesTarifasDao</code>, usada por
	 * <code>ExcepcionesTarifas</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ExcepcionesTarifasDao</code>
	 */
	public ExcepcionesTarifasDao getExcepcionesTarifasDao() {
		return new PostgresqlExcepcionesTarifasDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>RecargoTarifaDao</code>, usada por <code>RecargoTarifa</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>RecargoTarifaDao</code>
	 */
	public RecargoTarifaDao getRecargoTarifaDao() {
		return new PostgresqlRecargoTarifaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>RecargosTarifasDao</code>, usada por
	 * <code>RecargosTarifas</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>RecargosTarifasDao</code>
	 */
	public RecargosTarifasDao getRecargosTarifasDao() {
		return new PostgresqlRecargosTarifasDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>SalarioMinimoDao</code>, usada por <code>SalarioMinimo</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>SalarioMinimoDao</code>
	 */
	public SalarioMinimoDao getSalarioMinimoDao() {
		return new PostgresqlSalarioMinimoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>RecargoTarifasDao</code>, usada por <code>RecargoTarifas</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>RecargoTarifasDao</code>
	 */
	public RecargoTarifasDao getRecargoTarifasDao() {
		return new PostgresqlRecargoTarifasDao();
	}

	

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>MantenimientoTablasDao</code>, usada por
	 * <code>MantenimientoTablas</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>MantenimientoTablasDao</code>
	 */
	public MantenimientoTablasDao getMantenimientoTablasDao() {
		return new PostgresqlMantenimientoTablasDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>TerceroDao</code>, usada por <code>Tercero</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>TerceroDao</code>
	 */
	public TerceroDao getTerceroDao() {
		return new PostgresqlTerceroDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>TopesfacturacionDao</code>, usada por
	 * <code>TopesFacturacionEconomica</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>TopesFacturacionDao</code>
	 */
	public TopesFacturacionDao getTopesFacturacionDao() {
		return new PostgresqlTopesFacturacionDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ResumenAtencionesDao</code>, usada por
	 * <code>ResumenAtencionesDao</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ResumenAtencionesDao</code>
	 */
	public ResumenAtencionesDao getResumenAtencionesDao() {
		return new PostgresqlResumenAtencionesDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>SolicitudEvolucionDao</code>, usada por
	 * <code>SolicitudEvolucion</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>SolicitudEvolucionDao</code>
	 */
	public SolicitudEvolucionDao getSolicitudEvolucionDao() {
		return new PostgresqlSolicitudEvolucionDao();
	}

	/**
	 * Retorna la implementacion en Hsqldb de la interfaz
	 * <code>MontosCobroDao</code>, usada por <code>MontosCobro</code>.
	 * 
	 * @return la implementacion en Postgresql de la interfaz
	 *         <code>MontosCobroDao</code>
	 */
	public MontosCobroDao getMontosCobroDao() {
		return new PostgresqlMontosCobroDao();
	}

	/**
	 * Retorna la implementacion en Hsqldb de la interfaz
	 * <code>MovimientoFacturasDao</code>, usada por
	 * <code>MovimientoFacturas</code>.
	 * 
	 * @return la implementacion en Postgresql de la interfaz
	 *         <code>MovimientoFacturasDao</code>
	 */
	public MovimientoFacturasDao getMovimientoFacturasDao() {
		return new PostgresqlMovimientoFacturasDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>PagosEmpresaDao</code>, usada por <code>PagosEmpresa</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>PagosEmpresaDao</code>
	 */
	public PagosEmpresaDao getPagosEmpresaDao() {
		return new PostgresqlPagosEmpresaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>PagosPaciente</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public PagosPacienteDao getPagosPacienteDao() {
		return new PostgresqlPagosPacienteDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>MontosCobroDao</code>, usada por <code>MontosCobroDao</code>.
	 * 
	 * @return la implementacion en Postgresql de la interfaz
	 *         <code>MontosCobroDao</code>
	 */
	public ActivacionCargosDao getActivacionCargosDao() {
		return new PostgresqlActivacionCargosDao();
	}

	/**
	 * Retorna el Dao con el cual el objeto<code>RegistrosUnidadesConsulta</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public RegUnidadesConsultaDao getRegUnidadesConsultaDao() {
		return new PostgresqlRegUnidadesConsultaDao();

	}

	/**
	 * Retorna el DAO con el cual el objeto <code>RegistroDiagnosticos</code>
	 * interactura con la fuente de datos.
	 */
	public RegistroDiagnosticosDao getRegistroDiagnosticosDao() {
		return new PostgresqlRegistroDiagnosticosDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ExcepcionesNaturalezaPacienteDao</code>, usada por
	 * <code>ExcepcionesNaturalezaPaciente</code>.
	 * 
	 * @return la implementacion en Postgresql de la interfaz
	 *         <code>ExcepcionesNaturalezaPacienteDao</code>
	 */
	public ExcepcionesNaturalezaPacienteDao getExcepcionesNaturalezaPacienteDao() {
		return new PostgresqlExcepcionesNaturalezaPacienteDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ValoresPorDefectoDao</code>
	 * interactura con la fuente de datos.
	 */
	public ValoresPorDefectoDao getValoresPorDefectoDao() {
		return new PostgresqlValoresPorDefectoDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>RegistroDiagnosticos</code>
	 * interactura con la fuente de datos.
	 */
	public SustitutosInventariosDao getSustitutosInventariosDao() {
		return new PostgresqlSustitutosInventariosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>TarifasInventario</code>
	 * interactura con la fuente de datos.
	 */
	public TarifasInventarioDao getTarifasInventarioDao() {
		return new PostgresqlTarifasInventarioDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>SolicitudMedicamentos</code>
	 * interactura con la fuente de datos.
	 */
	public SolicitudMedicamentosDao getSolicitudMedicamentosDao() {
		return new PostgresqlSolicitudMedicamentosDao();
	}

	/**
	 * Retorna el Dao con el cual el objeto<code>RegistrosUnidadesConsulta</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public PedidosInsumosDao getPedidosInsumosDao() {
		return new PostgresqlPedidosInsumosDao();

	}

	/**
	 * Retorna el DAO con el cual el objeto <code>DevolucionPedidos</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public DevolucionPedidosDao getDevolucionPedidosDao() {
		return new PostgresqlDevolucionPedidosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>DevolucionPedidos</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public RecepcionDevolucionPedidosDao getRecepcionDevolucionPedidosDao() {
		return new PostgresqlRecepcionDevolucionPedidosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>DevolucionPedidos</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public RecepcionDevolucionMedicamentosDao getRecepcionDevolucionMedicamentosDao() {
		return new PostgresqlRecepcionDevolucionMedicamentosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ParamInstitucionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return Objeto <code>ParamInstitucionDao</code> para la comunicacion
	 *         con la BD PostgresQL
	 */
	public ParamInstitucionDao getParamInstitucionDao() {
		return new PostgresqlParamInstitucionDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>MedicosXPoolDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return Objeto <code>MedicosXPoolDao</code> para la comunicacion con la
	 *         BD PostgresQL
	 */
	public MedicosXPoolDao getMedicosXPoolDao() {
		return new PostgresqlMedicosXPoolDao();
	}



	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>GeneracionExcepcionesFarmaciaDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return Objeto <code>GeneracionExcepcionesFarmaciaDao</code> para la
	 *         comunicacion con la BD PostgresQL
	 */
	public GeneracionExcepcionesFarmaciaDao getGeneracionExcepcionesFarmaciaDao() {
		return new PostgresqlGeneracionExcepcionesFarmaciaDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ValidacionesFacturacionDao</code>, usada por
	 * <code>ValidacionesFacturacion</code>.
	 * 
	 * @return la implementacion en Postgresql de la interfaz
	 *         <code>ValidacionesFacturacionDao</code>
	 */
	public ValidacionesFacturaDao getValidacionesFacturaDao() {
		return new PostgresqlValidacionesFacturaDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>AbonosYDescuentosDao</code>, usada por
	 * <code>AbonosYDescuentosDao</code>.
	 * 
	 * @return la implementacion en Postgresql de la interfaz
	 *         <code>AbonosYDescuentosDao</code>
	 */
	public AbonosYDescuentosDao getAbonosYDescuentosDao() {
		return new PostgresqlAbonosYDescuentosDao();
	}

	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>OrdenMedicaDao</code>, usada por <code>OrdenMedicaDao</code>.
	 * 
	 * @return la implementacion en Postgresql de la interfaz
	 *         <code>OrdenMedicaDao</code>
	 */
	public OrdenMedicaDao getOrdenMedicaDao() {
		return new PostgresqlOrdenMedicaDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>HojaObstetricaDao</code>, usada por
	 * <code>HojaObstetricaDao</code>.
	 * 
	 * @return la implementacion en Postgresql de la interfaz
	 *         <code>HojaObstetricaDao</code>
	 */
	public HojaObstetricaDao getHojaObstetricaDao() {
		return new PostgresqlHojaObstetricaDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>HojaOftalmologicaDao</code>, usada por
	 * <code>HojaOftalmologicaDao</code>.
	 * 
	 * @return la implementacion en Postgresql de la interfaz
	 *         <code>HojaOftalmologicaDao</code>
	 */
	public HojaOftalmologicaDao getHojaOftalmologicaDao() {
		return new PostgresqlHojaOftalmologicaDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>AntecedentesOftalmologicosDao</code>, usada por
	 * <code>AntecedentesOftalmologicosDao</code>.
	 * 
	 * @return la implementacion en Postgresql de la interfaz
	 *         <code>AntecedentesOftalmologicosDao</code>
	 */
	public AntecedentesOftalmologicosDao getAntecedentesOftalmologicosDao() {
		return new PostgresqlAntecedentesOftalmologicosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ProgramacionCirugia</code>, usada por
	 * <code>ProgramacionCirugiaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ProgramacionCirugia</code>
	 */
	public ProgramacionCirugiaDao getProgramacionCirugiaDao() {
		return new PostgresqlProgramacionCirugiaDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>UtilidadesDao</code>, usada por <code>Utilidades</code>.
	 * 
	 * @return la implementacion en Postgresql de la interfaz
	 *         <code>UtilidadesDao</code>
	 */
	public UtilidadesDao getUtilidadesDao() {
		return new PostgresqlUtilidadesDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>TriageDao</code>, usada por <code>TriageDao</code>.
	 * 
	 * @return la implementacion en Postgresql de la interfaz
	 *         <code>TriageDao</code>
	 */
	public TriageDao getTriageDao() {
		return new PostgresqlTriageDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>CuentasCobroDao</code>, usada por <code>CuentasCobro</code>.
	 * 
	 * @return la implementacion en Postgresql de la interfaz
	 *         <code>CuentasCobroDao</code>
	 */
	public CuentasCobroDao getCuentasCobroDao() {
		return new PostgresqlCuentasCobroDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>DiasFestivosDao</code>, usada por <code>DiasFestivos</code>.
	 * 
	 * @return la implementacion en Postgresql de la interfaz
	 *         <code>DiasFestivosDao</code>
	 */
	public DiasFestivosDao getDiasFestivosDao() {
		return new PostgresqlDiasFestivosDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ValidacionesCierreCuentaDao</code>, usada por
	 * <code>ValidacionesCierreCuenta</code>.
	 * 
	 * @return la implementacion en Postgresql de la interfaz
	 *         <code>ValidacionesCierreCuentaDao</code>
	 */
	public ValidacionesCierreCuentaDao getValidacionesCierreCuentaDao() {
		return new PostgresqlValidacionesCierreCuentaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>MotivoAnulacionFacturasDao </code>, usada por
	 * <code>MotivoAnulacionFacturas</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>MotivoAnulacionFacturasDao </code>
	 */
	public MotivoAnulacionFacturasDao getMotivoAnulacionFacturasDao() {
		return new PostgresqlMotivoAnulacionFacturasDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConceptosPagoCarteraDao</code>, usada por
	 * <code>ConceptosPagoCarteraDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ConceptosPagoCarteraDao</code>
	 */
	public ConceptosCarteraDao getConceptosCarteraDao() {
		return new PostgresqlConceptosCarteraDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>MotivoAnulacionFacturasDao</code>, usada por
	 * <code>ActualizacionAutorizacion</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ActualizacionAutorizacionDao</code>
	 */
	public ActualizacionAutorizacionDao getActualizacionAutorizacionDao() {
		return new PostgresqlActualizacionAutorizacionDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>JustificacionDinamicaDao</code>, usada por
	 * <code>JustificacionDinamicaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>JustificacionDinamicaDao</code>
	 */
	public JustificacionDinamicaDao getJustificacionDinamicaDao() {
		return new PostgresqlJustificacionDinamicaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaFacturasDao</code>, usada por
	 * <code>ConsultaFacturas</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ConsultaFacturasDao</code>
	 */
	public ConsultaFacturasDao getConsultaFacturasDao() {
		return new PostgresqlConsultaFacturasDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz <code>RipsDao</code>,
	 * usada por <code>RipsDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz <code>RipsDao</code>
	 */
	public RipsDao getRipsDao() {
		return new PostgresqlRipsDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConceptoTesoreriaDao</code>, usada por
	 * <code>ConceptoTesoreriaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ConceptoTesoreriaDao</code>
	 */
	public ConceptoTesoreriaDao getTesoreriaDao() {
		return new PostgresqlConceptoTesoreriaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>TrasladoCamasDao</code>, usada por <code>TrasladoCamasDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>TrasladoCamasDao</code>
	 */
	public TrasladoCamasDao getTrasladoCamasDao() {
		return new PostgresqlTrasladoCamasDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsecutivosDisponiblesDao</code>, usada por
	 * <code>ConsecutivosDisponiblesDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ConsecutivosDisponiblesDao</code>
	 */
	public ConsecutivosDisponiblesDao getConsecutivosDisponiblesDao() {
		return new PostgresqlConsecutivosDisponiblesDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CierreSaldoInicialCarteraDao</code>, usada por
	 * <code>CierreSaldoInicialCarteraDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>CierreSaldoInicialCarteraDao</code>
	 */
	public CierreSaldoInicialCarteraDao getCierreSaldoInicialCarteraDao() {
		return new PostgresqlCierreSaldoInicialCarteraDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>ValidacionesAnulacionFacturasDao</code>, usada por
	 * <code>ValidacionesAnulacionFacturas</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ValidacionesAnulacionFacturasDao</code>
	 */
	public ValidacionesAnulacionFacturasDao getValidacionesAnulacionFacturasDao() {
		return new PostgresqlValidacionesAnulacionFacturasDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>EstanciaAutomaticaDao</code>, usada por
	 * <code>EstanciaAutomatica</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>EstanciaAutomaticaDao</code>
	 */
	public EstanciaAutomaticaDao getEstanciaAutomaticaDao() {
		return new PostgresqlEstanciaAutomaticaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AprobacionAjustesEmpresaDao</code>, usada por
	 * <code>AprobacionAjustesEmpresaDao</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AprobacionAjustesEmpresaDao</code>
	 */
	public AprobacionAjustesEmpresaDao getAprobacionAjustesEmpresaDao() {
		return new PostgresqlAprobacionAjustesEmpresaDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>TiposMonitoreoDao</code>, usada por
	 * <code>TiposMonitoreoDao</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>TiposMonitoreoDao</code>
	 */
	public TiposMonitoreoDao getTiposMonitoreoDao() {
		return new PostgresqlTiposMonitoreoDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>TipoSalasDao</code>, usada por <code>TipoSalasDao</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>TipoSalasDao</code>
	 */
	public TipoSalasDao getTipoSalasDao() {
		return new PostgresqlTipoSalasDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>SalasDao</code>, usada por <code>SalasDao</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>SalasDao</code>
	 */
	public SalasDao getSalasDao() {
		return new PostgresqlSalasDao();
	}

	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>PorcentajesCxMultiplesDao</code>, usada por
	 * <code>PorcentajesCxMultiplesDao</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>PorcentajesCxMultiplesDao</code>
	 */
	public PorcentajesCxMultiplesDao getPorcentajesCxMultiplesDao() {
		return new PostgresqlPorcentajesCxMultiplesDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CajasDao</code>, usada por <code>CajasDao</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>CajasDao</code>
	 */
	public CajasDao getCajaDao() {
		return new PostgresqlCajasDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CajasCajerosDao</code>, usada por <code>CajasCajerosDao</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>CajasCajerosDao</code>
	 */
	public CajasCajerosDao getCajasCajerosDao() {
		return new PostgresqlCajasCajerosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FormasPagoDao</code>, usada por <code>FormasPagoDao</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>FormasPagoDao</code>
	 */
	public FormasPagoDao getFormasPagoDao() {
		return new PostgresqlFormasPagoDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>EntidadesFinancierasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>EntidadesFinancierasDao</code>
	 */
	public EntidadesFinancierasDao getEntidadesFinancierasDao() {
		return new PostgresqlEntidadesFinancierasDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ExcepcionAsocioTipoSalaDao</code>, usada por
	 * <code>ExcepcionAsocioTipoSalaDao</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>ExcepcionAsocioTipoSalaDao</code>
	 */
	public ExcepcionAsocioTipoSalaDao getExcepcionAsocioTipoSalaDao() {
		return new PostgresqlExcepcionAsocioTipoSalaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>TarjetasFinancierasDao</code>, usada por
	 * <code>TarjetasFinancierasDao</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>TarjetasFinancierasDao</code>
	 */
	public TarjetasFinancierasDao getTarjetasFinancierasDao() {
		return new PostgresqlTarjetasFinancierasDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>RecibosCajaDao</code>, usada por <code>RecibosCajaDao</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>RecibosCajaDao</code>
	 */
	public RecibosCajaDao getRecibosCajaDao() {
		return new PostgresqlRecibosCajaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AnulacionRecibosCajaDao</code>, usada por
	 * <code>AnulacionRecibosCajaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>AnulacionRecibosCajaDao</code>
	 */
	public AnulacionRecibosCajaDao getAnulacionRecibosCajaDao() {
		return new PostgresqlAnulacionRecibosCajaDao();
	}

	

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>MaterialesQxDao</code>, usada por <code>MaterialesQxDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>MaterialesQxDao</code>
	 */
	public MaterialesQxDao getMaterialesQxDao() {
		return new PostgresqlMaterialesQxDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaReciboCajaDao</code>, usada por
	 * <code>ConsultaReciboCajaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ConsultaReciboCajaDao</code>
	 */
	public ConsultaReciboCajaDao getConsultaReciboCajaDao() {
		return new PostgresqlConsultaRecibosCajaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>PeticionDao</code>, usada por <code>PeticionDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>PeticionDao</code>
	 */
	public PeticionDao getPeticionDao() {
		return new PostgresqlPeticionDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>PreanestesiaDao</code>, usada por <code>PreanestesiaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>PreanestesiaDao</code>
	 */
	public PreanestesiaDao getPreanestesiaDao() {
		return new PostgresqlPreanestesiaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AprobacionPagosCarteraDao</code>, usada por
	 * <code>AprobacionPagosCarteraDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>AprobacionPagosCarteraDao</code>
	 */
	public AprobacionPagosCarteraDao getAprobacionPagosCarteraDao() {
		return new PostgresqlAprobacionPagosCarteraDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AplicacionPagosEmpresaDao</code>, usada por
	 * <code>AplicacionPagosEmpresaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>AplicacionPagosEmpresaDao</code>
	 */
	public AplicacionPagosEmpresaDao getAplicacionPagosEmpresaDao() {
		return new PostgresqlAplicacionPagosEmpresaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaFacturasDao</code>, usada por
	 * <code>ResponderCirugias</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ResponderCirugias</code>
	 */
	public ResponderCirugiasDao getResponderCirugiasDao() {
		return new PostgresqlResponderCirugiasDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaFacturasDao</code>, usada por
	 * <code>NotasGeneralesEnfermeria</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>NotasGeneralesEnfermeria</code>
	 */
	public NotasGeneralesEnfermeriaDao getNotasGeneralesEnfermeriaDao() {
		return new PostgresqlNotasGeneralesEnfermeriaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>HojaAnestesiaDao</code>, usada por <code>HojaAnestesiaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>HojaAnestesiaDao</code>
	 */
	public HojaAnestesiaDao getHojaAnestesiaDao() {
		return new PostgresqlHojaAnestesiaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>TiposTransaccionesInvDao</code>, usada por
	 * <code>TiposTransaccionesInvDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>TiposTransaccionesInvDao</code>
	 */
	public TiposTransaccionesInvDao getTiposTransaccionesInvDao() {
		return new PostgresqlTiposTransaccionesInvDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>HojaQuirurgicaDao</code>, usada por
	 * <code>HojaQuirurgicaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>HojaQuirurgicaDao</code>
	 */
	public HojaQuirurgicaDao getHojaQuirurgicaDao() {
		return new PostgresqlHojaQuirurgicaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>TransaccionesValidasXCCDao</code>, usada por
	 * <code>TransaccionesValidasXCCDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>TransaccionesValidasXCCDao</code>
	 */
	public TransaccionesValidasXCCDao getTransaccionesValidasXCCDao() {
		return new PostgresqlTransaccionesValidasXCCDao();
	}

	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>MotDevolucionInventariosDao</code>, usada por
	 * <code>MotDevolucionInventariosDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>MotDevolucionInventariosDao</code>
	 */
	public MotDevolucionInventariosDao getMotDevolucionInventariosDao() {
		return new PostgresqlMotDevolucionInventariosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ArticulosPuntoPedidoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ArticulosPuntoPedidoDao</code>
	 */
	public ArticulosPuntoPedidoDao getArticulosPuntoPedidoDao() {
		return new PostgresqlArticulosPuntoPedidoDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>UsuariosXAlmacenDao</code>, usada por
	 * <code>UsuariosXAlmacenDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>UsuariosXAlmacenDao</code>
	 */
	public UsuariosXAlmacenDao getUsuariosXAlmacenDao() {
		return new PostgresqlUsuariosXAlmacenDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>NotasRecuperacionDao</code>, usada por
	 * <code>NotasRecuperacion</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>NotasRecuperacion</code>
	 */
	public NotasRecuperacionDao getNotasRecuperacionDao() {
		return new PostgresqlNotasRecuperacionDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ModificarReversarQxDao</code>, usada por
	 * <code>ModificarReversarQxDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ModificarReversarQxDao</code>
	 */
	public ModificarReversarQxDao getModificarReversarQxDao() {
		return new PostgresqlModificarReversarQxDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ModificarReversarQxDao</code>, usada por
	 * <code>ModificarReversarQxDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ModificarReversarQxDao</code>
	 */
	public UtilidadInventariosDao getUtilidadInventariosDao() {
		return new PostgresqlUtilidadInventariosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>UtilidadLaboratoriosDao</code>, usada por
	 * <code>UtilidadLaboratoriosDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>UtilidadLaboratoriosDao</code>
	 */
	public UtilidadLaboratoriosDao getUtilidadLaboratoriosDao() {
		return new PostgresqlUtilidadLaboratoriosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>RegistroTransaccionesDao</code>, usada por
	 * <code>RegistroTransaccionesDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>RegistroTransaccionesDao</code>
	 */
	public RegistroTransaccionesDao getRegistroTransaccionesDao() {
		return new PostgresqlRegistroTransaccionesDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>PresupuestoPacienteDao</code>, usada por
	 * <code>PresupuestoPacienteDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>PresupuestoPacienteDao</code>
	 */
	public PresupuestoPacienteDao getPresupuestoPacienteDao() {
		return new PostgresqlPresupuestoPacienteDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>PresupuestoPacienteDao</code>, usada por
	 * <code>CierresInventarioDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>CierresInventarioDao</code>
	 */
	public CierresInventarioDao getCierresInventarioDao() {
		return new PostgresqlCierresInventarioDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>KardexDao</code>, usada por <code>KardexDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>KardexDao</code>
	 */
	public KardexDao getKardexDao() {
		return new PostgresqlKardexDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaHonorariosMedicosDao</code>, usada por
	 * <code>ConsultaHonorariosMedicosDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ConsultaHonorariosMedicosDao</code>
	 */
	public ConsultaHonorariosMedicosDao getConsultaHonorariosMedicosDao() {
		return new PostgresqlConsultaHonorariosMedicosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>SolicitudTrasladoAlmacenDao</code>, usada por
	 * <code>SolicitudTrasladoAlmacenDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>SolicitudTrasladoAlmacenDao</code>
	 */
	public SolicitudTrasladoAlmacenDao getSolicitudTrasladoAlmacenDao() {
		return new PostgresqlSolicitudTrasladoAlmacenDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ExistenciasInventariosDao</code>, usada por
	 * <code>ExistenciasInventariosDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ExistenciasInventariosDao</code>
	 */
	public ExistenciasInventariosDao getExistenciasInventariosDao() {
		return new PostgresqlExistenciasInventariosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FormatoImpresionPresupuestoDao</code>, usada por
	 * <code>FormatoImpresionPresupuestoDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>FormatoImpresionPresupuestoDao</code>
	 */
	public FormatoImpresionPresupuestoDao getFormatoImpresionPresupuestoDao() {
		return new PostgresqlFormatoImpresionPresupuestoDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaPresupuestoDao</code>, usada por
	 * <code>ConsultaPresupuestoDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ConsultaPresupuestoDao</code>
	 */
	public ConsultaPresupuestoDao getConsultaPresupuestoDao() {
		return new PostgresqlConsultaPresupuestoDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>GruposServiciosDao</code>, usada por
	 * <code>GruposServiciosDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>GruposServiciosDao</code>
	 */
	public GruposServiciosDao getGruposServiciosDao() {
		return new PostgresqlGruposServiciosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>DespachoTrasladoAlmacenDao</code>, usada por
	 * <code>DespachoTrasladoAlmacenDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>DespachoTrasladoAlmacenDao</code>
	 */
	public DespachoTrasladoAlmacenDao getDespachoTrasladoAlmacenDao() {
		return new PostgresqlDespachoTrasladoAlmacenDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaImpresionTrasladosDao</code>, usada por
	 * <code>ConsultaImpresionTrasladosDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ConsultaImpresionTrasladosDao</code>
	 */
	public ConsultaImpresionTrasladosDao getConsultaImpresionTrasladosDao() {
		return new PostgresqlConsultaImpresionTrasladosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FormatoImpresionFacturaDao</code>, usada por
	 * <code>FormatoImpresionFacturaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>FormatoImpresionFacturaDao</code>
	 */
	public FormatoImpresionFacturaDao getFormatoImpresionFacturaDao() {
		return new PostgresqlFormatoImpresionFacturaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>RegistroEnfermeriaDao</code>, usada por
	 * <code>RegistroEnfermeriaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>RegistroEnfermeriaDao</code>
	 */
	public RegistroEnfermeriaDao getRegistroEnfermeriaDao() {
		return new PostgresqlRegistroEnfermeriaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>RegistroEnfermeriaDao</code>, usada por
	 * <code>ImpresionFacturaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ImpresionFacturaDao</code>
	 */
	public ImpresionFacturaDao getImpresionFacturaDao() {
		return new PostgresqlImpresionFacturaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ExTarifasAsociosDao</code>, usada por
	 * <code>ExTarifasAsociosDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ExTarifasAsociosDao</code>
	 */
	public ExTarifasAsociosDao getExTarifasAsociosDao() {
		return new PostgresqlExTarifasAsociosDao();	
	}
 
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaProfesionalPoolDao</code>, usada por
	 * <code>ConsultaProfesionalPoolDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ConsultaProfesionalPoolDao</code>
	 */
	public ConsultaProfesionalPoolDao getConsultaProfesionalPoolDao() {
		return new PostgresqlConsultaProfesionalPoolDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaProfesionalPoolDao</code>, usada por
	 * <code>ConsultaProfesionalPoolDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ConsultaProfesionalPoolDao</code>
	 */
	public ConsultaTarifasServiciosDao getConsultaTarifasServiciosDao() {
		return new PostgresqlConsultaTarifasServiciosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CuentaInventarioDao</code>, usada por
	 * <code>CuentaInventarioDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>CuentaInventarioDao</code>
	 */
	public CuentaInventarioDao getCuentaInventarioDao() {
		return new PostgresqlCuentaInventarioDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CuentaInventarioDao</code>, usada por
	 * <code>CuentaInventarioDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>CuentaInventarioDao</code>
	 */
	public CuentaServicioDao getCuentaServicioDao() {
		return new PostgresqlCuentaServicioDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>getCuentasConveniosDao</code>, usada por
	 * <code>getCuentasConveniosDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>getCuentasConveniosDao</code>
	 */
	public CuentasConveniosDao getCuentasConveniosDao() {
		return new PostgresqlCuentasConveniosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>getArqueosDao</code>, usada por <code>getArqueosDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>getArqueosDao</code>
	 */
	public ArqueosDao getArqueosDao() {
		return new PostgresqlArqueosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CampoInterfazDao</code>, usada por <code>CampoInterfazDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>CampoInterfazDao</code>
	 */
	public CampoInterfazDao getCampoInterfazDao() {
		return new PostgresqlCampoInterfazDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ParamInterfazDao</code>, usada por <code>ParamInterfaz</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ParamInterfazDao</code>
	 */
	public ParamInterfazDao getParamInterfazDao() {
		return new PostgresqlParamInterfazDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>UnidadFuncionalDao</code>, usada por
	 * <code>UnidadFuncional</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>UnidadFuncionalDao</code>
	 */
	public UnidadFuncionalDao getUnidadFuncionalDao() {
		return new PostgresqlUnidadFuncionalDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>UnidadFuncionalDao</code>, usada por
	 * <code>UnidadFuncional</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>UnidadFuncionalDao</code>
	 */
	public CuposExtraDao getCuposExtraDao() {
		return new PostgresqlCuposExtraDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ReasignarAgendaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ReasignarAgenda</code>
	 */
	public ReasignarAgendaDao getReasignarAgendaDao() {
		return new PostgresqlReasignarAgendaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaLogCuposExtraDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ConsultaLogCuposExtra</code>
	 */
	public ConsultaLogCuposExtraDao getConsultaLogCuposExtraDao() {
		return new PostgresqlConsultaLogCuposExtraDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>CentrosCostoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CentrosCosto</code>
	 */
	public CentrosCostoDao getCentrosCostoDao() {
		return new PostgresqlCentrosCostoDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CentroCostoGrupoServicioDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>CentroCostoGrupoServicio</code>
	 */
	public CentroCostoGrupoServicioDao getCentroCostoGrupoServicioDao() {
		return new PostgresqlCentroCostoGrupoServicioDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>getCentrosAtencionDao</code>, usada por
	 * <code>getCentrosAtencionDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>getCentrosAtencionDao</code>
	 */
	public CentrosAtencionDao getCentrosAtencionDao() {
		return new PostgresqlCentrosAtencionDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>getCentrosCostoXUnidadConsultaDao</code>, usada por
	 * <code>getCentrosCostoXUnidadConsultaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>getCentrosCostoXUnidadConsultaDao</code>
	 */
	public CentrosCostoXUnidadConsultaDao getCentrosCostoXUnidadConsultaDao() {
		return new PostgresqlCentrosCostoXUnidadConsultaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultarCentrosCostoDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ConsultarCentrosCosto</code>
	 */
	public ConsultarCentrosCostoDao getConsultarCentrosCostoDao() {
		return new PostgresqlConsultarCentrosCostoDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CuentaUnidadFuncionalDao</code>, usada por
	 * <code>CuentaUnidadFuncionalDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>UnidadFuncionalDao</code>
	 */
	public CuentaUnidadFuncionalDao getCuentaUnidadFuncionalDao() {
		return new PostgresqlCuentaUnidadFuncionalDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CentrosCostoViaIngresoDao</code>, usada por
	 * <code>CentrosCostoViaIngresoDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>CentrosCostoViaIngresoDao</code>
	 */
	public CentrosCostoViaIngresoDao getCentrosCostoViaIngresoDao() {
		return new PostgresqlCentrosCostoViaIngresoDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>GruposEtareosDao</code>, usada por <code>GruposEtareosDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>GruposEtareosDao</code>
	 */
	public GruposEtareosDao getGruposEtareosDao() {
		return new PostgresqlGruposEtareosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>GruposEtareosDao</code>, usada por
	 * <code>ConsultaGruposEtareosDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ConsultaGruposEtareosDao</code>
	 */
	public ConsultaGruposEtareosDao getConsultaGruposEtareosDao() {
		return new PostgresqlConsultaGruposEtareosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ArticulosXMezclaDao</code>, usada por
	 * <code>ArticulosXMezclaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ArticulosXMezclaDao</code>
	 */
	public ArticulosXMezclaDao getArticulosXMezclaDao() {
		return new PostgresqlArticulosXMezclaDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>MezclasDao</code>, usada por <code>Mezcla</code>.
	 * 
	 * @return la implementacion en Potgresql de la interfaz
	 *         <code>MezclasDao</code>
	 */
	public MezclasDao getMezclasDao() {
		return new PostgresqlMezclasDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>SistemasMotivoConsultaDao</code>, usada por
	 * <code>SistemasMotivoConsultaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>SistemasMotivoConsultaDao</code>
	 */
	public SistemasMotivoConsultaDao getSistemasMotivoConsultaDao() {
		return new PostgresqlSistemasMotivoConsultaDao();
	}

	/**
	 * Retorna la implementacion en postgres de la interfaz
	 * <code>SignosSintomasXSistemaDao</code>, usada por
	 * <code>SignosSintomasXSistemaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>SignosSintomasXSistemaDao</code>
	 */
	public SignosSintomasXSistemaDao getSignosSintomasXSistemaDao() {
		return new PostgresqlSignosSintomasXSistemaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>DestinoTriageDao</code>, usada por <code>DestinoTriageDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>DestinoTriageDao</code>
	 */
	public DestinoTriageDao getDestinoTriageDao() {
		return new PostgresqlDestinoTriageDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>UnidadMedidaDao</code>, usada por <code>UnidadMedidaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>UnidadMedidaDao</code>
	 */
	public UnidadMedidaDao getUnidadMedidaDao() {
		return new PostgresqlUnidadMedidaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CategoriasTriageDao</code>, usada por
	 * <code>CategoriasTriageDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>CategoriasTriageDao</code>
	 */
	public CategoriasTriageDao getCategoriasTriageDao() {
		return new PostgresqlCategoriasTriageDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaPacientesTriageDao</code>, usada por
	 * <code>ConsultaPacientesTriageDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>ConsultaPacientesTriageDao</code>
	 */
	public ConsultaPacientesTriageDao getConsultaPacientesTriageDao() {
		return new PostgresqlConsultaPacientesTriageDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>PacientesTriageUrgenciasDao</code>, usada por
	 * <code>PacientesTriageUrgenciasDao</code>.
	 * 
	 * @return la implementacion en postgres de la interfaz
	 *         <code>PacientesTriageUrgenciasDao</code>
	 */
	public PacientesTriageUrgenciasDao getPacientesTriageUrgenciasDao() {
		return new PostgresqlPacientesTriageUrgenciasDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>SignosSintomasDao</code>, usada por <code>UnidadMedidaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>SignosSintomasDao</code>
	 */
	public SignosSintomasDao getSignosSintomasDao() {
		return new PostgresqlSignosSintomasDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>PacientesTriageUrgenciasDao</code>, usada por
	 * <code>PacientesUrgenciasPorValorarDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>PacientesUrgenciasPorValorarDao</code>
	 */
	public PacientesUrgenciasPorValorarDao getPacientesUrgenciasPorValorarDao() {
		return new PostgresqlPacientesUrgenciasPorValorarDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>InformacionPartoDao</code>, usada por
	 * <code>InformacionPartoDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>InformacionPartoDao</code>
	 */
	public InformacionPartoDao getInformacionPartoDao() {
		return new PostgresqlInformacionPartoDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>NivelServicioDao</code>, usada por <code>NivelServicioDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>NivelServicioDao</code>
	 */
	public NivelAtencionDao getNivelAtencionDao() {
		return new PostgresqlNivelAtencionDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CuentaCobroCapitacionDao</code>, usada por
	 * <code>CuentaCobroCapitacionDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>CuentaCobroCapitacionDao</code>
	 */
	public CuentaCobroCapitacionDao getCuentaCobroCapitacionDao() {
		return new PostgresqlCuentaCobroCapitacionDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>GeneracionInterfazDao</code>, usada por
	 * <code>GeneracionInterfazDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>GeneracionInterfazDao</code>
	 */
	public GeneracionInterfazDao getGeneracionInterfazDao() {
		return new PostgresqlGeneracionInterfazDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>RadicacionCuentasCobroCapitacionDao</code>, usada por
	 * <code>RadicacionCuentasCobroCapitacionDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>RadicacionCuentasCobroCapitacionDao</code>
	 */
	public RadicacionCuentasCobroCapitacionDao getRadicacionCuentasCobroCapitacionDao() {
		return new PostgresqlRadicacionCuentasCobroCapitacionDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CuentasProcesoFacturacionDao</code>, usada por
	 * <code>CuentasProcesoFacturacionDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>CuentasProcesoFacturacionDao</code>
	 */
	public CuentasProcesoFacturacionDao getCuentasProcesoFacturacionDao() {
		return new PostgresqlCuentasProcesoFacturacionDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>PacientesUrgenciasSalaEsperaDao</code>, usada por
	 * <code>PacientesUrgenciasSalaEsperaDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>PacientesUrgenciasSalaEsperaDao</code>
	 */
	public PacientesUrgenciasSalaEsperaDao getPacientesUrgenciasSalaEsperaDao() {
		return new PostgresqlPacientesUrgenciasSalaEsperaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>PacientesUrgenciasPorHospitalizarDao</code>, usada por
	 * <code>PacientesUrgenciasPorHospitalizarDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>PacientesUrgenciasPorHospitalizarDao</code>
	 */
	public PacientesUrgenciasPorHospitalizarDao getPacientesUrgenciasPorHospitalizarDao() {
		return new PostgresqlPacientesUrgenciasPorHospitalizarDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>PacientesUrgenciasPorHospitalizarDao</code>, usada por
	 * <code>PacientesUrgenciasPorHospitalizarDao</code>.
	 * 
	 * @return la implementacion en Postgres de la interfaz
	 *         <code>PacientesUrgenciasPorHospitalizarDao</code>
	 */
	public GrupoEtareoCrecimientoDesarrolloDao getGrupoEtareoCrecimientoDesarrolloDao() {
		return new PostgresqlGrupoEtareoCrecimientoDesarrolloDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ProgramasSaludPYPDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ProgramasSaludPYPDao</code>
	 */
	public ProgramasSaludPYPDao getProgramasSaludPYPDao() {
		return new PostgresqlProgramasSaludPYPDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ProgramasSaludPYPDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ProgramasSaludPYPDao</code>
	 */
	public AsociarCxCAFacturasDao getAsociarCxCAFacturasDao() {
		return new PostgresqlAsociarCxCAFacturasDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ActividadesPypDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ActividadesPypDao</code>
	 */
	public ActividadesPypDao getActividadesPypDao() {
		return new PostgresqlActividadesPypDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CierresCarteraCapitacionDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>CierresCarteraCapitacionDao</code>
	 */
	public CierresCarteraCapitacionDao getCierresCarteraCapitacionDao() {
		return new PostgresqlCierresCarteraCapitacionDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ProgramasActividadesConvenioDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return el DAO usado por <code>ProgramasActividadesConvenioDao</code>
	 */
	public ProgramasActividadesConvenioDao getProgramasActividadesConvenioDao() {
		return new PostgresqlProgramasActividadesConvenioDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ActividadesProgramasPYPDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ActividadesProgramasPYPDao</code>
	 */
	public ActividadesProgramasPYPDao getActividadesProgramasPYPDao() {
		return new PostgresqlActividadesProgramasPYPDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>MetasPYPDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>MetasPYPDao</code>
	 */
	public MetasPYPDao getMetasPYPDao() {
		return new PostgresqlMetasPYPDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RegistroSaldosInicialesCapitacionDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>RegistroSaldosInicialesCapitacionDao</code>
	 */
	public RegistroSaldosInicialesCapitacionDao getRegistroSaldosInicialesCapitacionDao() {
		return new PostgresqlRegistroSaldosInicialesCapitacionDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ProgramasPYPDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ProgramasPYPDao</code>
	 */
	public ProgramasPYPDao getProgramasPYPDao() {
		return new PostgresqlProgramasPYPDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>OrdenesAmbulatoriasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>OrdenesAmbulatoriasDao</code>
	 */
	public OrdenesAmbulatoriasDao getOrdenesAmbulatoriasDao() {
		return new PostgresqlOrdenesAmbulatoriasDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>TrasladoCentroAtencionDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>TrasladoCentroAtencionDao</code>
	 */
	public TrasladoCentroAtencionDao getTrasladoCentroAtencionDao() {
		return new PostgresqlTrasladoCentroAtencionDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ImpresionResumenAtencionesDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ImpresionResumenAtencionesDao</code>
	 */
	public ImpresionResumenAtencionesDao getImpresionResumenAtencionesDao() {
		return new PostgresqlImpresionResumenAtencionesDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>MotivosSircDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>MotivosSircDao</code>
	 */
	public MotivosSircDao getMotivosSircDao() {
		return new PostgresqlMotivosSircDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>InstitucionesSircDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>InstitucionesSircDao</code>
	 */
	public InstitucionesSircDao getInstitucionesSircDao() {
		return new PostgresqlInstitucionesSircDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ServiciosSircDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ServiciosSircDao</code>
	 */
	public ServiciosSircDao getServiciosSircDao() {
		return new PostgresqlServiciosSircDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ActEjecutadasXCargarDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ActEjecutadasXCargarDao</code>
	 */
	public ActEjecutadasXCargarDao getActEjecutadasXCargarDao() {
		return new PostgresqlActEjecutadasXCargarDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>IndicativoSolicitudGrupoServiciosDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>IndicativoSolicitudGrupoServiciosDao</code>
	 */
	public IndicativoSolicitudGrupoServiciosDao getIndicativoSolicitudGrupoServiciosDao() {
		return new PostgresqlIndicativoSolicitudGrupoServiciosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>IndicativoCargoViaIngresoServicioDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>IndicativoCargoViaIngresoServicioDao</code>
	 */
	public IndicativoCargoViaIngresoServicioDao getIndicativoCargoViaIngresoServicioDao() {
		return new PostgresqlIndicativoCargoViaIngresoServicioDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RespuestaProcedimientosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>RespuestaProcedimientosDao</code>
	 */
	public RespuestaProcedimientosDao getRespuestaProcedimientosDao() {
		return new PostgresqlRespuestaProcedimientosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>InterpretarProcedimientoDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>InterpretarProcedimientoDao</code>
	 */
	public InterpretarProcedimientoDao getInterpretarProcedimientoDao() {
		return new PostgresqlInterpretarProcedimientoDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>InformacionRecienNacidosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>InformacionRecienNacidosDao</code>
	 */

	public InformacionRecienNacidosDao getInformacionRecienNacidosDao() {
		return new PostgresqlInformacionRecienNacidosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ImpresionCLAPDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ImpresionCLAPDao</code>
	 */
	public ImpresionCLAPDao getImpresionCLAPDao() {
		return new PostgresqlImpresionCLAPDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>BusquedaDiagnosticosGenericaDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return el DAO usado por <code>BusquedaDiagnosticosGenericaDao</code>
	 */
	public BusquedaDiagnosticosGenericaDao getBusquedaDiagnosticosGenericaDao() {
		return new PostgresqlBusquedaDiagnosticosGenericaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>TrasladosCajaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TrasladosCajaDao</code>
	 */
	public TrasladosCajaDao getTrasladosCajaDao() {
		return new PostgresqlTrasladosCajaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaWSDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultaWSDao</code>
	 */
	public ConsultaWSDao getConsultaWSDao() {
		return new PostgresqlConsultaWSDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultoriosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultoriosDao</code>
	 */
	public ConsultoriosDao getConsultoriosDao() {
		return new PostgresqlConsultoriosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>TiposInventarioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiposInventarioDao</code>
	 */
	public TiposInventarioDao getTiposInventarioDao() {
		return new PostgresqlTiposInventarioDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>HojaGastosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>HojaGastosDao</code>
	 */
	public HojaGastosDao getHojaGastosDao() {
		return new PostgresqlHojaGastosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ArticulosFechaVencimientoDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ArticulosFechaVencimientoDao</code>
	 */
	public ArticulosFechaVencimientoDao getArticulosFechaVencimientoDao() {
		return new PostgresqlArticulosFechaVencimientoDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>FormaFarmaceuticaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>FormaFarmaceuticaDao</code>
	 */
	public FormaFarmaceuticaDao getFormaFarmaceuticaDao() {
		return new PostgresqlFormaFarmaceuticaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>PaquetesDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PaquetesDao</code>
	 */
	public PaquetesDao getPaquetesDao() {
		return new PostgresqlPaquetesDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>NaturalezaArticulosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>NaturalezaArticulosDao</code>
	 */
	public NaturalezaArticulosDao getNaturalezaArticulosDao() {
		return new PostgresqlNaturalezaArticulosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ViasIngresoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ViasIngresoDao</code>
	 */
	public ViasIngresoDao getViasIngresoDao() {
		return new PostgresqlViasIngresoDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>CierreIngresoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CierreIngresoDao</code>
	 */
	public CierreIngresoDao getCierreIngresoDao() {
		return new PostgresqlCierreIngresoDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaCierreAperturaIngresoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultaCierreAperturaIngresoDao</code>
	 */
	public ConsultaCierreAperturaIngresoDao getConsultaCierreAperturaIngresoDao() {
		return new PostgresqlConsultaCierreAperturaIngresoDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>PacientesHospitalizadosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PacientesHospitalizadosDao</code>
	 */
	public PacientesHospitalizadosDao getPacientesHospitalizadosDao() {
		return new PostgresqlPacientesHospitalizadosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>UbicacionGeograficaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>UbicacionGeograficaDao</code>
	 */
	public UbicacionGeograficaDao getUbicacionGeograficaDao() {
		return new PostgresqlUbicacionGeograficaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ArticuloCatalogoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ArticuloCatalogoDao</code>
	 */
	public ArticuloCatalogoDao getArticuloCatalogoDao() {
		return new PostgresqlArticuloCatalogoDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>DevolucionInventariosPacienteDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DevolucionInventariosPacienteDao</code>
	 */
	public DevolucionInventariosPacienteDao getDevolucionInventariosPacienteDao() {
		return new PostgresqlDevolucionInventariosPacienteDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultarAdmisionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultarAdmisionDao</code>
	 */
	public ConsultarAdmisionDao getConsultarAdmisionDao() {
		return new PostgresqlConsultarAdmisionDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>TiposMonedaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiposMonedaDao</code>
	 */
	public TiposMonedaDao getTiposMonedaDao() {
		return new PostgresqlTiposMonedaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ServiciosViaAccesoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ServiciosViaAccesoDao</code>
	 */
	public ServiciosViaAccesoDao getServiciosViaAccesoDao() {
		return new PostgresqlServiciosViaAccesoDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ParamArchivoPlanoColsanitasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ParamArchivoPlanoColsanitasDao</code>
	 */
	public ParamArchivoPlanoColsanitasDao getParamArchivoPlanoColsanitasDao() {
		return new PostgresqlParamArchivoPlanoColsanitasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>PaquetesConvenioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PaquetesConvenioDao</code>
	 */
	public PaquetesConvenioDao getPaquetesConvenioDao() {
		return new PostgresqlPaquetesConvenioDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>InclusionesExclusionesDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>InclusionesExclusionesDao</code>
	 */
	public InclusionesExclusionesDao getInclusionesExclusionesDao() {
		return new PostgresqlInclusionesExclusionesDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ServiciosGruposEsteticosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ServiciosGruposEsteticosDao</code>
	 */
	public ServiciosGruposEsteticosDao getServiciosGruposEsteticosDao() {
		return new PostgresqlServiciosGruposEsteticosDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>CoberturaDao</code>
	 */
	public CoberturaDao getCoberturaDao() {
		return new PostgresqlCoberturaDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>CuentaInvUnidadFunDao</code>
	 */
	public CuentaInvUnidadFunDao getCuentaInvUnidadFunDao() {
		return new PostgresqlCuentaInvUnidadFunDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaAjustesEmpresaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ConsultaAjustesEmpresaDao</code>
	 */
	public ConsultaAjustesEmpresaDao getConsultaAjustesEmpresaDao() {
		return new PostgresqlConsultaAjustesEmpresaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ExamenCondiTomaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ExamenCondiTomaDao</code>
	 */
	public ExamenCondiTomaDao getExamenCondiTomaDao() {
		return new PostgresqlExamenCondiTomaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>TiposConvenioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiposConvenioDao</code>
	 */
	public TiposConvenioDao getTiposConvenioDao() {
		return new PostgresqlTiposConvenioDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>UnidadesProcedimientoDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>UnidadesProcedimientoDao</code>
	 */
	public UnidadProcedimientoDao getUnidadProcedimientoDao() {
		return new PostgresqlUnidadProcedimientoDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>FosygaDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>FosygaDao</code>
	 */
	public FosygaDao getFosygaDao() {
		return new PostgresqlFosygaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>UtilidadesFacturacioniDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>UtilidadesFacturacionDao</code>
	 */
	public UtilidadesFacturacionDao getUtilidadesFacturacionDao() {
		return new PostgresqlUtilidadesFacturacionDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RegistroEventosCatastroficosDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return el DAO usado por <code>RegistroEventosCatastroficosDao</code>
	 */
	public RegistroEventosCatastroficosDao getRegistroEventosCatastroficosDao() {
		return new PostgresqlRegistroEventosCatastroficosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>GeneracionAnexosForecatDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>GeneracionAnexosForecatDao</code>
	 */
	public GeneracionAnexosForecatDao getGeneracionAnexosForecatDao() {
		return new PostgresqlGeneracionAnexosForecatDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>PisosDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PisosDao</code>
	 */
	public PisosDao getPisosDao() {
		return new PostgresqlPisosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ComponentesPaquetesDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ComponentesPaquetesDao</code>
	 */
	public ComponentesPaquetesDao getComponentesPaquetesDao() {
		return new PostgresqlComponentesPaquetesDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>TipoHabitacionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TipoHabitacionDao</code>
	 */
	public TipoHabitacionDao getTipoHabitacionDao() {
		return new PostgresqlTipoHabitacionDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>UtilidadesHistoriaClinicaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>UtilidadesHistoriaClinicaDao</code>
	 */
	public UtilidadesHistoriaClinicaDao getUtilidadesHistoriaClinicaDao() {
		return new PostgresqlUtilidadesHistoriaClinicaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>DetalleCoberturaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DetalleCoberturaDao</code>
	 */
	public DetalleCoberturaDao getDetalleCoberturaDao() {
		return new PostgresqlDetalleCoberturaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>AlmacenParametrosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AlmacenParametrosDao</code>
	 */
	public AlmacenParametrosDao getAlmacenParametrosDao() {
		return new PostgresqlAlmacenParametrosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>HabitacionesDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>HabitacionesDao</code>
	 */
	public HabitacionesDao getHabitacionesDao() {
		return new PostgresqlHabitacionesDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ReferenciaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ReferenciaDao</code>
	 */
	public ReferenciaDao getReferenciaDao() {
		return new PostgresqlReferenciaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ReportesEstadosCarteraDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ReportesEstadosCarteraDao</code>
	 */
	public ReportesEstadosCarteraDao getReportesEstadosCarteraDao() {
		return new PostgresqlReportesEstadosCarteraDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>CoberturasConvenioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CoberturasConvenioDao</code>
	 */
	public CoberturasConvenioDao getCoberturasConvenioDao() {
		return new PostgresqlCoberturasConvenioDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>TiposUsuarioCamaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiposUsuarioCamaDao</code>
	 */
	public TiposUsuarioCamaDao getTiposUsuarioCamaDao() {
		return new PostgresqlTiposUsuarioCamaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>EdadCarteraDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>EdadCarteraDao</code>
	 */
	public EdadCarteraDao getEdadCarteraDao() {
		return new PostgresqlEdadCarteraDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>DescuentosComercialesDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>DescuentosComercialesDao</code>
	 */
	public DescuentosComercialesDao getDescuentosComercialesDao() {
		return new PostgresqlDescuentosComercialesDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>EdadCarteraCapitacionDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>EdadCarteraCapitacionDao</code>
	 */
	public EdadCarteraCapitacionDao getEdadCarteraCapitacionDao() {
		return new PostgresqlEdadCarteraCapitacionDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>DetalleInclusionesExclusionesDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return el DAO usado por <code>DetalleInclusionesExclusionesDao</code>
	 */
	public DetalleInclusionesExclusionesDao getDetalleInclusionesExclusionesDao() {
		return new PostgresqlDetalleInclusionesExclusionesDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>TramiteReferenciaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TramiteReferenciaDao</code>
	 */
	public TramiteReferenciaDao getTramiteReferenciaDao() {
		return new PostgresqlTramiteReferenciaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>TiposAmbulanciaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiposAmbulanciaDao</code>
	 */
	public TiposAmbulanciaDao getTiposAmbulanciaDao() {
		return new PostgresqlTiposAmbulanciaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>InclusionExclusionConvenioDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>InclusionExclusionConvenioDao</code>
	 */
	public InclusionExclusionConvenioDao getInclusionExclusionConvenioDao() {
		return new PostgresqlInclusionExclusionConvenioDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ContrarreferenciaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ContrarreferenciaDao</code>
	 */
	public ContrarreferenciaDao getContrarreferenciaDao() {
		return new PostgresqlContrarreferenciaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ExcepcionesTarifas1Dao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ExcepcionesTarifas1Dao</code>
	 */
	public ExcepcionesTarifas1Dao getExcepcionesTarifas1Dao() {
		return new PostgresqlExcepcionesTarifas1Dao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>UtilidadesManejoPacienteDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>UtilidadesManejoPacienteDao</code>
	 */
	public UtilidadesManejoPacienteDao getUtilidadesManejoPacienteDao() {
		return new PostgresqlUtilidadesManejoPacienteDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ContrarreferenciaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ContrarreferenciaDao</code>
	 */
	public ConsultaReferenciaContrareferenciaDao getConsultaReferenciaContrareferenciaDao() {
		return new PostgresqlConsultaReferenciaContrareferenciaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>CargosDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CargosDao</code>
	 */
	public CargosDao getCargosDao() {
		return new PostgresqlCargosDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>BusquedaBarriosGenericaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>BusquedaBarriosGenericaDao</code>
	 */
	public BusquedaBarriosGenericaDao getBusquedaBarriosGenericaDao() {
		return new PostgresqlBusquedaBarriosGenericaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>PaquetizacionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PaquetizacionDao</code>
	 */
	public PaquetizacionDao getPaquetizacionDao() {
		return new PostgresqlPaquetizacionDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>DocumentosGarantiaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DocumentosGarantiaDao</code>
	 */
	public DocumentosGarantiaDao getDocumentosGarantiaDao() {
		return new PostgresqlDocumentosGarantiaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>DocumentosGarantiaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DocumentosGarantiaDao</code>
	 */
	public ApliPagosCarteraPacienteDao getApliPagosCarteraPacienteDao() {
		return new PostgresqlApliPagosCarteraPacienteDao();
	}

	/**
	 * Retorna la implementacion en postgresql de la interfaz
	 * <code>ConsentimeintoInformadoDao</code>
	 */
	public ConsentimientoInformadoDao getConsentimientoInformadoDao() {
		return new PostgresqlConsentimientoInformadoDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>DistribucionCuentaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DistribucionCuentaDao</code>
	 */
	public DistribucionCuentaDao getDistribucionCuentaDao() {
		return new PostgresqlDistribucionCuentaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>RevisionCuentaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>RevisionCuentaDao</code>
	 */
	public RevisionCuentaDao getRevisionCuentaDao()
	{
		return new PostgresqlRevisionCuentaDao();
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaTarifasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultaTarifasDao</code>
	 */
	public ConsultaTarifasDao getConsultaTarifasDao() {
		return new PostgresqlConsultaTarifasDao();
	}
	
	
	public CondicionesXServicioDao getCondicionesXServicioDao()
	{
		return new PostgresqlCondicionesXServiciosDao();	
	}
	
	public ReporteProcedimientosEsteticosDao getReporteProcedimientosEsteticosDao()
	{
		return new PostgresqlReporteProcedimientosEsteticosDao();	
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>CargosAutomaticosPresupuestoDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>CargosAutomaticosPresupuestoDao</code>
	 */
	public CargosAutomaticosPresupuestoDao getCargosAutomaticosPresupuestoDao()
	{
		return new PostgresqlCargosAutomaticosPresupuestoDao();	
	}
	
	/**
	 * Retorna la implementacion en postgresql de la interfaz
	 * <code>CensoCamasDao</code>
	 */
	public CensoCamasDao getCensoCamasDao() {
		return new PostgresqlCensoCamasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>CargosAutomaticosPresupuestoDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>CargosAutomaticosPresupuestoDao</code>
	 */
	public PendienteFacturarDao getPendienteFacturarDao()
	{
		return new PostgresqlPendienteFacturarDao();	
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ReliquidacionTarifasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ReliquidacionTarifasDao</code>
	 */
	public ReliquidacionTarifasDao getReliquidacionTarifasDao()
	{
		return new PostgresqlReliquidacionTarifasDao();	
	}

	
	/**
	 * Retorna el DAO con el cual el objeto <code>AnulacionCargosFarmaciaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>AnulacionCargosFarmaciaDao</code>
	 */
	public AnulacionCargosFarmaciaDao getAnulacionCargosFarmaciaDao()
	{
		return new PostgresqlAnulacionCargosFarmaciaDao();	
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultarActivarCamasReservadasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultarActivarCamasReservadasDao</code>
	 */
	public ConsultarActivarCamasReservadasDao getConsultarActivarCamasReservadasDao()
	{
		return new PostgresqlConsultarActivarCamasReservadasDao();	
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConceptosPagoPoolesDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConceptosPagoPoolesDao</code>
	 */
	public ConceptosPagoPoolesDao getConceptosPagoPoolesDao()
	{
		return new PostgresqlConceptosPagoPoolesDao();	
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>FactorConversionMonedasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>FactorConversionMonedasDao</code>
	 */
	public FactorConversionMonedasDao getFactorConversionMonedasDao()
	{
		return new PostgresqlFactorConversionMonedasDao();	
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConceptosPagoPoolesXConvenioDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConceptosPagoPoolesXConvenioDao</code>
	 */
	public ConceptosPagoPoolesXConvenioDao getConceptosPagoPoolesXConvenioDao()
	{
		return new PostgresqlConceptosPagoPoolesXConvenioDao();	
	}
	
		
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UtilidadesAdministracionDao</code> 
	 */
	public UtilidadesAdministracionDao getUtilidadesAdministracionDao()
	{
		return new PostgresqlUtilidadesAdministracionDao ();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UtilConversionMonedaDao</code> 
	 */
	public UtilConversionMonedasDao getUtilConversionMonedasDao()
	{
		return new PostgresqlUtilConversionMonedasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UtilidadesConsultaExternaDao</code> 
	 */
	public UtilidadesConsultaExternaDao getUtilidadesConsultaExternaDao()
	{
		return new PostgresqlUtilidadesConsultaExternaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 */
	public ReingresoSalidaHospiDiaDao getReingresoSalidaHospiDiaDao()
	{
		return new PostgresqlReingresoSalidaHospiDiaDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RegistroTerapiasGrupalesDao</code> 
	 */
	public RegistroTerapiasGrupalesDao getRegistroTerapiasGrupalesDao() 
	{
		return new PostgresqlRegistroTerapiasGrupalesDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ArchivoPlanoColsaDao</code> 
	 */
	public ArchivoPlanoColsaDao getArchivoPlanoColsaDao()
	{
		return new PostgresqlArchivoPlanoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaIngresosHospitalDiaDao</code> 
	 */
	public ConsultaIngresosHospitalDiaDao getConsultaIngresosHospitalDiaDao()
	{
		return new PostgresqlConsultaIngresosHospitalDiaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaTerapiasGrupalesDao</code> 
	 */
	public ConsultaTerapiasGrupalesDao getConsultaTerapiasGrupalesDao()
	{
		return new PostgresqlConsultaTerapiasGrupalesDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AsociosXRangoTiempoDao</code> 
	 */
	public AsociosXRangoTiempoDao getAsociosXRangoTiempoDao()
	{
		return new PostgresqlAsociosXRangoTiempoDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AsociosSalaCirugiaDao</code> 
	 */
	public AsocioSalaCirugiaDao getAsocioSalaCirugiaDao()
	{
		return new PostgresqlAsocioSalaCirugiaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>DespachoPedidoQxDao</code> 
	 */
	public DespachoPedidoQxDao getDespachoPedidoQxDao()
	{
		return new PostgresqlDespachoPedidoQxDao();
	}
	

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AsocioServicioTarifaDao</code> 
	 */
	public AsocioServicioTarifaDao getAsocioServicioTarifaDao()
	{
		return new PostgresqlAsocioServicioTarifaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>LecturaPlanosEntidadesDao</code> 
	 */
	public LecturaPlanosEntidadesDao getLecturaPlanosEntidadesDao()
	{
		return new PostgresqlLecturaPlanosEntidadesDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>PacientesEntidadesSubConDao</code> 
	 */
	public PacientesEntidadesSubConDao getPacientesEntidadesSubConDao()
	{
		return new PostgresqlPacientesEntidadesSubConDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>LecturaPlanosEntidadesDao</code> 
	 */
	public EntidadesSubContratadasDao getEntidadesSubContratadasDao()
	{
		return new PostgresqlEntidadesSubContratadasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ParametrosEntidadesSubContratadasDao</code> 
	 */
	public ParametrosEntidadesSubContratadasDao getParametrosEntidadesSubContratadasDao()
	{
		return new PostgresqlParametrosEntidadesSubContratadasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CargosDirectosDao</code> 
	 */
	public CargosDirectosDao getCargosDirectosDao()
	{
		return new PostgresqlCargosDirectosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>SeccionesDao</code> 
	 */
	public SeccionesDao getSeccionesDao()
	{
		return new PostgresqlSeccionesDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>MovimientosAlmacenesDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>MovimientosAlmacenesDao</code>
	 */
	public MovimientosAlmacenesDao getMovimientosAlmacenesDao() {
		return new PostgresqlMovimientosAlmacenesDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ArticulosConsumidosPacientesDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ArticulosConsumidosPacientesDao</code>
	 */
	public ArticulosConsumidosPacientesDao getArticulosConsumidosPacientesDao() {
		return new PostgresqlArticulosConsumidosPacientesDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>OcupacionDiariaCamasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>OcupacionDiariaCamasDao</code>
	 */
	public OcupacionDiariaCamasDao getOcupacionDiariaCamasDao() {
		return new PostgresqlOcupacionDiariaCamasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ListadoCamasHospitalizacionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ListadoCamasHospitalizacionDao</code>
	 */
	public ListadoCamasHospitalizacionDao getListadoCamasHospitalizacionDao() {
		return new PostgresqlListadoCamasHospitalizacionDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>TotalOcupacionCamasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TotalOcupacionCamasDao</code>
	 */
	public TotalOcupacionCamasDao getTotalOcupacionCamasDao() {
		return new PostgresqlTotalOcupacionCamasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ArticulosPorAlmacenDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>ArticulosPorAlmacenDao</code>
	 */
	public ArticulosPorAlmacenDao getArticulosPorAlmacenDao()
	{
		return new PostgresqlArticulosPorAlmacenDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>FarmaciaCentroCostoDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>FarmaciaCentroCostoDao</code>
	 */
	public FarmaciaCentroCostoDao getFarmaciaCentroCostoDao()
	{
		return new PostgresqlFarmaciaCentroCostoDao();		
	}
	
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConceptosFacturasVariasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConceptosFacturasVariasDao</code>
	 */
	public ConceptosFacturasVariasDao getConceptosFacturasVariasDao()
	{
		return new PostgresqlConceptosFacturasVariasDao();		
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>DeudoresDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>DeudoresDao</code>
	 */
	public DeudoresDao getDeudoresDao()
	{
		return new PostgresqlDeudoresDao();		
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConceptosFacturasVariasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConceptosFacturasVariasDao</code>
	 */
	public GenModFacturasVariasDao getGenModFacturasVariasDao()
	{
		return new PostgresqlGenModFacturasVariasDao();		
	}
	
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaFacturasVariasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaFacturasVariasDao</code>
	 */
	public ConsultaFacturasVariasDao getConsultaFacturasVariasDao()
	{
		return new PostgresqlConsultaFacturasVariasDao();		
	}
	
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>BusquedaTercerosGenericaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>BusquedaTercerosGenericaDao</code>
	 */
	public BusquedaTercerosGenericaDao getBusquedaTercerosGenericaDao()
	{
		return new PostgresqlBusquedaTercerosGenericaDao();		
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConceptosFacturasVariasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConceptosFacturasVariasDao</code>
	 */
	public PreparacionTomaInventarioDao getPreparacionTomaInventarioDao()
	{
		return new PostgresqlPreparacionTomaInventarioDao();		
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>Imprimir lista conteo</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ImpListaConteoDao</code>
	 */
	public ImpListaConteoDao getImpListaConteoDao() {
		
		return new PostgresqlImpListaConteoDao();
	
	}
	
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>LiquidacionServiciosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>LiquidacionServiciosDao</code>
	 */
	public LiquidacionServiciosDao getLiquidacionServiciosDao()
	{
		return new PostgresqlLiquidacionServiciosDao();		
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadesSalasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>UtilidadesSalasDao</code>
	 */
	public UtilidadesSalasDao getUtilidadesSalasDao()
	{
		return new PostgresqlUtilidadesSalasDao();		
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadesSalasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>UtilidadesSalasDao</code>
	 */
	public SignosVitalesDao getSignosVitalesDao()
	{
		return new PostgresqlSignosVitalesDao();		
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>MotivoCierreAperturaIngresos</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>MotivoCierreAperturaIngresos</code>
	 */
	public MotivoCierreAperturaIngresosDao getMotivoCierreAperturaIngresosDao()
	{
		return new PostgresqlMotivoCierreAperturaIngresosDao();		
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>MotivoCierreAperturaIngresos</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>MotivoCierreAperturaIngresos</code>
	 */
	public SustitutosNoPosDao getSustitutosNoPosDao()
	{
		return new PostgresqlSustitutosNoPosDao();		
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>RegistroConteoInventarioDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>RegistroConteoInventarioDao</code>
	 */
	public RegistroConteoInventarioDao getRegistroConteoInventarioDao()
	{
		return new PostgresqlRegistroConteoInventarioDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ComparativoUltimoConteoDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>ComparativoUltimoConteoDao</code>
	 */
	public ComparativoUltimoConteoDao getComparativoUltimoConteoDao()
	{
		return new PostgresqlComparativoUltimoConteoDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>InterfazSistemaUnoDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>InterfazSistemaUnoDao</code>
	 */
	public InterfazSistemaUnoDao getInterfazSistemaUnoDao()
	{
		return new PostgresqlInterfazSistemaUnoDao();
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaInventarioFisicoArticulosDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaInventarioFisicoArticulosDao</code>
	 */
	public ConsultaInventarioFisicoArticulosDao getConsultaInventarioFisicoArticulosDao()
	{
		return new PostgresqlConsultaInventarioFisicoArticulosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>AjustesXInventarioFisicoDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>AjustesXInventarioFisicoDao</code>
	 */
	public AjustesXInventarioFisicoDao getAjustesXInventarioFisicoDao()
	{
		return new PostgresqlAjustesXInventarioFisicoDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsumosCentrosCosto</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsumosCentrosCosto</code>
	 */
	public ConsumosCentrosCostoDao getConsumosCentrosCostoDao()
	{
		return new PostgresqlConsumosCentrosCostoDao();		
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>EventosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>Eventos</code>
	 */
	public EventosDao getEventosDao()
	{
		return new PostgresqlEventosDao();		
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaDevolucionPedidos</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaDevolucionPedidos</code>
	 */
	public ConsultaDevolucionPedidosDao getConsultaDevolucionPedidosDao()
	{
		return new PostgresqlConsultaDevolucionPedidosDao();		
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>EventosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>Eventos</code>
	 */
	public TiemposHojaAnestesiaDao getTiemposHojaAnestesiaDao()
	{
		return new PostgresqlTiemposHojaAnestesiaDao();		
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>GasesHojaAnestesiaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>GasesHojaAnestesiaDao</code>
	 */
	public GasesHojaAnestesiaDao getGasesHojaAnestesiaDao()
	{
		return new PostgresqlGasesHojaAnestesiaDao();		
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaDevolucionInventarioPaciente</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaDevolucionInventarioPaciente</code>
	 */
	public ConsultaDevolucionInventarioPacienteDao getConsultaDevolucionInventarioPacienteDao()
	{
		return new PostgresqlConsultaDevolucionInventarioPacienteDao();		
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaDevolucionInventarioPaciente</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaDevolucionInventarioPaciente</code>
	 */
	public MonitoreoHemodinamicaDao getMonitoreoHemodinamicaDao()
	{
		return new PostgresqlMonitoreoHemodinamicaDao();		
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>TecnicaAnestesiaDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>TecnicaAnestesiaDao</code>
	 */
	public TecnicaAnestesiaDao getTecnicaAnestesiaDao()
	{
		return new PostgresqlTecnicaAnestesiaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>PosicionesAnestesiaDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>PosicionesAnestesiaDao</code>
	 */
	public PosicionesAnestesiaDao getPosicionesAnestesiaDao()
	{
		return new PostgresqlPosicionesAnestesiaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>InfoGeneralHADao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>InfoGeneralHADao</code>
	 */
	public InfoGeneralHADao getInfoGeneralHADao()
	{
		return new PostgresqlInfoGeneralHADao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>TiposTratamientosOdontologicosDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>TiposTratamientosOdontologicosDao</code>
	 */
	public TiposTratamientosOdontologicosDao getTiposTratamientosOdontologicosDao()
	{
		return new PostgresqlTiposTratamientosOdontologicosDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ViasAereasDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>ViasAereasDao</code>
	 */
	public ViasAereasDao getViasAereasDao()
	{
		return new PostgresqlViasAereasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>CargosDirectosCxDytDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>CargosDirectosCxDytDao</code>
	 */
	public CargosDirectosCxDytDao getCargosDirectosCxDytDao()
	{
		return new PostgresqlCargosDirectosCxDytDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>FormatoJustArtNoposDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>FormatoJustArtNoposDao</code>
	 */
	public FormatoJustArtNoposDao getFormatoJustArtNoposDao()
	{
		return new PostgresqlFormatoJustArtNoposDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>FormatoJustServNoposDao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>FormatoJustServNoposDao</code>
	 */
	public FormatoJustServNoposDao getFormatoJustServNoposDao()
	{
		return new PostgresqlFormatoJustServNoposDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>InfusionesHADao</code> interactua 
	 * con la fuente de datos.
	 * @return el DAO usado por <code>InfusionesHADao</code>
	 */
	public IntubacionesHADao getInfusionesHADao()
	{
		return new PostgresqlIntubacionesHADao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AprobacionAnulacionDevolucionesDao</code> 
	 */
	public AprobacionAnulacionDevolucionesDao getAprobacionAnulacionDevolucionesDao()
	{
		return new PostgresqlAprobacionAnulacionDevolucionesDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AprobacionAnulacionFacturasVariasDao</code> 
	 */
	public AprobacionAnulacionFacturasVariasDao getAprobacionAnulacionFacturasVariasDao()
	{
		return new PostgresqlAprobacionAnulacionFacturasVariasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>PagosFacturasVariasDao</code> 
	 */
	public PagosFacturasVariasDao getPagosFacturasVariasDao()
	{
		return new PostgresqlPagosFacturasVariasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AprobacionAnulacionPagosFacturasVariasDao</code> 
	 */
	public AprobacionAnulacionPagosFacturasVariasDao getAprobacionAnulacionPagosFacturasVariasDao()
	{
		return new PostgresqlAprobacionAnulacionPagosFacturasVariasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AprobacionAnulacionPagosFacturasVariasDao</code> 
	 */
	public ConsultaImpresionPagosFacturasVariasDao getConsultaImpresionPagosFacturasVariasDao()
	{
		return new PostgresqlConsultaImpresionPagosFacturasVariasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>RegistroDevolucionRecibosCajaDao</code> 
	 */
	public RegistroDevolucionRecibosCajaDao getRegistroDevolucionRecibosCajaDao()
	{
		return new PostgresqlRegistroDevolucionRecibosCajaDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaImpresionDevolucionDao</code> 
	 */
	public ConsultaImpresionDevolucionDao getConsultaImpresionDevolucionDao()
	{
		return new PostgresqlConsultaImpresionDevolucionDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>MotivosDevolucionRecibosCajaDao</code> 
	 */
	public MotivosDevolucionRecibosCajaDao getMotivosDevolucionRecibosCajaDao()
	{
		return new PostgresqlMotivosDevolucionRecibosCajaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>RegistroRipsCargosDirectosDao</code> 
	 */
	public RegistroRipsCargosDirectosDao getRegistroRipsCargosDirectosDao()
	{
		return new PostgresqlRegistroRipsCargosDirectosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CopiarTarifasEsquemaTarifarioDao</code> 
	 */
	public CopiarTarifasEsquemaTarifarioDao getCopiarTarifasEsquemaTarifarioDao()
	{
		return new PostgresqlCopiarTarifasEsquemaTarifarioDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaTarifasArticulosDao</code> 
	 */
	public ConsultaTarifasArticulosDao getConsultaTarifasArticulosDao()
	{
		return new PostgresqlConsultaTarifasArticulosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ActualizacionAutomaticaEsquemaTarifarioDao</code> 
	 */
	public ActualizacionAutomaticaEsquemaTarifarioDao getActualizacionAutomaticaEsquemaTarifarioDao()
	{
		return new PostgresqlActualizacionAutomaticaEsquemaTarifarioDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadJustificacionPendienteArtServDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>UtilidadJustificacionPendienteArtServDao</code>
	 */
	public UtilidadJustificacionPendienteArtServDao getUtilidadJustificacionPendienteArtServDao()
	{
		return new PostgresqlUtilidadJustificacionPendienteArtServDao();		
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>AccesosVascularesHADao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>AccesosVascularesHADao</code>
	 */
	public AccesosVascularesHADao getAccesosVascularesHADao()
	{
		return new PostgresqlAccesosVascularesHADao();		
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>EscalasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>EscalasDao</code>
	 */
	public EscalasDao getEscalasDao()
	{
		return new PostgresqlEscalasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>GeneracionModificacionAjustesFacturasVariasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>GeneracionModificacionAjustesFacturasVariasDao</code>
	 */
	public GeneracionModificacionAjustesFacturasVariasDao getGeneracionModificacionAjustesFacturasVariasDao()
	{
		return new PostgresqlGeneracionModificacionAjustesFacturasVariasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>JustificacionNoPosServDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>JustificacionNoPosServDao</code>
	 */
	public JustificacionNoPosServDao getJustificacionNoPosServDao()
	{
		return new PostgresqlJustificacionNoPosServDao(); 
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ValoracionesDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ValoracionesDao</code>
	 */
	public ValoracionesDao getValoracionesDao()
	{
		return new PostgresqlValoracionesDao(); 
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ParametrizacionComponentesDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ParametrizacionComponentesDao</code>
	 */
	public ParametrizacionComponentesDao getParametrizacionComponentesDao()
	{
		return new PostgresqlParametrizacionComponentesDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>MotivosSatisfaccionInsatisfaccionDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>MotivosSatisfaccionInsatisfaccionDao</code>
	 */
	public MotivosSatisfaccionInsatisfaccionDao getMotivosSatisfaccionInsatisfaccionDao()
	{
		return new  PostgresqlMotivosSatisfaccionInsatisfaccionDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>PlantillasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>PlantillasDao</code>
	 */
	public PlantillasDao getPlantillasDao()
	{
		return new PostgresqlPlantillasDao();
	}	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>InterfazSistemaUnoDao</code> 
	 */
	public RecaudoCarteraEventoDao getRecaudoCarteraEventoDao()
	{
		return new PostgresqlRecaudoCarteraEventoDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EncuestaCalidadAtencionDao</code> 
	 */
	public EncuestaCalidadAtencionDao getEncuestaCalidadAtencionDao() {
		return new PostgresqlEncuestaCalidadAtencionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RegistroEventosAdversosDao</code> 
	 */
	public RegistroEventosAdversosDao getRegistroEventosAdversosDao() {
		return new PostgresqlRegistroEventosAdversosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ParamArchivoPlanoIndCalidadDao</code> 
	 */
	public ParamArchivoPlanoIndCalidadDao getParamArchivoPlanoIndCalidadDao() {
		return new PostgresqlParamArchivoPlanoIndCalidadDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>EventosAdversosDao</code> 
	 */
	public EventosAdversosDao getEventosAdversosDao() {
		return new PostgresqlEventosAdversosDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>GeneracionArchivoPlanoIndicadoresCalidadDao</code> 
	 */
	public GeneracionArchivoPlanoIndicadoresCalidadDao getGeneracionArchivoPlanoIndicadoresCalidadDao() {
		return new PostgresqlGeneracionArchivoPlanoIndicadoresCalidadDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ParametrizacionPlantillasDao</code> 
	 */
	public ParametrizacionPlantillasDao getParametrizacionPlantillasDao()
	{
		return new PostgresqlParametrizacionPlantillasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>UnidadAgendaUsuarioCentroDao</code> 
	 */
	public UnidadAgendaUsuarioCentroDao getUnidadAgendaUsuarioCentroDao() {
		return new PostgresqlUnidadAgendaUsuarioCentroDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RegistroResumenParcialHistoriaClinicaDao</code> 
	 */
	public RegistroResumenParcialHistoriaClinicaDao getRegistroResumenParcialHistoriaClinicaDao() {
		return new PostgresqlRegistroResumenParcialHistoriaClinicaDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>AperturaIngresosDao</code> 
	 */
	public AperturaIngresosDao getAperturaIngresosDao() {
		return new PostgresqlAperturaIngresosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ConsultaPreingresosDao</code> 
	 */
	public ConsultaPreingresosDao getConsultaPreingresosDao() {
		return new PostgresqlConsultaPreingresosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>MotivosCancelacionCitaDao</code> 
	 */
	public MotivosCancelacionCitaDao getMotivosCancelacionCitaDao() {
		return new PostgresqlMotivosCancelacionCitaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ConsumosFacturadosDao</code> 
	 */
	public ConsumosFacturadosDao getConsumosFacturadosDao() {
		return new PostgresqlConsumosFacturadosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>LiberacionCamasIngresosCerradosDao</code> 
	 */
	public LiberacionCamasIngresosCerradosDao getLiberacionCamasIngresosCerradosDao() {
		return new PostgresqlLiberacionCamasIngresosCerradosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>OracleValoracionPacientesCuidadosEspecialesDao</code> 
	 */
	public ValoracionPacientesCuidadosEspecialesDao getValoracionPacientesCuidadosEspecialesDao() {
		return new PostgresqlValoracionPacientesCuidadosEspecialesDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>Epicrisis1Dao</code> 
	 */
	public Epicrisis1Dao getEpicrisis1Dao() {
		return new PostgresqlEpicrisis1Dao();
	}
	
		/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>AsignarCitasControlPostOperatorioDao</code> 
	 */
	public AsignarCitasControlPostOperatorioDao getAsignarCitasControlPostOperatorioDao() {
		return new PostgresqlAsignarCitasControlPostOperatorioDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlAsignacionCamaCuidadoEspecialDao</code> 
	 */
	public AsignacionCamaCuidadoEspecialAPisoDao getAsignacionCamaCuidadoEspecialAPisoDao() {
		return new PostgresqlAsignacionCamaCuidadoEspecialAPisoDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlAsignacionCamaCuidadoEspecialDao</code> 
	 */
	public AsignacionCamaCuidadoEspecialDao getAsignacionCamaCuidadoEspecialDao() {
		return new PostgresqlAsignacionCamaCuidadoEspecialDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlPacientesConAtencionDao</code> 
	 */
	public PacientesConAtencionDao getPacientesConAtencionDao() {
		return new PostgresqlPacientesConAtencionDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlConsultaProgramacionCirugiasDao</code> 
	 */
	public ConsultaProgramacionCirugiasDao getConsultaProgramacionCirugiasDao() {
		return new PostgresqlConsultaProgramacionCirugiasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlUtilidadesOrdenesMedicasDao</code> 
	 */
	public UtilidadesOrdenesMedicasDao getUtilidadesOrdenesMedicasDao()
	{
		return new PostgresqlUtilidadesOrdenesMedicasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlConsultaImpresionMaterialesQxDao</code> 
	 */
	public ConsultaImpresionMaterialesQxDao getConsultaImpresionMaterialesQxDao()
	{
		return new PostgresqlConsultaImpresionMaterialesQxDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlDevolucionPedidoQxDao</code> 
	 */
	public DevolucionPedidoQxDao getDevolucionPedidoQxDao()
	{
		return new PostgresqlDevolucionPedidoQxDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlTrasladoSolicitudesPorTransplantesDao</code> 
	 */
	public TrasladoSolicitudesPorTransplantesDao getTrasladoSolicitudesPorTransplantesDao()
	{
		return new PostgresqlTrasladoSolicitudesPorTransplantesDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlConsultarIngresosPorTransplantesDao</code> 
	 */
	public ConsultarIngresosPorTransplantesDao getConsultarIngresosPorTransplantesDao()
	{
		return new PostgresqlConsultarIngresosPorTransplantesDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlCostoInventarioPorFacturarDao</code> 
	 */
	public CostoInventarioPorFacturarDao getCostoInventarioPorFacturarDao()
	{
		return new PostgresqlCostoInventarioPorFacturarDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlEvolucionesDao</code> 
	 */
	public EvolucionesDao getEvolucionesDao()
	{
		return new PostgresqlEvolucionesDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlCostoInventarioPorFacturarDao</code> 
	 */
	public CostoVentasPorInventarioDao getCostoVentasPorInventarioDao()
	{
		return new PostgresqlCostoVentasPorInventarioDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlCostoInventariosPorAlmacenDao</code> 
	 */
	public CostoInventariosPorAlmacenDao getCostoInventariosPorAlmacenDao()
	{
		return new PostgresqlCostoInventariosPorAlmacenDao();
	}	
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlConsolidadoFacturacionDao</code> 
	 */
	public ConsolidadoFacturacionDao getConsolidadoFacturacionDao()
	{
		return new PostgresqlConsolidadoFacturacionDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlFacturadosPorConvenioDao</code> 
	 */
	public FacturadosPorConvenioDao getFacturadosPorConvenioDao()
	{
		return new PostgresqlFacturadosPorConvenioDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlPacientesPorFacturarDao</code> 
	 */
	public PacientesPorFacturarDao getPacientesPorFacturarDao()
	{
		return new PostgresqlPacientesPorFacturarDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlPacientesPorFacturarDao</code> 
	 */
	public ServiciosXTipoTratamientoOdontologicoDao  getServiciosXTipoTratamientoOdontologicoDao()
	{
		return new PostgresqlServiciosXTipoTratamientoOdontologicoDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlDiagnosticosOdontologicosATratarDao</code> 
	 */
	public DiagnosticosOdontologicosATratarDao  getDiagnosticosOdontologicosATratarDao()
	{
		return new PostgresqlDiagnosticosOdontologicosATratarDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlConsultaPaquetesFacturadosDao</code> 
	 */
	public ConsultaPaquetesFacturadosDao getConsultaPaquetesFacturadosDao()
	{
		return new PostgresqlConsultaPaquetesFacturadosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlValoresTipoReporteDao</code> 
	 */
	public ValoresTipoReporteDao getValoresTipoReporteDao()
	{
		return new PostgresqlValoresTipoReporteDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlConsultaMovimientoDeudorDao</code> 
	 */
	public ConsultaMovimientoDeudorDao getConsultaMovimientoDeudorDao()
	{
		return new PostgresqlConsultaMovimientoDeudorDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlConsultaMovimientoDeudorDao</code> 
	 */
	public EstadisticasServiciosDao getEstadisticasServiciosDao()
	{
		return new PostgresqlEstadisticasServiciosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlConsultaMovimientoDeudorDao</code> 
	 */
	public CalidadAtencionDao getCalidadAtencionDao()
	{
		return new PostgresqlCalidadAtencionDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlUsuariosAutorizarAnulacionFacturasDao</code> 
	 */
	public UsuariosAutorizarAnulacionFacturasDao getUsuariosAutorizarAnulacionFacturasDao()
	{
		return new PostgresqlUsuariosAutorizarAnulacionFacturasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlConsultaMovimientoFacturaDao</code> 
	 */
	public ConsultaMovimientoFacturaDao getConsultaMovimientoFacturaDao()
	{
		return new PostgresqlConsultaMovimientoFacturaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlReporteMortalidadDao</code> 
	 */
	public ReporteMortalidadDao getReporteMortalidadDao()
	{
		return new PostgresqlReporteMortalidadDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlSolicitarAnulacionFacturasDao</code> 
	 */
	public SolicitarAnulacionFacturasDao getSolicitarAnulacionFacturasDao()
	{
		return new PostgresqlSolicitarAnulacionFacturasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>PostgresqlConsultaSaldosCierresInventariosDao</code> 
	 */
	public ConsultaSaldosCierresInventariosDao getConsultaSaldosCierresInventariosDao()
	{
		return new PostgresqlConsultaSaldosCierresInventariosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ReporteEstadisticoConsultaExDao</code> 
	 */
	public ReporteEstadisticoConsultaExDao getReporteEstadisticoConsultaExDao()
	{ 
		return new PostgresqlReporteEstadisticoConsultaExDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ConsultaCostoArticulosDao</code> 
	 */
	public ConsultaCostoArticulosDao getConsultaCostoArticulosDao()
	{ 
		return new PostgresqlConsultaCostoArticulosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>AutorizarAnulacionFacturasDao</code> 
	 */
	public AutorizarAnulacionFacturasDao getAutorizarAnulacionFacturasDao()
	{
		return new PostgresqlAutorizarAnulacionFacturasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>AprobacionAnulacionAjustesFacturasVariasDao</code> 
	 */
	public AprobacionAnulacionAjustesFacturasVariasDao getAprobacionAnulacionAjustesFacturasVariasDao()
	{
		return new PostgresqlAprobacionAnulacionAjustesFacturasVariasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ConceptosGeneralesDao</code> 
	 */
	public ConceptosGeneralesDao getConceptosGeneralesDao()
	{
		return new PostgresqlConceptosGenerealesDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ConsultaImpresionAjustesFacturasVariasDao</code> 
	 */
	public ConsultaImpresionAjustesFacturasVariasDao getConsultaImpresionAjustesFacturasVariasDao()
	{
		return new PostgresqlConsultaImpresionAjustesFacturasVariasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ReporteEgresosEstanciasDao</code> 
	 */
	public ReporteEgresosEstanciasDao getReporteEgresosEstanciasDao()
	{
		return new PostgresqlReporteEgresosEstanciasDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ConceptosEspecificosDao</code> 
	 */
	public ConceptosEspecificosDao  getConceptosEspecificosDao()
	{
		return new PostgresqlConceptosEspecificosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>VentasCentroCostoDao</code> 
	 */
	public VentasCentroCostoDao getVentasCentroCostoDao()
	{
		return new PostgresqlVentasCentroCostoDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>VentasEmpresaConvenioDao</code> 
	 */
	public VentasEmpresaConvenioDao getVentasEmpresaConvenioDao()
	{
		return new PostgresqlVentasEmpresaConvenioDao();
	}
	

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ConsumosPorFacturarPacientesHospitalizadosDao</code> 
	 */
	public ConsumosPorFacturarPacientesHospitalizadosDao getConsumosPorFacturarPacientesHospitalizadosDao()
	{
		return new PostgresqlConsumosPorFacturarPacientesHospitalizadosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ReporteReferenciaInternaDao</code> 
	 */
	public ReporteReferenciaInternaDao getReporteReferenciaInternaDao()
	{
		return new PostgresqlReporteReferenciaInternaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>EstadisticasIngresosDao</code> 
	 */
	public EstadisticasIngresosDao getEstadisticasIngresosDao()
	{
		return new PostgresqlEstadisticasIngresosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>Servicios_ArticulosIncluidosEnOtrosProcedimientosDao</code> 
	 */
	public Servicios_ArticulosIncluidosEnOtrosProcedimientosDao getServicios_ArticulosIncluidosEnOtrosProcedimientosDao()
	{
		return new PostgresqlServicios_ArticulosIncluidosEnOtrosProcedimientosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ReporteReferenciaInternaDao</code> 
	 */
	public ReporteReferenciaExternaDao getReporteReferenciaExternaDao()
	{
		return new PostgresqlReporteReferenciaExternaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>GrupoEspecialArticulosDao</code> 
	 */
	public GrupoEspecialArticulosDao getGrupoEspecialArticulosDao()
	{
		return new PostgresqlGrupoEspecialArticulosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>TextosRespuestaProcedimientosDao</code> 
	 */
	public TextosRespuestaProcedimientosDao getTextosRespuestaProcedimientosDao()
	{
		return new PostgresqlTextosRespuestaProcedimientosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>SolicitudMedicamentosXDespacharDao</code> 
	 */
	public SolicitudMedicamentosXDespacharDao getSolicitudMedicamentosXDespacharDao()
	{
		return new PostgresqlSolicitudMedicamentosXDespacharDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>SolicitudMedicamentosXDespacharDao</code> 
	 */
	public MedicamentosControladosAdministradosDao getMedicamentosControladosAdministradosDao()
	{
		return new PostgresqlMedicamentosControladosAdministradosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>TotalFacturadoConvenioContratoDao</code> 
	 */
	public TotalFacturadoConvenioContratoDao getTotalFacturadoConvenioContratoDao()
	{
		return new PostgresqlTotalFacturadoConvenioContratoDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ConsumosXFacturarDao</code> 
	 */
	public ConsumosXFacturarDao getConsumosXFacturarDao()
	{
		return new PostgresqlConsumosXFacturarDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ConsultaMovimientosConsignacionesDao</code> 
	 */
	public ConsultaMovimientosConsignacionesDao getConsultaMovimientosConsignacionesDao()
	{
		return new PostgresqlConsultaMovimientosConsignacionesDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>MovimientosAlmacenConsignacionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>MovimientosAlmacenConsignacionDao</code>
	 */
	public MovimientosAlmacenConsignacionDao getMovimientosAlmacenConsignacionDao() {
		return new PostgresqlMovimientosAlmacenConsignacionDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ConsumosXFacturarDao</code> 
	 */
	public PlanosFURIPSDao getPlanosFURIPSDao()
	{
		return new PostgresqlPlanosFURIPSDao();
	}
	
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ParametrosFirmasImpresionRespDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ParametrosFirmasImpresionRespDao</code>
	 */
	public ParametrosFirmasImpresionRespDao getParametrosFirmasImpresionRespDao()
	{
		return new PostgresqlParametrosFirmasImpresionRespDao();
	}
	
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ConceptosRespuestasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConceptosRespuestasDao</code>
	 */
	public ConceptosRespuestasDao getConceptosRespuestasDao()
	{
		return new PostgresqlConceptosRespuestasDao();
	}
	
	/**
	 *Retorna el DAO con el cual el objeto <code>ConsultasBirtDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultasBirtDao</code>
	 */
	public ConsultasBirtDao getConsultasBirtDao()
	{
		return new PostgresqlConsultasBirtDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>GlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>GlosasDao</code>
	 */
	public GlosasDao getGlosasDao()
	{
		return new PostgresqlGlosasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>GlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>GlosasDao</code>
	 */
	public ProgramacionAreaPacienteDao getProgramacionAreaPacienteDao()
	{
		return new PostgresqlProgramacionAreaPacienteDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ProgramacionCuidadoEnferDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ProgramacionCuidadoEnferDao</code>
	 */
	public ProgramacionCuidadoEnferDao getProgramacionCuidadoEnferDao()
	{
		return new PostgresqlProgramacionCuidadoEnferDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConfirmarAnularGlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConfirmarAnularGlosasDao</code>
	 */
	public ConfirmarAnularGlosasDao getConfirmarAnularGlosasDao()
	{
		return new PostgresqlConfirmarAnularGlosasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>BusquedaGlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>BusquedaGlosasDao</code>
	 */
	public BusquedaGlosasDao getBusquedaGlosasDao()
	{
		return new PostgresqlBusquedaGlosasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultarImprimirFacturasAuditadasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultarImprimirFacturasAuditadasDao</code>
	 */
	public ConsultarImpFacAudiDao getConsultarImpFacAudiDao()
	{
		return new PostgresqlConsultarImpFacAudiDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>SoportesFacturasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>SoportesFacturasDao</code>
	 */
	public SoportesFacturasDao getSoportesFacturasDao()
	{
		return new PostgresqlSoportesFacturasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>AprobarGlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>AprobarGlosasDao</code>
	 */
	public AprobarGlosasDao getAprobarGlosasDao()
	{
		return new PostgresqlAprobarGlosasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>RegistrarModificarGlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>RegistrarModificarGlosasDao</code>
	 */
	public RegistrarModificarGlosasDao getRegistrarModificarGlosasDao()
	{
		return new PostgresqlRegistrarModificarGlosasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>AprobarGlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>AprobarGlosasDao</code>
	 */
	public ImpresionSoportesFacturasDao getImpresionSoportesFacturasDao()
	{
		return new PostgresqlImpresionSoportesFacturasDao();
	}
	
	
	/**
	 * Retonrna el DAO con el cual el objeto <code>ConsultaProgramacionCuidadosAreaDao</code>
	 * interactua cona la fuente de datos.
	 * @return el DAO usado por <code>ConsultaProgramacionCuidadosAreaDao</code>
	 */
	public ConsultaProgramacionCuidadosAreaDao getConsultaProgramacionCuidadosAreaDao(){
		return new PostgresqlConsultaProgramacionCuidadosAreaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaProgramacionCuidadosPacienteDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultaProgramacionCuidadosPacienteDao</code>
	 */
	public ConsultaProgramacionCuidadosPacienteDao getConsultaProgramacionCuidadosPacienteDao(){
		return new PostgresqlConsultaProgramacionCuidadosPacienteDao();
	}
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>RegistrarRespuestaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>RegistrarRespuestaDao</code>
	 */
	public RegistrarRespuestaDao getRegistrarRespuestaDao()
	{
		return new PostgresqlRegistrarRespuestaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>RegistrarRespuestaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>RegistrarRespuestaDao</code>
	 */
	public ConsultarImprimirGlosasDao getConsultarImprimirGlosasDao()
	{
		return new PostgresqlConsultarImprimirGlosasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>BusquedaRespuestaGlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>OracleBusquedaGlosasDao</code>
	 */
	public BusquedaRespuestaGlosasDao getBusquedaRespuestaGlosasDao()
	{
		return new PostgresqlBusquedaRespuestaGlosasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>AprobarAnularRespuestasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>OracleAprobarAnularRespuestasDao</code>
	 */
	public AprobarAnularRespuestasDao getAprobarAnularRespuestasDao()
	{
		return new PostgresqlAprobarAnularRespuestasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>RadicarRespuestasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>OracleRadicarRespuestasDao</code>
	 */
	public RadicarRespuestasDao getRadicarRespuestasDao()
	{
		return new PostgresqlRadicarRespuestasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>DetalleGlosasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>OracleDetalleGlosasDao</code>
	 */
	public DetalleGlosasDao getDetalleGlosasDao()
	{
		return new PostgresqlDetalleGlosasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultarImrpimirRespuestasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>PostgresqlConsultarImprimirRespuestasDao</code>
	 */
	public ConsultarImprimirRespuestasDao getConsultarImprimirRespuestasDao()
	{
		return new PostgresqlConsultarImprimirRespuestasDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultarImprimirGlosasSinRespuestaDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ConsultarImprimirGlosasSinRespuestaDao</code>
	 */
	public ConsultarImprimirGlosasSinRespuestaDao getConsultarImprimirGlosasSinRespuestaDao() {
		
		return new PostgresqlConsultarImprimirGlosasSinRespuestaDao();
	}
	
	/**
	 * Retorna el DAO con el cual el objeto <code>ExcepcionesCCInterconsultasDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>ExcepcionesCCInterconsultasDao</code>
	 */
	public ExcepcionesCCInterconsultasDao getExcepcionesCCInterconsultasDao() {
		return new PostgresqlExcepcionesCCInterconsultasDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RegistroEnvioInfAtencionIniUrgDao</code>, usada por <code>RegistroEnvioInfAtencionIniUrg</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>RegistroEnvioInfAtencionIniUrgDao</code>
	 */
	public RegistroEnvioInfAtencionIniUrgDao  getRegistroEnvioInfAtencionIniUrgDao()
	{
		return new PostgresqlRegistroEnvioInfAtencionIniUrgDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RegistroEnvioInfAtencionIniUrgDao</code>, usada por <code>PostgresqlUtilidadesJustificacionNoPosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>PostgresqlUtilidadesJustificacionNoPosDao</code>
	 */
	public UtilidadesJustificacionNoPosDao  getUtilidadesJustificacionNoPosDao()
	{
		return new PostgresqlUtilidadesJustificacionNoPosDao();
	}
	
	public RegistroEnvioInformInconsisenBDDao  getRegistroEnvioInformInconsisenBDDao()
	{
		return new PostgresqlRegistroEnvioInformInconsisenBDDao();
	}
	
	public ConsultaEnvioInconsistenciasenBDDao getConsultaEnvioInconsistenciasenBDDao()
	{
		return new PostgresqlConsultaEnvioInconsistenciasenBDDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>RegistroEnvioInfAtencionIniUrgDao</code>, usada por <code>RegistroEnvioInfAtencionIniUrg</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>RegistroEnvioInfAtencionIniUrgDao</code>
	 */
	public AutorizacionesDao  getAutorizacionesDao()
	{
		return new PostgresqlAutorizacionesDao();
	}

	/**
	 * Retorna la implementacion en postgres de la interfaz
	 * <code>MotivosAnulacionCondonacionDao</code>, usada por <code>MotivosAnulacionCondonacionDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>MotivosAnulacionCondonacionDao</code>
	 */
	public MotivosAnulacionCondonacionDao  getMotivosAnulacionCondonacionDao()
	{
		return new PostgresqlMotivosAnulacionCondonacionDao();
	}
	
	
	/**
	 * 
	 */
	public AnulacionCondonacionMultasDao getAnulacionCondonacionMultasDao()
	{
		return new PostgresqlAnulacionCondonacionMultasDao();
	}
		
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>MultasDao</code>, usada por <code>MultasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>MultasDao</code>
	 */
	public MultasDao getMultasDao()
	{
		return new PostgresqlMultasDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultaMultasAgendaCitasDao</code>, usada por <code>ConsultaMultasAgendaCitasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ConsultaMultasAgendaCitasDao</code>
	 */
	public ConsultaMultasAgendaCitasDao getConsultaMultasAgendaCitasDao()
	{
		return new PostgresqlConsultaMultasAgendaCitasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CentrosCostoEntidadesSubcontratadasDao</code>, usada por <code>CentrosCostoEntidadesSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>CentrosCostoEntidadesSubcontratadasDao</code>
	 */
	public IngresarModificarContratosEntidadesSubcontratadasDao getIngresarModificarContratosEntidadesSubcontratadasDao()
	{
		return new PostgresqlIngresarModificarContratosEntidadesSubcontratadasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CentrosCostoEntidadesSubcontratadasDao</code>, usada por <code>CentrosCostoEntidadesSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>CentrosCostoEntidadesSubcontratadasDao</code>
	 */
	public CentrosCostoEntidadesSubcontratadasDao getCentrosCostoEntidadesSubcontratadasDao()
	{
		return new PostgresqlCentrosCostoEntidadesSubcontratadasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CargosEntidadesSubcontratadasDao</code>, usada por <code>CargosEntidadesSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>CargosEntidadesSubcontratadasDao</code>
	 */
	public CargosEntidadesSubcontratadasDao getCargosEntidadesSubcontratadasDao()
	{
		return new PostgresqlCargosEntidadesSubcontratadasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AutorizacionesEntidadesSubcontratadasDao</code>, usada por <code>AutorizacionesEntidadesSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>AutorizacionesEntidadesSubcontratadasDao</code>
	 */
	public AutorizacionesEntidadesSubcontratadasDao getAutorizacionesEntidadesSubcontratadasDao() {
		
		return new PostgresqlAutorizacionesEntidadesSubcontratadasDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConsultarImprimirAutorizacionesEntSubcontratadasDao</code>, usada por <code>ConsultarImprimirAutorizacionesEntSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ConsultarImprimirAutorizacionesEntSubcontratadasDao</code>
	 */
	public ConsultarImprimirAutorizacionesEntSubcontratadasDao getConsultarImprimirAutorizacionesEntSubcontratadasDao() {
		
		return new PostgresqlConsultarImprimirAutorizacionesEntSubcontratadasDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ProrrogarAnularAutorizacionesEntSubcontratadasDao</code>, usada por <code>ProrrogarAnularAutorizacionesEntSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ProrrogarAnularAutorizacionesEntSubcontratadasDao</code>
	 */
	public ProrrogarAnularAutorizacionesEntSubcontratadasDao getProrrogarAnularAutorizacionesEntSubcontratadasDao() {
		
		return new PostgresqlProrrogarAnularAutorizacionesEntSubcontratadasDao();
	}

	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CoberturasEntidadesSubcontratadasDao</code>, usada por <code>CoberturasEntidadesSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>CoberturasEntidadesSubcontratadasDao</code>
	 */
	public CoberturasEntidadesSubcontratadasDao getCoberturasEntidadesSubcontratadasDao()
	{
		return new PostgresqlCoberturasEntidadesSubcontratadasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>BusquedaDeudoresGenericaDao</code>, usada por <code>BusquedaDeudoresGenericaDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>BusquedaDeudoresGenericaDao</code>
	 */
	public BusquedaDeudoresGenericaDao getBusquedaDeudoresGenericaDao()
	{
		return new PostgresqlBusquedaDeudoresGenericaDao();
	}
		
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CentrosCostoEntidadesSubcontratadasDao</code>, usada por <code>CentrosCostoEntidadesSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>CentrosCostoEntidadesSubcontratadasDao</code>
	 */
	public ConsultarContratosEntidadesSubcontratadasDao getConsultarContratosEntidadesSubcontratadasDao()
	{
		return new PostgresqlConsultarContratosEntidadesSubcontratadasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CentrosCostoEntidadesSubcontratadasDao</code>, usada por <code>CentrosCostoEntidadesSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>CentrosCostoEntidadesSubcontratadasDao</code>
	 */
	public GeneracionTarifasPendientesEntSubDao getGeneracionTarifasPendientesEntSubDao()
	{
		return new PostgresqlGeneracionTarifasPendientesEntSubDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ResponderConsultasEntSubcontratadasDao</code>, usada por <code>ResponderConsultasEntSubcontratadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ResponderConsultasEntSubcontratadasDao</code>
	 */
	public ResponderConsultasEntSubcontratadasDao getResponderConsultasEntSubcontratadasDao() {
		
		return new PostgresqlResponderConsultasEntSubcontratadasDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ParamInterfazSistema1E</code>, usada por <code>ParamInterfazSistema1E</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ParamInterfazSistema1E</code>
	 */
	public ParamInterfazSistema1EDao getParamInterfazSistema1EDao()
	{
		return new PostgresqlParamInterfazSistema1EDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>GeneracionInterfaz1EDao</code>, usada por <code>GeneracionInterfaz1EDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>GeneracionInterfaz1EDao</code>
	 */
	public GeneracionInterfaz1EDao getGeneracionInterfaz1EDao()
	{
		return new PostgresqlGeneracionInterfaz1EDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>GeneracionInterfaz1EDao</code>, usada por <code>GeneracionInterfaz1EDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>GeneracionInterfaz1EDao</code>
	 */
	public UtilidadesInterfazDao getUtilidadesInterfazDao()
	{
		return new PostgresqlUtilidadesInterfazDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code> DesmarcarDocProcesadosDao</code>, usada por <code> DesmarcarDocProcesadosDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code> DesmarcarDocProcesadosDao</code>
	 */
	public DesmarcarDocProcesadosDao getDesmarcarDocProcesadosDao()
	{
		return new PostgresqlDesmarcarDocProcesadosDao();
		
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>EspecialidadesDao</code>, usada por <code>EspecialidadesDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>EspecialidadesDao</code>
	 */
	public EspecialidadesDao getEspecialidadesDao()
	{
		return new PostgresqlEspecialidadesDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ReporteMovTipoDocDao</code>
	 * @return el DAO usado por <code>ReporteMovTipoDocDao</code>
	 */
	public ReporteMovTipoDocDao getReporteMovTipoDocDao()
	{
		return new PostgresqlReporteMovTipoDocDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConsultaInterfazSistema1EDao</code>, usada por <code>ConsultaInterfazSistema1EDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ConsultaInterfazSistema1EDao</code>
	 */
	public ConsultaInterfazSistema1EDao getConsultaInterfazSistema1EDao()
	{
		return new PostgresqlConsultaInterfazSistema1EDao();
	}
	
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ConceptosRetencionDao</code>, usada por <code>ConceptosRetencionDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>ConceptosRetencionDao</code>
	 */
	public ConceptosRetencionDao getConceptosRetencionDao()
	{
		return new PostgresqlConceptosRetencionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>DetConceptosRetencionDao</code>, usada por <code>DetConceptosRetencionDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>DetConceptosRetencionDao</code>
	 */
	public DetConceptosRetencionDao getDetConceptosRetencion()
	{
		return new PostgresqlDetConceptosRetencionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>DetConceptosRetencionDao</code>, usada por <code>DetConceptosRetencionDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>DetConceptosRetencionDao</code>
	 */
	public TiposRetencionDao getTiposRetencionDao()
	{
		return new PostgresqlTiposRetencionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>DetConceptosRetencionDao</code>, usada por <code>DetConceptosRetencionDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>DetConceptosRetencionDao</code>
	 */
	public UtilidadesFacturasVariasDao getUtilidadesFacturasVariasDao()
	{
		return new PostgresqlUtilidadesFacturasVariasDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>DatosFinanciacionDao</code>, usada por <code>DatosFinanciacionDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>DatosFinanciacionDao</code>
	 */
	public DatosFinanciacionDao getDatosFinanciacionDao()
	{
		return new PostgresqlDatosFinanciacionDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>PazYSalvoCarteraPacienteDao</code>, usada por <code>PazYSalvoCarteraPacienteDaoPazYSalvoCarteraPacienteDao</code>.
	 * @return la implementacion en Oracle de la interfaz
	 * <code>DatosFinanciacionDao</code>
	 */
	public PazYSalvoCarteraPacienteDao getPazYSalvoCarteraPacienteDao()
	{
		return new PostgresqlPazYSalvoCarteraPacienteDao();
	}
	
	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>PostgresqlEdadCarteraPacienteDao</code>, usada por <code>Cartera Paciente Dao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>PostgresqlEdadCarteraPacienteDao</code>
	 */
	public EdadCarteraPacienteDao getEdadCarteraPacienteDao()
	{
		return new PostgresqlEdadCarteraPacienteDao();
	}
	
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>BusquedaConvencionesOdontologicasDao</code>, usada por <code>Odontologia Dao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>BusquedaConvencionesOdontologicasDao</code>
	 */
	public BusquedaConvencionesOdontologicasDao getBusquedaConvencionesOdontologicasDao() {
		
		return new PostgresqlBusquedaConvencionesOdontologicasDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ConvencionesOdontologicasDao</code>, usada por <code>Odontologia Dao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ConvencionesOdontologicasDao</code>
	 */
	public ConvencionesOdontologicasDao getConvencionesOdontologicasDao() {
		
		return new PostgresqlConvencionesOdontologicasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>HallazgosOdontologicosDao</code>, usada por <code>Odontologia Dao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>HallazgosOdontologicosDao</code>
	 */
	public HallazgosOdontologicosDao getHallazgosOdontologicosDao() {
		
		return new PostgresqlHallazgosOdontologicosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>MotivosCitaDao</code>, usada por <code>MotivosCitaDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>MotivosCitaDao</code>
	 */
	public MotivosCitaDao getMotivosCitaDao() {
		
		return new PostgresqlMotivosCitaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>MotivosAtencionOdontologicaDao</code>, usada por <code>MotivosAtencionOdontologicaDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>MotivosAtencionOdontologicaDao</code>
	 */
	public MotivosAtencionOdontologicaDao getMotivosAtencionOdontologicaDao() {
		
		return new PostgresqlMotivosAtencionOdontologicaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ServiAdicionalesXProfAtenOdontoDao</code>, usada por <code>ServiAdicionalesXProfAtenOdontoDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ServiAdicionalesXProfAtenOdontoDao</code>
	 */
	public ServiAdicionalesXProfAtenOdontoDao getServiAdicionalesXProfAtenOdontoDao() {
		
		return new PostgresqlServiAdicionalesXProfAtenOdontoDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>MotivosAtencionOdontologicaDao</code>, usada por <code>MotivosAtencionOdontologicaDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>MotivosAtencionOdontologicaDao</code>
	 */
	public GenerarAgendaOdontologicaDao getGenerarAgendaOdontologicaDao() {
		
		return new PostgresqlGenerarAgendaOdontologicaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ReportePagosCarteraPacienteDao</code>, usada por <code>ReportePagosCarteraPacienteDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ReportePagosCarteraPacienteDao</code>
	 */
	public ReportePagosCarteraPacienteDao getReportePagosCarteraPacienteDao() {
		
		return new PostgresqlReportePagosCarteraPacienteDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>MotivosAtencionOdontologicaDao</code>, usada por <code>MotivosAtencionOdontologicaDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>MotivosAtencionOdontologicaDao</code>
	 */
	public AgendaOdontologicaDao getAgendaOdontologicaDao() {
		
		return new PostgresqlAgendaOdontologicaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>IngresoPacienteOdontologiaDao</code>, usada por <code>IngresoPacienteOdontologiaDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>IngresoPacienteOdontologiaDao</code>
	 */
	public IngresoPacienteOdontologiaDao getIngresoPacienteOdontologiaDao() {
		
		return new PostgresqlIngresoPacienteOdontologiaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>RegistrarConciliacionDao</code>, usada por <code>MotivosAtencionOdontologicaDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>RegistrarConciliacionDaoRegistrarConciliacionDao</code>
	 */
	public RegistrarConciliacionDao getRegistrarConciliacionDao() {
		
		return new PostgresqlRegistrarConciliacionDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>RegistrarConciliacionDao</code>, usada por <code>MotivosAtencionOdontologicaDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>RegistrarConciliacionDaoRegistrarConciliacionDao</code>
	 */
	public CitaOdontologicaDao getCitaOdontologicaDao() {
		
		return new PostgresqlCitaOdontologicaDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>UtilidadesGlosasDao</code>, usada por <code>UtilidadesGlosasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>UtilidadesGlosasDao</code>
	 */
	public UtilidadesGlosasDao getUtilidadesGlosasDao() {
		return new PostgresqlUtilidadesGlosasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>BusquedaConciliacionesDao</code>, usada por <code>BusquedaConciliacionesDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>BusquedaConciliacionesDao</code>
	 */
	public BusquedaConciliacionesDao getBusquedaConciliacionesDao() {
		return new PostgresqlBusquedaConciliacionesDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ReporteEstadoCarteraGlosasDao</code>, usada por <code>ReporteEstadoCarteraGlosasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>BusquedaConciliacionesDao</code>
	 */
	public ReporteEstadoCarteraGlosasDao getReporteEstadoCarteraGlosasDao() {
		return new PostgresqlReporteEstadoCarteraGlosasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ReporteFacturasReiteradasDao</code>, usada por <code>ReporteFacturasReiteradasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ReporteFacturasReiteradasDao</code>
	 */
	public ReporteFacturasReiteradasDao getReporteFacturasReiteradasDao() {
		return new PostgresqlReporteFacturasReiteradasDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ReporteFacturacionEventoRadicarDao</code>, usada por <code>ReporteFacturacionEventoRadicarDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ReporteFacturacionEventoRadicarDao</code>
	 */
	public ReporteFacturacionEventoRadicarDao getReporteFacturacionEventoRadicarDao() {
		return new PostgresqlReporteFacturacionEventoRadicarDao();
	}
	
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ReporteFacturasVencidasNoObjetadasDao</code>, usada por <code>ReporteFacturasVencidasNoObjetadasDao</code>.
	 * @return la implementacion en Postgres de la interfaz
	 * <code>ReporteFacturasVencidasNoObjetadasDao</code>
	 */
	public ReporteFacturasVencidasNoObjetadasDao getReporteFacturasVencidasNoObjetadasDao() {
		return new PostgresqlReporteFacturasVencidasNoObjetadasDao();
	}
	
	/**
	 * ************************************* MANIZALES **********************************************
	 */

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>SubirPacienteDao</code>
	 * 
	 * @return
	 */
	public SubirPacienteDao getSubirPacienteDao() {
		return new PostgresqlSubirPacienteDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>ExcepcionNivelDao</code>
	 * 
	 * @return
	 */
	public ExcepcionNivelDao getExcepcionNivelDao() {
		return new PostgresqlExcepcionNivelDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AjusteCxCDao</code>
	 * 
	 * @return
	 */
	public AjusteCxCDao getAjusteCxCDao() {
		return new PostgresqlAjusteCxCDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AprobacionAjustesDao</code>
	 * 
	 * @return
	 */
	public PagosCapitacionDao getPagosCapitacionDao() {
		return new PostgresqlPagosCapitacionDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>AprobacionAjustesDao</code>
	 * 
	 * @return
	 */
	public AprobacionAjustesDao getAprobacionAjustesDao() {
		return new PostgresqlAprobacionAjustesDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>PacientesConEgresoPorFacturarDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return el DAO usado por <code>PacientesConEgresoPorFacturarDao</code>
	 */
	public PacientesConEgresoPorFacturarDao getPacientesConEgresoPorFacturarDao() {
		return new PostgresqlPacientesConEgresoPorFacturarDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>TiposProgamaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiposProgamaDao</code>
	 */
	public TiposProgamaDao getTiposProgamaDao() {
		return new PostgresqlTiposProgamaDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ContratoCargue</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ContratoCargue</code>
	 */
	public ContratoCargueDao getContratoCargueDao() {
		return new PostgresqlContratoCargueDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>CalificacionesXCumpliMetasDao</code>
	 * 
	 * @return
	 */
	public CalificacionesXCumpliMetasDao getCalificacionesXCumpliMetasDao() {
		return new PostgresqlCalificacionesXCumpliMetasDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ProgramaArticuloPyp</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ProgramaArticuloPyp</code>
	 */
	public ProgramaArticuloPypDao getProgramaArticuloPypDao() {
		return new PostgresqlProgramaArticuloPypDao();
	}

	/**
	 * Retorna el DAO con el cual el objeto <code>ProgramaArticuloPyp</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ProgramaArticuloPyp</code>
	 */
	public TipoCalificacionPypDao getTipoCalificacionPypDao() {
		return new PostgresqlTipoCalificacionPypDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>AntecedentesVacunasDao</code>
	 */
	public AntecedentesVacunasDao getAntecedentesVacunasDao() {
		return new PostgresqlAntecedentesVacunasDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>AntecedentesVacunasDao</code>
	 */
	public UnidadPagoDao getUnidadPagoDao() {
		return new PostgresqlUnidadPagoDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>CrecimientoDesarrolloDao</code>
	 */
	public CrecimientoDesarrolloDao getCrecimientoDesarrolloDao() {
		return new PostgresqlCrecimientoDesarrolloDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>MotivosDescuentosDao</code>
	 */
	
	public MotivosDescuentosDao getMotivosDescuentosDao(){
		return new PostgresqlMotivoDescuentoDao();
	}

	/**
	 * ************************************* FIN MANIZALES
	 * *****************************************
	 */

	/**
	 * ************************************* INICIO MERCURY
	 * *************************************************
	 */

	public ValoracionOdontologiaDao getValoracionOdontologiaDao() {
		return new PostgresqlValoracionOdontologiaDao();
	}

	public OdontogramaDao getOdontogramaDao() {
		return new PostgresqlOdontogramaDao();
	}

	public AntecedentesOdontologiaDao getAntecedentesOdontologiaDao() {
		return new PostgresqlAntecedentesOdontologiaDao();
	}

	public TratamientoOdontologiaDao getTratamientoOdontologiaDao() {
		return new PostgresqlTratamientoOdontologiaDao();
	}

	public IndicePlacaDao getIndicePlacaDao() {
		return new PostgresqlIndicePlacaDao();
	}
	
	public CartaDentalDao getCartaDentalDao()
    {
    	return new PostgresqlCartaDentalDao();
    }

	/**
	 * ************************************* FIN MERCURY
	 * *************************************************
	 */

	/**
	 * ************************************ INICIO SYSMEDICA
	 * ***********************************************
	 */
	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FichaRabiaDao</code>
	 */
	public FichaRabiaDao getFichaRabiaDao() {
		return new PostgresqlFichaRabiaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FichaRabiaDao</code>
	 */
	public FichaSarampionDao getFichaSarampionDao() {
		return new PostgresqlFichaSarampionDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FichaVIHDao</code>
	 */
	public FichaVIHDao getFichaVIHDao() {
		return new PostgresqlFichaVIHDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FichaDengueDao</code>
	 */
	public FichaDengueDao getFichaDengueDao() {
		return new PostgresqlFichaDengueDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FichaParalisisDao</code>
	 */
	public FichaParalisisDao getFichaParalisisDao() {
		return new PostgresqlFichaParalisisDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FichaSifilisDao</code>
	 */
	public FichaSifilisDao getFichaSifilisDao() {
		return new PostgresqlFichaSifilisDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FichaTetanosDao</code>
	 */
	public FichaTetanosDao getFichaTetanosDao() {
		return new PostgresqlFichaTetanosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FichaGenericaDao</code>
	 */
	public FichaGenericaDao getFichaGenericaDao() {
		return new PostgresqlFichaGenericaDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FichaTuberculosisDao</code>
	 */
	public FichaTuberculosisDao getFichaTuberculosisDao() {
		return new PostgresqlFichaTuberculosisDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FichaMortalidadDao</code>
	 */
	public FichaMortalidadDao getFichaMortalidadDao() {
		return new PostgresqlFichaMortalidadDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FichaSivimDao</code>
	 */
	public FichaSivimDao getFichaSivimDao() {
		return new PostgresqlFichaSivimDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FichaBrotesDao</code>
	 */
	public FichaBrotesDao getFichaBrotesDao() {
		return new PostgresqlFichaBrotesDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FichaInfeccionesDao</code>
	 */
	public FichaInfeccionesDao getFichaInfeccionesDao() {
		return new PostgresqlFichaInfeccionesDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FichaInfeccionesDao</code>
	 */
	public FichaLesionesDao getFichaLesionesDao() {
		return new PostgresqlFichaLesionesDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>FichaSolicitudLaboratoriosDao</code>
	 */
	public FichaSolicitudLaboratoriosDao getFichaSolicitudLaboratoriosDao() {
		return new PostgresqlFichaSolicitudLaboratoriosDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>NotificacionDao</code>
	 */
	public NotificacionDao getNotificacionDao() {
		return new PostgresqlNotificacionDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>BusquedaFichasDao</code>
	 * 
	 * @return
	 */
	public BusquedaFichasDao getBusquedaFichasDao() {
		return new PostgresqlBusquedaFichasDao();
	}

	/**
	 * Retorna la implementacion en Postgres de la interfaz
	 * <code>BusquedaNotificacionesDao</code>
	 * 
	 * @return
	 */
	public BusquedaNotificacionesDao getBusquedaNotificacionesDao() {
		return new PostgresqlBusquedaNotificacionesDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ReportesSecretariaDao</code>
	 */
	public ReportesSecretariaDao getReportesSecretariaDao() {
		return new PostgresqlReportesSecretariaDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>IngresoPacienteEpiDao</code>
	 */
	public IngresoPacienteEpiDao getIngresoPacienteEpiDao() {
		return new PostgresqlIngresoPacienteEpiDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ParamLaboratoriosDao</code>
	 */
	public ParamLaboratoriosDao getParamLaboratoriosDao() {
		return new PostgresqlParamLaboratoriosDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>ParamLaboratoriosDao</code>
	 */
	public FichasAnterioresDao getFichasAnterioresDao() {
		return new PostgresqlFichasAnterioresDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>UtilidadFichasDao</code>
	 */
	public UtilidadFichasDao getUtilidadFichasDao() {
		return new PostgresqlUtilidadFichasDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>FichaIntoxicacionesDao</code>
	 */
	public FichaIntoxicacionesDao getFichaIntoxicacionesDao() {
		return new PostgresqlFichaIntoxicacionesDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>FichaRubCongenitaDao</code>
	 */
	public FichaRubCongenitaDao getFichaRubCongenitaDao() {
		return new PostgresqlFichaRubCongenitaDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>FichaAcciOfidicoDao</code>
	 */
	public FichaAcciOfidicoDao getFichaAcciOfidicoDao() {
		return new PostgresqlFichaAcciOfidicoDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>FichaLepraDao</code>
	 */
	public FichaLepraDao getFichaLepraDao() {
		return new PostgresqlFichaLepraDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>FichaDifteriaDao</code>
	 */
	public FichaDifteriaDao getFichaDifteriaDao() {
		return new PostgresqlFichaDifteriaDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>FichaEasvDao</code>
	 */
	public FichaEasvDao getFichaEasvDao() {
		return new PostgresqlFichaEasvDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>FichaEasvDao</code>
	 */
	public FichaEsiDao getFichaEsiDao() {
		return new PostgresqlFichaEsiDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>FichaEtasDao</code>
	 */
	public FichaEtasDao getFichaEtasDao() {
		return new PostgresqlFichaEtasDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>FichaHepatitisDao</code>
	 */
	public FichaHepatitisDao getFichaHepatitisDao() {
		return new PostgresqlFichaHepatitisDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>FichaLeishmaniasisDao</code>
	 */
	public FichaLeishmaniasisDao getFichaLeishmaniasisDao() {
		return new PostgresqlFichaLeishmaniasisDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>FichaMalariaDao</code>
	 */
	public FichaMalariaDao getFichaMalariaDao() {
		return new PostgresqlFichaMalariaDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>FichaMeningitisDao</code>
	 */
	public FichaMeningitisDao getFichaMeningitisDao() {
		return new PostgresqlFichaMeningitisDao();
	}

	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>FichaTosferinaDao</code>
	 */
	public FichaTosferinaDao getFichaTosferinaDao() {
		return new PostgresqlFichaTosferinaDao();
	}

	/**
	 * Retorna la implementacion en Oracle de la interfaz
	 * <code>ArchivosPlanosDao</code>
	 */
	public ArchivosPlanosDao getArchivosPlanosDao()
	{
		return new PostgresqlArchivosPlanosDao();
	}
	
	/**
	 * Retorna la implementacion en Postgresql de la interfaz
	 * <code>RegistroAccidentesTransitoDao</code>
	 */
	public RegistroAccidentesTransitoDao getRegistroAccidentesTransitoDao() {
		return new PostgresqlRegistroAccidentesTransitoDao();
	}

	/**
	 * ************************************ FIN SYSMEDICA
	 * *************************************************
	 */

	/**
	 * Retorna un objeto <code>Connection</code>, que representa una conexion
	 * abierta con una base de datos PostgreSQL, utilizando los par�metros
	 * USERNAME y PASSWORD definidos al momento de inicializar esta <i>Factory</i>.
	 * 
	 * @return una conexion abierta a una base de datos PostgreSQL. Puede o no
	 *         ser una conexi�n proveniente de un pool de conexiones,
	 *         dependiendo de c�mo se haya inicializado esta clase.
	 */
	public Connection getConnection() throws SQLException {
		Connection con = null;
		synchronized (fuenteDatos) {
			con = fuenteDatos.getConnection(); // cogemos la conexion
			Date fecha = new Date();
			SimpleDateFormat dateFormatter = new SimpleDateFormat(
					"dd/MM/yyyy  HH:mm:ss");
			logger.info(" *** N�mero Conexiones ["
					+ dateFormatter.format(fecha) + "] - Activas: "
					+ fuenteDatos.getNumActive() + " Inactivas: "
					+ fuenteDatos.getNumIdle());

		}
		if (con == null) {
			logger
					.error("ERROR AL RECUPERAR LA CONEXION CON LA FUENTE DE DATOS.");
		}
		return con;
		/*
		 * Connection con=DriverManager.getConnection(PROTOCOL, USERNAME,
		 * PASSWORD);
		 * 
		 * if
		 * (poolableConnectionFactory!=null&&poolableConnectionFactory.getPool
		 * ()!=null) { Date fecha= new Date(); SimpleDateFormat dateFormatter =
		 * new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); System.err.println("
		 * Abriendo Conexion --> "+con); System.err.println(" N�mero Conexiones
		 * Activas ["+ dateFormatter.format(fecha)+"] : " +
		 * poolableConnectionFactory.getPool().getNumActive()+" Inactivas
		 * ["+poolableConnectionFactory.getPool().getNumIdle()+"]"); } return
		 * con;
		 */
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
			Log4JManager.error("No pudo cargar el driver");
			return null;
		}
		try
		{
			con= DriverManager.getConnection(PROTOCOL,USERNAME,PASSWORD);
		}
		catch(SQLException e)
		{
			Log4JManager.error("No se pudo conectar");
			return null;
        }
		return con;
	}
	
	
	
	
	/**
	 * Metodo que retorna El numero de conexiones que ha abierto el pool
	 */
	public int getNumeroConeccionesActivas() {
		synchronized (fuenteDatos) {
			return fuenteDatos.getNumActive();
		}
		/*
		 * if
		 * (poolableConnectionFactory!=null&&poolableConnectionFactory.getPool()!=null) {
		 * return poolableConnectionFactory.getPool().getNumActive(); } return
		 * 0;
		 */
	}

	/**
	 * Metodo que retorna El numero de conexiones Inactivas
	 */
	public int getNumeroConeccionesInactivas() {
		synchronized (fuenteDatos) {
			return fuenteDatos.getNumIdle();
		}
	}

	/*
	 * M�todos est�ndar de las DaoFactory para inicializar y obtener conexiones
	 * de una BD
	 */

	/**
	 * Inicializa el acceso a una base de datos PostgreSQL. Este m�todo no
	 * utiliza un pool de conexiones para inicializar la BD.
	 * 
	 * @param driver
	 *            nombre completo del driver jdbc para PostgreSQL
	 * @param protocol
	 *            URL usada para acceder a la BD
	 * @param username
	 *            login del usuario de la BD
	 * @param password
	 *            password del usuario
	 */
	public void init(String driver, String protocol, String username,
			String password) {

		DRIVER = driver;
		PROTOCOL = protocol;
		USERNAME = username;
		PASSWORD = MD5Hash.hashPassword(password); // Los passwords se
													// almacenan "hashed" en la
													// BD

		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException cnfe) {
			logger.error("No encontr� la clase : " + DRIVER);
			cnfe.printStackTrace();
		}

	}

	/**
	 * Inicializa el acceso a una base de datos PostgreSQL, usando un pool de
	 * conexiones.
	 * 
	 * @param driver
	 *            nombre completo del driver jdbc para PostgreSQL
	 * @param protocol
	 *            URL usada para acceder a la BD
	 * @param username
	 *            login del usuario de la BD
	 * @param password
	 *            password del usuario
	 * @param maxActive
	 *            n�mero m�ximo de conexiones activas
	 * @param whenExhaustedAction
	 *            c�digo indicando la accion a seguir cuando se acaben las
	 *            conexiones activas
	 * @param removeAbandonedTimeout
	 *            tiempo en milisegundos antes de remover una conexi�n
	 *            abandonada
	 */
	public void init(String driver, String protocol, String username,
			String password, int maxActive, int maxIdle, int maxWait) {

		DRIVER = driver;
		PROTOCOL = protocol;
		USERNAME = username;
		PASSWORD = password;

		try {
			@SuppressWarnings("unused")
			Context contextoInicial = new InitialContext(); // Equivalente: new InitialContext(null).
			// Context contexto = (Context)
			// contextoInicial.lookup("java:comp/env/jdbc");
			fuenteDatos = new BasicDataSource();
			fuenteDatos.setDriverClassName(DRIVER);
			fuenteDatos.setUrl(PROTOCOL);
			fuenteDatos.setUsername(USERNAME);
			fuenteDatos.setPassword(PASSWORD);

			fuenteDatos.setRemoveAbandoned(true);
			fuenteDatos.setRemoveAbandonedTimeout(30);
			fuenteDatos.setMaxActive(maxActive);
			fuenteDatos.setMaxIdle(maxIdle);
			fuenteDatos.setMaxWait(5000);
			//fuenteDatos.setMinEvictableIdleTimeMillis(60000);
		
		} catch (NamingException e) {
			e.printStackTrace();
		}

		/*
		 * try { Class.forName(DRIVER); } catch (ClassNotFoundException cnfe) {
		 * System.err.println("No encontr� la clase : " + DRIVER);
		 * cnfe.printStackTrace(); }
		 *  // C�digo para configurar el DriverManager y el Connection Pool
		 *  // Establecemos el DriverManager que usaremos para obtener
		 * conexiones ConnectionFactory connectionFactory = new
		 * DriverManagerConnectionFactory(PROTOCOL, USERNAME, PASSWORD);
		 *  // Establecemos la configuraci�n para el manejo de conexiones
		 * abandonadas: se van a remover luego de 'removeAbandonedTimeout'
		 * milisegundos
		 * 
		 * AbandonedConfig config = new AbandonedConfig();
		 * config.setRemoveAbandoned(true);
		 * config.setRemoveAbandonedTimeout(removeAbandonedTimeout);
		 *  // Creamos un ObjectPool que puede recuperar conexiones abandonads
		 * abandonedObjectPool connectionPool = new AbandonedObjectPool(null,
		 * config);
		 * 
		 *  // Configuramos el ObjectPool con los par�metros que nos pasan
		 * connectionPool.setMaxActive(maxActive);
		 * connectionPool.setMaxIdle(maxIdle); connectionPool.setMaxWait(-1);
		 * connectionPool.setWhenExhaustedAction(whenExhaustedAction);
		 *  // Instanciamos la Factory del pool de conexiones
		 * poolableConnectionFactory = new
		 * PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true,config);
		 *  // Creamos el PoolingDriver propiamente dicho PoolingDriver
		 * poolingDriver = new PoolingDriver();
		 *  // ... y registramos nuestro pool con �l
		 * poolingDriver.registerPool("axioma", connectionPool);
		 *  // Ahora, el PROTOCOL que usamos es el del pool PROTOCOL =
		 * "jdbc:apache:commons:dbcp:axioma";
		 */
	}

	@Override
	public TarjetaClienteDao getTarjetaClienteDao() {
		
		return new  PostgresqlTarjetaClienteDao();
	}

	@Override
	public ServicioHonorariosDao getServicioHonorariosDao() {
		
		return new PostgresqlServicioHonorariosDao();
	}

	@Override
	public DetalleServicioHonorariosDao getDetalleServicioHonorariosDao() {
		
		return new PostgresqlDetalleServicioHonorarioDao();
	}

	@Override
	public DetalleAgrupacionHonorarioDao getDetalleAgrupacionHonorarioDao() {
		
		return new PostgresqlDetalleAgrupacionHonorarioDao();
	}

	@Override
	public ContactosEmpresaDao getContactosEmpresaDao() {
		
		return new  PostgresqlContactosEmpresaDao();
	}

	@Override
	public CategoriaAtencionDao getCategoriaAtencionDao() {
		
		return new PostgresqlCategoriaAtencionDao();
	}

	@Override
	public RegionesCoberturaDao getRegionesCoberturaDao() {
		
		return new PostgresqlRegionesCoberturaDao();
	}

	@Override
	public EmisionBonosDescDao getEmisionBonosDescDao() {
			return new PostgresEmisionBonosDao();
	}

	@Override
	public HistoricoAtencionesDao getHistoricoAtencionesDao() {
		return new PostgresHistoricoAtencionesDao();
	}

	@Override
	public ProgramaDao getProgramaDao() {
		
		return new PostgresqlProgramasDao();
	}

	@Override
	public DescuentoOdontologicoDao getDescuentoOdontologicoDao() {
		
		return new PostgresqlDescuentoOdontologicoDao();
	}

	@Override
	public DetalleDescuentoOdontologicoDao getDetalleDescuentoOdontologicoDao() {
		
		return new PostgresqlDetalleDescuentoOdontologicoDao();
	}

	@Override
	public TiposDeUsuarioDao getTiposDeUsuarioDao() {
		
		return new PostgresqlTiposUsuarioDao();
	}

	@Override
	public ConsultarRefinanciarCuotaPacienteDao getConsultarRefinanciarCuotaPacienteDao() {
		
		return new PostgresqlConsultarRefinanciarCuotaPacienteDao();
	}

	@Override
	public HistoricoDescuentoOdontologicoDao getHistoricoDescuentoOdontologicoDao() {
		
		return new PostgresqlHistoricoDescuentoOdontologicoDao();
	}

	@Override
	public HistoricoDetalleDescuentoOdontologico getHistoricoDetalleDescuentoOdontologicoDao() {
		
		return new PostgresqlHistoricoDetalleDescuentoOdontologicoDao();
	}

	@Override
	public DetalleEmisionTarjetaClienteDao getDetalleEmisionTarjetaClienteDao() {
		
		return  new PostgresqlDetalleEmisionTarjetaClienteDao();
	}

	@Override
	public EmisionTarjetaClienteDao getEmisionTarjetaClienteDao() {
		
		return  new PostgresqlEmisionTarjetaClienteDao();
	}

	@Override
	public HistoricoDetalleEmisionTarjetaClienteDao getHistoricoDetalleEmisionTarjetaClienteDao() {
		
		return  new PostgresqlHistoricoDetalleEmisionTarjetaClienteDao();
	}

	@Override
	public HistoricoEmisionTarjetaClienteDao getHistoricoEmisionTarjetaClienteDao() {
		
		return  new PostgresqlHistoricoEmisionTarjetaClienteDao();
	}	
	
	/**
	 * 
	 */
	public PromocionesOdontologicasDao getPromocionesOdontologicasDao(){
		 return new PostgresqlPromocionesOdontologicasDao();
	}
	
	
	/**
	 * 
	 */
	public DetPromocionesOdoDao getDetPromocionesOdoDao(){
		 return new PostgresqlDetPromocionesOdo();
	}


	@Override
	public AliadoOdontologicoDao getAliadoOdontologicoDao() {
		
		return new PostgresqlAliadoOdontologicoDao();
	}


	
	/**
	 * 
	 * @return
	 */
	public DetCaPromocionesOdoDao getDetCaPromocionesOdoDao(){
		 return new PostgresqlDetCaPromocionesOdoDao();
	}
	/**
	 * 
	 * @return
	 */
	public DetConvPromocionesOdoDao getDetConvPromocionesOdoDao(){
		 return new PostgresqlDetConvPromocionesOdoDao();
	}
	
		@Override
	public ControlAnticipoContratoDao getControlAnticipoContratoDao() {
		
		return new PostgesqlControlAnticipoContratoDao();
	}

	@Override
	public LogControlAnticipoContratoDao getLogControlAnticipoContratoDao() {
		
		return new PostgresqlLogControlAnticipoContratoDao();
	}

	@Override
	public VentasTarjetasClienteDao getVentasTarjetasCliente() {
	return new PostgresqlVentasTarjetaClienteDao();
	}

	@Override
	public BeneficiarioTarjetaClienteDao getBeneficiarioTarjetaClienteDao() {
		return new PostgresqlBeneficiariosTarjetaCliente();
	}

	@Override
	public ParentezcoDao getParentezcoDao() {
		
		return new PostgresqlParentezcoDao();
	}

	@Override
	public ImagenesBaseDao getImagenesBaseDao() {
		
		return new PostgresqlImagenesBaseDao();
	}
	
	public ProgramasOdontologicosDao getProgramasOdontologicosDao()
	{
		return new PostgresqlProgramasOdontologicosDao();
	}

	@Override
	public RegistroIncapacidadesDao getRegistroIncapacidadesDao() {
		
		return new PostgresqlRegistroIncapacidadesDao();
	}

	@Override
	public PerfilNEDDao getPerfilNEDDao() {
		
		return new PostgresqlPerfilNEDDao();
	}

	@Override
	public DetalleHallazgoProgramaServicioDao getDetalleHallazgoProgramaServicioDao() {
		
		return new PostgresqlDetalleHallazgoProgramaServicioDao();
	}

	@Override
	public HallazgoVsProgramaServicioDao getHallazgoVsProgramaServicioDao() {
		
		return  new PostgresqlHallazgoVsProgramaServicioDao();
	}

	@Override
	public UtilidadOdontologiaDao getUtilidadOdontologiaDao() {
		
		return new PostgresqlUtilidadOdontologiaDao();
	}
	
	public ExtractoDeudoresCPDao getExtractoDeudoresCPDao()
	{
		return new PostgresqlExtractoDeudoresCPDao();
	}

	@Override
	public CargosOdonDao getCargosOdonDao() {
		
		return new PostgresqlCargosOdonDao();
	}

	

	@Override
	public PresupuestoOdontologicoDao getPresupuestoOdontologicoDao() {
		
		return new PostgresqlPresupuestoOdontologiaDao();
	}
	
	public AutorizacionIngresoPacienteSaldoMoraDao getAutorizacionIngresoPacienteSaldoMoraDao()
	{
		return new PostgresqlAutorizacionIngresoPacienteSaldoMoraDao();
	}

	@Override
	public PlanTratamientoDao getPlanTratamientoDao() {
		
		return new PostgresqlPlanTratamientoDao();
	}
	
	public ReporteDocumentosCarteraPacienteDao getReporteDocumentosCarteraPacienteDao()
	{
		return new PostgresqlReporteDocumentosCarteraPacienteDao();
	}

	@Override
	public ValidacionesPresupuestoDao getValidacionesPresupuestoDao()
	{
		
		return new PostgresqlValidacionesPresupuestoDao();
	}
	
	public EdadGlosaXFechaRadicacionDao getEdadGlosaXFechaRadicacionDao()
	{
		return new PostgresqlEdadGlosaXFechaRadicacionDao();
	}
	
	public AperturaCuentaPacienteOdontologicoDao getAperturaCuentaPacienteOdontologicoDao()
	{
		return new PostgresqlAperturaCuentaPacienteOdontologicoDao();
	}
	
	/**
	 * Retorna la implementacion en PostgreSQL de la interfaz
	 * <code>AtencionCitasOdontologiaDao</code>, usada por
	 * <code>AtencionCitasOdontologia</code>.
	 * 
	 * @return la implementacion en PostgreSQL de la interfaz
	 *         <code>AtencionCitasOdontologiaDao</code>
	 */
	public AtencionCitasOdontologiaDao getAtencionCitasOdontologiaDao() {
		return new PostgresqlAtencionCitasOdontologiaDao();
	}
	
	/**
	 * 
	 */
	public ReasignarProfesionalOdontoDao getReasignarProfesionalOdontoDao() {
		return new PostgresqlReasignarProfesionalOdontoDao();
	}

	@Override
	public PacientesConvenioOdontologiaDao getPacientesConvenioOdontologiaDao() {
		return new PostgresqlPacientesConvenioOdontologiaDao();
	}

	@Override
	public ValoracionOdontologicaDao getValoracionOdontologicaDao() {
		
		return new PostgresqlValoracionOdontologicaDao();
	}

	
	
	public CancelarAgendaOdontoDao getCancelarAgendaOdontoDao() {
		return new PostgresqlCancelarAgendaOdontoDao();
	}

	@Override
	public PresupuestoExclusionesInclusionesDao getPresupuestoExclusionesInclusionesDao()
	{
		return new PostgresqlPresupuestoExclusionesInclusionesDao();
	}
	
		@Override
	public VerResumenOdontologicoDao getVerResumenOdontologicoDao() {
		
		return new PostgresqlVerResumenOdontologicoDao();
	}

	@Override
	public EvolucionOdontologicaDao getEvolucionOdontologicaDao() {
		
		return new PostgresqlEvolucionOdontologicaDao();
	}

	@Override
	public ProcesosAutomaticosOdontologiaDao getProcesosAutomaticosOdontologiaDao() {
		
		return new PostgresqlProcesosAutomaticosOdontologiaDao();
	}

	@Override
	public SaldosInicialesCarteraPacienteDao getSaldosInicialesCarteraPacienteDao() 
	{
		return new PostgresqlSaldosInicialesCarteraPacienteDao();
	}
	
	@Override
	public CierreSaldoInicialCarteraPacienteDao getCierreSaldoInicialCarteraPacienteDao()
	{
		return new PostgresqlCierreSaldoInicialCarteraPacienteDao();
	}
	
	public AutorizacionDescuentosOdonDao getAutorizacionDescuentosOdonDao()
	{
		return new PostgresqlAutorizacionDescuentosOdonDao();
	}

	@Override
	public HonorariosPoolDao getHonorariosPoolDao() {
		
		return new PostgresqlHonorariosPoolDao();
	}

	@Override
	public CalculoHonorariosPoolesDao getCalculoHonorariosPoolesDao()
	{
		
		return new PostgresqlCalculoHonorariosPoolesDao();
	}

	@Override
	public EnvioEmailAutomaticoDao getEnvioEmailAutomaticoDao() {
		return new PostgresqlEnvioEmailAutomaticoDao();
	}

	@Override
	public ExcepcionesHorarioAtencionDao getExcepcionesHorarioAtencionDao() {
		
		return  new PostgresqlExcepcionesHorarioAtencionDao();
	}
	
	@Override
	public NewMenuDao getNewMenuDao() {
		return  new PostgresqlNewMenuDao();
	}

	@Override
	public ConsecutivosCentroAtencionDao getConsecutivosCentroAtencionDao() {
		
		return new PostgresqlConsecutivosCentroAtencionDao();
	}

	@Override
	public FacturaOdontologicaDao getFacturaOdontologicaDao() {
		return new PostgresqlFacturaOdontologicaDao();
	}

	@Override
	public NumeroSuperficiesPresupuestoDao getNumeroSuperficiesPresupuestoDao() {
		return new PostgresqlNumeroSuperficiesPresupuestoDao();
	}

	@Override
	public PresupuestoContratadoDao getPresupuestoContratadoDao() {
		return new PostgresqlPresupuestoContratadoDao();
	}

	@Override
	public PresupuestoCuotasEspecialidadDao getPresupuestoCuotasEspecialidadDao() {
		return new PostgresqlPresupuestoCuotasEspecialidadDao();
	}

	@Override
	public PresupuestoLogImpresionDao getPresupuestoLogImpresionDao() {
		return new PostgresqlPresupuestoLogImpresionDao();
	}

	@Override
	public PresupuestoPaquetesDao getPresupuestoPaquetesDao() {
		return new PostgresqlPresupuestoPaquetesDao();
	}

	@Override
	public MontoAgrupacionArticuloDAO getMontoAgrupacionArticuloDAO() {		
		return new PostgresSQLMontoAgrupacionArticulo();
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
		return new PostgresSQLMontoArticuloEspecificoDAO();
	}
	

	/**
	 * 
	 * Este m�todo se encarga de devolver una instancia de 
	 * HistoMontoAgrupacionArticuloDAO
	 * @return HistoMontoAgrupacionArticuloDAO
	 * @author Angela Maria Aguirre
	 */
	@Override
	public HistoMontoAgrupacionArticuloDAO getHistoMontoAgrupacionArticuloDAO() {
		return new PostgresSQLHistoMontoAgrupacionArticulo();
	}
	
	/**
	 * 
	 */
	@Override
	public ReporteCitasOdontologicasDao getReporteCitasOdontologicasDao()
	{
		return new PostgresqlReporteCitasOdontologicasDao();
		
	}

	/**
	 * 
	 * Este m�todo se encarga de devolver una instancia de 
	 * PostgresSQLNivelAutorizacionAgrupacionArticuloDAO
	 * @return PostgresSQLNivelAutorizacionAgrupacionArticuloDAO
	 * @author Angela Maria Aguirre
	 */
	@Override
	public NivelAutorizacionAgrupacionArticuloDAO getNivelAutorizacionAgrupacionArticuloDAO() {
		return new PostgresSQLNivelAutorizacionAgrupacionArticuloDAO();
	}

	/**
	 * 
	 * Este m�todo se encarga de devolver una instancia de 
	 * PostgresSQLNivelAutorizacionArticuloEspecificoDAO
	 * @return PostgresSQLNivelAutorizacionArticuloEspecificoDAO
	 * @author Angela Maria Aguirre
	 */
	@Override
	public NivelAutorizacionArticuloEspecificoDAO getNivelAutorizacionArticuloEspecificoDAO() {
		return new PostgresSQLNivelAutorizacionArticuloEspecificoDAO();
	}
	
	/**
	 * 
	 */
	@Override
	public CalculoValorCobrarPacienteDao getCalculoValorCobrarPacienteDao()
	{
		return new PostgresqlCalculoValorCobrarPacienteDao();
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public ProcesoNivelAutorizacionDao getProcesoNivelAutorizacionDao()
	{
		return new PostgresqlProcesoNivelAutorizacionDao();
	}

	@Override
	public boolean alterarSesionFechaOracle(Connection con) throws SQLException 
	{
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("alter session set NLS_DATE_FORMAT='YYYY-MM-DD'",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		boolean resultado=ps.executeUpdate()>0;
		ps.close();
		return resultado;
	}
	
}