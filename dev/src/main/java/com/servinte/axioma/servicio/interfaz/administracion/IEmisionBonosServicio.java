/**
 * 
 */
package com.servinte.axioma.servicio.interfaz.administracion;

import java.sql.Connection;

import com.princetonsa.dto.odontologia.DtoBusquedaEmisionBonos;
import com.princetonsa.dto.odontologia.administracion.DtoBonoDescuento;
import com.servinte.axioma.orm.EmisionBonosDesc;

/**
 * @author Juan David Ram�rez
 * @since 2 Diciembre 2010
 */
public interface IEmisionBonosServicio
{

	/**
	 * Buscar una emisi�n de bonos de descuento seg�n el bono enviado por par�metros
	 * @param bono Bono para el cual se desea buscar la emisi�n
	 * @return {@link EmisionBonosDesc} con el detalle del bono
	 */
	public EmisionBonosDesc buscarEmisionXBono(DtoBusquedaEmisionBonos bono);

	/**
	 * Marcar los bonos como utilizados = 'S' para que no sean utilizados de nuevo en el paciente.
	 * Adicionalmente genera una relaci�n entre el programa donde se utiliz� y el bono.
	 * @param con Conexi�n con la BD (Abierta)
	 * @param bono {@link DtoBonoDescuento} Bono que se desea asociar
	 * @return true en caso de hacer la modificaci�n correctamente
	 */
	public boolean marcarBonoUtilizado(Connection con, DtoBonoDescuento bono);

}
