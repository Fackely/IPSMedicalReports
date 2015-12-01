/**
 * 
 * @author Jorge Armando Osorio Velasquez
 *
 */


package com.princetonsa.dao.webServiceCitasMedicas;

import java.sql.Connection;


/**
 * 
 * @author Jorge Armando Osorio Velasquez
 *
 */
public interface ConsultaWSDao
{

	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	int obtenerCitasAntendiasPortal(Connection con, String fechaInicial, String fechaFinal);

	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	int obtenerCitasSolicitadasPortal(Connection con, String fechaInicial, String fechaFinal);

	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	int obtenerTotalCitas(Connection con, String fechaInicial, String fechaFinal);

}
