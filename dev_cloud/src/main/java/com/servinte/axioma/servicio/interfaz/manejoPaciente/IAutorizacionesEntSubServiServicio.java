/**
 * 
 */
package com.servinte.axioma.servicio.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * de la entidad  AutorizacionesEntSubServi
 * 
 * @author Angela Maria Aguirre
 * @since 10/12/2010
 */
public interface IAutorizacionesEntSubServiServicio {
	
	/**
	 * 
	 * Este Método se encarga de consultar los datos de los servicios
	 * de una autorización de capitación, a través del id de su respectiva
	 * autorización de entidad subcontratada
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
	 * @author Fabián Becerra
	 * @param dtoParametros
	 * @return ArrayList<AutorizacionesEntSubServi>
	 */
	public ArrayList<AutorizacionesEntSubServi> listarAutorizacionesEntSubServiPorAutoEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoParametros);

}
