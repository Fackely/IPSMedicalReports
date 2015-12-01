/**
 * 
 */
package com.sies.hibernate.delegate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.servinte.axioma.orm.Personas;
import com.servinte.axioma.orm.PersonasHome;


/**
 * @author Juan David Ramírez López
 * Creado el 6/06/2008
 */
public class PersonasDelegate extends PersonasHome
{

	
	
	/**
	 * BuscarPersona
	 * @param identificacionBuscar
	 * @param acronimoTipoIdentificacion
	 * 
	 * @return Personas
	 */
	public Personas buscarPersona(String identificacionBuscar, String acronimoTipoIdentificacion)
	{
		Personas persona = (Personas) sessionFactory.getCurrentSession()
			
			.createCriteria(Personas.class, "per")
			
			.add(Restrictions.eq("per.numeroIdentificacion"			, identificacionBuscar))
			.add(Restrictions.eq("per.tiposIdentificacion.acronimo"	, acronimoTipoIdentificacion))
			
			.uniqueResult();
		
		if(persona!=null){
			if(persona.getSexo() != null){
			persona.getSexo().getNombre();
			}
			persona.getTiposIdentificacion().getNombre();
		}
		
		return persona;
	}

	/**
	 * Buscar una persona por el tipo y número de identificación
	 * @param dtoPersona DtoPersonas con los datos de la persona buscada
	 * @return DtoPersona con los datos de la persona buscada, null en caso de no existir el tipo y numero id 
	 */
	public DtoPersonas buscarPersona(DtoPersonas dtoPersona) {
		DtoPersonas persona = (DtoPersonas) sessionFactory.getCurrentSession()
		
		.createCriteria(Personas.class, "per")
		
		.add(Restrictions.eq("per.numeroIdentificacion"			, dtoPersona.getNumeroIdentificacion()))
		.add(Restrictions.eq("per.tiposIdentificacion.acronimo"	, dtoPersona.getTipoIdentificacion()))
		.setProjection(Projections.projectionList()
				.add(Projections.property("codigo"), "codigo")
				.add(Projections.property("numeroIdentificacion"), "numeroIdentificacion")
				.add(Projections.property("tiposIdentificacion.acronimo"), "tipoIdentificacion")
				.add(Projections.property("primerNombre"), "primerNombre")
				.add(Projections.property("segundoNombre"), "segundoNombre")
				.add(Projections.property("primerApellido"), "primerApellido")
				.add(Projections.property("segundoApellido"), "segundoApellido")
		)
		.setResultTransformer(Transformers.aliasToBean(DtoPersonas.class))
		
		.uniqueResult();
		return persona;
	}
	
