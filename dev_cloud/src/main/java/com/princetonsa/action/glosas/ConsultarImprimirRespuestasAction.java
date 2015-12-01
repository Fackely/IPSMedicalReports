package com.princetonsa.action.glosas;

import java.sql.Connection;
import java.util.Collections;
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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ConstantesValoresPorDefecto;
import util.CsvFile;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.glosas.ConsultarImprimirRespuestasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.ConsultarImprimirRespuestas;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ExtractCSV;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Clase para consultar e imprimir las respuestas
 * Date: 2009-03-09
 * @author jfhernandez@princetonsa.com
 */
public class ConsultarImprimirRespuestasAction extends Action 
{
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(ConsultarImprimirGlosasAction.class);
	
	/**
	 * Metodo excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Connection con = null;
		try{
			if (response == null);
			if (form instanceof ConsultarImprimirRespuestasForm) {

				con = UtilidadBD.abrirConexion();

				if (con == null) {
					request.setAttribute("CodigoDescripcionError",
					"errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				ConsultarImprimirRespuestasForm forma = (ConsultarImprimirRespuestasForm) form;
				String estado = forma.getEstado();

				ActionErrors errores = new ActionErrors();

				UsuarioBasico usuario = (UsuarioBasico) request.getSession()
				.getAttribute("usuarioBasico");

				logger.info("\n\n\n ESTADO --> " + estado + "\n\n");

				if (estado == null) {
					forma.reset();
					logger
					.warn("Estado No Valido Dentro Del Flujo de Consultar e Imprimir Respuestas de Glosas (null)");
					request.setAttribute("CodigoDescripcionError",
					"Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				} else
					/*
					 * ------------------------------ ESTADO > empezar
					 * -------------------------------
					 */
					if (estado.equals("empezar")) {
						return accionEmpezar(con, forma, usuario, request, mapping);
					}
				/*
				 * ------------------------------ ESTADO > cargarConvenios
				 * -------------------------------
				 */
				if (estado.equals("cargarConvenios")) {
					return accionCargarConvenios(con, forma, usuario, request,
							mapping);
				}
				/*
				 * ------------------------------ ESTADO > cargarContratos
				 * -------------------------------
				 */
				if (estado.equals("cargarContratos")) {
					return accionCargarContratos(con, forma, usuario, request,
							mapping);
				}
				/*
				 * ------------------------------ ESTADO > cargarListadoGlosas
				 * -------------------------------
				 */
				if (estado.equals("cargarListadoGlosas")) {
					return accionCargarListadoGlosas(con, forma, usuario, request,
							mapping);
				}			
				/*
				 * ------------------------------ ESTADO >
				 * generarReporteListadoRespuestas-------------------------------
				 */
				if (estado.equals("generarReporteListadoRespuestas")) {
					return accionGenerarReporteListadoRespuestas(con, forma,
							usuario, request, mapping);
				}
				/*
				 * ------------------------------ ESTADO > mostrarRespuestaGlosa
				 * -------------------------------
				 */
				if (estado.equals("mostrarRespuestaGlosa")) {
					return accionMostrarRespuestaGlosa(con, forma, usuario,
							request, mapping);
				}
				/*
				 * ------------------------------ ESTADO >
				 * mostrarDetalleRespuestaFacturasGlosa
				 * -------------------------------
				 */
				if (estado.equals("mostrarDetalleRespuestaFacturasGlosa")) {
					return accionMostrarDetalleRespuestaFacturasGlosa(con, forma,
							usuario, request, mapping);
				}
				/*
				 * ------------------------------ ESTADO >
				 * mostrarDetalleRespuestaSolicitudesGlosa
				 * -------------------------------
				 */
				if (estado.equals("mostrarDetalleRespuestaSolicitudesGlosa")) {
					return accionMostrarDetalleRespuestaSolicitudesGlosa(con,
							forma, usuario, request, mapping);
				}
				/*
				 * ------------------------------ ESTADO >
				 * generarReporteListadoFacturasRespuestas
				 * -------------------------------
				 */
				if (estado.equals("generarReporteRespuestasFacturas")) {
					return accionGenerarReporteRespuestasFacturas(con, forma, usuario, request, mapping);
				}

