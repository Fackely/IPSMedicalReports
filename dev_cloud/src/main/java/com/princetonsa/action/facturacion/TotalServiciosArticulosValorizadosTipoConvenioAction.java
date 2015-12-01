package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.capitacion.ConstantesCapitacion;
import util.facturacion.ConstantesBDFacturacion;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.actionform.facturacion.TotalServiciosArticulosValorizadosTipoConvenioForm;
import com.princetonsa.dto.capitacion.DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.dto.capitacion.DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.capitacion.DtoDiaCierre;
import com.servinte.axioma.dto.capitacion.DtoTipoConsulta;
import com.servinte.axioma.generadorReporte.capitacion.GeneradorReporteTotalServiciosArticulosValorizadosPorConvenio.GeneradorReporteTotalServArtPorConvenioDinamico;
import com.servinte.axioma.generadorReporte.comun.IConstantesReporte;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.impl.administracion.InstitucionesMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IInstitucionesMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IValorizacionServiciosArticulosMundo;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 *   Action, controla todas las opciones dentro de la consulta
 *   del total de servicios/articulos valorizados por tipo y convenio
 *   incluyendo los posibles casos de error y los casos de flujo.
 * @version 1.0, Abr 25, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 */
public class TotalServiciosArticulosValorizadosTipoConvenioAction extends Action {

	/**
     * Objeto para manejar los logs de esta clase
    */
    public Logger logger = Logger.getLogger(TotalServiciosArticulosValorizadosTipoConvenioAction.class);
    
