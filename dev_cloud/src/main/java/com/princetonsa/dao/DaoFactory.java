/*
 * @(#)DaoFactory.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mercury.dao.odontologia.AntecedentesOdontologiaDao;
import com.mercury.dao.odontologia.CartaDentalDao;
import com.mercury.dao.odontologia.IndicePlacaDao;
import com.mercury.dao.odontologia.OdontogramaDao;
import com.mercury.dao.odontologia.TratamientoOdontologiaDao;
import com.mercury.dao.odontologia.ValoracionOdontologiaDao;
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
import com.princetonsa.dao.oracle.OracleDaoFactory;
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
import com.princetonsa.dao.postgresql.PostgresqlDaoFactory;
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

/**
 * Esta clase abstracta es una <i>Factory</i> que produce <i>DAOs</i>,
 * "Data Acces Objects". Los DAOs son los objetos encargados de acceder y operar
 * sobre una fuente de datos, como puede ser una Base de Datos Relacional. Con
 * el fin de desacoplar el modelo del mundo de la persistencia de ï¿½te, los
 * DAOs proporcionan una interfaz estï¿½ndar con las operaciones de persistencia
 * propias de cada objeto, interfaz que es implementada por cada fuente de
 * datos, segun corresponda.
 * 
 * @version 1.0, Sep 20, 2002
 * @author <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s
 *         L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo
 *         Andr&eacute;s Camacho P.</a>
 */

public abstract class DaoFactory {

	/**
	 * Constante que identifica las bases de datos Access.
	 */
	public static final int ACCESS = 9;

	/**
	 * Constante que identifica las bases de datos dBase.
	 */
	public static final int DBASE = 5;

	/**
	 * Constante que identifica las bases de datos FoxPro.
	 */
	public static final int FOXPRO = 8;

	/**
	 * Constante que identifica las bases de datos MySQL.
	 */
	public static final int MYSQL = 3;

	/**
	 * Constante que identifica las bases de datos Oracle.
	 */
	public static final int ORACLE = 1;

	/**
	 * Constante que identifica las bases de datos PostgreSQL.
	 */
	public static final int POSTGRESQL = 2;

	/**
	 * Constante que identifica las bases de datos SQLServer.
	 */
	public static final int SQLSERVER = 10;

	/**
	 * Constante que identifica las bases de datos Sybase.
	 */
	public static final int SYBASE = 6;

	/**
	 * Constante que identifica las bases de datos Teradata.
	 */
	public static final int TERADATA = 7;

	/**
	 * Constante que identifica las bases de datos Unify.
	 */
	public static final int UNIFY = 4;

	/**
	 * Dado el cdigo de un tipo de fuente de datos, retorna la implementacin de
	 * <code>DaoFactory</code> correspondiente.
	 * 
	 * @param whichFactory
	 *            el cdigo de la fuente de datos que se desea instanciar, debe
	 *            ser una de las constantes definidas para esta clase
	 * @return la implementacin de <code>DaoFactory</code> especificada en
	 *         <code>whichFactory</code>
	 */
	public static DaoFactory getDaoFactory(int whichFactory) {

		switch (whichFactory) {

		case POSTGRESQL:
			return PostgresqlDaoFactory.getInstance();
		case ORACLE:
			return OracleDaoFactory.getInstance();
		default:
			return null;

		}

	}

	/**
	 * Dado el nombre de un tipo de fuente de datos, retorna la implementacin de
	 * <code>DaoFactory</code> correspondiente.
	 * 
	 * @param whichFactoryName
	 *            el nombre de la fuente de datos que se desea instanciar, debe
	 *            ser uno de los nombres de las constantes definidas para esta
	 *            clase
	 * @return la implementacion de <code>DaoFactory</code> especificada en
	 *         <code>whichFactoryName</code>
	 */
	public static DaoFactory getDaoFactory(String whichFactoryName) {

		String whichFactory = whichFactoryName.trim().toUpperCase();

		if (whichFactory.compareTo("ORACLE") == 0)
			return getDaoFactory(ORACLE);
		else if (whichFactory.compareTo("POSTGRESQL") == 0)
			return getDaoFactory(POSTGRESQL);
		else if (whichFactory.compareTo("MYSQL") == 0)
			return getDaoFactory(MYSQL);
		else if (whichFactory.compareTo("UNIFY") == 0)
			return getDaoFactory(UNIFY);
		else if (whichFactory.compareTo("DBASE") == 0)
			return getDaoFactory(DBASE);
		else if (whichFactory.compareTo("SYBASE") == 0)
			return getDaoFactory(SYBASE);
		else if (whichFactory.compareTo("TERADATA") == 0)
			return getDaoFactory(TERADATA);
		else if (whichFactory.compareTo("FOXPRO") == 0)
			return getDaoFactory(FOXPRO);
		else if (whichFactory.compareTo("ACCESS") == 0)
			return getDaoFactory(ACCESS);
		else if (whichFactory.compareTo("SQLSERVER") == 0)
			return getDaoFactory(SQLSERVER);
		else
			return null;

	}

	public static int getConstanteTipoBD(String tipoBD) {
		if (tipoBD.equals("ORACLE"))
			return ORACLE;
		else if (tipoBD.equals("POSTGRESQL"))
			return POSTGRESQL;
		else
			return -1;
	}

	/**
	 * Hace rollback de una transaccin.
	 * 
	 * @param con
	 *            una conexin abierta con la fuente de datos
	 */
	public abstract void abortTransaction(Connection con) throws SQLException;

	/**
	 * Inicia una transacciï¿½n.
	 * 
	 * @param con
	 *            una conexin abierta con la fuente de datos
	 * @return boolean <b>true</b> si se pudo iniciar la transaccin,
	 *         <b>false</b> si no
	 */
	public abstract boolean beginTransaction(Connection con)
			throws SQLException;

	/**
	 * Termina una transaccin
	 * 
	 * @param con
	 *            una conexin abierta con la fuente de datos
	 */
	public abstract void endTransaction(Connection con) throws SQLException;

	/**
	 * Hace rollback de una transaccin.
	 * 
	 * @param con
	 *            una conexin abierta con la fuente de datos
	 */
	public abstract void abortTransactionSinMensaje(Connection con)
			throws SQLException;

	/**
	 * Inicia una transacciï¿½n.
	 * 
	 * @param con
	 *            una conexin abierta con la fuente de datos
	 * @return boolean <b>true</b> si se pudo iniciar la transaccin,
	 *         <b>false</b> si no
	 */
	public abstract boolean beginTransactionSinMensaje(Connection con)
			throws SQLException;

	/**
	 * Termina una transaccin
	 * 
	 * @param con
	 *            una conexin abierta con la fuente de datos
	 */
	public abstract void endTransactionSinMensaje(Connection con)
			throws SQLException;

	/**
	 * @param con
	 * @param cadenaSQL
	 * @param filtro
	 */
	@SuppressWarnings("unchecked")
	public abstract boolean bloquearRegistroActualizacion(Connection con,
			String cadenaSQL, ArrayList filtro);

	/**
	 * Obtiene el ultimo valor generado por una secuencia
	 * 
	 * @param con
	 *            una conexion abierta con la base de datos
	 * @param nombreSecuencia
	 *            el nombre de la secuencia
	 * @return el ultimo valor generado por la secuencia
	 * @throws SQLException
	 */
	public abstract int obtenerUltimoValorSecuencia(Connection con,
			String nombreSecuencia) throws SQLException;

	/**
	 * Metodo que incrementa una secuencia. y retorna el valor obtenido.
	 * 
	 * @param con
	 * @param nombreSecuencia
	 *            de la secuencia.
	 */
	public abstract int incrementarValorSecuencia(Connection con,
			String nombreSecuencia);

	/**
	 * Metodo que retorn el Dao de Utilidades BD
	 * 
	 * @return
	 */
	public abstract UtilidadesBDDao getUtilidadesBDao();

	/**
	 * Metodo que retorn el Dao de Utilidad Impresion
	 * 
	 * @return
	 */
	public abstract UtilidadImpresionDao getUtilidadImpresionDao();


	/**
	 * Retorna el DAO con el cual el objeto <code>AdminMedicamentosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AdminMedicamentos</code>
	 */
	public abstract AdminMedicamentosDao getAdminMedicamentosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AdmisionHospitalaria</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AdmisionHospitalaria</code>
	 */
	public abstract AdmisionHospitalariaDao getAdmisionHospitalariaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AdmisionUrgencias</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AdmisionUrgencias</code>
	 */
	public abstract AdmisionUrgenciasDao getAdmisionUrgenciasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ActivPyPXFacturar</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ActivPyPXFacturar</code>
	 */
	public abstract ActivPyPXFacturarDao getActivPyPXFacturarDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Agenda</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Agenda</code>
	 */
	public abstract AgendaDao getAgendaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AjustesEmpresaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AjustesEmpresa</code>
	 */
	public abstract AjustesEmpresaDao getAjustesEmpresaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AntecedenteMedicamento</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AntecedenteMedicamento</code>
	 */
	public abstract AntecedenteMedicamentoDao getAntecedenteMedicamentoDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AntecedenteMorbidoMedico</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AntecedenteMorbidoMedico</code>
	 */
	public abstract AntecedenteMorbidoMedicoDao getAntecedenteMorbidoMedicoDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AntecedenteMorbidoQuirurgico</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>AntecedenteMorbidoQuirurgico</code>
	 */
	public abstract AntecedenteMorbidoQuirurgicoDao getAntecedenteMorbidoQuirurgicoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AntecedentePediatrico</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AntecedentePediatrico</code>
	 */
	public abstract AntecedentePediatricoDao getAntecedentePediatricoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AntecedentesAlergias</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AntecedentesAlergias</code>
	 */
	public abstract AntecedentesAlergiasDao getAntecedentesAlergiasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AntecedentesFamiliares</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AntecedentesFamiliares</code>
	 */
	public abstract AntecedentesFamiliaresDao getAntecedentesFamiliaresDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AntecedentesGinecoObstetricos</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>AntecedentesGinecoObstetricos</code>
	 */
	public abstract AntecedentesGinecoObstetricosDao getAntecedentesGinecoObstetricosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AntecedentesGinecoObstetricosHistorico</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>AntecedentesGinecoObstetricosHistorico</code>
	 */
	public abstract AntecedentesGinecoObstetricosHistoricoDao getAntecedentesGinecoObstetricosHistoricoDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AntecedentesMedicamentos</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AntecedentesMedicamentos</code>
	 */
	public abstract AntecedentesMedicamentosDao getAntecedentesMedicamentosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AntecedentesMorbidos</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AntecedentesMorbidos</code>
	 */
	public abstract AntecedentesMorbidosDao getAntecedentesMorbidosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AntecedentesToxicos</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AntecedentesToxicos</code>
	 */
	public abstract AntecedentesToxicosDao getAntecedentesToxicosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AntecedentesTransfusionales</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>AntecedentesTransfusionales</code>
	 */
	public abstract AntecedentesTransfusionalesDao getAntecedentesTransfusionalesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AntecedentesVarios</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AntecedenteVario</code>
	 */
	public abstract AntecedentesVariosDao getAntecedentesVariosDao();

	public abstract AntecedenteToxicoDao getAntecedenteToxicoDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AntecedenteTransfusional</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AntecedenteTransfusional</code>
	 */
	public abstract AntecedenteTransfusionalDao getAntecedenteTransfusionalDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Articulo</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Articulo</code>
	 */
	public abstract ArticuloDao getArticuloDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Articulos</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Articulos</code>
	 */
	public abstract ArticulosDao getArticulosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AsociosTipoServicioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AsociosTipoServicio</code>
	 */
	public abstract AsociosTipoServicioDao getAsociosTipoServicioDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AsociosXUvrDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AsociosXUvr</code>
	 */
	public abstract AsociosXUvrDao getAsociosXUvrDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AuxiliarDiagnosticos</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AuxiliarDiagnosticos</code>
	 */
	public abstract AuxiliarDiagnosticosDao getAuxiliarDiagnosticosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>BusquedaServiciosGenericaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>BusquedaServiciosGenerica</code>
	 */
	public abstract BusquedaServiciosGenericaDao getBusquedaServiciosGenericaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>BusquedaArticulosGenericaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>BusquedaArticulosGenerica</code>
	 */
	public abstract BusquedaArticulosGenericaDao getBusquedaArticulosGenericaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>BusquedaCondicionTmExamenGenericaDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>BusquedaCondicionesTomaExamenGenerica</code>
	 */
	public abstract BusquedaCondicionTmExamenGenericaDao getBusquedaCondicionTmExamenGenericaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>BusquedaInstitucionesSircGenericaDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>BusquedaInstitucionesSircGenericaDao</code>
	 */
	public abstract BusquedaInstitucionesSircGenericaDao getBusquedaInstitucionesSircGenericaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Cama</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por <code>Cama</code>
	 */
	public abstract CamaDao getCamaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Camas</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por <code>Camas</code>
	 */
	public abstract CamasDao getCamasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Camas1</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Camas1</code>
	 */
	public abstract Camas1Dao getCamas1Dao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CensoCamas1</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CensoCamas1</code>
	 */
	public abstract CensoCamas1Dao getCensoCamas1Dao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CIEDao</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CIE</code>
	 */
	public abstract CIEDao getCIEDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Cita</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por <code>Cita</code>
	 */
	public abstract CitaDao getCitaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ClasificacionSocioEconomicaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ClasificacionSocioEconomica</code>
	 */
	public abstract ClasificacionSocioEconomicaDao getClasificacionSocioEconomicaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CoberturaAccidentesTransitoDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>CoberturaAccidentesTransitoDao</code>
	 */
	public abstract CoberturaAccidentesTransitoDao getCoberturaAccidentesTransitoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ContratoDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Contrato</code>
	 */
	public abstract ContratoDao getContratoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConvenioDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Convenio</code>
	 */
	public abstract ConvenioDao getConvenioDao();

	/**
	 * Retorna un objeto <code>java.sql.Connection</code>, que representa una
	 * conexion abierta con una fuente de datos. Dependiendo de cï¿½mo se
	 * inicializï¿½ la factory, esta conexiï¿½n puede o no venir de un pool de
	 * conexiones.
	 * 
	 * @return una conexion abierta a una fuente de datos
	 */
	public abstract Connection getConnection() throws SQLException;

	/**
	 * Alterar la sesión de la conexción de oracle para que acepte conexiones en formato YYYY-MM-DD (Estandar BD)
	 * @param con Conexión con la BD
	 * @throws SQLException Excepción al alterar la sesión
	 */
	public abstract boolean alterarSesionFechaOracle(Connection con) throws SQLException;

	/**
	 * Retorna un objeto <code>java.sql.Connection</code>, que representa una
	 * conexion abierta con una fuente de datos. Dependiendo de cï¿½mo se
	 * inicializï¿½ la factory, esta conexiï¿½n puede o no venir de un pool de
	 * conexiones.
	 * 
	 * @return una conexion abierta a una fuente de datos
	 */
	public abstract Connection getConnectionNoPool() throws SQLException;

