/*
 * julio 22, 2010
 */
package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.tesoreria.DtoConsultaTrasladoAbonoPAciente;
import com.servinte.axioma.orm.TrasladosAbonos;
import com.servinte.axioma.orm.TrasladosAbonosHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class TrasladosAbonosDelegate extends TrasladosAbonosHome{
	
	/**
	 * Lista todos
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TrasladosAbonos> listarTodos()
	{
		return (ArrayList<TrasladosAbonos>) sessionFactory.getCurrentSession()
			.createCriteria(TrasladosAbonos.class)
			.list();
	}

	
	/**
	 * Guarda el traslado
	 * @param transientInstance
	 * @return
	 */
	public boolean guardarTraslado(TrasladosAbonos transientInstance) 
	{
		boolean save = false;
		try{
			super.persist(transientInstance);
			save = true;
		}
		catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el Traslado de Abono: ",e);
		}
		
		return save;
	}
	
	
	
	
	/**
	 * Retorna los detalles  de un traslado de abonos enviando como parametro el codigo del traslado
	 * @param dtoConsulta
	 */
	@SuppressWarnings("unchecked")
	public List<DtoConsultaTrasladoAbonoPAciente> obtenerDetallesTrasladoAbonos(DtoConsultaTrasladoAbonoPAciente dtoConsulta)
	{
		Criteria criteria 	= sessionFactory.getCurrentSession().createCriteria(TrasladosAbonos.class, "trasAbonos");
		criteria.createAlias("trasAbonos.centroAtencion"			,"centroAtenProc"		,Criteria.LEFT_JOIN);
		criteria.createAlias("trasAbonos.usuarios"					,"usuProceso"			,Criteria.LEFT_JOIN);
		
		criteria.createAlias("trasAbonos.origenTrasladosAbonosPacs"	,"origenTrasAbono" 		,Criteria.LEFT_JOIN);
		criteria.createAlias("origenTrasAbono.movimientosAbonos"	,"movAbonoOrigen"		,Criteria.LEFT_JOIN);
		criteria.createAlias("movAbonoOrigen.centroAtencionByCentroAtencion"		,"centroAtenOri"		,Criteria.LEFT_JOIN);
		criteria.createAlias("movAbonoOrigen.ingresos"				,"ingresoOrigen"		,Criteria.LEFT_JOIN);
		criteria.createAlias("ingresoOrigen.centroAtencion"			,"cAIngresoOrigen"		,Criteria.LEFT_JOIN);	// De aca se saca el centro de atencion del origen
		criteria.createAlias("ingresoOrigen.planTratamientos"		,"planTrataIngresoOri"	,Criteria.LEFT_JOIN);
		criteria.createAlias("movAbonoOrigen.personas"				,"perOrigen"			,Criteria.LEFT_JOIN);
		criteria.createAlias("perOrigen.tiposIdentificacion"		,"tipoIdOrigen"			,Criteria.LEFT_JOIN);
		
		criteria.createAlias("trasAbonos.destinoTrasladosAbonosPacs","destinTrasAbono"		,Criteria.LEFT_JOIN);
		criteria.createAlias("destinTrasAbono.movimientosAbonos"	,"movAbonoDestin"		,Criteria.LEFT_JOIN);
		criteria.createAlias("movAbonoDestin.centroAtencionByCentroAtencion"		,"centroAtenDes"		,Criteria.LEFT_JOIN);
		criteria.createAlias("movAbonoDestin.ingresos"				,"ingresoDestino"		,Criteria.LEFT_JOIN);
		criteria.createAlias("ingresoDestino.planTratamientos"		,"planTrataIngresoDes"	,Criteria.LEFT_JOIN);
		criteria.createAlias("movAbonoDestin.personas"				,"perDestino"			,Criteria.LEFT_JOIN);
		criteria.createAlias("perDestino.tiposIdentificacion"		,"tipoIdDestino"		,Criteria.LEFT_JOIN);
		
		
		criteria.add(Restrictions.eq("trasAbonos.codigoPk", dtoConsulta.getIdTrasladoAbonos()));
		
		
		ProjectionList projectionList 	= Projections.projectionList();
		
		projectionList.add( Projections.property("trasAbonos.codigoPk")				,"idTrasladoAbonos");
		projectionList.add( Projections.property("centroAtenProc.consecutivo")		,"codCentroAtenProceso");
		projectionList.add( Projections.property("centroAtenProc.descripcion")		,"nomCentroAtenProceso");
		projectionList.add( Projections.property("usuProceso.login")				,"loginUsuarioProceso");
		projectionList.add( Projections.property("trasAbonos.fecha")				,"fechaProceso");
		projectionList.add( Projections.property("trasAbonos.hora")					,"horaProceso");
		
		projectionList.add( Projections.property("tipoIdOrigen.acronimo")			,"tipoIdOri");
		projectionList.add( Projections.property("perOrigen.numeroIdentificacion")	,"numeroIdOri");
		projectionList.add( Projections.property("perOrigen.primerNombre")			,"primerNombreOri");
		projectionList.add( Projections.property("perOrigen.segundoNombre")			,"segundoNombreOri");
		projectionList.add( Projections.property("perOrigen.primerApellido")		,"primerApellidoOri");
		projectionList.add( Projections.property("perOrigen.segundoApellido")		,"segundoApellidoOri");
		//projectionList.add( Projections.property("centroAtenOri.consecutivo")		,"centroAtencionOri");
		//projectionList.add( Projections.property("centroAtenOri.descripcion")		,"nombreCentroAtencionOri");
		projectionList.add( Projections.property("cAIngresoOrigen.consecutivo")		,"centroAtencionOriObj");
		projectionList.add( Projections.property("cAIngresoOrigen.descripcion")		,"nombreCentroAtencionOri");
		//--
		projectionList.add( Projections.property("movAbonoOrigen.valor")			,"abonoTrasladadoOri");
		projectionList.add( Projections.property("ingresoOrigen.id")				,"idIngresoOriObj");
		projectionList.add( Projections.property("planTrataIngresoOri.estado")		,"estadoPlanTramiendoOri");

		projectionList.add( Projections.property("tipoIdDestino.acronimo")			,"tipoIdDes");
		projectionList.add( Projections.property("perDestino.numeroIdentificacion")	,"numeroIdDes");
		projectionList.add( Projections.property("perDestino.primerNombre")			,"primerNombreDes");
		projectionList.add( Projections.property("perDestino.segundoNombre")		,"segundoNombreDes");
		projectionList.add( Projections.property("perDestino.primerApellido")		,"primerApellidoDes");
		projectionList.add( Projections.property("perDestino.segundoApellido")		,"segundoApellidoDes");
		projectionList.add( Projections.property("centroAtenDes.consecutivo")		,"centroAtencionDes");
		projectionList.add( Projections.property("centroAtenDes.descripcion")		,"nombreCentroAtencionDes");
		projectionList.add( Projections.property("movAbonoDestin.valor")			,"abonoTrasladadoDes");
		projectionList.add( Projections.property("ingresoDestino.id")				,"idIngresoDes");
		projectionList.add( Projections.property("planTrataIngresoDes.estado")		,"estadoPlanTramiendoDes");
		
		criteria.setProjection(projectionList);

		criteria.setResultTransformer(Transformers.aliasToBean(DtoConsultaTrasladoAbonoPAciente.class));
		
		
		return (List<DtoConsultaTrasladoAbonoPAciente>) criteria.list();
	}

	/*
	public static void main(String[] args) {
		TrasladosAbonosDelegate d = new TrasladosAbonosDelegate();
		DtoConsultaTrasladoAbonoPAciente dtoConsulta = new DtoConsultaTrasladoAbonoPAciente();
		dtoConsulta.setIdTrasladoAbonos(7);
		System.out.println("=================================================");
		List<DtoConsultaTrasladoAbonoPAciente> l =d.obtenerDetallesTrasladoAbonos(dtoConsulta);
		System.out.println("SIZESIZESIZEE: "+l.size());
	}
	*/
}
