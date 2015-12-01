package com.servinte.axioma.fwk.persistencia.servicio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Clase que implementa los servicios de integración con la base de datos haciendo
 * uso de Hibernate
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:02 p.m.
 */
public class PersistenciaSvc implements IPersistenciaSvc{

	private SessionFactory session=HibernateUtil.getSessionFactory(false);
	
	public Session getSession(){
		return session.getCurrentSession();
	}
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc#createNamedQuery(java.lang.String)
	 */
	@Override
	public List<?> createNamedQuery(String namedQuery) {
		return getSession().getNamedQuery(namedQuery).list();
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc#createNamedQuery(java.lang.String, java.util.Map)
	 */
	@Override
	public List<?> createNamedQuery(String namedQuery, Map<String, Object> parameters) {
		Query query=getSession().getNamedQuery(namedQuery);
		asignarParameters(parameters, query);
		return query.list();
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc#createNamedQueryUniqueResult(java.lang.String)
	 */
	@Override
	public Object createNamedQueryUniqueResult(String namedQuery) {
		return getSession().getNamedQuery(namedQuery).uniqueResult();
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc#createNamedQueryUniqueResult(java.lang.String, java.util.Map)
	 */
	@Override
	public Object createNamedQueryUniqueResult(String namedQuery, Map<String, Object> parameters) {
		Query query=getSession().getNamedQuery(namedQuery);
		asignarParameters(parameters, query);
		return query.uniqueResult();
	}
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc#createNamedQueryFirstResult(java.lang.String, java.util.Map)
	 */
	@Override
	public Object createNamedQueryFirstResult(String namedQuery, Map<String, Object> parameters) {
		Query query=getSession().getNamedQuery(namedQuery);
		asignarParameters(parameters, query);
		query.setMaxResults(1);
		return query.uniqueResult();
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc#createNamedQueryFirstResult(java.lang.String)
	 */
	@Override
	public Object createNamedQueryFirstResult(String namedQuery) {
		Query query=getSession().getNamedQuery(namedQuery);
		query.setMaxResults(1);
		return query.uniqueResult();
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc#find(java.lang.Class, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		return (T) getSession().get(entityClass, (Serializable) primaryKey, LockMode.NONE);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc#flush()
	 */
	@Override
	public void flush() {
		getSession().flush();
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc#merge(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T merge(T entity) {
		T atachEntity = null;
		atachEntity = (T) getSession().merge(entity);
		return atachEntity;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc#persist(java.lang.Object)
	 */
	@Override
	public void persist(Object entity) {
		getSession().persist(entity);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc#createUpdateNamedQuery(java.lang.String)
	 */
	@Override
	public int createUpdateNamedQuery(String updateNamedQuery) throws IPSException {
		return getSession().getNamedQuery(updateNamedQuery).executeUpdate();
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc#createUpdateNamedQuery(java.lang.String, java.util.Map)
	 */
	@Override
	public int createUpdateNamedQuery(String updateNamedQuery, Map<String, Object> parameters)
			throws IPSException {
		Query query=getSession().getNamedQuery(updateNamedQuery);
		asignarParameters(parameters, query);
		return query.executeUpdate();
	}
	
	/**
	 * Metodo utilizado para asignar los parametros a un Query
	 * 
	 * @param parameters
	 * @param query
	 */
	private void asignarParameters(Map<String,Object> parameters, Query query) {
		if (parameters!= null && !parameters.isEmpty()) {
			for (Entry<String, ?> entry : parameters.entrySet()) {
				if (entry.getValue() instanceof String) {
					query.setParameter(entry.getKey(), entry.getValue(), Hibernate.STRING);
				} else if (entry.getValue() instanceof Integer) {
					query.setParameter(entry.getKey(), entry.getValue(), Hibernate.INTEGER);
				} else if (entry.getValue() instanceof BigDecimal) {
					query.setParameter(entry.getKey(), entry.getValue(), Hibernate.BIG_DECIMAL);
				} else if (entry.getValue() instanceof BigInteger) {
					query.setParameter(entry.getKey(), entry.getValue(), Hibernate.BIG_INTEGER);
				} else if (entry.getValue() instanceof Boolean) {
					query.setParameter(entry.getKey(), entry.getValue(), Hibernate.BOOLEAN);
				} else if (entry.getValue() instanceof Byte) {
					query.setParameter(entry.getKey(), entry.getValue(), Hibernate.BYTE);
				} else if (entry.getValue() instanceof char[]) {
					query.setParameter(entry.getKey(), entry.getValue(), Hibernate.CHAR_ARRAY);
				} else if (entry.getValue() instanceof Character) {
					query.setParameter(entry.getKey(), entry.getValue(), Hibernate.CHARACTER);
				} else if (entry.getValue() instanceof Character[]) {
					query.setParameter(entry.getKey(), entry.getValue(), Hibernate.CHARACTER_ARRAY);
				} else if (entry.getValue() instanceof Date) {
					query.setParameter(entry.getKey(), entry.getValue(), Hibernate.DATE);
				} else if (entry.getValue() instanceof Double) {
					query.setParameter(entry.getKey(), entry.getValue(), Hibernate.DOUBLE);
				} else if (entry.getValue() instanceof Float) {
					query.setParameter(entry.getKey(), entry.getValue(), Hibernate.FLOAT);
				} else if (entry.getValue() instanceof Long) {
					query.setParameter(entry.getKey(), entry.getValue(), Hibernate.LONG);
				} else if (entry.getValue() instanceof Short) {
					query.setParameter(entry.getKey(), entry.getValue(), Hibernate.SHORT);
				} else if (entry.getValue() instanceof List<?>) {
					query.setParameterList(entry.getKey(), (List<?>)entry.getValue());
				}
				else{
					query.setParameter(entry.getKey(), entry.getValue());
				} 
			}
		}
	}
	
}