	/**
	 * Metodo que retorna El numero de conexiones que ha abierto el pool
	 */
	public abstract int getNumeroConeccionesActivas();

	/**
	 * 
	 * @return
	 */
	public abstract int getNumeroConeccionesInactivas();

	/**
	 * Retorna el DAO con el cual el objeto <code>Cuenta</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Cuenta</code>
	 */
	public abstract CuentaDao getCuentaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>DespachoMedicamentosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DespachoMedicamentos</code>
	 */
	public abstract DespachoMedicamentosDao getDespachoMedicamentosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>DespachoPedidosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DespachoPedidos</code>
	 */
	public abstract DespachoPedidosDao getDespachoPedidosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>DevolucionAFarmaciaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DevolucionAFarmacia</code>
	 */
	public abstract DevolucionAFarmaciaDao getDevolucionAFarmaciaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>DocumentoAdjunto</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DocumentoAdjunto</code>
	 */
	public abstract DocumentoAdjuntoDao getDocumentoAdjuntoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>DocumentosAdjuntos</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DocumentosAdjuntos</code>
	 */
	public abstract DocumentosAdjuntosDao getDocumentosAdjuntosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Egreso</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Egreso</code>
	 */
	public abstract EgresoDao getEgresoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Embarazo</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Embarazo</code>
	 */
	public abstract EmbarazoDao getEmbarazoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Evolucion</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Evolucion</code>
	 */
	public abstract EvolucionDao getEvolucionDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>EvolucionHospitalaria</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>EvolucionHospitalaria</code>
	 */
	public abstract EvolucionHospitalariaDao getEvolucionHospitalariaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ExcepcionesFarmacia</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ExcepcionesFarmacia</code>
	 */
	public abstract ExcepcionesFarmaciaDao getExcepcionesFarmaciaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ExcepcionesServiciosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ExcepcionesServicios</code>
	 */
	public abstract ExcepcionesServiciosDao getExcepcionesServiciosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ExcepcionesQXDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ExcepcionesQX</code>
	 */
	public abstract ExcepcionesQXDao getExcepcionesQXDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>FacturaDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Factura</code>
	 */
	public abstract FacturaDao getFacturaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AnulacionFacturasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AnulacionFacturas</code>
	 */
	public abstract AnulacionFacturasDao getAnulacionFacturasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>GruposDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Grupos</code>
	 */
	public abstract GruposDao getGruposDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>HijoBasico</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>HijoBasico</code>
	 */
	public abstract HijoBasicoDao getHijoBasicoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>HistoriaClinica</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>HistoriaClinica</code>
	 */
	public abstract HistoriaClinicaDao getHistoriaClinicaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>HistoricoAdmisiones</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>HistoricoAdmisiones</code>
	 */
	public abstract HistoricoAdmisionesDao getHistoricoAdmisionesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>HistoricoEvoluciones</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>HistoricoEvoluciones</code>
	 */
	public abstract HistoricoEvolucionesDao getHistoricoEvolucionesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>HorarioAtencion</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>HorarioAtencion</code>
	 */
	public abstract HorarioAtencionDao getHorarioAtencionDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>InformacionParametrizable</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>InformacionParametrizable</code>
	 */
	public abstract InformacionParametrizableDao getInformacionParametrizableDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>IngresoGeneral</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>IngresoGeneral</code>
	 */
	public abstract IngresoGeneralDao getIngresoGeneralDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Interconsulta</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Interconsulta</code>
	 */
	public abstract SolicitudInterconsultaDao getSolicitudInterconsultaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ListadoCitasDao</code>
	 * interactï¿½a con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Citas</code>
	 */
	public abstract ListadoCitasDao getListadoCitasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Medico</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Medico</code>
	 */
	public abstract MedicoDao getMedicoDao();

	/*
	 * Cada posible DAO, asociado a cada objeto de la aplicacion que necesite
	 * acceder a la fuente de datos, debe ser definido a continuacion
	 */

	/**
	 * Retorna el DAO con el cual el objeto <code>MenuFilter</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>MenuFilter</code>
	 */
	public abstract MenuDao getMenuDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Paciente</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Paciente</code>
	 */
	public abstract PacienteDao getPacienteDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ParametrizacionCurvaAlertaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ParametrizacionCurvaAlerta</code>
	 */
	public abstract ParametrizacionCurvaAlertaDao getParametrizacionCurvaAlertaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ParticipacionPoolXTarifasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ParticipacionPoolXTarifa</code>
	 */
	public abstract ParticipacionPoolXTarifasDao getParticipacionPoolXTarifaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PersonaBasica</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PersonaBasica</code>
	 */
	public abstract PersonaBasicaDao getPersonaBasicaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Persona</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Persona</code>
	 */
	public abstract PersonaDao getPersonaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Pooles</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Pooles</code>
	 */
	public abstract PoolesDao getPoolesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Procedimiento</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Procedimiento</code>
	 */
	public abstract ProcedimientoDao getProcedimientoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>RequisitosPacienteDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>RequisitosPaciente</code>
	 */
	public abstract RequisitosPacienteDao getRequisitosPacienteDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>RolesFuncsBean</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>RolesFuncsBean</code>
	 */
	public abstract RolesFuncsDao getRolesFuncsDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Servicios</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Servicios</code>
	 */
	public abstract ServiciosDao getServiciosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ServiciosCamas1</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ServiciosCamas1</code>
	 */
	public abstract ServiciosCamas1Dao getServiciosCamas1Dao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>SolicitudConsultaExternaDao</code> interactï¿½a con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>SolicitudConsultaExterna</code>
	 */
	public abstract SolicitudConsultaExternaDao getSolicitudConsultaExternaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Solicitud</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Solicitud</code>
	 */
	public abstract SolicitudDao getSolicitudDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Solicitudes</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Solicitudes</code>
	 */
	public abstract SolicitudesDao getSolicitudesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>SolicitudProcedimiento</code>
	 * interactï¿½a con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>SolicitudProcedimiento</code>
	 */
	public abstract SolicitudProcedimientoDao getSolicitudProcedimientoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>SolicitudesCxDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>SolicitudesCx</code>
	 */
	public abstract SolicitudesCxDao getSolicitudesCxDao();

	/**
	 * Retorna el DAO con el cual los <i>custom tags</i> interactuan con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por los <i>custom tags</i>
	 */
	public abstract TagDao getTagDao();

	/**
	 * Retorna el DAO con el cual el objeto <i>UsuarioBasico</i> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>UsuarioBasico</code>
	 */
	public abstract UsuarioBasicoDao getUsuarioBasicoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Usuario</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Usuario</code>
	 */
	public abstract UsuarioDao getUsuarioDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadValidacion</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>UtilidadValidacion</code>
	 */
	public abstract UtilidadValidacionDao getUtilidadValidacionDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ValidacionesSolicitud</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ValidacionesSolicitud</code>
	 */
	public abstract ValidacionesSolicitudDao getValidacionesSolicitudDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>EmpresaDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Empresa</code>
	 */
	public abstract EmpresaDao getEmpresaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TarifaISSDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TarifaISS</code>
	 */
	public abstract TarifaISSDao getTarifaISSDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TarifaSISSDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TarifaSISS</code>
	 */
	public abstract TarifasISSDao getTarifasISSDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TarifaSOATDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TarifaSOAT</code>
	 */
	public abstract TarifaSOATDao getTarifaSOATDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TarifasSOATDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TarifasSOAT</code>
	 */
	public abstract TarifasSOATDao getTarifasSOATDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>EsquemaTarifarioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>EsquemaTarifario</code>
	 */
	public abstract EsquemaTarifarioDao getEsquemaTarifarioDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>EstadoCuentaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>EstadoCuenta</code>
	 */
	public abstract EstadoCuentaDao getEstadoCuentaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ExcepcionesTarifasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ExcepcionesTarifas</code>
	 */
	public abstract ExcepcionesTarifasDao getExcepcionesTarifasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>RecargoTarifaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>RecargoTarifa</code>
	 */
	public abstract RecargoTarifaDao getRecargoTarifaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>RecargoTarifasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>RecargoTarifas</code>
	 */
	public abstract RecargoTarifasDao getRecargoTarifasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>RecargosTarifasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>RecargosTarifas</code>
	 */
	public abstract RecargosTarifasDao getRecargosTarifasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>SalarioMinimoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>SalarioMinimo</code>
	 */
	public abstract SalarioMinimoDao getSalarioMinimoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>MantenimientoTablas</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>MantenimientoTablas</code>
	 */
	public abstract MantenimientoTablasDao getMantenimientoTablasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>Tercero</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Tercero</code>
	 */
	public abstract TerceroDao getTerceroDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TopesFacturacionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TopesFacturacion</code>
	 */
	public abstract TopesFacturacionDao getTopesFacturacionDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>SolicitudEvolucion</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>SolicitudEvolucion</code>
	 */
	public abstract SolicitudEvolucionDao getSolicitudEvolucionDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ResumenAtenciones</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ResumenAtenciones</code>
	 */
	public abstract ResumenAtencionesDao getResumenAtencionesDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>GeneracionCargosPendientes</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>GeneracionCargosPendientes</code>
	 */
	public abstract GeneracionCargosPendientesDao getGeneracionCargosPendientesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>MontosCobro</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>MontosCobro</code>
	 */
	public abstract MontosCobroDao getMontosCobroDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>MovimientoFacturasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>MovimientoFacturas</code>
	 */
	public abstract MovimientoFacturasDao getMovimientoFacturasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PagosPaciente</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract PagosPacienteDao getPagosPacienteDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PagosEmpresaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PagosEmpresa</code>
	 */
	public abstract PagosEmpresaDao getPagosEmpresaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ActivacionCargos</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ActivacionCargosDao getActivacionCargosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>RegistrosDiagnosticos</code>
	 * interactua con la base de datos*
	 * 
	 * @return RegistroDiagnosticosDato Interface que contine los metodos de la
	 *         funcionalidad registro de diagnosticos.
	 */
	public abstract RegistroDiagnosticosDao getRegistroDiagnosticosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>RegUnidadesConsulta</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return RegUnidadesConsultaDao
	 */
	public abstract RegUnidadesConsultaDao getRegUnidadesConsultaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ExcepcionesNaturalezaPaciente</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return ExcepcionesNaturalezaPaciente
	 */
	public abstract ExcepcionesNaturalezaPacienteDao getExcepcionesNaturalezaPacienteDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ValoresPorDefecto</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return ValoresPorDefectoDao
	 */
	public abstract ValoresPorDefectoDao getValoresPorDefectoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>SustitutosInventarios</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return SustitutosInventariosDao.
	 */
	public abstract SustitutosInventariosDao getSustitutosInventariosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TarifasInventario</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return TarifasInventarioDao.
	 */
	public abstract TarifasInventarioDao getTarifasInventarioDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>SolicitudMedicamentos</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return SolicitudMedicamentosDao.
	 */
	public abstract SolicitudMedicamentosDao getSolicitudMedicamentosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PedidosInsumos</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract PedidosInsumosDao getPedidosInsumosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>DevolucionPedidos</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract DevolucionPedidosDao getDevolucionPedidosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>Recepcion DevolucionPedidos</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract RecepcionDevolucionPedidosDao getRecepcionDevolucionPedidosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>Recepcion DevolucionMedicamentos</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract RecepcionDevolucionMedicamentosDao getRecepcionDevolucionMedicamentosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ParamInstitucion</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return Objeto <code>ParamInstitucionDao</code> para la comunicacion con
	 *         la BD
	 */
	public abstract ParamInstitucionDao getParamInstitucionDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>MedicosXPool</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return Objeto <code>MedicosXPoolDao</code> para la comunicacion con la
	 *         BD
	 */
	public abstract MedicosXPoolDao getMedicosXPoolDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>GeneracionExcepcionesFarmacia</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return Objeto <code>GeneracionExcepcionesFarmaciaDao</code> para la
	 *         comunicacion con la BD
	 */
	public abstract GeneracionExcepcionesFarmaciaDao getGeneracionExcepcionesFarmaciaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ValidacionesFactura</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return Objeto <code>ValidacionesFacturaDao</code> para la comunicacion
	 *         con la BD
	 */
	public abstract ValidacionesFacturaDao getValidacionesFacturaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AbonosYDescuentosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return Objeto <code>AbonosYDescuentosDao</code> para la comunicacion con
	 *         la BD
	 */
	public abstract AbonosYDescuentosDao getAbonosYDescuentosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadesDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return Objeto <code>UtilidadesDao</code> para la comunicacion con la BD
	 */
	public abstract UtilidadesDao getUtilidadesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>OrdenMedica</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>OrdenMedica</code>
	 */
	public abstract OrdenMedicaDao getOrdenMedicaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>HojaObstetrica</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>HojaObstetrica</code>
	 */
	public abstract HojaObstetricaDao getHojaObstetricaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>HojaOftalmologica</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>HojaOftalmologica</code>
	 */
	public abstract HojaOftalmologicaDao getHojaOftalmologicaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AntecedentesOftalmologicos</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>AntecedentesOftalmologicos</code>
	 */
	public abstract AntecedentesOftalmologicosDao getAntecedentesOftalmologicosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>onceptosFacturasVariasDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>onceptosFacturasVariasDao</code>
	 */
	public abstract ConceptosFacturasVariasDao getConceptosFacturasVariasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>GenModFacturasVariasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>GenModFacturasVariasDao</code>
	 */
	public abstract GenModFacturasVariasDao getGenModFacturasVariasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>DeudoresDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DeudoresDao</code>
	 */
	public abstract DeudoresDao getDeudoresDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaFacturasVariasDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultaFacturasVariasDao</code>
	 */
	public abstract ConsultaFacturasVariasDao getConsultaFacturasVariasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>BusquedaTercerosGenericaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>DeudoresDao</code>
	 */
	public abstract BusquedaTercerosGenericaDao getBusquedaTercerosGenericaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ListadoIngresosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ListadoIngresosDao</code>
	 */
	public abstract ListadoIngresosDao getListadoIngresosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>EquivalentesDeInventarioDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>EquivalentesDeInventarioDao</code>
	 */
	public abstract EquivalentesDeInventarioDao getEquivalentesDeInventarioDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AsociosXTipoServicioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AsociosXTipoServicioDao</code>
	 */
	public abstract AsociosXTipoServicioDao getAsociosXTipoServicioDao();

	/*********************** SiES *************************/

	/**
	 * Retorna el DAO con el cual el objeto <code>TriageDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return Objeto <code>TriageDao</code> para la comunicaciï¿½n con la BD
	 */
	public abstract TriageDao getTriageDao();

	/*
	 * Todas las Factory concretas (e.g., PostgresqlDaoFactory) deben
	 * implementar estos mï¿½odos de inicializacin de BD, obtencin de conexiones
	 * y manejo de transacciones
	 */

