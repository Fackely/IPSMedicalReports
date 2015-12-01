package com.servinte.axioma.persistencia.impl.hibernate;


import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.persistencia.interfaz.ITransaccion;


/**
 * Encargada del manejo de las transacciones específicas de
 * Hibernate
 * @author Juan David Ramírez, Edgar Carvajal
 * @version 1.0.0
 * @since 23 Julio 2010
 */
public class TransaccionHibernate implements ITransaccion 
{
	@Override
	public void begin() 
	{
		HibernateUtil.beginTransaction();
	}
	
	@Override
	public void commit() 
	{
		HibernateUtil.endTransaction();
	}
	
	@Override
	public void rollback() 
	{
		HibernateUtil.abortTransaction();
	}
	
	@Override
	public boolean isActive()
	{
		return HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().isActive();
	}
	
	
	@Override
	public void flush(){
		HibernateUtil.flush();
	}

	@Override
	public boolean isSessionOpen()
	{
		return HibernateUtil.isSessionOpen();
	}
	
	
	
}
