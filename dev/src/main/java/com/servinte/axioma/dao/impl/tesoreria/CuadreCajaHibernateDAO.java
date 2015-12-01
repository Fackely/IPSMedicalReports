
package com.servinte.axioma.dao.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.dao.interfaz.tesoreria.ICuadreCajaDAO;
import com.servinte.axioma.orm.CuadreCaja;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.delegate.tesoreria.CuadreCajaDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link }.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see CuadreCajaDelegate.
 */

public class CuadreCajaHibernateDAO implements ICuadreCajaDAO{


	private CuadreCajaDelegate cuadreCajaDelegate;

	
	public CuadreCajaHibernateDAO() {
		cuadreCajaDelegate  = new CuadreCajaDelegate();
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.ICuadreCajaDAO#consultarCuadreCajaPorMovimiento(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<CuadreCaja> consultarCuadreCajaPorMovimiento(MovimientosCaja arqueoCaja) {
	
		return cuadreCajaDelegate.consultarCuadreCajaPorMovimiento(arqueoCaja);
	}
}
