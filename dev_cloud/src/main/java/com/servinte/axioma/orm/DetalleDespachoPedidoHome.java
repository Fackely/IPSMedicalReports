package com.servinte.axioma.orm;

// Generated Dec 1, 2010 10:58:29 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class DetalleDespachoPedido.
 * @see com.servinte.axioma.orm.DetalleDespachoPedido
 * @author Hibernate Tools
 */
public class DetalleDespachoPedidoHome {

	private static final Log log = LogFactory
			.getLog(DetalleDespachoPedidoHome.class);

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

	public void persist(DetalleDespachoPedido transientInstance) {
		log.debug("persisting DetalleDespachoPedido instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DetalleDespachoPedido instance) {
		log.debug("attaching dirty DetalleDespachoPedido instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetalleDespachoPedido instance) {
		log.debug("attaching clean DetalleDespachoPedido instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DetalleDespachoPedido persistentInstance) {
		log.debug("deleting DetalleDespachoPedido instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetalleDespachoPedido merge(DetalleDespachoPedido detachedInstance) {
		log.debug("merging DetalleDespachoPedido instance");
		try {
			DetalleDespachoPedido result = (DetalleDespachoPedido) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DetalleDespachoPedido findById(
			com.servinte.axioma.orm.DetalleDespachoPedidoId id) {
		log.debug("getting DetalleDespachoPedido instance with id: " + id);
		try {
			DetalleDespachoPedido instance = (DetalleDespachoPedido) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.DetalleDespachoPedido", id);
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

	public List findByExample(DetalleDespachoPedido instance) {
		log.debug("finding DetalleDespachoPedido instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.DetalleDespachoPedido").add(
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
