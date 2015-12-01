package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoCentroCostoViaIngreso;
import com.servinte.axioma.dao.interfaz.manejoPaciente.ICentroCostoViaIngresoDAO;
import com.servinte.axioma.orm.delegate.manejoPaciente.AutorizacionesIngresoEstanciaDelegate;
import com.servinte.axioma.orm.delegate.manejoPaciente.CentroCostoViaIngresoDelegate;

/**
 * @author Cristhian Murillo
 */
public class CentroCostoViaIngresoHibernateDAO implements ICentroCostoViaIngresoDAO
{
	
	CentroCostoViaIngresoDelegate centroCostoViaIngresoDaoImp;

	public CentroCostoViaIngresoHibernateDAO(){
		centroCostoViaIngresoDaoImp = new CentroCostoViaIngresoDelegate();
	}
	
	@Override
	public ArrayList<DtoCentroCostoViaIngreso> obtenerCentrosCostoViaIngreso(int institucion) {
		return centroCostoViaIngresoDaoImp.obtenerCentrosCostoViaIngreso(institucion);
	}

	
	/**
	 * M&eacute;todo encargado de obtener los centros de costo por 
	 * v&iacute;a de ingreso
	 * @author Camilo Gomez
	 * @param viaIngreso
	 * @return ArrayList<DtoCentroCostoViaIngreso>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoCentroCostoViaIngreso> listarCentrosCostoXViaIngreso(int viaIngreso){
		return centroCostoViaIngresoDaoImp.listarCentrosCostoXViaIngreso(viaIngreso);
	}
	
}
