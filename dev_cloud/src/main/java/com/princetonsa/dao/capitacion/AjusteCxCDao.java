/**
 * Juan David Ramírez 30/06/2006
 * Princeton S.A.
 */
package com.princetonsa.dao.capitacion;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Juan David Ramírez
 *
 */
public interface AjusteCxCDao
{
	/**
	 * Método para consultar las opciones de selección de la funcionalidad
	 * @param con
	 * @param tipoConsulta
	 * @param codigoAjuste @todo
	 * @return
	 */
	public Collection consultarTipos(Connection con, int tipoConsulta, int codigoAjuste);

	/**
	 * Cargar ajuste existente
	 * @param con
	 * @param tipoAjuste
	 * @param consecutivo
	 * @return
	 */
	public HashMap cargarAjuste(Connection con, int tipoAjuste, int consecutivo);

	/**
	 * Cargar ajuste existente
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarAjuste(Connection con, int codigo);

	/**
	 * Método para cargar los datos de la cuenta de cobro
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public HashMap cargarCuentaCobro(Connection con, int cuentaCobro, int institucion);

	/**
	 * Método Para cargar El codigo y Nombre del Ajuste de una Cuenta de Cobro Especifica.
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @param consecutivo 
	 * @return
	 */
	public HashMap verificarCuentaCobro(Connection con,int tipoConsulta, int cuentaCobro, int institucion);

	/**
	 * Método para guardar el nuevo ajuste
	 * @param con
	 * @param tipoAjuste
	 * @param fecha
	 * @param cuentaCobro
	 * @param concepto
	 * @param valorAjuste
	 * @param observaciones
	 * @param usuario
	 * @param institucion
	 * @param consecutivo 
	 * @return
	 */
	public int guardar(Connection con, int tipoAjuste, String fecha, int cuentaCobro, String concepto, double valorAjuste, String observaciones, String usuario, int institucion, int consecutivo);

	/**
	 * Método para modificar el ajuste
	 * @param con
	 * @param codigo
	 * @param fecha
	 * @param concepto
	 * @param valorAjuste
	 * @param observaciones
	 * @return
	 */
	public int modificar(Connection con, int codigo, String fecha, String concepto, double valorAjuste, String observaciones);

	/**
	 * Método para anular el ajuste
	 * @param con
	 * @param codigo
	 * @param motivoAnulacion
	 * @param loginUsuario
	 * @return
	 */
	public int anular(Connection con, int codigo, String motivoAnulacion, String loginUsuario);
	
	/**
	 * Método para cargar los cargues del ajuste
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public HashMap cargarCargues(Connection con, int cuentaCobro,  int ajuste, int institucion);

	/**
	 * Método para guardar un detalle de un ajuste
	 * para un cargue
	 * @param con
	 * @param codigoCargue
	 * @param valor
	 * @param ajuste
	 * @param concepto @todo
	 * @param institucion @todo
	 * @param esModificacion @todo
	 * @return
	 */
	public int detalleAjuste(Connection con, int codigoCargue, double valor, int ajuste, String concepto, int institucion, boolean esModificacion);

	/**
	 * Método que retorna el detalle de los ajustes
	 * @param con
	 * @param codigoAjuste
	 * @param mostrarTodos @todo
	 * @return
	 */
	public HashMap consultarDetalleAjustes(Connection con, int codigoAjuste, boolean mostrarTodos);

	/**
	 * Método para eliminar el detalle de los cargues existentes
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarDetalleAjustes(Connection con, int codigo);

	/**
	 * Metodo para Verificar Las Fechas de Cierre de Cartera Capitacion 
	 * @param con
	 * @param tipoFecha
	 * @param fechaGeneracionAjuste
	 * @param institucion 
	 * @return
	 */
	public HashMap verificarFechaCierre(Connection con, int tipoFecha, String fechaGeneracionAjuste, int institucion);
	
}
