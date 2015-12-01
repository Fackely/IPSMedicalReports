package com.servinte.axioma.orm;

// Generated Nov 25, 2010 5:24:09 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class HistoricoEncabezado.
 * @see com.servinte.axioma.orm.HistoricoEncabezado
 * @author Hibernate Tools
 */
public class HistoricoEncabezadoHome {

	private static final Log log = LogFactory
			.getLog(HistoricoEncabezadoHome.class);
	
	protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
					.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(HistoricoEncabezado transientInstance) {
		log.debug("persisting HistoricoEncabezado instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HistoricoEncabezado instance) {
		log.debug("attaching dirty HistoricoEncabezado instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HistoricoEncabezado instance) {
		log.debug("attaching clean HistoricoEncabezado instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HistoricoEncabezado persistentInstance) {
		log.debug("deleting HistoricoEncabezado instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HistoricoEncabezado merge(HistoricoEncabezado detachedInstance) {
		log.debug("merging HistoricoEncabezado instance");
		try {
			HistoricoEncabezado result = (HistoricoEncabezado) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HistoricoEncabezado findById(long id) {
		log.debug("getting HistoricoEncabezado instance with id: " + id);
		try {
			HistoricoEncabezado instance = (HistoricoEncabezado) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.HistoricoEncabezado", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(HistoricoEncabezado instance) {
		log.debug("finding HistoricoEncabezado instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HistoricoEncabezado").add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
