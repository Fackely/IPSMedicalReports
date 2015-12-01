/**
 * 
 */
package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntSubServiDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionesEntSubServiMundo;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio
 * de la entidad  AutorizacionesEntSubServi
 * 
 * @author Angela Maria Aguirre
 * @since 10/12/2010
 */
public class AutorizacionesEntSubServiMundo implements
		IAutorizacionesEntSubServiMundo {
	
	IAutorizacionesEntSubServiDAO dao;
	
	
	public AutorizacionesEntSubServiMundo(){
		dao = ManejoPacienteDAOFabrica.crearAurorizacionEntidadSubServi();
	}
	
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
	public ArrayList<DtoServiciosAutorizaciones> obtenerDetalleServiciosAutorCapitacion(long idAutorEntSub,String tipoTarifario){
		return dao.obtenerDetalleServiciosAutorCapitacion(idAutorEntSub,tipoTarifario);
	}
	
	
	/**
	 * Lista los servicios de la autorizacion por entidad subcontradada
	 * 
	 * @author Fabi�n Becerra
	 * @param dtoParametros
	 * @return ArrayList<AutorizacionesEntSubServi>
	 */
	public ArrayList<AutorizacionesEntSubServi> listarAutorizacionesEntSubServiPorAutoEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoParametros) {
		return dao.listarAutorizacionesEntSubServiPorAutoEntSub(dtoParametros);
	}

}