	/**
	 * Retorna el DAO con el cual el objeto <code>CuentasCobroDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return Objeto <code>CuentasCobroDao</code> para la comunicacion con la
	 *         BD
	 */
	public abstract CuentasCobroDao getCuentasCobroDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CuentasCobroDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return Objeto <code>DiasFestivosDao</code> para la comunicacion con la
	 *         BD
	 */
	public abstract DiasFestivosDao getDiasFestivosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ValidacionesCierreCuentaDao</code> interactua con la fuente de
	 * datos
	 * 
	 * @return Objeto <code>ValidacionesCierreCuentaDao</code> para la
	 *         comunicacion con la BD
	 */
	public abstract ValidacionesCierreCuentaDao getValidacionesCierreCuentaDao();

	/**
	 * Inicializa el estado interno de la fuente de datos. Es obligatorio que
	 * las clases concretas que extiendan esta clase abstracta, implementen este
	 * mï¿½todo.
	 * 
	 * @param driver
	 *            nombre completo del driver para la fuente de datos
	 * @param protocol
	 *            URL usada para acceder a la fuente de datos
	 * @param username
	 *            login del usuario en la fuente de datos
	 * @param password
	 *            password del usuario en la fuente de datos
	 */
	public abstract void init(String driver, String protocol, String username,
			String password);

	/**
	 * Inicializa el estado interno de la fuente de datos, configurando algunos
	 * parï¿½metros para usar un pool de conexiones. Es opcional que las clases
	 * concretas que extiendan esta clase abstracta, implementen este mï¿½todo.
	 * (pueden proporcionar una implementaciï¿½n vacï¿½a, que llame al mï¿½todo de
	 * arriba y no haga nada mï¿½s).
	 * 
	 * @param driver
	 *            nombre completo del driver para la fuente de datos
	 * @param protocol
	 *            URL usada para acceder a la fuente de datos
	 * @param username
	 *            login del usuario en la fuente de datos
	 * @param password
	 *            password del usuario en la fuente de datos
	 * @param maxActive
	 *            nï¿½mero mï¿½ximo de conexiones activas
	 * @param whenExhaustedAction
	 *            cï¿½digo indicando la accion a seguir cuando se acaben las
	 *            conexiones activas
	 * @param removeAbandonedTimeout
	 *            tiempo en milisegundos antes de remover una conexiï¿½n
	 *            abandonada
	 */
	public abstract void init(String driver, String protocolo, String usuario,
			String password, int maxActive, int maxIdle, int maxWait);

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>MotivoAnulacionFacturasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>MotivoAnulacionFacturas</code>
	 */
	public abstract MotivoAnulacionFacturasDao getMotivoAnulacionFacturasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConceptosCarteraDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return Objeto <code>ConceptosCarteraDao</code> para la comunicacion con
	 *         la BD
	 */
	public abstract ConceptosCarteraDao getConceptosCarteraDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ActualizacionAutorizacionDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ActualizacionAutorizacion</code>
	 */
	public abstract ActualizacionAutorizacionDao getActualizacionAutorizacionDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>JustificacionDinamicaDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>JustificacionDinamica</code>
	 */
	public abstract JustificacionDinamicaDao getJustificacionDinamicaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaFacturasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultaFacturas</code>
	 */
	public abstract ConsultaFacturasDao getConsultaFacturasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>RipsDao</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Rips</code>
	 */
	public abstract RipsDao getRipsDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TesoreriaDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Tesoreria</code>
	 */
	public abstract ConceptoTesoreriaDao getTesoreriaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TrasladoCamasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TrasladoCamas</code>
	 */
	public abstract TrasladoCamasDao getTrasladoCamasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsecutivosDisponiblesDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ConsecutivosDisponibles</code>
	 */
	public abstract ConsecutivosDisponiblesDao getConsecutivosDisponiblesDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CierreSaldoInicialCarteraDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>CierreSaldoInicialCarteraDao</code>
	 */
	public abstract CierreSaldoInicialCarteraDao getCierreSaldoInicialCarteraDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ValidacionesAnulacionFacturasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ValidacionesAnulacionFacturas</code>
	 */
	public abstract ValidacionesAnulacionFacturasDao getValidacionesAnulacionFacturasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>EstanciaAutomaticaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>EstanciaAutomatica</code>
	 */
	public abstract EstanciaAutomaticaDao getEstanciaAutomaticaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AprobacionAjustesEmpresaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>AprobacionAjustesEmpresaDao</code>
	 */
	public abstract AprobacionAjustesEmpresaDao getAprobacionAjustesEmpresaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TiposMonitoreoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiposMonitoreoDao</code>
	 */
	public abstract TiposMonitoreoDao getTiposMonitoreoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TipoSalasDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TipoSalasDao</code>
	 */
	public abstract TipoSalasDao getTipoSalasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>SalasDao</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>SalasDao</code>
	 */
	public abstract SalasDao getSalasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>PorcentajesCxMultiplesDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PorcentajesCxMultiplesDao</code>
	 */
	public abstract PorcentajesCxMultiplesDao getPorcentajesCxMultiplesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CajasDao</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CajasDao</code>
	 */
	public abstract CajasDao getCajaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CajasCajerosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CajasDao</code>
	 */
	public abstract CajasCajerosDao getCajasCajerosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>FormasPagoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>FormasPagoDao</code>
	 */
	public abstract FormasPagoDao getFormasPagoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>EntidadesFinancierasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>EntidadesFinancierasDao</code>
	 */
	public abstract EntidadesFinancierasDao getEntidadesFinancierasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ExcepcionAsocioTipoSalaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ExcepcionAsocioTipoSalaDao</code>
	 */
	public abstract ExcepcionAsocioTipoSalaDao getExcepcionAsocioTipoSalaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TarjetasFinancierasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TarjetasFinancierasDao</code>
	 */
	public abstract TarjetasFinancierasDao getTarjetasFinancierasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>RecibosCajaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>RecibosCajaDao</code>
	 */
	public abstract RecibosCajaDao getRecibosCajaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AnulacionRecibosCajaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AnulacionRecibosCajaDao</code>
	 */
	public abstract AnulacionRecibosCajaDao getAnulacionRecibosCajaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>MaterialesQxDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>MaterialesQxDao</code>
	 */
	public abstract MaterialesQxDao getMaterialesQxDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaReciboCajaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultaReciboCajaDao</code>
	 */
	public abstract ConsultaReciboCajaDao getConsultaReciboCajaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PeticionDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PeticionDao</code>
	 */
	public abstract PeticionDao getPeticionDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PreanestesiaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PreanestesiaDao</code>
	 */
	public abstract PreanestesiaDao getPreanestesiaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PreanestesiaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PreanestesiaDao</code>
	 */
	public abstract ProgramacionCirugiaDao getProgramacionCirugiaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AprobacionPagosCarteraDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AprobacionPagosCarteraDao</code>
	 */
	public abstract AprobacionPagosCarteraDao getAprobacionPagosCarteraDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>getAplicacionPagosEmpresaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>getAplicacionPagosEmpresaDao</code>
	 */
	public abstract AplicacionPagosEmpresaDao getAplicacionPagosEmpresaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ResponderCirugiasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ResponderCirugias</code>
	 */
	public abstract ResponderCirugiasDao getResponderCirugiasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>NotasGeneralesEnfermeriaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>NotasGeneralesEnfermeria</code>
	 */
	public abstract NotasGeneralesEnfermeriaDao getNotasGeneralesEnfermeriaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>HojaAnestesiaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>HojaAnestesiaDao</code>
	 */
	public abstract HojaAnestesiaDao getHojaAnestesiaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>TiposTransaccionesInvDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiposTransaccionesInvDao</code>
	 */
	public abstract TiposTransaccionesInvDao getTiposTransaccionesInvDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>HojaQuirurgicaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>HojaQuirurgicaDao</code>
	 */
	public abstract HojaQuirurgicaDao getHojaQuirurgicaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>TransaccionesValidasXCCDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>TransaccionesValidasXCCDao</code>
	 */
	public abstract TransaccionesValidasXCCDao getTransaccionesValidasXCCDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>MotDevolucionInventariosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>MotDevolucionInventariosDao</code>
	 */
	public abstract MotDevolucionInventariosDao getMotDevolucionInventariosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ArticulosPuntoPedidoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ArticulosPuntoPedidoDao</code>
	 */
	public abstract ArticulosPuntoPedidoDao getArticulosPuntoPedidoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>UsuariosXAlmacenDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>UsuariosXAlmacenDao</code>
	 */
	public abstract UsuariosXAlmacenDao getUsuariosXAlmacenDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>NotasRecuperacionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>NotasRecuperacion</code>
	 */
	public abstract NotasRecuperacionDao getNotasRecuperacionDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ModificarReversarQxDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ModificarReversarQxDao</code>
	 */
	public abstract ModificarReversarQxDao getModificarReversarQxDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadInventariosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>UtilidadInventariosDao</code>
	 */
	public abstract UtilidadInventariosDao getUtilidadInventariosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadLaboratoriosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>UtilidadLaboratoriosDao</code>
	 */
	public abstract UtilidadLaboratoriosDao getUtilidadLaboratoriosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RegistroTransaccionesDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>RegistroTransaccionesDao</code>
	 */
	public abstract RegistroTransaccionesDao getRegistroTransaccionesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PresupuestoPacienteDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PresupuestoPacienteDao</code>
	 */
	public abstract PresupuestoPacienteDao getPresupuestoPacienteDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CierresInventarioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CierresInventarioDao</code>
	 */
	public abstract CierresInventarioDao getCierresInventarioDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>KardexDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>KardexDao</code>
	 */
	public abstract KardexDao getKardexDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaHonorariosMedicosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ConsultaHonorariosMedicosDao</code>
	 */
	public abstract ConsultaHonorariosMedicosDao getConsultaHonorariosMedicosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ExistenciasInventariosDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ExistenciasInventariosDao</code>
	 */
	public abstract ExistenciasInventariosDao getExistenciasInventariosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>SolicitudTrasladoAlmacenDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>SolicitudTrasladoAlmacenDao</code>
	 */
	public abstract SolicitudTrasladoAlmacenDao getSolicitudTrasladoAlmacenDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>FormatoImpresionPresupuestoDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>FormatoImpresionPresupuestoDao</code>
	 */
	public abstract FormatoImpresionPresupuestoDao getFormatoImpresionPresupuestoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaPresupuestoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultaPresupuestoDao</code>
	 */
	public abstract ConsultaPresupuestoDao getConsultaPresupuestoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>GruposServiciosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>GruposServiciosDao</code>
	 */
	public abstract GruposServiciosDao getGruposServiciosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>DespachoTrasladoAlmacenDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>DespachoTrasladoAlmacenDao</code>
	 */
	public abstract DespachoTrasladoAlmacenDao getDespachoTrasladoAlmacenDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaImpresionTrasladosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ConsultaImpresionTrasladosDao</code>
	 */
	public abstract ConsultaImpresionTrasladosDao getConsultaImpresionTrasladosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>FormatoImpresionFacturaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>FormatoImpresionFacturaDao</code>
	 */
	public abstract FormatoImpresionFacturaDao getFormatoImpresionFacturaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>RegistroEnfermeriaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>RegistroEnfermeriaDao</code>
	 */
	public abstract RegistroEnfermeriaDao getRegistroEnfermeriaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ImpresionFacturaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ImpresionFacturaDao</code>
	 */
	public abstract ImpresionFacturaDao getImpresionFacturaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ExTarifasAsociosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ExTarifasAsociosDao</code>
	 */
	public abstract ExTarifasAsociosDao getExTarifasAsociosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaProfesionalPoolDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ConsultaProfesionalPoolDao</code>
	 */
	public abstract ConsultaProfesionalPoolDao getConsultaProfesionalPoolDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaTarifasServiciosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ConsultaTarifasServiciosDao</code>
	 */
	public abstract ConsultaTarifasServiciosDao getConsultaTarifasServiciosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CuentaInventarioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CuentaInventarioDao</code>
	 */
	public abstract CuentaInventarioDao getCuentaInventarioDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CuentaInventarioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CuentaInventarioDao</code>
	 */
	public abstract CuentaServicioDao getCuentaServicioDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CuentasConveniosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CuentasConveniosDao</code>
	 */
	public abstract CuentasConveniosDao getCuentasConveniosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ArqueosDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ArqueosDao</code>
	 */
	public abstract ArqueosDao getArqueosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CampoInterfazDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CampoInterfazDao</code>
	 */
	public abstract CampoInterfazDao getCampoInterfazDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ParamInterfazDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ParamInterfaz</code>
	 */
	public abstract ParamInterfazDao getParamInterfazDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>UnidadFuncionalDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>UnidadFuncional</code>
	 */
	public abstract UnidadFuncionalDao getUnidadFuncionalDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CuposExtraDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CuposExtra</code>
	 */
	public abstract CuposExtraDao getCuposExtraDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ReasignarAgendaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ReasignarAgenda</code>
	 */
	public abstract ReasignarAgendaDao getReasignarAgendaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaLogCuposExtraDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultaLogCuposExtra</code>
	 */
	public abstract ConsultaLogCuposExtraDao getConsultaLogCuposExtraDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CentrosCostoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CentrosCosto</code>
	 */
	public abstract CentrosCostoDao getCentrosCostoDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CentroCostoGrupoServicioDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>CentroCostoGrupoServicio</code>
	 */
	public abstract CentroCostoGrupoServicioDao getCentroCostoGrupoServicioDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CentrosCostoXUnidadConsultaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>CentrosCostoXUnidadConsultaDao</code>
	 */
	public abstract CentrosCostoXUnidadConsultaDao getCentrosCostoXUnidadConsultaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CentrosAtencionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CentrosAtencionDao</code>
	 */
	public abstract CentrosAtencionDao getCentrosAtencionDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultarCentrosCostoDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultarCentrosCosto</code>
	 */
	public abstract ConsultarCentrosCostoDao getConsultarCentrosCostoDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CuentaUnidadFuncionalDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CuentaUnidadFuncionalDao</code>
	 */
	public abstract CuentaUnidadFuncionalDao getCuentaUnidadFuncionalDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CentrosCostoViaIngresoDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CentrosCostoViaIngresoDao</code>
	 */
	public abstract CentrosCostoViaIngresoDao getCentrosCostoViaIngresoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>GruposEtareosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>GruposEtareosDao</code>
	 */
	public abstract GruposEtareosDao getGruposEtareosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaGruposEtareosDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultaGruposEtareosDao</code>
	 */
	public abstract ConsultaGruposEtareosDao getConsultaGruposEtareosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ArticulosXMezclaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ArticulosXMezclaDao</code>
	 */
	public abstract ArticulosXMezclaDao getArticulosXMezclaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>SistemasMotivoConsultaDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>SistemasMotivoConsultaDao</code>
	 */
	public abstract SistemasMotivoConsultaDao getSistemasMotivoConsultaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>SignosSintomasXSistemaDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>SignosSintomasXSistemaDao</code>
	 */
	public abstract SignosSintomasXSistemaDao getSignosSintomasXSistemaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>DestinoTriageDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DestinoTriageDao</code>
	 */
	public abstract DestinoTriageDao getDestinoTriageDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>UnidadMedidaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>UnidadMedida</code>
	 */
	public abstract UnidadMedidaDao getUnidadMedidaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>MezclasDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Mezcla</code>
	 */
	public abstract MezclasDao getMezclasDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>CategoriasTriageDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CategoriasTriage</code>
	 */
	public abstract CategoriasTriageDao getCategoriasTriageDao();

