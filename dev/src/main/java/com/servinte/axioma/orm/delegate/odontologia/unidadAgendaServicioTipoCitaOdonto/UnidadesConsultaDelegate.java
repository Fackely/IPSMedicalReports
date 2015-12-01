
package com.servinte.axioma.orm.delegate.odontologia.unidadAgendaServicioTipoCitaOdonto;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;

import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.UnidadesConsulta;
import com.servinte.axioma.orm.UnidadesConsultaHome;

/**
 * Clase que se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link UnidadesConsulta}.
 * 
 * @author Jorge Armando Agudelo Quintero
 */

public class UnidadesConsultaDelegate  extends UnidadesConsultaHome{

	/**
	 * M&eacute;todo que lista las Unidades de Agenda de un tipo y un estado espec&iacute;fico
	 * pasado como par&aacute;metro
	 * 
	 * @param tipoAtencion
	 * @param estado
	 * @return {@link List} con las Unidades de Agenda de un tipo y estado espec&iacute;fico
	 */
	@SuppressWarnings("unchecked")
	public List<UnidadesConsulta> listarUnidadesAgendaPorTipoPorEstado (String tipoAtencion, boolean estado){
	
		return (List<UnidadesConsulta>) sessionFactory.getCurrentSession()
		.createCriteria(UnidadesConsulta.class)
		.add(Restrictions.eq("activa", estado))
		.add(Restrictions.eq("tipoAtencion", tipoAtencion))
		.addOrder(Order.asc("descripcion"))
		.list();
	 
	}
	
	/**
	 * M&eacute;todo que retorna un listado con los Servicios asociados a una Unidad de Agenda espec&iacute;fica
	 * pasada como par&aacute;metro y teniendo en cuenta el estado.
	 * 
	 * @param codigoUnidadAgenda
	 * @param estado
	 * @return List<{@link DtoUnidadAgendaServCitaOdonto}> listado con los Servicios asociados a una Unidad de Agenda espec&iacute;fica.
	 */
	@SuppressWarnings("unchecked")
	public List<DtoUnidadAgendaServCitaOdonto> listarServiciosPorUnidadAgendaPorEstado (int codigoUnidadAgenda, boolean estado){
		
		return (List<DtoUnidadAgendaServCitaOdonto>) sessionFactory.getCurrentSession()
		.createCriteria(UnidadesConsulta.class)
		.createAlias("servicioses", "servicio")

		.setProjection(Projections.projectionList()
			  .add( Projections.property("codigo"), "codigoUnidadAgenda" )
			  .add( Projections.property("descripcion"), "descripcionUnidadAgenda")
			  .add( Projections.property("servicio.codigo"), "codigoServicio" )
	  		)
	  	.add(Restrictions.eq("codigo", codigoUnidadAgenda))
	  	.add(Restrictions.eq("servicio.activo", estado))
	  	.addOrder(Order.asc("descripcion"))
		.setResultTransformer(Transformers.aliasToBean(DtoUnidadAgendaServCitaOdonto.class))
		.list();	
	}
	
	
	/**
	 * M&eacute;todo que retorna un listado de Servicios con la informaci&oacute;n de la descripci&oacute;n 
	 * y el c&oacute;digo propietario por cada uno de los servicios asociados a un tipo tarifario espec&iacute;fico
	 * y a una Unidad de Agenda.
	 * 
	 * @param codigoUnidadAgenda
	 * @param codigoTarifario
	 * @param codigosServicio
	 * @return List<{@link DtoUnidadAgendaServCitaOdonto}> listado de Servicios con informaci&oacute;n espec&iacute;fica de c&oacute;digo y descripci&oacute;n
	 */
	@SuppressWarnings("unchecked")
	public List<DtoUnidadAgendaServCitaOdonto> obtenerInformacionServiciosPorUnidadAgendaPorTipoTarifario (int codigoUnidadAgenda, int codigoTarifario, ArrayList<Integer> codigosServicio){
		
		return (List<DtoUnidadAgendaServCitaOdonto>) sessionFactory.getCurrentSession()
		.createCriteria(UnidadesConsulta.class)
		.createAlias("servicioses", "servicio")
		.createAlias("servicio.referenciasServicios", "refServicio")
		.createAlias("refServicio.tarifariosOficiales", "tarifariosOficiales")

		.setProjection(Projections.projectionList()
			  .add( Projections.property("codigo"), "codigoUnidadAgenda" )
			  .add( Projections.property("descripcion"), "descripcionUnidadAgenda" )
			  .add( Projections.property("servicio.codigo"), "codigoServicio" )
			  .add( Projections.property("refServicio.descripcion"), "descripcionServicio" )
			  .add( Projections.property("refServicio.codigoPropietario"), "codigoPropietarioServicio" )
			  .add( Projections.property("tarifariosOficiales.codigo"), "codigoTipoTarifario" )
	          .add( Projections.property("tarifariosOficiales.nombre"), "nombreTipoTarifario" )
	  		)
	  	.add(Restrictions.eq("codigo", codigoUnidadAgenda))
	  	.add(Restrictions.eq("tarifariosOficiales.codigo", codigoTarifario))
	  	.add(Restrictions.in("servicio.codigo", codigosServicio))
	  	.addOrder(Order.asc("refServicio.descripcion"))
		.setResultTransformer(Transformers.aliasToBean(DtoUnidadAgendaServCitaOdonto.class))
		.list();	
	}
	
