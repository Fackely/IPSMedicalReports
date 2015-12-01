package com.princetonsa.dao.capitacion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionArticulo;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public interface NivelAutorizacionAgrupacionArticuloDAO {
	
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
	public int insertarNivelAutorizacionAgrupacionArticulo(Connection conn,
			DTOBusquedaNivelAutorAgrupacionArticulo dto,int TIPO_BD);
	
	 /** 
	 * Este M�todo se encarga de eliminar un registro de nivel de autorizaci�n
	 * de agrupaci�n de art�culos
	 * 
	 * @param Connection conn, DTOBusquedaNivelAutorAgrupacionArticulo dto
	 * @return boolean	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionAgrupacionArticulo(Connection conn,
			DTOBusquedaNivelAutorAgrupacionArticulo dto);
	
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
	public int actualizarNivelAutorizacionAgrupacionArticulo(Connection conn,
			DTOBusquedaNivelAutorAgrupacionArticulo dto);
	
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
	public ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> buscarNivelAutorizacionAgrupacionArticulo(Connection conn,
			DTOBusquedaNivelAutorAgrupacionArticulo dto);

}
