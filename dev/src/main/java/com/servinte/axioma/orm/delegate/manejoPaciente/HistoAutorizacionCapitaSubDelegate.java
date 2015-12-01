package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;

import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.servinte.axioma.orm.HistoAutorizacionCapitaSub;
import com.servinte.axioma.orm.HistoAutorizacionCapitaSubHome;

/**
 * Esta clase se encarga de ejecutar las consultas sobre 
 * la entidad  HistoAutorizacionCapitaSub
 * 
 * @author Angela Maria Aguirre
 * @since 17/12/2010
 */
public class HistoAutorizacionCapitaSubDelegate extends
		HistoAutorizacionCapitaSubHome {
	
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro del historial de autorización de capitación subcontratada
	 * 
	 * @param AutorizacionesCapitacionSub autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarAutorizacionEntidadSubcontratada(HistoAutorizacionCapitaSub historial){
		/**
		 * Se elimina la captura de la excepción en este nivel, ya que no se manejan excepciones por cada transacción
		 * de esta manera se deja la captura de la excepción en el mundo.
		 */
		boolean save = true;					
		super.persist(historial);
		return save;				
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar por el ID el historico de las 
	 * autorizaciones de entidades subcontratadas y su respectiva
	 * autorización de capitación
	 * 
	 * @param DTOAutorEntidadSubcontratadaCapitacion dto
	 * @return ArrayList<DTOAutorEntidadSubcontratadaCapitacion>
	 * @author, Camilo Gomez 
	 *
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<DTOAutorEntidadSubcontratadaCapitacion> obtenerHistoricoAutorizacionEntidadSubCapitacionPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto){
				
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				HistoAutorizacionCapitaSub.class, "histoAutorizacionCapitaSubs");		
		
		criteria.createAlias("histoAutorizacionCapitaSubs.autorizacionesCapitacionSub","autorizacionesCapitacionSub");
		criteria.createAlias("histoAutorizacionCapitaSubs.usuarios","usuario");
		
		criteria.add(Restrictions.eq("autorizacionesCapitacionSub.codigoPk", dto.getAutorCapitacion().getCodigoPK()));
				
		criteria.setProjection(Projections.distinct(Projections.projectionList()											
				.add(Projections.property("histoAutorizacionCapitaSubs.codigoPk"),"codigoPk")
				.add(Projections.property("usuario.login"),"login")
				.add(Projections.property("histoAutorizacionCapitaSubs.indicativoTemporal")	,"indicativoTemporal")
				.add(Projections.property("histoAutorizacionCapitaSubs.descripcionEntidad")	,"descripcionEntidad")
				.add(Projections.property("histoAutorizacionCapitaSubs.direccionEntidad")	,"direccionEntidad")
				.add(Projections.property("histoAutorizacionCapitaSubs.telefonoEntidad")	,"telefonoEntidad")
				.add(Projections.property("histoAutorizacionCapitaSubs.fechaModifica")		,"fechaModifica")				
				.add(Projections.property("histoAutorizacionCapitaSubs.horaModifica")		,"horaModifica")				
				.add(Projections.property("histoAutorizacionCapitaSubs.accionRealizada")	,"accionRealizada")
				.add(Projections.property("histoAutorizacionCapitaSubs.observaciones")		,"observaciones")
				.add(Projections.property("histoAutorizacionCapitaSubs.fechaVencimiento")	,"fechaVencimiento")				
		));		
		
		criteria.addOrder(Order.asc("histoAutorizacionCapitaSubs.fechaModifica"));
		criteria.addOrder(Order.asc("histoAutorizacionCapitaSubs.horaModifica"));
		
		Class[] parametros=new Class[11];
		parametros[0]=long.class;
		parametros[1]=String.class;
		parametros[2]=char.class;
		parametros[3]=String.class;
		parametros[4]=String.class;
		parametros[5]=String.class;
		parametros[6]=Date.class;	
		parametros[7]=String.class;
		parametros[8]=String.class;
		parametros[9]=String.class;
		parametros[10]=Date.class;

		
		Constructor constructor;
		try {
			constructor = DTOAutorEntidadSubcontratadaCapitacion.class.getConstructor(parametros);
			criteria.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
			
		} catch (SecurityException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
		} catch (NoSuchMethodException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
			e.printStackTrace();
		}	
		
		@SuppressWarnings("unchecked")
		ArrayList<DTOAutorEntidadSubcontratadaCapitacion> autorizacion = (ArrayList<DTOAutorEntidadSubcontratadaCapitacion>)criteria.list();//uniqueResult();
		
		return autorizacion;	
	}
	

}
