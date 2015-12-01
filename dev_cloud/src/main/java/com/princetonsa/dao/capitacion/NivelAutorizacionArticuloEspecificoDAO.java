/**
 * 
 */
package com.princetonsa.dao.capitacion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorArticuloEspecifico;

/**
 * Esta clase se encarga de definir los m�todos de
 * negocio para la entidad NivelAutorizacionArticuloEspecifico
 * @author Angela Aguirre
 *
 */
public interface NivelAutorizacionArticuloEspecificoDAO {
	
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
			DTOBusquedaNivelAutorArticuloEspecifico dto);

}
