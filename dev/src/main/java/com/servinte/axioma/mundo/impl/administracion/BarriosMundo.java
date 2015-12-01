package com.servinte.axioma.mundo.impl.administracion;

import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.IBarriosDAO;
import com.servinte.axioma.mundo.interfaz.administracion.IBarriosMundo;
import com.servinte.axioma.orm.Barrios;

/**
 * Clase para los métodos de la clase Barrios
 * 
 * @author Ricardo Ruiz
 *
 */
public class BarriosMundo implements IBarriosMundo{

	IBarriosDAO  barriosDAO;
	
	public BarriosMundo(){
		barriosDAO=AdministracionFabricaDAO.crearBarriosDAO();
	}
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.administracion.IBarriosMundo#findByCodigoBarrio(java.lang.String)
	 */
	@Override
	public Barrios findByCodigoBarrio(String codigoBarrio, String codigoCiudad, String codigoDepartamento, String codigoPais) {
		return barriosDAO.findByCodigoBarrio(codigoBarrio, codigoCiudad, codigoDepartamento, codigoPais);
	}

}
