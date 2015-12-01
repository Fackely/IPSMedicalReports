package com.princetonsa.action.odontologia;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
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
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.clonacion.UtilidadClonacion;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.odontologia.InfoBonoDcto;
import util.odontologia.InfoConvenioContratoPresupuesto;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoNumSuperficiesPresupuesto;
import util.odontologia.InfoPaquetesPresupuesto;
import util.odontologia.InfoPlanTratamiento;
import util.odontologia.InfoPresupuestoXConvenioProgramaServicio;
import util.odontologia.InfoProgramaServicioPlan;
import util.odontologia.InfoPromocionPresupuestoServPrograma;
import util.odontologia.InfoServicios;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.odontologia.PresupuestoOdontologicoForm;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.odontologia.DtoConcurrenciaPresupuesto;
import com.princetonsa.dto.odontologia.DtoDetallePresupuestoPlanNumSuperficies;
import com.princetonsa.dto.odontologia.DtoDetalleProgramas;
import com.princetonsa.dto.odontologia.DtoDetalleProximaCita;
import com.princetonsa.dto.odontologia.DtoFormatoImpresionContratoOdontologico;
import com.princetonsa.dto.odontologia.DtoHistoricoIngresoPresupuesto;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoLogPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoMotivoDescuento;
import com.princetonsa.dto.odontologia.DtoMotivosAtencion;
import com.princetonsa.dto.odontologia.DtoOtroSi;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoPresupuesto;
import com.princetonsa.dto.odontologia.DtoPresuOdoProgServ;
import com.princetonsa.dto.odontologia.DtoPresupuestoDetalleServiciosProgramaDao;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdoConvenio;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.princetonsa.dto.odontologia.DtoPresupuestoPaquetes;
import com.princetonsa.dto.odontologia.DtoPresupuestoPiezas;
import com.princetonsa.dto.odontologia.DtoPresupuestoPlanTratamientoNumeroSuperficies;
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalConvenio;
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalesContratadoPrecontratado;
import com.princetonsa.dto.odontologia.DtoProcesoCitaProgramada;
import com.princetonsa.dto.odontologia.DtoProgHallazgoPieza;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosPlanT;
import com.princetonsa.dto.odontologia.DtoRecomendaciones;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.dto.odontologia.DtoSuperficiesPorPrograma;
import com.princetonsa.dto.odontologia.DtoValorAnticipoPresupuesto;
import com.princetonsa.dto.odontologia.administracion.DtoBonoDescuento;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.princetonsa.enu.general.CarpetasArchivos;
import com.princetonsa.enu.general.EnumTipoModificacion;
import com.princetonsa.enums.odontologia.ColoresPlanTratamiento;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosOdon;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.odontologia.CitaOdontologica;
import com.princetonsa.mundo.odontologia.DetalleHallazgoProgramaServicio;
import com.princetonsa.mundo.odontologia.MotivosAtencionOdontologica;
import com.princetonsa.mundo.odontologia.MotivosDescuentos;
import com.princetonsa.mundo.odontologia.NumeroSuperficiesPresupuesto;
import com.princetonsa.mundo.odontologia.PlanTratamiento;
import com.princetonsa.mundo.odontologia.PresupuestoContratado;
import com.princetonsa.mundo.odontologia.PresupuestoExclusionesInclusiones;
import com.princetonsa.mundo.odontologia.PresupuestoOdontologico;
import com.princetonsa.mundo.odontologia.PresupuestoPaquetes;
import com.princetonsa.mundo.odontologia.Programa;
import com.princetonsa.mundo.odontologia.ProgramasOdontologicos;
import com.princetonsa.mundo.odontologia.ValidacionesPresupuesto;
import com.princetonsa.mundo.parametrizacion.CentroAtencion;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.helper.odontologia.PresupuestoHelper;
import com.servinte.axioma.mundo.impl.odontologia.contrato.ContratoOdontologicoMundo;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.OdontologiaServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.contrato.ContratoFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.presupuesto.PresupuestoFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.recomendacion.RecomendacionSERVICIOFabrica;
import com.servinte.axioma.servicio.impl.administracion.CentroAtencionServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IEmisionBonosServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IIngresosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.IProximaCitaProgramadaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.contrato.IOtrosSiServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.recomendacion.IRecomendacionesContOdontoServicio;
import com.servinte.axioma.vista.odontologia.presupuesto.PresupuestoHelperVista;
/**
 * 
 * Clase para el manejo del workflow del presupuesto odontologico,
 * @author axioma
 *
 */
@SuppressWarnings("unchecked")
public class PresupuestoOdontologicoAction extends Action 
{
	/**
	 * manejo de logs  
	 */
	private Logger logger = Logger.getLogger(PresupuestoOdontologicoAction.class);

	/**
	 * agrupamientos de valores 
	 */
	public TreeSet<Integer> treeTemp= new TreeSet<Integer>();
	public ArrayList<DtoPresupuestoOdoConvenio> arrayTotales = new ArrayList<DtoPresupuestoOdoConvenio>();
	
	@SuppressWarnings("deprecation")
	@Override
	public ActionForward execute(	ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response)throws Exception 
									{
		Connection con =null;
		try{
			if (form instanceof PresupuestoOdontologicoForm) 
			{
				PresupuestoOdontologicoForm forma = (PresupuestoOdontologicoForm) form;
				logger.info("\n\n************************************************************************");
				logger.info("               PRESUPUESTO ODONTOLOGICO ------>"+forma.getEstado());
				logger.info("************************************************************************\n\n");
				UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				Log4JManager.info(forma.getEstado());
				Log4JManager.info(" Path Action-->"+request.getServletPath());


				// Se valida el ingreso del paciente para saber si se debe desplegar  el resto de la información del presupuesto
				forma.setTieneIngresoAbierto(true);
				con = UtilidadBD.abrirConexion();
				if (UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().equals(ConstantesIntegridadDominio.acronimoEstadoCerrado))
				{
					logger.warn("INGRESO CERRADO (null)");	
					forma.setTieneIngresoAbierto(false);
				}

				UtilidadBD.cerrarConexion(con);

				forma.setEstadoPresupuestoTmp("");

				/*
				 *Validaciones de Ingreso a la funcionalidad 
				 */
				//Si el detalle del presupuesto se llama desde el reporte de pacientes por
				//estado del presupuesto(CU 1172), no debe validarse el paciente
				if(((UtilidadTexto.isEmpty(forma.getEstadoOtraFuncionalidad())) || 
						!(forma.getEstadoOtraFuncionalidad().equals(
								"detallePresupuestoOtraFuncionalidad")))){

					ActionForward validaciones= validacionesAcceso(mapping, request, paciente, usuario, forma, response);

					/*
					 *Flujo de trabajo de la pagina: 
					 */				
					if(validaciones!=null)
						return validaciones;
				}

				if (forma.getEstado().equals("empezar"))
				{
					return accionEmpezar(mapping, forma, usuario, paciente, request, true, response);
				}
				else if (forma.getEstado().equals("empezarSinBloqueo"))
				{
					return accionEmpezar(mapping, forma, usuario, paciente, request, false, response);
				}
				else if (forma.getEstado().equals("ordenar")) 
				{
					return accionOrdenar(mapping, forma, usuario);
				}
				else if(forma.getEstado().equals("consultarHistorico"))
				{
					return accionConsultarHistorico(mapping, forma, usuario);
				}
				else if (forma.getEstado().equals("empezarDetalle")) 
				{
					return accionEmpezarDetalle(mapping, forma, usuario, request);
				}
				else if (forma.getEstado().equals("copiar")) 
				{
					return accionCopiar(mapping, forma, usuario, paciente, request, response);
				}
				else if (forma.getEstado().equals("cargarPrecontratados")) 
				{
					return accionCargarPrecontratados(mapping, forma, usuario,paciente);
				}
				else if (forma.getEstado().equals("contratar")) 
				{
					return accionContratar(mapping, forma, usuario, paciente);
				}
				else if(forma.getEstado().equals("modificarPresupuesto"))
				{
					return accionModificarPresupuesto(mapping, forma, usuario);
				}
				else if(forma.getEstado().equals("empezarModificar"))
				{
					return accionEmpezarModificar(mapping, forma, usuario, paciente, request, response);
				}
				else if(forma.getEstado().equals("adicionarProgramaServicio"))
				{
					return accionAdicionarProgramaServicio(mapping, forma, usuario, paciente);
				}
				else if(forma.getEstado().equals("guardarModificarEstado"))
				{
					return accionGuardarModificarEstado(mapping, forma, usuario,paciente, request, response);
				}
				else if(forma.getEstado().equals("guardarModificarPresupuesto"))
				{
					return accionGuardarModificarPresupuesto(mapping, forma, usuario,paciente , request, response);
				}
				else if(forma.getEstado().equals("guardarPresupuesto"))
				{
					return accionGuardarPresupuesto(mapping, forma, usuario,paciente , request, response, true);
				}
				else if(forma.getEstado().equals("guardarContratado") || forma.getEstado().equals("guardarPrecontratado"))
				{
					//------
					llenarListaOpcionesImprimir(forma, usuario, request); 
					//--------

					if(forma.getDtoPresupuestoContratado().getGenerarNuevaSolicitudDescuento().equals(ConstantesBD.acronimoSi))
					{
						return accionGenerarNuevaSolicitudDescuento(mapping, forma, usuario, paciente, request);
					}

					forma.setPrecontratar(forma.getEstado().equals("guardarPrecontratado"));

					//1. VALIDO SI DESEO PRECONTRATATAR
					if(forma.getDeseoPrecontratar().equals("") && puedoPrecontratar(forma, paciente, usuario))
					{
						forma.setEstado("popupPreguntaPrecontratar");
						return mapping.findForward("contratarPresupuesto");

					}else{

						return validarGuardarPresupuesto (mapping, forma, usuario,paciente , request);
					}

					//return accionGuardarContratadoPrecontratado(mapping, forma, usuario,paciente , request, forma.getEstado().equals("guardarPrecontratado"));
				}

				else if(forma.getEstado().equals("guardarPresupuestoPrecontratadoDirecto")){

					//------
					llenarListaOpcionesImprimir(forma, usuario, request); 
					//--------

					/*
					 * Guardar presupuesto PreContratado
					 */

					forma.setPrecontratar(Boolean.TRUE);

					return validarGuardarPresupuesto (mapping, forma, usuario, paciente, request);

					//return accionPrecontratarDirectamente(mapping, forma, usuario,paciente , request, Boolean.TRUE);

				}

				else if(forma.getEstado().equals("InsertarMotivoSuspension"))
				{
					return accionInsertarMotivoSuspension(mapping, forma, usuario);
				}
				else if(forma.getEstado().equals("InsertarMotivoCancelacion"))
				{
					return accionInsertarMotivoCancelacion(mapping, forma, usuario, paciente.getCodigoPersona());
				}
				else if(forma.getEstado().equals("empezarCancelarPrecontratado"))
				{
					return accionEmpezarCancelarPrecontratado(mapping, forma, usuario);
				}
				else if(forma.getEstado().equals("eliminar"))
				{
					return accionEliminar(mapping, forma, usuario, paciente);
				}
				else if(forma.getEstado().equals("paginaDetalleProgramaServicio"))
				{
					return this.accionCargarDetalleProgramas(mapping, forma, usuario);
				}
				else if(forma.getEstado().equals("actualizarProgramasServiciosPlanTratamiento"))
				{
					logger.info("ACTUALIZAR DETALLE");
					return accionActualizarProgramasServiciosPlanTratamiento(mapping , forma, usuario, request);
				}
				else if(forma.getEstado().equals("cargarRetazoPlanTratamiento"))
				{
					logger.info("Si llega hasta este punto");
					return cargarRetazoPlanTratamiento(forma, paciente, mapping, request);
				}
				else if(forma.getEstado().equals("retazoPlanTratamientoOtros"))
				{
					return this.cargarRetazoPlanTratamientoOtros(forma, paciente, mapping, request);
				}
				else if(forma.getEstado().equals("retazoPlanTratamientoBoca"))
				{
					return this.cargarRetazoPlanTratamientoBoca(forma, paciente, mapping, request);
				}
				else if(forma.getEstado().equals("seleccionarConveniosContratos"))
				{
					return mapping.findForward("seleccionarConveniosContratos");
				}
				else if(forma.getEstado().equals("continuarPresupuesto"))
				{
					return mapping.findForward("paginaPrincipal");
				}
				else if(forma.getEstado().equals("generarPresupuesto"))
				{
					return this.accionCargarPresupuestoConPlanTratamiento(mapping, forma, usuario, paciente);
				}
				else if(forma.getEstado().equals("recalcularPresupuestoGenerado"))
				{
					return this.accionRecalcularPresupuestoGenerado(mapping, forma, usuario, paciente);
				}
				else if(forma.getEstado().equals("mostrarErroresContratar"))
				{
					//return mapping.findForward("paginaPrincipal");
					//forma.setEstado("empezar");
					return mapping.findForward("contratarPresupuesto");

				}
				else if(forma.getEstado().equals("mostrarErroresContratado"))
				{
					return mapping.findForward("contratarPresupuesto");
				}
				else if(forma.getEstado().equals("imprimirReporteDetalle") || forma.getEstado().equals("imprimirReporteContratar"))
				{
					return accionImprimirReporteDetalle(mapping , forma, usuario , request , paciente);
				}
				else if(forma.getEstado().equals("procesoCancelado"))
				{
					return accionProcesoCancelado(paciente, forma, mapping, response, request.getSession().getId());
				}
				else if(forma.getEstado().equals("cargarDetalleYLimpiarPpal"))
				{
					return accionCargarDetalleYLimpiarPpal(mapping, request, forma,	paciente, response, usuario.getCodigoInstitucionInt());
				}
				else if(forma.getEstado().equals("resumenProximaCita"))
				{
					//return accionProximaCita(mapping, forma, usuario);
					// return mapping.findForward(programarProximaCita(forma, paciente, request));

					return mapping.findForward("resumenProximaCita");
				}
				else if(forma.getEstado().equals("guardarProximaCita"))
				{
					//return accionGuardarProximaCita(mapping, forma, paciente, usuario, request);
				}
				else if(forma.getEstado().equals("mostrarErroresProximaCita"))
				{
					return mapping.findForward("paginaProximaCita");
				}
				else if(forma.getEstado().equals("guardarCancelarPresupuesto"))
				{
					return accionGuardarCancelarPresupuesto(mapping, request,forma, usuario, paciente, response);
				}
				else if(forma.getEstado().equals("cargarPaquetes"))
				{
					return accionCargarPaquetes(mapping, forma, usuario, paciente);
				}
				else if(forma.getEstado().equals("cargarDetallePaquete"))
				{
					return accionCargarDetallePaquete(mapping, forma);
				}
				else if(forma.getEstado().equals("actualizarProgramasXPaquetes"))
				{
					return accionActualizarProgramasXPaquetes(mapping, forma, usuario, paciente);
				}
				else if(forma.getEstado().equals("cargarListaHistoricosPresupuesto")){

					this.accionCargarHistoricoIngresoPresupuesto(usuario, paciente ,forma);
					return mapping.findForward("ingresosHistoricosPresupuesto");
				}

				else if(forma.getEstado().equals("cargarHistoricoIngresoPresupuesto"))
				{
					this.accionCargarHistPresupuesto(forma, request, response, mapping, usuario.getCodigoInstitucionInt());
					//return mapping.findForward("presupuestoIngresoHistoricos");
					return mapping.findForward("ingresosHistoricosPresupuesto");

				}
				else if( forma.getEstado().equals("reactivarPresupuesto") )
				{
					return this.accionReactivarPresupuesto(forma, request, response, mapping, usuario,paciente);

				}else if(!UtilidadTexto.isEmpty(forma.getEstadoOtraFuncionalidad()) && 
						forma.getEstadoOtraFuncionalidad().equals("detallePresupuestoOtraFuncionalidad") ){
					return consultarDetallePresupuestoOtraFuncionalidad(mapping, forma, usuario, request);

				}else if (forma.getEstado().equals("volverDesdeProcesoProximaCita")){

					ActionForward forward = null;

					String volverDesdeResumen = (String) request.getSession().getAttribute("volverDesdeResumen");

					if(volverDesdeResumen!=null && UtilidadTexto.getBoolean(volverDesdeResumen)){

						request.getSession().removeAttribute("volverDesdeResumen");

						forma.setEstado("contratadoExitoso");
						forward = mapping.findForward("contratarPresupuesto");

					}else if(request.getSession().getAttribute("codigoProximaCitaRegistrada")!=null && UtilidadTexto.isNumber(request.getSession().getAttribute("codigoProximaCitaRegistrada")+"")){

						int codigoProximaCitaRegistrada = (Integer) request.getSession().getAttribute("codigoProximaCitaRegistrada");
						request.getSession().removeAttribute("codigoProximaCitaRegistrada");

						forma.setCodigoProximaCitaRegistrada(codigoProximaCitaRegistrada);

						forma.setEstado(forma.getEstadoTemporal());

						forward = verificarRegistroProximaCita(mapping, forma, usuario, paciente, request);

						//forma.setRecargaContratarPresupuesto(true);
					}

					if(forward!=null){

						return forward;
					}
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}

	
	
	
	/**
	 * Accion Reactivar Presupuesto
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 */
	private ActionForward  accionReactivarPresupuesto(PresupuestoOdontologicoForm forma,
											HttpServletRequest request, 
											HttpServletResponse response,
											ActionMapping mapping, 
											UsuarioBasico usuairo,
											PersonaBasica paciente) throws IPSException {
		
		
		/*
		 *Cargamos el objeto 
		 */
		forma.setDtoPresupuesto( (DtoPresupuestoOdontologico)forma.getListaPresupuestos().get(forma.getPosArray()).clone());
		
		/*
		 * Seteamos el nuevo estado del presupuesto
		 */
		forma.getDtoPresupuesto().setEstado(ConstantesIntegridadDominio.acronimoContratadoContratado); 
		
		/*
		 * Guardarmos en nuevo estado de presupuesto 
		 */
		return this.accionGuardarModificarEstado(mapping, forma, usuairo, paciente, request, response);
	
	}




	/**
	 * 	Accion cargar Historico presupuesto
	 * Metodo para cargar todos los historicos del presupuesto 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param institucion 
	 */
	private void accionCargarHistPresupuesto(PresupuestoOdontologicoForm forma,
													HttpServletRequest request,
													HttpServletResponse response ,
													ActionMapping mapping, int institucion) {
		/*
		 *1. Cargamos el objeto 
		 */
		DtoHistoricoIngresoPresupuesto dtoTmp = (DtoHistoricoIngresoPresupuesto) UtilidadClonacion.clonar(forma.getListaHistoricoPresuIngreso().get(forma.getPostArrayIngresoHistoricos())) ;
		
		/*
		 *2. Setter DTO Ingreso Historico
		 */
		forma.setDtoIngresoHistoricoPresu(dtoTmp);
		
		/*
		 *3. Cargar Presupuesto  Ingreso Historico  
		 */
		this.cargarPresupuestoIngresoHistorico(forma, request, response, mapping, institucion);
		
		
		
	}
	
	
	
	
	
	/**
	 * Cargar Presupuesto Ingreo Historico
	 * 1. Cargar los Presupuesto asociados al ingreso seleccionado
	 * 1.1 Cargar los programas y servicios del presupuesto
	 * 1.2  Arma la sumatoria de convenios 
	 * 2. Cargar el respectivo plan de tratamiento 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param institucion 
	 */
	public void cargarPresupuestoIngresoHistorico(PresupuestoOdontologicoForm forma , 
												HttpServletRequest request,
												HttpServletResponse response ,
												ActionMapping mapping, int institucion)
	{
		
		// 1.Cargar el Codigo del ingreso del paciente   
		BigDecimal codigoIngresoPaciente = new BigDecimal(forma.getDtoIngresoHistoricoPresu().getNoIngreso())  ;
		int codigoIngreso = Utilidades.convertirAEntero(forma.getDtoIngresoHistoricoPresu().getNoIngreso()); 
		
		
		
		//2. Armar Dto de Busqueda 
		DtoPresupuestoOdontologico dtoWhere = new DtoPresupuestoOdontologico();
		dtoWhere.setIngreso(codigoIngresoPaciente);
		forma.setListaPresupuestos(PresupuestoOdontologico.cargarPresupuesto(dtoWhere));
		
		//3. Si Existe presupuesto cargar los programas o servicios
		for(DtoPresupuestoOdontologico dto: forma.getListaPresupuestos())
		{
			dto.setListaTarifas(PresupuestoOdontologico.cargarTotalesPresupuetoConvenio(dto.getCodigoPK()));
		}
		
		//4. Cargar la sumatoria de convenios
		forma.setListaSumatoriaConvenios(forma.getSumatoria());
		
		PersonaBasica paciente = new PersonaBasica();
		paciente.setCodigoIngreso(codigoIngreso);
		
		/*
		 *5.
		 */
		cargarPlanTratamiento(forma, paciente, request, mapping,   BigDecimal.ZERO /* Codigo presupuesto */, response, request.getSession().getId(), institucion);
	}
	
	
	
	


	/**
	 * Metodo para cargar los ingreso de los pacientes que tengan presupuesto asociado
	 * @author Edgar Carvajal Ruiz
	 * @param usuario
	 * @param paciente
	 * @param forma
	 */
	private void accionCargarHistoricoIngresoPresupuesto(UsuarioBasico usuario,
														PersonaBasica paciente, 
														PresupuestoOdontologicoForm forma) {
		
		/*
		 * 1. cargar las lista de Ingreso
		 */
		List<DtoHistoricoIngresoPresupuesto>  listTMP=PresupuestoOdontologico.listaIngresoHistoricosPresupuesto(
																			paciente.getCodigoPersona(), //codigo Paciente 
																			ConstantesBD.codigoViaIngresoConsultaExterna,  //consulta Externa
																			usuario.getCodigoInstitucionInt()); // codigoInstitucion
		
		/*
		 *2. Settear a la presentacion grafica 
		 */
		forma.setListaHistoricoPresuIngreso(listTMP);
		
	}

	/**
	 * Metodo para guardar el cancelar un presupuesto.
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionGuardarCancelarPresupuesto(	ActionMapping mapping, 
															HttpServletRequest request,
															PresupuestoOdontologicoForm forma, 
															UsuarioBasico usuario,
															PersonaBasica paciente,
															HttpServletResponse response)
	{
		//1. INICIO DE LA TRANSACCION
		Connection con = UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		//2. MODIFICAMOS EL PRESUPUESTO A ESTADO CANCELADO
		forma.getDtoPresupuesto().setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		PresupuestoOdontologico.modificarPresupuesto(forma.getDtoPresupuesto(), con);
		
		//3. ANULAMOS EL DCTO DEL PRESUPUESTO SI EXISTE
		if(!PresupuestoOdontologico.anularDescuentoPresupuesto(con, forma.getDtoPresupuesto().getCodigoPK(), usuario.getLoginUsuario(), forma.getCodigoMotivoAnulacionDctoSel()))
		{
			logger.error("NO Presupuesto");
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
		}
		
		//4. LLENAMOS EL LOG DEL PRESUPUESTO Y LO GUARDAMOS
		DtoLogPresupuestoOdontologico dtoLog = llenarDtoLog(forma);
		
		logger.info("modifica  100%%%");
		
		//5. FINALIZAMOS LA TRANSACCION
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		
		//6. VOLVEMOS AL ESTADO EMPEZAR
		forma.setEstado("empezar");
		return accionEmpezar(mapping, forma, usuario, paciente, request, false /*crear bloqueo ya existe*/, response);
	}

	/**
	 * Metodo para llenar el Log de presupuesto con los datos de la forma
	 * @param forma
	 * @return
	 */
	private DtoLogPresupuestoOdontologico llenarDtoLog(	PresupuestoOdontologicoForm forma) 
	{
		DtoLogPresupuestoOdontologico dtoLog = new DtoLogPresupuestoOdontologico();
		dtoLog.setCodigoPresupuesto((forma.getListaPresupuestos().get(forma.getPosArray())).getCodigoPK());
		dtoLog.setEspecialidad((forma.getListaPresupuestos().get(forma.getPosArray())).getEspecialidad());
		dtoLog.setUsuarioModifica((forma.getListaPresupuestos().get(forma.getPosArray())).getUsuarioModifica());
		dtoLog.setEstado((forma.getListaPresupuestos().get(forma.getPosArray())).getEstado());
		dtoLog.setMotivo((forma.getListaPresupuestos().get(forma.getPosArray())).getMotivo());
		return dtoLog;
	}


	/**
	 * Método para cargar el popup de detalle y limpiar la página ppal
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param paciente
	 * @param response
	 * @param institucion
	 * @return
	 */
	private ActionForward accionCargarDetalleYLimpiarPpal(	ActionMapping mapping, 
															HttpServletRequest request,	
															PresupuestoOdontologicoForm forma, 
															PersonaBasica paciente,
															HttpServletResponse response,
															int institucion)
	{
		
		//1. RESETEAMOS EL DETALLE PRESUPUESTO
		forma.resetDetallePresupuesto();
		forma.setModificando(false);
		
		//2. RESET DEL PLAN DE TRATAMIENTO
		forma.resetPlanTratamiento();
		
		//3. CARGAR PLAN TRATAMIENTO
		ActionForward forward= cargarPlanTratamiento(forma, paciente, request, mapping, BigDecimal.ZERO, response, request.getSession().getId(), institucion);
		if(forward!=null)
			return forward;
		
		//4. CARGAMOS CONVENIO/CONTRATO PRESUPUESTO
		
		IPresupuestoOdontologicoServicio presupuestoOdontologicoServicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
		
		forma.setListaConvenioPresupuesto(presupuestoOdontologicoServicio.cargarConveniosContratoPresupuesto(
				forma.getListaSumatoriaConvenios(), paciente.getCodigoPersona()));
		
		return mapping.findForward("paginaPrincipal");
	}
	
	
	
	
	
	/**
	 * Metodo con las valiaciones de acceso al presupuesto
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward validacionesAcceso(	ActionMapping mapping,HttpServletRequest request, PersonaBasica paciente, 
												UsuarioBasico usuario ,	PresupuestoOdontologicoForm forma, HttpServletResponse response) 
	{
		
		request.setAttribute("pruebaPresupuesto", "pruebaPresupuesto");
		
		// Inicializamos variables --------------------------------------------------------------------------------------------
		forma.setTieneIngresoAbierto(true);
		if(paciente.getCodigoIngreso() <=0){
			IIngresosServicio ingresosServicio = ManejoPacienteServicioFabrica.crearIngresosServicio();
			DtoInfoIngresoTrasladoAbonoPaciente dtoIngreso = ingresosServicio.obtenerUltimoIngresoPaciente(paciente.getCodigoPersona());
			if(dtoIngreso != null){
				paciente.setCodigoIngreso(dtoIngreso.getIdIngreso());
			}
		}
		//---------------------------------------------------------------------------------------------------------------------
		
		//1. CARGAMOS UN ATRIBUTO "DUMMY" PARA SABER SI DEBEMOS MOSTRAR LA PAGINA CON ENCABEZADO PPAL O NO- 
		boolean esDummy = false;
		if(request.getServletPath().equals("/presupuestoOdontologicoDummy/presupuestoOdontologicoDummy.do"))
		{
			esDummy = true;
		}
		
		//2. ABRIMOS CONECCION PARA UTILIZAR METODOS VIEJOS
		Connection con = UtilidadBD.abrirConexion();
		
		//3. HACEMOS VALIDACIONES DE PACIENTE
		if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
		{
			logger.warn("Paciente no vï¿½lido (null)");
			if(esDummy)
			{
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("errors.paciente.noCargado"));
				saveErrors(request, errores);
		       	UtilidadBD.closeConnection(con);
		       	return mapping.findForward("paginaErroresActionErrorsSinCabezote");
			}
			else
			{
				request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
		}
		
		//4. VALIDAMOS EL ESTADO DEL INGRESO
		else if (UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().equals(ConstantesIntegridadDominio.acronimoEstadoCerrado))
		{
			logger.warn("INGRESO CERRADO (null)");	
			forma.setTieneIngresoAbierto(false);
			/*
			request.setAttribute("codigoDescripcionError","errors.paciente.noIngreso");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaError");
			*/
		}
		
