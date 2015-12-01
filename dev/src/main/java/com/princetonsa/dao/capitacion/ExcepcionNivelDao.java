/*
 * Creado el Jun 15, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.capitacion;

import java.sql.Connection;
import java.util.HashMap;

public interface ExcepcionNivelDao {

	/**
	 * Metodo para cargar Los convenios
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarInformacion(Connection con, int institucion);


	/**
	 * Metodo para traer los servicios de un convenio especifico
	 * @param con
	 * @param tipoInformacion 
	 * @param institucion 
	 * @param nroContrato 
	 * @return
	 */
	public HashMap cargarServiciosConvenio(Connection con, int codigoConvenio, int tipoInformacion, int institucion, int nroContrato);


	/**
	 * Metodo para insertar 
	 * @param con
	 * @param servicio
	 * @param institucion 
	 * @param contrato
	 * @return
	 */
	public int insertarServiciosConvenio(Connection con, int servicio, int contrato, int institucion);

	/**
	 * Metodo para eliminar el contrato.
	 * @param con
	 * @param nroServicio
	 * @param nroContrato
	 * @return
	 */

	public int eliminarServicioContrato(Connection con, int nroServicio, int nroContrato);

}