	/**
	 *Retorna el DAO con el cual el objeto
	 * <code>ConsultaPacientesTriageDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ConsultaPacientesTriageDao</code>
	 */
	public abstract ConsultaPacientesTriageDao getConsultaPacientesTriageDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>PacientesTriageUrgenciasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>PacientesTriageUrgencias</code>
	 */
	public abstract PacientesTriageUrgenciasDao getPacientesTriageUrgenciasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>MezclasDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>Mezcla</code>
	 */
	public abstract SignosSintomasDao getSignosSintomasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>PacientesUrgenciasPorValorarDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>PacientesUrgenciasPorValorarDao</code>
	 */
	public abstract PacientesUrgenciasPorValorarDao getPacientesUrgenciasPorValorarDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>InformacionPartoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>InformacionPartoDao</code>
	 */
	public abstract InformacionPartoDao getInformacionPartoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>NivelServicioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>NivelServicioDao</code>
	 */
	public abstract NivelAtencionDao getNivelAtencionDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CuentaCobroCapitacionDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CuentaCobroCapitacion</code>
	 */
	public abstract CuentaCobroCapitacionDao getCuentaCobroCapitacionDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>GeneracionInterfazDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>GeneracionInterfaz</code>
	 */
	public abstract GeneracionInterfazDao getGeneracionInterfazDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RadicacionCuentasCobroCapitacionDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return el DAO usado por <code>RadicacionCuentasCobroCapitacionDao</code>
	 */
	public abstract RadicacionCuentasCobroCapitacionDao getRadicacionCuentasCobroCapitacionDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CuentasProcesoFacturacionDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>CuentasProcesoFacturacionDao</code>
	 */
	public abstract CuentasProcesoFacturacionDao getCuentasProcesoFacturacionDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>PacientesUrgenciasSalaEsperaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>PacientesUrgenciasSalaEsperaDao</code>
	 */
	public abstract PacientesUrgenciasSalaEsperaDao getPacientesUrgenciasSalaEsperaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>PacientesUrgenciasPorHospitalizarDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>PacientesUrgenciasPorHospitalizarDao</code>
	 */
	public abstract PacientesUrgenciasPorHospitalizarDao getPacientesUrgenciasPorHospitalizarDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TiposProgamaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiposProgamaDao</code>
	 */
	public abstract TiposProgamaDao getTiposProgamaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>GrupoEtareoCrecimientoDesarrolloDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return el DAO usado por <code>GrupoEtareoCrecimientoDesarrolloDao</code>
	 */
	public abstract GrupoEtareoCrecimientoDesarrolloDao getGrupoEtareoCrecimientoDesarrolloDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ProgramasSaludPYPDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ProgramasSaludPYPDao</code>
	 */
	public abstract ProgramasSaludPYPDao getProgramasSaludPYPDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AsociarCxCAFacturasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AsociarCxCAFacturasDao</code>
	 */
	public abstract AsociarCxCAFacturasDao getAsociarCxCAFacturasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ActividadesPypDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ActividadesPypDao</code>
	 */
	public abstract ActividadesPypDao getActividadesPypDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CierresCarteraCapitacionDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>CierresCarteraCapitacionDao</code>
	 */
	public abstract CierresCarteraCapitacionDao getCierresCarteraCapitacionDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ProgramasActividadesConvenioDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ProgramasActividadesConvenioDao</code>
	 */
	public abstract ProgramasActividadesConvenioDao getProgramasActividadesConvenioDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ActividadesProgramasPYPDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ActividadesProgramasPYPDao</code>
	 */
	public abstract ActividadesProgramasPYPDao getActividadesProgramasPYPDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>MetasPYPDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>MetasPYPDao</code>
	 */
	public abstract MetasPYPDao getMetasPYPDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RegistroSaldosInicialesCapitacionDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>RegistroSaldosInicialesCapitacionDao</code>
	 */
	public abstract RegistroSaldosInicialesCapitacionDao getRegistroSaldosInicialesCapitacionDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ProgramasPYPDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ProgramasPYPDao</code>
	 */
	public abstract ProgramasPYPDao getProgramasPYPDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>OrdenesAmbulatoriasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>OrdenesAmbulatoriasDao</code>
	 */
	public abstract OrdenesAmbulatoriasDao getOrdenesAmbulatoriasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>TrasladoCentroAtencionDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TrasladoCentroAtencionDao</code>
	 */
	public abstract TrasladoCentroAtencionDao getTrasladoCentroAtencionDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ImpresionResumenAtencionesDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ImpresionResumenAtencionesDao</code>
	 */

	public abstract ImpresionResumenAtencionesDao getImpresionResumenAtencionesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>MotivosSircDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>MotivosSircDao</code>
	 */
	public abstract MotivosSircDao getMotivosSircDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>InstitucionesSircDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>InstitucionesSircDao</code>
	 */
	public abstract InstitucionesSircDao getInstitucionesSircDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ServiciosSircDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ServiciosSircDao</code>
	 */
	public abstract ServiciosSircDao getServiciosSircDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ActEjecutadasXCargarDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ActEjecutadasXCargarDao</code>
	 */
	public abstract ActEjecutadasXCargarDao getActEjecutadasXCargarDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>IndicativoSolicitudGrupoServiciosDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>IndicativoSolicitudGrupoServiciosDao</code>
	 */
	public abstract IndicativoSolicitudGrupoServiciosDao getIndicativoSolicitudGrupoServiciosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>IndicativoCargoViaIngresoServicioDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>IndicativoCargoViaIngresoServicioDao</code>
	 */
	public abstract IndicativoCargoViaIngresoServicioDao getIndicativoCargoViaIngresoServicioDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RespuestaProcedimientosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>RespuestaProcedimientosDao</code>
	 */
	public abstract RespuestaProcedimientosDao getRespuestaProcedimientosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>InterpretarProcedimientoDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>InterpretarProcedimientoDao</code>
	 */
	public abstract InterpretarProcedimientoDao getInterpretarProcedimientoDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>InformacionRecienNacidosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>InformacionRecienNacidosDao</code>
	 */

	public abstract InformacionRecienNacidosDao getInformacionRecienNacidosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ImpresionCLAPDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ImpresionCLAPDao</code>
	 */
	public abstract ImpresionCLAPDao getImpresionCLAPDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>BusquedaDiagnosticosGenericaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>BusquedaDiagnosticosGenericaDao</code>
	 */
	public abstract BusquedaDiagnosticosGenericaDao getBusquedaDiagnosticosGenericaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TrasladosCajaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TrasladosCajaDao</code>
	 */
	public abstract TrasladosCajaDao getTrasladosCajaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultaWSDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultaWSDao</code>
	 */
	public abstract ConsultaWSDao getConsultaWSDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultoriosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultoriosDao</code>
	 */
	public abstract ConsultoriosDao getConsultoriosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TiposInventarioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiposInventarioDao</code>
	 */
	public abstract TiposInventarioDao getTiposInventarioDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>HojaGastosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>HojaGastosDao</code>
	 */
	public abstract HojaGastosDao getHojaGastosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ArticulosFechaVencimientoDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ArticulosFechaVencimientoDao</code>
	 */
	public abstract ArticulosFechaVencimientoDao getArticulosFechaVencimientoDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>FormaFarmaceuticaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>FormaFarmaceutica</code>
	 */
	public abstract FormaFarmaceuticaDao getFormaFarmaceuticaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CoberturaDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CoberturaDao</code>
	 */
	public abstract CoberturaDao getCoberturaDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>PaquetesDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PaquetesDao</code>
	 */
	public abstract PaquetesDao getPaquetesDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>PaquetesDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PaquetesDao</code>
	 */
	public abstract NaturalezaArticulosDao getNaturalezaArticulosDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>ViasIngresoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ViasIngresoDao</code>
	 */
	public abstract ViasIngresoDao getViasIngresoDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>UbicacionGeograficaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>UbicacionGeograficaDao</code>
	 */
	public abstract UbicacionGeograficaDao getUbicacionGeograficaDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>ArticuloCatalogoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ArticuloCatalogoDao</code>
	 */
	public abstract ArticuloCatalogoDao getArticuloCatalogoDao();

	/**
	 *Retorna el DAO con el cual el objeto
	 * <code>DevolucionInventariosPacienteDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>DevolucionInventariosPacienteDao</code>
	 */
	public abstract DevolucionInventariosPacienteDao getDevolucionInventariosPacienteDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>ConsultarAdmisionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultarAdmisionDao</code>
	 */
	public abstract ConsultarAdmisionDao getConsultarAdmisionDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>TiposMonedaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiposMonedaDao</code>
	 */
	public abstract TiposMonedaDao getTiposMonedaDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>ServiciosViaAccesoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ServiciosViaAccesoDao</code>
	 */
	public abstract ServiciosViaAccesoDao getServiciosViaAccesoDao();

	/**
	 *Retorna el DAO con el cual el objeto
	 * <code>ParamArchivoPlanoColsanitasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ParamArchivoPlanoColsanitasDao</code>
	 */
	public abstract ParamArchivoPlanoColsanitasDao getParamArchivoPlanoColsanitasDao();

	/**
	 *Retorna el DAO con el cual el objeto
	 * <code>InclusionesExclusionesDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>InclusionesExclusionesDao</code>
	 */
	public abstract InclusionesExclusionesDao getInclusionesExclusionesDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>PaquetesConvenioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PaquetesConvenioDao</code>
	 */
	public abstract PaquetesConvenioDao getPaquetesConvenioDao();

	/**
	 *Retorna el DAO con el cual el objeto
	 * <code>ServiciosGruposEsteticosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ServiciosGruposEsteticosDao</code>
	 */
	public abstract ServiciosGruposEsteticosDao getServiciosGruposEsteticosDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>CuentaInvUnidadFunDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CuentaInvUnidadFunDao</code>
	 */
	public abstract CuentaInvUnidadFunDao getCuentaInvUnidadFunDao();

	/**
	 *Retorna el DAO con el cual el objeto
	 * <code>ConsultaAjustesEmpresaDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultaAjustesEmpresaDao</code>
	 */
	public abstract ConsultaAjustesEmpresaDao getConsultaAjustesEmpresaDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>ExamenCondiTomaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ExamenCondiTomaDao</code>
	 */
	public abstract ExamenCondiTomaDao getExamenCondiTomaDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>TiposConvenioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiposConvenioDao</code>
	 */
	public abstract TiposConvenioDao getTiposConvenioDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>TiposConvenioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiposConvenioDao</code>
	 */
	public abstract UnidadProcedimientoDao getUnidadProcedimientoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>FosygaDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>FosygaDao</code>
	 */
	public abstract FosygaDao getFosygaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>UtilidadesFacturacionDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>UtilidadesFacturacionDao</code>
	 */
	public abstract UtilidadesFacturacionDao getUtilidadesFacturacionDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RegistroEventosCatastroficosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>RegistroEventosCatastroficosDao</code>
	 */
	public abstract RegistroEventosCatastroficosDao getRegistroEventosCatastroficosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>GeneracionAnexosForecatDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>GeneracionAnexosForecatDao</code>
	 */
	public abstract GeneracionAnexosForecatDao getGeneracionAnexosForecatDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>PisosDao</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PisosDao</code>
	 */
	public abstract PisosDao getPisosDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>ComponentesPaquetesDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ComponentesPaquetesDao</code>
	 */
	public abstract ComponentesPaquetesDao getComponentesPaquetesDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>TipoHabitacionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TipoHabitacionDao</code>
	 */
	public abstract TipoHabitacionDao getTipoHabitacionDao();

	/**
	 *Retorna el DAO con el cual el objeto
	 * <code>UtilidadesHistoriaClinicaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>UtilidadesHistoriaClinicaDao</code>
	 */
	public abstract UtilidadesHistoriaClinicaDao getUtilidadesHistoriaClinicaDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>DetalleCoberturaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DetalleCoberturaDao</code>
	 */
	public abstract DetalleCoberturaDao getDetalleCoberturaDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>AlmacenParametrosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AlmacenParametrosDao</code>
	 */
	public abstract AlmacenParametrosDao getAlmacenParametrosDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>HabitacionesDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>HabitacionesDao</code>
	 */
	public abstract HabitacionesDao getHabitacionesDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>ReferenciaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ReferenciaDao</code>
	 */
	public abstract ReferenciaDao getReferenciaDao();

	/**
	 *Retorna el DAO con el cual el objeto
	 * <code>ReportesEstadosCarteraDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ReportesEstadosCarteraDao</code>
	 */
	public abstract ReportesEstadosCarteraDao getReportesEstadosCarteraDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>CoberturasConvenioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CoberturasConvenioDao</code>
	 */
	public abstract CoberturasConvenioDao getCoberturasConvenioDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>TiposUsuarioCamaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiposUsuarioCamaDao</code>
	 */
	public abstract TiposUsuarioCamaDao getTiposUsuarioCamaDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>EdadCarteraDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>EdadCarteraDao</code>
	 */
	public abstract EdadCarteraDao getEdadCarteraDao();

	/**
	 *Retorna el DAO con el cual el objeto
	 * <code>DescuentosComercialesDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DescuentosComercialesDao</code>
	 */
	public abstract DescuentosComercialesDao getDescuentosComercialesDao();

	/**
	 *Retorna el DAO con el cual el objeto
	 * <code>EdadCarteraCapitacionDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>EdadCarteraCapitacionDao</code>
	 */
	public abstract EdadCarteraCapitacionDao getEdadCarteraCapitacionDao();

	/**
	 *Retorna el DAO con el cual el objeto
	 * <code>DetalleInclusionesExclusionesDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>DetalleInclusionesExclusionesDao</code>
	 */
	public abstract DetalleInclusionesExclusionesDao getDetalleInclusionesExclusionesDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>TramiteReferenciaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TramiteReferenciaDao</code>
	 */
	public abstract TramiteReferenciaDao getTramiteReferenciaDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>TiposAmbulanciaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiposAmbulanciaDao</code>
	 */
	public abstract TiposAmbulanciaDao getTiposAmbulanciaDao();