		//5. VALIDAMOS LA CUENTA DEL PACIENTE
		else if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta()) <= 0)
		{
			logger.warn("Cuenta no Activa (null)");			
			if(esDummy)
			{
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("errors.paciente.cuentaNoValidaOdontologia"));
				saveErrors(request, errores);
		       	UtilidadBD.closeConnection(con);
		       	return mapping.findForward("paginaErroresActionErrorsSinCabezote");
			}
			else
			{
				request.setAttribute("codigoDescripcionError", "errors.paciente.cuentaNoValidaOdontologia");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
		}
		
		//6. VALIDAMOS LA VIA DE INGRESO DEL PACIENTE
		else if (paciente.getCodigoUltimaViaIngreso() != ConstantesBD.codigoViaIngresoConsultaExterna)
		{
			if(esDummy)
			{
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("error.cita.asignacion.viaIngreso"));
				saveErrors(request, errores);
		       	UtilidadBD.closeConnection(con);
		       	return mapping.findForward("paginaErroresActionErrorsSinCabezote");
			}
			else
			{
				logger.warn("Via de ingreso no es CONSULTA EXTERNA (null)");			
				request.setAttribute("codigoDescripcionError","error.cita.asignacion.viaIngreso");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
		}
		
		//7
		else if(!existeConsecutivoDescuento(usuario.getCodigoInstitucionInt(), con))
		{
			ActionErrors errores = new ActionErrors(); 
			errores.add("", new ActionMessage("error.faltaDefinirConsecutivo", "Consecutivo Solicitud Descuento Odontolï¿½gico"));
	       	saveErrors(request, errores);
	       	UtilidadBD.closeConnection(con);
			if(esDummy)
			{
				return mapping.findForward("paginaErroresActionErrorsSinCabezote");
			}
			else
			{
				logger.warn("NO EXISTE EL CONSECUTIVO DESCUENTO ODONTOLOGICOS");
				
		       	return mapping.findForward("paginaErroresActionErrors");
			}
		}
		
		//8. VALIDAMOS LA CONCURRENCIA
		DtoConcurrenciaPresupuesto dtoConcurrencia = PresupuestoOdontologico.estaEnProcesoPresupuesto(con, paciente.getCodigoCuenta(), request.getSession().getId(), false);
		if(dtoConcurrencia.existeConcurrencia())
		{
			logger.error("ya existe un usuario generando presupuesto!!!!");
			saveErrors(request, dtoConcurrencia.getErrorConcurrencia());
			if(esDummy)
			{
				return mapping.findForward("paginaErroresActionErrorsSinCabezote");
			}
			else
			{
				return mapping.findForward("paginaErroresActionErrors");
			}
		}
		/*
		 *
		 */
		//VALIDAMOS QUE EL PARAMETRO VALIDAR PRESUPUESTO CONTRATADO
		
		
		
		
	
		/*
		 *Settear validacion parametro general validar presupuesto Contratado 
		 */
		String validarParametroGeneral= ValoresPorDefecto.getValidaPresupuestoOdoContratado(usuario.getCodigoInstitucionInt());
		
		
		/*
		 * Validacion que exista el parametro valida presupuesto Contratado
		 */
		if(UtilidadTexto.isEmpty(validarParametroGeneral))
		{
		
			String bundleProperties="msgPresupuestoOdontologicoForm";
			String keyProperties="PresupuestoOdontologicoForm.mensajes.faltaDefinirParemetro";
			
			request.setAttribute("keyProperties", keyProperties);
			request.setAttribute("bundleProperties", bundleProperties);
			
			/*
			 * Nota se Modificaron estas lineas debedio a estandar de interfaz grafica
			 * Ya no es  un error si no un Mensaje.
			ActionErrors errores = new ActionErrors(); 
			errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro", "Valida presupuesto odontologico contratado"));
	       	saveErrors(request, errores);
	       	UtilidadBD.closeConnection(con);
			if(esDummy)
			{
				return mapping.findForward("paginaErroresActionErrorsSinCabezote");
			}
			else
			{
				return mapping.findForward("paginaErroresActionErrors");
			}*/
			
			return mapping.findForward("paginaMensajesBloqueantes"); // Mensajes Bloqueantes
		}
		
		
		//9. CERRAMOS LA CONEXION
		UtilidadBD.closeConnection(con);
		return null;
	}
	
	/**
	 * Metodo que inicializa el workflow del presupuesto
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping,
										PresupuestoOdontologicoForm forma, 
										UsuarioBasico usuario, 
										PersonaBasica paciente, 
										HttpServletRequest request,
										boolean crearBloqueo,
										HttpServletResponse response) 
	{
		//1. SETEAMOS UN ATRIBUTO DUMMY PARA SABER SI DEBEMOS QUITAR O NO EL ENCABEZADO SUPERIOR
		boolean esDummy = false;
		if(request.getServletPath().equals("/presupuestoOdontologicoDummy/presupuestoOdontologicoDummy.do"))
		{
			esDummy = true;
		}
		
		//2. VALIDAMOS SI DEBEMOS INSERTAR EL BLOQUEO DE ACCESO
		// Solamente se bloquea si tiene cuenta activa, en el caso contrario debería mostrar los históricos
		if(paciente.getCodigoCuenta() >0 && crearBloqueo)
		{	
			DtoConcurrenciaPresupuesto dtoConcurrencia = PresupuestoOdontologico.estaEnProcesoPresupuesto(paciente.getCodigoCuenta(), request.getSession().getId(), true);
			if(!dtoConcurrencia.existeConcurrencia())
			{	
				if(!PresupuestoOdontologico.empezarBloqueoPresupuesto(paciente.getCodigoCuenta(), usuario.getLoginUsuario(), request.getSession().getId(), false))
				{	
					logger.error("concurrencia!!!!!!!");
					if(esDummy)
					{
						ActionErrors errores = new ActionErrors();
						errores.add("", new ActionMessage("errors.problemasBd"));
						saveErrors(request, errores);
						return mapping.findForward("paginaErroresActionErrorsSinCabezote");
					}
					else
					{
						return ComunAction.accionSalirCasoError(mapping, request, null, logger, "", "errors.problemasBd", true);
					}
					
				}
			}	
		}
		
		//3. RESETEAMOS TODOS LOS ATRIBUTOS DE LA FORMA
		forma.reset();
	

		/*
		 * 3.1 Cargar parametro general
		 * Validar Presupuesto Contratado 
		 */
		String validarParametroGeneral= ValoresPorDefecto.getValidaPresupuestoOdoContratado(usuario.getCodigoInstitucionInt());
		Log4JManager.info(validarParametroGeneral);
		forma.setValidaPresupuestoOdoContratado(validarParametroGeneral);
		
		
		
		//4. SETEAMOS LOS PERMISOS PARA (MODIFICAR - COPIAR - CONTRATAR - ....)
		/*
		 * Metodo para cargar los permisos basicos sobre la funcionalidades 
		 */
		forma.setPermisos(PresupuestoOdontologico.cargarPermisosPresupuesto(usuario.getLoginUsuario()));
		
		//5. SE DEFINE SI ES X PROGRAMA O SERVICIO
		forma.setTipoRelacion(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)?"Programa":"Servicio");
		
		//6. CARGAMOS EL PRESUPUESTO
		DtoPresupuestoOdontologico dtoWhere = new DtoPresupuestoOdontologico();
		dtoWhere.setIngreso(new BigDecimal(paciente.getCodigoIngreso()));
		forma.setListaPresupuestos(PresupuestoOdontologico.cargarPresupuesto(dtoWhere));
		
		/*
		 *6.2 Clasificar a que prespuesto se les puede contratar 
		 */
		forma.setListaPresupuestos(PresupuestoHelperVista.validarCentroAtencionPresupuesto(forma.getListaPresupuestos(), usuario));
		
		
		//Se cargan los programas o servicios
		for(DtoPresupuestoOdontologico dto: forma.getListaPresupuestos())
		{
			dto.setListaTarifas(PresupuestoOdontologico.cargarTotalesPresupuetoConvenio(dto.getCodigoPK()));
		}
		
		
		/*
		 * TODO OJO ELIMInar estas lineas  
		 */
		@SuppressWarnings("unused")
		BigDecimal valorDescuento=BigDecimal.ZERO;
		
		for(DtoPresupuestoOdontologico dtoPresupuesto: forma.getListaPresupuestos())
		{
			if(!dtoPresupuesto.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoActivo))
			{
				valorDescuento=PresupuestoOdontologico.cargarValorPresupuesto(dtoPresupuesto.getCodigoPK());
			}
		} 
		
		IPresupuestoOdontologicoServicio presupuestoOdontologicoServicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();

		//Se carga la sumatoria de convenios
		forma.setListaSumatoriaConvenios(presupuestoOdontologicoServicio.obtenerListadoConvenios(forma.getListaPresupuestos()));
		
		ActionForward forward= cargarPlanTratamiento(forma, paciente, request, mapping, BigDecimal.ZERO, response, request.getSession().getId(), usuario.getCodigoInstitucionInt());
		if(forward!=null)
		{	
			accionProcesoCancelado(paciente, forma, mapping, response, request.getSession().getId());
			return forward;
		}
		
		forma.setListaConvenioPresupuesto(presupuestoOdontologicoServicio.cargarConveniosContratoPresupuesto(
				forma.getListaSumatoriaConvenios(), paciente.getCodigoPersona()));

		return mapping.findForward("paginaPrincipal");
	}	
	
	/**
	 * Metodo para ordenar los registros del listado de presupuesto existentes
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenar(	ActionMapping mapping,
											PresupuestoOdontologicoForm forma, 
											UsuarioBasico usuario) 
	{
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		int convenioSort=0;
		if(forma.getPatronOrdenar().split(ConstantesBD.separadorSplitComplejo).length==2)
		{
			convenioSort=Utilidades.convertirAEntero(forma.getPatronOrdenar().split(ConstantesBD.separadorSplitComplejo)[1]);
			forma.setPatronOrdenar(forma.getPatronOrdenar().split(ConstantesBD.separadorSplitComplejo)[0]);
		}
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()))
		{
			forma.setEsDescendente(forma.getPatronOrdenar()+"descendente");
		}
		else
		{
			forma.setEsDescendente(forma.getPatronOrdenar());
		}	
		for(DtoPresupuestoOdontologico dto: forma.getListaPresupuestos())
		{
			dto.setConvenioSort(convenioSort);
		}
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaPresupuestos(),sortG);
		forma.setEstado("empezar");
		return mapping.findForward("paginaPrincipal");
	}
	
	
	/**
	 * Metidi para consultar los historicos de presupuestos
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionConsultarHistorico(	ActionMapping mapping, 
													PresupuestoOdontologicoForm forma, 
													UsuarioBasico usuario)
	{
		DtoLogPresupuestoOdontologico dtoHistorico = new DtoLogPresupuestoOdontologico();
     	//forma.setDtoPresupuesto(forma.getListaPresupuestos().get(forma.getPosArray()));
		forma.setEncabezadoHistorico(new InfoDatosDouble(forma.getListaPresupuestos().get(forma.getPosArray()).getConsecutivo().doubleValue(), ValoresPorDefecto.getIntegridadDominio(forma.getListaPresupuestos().get(forma.getPosArray()).getEstado())+""));
		dtoHistorico.setCodigoPresupuesto(forma.getListaPresupuestos().get(forma.getPosArray()).getCodigoPK());
	 	logger.info("CODIGO PRESUPUESTO *************************"+forma.getListaPresupuestos().get(forma.getPosArray()).getCodigoPK());
		forma.setListaPresupuestosTmp(PresupuestoOdontologico.cargarLogPresupuesto(dtoHistorico));
	 	logger.info("TERMINA");
		return mapping.findForward("historicoPresupuesto");
	}
 
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarDetalle(	ActionMapping mapping, PresupuestoOdontologicoForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		// Anexo 1049. Tambien aplica para todos los anexos de formato de impresion
		Log4JManager.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>  accionEmpezarDetalle  >>  llenarListaOpcionesImprimir");
		
		//1. RESETEAMOS LA POS DEL ARRAY PROGRAMA/SERVICIO
		forma.setPosArrayProgramaServicio(ConstantesBD.codigoNuncaValido);
		
		//2. CLONAMOS EL ENCABEZADO DEL PRESUPUESTO
		forma.setDtoPresupuesto((DtoPresupuestoOdontologico)forma.getListaPresupuestos().get(forma.getPosArray()).clone());
		
		//3. CARGAMOS LOS PROGRAMAS/SERVICIOS DEL PRESUPUESTO
		forma.setListaProgramasServicios(PresupuestoOdontologico.cargarPresupuestoOdoProgServ(forma.getDtoPresupuesto().getCodigoPK()));
		
		//4. CARGAMOS LOS DETALLES DE CONVENIOS Y LAS PIEZAS ASOCIADAS 
		this.cargarConveniosPiezasXProgramaServicio(forma, false);
		
		//5. SE CARGAN LOS TOTALES
		forma.setListaSumatoriaConvenios(PresupuestoOdontologico.cargarTotalesPresupuetoConvenio(forma.getDtoPresupuesto().getCodigoPK()));
		
		//6. SE CARGAN LOS TOTALES CUANDO ESTA CONTRATADO
		forma.setDtoPresupuestoContratado(PresupuestoOdontologico.obtenerTotalesContratadoPrecontratado(forma.getDtoPresupuesto().getCodigoPK()));
		
		// Llenar las opciones para imprimir el presupuesto
		forma.setMostrarImpresionContratoOdonto(false);
		llenarListaOpcionesImprimir(forma, usuario, request);
		
		return mapping.findForward("paginaDetalle");
		
	}
	
	
	/**
	 * Llena la lista con las opciones de impresion disponibles.
	 * Segun el anexo 881 se deben hacer unas validaciones para ver que opciones mostrar en este caso.
	 * Para el caso de Otros Si se listan todos los OtroSi disponibles para el presupuesto
	 * 
	 * @param forma
	 * @param usuario
	 * @param request
	 * 
	 * @author Cristhian Murillo
	 */
	private void llenarListaOpcionesImprimir(PresupuestoOdontologicoForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		forma.setListaOpcionesImprimirMostrar(new ArrayList<DtoCheckBox>());
		forma.setListaOpcionesImprimirOtrosSi(new ArrayList<DtoOtroSi>());
		
		// Esta opción siempre se muestran --------------------------------------------------
		forma.getListaOpcionesImprimirMostrar().add(new DtoCheckBox(ConstantesBD.anexoPresupuestoPresupuesto, true));
		// -------------------------------------------------------------------------------------
		
		
		// se valida si se debe mostrar la opción de contratos ---------------------------
		ArrayList<String> estadosValidosParaImprimir = new ArrayList<String>();
			estadosValidosParaImprimir.add(ConstantesIntegridadDominio.acronimoContratadoContratado);
			estadosValidosParaImprimir.add(ConstantesIntegridadDominio.acronimoContratadoCancelado);
			estadosValidosParaImprimir.add(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente);
			estadosValidosParaImprimir.add(ConstantesIntegridadDominio.acronimoContratadoTerminado);
			estadosValidosParaImprimir.add(ConstantesIntegridadDominio.acronimoContratado);
		
		ContratoOdontologicoMundo contratoOdontologicoMundo = new ContratoOdontologicoMundo();
		DtoFormatoImpresionContratoOdontologico dtoFormatoImpresionContratoOdontologico = new DtoFormatoImpresionContratoOdontologico();
		dtoFormatoImpresionContratoOdontologico.setCodigoPkPresupuesto(forma.getDtoPresupuesto().getCodigoPK().longValue());
		dtoFormatoImpresionContratoOdontologico.setValorTotalPresupuesto(forma.getDtoPresupuestoContratado().getTotalPresupuestoParaDescuentoFormateado());
		dtoFormatoImpresionContratoOdontologico = contratoOdontologicoMundo.generarImpresionContratoOdontologico(dtoFormatoImpresionContratoOdontologico);
			
		if(estadosValidosParaImprimir.contains(dtoFormatoImpresionContratoOdontologico.getEstadoPresupuesto())){
			forma.getListaOpcionesImprimirMostrar().add(new DtoCheckBox(ConstantesBD.anexoPresupuestoContrato, true));
		}
		// --------------------------------------------------------------------------------------
		
		
		// se valida si se debe mostrar la opción de recomendaciones ---------------------------
		IRecomendacionesContOdontoServicio recomendacionesContOdontoServicio = RecomendacionSERVICIOFabrica.crearRecomendacionesCont();
		ArrayList<DtoRecomendaciones> listaRecomendaciones = new ArrayList<DtoRecomendaciones>();
		listaRecomendaciones = recomendacionesContOdontoServicio.obtenerRecomendacionesPresuOdonto(forma.getDtoPresupuesto().getCodigoPK().longValue());
		if(!Utilidades.isEmpty(listaRecomendaciones)){
			forma.getListaOpcionesImprimirMostrar().add(new DtoCheckBox(ConstantesBD.anexoPresupuestoRecomendaciones, true));
		}
		Log4JManager.info("------------------- cantidad recomendaciones: "+listaRecomendaciones.size());
		// --------------------------------------------------------------------------------------
		
		
		// se valida si se debe mostrar la opción de Otros Si -------------------------------------
		if(PresupuestoExclusionesInclusiones.existenInclusionesExclusionesPresupuesto(forma.getDtoPresupuesto().getCodigoPK(), usuario.getCodigoInstitucionInt()))
		{
			//ANTES: forma.getListaOpcionesImprimirMostrar().add(new DtoCheckBox(ConstantesBD.anexoPresupuestoOtro, true));
			// Se consultan todos los otros si disponibles para el presupuesto
			IOtrosSiServicio otrosSiServicio = ContratoFabricaServicio.crearOtrosSiServicio();
			forma.setListaOpcionesImprimirOtrosSi(otrosSiServicio.obtenerOtrosSiporPresupuesto(forma.getDtoPresupuesto().getCodigoPK().longValue()));
			for (DtoOtroSi dtoOtroSi : forma.getListaOpcionesImprimirOtrosSi()) {
				dtoOtroSi.setCheck(true);
			}
		}
		// --------------------------------------------------------------------------------------
		
	}
	
	
	
	/**
	 * Imprime las opciones seleccionadas para imprimir, tanto anexos como otrosSi
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param paciente
	 * @return ActionForward
	 * 
	 * @autor Cristhian Murillo
	 */
	private ActionForward accionImprimirReporteDetalle(	ActionMapping mapping, PresupuestoOdontologicoForm forma, 
														UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente) 
	{
		// Aca llega con cualquiera de estos dos estados: "imprimirReporteDetalle" o "imprimirReporteContratar"
		forma.setMostrarImpresionContratoOdonto(false);
		boolean exitoso = false;

		CentroAtencionServicio centroAtencionServicio = new CentroAtencionServicio();
		com.servinte.axioma.orm.CentroAtencion centroAtencion = new com.servinte.axioma.orm.CentroAtencion();
		centroAtencion = centroAtencionServicio.buscarPorCodigoPK(usuario.getCodigoCentroAtencion());

		// Se recorren las opciones a imprimir y  se toman las que fueron seleccionadas
		for (DtoCheckBox dtoCheckBox : forma.getListaOpcionesImprimirMostrar()) 
		{
			if(dtoCheckBox.isCheck()== true)
			{
				Log4JManager.info(">>>>>>>>>> Fue seleccionado para imprimir "+dtoCheckBox.getNombre());
				
				if(dtoCheckBox.getNombre().equalsIgnoreCase(ConstantesBD.anexoPresupuestoContrato))
				{
					imprimirContratoPresupuesto(forma, dtoCheckBox, centroAtencion);
					exitoso = true;
				}
				if(dtoCheckBox.getNombre().equalsIgnoreCase(ConstantesBD.anexoPresupuestoRecomendaciones))
				{
					imprimirRecomendacionesPresupuesto(forma, dtoCheckBox, centroAtencion);
					exitoso = true;
				}
				/*
				if(dtoCheckBox.getNombre().equalsIgnoreCase(ConstantesBD.anexoPresupuestoOtro))
				{
					imprimirOtroSiPresupuesto(forma, request, usuario);
				}
				*/
				if(dtoCheckBox.getNombre().equalsIgnoreCase(ConstantesBD.anexoPresupuestoPresupuesto))
				{
					exitoso = imprimirReporteDetalle(mapping, forma, request, usuario, paciente);
				}
			}
		}
		
		
		// Se recorren los otroSi a imprimir y  se toman las que fueron seleccionadas
		int i = 0;
		for (DtoOtroSi dtoOtroSi : forma.getListaOpcionesImprimirOtrosSi()) 
		{
			if(dtoOtroSi.isCheck()== true)
			{
				imprimirOtroSiPresupuesto(forma, request, usuario, dtoOtroSi.getOtroSi(), i);
				exitoso = true;
			}
			i++;
		}
		
		
		
		 if(forma.getEstado().equals("imprimirReporteContratar"))
	     {
			 for (DtoCheckBox dtoCheckBox : forma.getListaOpcionesImprimirMostrar()) {
				 dtoCheckBox.setCheck(true);
			 }
			 
			 if(exitoso){
				 forma.setEstado("contratadoExitoso");
			 }
	 		return mapping.findForward("contratarPresupuesto");
	     }
		 else if(forma.getEstado().equals("imprimirReporteDetalle")){
				 return mapping.findForward("paginaDetalle");
		 }
		 
		 return null;
	}
	
	
	private boolean imprimirReporteDetalle(ActionMapping mapping, PresupuestoOdontologicoForm forma, 
			HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente){
		
		Log4JManager.info(">>>>> Imprimiendo tipo Presupuesto");
		
		 String nombreRptDesign = "DetallePresupuestoOdontologico.rptdesign";	   	             
		 String sql="";
		 
		 if(!Utilidades.isEmpty(forma.getListaPresupuestos()))
		 {
			 if(!forma.getListaPresupuestos().get(forma.getPosArray()).getEstado().equals(ConstantesIntegridadDominio.acronimoContratadoContratado)
					&& !forma.getEstado().equals("imprimirReporteContratar"))
			 {
				 sql= PresupuestoOdontologico.consultaReporteDetalle(forma.getDtoPresupuesto().getCodigoPK().intValue() , forma.getTipoRelacion());
			 }
			 else
			 {
				 sql= PresupuestoOdontologico.consultaReporteContratar( forma.getTipoRelacion() , forma.getDtoPresupuesto().getCodigoPK().intValue() );
			 }
		 }else{
			 sql= PresupuestoOdontologico.consultaReporteContratar( forma.getTipoRelacion(),
					 forma.getDtoPresupuesto().getCodigoPK().intValue() );
		 }
	       		
	     InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica"); 
	     
	     int celdaImagen=0;
	     int celdaInfoDiagnosticador=2;
	     
	     logger.info("///////////////////////////ins.getUbicacionLogo()------------>"+ins.getUbicacionLogo());
	     
	     if(ins.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha))
	     {
	    	 celdaImagen=2;
	    	 celdaInfoDiagnosticador=0;
	     }
	    	 
	     
	     DesignEngineApi comp; 
	     comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"odontologia"+System.getProperty("file.separator"),nombreRptDesign);
	     // Inserta Logo
	     comp.insertImageHeaderOfMasterPage1(0, celdaImagen, ins.getLogoReportes());
	     //Crea una grilla en la posicion 0,1 con una fila y 4 columnas
	     comp.insertGridHeaderOfMasterPage(0,1,1,4);
	     Vector v=new Vector();

	     CentroAtencion centroAtencion= new CentroAtencion();
	     centroAtencion.consultar(usuario.getCodigoCentroAtencion());
	     v.add("Nro PRESUPUESTO: " + forma.getDtoPresupuesto().getConsecutivo());
	     v.add("CENTRO ATENCION: " + centroAtencion.getDescripcion());
	     v.add("DIRECCION: " +centroAtencion.getDireccion() );
	     v.add("TELEFONO: " + centroAtencion.getTelefono());
	     comp.insertLabelInGridOfMasterPage(0,1,v);
	     comp.insertGridHeaderOfMasterPageWithName(0,celdaInfoDiagnosticador,1,4, "datos presupuesto2");
	     Vector v4=new Vector();
	     v4.add("ESTADO: " + ValoresPorDefecto.getIntegridadDominio(forma.getDtoPresupuesto().getEstado()));
	     v4.add("FECHA: " + UtilidadFecha.getFechaActual());
	     v4.add("HORA: " + UtilidadFecha.getHoraActual());
	     v4.add("DIAGNOSTICADOR: " + Utilidades.obtenerNombreUsuarioXLogin(forma.getDtoPresupuesto().getUsuarioModifica().getUsuarioModifica(), true)); 
	     comp.insertLabelInGridOfMasterPage(0,celdaInfoDiagnosticador,v4);
	     comp.insertGridHeaderOfMasterPageWithName(2,0,1,2,"datos paciente1");
	     Vector v2=new Vector();
	     v2.add("PACIENTE: " + paciente.getNombrePersona(false));
	     v2.add("DIRECCIÓN: " + paciente.getDireccion());
	     comp.insertLabelInGridOfMasterPageWithProperties(2,0,v2,DesignChoiceConstants.TEXT_ALIGN_LEFT);
	     comp.insertGridHeaderOfMasterPageWithName(2,1,1,2,"datos paciente2");
	     Vector v3=new Vector();
	     v3.add("DOCUMENTO DE IDENTIDAD: " + paciente.getTipoIdentificacionPersona(false) + "  -  " +paciente.getNumeroIdentificacionPersona());
	     if(!UtilidadTexto.isEmpty(paciente.getTelefonoFijo()))
	     {	 
	    	 v3.add("TELEFONO: " + paciente.getTelefonoFijo());
	     }
	     else
	     {
	    	 if(!UtilidadTexto.isEmpty(paciente.getTelefonoCelular()))
	    	 {
	    		 v3.add("CELULAR: "+paciente.getTelefonoCelular());
	    	 }
	    	 else
	    	 {
	    		 v3.add("TELEFONO:");
	    	 }
	     }
	     comp.insertLabelInGridOfMasterPageWithProperties(2,1,v3,DesignChoiceConstants.TEXT_ALIGN_LEFT);
	     comp.insertLabelInGridPpalOfHeader(1,0, "PRESUPUESTO ODONTOLOGICO");
	     
	     //insertar pie de pagina
	     if(!UtilidadTexto.isEmpty(centroAtencion.getPiePaginaPresupuestoOdon().trim()))
	     {	 
		     logger.info("PATH DEL FOOTER--->"+CarpetasArchivos.IMAGENES_PRESUPUESTO_ODON.getRutaFisica()+centroAtencion.getPiePaginaPresupuestoOdon());
		     comp.insertImageFooterOfMasterPage1(0, 0,  CarpetasArchivos.IMAGENES_PRESUPUESTO_ODON.getRutaFisica()+centroAtencion.getPiePaginaPresupuestoOdon());
	     }    
	     
	     comp.obtenerComponentesDataSet("DetallePresupuestoOdontologico");
	     comp.modificarQueryDataSet(sql);
	                
	        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
	     comp.lowerAliasDataSet();
	     String newPathReport = comp.saveReport1(false);
	     comp.updateJDBCParameters(newPathReport);
	        
	     if(!newPathReport.equals(""))
	     {
	    	 request.setAttribute("isOpenReport", "true");
	    	 request.setAttribute("newPathReport", newPathReport);
	    	 return true;
	     }
	     else {
	    	 return false;
	     }
	     
	     /*
	     if(forma.getEstado().equals("imprimirReporteContratar"))
	     {
	    	 //forma.setEstado("contratadoExitoso");
	 		return mapping.findForward("contratarPresupuesto");
	     }
	     
		 return mapping.findForward("paginaDetalle");
		 
		 */
	}
	
	
	
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarModificar(	ActionMapping mapping,
													PresupuestoOdontologicoForm forma, 
													UsuarioBasico usuario,
													PersonaBasica paciente,
													HttpServletRequest request,
													HttpServletResponse response
												) throws IPSException 
	{
		//1. RESETEAMOS LOS ATRIBUTOS DE LA FORMA
		forma.resetModificar();
		
		//2. CLONAMOS EL DTO DE LA LISTA Y LO COLOCAMOS EN EL DTO DEL DETALLE
		forma.setDtoPresupuesto((DtoPresupuestoOdontologico)forma.getListaPresupuestos().get(forma.getPosArray()).clone());
		
		//3. CARGAMOS EL PLAN TRATAMIENTO DEL PRESUPUESTO ALMACENADO
		ActionForward forward= cargarPlanTratamiento(forma, paciente, request, mapping, forma.getDtoPresupuesto().getCodigoPK(), response, request.getSession().getId(), usuario.getCodigoInstitucionInt());
		if(forward!=null)
		{
			return forward;
		}
		
		//3. CARGAMOS LOS PROGRAMAS/SERVICIOS DEL PRESUPUESTO
		forma.setListaProgramasServicios(PresupuestoOdontologico.cargarPresupuestoOdoProgServ(forma.getDtoPresupuesto().getCodigoPK()));
		
		//4. CARGAMOS LA INFORMACION DE LOS CONVENIOS Y LAS PIEZAS CORRESPONDIENTES A LOS PROGRAMAS/SERVICIOS 
		this.cargarConveniosPiezasXProgramaServicio(forma, true);
		
		//5. RECALCULAMOS LAS TARIFAS
		this.calcularTarifasPendientes(forma, usuario, paciente);
		
		//6. RECALCULAMOS LOS BONOS Y PROMOCIONES
		this.recalcularBonosPromociones(forma, usuario);
		
		//7. SE CARGAN LOS TOTALES
		forma.setListaSumatoriaConvenios(PresupuestoOdontologico.cargarTotalesPresupuetoConvenio(forma.getDtoPresupuesto().getCodigoPK()));
		
		//8. RECALCULO DE LOS TOTALES PARA QUITAR LOS PROGRAMAS/SERVICIOS DEL PRESUPUESTO QUE YA NO APLICAN EN EL PLAN DE TRATAMIENTO 
		this.recalcularTotalesSinProgramasServEstadoNoPendiente(forma);
		
		//9. CARGAMOS LOS CONVENIOS / CONTRATOS X PRESUPUESTO
		this.cargarConveniosContratosPresupuesto(forma);
		
		//10. MARCAMOS LOS PROGRAMAS/SERVICIOS YA SELECCIONADOS EN EL PRESUPUESTO, PARA NO DEJAR SELECCIONARLOS EN EL PLAN DE TRATAMIENTO
		marcarServicioProgramasYaSeleccionadosPresupuesto(forma);
		
		//cargamos los paquetes
		forma.setListaPaquetes(PresupuestoPaquetes.cargarInfoPaquetesTarifas(forma.getDtoPresupuesto().getCodigoPK()));
		
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * @param forma
	 */
	private void cargarConveniosContratosPresupuesto(PresupuestoOdontologicoForm forma) 
	{
		forma.setListaConvenioPresupuesto(new ArrayList<InfoConvenioContratoPresupuesto>());
		for(DtoPresupuestoTotalConvenio infoConvenio: forma.getListaSumatoriaConvenios())
		{	
			InfoConvenioContratoPresupuesto info= new InfoConvenioContratoPresupuesto();
			info.setActivo(true);
			info.setEsParametroGeneral(false);
			info.setConvenio(infoConvenio.getConvenio());
			
			InfoDatosInt contrato=new InfoDatosInt();
			for(DtoPresuOdoProgServ dtop : forma.getListaProgramasServicios())
			{
				for(DtoPresupuestoOdoConvenio detalleConvenioServPrograma: dtop.getListPresupuestoOdoConvenio())
				{
					if(infoConvenio.getConvenio().getCodigo()== detalleConvenioServPrograma.getConvenio().getCodigo())
					{
						contrato= detalleConvenioServPrograma.getContrato();
						break;
					}
				}
			}
			info.setContrato(contrato);
			forma.getListaConvenioPresupuesto().add(info);
		}
	}

	/** 
	 * @param forma
	 */
	private void cargarConveniosPiezasXProgramaServicio(PresupuestoOdontologicoForm forma, boolean inactivarNoPendientesPlanTratamiento) 
	{
		for(DtoPresuOdoProgServ dtop : forma.getListaProgramasServicios())
		{
			dtop.setListPresupuestoOdoConvenio(PresupuestoOdontologico.cargarPresupuestoConvenio(dtop.getCodigoPk()));
			dtop.setListPresupuestoPiezas(PresupuestoOdontologico.cargarPresupuestoPieza(dtop.getCodigoPk(), inactivarNoPendientesPlanTratamiento, forma.getUtilizaPrograma()));
		}
	}

	/**
	 * @param forma
	 */
	private void recalcularTotalesSinProgramasServEstadoNoPendiente(PresupuestoOdontologicoForm forma) throws IPSException
	{
		///debemos recalcular los totales en el modificar para quitar los servicios - programas que no estan pendientes
		for(DtoPresupuestoTotalConvenio dtoTotal: forma.getListaSumatoriaConvenios())
		{
			BigDecimal totalSinContratar= BigDecimal.ZERO;
			for(DtoPresuOdoProgServ dtop : forma.getListaProgramasServicios())
			{
				//dtop.setTipoModificacion(EnumTipoModificacion.MODIFICADO);
				for(DtoPresupuestoOdoConvenio dtoConv: dtop.getListPresupuestoOdoConvenio())
				{
					if(dtoConv.getConvenio().getCodigo()==dtoTotal.getConvenio().getCodigo())
					{	
						totalSinContratar= totalSinContratar.add(dtoConv.getValorTotalConvenioXProgServ(dtop.getCantidad()));
					}	
				}	
			}
			dtoTotal.setValorSubTotalSinContratar(totalSinContratar);
		}
	}

	/**
	 * Metodo para poder marcar los programas y servicios del plan de tratamiento con un fondo amarillo y sin permitir modificar 
	 * @param forma
	 * 
	 */
	private void marcarServicioProgramasYaSeleccionadosPresupuesto(PresupuestoOdontologicoForm forma)
	{
		///marcamos los servicios/programas que ya fueron seleccionados en el presupuesto para no dejarlos seleccionar nuevamente
		///seccion detallle
		for(InfoDetallePlanTramiento objPlan :   forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle())
		{
			 for(InfoHallazgoSuperficie objHallazgo : objPlan.getDetalleSuperficie())
			 {
				 for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
				 {
					 objServProg.setAsignadoAlPresupuesto(PresupuestoOdontologico.existeProgramaServicioPlanTratamientoEnPresupuesto(objPlan.getPieza().getCodigo(), objHallazgo.getSuperficieOPCIONAL().getCodigo(), objHallazgo.getHallazgoREQUERIDO().getCodigo(), objServProg.getCodigoPkProgramaServicio(), forma.getUtilizaPrograma(), forma.getDtoPresupuesto().getCodigoPK(), ConstantesIntegridadDominio.acronimoDetalle));
				 }
			 }
		 }	 
		
		//// seccion otros
		for(InfoDetallePlanTramiento objPlan :   forma.getDtoPlanTratamiento().getSeccionOtrosHallazgos())
		{
			 for(InfoHallazgoSuperficie objHallazgo : objPlan.getDetalleSuperficie())
			 {
				 for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
				 {
					 objServProg.setAsignadoAlPresupuesto(PresupuestoOdontologico.existeProgramaServicioPlanTratamientoEnPresupuesto(objPlan.getPieza().getCodigo(), objHallazgo.getSuperficieOPCIONAL().getCodigo(), objHallazgo.getHallazgoREQUERIDO().getCodigo(), objServProg.getCodigoPkProgramaServicio(), forma.getUtilizaPrograma(), forma.getDtoPresupuesto().getCodigoPK(), ConstantesIntegridadDominio.acronimoOtro));
				 }
			 }
		}	 
		
		////seccion boca
		for(InfoHallazgoSuperficie objHallazgo : forma.getDtoPlanTratamiento().getSeccionHallazgosBoca())
		{
			 for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
			 {
				 objServProg.setAsignadoAlPresupuesto(PresupuestoOdontologico.existeProgramaServicioPlanTratamientoEnPresupuesto(ConstantesBD.codigoNuncaValido/*objPlan.getPieza().getCodigo()*/, ConstantesBD.codigoNuncaValido/*objHallazgo.getSuperficieOPCIONAL().getCodigo()*/, objHallazgo.getHallazgoREQUERIDO().getCodigo(), objServProg.getCodigoPkProgramaServicio(), forma.getUtilizaPrograma(), forma.getDtoPresupuesto().getCodigoPK(), ConstantesIntegridadDominio.acronimoBoca));
			 }
		}
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAdicionarProgramaServicio(	ActionMapping mapping, 
															PresupuestoOdontologicoForm forma,
															UsuarioBasico usuario, 
															PersonaBasica paciente) throws IPSException
	
	{
		 extraerProgramasServiciosPlanAlPresupuesto(forma, usuario);
		 ////OTROS HALLAZGOS
		 extraerProgramasServiciosPlanAlPresupuestoSeccionOtros(forma, usuario);
		 ///////HALLAZGOS BOCA
		 extraerProgramasServiciosPlanAlPresupuestoSeccionBoca(forma,usuario);
		 
		 IPresupuestoOdontologicoServicio presupuestoOdontologicoServicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
		 
		 if(forma.getModificando() && forma.getEsNuevo())
		 {	 
			 ////hacemos el calculo de las cantidades dependiendo del numero de superficies
			 //calcularCantidadesXNumeroSuperficies(forma);

			 presupuestoOdontologicoServicio.calcularCantidadesXNumeroSuperficies(forma.getListaProgramasServicios(), forma.getUtilizaPrograma(), false);
		 }
		 else
		 {
			 presupuestoOdontologicoServicio.calcularCantidadesXNumeroSuperficies(forma.getListaProgramasServicios(), forma.getUtilizaPrograma(), true);

		 }
		 
		 ////ahora que ya tenemos el presupuesto, debemos hacer el calculo de las tarifas ENVIANDOLE LA CANTIDAD QUE YA LA TENEMOS AGRUPADA
		 /////cargar el detalle de los convenios
		 logger.info("EL NUMERO DE SERV/PROG AGRUPADOS SON:::"+forma.getListaProgramasServicios().size());
		 
		 
		 //calcularTarifas(forma, usuario, paciente);	 
		 
		 presupuestoOdontologicoServicio.calcularTarifas(forma.getListaProgramasServicios(), forma.getListaConvenioPresupuesto(), 
				 usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt(), paciente.getCodigoCuenta(), forma.getUtilizaPrograma());
		 

		 //crearValoresTotalesXConvenio(forma, usuario, paciente);
		 
		 forma.setListaSumatoriaConvenios(presupuestoOdontologicoServicio.crearValoresTotalesXConvenio(forma.getListaProgramasServicios(), forma.getListaConvenioPresupuesto()));

		 ////ojo este es copia es que me permita eliminar
		 forma.setPuedoEliminar(true);
		 
		 armarEstructuraPaquetesXTodos(forma, usuario, paciente);
		 
		 ///hacemos el ordenamiento x especialidad
		 Collections.sort(forma.getListaProgramasServicios());
		 return mapping.findForward("paginaPrincipal");
	}
	
//	/**
//	 * 
//	 * @param forma
//	 */
//	private void calcularCantidadesXNumeroSuperficies(PresupuestoOdontologicoForm forma) 
//	{
//		for(DtoPresuOdoProgServ dto: forma.getListaProgramasServicios())
//		{
//			logger.info("\n\n\n\n\n CANTIDAD--->"+dto.getCantidad()+" dto.getTipoModificacion()-->"+dto.getTipoModificacion()+"\n\n\n\n\n\n\n\n\n\n\n");
//			
//			if(dto.getTipoModificacion()==EnumTipoModificacion.NUEVO
//					 || dto.getTipoModificacion()==EnumTipoModificacion.MODIFICADO)
//			{	
//				dto.setCantidad(dto.calcularCantidad(forma.getUtilizaPrograma()));
//			}
//		}
//	}

//	/**
//	 * Metodo para calcular las cantidades x numero de superficies 
//	 * @param forma
//	 */
//	private void calcularCantidadesXNumeroSuperficiesModificadas(PresupuestoOdontologicoForm forma) 
//	{
//		for(DtoPresuOdoProgServ dto: forma.getListaProgramasServicios())
//		{
//			int cantidad= dto.getCantidad()>0?dto.getCantidad():0;
//			logger.info("\n\n\n\n\n CANTIDAD--->"+dto.getCantidad()+" dto.getTipoModificacion()-->"+dto.getTipoModificacion()+"\n\n\n\n\n\n\n\n\n\n\n");
//			
//			if(dto.getTipoModificacion()==EnumTipoModificacion.NUEVO
//					 || dto.getTipoModificacion()==EnumTipoModificacion.MODIFICADO)
//			{	
//				dto.setCantidad(dto.calcularCantidadModificada(forma.getUtilizaPrograma())+cantidad);
//			}	
//		}
//	}
	
	
   /**
    * Metodo para validar si puedo Precontratar un presupuesto
    * @param forma
    * @return
    */
   	private boolean puedoPrecontratar(PresupuestoOdontologicoForm forma, PersonaBasica paciente, UsuarioBasico usuario)
	{
   		if(!forma.getExisteIncumplimientoParaAplicarDcto() && forma.getDtoPresupuestoContratado().getTotalPresupuestoParaDescuento().doubleValue()>0)
   		{	
	   		//debemos calcular el dcto Odon
	   		forma.setExisteDctoOdontologico(ValidacionesPresupuesto.existeDescuentoOdontologico(usuario.getCodigoCentroAtencion(), UtilidadFecha.getFechaActual() , forma.getDtoPresupuestoContratado().getTotalPresupuestoParaDescuento()));
	   		return forma.isExisteDctoOdontologico();
   		}
   		return false;
	}

   	
   	/**
   	 * Metodo para Cargar los Precontratados del presupuesto
   	 * Este metodo sirve para cargar los presupuesto en estado precontratado.
   	 * la lista de presupuesto precontratado se cargar para mostrarla en un popup que solamente informativo
   	 * @param mapping
   	 * @param forma
   	 * @param usuario
   	 * @param paciente
   	 * @return
   	 */
   	private ActionForward accionCargarPrecontratados(ActionMapping mapping,
			PresupuestoOdontologicoForm forma, UsuarioBasico usuario,
			PersonaBasica paciente) throws IPSException 
   	{
   		forma.resetContratar();
   		////postulamos el motivo de anulacion del dcto
   		/*
   		 * TODO falta enviar el codigo
   		 */
   	
   		/*
   		 * Cargarmos presupuesto Seleccionado
   		 */
   		
   		forma.setDtoPresupuesto((DtoPresupuestoOdontologico)forma.getListaPresupuestos().get(forma.getPosArray()).clone());
   		forma.setCodigoMotivoAnulacionDctoSel(ValoresPorDefecto.getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologicoInt(usuario.getCodigoInstitucionInt()));
   		forma.setListaPresupuestosPrecontratados(PresupuestoOdontologico.cargarPresupuestosPresontratados(paciente.getCodigoIngreso(), forma.getDtoPresupuesto().getCodigoPK()));
   		if(forma.getListaPresupuestosPrecontratados().size()>0)
   		{   			
   			return mapping.findForward("cargarPrecontratados");
   		}
   		return accionContratar(mapping, forma, usuario, paciente);
	}
   	
   	
	/**
   	 * Metodo para Contratar el Presupuesto
   	 * @param mapping
   	 * @param forma
   	 * @param usuario
   	 * @return
   	 */
   	private ActionForward accionContratar(	ActionMapping mapping,
   											PresupuestoOdontologicoForm forma, 
   											UsuarioBasico usuario , PersonaBasica paciente) throws IPSException
   	{
   		//1. CLONAMOS EL ENCABEZADO CON EL DETALLE SELECCIONADO
		forma.setDtoPresupuesto((DtoPresupuestoOdontologico)forma.getListaPresupuestos().get(forma.getPosArray()).clone());
		
	
	
		//
		
		
		//2. SE CARGAN LOS PROGRAMAS/SERVICIOS
		forma.setListaProgramasServicios(PresupuestoOdontologico.cargarPresupuestoOdoProgServ(forma.getDtoPresupuesto().getCodigoPK()));
		
		for(DtoPresuOdoProgServ programa : forma.getListaProgramasServicios())
		{	
			programa.setContratado(ConstantesBD.acronimoNo);
		}
		
		//3. SE CARGAN LOS CONVENIOS/PIEZAS DEL PROGRAMA/SERVICIO		
		this.cargarConveniosPiezasXProgramaServicio(forma, false);
		
		//4. CALCULAMOS NUEVAMENTE LAS TARIFAS
		this.calcularTarifasPendientes(forma, usuario, paciente);
		
		//5 RECALCULAMOS LOS BONOS Y LAS PROMOCIONES
		this.recalcularBonosPromociones(forma, usuario);
				
		//6. SE CARGAN LOS TOTALES
		forma.setListaSumatoriaConvenios(PresupuestoOdontologico.cargarTotalesPresupuetoConvenio(forma.getDtoPresupuesto().getCodigoPK()));
		
		DtoPresupuestoTotalesContratadoPrecontratado contratado = new DtoPresupuestoTotalesContratadoPrecontratado();
		contratado.setDescuento(new BigDecimal(0));
		logger.info("TOTAL X PRESUPUETO -----------__>"+ forma.totalPorPresupuesto());
		contratado.setTotalPresupuesto(forma.totalPorPresupuesto());
		logger.info("TOTAL X PRESUPUETO  DES-----------__>"+ forma.totalPorDescuentoPresupuesto());
		contratado.setTotalPresupuestoParaDescuento(forma.totalPorDescuentoPresupuesto());
		forma.setDtoPresupuestoContratado(contratado);
		
		//7. CUANDO SEA PRECONTRATADO CARGAMOS LA INFORMACION PERTINENTE PARA EL MANEJO DE LOS DESCUENTOS ODONTOLOGICOS
		cargarValidacionesDctosAlPrecontratar(forma, usuario);
		
		//8. GENERAMOS LAS ADVERTENCIAS DE LOS CONTRATOS VENCIDOS
		warningsContratosVencidos(forma);		
		
		//9. VERIFICAMOS SI EXISTE INCUMPLIMIENTO X CONTRATO
		validarIncumplimientoXContrato(forma, paciente);	
		
		//cargamos si el convenio existe en la cuenta
		
		ArrayList<DtoPresupuestoTotalConvenio> listaConveniosContratar=new ArrayList<DtoPresupuestoTotalConvenio>();
		
		boolean permitirContratar=true;
				
		String convenios="";
		
		for(DtoPresupuestoTotalConvenio dtoTotal: forma.getListaSumatoriaConvenios())
		{
			if(UtilidadTexto.getBoolean(forma.getEsContratar()))
			{
				dtoTotal.setExisteConvenioEnIngreso(ValidacionesPresupuesto.existeConvenioEnIngreso(paciente.getCodigoIngreso(), dtoTotal.getConvenio().getCodigo()));
				if(dtoTotal.getExisteConvenioEnIngreso() && dtoTotal.isEsConvenioTarjetaCliente())
				{
					dtoTotal.setTarjetaActiva(ValidacionesPresupuesto.pacienteTienetarjetaActiva(paciente.getCodigoPersona()));
				}
				if(forma.getDtoPresupuesto().getEstado().equals(ConstantesIntegridadDominio.acronimoPrecontratado))
				{
					for(DtoPresuOdoProgServ prog:forma.getListaProgramasServicios())
					{
						for(DtoPresupuestoOdoConvenio presupuestoOdoConvenio:prog.getListPresupuestoOdoConvenio())
						{
							boolean programaContratado=UtilidadTexto.getBoolean(presupuestoOdoConvenio.getContratado());
							if(programaContratado && presupuestoOdoConvenio.getConvenio().getCodigo()==dtoTotal.getConvenio().getCodigo())
							{
								if(!listaConveniosContratar.contains(dtoTotal))
								{
									listaConveniosContratar.add(dtoTotal);
									if(!dtoTotal.getExisteConvenioEnIngreso())
									{
										if(UtilidadTexto.isEmpty(convenios))
										{
											convenios=dtoTotal.getConvenio().getNombre();
										}
										else
										{
											convenios+=", "+dtoTotal.getConvenio().getNombre();
										}
										permitirContratar=false;
									}
								}
							}
						}
					}
				}
			}
			else
			{
				if(dtoTotal.isEsConvenioTarjetaCliente())
				{
					dtoTotal.setTarjetaActiva(ValidacionesPresupuesto.pacienteTienetarjetaActiva(paciente.getCodigoPersona()));
				}
				
				dtoTotal.setExisteConvenioEnIngreso(true);
			}
		}
		
		forma.setPermitirContratar(permitirContratar);
		if(!permitirContratar)
		{
			MessageResources mensajes=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.PresupuestoOdontologicoForm");
			forma.setMensajeInformativoConveniosNoRelacionados(mensajes.getMessage("PresupuestoOdontologicoForm.mensaje.conveniosNoRelacionados", convenios));
		}
		if(forma.getDtoPresupuesto().getEstado().equals(ConstantesIntegridadDominio.acronimoPrecontratado))
		{
			forma.setListaSumatoriaConvenios(listaConveniosContratar);
		}
		
		return mapping.findForward("contratarPresupuesto");
	}

	/**
	 * Metodo para graficar si existe incumplimiento x contrato
	 * @param forma
	 * @param paciente
	 */
	private void validarIncumplimientoXContrato(PresupuestoOdontologicoForm forma, PersonaBasica paciente) 
	{
		forma.setExisteIncumplimientoParaAplicarDcto(ValidacionesPresupuesto.existeCancelacionContratoXIncumplimiento(forma.getDtoPresupuesto().getPlanTratamiento(), paciente.getCodigoIngreso()));
		if(forma.getExisteIncumplimientoParaAplicarDcto())
		{	
			forma.getWarningsContratosVencidos().add("NO SE PUEDE SOLICITAR UN DESCUENTO X ATENCION O PRESUPUESTO PORQUE EL PACIENTE PRESENTA UN INCUMPLIMIENTO.");
		}
	}

	/**
	 * Validacion Autorizaciones de Descuento al Precontratar el presupuesto
	 * @param forma
	 * @param usuario
	 */
	private void cargarValidacionesDctosAlPrecontratar(	PresupuestoOdontologicoForm forma, UsuarioBasico usuario) 
	{
		if(forma.getDtoPresupuesto().getEstado().equals(ConstantesIntegridadDominio.acronimoPrecontratado))
		{
			forma.setDeseoPrecontratar(ConstantesBD.acronimoNo);
			//cargamos de bd los totales contratados/precontratados
			DtoPresupuestoTotalesContratadoPrecontratado dtoTotales= PresupuestoOdontologico.obtenerTotalesContratadoPrecontratado(forma.getDtoPresupuesto().getCodigoPK());
			
			//verificamos si el valor total para dcto ha cambiado
			////si son iguales
			if(dtoTotales.getTotalPresupuestoParaDescuento().doubleValue()==forma.getDtoPresupuestoContratado().getTotalPresupuestoParaDescuento().doubleValue())
			{
				forma.setDtoPresupuestoContratado(dtoTotales);
				
				forma.getDtoPresupuestoContratado().setExisteCambioTotalPresupuestoParaDcto(false);
				forma.getDtoPresupuestoContratado().setGenerarNuevaSolicitudDescuento(ConstantesBD.acronimoNo);
				
				//NO SE VUELVE A PREGUNTAR POR UNA NUEVA AUTORIZACION DE DCTO
				if(dtoTotales.getEstadoDescuento().equals(ConstantesIntegridadDominio.acronimoAutorizado))
				{
					//SE DEBE ACTUALIZAR A CONTRATADO
					forma.getDtoPresupuestoContratado().setEstadoDescuentoNuevo(ConstantesIntegridadDominio.acronimoContratado1);
				}
				else if(dtoTotales.getEstadoDescuento().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado) || dtoTotales.getEstadoDescuento().equals(ConstantesIntegridadDominio.acronimoNoAautorizado))
				{
					String nomEstado= (dtoTotales.getEstadoDescuento().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado))? "Anulada":"No Autorizada";
					forma.getDtoPresupuestoContratado().setMensaje("La solicitud de Autorizaciï¿½n de descuento para el presupuesto ha sido "+nomEstado+", Desea Contratar?.");
				}
				else if(dtoTotales.getEstadoDescuento().equals(ConstantesIntegridadDominio.acronimoXDefinir) || dtoTotales.getEstadoDescuento().equals(ConstantesIntegridadDominio.acronimoPendientePorAutorizar))
				{
					String nomEstado= (dtoTotales.getEstadoDescuento().equals(ConstantesIntegridadDominio.acronimoXDefinir))? "Por Definir":"Por Autorizar";
					forma.getDtoPresupuestoContratado().setMensaje("El presupuesto tiene asociada una solicitud de autorización de descuento en estado "+nomEstado+", Desea contratar el presupuesto sin descuento?.");
					
					//ANULAR LA SOLICITUD
					forma.getDtoPresupuestoContratado().setEstadoDescuentoNuevo(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado);
					////postulamos el motivo de anulacion del dcto
			   		forma.setCodigoMotivoAnulacionDctoSel(ValoresPorDefecto.getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologicoInt(usuario.getCodigoInstitucionInt()));
					cargarMotivosAnulacionDcto(forma, usuario);
				}
			}
			////EN EL CASO DE QUE CAMBIE
			else
			{
				logger.info("7");
				forma.getDtoPresupuestoContratado().setCodigoPkDcto(dtoTotales.getCodigoPkDcto());
				forma.getDtoPresupuestoContratado().setDescuento(dtoTotales.getDescuento());
				forma.getDtoPresupuestoContratado().setDescuentoNoAutorizadoNoContratado(dtoTotales.getDescuentoNoAutorizadoNoContratado());
				forma.getDtoPresupuestoContratado().setEstadoDescuento(dtoTotales.getEstadoDescuento());
				forma.getDtoPresupuestoContratado().setExisteCambioTotalPresupuestoParaDcto(true);
				forma.getDtoPresupuestoContratado().setGenerarNuevaSolicitudDescuento("");
				cargarMotivosAnulacionDcto(forma, usuario);
				
				if(dtoTotales.getEstadoDescuento().equals(ConstantesIntegridadDominio.acronimoAutorizado) || dtoTotales.getEstadoDescuento().equals(ConstantesIntegridadDominio.acronimoXDefinir) || dtoTotales.getEstadoDescuento().equals(ConstantesIntegridadDominio.acronimoPendientePorAutorizar))
				{
					logger.info("4");
					//SE DEBE ACTUALIZAR A ANULADO
					forma.getDtoPresupuestoContratado().setEstadoDescuentoNuevo(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado);
					forma.getDtoPresupuestoContratado().setMensaje("El total de presupuesto para dcto ha cambiado, Desea Generar una nueva solicitud de dcto?.");
				}
				else if(dtoTotales.getEstadoDescuento().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado) || dtoTotales.getEstadoDescuento().equals(ConstantesIntegridadDominio.acronimoNoAautorizado))
				{
					logger.info("5");
					String nomEstado= (dtoTotales.getEstadoDescuento().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado))? "Anulada":"No Autorizada";
					forma.getDtoPresupuestoContratado().setMensaje("El total de presupuesto para dcto ha cambiado y la solicitud de Autorizaciï¿½n de descuento anterior para el presupuesto ha sido "+nomEstado+", Desea Generar una nueva solicitud de dcto?.");
				}
			}
		}
	}

	/**
	 * Metodo para validar los contratos vencidos 
	 * @param forma
	 */
	private void warningsContratosVencidos(PresupuestoOdontologicoForm forma)
	{
		Vector contratos= new Vector();
		for(DtoPresupuestoTotalConvenio dtoTotal : forma.getListaSumatoriaConvenios())
		{
			contratos.add(dtoTotal.getConvenio().getCodigo());
		}
		HashMap contratosVencidosMap= Contrato.obtenerContratosVencidos(contratos);
		for(int a=0; a<Utilidades.convertirAEntero(contratosVencidosMap.get("numRegistros")+""); a++)
		{
			forma.getWarningsContratosVencidos().add("EL CONTRATO DEL CONVENIO  "+contratosVencidosMap.get("nombreconvenio_"+a)+" VENCIDO. Fecha Inicial "+contratosVencidosMap.get("fechainicial_"+a)+" Fecha Final "+contratosVencidosMap.get("fechafinal_"+a));
		}
	}
   	
	
	
	/**
	 * 
	 * Método que se encarga de guardar la información del presupuesto
	 * cuando se contrata, se precontrata o se precontrata directamente.
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param preContratar
	 * @return
	 */
	private ActionForward accionGuardarContratadoPreContratadoYDirecto(ActionMapping mapping,
														PresupuestoOdontologicoForm forma, 
														UsuarioBasico usuario,
														PersonaBasica paciente, 
														HttpServletRequest request){
		
		
		boolean preContratar = forma.isPrecontratar();
		
//		if(forma.getEstado().equals("guardarContratado") || forma.getEstado().equals("guardarPrecontratado")){
//			
//			//1. VALIDO SI DESEO PRECONTRATATAR
//			if(forma.getDeseoPrecontratar().equals("") && puedoPrecontratar(forma, paciente, usuario))
//			{
//				forma.setEstado("popupPreguntaPrecontratar");
//				return mapping.findForward("contratarPresupuesto");
//			}
//		}

		//2. INICIAMOS LA TRANSACCION
		Connection con= UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		  
		//3.ACTUALIZAMOS LOS CONVENIOS QUE NO TENIAN TARIFAS PERO QUE AHORA SI LA TIENEN
		ActionForward forward= modificarTarifasPendientesConveniosPresupuesto(mapping, forma, usuario, request, con);
		if(forward!=null)
		{
			return forward;
		}
		   
		//4. LLENAMOS EL DTO LOG DE PRESUPUESTO 
		DtoLogPresupuestoOdontologico dtoLog = llenarDtoLog(forma);

		//5. MODIFICAMOS EL PRESUPUESTO A CONTRATADO O PRECONTRATADO
	  	actualizarEstadoFHUPresupuesto(forma, usuario, preContratar);
	  	if(!PresupuestoOdontologico.modificarPresupuesto(forma.getDtoPresupuesto() , con))
	  	{
	  		logger.error("NO Presupuesto");
	  		UtilidadBD.abortarTransaccion(con);
	  		UtilidadBD.closeConnection(con);

			return salirEliminarCitaProgramada(mapping, forma, request, con);
	   	}
		   
	   	for(DtoPresuOdoProgServ dtoProSer :forma.getListaProgramasServicios())
	   	{
   			for(DtoPresupuestoOdoConvenio dtoConvenio : dtoProSer.getListPresupuestoOdoConvenio())
		   	{
			   	if(dtoConvenio.getContratado().equals(ConstantesBD.acronimoSi))
			   	{
				   	//debemos liberar los bonos que no se usaron en el momento de CONTRATAR
		   			forward=marcarBonosUsadosContrato(paciente, preContratar, con, dtoConvenio, dtoProSer.getPrograma().getCodigo().intValue());
		   			if(forward!=null)
				  	{
					   	return forward;
				   	}

				   	//debemos actualizar el indicativo de si utiliza reserva de anticipo
				   	dtoConvenio.setReservaAnticipo(forma.getUtilizaReservaAnticipo(dtoConvenio.getContrato().getCodigo()));
				   	if(!dtoConvenio.getSeleccionadoBono())
				   	{
					   	dtoConvenio.setValorDescuentoBono(new BigDecimal(0));
				   	}
				   	if(!dtoConvenio.getSeleccionadoPromocion())
			   		{
					   	dtoConvenio.setValorDescuentoPromocion(new BigDecimal(0));
				   	}
				   	if(UtilidadTexto.isEmpty(dtoConvenio.getErrorCalculoTarifa()))
				   	{
					   	if(!PresupuestoOdontologico.modificarPresupuestoConvenio(dtoConvenio , con))
					   	{
						   	logger.error("NO INSERTA CONVENIO");
						   	UtilidadBD.abortarTransaccion(con);
						   	UtilidadBD.closeConnection(con);

							return salirEliminarCitaProgramada(mapping, forma,
									request, con);
					   	}
				   	}
			   	}
		   	}
	   	}
	   	
	   	logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n****************PRECONTRATAR :"+preContratar+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n****************");
	   	for(DtoPresuOdoProgServ programa : forma.getListaProgramasServicios())
	   	{
	   		for (DtoPresupuestoPiezas pieza : programa.getListPresupuestoPiezas())
	   		{
	   			logger.info("111");
	   			boolean utilizaProgramas = (forma.getTipoRelacion().equals("Programa"));
	   			BigDecimal codigoProgramaServicio= (utilizaProgramas)? new BigDecimal(programa.getPrograma().getCodigo()): new BigDecimal(programa.getServicio().getCodigo()); 
	   			int codigoDetallePlanT = PresupuestoOdontologico.existeProgramaServicioPresupuestoEnPlanTratamiento(pieza.getPieza().intValue(),pieza.getSuperficie().intValue(),pieza.getHallazgo().intValue(),  codigoProgramaServicio ,utilizaProgramas , forma.getDtoPlanTratamiento().getCodigoPk(), ""); 
	   			
	   			//BigDecimal codigoDetPlanTratamiento= PlanTratamiento.obtenerDetPlanTratamiento(forma.getDtoPresupuesto().getPlanTratamiento(), pieza.getPieza(), pieza.getHallazgo(), pieza.getSuperficie(), pieza.getSeccion());
	   			
	   			if(codigoDetallePlanT <= 0)
	   			{
	   				logger.info("---222");
	   				if(!preContratar)
	   				{
	   					
	   					forward=insertarNuevoProgramaServicoPlanTratamientoInicial(mapping,forma, usuario, request, con, programa, pieza, new BigDecimal(codigoDetallePlanT), paciente);
	   					if(forward!=null)
	   					{
	   						return forward;
	   					}
	   				}
	   			}
	   			else
	   			{
	   				logger.info("---333");
	   				///////////OJO SUPER IMPORTANTE, NO SE ACTUALIZA EL ESTADO DE LOS PROGRAMAS/SERVICIOS PLAN T CUANDO ES PRECONTRATAR
	   				if(!preContratar)
	   				{
	   					logger.info("Programa Contratado >> "+programa.getContratado());
	   					if(!programa.getContratado().equals(ConstantesBD.acronimoNo))
	   					{
	   						forward=actualizarAEstadoContratadoProgramaServicioPlanTtoInicial(mapping, usuario, request, con, programa,utilizaProgramas, new BigDecimal(codigoDetallePlanT), forma.getCodigoProximaCitaRegistrada());
	   						if(forward!=null)
	   						{
	   							return forward;
	   						}
	   					}
	   				}	
	   			}
				   //Cambiar a estado COT Serv Y PRogramas del PT
	   		}
	   	}
		   
		   //6. ACTUALIZAMOS LOS BONOS Y LAS PROMOCIONES
	   	forward= modificarBonosPromocionesPresupuesto(mapping, forma, usuario, request, con);
		  
	   	if(forward!=null)
	   	{
	   		return forward;
	   	}
		   
	  /* 	if(PresupuestoOdontologico.guardarLogPresupuesto(dtoLog , con) <= 0)
	   	{
	   		logger.error("NO log Presupuesto");
	   		UtilidadBD.abortarTransaccion(con);
	   		UtilidadBD.closeConnection(con);

			return salirEliminarCitaProgramada(mapping, forma, request, con);
	   	}*/

	   	/////ahora insertamos el valor de anticipo que se llena desde las validaciones de la forma
	   	if(!preContratar)
	   	{
	   		logger.info("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyYYYYTAMANIO LISTA ANTICIPOS="+ forma.getListaAnticiposPresupuesto());
	   		
	   		for(DtoValorAnticipoPresupuesto dtoAnticipo: forma.getListaAnticiposPresupuesto())
	   		{
	   			if(!Contrato.modificarValorAnticipoReservadoPresupuesto(con, dtoAnticipo.getContrato(), dtoAnticipo.getValorAnticipo()))
	   			{
	   				logger.error("NO valor anticipos reservados presupuesto Presupuesto");
	   				UtilidadBD.abortarTransaccion(con);
	   				UtilidadBD.closeConnection(con);

	   				return salirEliminarCitaProgramada(mapping, forma, request,
							con);
	   			}
	   		}
	   	}

	   	logger.info("modifica  100%%%");
		   
	   	if(preContratar && forma.getEstado().equals("guardarPrecontratado"))
	   	{
			  //2. SE INSERTA LA SOLICITUD DE AUTORIZACION DE DCTO ODON
	   		forward= insertarNuevaSolicitudDctoOdo(mapping, forma, usuario, request, con);
	   		if(forward!=null)
		   	{
	   			return forward;
		   	}
			  
	   	}else{
			  
	   		//////////hacemos las actualizaciones de los dctos 
	   		if(dtoLog.getEstado().equals(ConstantesIntegridadDominio.acronimoPrecontratado) && forma.getDtoPresupuestoContratado().getCodigoPkDcto().doubleValue()>0)
	   		{
	   			if(!UtilidadTexto.isEmpty(forma.getDtoPresupuestoContratado().getEstadoDescuentoNuevo()))
	   			{
	   				//if es anular entonces debemos insertar el motivo
	   				if(forma.getDtoPresupuestoContratado().getEstadoDescuentoNuevo().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado))
	   				{
	   					if(!PresupuestoOdontologico.anularDescuentoPresupuesto(con, forma.getDtoPresupuesto().getCodigoPK(), usuario.getLoginUsuario(), forma.getCodigoMotivoAnulacionDctoSel()))
	   					{
	   						logger.error("NO Presupuesto");
	   						UtilidadBD.abortarTransaccion(con);
	   						UtilidadBD.closeConnection(con);

	   						return salirEliminarCitaProgramada(mapping, forma,
									request, con);
	   					}
						  
	   				}else{
						  
	   					if(!PresupuestoOdontologico.cambiarEstadoDescuentoPresupuesto(con, new DtoInfoFechaUsuario(usuario.getLoginUsuario()), forma.getDtoPresupuestoContratado().getEstadoDescuentoNuevo(), forma.getDtoPresupuestoContratado().getCodigoPkDcto()))
	   					{
	   						logger.error("NO cambia el estado del descuento del presupuesto");
	   						UtilidadBD.abortarTransaccion(con);
	   						UtilidadBD.closeConnection(con);

	   						return salirEliminarCitaProgramada(mapping, forma,
									request, con);
	   					}
	   				}	
	   			}
	   		}

	   		///hacemos las actualizaciones de los otros presupeusto precontratados, 
	   		if(!PresupuestoOdontologico.anularDescuentoPresupuestoPrecontratadoYCambioEstadoActivo(con,forma.getListaPresupuestosPrecontratados(), usuario.getLoginUsuario(), ValoresPorDefecto.getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologicoInt(usuario.getCodigoInstitucionInt())))
	   		{
	   			logger.error("NO actualiza los otros presupuesto en estado precontratado");
	   			UtilidadBD.abortarTransaccion(con);
	   			UtilidadBD.closeConnection(con);

				return salirEliminarCitaProgramada(mapping, forma, request, con);
	   		}
	   	}

	   	//INSERTAMOS EL CENTRO DE ATENCION DUENIO
	   	if(!preContratar)
	   	{
	   		if(!Paciente.actualizarCentroAtencionDuenioPaciente(con, paciente.getCodigoPersona(), usuario.getCodigoCentroAtencion()))
	   		{
	   			logger.error("NO actualiza los otros presupuesto en estado precontratado");
			 	UtilidadBD.abortarTransaccion(con);
			 	UtilidadBD.closeConnection(con);
			 
				return salirEliminarCitaProgramada(mapping, forma, request, con);
	   		}
	   	}
		   
	   	//INSERTAMOS EN LA TABLA presupuesto_contratado CUANDO PRECONTRATO O CONTRATO
	   	if(!PresupuestoContratado.insertarPresupuestoContratadoPrecontratadoYCuotas(con, forma.getDtoPresupuesto().getCodigoPK(), usuario , preContratar))
	   	{
	   		logger.error("NO inserta presupuesto contratado-precontratado");
	   		UtilidadBD.abortarTransaccion(con);
	   		UtilidadBD.closeConnection(con);
	   		
			return salirEliminarCitaProgramada(mapping, forma, request, con);
	   	}
		   
		 
	   	UtilidadBD.finalizarTransaccion(con);
	   	UtilidadBD.closeConnection(con);

	   	request.getSession().removeAttribute("DetallesAsociadosCita");
	   	
	   	forma.setEstado("contratadoExitoso");
	   	//forma.setEsContratar(ConstantesBD.acronimoNo);
	   	llenarListaOpcionesImprimir(forma, usuario, request);
	   	
	   	return mapping.findForward("contratarPresupuesto");
	   	
	   	

