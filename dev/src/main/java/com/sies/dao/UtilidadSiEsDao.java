package com.sies.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

/**
 * Clase para el manejo de las Utilidades que van a BD
 * @author juanda
 *
 */
public interface UtilidadSiEsDao
{
	/**
	 * M�todo para obtener el siguiente valor de una secuencia
	 * @param con
	 * @param nombreSecuencia
	 * @return valor de la secuencia
	 */
	public int obtenerValorSecuencia(Connection con, String nombreSecuencia);

	/**
	 * M�todo para consultar el nombre de la persona
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarNombrePersona(Connection con, int codigoPersona);

	/**
	 * M�todo que busca un listado de personas por nombre (criterio=false) o por c�digo (criterio=true)
	 * @param textoBusqueda
	 * @param criterio
	 * @param institucion Instituci�n
	 * @param centroCosto
	 * @param centroAtencion
	 * @param connection
	 * @return
	 */
	public Collection<HashMap<String, Object>> busquedaAvanzadaPersonas(Connection con, String textoBusqueda, boolean criterio, int institucion, int centroCosto, int centroAtencion);

	/**
	 * M�todo que busca personas que no tienen turnos en un rango de fechas
	 * para in cuadro de turnos espec�fico
	 * @param connection
	 * @param textoBusqueda
	 * @param criterio
	 * @param institucion
	 * @param centroCosto
	 * @param centroAtencion
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoCuadro
	 * @return
	 */
	public Collection<HashMap<String, Object>> busquedaAvanzadaPersonas(Connection connection, String textoBusqueda, boolean criterio, int institucion, int centroCosto, int centroAtencion, String fechaInicial, String fechaFinal, int codigoCuadro);
	
	/**
	 * M�todo para consultar la categor�a a la cual pertenece un turno 
	 * @param con
	 * @param codigoTurno
	 * @return C�digo de la categor�a
	 */
	public Integer codigoCategoriaTurno(Connection con, int codigoTurno);

	/**
	 * M�todo para listar los centros de costo
	 * @param con Conexi�n con la base de datos
	 * @return Listado de los centros de costo (Keys: "codigo", "nombre")
	 */
	public Collection<HashMap<String, Object>> listarCentrosCosto(Connection con);

}
