package com.servinte.axioma.orm.delegate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;

import util.MD5Hash;
import util.UtilidadTexto;

import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.orm.Personas;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.UsuariosHome;
import com.servinte.axioma.orm.UsuariosInactivos;

@SuppressWarnings("unchecked")
public class UsuariosDelegate extends UsuariosHome{
	
	
	public Usuarios validarUsuario(String usuario, String password)
	{
		DetachedCriteria dc=
			DetachedCriteria.forClass(UsuariosInactivos.class).
			setProjection(Property.forName("usuarios.login"));
		Usuarios objetoUsuario = null;
		if(usuario != null && password != null && !"".equals(password)) {
			objetoUsuario = (Usuarios)sessionFactory.getCurrentSession().createCriteria(Usuarios.class).
				add(Expression.eq("login", usuario)).
				//add(Expression.sql("password=MD5(?)", password, Hibernate.STRING)).
				add(Expression.eq("password", MD5Hash.hashPassword(password))).
				add(Subqueries.notIn("login", dc)).
				uniqueResult();
		}
		return objetoUsuario;
	}
	
	
	public Usuarios findById(String login) {
		//UtilidadTransaccion.getTransaccion().begin();
		Usuarios  usuario=super.findById(login);
		
		if(usuario!=null){
			usuario.getPersonas().getCodigo();
			usuario.getPersonas().getPrimerApellido();
			usuario.getPersonas().getTiposPersonas().getCodigo();
		}
		
		return usuario;
	}

