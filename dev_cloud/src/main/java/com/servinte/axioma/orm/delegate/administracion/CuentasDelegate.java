package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;

import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.servinte.axioma.orm.Cuentas;
import com.servinte.axioma.orm.CuentasHome;


@SuppressWarnings("unchecked")
public class CuentasDelegate extends CuentasHome {
	
	
	/**
	 * Cuentas de todas las vías de ingreso.
	 * @param viaIngreso
	 * @return ArrayList<Cuentas>
	 */
	public ArrayList<Cuentas> verificarCuentasPorIngreso (int viaIngreso){
		
		return (ArrayList<Cuentas>) sessionFactory.getCurrentSession()
				.createCriteria(Cuentas.class)
				.add(Expression.eq("viasIngreso.codigo",viaIngreso))
				.list();
	}
	
	
	/**
	 * Cuentas con estado abierto por paciente
	 * @param codCuenta
	 * @return rrayList<Cuentas>
	 */
	public ArrayList<Cuentas> verificarEstadoCuenta (int codCuenta){
		
		return (ArrayList<Cuentas>) sessionFactory.getCurrentSession()
				.createCriteria(Cuentas.class)
				.add(Expression.eq("id",codCuenta))
				.list();
	}
	
	
	/**
	 * Retorna los tipos de estado por su actividad asignada
	 * @param listaEstadosCuenta
	 * @param codPaciente
	 * @return  ArrayList<Cuentas>
	 */
	public ArrayList<Cuentas> listarTodosXEstadoCuenta(String[] listaEstadosCuenta, int codPaciente){
		
		return (ArrayList<Cuentas>) sessionFactory.getCurrentSession()
				.createCriteria(Cuentas.class)
				.add(Expression.in("estado_cuenta", listaEstadosCuenta))
				.createCriteria("instituciones")
					.add(Expression.eq("codigo_paciente.codigo_paciente", codPaciente))
				.list();
	}
	
	/**
	 * Retorna la cuenta por el codigo de la persona
	 * @param codPersona
	 * @return  Cuentas
	 */
	public Cuentas obtenerCuentaPorCodigoPersona(int codPersona)
	{
		Cuentas cuenta = (Cuentas) sessionFactory.getCurrentSession()
		
		.createCriteria(Cuentas.class, "cuenta")
		.createAlias("cuenta.pacientes"					, "paciente")
		.createAlias("paciente.personas"				, "persona")
		
		.add(Restrictions.eq("persona.codigo"			, codPersona))
		.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id")
				.add(Projections.property("cuenta.ingresos"), "ingresos")
		)
		.setResultTransformer(Transformers.aliasToBean(Cuentas.class))
		
		.uniqueResult();
		return cuenta;
	}
	
}