	/**
	 * M&eacute;todo que retorna un listado con los Servicios asociados a una Unidad de Agenda espec&iacute;fica
	 * pasada como par&aacute;metro. Tambi&eacute;n se involucra en la b&uacute;queda el c&oacute;digo del tipo Tarifario
	 * definido de acuerdo al par&aacute;metro General del m&oacute;dulo de administraci&oacute;n 
	 * - C&oacute;digo Manual Est&aacute;ndar B&uacute;squeda y Presentaci&oacute;n de Servicios.
	 * 
	 * @param codigoUnidadAgenda}
	 * @param codigoTarifario
	 * @param estado
	 * @return List<{@link DtoUnidadAgendaServCitaOdonto}> listado con los Servicios asociados a una Unidad de Agenda espec&iacute;fica.
	 */
	@SuppressWarnings("unchecked")
	public List<DtoUnidadAgendaServCitaOdonto> listarServiciosPorUnidadAgendaPorTarifarioPorEstado (int codigoUnidadAgenda, int codigoTarifario, boolean estado){
		
		return (List<DtoUnidadAgendaServCitaOdonto>) sessionFactory.getCurrentSession()
		.createCriteria(UnidadesConsulta.class)
		.createAlias("servicioses", "servicio")
		.createAlias("servicio.referenciasServicios", "refServicio")
		.createAlias("refServicio.tarifariosOficiales", "tarifariosOficiales")

		.setProjection(Projections.projectionList()
			  .add( Projections.property("codigo"), "codigoUnidadAgenda" )
			  .add( Projections.property("descripcion"), "descripcionUnidadAgenda" )
			  .add( Projections.property("servicio.codigo"), "codigoServicio" )
			  .add( Projections.property("refServicio.descripcion"), "descripcionServicio" )
			  .add( Projections.property("refServicio.codigoPropietario"), "codigoPropietarioServicio" )
			  .add( Projections.property("tarifariosOficiales.codigo"), "codigoTipoTarifario" )
	          .add( Projections.property("tarifariosOficiales.nombre"), "nombreTipoTarifario" )
	  		)
	  	.add(Restrictions.eq("codigo", codigoUnidadAgenda))
	  	.add(Restrictions.eq("tarifariosOficiales.codigo", codigoTarifario))
	  	.add(Restrictions.eq("servicio.activo", estado))
	  	.addOrder(Order.asc("refServicio.descripcion"))
		.setResultTransformer(Transformers.aliasToBean(DtoUnidadAgendaServCitaOdonto.class))
		.list();
	}
	
	/**
	 * 
	 * M&eacute;todo que permite realizar la consulta de las unidades de consulta
	 * para un servicio espec&iacute;fico. 
	 * @param codServicio
	 * @return List
	 * @author Diana Ruiz
	 * @since 23/06/2011
	 * 
	 */	
	@SuppressWarnings("unchecked")
	public ArrayList<DtoUnidadesConsulta> listaUnidadesConsulta(int codServicio){	
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UnidadesConsulta.class,"unidadesConsulta");
		criteria.createAlias("unidadesConsulta.servicioses" , "servicio")
		.add(Restrictions.eq("servicio.codigo", codServicio));
				
		ProjectionList projectionList = Projections.projectionList();			
		projectionList.add(Projections.property("unidadesConsulta.codigo")				,"codigo");
		projectionList.add(Projections.property("unidadesConsulta.descripcion")			,"descripcion");	
		
		
		criteria.setProjection(projectionList);				
		criteria.setResultTransformer(Transformers.aliasToBean(DtoUnidadesConsulta.class));
		ArrayList<DtoUnidadesConsulta> listaUnidades= (ArrayList<DtoUnidadesConsulta>) criteria.list();		
		
		return listaUnidades;	
		
	}
	
	/**
	 * Lista de las unidades de Consulta
	 * 
	 * @author Cesar Gomez
	 * @return ArrayList<DtoUnidadesConsulta>
	*/
	public ArrayList<DtoUnidadesConsulta> listaTodoUnidadesConsulta()
	{
		 String consulta="SELECT new com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta(uni.codigo, uni.descripcion) " +
					      "FROM UnidadesConsulta uni " +
					      "ORDER BY uni.descripcion ";
			  
		  Query query = sessionFactory.getCurrentSession().createQuery(consulta);
		  ArrayList<DtoUnidadesConsulta> listaunidades = (ArrayList<DtoUnidadesConsulta>)query.list();
		  return listaunidades;
	}
	
}

