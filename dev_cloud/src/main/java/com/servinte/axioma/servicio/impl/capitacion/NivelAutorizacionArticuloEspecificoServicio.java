package com.servinte.axioma.servicio.impl.capitacion;

import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionArticuloEspecificoMundo;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionArticuloEspecificoServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización de articulos específicos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionArticuloEspecificoServicio implements
		INivelAutorizacionArticuloEspecificoServicio {
	
	INivelAutorizacionArticuloEspecificoMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionArticuloEspecificoServicio(){
		mundo = CapitacionFabricaMundo.crearNivelAutorizacionArticuloEspecificoMundo();
	}

	 /** 
	 * Este método se encarga de eliminar el registro
	 * de nivel de autorización de un artículo específico
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionArticuloEspecifico(int id) {
		return mundo.eliminarNivelAutorizacionArticuloEspecifico(id);
	}

}
