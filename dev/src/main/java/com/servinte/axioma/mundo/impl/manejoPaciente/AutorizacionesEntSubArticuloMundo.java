/**
 * 
 */
package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntSubArticuloDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionesEntSubArticuloMundo;

/**
 * Esta clase se encarga de encarga de ejecutar los m�todos de 
 * negocio para la entidad AutorizacionesEntSubArticu
 * 
 * @author Angela Maria Aguirre
 * @since 10/01/2011
 */
public class AutorizacionesEntSubArticuloMundo implements
		IAutorizacionesEntSubArticuloMundo {
	
	IAutorizacionesEntSubArticuloDAO dao;
	
	public AutorizacionesEntSubArticuloMundo(){
		dao = ManejoPacienteDAOFabrica.crearAurorizacionEntidadSubArticulo();
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
		return dao.obtenerDetalleArticulosAutorCapitacion(idAutorEntSub);
	}
	
	
	/**
	 * Listar los articulos por autorizacion 
	 * @author Fabi�n Becerra
	 * @param dtoParametros
	 * @return ArrayList<DtoArticulosAutorizaciones>
	 */
	public ArrayList<DtoArticulosAutorizaciones> listarautorizacionesEntSubArticuPorAutoEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoParametros){
		return dao.listarautorizacionesEntSubArticuPorAutoEntSub(dtoParametros);
	}

}
