package com.servinte.axioma.orm.delegate.interfaz;

import java.util.ArrayList;

import org.hibernate.criterion.Expression;

import com.servinte.axioma.orm.CuentaInterfazEmpresInsti;
import com.servinte.axioma.orm.CuentaInterfazEmpresInstiHome;


/**
 * @author Cristhian Murillo
 */
@SuppressWarnings("unchecked")
public class CuentaInterfazEmpresInstiDelegate  extends CuentaInterfazEmpresInstiHome
{
	
	/**
	 * Lista las cuentas parametrizadas para esta Institucion (Cuentas contables por cobrar traslado de abonos)
	 * @param institucion
	 * @return ArrayList<CuentaInterfazEmpresInsti>
	 */
	public ArrayList<CuentaInterfazEmpresInsti> listarPorInstitucion(int institucion){
		
		return (ArrayList<CuentaInterfazEmpresInsti>) sessionFactory.getCurrentSession()
			.createCriteria(CuentaInterfazEmpresInsti.class)
			.add(Expression.eq("instituciones.codigo",institucion))
			.list();
	}
	
	
}
