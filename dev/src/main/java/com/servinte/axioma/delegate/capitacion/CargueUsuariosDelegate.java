/**
 * 
 */
package com.servinte.axioma.delegate.capitacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;

import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.dto.capitacion.CargueDto;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.ConvUsuariosCapitados;
import com.servinte.axioma.orm.Personas;
import com.servinte.axioma.orm.UsuarioXConvenio;
import com.servinte.axioma.orm.UsuariosCapitados;

/**
 * Clase que permite el acceso a datos del cargue de usuarios
 * 
 * @author jeilones
 * @version 1.0
 * @created 3/10/2012
 *
 */
public class CargueUsuariosDelegate {
	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	/**
	 * Consulta los cargue de usuarios que coincida con los filtros de busqueda
	 * 
	 * @param filtrosUsuario
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 4/10/2012
	 */
	@SuppressWarnings("unchecked")
	public List<DtoUsuariosCapitados> consultarCargueUsuarios(DtoUsuariosCapitados filtrosUsuario) throws BDException{
		
		
		
		persistenciaSvc= new PersistenciaSvc();
		
		/**Consulta de usuarios capitados no ingresados**/
		
		Criteria criteria=persistenciaSvc.getSession().createCriteria(UsuariosCapitados.class.getName(),"usuarioCapitado");
		criteria.createAlias("usuarioCapitado.convUsuariosCapitadoses","convUsuarioCapitado",Criteria.INNER_JOIN);
		criteria.createAlias("convUsuarioCapitado.tiposAfiliado","tipoAfiliado",Criteria.LEFT_JOIN);
		
		if(filtrosUsuario.getTipoIdentificacion()!=null&&!filtrosUsuario.getTipoIdentificacion().trim().isEmpty()
				&&!filtrosUsuario.getTipoIdentificacion().equals(ConstantesBD.codigoNuncaValido+"")
				&&filtrosUsuario.getNumeroIdentificacion()!=null&&!filtrosUsuario.getNumeroIdentificacion().trim().isEmpty()){
			
			criteria.add(Restrictions.eq("usuarioCapitado.tipoIdentificacion", filtrosUsuario.getTipoIdentificacion()));
			//criteria.add(Restrictions.eq("usuarioCapitado.numeroIdentificacion", filtrosUsuario.getNumeroIdentificacion()));
			criteria.add(Restrictions.sqlRestriction("UPPER({alias}.numero_identificacion) like UPPER(?) ","%"+filtrosUsuario.getNumeroIdentificacion()+"%", Hibernate.STRING));
		}
		
		if(filtrosUsuario.getPrimerApellido()!=null&&!filtrosUsuario.getPrimerApellido().trim().isEmpty()){
			//criteria.add(Restrictions.like("usuarioCapitado.primerApellido", "%"+filtrosUsuario.getPrimerApellido()+"%"));
			criteria.add(Restrictions.sqlRestriction("UPPER({alias}.primer_apellido) like UPPER(?) ","%"+filtrosUsuario.getPrimerApellido()+"%", Hibernate.STRING));
		}
		
		if(filtrosUsuario.getSegundoApellido()!=null&&!filtrosUsuario.getSegundoApellido().trim().isEmpty()){
			//criteria.add(Restrictions.like("usuarioCapitado.segundoApellido", "%"+filtrosUsuario.getSegundoApellido()+"%"));
			criteria.add(Restrictions.sqlRestriction("UPPER({alias}.segundo_apellido) like UPPER(?) ","%"+filtrosUsuario.getSegundoApellido()+"%", Hibernate.STRING));
		}
		
		if(filtrosUsuario.getPrimerNombre()!=null&&!filtrosUsuario.getPrimerNombre().trim().isEmpty()){
			//criteria.add(Restrictions.like("usuarioCapitado.primerNombre", "%"+filtrosUsuario.getPrimerNombre()+"%"));
			criteria.add(Restrictions.sqlRestriction("UPPER({alias}.primer_nombre) like UPPER(?) ","%"+filtrosUsuario.getPrimerNombre()+"%", Hibernate.STRING));
		}
		
		if(filtrosUsuario.getSegundoNombre()!=null&&!filtrosUsuario.getSegundoNombre().trim().isEmpty()){
			//criteria.add(Restrictions.like("usuarioCapitado.segundoNombre", "%"+filtrosUsuario.getSegundoNombre()+"%"));
			criteria.add(Restrictions.sqlRestriction("UPPER({alias}.segundo_nombre) like UPPER(?) ","%"+filtrosUsuario.getSegundoNombre()+"%", Hibernate.STRING));
		}
		
		if(filtrosUsuario.getTipoAfiliado()!=null){
			criteria.add(Restrictions.eq("tipoAfiliado.acronimo", filtrosUsuario.getTipoAfiliado()));
		}
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.groupProperty("usuarioCapitado.tipoIdentificacion"),"tipoIdentificacion")								
				.add(Projections.groupProperty("usuarioCapitado.numeroIdentificacion"),"numeroIdentificacion")
				.add(Projections.groupProperty("usuarioCapitado.primerApellido"),"primerApellido")
				.add(Projections.groupProperty("usuarioCapitado.segundoApellido"),"segundoApellido")
				.add(Projections.groupProperty("usuarioCapitado.primerNombre"),"primerNombre")
				.add(Projections.groupProperty("usuarioCapitado.segundoNombre"),"segundoNombre")
				
