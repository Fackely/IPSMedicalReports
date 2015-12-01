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
 * Home object for domain model class ViewCensoHsuba.
 * @see com.servinte.axioma.orm.ViewCensoHsuba
 * @author Hibernate Tools
 */
public class ViewCensoHsubaHome {

	private static final Log log = LogFactory.getLog(ViewCensoHsubaHome.class);

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

	public void persist(ViewCensoHsuba transientInstance) {
		log.debug("persisting ViewCensoHsuba instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ViewCensoHsuba instance) {
		log.debug("attaching dirty ViewCensoHsuba instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ViewCensoHsuba instance) {
		log.debug("attaching clean ViewCensoHsuba instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ViewCensoHsuba persistentInstance) {
		log.debug("deleting ViewCensoHsuba instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ViewCensoHsuba merge(ViewCensoHsuba detachedInstance) {
		log.debug("merging ViewCensoHsuba instance");
		try {
			ViewCensoHsuba result = (ViewCensoHsuba) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ViewCensoHsuba findById(com.servinte.axioma.orm.ViewCensoHsubaId id) {
		log.debug("getting ViewCensoHsuba instance with id: " + id);
		try {
			ViewCensoHsuba instance = (ViewCensoHsuba) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ViewCensoHsuba", id);
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

	public List findByExample(ViewCensoHsuba instance) {
		log.debug("finding ViewCensoHsuba instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ViewCensoHsuba").add(
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
