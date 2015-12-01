package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.ConsultarImprimirAutorizacionesEntSubcontratadasForm;
import com.princetonsa.dto.facturacion.DTOMontosCobroDetalleGeneral;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionPaciente;
import com.princetonsa.dto.manejoPaciente.DTOReporteEstandarAutorizacionServiciosArticulos;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacionEntSubContratada;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteArticulosAutorizados;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteServiciosAutorizados;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.manejoPaciente.ConsultarImprimirAutorizacionesEntSubcontratadas;
import com.princetonsa.pdf.ListaConsultaAutorizacionEntSubPdf;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatoEstandar.formatoAutorizacionMedicamentosInsumos.GeneradorReporteFormatoEstandarAutorArticulos;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatoEstandar.formatoAutorizacionServicios.GeneradorReporteFormatoEstandarAutorservicio;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionMedicamentosInsumos.GeneradorReporteFormatoCapitacionAutorArticulos;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionServicios.GeneradorReporteFormatoCapitacionAutorservicio;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IDetalleMontoGeneralServicio;

public class ConsultarImprimirAutorizacionesEntSubcontratadasAction extends Action  {

	Logger logger = Logger.getLogger(ConsultarImprimirAutorizacionesEntSubcontratadasAction.class);
	ConsultarImprimirAutorizacionesEntSubcontratadas mundo;
	
