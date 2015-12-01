package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.CuentasBancarias;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de Cuentas Bancarias 
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */


public interface ICuentasBancariasDAO {
	
	/**
	 * Retorna un listado con las Cuentas Bancarias asociadas a una Entidad Financiera espec&iacute;fica
	 * 
	 * @param consecutivoEntidad
	 * @return List<{@link CuentasBancarias}>
	 */
	public List<CuentasBancarias> obtenerCuentasBancariasPorEntidad(int consecutivoEntidad);
	
	/**
	 * Retorna un listado con las Cuentas Bancarias asociadas a una Empresa - Instituci&oacute;n relacionada 
	 * a un Centro de atenci&oacute;n espec&iacute;fico. 
	 * 
	 * @param consecutivoCentroAtencion
	 * @return List<{@link CuentasBancarias}>
	 */
	public List<CuentasBancarias> obtenerCuentasBancariasPorEntidadYEmpresaInstitucion(int consecutivoEntidad, int consecutivoCentroAtencion);
}	
