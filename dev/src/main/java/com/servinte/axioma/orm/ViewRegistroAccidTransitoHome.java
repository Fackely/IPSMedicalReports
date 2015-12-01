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
 * Home object for domain model class ViewRegistroAccidTransito.
 * @see com.servinte.axioma.orm.ViewRegistroAccidTransito
 * @author Hibernate Tools
 */
public class ViewRegistroAccidTransitoHome {

	private static final Log log = LogFactory
			.getLog(ViewRegistroAccidTransitoHome.class);

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

	public void persist(ViewRegistroAccidTransito transientInstance) {
		log.debug("persisting ViewRegistroAccidTransito instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ViewRegistroAccidTransito instance) {
		log.debug("attaching dirty ViewRegistroAccidTransito instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ViewRegistroAccidTransito instance) {
		log.debug("attaching clean ViewRegistroAccidTransito instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ViewRegistroAccidTransito persistentInstance) {
		log.debug("deleting ViewRegistroAccidTransito instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ViewRegistroAccidTransito merge(
			ViewRegistroAccidTransito detachedInstance) {
		log.debug("merging ViewRegistroAccidTransito instance");
		try {
			ViewRegistroAccidTransito result = (ViewRegistroAccidTransito) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ViewRegistroAccidTransito findById(
			com.servinte.axioma.orm.ViewRegistroAccidTransitoId id) {
		log.debug("getting ViewRegistroAccidTransito instance with id: " + id);
		try {
			ViewRegistroAccidTransito instance = (ViewRegistroAccidTransito) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.ViewRegistroAccidTransito",
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

	public List findByExample(ViewRegistroAccidTransito instance) {
		log.debug("finding ViewRegistroAccidTransito instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ViewRegistroAccidTransito").add(
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
