package com.servinte.axioma.bl.manejoPaciente.interfaz;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.facturacion.EstanciaAutomatica;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;



/**
 * @author DiaRuiPe
 * @version 1.0
 * @created 03-jul-2012 03:34:35 p.m.
 */

/**
 * Interface que presenta los servicios del proceso centralizado para la generaci�n de la 
 * autorizaci�n de capitaci�n subcontratada.
 */

public interface IProcesoGeneracionAutorizacionMundo {

	/**
	 * Servicio que permite realizar las validaciones necesarias para la generaci�n de la autorizaci�n capitaci�n subcontratada
	 * definido en el DCU 1106.
	 * 
	 * @param connection
	 * @param dtoAutorizacion
	 */
	public AutorizacionCapitacionDto generacionAutorizacion(AutorizacionCapitacionDto dtoAutorizacion, boolean requiereTransaccion)  throws IPSException;

	
	/**
	 * 
	 * @param autorizacionEntSubCapita
	 * @param con
	 * @throws IPSException
	 */
	public void generarProcesoAutorizacionEstanciaAutomaticaCargosPendientes(AutorizacionCapitacionDto autorizacionCapitacion, EstanciaAutomatica estanciaAutomatica,  
			HashMap mapaCuentas, UsuarioBasico usuario, int servicio, int pos, boolean esEstancia, Cargos cargos) throws IPSException;
	
	/**
	 * M�todo que permite asociar al detalle de la solicitud la autorizaci�n de capitaci�n subcontratada
	 * @param ordenesAutorizar
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	void asociarDetalleSolicitudesAutorizaciones(List<OrdenAutorizacionDto> ordenesAutorizar, AutorizacionesEntidadesSub autorizacionesEntidadesSub, boolean requiereTransaccion) throws IPSException;
	
	
	/**
	 * M�todo que se encarga de verificar si se generar� autorizacion para la solicitud
	 * 
	 * @param autorizacionCapitacionDto
	 * @throws IPSException
	 * @author wilgomcr
	 */
	List<AutorizacionCapitacionDto> verificarGenerarAutorizacionCapitacion(AutorizacionCapitacionDto autorizacionCapitacionDto)throws IPSException;

	/**
	 * M�todo que permite calcular la tarifa para los Servicios/MedicamentosInsumos a autorizar cuando 
	 * la tarifa de la entidad subcontratada es 'Convenio Paciente'.
	 *  
	 * @param autorizacionEntSubCapita
	 * @param con
	 * @throws IPSException
	 * @author jeilones
	 * @created 30/08/2012
	 */
	public void calcularTarifaArticuloServicioConvenio(AutorizacionCapitacionDto autorizacionEntSubCapita, Connection con) throws IPSException;
	
	/**
	 * M�todo que permite calcular la tarifa para los Servicios/MedicamentosInsumos a autorizar cuando 
	 * la tarifa de la entidad subcontratada es 'Propia'.
	 * 
	 * @param con
	 * @param autorizacionEntSubCapita
	 * @throws IPSException
	 * @author jeilones
	 * @created 30/08/2012
	 */
	public void calcularTarifaArticuloServicioEntSub(Connection con, AutorizacionCapitacionDto autorizacionEntSubCapita) throws IPSException;
	
	/**
	 * M�todo que realiza la validaci�n de presupuesto para los Servicios/MedicamentosInsumos a Autorizar
	 * 	
	 * @param autorizacionEntSubCapita
	 * @throws IPSException
	 * @author jeilones
	 * @created 30/08/2012
	 */
	public void validarPresupuestoCapitacion(AutorizacionCapitacionDto autorizacionEntSubCapita) throws IPSException;
	
	/**
	 * M�todo que permite borrar los registros de la tabla de cierre temporal donde la fecha
	 * de cierre sea anterior a la fecha del sistema
	 * @return
	 * @throws IPSException 
	 */
	public void eliminarAcumuladoCierreTemporal(int codContrato) throws IPSException;
	
	/**
	 * 
	 * @param autorizacionCapitacion
	 * @throws Exception 
	 */
	public void generarAcumuladoCierreTemporal(AutorizacionCapitacionDto autorizacionCapitacion) throws IPSException;

	/**
	 * M�todo para calcular el monto de cobro para la autorizaci�n 
	 * @param montoCobro
	 * @throws IPSException 
	 */
	public void calcularMontoCobro(AutorizacionCapitacionDto autorizacionEntSubCapita) throws IPSException;
	
	/**
	 * M�todo que permite guardar los montos generados por la autorizaci�n de capitaci�n subcontratada
	 * @param autorizacionEntSubCapita
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	public void generarAutorizacionEntSubMontos(AutorizacionCapitacionDto autorizacionEntSubCapita, 
			AutorizacionesEntidadesSub autorizacionesEntidadesSub) throws IPSException;
	
	/**
	 * M�todo que permite calcular la tarifa para los Servicios/MedicamentosInsumos a autorizar cuando 
	 * la tarifa de la entidad subcontratada es 'Convenio Paciente'.
	 *  
	 * @param autorizacionEntSubCapita
	 * @param con
	 * @throws IPSException
	 * @author jeilones
	 * @param requiereTransaccion 
	 * @created 30/08/2012
	 */
	public void generarAutorizacionServicioTemporal(AutorizacionCapitacionDto autorizacionEntSubCapita, boolean requiereTransaccion) throws IPSException;
	
	/**
	 * M�todo que permite asociar la autorizaci�n de entidad subcontratada a la solicitud por la cual se genera dicha 
	 * autorizaci�n
	 * @param ordenesAutorizar
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException 
	 */
	public void asociarSolicitudAutorizacion(List<OrdenAutorizacionDto> ordenesAutorizar, AutorizacionesEntidadesSub autorizacionesEntidadesSub) throws IPSException;
	
}