	public ActionForward execute(ActionMapping mapping,
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception
			 {
		Connection con = null;
		try{
			if(response == null);

			if (form instanceof ConsultarImprimirAutorizacionesEntSubcontratadasForm) 
			{			 

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				//Institucion
				InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				//ActionErrors
				ActionErrors errores = new ActionErrors();
				mundo = new ConsultarImprimirAutorizacionesEntSubcontratadas();

				ConsultarImprimirAutorizacionesEntSubcontratadasForm forma = (ConsultarImprimirAutorizacionesEntSubcontratadasForm)form;		
				String estado = forma.getEstado();		 

				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());			 
				logger.info("-------------------------------------");
				logger.info("-------------------------------------");

				if(estado.equals("empezar"))
				{ 
					forma.reset();	  
					UtilidadBD.closeConnection(con); 
					return mapping.findForward("principal");
				}else
					if(estado.equals("consultaPaciente"))
					{  
						forma.reset();
						ActionForward forward = new ActionForward();
						forward=accionValidarPaciente(con, forma, paciente, usuario, request, mapping);
						if(forward != null)
							return forward;	
						UtilidadBD.closeConnection(con);
						forma.setOpcionListadoAutorizacion("paciente");
						return listadoAutorizaciones(forma, paciente, usuario, request, mapping);
					}else

						if (estado.equals("consultaPeriodo"))
						{ 
							forma.reset();	  
							UtilidadBD.closeConnection(con);
							forma.setOpcionListadoAutorizacion("periodo");
							return parametrosBusqueda(forma,usuario, mapping, request);

						}else
							if (estado.equals("buscarAutorizaciones"))
							{   	
								UtilidadBD.closeConnection(con); 
								return accionBusquedaAutorizacionesRango(forma, usuario,paciente,institucionBasica, mapping, request);											   
							}
							else
								if (estado.equals("ordenar"))
								{    
									UtilidadBD.closeConnection(con); 
									return ordenarXColumna(forma, mapping);
								}else
									if (estado.equals("verDetalle"))
									{    
										UtilidadBD.closeConnection(con);
										String forward=generarReportes(forma, usuario,
												paciente, errores, request);
										return mapping.findForward(forward);
									}
									else
										if (estado.equals("imprimirAutorizacion"))
										{  
											UtilidadBD.closeConnection(con);  
											return accionImprimirAutorizacion(forma,usuario, mapping, request,paciente);
										}
										else
											if (estado.equals("imprimirListaAutorizacion"))
											{  
												UtilidadBD.closeConnection(con);  
												return accionImprimirListadoAutorizacion(forma,usuario, mapping, request);
											}
											else if (estado.equals("redireccion"))
											{
												UtilidadBD.closeConnection(con);
												response.sendRedirect(forma.getLinkSiguiente());
												return null;
											}
											else if (estado.equals("refrescarAutorizacion"))
											{
												UtilidadBD.closeConnection(con); 
												return mapping.findForward("detalleAutorizacion");
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
	 * Método encargado de validar el parámetro formato impresión autorización entidad subcontratada
	 * y llamar al método para generar el reporte dependiendo del párametro
	 * @param con Conexion a la BD
	 * @param forma 
	 * @param usuario
	 * @param paciente
	 * @param errores
	 * @param request
	 * @return
	 * @author Fabián Becerra
	 * @throws SQLException 
	 */

	private String generarReportes(ConsultarImprimirAutorizacionesEntSubcontratadasForm forma, UsuarioBasico usuario,
						PersonaBasica paciente, ActionErrors errores, HttpServletRequest request) throws SQLException {
		
		forma.setEstadoAutorizaciondetalle(((DtoAutorizacionEntSubContratada)forma.getAutorizacionesEntSubContratadas().get(forma.getPosAutorizacion())).getEstado());
		DtoAutorizacionEntSubContratada autorizacionEntSub= (DtoAutorizacionEntSubContratada)forma.getAutorizacionesEntSubContratadas().get(forma.getPosAutorizacion());
		
		MessageResources fuenteMensaje = MessageResources.getMessageResources("mensajes.ApplicationResources");
		String tipoFormatoImpresion = ValoresPorDefecto.getFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		
			if(!UtilidadTexto.isEmpty(tipoFormatoImpresion))
			{		
				if(tipoFormatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionEstandar)){
					generarReporteAutorizacionFormatoEstandar(forma, paciente, usuario, request, autorizacionEntSub);
	 			
	 			}else if(tipoFormatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionVersalles)){
	 				generarReporteAutorizacionFormatoVersalles(forma, usuario, paciente, request, autorizacionEntSub);
	 			}
			}
			else{
				errores.add("Formato no Definido",	new ActionMessage("errors.notEspecific", fuenteMensaje.getMessage("errors.peticiones.formatoNoDefinido")));
				saveErrors(request, errores);
			}
			
		return "detalleAutorizacion";
	}	
	
	/**
	 * Método encargado de generar el reporte en formato Versalles de la autorizacion
	 * @param con Conexion a la BD
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @author Fabián Becerra
	 * @throws SQLException 
	 */
	private void generarReporteAutorizacionFormatoVersalles(ConsultarImprimirAutorizacionesEntSubcontratadasForm forma, 
				UsuarioBasico usuario,PersonaBasica paciente, HttpServletRequest request, DtoAutorizacionEntSubContratada autorizacionEntSub) throws SQLException {
    	
		MessageResources mensajes=MessageResources.getMessageResources(
 		"com.servinte.mensajes.manejoPaciente.AutorizacionesEntidadSubcontratadaForm");
		
		String nombreReporte="AUTORIZACION ORDENES MEDICAS";
		String nombreArchivo ="";
		DtoGeneralReporteServiciosAutorizados dtoReporteServicios = new DtoGeneralReporteServiciosAutorizados();
		DtoGeneralReporteArticulosAutorizados dtoReporteArticulos = new DtoGeneralReporteArticulosAutorizados();
		DTOReporteAutorizacionSeccionPaciente dtoPaciente = new DTOReporteAutorizacionSeccionPaciente();
		DTOReporteAutorizacionSeccionAutorizacion dtoAutorizacionReporte = new DTOReporteAutorizacionSeccionAutorizacion();
		DtoServiciosAutorizaciones dtoServiciosAutorizaciones					= new DtoServiciosAutorizaciones();	
		ArrayList<DtoServiciosAutorizaciones> listaServiciosAutorizados 		= new ArrayList<DtoServiciosAutorizaciones>();
		ArrayList<DtoArticulosAutorizaciones> listaArticulosAutorizados			= new ArrayList<DtoArticulosAutorizaciones>();
		DtoArticulosAutorizaciones dtoArticulosAutorizaciones					= null;
		ArrayList<String> listaNombresReportes 									= new ArrayList<String>();
		
	    InstitucionBasica institucion = (
        		InstitucionBasica)request.getSession().getAttribute("institucionBasica");
	    
		String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(
				usuario.getCodigoInstitucionInt());
		
		String infoEncabezado = ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(
				usuario.getCodigoInstitucionInt());
		
		String infoPiePagina=ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(
				usuario.getCodigoInstitucionInt());
		
		if(UtilidadTexto.isEmpty(reporteMediaCarta)){
			reporteMediaCarta=ConstantesBD.acronimoNo;
		}	
		
		dtoPaciente.setNombrePaciente(autorizacionEntSub.getNomPaciente());
		dtoPaciente.setTipoDocPaciente(autorizacionEntSub.getTipoIdPacinte());
		dtoPaciente.setNumeroDocPaciente(autorizacionEntSub.getNumIdPaciente());
		dtoPaciente.setTipoAfiliado(autorizacionEntSub.getTipoAfiliado());
		dtoPaciente.setEdadPaciente(autorizacionEntSub.getEdadPaciente());

		/*if(!forma.getDtoAutorizacionCapitacion().getAutorCapitacion().getOtroConvenioRecobro().isEmpty())
 		{
 			dtoPaciente.setRecobro(ConstantesBD.acronimoSi);
 			dtoPaciente.setEntidadRecobro(forma.getDtoAutorizacionCapitacion().getAutorCapitacion().getOtroConvenioRecobro());
 		}else{*/	
 			dtoPaciente.setRecobro(ConstantesBD.acronimoNo);
 			dtoPaciente.setEntidadRecobro("");
 		//}
 			
//		dtoPaciente.setSemanasCotizacion((autorizacionEntSub.getSemanasCotizacion()!=null?autorizacionEntSub.getSemanasCotizacion()+"":""));
 			
		dtoAutorizacionReporte.setEntidadAutoriza(usuario.getInstitucion());
		dtoAutorizacionReporte.setUsuarioAutoriza(usuario.getLoginUsuario());
		
		dtoPaciente.setTipoContratoPaciente(autorizacionEntSub.getNomTipoContrato());
		dtoPaciente.setCategoriaSocioEconomica(autorizacionEntSub.getClasificacionSocioeconomica());
		dtoPaciente.setConvenioPaciente(autorizacionEntSub.getNomConvenio());
		
		dtoAutorizacionReporte.setEntidadSub(autorizacionEntSub.getNomEntidadAutorizada());
		dtoAutorizacionReporte.setDireccionEntidadSub(autorizacionEntSub.getDirEntidadAutorizada());
		dtoAutorizacionReporte.setTelefonoEntidadSub(autorizacionEntSub.getTelEntidadAutorizada());
		dtoAutorizacionReporte.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(autorizacionEntSub.getFechaAutorizacion()));
		dtoAutorizacionReporte.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(autorizacionEntSub.getFechaVencimiento()));
		dtoAutorizacionReporte.setEstadoAutorizacion((String)ValoresPorDefecto.getIntegridadDominio(autorizacionEntSub.getEstado()));
		
		//dtoAutorizacionReporte.setIndicadorPrioridad(UtilidadTexto.isEmpty(dtoAutorizacion.getAutorCapitacion().getIndicadorPrioridad()+"")?"":dtoAutorizacion.getAutorCapitacion().getIndicadorPrioridad()+" días");---------
		
		dtoAutorizacionReporte.setObservaciones(autorizacionEntSub.getObservaciones());		
		dtoAutorizacionReporte.setNumeroAutorizacion(autorizacionEntSub.getConsecutivoAutorizacion());
		
		DtoSubCuentas subcuenta =null;
		if(autorizacionEntSub.getCodServicio()>0){
			Connection con = null;
			try{
				con = UtilidadBD.abrirConexion();
				InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
				infoResponsableCobertura=Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", 
						paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(),autorizacionEntSub.getCodServicio(), 
						usuario.getCodigoInstitucionInt(),false, "");
				
			    Cuenta cuenta= new Cuenta();
		        cuenta.cargarCuenta(con, paciente.getCodigoCuenta()+"");				
				subcuenta =  infoResponsableCobertura.getDtoSubCuenta();
			}catch(Exception e){
				Log4JManager.warning("Error validando la cobertura del servicio para la impresion: "+e);
			}finally{
				UtilidadBD.cerrarConexion(con);
			}
			
			/** Se obtiene información de los servicios autorizados **/
			int codigoPropietario = Utilidades.convertirAEntero(autorizacionEntSub.getCodigoPropietario());
			if(UtilidadTexto.isEmpty(autorizacionEntSub.getCodigoPropietario()))
				dtoServiciosAutorizaciones.setCodigoServicio(autorizacionEntSub.getCodServicio());
			else
				dtoServiciosAutorizaciones.setCodigoServicio(codigoPropietario);
			dtoServiciosAutorizaciones.setDescripcionServicio(autorizacionEntSub.getNomServicio());
			dtoServiciosAutorizaciones.setCantidadSolicitada(Integer.parseInt(autorizacionEntSub.getCantidad()));
			dtoServiciosAutorizaciones.setDescripcionNivelAutorizacion(autorizacionEntSub.getNivel()); 
			dtoServiciosAutorizaciones.setCodigoPropietario(autorizacionEntSub.getCodigoPropietario());
			dtoServiciosAutorizaciones.setConsecutivoOrdenMed(Utilidades.convertirAEntero(autorizacionEntSub.getConseOrdenMedica()));
			dtoServiciosAutorizaciones.setFechaOrden(autorizacionEntSub.getFechaOrden());
			if(!UtilidadTexto.isEmpty(autorizacionEntSub.getAcronimoDiagnostico())&&autorizacionEntSub.getTipoCieDiagnostico()!=null)
				dtoServiciosAutorizaciones.setDiagnostico(autorizacionEntSub.getAcronimoDiagnostico()+" - "+autorizacionEntSub.getTipoCieDiagnostico());
			else
				dtoServiciosAutorizaciones.setDiagnostico("");
			listaServiciosAutorizados.add(dtoServiciosAutorizaciones);
		}
		else{
			
			for (int i=0; i<autorizacionEntSub.getAgrupaListadoEntSub().size(); i++){
				Connection con = null;
				try{
					con = UtilidadBD.abrirConexion();
					InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
					int codArticulo = Utilidades.convertirAEntero(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getCodMedicamento());
					infoResponsableCobertura=Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", 
							paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(),codArticulo , 
							usuario.getCodigoInstitucionInt(),false, "");
					
				    Cuenta cuenta= new Cuenta();
			        cuenta.cargarCuenta(con, paciente.getCodigoCuenta()+"");				
					subcuenta =  infoResponsableCobertura.getDtoSubCuenta();
				}catch(Exception e){
					Log4JManager.warning("Error validando la cobertura del servicio para la impresion: "+e);
				}finally{
					UtilidadBD.cerrarConexion(con);
				}
				
				/** Se obtiene información de los artículos autorizados se verifica si es de naturaleza Medicamento**/
					
					if((autorizacionEntSub.getAgrupaListadoEntSub().get(i).getEs_medicamento().equals(ConstantesBD.codigoNaturalezaArticuloMedicamentoPos))
							|| autorizacionEntSub.getAgrupaListadoEntSub().get(i).getEs_medicamento().equals(ConstantesBD.codigoNaturalezaArticuloMedicamentoNoPos)) {
						
						dtoArticulosAutorizaciones = new DtoArticulosAutorizaciones();
						dtoArticulosAutorizaciones.setCodigoArticulo(Integer.parseInt(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getCodMedicamento()));
						dtoArticulosAutorizaciones.setDescripcionArticulo(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getDescripMedicamento());
						dtoArticulosAutorizaciones.setUnidadMedidaArticulo(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getUnidMedidaMedicamento());
						dtoArticulosAutorizaciones.setDosisFormulacion(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getDosisMedicamento());
						dtoArticulosAutorizaciones.setFrecuenciaFormulacion((UtilidadTexto.isEmpty(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getFrecuenciaMedicamento())?null:Integer.parseInt(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getFrecuenciaMedicamento())));
						dtoArticulosAutorizaciones.setTipoFrecuenciaFormulacion(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getTipoFrecueMedicamento());
						dtoArticulosAutorizaciones.setViaFormulacion(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getViaMedicamento());
						dtoArticulosAutorizaciones.setDiasTratamientoFormulacion((UtilidadTexto.isEmpty(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getDiasTrataMedicamento())?null:Long.parseLong(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getDiasTrataMedicamento())));
						dtoArticulosAutorizaciones.setCantidadSolicitada((UtilidadTexto.isEmpty(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getNroDosisTotalMedicamento())?null:Integer.parseInt(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getNroDosisTotalMedicamento())));
						dtoArticulosAutorizaciones.setEsMedicamento(ConstantesBD.acronimoSiChar);
						
					}
					if(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getEs_medicamento().equals(ConstantesBD.codigoNaturalezaArticuloMaterialesInsumos)){
						dtoArticulosAutorizaciones = new DtoArticulosAutorizaciones();
						dtoArticulosAutorizaciones.setCodigoArticulo(Integer.parseInt(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getCodInsumo()));
						dtoArticulosAutorizaciones.setDescripcionArticulo(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getDescripInsumo());
						dtoArticulosAutorizaciones.setUnidadMedidaArticulo(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getUnidMedidaInsumo());
						dtoArticulosAutorizaciones.setCantidadSolicitada(Integer.parseInt(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getNroDosisTotalInsumo()));
						dtoArticulosAutorizaciones.setEsMedicamento(ConstantesBD.acronimoNoChar);
						
					}
					if(!UtilidadTexto.isEmpty(autorizacionEntSub.getAcronimoDiagnostico())&&autorizacionEntSub.getTipoCieDiagnostico()!=null)
						dtoArticulosAutorizaciones.setDiagnostico(autorizacionEntSub.getAcronimoDiagnostico()+" - "+autorizacionEntSub.getTipoCieDiagnostico());
					else
						dtoArticulosAutorizaciones.setDiagnostico("");
					dtoArticulosAutorizaciones.setNaturalezaArticulo(autorizacionEntSub.getNaturalezaArticulo());
					dtoArticulosAutorizaciones.setFechaOrden(autorizacionEntSub.getFechaOrden());
					dtoArticulosAutorizaciones.setNumeroOrden(Utilidades.convertirAEntero(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getConseOrdenMedica()));
					
					listaArticulosAutorizados.add(dtoArticulosAutorizaciones);
					
				
			}
		}
		int idDetalleMonto = subcuenta.getMontoCobro();
		String montoCobro="";
		if(idDetalleMonto >0){
			IDetalleMontoGeneralServicio detalleMontoGeneralServicio = 
				FacturacionServicioFabrica.crearDetalleMontoGeneralServicio();
			DTOMontosCobroDetalleGeneral detalleMontoGeneral = 
				detalleMontoGeneralServicio.obtenerValorTipoMonto(idDetalleMonto);
			
			if(detalleMontoGeneral!=null){
				
				String temporalMontoCobro="";
				if(detalleMontoGeneral.getPorcentaje()!=null)
					temporalMontoCobro=detalleMontoGeneral.getPorcentaje().doubleValue()+"%";
				else if (detalleMontoGeneral.getValor()!=null)
					temporalMontoCobro=" $"+String.valueOf(detalleMontoGeneral.getValor().doubleValue());

				if(!UtilidadTexto.isEmpty(temporalMontoCobro)){
					
					if((detalleMontoGeneral.getDetalleMonto().getTiposMonto().getCodigo())==
						ConstantesBD.codigoTipoMontoCopago){
						montoCobro=
							mensajes.getMessage("AutorizacionesEntidadSubcontratadaForm.acronimoTipoMontoCopago") + " " +					
						temporalMontoCobro; 
						
					}else if((detalleMontoGeneral.getDetalleMonto().getTiposMonto().getCodigo())==
						ConstantesBD.codigoTipoMontoCuotaModeradora){
						montoCobro=
							mensajes.getMessage("AutorizacionesEntidadSubcontratadaForm.acronimoTipoMontoCuotaModeradora") + " " + 
						temporalMontoCobro;
					}
				}
			}
		}
		dtoPaciente.setMontoCobro(montoCobro);
		
		
		if(listaArticulosAutorizados.size()>0){
			dtoReporteArticulos.setDtoPaciente(dtoPaciente);
			dtoReporteArticulos.setDatosEncabezado(infoEncabezado);
			dtoReporteArticulos.setDatosPie(infoPiePagina);
			dtoReporteArticulos.setTipoReporteMediaCarta(reporteMediaCarta);
			dtoReporteArticulos.setRutaLogo(institucion.getLogoJsp());
			dtoReporteArticulos.setUbicacionLogo(institucion.getUbicacionLogo());
			dtoReporteArticulos.setDtoAutorizacion(dtoAutorizacionReporte);
			dtoReporteArticulos.setListaArticulos(listaArticulosAutorizados);
			GeneradorReporteFormatoCapitacionAutorArticulos generadorReporteArticulos = 
				new GeneradorReporteFormatoCapitacionAutorArticulos(dtoReporteArticulos);
			JasperPrint reporte = generadorReporteArticulos.generarReporte();
			nombreArchivo = generadorReporteArticulos.exportarReportePDF(reporte, nombreReporte);
		}else if(listaServiciosAutorizados.size()>0){
			dtoReporteServicios.setDtoPaciente(dtoPaciente);
			dtoReporteServicios.setDatosEncabezado(infoEncabezado);
			dtoReporteServicios.setDatosPie(infoPiePagina);
			dtoReporteServicios.setTipoReporteMediaCarta(reporteMediaCarta);
			dtoReporteServicios.setRutaLogo(institucion.getLogoJsp());
			dtoReporteServicios.setUbicacionLogo(institucion.getUbicacionLogo());
			dtoReporteServicios.setDtoAutorizacion(dtoAutorizacionReporte);
			dtoReporteServicios.setListaServicios(listaServiciosAutorizados);
			GeneradorReporteFormatoCapitacionAutorservicio generadorReporteServicios = 
				new GeneradorReporteFormatoCapitacionAutorservicio(dtoReporteServicios);
			JasperPrint reporte = generadorReporteServicios.generarReporte();
			nombreArchivo = generadorReporteServicios.exportarReportePDF(reporte, nombreReporte);
		}
		
		listaNombresReportes.add(nombreArchivo);
		
		if(listaNombresReportes!=null && listaNombresReportes.size()>0){
			forma.setListaNombresReportes(listaNombresReportes);
		}
		
			
	}

