package com.servinte.axioma.bl.ordenes.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.servinte.axioma.bl.ordenes.interfaz.ISolicitudesMundo;
import com.servinte.axioma.delegate.ordenes.OrdenesAmbulatoriasDelegate;
import com.servinte.axioma.delegate.ordenes.SolicitudesDelegate;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionesSolicitudesDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.DetCargos;
import com.servinte.axioma.orm.DetalleSolicitudes;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.orm.SolCirugiaPorServicio;
import com.servinte.axioma.orm.SolProcedimientos;
import com.servinte.axioma.orm.Solicitudes;
import com.servinte.axioma.orm.SolicitudesConsulta;
import com.servinte.axioma.orm.SolicitudesInter;
import com.servinte.axioma.orm.SolicitudesSubcuenta;
import com.servinte.axioma.orm.SubCuentas;
import com.servinte.axioma.orm.Usuarios;

/**
 * @author wilgomcr
 * @version 1.0
 * @created 29-jun-2012 11:45:00 a.m.
 */
public class SolicitudesMundo implements ISolicitudesMundo{
	
	/**
	 * Método que se encarga de asociar las solicitudes generadas con las autorizaciones.
	 * @param  autorizacionesEntidadesSub
	 * @param autorizacionesEntidadesSub
	 * @param tipoOrden
	 * @param codigoServiArti
	 * @param codSolicitud
	 * @throws IPSException
	 */
	public void asociarSolicitudesAutorizaciones(AutorizacionesEntidadesSub autorizacionesEntidadesSub, int tipoOrden, int codigoServiArti, int codSolicitud, boolean requiereTransaccion)throws IPSException 
	{
		try{
			if (requiereTransaccion){
				HibernateUtil.beginTransaction();
			}
						
			if(tipoOrden==ConstantesBD.codigoTipoSolicitudCita){
				asociarCitaAutorizacion(autorizacionesEntidadesSub,codSolicitud);
			}else if(tipoOrden==ConstantesBD.codigoTipoSolicitudProcedimiento){
				asociarProcedimientoAutorizacion(autorizacionesEntidadesSub, codSolicitud);
			}else if(tipoOrden==ConstantesBD.codigoTipoSolicitudMedicamentos || 
					tipoOrden==ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos){
				asociarMedicamentosAutorizacion(autorizacionesEntidadesSub,codigoServiArti, codSolicitud);
			}else if(tipoOrden ==ConstantesBD.codigoTipoSolicitudCirugia){
				asociarCirugiaAutorizacion(autorizacionesEntidadesSub, codigoServiArti, codSolicitud);  
			}else if(tipoOrden ==ConstantesBD.codigoTipoSolicitudInterconsulta){
				asociarInterconsultaAutorizacion(autorizacionesEntidadesSub, codSolicitud);  
			}
			
			if (requiereTransaccion){
				HibernateUtil.endTransaction();
			}
			
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
	}

	/**
	 * Método que se encarga de asociar la solicitud de Consulta (Cita) generada con la autorización.
	 * @param autorizacionesEntidadesSub
	 * @param codSolicitud
	 * @throws IPSException
	 */
	private void asociarCitaAutorizacion(AutorizacionesEntidadesSub autorizacionesEntidadesSub, int codSolicitud)throws IPSException
	{
		SolicitudesDelegate delegate=null;
		SolicitudesConsulta solicitudesConsulta	=null; 
		try{
			delegate 			= new SolicitudesDelegate();
			solicitudesConsulta	= delegate.obtenerSolicitudConsultaPorId(codSolicitud);
			solicitudesConsulta.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
			delegate.actualizarSolicitudConsulta(solicitudesConsulta);
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
	}
	
	/**
	 * Método que se encarga de asociar la solicitud de Procedimientos generada con la autorización.
	 * @param autorizacionesEntidadesSub
	 * @param codigoServiArti
	 * @param codSolicitud
	 * @throws IPSException
	 */
	private void asociarProcedimientoAutorizacion(AutorizacionesEntidadesSub autorizacionesEntidadesSub, int codSolicitud)throws IPSException
	{
		SolicitudesDelegate delegate=null;
		SolProcedimientos solProcedimientos	=null; 
		try{
			delegate 			= new SolicitudesDelegate();
			solProcedimientos	= delegate.obtenerSolProcedimientosPorId(codSolicitud);
			solProcedimientos.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
			delegate.actualizarSolProcedimientos(solProcedimientos);
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
	}
	
	/**
	 * Método que se encarga de asociar la solicitud de Medicamentos generada con la autorización.
	 * @param autorizacionesEntidadesSub
	 * @param codigoServiArti
	 * @param codSolicitud
	 * @throws IPSException
	 */
	private void asociarMedicamentosAutorizacion(AutorizacionesEntidadesSub autorizacionesEntidadesSub, int codigoServiArti, int codSolicitud)throws IPSException
	{
		SolicitudesDelegate delegate=null;
		DetalleSolicitudes detalleSolicitudes	=null; 
		try{
			delegate 			= new SolicitudesDelegate();
			detalleSolicitudes	= delegate.obtenerDetalleSolicitudMedicamentosPorId(codSolicitud,codigoServiArti);
			detalleSolicitudes.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
			delegate.actualizarSolicitudMedicamentos(detalleSolicitudes);
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
	}
	
	/**
	 * Método que se encarga de asociar la solicitud de Cirugia generada con la autorización.
	 * @param autorizacionesEntidadesSub
	 * @param codigoServiArti
	 * @param codSolicitud
	 * @throws IPSException
	 */
	private void asociarCirugiaAutorizacion(AutorizacionesEntidadesSub autorizacionesEntidadesSub, int codigoServiArti, int codSolicitud)throws IPSException
	{
		SolicitudesDelegate delegate = null;
		SolCirugiaPorServicio solCirugiaPorServicio = null; 
		try{
			delegate				= new SolicitudesDelegate();
			solCirugiaPorServicio	= delegate.obtenerSolCirugiaPorServicioPorId(codSolicitud,codigoServiArti);
			solCirugiaPorServicio.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
			delegate.actualizarSolCirugiaPorServicio(solCirugiaPorServicio);
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
	}
	
	/**
	 * Retorna las solicitudes de una autorizacion y que provienen de una orden ambulatoria
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @return ArrayList<DtoEntregaMedicamentosInsumosEntSubcontratadas>
	 * 
	 * @author Jeison Londono
	 */
	public List<AutorizacionesSolicitudesDto> obtenerSolicitudesAutorizacionConOrdenAmbulatoria(
			DTOAutorEntidadSubcontratadaCapitacion dtoAutorizacionEntSubcontratadasCapitacion) throws IPSException{
		SolicitudesDelegate delegate = null;
		List<AutorizacionesSolicitudesDto>listaSolicitudes=new ArrayList<AutorizacionesSolicitudesDto>();
		try{
			HibernateUtil.beginTransaction();
			delegate				= new SolicitudesDelegate();
			listaSolicitudes=delegate.obtenerSolicitudesAutorizacionConOrdenAmbulatoria(dtoAutorizacionEntSubcontratadasCapitacion);
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
		return listaSolicitudes;		
	}
	
	/**
	 * Metodo que se encarga de Anular la solicitud y actualizar en sus repectivas entidades
	 * 
	 * @param anulacionesSolicitud
	 * @throws IPSException
	 */
	public void anularSolicitudes(AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto)throws IPSException
	{
		SolicitudesDelegate delegate= null;
		try{
			delegate	= new SolicitudesDelegate();
						
			//Se Guarda en Anulaciones_Solicitud
			delegate.guardarAnulacionesSolicitud(anulacionAutorizacionDto);
			//Se actualiza el estado de la Solicitud 
			delegate.anularSolicitudes(anulacionAutorizacionDto.getNumeroSolicitud());
			//Se actualiza el estado de DetCargo
			delegate.anularDetCargosPorSolicitud(anulacionAutorizacionDto.getNumeroSolicitud());			
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
	}
	
	/**
	 * Consultar una solicitud dado su ID
	 * 
	 * @param numeroSolicitud
	 * @return solicitud consultada
	 * @throws IPSException
	 * @author jeilones
	 * @created 16/08/2012
	 */
	public Solicitudes obtenerSolicitudPorId(int numeroSolicitud)throws IPSException
	{
		SolicitudesDelegate delegate= null;
		Solicitudes solicitud=null;
		try{
			delegate	= new SolicitudesDelegate();
						
			solicitud=delegate.obtenerSolicitudesPorId(numeroSolicitud);		
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
		return solicitud;
	}
	
	
	/**
	 * Método que se encarga de asociar la solicitud de Interconsula generada con la autorización.
	 * @param autorizacionesEntidadesSub
	 * @param codSolicitud
	 * @throws IPSException
	 */
	private void asociarInterconsultaAutorizacion(AutorizacionesEntidadesSub autorizacionesEntidadesSub, int codSolicitud)throws IPSException
	{
		SolicitudesDelegate delegate=null;
		SolicitudesInter solicitudesInterconsulta	=null; 
		try{
			delegate 			= new SolicitudesDelegate();
			solicitudesInterconsulta	= delegate.obtenerSolicitudInterConsultaPorId(codSolicitud);
			solicitudesInterconsulta.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub); 
			delegate.actualizarSolicitudInterconsulta(solicitudesInterconsulta);
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
	}
	
	/**
	 * Metodo que se encarga de obtener los servicios por solicitud
	 * @param codigoOrden
	 * @param claseOrden
	 * @param tipoOrden
	 * @return
	 * @throws IPSException
	 */
	public List<ServicioAutorizacionOrdenDto> obtenerServiciosPorAutorizar(int codigoOrden, 
			int claseOrden, int tipoOrden)throws IPSException
	{
		SolicitudesDelegate solicitudesDelegate = null;
		OrdenesAmbulatoriasDelegate ordenesAmbulatoriasDelegate = null;
		List<ServicioAutorizacionOrdenDto> listaServiciosOrden = null;
		try{
			HibernateUtil.beginTransaction();
			if(claseOrden==ConstantesBD.claseOrdenOrdenMedica ||
					claseOrden==ConstantesBD.claseOrdenCargoDirecto){
				solicitudesDelegate = new SolicitudesDelegate();
				listaServiciosOrden = solicitudesDelegate.obtenerServiciosPorAutorizar(codigoOrden, claseOrden, tipoOrden);
			}
			else if(claseOrden == ConstantesBD.claseOrdenOrdenAmbulatoria){
				ordenesAmbulatoriasDelegate = new OrdenesAmbulatoriasDelegate();
				listaServiciosOrden = ordenesAmbulatoriasDelegate.obtenerServiciosPorAutorizar(Long.valueOf(codigoOrden));
			}
			HibernateUtil.endTransaction();
			return listaServiciosOrden;
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
	}
	
	/**
	 * Metodo encargado de obtener los Medicamentos/Insumos pendientes por autorizar para una orden medica
	 * @param codigoOrden
	 * @return List<MedicamentoInsumoAutorizacionOrdenDto>
	 * @throws IPSException
	 * @author Camilo Gómez
	 */
	public List<MedicamentoInsumoAutorizacionOrdenDto> obtenerMedicamentosInsumosPorAutorizar(int codigoOrden)throws IPSException{
		SolicitudesDelegate solicitudesDelegate = null;
		List<MedicamentoInsumoAutorizacionOrdenDto> listaArticulosOrden = null;
		try{
			HibernateUtil.beginTransaction();
			solicitudesDelegate = new SolicitudesDelegate();
			listaArticulosOrden = solicitudesDelegate.obtenerMedicamentosInsumosPorAutorizar(codigoOrden);
			HibernateUtil.endTransaction();
			return listaArticulosOrden;
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
	}

	@Override
	public ContratoDto obtenerConvenioContratoPorOrdenMedica(int codigoOrden, int claseOrden, int tipoOrden)
			throws IPSException {
		SolicitudesDelegate delegate=null;
		ContratoDto contratoDto	=null; 
		try{
			delegate 			= new SolicitudesDelegate();
			HibernateUtil.beginTransaction();
			contratoDto	= delegate.obtenerConvenioContratoPorOrdenMedica(codigoOrden, claseOrden, tipoOrden);
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
		return contratoDto;
	}
	
	/**
	 * Metodo encargado de obtener los Medicamentos/Insumos con o sin autorización asociada
	 * @param codigoOrden
	 * @return List<MedicamentoInsumoAutorizacionOrdenDto>
	 * @author DiaRuiPe
	 * @throws IPSException
	 */
	@Override
	public List<MedicamentoInsumoAutorizacionOrdenDto> obtenerMedicamentosInsumosPorSolicitud(
			int codigoOrden) throws IPSException {
		SolicitudesDelegate solicitudesDelegate = null;
		List<MedicamentoInsumoAutorizacionOrdenDto> listaArticulosOrden = null;
		try{
			HibernateUtil.beginTransaction();
			solicitudesDelegate = new SolicitudesDelegate();
			listaArticulosOrden = solicitudesDelegate.obtenerMedicamentosInsumosPorSolicitud(codigoOrden);
			HibernateUtil.endTransaction();
			return listaArticulosOrden;
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
	}
	

	/**
	 * Retorna La entidad Ingreso por numero de solicitud 
	 * @param idSolicitud
	 * @return {@link com.servinte.axioma.orm.Ingresos}
	 * @throws IPSException
	 * @author javrammo
	 */
	public  Ingresos  obtenerIngresoPorNumeroSolicitud(int idSolicitud) throws IPSException{
		
	
		SolicitudesDelegate solicitudesDelegate = null;
		Ingresos ingreso = null;
		
		try{
			HibernateUtil.beginTransaction();
			solicitudesDelegate = new SolicitudesDelegate();
			ingreso = solicitudesDelegate.obtenerIngresoPorNumeroSolicitud(idSolicitud);
			HibernateUtil.endTransaction();
			return ingreso;
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
	}	

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.ordenes.interfaz.ISolicitudesMundo#actualizarCobertura(com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta, boolean)
	 */
	public  void  actualizarCobertura(DtoSolicitudesSubCuenta solicitudesSubcuenta, boolean requiereTransaccion) throws IPSException{
		
	
		SolicitudesDelegate solicitudesDelegate = null;
		
		try{
			solicitudesDelegate = new SolicitudesDelegate();
			
			SolicitudesSubcuenta solSubcuenta=new SolicitudesSubcuenta();
			
			
			solSubcuenta.setCuenta(Integer.parseInt(solicitudesSubcuenta.getCuenta()));
			SubCuentas subCuenta=new SubCuentas();
			subCuenta.setSubCuenta(((Double)Double.parseDouble(solicitudesSubcuenta.getSubCuenta())).intValue());
			solSubcuenta.setSubCuentas(subCuenta);
			Solicitudes solicitudes=new Solicitudes();
			
			solicitudes.setNumeroSolicitud(Integer.parseInt(solicitudesSubcuenta.getNumeroSolicitud()));
			
			solSubcuenta.setSolicitudes(solicitudes);
			
			Servicios servicios=new Servicios();
			servicios.setCodigo(Integer.parseInt(solicitudesSubcuenta.getServicio().getId()));
			
			solSubcuenta.setServiciosByServicio(servicios);
			
			Articulo articulo=new Articulo();
			articulo.setCodigo(Integer.parseInt(solicitudesSubcuenta.getArticulo().getId()));
			solSubcuenta.setArticulo(articulo);
			
			char cubierto=ConstantesBD.acronimoNoChar;
			if(solicitudesSubcuenta.getCubierto()!=null&&UtilidadTexto.getBoolean(solicitudesSubcuenta.getCubierto())){
				cubierto=ConstantesBD.acronimoSiChar;
			}
			solSubcuenta.setCubierto(cubierto);
			
			Usuarios usuario= new Usuarios();
			usuario.setLogin(solicitudesSubcuenta.getUsuarioModifica());
			solSubcuenta.setUsuarios(usuario);
			solSubcuenta.setCodigo(ConstantesBD.codigoNuncaValidoLong);
			
			Date fechaModificacion= UtilidadFecha.getFechaActualTipoBD();
			solSubcuenta.setFechaModifica(fechaModificacion);
			solSubcuenta.setHoraModifica(UtilidadFecha.getHoraActual());
			
			solicitudesDelegate.actualizarSolicitudSubcuenta(solSubcuenta);
			
			DetCargos detCargos=new DetCargos();
			
			detCargos.setSolicitudes(solicitudes);
			detCargos.setArticulo(articulo);
			detCargos.setCubierto(cubierto+"");
			detCargos.setFechaModifica(fechaModificacion);
			detCargos.setHoraModifica(UtilidadFecha.getHoraActual());
			detCargos.setUsuarios(usuario);
			
			solicitudesDelegate.actualizarDetCargo(detCargos);
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
}
