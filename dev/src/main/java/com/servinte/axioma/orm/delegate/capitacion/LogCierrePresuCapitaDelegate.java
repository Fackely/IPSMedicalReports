/**
 * 
 */
package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.UtilidadTexto;

import com.princetonsa.dto.capitacion.DtoLogCierrePresuCapita;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.servinte.axioma.orm.LogCierrePresuCapita;
import com.servinte.axioma.orm.LogCierrePresuCapitaHome;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * @author Cristhian Murillo
 */
@SuppressWarnings("unchecked")
public class LogCierrePresuCapitaDelegate extends LogCierrePresuCapitaHome
{

	/**
	 * Lista todos
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<LogCierrePresuCapita> obtenerLogs()
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LogCierrePresuCapita.class, "logCierrePresuCapita");
		ArrayList<LogCierrePresuCapita> listaResultado = new ArrayList<LogCierrePresuCapita>();
		listaResultado = (ArrayList<LogCierrePresuCapita>)criteria.list();
		
		return listaResultado;
	}
	
	
	@Override
	public void attachDirty(LogCierrePresuCapita instance) {
		super.attachDirty(instance);
	}
	
	
	
	/**
	 * Lista todos según los parametros
	 * @param parametros
	 * @return ArrayList<LogCierrePresuCapita>
	 *
	 * @autor Cristhian Murillo
	 */
	public ArrayList<DtoLogCierrePresuCapita> obtenerLogs(DtoProcesoPresupuestoCapitado parametros)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LogCierrePresuCapita.class, "logCierrePresuCapita");
		criteria.createAlias("logCierrePresuCapita.usuarios", "usuarios", Criteria.LEFT_JOIN);
		criteria.createAlias("usuarios.personas", "personas", Criteria.LEFT_JOIN);
		
		
		if(!UtilidadTexto.isEmpty(parametros.getLoginUsuario())){
			criteria.add(Restrictions.eq("usuarios.login", parametros.getLoginUsuario()));
		}
		if(parametros.getFechaInicio() != null && parametros.getFechaFin() != null){
			criteria.add(Restrictions.between("logCierrePresuCapita.fechaGeneracion", parametros.getFechaInicio(), parametros.getFechaFin())); 
		}
		
		if(parametros.getFechaCierre() != null){
			criteria.add(Restrictions.eq("logCierrePresuCapita.fechaCierre", parametros.getFechaCierre())); 
		}
		
		if(!UtilidadTexto.isEmpty(parametros.getEstado())){
			criteria.add(Restrictions.eq("logCierrePresuCapita.estado", parametros.getEstado())); 
		}
		if(!UtilidadTexto.isEmpty(parametros.getTipoProceso())){
			criteria.add(Restrictions.eq("logCierrePresuCapita.estado", parametros.getEstado())); 
		}
		
		ProjectionList projectionList = Projections.projectionList(); 
			// Agrupado por fecha de generación de cierre ----------------------------------------------------------------
			projectionList.add(Projections.property("logCierrePresuCapita.fechaGeneracion")		,"fechaGeneracion");
			projectionList.add(Projections.property("logCierrePresuCapita.fechaCierre")			,"fechaCierre");
			projectionList.add(Projections.property("logCierrePresuCapita.horaGeneracion")		,"horaGeneracion");
			projectionList.add(Projections.property("logCierrePresuCapita.estado")				,"estado");
			projectionList.add(Projections.property("logCierrePresuCapita.observaciones")		,"observaciones");
			projectionList.add(Projections.property("usuarios.login")							,"login");
			projectionList.add(Projections.property("personas.primerNombre")					,"primerNombre");
			projectionList.add(Projections.property("personas.segundoNombre")					,"segundoNombre");
			projectionList.add(Projections.property("personas.primerApellido")					,"primerApellido");
			projectionList.add(Projections.property("personas.segundoApellido")					,"segundoApellido");
			//----
			projectionList.add( Projections.groupProperty("logCierrePresuCapita.fechaGeneracion"));
			projectionList.add( Projections.groupProperty("logCierrePresuCapita.fechaCierre"));
			projectionList.add( Projections.groupProperty("logCierrePresuCapita.horaGeneracion"));
			projectionList.add( Projections.groupProperty("logCierrePresuCapita.estado"));
			projectionList.add( Projections.groupProperty("logCierrePresuCapita.observaciones"));
			projectionList.add( Projections.groupProperty("usuarios.login"));
			projectionList.add( Projections.groupProperty("personas.primerNombre"));
			projectionList.add( Projections.groupProperty("personas.segundoNombre"));
			projectionList.add( Projections.groupProperty("personas.primerApellido"));
			projectionList.add( Projections.groupProperty("personas.segundoApellido"));
			//------------------------------------------------------------------------------------------------------------
			
		if(parametros.getCaseParaBusquedaLog() != null)
		{
			switch (parametros.getCaseParaBusquedaLog()) 
			{
				case 1: // Detallado y agrupado por contrato y convenio
					
					criteria.createAlias("logCierrePresuCapita.convenios", "convenios" );
					criteria.createAlias("logCierrePresuCapita.contratos", "contratos" );
					
					projectionList.add(Projections.property("convenios.codigo")					,"codigoConvenio");
					projectionList.add( Projections.groupProperty("convenios.codigo"));
					
					projectionList.add(Projections.property("convenios.nombre")					,"nombreConvenio");
					projectionList.add( Projections.groupProperty("convenios.nombre"));
					
					projectionList.add(Projections.property("contratos.codigo")					,"codigoContrato");
					projectionList.add( Projections.groupProperty("contratos.codigo"));
					
					projectionList.add(Projections.property("contratos.numeroContrato")			,"numeroContrato");
					projectionList.add( Projections.groupProperty("contratos.numeroContrato"));
					
					projectionList.add(Projections.property("contratos.fechaInicial")			,"fechaInicialContrato");
					projectionList.add( Projections.groupProperty("contratos.fechaInicial"));
					
					projectionList.add(Projections.property("contratos.fechaFinal")				,"fechaFinalContrato");
					projectionList.add( Projections.groupProperty("contratos.fechaFinal"));
					
					
					criteria.add(Restrictions.eq("logCierrePresuCapita.fechaGeneracion", parametros.getDtoLogCierrePresuCapita().getFechaGeneracion()));
					criteria.add(Restrictions.eq("logCierrePresuCapita.fechaCierre", parametros.getDtoLogCierrePresuCapita().getFechaCierre()));
					criteria.add(Restrictions.eq("logCierrePresuCapita.horaGeneracion", parametros.getDtoLogCierrePresuCapita().getHoraGeneracion()));
					criteria.add(Restrictions.eq("logCierrePresuCapita.estado", parametros.getDtoLogCierrePresuCapita().getEstado()));
					if(parametros.getDtoLogCierrePresuCapita().getObservaciones() != null
							&& !parametros.getDtoLogCierrePresuCapita().getObservaciones().isEmpty()){
						criteria.add(Restrictions.eq("logCierrePresuCapita.observaciones", parametros.getDtoLogCierrePresuCapita().getObservaciones()));
					}
					else{
						criteria.add(Restrictions.isNull("logCierrePresuCapita.observaciones"));
					}
					if(parametros.getDtoLogCierrePresuCapita().getLogin() != null
							&& !parametros.getDtoLogCierrePresuCapita().getLogin().isEmpty()){
						criteria.add(Restrictions.eq("usuarios.login", parametros.getDtoLogCierrePresuCapita().getLogin()));
					}
					else{
						criteria.add(Restrictions.isNull("usuarios.login"));
					}
					if(parametros.getDtoLogCierrePresuCapita().getPrimerNombre() != null
							&& !parametros.getDtoLogCierrePresuCapita().getPrimerNombre().isEmpty()){
						criteria.add(Restrictions.eq("personas.primerNombre", parametros.getDtoLogCierrePresuCapita().getPrimerNombre()));
					}
					else{
						criteria.add(Restrictions.isNull("personas.primerNombre"));
					}
					if(parametros.getDtoLogCierrePresuCapita().getPrimerApellido() != null
							&& !parametros.getDtoLogCierrePresuCapita().getPrimerApellido().isEmpty()){
						criteria.add(Restrictions.eq("personas.primerApellido", parametros.getDtoLogCierrePresuCapita().getPrimerApellido()));
					}
					else{
						criteria.add(Restrictions.isNull("personas.primerApellido"));
					}
					if(!UtilidadTexto.isEmpty(parametros.getDtoLogCierrePresuCapita().getSegundoNombre())){
						criteria.add(Restrictions.eq("personas.segundoNombre", parametros.getDtoLogCierrePresuCapita().getSegundoNombre()));
					}
					if(!UtilidadTexto.isEmpty(parametros.getDtoLogCierrePresuCapita().getSegundoApellido())){
						criteria.add(Restrictions.eq("personas.segundoApellido", parametros.getDtoLogCierrePresuCapita().getSegundoApellido()));
					}
					
					break;
		
					
				case 2: // Detallado y agrupado por Insoncistencia
					criteria.createAlias("logCierrePresuCapita.convenios", "convenios" );
					criteria.createAlias("logCierrePresuCapita.contratos", "contratos" );
					
					
					projectionList.add(Projections.property("convenios.codigo")					,"codigoConvenio");
					projectionList.add( Projections.groupProperty("convenios.codigo"));
					
					projectionList.add(Projections.property("convenios.nombre")					,"nombreConvenio");
					projectionList.add( Projections.groupProperty("convenios.nombre"));
					
					projectionList.add(Projections.property("contratos.codigo")					,"codigoContrato");
					projectionList.add( Projections.groupProperty("contratos.codigo"));
					
					projectionList.add(Projections.property("contratos.numeroContrato")			,"numeroContrato");
					projectionList.add( Projections.groupProperty("contratos.numeroContrato"));
					
					projectionList.add(Projections.property("contratos.fechaInicial")			,"fechaInicialContrato");
					projectionList.add( Projections.groupProperty("contratos.fechaInicial"));
					
					projectionList.add(Projections.property("contratos.fechaFinal")				,"fechaFinalContrato");
					projectionList.add( Projections.groupProperty("contratos.fechaFinal"));
					
					criteria.add(Restrictions.eq("logCierrePresuCapita.fechaGeneracion", parametros.getDtoLogCierrePresuCapita().getFechaGeneracion()));
					criteria.add(Restrictions.eq("logCierrePresuCapita.fechaCierre", parametros.getDtoLogCierrePresuCapita().getFechaCierre()));
					criteria.add(Restrictions.eq("logCierrePresuCapita.horaGeneracion", parametros.getDtoLogCierrePresuCapita().getHoraGeneracion()));
					criteria.add(Restrictions.eq("logCierrePresuCapita.estado", parametros.getDtoLogCierrePresuCapita().getEstado()));
					if(parametros.getDtoLogCierrePresuCapita().getObservaciones() != null 
							&& !parametros.getDtoLogCierrePresuCapita().getObservaciones().isEmpty()){
						criteria.add(Restrictions.eq("logCierrePresuCapita.observaciones", parametros.getDtoLogCierrePresuCapita().getObservaciones()));
					}
					else{
						criteria.add(Restrictions.isNull("logCierrePresuCapita.observaciones"));
					}
					if(parametros.getDtoLogCierrePresuCapita().getLogin() != null 
							&& !parametros.getDtoLogCierrePresuCapita().getLogin().isEmpty()){
						criteria.add(Restrictions.eq("usuarios.login", parametros.getDtoLogCierrePresuCapita().getLogin()));
					}
					else{
						criteria.add(Restrictions.isNull("usuarios.login"));
					}
					if(parametros.getDtoLogCierrePresuCapita().getPrimerNombre() != null 
							&& !parametros.getDtoLogCierrePresuCapita().getPrimerNombre().isEmpty()){
						criteria.add(Restrictions.eq("personas.primerNombre", parametros.getDtoLogCierrePresuCapita().getPrimerNombre()));
					}
					else{
						criteria.add(Restrictions.isNull("personas.primerNombre"));
					}
					if(parametros.getDtoLogCierrePresuCapita().getPrimerApellido() != null 
							&& !parametros.getDtoLogCierrePresuCapita().getPrimerApellido().isEmpty()){
						criteria.add(Restrictions.eq("personas.primerApellido", parametros.getDtoLogCierrePresuCapita().getPrimerApellido()));
					}
					else{
						criteria.add(Restrictions.isNull("personas.primerApellido"));
					}
					if(!UtilidadTexto.isEmpty(parametros.getDtoLogCierrePresuCapita().getSegundoNombre())){
						criteria.add(Restrictions.eq("personas.segundoNombre", parametros.getDtoLogCierrePresuCapita().getSegundoNombre()));
					}
					if(!UtilidadTexto.isEmpty(parametros.getDtoLogCierrePresuCapita().getSegundoApellido())){
						criteria.add(Restrictions.eq("personas.segundoApellido", parametros.getDtoLogCierrePresuCapita().getSegundoApellido()));
					}
					
					//-----------------------------
					
					criteria.add(Restrictions.eq("convenios.codigo", parametros.getDtoLogCierrePresuCapita().getCodigoConvenio()));
					criteria.add(Restrictions.eq("contratos.codigo", parametros.getDtoLogCierrePresuCapita().getCodigoContrato()));
					
					projectionList.add(Projections.property("logCierrePresuCapita.serviArti")					,"serviArti");
					projectionList.add( Projections.groupProperty("logCierrePresuCapita.serviArti"));
					
					projectionList.add(Projections.property("logCierrePresuCapita.codigoPk")					,"codigoPk");
					projectionList.add( Projections.groupProperty("logCierrePresuCapita.codigoPk"));
					
					projectionList.add(Projections.property("logCierrePresuCapita.proceso")						,"proceso");
					projectionList.add( Projections.groupProperty("logCierrePresuCapita.proceso"));
					
					projectionList.add(Projections.property("logCierrePresuCapita.tipoInconsistencia")			,"tipoInconsistencia");
					projectionList.add( Projections.groupProperty("logCierrePresuCapita.tipoInconsistencia"));
					
					projectionList.add(Projections.property("logCierrePresuCapita.descripcion")					,"descripcion");
					projectionList.add( Projections.groupProperty("logCierrePresuCapita.descripcion"));
					
					
					break;
				case 3:
					criteria.createAlias("logCierrePresuCapita.convenios", "convenios" ,Criteria.INNER_JOIN);
					criteria.createAlias("logCierrePresuCapita.contratos", "contratos" ,Criteria.INNER_JOIN);
					
					criteria.add(Restrictions.eq("convenios.codigo", parametros.getConvenio()));
					criteria.add(Restrictions.eq("contratos.codigo", parametros.getContrato()));
					
					break;
				default:
					break;
			}
		}
		criteria.addOrder(Property.forName("logCierrePresuCapita.fechaGeneracion").desc());
		
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoLogCierrePresuCapita.class));
		
		return (ArrayList<DtoLogCierrePresuCapita>)criteria.list();
	}
	

	
	/**
	 * Lista todos según los parametros
	 * @param parametros
	 * @return ArrayList<LogCierrePresuCapita>
	 *
	 * @autor Fabián Becerra
	 */
	public ArrayList<DtoLogCierrePresuCapita> obtenerLogsParaIndicativo(DtoProcesoPresupuestoCapitado parametros)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LogCierrePresuCapita.class, "logCierrePresuCapita");
		criteria.createAlias("logCierrePresuCapita.usuarios", "usuarios", Criteria.LEFT_JOIN);
		criteria.createAlias("usuarios.personas", "personas", Criteria.LEFT_JOIN);
		
		
		if(!UtilidadTexto.isEmpty(parametros.getLoginUsuario())){
			criteria.add(Restrictions.eq("usuarios.login", parametros.getLoginUsuario()));
		}
		if(parametros.getFechaInicio() != null && parametros.getFechaFin() != null){
			criteria.add(Restrictions.between("logCierrePresuCapita.fechaGeneracion", parametros.getFechaInicio(), parametros.getFechaFin()));
		}
		if(!UtilidadTexto.isEmpty(parametros.getEstado())){
			criteria.add(Restrictions.eq("logCierrePresuCapita.estado", parametros.getEstado())); 
		}
		
		ProjectionList projectionList = Projections.projectionList(); 
			// Agrupado por fecha de generación de cierre ----------------------------------------------------------------
			projectionList.add(Projections.property("logCierrePresuCapita.fechaGeneracion")		,"fechaGeneracion");
			projectionList.add(Projections.property("logCierrePresuCapita.fechaCierre")			,"fechaCierre");
			projectionList.add(Projections.property("logCierrePresuCapita.horaGeneracion")		,"horaGeneracion");
			projectionList.add(Projections.property("logCierrePresuCapita.estado")				,"estado");
			projectionList.add(Projections.property("logCierrePresuCapita.observaciones")		,"observaciones");
			projectionList.add(Projections.property("logCierrePresuCapita.noInformacion")		,"noInformacion");
			projectionList.add(Projections.property("logCierrePresuCapita.proceso")				,"tipoProceso");
			
			projectionList.add(Projections.property("usuarios.login")							,"login");
			projectionList.add(Projections.property("personas.primerNombre")					,"primerNombre");
			projectionList.add(Projections.property("personas.segundoNombre")					,"segundoNombre");
			projectionList.add(Projections.property("personas.primerApellido")					,"primerApellido");
			projectionList.add(Projections.property("personas.segundoApellido")					,"segundoApellido");
			
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoLogCierrePresuCapita.class));
		
		return (ArrayList<DtoLogCierrePresuCapita>)criteria.list();
	}
	
	
	/*
	public static void main(String[] args) {
		LogCierrePresuCapitaDelegate d = new LogCierrePresuCapitaDelegate();
		DtoProcesoPresupuestoCapitado parametros = new DtoProcesoPresupuestoCapitado();
		parametros.setFechaInicio(new Date());
		parametros.setFechaFin(new Date());
		//parametros.setCaseParaBusquedaLog(1);
		parametros.setCaseParaBusquedaLog(null);
		
		ArrayList<DtoLogCierrePresuCapita> lista =d.obtenerLogs(parametros);
		Log4JManager.info(lista.size());
		
	}
	*/
	
	/**
	 * Obtiene la fecha del primer día en que se generó cierre si existe si no retorna null
	 * 
	 * @author Ricardo Ruiz
	 * 
	 * @return Date
	 */
	public Date obtenerPrimerDiaCierrePresupuesto()	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LogCierrePresuCapita.class, "logCierre");
		criteria.addOrder(Property.forName("logCierre.fechaCierre").asc());
		
		List<LogCierrePresuCapita> logCierres = (List<LogCierrePresuCapita>)criteria.list();
		if(logCierres == null || logCierres.isEmpty()){
			return null;
		}
		else{
			LogCierrePresuCapita cierre = logCierres.get(0);
			return cierre.getFechaCierre();
		}
	}
	
	

	/**
	 * Lista los logs según la fecha de cierre y que no tengan una inconsitencia del tipo que llega por parámetro
	 * @param fechaCierre
	 * @param tipoInconsistencia
	 * @return ArrayList<LogCierrePresuCapita>
	 *
	 * @autor Ricardo Ruiz
	 */
	public ArrayList<DtoLogCierrePresuCapita> obtenerLogsPorFechaSinTipoInconsistencia(Date fechaCierre, String tipoInconsitencia)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LogCierrePresuCapita.class, "logCierrePresuCapita");
		criteria.createAlias("logCierrePresuCapita.usuarios", "usuarios", Criteria.LEFT_JOIN);
		criteria.createAlias("usuarios.personas", "personas", Criteria.LEFT_JOIN);
		
		
		criteria.add(Restrictions.eq("logCierrePresuCapita.fechaCierre", fechaCierre));
		criteria.add(Restrictions.ne("logCierrePresuCapita.tipoInconsistencia", tipoInconsitencia)); 
		
		ProjectionList projectionList = Projections.projectionList(); 
			// Agrupado por fecha de generación de cierre ----------------------------------------------------------------
			projectionList.add(Projections.property("logCierrePresuCapita.fechaGeneracion")		,"fechaGeneracion");
			projectionList.add(Projections.property("logCierrePresuCapita.fechaCierre")			,"fechaCierre");
			projectionList.add(Projections.property("logCierrePresuCapita.horaGeneracion")		,"horaGeneracion");
			projectionList.add(Projections.property("logCierrePresuCapita.estado")				,"estado");
			projectionList.add(Projections.property("logCierrePresuCapita.observaciones")		,"observaciones");
			projectionList.add(Projections.property("logCierrePresuCapita.noInformacion")		,"noInformacion");
			projectionList.add(Projections.property("logCierrePresuCapita.proceso")				,"tipoProceso");
			
			projectionList.add(Projections.property("usuarios.login")							,"login");
			projectionList.add(Projections.property("personas.primerNombre")					,"primerNombre");
			projectionList.add(Projections.property("personas.segundoNombre")					,"segundoNombre");
			projectionList.add(Projections.property("personas.primerApellido")					,"primerApellido");
			projectionList.add(Projections.property("personas.segundoApellido")					,"segundoApellido");
			
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoLogCierrePresuCapita.class));
		
		return (ArrayList<DtoLogCierrePresuCapita>)criteria.list();
	}
}