/**
 * Metodo que realiza la busqueda segun los parametros ingresados
 * @param forma
 * @param usuario
 * @param paciente
 * @param institucionBasica
 * @param mapping
 * @param request
 * @return
 * @author Camilo Gomez
 */
@SuppressWarnings("static-access")
private ActionForward accionBusquedaAutorizacionesRango(ConsultarImprimirAutorizacionesEntSubcontratadasForm forma,UsuarioBasico usuario, PersonaBasica paciente,InstitucionBasica institucionBasica, ActionMapping mapping,	HttpServletRequest request) {
		
	    ActionErrors errores = new ActionErrors();
	    errores = mundo.validacionBusquedaporRango(forma.getParametrosBusqueda());	
	    if(errores.isEmpty())
	    {
			//forma.setAutorizacionesEntSubContratadas(mundo.obtenerAutorizacionesEntSubContrXRango(forma.getParametrosBusqueda(), usuario.getCodigoInstitucionInt()));
	    	//ArrayList <DtoAutorizacionEntSubContratada> listado=mundo.listadoAutorizacionesEntSub(paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt());
	    	ArrayList <DtoAutorizacionEntSubContratada> listado=mundo.obtenerAutorizacionesEntSubContrXRango(forma.getParametrosBusqueda(), usuario.getCodigoInstitucionInt());
	    	ArrayList <DtoAutorizacionEntSubContratada> tempListado=new ArrayList<DtoAutorizacionEntSubContratada>();
	    	ArrayList<String> codigoSolicitud=new ArrayList<String>();
	    	
	    	//Agrupa las solicuitudes de Medicamentos e Insumos en una misma autorizacion
	    	for(int i=0;i<listado.size();i++)
	    	{
	    		tempListado=new ArrayList<DtoAutorizacionEntSubContratada>();
	    		if(!codigoSolicitud.contains(listado.get(i).getNumeroSolicitud()))
	    		{
	    			for(int j=i;j<listado.size();j++)
	    			{
	    				if(listado.get(i).getNumeroSolicitud().equals(listado.get(j).getNumeroSolicitud()))
	    				{
	    					tempListado.add(listado.get(j));	
	    				}									
	    			}	
	    			codigoSolicitud.add(listado.get(i).getNumeroSolicitud());
	    			listado.get(i).setAgrupaListadoEntSub(tempListado);
	    		}
	    	}
	    	
	    	//Borra los registros vacios
	    	ArrayList<DtoAutorizacionEntSubContratada> listaDefinitiva = new ArrayList<DtoAutorizacionEntSubContratada>();
	    	for (DtoAutorizacionEntSubContratada retorno : listado) 
	    	{
	    		if (!Utilidades.isEmpty(retorno.getAgrupaListadoEntSub())) 
	    		{
	    			if(!UtilidadTexto.isEmpty(retorno.getConsecutivoAutorizacion())
	    					&& Utilidades.convertirAEntero(retorno.getConsecutivoAutorizacion())!=ConstantesBD.codigoNuncaValido)
	    			{
	    				listaDefinitiva.add(retorno);
	    			}
	    			else{
	    				if(retorno.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoInterna))
	    				{
	    					listaDefinitiva.add(retorno);
	    				}
	    			}
	    		}
	    	}	    	
	    	forma.setAutorizacionesEntSubContratadas(listaDefinitiva);
			
			return mapping.findForward("resultadoPeriodo");
		}
		else
		{		
			saveErrors(request, errores);	
			return mapping.findForward("periodo");
		} 
	
	}