	/**
	 * Buscar una persona por el codigo
	 * @param dtoPersona DtoPersonas con los datos de la persona buscada
	 * @return DtoPersona con los datos de la persona buscada, null en caso de no existir el tipo y numero id 
	 */
	public DtoPersonas buscarPersonaPorCodigo(int codigoPkPersona) {
		DtoPersonas persona = (DtoPersonas) sessionFactory.getCurrentSession()
	
		.createCriteria(Personas.class, "per")
	
		.add(Restrictions.eq("per.codigo", codigoPkPersona))
		.setProjection(Projections.projectionList()
				.add(Projections.property("codigo"), "codigo")
				.add(Projections.property("numeroIdentificacion"), "numeroIdentificacion")
				.add(Projections.property("tiposIdentificacion.acronimo"), "tipoIdentificacion")
				.add(Projections.property("primerNombre"), "primerNombre")
				.add(Projections.property("segundoNombre"), "segundoNombre")
				.add(Projections.property("primerApellido"), "primerApellido")
				.add(Projections.property("segundoApellido"), "segundoApellido")
				.add(Projections.property("email"), "email")
				.add(Projections.property("telefonoFijo"), "telefonoFijo")
				.add(Projections.property("direccion"), "direccion")
		)
		.setResultTransformer(Transformers.aliasToBean(DtoPersonas.class))
		.uniqueResult();
		return persona;
	}
	
	
	/**
	 * Busca una persona por los parametros enviados.
	 * 
	 * @param parametrosBusqueda
	 * @return ArrayList<DtoPersonas>
	 * 
	 * @author Camilo Gomez
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoPersonas> buscarPersonasPorNombresApellidos(DtoPersonas parametrosBusqueda)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Personas.class,"personas");
		
		// Join Persona
		criteria.createAlias("personas.tiposIdentificacion"		, "tiposIdentificacion");
		criteria.createAlias("personas.sexo"					, "sexo");
		criteria.createAlias("personas.barrios"					, "barrio");
		criteria.createAlias("personas.ciudadesByFkPCiudadviv"		, "ciudadResidencia");
		criteria.createAlias("ciudadResidencia.departamentos"		, "departamentoResidencia");
		criteria.createAlias("departamentoResidencia.paises"		, "paisResidencia");
		
		
		boolean buscarPorLike = parametrosBusqueda.isBuscarPorLike();

		//Parametro Tipo Identificación
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getTipoIdentificacion()))
		{
			criteria.add(Restrictions.eq("tiposIdentificacion.acronimo"	, parametrosBusqueda.getTipoIdentificacion()));
		}
		//Parametro Número Identificación
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getNumeroIdentificacion()))
		{
			criteria.add(Restrictions.eq("personas.numeroIdentificacion", parametrosBusqueda.getNumeroIdentificacion()));
		}
		//Parametro Primer Nombre
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getPrimerNombre()))
		{
			if(buscarPorLike){
				criteria.add(Restrictions.like("personas.primerNombre", "%"+parametrosBusqueda.getPrimerNombre()+"%"));
			}
			else{
				criteria.add(Restrictions.eq("personas.primerNombre", parametrosBusqueda.getPrimerNombre()));
			}
			
		}
		//Parametro Segundo Nombre
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getSegundoNombre()))
		{
			if(buscarPorLike){
				criteria.add(Restrictions.like("personas.segundoNombre", "%"+parametrosBusqueda.getSegundoNombre()+"%"));
			}else{
				criteria.add(Restrictions.eq("personas.segundoNombre", parametrosBusqueda.getSegundoNombre()));
			}
			
		}
		//Parametro Primer Apellido
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getPrimerApellido()))
		{
			if(buscarPorLike){
				criteria.add(Restrictions.like("personas.primerApellido", "%"+parametrosBusqueda.getPrimerApellido()+"%"));
			}else{
				criteria.add(Restrictions.eq("personas.primerApellido",parametrosBusqueda.getPrimerApellido()));
			}
		}
		//Parametro Segundo Apellido
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getSegundoApellido()))
		{
			if(buscarPorLike){
				criteria.add(Restrictions.like("personas.segundoApellido", "%"+parametrosBusqueda.getSegundoApellido()+"%"));
			}else{
				criteria.add(Restrictions.eq("personas.segundoApellido",parametrosBusqueda.getSegundoApellido()));
			}
		}
		
		
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("personas.codigo")					,"codigo");
			projectionList.add(Projections.property("tiposIdentificacion.acronimo")		,"tipoId");
			projectionList.add(Projections.property("tiposIdentificacion.nombre")		,"nombreTipoIdentificacion");
			projectionList.add(Projections.property("personas.numeroIdentificacion")	,"numeroIdentificacion");
			projectionList.add(Projections.property("personas.primerNombre")			,"primerNombre");
			projectionList.add(Projections.property("personas.segundoNombre")			,"segundoNombre");
			projectionList.add(Projections.property("personas.primerApellido")			,"primerApellido");
			projectionList.add(Projections.property("personas.segundoApellido")			,"segundoApellido");
			projectionList.add(Projections.property("personas.fechaNacimiento")			,"fechaNacimientoTipoDate");
			projectionList.add(Projections.property("personas.direccion")				,"direccion");
			projectionList.add(Projections.property("sexo.nombre")						,"sexo");
			projectionList.add(Projections.property("barrio.descripcion")				,"barrio");
			projectionList.add(Projections.property("personas.telefono")				,"telefono");
			projectionList.add(Projections.property("personas.email")					,"email");			
			projectionList.add(Projections.property("ciudadResidencia.descripcion")		,"municipio");
			projectionList.add(Projections.property("departamentoResidencia.descripcion")	,"departamento");
			projectionList.add(Projections.property("paisResidencia.descripcion")			,"pais");
			
			
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoPersonas.class));
		
		ArrayList<DtoPersonas> listaResultado = (ArrayList<DtoPersonas>) criteria.list();
		
		return listaResultado;
	}
	
	/**
	 * Este método se creo solo para la loógica de Cargue Masivo de Usuarios Capitados
	 * 
	 * Busca las personas de acuerdo a tipo de identificación, número de identificación
	 * primer nombre, segundo nombre, primer apellido, segundo apellido
	 * @param dtoPersona
	 * @return
	 * @author Ricardo Ruiz
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<DtoPersonas> buscarPersonasPorFiltro(DtoPersonas filtro)
	{
		List<DtoPersonas> listaPersonas = new ArrayList<DtoPersonas>();
		String consulta = "SELECT per.codigo, ti.acronimo, ti.nombre, per.numeroIdentificacion, "+
								"per.primerNombre, per.segundoNombre, per.primerApellido, "+
								"per.segundoApellido, per.fechaNacimiento "+
							"FROM Personas per INNER JOIN per.tiposIdentificacion ti ";
		Query query= null;
		if(filtro.getTipoIdentificacion() != null && !filtro.getTipoIdentificacion().trim().isEmpty()
				&& filtro.getNumeroIdentificacion() != null && !filtro.getNumeroIdentificacion().trim().isEmpty()){
			consulta+="WHERE ti.acronimo = :tipoId AND per.numeroIdentificacion = :numeroId";
			query=sessionFactory.getCurrentSession().createQuery(consulta);
			query.setParameter("tipoId", filtro.getTipoIdentificacion(), Hibernate.STRING);
			query.setParameter("numeroId", filtro.getNumeroIdentificacion(), Hibernate.STRING);
		}
		else{
			boolean segundoNombre=false;
			boolean segundoApellido=false;
			if(filtro.getSegundoNombre() != null && !filtro.getSegundoNombre().trim().isEmpty()){
				segundoNombre=true;
			}
			if(filtro.getSegundoApellido() != null && !filtro.getSegundoApellido().trim().isEmpty()){
				segundoApellido=true;
			}
			if(segundoNombre && segundoApellido){
				consulta+="WHERE per.primerNombre = :primerNombre AND per.segundoNombre = :segundoNombre "+
								"AND per.primerApellido = :primerApellido AND per.segundoApellido = :segundoApellido";
				query=sessionFactory.getCurrentSession().createQuery(consulta);
				query.setParameter("primerNombre", filtro.getPrimerNombre(), Hibernate.STRING);
				query.setParameter("segundoNombre", filtro.getSegundoNombre(), Hibernate.STRING);
				query.setParameter("primerApellido", filtro.getPrimerApellido(), Hibernate.STRING);
				query.setParameter("segundoApellido", filtro.getSegundoApellido(), Hibernate.STRING);
			}
			else if(segundoNombre && !segundoApellido){
				consulta+="WHERE per.primerNombre = :primerNombre AND per.segundoNombre = :segundoNombre "+
								"AND per.primerApellido = :primerApellido";
				query=sessionFactory.getCurrentSession().createQuery(consulta);
				query.setParameter("primerNombre", filtro.getPrimerNombre(), Hibernate.STRING);
				query.setParameter("segundoNombre", filtro.getSegundoNombre(), Hibernate.STRING);
				query.setParameter("primerApellido", filtro.getPrimerApellido(), Hibernate.STRING);
			}
			else if(!segundoNombre && segundoApellido){
				consulta+="WHERE per.primerNombre = :primerNombre "+
								"AND per.primerApellido = :primerApellido AND per.segundoApellido = :segundoApellido";
				query=sessionFactory.getCurrentSession().createQuery(consulta);
				query.setParameter("primerNombre", filtro.getPrimerNombre(), Hibernate.STRING);
				query.setParameter("primerApellido", filtro.getPrimerApellido(), Hibernate.STRING);
				query.setParameter("segundoApellido", filtro.getSegundoApellido(), Hibernate.STRING);
			}
			else{
				consulta+="WHERE per.primerNombre = :primerNombre AND per.primerApellido = :primerApellido";
				query=sessionFactory.getCurrentSession().createQuery(consulta);
				query.setParameter("primerNombre", filtro.getPrimerNombre(), Hibernate.STRING);
				query.setParameter("primerApellido", filtro.getPrimerApellido(), Hibernate.STRING);
			}
		}
		
		List<Object[]> lista = query.list();
		if(lista != null & !lista.isEmpty()){
			for(Object[] result:lista){
				DtoPersonas dto = new DtoPersonas();
				dto.setCodigo((Integer)result[0]);
				dto.setTipoId((String)result[1]);
				dto.setNombreTipoIdentificacion((String)result[2]);
				dto.setNumeroIdentificacion((String)result[3]);
				dto.setPrimerNombre((String)result[4]);
				if(result[5] != null){
					dto.setSegundoNombre((String)result[5]);
				}
				dto.setPrimerApellido((String)result[6]);
				if(result[7] != null){
					dto.setSegundoApellido((String)result[7]);
				}
				if(result[8] != null){
					dto.setFechaNacimientoTipoDate((Date)result[8]);
				}
				listaPersonas.add(dto);
			}
		}
		return listaPersonas;
	}
	
}


