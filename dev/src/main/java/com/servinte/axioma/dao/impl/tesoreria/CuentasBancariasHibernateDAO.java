package com.servinte.axioma.dao.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.dao.interfaz.tesoreria.ICuentasBancariasDAO;
import com.servinte.axioma.orm.CuentasBancarias;
import com.servinte.axioma.orm.delegate.tesoreria.CuentasBancariasDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link ICuentasBancariasDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see CuentasBancariasDelegate
 */

public class CuentasBancariasHibernateDAO implements ICuentasBancariasDAO{

	
	private CuentasBancariasDelegate cuentasBancariasDelegate;

	
	public CuentasBancariasHibernateDAO() {
		cuentasBancariasDelegate  = new CuentasBancariasDelegate();
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.ICuentasBancariasDAO#obtenerCuentasBancariasPorEntidad(int)
	 */
	public List<CuentasBancarias> obtenerCuentasBancariasPorEntidad(int consecutivoEntidad){
		
		return cuentasBancariasDelegate.obtenerCuentasBancariasPorEntidad(consecutivoEntidad);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.ICuentasBancariasDAO#obtenerCuentasBancariasPorEntidadYEmpresaInstitucion(int, int)
	 */
	@Override
	public List<CuentasBancarias> obtenerCuentasBancariasPorEntidadYEmpresaInstitucion(	int consecutivoEntidad, int consecutivoCentroAtencion) {
		
		return cuentasBancariasDelegate.obtenerCuentasBancariasPorEntidadYEmpresaInstitucion(consecutivoEntidad, consecutivoCentroAtencion);
	}
}
