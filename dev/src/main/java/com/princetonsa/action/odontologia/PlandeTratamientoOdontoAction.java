package com.princetonsa.action.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoIngresoPlanTratamiento;
import util.odontologia.InfoPlanTratamiento;
import util.odontologia.InfoProgramaServicioPlan;

import com.mercury.mundo.odontologia.Odontograma;
import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.odontologia.PlandeTratamientoOdontoForm;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoLogPlanTratamiento;
import com.princetonsa.dto.odontologia.DtoLogPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoLogProgServPlant;
import com.princetonsa.dto.odontologia.DtoMotivosAtencion;
import com.princetonsa.dto.odontologia.DtoOdontograma;
import com.princetonsa.dto.odontologia.DtoOtroSi;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosPlanT;
import com.princetonsa.dto.odontologia.DtoServArtIncCitaOdo;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.princetonsa.enu.general.CarpetasArchivos;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.MotivosAtencionOdontologica;
import com.princetonsa.mundo.odontologia.PlanTratamiento;
import com.princetonsa.mundo.odontologia.PresupuestoOdontologico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.presupuesto.PresupuestoFabricaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IIngresosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio;



/**
 * @author Edgar Carvajal
 */
public class PlandeTratamientoOdontoAction extends Action {

	Logger logger = Logger.getLogger(PlandeTratamientoOdontoAction.class);
	IPresupuestoOdontologicoServicio presupuestoOdontologicoServicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
	
	public ActionForward execute(ActionMapping mapping,
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception
		{
		
		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		ActionErrors errores = new ActionErrors();
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n");
		logger.info(" CODIGO INGRESO PACIENTE --------------------------------------------------------->"+paciente.getCodigoIngreso());
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n");
	
		
		
		/*
		ActionForward validaciones= validarPaciente(mapping, request, paciente, usuario);
		
		if(validaciones!=null)
			return validaciones;
		*/
		if (form instanceof PlandeTratamientoOdontoForm) 
		{	
			PlandeTratamientoOdontoForm forma = (PlandeTratamientoOdontoForm)form;	
			
			
			// valida si el paciente tiene plan de tratamiento abierto o cerrado
			validarPaciente(mapping, request, paciente, usuario, forma);
			Log4JManager.info("El paciente tiene ingreso: "+forma.isTieneIngresoAbierto());
			
			if(forma.getEstado().equals("empezar"))
			{
				return accionEmpezar(mapping, usuario, paciente, forma, request, errores);
			}
			
			if(forma.getEstado().equals("historico"))
			{
				DtoLogPlanTratamiento dtoWhere = new DtoLogPlanTratamiento();
				dtoWhere.setPlanTratamiento(forma.getDtoPlanTratamiento().getCodigoPk().doubleValue());
				forma.setArrayLogs(PlanTratamiento.cargarLogs(dtoWhere));
				return mapping.findForward("paginaHistoricos");
			}
			
			if(forma.getEstado().equals("historicoPrograma"))
			{
				accionCargarHistoricoPlanTratamiento(forma);
				return mapping.findForward("paginaHistoricosP");
			}
			
			if(forma.getEstado().equals("imprimirOdontogramaEvolucion"))
			{
			   return accionImprimirOdontogramaEvolucion (mapping, forma, usuario , paciente,request);
			}
			
			if(forma.getEstado().equals("cancelar"))
			{
			   return accionCancelar (mapping, forma, usuario , paciente,request,  errores );
			}
			
			if(forma.getEstado().equals("guardar"))
			{
			   return accionGuardar (mapping, forma, usuario , paciente,request);
			}
			else if (forma.getEstado().equals("cargarServArt"))
			{
				return accionCargarServArt(mapping, forma,usuario );
			}
			else if(forma.getEstado().equals("cargarHitoricosIngresos"))
			{
				return cargarHistoriciosIngreso(mapping, forma, usuario, paciente);
			}
			else if(forma.getEstado().equals("detalleIngresoPlanTratamiento"))
			{
				return accionCargarDetalleIngresoPlan(mapping, forma, usuario, paciente );
			}
		}
		
		
		return null;
	}

	
	
	
	
	/**
	 * Accion Cargar Detalle Plan de Tratamiento
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionCargarDetalleIngresoPlan(ActionMapping mapping,
														PlandeTratamientoOdontoForm forma, UsuarioBasico usuario,
														PersonaBasica paciente) 
	{
		
		forma.setInfoIngresoPlanTratamiento(forma.getListaIngresoPlan().get(forma.getPostArrayIngresoPlan()));
		/*
		 * CARGAR INFO PLAN DE TRATAMIENTO PARA UN INGRESO
		 */
		DtoPlanTratamientoOdo dto = new DtoPlanTratamientoOdo();
		dto.setCodigoPk(forma.getInfoIngresoPlanTratamiento().getDtoPlanTratamiento().getCodigoPk());
		Log4JManager.info("Codigo Plan "+dto.getCodigoPk());
		
		int tmpIngreso= Utilidades.convertirAEntero(forma.getInfoIngresoPlanTratamiento().getDtoIngresoPaciente().getIngreso());
		/*
		 * CARGAR PLAN DE TRATAMIENTO CON RESPECTO AL INGRESO 
		 */
		if(forma.isUtilizaProgramas())
		{	
			forma.setInfoPlanTratamiento(PlanTratamiento.obtenerPlanTratamientoPresupuestoCompleto(dto.getCodigoPk(), tmpIngreso, "", true /*UTILIZA PROGRAMAS*/ , usuario.getCodigoInstitucionInt()));
		}
		else 
		{
			forma.setInfoPlanTratamiento(PlanTratamiento.obtenerPlanTratamientoPresupuestoCompleto(dto.getCodigoPk(), tmpIngreso, "", false /*UTILIZA SERVICIOS*/ , usuario.getCodigoInstitucionInt()));
		}
		forma.setEstado("cargarPlan");
		
		return mapping.findForward("paginaIngresos"); 
	}




	/**
	 * CARGAR HISTORICOS INGRESOS PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward cargarHistoriciosIngreso(ActionMapping mapping,
													PlandeTratamientoOdontoForm forma, UsuarioBasico usuario,
													PersonaBasica paciente) 
	{
		InfoIngresoPlanTratamiento dtoIngresoPlan = new InfoIngresoPlanTratamiento();
		dtoIngresoPlan.getDtoPlanTratamiento().setIngreso(paciente.getCodigoIngreso());
		dtoIngresoPlan.getDtoIngresoPaciente().setCodigoPaciente(paciente.getCodigoPersona()+"");
		forma.setListaIngresoPlan(PlanTratamiento.cargarIngresosPlanTratamiento(dtoIngresoPlan));
		forma.setInfoPlanTratamiento(new InfoPlanTratamiento());
		forma.setEstado("cargarIngresos");
		return mapping.findForward("paginaIngresos"); 
	}




	/**
	 * 
	 * CARGAR HISTORICOS PLAN DE TRATAMIENTO
	 * @param forma
	 */
	private void accionCargarHistoricoPlanTratamiento(
			PlandeTratamientoOdontoForm forma) {
		DtoLogProgServPlant parametrosDtoBusquedaProg = new DtoLogProgServPlant();
		logger.info("			CONSULSTAR HISTORICOS		 ");
		//PILAS SE PUEDE MEJORA ESTA CONSULTA
		
		String pkTmpProgramaServicio= PlanTratamiento.obtenerCodPkLogServPlanT( forma.getTmpCodigoDetallePlanTratamiento().intValue(), forma.getTmpCodigoPrograma().intValue(), forma.getTmpCodigoServicio())+"";
		logger.info("codigo PROGRAMA SERVICIO -->"+pkTmpProgramaServicio);
		parametrosDtoBusquedaProg.setProgServPlant(Utilidades.convertirADouble(pkTmpProgramaServicio));
		parametrosDtoBusquedaProg.setHistoricoProgServ(ConstantesBD.acronimoSi);
		forma.setListaLogProgServPlant(PlanTratamiento.cargarLogProgramas(parametrosDtoBusquedaProg));
	}

	
	
	
	/**
	 * CARGA LA LISTA DE SERVICIOS ARTICULOS 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionCargarServArt(ActionMapping mapping, PlandeTratamientoOdontoForm forma, UsuarioBasico usuario) 
	{
		DtoServArtIncCitaOdo dto = new DtoServArtIncCitaOdo();
		dto.getServicio().setCodigo(forma.getTmpCodigoServicio());
		forma.setListaServArtIncPlanT(PlanTratamiento.cargarServArtIncPlanT(dto, forma.getTmpCodigoDetallePlanTratamiento(), usuario.getCodigoInstitucionInt())); 
		return mapping.findForward("cargarServArt");
	}

	
	/**
	 * 
	 * @author Cavajal
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,	PlandeTratamientoOdontoForm forma, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		logger.info("***************************************************************************************************************");
		logger.info("*************************** MODIFICAR EL PLAN DE TRATAMIENTO ***************************************************");
		logger.info("***************************************************************************************************************");
		validarCancelacion(forma,  paciente, usuario); //NO HAY MAS ESPECIFICACIONES
		
		
		ActionErrors errores = new ActionErrors(); 
		
		
		if(forma.getInfoPlanTratamiento().getEsConsulta().equals(ConstantesBD.acronimoNo))
			
		{
		// Abrir transaccion
		Connection con = UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		
		/*
		 * Boolean que indica si se debe guardar un registro de Otros Si.
		 * El otro si será guardado únicamente en el caso de que algúna 
		 * autorización de Inclusión/Exclusión sea modificada.
		 * Anexo: 879 - 1.4
		 */
		boolean modificaEstadoProgramaOservicio = false;
		
		
		
