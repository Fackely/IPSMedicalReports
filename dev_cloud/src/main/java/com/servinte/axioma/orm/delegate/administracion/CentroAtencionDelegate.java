package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.UtilidadTexto;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.CentroAtencionHome;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.RecibosCajaId;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class CentroAtencionDelegate extends CentroAtencionHome{
	
	/**
	 * 	Retorna una lista de los centros de atenci&oacute;n 
	 * activos en el sistema.
	 */
	public ArrayList<CentroAtencion> listarActivos(){
		return (ArrayList<CentroAtencion>) sessionFactory.getCurrentSession()
				.createCriteria(CentroAtencion.class)
				.add(Expression.eq("activo", true)).addOrder( Order.asc("descripcion") )
				.list();
	}
	
	/**
	 * 	Retorna los activos de acuerdo a una insitucion
	 */
	public ArrayList<CentroAtencion> listarTodosActivosPorInstitucion(int institucion){
		
		return (ArrayList<CentroAtencion>) sessionFactory.getCurrentSession()
				.createCriteria(CentroAtencion.class)
				.add(Expression.eq("activo", true))
				.createCriteria("instituciones").add(Expression.eq("codigo", institucion))
				.list();
	}
	
	/**
	 * M&eacute;todo que lista los centros de atenci&oacute;n pasando una instancia del objeto <code>Instituciones</code>
	 * @param institucionObj
	 * @return
	 */
	public ArrayList<CentroAtencion> listarCentrosInstitucion(Instituciones institucionObj)
	{
		return (ArrayList<CentroAtencion>) sessionFactory.getCurrentSession()
				.createCriteria(CentroAtencion.class)
				.add(Expression.eq("instituciones", institucionObj))
				.list();
	}
	
	/**
	 * 	Retorna los activos de acuerdo a una institucion recibiendo
	 * como par&aacute;metro un Dto de tipo CentroAtencion
	 */
	public ArrayList<CentroAtencion> listarTodosActivosPorInstitucion(CentroAtencion dtoCentro)
	{
		Criteria criterio= sessionFactory.getCurrentSession().createCriteria(CentroAtencion.class);
		
		if (!UtilidadTexto.isEmpty( dtoCentro.getCodigo() ))
		{	
			criterio.add(Restrictions.eq("codigo",dtoCentro.getCodigo()));
		}
	
		if(!UtilidadTexto.isEmpty(dtoCentro.getDescripcion()))
		{
			criterio.add(Restrictions.eq("descripcion", dtoCentro.getDescripcion()));
		}
		
		if(dtoCentro.getInstituciones()!=null && dtoCentro.getInstituciones().getCodigo()>0)
		{
			criterio.add(Restrictions.eq("instituciones.codigo", dtoCentro.getInstituciones().getCodigo()));
		}
		
		criterio.add(Expression.eq("activo",Boolean.TRUE));
		
		
		return (ArrayList<CentroAtencion>) criterio.list(); 
	}
	
	/**
	 * 	Retorna los centros de atenci&oacute;n activos en el sistema de 
	 * acuerdo a una instituci&oacute;n y una regi&oacute;n especifica.
	 */
	public ArrayList<CentroAtencion> listarTodosActivosPorInstitucionYRegion(int institucion, long codRegion){
		
		return (ArrayList<CentroAtencion>) sessionFactory.getCurrentSession()
				.createCriteria(CentroAtencion.class)
				.createAlias("instituciones", "instituciones")
				.createAlias("regionesCobertura", "regionesCobertura")
				
				.add(Restrictions.eq("activo", true))
				.add(Restrictions.eq("instituciones.codigo", institucion))
				.add(Restrictions.eq("regionesCobertura.codigo", codRegion))
				
				.list();
	}
	
	/**
	 * Este m&eacute;todo se encarga de retornar el listado con
	 * todos los centros de atenci&oacute;n existentes en el sistema. 
	 * 
	 * @return listaCentro listado de todos los centros de atenci&oacute;n.
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoCentrosAtencion> listarTodosCentrosAtencion(){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentroAtencion.class,"centroAtencion")
			.createAlias("centroAtencion.regionesCobertura", "regionesCobertura",Criteria.LEFT_JOIN)
			.createAlias("centroAtencion.ciudades", "ciudades")
		.addOrder( Order.asc("descripcion"));
		
		criteria.setProjection(Projections.projectionList()	
			.add(Projections.property("centroAtencion.consecutivo"),"consecutivo")
			.add(Projections.property("centroAtencion.descripcion"),"descripcion")
			.add(Projections.property("ciudades.descripcion"),"descripcionCiudad")
			.add(Projections.property("regionesCobertura.descripcion"),"descripcionRegion")
			.add(Projections.property("centroAtencion.activo"),"activo")
		)
		
		.setResultTransformer( Transformers.aliasToBean(DtoCentrosAtencion.class));
		 ArrayList<DtoCentrosAtencion> listaCentro=(ArrayList)criteria.list();

		 return listaCentro;
	}
	
	/**
	 * 	Retorna todos los centros de atenci&oacute;n del sistema de 
	 * acuerdo a una instituci&oacute;n y una regi&oacute;n espec&iacute;fica.
	 * @param institucion
	 * @param codRegion
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<CentroAtencion> listarTodosPorInstitucionYRegion(int institucion, long codRegion){
		
		return (ArrayList<CentroAtencion>) sessionFactory.getCurrentSession()
				.createCriteria(CentroAtencion.class)
				.createAlias("instituciones", "instituciones")
				.createAlias("regionesCobertura", "regionesCobertura")
				
				.add(Restrictions.eq("instituciones.codigo", institucion))
				.add(Restrictions.eq("regionesCobertura.codigo", codRegion))
				.addOrder( Order.asc("descripcion") )
				
				.list();
	}
	
	/**
	 * Retorna todos los centros de atenci&oacute;n del sistema de 
	 * acuerdo a una empresa institucion y una ciudad espec&iacute;fica.
	 * @param empresaInstitucion
	 * @param codigoCiudad
	 * @param codigoPais
	 * @param codigoDto
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucionYCiudad(long empresaInstitucion, String codigoCiudad, String codigoPais, String codigoDto ){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentroAtencion.class,"centroAtencion")
			
			.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
			.createAlias("regionesCobertura", "regionesCobertura")
			.createAlias("centroAtencion.ciudades", "ciudades");
		
			
				criteria.add(Restrictions.eq("empresaInstitucion.codigo", empresaInstitucion))
				.add(Restrictions.eq("ciudades.id.codigoCiudad", codigoCiudad))
				.add(Restrictions.eq("ciudades.id.codigoPais", codigoPais))
				.add(Restrictions.eq("ciudades.id.codigoDepartamento", codigoDto))
				
				.addOrder( Order.asc("descripcion"));
				
				criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("centroAtencion.consecutivo"),"consecutivo")
				.add(Projections.property("centroAtencion.descripcion"),"descripcion")
				.add(Projections.property("ciudades.descripcion"),"descripcionCiudad")
				.add(Projections.property("regionesCobertura.descripcion"),"descripcionRegion")
				
				)
				
				.setResultTransformer( Transformers.aliasToBean(DtoCentrosAtencion.class));
				 ArrayList<DtoCentrosAtencion> listaCentro=(ArrayList)criteria.list();
		
		return listaCentro;
	}
	
	/**
	 * Retorna todos los centros de atenci&oacute;n del sistema de 
	 * acuerdo a una empresa institucion y una regi&oacute; espec&iacute;fica.
	 * @param empresaInstitucion
	 * @param codigoRegion
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucionYRegion(long empresaInstitucion, long codigoRegion ){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentroAtencion.class,"centroAtencion")

			.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
			.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
			.createAlias("centroAtencion.ciudades", "ciudades");
		
			
				criteria.add(Restrictions.eq("empresaInstitucion.codigo", empresaInstitucion))
				.add(Restrictions.eq("regionesCobertura.codigo", codigoRegion))
				
				.addOrder( Order.asc("descripcion"));
				
				criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("centroAtencion.consecutivo"),"consecutivo")
				.add(Projections.property("centroAtencion.descripcion"),"descripcion")
				.add(Projections.property("ciudades.descripcion"),"descripcionCiudad")
				.add(Projections.property("regionesCobertura.descripcion"),"descripcionRegion")
				
				)
				
				.setResultTransformer( Transformers.aliasToBean(DtoCentrosAtencion.class));
				 ArrayList<DtoCentrosAtencion> listaCentro=(ArrayList)criteria.list();
		
		return listaCentro;
	}
	
	/**
	 * Este m&eacute;todo se encarga de retornar el c&oacute;digo de los centros 
	 * de atenci&oacute;n en los que se han dado ingresos.
	 * @param ingresos
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionIngresos (List<Integer> ingresos){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentroAtencion.class, "centroAtencion")
		.createAlias("centroAtencion.ingresoses", "ingresos")
		.createAlias("centroAtencion.ciudades", "ciudades")
		.createAlias("ciudades.paises", "paises")
		.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
		.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
		.createAlias("centroAtencion.instituciones", "institucion")
		
		.add(Restrictions.in("ingresos.id", ingresos))
		
		.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("centroAtencion.consecutivo"), "consecutivo")
				.add(Projections.property("institucion.codigo"), "codInstitucion")
				.add(Projections.property("centroAtencion.descripcion"), "descripcion")
				.add( Projections.property("empresaInstitucion.razonSocial"), "descripcionEmpresaInstitucion" )
				.add( Projections.property("paises.descripcion"),"nombrePais")
				.add( Projections.property("ciudades.descripcion")	, "descripcionCiudad" )
				.add( Projections.property("regionesCobertura.descripcion"), "descripcionRegion" )
				
				))
				
		.setResultTransformer(Transformers.aliasToBean(DtoCentrosAtencion.class));
		ArrayList<DtoCentrosAtencion> listaCentros=(ArrayList)criteria.list();
		
		return listaCentros;
		
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtner el listado de los centros de 
	 * atenci&oacute;n pertenecientes a una determinada ciudad.
	 * @param codigoCiudad
	 * @param codigoPais
	 * @param codigoDto
	 * @returndelegate
	 *
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoCentrosAtencion> listarTodosPorCiudad (String codigoCiudad, String codigoPais, String codigoDto ){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentroAtencion.class, "centroAtencion")
			
			.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion",Criteria.LEFT_JOIN)
			.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
			.createAlias("centroAtencion.ciudades", "ciudades");
		
				criteria.add(Restrictions.eq("ciudades.id.codigoCiudad", codigoCiudad))
				.add(Restrictions.eq("ciudades.id.codigoPais", codigoPais))
				.add(Restrictions.eq("ciudades.id.codigoDepartamento", codigoDto))
				
				.addOrder( Order.asc("descripcion"));
				
				criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("consecutivo"),"consecutivo")
				.add(Projections.property("descripcion"),"descripcion")
				.add(Projections.property("ciudades.descripcion"),"descripcionCiudad")
				.add(Projections.property("regionesCobertura.descripcion"),"descripcionRegion")
				
				)
				
				.setResultTransformer( Transformers.aliasToBean(DtoCentrosAtencion.class));
				 ArrayList<DtoCentrosAtencion> listaCentro=(ArrayList)criteria.list();
		
		return listaCentro;
	}

	/**
	 * Retorna todos los centros de atenci&oacute;n del sistema de 
	 * acuerdo a una empresa instituci&oacute;n espec&iacute;fica.
	 * @param empresaInstitucion
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucion(long empresaInstitucion){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentroAtencion.class,"centroAtencion")

			.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
			.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
			.createAlias("centroAtencion.ciudades", "ciudades");
	
				criteria.add(Restrictions.eq("empresaInstitucion.codigo", empresaInstitucion))
					
				.addOrder( Order.asc("descripcion"));
				
				criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("centroAtencion.consecutivo"),"consecutivo")
				.add(Projections.property("centroAtencion.descripcion"),"descripcion")
				.add(Projections.property("ciudades.descripcion"),"descripcionCiudad")
				.add(Projections.property("regionesCobertura.descripcion"),"descripcionRegion")
								
				)
				
				.setResultTransformer( Transformers.aliasToBean(DtoCentrosAtencion.class));
				 ArrayList<DtoCentrosAtencion> listaCentro=(ArrayList)criteria.list();
		
		return listaCentro;
	}	
	
	/**
	 * Retorna todos los centros de atenci&oacute;n del sistema de 
	 * acuerdo a una regi&oacute; espec&iacute;fica.
	 * @param codigoRegion
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoCentrosAtencion> listarTodosPorRegion(long codigoRegion ){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentroAtencion.class,"centroAtencion")

			.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
			.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
			.createAlias("centroAtencion.ciudades", "ciudades");
		
			
				criteria.add(Restrictions.eq("regionesCobertura.codigo", codigoRegion))
				
				.addOrder( Order.asc("descripcion"));
				
				criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("centroAtencion.consecutivo"),"consecutivo")
				.add(Projections.property("centroAtencion.descripcion"),"descripcion")
				.add(Projections.property("ciudades.descripcion"),"descripcionCiudad")
				.add(Projections.property("regionesCobertura.descripcion"),"descripcionRegion")
				
				)
				
				.setResultTransformer( Transformers.aliasToBean(DtoCentrosAtencion.class));
				 ArrayList<DtoCentrosAtencion> listaCentro=(ArrayList)criteria.list();
		
		return listaCentro;
	}
			
	
	@Override
	public CentroAtencion findById(int id) 
	{
		CentroAtencion centroAtencion = super.findById(id);
		
		if(centroAtencion!=null)
		{
			centroAtencion.getInstituciones().getRazonSocial();
			if(centroAtencion.getEmpresasInstitucion()!=null){
				centroAtencion.getEmpresasInstitucion().getRazonSocial();
			}
			
			if(centroAtencion.getEntidadesSubcontratadas() != null){
				centroAtencion.getEntidadesSubcontratadas().getCodigoPk();
			}
		}
		
		return centroAtencion;
	}
	
	
	/**
	 * Retorna los centros de atenci&oacute;n en los cuales
	 * se registraron los presupuestos odontol&oacute; contratados  
	 * @param presupuesto
	 * @return listaCentros
	 * @author Diana Carolina G
	 */
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionPresupuestos (List<Long> presupuesto){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentroAtencion.class, "centroAtencion")
		.createAlias("centroAtencion.presupuestoOdontologicos", "presupuesto")
		.createAlias("centroAtencion.ciudades", "ciudades")
		.createAlias("ciudades.paises", "paises")
		.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
		.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
		.createAlias("centroAtencion.instituciones", "institucion")
		
		.add(Restrictions.in("presupuesto.codigoPk", presupuesto))
		
		.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("centroAtencion.consecutivo"), "consecutivo")
				.add(Projections.property("institucion.codigo"), "codInstitucion")
				.add(Projections.property("centroAtencion.descripcion"), "descripcion")
				.add( Projections.property("empresaInstitucion.razonSocial"), "descripcionEmpresaInstitucion" )
				.add( Projections.property("paises.descripcion"),"nombrePais")
				.add( Projections.property("ciudades.descripcion")	, "descripcionCiudad" )
				.add( Projections.property("regionesCobertura.descripcion"), "descripcionRegion" )
				
				))
				
		.setResultTransformer(Transformers.aliasToBean(DtoCentrosAtencion.class));
		ArrayList<DtoCentrosAtencion> listaCentros=(ArrayList<DtoCentrosAtencion>)criteria.list();
		
		return listaCentros;
		
	}
	
	/**
	 * M&eacute;todo que retorna los centros de atenci&oacute;n en los 
	 * cuales quedan asociados los recibos de caja.
	 * @param numeroRC
	 * @return ArrayList<DtoCentrosAtencion>
	 * @author Diana Carolina G
	 */
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionRecibosCaja (List<RecibosCajaId> numeroRC){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentroAtencion.class, "centro_aten")
		.createAlias("centro_aten.recibosCajas"			, "recibosCajas")
		.createAlias("centro_aten.ciudades"				, "ciudades")
		.createAlias("ciudades.paises"					, "paises")
		.createAlias("centro_aten.regionesCobertura"	, "regionesCobertura")
		.createAlias("centro_aten.empresasInstitucion"	, "empresasInstitucion")
		.createAlias("centro_aten.instituciones"		, "instituciones")
		
		.add(Restrictions.in("recibosCajas.id", numeroRC))
		
		.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("centro_aten.consecutivo")	, "consecutivo")
				.add(Projections.property("instituciones.codigo")		, "codInstitucion")
				.add(Projections.property("centro_aten.descripcion")	, "descripcion")
		))
		
		.setResultTransformer(Transformers.aliasToBean(DtoCentrosAtencion.class));
		
		ArrayList<DtoCentrosAtencion> listaCentros=(ArrayList<DtoCentrosAtencion>)criteria.list();
		
		return listaCentros;
	} 

	/**
	 * M&eacute;todo encargado de obtener el listado de los centros de 
	 * atenci&oacute;n pertenecientes a un pa%iacute; determinado.
	 * 
	 * @param codigoPais
	 * @return listaCentro
	 *
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<DtoCentrosAtencion> listarTodosPorPais (String codigoPais) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentroAtencion.class, "centroAtencion")
			
			.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
			.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
			.createAlias("centroAtencion.ciudades", "ciudades");

		criteria.add(Restrictions.eq("ciudades.id.codigoPais", codigoPais))
				.addOrder(Order.asc("descripcion"));

		criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("consecutivo"),"consecutivo")
				.add(Projections.property("descripcion"),"descripcion")
				.add(Projections.property("ciudades.descripcion"),"descripcionCiudad")
				.add(Projections.property("regionesCobertura.descripcion"),"descripcionRegion"))
				.setResultTransformer( Transformers.aliasToBean(DtoCentrosAtencion.class));
				 ArrayList<DtoCentrosAtencion> listaCentro=(ArrayList)criteria.list();

		return listaCentro;
	}
	
	
	/**
	 * @return Lista con centros de atencion 
	 */
	public ArrayList<CentroAtencion> consultarTodosCentrosAtencion(){

		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(CentroAtencion.class,
						"centroAtencion");
		
		//SE CONSULTA POR RANGO INICIAL ASCENDENTEMENTE
		criteria.addOrder(Order.asc("centroAtencion.descripcion"));

		ArrayList<CentroAtencion> res = (ArrayList<CentroAtencion>) criteria.list();
		
		
		return res;
	}
	
	/**
	 * Este m&eacute;todo se encarga de retornar el listado con
	 * todos los centros de atenci&oacute;n activos asociados al usuario 
	 * 
	 * @return lista Centros Atencion - listado de todos los centros de atenci&oacute;n.
	 * @author Diecorqu
	 */
	public ArrayList<CentroAtencion> listarCentrosAtencionActivosUsuario(String login) {
	
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentroAtencion.class,"centroAtencion")
			.createAlias("centroAtencion.centrosCostos", "centrosCostos")
			.createAlias("centrosCostos.usuarioses", "usuario")
		    .add(Restrictions.eq("usuario.login", login))
		    .add(Restrictions.eq("centroAtencion.activo", true))
		    .addOrder(Order.asc("centroAtencion.descripcion"))
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 return (ArrayList<CentroAtencion>)criteria.list();
	}
}
