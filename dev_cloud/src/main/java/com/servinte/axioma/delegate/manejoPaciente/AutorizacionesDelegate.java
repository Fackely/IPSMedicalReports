package com.servinte.axioma.delegate.manejoPaciente;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;
import com.servinte.axioma.dto.facturacion.MontoCobroDto;
import com.servinte.axioma.dto.facturacion.ParametroBusquedaMontoDto;
import com.servinte.axioma.dto.manejoPaciente.ArticuloAutorizadoCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionEntregaDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionPorOrdenDto;
import com.servinte.axioma.dto.manejoPaciente.ServicioAutorizadoCapitacionDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.AutoCapiXCentroCosto;
import com.servinte.axioma.orm.AutoEntsubOrdenambula;
import com.servinte.axioma.orm.AutoEntsubPeticiones;
import com.servinte.axioma.orm.AutoEntsubSolicitudes;
import com.servinte.axioma.orm.AutorizacionesCapitacionSub;
import com.servinte.axioma.orm.AutorizacionesEntSubArticu;
import com.servinte.axioma.orm.AutorizacionesEntSubMontos;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.AutorizacionesEstanciaCapita;
import com.servinte.axioma.orm.EntregaAutorizacion;
import com.servinte.axioma.orm.HistoAutorizacionCapitaSub;
import com.servinte.axioma.orm.OrdenesAmbulatorias;
import com.servinte.axioma.orm.OrdenesAmbulatoriasPosponer;
import com.servinte.axioma.orm.PeticionQx;
import com.servinte.axioma.orm.PeticionesPosponer;
import com.servinte.axioma.orm.Solicitudes;
import com.servinte.axioma.orm.SolicitudesPosponer;
import com.servinte.axioma.orm.Usuarios;

/**
 * Clase de implementa los metodos de integración con la base de datos
 * asociados a Autorizaciones
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:23:59 p.m.
 */
public class AutorizacionesDelegate {

	
	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	
	/**
	 * Método que obtiene las ordenes pendientes por autorizar de un paciente
	 * incluye Ordenes Médicas, Solicitudes de Cargos Directos, Ordenes Ambulatorias
	 * y Peticiones de Cirugía
	 * 
	 * @param codigoPaciente
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<OrdenAutorizacionDto> obtenerOrdenesPorAutorizarPorPaciente(int codigoPaciente) throws BDException{
		try{
			List<OrdenAutorizacionDto> ordenesPorPaciente=new ArrayList<OrdenAutorizacionDto>();
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoPaciente", codigoPaciente);
			parameters.put("ordenAmbulatoria", ConstantesBD.claseOrdenOrdenAmbulatoria);
			parameters.put("peticion", ConstantesBD.claseOrdenPeticion);
			parameters.put("cargoDirecto", ConstantesBD.claseOrdenCargoDirecto);
			parameters.put("ordenMedica", ConstantesBD.claseOrdenOrdenMedica);
			parameters.put("descAmbulatoria", ConstantesBD.claseOrdenOrdenAmbulatoriaDesc);
			parameters.put("descPeticion", ConstantesBD.claseOrdenPeticionDesc);
			parameters.put("descMedica", ConstantesBD.claseOrdenOrdenMedicaDesc);
			if(ValoresPorDefecto.getValorTrueParaConsultas().equals("1")){
				parameters.put("valorTrueConsulta", Integer.valueOf(1));
			}
			else{
				parameters.put("valorTrueConsulta", true);
			}
			parameters.put("tipoOrdenAmbArticulo", ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos);
			parameters.put("tipoOrdenAmbServicio", ConstantesBD.codigoTipoOrdenAmbulatoriaServicios);
			parameters.put("estadoOrdenAmbPendiente", ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente);
			parameters.put("cubierto", ConstantesBD.acronimoSi);
			parameters.put("tipoContrato", ConstantesBD.codigoTipoContratoCapitado);
			parameters.put("capitacionSubcontradada", ConstantesBD.acronimoSiChar);
			parameters.put("estadoSolHCAnulada", ConstantesBD.codigoEstadoHCAnulada);
			parameters.put("estadoSolHCCargoDirecto", ConstantesBD.codigoEstadoHCCargoDirecto);
			parameters.put("estadoSolHCSolicitada", ConstantesBD.codigoEstadoHCSolicitada);
			parameters.put("estadoAutorizacionAnulada", ConstantesIntegridadDominio.acronimoEstadoAnulado);
			List<Integer> estadosPeticiones= new ArrayList<Integer>();
			estadosPeticiones.add(ConstantesBD.codigoEstadoPeticionPendiente);
			estadosPeticiones.add(ConstantesBD.codigoEstadoPeticionProgramada);
			estadosPeticiones.add(ConstantesBD.codigoEstadoPeticionReprogramada);
			parameters.put("estadosPeticion", estadosPeticiones);
			parameters.put("tipoSolicitudCargoDirectoArticulo", ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos);
			parameters.put("tipoSolicitudCargoDirectoServicio", ConstantesBD.codigoTipoSolicitudCargosDirectosServicios);
			parameters.put("tipoSolicitudCirugia", ConstantesBD.codigoTipoSolicitudCirugia);
			parameters.put("tipoSolicitudMedicamento", ConstantesBD.codigoTipoSolicitudMedicamentos);
			parameters.put("tipoSolicitudProcedimiento", ConstantesBD.codigoTipoSolicitudProcedimiento);
			parameters.put("tipoSolicitudInterconsulta", ConstantesBD.codigoTipoSolicitudInterconsulta);
			parameters.put("tipoSolicitudConsulta", ConstantesBD.codigoTipoSolicitudCita);
			parameters.put("estadoCargoCargado", ConstantesBD.codigoEstadoFCargada);
			parameters.put("estadoAutorizacionAutorizado", ConstantesIntegridadDominio.acronimoAutorizado);
			List<Object[]> ordenesPorAutorizar=(List<Object[]>)persistenciaSvc
									.createNamedQuery("autorizacionCapitacion.obtenerOrdenesPorAutorizarPorPaciente", parameters);
			if(ordenesPorAutorizar != null && !ordenesPorAutorizar.isEmpty()){
				for(Object[] orden:ordenesPorAutorizar){
					OrdenAutorizacionDto ordenDto= new OrdenAutorizacionDto((Long)orden[0],
							(String)orden[1], (Integer)orden[2],(Integer)orden[3],(Date)orden[4],
							(String)orden[5], (Integer)orden[6], (String)orden[7], (String)orden[8],
							(Integer)orden[9], (String)orden[10], (Date)orden[11], (String)orden[12]);
					ordenesPorPaciente.add(ordenDto);
				}
			}
			return ordenesPorPaciente;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método que obtiene la via de ingreso asociada a la ultima cuenta del ingreso de la orden médica
	 * 
	 * @param codigoIngreso
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	public int obtenerViaIngresoPorIngreso(int codigoIngreso) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoIngreso", codigoIngreso);
			return (Integer) persistenciaSvc.createNamedQueryFirstResult("autorizacionCapitacion.obtenerViaIngresoPorIngreso", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	
	
	/**
	 * Método que permite guardar la autorización de Entidad Subcontratada generada por la capita
	 * @param autorizacionEntSubCapita
	 * @throws BDException
	 * @author DiaRuiPe
	 */
	public AutorizacionesEntidadesSub generarAutorizacionEntidadSub(AutorizacionesEntidadesSub autorizacionesEntidadesSub) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			persistenciaSvc.persist(autorizacionesEntidadesSub);
			return autorizacionesEntidadesSub;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Método que se encarga de guardar las relaciones entre solicitudes y autorizaciones
	 * @param autoEntsubSolicitudes
	 * @throws BDException
	 * @author DiaRuiPe
	 */
	public void generarAutoEntSubSolicitudes(AutoEntsubSolicitudes autoEntsubSolicitudes)throws BDException
	{
		try{
			persistenciaSvc= new PersistenciaSvc();
			persistenciaSvc.persist(autoEntsubSolicitudes);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Método que permite asociar la autorización de capitación subcontratada a la orden ambulatoria
	 * @param autorizacionesEntidadesSub
	 * @param autorizacionCapitacion
	 * @throws BDException 
	 * @author DiaRuiPe
	 */
	public void generarAutorizacionAmbulatoria(AutoEntsubOrdenambula autoEntSubOrdenAmbula) throws BDException{
			try{
				persistenciaSvc= new PersistenciaSvc();
				persistenciaSvc.persist(autoEntSubOrdenAmbula);
			}catch (Exception e) {
				Log4JManager.error(e);
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
			}
	}
	
	/**
	 * Método que permite asociar la autorización de capitación subcontratada a la Petición
	 * @param autorizacionCapitacion
	 * @param autorizacionesEntidadesSub
	 * @throws BDException
	 * @author DiaRuiPe 
	 */
	public void generarAutorizacionPeticion(AutoEntsubPeticiones autoEntSubPeticion) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			persistenciaSvc.persist(autoEntSubPeticion);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	
	/**
	 * Método que permite generar la autorización de capitación subcontratada
	 * @param autorizacionesCapitacionSub
	 * @throws BDException
	 * @author DiaRuiPe
	 */
	
	public AutorizacionesCapitacionSub generarAutorizacionCapitacion(AutorizacionesCapitacionSub autorizacionesCapitacionSub) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			persistenciaSvc.persist(autorizacionesCapitacionSub);
			return autorizacionesCapitacionSub;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	
	/**
	 * Método que permite generar el detalle de la autorización de Entidad subcontratada de 
	 * servicios por la capitación.
	 * @param AutorizacionesEntSubServi
	 * @throws BDException
	 * @author DiaRuiPe
	 */
	public void generarAutoEntSubServicios(AutorizacionesEntSubServi autoEntSubServicios) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			persistenciaSvc.persist(autoEntSubServicios);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Método que permite generar el detalle de la autorización de Entidad subcontratada de 
	 * Articulos por la capitación.
	 * @param AutorizacionesEntSubArticu
	 * @throws BDException
	 * @author DiaRuiPe
	 */
	public void generarAutoEntSubArticulos(AutorizacionesEntSubArticu autoEntSubArticulos) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			persistenciaSvc.persist(autoEntSubArticulos);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}

	/**
	 * Método que permite guardar el calculo de los montos de cobro generados por la autorización de capitación subcontratada
	 * @param AutorizacionesEntSubMontos
	 * @throws BDException 
	 * @author DiaRuiPe
	 */
	public void generarAutorizacionesEntSubMontos(AutorizacionesEntSubMontos autorizacionEntSubMonto) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			persistenciaSvc.persist(autorizacionEntSubMonto);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}