/**
 * 	Metodo que realiza la Consulta de las Autorizaciones a entidades Subcontratadas	
 * @param forma
 * @param paciente
 * @param usuario
 * @param request
 * @param mapping
 * @return 
 * @author Camilo Gomez
 */
	
@SuppressWarnings("static-access")
private ActionForward listadoAutorizaciones(ConsultarImprimirAutorizacionesEntSubcontratadasForm forma, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		
	//forma.setAutorizacionesEntSubContratadas(mundo.listadoAutorizacionesEntSub(paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt()));
	ArrayList <DtoAutorizacionEntSubContratada> listado=mundo.listadoAutorizacionesEntSub(paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt());
	ArrayList <DtoAutorizacionEntSubContratada> tempListado=new ArrayList<DtoAutorizacionEntSubContratada>();
	ArrayList<String> codigoSolicitud=new ArrayList<String>();
	
	//Agrupa las solicuitudes de Medicamentos e Insumos en una misma autorizacion
	for(int i=0;i<listado.size();i++)
	{
		tempListado=new ArrayList<DtoAutorizacionEntSubContratada>();
		if(!codigoSolicitud.contains(listado.get(i).getNumeroSolicitud()))
		{
			for(int j=i;j<listado.size();j++)
			{
				if(listado.get(i).getNumeroSolicitud().equals(listado.get(j).getNumeroSolicitud()))
				{
					tempListado.add(listado.get(j));	
				}									
			}	
			codigoSolicitud.add(listado.get(i).getNumeroSolicitud());
			listado.get(i).setAgrupaListadoEntSub(tempListado);
		}
	}
	
	//Borra los registros vacios
	ArrayList<DtoAutorizacionEntSubContratada> listaDefinitiva = new ArrayList<DtoAutorizacionEntSubContratada>();
	for (DtoAutorizacionEntSubContratada retorno : listado) 
	{
		if (!Utilidades.isEmpty(retorno.getAgrupaListadoEntSub()))
		{
			if(!UtilidadTexto.isEmpty(retorno.getConsecutivoAutorizacion())
					&& Utilidades.convertirAEntero(retorno.getConsecutivoAutorizacion())!=ConstantesBD.codigoNuncaValido)
			{
				listaDefinitiva.add(retorno);
			}		
			else{
				if(retorno.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoInterna))
				{
					listaDefinitiva.add(retorno);
				}
			}
		}
	}
	/*logger.info("\n\n\n tamaño de la lista definitiva---->"+listaDefinitiva.size()+"\n\n\n\n");
	for(int i=0;i<listaDefinitiva.size();i++){
		logger.info("\n\n\n tamaño lista Interior---->"+listaDefinitiva.get(i).getAgrupaListadoEntSub().size());
	}*/
	
	forma.setAutorizacionesEntSubContratadas(listaDefinitiva);
	     return mapping.findForward("paciente");
}



