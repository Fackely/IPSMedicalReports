/**
 * 
 */
package com.servinte.axioma.servicio.interfaz.administracion;

import java.sql.Connection;

import com.princetonsa.dto.odontologia.DtoBusquedaEmisionBonos;
import com.princetonsa.dto.odontologia.administracion.DtoBonoDescuento;
import com.servinte.axioma.orm.EmisionBonosDesc;

/**
 * @author Juan David Ramírez
 * @since 2 Diciembre 2010
 */
public interface IEmisionBonosServicio
{

	/**
	 * Buscar una emisión de bonos de descuento según el bono enviado por parámetros
	 * @param bono Bono para el cual se desea buscar la emisión
	 * @return {@link EmisionBonosDesc} con el detalle del bono
	 */
	public EmisionBonosDesc buscarEmisionXBono(DtoBusquedaEmisionBonos bono);

	/**
	 * Marcar los bonos como utilizados = 'S' para que no sean utilizados de nuevo en el paciente.
	 * Adicionalmente genera una relación entre el programa donde se utilizó y el bono.
	 * @param con Conexión con la BD (Abierta)
	 * @param bono {@link DtoBonoDescuento} Bono que se desea asociar
	 * @return true en caso de hacer la modificación correctamente
	 */
	public boolean marcarBonoUtilizado(Connection con, DtoBonoDescuento bono);

}
