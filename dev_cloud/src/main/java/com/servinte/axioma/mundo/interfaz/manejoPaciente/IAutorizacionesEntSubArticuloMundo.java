/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;

/**
 * Esta clase se encarga de encarga de definir los métodos de 
 * negocio para la entidad AutorizacionesEntSubArticu
 * 
 * @author Angela Maria Aguirre
 * @since 10/01/2011
 */
public interface IAutorizacionesEntSubArticuloMundo {
	
	/**
	 * 
	 * Este Método se encarga de consultar los datos de los artículos
	 * de una autorización de capitación, a través del id de su respectiva
	 * autorización de entidad subcontratada
	 * 
	 * @param long idAutorEntSub
	 * @return ArrayList<DtoArticulosAutorizaciones>
	 * @author Angela Maria Aguirre
	 * 
	 */
	public ArrayList<DtoArticulosAutorizaciones> obtenerDetalleArticulosAutorCapitacion(long idAutorEntSub);

	
	/**
	 * Listar los articulos por autorizacion 
	 * @author Fabián Becerra
	 * @param dtoParametros
	 * @return ArrayList<DtoArticulosAutorizaciones>
	 */
	public ArrayList<DtoArticulosAutorizaciones> listarautorizacionesEntSubArticuPorAutoEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoParametros);
}