/**
 * Metodo para Ordenar por columna
 * @param forma
 * @param mapping
 * @return
 */
@SuppressWarnings("static-access")
private ActionForward ordenarXColumna(ConsultarImprimirAutorizacionesEntSubcontratadasForm forma,ActionMapping mapping) {
		
	   ArrayList<DtoAutorizacionEntSubContratada> arrayOrdenado = new ArrayList<DtoAutorizacionEntSubContratada>();
	   arrayOrdenado=mundo.ordenarColumna(forma.getAutorizacionesEntSubContratadas(),forma.getUltimaPropiedad(), forma.getPropiedadOrdenar());
	   forma.setUltimaPropiedad(forma.getPropiedadOrdenar());
	   forma.resetArrayAutorizacionesEntSub();
	   forma.setAutorizacionesEntSubContratadas(arrayOrdenado);
	   
	   if(forma.getOpcionListadoAutorizacion().equals("paciente"))
		  {
		  return mapping.findForward("paciente");
		  }
		  else{
			  if(forma.getOpcionListadoAutorizacion().equals("periodo"))
			  {
			  return mapping.findForward("resultadoPeriodo");
			  }
		  }
	  
		return null;
	}


/**
 * 
 * @param forma
 * @param usuario
 * @param mapping
 * @param request
 * @return
 */
