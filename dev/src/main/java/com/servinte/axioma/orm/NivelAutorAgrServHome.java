package com.servinte.axioma.orm;

// Generated Jan 24, 2011 6:37:07 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class NivelAutorAgrServ.
 * @see com.servinte.axioma.orm.NivelAutorAgrServ
 * @author Hibernate Tools
 */
public class NivelAutorAgrServHome {

	private static final Log log = LogFactory
			.getLog(NivelAutorAgrServHome.class);

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

	public void persist(NivelAutorAgrServ transientInstance) {
		log.debug("persisting NivelAutorAgrServ instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(NivelAutorAgrServ instance) {
		log.debug("attaching dirty NivelAutorAgrServ instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(NivelAutorAgrServ instance) {
		log.debug("attaching clean NivelAutorAgrServ instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(NivelAutorAgrServ persistentInstance) {
		log.debug("deleting NivelAutorAgrServ instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public NivelAutorAgrServ merge(NivelAutorAgrServ detachedInstance) {
		log.debug("merging NivelAutorAgrServ instance");
		try {
			NivelAutorAgrServ result = (NivelAutorAgrServ) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public NivelAutorAgrServ findById(int id) {
		log.debug("getting NivelAutorAgrServ instance with id: " + id);
		try {
			NivelAutorAgrServ instance = (NivelAutorAgrServ) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.NivelAutorAgrServ", id);
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

	public List findByExample(NivelAutorAgrServ instance) {
		log.debug("finding NivelAutorAgrServ instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.NivelAutorAgrServ").add(
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