	/**
	 * Método que permite guardar el centro de costo que ejecuta la o las ordenes autorizadas
	 * @param centroCostoResponde
	 * @param autoCapiXCentroCosto
	 * @throws BDException 
	 * @author DiaRuiPe
	 */
	public void generarAutoCapiXCentroCosto(AutoCapiXCentroCosto autoCapiXCentroCosto) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			persistenciaSvc.persist(autoCapiXCentroCosto);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}

	/**
	 * Método que permite guardar el historico de la autorización de entidad subcontratada generada por la 
	 * capitación subcontratada
	 * @param HistoAutorizacionCapitaSub  
	 * @throws BDException 
	 * @author DiaRuiPe
	 */
	public void  generarHistorialAutorCapitacionSub(HistoAutorizacionCapitaSub hisAutoCapiSub) throws BDException{
		
		try{
			persistenciaSvc= new PersistenciaSvc();
			persistenciaSvc.persist(hisAutoCapiSub);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}

	/**
	 * Servicio encargado de obtener las entidades subcontratadas que correspondan
	 * a las prioridades de los niveles de autorización del usuario
	 * y que esten activas y tengan un contrato vigente
	 * 
	 * @param codigoCentroCosto
	 * @param prioridades
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<EntidadSubContratadaDto> obtenerEntidadesSubContratadasExternas(int codigoCentroCosto, List<Integer> prioridades) throws BDException{
		List<EntidadSubContratadaDto> listaEntidades = null;
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("codigoCentroCosto", codigoCentroCosto);
			params.put("prioridades", prioridades);
			params.put("activo", ConstantesBD.acronimoSi);
			listaEntidades=(List<EntidadSubContratadaDto>)persistenciaSvc.createNamedQuery("autorizacionCapitacion.obtenerEntidadesSubContratadasExternas", params);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		return listaEntidades;
	}

	/**
	 * 
	 * @param parametrosBusqueda
	 */
	public List<MontoCobroDto> obtenerMontosCobroParametrica(ParametroBusquedaMontoDto parametrosBusqueda){
		return null;
	}
	
	/**
	 * Método que se encarga de obtener la Autorizacion De Entidad Subcontratada
	 * @param consecutivo
	 * @return AutorizacionesEntidadesSub
	 * @throws BDException
	 */
	public AutorizacionesEntidadesSub obtenerAutorizacionEntidadSub(long consecutivo)throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			return persistenciaSvc.find(AutorizacionesEntidadesSub.class, consecutivo);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Método que se encarga de actualizar la Autorizacion De Entidad Subcontratada
	 * @param autorizacionesEntidadesSub
	 * @throws BDException
	 */
	public void actualizarAutorizacionEntidadSub(AutorizacionesEntidadesSub autorizacionesEntidadesSub)throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			persistenciaSvc.merge(autorizacionesEntidadesSub);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	
	/**
	 * Método encargado de guardar el registro de posponer de las ordenes seleccionadas
	 * en dicha funcionalidad
	 * 
	 * @param ordenesPosponer
	 * @param loginUsuario
	 * @param fechaPosponer
	 * @param observaciones
	 * @throws BDException
	 * @author ricruico
	 */
	public void posponerOrdenes(List<OrdenAutorizacionDto> ordenesPosponer, String loginUsuario, 
			Date fechaActual, String horaActual, Date fechaPosponer, String observaciones)throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			for(OrdenAutorizacionDto dtoOrden:ordenesPosponer){
				if(dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenOrdenMedica
						|| dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenCargoDirecto){
					SolicitudesPosponer solPosponer = new SolicitudesPosponer();
					Solicitudes solicitudes = new Solicitudes();
					solicitudes.setNumeroSolicitud(dtoOrden.getCodigoOrden().intValue());
					Usuarios usuarios = new Usuarios();
					usuarios.setLogin(loginUsuario);
					solPosponer.setSolicitudes(solicitudes);
					solPosponer.setUsuarios(usuarios);
					solPosponer.setFechaModifica(fechaActual);
					solPosponer.setFechaPosponer(fechaPosponer);
					solPosponer.setHoraModifica(horaActual);
					solPosponer.setObservacion(observaciones);
					persistenciaSvc.persist(solPosponer);
				}
				else if(dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenOrdenAmbulatoria){
					OrdenesAmbulatoriasPosponer ordenAmbPosponer = new OrdenesAmbulatoriasPosponer();
					OrdenesAmbulatorias ordenesAmbulatorias = new OrdenesAmbulatorias();
					ordenesAmbulatorias.setCodigo(dtoOrden.getCodigoOrden());
					Usuarios usuarios = new Usuarios();
					usuarios.setLogin(loginUsuario);
					ordenAmbPosponer.setOrdenesAmbulatorias(ordenesAmbulatorias);
					ordenAmbPosponer.setUsuarios(usuarios);
					ordenAmbPosponer.setFechaModifica(fechaActual);
					ordenAmbPosponer.setFechaPosponer(fechaPosponer);
					ordenAmbPosponer.setHoraModifica(horaActual);
					ordenAmbPosponer.setObservacion(observaciones);
					persistenciaSvc.persist(ordenAmbPosponer);
				}
				else if(dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenPeticion){
					PeticionesPosponer peticionesPosponer = new PeticionesPosponer();
					PeticionQx peticionQx = new PeticionQx();
					peticionQx.setCodigo(dtoOrden.getCodigoOrden().intValue());
					Usuarios usuarios = new Usuarios();
					usuarios.setLogin(loginUsuario);
					peticionesPosponer.setPeticionQx(peticionQx);
					peticionesPosponer.setUsuarios(usuarios);
					peticionesPosponer.setFechaModifica(fechaActual);
					peticionesPosponer.setFechaPosponer(fechaPosponer);
					peticionesPosponer.setHoraModifica(horaActual);
					peticionesPosponer.setObservacion(observaciones);
					persistenciaSvc.persist(peticionesPosponer);
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Método que se encarga de verificar si la Entidad Subcontratada se encuentra activa
	 * y si tiene un contrato vigente
	 * 
	 * @param codigoEntidad
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public EntidadSubContratadaDto verificarEntidadSubContratadaParametrizada(Long codigoEntidad) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("codigoEntidad", codigoEntidad);
			params.put("activo", ConstantesBD.acronimoSi);
			List<Object[]> entidades=(List<Object[]>)persistenciaSvc.createNamedQuery("autorizacionCapitacion.verificarEntidadSubContratadaParametrizada", params);
			if(entidades != null && !entidades.isEmpty()){
				EntidadSubContratadaDto info= new EntidadSubContratadaDto();
				info.setCodEntidadSubcontratada((Long)entidades.get(0)[0]);
				info.setRazonSocial((String)entidades.get(0)[1]);
				info.setDireccionEntidad((String)entidades.get(0)[2]);
				info.setTelefonoEntidad((String)entidades.get(0)[3]);
				info.setCodContratoEntidadSub((Long)entidades.get(0)[4]);
				info.setTipoTarifa((String)entidades.get(0)[5]);
				return info;
			}
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		return null;
	}
	
	/**
	 * Consulta todos los servicios autorizados de capitacion (Solicitud, Orden Ambulatoria o Peticion)
	 * @param autorizacionesEntidadesSub
	 * @throws BDException
	 */
	@SuppressWarnings("unchecked")
	public List<ServicioAutorizadoCapitacionDto> consultarServiciosAutorizadosCapitacion(long consecutivo,long tarifario, boolean isMigrado)throws BDException{
		List<ServicioAutorizadoCapitacionDto>serviciosAutorizados=null;
		
		try{
			serviciosAutorizados=new ArrayList<ServicioAutorizadoCapitacionDto>(0);
			
			persistenciaSvc= new PersistenciaSvc();
			HashMap<String, Object>parameters=new HashMap<String, Object>(0);
			List<Integer>listaTiposSolicitud =new ArrayList<Integer>(0);
			listaTiposSolicitud.add(ConstantesBD.codigoTipoSolicitudInicialUrgencias);
			listaTiposSolicitud.add(ConstantesBD.codigoTipoSolicitudInicialHospitalizacion);
			listaTiposSolicitud.add(ConstantesBD.codigoTipoSolicitudCargosDirectosServicios);
			listaTiposSolicitud.add(ConstantesBD.codigoTipoSolicitudEstancia);
			parameters.put("tipoSolicitud", listaTiposSolicitud);
			parameters.put("autorizacion", consecutivo);
			parameters.put("migrado", isMigrado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			parameters.put("tarifario", tarifario);
			List<Object[]>servicios=(List<Object[]>)persistenciaSvc.createNamedQuery("autorizacionCapitacion.obtenerServiciosAutorizadosCapitacion", parameters);
			for(Object[]servicio:servicios){
				
				final int tipo_autorizacion=0;
				final int codigo=1;
				final int consecutivo_aut=2;
				final int fecha_generacion=3;
				final int id_servicio=4;
				final int especialidad=5;
				final int tipo_servicio=6;
				final int grupo_servicio=7;
				final int cod_serv=8;
				final int nom_ser=9;
				final int nivel_auto=10;
				final int cantidad=11;
				final int acronimo_diag=12;
				final int tipo_cie_diag=13;
				final int diag_descripcion=14;
				final int valor_tarifa=15;
				final int indicativo_temporal=16;
				final int via_ingreso=17;
				final int id_ingreso=18;
				final int codigo_cuenta=19;
				final int tipo_paciente=20;
				final int tipo_solicitud=21;
				final int pyp=22;
				
				ServicioAutorizadoCapitacionDto servicioAutorizadoCapitacionDto=new ServicioAutorizadoCapitacionDto(
						(Long)servicio[tipo_autorizacion], 
						(Long)servicio[codigo],
						(Long)servicio[consecutivo_aut], 
						(Date)servicio[fecha_generacion],
						(Long)servicio[id_servicio],
						(Integer)servicio[especialidad],
						(String)servicio[tipo_servicio],
						(Integer)servicio[grupo_servicio],
						(String)servicio[cod_serv], 
						(String)servicio[nom_ser], 
						(String)servicio[nivel_auto], 
						(Long)servicio[cantidad], 
						(String)servicio[acronimo_diag], 
						(Integer)servicio[tipo_cie_diag],
						(String)servicio[diag_descripcion],
						(Double)servicio[valor_tarifa],
						(String)servicio[indicativo_temporal],
						(Integer)servicio[via_ingreso],
						(Long)servicio[id_ingreso],
						(Long)servicio[codigo_cuenta],
						(String)servicio[tipo_paciente],
						(Integer)servicio[tipo_solicitud],
						(String)servicio[pyp]);
				serviciosAutorizados.add(servicioAutorizadoCapitacionDto);
			}
			
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		return serviciosAutorizados;
	}
	/**
	 * Consulta todos los servicios autorizados de capitacion (Solicitud o Orden Ambulatoria)
	 * @param autorizacionesEntidadesSub
	 * @throws BDException
	 */
	@SuppressWarnings("unchecked")
	public List<ArticuloAutorizadoCapitacionDto> consultarArticulosAutorizadosCapitacion(long consecutivo,int institucion,boolean isMigrado)throws BDException{
		List<ArticuloAutorizadoCapitacionDto>articulosAutorizados=null;
		
		try{
			articulosAutorizados=new ArrayList<ArticuloAutorizadoCapitacionDto>(0);
			
			persistenciaSvc= new PersistenciaSvc();
			HashMap<String, Object>parameters=new HashMap<String, Object>(0);
			parameters.put("autorizacion", consecutivo);
			parameters.put("institucion", institucion);
			parameters.put("migrado",isMigrado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			List<Object[]>articulos=(List<Object[]>)persistenciaSvc.createNamedQuery("autorizacionCapitacion.obtenerArticulosAutorizadosCapitacion", parameters);
			for(Object[]articulo:articulos){
				
				final int tipo_autorizacion=0;
				final int codigo=1;
				final int consecutivo_aut=2;
				final int fecha_generacion=3;
				final int cod_art=4;
				final int nom_art=5;
				final int concentracion=6;
				final int forma_farmaceutica=7;
				final int unidad_medida=8;
				final int nat_art=9;
				final int acronimo_nat_art=10;
				final int codigo_subgrupo=11;
				final int cantidad=12;
				final int acronimo_diag=13;
				final int tipo_cie_diag=14;
				final int diag_descripcion=15;
				final int valor_tarifa=16;
				final int indicativo_temporal=17;
				final int via_ingreso=18;
				final int tipo_solicitud=19;
				final int pyp=20;
				  
				  
				ArticuloAutorizadoCapitacionDto articuloAutorizadoCapitacionDto=new ArticuloAutorizadoCapitacionDto(
						(Long)articulo[tipo_autorizacion], 
						(Long)articulo[codigo],
						(Long)articulo[consecutivo_aut], 
						(Date)articulo[fecha_generacion], 
						(Long)articulo[cod_art], 
						(String)articulo[nom_art],
						(String)articulo[concentracion],
						(String)articulo[forma_farmaceutica],
						(String)articulo[unidad_medida],
						(String)articulo[nat_art],
						(String)articulo[acronimo_nat_art],
						(Integer)articulo[codigo_subgrupo],
						(Long)articulo[cantidad], 
						(String)articulo[acronimo_diag], 
						(Integer)articulo[tipo_cie_diag], 
						(String)articulo[diag_descripcion],
						(Double)articulo[valor_tarifa],
						(String)articulo[indicativo_temporal],
						(Integer)articulo[via_ingreso],
						(Integer)articulo[tipo_solicitud],
						(String)articulo[pyp]);
				articulosAutorizados.add(articuloAutorizadoCapitacionDto);
			}
			
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		return articulosAutorizados;
	}
	
	
	/**
	 * Método que se encarga de obtener si la Solicitud tiene asociada una autorizacion de capitación
	 * 
	 * @param numeroSolicitud
	 * @return List<AutorizacionPorOrdenDto>
	 * @throws BDException
	 */
	@SuppressWarnings("unchecked")
	public List<AutorizacionPorOrdenDto> existeAutorizacionCapitaSolicitud(int numeroSolicitud, List<String> estadosAutorizacion)throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("numeroSolicitud", numeroSolicitud);
			parameters.put("estadosAutorizacion", estadosAutorizacion);
			return (List<AutorizacionPorOrdenDto>) persistenciaSvc.
					createNamedQuery("autorizacionCapitacion.existeAutorizacionCapitaSolicitud", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método que se encarga de obtener si la Orden Ambulatoria tiene asociada una autorizacion de capitación
	 * 
	 * @param codigo
	 * @return List<AutorizacionPorOrdenDto>
	 * @throws BDException
	 */
	@SuppressWarnings("unchecked")
	public List<AutorizacionPorOrdenDto> existeAutorizacionCapitaOrdenAmbulatoria(Long codigo, List<String> estadosAutorizacion)throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigo", codigo);
			parameters.put("estadosAutorizacion", estadosAutorizacion);
			return (List<AutorizacionPorOrdenDto>) persistenciaSvc.
					createNamedQuery("autorizacionCapitacion.existeAutorizacionCapitaOrdenAmbulatoria", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método que se encarga de obtener si la Peticion tiene asociada una autorizacion de capitación
	 * 
	 * @param numeroSolicitud
	 * @return List<AutorizacionPorOrdenDto>
	 * @throws BDException
	 */
	@SuppressWarnings("unchecked")
	public List<AutorizacionPorOrdenDto> existeAutorizacionCapitaPeticion(int codigoPeticion, List<String> estadosAutorizacion)throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoPeticion", codigoPeticion);
			parameters.put("estadosAutorizacion", estadosAutorizacion);
			return (List<AutorizacionPorOrdenDto>) persistenciaSvc.
					createNamedQuery("autorizacionCapitacion.existeAutorizacionCapitaPeticion", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método que se encarga de obtener si existe Solicitudes asociadas a la Autorizacion Capitacion Sub
	 * 
	 * @param consecutivo
	 * @return List<AutorizacionPorOrdenDto>
	 * @throws BDException
	 */
	@SuppressWarnings("unchecked")
	public List<AutorizacionPorOrdenDto> obtenerSolicitudesAsociadasAutorizacion(long consecutivo,int numeroSolicitud)throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("consecutivo", consecutivo);
			parameters.put("numeroSolicitud", numeroSolicitud);
			return (List<AutorizacionPorOrdenDto>) persistenciaSvc.
					createNamedQuery("autorizacionCapitacion.obtenerSolicitudesAsociadasAutorizacion", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método que se encarga de obtener si existe Ordenes Ambulatorias asociadas a la Autorizacion Capitacion Sub
	 * 
	 * @param consecutivo
	 * @return List<AutorizacionPorOrdenDto>
	 * @throws BDException
	 */
	@SuppressWarnings("unchecked")
	public List<AutorizacionPorOrdenDto> obtenerOrdenesAmbuAsociadasAutorizacion(long consecutivo,long codigoOrden)throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("consecutivo", consecutivo);
			parameters.put("codigoOrden", codigoOrden);
			return (List<AutorizacionPorOrdenDto>) persistenciaSvc.
					createNamedQuery("autorizacionCapitacion.obtenerOrdenesAmbuAsociadasAutorizacion", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo encargado de obtener la descripcion del servicio 
	 * y la informacion de la urgencia de atencion de la autorizacion
	 * 
	 * @param codigoServicio
	 * @param codigoTarifarioOficial
	 * @return String[]
	 * DESCRIPCIONSERVICIO = 0;
	 * CODIGOPROPIETARIO   = 1;
	 * NUMDIASURGENTE 	   = 2;
	 * ACRONIMODIASURGENTE = 3;
	 * NUMDIASNORMAL 	   = 4;
	 * ACRONIMODIASNORMAL  = 5;
	 * @throws BDException
	 */
	public String[] obtenerDescripcionServicioAutorizar(int codigoServicio,int codigoTarifarioOficial) throws BDException
	{
		
		String[] descripcionServicio = new String[6]; 
		final int DESCRIPCIONSERVICIO = 0;
		final int CODIGOPROPIETARIO   = 1;
		final int NUMDIASURGENTE 	  = 2;
		final int ACRONIMODIASURGENTE = 3;
		final int NUMDIASNORMAL 	  = 4;
		final int ACRONIMODIASNORMAL  = 5;
		
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoServicio", codigoServicio);
			parameters.put("tarifarioOficial", codigoTarifarioOficial);
			Object[] resultado = (Object[]) persistenciaSvc.
					createNamedQueryUniqueResult("autorizacionCapitacion.obtenerDescripcionServicio", parameters);
			
			descripcionServicio[DESCRIPCIONSERVICIO] = String.valueOf(resultado[DESCRIPCIONSERVICIO]);
			descripcionServicio[CODIGOPROPIETARIO] = String.valueOf(resultado[CODIGOPROPIETARIO]);
			descripcionServicio[NUMDIASURGENTE] = String.valueOf(resultado[NUMDIASURGENTE]);
			descripcionServicio[ACRONIMODIASURGENTE] = String.valueOf(resultado[ACRONIMODIASURGENTE]);
			descripcionServicio[NUMDIASNORMAL] = String.valueOf(resultado[NUMDIASNORMAL]);
			descripcionServicio[ACRONIMODIASNORMAL] = String.valueOf(resultado[ACRONIMODIASNORMAL]);
			
			return descripcionServicio;
			
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo encargado de obtener la descripcion del medicamento / insumo 
	 * y datos relacionados
	 * 
	 * @param codigoArticulo
	 * @param codigoTarifarioOficial
	 * @return String[]
	 * DESCRIPCIONARTICULO = 0;
	 * CODIGOPROPIETARIO   = 1;
	 * CONCENTRACION       = 2;
	 * UNIDADMEDIDA 	   = 3;
	 * @throws BDException
	 */
	public String[] obtenerDescripcionMedicamentoAutorizar(int codigoMedicamento, int institucion) throws BDException
	{
		
		String[] descripcionMedicamento = new String[4]; 
		String codigoTarifarioArticulos = "";
		
		final int DESCRIPCIONMEDICAMENTO = 0;
		final int CODIGOPROPIETARIO		 = 1;
		final int CONCENTRACION			 = 2;
		final int UNIDADMEDIDA   	     = 3;
		
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoArticulo", codigoMedicamento);
			Object[] resultado = (Object[]) persistenciaSvc.
					createNamedQueryUniqueResult("autorizacionCapitacion.obtenerDescripcionMedicamentoInsumo", parameters);
			
			
			//la descripcion se debe mostrar de acuerdo a lo definido en el parámetro general 
			//para el modulo de Administración 'Codigo Manual estandar Búsqueda de Artículos'
			codigoTarifarioArticulos = ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(institucion);
			
			if(codigoTarifarioArticulos != null && 
					codigoTarifarioArticulos.equals(ConstantesIntegridadDominio.acronimoInterfaz)) {
				descripcionMedicamento[CODIGOPROPIETARIO] = String.valueOf(resultado[2]);
			} else {
				descripcionMedicamento[CODIGOPROPIETARIO] = String.valueOf(resultado[1]);
			}
			
			descripcionMedicamento[DESCRIPCIONMEDICAMENTO] = String.valueOf(resultado[DESCRIPCIONMEDICAMENTO]);
			descripcionMedicamento[CONCENTRACION] = String.valueOf(resultado[CONCENTRACION]);
			descripcionMedicamento[UNIDADMEDIDA] = String.valueOf(resultado[UNIDADMEDIDA]);
			
			return descripcionMedicamento;
			
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Retorna una autorizacion de entidad subcontratada vs articulo
	 * 
	 * @param codigoAutoEntSub
	 * @param codigoArticulo
	 * @return autorizacionesEntSubArticu
	 * @throws IPSException
	 * @author jeilones
	 * @throws BDException 
	 * @created 22/08/2012
	 */
	@SuppressWarnings("unchecked")
	public List<AutorizacionesEntSubArticu> obtenerAutorizacionEntSubArticulo(
			long codigoAutoEntSub, int codigoArticulo) throws BDException {
		
		
		persistenciaSvc= new PersistenciaSvc();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("codigoAutoEntSub", codigoAutoEntSub);
		parameters.put("codigoArticulo", codigoArticulo);
		List<AutorizacionesEntSubArticu> autorizacionesEntSubArticu=null;
		try{
			autorizacionesEntSubArticu= (List<AutorizacionesEntSubArticu>) persistenciaSvc.
					createNamedQuery("autorizacionCapitacion.obtenerAutorizacionEntSubArticulo", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return autorizacionesEntSubArticu;
	}
	
	/**
	 * Retorna una autorizacion de entidad subcontratada vs servicio
	 * 
	 * @param codigoAutoEntSub
	 * @param codigoServicio
	 * @return autorizacionesEntSubArticu
	 * @throws IPSException
	 * @author jeilones
	 * @throws BDException 
	 * @created 22/08/2012
	 */
	@SuppressWarnings("unchecked")
	public List<AutorizacionesEntSubServi> obtenerAutorizacionEntSubServicio(
			long codigoAutoEntSub, int codigoServicio) throws BDException {
		
		
		persistenciaSvc= new PersistenciaSvc();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("codigoAutoEntSub", codigoAutoEntSub);
		parameters.put("codigoServicio", codigoServicio);
		List<AutorizacionesEntSubServi> autorizacionesEntSubServi=null;
		try{
			autorizacionesEntSubServi= (List<AutorizacionesEntSubServi>) persistenciaSvc.
					createNamedQuery("autorizacionCapitacion.obtenerAutorizacionEntSubServicio", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return autorizacionesEntSubServi;
	}
	
	/**
	 * Consulta las autorizaciones de entidad subcontratada de una autorizacion de capitacion que pertenece a un 
	 * ingreso estancia
	 * 
	 * @param idAutoIngEst
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 3/09/2012
	 */
	@SuppressWarnings("unchecked")
	public List<AutorizacionesEntidadesSub> obtenerAutorizacionEntSubDeIngEstancia(long idAutoIngEst)throws IPSException{
		persistenciaSvc= new PersistenciaSvc();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idAutoIngEst", idAutoIngEst);
		List<AutorizacionesEntidadesSub> autorizaciones=null;
		try{
			autorizaciones= (List<AutorizacionesEntidadesSub>) persistenciaSvc.
					createNamedQuery("autorizacionCapitacion.obtenerAutorizacionEntSubDeIngEstancia", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return autorizaciones;
	}
	
	/**
	 * Servicio encargado de validar si un Medicamento/Insumo esta cubierto por el convenio capitado de la orden
	 * 
	 * @param claseOrden
	 * @param codigoConvenio
	 * @param codigoOrden
	 * @param codigoMedicamentoInsumo
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public boolean tieneCoberturaMedicamentoInsumo(int claseOrden, int codigoConvenio, Long codigoOrden, int codigoMedicamentoInsumo) throws BDException{
		boolean cubierto=false;
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			List<String> medicamentoCubierto = new ArrayList<String>();
			parameters.put("codigoConvenio", codigoConvenio);
			parameters.put("codigoMedicamentoInsumo", codigoMedicamentoInsumo);
			parameters.put("cubierto", ConstantesBD.acronimoSi);
			if(claseOrden==ConstantesBD.claseOrdenOrdenMedica || claseOrden==ConstantesBD.claseOrdenCargoDirecto){
				parameters.put("codigoSolicitud", codigoOrden.intValue());
				medicamentoCubierto=(List<String>)persistenciaSvc.createNamedQuery("autorizacionCapitacion.tieneCoberturaMedicamentoInsumoSolicitud", parameters);
			}
			else if(claseOrden==ConstantesBD.claseOrdenOrdenAmbulatoria){
				parameters.put("codigoOrden", codigoOrden);
				medicamentoCubierto=(List<String>)persistenciaSvc.createNamedQuery("autorizacionCapitacion.tieneCoberturaMedicamentoInsumoOrdenAmbulatoria", parameters);
			}
			if(medicamentoCubierto != null && !medicamentoCubierto.isEmpty()){
				cubierto=true;
			}
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return cubierto;
	}
	
	/**
	 * Metodo que permite consultar la autorización de capitación subcontratada con sus respectivos detalles.
	 * @param codAutorizacion
	 * @param codOrden
	 * @param tipoOrden
	 * @return AutorizacionCapitacionDto
	 * @throws BDException
	 * @author DiaRuiPe
	 */
	@SuppressWarnings("unchecked")
	public AutorizacionCapitacionDto consultarAutorizacionCapitacion(int codAutorizacion, int codOrden, int tipoOrden) throws BDException{
		
		AutorizacionCapitacionDto autorizacionCapitacion=null;
		List<OrdenAutorizacionDto> ordenesAutorizar = null;
		try {
			
			persistenciaSvc= new PersistenciaSvc();
			autorizacionCapitacion=new AutorizacionCapitacionDto();
			ordenesAutorizar = new ArrayList<OrdenAutorizacionDto>();
			Map<String, Object> parameters1 = new HashMap<String, Object>();
			parameters1.put("codAutorizacion", codAutorizacion);
			
			//Consulto el la autorización de entidad subcontratada y capitación subcontratada
			autorizacionCapitacion= (AutorizacionCapitacionDto) persistenciaSvc.createNamedQueryUniqueResult("autorizacionCapitacion.obtenerAutorizacionEntCapitacion", parameters1);
			
			Map<String, Object> parameters2 = new HashMap<String, Object>();
			parameters2.put("codAutorizacion", codAutorizacion);
			parameters2.put("codOrden", codOrden);
			//Consulto el detalle de la orden medica de procedimientos
			if (tipoOrden == ConstantesBD.codigoTipoSolicitudProcedimiento){
				ordenesAutorizar = (List<OrdenAutorizacionDto>) persistenciaSvc.createNamedQuery("autorizacionCapitacion.obtenerDetalleAutorizacionSolProcedimientos", parameters2);
				for (OrdenAutorizacionDto ordenAutorizacionDto : ordenesAutorizar) {
					ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenOrdenMedica);
					ordenAutorizacionDto.setTipoOrden(ConstantesBD.codigoTipoSolicitudProcedimiento);
				}
				autorizacionCapitacion.setOrdenesAutorizar(ordenesAutorizar);
				autorizacionCapitacion.setProcesoExitoso(true);
			}
			
		
			//Consulto el detalle de la orden ambulatoria de servicios
			if (tipoOrden == ConstantesBD.codigoTipoOrdenAmbulatoriaServicios){
				ordenesAutorizar = (List<OrdenAutorizacionDto>) persistenciaSvc.createNamedQuery("autorizacionCapitacion.obtenerDetalleAutorizacionOrdenAmbServ", parameters2);
				for (OrdenAutorizacionDto ordenAutorizacionDto : ordenesAutorizar) {
					ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenOrdenAmbulatoria);
					ordenAutorizacionDto.setTipoOrden(ConstantesBD.codigoTipoOrdenAmbulatoriaServicios);
				}
				autorizacionCapitacion.setOrdenesAutorizar(ordenesAutorizar);
				autorizacionCapitacion.setProcesoExitoso(true);
			}
			
		} catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return autorizacionCapitacion;
	}
	
	/**
	 * Metodo que permite consultar la autorización de capitación subcontratada.
	 * @param codAutorizacion
	 * @return AutorizacionCapitacionDto
	 * @throws BDException
	 * @author Camilo Gomez
	 */
	public AutorizacionCapitacionDto obtenerAutorizacionCapitacionSub(long codAutorizacion) throws BDException{
		
		AutorizacionCapitacionDto autorizacionCapitacion=null;
		try {
			persistenciaSvc= new PersistenciaSvc();
			autorizacionCapitacion=new AutorizacionCapitacionDto();
			Map<String, Object> parameters1 = new HashMap<String, Object>();
			parameters1.put("codAutorizacion", codAutorizacion);
			
			//Consulto el la autorización de entidad subcontratada y capitación subcontratada
			autorizacionCapitacion	= (AutorizacionCapitacionDto) persistenciaSvc.
					createNamedQueryUniqueResult("autorizacionCapitacion.obtenerAutorizacionEntCapitacion", parameters1);
			
		} catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return autorizacionCapitacion;
	}
	
	/**
	 * Metodo que permite asociar las autorizaciones de medicamentos e insumos de ingreso estancia 
	 * a la autorización de capitación.
	 * 
	 * @param autorizacionesEstanciaCapita
	 * @throws BDException
	 * @author DiaRuiPe
	 */

	public void generarAsocioAutoSerArtIngresoEstancia(AutorizacionesEstanciaCapita autorizacionesEstanciaCapita) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			persistenciaSvc.persist(autorizacionesEstanciaCapita);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Metodo que permite obtener los servicios autorizados de una autorizacion y los valores correspondientes a cada uno.
	 * 
	 * @param codigoPkAutorizacion
	 * @throws BDException
	 * @return List<ServicioAutorizacionOrdenDto>
	 * @author ricruico
	 */

	@SuppressWarnings("unchecked")
	public List<ServicioAutorizacionOrdenDto> obtenerValorTarifaServiciosAutorizados(Long codigoPkAutorizacionEntSub) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			List<ServicioAutorizacionOrdenDto> listaServicios=null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("consecutivoAutoEntSub", codigoPkAutorizacionEntSub);
			listaServicios=(List<ServicioAutorizacionOrdenDto>)persistenciaSvc.createNamedQuery("autorizacionCapitacion.obtenerValorTarifaServiciosAutorizados", params);
			return listaServicios;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	
	/**
	 * Metodo que permite obtener los medicamentos/insumos autorizados de una autorizacion y los valores correspondientes a cada uno.
	 * 
	 * @param codigoPkAutorizacion
	 * @throws BDException
	 * @return List<MedicamentoInsumoAutorizacionOrdenDto>
	 * @author ricruico
	 */

	@SuppressWarnings("unchecked")
	public List<MedicamentoInsumoAutorizacionOrdenDto> obtenerValorTarifaMedicamentosInsumosAutorizados(Long codigoPkAutorizacionEntSub) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			List<MedicamentoInsumoAutorizacionOrdenDto> listaMedicamentosInsumos=null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("consecutivoAutoEntSub", codigoPkAutorizacionEntSub);
			listaMedicamentosInsumos=(List<MedicamentoInsumoAutorizacionOrdenDto>)persistenciaSvc.createNamedQuery("autorizacionCapitacion.obtenerValorTarifaMedicamentosInsumosAutorizados", params);
			return listaMedicamentosInsumos;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Metodo valida si para un medicamento de una solicitud de  medimentos existe una ordeb ambulatoria de medicamentos
	 * que tenga asociada autorizacion
	 * 
	 * @param numeroSolicitud
	 * @throws BDException
	 * @return boolean
	 * @author ricruico
	 */

	@SuppressWarnings("unchecked")
	public boolean existeAutorizacionMedicamentoOrden(int numeroSolicitud, int codigoMedicamentoInsumo, Long consecutivoAutorizacion) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			boolean existe=false;
			List<Integer> listaMedicamentosConAutorizacion=null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("numeroSolicitud", numeroSolicitud);
			params.put("codigoMedicamentoInsumo", codigoMedicamentoInsumo);
			params.put("consecutivoAutorizacionEntSub", consecutivoAutorizacion);
			listaMedicamentosConAutorizacion=(List<Integer>)persistenciaSvc.createNamedQuery("autorizacionCapitacion.existeAutorizacionMedicamentoOrden", params);
			if(listaMedicamentosConAutorizacion != null && !listaMedicamentosConAutorizacion.isEmpty()){
				existe=true;
			}
			return existe;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Sevicio que permite identificar si una Solicitud se encuentra asociada a una Orden Ambulatoria
	 * y retorna el consecutivo de la Orden Ambulatoria Asociada.
	 * 
	 * @param numeroSolicitud
	 * @return
	 * @throws IPSException
	 * @author DiaRuiPe
	 */

	@SuppressWarnings("unchecked")
	public String existeOrdenAmbAsociadaSolicitud(int numeroSolicitud) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			String consecutivoOrdenAmb=null;
			List<String> ordenesAmbulatorias=null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("numeroSolicitud", numeroSolicitud);
			ordenesAmbulatorias=(List<String>)persistenciaSvc.createNamedQuery("autorizacionCapitacion.existeOrdenAmbAsociadaSolicitud", params);
			if(ordenesAmbulatorias != null && !ordenesAmbulatorias.isEmpty()){
				consecutivoOrdenAmb=ordenesAmbulatorias.get(0);
			}
			return consecutivoOrdenAmb;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Consulta todos los servicios anulados de capitacion (Solicitud, Orden Ambulatoria o Peticion)
	 * @param consecutivo
	 * @param tarifario
	 * @param isMigrado
	 * @throws BDException
	 * @author hermorhu
	 */
	@SuppressWarnings("unchecked")
	public List<ServicioAutorizadoCapitacionDto> consultarServiciosAnuladosCapitacion(long consecutivo, long tarifario, boolean isMigrado) throws BDException {
		List<ServicioAutorizadoCapitacionDto>serviciosAutorizados=null;
		
		try{
			serviciosAutorizados=new ArrayList<ServicioAutorizadoCapitacionDto>(0);
			
			persistenciaSvc= new PersistenciaSvc();
			HashMap<String, Object>parameters=new HashMap<String, Object>(0);
			List<Integer>listaTiposSolicitud =new ArrayList<Integer>(0);
			listaTiposSolicitud.add(ConstantesBD.codigoTipoSolicitudInicialUrgencias);
			listaTiposSolicitud.add(ConstantesBD.codigoTipoSolicitudInicialHospitalizacion);
			listaTiposSolicitud.add(ConstantesBD.codigoTipoSolicitudCargosDirectosServicios);
			listaTiposSolicitud.add(ConstantesBD.codigoTipoSolicitudEstancia);
			parameters.put("tipoSolicitud", listaTiposSolicitud);
			parameters.put("autorizacion", consecutivo);
			parameters.put("migrado", isMigrado ? ConstantesBD.acronimoSi : ConstantesBD.acronimoNo);
			parameters.put("tarifario", tarifario);
			List<Object[]>servicios=(List<Object[]>)persistenciaSvc.createNamedQuery("autorizacionCapitacion.obtenerServiciosAnuladosCapitacion", parameters);
			
			for(Object[]servicio:servicios){
				
				final int tipo_autorizacion=0;
				final int codigo=1;
				final int consecutivo_aut=2;
				final int fecha_generacion=3;
				final int id_servicio=4;
				final int especialidad=5;
				final int tipo_servicio=6;
				final int grupo_servicio=7;
				final int cod_serv=8;
				final int nom_ser=9;
				final int nivel_auto=10;
				final int cantidad=11;
				final int acronimo_diag=12;
				final int tipo_cie_diag=13;
				final int diag_descripcion=14;
				final int valor_tarifa=15;
				final int indicativo_temporal=16;
				final int via_ingreso=17;
				final int id_ingreso=18;
				final int codigo_cuenta=19;
				final int tipo_paciente=20;
				final int tipo_solicitud=21;
				final int pyp=22;
				
				ServicioAutorizadoCapitacionDto servicioAutorizadoCapitacionDto=new ServicioAutorizadoCapitacionDto(
						(Long)servicio[tipo_autorizacion], 
						(Long)servicio[codigo],
						(Long)servicio[consecutivo_aut], 
						(Date)servicio[fecha_generacion],
						(Long)servicio[id_servicio],
						(Integer)servicio[especialidad],
						(String)servicio[tipo_servicio],
						(Integer)servicio[grupo_servicio],
						(String)servicio[cod_serv], 
						(String)servicio[nom_ser], 
						(String)servicio[nivel_auto], 
						(Long)servicio[cantidad], 
						(String)servicio[acronimo_diag], 
						(Integer)servicio[tipo_cie_diag],
						(String)servicio[diag_descripcion],
						(Double)servicio[valor_tarifa],
						(String)servicio[indicativo_temporal],
						(Integer)servicio[via_ingreso],
						(Long)servicio[id_ingreso],
						(Long)servicio[codigo_cuenta],
						(String)servicio[tipo_paciente],
						(Integer)servicio[tipo_solicitud],
						(String)servicio[pyp]);
				serviciosAutorizados.add(servicioAutorizadoCapitacionDto);
			}
			
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		return serviciosAutorizados;
	}
	
	/**
	 * Consulta todos los articulos autorizados de capitacion (Orden Ambulatoria o Solicitud Medicamentos)
	 * @param consecutivo
	 * @param institucion
	 * @param isMigrado
	 * @throws BDException
	 * @author hermorhu
	 */
	@SuppressWarnings("unchecked")
	public List<ArticuloAutorizadoCapitacionDto> consultarArticulosAnuladosCapitacion(long consecutivo, int institucion, boolean isMigrado) throws BDException {
		List<ArticuloAutorizadoCapitacionDto>articulosAutorizados = null;
		
		try{
			articulosAutorizados = new ArrayList<ArticuloAutorizadoCapitacionDto>(0);
			
			persistenciaSvc = new PersistenciaSvc();
			HashMap<String, Object>parameters = new HashMap<String, Object>(0);
			parameters.put("autorizacion", consecutivo);
			parameters.put("institucion", institucion);
			parameters.put("migrado",isMigrado ? ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			List<Object[]>articulos=(List<Object[]>)persistenciaSvc.createNamedQuery("autorizacionCapitacion.obtenerArticulosAnuladosCapitacion", parameters);
			
			for(Object[]articulo:articulos){
				
				final int tipo_autorizacion=0;
				final int codigo=1;
				final int consecutivo_aut=2;
				final int fecha_generacion=3;
				final int cod_art=4;
				final int nom_art=5;
				final int concentracion=6;
				final int forma_farmaceutica=7;
				final int unidad_medida=8;
				final int nat_art=9;
				final int acronimo_nat_art=10;
				final int codigo_subgrupo=11;
				final int cantidad=12;
				final int acronimo_diag=13;
				final int tipo_cie_diag=14;
				final int diag_descripcion=15;
				final int valor_tarifa=16;
				final int indicativo_temporal=17;
				final int via_ingreso=18;
				final int tipo_solicitud=19;
				final int pyp=20;
				  
				ArticuloAutorizadoCapitacionDto articuloAutorizadoCapitacionDto=new ArticuloAutorizadoCapitacionDto(
						(Long)articulo[tipo_autorizacion], 
						(Long)articulo[codigo],
						(Long)articulo[consecutivo_aut], 
						(Date)articulo[fecha_generacion], 
						(Long)articulo[cod_art], 
						(String)articulo[nom_art],
						(String)articulo[concentracion],
						(String)articulo[forma_farmaceutica],
						(String)articulo[unidad_medida],
						(String)articulo[nat_art],
						(String)articulo[acronimo_nat_art],
						(Integer)articulo[codigo_subgrupo],
						(Long)articulo[cantidad], 
						(String)articulo[acronimo_diag], 
						(Integer)articulo[tipo_cie_diag], 
						(String)articulo[diag_descripcion],
						(Double)articulo[valor_tarifa],
						(String)articulo[indicativo_temporal],
						(Integer)articulo[via_ingreso],
						(Integer)articulo[tipo_solicitud],
						(String)articulo[pyp]);
				articulosAutorizados.add(articuloAutorizadoCapitacionDto);
			}
			
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		return articulosAutorizados;
	}
	
	/**
	* Método que evalua si una solicitud tiene asociadas autorizaciones de capitacion 
	* Generadas desde la Petición de Cirugia
	* @author ricruico
	* @param numeroSolicitud
	* @return existe
	* @throws IPSException
	*
	*/
	@SuppressWarnings("unchecked")
	public boolean existeAutorizacionCapitacionGeneradaPorPeticion(int numeroSolicitud) throws IPSException {
		try{
			persistenciaSvc= new PersistenciaSvc();
			boolean existe=false;
			List<Long> autorizacionesPorPeticion=null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("numeroSolicitud", numeroSolicitud);
			params.put("migrado", ConstantesBD.acronimoSiChar);
			params.put("autorizado", ConstantesIntegridadDominio.acronimoAutorizado);
			autorizacionesPorPeticion=(List<Long>)persistenciaSvc.createNamedQuery("autorizacionCapitacion.existeAutorizacionCapitacionGeneradaPorPeticion", params);
			if(autorizacionesPorPeticion != null && !autorizacionesPorPeticion.isEmpty()){
				existe=true;
			}
			return existe;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}

	/**
	 * Metodo encargado de consultar los datos de la entrega de la autorizacion
	 * @param idAutorizacionEntidadSub
	 * @return {@link AutorizacionEntregaDto}
	 * @throws IPSException
	 * @author hermorhu
	 * @created 20-feb-2013 
	 */
	public AutorizacionEntregaDto consultarEntregaAutorizacionEntidadSubContratada(long idAutorizacionEntidadSub) throws BDException { 
		try {
			AutorizacionEntregaDto autorizacionEntregadaDto = null;
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("idAutorizacionEntidadSub", idAutorizacionEntidadSub);
			autorizacionEntregadaDto = (AutorizacionEntregaDto)persistenciaSvc.createNamedQueryUniqueResult("autorizacionCapitacion.consultarEntregaAutorizacionEntidadSubContratada", parametros);
			return autorizacionEntregadaDto;
		} catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Metodo encargado de guardar los datos de entrega de la autorizacion original
	 * @param autorizacionEntregaDto
	 * @return {@link Boolean}
	 * @throws BDException
	 * @author hermorhu
	 * @created 21-feb-2013
	 */
	public boolean guardarEntregaAutorizacionEntidadSubContratadaOriginal(AutorizacionEntregaDto autorizacionEntregaDto) throws BDException {
		try {
			persistenciaSvc= new PersistenciaSvc();
			
			EntregaAutorizacion entregaAutorizacion = new EntregaAutorizacion();
			
			entregaAutorizacion.setFechaEntrega(UtilidadFecha.conversionFormatoFechaStringDate(autorizacionEntregaDto.getFechaEntrega()));
			entregaAutorizacion.setHoraEntrega(autorizacionEntregaDto.getHoraEntrega());
			
			Usuarios usuario = new Usuarios();
			usuario.setLogin(autorizacionEntregaDto.getUsuarioEntrega().getLogin());
			
			entregaAutorizacion.setUsuarios(usuario);
			entregaAutorizacion.setPersonaRecibe(autorizacionEntregaDto.getPersonaRecibe());
			entregaAutorizacion.setObservaciones(autorizacionEntregaDto.getObservaciones());
			
			AutorizacionesEntidadesSub autorizacionEntidadesSub = new AutorizacionesEntidadesSub();
			autorizacionEntidadesSub.setConsecutivo(autorizacionEntregaDto.getIdAutorizacionEntidadSub());
			
			entregaAutorizacion.setAutorizacionesEntidadesSub(autorizacionEntidadesSub);
			
			entregaAutorizacion = persistenciaSvc.merge(entregaAutorizacion);

			return true;
		} catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Metodo encargado de consultar el id de la Autorizacion de Entidad Subcontratada
	 * apartir de el consecutivo de Autorizacion de Entidad SubContratada
	 * @param consecutivoAutorizacion
	 * @return {@link Long}
	 * @throws BDException
	 * @author hermorhu
	 * @created 26-feb-2013
	 */
	public Long consultarIdAutorizacionEntidadSubXConsecutivoAutorizacion(String consecutivoAutorizacion) throws BDException {
		try {
			Long idAutorizacionEntSub = null;
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("consecutivoAutorizacion", consecutivoAutorizacion);
			idAutorizacionEntSub = (Long)persistenciaSvc.createNamedQueryUniqueResult("autorizacionCapitacion.consultarIdAutorizacionEntidadSubXConsecutivoAutorizacion", parametros);
			return idAutorizacionEntSub;
		} catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		
		
	}

	/**
	 * Metodo encargado de consulta y el paciente paga atención o No 
	 * 
	 * @param codigoContrato
	 * @return Boolean
	 * @throws BDException
	 * @author sanbarga
	 * MT6703
	 * @created 12-Abr-2013
	 */
	public Boolean consultarSiPacientePagaAtencion(int codigoContrato) throws BDException {
		try {
			Boolean isPacientePagaAtención = false;
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("codigoContrato", codigoContrato);
			Character pacientePagaAtencion=(Character)persistenciaSvc.createNamedQueryUniqueResult("autorizacionCapitacion.consultarSiPacientePagaAtención", parametros);
			isPacientePagaAtención =pacientePagaAtencion!=null&&pacientePagaAtencion.equals('S');
			return isPacientePagaAtención;
		} catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		
		
	}
	
	public Boolean consultarSiConvenioManejaMontos(int codigoConvenio) throws BDException {
		try {
			Boolean isPacientePagaAtención = false;
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("codigoConvenio", codigoConvenio);
			String pacientePagaAtención=(String)persistenciaSvc.createNamedQueryUniqueResult("autorizacionCapitacion.consultarSiConvenioManejaMontoCobro", parametros);
			isPacientePagaAtención =pacientePagaAtención!=null&&pacientePagaAtención.equals("S");
			return isPacientePagaAtención;
		} catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		
		
	}



}
