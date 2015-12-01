package com.servinte.axioma.mundo.impl.administracion;

import java.util.List;

import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.IOcupacionesMedicasDAO;
import com.servinte.axioma.mundo.interfaz.administracion.IOcupacionesMedicasMundo;
import com.servinte.axioma.orm.OcupacionesMedicas;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio de la 
 * entidad Ocupaciones M�dicas
 * @author Angela Maria Aguirre
 * @since 23/09/2010
 */
public class OcupacionesMedicasMundo implements IOcupacionesMedicasMundo {
	
	IOcupacionesMedicasDAO dao;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public OcupacionesMedicasMundo(){
		dao = AdministracionFabricaDAO.crearOcupacionesMedicasDao();
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar las ocupaciones m�dicas 
	 * existentes en el sistema
	 * 
	 * @return List<OcupacionesMedicas>
	 */
	@Override
	public List<OcupacionesMedicas> listarOcupacionesMedicas() {
		return dao.listarOcupacionesMedicas();
	}

}