private ActionForward accionImprimirAutorizacion(ConsultarImprimirAutorizacionesEntSubcontratadasForm forma,UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente) {
	
	/*DtoAutorizacionEntSubContratada autorizacionEntSub= (DtoAutorizacionEntSubContratada)forma.getAutorizacionesEntSubContratadas().get(forma.getPosAutorizacion()); 
	request.setAttribute("nombreArchivo",ConsultaImpresionAutorizacionEntSubPdf.pdfDetalleConsultaImpresionAutorizacionEntSub(usuario, request, autorizacionEntSub));
	request.setAttribute("nombreVentana", "Autorizacion Ordenes Medicas ");
	return mapping.findForward("abrirPdf"); */ /**Se deja en comentario el código que anteriormente se estaba ejecutando para la impresión del detalle **/
	/**Según tarea xplanner2010 ID 33905, El formato que se debe mostrar al imprimir el detalle es el definido en el anexo 785,
	 * para servicios o medicamentos (Impresión Autorizaciones de Servicios, Impresión Autorizaciones de Medicamentos e Insumos) **/
	
	return mapping.findForward("detalleAutorizacion");
			
		
	
}
	/**
	 * Este m&eacute;todo se encarga de generar el formato estandar
	 * para las autorizaciones generadas de entidades subcontratadas
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param autorizacionEntSub
	 * @author Diana Carolina G
	 */
	private void generarReporteAutorizacionFormatoEstandar(ConsultarImprimirAutorizacionesEntSubcontratadasForm forma,
		PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request, DtoAutorizacionEntSubContratada autorizacionEntSub ) {
	
		String nombreReporte													= "AUTORIZACION ORDENES MEDICAS";
		String nombreArchivo 													= "";
		DTOReporteEstandarAutorizacionServiciosArticulos dtoReporte 			= new DTOReporteEstandarAutorizacionServiciosArticulos();
		GeneradorReporteFormatoEstandarAutorArticulos generadorReporteArticulos = new GeneradorReporteFormatoEstandarAutorArticulos(dtoReporte);
		GeneradorReporteFormatoEstandarAutorservicio generadorReporteServicios  = new GeneradorReporteFormatoEstandarAutorservicio(dtoReporte);
		ArrayList<String> listaNombresReportes 									= new ArrayList<String>();
		ArrayList<DtoServiciosAutorizaciones> listaServiciosAutorizados 		= new ArrayList<DtoServiciosAutorizaciones>();
		ArrayList<DtoArticulosAutorizaciones> listaArticulosAutorizados			= new ArrayList<DtoArticulosAutorizaciones>();
		DtoServiciosAutorizaciones dtoServiciosAutorizaciones					= new DtoServiciosAutorizaciones();	
		DtoArticulosAutorizaciones dtoArticulosAutorizaciones					= null;

		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
		String nombrePaciente = paciente.getPrimerNombre() + " " + 
			paciente.getSegundoNombre() + " " + paciente.getPrimerApellido() + 
			" " + paciente.getSegundoApellido();
			 			     			
		/** Se verifican los parámetros Impresión Media Carta, Encabezado, Pie **/
		String formatoReporte = ValoresPorDefecto.getImpresionMediaCarta(usuario.getCodigoInstitucionInt());
		String infoParametroGeneral = ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		String infoPiePagina=ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		
		if(UtilidadTexto.isEmpty(formatoReporte)){
			formatoReporte=ConstantesBD.acronimoNo;
		}
			
			dtoReporte.setNombrePaciente(nombrePaciente);
			dtoReporte.setTipoDocPaciente(paciente.getCodigoTipoIdentificacionPersona());
			dtoReporte.setNumeroDocPaciente(paciente.getNumeroIdentificacionPersona());
			dtoReporte.setTipoContrato(autorizacionEntSub.getNomTipoContrato());	 			     			
			dtoReporte.setEntidadSubcontratada(autorizacionEntSub.getNomEntidadAutorizada());
			dtoReporte.setNumeroAutorizacion(autorizacionEntSub.getConsecutivoAutorizacion());
			dtoReporte.setTelefonoEntidadSub(autorizacionEntSub.getTelEntidadAutorizada());
			dtoReporte.setDireccionEntidadSub(autorizacionEntSub.getDirEntidadAutorizada());
			dtoReporte.setFechaAutorizacion(autorizacionEntSub.getFechaAutorizacion());
			dtoReporte.setFechaVencimiento(autorizacionEntSub.getFechaVencimiento());
			dtoReporte.setEstadoAutorizacion((String)ValoresPorDefecto.getIntegridadDominio(autorizacionEntSub.getEstado()));
			
			dtoReporte.setEntidadAutoriza(autorizacionEntSub.getNomInstitucion());
			dtoReporte.setUsuarioAutoriza(autorizacionEntSub.getUsuarioModificacion());
			dtoServiciosAutorizaciones.setNumeroOrden(Integer.parseInt(autorizacionEntSub.getNumeroSolicitud()));
			
			dtoReporte.setFormatoMediaCarta(formatoReporte);
			dtoReporte.setInfoParametroGeneral(infoParametroGeneral);
			dtoReporte.setInfoPiePagina(infoPiePagina);
			dtoReporte.setRutaLogo(institucion.getLogoJsp());
			dtoReporte.setUbicacionLogo(institucion.getUbicacionLogo());
			dtoReporte.setObservaciones(autorizacionEntSub.getObservaciones());
				
			for (int i=0; i<autorizacionEntSub.getAgrupaListadoEntSub().size(); i++){
				/** Se obtiene información de los servicios autorizados **/
				if(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getCodServicio()>0){
					int codigoPropietario = Utilidades.convertirAEntero(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getCodigoPropietario());
					if(UtilidadTexto.isEmpty(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getCodigoPropietario()))
						dtoServiciosAutorizaciones.setCodigoServicio(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getCodServicio());
					else
						dtoServiciosAutorizaciones.setCodigoServicio(codigoPropietario);
					dtoServiciosAutorizaciones.setDescripcionServicio(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getNomServicio());
					dtoServiciosAutorizaciones.setCantidadSolicitada(Integer.parseInt(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getCantidad()));
					dtoServiciosAutorizaciones.setDescripcionNivelAutorizacion(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getNivel()); 
					
					listaServiciosAutorizados.add(dtoServiciosAutorizaciones);
					
				}else{ /** Se obtiene información de los artículos autorizados se verifica si es de naturaleza Medicamento**/
					
					if((autorizacionEntSub.getAgrupaListadoEntSub().get(i).getEs_medicamento().equals(ConstantesBD.codigoNaturalezaArticuloMedicamentoPos))
							|| autorizacionEntSub.getAgrupaListadoEntSub().get(i).getEs_medicamento().equals(ConstantesBD.codigoNaturalezaArticuloMedicamentoNoPos)) {
						
						dtoArticulosAutorizaciones = new DtoArticulosAutorizaciones();
						dtoArticulosAutorizaciones.setCodigoArticulo(Integer.parseInt(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getCodMedicamento()));
						dtoArticulosAutorizaciones.setDescripcionArticulo(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getDescripMedicamento());
						dtoArticulosAutorizaciones.setUnidadMedidaArticulo(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getUnidMedidaMedicamento());
						dtoArticulosAutorizaciones.setDosisFormulacion(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getDosisMedicamento());
						dtoArticulosAutorizaciones.setFrecuenciaFormulacion((UtilidadTexto.isEmpty(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getFrecuenciaMedicamento())?null:Integer.parseInt(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getFrecuenciaMedicamento())));
						dtoArticulosAutorizaciones.setTipoFrecuenciaFormulacion(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getTipoFrecueMedicamento());
						dtoArticulosAutorizaciones.setViaFormulacion(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getViaMedicamento());
						dtoArticulosAutorizaciones.setDiasTratamientoFormulacion((UtilidadTexto.isEmpty(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getDiasTrataMedicamento())?null:Long.parseLong(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getDiasTrataMedicamento())));
						dtoArticulosAutorizaciones.setCantidadAutorizadaArticulo(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getNroDosisTotalMedicamento());
						dtoArticulosAutorizaciones.setEsMedicamento(ConstantesBD.acronimoSiChar);
						
					}
					if(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getEs_medicamento().equals(ConstantesBD.codigoNaturalezaArticuloMaterialesInsumos)){
						dtoArticulosAutorizaciones = new DtoArticulosAutorizaciones();
						dtoArticulosAutorizaciones.setCodigoArticulo(Integer.parseInt(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getCodInsumo()));
						dtoArticulosAutorizaciones.setDescripcionArticulo(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getDescripInsumo());
						dtoArticulosAutorizaciones.setUnidadMedidaArticulo(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getUnidMedidaInsumo());
						dtoArticulosAutorizaciones.setCantidadSolicitada(Integer.parseInt(autorizacionEntSub.getAgrupaListadoEntSub().get(i).getNroDosisTotalInsumo()));
						dtoArticulosAutorizaciones.setEsMedicamento(ConstantesBD.acronimoNoChar);
						
					}
					
					listaArticulosAutorizados.add(dtoArticulosAutorizaciones);
					
				}
			}
				
			if(listaArticulosAutorizados.size()>0){
				dtoReporte.setListaArticulosAutorizados(listaArticulosAutorizados);
				JasperPrint reporte = generadorReporteArticulos.generarReporte();
				nombreArchivo = generadorReporteArticulos.exportarReportePDF(reporte, nombreReporte);
				
				
			}else if(listaServiciosAutorizados.size()>0){
				dtoReporte.setListaServiciosAutorizados(listaServiciosAutorizados);
				JasperPrint reporte = generadorReporteServicios.generarReporte();
				nombreArchivo = generadorReporteServicios.exportarReportePDF(reporte, nombreReporte);
				
			}
			
			listaNombresReportes.add(nombreArchivo);
			
			if(listaNombresReportes!=null && listaNombresReportes.size()>0){
				forma.setListaNombresReportes(listaNombresReportes);
			}
				
 }