    /** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.facturacion.TotalServiciosArticulosValorizadosTipoConvenioForm");
    
    /**
	 * Método excute del Action
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            						HttpServletRequest request, HttpServletResponse response)
    								throws Exception{
    	ActionErrors errores=new ActionErrors();
		TotalServiciosArticulosValorizadosTipoConvenioForm forma=(TotalServiciosArticulosValorizadosTipoConvenioForm)form;
    	try{
		if(form instanceof TotalServiciosArticulosValorizadosTipoConvenioForm){
    		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			String estado=forma.getEstado();
			if(estado.equals("empezar")){
				return mapping.findForward(llenarForma(forma, errores, request,usuario));
			}
			else if(estado.equals("listarConvenios")){
				return mapping.findForward(llenarListadoConvenios(forma, errores, usuario));
			}
			else if(estado.equals("listarContratos")){
				return mapping.findForward(llenarListadoContratos(forma, errores));
			}
			else if(estado.equals("buscarTotales")){
				//Se valida que el mes seleccionado sea mayor o igual al mes del
				//parametro general  'Fecha Inicio de Cierre de Ordenes Medicas'
				String fechaInicioCierreOrdenMedicaStr = ValoresPorDefecto.getFechaInicioCierreOrdenMedica(usuario.getCodigoInstitucionInt());
				Date fechaInicioCierre;
				if(fechaInicioCierreOrdenMedicaStr != null){
		    		fechaInicioCierre=UtilidadFecha.conversionFormatoFechaStringDate(fechaInicioCierreOrdenMedicaStr);
		    		Calendar mesAnioSeleccionado=Calendar.getInstance();
		    		Calendar mesAnioCierre=Calendar.getInstance();
		    		mesAnioSeleccionado.set(Calendar.YEAR, forma.getIdAnio());
		    		mesAnioSeleccionado.set(Calendar.MONTH, forma.getIdMesInicio());
		    		mesAnioCierre.setTime(fechaInicioCierre);
		    		if(mesAnioSeleccionado.get(Calendar.YEAR) < mesAnioCierre.get(Calendar.YEAR)
		    			 || (mesAnioSeleccionado.get(Calendar.YEAR)==mesAnioCierre.get(Calendar.YEAR)
		    					&& mesAnioSeleccionado.get(Calendar.MONTH)<mesAnioCierre.get(Calendar.MONTH))){
		    			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		    			errores.add("Error Mes Anio Inferior a Fecha Cierre",
								new ActionMessage("errors.notEspecific", messageResource.getMessage(
										"totalServiciosArticulosValorizadosTipoConvenio.mesAnioInvalido", new Object[]{format.format(fechaInicioCierre)})));
						saveErrors(request, errores);
		    			forma.setTipoSalida(null);
						forma.setEnumTipoSalida(null);
						return mapping.findForward("principal");
		    		}
		    	}
				else{
					errores.add("No Parametro",
							new ActionMessage("errors.notEspecific", messageResource.getMessage(
									"totalServiciosArticulosValorizadosTipoConvenio.noParametro")));
					saveErrors(request, errores);
	    			forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					return mapping.findForward("principal");
				}
				
				
				int idTipoSalida=Integer.parseInt(forma.getTipoSalida());
				if (idTipoSalida != ConstantesBD.codigoNuncaValido) {
					boolean validacion=consultarTotales(forma, request, errores, usuario, idTipoSalida, fechaInicioCierre);
					if(!validacion){
						forma.setTipoSalida(null);
						forma.setEnumTipoSalida(null);
						return mapping.findForward("principal");
					}
					else{
						JasperReportBuilder reportBuild=null;
						String nombreReporte="";
						if(idTipoSalida == EnumTiposSalida.PLANO.getCodigo()){
							reportBuild=generarReporte(forma, request, errores, usuario);
								nombreReporte=ConstantesCapitacion.reporteValorizados;
							if(reportBuild != null){
								GeneradorReporteDinamico generadorReporte = new GeneradorReporteDinamico();
								String nombreArchivo=generadorReporte.exportarReporteArchivoPlano(reportBuild, nombreReporte);
								forma.setNombreReporte(nombreArchivo);
							}
							else{
								errores.add("Error Reporte no tiene Datos",
										new ActionMessage("errors.notEspecific", messageResource.getMessage(
												"totalServiciosArticulosValorizadosTipoConvenio.busquedaNoDatos")));
								saveErrors(request, errores);
								forma.setTipoSalida(null);
								forma.setEnumTipoSalida(null);
								return mapping.findForward("principal");
							}
						}
						else{
							reportBuild=generarReporte(forma, request, errores, usuario);
							nombreReporte=ConstantesCapitacion.reporteValorizados;
						
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
												"totalServiciosArticulosValorizadosTipoConvenio.busquedaNoDatos")));
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
			else if(estado.equals("asignarPropiedad")){
			
				/*  * Se asigna una propiedad a la forma sin cambiar nada en la presentación  */
				return null;
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

    
    public String llenarForma(TotalServiciosArticulosValorizadosTipoConvenioForm forma, ActionErrors errores, HttpServletRequest request,UsuarioBasico usuario){
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
		    	forma.setIdMesFin(anioActual.get(Calendar.MONTH));
		    	forma.setListadoAnios(UtilidadFecha.obtenerListaDtoAnios(anio2010.getTime(), new Date()));
		    	forma.setListadoMesesInicio(UtilidadFecha.obtenerListaDtoMes());
		    	forma.setListadoMesesFin(UtilidadFecha.obtenerListaDtoMes());
		    	forma.setListadoTipoConsulta(obtenerTiposConsulta());
	    	}
	    	else{
	    		request.setAttribute("codigoDescripcionError", "errors.noExisteCierre");
	    		return "paginaError";
	    	}
    	}
    	catch(Exception e){
    		logger.error(e);
    		errores.add("Error Llenado Forma",new ActionMessage("errors.notEspecific",e.getMessage()));
    		saveErrors(request, errores);
    	}
    	finally{
    		UtilidadTransaccion.getTransaccion().commit();
    	}
    	return "principal";
    }
    
    public List<DtoTipoConsulta> obtenerTiposConsulta(){
    	List<DtoTipoConsulta> tiposConsulta = new ArrayList<DtoTipoConsulta>();
    	DtoTipoConsulta tipoConsulta = new DtoTipoConsulta();
    	tipoConsulta.setCodigo(ConstantesBDFacturacion.codigoTipoConsultaOrdenado);
    	tipoConsulta.setDescripcion(messageResource.getMessage(
    							"totalServiciosArticulosValorizadosTipoConvenio.tipoConsulta"+
    							ConstantesBDFacturacion.codigoTipoConsultaOrdenado));
    	tiposConsulta.add(tipoConsulta);
    	tipoConsulta= new DtoTipoConsulta();
    	tipoConsulta.setCodigo(ConstantesBDFacturacion.codigoTipoConsultaAutorizado);
    	tipoConsulta.setDescripcion(messageResource.getMessage(
    							"totalServiciosArticulosValorizadosTipoConvenio.tipoConsulta"+
    							ConstantesBDFacturacion.codigoTipoConsultaAutorizado));
    	tiposConsulta.add(tipoConsulta);
    	tipoConsulta= new DtoTipoConsulta();
    	tipoConsulta.setCodigo(ConstantesBDFacturacion.codigoTipoConsultaCargado);
    	tipoConsulta.setDescripcion(messageResource.getMessage(
    							"totalServiciosArticulosValorizadosTipoConvenio.tipoConsulta"+
    							ConstantesBDFacturacion.codigoTipoConsultaCargado));
    	tiposConsulta.add(tipoConsulta);
    	tipoConsulta= new DtoTipoConsulta();
    	tipoConsulta.setCodigo(ConstantesBDFacturacion.codigoTipoConsultaFacturado);
    	tipoConsulta.setDescripcion(messageResource.getMessage(
    							"totalServiciosArticulosValorizadosTipoConvenio.tipoConsulta"+
    							ConstantesBDFacturacion.codigoTipoConsultaFacturado));
    	tiposConsulta.add(tipoConsulta);
    	return tiposConsulta;
    }
    
    public String llenarListadoConvenios(TotalServiciosArticulosValorizadosTipoConvenioForm forma, ActionErrors errores, UsuarioBasico usuarioBasico){
    	ArrayList<Convenios> convenios = new ArrayList<Convenios>();
    	IConveniosMundo convenioMundo = FacturacionFabricaMundo.crearcConveniosMundo();
    	try{
    		UtilidadTransaccion.getTransaccion().begin();
			if(forma.getIdTipoConsulta() != ConstantesBD.codigoNuncaValido){
				if(forma.getIdTipoConsulta() == ConstantesBDFacturacion.codigoTipoConsultaAutorizado){
					forma.setListadoConvenios(convenioMundo.listarConveniosCapitadosPorInstitucion(
							usuarioBasico.getCodigoInstitucionInt()));
				}
				else{
					forma.setListadoConvenios(convenioMundo.listarConveniosPorInstitucion(
													usuarioBasico.getCodigoInstitucionInt()));
				}
			}
			else{
				forma.setListadoConvenios(convenios);
			}
			forma.setListadoContratos(new ArrayList<Contratos>());
			forma.setConvenioHelper(ConstantesBD.codigoNuncaValido);
			forma.setContratoHelper(ConstantesBD.codigoNuncaValido);
	    }
    	catch(Exception e){
    		logger.error(e);
    		errores.add("Error Obtener Listado Convenios",new ActionMessage("errors.notEspecific",e.getMessage()));
    	}
    	finally{
    		UtilidadTransaccion.getTransaccion().commit();
    	}
    	return "listarConvenios";
    }
    
    
    public String llenarListadoContratos(TotalServiciosArticulosValorizadosTipoConvenioForm forma, ActionErrors errores){
    	IContratoMundo contratoMundo = FacturacionFabricaMundo.crearContratoMundo();
    	ArrayList<Contratos> contratos = new ArrayList<Contratos>();
    	try{
    		UtilidadTransaccion.getTransaccion().begin();
			if(forma.getConvenioHelper() != ConstantesBD.codigoNuncaValido){
				forma.setListadoContratos(contratoMundo.listarContratosPorConvenio(
								forma.getConvenioHelper()));
			}
			else{
				forma.setListadoContratos(contratos);
			}	
	    }
    	catch(Exception e){
    		logger.error(e);
    		errores.add("Error Obtener Listado Contratos",new ActionMessage("errors.notEspecific",e.getMessage()));
    	}
    	finally{
    		UtilidadTransaccion.getTransaccion().commit();
    	}
    	return "listarContratos";
    }
    
    /**Método que se encarga de consultar la información que se presentara en el reporte y realizar
     * las validaciones correspondientes
     */
    private boolean consultarTotales(TotalServiciosArticulosValorizadosTipoConvenioForm forma, HttpServletRequest request, 
    								ActionErrors errores, UsuarioBasico usuario, int idTipoSalida, Date fechaInicioCierre){
    	try{
    		IValorizacionServiciosArticulosMundo valorizacionServiciosArticuloMundo=FacturacionFabricaMundo.crearValorizacionServiciosArticulosMundo();
    		Calendar fechaInicioReporte=Calendar.getInstance();
    		Calendar fechaFinReporte=Calendar.getInstance();
    		//Se setea el año y mes seleccionado por el usuario para validar
    		//los cierres diarios
    		fechaInicioReporte.set(Calendar.YEAR, forma.getIdAnio());
    		fechaInicioReporte.set(Calendar.MONTH, forma.getIdMesInicio());
    		fechaFinReporte.set(Calendar.YEAR, forma.getIdAnio());
    		fechaFinReporte.set(Calendar.MONTH, forma.getIdMesFin());
    		List<DtoDiaCierre> cierresDiarios = obtenerDiasCierre(fechaInicioReporte, fechaFinReporte, fechaInicioCierre); 
    		cierresDiarios=valorizacionServiciosArticuloMundo.validarCierresDiarios(cierresDiarios);
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
    		if(validate){
    			saveErrors(request, errores);
    			return false;
    		}
    		boolean isContrato=false;
    		boolean isSalidaArchivoPlano=false;
    		//Se valida si esta o no selecciondo el contrato
			if(forma.getContratoHelper() != ConstantesBD.codigoNuncaValido){
				isContrato=true;
			}
			if(idTipoSalida==EnumTiposSalida.PLANO.getCodigo()){
				isSalidaArchivoPlano=true;
			}
			//Se valida como seleccionó el usuario que va detallar el reporte
    		//en la sección servicios
			ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consolidadoServicios = null;
			int formatoServicios=ConstantesBD.codigoNuncaValido;
			
			if(forma.isServicioNivel()){
				if(isContrato){
					consolidadoServicios=valorizacionServiciosArticuloMundo
						.consultarConsolidadoServiciosPorNivelPorContrato(
								forma.getContrato(), 
								this.obtenerProcesoPorIdTipoConsulta(forma.getIdTipoConsulta()), 
								this.obtenerMesesReporte(fechaInicioReporte, fechaFinReporte));
				}
				else{
					consolidadoServicios=valorizacionServiciosArticuloMundo
						.consultarConsolidadoServiciosPorNivelPorConvenio(
								forma.getConvenio(), 
								this.obtenerProcesoPorIdTipoConsulta(forma.getIdTipoConsulta()), 
								this.obtenerMesesReporte(fechaInicioReporte, fechaFinReporte));
				}
				if(consolidadoServicios != null){
					if(isSalidaArchivoPlano){
						formatoServicios=IConstantesReporte.formatoPlanoA;
					}
					else{
						formatoServicios=IConstantesReporte.formatoA;
					}
				}
			}
			else if(forma.isServicioGrupo()){
				if(isContrato){
					consolidadoServicios=valorizacionServiciosArticuloMundo
						.consultarConsolidadoServiciosPorGrupoPorContrato(
								forma.getContrato(), 
								this.obtenerProcesoPorIdTipoConsulta(forma.getIdTipoConsulta()), 
								this.obtenerMesesReporte(fechaInicioReporte, fechaFinReporte));
				}
				else{
					consolidadoServicios=valorizacionServiciosArticuloMundo
						.consultarConsolidadoServiciosPorGrupoPorConvenio(
								forma.getConvenio(), 
								this.obtenerProcesoPorIdTipoConsulta(forma.getIdTipoConsulta()), 
								this.obtenerMesesReporte(fechaInicioReporte, fechaFinReporte));
				}
				if(consolidadoServicios != null){
					if(isSalidaArchivoPlano){
						formatoServicios=IConstantesReporte.formatoPlanoB;
					}
					else{
						formatoServicios=IConstantesReporte.formatoB;
					}
				}
    		}
    		else if(forma.isServicioServicio()){
    			if(isContrato){
    				consolidadoServicios=valorizacionServiciosArticuloMundo
						.consultarConsolidadoServiciosPorServicioPorContrato(
								forma.getContrato(), 
								this.obtenerProcesoPorIdTipoConsulta(forma.getIdTipoConsulta()), 
								this.obtenerMesesReporte(fechaInicioReporte, fechaFinReporte));
				}
				else{
					consolidadoServicios=valorizacionServiciosArticuloMundo
						.consultarConsolidadoServiciosPorServicioPorConvenio(
								forma.getConvenio(), 
								this.obtenerProcesoPorIdTipoConsulta(forma.getIdTipoConsulta()), 
								this.obtenerMesesReporte(fechaInicioReporte, fechaFinReporte));
				}
    			if(consolidadoServicios != null){
	    			if(isSalidaArchivoPlano){
						formatoServicios=IConstantesReporte.formatoPlanoC;
					}
					else{
						formatoServicios=IConstantesReporte.formatoC;
					}
    			}
    		}
			//Se valida como seleccionó el usuario que va detallar el reporte
    		//en la sección Medicamentos
			ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consolidadoArticulos = null;
			int formatoArticulos=ConstantesBD.codigoNuncaValido;
    		if(forma.isMedicamentoNivel()){
    			if(isContrato){
    				consolidadoArticulos=valorizacionServiciosArticuloMundo
						.consultarConsolidadoArticulosPorNivelPorContrato(
								forma.getContrato(), 
								this.obtenerProcesoPorIdTipoConsulta(forma.getIdTipoConsulta()), 
								this.obtenerMesesReporte(fechaInicioReporte, fechaFinReporte));
				}
				else{
					consolidadoArticulos=valorizacionServiciosArticuloMundo
						.consultarConsolidadoArticulosPorNivelPorConvenio(
								forma.getConvenio(), 
								this.obtenerProcesoPorIdTipoConsulta(forma.getIdTipoConsulta()), 
								this.obtenerMesesReporte(fechaInicioReporte, fechaFinReporte));
				}
    			if(consolidadoArticulos != null){
	    			if(isSalidaArchivoPlano){
						formatoArticulos=IConstantesReporte.formatoPlanoA;
					}
					else{
						formatoArticulos=IConstantesReporte.formatoA;
					}
    			}
    		}
    		else if(forma.isMedicamentoClase()){
    			if(isContrato){
    				consolidadoArticulos=valorizacionServiciosArticuloMundo
						.consultarConsolidadoArticulosPorClasePorContrato(
								forma.getContrato(), 
								this.obtenerProcesoPorIdTipoConsulta(forma.getIdTipoConsulta()), 
								this.obtenerMesesReporte(fechaInicioReporte, fechaFinReporte));
				}
				else{
					consolidadoArticulos=valorizacionServiciosArticuloMundo
						.consultarConsolidadoArticulosPorClasePorConvenio(
								forma.getConvenio(), 
								this.obtenerProcesoPorIdTipoConsulta(forma.getIdTipoConsulta()), 
								this.obtenerMesesReporte(fechaInicioReporte, fechaFinReporte));
				}
    			if(consolidadoArticulos != null){
	    			if(isSalidaArchivoPlano){
						formatoArticulos=IConstantesReporte.formatoPlanoB;
					}
					else{
						formatoArticulos=IConstantesReporte.formatoB;
					}
    			}
    		}
    		else if(forma.isMedicamentoInsumo()){
    			if(isContrato){
    				consolidadoArticulos=valorizacionServiciosArticuloMundo
						.consultarConsolidadoArticulosPorArticuloPorContrato(
								forma.getContrato(), 
								this.obtenerProcesoPorIdTipoConsulta(forma.getIdTipoConsulta()), 
								this.obtenerMesesReporte(fechaInicioReporte, fechaFinReporte));
				}
				else{
					consolidadoArticulos=valorizacionServiciosArticuloMundo
						.consultarConsolidadoArticulosPorArticuloPorConvenio(
								forma.getConvenio(), 
								this.obtenerProcesoPorIdTipoConsulta(forma.getIdTipoConsulta()), 
								this.obtenerMesesReporte(fechaInicioReporte, fechaFinReporte));
				}
    			if(consolidadoArticulos != null){
	    			if(isSalidaArchivoPlano){
						formatoArticulos=IConstantesReporte.formatoPlanoC;
					}
					else{
						formatoArticulos=IConstantesReporte.formatoC;
					}
    			}
    		}
    		//Se valida si el reporte se detalla por servicios o por medicamentos
    		//desde la presentación se garantiza que al menos una sección este seleccionada
    		if(consolidadoServicios == null && consolidadoArticulos == null){
    			String tipo=messageResource.getMessage("totalServiciosArticulosValorizadosTipoConvenio.tipoConsulta"+forma.getIdTipoConsulta());
    			if(isContrato){
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					String fechas=format.format(forma.getContrato().getFechaInicial())+" - "+format.format(forma.getContrato().getFechaFinal());
					String numero=forma.getContrato().getNumeroContrato();
					errores.add("Error Contratos Sin Niveles",
							new ActionMessage("errors.contratoSinNiveles", new String[]{tipo,numero, fechas}));
					saveErrors(request, errores);
	    			return false;
				}
				else{
					String nombre=forma.getConvenio().getNombre();
					errores.add("Error Contratos del Convenio Sin Niveles",
							new ActionMessage("errors.convenioSinNiveles", new String[]{tipo,nombre}));
					saveErrors(request, errores);
	    			return false;
				}
    		}
    		forma.setConsolidadoServicios(consolidadoServicios);
    		forma.setIdFormatoServicios(formatoServicios);
    		forma.setConsolidadoArticulos(consolidadoArticulos);
    		forma.setIdFormatoArticulos(formatoArticulos);
	    }
    	catch(Exception e){
    		logger.error(e);
    		errores.add("Error Obtener Listado Contratos",new ActionMessage("errors.notEspecific",e.getMessage()));
    		saveErrors(request, errores);
			return false;
    	}
    	return true;
    }
    
    /**Metodo encargado de validar en que dia existe o no un cierre
     * entre un rango de fechas
     * @param fechaInicioReporte
     * @param fechaFinReporte
     * @return
     */
    private List<DtoDiaCierre> obtenerDiasCierre(Calendar fechaInicioReporte, Calendar fechaFinReporte, Date fechaInicioCierre){
    	List<DtoDiaCierre> cierresPorDia = new ArrayList<DtoDiaCierre>();
    	int primerDiaMes=fechaInicioReporte.getActualMinimum(Calendar.DAY_OF_MONTH);
    	Calendar fechaCierre= Calendar.getInstance();
    	fechaCierre.setTime(fechaInicioCierre);
    	if(fechaInicioReporte.get(Calendar.YEAR) == fechaCierre.get(Calendar.YEAR)
    			&& fechaInicioReporte.get(Calendar.MONTH) == fechaCierre.get(Calendar.MONTH)){
    		primerDiaMes=fechaCierre.get(Calendar.DAY_OF_MONTH);
    	}
    	
    	int ultimoDiaMes=fechaFinReporte.getActualMaximum(Calendar.DAY_OF_MONTH);
    	Calendar fechaActual = Calendar.getInstance();
    	if(fechaFinReporte.get(Calendar.YEAR) == fechaActual.get(Calendar.YEAR)
    			&& fechaFinReporte.get(Calendar.MONTH) == fechaActual.get(Calendar.MONTH)){
    		//Se debe validar que exista cierre hasta un dia antes del dia actual
    		//porque no va a existir cierre el mismo dia
    		ultimoDiaMes=fechaActual.get(Calendar.DAY_OF_MONTH)-1;
    	}
    	fechaInicioReporte.set(Calendar.DAY_OF_MONTH, primerDiaMes);
    	fechaFinReporte.set(Calendar.DAY_OF_MONTH, ultimoDiaMes);
    	fechaInicioReporte=UtilidadFecha.asignarHoraMinutoSegundoMiliSegundo(fechaInicioReporte, 0, 0, 0, 0);
    	fechaFinReporte=UtilidadFecha.asignarHoraMinutoSegundoMiliSegundo(fechaFinReporte, 23, 59, 59, 999);
    	Calendar fechaValidar=(Calendar)fechaInicioReporte.clone();
    	//Obtenemos la diferencia en milisegundos entre las dos fechas
    	double miliseg=fechaFinReporte.getTimeInMillis()-fechaInicioReporte.getTimeInMillis();
    	double miliDia=86400000;
    	//Obtenemos el numero de dias dividiendo los milisegundos entre
    	//las fechas en el número de milisegundo de un dia
    	double dias=Math.ceil(miliseg/miliDia);
    	for(int i=1 ;i<=dias;i++){
    		if(i!=1){
    			fechaValidar.add(Calendar.DATE, 1);
    		}
    		Date fechaDia = fechaValidar.getTime();
    		cierresPorDia.add(new DtoDiaCierre(fechaDia, false));
    	}
    	return cierresPorDia;
    }
    
    
    /**
     * Carga en persona básica todos los datos del usuario
     * @param usuario
     * @return PersonaBasica
     *
     * @autor Cristhian Murillo
     */
    private PersonaBasica cargarPersonaBasica(UsuarioBasico usuario)
    {
    	PersonaBasica personaBasica = new PersonaBasica();
    	
    	Connection con = null;
    	con = UtilidadBD.abrirConexion();
    	
    	try {
    		personaBasica.cargar(con, usuario.getCodigoPersona());
    		personaBasica.cargarPaciente(con, personaBasica.getCodigoPersona(),usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		UtilidadBD.closeConnection(con);
		
		return personaBasica;
    }
    
    
    /**Metodo encargado de invocar el generador del reporte
     *    
     * @param forma
     * @param request
     * @param errores
     * @param usuario
     * @return JasperReportBuilder
     */
    private JasperReportBuilder generarReporte(TotalServiciosArticulosValorizadosTipoConvenioForm forma, 
    		HttpServletRequest request,ActionErrors errores, UsuarioBasico usuario){
    	try{
    		
    		DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio dtoDatosReporte = new DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio();
    		IInstitucionesMundo institucionesMundo = new InstitucionesMundo();
    		Instituciones institucion = institucionesMundo.buscarPorCodigo(usuario.getCodigoInstitucionInt());
    		dtoDatosReporte.setNombreHospital(institucion.getRazonSocial());
    		dtoDatosReporte.setNitHospital(institucion.getNit());
    		dtoDatosReporte.setRutaLogo(institucion.getLogo());
    		dtoDatosReporte.setUbicacionLogo(institucion.getUbicacionLogoReportes());
    		
    		//dtoDatosReporte.setUsuarioProceso(usuario.getNombreUsuario()+" ("+usuario.getLoginUsuario()+")");
    		PersonaBasica personaBasica = cargarPersonaBasica(usuario);
    		dtoDatosReporte.setUsuarioProceso(personaBasica.getPrimerNombre()+" "+personaBasica.getPrimerApellido()+" ("+usuario.getLoginUsuario()+")");
    		
    		dtoDatosReporte.setFecha(obtenerFechaFormatoReporte(forma.getIdAnio(), forma.getIdMesInicio(), forma.getIdMesFin()));
    		dtoDatosReporte.setCantidadMesesMostrar(forma.getIdMesFin() - forma.getIdMesInicio() +1);
    		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
    		dtoDatosReporte.setFechaProceso(sdf.format(UtilidadFecha.getFechaActualTipoBD())+" "+UtilidadFecha.getHoraActual());
    		dtoDatosReporte.setNombreConvenio(forma.getConvenio().getNombre());
    		dtoDatosReporte.setNombreContrato(null);
    		if(forma.getContratoHelper() != ConstantesBD.codigoNuncaValido){
    			dtoDatosReporte.setNombreContrato(forma.getContrato().getNumeroContrato());
    		}
    		dtoDatosReporte.setIdFormatoArticulos(forma.getIdFormatoArticulos());
    		dtoDatosReporte.setIdFormatoServicios(forma.getIdFormatoServicios());
    		dtoDatosReporte.setListaNivelesAtencionServicios(forma.getConsolidadoServicios());
    		dtoDatosReporte.setListaNivelesAtencionArticulos(forma.getConsolidadoArticulos());
    		GeneradorReporteTotalServArtPorConvenioDinamico generadorReporte = new GeneradorReporteTotalServArtPorConvenioDinamico(dtoDatosReporte);
    		return generadorReporte.getReportBuilder();
    	}
		catch(Exception e){
			logger.error(e);
			errores.add("Error Generar Reporte",new ActionMessage("errors.notEspecific","Error Generando el Reporte"));
			saveErrors(request, errores);
		}
		return null;
    }
    
    /**Metodo encargado de obtener la instancia de todos los meses (calendar)
     * entre un rango de fechas
     * @param fechaInicioReporte
     * @param fechaFinReporte
     * @return List<Calendar>
     */
    private List<Calendar> obtenerMesesReporte(Calendar fechaInicioReporte, Calendar fechaFinReporte){
    	List<Calendar> mesesReporte = new ArrayList<Calendar>();
    	if(fechaInicioReporte.get(Calendar.MONTH) == fechaFinReporte.get(Calendar.MONTH)){
    		mesesReporte.add(fechaInicioReporte);
    	}
    	else{
    		int k=fechaInicioReporte.get(Calendar.MONTH);
    		int j=fechaFinReporte.get(Calendar.MONTH);
    		for(int i=k;i<=j;i++){
    			Calendar mes= Calendar.getInstance();
    			mes.set(Calendar.YEAR, fechaInicioReporte.get(Calendar.YEAR));
    			mes.set(Calendar.MONTH, i);
    			mesesReporte.add(mes);
    		}
    	}
    	return mesesReporte;
    }
    
    /**Metodo encargado de obtener el id del proceso para el cual se requiere que se 
     * genere el reporte
     * @param idTipoConsulta
     * @return proceso
     */
    private String obtenerProcesoPorIdTipoConsulta(int idTipoConsulta){
    	String proceso;
    	if(idTipoConsulta==ConstantesBDFacturacion.codigoTipoConsultaOrdenado){
    		proceso=ConstantesIntegridadDominio.acronimoTipoProcesoOrden;
    	}
    	else if(idTipoConsulta==ConstantesBDFacturacion.codigoTipoConsultaAutorizado){
    		proceso=ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion;
    	}
		else if(idTipoConsulta==ConstantesBDFacturacion.codigoTipoConsultaCargado){
			proceso=ConstantesIntegridadDominio.acronimoTipoProcesoCargoCuenta;		
		}
		else if(idTipoConsulta==ConstantesBDFacturacion.codigoTipoConsultaFacturado){
			proceso=ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion;
		}
    	else{
    		proceso=null;
    	}
    	return proceso;
    }
    
    /**Metodo encargado de obtener la fecha seleccionada por el usuario en el formato requerido para el reporte
     * @param idAnio
     * @param idMesInicio
     * @param idMesFin
     * @return fecha
     */
    private String obtenerFechaFormatoReporte(int idAnio, int idMesInicio, int idMesFin){
    	String fecha;
    	String mesInicio=UtilidadFecha.obtenerNombreMesProperties(idMesInicio);
    	String mesFin=UtilidadFecha.obtenerNombreMesProperties(idMesFin);
    	fecha=String.valueOf(idAnio)+" "+mesInicio+" - "+mesFin;
    	return fecha;
    }
    
    
  
}