				.add(Projections.groupProperty("tipoAfiliado.acronimo"),"tipoAfiliado")
				.add(Projections.groupProperty("tipoAfiliado.nombre"),"nombreTipoAfiliado")
				
				.add(Projections.groupProperty("convUsuarioCapitado.activo"),"estadoCargue")
				.add(Projections.max("convUsuarioCapitado.fechaCargue"),"fechaCargue")
			));
		
		List<DtoUsuariosCapitados> usuariosCapitadosNoIngresados=new ArrayList<DtoUsuariosCapitados>(0);
		try {
			criteria.setResultTransformer(Transformers.aliasToBean(DtoUsuariosCapitados.class));

			usuariosCapitadosNoIngresados= (List<DtoUsuariosCapitados>)criteria.list();			
		} catch (SecurityException e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		} 
		
		/**Consulta de usuarios capitados no ingresados**/
		
		criteria=persistenciaSvc.getSession().createCriteria(Personas.class.getName(),"usuarioCapitado");
		criteria.createAlias("usuarioCapitado.usuarioXConvenios","convUsuarioCapitado",Criteria.INNER_JOIN);
		criteria.createAlias("usuarioCapitado.tiposIdentificacion","tipoIdentificacion",Criteria.INNER_JOIN);
		criteria.createAlias("convUsuarioCapitado.tiposAfiliado","tipoAfiliado",Criteria.LEFT_JOIN);
		
		if(filtrosUsuario.getTipoIdentificacion()!=null&&!filtrosUsuario.getTipoIdentificacion().trim().isEmpty()
				&&!filtrosUsuario.getTipoIdentificacion().equals(ConstantesBD.codigoNuncaValido+"")
				&&filtrosUsuario.getNumeroIdentificacion()!=null&&!filtrosUsuario.getNumeroIdentificacion().trim().isEmpty()){
			
			criteria.add(Restrictions.eq("tipoIdentificacion.acronimo", filtrosUsuario.getTipoIdentificacion()));
			//criteria.add(Restrictions.eq("usuarioCapitado.numeroIdentificacion", filtrosUsuario.getNumeroIdentificacion()));
			criteria.add(Restrictions.sqlRestriction("UPPER({alias}.numero_identificacion) like UPPER(?) ","%"+filtrosUsuario.getNumeroIdentificacion()+"%", Hibernate.STRING));
		}
		
		if(filtrosUsuario.getPrimerApellido()!=null&&!filtrosUsuario.getPrimerApellido().trim().isEmpty()){
			//criteria.add(Restrictions.like("usuarioCapitado.primerApellido", "%"+filtrosUsuario.getPrimerApellido()+"%"));
			criteria.add(Restrictions.sqlRestriction("UPPER({alias}.primer_apellido) like UPPER(?) ","%"+filtrosUsuario.getPrimerApellido()+"%", Hibernate.STRING));
		}
		
		if(filtrosUsuario.getSegundoApellido()!=null&&!filtrosUsuario.getSegundoApellido().trim().isEmpty()){
			//criteria.add(Restrictions.like("usuarioCapitado.segundoApellido", "%"+filtrosUsuario.getSegundoApellido()+"%"));
			criteria.add(Restrictions.sqlRestriction("UPPER({alias}.segundo_apellido) like UPPER(?) ","%"+filtrosUsuario.getSegundoApellido()+"%", Hibernate.STRING));
		}
		
		if(filtrosUsuario.getPrimerNombre()!=null&&!filtrosUsuario.getPrimerNombre().trim().isEmpty()){
			//criteria.add(Restrictions.like("usuarioCapitado.primerNombre", "%"+filtrosUsuario.getPrimerNombre()+"%"));
			criteria.add(Restrictions.sqlRestriction("UPPER({alias}.primer_nombre) like UPPER(?) ","%"+filtrosUsuario.getPrimerNombre()+"%", Hibernate.STRING));
		}
		
		if(filtrosUsuario.getSegundoNombre()!=null&&!filtrosUsuario.getSegundoNombre().trim().isEmpty()){
			//criteria.add(Restrictions.like("usuarioCapitado.segundoNombre", "%"+filtrosUsuario.getSegundoNombre()+"%"));
			criteria.add(Restrictions.sqlRestriction("UPPER({alias}.segundo_nombre) like UPPER(?) ","%"+filtrosUsuario.getSegundoNombre()+"%", Hibernate.STRING));
		}
		
		if(filtrosUsuario.getTipoAfiliado()!=null){
			criteria.add(Restrictions.eq("tipoAfiliado.acronimo", filtrosUsuario.getTipoAfiliado()));
		}
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.groupProperty("tipoIdentificacion.acronimo"),"tipoIdentificacion")								
				.add(Projections.groupProperty("usuarioCapitado.numeroIdentificacion"),"numeroIdentificacion")
				.add(Projections.groupProperty("usuarioCapitado.primerApellido"),"primerApellido")
				.add(Projections.groupProperty("usuarioCapitado.segundoApellido"),"segundoApellido")
				.add(Projections.groupProperty("usuarioCapitado.primerNombre"),"primerNombre")
				.add(Projections.groupProperty("usuarioCapitado.segundoNombre"),"segundoNombre")
				
				.add(Projections.groupProperty("tipoAfiliado.acronimo"),"tipoAfiliado")
				.add(Projections.groupProperty("tipoAfiliado.nombre"),"nombreTipoAfiliado")
				
				.add(Projections.groupProperty("convUsuarioCapitado.activo"),"estadoCargue")
				.add(Projections.max("convUsuarioCapitado.fechaCargue"),"fechaCargue")
			));
		
		List<DtoUsuariosCapitados> usuariosCapitadosIngresados=new ArrayList<DtoUsuariosCapitados>(0);
		
		List<DtoUsuariosCapitados>usuariosCapitadosFiltrados=new ArrayList<DtoUsuariosCapitados>(0);
		try {
			criteria.setResultTransformer(Transformers.aliasToBean(DtoUsuariosCapitados.class));

			usuariosCapitadosIngresados= (List<DtoUsuariosCapitados>)criteria.list();
			
			HashMap<String, DtoUsuariosCapitados>mapaUsuarios=new HashMap<String, DtoUsuariosCapitados>(0);
			
			for(DtoUsuariosCapitados dtoUsuariosCapitados:usuariosCapitadosIngresados){
				if(!mapaUsuarios.containsKey(dtoUsuariosCapitados.getTipoIdentificacion()+"-"+dtoUsuariosCapitados.getNumeroIdentificacion())){
					mapaUsuarios.put(dtoUsuariosCapitados.getTipoIdentificacion()+"-"+dtoUsuariosCapitados.getNumeroIdentificacion(), dtoUsuariosCapitados);
					usuariosCapitadosFiltrados.add(dtoUsuariosCapitados);
					dtoUsuariosCapitados.setEsIngresado(true);
				}
			}
			
			for(DtoUsuariosCapitados dtoUsuariosCapitados:usuariosCapitadosNoIngresados){
				if(!mapaUsuarios.containsKey(dtoUsuariosCapitados.getTipoIdentificacion()+"-"+dtoUsuariosCapitados.getNumeroIdentificacion())){
					usuariosCapitadosFiltrados.add(dtoUsuariosCapitados);
					dtoUsuariosCapitados.setEsIngresado(false);
				}
			}
			
		} catch (SecurityException e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		} 
		return usuariosCapitadosFiltrados;
	}
	
	/**
	 * Consulta el grupo familiar de un usuario cotizante
	 * 
	 * @param usuarioCapitado
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 4/10/2012
	 */
	@SuppressWarnings("unchecked")
	public List<DtoUsuariosCapitados> consultarGrupoFamiliar(DtoUsuariosCapitados usuarioCapitado) throws BDException{
		List<DtoUsuariosCapitados> grupoFamiliar=new ArrayList<DtoUsuariosCapitados>(0);
		try {
			persistenciaSvc= new PersistenciaSvc();
			Criteria criteria=null;
			if(usuarioCapitado.isEsIngresado()){
				/**
				 * Se consulta las tablas personas y usuarios_x_convenios
				 * ya que la persona esta ingresada
				 * **/
				criteria=persistenciaSvc.getSession().createCriteria(Personas.class.getName(),"usuarioCapitado");
				criteria.createAlias("usuarioCapitado.usuarioXConvenios","convUsuarioCapitado",Criteria.INNER_JOIN);
				criteria.createAlias("usuarioCapitado.tiposIdentificacion","tipoIdentificacion",Criteria.INNER_JOIN);
			}else{
				/**
				 * Se consulta las tablas usuarios_capitados y conv_usuarios_capitados
				 * ya que no se ha ingresado el paciente
				 * **/
				criteria=persistenciaSvc.getSession().createCriteria(UsuariosCapitados.class.getName(),"usuarioCapitado");
				criteria.createAlias("usuarioCapitado.convUsuariosCapitadoses","convUsuarioCapitado",Criteria.INNER_JOIN);
			}
			
			criteria.createAlias("convUsuarioCapitado.tiposAfiliado","tipoAfiliado",Criteria.LEFT_JOIN);
			criteria.createAlias("convUsuarioCapitado.tiposParentesco","tipoParentesco",Criteria.LEFT_JOIN);
			
			criteria.add(Restrictions.ne("tipoAfiliado.acronimo", ConstantesBD.codigoTipoAfiliadoCotizante));
			
			if(usuarioCapitado.getTipoIdentificacion()!=null&&!usuarioCapitado.getTipoIdentificacion().trim().isEmpty()
					&&usuarioCapitado.getNumeroIdentificacion()!=null&&!usuarioCapitado.getNumeroIdentificacion().trim().isEmpty()){
				
				criteria.add(Restrictions.eq("convUsuarioCapitado.tipoIdCotizante", usuarioCapitado.getTipoIdentificacion()));
				criteria.add(Restrictions.eq("convUsuarioCapitado.numeroIdCotizante", usuarioCapitado.getNumeroIdentificacion()));
			}
		
			criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.groupProperty(usuarioCapitado.isEsIngresado()?"tipoIdentificacion.acronimo":"usuarioCapitado.tipoIdentificacion"),"tipoIdentificacion")								
				.add(Projections.groupProperty("usuarioCapitado.numeroIdentificacion"),"numeroIdentificacion")
				.add(Projections.groupProperty("usuarioCapitado.primerApellido"),"primerApellido")
				.add(Projections.groupProperty("usuarioCapitado.segundoApellido"),"segundoApellido")
				.add(Projections.groupProperty("usuarioCapitado.primerNombre"),"primerNombre")
				.add(Projections.groupProperty("usuarioCapitado.segundoNombre"),"segundoNombre")
				
				.add(Projections.groupProperty("usuarioCapitado.fechaNacimiento"),"fechaNacimiento")
				
				.add(Projections.groupProperty("tipoAfiliado.acronimo"),"tipoAfiliado")
				.add(Projections.groupProperty("tipoAfiliado.nombre"),"nombreTipoAfiliado")
				
				.add(Projections.groupProperty("tipoParentesco.nombre"),"parentesco")
				.add(Projections.groupProperty("usuarioCapitado.telefono"),"telefono")
				
				.add(Projections.max("convUsuarioCapitado.fechaCargue"),"fechaCargue")
			));
		
			criteria.setResultTransformer(Transformers.aliasToBean(DtoUsuariosCapitados.class));

			grupoFamiliar= (List<DtoUsuariosCapitados>)criteria.list();			
		} catch (SecurityException e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		} 
		return grupoFamiliar;
	}
	
	/**
	 * Consulta el historico de cargues que se le haya hecho a un usuario
	 * 
	 * @param usuarioCapitado
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 4/10/2012
	 */
	@SuppressWarnings("unchecked")
	public List<CargueDto> consultarHistoricoCargue(DtoUsuariosCapitados usuarioCapitado) throws BDException{
		List<CargueDto> historicoCargue=new ArrayList<CargueDto>(0);
		try{
			
			persistenciaSvc= new PersistenciaSvc();
			
			HashMap<String, Object>parametros=new HashMap<String, Object>(0);
			parametros.put("tipoIdentificacion", usuarioCapitado.getTipoIdentificacion());
			parametros.put("numeroIdentificacion", usuarioCapitado.getNumeroIdentificacion());
			
			List<Object[]>historicoCargueObject=new ArrayList<Object[]>(0);
			if(usuarioCapitado.isEsIngresado()){
				historicoCargueObject=(List<Object[]>)persistenciaSvc.createNamedQuery("cargueUsuarios.consultarCargueUsuariosIngresados",parametros);
			}else{
				historicoCargueObject=(List<Object[]>)persistenciaSvc.createNamedQuery("cargueUsuarios.consultarCargueUsuariosNoIngresados",parametros);
			}
			
			final int FECHA_INICIAL=0;
			final int FECHA_FINAL=1;
			final int FECHA_CARGUE=2;
			final int CODIGO_CONV=3;
			final int NOMBRE_CONV=4;
			final int CODIGO_CONT=5;
			final int NUMERO_CONTRATO=6;
			final int NOMBRE_TIPO_CARGUE=7;
			final int ESTADO_CARGUE=8;
			final int USUARIO_REALIZA_CARGUE=9;
			final int NOMBRE_NATURALEZA_PACIENTE=10;
			
			for(Object[]resultado:historicoCargueObject){
				ContratoDto contratoDto=new ContratoDto();
				if(resultado[CODIGO_CONT]!=null){
					contratoDto.setCodigo((Integer) resultado[CODIGO_CONT]);
				}
				contratoDto.setNumero((String) resultado[NUMERO_CONTRATO]);
				
				ConvenioDto convenioDto=new ConvenioDto();
				if(resultado[CODIGO_CONV]!=null){
					convenioDto.setCodigo((Integer)resultado[CODIGO_CONV]);
				}
				convenioDto.setNombre((String)resultado[NOMBRE_CONV]);
				
				contratoDto.setConvenio(convenioDto);
				
				CargueDto cargueDto=new CargueDto();
				
				cargueDto.setContratoDto(contratoDto);
				
				cargueDto.setFechaInicial((Date)resultado[FECHA_INICIAL]);
				cargueDto.setFechaFinal((Date)resultado[FECHA_FINAL]);
				cargueDto.setFechaCargue((Date)resultado[FECHA_CARGUE]);
				
				cargueDto.setNombreTipoCargue((String)resultado[NOMBRE_TIPO_CARGUE]);
				cargueDto.setEstado((String)resultado[ESTADO_CARGUE]);
				cargueDto.setUsuarioGeneraCargue((String)resultado[USUARIO_REALIZA_CARGUE]);
				
				cargueDto.setNombreNaturalezaPaciente((String)resultado[NOMBRE_NATURALEZA_PACIENTE]);
				
				historicoCargue.add(cargueDto);
			}
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return historicoCargue;
	}
	
	/**
	 * Consulta la informacion detallada de un usuario capitado
	 * 
	 * @param requiereTransaccion
	 * @param usuarioCapitado
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 5/10/2012
	 */
	public DtoUsuariosCapitados consultarDetalleUsuario(DtoUsuariosCapitados usuarioCapitado) throws BDException{
		persistenciaSvc= new PersistenciaSvc();
		
		Criteria criteria=null;
		if(usuarioCapitado.isEsIngresado()){
			/**
			 * Se consulta las tablas personas y usuarios_x_convenios
			 * ya que la persona esta ingresada
			 * **/
			criteria=persistenciaSvc.getSession().createCriteria(Personas.class.getName(),"usuarioCapitado");
			criteria.createAlias("usuarioCapitado.ciudadesByFkPCiudadviv","ciudadVivienda",Criteria.LEFT_JOIN);
			criteria.createAlias("usuarioCapitado.tiposIdentificacion","tipoIdentificacion",Criteria.INNER_JOIN);			
		}else{
			/**
			 * Se consulta las tablas usuarios_capitados y conv_usuarios_capitados
			 * ya que no se ha ingresado el paciente
			 * **/
			criteria=persistenciaSvc.getSession().createCriteria(UsuariosCapitados.class.getName(),"usuarioCapitado");
			criteria.createAlias("usuarioCapitado.ciudades","ciudadVivienda",Criteria.LEFT_JOIN);
		}

		criteria.createAlias("ciudadVivienda.departamentos","departamentoVivienda",Criteria.LEFT_JOIN);
		criteria.createAlias("ciudadVivienda.paises","paisVivienda",Criteria.LEFT_JOIN);
		criteria.createAlias("usuarioCapitado.barrios","barrio",Criteria.LEFT_JOIN);
		criteria.createAlias("barrios.localidades","localidad",Criteria.LEFT_JOIN);
		
		criteria.createAlias("usuarioCapitado.sexo","sexo",Criteria.LEFT_JOIN);
		
		if(usuarioCapitado.isEsIngresado()){
			criteria.add(Restrictions.eq("tipoIdentificacion.acronimo", usuarioCapitado.getTipoIdentificacion()));
		}else{
			criteria.add(Restrictions.eq("usuarioCapitado.tipoIdentificacion", usuarioCapitado.getTipoIdentificacion()));
		}
		criteria.add(Restrictions.eq("usuarioCapitado.numeroIdentificacion", usuarioCapitado.getNumeroIdentificacion()));
		
		Personas persona=null;
		UsuariosCapitados usuarioCapitadoSeleccionado=null;
		try {

			if(usuarioCapitado.isEsIngresado()){
				persona= (Personas) criteria.uniqueResult();			
			}else{
				usuarioCapitadoSeleccionado= (UsuariosCapitados) criteria.uniqueResult();
			}
		} catch (SecurityException e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		
		if(usuarioCapitado.isEsIngresado()){
			criteria=persistenciaSvc.getSession().createCriteria(UsuarioXConvenio.class.getName(),"convUsuarioCapitado");
			criteria.createAlias("convUsuarioCapitado.personas","usuarioCapitado");
			criteria.createAlias("usuarioCapitado.tiposIdentificacion","tipoIdentificacion",Criteria.INNER_JOIN);
		}else{
			criteria=persistenciaSvc.getSession().createCriteria(ConvUsuariosCapitados.class.getName(),"convUsuarioCapitado");
			criteria.createAlias("convUsuarioCapitado.usuariosCapitados","usuarioCapitado");
		}

		criteria.createAlias("convUsuarioCapitado.tiposParentesco","tipoParentesco",Criteria.LEFT_JOIN);
		
		criteria.createAlias("convUsuarioCapitado.estratosSociales","estratoSocial",Criteria.LEFT_JOIN);
		criteria.createAlias("convUsuarioCapitado.centroAtencion","centroAtencion",Criteria.LEFT_JOIN);
		
		if(usuarioCapitado.isEsIngresado()){
			criteria.add(Restrictions.eq("tipoIdentificacion.acronimo", usuarioCapitado.getTipoIdentificacion()));
		}else{
			criteria.add(Restrictions.eq("usuarioCapitado.tipoIdentificacion", usuarioCapitado.getTipoIdentificacion()));
		}
		criteria.add(Restrictions.eq("usuarioCapitado.numeroIdentificacion", usuarioCapitado.getNumeroIdentificacion()));
		
		criteria.addOrder(Order.desc("convUsuarioCapitado.fechaCargue"));
		
		criteria.setMaxResults(1);
		UsuarioXConvenio usuarioXConvenio=null;
		ConvUsuariosCapitados convUsuariosCapitados=null;
		try {
			List<?>resultado=criteria.list();;
			if(usuarioCapitado.isEsIngresado()){
				
				if(!resultado.isEmpty()){
					usuarioXConvenio= (UsuarioXConvenio) resultado.get(0);
				}
			}else{
				if(!resultado.isEmpty()){
					convUsuariosCapitados=(ConvUsuariosCapitados) resultado.get(0);
				}
			}
		} catch (SecurityException e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		
		if(usuarioCapitado.isEsIngresado()){
			usuarioCapitado.setFechaNacimiento(persona.getFechaNacimiento());
			if(persona.getSexo()!=null){
				usuarioCapitado.setSexo(persona.getSexo().getNombre());
			}
			usuarioCapitado.setDireccion(persona.getDireccion());
			if(persona.getCiudadesByFkPCiudadviv()!=null){
				if(persona.getCiudadesByFkPCiudadviv().getPaises()!=null){
					usuarioCapitado.setPais(persona.getCiudadesByFkPCiudadviv().getPaises().getDescripcion());
				}
				if(persona.getCiudadesByFkPCiudadviv().getDepartamentos()!=null){
					usuarioCapitado.setDepartamento(persona.getCiudadesByFkPCiudadviv().getDepartamentos().getDescripcion());
				}
				usuarioCapitado.setCiudad(persona.getCiudadesByFkPCiudadviv().getDescripcion());
			}
			if(persona.getBarrios()!=null){
				if(persona.getBarrios().getLocalidades()!=null){
					usuarioCapitado.setLocalidad(persona.getBarrios().getLocalidades().getDescripcion());
				}
				usuarioCapitado.setBarrio(persona.getBarrios().getDescripcion());
			}
			usuarioCapitado.setTelefono(persona.getTelefono());
			if(usuarioXConvenio!=null){
				if(usuarioXConvenio.getEstratosSociales()!=null){
					usuarioCapitado.setDescripcionEstratoSocial(usuarioXConvenio.getEstratosSociales().getDescripcion());
				}
				usuarioCapitado.setNumeroFicha(usuarioXConvenio.getNumeroFicha());
				if(usuarioXConvenio.getCentroAtencion()!=null){
					usuarioCapitado.setCentroAtencion(usuarioXConvenio.getCentroAtencion().getDescripcion());
				}
				usuarioCapitado.setTipoIdEmpleador(usuarioXConvenio.getTipoIdEmpleador());
				usuarioCapitado.setNumIdEmpleador(usuarioXConvenio.getNumeroIdEmpleador());
				usuarioCapitado.setRazonSociEmpleador(usuarioXConvenio.getRazonSociEmpleador());
				usuarioCapitado.setTipoIdCotizante(usuarioXConvenio.getTipoIdCotizante());
				usuarioCapitado.setNumIdCotizante(usuarioXConvenio.getNumeroIdCotizante());
				usuarioCapitado.setNombresCotizante(usuarioXConvenio.getNombresCotizante());
				usuarioCapitado.setApellidosCotizante(usuarioXConvenio.getApellidosCotizante());
				if(usuarioXConvenio.getTiposParentesco()!=null){
					usuarioCapitado.setParentesco(usuarioXConvenio.getTiposParentesco().getNombre());
				}
			}
		}else{
			usuarioCapitado.setFechaNacimiento(usuarioCapitadoSeleccionado.getFechaNacimiento());
			if(usuarioCapitadoSeleccionado.getSexo()!=null){
				usuarioCapitado.setSexo(usuarioCapitadoSeleccionado.getSexo().getNombre());
			}
			usuarioCapitado.setDireccion(usuarioCapitadoSeleccionado.getDireccion());
			if(usuarioCapitadoSeleccionado.getCiudades()!=null){
				if(usuarioCapitadoSeleccionado.getCiudades().getPaises()!=null){
					usuarioCapitado.setPais(usuarioCapitadoSeleccionado.getCiudades().getPaises().getDescripcion());
				}
				if(usuarioCapitadoSeleccionado.getCiudades().getDepartamentos()!=null){
					usuarioCapitado.setDepartamento(usuarioCapitadoSeleccionado.getCiudades().getDepartamentos().getDescripcion());
				}
				usuarioCapitado.setCiudad(usuarioCapitadoSeleccionado.getCiudades().getDescripcion());
			}
			if(usuarioCapitadoSeleccionado.getBarrios()!=null){
				if(usuarioCapitadoSeleccionado.getBarrios().getLocalidades()!=null){
					usuarioCapitado.setLocalidad(usuarioCapitadoSeleccionado.getBarrios().getLocalidades().getDescripcion());
				}
				usuarioCapitado.setBarrio(usuarioCapitadoSeleccionado.getBarrios().getDescripcion());
			}
			usuarioCapitado.setTelefono(usuarioCapitadoSeleccionado.getTelefono());
			if(convUsuariosCapitados!=null){
				if(convUsuariosCapitados.getEstratosSociales()!=null){
					usuarioCapitado.setDescripcionEstratoSocial(convUsuariosCapitados.getEstratosSociales().getDescripcion());
				}
				usuarioCapitado.setNumeroFicha(convUsuariosCapitados.getNumeroFicha());
				if(convUsuariosCapitados.getCentroAtencion()!=null){
					usuarioCapitado.setCentroAtencion(convUsuariosCapitados.getCentroAtencion().getDescripcion());
				}
				usuarioCapitado.setTipoIdEmpleador(convUsuariosCapitados.getTipoIdEmpleador());
				usuarioCapitado.setNumIdEmpleador(convUsuariosCapitados.getNumeroIdEmpleador());
				usuarioCapitado.setRazonSociEmpleador(convUsuariosCapitados.getRazonSociEmpleador());
				usuarioCapitado.setTipoIdCotizante(convUsuariosCapitados.getTipoIdCotizante());
				usuarioCapitado.setNumIdCotizante(convUsuariosCapitados.getNumeroIdCotizante());
				usuarioCapitado.setNombresCotizante(convUsuariosCapitados.getNombresCotizante());
				usuarioCapitado.setApellidosCotizante(convUsuariosCapitados.getApellidosCotizante());
				if(convUsuariosCapitados.getTiposParentesco()!=null){
					usuarioCapitado.setParentesco(convUsuariosCapitados.getTiposParentesco().getNombre());
				}
			}
		}
		
		return usuarioCapitado;
	}
}
