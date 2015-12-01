package com.servinte.axioma.orm;

// Generated May 10, 2010 11:37:05 AM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ProgramasHallazgoPieza.
 * @see com.servinte.axioma.orm.ProgramasHallazgoPieza
 * @author Hibernate Tools
 */
public class ProgramasHallazgoPiezaHome {

	private static final Log log = LogFactory
			.getLog(ProgramasHallazgoPiezaHome.class);

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

	public void persist(ProgramasHallazgoPieza transientInstance) {
		log.debug("persisting ProgramasHallazgoPieza instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ProgramasHallazgoPieza instance) {
		log.debug("attaching dirty ProgramasHallazgoPieza instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ProgramasHallazgoPieza instance) {
		log.debug("attaching clean ProgramasHallazgoPieza instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ProgramasHallazgoPieza persistentInstance) {
		log.debug("deleting ProgramasHallazgoPieza instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ProgramasHallazgoPieza merge(ProgramasHallazgoPieza detachedInstance) {
		log.debug("merging ProgramasHallazgoPieza instance");
		try {
			ProgramasHallazgoPieza result = (ProgramasHallazgoPieza) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ProgramasHallazgoPieza findById(long id) {
		log.debug("getting ProgramasHallazgoPieza instance with id: " + id);
		try {
			ProgramasHallazgoPieza instance = (ProgramasHallazgoPieza) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ProgramasHallazgoPieza",
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

	public List findByExample(ProgramasHallazgoPieza instance) {
		log.debug("finding ProgramasHallazgoPieza instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ProgramasHallazgoPieza").add(
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
