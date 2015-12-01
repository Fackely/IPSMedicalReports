package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.capitacion.ConstantesCapitacion;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.InfoTarifaYExcepcion;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.cargos.CargosEntidadesSubcontratadasDao;
import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.princetonsa.dto.cargos.DTOCalculoTarifaServicioArticulo;
import com.princetonsa.dto.facturacion.DTOMontosCobroDetalleGeneral;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.inventario.DTOEsqTarInventarioContrato;
import com.princetonsa.dto.inventario.DTOEsqTarProcedimientoContrato;
import com.princetonsa.dto.inventario.DtoArticulos;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorizacionCapitacionSubcontratada;
import com.princetonsa.dto.manejoPaciente.DTOProcesoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoParametrosBusquedaMonto;
import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.facturacion.ConsultarContratosEntidadesSubcontratadas;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.interfaz.capitacion.IUsuarioXConvenioDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ITarifasEntidadSubDAO;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IContratoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IConvenioDAO;
import com.servinte.axioma.dao.interfaz.inventario.IArticuloDAO;
import com.servinte.axioma.dao.interfaz.inventario.IClaseInventarioDAO;
import com.servinte.axioma.dao.interfaz.inventario.IEsqTarInventariosContratoDAO;
import com.servinte.axioma.dao.interfaz.inventario.IEsqTarProcedimientoContratoDAO;
import com.servinte.axioma.dao.interfaz.inventario.IUnidosisXArticuloDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesCapitacionSubDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntSubArticuloDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntSubServiDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntidadesSubDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEstanciaCapitaDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IHistoAutorizacionCapitaSubDAO;
import com.servinte.axioma.dto.capitacion.DtoValidacionPresupuesto;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.facturacion.convenio.ConveniosFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempClaseInvArtMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteClaseInvArtMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IValidacionPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IDetalleMontoGeneralMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IServiciosMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IPacientesMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IProcesoGeneracionAutorizacionMundo;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.AutoEntsubOrdenambula;
import com.servinte.axioma.orm.AutoEntsubPeticiones;
import com.servinte.axioma.orm.AutoEntsubSolicitudes;
import com.servinte.axioma.orm.AutorizacionesCapitacionSub;
import com.servinte.axioma.orm.AutorizacionesEntSubArticu;
import com.servinte.axioma.orm.AutorizacionesEntSubMontos;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.AutorizacionesEstanciaCapita;
import com.servinte.axioma.orm.AutorizacionesIngreEstancia;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.CierreTempClaseInvArt;
import com.servinte.axioma.orm.CierreTempGrupoServicio;
import com.servinte.axioma.orm.CierreTempNivAteClInvArt;
import com.servinte.axioma.orm.CierreTempNivelAteGruServ;
import com.servinte.axioma.orm.CierreTempNivelAtenArt;
import com.servinte.axioma.orm.CierreTempNivelAtenServ;
import com.servinte.axioma.orm.CierreTempServArt;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.ContratosEntidadesSub;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.EntidadesSubcontratadas;
import com.servinte.axioma.orm.EsquemasTarifarios;
import com.servinte.axioma.orm.EstadosSolFact;
import com.servinte.axioma.orm.EstratosSociales;
import com.servinte.axioma.orm.FinalidadesServicio;
import com.servinte.axioma.orm.HistoAutorizacionCapitaSub;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.NivelAutorizacion;
import com.servinte.axioma.orm.OrdenesAmbulatorias;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.PeticionQx;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.orm.Solicitudes;
import com.servinte.axioma.orm.TarifasEntidadSub;
import com.servinte.axioma.orm.TiposAfiliado;
import com.servinte.axioma.orm.UnidosisXArticulo;
import com.servinte.axioma.orm.UsuarioXConvenio;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.ViasIngreso;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.capitacion.CapitacionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroCostosServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempGrupoServicioServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAteGruServServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAtenArtServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAtenServServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempServArtServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IServiciosServicio;
 

/**
 * Esta clase se encarga de realizar el proceso de autorización de capitación Subcontratada.
 * @author Angela Maria Aguirre
 * @since 2/12/2010
 * 
 * @author Cristhian Murillo
 * @since 16/06/2011
 */
public class ProcesoGeneracionAutorizacionMundo implements IProcesoGeneracionAutorizacionMundo 
{
	/** * Daos */
	private IAutorizacionesEntidadesSubDAO autorizacionDAO = ManejoPacienteDAOFabrica.crearAutorizacionesEntidadesSubDAO();
	
	private ContratosEntidadesSub contratoEntidadSubc = new ContratosEntidadesSub();
	
	/** Lista para almacenar los consecutivos de Autorizaciones de Entidad Subcontratada */	
	private ArrayList<String> consecutivoAutoEntSub = new ArrayList<String>();
	/** Lista para almacenar los consecutivos de Autorizaciones de Capitación Subcontratada */
	private ArrayList<String> consecutivoAutoCapiSub = new ArrayList<String>();
	/**
	 * Este Método se encarga de realizar el proceso de autorización de 
	 * capitación Subcontratada.
	 * @param DTOProcesoAutorizacion dtoProcesoAutorizacion
	 * @return DTOProcesoAutorizacion
	 * 
	 * @author, Angela Maria Aguirre
	*/
	public DTOProcesoAutorizacion generacionAutorizacion(DTOProcesoAutorizacion dtoProcesoAutorizacion) throws Exception 
	{
		boolean entidadSubcontratadaOTra =false;
		ActionErrors errores = new ActionErrors();	
		Date fechaVencimientoAutorizacion = null;
		boolean esServicio=false;
		Cuenta cuenta= new Cuenta();
		String serviciosArticulosSinTarifa = "";
		List<DtoValidacionPresupuesto> serviciosArticulosSinPresupuesto = new ArrayList<DtoValidacionPresupuesto>();
		String serviciosArticulosSinCierreOrdenMedica = "";
		ArrayList<DtoServiciosAutorizaciones> listaServiciosValidados = new ArrayList<DtoServiciosAutorizaciones>();
		ArrayList<DtoArticulosAutorizaciones> listaArticulosValidados = new ArrayList<DtoArticulosAutorizaciones>();
		ArrayList<DTOAutorEntidadSubcontratadaCapitacion> listaAutorizaciones = null;
		boolean realizarEliminacion=true;
		boolean esIngresoEstancia = false;
		boolean esAmbulaOPeticion = false;
		String tipoPaciente=null;
		
		consecutivoAutoEntSub.clear();
		consecutivoAutoCapiSub.clear();
		
		MessageResources mensajes = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.ProcesoGeneracionAutorizacionForm");
		
		Connection con = UtilidadPersistencia.getPersistencia().obtenerConexion();
		UtilidadTransaccion.getTransaccion().begin();
						
		try
		{
			if(dtoProcesoAutorizacion.getDtoSolicitud()!= null){
				if(!Utilidades.isEmpty(dtoProcesoAutorizacion.getDtoSolicitud().getListaServicios())){
					esServicio=true;
				}
				if(dtoProcesoAutorizacion.isOrdenAmbulatoria()){
					
					if (dtoProcesoAutorizacion.getPaciente().getCodigoContrato() != ConstantesBD.codigoNuncaValido && dtoProcesoAutorizacion.getPaciente().getCodigoContrato() != 0){
						dtoProcesoAutorizacion.setContratoAutorizacion(dtoProcesoAutorizacion.getPaciente().getCodigoContrato());
					}
					
					esAmbulaOPeticion=true;
				}
			}
			else if(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia()!= null){
				esIngresoEstancia = true;
				if(!Utilidades.isEmpty(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia().getListaServicios())){
					esServicio=true;
				}
			}
			
			if(dtoProcesoAutorizacion.getDtoEntidadSubcontratada().getCodigoPk()== ConstantesBD.codigoNuncaValidoLong){ 
				entidadSubcontratadaOTra =true;
			}
			
			if(!esIngresoEstancia)
			{
				if(!esAmbulaOPeticion)
				{//cuando es orden medica
				cuenta.cargarCuenta(con, (dtoProcesoAutorizacion.getPaciente().getCodigoCuenta()+""));
				tipoPaciente= UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con, (cuenta.getIdCuenta())).getAcronimo();
				}else{
					ArrayList<HashMap<String,Object>> listaTiposPaciente=new ArrayList<HashMap<String,Object>>();
					listaTiposPaciente = UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, dtoProcesoAutorizacion.getViaIngreso()+"");
					tipoPaciente=listaTiposPaciente.get(0).get("codigoTipoPaciente").toString();
			}
			}
					
			String fechaAutorizacion = UtilidadFecha.getFechaActual();
			
			
			
			if(!entidadSubcontratadaOTra)
			{
				//1. Se realiza el cálculo de la tarifa
				CargosEntidadesSubcontratadasDao cargosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosEntidadesSubcontratadasDao(); 
				DtoContratoEntidadSub contratoEntidadSub = cargosDao.obtenerContratoVigenteEntidadSubcontratada(con,(dtoProcesoAutorizacion.getDtoEntidadSubcontratada().getCodigoPk()+""),fechaAutorizacion);			
				
				contratoEntidadSubc.setConsecutivo(Utilidades.convertirALong(contratoEntidadSub.getConsecutivo()));
				
				if(contratoEntidadSub != null)
				{	
					DtoEntidadSubcontratada dtoEntidadSub = new DtoEntidadSubcontratada();
					dtoEntidadSub.setCodigoPk(dtoProcesoAutorizacion.getDtoEntidadSubcontratada().getCodigoPk());				
					
					contratoEntidadSub.setEntidad(dtoEntidadSub);		
					
					//Se determina el tipo de tarifa de la entidad subcontratada
					ConsultarContratosEntidadesSubcontratadas.consultarTipoTarifaEntidadSubcontratada(con, contratoEntidadSub);
					
					if(!UtilidadTexto.isEmpty(contratoEntidadSub.getTipoTarifa()))
					{
						if(contratoEntidadSub.getTipoTarifa().equals(ConstantesIntegridadDominio.acronimoTipoTarifaPropia))
						{
							//Se calcula la tarifa por la entidad subcontratada CU 799
							serviciosArticulosSinTarifa += calcularTarifaArticuloServicioEntidadSub
							(
									dtoEntidadSub, dtoProcesoAutorizacion, con, esServicio, listaServiciosValidados, listaArticulosValidados
							);
						}
						else if(contratoEntidadSub.getTipoTarifa().equals(ConstantesIntegridadDominio.acronimoTipoTarifaConvenioPaciente))
						{
							//Se calcula la tarifa por cargo modificado CU 438
							serviciosArticulosSinTarifa +=  calcularTarifaArticuloServicioCargoModificado
							( 
									dtoProcesoAutorizacion, con, esServicio, tipoPaciente, listaServiciosValidados, listaArticulosValidados, dtoEntidadSub
							);
						}
						
						//Se genera mensaje de error para los servicios o artículos que no tienen tarifa 
						if(!UtilidadTexto.isEmpty(serviciosArticulosSinTarifa))
						{
							errores.add("Servicios o Artículos sin tarifa", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("procesoGeneracionAutorizacionForm.serviciosArticulosSinTarifa", serviciosArticulosSinTarifa)));							
						}						
					}
				}
				
				
				//Se continúa el proceso solo con los artículos o servicios a los cuales se les asignó tarifa
				
