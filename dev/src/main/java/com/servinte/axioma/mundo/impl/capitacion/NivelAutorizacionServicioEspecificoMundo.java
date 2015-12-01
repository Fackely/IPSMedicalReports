package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorServicioEspecifico;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionServicioEspecificoDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionServicioEspecificoMundo;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio de 
 * la entidad Nivel de Autorizaci�n de servicios espec�ficos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionServicioEspecificoMundo implements
		INivelAutorizacionServicioEspecificoMundo {
	
	INivelAutorizacionServicioEspecificoDAO dao;
	
	
	public NivelAutorizacionServicioEspecificoMundo(){
		dao = CapitacionFabricaDAO.crearNivelAutorizacionServicioEspecificoDAO();
	}
	
	

	/**
	 * 
	 * Este m�todo se encarga de consultar todos los
	 * niveles de autorizaci�n por servicios espec�ficos 
	 * a trav�s del id del nivel de autorizaci�n de serivicios -
	 * art�culos relacionado
	 * 
	 * @param int id
	 * @return ArrayList<DTOBusquedaNivelAutorServicioEspecifico>
	 * @author Angela Aguirre
	 * 
	 */
	public ArrayList<DTOBusquedaNivelAutorServicioEspecifico> buscarNivelAutorizacionServicios(int id,int codigoTarifario){
		return dao.buscarNivelAutorizacionServicios(id, codigoTarifario);
	}
	
	/**
	 * 
	 * Este m�todo se encarga de eliminar el registro
	 * de un nivel de autorizaci�n de servicio espec�fico
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarServicioEspecifico(int id){
		
		return dao.eliminarServicioEspecifico(id);
	}

}
