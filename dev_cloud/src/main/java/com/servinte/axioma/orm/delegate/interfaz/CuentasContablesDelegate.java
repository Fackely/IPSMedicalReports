package com.servinte.axioma.orm.delegate.interfaz;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.CuentasContables;
import com.servinte.axioma.orm.CuentasContablesHome;


/**
 * 
 * @author 
 *
 */
public class CuentasContablesDelegate extends CuentasContablesHome {
	
	
	

	/**
	 * CONSULTAR LAS CUENTAS CONTABLES POR UNA LISTA DE CODIGOS
	 * RETORNA UN ARRAYLIST DE CUENTAS CONTABLES
	 * RECIBE UNA LISTA DE CODIGOS  
	 * @param listaCodigoCentroAtencion
	 */
	public ArrayList<CuentasContables> consultarCuentasContables(ArrayList<Long> listaCuentas)
	{
		ArrayList<CuentasContables> newlistaCuentas = new ArrayList<CuentasContables>();
		
		if(listaCuentas.size()>0)
		{
			HibernateUtil.beginTransaction();
				Criteria criteriosList= sessionFactory.getCurrentSession()
				.createCriteria(CuentasContables.class).add(Expression.in("codigo", listaCuentas));
				newlistaCuentas= (ArrayList<CuentasContables>)criteriosList.list();
			HibernateUtil.endTransaction();
		}
		
		return newlistaCuentas;
	}
	
	/*
	
	public CuentasContables findById(long id)
	{
		CuentasContables cuentasContables=new CuentasContables();
		
		cuentasContables.setCodigo(((CuentasContables)
				this.sessionFactory.getCurrentSession().
				createCriteria(CuentasContables.class).
				add(Expression.eq("codigo", id)).
				uniqueResult()).getCodigo());
		return cuentasContables;
	}*/
	

}
