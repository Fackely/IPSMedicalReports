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

import com.princetonsa.dto.manejoPaciente.DTOAutorizacionIngresoEstancia;
import com.servinte.axioma.orm.HistoAutorizacionIngEstan;
import com.servinte.axioma.orm.HistoAutorizacionIngEstanHome;

public class HistoAutorizacionIngEstanciaSubDelegate extends HistoAutorizacionIngEstanHome{

	/**
	 * Este Método se encarga de consultar por el ID las autorizaciones historicas de
	 * Ingreso Estancia 
	 * 
	 * @param ArrayList<DTOAutorEntidadSubcontratadaCapitacion> dto
	 * @return 
	 * @author Camilo Gomez
	 */		
	public ArrayList<DTOAutorizacionIngresoEstancia> obtenerHistoAutorizacionIngEstanciaSubDelegate(DTOAutorizacionIngresoEstancia dto){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				HistoAutorizacionIngEstan.class, "histoAutorizacionIngEstan");		
		
		criteria.createAlias("histoAutorizacionIngEstan.autorizacionesIngreEstancia","autorizacionesIngEstancia");
		criteria.createAlias("autorizacionesIngEstancia.ingresosEstancia","ingresoEstancia");
		criteria.createAlias("ingresoEstancia.entidadesSubcontratadas","entidadSubcontratada");
		criteria.createAlias("histoAutorizacionIngEstan.usuarios","usuario");
		
		criteria.add(Restrictions.eq("autorizacionesIngEstancia.codigoPk", dto.getCodigoPk()));
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()											
				.add(Projections.property("histoAutorizacionIngEstan.codigoPk")					,"codigoPk")
				.add(Projections.property("usuario.login")										,"login")
				.add(Projections.property("histoAutorizacionIngEstan.fechaInicioAutorizacion")	,"fechaInicioAutorizacion")
				.add(Projections.property("histoAutorizacionIngEstan.observaciones")			,"observaciones")
				.add(Projections.property("histoAutorizacionIngEstan.indicativoTemporal")		,"indicativoTemporal")
				.add(Projections.property("histoAutorizacionIngEstan.estado")					,"estado")
				.add(Projections.property("histoAutorizacionIngEstan.fechaModifica")			,"fechaModifica")				
				.add(Projections.property("histoAutorizacionIngEstan.horaModifica")				,"horaModifica")				
				.add(Projections.property("histoAutorizacionIngEstan.accionRealizada")			,"accionRealizada")
				/*.add(Projections.property("entidadSubcontratada.razonSocial")			,"descripcionEntidad")
				.add(Projections.property("entidadSubcontratada.direccion")			,"direccionEntidad")
				.add(Projections.property("entidadSubcontratada.telefono")			,"telefonoEntidad")*/
				
				//De esta tabla se cargan los datos de la entidad OTRA cuando es autorización temporal
				.add(Projections.property("ingresoEstancia.descripcionEntidadSub")			,"descripcionEntidad")
				.add(Projections.property("ingresoEstancia.direccionEntidadSub")			,"direccionEntidad")
				.add(Projections.property("ingresoEstancia.telefonoEntidadSub")			,"telefonoEntidad")
				
				.add(Projections.property("histoAutorizacionIngEstan.diasEstanciaAutorizados"), "numDiasAutorizados")
				
		));
		criteria.addOrder(Order.desc("histoAutorizacionIngEstan.fechaModifica"));
		criteria.addOrder(Order.desc("histoAutorizacionIngEstan.horaModifica"));
		
				
		Class[] parametros=new Class[13];
		parametros[0]=long.class;
		parametros[1]=String.class;
		parametros[2]=Date.class;
		parametros[3]=String.class;
		parametros[4]=char.class;
		parametros[5]=String.class;
		parametros[6]=Date.class;	
		parametros[7]=String.class;
		parametros[8]=String.class;
		parametros[9]=String.class;
		parametros[10]=String.class;
		parametros[11]=String.class;
		parametros[12]=int.class;
	
		Constructor constructor;
		try {
			constructor = DTOAutorizacionIngresoEstancia.class.getConstructor(parametros);
			criteria.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
			
		} catch (SecurityException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
		} catch (NoSuchMethodException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
			e.printStackTrace();
		}	
		
		@SuppressWarnings("unchecked")
		ArrayList<DTOAutorizacionIngresoEstancia> autorizacion = (ArrayList<DTOAutorizacionIngresoEstancia>)criteria.list();
		
		return autorizacion;	
	}
	
}
