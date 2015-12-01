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
 * Home object for domain model class CancelacionAgendaOdo.
 * @see com.servinte.axioma.orm.CancelacionAgendaOdo
 * @author Hibernate Tools
 */
public class CancelacionAgendaOdoHome {

	private static final Log log = LogFactory
			.getLog(CancelacionAgendaOdoHome.class);

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

	public void persist(CancelacionAgendaOdo transientInstance) {
		log.debug("persisting CancelacionAgendaOdo instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(CancelacionAgendaOdo instance) {
		log.debug("attaching dirty CancelacionAgendaOdo instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CancelacionAgendaOdo instance) {
		log.debug("attaching clean CancelacionAgendaOdo instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CancelacionAgendaOdo persistentInstance) {
		log.debug("deleting CancelacionAgendaOdo instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CancelacionAgendaOdo merge(CancelacionAgendaOdo detachedInstance) {
		log.debug("merging CancelacionAgendaOdo instance");
		try {
			CancelacionAgendaOdo result = (CancelacionAgendaOdo) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public CancelacionAgendaOdo findById(long id) {
		log.debug("getting CancelacionAgendaOdo instance with id: " + id);
		try {
			CancelacionAgendaOdo instance = (CancelacionAgendaOdo) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.CancelacionAgendaOdo", id);
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

	public List findByExample(CancelacionAgendaOdo instance) {
		log.debug("finding CancelacionAgendaOdo instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.CancelacionAgendaOdo").add(
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
