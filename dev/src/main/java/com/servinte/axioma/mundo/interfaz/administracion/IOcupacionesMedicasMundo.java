package com.servinte.axioma.mundo.interfaz.administracion;

import java.util.List;

import com.servinte.axioma.orm.OcupacionesMedicas;

/**
 * Esta clase se encarga de definir los m�todos de negocio de la 
 * entidad Ocupaciones M�dicas
 * 
 * @author Angela Maria Aguirre
 * @since 23/09/2010
 */
public interface IOcupacionesMedicasMundo {
	
	/**
	 * 
	 * Este M�todo se encarga de consultar las ocupaciones m�dicas 
	 * existentes en el sistema
	 * 
	 * @return List<OcupacionesMedicas>
	 */
	public List<OcupacionesMedicas> listarOcupacionesMedicas();

}
