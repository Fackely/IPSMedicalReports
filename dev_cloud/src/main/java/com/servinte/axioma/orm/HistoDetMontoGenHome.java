package com.servinte.axioma.orm;

// Generated Sep 3, 2010 2:49:57 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class HistoDetMontoGen.
 * @see com.servinte.axioma.orm.HistoDetMontoGen
 * @author Hibernate Tools
 */
public class HistoDetMontoGenHome {

	private static final Log log = LogFactory
			.getLog(HistoDetMontoGenHome.class);

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

	public void persist(HistoDetMontoGen transientInstance) {
		log.debug("persisting HistoDetMontoGen instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HistoDetMontoGen instance) {
		log.debug("attaching dirty HistoDetMontoGen instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HistoDetMontoGen instance) {
		log.debug("attaching clean HistoDetMontoGen instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HistoDetMontoGen persistentInstance) {
		log.debug("deleting HistoDetMontoGen instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HistoDetMontoGen merge(HistoDetMontoGen detachedInstance) {
		log.debug("merging HistoDetMontoGen instance");
		try {
			HistoDetMontoGen result = (HistoDetMontoGen) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HistoDetMontoGen findById(int id) {
		log.debug("getting HistoDetMontoGen instance with id: " + id);
		try {
			HistoDetMontoGen instance = (HistoDetMontoGen) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.HistoDetMontoGen", id);
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

	public List findByExample(HistoDetMontoGen instance) {
		log.debug("finding HistoDetMontoGen instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HistoDetMontoGen").add(
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