//	     //////////hacemos las actualizaciones de los dctos 
//	   if(dtoLog.getEstado().equals(ConstantesIntegridadDominio.acronimoPrecontratado) && forma.getDtoPresupuestoContratado().getCodigoPkDcto().doubleValue()>0)
//	   {
//		   if(!UtilidadTexto.isEmpty(forma.getDtoPresupuestoContratado().getEstadoDescuentoNuevo()))
//		   {
//			   ///if es anular entonces debemos insertar el motivo
//			   if(forma.getDtoPresupuestoContratado().getEstadoDescuentoNuevo().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado))
//			   {	
//				   if(!PresupuestoOdontologico.anularDescuentoPresupuesto(con, forma.getDtoPresupuesto().getCodigoPK(), usuario.getLoginUsuario(), forma.getCodigoMotivoAnulacionDctoSel()))
//				   {
//					   logger.error("NO Presupuesto");
//					   UtilidadBD.abortarTransaccion(con);
//					   UtilidadBD.closeConnection(con);
//					   return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
//				   }
//			   }
//			   else
//			   {	
//				   if(!PresupuestoOdontologico.cambiarEstadoDescuentoPresupuesto(con, new DtoInfoFechaUsuario(usuario.getLoginUsuario()), forma.getDtoPresupuestoContratado().getEstadoDescuentoNuevo(), forma.getDtoPresupuestoContratado().getCodigoPkDcto()))
//				   {
//					   logger.error("NO cambia el estado del descuento del presupuesto");
//					   UtilidadBD.abortarTransaccion(con);
//					   UtilidadBD.closeConnection(con);
//					   ////hacer error
//					   return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
//				   }
//			   }	
//		   }
//	  }
//
//	   ///hacemos las actualizaciones de los otros presupeusto precontratados, 
//	  if(!PresupuestoOdontologico.anularDescuentoPresupuestoPrecontratadoYCambioEstadoActivo(con,forma.getListaPresupuestosPrecontratados(), usuario.getLoginUsuario(), ValoresPorDefecto.getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologicoInt(usuario.getCodigoInstitucionInt())))
//	  {
//		  logger.error("NO actualiza los otros presupuesto en estado precontratado");
//		  UtilidadBD.abortarTransaccion(con);
//		  UtilidadBD.closeConnection(con);
//		  return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
//	  }
	   
	}

	
	