	/**
	 *Retorna el DAO con el cual el objeto
	 * <code>InclusionExclusionConvenioDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>InclusionExclusionConvenioDao</code>
	 */
	public abstract InclusionExclusionConvenioDao getInclusionExclusionConvenioDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ContrarreferenciaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ContrarreferenciaDao</code>
	 */
	public abstract ContrarreferenciaDao getContrarreferenciaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ExcepcionesTarifas1Dao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ExcepcionesTarifas1Dao</code>
	 */
	public abstract ExcepcionesTarifas1Dao getExcepcionesTarifas1Dao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>UtilidadesManejoPacienteDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>UtilidadesManejoPacienteDao</code>
	 */
	public abstract UtilidadesManejoPacienteDao getUtilidadesManejoPacienteDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaReferenciaContrareferenciaDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>ConsultaReferenciaContrareferenciaDao</code>
	 */
	public abstract ConsultaReferenciaContrareferenciaDao getConsultaReferenciaContrareferenciaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CargosDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CargosDao</code>
	 */
	public abstract CargosDao getCargosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>BusquedaBarriosGenericaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>BusquedaBarriosGenericaDao</code>
	 */
	public abstract BusquedaBarriosGenericaDao getBusquedaBarriosGenericaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PaquetizacionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PaquetizacionDao</code>
	 */
	public abstract PaquetizacionDao getPaquetizacionDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>DeudoresCoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DeudoresCoDao</code>
	 */
	public abstract DocumentosGarantiaDao getDocumentosGarantiaDao();
	
	/**
	 * Retorna el DAO con el cual el objeto <code>DeudoresCoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DeudoresCoDao</code>
	 */
	public abstract ApliPagosCarteraPacienteDao getApliPagosCarteraPacienteDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConsentimientoInformado</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsentimientoInformadoDao</code>
	 */
	public abstract ConsentimientoInformadoDao getConsentimientoInformadoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>DistribucionCuentaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DistribucionCuentaDao</code>
	 */
	public abstract DistribucionCuentaDao getDistribucionCuentaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>RevisionCuentaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>RevisionCuentaDao</code>
	 */
	public abstract RevisionCuentaDao getRevisionCuentaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CondicionesXServicioDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CondicionesXServicioDao</code>
	 */
	public abstract CondicionesXServicioDao getCondicionesXServicioDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>ConsultaTarifasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultaTarifasDao</code>
	 */
	public abstract ConsultaTarifasDao getConsultaTarifasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>reporteProcedimientosEsteticosDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return el DAO usado por <code>reporteProcedimientosEsteticosDao</code>
	 */
	public abstract ReporteProcedimientosEsteticosDao getReporteProcedimientosEsteticosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CargosAutomaticosPresupuestoDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>CargosAutomaticosPresupuestoDao</code>
	 */
	public abstract CargosAutomaticosPresupuestoDao getCargosAutomaticosPresupuestoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CensoCamasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CensoCamasDao</code>
	 */
	public abstract CensoCamasDao getCensoCamasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PendienteFacturarDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PendienteFacturarDao</code>
	 */
	public abstract PendienteFacturarDao getPendienteFacturarDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ReliquidacionTarifasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ReliquidacionTarifasDao</code>
	 */
	public abstract ReliquidacionTarifasDao getReliquidacionTarifasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AnulacionCargosFarmaciaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>AnulacionCargosFarmaciaDao</code>
	 */
	public abstract AnulacionCargosFarmaciaDao getAnulacionCargosFarmaciaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultarActivarCamasReservadasDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return el DAO usado por <code>ConsultarActivarCamasReservadasDao</code>
	 */
	public abstract ConsultarActivarCamasReservadasDao getConsultarActivarCamasReservadasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConceptosPagoPoolesDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConceptosPagoPoolesDao</code>
	 */
	public abstract ConceptosPagoPoolesDao getConceptosPagoPoolesDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>FactorConversionMonedasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>FactorConversionMonedasDao</code>
	 */
	public abstract FactorConversionMonedasDao getFactorConversionMonedasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConceptosPagoPoolesXConvenioDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ConceptosPagoPoolesXConvenioDao</code>
	 */
	public abstract ConceptosPagoPoolesXConvenioDao getConceptosPagoPoolesXConvenioDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>UtilConversionMonedasDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>UtilConversionMonedasDao</code>
	 */
	public abstract UtilConversionMonedasDao getUtilConversionMonedasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>UtilidadesConsultaExternaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>UtilidadesConsultaExternaDao</code>
	 */
	public abstract UtilidadesConsultaExternaDao getUtilidadesConsultaExternaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>UtilidadesConsultaExternaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>UtilidadesConsultaExternaDao</code>
	 */
	public abstract ReingresoSalidaHospiDiaDao getReingresoSalidaHospiDiaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ArchivosPlanosColsaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ArchivosPlanosColsaDao</code>
	 */
	public abstract ArchivoPlanoColsaDao getArchivoPlanoColsaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaTerapiasGrupalesDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ConsultaTerapiasGrupalesDao</code>
	 */
	public abstract ConsultaTerapiasGrupalesDao getConsultaTerapiasGrupalesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AsociosXRangoTiempoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AsociosXRangoTiempoDao</code>
	 */
	public abstract AsociosXRangoTiempoDao getAsociosXRangoTiempoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AsocioSalaCirugiaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AsocioSalaCirugiaDao</code>
	 */
	public abstract AsocioSalaCirugiaDao getAsocioSalaCirugiaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AsocioServicioTarifaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AsocioServicioTarifaDao</code>
	 */
	public abstract AsocioServicioTarifaDao getAsocioServicioTarifaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>DespachoPedidoQxDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DespachoPedidoQxDao</code>
	 */
	public abstract DespachoPedidoQxDao getDespachoPedidoQxDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>PacientesEntidadesSubConDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>PacientesEntidadesSubConDao</code>
	 */
	public abstract PacientesEntidadesSubConDao getPacientesEntidadesSubConDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CierreIngresoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CierreIngresoDao</code>
	 */
	public abstract CierreIngresoDao getCierreIngresoDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaCierreAperturaIngresoDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ConsultaCierreAperturaIngresoDao</code>
	 */
	public abstract ConsultaCierreAperturaIngresoDao getConsultaCierreAperturaIngresoDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>LecturaPlanosEntidadesDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>LecturaPlanosEntidadesDao</code>
	 */
	public abstract LecturaPlanosEntidadesDao getLecturaPlanosEntidadesDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>EntidadesSubContratadasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>EntidadesSubContratadasDao</code>
	 */
	public abstract EntidadesSubContratadasDao getEntidadesSubContratadasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ParametrosEntidadesSubContratadasDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>ParametrosEntidadesSubContratadasDao</code>
	 */
	public abstract ParametrosEntidadesSubContratadasDao getParametrosEntidadesSubContratadasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CargosDirectosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CargosDirectosDao</code>
	 */
	public abstract CargosDirectosDao getCargosDirectosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>SeccionesDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>SeccionesDao</code>
	 */
	public abstract SeccionesDao getSeccionesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>FarmaciaCentroCostoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>FarmaciaCentroCostoDao</code>
	 */
	public abstract FarmaciaCentroCostoDao getFarmaciaCentroCostoDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>MovimientosAlmacenesDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>MovimientosAlmacenesDao</code>
	 */

	public abstract MovimientosAlmacenesDao getMovimientosAlmacenesDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>TotalOcupacionCamasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TotalOcupacionCamasDao</code>
	 */

	public abstract TotalOcupacionCamasDao getTotalOcupacionCamasDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>TotalOcupacionCamasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ListadoCamasHospitalizacionDao</code>
	 */

	public abstract ListadoCamasHospitalizacionDao getListadoCamasHospitalizacionDao();

	/**
	 *Retorna el DAO con el cual el objeto
	 * <code>ArticulosConsumidosPacientesDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ArticulosConsumidosPacientesDao</code>
	 */

	public abstract ArticulosConsumidosPacientesDao getArticulosConsumidosPacientesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ArticulosPorAlmacenDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ArticulosPorAlmacenDao</code>
	 */

	public abstract ArticulosPorAlmacenDao getArticulosPorAlmacenDao();

	/**
	 *Retorna el DAO con el cual el objeto <code>OcupacionDiariaCamasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>OcupacionDiariaDao</code>
	 */

	public abstract OcupacionDiariaCamasDao getOcupacionDiariaCamasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ImpListaConteoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ImpListaConteoDao</code>
	 */
	public abstract ImpListaConteoDao getImpListaConteoDao();

	/**
	 * 
	 * @return
	 */
	public abstract PreparacionTomaInventarioDao getPreparacionTomaInventarioDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>LiquidacionServiciosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>LiquidacionServiciosDao</code>
	 */
	public abstract LiquidacionServiciosDao getLiquidacionServiciosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadesSalasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>UtilidadesSalasDao</code>
	 */
	public abstract UtilidadesSalasDao getUtilidadesSalasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadesSalasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>UtilidadesSalasDao</code>
	 */
	public abstract SignosVitalesDao getSignosVitalesDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>MotivoCierreAperturaIngresosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>MotivoCierreAperturaIngresosDao</code>
	 */
	public abstract MotivoCierreAperturaIngresosDao getMotivoCierreAperturaIngresosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RegistroConteoInventarioDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>RegistroConteoInventarioDao</code>
	 */

	public abstract RegistroConteoInventarioDao getRegistroConteoInventarioDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>SustitutosNoPosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>SustitutosNoPosDao</code>
	 */
	public abstract SustitutosNoPosDao getSustitutosNoPosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ComparativoUltimoConteoDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ComparativoUltimoConteoDao</code>
	 */
	public abstract ComparativoUltimoConteoDao getComparativoUltimoConteoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>InterfazSistemaUnoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>InterfazSistemaUnoDao</code>
	 */
	public abstract InterfazSistemaUnoDao getInterfazSistemaUnoDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaInventarioFisicoArticulosDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>ConsultaInventarioFisicoArticulosDao</code>
	 */
	public abstract ConsultaInventarioFisicoArticulosDao getConsultaInventarioFisicoArticulosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AjustesXInventarioFisicoDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>AjustesXInventarioFisicoDao</code>
	 */
	public abstract AjustesXInventarioFisicoDao getAjustesXInventarioFisicoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConsumosCentrosCostoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsumosCentrosCostoDao</code>
	 */
	public abstract ConsumosCentrosCostoDao getConsumosCentrosCostoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>EventosDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>EventosDao</code>
	 */
	public abstract EventosDao getEventosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaDevolucionPedidosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ConsumosCentrosCostoDao</code>
	 */
	public abstract ConsultaDevolucionPedidosDao getConsultaDevolucionPedidosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>GasesHojaAnestesiaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>GasesHojaAnestesiaDao</code>
	 */
	public abstract GasesHojaAnestesiaDao getGasesHojaAnestesiaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaDevolucionInventarioPacienteDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>ConsultaDevolucionInventarioPacienteDao</code>
	 */
	public abstract ConsultaDevolucionInventarioPacienteDao getConsultaDevolucionInventarioPacienteDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>MonitoreoHemodinamicaDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>MonitoreoHemodinamicaDao</code>
	 */
	public abstract MonitoreoHemodinamicaDao getMonitoreoHemodinamicaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TecnicaAnestesiaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TecnicaAnestesiaDao</code>
	 */
	public abstract TecnicaAnestesiaDao getTecnicaAnestesiaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PosicionesAnestesiaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PosicionesAnestesiaDao</code>
	 */
	public abstract PosicionesAnestesiaDao getPosicionesAnestesiaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>InfoGeneralHADao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>InfoGeneralHADao</code>
	 */
	public abstract InfoGeneralHADao getInfoGeneralHADao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>TiposTratamientosOdontologicosDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return el DAO usado por <code>TiposTratamientosOdontologicosDao</code>
	 */
	public abstract TiposTratamientosOdontologicosDao getTiposTratamientosOdontologicosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ViasAereasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ViasAereasDao</code>
	 */
	public abstract ViasAereasDao getViasAereasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>CargosDirectosCxDytDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CargosDirectosCxDytDao</code>
	 */
	public abstract CargosDirectosCxDytDao getCargosDirectosCxDytDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>FormatoJustArtNoposDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>FormatoJustArtNoposDao</code>
	 */
	public abstract FormatoJustArtNoposDao getFormatoJustArtNoposDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>FormatoJustServNoposDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>FormatoJustServNoposDao</code>
	 */
	public abstract FormatoJustServNoposDao getFormatoJustServNoposDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>InfusionesHADao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>InfusionesHADao</code>
	 */
	public abstract IntubacionesHADao getInfusionesHADao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AprobacionAnulacionDevolucionesDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return el DAO usado por <code>AprobacionAnulacionDevolucionesDao</code>
	 */
	public abstract AprobacionAnulacionDevolucionesDao getAprobacionAnulacionDevolucionesDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AprobacionAnulacionFacturasVariasDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>AprobacionAnulacionFacturasVariasDao</code>
	 */
	public abstract AprobacionAnulacionFacturasVariasDao getAprobacionAnulacionFacturasVariasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PagosFacturasVariasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PagosFacturasVariasDao</code>
	 */
	public abstract PagosFacturasVariasDao getPagosFacturasVariasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AprobacionAnulacionPagosFacturasVariasDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>AprobacionAnulacionPagosFacturasVariasDao</code>
	 */
	public abstract AprobacionAnulacionPagosFacturasVariasDao getAprobacionAnulacionPagosFacturasVariasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaImpresionPagosFacturasVariasDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>ConsultaImpresionPagosFacturasVariasDao</code>
	 */
	public abstract ConsultaImpresionPagosFacturasVariasDao getConsultaImpresionPagosFacturasVariasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RegistroDevolucionRecibosCajaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>RegistroDevolucionRecibosCajaDao</code>
	 */
	public abstract RegistroDevolucionRecibosCajaDao getRegistroDevolucionRecibosCajaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaImpresionDevolucionDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ConsultaImpresionDevolucionDao</code>
	 */
	public abstract ConsultaImpresionDevolucionDao getConsultaImpresionDevolucionDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>MotivosDevolucionRecibosCajaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>MotivosDevolucionRecibosCajaDao</code>
	 */
	public abstract MotivosDevolucionRecibosCajaDao getMotivosDevolucionRecibosCajaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RegistroRipsCargosDirectosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>RegistroRipsCargosDirectosDao</code>
	 */
	public abstract RegistroRipsCargosDirectosDao getRegistroRipsCargosDirectosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CopiarTarifasEsquemaTarifarioDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>CopiarTarifasEsquemaTarifarioDao</code>
	 */
	public abstract CopiarTarifasEsquemaTarifarioDao getCopiarTarifasEsquemaTarifarioDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaTarifasArticulosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ConsultaTarifasArticulosDao</code>
	 */
	public abstract ConsultaTarifasArticulosDao getConsultaTarifasArticulosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ActualizacionAutomaticaEsquemaTarifarioDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>ActualizacionAutomaticaEsquemaTarifarioDao</code>
	 */
	public abstract ActualizacionAutomaticaEsquemaTarifarioDao getActualizacionAutomaticaEsquemaTarifarioDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>UtilidadJustificacionPendienteArtServDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>UtilidadJustificacionPendienteArtServDao</code>
	 */
	public abstract UtilidadJustificacionPendienteArtServDao getUtilidadJustificacionPendienteArtServDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AccesosVascularesHADao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AccesosVascularesHADao</code>
	 */
	public abstract AccesosVascularesHADao getAccesosVascularesHADao();

