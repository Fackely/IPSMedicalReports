package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;

import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;
import com.princetonsa.dto.ordenes.DtoOrdenesAmbulatorias;
import com.servinte.axioma.orm.Medicos;
import com.servinte.axioma.orm.MedicosHome;

/**
 * Esta clase se encarga de obtener la informaci&oacute;n requerida 
 * de la base de datos.
 *
 * @author Yennifer Guerrero
 * @since	06/09/2010
 *
 */
public class MedicosDelegate extends MedicosHome {
	
	/**
	 * Este m&eacute;todo se encarga de obtener los profesionales
	 * de la salud activos e inactivos en el sistema con ocupaciones 
	 * de odont&oacute;logo y auxiliar de odontolog&iacute;.
	 * @return
	 * @author Yennifer Guerrero
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<DtoPersonas> obtenerTodosMedicosOdonto (int codigoInstitucion){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Medicos.class, "medicos")
		.createAlias("medicos.personas", "personas")
		.createAlias("ocupacionesMedicas", "ocupacion")
		.createAlias("personas.usuarioses", "usuarios");
		
		int codigoOcupacionOdonto= 0;
		int codigoOcupacionAuxiOdonto = 0;
		
		String ocupacionOdon=ValoresPorDefecto.getOcupacionOdontologo(codigoInstitucion);
		String ocupacionAuxi = ValoresPorDefecto.getOcupacionAuxiliarOdontologo(codigoInstitucion);
		
		if(!UtilidadTexto.isEmpty(ocupacionOdon))
		{
			codigoOcupacionOdonto=Integer.parseInt(ocupacionOdon);
		}
		
		if(!UtilidadTexto.isEmpty(ocupacionAuxi))
		{
			codigoOcupacionAuxiOdonto=Integer.parseInt(ocupacionAuxi);
		}
		
		if (codigoOcupacionOdonto > 0 && codigoOcupacionAuxiOdonto > 0) {
			
			criteria.add(Restrictions.or(
					Restrictions.eq("ocupacion.codigo", codigoOcupacionOdonto), 
					Restrictions.eq("ocupacion.codigo", codigoOcupacionAuxiOdonto)
					));
			
		} else {
			
			if (codigoOcupacionOdonto > 0) {
				criteria.add(Restrictions.eq("ocupacion.codigo", codigoOcupacionOdonto));
			}else if (codigoOcupacionAuxiOdonto > 0) {
				criteria.add(Restrictions.eq("ocupacion.codigo", codigoOcupacionAuxiOdonto));
			}

		}
		
		criteria.setProjection(Projections.projectionList()
				.add( Projections.property("usuarios.login"), "login")
				.add( Projections.property("personas.primerNombre"), "primerNombre")
				.add( Projections.property("personas.segundoNombre"), "segundoNombre")
				.add( Projections.property("personas.primerApellido"), "primerApellido")
				.add( Projections.property("personas.segundoApellido"), "segundoApellido")
				);
				
		criteria.addOrder( Order.asc("personas.primerApellido"))
				
		.setResultTransformer(Transformers.aliasToBean(DtoPersonas.class));		
		ArrayList<DtoPersonas> listaProfesionales=(ArrayList)criteria.list();
		
		return listaProfesionales;
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de obtener los profesionales
	 * de la salud activos e inactivos en el sistema con ocupaciones 
	 * de odont&oacute;logo.
	 * @return
	 * @author Carolina G&oacute;mez
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoPersonas> obtenerMedicosOdontologos (int codigoInstitucion){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Medicos.class, "medicos")
		.createAlias("medicos.personas", "personas")
		.createAlias("ocupacionesMedicas", "ocupacion")
		.createAlias("personas.usuarioses", "usuarios");
		
		int codigoOcupacionOdonto= 0;
		
		String ocupacionOdon=ValoresPorDefecto.getOcupacionOdontologo(codigoInstitucion);
		
		if(!UtilidadTexto.isEmpty(ocupacionOdon))
		{
			codigoOcupacionOdonto=Integer.parseInt(ocupacionOdon);
		}
		if (codigoOcupacionOdonto > 0) {
			
			criteria.add(Restrictions.eq("ocupacion.codigo", codigoOcupacionOdonto));

		}
		
		criteria.setProjection(Projections.projectionList()
				.add( Projections.property("usuarios.login"), "login")
				.add( Projections.property("personas.primerNombre"), "primerNombre")
				.add( Projections.property("personas.segundoNombre"), "segundoNombre")
				.add( Projections.property("personas.primerApellido"), "primerApellido")
				.add( Projections.property("personas.segundoApellido"), "segundoApellido")
				);
				
		criteria.addOrder( Order.asc("personas.primerApellido"))
				
		.setResultTransformer(Transformers.aliasToBean(DtoPersonas.class));		
		ArrayList<DtoPersonas> listaProfesionalesOdont=(ArrayList<DtoPersonas>)criteria.list();
		
		return listaProfesionalesOdont;
	}
	
	
	/**
	 * Retorna Los profesionales de la salud con permisos para centros de costo dependiendo del centro
	 * de atención enviado como parámetro
	 * @param consecutivoCentroAtencion
	 * @return List<{@link DtoPersonas}>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoPersonas> obtenerProfesionalesConPermisosCentroCostoPorCentroAtencion(int codigoInstitucion, int consecutivoCentroAtencion)
	{
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Medicos.class, "medicos")
		
		.createAlias("medicos.personas", "per")
		.createAlias("ocupacionesMedicas", "ocupacion")
		.createAlias("per.usuarioses", "usuarios")
		.createAlias("usuarios.centrosCostos", "centroCosto")
		.createAlias("centroCosto.centroAtencion", "centroAtencion");
		
		int codigoOcupacionOdonto= 0;
		
		String ocupacionOdon=ValoresPorDefecto.getOcupacionOdontologo(codigoInstitucion);
		
		if(!UtilidadTexto.isEmpty(ocupacionOdon))
		{
			codigoOcupacionOdonto=Integer.parseInt(ocupacionOdon);
		}
		if (codigoOcupacionOdonto > 0) {
			
			criteria.add(Restrictions.eq("ocupacion.codigo", codigoOcupacionOdonto));

		}
		
		ProjectionList projection = Projections.projectionList();
		criteria.add(Restrictions.eq("centroAtencion.consecutivo", consecutivoCentroAtencion));
		
		projection.add(Projections.property("usuarios.login"),"login");
		projection.add( Projections.property("per.primerNombre")		, "primerNombre");
		projection.add( Projections.property("per.primerApellido")	, "primerApellido");
		projection.add( Projections.property("per.segundoNombre")		, "segundoNombre");
		projection.add( Projections.property("per.segundoApellido")	, "segundoApellido");
		
		criteria.setProjection(projection);
		criteria.addOrder( Order.asc("per.primerApellido") );
		criteria.addOrder( Order.asc("per.segundoApellido") );
		criteria.addOrder( Order.asc("per.primerNombre") );
		criteria.addOrder( Order.asc("per.segundoNombre") );
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoPersonas.class));
		ArrayList<DtoPersonas> listadoUsuarios=
			(ArrayList<DtoPersonas>)criteria.list();
		
		return listadoUsuarios;
	}
	
	/**
	 * Retorna las ocupaciones medicas de un profesional de la salud
	 * @param login Login del profesional de la salud
	 * @return @link DTONivelAutorizacionOcupacionMedica
	 */
	@SuppressWarnings("unchecked")
	public DTONivelAutorizacionOcupacionMedica obtenerOcupacionMedicaDeUsuarioProfesional(String login)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Medicos.class, "medicos")
		
		.createAlias("medicos.personas", "per")
		.createAlias("ocupacionesMedicas", "ocupacion")
		.createAlias("per.usuarioses", "usuarios");
		
		ProjectionList projection = Projections.projectionList();
		criteria.add(Restrictions.eq("usuarios.login", login));
		
		projection.add(Projections.property("ocupacion.codigo"),"ocupacionMedicaID");
		
		criteria.setProjection(projection);
		
		criteria.setResultTransformer( Transformers.aliasToBean(DTONivelAutorizacionOcupacionMedica.class));
		DTONivelAutorizacionOcupacionMedica ocupacionMedica=
			(DTONivelAutorizacionOcupacionMedica)criteria.uniqueResult();
		
		return ocupacionMedica;
	}

	
	/**
	 * Retorna especialidades del medico
	 * @param loginMedico
	 * @return ArrayList<String>
	 * @author Camilo Gómez
	 */
	public ArrayList<String> obtenerEspecialidadesMedico(String loginMedico)
	{

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Medicos.class, "medico");
		criteria.createAlias("medico.personas","persona");
		criteria.createAlias("persona.usuarioses","usuario");
		criteria.createAlias("medico.especialidadesMedicoses","especialidadesMedico");
		criteria.createAlias("especialidadesMedico.especialidades","especialidad");
		
		criteria.add(Restrictions.eq("usuario.login",loginMedico));
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("especialidad.nombre"),"especialidad");
		
		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(DtoOrdenesAmbulatorias.class));
		ArrayList<DtoOrdenesAmbulatorias> listaEspecialidades = (ArrayList<DtoOrdenesAmbulatorias>)criteria.list();
	
		ArrayList<String> lista=new ArrayList<String>();
			for(DtoOrdenesAmbulatorias especialidad:listaEspecialidades){
				if(!UtilidadTexto.isEmpty(especialidad.getEspecialidad()))
					lista.add(especialidad.getEspecialidad());
			}
				
		return lista;
	}
	
	/**
	 * Retorna especialidades del medico
	 * @param codigoMedico
	 * @return String
	 * @author Ricardo Ruiz
	 */
	@SuppressWarnings("unchecked")
	public String obtenerEspecialidadesMedicoSeparadoPorComa(int codigoMedico)
	{
		String especialidades="";
		String consulta="SELECT esp.nombre FROM Medicos med " +
							"INNER JOIN med.especialidadesMedicoses espMed " +
							"INNER JOIN espMed.especialidades esp " +
						"WHERE med.codigoMedico=:codigoMedico";
		Query query=sessionFactory.getCurrentSession().createQuery(consulta);
		query.setParameter("codigoMedico", codigoMedico, StandardBasicTypes.INTEGER);
		List<String> listaEspecialidades=(List<String>)query.list();
		if(listaEspecialidades != null && ! listaEspecialidades.isEmpty()){
			int i=0;
			int tam=listaEspecialidades.size();
			for(String especialidad:listaEspecialidades){
				i++;
				especialidades+=especialidad;
				if(i<tam){
					especialidades+=", ";
				}
			}
		}
		return especialidades;
	}
	
}
