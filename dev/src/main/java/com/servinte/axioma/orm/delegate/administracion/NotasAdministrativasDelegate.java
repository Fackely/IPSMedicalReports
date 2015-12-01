package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import util.ConstantesBD;
import com.servinte.axioma.orm.NotasAdministrativas;
import com.servinte.axioma.orm.NotasAdministrativasHome;
import com.servinte.axioma.orm.Cuentas;
import com.servinte.axioma.orm.CuentasHome;

public class NotasAdministrativasDelegate extends NotasAdministrativasHome {
	

	/**
	 * 	Retorna todas las notas
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<NotasAdministrativas> listarNotas(Integer ingreso)
	{
		
		return (ArrayList<NotasAdministrativas>) sessionFactory.getCurrentSession()
				.createCriteria(NotasAdministrativas.class)
				.add(Expression.eq("ingresos.id", ingreso)).addOrder(Order.desc("fechaModifica")).addOrder(Order.desc("horaModifica"))
				.list();
	}
	
	
	/**
	 * 	Cuentas de todas las vías de ingreso.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Cuentas> verificarCuentasPorIngreso (int viaIngreso){
		
		return (ArrayList<Cuentas>) sessionFactory.getCurrentSession()
				.createCriteria(Cuentas.class)
				.add(Expression.eq("viasIngreso.codigo",viaIngreso))
				.list();
	}
	
	
	
	
}






	