				/*
				 * ------------------------------ ESTADO >
				 * generarReporteRespuestaGlosaFactura
				 * -------------------------------
				 */
				if (estado.equals("generarReporteRespuestaGlosaFactura")) {
					return accionGenerarReporteRespuestaGlosaFactura(con, forma, usuario, request, mapping);
				}


				/*
				 * ------------------------------ ESTADO >
				 * generarReporteRespuestaGlosa
				 * -------------------------------
				 */
				if (estado.equals("generarReporteRespuestaGlosa")) {
					return accionGenerarReporteRespuestaGlosa(con, forma, usuario, request, mapping);
				}

				if (estado.equals("ordenar"))
				{				
					return accionOrdenar(forma,request,mapping);

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
	 * Metodo que realiza el ordenamiento por columnas
	 * @param forma
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(ConsultarImprimirRespuestasForm forma,HttpServletRequest request, ActionMapping mapping) {
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPropiedadOrdenar()))
		{
			forma.setEsDescendente(forma.getPropiedadOrdenar()+"descendente") ;
		}else{
			forma.setEsDescendente(forma.getPropiedadOrdenar());
		}	
		
		logger.info("patron ORDENAR-> " + forma.getPropiedadOrdenar());
		logger.info("DESCENDENTE --> " + forma.getEsDescendente() );
		
		if(forma.getEsDescendente().equals(forma.getPropiedadOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPropiedadOrdenar(),ordenamiento);
		Collections.sort(forma.getArrayListGlosas(),sortG);
		return mapping.findForward("filtro");
		
	}

