/*
 * @(#)ConsultaFacturasAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.facturacion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.ConsultaFacturasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ConsultaFacturas;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;
import com.princetonsa.mundo.facturacion.Factura;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.resumenAtenciones.EstadoCuenta;
import com.princetonsa.pdf.ConsultaFacturasPdf;
import com.princetonsa.pdf.FacturacionPdf;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Clase encargada del control de la funcionalidad de Consulta de Facturas
 * 
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 10 /Jun/ 2005
 */
public class ConsultaFacturasAction extends Action {
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action
	 */
	private Logger logger = Logger.getLogger(ConsultaFacturasAction.class);

	private static final int IMPRIMIR_COPIA=1;
	private static final int IMPRIMIR_ORIGINAL=2;
	private static final int IMPRIMIR_AMBOS=3;
	
	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Connection con = null;
		try{
			if (form instanceof ConsultaFacturasForm) {


				/** Intentamos abrir una conexion con la fuente de datos **/
				con = openDBConnection(con);
				if (con == null) {
					request.setAttribute("codigoDescripcionError",
					"errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				ConsultaFacturasForm consultaFacturasForm = (ConsultaFacturasForm) form;

				HttpSession session = request.getSession();
				PersonaBasica paciente = (PersonaBasica) session
				.getAttribute("pacienteActivo");
				UsuarioBasico medico = (UsuarioBasico) session
				.getAttribute("usuarioBasico");

				// *****MANEJO DE SESIÓN CADUCADA**************************+
				if (medico == null) {
					this.cerrarConexion(con);
					return mapping.findForward("inicio");
				}
				// *****************************************
				int maxPageItems = Integer.parseInt(ValoresPorDefecto
						.getMaxPageItems(medico.getCodigoInstitucionInt()));

				ConsultaFacturas mundo = new ConsultaFacturas();
				String estado = consultaFacturasForm.getEstado();
				logger.warn("[ConsultaFacturasAction] estado->" + estado);

				
				if(Utilidades.tieneRolFuncionalidad(medico.getLoginUsuario(), ConstantesBD.permisoImprimirOriginalFactura)){
					consultaFacturasForm.setTienePermisoImprimirOriginalFactura(true);
				}
				
				if (estado == null) {
					logger
					.warn("Estado no valido dentro del flujo de ConsultasFacturasAction (null) ");
					request.setAttribute("codigoDescripcionError",
					"errors.estadoInvalido");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				else if (estado.equals("empezarPaciente")) {
					if (paciente == null
							|| paciente.getCodigoTipoIdentificacionPersona()
							.equals("")) {
						return ComunAction.accionSalirCasoError(mapping, request,
								con, logger, "paciente null o sin id",
								"errors.paciente.noCargado", true);
					} else {
						return this.accionEmpezarPaciente(mapping, request,
								consultaFacturasForm, con, paciente, mundo, medico);
					}
				} else if (estado.equals("iniciarBusqueda")) {
					consultaFacturasForm.reset();

					HashMap criteriosMap = new HashMap();
					criteriosMap.put("institucion2_0", medico.getCodigoInstitucion());
					consultaFacturasForm.setEntidadesSubcontratadasMap(EntidadesSubContratadas.buscar0(con, criteriosMap));

					if (UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(medico.getCodigoInstitucionInt()))) {
						criteriosMap = new HashMap();
						criteriosMap.put("institucion3_", medico
								.getCodigoInstitucion());
						consultaFacturasForm.setEmpresasInstitucionMap(ParametrizacionInstitucion.consultarEmpresas(con, criteriosMap));
					}

				//consultaFacturasForm.setCodigoCentroAtencion(medico.getCodigoCentroAtencion()); se comenta por MT 1772 DCU 234 v1.59
					this.cerrarConexion(con);
					return mapping.findForward("inicioBusqueda");
				} else if (estado.equals("ordenarColumnaPaciente")) {
					return this.accionOrdenarColumnaPaciente(con,
							consultaFacturasForm, response);
				} else if (estado.equals("volverDetalleFacturas")) {
					if (consultaFacturasForm
							.getTipoReporte()
							.equals(
									ConstantesIntegridadDominio.acronimoTipoReporteConvenio)) {
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listadoTipoReporteConvenio");
					} else if (consultaFacturasForm
							.getTipoReporte()
							.equals(
									ConstantesIntegridadDominio.acronimoTipoReporteConvenioPaciente)) {
						this.cerrarConexion(con);
						return mapping.findForward("busquedaAvanzada");
					} else if (consultaFacturasForm
							.getTipoReporte()
							.equals(
									ConstantesIntegridadDominio.acronimoTipoReporteFacturadoRadicado)) {
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listadoTipoReporteFacturado");
					}
					this.cerrarConexion(con);
					return mapping.findForward("listadoTipoReporteConvenio");
					
				}else if(estado.equals("volverDetalleFacturasPaciente")){
					UtilidadBD.closeConnection(con);
					this.cerrarConexion(con);
					return mapping.findForward("facturasPaciente");
				}

				else if (estado.equals("ordenarColumnaTodos")) {
					return this.accionOrdenarColumnaTodos(con,consultaFacturasForm, response);
				} else if (estado.equals("ordenarColumnaTipoReporteConvenio")) {
					return this.accionOrdenarColumnaReporteConvenio(con,consultaFacturasForm, response);
				} else if (estado.equals("ordenarColumnaTipoReporteFacturado")) {
					return this.accionOrdenarColumnaReporteFacturado(con,consultaFacturasForm, response);
				} else if (estado.equals("detalleFacturas")) {
					consultaFacturasForm.setAnexoC("");
					return this.accionDetalleFactura(consultaFacturasForm, mapping,	con, medico);
				} else if (estado.equals("detalleFacturasTodos")) {
					return this.accionDetalleFacturaTodos(consultaFacturasForm,mapping, request, con, medico, paciente);
				} else if (estado.equals("resultadoBusqueda")) {
					return this.accionBusquedaAvanzada(consultaFacturasForm,mapping, con);
				} else if (estado.equals("cancelarProceso")) {
					UtilidadBD.closeConnection(con);
					return mapping.findForward("inicioBusqueda");
				} else if (estado.equals("redireccionTodos")) {
					this.cerrarConexion(con);
					response.sendRedirect(consultaFacturasForm.getLinkSiguiente());
					return null;
				} else if (estado.equals("redireccionPaciente")) {
					this.cerrarConexion(con);
					response.sendRedirect(consultaFacturasForm.getLinkSiguiente());
					return null;
				} else if (estado.equals("redireccionSolicitud")) {
					consultaFacturasForm.getMapaSolicitudesFactura();
					this.cerrarConexion(con);
					response.sendRedirect(consultaFacturasForm.getLinkSiguiente());
					return null;
				} else if (estado.equals("ordenarColumnaSolicitudPaciente")) {
					return this.accionOrdenarColumnaSolicitud(con,consultaFacturasForm, mapping);
				} else if (estado.equals("ordenarColumnaSolicitudTodos")) {
					return this.accionOrdenarColumnaSolicitudTodos(con,consultaFacturasForm, /* mapping, */response, request,maxPageItems);
				} else if (estado.equals("imprimir")) {
					return this.accionImprimir(mapping, request, con,consultaFacturasForm, medico);
				} else if(estado.equals("verificarImprimirDetallePaciente")){
					
					request.getSession().setAttribute("estadoRetornarFactura","detalleFactura");
					consultaFacturasForm.setNombreArchivoGenerado("");
					consultaFacturasForm.setNombreArchivoGeneradoOriginal("");
					consultaFacturasForm.setTipoImpresionPermiso(IMPRIMIR_COPIA);
					
					if(consultaFacturasForm.isTienePermisoImprimirOriginalFactura()){
						return mapping.findForward("confirmarImpresionOriginalFactura"); 
					}else{
						consultaFacturasForm.setTipoImpresionPermiso(IMPRIMIR_COPIA);
						return this.accionImprimirDetalleFacturas(mapping, request,con, consultaFacturasForm, medico, paciente,response,false);
					}
				}else if (estado.equals("imprimirDetallePaciente")) {
					
					request.getSession().setAttribute("estadoRetornarFactura","detalleFactura");
					consultaFacturasForm.setNombreArchivoGenerado("");
					consultaFacturasForm.setNombreArchivoGeneradoOriginal("");
					
					return this.accionImprimirDetalleFacturas(mapping, request,con, consultaFacturasForm, medico, paciente,response,true);
				} else if (estado.equals("imprimirTipoReporteConvenio")) {
					return this.generarReporteConvenio(con, consultaFacturasForm,medico, request, mapping);
				} else if (estado.equals("cargarTipoPaciente")) {
					this.cerrarConexion(con);
					return mapping.findForward("inicioBusqueda");
				} else if (estado.equals("imprimirFacturaOdontologica")) {

					String nombreRptDesign="FacturaSonriaPOS.rptdesign";
					String newPathReport = "";

					DesignEngineApi comp;
					comp = new DesignEngineApi(ParamsBirtApplication.getReportsPath()+ "facturacion/", nombreRptDesign);

					newPathReport = comp.saveReport1(false);
					comp.updateJDBCParameters(newPathReport);

					//Envio Parametros
					newPathReport += 	"&codigoFactura="+consultaFacturasForm.getMapaDetalleFactura().get("codfactura_0")+
					"&reimpresion="+medico.getNombreUsuario()+
					"&fechaReimpresion="+UtilidadFecha.getFechaActual(con)+" "+UtilidadFecha.getHoraActual();

					request.setAttribute("isOpenReport", "true");
					request.setAttribute("newPathReport",newPathReport);

					this.cerrarConexion(con);
					return mapping.findForward("detalleFactura");
				}

			} else {
				logger
				.error("El form no es compatible con el form de Consulta de Facturas");
				request.setAttribute("codigoDescripcionError",
				"errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		}catch (Exception e) {
			Log4JManager.error("Error: ", e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}

	/**
	 * Acción que permite el ordenamiento por cualquiera de las columnas en el
	 * listado de facturas despues de una búsqueda
	 * 
	 * @param con
	 * @param consultaFacturasForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarColumnaReporteFacturado(Connection con,
			ConsultaFacturasForm consultaFacturasForm,
			HttpServletResponse response) {
		logger.info("Mapa--->" + consultaFacturasForm.getMapaFacturasTodos());
		String[] indices = { "nombretipoconvenio_", "nombreconvenio_",
				"consecutivofactura_", "fechafactura_", "fecharadicacion_",
				"valorradicado_", "ajustescredito_", "ajustesdebito_",
				"totalradicado_" };

		int tmp = Integer.parseInt(consultaFacturasForm
				.getMapaFacturasTodos("numRegistros")
				+ "");
		consultaFacturasForm.setMapaFacturasTodos(Listado.ordenarMapa(indices,
				consultaFacturasForm.getPatronOrdenar(), consultaFacturasForm
						.getUltimoPatron(), consultaFacturasForm
						.getMapaFacturasTodos(), Integer
						.parseInt(consultaFacturasForm
								.getMapaFacturasTodos("numRegistros")
								+ "")));
		consultaFacturasForm.setUltimoPatron(consultaFacturasForm
				.getPatronOrdenar());
		consultaFacturasForm.setMapaFacturasTodos("numRegistros", tmp + "");
		this.cerrarConexion(con);
		/** return mapping.findForward("busquedaAvanzada"); **/
		return this.redireccionColumnaTodos(con, consultaFacturasForm,
				response, "listadoTipoReporteFacturado.jsp");
	}

	/**
	 * Metodo para Imprimir la Factura Detallado por solicitud
	 * 
	 * @param con
	 * @param consultaFacturasForm
	 * @param medico
	 * @param request
	 * @param mapping
	 * @param paciente
	 * @return
	 */
	private String generarReporteAnexoSolicitud(Connection con,
			ConsultaFacturasForm consultaFacturasForm, UsuarioBasico medico,
			HttpServletRequest request, ActionMapping mapping,
			PersonaBasica paciente) {
		String path = "";
		HashMap<Object, Object> mapa = new HashMap<Object, Object>();
		mapa = ConsultaFacturas.consultaDatosFactura(con, consultaFacturasForm
				.getCodigoFactura());
		path = EstadoCuenta.imprimirFacturaDetalladoSolicitud(con, request,
				medico, paciente, consultaFacturasForm.getCodigoFactura(), mapa
						.get("centroatencion_0").toString(),  mapa.get("entidadsub_0")
						.toString(), mapa.get("estadocuenta_0").toString(),
				mapa.get("tipomonto_0").toString(), mapa.get("estadoingreso_0")
						.toString(), mapa.get("natupaciente_0").toString(),
				mapa.get("nombreconvenio_0").toString(), mapa.get("clasecon_0")
						.toString(), mapa.get("subcuenta_0").toString());
		if (!path.equals(""))
			return path;
		else
			return "";
	}

	/**
	 * Metodo para Imprimir la Factura Resumido por Centro de Costo
	 * 
	 * @param con
	 * @param consultaFacturasForm
	 * @param medico
	 * @param request
	 * @param mapping
	 * @param paciente
	 * @return
	 */
	private String generarReporteAnexoCC(Connection con,
			ConsultaFacturasForm consultaFacturasForm, UsuarioBasico medico,
			HttpServletRequest request, ActionMapping mapping,
			PersonaBasica paciente) {
		String path = "";
		HashMap<Object, Object> mapa = new HashMap<Object, Object>();
		mapa = ConsultaFacturas.consultaDatosFactura(con, consultaFacturasForm
				.getCodigoFactura());
		path = EstadoCuenta.imprimirFacturaResumidoCC(con, request, medico,
				paciente, consultaFacturasForm.getCodigoFactura(), mapa.get(
						"centroatencion_0").toString(), mapa.get(
						"entidadsub_0").toString(), mapa.get("estadocuenta_0")
						.toString(), mapa.get("tipomonto_0").toString(), mapa
						.get("estadoingreso_0").toString(), mapa.get(
						"natupaciente_0").toString(), mapa.get(
						"nombreconvenio_0").toString(), mapa.get("clasecon_0")
						.toString(), mapa.get("subcuenta_0").toString());
		if (!path.equals(""))
			return path;
		else
			return "";
	}

	/**
	 * Metodo para Imprimir la Factura Detallado por Item
	 * 
	 * @param con
	 * @param consultaFacturasForm
	 * @param medico
	 * @param request
	 * @param mapping
	 * @param paciente
	 * @return
	 */
	private String generarReporteAnexoIT(Connection con,
			ConsultaFacturasForm consultaFacturasForm, UsuarioBasico medico,
			HttpServletRequest request, ActionMapping mapping,
			PersonaBasica paciente) {
		String path = "";
		HashMap<Object, Object> mapa = new HashMap<Object, Object>();
		mapa = ConsultaFacturas.consultaDatosFactura(con, consultaFacturasForm
				.getCodigoFactura());
		Utilidades.imprimirMapa(mapa);
		logger.info("===>Codigo Convenio: "
				+ mapa.get("codigoconvenio_0").toString());
		logger.info("===>Nombre Convenio: "
				+ mapa.get("nombreconvenio_0").toString());
		logger.info("===>Codigo Cuenta: " + mapa.get("cuenta_0").toString());
		logger.info("===>Codigo subCuenta: "
				+ mapa.get("subcuenta_0").toString());
		/**
		* Tipo Modificacion: Segun incidencia 6078
		* Autor: Alejandro Aguirre Luna
		* usuario: aleagulu
		* Fecha: 19/02/2013
		* Descripcion: la MT 6078 la cual utilizaba el método 
		* imprimirFacturaDetalladoIT sobrecargado con el nuevo argumento 
		* estado de factura. Ahora se generó un cambio en los DCUs, 
		* entonces ya no es necesaria la utilización del método sobrecargado. 
		**/
		path = EstadoCuenta.imprimirFacturaDetalladoIT(con, request, medico,
				paciente, consultaFacturasForm.getCodigoFactura(), mapa.get(
						"centroatencion_0").toString(), mapa.get(
						"entidadsub_0").toString(), mapa.get("estadocuenta_0")
						.toString(), mapa.get("tipomonto_0").toString(), mapa
						.get("estadoingreso_0").toString(), mapa.get(
						"natupaciente_0").toString(), mapa.get(
						"codigoconvenio_0").toString(), mapa.get(
						"nombreconvenio_0").toString(), mapa.get("clasecon_0")
						.toString(), mapa.get("subcuenta_0").toString());
		if (!path.equals(""))
			return path;
		else
			return "";
	}

	/**
	 * 
	 * @param con
	 * @param consultaFacturasForm
	 * @param medico
	 * @param request
	 * @return
	 */
	private ActionForward generarReporteConvenio(Connection con,
			ConsultaFacturasForm consultaFacturasForm, UsuarioBasico medico,
			HttpServletRequest request, ActionMapping mapping) {
		DesignEngineApi comp;
		InstitucionBasica institucionBasica = (InstitucionBasica) request
				.getSession().getAttribute("institucionBasica");
		String rutaReporte = "", tipoReporte = "";
		if (consultaFacturasForm.getTipoReporte().equals(
				ConstantesIntegridadDominio.acronimoTipoReporteConvenio)) {
			rutaReporte = "ListadoFacturasReporteConvenio.rptdesign";
			tipoReporte = "Convenio";
		} else if (consultaFacturasForm
				.getTipoReporte()
				.equals(
						ConstantesIntegridadDominio.acronimoTipoReporteFacturadoRadicado)) {
			rutaReporte = "ListadoFacturasReporteFacturadoRadicado.rptdesign";
			tipoReporte = "Facturado/Radicado";
		}

		comp = new DesignEngineApi(ParamsBirtApplication.getReportsPath()
				+ "facturacion/", rutaReporte);
		comp.insertImageHeaderOfMasterPage1(0, 0, institucionBasica
				.getLogoReportes());
		comp.insertGridHeaderOfMasterPage(0, 1, 1, 4);
		Vector v = new Vector();
		v.add(institucionBasica.getRazonSocial());
		if (Utilidades.convertirAEntero(institucionBasica
				.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
			v.add(Utilidades.getDescripcionTipoIdentificacion(con,
					institucionBasica.getTipoIdentificacion())
					+ ". "
					+ institucionBasica.getNit()
					+ " - "
					+ institucionBasica.getDigitoVerificacion());
		else
			v.add(Utilidades.getDescripcionTipoIdentificacion(con,
					institucionBasica.getTipoIdentificacion())
					+ ". " + institucionBasica.getNit());
		if (!institucionBasica.getActividadEconomica().equals(""))// si no se
																	// posee
																	// actividad
																	// economica
																	// no
																	// mostrar
																	// el campo
			v.add("Actividad Económica: "
					+ institucionBasica.getActividadEconomica());
		v.add(institucionBasica.getDireccion() + " - Tel."
				+ institucionBasica.getTelefono());
		comp.insertLabelInGridOfMasterPage(0, 1, v);
		comp.insertLabelInGridPpalOfHeader(1, 1, "FACTURAS POR RANGOS");
		comp.insertLabelInGridPpalOfHeader(1, 2, "TIPO: " + tipoReporte);
		comp.insertLabelInGridPpalOfHeader(1, 2, "PERIODO: "
				+ consultaFacturasForm.getFechaElaboracionInicial() + " al "
				+ consultaFacturasForm.getFechaElaboracionFinal());
		comp.obtenerComponentesDataSet("listadoFacturas");
		String oldQuery = comp.obtenerQueryDataSet();
		logger.info("Query original del design->" + oldQuery);
		String newQuery = oldQuery.replace("1=2", " 1=1 "
				+ ConsultaFacturas.armarWhereFiltroFacturas(
						consultaFacturasForm.getFacturaInicial(),
						consultaFacturasForm.getFacturaFinal(),
						consultaFacturasForm.getFechaElaboracionInicial(),
						consultaFacturasForm.getFechaElaboracionFinal(),
						consultaFacturasForm.getPropiedadTempI(),
						consultaFacturasForm.getPropiedadTempViaIngreso(),
						consultaFacturasForm.getTipoPaciente(),
						consultaFacturasForm.getPropiedadTempEFactura(),
						consultaFacturasForm.getPropiedadTempEPaciente(),
						consultaFacturasForm.getUsuario(), consultaFacturasForm
								.getCodigoCentroAtencion(),
						consultaFacturasForm.getEntidadSubcontratada(),
						consultaFacturasForm.getEmpresaInstitucion()));
		logger.info("Nueva Consulta->" + newQuery);
		comp.modificarQueryDataSet(newQuery);
		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		if (!newPathReport.equals("")) {
			request.setAttribute("isOpenReport", "true");
			request.setAttribute("newPathReport", newPathReport);
		}
		comp.updateJDBCParameters(newPathReport);

		UtilidadBD.closeConnection(con);
		if (consultaFacturasForm.getTipoReporte().equals(
				ConstantesIntegridadDominio.acronimoTipoReporteConvenio))
			return mapping.findForward("listadoTipoReporteConvenio");
		else if (consultaFacturasForm
				.getTipoReporte()
				.equals(
						ConstantesIntegridadDominio.acronimoTipoReporteFacturadoRadicado))
			return mapping.findForward("listadoTipoReporteFacturado");
		else
			return null;
	}

	/**
	 * Metodo para realizar la impresión de la factura po la opción de pacientes
	 * 
	 * @param mapping
	 * @param request
	 * @param con
	 * @param consultaFacturasForm
	 * @param medico
	 * @return ActionForward
	 * @author jarloc
	 * @param tipoImpresionPermiso 
	 * @throws IPSException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionImprimirDetalleFacturas(ActionMapping mapping,
			HttpServletRequest request, Connection con,
			ConsultaFacturasForm consultaFacturasForm, UsuarioBasico usuario,
			PersonaBasica paciente,HttpServletResponse response, boolean tipoImpresionPermiso) throws IPSException {
		
		if(consultaFacturasForm.getTipoImpresionPermiso()==IMPRIMIR_COPIA){
			request.getSession().setAttribute("piePaginaImpresion","copia" );
		}else{
			if(consultaFacturasForm.getTipoImpresionPermiso()==IMPRIMIR_ORIGINAL){
				request.getSession().setAttribute("piePaginaImpresion","original" );
			}else{
				request.getSession().setAttribute("piePaginaImpresion","copia" );
			}
		}
		
		logger.info("ANEXO DE CUENTA>>>>>>>>>>>>>>>"
				+ consultaFacturasForm.getAnexoC());
		String path = "";
		if (!consultaFacturasForm.getAnexoC().equals("")) {
			if (consultaFacturasForm.getAnexoC().equals("sol"))
				path = generarReporteAnexoSolicitud(con, consultaFacturasForm,
						usuario, request, mapping, paciente);
			if (consultaFacturasForm.getAnexoC().equals("cen"))
				path = generarReporteAnexoCC(con, consultaFacturasForm,
						usuario, request, mapping, paciente);
			if (consultaFacturasForm.getAnexoC().equals("ite"))
				path = generarReporteAnexoIT(con, consultaFacturasForm,
						usuario, request, mapping, paciente);
		}
		// Vector nombresArchivos=new Vector();
		HashMap mapaImprimir = new HashMap();
		// Utilidades.obtenerCodigoFactura(Integer.parseInt(consultaFacturasForm.getMapaDetalleFactura("consecutivo_0")+""),usuario.getCodigoInstitucionInt(),
		// Double.parseDouble(consultaFacturasForm.getMapaDetalleFactura("empresaInstitucion_0")))+"";
		mapaImprimir.put("codigoFactura_0", consultaFacturasForm.getCodigoFactura()+ "");
		mapaImprimir.put("empresaInstitucion_0", consultaFacturasForm.getEmpresaInstitucion()+ "");
		mapaImprimir.put("nroAutorizacion_0", consultaFacturasForm.getNumeroAutorizacion());
		
		//MT 6082: Se llama el prefijo de la factura para impresión
		mapaImprimir.put("prefijoFactura_0",consultaFacturasForm.getMapaDetalleFactura("prefijoFactura_0") == null ? "" : consultaFacturasForm.getMapaDetalleFactura("prefijoFactura_0"));
		// Fin MT
		
		
		

		if (!System.getProperty("PAIS").toString().toUpperCase().equals("VENEZUELA")) 
			mapaImprimir.put("formatoImpresion_0", Factura.obtenerFormatoFacturaXCodigoFact(con, consultaFacturasForm.getCodigoFactura()+ ""));
		else
			mapaImprimir.put("formatoImpresion_0",ConstantesBD.codigoFormatoImpresionVenezuela);

		logger.info("\n\n\nVALOR DE FORMATO IMPRESION>>>>>>>>>>>>>>>>>>>"+ mapaImprimir.get("formatoImpresion_0") + "\n\n\n");
		logger.info("\n\n\nIMPRIMIR ANEXO ????????>>>>>>>"+ consultaFacturasForm.isImprimirAnexo() + "\n\n\n");

		mapaImprimir.put("tipoAnexo_0", consultaFacturasForm.getTipoAnexo());
		mapaImprimir.put("tipoimpresion_0", consultaFacturasForm.getTipoImpresion());
		mapaImprimir.put("tipoanexo_0", path);
		// Se incluyo para el manejo de los formatos de impresion establecidos
		// para Venezuela
		logger.info("===>Formato Impresion Venezuela: "	+ consultaFacturasForm.getFormatoImpresionVenezuela());
		mapaImprimir.put("formatoImpresionVenezuela_0", consultaFacturasForm.getFormatoImpresionVenezuela());
		mapaImprimir.put("numRegistros", "1");

		logger.info("\n\n\n****************************CONSULTA IMPRESION FACTURAS*********************************");
		Utilidades.imprimirMapa(mapaImprimir);

		consultaFacturasForm.setAnexoC("");
		consultaFacturasForm.setTipoImpresion("");
		consultaFacturasForm.setImprimirAnexo(false);
		consultaFacturasForm.setTipoAnexo("");


		FacturacionPdf.nombreReporteAgrupado="";
		ActionForward forward=null;
		
		
		HashMap archivosGeneradosBirt=new HashMap(0);
		archivosGeneradosBirt.put("numRegistros", "0");
		request.setAttribute("archivosBirt",archivosGeneradosBirt);
		request.setAttribute("archivos", new Vector());
		
		if(consultaFacturasForm.getTipoImpresionPermiso()==IMPRIMIR_COPIA||consultaFacturasForm.getTipoImpresionPermiso()==IMPRIMIR_AMBOS){
			request.getSession().setAttribute("piePaginaImpresion","copia" );
			
			forward= FacturacionPdf.imprimirFacturas(paciente, usuario, mapaImprimir,	con, request, "Consulta Facturas", mapping, "detalleFactura",response);
			consultaFacturasForm.setNombreArchivoGenerado(FacturacionPdf.nombreReporteAgrupado);
		}
		//Crear factura Original
		FacturacionPdf.nombreReporteAgrupado="";
		
		if(consultaFacturasForm.getTipoImpresionPermiso()==IMPRIMIR_ORIGINAL||consultaFacturasForm.getTipoImpresionPermiso()==IMPRIMIR_AMBOS){
			request.getSession().setAttribute("piePaginaImpresion","original" );
			Connection connection=null;
			connection = openDBConnection(connection);
			forward=FacturacionPdf.imprimirFacturas(paciente, usuario, mapaImprimir,	connection, request, "Consulta Original Facturas", mapping, "detalleFactura",response);
			consultaFacturasForm.setNombreArchivoGeneradoOriginal(FacturacionPdf.nombreReporteAgrupado);
			
			UtilidadBD.closeConnection(connection);
		}
		return forward;
		
		

	}

	/**
	 * Acción de empezar con el flujo de Paciente
	 * 
	 * @param mapping
	 * @param request
	 * @param consultaFacturasForm
	 * @param con
	 * @param paciente
	 * @param consultaFacturas
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEmpezarPaciente(ActionMapping mapping,
			HttpServletRequest request,
			ConsultaFacturasForm consultaFacturasForm, Connection con,
			PersonaBasica paciente, ConsultaFacturas consultaFacturas,
			UsuarioBasico user) throws Exception {
		consultaFacturasForm.reset();
		consultaFacturasForm.setCodigoCentroAtencion(user
				.getCodigoCentroAtencion());
		consultaFacturasForm.setMapaFacturasPaciente(consultaFacturas
				.cargarFacturasPaciente(con, paciente.getCodigoPersona()));
		if (Integer.parseInt(consultaFacturasForm.getMapaFacturasPaciente(
				"numRegistros").toString()) < 0) {
			this.cerrarConexion(con);
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Paciente sin Facturas");
			request.setAttribute("codigoDescripcionError",
					"error.facturacion.pacienteSinFacturas");
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");
		}

		this.cerrarConexion(con);
		return mapping.findForward("facturasPaciente");
	}

	/**
	 * Acción que permite el ordenamiento por cualquiera de las columnas en el
	 * listado de facturas de una paciente
	 * 
	 * @param con
	 * @param consultaFacturasForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarColumnaPaciente(Connection con,
			ConsultaFacturasForm consultaFacturasForm,
			HttpServletResponse response) {
		String[] indices = { "factura_", "consecutivo_", "valorTotal_",
				"fechaHoraElaboracion_", "viaIngreso_","codigoIngreso_", "responsable_",
				"estadoFactura_", "estadoPaciente_", "nombreCentroAtencion_",
				"entidadsubcontratada_", "descentidadsubcontratada_",
				"empresainstitucion_", "descempresainstitucion_" };
		int tmp = Integer.parseInt(consultaFacturasForm
				.getMapaFacturasPaciente("numRegistros")
				+ "");
		consultaFacturasForm.setMapaFacturasPaciente(Listado.ordenarMapa(
				indices, consultaFacturasForm.getPatronOrdenar(),
				consultaFacturasForm.getUltimoPatron(), consultaFacturasForm
						.getMapaFacturasPaciente(), Integer
						.parseInt(consultaFacturasForm
								.getMapaFacturasPaciente("numRegistros")
								+ "")));
		consultaFacturasForm.setUltimoPatron(consultaFacturasForm
				.getPatronOrdenar());
		consultaFacturasForm.setMapaFacturasPaciente("numRegistros", tmp + "");
		this.cerrarConexion(con);
		// return mapping.findForward("facturasPaciente");
		return this.redireccionColumnaPaciente(con, consultaFacturasForm,
				response, "facturasPaciente.jsp");
	}

	/**
	 * Acción que permite el ordenamiento por cualquiera de las columnas en el
	 * listado de facturas despues de una búsqueda
	 * 
	 * @param con
	 * @param consultaFacturasForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarColumnaTodos(Connection con,
			ConsultaFacturasForm consultaFacturasForm,
			HttpServletResponse response) {
		logger.info("Mapa--->" + consultaFacturasForm.getMapaFacturasTodos());
		String[] indices = { "factura_", "consecutivo_", "valorTotal_",
				"fechaHoraElaboracion_", "paciente_", "tipoIdentificacion_",
				"numIdentificacion_", "viaIngreso_","codigoIngreso_", "responsable_",
				"estadoFactura_", "estadoPaciente_", "codigoEstadoFactura_",
				"codPaciente_", "usuario_", "valorConvenio_", "valorPaciente_",
				"nombreCentroAtencion_", "entidadsubcontratada_",
				"descentidadsubcontratada_", "empresainstitucion_",
				"descempresainstitucion_" };

		int tmp = Integer.parseInt(consultaFacturasForm
				.getMapaFacturasTodos("numRegistros")
				+ "");
		consultaFacturasForm.setMapaFacturasTodos(Listado.ordenarMapa(indices,
				consultaFacturasForm.getPatronOrdenar(), consultaFacturasForm
						.getUltimoPatron(), consultaFacturasForm
						.getMapaFacturasTodos(), Integer
						.parseInt(consultaFacturasForm
								.getMapaFacturasTodos("numRegistros")
								+ "")));
		consultaFacturasForm.setUltimoPatron(consultaFacturasForm
				.getPatronOrdenar());
		consultaFacturasForm.setMapaFacturasTodos("numRegistros", tmp + "");
		this.cerrarConexion(con);
		/** return mapping.findForward("busquedaAvanzada"); **/
		return this.redireccionColumnaTodos(con, consultaFacturasForm,
				response, "facturasTodos.jsp");
	}

	/**
	 * Acción que permite el ordenamiento por cualquiera de las columnas en el
	 * listado de facturas despues de una búsqueda
	 * 
	 * @param con
	 * @param consultaFacturasForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarColumnaReporteConvenio(Connection con,
			ConsultaFacturasForm consultaFacturasForm,
			HttpServletResponse response) {
		logger.info("Mapa--->" + consultaFacturasForm.getMapaFacturasTodos());
		String[] indices = { "nombretipoconvenio_", "nombreconvenio_",
				"consecutivofactura_", "fechafactura_", "valorfactura_",
				"ajustescredito_", "ajustesdebito_", "totalfacturado_" };

		int tmp = Integer.parseInt(consultaFacturasForm
				.getMapaFacturasTodos("numRegistros")
				+ "");
		consultaFacturasForm.setMapaFacturasTodos(Listado.ordenarMapa(indices,
				consultaFacturasForm.getPatronOrdenar(), consultaFacturasForm
						.getUltimoPatron(), consultaFacturasForm
						.getMapaFacturasTodos(), Integer
						.parseInt(consultaFacturasForm
								.getMapaFacturasTodos("numRegistros")
								+ "")));
		consultaFacturasForm.setUltimoPatron(consultaFacturasForm
				.getPatronOrdenar());
		consultaFacturasForm.setMapaFacturasTodos("numRegistros", tmp + "");
		this.cerrarConexion(con);
		/** return mapping.findForward("busquedaAvanzada"); **/
		return this.redireccionColumnaTodos(con, consultaFacturasForm,
				response, "listadoTipoReporteConvenio.jsp");
	}

	/**
	 * Permite el ordenamiento de las columnas de las solicitudes asociadas a
	 * una factura
	 * 
	 * @param con
	 * @param consultaFacturasForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarColumnaSolicitud(Connection con,
			ConsultaFacturasForm consultaFacturasForm, ActionMapping mapping) {
		String[] colums = {};

		String[] indices = { "area_", "codigo_", "codigopropietario_",
				"descripcionServicio_", "cantidad_", "valUnitario_",
				"valTotal_", "codigodetallefactura_", "numero_solicitud_",
				"es_qx_", "es_material_especial_" };

		int tmp = Integer.parseInt(consultaFacturasForm
				.getMapaSolicitudesFactura("numRegistros")
				+ "");
		consultaFacturasForm.setMapaSolicitudesFactura((Listado.ordenarMapa(
				indices, consultaFacturasForm.getPatronOrdenar(),
				consultaFacturasForm.getUltimoPatron(), consultaFacturasForm
						.getMapaSolicitudesFactura(), Integer
						.parseInt(consultaFacturasForm
								.getMapaSolicitudesFactura("numRegistros")
								+ ""))));
		consultaFacturasForm.setUltimoPatron(consultaFacturasForm
				.getPatronOrdenar());
		consultaFacturasForm
				.setMapaSolicitudesFactura("numRegistros", tmp + "");
		this.cerrarConexion(con);
		return mapping.findForward("detalleFactura");
	}

	/**
	 * Permite el ordenamiento de las columnas de las solicitudes asociadas a
	 * una factura
	 * 
	 * @param con
	 * @param consultaFacturasForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarColumnaSolicitudTodos(Connection con,
			ConsultaFacturasForm consultaFacturasForm,/* ActionMapping mapping, */
			HttpServletResponse response, HttpServletRequest request,
			int maxPageItems) {
		String[] indices = { "area_", "codigo_", "codigopropietario_",
				"descripcionServicio_", "cantidad_", "valUnitario_",
				"valTotal_", "codigodetallefactura_", "numero_solicitud_",
				"es_qx_", "es_material_especial_" };
		int tmp = Integer.parseInt(consultaFacturasForm
				.getMapaSolicitudesFactura("numRegistros")
				+ "");
		consultaFacturasForm.setMapaSolicitudesFactura((Listado.ordenarMapa(
				indices, consultaFacturasForm.getPatronOrdenar(),
				consultaFacturasForm.getUltimoPatron(), consultaFacturasForm
						.getMapaSolicitudesFactura(), Integer
						.parseInt(consultaFacturasForm
								.getMapaSolicitudesFactura("numRegistros")
								+ ""))));
		consultaFacturasForm.setUltimoPatron(consultaFacturasForm
				.getPatronOrdenar());
		consultaFacturasForm
				.setMapaSolicitudesFactura("numRegistros", tmp + "");
		this.cerrarConexion(con);
		// return mapping.findForward("detalleFactura");
		return this.redireccion(con, consultaFacturasForm, response, request,
				"detalleFactura.jsp", maxPageItems);
	}

	/**
	 * Accion para ver el detalle de una factura
	 * 
	 * @param consultaFacturasForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionDetalleFactura(ConsultaFacturasForm consultaFacturasForm, ActionMapping mapping,Connection con, UsuarioBasico medico) throws SQLException {
		int posicion = consultaFacturasForm.getPosicionMapa();
		
		if(consultaFacturasForm.getMapaDetalleFactura().get("cuenta_"+posicion)==null)
		{
			posicion =0;
		}
		ConsultaFacturas mundo = new ConsultaFacturas();
		mundo.setConsecutivoFactura((Integer.parseInt(consultaFacturasForm.getMapaFacturasPaciente("consecutivo_" + posicion)+ "")));
		mundo.setInstitucion(medico.getCodigoInstitucionInt());
		

		logger.info("\n\n FACTURA ID=----->"+ consultaFacturasForm.getCodigoFactura()+ " EMPRESA INSTITUCION="+ consultaFacturasForm.getEmpresaInstitucion() + "  \n\n");

		mundo.setIdFactura(Double.parseDouble(consultaFacturasForm.getCodigoFactura()));

		consultaFacturasForm.setMapaDetalleFactura(mundo.cargarDetalleFactura(con, Utilidades.convertirADouble(consultaFacturasForm.getCodigoFactura())));
		consultaFacturasForm.setNumeroAutorizacion(ConsultaFacturas.consultarNumeroAutorizacion(Utilidades.convertirAEntero(consultaFacturasForm.getMapaDetalleFactura().get("cuenta_"+posicion)+""),Utilidades.convertirAEntero(consultaFacturasForm.getMapaDetalleFactura().get("subcuenta_"+posicion)+""))+"");
		
		logger.info("\n\n NUMERO AUTORIZACION >> "+consultaFacturasForm.getNumeroAutorizacion());
		// --Se carga el listado de recibos de caja ...
		consultaFacturasForm.setListaRecibos(mundo.cargarListadoRecibosCaja(con, Utilidades.convertirADouble(consultaFacturasForm.getCodigoFactura())));
        
		
		/**Control de cambio Anexo de la cuenta (3463)
		 * desarrollador: leoquico
		 * fecha: 23-Abril-2013
		 * */
		
		// 1. Consultar parametrizacion Convenios tipo de articulo
		String convenio = UtilidadesFacturacion.consultarCodigoConvenio(con,consultaFacturasForm.getCodigoFactura());
		int codigoInstitucion = medico.getCodigoInstitucionInt();
		int tipoConvenioArticulo = UtilidadesFacturacion.consultarTipoConvenioArticulo(con, convenio);
		String tipoArticulo = "";

		// 2. realizar consulta tipo convenio articulo
		if (tipoConvenioArticulo >= 0) {
			if (tipoConvenioArticulo == 0) {
				// flujo tipo codigo CUM
				tipoArticulo = "CUM";
			}
			if (tipoConvenioArticulo == 1) {
				// flujo normal
				tipoArticulo = "AXM";
			}
		} else {
			// 3. Valiadar parametrizacion por el codigo manual estandar
			String codigoManualArticulo = ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulosLargo((codigoInstitucion));
			String[] codigoManualAbv = codigoManualArticulo.split("@@");
			codigoManualArticulo = codigoManualAbv[0];
			if (codigoManualArticulo.equals("INZ")) {
				// mostrar codigo interfaz si no existe mostrar axioma
				tipoArticulo = "INZ";
			}
			if (codigoManualArticulo.equals("AXM")) {
				// flujo normal
				tipoArticulo = "AXM";
			}
		}
	    //validacion para servicios 
		int tipoConvenioServicio = UtilidadesFacturacion.consultarTipoConvenioServicio(con, convenio);
		int tipoServicio = ConstantesBD.codigoNuncaValido;
		if (tipoConvenioServicio >= 0) {
			tipoServicio = tipoConvenioServicio;
		} else {
			String codigoManualServicio = ValoresPorDefecto.getCodigoManualEstandarBusquedaServiciosLargo(codigoInstitucion);
			String[] codigoManualAbv = codigoManualServicio.split("@@");
			if (codigoManualAbv.length > 0) {
				codigoManualServicio = codigoManualAbv[0];
				tipoServicio = Integer.parseInt(codigoManualServicio);
			}
	    }
		consultaFacturasForm.setMapaSolicitudesFactura(mundo.cargarSolicitudesFactura(con, Utilidades.convertirADouble(consultaFacturasForm.getCodigoFactura()),tipoArticulo, tipoServicio));
		consultaFacturasForm.setFormatoConvenio(Factura.obtenerFormatoFacturaXCodigoFact(con, consultaFacturasForm.getCodigoFactura()+ "")+ "");
		this.cerrarConexion(con);
		return mapping.findForward("detalleFactura");
	}

	/**
	 * Accion para ver el detalle de una factura
	 * 
	 * @param consultaFacturasForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionDetalleFacturaTodos(
			ConsultaFacturasForm consultaFacturasForm, ActionMapping mapping,
			HttpServletRequest request, Connection con, 
			UsuarioBasico medico, PersonaBasica paciente)
			throws SQLException {
		int posicion = consultaFacturasForm.getPosicionMapa();
		int posicionPaciente = consultaFacturasForm.getPosicionMapa();
		/*
		 *  No se si en algún lugar se trae un mapa completo cion muchos registros
		 *  para el caso de prueba solamente se trae un registro, por lo tanto
		 *  la posición siempres será 0
		 */
		if(consultaFacturasForm.getMapaDetalleFactura().get("cuenta_"+posicion)==null)
		{
			posicion=0;
		}
		ConsultaFacturas mundo = new ConsultaFacturas();
		mundo.setConsecutivoFactura((Integer.parseInt(consultaFacturasForm
				.getMapaFacturasTodos("consecutivo_" + posicion)
				+ "")));
		mundo.setInstitucion(medico.getCodigoInstitucionInt());

		logger.info("\n\n EMPRESA INSTITUCION-->"+ consultaFacturasForm.getEmpresaInstitucion()+ " CODIGO FACTURA->" + consultaFacturasForm.getCodigoFactura()+ "\n\n");

		mundo.setIdFactura(Double.parseDouble(consultaFacturasForm.getCodigoFactura()));
		consultaFacturasForm.setMapaDetalleFactura(mundo.cargarDetalleFactura(con, Double.parseDouble(consultaFacturasForm.getCodigoFactura())));
		Utilidades.imprimirMapa(consultaFacturasForm.getMapaDetalleFactura());
		consultaFacturasForm.setNumeroAutorizacion(ConsultaFacturas.consultarNumeroAutorizacion(Utilidades.convertirAEntero(consultaFacturasForm.getMapaDetalleFactura().get("cuenta_"+posicion).toString()),Utilidades.convertirAEntero(consultaFacturasForm.getMapaDetalleFactura().get("subcuenta_"+posicion).toString()))+"");
						
		/**Control de cambio Anexo de la cuenta (3463)
		 * desarrollador: leoquico
		 * fecha: 23-Abril-2013
		 * */
		
		// 1. Consultar parametrizacion Convenios tipo de articulo
		String convenio = UtilidadesFacturacion.consultarCodigoConvenio(con,consultaFacturasForm.getCodigoFactura());
		InstitucionBasica institucion = (InstitucionBasica) request.getSession().getAttribute("institucionBasica");
		int tipoConvenioArticulo = UtilidadesFacturacion.consultarTipoConvenioArticulo(con, convenio);
		String tipoArticulo = "";
		// consultaFacturasForm.setTipoCodigoArticulo(tipoConvenioArticulo);

		// 2. realizar consulta tipo convenio articulo
		if (tipoConvenioArticulo >= 0) {
			if (tipoConvenioArticulo == 0) {
				// flujo tipo codigo CUM
				tipoArticulo = "CUM";
			}
			if (tipoConvenioArticulo == 1) {
				// flujo normal
				tipoArticulo = "AXM";
			}
		}
		 else
		 {
			// 3. Valiadar parametrizacion por el codigo manual estandar
			String codigoManualArticulo = ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulosLargo((institucion.getCodigoInstitucionBasica()));
			String[] codigoManualAbv = codigoManualArticulo.split("@@");
			codigoManualArticulo = codigoManualAbv[0];
			if (codigoManualArticulo.equals("INZ")) {
				// mostrar codigo interfaz si no existe mostrar axioma
				tipoArticulo = "INZ";
			}
			if (codigoManualArticulo.equals("AXM")) {
				// flujo normal
				tipoArticulo = "AXM";
			}
		}
		// validacion para servicios
		int tipoConvenioServicio = UtilidadesFacturacion.consultarTipoConvenioServicio(con, convenio);
		int tipoServicio = ConstantesBD.codigoNuncaValido;
		if (tipoConvenioServicio > 0) {
			tipoServicio = tipoConvenioServicio;
		} else {
			String codigoManualServicio = ValoresPorDefecto.getCodigoManualEstandarBusquedaServiciosLargo(institucion.getCodigoInstitucionBasica());
			String[] codigoManualAbv = codigoManualServicio.split("@@");
			if (codigoManualAbv.length > 0) {
				codigoManualServicio = codigoManualAbv[0];
				tipoServicio = Integer.parseInt(codigoManualServicio);
			}
	    }
	    consultaFacturasForm.setMapaSolicitudesFactura(mundo
				.cargarSolicitudesFactura(con, Double
						.parseDouble(consultaFacturasForm
								.getCodigoFactura()), tipoArticulo, tipoServicio));
	    
	    /** para cargar el paciente que corresponda a la factura **/
		paciente.setCodigoPersona((Integer.parseInt(consultaFacturasForm.getMapaFacturasTodos("codPaciente_" + posicionPaciente)+ "")));
		paciente.cargar(con, (Integer.parseInt(consultaFacturasForm.getMapaFacturasTodos("codPaciente_" + posicionPaciente)	+ "")));
		paciente.cargarPaciente(con, (Integer.parseInt(consultaFacturasForm.getMapaFacturasTodos("codPaciente_" + posicionPaciente)	+ "")), medico.getCodigoInstitucion(), medico.getCodigoCentroAtencion()+ "");
		consultaFacturasForm.setFormatoConvenio(Factura.obtenerFormatoFacturaXCodigoFact(con, consultaFacturasForm.getCodigoFactura()+ "")+ "");
		this.cerrarConexion(con);
		return mapping.findForward("detalleFactura");
	}

	/**
	 * Búsqueda De Facturas por el Flujo de TODOS
	 * 
	 * @param consultaFacturasForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(
			ConsultaFacturasForm consultaFacturasForm, ActionMapping mapping,
			Connection con) throws SQLException {
		if (consultaFacturasForm.getTipoSalida().equals(
				ConstantesIntegridadDominio.acronimoTipoSalidaArchivo)) {
			if (consultaFacturasForm.getTipoReporte().equals(
					ConstantesIntegridadDominio.acronimoTipoReporteConvenio)) {
				consultaFacturasForm
						.setMapaFacturasTodos(ConsultaFacturas
								.cargarListadoConvenio(con,
										consultaFacturasForm
												.getFacturaInicial(),
										consultaFacturasForm.getFacturaFinal(),
										consultaFacturasForm
												.getFechaElaboracionInicial(),
										consultaFacturasForm
												.getFechaElaboracionFinal(),
										consultaFacturasForm
												.getPropiedadTempI(),
										consultaFacturasForm
												.getPropiedadTempViaIngreso(),
										consultaFacturasForm.getTipoPaciente(),
										consultaFacturasForm
												.getPropiedadTempEFactura(),
										consultaFacturasForm
												.getPropiedadTempEPaciente(),
										consultaFacturasForm.getUsuario(),
										consultaFacturasForm
												.getCodigoCentroAtencion(),
										consultaFacturasForm
												.getEntidadSubcontratada(),
										consultaFacturasForm
												.getEmpresaInstitucion()));
				this.generarArchivoPlano(con, consultaFacturasForm);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("inicioBusqueda");
			} else if (consultaFacturasForm
					.getTipoReporte()
					.equals(
							ConstantesIntegridadDominio.acronimoTipoReporteConvenioPaciente)) {
				ConsultaFacturas mundo = new ConsultaFacturas();
				consultaFacturasForm
						.setMapaFacturasTodos(mundo
								.busquedaFacturasTodos(con,
										consultaFacturasForm
												.getFacturaInicial(),
										consultaFacturasForm.getFacturaFinal(),
										consultaFacturasForm
												.getFechaElaboracionInicial(),
										consultaFacturasForm
												.getFechaElaboracionFinal(),
										consultaFacturasForm
												.getPropiedadTempI(),
										consultaFacturasForm
												.getPropiedadTempViaIngreso(),
										consultaFacturasForm.getTipoPaciente(),
										consultaFacturasForm
												.getPropiedadTempEFactura(),
										consultaFacturasForm
												.getPropiedadTempEPaciente(),
										consultaFacturasForm.getUsuario(),
										consultaFacturasForm
												.getCodigoCentroAtencion(),
										consultaFacturasForm
												.getEntidadSubcontratada(),
										consultaFacturasForm
												.getEmpresaInstitucion()));
				this.generarArchivoPlano(con, consultaFacturasForm);
				this.cerrarConexion(con);
				return mapping.findForward("inicioBusqueda");
			} else if (consultaFacturasForm
					.getTipoReporte()
					.equals(
							ConstantesIntegridadDominio.acronimoTipoReporteFacturadoRadicado)) {
				consultaFacturasForm
						.setMapaFacturasTodos(ConsultaFacturas
								.cargarListadoFacturadoRadicado(con,
										consultaFacturasForm
												.getFacturaInicial(),
										consultaFacturasForm.getFacturaFinal(),
										consultaFacturasForm
												.getFechaElaboracionInicial(),
										consultaFacturasForm
												.getFechaElaboracionFinal(),
										consultaFacturasForm
												.getPropiedadTempI(),
										consultaFacturasForm
												.getPropiedadTempViaIngreso(),
										consultaFacturasForm.getTipoPaciente(),
										consultaFacturasForm
												.getPropiedadTempEFactura(),
										consultaFacturasForm
												.getPropiedadTempEPaciente(),
										consultaFacturasForm.getUsuario(),
										consultaFacturasForm
												.getCodigoCentroAtencion(),
										consultaFacturasForm
												.getEntidadSubcontratada(),
										consultaFacturasForm
												.getEmpresaInstitucion()));
				this.generarArchivoPlano(con, consultaFacturasForm);
				this.cerrarConexion(con);
				return mapping.findForward("inicioBusqueda");
			}

			this.cerrarConexion(con);
			return null;
		} else {

			if (consultaFacturasForm.getTipoReporte().equals(
					ConstantesIntegridadDominio.acronimoTipoReporteConvenio)) {
				consultaFacturasForm
						.setMapaFacturasTodos(ConsultaFacturas
								.cargarListadoConvenio(con,
										consultaFacturasForm
												.getFacturaInicial(),
										consultaFacturasForm.getFacturaFinal(),
										consultaFacturasForm
												.getFechaElaboracionInicial(),
										consultaFacturasForm
												.getFechaElaboracionFinal(),
										consultaFacturasForm
												.getPropiedadTempI(),
										consultaFacturasForm
												.getPropiedadTempViaIngreso(),
										consultaFacturasForm.getTipoPaciente(),
										consultaFacturasForm
												.getPropiedadTempEFactura(),
										consultaFacturasForm
												.getPropiedadTempEPaciente(),
										consultaFacturasForm.getUsuario(),
										consultaFacturasForm
												.getCodigoCentroAtencion(),
										consultaFacturasForm
												.getEntidadSubcontratada(),
										consultaFacturasForm
												.getEmpresaInstitucion()));
				UtilidadBD.closeConnection(con);
				return mapping.findForward("listadoTipoReporteConvenio");
			} else if (consultaFacturasForm
					.getTipoReporte()
					.equals(
							ConstantesIntegridadDominio.acronimoTipoReporteConvenioPaciente)) {
				ConsultaFacturas mundo = new ConsultaFacturas();
				consultaFacturasForm
						.setMapaFacturasTodos(mundo
								.busquedaFacturasTodos(con,
										consultaFacturasForm
												.getFacturaInicial(),
										consultaFacturasForm.getFacturaFinal(),
										consultaFacturasForm
												.getFechaElaboracionInicial(),
										consultaFacturasForm
												.getFechaElaboracionFinal(),
										consultaFacturasForm
												.getPropiedadTempI(),
										consultaFacturasForm
												.getPropiedadTempViaIngreso(),
										consultaFacturasForm.getTipoPaciente(),
										consultaFacturasForm
												.getPropiedadTempEFactura(),
										consultaFacturasForm
												.getPropiedadTempEPaciente(),
										consultaFacturasForm.getUsuario(),
										consultaFacturasForm
												.getCodigoCentroAtencion(),
										consultaFacturasForm
												.getEntidadSubcontratada(),
										consultaFacturasForm
												.getEmpresaInstitucion()));
				this.cerrarConexion(con);
				return mapping.findForward("busquedaAvanzada");
			} else if (consultaFacturasForm
					.getTipoReporte()
					.equals(
							ConstantesIntegridadDominio.acronimoTipoReporteFacturadoRadicado)) {
				consultaFacturasForm
						.setMapaFacturasTodos(ConsultaFacturas
								.cargarListadoFacturadoRadicado(con,
										consultaFacturasForm
												.getFacturaInicial(),
										consultaFacturasForm.getFacturaFinal(),
										consultaFacturasForm
												.getFechaElaboracionInicial(),
										consultaFacturasForm
												.getFechaElaboracionFinal(),
										consultaFacturasForm
												.getPropiedadTempI(),
										consultaFacturasForm
												.getPropiedadTempViaIngreso(),
										consultaFacturasForm.getTipoPaciente(),
										consultaFacturasForm
												.getPropiedadTempEFactura(),
										consultaFacturasForm
												.getPropiedadTempEPaciente(),
										consultaFacturasForm.getUsuario(),
										consultaFacturasForm
												.getCodigoCentroAtencion(),
										consultaFacturasForm
												.getEntidadSubcontratada(),
										consultaFacturasForm
												.getEmpresaInstitucion()));
				UtilidadBD.closeConnection(con);
				return mapping.findForward("listadoTipoReporteFacturado");
			}

			this.cerrarConexion(con);
			return null;
		}
	}

	/**
	 * Metodo encargado de generar el archivo plano en caso de que seleccionen
	 * tipo de salida "Archivo Plano"
	 * 
	 * @param con
	 * @param consultaFacturasForm
	 */
	private void generarArchivoPlano(Connection con, ConsultaFacturasForm forma) {
		String nomRep = "Consulta Facturas por Rangos", tipoReporte = "", nombre = "", encabezado = "", exTxt = ".txt", exZip = ".zip", periodo = "";
		ConsultaFacturas mundo = new ConsultaFacturas();
		StringBuffer datosArchivos = new StringBuffer();
		periodo = UtilidadFecha.conversionFormatoFechaABD(forma
				.getFechaElaboracionInicial())
				+ "-"
				+ UtilidadFecha.conversionFormatoFechaABD(forma
						.getFechaElaboracionFinal());

		if (forma
				.getTipoReporte()
				.equals(
						ConstantesIntegridadDominio.acronimoTipoReporteConvenioPaciente)) {
			tipoReporte = "Convenio/Paciente";
			encabezado = "Factura, Entidad Subcontratada, Fecha Elaboración, Usuario Elabora, Vía Ingreso, Nro. Ingreso, Paciente, Tipo ID, No ID, Responsable, Centro Atención, Estado Factura, Estado Paciente, Valor Total, Valor Convenio, Valor Paciente";
			nombre = util.TxtFile.armarNombreArchivoConPeriodo(
					"consultaFacturasXRangos", forma.getTipoReporte(), periodo);
			datosArchivos = mundo.cargarMapaConvenioPaciente(forma
					.getMapaFacturasTodos(), nomRep, tipoReporte, periodo,
					encabezado);
		}
		if (forma.getTipoReporte().equals(
				ConstantesIntegridadDominio.acronimoTipoReporteConvenio)) {
			tipoReporte = "Convenio";
			encabezado = "Tipo Conv., Convenio, Factura, Fecha Fact., Valor Factura, Ajus. Deb., Ajus. Cred., Total Facturado";
			nombre = util.TxtFile.armarNombreArchivoConPeriodo(
					"consultaFacturasXRangos", forma.getTipoReporte(), periodo);
			datosArchivos = mundo.cargarMapaConvenio(forma
					.getMapaFacturasTodos(), nomRep, tipoReporte, periodo,
					encabezado);
		}
		if (forma
				.getTipoReporte()
				.equals(
						ConstantesIntegridadDominio.acronimoTipoReporteFacturadoRadicado)) {
			tipoReporte = "Facturado/Radicado";
			encabezado = "Tipo Conv., Convenio, Factura, Fecha Fact., Fecha Rad., Valor Radicado, Ajus. Deb., Ajus. Cred., Total Radicado";
			nombre = util.TxtFile.armarNombreArchivoConPeriodo(
					"consultaFacturasXRangos", forma.getTipoReporte(), periodo);
			datosArchivos = mundo.cargarMapaConvenioFacturado(forma
					.getMapaFacturasTodos(), nomRep, tipoReporte, periodo,
					encabezado);
		}

		String path = ValoresPorDefecto.getReportPath() + "facturacion/";
		if (Utilidades.convertirAEntero(forma
				.getMapaFacturasTodos("numRegistros")
				+ "") > 0) {
			boolean archivo = util.TxtFile.generarTxt(datosArchivos, nombre,
					path, exTxt);
			if (archivo) {
				if (BackUpBaseDatos.EjecutarComandoSO("zip -j " + path + nombre
						+ exZip + " " + path + nombre + exTxt) != ConstantesBD.codigoNuncaValido) {
					forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()
							+ "facturacion/" + nombre + exZip);
					forma.setMensaje(new ResultadoBoolean(true,
							"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA "
									+ path + nombre + exTxt + "!!!!!"));
					forma.setArchivo(true);
					forma.setZip(UtilidadFileUpload.existeArchivo(path, nombre
							+ exZip));
					UtilidadBD.finalizarTransaccion(con);
					UtilidadBD.closeConnection(con);
				} else {
					forma.setMensaje(new ResultadoBoolean(true,
							"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA "
									+ path + nombre + exTxt + "!!!!!"));
					forma.setArchivo(true);
					forma.setZip(false);
					UtilidadBD.finalizarTransaccion(con);
					UtilidadBD.closeConnection(con);
				}
			} else {
				forma.setMensaje(new ResultadoBoolean(true,
						"INCONSISTENCIAS EN LA GENERACION DEL ARCHIVO PLANO "
								+ path + nombre + exTxt + "!!!!!"));
				forma.setArchivo(false);
				forma.setZip(false);
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
			}
		} else {
			forma.setMensaje(new ResultadoBoolean(true,
					"SIN RESULTADOS PARA GENERAR EL ARCHIVO PLANO!!!!!"));
			forma.setArchivo(false);
			forma.setZip(false);
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
		}
	}