//   	/**
//	 * 
//	 * @param mapping
//	 * @param forma
//	 * @param usuario
//	 * @param paciente
//	 * @param request
//	 * @return
//	 * @throws SQLException 
//	 */
//   private ActionForward accionGuardarContratadoPrecontratado(	ActionMapping mapping,
//																PresupuestoOdontologicoForm forma, 
//																UsuarioBasico usuario,
//																PersonaBasica paciente, 
//																HttpServletRequest request, 
//																boolean preContratar) 
//   {
//	   ghfgh
//	   //1. VALIDO SI DESEO PRECONTRATATAR
//	   if(forma.getDeseoPrecontratar().equals("") && puedoPrecontratar(forma, paciente, usuario))
//	   {
//		   	forma.setEstado("popupPreguntaPrecontratar");
//			return mapping.findForward("contratarPresupuesto");
//	   }
//	   
//		
//	   
//	   //2. INICIAMOS LA TRANSACCION
//	   Connection con= UtilidadBD.abrirConexion();
//	   UtilidadBD.iniciarTransaccion(con);
//	   
//	   //3.ACTUALIZAMOS LOS CONVENIOS QUE NO TENIAN TARIFAS PERO QUE AHORA SI LA TIENEN
//	   ActionForward forward= modificarTarifasPendientesConveniosPresupuesto(mapping, forma, usuario, request, con);
//	   if(forward!=null)
//	   {
//		   return forward;
//	   }
//	   
//	   //4. LLENAMOS EL DTO LOG DE PRESUPUESTO 
//	   DtoLogPresupuestoOdontologico dtoLog = llenarDtoLog(forma);
//
//	   //5. MODIFICAMOS EL PRESUPUESTO A CONTRATADO O PRECONTRATADO
//	   actualizarEstadoFHUPresupuesto(forma, usuario, preContratar);
//	   if(!PresupuestoOdontologico.modificarPresupuesto(forma.getDtoPresupuesto() , con))
//	   {
//		   logger.error("NO Presupuesto");
//		   UtilidadBD.abortarTransaccion(con);
//		   UtilidadBD.closeConnection(con);
//		   return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
//	   }
//
//	   for(DtoPresuOdoProgServ dtoProSer :forma.getListaProgramasServicios())
//	   {
//		   for(DtoPresupuestoOdoConvenio dtoConvenio : dtoProSer.getListPresupuestoOdoConvenio())
//		   {
//			   if(dtoConvenio.getContratado().equals(ConstantesBD.acronimoSi))
//			   {
//				   //debemos liberar los bonos que no se usaron en el momento de CONTRATAR
//				   forward= liberarBonosNoUsadosContrato(mapping, paciente, request,	preContratar, con, dtoConvenio);
//				   if(forward!=null)
//				   {
//					   return forward;
//				   }
//
//				   //debemos actualizar el indicativo de si utiliza reserva de anticipo
//				   dtoConvenio.setReservaAnticipo(forma.getUtilizaReservaAnticipo(dtoConvenio.getContrato().getCodigo()));
//				   if(!dtoConvenio.getSeleccionadoBono())
//				   {
//					   dtoConvenio.setValorDescuentoBono(new BigDecimal(0));
//				   }
//				   if(!dtoConvenio.getSeleccionadoPromocion())
//				   {
//					   dtoConvenio.setValorDescuentoPromocion(new BigDecimal(0));
//				   }
//				   if(UtilidadTexto.isEmpty(dtoConvenio.getErrorCalculoTarifa()))
//				   {
//					   if(!PresupuestoOdontologico.modificarPresupuestoConvenio(dtoConvenio , con))
//					   {
//						   logger.error("NO INSERTA CONVENIO");
//						   UtilidadBD.abortarTransaccion(con);
//						   UtilidadBD.closeConnection(con);
//						   return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
//					   }
//				   }
//			   }
//		   }
//	   }
//
//	   logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n****************PRECONTRATAR :"+preContratar+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n****************");
//	   for(DtoPresuOdoProgServ programa : forma.getListaProgramasServicios())
//	   {
//		   for (DtoPresupuestoPiezas pieza : programa.getListPresupuestoPiezas())
//		   {
//			   logger.info("111");
//			   boolean utilizaProgramas = (forma.getTipoRelacion().equals("Programa"));
//			   BigDecimal codigoProgramaServicio= (utilizaProgramas)? new BigDecimal(programa.getPrograma().getCodigo()): new BigDecimal(programa.getServicio().getCodigo()); 
//			   int codigoDetallePlanT = PresupuestoOdontologico.existeProgramaServicioPresupuestoEnPlanTratamiento(pieza.getPieza().intValue(),pieza.getSuperficie().intValue(),pieza.getHallazgo().intValue(),  codigoProgramaServicio ,utilizaProgramas , forma.getDtoPlanTratamiento().getCodigoPk(), ""); 
//			   BigDecimal codigoDetPlanTratamiento= PlanTratamiento.obtenerDetPlanTratamiento(forma.getDtoPresupuesto().getPlanTratamiento(), pieza.getPieza(), pieza.getHallazgo(), pieza.getSuperficie(), pieza.getSeccion());
//			   
//			   if(codigoDetallePlanT <= 0)
//			   {
//				   logger.info("---222");
//				   if(!preContratar)
//				   {
//					   forward=insertarNuevoProgramaServicoPlanTratamientoInicial(mapping,forma, usuario, request, con, programa, pieza, codigoDetPlanTratamiento);
//					   if(forward!=null)
//					   {
//						   return forward;
//					   }
//				   }   
//			   }
//			   else
//			   {
//				   logger.info("---333");
//				   ///////////OJO SUPER IMPORTANTE, NO SE ACTUALIZA EL ESTADO DE LOS PROGRAMAS/SERVICIOS PLAN T CUANDO ES PRECONTRATAR
//				   if(!preContratar)
//				   {
//					   logger.info("Programa Contratado >> "+programa.getContratado());
//					   if(!programa.getContratado().equals(ConstantesBD.acronimoNo))
//					   {
//					       forward=actualizarAEstadoContratadoProgramaServicioPlanTtoInicial(mapping, usuario, request, con, programa,utilizaProgramas, codigoDetPlanTratamiento);
//							   if(forward!=null)
//							   {
//								   return forward;
//							   }
//					   }
//				   }	
//			   }
//			   //Cambiar a estado COT Serv Y PRogramas del PT
//		   }
//	   }
//	   
//	   //6. ACTUALIZAMOS LOS BONOS Y LAS PROMOCIONES
//		forward= modificarBonosPromocionesPresupuesto(mapping, forma, usuario, request, con);
//		if(forward!=null)
//		{
//			return forward;
//		}
//	   
//	   
//	   if(PresupuestoOdontologico.guardarLogPresupuesto(dtoLog , con) <= 0)
//	   {
//		   logger.error("NO log Presupuesto");
//		   UtilidadBD.abortarTransaccion(con);
//		   UtilidadBD.closeConnection(con);
//		   return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
//	   }
//
//	   /////ahora insertamos el valor de anticipo que se llena desde las validaciones de la forma
//	   if(!preContratar)
//	   {
//		   logger.info("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyYYYYTAMANIO LISTA ANTICIPOS="+ forma.getListaAnticiposPresupuesto());
//
//		   for(DtoValorAnticipoPresupuesto dtoAnticipo: forma.getListaAnticiposPresupuesto())
//		   {
//			   if(!Contrato.modificarValorAnticipoReservadoPresupuesto(con, dtoAnticipo.getContrato(), dtoAnticipo.getValorAnticipo()))
//			   {
//				   logger.error("NO valor anticipos reservados presupuesto Presupuesto");
//				   UtilidadBD.abortarTransaccion(con);
//				   UtilidadBD.closeConnection(con);
//				   ////hacer error
//				   return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
//			   }
//		   }
//	   }
//
//	   logger.info("modifica  100%%%");
//
//	   if(preContratar)
//	   {
//		   //2. SE INSERTA LA SOLICITUD DE AUTORIZACION DE DCTO ODON
//		   forward= insertarNuevaSolicitudDctoOdo(mapping, forma, usuario, request, con);
//		   if(forward!=null)
//		   {	
//			   return forward;
//		   }
//	   }
//	   else
//	   {
//		   //////////hacemos las actualizaciones de los dctos 
//		   if(dtoLog.getEstado().equals(ConstantesIntegridadDominio.acronimoPrecontratado) && forma.getDtoPresupuestoContratado().getCodigoPkDcto().doubleValue()>0)
//		   {
//			   if(!UtilidadTexto.isEmpty(forma.getDtoPresupuestoContratado().getEstadoDescuentoNuevo()))
//			   {
//				   ///if es anular entonces debemos insertar el motivo
//				   if(forma.getDtoPresupuestoContratado().getEstadoDescuentoNuevo().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado))
//				   {	
//					   if(!PresupuestoOdontologico.anularDescuentoPresupuesto(con, forma.getDtoPresupuesto().getCodigoPK(), usuario.getLoginUsuario(), forma.getCodigoMotivoAnulacionDctoSel()))
//					   {
//						   logger.error("NO Presupuesto");
//						   UtilidadBD.abortarTransaccion(con);
//						   UtilidadBD.closeConnection(con);
//						   return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
//					   }
//				   }
//				   else
//				   {	
//					   if(!PresupuestoOdontologico.cambiarEstadoDescuentoPresupuesto(con, new DtoInfoFechaUsuario(usuario.getLoginUsuario()), forma.getDtoPresupuestoContratado().getEstadoDescuentoNuevo(), forma.getDtoPresupuestoContratado().getCodigoPkDcto()))
//					   {
//						   logger.error("NO cambia el estado del descuento del presupuesto");
//						   UtilidadBD.abortarTransaccion(con);
//						   UtilidadBD.closeConnection(con);
//						   ////hacer error
//						   return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
//					   }
//				   }	
//			   }
//		   }
//
//		   ///hacemos las actualizaciones de los otros presupeusto precontratados, 
//		   if(!PresupuestoOdontologico.anularDescuentoPresupuestoPrecontratadoYCambioEstadoActivo(con,forma.getListaPresupuestosPrecontratados(), usuario.getLoginUsuario(), ValoresPorDefecto.getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologicoInt(usuario.getCodigoInstitucionInt())))
//		   {
//			   logger.error("NO actualiza los otros presupuesto en estado precontratado");
//			   UtilidadBD.abortarTransaccion(con);
//			   UtilidadBD.closeConnection(con);
//			   return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
//		   }
//	   }
//
//	   //INSERTAMOS EL CENTRO DE ATENCION DUENIO
//	   if(!preContratar)
//	   {
//		   if(!Paciente.actualizarCentroAtencionDuenioPaciente(con, paciente.getCodigoPersona(), usuario.getCodigoCentroAtencion()))
//		   {	
//			   logger.error("NO actualiza los otros presupuesto en estado precontratado");
//			   UtilidadBD.abortarTransaccion(con);
//			   UtilidadBD.closeConnection(con);
//			   return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
//		   }
//	   }
//	   
//	   //INSERTAMOS EN LA TABLA presupuesto_contratado CUANDO PRECONTRATO O CONTRATO
//	   if(!PresupuestoContratado.insertarPresupuestoContratadoPrecontratadoYCuotas(con, forma.getDtoPresupuesto().getCodigoPK(), usuario , preContratar))
//	   {
//		   logger.error("NO inserta presupuesto contratado-precontratado");
//		   UtilidadBD.abortarTransaccion(con);
//		   UtilidadBD.closeConnection(con);
//		   return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
//	   }
//	   
//	   UtilidadBD.finalizarTransaccion(con);
//	   UtilidadBD.closeConnection(con);
//
//	   if(preContratar || !UtilidadTexto.getBoolean(ValoresPorDefecto.getEsRequeridoProgramarCitaAlContratarPresupuestoOdon(usuario.getCodigoInstitucionInt())))
//	   {	
//		   forma.setEstado("contratadoExitoso");
//		   return mapping.findForward("contratarPresupuesto");
//	   
//	   }else{
//		   
//		   /*
//		    * Es requerido programar la próxima cita
//		    */
//		   
//		   return mapping.findForward(programarProximaCita(forma, paciente, request));
//		   
////		   forma.setEstado("proximaCita");
////		   return accionProximaCita(mapping, forma, usuario);
//	   }
//	}

	/**
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param con
	 * @param programa
	 * @param utilizaProgramas
	 * @param codigoDetallePlan
	 * @param codigoProximaCitaRegistrada 
	 */
	private ActionForward actualizarAEstadoContratadoProgramaServicioPlanTtoInicial(	ActionMapping mapping, UsuarioBasico usuario,
																			HttpServletRequest request, Connection con,
																			DtoPresuOdoProgServ programa, boolean utilizaProgramas,
																			BigDecimal codigoDetallePlan, int codigoProximaCitaRegistrada) 
	{
		DtoProgramasServiciosPlanT newPrograma = new DtoProgramasServiciosPlanT();											 		          
		newPrograma.setDetPlanTratamiento(codigoDetallePlan);
		if(utilizaProgramas)
		{	
			newPrograma.setPrograma(programa.getPrograma());
		}	
		else
		{	
			newPrograma.setServicio(programa.getServicio());
		}	
		newPrograma.setEstadoPrograma(ConstantesIntegridadDominio.acronimoContratado);
		newPrograma.setEstadoServicio(ConstantesIntegridadDominio.acronimoContratado);	
		newPrograma.setPorConfirmado(ConstantesBD.acronimoNo);
		newPrograma.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		newPrograma.setActivo(ConstantesBD.acronimoSi);
		
		if (!PlanTratamiento.modicarEstadosDetalleProgServ(newPrograma , con))
		{
			logger.error("NO PASA POR MDIFICAR SOLO CONTRATADOS");
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			////hacer error
	   		
			IProximaCitaProgramadaServicio proximaCita=OdontologiaServicioFabrica.crearProximaCitaProgramadaServicio();
			proximaCita.eliminarRegistroProximaCita(codigoProximaCitaRegistrada);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
		}
		return null;
	}

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param con
	 * @param programa
	 * @param pieza
	 * @param detPlanTratamiento 
	 * @param paciente 
	 */
	private ActionForward insertarNuevoProgramaServicoPlanTratamientoInicial(	ActionMapping mapping, 
																		PresupuestoOdontologicoForm forma,
																		UsuarioBasico usuario, 
																		HttpServletRequest request, 
																		Connection con,
																		DtoPresuOdoProgServ programa, 
																		DtoPresupuestoPiezas pieza, 
																		BigDecimal detPlanTratamiento, PersonaBasica paciente) 
	{
		logger.info("LO DEBO INSERTAR --------------------->");
		
		
		/*
		 * Se obtienen los servicios asociados al programa
		 */
//		String codigoTarifarioServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
//		ArrayList<InfoServicios> servicios = cargarServiciosPorProgramaOdontologico (programa.getPrograma().getCodigo(), codigoTarifarioServicios);
//			
		String tmpBusquedaServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
		ArrayList<DtoDetalleProgramas> arrayServicios = new ArrayList<DtoDetalleProgramas>();
		arrayServicios = ProgramasOdontologicos.cargarDetallePrograma(programa.getPrograma().getCodigo(),tmpBusquedaServicios);
		
		int orderServicio=PlanTratamiento.cargarOrdenServicio(programa.getPrograma().getCodigo().intValue(), detPlanTratamiento ,con, false);

	 
		ArrayList<DtoDetalleProximaCita> detallesAsociadosCita = (ArrayList<DtoDetalleProximaCita>) request.getSession().getAttribute("DetallesAsociadosCita");
		
		logger.info("EL CODIGO DEL ORDEN" + orderServicio );
		if(orderServicio <= 0)
		{
			logger.error("NO orden detalle");
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);

			return salirEliminarCitaProgramada(mapping, forma, request, con);
		}
		if(arrayServicios.size() > 0)
		{
			for(DtoDetalleProgramas detProSer : arrayServicios)
			{
				DtoProgramasServiciosPlanT newPrograma = new DtoProgramasServiciosPlanT();
				newPrograma.setDetPlanTratamiento(detPlanTratamiento);
				newPrograma.setActivo(ConstantesBD.acronimoSi);
				newPrograma.setPrograma( programa.getPrograma());
				newPrograma.setServicio(new InfoDatosInt(detProSer.getServicio(), ""));
				newPrograma.setPorConfirmado(ConstantesBD.acronimoNo);
				newPrograma.setEstadoPrograma(ConstantesIntegridadDominio.acronimoContratado);
				newPrograma.setEstadoServicio(ConstantesIntegridadDominio.acronimoContratado);
				newPrograma.setIndicativoPrograma(ConstantesIntegridadDominio.acronimoPresupuesto);
				newPrograma.setIndicativoServicio(ConstantesIntegridadDominio.acronimoPresupuesto);
				newPrograma.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
				newPrograma.setOrdenServicio(orderServicio);
				
				logger.info("						GUARDAR PROGRAMA SERVICIO			        ");
				double codigoPkProgramaServPlant =PlanTratamiento.guardarProgramasServicio(con,newPrograma);
				logger.info("EL CODIGO PROGRAMA" + codigoPkProgramaServPlant ); 
				if(codigoPkProgramaServPlant <= 0)
				{
					logger.error("NO PASA POR PROGRAMAS DEL PLAN");
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);

					return salirEliminarCitaProgramada(mapping, forma, request,
							con);
				}
				
				//Actulizamos en el plan tto del presupuesto el campo 
				if(!PresupuestoOdontologico.actualizarPlanTtoPresupuestoProgServ(con, new BigDecimal(codigoPkProgramaServPlant), detPlanTratamiento, programa.getPrograma(), detProSer.getServicio(), forma.getDtoPresupuesto().getCodigoPK()))
				{
					logger.error("NO PASA POR PROGRAMAS DEL PLAN");
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);

					return salirEliminarCitaProgramada(mapping, forma, request,
							con);
				}
				
				
				//DEBEMOS CANCELAR LOS EQUIVALENTES NO CONTRATADOS
				DtoProgramasServiciosPlanT dtoFiltro = new DtoProgramasServiciosPlanT();
				dtoFiltro.setDetPlanTratamiento(detPlanTratamiento);
				dtoFiltro.setActivo(ConstantesBD.acronimoSi);
				
				ArrayList<DtoProgramasServiciosPlanT> listaProgramas= PlanTratamiento.cargarProgramasServiciosPlanT(dtoFiltro);
				
				for(DtoProgramasServiciosPlanT dtoProgServ: listaProgramas)
				{
					if(dtoProgServ.getEstadoPrograma().equals(ConstantesIntegridadDominio.acronimoEstadoEnProceso)
							|| dtoProgServ.getEstadoPrograma().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
					{
						if(DetalleHallazgoProgramaServicio.programasServiciosEquivalentes(new BigDecimal(programa.getPrograma().getCodigo()), new BigDecimal(dtoProgServ.getPrograma().getCodigo()), pieza.getHallazgo().doubleValue(), true))
						{
							if(programa.getPrograma().getCodigo().doubleValue()!=dtoProgServ.getPrograma().getCodigo().doubleValue())
							{	
								DtoProgramasServiciosPlanT dtoProgramaServActualiza= new DtoProgramasServiciosPlanT();
								dtoProgramaServActualiza.setCodigoPk(dtoProgServ.getCodigoPk());
								dtoProgramaServActualiza.setEstadoPrograma(ConstantesIntegridadDominio.acronimoEstadoCancelado);
								dtoProgramaServActualiza.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoCancelado);
								dtoProgramaServActualiza.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
								dtoProgramaServActualiza.setPorConfirmado(ConstantesBD.acronimoNo);
								dtoProgramaServActualiza.setActivo(ConstantesBD.acronimoNo);
								if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoProgramaServActualiza, con))
								{
									logger.error("NO CANCELA EQUIVALENTES NO CONTRATADOS");
									UtilidadBD.abortarTransaccion(con);
									UtilidadBD.closeConnection(con);

									return salirEliminarCitaProgramada(mapping,
											forma, request, con);
								}
							}	
						}
					}
				}
				
				
				orderServicio++;
			}
			
			
			//insertamos los programas hallazgos piezas
			DtoProgHallazgoPieza dtop= new DtoProgHallazgoPieza();
			dtop.setHallazgo(pieza.getHallazgo().intValue());
			dtop.setInfoFechaUsuario(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
			dtop.setPiezaDental(pieza.getPieza().intValue());
			dtop.setPlanTratamiento(forma.getDtoPresupuesto().getPlanTratamiento().intValue());
			dtop.setPrograma(programa.getPrograma().getCodigo().intValue());
			dtop.setSeccion(pieza.getSeccion());
			
			if(pieza.getNumSuperficies()<=1)
			{
				//PARA ESTE CASO ES NUEVO ENTONCES NO DEBEMOS VERIFICAR EXISTENCIA
				
				dtop.setColorLetra(ColoresPlanTratamiento.NEGRO.getColor());
				if(pieza.getSuperficie().doubleValue()>0)
				{
					DtoSuperficiesPorPrograma dtoDet= new DtoSuperficiesPorPrograma();
					dtoDet.setDetPlanTratamiento(detPlanTratamiento.intValue());
					dtoDet.setInfoFechaUsuario(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
					dtoDet.setSuperficieDental(pieza.getSuperficie().intValue());
					dtop.getSuperficiesPorPrograma().add(dtoDet);
				}
				else
				{
					DtoSuperficiesPorPrograma dtoDet= new DtoSuperficiesPorPrograma();
					dtoDet.setDetPlanTratamiento(detPlanTratamiento.intValue());
					dtoDet.setInfoFechaUsuario(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
					if(dtop.getSeccion().equals(ConstantesIntegridadDominio.acronimoBoca))
					{	
						dtoDet.setSuperficieDental(ConstantesBD.codigoSuperficieBoca);
					}
					else
					{
						dtoDet.setSuperficieDental(ConstantesBD.codigoSuperficieDiente);
					}
					dtop.getSuperficiesPorPrograma().add(dtoDet);
				}
				
				ArrayList<DtoProgHallazgoPieza> listaHallazgos = PlanTratamiento.guardarRelacionesProgSuperficies(con, dtop);
				
				if(!validarRegistroProgramaHallazgoPieza(listaHallazgos))
				{
					logger.error("NO INSERTA COLORES DEL PLAN");
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);

					return salirEliminarCitaProgramada(mapping, forma, request,
							con);
				
				}else{
					
					ArrayList<DtoServicioOdontologico> serviciosProg = UtilidadOdontologia.obtenerServiciosXEstadoCitaXTipoCita
					(paciente.getCodigoPersona(), paciente.getCodigoIngreso(), ConstantesIntegridadDominio.acronimoProgramado, 
							ConstantesIntegridadDominio.acronimoTratamiento, forma.getCodigoProximaCitaRegistrada());

					actualizarServiciosCitaProgramada(con, programa.getPrograma().getCodigo(), pieza, detallesAsociadosCita, dtop, serviciosProg);
				}
			}
			else
			{
				
				//PARA ESTE CASO ES POSIBLE QUE YA EXISTA EN LA BD LA INFO DE COLOR, ENTONCES DEBEMOS VERIFICAR SI YA FUE INSERTADO
				
				DtoPresupuestoPlanTratamientoNumeroSuperficies dtoBusqueda= new DtoPresupuestoPlanTratamientoNumeroSuperficies();
				dtoBusqueda.setHallazgo(new InfoDatosInt(pieza.getHallazgo().intValue()));
				dtoBusqueda.setPiezaDental(new InfoDatosInt(pieza.getPieza().intValue()));
				dtoBusqueda.setPresupuesto(forma.getDtoPresupuesto().getCodigoPK());
				dtoBusqueda.setPrograma(new InfoDatosInt(programa.getPrograma().getCodigo().intValue()));
				dtoBusqueda.setSeccion(new InfoIntegridadDominio(pieza.getSeccion()));
				InfoNumSuperficiesPresupuesto infoN= NumeroSuperficiesPresupuesto.obtenerInfoNumSuperficiesPresupuestoXSuperfice(con, dtoBusqueda, pieza.getSuperficie().intValue());
				dtop.setColorLetra(infoN.getColor());
				
				//verificamos si existe el encabezado en el plan de tratamiento
				DtoProgHallazgoPieza dtoPHP= new DtoProgHallazgoPieza();
				dtoPHP.setColorLetra(dtop.getColorLetra());
				dtoPHP.setHallazgo(dtop.getHallazgo());
				dtoPHP.setPiezaDental(dtop.getPiezaDental());
				dtoPHP.setPlanTratamiento(dtop.getPlanTratamiento());
				dtoPHP.setPrograma(dtop.getPrograma());
				dtoPHP.setSeccion(dtop.getSeccion());
				DtoProgHallazgoPieza dtoResultado= NumeroSuperficiesPresupuesto.obtenerProgramaHallazgoPiezaPlanTratamiento(con, dtoPHP);
				
				//si no existe entonces lo insertamos
				if(dtoResultado==null)
				{
					DtoSuperficiesPorPrograma dtoDet= new DtoSuperficiesPorPrograma();
					dtoDet.setDetPlanTratamiento(detPlanTratamiento.intValue());
					dtoDet.setInfoFechaUsuario(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
					dtoDet.setSuperficieDental(infoN.getCodigoSuperficie());
					dtop.getSuperficiesPorPrograma().add(dtoDet);
					
					ArrayList<DtoProgHallazgoPieza> listaHallazgos = PlanTratamiento.guardarRelacionesProgSuperficies(con, dtop);
					
					if(!validarRegistroProgramaHallazgoPieza(listaHallazgos))
					{
						logger.error("NO INSERTA COLORES DEL PLAN");
						UtilidadBD.abortarTransaccion(con);
						UtilidadBD.closeConnection(con);
						
						return salirEliminarCitaProgramada(mapping, forma,
								request, con);
					
					}else{
						
						ArrayList<DtoServicioOdontologico> serviciosProg = UtilidadOdontologia.obtenerServiciosXEstadoCitaXTipoCita
						(paciente.getCodigoPersona(), paciente.getCodigoIngreso(), ConstantesIntegridadDominio.acronimoProgramado, 
								ConstantesIntegridadDominio.acronimoTratamiento, forma.getCodigoProximaCitaRegistrada());

						actualizarServiciosCitaProgramada(con, programa.getPrograma().getCodigo(), pieza, detallesAsociadosCita, dtop, serviciosProg);
					}
				}
				//de lo contrario verificamos si tiene detalle
				else
				{
					boolean existeSuperficie= false;
					for(DtoSuperficiesPorPrograma dtoSuperficies: dtoResultado.getSuperficiesPorPrograma())
					{
						if(dtoSuperficies.getSuperficieDental()==infoN.getCodigoSuperficie())
						{
							existeSuperficie= true;
							break;
						}
					}
					if(!existeSuperficie)
					{
						DtoSuperficiesPorPrograma dtoDet= new DtoSuperficiesPorPrograma();
						dtoDet.setDetPlanTratamiento(detPlanTratamiento.intValue());
						dtoDet.setInfoFechaUsuario(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
						dtoDet.setSuperficieDental(infoN.getCodigoSuperficie());
						dtoDet.setProgHallazgoPieza(new DtoProgHallazgoPieza(dtoResultado.getCodigoPk(), dtoResultado.getColorLetra()));
						dtop.getSuperficiesPorPrograma().add(dtoDet);
					
						if(!NumeroSuperficiesPresupuesto.guardarSuperficiesPlanTratamiento(con, dtoDet))
						{
							logger.error("NO INSERTA superficies COLORES DEL PLAN");
							UtilidadBD.abortarTransaccion(con);
							UtilidadBD.closeConnection(con);

							return salirEliminarCitaProgramada(mapping, forma,
									request, con);
						}
					}
				}
			}
		}
		else
		{
			logger.info("NO HAY SERVICIOS");
		}
		return null;
	}



	/**
	 * Método que se encarga de validar si se ha realizado un registro de programa
	 * hallazgo pieza para cada uno de los registros.
	 * 
	 * @param listaHallazgos
	 * @return
	 */
	private boolean validarRegistroProgramaHallazgoPieza(ArrayList<DtoProgHallazgoPieza> listaHallazgos) {
	
		boolean resultado = true;
		
		if(listaHallazgos!=null && listaHallazgos.size()>0)
		{
			for (DtoProgHallazgoPieza progHallazgoPieza : listaHallazgos) {
				
				if(progHallazgoPieza==null || !progHallazgoPieza.isRegistrado()){
					
					resultado = false;
					return resultado;
				}
			}
		}else{
			
			resultado = false;
		}
		
		return resultado;
	}

	
	/**
	 * Método que se encarga de actualizar para cada uno de los servicios asociados a
	 * la cita programada generada en el proceso de contratación, el programa hallazgo pieza
	 * respectivo
	 * 
	 * @param con
	 * @param codigoPrograma
	 * @param pieza
	 * @param detallesAsociadosCita
	 * @param dtoProgramaHallazgo
	 * @param serviciosProg
	 */
	private void actualizarServiciosCitaProgramada(Connection con, Double codigoPrograma, DtoPresupuestoPiezas pieza,
			ArrayList<DtoDetalleProximaCita> detallesAsociadosCita,
			DtoProgHallazgoPieza dtoProgramaHallazgo, ArrayList<DtoServicioOdontologico> serviciosProg) {
		
		
		for (DtoDetalleProximaCita dtoDetalleProximaCita : detallesAsociadosCita) {
			
			if(dtoDetalleProximaCita.getPrograma().getCodigo() == codigoPrograma){
			
				DtoPresupuestoPiezas dtoPresupuestoPieza = dtoDetalleProximaCita.getPieza();
				
				if(dtoPresupuestoPieza!=null && dtoPresupuestoPieza.getHallazgo().intValue() == pieza.getHallazgo().intValue()
						&&  dtoPresupuestoPieza.getSuperficie().intValue() == pieza.getSuperficie().intValue()
						&& dtoPresupuestoPieza.getPieza().intValue() == pieza.getPieza().intValue()){
					
					ArrayList<InfoServicios> serviciosAsociados = dtoDetalleProximaCita.getServicios();

					for (InfoServicios servicioAsociado : serviciosAsociados) {
					
						for (DtoServicioOdontologico servicioRegistrado : serviciosProg) {
							
							if(servicioRegistrado.getCodigoPk() == servicioAsociado.getCodigoPk()){
								
								CitaOdontologica.actualizarProgramaHallazgoPiezaServicioCita(con, dtoProgramaHallazgo.getCodigoPk(), servicioRegistrado.getCodigoPk());
								break;
							}
						}
					}
					
					break;
				}
			}
		}
	}


	/**
	 * @param mapping
	 * @param paciente
	 * @param request
	 * @param preContratar
	 * @param con
	 * @param dtoConvenio
	 * @param programa 
	 */
	private ActionForward marcarBonosUsadosContrato(PersonaBasica paciente,
			boolean preContratar, Connection con,
			DtoPresupuestoOdoConvenio dtoConvenio, int programa) 
	{
		if(!preContratar)
		{
			if(dtoConvenio.getValorDescuentoBono().doubleValue()>0 && dtoConvenio.getSeleccionadoBono())
			{
				IEmisionBonosServicio emisionBonos=AdministracionFabricaServicio.crearEmisionBonosServicio();
				
				DtoBonoDescuento bono=new DtoBonoDescuento();
				bono.setPresupuestoProgramaServicio(dtoConvenio.getPresupuestoOdoProgServ().intValue());
				DtoPersonas persona=new DtoPersonas();
				persona.setCodigo(paciente.getCodigoPersona());
				bono.setPersona(persona);
				bono.setPrograma(programa);
				bono.setSerial(dtoConvenio.getSerialBono().toBigInteger());
				bono.setBonoPaciente(dtoConvenio.getBonoPaciente());
				emisionBonos.marcarBonoUtilizado(con, bono);
				/*
				if(!CargosOdon.liberarSerialBono(con, dtoConvenio.getConvenio().getCodigo(), paciente.getCodigoIngreso(), dtoConvenio.getSerialBono(), programa))
				{
					logger.error("NO LIBERA EL BONO !!!!!!");
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
				}
				*/
			}
		}
		return null;
	}

	/**
	 * @param forma
	 * @param usuario
	 * @param preContratar
	 */
	private void actualizarEstadoFHUPresupuesto(PresupuestoOdontologicoForm forma, 
												UsuarioBasico usuario,
												boolean preContratar) 
	{
		forma.getDtoPresupuesto().setFechaUsuarioGenera( forma.getDtoPresupuesto().getUsuarioModifica());
		forma.getDtoPresupuesto().setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		forma.getDtoPresupuesto().setEstado(ConstantesIntegridadDominio.acronimoContratadoContratado);
		if(preContratar)
		{
			forma.getDtoPresupuesto().setEstado(ConstantesIntegridadDominio.acronimoPrecontratado);
		}
	}

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param con
	 */
	private ActionForward insertarNuevaSolicitudDctoOdo(	ActionMapping mapping,
															PresupuestoOdontologicoForm forma, 
															UsuarioBasico usuario,
															HttpServletRequest request, 
															Connection con)
	{
		DtoPresupuestoOdontologicoDescuento dtoDcto= new DtoPresupuestoOdontologicoDescuento();
		dtoDcto.setEstado(ConstantesIntegridadDominio.acronimoXDefinir);
		dtoDcto.setPresupuesto(forma.getDtoPresupuesto().getCodigoPK());
		dtoDcto.setUsuarioFechaModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		dtoDcto.setUsuarioFechaSolicitud(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		
		dtoDcto.setConsecutivo(new BigDecimal(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoSolicitudDescuentoOdo, usuario.getCodigoInstitucionInt())));
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoSolicitudDescuentoOdo, usuario.getCodigoInstitucionInt(), dtoDcto.getConsecutivo()+"", ConstantesBD.acronimoSi, ConstantesBD.acronimoNo);
		
		if(PresupuestoOdontologico.guardarPresupuestoDescuento(dtoDcto, con)  <= 0)
		{
			logger.error("NO INSERTA LOS DCTOS");
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoSolicitudDescuentoOdo, usuario.getCodigoInstitucionInt(), dtoDcto.getConsecutivo()+"", ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
		}
		
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoSolicitudDescuentoOdo, usuario.getCodigoInstitucionInt(), dtoDcto.getConsecutivo()+"", ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		logger.info("modifica  100%%%");
		return null;
	}
   	
   	
   	/**
   	 * 
   	 * @param forma
   	 * @param usuario
   	 */
   	private void recalcularBonosPromociones(PresupuestoOdontologicoForm forma,
			UsuarioBasico usuario)
	{
		
   		for(DtoPresuOdoProgServ dtoProgServ: forma.getListaProgramasServicios())
   		{
   			for(DtoPresupuestoOdoConvenio dtoConvenio: dtoProgServ.getListPresupuestoOdoConvenio())
   			{
   				boolean existePaquete=dtoConvenio.getDtoPresupuestoPaquete().getCodigoPk().doubleValue()>0;
   				
   				if(!existePaquete)
   				{	
	   				InfoBonoDcto infoBono= CargosOdon.obtenerDescuentoXBonos( dtoConvenio.getConvenio().getCodigo() , forma.getDtoPresupuesto().getCuenta(), dtoProgServ.getPrograma().getCodigo(), dtoProgServ.getServicio().getCodigo(), usuario.getCodigoInstitucionInt(), dtoConvenio.getValorUnitarioMenosDctoComercial(), UtilidadFecha.getFechaActual());
	   				InfoPromocionPresupuestoServPrograma infoPromocion= CargosOdon.obtenerDescuentoXPromociones(dtoProgServ.getPrograma().getCodigo(), dtoProgServ.getServicio().getCodigo(), dtoConvenio.getConvenio().getCodigo(), dtoConvenio.getContrato().getCodigo(), UtilidadFecha.getFechaActual(), usuario.getCodigoInstitucionInt(), forma.getDtoPresupuesto().getCuenta(), dtoConvenio.getValorUnitarioMenosDctoComercial());
	   				
	   				boolean existeBono= infoBono.getValorDctoCALCULADO().doubleValue()>0;
	   				logger.info("EXISTE BONO ------------------------_>"+existeBono);
	   				boolean existePromocion= infoPromocion.getValorPromocion().doubleValue()>0;
	   				logger.info("EXISTE PROMOCION ------------------------_>"+existePromocion);
	   				//boolean existeDiferenteBono= (dtoConvenio.getSerialBono().doubleValue()!=infoBono.getSerial().doubleValue() || dtoConvenio.getValorDescuentoBono().doubleValue()!=infoBono.getValorDctoCALCULADO().doubleValue());
	   				//boolean existeDiferentePromocion= (dtoConvenio.getValorDescuentoPromocion().doubleValue()!=infoPromocion.getValorDescuentosUnitarioPromocionesCALCULADO().doubleValue());
	   				
	   				//1. verificamos si solo existe bono sin promocion
	   				if(existeBono && !existePromocion)
	   				{
	   					dtoConvenio.resetPromocion();
	   					dtoConvenio.setSeleccionadoBono(true);
	   					dtoConvenio.setValorDescuentoBono(infoBono.getValorDctoCALCULADO());
	   					dtoConvenio.setPorcentajeDctoBono(infoBono.getPorcentajeDescuento());
	   					dtoConvenio.setAdvertenciaBono(infoBono.getAdvertencia());
	   					dtoConvenio.setSerialBono(infoBono.getSerial());
	   					dtoConvenio.setSeleccionadoPorcentajeBono(infoBono.isSeleccionadoPorcentaje());
	   					dtoConvenio.setBonoPaciente(infoBono.getBonoPaciente());
	   				}
	   				//2. verificamos si solo existe promocion sin bono
	   				else if(!existeBono && existePromocion)
	   				{
	   					dtoConvenio.resetBono();
	   					dtoConvenio.setSeleccionadoPromocion(true);
	   					dtoConvenio.setValorDescuentoPromocion(infoPromocion.getValorPromocion());
	   					dtoConvenio.setPorcentajePromocion(infoPromocion.getPorcentajePromocion());
	   					dtoConvenio.setPorcentajeHonorarioPromocion(infoPromocion.getPorcentajeHonorario());
	   					dtoConvenio.setValorHonorarioPromocion(infoPromocion.getValorHonorario());
	   					dtoConvenio.setAdvertenciaPromocion(infoPromocion.getAdvertencia());
	   					dtoConvenio.setSeleccionadoPorcentajePromocion(infoPromocion.isSeleccionadoPorcentaje());
	   				}
	   				//3. si no existe ni bono ni promocion
	   				else if(!existeBono && !existePromocion)
	   				{
	   					dtoConvenio.resetPromocion();
	   					dtoConvenio.resetBono();
	   				}
	   				//4. si existen ambas
	   				else if(existeBono && existePromocion)
	   				{
	   					//lo del seleccionado lo dejamos tal como estaba
	   					dtoConvenio.setValorDescuentoBono(infoBono.getValorDctoCALCULADO());
	   					dtoConvenio.setPorcentajeDctoBono(infoBono.getPorcentajeDescuento());
	   					dtoConvenio.setAdvertenciaBono(infoBono.getAdvertencia());
	   					dtoConvenio.setSerialBono(infoBono.getSerial());
	   					
	   					dtoConvenio.setValorDescuentoPromocion(infoPromocion.getValorPromocion());
	   					dtoConvenio.setPorcentajePromocion(infoPromocion.getPorcentajePromocion());
	   					dtoConvenio.setPorcentajeHonorarioPromocion(infoPromocion.getPorcentajeHonorario());
	   					dtoConvenio.setValorHonorarioPromocion(infoPromocion.getValorHonorario());
	   					dtoConvenio.setAdvertenciaPromocion(infoPromocion.getAdvertencia());
	   					
	   					dtoConvenio.setSeleccionadoPorcentajeBono(infoBono.isSeleccionadoPorcentaje());
	   					dtoConvenio.setSeleccionadoPorcentajePromocion(infoPromocion.isSeleccionadoPorcentaje());
	   				}
   				}
   				else
   				{
   					dtoConvenio.resetPromocion();
   					dtoConvenio.resetBono();
   				}
   			}	
   		}	
 	}

	/**
   	 * 
   	 * @param mapping
   	 * @param forma
   	 * @param usuario
   	 * @param paciente
   	 * @param request
   	 * @return
   	 */
	private ActionForward accionGuardarModificarPresupuesto(	ActionMapping mapping, 
																PresupuestoOdontologicoForm forma,
																UsuarioBasico usuario, 
																PersonaBasica paciente , 
																HttpServletRequest request,
																HttpServletResponse response) 
	{
		//1. INICIO DE LA TRANSACCION
		Connection con= UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
	    
		//2.CON LO DE PAQUETES LO MEJOR ES BORRAR Y VOLVER A INSERTAR LOS REGISTROS
		ActionForward forward= eliminarProgramasServiciosCascada(mapping, forma, request, con);
		if(forward!=null)
		{
			return forward;
		}
		//3.SE INSERTA TODA LA ESTRUCTURA NUEVAMENTE
		forward=  accionGuardarPresupuesto(mapping, forma, usuario, paciente, request, response, false);
		if(forward!=null)
		{
			return forward;
		}
		logger.info("INSERTA EL 100% DE LA MODIFICACION--------- ");
		//5. FINALIZAMOS LA TRANSACCION
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		forma.setEstado("empezar");
		return accionEmpezar(mapping, forma, usuario, paciente,request, false /*crear bloqueo ya existe*/, response);
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param con
	 * @return
	 */
	private ActionForward modificarTarifasPendientesConveniosPresupuesto(	ActionMapping mapping, 
																			PresupuestoOdontologicoForm forma,
																			UsuarioBasico usuario, 
																			HttpServletRequest request, 
																			Connection con) 
	{
		
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nENTRA A MODIFICAR LAS TARIFAS PENDIENTES CONVENIOS PRESUPUESTO!!!!!!!!!!!!!!!!!!!!!!!!\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		
		for(DtoPresuOdoProgServ dtoPresuServPrograma: forma.getListaProgramasServicios())
		{
			for(DtoPresupuestoOdoConvenio dtocon: dtoPresuServPrograma.getListPresupuestoOdoConvenio())
			{
				logger.info("dtocon.getTipoModificacionCONVENIO()-->"+dtocon.getTipoModificacionCONVENIO());
				if(dtocon.getTipoModificacionCONVENIO()==EnumTipoModificacion.RECALCULADA_TARIFA)
				{
					//eliminamos los servicios de los programas
					if(forma.getUtilizaPrograma())
					{
						DtoPresupuestoDetalleServiciosProgramaDao dtoServiciosPrograma= new DtoPresupuestoDetalleServiciosProgramaDao();
						dtoServiciosPrograma.setPresupuestoOdoConvenio(dtocon.getCodigoPK());
						if(!PresupuestoOdontologico.eliminarPresupuestoDetalleServiciosProgramaDao(dtoServiciosPrograma))
						{
							logger.error("NO ELIMINA -->"+dtoServiciosPrograma.getCodigoPk());
							UtilidadBD.abortarTransaccion(con);
							UtilidadBD.closeConnection(con);
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
						}
					}
					//eliminamos convenios
					if(!PresupuestoOdontologico.eliminarPresupuestoOdoConvenioXCodigoPk(dtocon.getCodigoPK(), con))
					{
						logger.error("NO ELIMINA -->"+dtocon.getCodigoPK());
						UtilidadBD.abortarTransaccion(con);
						UtilidadBD.closeConnection(con);
						return salirEliminarCitaProgramada(mapping, forma,
								request, con);
					}
					
					
					//insertamos los nuevos
					//3 SETEO VALORES NUEVOS A CONVENIOS
					dtocon.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
					double codigoConvenio=PresupuestoOdontologico.guardarPresupuestoOdoConvenio(dtocon , con);
					
					logger.info("CONVENIO --------------------_>"+codigoConvenio);
					if(codigoConvenio<=0)
					{
						logger.error("NO INSERTA CONVENIO");
						UtilidadBD.abortarTransaccion(con);
						UtilidadBD.closeConnection(con);
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
					}
					dtocon.setCodigoPK(new BigDecimal(codigoConvenio));
					
					/*
					 * 
					 * SE guardan detalle servicios por prog
					 * 
					 */
					for (DtoPresupuestoDetalleServiciosProgramaDao detalle : dtocon.getListaDetalleServiciosPrograma())
					{
						//logger.info("DETALLE PROGRAMA ---------->"+UtilidadLog.obtenerString(detalle, true));
						detalle.setPresupuestoOdoConvenio(new BigDecimal(codigoConvenio));
						
						detalle.setCodigoPk(new BigDecimal(PresupuestoOdontologico.insertPresupuestoDetalleServiciosProgramaDao(detalle, con)));
						if(detalle.getCodigoPk().doubleValue()<=0)
						{
							logger.error("NO inserta SERV X PROG");
							UtilidadBD.abortarTransaccion(con);
							UtilidadBD.closeConnection(con);
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
						}
						
					}
					
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param con
	 * @return
	 */
	private ActionForward modificarBonosPromocionesPresupuesto(	ActionMapping mapping, 
																PresupuestoOdontologicoForm forma,
																UsuarioBasico usuario, 
																HttpServletRequest request, 
																Connection con) 
	{
		for(DtoPresuOdoProgServ dtoPresuServPrograma: forma.getListaProgramasServicios())
		{
			if(dtoPresuServPrograma.getTipoModificacion()!=EnumTipoModificacion.ELIMINADO)
			{
				for(DtoPresupuestoOdoConvenio dtoConvenio: dtoPresuServPrograma.getListPresupuestoOdoConvenio())
				{
					if(dtoConvenio.getCodigoPK().doubleValue()>0)
					{	 
						if(!PresupuestoOdontologico.modificarBonos(con, dtoConvenio))
						{
							logger.error("NO MODIFICA BONOS Y PROM -->");
							UtilidadBD.abortarTransaccion(con);
							UtilidadBD.closeConnection(con);
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
						}
						if(!PresupuestoOdontologico.modificarPromociones(con, dtoConvenio))
						{
							logger.error("NO MODIFICA BONOS Y PROM -->");
							UtilidadBD.abortarTransaccion(con);
							UtilidadBD.closeConnection(con);
							return salirEliminarCitaProgramada(mapping, forma,
									request, con);
						}
					}	 
				}
			}
		}
		
		return null;
	}




	private ActionForward salirEliminarCitaProgramada(ActionMapping mapping,
			PresupuestoOdontologicoForm forma, HttpServletRequest request,
			Connection con)
	{
		IProximaCitaProgramadaServicio proximaCita=OdontologiaServicioFabrica.crearProximaCitaProgramadaServicio();
		proximaCita.eliminarRegistroProximaCita(forma.getCodigoProximaCitaRegistrada());
		return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
	}
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param con
	 */
	private ActionForward eliminarProgramasServiciosCascada(	ActionMapping mapping,
														PresupuestoOdontologicoForm forma, 
														HttpServletRequest request,
														Connection con) 
	{
		
		ArrayList<DtoPresuOdoProgServ> listaProgramas= PresupuestoOdontologico.cargarPresupuestoOdoProgServ(forma.getDtoPresupuesto().getCodigoPK());
		
		for(DtoPresuOdoProgServ dto: listaProgramas)
		{
			logger.info("ENTRA A ELIMINAR1");
			if(!PresupuestoOdontologico.eliminarCascadaProgramaServPresupuesto(forma.getUtilizaPrograma(), con, dto.getCodigoPk(), forma.getDtoPresupuesto().getCodigoPK()))
			{
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
			}	 
		}
		
		//ELIMINAMOS LOS PAQUETES PRESUPUESTO
		DtoPresupuestoPaquetes dtopp= new DtoPresupuestoPaquetes();
		dtopp.setPresupuesto(forma.getDtoPresupuesto().getCodigoPK());
		if(!PresupuestoPaquetes.eliminar(con, dtopp))
		{
			Log4JManager.error("NO ELIMINA -->");
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
		}
		
		return null;
	}

	

	/**
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param con
	 * @param codigoPresupuesto
	 * @param dtoProSer
	 */
	private ActionForward insertarNuevoProgServPresupuesto(	ActionMapping mapping,
															UsuarioBasico usuario, HttpServletRequest request, Connection con,
															double codigoPresupuesto, DtoPresuOdoProgServ dtoProSer)
	{
		//2 SETEO VALORES NUEVOS A PROGRAMA
		dtoProSer.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		dtoProSer.setPresupuesto(new BigDecimal(codigoPresupuesto));
		
		if(dtoProSer.getCantidad()>0)
		{	
			double codigoDetalle=PresupuestoOdontologico.guardarPresupuestoOdoProgramaServicio(dtoProSer , con);
			logger.info("PROGRAMA O SERVICIO -------------------->"+codigoPresupuesto);
			if(codigoDetalle<=0)
			{
				logger.error("NO INSERTA PRO O SER");
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
			}	
			
			for(DtoPresupuestoOdoConvenio dtoConvenio : dtoProSer.getListPresupuestoOdoConvenio())
			{
				//3 SETEO VALORES NUEVOS A CONVENIOS
				dtoConvenio.setPresupuestoOdoProgServ(new BigDecimal(codigoDetalle));
				dtoConvenio.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
				
				double codigoConvenio=PresupuestoOdontologico.guardarPresupuestoOdoConvenio(dtoConvenio , con);
				this.ingresarATotales(dtoConvenio);
				
				logger.info("CONVENIO --------------------_>"+codigoConvenio);
				if(codigoConvenio<=0)
				{
					logger.error("NO INSERTA CONVENIO");
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
				}
				
				
				//SE guardan detalle servicios por prog
				for (DtoPresupuestoDetalleServiciosProgramaDao detalle : dtoConvenio.getListaDetalleServiciosPrograma())
				{
					//logger.info("DETALLE PROGRAMA ---------->"+UtilidadLog.obtenerString(detalle, true));
					detalle.setPresupuestoOdoConvenio(new BigDecimal(codigoConvenio));
					
					if(PresupuestoOdontologico.insertPresupuestoDetalleServiciosProgramaDao(detalle, con)<=0)
					{
						logger.error("NO inserta SERV X PROG");
						UtilidadBD.abortarTransaccion(con);
						UtilidadBD.closeConnection(con);
						
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
					}
				}
			}
			
			for(DtoPresupuestoPiezas dtoPieza : dtoProSer.getListPresupuestoPiezas())
			{
				//4 SETEO VALORES NUEVOS A PIEZAS
				dtoPieza.setPresupuestoOdoProgServ(new BigDecimal(codigoDetalle));
				dtoPieza.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
				double codigoPieza=PresupuestoOdontologico.guardarPresupuestoPiezas(dtoPieza , con);
				logger.info("PIEZA --------------------_>"+codigoPieza);
				if(codigoPieza<=0)
				{
					logger.error("NO PIEZA");
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
				}	
			}
		}	
		return null;
	}

	/**
	 * 
	 * @param tarifa
	 */
	public void  ingresarATotales(DtoPresupuestoOdoConvenio tarifa)
	{
		if(!treeTemp.contains(tarifa.getConvenio().getCodigo()))
		{	
			treeTemp.add(tarifa.getConvenio().getCodigo());
			arrayTotales.add(tarifa);
		}
		else
		{
			 for(DtoPresupuestoOdoConvenio i : arrayTotales)
			 {
				 if(i.getConvenio().getCodigo() == tarifa.getConvenio().getCodigo())
				 {
					 i.setValorUnitario(i.getValorUnitario().add(tarifa.getValorUnitario()));
				 }
			 }
		}
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarPresupuesto(	ActionMapping mapping,
													PresupuestoOdontologicoForm forma, 
													UsuarioBasico usuario,
													PersonaBasica paciente , 
													HttpServletRequest request,
													HttpServletResponse response,
													boolean insertarEncabezado
												  ) 
	{
		//1. RESTEO LISTA PARA VALORES TOTALES
		this.arrayTotales = new  ArrayList<DtoPresupuestoOdoConvenio>();
		this.treeTemp = new TreeSet<Integer>();
		
		//2.INICIAMOS LA TRANSACCION
		Connection con= UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		
		//3 SETEO VALORES NUEVOS A PRESUPUESTO
		double codigoPresupuesto=0;
		if(insertarEncabezado)
		{	
			forma.getDtoPresupuesto().setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
			forma.getDtoPresupuesto().setFechaUsuarioGenera(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
			forma.getDtoPresupuesto().setEstado(ConstantesIntegridadDominio.acronimoEstadoActivo);
			
			//4. GUARDAMOS EL ENCABEZADO DEL PRESUPUESTO
			codigoPresupuesto=PresupuestoOdontologico.guardarPresupuesto(forma.getDtoPresupuesto() , con);
			if(codigoPresupuesto<=0)
			{
				logger.error("NO Presupuesto");
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
			}
		}	
		else
		{
			codigoPresupuesto= forma.getDtoPresupuesto().getCodigoPK().doubleValue();
		}
		
		//5. INSERTAMOS LOS paquetes_presupuesto
		for(InfoPaquetesPresupuesto infoPaquete: forma.getListaPaquetes())
		{
			if(infoPaquete.isSeleccionado())
			{	
				DtoPresupuestoPaquetes dto= new DtoPresupuestoPaquetes();
				dto.setFHU(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
				dto.setDetallePaqueteOdontologicoConvenio(infoPaquete.getDetallePkPaqueteOdonCONVENIO());
				dto.setPresupuesto(new BigDecimal(codigoPresupuesto));
				
				dto.setCodigoPk(PresupuestoPaquetes.insertar(con, dto));
				if(dto.getCodigoPk().doubleValue()<=0)
				{
					logger.error("NO Presupuesto");
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
				}
				//seteamos el presupuesto odo convenio con el codigo presupuesto paquete que le corresponde
				PresupuestoPaquetes.setearPaquetesPresupuestoOdoConvenio(forma.getListaProgramasServicios(), dto);
			}	
		}
		
		//6. POR CADA PROGRAMA SERVICIO INSERTAMOS LAS CANTIDADES - CONVENIOS - PIEZAS - DETALLE SERVICIOS X PROGRAMA
		for(DtoPresuOdoProgServ dtoProSer :forma.getListaProgramasServicios())
		{
			ActionForward forward= insertarNuevoProgServPresupuesto(mapping, usuario, request, con,	codigoPresupuesto, dtoProSer);
			if(forward!=null)
			{
				return forward;
			}
		}
		
		//7. INSERTAMOS LA INFORMACION DEL PLAN DE TRATAMIENTO DEL PRESUPUESTO
		ActionForward forward=  insertarPlanTtoPresupuesto(mapping, forma, usuario, request, con, codigoPresupuesto);
		if(forward!=null)
		{
			return forward;
		}
		
		//8 INSERTAMOS LA INFORMACION DE LA AGRUPACION DE LAS N SUPERFICIES
		if(forma.getUtilizaPrograma())
		{	
			forward= insertarAgrupacionNSuperficies(mapping, forma, usuario, request, con, codigoPresupuesto);
			if(forward!=null)
			{
				return forward;
			}
		}	

		logger.info("inserta 100%%%");
		
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		
		if(insertarEncabezado)
		{	
			forma.setEstado("empezar");
			return accionEmpezar(mapping, forma, usuario, paciente, request, false /*crear bloqueo ya existe*/, response);
		}
		return null;
	}

	

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param con
	 * @param codigoPresupuesto
	 */
	private ActionForward insertarPlanTtoPresupuesto(ActionMapping mapping,
			PresupuestoOdontologicoForm forma, UsuarioBasico usuario,
			HttpServletRequest request, Connection con, double codigoPresupuesto) {
		logger.info("6. INSERTAM0OS LA INFORMACION DEL PLAN DE TRATAMIENTO DEL PRESUPUESTO");
		ArrayList<DtoPlanTratamientoPresupuesto> listaPlanTtoPresupuesto= cargarPlanTtoPresupuestoTabla(forma, usuario, new BigDecimal(codigoPresupuesto));
		
		for(DtoPlanTratamientoPresupuesto dtoPlanTtoPresupuesto: listaPlanTtoPresupuesto)
		{
			BigDecimal codigoPlanTratamientoPresupuesto= PresupuestoOdontologico.insertarPlanTtoPresupuesto(con, dtoPlanTtoPresupuesto, forma.getUtilizaPrograma());
			if(codigoPlanTratamientoPresupuesto.doubleValue()<=0)
			{
				logger.error("NO plan tto Presupuesto");
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
			}
		}
		return null;
	}

	/**
	 * 
	 * Metodo para .......
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private ActionForward insertarAgrupacionNSuperficies(ActionMapping mapping,
			PresupuestoOdontologicoForm forma, UsuarioBasico usuario,
			HttpServletRequest request, Connection con, double codigoPresupuesto) 
	{
		ArrayList<DtoPresupuestoPlanTratamientoNumeroSuperficies> listaNSuperficies= cargarAgrapacionNSuperficiesPlanTabla(forma, usuario, new BigDecimal(codigoPresupuesto), con);
		
		for(DtoPresupuestoPlanTratamientoNumeroSuperficies dtoNSuperficies: listaNSuperficies)
		{
			BigDecimal codigoEncabezado= NumeroSuperficiesPresupuesto.insertarEncabezado(con, dtoNSuperficies);
			
			if(codigoEncabezado.doubleValue()<=0)
			{
				logger.error("NO plan tto Presupuesto");
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
			}
			
			for(DtoDetallePresupuestoPlanNumSuperficies dtoDetalleNSuperficies: dtoNSuperficies.getListaDetalleSuperficies())
			{
				dtoDetalleNSuperficies.setCodigoEncabezadoPresuPlanTtoNumSuperficies(codigoEncabezado);
				if(!NumeroSuperficiesPresupuesto.insertarDetalle(con, dtoDetalleNSuperficies))
				{
					logger.error("NO plan tto Presupuesto");
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
				}
			}	
		}
		return null;
	}
	
	/**
	 * 
	 * Metodo para .......
	 * @param forma
	 * @param usuario
	 * @param bigDecimal
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private ArrayList<DtoPresupuestoPlanTratamientoNumeroSuperficies> cargarAgrapacionNSuperficiesPlanTabla(
			PresupuestoOdontologicoForm forma, UsuarioBasico usuario,
			BigDecimal codigoPresupuesto, Connection con) 
	{
		ArrayList<DtoPresupuestoPlanTratamientoNumeroSuperficies> listaNSuperficies= new ArrayList<DtoPresupuestoPlanTratamientoNumeroSuperficies>();
		
		ArrayList<String> agrupar= new ArrayList<String>();
		for(InfoDetallePlanTramiento planTtoOriginalSeccionDET: forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle())
		{
			for(InfoHallazgoSuperficie infoSuperficie: planTtoOriginalSeccionDET.getDetalleSuperficie() )
			{	
				for(InfoProgramaServicioPlan infoPrograma: infoSuperficie.getProgramasOservicios())
				{
					
					Log4JManager.info("pieza-->"+planTtoOriginalSeccionDET.getPieza().getNombre()+" " +
							"hallazgo-->"+infoSuperficie.getHallazgoREQUERIDO().getNombre()+" " +
							"superficie-->"+infoSuperficie.getSuperficieOPCIONAL().getNombre()+" " +
							"programa-->"+infoPrograma.getNombreProgramaServicio()+" activo-->"+infoPrograma.getActivo()+" " +
							"asignado-->"+infoPrograma.isAsignadoAlPresupuesto());
					
					if(infoPrograma.getNumeroSuperficies()>1 && infoPrograma.isAsignadoAlPresupuesto())
					{
						String unique=	ConstantesIntegridadDominio.acronimoDetalle
										+"_"+planTtoOriginalSeccionDET.getPieza().getCodigo()
										+"_"+infoSuperficie.getHallazgoREQUERIDO().getCodigo()
										+"_"+infoPrograma.getCodigoPkProgramaServicio()
										+"_"+infoPrograma.getColorLetra();
						if(!agrupar.contains(unique))
						{	
							agrupar.add(unique);
							DtoPresupuestoPlanTratamientoNumeroSuperficies dtoEncabezado= new DtoPresupuestoPlanTratamientoNumeroSuperficies();
							dtoEncabezado.setCodigoPk(BigDecimal.ZERO); //no lo tengo que cargar se setea al insertar
							dtoEncabezado.setColor(infoPrograma.getColorLetra());
							dtoEncabezado.setFHU(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
							dtoEncabezado.setHallazgo(infoSuperficie.getHallazgoREQUERIDO());
							dtoEncabezado.setPiezaDental(planTtoOriginalSeccionDET.getPieza());
							dtoEncabezado.setPresupuesto(codigoPresupuesto);
							dtoEncabezado.setPrograma(new InfoDatosInt(infoPrograma.getCodigoPkProgramaServicio().intValue(), infoPrograma.getNombreProgramaServicio()));
							dtoEncabezado.setSeccion(new InfoIntegridadDominio(ConstantesIntegridadDominio.acronimoDetalle));
							ArrayList<DtoDetallePresupuestoPlanNumSuperficies> listaDetalle= new ArrayList<DtoDetallePresupuestoPlanNumSuperficies>();
							
							for(InfoNumSuperficiesPresupuesto infoNum: infoPrograma.getSuperficiesAplicaPresupuesto())
							{
								if(infoNum.getActivo() && (infoNum.getModificable() || infoNum.isMarcarXDefecto()))
								{	
									DtoDetallePresupuestoPlanNumSuperficies dtoDetalle= new DtoDetallePresupuestoPlanNumSuperficies();
									dtoDetalle.setCodigoEncabezadoPresuPlanTtoNumSuperficies(BigDecimal.ZERO); //no lo tengo que cargar se setea al insertar
									DtoPlanTratamientoPresupuesto dtoPlanPresupuesto = new DtoPlanTratamientoPresupuesto();
									dtoPlanPresupuesto.setActivo(true);
									dtoPlanPresupuesto.setDetPlanTratamiento(infoSuperficie.getCodigoPkDetalle());
									dtoPlanPresupuesto.setPresupuesto(dtoEncabezado.getPresupuesto());
									dtoPlanPresupuesto.setPrograma(new BigDecimal(dtoEncabezado.getPrograma().getCodigo()));
									//dtoPlanPresupuesto.setProgramaServicioPlanTratamientoFK(ValidacionesPresupuesto.obtenerCodigoPkProgramaServicioPlanTratamiento(infoSuperficie.getCodigoPkDetalle(), infoPrograma.getCodigoPkProgramaServicio(), infoPrograma.getCodigoP))
									//dtoDetalle.setCodigoPk(NumeroSuperficiesPresupuesto.obtenerCodigoPresuPlanTtoProgSer(con, dtoPlanPresupuesto)); //debo consultarlo
									dtoDetalle.setCodigoPk(BigDecimal.ZERO); //no lo tengo que setear, solo lo inserto al final
									dtoDetalle.setSuperficie(new InfoDatosInt(infoNum.getCodigoSuperficie(), infoNum.getNombreSuperficie()));
									dtoDetalle.setFHU(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
									listaDetalle.add(dtoDetalle);
								}	
							}
							dtoEncabezado.setListaDetalleSuperficies(listaDetalle);
							
							listaNSuperficies.add(dtoEncabezado);
						}	
					}
				}
			}	
		}
		return listaNSuperficies;
	}

	/**
	 * 
	 * @param forma
	 * @return
	 */
	private ArrayList<DtoPlanTratamientoPresupuesto> cargarPlanTtoPresupuestoTabla(PresupuestoOdontologicoForm forma, UsuarioBasico usuario, BigDecimal codigoPresupuesto) 
	{
		ArrayList<DtoPlanTratamientoPresupuesto> listaPlanTtoPresupuesto= new ArrayList<DtoPlanTratamientoPresupuesto>();
		
		for(InfoDetallePlanTramiento planTtoOriginalSeccionDET: forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle())
		{
			for(InfoHallazgoSuperficie infoSuperficie: planTtoOriginalSeccionDET.getDetalleSuperficie() )
			{	
				cargarProgramasServiciosPlanTtoPresupuesto(forma, usuario,codigoPresupuesto, listaPlanTtoPresupuesto,infoSuperficie);	
			}	
		}
		
		for(InfoDetallePlanTramiento planTtoOriginalSeccionOTROS: forma.getDtoPlanTratamiento().getSeccionOtrosHallazgos())
		{
			for(InfoHallazgoSuperficie infoSuperficie: planTtoOriginalSeccionOTROS.getDetalleSuperficie() )
			{	
				cargarProgramasServiciosPlanTtoPresupuesto(forma, usuario,codigoPresupuesto, listaPlanTtoPresupuesto,infoSuperficie);	
			}	
		}
		
		for(InfoHallazgoSuperficie infoSuperficie: forma.getDtoPlanTratamiento().getSeccionHallazgosBoca())
		{	
			cargarProgramasServiciosPlanTtoPresupuesto(forma, usuario,codigoPresupuesto, listaPlanTtoPresupuesto,infoSuperficie);	
		}	
		
		return listaPlanTtoPresupuesto;
	}

	/**
	 * @param forma
	 * @param usuario
	 * @param codigoPresupuesto
	 * @param listaPlanTtoPresupuesto
	 * @param infoSuperficie
	 */
	private void cargarProgramasServiciosPlanTtoPresupuesto(	PresupuestoOdontologicoForm forma, UsuarioBasico usuario,
																BigDecimal codigoPresupuesto,
																ArrayList<DtoPlanTratamientoPresupuesto> listaPlanTtoPresupuesto,
																InfoHallazgoSuperficie infoSuperficie) 
	{
		
		String tmpBusquedaServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
		
		for(InfoProgramaServicioPlan infoProgServ:  infoSuperficie.getProgramasOservicios())
		{	
			if(forma.getUtilizaPrograma())
			{
				//consultamos los servicios del programa
				ArrayList<DtoDetalleProgramas> arrayDetalleProgramas= ProgramasOdontologicos.cargarDetallePrograma(infoProgServ.getCodigoPkProgramaServicio().doubleValue(),tmpBusquedaServicios);
				//iteramos uno a uno los servicios de programa para obtener las tarifas
				for(DtoDetalleProgramas dtoPrograma: arrayDetalleProgramas)
				{
					DtoPlanTratamientoPresupuesto dtoPlanTtoPresupuesto= new DtoPlanTratamientoPresupuesto();
					dtoPlanTtoPresupuesto.setDetPlanTratamiento(infoSuperficie.getCodigoPkDetalle());
					dtoPlanTtoPresupuesto.setFHU(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
					dtoPlanTtoPresupuesto.setPresupuesto(codigoPresupuesto);
					dtoPlanTtoPresupuesto.setPrograma(infoProgServ.getCodigoPkProgramaServicio());
					dtoPlanTtoPresupuesto.setServicio(dtoPrograma.getServicio());
					dtoPlanTtoPresupuesto.setProgramaServicioPlanTratamientoFK(ValidacionesPresupuesto.obtenerCodigoPkProgramaServicioPlanTratamiento(infoSuperficie.getCodigoPkDetalle(), infoProgServ.getCodigoPkProgramaServicio(), dtoPrograma.getServicio()));
					dtoPlanTtoPresupuesto.setActivo(infoProgServ.isAsignadoAlPresupuesto());
					
					listaPlanTtoPresupuesto.add(dtoPlanTtoPresupuesto);
				}	
			}
			else
			{
				DtoPlanTratamientoPresupuesto dtoPlanTtoPresupuesto= new DtoPlanTratamientoPresupuesto();
				dtoPlanTtoPresupuesto.setDetPlanTratamiento(infoSuperficie.getCodigoPkDetalle());
				dtoPlanTtoPresupuesto.setFHU(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
				dtoPlanTtoPresupuesto.setPresupuesto(codigoPresupuesto);
				dtoPlanTtoPresupuesto.setServicio(infoProgServ.getCodigoPkProgramaServicio().intValue());
				dtoPlanTtoPresupuesto.setProgramaServicioPlanTratamientoFK(infoProgServ.getCodigoPKProgramasServiciosPlanTratamiento());
				dtoPlanTtoPresupuesto.setProgramaServicioPlanTratamientoFK(ValidacionesPresupuesto.obtenerCodigoPkProgramaServicioPlanTratamiento(infoSuperficie.getCodigoPkDetalle(), BigDecimal.ZERO/*infoProgServ.getCodigoPkProgramaServicio()*/, infoProgServ.getCodigoPkProgramaServicio().intValue()));
				dtoPlanTtoPresupuesto.setActivo(infoProgServ.isAsignadoAlPresupuesto());
				
				listaPlanTtoPresupuesto.add(dtoPlanTtoPresupuesto);
			}
		}
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionEliminar(	ActionMapping mapping,
											PresupuestoOdontologicoForm forma, 
											UsuarioBasico usuario,
											PersonaBasica paciente) throws IPSException 
	{
		logger.info("******************************************************************* POSICION A ELIMINAR " +forma.getPosArrayDetalle());
		//logger.info("*******************************************************************"+ UtilidadLog.obtenerStringHerencia(forma.getListaPresupuestos().get(forma.getPosArrayDetalle()), true));
		
		desmarcarProgramasServPlanTratamiento(forma);
		forma.getListaProgramasServicios().remove(forma.getPosArrayDetalle());
		
		actualizacionTotales(forma, usuario, paciente);
		
		if(forma.getModificando())
		{
			return mapping.findForward("paginaPrincipal");
		}
		return mapping.findForward("paginaDetalle");
	}

	/**
	 * @param forma
	 */
	private void desmarcarProgramasServPlanTratamiento(
			PresupuestoOdontologicoForm forma)
	{
		//antes de removerlos de la lista debemos verificar los que estan marcados en el plan de tratamineto, para poderlos activar para su seleccion
		logger.info("forma.getPosArrayDetalle()-->"+forma.getPosArrayDetalle());
		if(forma.getModificando())
		{
			for(InfoDetallePlanTramiento objPlan :   forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle())
			{
				 for(InfoHallazgoSuperficie objHallazgo : objPlan.getDetalleSuperficie())
				 {
					 for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
					 {
						 for(DtoPresupuestoPiezas dtoPresuPieza: forma.getListaProgramasServicios().get(forma.getPosArrayDetalle()).getListPresupuestoPiezas())
						 {
							//verificamos que sean la misma seccion - hallazgo - pieza - superficie
							 if(dtoPresuPieza.getSeccion().equals(ConstantesIntegridadDominio.acronimoDetalle)
								&& dtoPresuPieza.getHallazgo().intValue()==objHallazgo.getHallazgoREQUERIDO().getCodigo()
								&& dtoPresuPieza.getPieza().intValue()==objPlan.getPieza().getCodigo()
								&& dtoPresuPieza.getSuperficie().intValue()==objHallazgo.getSuperficieOPCIONAL().getCodigo())
							 {	 
								 boolean sonServicioPrograaIgual=false;
								 if(forma.getListaProgramasServicios().get(forma.getPosArrayDetalle()).getServicio().getCodigo()>0 &&
									(forma.getListaProgramasServicios().get(forma.getPosArrayDetalle()).getServicio().getCodigo()== objServProg.getCodigoPkProgramaServicio().intValue()))
								 {
									 sonServicioPrograaIgual=true;
								 }
								 else if(forma.getListaProgramasServicios().get(forma.getPosArrayDetalle()).getPrograma().getCodigo()>0 &&
										(forma.getListaProgramasServicios().get(forma.getPosArrayDetalle()).getPrograma().getCodigo().doubleValue()==objServProg.getCodigoPkProgramaServicio().doubleValue()))
								 {
									 sonServicioPrograaIgual=true;
								 }
								 
								 if(sonServicioPrograaIgual)
								 {
									 objServProg.setAsignadoAlPresupuesto(false);
								 }
							 }	 
						 }
					 }
				 }
			 }	 
			
			//// seccion otros
			for(InfoDetallePlanTramiento objPlan :   forma.getDtoPlanTratamiento().getSeccionOtrosHallazgos())
			{
				 for(InfoHallazgoSuperficie objHallazgo : objPlan.getDetalleSuperficie())
				 {
					 for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
					 {
						 for(DtoPresupuestoPiezas dtoPresuPieza: forma.getListaProgramasServicios().get(forma.getPosArrayDetalle()).getListPresupuestoPiezas())
						 {
							//verificamos que sean la misma seccion - hallazgo - pieza - superficie
							 if(dtoPresuPieza.getSeccion().equals(ConstantesIntegridadDominio.acronimoOtro)
								&& dtoPresuPieza.getHallazgo().intValue()==objHallazgo.getHallazgoREQUERIDO().getCodigo()
								&& dtoPresuPieza.getPieza().intValue()==objPlan.getPieza().getCodigo()
								&& (dtoPresuPieza.getSuperficie().intValue()==objHallazgo.getSuperficieOPCIONAL().getCodigo()
										|| dtoPresuPieza.getSuperficie().intValue()<=0 && objHallazgo.getSuperficieOPCIONAL().getCodigo()<=0))
							 {
								 boolean sonServicioPrograaIgual=false;
								 if(forma.getListaProgramasServicios().get(forma.getPosArrayDetalle()).getServicio().getCodigo()>0 &&
									(forma.getListaProgramasServicios().get(forma.getPosArrayDetalle()).getServicio().getCodigo()== objServProg.getCodigoPkProgramaServicio().intValue()))
								 {
									 sonServicioPrograaIgual=true;
								 }
								 else if(forma.getListaProgramasServicios().get(forma.getPosArrayDetalle()).getPrograma().getCodigo()>0 &&
										(forma.getListaProgramasServicios().get(forma.getPosArrayDetalle()).getPrograma().getCodigo().doubleValue()==objServProg.getCodigoPkProgramaServicio().doubleValue()))
								 {
									 sonServicioPrograaIgual=true;
								 }
								 if(sonServicioPrograaIgual)
								 {	 
									 objServProg.setAsignadoAlPresupuesto(false);	 
								 }	
							 }
						 }	 
					 }
				 }
			 }	 
			
			////seccion boca
			for(InfoHallazgoSuperficie objHallazgo : forma.getDtoPlanTratamiento().getSeccionHallazgosBoca())
			{
				 for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
				 {
					 for(DtoPresupuestoPiezas dtoPresuPieza: forma.getListaProgramasServicios().get(forma.getPosArrayDetalle()).getListPresupuestoPiezas())
					 {	 
						 //verificamos que sean la misma seccion - hallazgo - pieza - superficie
						 if(dtoPresuPieza.getSeccion().equals(ConstantesIntegridadDominio.acronimoBoca)
							&& dtoPresuPieza.getHallazgo().intValue()==objHallazgo.getHallazgoREQUERIDO().getCodigo())
						 {
							 boolean sonServicioPrograaIgual=false;
							 if(forma.getListaProgramasServicios().get(forma.getPosArrayDetalle()).getServicio().getCodigo()>0 &&
								(forma.getListaProgramasServicios().get(forma.getPosArrayDetalle()).getServicio().getCodigo()== objServProg.getCodigoPkProgramaServicio().intValue()))
							 {
								 sonServicioPrograaIgual=true;
							 }
							 else if(forma.getListaProgramasServicios().get(forma.getPosArrayDetalle()).getPrograma().getCodigo()>0 &&
									(forma.getListaProgramasServicios().get(forma.getPosArrayDetalle()).getPrograma().getCodigo().doubleValue()==objServProg.getCodigoPkProgramaServicio().doubleValue()))
							 {
								 sonServicioPrograaIgual=true;
							 }
							 if(sonServicioPrograaIgual)
							 {	 
								 objServProg.setAsignadoAlPresupuesto(false);	 
							 }
						 }	 
					 }	 
				 }
			}
		}
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionInsertarMotivoCancelacion(
			ActionMapping mapping, PresupuestoOdontologicoForm forma,
			UsuarioBasico usuario,
			int codigoPaciente) 
	{
		forma.setDtoPresupuesto( (DtoPresupuestoOdontologico)forma.getListaPresupuestos().get(forma.getPosArray()).clone());
		cargarMotivosAtencion(forma, usuario);
		forma.setListaCitasPresupuesto(PresupuestoOdontologico.cargarCitasPresupuestoOdontologico(codigoPaciente));
		forma.setListaMotivosCancelacionCita(UtilidadesConsultaExterna.obtenerMotivosCancelacion(ConstantesBD.acronimoSi, ConstantesBD.codigoEstadoCitaCanceladaPaciente+""));
		forma.setMotivoCancelacionCita(0);
		return mapping.findForward("modificarPresupuesto");
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarCancelarPrecontratado(
			ActionMapping mapping, PresupuestoOdontologicoForm forma,
			UsuarioBasico usuario)
	{
		cargarMotivosAtencion(forma, usuario);
		cargarMotivosAnulacionDcto(forma, usuario);
		forma.setCodigoMotivoAnulacionDctoSel(ValoresPorDefecto.getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologicoInt(usuario.getCodigoInstitucionInt()));
		return mapping.findForward("modificarPresupuesto");
	}


	/**
	 * @param forma
	 * @param usuario
	 */
	private void cargarMotivosAnulacionDcto(PresupuestoOdontologicoForm forma,
			UsuarioBasico usuario)
	{
		DtoMotivoDescuento motivo= new DtoMotivoDescuento();
		motivo.setTipo(ConstantesIntegridadDominio.acronimoEstadoAnulado);
		motivo.setInstitucion(usuario.getCodigoInstitucionInt());
		motivo.setActivo(ConstantesBD.acronimoSi);
		forma.setListaMotivosAnulacionDcto(MotivosDescuentos.cargar(motivo));
	}
	
	/**
	 * Metodo que Cargar los motivos de atencion 
	 * @param forma
	 * @param usuario
	 */
	private void cargarMotivosAtencion(PresupuestoOdontologicoForm forma,
			UsuarioBasico usuario)
	{
		forma.setListaMotivos(new ArrayList<DtoMotivosAtencion>());
		
		MotivosAtencionOdontologica motivo = new MotivosAtencionOdontologica();
		ArrayList<Integer> filtroEstados= new ArrayList<Integer>();
		filtroEstados.add(ConstantesBD.CancelarPresupuestoOdontologico);
		forma.setListaMotivos(motivo.consultarMotivoAtencionO(filtroEstados, usuario.getCodigoInstitucionInt()));
	}
	
	/**
	 * Accion insertar Motivos de cancelacion
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionInsertarMotivoSuspension(ActionMapping mapping,
			PresupuestoOdontologicoForm forma, UsuarioBasico usuario) 
	{
		
		/*
		 *1. Cargar el presupuesto Seleccionado 
		 */
		forma.setDtoPresupuesto( (DtoPresupuestoOdontologico)forma.getListaPresupuestos().get(forma.getPosArray()).clone());
		
		/*
		 * 2. Cargar la lista de motivos de suspencion
		 */
		forma.setListaMotivos(new ArrayList<DtoMotivosAtencion>());
		MotivosAtencionOdontologica motivo = new MotivosAtencionOdontologica();
		ArrayList<Integer> filtroEstados= new ArrayList<Integer>();
		filtroEstados.add(ConstantesBD.SuspenderTemporalmentePlandeTratamientoPresupuesto);
		/*
		 * 3. Settera lo motivos de suspencion presupuesto
		 */
		forma.setListaMotivos(motivo.consultarMotivoAtencionO(filtroEstados, usuario.getCodigoInstitucionInt()));
		
		
		
		return mapping.findForward("modificarPresupuesto");
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarModificarEstado(	ActionMapping mapping,
														PresupuestoOdontologicoForm forma, 
														UsuarioBasico usuario , 
														PersonaBasica paciente, 
														HttpServletRequest request,
														HttpServletResponse response) throws IPSException 
	{	
		Connection con = UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
	
		///SI EL ESTADO ES CANCELAR
		if(forma.getDtoPresupuesto().getEstado().equals(ConstantesIntegridadDominio.acronimoContratadoCancelado))
		{	
			//hacemos la reversion de los anticipos
			if(!PresupuestoOdontologico.reversarAnticiposPresupuestoContratado(con, forma.getDtoPresupuesto().getCodigoPK()))
			{
				logger.error("NO reversa Presupuesto");
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
			}
			//logger.info("NUEVO ************************************"+UtilidadLog.obtenerString(forma.getDtoPresupuesto(), true));
			forma.getDtoPresupuesto().setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
			PresupuestoOdontologico.modificarPresupuesto(forma.getDtoPresupuesto(), con);
			
			///si desean cancelar el presupuesto que esta en estado suspendido temporalmente entonces debemos actualizar el plan de tratamiento a estado en proceso
			if(forma.getListaPresupuestos().get(forma.getPosArray()).getEstado().equals(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente))
			{
				DtoPlanTratamientoOdo dtoWhere = new DtoPlanTratamientoOdo();
				dtoWhere.setCodigoPk(forma.getDtoPlanTratamiento().getCodigoPk());
				DtoPlanTratamientoOdo dtoNuevo = new DtoPlanTratamientoOdo();
				dtoNuevo.setEstado(ConstantesIntegridadDominio.acronimoEstadoEnProceso);
				//**********************************************
				PlanTratamiento.modificar(dtoWhere, dtoNuevo , con);
			}
			
			////SI EL ESTADO ANTERIOR ERA CONTRATADO O SUSPENDIDO TEMPORALMENTE 
			////ENTONCES SE DEBEN ACTUALIZAR LOS ESTADOS DE LOS PROGRAMAS - SERVICIOS 
			////DEL PLN TRATAMIENTO QEU ESTEN EN ESTADO CONTRATADO (COT) A ESTADO PENDIENTE (PEN)
			logger.info("ESTADO DTO-->xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxXXXXXXSSSSSSSSSSSSSSSSSSSSSSSSS"+forma.getDtoPresupuesto().getEstado()+" ESTADO LISTA-->"+((DtoPresupuestoOdontologico)forma.getListaPresupuestos().get(forma.getPosArray())).getEstado());
			
			if( forma.getListaPresupuestos().get(forma.getPosArray()).getEstado().equals(ConstantesIntegridadDominio.acronimoContratadoContratado)
				|| forma.getListaPresupuestos().get(forma.getPosArray()).getEstado().equals(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente))
			{
				ActionForward forward= cambiarEstadosProgServPlanTratamientoConPresupuesto(mapping, forma,usuario, request, con, ConstantesIntegridadDominio.acronimoEstadoPendiente);
				if(forward!=null)
				{
					return forward;
				}
			}
			
			//SI EL ESTADO ANTERIOR ERA CONTRATADO ENTONCES CANCELAMOS LAS CITAS
			if( forma.getListaPresupuestos().get(forma.getPosArray()).getEstado().equals(ConstantesIntegridadDominio.acronimoContratadoContratado))
			{
				if(!PresupuestoOdontologico.cancelarCitasPresupuesto(con, forma.getListaCitasPresupuesto(), forma.getMotivoCancelacionCita(), usuario, paciente.getCodigoPersona(), paciente.getCodigoIngreso()))
				{
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
				}
			}
		}
		
		// SI EL ESTADO ES SUSPENDER TEMPORALMENET CAMBIANDO DE ESTADO AL SUT 
		else if(forma.getDtoPresupuesto().getEstado().equals(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente))
		{
			//logger.info("NUEVO ************************************"+UtilidadLog.obtenerString(forma.getDtoPresupuesto(), true));
			forma.getDtoPresupuesto().setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
			PresupuestoOdontologico.modificarPresupuesto(forma.getDtoPresupuesto(), con);
			
			DtoPlanTratamientoOdo dtoWhere = new DtoPlanTratamientoOdo();
			dtoWhere.setCodigoPk(forma.getDtoPlanTratamiento().getCodigoPk());		
			//**********************************************
			DtoPlanTratamientoOdo dtoNuevo = new DtoPlanTratamientoOdo();
			dtoNuevo.setEstado(ConstantesIntegridadDominio.acronimoSuspendidoTemporalmente);
			dtoNuevo.setMotivo(forma.getDtoPresupuesto().getMotivo().getCodigo());
			//**********************************************
			PlanTratamiento.modificar(dtoWhere, dtoNuevo , con);
		}	
		// SI EL ESTADO ES CONTRATADO
		else if(forma.getDtoPresupuesto().getEstado().equals(ConstantesIntegridadDominio.acronimoContratadoContratado))
		{
			//logger.info("NUEVO ************************************"+UtilidadLog.obtenerString(forma.getDtoPresupuesto(), true));
			forma.getDtoPresupuesto().setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
			PresupuestoOdontologico.modificarPresupuesto(forma.getDtoPresupuesto(), con);
			
			DtoPlanTratamientoOdo dtoWhere = new DtoPlanTratamientoOdo();
			dtoWhere.setCodigoPk(forma.getDtoPlanTratamiento().getCodigoPk());		
			//**********************************************
			DtoPlanTratamientoOdo dtoNuevo = new DtoPlanTratamientoOdo();
			dtoNuevo.setEstado(ConstantesIntegridadDominio.acronimoEstadoActivo);
			dtoNuevo.setMotivo(forma.getDtoPresupuesto().getMotivo().getCodigo());
			//**********************************************
			PlanTratamiento.modificar(dtoWhere, dtoNuevo , con);
		}			
		
		DtoLogPresupuestoOdontologico dtoLog = llenarDtoLog(forma);
		
		 
	/*	if(PresupuestoOdontologico.guardarLogPresupuesto(dtoLog , con) <= 0)
		{
			logger.error("NO Presupuesto");
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			////hacer error
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
			
		}*/
        
		
		logger.info("modifica  100%%%");
		
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		
		forma.setEstado("empezar");
		return accionEmpezar(mapping, forma, usuario, paciente, request, false /*crear bloqueo ya existe*/, response);
	}

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param con
	 */
	private ActionForward cambiarEstadosProgServPlanTratamientoConPresupuesto(	ActionMapping mapping, 
																				PresupuestoOdontologicoForm forma,
																				UsuarioBasico usuario, 
																				HttpServletRequest request, 
																				Connection con,
																				String estadoProgramasServPlanTratamiento) {
		//debemos cargar la lista de programas-servicios del presupuesto
		forma.getListaPresupuestos().get(forma.getPosArray()).setDtoPresuOdoProgServ(PresupuestoOdontologico.cargarPresupuestoOdoProgServ(forma.getDtoPresupuesto().getCodigoPK()));
		
		for(DtoPresuOdoProgServ dtop: forma.getListaPresupuestos().get(forma.getPosArray()).getDtoPresuOdoProgServ())
		{	
			dtop.setListPresupuestoOdoConvenio(PresupuestoOdontologico.cargarPresupuestoConvenio(dtop.getCodigoPk()));
			dtop.setListPresupuestoPiezas(PresupuestoOdontologico.cargarPresupuestoPieza(dtop.getCodigoPk(), false, forma.getUtilizaPrograma()));
			
			for(DtoPresupuestoOdoConvenio dtoConvenio: dtop.getListPresupuestoOdoConvenio())
			{	
				if(dtoConvenio.getContratado().equals(ConstantesBD.acronimoSi))
				{	
					for(DtoPresupuestoPiezas dtoPiezas: dtop.getListPresupuestoPiezas())
					{	
						int codigoDetallePlanT= PresupuestoOdontologico.existeProgramaServicioPresupuestoEnPlanTratamiento(dtoPiezas.getPieza().intValue(), dtoPiezas.getSuperficie().intValue(), dtoPiezas.getHallazgo().intValue(), new BigDecimal(dtop.getProgramaOServicio(forma.getUtilizaPrograma())), forma.getUtilizaPrograma(), forma.getDtoPresupuesto().getPlanTratamiento(), ConstantesIntegridadDominio.acronimoContratado);
						if(codigoDetallePlanT>0)
						{	
							DtoProgramasServiciosPlanT newPrograma = new DtoProgramasServiciosPlanT();											 		          
							newPrograma.setDetPlanTratamiento(new BigDecimal(codigoDetallePlanT));
							if(forma.getUtilizaPrograma())
							{
								newPrograma.setPrograma( dtop.getPrograma());
							}
							else
							{	
								newPrograma.setServicio( dtop.getServicio());
							}	
							newPrograma.setEstadoPrograma(estadoProgramasServPlanTratamiento);
							newPrograma.setEstadoServicio(estadoProgramasServPlanTratamiento);
							newPrograma.setPorConfirmado(ConstantesBD.acronimoNo);
							newPrograma.setActivo(ConstantesBD.acronimoSi);
							newPrograma.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
							
							if (!PlanTratamiento.modicarEstadosDetalleProgServ(newPrograma , con))
							{
								logger.error("NO PASA POR MDIFICAR SOLO CONTRATADOS");
								UtilidadBD.abortarTransaccion(con);
								UtilidadBD.closeConnection(con);
								return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
							}
						}	
					}	
				}	
			}	
		}
		return null;
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCopiar(	ActionMapping mapping,
										PresupuestoOdontologicoForm forma, 
										UsuarioBasico usuario, 
										PersonaBasica paciente,
										HttpServletRequest request,
										HttpServletResponse response) throws IPSException 
	{
		forma.setPuedoEliminar(true);
		forma.setModificando(true);
		forma.setEsNuevo(true);
		forma.resetPaquetes();
		
		logger.info("*******************************************************************************");
		logger.info("------------------------------PRESUPUESTO DETALLE COPIAAAAAAAAAAAA----------------------------- ");
		logger.info(" pos- "+forma.getPosArray());
		
		forma.setDtoPresupuesto(forma.getListaPresupuestos().get(forma.getPosArray()));
		
		/*
		 * Se actualiza el centro de atención para permitir la contratación si se está
		 * copiando en un centro de atención diferente al inicial.
		 */
		forma.getDtoPresupuesto().setCentroAtencion(new InfoDatosInt(usuario.getCodigoCentroAtencion()));
		//debe ir este reset despues del cargado del dto
		forma.resetPaquetesConvenios();
		forma.setListaPaquetes(new ArrayList<InfoPaquetesPresupuesto>());
		
		//logger.info(UtilidadLog.obtenerString("DTO A CARGAR DETALLE***************"+forma.getDtoPresupuesto(), true));
		
		//"SE CARGAN LOS PROGRAMAS/SERVICIOS
		forma.setListaProgramasServicios(PresupuestoOdontologico.cargarPresupuestoOdoProgServ(forma.getDtoPresupuesto().getCodigoPK()));
		//"SE CARGAN LOS  CONVENIOS
		
		//3. CARGAMOS EL PLAN TRATAMIENTO DEL PRESUPUESTO ALMACENADO
		ActionForward forward= cargarPlanTratamiento(forma, paciente, request, mapping, forma.getDtoPresupuesto().getCodigoPK(), response, request.getSession().getId(), usuario.getCodigoInstitucionInt());
		if(forward!=null)
		{
			return forward;
		}
		
		//debe ir este reset despues del cargado del dto
		forma.resetPaquetesConvenios();
		forma.setListaPaquetes(new ArrayList<InfoPaquetesPresupuesto>());
		// agrupamos las cantidades que posiblemente habian quedado con cantidad 1 pero ya no tienen paquetes y queden agrupadas en cantidades
		PresupuestoPaquetes.agruparCantidades(forma.getListaProgramasServicios());
		
		
		for(DtoPresuOdoProgServ dtop : forma.getListaProgramasServicios())
		{
			dtop.setListPresupuestoOdoConvenio(PresupuestoOdontologico.cargarPresupuestoConvenio(dtop.getCodigoPk()));
			dtop.setListPresupuestoPiezas(PresupuestoOdontologico.cargarPresupuestoPieza(dtop.getCodigoPk(), false, forma.getUtilizaPrograma()));
			//logger.info("LA LISTA QUE CARGA ES **********************************"+"********"+UtilidadLog.obtenerString(dtop, true) + dtop.getListPresupuestoOdoConvenio().size());
			
			//DEBEMOS RECALCULAR LOS CARGOS DEL PRESUPUESTO X SERVICIO/PROGRAMA - CONVENIO
			for(DtoPresupuestoOdoConvenio detalleConvenioServPrograma: dtop.getListPresupuestoOdoConvenio())
			{	
				
				//metodo de wilson para calcular totales
				
				InfoPresupuestoXConvenioProgramaServicio infoPresupuesto=CargosOdon.obtenerPresupuestoXConvenio(
																													dtop.getServicio().getCodigo(), 
																													dtop.getPrograma().getCodigo(), 
																													dtop.getCantidad(), 
																													detalleConvenioServPrograma.getConvenio().getCodigo(), 
																													//CargosOdon.obtenerContrato(forma.getDtoPresupuesto().getIngreso().intValue(), detalleConvenioServPrograma.getConvenio().getCodigo()) ,
																													detalleConvenioServPrograma.getContrato().getCodigo(),
																													UtilidadFecha.getFechaActual()/*fechaCalculoVigencia*/, 
																													usuario.getCodigoInstitucionInt(),  
																													forma.getDtoPresupuesto().getCuenta());				
				//logger.info("EL INFO PRESUPUESTO DE ESTE CONVENIO ES ***************************************************************************************************************************************************************************************************************** " + UtilidadLog.obtenerStringHerencia(infoPresupuesto, true));
				//
				detalleConvenioServPrograma.setValorUnitario(infoPresupuesto.getValorUnitarioProgramaServicioConvenio());
				detalleConvenioServPrograma.setErrorCalculoTarifa(infoPresupuesto.getErroresTotalesStr("<br>"));
				detalleConvenioServPrograma.setAdvertenciaPromocion(infoPresupuesto.getDetallePromocionDescuento().getAdvertencia());
				detalleConvenioServPrograma.setDescuentoComercialUnitario(infoPresupuesto.getValorUnitarioDctoComercial());
				detalleConvenioServPrograma.setPorcentajeHonorarioPromocion(infoPresupuesto.getDetallePromocionDescuento().getPorcentajeHonorario());
				detalleConvenioServPrograma.setValorHonorarioPromocion(infoPresupuesto.getDetallePromocionDescuento().getValorHonorario());
				detalleConvenioServPrograma.setValorDescuentoPromocion(infoPresupuesto.getDetallePromocionDescuento().getValorPromocion());
				detalleConvenioServPrograma.setSerialBono(infoPresupuesto.getDetalleBonoDescuento().getSerial());
				detalleConvenioServPrograma.setValorDescuentoBono(infoPresupuesto.getDetalleBonoDescuento().getValorDctoCALCULADO());
				detalleConvenioServPrograma.setAdvertenciaBono(infoPresupuesto.getDetalleBonoDescuento().getAdvertencia());
				detalleConvenioServPrograma.setDetallePromocion(infoPresupuesto.getDetallePromocionDescuento().getDetPromocion());
				detalleConvenioServPrograma.setContratado(ConstantesBD.acronimoNo);
				
				detalleConvenioServPrograma.setSeleccionadoPorcentajeBono(infoPresupuesto.getDetalleBonoDescuento().isSeleccionadoPorcentaje());
				detalleConvenioServPrograma.setSeleccionadoPorcentajePromocion(infoPresupuesto.getDetallePromocionDescuento().isSeleccionadoPorcentaje());
				
				if(forma.getUtilizaPrograma())
				{
					IPresupuestoOdontologicoServicio presupuestoOdontologicoServicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
					
					detalleConvenioServPrograma.setListaDetalleServiciosPrograma(
							presupuestoOdontologicoServicio.cargarListaServiciosProgramaTarifas(infoPresupuesto.getDetalleTarifasServicio(), usuario.getLoginUsuario(),  dtop.getPrograma().getCodigo()));

				}
				
				detalleConvenioServPrograma.setDtoPresupuestoPaquete(new DtoPresupuestoPaquetes());
			}	
		}
		
		// SE CARGAN LOS TOTALES
		forma.setListaSumatoriaConvenios(PresupuestoOdontologico.cargarTotalesPresupuetoConvenio(forma.getDtoPresupuesto().getCodigoPK()));
		
		actualizacionTotales(forma, usuario, paciente);
		marcarServicioProgramasYaSeleccionadosPresupuesto(forma);
		
		return mapping.findForward("paginaPrincipal");
	}
	
	 /**
	  * 
	  * @param forma
	  */
	 private ActionForward accionCargarPresupuestoConPlanTratamiento(ActionMapping mapping, PresupuestoOdontologicoForm forma, UsuarioBasico usuario, PersonaBasica paciente) throws IPSException
	 {
		 logger.info("\n\n\n*****************************************EMPEZAMOS A CARGAR PRESUPUESTO DESDE EL PLAN DE TRATAMIENTO**************************");
		 forma.resetDetallePresupuesto();
		 
		 extraerProgramasServiciosPlanAlPresupuesto(forma, usuario);
		 ////OTROS HALLAZGOS
		 extraerProgramasServiciosPlanAlPresupuestoSeccionOtros(forma, usuario);
		 ///////HALLAZGOS BOCA
		 extraerProgramasServiciosPlanAlPresupuestoSeccionBoca(forma, usuario);

		 ////hacemos el calculo de las cantidades dependiendo del numero de superficies
		 //calcularCantidadesXNumeroSuperficies(forma); ANTES SE LLAMABA ASÍ
		 
		 IPresupuestoOdontologicoServicio presupuestoOdontologicoServicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
		 presupuestoOdontologicoServicio.calcularCantidadesXNumeroSuperficies(forma.getListaProgramasServicios(), forma.getUtilizaPrograma(), false);
		 
		 ////ahora que ya tenemos el presupuesto, debemos hacer el calculo de las tarifas ENVIANDOLE LA CANTIDAD QUE YA LA TENEMOS AGRUPADA
		 /////cargar el detalle de los convenios
		 logger.info("EL NUMERO DE SERV/PROG AGRUPADOS SON:::"+forma.getListaProgramasServicios().size());
		 
		 
		 // calcularTarifas(forma, usuario, paciente);	 
		 
		 presupuestoOdontologicoServicio.calcularTarifas(forma.getListaProgramasServicios(), forma.getListaConvenioPresupuesto(), 
				 usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt(), paciente.getCodigoCuenta(), forma.getUtilizaPrograma());

		 logger.info("llenamos el dtoPresupuesto!!!!!");

		 forma.setDtoPresupuesto(presupuestoOdontologicoServicio.crearEncabezadoPresupuesto(usuario, paciente, forma.getDtoPlanTratamiento().getCodigoPk()));
		 forma.getDtoPresupuesto().setDtoPresuOdoProgServ(forma.getListaProgramasServicios());
		 
		 //crearValoresTotalesXConvenio(forma, usuario, paciente);
		 
		 forma.setListaSumatoriaConvenios(presupuestoOdontologicoServicio.crearValoresTotalesXConvenio(forma.getListaProgramasServicios(), forma.getListaConvenioPresupuesto()));
		 
		 forma.setPuedoEliminar(true);
		 forma.setModificando(true);
		 forma.setEsNuevo(true);
		 
		 ///hacemos el ordenamiento x especialidad
		 Collections.sort(forma.getListaProgramasServicios());
		 return mapping.findForward("paginaPrincipal");
	 }

	 /**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionRecalcularPresupuestoGenerado(
			ActionMapping mapping, PresupuestoOdontologicoForm forma,
			UsuarioBasico usuario, PersonaBasica paciente) throws IPSException 
	{
		for(InfoDetallePlanTramiento objPlan :   forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle())
		{
			 for(InfoHallazgoSuperficie objHallazgo : objPlan.getDetalleSuperficie())
			 {
				 for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
				 {
					 if(objServProg.isAsignadoAlPresupuesto())
					 {
						 objServProg.setAsignadoAlPresupuesto(false);
						 objServProg.setActivo(true);
					 }
				 }
			 }
		 }
		for(InfoDetallePlanTramiento objPlan :   forma.getDtoPlanTratamiento().getSeccionOtrosHallazgos())
		{
			 for(InfoHallazgoSuperficie objHallazgo : objPlan.getDetalleSuperficie())
			 {
				 for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
				 {
					 logger.info("servicio/programa activo--->"+objServProg.getActivo()+" --> "+objServProg.getNombreProgramaServicio());
					 if(objServProg.isAsignadoAlPresupuesto())
					 {
						 objServProg.setAsignadoAlPresupuesto(false);
						 objServProg.setActivo(true);
					 }
				 }
			 }
		 }	
		for(InfoHallazgoSuperficie objHallazgo : forma.getDtoPlanTratamiento().getSeccionHallazgosBoca())
		 {
			 for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
			 {
				 logger.info("servicio/programa activo--->"+objServProg.getActivo()+" --> "+objServProg.getNombreProgramaServicio());
				 if(objServProg.isAsignadoAlPresupuesto())
				 {
					 objServProg.setAsignadoAlPresupuesto(false);
					 objServProg.setActivo(true);
				 }
			 }
		 }
		return this.accionCargarPresupuestoConPlanTratamiento(mapping, forma, usuario, paciente);
	}


//	 /**
//	 * @param forma
//	 * @param usuario
//	 * @param paciente
//	 */
//	private void crearValoresTotalesXConvenio (PresupuestoOdontologicoForm forma, UsuarioBasico usuario, PersonaBasica paciente)
//	{
//		//ACTUALIZACION DE LOS VALORES TOTALES X CONVENIO
//		 ArrayList<DtoPresupuestoTotalConvenio> arrayTotales= new ArrayList<DtoPresupuestoTotalConvenio>();
//		 for(InfoConvenioContratoPresupuesto infoConvenio:  forma.getListaConvenioPresupuesto())
//		 {
//			 if(infoConvenio.getActivo())
//			 {
//				 DtoPresupuestoTotalConvenio dtoTotalesXConvenio= new DtoPresupuestoTotalConvenio();
//				 logger.info("INICIO CONVENIO---------------------->"+infoConvenio.getConvenio().getCodigo());
//				 BigDecimal valorSubTotal= new BigDecimal(0);
//				 BigDecimal valorSubTotalSinInclusiones= new BigDecimal(0);
//				 for(DtoPresuOdoProgServ dtop : forma.getListaProgramasServicios())
//				 {
//					 for(DtoPresupuestoOdoConvenio detalleConvenioServPrograma: dtop.getListPresupuestoOdoConvenio())
//					 {
//						 if(infoConvenio.getConvenio().getCodigo()==detalleConvenioServPrograma.getConvenio().getCodigo())
//						 {
//							 logger.info("val unitario externo-->"+detalleConvenioServPrograma.getValorUnitario()+ "  cantidad-->"+ dtop.getCantidad());
//							 if(detalleConvenioServPrograma.getValorTotalConvenioXProgServ(dtop.getCantidad()).doubleValue()>0)
//							 {
//								 logger.info(" antes entra!!! valorSubTotal-->"+valorSubTotal+ "    valor unitario a sumar-->"+detalleConvenioServPrograma.getValorUnitario()+" *  cantidad ->"+dtop.getCantidad());
//								 valorSubTotal= valorSubTotal.add(detalleConvenioServPrograma.getValorTotalConvenioXProgServ(dtop.getCantidad()));
//								 logger.info(" despues entra!!! valorSubTotal-->"+valorSubTotal);
//								 if(dtop.getInclusion()!=null && dtop.getInclusion().intValue()<=0)
//								 {
//									 valorSubTotalSinInclusiones=valorSubTotalSinInclusiones.add(detalleConvenioServPrograma.getValorTotalConvenioXProgServ(dtop.getCantidad()));
//								 }
//							 }
//						 }
//					 }
//				 }
//				 
//				  //dtoTotalesXConvenio.setMensajeDescuento(infoDcto.getMensaje());
//				 //dtoTotalesXConvenio.setValorDescuento(infoDcto.getValorDctoCALCULADO());
//				 
//				 logger.info("FINALES--------------------------------------");
//				 logger.info("---------------------------------------------");
//				 dtoTotalesXConvenio.setSubTotalContratadoSinDescuentos(valorSubTotalSinInclusiones);
//				 dtoTotalesXConvenio.setValorSubTotalSinContratar(valorSubTotal);
//				 dtoTotalesXConvenio.setValorSubTotalContratado(new BigDecimal(0));
//				 dtoTotalesXConvenio.setConvenio(infoConvenio.getConvenio());
//				 dtoTotalesXConvenio.setContrato(infoConvenio.getContrato().getCodigo());
//				 dtoTotalesXConvenio.setPresupuesto(new BigDecimal(0));
//				 
//				 arrayTotales.add(dtoTotalesXConvenio);
//			 }	 
//		 }
//		 
//		 forma.setListaSumatoriaConvenios(arrayTotales);
//	}

//	/**
//	 * @param forma
//	 * @param usuario
//	 * @param paciente
//	 * @param arrayServProg
//	 */
//	private void crearEncabezadoPresupuesto(PresupuestoOdontologicoForm forma,
//			UsuarioBasico usuario, PersonaBasica paciente
//			)
//	{
//		 DtoPresupuestoOdontologico dtoPresupuesto= new DtoPresupuestoOdontologico();
//		 dtoPresupuesto.setCentroAtencion(new InfoDatosInt(usuario.getCodigoCentroAtencion(), usuario.getCentroAtencion()));
//		 dtoPresupuesto.setCodigoPaciente(new InfoDatosInt(paciente.getCodigoPersona(), paciente.getNombrePersona()));
//		 dtoPresupuesto.setConsecutivo(new BigDecimal(0));
//		 dtoPresupuesto.setCuenta(new BigDecimal(paciente.getCodigoCuenta()));
//		 dtoPresupuesto.setDtoPresuOdoProgServ(forma.getListaProgramasServicios());
//		 
//		 if(usuario.getEspecialidades().length>0)
//			 dtoPresupuesto.setEspecialidad(new InfoDatosInt(usuario.getEspecialidades()[0].getCodigo()));
//		 
//		 dtoPresupuesto.setEstado(ConstantesIntegridadDominio.acronimoEstadoActivo);
//		 dtoPresupuesto.setFechaUsuarioGenera( new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
//		 dtoPresupuesto.setIngreso(new BigDecimal(paciente.getCodigoIngreso()));
//		 dtoPresupuesto.setInstitucion(usuario.getCodigoInstitucionInt());
//		 dtoPresupuesto.setPlanTratamiento(forma.getDtoPlanTratamiento().getCodigoPk());
//		 dtoPresupuesto.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
//		 
//		 forma.setDtoPresupuesto(dtoPresupuesto);
//	}

//	/**
//	 * @param forma
//	 * @param usuario
//	 * @param paciente
//	 * @param arrayServProg
//	 */
//	private void calcularTarifas(PresupuestoOdontologicoForm forma, UsuarioBasico usuario, PersonaBasica paciente)
//	{
//		String fechaCalculoTarifa=UtilidadFecha.getFechaActual();
//		
//		for(DtoPresuOdoProgServ dtoPresuServPrograma: forma.getListaProgramasServicios())
//		{
//			if(dtoPresuServPrograma.getTipoModificacion()==EnumTipoModificacion.NUEVO
//					 || dtoPresuServPrograma.getTipoModificacion()==EnumTipoModificacion.MODIFICADO)
//			{
//				ArrayList<DtoPresupuestoOdoConvenio> arrayConvenio= new ArrayList<DtoPresupuestoOdoConvenio>();
//				logger.info("numero de convenios-->"+forma.getListaConvenioPresupuesto().size());
//				for(InfoConvenioContratoPresupuesto infoConvenio:  forma.getListaConvenioPresupuesto())
//				{
//					if(infoConvenio.getActivo())
//					{
//						DtoPresupuestoOdoConvenio detalleConvenioServPrograma= new DtoPresupuestoOdoConvenio();
//					 	InfoPresupuestoXConvenioProgramaServicio infoPresupuesto= CargosOdon.obtenerPresupuestoXConvenio(dtoPresuServPrograma.getServicio().getCodigo(), dtoPresuServPrograma.getPrograma().getCodigo().doubleValue(), dtoPresuServPrograma.getCantidad(), infoConvenio.getConvenio().getCodigo(), infoConvenio.getContrato().getCodigo(), fechaCalculoTarifa, usuario.getCodigoInstitucionInt(), new BigDecimal(paciente.getCodigoCuenta()));
//					 	detalleConvenioServPrograma.setValorUnitario(infoPresupuesto.getValorUnitarioProgramaServicioConvenio());
//						detalleConvenioServPrograma.setErrorCalculoTarifa(infoPresupuesto.getErroresTotalesStr("<br>"));
//						detalleConvenioServPrograma.setAdvertenciaPromocion(infoPresupuesto.getDetallePromocionDescuento().getAdvertencia());
//						detalleConvenioServPrograma.setDescuentoComercialUnitario(infoPresupuesto.getValorUnitarioDctoComercial());
//						detalleConvenioServPrograma.setPorcentajeHonorarioPromocion(infoPresupuesto.getDetallePromocionDescuento().getPorcentajeHonorario());
//						detalleConvenioServPrograma.setValorHonorarioPromocion(infoPresupuesto.getDetallePromocionDescuento().getValorHonorario());
//						detalleConvenioServPrograma.setValorDescuentoPromocion(infoPresupuesto.getDetallePromocionDescuento().getValorPromocion());
//						detalleConvenioServPrograma.setPorcentajePromocion(infoPresupuesto.getDetallePromocionDescuento().getPorcentajePromocion());
//						detalleConvenioServPrograma.setSerialBono(infoPresupuesto.getDetalleBonoDescuento().getSerial());
//						detalleConvenioServPrograma.setValorDescuentoBono(infoPresupuesto.getDetalleBonoDescuento().getValorDctoCALCULADO());
//						detalleConvenioServPrograma.setPorcentajeDctoBono(infoPresupuesto.getDetalleBonoDescuento().getPorcentajeDescuento());
//						detalleConvenioServPrograma.setAdvertenciaBono(infoPresupuesto.getDetalleBonoDescuento().getAdvertencia());
//						detalleConvenioServPrograma.setConvenio(infoConvenio.getConvenio());
//						detalleConvenioServPrograma.setContrato(infoConvenio.getContrato());
//						detalleConvenioServPrograma.setDetallePromocion(infoPresupuesto.getDetallePromocionDescuento().getDetPromocion());
//						
//						detalleConvenioServPrograma.setSeleccionadoPorcentajeBono(infoPresupuesto.getDetalleBonoDescuento().isSeleccionadoPorcentaje());
//						detalleConvenioServPrograma.setSeleccionadoPorcentajePromocion(infoPresupuesto.getDetallePromocionDescuento().isSeleccionadoPorcentaje());
//						
//						logger.info("sele prom-->"+detalleConvenioServPrograma.isSeleccionadoPorcentajePromocion());
//						logger.info("seteamos las tarifas!!!!!!!!!!");
//						
//						//si existe valor descuento x bono y x promocion entonces se debe postular la de mayor valor
//						if(detalleConvenioServPrograma.getValorDescuentoBono().doubleValue()>0 && detalleConvenioServPrograma.getValorDescuentoPromocion().doubleValue()>0)
//						{
//							if(detalleConvenioServPrograma.getValorDescuentoBono().doubleValue()>detalleConvenioServPrograma.getValorDescuentoPromocion().doubleValue())
//							{	
//								detalleConvenioServPrograma.setSeleccionadoBono(true);
//								detalleConvenioServPrograma.setSeleccionadoPromocion(false);
//							}
//							else
//							{	
//								detalleConvenioServPrograma.setSeleccionadoPromocion(true);
//								detalleConvenioServPrograma.setSeleccionadoBono(false);
//							}	
//						}
//						else
//						{
//							detalleConvenioServPrograma.setSeleccionadoBono(detalleConvenioServPrograma.getValorDescuentoBono().doubleValue()>0);
//							detalleConvenioServPrograma.setSeleccionadoPromocion(detalleConvenioServPrograma.getValorDescuentoPromocion().doubleValue()>0);
//						}
//						
//						logger.info("sele prom1-->"+detalleConvenioServPrograma.isSeleccionadoPorcentajePromocion());
//						//DEBEMOS SETEAR LOS DETALLES DE LOS SERVICIOS DEL PROGRAMA
//						if(forma.getUtilizaPrograma())
//						{
//							detalleConvenioServPrograma.setListaDetalleServiciosPrograma(cargarListaServiciosProgramaTarifas(infoPresupuesto.getDetalleTarifasServicio(), usuario, dtoPresuServPrograma.getPrograma().getCodigo()));
//						}
//						
//						logger.info("sele prom 2-->"+detalleConvenioServPrograma.isSeleccionadoPorcentajePromocion());
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("logger ->"+detalleConvenioServPrograma.getLogPromocion());
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						logger.info("=============================================================================================================");
//						
//						logger.info("sele prom 3-->"+detalleConvenioServPrograma.isSeleccionadoPorcentajePromocion());
//						
//						arrayConvenio.add(detalleConvenioServPrograma);
//					}
//				}
//				logger.info("seteamos las tarifas finales!!!!!!!!!!");
//				dtoPresuServPrograma.setListPresupuestoOdoConvenio(arrayConvenio);
//			}
//		}
//	}

	
	/**
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param arrayServProg
	 */
	private void calcularTarifasPendientes(	PresupuestoOdontologicoForm forma,
											UsuarioBasico usuario, 
											PersonaBasica paciente) throws IPSException
	{
		String fechaCalculoTarifa=UtilidadFecha.getFechaActual();
		
		for(DtoPresuOdoProgServ dtoPresuServPrograma: forma.getListaProgramasServicios())
		{
			for(DtoPresupuestoOdoConvenio detalleConvenioServPrograma: dtoPresuServPrograma.getListPresupuestoOdoConvenio())
			{
				//SI EXISTE ERROR ENTONCES QUE ME RECALCULE LA TARIFA
				if(!UtilidadTexto.isEmpty(detalleConvenioServPrograma.getErrorCalculoTarifa()))
				{	
					
					///CON ESTE INDICATIVO SABEMOS SI LO DEBEMOS GUARDAR A NIVEL DEL DETALLE DE ESE PROGRAMA/SERVICIO CONVENIO
					detalleConvenioServPrograma.setTipoModificacionCONVENIO(EnumTipoModificacion.RECALCULADA_TARIFA);
					
					InfoPresupuestoXConvenioProgramaServicio infoPresupuesto= CargosOdon.obtenerPresupuestoXConvenio(dtoPresuServPrograma.getServicio().getCodigo(), dtoPresuServPrograma.getPrograma().getCodigo().doubleValue(), dtoPresuServPrograma.getCantidad(), detalleConvenioServPrograma.getConvenio().getCodigo(), detalleConvenioServPrograma.getContrato().getCodigo(), fechaCalculoTarifa, usuario.getCodigoInstitucionInt(), new BigDecimal(paciente.getCodigoCuenta()));
				 	detalleConvenioServPrograma.setValorUnitario(infoPresupuesto.getValorUnitarioProgramaServicioConvenio());
					detalleConvenioServPrograma.setErrorCalculoTarifa(infoPresupuesto.getErroresTotalesStr("<br>"));
					detalleConvenioServPrograma.setAdvertenciaPromocion(infoPresupuesto.getDetallePromocionDescuento().getAdvertencia());
					detalleConvenioServPrograma.setDescuentoComercialUnitario(infoPresupuesto.getValorUnitarioDctoComercial());
					detalleConvenioServPrograma.setPorcentajeHonorarioPromocion(infoPresupuesto.getDetallePromocionDescuento().getPorcentajeHonorario());
					detalleConvenioServPrograma.setValorHonorarioPromocion(infoPresupuesto.getDetallePromocionDescuento().getValorHonorario());
					detalleConvenioServPrograma.setValorDescuentoPromocion(infoPresupuesto.getDetallePromocionDescuento().getValorPromocion());
					detalleConvenioServPrograma.setPorcentajePromocion(infoPresupuesto.getDetallePromocionDescuento().getPorcentajePromocion());
					detalleConvenioServPrograma.setSerialBono(infoPresupuesto.getDetalleBonoDescuento().getSerial());
					detalleConvenioServPrograma.setValorDescuentoBono(infoPresupuesto.getDetalleBonoDescuento().getValorDctoCALCULADO());
					detalleConvenioServPrograma.setPorcentajeDctoBono(infoPresupuesto.getDetalleBonoDescuento().getPorcentajeDescuento());
					detalleConvenioServPrograma.setAdvertenciaBono(infoPresupuesto.getDetalleBonoDescuento().getAdvertencia());
					detalleConvenioServPrograma.setDetallePromocion(infoPresupuesto.getDetallePromocionDescuento().getDetPromocion());
					
					detalleConvenioServPrograma.setSeleccionadoPorcentajeBono(infoPresupuesto.getDetalleBonoDescuento().isSeleccionadoPorcentaje());
					detalleConvenioServPrograma.setSeleccionadoPorcentajePromocion(infoPresupuesto.getDetallePromocionDescuento().isSeleccionadoPorcentaje());
					
					logger.info("seteamos las tarifas!!!!!!!!!!");
					
					//si existe valor descuento x bono y x promocion entonces se debe postular la de mayor valor
					if(detalleConvenioServPrograma.getValorDescuentoBono().doubleValue()>0 && detalleConvenioServPrograma.getValorDescuentoPromocion().doubleValue()>0)
					{
						if(detalleConvenioServPrograma.getValorDescuentoBono().doubleValue()>detalleConvenioServPrograma.getValorDescuentoPromocion().doubleValue())
						{	
							detalleConvenioServPrograma.setSeleccionadoBono(true);
							detalleConvenioServPrograma.setSeleccionadoPromocion(false);
						}
						else
						{	
							detalleConvenioServPrograma.setSeleccionadoPromocion(true);
							detalleConvenioServPrograma.setSeleccionadoBono(false);
						}	
					}
					else
					{
						detalleConvenioServPrograma.setSeleccionadoBono(detalleConvenioServPrograma.getValorDescuentoBono().doubleValue()>0);
						detalleConvenioServPrograma.setSeleccionadoPromocion(detalleConvenioServPrograma.getValorDescuentoPromocion().doubleValue()>0);
					}
					
					//DEBEMOS SETEAR LOS DETALLES DE LOS SERVICIOS DEL PROGRAMA
					if(forma.getUtilizaPrograma())
					{
						IPresupuestoOdontologicoServicio presupuestoOdontologicoServicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
						
						detalleConvenioServPrograma.setListaDetalleServiciosPrograma(
								presupuestoOdontologicoServicio.cargarListaServiciosProgramaTarifas(infoPresupuesto.getDetalleTarifasServicio(), usuario.getLoginUsuario(),  dtoPresuServPrograma.getPrograma().getCodigo()));
					}
				 }	
			 }
			 logger.info("seteamos las tarifas finales!!!!!!!!!!");
		}
	}
	
	
//	/**
//	 * 
//	 * @param detalleTarifasServicio
//	 * @return
//	 */
//	private ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> cargarListaServiciosProgramaTarifas(
//			ArrayList<InfoTarifaServicioPresupuesto> detalleTarifasServicio, UsuarioBasico usuario, Double programa) 
//	{
//		ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> array= new ArrayList<DtoPresupuestoDetalleServiciosProgramaDao>();
//		
//		for(InfoTarifaServicioPresupuesto info: detalleTarifasServicio)
//		{
//			DtoPresupuestoDetalleServiciosProgramaDao detalle= new DtoPresupuestoDetalleServiciosProgramaDao();
//			detalle.setDctoComercialUnitario(info.getValorDescuentoComercial());
//			detalle.setErrorCalculoTarifa(info.getError());
//			detalle.setFHU(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
//			detalle.setPorcentajeDctoBonoServicio(info.getPorcentajeDecuentoBonoUnitario());
//			detalle.setPorcentajeDctoPromocionServicio(info.getPorcentajeDescuentoPromocionUnitario());
//			//detalle.setPresupuestoOdoConvenio(presupuestoOdoConvenio);
//			detalle.setPrograma(programa);
//			detalle.setServicio(info.getServicio());
//			detalle.setValorDctoBonoServicio(info.getValorDescuentoBonoUnitario());
//			detalle.setValorDctoPromocionServicio(info.getValorDescuentoPromocionUnitario());
//			detalle.setValorUnitarioServicio(info.getValorTarifaUnitaria());
//			detalle.setValorHonorarioDctoPromocionServicio(info.getValorHonorarioPromocion());
//			detalle.setPorcentajeHonorarioDctoPromocionServicio(info.getPorcentajeHonorarioPromocion());
//			
//			logger.info("logger ->"+detalle.loggerPromocion());
//			
//			array.add(detalle);
//		}
//		
//		return array;
//	}

	/**
	 * @param forma
	 * @param usuario
	 * @param arrayServProg
	 */
	private void extraerProgramasServiciosPlanAlPresupuestoSeccionBoca(
			PresupuestoOdontologicoForm forma, 
			UsuarioBasico usuario)
	{
		int posExisteServProg;
		for(InfoHallazgoSuperficie objHallazgo : forma.getDtoPlanTratamiento().getSeccionHallazgosBoca())
		 {
			 for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
			 {
				 logger.info("servicio/programa activo--->"+objServProg.getActivo()+" --> "+objServProg.getNombreProgramaServicio());
				 if(objServProg.getActivo() && !objServProg.isAsignadoAlPresupuesto())
				 {
					 objServProg.setAsignadoAlPresupuesto(true);
					 posExisteServProg= existeServPrograma(forma.getListaProgramasServicios(), objServProg.getCodigoPkProgramaServicio(), forma.getUtilizaPrograma());
					 logger.info("posExisteServProg-->"+posExisteServProg);
					 
					 if(posExisteServProg>ConstantesBD.codigoNuncaValido)
					 {
						 forma.getListaProgramasServicios().get(posExisteServProg).getListaCalculoCantidades().add(objHallazgo.getHallazgoREQUERIDO().getCodigo(), objServProg.getCodigoPkProgramaServicio(), objServProg.getNumeroSuperficies());
						 //forma.getListaProgramasServicios().get(posExisteServProg).setCantidad((forma.getListaProgramasServicios().get(posExisteServProg).getCantidad())+1);
						 if(forma.getListaProgramasServicios().get(posExisteServProg).getCodigoPk().doubleValue()>0)
						 {
							 forma.getListaProgramasServicios().get(posExisteServProg).setTipoModificacion(EnumTipoModificacion.MODIFICADO);
						 }
						 ///adicionamos la info de la pieza
						 DtoPresupuestoPiezas dtoPieza= new DtoPresupuestoPiezas();
						 dtoPieza.setHallazgo(new BigDecimal( objHallazgo.getHallazgoREQUERIDO().getCodigo()));
						 dtoPieza.setPieza(new BigDecimal(ConstantesBD.codigoNuncaValido));
						 dtoPieza.setSuperficie(new BigDecimal(objHallazgo.getSuperficieOPCIONAL().getCodigo()));
						 dtoPieza.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
						 dtoPieza.setSeccion(ConstantesIntegridadDominio.acronimoBoca);
						 dtoPieza.setNumSuperficies(objServProg.getNumeroSuperficies());
						 forma.getListaProgramasServicios().get(posExisteServProg).getListPresupuestoPiezas().add(dtoPieza);
					 }
					 else
					 {	 
						 DtoPresuOdoProgServ dto= new DtoPresuOdoProgServ();
						 
						 /////cargar el detalle de las piezas 
						 ArrayList<DtoPresupuestoPiezas> arrayPieza= new ArrayList<DtoPresupuestoPiezas>();
						 DtoPresupuestoPiezas dtoPieza= new DtoPresupuestoPiezas();
						 dtoPieza.setHallazgo(new BigDecimal( objHallazgo.getHallazgoREQUERIDO().getCodigo()));
						 dtoPieza.setPieza(new BigDecimal(ConstantesBD.codigoNuncaValido));
						 dtoPieza.setSuperficie(new BigDecimal(objHallazgo.getSuperficieOPCIONAL().getCodigo()));
						 dtoPieza.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
						 dtoPieza.setSeccion(ConstantesIntegridadDominio.acronimoBoca);
						 dtoPieza.setNumSuperficies(objServProg.getNumeroSuperficies());
						 arrayPieza.add(dtoPieza);
						 logger.info("pieza-->"+arrayPieza);
						 ////////////////////////////
						 
						 dto.setListPresupuestoPiezas(arrayPieza);
						 //dto.setCantidad(1);
						 dto.getListaCalculoCantidades().add(objHallazgo.getHallazgoREQUERIDO().getCodigo(), objServProg.getCodigoPkProgramaServicio(), objServProg.getNumeroSuperficies());
						 if(forma.getUtilizaPrograma())
						 {	 
							 dto.setPrograma(new InfoDatosDouble(objServProg.getCodigoPkProgramaServicio().doubleValue(), ProgramasOdontologicos.obtenerNombrePrograma(objServProg.getCodigoPkProgramaServicio().doubleValue()), objServProg.getCodigoAmostrar()));
							 dto.setEspecialidad(Programa.obtenerEspeciliadadPrograma(dto.getPrograma().getCodigo()));
						 }
						 else
						 {
							 dto.setServicio(new InfoDatosInt(objServProg.getCodigoPkProgramaServicio().intValue(), objServProg.getNombreProgramaServicio(), objServProg.getCodigoAmostrar()));
							 dto.setEspecialidad(UtilidadesFacturacion.obtenerEspecialidadServicio(dto.getServicio().getCodigo()).getNombre());
						 }
						 dto.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
						 dto.setTipoModificacion(EnumTipoModificacion.NUEVO);
						 forma.getListaProgramasServicios().add(dto);
					 }
				 }
			 }
		 }
	}

	/**
	 * @param forma
	 * @param usuario
	 * @param arrayServProg
	 */
	private void extraerProgramasServiciosPlanAlPresupuestoSeccionOtros(
			PresupuestoOdontologicoForm forma, 
			UsuarioBasico usuario)
	{
		int posExisteServProg;
		for(InfoDetallePlanTramiento objPlan :   forma.getDtoPlanTratamiento().getSeccionOtrosHallazgos())
		 {
			 for(InfoHallazgoSuperficie objHallazgo : objPlan.getDetalleSuperficie())
			 {
				 for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
				 {
					 logger.info("servicio/programa activo--->"+objServProg.getActivo()+" --> "+objServProg.getNombreProgramaServicio());
					 if(objServProg.getActivo() && !objServProg.isAsignadoAlPresupuesto())
					 {
						 objServProg.setAsignadoAlPresupuesto(true);
						 posExisteServProg= existeServPrograma(forma.getListaProgramasServicios(), objServProg.getCodigoPkProgramaServicio(), forma.getUtilizaPrograma());
						 logger.info("posExisteServProg-->"+posExisteServProg);
						 
						 if(posExisteServProg>ConstantesBD.codigoNuncaValido)
						 {
							 forma.getListaProgramasServicios().get(posExisteServProg).getListaCalculoCantidades().add(objHallazgo.getHallazgoREQUERIDO().getCodigo(), objServProg.getCodigoPkProgramaServicio(), objServProg.getNumeroSuperficies());
							 //forma.getListaProgramasServicios().get(posExisteServProg).setCantidad((forma.getListaProgramasServicios().get(posExisteServProg).getCantidad())+1);
							 if(forma.getListaProgramasServicios().get(posExisteServProg).getCodigoPk().doubleValue()>0)
							 {
								 forma.getListaProgramasServicios().get(posExisteServProg).setTipoModificacion(EnumTipoModificacion.MODIFICADO);
							 }
							 //adicionamos la info de la pieza
							 DtoPresupuestoPiezas dtoPieza= new DtoPresupuestoPiezas();
							 dtoPieza.setHallazgo(new BigDecimal( objHallazgo.getHallazgoREQUERIDO().getCodigo()));
							 dtoPieza.setPieza(new BigDecimal(objPlan.getPieza().getCodigo()));
							 dtoPieza.setSuperficie(new BigDecimal(objHallazgo.getSuperficieOPCIONAL().getCodigo()));
							 dtoPieza.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
							 dtoPieza.setSeccion(ConstantesIntegridadDominio.acronimoOtro);
							 dtoPieza.setNumSuperficies(objServProg.getNumeroSuperficies());
							 forma.getListaProgramasServicios().get(posExisteServProg).getListPresupuestoPiezas().add(dtoPieza);
						 }
						 else
						 {	 
							 DtoPresuOdoProgServ dto= new DtoPresuOdoProgServ();
							 
							 /////cargar el detalle de las piezas 
							 ArrayList<DtoPresupuestoPiezas> arrayPieza= new ArrayList<DtoPresupuestoPiezas>();
							 DtoPresupuestoPiezas dtoPieza= new DtoPresupuestoPiezas();
							 dtoPieza.setHallazgo(new BigDecimal( objHallazgo.getHallazgoREQUERIDO().getCodigo()));
							 dtoPieza.setPieza(new BigDecimal(objPlan.getPieza().getCodigo()));
							 dtoPieza.setSuperficie(new BigDecimal(objHallazgo.getSuperficieOPCIONAL().getCodigo()));
							 dtoPieza.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
							 dtoPieza.setSeccion(ConstantesIntegridadDominio.acronimoOtro);
							 dtoPieza.setNumSuperficies(objServProg.getNumeroSuperficies());
							 arrayPieza.add(dtoPieza);
							 logger.info("pieza-->"+arrayPieza);
							 ////////////////////////////
							 
							 dto.setListPresupuestoPiezas(arrayPieza);
							 //dto.setCantidad(1);
							 dto.getListaCalculoCantidades().add(objHallazgo.getHallazgoREQUERIDO().getCodigo(), objServProg.getCodigoPkProgramaServicio(), objServProg.getNumeroSuperficies());
							 if(forma.getUtilizaPrograma())
							 {	 
								 dto.setPrograma(new InfoDatosDouble(objServProg.getCodigoPkProgramaServicio().doubleValue(), ProgramasOdontologicos.obtenerNombrePrograma(objServProg.getCodigoPkProgramaServicio().doubleValue()), objServProg.getCodigoAmostrar()));
								 dto.setEspecialidad(Programa.obtenerEspeciliadadPrograma(dto.getPrograma().getCodigo()));
							 }
							 else
							 {
								 dto.setServicio(new InfoDatosInt(objServProg.getCodigoPkProgramaServicio().intValue(), objServProg.getNombreProgramaServicio(), objServProg.getCodigoAmostrar()));
								 dto.setEspecialidad(UtilidadesFacturacion.obtenerEspecialidadServicio(dto.getServicio().getCodigo()).getNombre());
							 }
							 dto.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
							 dto.setTipoModificacion(EnumTipoModificacion.NUEVO);
							 forma.getListaProgramasServicios().add(dto);
						 }
					 }
				 }
			 }
		 }
	}

	/**
	 * @param forma
	 * @param usuario
	 * @param arrayServProg
	 */
	private void extraerProgramasServiciosPlanAlPresupuesto(
			PresupuestoOdontologicoForm forma, 
			UsuarioBasico usuario)
	{		
		int posExisteServProg;
		for(InfoDetallePlanTramiento objPlan :   forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle())
		 {
			 for(InfoHallazgoSuperficie objHallazgo : objPlan.getDetalleSuperficie())
			 {
				 for(InfoProgramaServicioPlan objServProg: objHallazgo.getProgramasOservicios())
				 {
					 if(objServProg.getActivo() && !objServProg.isAsignadoAlPresupuesto())
					 {
						 objServProg.setAsignadoAlPresupuesto(true);
						 posExisteServProg= existeServPrograma(forma.getListaProgramasServicios(), objServProg.getCodigoPkProgramaServicio(), forma.getUtilizaPrograma());
						 logger.info("posExisteServProg-->"+posExisteServProg);
						 
						 if(posExisteServProg>ConstantesBD.codigoNuncaValido)
						 {
							 logger.info("objHallazgo.getHallazgoREQUERIDO().getCodigo()->"+objHallazgo.getHallazgoREQUERIDO().getCodigo()+" objServProg.getCodigoPkProgramaServicio()->"+objServProg.getCodigoPkProgramaServicio()+" objServProg.getNumeroSuperficies()->"+objServProg.getNumeroSuperficies());
							 forma.getListaProgramasServicios().get(posExisteServProg).getListaCalculoCantidades().add(objHallazgo.getHallazgoREQUERIDO().getCodigo(), objServProg.getCodigoPkProgramaServicio(), objServProg.getNumeroSuperficies());
							 //forma.getListaProgramasServicios().get(posExisteServProg).setCantidad((forma.getListaProgramasServicios().get(posExisteServProg).getCantidad())+1);
							 
							 if(forma.getListaProgramasServicios().get(posExisteServProg).getCodigoPk().doubleValue()>0)
							 {
								 forma.getListaProgramasServicios().get(posExisteServProg).setTipoModificacion(EnumTipoModificacion.MODIFICADO);
							 }
							 //adicionamos la info de la pieza
							 DtoPresupuestoPiezas dtoPieza= new DtoPresupuestoPiezas();
							 dtoPieza.setHallazgo(new BigDecimal( objHallazgo.getHallazgoREQUERIDO().getCodigo()));
							 dtoPieza.setPieza(new BigDecimal(objPlan.getPieza().getCodigo()));
							 dtoPieza.setSuperficie(new BigDecimal(objHallazgo.getSuperficieOPCIONAL().getCodigo()));
							 dtoPieza.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
							 dtoPieza.setSeccion(ConstantesIntegridadDominio.acronimoDetalle);
							 dtoPieza.setNumSuperficies(objServProg.getNumeroSuperficies());
							 forma.getListaProgramasServicios().get(posExisteServProg).getListPresupuestoPiezas().add(dtoPieza);
						 }
						 else
						 {	 
							 DtoPresuOdoProgServ dto= new DtoPresuOdoProgServ();
							 
							 /////cargar el detalle de las piezas 
							 ArrayList<DtoPresupuestoPiezas> arrayPieza= new ArrayList<DtoPresupuestoPiezas>();
							 DtoPresupuestoPiezas dtoPieza= new DtoPresupuestoPiezas();
							 dtoPieza.setHallazgo(new BigDecimal( objHallazgo.getHallazgoREQUERIDO().getCodigo()));
							 dtoPieza.setPieza(new BigDecimal(objPlan.getPieza().getCodigo()));
							 dtoPieza.setSuperficie(new BigDecimal(objHallazgo.getSuperficieOPCIONAL().getCodigo()));
							 dtoPieza.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
							 dtoPieza.setSeccion(ConstantesIntegridadDominio.acronimoDetalle);
							 dtoPieza.setNumSuperficies(objServProg.getNumeroSuperficies());
							 arrayPieza.add(dtoPieza);
							 logger.info("pieza-->"+arrayPieza);
							 ////////////////////////////
							 
							 dto.setListPresupuestoPiezas(arrayPieza);
							 //dto.setCantidad(1);
							 dto.getListaCalculoCantidades().add(objHallazgo.getHallazgoREQUERIDO().getCodigo(), objServProg.getCodigoPkProgramaServicio(), objServProg.getNumeroSuperficies());
							 if(forma.getUtilizaPrograma())
							 {	 
								 dto.setPrograma(new InfoDatosDouble(objServProg.getCodigoPkProgramaServicio().doubleValue(), ProgramasOdontologicos.obtenerNombrePrograma(objServProg.getCodigoPkProgramaServicio().doubleValue()), objServProg.getCodigoAmostrar()));
								 dto.setEspecialidad(Programa.obtenerEspeciliadadPrograma(dto.getPrograma().getCodigo()));
							 }
							 else
							 {
								 dto.setServicio(new InfoDatosInt(objServProg.getCodigoPkProgramaServicio().intValue(), objServProg.getNombreProgramaServicio(), objServProg.getCodigoAmostrar()));
								 dto.setEspecialidad(UtilidadesFacturacion.obtenerEspecialidadServicio(dto.getServicio().getCodigo()).getNombre());
							 }
							 dto.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
							 dto.setTipoModificacion(EnumTipoModificacion.NUEVO);
							 forma.getListaProgramasServicios().add(dto);
						 }
					 }
				 }
			 }
		 }
	}
	
	
 /**
	  * 
	  * @param arrayServProg
	  * @param codigoPkProgramaServicio
	  * @return
	  */
	 private int existeServPrograma(	ArrayList<DtoPresuOdoProgServ> arrayServProg,
			 							BigDecimal codigoPkProgramaServicio,
			 							boolean utilizaProgramas) 
	 {
		 int posArray=0;
		 for(DtoPresuOdoProgServ dto: arrayServProg)
		 {
			if(utilizaProgramas)
			{	
				if(dto.getPrograma().getCodigo().doubleValue()==codigoPkProgramaServicio.doubleValue() && !dto.getPaquetizado())
				{
					return posArray;
				}
			}
			else
			{
				if(dto.getServicio().getCodigo()==codigoPkProgramaServicio.intValue())
				{
					return posArray;
				}
			}
			posArray++;
		 }
		 return ConstantesBD.codigoNuncaValido;
	 }

	 

	/**
	 * 
	 * @param codigoInstitucion
	 * @param con
	 * @return
	 */
	
	private boolean existeConsecutivoDescuento(int codigoInstitucion, Connection con )
	{
		String consecutivoAInsertar=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoSolicitudDescuentoOdo,codigoInstitucion);
		logger.info("consecutivoAInsertar--->"+consecutivoAInsertar);
		if(consecutivoAInsertar.equals("") || Utilidades.convertirADouble(consecutivoAInsertar)<=0)
			return false;
		else
			return true;
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificarPresupuesto(ActionMapping mapping, PresupuestoOdontologicoForm forma, UsuarioBasico usuario)
	{
		 forma.setModificando(false);
		 forma.setEsNuevo(false);
		 logger.info("EL POS ARRAY DETALLE *************************************************************************** ES ----------> "+forma.getPosArray());
		 forma.setDtoPresupuesto( (DtoPresupuestoOdontologico)forma.getListaPresupuestos().get(forma.getPosArray()).clone());
		 logger.info("******************************************************");
		 logger.info(" 	CARGAR MODIFICAR PRESUPUESTO");
		 return mapping.findForward("modificarPresupuesto");
	}
	 
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarDetalleProgramas(ActionMapping mapping, PresupuestoOdontologicoForm forma, UsuarioBasico usuario )
	{
		logger.info("********************************************************************************************");
		logger.info("----------------------------------CagarDetalle Programas Servicios--------------------------");
		if(forma.getTipoSeccion().equals(ConstantesIntegridadDominio.acronimoDetalle))
		{
			cargarDetallePlanTratamiento(forma,usuario);
		}
		else if(forma.getTipoSeccion().equals(ConstantesIntegridadDominio.acronimoOtro))
		{
			cargarDetallePlanTratamientoAOTRO(forma,usuario);
		}
		else if (forma.getTipoSeccion().equals(ConstantesIntegridadDominio.acronimoBoca))
		{
			cargarDetallePlanTratamientoBOCA(forma,usuario);
		}
		return mapping.findForward("paginaDetalleProgramaServicio");
	}
	
	/**
	 * 
	 * @param forma
	 */
	private void cargarDetallePlanTratamientoBOCA(PresupuestoOdontologicoForm forma,UsuarioBasico usuario) 
	{
		logger.info("CARGA SECCION BOCA"); 
		
		String tmpBusquedaServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
		
		String codigoTemporales="";
		ArrayList<Double> codigosAsignadosPresupuesto= new ArrayList<Double>();
		BigDecimal codigoDetallePlan= forma.getDtoPlanTratamiento().getSeccionHallazgosBoca().get(forma.getPosArray()).getCodigoPkDetalle();
		double codigoHallazgo= forma.getDtoPlanTratamiento().getSeccionHallazgosBoca().get(forma.getPosArray()).getHallazgoREQUERIDO().getCodigo();
		if(forma.getDtoPlanTratamiento().getSeccionHallazgosBoca().get(forma.getPosArray()).getProgramasOservicios().size()>0)
		{
			for(int w=0; w<forma.getDtoPlanTratamiento().getSeccionHallazgosBoca().get(forma.getPosArray()).getProgramasOservicios().size(); w++)
			{
				if(w>0)
				{
					codigoTemporales+=",";
				}
				codigoTemporales +=forma.getDtoPlanTratamiento().getSeccionHallazgosBoca().get(forma.getPosArray()).getProgramasOservicios().get(w).getCodigoPkProgramaServicio()+"";
				if(forma.getDtoPlanTratamiento().getSeccionHallazgosBoca().get(forma.getPosArray()).getProgramasOservicios().get(w).isAsignadoAlPresupuesto())
				{
					codigosAsignadosPresupuesto.add(forma.getDtoPlanTratamiento().getSeccionHallazgosBoca().get(forma.getPosArray()).getProgramasOservicios().get(w).getCodigoPkProgramaServicio().doubleValue());
				}
			}
		}
		forma.setListaDetalleServicioProgramaPlanTratamiento(PlanTratamiento.obtenerProgramasServiciosHallazgosPlanTramiento(codigoHallazgo, codigoTemporales, codigoDetallePlan, forma.getUtilizaPrograma(), 0, forma.getDtoPresupuesto().getCodigoPK(), UtilidadTexto.convertirArrayDoubleACodigosSeparadosXComas(codigosAsignadosPresupuesto), new ArrayList<InfoNumSuperficiesPresupuesto>())) ; //falta el servicio 
		
		ArrayList<InfoProgramaServicioPlan> posAEliminar= new ArrayList<InfoProgramaServicioPlan>();
		
		if(forma.getListaDetalleServicioProgramaPlanTratamiento().size()>0){
			for( int i=0; i<forma.getListaDetalleServicioProgramaPlanTratamiento().size(); i++ )
			 {
				int tamanio= ProgramasOdontologicos.cargarDetallePrograma(forma.getListaDetalleServicioProgramaPlanTratamiento().get(i).getCodigoPkProgramaServicio().doubleValue(), tmpBusquedaServicios).size();
				logger.info("tamanio--->"+tamanio);
				 if(tamanio<=0)
				 {
					 posAEliminar.add(forma.getListaDetalleServicioProgramaPlanTratamiento().get(i));
					//forma.getListaDetalleServicioPrograma().remove(i);
					logger.info("remueve!!!!!-->"+i);
				 }
			 }
		}
		
		logger.info("tamanio array elim-->"+posAEliminar.size());
		for(InfoProgramaServicioPlan i: posAEliminar)
		{
			logger.info("en forma-->"+i);
			logger.info(forma.getListaDetalleServicioProgramaPlanTratamiento().remove(i));
		}
		cargarEquivalentes(forma, codigoHallazgo);
	}
	 
	/**
	 * 
	 * @param forma
	 */
	private void cargarDetallePlanTratamientoAOTRO(	PresupuestoOdontologicoForm forma,UsuarioBasico usuario) 
	{
		
		String tmpBusquedaServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
		BigDecimal codigoDetallePlan= forma.getDtoPlanTratamiento().getSeccionOtrosHallazgos().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getCodigoPkDetalle();
		double codigoHallazgo=	forma.getDtoPlanTratamiento().getSeccionOtrosHallazgos().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getHallazgoREQUERIDO().getCodigo();
		//BigDecimal codigoProgramaServicio = new BigDecimal(0);
		String codigoTemporales="";
		ArrayList<Double> codigosAsignadosPresupuesto= new ArrayList<Double>();
		ArrayList<InfoNumSuperficiesPresupuesto> superficies= new ArrayList<InfoNumSuperficiesPresupuesto>();
		
		if(forma.getDtoPlanTratamiento().getSeccionOtrosHallazgos().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getProgramasOservicios().size()>0)
		{
			for (int w=0; w<forma.getDtoPlanTratamiento().getSeccionOtrosHallazgos().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getProgramasOservicios().size() ; w++)
			{
				if(w>0)
				{
					codigoTemporales+=",";
				}
				codigoTemporales +=forma.getDtoPlanTratamiento().getSeccionOtrosHallazgos().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getProgramasOservicios().get(w).getCodigoPkProgramaServicio() +"";
				if(forma.getDtoPlanTratamiento().getSeccionOtrosHallazgos().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getProgramasOservicios().get(w).isAsignadoAlPresupuesto())
				{
					codigosAsignadosPresupuesto.add(forma.getDtoPlanTratamiento().getSeccionOtrosHallazgos().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getProgramasOservicios().get(w).getCodigoPkProgramaServicio().doubleValue());
				}
			}
			
		}	
		
		for(InfoHallazgoSuperficie info: forma.getDtoPlanTratamiento().getSeccionOtrosHallazgos().get(forma.getPosArray()).getDetalleSuperficie())
		{
			boolean marcarXDefecto= forma.getDtoPlanTratamiento().getSeccionOtrosHallazgos().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getSuperficieOPCIONAL().getCodigo()==info.getSuperficieOPCIONAL().getCodigo();
			superficies.add(new InfoNumSuperficiesPresupuesto(info.getSuperficieOPCIONAL(), marcarXDefecto));
		}
		
		forma.setListaDetalleServicioProgramaPlanTratamiento(PlanTratamiento.obtenerProgramasServiciosHallazgosPlanTramiento(codigoHallazgo, codigoTemporales, codigoDetallePlan, forma.getUtilizaPrograma(), 0, forma.getDtoPresupuesto().getCodigoPK(), UtilidadTexto.convertirArrayDoubleACodigosSeparadosXComas(codigosAsignadosPresupuesto), superficies )  ) ;
			
		ArrayList<InfoProgramaServicioPlan> posAEliminar= new ArrayList<InfoProgramaServicioPlan>();
		
		if(forma.getListaDetalleServicioProgramaPlanTratamiento().size()>0)
		{
			for( int i=0; i<forma.getListaDetalleServicioProgramaPlanTratamiento().size(); i++ )
			{
				int tamanio= ProgramasOdontologicos.cargarDetallePrograma(forma.getListaDetalleServicioProgramaPlanTratamiento().get(i).getCodigoPkProgramaServicio().doubleValue(),tmpBusquedaServicios).size();
				logger.info("tamanio--->"+tamanio);
				if(tamanio<=0)
				{
					posAEliminar.add(forma.getListaDetalleServicioProgramaPlanTratamiento().get(i));
					//forma.getListaDetalleServicioPrograma().remove(i);
					logger.info("remueve!!!!!-->"+i);
				}
			}
		}
		
		logger.info("tamanio array elim-->"+posAEliminar.size());
		for(InfoProgramaServicioPlan i: posAEliminar)
		{
			logger.info("en forma-->"+i);
			logger.info(forma.getListaDetalleServicioProgramaPlanTratamiento().remove(i));
		}
		cargarEquivalentes(forma, codigoHallazgo);
	}
	 
	/**
	 * 
	 * @param forma
	 */
	private void cargarDetallePlanTratamiento(PresupuestoOdontologicoForm forma,UsuarioBasico usuario) 
	{
		
		String tmpBusquedaServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
		BigDecimal codigoDetallePlan= forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getCodigoPkDetalle();
		double codigoHallazgo=	forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getHallazgoREQUERIDO().getCodigo();
		//BigDecimal codigoProgramaServicio = new BigDecimal(0);
		String codigoTemporales="";
		ArrayList<Double> codigosAsignadosPresupuesto= new ArrayList<Double>();
		ArrayList<InfoNumSuperficiesPresupuesto> superficies= new ArrayList<InfoNumSuperficiesPresupuesto>();
		
		if(forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getProgramasOservicios().size()>0)
		{
		//	 codigoProgramaServicio =forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getProgramasOservicios().get(0).getCodigopk();
			
			for (int w=0; w<forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getProgramasOservicios().size() ; w++)
			{
				if(w>0)
				{
					codigoTemporales+=",";
				}
				codigoTemporales +=forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getProgramasOservicios().get(w).getCodigoPkProgramaServicio() +"";
				if(forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getProgramasOservicios().get(w).isAsignadoAlPresupuesto())
				{
					codigosAsignadosPresupuesto.add(forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getProgramasOservicios().get(w).getCodigoPkProgramaServicio().doubleValue());
				}
			//	codigoTemporales +=forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getProgramasOservicios().get(w).getNombreProgramaServicio();
			}
			
		}
		
		for(InfoHallazgoSuperficie info: forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie())
		{
			if(info.getHallazgoREQUERIDO().getCodigo()==codigoHallazgo)
			{	
				boolean marcarXDefecto= forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getSuperficieOPCIONAL().getCodigo()==info.getSuperficieOPCIONAL().getCodigo();
				superficies.add(new InfoNumSuperficiesPresupuesto(info.getSuperficieOPCIONAL(), marcarXDefecto));
			}	
		}
		
		forma.setListaDetalleServicioProgramaPlanTratamiento(PlanTratamiento.obtenerProgramasServiciosHallazgosPlanTramiento(codigoHallazgo, codigoTemporales, codigoDetallePlan, forma.getUtilizaPrograma(), 0, forma.getDtoPresupuesto().getCodigoPK(), UtilidadTexto.convertirArrayDoubleACodigosSeparadosXComas(codigosAsignadosPresupuesto), superficies)) ;
		
		
		
		//le colocamos el color en caso de que ya este seleccionado en el plan
		for(InfoHallazgoSuperficie hallazgoSuperficie1: forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie())
		{	
			for(InfoProgramaServicioPlan progServ: hallazgoSuperficie1.getProgramasOservicios())
			{
				if(progServ.getNumeroSuperficies()>1)
				{
					for(InfoProgramaServicioPlan popupProgServ: forma.getListaDetalleServicioProgramaPlanTratamiento())
					{
						if(progServ.getCodigoPkProgramaServicio().doubleValue()==popupProgServ.getCodigoPkProgramaServicio().doubleValue())
						{
							for(InfoNumSuperficiesPresupuesto numSup:  popupProgServ.getSuperficiesAplicaPresupuesto())
							{
								if(hallazgoSuperficie1.getSuperficieOPCIONAL().getCodigo()== numSup.getCodigoSuperficie())
								{
									if(!UtilidadTexto.isEmpty(progServ.getColorLetra()))
									{
										numSup.setColor(progServ.getColorLetra());
										numSup.setActivo(true);
									}
									else
									{
										numSup.setColor("");
										numSup.setActivo(true);
									}
									
									if(numSup.isMarcarXDefecto())
									{
										popupProgServ.setColorLetra(numSup.getColor());
									}
								}	
							}
						}
					}
				}	
			}	
		}	
			
		
		//colocamos el modificable
		//le colocamos el color en caso de que ya este seleccionado en el plan
		for(InfoHallazgoSuperficie hallazgoSuperficie1: forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie())
		{	
			for(InfoProgramaServicioPlan progServ: hallazgoSuperficie1.getProgramasOservicios())
			{
				if(progServ.getNumeroSuperficies()>1)
				{
					for(InfoProgramaServicioPlan popupProgServ: forma.getListaDetalleServicioProgramaPlanTratamiento())
					{
						if(progServ.getCodigoPkProgramaServicio().doubleValue()==popupProgServ.getCodigoPkProgramaServicio().doubleValue())
						{
							String colorDefecto= obtenerColorDefault(popupProgServ.getSuperficiesAplicaPresupuesto());
							for(InfoNumSuperficiesPresupuesto numSup:  popupProgServ.getSuperficiesAplicaPresupuesto())
							{
								if(hallazgoSuperficie1.getSuperficieOPCIONAL().getCodigo()== numSup.getCodigoSuperficie())
								{
									logger.info("Superficie-->"+hallazgoSuperficie1.getSuperficieOPCIONAL().getNombre()+"  PROGRAMA --->"+progServ.getNombreProgramaServicio()+" Color-->"+progServ.getColorLetra()+" color defecto-->"+colorDefecto);
									
									if(numSup.isMarcarXDefecto())
									{
										numSup.setModificable(false);
									}
									else
									{	
										if(!UtilidadTexto.isEmpty(progServ.getColorLetra()))
										{
											logger.info("colorDefecto-->"+colorDefecto+" colorNumSup-->"+numSup.getColor()+" iguales-->"+colorDefecto.equals(numSup.getColor()));
											if(colorDefecto.equals(numSup.getColor()))
											{
												numSup.setModificable(true);
											}
											else
											{
												numSup.setModificable(false);
											}
										}
										else
										{
											numSup.setModificable(true);
										}
									}
									
								}	
							}
						}
					}
						
				}	
			}	
		}	
		
		
		
		
		ArrayList<InfoProgramaServicioPlan> posAEliminar= new ArrayList<InfoProgramaServicioPlan>();
		//No ser repitan 
		if(forma.getListaDetalleServicioProgramaPlanTratamiento().size()>0)
		{
			for( int i=0; i<forma.getListaDetalleServicioProgramaPlanTratamiento().size(); i++ )
			{
				int tamanio= ProgramasOdontologicos.cargarDetallePrograma(forma.getListaDetalleServicioProgramaPlanTratamiento().get(i).getCodigoPkProgramaServicio().doubleValue(),tmpBusquedaServicios).size();
				logger.info("tamanio--->"+tamanio);
				if(tamanio<=0)
				{
					posAEliminar.add(forma.getListaDetalleServicioProgramaPlanTratamiento().get(i));
					//forma.getListaDetalleServicioPrograma().remove(i);
					logger.info("remueve!!!!!-->"+i);
				}
			}
		}
	
		logger.info("tamanio array elim-->"+posAEliminar.size());
		for(InfoProgramaServicioPlan i: posAEliminar)
		{
			logger.info("en forma-->"+i);
			logger.info(forma.getListaDetalleServicioProgramaPlanTratamiento().remove(i));
		}
		
		//seteamos los estados de esos programas o servicios que tienen en el plan de tratamiento
		for(InfoProgramaServicioPlan e: forma.getListaDetalleServicioProgramaPlanTratamiento())
		{
			if(forma.getUtilizaPrograma())
			{
				e.setEstadoPrograma(PlanTratamiento.obtenerEstadoProgramaServicioPlanTratamiento(codigoDetallePlan, forma.getUtilizaPrograma(), e.getCodigoPkProgramaServicio().doubleValue()));
			}
			else
			{
				e.setEstadoServicio(PlanTratamiento.obtenerEstadoProgramaServicioPlanTratamiento(codigoDetallePlan, forma.getUtilizaPrograma(), e.getCodigoPkProgramaServicio().doubleValue()));
			}
		}
		
		cargarEquivalentes(forma, codigoHallazgo);
	}

	/**
	 * 
	 * Metodo para .......
	 * @param superficiesAplicaPresupuesto
	 * @param codigo
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private String obtenerColorDefault(ArrayList<InfoNumSuperficiesPresupuesto> superficiesAplicaPresupuesto) 
	{
		for(InfoNumSuperficiesPresupuesto numSup: superficiesAplicaPresupuesto)
		{
			if(numSup.isMarcarXDefecto())
			{
				return numSup.getColor();
			}
		}
		return "";
	}

	/**
	 * 
	 * @param forma
	 */
	private void cargarEquivalentes(PresupuestoOdontologicoForm forma, double codigoHallazgo) 
	{
		for(InfoProgramaServicioPlan progServ: forma.getListaDetalleServicioProgramaPlanTratamiento() )
		{
			progServ.setCodigoEquivalente(UtilidadTexto.convertirArrayDoubleACodigosSeparadosXComas(DetalleHallazgoProgramaServicio.obtenerEquivalentesProgServ(progServ.getCodigoPkProgramaServicio(), codigoHallazgo, forma.getUtilizaPrograma())));
		}
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionActualizarProgramasServiciosPlanTratamiento(ActionMapping mapping,PresupuestoOdontologicoForm forma, UsuarioBasico usuario, HttpServletRequest request ) 
	{
		
		return mapping.findForward("cargarPlanTratamiento");
	}

	/**
	 * 
	 * @param forma
	 * @param paciente
	 * @param mapping
	 * @return
	 */
	private ActionForward cargarRetazoPlanTratamiento(PresupuestoOdontologicoForm forma,PersonaBasica paciente,ActionMapping mapping, HttpServletRequest resquest)
	{
		//TOMAMOS LOS SERVICIOS DEL HALLAZGO
		ArrayList<InfoProgramaServicioPlan> listaProgramasServicios = obtenerProgramasServiciosSeleccionadosPopup(forma);
		forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).setProgramasOservicios(listaProgramasServicios);
		
		//DEBEMOS REALIZAR LA BUSQUEDA DE LOS PROGRAMAS - SERVICIOS QUE APLICAN PARA N SUPERFICIES
		
		for(InfoProgramaServicioPlan dto: forma.getListaDetalleServicioProgramaPlanTratamiento())
		{
			if(dto.getNumeroSuperficies()>1)
			{
				for(InfoNumSuperficiesPresupuesto superficie: dto.getSuperficiesAplicaPresupuesto())
				{
					actualizarProgramasMismoHallazgoDadaSuperficie(superficie, forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getHallazgoREQUERIDO().getCodigo(), ConstantesIntegridadDominio.acronimoDetalle, forma, dto);
				}
			}
		}
		
		logger.info("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYSIZE--->"+forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getProgramasOservicios().size());
		
		return mapping.findForward("retazoPlanTratamiento");
	}

	/**
	 * 
	 * Metodo para 
	 * @param superficie
	 * @param codigo
	 * @param acronimodetalle
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @param forma 
	 * @param dto 
	 * @see
	 */
	private void actualizarProgramasMismoHallazgoDadaSuperficie(InfoNumSuperficiesPresupuesto superficie, int codigoHallazgo, String seccion, PresupuestoOdontologicoForm forma, InfoProgramaServicioPlan dtoProgramaServicio) 
	{
		if(seccion.equals(ConstantesIntegridadDominio.acronimoDetalle))
		{
			//DEBEMOS SETEAR EL COLOR CUANDO TIENE N SUPEFICIES
			
			for(InfoHallazgoSuperficie hallazgoSuperficie: forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie() )
			{		
				if( hallazgoSuperficie.getHallazgoREQUERIDO().getCodigo()==codigoHallazgo && hallazgoSuperficie.getSuperficieOPCIONAL().getCodigo()==superficie.getCodigoSuperficie() )
				{
					boolean yaExistePrograma= false;
					
					//verificamos que no exista el programa - servicio para adicionarlo
					for(InfoProgramaServicioPlan progServ: hallazgoSuperficie.getProgramasOservicios())
					{	
						if(dtoProgramaServicio.getCodigoPkProgramaServicio().doubleValue()==progServ.getCodigoPkProgramaServicio().doubleValue())
						{	
							yaExistePrograma=true;
						}
					}
					
					logger.info("SUPERFICIE--->"+superficie.getCodigoSuperficie()+" PROGRAMA-->"+dtoProgramaServicio.getCodigoPkProgramaServicio().doubleValue()+" YA EXISTE-->"+yaExistePrograma+" color-->"+dtoProgramaServicio.getColorLetra());
					
					if(!yaExistePrograma && (dtoProgramaServicio.getActivo() && superficie.getActivo()))
					{
						if(UtilidadTexto.isEmpty(superficie.getColor()))
						{
							dtoProgramaServicio.setColorLetra(obtenerSiguienteColor(dtoProgramaServicio.getSuperficiesAplicaPresupuesto()));
						}
						else
						{
							dtoProgramaServicio.setColorLetra(superficie.getColor());
						}
						hallazgoSuperficie.getProgramasOservicios().add(dtoProgramaServicio.copiar());
					}
					else if(yaExistePrograma && ( (!dtoProgramaServicio.getActivo() || !superficie.getActivo()) && superficie.getModificable()))
					{
						Iterator<InfoProgramaServicioPlan> iterador= hallazgoSuperficie.getProgramasOservicios().iterator();
						while(iterador.hasNext())
						{
							if(iterador.next().getCodigoPkProgramaServicio().doubleValue()==dtoProgramaServicio.getCodigoPkProgramaServicio().doubleValue())
							{
								iterador.remove();
							}
						}
					}
					/*else
					{
						if(yaExistePrograma && dtoProgramaServicio.getColorLetra().isEmpty())
						{
							dtoProgramaServicio.setColorLetra(obtenerColorActualizado(hallazgoSuperficie.getProgramasOservicios(), dtoProgramaServicio));
						}
					}*/
					
					//DEBEMOS QUITAR LOS EQUIVALENTES...................
				}
			}	
		}
	}

	/**
	 * 
	 * Metodo para .......
	 * @param arrayList
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private String obtenerSiguienteColor(ArrayList<InfoNumSuperficiesPresupuesto> listaNumSup) 
	{
		for(ColoresPlanTratamiento color: ColoresPlanTratamiento.values())
		{
			if(!color.getColor().equals(ColoresPlanTratamiento.NEGRO.getColor()))
			{	
				boolean existe= false;
				for(InfoNumSuperficiesPresupuesto numSup: listaNumSup)
				{	
					if(numSup.getColor().equals(color.getColor()))
					{
						existe= true;
					}
				}
				if(!existe)
				{
					return color.getColor();
				}
			}	
		}	
		logger.error("PROBLEMA OBTENIENDO LOS COLORES, SE AGOTO EL ENUM!!!!!!!!");
		return ColoresPlanTratamiento.NARANJA.getColor();
	}

	/**
	 * @param forma
	 * @return
	 */
	private ArrayList<InfoProgramaServicioPlan> obtenerProgramasServiciosSeleccionadosPopup(
			PresupuestoOdontologicoForm forma) {
		ArrayList<InfoProgramaServicioPlan> listaProgramasServicios = new ArrayList<InfoProgramaServicioPlan>();	//forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).getProgramasOservicios();
		
		//ITERAMOS LOS SERVICIOS DEL POPUP Y SETEAMOS LOS ACTIVOS A LA LISTA DE PROGRAMAS X HALLAZGO
		for(InfoProgramaServicioPlan dto: forma.getListaDetalleServicioProgramaPlanTratamiento())
		{
			if(dto.getActivo())
			{
				listaProgramasServicios.add(dto);
			}
		}
		return listaProgramasServicios;
	}
	 
	/**
	 * Metodo que carga el plan de tratamiento
	 * @param forma
	 * @param paciente
	 * @param institucion 
	 */
	private ActionForward cargarPlanTratamiento(	PresupuestoOdontologicoForm forma,
													PersonaBasica paciente, HttpServletRequest request, 
													ActionMapping mapping,
													BigDecimal codigoPkPresupuesto,
													HttpServletResponse response,
													String idSesion, int institucion) 
	{
		
		boolean esDummy = false;
		if(request.getServletPath().equals("/presupuestoOdontologicoDummy/presupuestoOdontologicoDummy.do"))
		{
			esDummy = true;
		}
		
		InfoPlanTratamiento dto= null;
		logger.info("\n\n\n\nCARGAR PLANES DE TRATAMIENTO Programas "+paciente.getCodigoIngreso());
		
		if(codigoPkPresupuesto.doubleValue()<=0)
		{
			//SI EL CODIGO DEL PRESUPUESTO ENVIADO ES MENOR IGUAL QUE CERO ENTONCES DEBEMOS CARGAR EL PLAN DE TRATAMIENTO INICIAL
			dto= PlanTratamiento.obtenerPlanTratamientoInicialPresupuesto(paciente.getCodigoIngreso(), forma.getUtilizaPrograma());
		}	
		else
		{
			//DE LO CONTRARIO CARGAMOS LO QUE HALLA SELECCIONADO EN EL PRESUPUESTO TABLA odontologia.presu_plan_tto_prog_ser
			dto= PlanTratamiento.obtenerPlanTratamientoXPresupuesto(codigoPkPresupuesto, forma.getUtilizaPrograma());
		}
		if(dto!=null)
		{	
			forma.setDtoPlanTratamiento(dto);
			if(codigoPkPresupuesto.doubleValue()<=0)
			{
				//solamente los tenemos que setear cuando no existe previo presupuesto en bd, de lo contrario
				//el atributo isAsignadoAlPresupuesto se setea en el SqlPlanTratamiento al cargar los programas/servicios de los hallazgos
				marcarServicioProgramasYaSeleccionadosPresupuesto(forma);
			}	
		}
		else
		{
			DtoPlanTratamientoOdo dtoPlanTratamiento=PlanTratamiento.consultarPlanTratamientoPaciente(paciente.getCodigoPersona(), institucion);
			if(dtoPlanTratamiento!=null && dtoPlanTratamiento.getCodigoPk().intValue()>0)
			{
				return null;
			}
			logger.warn("NO EXISTE PLAN TRATAMIENTO INICIAL");
			ActionErrors errores = new ActionErrors(); 
			errores.add("", new ActionMessage("error.errorEnBlanco", "Paciente no tiene Plan de Tratamiento Activo"));
	       	saveErrors(request, errores);
	    	if(esDummy)
	       	{
	    		this.accionProcesoCancelado(paciente, forma, mapping, response, idSesion);
	       		return mapping.findForward("paginaErroresActionErrorsSinCabezote");
	       	}
	       	else
	       	{
	       		return mapping.findForward("paginaErroresActionErrors");
	       	}
		}
		return null;
	}
		
	
	/**
	 * 
	 * @param forma
	 * @param paciente
	 * @param mapping
	 * @return
	 */
	 private ActionForward cargarRetazoPlanTratamientoOtros(PresupuestoOdontologicoForm forma,PersonaBasica paciente,ActionMapping mapping, HttpServletRequest request){
		logger.info("LOGGER- CARGANDO RETAZO ATRO");	
		ArrayList<InfoProgramaServicioPlan> listaProgramasServicios = obtenerProgramasServiciosSeleccionadosPopup(forma);
		forma.getDtoPlanTratamiento().getSeccionOtrosHallazgos().get(forma.getPosArray()).getDetalleSuperficie().get(forma.getPosArrayDetalle()).setProgramasOservicios(listaProgramasServicios);
		return mapping.findForward("retazoPlanTratamientoOtros");
	 }
	 
	/**
	 * 
	 * @param forma
	 * @param paciente
	 * @param mapping
	 * @return
	 */
	 private ActionForward cargarRetazoPlanTratamientoBoca(PresupuestoOdontologicoForm forma,PersonaBasica paciente,ActionMapping mapping, HttpServletRequest request){
		logger.info("LOGGER CARGAR RETAZO BOCA");	
		ArrayList<InfoProgramaServicioPlan> listaProgramasServicios = obtenerProgramasServiciosSeleccionadosPopup(forma);
		forma.getDtoPlanTratamiento().getSeccionHallazgosBoca().get(forma.getPosArray()).setProgramasOservicios(listaProgramasServicios);
		return mapping.findForward("retazoPlanTratamientoBoca");
	 }
	 
//	 /**
//	  * 
//	  * @param forma
//	  * @param paciente
//	  */
//	 private void cargarConveniosContratoPresupuesto(PresupuestoOdontologicoForm forma , PersonaBasica paciente)
//	 {
//		 forma.setListaConvenioPresupuesto(new ArrayList<InfoConvenioContratoPresupuesto>());
//		 ////primero cargamos los contratos - convenios que provienen de los parametros generales
//		 ArrayList<InfoConvenioContratoPresupuesto> arrayParametros= new ArrayList<InfoConvenioContratoPresupuesto>();
//		 ArrayList<HashMap<String, Object>> array= ValoresPorDefecto.getConveniosAMostrarPresupuestoOdo();
//		 for( HashMap<String, Object> mapa: array)
//		 {
//			 InfoConvenioContratoPresupuesto info= new InfoConvenioContratoPresupuesto();
//			 info.setActivo(true);
//			 info.setContrato(new InfoDatosInt(Utilidades.convertirAEntero(mapa.get("codigoContrato")+""), mapa.get("numeroContrato")+""));
//			 info.setConvenio(new InfoDatosInt(Utilidades.convertirAEntero(mapa.get("codigoConvenio")+""), mapa.get("descripcionConvenio")+""));
//			 info.setEsParametroGeneral(true);
//			 arrayParametros.add(info);
//		 }
//		 
//		 for( InfoConvenioContratoPresupuesto infoParametros: arrayParametros)
//		 {
//			 forma.getListaConvenioPresupuesto().add(infoParametros);
//		 }
//		 
//		 ////cargamos los contratos - convenios que vienen del paciente desde la subcuenta
//		 /*
//		  * Cargar los convenios de la tabla convenios ingreso paciente
//		  */
//		
//		 List<ConveniosIngresoPaciente> lisConvenoPacienteTMP=  PresupuestoOdontologico.cargarIngresoPaciente(paciente.getCodigoPersona());
//		 ArrayList<InfoConvenioContratoPresupuesto> arrayPaciente= PresupuestoHelperVista.transformarListConvenios(lisConvenoPacienteTMP);
//		 
//		// ArrayList<InfoConvenioContratoPresupuesto> arrayPaciente= ValidacionesPresupuesto.cargarConveniosContratosPacientePresupuesto(paciente.getCodigoIngreso());
//		 
//		 for( InfoConvenioContratoPresupuesto infoPaciente: arrayPaciente)
//		 {
//			 boolean puedoAdicionar=true;
//			 
//			 for(InfoConvenioContratoPresupuesto infoParametros: arrayParametros)
//			 {
//				 if(infoParametros.getConvenio().getCodigo()==infoPaciente.getConvenio().getCodigo() 
//						 && infoParametros.getContrato().getCodigo()==infoPaciente.getContrato().getCodigo())
//				 {
//					 logger.info("no se puede adicionar porque ya esta en los parametros !!!!!!!!!!!");
//					 puedoAdicionar=false;
//				 }
//			 }
//			 
//			 if(puedoAdicionar)
//			 {
//				 forma.getListaConvenioPresupuesto().add(infoPaciente);
//			 }
//		 }
//		 
//		 
//		 /*
//		  *Validar si existe convenio en ingresos 
//		  */
//		 for( InfoConvenioContratoPresupuesto infoConvenioContrato : arrayPaciente)
//		 {
//			 for ( DtoPresupuestoTotalConvenio dtoPresupuesto:  forma.getListaSumatoriaConvenios() )
//			 {
//				 if(dtoPresupuesto.getConvenio().getCodigo()==infoConvenioContrato.getConvenio().getCodigo() 
//						 && dtoPresupuesto.getContrato()==infoConvenioContrato.getContrato().getCodigo())
//				 {
//					dtoPresupuesto.setExisteConvenioEnIngreso(Boolean.FALSE); 
//				 }
//			 }
//		 }
//		 
//		 /*
//		  *Adiccionar  
//		  */
//		 
//		 for( InfoConvenioContratoPresupuesto infoPaciente: arrayPaciente)
//		 {
//			 
//		 
//			 for ( InfoConvenioContratoPresupuesto infoContrato: forma.getListaConvenioPresupuesto() ){
//				 
//				 if(infoContrato.getConvenio().getCodigo()==infoPaciente.getConvenio().getCodigo() 
//						 && infoContrato.getContrato().getCodigo()==infoPaciente.getContrato().getCodigo())
//				 {
//					 
//					 if(infoPaciente.getActivo())
//					 {
//						 infoContrato.setActivo(Boolean.TRUE);
//					 }
//				 }
//			 }
//		 }	 
//	 }
	 
	/**
	 * @param forma
	 * @param usuario
	 * @param paciente
	 */
	private void actualizacionTotales(PresupuestoOdontologicoForm forma,
			UsuarioBasico usuario, PersonaBasica paciente) throws IPSException
	{
		//ACTUALIZACION DE LOS VALORES TOTALES X CONVENIO
		for(DtoPresupuestoTotalConvenio dtoTotalesXConvenio: forma.getListaSumatoriaConvenios())
		{
			logger.info("INICIO CONVENIO---------------------->"+dtoTotalesXConvenio.getConvenio().getCodigo());
			BigDecimal valorSubTotal= BigDecimal.ZERO;
			for(DtoPresuOdoProgServ dtop : forma.getListaProgramasServicios())
			{
				for(DtoPresupuestoOdoConvenio detalleConvenioServPrograma: dtop.getListPresupuestoOdoConvenio())
				{
					if(dtoTotalesXConvenio.getConvenio().getCodigo()==detalleConvenioServPrograma.getConvenio().getCodigo())
					{
						logger.info("val unitario externo-->"+detalleConvenioServPrograma.getValorTotalConvenioXProgServ(dtop.getCantidad()));
						if(detalleConvenioServPrograma.getValorUnitario().doubleValue()>0 && dtop.getCantidad()>0)
						{
							logger.info(" antes entra!!! valorSubTotal-->"+valorSubTotal+ "    valor unitario a sumar-->"+detalleConvenioServPrograma.getValorUnitario()+" *  cantidad ->"+dtop.getCantidad());
							valorSubTotal= valorSubTotal.add(detalleConvenioServPrograma.getValorTotalConvenioXProgServ(dtop.getCantidad()));
							logger.info(" despues entra!!! valorSubTotal-->"+valorSubTotal);
						}	
					}
				}
			}
			
			 
			logger.info("FINALES--------------------------------------");
			logger.info("---------------------------------------------");
			dtoTotalesXConvenio.setValorSubTotalSinContratar(valorSubTotal);
			dtoTotalesXConvenio.setValorSubTotalContratado(new BigDecimal(0));
			dtoTotalesXConvenio.setConvenio(dtoTotalesXConvenio.getConvenio());
			dtoTotalesXConvenio.setPresupuesto(new BigDecimal(0));
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param paciente
	 * @param forma
	 * @param mapping
	 * @param response
	 * @return
	 */
	private ActionForward accionProcesoCancelado( PersonaBasica paciente, PresupuestoOdontologicoForm forma, ActionMapping mapping, HttpServletResponse response, String idSesion) 
	{
		@SuppressWarnings("unused")
		boolean resultado=PresupuestoOdontologico.cancelarProcesoPresupuesto(paciente.getCodigoCuenta(), idSesion);
		String paginaSiguiente=forma.getSiguientePagina();
		if(!paginaSiguiente.equals(""))
		{
			try
			{
				logger.info("paginaSiguiente "+paginaSiguiente);
				response.sendRedirect(paginaSiguiente);
			}
			catch (IOException e)
			{
				logger.error("Error redireccionando a la pï¿½gina seleccionada "+e);
			}
		}
		return null;
	}
	
//	/**
//	 * 
//	 * @param mapping
//	 * @param request
//	 * @param forma
//	 * @param paciente
//	 * @return
//	 */
//	private ActionForward accionProximaCita(ActionMapping mapping,
//			PresupuestoOdontologicoForm forma, UsuarioBasico usuario)
//	{
//		
//		logger.info("\n\n********************************************PROXIMA CITA*********************************************************");
//		
//		if(!forma.getProximaCitaDto().getGuardoProximaCita())
//		{	
//			forma.getProximaCitaDto().setFecha(UtilidadFecha.getFechaActual());
//			
//			//cargamos el dtoProximaCita
//			ArrayList<String> unique= new ArrayList<String>();
//			
//			for(DtoPresuOdoProgServ progServ: forma.getListaProgramasServicios())
//			{
//				int contrato=0;
//				for(DtoPresupuestoOdoConvenio convenio: progServ.getListPresupuestoOdoConvenio())
//				{
//					if(UtilidadTexto.getBoolean(convenio.getContratado()))
//					{
//						contrato=convenio.getContrato().getCodigo();
//						break;
//					}
//				}
//				
//				logger.info("contrato=="+contrato);
//				
//				if(contrato>0)
//				{
//					for(DtoPresupuestoPiezas pieza: progServ.getListPresupuestoPiezas())
//					{
//						DtoPresupuestoPlanTratamientoNumeroSuperficies dtoNumSup= new DtoPresupuestoPlanTratamientoNumeroSuperficies();
//						dtoNumSup.setHallazgo(new InfoDatosInt(pieza.getHallazgo().intValue()));
//						dtoNumSup.setPiezaDental(new InfoDatosInt(pieza.getPieza().intValue()));
//						dtoNumSup.setPresupuesto(forma.getDtoPresupuesto().getCodigoPK());
//						dtoNumSup.setPrograma(new InfoDatosInt(Utilidades.convertirAEntero(progServ.getPrograma().getCodigo()+""), progServ.getPrograma().getNombre()));
//						dtoNumSup.setSeccion(new InfoIntegridadDominio(pieza.getSeccion()));
//						
//						Connection con= UtilidadBD.abrirConexion();
//						ArrayList<InfoNumSuperficiesPresupuesto> superficiesAplica= NumeroSuperficiesPresupuesto.obtenerInfoNumSuperficiesPresupuestoXColorSuperfice(con, dtoNumSup, pieza.getSuperficie().intValue());
//						
//						Log4JManager.info(superficiesAplica);
//						UtilidadBD.closeConnection(con);
//						
//						String uniqueStr=	pieza.getSeccion()
//						+"_"+pieza.getPieza().intValue()
//						+"_"+pieza.getHallazgo().intValue()
//						+"_"+forma.getDtoPresupuesto().getCodigoPK()
//						+"_"+progServ.getPrograma().getCodigo();
//						
//						if(superficiesAplica.size()>0)
//						{
//							uniqueStr+= "_"+superficiesAplica.get(0).getColor();
//						}	
//						
//						if(!unique.contains(uniqueStr))
//						{
//							unique.add(uniqueStr);
//							DtoDetalleProximaCita dtoDetalleProximaCita= new DtoDetalleProximaCita();
//							dtoDetalleProximaCita.setPiezaDental(new InfoDatosInt(pieza.getPieza().intValue(), UtilidadOdontologia.obtenerNombrePieza(pieza.getPieza().intValue()) ));
//							
//							if(superficiesAplica.size()>0)
//							{
//								for(InfoNumSuperficiesPresupuesto info: superficiesAplica)
//								{
//									dtoDetalleProximaCita.getSuperficies().add( new InfoDatosDouble(Utilidades.convertirADouble(info.getCodigoSuperficie()+""), UtilidadOdontologia.obtenerNombreSuperficie(new BigDecimal(info.getCodigoSuperficie()))));
//								}
//							}
//							else
//							{
//								dtoDetalleProximaCita.getSuperficies().add(new InfoDatosDouble(pieza.getSuperficie().doubleValue(), UtilidadOdontologia.obtenerNombreSuperficie(pieza.getSuperficie())));
//							}
//							
//							if(forma.getUtilizaPrograma())
//							{
//								logger.info("list");
//								dtoDetalleProximaCita.setPrograma(progServ.getPrograma());
//								dtoDetalleProximaCita.setServicios(PlanTratamiento.cargarServiciosDeProgramasPlanT(forma.getDtoPlanTratamiento().getCodigoPk(), progServ.getPrograma().getCodigo(), pieza.getPieza().intValue(), pieza.getSuperficie().intValue(), pieza.getHallazgo().intValue(), usuario.getCodigoInstitucionInt()));
//
//								
//								for(InfoServicios info: dtoDetalleProximaCita.getServicios())
//								{
//									info.setContrato(contrato);
//								}
//							}
//							else 
//							{
//								//falta hacer la implementacion cuando no utiliza programas
//								//dtoDetalleProximaCita.setServicios(progServ.getServicio());
//							}
//							forma.getProximaCitaDto().getListaDetalle().add(dtoDetalleProximaCita);
//						}
//					}	
//				}
//			}
//		}	
//		return mapping.findForward("paginaProximaCita");
//	}
//	
//	/**
//	 * 
//	 * @param mapping
//	 * @param forma
//	 * @param paciente
//	 * @param usuario
//	 * @return
//	 */
//	private ActionForward accionGuardarProximaCita(ActionMapping mapping,
//			PresupuestoOdontologicoForm forma, PersonaBasica paciente,
//			UsuarioBasico usuario, HttpServletRequest request)
//	{
//		Connection con= UtilidadBD.abrirConexion();
//		/*
//		 *TODO VALIDAR EL FUNCIONAMIENTO DE ESTA LIENA  CARVAJAL  
//		 */
//		forma.getProximaCitaDto().setGuardoProximaCita(CitaOdontologica.insertarProximaCitaOdontologia(con, forma.getProximaCitaDto().getTodosServicios(), forma.getProximaCitaDto().getFecha(), paciente.getCodigoPersona(), usuario.getLoginUsuario() , ConstantesBD.codigoNuncaValido, usuario.getCodigoCentroCosto())>0);
//	
//		if(!forma.getProximaCitaDto().getGuardoProximaCita())
//		{
//			logger.error("no inserta la proxima cita");
//			UtilidadBD.closeConnection(con);
//			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
//		}
//		UtilidadBD.closeConnection(con);
//		forma.setEstado("contratadoExitoso");
//		return mapping.findForward("contratarPresupuesto");
//	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionGenerarNuevaSolicitudDescuento(
			ActionMapping mapping, PresupuestoOdontologicoForm forma,
			UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request)
	{
		///en este caso sigue precontratado el presupuesto,
		Connection con= UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		///falta metodo para actualizar las tarifas , bonos y promociones
		if(!UtilidadTexto.isEmpty(forma.getDtoPresupuestoContratado().getEstadoDescuentoNuevo()))
		{
			///if es anular entonces debemos insertar el motivo
			if(forma.getDtoPresupuestoContratado().getEstadoDescuentoNuevo().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado))
			{	
				if(!PresupuestoOdontologico.anularDescuentoPresupuesto(con, forma.getDtoPresupuesto().getCodigoPK(), usuario.getLoginUsuario(), ValoresPorDefecto.getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologicoInt(usuario.getCodigoInstitucionInt())/*forma.getCodigoMotivoAnulacionDctoSel()*/))
				{
					logger.error("NO Presupuesto");
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
				}
			}
			
			////luego de anulado generamos una nueva solicitud
			//2. SE INSERTA LA SOLICITUD DE AUTORIZACION DE DCTO ODON
			ActionForward forward= insertarNuevaSolicitudDctoOdo(mapping, forma, usuario, request, con);
			if(forward!=null)
			{	
				return forward;
			}
		}
		
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		forma.setEstado("contratadoExitoso");
		return mapping.findForward("contratarPresupuesto"); 
	}

	/**
	 * 
	 * Metodo para cargar los paquetes que pueden aplicar para el presupuesto
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private ActionForward accionCargarPaquetes(		ActionMapping mapping,
													PresupuestoOdontologicoForm forma,
													UsuarioBasico usuario, PersonaBasica paciente
													) throws IPSException 
	{
		ArrayList<InfoPaquetesPresupuesto> paquetesConsultados= PresupuestoPaquetes.cargarPaquetesAplicanPresupuesto(forma.getListaProgramasServicios(), forma.getListaSumatoriaConvenios(), forma.getListaPaquetes());
		if(forma.getListaPaquetes().size()>0)
		{	
			forma.setListaPaquetes(PresupuestoPaquetes.cargarPaquetesAplicanPresupuestoActualizado(forma.getListaPaquetes(), paquetesConsultados));
		}
		else
		{
			forma.setListaPaquetes(paquetesConsultados);
		}
		return mapping.findForward("cargarPaquetes");
	}

	/**
	 * 
	 * Metodo para cargar el detalle del paquete
	 * @param mapping
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @param forma 
	 * @see
	 */
	private ActionForward accionCargarDetallePaquete(ActionMapping mapping, PresupuestoOdontologicoForm forma) 
	{
		for(InfoPaquetesPresupuesto info: forma.getListaPaquetes())
		{
			if(info.getDetallePkPaqueteOdonCONVENIO()==forma.getDetallePaqueteOdonCONVENIOSeleccionado())
			{	
				forma.setDetallePaquete(info.getListaProgramas());
				break;
			}	
		}	
		return mapping.findForward("cargarDetallePaquete");
	}
	
	/**
	 * 
	 * Metodo para actualizar el paquete seleccionado o no 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private ActionForward accionActualizarProgramasXPaquetes(
			ActionMapping mapping, 
			PresupuestoOdontologicoForm forma, UsuarioBasico usuario,
			PersonaBasica paciente) throws IPSException 
	{
		//debemos aplicar los paquetes
		armarEstructuraPaquetesXTodos(forma, usuario, paciente);
		///hacemos el ordenamiento x especialidad
		Collections.sort(forma.getListaProgramasServicios());
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * 
	 * Metodo para armar la estructura del paquete de todos los paquetes seleccionados
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private void armarEstructuraPaquetesXTodos(PresupuestoOdontologicoForm forma,	UsuarioBasico usuario, PersonaBasica paciente) throws IPSException 
	{
		for(InfoPaquetesPresupuesto info: forma.getListaPaquetes())
		{
			PresupuestoPaquetes.limpiarPaquete(info, forma.getListaProgramasServicios(), usuario, paciente);
			actualizacionTotales(forma, usuario, paciente);
		}
		
		for(InfoPaquetesPresupuesto info: forma.getListaPaquetes())
		{
			if(info.isSeleccionado())
			{
				PresupuestoPaquetes.desarmarProgramasAgrupadosCantidad1a1(info, forma.getListaProgramasServicios(), usuario);
				actualizacionTotales(forma, usuario, paciente);
			}
		}
		
		// agrupamos las cantidades que posiblemente habian quedado con cantidad 1 pero ya no tienen paquetes y queden agrupadas en cantidades
		PresupuestoPaquetes.agruparCantidades(forma.getListaProgramasServicios());
		
	}
	
	
	/**
	 * Imprime el presupuesto del contrato
	 * @param forma
	 * @param dtoCheckBox
	 */
	private void imprimirContratoPresupuesto(PresupuestoOdontologicoForm forma, DtoCheckBox dtoCheckBox, com.servinte.axioma.orm.CentroAtencion centroAtencion)
	{
		Log4JManager.info(">>>>> Imprimiendo tipo Contrato");
		
		ContratoOdontologicoMundo contratoOdontologicoMundo = new ContratoOdontologicoMundo();
		DtoFormatoImpresionContratoOdontologico dtoFormatoImpresionContratoOdontologico = new DtoFormatoImpresionContratoOdontologico();
		dtoFormatoImpresionContratoOdontologico.setCodigoPkPresupuesto(forma.getDtoPresupuesto().getCodigoPK().longValue());
		dtoFormatoImpresionContratoOdontologico.setValorTotalPresupuesto(forma.getDtoPresupuestoContratado().getTotalPresupuestoParaDescuentoFormateado());
		
		for (DtoCheckBox dtoCheckBoxSeleccionado : forma.getListaOpcionesImprimirMostrar()) 
		{
			if(dtoCheckBox.isCheck()== true)
			{
				dtoFormatoImpresionContratoOdontologico.getListaAnexosImpresos().add(dtoCheckBoxSeleccionado);
			}
		}
		
		try 
		{
			dtoFormatoImpresionContratoOdontologico = contratoOdontologicoMundo.generarImpresionContratoOdontologico(dtoFormatoImpresionContratoOdontologico);
			dtoFormatoImpresionContratoOdontologico.setDireccionInstitucion(centroAtencion.getDireccion());
			dtoFormatoImpresionContratoOdontologico.setTelefonoInstitucion(centroAtencion.getTelefono());
			if (UtilidadTexto.isEmpty(dtoFormatoImpresionContratoOdontologico.getPacDireccion())) {
				dtoFormatoImpresionContratoOdontologico.setPacDireccion("");
			}
			if (UtilidadTexto.isEmpty(dtoFormatoImpresionContratoOdontologico.getPacTelefono())) {
				dtoFormatoImpresionContratoOdontologico.setPacTelefono("");
			}
			forma.setObjImpresionContratoPresupuesto(dtoFormatoImpresionContratoOdontologico);
			
			forma.setMostrarImpresionContratoOdonto(true);
			//return mapping.findForward("paginaDetalle");
			
		} catch (Exception e) {
			Log4JManager.error("Error generando la impresion del presupuesto contratado. ",e);
		}
	}
	
	
	
	/**
	 * Imprime las recomendaciones del contrato
	 * @param forma
	 * @param dtoCheckBox
	 */
	private void imprimirRecomendacionesPresupuesto(PresupuestoOdontologicoForm forma, DtoCheckBox dtoCheckBox, com.servinte.axioma.orm.CentroAtencion centroAtencion)
	{
		Log4JManager.info(">>>>> Imprimiendo tipo Recomendaciones");
		
		ContratoOdontologicoMundo contratoOdontologicoMundo = new ContratoOdontologicoMundo();
		
		DtoFormatoImpresionContratoOdontologico dtoFormatoImpresionRecomendacionesOdontologico = new DtoFormatoImpresionContratoOdontologico();
		dtoFormatoImpresionRecomendacionesOdontologico.setCodigoPkPresupuesto(forma.getDtoPresupuesto().getCodigoPK().longValue());
		dtoFormatoImpresionRecomendacionesOdontologico.setValorTotalPresupuesto(forma.getDtoPresupuestoContratado().getTotalPresupuestoParaDescuentoFormateado());
		
		try 
		{
			dtoFormatoImpresionRecomendacionesOdontologico = contratoOdontologicoMundo.generarImpresionRecomendacionesOdontologico(dtoFormatoImpresionRecomendacionesOdontologico);
			dtoFormatoImpresionRecomendacionesOdontologico.setDireccionInstitucion(centroAtencion.getDireccion());
			dtoFormatoImpresionRecomendacionesOdontologico.setTelefonoInstitucion(centroAtencion.getTelefono());
			if (UtilidadTexto.isEmpty(dtoFormatoImpresionRecomendacionesOdontologico.getPacDireccion())) {
				dtoFormatoImpresionRecomendacionesOdontologico.setPacDireccion("");
			}
			if (UtilidadTexto.isEmpty(dtoFormatoImpresionRecomendacionesOdontologico.getPacTelefono())) {
				dtoFormatoImpresionRecomendacionesOdontologico.setPacTelefono("");
			}
			forma.setObjImpresionRecomendacionesPresupuesto(dtoFormatoImpresionRecomendacionesOdontologico);
			forma.setMostrarImpresionRecomendacionesOdonto(true);
			
			Log4JManager.info("================================================= recomendaciones seteadas en la forma: "+forma.getObjImpresionRecomendacionesPresupuesto().getListaRecomendaciones().size());
			
		} catch (Exception e) {
			Log4JManager.error("Error generando la impresion de recomendaciones contratado. ",e);
		}
	}
	
	
	
	/**
	 * Imprime Otro Si del contrato
	 * @param forma
	 * @param request
	 * @param otroSi
	 * @param usuario
	 * @param i
	 * 
	 * @autor Cristhian Murillo
	 */
	private void imprimirOtroSiPresupuesto(PresupuestoOdontologicoForm forma, HttpServletRequest request, UsuarioBasico usuario, long otroSi, int i)
	{
		Log4JManager.info(">>>>> Imprimiendo tipo Otro Si");
		
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Cargar Reporte Presupuesto 
		String newPathReport= PresupuestoHelper.armarPdfOtrosSiPresupuesto(forma.getDtoPresupuesto().getCodigoPK(), institucion, usuario.getCodigoInstitucionInt(), otroSi);
		
		if(!UtilidadTexto.isEmpty(newPathReport))
		{
			//request.setAttribute("newPathReport"+i, newPathReport);
			request.setAttribute("isOpenReport2", "true");
			request.setAttribute("newPathReport2", newPathReport);
		}
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar el detalle de un presupuesto
	 * odontológico desde otra funcionalidad del sistema
	 * 
	 * @param ActionMapping mapping, PresupuestoOdontologicoForm forma, 
			  UsuarioBasico usuario, HttpServletRequest request
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward consultarDetallePresupuestoOtraFuncionalidad(	ActionMapping mapping, PresupuestoOdontologicoForm forma, 
			UsuarioBasico usuario, HttpServletRequest request){
		DtoPresupuestoOdontologico dto = new DtoPresupuestoOdontologico(); 
		
		forma.setDetallePresupuestoOtraFuncionalidad(true);
		dto.setCodigoPK(new BigDecimal(forma.getPresupuestoID()));
		
		DtoPresupuestoOdontologico dtoWhere = new DtoPresupuestoOdontologico();
		dtoWhere.setCodigoPK(dto.getCodigoPK());
		ArrayList<DtoPresupuestoOdontologico> listaResultado = PresupuestoOdontologico.cargarPresupuesto(dtoWhere);
		
		if(listaResultado!=null && listaResultado.size()>0){
			
			dto.setConsecutivo(listaResultado.get(0).getConsecutivo());
			dto.setEstado(listaResultado.get(0).getEstado());
			DtoInfoFechaUsuario dtoUsuarioModifica = new DtoInfoFechaUsuario();
			dtoUsuarioModifica.setNombreUsuarioModifica(usuario.getNombreUsuario());
			
			Date fechaActual = (Calendar.getInstance().getTime());		
			dtoUsuarioModifica.setFechaModifica(UtilidadFecha.conversionFormatoFechaAAp(fechaActual));
			dto.setUsuarioModifica( dtoUsuarioModifica);
			
			forma.setDtoPresupuesto(dto);
			
			//SE DEFINE SI ES X PROGRAMA O SERVICIO
			forma.setTipoRelacion(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(
					usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)?"Programa":"Servicio");
			
			//CARGAMOS LOS PROGRAMAS/SERVICIOS DEL PRESUPUESTO
			forma.setListaProgramasServicios(PresupuestoOdontologico.cargarPresupuestoOdoProgServ(forma.getDtoPresupuesto().getCodigoPK()));
			
			//CARGAMOS LOS DETALLES DE CONVENIOS Y LAS PIEZAS ASOCIADAS
			if(forma.getListaProgramasServicios()!=null && forma.getListaProgramasServicios().size()>0){
				this.cargarConveniosPiezasXProgramaServicio(forma, false);
			}
			
			//SE CARGAN LOS TOTALES
			forma.setListaSumatoriaConvenios(PresupuestoOdontologico.cargarTotalesPresupuetoConvenio(forma.getDtoPresupuesto().getCodigoPK()));
			
			//SE CARGAN LOS TOTALES CUANDO ESTA CONTRATADO
			forma.setDtoPresupuestoContratado(PresupuestoOdontologico.obtenerTotalesContratadoPrecontratado(forma.getDtoPresupuesto().getCodigoPK()));
			
			//Llenar las opciones para imprimir el presupuesto
			forma.setMostrarImpresionContratoOdonto(false);
			llenarListaOpcionesImprimir(forma, usuario, request);
		}
		
		return mapping.findForward("paginaDetalle");
		
	}	
	
	
	/**
	 * Método que se encarga de validar si se debe o no iniciar
	 * el proceso centralizado de Próxima cita antes de guardar la contratación del presupuesto
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param precontratar 
	 * @param es 
	 * @param preContratar
	 * @return
	 */
	private ActionForward validarGuardarPresupuesto (ActionMapping mapping,
			PresupuestoOdontologicoForm forma, 
			UsuarioBasico usuario,
			PersonaBasica paciente, 
			HttpServletRequest request) {

		if (!forma.getEstado().equals("guardarPrecontratado") && !forma.getEstado().equals("guardarPresupuestoPrecontratadoDirecto") && UtilidadTexto.getBoolean(ValoresPorDefecto.getEsRequeridoProgramarCitaAlContratarPresupuestoOdon(usuario.getCodigoInstitucionInt()))){
			   
		   /*
			* Es requerido programar la próxima cita
			*/
	
			return mapping.findForward(programarProximaCita(forma, paciente, request));
		
		}else{
			
			return accionGuardarContratadoPreContratadoYDirecto(mapping, forma, usuario, paciente, request);
		}
	}
	
	
	/**
	 * 
	 * Método que se encarga de cargar la información necesaria para que el proceso centralizado
	 * pueda guardar la programación de la próxima cita.
	 * 
	 * @param forma
	 * @param paciente
	 * @param request
	 * @return
	 */
	private String programarProximaCita(PresupuestoOdontologicoForm forma, PersonaBasica paciente, HttpServletRequest request) {
		
		DtoProcesoCitaProgramada dtoProcesoCitaProgramada=  new DtoProcesoCitaProgramada();
		dtoProcesoCitaProgramada.setCodigoPersona(paciente.getCodigoPersona());
		dtoProcesoCitaProgramada.setCodigoPresupuesto(forma.getDtoPresupuesto().getCodigoPK());
		dtoProcesoCitaProgramada.setCodigoPlanTratamiento(forma.getDtoPlanTratamiento().getCodigoPk());
		dtoProcesoCitaProgramada.setProgramasServiciosPresupuesto(forma.getListaProgramasServicios());
	   
		request.getSession().setAttribute("informacionProximaCitaProgramada", dtoProcesoCitaProgramada);
	   
		forma.setEstadoTemporal(forma.getEstado());
		
		forma.setAbrePopUpProximaCita(true);
		//forma.setEstado("proximaCita");
	   
		
		return "contratarPresupuesto";
		
		//return "popUpProximaCitaProgramada";
	}
	
	/**
	 * Método que se encarga de validar si se ha generado adecuadamente la Próxima cita
	 * desde el proceso centralizado antes de iniciar el proceso de guardar la contratación del presupuesto
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param precontratar
	 * @return 
	 */
	private ActionForward verificarRegistroProximaCita (ActionMapping mapping,
			PresupuestoOdontologicoForm forma, 
			UsuarioBasico usuario,
			PersonaBasica paciente, 
			HttpServletRequest request) {
		
		//se guardo la proxima cita, entonces se debe continuar con el proceso
		if(forma.getCodigoProximaCitaRegistrada()>0)
		{
			forma.setAbrePopUpProximaCita(false);
			return accionGuardarContratadoPreContratadoYDirecto(mapping, forma, usuario, paciente, request);
		
		}else{
			   
		   /*
			* Si no se guarda, no debe hacer nada, el proceso no es exitoso.
			*/
			   
			Log4JManager.info("no se guardo la cita correctamente");
	
			forma.setAbrePopUpProximaCita(true);
		   	return mapping.findForward("contratarPresupuesto");
		}
	}
}

