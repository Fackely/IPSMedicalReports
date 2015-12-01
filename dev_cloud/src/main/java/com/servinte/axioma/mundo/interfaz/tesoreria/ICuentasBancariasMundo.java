package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.CuentasBancarias;

/**
 * Define la l&oacute;gica de negocio relacionada con las Cuentas Bancarias
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.mundo.impl.tesoreria.CuentasBancariasMundo
 */



public interface ICuentasBancariasMundo {
	
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