	/**
	 * Retorna el DAO con el cual el objeto <code>EscalasDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>EscalasDao</code>
	 */
	public abstract EscalasDao getEscalasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>GeneracionModificacionAjustesFacturasVariasDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>GeneracionModificacionAjustesFacturasVariasDao</code>
	 */
	public abstract GeneracionModificacionAjustesFacturasVariasDao getGeneracionModificacionAjustesFacturasVariasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ValoracionesDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ValoracionesDao</code>
	 */
	public abstract ValoracionesDao getValoracionesDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ParametrizacionComponentesDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ParametrizacionComponentesDao</code>
	 */
	public abstract ParametrizacionComponentesDao getParametrizacionComponentesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>RecaudoCarteraEventoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>RecaudoCarteraEventoDao</code>
	 */
	public abstract RecaudoCarteraEventoDao getRecaudoCarteraEventoDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>MotivosSatisfaccionInsatisfaccionDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>MotivosSatisfaccionInsatisfaccionDao</code>
	 */
	public abstract MotivosSatisfaccionInsatisfaccionDao getMotivosSatisfaccionInsatisfaccionDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PlantillasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PlantillasDao</code>
	 */
	public abstract PlantillasDao getPlantillasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>EncuestaCalidadAtencionDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>EncuestaCalidadAtencionDao</code>
	 */
	public abstract EncuestaCalidadAtencionDao getEncuestaCalidadAtencionDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ParamArchivoPlanoIndCalidadDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ParamArchivoPlanoIndCalidadDao</code>
	 */
	public abstract ParamArchivoPlanoIndCalidadDao getParamArchivoPlanoIndCalidadDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ParamArchivoPlanoIndCalidadDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ParamArchivoPlanoIndCalidadDao</code>
	 */
	public abstract EventosAdversosDao getEventosAdversosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RegistroEventosAdversosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract RegistroEventosAdversosDao getRegistroEventosAdversosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>GeneracionArchivoPlanoIndicadoresCalidadDao</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return
	 */
	public abstract GeneracionArchivoPlanoIndicadoresCalidadDao getGeneracionArchivoPlanoIndicadoresCalidadDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ParametrizacionPlantillasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract ParametrizacionPlantillasDao getParametrizacionPlantillasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>UnidadAgendaUsuarioCentroDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>UnidadAgendaUsuarioCentroDao</code>
	 */
	public abstract UnidadAgendaUsuarioCentroDao getUnidadAgendaUsuarioCentroDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RegistroResumenParcialHistoriaClinicaDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return
	 */
	public abstract RegistroResumenParcialHistoriaClinicaDao getRegistroResumenParcialHistoriaClinicaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AperturaIngresosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract AperturaIngresosDao getAperturaIngresosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AperturaIngresosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ConsultaPreingresosDao getConsultaPreingresosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>MotivosCancelacionCitaDao</code> interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract MotivosCancelacionCitaDao getMotivosCancelacionCitaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>LiberacionCamasIngresosCerradosDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return
	 */
	public abstract LiberacionCamasIngresosCerradosDao getLiberacionCamasIngresosCerradosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ValoracionPacientesCuidadosEspecialesDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return
	 */
	public abstract ValoracionPacientesCuidadosEspecialesDao getValoracionPacientesCuidadosEspecialesDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>MotivosCancelacionCitaDao</code> interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract Epicrisis1Dao getEpicrisis1Dao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AsignacionCamaCuidadoEspecialDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract AsignacionCamaCuidadoEspecialAPisoDao getAsignacionCamaCuidadoEspecialAPisoDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AsignacionCamaCuidadoEspecialDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract AsignacionCamaCuidadoEspecialDao getAsignacionCamaCuidadoEspecialDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PacientesConAtencionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract PacientesConAtencionDao getPacientesConAtencionDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PacientesConAtencionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ConsultaProgramacionCirugiasDao getConsultaProgramacionCirugiasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>UtilidadesOrdenesMedicasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract UtilidadesOrdenesMedicasDao getUtilidadesOrdenesMedicasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaImpresionMaterialesQxDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract ConsultaImpresionMaterialesQxDao getConsultaImpresionMaterialesQxDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>DevolucionPedidoQxDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract DevolucionPedidoQxDao getDevolucionPedidoQxDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>TrasladoSolicitudesPorTransplantesDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return
	 */
	public abstract TrasladoSolicitudesPorTransplantesDao getTrasladoSolicitudesPorTransplantesDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultarIngresosPorTransplantesDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return
	 */
	public abstract ConsultarIngresosPorTransplantesDao getConsultarIngresosPorTransplantesDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CostoInventarioPorFacturarDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract CostoInventarioPorFacturarDao getCostoInventarioPorFacturarDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>EvolucionesDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract EvolucionesDao getEvolucionesDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CostoVentasPorInventarioDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract CostoVentasPorInventarioDao getCostoVentasPorInventarioDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CostoInventariosPorAlmacenDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract CostoInventariosPorAlmacenDao getCostoInventariosPorAlmacenDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsolidadoFacturacionDao</code> interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ConsolidadoFacturacionDao getConsolidadoFacturacionDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>FacturadosPorConvenioDao</code> interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract FacturadosPorConvenioDao getFacturadosPorConvenioDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PacientesPorFacturarDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract PacientesPorFacturarDao getPacientesPorFacturarDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PacientesPorFacturarDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ServiciosXTipoTratamientoOdontologicoDao getServiciosXTipoTratamientoOdontologicoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PacientesPorFacturarDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract DiagnosticosOdontologicosATratarDao getDiagnosticosOdontologicosATratarDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaPaquetesFacturadosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract ConsultaPaquetesFacturadosDao getConsultaPaquetesFacturadosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ValoresTipoReporteDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ValoresTipoReporteDao getValoresTipoReporteDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaMovimientoDeudorDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract ConsultaMovimientoDeudorDao getConsultaMovimientoDeudorDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>EstadisticasServiciosDao</code> interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract EstadisticasServiciosDao getEstadisticasServiciosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>EstadisticasServiciosDao</code> interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract CalidadAtencionDao getCalidadAtencionDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>UsuariosAutorizarAnulacionFacturasDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return
	 */
	public abstract UsuariosAutorizarAnulacionFacturasDao getUsuariosAutorizarAnulacionFacturasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaMovimientoDeudorDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract ConsultaMovimientoFacturaDao getConsultaMovimientoFacturaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ReporteMortalidadDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ReporteMortalidadDao getReporteMortalidadDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>SolicitarAnulacionFacturasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract SolicitarAnulacionFacturasDao getSolicitarAnulacionFacturasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaSaldosCierresInventariosDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return
	 */
	public abstract ConsultaSaldosCierresInventariosDao getConsultaSaldosCierresInventariosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ReporteEstadisticoConsultaExDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract ReporteEstadisticoConsultaExDao getReporteEstadisticoConsultaExDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaCostoArticulosDao</code> interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ConsultaCostoArticulosDao getConsultaCostoArticulosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AutorizarAnulacionFacturasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract AutorizarAnulacionFacturasDao getAutorizarAnulacionFacturasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AprobacionAnulacionAjustesFacturasVariasDao</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return
	 */
	public abstract AprobacionAnulacionAjustesFacturasVariasDao getAprobacionAnulacionAjustesFacturasVariasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConceptosGeneralesDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ConceptosGeneralesDao getConceptosGeneralesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConceptosGeneralesDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ConsultaImpresionAjustesFacturasVariasDao getConsultaImpresionAjustesFacturasVariasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ReporteEgresosEstanciasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract ReporteEgresosEstanciasDao getReporteEgresosEstanciasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConceptosEspecificosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ConceptosEspecificosDao getConceptosEspecificosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>VentasCentroCostoDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract VentasCentroCostoDao getVentasCentroCostoDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>VentasEmpresaConvenioDao</code> interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract VentasEmpresaConvenioDao getVentasEmpresaConvenioDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsumosPorFacturarPacientesHospitalizadosDao</code> interactua con
	 * la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ConsumosPorFacturarPacientesHospitalizadosDao getConsumosPorFacturarPacientesHospitalizadosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ReporteReferenciaInternaDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract ReporteReferenciaInternaDao getReporteReferenciaInternaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>EstadisticasIngresosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract EstadisticasIngresosDao getEstadisticasIngresosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>Servicios_ArticulosIncluidosEnOtrosProcedimientosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract Servicios_ArticulosIncluidosEnOtrosProcedimientosDao getServicios_ArticulosIncluidosEnOtrosProcedimientosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>EstadisticasIngresosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ReporteReferenciaExternaDao getReporteReferenciaExternaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>GrupoEspecialArticulosDao</code> interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract GrupoEspecialArticulosDao getGrupoEspecialArticulosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>TextosRespuestaProcedimientosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract TextosRespuestaProcedimientosDao getTextosRespuestaProcedimientosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>TextosRespuestaProcedimientosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract SolicitudMedicamentosXDespacharDao getSolicitudMedicamentosXDespacharDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>MedicamentosControladosAdministradosDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return
	 */
	public abstract MedicamentosControladosAdministradosDao getMedicamentosControladosAdministradosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>TotalFacturadoConvenioContratoDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return
	 */
	public abstract TotalFacturadoConvenioContratoDao getTotalFacturadoConvenioContratoDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConsumosXFacturarDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ConsumosXFacturarDao getConsumosXFacturarDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultaMovimientosConsignacionesDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return
	 */
	public abstract ConsultaMovimientosConsignacionesDao getConsultaMovimientosConsignacionesDao();

	/**
	 *Retorna el DAO con el cual el objeto
	 * <code>MovimientosAlmacenConsignacinDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>MovimientosAlmacenConsignacionDao</code>
	 */

	public abstract MovimientosAlmacenConsignacionDao getMovimientosAlmacenConsignacionDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PlanosFURIPSDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract PlanosFURIPSDao getPlanosFURIPSDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ParametrosFirmasImpresionRespDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return
	 */
	public abstract ParametrosFirmasImpresionRespDao getParametrosFirmasImpresionRespDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConceptosRespuestasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ConceptosRespuestasDao getConceptosRespuestasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultasBirtDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return
	 */
	public abstract ConsultasBirtDao getConsultasBirtDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>GlosasDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>GlosasDao</code>
	 */
	public abstract GlosasDao getGlosasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>PriogramacionAreaPacienteDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ProgramacionAreaPacienteDao</code>
	 */
	public abstract ProgramacionAreaPacienteDao getProgramacionAreaPacienteDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ProgramacionCuidadoEnferDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ProgramacionCuidadoEnferDao</code>
	 */
	public abstract ProgramacionCuidadoEnferDao getProgramacionCuidadoEnferDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConfirmarAnularGlosasDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConfirmarAnularGlosasDao</code>
	 */
	public abstract ConfirmarAnularGlosasDao getConfirmarAnularGlosasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>BusquedaGlosasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>BusquedaGlosasDao</code>
	 */
	public abstract BusquedaGlosasDao getBusquedaGlosasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConsultarImpFacAudiDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsultarImpFacAudiDao</code>
	 */
	public abstract ConsultarImpFacAudiDao getConsultarImpFacAudiDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RegistrarModificarGlosasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>RegistrarModificarGlosasDao</code>
	 */
	public abstract RegistrarModificarGlosasDao getRegistrarModificarGlosasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>SoportesFacturasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>SoportesFacturasDao</code>
	 */
	public abstract SoportesFacturasDao getSoportesFacturasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AprobarGlosasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AprobarGlosasDao</code>
	 */
	public abstract AprobarGlosasDao getAprobarGlosasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ImpresionSoportesFacturasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ImpresionSoportesFacturasDao</code>
	 */
	public abstract ImpresionSoportesFacturasDao getImpresionSoportesFacturasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>getConsultaProgramacionCuidadosAreaDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>getConsultaProgramacionCuidadosAreaDao</code>
	 */
	public abstract ConsultaProgramacionCuidadosAreaDao getConsultaProgramacionCuidadosAreaDao();

	/**
	 * Retorna el DAO con el cual el Objeto
	 * <code>ConsultaProgramacionCuidadosPacienteDao</code> interacuta con la
	 * fuente de datos
	 * 
	 * @return el DAO usado por
	 *         <code> getConsultaProgramacionCuidadosPacienteDao</code>
	 */
	public abstract ConsultaProgramacionCuidadosPacienteDao getConsultaProgramacionCuidadosPacienteDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>RegistrarRespuestaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>RegistrarRespuestaDao</code>
	 */
	public abstract RegistrarRespuestaDao getRegistrarRespuestaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultarImprimirGlosasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>AprobarGlosasDao</code>
	 */
	public abstract ConsultarImprimirGlosasDao getConsultarImprimirGlosasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>BusquedaRespuestaGlosasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>BusquedaGlosasDao</code>
	 */
	public abstract BusquedaRespuestaGlosasDao getBusquedaRespuestaGlosasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AprobarAnularRespuestasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>AprobarAnularRespuestasDao</code>
	 */
	public abstract AprobarAnularRespuestasDao getAprobarAnularRespuestasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>RadicarRespuestasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>RadicarRespuestasDao</code>
	 */
	public abstract RadicarRespuestasDao getRadicarRespuestasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>DetalleGlosasDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DetalleGlosasDao</code>
	 */
	public abstract DetalleGlosasDao getDetalleGlosasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultarImprimirRespuestasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>AprobarGlosasDao</code>
	 */
	public abstract ConsultarImprimirRespuestasDao getConsultarImprimirRespuestasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ConsultarImprimirRespuestasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>AprobarGlosasDao</code>
	 */
	public abstract ConsultarImprimirGlosasSinRespuestaDao getConsultarImprimirGlosasSinRespuestaDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>ExcepcionesCCInterconsultasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>ExcepcionesCCInterconsultasDao</code>
	 */
	public abstract ExcepcionesCCInterconsultasDao getExcepcionesCCInterconsultasDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RegistroEnvioInfAtencionIniUrgDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return el DAO usado por <code>RegistroEnvioInfAtencionIniUrgDao</code>
	 */
	public abstract RegistroEnvioInfAtencionIniUrgDao getRegistroEnvioInfAtencionIniUrgDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>UtilidadesJustificacionNoPosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>UtilidadesJustificacionNoPosDao</code>
	 */
	public abstract UtilidadesJustificacionNoPosDao getUtilidadesJustificacionNoPosDao();

	/**
	 * Retorna el Dao el cual el objeto
	 * <code>RegistroEnvioInformInconsisenBDDao</code> interactua con la fuente
	 * de datos.
	 * 
	 * @return el DAO usado por <code>RegistroEnvioInformInconsisenBDDao</code>
	 * 
	 */
	public abstract RegistroEnvioInformInconsisenBDDao getRegistroEnvioInformInconsisenBDDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ConsultaEnvioInconsistenciasenBDDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ConsultaEnvioInconsistenciasenBDDao</code>
	 */
	public abstract ConsultaEnvioInconsistenciasenBDDao getConsultaEnvioInconsistenciasenBDDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>AutorizacionesDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>AutorizacionesDao</code>
	 */
	public abstract AutorizacionesDao getAutorizacionesDao();

