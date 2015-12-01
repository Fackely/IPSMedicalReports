package com.servinte.axioma.mundo.impl.capitacion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.NivelAutorizacionArticuloEspecificoDAO;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorArticuloEspecifico;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionArticuloEspecificoDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionArticuloEspecificoMundo;
import com.servinte.axioma.persistencia.UtilidadPersistencia;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio de 
 * la entidad Nivel de Autorizaci�n de articulos espec�ficos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionArticuloEspecificoMundo implements
		INivelAutorizacionArticuloEspecificoMundo {
	
	INivelAutorizacionArticuloEspecificoDAO dao;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionArticuloEspecificoMundo(){
		dao = CapitacionFabricaDAO.crearNivelAutorizacionArticuloEspecificoDAO();
	}

	/**
	 * 
	 * Este m�todo se encarga de eliminar el registro
	 * de nivel de autorizaci�n de un art�culo espec�fico
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionArticuloEspecifico(int id) {
		return dao.eliminarNivelAutorizacionArticuloEspecifico(id);
	}
	
	/**
	 * 
	 * Este M�todo se encarga de buscar un registro de
	 * de nivel de autorizaci�n de art�culos espec�ficos
	 * 
	 * @param Connection conn, DTOBusquedaNivelAutorArticuloEspecifico dto
	 * @return ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> buscarNivelAutorizacionArticuloEspecifico(
			DTOBusquedaNivelAutorArticuloEspecifico dto) {
		
		NivelAutorizacionArticuloEspecificoDAO dao = DaoFactory.getDaoFactory(
				System.getProperty("TIPOBD")).getNivelAutorizacionArticuloEspecificoDAO();
		
		Connection conn = UtilidadPersistencia.getPersistencia().obtenerConexion();
		ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> lista = dao.buscarNivelAutorizacionArticuloEspecifico(conn, dto);
		return lista;
	}

}
