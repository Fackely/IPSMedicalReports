package com.princetonsa.action.capitacion;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.capitacion.ConstantesCapitacion;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.actionform.capitacion.ConsultarImprimirOrdenesCapitacionSubcontratadaForm;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.capitacion.DtoConvenioReporte;
import com.servinte.axioma.dto.capitacion.DtoDiaCierre;
import com.servinte.axioma.dto.capitacion.DtoTipoConsulta;
import com.servinte.axioma.generadorReporte.capitacion.ordenesCapitacionSubcontratada.GeneradorReporteOrdenesCapitacionSubcontratada;
import com.servinte.axioma.generadorReporte.comun.IConstantesReporte;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.impl.administracion.InstitucionesMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IInstitucionesMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IOrdenCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.ParamPresupuestosCap;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 *   Action, controla todas las opciones dentro de la consulta
 *   de ordenes de capitación subcontratada
 *   incluyendo los posibles casos de error y los casos de flujo.
 * @version 1.0, Abr 18, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 */
/**
 * @author ricruico
 *
 */
public class ConsultarImprimirOrdenesCapitacionSubcontratadaAction extends
		Action {
	
	/**
     * Objeto para manejar los logs de esta clase
    */
    public Logger logger = Logger.getLogger(ConsultarImprimirOrdenesCapitacionSubcontratadaAction.class);
    
    /** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.capitacion.ConsultarImprimirOrdenesCapitacionSubcontratadaForm");
    
    /**
	 * Método excute del Action
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            						HttpServletRequest request, HttpServletResponse response)
    								throws Exception{
    	ActionErrors errores=new ActionErrors();
		ConsultarImprimirOrdenesCapitacionSubcontratadaForm forma=(ConsultarImprimirOrdenesCapitacionSubcontratadaForm)form;
    	try{
    	   	if(form instanceof ConsultarImprimirOrdenesCapitacionSubcontratadaForm){
    		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    		String estado=forma.getEstado();
			if(estado.equals("empezar")){
				return mapping.findForward(llenarForma(forma, request, errores, usuario));
			}
			else if(estado.equals("listarContratosConvenio")){
				return mapping.findForward(llenarListadoContratos(forma, request, errores));
			}
			else if(estado.equals("consultarOrdenes")){
				
				//Se valida que el mes seleccionado sea mayor o igual al mes del
				//parametro general  'Fecha Inicio de Cierre de Ordenes Medicas'
				String fechaInicioCierreOrdenMedicaStr = ValoresPorDefecto.getFechaInicioCierreOrdenMedica(usuario.getCodigoInstitucionInt());
				Date fechaInicioCierre;
				if(fechaInicioCierreOrdenMedicaStr != null){
		    		fechaInicioCierre=UtilidadFecha.conversionFormatoFechaStringDate(fechaInicioCierreOrdenMedicaStr);
		    		Calendar mesAnioSeleccionado=Calendar.getInstance();
		    		Calendar mesAnioCierre=Calendar.getInstance();
		    		mesAnioSeleccionado.set(Calendar.YEAR, forma.getIdAnio());
		    		mesAnioSeleccionado.set(Calendar.MONTH, forma.getIdMes());
		    		mesAnioCierre.setTime(fechaInicioCierre);
		    		if(mesAnioSeleccionado.get(Calendar.YEAR) < mesAnioCierre.get(Calendar.YEAR)
		    			 || (mesAnioSeleccionado.get(Calendar.YEAR)==mesAnioCierre.get(Calendar.YEAR)
		    					&& mesAnioSeleccionado.get(Calendar.MONTH)<mesAnioCierre.get(Calendar.MONTH))){
		    			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		    			errores.add("Error Mes Anio Inferior a Fecha Cierre",
								new ActionMessage("errors.notEspecific", messageResource.getMessage(
										"consultarImprimirOrdenesCapitacionSubcontratada.mesAnioInvalido", new Object[]{format.format(fechaInicioCierre)})));
						saveErrors(request, errores);
		    			forma.setTipoSalida(null);
						forma.setEnumTipoSalida(null);
						return mapping.findForward("principal");
		    		}
		    	}
				else{
					errores.add("No Parametro",
							new ActionMessage("errors.notEspecific", messageResource.getMessage(
									"consultarImprimirOrdenesCapitacionSubcontratada.noParametro")));
					saveErrors(request, errores);
	    			forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					return mapping.findForward("principal");
				}
				
				int idTipoSalida=Integer.parseInt(forma.getTipoSalida());
				if (idTipoSalida != ConstantesBD.codigoNuncaValido) {
					int tipoConsulta=consultarOrdenes(forma, request, errores, usuario, fechaInicioCierre);
					if(tipoConsulta==ConstantesBD.codigoNuncaValido){
						forma.setTipoSalida(null);
						forma.setEnumTipoSalida(null);
						return mapping.findForward("principal");
					}
					else{
						JasperReportBuilder reportBuild=null;
						String nombreReporte="";
						if(idTipoSalida == EnumTiposSalida.PLANO.getCodigo()){
							if(tipoConsulta==ConstantesCapitacion.codigoTipoConsultaNivel){
								reportBuild=generarReporte(forma, request, errores, 
												IConstantesReporte.formatoPlanoA, usuario);
								nombreReporte=ConstantesCapitacion.reporteNivel;
							}
							else if(tipoConsulta==ConstantesCapitacion.codigoTipoConsultaConvenioContrato){
								reportBuild=generarReporte(forma, request, errores, 
										IConstantesReporte.formatoPlanoB, usuario);
								nombreReporte=ConstantesCapitacion.reporteConvenioContrato;
							}
							else if(tipoConsulta==ConstantesCapitacion.codigoTipoConsultaGrupoSClaseI){
								reportBuild=generarReporte(forma, request, errores, 
										IConstantesReporte.formatoPlanoC, usuario);
								nombreReporte=ConstantesCapitacion.reporteGrupoClase;
							}
							if(reportBuild != null){
								GeneradorReporteDinamico generadorReporte = new GeneradorReporteDinamico();
								String nombreArchivo=generadorReporte.exportarReporteArchivoPlano(reportBuild, nombreReporte);
								forma.setNombreReporte(nombreArchivo);
							}
							else{
								errores.add("Error Reporte no tiene Datos",
										new ActionMessage("errors.notEspecific", messageResource.getMessage(
												"consultarImprimirOrdenesCapitacionSubcontratada.contratoSinParametrizacion")));
								saveErrors(request, errores);
								forma.setTipoSalida(null);
								forma.setEnumTipoSalida(null);
								return mapping.findForward("principal");
							}
						}
						else{
							if(tipoConsulta==ConstantesCapitacion.codigoTipoConsultaNivel){
								reportBuild=generarReporte(forma, request, errores, 
												IConstantesReporte.formatoA, usuario);
								nombreReporte=ConstantesCapitacion.reporteNivel;
							}
							else if(tipoConsulta==ConstantesCapitacion.codigoTipoConsultaConvenioContrato){
								reportBuild=generarReporte(forma, request, errores, 
										IConstantesReporte.formatoB, usuario);
								nombreReporte=ConstantesCapitacion.reporteConvenioContrato;
							}
							else if(tipoConsulta==ConstantesCapitacion.codigoTipoConsultaGrupoSClaseI){
								reportBuild=generarReporte(forma, request, errores, 
										IConstantesReporte.formatoC, usuario);
								nombreReporte=ConstantesCapitacion.reporteGrupoClase;
							}
							if(reportBuild != null){
								GeneradorReporteDinamico generadorReporte = new GeneradorReporteDinamico();
								String nombreArchivo="";
								if(idTipoSalida == EnumTiposSalida.PDF.getCodigo()){
									nombreArchivo=generadorReporte.exportarReportePDF(reportBuild, nombreReporte);
								}
								if(idTipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo()){
									nombreArchivo=generadorReporte.exportarReporteExcel(reportBuild, nombreReporte);
								}
								forma.setNombreReporte(nombreArchivo);
							}
							else{
								errores.add("Error Reporte no tiene Datos",
										new ActionMessage("errors.notEspecific", messageResource.getMessage(
												"consultarImprimirOrdenesCapitacionSubcontratada.contratoSinParametrizacion")));
								saveErrors(request, errores);
								forma.setTipoSalida(null);
								forma.setEnumTipoSalida(null);
								return mapping.findForward("principal");
							}
						}
					}
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
				}
				return mapping.findForward("principal");
			}
			else if(estado.equals("volverMenu")){
				return mapping.findForward("volverMenu");
			}
			else if(estado.equals("volverBusqueda")){
				return mapping.findForward("volverBusqueda");
			}
		}
    	}
    	catch(Exception e){
    		forma.setTipoSalida(null);
			forma.setEnumTipoSalida(null);
    		errores.add("Error Exportando el Reporte",
					new ActionMessage("errors.notEspecific", e.getMessage()));
			saveErrors(request, errores);
    		return mapping.findForward("principal");
    	}
		return mapping.findForward("principal");    
    }

    /**Método que se encarga de obtener del archivo de propiedades los tipos de consulta disponibles
     * @return String
     */
    private String llenarForma(ConsultarImprimirOrdenesCapitacionSubcontratadaForm forma, HttpServletRequest request, ActionErrors errores, UsuarioBasico usuario){
    	try{
    		Calendar anio2010 = Calendar.getInstance();
	    	anio2010.set(Calendar.YEAR, 2010);
	    	anio2010.set(Calendar.MONTH, 0);
	    	anio2010.set(Calendar.DAY_OF_MONTH, 1);
	    	Calendar anioActual = Calendar.getInstance();
	    	forma.reset();
	    	//Se valida que por lo menos exista un cierre
	    	ICierrePresupuestoCapitacionoMundo presupuestoMundo=CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
	    	UtilidadTransaccion.getTransaccion().begin();
	    	Date fechaInicio=presupuestoMundo.obtenerPrimerDiaCierrePresupuesto();
	    	if(fechaInicio != null){
		    	forma.setIdAnio(anioActual.get(Calendar.YEAR));
		    	forma.setIdMes(anioActual.get(Calendar.MONTH));
		    	forma.setListadoAnios(UtilidadFecha.obtenerListaDtoAnios(anio2010.getTime(), new Date()));
		    	forma.setListadoMeses(UtilidadFecha.obtenerListaDtoMes());
		    	forma.setListadoTipoConsulta(obtenerTiposConsulta());
		    	forma.setListadoConvenios(obtenerListadoConvenios(request, usuario, errores));
	    	}
	    	else{
	    		request.setAttribute("codigoDescripcionError", "errors.noExisteCierre");
	    		return "paginaError";
	    	}
    	}
    	catch(Exception e){
    		logger.error(e);
    		errores.add("Error Llenando Forma",new ActionMessage("errors.notEspecific",e.getMessage()));
    		saveErrors(request, errores);
    	}
    	finally{
    		UtilidadTransaccion.getTransaccion().commit();
    	}
    	return "principal";
    }
    
    
    /**Método que se encarga de obtener del archivo de propiedades los tipos de consulta disponibles
     * @return List<DtoTipoConsulta>
     */
    private List<DtoTipoConsulta> obtenerTiposConsulta(){
    	List<DtoTipoConsulta> tiposConsulta = new ArrayList<DtoTipoConsulta>();
    	DtoTipoConsulta tipoConsulta = new DtoTipoConsulta();
    	tipoConsulta.setCodigo(ConstantesCapitacion.codigoTipoConsultaNivel);
    	tipoConsulta.setDescripcion(messageResource.getMessage(
    							"consultarImprimirOrdenesCapitacionSubcontratada.tipoConsulta"+
    							ConstantesCapitacion.codigoTipoConsultaNivel));
    	tiposConsulta.add(tipoConsulta);
    	tipoConsulta= new DtoTipoConsulta();
    	tipoConsulta.setCodigo(ConstantesCapitacion.codigoTipoConsultaConvenioContrato);
    	tipoConsulta.setDescripcion(messageResource.getMessage(
    							"consultarImprimirOrdenesCapitacionSubcontratada.tipoConsulta"+
    							ConstantesCapitacion.codigoTipoConsultaConvenioContrato));
    	tiposConsulta.add(tipoConsulta);
    	tipoConsulta= new DtoTipoConsulta();
    	tipoConsulta.setCodigo(ConstantesCapitacion.codigoTipoConsultaGrupoSClaseI);
    	tipoConsulta.setDescripcion(messageResource.getMessage(
    							"consultarImprimirOrdenesCapitacionSubcontratada.tipoConsulta"+
    							ConstantesCapitacion.codigoTipoConsultaGrupoSClaseI));
    	tiposConsulta.add(tipoConsulta);
    	return tiposConsulta;
    }
    
    
    /**Método que se encarga de obtener los convenios activos Capitados de una institución
     * @return ArrayList<Convenios>
     */
    private ArrayList<Convenios> obtenerListadoConvenios(HttpServletRequest request, UsuarioBasico usuario, ActionErrors errores){
    	ArrayList<Convenios> convenios = new ArrayList<Convenios>();
    	IConveniosMundo convenioMundo = FacturacionFabricaMundo.crearcConveniosMundo();
    	try{
    		UtilidadTransaccion.getTransaccion().begin();
    		convenios=convenioMundo.listarConveniosCapitadosActivosPorInstitucionManejaPresupuesto(usuario.getCodigoInstitucionInt());
    		if(convenios != null && !convenios.isEmpty()){
    			return convenios;
    		}
    		else{
    			errores.add("Error Obtener Listado Convenios",new ActionMessage("errors.notEspecific",messageResource.
    							getMessage("consultarImprimirOrdenesCapitacionSubcontratada.noConvenios")));
    		}
    	}
    	catch(Exception e){
    		logger.error(e);
    		errores.add("Error Obtener Listado Convenios",new ActionMessage("errors.notEspecific",e.getMessage()));
    	}
    	finally{
    		UtilidadTransaccion.getTransaccion().commit();
    	}
    	saveErrors(request, errores);
    	return convenios;
    }
    
    
    /**Método que se encarga de obtener de los contratos asociados a un convenio y 
     * cuya fecha Inicial sea menor o igual a la fecha actual
     * @return List<DtoTipoConsulta>
     */
    private String llenarListadoContratos(ConsultarImprimirOrdenesCapitacionSubcontratadaForm forma, 
    										HttpServletRequest request,	ActionErrors errores){
    	IContratoMundo contratoMundo = FacturacionFabricaMundo.crearContratoMundo();
    	ArrayList<Contratos> contratos = new ArrayList<Contratos>();
    	try{
    		UtilidadTransaccion.getTransaccion().begin();
			if(forma.getConvenioHelper() != ConstantesBD.codigoNuncaValido){
				forma.setListadoContratos(
						contratoMundo.listarContratosPorConvenioPorFechaMenor(
								forma.getConvenioHelper(), "fechaInicial", new Date()));
			}
			else{
				forma.setListadoContratos(contratos);
			}	
	    }
    	catch(Exception e){
    		logger.error(e);
    		errores.add("Error Obtener Listado Contratos",new ActionMessage("errors.notEspecific",e.getMessage()));
    		saveErrors(request, errores);
    	}
    	finally{
    		UtilidadTransaccion.getTransaccion().commit();
    	}
    	return "listarContratosConvenio";
    }
    
    /**Método que se encarga de consultar las ordenesde capitación subcontratada y realizar
     * las validaciones correspondientes
     * @return int del tipo de consulta
     */
    private int consultarOrdenes(ConsultarImprimirOrdenesCapitacionSubcontratadaForm forma, HttpServletRequest request, ActionErrors errores, UsuarioBasico usuario, Date fechaInicioCierre){
    	try{
    		IOrdenCapitacionMundo ordenCapitacionMundo= CapitacionFabricaMundo.crearOrdenCapitacionMundo();
    		Calendar fechaReporte=Calendar.getInstance();
    		//Se setea el año y mes seleccionado por el usuario para validar
    		//los cierres diarios
    		fechaReporte.set(Calendar.YEAR, forma.getIdAnio());
    		fechaReporte.set(Calendar.MONTH, forma.getIdMes());
    		List<DtoDiaCierre> cierresDiarios = obtenerDiasCierre(fechaReporte, fechaInicioCierre);
    		cierresDiarios=ordenCapitacionMundo.validarCierresDiarios(cierresDiarios);
    		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    		boolean validate=false;
    		for(DtoDiaCierre dia:cierresDiarios){
				//Por cada día que no haya cierre se muestra un mensaje de error en el formulario
    			if(!dia.isTieneCierre()){
					validate=true;
					errores.add("Error Dia sin cierre",
							new ActionMessage("errors.diaSinCierre",formato.format(dia.getFechaCierre())));
				}
			}
    		if(forma.getIdTipoConsulta()==ConstantesCapitacion.codigoTipoConsultaNivel){
    			if(forma.getConvenioHelper()!= ConstantesBD.codigoNuncaValido){
	    			boolean existeParametrizacion=false;
	    			if(forma.getContratoHelper()!= ConstantesBD.codigoNuncaValido){
	    				existeParametrizacion=ordenCapitacionMundo.existeParametrizacionPresupuesto(
								forma.getContrato().getCodigo(), String.valueOf(fechaReporte.get(Calendar.YEAR)));
	    			}
	    			else{
	    				existeParametrizacion=ordenCapitacionMundo.existeParametrizacionPresupuestoConvenio(
								forma.getConvenio().getCodigo(), String.valueOf(fechaReporte.get(Calendar.YEAR)));
	    			}
	    			
	    			if(!existeParametrizacion){
	    				validate=true;
						errores.add("Error Contrato no tiene parametrización",
								new ActionMessage("errors.notEspecific", messageResource.getMessage(
										"consultarImprimirOrdenesCapitacionSubcontratada.contratoSinParametrizacion")));
	    			}
    			}
    		}
    		if(forma.getIdTipoConsulta()==ConstantesCapitacion.codigoTipoConsultaConvenioContrato){
    			boolean existeParametrizacion=false;
    			if(forma.getContratoHelper()!=ConstantesBD.codigoNuncaValido){
    				existeParametrizacion=ordenCapitacionMundo.existeParametrizacionPresupuesto(
							forma.getContrato().getCodigo(), String.valueOf(fechaReporte.get(Calendar.YEAR)));
    			}
    			else{
    				existeParametrizacion=ordenCapitacionMundo.existeParametrizacionPresupuestoConvenio(
							forma.getConvenio().getCodigo(), String.valueOf(fechaReporte.get(Calendar.YEAR)));
    			}
    			if(!existeParametrizacion){
    				validate=true;
					errores.add("Error Contrato no tiene parametrización",
							new ActionMessage("errors.notEspecific", messageResource.getMessage(
									"consultarImprimirOrdenesCapitacionSubcontratada.contratoSinParametrizacion")));
    			}
    		}
    		if(forma.getIdTipoConsulta()==ConstantesCapitacion.codigoTipoConsultaGrupoSClaseI){
    			boolean existeParametrizacion=ordenCapitacionMundo.existeParametrizacionDetalladaPresupuesto(
    											forma.getContrato().getCodigo(), fechaReporte);
    			if(!existeParametrizacion){
    				validate=true;
					errores.add("Error Contrato no tiene parametrizacióndetallada",
							new ActionMessage("errors.notEspecific", messageResource.getMessage(
									"consultarImprimirOrdenesCapitacionSubcontratada.contratoSinParametrizacionDetallada")));
    			}
    		}
    		if(validate){
    			saveErrors(request, errores);
    			return ConstantesBD.codigoNuncaValido;
    		}
    		int invalido=ConstantesBD.codigoNuncaValido;
    		Calendar mesAnio=Calendar.getInstance();
    		mesAnio.set(Calendar.YEAR, forma.getIdAnio());
    		mesAnio.set(Calendar.MONTH, forma.getIdMes());
    		forma.setFechaSeleccionada(UtilidadFecha.obtenerNombreMesProperties(forma.getIdMes()) +" - "
    				+forma.getIdAnio());
    		if(forma.getIdTipoConsulta()==ConstantesCapitacion.codigoTipoConsultaNivel){
    			if(forma.getConvenioHelper() != invalido
    					&& forma.getContratoHelper() != invalido){
    				if(forma.getConvenio().getCapitacionSubcontratada().charValue()==ConstantesBD.acronimoSiChar){
    					forma.setConsolidadosConveniosAutorizados(ordenCapitacionMundo.
    								obtenerConsolidadoConveniosPorNivelAtencionPorConvenioContrato(
    										mesAnio, forma.getConvenio(), forma.getContrato(), 
    										usuario.getCodigoInstitucionInt(), ConstantesBD.acronimoSiChar));
    					forma.setConsolidadosConveniosNoAutorizados(new ArrayList<DtoConvenioReporte>());
    				}
    				else if(forma.getConvenio().getCapitacionSubcontratada().charValue()==ConstantesBD.acronimoNoChar){
    					forma.setConsolidadosConveniosNoAutorizados(ordenCapitacionMundo.
    							obtenerConsolidadoConveniosPorNivelAtencionPorConvenioContrato(
									mesAnio, forma.getConvenio(), forma.getContrato(), 
									usuario.getCodigoInstitucionInt(), ConstantesBD.acronimoNoChar));
    					forma.setConsolidadosConveniosAutorizados(new ArrayList<DtoConvenioReporte>());
    				}
    			}
    			else{
	    			if(forma.getConvenioHelper() != invalido
	    					&& forma.getContratoHelper() == invalido){
	    				if(forma.getConvenio().getCapitacionSubcontratada().charValue()==ConstantesBD.acronimoSiChar){
		    				forma.setConsolidadosConveniosAutorizados(ordenCapitacionMundo.
		    								obtenerConsolidadoConveniosPorNivelAtencionPorConvenio(
		    										mesAnio, forma.getConvenio(), 
		    										usuario.getCodigoInstitucionInt(), ConstantesBD.acronimoSiChar));
		    				forma.setConsolidadosConveniosNoAutorizados(new ArrayList<DtoConvenioReporte>());
	    				}
	    				else if(forma.getConvenio().getCapitacionSubcontratada().charValue()==ConstantesBD.acronimoNoChar){ 
		    				forma.setConsolidadosConveniosNoAutorizados(ordenCapitacionMundo.
									obtenerConsolidadoConveniosPorNivelAtencionPorConvenio(
											mesAnio, forma.getConvenio(), 
											usuario.getCodigoInstitucionInt(), ConstantesBD.acronimoNoChar));
		    				forma.setConsolidadosConveniosAutorizados(new ArrayList<DtoConvenioReporte>());
	    				}
	    			}
	    			else{
	    				forma.setConsolidadosConveniosAutorizados(ordenCapitacionMundo.
								obtenerConsolidadoConveniosPorNivelAtencion(mesAnio, 
										usuario.getCodigoInstitucionInt(), ConstantesBD.acronimoSiChar));
	    				forma.setConsolidadosConveniosNoAutorizados(ordenCapitacionMundo.
								obtenerConsolidadoConveniosPorNivelAtencion(mesAnio, 
										usuario.getCodigoInstitucionInt(), ConstantesBD.acronimoNoChar));
	    			}
    			}
    			return ConstantesCapitacion.codigoTipoConsultaNivel;
    		}
    		if(forma.getIdTipoConsulta()==ConstantesCapitacion.codigoTipoConsultaConvenioContrato){
    			char esCapitacionSubcontratada=ConstantesBD.acronimoNoChar;
    			if(forma.getConvenio().getCapitacionSubcontratada() != null
						&& forma.getConvenio().getCapitacionSubcontratada().charValue()==ConstantesBD.acronimoSiChar){
    				esCapitacionSubcontratada=ConstantesBD.acronimoSiChar;
    			}
    			if(forma.getContratoHelper() != invalido){
    				forma.setConsolidadoConvenio(ordenCapitacionMundo.
    									obtenerConsolidadoConvenioPorConvenioContrato(
    										mesAnio, forma.getConvenio(), forma.getContrato(), esCapitacionSubcontratada));
    			}
    			else{
	    			forma.setConsolidadoConvenio(ordenCapitacionMundo.
								obtenerConsolidadoConvenioPorConvenio(mesAnio, forma.getConvenio(), esCapitacionSubcontratada));
    			}
    			return ConstantesCapitacion.codigoTipoConsultaConvenioContrato;
    		}
    		if(forma.getIdTipoConsulta()==ConstantesCapitacion.codigoTipoConsultaGrupoSClaseI){
    			char esCapitacionSubcontratada=ConstantesBD.acronimoNoChar;
    			if(forma.getConvenio().getCapitacionSubcontratada() != null
						&& forma.getConvenio().getCapitacionSubcontratada().charValue()==ConstantesBD.acronimoSiChar){
    				esCapitacionSubcontratada=ConstantesBD.acronimoSiChar;
    			}
    			forma.setConsolidadoContrato(ordenCapitacionMundo.
    					obtenerConsolidadoContratoPorConvenioContrato(mesAnio, forma.getConvenio(), 
    							forma.getContrato(), esCapitacionSubcontratada));
    			return ConstantesCapitacion.codigoTipoConsultaGrupoSClaseI;
    		}
	    }
    	catch(Exception e){
    		logger.error(e);
    		errores.add("Error Obtener Listado Contratos",new ActionMessage("errors.notEspecific",e.getMessage()));
    	}
    	return ConstantesBD.codigoNuncaValido;
    }
    
    /**Metodo encargado de validar en que dia existe o no un cierre
     * @param fechaReporte
     * @return
     */
    private List<DtoDiaCierre> obtenerDiasCierre(Calendar fechaReporte, Date fechaInicioCierre){
    	List<DtoDiaCierre> cierresPorDia = new ArrayList<DtoDiaCierre>();
    	int primerDiaMes=1;
    	Calendar fechaCierre= Calendar.getInstance();
    	fechaCierre.setTime(fechaInicioCierre);
    	if(fechaReporte.get(Calendar.YEAR) == fechaCierre.get(Calendar.YEAR)
    			&& fechaReporte.get(Calendar.MONTH) == fechaCierre.get(Calendar.MONTH)){
    		primerDiaMes=fechaCierre.get(Calendar.DAY_OF_MONTH);
    	}
    	
    	int ultimoDiaMes=fechaReporte.getActualMaximum(Calendar.DAY_OF_MONTH);
    	
    	Calendar fechaActual = Calendar.getInstance();
    	if(fechaReporte.get(Calendar.YEAR) == fechaActual.get(Calendar.YEAR)
    			&& fechaReporte.get(Calendar.MONTH) == fechaActual.get(Calendar.MONTH)){
    		//se le de debe asignar el dia anterior dado que el cierre para el mismo dia no se ha ejecutado
    		ultimoDiaMes=fechaActual.get(Calendar.DAY_OF_MONTH)-1;
    	}
    	for(int i=primerDiaMes; i<=ultimoDiaMes;i++){
    		fechaReporte.set(Calendar.DAY_OF_MONTH, i);
    		Date fechaDia = fechaReporte.getTime();
    		cierresPorDia.add(new DtoDiaCierre(fechaDia, false));
    	}
    	return cierresPorDia;
    }
    
    /**Metodo encargado de invocar el generador del reporte de acuerdo 
     * al formato
     * @param idFormato
     * @return JasperReportBuilder
     * @return
     */
    private JasperReportBuilder generarReporte(ConsultarImprimirOrdenesCapitacionSubcontratadaForm forma, 
    		HttpServletRequest request,ActionErrors errores, int idFormato, UsuarioBasico usuario){
    	try{
    		
    		GeneradorReporteOrdenesCapitacionSubcontratada generadorReporte = new GeneradorReporteOrdenesCapitacionSubcontratada();
    		Map<String, String> params = new HashMap<String, String>();
    		IInstitucionesMundo institucionesMundo = new InstitucionesMundo();
    		Instituciones institucion = institucionesMundo.buscarPorCodigo(usuario.getCodigoInstitucionInt());
    		params.put(IConstantesReporte.nombreInstitucion, institucion.getRazonSocial());
    		params.put(IConstantesReporte.nitInstitucion, institucion.getNit());
    		params.put(IConstantesReporte.fecha, forma.getFechaSeleccionada());
    		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
    		params.put(IConstantesReporte.fechaProcesa, sdf.format(UtilidadFecha.getFechaActualTipoBD())+" "+UtilidadFecha.getHoraActual());
    		params.put(IConstantesReporte.rutaLogo, institucion.getLogo());
    		params.put(IConstantesReporte.ubicacionLogo, institucion.getUbicacionLogoReportes());
    		params.put(IConstantesReporte.usuarioProceso, usuario.getNombreUsuario());
    		params.put(IConstantesReporte.loginUsuarioProceso, usuario.getLoginUsuario());
    		params.put(IConstantesReporte.tipoConsulta, messageResource.getMessage(
					"consultarImprimirOrdenesCapitacionSubcontratada.tipoConsulta"+
					forma.getIdTipoConsulta()));
    		if(forma.getConvenioHelper()!=ConstantesBD.codigoNuncaValido){
    			params.put(IConstantesReporte.convenio, forma.getConvenio().getNombre());
    		}
    		else{
    			params.put(IConstantesReporte.convenio, "");
    		}
    		if(forma.getContratoHelper()!=ConstantesBD.codigoNuncaValido){
    			params.put(IConstantesReporte.contrato, forma.getContrato().getNumeroContrato());
    		}
    		else{
    			params.put(IConstantesReporte.contrato, "");
    		}
    		
    		if(idFormato==IConstantesReporte.formatoA){
    			if(forma.getConsolidadosConveniosAutorizados() != null 
    					&& forma.getConsolidadosConveniosNoAutorizados() != null){
    				if(forma.getConsolidadosConveniosAutorizados().isEmpty() 
    						&& forma.getConsolidadosConveniosNoAutorizados().isEmpty()){
    					return null;
    				}
    				return generadorReporte.buildReportFormatoA(forma.getConsolidadosConveniosAutorizados(),
    										forma.getConsolidadosConveniosNoAutorizados(), params);
    			}
    			else{
    				throw new IllegalArgumentException("La lista de los DtoConvenioReporte no puede ser nula o vacia");
    			}
    		}
    		else if(idFormato==IConstantesReporte.formatoPlanoA){
    			if(forma.getConsolidadosConveniosAutorizados() != null 
    					&& forma.getConsolidadosConveniosNoAutorizados() != null){
    				if(forma.getConsolidadosConveniosAutorizados().isEmpty() 
    						&& forma.getConsolidadosConveniosNoAutorizados().isEmpty()){
    					return null;
    				}
    				return generadorReporte.buildReportPlanoFormatoA(forma.getConsolidadosConveniosAutorizados(),
    										forma.getConsolidadosConveniosNoAutorizados(), params);
    			}
    			else{
    				throw new IllegalArgumentException("La lista de los DtoConvenioReporte no puede ser nula o vacia");
    			}
    		}
    		else if(idFormato==IConstantesReporte.formatoB){
    			if(forma.getConsolidadoConvenio() != null){
    				if(forma.getConsolidadoConvenio().getContratos().isEmpty()){
    					return null;
    				}
    				return generadorReporte.buildReportFormatoB(forma.getConsolidadoConvenio().getContratos(), params);
    			}
    			else{
    				throw new IllegalArgumentException("El DtoConvenioReporte no puede ser nulo");
    			}
    		}
    		else if(idFormato==IConstantesReporte.formatoPlanoB){
    			if(forma.getConsolidadoConvenio() != null){
    				if(forma.getConsolidadoConvenio().getContratos().isEmpty()){
    					return null;
    				}
    				return generadorReporte.buildReportPlanoFormatoB(forma.getConsolidadoConvenio().getContratos(), params);
    			}
    			else{
    				throw new IllegalArgumentException("El DtoConvenioReporte no puede ser nulo");
    			}
    		}
    		else if(idFormato==IConstantesReporte.formatoC){
    			if(forma.getConsolidadoContrato() != null){
    				if(forma.getConsolidadoContrato().getNivelesAtencion().isEmpty()){
    					return null;
    				}
    				IOrdenCapitacionMundo ordenCapitacionMundo= CapitacionFabricaMundo.crearOrdenCapitacionMundo();
    				Calendar anioMes= Calendar.getInstance();
    				anioMes.set(Calendar.YEAR, forma.getIdAnio());
    				anioMes.set(Calendar.MONTH, forma.getIdMes());
    				ParamPresupuestosCap parametrizacionContrato = ordenCapitacionMundo
    																.obtenerParametrizacionContrato(
    																		forma.getContrato().getCodigo(), anioMes);
    				if(parametrizacionContrato != null){
    					NumberFormat nf = NumberFormat.getInstance();
    				    nf.setMaximumFractionDigits(2);
    					params.put(IConstantesReporte.valorMensualContrato, nf.format(parametrizacionContrato.getValorContrato().doubleValue()));
	    				params.put(IConstantesReporte.porcentajeGastoMensual, nf.format(parametrizacionContrato.getPorcentajeGastoGeneral().doubleValue()));
	    				params.put(IConstantesReporte.valorGastoMensual, nf.format(parametrizacionContrato.getValorGastoGeneral().doubleValue()));
    				}
        			return generadorReporte.buildReportFormatoC(forma.getConsolidadoContrato().getNivelesAtencion(), params);
    			}
    			else{
    				throw new IllegalArgumentException("El DtoContratoReporte no puede ser nulo");
    			}
    		}
    		else if(idFormato==IConstantesReporte.formatoPlanoC){
    			if(forma.getConsolidadoContrato() != null){
    				if(forma.getConsolidadoContrato().getNivelesAtencion().isEmpty()){
    					return null;
    				}
    				return generadorReporte.buildReportPlanoFormatoC(forma.getConsolidadoContrato().getNivelesAtencion(), params);
    			}
    			else{
    				throw new IllegalArgumentException("El DtoContratoReporte no puede ser nulo");
    			}
    		}
    		else{
    			throw new IllegalArgumentException("El id del formato es invalido");
    		}
    	}
		catch(Exception e){
			logger.error(e);
			errores.add("Error Generar Reporte",new ActionMessage("errors.notEspecific","Error Generando el Reporte"));
			saveErrors(request, errores);
		}
		return null;
    }
}