/**
 * 
 * @param forma
 * @param usuario
 * @param mapping
 * @param request
 * @return
 */
private ActionForward accionImprimirListadoAutorizacion(ConsultarImprimirAutorizacionesEntSubcontratadasForm forma,UsuarioBasico usuario, ActionMapping mapping,HttpServletRequest request) {
	
	request.setAttribute("nombreArchivo",ListaConsultaAutorizacionEntSubPdf.pdfListadoConsultaAutorizacionEntSub(usuario, request,forma.getAutorizacionesEntSubContratadas()));
	request.setAttribute("nombreVentana", "Lista Autorizacion Ordenes Medicas ");
	return mapping.findForward("abrirPdf");
}


/**
 * 	Metodo para Validar que el paciente esté cargado en sesion	
 * @param con
 * @param forma
 * @param paciente
 * @param usuario
 * @param request
 * @param mapping
 * @return
 */
 private ActionForward accionValidarPaciente(Connection con,ConsultarImprimirAutorizacionesEntSubcontratadasForm forma, PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
			if(paciente==null || paciente.getCodigoPersona()<=0)
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
			}
	        
			return null;
    }
	
 


 
/**
 * 
 * @param forma
 * @param usuario
 * @param mapping
 * @param request
 * @return
 */
 @SuppressWarnings("static-access")
private ActionForward parametrosBusqueda(ConsultarImprimirAutorizacionesEntSubcontratadasForm forma,UsuarioBasico usuario, ActionMapping mapping,HttpServletRequest request) {
 		
 	     forma.setParametrosBusqueda(mundo.inicializarParametrosBusqueda());
 	     forma.setEntidadesSubcontratadas(mundo.listaEntidadesSubcontratadas());
 	     return mapping.findForward("periodo");
 		
 	}
 
	
}