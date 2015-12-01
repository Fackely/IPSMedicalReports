package com.servinte.axioma.mundo.impl.capitacion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.NivelAutorizacionAgrupacionArticuloDAO;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionArticulo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionAgrupacionArticuloMundo;
import com.servinte.axioma.persistencia.UtilidadPersistencia;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionAgrupacionArticuloMundo implements
		INivelAutorizacionAgrupacionArticuloMundo {
	
	NivelAutorizacionAgrupacionArticuloDAO dao;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionAgrupacionArticuloMundo(){
		dao = DaoFactory.getDaoFactory(
				System.getProperty("TIPOBD")).getNivelAutorizacionAgrupacionArticuloDAO();
	}

	/**
	 * 
	 * Este M�todo se encarga de insertar un registro de nivel de autorizaci�n
	 * de agrupaci�n de art�culos
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto, int TIPO_BD
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public int insertarMontoAgrupacionArticulo(DTOBusquedaNivelAutorAgrupacionArticulo dto) {
		Connection conn = UtilidadPersistencia.getPersistencia().obtenerConexion();
		int tipoBD = DaoFactory.getConstanteTipoBD(System.getProperty("TIPOBD"));
		int filasAfectadas = dao.insertarNivelAutorizacionAgrupacionArticulo(conn, dto, tipoBD);
		
		return filasAfectadas;
	}

	/**
	 * 
	 * Este M�todo se encarga de eliminar un registro de nivel de autorizaci�n
	 * de agrupaci�n de art�culos
	 * 
	 * @param Connection conn, DTOBusquedaNivelAutorAgrupacionArticulo dto
	 * @return boolean	
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean eliminarNivelAutorizacionAgrupacionArticulo(DTOBusquedaNivelAutorAgrupacionArticulo dto) {
		Connection conn = UtilidadPersistencia.getPersistencia().obtenerConexion();
		boolean eliminacionExitosa = dao.eliminarNivelAutorizacionAgrupacionArticulo(conn, dto);		
		return eliminacionExitosa;
	}

	/**
	 * 
	 * Este M�todo se encarga de actualizar un registro de autorizaci�n
	 * de agrupaci�n de art�culos
	 * 
	 * @param Connection conn, DTOBusquedaNivelAutorAgrupacionArticulo dto
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public int actualizarMontoAgrupacionArticulo(DTOBusquedaNivelAutorAgrupacionArticulo dto) {
		Connection conn = UtilidadPersistencia.getPersistencia().obtenerConexion();
		int filasAfectadas = dao.actualizarNivelAutorizacionAgrupacionArticulo(conn, dto);
		
		return filasAfectadas;
	}

	/**
	 * 
	 * Este M�todo se encarga de buscar un registro de nivel de autorizaci�n
	 * de agrupaci�n de art�culos por el id del nivel de autorizaci�n de
	 * servicios - art�culos relacionado
	 * 
	 * @param Connection conn, DTOBusquedaNivelAutorAgrupacionArticulo dto
	 * @return ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> buscarNivelAutorizacionAgrupacionArticulo(
			DTOBusquedaNivelAutorAgrupacionArticulo dto) {
		Connection conn = UtilidadPersistencia.getPersistencia().obtenerConexion();
		ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> lista = dao.buscarNivelAutorizacionAgrupacionArticulo(conn, dto);
		return lista;
	}

}
