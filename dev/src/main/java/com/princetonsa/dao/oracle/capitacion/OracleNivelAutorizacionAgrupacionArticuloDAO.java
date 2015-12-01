package com.princetonsa.dao.oracle.capitacion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.capitacion.NivelAutorizacionAgrupacionArticuloDAO;
import com.princetonsa.dao.sqlbase.capitacion.SQLBaseNivelAutorizacionAgrupacionArticuloDAO;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionArticulo;

/**
 * Esta clase se encarga de ejecutar los métodos de
 * negocio para la entidad NivelAutorizacionAgrupacionArticulo
 * @author Angela Aguirre
 *
 */
public class OracleNivelAutorizacionAgrupacionArticuloDAO implements
		NivelAutorizacionAgrupacionArticuloDAO {

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
	public int insertarNivelAutorizacionAgrupacionArticulo(Connection conn,
			DTOBusquedaNivelAutorAgrupacionArticulo dto, int TIPO_BD) {
		return SQLBaseNivelAutorizacionAgrupacionArticuloDAO.insertarMontoAgrupacionArticulo(conn, dto, TIPO_BD);
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
	public boolean eliminarNivelAutorizacionAgrupacionArticulo(Connection conn,
			DTOBusquedaNivelAutorAgrupacionArticulo dto) {
		return SQLBaseNivelAutorizacionAgrupacionArticuloDAO.eliminarNivelAutorizacionAgrupacionArticulo(conn, dto);
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
	public int actualizarNivelAutorizacionAgrupacionArticulo(Connection conn,
			DTOBusquedaNivelAutorAgrupacionArticulo dto) {
		return SQLBaseNivelAutorizacionAgrupacionArticuloDAO.actualizarMontoAgrupacionArticulo(conn, dto);
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
			Connection conn, DTOBusquedaNivelAutorAgrupacionArticulo dto) {
		return SQLBaseNivelAutorizacionAgrupacionArticuloDAO.buscarNivelAutorizacionAgrupacionArticulo(conn, dto);
	}

}
