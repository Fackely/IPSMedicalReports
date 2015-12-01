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
 * Home object for domain model class IndCalidadEspecialidad.
 * @see com.servinte.axioma.orm.IndCalidadEspecialidad
 * @author Hibernate Tools
 */
public class IndCalidadEspecialidadHome {

	private static final Log log = LogFactory
			.getLog(IndCalidadEspecialidadHome.class);

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

	public void persist(IndCalidadEspecialidad transientInstance) {
		log.debug("persisting IndCalidadEspecialidad instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(IndCalidadEspecialidad instance) {
		log.debug("attaching dirty IndCalidadEspecialidad instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(IndCalidadEspecialidad instance) {
		log.debug("attaching clean IndCalidadEspecialidad instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(IndCalidadEspecialidad persistentInstance) {
		log.debug("deleting IndCalidadEspecialidad instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public IndCalidadEspecialidad merge(IndCalidadEspecialidad detachedInstance) {
		log.debug("merging IndCalidadEspecialidad instance");
		try {
			IndCalidadEspecialidad result = (IndCalidadEspecialidad) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public IndCalidadEspecialidad findById(long id) {
		log.debug("getting IndCalidadEspecialidad instance with id: " + id);
		try {
			IndCalidadEspecialidad instance = (IndCalidadEspecialidad) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.IndCalidadEspecialidad",
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

	public List findByExample(IndCalidadEspecialidad instance) {
		log.debug("finding IndCalidadEspecialidad instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.IndCalidadEspecialidad").add(
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
