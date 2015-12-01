package com.servinte.axioma.bl.manejoPaciente.impl;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.servinte.axioma.bl.capitacion.impl.CierrePresupuestoMundo;
import com.servinte.axioma.bl.capitacion.impl.NivelAutorizacionMundo;
import com.servinte.axioma.bl.capitacion.interfaz.ICierrePresupuestoMundo;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo;
import com.servinte.axioma.bl.ordenes.impl.OrdenesAmbulatoriasMundo;
import com.servinte.axioma.bl.ordenes.impl.SolicitudesMundo;
import com.servinte.axioma.bl.ordenes.interfaz.IOrdenesAmbulatoriasMundo;
import com.servinte.axioma.bl.ordenes.interfaz.ISolicitudesMundo;
import com.servinte.axioma.bl.salasCirugia.impl.PeticionesMundo;
import com.servinte.axioma.bl.salasCirugia.interfaz.IPeticionesMundo;
import com.servinte.axioma.bl.util.impl.UtilidadesMundo;
import com.servinte.axioma.common.ErrorMessage;
import com.servinte.axioma.delegate.manejoPaciente.AutorizacionesDelegate;
import com.servinte.axioma.delegate.ordenes.OrdenesAmbulatoriasDelegate;
import com.servinte.axioma.delegate.ordenes.PeticionesDelegate;
import com.servinte.axioma.delegate.ordenes.SolicitudesDelegate;
import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;
import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.dto.manejoPaciente.ArticuloAutorizadoCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionEntregaDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionPorOrdenDto;
import com.servinte.axioma.dto.manejoPaciente.ParametroBusquedaOrdenAutorizacionDto;
import com.servinte.axioma.dto.manejoPaciente.ServicioAutorizadoCapitacionDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.AutorizacionesCapitacionSub;
import com.servinte.axioma.orm.AutorizacionesEntSubArticu;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.NivelAutorizacion;
import com.servinte.axioma.orm.Usuarios;


/**
 * Clase que implementa los servicios de Negocio correspondientes a la lógica asociada a las
 * Autorizaciones de Capitación
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:23:59 p.m.
 */
