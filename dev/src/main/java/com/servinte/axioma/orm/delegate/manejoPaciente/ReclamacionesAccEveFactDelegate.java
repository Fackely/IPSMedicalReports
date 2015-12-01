/**
 * 
 */
package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.servinte.axioma.dto.manejoPaciente.DtoFiltroBusquedaAvanzadaReclamaciones;
import com.servinte.axioma.orm.ComisionXCentroAtencion;
import com.servinte.axioma.orm.ReclamacionesAccEveFact;
import com.servinte.axioma.orm.ReclamacionesAccEveFactHome;

/**
 * @author axioma
 *
 */
public class ReclamacionesAccEveFactDelegate extends
		ReclamacionesAccEveFactHome {

	/**
	 * 
	 * @param codigoFactura
	 * @return
	 */
	public ArrayList<ReclamacionesAccEveFact> consultarReclamacionesFactura(
			int codigoFactura) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ReclamacionesAccEveFact.class);
		criteria.add(Restrictions.eq("facturas.codigo", codigoFactura));
		criteria.addOrder(Order.desc("fechaRaclamacion"));
		criteria.addOrder(Order.desc("horaReclamacion"));
		
		ArrayList<ReclamacionesAccEveFact> resultado=(ArrayList<ReclamacionesAccEveFact>)criteria.list();
		return resultado;
	}
	
	

	/**
	 * 
	 * @param codigoFactura
	 * @return
	 */
	public ArrayList<ReclamacionesAccEveFact> consultarReclamacionesEventoCatastrofico(
			int codigo,boolean todas) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ReclamacionesAccEveFact.class);
		criteria.add(Restrictions.eq("registroEventoCatastrofico.codigo",new Long(codigo)));
		if(!todas)
		{
			String[] estados={ConstantesIntegridadDominio.acronimoEstadoGenerado,ConstantesIntegridadDominio.acronimoRadicado};
			criteria.add(Restrictions.in("estado", estados));
		}
		criteria.addOrder(Order.desc("fechaRaclamacion"));
		criteria.addOrder(Order.desc("horaReclamacion"));
		
		ArrayList<ReclamacionesAccEveFact> resultado=(ArrayList<ReclamacionesAccEveFact>)criteria.list();
		return resultado;
	}
	
	

	/**
	 * 
	 * @param codigoFactura
	 * @return
	 */
	public ArrayList<ReclamacionesAccEveFact> consultarReclamacionesAccidenteTransito(
			int codigo,boolean todas) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ReclamacionesAccEveFact.class);
		criteria.add(Restrictions.eq("registroAccidentesTransito.codigo", new Long(codigo)));
		if(!todas)
		{
			String[] estados={ConstantesIntegridadDominio.acronimoEstadoGenerado,ConstantesIntegridadDominio.acronimoRadicado};
			criteria.add(Restrictions.in("estado", estados));
		}
		criteria.addOrder(Order.desc("fechaRaclamacion"));
		criteria.addOrder(Order.desc("horaReclamacion"));
		
		ArrayList<ReclamacionesAccEveFact> resultado=(ArrayList<ReclamacionesAccEveFact>)criteria.list();
		return resultado;
	}



	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public ArrayList<ReclamacionesAccEveFact> consultarReclamacionesBusquedaAvanzada(DtoFiltroBusquedaAvanzadaReclamaciones filtro) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ReclamacionesAccEveFact.class, "reclamaciones");
		if(!UtilidadTexto.isEmpty(filtro.getTipoEvento()))
		{
			criteria.add(Restrictions.eq("reclamaciones.tipoEvento", filtro.getTipoEvento()));
		}
		if(!UtilidadTexto.isEmpty(filtro.getTipoReclamacion()))
		{
			criteria.add(Restrictions.eq("reclamaciones.tipoReclamacion", filtro.getTipoReclamacion()));
		}
		if(!UtilidadTexto.isEmpty(filtro.getEstadoReclamacion()))
		{
			criteria.add(Restrictions.eq("reclamaciones.estado", filtro.getEstadoReclamacion()));
		}
		if(Utilidades.convertirAEntero(filtro.getConvenioResponsable())>0)
		{
			criteria.createAlias("reclamaciones.facturas", "facturas");
			criteria.createAlias("facturas.convenios", "convenio");
			criteria.add(Restrictions.eq("convenio.codigo", Utilidades.convertirAEntero(filtro.getConvenioResponsable())));
		}
		if(!UtilidadTexto.isEmpty(filtro.getFechaInicialReclamacion())&&!UtilidadTexto.isEmpty(filtro.getFechaFinalReclamacion()))
		{
			criteria.add(Restrictions.between("reclamaciones.fechaRaclamacion", UtilidadFecha.conversionFormatoFechaStringDate(filtro.getFechaInicialReclamacion()), UtilidadFecha.conversionFormatoFechaStringDate(filtro.getFechaFinalReclamacion())) );
		}
		if(!UtilidadTexto.isEmpty(filtro.getNroInicialReclamacion())&&!UtilidadTexto.isEmpty(filtro.getNroFinalReclamacion()))
		{
			criteria.add(Restrictions.between("reclamaciones.nroReclamacion", filtro.getNroInicialReclamacion(), filtro.getNroFinalReclamacion()) );
		}
		
		criteria.addOrder(Order.desc("reclamaciones.fechaRaclamacion"));
		criteria.addOrder(Order.desc("reclamaciones.horaReclamacion"));
		
		ArrayList<ReclamacionesAccEveFact> resultado=(ArrayList<ReclamacionesAccEveFact>)criteria.list();
		return resultado;
	}
}