	/**
	 * accionGenerarReporteRespuestaGlosaFactura
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGenerarReporteRespuestaGlosaFactura(
			Connection con, ConsultarImprimirRespuestasForm forma,
			UsuarioBasico usuario, HttpServletRequest request,
			ActionMapping mapping) {
		
		String nombreRptDesign = "ImpresionRespuestaGlosaFactura.rptdesign";
		InstitucionBasica ins = (InstitucionBasica) request.getSession().getAttribute("institucionBasica");
		Vector v;

		// ***************** INFORMACIÓN DEL CABEZOTE
		DesignEngineApi comp;
		comp = new DesignEngineApi(ParamsBirtApplication.getReportsPath()+ "glosas/", nombreRptDesign);

		// Logo
		comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());

		// Nombre Institución, titulo y rango de fechas
		comp.insertGridHeaderOfMasterPageWithName(0, 1, 1, 2, "titulo");

		v = new Vector();
		v.add(ins.getRazonSocial());
		v.add("\nIMPRESIÓN RESPUESTA GLOSA FACTURA");
		comp.insertLabelInGridOfMasterPage(0, 1, v);

		// Parametros de Generación
		comp.insertGridHeaderOfMasterPageWithName(1, 0, 1, 1, "param");
		v = new Vector();

		String filtroo = "";
		v.add(filtroo);
		
		comp.insertLabelInGridOfMasterPageWithProperties(1, 0, v,DesignChoiceConstants.TEXT_ALIGN_LEFT);

		// Fecha hora de proceso y usuario
		comp.insertLabelInGridPpalOfFooter(0, 0, "Fecha: "+UtilidadFecha.getFechaActual() +" Hora: "+ UtilidadFecha.getHoraActual());
		comp.insertLabelInGridPpalOfFooter(0, 1, "Usuario: "+ usuario.getLoginUsuario());
		// ****************** FIN INFORMACIÓN DEL CABEZOTE

		comp.obtenerComponentesDataSet("dataSet");

		// ***************** NUEVA CONSULTA DEL REPORTE
		logger.info("Consulta > "+ forma.getRespuestaFacturaGlosa().getSqlImpresionRespuestaGlosaFactura());
		comp.modificarQueryDataSet(forma.getRespuestaFacturaGlosa().getSqlImpresionRespuestaGlosaFactura());
		// *****************

		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		comp.updateJDBCParameters(newPathReport);

		if (forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			newPathReport += 	"&convenio="+forma.getGlosa().getNombreConvenio() + 
								"&respuesta="+forma.getGlosa().getDtoRespuestaGlosa().getRespuestaConsecutivo()+
								"&glosa="+forma.getGlosa().getGlosaSistema()+
								"&factura="+forma.getRespuestaFacturaGlosa().getConsecutivoFactura()+
								"&valorglosafactura="+forma.getRespuestaFacturaGlosa().getValorGlosa()+
								"&indicativofueauditada="+forma.getGlosa().getIndicativoFueAuditada();
							

		if (!newPathReport.equals("")) {
			request.setAttribute("isOpenReport", "true");
			request.setAttribute("newPathReport", newPathReport);
		}
		// ******************* GENERAR ARCHIVO PLANO
		if (forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo)) {
			String encabezado = "Reporte: Impresión Respuesta Factura\n" + 
								"Convenio: "+forma.getGlosa().getNombreConvenio() +", "+
								"Respuesta: "+forma.getGlosa().getDtoRespuestaGlosa().getRespuestaConsecutivo()+", "+
								"Glosa: " + forma.getGlosa().getGlosaSistema()+", "+
								"Factura: "+forma.getRespuestaFacturaGlosa().getConsecutivoFactura()+", "+
								"Valor Glosa Factura: "+forma.getRespuestaFacturaGlosa().getValorGlosa()+
								"Indicativo: "+forma.getGlosa().getIndicativoFueAuditada();

			String nombre = CsvFile.armarNombreArchivo("ImpresionRespuestaGlosaFactura", usuario);
			ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvConEncabezadoComprimido(newPathReport,nombre, encabezado);
			if (resultado.isTrue()) {
				String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
				forma.setUrlArchivoPlano(rutas[0]);
				forma.setPathArchivoPlano(rutas[2]);
			} else {
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
				saveErrors(request, errores);
			}
		}
		// ******************* FIN GENERAR ARCHIVO PLANO

		UtilidadBD.closeConnection(con);
		return mapping.findForward("respuestaSolicitudesGlosa");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionMostrarDetalleRespuestaSolicitudesGlosa(
			Connection con, ConsultarImprimirRespuestasForm forma,
			UsuarioBasico usuario, HttpServletRequest request,
			ActionMapping mapping) {
		forma.setRespuestaFacturaGlosa(ConsultarImprimirRespuestas.consultarRespuestaSolicitudesGlosa(con, forma.getGlosa().getRespuestasFacturas(forma.getPosArray()), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("respuestaSolicitudesGlosa");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionMostrarDetalleRespuestaFacturasGlosa(
			Connection con, ConsultarImprimirRespuestasForm forma,
			UsuarioBasico usuario, HttpServletRequest request,
			ActionMapping mapping) {
		forma.setGlosa(ConsultarImprimirRespuestas
				.consultarRespuestaFacturasGlosa(con, forma.getGlosa()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("respuestaFacturasGlosa");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionMostrarRespuestaGlosa(Connection con,
			ConsultarImprimirRespuestasForm forma, UsuarioBasico usuario,
			HttpServletRequest request, ActionMapping mapping) {
		forma.setGlosa(forma.getArrayListGlosas(forma.getPosArray()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("respuestaGlosa");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con,
			ConsultarImprimirRespuestasForm forma, UsuarioBasico usuario,
			HttpServletRequest request, ActionMapping mapping) {
		forma.reset();
		UtilidadBD.closeConnection(con);
		return mapping.findForward("filtro");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCargarConvenios(Connection con,
			ConsultarImprimirRespuestasForm forma, UsuarioBasico usuario,
			HttpServletRequest request, ActionMapping mapping) {
		forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", true, "",
				true));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("filtro");
	}

	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCargarContratos(Connection con,
			ConsultarImprimirRespuestasForm forma, UsuarioBasico usuario,
			HttpServletRequest request, ActionMapping mapping) {
		forma.setContratos(Utilidades.obtenerContratos(con, Utilidades
				.convertirAEntero(forma.getFiltrosMap("convenio").toString()),
				false, true));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("filtro");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCargarListadoGlosas(Connection con,ConsultarImprimirRespuestasForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) 
	{
		forma.setFiltrosMap("institucion", usuario.getCodigoInstitucion());
		Utilidades.imprimirMapa(forma.getFiltrosMap());
		forma.setArrayListGlosas(ConsultarImprimirRespuestas.listarGlosas(con,
				forma.getFiltrosMap()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("filtro");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarListadoRespuestas(Connection con,
			ConsultarImprimirRespuestasForm forma, UsuarioBasico usuario,
			HttpServletRequest request, ActionMapping mapping) {
		// String[] indicesRespuestaArray =
		// {"codrespuesta","resconsecutivo","fecharespuesta"
		// ,"estado","valrespuesta"
		// ,"codigoglosa","glosasistema","convenio","nomconvenio","contrato",
		// "numcontrato","glosaentidad","fechanoti","valorglosa",""};
		UtilidadBD.closeConnection(con);
		return mapping.findForward("filtro");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGenerarReporteListadoRespuestas(Connection con,
			ConsultarImprimirRespuestasForm forma, UsuarioBasico usuario,
			HttpServletRequest request, ActionMapping mapping) {
		String nombreRptDesign = "ListadoRespuestas.rptdesign";
		InstitucionBasica ins = (InstitucionBasica) request.getSession()
				.getAttribute("institucionBasica");
		Vector v;

		// ***************** INFORMACIÓN DEL CABEZOTE
		DesignEngineApi comp;
		comp = new DesignEngineApi(ParamsBirtApplication.getReportsPath()
				+ "glosas/", nombreRptDesign);

		// Logo
		comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());

		// Nombre Institución, titulo y rango de fechas
		comp.insertGridHeaderOfMasterPageWithName(0, 1, 1, 2, "titulo");

		v = new Vector();
		v.add(ins.getRazonSocial());
		v.add("\nIMPRESIÓN RESPUESTA GLOSA ");
		comp.insertLabelInGridOfMasterPage(0, 1, v);

		// Parametros de Generación
		comp.insertGridHeaderOfMasterPageWithName(1, 0, 1, 1, "param");
		v = new Vector();

		// Rango de Fechas
		String filtroo = "";

		if (!forma.getFiltrosMap("fechaResIni").toString().equals("") && !forma.getFiltrosMap("fechaResFin").toString().equals(""))
			filtroo = 	"Fecha Respuesta Inicial: "+ UtilidadFecha.conversionFormatoFechaAAp(forma.getFiltrosMap("fechaResIni")+ "")+" "+
						"Fecha Respuesta Final: "+ UtilidadFecha.conversionFormatoFechaAAp(forma.getFiltrosMap("fechaResFin")+ "")+", ";

		if (!forma.getFiltrosMap("resIni").toString().equals("") && !forma.getFiltrosMap("resFin").toString().equals(""))
			filtroo = 	"Respuesta Inicial: " + forma.getFiltrosMap("resIni")+" "+ 
						"Respuesta Final: " + forma.getFiltrosMap("resFin")+", ";

		if (!forma.getFiltrosMap("glosa").toString().equals(""))
			filtroo += "Glosa: " + forma.getFiltrosMap("glosa") + ", ";

		if (!forma.getFiltrosMap("glosaE").toString().equals(""))
			filtroo += "Glosa Entidad: " + forma.getFiltrosMap("glosaE") + ", ";

		if (!forma.getFiltrosMap("convenio").toString().equals(""))
			filtroo += "Convenio: " + forma.getFiltrosMap("convenio") + ", ";

		if (!forma.getFiltrosMap("contrato").toString().equals(""))
			filtroo += "Contrato: " + forma.getFiltrosMap("contrato") + ", ";

		if (!forma.getFiltrosMap("fechaNoti").toString().equals(""))
			filtroo += "Fecha Notificación: "+ UtilidadFecha.conversionFormatoFechaAAp(forma.getFiltrosMap("fechaNoti")+ "")+", ";

		if (!forma.getFiltrosMap("estadoRes").toString().equals(""))
			filtroo += "Estado Respuesta: " +forma.getFiltrosMap("estadoRes").toString().split(ConstantesBD.separadorSplit)[1];

		v.add(filtroo);
		comp.insertLabelInGridOfMasterPageWithProperties(1, 0, v,DesignChoiceConstants.TEXT_ALIGN_LEFT);

		// Fecha hora de proceso y usuario
		comp.insertLabelInGridPpalOfFooter(0, 0, "Fecha: "
				+ UtilidadFecha.getFechaActual() + " Hora: "
				+ UtilidadFecha.getHoraActual());
		comp.insertLabelInGridPpalOfFooter(0, 1, "Usuario: "
				+ usuario.getLoginUsuario());
		// ****************** FIN INFORMACIÓN DEL CABEZOTE

		comp.obtenerComponentesDataSet("dataSet");

		// ***************** NUEVA CONSULTA DEL REPORTE
		logger.info("Consulta > " + forma.getFiltrosMap("cadenasql"));
		comp.modificarQueryDataSet(forma.getFiltrosMap("cadenasql") + "");
		// *****************

		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		comp.updateJDBCParameters(newPathReport);

		if (!newPathReport.equals("")) {
			request.setAttribute("isOpenReport", "true");
			request.setAttribute("newPathReport", newPathReport);
		}
		// ******************* GENERAR ARCHIVO PLANO
		if (forma.getTipoSalida().equals(
				ConstantesIntegridadDominio.acronimoTipoSalidaArchivo)) {
			String encabezado = "Reporte: Listado Respuestas\n"
					+ "Parametros de Generación(" + filtroo + ")\n";

			String nombre = CsvFile.armarNombreArchivo("ListadoRespuestas",
					usuario);
			ResultadoBoolean resultado = ExtractCSV
					.extraerArchivoCsvConEncabezadoComprimido(newPathReport,
							nombre, encabezado);
			if (resultado.isTrue()) {
				String[] rutas = resultado.getDescripcion().split(
						ConstantesBD.separadorSplit);
				forma.setUrlArchivoPlano(rutas[0]);
				forma.setPathArchivoPlano(rutas[2]);
			} else {
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("errors.notEspecific",
						resultado.getDescripcion()));
				saveErrors(request, errores);
			}
		}
		// ******************* FIN GENERAR ARCHIVO PLANO

		UtilidadBD.closeConnection(con);
		return mapping.findForward("filtro");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGenerarReporteRespuestasFacturas(Connection con, ConsultarImprimirRespuestasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		String nombreRptDesign = "ImpresionDetalleRespuesta.rptdesign";
		InstitucionBasica ins = (InstitucionBasica) request.getSession()
				.getAttribute("institucionBasica");
		Vector v;

		// ***************** INFORMACIÓN DEL CABEZOTE
		DesignEngineApi comp;
		comp = new DesignEngineApi(ParamsBirtApplication.getReportsPath()
				+ "glosas/", nombreRptDesign);

		// Logo
		comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());

		// Nombre Institución, titulo y rango de fechas
		comp.insertGridHeaderOfMasterPageWithName(0, 1, 1, 2, "titulo");

		v = new Vector();
		v.add(ins.getRazonSocial());
		v.add("\nIMPRESION DETALLE RESPUESTA GLOSA ");
		comp.insertLabelInGridOfMasterPage(0, 1, v);

		// Parametros de Generación
		comp.insertGridHeaderOfMasterPageWithName(1, 0, 1, 1, "param");
		v = new Vector();

		// Rango de Fechas
		String filtroo = "";
		v.add(filtroo);
		comp.insertLabelInGridOfMasterPageWithProperties(1, 0, v,
				DesignChoiceConstants.TEXT_ALIGN_LEFT);

		// Fecha hora de proceso y usuario
		comp.insertLabelInGridPpalOfFooter(0, 0, "Fecha: "
				+ UtilidadFecha.getFechaActual() + " Hora: "
				+ UtilidadFecha.getHoraActual());
		comp.insertLabelInGridPpalOfFooter(0, 1, "Usuario: "
				+ usuario.getLoginUsuario());
		// ****************** FIN INFORMACIÓN DEL CABEZOTE

		comp.obtenerComponentesDataSet("dataSet");

		// ***************** NUEVA CONSULTA DEL REPORTE
		logger.info("Consulta > "
				+ forma.getGlosa().getSqlImpresionDetalleRespuesta());
		comp.modificarQueryDataSet(forma.getGlosa()
				.getSqlImpresionDetalleRespuesta());
		// *****************

		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		comp.updateJDBCParameters(newPathReport);

		if (forma.getTipoSalida().equals(
				ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			newPathReport 	+="&convenio="+ forma.getGlosa().getNombreConvenio()
							+"&respuesta="+ forma.getGlosa().getDtoRespuestaGlosa().getRespuestaConsecutivo() 
							+"&glosa="+ forma.getGlosa().getGlosaSistema() 
							+"&valorglosa="+ UtilidadTexto.formatearValores(forma.getGlosa().getValorGlosa())
							+"&indicativofueauditada="+ forma.getGlosa().getIndicativoFueAuditada();
		
		if (!newPathReport.equals("")) {
			request.setAttribute("isOpenReport", "true");
			request.setAttribute("newPathReport", newPathReport);
		}
		// ******************* GENERAR ARCHIVO PLANO
		if (forma.getTipoSalida().equals(
				ConstantesIntegridadDominio.acronimoTipoSalidaArchivo)) {
			String encabezado = "Reporte: Impresión Detalle Respuesta\n"
					+ "Convenio: "
					+ forma.getGlosa().getNombreConvenio()
					+ ", "
					+ "Respuesta: "
					+ forma.getGlosa().getDtoRespuestaGlosa()
							.getRespuestaConsecutivo() + ", " + "Glosa: "
					+ forma.getGlosa().getGlosaSistema() + ", "
					+ "Valor Glosa: " + forma.getGlosa().getValorGlosa()+", "
					+"Indicativo: "+forma.getGlosa().getIndicativoFueAuditada();

			String nombre = CsvFile.armarNombreArchivo(
					"ImpresionDetalleRespuesta", usuario);
			ResultadoBoolean resultado = ExtractCSV
					.extraerArchivoCsvConEncabezadoComprimido(newPathReport,
							nombre, encabezado);
			if (resultado.isTrue()) {
				String[] rutas = resultado.getDescripcion().split(
						ConstantesBD.separadorSplit);
				forma.setUrlArchivoPlano(rutas[0]);
				forma.setPathArchivoPlano(rutas[2]);
			} else {
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("errors.notEspecific",
						resultado.getDescripcion()));
				saveErrors(request, errores);
			}
		}
		// ******************* FIN GENERAR ARCHIVO PLANO

		UtilidadBD.closeConnection(con);
		return mapping.findForward("respuestaFacturasGlosa");
	}

/**
 * 
 * @param con
 * @param forma
 * @param usuario
 * @param request
 * @param mapping
 * @return
 */
 private ActionForward accionGenerarReporteRespuestaGlosa(Connection con,
			ConsultarImprimirRespuestasForm forma, UsuarioBasico usuario,
			HttpServletRequest request, ActionMapping mapping) 
 {
	 
	 	String formatoImpresion= ValoresPorDefecto.getFormatoImpresionRespuesGlosa(usuario.getCodigoInstitucionInt());
	 	String nombreRptDesign="";
	 	String newPathReport = "";
	 	//Nombre del formato de impresion cuando viene de cunsultar/imprimir Rtas  
	 	if(formatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionResSuba))
	 		nombreRptDesign = "ImpresionRespuestaGlosaSuba.rptdesign";
	 	else	
	 		nombreRptDesign = "ImpresionRespuestaGlosaStandar.rptdesign";
	 	
