/**
 * Juan David Ramírez 30/06/2006
 * Princeton S.A.
 */
package com.princetonsa.dao.oracle.capitacion;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.capitacion.AjusteCxCDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseAjusteCxCDao;

/**
 * @author Juan David Ramírez
 *
 */
public class OracleAjusteCxCDao implements AjusteCxCDao
{
	/**
	 * Método para consultar las opciones de selección de la funcionalidad
	 * @param con
	 * @param tipoConsulta
	 * @return
	 */
	public Collection consultarTipos(Connection con, int tipoConsulta, int codigoAjuste)
	{
		return SqlBaseAjusteCxCDao.consultarTipos(con, tipoConsulta, codigoAjuste);
	}

	/**
	 * Cargar ajuste existente
	 * @param con
	 * @param tipoAjuste
	 * @param consecutivo
	 * @return
	 */
	public HashMap cargarAjuste(Connection con, int tipoAjuste, int consecutivo)
	{
		return SqlBaseAjusteCxCDao.cargarAjuste(con, tipoAjuste, consecutivo);
	}

	/**
	 * Cargar ajuste existente
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarAjuste(Connection con, int codigo)
	{
		return SqlBaseAjusteCxCDao.cargarAjuste(con, codigo);
	}

	/**
	 * Método para cargar los datos de la cuenta de cobro
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public HashMap cargarCuentaCobro(Connection con, int cuentaCobro, int institucion)
	{
		return SqlBaseAjusteCxCDao.cargarCuentaCobro(con, cuentaCobro, institucion);
	}

	/**
	 * Método Para cargar El codigo y Nombre del Ajuste de una Cuenta de Cobro Especifica.
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public HashMap verificarCuentaCobro(Connection con,int tipoConsulta, int cuentaCobro, int institucion)
	{
		return SqlBaseAjusteCxCDao.verificarCuentaCobro(con, tipoConsulta, cuentaCobro, institucion);
	}

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
	 * @return
	 */
	public int guardar(Connection con, int tipoAjuste, String fecha, int cuentaCobro, String concepto, double valorAjuste, String observaciones, String usuario, int institucion,int consecutivo)
	{
		return SqlBaseAjusteCxCDao.guardar(con, tipoAjuste, fecha, cuentaCobro, concepto, valorAjuste, observaciones, usuario, institucion,consecutivo);
	}
	
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
	public int modificar(Connection con, int codigo, String fecha, String concepto, double valorAjuste, String observaciones)
	{
		return SqlBaseAjusteCxCDao.modificar(con, codigo, fecha, concepto, valorAjuste, observaciones);
	}

	/**
	 * Método para anular el ajuste
	 * @param con
	 * @param codigo
	 * @param motivoAnulacion
	 * @param loginUsuario
	 * @return
	 */
	public int anular(Connection con, int codigo, String motivoAnulacion, String loginUsuario)
	{
		return SqlBaseAjusteCxCDao.anular(con, codigo, motivoAnulacion, loginUsuario);
	}

	/**
	 * Método para cargar los cargues del ajuste
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public HashMap cargarCargues(Connection con, int cuentaCobro, int ajuste, int institucion)
	{
		return SqlBaseAjusteCxCDao.cargarCargues(con, cuentaCobro, ajuste,  institucion);
	}

	/**
	 * Método para guardar un detalle de un ajuste
	 * para un cargue
	 * @param con
	 * @param codigoCargue
	 * @param valor
	 * @param ajuste
	 * @return
	 */
	public int detalleAjuste(Connection con, int codigoCargue, double valor, int ajuste, String concepto, int institucion, boolean esModificacion)
	{
		return SqlBaseAjusteCxCDao.detalleAjuste(con, codigoCargue, valor, ajuste, concepto, institucion, esModificacion);
	}

	/**
	 * Método que retorna el detalle de los ajustes
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public HashMap consultarDetalleAjustes(Connection con, int codigoAjuste, boolean mostrarTodos)
	{
		return SqlBaseAjusteCxCDao.consultarDetalleAjustes(con, codigoAjuste, mostrarTodos);
	}
	
	/**
	 * Método para eliminar el detalle de los cargues existentes
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarDetalleAjustes(Connection con, int codigo)
	{
		return SqlBaseAjusteCxCDao.eliminarDetalleAjustes(con, codigo);
	}
	
	/**
	 * Metodo para Verificar Las Fechas de Cierre de Cartera Capitacion 
	 * @param con
	 * @param tipoFecha
	 * @param fechaGeneracionAjuste
	 * @return
	 */
	public HashMap verificarFechaCierre(Connection con, int tipoFecha, String fechaGeneracionAjuste, int institucion)
	{
		return SqlBaseAjusteCxCDao.verificarFechaCierre(con, tipoFecha, fechaGeneracionAjuste, institucion);
	}
	


}
