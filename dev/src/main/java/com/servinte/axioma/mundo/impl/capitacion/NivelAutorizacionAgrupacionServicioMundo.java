package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionServicio;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionAgrupacionServicioDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionAgrupacionServicioMundo;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización de agrupación de servicios
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionAgrupacionServicioMundo implements
		INivelAutorizacionAgrupacionServicioMundo {
	
	INivelAutorizacionAgrupacionServicioDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionAgrupacionServicioMundo(){
		dao = CapitacionFabricaDAO.crearNivelAutorizacionAgrupacionServicioDAO();
	}

	/** 
	 * Este Método se encarga de consultar los niveles de autorización
	 * de agrupación de servicios registrados en el sistema
	 * 
	 * @return DTOBusquedaNivelAutorizacionServicioArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaNivelAutorAgrupacionServicio> buscarNivelAutorizacionServicioArticulo(
			int nivelAutorizacionID){			
		return dao.buscarNivelAutorizacionServicioArticulo(nivelAutorizacionID);
	}

	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización de agrupación de servicios
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionServicioArticulo(
			int idNivelAutorizacionUsuario) {
		return dao.eliminarNivelAutorizacionServicioArticulo(idNivelAutorizacionUsuario);
	}

}
