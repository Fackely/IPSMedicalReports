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
 * Home object for domain model class CtTurnoGeneral.
 * @see com.servinte.axioma.orm.CtTurnoGeneral
 * @author Hibernate Tools
 */
public class CtTurnoGeneralHome {

	private static final Log log = LogFactory.getLog(CtTurnoGeneralHome.class);

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

	public void persist(CtTurnoGeneral transientInstance) {
		log.debug("persisting CtTurnoGeneral instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(CtTurnoGeneral instance) {
		log.debug("attaching dirty CtTurnoGeneral instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CtTurnoGeneral instance) {
		log.debug("attaching clean CtTurnoGeneral instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CtTurnoGeneral persistentInstance) {
		log.debug("deleting CtTurnoGeneral instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CtTurnoGeneral merge(CtTurnoGeneral detachedInstance) {
		log.debug("merging CtTurnoGeneral instance");
		try {
			CtTurnoGeneral result = (CtTurnoGeneral) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public CtTurnoGeneral findById(int id) {
		log.debug("getting CtTurnoGeneral instance with id: " + id);
		try {
			CtTurnoGeneral instance = (CtTurnoGeneral) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.CtTurnoGeneral", id);
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

	public List findByExample(CtTurnoGeneral instance) {
		log.debug("finding CtTurnoGeneral instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.CtTurnoGeneral").add(
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
