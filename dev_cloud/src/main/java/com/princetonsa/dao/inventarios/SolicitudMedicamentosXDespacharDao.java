package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.inventarios.AjustesXInventarioFisico;
import com.princetonsa.mundo.inventarios.ComparativoUltimoConteo;

/**
 * Interfaz de SolicitudMedicamentosXDespachar
 * @author garias@princetonsa.com
 */
public interface SolicitudMedicamentosXDespacharDao {

	/**
	 * 
	 * @param con
	 * @param filtros
	 * @return
	 */
	public String crearConsultaMedicamentosXDespachar(Connection con, HashMap filtros);
	
	/**
	 * 
	 * @param con
	 * @param filtros
	 * @return
	 */
	public String crearConsultaMedicamentosXEntregar(Connection con, HashMap filtros);
	
	/**
	 * 
	 * @param con
	 * @param filtros
	 * @return
	 */
	public String crearConsultaMedicamentosDespachadosXPaciente(Connection con, HashMap filtros);
	
	/**
	 * M�todo encargado de Obtener el c�digo de un centro de atenci�n dado su nombre
	 * @author Felipe P�rez Granda
	 * @param con
	 * @param nombreCentroAtencion
	 * @return int codigoCentroAtencion
	 */
	public int obtenerCodigoCentroAtencion(Connection con, String nombreCentroAtencion);
}	