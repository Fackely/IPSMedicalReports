/**
 * 
 */
package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionesEntSubServiMundo;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntSubServiServicio;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio
 * de la entidad  AutorizacionesEntSubServi
 * 
 * @author Angela Maria Aguirre
 * @since 10/12/2010
 */
public class AutorizacionesEntSubServiServicio implements
		IAutorizacionesEntSubServiServicio {
	
	IAutorizacionesEntSubServiMundo  mundo;
	
	
	public AutorizacionesEntSubServiServicio(){
		mundo = ManejoPacienteFabricaMundo.crearAutorizacionesEntSubServiMundo();
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
		return mundo.obtenerDetalleServiciosAutorCapitacion(idAutorEntSub, tipoTarifario);
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
		return mundo.listarAutorizacionesEntSubServiPorAutoEntSub(dtoParametros);
	}
	
	

}