	/**
	 * Retorna el Dao el cual el objeto
	 * <code>MotivosAnulacionCondonacionDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>MotivosAnulacionCondonacionDao</code>
	 */
	public abstract MotivosAnulacionCondonacionDao getMotivosAnulacionCondonacionDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>AnulacionCondonacionMultasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>AnulacionCondonacionMultasDao</code>
	 */
	public abstract AnulacionCondonacionMultasDao getAnulacionCondonacionMultasDao();  
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>MultasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>MultasDao</code>
	 */
	public abstract MultasDao getMultasDao();	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>CentrosCostoEntidadesSubcontratadasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>CentrosCostoEntidadesSubcontratadasDao</code>
	 */
	public abstract CentrosCostoEntidadesSubcontratadasDao getCentrosCostoEntidadesSubcontratadasDao();	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ConsultaMultasAgendaCitasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ConsultaMultasAgendaCitasDao</code>
	 */
	public abstract ConsultaMultasAgendaCitasDao getConsultaMultasAgendaCitasDao();	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>CoberturasEntidadesSubcontratadasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>CoberturasEntidadesSubcontratadasDao</code>
	 */
	public abstract CoberturasEntidadesSubcontratadasDao getCoberturasEntidadesSubcontratadasDao();	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>AutorizacionesEntidadesSubcontratadasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>AutorizacionesEntidadesSubcontratadasDao</code>
	 */
	public abstract AutorizacionesEntidadesSubcontratadasDao getAutorizacionesEntidadesSubcontratadasDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ConsultarImprimirAutorizacionesEntSubcontratadasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ConsultarImprimirAutorizacionesEntSubcontratadasDao</code>
	 */
	public abstract ConsultarImprimirAutorizacionesEntSubcontratadasDao getConsultarImprimirAutorizacionesEntSubcontratadasDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ProrrogarAnularAutorizacionesEntSubcontratadasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ProrrogarAnularAutorizacionesEntSubcontratadasDao</code>
	 */
	public abstract ProrrogarAnularAutorizacionesEntSubcontratadasDao getProrrogarAnularAutorizacionesEntSubcontratadasDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>BusquedaDeudoresGenericaDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>BusquedaDeudoresGenericaDao</code>
	 */
	public abstract BusquedaDeudoresGenericaDao getBusquedaDeudoresGenericaDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>IngresarModificarContratosEntidadesSubcontratadas</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>CentrosCostoEntidadesSubcontratadasDao</code>
	 */
	public abstract IngresarModificarContratosEntidadesSubcontratadasDao getIngresarModificarContratosEntidadesSubcontratadasDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ConsultarContratosEntidadesSubcontratadas</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ConsultarContratosEntidadesSubcontratadasDao</code>
	 */
	public abstract ConsultarContratosEntidadesSubcontratadasDao getConsultarContratosEntidadesSubcontratadasDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>GeneracionTarifasPendientesEntSubDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>GeneracionTarifasPendientesEntSubDao</code>
	 */
	public abstract GeneracionTarifasPendientesEntSubDao getGeneracionTarifasPendientesEntSubDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ResponderConsultasEntSubcontratadasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ResponderConsultasEntSubcontratadasDao</code>
	 */
	public abstract ResponderConsultasEntSubcontratadasDao getResponderConsultasEntSubcontratadasDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>paramInterfazSistema1EDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>paramInterfazSistema1EDao</code>
	 */
	public abstract ParamInterfazSistema1EDao getParamInterfazSistema1EDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>GeneracionInterfaz1EDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>GeneracionInterfaz1EDao</code>
	 */
	public abstract GeneracionInterfaz1EDao getGeneracionInterfaz1EDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>GeneracionInterfaz1EDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>GeneracionInterfaz1EDao</code>
	 */
	public abstract UtilidadesInterfazDao getUtilidadesInterfazDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>DesmarcarDocProcesadosDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>DesmarcarDocProcesadosDao</code>
	 */
	public abstract DesmarcarDocProcesadosDao getDesmarcarDocProcesadosDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ReporteMovTipoDocDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ReporteMovTipoDocDao</code>
	 */
	public abstract ReporteMovTipoDocDao getReporteMovTipoDocDao();
	
	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>EspecilidadesDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>EspecilidadesDao</code>
	 */
	public abstract EspecialidadesDao getEspecialidadesDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ConsultaInterfazSistema1EDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ConsultaInterfazSistema1EDao</code>
	 */
	public abstract ConsultaInterfazSistema1EDao getConsultaInterfazSistema1EDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ConceptosRetencionDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ConceptosRetencionDao</code>
	 */
	public abstract ConceptosRetencionDao getConceptosRetencionDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>TiposRetencionDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>TiposRetencionDao</code>
	 */
	public abstract TiposRetencionDao getTiposRetencionDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>UtilidadesFacturasVariasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>UtilidadesFacturasVariasDao</code>
	 */
	public abstract UtilidadesFacturasVariasDao getUtilidadesFacturasVariasDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ConsultarRefinanciarCuotaPacienteDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ConsultarRefinanciarCuotaPacienteDao</code>
	 */
	public abstract ConsultarRefinanciarCuotaPacienteDao getConsultarRefinanciarCuotaPacienteDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>UtilidadesFacturasVariasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>UtilidadesFacturasVariasDao</code>
	 */
	public abstract DatosFinanciacionDao getDatosFinanciacionDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>PazYSalvoCarteraPacienteDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>PazYSalvoCarteraPacienteDao</code>
	 */
	public abstract PazYSalvoCarteraPacienteDao getPazYSalvoCarteraPacienteDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>EdadCarteraPacienteDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>EdadCarteraPacienteDao</code>
	 */
	public abstract EdadCarteraPacienteDao getEdadCarteraPacienteDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>BusquedaConvencionesOdontologicasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>BusquedaConvencionesOdontologicasDao</code>
	 */
	
	public abstract BusquedaConvencionesOdontologicasDao getBusquedaConvencionesOdontologicasDao();
	
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ConvencionesOdontologicasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ConvencionesOdontologicasDao</code>
	 */
	public abstract ConvencionesOdontologicasDao getConvencionesOdontologicasDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>HallazgosOdontologicosDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>HallazgosOdontologicosDao</code>
	 */
	public abstract HallazgosOdontologicosDao getHallazgosOdontologicosDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>MotivosCitaDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>MotivosCitaDao</code>
	 */
	public abstract MotivosCitaDao getMotivosCitaDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>MotivosAtencionOdontologicaDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>MotivosAtencionOdontologicaDao</code>
	 */
	public abstract MotivosAtencionOdontologicaDao getMotivosAtencionOdontologicaDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ServiAdicionalesXProfAtenOdontoDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ServiAdicionalesXProfAtenOdontoDao</code>
	 */
	public abstract ServiAdicionalesXProfAtenOdontoDao getServiAdicionalesXProfAtenOdontoDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>getGenerarAgendaOdontologicaDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>getGenerarAgendaOdontologicaDao</code>
	 */
	public abstract GenerarAgendaOdontologicaDao getGenerarAgendaOdontologicaDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>getReportePagosCarteraPacienteDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>getReportePagosCarteraPacienteDao</code>
	 */
	public abstract ReportePagosCarteraPacienteDao getReportePagosCarteraPacienteDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>getReportePagosCarteraPacienteDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>getReportePagosCarteraPacienteDao</code>
	 */
	public abstract UtilidadOdontologiaDao  getUtilidadOdontologiaDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>getGenerarAgendaOdontologicaDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>getGenerarAgendaOdontologicaDao</code>
	 */
	public abstract AgendaOdontologicaDao getAgendaOdontologicaDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>IngresoPacienteOdontologiaDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>IngresoPacienteOdontologiaDao</code>
	 */
	public abstract IngresoPacienteOdontologiaDao getIngresoPacienteOdontologiaDao();
	
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>getRegistrarConciliacionDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>getRegistrarConciliacionDao</code>
	 */
	public abstract RegistrarConciliacionDao getRegistrarConciliacionDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>getCitaOdontologicaDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>getCitaOdontologicaDao</code>
	 */
	public abstract CitaOdontologicaDao getCitaOdontologicaDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>UtilidadesGlosasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>UtilidadesGlosasDao</code>
	 */
	public abstract UtilidadesGlosasDao getUtilidadesGlosasDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>BusquedaConciliacionesDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>BusquedaConciliacionesDao</code>
	 */
	public abstract BusquedaConciliacionesDao getBusquedaConciliacionesDao();
	
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>VerResumenOdontologicoDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>VerResumenOdontologicoDao</code>
	 */
	public abstract VerResumenOdontologicoDao getVerResumenOdontologicoDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ReporteEstadoCarteraGlosasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ReporteEstadoCarteraGlosasDao</code>
	 */
	public abstract ReporteEstadoCarteraGlosasDao getReporteEstadoCarteraGlosasDao();
	
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ReporteFacturasReiteradasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ReporteFacturasReiteradasvDao</code>
	 */
	public abstract ReporteFacturasReiteradasDao getReporteFacturasReiteradasDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ReporteFacturacionEventoRadicarDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ReporteFacturacionEventoRadicarDao</code>
	 */
	public abstract ReporteFacturacionEventoRadicarDao getReporteFacturacionEventoRadicarDao();
	
	/**
	 * Retorna el Dao el cual el objeto
	 * <code>ReporteFacturasVencidasNoObjetadasDao</code> interactua con la fuente
	 * de datos.
	 * @return el DAO usado por <code>ReporteFacturasVencidasNoObjetadasDao</code>
	 */
	public abstract ReporteFacturasVencidasNoObjetadasDao getReporteFacturasVencidasNoObjetadasDao();
	
	/*************************************** MANIZALES ***********************************************/

	/**
	 * Retorna el DAO con el cual el objeto <code>SubirPacienteDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>SubirPaciente</code>
	 */
	public abstract SubirPacienteDao getSubirPacienteDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ExcepcionNivelDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ExcepcionNivelDao</code>
	 */
	public abstract ExcepcionNivelDao getExcepcionNivelDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AjusteCxCDao</code> interactua
	 * con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AjusteCxCDao</code>
	 */
	public abstract AjusteCxCDao getAjusteCxCDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AprobacionAjustesDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AprobacionAjustesDao</code>
	 */
	public abstract AprobacionAjustesDao getAprobacionAjustesDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PagosCapitacionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PagosCapitacion</code>
	 */
	public abstract PagosCapitacionDao getPagosCapitacionDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>PacientesConEgresoPorFacturarDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>PacientesConEgresoPorFacturarDao</code>
	 */
	public abstract PacientesConEgresoPorFacturarDao getPacientesConEgresoPorFacturarDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ContratoCargue</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ContratoCargue</code>
	 */
	public abstract ContratoCargueDao getContratoCargueDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CalificacionesXCumpliMetasDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>CalificacionesXCumpliMetas</code>
	 */
	public abstract CalificacionesXCumpliMetasDao getCalificacionesXCumpliMetasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ProgramaArticulo</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ProgramaArticulo</code>
	 */
	public abstract ProgramaArticuloPypDao getProgramaArticuloPypDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>TipoCalificacionPyp</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TipoCalificacionPyp</code>
	 */
	public abstract TipoCalificacionPypDao getTipoCalificacionPypDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>AntecedentesVacunas</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AntecedentesVacunas</code>
	 */
	public abstract AntecedentesVacunasDao getAntecedentesVacunasDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>UnidadPagoVacunas</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AntecedentesVacunas</code>
	 */
	public abstract UnidadPagoDao getUnidadPagoDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>RegistroAccidentesTransitoDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>RegistroAccidentesTransitoDao</code>
	 */
	public abstract RegistroAccidentesTransitoDao getRegistroAccidentesTransitoDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CrecimientoDesarrolloDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>CrecimientoDesarrollo</code>
	 */
	public abstract CrecimientoDesarrolloDao getCrecimientoDesarrolloDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>UtilidadesAdministracionDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>UtilidadesAdministracionDao</code>
	 */
	public abstract UtilidadesAdministracionDao getUtilidadesAdministracionDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadesAdministracionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>UtilidadesAdministracionDao</code>
	 */
	
	public abstract TarjetaClienteDao getTarjetaClienteDao();
	
	/**
	 * Retorna el DAO con el cual el objeto <code>TarjetaClienteDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TarjetaClienteDao</code>
	 */
	
	public abstract EmisionBonosDescDao getEmisionBonosDescDao();
	
	
	/**
	 * Retorna el DAO con el cual el objeto <code>EmisionBonosDescDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>EmisionBonosDescDao</code>
	 */
	public abstract TiemposHojaAnestesiaDao getTiemposHojaAnestesiaDao();
	
	/**
	 * Retorna el DAO con el cual el objeto <code>TiemposHojaAnestesiaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>TiemposHojaAnestesiaDao</code>
	 */
	
	
	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>JustificacionNoPosServDao</code> interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>JustificacionNoPosServDao</code>
	 */
	public abstract JustificacionNoPosServDao getJustificacionNoPosServDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>PacientesHospitalizadosDao</code> interactua con la fuente de
	 * datos.
	 * 
	 * @return el DAO usado por <code>PacientesHospitalizadosDao</code>
	 */
	public abstract PacientesHospitalizadosDao getPacientesHospitalizadosDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>ConsumosFacturadosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ConsumosFacturadosDao</code>
	 */
	public abstract ConsumosFacturadosDao getConsumosFacturadosDao();

	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>AsignarCitasControlPostOperatorioDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>AsignarCitasControlPostOperatorioDao</code>
	 */
	public abstract AsignarCitasControlPostOperatorioDao getAsignarCitasControlPostOperatorioDao();
	
	/**
	 * Retorna el DAO con el cual el objeto
	 * <code>CargosEntidadesSubcontratadasDao</code> interactua con la
	 * fuente de datos.
	 * 
	 * @return el DAO usado por
	 *         <code>CargosEntidadesSubcontratadasDao</code>
	 */
	public abstract CargosEntidadesSubcontratadasDao getCargosEntidadesSubcontratadasDao();
	
	/**
	 * Retorna el DAO con el cual el objeto <code>MotivosDesceuntosDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>MotivosDescuentosDao</code>
	 */
	public abstract MotivosDescuentosDao getMotivosDescuentosDao();
	
	/**
	 * Retorna el DAO con el cual el objeto <code>DetConceptosRetencionDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>DetConceptosRetencionDao</code>
	 */
	public abstract DetConceptosRetencionDao getDetConceptosRetencion();

	
	public abstract HistoricoAtencionesDao getHistoricoAtencionesDao();
	
	public abstract ProgramaDao getProgramaDao();
	 
	
	/*************************************** FIN MANIZALES ******************************************/

	/*************************************** INICIO MERCURY **************************************************/

