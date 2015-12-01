package com.servinte.axioma.servicio.impl.capitacion;

import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionArticuloEspecificoMundo;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionArticuloEspecificoServicio;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio de 
 * la entidad Nivel de Autorizaci�n de articulos espec�ficos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionArticuloEspecificoServicio implements
		INivelAutorizacionArticuloEspecificoServicio {
	
	INivelAutorizacionArticuloEspecificoMundo mundo;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionArticuloEspecificoServicio(){
		mundo = CapitacionFabricaMundo.crearNivelAutorizacionArticuloEspecificoMundo();
	}

	 /** 
	 * Este m�todo se encarga de eliminar el registro
	 * de nivel de autorizaci�n de un art�culo espec�fico
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
