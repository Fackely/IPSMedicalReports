package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;

/**
 * Esta clase se encarga de definir los m�todos de negocio
 * de la entidad  AutorizacionesEntSubServi
 * 
 * @author Angela Maria Aguirre
 * @since 9/12/2010
 */
public interface IAutorizacionesEntSubServiDAO {
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos un registro de
	 * servicio de una autorizaci�n de entidad subcontratada
	 * 
	 * @param AutorizacionesEntSubServi servicio
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarServicioAutorizacionEntidadSub(AutorizacionesEntSubServi servicio);
	
	/**
	 * Este metodo permite modificar un valor para el registro previamente ingresado
	 * @param AutorizacionesEntSubServi servicio
	 * @return
	 * @author Diana Ruiz
	 */
	public boolean modificarServicioAutorizacionEntidadSub(AutorizacionesEntSubServi servicio);
	
	/**
	 * 
	 * Este M�todo se encarga de consultar los datos de los servicios
	 * de una autorizaci�n de capitaci�n, a trav�s del id de su respectiva
	 * autorizaci�n de entidad subcontratada
	 * 
	 * @param long idAutorEntSub
	 * @return ArrayList<DtoServiciosAutorizaciones>
	 * @author Angela Maria Aguirre
	 * 
	 */
	public ArrayList<DtoServiciosAutorizaciones> obtenerDetalleServiciosAutorCapitacion(long idAutorEntSub,String tipoTarifario);

	
	/**
	 * Lista los servicios de la autorizacion por entidad subcontradada
	 * 
	 * @author Fabi�n Becerra
	 * @param dtoParametros
	 * @return ArrayList<AutorizacionesEntSubServi>
	 */
	public ArrayList<AutorizacionesEntSubServi> listarAutorizacionesEntSubServiPorAutoEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoParametros) ;
}
