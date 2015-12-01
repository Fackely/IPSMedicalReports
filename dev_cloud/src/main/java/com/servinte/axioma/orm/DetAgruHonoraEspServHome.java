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
 * Home object for domain model class DetAgruHonoraEspServ.
 * @see com.servinte.axioma.orm.DetAgruHonoraEspServ
 * @author Hibernate Tools
 */
public class DetAgruHonoraEspServHome {

	private static final Log log = LogFactory
			.getLog(DetAgruHonoraEspServHome.class);

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

	public void persist(DetAgruHonoraEspServ transientInstance) {
		log.debug("persisting DetAgruHonoraEspServ instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DetAgruHonoraEspServ instance) {
		log.debug("attaching dirty DetAgruHonoraEspServ instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetAgruHonoraEspServ instance) {
		log.debug("attaching clean DetAgruHonoraEspServ instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DetAgruHonoraEspServ persistentInstance) {
		log.debug("deleting DetAgruHonoraEspServ instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetAgruHonoraEspServ merge(DetAgruHonoraEspServ detachedInstance) {
		log.debug("merging DetAgruHonoraEspServ instance");
		try {
			DetAgruHonoraEspServ result = (DetAgruHonoraEspServ) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DetAgruHonoraEspServ findById(long id) {
		log.debug("getting DetAgruHonoraEspServ instance with id: " + id);
		try {
			DetAgruHonoraEspServ instance = (DetAgruHonoraEspServ) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.DetAgruHonoraEspServ", id);
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

	public List findByExample(DetAgruHonoraEspServ instance) {
		log.debug("finding DetAgruHonoraEspServ instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.DetAgruHonoraEspServ").add(
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
