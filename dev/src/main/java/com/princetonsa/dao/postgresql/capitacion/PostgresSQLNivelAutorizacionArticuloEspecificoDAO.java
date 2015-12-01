/**
 * 
 */
package com.princetonsa.dao.postgresql.capitacion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.capitacion.NivelAutorizacionArticuloEspecificoDAO;
import com.princetonsa.dao.sqlbase.capitacion.SQLBaseNivelAutorizacionArticuloEspecificoDAO;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorArticuloEspecifico;

/**
 * Esta clase se encarga de ejecutar los m�todos de
 * negocio para la entidad NivelAutorizacionArticuloEspecifico
 * @author Angela Aguirre
 *
 */
public class PostgresSQLNivelAutorizacionArticuloEspecificoDAO implements
	NivelAutorizacionArticuloEspecificoDAO {
	
	
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
	public ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> buscarNivelAutorizacionArticuloEspecifico(Connection conn,
			DTOBusquedaNivelAutorArticuloEspecifico dto){
		return SQLBaseNivelAutorizacionArticuloEspecificoDAO.buscarNivelAutorizacionArticuloEspecifico(conn, dto);
	}	

}
