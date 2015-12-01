package com.servinte.axioma.mundo.interfaz.administracion;

import java.util.List;

import com.servinte.axioma.orm.OcupacionesMedicas;

/**
 * Esta clase se encarga de definir los métodos de negocio de la 
 * entidad Ocupaciones Médicas
 * 
 * @author Angela Maria Aguirre
 * @since 23/09/2010
 */
public interface IOcupacionesMedicasMundo {
	
	/**
	 * 
	 * Este Método se encarga de consultar las ocupaciones médicas 
	 * existentes en el sistema
	 * 
	 * @return List<OcupacionesMedicas>
	 */
	public List<OcupacionesMedicas> listarOcupacionesMedicas();

}
