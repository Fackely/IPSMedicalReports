package com.servinte.axioma.orm;

// Generated Jul 27, 2010 11:58:55 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class PresuPlanTtoProgSer.
 * @see com.servinte.axioma.orm.PresuPlanTtoProgSer
 * @author Hibernate Tools
 */
public class PresuPlanTtoProgSerHome {

	private static final Log log = LogFactory
			.getLog(PresuPlanTtoProgSerHome.class);

	private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

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

	public void persist(PresuPlanTtoProgSer transientInstance) {
		log.debug("persisting PresuPlanTtoProgSer instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PresuPlanTtoProgSer instance) {
		log.debug("attaching dirty PresuPlanTtoProgSer instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PresuPlanTtoProgSer instance) {
		log.debug("attaching clean PresuPlanTtoProgSer instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PresuPlanTtoProgSer persistentInstance) {
		log.debug("deleting PresuPlanTtoProgSer instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PresuPlanTtoProgSer merge(PresuPlanTtoProgSer detachedInstance) {
		log.debug("merging PresuPlanTtoProgSer instance");
		try {
			PresuPlanTtoProgSer result = (PresuPlanTtoProgSer) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PresuPlanTtoProgSer findById(long id) {
		log.debug("getting PresuPlanTtoProgSer instance with id: " + id);
		try {
			PresuPlanTtoProgSer instance = (PresuPlanTtoProgSer) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.PresuPlanTtoProgSer", id);
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

	public List findByExample(PresuPlanTtoProgSer instance) {
		log.debug("finding PresuPlanTtoProgSer instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.PresuPlanTtoProgSer").add(
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
