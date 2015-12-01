/**
 * 
 */
package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionesEntSubArticuloMundo;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntSubArticuloServicio;

/**
 * @author mtwom
 *
 */
public class AutorizacionesEntSubArticuloServicio implements
		IAutorizacionesEntSubArticuloServicio {
	
	IAutorizacionesEntSubArticuloMundo mundo;
	
	public AutorizacionesEntSubArticuloServicio(){
		mundo = ManejoPacienteFabricaMundo.crearAutorizacionesEntSubArticuloMundo();
	}

	/**
	 * 
	 * Este M�todo se encarga de consultar los datos de los art�culos
	 * de una autorizaci�n de capitaci�n, a trav�s del id de su respectiva
	 * autorizaci�n de entidad subcontratada
	 * 
	 * @param long idAutorEntSub
	 * @return ArrayList<DtoArticulosAutorizaciones>
	 * @author Angela Maria Aguirre
	 * 
	 */
	@Override
	public ArrayList<DtoArticulosAutorizaciones> obtenerDetalleArticulosAutorCapitacion(
			long idAutorEntSub) {
		return mundo.obtenerDetalleArticulosAutorCapitacion(idAutorEntSub);
	}

	
	/**
	 * Listar los articulos por autorizacion 
	 * @author Fabi�n Becerra
	 * @param dtoParametros
	 * @return ArrayList<DtoArticulosAutorizaciones>
	 */
	public ArrayList<DtoArticulosAutorizaciones> listarautorizacionesEntSubArticuPorAutoEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoParametros){
		return mundo.listarautorizacionesEntSubArticuPorAutoEntSub(dtoParametros);
	}
}
