package com.servinte.axioma.hibernate;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.CriteriaImpl.Subcriteria;
import org.hibernate.impl.SessionImpl;
import org.hibernate.loader.OuterJoinLoader;
import org.hibernate.loader.criteria.CriteriaLoader;
import org.hibernate.persister.entity.OuterJoinLoadable;

/**
 * Code <code>HibernateUtil</code> 
 * Utilidad para el manejo de las
 * conexiones a la BD a través de Hibernate
 * 
 * @author Juan David Ramírez
 * @version 1.0
 */
public class HibernateUtil implements ObjectFactory {

	/**
	 * Fábrica de rescursos de la BD
	 */
	private static final SessionFactory sessionFactory;
	private static Configuration configuration=new Configuration();
	
	static {
		
		try {
			
			// Construir el SessionFactory a partir del hibernate.cfg.xml
			sessionFactory = configuration.configure("hibernate-axioma.cfg.xml").buildSessionFactory();
			
			/*InitialContext contexto=new InitialContext();
			sessionFactory = (SessionFactory) contexto.lookup("java:comp/env/SessionFactory");*/
			}
		catch (Throwable ex) 
		{
			// Mostrar la excepción
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Obtener la fábrica de sesión
	 * @return SessionFactory asociado a la BD
	 */
	public static SessionFactory getSessionFactory() {
		beginTransaction();
		return sessionFactory;
	}

	/**
	 * 
	 * @param beginTransaction
	 * @return
	 */
	public static SessionFactory getSessionFactory(boolean beginTransaction) {
		if(beginTransaction)
		{
			beginTransaction();
		}
		return sessionFactory;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Transaction beginTransaction() 
	{
		if(!sessionFactory.getCurrentSession().getTransaction().isActive())
		{
			return sessionFactory.getCurrentSession().beginTransaction();
		}
		else
		{
			return sessionFactory.getCurrentSession().getTransaction();
		}
	}
	
	/**
	 * M&eacute;todo para abortar la transacci&oacuten;
	 */
	public static void abortTransaction() {
		if(sessionFactory.getCurrentSession().getTransaction().isActive()) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
		}	
		cerrarSession();
	}
	
	/**
	 * Obtiene la conexi&oacute;n creada por Hibernate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Connection obtenerConexion() 
	{
		if (!sessionFactory.getCurrentSession().getTransaction().isActive()) 
		{
			beginTransaction();
		}
		try
		{
			return sessionFactory.getCurrentSession().connection();
		} catch (HibernateException e)
		{
			Log4JManager.error("Error abriendo la conexión Hibernate",e);
		}
	/*	catch (SQLException e)
		{
			Log4JManager.info("Error abriendo la conexión Hibernate",e);
		}*/
		return null;
	}
	
	/**
	 * 
	 */
	public static void endTransaction()
	{
		
		try{
			if (sessionFactory.getCurrentSession().getTransaction().isActive()) 
			{
				sessionFactory.getCurrentSession().getTransaction().commit();
			}
			
			cerrarSession();
		}catch (Exception e) {
			Log4JManager.error("Error finalizando la transacción", e);
			throw new RuntimeException("Error finalizando la transacción");
		}
	}
	
	
	/**
	 * 
	 * 
	 */
	public static void cerrarSession()
	{
		if(sessionFactory!=null && sessionFactory.getCurrentSession()!=null)
		{
			sessionFactory.getCurrentSession().close();
		}
		else
		{
			throw new RuntimeException("Error cerrando la sesión");
		}
	}

	
	@SuppressWarnings("rawtypes")
	public Object getObjectInstance(Object obj, Name name, Context cntx,
			Hashtable env) throws NamingException {

		SessionFactory sessionFactory = null;
		RefAddr addr = null;

		try {
			Enumeration addrs = ((Reference) (obj)).getAll();

			while (addrs.hasMoreElements()) {
				addr = (RefAddr) addrs.nextElement();
				if ("configuration".equals((String) (addr.getType()))) {
					sessionFactory = (new Configuration()).configure(
							(String) addr.getContent()).buildSessionFactory();
				}
			}
		} catch (Exception ex) {
			throw new javax.naming.NamingException(ex.getMessage());
		}

		return sessionFactory;
	}
	
	
	
	/**
	 * Forza a la transaccion a que sea ejecutada
	 * 
	 */
	public static void flush()
	{
		if(sessionFactory!=null && sessionFactory.getCurrentSession()!=null)
		{
			sessionFactory.getCurrentSession().flush();
		}
		else
		{
			throw new RuntimeException("Error haciendo flush");
		}
	}

	/**
	 * Método para verificar que la sesión esté abierta
	 */
	public static boolean isSessionOpen() {
		return sessionFactory.getCurrentSession().isOpen();
	}

	/**
	 * Metodo encargado de obtener la consulta SQL que resulta de un objeto tipo Criteria
	 * @param criteria
	 * @return
	 */
	public static String getSql(Criteria criteria)
	{
		String sql = null;
		try
		{
			CriteriaImpl c = null;
			if(criteria instanceof CriteriaImpl)
			{
				c = (CriteriaImpl)criteria;  
			}
			else if(criteria instanceof Subcriteria)
			{
				c = (CriteriaImpl)((Subcriteria)criteria).getParent();
			}
			
	        SessionImpl s = (SessionImpl)c.getSession();  
	        SessionFactoryImplementor factory = (SessionFactoryImplementor)s.getSessionFactory();  
	        String[] implementors = factory.getImplementors( c.getEntityOrClassName() );  
	        CriteriaLoader loader = new CriteriaLoader((OuterJoinLoadable)factory.getEntityPersister(implementors[0]), factory, c, implementors[0], s.getEnabledFilters());  
	        Field f = OuterJoinLoader.class.getDeclaredField("sql");  
	        f.setAccessible(true);  
	        sql = (String)f.get(loader);  
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sql;
	}
}