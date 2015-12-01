package com.servinte.axioma.orm.delegate.facturacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.facturacion.ConstantesCamposProcesoRipsEntidadesSub;

import com.princetonsa.dto.facturacion.DtoFiltroConsultaProcesoRipsEntidadesSub;
import com.princetonsa.dto.facturacion.DtoResultadoConsultaLogRipsEntidadesSub;
import com.servinte.axioma.orm.LogRipsEntidadesSub;
import com.servinte.axioma.orm.LogRipsEntidadesSubHome;

public class LogRipsEntidadesSubcontratadasDelegate extends LogRipsEntidadesSubHome{
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * 
	 * @param logRipsEntSub log generado en el proceso
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarLogRipsEntidadesSub(LogRipsEntidadesSub logRipsEntSub){
		boolean save = true;					
		try{
			super.attachDirty(logRipsEntSub);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar/actualizar el registro de " +
					"log de rips entidades subcontratadas: ",e);
		}				
		return save;				
	}

	
	/**
	 * 
	 * Este Método se encarga de consultar los
	 * registros de log del proceso de rips entidades subcontratadas
	 * 
	 * @param dtoFiltroConsultaProcesoRips parámetros para la consulta
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> consultarRegistrosLogRipsEntidadesSub(DtoFiltroConsultaProcesoRipsEntidadesSub dtoFiltroConsultaProcesoRips){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LogRipsEntidadesSub.class, "logRipsEntSub")
			
			.createAlias("logRipsEntSub.entidadesSubcontratadas", "entidadSub")
			.createAlias("logRipsEntSub.usuarios", "usuario")
			.createAlias("usuario.personas", "persona")
			
			;
			
			ProjectionList projection = Projections.projectionList();
			
			//ENTIDAD SUBCONTRATADA
			if(dtoFiltroConsultaProcesoRips.getCodigoPkEntidadSub()!=ConstantesBD.codigoNuncaValido){
				criteria.add(Restrictions.eq("entidadSub.codigoPk",dtoFiltroConsultaProcesoRips.getCodigoPkEntidadSub()));
			}
			
			//FECHA 
			if(!UtilidadTexto.isEmpty(dtoFiltroConsultaProcesoRips.getFechaInicial().toString())&& !UtilidadTexto.isEmpty(dtoFiltroConsultaProcesoRips.getFechaFinal().toString())){
				criteria.add(Restrictions.between("logRipsEntSub.fechaProceso",dtoFiltroConsultaProcesoRips.getFechaInicial(), dtoFiltroConsultaProcesoRips.getFechaFinal()));
			}
				
			//USUARIO
			if(!dtoFiltroConsultaProcesoRips.getLoginUsuario().equals(ConstantesBD.codigoNuncaValido+"")
					&&!UtilidadTexto.isEmpty(dtoFiltroConsultaProcesoRips.getLoginUsuario())){
				criteria.add(Restrictions.eq("usuario.login",dtoFiltroConsultaProcesoRips.getLoginUsuario()));
			}
			
			
			projection.add(Projections.property("logRipsEntSub.codigoPk"),"codigoPkLog");
			
			projection.add(Projections.property("entidadSub.codigoPk"),"codigoPkEntidadSub");
			projection.add(Projections.property("entidadSub.razonSocial"),"razonSocialEntidadSub");
			projection.add(Projections.property("logRipsEntSub.fechaProceso"),"fechaProceso");
			projection.add(Projections.property("logRipsEntSub.horaProceso"),"horaProceso");
			projection.add(Projections.property("usuario.login"),"loginUsuario");
			projection.add(Projections.property("persona.primerNombre"),"primerNombreUsuarioProceso");
			projection.add(Projections.property("persona.segundoNombre"),"segundoNombreUsuarioProceso");
			projection.add(Projections.property("persona.primerApellido"),"primerApellidoUsuarioProceso");
			projection.add(Projections.property("persona.segundoApellido"),"segundoApellidoUsuarioProceso");
			
		
			
			criteria.setProjection(Projections.distinct(projection));
			criteria.addOrder( Order.desc("logRipsEntSub.fechaProceso") );
			criteria.addOrder( Order.desc("logRipsEntSub.horaProceso") );
			
			
			criteria.setResultTransformer( Transformers.aliasToBean(DtoResultadoConsultaLogRipsEntidadesSub.class));
			ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listadoResultadoLog=
				(ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>)criteria.list();
		
		return listadoResultadoLog;
		
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar los
	 * registros de log del proceso de rips entidades subcontratadas
	 * por su codigo pk para ser mostrado en el detalle de las consultas
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> consultarRegistrosLogRipsEntidadesSubPorCodigoPk(long codigoPkLogSeleccionado){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LogRipsEntidadesSub.class, "logRipsEntSub")
			
			.createAlias("logRipsEntSub.entidadesSubcontratadas", "entidadSub")
			.createAlias("logRipsEntSub.usuarios", "usuario")
			.createAlias("usuario.personas", "persona")
			.createAlias("logRipsEntSub.tarifariosOficiales", "tarifarioOficial")
			.createAlias("logRipsEntSub.logRipsEntidadesSubArchivos", "logRipsArchivos")
			.createAlias("logRipsArchivos.logRipsEntSubInconsisArchs", "logRipsInconsisArchivos",Criteria.LEFT_JOIN)
			.createAlias("logRipsArchivos.logRipsEntidadesSubRegistrs", "logRipsRegistros",Criteria.LEFT_JOIN)
			.createAlias("logRipsRegistros.logRipsEntSubInconsisCamps", "logRipsInconsisCampos",Criteria.LEFT_JOIN)
			
			
			;
			
			ProjectionList projection = Projections.projectionList();
			
			criteria.add(Restrictions.eq("logRipsEntSub.codigoPk",codigoPkLogSeleccionado));
			
			projection.add(Projections.property("logRipsEntSub.codigoPk"),"codigoPkLog");
			
			projection.add(Projections.property("entidadSub.codigoPk"),"codigoPkEntidadSub");
			projection.add(Projections.property("entidadSub.razonSocial"),"razonSocialEntidadSub");
			projection.add(Projections.property("logRipsEntSub.fechaProceso"),"fechaProceso");
			projection.add(Projections.property("logRipsEntSub.horaProceso"),"horaProceso");
			projection.add(Projections.property("usuario.login"),"loginUsuario");
			projection.add(Projections.property("persona.primerNombre"),"primerNombreUsuarioProceso");
			projection.add(Projections.property("persona.segundoNombre"),"segundoNombreUsuarioProceso");
			projection.add(Projections.property("persona.primerApellido"),"primerApellidoUsuarioProceso");
			projection.add(Projections.property("persona.segundoApellido"),"segundoApellidoUsuarioProceso");
			projection.add(Projections.property("tarifarioOficial.nombre"),"codificacionServicios");
			projection.add(Projections.property("logRipsEntSub.codificacionMedicainsum"),"acronimoCodificacionMedicaInsu");
			projection.add(Projections.property("logRipsArchivos.nombreArchivo"),"nombreArchivo");
			projection.add(Projections.property("logRipsArchivos.cantidadRegistrosLeidos"),"cantidadRegistrosLeidos");
			projection.add(Projections.property("logRipsArchivos.cantidadRegistrosProcesados"),"cantidadRegistrosProcesados");
			projection.add(Projections.property("logRipsInconsisCampos.codigoPk"),"codigoPkInconsisCamp");
			projection.add(Projections.property("logRipsInconsisArchivos.codigoPk"),"codigoPkInconsisArch");
			projection.add(Projections.property("logRipsArchivos.codigoPk"),"codigoPkArchivo");
			
			
			
			
			criteria.setProjection(Projections.distinct(projection));
			criteria.addOrder( Order.asc("logRipsEntSub.fechaProceso") );
			criteria.addOrder( Order.asc("logRipsEntSub.horaProceso") );
			
			
			criteria.setResultTransformer( Transformers.aliasToBean(DtoResultadoConsultaLogRipsEntidadesSub.class));
			ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listadoResultadoLog=
				(ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>)criteria.list();
		
		return listadoResultadoLog;
		
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar los
	 * registros de log del proceso de rips entidades subcontratadas
	 * por su codigo pk y el codigo pk del archivo seleccionado
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @param codigoPkLogArchivoSeleccionado codigo pk de log rips entidades sub archivo
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> consultarRegistrosLogRipsEntidadesSubPorCodigoPkyArchivo(long codigoPkLogSeleccionado, long codigoPkLogArchivoSeleccionado){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LogRipsEntidadesSub.class, "logRipsEntSub")
			
			.createAlias("logRipsEntSub.entidadesSubcontratadas", "entidadSub")
			.createAlias("logRipsEntSub.usuarios", "usuario")
			.createAlias("usuario.personas", "persona")
			.createAlias("logRipsEntSub.tarifariosOficiales", "tarifarioOficial")
			.createAlias("logRipsEntSub.logRipsEntidadesSubArchivos", "logRipsArchivos")
			.createAlias("logRipsArchivos.logRipsEntSubInconsisArchs", "logRipsInconsisArchivos",Criteria.LEFT_JOIN)
			.createAlias("logRipsArchivos.logRipsEntidadesSubRegistrs", "logRipsRegistros",Criteria.LEFT_JOIN)
			.createAlias("logRipsRegistros.logRipsEntSubInconsisCamps", "logRipsInconsisCampos",Criteria.LEFT_JOIN)
			.createAlias("logRipsRegistros.logRipsEntSubRegValors", "logValoresCampos",Criteria.LEFT_JOIN)
			;
			
			ProjectionList projection = Projections.projectionList();
			
			criteria.add(Restrictions.eq("logRipsEntSub.codigoPk",codigoPkLogSeleccionado));
			criteria.add(Restrictions.eq("logRipsArchivos.codigoPk",codigoPkLogArchivoSeleccionado));
			
			projection.add(Projections.property("logRipsEntSub.codigoPk"),"codigoPkLog");
			projection.add(Projections.property("logRipsRegistros.codigoPk"),"codigoPkLogRegistro");
			projection.add(Projections.property("logRipsInconsisCampos.codigoPk"),"codigoPkInconsisCamp");
			projection.add(Projections.property("logRipsInconsisArchivos.codigoPk"),"codigoPkInconsisArch");
			projection.add(Projections.property("logValoresCampos.codigoPk"),"codigoPkValorCampo");
			
			
			projection.add(Projections.property("logRipsArchivos.nombreArchivo"),"nombreArchivo");
			
			//VALORES POR REGISTRO
			projection.add(Projections.property("logRipsRegistros.numeroFila"),"numeroFila");
			
			//VALORES INCONSISTENCIAS CAMPOS 
			projection.add(Projections.property("logRipsInconsisCampos.nombreCampo"),"nombreCampoInconsistencia");
			projection.add(Projections.property("logRipsInconsisCampos.tipoInconsistencia"),"tipoInconsistenciaCampo");
			
			//VALORES DE LOS CAMPOS POR REGISTRO
			projection.add(Projections.property("logValoresCampos.campoObligatorio"),"campoMostrar");
			projection.add(Projections.property("logValoresCampos.valor"),"valorCampoMostrar");
			
			criteria.setProjection(Projections.distinct(projection));
			
			
			criteria.setResultTransformer( Transformers.aliasToBean(DtoResultadoConsultaLogRipsEntidadesSub.class));
			ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listadoResultadoLog=
				(ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>)criteria.list();
		
		return listadoResultadoLog;
		
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar los
	 * registros log del proceso de rips entidades subcontratadas
	 * 
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @param codigoPkLogArchivoSeleccionado codigo pk de log rips entidades sub archivo
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> consultarRegistrosLogRipsEntidadesSubPorNumerosAutorizacion(long codigoPkLogSeleccionado){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LogRipsEntidadesSub.class, "logRipsEntSub")
			
			.createAlias("logRipsEntSub.entidadesSubcontratadas", "entidadSub")
			.createAlias("logRipsEntSub.usuarios", "usuario")
			.createAlias("usuario.personas", "persona")
			.createAlias("logRipsEntSub.tarifariosOficiales", "tarifarioOficial")
			.createAlias("logRipsEntSub.logRipsEntidadesSubArchivos", "logRipsArchivos")
			.createAlias("logRipsArchivos.logRipsEntSubInconsisArchs", "logRipsInconsisArchivos",Criteria.LEFT_JOIN)
			.createAlias("logRipsArchivos.logRipsEntidadesSubRegistrs", "logRipsRegistros",Criteria.LEFT_JOIN)
			.createAlias("logRipsRegistros.logRipsEntSubInconsisCamps", "logRipsInconsisCampos",Criteria.LEFT_JOIN)
			.createAlias("logRipsRegistros.logRipsEntSubRegValors", "logValoresCampos",Criteria.LEFT_JOIN)
			;
			
			ProjectionList projection = Projections.projectionList();
			
			criteria.add(Restrictions.eq("logRipsEntSub.codigoPk",codigoPkLogSeleccionado));
			
			Disjunction disjunctionArchivos = Restrictions.disjunction();
			disjunctionArchivos.add(Restrictions.eq("logRipsArchivos.nombreArchivo",ConstantesBD.ripsAC));
			disjunctionArchivos.add(Restrictions.eq("logRipsArchivos.nombreArchivo",ConstantesBD.ripsAP));
			disjunctionArchivos.add(Restrictions.eq("logRipsArchivos.nombreArchivo",ConstantesBD.ripsAM));
			disjunctionArchivos.add(Restrictions.eq("logRipsArchivos.nombreArchivo",ConstantesBD.ripsAT));
			//disjunctionArchivos.add(Restrictions.eq("logRipsArchivos.nombreArchivo",ConstantesBD.ripsAU));
			//disjunctionArchivos.add(Restrictions.eq("logRipsArchivos.nombreArchivo",ConstantesBD.ripsAH));
			
			criteria.add(disjunctionArchivos);
			
			Disjunction disjunctionCampos = Restrictions.disjunction();
			disjunctionCampos.add(Restrictions.eq("logValoresCampos.campoObligatorio",ConstantesCamposProcesoRipsEntidadesSub.numeroAutorizacion));
			disjunctionCampos.add(Restrictions.eq("logValoresCampos.campoObligatorio",ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario));
			disjunctionCampos.add(Restrictions.eq("logValoresCampos.campoObligatorio",ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario));
			disjunctionCampos.add(Restrictions.eq("logValoresCampos.campoObligatorio",ConstantesCamposProcesoRipsEntidadesSub.valorArchivosAutorizaciones));
			criteria.add(disjunctionCampos);
			
			projection.add(Projections.property("logRipsEntSub.codigoPk"),"codigoPkLog");
			projection.add(Projections.property("logRipsRegistros.codigoPk"),"codigoPkLogRegistro");
			projection.add(Projections.property("logRipsInconsisCampos.codigoPk"),"codigoPkInconsisCamp");
			projection.add(Projections.property("logRipsInconsisArchivos.codigoPk"),"codigoPkInconsisArch");
			projection.add(Projections.property("logValoresCampos.codigoPk"),"codigoPkValorCampo");
			projection.add(Projections.property("logRipsArchivos.codigoPk"),"codigoPkArchivo");
			
		
			
			//VALORES DE LOS CAMPOS POR REGISTRO
			projection.add(Projections.property("logValoresCampos.campoObligatorio"),"campoMostrar");
			projection.add(Projections.property("logValoresCampos.valor"),"valorCampoMostrar");
			
			criteria.setProjection(Projections.distinct(projection));
			
			
			criteria.setResultTransformer( Transformers.aliasToBean(DtoResultadoConsultaLogRipsEntidadesSub.class));
			ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listadoResultadoLog=
				(ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>)criteria.list();
		
		return listadoResultadoLog;
		
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar un registro
	 * log del proceso de rips entidades subcontratadas
	 * por su codigopk, el codigo del archivo y el del registro
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @param codigoPkArchivo codigo pk de log rips entidades sub de archivo
	 * @param codigoPkRegistro codigo pk de log rips entidades sub de registro
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> consultarRegistrosLogRipsEntidadesSubParaDetallePorNumeroAutorizacion(long codigoPkLogSeleccionado, long codigoPkArchivo, long codigoPkRegistro){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LogRipsEntidadesSub.class, "logRipsEntSub")
			
			.createAlias("logRipsEntSub.entidadesSubcontratadas", "entidadSub")
			.createAlias("logRipsEntSub.usuarios", "usuario")
			.createAlias("usuario.personas", "persona")
			.createAlias("logRipsEntSub.logRipsEntidadesSubArchivos", "logRipsArchivos")
			.createAlias("logRipsArchivos.logRipsEntSubInconsisArchs", "logRipsInconsisArchivos",Criteria.LEFT_JOIN)
			.createAlias("logRipsArchivos.logRipsEntidadesSubRegistrs", "logRipsRegistros",Criteria.LEFT_JOIN)
			.createAlias("logRipsRegistros.logRipsEntSubInconsisCamps", "logRipsInconsisCampos",Criteria.LEFT_JOIN)
			.createAlias("logRipsRegistros.logRipsEntSubRegValors", "logValoresCampos",Criteria.LEFT_JOIN)
			;
			
			ProjectionList projection = Projections.projectionList();
			
			criteria.add(Restrictions.eq("logRipsEntSub.codigoPk",codigoPkLogSeleccionado));
			criteria.add(Restrictions.eq("logRipsArchivos.codigoPk",codigoPkArchivo));
			criteria.add(Restrictions.eq("logRipsRegistros.codigoPk",codigoPkRegistro));
			
			Disjunction disjunctionCampos = Restrictions.disjunction();
			disjunctionCampos.add(Restrictions.eq("logValoresCampos.campoObligatorio",ConstantesCamposProcesoRipsEntidadesSub.numeroAutorizacion));
			disjunctionCampos.add(Restrictions.eq("logValoresCampos.campoObligatorio",ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario));
			disjunctionCampos.add(Restrictions.eq("logValoresCampos.campoObligatorio",ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario));
			disjunctionCampos.add(Restrictions.eq("logValoresCampos.campoObligatorio",ConstantesCamposProcesoRipsEntidadesSub.codigoDeConcepto));
			disjunctionCampos.add(Restrictions.eq("logValoresCampos.campoObligatorio",ConstantesCamposProcesoRipsEntidadesSub.codigoDeConsulta));
			disjunctionCampos.add(Restrictions.eq("logValoresCampos.campoObligatorio",ConstantesCamposProcesoRipsEntidadesSub.codigoDeProcedimiento));
			disjunctionCampos.add(Restrictions.eq("logValoresCampos.campoObligatorio",ConstantesCamposProcesoRipsEntidadesSub.codigoDeMedicamento));
			disjunctionCampos.add(Restrictions.eq("logValoresCampos.campoObligatorio",ConstantesCamposProcesoRipsEntidadesSub.nombreGenericoMedicamento));
			disjunctionCampos.add(Restrictions.eq("logValoresCampos.campoObligatorio",ConstantesCamposProcesoRipsEntidadesSub.codigoDeServicio));
			disjunctionCampos.add(Restrictions.eq("logValoresCampos.campoObligatorio",ConstantesCamposProcesoRipsEntidadesSub.nombreDeServicio));
			criteria.add(disjunctionCampos);
		

			/*Disjunction disjunctionTipoIncons = Restrictions.disjunction();
			disjunctionTipoIncons.add(Restrictions.eq("logRipsInconsisCampos.tipoInconsistencia",ConstantesIntegridadDominio.acronimoAutorizacionNoEncontrada));
			disjunctionTipoIncons.add(Restrictions.eq("logRipsInconsisCampos.tipoInconsistencia",ConstantesIntegridadDominio.acronimoAutorizacionProcesadaConAnterioridad));
			disjunctionTipoIncons.add(Restrictions.eq("logRipsInconsisCampos.tipoInconsistencia",ConstantesIntegridadDominio.acronimoValoresAutorizacionNoValidos));
			criteria.add(disjunctionTipoIncons);*/
			
			projection.add(Projections.property("logRipsEntSub.codigoPk"),"codigoPkLog");
			projection.add(Projections.property("logRipsRegistros.codigoPk"),"codigoPkLogRegistro");
			projection.add(Projections.property("logRipsInconsisCampos.codigoPk"),"codigoPkInconsisCamp");
			projection.add(Projections.property("logRipsInconsisArchivos.codigoPk"),"codigoPkInconsisArch");
			projection.add(Projections.property("logValoresCampos.codigoPk"),"codigoPkValorCampo");
			projection.add(Projections.property("logRipsArchivos.codigoPk"),"codigoPkArchivo");
			
			projection.add(Projections.property("logRipsArchivos.nombreArchivo"),"nombreArchivo");
			
			//VALORES DE LOS CAMPOS POR REGISTRO
			projection.add(Projections.property("logValoresCampos.campoObligatorio"),"campoMostrar");
			projection.add(Projections.property("logValoresCampos.valor"),"valorCampoMostrar");
			
			//INCONSISTENCIAS
			projection.add(Projections.property("logRipsInconsisCampos.nombreCampo"),"nombreCampoInconsistencia");
			projection.add(Projections.property("logRipsInconsisCampos.tipoInconsistencia"),"tipoInconsistenciaCampo");
			
			criteria.setProjection(Projections.distinct(projection));
			
			
			criteria.setResultTransformer( Transformers.aliasToBean(DtoResultadoConsultaLogRipsEntidadesSub.class));
			ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listadoResultadoLog=
				(ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>)criteria.list();
		
		return listadoResultadoLog;
		
	}
	public static void main(String args[]){
		LogRipsEntidadesSubcontratadasDelegate l=new LogRipsEntidadesSubcontratadasDelegate();
		l.consultarRegistrosLogRipsEntidadesSubParaDetallePorNumeroAutorizacion(321,603,562);
	}
}