	 	//Nombre del formato de imrpesion cuando viene de la funcionalidad Registrar rtas
	 	if (request.getParameter("formatoImpresion")!=null)
	 		if(request.getParameter("formatoImpresion").equals(ConstantesIntegridadDominio.acronimoFormatoImpresionResEstandar))
	 			nombreRptDesign = "ImpresionRespuestaGlosaStandar.rptdesign";
	 		
	 	InstitucionBasica ins = (InstitucionBasica) request.getSession().getAttribute("institucionBasica");
		Vector v;

		// ***************** INFORMACIÓN DEL CABEZOTE
		DesignEngineApi comp;
		comp = new DesignEngineApi(ParamsBirtApplication.getReportsPath()
				+ "glosas/", nombreRptDesign);

		// Logo
		comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());

		// Nombre Institución, titulo y rango de fechas
		comp.insertGridHeaderOfMasterPageWithName(0, 1, 1, 2, "titulo");

		v = new Vector();
		v.add(ins.getRazonSocial());
		
		//Encabezado cuando la imrpresion se ahce para consultar imprimir rtas
		if(formatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionResSuba))
			v.add("\nOFICINA DE FACTURACIÓN" +"\nRESPUESTA GLOSA No. "+forma.getGlosa().getGlosaSistema()+" - "+forma.getGlosa().getNombreConvenio());
		else	
			v.add("\nIMPRESION RESPUESTA DE GLOSA ESTÁNDAR");
		
		//Encabezado cuando la imrpesuion se ahce para la funcionalidad registrar rtas
		if (request.getParameter("formatoImpresion")!=null)
			if(request.getParameter("formatoImpresion").equals(ConstantesIntegridadDominio.acronimoFormatoImpresionResEstandar))
				v.add("\nIMPRESION RESPUESTA DE GLOSA ESTÁNDAR");
		
		comp.insertLabelInGridOfMasterPage(0, 1, v);

		
		// Fecha hora de proceso y usuario
		comp.insertLabelInGridPpalOfFooter(0, 0, "Fecha: "+ UtilidadFecha.getFechaActual() + " Hora:"+ UtilidadFecha.getHoraActual());
		comp.insertLabelInGridPpalOfFooter(0, 1, "Usuario:"+ usuario.getLoginUsuario());
		// ****************** FIN INFORMACIÓN DEL CABEZOTE"

		// ***************** NUEVA CONSULTA DEL REPORTE
		
		
		comp.obtenerComponentesDataSet("dataSetFirmas");
		comp.modificarQueryDataSet(comp.obtenerQueryDataSet().replaceAll("999999999", usuario.getCodigoInstitucion()).replaceAll("nombreparametro", ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionRespuesGlosa));
		
		comp.obtenerComponentesDataSet("dataSet");
		
		
		
		if(request.getParameter("nombreFormaRemite")!=null && request.getParameter("codigorespuesta")!=null && request.getParameter("convenio")!=null && request.getParameter("glosaentidad")!= null && request.getParameter("fecharegi")!= null && request.getParameter("glosasistema")!=null && request.getParameter("fecharadi")!=null && request.getParameter("indicativo")!=null)
		{
			forma.reset();
						
			logger.info("Consulta >"+ConsultasBirt.impresionRespuestaGlosaStandar(request.getParameter("codigorespuesta")+""));
			comp.modificarQueryDataSet(ConsultasBirt.impresionRespuestaGlosaStandar(request.getParameter("codigorespuesta")+""));
			
			//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
	      	comp.lowerAliasDataSet();
			newPathReport = comp.saveReport1(false);
			comp.updateJDBCParameters(newPathReport);
			
			if(request.getParameter("formatoImpresion").equals(ConstantesIntegridadDominio.acronimoFormatoImpresionResEstandar))
				if (request.getParameter("tiposalida").equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))					
				{
					newPathReport += 
							"&convenio="+ request.getParameter("convenio")+ 
							"&glosaentidad="+ request.getParameter("glosaentidad")+
							"&fechanotificacion="+ request.getParameter("fecharegi")+
							"&glosa="+ request.getParameter("glosasistema")+
							"&respuesta="+ request.getParameter("codigorespuesta")+
							"&fecharadicacion="+ request.getParameter("fecharadi")+
							"&indicativofueauditada="+request.getParameter("indicativo");
					forma.setTipoSalida(request.getParameter("tiposalida"));
				}
			if (!newPathReport.equals("")) {
				
				request.setAttribute("isOpenReport", "true");
				request.setAttribute("newPathReport", newPathReport);
			}
			UtilidadBD.closeConnection(con);
			return mapping.findForward("respuestaFacturasGlosa");
		}
		else
		{
			logger.info("Consulta >"+ConsultasBirt.impresionRespuestaGlosaStandar(forma.getGlosa().getDtoRespuestaGlosa().getCodigoPk()+""));
			comp.modificarQueryDataSet(ConsultasBirt.impresionRespuestaGlosaStandar(forma.getGlosa().getDtoRespuestaGlosa().getCodigoPk()+""));
				
			//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
	      	comp.lowerAliasDataSet();
			newPathReport = comp.saveReport1(false);
			comp.updateJDBCParameters(newPathReport);
	
			if(formatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionResEstandar))
				if (forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
					newPathReport += 
							"&convenio="+ forma.getGlosa().getNombreConvenio()+ 
							"&glosaentidad="+ forma.getGlosa().getGlosaEntidad()+
							"&fechanotificacion="+ forma.getGlosa().getFechaNotificacion()+
							"&glosa="+ forma.getGlosa().getGlosaSistema()+
							"&respuesta="+ forma.getGlosa().getDtoRespuestaGlosa().getRespuestaConsecutivo()+
							"&fecharadicacion="+ forma.getGlosa().getDtoRespuestaGlosa().getFechaRadicacion()+
							"&indicativofueauditada="+forma.getGlosa().getIndicativoFueAuditada();
			
			if (!newPathReport.equals("")) {
				request.setAttribute("isOpenReport", "true");
				request.setAttribute("newPathReport", newPathReport);
			}
		}
		// ******************* GENERAR ARCHIVO PLANO

		if (forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo)) {
			
			String encabezado="";
			String nombre="";
			
			if(formatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionResSuba)){
				encabezado = "Reporte: Impresión Respuesta Glosa Suba";
				nombre = CsvFile.armarNombreArchivo("ImpresionRespuestaGlosaSuba", usuario);
			}
			else
			{	
				encabezado = "Reporte: Impresión Respuesta Glosa Estandar\n"+
							 "Convenio="+ forma.getGlosa().getNombreConvenio()+ ", "+
							 "Glosa Entidad="+ forma.getGlosa().getGlosaEntidad()+ ", "+
							 "Fecha Notificacion="+ forma.getGlosa().getFechaNotificacion()+ ", "+
							 "Glosa="+ forma.getGlosa().getGlosaSistema()+ ", "+
							 "Respuesta="+ forma.getGlosa().getDtoRespuestaGlosa().getRespuestaConsecutivo()+ ", "+
							 "Fecha Radicacion="+ forma.getGlosa().getDtoRespuestaGlosa().getFechaRadicacion()+
							 "Indicativo: "+forma.getGlosa().getIndicativoFueAuditada();
				nombre = CsvFile.armarNombreArchivo("ImpresionRespuestaGlosaStandar", usuario);
			}
			ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvConEncabezadoComprimido(newPathReport,nombre, encabezado);
			if (resultado.isTrue()) {
				String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
				forma.setUrlArchivoPlano(rutas[0]);
				forma.setPathArchivoPlano(rutas[2]);
			} else {
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
				saveErrors(request, errores);
			}
		}
		// ******************* FIN GENERAR ARCHIVO PLANO

		UtilidadBD.closeConnection(con);
		return mapping.findForward("respuestaFacturasGlosa");
	}


}