	/**
	 * Método encargado de buscar un usuario y adicionarle una persona de
	 * acuerdo al login que se envíe por parámetro.
	 * 
	 * @param login  Login de usuario a buscar
	 * @return usuario  Usuario al que le corresponda el login
	 * 
	 * @author Luis Fernando Hincapié Ospina
	 * @since 17/01/2011
	 */
	public Usuarios findByIdLogin(String login) {
		Usuarios  usuario=super.findById(login);
		if(usuario!=null){
			DtoUsuarioPersona dtoUsuarioPersona = new DtoUsuarioPersona();

			dtoUsuarioPersona = this.obtenerDtoUsuarioPersona(login);
			usuario.setPersonas(new Personas());
			usuario.getPersonas().setPrimerNombre(dtoUsuarioPersona.getNombre());
			usuario.getPersonas().setSegundoNombre(dtoUsuarioPersona.getSegundoNombre());
			usuario.getPersonas().setPrimerApellido(dtoUsuarioPersona.getApellido());
			usuario.getPersonas().setSegundoApellido(dtoUsuarioPersona.getSegundoApellido());

			usuario.getPersonas().getPrimerApellido();
			
		}

		return usuario;
	}
	
	
	/**
	 * Retorna Los usuarios activos relacionados con el centro de atencion y diferentes del usuario enviado
	 * @param codInstitucion
	 * @param loginUsuario
	 * @param incluirInactivos
	 * @return List<DtoUsuarioPersona>
	 *
	 * @autor Cristhian Murillo
	 */
	public List<DtoUsuarioPersona> obtenerUsuariosActivosDiferenteDe(int codInstitucion, String loginUsuario, boolean incluirInactivos)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Usuarios.class, "usu");
		criteria.createAlias("personas", 		"per");
		
		if(!incluirInactivos){
			DetachedCriteria subQuery = DetachedCriteria.forClass(UsuariosInactivos.class);
			subQuery.setProjection(Projections.property("usuarios.login"));
			criteria.add(Subqueries.notIn("login", subQuery));
		}
		
		criteria.add(Restrictions.eq("instituciones.codigo", codInstitucion));
		
		if(!UtilidadTexto.isEmpty(loginUsuario)){
			criteria.add(Restrictions.ne("usu.login", loginUsuario));
		}
		
		criteria.setProjection(Projections.projectionList()
					.add( Projections.property("usu.login")				, "login")
					.add( Projections.property("per.primerNombre")		, "nombre")
					.add( Projections.property("per.primerApellido")	, "apellido")
					.add( Projections.property("per.segundoNombre")		, "segundoNombre")
					.add( Projections.property("per.segundoApellido")	, "segundoApellido")
		);
		
		criteria.addOrder(Order.asc("per.primerApellido"));
			
		criteria.setResultTransformer( Transformers.aliasToBean(DtoUsuarioPersona.class));
		
		return criteria.list();
	}
	
	
	
	/**
	 * Retorna informacion muy basica sobre un usuario/persona del sistema
	 * @param loginUsuario
	 * @return DtoUsuarioPersona
	 */
	public DtoUsuarioPersona obtenerDtoUsuarioPersona(String loginUsuario)
	{
		DtoUsuarioPersona dtoUsuarioPersona = (DtoUsuarioPersona) sessionFactory.getCurrentSession()
			.createCriteria(Usuarios.class, "usu")
			.createAlias("personas", 		"per")
			.createAlias("per.tiposIdentificacion", "tiposID")
			.add(Restrictions.eq("usu.login", loginUsuario))
			.setProjection(Projections.projectionList()
					.add( Projections.property("usu.login")			, "login")
					.add( Projections.property("per.primerNombre")	, "nombre")
					.add( Projections.property("per.primerApellido"), "apellido")
					.add( Projections.property("per.segundoNombre"), "segundoNombre")
					.add( Projections.property("per.segundoApellido"), "segundoApellido")					
					.add( Projections.property("tiposID.acronimo"), "acronimoTipoID")
					.add( Projections.property("per.numeroIdentificacion"), "numeroID")
			)
			
		.setResultTransformer( Transformers.aliasToBean(DtoUsuarioPersona.class) )
		.uniqueResult();
		
		return dtoUsuarioPersona;
	}


	/**
	 * 
	 * @param institucion
	 * @param filtrarActivos
	 * @return
	 */
	public ArrayList<DtoUsuarioPersona> obtenerUsuariosSistemas(int institucion, boolean filtrarActivos) {
		DetachedCriteria subQuery=DetachedCriteria.forClass(UsuariosInactivos.class);
		subQuery.setProjection(Projections.property("usuarios.login"));
		
		Criteria criteria= sessionFactory.getCurrentSession()
			.createCriteria(Usuarios.class, "usu")
			.createAlias("personas", 		"per");
			
			if(filtrarActivos)
				criteria.add(Subqueries.notIn("login", subQuery));
			
			criteria
			.add(Restrictions.eq("instituciones.codigo", institucion))
			.addOrder(Order.asc("per.primerApellido"))
			.setProjection(Projections.projectionList()
					.add( Projections.property("usu.login")				, "login")
					.add( Projections.property("per.primerNombre")		, "nombre")
					.add( Projections.property("per.primerApellido")	, "apellido")
					.add( Projections.property("per.segundoNombre")		, "segundoNombre")
					.add( Projections.property("per.segundoApellido")	, "segundoApellido")
				)
			
			.setResultTransformer( Transformers.aliasToBean(DtoUsuarioPersona.class) );
		
		ArrayList<DtoUsuarioPersona> listaTestigos = (ArrayList<DtoUsuarioPersona>) criteria.list();
		
		return listaTestigos;
	}	
	
	
	
	/**
	 * Retorna Los usuarios activos e inactivos con permisos para centros de costo dependiendo del centro
	 * de atención enviado como parámetro
	 * @param consecutivoCentroAtencion
	 * @return List<{@link DtoUsuarioPersona}>
	 */
	public ArrayList<DtoUsuarioPersona> obtenerUsuariosConPermisosCentroCostoPorCentroAtencion(int consecutivoCentroAtencion)
	{
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Usuarios.class, "usuarios")
		.createAlias("personas", 		"per")
		.createAlias("usuarios.centrosCostos", "centroCosto")
		.createAlias("centroCosto.centroAtencion", "centroAtencion");
		
		ProjectionList projection = Projections.projectionList();
		criteria.add(Restrictions.eq("centroAtencion.consecutivo", consecutivoCentroAtencion));
		
		projection.add(Projections.property("usuarios.login"),"login");
		projection.add( Projections.property("per.primerNombre")		, "nombre");
		projection.add( Projections.property("per.primerApellido")	, "apellido");
		projection.add( Projections.property("per.segundoNombre")		, "segundoNombre");
		projection.add( Projections.property("per.segundoApellido")	, "segundoApellido");
		
		criteria.setProjection(projection);
		criteria.addOrder( Order.asc("per.primerApellido") );
		criteria.addOrder( Order.asc("per.segundoApellido") );
		criteria.addOrder( Order.asc("per.primerNombre") );
		criteria.addOrder( Order.asc("per.segundoNombre") );
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoUsuarioPersona.class));
		ArrayList<DtoUsuarioPersona> listadoUsuarios=
			(ArrayList<DtoUsuarioPersona>)criteria.list();
		
		return listadoUsuarios;
	}
	
	
	
	
	
	
}