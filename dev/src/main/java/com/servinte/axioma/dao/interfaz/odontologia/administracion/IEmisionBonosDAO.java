/**
 * 
 */
package com.servinte.axioma.dao.interfaz.odontologia.administracion;

import java.sql.Connection;

import com.princetonsa.dto.odontologia.DtoBusquedaEmisionBonos;
import com.princetonsa.dto.odontologia.administracion.DtoBonoDescuento;
import com.servinte.axioma.orm.EmisionBonosDesc;

/**
 * @author axioma
 *
 */
public interface IEmisionBonosDAO
{

	/**
	 * Buscar una emisión de bonos de descuento según el bono enviado por parámetros
	 * @param bono Bono para el cual se desea buscar la emisión
	 * @return {@link EmisionBonosDesc} con el detalle del bono
	 */
	public EmisionBonosDesc buscarEmisionXBono(DtoBusquedaEmisionBonos bono);

	/**
	 * Marcar los bonos como utilizados = 'S' para que no sean utilizados de nuevo en el paciente.
	 * @param con Conexión con la BD (Abierta)
	 * @param bono {@link DtoBonoDescuento} Bono que se desea asociar
	 * @return true en caso de hacer la modificación correctamente
	 */
	public boolean marcarBonoUtilizado(Connection con, DtoBonoDescuento bono);

	/**
	 * Generar una relación entre el programa donde se utilizó y el bono.
	 * @param con Conexión con la BD (Abierta)
	 * @param bono {@link DtoBonoDescuento} Bono que se desea asociar
	 * @return true en caso de generar la relación correctamente
	 */
	public boolean asociarBonoProgramaConvenio(Connection con, DtoBonoDescuento bono);

}