public class AutorizacionCapitacionMundo implements IAutorizacionCapitacionMundo {

	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#obtenerEntidadesSubContratadasExternas(int, java.util.List)
	 * @author ricruico
	 */
	@Override
	public List<EntidadSubContratadaDto> obtenerEntidadesSubContratadasExternas(
			int codigoCentroCosto, List<NivelAutorizacionDto> nivelesAutorizacion, boolean requiereTransaccion)  throws IPSException{
		List<EntidadSubContratadaDto> listaEntidades=null;
		try{
			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			List<Integer> prioridades= new ArrayList<Integer>();
			for(NivelAutorizacionDto nivelDto:nivelesAutorizacion){
				prioridades.addAll(nivelDto.getPrioridades());
			}
			listaEntidades=autorizacionesDelegate.obtenerEntidadesSubContratadasExternas(codigoCentroCosto,
								prioridades);
			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}
		}
		catch (IPSException ipsme) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}	
			throw ipsme;
		}
		catch (Exception e) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return listaEntidades;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#obtenerOrdenesPorAutorizarPorPaciente(int)
	 * @author ricruico
	 */
	@Override
	public List<OrdenAutorizacionDto> obtenerOrdenesPorAutorizarPorPaciente(int codigoPaciente)  throws IPSException{
		List<OrdenAutorizacionDto> ordenesPorAutorizar=null;
		AutorizacionesDelegate delegate=null;
		try{
			HibernateUtil.beginTransaction();
			delegate = new AutorizacionesDelegate();
			ordenesPorAutorizar=delegate.obtenerOrdenesPorAutorizarPorPaciente(codigoPaciente);
			if(ordenesPorAutorizar != null && !ordenesPorAutorizar.isEmpty()){
				SolicitudesDelegate solicitudesDelegate= new SolicitudesDelegate();
				OrdenesAmbulatoriasDelegate ordenesAmbulatoriasDelegate= new OrdenesAmbulatoriasDelegate();
				PeticionesDelegate peticionesDelegate= new PeticionesDelegate();
				for(OrdenAutorizacionDto ordenDto:ordenesPorAutorizar){
					ContratoDto infoContrato= new ContratoDto();
					if(ordenDto.getClaseOrden()==ConstantesBD.claseOrdenOrdenMedica
							|| ordenDto.getClaseOrden()==ConstantesBD.claseOrdenCargoDirecto){
						//Se obtene la via de ingreso asociada a la última cuenta del ingreso de la orden Médica
						ordenDto.setCodigoViaIngreso(delegate.obtenerViaIngresoPorIngreso(ordenDto.getCodigoIngreso()));
						//Se obtiene la información del Convenio/Contrato capitado asociado a la orden médica
						infoContrato=solicitudesDelegate.obtenerConvenioContratoPorOrdenMedica(ordenDto.getCodigoOrden().intValue(), ordenDto.getClaseOrden(), ordenDto.getTipoOrden());
						if(ordenDto.getTipoOrden()==ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos
								|| ordenDto.getTipoOrden()==ConstantesBD.codigoTipoSolicitudMedicamentos){
							//Se obtienen los Medicamentos/Insumos de la orden Médica
							ordenDto.setMedicamentosInsumosPorAutorizar(solicitudesDelegate
										.obtenerMedicamentosInsumosPorAutorizar(ordenDto.getCodigoOrden().intValue()));
						}
						else{
							//Se obtienen los Servicios de la orden Médica
							ordenDto.setServiciosPorAutorizar(solicitudesDelegate.
										obtenerServiciosPorAutorizar(ordenDto.getCodigoOrden().intValue(), 
													ordenDto.getClaseOrden(), ordenDto.getTipoOrden()));
						}
					}
					else if(ordenDto.getClaseOrden()==ConstantesBD.claseOrdenOrdenAmbulatoria){
						//Se obtiene la información del Convenio/Contrato capitado asociado a la orden ambulatoria
						infoContrato=ordenesAmbulatoriasDelegate.obtenerConvenioContratoPorOrdenAmbulatoria(ordenDto.getCodigoOrden(), ordenDto.getTipoOrden());
						if(ordenDto.getTipoOrden()==ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos){
							//Se obtienen los Medicamentos/Insumos de la orden ambulatoria
							ordenDto.setMedicamentosInsumosPorAutorizar(ordenesAmbulatoriasDelegate.
										obtenerMedicamentosInsumosPorAutorizar(ordenDto.getCodigoOrden()));
						}
						else{
							//Se obtienen los Servicios de la orden ambulatoria
							ordenDto.setServiciosPorAutorizar(ordenesAmbulatoriasDelegate.
										obtenerServiciosPorAutorizar(ordenDto.getCodigoOrden()));
						}
					}
					else if(ordenDto.getClaseOrden()==ConstantesBD.claseOrdenPeticion){
						//Se obtiene la información del Convenio/Contrato capitado asociado a la Petición Qx
						infoContrato=peticionesDelegate.obtenerConvenioContratoPorPeticion(ordenDto.getCodigoOrden().intValue());
						//Se obtienen los Servicios de la petición
						ordenDto.setServiciosPorAutorizar(peticionesDelegate.
									obtenerServiciosPorAutorizar(ordenDto.getCodigoOrden().intValue()));
					}
					ordenDto.setContrato(infoContrato);
				}
			}
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return ordenesPorAutorizar;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#obtenerOrdenesPorAutorizarPorRango(com.servinte.axioma.dto.manejoPaciente.ParametroBusquedaOrdenAutorizacionDto)
	 * @author ricruico
	 */
	@Override
	public List<OrdenAutorizacionDto> obtenerOrdenesPorAutorizarPorRango(
			ParametroBusquedaOrdenAutorizacionDto parametrosBusqueda)  throws IPSException{
		List<OrdenAutorizacionDto> ordenesPorAutorizar=new ArrayList<OrdenAutorizacionDto>();
		List<OrdenAutorizacionDto> ordenesPorRango=null;
		try{
			HibernateUtil.beginTransaction();
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			SolicitudesDelegate solicitudesDelegate= new SolicitudesDelegate();
			OrdenesAmbulatoriasDelegate ordenesAmbulatoriasDelegate= new OrdenesAmbulatoriasDelegate();
			PeticionesDelegate peticionesDelegate= new PeticionesDelegate();
			//Se valida la clase de orden que llega por parámetro
			if(parametrosBusqueda.getCodigoClaseOrden() == ConstantesBD.claseOrdenOrdenMedica){
				ordenesPorRango=solicitudesDelegate.obtenerOrdenesMedicasPorAutorizar(parametrosBusqueda);
			}
			else if(parametrosBusqueda.getCodigoClaseOrden() == ConstantesBD.claseOrdenOrdenAmbulatoria){
				//se valida que el parametro general corresponda a la via de ingreso que llega por parametro en lso filtros
				if(parametrosBusqueda.getParametroViaIngresoOrdenAmb() != null
						&& !parametrosBusqueda.getParametroViaIngresoOrdenAmb().trim().isEmpty()
						&& Integer.valueOf(parametrosBusqueda.getParametroViaIngresoOrdenAmb().trim()).intValue() == parametrosBusqueda.getCodigoViaIngreso()){
					ordenesPorRango=ordenesAmbulatoriasDelegate.obtenerOrdenesAmbulatoriasPorAutorizar(parametrosBusqueda);
				}
			}
			else if(parametrosBusqueda.getCodigoClaseOrden() == ConstantesBD.claseOrdenPeticion){
				//se valida que el parametro general corresponda a la via de ingreso que llega por parametro en lso filtros
				if(parametrosBusqueda.getParametroViaIngresoPeticion() != null
						&& !parametrosBusqueda.getParametroViaIngresoPeticion().trim().isEmpty()
						&& Integer.valueOf(parametrosBusqueda.getParametroViaIngresoPeticion().trim()).intValue() == parametrosBusqueda.getCodigoViaIngreso()){
					ordenesPorRango=peticionesDelegate.obtenerPeticionesQxPorAutorizar(parametrosBusqueda);
				}
			}
			
			if(ordenesPorRango != null && !ordenesPorRango.isEmpty()){
				for(OrdenAutorizacionDto ordenDto:ordenesPorRango){
					boolean valido=true;
					if(ordenDto.getClaseOrden()==ConstantesBD.claseOrdenOrdenMedica
							|| ordenDto.getClaseOrden()==ConstantesBD.claseOrdenCargoDirecto){
						//Se obtene la via de ingreso asociada a la última cuenta del ingreso de la orden Médica
						ordenDto.setCodigoViaIngreso(autorizacionesDelegate.obtenerViaIngresoPorIngreso(ordenDto.getCodigoIngreso()));
						//Se valida que la via de ingreso de la orden sea la misma que la via ingreso que llega por parámetro
						if(ordenDto.getCodigoViaIngreso()==parametrosBusqueda.getCodigoViaIngreso()){
							//Se obtiene el nombre del convenio capitado asociado a la orden médica
							ContratoDto infoContrato=solicitudesDelegate.obtenerConvenioContratoPorOrdenMedica(ordenDto.getCodigoOrden().intValue(), ordenDto.getClaseOrden(), ordenDto.getTipoOrden());
							ordenDto.setContrato(infoContrato);
							if(ordenDto.getTipoOrden()==ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos
									|| ordenDto.getTipoOrden()==ConstantesBD.codigoTipoSolicitudMedicamentos){
								//Se obtienen los Medicamentos/Insumos de la orden Médica
								ordenDto.setMedicamentosInsumosPorAutorizar(solicitudesDelegate
											.obtenerMedicamentosInsumosPorAutorizar(ordenDto.getCodigoOrden().intValue()));
							}
							else{
								//Se obtienen los Servicios de la orden Médica
								ordenDto.setServiciosPorAutorizar(solicitudesDelegate.
											obtenerServiciosPorAutorizar(ordenDto.getCodigoOrden().intValue(), 
														ordenDto.getClaseOrden(), ordenDto.getTipoOrden()));
							}
						}
						else{
							valido=false;
						}
					}
					else if(ordenDto.getClaseOrden()==ConstantesBD.claseOrdenOrdenAmbulatoria){
						//Se obtiene el nombre del convenio capitado asociado a la orden Ambulatoria
						ContratoDto infoContrato=ordenesAmbulatoriasDelegate.obtenerConvenioContratoPorOrdenAmbulatoria(ordenDto.getCodigoOrden(), ordenDto.getTipoOrden());
						ordenDto.setContrato(infoContrato);
						if(ordenDto.getTipoOrden()==ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos){
							//Se obtienen los Medicamentos/Insumos de la orden ambulatoria
							ordenDto.setMedicamentosInsumosPorAutorizar(ordenesAmbulatoriasDelegate.
										obtenerMedicamentosInsumosPorAutorizar(ordenDto.getCodigoOrden()));
						}
						else{
							//Se obtienen los Servicios de la orden ambulatoria
							ordenDto.setServiciosPorAutorizar(ordenesAmbulatoriasDelegate.
										obtenerServiciosPorAutorizar(ordenDto.getCodigoOrden()));
						}
					}
					else if(ordenDto.getClaseOrden()==ConstantesBD.claseOrdenPeticion){
						//Se obtiene el nombre del convenio capitado asociado a la orden Ambulatoria
						ContratoDto infoContrato=peticionesDelegate.obtenerConvenioContratoPorPeticion(ordenDto.getCodigoOrden().intValue());
						ordenDto.setContrato(infoContrato);
						//Se obtienen los Servicios de la petición
						ordenDto.setServiciosPorAutorizar(peticionesDelegate.
									obtenerServiciosPorAutorizar(ordenDto.getCodigoOrden().intValue()));
					}
					if(valido){
						ordenesPorAutorizar.add(ordenDto);
					}
				}
			}
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return ordenesPorAutorizar;
	}

	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#posponerOrdenes(java.util.List, java.lang.String, java.util.Date, java.lang.String)
	 * @author ricruico
	 */
	@Override
	public boolean posponerOrdenes(List<OrdenAutorizacionDto> ordenesPosponer, String loginUsuario, 
						Date fechaPosponer, String observaciones)  throws IPSException{
		Connection con=null;
		try{
			HibernateUtil.beginTransaction();
			con=UtilidadBD.abrirConexion();
			Date fechaActual=UtilidadFecha.getFechaActualTipoBD(con);
			String horaActual=UtilidadFecha.getHoraActual(con);
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			autorizacionesDelegate.posponerOrdenes(ordenesPosponer, loginUsuario, fechaActual, 
													horaActual, fechaPosponer, observaciones);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return true;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#verificarEntidadSubContratadaParametrizada(java.lang.String)
	 * @author ricruico
	 */
	@Override
	public EntidadSubContratadaDto verificarEntidadSubContratadaParametrizada(String codigoEntidad, boolean requiereTransaccion)  throws IPSException{
		EntidadSubContratadaDto infoEntidad=null;
		try{
			if(requiereTransaccion){
				HibernateUtil.beginTransaction();
			}
			
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			infoEntidad=autorizacionesDelegate.verificarEntidadSubContratadaParametrizada(new Long(codigoEntidad));
			
			if(requiereTransaccion){
				HibernateUtil.endTransaction();
			}
			
		}
		catch (IPSException ipsme) {
			if(requiereTransaccion){
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		}
		catch (Exception e) {
			if(requiereTransaccion){
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return infoEntidad;
	}
	/**
	 * Consulta todos los servicios autorizados de capitacion (Solicitud, Orden Ambulatoria o Peticion)
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	public List<ServicioAutorizadoCapitacionDto> consultarServiciosAutorizadosCapitacion(long consecutivo,long tarifario,boolean isMigrado)throws IPSException{
		AutorizacionesDelegate delegate = null;
		List<ServicioAutorizadoCapitacionDto>listaServicios=null;
		try{
			HibernateUtil.beginTransaction();
			delegate				= new AutorizacionesDelegate();
			listaServicios=delegate.consultarServiciosAutorizadosCapitacion(consecutivo, tarifario,isMigrado);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		return listaServicios;
	}
	
	/**
	 * Consulta todos los servicios autorizados de capitacion (Solicitud o Orden Ambulatoria)
	 * @param institucion 
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	public List<ArticuloAutorizadoCapitacionDto> consultarArticulosAutorizadosCapitacion(long consecutivo,int institucion,boolean isMigrado)throws IPSException{
		AutorizacionesDelegate delegate = null;
		List<ArticuloAutorizadoCapitacionDto>listaArticulos=null;
		try{
			HibernateUtil.beginTransaction();
			delegate				= new AutorizacionesDelegate();
			listaArticulos=delegate.consultarArticulosAutorizadosCapitacion(consecutivo, institucion, isMigrado);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		return listaArticulos;
	}
	
	/**
	 * Método que se encarga Valdiar la Anulación de la Autorización de Capitación para el Tipo de Solicitud.
	 * 1. Anular solicitud	2. Anular Autorizacion	3. Calcular cierre Anuladas
	 * 
	 * @author wilgomcr
	 * @param anulacionAutorizacionDto
	 * @param claseOrden
	 * @param tipoOrden
	 * @param tipoOrdenPyp
	 * @throws IPSException
	 */
	public List<AutorizacionCapitacionDto> validarAnulacionAutorizacionCapitaSolictud(AnulacionAutorizacionSolicitudDto  anulacionAutorizacionDto,
			int claseOrden, int tipoOrden, String tipoOrdenPyp, int institucion)throws IPSException
	{
		List<AutorizacionPorOrdenDto> listAutorizacionPorOrdenDto	= null;
		AutorizacionesDelegate autorizacionesDelegate	= null;
		ICierrePresupuestoMundo cierrePresupuestoMundo	= null;
		boolean calcularCierreAnulacion		= false;
		ISolicitudesMundo solicitudesMundo 	= null;
		IOrdenesAmbulatoriasMundo ordenAmbulatoriaMundo = null;
		IPeticionesMundo peticionesMundo = null;
		AutorizacionCapitacionDto autorizacionCapitacionDto = null;
		List<AutorizacionCapitacionDto> listaAutorizaCapita	= null;
		try{
			HibernateUtil.beginTransaction();
			
			if(claseOrden==ConstantesBD.claseOrdenOrdenMedica && (tipoOrden==ConstantesBD.codigoTipoSolicitudInterconsulta 
					|| tipoOrden==ConstantesBD.codigoTipoSolicitudProcedimiento	|| tipoOrden==ConstantesBD.codigoTipoSolicitudMedicamentos)){
				//Metodo que se encarga de anulacion de la solicitud de Aplica INTERCONSULTA - PROCEDIMIENTO - MEDICAMENTOS
				solicitudesMundo = new SolicitudesMundo();
				solicitudesMundo.anularSolicitudes(anulacionAutorizacionDto);
			}else if(claseOrden==ConstantesBD.claseOrdenOrdenAmbulatoria){
				//Metodo que se encarga de anulacion de la ORDEN AMBULATORIA
				ordenAmbulatoriaMundo = new OrdenesAmbulatoriasMundo();
				ordenAmbulatoriaMundo.anularOrdenAmbulatoria(anulacionAutorizacionDto.getCodigoOrdenAmbulatoria().longValue(),
						anulacionAutorizacionDto.getLoginUsuarioAnulacion(), anulacionAutorizacionDto.getFechaAnulacion(),
						anulacionAutorizacionDto.getHoraAnulacion(), anulacionAutorizacionDto.getMotivoAnulacion(),
						anulacionAutorizacionDto.isPyP());
			}else if (claseOrden==ConstantesBD.claseOrdenPeticion){
				//Metodo que se encarga de anulacion de la PETICION
				peticionesMundo	= new PeticionesMundo();
				peticionesMundo.anularPeticion(anulacionAutorizacionDto.getCodigoPeticion(), anulacionAutorizacionDto.getLoginUsuarioAnulacion(),
						anulacionAutorizacionDto.getFechaAnulacion(), anulacionAutorizacionDto.getHoraAnulacion(),  
						anulacionAutorizacionDto.getCodigoMotivoAnulacion(), anulacionAutorizacionDto.getMotivoAnulacion(),false);
			}
			
			autorizacionesDelegate = new AutorizacionesDelegate();
			List<String> estadosAutorizacion = new ArrayList<String>();
			estadosAutorizacion.add(ConstantesIntegridadDominio.acronimoAutorizado);
			if(anulacionAutorizacionDto.getNumeroSolicitud()!=null){
				//Consulta: La SOLICITUD tiene asociada una Autorizacion de Capitacion
				listAutorizacionPorOrdenDto	= autorizacionesDelegate.
							existeAutorizacionCapitaSolicitud(anulacionAutorizacionDto.getNumeroSolicitud(), estadosAutorizacion);
			}else if(anulacionAutorizacionDto.getCodigoOrdenAmbulatoria()!=null){
				//Consulta: La ORDEN AMBULATORIA tiene asociada una Autorizacion de Capitacion
				listAutorizacionPorOrdenDto	= autorizacionesDelegate.
						existeAutorizacionCapitaOrdenAmbulatoria(anulacionAutorizacionDto.getCodigoOrdenAmbulatoria(), estadosAutorizacion);
			}else if(anulacionAutorizacionDto.getCodigoPeticion()!=null){
				//Consulta: La PETICION tiene asociada una Autorizacion de Capitacion
				listAutorizacionPorOrdenDto	= autorizacionesDelegate.
						existeAutorizacionCapitaPeticion(anulacionAutorizacionDto.getCodigoPeticion(), estadosAutorizacion);
			}
			
			//1. Validacion: existe autorizacion para la solicitud.
			if(listAutorizacionPorOrdenDto != null && !listAutorizacionPorOrdenDto.isEmpty())
			{
				//Se valida que todas las autorizaciones sean Automaticas, y se cancela la anulacion de autorizacion y solicitud
				if(claseOrden==ConstantesBD.claseOrdenOrdenAmbulatoria || 
						(claseOrden==ConstantesBD.claseOrdenOrdenMedica && tipoOrden==ConstantesBD.codigoTipoSolicitudMedicamentos))
				{
					/*
					 * MT 5521
					 */
					
					if(listAutorizacionPorOrdenDto.size()>1){
						for (AutorizacionPorOrdenDto autorizacionPorOrdenDto : listAutorizacionPorOrdenDto){
							if(autorizacionPorOrdenDto.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoManual)){
								if(tipoOrden==ConstantesBD.codigoTipoSolicitudMedicamentos){
									String []param={"Solicitud","Capitación Subcontratada"};
									throw new IPSException(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ANULAR_ORDENES_AUTORIZACION,param);
								}else{
									String []param={"Orden Ambulatoria","Capitación Subcontratada"};
									throw new IPSException(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ANULAR_ORDENES_AUTORIZACION,param);
								}
							}
						}
					}
					/*for (AutorizacionPorOrdenDto autorizacionPorOrdenDto : listAutorizacionPorOrdenDto){
						if(autorizacionPorOrdenDto.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoManual)){
							if(tipoOrden==ConstantesBD.codigoTipoSolicitudMedicamentos){
								if(listAutorizacionPorOrdenDto.size()>1){
									String []param={"Solicitud","Capitación Subcontratada"};
									throw new IPSException(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ANULAR_ORDENES_AUTORIZACION,param);
								}
							}else{
								String []param={"Orden Ambulatoria","Capitación Subcontratada"};
								throw new IPSException(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ANULAR_ORDENES_AUTORIZACION,param);
							}
						}
					}*/
				}
				
				listaAutorizaCapita = new ArrayList<AutorizacionCapitacionDto>();
				for (AutorizacionPorOrdenDto autorizacionPorOrdenDto : listAutorizacionPorOrdenDto)
				{
					boolean esServicios=true;
					if(tipoOrden==ConstantesBD.codigoTipoSolicitudInterconsulta){
						calcularCierreAnulacion	= this.procesoAnulacionAutorizacionInterconsulta(
								anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion);
						
					}else if(tipoOrden==ConstantesBD.codigoTipoSolicitudProcedimiento){
						autorizacionCapitacionDto  = this.procesoAnulacionAutorizacionProcedimiento(
								anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion,tipoOrden,
								autorizacionCapitacionDto);
						calcularCierreAnulacion=autorizacionCapitacionDto.isActualizarCierre();
						
					}else if(tipoOrden==ConstantesBD.codigoTipoSolicitudMedicamentos){
						calcularCierreAnulacion	= this.procesoAnulacionAutorizacionMedicamentos(
								anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion);
						esServicios=false;
					}else if(tipoOrden==ConstantesBD.codigoTipoSolicitudCirugia){
						calcularCierreAnulacion	= this.procesoAnulacionAutorizacionCirugia(
								anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion);
						  
					}else if(claseOrden==ConstantesBD.claseOrdenOrdenAmbulatoria){
						autorizacionCapitacionDto	  = this.procesoAnulacionAutorizacionOrdenAmbulatoria(
								anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion,tipoOrden,
								autorizacionCapitacionDto);
						calcularCierreAnulacion=autorizacionCapitacionDto.isActualizarCierre();
						if(tipoOrden==ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos){
							esServicios=false;
						}
						
					}else if(claseOrden==ConstantesBD.claseOrdenPeticion){
						calcularCierreAnulacion = this.procesoAnulacionAutorizacionPeticion(
								anulacionAutorizacionDto, autorizacionPorOrdenDto, institucion);
						
					}else if(tipoOrdenPyp!=null && (tipoOrdenPyp.equals(ConstantesBD.tipoActividadPYPArticulo) 
							|| tipoOrdenPyp.equals(ConstantesBD.tipoActividadPYPServicio))){
						calcularCierreAnulacion	= this.procesoAnulacionAutorizacionProgramasPyP(
								anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion);
						if(tipoOrdenPyp.equals(ConstantesBD.tipoActividadPYPArticulo)){
							esServicios=false;
					}
					}
					listaAutorizaCapita.add(autorizacionCapitacionDto);
					//Se valida si el mes/año de la autorización corresponde con el mes/año actual
					//para permitir recalcular el cierre temporal
					Calendar mesAnioActual= Calendar.getInstance();
					Calendar mesAnioAutorizacion=Calendar.getInstance();
					mesAnioAutorizacion.setTime(autorizacionPorOrdenDto.getFechaAutorizacionCapitacion());
					if(mesAnioActual.get(Calendar.MONTH) == mesAnioAutorizacion.get(Calendar.MONTH)
							&& mesAnioActual.get(Calendar.YEAR) == mesAnioAutorizacion.get(Calendar.YEAR)){
						calcularCierreAnulacion=true;
					}
					else{
						calcularCierreAnulacion=false;
					}
				if(calcularCierreAnulacion){
					cierrePresupuestoMundo	= new CierrePresupuestoMundo();
						cierrePresupuestoMundo.recalcularCierreTemporalPresupuesto(anulacionAutorizacionDto, 
								autorizacionPorOrdenDto, institucion, esServicios);
					}
				}
			}
			HibernateUtil.endTransaction();
			return listaAutorizaCapita;			
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}

	/**
	 * Metodo que se encarga del proceso de anulacion de la autorizacion
	 * de la solicitud de INTERCONSULTA
	 * 
	 * @param anulacionAutorizacionDto
	 * @param autorizacionPorOrdenDto
	 * @param institucion
	 * @return boolean
	 * @throws IPSException
	 */
	private boolean procesoAnulacionAutorizacionInterconsulta(AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto,
			AutorizacionPorOrdenDto autorizacionPorOrdenDto,int institucion)throws IPSException
	{
		//ICierrePresupuestoMundo cierrePresupuestoMundo	= null;
		try{
			//Metodo que se encarga de anular la autorizacion
			anularAutorizacionCapitacion(anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion);
			return true;
			//Se recalcula el cierre con las solicitudes y autorizaciones anuladas 
			/*cierrePresupuestoMundo	= new CierrePresupuestoMundo();
			cierrePresupuestoMundo.recalcularCierrePresupuesto(anulacionAutorizacionDto, 
								autorizacionPorOrdenDto, institucion);*/
		}
		catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Metodo que se encarga del proceso de anulacion de la autorizacion
	 * de la solicitud de PROCEDIMIENTO
	 * 
	 * @param anulacionAutorizacionDto
	 * @param autorizacionPorOrdenDto
	 * @param institucion
	 * @return boolean
	 * @throws IPSException
	 */
	private AutorizacionCapitacionDto procesoAnulacionAutorizacionProcedimiento(AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto,
			AutorizacionPorOrdenDto autorizacionPorOrdenDto,int institucion, int tipoOrden,
			AutorizacionCapitacionDto autorizacionCapitacionDto)throws IPSException
	{
		ProcesoGeneracionAutorizacionMundo procesoGeneracionAutorizacionMundo = null;
		List<AutorizacionPorOrdenDto> listSolicitudesAsociadas	= null;
		AutorizacionesDelegate autorizacionesDelegate			= null;
		boolean actualizarCierre	= false;
		Connection con	= null;
		try{
			autorizacionesDelegate		= new AutorizacionesDelegate();
			
			//2. Validacion: Se encuentra asociada a una Orden Ambulatoria
			if(autorizacionPorOrdenDto.getMigrado()!=ConstantesBD.acronimoSiChar)
			{
				listSolicitudesAsociadas = autorizacionesDelegate.
						obtenerSolicitudesAsociadasAutorizacion(autorizacionPorOrdenDto.getConsecutivo(),anulacionAutorizacionDto.getNumeroSolicitud());
				
				//3. Consulta: La autorizacion asociada a otras solicitudes
				if(listSolicitudesAsociadas != null && !listSolicitudesAsociadas.isEmpty()){
					if(autorizacionPorOrdenDto.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoManual)){
						String []param={"Solicitud","Capitación Subcontratada"};
						throw new IPSException(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ANULAR_ORDENES_AUTORIZACION,param);
					}else{
						anularAutorizacionCapitacion(anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion);
						actualizarCierre = true;
						
						//Consulta la autorizacion para guarde los mismos datos
						autorizacionCapitacionDto=autorizacionesDelegate.consultarAutorizacionCapitacion(
								Utilidades.convertirAEntero(autorizacionPorOrdenDto.getConsecutivo()+""),
								anulacionAutorizacionDto.getNumeroSolicitud(), ConstantesBD.codigoTipoSolicitudProcedimiento);
						
						if(autorizacionCapitacionDto !=null){
							con = UtilidadBD.abrirConexion();
							procesoGeneracionAutorizacionMundo	= new ProcesoGeneracionAutorizacionMundo();
							//Generacion de nueva autorizacion con solicitudes asociadas restantes 
							procesoGeneracionAutorizacionMundo.guardarProcesoAutorizacion(con, autorizacionCapitacionDto);
							procesoGeneracionAutorizacionMundo.generarAcumuladoCierreTemporal(autorizacionCapitacionDto);
							String codigosOrdenes = "";
							int cont = 1;
							for(OrdenAutorizacionDto ordenGenerada: autorizacionCapitacionDto.getOrdenesAutorizar()){
								codigosOrdenes += ordenGenerada.getConsecutivoOrden();
								if(cont != autorizacionCapitacionDto.getOrdenesAutorizar().size()){
									codigosOrdenes +=", ";
								}
								cont++;
							}
							
							String []param={autorizacionCapitacionDto.getConsecutivoAutorizacionCapita(),codigosOrdenes};
							ErrorMessage mensajeErrorGeneral = new ErrorMessage(
									"errors.autorizacion.nuevaAutorizacionGenerada",param);
							autorizacionCapitacionDto.setVerificarDetalleError(false);
							autorizacionCapitacionDto.setMensajeErrorGeneral(mensajeErrorGeneral);
							UtilidadBD.cerrarConexion(con);
						}
					}
				}else{
					anularAutorizacionCapitacion(anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion);
					actualizarCierre = true;
				}
			}else{
				//el campo Migrado es 'S' entonces se genero desde la Orden Ambulatoria
				//Se continúa con el flujo actual de la funcionalidad y se anula la solicitud.
			}
			if(autorizacionCapitacionDto !=null){
				autorizacionCapitacionDto.setActualizarCierre(actualizarCierre);
			}else{
				autorizacionCapitacionDto = new AutorizacionCapitacionDto();
				autorizacionCapitacionDto.setActualizarCierre(actualizarCierre);
			}
			return autorizacionCapitacionDto;
		}
		catch (IPSException ipsme) {
			if(con!=null){
				UtilidadBD.abortarTransaccion(con);
			}
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}

	/**
	 * Metodo que se encarga del proceso de anulacion de la autorizacion
	 * de la solicitud de MEDICAMENTOS
	 * 
	 * @param anulacionAutorizacionDto
	 * @param autorizacionPorOrdenDto
	 * @param institucion
	 * @return boolean
	 * @throws IPSException
	 */
	private boolean procesoAnulacionAutorizacionMedicamentos(AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto,
			AutorizacionPorOrdenDto autorizacionPorOrdenDto,int institucion)throws IPSException
	{
		boolean actualizarCierre	= false;
		try{
			//2. Validacion: Se encuentra asociada a una Orden Ambulatoria 
			if(autorizacionPorOrdenDto.getMigrado()!=ConstantesBD.acronimoSiChar)
			{
				anularAutorizacionCapitacion(anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion);
				actualizarCierre = true;
			}else{
				//el campo Migrado es 'S' entonces se genero desde la Orden Ambulatoria
				//Se continúa con el flujo actual de la funcionalidad y se anula la solicitud.
			}
			return actualizarCierre;
		}
		catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}

	/**
	 * Metodo que se encarga del proceso de anulacion de la autorizacion
	 * de la solicitud de CIRUGIA
	 * 
	 * @param anulacionAutorizacionDto
	 * @param autorizacionPorOrdenDto
	 * @param institucion
	 * @return boolean
	 * @throws IPSException
	 */
	private boolean procesoAnulacionAutorizacionCirugia(AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto,
			AutorizacionPorOrdenDto autorizacionPorOrdenDto,int institucion)throws IPSException
	{
		boolean actualizarCierre	= false;
		//AutorizacionesDelegate autorizacionesDelegate = null;
		//List<AutorizacionPorOrdenDto> listaAutorizPeticion	= null;
		try{
			/*autorizacionesDelegate = new AutorizacionesDelegate();
			listaAutorizPeticion = autorizacionesDelegate.existeAutorizacionCapitaPeticion(anulacionAutorizacionDto.getNumeroSolicitud());
			FIXME la consulta se puede quitar ya que con el campo 'migrado' se puede saber si se genero
			desde Orden Ambulatoria o Peticion para ese numero_solicitud.*/
			
			//2. Validacion: Se encuentra asociada a una Peticion
			//if(listaAutorizPeticion != null && !listaAutorizPeticion.isEmpty())
			//{
				//3. Se valida si la autorizacion se genero desde la peticion
				if(autorizacionPorOrdenDto.getMigrado()!=ConstantesBD.acronimoSiChar)
				{
					//Validar desde donde se genero la autorizacion (Desde la solicitud o peticion)
					anularAutorizacionCapitacion(anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion);
					actualizarCierre = true;
				}else{
					//el campo Migrado es 'S' entonces se genero desde la Peticion
					//Se continúa con el flujo actual de la funcionalidad y se anula la solicitud.
					anularAutorizacionCapitacion(anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion);
					//Se modifica porque habia un vacio funcional en el documento
					actualizarCierre = true;
				}
			/*}else{
				anularAutorizacionCapitacion(anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion);
				actualizarCierre = true;
			}*/
			return actualizarCierre;
		}
		catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Metodo que se encarga del proceso de anulacion de la autorizacion
	 * de ORDENES AMBULATORIAS
	 * 
	 * @param anulacionAutorizacionDto
	 * @param autorizacionPorOrdenDto
	 * @param institucion
	 * @param tipoOrdenAmbulatoria
	 * @return boolean
	 * @throws IPSException
	 */
	private AutorizacionCapitacionDto procesoAnulacionAutorizacionOrdenAmbulatoria(AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto,
			AutorizacionPorOrdenDto autorizacionPorOrdenDto,int institucion,int tipoOrdenAmbulatoria,
			AutorizacionCapitacionDto autorizacionCapitacionDto)throws IPSException
	{
		ProcesoGeneracionAutorizacionMundo procesoGeneracionAutorizacionMundo = null;
		List<AutorizacionPorOrdenDto> listSolicitudesAsociadas	= null;
		AutorizacionesDelegate autorizacionesDelegate 			= null;
		boolean actualizarCierre	= false;
		Connection con	= null;
		try{
			autorizacionesDelegate = new AutorizacionesDelegate();
			
			listSolicitudesAsociadas = autorizacionesDelegate.
					obtenerOrdenesAmbuAsociadasAutorizacion(autorizacionPorOrdenDto.getConsecutivo(),anulacionAutorizacionDto.getCodigoOrdenAmbulatoria());
			if(listSolicitudesAsociadas != null && !listSolicitudesAsociadas.isEmpty())
			{
				if(!UtilidadTexto.isEmpty(autorizacionPorOrdenDto.getConsecutivoAutorizacion())){
					String []param={"Orden Ambulatoria","Entidad Subcontratada"};
					throw new IPSException(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ANULAR_ORDENES_AUTORIZACION,param);
				}else{
					anularAutorizacionCapitacion(anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion);
					actualizarCierre = true;
					
					//Generacion de nueva autorizacion con solicitudes asociadas restantes
					autorizacionCapitacionDto=autorizacionesDelegate.consultarAutorizacionCapitacion(
							Utilidades.convertirAEntero(autorizacionPorOrdenDto.getConsecutivo()+""),
							Utilidades.convertirAEntero(anulacionAutorizacionDto.getCodigoOrdenAmbulatoria()+""),
							tipoOrdenAmbulatoria);
					
					if(autorizacionCapitacionDto !=null){
						con = UtilidadBD.abrirConexion();
						procesoGeneracionAutorizacionMundo	= new ProcesoGeneracionAutorizacionMundo();
						procesoGeneracionAutorizacionMundo.guardarProcesoAutorizacion(con, autorizacionCapitacionDto);
						procesoGeneracionAutorizacionMundo.generarAcumuladoCierreTemporal(autorizacionCapitacionDto);
						String codigosOrdenes = "";
						int cont = 1;
						for(OrdenAutorizacionDto ordenGenerada: autorizacionCapitacionDto.getOrdenesAutorizar()){
							codigosOrdenes += ordenGenerada.getConsecutivoOrden();
							if(cont != autorizacionCapitacionDto.getOrdenesAutorizar().size()){
								codigosOrdenes +=", ";
							}
							cont++;
						}
						
						String []param={autorizacionCapitacionDto.getConsecutivoAutorizacionCapita(),codigosOrdenes};
						ErrorMessage mensajeErrorGeneral = new ErrorMessage(
								"errors.autorizacion.nuevaAutorizacionGenerada",param);
						autorizacionCapitacionDto.setVerificarDetalleError(false);
						autorizacionCapitacionDto.setMensajeErrorGeneral(mensajeErrorGeneral);
						UtilidadBD.cerrarConexion(con);
					}
				}
			}else{
				if(!UtilidadTexto.isEmpty(autorizacionPorOrdenDto.getConsecutivoAutorizacion())){
					String []param={"Orden Ambulatoria","Entidad Subcontratada"};
					throw new IPSException(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ANULAR_ORDENES_AUTORIZACION,param);
				}else{
					anularAutorizacionCapitacion(anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion);
					actualizarCierre = true;
				}
			}
			
			if(autorizacionCapitacionDto !=null){
				autorizacionCapitacionDto.setActualizarCierre(actualizarCierre);
			}else{
				autorizacionCapitacionDto = new AutorizacionCapitacionDto();
				autorizacionCapitacionDto.setActualizarCierre(actualizarCierre);
			}
			return autorizacionCapitacionDto;
		}
		catch (IPSException ipsme) {
			if(con!=null){
				UtilidadBD.abortarTransaccion(con);
			}
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Metodo que se encarga del proceso de anulacion de la autorizacion
	 * de PETICION
	 * 
	 * @param anulacionAutorizacionDto
	 * @param autorizacionPorOrdenDto
	 * @param institucion
	 * @return boolean
	 * @throws IPSException
	 */
	private boolean procesoAnulacionAutorizacionPeticion(AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto,
			AutorizacionPorOrdenDto autorizacionPorOrdenDto,int institucion)throws IPSException
	{
		boolean actualizarCierre	= false;
		try{
			if(!UtilidadTexto.isEmpty(autorizacionPorOrdenDto.getConsecutivoAutorizacion())){
				String []param={"Peticion","Entidad Subcontratada"};
				throw new IPSException(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ANULAR_ORDENES_AUTORIZACION,param);
			}else{
				anularAutorizacionCapitacion(anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion);
				actualizarCierre = true;
			}
			
			return actualizarCierre;
		}
		catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Metodo que se encarga del proceso de anulacion de la autorizacion
	 * deL PROGRAMA DE PYP
	 * 
	 * @param anulacionAutorizacionDto
	 * @param autorizacionPorOrdenDto
	 * @param institucion
	 * @return boolean
	 * @throws IPSException
	 */
	private boolean procesoAnulacionAutorizacionProgramasPyP(AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto,
			AutorizacionPorOrdenDto autorizacionPorOrdenDto,int institucion)throws IPSException
	{
		try{
			//Metodo que se encarga de anular la autorizacion
			anularAutorizacionCapitacion(anulacionAutorizacionDto,autorizacionPorOrdenDto,institucion);
			return true;
		}
		catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Método que se encarga de anular la Autorización de Capitación de la Solicitud
	 * 
	 * @param anulacionAutorizacionsolicitudDto
	 * @param autorizacionPorOrdenDto
	 * @param institucion
	 * @throws IPSException
	 */
	public void anularAutorizacionCapitacion(AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto, 
			AutorizacionPorOrdenDto autorizacionPorOrdenDto, int institucion)throws IPSException
	{
		AutorizacionesDelegate delegate	= null; 
		AutorizacionesEntidadesSub autorizacionesEntidadesSub	= null;
		ProcesoGeneracionAutorizacionMundo procesoGeneracionAutorizacionMundo = null;
		AutorizacionesDelegate autorizacionesDelegate 			= null;
		AutorizacionesCapitacionSub autorizacionesCapitacionSub = null;
		AutorizacionCapitacionDto autorizacionCapitacionDto		= null;
		Usuarios usuarioAnula	= null;
		try{
			delegate	= new AutorizacionesDelegate();
			autorizacionesEntidadesSub	= delegate.obtenerAutorizacionEntidadSub(autorizacionPorOrdenDto.getConsecutivo());
			autorizacionesEntidadesSub.setEstado(ConstantesIntegridadDominio.acronimoEstadoAnulado);
			autorizacionesEntidadesSub.setMotivoAnulacion(anulacionAutorizacionDto.getMotivoAnulacion());
			autorizacionesEntidadesSub.setFechaAnulacion(anulacionAutorizacionDto.getFechaAnulacion());
			autorizacionesEntidadesSub.setHoraAnulacion(anulacionAutorizacionDto.getHoraAnulacion());
			usuarioAnula	= new Usuarios();
			usuarioAnula.setLogin(anulacionAutorizacionDto.getLoginUsuarioAnulacion());
			autorizacionesEntidadesSub.setUsuarios(usuarioAnula);
			delegate.actualizarAutorizacionEntidadSub(autorizacionesEntidadesSub);
			
			//HibernateUtil.flush();
			
			//Consulto el la autorización de entidad subcontratada y capitación subcontratada
			autorizacionesDelegate	  = new AutorizacionesDelegate();
			autorizacionCapitacionDto = autorizacionesDelegate.obtenerAutorizacionCapitacionSub(autorizacionPorOrdenDto.getConsecutivo());
			
			if(autorizacionCapitacionDto!=null){
				autorizacionesEntidadesSub.setConsecutivo(autorizacionPorOrdenDto.getConsecutivo());
				autorizacionesCapitacionSub	= new AutorizacionesCapitacionSub();
				autorizacionesCapitacionSub.setCodigoPk(autorizacionPorOrdenDto.getCodigoPkCapitacion());
				autorizacionesCapitacionSub.setConsecutivo(autorizacionPorOrdenDto.getConsecutivoCapitacion());
				autorizacionesCapitacionSub.setTipoAutorizacion(autorizacionCapitacionDto.getTipoAutorizacion());
				autorizacionesCapitacionSub.setIndicativoTemporal(autorizacionCapitacionDto.getIndicativoTemporal());
				autorizacionesCapitacionSub.setDescripcionEntidad(autorizacionCapitacionDto.getEntidadSubAutorizarCapitacion().getRazonSocial());
				autorizacionesCapitacionSub.setDireccionEntidad(autorizacionCapitacionDto.getEntidadSubAutorizarCapitacion().getDireccionEntidad());
				autorizacionesCapitacionSub.setTelefonoEntidad(autorizacionCapitacionDto.getEntidadSubAutorizarCapitacion().getTelefonoEntidad());
				//MT 5617-5629
				autorizacionCapitacionDto.setObservacionesGenerales(anulacionAutorizacionDto.getMotivoAnulacion());
				procesoGeneracionAutorizacionMundo = new ProcesoGeneracionAutorizacionMundo();
				procesoGeneracionAutorizacionMundo.generarAutorizacionEntSubHistorico(autorizacionCapitacionDto,
						autorizacionesEntidadesSub, autorizacionesCapitacionSub,true);
			}
			
		}
		catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#validarGenerarAutorizacionCapitacion(com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto)
	 */
	@Override
	public List<AutorizacionCapitacionDto> validarGenerarAutorizacionCapitacion(
			AutorizacionCapitacionDto autorizacionCapitacionDto) throws IPSException{
			
		boolean existeConsecutivoAutoEntSub = false;
		boolean existeConsecutivoAutoCapiSub = false;
		NivelAutorizacionMundo nivelAutorizacionMundo = null;
		ProcesoGeneracionAutorizacionMundo procesoGeneracionAutorizacionMundo = null;
		List<AutorizacionCapitacionDto> listaAutorizaciones = new ArrayList<AutorizacionCapitacionDto>();
		
		try{
			
			nivelAutorizacionMundo = new NivelAutorizacionMundo();
			procesoGeneracionAutorizacionMundo = new ProcesoGeneracionAutorizacionMundo();
			
			/**Verifico si se encuentra definido el consecutivo de Autorizaciones de Entidad Subcontratada */
 			UtilidadesMundo utilidadesMundo = new UtilidadesMundo();
 			existeConsecutivoAutoEntSub = utilidadesMundo.existeConsecutivo(
 					ConstantesBD.nombreConsecutivoAutorizacionEntiSub, 
 					autorizacionCapitacionDto.getCodigoInstitucion());
 			
			/**Verifico si se encuentra definido el consecutivo de capitación Subcontratada */
 			existeConsecutivoAutoCapiSub = utilidadesMundo.existeConsecutivo(
 					ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, 
 					autorizacionCapitacionDto.getCodigoInstitucion()); 
 			
 			if (existeConsecutivoAutoEntSub && existeConsecutivoAutoCapiSub) {
 			
 				//Se realiza el llamado al DCU 1115. Se obtienen las autorizaciones (agrupadas si es el caso)
 				listaAutorizaciones = 
 						nivelAutorizacionMundo.validarNivelAutorizacionParaAutorizacionAutomatica(
 								autorizacionCapitacionDto, true);
 			
 				if(listaAutorizaciones!=null && !listaAutorizaciones.isEmpty()){
 					
	 				for (AutorizacionCapitacionDto autorizacionCapitacion : listaAutorizaciones) {
						
	 	 				if (autorizacionCapitacion.isProcesoExitoso()) {
	 	 					this.obtenerMensajesServiciosMedicamentosInsumosAutorizar(autorizacionCapitacion, true);
	 	 					try {
	 	 						HibernateUtil.beginTransaction();
	 	 						//Se hace el llamado al 1106 para realizar el proceso de autorizacion
	 	 						procesoGeneracionAutorizacionMundo.generacionAutorizacion(autorizacionCapitacion, false);
								HibernateUtil.endTransaction();
							} catch (IPSException ipsme) {
								this.marcarAutorizacionFallida(autorizacionCapitacion);
								HibernateUtil.abortTransaction();
								Log4JManager.error(ipsme.getMessage(),ipsme);
							} catch (Exception e) {
								this.marcarAutorizacionFallida(autorizacionCapitacion);
								HibernateUtil.abortTransaction();
								Log4JManager.error(e.getMessage(),e);
							}
	 	 				} else {
	 	 					//Si se realiza la verificacion de los servicio o insumos para validar si no se pueden
		 	 				//autorizar y colocar el respectivo mensaje
		 	 				this.obtenerMensajesServiciosMedicamentosInsumosAutorizar(autorizacionCapitacion, true);
	 	 				}
	 				}	
	 	 		}
 				
 			} else {
 				
 				this.obtenerMensajesServiciosMedicamentosInsumosAutorizar(autorizacionCapitacionDto, true);
 				autorizacionCapitacionDto.setProcesoExitoso(false);
 				autorizacionCapitacionDto.setVerificarDetalleError(true);
 				listaAutorizaciones.add(autorizacionCapitacionDto);
 				
 			}
 			
 			return listaAutorizaciones;
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#validarDescripcionServicio(com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto)
	 */
	@Override
	public void obtenerMensajesServiciosMedicamentosInsumosAutorizar(
			AutorizacionCapitacionDto autorizacionCapitacionDto, 
			boolean requiereTransaccion) throws IPSException {
		
		AutorizacionesDelegate autorizacionesDelegate = null;
		int codigoTarifarioServicio = ConstantesBD.codigoNuncaValido;
		StringBuilder mensaje = null;
		
		try {
			
			autorizacionesDelegate = new AutorizacionesDelegate();
			
			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}
			
			//la descripcion se debe mostrar de acuerdo a lo definido en el parámetro general 
			//para el modulo de Administración 'Codigo Manual estandar Búsqueda de Servicios'
			codigoTarifarioServicio = Utilidades.convertirAEntero(
					ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(
							autorizacionCapitacionDto.getCodigoInstitucion()));
			
			for (OrdenAutorizacionDto orden : autorizacionCapitacionDto.getOrdenesAutorizar()) {
				
				if (orden.getServiciosPorAutorizar() != null && 
						!orden.getServiciosPorAutorizar().isEmpty()) {

					for (ServicioAutorizacionOrdenDto servicio : orden.getServiciosPorAutorizar()) {

						if (!servicio.isAutorizar() || !servicio.isAutorizado()) {

							String[] resultado = 
									autorizacionesDelegate.obtenerDescripcionServicioAutorizar(
											servicio.getCodigo(), codigoTarifarioServicio);

							mensaje = new StringBuilder();
							mensaje.append(resultado[0] != null ? resultado[0] : "");
							//Se valida si la orden no es urgente (Normal)
							if (!orden.getEsUrgente()) {
								
								if (orden.getClaseOrden()==ConstantesBD.claseOrdenOrdenAmbulatoria){
									if (!UtilidadTexto.isEmpty(resultado[4])) {
										mensaje.append(" ");
										mensaje.append(resultado[4]);
										mensaje.append("  días ");
									}
								}else{
									if (!UtilidadTexto.isEmpty(resultado[5])) {
										//Si tiene informacion de acronimo días urgencia solo se muestra la descripcion del servicio
										mensaje.append(" - ");
										mensaje.append(resultado[5]);
									}
								}
							} else {
								if (orden.getClaseOrden()==ConstantesBD.claseOrdenOrdenAmbulatoria){
									if (!UtilidadTexto.isEmpty(resultado[2])) {
										mensaje.append(" ");
										mensaje.append(resultado[2]);
										mensaje.append("  días ");
									}
								}else{
									if (!UtilidadTexto.isEmpty(resultado[3])) {
										//Si tiene informacion de acronimo días normal solo se muestra la descripcion del servicio
										mensaje.append(" - ");
										mensaje.append(resultado[3]);
									}
								}
							}

							/*ErrorMessage mensajeError = new ErrorMessage(
									"errors.autorizacion.noAutorizarServicioOrden",
									mensaje.toString(),orden.getConsecutivoOrden()+"");*/

							ErrorMessage mensajeError = new ErrorMessage(
									"errors.autorizacion.noAutorizarServicioOrden",
									mensaje.toString());

							
							servicio.setMensajeError(mensajeError);

						}
					}
				}
				
				if (orden.getMedicamentosInsumosPorAutorizar() != null && 
						!orden.getMedicamentosInsumosPorAutorizar().isEmpty()) {

					for (MedicamentoInsumoAutorizacionOrdenDto medicamento : orden.getMedicamentosInsumosPorAutorizar()) {

						if (!medicamento.isAutorizar() || !medicamento.isAutorizado()) {

							mensaje = new StringBuilder();
							mensaje.append(medicamento.getDescripcion());

							/*ErrorMessage mensajeError = new ErrorMessage(
									"errors.autorizacion.noAutorizarMedicamentoInsumoOrden",
									mensaje.toString(),orden.getConsecutivoOrden()+"");*/
							
							ErrorMessage mensajeError = new ErrorMessage(
									"errors.autorizacion.noAutorizarMedicamentoInsumoOrden",
									mensaje.toString());

							medicamento.setMensajeError(mensajeError);

						}
					}
				}
			}
			
			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}
			
		} catch (IPSException ipsme) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		} catch (Exception e) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		
	}

	/**
	 * (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#obtenerDescripcionServicioAutorizar(int, int, boolean)
	 */
	@Override
	public String[] obtenerDescripcionServicioAutorizar(
			int codigoServicio, int codigoTarifarioServicio,
			boolean requiereTransaccion) throws IPSException {

		AutorizacionesDelegate autorizacionesDelegate = null;
		String[] descripcion = null;
		
		try {

			autorizacionesDelegate = new AutorizacionesDelegate();

			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}

			descripcion = autorizacionesDelegate.obtenerDescripcionServicioAutorizar(
					codigoServicio, codigoTarifarioServicio);

			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}

		} catch (IPSException ipsme) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		} catch (Exception e) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	
		return descripcion;
	}

	/**
	 * (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#obtenerDescripcionMedicamentoInsumoAutorizar(int, int, boolean)
	 */
	@Override
	public String[] obtenerDescripcionMedicamentoInsumoAutorizar(
			int codigoMedicamentoInsumo, int codigoInstitucion, 
			boolean requiereTransaccion) throws IPSException {
		
		AutorizacionesDelegate autorizacionesDelegate = null;
		String[] descripcion = null;
		
		try {

			autorizacionesDelegate = new AutorizacionesDelegate();

			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}

			descripcion = autorizacionesDelegate.obtenerDescripcionMedicamentoAutorizar(
					codigoMedicamentoInsumo, codigoInstitucion);
			
			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}

		} catch (IPSException ipsme) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		} catch (Exception e) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	
		return descripcion;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#obtenerAutorizacionEntSubArticulo(long, long, boolean)
	 */
	public List<AutorizacionesEntSubArticu> obtenerAutorizacionEntSubArticulo(long codigoAutoEntSub,int codigoArticulo, boolean requiereTransaccion)throws IPSException
	{
		AutorizacionesDelegate autorizacionesDelegate = null;
		List<AutorizacionesEntSubArticu> autorizacionesEntSubArticu=new ArrayList<AutorizacionesEntSubArticu>(0);
		
		try {
			autorizacionesDelegate=new AutorizacionesDelegate();
			
			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}
			
			autorizacionesEntSubArticu = autorizacionesDelegate.obtenerAutorizacionEntSubArticulo(codigoAutoEntSub,codigoArticulo);
			
			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}
		} catch (IPSException ipsme) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		} catch (Exception e) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		return autorizacionesEntSubArticu;
	}
	
	/**
	 * Método encargado de dejar en false la marca de autorizar de servicios y 
	 * medicamentos en caso de que falle la autorización
	 * @param autorizacionCapitacionDto
	 */
	private void marcarAutorizacionFallida(AutorizacionCapitacionDto autorizacionCapitacionDto) {
		
		try {
			if (autorizacionCapitacionDto != null) {
				ErrorMessage mensajeErrorGeneral = new ErrorMessage(
						"errors.autorizacion.messageGeneral");
				autorizacionCapitacionDto.setProcesoExitoso(false);
				autorizacionCapitacionDto.setVerificarDetalleError(true);
				autorizacionCapitacionDto.setMensajeErrorGeneral(mensajeErrorGeneral);
				
				for (OrdenAutorizacionDto ordenAutorizar : 
						autorizacionCapitacionDto.getOrdenesAutorizar()) {
					
					for (ServicioAutorizacionOrdenDto servicioAutorizar : 
							ordenAutorizar.getServiciosPorAutorizar()) {
						servicioAutorizar.setAutorizar(false);
					}
					
					for (MedicamentoInsumoAutorizacionOrdenDto medicamentoAutorizar : 
							ordenAutorizar.getMedicamentosInsumosPorAutorizar()) {
						medicamentoAutorizar.setAutorizar(false);
					}
				}
			}
		} catch (Exception e) {
			//Si fallo en la marcacion de los servicios o medicamentos no se debe 
			//revisar el detalle
			autorizacionCapitacionDto.setVerificarDetalleError(false);
		}
		
	}
	
	@Override
	public void actualizarNivelAutorizacionAutoEntSubServicio(
			AutorizacionesEntSubServi autorizacionesEntSubServi,NivelAutorizacion nivelAutorizacion,boolean requiereTransaccion)
			throws IPSException {
		AutorizacionesDelegate autorizacionesDelegate = null;
		try {
			autorizacionesDelegate=new AutorizacionesDelegate();
			
			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}
			
			List<AutorizacionesEntSubServi> listaAutorizacionesEntSubServiTemp= autorizacionesDelegate.obtenerAutorizacionEntSubServicio(
					autorizacionesEntSubServi.getAutorizacionesEntidadesSub().getConsecutivo(), 
					autorizacionesEntSubServi.getServicios().getCodigo());
			for(AutorizacionesEntSubServi autorizacionesEntSubServiTemp:listaAutorizacionesEntSubServiTemp){
				autorizacionesEntSubServiTemp.setNivelAutorizacion(nivelAutorizacion);
				autorizacionesEntSubServiTemp.setValorTarifa(autorizacionesEntSubServi.getValorTarifa());
				
				autorizacionesDelegate.generarAutoEntSubServicios(autorizacionesEntSubServiTemp);
			}
			
			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}
		} catch (IPSException ipsme) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		} catch (Exception e) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	@Override
	public void actualizarNivelAutorizacionAutoEntSubArticulo(
			AutorizacionesEntSubArticu autorizacionesEntSubArticu,NivelAutorizacion nivelAutorizacion,boolean requiereTransaccion)
			throws IPSException {
		AutorizacionesDelegate autorizacionesDelegate = null;
		try {
			autorizacionesDelegate=new AutorizacionesDelegate();
			
			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}
			
			List<AutorizacionesEntSubArticu> listaAutorizacionesEntSubArticuloTemp= autorizacionesDelegate.obtenerAutorizacionEntSubArticulo(
					autorizacionesEntSubArticu.getAutorizacionesEntidadesSub().getConsecutivo(), 
					autorizacionesEntSubArticu.getArticulo().getCodigo());
			for(AutorizacionesEntSubArticu autorizacionesEntSubArticuloTemp:listaAutorizacionesEntSubArticuloTemp){
				autorizacionesEntSubArticuloTemp.setNivelAutorizacion(nivelAutorizacion);
				autorizacionesEntSubArticuloTemp.setValorTarifa(autorizacionesEntSubArticu.getValorTarifa());
				
				autorizacionesDelegate.generarAutoEntSubArticulos(autorizacionesEntSubArticuloTemp);
			}
			
			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}
		} catch (IPSException ipsme) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		} catch (Exception e) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}

	@Override
	public List<AutorizacionesEntidadesSub> obtenerAutorizacionEntSubDeIngEstancia(
			long idAutoIngEst, boolean requiereTransaccion) throws IPSException {
		AutorizacionesDelegate autorizacionesDelegate = null;
		
		List<AutorizacionesEntidadesSub> autorizaciones=null;
		try {
			autorizacionesDelegate=new AutorizacionesDelegate();
			
			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}
			
			autorizaciones= autorizacionesDelegate.obtenerAutorizacionEntSubDeIngEstancia(idAutoIngEst);
			
			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}
		} catch (IPSException ipsme) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		} catch (Exception e) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		return autorizaciones;
	}
	
	/**
	 * Método que obtiene los mensajes de error despúes de ejecutar el proceso de autorización
	 * 
	 * @param listDtoAutorizacion
	 * @param errores
	 */
	public void obtenerMensajesError(List<AutorizacionCapitacionDto> listDtoAutorizacion, ActionMessages errores) throws IPSException {
		ActionErrors errorTempEncabezado = null;
		ActionErrors erroresTempDetalle = null;
		boolean mostrarMjeAutorizadoAlgunosServArt = false;
		boolean mostrarMjeProcesoExitoso = false;
		boolean mostrarMjeNoAutorizacion = false;
		
		try {
			errorTempEncabezado = new ActionErrors();
			erroresTempDetalle = new ActionErrors();
			//hermorhu - MT6032
			//Cambio en funcionalidad de obtenerMensajes para cumplir con cambios MT5155 en DCU1106 v1.13 
			//para el caso cuando existe mas de un AutorizacionCapitacionDto
			for(AutorizacionCapitacionDto dtoAutorizacion : listDtoAutorizacion) {
			
				//Se valida si existe un mensaje de error generar de la autorización
				if(dtoAutorizacion.getMensajeErrorGeneral() != null) {
					if(dtoAutorizacion.getMensajeErrorGeneral().getParamsMsg() != null
							&& dtoAutorizacion.getMensajeErrorGeneral().getParamsMsg().length > 0){
						errores.add("", new ActionMessage(dtoAutorizacion.getMensajeErrorGeneral().getErrorKey(), dtoAutorizacion.getMensajeErrorGeneral().getParamsMsg()));
					}
					else{
						errores.add("", new ActionMessage(dtoAutorizacion.getMensajeErrorGeneral().getErrorKey()));
					}
				}
				
				//Se valida si se debe verificar el detalle de los Servicios/Articulos para mostrar el encabezado de los mensajes
				if (dtoAutorizacion.isVerificarDetalleError() &&  dtoAutorizacion.isProcesoExitoso()){
					if((!mostrarMjeAutorizadoAlgunosServArt && !mostrarMjeProcesoExitoso && !mostrarMjeNoAutorizacion)
							|| (!mostrarMjeAutorizadoAlgunosServArt && (mostrarMjeProcesoExitoso || mostrarMjeNoAutorizacion))) {
						errorTempEncabezado.clear();
						ErrorMessage mensajeErrorGeneral1 = new ErrorMessage("errors.autorizacion.encabezadoMensajeAutorizadoAlgunosServArt");
						dtoAutorizacion.setMensajeErrorGeneral(mensajeErrorGeneral1);
						errorTempEncabezado.add("", new ActionMessage(dtoAutorizacion.getMensajeErrorGeneral().getErrorKey()));
						ErrorMessage mensajeErrorGeneral2 = new ErrorMessage("errors.autorizacion.encabezadoMensajeNoAutorizadoAlgunosServArt");
						dtoAutorizacion.setMensajeErrorGeneral(mensajeErrorGeneral2);
						errorTempEncabezado.add("", new ActionMessage(dtoAutorizacion.getMensajeErrorGeneral().getErrorKey()));
					}
					
					mostrarMjeAutorizadoAlgunosServArt = true;
				}
				else if(dtoAutorizacion.isVerificarDetalleError() && !dtoAutorizacion.isProcesoExitoso()){
					if(!mostrarMjeNoAutorizacion && !mostrarMjeProcesoExitoso && !mostrarMjeAutorizadoAlgunosServArt) {
						ErrorMessage mensajeErrorGeneral = new ErrorMessage("errors.autorizacion.messageGeneral");
						dtoAutorizacion.setMensajeErrorGeneral(mensajeErrorGeneral);
						errorTempEncabezado.add("", new ActionMessage(dtoAutorizacion.getMensajeErrorGeneral().getErrorKey()));
					}
					
					if(mostrarMjeProcesoExitoso && !mostrarMjeAutorizadoAlgunosServArt) {
						errorTempEncabezado.clear();
						ErrorMessage mensajeErrorGeneral1 = new ErrorMessage("errors.autorizacion.encabezadoMensajeAutorizadoAlgunosServArt");
						dtoAutorizacion.setMensajeErrorGeneral(mensajeErrorGeneral1);
						errorTempEncabezado.add("", new ActionMessage(dtoAutorizacion.getMensajeErrorGeneral().getErrorKey()));
						ErrorMessage mensajeErrorGeneral2 = new ErrorMessage("errors.autorizacion.encabezadoMensajeNoAutorizadoAlgunosServArt");
						dtoAutorizacion.setMensajeErrorGeneral(mensajeErrorGeneral2);
						errorTempEncabezado.add("", new ActionMessage(dtoAutorizacion.getMensajeErrorGeneral().getErrorKey()));
					}
				
					mostrarMjeNoAutorizacion = true;
				}
				//MT 5155 DCU 1106 V1.13
				else if(!dtoAutorizacion.isVerificarDetalleError() && dtoAutorizacion.isProcesoExitoso()){
					if(!mostrarMjeProcesoExitoso && !mostrarMjeNoAutorizacion && !mostrarMjeAutorizadoAlgunosServArt) {
						ErrorMessage mensajeErrorGeneral = new ErrorMessage("errors.autorizacion.procesoExitosoAutorizacion");
						dtoAutorizacion.setMensajeErrorGeneral(mensajeErrorGeneral);
						errorTempEncabezado.add("", new ActionMessage(dtoAutorizacion.getMensajeErrorGeneral().getErrorKey()));
					}
					
					if(mostrarMjeNoAutorizacion && !mostrarMjeAutorizadoAlgunosServArt) {
						errorTempEncabezado.clear();
						ErrorMessage mensajeErrorGeneral1 = new ErrorMessage("errors.autorizacion.encabezadoMensajeAutorizadoAlgunosServArt");
						dtoAutorizacion.setMensajeErrorGeneral(mensajeErrorGeneral1);
						errorTempEncabezado.add("", new ActionMessage(dtoAutorizacion.getMensajeErrorGeneral().getErrorKey()));
						ErrorMessage mensajeErrorGeneral2 = new ErrorMessage("errors.autorizacion.encabezadoMensajeNoAutorizadoAlgunosServArt");
						dtoAutorizacion.setMensajeErrorGeneral(mensajeErrorGeneral2);
						errorTempEncabezado.add("", new ActionMessage(dtoAutorizacion.getMensajeErrorGeneral().getErrorKey()));
					}
				 
					mostrarMjeProcesoExitoso = true;
				}
				
				//Adiciono los mensajes de error para los servicios/articulos
				if(dtoAutorizacion.isVerificarDetalleError()) {
					for(OrdenAutorizacionDto dtoOrden:dtoAutorizacion.getOrdenesAutorizar()){
						if(dtoOrden.getServiciosPorAutorizar() != null
								&& !dtoOrden.getServiciosPorAutorizar().isEmpty()){
							for(ServicioAutorizacionOrdenDto dtoServicio:dtoOrden.getServiciosPorAutorizar()){
								if(!dtoServicio.isAutorizar() || !dtoServicio.isAutorizado()){
									if(dtoServicio.getMensajeError().getParamsMsg() != null 
											&& dtoServicio.getMensajeError().getParamsMsg().length > 0){
										erroresTempDetalle.add("", new ActionMessage(dtoServicio.getMensajeError().getErrorKey(),dtoServicio.getMensajeError().getParamsMsg()));
									}
									else{
										erroresTempDetalle.add("", new ActionMessage(dtoServicio.getMensajeError().getErrorKey()));
									}
								}
							}
						}
						else if(dtoOrden.getMedicamentosInsumosPorAutorizar() != null
								&& !dtoOrden.getMedicamentosInsumosPorAutorizar().isEmpty()){
							for(MedicamentoInsumoAutorizacionOrdenDto dtoMedicamento:dtoOrden.getMedicamentosInsumosPorAutorizar()){
								if(!dtoMedicamento.isAutorizar() || !dtoMedicamento.isAutorizado()){
									if(dtoMedicamento.getMensajeError().getParamsMsg() != null
											&& dtoMedicamento.getMensajeError().getParamsMsg().length > 0){
										erroresTempDetalle.add("", new ActionMessage(dtoMedicamento.getMensajeError().getErrorKey(), dtoMedicamento.getMensajeError().getParamsMsg()));
									}
									else{
										erroresTempDetalle.add("", new ActionMessage(dtoMedicamento.getMensajeError().getErrorKey()));
									}
								}
							}
						}
					}
				}
			}
			
			errores.add(errorTempEncabezado);
			errores.add(erroresTempDetalle);
		} catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#tieneCoberturaMedicamentoInsumo(int, int, int)
	 */
	@Override
	public boolean tieneCoberturaMedicamentoInsumo(int claseOrden,int codigoConvenio, Long codigoOrden, int codigoMedicamentoInsumo)
								throws IPSException {
		boolean cubierto=false; 
		try{
			HibernateUtil.beginTransaction();
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			cubierto=autorizacionesDelegate.tieneCoberturaMedicamentoInsumo(claseOrden,	codigoConvenio, codigoOrden, codigoMedicamentoInsumo);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return cubierto;
	}
	
	
	/**
	 * Servicio que permite obtener las autorizaciones de capitación y entidad subcontratada asociadas a las 
	 * ordenes medicas, ordenes ambulatorias y peticiones. 
	 * 
	 * @param claseOrden
	 * @param tipoOrden
	 * @param codigoOrden
	 * @param Estado
	 * @return
	 * @throws IPSException
	 * @author DiaRuiPe
	 */
	@Override
	public List<AutorizacionPorOrdenDto> obtenerAutorizacionCapitacion(
			int claseOrden, int tipoOrden, Long codigoOrden, List<String> estadosAutorizacion)
			throws IPSException {
		List<AutorizacionPorOrdenDto> listaAutorizacionesOrden=null; 
		try{
			HibernateUtil.beginTransaction();
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			if(claseOrden == ConstantesBD.claseOrdenOrdenMedica 
					|| claseOrden == ConstantesBD.claseOrdenCargoDirecto){
				listaAutorizacionesOrden=autorizacionesDelegate.existeAutorizacionCapitaSolicitud(codigoOrden.intValue(), estadosAutorizacion);
			}
			else if(claseOrden == ConstantesBD.claseOrdenOrdenAmbulatoria){
				listaAutorizacionesOrden=autorizacionesDelegate.existeAutorizacionCapitaOrdenAmbulatoria(codigoOrden, estadosAutorizacion);
			}
			else if(claseOrden == ConstantesBD.claseOrdenPeticion){
				listaAutorizacionesOrden=autorizacionesDelegate.existeAutorizacionCapitaPeticion(codigoOrden.intValue(), estadosAutorizacion);
			}
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return listaAutorizacionesOrden;
	}
	
	/**
	 * Servicio que permite realizar la anulación  y recalcular el cierre temporal
	 * para las ordenes medicas, ordenes ambulatorias y peticiones que se esten modificando, 
	 * las cuales tengan una autorización asociada. 
	 * 
	 * @param claseOrden
	 * @param tipoOrden
	 * @param autorizacionesPorOrdenDto
	 * @return
	 * @throws IPSException
	 * @author DiaRuiPe
	 */
	
	@Override
	public boolean procesoAnulacionAutorizacion(int claseOrden, int tipoOrden,List<AutorizacionPorOrdenDto> autorizacionesPorOrdenDto,
			AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto, int codInstitucion)	throws IPSException {
		
		ICierrePresupuestoMundo cierrePresupuestoMundo	= null;
		boolean procesoExitodo=false;
		boolean esServicios=false;
		try{
			
			HibernateUtil.beginTransaction();
			
			if (tipoOrden != ConstantesBD.codigoTipoSolicitudMedicamentos 
					&& tipoOrden != ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos){
				esServicios = true;
			}

			for (AutorizacionPorOrdenDto autorizacionPorOrden : autorizacionesPorOrdenDto) {
				//Anular la autorización
				anularAutorizacionCapitacion(anulacionAutorizacionDto,autorizacionPorOrden, codInstitucion);
				
				//Se valida si el mes/año de la autorización corresponde con el mes/año actual
				//para permitir recalcular el cierre temporal
				Calendar mesAnioActual= Calendar.getInstance();
				Calendar mesAnioAutorizacion=Calendar.getInstance();
				mesAnioAutorizacion.setTime(autorizacionPorOrden.getFechaAutorizacionCapitacion());
				boolean calcularCierreAnulacion=false;
				if(mesAnioActual.get(Calendar.MONTH) == mesAnioAutorizacion.get(Calendar.MONTH)
						&& mesAnioActual.get(Calendar.YEAR) == mesAnioAutorizacion.get(Calendar.YEAR)){
					calcularCierreAnulacion=true;
				}
				if(calcularCierreAnulacion){
				//Restar el valor de la autorización en la valorización Capitación Subcontratada en la temporal del Cierre del Presupuesto.
				cierrePresupuestoMundo	= new CierrePresupuestoMundo();
				cierrePresupuestoMundo.recalcularCierreTemporalPresupuesto(anulacionAutorizacionDto, 
						autorizacionPorOrden, codInstitucion, esServicios);
			}
		}
			
			procesoExitodo=true;
			
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
		return procesoExitodo;
	}
	
	
	/**
	 * Sevicio que permite identificar si una Solicitud se encuentra asociada a una Orden Ambulatoria
	 * y retorna el consecutivo de la Orden Ambulatoria Asociada.
	 * 
	 * @param codOrden
	 * @return
	 * @throws IPSException
	 * @author DiaRuiPe
	 */
	@Override
	public String existeOrdenAmbAsociadaSolicitud(int codidoSolicitud)
			throws IPSException {
		
		String  consecutivoOrdenAmb=null;
		try{
			HibernateUtil.beginTransaction();
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			consecutivoOrdenAmb=autorizacionesDelegate.existeOrdenAmbAsociadaSolicitud(codidoSolicitud);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
		return consecutivoOrdenAmb;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#consultarServiciosAnuladosCapitacion(long, long, boolean)
	 */
	public List<ServicioAutorizadoCapitacionDto> consultarServiciosAnuladosCapitacion(long consecutivo, long tarifario, boolean isMigrado) throws IPSException {
		AutorizacionesDelegate delegate = null;
		List<ServicioAutorizadoCapitacionDto> listaServicios = null;
		try{
			HibernateUtil.beginTransaction();
			delegate = new AutorizacionesDelegate();
			listaServicios = delegate.consultarServiciosAnuladosCapitacion(consecutivo, tarifario,isMigrado);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		return listaServicios;
	}
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#consultarArticulosAnuladosCapitacion(long, int, boolean)
	 */
	public List<ArticuloAutorizadoCapitacionDto> consultarArticulosAnuladosCapitacion(long consecutivo, int institucion, boolean isMigrado) throws IPSException {
		AutorizacionesDelegate delegate = null;
		List<ArticuloAutorizadoCapitacionDto> listaArticulos = null;
		try{
			HibernateUtil.beginTransaction();
			delegate = new AutorizacionesDelegate();
			listaArticulos = delegate.consultarArticulosAnuladosCapitacion(consecutivo, institucion, isMigrado);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		return listaArticulos;
	}
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#existeAutorizacionCapitacionGeneradaPorPeticion(int)
	 */
	@Override
	public boolean existeAutorizacionCapitacionGeneradaPorPeticion(int numeroSolicitud) throws IPSException {
		boolean existe=true;
		try{
			HibernateUtil.beginTransaction();
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			existe=autorizacionesDelegate.existeAutorizacionCapitacionGeneradaPorPeticion(numeroSolicitud);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
		return existe;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#consultarEntregaAutorizacionEntidadSubContratada(long)
	 */
	@Override
	public AutorizacionEntregaDto consultarEntregaAutorizacionEntidadSubContratada(long idAutorizacionEntidadSub) throws IPSException {
		AutorizacionEntregaDto autorizacionEntregadaDto = null;
		try{
			HibernateUtil.beginTransaction();
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			autorizacionEntregadaDto = autorizacionesDelegate.consultarEntregaAutorizacionEntidadSubContratada(idAutorizacionEntidadSub);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return autorizacionEntregadaDto;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#guardarEntregaAutorizacionOriginal(com.servinte.axioma.dto.manejoPaciente.AutorizacionEntregaDto)
	 */
	@Override
	public boolean guardarEntregaAutorizacionEntidadSubContratadaOriginal(AutorizacionEntregaDto autorizacionEntregaDto) throws IPSException {
		boolean resultado = false;
		try{
			HibernateUtil.beginTransaction();
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			resultado = autorizacionesDelegate.guardarEntregaAutorizacionEntidadSubContratadaOriginal(autorizacionEntregaDto);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
		return resultado;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo#consultarIdAutorizacionEntidadSubXConsecutivoAutorizacion(java.lang.String)
	 */
	@Override
	public Long consultarIdAutorizacionEntidadSubXConsecutivoAutorizacion(String consecutivoAutorizacion) throws IPSException {
		Long idAutorizacionEntSub = null;
		try{
			HibernateUtil.beginTransaction();
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			idAutorizacionEntSub = autorizacionesDelegate.consultarIdAutorizacionEntidadSubXConsecutivoAutorizacion(consecutivoAutorizacion);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return idAutorizacionEntSub;
	}
	/** 
	 * 
	 * MT6703
	 */
	@Override
	public Boolean consultarSiPacientePagaAtencion(int codigoContrato, boolean requiereTransaccion) throws IPSException {
		
		 Boolean isPacientePagaAtención=false;
		try{
			if(requiereTransaccion)
			{
				HibernateUtil.beginTransaction();
			}
			
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			isPacientePagaAtención = autorizacionesDelegate.consultarSiPacientePagaAtencion(codigoContrato );

			if(requiereTransaccion)
				{
					HibernateUtil.endTransaction();
				}
			
		}
		catch (IPSException ipsme) {

			if(requiereTransaccion)
				{
					HibernateUtil.abortTransaction();		
				}
			
			throw ipsme;
		}
		catch (Exception e) {

		if(requiereTransaccion)
			{
				HibernateUtil.abortTransaction();	
			}
			
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return isPacientePagaAtención;
	}
	
	
	/** 
	 * 
	 * MT6703
	 */
	@Override
	public Boolean consultarSiConvenioManejaMontos(int codigoConvenio) throws IPSException {
		
		 Boolean isPacientePagaAtención=false;
		try{
			HibernateUtil.beginTransaction();
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			isPacientePagaAtención = autorizacionesDelegate.consultarSiConvenioManejaMontos(codigoConvenio);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return isPacientePagaAtención;
	}


}