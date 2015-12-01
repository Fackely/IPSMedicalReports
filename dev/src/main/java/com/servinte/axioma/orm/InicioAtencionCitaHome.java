package com.servinte.axioma.orm;

// Generated Nov 17, 2010 5:12:44 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class InicioAtencionCita.
 * @see com.servinte.axioma.orm.InicioAtencionCita
 * @author Hibernate Tools
 */
public class InicioAtencionCitaHome {

	private static final Log log = LogFactory
			.getLog(InicioAtencionCitaHome.class);

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

	public void persist(InicioAtencionCita transientInstance) {
		log.debug("persisting InicioAtencionCita instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(InicioAtencionCita instance) {
		log.debug("attaching dirty InicioAtencionCita instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(InicioAtencionCita instance) {
		log.debug("attaching clean InicioAtencionCita instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(InicioAtencionCita persistentInstance) {
		log.debug("deleting InicioAtencionCita instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public InicioAtencionCita merge(InicioAtencionCita detachedInstance) {
		log.debug("merging InicioAtencionCita instance");
		try {
			InicioAtencionCita result = (InicioAtencionCita) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public InicioAtencionCita findById(long id) {
		log.debug("getting InicioAtencionCita instance with id: " + id);
		try {
			InicioAtencionCita instance = (InicioAtencionCita) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.InicioAtencionCita", id);
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

	public List findByExample(InicioAtencionCita instance) {
		log.debug("finding InicioAtencionCita instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.InicioAtencionCita").add(
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
