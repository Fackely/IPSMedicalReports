/**
 * 
 */
package com.servinte.axioma.orm.delegate.capitacion;

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

import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.orm.UsuariosCapitados;
import com.servinte.axioma.orm.UsuariosCapitadosHome;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio para Usuarioscapitados
 * @author Cristhian Murillo
 */
@SuppressWarnings("unchecked")
public class UsuariosCapitadosDelegate extends UsuariosCapitadosHome 
{


	/**
	 * Lista todos
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<UsuariosCapitados> listarTodos()
	{
		return (ArrayList<UsuariosCapitados>) sessionFactory.getCurrentSession()
			.createCriteria(UsuariosCapitados.class)
			.list();
	}
	
	
	
	/**
	 * Busca un usuario en las estructuras d eusuarios capitados
	 * @param parametrosBusqueda
	 * @return ArrayList<DtoUsuariosCapitados>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoUsuariosCapitados> buscarUsuariosCapitados(DtoUsuariosCapitados parametrosBusqueda)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UsuariosCapitados.class,"usuariosCapitados");
	
		criteria.createAlias("usuariosCapitados.convUsuariosCapitadoses"	, "convUsuariosCapitadoses"	, Criteria.LEFT_JOIN);
		criteria.createAlias("convUsuariosCapitadoses.tiposAfiliado"		, "tipoAfiliado"			, Criteria.LEFT_JOIN);		
		criteria.createAlias("convUsuariosCapitadoses.contratos"			, "contrato"				, Criteria.LEFT_JOIN);
		criteria.createAlias("contrato.convenios"							, "convenio"				, Criteria.LEFT_JOIN);
		criteria.createAlias("convenio.tiposContrato"						, "tipoContrato"            , Criteria.LEFT_JOIN);
		criteria.createAlias("convUsuariosCapitadoses.estratosSociales"		, "estratoSocial"		    , Criteria.LEFT_JOIN);
		criteria.createAlias("convUsuariosCapitadoses.tiposCargue"			, "tipoCargue"				, Criteria.LEFT_JOIN);		
		criteria.createAlias("convUsuariosCapitadoses.centroAtencion"		, "centroAtencion"			, Criteria.LEFT_JOIN);
		criteria.createAlias("usuariosCapitados.ciudades"					, "ciudades"				, Criteria.LEFT_JOIN);
		criteria.createAlias("usuariosCapitados.barrios"					, "barrios"					, Criteria.LEFT_JOIN);
		criteria.createAlias("ciudades.departamentos"						, "departamentos"			, Criteria.LEFT_JOIN);
		criteria.createAlias("departamentos.paises"							, "paises"					, Criteria.LEFT_JOIN);		
		criteria.createAlias("usuariosCapitados.sexo"						, "sexo"					, Criteria.LEFT_JOIN);
		criteria.createAlias("convUsuariosCapitadoses.tiposParentesco"		, "tipoParentesco"			, Criteria.LEFT_JOIN);
		criteria.createAlias("convUsuariosCapitadoses.naturalezaPacientes"	, "naturalezaPacientes"		, Criteria.LEFT_JOIN);
		
		
		boolean buscarPorLike = parametrosBusqueda.isBuscarPorLike();
		
		// Parametro convenio
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getConvenio()))
		{
			Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
			criteria.add(Restrictions.eq("convenio.codigo"						, Integer.parseInt(parametrosBusqueda.getConvenio())));
			criteria.add(Restrictions.ge("convUsuariosCapitadoses.fechaFinal"	, fechaActual));
			criteria.add(Restrictions.le("convUsuariosCapitadoses.fechaInicial"	, fechaActual));
		}
		//Parametro TipoUsuario/TipoAfiliado 
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getTipoAfiliado()+""))
		{
			criteria.add(Restrictions.eq("tipoAfiliado.acronimo"		, parametrosBusqueda.getTipoAfiliado()));
		}
		//Parametro Tipo Identificación
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getTipoIdentificacion()))
		{
			criteria.add(Restrictions.eq("usuariosCapitados.tipoIdentificacion"	, parametrosBusqueda.getTipoIdentificacion()));
		}
		//Parametro Número Identificación
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getNumeroIdentificacion()))
		{
			criteria.add(Restrictions.eq("usuariosCapitados.numeroIdentificacion", parametrosBusqueda.getNumeroIdentificacion()));
		}
		//Parametro Primer Nombre
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getPrimerNombre()))
		{
			if(buscarPorLike){
				criteria.add(Restrictions.like("usuariosCapitados.primerNombre", "%"+parametrosBusqueda.getPrimerNombre()+"%"));
			}else{
				criteria.add(Restrictions.eq("usuariosCapitados.primerNombre", parametrosBusqueda.getPrimerNombre()));
			}
		}
		//Parametro Segundo Nombre
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getSegundoNombre()))
		{
			if(buscarPorLike){
				criteria.add(Restrictions.like("usuariosCapitados.segundoNombre", "%"+parametrosBusqueda.getSegundoNombre()+"%"));
			}else{
				criteria.add(Restrictions.eq("usuariosCapitados.segundoNombre", parametrosBusqueda.getSegundoNombre()));
			}
		}
		//Parametro Primer Apellido
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getPrimerApellido()))
		{
			if(buscarPorLike){
				criteria.add(Restrictions.like("usuariosCapitados.primerApellido", "%"+parametrosBusqueda.getPrimerApellido()+"%"));
			}else{
				criteria.add(Restrictions.eq("usuariosCapitados.primerApellido", parametrosBusqueda.getPrimerApellido()));
			}
		}
		//Parametro Segundo Apellido
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getSegundoApellido()))
		{
			if(buscarPorLike){
				criteria.add(Restrictions.like("usuariosCapitados.segundoApellido", "%"+parametrosBusqueda.getSegundoApellido()+"%"));
			}else{
				criteria.add(Restrictions.eq("usuariosCapitados.segundoApellido", parametrosBusqueda.getSegundoApellido()));
			}
		}
		
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("convenio.codigo")							,"convenioInt");
			projectionList.add(Projections.property("convenio.nombre")							,"nombreConvenio");
			projectionList.add(Projections.property("tipoAfiliado.acronimo")					,"tipoAfiliado");
			projectionList.add(Projections.property("usuariosCapitados.tipoIdentificacion")		,"tipoIdentificacion");
			projectionList.add(Projections.property("usuariosCapitados.numeroIdentificacion")	,"numeroIdentificacion");
			projectionList.add(Projections.property("usuariosCapitados.primerNombre")			,"primerNombre");
			projectionList.add(Projections.property("usuariosCapitados.segundoNombre")			,"segundoNombre");
			projectionList.add(Projections.property("usuariosCapitados.primerApellido")			,"primerApellido");
			projectionList.add(Projections.property("usuariosCapitados.segundoApellido")		,"segundoApellido");
			projectionList.add(Projections.property("usuariosCapitados.fechaNacimiento")		,"fechaNacimiento");
			projectionList.add(Projections.property("usuariosCapitados.direccion")				,"direccion");
			projectionList.add(Projections.property("tipoContrato.nombre")						,"nombreTipoContrato");
			projectionList.add(Projections.property("tipoAfiliado.nombre")						,"nombreTipoAfiliado");
			projectionList.add(Projections.property("estratoSocial.descripcion")				,"descripcionEstratoSocial");
			projectionList.add(Projections.property("estratoSocial.codigo")						,"codigoEstratoSocial");
			//---------------------------------------------------------------------------------------------------------------
			projectionList.add(Projections.property("convUsuariosCapitadoses.fechaInicial")		,"fechaInicial");
			projectionList.add(Projections.property("convUsuariosCapitadoses.fechaFinal")		,"fechaFinal");
			projectionList.add(Projections.property("tipoCargue.codigo")						,"codigoTipoCargue");
			projectionList.add(Projections.property("usuariosCapitados.codigo")					,"codigoUsuarioCapitado");
			projectionList.add(Projections.property("contrato.codigo")							,"codigoContrato");
			//---------------------------------------------------------------------------------------------------------------
			projectionList.add(Projections.property("convUsuariosCapitadoses.tipoIdEmpleador")		,"tipoIdEmpleador");
			projectionList.add(Projections.property("convUsuariosCapitadoses.numeroIdEmpleador")			,"numIdEmpleador");
			projectionList.add(Projections.property("convUsuariosCapitadoses.razonSociEmpleador")		,"razonSociEmpleador");
			projectionList.add(Projections.property("convUsuariosCapitadoses.tipoIdCotizante")		,"tipoIdCotizante");
			projectionList.add(Projections.property("convUsuariosCapitadoses.numeroIdCotizante")			,"numIdCotizante");
			projectionList.add(Projections.property("convUsuariosCapitadoses.nombresCotizante")		,"nombresCotizante");
			projectionList.add(Projections.property("convUsuariosCapitadoses.apellidosCotizante")		,"apellidosCotizante");
			projectionList.add(Projections.property("naturalezaPacientes.nombre")			,"excepcionMonto");
			projectionList.add(Projections.property("usuariosCapitados.telefono")				,"telefono");
			projectionList.add(Projections.property("usuariosCapitados.email")					,"email");
			projectionList.add(Projections.property("convUsuariosCapitadoses.numeroFicha")			,"numeroFicha");			
			projectionList.add(Projections.property("centroAtencion.descripcion")				,"centroAtencion");
	    	projectionList.add(Projections.property("centroAtencion.consecutivo")				,"centroAtencionConsecutivo");
			projectionList.add(Projections.property("paises.descripcion")						,"pais");
			projectionList.add(Projections.property("departamentos.descripcion")				,"departamento");
			projectionList.add(Projections.property("ciudades.descripcion")						,"ciudad");
			projectionList.add(Projections.property("usuariosCapitados.localidad")				,"localidad");
			projectionList.add(Projections.property("barrios.descripcion")						,"barrio");
			projectionList.add(Projections.property("barrios.codigo")							,"codigoBarrio");
			projectionList.add(Projections.property("sexo.nombre")								,"sexo");
			projectionList.add(Projections.property("sexo.codigo")							    ,"codigoSexo");
			projectionList.add(Projections.property("tipoParentesco.nombre")					,"parentesco");
			
			
		criteria.setProjection(projectionList);
	
		criteria.setResultTransformer( Transformers.aliasToBean(DtoUsuariosCapitados.class));
	
		ArrayList<DtoUsuariosCapitados> listaResultado = (ArrayList<DtoUsuariosCapitados>) criteria.list();
		
		return listaResultado;
	}
	
	
	
	@Override
	public void delete(UsuariosCapitados persistentInstance) {
		super.delete(persistentInstance);
	}
	
	@Override
	public void attachDirty(UsuariosCapitados persistentInstance) {
		super.attachDirty(persistentInstance);
	}
	
	
	@Override
	public UsuariosCapitados findById(long id) {
		return super.findById(id);
	}
	
	@Override
	public UsuariosCapitados merge(UsuariosCapitados instance) {
		return super.merge(instance);
	}
	
	@Override
	public void persist(UsuariosCapitados persistentInstance) {
		super.persist(persistentInstance);
	}
	
	@Override
	public void attachClean(UsuariosCapitados persistentInstance) {
		super.attachClean(persistentInstance);
	}
	
	public Long buscarCodigoUsuarioCapitadoPorTipoNumeroID(String tipoId, String numeroId) {
		String consulta="SELECT uc.codigo FROM UsuariosCapitados uc "+
						"WHERE uc.tipoIdentificacion =:tipoId "+
							"AND uc.numeroIdentificacion =:numeroId"; 
		Query query = sessionFactory.getCurrentSession().createQuery(consulta);
		query.setParameter("tipoId", tipoId, Hibernate.STRING);
		query.setParameter("numeroId", numeroId, Hibernate.STRING);
		List<Object> lista = query.list();
		if(lista!= null && !lista.isEmpty()){
			return (Long)lista.get(0);
		}
		return null;
	}


}
