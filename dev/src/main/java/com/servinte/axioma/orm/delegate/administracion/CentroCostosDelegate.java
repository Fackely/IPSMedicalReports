package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoCentroCostosVista;
import com.servinte.axioma.orm.CentroCostoViaIngreso;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.CentrosCostoHome;
import com.servinte.axioma.orm.UnidadesConsulta;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * 
 * @author Edgar Carvajal 
 *
 */
@SuppressWarnings("unchecked")
public class CentroCostosDelegate extends CentrosCostoHome
{
	
	
	/**
	 * CARGA EL CENTRO DE COSTOS POR INSTITUCION 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public List<DtoCentroCostosVista> busquedaCentroCostos(DtoCentroCostosVista dto)
	{
		UtilidadTransaccion.getTransaccion().begin();
	
		Criteria criterio=sessionFactory.getCurrentSession().createCriteria(CentrosCosto.class);
		
		if(dto.getCodigoCentroAtencion()>0)
		{
			criterio.add(Restrictions.eq("centroAtencion.consecutivo",dto.getCodigoCentroAtencion() )) ;
		}

		criterio.setProjection(Projections.projectionList().
				add(Projections.property("codigo"), "codigo").
				add(Projections.property("nombre"), "nombre")).
				setResultTransformer(Transformers.aliasToBean(DtoCentroCostosVista.class));
		
		List<DtoCentroCostosVista> listaCentros= criterio.list();
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return listaCentros;
	}
	
	
	
	/**
	 * Lista los centros de costo por Centro de Atención.
	 * Si se le envia el tipoArea realiza el filtro para el tipo de area del centro de costo
	 * 
	 * @author Cristhian Murillo
	 * @param consecutivoCentroAttencion
	 * @param tipoArea
	 * @return  List<DtoCentroCostosVista>
	 */
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorCentroAtencion(int consecutivoCentroAttencion, int tipoArea)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentrosCosto.class, "centrosCosto");
		criteria.createAlias("centrosCosto.centroAtencion"			, "centroAtencion");
		criteria.add(Restrictions.eq("centroAtencion.consecutivo"	,consecutivoCentroAttencion )) ;
		
		if(tipoArea > 0)
		{
			criteria.createAlias("centrosCosto.tiposArea"		, "tiposArea");
			criteria.add(Restrictions.eq("tiposArea.codigo"		, tipoArea )) ;
		}
		
		criteria.setProjection(Projections.projectionList().
				add(Projections.property("codigo"), "codigo").
				add(Projections.property("nombre"), "nombre")).
				setResultTransformer(Transformers.aliasToBean(DtoCentroCostosVista.class));
		
		List<DtoCentroCostosVista> listaCentros= criteria.list();
		
		return listaCentros;
	}
	
	
	/**
	 * obtenerCentrosCostoPorViaIngreso
	 * @param consecutivoVia
	 * @return List<DtoCentroCostosVista>
	 */
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorViaIngreso(int consecutivoVia)
	{
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(CentroCostoViaIngreso.class, "centroCostoViaIngreso");
		
		//criteria.createAlias("centroCostoViaIngreso.codigo", "codCentroCostoViaIngresos");
		criteria.createAlias("centroCostoViaIngreso.viasIngreso", "viasIngreso");
		criteria.createAlias("centroCostoViaIngreso.centrosCosto", "centrosCosto");
		//criteria.createAlias("viasIngreso.codigo","codViaIngreso");
		
		criteria.add(Restrictions.eq("viasIngreso.codigo", consecutivoVia));
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
			.add(Projections.property("centrosCosto.codigo"),"codigo")
			.add(Projections.property("centrosCosto.nombre"),"nombre")));
		
			criteria.addOrder(Order.asc("centrosCosto.nombre"));
			criteria.setResultTransformer(Transformers.aliasToBean(DtoCentroCostosVista.class));
		
		
		List<DtoCentroCostosVista> listaCetros=criteria.list();
		return listaCetros;
	}
	
	
	
	/**
	 * Lista los centros de costo activos por institución que cumplan con el listados de tipos de entidad que ejecuta enviado
	 * Se envia un arreglo de los tipos de entidad que ejecuta que se quieren filtrar
	 * 
	 * @author Cristhian Murillo
	 * @param institucion
	 * @param tipoentidadesEjecuta
	 * @return  List<DtoCentroCostosVista>
	 */
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorInstitucionTipoEntidad(int institucion, String[] tipoentidadesEjecuta)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentrosCosto.class, "centrosCosto");
		
		criteria.createAlias("centrosCosto.instituciones"				, "instituciones");
		
		criteria.add(Restrictions.eq("centrosCosto.esActivo"			, true )) ;
		criteria.add(Restrictions.in("centrosCosto.tipoEntidadEjecuta"	, tipoentidadesEjecuta )) ;
		criteria.add(Restrictions.eq("instituciones.codigo"				, institucion )) ;
		
		criteria.setProjection(Projections.projectionList().
				add(Projections.property("codigo"), "codigo").
				add(Projections.property("nombre"), "nombre")).
				setResultTransformer(Transformers.aliasToBean(DtoCentroCostosVista.class));
		
		List<DtoCentroCostosVista> listaCentros= criteria.list();
		
		return listaCentros;
	}
	
	
	
	@Override
	public CentrosCosto findById(int id) 
	{
		CentrosCosto centrosCosto = new CentrosCosto();
		centrosCosto = super.findById(id);
		centrosCosto.getTiposArea().getCodigo();
		
		return centrosCosto;
	}
	
	
	/**
	 * Retorna los centro de costo activos para las EntidadesSubcontratadas
	 * 
	 * @param codentidadSubcontratada
	 * @return  List<DtoCentroCostosVista>
	 * 
	 * @author Cristhian Murillo
	*/
	public List<DtoCentroCostosVista> listaCentroCostoActivoXrEntidadesSub (long codentidadSubcontratada)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentrosCosto.class, "centrosCosto");
		
		criteria.createAlias("centrosCosto.centrosCostoEntidadesSubs"				, "centrosCostoEntidadesSubs");
		criteria.createAlias("centrosCostoEntidadesSubs.entidadesSubcontratadas"	, "entidadesSubcontratadas");
		criteria.createAlias("centrosCosto.tiposArea"								, "tiposArea");
		
		
		criteria.add(Restrictions.eq("centrosCosto.esActivo"			, true )) ;
		criteria.add(Restrictions.eq("entidadesSubcontratadas.codigoPk"	, codentidadSubcontratada )) ;
		
		ProjectionList projectionList = Projections.projectionList(); 
			projectionList.add(Projections.property("centrosCosto.codigo")						, "codigo");
			projectionList.add(Projections.property("centrosCosto.nombre")						, "nombre");
			projectionList.add(Projections.property("centrosCostoEntidadesSubs.nroPrioridad")	,"nroPrioridad");
			projectionList.add(Projections.property("tiposArea.codigo")							, "tipoArea");
			
			projectionList.add( Projections.groupProperty("centrosCosto.codigo"));
			projectionList.add( Projections.groupProperty("centrosCosto.nombre"));
			projectionList.add(Projections.groupProperty("centrosCostoEntidadesSubs.nroPrioridad"));
			projectionList.add(Projections.groupProperty("tiposArea.codigo"));
			
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoCentroCostosVista.class));
		List<DtoCentroCostosVista> listaCentros= criteria.list();
		
		return listaCentros;
	}
	
	
	
	/**
	 * Realiza la búsqueda segun la parametrica de centro de costo en donde se selecciona la vía de 
	 *  ingreso y la entidad subcontratada por cada centro de costo.
	 * 
	 * @param parametros
	 * @return CentrosCosto
	 * 
	 * @author Cristhian Murillo
	 */
	public CentrosCosto obtenerCentrosCostoPorViaIngresoEntSub(DTOEstanciaViaIngCentroCosto parametros)
	{
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(CentrosCosto.class, "centrosCosto");
		
		criteria.createAlias("centrosCosto.estanciaViaIngCentroCostos"				, "estanciaViaIngCentroCostos");
		criteria.createAlias("estanciaViaIngCentroCostos.entidadesSubcontratadas"	, "entidadesSubcontratadas");
		criteria.createAlias("estanciaViaIngCentroCostos.viasIngreso"				, "viasIngreso");
		
		criteria.add(Restrictions.eq("entidadesSubcontratadas.codigoPk"	, parametros.getEntidadSubcontratada()));
		criteria.add(Restrictions.eq("viasIngreso.codigo"				, parametros.getViaIngreso()));
		
		return (CentrosCosto)criteria.uniqueResult();
	}
			
	/**
	 * 
	 * M&eacute;todo que permite realizar la consulta de los centros de costo por grupo de servicio
	 * para un servicio espec&iacute;fico. 
	 * @param codServicio
	 * @return ArrayList
	 * @author Diana Ruiz
	 * @since 23/06/2011
	 * 
	 */	
	public ArrayList<DtoCentroCosto> listaCentroCostoGrupoServicio(int grupoServicio, int codCentroAtencion){	
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentrosCosto.class,"centroCosto");
		criteria.createAlias("centroCosto.centroCostoGrupoSers" 		, "centroCostoGrupoServicio");
		criteria.createAlias("centroCostoGrupoServicio.gruposServicios" ,"grupoServicio" );
		criteria.createAlias("centroCostoGrupoServicio.centroAtencion" 	,"centroAtencion" );
		criteria.add(Restrictions.eq("grupoServicio.codigo", grupoServicio));
		criteria.add(Restrictions.eq("centroAtencion.consecutivo", codCentroAtencion));
		
		
		ProjectionList projectionList = Projections.projectionList();			
			projectionList.add(Projections.property("centroCosto.codigo")			,"codigoCentroCosto");
			projectionList.add(Projections.property("centroCosto.nombre")			,"nombre");
			projectionList.add(Projections.property("centroAtencion.consecutivo")	,"codigoCentroAtencion");
		criteria.setProjection(projectionList);
				
		criteria.setResultTransformer(Transformers.aliasToBean(DtoCentroCosto.class));
		ArrayList<DtoCentroCosto> listaCentros= (ArrayList<DtoCentroCosto>) criteria.list();
		
		return  listaCentros;
					
	}
	
	/**
	 * 
	 * M&eacute;todo que permite realizar la consulta de los centros de costo por unidad de agenda y por centro de atención
	 * para un servicio espec&iacute;fico. 
	 * @param codServicio
	 * @return ArrayList
	 * @author Diana Ruiz
	 * @since 24/06/2011
	 * 
	 */		
	
	public ArrayList<DtoCentroCosto> listaCentroCostoUnidadConsulta(DtoCentroCosto centroCosto){	
			
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentrosCosto.class,"centroCosto");
			criteria.createAlias("centroCosto.unidadesConsultas" , "unidadesConsulta");
			
			
			//Parametro unidad Consulta
			if(centroCosto.getCodigoUnidadConsulta() > 0 && centroCosto.getCodigoUnidadConsulta() != null)
			{
				criteria.add(Restrictions.eq("unidadesConsulta.codigo"	, centroCosto.getCodigoUnidadConsulta()));			
			}
			
			//Parametro Centro Costo
			if(centroCosto.getCodigoCentroCosto() > 0)
			{
				criteria.add(Restrictions.eq("centroCosto.codigo"	, centroCosto.getCodigoCentroCosto()));			
			}
			
			//Parametro servicio
			if(centroCosto.getCodServicio() != null)
			{
				criteria.createAlias("unidadesConsulta.servicioses" , "servicio");
				criteria.add(Restrictions.eq("servicio.codigo", centroCosto.getCodServicio()));
			}
			
			//Parametro centro atención
			if(centroCosto.getCodigoCentroAtencion() != null)
			{
				criteria.createAlias("centroCosto.centroAtencion" , "centroAtencion");
				criteria.add(Restrictions.eq("centroAtencion.consecutivo", centroCosto.getCodigoCentroAtencion()));
			}
			
			ProjectionList projectionList = Projections.projectionList();			
			projectionList.add(Projections.property("unidadesConsulta.codigo")				,"codigoUnidadConsulta");
			projectionList.add(Projections.property("unidadesConsulta.descripcion")			,"descripcionUnidadConsulta");
			projectionList.add(Projections.property("centroCosto.codigo")					,"codigoCentroCosto");
			
			
			criteria.setProjection(projectionList);				
			criteria.setResultTransformer(Transformers.aliasToBean(DtoCentroCosto.class));
			ArrayList<DtoCentroCosto> listaCentrosUnidades= (ArrayList<DtoCentroCosto>) criteria.list();		
			
			return listaCentrosUnidades;		
			
		}	
	
	
	/**
	 * 
	 * M&eacute;todo que permite realizar la consulta del centro de Atencion de acuerdo al codigo del centro de costo 
	 *  
	 * @param codigoCentroAtencion
	 * @return DtoCentroCosto
	 * @author Camilo Gómez
	 */		
	public DtoCentroCosto obtenerCentroAtencionXCentroCosto(DtoCentroCosto centroCosto)
	{	
			
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentrosCosto.class,"centroCosto");
		criteria.createAlias("centroCosto.centroAtencion" 	 , "centroAtencion");
		
		//Parametro Centro Costo
		if(centroCosto.getCodigoCentroCosto() > 0)
		{
			criteria.add(Restrictions.eq("centroCosto.codigo"	, centroCosto.getCodigoCentroCosto()));			
		}
		
		ProjectionList projectionList = Projections.projectionList();			
		projectionList.add(Projections.property("centroCosto.codigo")					,"codigoCentroCosto");
		projectionList.add(Projections.property("centroAtencion.consecutivo")			,"codigoCentroAtencion");
		projectionList.add(Projections.property("centroAtencion.descripcion")			,"nombreCentroAtencion");
		
		criteria.setProjection(projectionList);				
		criteria.setResultTransformer(Transformers.aliasToBean(DtoCentroCosto.class));
		DtoCentroCosto centroAtencion= (DtoCentroCosto) criteria.uniqueResult();		
		
		return centroAtencion;		
	}
	
	
	/**
	 * Metodo que permite listar los centros de costo de la funcionalidad FarmaciasXCentrosCosto para el centro de atencion 
	 * del paciente
	 * @param codigoCentroAtencion
	 * @return
	 * 
	 * @author Diana Ruiz
	 * 
	 */
	
	public ArrayList<DtoCentroCosto> listaCentroCostoSubAlmacenXCentroAtencion(int centroAtencion){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentrosCosto.class,"centroCosto");
		criteria.createAlias("centroCosto.detFarmaciaCcs" 		, "detFarmaciaCcs");
		criteria.createAlias("detFarmaciaCcs.farmaciaXCentroCosto" 		, "farmaciaXCentroCosto");		
		criteria.createAlias("farmaciaXCentroCosto.centroAtencion" 		, "centroAtencion");		
		criteria.add(Restrictions.eq("centroAtencion.consecutivo", centroAtencion));
			
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("centroCosto.nombre")	,"nombre")
				.add(Projections.property("centroCosto.codigo")	,"codigoCentroCosto")
				.add(Projections.property("centroAtencion.consecutivo")	,"codigoCentroAtencion")));
		criteria.addOrder(Order.asc("centroCosto.nombre"));
		criteria.setResultTransformer(Transformers.aliasToBean(DtoCentroCosto.class));
		ArrayList<DtoCentroCosto> listaCentros= (ArrayList<DtoCentroCosto>) criteria.list();
		
		return listaCentros;
			
	}	
		
	/**
	 * 
	 * M&eacute;todo que permite realizar la consulta de los centros de costo por tipo de área y estado activo
	 * @param tipoArea
	 * @param estado
	 * @return ArrayList
	 * @author Diego Corredor
	 * @since 21/03/2012
	 * 
	 */	
	public ArrayList<DtoCentroCosto> listaCentroCostoTipoArea(int tipoArea, boolean estado) {
		ArrayList<DtoCentroCosto> listaCentrosCosto = null;
		
		try {
			String sql = "SELECT new com.princetonsa.dto.manejoPaciente.DtoCentroCosto(cc.codigo, cc.identificador, cc.nombre, ca.consecutivo, ca.descripcion, cc.codigoInterfaz) " +
							"FROM CentrosCosto cc " +
								"INNER JOIN cc.centroAtencion ca " +
								"INNER JOIN cc.tiposArea ta " +
						 "WHERE ta.codigo =:tipoArea AND cc.esActivo =:estado " +
						 "ORDER BY cc.nombre";
			Query query = sessionFactory.getCurrentSession().createQuery(sql);
			query.setParameter("tipoArea", tipoArea, Hibernate.INTEGER);
			query.setParameter("estado", estado, Hibernate.BOOLEAN);
			listaCentrosCosto = new ArrayList<DtoCentroCosto>();
			listaCentrosCosto = (ArrayList<DtoCentroCosto>) query.list();
		} catch (Exception e) {
			Log4JManager.error("Error obteniendo los centros de costo por tipo área", e);
		}
		
		return listaCentrosCosto;
	}
}
