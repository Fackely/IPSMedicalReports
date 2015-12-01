package com.servinte.axioma.orm;

// Generated Dec 7, 2010 9:11:49 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class RegistroEntregaEntsubPedido.
 * @see com.servinte.axioma.orm.RegistroEntregaEntsubPedido
 * @author Hibernate Tools
 */
public class RegistroEntregaEntsubPedidoHome {

	private static final Log log = LogFactory
			.getLog(RegistroEntregaEntsubPedidoHome.class);

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

	public void persist(RegistroEntregaEntsubPedido transientInstance) {
		log.debug("persisting RegistroEntregaEntsubPedido instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(RegistroEntregaEntsubPedido instance) {
		log.debug("attaching dirty RegistroEntregaEntsubPedido instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RegistroEntregaEntsubPedido instance) {
		log.debug("attaching clean RegistroEntregaEntsubPedido instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(RegistroEntregaEntsubPedido persistentInstance) {
		log.debug("deleting RegistroEntregaEntsubPedido instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RegistroEntregaEntsubPedido merge(
			RegistroEntregaEntsubPedido detachedInstance) {
		log.debug("merging RegistroEntregaEntsubPedido instance");
		try {
			RegistroEntregaEntsubPedido result = (RegistroEntregaEntsubPedido) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public RegistroEntregaEntsubPedido findById(long id) {
		log
				.debug("getting RegistroEntregaEntsubPedido instance with id: "
						+ id);
		try {
			RegistroEntregaEntsubPedido instance = (RegistroEntregaEntsubPedido) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.RegistroEntregaEntsubPedido",
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

	public List findByExample(RegistroEntregaEntsubPedido instance) {
		log.debug("finding RegistroEntregaEntsubPedido instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.RegistroEntregaEntsubPedido").add(
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