				if(!Utilidades.isEmpty(listaServiciosValidados) || !Utilidades.isEmpty(listaArticulosValidados))
				{
					/* DCU 1106 - Versión cambio 1.1. Cristhian Murillo */
					String ValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica = null;
					boolean realizarValidacionPresupuestoCapitacion = true; 
					
					if(dtoProcesoAutorizacion.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutomatica))
					{
						ValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica = ValoresPorDefecto.getValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica(dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt());
						if(UtilidadTexto.isEmpty(ValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica))
						{
							if(ValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica.equals(ConstantesBD.acronimoNo))
							{
								realizarValidacionPresupuestoCapitacion = false;
							}
							else if(ValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica.equals(ConstantesBD.acronimoSi))
							{
								realizarValidacionPresupuestoCapitacion = true;
							}
						}
						else{
							realizarValidacionPresupuestoCapitacion = true;
						}
					}
					//----------------------------------------------------------
					
					
					if(realizarValidacionPresupuestoCapitacion)
					{
						//2. Se valida el presupuesto de capitación
						//Se obtiene la fecha de cierre de orden médica
						String fechaCierreOrdenMedica = ValoresPorDefecto.getFechaInicioCierreOrdenMedica(dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt());
						
						if(UtilidadTexto.isEmpty(fechaCierreOrdenMedica))
						{
							serviciosArticulosSinCierreOrdenMedica = "";
							
							if(esServicio)
							{
								for(DtoServiciosAutorizaciones servicio :listaServiciosValidados){
									 serviciosArticulosSinCierreOrdenMedica +=servicio.getDescripcionServicio() + " - ";								
								}
							}else{
								for(DtoArticulosAutorizaciones articulo :listaArticulosValidados){
									 serviciosArticulosSinCierreOrdenMedica +=articulo.getDescripcionArticulo() + " - ";								
								}
							}					
							
							errores.add("Parámetro Fecha Cierre Orden Medica Nula", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("procesoGeneracionAutorizacionForm.parametroFechaCierreOrdenMedica",
											serviciosArticulosSinCierreOrdenMedica)));
							
						}else{
							//Si la fecha Actual es menor o igual a la fecha Inicio Cierre Ordenes Médicas
							//no se valida el presupuesto
							Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
							Date fechaInicioCOM = UtilidadFecha.conversionFormatoFechaStringDate(fechaCierreOrdenMedica);
							if(fechaActual.after(fechaInicioCOM)){
								serviciosArticulosSinPresupuesto = validarPresupuestoCapitacion(con, cuenta,dtoProcesoAutorizacion, 
										listaServiciosValidados,listaArticulosValidados,esServicio,tipoPaciente, esIngresoEstancia,
										esAmbulaOPeticion,fechaCierreOrdenMedica, realizarEliminacion);
																	
								if(serviciosArticulosSinPresupuesto != null 
										&& !serviciosArticulosSinPresupuesto.isEmpty()){
										boolean validacionSobrepasaTope=false;
										boolean validacionNoCierre=false;
										boolean validacionNoCierreAutorizaciones=false;
										boolean validacionNoParametrizacion=false;
										boolean validacionNoNivelAtencion=false;
										String articulosServiciosSobrepasaTope="";
										String articulosServiciosNoCierre="";
										String articulosServiciosNoCierreAutorizaciones="";
										String articulosServiciosNoParametrizacion="";
										String articulosServiciosNoNivelAtencion="";
										String fechaValidacion="";
										String convenioValidacion="";
										String contratoValidacion="";
										String anioValidacion="";
										String mesValidacion="";
										for(DtoValidacionPresupuesto dtoValPresupuesto:serviciosArticulosSinPresupuesto){
											if(dtoValPresupuesto.getCodigoValidacion() != null
													&& dtoValPresupuesto.getCodigoValidacion().intValue()==ConstantesCapitacion.codigoValPresupuestoSobrepasaTope){
												articulosServiciosSobrepasaTope += dtoValPresupuesto.getNombreServicioArticulo()+", ";
												validacionSobrepasaTope=true;
											}
											else if(dtoValPresupuesto.getCodigoValidacion() != null
														&& dtoValPresupuesto.getCodigoValidacion().intValue()==ConstantesCapitacion.codigoValPresupuestoNoCierreAutorizaciones){
												articulosServiciosNoCierreAutorizaciones += dtoValPresupuesto.getNombreServicioArticulo()+", ";
												validacionNoCierreAutorizaciones=true;
												convenioValidacion=dtoValPresupuesto.getConvenioValidacion();
												contratoValidacion=dtoValPresupuesto.getContratoValidacion();
												anioValidacion=dtoValPresupuesto.getAnioValidacion();
												mesValidacion=dtoValPresupuesto.getMesValidacion();
											}
											else if(dtoValPresupuesto.getCodigoValidacion() != null
														&& dtoValPresupuesto.getCodigoValidacion().intValue()==ConstantesCapitacion.codigoValPresupuestoNoParametrizacion){
												articulosServiciosNoParametrizacion += dtoValPresupuesto.getNombreServicioArticulo()+", ";
												validacionNoParametrizacion=true;
												convenioValidacion=dtoValPresupuesto.getConvenioValidacion();
												contratoValidacion=dtoValPresupuesto.getContratoValidacion();
												anioValidacion=dtoValPresupuesto.getAnioValidacion();
												mesValidacion=dtoValPresupuesto.getMesValidacion();
											}
											else if(dtoValPresupuesto.getCodigoValidacion() != null
													&& dtoValPresupuesto.getCodigoValidacion().intValue()==ConstantesCapitacion.codigoValPresupuestoNoCierre){
												articulosServiciosNoCierre += dtoValPresupuesto.getNombreServicioArticulo()+", ";
												validacionNoCierre=true;
												convenioValidacion=dtoValPresupuesto.getConvenioValidacion();
												contratoValidacion=dtoValPresupuesto.getContratoValidacion();
												anioValidacion=dtoValPresupuesto.getAnioValidacion();
												mesValidacion=dtoValPresupuesto.getMesValidacion();
												fechaValidacion=dtoValPresupuesto.getFechaValidacion();
											}
											else if(dtoValPresupuesto.getCodigoValidacion() != null
													&& dtoValPresupuesto.getCodigoValidacion().intValue()==ConstantesCapitacion.codigoValPresupuestoNoNivelAtencion){
												articulosServiciosNoNivelAtencion += dtoValPresupuesto.getNombreServicioArticulo()+", ";
												validacionNoNivelAtencion=true;
											}
										}
										
										if(esServicio){
											//Se debe generar mensaje de error para los servicios que no se pueden validar
											if(validacionSobrepasaTope){
												errores.add("Servicios sobrepasan presupuesto", new ActionMessage("errors.notEspecific", 
													mensajes.getMessage("procesoGeneracionAutorizacionForm.errorServicioSobrepasaPresupuesto",
															new Object[]{articulosServiciosSobrepasaTope})));
											}
											if(validacionNoCierreAutorizaciones){
												errores.add("Servicios sin cierre", new ActionMessage("errors.notEspecific", 
													mensajes.getMessage("procesoGeneracionAutorizacionForm.errorServicioNoCierreAutorizaciones",
															new Object[]{convenioValidacion, contratoValidacion, anioValidacion, mesValidacion,
																			articulosServiciosNoCierreAutorizaciones})));
											}
											if(validacionNoParametrizacion){
												errores.add("Servicios sin parametrización presupuesto", new ActionMessage("errors.notEspecific", 
													mensajes.getMessage("procesoGeneracionAutorizacionForm.errorServicioNoParametrizacion",
															new Object[]{convenioValidacion, contratoValidacion, anioValidacion, mesValidacion,
																			articulosServiciosNoParametrizacion})));
											}
											if(validacionNoCierre){
												errores.add("Servicios sin cierre", new ActionMessage("errors.notEspecific", 
													mensajes.getMessage("procesoGeneracionAutorizacionForm.errorServicioNoCierre",
															new Object[]{fechaValidacion, convenioValidacion, contratoValidacion, anioValidacion, mesValidacion,
																			articulosServiciosNoCierre})));
											}
										}
										else{
											//Se debe generar mensaje de error para los medicamentos/ insumos que no se pueden validar
											if(validacionSobrepasaTope){
												errores.add("Articulos sobrepasan presupuesto", new ActionMessage("errors.notEspecific", 
													mensajes.getMessage("procesoGeneracionAutorizacionForm.errorArticuloSobrepasaPresupuesto",
															new Object[]{articulosServiciosSobrepasaTope})));
											}
											if(validacionNoCierreAutorizaciones){
												errores.add("Articulos sin cierre", new ActionMessage("errors.notEspecific", 
													mensajes.getMessage("procesoGeneracionAutorizacionForm.errorArticuloNoCierreAutorizaciones",
															new Object[]{convenioValidacion, contratoValidacion, anioValidacion, mesValidacion,
																			articulosServiciosNoCierreAutorizaciones})));
											}
											if(validacionNoParametrizacion){
												errores.add("Articulos sin parametrización presupuesto", new ActionMessage("errors.notEspecific", 
													mensajes.getMessage("procesoGeneracionAutorizacionForm.errorArticuloNoParametrizacion",
															new Object[]{convenioValidacion, contratoValidacion, anioValidacion, mesValidacion,
																			articulosServiciosNoParametrizacion})));
											}
											if(validacionNoCierre){
												errores.add("Articulos sin cierre", new ActionMessage("errors.notEspecific", 
													mensajes.getMessage("procesoGeneracionAutorizacionForm.errorArticuloNoCierre",
															new Object[]{fechaValidacion, convenioValidacion, contratoValidacion, anioValidacion, mesValidacion,
																			articulosServiciosNoCierre})));
											}
											if(validacionNoNivelAtencion){
												errores.add("Articulos sin Nivel", new ActionMessage("errors.notEspecific", 
													mensajes.getMessage("procesoGeneracionAutorizacionForm.errorArticuloNoNivelAtencion",
															new Object[]{articulosServiciosNoNivelAtencion})));
											}
										}
								}
							}
						}
					}
				}
				
			}else{
				
				ArrayList<DtoArticulosAutorizaciones> listaArticulosValidadosAux = new ArrayList<DtoArticulosAutorizaciones>();
				String serviciosArticulosNoAutorizados="";
				Set<Contratos> setContrato = null;
				Contratos contrato = null;
				
				if(esServicio){					
					
					if(dtoProcesoAutorizacion.getDtoSolicitud()!=null){
						
						listaServiciosValidados = dtoProcesoAutorizacion.getDtoSolicitud().getListaServicios();
						
					}else if(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia()!=null){
						listaServiciosValidados = dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia().getListaServicios();
					}					
				}else{
					
					if(dtoProcesoAutorizacion.getDtoSolicitud()!=null){
						
						listaArticulosValidados = dtoProcesoAutorizacion.getDtoSolicitud().getListaArticulos();
						
					}else if(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia()!=null){
						listaArticulosValidados = dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia().getListaArticulos();
					}
					
					for( DtoArticulosAutorizaciones articuloValidado: listaArticulosValidados){						
						//Se busca el convenio responsable para cada articulo de la autorización
						InfoResponsableCobertura infoCobertura=							
							Cobertura.validacionCoberturaArticulo(con, dtoProcesoAutorizacion.getPaciente().getCodigoIngreso()+"", 
								dtoProcesoAutorizacion.getPaciente().getCodigoUltimaViaIngreso(),
								dtoProcesoAutorizacion.getPaciente().getCodigoTipoPaciente(), 
								articuloValidado.getCodigoArticulo(), 
								dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt(),false);
						
						if(infoCobertura.getDtoSubCuenta()!=null && infoCobertura.getDtoSubCuenta().getConvenio()!=null &&
								infoCobertura.getDtoSubCuenta().getConvenio().getCodigo()>0){
							
							setContrato = new HashSet<Contratos>(0);
							Convenios convenioResponsable = new Convenios();
							contrato = new Contratos();
							
							contrato.setCodigo(infoCobertura.getDtoSubCuenta().getContrato());
							setContrato.add(contrato);		
							
							convenioResponsable.setCodigo(infoCobertura.getDtoSubCuenta().getConvenio().getCodigo());
							convenioResponsable.setContratoses(setContrato);
							
							articuloValidado.setConvenioResponsable(convenioResponsable);	
							listaArticulosValidadosAux.add(articuloValidado);
							
						}else{
							
							//Se cancela el proceso	para la solicitud para este artículo								
							serviciosArticulosNoAutorizados +=articuloValidado.getDescripcionArticulo() + " - ";
						}
					}
					if(!UtilidadTexto.isEmpty(serviciosArticulosNoAutorizados)){
						//Se debe generar mensaje de error para los artículos que no tienen convenio responsable
						errores.add("Servicios o Artículos sin convenio responsable", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("procesoGeneracionAutorizacionForm.articuloSinConvenioResponsable",
										serviciosArticulosNoAutorizados)));
					}
					listaArticulosValidados = listaArticulosValidadosAux;
				}				
			}
			
			
			//Se autorizan los artículos o servicios de la solicitud que pasaron las validaciones
			if((esServicio && !Utilidades.isEmpty(listaServiciosValidados)) || (!esServicio && !Utilidades.isEmpty(listaArticulosValidados)))
			{
				//3. Se realiza el cálculo de la vigencia de la autorización automática 
				if(dtoProcesoAutorizacion.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutomatica))
				{
					int diasCalculoVigencia=0;					
					
					if(esServicio)
					{
						String valorParametroDiasVigencia = ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionServicio(dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt());
						
						if(!UtilidadTexto.isEmpty(valorParametroDiasVigencia)){						
							diasCalculoVigencia = Integer.valueOf(valorParametroDiasVigencia);
						}else{
							diasCalculoVigencia = 30;
						}	
						
						fechaVencimientoAutorizacion =  UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.incrementarDiasAFecha(fechaAutorizacion, diasCalculoVigencia, false));
						
					}else{
						
						String valorParametroDiasVigencia = ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionArticulo(dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt());
						
						if(!UtilidadTexto.isEmpty(valorParametroDiasVigencia)){						
							diasCalculoVigencia = Integer.valueOf(valorParametroDiasVigencia);
						}else{
							diasCalculoVigencia = 30;
						}						

						fechaVencimientoAutorizacion = UtilidadFecha.conversionFormatoFechaStringDate(
								UtilidadFecha.incrementarDiasAFecha(fechaAutorizacion, diasCalculoVigencia, false));
					}
					
					//4. Validación Monto de Cobro para Autorizaciones Automáticas
					validacionMontoCobroAutorizacionesAutomaticas(dtoProcesoAutorizacion, listaArticulosValidados, listaServiciosValidados);
					 
					
				}else{
					fechaVencimientoAutorizacion = dtoProcesoAutorizacion.getFechaVencimientoAutorizacion();
				}			
				
				//5. Se genera la autorización de entidad subcontratada. Esta siempre se genera así no exista
				//   la entidad subcontratada en el sistema, en este caso se guarda con entidad subcontratada = -1.			
				listaAutorizaciones = generarAutorizacionEntidadSubcontratada(dtoProcesoAutorizacion, fechaVencimientoAutorizacion, 
						cuenta,listaArticulosValidados,listaServiciosValidados,	con,tipoPaciente);
			}
			
			/* Se realiza el acumulativo de la tabla de cierre temporal de presupuesto, solo para las autorizaciones
				que tienen una entidad subcontratada existente, las que no, se realiza su cierre en el proceso de 
				administración de autorizaciones, anexo 1098. */
			if(!Utilidades.isEmpty(listaAutorizaciones) && !entidadSubcontratadaOTra){
				generarAcumuladoCierreTemporal(listaAutorizaciones);
			}
			dtoProcesoAutorizacion.setErroresAutorizacion(errores);
			dtoProcesoAutorizacion.setListaAutorizaciones(listaAutorizaciones);						
			
			UtilidadTransaccion.getTransaccion().commit();
		} catch (Exception e) {			
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.error("Error, No se pudo generar la autorización " + e);
							
			for(int i=0; i<consecutivoAutoEntSub.size();i++){
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(ConstantesBD.nombreConsecutivoAutorizacionEntiSub, 
						dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt(), consecutivoAutoEntSub.get(i), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);				
			}
			
			for(int j=0; j<consecutivoAutoCapiSub.size();j++){
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, 
						dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt(), consecutivoAutoCapiSub.get(j), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
			}
			
			dtoProcesoAutorizacion=null;
			
		}
		return dtoProcesoAutorizacion;
	}
	
	
	/**
	 * 
	 * Este Método se encarga de generar el acumulado del cierre temporal de
	 * presupuesto
	 * 
	 * @param ArrayList<DTOAutorEntidadSubcontratadaCapitacion> listaAutorizaciones
	 * @author, Angela Maria Aguirre
	 *
	 */
	private void generarAcumuladoCierreTemporal(ArrayList<DTOAutorEntidadSubcontratadaCapitacion> listaAutorizaciones){
		boolean esServicio = false;
		ICierreTempServArtServicio cierreServArtServicio = CapitacionFabricaServicio.crearCierreTempServArtServicio();
		ICierreTempNivelAtenServServicio cierreNivelAtenServServicio = CapitacionFabricaServicio.crearCierreTempNivelAtenServServicio();
		ICierreTempGrupoServicioServicio cierreGrupoServServicio = CapitacionFabricaServicio.crearCierreTempGrupoServicioServicio();
		ICierreTempNivelAteGruServServicio cierreNivelGrupoSerServicio = CapitacionFabricaServicio.crearCierreTempNivelAteGruServServicio();
		
		ICierreTempClaseInvArtMundo cierreClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempClaseInvArtMundo();
		ICierreTempNivelAtenArtServicio cierreNivelArticuloServicio = CapitacionFabricaServicio.crearCierreTempNivelAtenArtServicio();
		ICierreTempNivelAteClaseInvArtMundo cierreNivelAteClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempNivelAteClaseInvArtMundo();
		IServiciosServicio servServicio = FacturacionServicioFabrica.crearServiciosServicio();
		IArticuloDAO articuloDAO = InventarioDAOFabrica.crearArticuloDAO();
		IClaseInventarioDAO claseInventarioDAO = InventarioDAOFabrica.crearClaseInventarioDAO();
				
		DTOBusquedaCierreTemporalServicio dtoParametrosServicios = new DTOBusquedaCierreTemporalServicio();
		DTOBusquedaCierreTemporalArticulo dtoParametrosArticulos = new DTOBusquedaCierreTemporalArticulo();		
		BigDecimal valorTotal=new BigDecimal(0);
		Servicios servicio =null;		
		int cantidadSolicitada=1;
		ArrayList<CierreTempNivelAteGruServ> listaCierreGrupoNivelServicio =null;
		CierreTempNivelAteGruServ cierreGrupoNivelServicio =null;
		ArrayList<CierreTempGrupoServicio> listaCierreGrupoServicio = null;
		CierreTempGrupoServicio cierreGrupoServicio = null;
		ArrayList<CierreTempNivelAtenServ> listaCierreNivelServicio = null;
		CierreTempNivelAtenServ cierreNivelServicio = null;
		ArrayList<CierreTempServArt> listaCierreServArt=null;
		CierreTempServArt cierreServArt = null;
		DtoArticulos dtoArt = null;		
		ArrayList<CierreTempClaseInvArt> listaCierreClaseInvArt =null;
		CierreTempClaseInvArt cierreClaseInvArt =null;
		ArrayList<CierreTempNivelAtenArt> listaCierreNivelAtencion=null;
		CierreTempNivelAtenArt cierreNivelAtencion=null;
		ArrayList<CierreTempNivAteClInvArt> listaCierreNivelAteClaseInvArticulo =null;
		CierreTempNivAteClInvArt cierreNivelAteClaseInvArticulo =null;
		/*
		for (DTOAutorEntidadSubcontratadaCapitacion autorizacion : listaAutorizaciones) {
			
			if(!Utilidades.isEmpty(autorizacion.getListaServicios())){
				esServicio=true;
			}
			
			if(esServicio){
				for (DtoServiciosAutorizaciones dtoServicio : autorizacion.getListaServicios()) {
					
					dtoParametrosServicios = new DTOBusquedaCierreTemporalServicio();					
					
					servicio = servServicio.obtenerServicioPorId(dtoServicio.getCodigoServicio());
					
					dtoParametrosServicios.setCierreServicio(ConstantesBD.acronimoSiChar);
					dtoParametrosServicios.setCodigoContrato(autorizacion.getDtoContrato().getCodigo());					
					//listaCierreServArt = cierreServArtServicio.buscarCierreTemporalServicioArticulo(dtoParametrosServicios);
					
					if(Utilidades.isEmpty(listaCierreServArt)){
						
						Contratos contrato = new Contratos();						
						cierreServArt = new CierreTempServArt();
						
						contrato.setCodigo(autorizacion.getDtoContrato().getCodigo());
						cierreServArt.setContratos(contrato);
						cierreServArt.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
						cierreServArt.setValorAcumulado(0L);
						cierreServArt.setCierreServicio(ConstantesBD.acronimoSiChar);
					}else{
						cierreServArt = listaCierreServArt.get(0);
					}
												
					if(dtoServicio.getCantidadSolicitada()>1){
						cantidadSolicitada = dtoServicio.getCantidadSolicitada();
					}
					valorTotal = dtoServicio.getValorServicio().multiply(
							new BigDecimal(cantidadSolicitada));
					
					cierreServArt.setValorAcumulado(cierreServArt.getValorAcumulado()+ (valorTotal.doubleValue()));
					//cierreServArtServicio.attachDirty(cierreServArt);					
					
					dtoParametrosServicios.setCodigoNivelAtencion(servicio.getNivelAtencion().getConsecutivo());					
					//listaCierreNivelServicio = cierreNivelAtenServServicio.buscarCierreTemporalNivelAtencion(dtoParametrosServicios);
					
					if(Utilidades.isEmpty(listaCierreNivelServicio)){
						Contratos contrato = new Contratos();						
						cierreNivelServicio = new CierreTempNivelAtenServ();
						NivelAtencion nivelAtencion = new NivelAtencion();
						
						contrato.setCodigo(autorizacion.getDtoContrato().getCodigo());
						cierreNivelServicio.setContratos(contrato);
						cierreNivelServicio.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
						cierreNivelServicio.setValorAcumulado(0L);						
						nivelAtencion.setConsecutivo(servicio.getNivelAtencion().getConsecutivo());
						cierreNivelServicio.setNivelAtencion(nivelAtencion);
					}else{
						cierreNivelServicio = listaCierreNivelServicio.get(0);
					}
					/*
					cierreNivelServicio.setValorAcumulado(cierreNivelServicio.getValorAcumulado() +
							valorTotal.doubleValue());
					cierreNivelAtenServServicio.sincronizarCierreTemporal(cierreNivelServicio);
					
					
					dtoParametrosServicios.setCodigoGrupoServicio(servicio.getGruposServicios().getCodigo());
					listaCierreGrupoServicio = cierreGrupoServServicio.buscarCierreTemporalNivelAtencion(dtoParametrosServicios);
				
					if(Utilidades.isEmpty(listaCierreGrupoServicio)){
						Contratos contrato = new Contratos();						
						cierreGrupoServicio = new CierreTempGrupoServicio();
						GruposServicios grupoServicio = new GruposServicios();
						
						contrato.setCodigo(autorizacion.getDtoContrato().getCodigo());
						cierreGrupoServicio.setContratos(contrato);
						cierreGrupoServicio.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
						cierreGrupoServicio.setValorAcumulado(0L);						
						grupoServicio.setCodigo(servicio.getGruposServicios().getCodigo());
						cierreGrupoServicio.setGruposServicios(grupoServicio);
					}else{
						cierreGrupoServicio=listaCierreGrupoServicio.get(0);
					}
					
					cierreGrupoServicio.setValorAcumulado(cierreGrupoServicio.getValorAcumulado() + 
							valorTotal.doubleValue());
					cierreGrupoServServicio.sincronizarCierreTemporal(cierreGrupoServicio);
					
					
					dtoParametrosServicios.setCodigoGrupoServicio(servicio.getGruposServicios().getCodigo());
					
					listaCierreGrupoNivelServicio = 
						cierreNivelGrupoSerServicio.buscarCierreTemporalNivelAtencionGrupoServicio(dtoParametrosServicios);
					
					if(Utilidades.isEmpty(listaCierreGrupoNivelServicio)){
						Contratos contrato = new Contratos();						
						cierreGrupoNivelServicio = new CierreTempNivelAteGruServ();
						NivelAtencion nivelAtencion = new NivelAtencion();
						GruposServicios grupoServicio = new GruposServicios();
						
						contrato.setCodigo(autorizacion.getDtoContrato().getCodigo());
						nivelAtencion.setConsecutivo(servicio.getNivelAtencion().getConsecutivo());
						grupoServicio.setCodigo(servicio.getGruposServicios().getCodigo());
						
						cierreGrupoNivelServicio.setContratos(contrato);
						cierreGrupoNivelServicio.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
						cierreGrupoNivelServicio.setValorAcumulado(0L);
						cierreGrupoNivelServicio.setNivelAtencion(nivelAtencion);
						cierreGrupoNivelServicio.setGruposServicios(grupoServicio);
					}else{
						cierreGrupoNivelServicio = listaCierreGrupoNivelServicio.get(0);
					}
					cierreGrupoNivelServicio.setValorAcumulado(cierreGrupoNivelServicio.getValorAcumulado()+ 
							valorTotal.doubleValue());
					
					cierreNivelGrupoSerServicio.sincronizarCierreTemporal(cierreGrupoNivelServicio);
						
					
					cantidadSolicitada=1;
				}
			}else{
				
				for (DtoArticulosAutorizaciones dtoArticulo : autorizacion.getListaArticulos()) {
					
					dtoParametrosArticulos = new DTOBusquedaCierreTemporalArticulo();
					dtoParametrosServicios = new DTOBusquedaCierreTemporalServicio();
					DtoClaseInventario dtoClaseInventario = new DtoClaseInventario();
					
					dtoArt = articuloDAO.consultarArticuloPorID(dtoArticulo.getCodigoArticulo());
					dtoClaseInventario = claseInventarioDAO.obtenerClaseInventarioPorSungrupo(dtoArt.getCodigoSubGrupoArticulo());
					dtoArticulo.setCodigoSubGrupoArticulo(dtoArt.getCodigoSubGrupoArticulo());
					dtoArticulo.setCodigoClaseInvArticulo(dtoClaseInventario.getCodigo());
					dtoArticulo.setNombreClaseInvArticulo(dtoClaseInventario.getNombre());
					
					dtoParametrosServicios.setCierreServicio(ConstantesBD.acronimoNoChar);
					dtoParametrosServicios.setCodigoContrato(autorizacion.getDtoContrato().getCodigo());
					
					//listaCierreServArt = cierreServArtServicio.buscarCierreTemporalServicioArticulo(dtoParametrosServicios);
					
					if(Utilidades.isEmpty(listaCierreServArt)){
						Contratos contrato = new Contratos();						
						cierreServArt = new CierreTempServArt();
						
						contrato.setCodigo(autorizacion.getDtoContrato().getCodigo());
						cierreServArt.setContratos(contrato);
						cierreServArt.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
						cierreServArt.setValorAcumulado(0L);		
						cierreServArt.setCierreServicio(ConstantesBD.acronimoNoChar);
					}else{
						cierreServArt = listaCierreServArt.get(0);
					}
												
					if(dtoArticulo.getCantidadSolicitada()>1){
						cantidadSolicitada = dtoArticulo.getCantidadSolicitada();
					}
					valorTotal = dtoArticulo.getValorArticulo().multiply(
							new BigDecimal(cantidadSolicitada));
					
					cierreServArt.setValorAcumulado(cierreServArt.getValorAcumulado()+ (valorTotal.doubleValue()));
					//cierreServArtServicio.attachDirty(cierreServArt);		
						
					if(dtoArticulo.getCodigoClaseInvArticulo() != ConstantesBD.codigoNuncaValido){
						
						dtoParametrosArticulos.setCodigoContrato(autorizacion.getDtoContrato().getCodigo());
						dtoParametrosArticulos.setCodigoClaseInventario(dtoArticulo.getCodigoClaseInvArticulo());
						
						listaCierreClaseInvArt = cierreClaseInvArticuloMundo.buscarCierreTemporalClaseInventarioArticulo(dtoParametrosArticulos);
						
						if(Utilidades.isEmpty(listaCierreClaseInvArt)){
							Contratos contrato = new Contratos();						
							ClaseInventario claseInventario = new ClaseInventario();
							
							cierreClaseInvArt = new CierreTempClaseInvArt();
							
							contrato.setCodigo(autorizacion.getDtoContrato().getCodigo());
							claseInventario.setCodigo(dtoArticulo.getCodigoClaseInvArticulo());
													
							cierreClaseInvArt.setContratos(contrato);
							cierreClaseInvArt.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
							cierreClaseInvArt.setValorAcumulado(new BigDecimal(0));
							cierreClaseInvArt.setClaseInventario(claseInventario);
						}else{
							cierreClaseInvArt = listaCierreClaseInvArt.get(0);
						}
						
						cierreClaseInvArt.setValorAcumulado(cierreClaseInvArt.getValorAcumulado().add(valorTotal));
						
					//	cierreClaseInvArticuloMundo.sincronizarCierreTemporal(cierreClaseInvArt);
					}						
					
					if(dtoArt.getNivelAtencion()!=null && dtoArt.getNivelAtencion()!=ConstantesBD.codigoNuncaValidoLong){
						dtoParametrosArticulos.setCodigoNivelAtencion(dtoArt.getNivelAtencion());
						listaCierreNivelAtencion = cierreNivelArticuloServicio.buscarCierreTemporalNivelAtencion(dtoParametrosArticulos);
						
						if(Utilidades.isEmpty(listaCierreNivelAtencion)){
							Contratos contrato = new Contratos();						
							cierreNivelAtencion = new CierreTempNivelAtenArt();
							NivelAtencion nivelAtencion = new NivelAtencion();
							
							contrato.setCodigo(autorizacion.getDtoContrato().getCodigo());
							nivelAtencion.setConsecutivo(dtoArt.getNivelAtencion());
													
							cierreNivelAtencion.setContratos(contrato);
							cierreNivelAtencion.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
							cierreNivelAtencion.setValorAcumulado(0L);						
							cierreNivelAtencion.setNivelAtencion(nivelAtencion);
						}else{
							cierreNivelAtencion = listaCierreNivelAtencion.get(0);
						}
						
						cierreNivelAtencion.setValorAcumulado(cierreNivelAtencion.getValorAcumulado()+ valorTotal.doubleValue());
						
						cierreNivelArticuloServicio.sincronizarCierreTemporal(cierreNivelAtencion);
						
						if(dtoArticulo.getCodigoClaseInvArticulo() != ConstantesBD.codigoNuncaValido){
							listaCierreNivelAteClaseInvArticulo = 
								cierreNivelAteClaseInvArticuloMundo.buscarCierreTemporalNivelAtencionClaseInventarioArticulo(dtoParametrosArticulos);
							
							if(Utilidades.isEmpty(listaCierreNivelAteClaseInvArticulo)){
								Contratos contrato = new Contratos();
								ClaseInventario claseInventario = new ClaseInventario();
								NivelAtencion nivelAtencion = new NivelAtencion();
								cierreNivelAteClaseInvArticulo = new CierreTempNivAteClInvArt();
																
								contrato.setCodigo(autorizacion.getDtoContrato().getCodigo());
								claseInventario.setCodigo(dtoArticulo.getCodigoClaseInvArticulo());
								nivelAtencion.setConsecutivo(dtoArt.getNivelAtencion());
														
								cierreNivelAteClaseInvArticulo.setContratos(contrato);
								cierreNivelAteClaseInvArticulo.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
								cierreNivelAteClaseInvArticulo.setValorAcumulado(new BigDecimal(0));
								cierreNivelAteClaseInvArticulo.setClaseInventario(claseInventario);							
								cierreNivelAteClaseInvArticulo.setNivelAtencion(nivelAtencion);
							}else{
								cierreNivelAteClaseInvArticulo = listaCierreNivelAteClaseInvArticulo.get(0);
							}
								
							cierreNivelAteClaseInvArticulo.setValorAcumulado(cierreNivelAteClaseInvArticulo.getValorAcumulado().add(valorTotal));
							
							cierreNivelAteClaseInvArticuloMundo.sincronizarCierreTemporal(cierreNivelAteClaseInvArticulo);
						}	
					}
					cantidadSolicitada=1;
				}
			}
		}*/
		
	}
	
	
	
	
	/**
	 * Este Método se encarga de realizar el cálculo de la tarifa de los servicios o 
	 * medicamentos e insumos a través de la entidad subcontratada relacionada
	 * 
	 * @param dtoEntidadSub
	 * @param dtoProcesoAutorizacion
	 * @param con
	 * @param esServicio
	 * @param listaServiciosValidados
	 * @param listaArticulosValidados
	 * @return String
	 * @throws Exception
	 *
	 * @autor Angela Maria Aguirre
	*/
	private String calcularTarifaArticuloServicioEntidadSub(DtoEntidadSubcontratada dtoEntidadSub,
			DTOProcesoAutorizacion dtoProcesoAutorizacion,Connection con, boolean esServicio,
			ArrayList<DtoServiciosAutorizaciones> listaServiciosValidados,
			ArrayList<DtoArticulosAutorizaciones> listaArticulosValidados) throws Exception
	{
		
		CargosEntidadesSubcontratadas tarifaEntidadSub = new CargosEntidadesSubcontratadas();
		DTOCalculoTarifaServicioArticulo dtoCalculo = new DTOCalculoTarifaServicioArticulo();
		EsquemaTarifario esquema = new EsquemaTarifario();
		String serviciosArticulosNoAutorizados = "";
		IUsuarioXConvenioDAO usuarioConvenioDAO = CapitacionFabricaDAO.crearUsuarioXConvenioDAO();
		boolean esIngresoEstancia = false;
		boolean esAmbulaOPeticion = false;
		
		/* Se setea el codigo_pk en el campo de consecutivo porque el metodo de calcular tarífa así lo recibe y no se puede cambiar */
		dtoEntidadSub.setConsecutivo(String.valueOf(dtoEntidadSub.getCodigoPk()));
		
		dtoCalculo.setEntidadSubcontratada(dtoEntidadSub);
		dtoCalculo.setFechaVigencia(UtilidadFecha.getFechaActual()); 
		dtoCalculo.setEsServicio(esServicio);
		
		
		if(esServicio)
		{
			ArrayList<DtoServiciosAutorizaciones> listaServicios = new ArrayList<DtoServiciosAutorizaciones>();
			
			if(dtoProcesoAutorizacion.getDtoSolicitud()!=null){
				listaServicios = dtoProcesoAutorizacion.getDtoSolicitud().getListaServicios();
			}
			else if(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia()!=null){
				listaServicios = dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia().getListaServicios();
			}

			
			for( DtoServiciosAutorizaciones servicio : listaServicios)
			{
				double tarifa = 0;
				
				dtoCalculo.setCodigoArticuloServicio(servicio.getCodigoServicio());
				tarifa = tarifaEntidadSub.calcularTarifaEntidadSubcontratada(con, dtoCalculo, esquema);
				
				if(tarifa>0)
				{
					servicio.setValorServicio(new BigDecimal(tarifa));
					listaServiciosValidados.add(servicio);
				}else{
					serviciosArticulosNoAutorizados +=servicio.getDescripcionServicio() + " - ";
				}
			}
			
		}else{
			
			ArrayList<DtoArticulosAutorizaciones> listaArticulos = new ArrayList<DtoArticulosAutorizaciones>();
			InfoResponsableCobertura infoCobertura=null;
			Set<Contratos> hashSetContrato = null;
			Contratos contrato = null;
			Convenios convenioResponsable = null;
						
			if(dtoProcesoAutorizacion.getDtoSolicitud()!=null)
			{			
				listaArticulos = dtoProcesoAutorizacion.getDtoSolicitud().getListaArticulos();				
				
				if(dtoProcesoAutorizacion.isOrdenAmbulatoria())
				{	esAmbulaOPeticion	=true;
					contrato = new Contratos();
					contrato.setCodigo(dtoProcesoAutorizacion.getContratoAutorizacion());
				}
				
			}else if(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia()!=null){
				esIngresoEstancia=true;				
				
				//Se consulta el convenio del usuario capitado
				UsuarioXConvenio usuarioConvenio = usuarioConvenioDAO.buscarUsuarioConvenioPorPaciente(dtoProcesoAutorizacion.getPaciente().getCodigoPersona());
				contrato = new Contratos();
				contrato.setCodigo(usuarioConvenio.getContratos().getCodigo());
				listaArticulos = dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia().getListaArticulos();				
			}
			
			for( DtoArticulosAutorizaciones articulo : listaArticulos)
			{
				double tarifa = 0;
				
				hashSetContrato = new HashSet<Contratos>(0);
				
				dtoCalculo.setCodigoArticuloServicio(articulo.getCodigoArticulo());
				tarifa = tarifaEntidadSub.calcularTarifaEntidadSubcontratada(con, dtoCalculo, esquema);
								
				if(tarifa>0)
				{
					articulo.setValorArticulo(new BigDecimal(tarifa));
					
					if(esIngresoEstancia || esAmbulaOPeticion){
						
						if(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia()!=null)
						convenioResponsable = dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia().getConvenioResponsable();						
						else
							convenioResponsable = dtoProcesoAutorizacion.getDtoSolicitud().getConvenioResponsable();
					}else
					{
						//Se consulta el convenio responsable para cada artículo relacionado a la solicitud,
						//cuando es ingreso estancia el convenio responsable siempre es el mismo para todos los artículos						
						infoCobertura = Cobertura.validacionCoberturaArticulo(con,  dtoProcesoAutorizacion.getPaciente().getCodigoIngreso()+"", 
								dtoProcesoAutorizacion.getPaciente().getCodigoUltimaViaIngreso(),
								dtoProcesoAutorizacion.getPaciente().getCodigoTipoPaciente(), 
								articulo.getCodigoArticulo(), 
								dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt(),false);
						
						convenioResponsable = new Convenios();
						convenioResponsable.setCodigo(infoCobertura.getDtoSubCuenta().getConvenio().getCodigo());
						contrato = new Contratos();
						contrato.setCodigo(infoCobertura.getDtoSubCuenta().getContrato());
					}
					
					hashSetContrato.add(contrato);	
					
					convenioResponsable.setContratoses(hashSetContrato);
					
					articulo.setConvenioResponsable(convenioResponsable);
					
					listaArticulosValidados.add(articulo);
					
				}else{
					serviciosArticulosNoAutorizados +=articulo.getDescripcionArticulo() + " - ";
				}			
			}
		}
		
		return serviciosArticulosNoAutorizados;
	}
	
	
	
	/**
	 * Este Método se encarga de realizar el cálculo de la tarifa de los servicios o 
	 * medicamentos e insumos a través del cargo modificado.
	 * 
	 * @param dtoProcesoAutorizacion
	 * @param con
	 * @param esServicio
	 * @param tipoPaciente
	 * @param listaServiciosValidados
	 * @param listaArticulosValidados
	 * @param dtoEntidadSub
	 * @return String
	 * @throws Exception
	 *
	 * @autor Angela Maria Aguirre
	 */
	private String calcularTarifaArticuloServicioCargoModificado(DTOProcesoAutorizacion dtoProcesoAutorizacion,
			Connection con,	boolean esServicio, String tipoPaciente,
			ArrayList<DtoServiciosAutorizaciones> listaServiciosValidados,
			ArrayList<DtoArticulosAutorizaciones> listaArticulosValidados,
			DtoEntidadSubcontratada dtoEntidadSub)throws Exception
	{
		IUsuarioXConvenioDAO usuarioConvenioDAO = CapitacionFabricaDAO.crearUsuarioXConvenioDAO();
		
		InfoDatosInt tipoComplejidad=new InfoDatosInt();
		String serviciosArticulosNoAutorizados = "";		
		int codigoConvenio = 0;
		boolean esIngresoEstancia = false;
		boolean esAmbulaOPeticion	= false;
		int codigoContrato= 0;
		//int codigoEsquemaTarifario=0;
				
		
		if(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia()!=null 
				||dtoProcesoAutorizacion.isOrdenAmbulatoria())
		{
			tipoComplejidad.setCodigo(ConstantesBD.codigoNuncaValido);		
			
			//Se consulta el convenio del usuario capitado
			UsuarioXConvenio usuarioConvenio = usuarioConvenioDAO.buscarUsuarioConvenioPorPaciente(dtoProcesoAutorizacion.getPaciente().getCodigoPersona());
			codigoContrato = usuarioConvenio.getContratos().getCodigo();
			
			if(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia()!=null)
			{	esIngresoEstancia 	=true;
				esAmbulaOPeticion	=false;
			}else{
				esIngresoEstancia 	=false;
				esAmbulaOPeticion	=true;
			}
			
			//Para el caso de ingreso estancia el convenio se toma de la autorización de ingreso estancia
			if(esIngresoEstancia)
			codigoConvenio = dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia().getConvenioResponsable().getCodigo();
			else
				codigoConvenio = dtoProcesoAutorizacion.getDtoSolicitud().getConvenioResponsable().getCodigo();
			
		}else{//Solo para Ordenes Medicas
			tipoComplejidad = Utilidades.obtenerTipoComplejidadCuenta(con, dtoProcesoAutorizacion.getPaciente().getCodigoCuenta());
		}
		
		
		if(esServicio)
		{
			IEsqTarProcedimientoContratoDAO inventarioDAO = InventarioDAOFabrica.crearEsqTarProcedimientoContrato();
			DTOEsqTarProcedimientoContrato dtoEsquema = null;
			
			ArrayList<DtoServiciosAutorizaciones> listaServicios = new ArrayList<DtoServiciosAutorizaciones>();
			
			if(!esIngresoEstancia){
				listaServicios = dtoProcesoAutorizacion.getDtoSolicitud().getListaServicios();				
			}else{				
				listaServicios = dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia().getListaServicios();
			}			
			
			/* DCU 1106 - Versión cambio 1.1. Cristhian Murillo */
			IServiciosMundo servicioMundo = FacturacionFabricaMundo.crearServiciosMundo();
			String esquemaTarifarioAutocapitaSubCirugiasNoCurentos = null;
			esquemaTarifarioAutocapitaSubCirugiasNoCurentos = ValoresPorDefecto.getEsquemaTarifarioAutocapitaSubCirugiasNoCurentos(dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt());
			
			ArrayList<Integer> listaTiposLiquidacion = new ArrayList<Integer>();
			listaTiposLiquidacion.add(ConstantesBD.codigoTipoLiquidacionSoatGrupo);
			listaTiposLiquidacion.add(ConstantesBD.codigoTipoLiquidacionSoatUvr);
			
			ArrayList<String> listaTiposServicio = new ArrayList<String>();
			listaTiposServicio.add(ConstantesBD.codigoServicioNoCruentos+"");
			listaTiposServicio.add(ConstantesBD.codigoServicioQuirurgico+"");
			//----------------------------------------------------------------
			
			for( DtoServiciosAutorizaciones servicio : listaServicios)
			{
				double tarifa = 0;
				if(!esIngresoEstancia && !esAmbulaOPeticion)
				{//Solo cuando es Orden Medica
					InfoResponsableCobertura infoCobertura = Cobertura.validacionCoberturaServicio(
							con, dtoProcesoAutorizacion.getPaciente().getCodigoIngreso()+"",
							dtoProcesoAutorizacion.getPaciente().getCodigoUltimaViaIngreso(), 
							tipoPaciente,servicio.getCodigoServicio(), 
							dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt(),
							false, "");
					
					if(infoCobertura!=null && !UtilidadTexto.isEmpty(infoCobertura.getDtoSubCuenta().getSubCuenta())){
						codigoConvenio = infoCobertura.getDtoSubCuenta().getConvenio().getCodigo();	
						codigoContrato = infoCobertura.getDtoSubCuenta().getContrato();
					}
				}
				
				dtoEsquema = new DTOEsqTarProcedimientoContrato();
				
				DtoContrato dtoContrato = new DtoContrato();
				dtoContrato.setCodigo(codigoContrato);
				
				dtoEsquema.setContrato(dtoContrato);
				
				dtoEsquema = inventarioDAO.buscarEsquematarifarioPorContrato(dtoEsquema);
				
				if(dtoEsquema.getCodigo()!=ConstantesBD.codigoNuncaValidoLong)
				{
					ArrayList<DtoServicios> listaServiciosCirugiasNoCruentosGrupoUVR = new ArrayList<DtoServicios>();
					listaServiciosCirugiasNoCruentosGrupoUVR = servicioMundo.obtenerServicioLiquidacionPorEsquema(servicio.getCodigoServicio(), dtoEsquema.getCodigo().intValue(), listaTiposLiquidacion, listaTiposServicio);
					
					if(Utilidades.isEmpty(listaServiciosCirugiasNoCruentosGrupoUVR))
					{
						InfoTarifaYExcepcion infoTarifaYExcepcion=Cargos.obtenerValorTarifaYExcepcion(
								con, codigoConvenio,
								dtoProcesoAutorizacion.getPaciente().getCodigoContrato(), dtoEsquema.getCodigoEsquema(),
								dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt(),
								servicio.getCodigoServicio(), true, tipoPaciente,
								dtoProcesoAutorizacion.getPaciente().getCodigoUltimaViaIngreso(),
								tipoComplejidad.getCodigo(), "",
								dtoProcesoAutorizacion.getUsuarioSesion().getCodigoCentroAtencion());
							
						tarifa = infoTarifaYExcepcion.getValorTarifaTotalConExcepcion();
					}
					
					else
					{
						/* Si hay resultados el servicio es: 
						 * - Tipo Servicio: Cirugías y No Cruentos
						 * - Tipo Liquidación como: Grupo o UVR  */
						if(UtilidadTexto.isEmpty(esquemaTarifarioAutocapitaSubCirugiasNoCurentos)){
							/* Si NO esta definido el parámetro, no se puede generar la Tarifa 
							 * correspondiente y esta queda pendiente, se debe realizar lo que se explica más adelante para estos casos.  */
							tarifa = 0; //correcto?
						}
						else{
							/* Si SI esta definido se debe realizar el cálculo de la Tarifa con este esquema Tarifario, 
							 * utilizando el método Cálculo Tarifa Entidades Subcontratadas - 799. */
							
							CargosEntidadesSubcontratadas tarifaEntidadSub = new CargosEntidadesSubcontratadas();
							DTOCalculoTarifaServicioArticulo dtoCalculo = new DTOCalculoTarifaServicioArticulo();
							dtoCalculo.setEntidadSubcontratada(dtoEntidadSub);
							dtoCalculo.setFechaVigencia(UtilidadFecha.getFechaActual());
							dtoCalculo.setEsServicio(esServicio);
							EsquemaTarifario esquema = new EsquemaTarifario();
							
							dtoCalculo.setCodigoArticuloServicio(servicio.getCodigoServicio());
							tarifa = tarifaEntidadSub.calcularTarifaEntidadSubcontratada(con, dtoCalculo, esquema);
							
						}
					}
				}	
				
				
				if(tarifa>0){
					servicio.setValorServicio(new BigDecimal(tarifa));
					listaServiciosValidados.add(servicio);
					
				}else{
					serviciosArticulosNoAutorizados +=servicio.getDescripcionServicio() + " - ";
				}
			}
		}
		else
		{
			IEsqTarInventariosContratoDAO inventarioDAO = InventarioDAOFabrica.crearEsqTarInventariosContrato();
			DTOEsqTarInventarioContrato dtoEsquema = null;
			ArrayList<DtoArticulosAutorizaciones> listaArticulos = new ArrayList<DtoArticulosAutorizaciones>();
			Set<Contratos> setContrato = null;
			Contratos contrato =null;
			
			if(esIngresoEstancia){
				listaArticulos = dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia().getListaArticulos();				
			}else {				
				listaArticulos = dtoProcesoAutorizacion.getDtoSolicitud().getListaArticulos();
			}
			
			Convenios convenioResponsable=null;
			
			for( DtoArticulosAutorizaciones articulo : listaArticulos)
			{
				InfoResponsableCobertura infoCobertura=new InfoResponsableCobertura();
				double tarifa=0;
								
				if(!esIngresoEstancia && !esAmbulaOPeticion)
				{
					infoCobertura = Cobertura.validacionCoberturaArticulo(con,  dtoProcesoAutorizacion.getPaciente().getCodigoIngreso()+"", 
							dtoProcesoAutorizacion.getPaciente().getCodigoUltimaViaIngreso(),
							dtoProcesoAutorizacion.getPaciente().getCodigoTipoPaciente(), 
							articulo.getCodigoArticulo(), 
							dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt(),false);
					
					if(infoCobertura!=null && !UtilidadTexto.isEmpty(infoCobertura.getDtoSubCuenta().getSubCuenta())){
						codigoConvenio = infoCobertura.getDtoSubCuenta().getConvenio().getCodigo();	
						codigoContrato = infoCobertura.getDtoSubCuenta().getContrato();
					}
				}	
				
				dtoEsquema = new DTOEsqTarInventarioContrato();
				
				dtoEsquema.setContrato(new DtoContrato());
				dtoEsquema.getContrato().setCodigo(codigoContrato);
				
				dtoEsquema = inventarioDAO.buscarEsquematarifarioPorContrato(dtoEsquema);
				
				if(dtoEsquema.getCodigo()!=ConstantesBD.codigoNuncaValidoLong){
					InfoTarifaYExcepcion infoTarifaYExcepcion=Cargos.obtenerValorTarifaYExcepcion(
							con, codigoConvenio,
							infoCobertura.getDtoSubCuenta().getContrato(), dtoEsquema.getCodigoEsquema(),
							dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt(),
							articulo.getCodigoArticulo(), false, tipoPaciente,
							dtoProcesoAutorizacion.getPaciente().getCodigoUltimaViaIngreso(),
							tipoComplejidad.getCodigo(), "",
							dtoProcesoAutorizacion.getUsuarioSesion().getCodigoCentroAtencion());
											
					tarifa = infoTarifaYExcepcion.getValorTarifaTotalConExcepcion();
				}				
				
				if(tarifa>0){
					articulo.setValorArticulo(new BigDecimal(tarifa));
					convenioResponsable = new Convenios();
					setContrato = new HashSet<Contratos>(0);					
					contrato = new Contratos();
					
					contrato.setCodigo(codigoContrato);
					setContrato.add(contrato);		
					
					convenioResponsable.setCodigo(codigoConvenio);
					convenioResponsable.setContratoses(setContrato);									
					
					articulo.setConvenioResponsable(convenioResponsable);
					listaArticulosValidados.add(articulo);
					
				}else{
					serviciosArticulosNoAutorizados +=articulo.getDescripcionArticulo() + " - ";
				}
			}			
		}	
		return serviciosArticulosNoAutorizados;
	}
	
	
	/**
	 * 
	 * Este Método se encarga de realizar la validación de presupuesto
	 * para la generación de la autorización de capitación
	 * 
	 * @param Connection con, Cuenta cuenta,
	 *		DTOProcesoAutorizacion dtoProcesoAutorizacion,
	 *		ArrayList<DtoServiciosAutorizaciones> listaServiciosValidados,
	 * 		ArrayList<DtoArticulosAutorizaciones> listaArticulosValidados,
	 *		String serviciosArticulosNoAutorizados
	 *		
	 * @return ActionErrors
	 * @author, Angela Maria Aguirre
	 *
	 */
	private List<DtoValidacionPresupuesto> validarPresupuestoCapitacion(Connection con, Cuenta cuenta,
			DTOProcesoAutorizacion dtoProcesoAutorizacion,
			ArrayList<DtoServiciosAutorizaciones> listaServiciosValidados,
			ArrayList<DtoArticulosAutorizaciones> listaArticulosValidados,
			boolean esServicio,String tipoPaciente, boolean esIngresoEstancia,boolean esAmbulaOPetcion,
			String fechaCierreOrdenMedica, boolean realizarEliminacion)throws Exception{
		
		IConvenioDAO convenioDAO = FacturacionFabricaDAO.crearConvenioDAO();
		IContratoDAO contratoDAO = FacturacionFabricaDAO.crearContratoDAO();
		IValidacionPresupuestoCapitacionMundo validacionPresupuestoMundo = CapitacionFabricaMundo.crearValidacionPresupuestoCapitacionMundo();
		List<DtoValidacionPresupuesto> serviciosArticulosNoAutorizados= new ArrayList<DtoValidacionPresupuesto>();
		
		ResultadoBoolean comparacion=null;		
		int codigoConvenio = 0;
		Convenios convenio = new Convenios();
		Contratos contrato = new Contratos();
		int codigoContrato = 0;
				
		if(esIngresoEstancia){
			//Para el caso de ingreso estancia el convenio se toma de la autorización de ingreso estancia
			codigoConvenio = dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia()
				.getConvenioResponsable().getCodigo();
			codigoContrato = dtoProcesoAutorizacion.getContratoAutorizacion(); 
				//cuenta.getCodigoContrato();
		}else if(esAmbulaOPetcion){
			codigoConvenio	= dtoProcesoAutorizacion.getDtoSolicitud().getConvenioResponsable().getCodigo();
			
			codigoContrato	= dtoProcesoAutorizacion.getContratoAutorizacion(); 
		}
		
		if(esServicio){
			ArrayList<DtoServiciosAutorizaciones> listaServiciosConPresupuesto = new ArrayList<DtoServiciosAutorizaciones>();
			for( DtoServiciosAutorizaciones servicio : listaServiciosValidados){
				if(!esIngresoEstancia && !esAmbulaOPetcion){
					InfoResponsableCobertura infoResponsableCobertura= Cobertura.validacionCoberturaServicio(con, 
							cuenta.getCodigoIngreso()+"", Utilidades.convertirAEntero(cuenta.getCodigoViaIngreso()), 
							tipoPaciente, servicio.getCodigoServicio(), 
							dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt(), false, "");
					
					codigoConvenio = infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo();
					codigoContrato = infoResponsableCobertura.getDtoSubCuenta().getContrato();
				}
				
				comparacion=new ResultadoBoolean();
				convenio = convenioDAO.findById(codigoConvenio);
				contrato = contratoDAO.findById(codigoContrato);
				
				if(convenio != null && contrato != null && convenio.getManejaPresupCapitacion() != null 
						&& convenio.getManejaPresupCapitacion()==ConstantesBD.acronimoSiChar){						
					String fechaActual = UtilidadFecha.getFechaActual();
					
					comparacion = UtilidadFecha.compararFechas(fechaCierreOrdenMedica, null, fechaActual, null);
					
					if(!comparacion.isResultado()){
						//validacion CU 1180
						DtoValidacionPresupuesto dtoValidacionPresupuestoServicio = null;
						
						/*DtoValidacionPresupuesto dtoValidacionPresupuestoServicio = validacionPresupuestoMundo
								.validarPresupuestoCapitacionServicio(servicio, convenio.getCodigo(), 
										convenio.getNombre(), contrato.getCodigo(), contrato.getNumeroContrato());*/
						if(!dtoValidacionPresupuestoServicio.isValido()){
							//Se cancela el proceso para la solicitud para este servicio
							serviciosArticulosNoAutorizados.add(dtoValidacionPresupuestoServicio);
							Log4JManager.warning(dtoValidacionPresupuestoServicio.getMensaje());
						}else{
							listaServiciosConPresupuesto.add(servicio);
							if(realizarEliminacion){
								//Si la validación del presupuesto es exitosa, se borra la tabla
								//temporal del cierre de presupuesto.
								realizarEliminacion = eliminarAcumuladoCierreTemporal();
							}
						}
					}
				}
			}			
			listaServiciosValidados.clear();
			if(!listaServiciosConPresupuesto.isEmpty()){
				listaServiciosValidados.addAll(listaServiciosConPresupuesto);
			}
		}else{
			ArrayList<DtoArticulosAutorizaciones> listaArticulosConPresupuesto = new ArrayList<DtoArticulosAutorizaciones>();
			for( DtoArticulosAutorizaciones articulo : listaArticulosValidados){
				
				if(!esIngresoEstancia && !esAmbulaOPetcion){
					InfoResponsableCobertura infoResponsableCobertura= Cobertura.validacionCoberturaArticulo(con, 
							cuenta.getCodigoIngreso()+"", Utilidades.convertirAEntero(cuenta.getCodigoViaIngreso()), 
							tipoPaciente, articulo.getCodigoArticulo(), 
							dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt(), false);
					
					codigoConvenio = infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo();
					codigoContrato = infoResponsableCobertura.getDtoSubCuenta().getContrato();
				}								
				comparacion = new ResultadoBoolean();
				convenio = convenioDAO.findById(codigoConvenio);
				contrato = contratoDAO.findById(codigoContrato);
				Log4JManager.info("codigo del contrato: "+contrato.getCodigo()+" y numero contrato: "+contrato.getNumeroContrato());
				
				if(convenio != null && contrato != null && convenio.getManejaPresupCapitacion() != null
						&& convenio.getManejaPresupCapitacion()==ConstantesBD.acronimoSiChar){
					
					String fechaActual = UtilidadFecha.getFechaActual();
					
					comparacion = UtilidadFecha.compararFechas(fechaCierreOrdenMedica, null, fechaActual, null);
					
					if(!comparacion.isResultado()){
						//validacion CU 1180
						
						DtoValidacionPresupuesto dtoValidacionPresupuestoArticulo= null;
								/*
						DtoValidacionPresupuesto dtoValidacionPresupuestoArticulo= validacionPresupuestoMundo
						.validarPresupuestoCapitacionArticulo(articulo, convenio.getCodigo(), 
								convenio.getNombre(), contrato.getCodigo(), contrato.getNumeroContrato());*/
						if(!dtoValidacionPresupuestoArticulo.isValido()){
							//Se cancela el proceso	para la solicitud para este artículo								
							serviciosArticulosNoAutorizados.add(dtoValidacionPresupuestoArticulo);
							Log4JManager.warning(dtoValidacionPresupuestoArticulo.getMensaje());
						}else{
							listaArticulosConPresupuesto.add(articulo);
							if(realizarEliminacion){
								//Si la validación del presupuesto es exitosa, se borra la tabla
								//temporal del cierre de presupuesto.
								realizarEliminacion = eliminarAcumuladoCierreTemporal();
							}
						}
					}
				}
			}	
			listaArticulosValidados.clear();
			if(!listaArticulosConPresupuesto.isEmpty()){
				listaArticulosValidados.addAll(listaArticulosConPresupuesto);
			}
		}
		return  serviciosArticulosNoAutorizados;
	}
	
	
	/**
	 * 
	 * Este Método se encarga de borrar los registros de la tabla de cierre temporal dode la fecha
	 * de cierre se anterior a la fecha del sistema
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarAcumuladoCierreTemporal(){

		ICierreTempServArtServicio cierreServArtServicio = CapitacionFabricaServicio.crearCierreTempServArtServicio();
		ICierreTempNivelAtenServServicio cierreNivelAtenServServicio = CapitacionFabricaServicio.crearCierreTempNivelAtenServServicio();
		ICierreTempGrupoServicioServicio cierreGrupoServServicio = CapitacionFabricaServicio.crearCierreTempGrupoServicioServicio();
		ICierreTempNivelAteGruServServicio cierreNivelGrupoSerServicio = CapitacionFabricaServicio.crearCierreTempNivelAteGruServServicio();
		
		ICierreTempClaseInvArtMundo cierreClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempClaseInvArtMundo();
		ICierreTempNivelAtenArtServicio cierreNivelArticuloServicio = CapitacionFabricaServicio.crearCierreTempNivelAtenArtServicio();
		ICierreTempNivelAteClaseInvArtMundo cierreNivelAteClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempNivelAteClaseInvArtMundo();
		
		String fechaActual = UtilidadFecha.getFechaActual();
		DTOBusquedaCierreTemporalServicio dtoParametrosServicios = null ;
		DTOBusquedaCierreTemporalArticulo dtoParametrosArticulos = null;
		/*
		
	//	ArrayList<CierreTempServArt> cierreServicioArticulo = cierreServArtServicio.buscarCierreTemporalServicioArticulo(dtoParametrosServicios);
	//	ArrayList<CierreTempNivelAtenServ> cierreNivelServicio = cierreNivelAtenServServicio.buscarCierreTemporalNivelAtencion(dtoParametrosServicios);
		ArrayList<CierreTempGrupoServicio> cierreGrupoServicio = cierreGrupoServServicio.buscarCierreTemporalNivelAtencion(dtoParametrosServicios);
		ArrayList<CierreTempNivelAteGruServ> cierreNivelGrupoServicio =  cierreNivelGrupoSerServicio.buscarCierreTemporalNivelAtencionGrupoServicio(dtoParametrosServicios);
		
		ArrayList<CierreTempClaseInvArt> cierreClaseInvArticulo = cierreClaseInvArticuloMundo.buscarCierreTemporalClaseInventarioArticulo(dtoParametrosArticulos);
		ArrayList<CierreTempNivelAtenArt> cierreNivelAtencionArticulo = cierreNivelArticuloServicio.buscarCierreTemporalNivelAtencion(dtoParametrosArticulos);
		ArrayList<CierreTempNivAteClInvArt> cierreNivelAteClaseInvArticulo =  cierreNivelAteClaseInvArticuloMundo.buscarCierreTemporalNivelAtencionClaseInventarioArticulo(dtoParametrosArticulos);
		boolean eliminacion=false;
		
		//Se eliminan los registros de los acumulados de las tablas temporales,
		//donde la fecha de cierre sea anterior a la fecha actual
		/*if(!Utilidades.isEmpty(cierreServicioArticulo)){
			
			for (CierreTempServArt cierre : cierreServicioArticulo) {
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
					cierreServArtServicio.eliminarRegistro(cierre);
					
				}	
			}
		}*//*
		
		if(!Utilidades.isEmpty(cierreNivelServicio)){
			
			for (CierreTempNivelAtenServ cierre : cierreNivelServicio) {
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
					cierreNivelAtenServServicio.eliminarRegistro(cierre);
					
				}	
			}
		}
		
		if(!Utilidades.isEmpty(cierreGrupoServicio)){
			
			for (CierreTempGrupoServicio cierre : cierreGrupoServicio) {
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
					cierreGrupoServServicio.eliminarRegistro(cierre);
					
				}	
			}
		}	
		
		if(!Utilidades.isEmpty(cierreNivelGrupoServicio)){
			
			for (CierreTempNivelAteGruServ cierre : cierreNivelGrupoServicio) {
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
					cierreNivelGrupoSerServicio.eliminarRegistro(cierre);
					
				}	
			}
		}	
		
		if(!Utilidades.isEmpty(cierreClaseInvArticulo)){
			
			for (CierreTempClaseInvArt cierre : cierreClaseInvArticulo) {
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
					cierreClaseInvArticuloMundo.eliminarRegistro(cierre);
					
				}	
			}
		}
		
		if(!Utilidades.isEmpty(cierreNivelAtencionArticulo)){
			
			for (CierreTempNivelAtenArt cierre : cierreNivelAtencionArticulo) {
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
					cierreNivelArticuloServicio.eliminarRegistro(cierre);
					
				}	
			}
		}
		
		if(!Utilidades.isEmpty(cierreNivelAteClaseInvArticulo)){
			
			for (CierreTempNivAteClInvArt cierre : cierreNivelAteClaseInvArticulo) {
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
					cierreNivelAteClaseInvArticuloMundo.eliminarRegistro(cierre);
					
				}	
			}
		}*/
		
		//return eliminacion;
		return false;
	}
	
	
	/**
	 * Este Método se encarga de insertar en la base de datos
	 * la autorizacion de capitación subcontratada
	 * 
	 * @param DTOProcesoAutorizacion dtoProcesoAutorizacion
	 * @return DTOAutorizacionCapitacionSubcontratada
	 * @author, Angela Maria Aguirre
	 */
	private DTOAutorizacionCapitacionSubcontratada generarAutorizacionCapitacion(DTOProcesoAutorizacion dtoProcesoAutorizacion, AutorizacionesEntidadesSub autorizacion)throws Exception 
	{
		AutorizacionesCapitacionSub autorizacionCapitacion = new AutorizacionesCapitacionSub();	
		IAutorizacionesCapitacionSubDAO dao = ManejoPacienteDAOFabrica.crearAutorizacionCapitacion();
		Long consecutivo=0L;
		boolean transaction=UtilidadTransaccion.getTransaccion().isActive();
		DTOAutorizacionCapitacionSubcontratada autorCapitacion = null; 
				
		String consecutivoCapitacion = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, Integer.valueOf(dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt()));
		consecutivoAutoCapiSub.add(consecutivoCapitacion);
		
		/* Mt 413 ------------------------------------------------- */
		Connection conH = UtilidadPersistencia.getPersistencia().obtenerConexion();
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH, 
				ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, 
				Integer.valueOf(dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt()), 
				consecutivoCapitacion, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		/* -------------------------------------------------------- */
		
		
		// Tener en cuenta que cuando SI esta definido pero por ejemplo el año no corresponde el valor del consecutivo es -1
		if(!UtilidadTexto.isEmpty(consecutivoCapitacion)){ 
			consecutivo = Long.valueOf(consecutivoCapitacion);
		}		
		
		autorizacionCapitacion.setConsecutivo(consecutivo);
		autorizacionCapitacion.setAutorizacionesEntidadesSub(autorizacion);
		autorizacionCapitacion.setTipoEntQueSeAuto(autorizacion.getTipo());
		
		if(dtoProcesoAutorizacion.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoManual)){
			autorizacionCapitacion.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoManual);
			
		}else if(dtoProcesoAutorizacion.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutomatica)){
			autorizacionCapitacion.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAutomatica);
		}
		
		//Si la entidad subcontratada no existe en el sistema,
		//la autorización se registra con indicativo temporal igual a  'S'		
		if(dtoProcesoAutorizacion.getDtoEntidadSubcontratada().getCodigoPk()==ConstantesBD.codigoNuncaValidoLong)
		{
			autorizacionCapitacion.setIndicativoTemporal(ConstantesBD.acronimoSiChar);
			autorizacionCapitacion.setDescripcionEntidad(dtoProcesoAutorizacion.getDtoEntidadSubcontratada().getRazonSocial());			
			autorizacionCapitacion.setDireccionEntidad(dtoProcesoAutorizacion.getDtoEntidadSubcontratada().getDireccion());
			autorizacionCapitacion.setTelefonoEntidad(dtoProcesoAutorizacion.getDtoEntidadSubcontratada().getTelefono());
		}else{
			autorizacionCapitacion.setIndicativoTemporal(ConstantesBD.acronimoNoChar);
		}
		
		//Se asigna el convenio recobro
		if(dtoProcesoAutorizacion.getConvenioRecobro()!=null)
		{
			Convenios convenioRecobro = new Convenios();
			convenioRecobro.setCodigo(dtoProcesoAutorizacion.getConvenioRecobro().getCodigo());
			autorizacionCapitacion.setConvenios(convenioRecobro);
		}
		if(!UtilidadTexto.isEmpty(dtoProcesoAutorizacion.getDescripcionConvenioRecobro())){
			autorizacionCapitacion.setOtroConvenioRecobro(dtoProcesoAutorizacion.getDescripcionConvenioRecobro());				
		}
		
		if(dtoProcesoAutorizacion.getIndicadorPrioridad()!=null)
		{
			autorizacionCapitacion.setIndicadorPrioridad(dtoProcesoAutorizacion.getIndicadorPrioridad());
		}
			
		EstratosSociales clasificacionSocioEcono = new EstratosSociales();		
		TiposAfiliado tipoAfiliado=new TiposAfiliado();
		
		if (!UtilidadTexto.isEmpty(dtoProcesoAutorizacion.getClasificacionSocioEcono()) &&
				dtoProcesoAutorizacion.getClasificacionSocioEcono()!=0){
			clasificacionSocioEcono.setCodigo(dtoProcesoAutorizacion.getClasificacionSocioEcono());
		}else {
			clasificacionSocioEcono=null;			
		}
			
		if(!UtilidadTexto.isEmpty(dtoProcesoAutorizacion.getTipoAfiliado())){
			String cadenaTipoAf = dtoProcesoAutorizacion.getTipoAfiliado();
			char tipoAfili = cadenaTipoAf.charAt(0);
			tipoAfiliado.setAcronimo(tipoAfili);
		}else{
			tipoAfiliado=null;
		}
		
		autorizacionCapitacion.setTiposAfiliado(tipoAfiliado);
		autorizacionCapitacion.setEstratosSociales(clasificacionSocioEcono);		
					
		transaction = dao.sincronizarAutorizacionCapitacioncontratada(autorizacionCapitacion);
				

		if(transaction)
		{
			//Se guarda la relación de la autorización de capitación con la 
			//autorización de ingreso estancia		
			if(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia()!=null)
			{
				AutorizacionesEstanciaCapita autorizacionEstanciaCapita = new AutorizacionesEstanciaCapita();
				IAutorizacionesEstanciaCapitaDAO daoAutorEstanciaCapita = ManejoPacienteDAOFabrica.crearAutorizacionesEstanciaCapita();
				AutorizacionesIngreEstancia autoIngresoEstancia = new AutorizacionesIngreEstancia();
				autoIngresoEstancia.setCodigoPk(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia().getCodigoPk());			
				autorizacionEstanciaCapita.setAutorizacionesCapitacionSub(autorizacionCapitacion);
				autorizacionEstanciaCapita.setAutorizacionesIngreEstancia(autoIngresoEstancia);
				daoAutorEstanciaCapita.guardarAutorizacionEntidadSubcontratada(autorizacionEstanciaCapita);
			}
			
			//Se guarda el historial de la autorización		
			guardarHistorialAutorCapitacionSub(autorizacionCapitacion,dtoProcesoAutorizacion,autorizacion);
			
			//Se crea el objeto de la autorización			
			autorCapitacion = new DTOAutorizacionCapitacionSubcontratada();
			
			autorCapitacion.setCodigoPK(autorizacionCapitacion.getCodigoPk());
			autorCapitacion.setConsecutivo(autorizacionCapitacion.getConsecutivo());
			autorCapitacion.setCodigoConvenioRecobro(autorizacionCapitacion.getConvenios());
			autorCapitacion.setIndicadorPrioridad(autorizacionCapitacion.getIndicadorPrioridad());
			autorCapitacion.setIndicativoTemporal(autorizacionCapitacion.getIndicativoTemporal());
			autorCapitacion.setTipoAutorizacion(autorizacionCapitacion.getTipoAutorizacion());
		}
			
		return autorCapitacion;
	}

	
	
	/**
	 * Este Método se encarga de generar la autorización de entidad 
	 * 
	 * @param dtoProcesoAutorizacion
	 * @param fechaVencimiento
	 * @param cuenta
	 * @param listaArticulosValidados
	 * @param listaServiciosValidados
	 * @param con
	 * @param tipoPaciente
	 * @return ArrayList<DTOAutorEntidadSubcontratadaCapitacion>
	 * @throws Exception
	 *
	 * @author, Angela Maria Aguirre
	 */
	private ArrayList<DTOAutorEntidadSubcontratadaCapitacion> generarAutorizacionEntidadSubcontratada(
			DTOProcesoAutorizacion dtoProcesoAutorizacion, Date fechaVencimiento, Cuenta cuenta,ArrayList<DtoArticulosAutorizaciones> listaArticulosValidados,
			ArrayList<DtoServiciosAutorizaciones> listaServiciosValidados, Connection con, String tipoPaciente)throws Exception 
	{
		
		ArrayList<DTOAutorEntidadSubcontratadaCapitacion> listaAutorizaciones = new ArrayList<DTOAutorEntidadSubcontratadaCapitacion>();
		
		AutorizacionesEntidadesSub autorizacionesEntidadesSub = null;
		Convenios convenio 						= new Convenios();
		InfoResponsableCobertura infoCobertura 	= null;
		DtoContrato dtoContrato 				= null;
		int codigoConvenio 						= ConstantesBD.codigoNuncaValido;
		int numeroOrden							= ConstantesBD.codigoNuncaValido;
		DTOAutorEntidadSubcontratadaCapitacion dtoAutorizacion 	= null;
		EntidadesSubcontratadas entidadSubcontratada 			= new EntidadesSubcontratadas();		
		
		
	
		boolean transaction=UtilidadTransaccion.getTransaccion().isActive();
		//String consecutivo="";		
		
		Date fechaActual = UtilidadFecha.getFechaActualTipoBD();				
		String horaActual = UtilidadFecha.getHoraActual();
		entidadSubcontratada.setCodigoPk(dtoProcesoAutorizacion.getDtoEntidadSubcontratada().getCodigoPk());
		
				
		if(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia()!=null){
			codigoConvenio = dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia().getConvenioResponsable().getCodigo();	
		}else if(dtoProcesoAutorizacion.getDtoSolicitud()!=null){				
			numeroOrden = dtoProcesoAutorizacion.getDtoSolicitud().getNumeroSolicitud();
		}
		
		
		if(!Utilidades.isEmpty(listaServiciosValidados))
		{
			IAutorizacionesEntSubServiDAO autorizacionServiDAO 				= ManejoPacienteDAOFabrica.crearAurorizacionEntidadSubServi();			
			Servicios servicio 												= null;			
			AutorizacionesEntSubServi autorizacionesEntSubServi 			= null;
			ArrayList<DtoServiciosAutorizaciones> listaServiciosAutorizados = null;
			
			//Para el caso de solicitudes de servicios, así se tengan varios servicios asociados, el convenios será solo uno y se toma del convenio asociado a la solicitud
			if(dtoProcesoAutorizacion.getDtoSolicitud()!=null){				
				codigoConvenio = dtoProcesoAutorizacion.getDtoSolicitud().getConvenioResponsable().getCodigo();				
			}
			convenio.setCodigo(codigoConvenio);
			
			//Se crea una autorización por cada servicio de la orden
			for( DtoServiciosAutorizaciones registro : listaServiciosValidados)
			{	
				listaServiciosAutorizados 	= new ArrayList<DtoServiciosAutorizaciones>();
				autorizacionesEntidadesSub 	= new AutorizacionesEntidadesSub();
				autorizacionesEntSubServi 	= new AutorizacionesEntSubServi();				
				servicio 					= new Servicios();
				infoCobertura				= new InfoResponsableCobertura();
				
				//Se inserta la autorización, una por cada servicio		
				autorizacionesEntidadesSub.setConsecutivoAutorizacion(obtenerconsecutivoAutorizacion(dtoProcesoAutorizacion)); 
				autorizacionesEntidadesSub.setEntidadesSubcontratadas(entidadSubcontratada);
				autorizacionesEntidadesSub.setTipo(dtoProcesoAutorizacion.getTipoEntidadAutoriza());
				autorizacionesEntidadesSub.setEstado(ConstantesIntegridadDominio.acronimoAutorizado);					
				autorizacionesEntidadesSub.setFechaAutorizacion(fechaActual);
				autorizacionesEntidadesSub.setHoraAutorizacion(horaActual);
				autorizacionesEntidadesSub.setFechaVencimiento(fechaVencimiento);
				autorizacionesEntidadesSub.setConvenios(convenio);
				autorizacionesEntidadesSub.setContabilizado(ConstantesBD.acronimoNo);				
				autorizacionesEntidadesSub.setFechaModificacion(fechaActual);
				autorizacionesEntidadesSub.setHoraModificacion(horaActual);
				autorizacionesEntidadesSub.setUsuarioModificacion(dtoProcesoAutorizacion.getUsuarioSesion().getLoginUsuario());
				autorizacionesEntidadesSub.setObservaciones(dtoProcesoAutorizacion.getObservaciones());
				autorizacionesEntidadesSub.setContabilizadoAnulacion(ConstantesBD.acronimoNo);
				
				Pacientes paciente = new Pacientes();
				paciente.setCodigoPaciente(dtoProcesoAutorizacion.getPaciente().getCodigoPersona());
				
				autorizacionesEntidadesSub.setPacientes(paciente);
				
				asociarDatosAutorizacion(dtoProcesoAutorizacion, autorizacionesEntidadesSub, numeroOrden);
				// autorizacion.setSolicitudes(solicitud);
				
				Instituciones institucion = new Instituciones();
				institucion.setCodigo(dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt());
				
				autorizacionesEntidadesSub.setInstituciones(institucion);
				
				
			    /* Si la entidad subcontratada no existe y fué seleccionada como otra, se genera la autorización donde la entidad subcontratada 
			     	se registra con su pk = -1. Este proceso se realiza para mantener la integridad en la base de datos, pasa a ser realmente una 
			     	autorización válida para el sistema cuando se cree la entidad subcontratada. */				
				
				transaction = autorizacionDAO.sincronizarAutorizacionEntidadSubcontratada(autorizacionesEntidadesSub); 	
				
				if(transaction)
				{
					//Se inserta el servicio de la autorización
					if(registro.getNivelAutorizacion() != ConstantesBD.codigoNuncaValido)
					{
						NivelAutorizacion nivelAutorizacion;
						nivelAutorizacion = new NivelAutorizacion();
						nivelAutorizacion.setCodigoPk(registro.getNivelAutorizacion());
						autorizacionesEntSubServi.setNivelAutorizacion(nivelAutorizacion);
					}
					
					servicio.setCodigo(registro.getCodigoServicio());				
					autorizacionesEntSubServi.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
					autorizacionesEntSubServi.setServicios(servicio);
					autorizacionesEntSubServi.setCantidad(registro.getCantidadSolicitada());
					
					
					if(registro.getFinalidadServicio()!=null){
						FinalidadesServicio finalidadServicio = new FinalidadesServicio();
						finalidadServicio.setCodigo(registro.getFinalidadServicio());
						autorizacionesEntSubServi.setFinalidadesServicio(finalidadServicio);	
					}				
									
					autorizacionesEntSubServi.setUrgente(registro.getUrgente());				
					autorizacionesEntSubServi.setValorTarifa(registro.getValorServicio());
					
					Contratos contratoConvenioResponsable=new Contratos();
					
					if (dtoProcesoAutorizacion.getContratoAutorizacion()!= null){
						contratoConvenioResponsable.setCodigo(dtoProcesoAutorizacion.getContratoAutorizacion());
					}else {
						contratoConvenioResponsable=null;
					}
					
					autorizacionesEntSubServi.setContratos(contratoConvenioResponsable);					
					
					autorizacionServiDAO.guardarServicioAutorizacionEntidadSub(autorizacionesEntSubServi);
					
													
					if (!UtilidadTexto.isEmpty(dtoProcesoAutorizacion.getCentroCostoResponde()))
					{
						CentrosCosto centroCostoResponde = new CentrosCosto();
						centroCostoResponde.setCodigo(dtoProcesoAutorizacion.getCentroCostoResponde());			
						Set autoEntSubMont = autorizacionesEntidadesSub.getAutorizacionesEntSubMontoses();
						
						AutorizacionesEntSubMontos autoEntMonto = null;
						for (Object entidad : autoEntSubMont) {
							autoEntMonto = (AutorizacionesEntSubMontos) entidad;
						} 
						guardarAutoCapiXCentroCosto(autoEntMonto,centroCostoResponde);
					}
					
					
					/*
					if (dtoProcesoAutorizacion.getDtoEntidadSubcontratada().getCodigoPk() != ConstantesBD.codigoNuncaValidoLong){
						
						generarTarifasEntidadSub(dtoProcesoAutorizacion, autorizacionesEntidadesSub, entidadSubcontratada, 
								registro.getCodigoServicio(), registro.getValorServicio(), true, con);						
					}
					*/
					
					//5. Se genera la autorización de capitación subcontratada		
					// Si la entidad subcontratada no existe y fué seleccionada como otra, se
					// genera la autorización pero con indicativo temporal mientras se crea la entidad subcontratada
									
					DTOAutorizacionCapitacionSubcontratada autorCapitacionSub =
						generarAutorizacionCapitacion(dtoProcesoAutorizacion,autorizacionesEntidadesSub);
								
					//Se crea el objeto de la autorización registrada
					if(autorCapitacionSub != null){
						dtoAutorizacion = new DTOAutorEntidadSubcontratadaCapitacion();
						dtoAutorizacion.setAutorizacion(autorizacionesEntidadesSub.getConsecutivo());
						
						//MT 3488 DCU 1007 v1.3 mostrar el número de autorización de la entidad subcontratada						
						dtoAutorizacion.setConsecutivoAutorizEntidadSub(autorizacionesEntidadesSub.getConsecutivoAutorizacion());
						
						dtoAutorizacion.setFechaAutorizacion(autorizacionesEntidadesSub.getFechaAutorizacion());
						dtoAutorizacion.setFechaVencimiento(autorizacionesEntidadesSub.getFechaVencimiento());
						dtoAutorizacion.setEstado(autorizacionesEntidadesSub.getEstado());
						dtoAutorizacion.setRazonSocialEntidadAutoriza(dtoProcesoAutorizacion.getUsuarioSesion().getInstitucion());
						
						DtoUsuarioPersona usuarioAutoriza = new DtoUsuarioPersona();
						usuarioAutoriza.setNombre(dtoProcesoAutorizacion.getUsuarioSesion().getNombreUsuario());				
						dtoAutorizacion.setUsuarioAutoriza(usuarioAutoriza);
						
						listaServiciosAutorizados.add(registro);
						dtoAutorizacion.setListaServicios(listaServiciosAutorizados);	
						dtoAutorizacion.setAutorCapitacion(autorCapitacionSub);
						dtoAutorizacion.setConsecutivoAutorizacion(String.valueOf(autorCapitacionSub.getConsecutivo()));
						dtoAutorizacion.setCodigoPaciente(autorizacionesEntidadesSub.getPacientes().getCodigoPaciente());
						
						dtoAutorizacion.setConvenioResponsable(autorizacionesEntidadesSub.getConvenios());
						
						if(numeroOrden!=ConstantesBD.codigoNuncaValido){
							dtoAutorizacion.setNumeroSolicitudEntSub(numeroOrden);	
						}					
						
						
						dtoContrato = new DtoContrato();
						if(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia()!=null || dtoProcesoAutorizacion.isOrdenAmbulatoria())
						{						
							dtoContrato.setCodigo(dtoProcesoAutorizacion.getContratoAutorizacion());
						}else{
							//Se obtiene el contrato asociado al convenio de la autorización, esto para realizar
							//el acumulado del cierre temporal de presupuesto
							infoCobertura = Cobertura.validacionCoberturaServicio(
									con, dtoProcesoAutorizacion.getPaciente().getCodigoIngreso()+"",//
									dtoProcesoAutorizacion.getPaciente().getCodigoUltimaViaIngreso(), 
									tipoPaciente,registro.getCodigoServicio(), 
									dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt(),
									false, "");
							
							dtoContrato.setCodigo(infoCobertura.getDtoSubCuenta().getContrato());
						}
						
						dtoAutorizacion.setDtoContrato(dtoContrato);
						
						listaAutorizaciones.add(dtoAutorizacion);
					}
				}				
			}		
			
		}
		else if(!Utilidades.isEmpty(listaArticulosValidados))
		{
			IAutorizacionesEntSubArticuloDAO autorizacionArticuloDAO 			= ManejoPacienteDAOFabrica.crearAurorizacionEntidadSubArticulo();
			AutorizacionesEntSubArticu autorizacionesEntSubArticu 				= null;
			Articulo articulo 													= null;
			ArrayList<DtoArticulosAutorizaciones> listaArticulosIgualConvenio	= null;
			ArrayList<DtoArticulosAutorizaciones> listaArticulosAutorizados 	= null;
			int cantidadEliminados 												= 0;
			Contratos contrato													= null;
			
			//Se genera autorización por el o los artículos que tengan el mismo convenio responsable			
			for(int i=0; i<listaArticulosValidados.size();i++)
			{
				autorizacionesEntidadesSub 	= new AutorizacionesEntidadesSub();
				listaArticulosIgualConvenio = new ArrayList<DtoArticulosAutorizaciones>();				
				listaArticulosAutorizados 	= new ArrayList<DtoArticulosAutorizaciones>();	
				
				listaArticulosIgualConvenio.add(listaArticulosValidados.get(i));
				
				for(int j=i+1; j<listaArticulosValidados.size();j++)
				{
					if((listaArticulosValidados.get(i).getConvenioResponsable().getCodigo()) == 
						(listaArticulosValidados.get(j-cantidadEliminados).getConvenioResponsable().getCodigo()))
					{
						listaArticulosIgualConvenio.add(listaArticulosValidados.get(j-cantidadEliminados));						
						listaArticulosValidados.remove(j-cantidadEliminados);
						cantidadEliminados++;
					}
				}
				
				//Para el caso de solicitudes, se debe tomar el convenio de la cobertura de cada artículo
				if(dtoProcesoAutorizacion.getDtoSolicitud()!=null)
				{
					codigoConvenio=0;
					codigoConvenio =listaArticulosIgualConvenio.get(0).getConvenioResponsable().getCodigo();
					convenio.setCodigo(codigoConvenio);
				}
				
				if(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia()!=null){	
					convenio.setCodigo(codigoConvenio);
				}
				
				if(listaArticulosIgualConvenio!=null && listaArticulosIgualConvenio.size()>0)
				{
					//Se inserta la autorización		
					autorizacionesEntidadesSub.setConsecutivoAutorizacion(obtenerconsecutivoAutorizacion(dtoProcesoAutorizacion)); 
					autorizacionesEntidadesSub.setEntidadesSubcontratadas(entidadSubcontratada);
					autorizacionesEntidadesSub.setTipo(dtoProcesoAutorizacion.getTipoEntidadAutoriza());			
					autorizacionesEntidadesSub.setEstado(ConstantesIntegridadDominio.acronimoAutorizado);					
					autorizacionesEntidadesSub.setFechaAutorizacion(fechaActual);
					autorizacionesEntidadesSub.setHoraAutorizacion(horaActual);
					autorizacionesEntidadesSub.setFechaVencimiento(fechaVencimiento);
					autorizacionesEntidadesSub.setConvenios(convenio);
					autorizacionesEntidadesSub.setContabilizado(ConstantesBD.acronimoNoChar+"");				
					autorizacionesEntidadesSub.setFechaModificacion(fechaActual);
					autorizacionesEntidadesSub.setHoraModificacion(horaActual);
					autorizacionesEntidadesSub.setUsuarioModificacion(dtoProcesoAutorizacion.getUsuarioSesion().getLoginUsuario());
					autorizacionesEntidadesSub.setObservaciones(dtoProcesoAutorizacion.getObservaciones());
					autorizacionesEntidadesSub.setContabilizadoAnulacion(ConstantesBD.acronimoNo);
					
					Pacientes paciente = new Pacientes();
					paciente.setCodigoPaciente(dtoProcesoAutorizacion.getPaciente().getCodigoPersona());
					autorizacionesEntidadesSub.setPacientes(paciente);
					
					asociarDatosAutorizacion(dtoProcesoAutorizacion, autorizacionesEntidadesSub, numeroOrden);
					//autorizacion.setSolicitudes(solicitud);
					
					Instituciones institucion = new Instituciones();
					institucion.setCodigo(dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt());					
					autorizacionesEntidadesSub.setInstituciones(institucion);				
					
					/* Si la entidad subcontratada no existe y fué seleccionada como otra, se
					 	genera la autorización donde la entidad subcontratada se registra con su pk -1.
						Este proceso se realiza para mantener la integridad en la base de datos,
						pasa a ser realmente una autorización válida para el sistema
						uando se cree la entidad subcontratada. */
					transaction = autorizacionDAO.sincronizarAutorizacionEntidadSubcontratada(autorizacionesEntidadesSub);		
					
					if(transaction)
					{						
						//Se guardan los artículos que han sido autorizados
						for(DtoArticulosAutorizaciones registro : listaArticulosIgualConvenio)
						{
							articulo = new Articulo();				
							autorizacionesEntSubArticu = new AutorizacionesEntSubArticu();
														
							articulo.setCodigo(registro.getCodigoArticulo());
							autorizacionesEntSubArticu.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
							autorizacionesEntSubArticu.setArticulo(articulo);
							autorizacionesEntSubArticu.setNroDosisTotal(registro.getCantidadSolicitada());
							autorizacionesEntSubArticu.setDosis(registro.getDosisFormulacion());
							autorizacionesEntSubArticu.setVia(registro.getViaFormulacion());
							autorizacionesEntSubArticu.setTipoFrecuencia(registro.getTipoFrecuenciaFormulacion());
							if(registro.getFrecuenciaFormulacion()!=null){
								autorizacionesEntSubArticu.setFrecuencia(registro.getFrecuenciaFormulacion());
							}						
							autorizacionesEntSubArticu.setDiasTratamiento(registro.getDiasTratamientoFormulacion());	
							
							if(registro.getDosisXArticuloID()!=null && registro.getDosisXArticuloID()!=ConstantesBD.codigoNuncaValidoLong)
							{
								IUnidosisXArticuloDAO unidosisDAO = InventarioDAOFabrica.crearUnidosisXArticulo();
								UnidosisXArticulo unidosis = unidosisDAO.buscarPorID(registro.getDosisXArticuloID());								
								autorizacionesEntSubArticu.setUnidosisXArticulo(unidosis);
							}
							
							//Este dato se agrega porque se necesita un estado para saber si el articulo ha sido despachado
							autorizacionesEntSubArticu.setEstado(ConstantesIntegridadDominio.acronimoEstadoPendiente);
							autorizacionesEntSubArticu.setValorTarifa(registro.getValorArticulo());				
							
							if(registro.getNivelAutorizacion() != ConstantesBD.codigoNuncaValido)
							{
								NivelAutorizacion nivelAutorizacion;
								nivelAutorizacion = new NivelAutorizacion();
								nivelAutorizacion.setCodigoPk(registro.getNivelAutorizacion());
								autorizacionesEntSubArticu.setNivelAutorizacion(nivelAutorizacion);
							}
							
							Contratos contratoConvenioResponsable=new Contratos();
							
							if (dtoProcesoAutorizacion.getContratoAutorizacion()!= null){
								contratoConvenioResponsable.setCodigo(dtoProcesoAutorizacion.getContratoAutorizacion());
							}else {
								contratoConvenioResponsable=null;
							}
							
							//contratoConvenioResponsable.setCodigo(dtoProcesoAutorizacion.getContratoAutorizacion());
							autorizacionesEntSubArticu.setContratos(contratoConvenioResponsable);		
							
							autorizacionArticuloDAO.guardarArticuloAutorizacionEntidadSub(autorizacionesEntSubArticu);

							
							/*
							if (dtoProcesoAutorizacion.getDtoEntidadSubcontratada().getCodigoPk() != ConstantesBD.codigoNuncaValidoLong){
								
								generarTarifasEntidadSub(dtoProcesoAutorizacion, autorizacionesEntidadesSub, entidadSubcontratada, 
										registro.getCodigoArticulo(), registro.getValorArticulo(), true, con);							
							}*/
							
							listaArticulosAutorizados.add(registro);
						}
						
						
						Set autoEntSubMont = autorizacionesEntidadesSub.getAutorizacionesEntSubMontoses();
						AutorizacionesEntSubMontos autoEntMonto = null;
						for (Object entidad : autoEntSubMont) {
							autoEntMonto = (AutorizacionesEntSubMontos) entidad;
						} 
						
						if(!Utilidades.isEmpty(dtoProcesoAutorizacion.getListaCentrosCosto()))
						{
							for (DtoCentroCosto dtoCentroCosto : dtoProcesoAutorizacion.getListaCentrosCosto()) 
							{
								CentrosCosto centroCostoResponde = new CentrosCosto();
								centroCostoResponde.setCodigo(dtoCentroCosto.getCodigoCentroCosto());			
								guardarAutoCapiXCentroCosto(autoEntMonto,centroCostoResponde);
							}
						}else {
							CentrosCosto centroCostoResponde = new CentrosCosto();
							centroCostoResponde.setCodigo(dtoProcesoAutorizacion.getCentroCostoResponde());			
							guardarAutoCapiXCentroCosto(autoEntMonto,centroCostoResponde);
						}
						
						
						//5. Se genera la autorización de capitación subcontratada		
						//Si la entidad subcontratada no existe y fué seleccionada como otra, se
						//genera la autorización pero con indicativo temporal mientras se crea la entidad subcontratada			
						DTOAutorizacionCapitacionSubcontratada autorCapitacionSub = generarAutorizacionCapitacion(dtoProcesoAutorizacion,autorizacionesEntidadesSub);
						if(autorCapitacionSub != null){				
							convenio = new Convenios();
							
							//Se crea el objeto de la autorización registrada
							dtoAutorizacion = new DTOAutorEntidadSubcontratadaCapitacion();
							dtoAutorizacion.setAutorizacion(autorizacionesEntidadesSub.getConsecutivo());
							dtoAutorizacion.setFechaAutorizacion(autorizacionesEntidadesSub.getFechaAutorizacion());
							dtoAutorizacion.setFechaVencimiento(autorizacionesEntidadesSub.getFechaVencimiento());
							dtoAutorizacion.setEstado(autorizacionesEntidadesSub.getEstado());
							dtoAutorizacion.setRazonSocialEntidadAutoriza(dtoProcesoAutorizacion.getUsuarioSesion().getInstitucion());
							dtoAutorizacion.setDtoEntidadSubcontratada(dtoProcesoAutorizacion.getDtoEntidadSubcontratada());
							dtoAutorizacion.setHoraAutorizacion(autorizacionesEntidadesSub.getHoraAutorizacion());
							
							DtoUsuarioPersona usuarioAutoriza = new DtoUsuarioPersona();
							usuarioAutoriza.setNombre(dtoProcesoAutorizacion.getUsuarioSesion().getNombreUsuario());				
							dtoAutorizacion.setUsuarioAutoriza(usuarioAutoriza);
							
							if(numeroOrden!=ConstantesBD.codigoNuncaValido){
								dtoAutorizacion.setNumeroSolicitudEntSub(numeroOrden);	
							}				
							dtoAutorizacion.setTipoAutorizacion(autorizacionesEntidadesSub.getTipo());				
							dtoAutorizacion.setListaArticulos(listaArticulosAutorizados);
							dtoAutorizacion.setAutorCapitacion(autorCapitacionSub);
							dtoAutorizacion.setObservacionesGenerales(autorizacionesEntidadesSub.getObservaciones());
							dtoAutorizacion.setConsecutivoAutorizacion(String.valueOf(autorCapitacionSub.getConsecutivo()));
							dtoAutorizacion.setCodigoPaciente(autorizacionesEntidadesSub.getPacientes().getCodigoPaciente());
							
							dtoAutorizacion.setConvenioResponsable(autorizacionesEntidadesSub.getConvenios());
													
							//Se obtiene el contrato asociado al convenio de la autorización, esto para realizar el acumulado del cierre temporal de presupuesto			
							contrato = (Contratos)listaArticulosIgualConvenio.get(0).getConvenioResponsable().getContratoses().iterator().next();
							
							dtoContrato = new DtoContrato();
							dtoContrato.setCodigo(contrato.getCodigo());
							
							dtoAutorizacion.setDtoContrato(dtoContrato);						
							
							listaAutorizaciones.add(dtoAutorizacion);
						}
					}
				}				
			}
		}			
		return listaAutorizaciones;		
	}
	
	
	
	/**
	 * Para las ordenes medicas, cargos directos que se autorizan con entidad subcontratada se debe generar el cargo en tarifas por entidades 
	 * subcontratadas para que sean contabilizadas en la interfaz 1E 
	 * 
	 * @param dtoProcesoAutorizacion
	 * @param autorizacion
	 * @param entidadSubcontratada
	 * @param codigoArticuloServicio
	 * @param valorServicioArticulo
	 * 
	 * @author Diana Ruiz
	 * @since 23/01/2012
	 */
	
	
			
	public void generarTarifasEntidadSub (DTOProcesoAutorizacion dtoProcesoAutorizacion, AutorizacionesEntidadesSub autorizacion, 
			EntidadesSubcontratadas entidadSubcontratada, int codigoArticuloServicio, BigDecimal valorServicioArticulo, boolean esServicio, 
			Connection con) throws Exception {
		
						 
		ITarifasEntidadSubDAO tarifasEntidadSubDAO = FacturacionFabricaDAO.crearTarifasEntidadSubDAO();
		Date fechaActual = UtilidadFecha.getFechaActualTipoBD();				
		String horaActual = UtilidadFecha.getHoraActual();
		EstadosSolFact estadoSolFactura = new EstadosSolFact();
		estadoSolFactura.setCodigo(3);
		Solicitudes solicitud = new Solicitudes();
		Usuarios usuarios = new Usuarios();
		usuarios.setLogin(dtoProcesoAutorizacion.getUsuarioSesion().getLoginUsuario());	
		solicitud.setNumeroSolicitud(dtoProcesoAutorizacion.getDtoSolicitud().getNumeroSolicitud());
		DTOCalculoTarifaServicioArticulo dtoCalculo = new DTOCalculoTarifaServicioArticulo();				
		DtoEntidadSubcontratada dtoEntidadSub =new DtoEntidadSubcontratada();		
		dtoEntidadSub.setCodigo(entidadSubcontratada.getCodigo());		
		dtoCalculo.setEntidadSubcontratada(dtoEntidadSub);		
		dtoCalculo.setFechaVigencia(UtilidadFecha.getFechaActual());
			
		TarifasEntidadSub tarifasEntidadSub = new TarifasEntidadSub();
		tarifasEntidadSub.setEntidadesSubcontratadas(entidadSubcontratada);
		tarifasEntidadSub.setContratosEntidadesSub(contratoEntidadSubc);
		tarifasEntidadSub.setFecha(fechaActual);
		tarifasEntidadSub.setHora(horaActual);					
		tarifasEntidadSub.setSolicitudes(solicitud);	
		tarifasEntidadSub.setValorUnitario(valorServicioArticulo);
		tarifasEntidadSub.setVieneDespacho("N");
		tarifasEntidadSub.setEstadosSolFact(estadoSolFactura);						
		dtoCalculo.setEsServicio(esServicio);
		dtoCalculo.setCodigoArticuloServicio(codigoArticuloServicio);								
		EsquemasTarifarios esquemaTarifarioServArti = new EsquemasTarifarios();
		esquemaTarifarioServArti = obtenerEsquemaTarifario(con, dtoCalculo, esServicio);				
		tarifasEntidadSub.setEsquemasTarifarios(esquemaTarifarioServArti);
		tarifasEntidadSub.setAutorizacionesEntidadesSub(autorizacion);
		tarifasEntidadSub.setFechaModifica(fechaActual);
		tarifasEntidadSub.setHoraModifica(horaActual);
		tarifasEntidadSub.setUsuarios(usuarios);					
		tarifasEntidadSubDAO.sincronizarTarifasEntidadSub(tarifasEntidadSub);
	}
	
	/**
	 * Metodo que permite obtener el tarifario oficial para el Servicio/Articulo 
	 * @return EsquemasTarifarios
	 * 
	 * @author Diana Ruiz
	 */
	
	
	public EsquemasTarifarios obtenerEsquemaTarifario (Connection con,
			DTOCalculoTarifaServicioArticulo dtoCalculoTarifario, boolean esServicio){
		
		EsquemaTarifario obtenerEsquemaTarifarioSerArt = new EsquemaTarifario(); 		
		EsquemasTarifarios esquemaTarifarioServArti = new EsquemasTarifarios();
		
		obtenerEsquemaTarifarioSerArt.obtenerEsquemaTarifarioServicioArticuloEntidadSub(con, 
		String.valueOf(contratoEntidadSubc.getConsecutivo()), dtoCalculoTarifario.getCodigoArticuloServicio(), 
		dtoCalculoTarifario.getFechaVigencia(), esServicio);	
		esquemaTarifarioServArti.setCodigo(obtenerEsquemaTarifarioSerArt.getCodigo());
		return esquemaTarifarioServArti;		
	}
	
	

	/**
	 * 		
	 * Este Método se encarga de registrar un histórico de la autorización de 
	 * capitación subcontratada
	 * 
	 * @param AutorizacionesCapitacionSub autorizacionCapitacion,
			  DTOProcesoAutorizacion dtoProcesoAutorizacion
	 * @author, Angela Maria Aguirre
	 *
	 */
	private void guardarHistorialAutorCapitacionSub(AutorizacionesCapitacionSub autorizacionCapitacion,
			DTOProcesoAutorizacion dtoProcesoAutorizacion,AutorizacionesEntidadesSub autorizacion)throws Exception{
		
		IHistoAutorizacionCapitaSubDAO dao = ManejoPacienteDAOFabrica.crearHistoAutorizacionCapitaSub();
						
		Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
		String horaActual = UtilidadFecha.getHoraActual();
		HistoAutorizacionCapitaSub historial = new HistoAutorizacionCapitaSub();
		historial.setAutorizacionesEntidadesSub(autorizacion);
		historial.setAutorizacionesCapitacionSub(autorizacionCapitacion);
		historial.setConsecutivo(autorizacionCapitacion.getConsecutivo());
		historial.setTipoAutorizacion(autorizacionCapitacion.getTipoAutorizacion());
		
		//Se asigna el convenio recobro
		if(dtoProcesoAutorizacion.getConvenioRecobro()!=null)
		{
			Convenios convenioRecobro = new Convenios();
			convenioRecobro.setCodigo(autorizacionCapitacion.getConvenios().getCodigo());
			historial.setConvenios(convenioRecobro);
		}
		if(!UtilidadTexto.isEmpty(dtoProcesoAutorizacion.getDescripcionConvenioRecobro())){
			historial.setOtroConvenioRecobro(autorizacionCapitacion.getOtroConvenioRecobro());
		}		
		
		historial.setIndicativoTemporal(autorizacionCapitacion.getIndicativoTemporal());
		if(autorizacionCapitacion.getIndicativoTemporal()==ConstantesBD.acronimoSiChar){
			
			historial.setDescripcionEntidad(autorizacionCapitacion.getDescripcionEntidad());			
			historial.setDireccionEntidad(autorizacionCapitacion.getDireccionEntidad());
			historial.setTelefonoEntidad(autorizacionCapitacion.getTelefonoEntidad());			
		}
		if(dtoProcesoAutorizacion.getIndicadorPrioridad()!=null && dtoProcesoAutorizacion.getIndicadorPrioridad()>0)
		{
			historial.setIndicadorPrioridad(dtoProcesoAutorizacion.getIndicadorPrioridad());
		}
		
		historial.setFechaModifica(fechaActual);
		historial.setHoraModifica(horaActual);		
		
		Usuarios usuarioModifica = new Usuarios();
		usuarioModifica.setLogin(dtoProcesoAutorizacion.getUsuarioSesion().getLoginUsuario());
		
		historial.setUsuarios(usuarioModifica);
		historial.setAccionRealizada(ConstantesIntegridadDominio.acronimoAccionHistoricaInsertar);
		historial.setFechaVencimiento(autorizacion.getFechaVencimiento());
		
		dao.guardarAutorizacionEntidadSubcontratada(historial);
	}
	
	
	/**
	 * Validación Monto de Cobro para Autorizaciones Automáticas.
	 * DCU 1106 - Versión cambio 1.1. Cristhian Murillo
	 * 
	 * @param dtoProcesoAutorizacion
	 * @param listaArticulosValidados
	 * @param listaServiciosValidados
	 *
	 * @autor Cristhian Murillo
	*/
	private void validacionMontoCobroAutorizacionesAutomaticas(DTOProcesoAutorizacion dtoProcesoAutorizacion, 
			ArrayList<DtoArticulosAutorizaciones> listaArticulosValidados, ArrayList<DtoServiciosAutorizaciones> listaServiciosValidados) 
	{
		IConveniosMundo convenioMundo 	= ConveniosFabricaMundo.crearConveniosMundo();
		IContratoMundo contratoMundo	= FacturacionFabricaMundo.crearContratoMundo();
		IServiciosMundo servicioMundo	= FacturacionFabricaMundo.crearServiciosMundo();
		IPacientesMundo pacienteMundo	= AdministracionFabricaMundo.crearPacienteMundo();
		IDetalleMontoGeneralMundo detalleMontoGeneralMundo	= FacturacionFabricaMundo.crearDetalleMontoGeneralMundo();
		
		Integer codigoConvenio = null;
		
		//------
		Double valorMonto 		= dtoProcesoAutorizacion.getValorMonto();
		Double porcentajeMonto 	= dtoProcesoAutorizacion.getPorcentajeMonto();
		String tipoDetalleMonto	= dtoProcesoAutorizacion.getTipoDetalleMonto();
		
		/* viaIngreso: Si es nulo para buscar monto no se puede continuar */
		Integer viaIngreso 		= dtoProcesoAutorizacion.getViaIngreso();
		String viaIngresoStr 	= null;
		//------
		
		for (DtoServiciosAutorizaciones dtoServiciosAutorizaciones : listaServiciosValidados) 
		{
			DtoServicios dtoServicios = new DtoServicios();
			dtoServicios = servicioMundo.obtenerTipoEspecialidadGrupoServicioPorID(dtoServiciosAutorizaciones.getCodigoServicio());
			dtoServiciosAutorizaciones.setCodigoTipoMonto(dtoServicios.getCodigoTipoMonto());
		}
		
		if(!dtoProcesoAutorizacion.isOrdenAmbulatoria())
		{
			DtoUsuariosCapitados parametrosBusqueda = new DtoUsuariosCapitados();
			
			parametrosBusqueda.setIdCuenta(dtoProcesoAutorizacion.getPaciente().getCodigoCuenta());
			parametrosBusqueda.setCodigoContrato(dtoProcesoAutorizacion.getContratoAutorizacion());
			
			ArrayList<DtoUsuariosCapitados> listaPaciente = new ArrayList<DtoUsuariosCapitados>();
			listaPaciente = pacienteMundo.buscarPacienteConvenio(parametrosBusqueda);
			
			if(!Utilidades.isEmpty(listaPaciente)){
				DtoUsuariosCapitados paciente = new DtoUsuariosCapitados();
				paciente = listaPaciente.get(0);
				
				if(!UtilidadTexto.isEmpty(paciente.getTipoCobroPaciente()))
				{
					if(paciente.getTipoCobroPaciente().equals(ConstantesIntegridadDominio.acronimoTipoPacienteNoManejaMontos))
					{
						/* Si el Convenio no maneja Montos, se muestra la información del campo Monto Cobro (asigna 0% o 100%) */
						porcentajeMonto = paciente.getPorcentajeMontoCobro().doubleValue();
					}
					else if(paciente.getTipoCobroPaciente().equals(ConstantesIntegridadDominio.acronimoTipoPacienteManejaMontos))
					{
						/* Si el Convenio Maneja Montos, se verifica lo definido en el campo Indicativo Monto por Convenio. */
						if(paciente.getTipoMontoCobro().equals(ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN))
						{
							DTOMontosCobroDetalleGeneral detalleGeneral = new DTOMontosCobroDetalleGeneral();
							detalleGeneral = detalleMontoGeneralMundo.obtenerValorTipoMonto(paciente.getDetalleCodigo());
							
							if(detalleGeneral.getValor() != null){
								valorMonto 		= detalleGeneral.getValor()*detalleGeneral.getCantidadMonto();
							}
							else if(detalleGeneral.getPorcentaje() != null){
								porcentajeMonto = detalleGeneral.getPorcentaje().doubleValue();
							}
							tipoDetalleMonto= paciente.getTipoMontoCobro();
						}
					}
				}
			}
		}
		else{
			//if(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia()!=null)
			{
				if(dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia()!=null)
					codigoConvenio = dtoProcesoAutorizacion.getDtoAutorizacionIngresoEstancia().getConvenioResponsable().getCodigo();
				else
					codigoConvenio = dtoProcesoAutorizacion.getDtoSolicitud().getConvenioResponsable().getCodigo();
				
				if(codigoConvenio != ConstantesBD.codigoNuncaValido)
				{
					DtoConvenio dtoConvenio = new DtoConvenio();
					dtoConvenio = convenioMundo.buscarConvenio(codigoConvenio);
					boolean convenioManejaMontos = false;
					
					if(dtoConvenio.getConvenioManejaMontos().equals(ConstantesBD.acronimoSi)){
						convenioManejaMontos = true;
					}
					else if(dtoConvenio.getConvenioManejaMontos().equals(ConstantesBD.acronimoNo)){
						convenioManejaMontos = false;
					}
					
					if (convenioManejaMontos) 
					{
						/* clasificacionSE: Si es nulo para buscar monto no se puede continuar */
						Integer clasificacionSE = null;		String clasificacionSEStr	= null;
						String tipoPaciente 	= null;//Ya no se utiliza  ValoresPorDefecto.getTipoPacienteViaIngresoValidarNivelAutorizacionCapitacion(dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt());
						String tipoAfiliado 	= dtoProcesoAutorizacion.getPaciente().getTipoAfiliado();
						/* Naturaleza Paciente (peude ser nulo para buscar monto) */
						String excepcionMontoStr 	= dtoProcesoAutorizacion.getPaciente().getExcepcionMonto();
						Integer excepcionMonto 		= ConstantesBD.codigoNuncaValido;
					
						if(!UtilidadTexto.isEmpty(excepcionMontoStr)){
							excepcionMonto = Integer.parseInt(excepcionMontoStr);
						}
												
						viaIngresoStr 		= validarParametroViaIngresoNivelAutorizacion(dtoProcesoAutorizacion.getDtoSolicitud().getTipoOrden(), dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt());
						clasificacionSEStr 	= dtoProcesoAutorizacion.getPaciente().getClasificacionSE();
						
						DtoParametrosBusquedaMonto dtoParametrosBusquedaMonto = new DtoParametrosBusquedaMonto();
							dtoParametrosBusquedaMonto.setViaIngresoStr(viaIngresoStr);
							dtoParametrosBusquedaMonto.setClasificacionSEStr(clasificacionSEStr);
							dtoParametrosBusquedaMonto.validarCampos();
						
						if(dtoParametrosBusquedaMonto.getPasoValidaciones())
						{
							viaIngreso 		= Integer.parseInt(viaIngresoStr);
							clasificacionSE = Integer.parseInt(clasificacionSEStr);
							
							ArrayList<DTOResultadoBusquedaDetalleMontos> listaMontos = dtoParametrosBusquedaMonto.buscarMonto(clasificacionSE, codigoConvenio, excepcionMonto, tipoAfiliado, tipoPaciente, viaIngreso);
							DTOResultadoBusquedaDetalleMontos montoCobro = new DTOResultadoBusquedaDetalleMontos();
							
							if(!Utilidades.isEmpty(listaMontos))
							{
								if(listaMontos.size() == 1)
								{
									montoCobro = new DTOResultadoBusquedaDetalleMontos();
									montoCobro = listaMontos.get(0);
									if(Utilidades.isEmpty(listaServiciosValidados))
									{
										/* Autorización de Artículos: Se asigna el monto obtenido */
										if(montoCobro.getTipoDetalleAcronimo().equals(ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN))
										{
											if(montoCobro.getValor() != null){
												valorMonto 	= montoCobro.getValor()*montoCobro.getCantidadMonto();
											}
											else if(montoCobro.getPorcentaje() != null){
												porcentajeMonto = montoCobro.getPorcentaje(); 
											}
											tipoDetalleMonto= montoCobro.getTipoDetalleAcronimo();
										}
										
									}
									else if(Utilidades.isEmpty(listaArticulosValidados))
									{
										/* Autorización de Servicios: Se asigna el monto obtenido de acuerdo al tipo monto del articulo */
										if(montoCobro.getTipoDetalleAcronimo().equals(ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN))
										{
											if(montoCobro.getValor() != null){
												valorMonto 	= montoCobro.getValor()*montoCobro.getCantidadMonto();
											}
											else if(montoCobro.getPorcentaje() != null){
												porcentajeMonto = montoCobro.getPorcentaje(); 
											}
											tipoDetalleMonto= montoCobro.getTipoDetalleAcronimo();
										}
									}
								}
								else{
									int k = 0;
									montoCobro = new DTOResultadoBusquedaDetalleMontos();
									for (DTOResultadoBusquedaDetalleMontos dtoResultadoBusquedaDetalleMontos : listaMontos) 
									{
										if(listaServiciosValidados.get(0).getCodigoTipoMonto().equals(dtoResultadoBusquedaDetalleMontos.getTipoAfiliadoAcronimo()))
										{
											montoCobro = dtoResultadoBusquedaDetalleMontos;
											k++; 										
										}
									}
									
									/* Solo debe existir un solo monto que cumpla con la condición del tipo afiliado */
									if(k == 1)
									{
										if(montoCobro.getTipoDetalleAcronimo().equals(ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN))
										{
											if(montoCobro.getValor() != null){
												valorMonto 	= montoCobro.getValor()*montoCobro.getCantidadMonto();
											}
											else if(montoCobro.getPorcentaje() != null){
												porcentajeMonto = montoCobro.getPorcentaje(); 
											}
											tipoDetalleMonto= montoCobro.getTipoDetalleAcronimo();
										}
									}
								}
							}
						}
					}
					else{
						if(dtoProcesoAutorizacion.getContratoAutorizacion() != null)
						{
							Contratos contrato = new Contratos();
							contrato = contratoMundo.findById(dtoProcesoAutorizacion.getContratoAutorizacion());
							
							if(contrato.getPacientePagaAtencion().equals(ConstantesBD.acronimoSiChar)){
								/* Si esta definido como SI, se asigna al campo Monto Cobro el valor 100% */
								porcentajeMonto = 100d;
							}
							else if(contrato.getPacientePagaAtencion().equals(ConstantesBD.acronimoNoChar)){
								/* Si esta definido como NO, se asigna al campo Monto  Cobro el valor 0% */
								porcentajeMonto = 0d;
							}
						}
					}
				}
			}
		}
		
		
		dtoProcesoAutorizacion.setPorcentajeMonto(porcentajeMonto);
		dtoProcesoAutorizacion.setValorMonto(valorMonto);
		dtoProcesoAutorizacion.setTipoDetalleMonto(tipoDetalleMonto);
		
		if(dtoProcesoAutorizacion.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoManual)){
			viaIngreso = ConstantesBD.codigoViaIngresoAmbulatorios;
		}
		dtoProcesoAutorizacion.setViaIngreso(viaIngreso);
	}
	
	
	/**
	 * Se encarga de asociar la Autorización dependiendo el caso con:
	 * 	- Orden ambulatoria
	 * 	- Peticion Qx
	 *  - Solicitudes
	 *  - vía Ingreso
	 *  - Montos
	 *  
	 * @param dtoProcesoAutorizacion
	 * @param autorizacionesEntidadesSub
	 * @param numeroSolicitud
	 * @throws Exception 
	 *
	 * @autor Cristhian Murillo
	 */
	private void asociarDatosAutorizacion(DTOProcesoAutorizacion dtoProcesoAutorizacion, AutorizacionesEntidadesSub autorizacionesEntidadesSub, int numeroSolicitud) throws Exception 
	{
		asociarOrdenesPorTipo(autorizacionesEntidadesSub, numeroSolicitud, dtoProcesoAutorizacion);
		
		AutorizacionesEntSubMontos autorizacionesEntSubMontos;	autorizacionesEntSubMontos = new AutorizacionesEntSubMontos();
		
		if(dtoProcesoAutorizacion.getViaIngreso() != null)
		{
			ViasIngreso viasIngreso;	
			viasIngreso = new ViasIngreso();
			viasIngreso.setCodigo(dtoProcesoAutorizacion.getViaIngreso().intValue());
			autorizacionesEntSubMontos.setViasIngreso(viasIngreso);
		}
		
		autorizacionesEntSubMontos.setValormonto(dtoProcesoAutorizacion.getValorMonto());
		autorizacionesEntSubMontos.setPorcentajemonto(dtoProcesoAutorizacion.getPorcentajeMonto());
		autorizacionesEntSubMontos.setTipodetallemonto(dtoProcesoAutorizacion.getTipoDetalleMonto());
		//autorizacionesEntSubMontos.setTipomonto(dtoProcesoAutorizacion.getTipoMonto());
		
		
		HashSet<AutorizacionesEntSubMontos> setAutorizacionesEntSubMontos = new HashSet<AutorizacionesEntSubMontos>();
		setAutorizacionesEntSubMontos.add(autorizacionesEntSubMontos);
		autorizacionesEntSubMontos.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
		autorizacionesEntidadesSub.setAutorizacionesEntSubMontoses(setAutorizacionesEntSubMontos); 
		
	}


	/**
	 * Método que se encarga de guardar el centro de costo que ejecuta la autorización
	 * @param autorizacionesEntSubMontos
	 * @param centroCostoResponde
	 * @throws Exception
	 * @author Diana Ruiz
	 */
	
	private void guardarAutoCapiXCentroCosto(AutorizacionesEntSubMontos autorizacionesEntSubMontos, CentrosCosto centroCostoResponde)throws Exception{
		/*
		IAutoCapiXCentroCostoDAO dao = ManejoPacienteDAOFabrica.crearAutoCapiXCentroCosto();
		AutoCapiXCentroCostoId autoCapiXCentroCostoId = new AutoCapiXCentroCostoId();
		AutoCapiXCentroCosto autoCapiXCentroCosto = new AutoCapiXCentroCosto();
		
		autoCapiXCentroCostoId.setAutoEntsubMonto(autorizacionesEntSubMontos.getCodigoPk());
		autoCapiXCentroCostoId.setCentroCosto(centroCostoResponde.getCodigo());
		autoCapiXCentroCosto.setId(autoCapiXCentroCostoId);
		autoCapiXCentroCosto.setAutorizacionesEntSubMontos(autorizacionesEntSubMontos);
		autoCapiXCentroCosto.setCentrosCosto(centroCostoResponde);
		
		dao.sincronizarAutoCapiXCentroCosto(autoCapiXCentroCosto);*/
			
	}
	
	
	/**
	 * Crea una lista de autorizaciones asociadas a la orden (ambulatoria, solicitud o petición).
	 * @param autorizacion
	 * @param numeroSolicitud
	 * @param dtoProcesoAutorizacion
	 *
	 * @autor Cristhian Murillo
	 */
	private void asociarOrdenesPorTipo(AutorizacionesEntidadesSub autorizacion,int numeroSolicitud, DTOProcesoAutorizacion dtoProcesoAutorizacion) 
	{
		if(numeroSolicitud!=ConstantesBD.codigoNuncaValido)
		{
			if(!UtilidadTexto.isEmpty(dtoProcesoAutorizacion.getDtoSolicitud().getTipoOrden()) 
					&& dtoProcesoAutorizacion.getDtoSolicitud().getTipoOrden().equals(ConstantesIntegridadDominio.acronimoTipoOrdenambulatoria))
			{
				OrdenesAmbulatorias ordenesAmbulatorias; ordenesAmbulatorias = new OrdenesAmbulatorias();
				ordenesAmbulatorias.setCodigo(new Long(numeroSolicitud));
				
				AutoEntsubOrdenambula autoEntsubOrdenambula;	autoEntsubOrdenambula = new AutoEntsubOrdenambula();
				autoEntsubOrdenambula.setAutorizacionesEntidadesSub(autorizacion);
				autoEntsubOrdenambula.setOrdenesAmbulatorias(ordenesAmbulatorias);
				
				//this.listaAutoEntsubOrdenambula.add(autoEntsubOrdenambula);
				HashSet<AutoEntsubOrdenambula> setAutoEntsubOrdenambula = new HashSet<AutoEntsubOrdenambula>();
				setAutoEntsubOrdenambula.add(autoEntsubOrdenambula);
				autorizacion.setAutoEntsubOrdenambulas(setAutoEntsubOrdenambula);
			}
			else if(!UtilidadTexto.isEmpty(dtoProcesoAutorizacion.getDtoSolicitud().getTipoOrden()) 
					&&  dtoProcesoAutorizacion.getDtoSolicitud().getTipoOrden().equals(ConstantesIntegridadDominio.acronimoTipoOrdenPeticionCx))
			{
				PeticionQx peticionQx; peticionQx = new PeticionQx();
				peticionQx.setCodigo(numeroSolicitud);
				
				AutoEntsubPeticiones autoEntsubPeticiones;	autoEntsubPeticiones = new AutoEntsubPeticiones();
				autoEntsubPeticiones.setAutorizacionesEntidadesSub(autorizacion);
				autoEntsubPeticiones.setPeticionQx(peticionQx);
				
				//this.listaAutoEntsubPeticiones.add(autoEntsubPeticiones);
				HashSet<AutoEntsubPeticiones> setAutoEntsubPeticiones = new HashSet<AutoEntsubPeticiones>();
				setAutoEntsubPeticiones.add(autoEntsubPeticiones);
				autorizacion.setAutoEntsubPeticioneses(setAutoEntsubPeticiones);
			}
			else
			{
				/* De lo contrario se asume que es una solicitud y corresponde al tipo ConstantesIntegridadDominio.acronimoTipoOrdenMedica */
				Solicitudes solicitudes;	solicitudes = new Solicitudes();
				solicitudes.setNumeroSolicitud(numeroSolicitud);
				
				AutoEntsubSolicitudes autoEntsubSolicitudes; autoEntsubSolicitudes = new AutoEntsubSolicitudes();
				autoEntsubSolicitudes.setAutorizacionesEntidadesSub(autorizacion);
				autoEntsubSolicitudes.setSolicitudes(solicitudes);
				
				//this.listaAutoEntsubSolicitudes.add(autoEntsubSolicitudes); 
				HashSet<AutoEntsubSolicitudes> setAutoEntsubSolicitudes = new HashSet<AutoEntsubSolicitudes>();
				setAutoEntsubSolicitudes.add(autoEntsubSolicitudes);
				autorizacion.setAutoEntsubSolicitudeses(setAutoEntsubSolicitudes);
			}
		}
	}
	
	
	
	
	/**
	 * Retorna el consecutivo para cada Autorización.
	 * Se valida si se debe asignar un consecutivo ya que para el caso de Ambulatorias y Peticiones no aplica.
	 * @param dtoProcesoAutorizacion
	 * @return consecutivo
	 *
	 * @autor Cristhian Murillo
	 */
	private String obtenerconsecutivoAutorizacion(DTOProcesoAutorizacion dtoProcesoAutorizacion)
	{
		String consecutivo = "";		
		
		boolean asignarConsecutivoAutorizacion = true;
		
		if(dtoProcesoAutorizacion.getDtoSolicitud()!=null)
		{
			if(!UtilidadTexto.isEmpty(dtoProcesoAutorizacion.getDtoSolicitud().getTipoOrden()) 
					&& dtoProcesoAutorizacion.getDtoSolicitud().getTipoOrden().equals(ConstantesIntegridadDominio.acronimoTipoOrdenambulatoria))
			{
				asignarConsecutivoAutorizacion = false;
			}
			else if(!UtilidadTexto.isEmpty(dtoProcesoAutorizacion.getDtoSolicitud().getTipoOrden()) 
					&&  dtoProcesoAutorizacion.getDtoSolicitud().getTipoOrden().equals(ConstantesIntegridadDominio.acronimoTipoOrdenPeticionCx))
			{
				asignarConsecutivoAutorizacion = false;
			}
		}
		
		if(UtilidadTexto.isEmpty(dtoProcesoAutorizacion.getTipoEntidadAutoriza()))
		{
			// Esto solo ocurre cuando es automática
			dtoProcesoAutorizacion.setTipoEntidadAutoriza(ConstantesIntegridadDominio.acronimoExterna);
			
			if(dtoProcesoAutorizacion.getCentroCostoResponde() != ConstantesBD.codigoNuncaValido)
			{
				ICentroCostosServicio centroCostosServicio = AdministracionFabricaServicio.crearCentroCostosServicio();
				
				CentrosCosto centroCosto; centroCosto = new CentrosCosto();
				centroCosto = centroCostosServicio.findById(dtoProcesoAutorizacion.getCentroCostoResponde()); 
				
				if(!UtilidadTexto.isEmpty(centroCosto.getTipoEntidadEjecuta()))
				{
					if(centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoAmbos))
					{
						String descripcionEntidad = ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt());
						//int prioridad=Utilidades.convertirAEntero(ValoresPorDefecto.getPrioridadEntidadSubcontratada(dto.getInstitucion()));
						  
						if(!UtilidadTexto.isEmpty(descripcionEntidad))
						{
							String[] entidad = descripcionEntidad.split("-");
							Long codigoEntidad = Long.parseLong(entidad[0].trim());
							
							if(dtoProcesoAutorizacion.getDtoEntidadSubcontratada().getCodigoPk() == codigoEntidad){
								dtoProcesoAutorizacion.setTipoEntidadAutoriza(ConstantesIntegridadDominio.acronimoInterna);
							}
						}
					}
					else
					{
						dtoProcesoAutorizacion.setTipoEntidadAutoriza(centroCosto.getTipoEntidadEjecuta());
					}
				}
				else{
					dtoProcesoAutorizacion.setTipoEntidadAutoriza(ConstantesIntegridadDominio.acronimoInterna);
				}
			}
		}
		
		if(dtoProcesoAutorizacion.getTipoEntidadAutoriza().equals(ConstantesIntegridadDominio.acronimoInterna))
		{
			asignarConsecutivoAutorizacion = false;
		}
		
		
		if(asignarConsecutivoAutorizacion)
		{
			if(dtoProcesoAutorizacion.getDtoEntidadSubcontratada().getCodigoPk() != ConstantesBD.codigoNuncaValidoLong)
			{
				consecutivo = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoAutorizacionEntiSub, dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt()); 
				
				consecutivoAutoEntSub.add(consecutivo);
				
				Connection conH = UtilidadPersistencia.getPersistencia().obtenerConexion();
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH, 
						ConstantesBD.nombreConsecutivoAutorizacionEntiSub, 
						Integer.valueOf(dtoProcesoAutorizacion.getUsuarioSesion().getCodigoInstitucionInt()), 
						consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			}
		}
		
		return consecutivo;
	}

	/**
	 * Método que valida el parametro general de via de ingreso de nivel de autorizacion para Ordenes Ambulatorias y Peticion 
	 * @param tipoOrden
	 * @param institucion
	 * @return viaIngresoParametro
	 */
	private String validarParametroViaIngresoNivelAutorizacion(String tipoOrden,int institucion)
	{
		String viaIngresoParametro=null;
		if(tipoOrden.equals(ConstantesIntegridadDominio.acronimoTipoOrdenambulatoria) 
				|| tipoOrden.equals(ConstantesIntegridadDominio.acronimoTipoOrdenMedica)){
			viaIngresoParametro=ValoresPorDefecto.getViaIngresoValidacionesOrdenesAmbulatorias(institucion);
		}else if(tipoOrden.equals(ConstantesIntegridadDominio.acronimoTipoOrdenPeticionCx)){
			viaIngresoParametro=ValoresPorDefecto.getViaIngresoValidacionesPeticiones(institucion);
		}
		return viaIngresoParametro;
	}
}
