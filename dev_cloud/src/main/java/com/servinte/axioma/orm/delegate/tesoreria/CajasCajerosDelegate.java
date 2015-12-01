package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dto.tesoreria.DtoCajaCajeros;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.CajasCajeros;
import com.servinte.axioma.orm.CajasCajerosHome;
import com.servinte.axioma.orm.Personas;
import com.servinte.axioma.orm.Usuarios;


/**
 * 
 * @author Cristhian Murillo
 */
@SuppressWarnings({ "unchecked", "unused" })
public class CajasCajerosDelegate extends CajasCajerosHome {
	

	
	/**
	 * Retorna la relaci&oacute;n de las cajas de el usuario enviado y que este activo
	 * @param usuario
	 * @return
	 */
	public ArrayList<CajasCajeros> obtenerCajasCajero(UsuarioBasico usuario)
	{
		ArrayList<CajasCajeros> listaCajasCajeros = (ArrayList<CajasCajeros>) sessionFactory.getCurrentSession()
			.createCriteria(CajasCajeros.class)
			.add(Expression.eq("activo", true))
			.add(Expression.eq("usuarios.login", usuario.getLoginUsuario()))
			.list();
		 
		 return listaCajasCajeros;
	}
	
	/**
	 * Metodo para obtener todas las cajas por cajero que se encuentren activas
	 * @author Diana Ruiz
	 * @since 10/10/2011
	 * @return ArrayList
	 * 
	 */
	public ArrayList<CajasCajeros> obtenerTodasCajasCajero(int codigo_caja)
	{
		ArrayList<CajasCajeros> listaCajasCajeros = (ArrayList<CajasCajeros>) sessionFactory.getCurrentSession()
			.createCriteria(CajasCajeros.class)
			.add(Expression.eq("activo", true))
			.add(Expression.eq("cajas.consecutivo", codigo_caja))
			.list();
		 
		 return listaCajasCajeros;
	}

	
	/**
	 * Retorna Los cajeros relacionados en cajas_cajeros
	 * @return List<{@link DtoUsuarioPersona}>
	 */
	public List<DtoUsuarioPersona> obtenerListadoCajeros()
	{
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(CajasCajeros.class)
			.createAlias("usuarios", "usuarios")
			.createAlias("usuarios.personas", "persona")
			;
		
		ProjectionList projection = Projections.projectionList();
		criteria.add(Restrictions.eq("activo",true));
		
		projection.add( Projections.property("persona.primerApellido"), "apellido" );
		projection.add( Projections.property("persona.segundoApellido"), "segundoApellido" );
		projection.add( Projections.property("persona.primerNombre"), "nombre" );
		projection.add( Projections.property("persona.segundoNombre"), "segundoNombre" );
		projection.add( Projections.property("usuarios.login"), "login" );
		projection.add( Projections.property("persona.codigo"), "codigo" );
		
		
		criteria.setProjection(Projections.distinct(projection));
		criteria.addOrder( Order.asc("persona.primerApellido") );
		criteria.addOrder( Order.asc("persona.segundoApellido") );
		criteria.addOrder( Order.asc("persona.primerNombre") );
		criteria.addOrder( Order.asc("persona.segundoNombre") );
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoUsuarioPersona.class));
		ArrayList<DtoUsuarioPersona> listadoCajeros=
			(ArrayList<DtoUsuarioPersona>)criteria.list();
		
		return listadoCajeros;
	}
	
	/**
	 * Retorna El cajero con el codigo ingresado
	 * @return List<{@link DtoUsuarioPersona}>
	 */
	public DtoUsuarioPersona obtenerCajeroXCodigo(Integer codigoCajero)
	{
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(CajasCajeros.class)
			.createAlias("usuarios", "usuarios")
			.createAlias("usuarios.personas", "persona")
			;
		
		ProjectionList projection = Projections.projectionList();
		criteria.add(Restrictions.eq("persona.codigo"	, codigoCajero));
		
		projection.add( Projections.property("persona.primerApellido"), "apellido" );
		projection.add( Projections.property("persona.segundoApellido"), "segundoApellido" );
		projection.add( Projections.property("persona.primerNombre"), "nombre" );
		projection.add( Projections.property("persona.segundoNombre"), "segundoNombre" );
		projection.add( Projections.property("usuarios.login"), "login" );
		projection.add( Projections.property("persona.codigo"), "codigo" );
		
		
		criteria.setProjection(Projections.distinct(projection));
		criteria.addOrder( Order.asc("persona.primerApellido") );
		criteria.addOrder( Order.asc("persona.segundoApellido") );
		criteria.addOrder( Order.asc("persona.primerNombre") );
		criteria.addOrder( Order.asc("persona.segundoNombre") );
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoUsuarioPersona.class));
		DtoUsuarioPersona cajero=
			(DtoUsuarioPersona)criteria.uniqueResult();
		
		return cajero;
	}
	
	
	/**
	 * Retorna Los cajeros relacionados en cajas_cajeros
	 * @param usuario
	 * @return List<{@link CajasCajeros}>
	 */
	/*public List<Usuarios> obtenerListadoCajeros()
	{
		ArrayList<Usuarios> listadoCajeros = (ArrayList<Usuarios>) sessionFactory.getCurrentSession()
			.createCriteria(CajasCajeros.class)
			.createAlias("usuarios", "usuarios")
			//.createAlias("usuarios.personas", "persona")
			//.addOrder(Property.forName("persona.primerApellido").asc())
			.setProjection(Projections.distinct(Projections.projectionList()
				.add((Projections.property("usuarios")))
				//.add((Projections.property("persona.primerApellido")))
				))
			//.setProjection(Projections.property("persona.primerApellido"))
			//.addOrder(Order.asc("persona.primerApellido"))
			.add(Restrictions.eq("activo", true))
		//.setResultTransformer( Transformers.aliasToBean(Usuarios.class))
	         .list();

//		String query = "Usuarios as u FROM CajasCajeros as c WHERE c.usuarios.login = u.login ORDER BY u.personas.primerApellido asc";
//		
//		ArrayList<Usuarios> listadoCajeros = (ArrayList<Usuarios>) sessionFactory.getCurrentSession()
//			.createQuery(query).list();
//		
		for(Usuarios usuario: listadoCajeros)
		{
			usuario.getPersonas().getPrimerNombre();
		}
		
		return listadoCajeros;
		
		//return ordernarUsuariosPorApellido(listadoLoginCajeros);
		
	}*/
	
	
	private List<Usuarios> ordernarUsuariosPorApellido (ArrayList<String> listadoLoginCajeros){
		
		ArrayList<Usuarios> listadoCajeros = (ArrayList<Usuarios>) sessionFactory.getCurrentSession()
		.createCriteria(Personas.class)
		//.createAlias("usuarioses", "usuario")
		//.createAlias("usuarios.personas", "persona")
		//.addOrder(Property.forName("persona.primerApellido").asc())
		.setProjection(Projections.distinct(Projections.projectionList()
			.add((Projections.property("usuarioses")))
			//.add((Projections.property("persona.primerApellido")))
			))
		//.setProjection(Projections.property("persona.primerApellido"))
		//.addOrder(Order.asc("persona.primerApellido"))
		.add(Restrictions.in("usuarioses.login", listadoLoginCajeros))
	//.setResultTransformer( Transformers.aliasToBean(Usuarios.class))
         .list();
		

		for(Usuarios usuario: listadoCajeros)
		{
			usuario.getPersonas().getPrimerNombre();
		}
		
		return listadoCajeros;
	}
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los usuarios cajeros pertenecientes
	 * a un centro de atenci&oacute;n espec&iacute;fico
	 * 
	 * @param consecutivoCentroAtencion
	 * @return ArrayList<DtoUsuarioPersona> 
	 * 
	 * @author, Angela Maria Aguirre
	 * @author  Yennifer Guerrero
	 *
	 */
	public ArrayList<DtoUsuarioPersona> obtenerCajerosCentrosAtencion(int consecutivoCentroAtencion)
	{		
		ArrayList<DtoUsuarioPersona> listaCajasCajeros = (ArrayList<DtoUsuarioPersona>) sessionFactory.getCurrentSession()
			.createCriteria(CajasCajeros.class)		
			.createAlias("usuarios", "u")
			.createAlias("u.centrosCostos", "centroCosto")
			.createAlias("centroCosto.centroAtencion", "centroAtencion")
			.createAlias("u.personas", "p")
			
			.setProjection(Projections.distinct(Projections.projectionList()
					.add( Projections.property("p.primerNombre"), "nombre" )
					.add( Projections.property("p.primerApellido"), "apellido" )
					.add( Projections.property("u.login"), "login" )))
			.add(Restrictions.eq("activo", true))
			.add(Restrictions.eq("centroAtencion.consecutivo", consecutivoCentroAtencion))
			
			.setResultTransformer( Transformers.aliasToBean(DtoUsuarioPersona.class))
	         .list();
			
			
		return listaCajasCajeros;
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los usuarios cajeros pertenecientes 
	 * a una instituci&oacute;n en particular.
	 * 
	 * @param codigoInstitucion
	 * @return ArrayList<DtoUsuarioPersona> 
	 * 
	 * @author Yennifer Guerrero
	 *
	 */
	public ArrayList<DtoUsuarioPersona> obtenerCajerosPorInstitucion(int codigoInstitucion){
		
		ArrayList<DtoUsuarioPersona> listaCajasCajeros = (ArrayList<DtoUsuarioPersona>) sessionFactory
		.getCurrentSession()
			.createCriteria(CajasCajeros.class)			
			.createAlias("instituciones", "i")			
			.createAlias("usuarios", "u")
			.createAlias("u.personas", "p")
			
			.setProjection(Projections.distinct(Projections.projectionList()
					.add( Projections.property("p.primerNombre"), "nombre" )
					.add( Projections.property("p.primerApellido"), "apellido" )
					.add( Projections.property("u.login"), "login" )))
			.add(Restrictions.eq("activo", true))
			.add(Restrictions.eq("i.codigo", codigoInstitucion))
			.addOrder(Order.asc("p.primerNombre") )

			.setResultTransformer( Transformers.aliasToBean(DtoUsuarioPersona.class))
	         .list();
		
		return listaCajasCajeros;
	}
	
	/**
	 * @param codigoInstitucion
	 * @param centroAtencion
	 * @return lista con datos de cajeros 
	 */
	public List<DtoCajaCajeros> consultarCajaCajerosParametrizados(String codigoInstitucion, String centroAtencion){
		List<DtoCajaCajeros> listaCajaCajeros = new ArrayList<DtoCajaCajeros>();
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(CajasCajeros.class,"cajasCajeros")
		.createAlias("usuarios", "usuarios")
		.createAlias("cajasCajeros.cajas", "cajas")
		.createAlias("cajas.centroAtencion", "centroAtencion")
		.createAlias("usuarios.personas", "persona")
		.createAlias("cajasCajeros.instituciones", "instituciones")
		;
		
		if(!UtilidadTexto.isEmpty(codigoInstitucion)){
			criteria.add(Restrictions.eq("instituciones.codigo",Integer.valueOf(codigoInstitucion)));
		}
		
		if(!UtilidadTexto.isEmpty(centroAtencion)){
			criteria.add(Restrictions.eq("centroAtencion.consecutivo",Integer.valueOf(centroAtencion)));
		}
		
		criteria.addOrder( Order.asc("persona.primerApellido") );
		criteria.addOrder( Order.asc("persona.segundoApellido") );
		criteria.addOrder( Order.asc("persona.primerNombre") );
		criteria.addOrder( Order.asc("persona.segundoNombre") );
		
		
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("persona.primerApellido"),"primerApellido")
				.add(Projections.property("persona.segundoApellido"),"segundoApellido")
				.add(Projections.property("persona.primerNombre"),"primerNombre")
				.add(Projections.property("persona.segundoNombre"),"segundoNombre")
				.add(Projections.property("usuarios.login"),"login")
				.add(Projections.property("persona.codigo"),"codigo")
				)
				).setResultTransformer( Transformers.aliasToBean(DtoCajaCajeros.class));
		listaCajaCajeros=(ArrayList)criteria.list();
		
		return listaCajaCajeros;
		
	}
	
	/**
	 * @param codigoInstitucion
	 * @param centroAtencion
	 * @return lista con datos de cajas 
	 */
	public List<DtoCajaCajeros> consultarDatosCajaParametrizados(Integer codigoCajero,String codigoInstitucion, String centroAtencion){
			
			List<DtoCajaCajeros> listaCajaCajeros = new ArrayList<DtoCajaCajeros>();
			
			Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(CajasCajeros.class,"cajasCajeros")
			.createAlias("usuarios", "usuarios")
			.createAlias("cajasCajeros.cajas", "cajas")
			.createAlias("cajas.tiposCaja", "tiposCaja")
			.createAlias("cajas.centroAtencion", "centroAtencion")
			.createAlias("usuarios.personas", "persona")
			.createAlias("cajasCajeros.instituciones", "instituciones")
			;
			
			if(!UtilidadTexto.isEmpty(codigoInstitucion)){
				criteria.add(Restrictions.eq("instituciones.codigo",Integer.valueOf(codigoInstitucion)));
			}
			
			if(!UtilidadTexto.isEmpty(centroAtencion)){
				criteria.add(Restrictions.eq("centroAtencion.consecutivo",Integer.valueOf(centroAtencion)));
			}
			
			if(codigoCajero !=null && !UtilidadTexto.isEmpty(codigoCajero)){
				criteria.add(Restrictions.eq("persona.codigo",codigoCajero));
			}
			
			criteria.add(Restrictions.eq("tiposCaja.codigo",Integer.valueOf(ConstantesBD.codigoCajaRecaudo)));
			
			criteria.addOrder( Order.asc("cajas.descripcion") );
			
			criteria.setProjection(Projections.distinct(Projections.projectionList()
					.add(Projections.property("cajas.codigo"),"codigoCaja")
					.add(Projections.property("cajas.descripcion"),"descripcion")
					.add(Projections.property("centroAtencion.descripcion"),"centroAtencionCaja")
					.add(Projections.property("cajas.consecutivo"),"consecutivoCaja")
					)).setResultTransformer( Transformers.aliasToBean(DtoCajaCajeros.class));
			
			listaCajaCajeros=(ArrayList)criteria.list();
			
			return listaCajaCajeros;
			
		}
	
	@Override
	public CajasCajeros merge(CajasCajeros instance) {
		return super.merge(instance);
	}
	
	
}
