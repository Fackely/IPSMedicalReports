package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.manejoPaciente.ICuentasDAO;
import com.servinte.axioma.orm.Cuentas;
import com.servinte.axioma.orm.delegate.administracion.CuentasDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link ICuentasDAO}.
 * 
 * @author Cristhian Murillo
 * @see CuentasDelegate.
 */
public class CuentasHibernateDAO implements ICuentasDAO{

	
	private CuentasDelegate daoImpl = new CuentasDelegate();

	@Override
	public ArrayList<Cuentas> listarTodosXEstadoCuenta(
			String[] listaEstadosCuenta, int codPaciente) {
		return daoImpl.listarTodosXEstadoCuenta(listaEstadosCuenta, codPaciente);
	}

	@Override
	public ArrayList<Cuentas> verificarCuentasPorIngreso(int viaIngreso) {
		return daoImpl.verificarCuentasPorIngreso(viaIngreso);
	}

	@Override
	public ArrayList<Cuentas> verificarEstadoCuenta(int codCuenta) {
		return daoImpl.verificarEstadoCuenta(codCuenta);
	}

	@Override
	public Cuentas obtenerCuentaPorCodigoPersona(int codPersona){
		return daoImpl.obtenerCuentaPorCodigoPersona(codPersona);
	}
}
