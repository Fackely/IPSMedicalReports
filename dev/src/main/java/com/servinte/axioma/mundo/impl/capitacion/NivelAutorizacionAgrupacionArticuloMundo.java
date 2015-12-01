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
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionAgrupacionArticuloMundo(){
		dao = DaoFactory.getDaoFactory(
				System.getProperty("TIPOBD")).getNivelAutorizacionAgrupacionArticuloDAO();
	}

	/**
	 * 
	 * Este Método se encarga de insertar un registro de nivel de autorización
	 * de agrupación de artículos
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
	 * Este Método se encarga de eliminar un registro de nivel de autorización
	 * de agrupación de artículos
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
	 * Este Método se encarga de actualizar un registro de autorización
	 * de agrupación de artículos
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
	 * Este Método se encarga de buscar un registro de nivel de autorización
	 * de agrupación de artículos por el id del nivel de autorización de
	 * servicios - artículos relacionado
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
