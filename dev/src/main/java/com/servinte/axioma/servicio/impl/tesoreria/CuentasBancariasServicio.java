package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICuentasBancariasMundo;
import com.servinte.axioma.orm.CuentasBancarias;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICuentasBancariasServicio;


/**
 * Implementaci&oacute;n de la interfaz {@link ICuentasBancariasServicio}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public class CuentasBancariasServicio implements ICuentasBancariasServicio {
	
	
	private ICuentasBancariasMundo cuentasBancariasMundo;
	
	public CuentasBancariasServicio() {
		cuentasBancariasMundo =  TesoreriaFabricaMundo.crearCuentasBancariasMundo();
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.ICuentasBancariasServicio#obtenerCuentasBancariasPorEntidad(int)
	 */
	public List<CuentasBancarias> obtenerCuentasBancariasPorEntidad(int consecutivoEntidad) {
		
		return cuentasBancariasMundo.obtenerCuentasBancariasPorEntidad(consecutivoEntidad);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.ICuentasBancariasServicio#obtenerCuentasBancariasPorEntidadYEmpresaInstitucion(int, int)
	 */
	@Override
	public List<CuentasBancarias> obtenerCuentasBancariasPorEntidadYEmpresaInstitucion(int consecutivoEntidad, int consecutivoCentroAtencion) {
		
		return cuentasBancariasMundo.obtenerCuentasBancariasPorEntidadYEmpresaInstitucion(consecutivoEntidad, consecutivoCentroAtencion);
	}
}
