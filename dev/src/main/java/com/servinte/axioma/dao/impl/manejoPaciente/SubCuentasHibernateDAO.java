package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.manejoPaciente.ISubCuentasDAO;
import com.servinte.axioma.orm.SubCuentas;
import com.servinte.axioma.orm.delegate.manejoPaciente.SubCuentasDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link ISubCuentasDAO}.
 * 
 * @author Cristhian Murillo
 * @see SubCuentasDelegate.
 */
public class SubCuentasHibernateDAO implements ISubCuentasDAO{

	
	private SubCuentasDelegate daoImpl = new SubCuentasDelegate();

	
	@Override
	public ArrayList<SubCuentas> listarCuentasPorPaciente(int paciente) {
		return daoImpl.listarCuentasPorPaciente(paciente);
	}


	@Override
	public SubCuentas cargarSubCuenta(int codigoResponsable) 
	{
		return daoImpl.findById(codigoResponsable);
		
	}
	
	/**
	 * Carga una subcuenta por su id y su detalle monto
	 * @param id de la subcuenta
	 * @return SubCuentas
	 */
	@Override
	public SubCuentas cargarSubcuentaDetalleMonto(int codigoResponsable){
		return daoImpl.cargarSubcuentaDetalleMonto(codigoResponsable);
	}

}