	public abstract ValoracionOdontologiaDao getValoracionOdontologiaDao();

	public abstract OdontogramaDao getOdontogramaDao();

	public abstract AntecedentesOdontologiaDao getAntecedentesOdontologiaDao();

	public abstract TratamientoOdontologiaDao getTratamientoOdontologiaDao();

	public abstract IndicePlacaDao getIndicePlacaDao();

	public abstract CartaDentalDao getCartaDentalDao();

	/*************************************** FIN MERCURY **************************************************/

	/*************************** SYSMEDICA **************************************************************/

	public abstract FichaRabiaDao getFichaRabiaDao();

	public abstract NotificacionDao getNotificacionDao();

	public abstract BusquedaFichasDao getBusquedaFichasDao();

	public abstract BusquedaNotificacionesDao getBusquedaNotificacionesDao();

	public abstract FichaSarampionDao getFichaSarampionDao();

	public abstract FichaVIHDao getFichaVIHDao();

	public abstract FichaDengueDao getFichaDengueDao();

	public abstract FichaParalisisDao getFichaParalisisDao();

	public abstract FichaSifilisDao getFichaSifilisDao();

	public abstract FichaTetanosDao getFichaTetanosDao();

	public abstract FichaGenericaDao getFichaGenericaDao();

	public abstract FichaTuberculosisDao getFichaTuberculosisDao();

	public abstract ReportesSecretariaDao getReportesSecretariaDao();

	public abstract IngresoPacienteEpiDao getIngresoPacienteEpiDao();

	public abstract FichaBrotesDao getFichaBrotesDao();

	public abstract FichaMortalidadDao getFichaMortalidadDao();

	public abstract FichaSivimDao getFichaSivimDao();

	public abstract FichaInfeccionesDao getFichaInfeccionesDao();

	public abstract FichaLesionesDao getFichaLesionesDao();

	public abstract FichaSolicitudLaboratoriosDao getFichaSolicitudLaboratoriosDao();

	public abstract ParamLaboratoriosDao getParamLaboratoriosDao();

	public abstract FichasAnterioresDao getFichasAnterioresDao();

	public abstract UtilidadFichasDao getUtilidadFichasDao();

	public abstract FichaIntoxicacionesDao getFichaIntoxicacionesDao();

	public abstract FichaRubCongenitaDao getFichaRubCongenitaDao();

	public abstract FichaAcciOfidicoDao getFichaAcciOfidicoDao();

	public abstract FichaLepraDao getFichaLepraDao();

	public abstract FichaDifteriaDao getFichaDifteriaDao();

	public abstract FichaEasvDao getFichaEasvDao();

	public abstract FichaEsiDao getFichaEsiDao();

	public abstract FichaEtasDao getFichaEtasDao();

	public abstract FichaHepatitisDao getFichaHepatitisDao();

	public abstract FichaLeishmaniasisDao getFichaLeishmaniasisDao();

	public abstract FichaMalariaDao getFichaMalariaDao();

	public abstract FichaMeningitisDao getFichaMeningitisDao();

	public abstract FichaTosferinaDao getFichaTosferinaDao();

	public abstract ArchivosPlanosDao getArchivosPlanosDao();

	public abstract RegistroTerapiasGrupalesDao getRegistroTerapiasGrupalesDao();

	public abstract ConsultaIngresosHospitalDiaDao getConsultaIngresosHospitalDiaDao();

	
	/*************************** FIN SYSMEDICA **************************************************************/
	
	public abstract ServicioHonorariosDao getServicioHonorariosDao();
	public abstract DetalleServicioHonorariosDao getDetalleServicioHonorariosDao();
	public abstract DetalleAgrupacionHonorarioDao getDetalleAgrupacionHonorarioDao();
	
	/***************************************************/
	/**
	 * Retorna el DAO con el cual el objeto <code>ContactosEmpresa</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>ContactosEmpresaDao</code>
	 */
	
	public abstract  ContactosEmpresaDao getContactosEmpresaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>UtilidadesTesoreria</code> interactua con la fuente de datos.
	 * @return el DAO usado por <code>UtilidadesTesoreriaDao</code>
	 */
	
	//public abstract UtilidadesTesoreriaDao getUtilidadesTesoreriaDao();
	
	/**
	 * 
	 */
	public abstract  CategoriaAtencionDao getCategoriaAtencionDao();
	
	/**
	 * 
	 * 
	 */
	
	public abstract  RegionesCoberturaDao getRegionesCoberturaDao();
	
	
	/**
	 * 
	 * 
	 */
	
	public abstract  DescuentoOdontologicoDao getDescuentoOdontologicoDao();
	
	/**
	 * 
	 * 
	 * 
	 */
	
	public abstract  DetalleDescuentoOdontologicoDao getDetalleDescuentoOdontologicoDao();
	
	/**
	 * 
	 * 
	 * 
	 */
	
	public abstract  TiposDeUsuarioDao getTiposDeUsuarioDao();
	/**
	 * 
	 * 
	 * @return
	 */
	
	public abstract  HistoricoDescuentoOdontologicoDao getHistoricoDescuentoOdontologicoDao();
	
	
	
	
	/**
	 * 
	 * 
	 * @return
	 */
	
	
	
	public abstract  HistoricoDetalleDescuentoOdontologico getHistoricoDetalleDescuentoOdontologicoDao();
	
	
	public abstract  EmisionTarjetaClienteDao getEmisionTarjetaClienteDao();
	
	public abstract  DetalleEmisionTarjetaClienteDao getDetalleEmisionTarjetaClienteDao();
	
	public abstract  HistoricoEmisionTarjetaClienteDao getHistoricoEmisionTarjetaClienteDao();
	
	public abstract  HistoricoDetalleEmisionTarjetaClienteDao getHistoricoDetalleEmisionTarjetaClienteDao();

	
	/**
	 * 
	 * 
	 * @return
	 */
	public abstract DetCaPromocionesOdoDao getDetCaPromocionesOdoDao();
	/**
	 * 
	 * 
	 * @return
	 */
	public abstract PromocionesOdontologicasDao getPromocionesOdontologicasDao();
	
	
	/**
	 * 
	 * 
	 * @return
	 */
	public abstract DetPromocionesOdoDao getDetPromocionesOdoDao();
	
	

	/**
	 * @return
	 */
	

	/**
	 * 
	 * @return
	 */
	public abstract DetConvPromocionesOdoDao getDetConvPromocionesOdoDao();

	public abstract AliadoOdontologicoDao getAliadoOdontologicoDao();
	
	
	
	/**
	 * 
	 * 
	 * @return
	 */
	public abstract ControlAnticipoContratoDao getControlAnticipoContratoDao();
	
	/**
	 * 
	 * 
	 * @return
	 */
	public abstract LogControlAnticipoContratoDao getLogControlAnticipoContratoDao();
	/**
	 * 
	 * @return
	 */
	public abstract VentasTarjetasClienteDao getVentasTarjetasCliente();
	/**
	 * 
	 * @return
	 */
	public abstract BeneficiarioTarjetaClienteDao getBeneficiarioTarjetaClienteDao();
	/**
	 * 
	 * @return
	 */
	public abstract ImagenesBaseDao getImagenesBaseDao();
	/**
	 * 
	 * @return
	 */
	public abstract ParentezcoDao getParentezcoDao();
	
	/**
	 * 
	 * @return
	 */
	public abstract RegistroIncapacidadesDao getRegistroIncapacidadesDao();
	
	/**
	 * 
	 * @return
	 */
	public abstract ProgramasOdontologicosDao getProgramasOdontologicosDao();
	
	/**
	 * 
	 * @return
	 */
	public abstract PerfilNEDDao getPerfilNEDDao();
	
	/*
	 * 
	 * 
	 */
	
	public abstract  HallazgoVsProgramaServicioDao getHallazgoVsProgramaServicioDao();
	
	/**
	 * 
	 * 
	 * 
	 */
	public abstract  DetalleHallazgoProgramaServicioDao getDetalleHallazgoProgramaServicioDao();
	
	/**
	 * 
	 */
	public abstract ExtractoDeudoresCPDao getExtractoDeudoresCPDao();
	
	/**
	 * 
	 */
	public abstract CargosOdonDao getCargosOdonDao();
	
	/**
	 * 
	 * @return
	 */
	public abstract PresupuestoOdontologicoDao getPresupuestoOdontologicoDao();
	
	/**
	 * 
	 * 
	 */
	public abstract AutorizacionIngresoPacienteSaldoMoraDao getAutorizacionIngresoPacienteSaldoMoraDao();
	
	public abstract PlanTratamientoDao getPlanTratamientoDao(); 
	
	/**
	 * 
	 * 
	 */
	public abstract ReporteDocumentosCarteraPacienteDao getReporteDocumentosCarteraPacienteDao();
	
	/**
	 * 
	 * 
	 */
	public abstract ValidacionesPresupuestoDao getValidacionesPresupuestoDao();
	
	/**
	 * 
	 * 
	 */
	public abstract EdadGlosaXFechaRadicacionDao getEdadGlosaXFechaRadicacionDao();
	
	/**
	 *  
	 * 
	 */
	public abstract AperturaCuentaPacienteOdontologicoDao getAperturaCuentaPacienteOdontologicoDao();
	
	/**
	 * Retorna el DAO con el cual el objeto <code>AtencionCitasOdontologiaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>AtencionCitasOdontologia</code>
	 */
	public abstract AtencionCitasOdontologiaDao getAtencionCitasOdontologiaDao();

	/**
	 * Retorna el DAO con el cual el objeto <code>PacientesConvenioOdontologiaDao</code>
	 * interactua con la fuente de datos.
	 * 
	 * @return el DAO usado por <code>PacientesConvenioOdontologiaDao</code>
	 */
	public abstract PacientesConvenioOdontologiaDao getPacientesConvenioOdontologiaDao();
	
	public abstract ReasignarProfesionalOdontoDao getReasignarProfesionalOdontoDao();
	
	public abstract ValoracionOdontologicaDao getValoracionOdontologicaDao();
	
	public abstract CancelarAgendaOdontoDao getCancelarAgendaOdontoDao();
	
	/**
	 * 
	 * @return
	 */
	public abstract PresupuestoExclusionesInclusionesDao getPresupuestoExclusionesInclusionesDao();

	
	public abstract EvolucionOdontologicaDao getEvolucionOdontologicaDao();
	
	public abstract ProcesosAutomaticosOdontologiaDao getProcesosAutomaticosOdontologiaDao();
	
	
	/**
	 * 
	 * @return
	 */
	public abstract SaldosInicialesCarteraPacienteDao getSaldosInicialesCarteraPacienteDao();
	
	/***
	 * 
	 * @return
	 */
	public abstract CierreSaldoInicialCarteraPacienteDao getCierreSaldoInicialCarteraPacienteDao();
	
	/**
	 * 
	 */
	public abstract AutorizacionDescuentosOdonDao getAutorizacionDescuentosOdonDao();
	
	/**
	 * 
	 * @return
	 */
	public abstract HonorariosPoolDao getHonorariosPoolDao();
	
	/**
	 * 
	 * @return
	 */
	public abstract CalculoHonorariosPoolesDao getCalculoHonorariosPoolesDao();
	
	/**
	 * 
	 */
	public abstract EnvioEmailAutomaticoDao getEnvioEmailAutomaticoDao(); 
	
		/**
	 * 
	 */
	public abstract ExcepcionesHorarioAtencionDao getExcepcionesHorarioAtencionDao();
	
	/**
	 * Retorna el DAO con el cual el objeto <code>NewMenuDao</code>
	 * interactua con la fuente de datos.
	 * @return el DAO usado por <code>NewMenuDao</code>
	 */
	public abstract NewMenuDao getNewMenuDao();
	
	/**
	 * 
	 * @return
	 */
	public abstract ConsecutivosCentroAtencionDao getConsecutivosCentroAtencionDao();
	
	/**
	 * 
	 * @return
	 */
	public abstract FacturaOdontologicaDao getFacturaOdontologicaDao();
	
	/**
	 * 
	 * @return
	 */
	public abstract NumeroSuperficiesPresupuestoDao getNumeroSuperficiesPresupuestoDao();
	
	/**
	 * 
	 * @return
	 */
	public abstract PresupuestoContratadoDao getPresupuestoContratadoDao();
	
	/**
	 * 
	 * @return
	 */
	public abstract PresupuestoCuotasEspecialidadDao getPresupuestoCuotasEspecialidadDao();
	
	/**
	 * 
	 * @return
	 */
	public abstract PresupuestoLogImpresionDao getPresupuestoLogImpresionDao();
	
	
	/**
	 * 
	 * @return
	 */
	public abstract PresupuestoPaquetesDao getPresupuestoPaquetesDao();


	/**
	 * 
	 * Este método se encarga de devolver una instancia de 
	 * MontoArticuloEspecificoDAO
	 * @return MontoAgrupacionArticuloDAO
	 * @author Angela Maria Aguirre
	 */
	public abstract MontoAgrupacionArticuloDAO getMontoAgrupacionArticuloDAO();
	
	/**
	 * 
	 * Este método se encarga de devolver una instancia de 
	 * MontoArticuloEspecificoDAO
	 * @return MontoArticuloEspecificoDAO
	 * @author Angela Maria Aguirre
	 */
	public abstract MontoArticuloEspecificoDAO getMontoArticuloEspecificoDAO();
	
	
	/**
	 * 
	 * Este método se encarga de devolver una instancia de 
	 * HistoMontoAgrupacionArticuloDAO
	 * @return HistoMontoAgrupacionArticuloDAO
	 * @author Angela Maria Aguirre
	 */
	public abstract HistoMontoAgrupacionArticuloDAO getHistoMontoAgrupacionArticuloDAO();

	/**
	 * 
	 * @return
	 */
	public abstract ReporteCitasOdontologicasDao getReporteCitasOdontologicasDao();
	
	/**
	 * 
	 * Este método se encarga de devolver una instancia de 
	 * NivelAutorizacionAgrupacionArticuloDAO
	 * @return NivelAutorizacionAgrupacionArticuloDAO
	 * @author Angela Maria Aguirre
	 */
	public abstract NivelAutorizacionAgrupacionArticuloDAO getNivelAutorizacionAgrupacionArticuloDAO();
	
	
	/**
	 * 
	 * Este método se encarga de devolver una instancia de 
	 * NivelAutorizacionArticuloEspecificoDAO
	 * @return NivelAutorizacionArticuloEspecificoDAO
	 * @author Angela Maria Aguirre
	 */
	public abstract NivelAutorizacionArticuloEspecificoDAO getNivelAutorizacionArticuloEspecificoDAO();

	/**
	 * 
	 * @return
	 */
	public abstract CalculoValorCobrarPacienteDao getCalculoValorCobrarPacienteDao();	
	
	/**
	 * 
	 * @return
	 */
	public abstract ProcesoNivelAutorizacionDao getProcesoNivelAutorizacionDao();
	
}