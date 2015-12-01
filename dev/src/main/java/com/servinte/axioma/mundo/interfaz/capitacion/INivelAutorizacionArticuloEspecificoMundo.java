package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorArticuloEspecifico;

/**
 * Esta clase se encarga de definir los métodos de negocio de 
 * la entidad Nivel de Autorización de articulos específicos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public interface INivelAutorizacionArticuloEspecificoMundo {
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de nivel de autorización de un artículo específico
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionArticuloEspecifico(int id);
	
	/**
	 * 
	 * Este Método se encarga de buscar un registro de
	 * de nivel de autorización de artículos específicos
	 * 
	 * @param Connection conn, DTOBusquedaNivelAutorArticuloEspecifico dto
	 * @return ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> buscarNivelAutorizacionArticuloEspecifico(
			DTOBusquedaNivelAutorArticuloEspecifico dto);

}
