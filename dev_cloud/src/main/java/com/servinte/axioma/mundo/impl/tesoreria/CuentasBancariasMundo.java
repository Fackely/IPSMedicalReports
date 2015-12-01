package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICuentasBancariasDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICuentasBancariasMundo;
import com.servinte.axioma.orm.CuentasBancarias;


/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con las 
 * Cuentas Bancarias
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see ICuentasBancariasMundo
 */

public class CuentasBancariasMundo implements ICuentasBancariasMundo{

	
	private ICuentasBancariasDAO cuentasBancariasDAO;

	public CuentasBancariasMundo() {
		inicializar();
	}
	
	private void inicializar() {
		cuentasBancariasDAO = TesoreriaFabricaDAO.crearCuentasBancariasDAO();
	}
	

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.ICuentasBancariasMundo#obtenerCuentasBancariasPorEntidad(int)
	 */
	public List<CuentasBancarias> obtenerCuentasBancariasPorEntidad(int consecutivoEntidad) {
		
		return cuentasBancariasDAO.obtenerCuentasBancariasPorEntidad(consecutivoEntidad);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.ICuentasBancariasMundo#obtenerCuentasBancariasPorEntidadYEmpresaInstitucion(int, int)
	 */
	@Override
	public List<CuentasBancarias> obtenerCuentasBancariasPorEntidadYEmpresaInstitucion(int consecutivoEntidad, int consecutivoCentroAtencion) {
		
		return cuentasBancariasDAO.obtenerCuentasBancariasPorEntidadYEmpresaInstitucion(consecutivoEntidad, consecutivoCentroAtencion);
	}
}
