package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:55 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class FuncionalidadesRestriccion.
 * @see com.servinte.axioma.orm.FuncionalidadesRestriccion
 * @author Hibernate Tools
 */
public class FuncionalidadesRestriccionHome {

	private static final Log log = LogFactory
			.getLog(FuncionalidadesRestriccionHome.class);

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

	public void persist(FuncionalidadesRestriccion transientInstance) {
		log.debug("persisting FuncionalidadesRestriccion instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(FuncionalidadesRestriccion instance) {
		log.debug("attaching dirty FuncionalidadesRestriccion instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(FuncionalidadesRestriccion instance) {
		log.debug("attaching clean FuncionalidadesRestriccion instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(FuncionalidadesRestriccion persistentInstance) {
		log.debug("deleting FuncionalidadesRestriccion instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public FuncionalidadesRestriccion merge(
			FuncionalidadesRestriccion detachedInstance) {
		log.debug("merging FuncionalidadesRestriccion instance");
		try {
			FuncionalidadesRestriccion result = (FuncionalidadesRestriccion) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public FuncionalidadesRestriccion findById(int id) {
		log.debug("getting FuncionalidadesRestriccion instance with id: " + id);
		try {
			FuncionalidadesRestriccion instance = (FuncionalidadesRestriccion) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.FuncionalidadesRestriccion",
							id);
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

	public List findByExample(FuncionalidadesRestriccion instance) {
		log.debug("finding FuncionalidadesRestriccion instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.FuncionalidadesRestriccion").add(
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
