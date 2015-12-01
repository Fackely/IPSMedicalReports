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
 * Home object for domain model class HisConfPlanTratamiento.
 * @see com.servinte.axioma.orm.HisConfPlanTratamiento
 * @author Hibernate Tools
 */
public class HisConfPlanTratamientoHome {

	private static final Log log = LogFactory
			.getLog(HisConfPlanTratamientoHome.class);

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

	public void persist(HisConfPlanTratamiento transientInstance) {
		log.debug("persisting HisConfPlanTratamiento instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HisConfPlanTratamiento instance) {
		log.debug("attaching dirty HisConfPlanTratamiento instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HisConfPlanTratamiento instance) {
		log.debug("attaching clean HisConfPlanTratamiento instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HisConfPlanTratamiento persistentInstance) {
		log.debug("deleting HisConfPlanTratamiento instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HisConfPlanTratamiento merge(HisConfPlanTratamiento detachedInstance) {
		log.debug("merging HisConfPlanTratamiento instance");
		try {
			HisConfPlanTratamiento result = (HisConfPlanTratamiento) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HisConfPlanTratamiento findById(long id) {
		log.debug("getting HisConfPlanTratamiento instance with id: " + id);
		try {
			HisConfPlanTratamiento instance = (HisConfPlanTratamiento) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.HisConfPlanTratamiento",
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

	public List findByExample(HisConfPlanTratamiento instance) {
		log.debug("finding HisConfPlanTratamiento instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HisConfPlanTratamiento").add(
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
