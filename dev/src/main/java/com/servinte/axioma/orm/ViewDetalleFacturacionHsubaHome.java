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
 * Home object for domain model class ViewDetalleFacturacionHsuba.
 * @see com.servinte.axioma.orm.ViewDetalleFacturacionHsuba
 * @author Hibernate Tools
 */
public class ViewDetalleFacturacionHsubaHome {

	private static final Log log = LogFactory
			.getLog(ViewDetalleFacturacionHsubaHome.class);

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

	public void persist(ViewDetalleFacturacionHsuba transientInstance) {
		log.debug("persisting ViewDetalleFacturacionHsuba instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ViewDetalleFacturacionHsuba instance) {
		log.debug("attaching dirty ViewDetalleFacturacionHsuba instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ViewDetalleFacturacionHsuba instance) {
		log.debug("attaching clean ViewDetalleFacturacionHsuba instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ViewDetalleFacturacionHsuba persistentInstance) {
		log.debug("deleting ViewDetalleFacturacionHsuba instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ViewDetalleFacturacionHsuba merge(
			ViewDetalleFacturacionHsuba detachedInstance) {
		log.debug("merging ViewDetalleFacturacionHsuba instance");
		try {
			ViewDetalleFacturacionHsuba result = (ViewDetalleFacturacionHsuba) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ViewDetalleFacturacionHsuba findById(
			com.servinte.axioma.orm.ViewDetalleFacturacionHsubaId id) {
		log
				.debug("getting ViewDetalleFacturacionHsuba instance with id: "
						+ id);
		try {
			ViewDetalleFacturacionHsuba instance = (ViewDetalleFacturacionHsuba) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.ViewDetalleFacturacionHsuba",
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

	public List findByExample(ViewDetalleFacturacionHsuba instance) {
		log.debug("finding ViewDetalleFacturacionHsuba instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ViewDetalleFacturacionHsuba").add(
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
