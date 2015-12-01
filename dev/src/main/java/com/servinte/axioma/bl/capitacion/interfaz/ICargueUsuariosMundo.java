/**
 * 
 */
package com.servinte.axioma.bl.capitacion.interfaz;

import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.dto.capitacion.CargueDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author jeilones
 * @created 4/10/2012
 *
 */
public interface ICargueUsuariosMundo {
	/**
	 * Consulta los cargue de usuarios que coincida con los filtros de busqueda
	 * 
	 * @param requiereTransaccion
	 * @param filtrosUsuario
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 4/10/2012
	 */
	public List<DtoUsuariosCapitados> consultarCargueUsuarios(boolean requiereTransaccion,DtoUsuariosCapitados filtrosUsuario) throws IPSException;
	
	/**
	 * Consulta el grupo familiar de un usuario cotizante
	 * 
	 * @param requiereTransaccion
	 * @param usuarioCapitado
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 4/10/2012
	 */
	public List<DtoUsuariosCapitados> consultarGrupoFamiliar(boolean requiereTransaccion,DtoUsuariosCapitados usuarioCapitado) throws IPSException;
	
	/**
	 * Consulta el historico de cargues que se le haya hecho a un usuario
	 * 
	 * @param requiereTransaccion
	 * @param usuarioCapitado
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 4/10/2012
	 */
	public List<CargueDto> consultarHistoricoCargue(boolean requiereTransaccion,DtoUsuariosCapitados usuarioCapitado) throws IPSException;
	
	/**
	 * Consulta la informacion detallada de un usuario capitado
	 * 
	 * @param requiereTransaccion
	 * @param usuarioCapitado
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 5/10/2012
	 */
	public DtoUsuariosCapitados consultarDetalleUsuario(boolean requiereTransaccion,DtoUsuariosCapitados usuarioCapitado) throws IPSException;
}