		//EXCLUSIONES DETALLE
 		for(InfoDetallePlanTramiento dto : forma.getInfoPlanTratamiento().getSeccionHallazgosDetalle())
		{
			for( InfoHallazgoSuperficie dtoSuper:  dto.getDetalleSuperficie() )
			{
				for(InfoProgramaServicioPlan dtoProgramas: dtoSuper.getProgramasOservicios())
				{
					DtoProgramasServiciosPlanT dtoNuevo= new DtoProgramasServiciosPlanT();
					dtoNuevo.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
					
				    if(dtoProgramas.getEstadoAutorizacion().equals(ConstantesBD.acronimoSi))
				     {
				    	 dtoNuevo.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoAutorizado);
				    	 
				    	 if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setEstadoPrograma(ConstantesIntegridadDominio.acronimoExcluido);
					     }
					     	 dtoNuevo.setEstadoServicio(ConstantesIntegridadDominio.acronimoExcluido);
					     	
					     if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setPrograma(new InfoDatosDouble(dtoProgramas.getCodigoPkProgramaServicio().doubleValue(),""));
					     }
					     else
					     {
					    	 dtoNuevo.setServicio(new InfoDatosInt(dtoProgramas.getCodigoPkProgramaServicio().intValue(),""));
					     }
					     
					     dtoNuevo.setDetPlanTratamiento(dtoSuper.getCodigoPkDetalle());
					     dtoNuevo.setPorConfirmado(ConstantesBD.acronimoNo); // nose si Es necesario???
					    
					     
					     if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoNuevo, con))
					     {
					    	 logger.error("no modifica");
							  UtilidadBD.abortarTransaccion(con);
							  UtilidadBD.closeConnection(con);
							  return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
					     }
					     else{
					    	 modificaEstadoProgramaOservicio = true;
					     }
				     
				     }
				     //
				     else  if(dtoProgramas.getEstadoAutorizacion().equals(ConstantesBD.acronimoNo))
				     {
				    	 dtoNuevo.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoNoAautorizado);
				    	 if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setEstadoPrograma(ConstantesIntegridadDominio.acronimoContratado);
					     }
					     	dtoNuevo.setEstadoServicio(ConstantesIntegridadDominio.acronimoContratado);
					     	
					     	
					     if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setPrograma(new InfoDatosDouble(dtoProgramas.getCodigoPkProgramaServicio().doubleValue(),""));
					     }
					     else
					     {
					    	 dtoNuevo.setServicio(new InfoDatosInt(dtoProgramas.getCodigoPkProgramaServicio().intValue(),""));
					     }
					     
					     dtoNuevo.setDetPlanTratamiento(dtoSuper.getCodigoPkDetalle());
					     
					     dtoNuevo.setPorConfirmado(ConstantesBD.acronimoNo);
					     
					     if(!UtilidadTexto.isEmpty( dtoProgramas.getMotivoCancelacion().getCodigo()))
					     {
						     dtoNuevo.getMotivo().setCodigo(Integer.parseInt(dtoProgramas.getMotivoCancelacion().getCodigo()));
						     logger.info(UtilidadLog.obtenerString(dtoNuevo, true));
					    	 if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoNuevo, con))
						     {
						    	 logger.error("no modifica");
								  UtilidadBD.abortarTransaccion(con);
								  UtilidadBD.closeConnection(con);
								  return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
						     }
					    	 else{
					    		 modificaEstadoProgramaOservicio = true;
					    	 }
					     }
					     else
					     {
					    	 	errores.add("", new ActionMessage("error.errorEnBlanco", "Requiere Un Motivo de Cancelación"));
					        	saveErrors(request, errores);
					        	UtilidadBD.abortarTransaccion(con);
								UtilidadBD.closeConnection(con);
					        	return mapping.findForward("paginaPrincipal");
					 		
					     }
					     
				     } 
				}
			}
		}
		
		
		
		
		
		//EXCLUSIONES OTROS
		
		for ( InfoDetallePlanTramiento dto: forma.getInfoPlanTratamiento().getSeccionOtrosHallazgos())
		{
			for (InfoHallazgoSuperficie dtoSuper:  dto.getDetalleSuperficie() )
			{
				for( InfoProgramaServicioPlan dtoProgramas : dtoSuper.getProgramasOservicios())
				{
					DtoProgramasServiciosPlanT dtoNuevo= new DtoProgramasServiciosPlanT();
					dtoNuevo.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
					
				    if(dtoProgramas.getEstadoAutorizacion().equals(ConstantesBD.acronimoSi))
				     {
				    	 dtoNuevo.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoAutorizado);
				    	 
				    	 if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setEstadoPrograma(ConstantesIntegridadDominio.acronimoExcluido);
					     }
					     	 dtoNuevo.setEstadoServicio(ConstantesIntegridadDominio.acronimoExcluido);
					     	
					     if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setPrograma(new InfoDatosDouble(dtoProgramas.getCodigoPkProgramaServicio().doubleValue(),""));
					     }
					     else
					     {
					    	 dtoNuevo.setServicio(new InfoDatosInt(dtoProgramas.getCodigoPkProgramaServicio().intValue(),""));
					     }
					     //dtoNuevo.setMotivo(programa.)--guardar motivo en el dto
					     dtoNuevo.setDetPlanTratamiento(dtoSuper.getCodigoPkDetalle());
					     dtoNuevo.setPorConfirmado(ConstantesBD.acronimoNo); // nose si Es necesario???
					    
					     logger.info(UtilidadLog.obtenerString(dtoNuevo, true));
					     
					     if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoNuevo, con))
					     {
					    	 logger.error("no modifica");
							  UtilidadBD.abortarTransaccion(con);
							  UtilidadBD.closeConnection(con);
							  return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
					     }
					     else{
					    	 modificaEstadoProgramaOservicio = true;
					     }
				     
				     }
				     
				     else  if(dtoProgramas.getEstadoAutorizacion().equals(ConstantesBD.acronimoNo))
				     {
				    	 dtoNuevo.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoNoAautorizado);
				    	 if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setEstadoPrograma(ConstantesIntegridadDominio.acronimoContratado);
					     }
					     	dtoNuevo.setEstadoServicio(ConstantesIntegridadDominio.acronimoContratado);
					     	
					     if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setPrograma(new InfoDatosDouble(dtoProgramas.getCodigoPkProgramaServicio().doubleValue(),""));
					     }
					     else
					     {
					    	 dtoNuevo.setServicio(new InfoDatosInt(dtoProgramas.getCodigoPkProgramaServicio().intValue(),""));
					     }
					     
					     dtoNuevo.setDetPlanTratamiento(dtoSuper.getCodigoPkDetalle());
					     dtoNuevo.setPorConfirmado(ConstantesBD.acronimoNo);
					     if(!UtilidadTexto.isEmpty(dtoProgramas.getMotivoCancelacion().getCodigo()))
					     {
						     dtoNuevo.getMotivo().setCodigo(Integer.parseInt(dtoProgramas.getMotivoCancelacion().getCodigo()));
						   //  logger.info(UtilidadLog.obtenerString(dtoNuevo, true));
					    	 
						     if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoNuevo, con))
						     {
						    	 logger.error("no modifica");
								  UtilidadBD.abortarTransaccion(con);
								  UtilidadBD.closeConnection(con);
								  return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
						     }
						     else{
						    	 modificaEstadoProgramaOservicio = true;
						     }
					     }
					     else
					     {
					    	 	errores.add("", new ActionMessage("error.errorEnBlanco", "Requiere Un Motivo de Cancelación"));
					        	saveErrors(request, errores);
					        	UtilidadBD.abortarTransaccion(con);
								UtilidadBD.closeConnection(con);
					        	return mapping.findForward("paginaPrincipal");
					     }
				     } 
				}
			}
		}
		
		
		
		
		
		//EXCLUSIONES BOCA
		for ( InfoHallazgoSuperficie dto: forma.getInfoPlanTratamiento().getSeccionHallazgosBoca())
		{
			for(InfoProgramaServicioPlan dtoProgramas:  dto.getProgramasOservicios())
			{
				
				DtoProgramasServiciosPlanT dtoNuevo= new DtoProgramasServiciosPlanT();
				dtoNuevo.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
				
				if(dtoProgramas.getInclusion().equals(ConstantesBD.acronimoSi))
				{
					
					if(dtoProgramas.getEstadoAutorizacion().equals(ConstantesBD.acronimoSi))
				    {
				    	 dtoNuevo.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoAutorizado);
				    	 
				    	 if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setEstadoPrograma(ConstantesIntegridadDominio.acronimoEstadoPendiente);
					     }
					     	 dtoNuevo.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
					     	
					     if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setPrograma(new InfoDatosDouble(dtoProgramas.getCodigoPkProgramaServicio().doubleValue(),""));
					     }
					     else
					     {
					    	 dtoNuevo.setServicio(new InfoDatosInt(dtoProgramas.getCodigoPkProgramaServicio().intValue(),""));
					     }
					     //dtoNuevo.setMotivo(programa.)--guardar motivo en el dto
					     dtoNuevo.setDetPlanTratamiento(dto.getCodigoPkDetalle());
					     dtoNuevo.setPorConfirmado(ConstantesBD.acronimoNo); // nose si Es necesario???
					    
						 logger.info(UtilidadLog.obtenerString(dtoNuevo, true));
					     if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoNuevo, con))
					     {
					    	 logger.error("no modifica");
							  UtilidadBD.abortarTransaccion(con);
							  UtilidadBD.closeConnection(con);
							  return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
					     }
					     else{
					    	 modificaEstadoProgramaOservicio = true;
					     }
					     
				     }
					// CUANDO NO SE AUTORIZA UNA INCLUSION QUEDA COMO ESTADO NO AUTORIZADO
			     
				    else  if(dtoProgramas.getEstadoAutorizacion().equals(ConstantesBD.acronimoNo))
				    {
				    	 dtoNuevo.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoNoAautorizado);
				    	 
				    	 if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setEstadoPrograma(ConstantesIntegridadDominio.acronimoNoAautorizado); //
					     }
					     	dtoNuevo.setEstadoServicio(ConstantesIntegridadDominio.acronimoNoAautorizado);
					     	
					     if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setPrograma(new InfoDatosDouble(dtoProgramas.getCodigoPkProgramaServicio().doubleValue(),""));
					     }
					     else
					     {
					    	 dtoNuevo.setServicio(new InfoDatosInt(dtoProgramas.getCodigoPkProgramaServicio().intValue(),""));
					     }
					     
					     dtoNuevo.setDetPlanTratamiento(dto.getCodigoPkDetalle());
					     
					     dtoNuevo.setPorConfirmado(ConstantesBD.acronimoNo);
					     
					     if(!UtilidadTexto.isEmpty(dtoProgramas.getMotivoCancelacion().getCodigo()))
					     {
					         dtoNuevo.getMotivo().setCodigo(Integer.parseInt(dtoProgramas.getMotivoCancelacion().getCodigo()));
						     logger.info(UtilidadLog.obtenerString(dtoNuevo, true));
					    	 
						     if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoNuevo, con))
						     {
						    	 logger.error("no modifica");
								  UtilidadBD.abortarTransaccion(con);
								  UtilidadBD.closeConnection(con);
								  return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
						     }
						     else{
						    	 modificaEstadoProgramaOservicio = true;
						     }
					     }
					     else
					     {
					    	 	errores.add("", new ActionMessage("error.errorEnBlanco", "Requiere Un Motivo de Cancelación"));
					        	saveErrors(request, errores);
					        	UtilidadBD.abortarTransaccion(con);
								UtilidadBD.closeConnection(con);
					        	return mapping.findForward("paginaPrincipal");
					 		
					     }
				    }
			
				}// fin 
				//PARA EXCLUSION DE PROGRAMAS Y SERVICIOS 
				else
				{
						if(dtoProgramas.getEstadoAutorizacion().equals(ConstantesBD.acronimoSi))
					    {
					    	 dtoNuevo.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoAutorizado);
					    	 
					    	 if (forma.isUtilizaProgramas())
						     {
						    	 dtoNuevo.setEstadoPrograma(ConstantesIntegridadDominio.acronimoExcluido);
						     }
						     	 dtoNuevo.setEstadoServicio(ConstantesIntegridadDominio.acronimoExcluido);
						     	
						     if (forma.isUtilizaProgramas())
						     {
						    	 dtoNuevo.setPrograma(new InfoDatosDouble(dtoProgramas.getCodigoPkProgramaServicio().doubleValue(),""));
						     }
						     else
						     {
						    	 dtoNuevo.setServicio(new InfoDatosInt(dtoProgramas.getCodigoPkProgramaServicio().intValue(),""));
						     }
						     //dtoNuevo.setMotivo(programa.)--guardar motivo en el dto
						     dtoNuevo.setDetPlanTratamiento(dto.getCodigoPkDetalle());
						     dtoNuevo.setPorConfirmado(ConstantesBD.acronimoNo); // nose si Es necesario???
						    
							 
						     if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoNuevo, con))
						     {
						    	 logger.error("no modifica");
								  UtilidadBD.abortarTransaccion(con);
								  UtilidadBD.closeConnection(con);
								  return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
						     }
						     else{
						    	 modificaEstadoProgramaOservicio = true;
						     }
					     }
						//
				     
					    else  if(dtoProgramas.getEstadoAutorizacion().equals(ConstantesBD.acronimoNo))
					    {
					    	 dtoNuevo.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoNoAautorizado);
					    	 if (forma.isUtilizaProgramas())
						     {
						    	 dtoNuevo.setEstadoPrograma(ConstantesIntegridadDominio.acronimoContratado);
						     }
						     	dtoNuevo.setEstadoServicio(ConstantesIntegridadDominio.acronimoContratado);
						     	
						     if (forma.isUtilizaProgramas())
						     {
						    	 dtoNuevo.setPrograma(new InfoDatosDouble(dtoProgramas.getCodigoPkProgramaServicio().doubleValue(),""));
						     }
						     else
						     {
						    	 dtoNuevo.setServicio(new InfoDatosInt(dtoProgramas.getCodigoPkProgramaServicio().intValue(),""));
						     }
						     
						     dtoNuevo.setDetPlanTratamiento(dto.getCodigoPkDetalle());
						     
						     dtoNuevo.setPorConfirmado(ConstantesBD.acronimoNo);
						     
						     if(!UtilidadTexto.isEmpty(dtoProgramas.getMotivoCancelacion().getCodigo()))
						     {
						         dtoNuevo.getMotivo().setCodigo(Integer.parseInt(dtoProgramas.getMotivoCancelacion().getCodigo()));
							     logger.info(UtilidadLog.obtenerString(dtoNuevo, true));
						    	 
							     if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoNuevo, con))
							     {
							    	 logger.error("no modifica");
									  UtilidadBD.abortarTransaccion(con);
									  UtilidadBD.closeConnection(con);
									  return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
							     }
							     else{
							    	 modificaEstadoProgramaOservicio = true;
							     }
						     }
						     else
						     {
						    	 	errores.add("", new ActionMessage("error.errorEnBlanco", "Requiere Un Motivo de Cancelación"));
						        	saveErrors(request, errores);
						        	UtilidadBD.abortarTransaccion(con);
									UtilidadBD.closeConnection(con);
						        	return mapping.findForward("paginaPrincipal");
						     }
					    }
				}
					
			}
				
		}
		
		
		
		
		//  INCLUSIONES
		for(InfoDetallePlanTramiento dto : forma.getInfoPlanTratamiento().getSeccionInclusiones())
		{
			for(InfoHallazgoSuperficie dtoHallazgo: dto.getSuperficiesInclusion())
			{
				for(InfoProgramaServicioPlan dtoProgramas: dtoHallazgo.getProgramasOservicios())
				{   
				     DtoProgramasServiciosPlanT dtoNuevo= new DtoProgramasServiciosPlanT();
				 		dtoNuevo.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
				     
				     if(dtoProgramas.getEstadoAutorizacion().equals(ConstantesBD.acronimoSi))
				     {
				    	 dtoNuevo.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoAutorizado);
				    	 
				    	 if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setEstadoPrograma(ConstantesIntegridadDominio.acronimoEstadoPendiente);
					     }
					     	 dtoNuevo.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
					     	
					     if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setPrograma(new InfoDatosDouble(dtoProgramas.getCodigoPkProgramaServicio().doubleValue(),""));
					     }
					     else
					     {
					    	 dtoNuevo.setServicio(new InfoDatosInt(dtoProgramas.getCodigoPkProgramaServicio().intValue(),""));
					     }
					     //dtoNuevo.setMotivo(programa.)--guardar motivo en el dto
					     dtoNuevo.setDetPlanTratamiento(dtoHallazgo.getCodigoPkDetalle());
					     dtoNuevo.setPorConfirmado(ConstantesBD.acronimoNo);
					    
					     logger.info(UtilidadLog.obtenerString(dtoNuevo, true));
					     
					     if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoNuevo, con))
					     {
					    	 logger.error("no modifica");
							  UtilidadBD.abortarTransaccion(con);
							  UtilidadBD.closeConnection(con);
							  return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
					     }
					     else{
					    	 modificaEstadoProgramaOservicio = true;
					     }
				     
				     }
				     else  if(dtoProgramas.getEstadoAutorizacion().equals(ConstantesBD.acronimoNo))
				     {
				    	 
				    	 dtoNuevo.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoNoAautorizado);
				    	 
				    	 if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setEstadoPrograma(ConstantesIntegridadDominio.acronimoNoAautorizado);
					     }
					     	dtoNuevo.setEstadoServicio(ConstantesIntegridadDominio.acronimoNoAautorizado);
					     	
					     if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setPrograma(new InfoDatosDouble(dtoProgramas.getCodigoPkProgramaServicio().doubleValue(),""));
					     }
					     else
					     {
					    	 dtoNuevo.setServicio(new InfoDatosInt(dtoProgramas.getCodigoPkProgramaServicio().intValue(),""));
					     }
					     dtoNuevo.setDetPlanTratamiento(dtoHallazgo.getCodigoPkDetalle());
					     dtoNuevo.setPorConfirmado(ConstantesBD.acronimoNo);
					    
					     if(!UtilidadTexto.isEmpty(dtoProgramas.getMotivoCancelacion().getCodigo()))
					     {
						     dtoNuevo.getMotivo().setCodigo(Integer.parseInt(dtoProgramas.getMotivoCancelacion().getCodigo()));
						     logger.info(UtilidadLog.obtenerString(dtoNuevo, true));
					    	 
						     if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoNuevo, con))
						     {
						    	 logger.error("no modifica");
								  UtilidadBD.abortarTransaccion(con);
								  UtilidadBD.closeConnection(con);
								  return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
						     }
						     else{
						    	 modificaEstadoProgramaOservicio = true;
						     }
					     }
					     else
					     {
					    	 	errores.add("", new ActionMessage("error.errorEnBlanco", "Requiere Un Motivo de Cancelación"));
					        	saveErrors(request, errores);
					        	UtilidadBD.abortarTransaccion(con);
								UtilidadBD.closeConnection(con);
					        	return mapping.findForward("paginaPrincipal");
					 		
					     }
				     } 
				
				}
				
			}
		}
		
		
		
		// GARANTIAS
		for(InfoDetallePlanTramiento dto : forma.getInfoPlanTratamiento().getSeccionGarantias())
		{
			for(InfoHallazgoSuperficie dtoHallazgo: dto.getSuperficiesGarantia())
			{
				for(InfoProgramaServicioPlan dtoProgramas: dtoHallazgo.getProgramasOservicios())
				{   
				    DtoProgramasServiciosPlanT dtoNuevo= new DtoProgramasServiciosPlanT();
				 	dtoNuevo.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
				 	
				     if(dtoProgramas.getEstadoAutorizacion().equals(ConstantesBD.acronimoSi))
				     {
				    	 dtoNuevo.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoAutorizado);
				    	 if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setEstadoPrograma(ConstantesIntegridadDominio.acronimoContratado);
					     }
					     	 dtoNuevo.setEstadoServicio(ConstantesIntegridadDominio.acronimoContratado);
					     	
					     if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setPrograma(new InfoDatosDouble(dtoProgramas.getCodigoPkProgramaServicio().doubleValue(),""));
					     }
					     else
					     {
					    	 dtoNuevo.setServicio(new InfoDatosInt(dtoProgramas.getCodigoPkProgramaServicio().intValue(),""));
					     }
					   //dtoNuevo.setMotivo(programa.)--guardar motivo en el dto
					     dtoNuevo.setDetPlanTratamiento(dtoHallazgo.getCodigoPkDetalle());
					     dtoNuevo.setPorConfirmado(ConstantesBD.acronimoNo);
					     logger.info(UtilidadLog.obtenerString(dtoNuevo, true));
					     if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoNuevo, con))
					     {
					    	 logger.error("no modifica");
							  UtilidadBD.abortarTransaccion(con);
							  UtilidadBD.closeConnection(con);
							  return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
					     }
				     }
				     else  if(dtoProgramas.getEstadoAutorizacion().equals(ConstantesBD.acronimoNo))
				     {
				    	 dtoNuevo.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoNoAautorizado);
				    	 
				    	 if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setEstadoPrograma(ConstantesIntegridadDominio.acronimoNoAautorizado);
					     }
					     	dtoNuevo.setEstadoServicio(ConstantesIntegridadDominio.acronimoNoAautorizado);
					     	
					     if (forma.isUtilizaProgramas())
					     {
					    	 dtoNuevo.setPrograma(new InfoDatosDouble(dtoProgramas.getCodigoPkProgramaServicio().doubleValue(),""));
					     }
					     else
					     {
					    	 dtoNuevo.setServicio(new InfoDatosInt(dtoProgramas.getCodigoPkProgramaServicio().intValue(),""));
					     }
					     dtoNuevo.setDetPlanTratamiento(dtoHallazgo.getCodigoPkDetalle());
					     dtoNuevo.setPorConfirmado(ConstantesBD.acronimoNo);
					     if(!UtilidadTexto.isEmpty(dtoProgramas.getMotivoCancelacion().getCodigo()))
					     {
					     	dtoNuevo.getMotivo().setCodigo(Integer.parseInt(dtoProgramas.getMotivoCancelacion().getCodigo()));
						    logger.info(UtilidadLog.obtenerString(dtoNuevo, true));
					    	 
						     if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoNuevo, con))
						     {
						    	 logger.error("no modifica");
								  UtilidadBD.abortarTransaccion(con);
								  UtilidadBD.closeConnection(con);
								  return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
						     }
					     }
					     else
					     {
				    	 	errores.add("", new ActionMessage("error.errorEnBlanco", "Requiere Un Motivo de Cancelación"));
				        	saveErrors(request, errores);
				        	UtilidadBD.abortarTransaccion(con);
							UtilidadBD.closeConnection(con);
				        	return mapping.findForward("paginaPrincipal");
					 		
					     }
				     } 
				    
				     
					}
					
				}
			}
		
		
			logger.info("realiza todo 100%");
			UtilidadBD.finalizarTransaccion(con);
			UtilidadBD.closeConnection(con);
		
		
			if(errores.isEmpty())
			{
				forma.setEstado("resumen"); //PROCESO EXITOSO
				
				if(modificaEstadoProgramaOservicio){
					// guardarOtrosSi(forma, usuario);
					Log4JManager.info("Ya no se guarda el OtrosSi en este punto. Ahora se guarda en el contratar");
				}
			}
		
		}
		
		this.cargar(paciente, forma, usuario);
		
		//return accionEmpezar(mapping, usuario, paciente, forma, request);
		return mapping.findForward("paginaPrincipal");
		
	}//fin guardar
	
	
	
	
	
	/**
	 * 
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping,	UsuarioBasico usuario, PersonaBasica paciente,	PlandeTratamientoOdontoForm forma,  HttpServletRequest request, ActionErrors errores) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		this.cargarMotivo(mapping, forma, usuario);
	
		
		/*
		 * Cargar información Básica de presupuesto 
		 */
		if(this.cargar(paciente, forma, usuario))
		{
			this.validarPorconfirma(forma);
		}
		else
		{
			errores.add("", new ActionMessage("errors.notEspecific", "No Existe Plan de Tratamiento"));
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresActionErrors");
		}
		
		forma.setEstado("cargar");
		return mapping.findForward("paginaPrincipal");
	}
	

	/**
	 * 
	 * METODO QUE CANCELA EL PLAN DE TRATAMIENTO 
	 * Y EL PRESUPUESTO CON LOS SIGUIENTE ESTADO ACTIVO, CONTRATADO SUSPENDIDO TEMPORTAMENTE
	 * SINO TIENE MOTIVO DE CANCELACION DE PRESUPUESTO ENVIA UN MENSAJE.
	 * 
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @throws SQLException 
	 * @return/*
	 */
	
	private ActionForward accionCancelar(ActionMapping mapping,	PlandeTratamientoOdontoForm forma, UsuarioBasico usuario,
			PersonaBasica paciente, HttpServletRequest request, ActionErrors errores) throws SQLException 
		{
		
		
		String motivoCancelacionPresuesto= ValoresPorDefecto.getMotivoCancelacionPresupuestoSuspendidoTemp(usuario.getCodigoInstitucionInt());
		int tmpMotivoCancelacionPresuesto=Utilidades.convertirAEntero(motivoCancelacionPresuesto);
		
		/*
		 * VALIDACION SI EXITE MOTIVO DE CANCELACION DE PRESPUESTO
		 * 
		 */
		if(tmpMotivoCancelacionPresuesto>0)
		{
			
			// Abrir transaccion
			
			Connection con = UtilidadBD.abrirConexion();
			UtilidadBD.iniciarTransaccion(con);
			
			DtoPlanTratamientoOdo dtoWhere = new DtoPlanTratamientoOdo();
			dtoWhere.setCodigoPk(forma.getDtoPlanTratamiento().getCodigoPk());
			forma.getDtoPlanTratamiento().setEstado(ConstantesIntegridadDominio.acronimoEstadoCancelado);
			
			/*
			 *MODIFICAR PLAN DE TRATAMIENTO 
			 */
			if(!PlanTratamiento.modificar( dtoWhere, forma.getDtoPlanTratamiento(),con))
			{
			
				logger.error("no modifica");
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
			}
		    
			
			
			//CARGAR LOS PLANES DE PRESPUESTO
			ArrayList<DtoPresupuestoOdontologico> arrayPresupuestos = new ArrayList<DtoPresupuestoOdontologico>();
		    DtoPresupuestoOdontologico dtoWhereP = new DtoPresupuestoOdontologico();
		    dtoWhereP.setPlanTratamiento(forma.getDtoPlanTratamiento().getCodigoPk());
			arrayPresupuestos = PresupuestoOdontologico.cargarPresupuesto(dtoWhereP);
			
			//CARGAR MOTIVO DE CANCELACION DEL PRESUPUESTO 
			//OJO SI NO TIENE MOTIVO ???????????
			
			
			
			
			//PROCESO DE MODIFICACION DEL PRESPUESTO
			logger.info("TAMAÑO PRESUPESTOS------------------------>"+arrayPresupuestos.size());
			
			
			for(DtoPresupuestoOdontologico dto: arrayPresupuestos)
			{
				DtoPresupuestoOdontologico dtoLog = (DtoPresupuestoOdontologico)dto.clone();
				dto.setEstado(ConstantesIntegridadDominio.acronimoContratadoCancelado);
				dto.getMotivo().setCodigo(tmpMotivoCancelacionPresuesto);
							
				
				ArrayList<String> arrayEstados = new ArrayList<String>();
				arrayEstados.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
				arrayEstados.add(ConstantesIntegridadDominio.acronimoContratadoContratado);
				arrayEstados.add(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente);
				
				
					if(!PresupuestoOdontologico.modificarPresupuesto( dto, con , arrayEstados))
					{
						logger.error("no guarda log");
						UtilidadBD.abortarTransaccion(con);
						UtilidadBD.closeConnection(con);
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
						
					}
			
					
					
			}
			
			//CERRAR LA TRANSACCION
			logger.info("realiza todo 100%");
			UtilidadBD.finalizarTransaccion(con);
			UtilidadBD.closeConnection(con);
			
		}
		else
		{
			errores.add("", new ActionMessage("errors.notEspecific", " No  Se Puede Cancelar el Plan de Tratamiento. No Existe Motivo de Cancelación de Presupuesto "));
			saveErrors(request, errores);
		}
		
		
		
		

		return this.accionEmpezar(mapping, usuario, paciente, forma, request, errores);
	}

   
	
	/**
	 * 
	 * @param plan
	 * @param usuario
	 * @return
	 */
	public DtoLogPlanTratamiento llenarLogPlan(DtoPlanTratamientoOdo plan  , UsuarioBasico usuario)
	{
		
		DtoLogPlanTratamiento dtoLogPlan = new DtoLogPlanTratamiento();
		dtoLogPlan.setPlanTratamiento(plan.getCodigoPk().doubleValue());
		dtoLogPlan.setEstado(plan.getEstado());
		dtoLogPlan.setModificacion(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		dtoLogPlan.setMotivo(new InfoDatosInt(plan.getMotivo(), ""));
		dtoLogPlan.setPorConfirmar(ConstantesBD.acronimoSi);
		dtoLogPlan.setEstado(plan.getEstado());
		return dtoLogPlan;
		
		
		
		
	}
	
	/**
	 * 
	 * @param presupuesto
	 * @return
	 */
	public DtoLogPresupuestoOdontologico llenarLogPresupuesto(DtoPresupuestoOdontologico presupuesto)
	
	{
		  DtoLogPresupuestoOdontologico dtoLog= new DtoLogPresupuestoOdontologico();
		  	dtoLog.setCodigoPresupuesto(presupuesto.getCodigoPK());
			dtoLog.setEspecialidad(presupuesto.getEspecialidad());
			dtoLog.setUsuarioModifica(presupuesto.getUsuarioModifica());
			dtoLog.setEstado(presupuesto.getEstado());
			dtoLog.setMotivo(presupuesto.getMotivo());
			return dtoLog;
		
	}
	
	
	
	
	
	/**
	 * ACCION IMPRIMIR. 
	 * ESTE METODO ARMA EL REPORTE DEL PLAN DE TRATAMIENTO
	 * @param paciente
	 * @param forma
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionImprimirOdontogramaEvolucion( ActionMapping mapping, PlandeTratamientoOdontoForm forma,	UsuarioBasico usuario, PersonaBasica paciente,
			HttpServletRequest request) 
	{
		
			
			//NOMBRE REPORTE
			String nombreRptDesign = "PlanTratamientoOdontogramaEvolucion.rptdesign";
			
            //Parametros Generacion reporte
	        @SuppressWarnings("unused")
			String parame="";  
	        
	        //DIRECCIONE DE IMAGENES 
	        @SuppressWarnings("unused")
			String directorioImagenes = System.getProperty("directorioImagenes");
		    
	       
		    
		     
		    
		    //Cargar las consultas
	        String codigoTarifarioStr=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
	        int codigoTarifario=ConstantesBD.codigoTarifarioCups;
	        if(!UtilidadTexto.isEmpty(codigoTarifarioStr))
	        {
	        	codigoTarifario=Integer.parseInt(codigoTarifarioStr);
	        }
		    String sqlDetalle=Odontograma.retornarConsultaOdontogramaPlanTratamiento(forma.getDtoLogPlanTratamiento(), ConstantesIntegridadDominio.acronimoDetalle,null  /*TIPOS GARANTIA o INCLUSION*/, codigoTarifario );
		    String sqlOtros=Odontograma.retornarConsultaOdontogramaPlanTratamiento(forma.getDtoPlanTratamiento(), ConstantesIntegridadDominio.acronimoOtro, null /*TIPOS GARANTIA O INCLUSION */, codigoTarifario );
		    String sqlBoca=Odontograma.retornarConsultaOdontogramaPlanTratamiento(forma.getDtoPlanTratamiento(), ConstantesIntegridadDominio.acronimoBoca, null /*TIPOS GARANTIA O INCLUSION */, codigoTarifario);
		    String sqlGarantia=Odontograma.retornarConsultaOdontogramaPlanTratamiento(forma.getDtoPlanTratamiento(), null/* SECCION*/, ConstantesIntegridadDominio.acronimoGarantia, codigoTarifario);
		    String sqlInclusion=Odontograma.retornarConsultaOdontogramaPlanTratamiento(forma.getDtoPlanTratamiento(), null/* SECCION*/, ConstantesIntegridadDominio.acronimoInclusion, codigoTarifario);
		    String sqlInclusionBoca= Odontograma.retornarConsultaOdontogramaPlanTratamiento(forma.getDtoPlanTratamiento(),ConstantesIntegridadDominio.acronimoBoca, ConstantesIntegridadDominio.acronimoInclusion, codigoTarifario);
		    
		    logger(sqlDetalle, sqlOtros, sqlBoca, sqlGarantia, sqlInclusion);
		        
	      
	       		
	         // CARGAR LA INSTITUCION BASICA      
	        InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica"); 
	        DesignEngineApi comp; 
	  
	        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"odontologia/",nombreRptDesign);

	        //CARGAR UBICACION DEL LOG DEPENDIENDO PARAMETRO GENERAL
	        if(ins.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda))
	        	comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
	        else
	        	comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
	        
	        //Crea una grilla en la posicion 0,1 con una fila y 4 columnas
	        comp.insertGridHeaderOfMasterPage(0,1,1,4);
	        comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
		    comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
	        
	        //ARMAR INFORMACION BASICA DEL REPORTE
			Vector v=new Vector();
	        v.add(ins.getRazonSocial());
	        v.add("NIT : " + ins.getNit()+"-"+ins.getDigitoVerificacion());
	        v.add("ODONTOGRAMA DE EVOLUCIÓN Y PLAN DE TRATAMIENTO");
	        
	        
	        comp.insertLabelInGridOfMasterPage(0,1,v);
	        comp.insertGridHeaderOfMasterPageWithName(1,0,1,2, "datos odontograma2");
	        Vector v4=new Vector();
	        v4.add("PACIENTE:"+paciente.getNombrePersona());        
	        v4.add("CENTRO DE ATENCIÓN:" + usuario.getCentroAtencion());
	        
	        comp.insertLabelInGridOfMasterPage(1,0,v4);
	        comp.insertGridHeaderOfMasterPageWithName(1,1,1,2, "datos odontograma3");
	        Vector v5=new Vector();
	        v5.add("IDENTIFICACIÓN:"+paciente.getTipoIdentificacionPersona(false) + "  -  " +paciente.getNumeroIdentificacionPersona());        
	        comp.insertLabelInGridOfMasterPage(1,1,v5);
	        comp.insertGridHeaderOfMasterPageWithName(1,2,1,2, "datos odontograma4");
	        Vector v6=new Vector();
	        v6.add("EDAD:"+paciente.getEdad() + "  años");        
	        v6.add("SEXO:" + paciente.getSexo());
	        comp.insertLabelInGridOfMasterPage(1,2,v6);        
	        
	        
	        	        	       
	        //Cargar presupuesto
	        DtoPresupuestoOdontologico dtoPresupuesto = new DtoPresupuestoOdontologico();
	        dtoPresupuesto = PresupuestoOdontologico.cargarPresupuestoContratado(new BigDecimal(paciente.getCodigoIngreso()));
	        if ( dtoPresupuesto != null)
	        {	
		        comp.insertGridHeaderOfMasterPageWithName(2,0,1,1, "datos p1");
		        if(ValoresPorDefecto.getValidaPresupuestoOdoContratado(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
		        {	
		            Vector vp=new Vector();
			        vp.add("DATOS PRESUPUESTO:   "+"\n"+ "Contratado : SI" );
			        comp.insertLabelInGridOfMasterPage(2,0,vp);
			        //*******************************
			        comp.insertGridHeaderOfMasterPageWithName(2,1,1,1, "datos p2");
			        Vector vp2=new Vector();
			        vp2.add("Número de Presupuesto:"+ dtoPresupuesto.getConsecutivo());        
			        comp.insertLabelInGridOfMasterPage(2,1,vp2);
			        comp.insertGridHeaderOfMasterPageWithName(2,2,1,1, "datos p3");
			        Vector vp3=new Vector();
			        vp3.add("Fecha de Contrato:"+ dtoPresupuesto.getFechaUsuarioGenera().getFechaModifica());        
			        comp.insertLabelInGridOfMasterPage(2,2,vp3);
		        
		        //*********************************
		        }
	        }
	        
	        // Cargar Plan de Tratamiento
	        DtoPlanTratamientoOdo dtoPlan = new DtoPlanTratamientoOdo();
	        ArrayList<String> estados = new ArrayList<String>();
	        estados.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
	        
	        // no aplica 
	        //dtoPlan.setCodigoPk(PlanTratamiento.obtenerUltimoCodigoPlanTratamiento(paciente.getCodigoIngreso(),  estados, "" /*porConfirmar*/)	);
	        dtoPlan.setCodigoPk(PlanTratamiento.obtenerUltimoCodigoPlanTratamiento(paciente.getCodigoPersona()));
	        dtoPlan = forma.getDtoPlanTratamiento();
	        
	        
	        comp.insertGridHeaderOfMasterPageWithName(3,0,1,1, "datos t1");
	        Vector vt1=new Vector();
	        
	        vt1.add("DATOS PLAN DE TRATAMIENTO:"+ "\n"+ "  estado :"+ValoresPorDefecto.getIntegridadDominio(dtoPlan.getEstado()));        
	        //*************
	        comp.insertLabelInGridOfMasterPage(3,0,vt1);
	        comp.insertGridHeaderOfMasterPageWithName(3,1,1,1, "datos t2");
	        Vector vt2=new Vector();
	        vt2.add("Fecha Grabación: "+ UtilidadFecha.conversionFormatoFechaAAp(dtoPlan.getFechaGrabacion()));        
	        comp.insertLabelInGridOfMasterPage(3,1,vt2);
	        comp.insertGridHeaderOfMasterPageWithName(3,2,1,1, "datos t3");
	        Vector vt3=new Vector();
	        vt3.add("Fecha Ultima Evolución: "+  UtilidadFecha.conversionFormatoFechaAAp(dtoPlan.getUsuarioModifica().getFechaModifica()));        
	        comp.insertLabelInGridOfMasterPage(3,2,vt3);
	        comp.insertLabelInGridPpalOfHeader(4,0, "ODONTOGRAMA DE EVOLUCIÓN ");
	       
	        
	        
	        /*
	         * ODONTOGRAMA DE DIAGNOSTICO
	         */
	        DtoOdontograma dtoOdontograma = new DtoOdontograma();
	        logger.info(" Odontograma de Diagnostico="+forma.getDtoPlanTratamiento().getOdontogramaDiagnostico());
	        ArrayList<DtoOdontograma> tmpOdontogramaDiagnostico = new ArrayList<DtoOdontograma>();
	        
	         
	        /*
	         * SI E PLAN DE TRATAMIENTO ESTA ACTIVO CARGAR EL ODONTOGRAMA DE DIAGNOSTICO 
	         */
	        
	        //CARGA LA IMAGENE DEL ODONTOLOGRAMA DE DIAGNOSTICO
	        if(forma.getDtoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoActivo))
	        {
	        	dtoOdontograma.setCodigoPk(forma.getDtoPlanTratamiento().getOdontogramaDiagnostico().doubleValue());
		        dtoOdontograma.setIndicativo(ConstantesIntegridadDominio.acronimoOdontogramaDiagnostico);
	        	tmpOdontogramaDiagnostico= Odontograma.cargar(dtoOdontograma);
	        	if (tmpOdontogramaDiagnostico.size() > 0)
	 	        {	
	 	        	dtoOdontograma = tmpOdontogramaDiagnostico.get(0);
	 	        	logger.info("Imagen de Odontolograma -->"+CarpetasArchivos.IMAGENES_ODONTODX.getRutaFisica()+System.getProperty("file.separator")+dtoOdontograma.getImagen());
	 	        	comp.insertImageBodyPage(0, 0 , CarpetasArchivos.IMAGENES_ODONTODX.getRutaFisica()+System.getProperty("file.separator")+dtoOdontograma.getImagen() , "imagenOdon");
	 	        }
		        
	        }
	        // CARGA LA IMAGEN DEL ODONTOGRAMA DE EVOLUCION
	        else
	        {
	        	
	        	//
	        		logger.info("ODONTOGRAMA DE EVOLUCION ");
	        		logger.info("odontograma codigo evolucion->"+forma.getDtoPlanTratamiento().getCodigoEvolucion().doubleValue());
	        		
	        		//dtoOdontograma.setEvolucion(forma.getDtoPlanTratamiento().getCodigoEvolucion().doubleValue());
	        		//dtoOdontograma.setIndicativo(ConstantesIntegridadDominio.acronimoOdontogramaTratamiento);
	        		tmpOdontogramaDiagnostico= Odontograma.cargar(dtoOdontograma, forma.getDtoPlanTratamiento());
	        		if (tmpOdontogramaDiagnostico.size() > 0)
		 	        {
		 	        	dtoOdontograma = tmpOdontogramaDiagnostico.get(0);
		 	        	logger.info("Imagen de Odontolograma -->"+CarpetasArchivos.IMAGENES_ODONTOEVO.getRutaFisica()+System.getProperty("file.separator")+dtoOdontograma.getImagen());
		 	        	comp.insertImageBodyPage(0, 0 , CarpetasArchivos.IMAGENES_ODONTOEVO.getRutaFisica()+System.getProperty("file.separator")+dtoOdontograma.getImagen() , "imagenOdon");
		 	        }
	        }
	        
	        
	        
	        
	        
	        //SETTEAR LAS CONSULTAS EN EL REPORTE
	        comp.obtenerComponentesDataSet("consultaDetalle") ;    
	        comp.modificarQueryDataSet(sqlDetalle);
	      	comp.obtenerComponentesDataSet("consultaOtros") ;    
            comp.modificarQueryDataSet(sqlOtros);
	      	comp.obtenerComponentesDataSet("consultaBoca") ;    
            comp.modificarQueryDataSet(sqlBoca);
	      	comp.obtenerComponentesDataSet("consultaGarantias") ;    
            comp.modificarQueryDataSet(sqlGarantia);
	        comp.obtenerComponentesDataSet("consultaInclusiones") ;    
            comp.modificarQueryDataSet(sqlInclusion);
            comp.obtenerComponentesDataSet("consultaInclusionesBoca") ; 
            comp.modificarQueryDataSet(sqlInclusionBoca);
            
            //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
	      	
	        comp.lowerAliasDataSet();
			String newPathReport = comp.saveReport1(false);
	        comp.updateJDBCParameters(newPathReport);
	        
	        if(!newPathReport.equals(""))
	        {
	        	request.setAttribute("isOpenReport", "true");
	        	request.setAttribute("newPathReport", newPathReport);
	        }
	             
	        forma.setEstado("empezar");
			return mapping.findForward("paginaPrincipal");
			
	}

	/**
	 * 
	 * @param sqlDetalle
	 * @param sqlOtros
	 * @param sqlBoca
	 * @param sqlGarantia
	 * @param sqlInclusion
	 */
	private void logger(String sqlDetalle, String sqlOtros, String sqlBoca,
			String sqlGarantia, String sqlInclusion) {
		logger.info("****************************************************************************************************************");
		logger.info("****************************************************************************************************************");
		logger.info("****************************************************************************************************************");
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n");
		logger.info("IMPRESION SQL DETALLE "+sqlDetalle);
		logger.info("****************************************************************************************************************");
		logger.info("****************************************************************************************************************");
		logger.info("\n\n");
		logger.info("IMPRESION SQL Boca "+sqlBoca);
		logger.info("****************************************************************************************************************");
		logger.info("****************************************************************************************************************");
		logger.info("\n\n");
		logger.info("IMPRESION SQL Garantia "+sqlGarantia);
		logger.info("\n\n");
		logger.info("IMPRESION SQL Inclusion-----> "+sqlInclusion);
		logger.info("\n\n");
		logger.info("IMPRESION SQL Otros "+sqlOtros);
		logger.info("****************************************************************************************************************");
		logger.info("****************************************************************************************************************");
		logger.info("****************************************************************************************************************");
		logger.info("****************************************************************************************************************");
		logger.info("****************************************************************************************************************");
		logger.info("****************************************************************************************************************");
		logger.info("\n\n");
		logger.info(" \n\n\n");
	}
	
	/**
	 * Cargar el plan de tratamiento
	 * 
	 * @param paciente
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private boolean cargar(PersonaBasica paciente, PlandeTratamientoOdontoForm forma, UsuarioBasico usuario){ 
		
		
		DtoPresupuestoOdontologico dtoPresupuesto = new DtoPresupuestoOdontologico();
		//VALORES POR DEFECTO 
		
		//CARGAR EL CODIGO DEL PLAN DE TRAMIENTO 
		DtoPlanTratamientoOdo dtoPlanTratamiento =  new  DtoPlanTratamientoOdo();
		dtoPlanTratamiento.setIngreso(paciente.getCodigoIngreso());
		//dtoPlanTratamiento.setPorConfirmar(ConstantesBD.acronimoNo);
		dtoPlanTratamiento.setInstitucion(usuario.getCodigoInstitucionInt());
		
		if(paciente.getCodigoIngreso()<=0)
		{
			forma.setDtoPlanTratamiento(new DtoPlanTratamientoOdo()); 
			
			IIngresosServicio ingresosServicio = ManejoPacienteServicioFabrica.crearIngresosServicio();
			DtoInfoIngresoTrasladoAbonoPaciente dtoIngreso = ingresosServicio.obtenerUltimoIngresoPaciente(paciente.getCodigoPersona());
			if(dtoIngreso != null){
				paciente.setCodigoIngreso(dtoIngreso.getIdIngreso());
				dtoPlanTratamiento.setIngreso(paciente.getCodigoIngreso());
				forma.setDtoPlanTratamiento(PlanTratamiento.consultarPlanTratamiento(dtoPlanTratamiento)); 
			}
		}
		else
		{
			forma.setDtoPlanTratamiento(PlanTratamiento.consultarPlanTratamiento(dtoPlanTratamiento));
			if(forma.getDtoPlanTratamiento()!=null && forma.getDtoPlanTratamiento().getCodigoPk().intValue()<=0)
			{
				forma.setDtoPlanTratamiento(PlanTratamiento.consultarPlanTratamientoPaciente(paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt()));
			}
		}
		
		//CARGAR DATOS PLAN TRATAMIENTO
		forma.setDtoLogPlanTratamiento((DtoPlanTratamientoOdo)forma.getDtoPlanTratamiento().clone());
		//validar que tenga un el plan de tratamiento por confirmar en NO
		
		forma.setInfoPlanTratamiento(PlanTratamiento.obtenerPlanTratamientoPresupuestoCompleto(forma.getDtoPlanTratamiento().getCodigoPk(), paciente.getCodigoIngreso(), "", true /*UTILIZA PROGRAMAS*/, usuario.getCodigoInstitucionInt() )); 
		
		
		if(forma.getInfoPlanTratamiento()==null)
		{
			//BigDecimal ultimoPlanTratamiento = PlanTratamiento.obtenerUltimoCodigoPlanTratamiento(paciente.getCodigoPersona());
			return false;
		}
		
		
		forma.getInfoPlanTratamiento().setPorConfirmar(forma.getDtoPlanTratamiento().getPorConfirmar());
		forma.getInfoPlanTratamiento().setAcronimoEstadoPlan(forma.getDtoPlanTratamiento().getEstado()); 
		forma.getInfoPlanTratamiento().setEstadoPlan(forma.getDtoPlanTratamiento().getEstado());
		
		//CARGAR ATRIBUTOS DE LA INTERFAZ COMO: ESTADO, NUMERO, FECHA 
		//y CARGAR LA INFORMACION DEL PRESUPUESTO ASOCIADO AL PLAN DE TRATAMIENTO
		
		
		if(forma.getDtoPlanTratamiento().getCodigoPk().doubleValue()>0)
		{
			dtoPresupuesto.setPlanTratamiento(forma.getDtoPlanTratamiento().getCodigoPk());
			ArrayList<String> estado = new ArrayList<String>();
			estado.add(ConstantesIntegridadDominio.acronimoContratadoContratado);
			
			ArrayList<DtoPresupuestoOdontologico>  listaPresupuesto = PresupuestoOdontologico.cargarPresupuesto(dtoPresupuesto, estado);
			if(listaPresupuesto.size()>0)
			{
				dtoPresupuesto= listaPresupuesto.get(0); // SOLO EXISTE UN PRESUPUESTO CON ESTADO CONTRATADO
			}
			else
			{
				logger.info("\n\n\n\n\n");
				logger.info("*******************************************************************************************************************");
				logger.info(" ---------------------------------------NO EXISTE PRESUPUESTO CONTRATADO ------------------------------------------");
				logger.info("-------------------------------------------------------------------------------------------------------------------");
				logger.info("\n\n\n\n\n");
			}
		}
		//	VALIDA PRESUPUESTO EN PARAMETRO GENERAL 
	    forma.setValidaPresupuestoOdoContratado(ValoresPorDefecto.getValidaPresupuestoOdoContratado(usuario.getCodigoInstitucionInt()));
		//LLENAR DATOS DEL PRESUPUESTO 
		llenarDatosPresupuesto(forma, dtoPresupuesto);
		validacionesEstado(forma);		   
		return true;
		
	}

	
	
	
	/**
	 * LLENAR DATOS PRESPUESTO
	 * @param forma
	 * @param dtoPresupuesto
	 */
	private void llenarDatosPresupuesto(PlandeTratamientoOdontoForm forma,
			DtoPresupuestoOdontologico dtoPresupuesto) 
	{
	
		
		if(forma.getValidaPresupuestoOdoContratado().equals(ConstantesBD.acronimoSi))
	    {
			if(dtoPresupuesto.getCodigoPK().doubleValue()>0)
			{
				forma.getInfoPlanTratamiento().setEstadoPresupuesto(ConstantesBD.acronimoSi);
				forma.getInfoPlanTratamiento().setConsecutivoPresupuesto( dtoPresupuesto.getConsecutivo().toString());
				forma.getInfoPlanTratamiento().setFechaContrato(UtilidadFecha.conversionFormatoFechaAAp(dtoPresupuesto.getFechaUsuarioGenera().getFechaModifica()));
			}
			else
			{
				forma.getInfoPlanTratamiento().setEstadoPresupuesto(ConstantesBD.acronimoNo); //
			}
			
			
			//CARGAR LA FECHA DE EVOLUCION
		    forma.getInfoPlanTratamiento().setFechaEvolucion(UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoPlanTratamiento().getUsuarioModifica().getFechaModifica()));
		    //CARGA LA FECHA DE GRABACION
		    forma.getInfoPlanTratamiento().setFechaGrabacion(UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoPlanTratamiento().getFechaGrabacion()));
		    //CODIGO PRESUPUESTO
		    
		    forma.getInfoPlanTratamiento().setNumeroPresupuesto(dtoPresupuesto.getCodigoPK());
			//ESTADOS DEL PLAN DE TRATAMIENTO
		    forma.getInfoPlanTratamiento().setFechaContrato(dtoPresupuesto.getUsuarioModifica().getFechaModifica());
		    forma.setDtoPresupuesto(dtoPresupuesto);
		    forma.setDtoLogPresupuesto((DtoPresupuestoOdontologico)dtoPresupuesto.clone());
	    }
	}

	
	
	/**
	 * VALIDA LOS ESTADOS DEL PLAN DE TRATAMIENTO
	 * @param forma
	 */
	private void validacionesEstado(PlandeTratamientoOdontoForm forma) 
	{
	
		if( forma.getDtoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoActivo) )
		{
			// PERMITE RESPUESTA--- DE LAS SOLICITUDES--
	    	forma.getInfoPlanTratamiento().setEsConsulta(ConstantesBD.acronimoNo);
	    }
	    else if(forma.getDtoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoTerminado))
	    {
	    	//	PERMITE RESPUESTA DE AUTORIZACION
	    	forma.getInfoPlanTratamiento().setEsConsulta(ConstantesBD.acronimoNo);
	    }
		
	    else if(forma.getDtoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoEnProceso))
		{
			//PERMINTE EL REGISTRO Y LA CANCELACION
	    	// CANCELACION DEL PLAN DE TRATAMIENTO
	    	forma.getInfoPlanTratamiento().setEsConsulta(ConstantesBD.acronimoNo);
		}
	    
	    else if(forma.getDtoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado))
	    {
	    	forma.getInfoPlanTratamiento().setEsConsulta(ConstantesBD.acronimoSi);
	    }
	    
	    else if(forma.getDtoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoInactivoPlanTratamiento))
	    {
	    	forma.getInfoPlanTratamiento().setEsConsulta(ConstantesBD.acronimoSi);
	    }
	    	
	    else if(forma.getDtoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoSuspendidoTemporalmente))
	    {
	    	forma.getInfoPlanTratamiento().setEsConsulta(ConstantesBD.acronimoSi);
	    }
	}
	
	
	
	
	/**
	 * Metodo para validar el paciente. 
	 */
	@SuppressWarnings("deprecation")
	private ActionForward validarPaciente(ActionMapping mapping,
			HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuario, PlandeTratamientoOdontoForm forma) {
	
		Connection con = UtilidadBD.abrirConexion();
		
		//Persona.obtenerCodigoPersona(numeroId, codigoTipoId)

		forma.setTieneIngresoAbierto(true);
		
		if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
		{
			logger.warn("Paciente no válido (null)");			
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			UtilidadBD.closeConnection(con);
			forma.setTieneIngresoAbierto(false);
			return mapping.findForward("paginaError");
		}/*
		else if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta()) <= 0)
		{
			logger.warn("Cuenta no Activa (null)");			
			request.setAttribute("codigoDescripcionError", "errors.paciente.cuentaNoValida");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaError");
			
		}
		else if (paciente.getCodigoUltimaViaIngreso() != ConstantesBD.codigoViaIngresoConsultaExterna)
		{
			logger.warn("Via de ingreso no es CONSULTA EXTERNA (null)");			
			request.setAttribute("codigoDescripcionError","error.cita.asignacion.viaIngreso");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaError");
		}*/
		
		else if (UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().equals(ConstantesIntegridadDominio.acronimoEstadoCerrado))
		{
			/*
			logger.warn("INGRESO CERRADO (null)");			
			request.setAttribute("codigoDescripcionError","errors.paciente.noIngreso");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaError");
			*/
			forma.setTieneIngresoAbierto(false);
		}
		UtilidadBD.closeConnection(con);
		
		return null;
	}
	
	
	
	/**
	 * CARGA LOS MOIVOS 
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario	}

	 * @return
	 */
	private void cargarMotivo(	ActionMapping mapping, PlandeTratamientoOdontoForm forma,
				UsuarioBasico usuario) 
		{
			forma.setListaMotivos(new ArrayList<DtoMotivosAtencion>());
			
			MotivosAtencionOdontologica motivo = new MotivosAtencionOdontologica();
			ArrayList<Integer> filtroEstados= new ArrayList<Integer>();
			filtroEstados.add(ConstantesBD.CancelarPlandeTratamiento); // 1 PRIMERA
			filtroEstados.add(ConstantesBD.NoAutorizarExclusionProgramasServicio);
			filtroEstados.add(ConstantesBD.NoAutorizarGarantiaProgramasServicio);
			filtroEstados.add(ConstantesBD.NoAutorizarInclusionProgramasServicio);
			forma.setListaMotivos(motivo.consultarMotivoAtencionO(filtroEstados, usuario.getCodigoInstitucionInt()));
			logger.info("Lista motivos Tamaño >"+forma.getListaMotivos().size());
			//forma.getListaMotivos().get(0).getCodigo();
			//	forma.getListaMotivos().get(0).getNombre();
		}
		
	
	
	
		/**
		 * VALIDAR EXISTENCIA DE REGISTRO POR CONFIRMA EN SI
		 * CON EL FIN DE DEFINIR SI ES MODIFICACION O CONSULTA.
		 * @param forma
		 */
		void validarPorconfirma(PlandeTratamientoOdontoForm forma) {
			
		boolean bandera=false;

		
		//EXCLUSIONES DETALLE
		for(InfoDetallePlanTramiento dto : forma.getInfoPlanTratamiento().getSeccionHallazgosDetalle())
		{
			for( InfoHallazgoSuperficie dtoSuper:  dto.getDetalleSuperficie() )
			{
				for(InfoProgramaServicioPlan dtoProgSer: dtoSuper.getProgramasOservicios())
				{
					if( dtoProgSer.getPorConfirmar().equals(ConstantesBD.acronimoSi))
					{
						bandera=true;	
					}
					
				}
			}
		}
		
		//EXCLUSIONES OTROS
		
		for ( InfoDetallePlanTramiento dto: forma.getInfoPlanTratamiento().getSeccionOtrosHallazgos())
		{
			for (InfoHallazgoSuperficie dtoSuper:  dto.getDetalleSuperficie() )
			{
				for( InfoProgramaServicioPlan dtoProgSer : dtoSuper.getProgramasOservicios())
				{
					if(dtoProgSer.getPorConfirmar().equals(ConstantesBD.acronimoSi))
					{
						bandera= true;
					}
				}
			}
		}
		
		
		//EXCLUSIONES BOCA
		for ( InfoHallazgoSuperficie dto: forma.getInfoPlanTratamiento().getSeccionHallazgosBoca())
		{
			for(InfoProgramaServicioPlan dtoProgServ:  dto.getProgramasOservicios())
			{
				if(dtoProgServ.getPorConfirmar().equals(ConstantesBD.acronimoSi))
				{
					bandera=true;
				}
			}
				
		}
		
		
		
			//BUSCAR EN INCLUSIONES
			for(InfoDetallePlanTramiento dto : forma.getInfoPlanTratamiento().getSeccionInclusiones())
			{
				for(InfoHallazgoSuperficie dtoHallazgo: dto.getSuperficiesInclusion())
				{
					for(InfoProgramaServicioPlan dtoProgramas: dtoHallazgo.getProgramasOservicios())
					{
						if(dtoProgramas.getPorConfirmar().equals(ConstantesBD.acronimoSi))
						{
						bandera=true;	
						}
						
					}
				}
			}
			
			//BUSCAR EN GARANTIAS
			
			
			for(InfoDetallePlanTramiento dto : forma.getInfoPlanTratamiento().getSeccionGarantias())
			{
				for(InfoHallazgoSuperficie dtoHallazgo: dto.getSuperficiesGarantia())
				{
					for(InfoProgramaServicioPlan dtoProgramas: dtoHallazgo.getProgramasOservicios())
					{   
						if ( dtoProgramas.getPorConfirmar().equals(ConstantesBD.acronimoSi) )
						{
							bandera=true;
						}
					}
					
				}
			}		
						
		if(bandera)
		{
			forma.getInfoPlanTratamiento().setEsConsulta(ConstantesBD.acronimoSi);
		}
						
	}
			
	 
		
		
		
		
		/**
		 * VALIDAR EXISTENCIA DE REGISTRO POR CONFIRMA EN SI
		 * CON EL FIN DE DEFINIR SI ES MODIFICACION O CONSULTA.
		 * @param forma
		 */
		
		
	private boolean validarCancelacion(PlandeTratamientoOdontoForm forma, PersonaBasica paciente, UsuarioBasico usuario) {
			
		boolean bandera=false;

		/**
		 * OBSERVACIONES AL METODO 
		 * 1. HAY QUE VERIFICAR SI ES POR SERVICIO O PROGRAMA
		 * 2. ES ESTA ENVIANDO UN SERVICIO QUE NO ES.
		 * 3. VERIFICAR LAS CONVERSION Y MIRAR QUE NO EXISTA UNA EXCEPTION
		 */
		
		ArrayList<String> listaCodigoPkProgramaServicioPlanT = new ArrayList<String>();
		int tmpPkProgramaServicio=0;
			
			
			
		logger.info("************************************************************************************");
		
		//EXCLUSIONES DETALLE
		for(InfoDetallePlanTramiento dto : forma.getInfoPlanTratamiento().getSeccionHallazgosDetalle())
		{
			for( InfoHallazgoSuperficie dtoSuper:  dto.getDetalleSuperficie() )
			{
				for(InfoProgramaServicioPlan dtoProgSer: dtoSuper.getProgramasOservicios())
				{
					tmpPkProgramaServicio=PlanTratamiento.obtenerCodPkLogServPlanT( dtoSuper.getCodigoPkDetalle().intValue(), dtoProgSer.getCodigoPkProgramaServicio().intValue() , forma.getTmpCodigoServicio());
					logger.info( "CODIGO PROGRAMAS SERVICIO PLAN ---------"+tmpPkProgramaServicio);
					if(tmpPkProgramaServicio>0)
					{
						listaCodigoPkProgramaServicioPlanT.add(String.valueOf(tmpPkProgramaServicio));
					}
				}
			}
		}
		
		//EXCLUSIONES OTROS
		
		for ( InfoDetallePlanTramiento dto: forma.getInfoPlanTratamiento().getSeccionOtrosHallazgos())
		{
			for (InfoHallazgoSuperficie dtoSuper:  dto.getDetalleSuperficie() )
			{
				for( InfoProgramaServicioPlan dtoProgSer : dtoSuper.getProgramasOservicios())
				{
					tmpPkProgramaServicio=PlanTratamiento.obtenerCodPkLogServPlanT(dtoSuper.getCodigoPkDetalle().intValue(),  dtoProgSer.getCodigoPkProgramaServicio().intValue(), forma.getTmpCodigoServicio());
					logger.info("CODIGO PROGRAMAS SERVICIO PLAN-----"+tmpPkProgramaServicio);
					if(tmpPkProgramaServicio>0)
					{
						listaCodigoPkProgramaServicioPlanT.add(String.valueOf(tmpPkProgramaServicio));
					}
				}
			}
		}
		
		
		//EXCLUSIONES BOCA
		for ( InfoHallazgoSuperficie dto: forma.getInfoPlanTratamiento().getSeccionHallazgosBoca())
		{
			for(InfoProgramaServicioPlan dtoProgServ:  dto.getProgramasOservicios())
			{
				tmpPkProgramaServicio=PlanTratamiento.obtenerCodPkLogServPlanT(dto.getCodigoPkDetalle().intValue(),  dtoProgServ.getCodigoPkProgramaServicio().intValue(), forma.getTmpCodigoServicio());
				logger.info("CODIGO PROGRAMAS SERVICIO PLAN-----"+tmpPkProgramaServicio);
				if(tmpPkProgramaServicio>0)
				{
					listaCodigoPkProgramaServicioPlanT.add(String.valueOf(tmpPkProgramaServicio));
				}
				
			}
				
		}
		
		
		
			//BUSCAR EN INCLUSIONES
			for(InfoDetallePlanTramiento dto : forma.getInfoPlanTratamiento().getSeccionInclusiones())
			{
				for(InfoHallazgoSuperficie dtoHallazgo: dto.getSuperficiesInclusion())
				{
					for(InfoProgramaServicioPlan dtoProgramas: dtoHallazgo.getProgramasOservicios())
					{
						tmpPkProgramaServicio=PlanTratamiento.obtenerCodPkLogServPlanT(dtoHallazgo.getCodigoPkDetalle().intValue(),  dtoProgramas.getCodigoPkProgramaServicio().intValue(), forma.getTmpCodigoServicio());
						logger.info("CODIGO PROGRAMAS SERVICIO PLAN-----"+tmpPkProgramaServicio);
						if(tmpPkProgramaServicio>0)
						{
							listaCodigoPkProgramaServicioPlanT.add(String.valueOf(tmpPkProgramaServicio));
						}
						
					}
				}
			}
			
			//BUSCAR EN GARANTIAS
			
			
			for(InfoDetallePlanTramiento dto : forma.getInfoPlanTratamiento().getSeccionGarantias())
			{
				for(InfoHallazgoSuperficie dtoHallazgo: dto.getSuperficiesGarantia())
				{
					for(InfoProgramaServicioPlan dtoProgramas: dtoHallazgo.getProgramasOservicios())
					{   
						tmpPkProgramaServicio=PlanTratamiento.obtenerCodPkLogServPlanT(dtoHallazgo.getCodigoPkDetalle().intValue(),  dtoProgramas.getCodigoPkProgramaServicio().intValue(), forma.getTmpCodigoServicio());
						logger.info("CODIGO PROGRAMAS SERVICIO PLAN-----"+tmpPkProgramaServicio);
						if(tmpPkProgramaServicio>0)
						{
							listaCodigoPkProgramaServicioPlanT.add(String.valueOf(tmpPkProgramaServicio));
						}
					}
					
				}
			}		
						
	
						
		if(listaCodigoPkProgramaServicioPlanT.size()>0)
		{
			if (PlanTratamiento.validarCargos(listaCodigoPkProgramaServicioPlanT))
			{
			 logger.info(" TODOS LOS CARGOS FACTURADOS  ");
			 logger.info("********************************************************************************************************");
			 logger.info("---------------------------------------	CERRAR ESTADO DE INGRESO---------------------------------------");
			 Connection con = UtilidadBD.abrirConexion();
			 IngresoGeneral.actualizarEstadoIngreso(con,  String.valueOf(paciente.getCodigoIngreso()), ConstantesIntegridadDominio.acronimoEstadoCerrado, usuario.getLoginUsuario());
			 UtilidadBD.cerrarObjetosPersistencia(null, null, con);
			 bandera=true; //TODOS LOS CARGAR FACTURADOS  
			}
			else
			{
			 logger.info(" NO ENCUENTAR CARGOS FACTURADOS");
			 bandera=false; // NO HAY CARGOS FACTURADOS O FALTAN POR FACTURAR
			}
		} 
		 return bandera;
		}
		
		
		
	/**
	 * Método encargado de guardar el Otrosi relacionado al 
	 * presupuesto  cargado en el plan de tratamiento
	 * @param forma
	 * @param usuario
	 */
	@SuppressWarnings("unused")
	private void guardarOtrosSi(PlandeTratamientoOdontoForm forma, UsuarioBasico usuario)
	{
		DtoOtroSi dtoOtroSi;
		dtoOtroSi = new DtoOtroSi();
		
		long presupuesto = ConstantesBD.codigoNuncaValidoLong;
		
		if(forma.getInfoPlanTratamiento().getNumeroPresupuesto() != null){
			presupuesto = forma.getInfoPlanTratamiento().getNumeroPresupuesto().longValue();
		}
		
		dtoOtroSi.setPresupuesto(presupuesto);
		dtoOtroSi.setUsuario(usuario.getLoginUsuario());
		dtoOtroSi.setCentroAtencion(usuario.getCodigoCentroAtencion());
		
		UtilidadTransaccion.getTransaccion().begin();
		
		boolean correcto = presupuestoOdontologicoServicio.guardarOtroSiPresupuesto(dtoOtroSi);
		
		if(!correcto){
			Log4JManager.info("No se guardo el OstroSi");
			UtilidadTransaccion.getTransaccion().rollback();
		}
		else{
			Log4JManager.info("OstroSi guardado exitosamente");
			UtilidadTransaccion.getTransaccion().commit();
		}
	}
	
}