	/**
	 * Acción de Imprimir El listado de facturas despues de la busqueda
	 * 
	 * @param mapping
	 * @param request
	 * @param con
	 * @param consultaFacturasForm
	 * @param medico
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionImprimir(ActionMapping mapping,
			HttpServletRequest request, Connection con,
			ConsultaFacturasForm consultaFacturasForm, UsuarioBasico medico)
			throws Exception {
		String nombreArchivo;
		Random r = new Random();

		nombreArchivo = "/aBorrar" + r.nextInt() + ".pdf";

		ConsultaFacturasPdf.pdfFacturasTodos(ValoresPorDefecto.getFilePath()
				+ nombreArchivo, consultaFacturasForm, medico, request);

		this.cerrarConexion(con);
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Consulta Facturas");
		return mapping.findForward("abrirPdf");
	}

	/**
	 * Metodo implementado para posicionarse en la ultima pagina del pager.
	 * 
	 * @param con
	 *            , Connection con la fuente de datos.
	 * @param poolesForm
	 *            ConceptosCarteraForm
	 * @param response
	 *            HttpServletResponse
	 * @param request
	 *            HttpServletRequest
	 * @param String
	 *            enlace
	 * @return null
	 */
	public ActionForward redireccion(Connection con,
			ConsultaFacturasForm forma, HttpServletResponse response,
			HttpServletRequest request, String enlace, int maxPageItems) {
		int numRegistros = Integer.parseInt(forma
				.getMapaSolicitudesFactura("numRegistros")
				+ "");

		forma.setOffset(((int) ((Integer.parseInt(forma
				.getMapaSolicitudesFactura("numRegistros")
				+ "") - 1) / maxPageItems))
				* maxPageItems);
		if (request.getParameter("ultimaPage") == null
				|| request.getParameter("ultimaPage").equals("null")) {
			if (numRegistros > (forma.getOffset() + maxPageItems)) {
				forma.setOffset(((int) (numRegistros / maxPageItems))
						* maxPageItems);
			}
			try {
				this.cerrarConexion(con);
				response.sendRedirect(enlace + "?pager.offset="
						+ forma.getOffset());
			} catch (IOException e) {

				e.printStackTrace();
			}
		} else {
			String ultimaPagina = request.getParameter("ultimaPage");

			int posOffSet = ultimaPagina.indexOf("offset=") + 7;
			if (numRegistros > (forma.getOffset() + maxPageItems))
				forma.setOffset(forma.getOffset() + maxPageItems);

			try {
				this.cerrarConexion(con);
				response.sendRedirect(ultimaPagina.substring(0, posOffSet)
						+ forma.getOffset());
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		this.cerrarConexion(con);
		return null;
	}

	/**
	 * Método para que al ordenar se quede posicionado en la página del pager en
	 * la cual se encontraba
	 * 
	 * @param con
	 * @param forma
	 * @param response
	 * @param enlace
	 * @return
	 */
	public ActionForward redireccionColumnaPaciente(Connection con,
			ConsultaFacturasForm forma, HttpServletResponse response,
			String enlace) {

		try {
			this.cerrarConexion(con);
			response
					.sendRedirect(enlace + "?pager.offset=" + forma.getOffset());
		} catch (IOException e) {

			e.printStackTrace();
		}

		this.cerrarConexion(con);
		return null;
	}

	/***
	 * Método para que al ordenar se quede posicionado en la página del pager en
	 * la cual se encontraba
	 * 
	 * @param con
	 * @param forma
	 * @param response
	 * @param enlace
	 * @return
	 */
	public ActionForward redireccionColumnaTodos(Connection con,
			ConsultaFacturasForm forma, HttpServletResponse response,
			String enlace) {
		try {
			this.cerrarConexion(con);
			response
					.sendRedirect(enlace + "?pager.offset=" + forma.getOffset());
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.cerrarConexion(con);
		return null;
	}

	/**
	 * Abrir la conceccion con la Base de Datos
	 * 
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con) {

		if (con != null)
			return con;

		try {
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
		} catch (Exception e) {
			logger.warn("Problemas con la base de datos al abrir la conexion");
			return null;
		}

		return con;
	}

	/**
	 * Método en que se cierra la conexión (Buen manejo recursos), usado ante
	 * todo al momento de hacer un forward
	 * 
	 * @param con
	 *            Conexión con la fuente de datos
	 */
	public void cerrarConexion(Connection con) {
		try {
			if (con != null && !con.isClosed()) {
				UtilidadBD.closeConnection(con);
			}
		} catch (Exception e) {
			logger
					.error("Error al tratar de cerrar la conexion con la fuente de datos Consulta Facturas Action. \n Excepcion: "
							+ e);
		}
	}

}