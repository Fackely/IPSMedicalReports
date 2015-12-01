package com.servinte.axioma.servicio.impl.administracion;

import java.util.List;

import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IOcupacionesMedicasMundo;
import com.servinte.axioma.orm.OcupacionesMedicas;
import com.servinte.axioma.servicio.interfaz.administracion.IOcupacionesMedicasServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de la 
 * entidad Ocupaciones Médicas
 * 
 * @author Angela Maria Aguirre
 * @since 23/09/2010
 */
public class OcupacionesMedicaServicio implements IOcupacionesMedicasServicio {
	
	
	IOcupacionesMedicasMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public OcupacionesMedicaServicio(){
		mundo = AdministracionFabricaMundo.crearOcupacionesMedicasMundo();
	}
	

	/**
	 * 
	 * Este Método se encarga de consultar las ocupaciones médicas 
	 * existentes en el sistema
	 *  
	 * @return List<OcupacionesMedicas>
	 * @author Angela Maria Aguirre
	 */
	@Override
	public List<OcupacionesMedicas> listarOcupacionesMedicas() {
		return mundo.listarOcupacionesMedicas();
	}

}
