package com.servinte.axioma.mundo.impl.facturacion;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ILogProfSaludNoHonorarioDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogProfSaludNoHonorarioMundo;
import com.servinte.axioma.orm.LogProfSaludNoHonorario;

/**
 * Esta clase se encarga de definir los métodos para la entidad
 * LogProfSaludNoHonorario
 * 
 * @author Luis Fernando Hincapié Ospina
 * @since 13/01/2011
 */
public class LogProfSaludNoHonorarioMundo implements ILogProfSaludNoHonorarioMundo {

	/**
	 * Instancia de la clase ILogProfSaludNoHonorarioDAO
	 */
	ILogProfSaludNoHonorarioDAO dao;

	/**
	 * Método constructor de la clase.
	 * 
	 * @author Luis Fernando Hincapié Ospina
	 * @since 13/01/2011
	 */
	public LogProfSaludNoHonorarioMundo() {
		dao = FacturacionFabricaDAO.crearLogProfSaludNoHonorarioDAO();
	}

	/**
	 * Método encargado de insertar el log para profesionales de la salud a los
	 * cuales no se les va a generar valor de honorarios.
	 * 
	 * @param logProfSaludNoHonorario  Información del log a insertar
	 * @return retorna  Cuando es true es porque se pudo almacenar el log, en
	 *         caso contrario es false
	 * 
	 * @author Luis Fernando Hincapié Ospina
	 * @since 13/01/2011
	 */
	@Override
	public boolean insertar(LogProfSaludNoHonorario logProfSaludNoHonorario) {
		return dao.insertar(logProfSaludNoHonorario);
	}

}
