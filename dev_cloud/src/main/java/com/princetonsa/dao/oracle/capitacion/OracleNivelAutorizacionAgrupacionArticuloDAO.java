package com.princetonsa.dao.oracle.capitacion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.capitacion.NivelAutorizacionAgrupacionArticuloDAO;
import com.princetonsa.dao.sqlbase.capitacion.SQLBaseNivelAutorizacionAgrupacionArticuloDAO;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionArticulo;

/**
 * Esta clase se encarga de ejecutar los m�todos de
 * negocio para la entidad NivelAutorizacionAgrupacionArticulo
 * @author Angela Aguirre
 *
 */
public class OracleNivelAutorizacionAgrupacionArticuloDAO implements
		NivelAutorizacionAgrupacionArticuloDAO {

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
	public int insertarNivelAutorizacionAgrupacionArticulo(Connection conn,
			DTOBusquedaNivelAutorAgrupacionArticulo dto, int TIPO_BD) {
		return SQLBaseNivelAutorizacionAgrupacionArticuloDAO.insertarMontoAgrupacionArticulo(conn, dto, TIPO_BD);
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
	public boolean eliminarNivelAutorizacionAgrupacionArticulo(Connection conn,
			DTOBusquedaNivelAutorAgrupacionArticulo dto) {
		return SQLBaseNivelAutorizacionAgrupacionArticuloDAO.eliminarNivelAutorizacionAgrupacionArticulo(conn, dto);
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
	public int actualizarNivelAutorizacionAgrupacionArticulo(Connection conn,
			DTOBusquedaNivelAutorAgrupacionArticulo dto) {
		return SQLBaseNivelAutorizacionAgrupacionArticuloDAO.actualizarMontoAgrupacionArticulo(conn, dto);
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
			Connection conn, DTOBusquedaNivelAutorAgrupacionArticulo dto) {
		return SQLBaseNivelAutorizacionAgrupacionArticuloDAO.buscarNivelAutorizacionAgrupacionArticulo(conn, dto);
	}

}
