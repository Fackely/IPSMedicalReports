/*
 * Created on 11/06/2004: Juand David Ramírez. Princeton S.A.  
 * Modificado: Jose Eduardo Arias Doncel. Abril 2008. Anexo 550 Y 529
 */
package com.princetonsa.action.resumenAtenciones;
 
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
import org.apache.struts.action.ActionMessages;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatos;
import util.InfoDatosBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.adjuntos.DTOArchivoAdjunto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.reportes.ConsultasBirt;

import com.princetonsa.action.ComunAction;
import com.princetonsa.action.medicamentos.SolicitudMedicamentosAction;
import com.princetonsa.actionform.AntecedentesGinecoObstetricosForm;
import com.princetonsa.actionform.medicamentos.SolicitudMedicamentosForm;
import com.princetonsa.actionform.resumenAtenciones.ResumenAtencionesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseImpresionResumenAtencionesDao;
import com.princetonsa.dto.historiaClinica.DtoCodigosCirugiaPeticiones;
import com.princetonsa.dto.historiaClinica.DtoIngresoHistoriaClinica;
import com.princetonsa.dto.historiaClinica.DtoPreanestesia;
import com.princetonsa.mundo.AntecedentesVacunas;
import com.princetonsa.mundo.CuentasPaciente;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.antecedentes.AntecedentePediatrico;
import com.princetonsa.mundo.antecedentes.AntecedentesGinecoObstetricos;
import com.princetonsa.mundo.antecedentes.Embarazo;
import com.princetonsa.mundo.antecedentes.HijoBasico;
import com.princetonsa.mundo.antecedentes.InfoMadre;
import com.princetonsa.mundo.antecedentes.InfoPadre;
import com.princetonsa.mundo.enfermeria.RegistroEnfermeria;
import com.princetonsa.mundo.historiaClinica.ImpresionResumenAtenciones;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.historiaClinica.RegistroEventosAdversos;
import com.princetonsa.mundo.historiaClinica.RegistroResumenParcialHistoriaClinica;
import com.princetonsa.mundo.manejoPaciente.RegistroAccidentesTransito;
import com.princetonsa.mundo.medicamentos.AdminMedicamentos;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.resumenAtenciones.ResumenAtenciones;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudMedicamentos;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;
import com.princetonsa.mundo.triage.Triage;
import com.princetonsa.pdf.ResumenAtencionesPdf;
import com.princetonsa.pdf.ResumenGuardarProcedimientoPdf;
import com.princetonsa.pdf.SolicitudMedicamentosPdf;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.dto.historiaClinica.InfoIngresoDto;
import com.servinte.axioma.dto.manejoPaciente.DtoNotaAclaratoria;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.generadorReporteHistoriaClinica.comun.IConstantesReporteHistoriaClinica;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.fabrica.historiaClinica.HistoriaClinicaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.historiaClinica.IAdjuntoNotaAclaratoriaMundo;
import com.servinte.axioma.mundo.interfaz.historiaClinica.INotaAclaratoriaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo;
import com.servinte.axioma.orm.AdjuntoNotaAclaratoria;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.orm.Medicos;
import com.servinte.axioma.orm.NotaAclaratoria;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 * @author juanda
 * 
 */
public class ResumenAtencionesAction extends Action
{
	private Logger logger = Logger.getLogger(ResumenAtencionesAction.class);
	/**
	* Tipo Modificacion: Segun incidencia 6480
	* Autor: Alejandro Aguirre Luna
	* usuario: aleagulu
	* Fecha: 18/02/2013
	* Descripcion: Variable numRegistrosEncontrados que permite establecer
	* el numero de secciones encontradas para el paciente. 
	**/
	private int numRegistrosEncontrados;
	public int getNumRegistrosEncontrados() {
		return numRegistrosEncontrados;
	}
	public void setNumRegistrosEncontrados(int numRegistrosEncontrados) {
		this.numRegistrosEncontrados = numRegistrosEncontrados;
	}
	public ResumenAtencionesAction(){
		numRegistrosEncontrados = 0;
	}
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Connection con=null;
		try{
			ResumenAtencionesForm resumenAtencionesForm;
			ResumenAtenciones resumenAtencionesMundo = new ResumenAtenciones();

			PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			InstitucionBasica institucionActual = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			ActionErrors errores=new ActionErrors();

		
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			}
			if (form instanceof ResumenAtencionesForm)
			{
				if(paciente==null||paciente.getCodigoPersona()<=0)
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado", "errors.paciente.noCargado", true);
				}
				resumenAtencionesForm=(ResumenAtencionesForm)form;
				
				String estado=resumenAtencionesForm.getEstado();
				logger.warn("[ResumenAtencionesAction] estado = >"+estado);

				
				resumenAtencionesForm.setMostrarRutaJsp(ValoresPorDefecto.getMostrarNombreJSP());
				
			
				//Validaciones de Profesional de la salud
				if(!UtilidadValidacion.esProfesionalSalud(usuario)&&estado.equals("empezar")){
					resumenAtencionesForm.reset();
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No es profesional de la salud Tratante", "errors.usuario.noAutorizado", true);
				}

				if(estado.equals("empezar"))
				{
					inicializarListaChecks(resumenAtencionesForm);
					
					return accionEmpezar(con,resumenAtencionesForm,resumenAtencionesMundo,paciente,usuario,mapping,request);
				}

				//************ESTADOS DEL DETALLE DEL INGRESO********************************************************************
				else if(estado.equals("resumenIngreso"))
				{
					accionResumenIngreso(con,resumenAtencionesForm,resumenAtencionesMundo,mapping,usuario,paciente);
					UtilidadBD.closeConnection(con);
//					if(resumenAtencionesForm.getEstadoHCCLiente().equals(obj))
					return mapping.findForward("resumenIngreso");
				}
				
				else if(estado.equals("resumenIngresoNuevo"))
				{
					accionResumenIngreso(con,resumenAtencionesForm,resumenAtencionesMundo,mapping,usuario,paciente);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenIngresoNuevo");
				}
				else if(estado.equals("buscarIngreso"))
				{
					return accionBuscarIngreso(con,resumenAtencionesForm,resumenAtencionesMundo,mapping,usuario,paciente);
				}
				else if(estado.equals("volverIngreso")||estado.equals("impresionHC")||estado.equals("impresionHCCompleta"))
				{
					UtilidadBD.closeConnection(con);
					
					if(estado.equals("impresionHCCompleta")){
						resumenAtencionesForm.setEstadoBotonImprimirHC(true);
						resumenAtencionesForm.setImprimiendoHC(false);
						resumenAtencionesForm.setContarCantidadSubmitImpresion(0);
					}else{
						if(estado.equals("impresionHC")){
							resumenAtencionesForm.setEstadoBotonImprimirHC(false);
							resumenAtencionesForm.setImprimiendoHC(true);							
							resumenAtencionesForm.setContarCantidadSubmitImpresion(resumenAtencionesForm.getContarCantidadSubmitImpresion()+1);
						}
					}
					
					if(resumenAtencionesForm.getEstadoHCCLiente().equals(0)){
						return mapping.findForward("resumenIngresoNuevo");
					}else{
						return mapping.findForward("resumenIngreso");
					}
				}else if(estado.equals("imprimiendoHC")){
					resumenAtencionesForm.setEstadoBotonImprimirHC(false);
					//Ya mando a ejecutar la impresion, por tanto, no es necesario activarlo
					resumenAtencionesForm.setImprimiendoHC(false);
					if(resumenAtencionesForm.getEstadoHCCLiente().equals(0)){
						return mapping.findForward("resumenIngresoNuevo");
					}else{
						return mapping.findForward("resumenIngreso");
					}
				}
				else if(estado.equals("impresionHCVariosIngresos")){
					
					ActionMessages messages=new ActionMessages();
					if(!resumenAtencionesForm.isHaSeleccionadoIngreso()){
						
						messages.add("", new ActionMessage("error.historiaClinica.ingresoRequerido"));
						saveErrors(request, messages);
						return mapping.findForward("paginaInicio");
					}
					
					accionResumenIngreso(con,resumenAtencionesForm,resumenAtencionesMundo,mapping,usuario,paciente);
					
					resumenAtencionesForm.setEstadoBotonImprimirHC(false);
					resumenAtencionesForm.setImprimiendoHC(true);							
					resumenAtencionesForm.setContarCantidadSubmitImpresion(resumenAtencionesForm.getContarCantidadSubmitImpresion()+1);
					
					UtilidadBD.closeConnection(con);
					return mapping.findForward("impresionHCVariosIngresos");
				}else if(estado.equals("impresionHCCompletaVarios")){
					resumenAtencionesForm.setEstadoBotonImprimirHC(true);
					resumenAtencionesForm.setImprimiendoHC(false);
					resumenAtencionesForm.setContarCantidadSubmitImpresion(0);
					return mapping.findForward("paginaInicio");
				}else if(estado.equals("seleccionarIngreso")){
					
					
					try {
						String haSeleccionadoIngresoString=request.getParameter("haSeleccionadoIngresoString");	
						String posicionSeleccionada=request.getParameter("posicionSeleccionada");
						DtoIngresoHistoriaClinica dto=resumenAtencionesForm.getListaDtoHc().get(Integer.parseInt(posicionSeleccionada));
						if(UtilidadTexto.getBoolean(haSeleccionadoIngresoString)){
							dto.setIngreso(true);
						}else{
							dto.setIngreso(false);
						}
						
						boolean haSeleccionadoIngreso = false;
						int cantidadIngresosSeleccionados = 0;
						for (DtoIngresoHistoriaClinica dtoIngresoHC:resumenAtencionesForm.getListaDtoHc()) {
							if(dtoIngresoHC.getIngreso()!=null&&dtoIngresoHC.getIngreso()){
								haSeleccionadoIngreso=true;
								cantidadIngresosSeleccionados++;
							}
						}
						
						resumenAtencionesForm.setHaSeleccionadoIngreso(haSeleccionadoIngreso);
						resumenAtencionesForm.setCantidadIngresosSeleccionados(cantidadIngresosSeleccionados);

						response.setCharacterEncoding("UTF-8");
						response.setContentType("text/plain");
						response.setHeader("Cache-Control", "no-cache");
						response.getWriter().write(String.valueOf(haSeleccionadoIngreso));

					} catch (IOException e) {
						logger.error("Error al enviar respuesta AJAX en seleccionarIngreso: "	+ e);
					}

					//RETORNA NULL POR QUE NO TIENE QUE RE DIRIGIR A NINGUN LADO 
					return null;						
					//return mapping.findForward("paginaInicio");
				}

				
				
				
				//
				
				//*************ESTADOS DEL PERFIL DE FARMACOTERAPIA
				else if(estado.equals("detallePerfilFarmacoterapia"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionDetallePerfilFarmacoterapia(con,resumenAtencionesForm,mapping);
				}
				else if(estado.equals("cambiarMesPerfilFarmacoterapia"))
				{
					return accionCambiarMesPerfilFarmacoterapia(con,resumenAtencionesForm,mapping);
				}
				else if(estado.equals("imprimirPerfilFarmacoterapia"))
				{
					accionImprimirPerfilFarmacoterapia(con,resumenAtencionesForm,mapping,request, usuario, paciente, ConstantesBD.acronimoNo);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detallePerfilFarmacoterapia");
				}
				else if (estado.equals("ordenarPerfilFarmacoterapia"))
				{
					accionOrdenarPerfilFarmacoterapia(resumenAtencionesForm);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detallePerfilFarmacoterapia");
				}


				//*************ESTADOS DEL DETALLE DE CADA ATENCION***********************************************************
				else if(estado.equals("detalleAdminMedicamentos")||estado.equals("volverAdminMedicamentos"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionDetalleAdminMedicamentos(con,resumenAtencionesForm,mapping);
				}
				else if(estado.equals("detalleArticuloAdmin")) //detalle de cada articulo en la hoja de administracion
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionDetalleArticuloAdmin(con,resumenAtencionesForm,mapping);
				}
				else if(estado.equals("detalleInsumos"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionDetalleInsumos(con,resumenAtencionesForm,mapping);
				}
				else if(estado.equals("detalleProcedimientos"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionDetalleProcedimientos(con,resumenAtencionesForm,mapping,usuario);
				}
				else if(estado.equals("detalleNotasEnfermeria"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionDetalleNotasEnfermeria(con,resumenAtencionesForm,mapping);
				}
				else if(estado.equals("detalleSignosVitales"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionDetalleSignosVitales(con,resumenAtencionesForm,mapping,usuario);
				}
				else if(estado.equals("detalleCateterSonda"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionDetalleCateterSonda(con,resumenAtencionesForm,mapping,usuario);
				}
				else if(estado.equals("detalleControlLiquidos"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return acccionDetalleControlLiquidos(con,resumenAtencionesForm,mapping,usuario,resumenAtencionesForm.getIndiceMapaValInfoCuentas());				
				}
				else if(estado.equals("detalleAntecedentes"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionDetalleAntecedentes(con,resumenAtencionesForm,mapping,paciente,usuario);

				}
				else if(estado.equals("popupResumenAntecedentes"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionPopupResumenAntecedentes(con,resumenAtencionesForm,mapping,paciente,usuario);

				}
				else if(estado.equals("detalleInterconsultas"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionDetalleInterconsultas(con,resumenAtencionesForm,mapping);

				}
				else if(estado.equals("detalleEvoluciones"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"detalleEvoluciones");
					UtilidadBD.cerrarConexion(con);				
					return mapping.findForward("detalleEvoluciones");

				}
				else if(estado.equals("detalleValoracionInicial")) {
					cargarDatosMapaCuentasForma(con, resumenAtencionesForm, "detalleValoracionInicial");
					UtilidadBD.cerrarConexion(con);				
					return mapping.findForward("detalleValoracionInicial");
				}
				
				else if(estado.equals("detalleOrdenesMedicas"))
				{				
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionOrdenesMedicas(con,resumenAtencionesForm,mapping, request);				
				}
				else if(estado.equals("detalleEventosAdversos"))
				{				
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionEventosAdversos(con,resumenAtencionesForm,mapping);				
				}
				else if(estado.equals("detalleRegistroAccidente"))
				{				
					return accionDetalleRegistroAccidente(con,resumenAtencionesForm,mapping,usuario);
				}
				else if(estado.equals("detalleSoporteRespiratorio"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionSoporteRespiratorio(con,resumenAtencionesForm,mapping);				
				}
				else if(estado.equals("detalleCuidadosEspeciales"))
				{				
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionCuidadosEspeciales(con,resumenAtencionesForm,mapping);				
				}
				else if(estado.equals("detalleValoracionEnfermeria"))
				{
					return accionCargarValoracionEnfermeria(con,resumenAtencionesForm,mapping,usuario);
				}
				else if(estado.equals("detalleResultadosLaboratorio"))
				{
					return accionCargarResultadosLaboratorio(con,resumenAtencionesForm,mapping,usuario);
				}

				else if(estado.equals("detalleHojaNeurologica"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionHojaNeurologica(con,resumenAtencionesForm,mapping,usuario);
				}
				///**************ESTADOS PARA EL MANEJO DE LAS CIRUGIAS****************************************
				else if(estado.equals("detalleCirugias")) //estado para consultar el listado de las cirugias
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return 	 accionDetalleCirugias(con,resumenAtencionesForm,mapping,usuario);
				}else if(estado.equals("detalleCirugiasNotasCirugias"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					accionDetalleCirugias(con,resumenAtencionesForm,mapping,usuario);
					
					return mapping.findForward("detalleCirugiasNotasCirugias");
				}
				else if(estado.equals("detalleCirugiasNotasRecuperacion"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					accionDetalleCirugias(con,resumenAtencionesForm,mapping,usuario);
					
					return mapping.findForward("detalleCirugiasNotasRecuperacion");
				}
				else if(estado.equals("detalleDatosCirugia")) //estado para ver el detalle de una cirugia específica
				{
					accionDetalleDatosCirugia(resumenAtencionesForm);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleCirugias");
				}
				else if(estado.equals("volverListadoCirugias")) 
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleListadoCirugias");
				}
				//*****************************************************************************
				else if(estado.equals("detalleCitas"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionDetalleCitas(con,resumenAtencionesForm,mapping);
				}

				else if(estado.equals("recargarDetalleCitas"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleCitas");
				}
				else if(estado.equals("detalleServiciosCitas"))
				{
					return accionDetalleServiciosCitas(con,resumenAtencionesForm,mapping);
				}
				else if(estado.equals("recargarDetalleServiciosCitas"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleServiciosCitas");
				}
				else if (estado.equals("detalleEscalas"))
				{
					return accionDetalleEscalas(con,resumenAtencionesForm,mapping);
				}
				else if (estado.equals("detalleValoracionesCuidadoEspecial"))
				{
					return accionDetalleValoracionesCuidadoEspecial(con,resumenAtencionesForm,mapping);
				}
				//*********************************************************************************************
				//*************ESTADOS PARA LAS CONSULTAS DE TRIAGE*********************************************
				else if(estado.equals("resumenListadoTriage"))
				{
					return accionResumenListadoTriage(con,resumenAtencionesForm,paciente,mapping,usuario);
				}
				else if(estado.equals("resumenDetalleTriage"))
				{
					return accionResumenDetalleTriage(con,resumenAtencionesForm,mapping);
				}
				//**********************************************************************************************
				//**************ESTADOS PARA LAS CONSULTAS DE PYP************************************************
				else if (estado.equals("consultasPYP"))
				{
					cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
					return accionConsultasPYP(con,resumenAtencionesForm,mapping,usuario);
				}
				else if (estado.equals("ordenarConsultasPYP"))
				{
					return accionOrdenarConsultasPYP(con,resumenAtencionesForm,mapping);
				}
				//****************************************************************************************************
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenar(resumenAtencionesForm);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("listadoSolicitudes");
				}
				else if(estado.equals("imprimirHistoriaClinicaAuto"))
				{
					accionImprimirHistoriaClinicaAuto(con,resumenAtencionesForm,resumenAtencionesMundo,mapping,usuario,paciente);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenIngreso");
				}
				else if (estado.equals("imprimir"))
				{
					String nombreArchivo;
					Random r=new Random();
					nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";

					ResumenAtencionesPdf.pdfResumenAtenciones(ValoresPorDefecto.getFilePath() + nombreArchivo,resumenAtencionesForm,paciente,usuario,con);
					UtilidadBD.cerrarConexion(con);
					request.setAttribute("nombreArchivo", nombreArchivo);
					request.setAttribute("nombreVentana", "Resumen de Atenciones");
					return mapping.findForward("abrirPdf");
				}
				////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				//estado adicionado para el manejo de las impresiones de la solicitud
				// tarea Xplanner3 No. 39076
				else 
					if (estado.equals("imprimirSol"))		
					{

						return imprimirSolcitudes(con, Utilidades.convertirAEntero(resumenAtencionesForm.getSolicitud()), mapping, usuario, paciente, request, resumenAtencionesForm.getTipoSolicitud(), Utilidades.convertirAEntero(resumenAtencionesForm.getCodigoArticulo()), institucionActual);

					}
				////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					else if(estado.equals("resumenes"))
					{
						consultarSolicitudesConAdministracion(con,resumenAtencionesForm);
						HashMap solicitudes=new HashMap();
						AdminMedicamentos mundoAdmin =new AdminMedicamentos();
						Iterator iterador=resumenAtencionesForm.getColeccionSolicitudes().iterator();
						int i=0;
						while(iterador.hasNext())
						{
							HashMap valor=(HashMap)iterador.next();
							int numeroSolicitud=Integer.parseInt(valor.get("numerosolicitud")+"");
							Solicitud  objetoSolicitud=new Solicitud();
							try
							{
								objetoSolicitud.cargar(con,numeroSolicitud);
							}
							catch(SQLException e)
							{
								e.printStackTrace();
							}
							solicitudes.put("numeroOrden_"+i,objetoSolicitud.getConsecutivoOrdenesMedicas()+"");
							solicitudes.put("solicitud_"+i,numeroSolicitud+"");
							solicitudes.put("numeroArticulos_"+i,mundoAdmin.resumenAdmiMedicamentosPart1(con,numeroSolicitud)+"");
							solicitudes.put("administraciones_"+i,mundoAdmin.resumenAdmiMedicamentosPart2(con,numeroSolicitud)+"");
							//recorrer los articulos que tiene una solictud.
							/**
							 * @armando
							 * Debido a que el hashmap de administracion contiene todos los datos sin ningun orden específico e decidido organizarlos en un hasmap
							 * con indices que indica a que solicitud, a que articulo y a que administracion corresponde cada dato.
							 */
							for(int k=0;k<Integer.parseInt(solicitudes.get("numeroArticulos_"+i)+"");k++)
							{
								//variable para manejar el indice de la administracion de cada articulo.
								int pos=0;
								//Cargando los datos de cada articulo
								resumenAtencionesForm.setSolicitudes("resumenCodigoArt_Articulo_"+k+"_Solicitud_"+i,mundoAdmin.getResumenAdminMap("resumenCodigoArt_"+k)+"");
								resumenAtencionesForm.setSolicitudes("resumenArt_Articulo_"+k+"_Solicitud_"+i,mundoAdmin.getResumenAdminMap("resumenArt_"+k)+"");
								resumenAtencionesForm.setSolicitudes("dosis_Articulo_"+k+"_Solicitud_"+i,mundoAdmin.getResumenAdminMap("RAdosis_"+k)+"");
								resumenAtencionesForm.setSolicitudes("frecuencia_Articulo_"+k+"_Solicitud_"+i,mundoAdmin.getResumenAdminMap("RAfrecuencia_"+k)+"");
								resumenAtencionesForm.setSolicitudes("via_Articulo_"+k+"_Solicitud_"+i,mundoAdmin.getResumenAdminMap("RAvia_"+k)+"");
								resumenAtencionesForm.setSolicitudes("resumenDespachoTotal_Articulo_"+k+"_Solicitud_"+i,mundoAdmin.getResumenAdminMap("resumenDespachoTotal_"+k)+"");
								resumenAtencionesForm.setSolicitudes("resumenAdminFarmacia_Articulo_"+k+"_Solicitud_"+i,mundoAdmin.getResumenAdminMap("resumenAdminFarmacia_"+k)+"");
								resumenAtencionesForm.setSolicitudes("resumenAdminPaciente_Articulo_"+k+"_Solicitud_"+i,mundoAdmin.getResumenAdminMap("resumenAdminPaciente_"+k)+"");
								resumenAtencionesForm.setSolicitudes("resumenTotalAdmin_Articulo_"+k+"_Solicitud_"+i,mundoAdmin.getResumenAdminMap("resumenTotalAdmin_"+k)+"");
								for(int z=0;z<Integer.parseInt(solicitudes.get("administraciones_"+i)+"");z++)
								{
									if((mundoAdmin.getResumenAdminMap("resumenCodigoArt_" + k)+"").equals(mundoAdmin.getResumenAdminMap("codigoArt_" + z)+""))
									{
										resumenAtencionesForm.setSolicitudes("cantidadArt_"+pos+"_Articulo_"+k+"_Solicitud_"+i,mundoAdmin.getResumenAdminMap("cantidadArt_"+z)+"");
										resumenAtencionesForm.setSolicitudes("fechaHora_Admin_"+pos+"_Articulo_"+k+"_Solicitud_"+i,mundoAdmin.getResumenAdminMap("fechaHoraAdminArt_"+z)+"");
										resumenAtencionesForm.setSolicitudes("observaciones_Admin_"+pos+"_Articulo_"+k+"_Solicitud_"+i,mundoAdmin.getResumenAdminMap("observacionesArt_"+z)+"");
										String[] fechahora=(mundoAdmin.getResumenAdminMap("usuarioFechaHoraArt_"+z)+"").split("-");
										resumenAtencionesForm.setSolicitudes("usuarionFechaHora_Admin_"+pos+"_Articulo_"+k+"_Solicitud_"+i,fechahora[0]+fechahora[1]+fechahora[2].substring(0,9));
										resumenAtencionesForm.setSolicitudes("traidoPaciente_Admin_"+pos+"_Articulo_"+k+"_Solicitud_"+i,mundoAdmin.getResumenAdminMap("traidoPacienteArt_"+z)+"");
										pos++;
									}
								}
								resumenAtencionesForm.setSolicitudes("administraciones_Articulo_"+k+"_Solicitud_"+i,pos+"");
							}
							resumenAtencionesForm.setSolicitudes("numeroOrden_"+i,objetoSolicitud.getConsecutivoOrdenesMedicas()+"");
							resumenAtencionesForm.setSolicitudes("solicitud_"+i,numeroSolicitud+"");
							resumenAtencionesForm.setSolicitudes("numeroArticulos_"+i,solicitudes.get("numeroArticulos_"+i)+"");
							String[] fechaSolicitud=(valor.get("fechahorasolicitud")+"").split(" ");
							resumenAtencionesForm.setSolicitudes("fechahorasolicitud_"+i,UtilidadFecha.conversionFormatoFechaAAp(fechaSolicitud[0])+" "+fechaSolicitud[1]);
							i++;
						}
						resumenAtencionesForm.setNumeroSolicitudes(i);
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("resumenAdministracion");
					}
					else if(estado.equals("ordenarlistado")){
						this.accionOrdenarIngresos(con,resumenAtencionesForm,resumenAtencionesMundo,paciente,usuario,mapping,request);
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaInicio");
					}
					else if(estado.equals("adjuntar"))
					{
						return this.accionAdjuntarDocumento(con, mapping, request, resumenAtencionesForm, resumenAtencionesMundo, paciente);
					}
				/**ESTADOS YA NO SON USADOs**/
					else if(estado.equals("cirugias"))
					{
						return accionCirugias(con,resumenAtencionesForm,mapping,usuario);
					}
					else if (estado.equals("redireccion"))
					{
						return accionRedireccion(con,resumenAtencionesForm,response,mapping,request);
					}
					else if (estado.equals("ordenarCx"))
					{
						return accionOrdenarCx(con,resumenAtencionesForm,mapping);
					}
				/***********************************/
				//Anexo 550.*****************************************
					else if(estado.equals("impresionDetalleCargoDir"))
					{
						accionCargoDirecto(con,resumenAtencionesForm, resumenAtencionesMundo, paciente,usuario);			
						UtilidadBD.cerrarConexion(con);								
						return mapping.findForward("detalleCargoDirecto");
					}			
					else if(estado.equals("detalleCargoDirecto"))
					{
						cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
						accionCargoDirecto(con,resumenAtencionesForm, resumenAtencionesMundo, paciente,usuario);
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("detalleCargoDirecto");
					}
					else if(estado.equals("detalleNotasGenerales"))
					{
						cargarDatosMapaCuentasForma(con,resumenAtencionesForm,"cargarCuenta");
						accionDetalleCirugias(con,resumenAtencionesForm,mapping,usuario);
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("detalleCargoDirecto");
					}	
						
				//***************************************************	
				
				//************* ESTADOS PARA EL REGISTRO DE NOTAS ACLARATORIAS *********************************************
					else if(estado.equals("registroNotasAclaratorias")||estado.equals("impresionHCNotasAclaratorias"))
					{
						return accionRegistroNotasAclaratorias(con, resumenAtencionesForm, paciente, usuario, mapping);
					}
				
					else if(estado.equals("nuevaNotaAclaratoria"))
					{
						return accionNuevaNotaAclaratoria(con, resumenAtencionesForm, paciente, usuario, mapping);
					}
					else if(estado.equals("guardarNotaAclaratoria"))
					{
						return accionGuardarNotaAclaratoria(con, resumenAtencionesForm, paciente, usuario, mapping, errores, request);
					}
					else if(estado.equals("detalleNotaAclaratoria"))
					{
						return accionDetalleNotaAclaratoria(con, resumenAtencionesForm, paciente, usuario, mapping);
					}else  if(estado.equals("filtroBusqueda")){


						String dias=ValoresPorDefecto.getMaximoDiasConsultarIngresos(usuario.getCodigoInstitucionInt());


						if(!dias.equals("")){
							Integer cantidadMaxDias = Integer.valueOf(dias);
							resumenAtencionesForm.setCantidadDiasParametro(cantidadMaxDias);
							if(cantidadMaxDias.equals(365)){
								resumenAtencionesForm.setMostrarFiltroAnos(true);
							}else{
								resumenAtencionesForm.setMostrarFiltroAnos(false);
							}
						}else{
							resumenAtencionesForm.setMostrarFiltroAnos(false);
						}


						return 	accionBusquedaFiltro(con, resumenAtencionesForm, resumenAtencionesMundo, paciente, usuario, mapping, request);
					}else  if(estado.equals("buscarNuevosIngresos")){
						return 	ejecutarBusquedaFiltro(con, resumenAtencionesForm, resumenAtencionesMundo, paciente, usuario, mapping, request);
					}
				
				
				
				
				
				
				
				//***************************************************			
					else
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion específicada no esta definida", "errors.estadoInvalido", true);
					}

			}
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion específicada no esta definida", "errors.estadoInvalido", true);
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}

	/**
	 * 
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarResultadosLaboratorio(Connection con,
			ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping,
			UsuarioBasico usuario) {
		String fechaInicial = resumenAtencionesForm.getFechaInicial();
		String fechaFinal = resumenAtencionesForm.getFechaFinal();
		String horaInicial = resumenAtencionesForm.getHoraInicial();
		String horaFinal = resumenAtencionesForm.getHoraFinal();
		
		fechaInicial = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
		fechaFinal = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
		
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones  ();
		resumenAtencionesForm.setResultadoLaboratorios(mundo.consultarResultatadosLaboratorios(con,resumenAtencionesForm.getIdIngreso(),fechaInicial,fechaFinal,horaInicial,horaFinal));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleResultadosLaboratorio");
	}

	/**
	 * 
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarValoracionEnfermeria(Connection con,ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping,UsuarioBasico usuario) 
	{
		String fechaInicial = resumenAtencionesForm.getFechaInicial();
		String fechaFinal = resumenAtencionesForm.getFechaFinal();
		String horaInicial = resumenAtencionesForm.getHoraInicial();
		String horaFinal = resumenAtencionesForm.getHoraFinal();
		
		fechaInicial = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
		fechaFinal = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
		
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones  ();
		
		resumenAtencionesForm.setValoracionesEnfermeria(mundo.consultarrValoracionesEnfermeria(con,resumenAtencionesForm.getIdIngreso(),fechaInicial,fechaFinal,horaInicial,horaFinal));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleValoracionEnfermeria");
	}
	
	/**
	 * Imprimir la historia clinica automaticamnete
	 * @param con
	 * @param resumenAtencionesForm
	 * @param resumenAtencionesMundo
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @throws IPSException 
	 */
	private void accionImprimirHistoriaClinicaAuto(Connection con,
			ResumenAtencionesForm resumenAtencionesForm,
			ResumenAtenciones resumenAtencionesMundo, ActionMapping mapping,
			UsuarioBasico usuario, PersonaBasica paciente) throws IPSException {
		
		accionResumenIngreso(con, resumenAtencionesForm, resumenAtencionesMundo, mapping, usuario, paciente);
		resumenAtencionesForm.setEsImpresionAutomatica(ConstantesBD.acronimoSi);
		
		resumenAtencionesForm.setCuentaAsocio(Utilidades.getCuentaAsociada(con,resumenAtencionesForm.getCuenta()+"",false));
		
		resumenAtencionesForm.setEstado("impresionHC");
	}


	/**
	 * Método para cargar el listado de las valoraciones de cuidado especial
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleValoracionesCuidadoEspecial(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping) 
	{
		ResumenAtenciones mundoResumen = new ResumenAtenciones();
		resumenAtencionesForm.setValoracionesCuidadoEspecial(
			mundoResumen.cargarValoracionesCuidadoEspecial(
				con, 
				resumenAtencionesForm.getIdIngreso(), 
				resumenAtencionesForm.getFechaInicial(), 
				resumenAtencionesForm.getHoraInicial(), 
				resumenAtencionesForm.getFechaFinal(), 
				resumenAtencionesForm.getHoraFinal()
			)
		);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleValoracionesCuidadoEspecial");
	}


	/**
	 * Método para cargar el detalle de las escalas
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleEscalas(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping) 
	{
		resumenAtencionesForm.setEscalas(
			Plantillas.obtenerEscalasIngreso(
				con, 
				resumenAtencionesForm.getIdIngreso(), 
				resumenAtencionesForm.getFechaInicial(), 
				resumenAtencionesForm.getHoraInicial(), 
				resumenAtencionesForm.getFechaFinal(), 
				resumenAtencionesForm.getHoraFinal()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleEscalas");
	}


	private ActionForward accionEventosAdversos(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping) {

		RegistroEventosAdversos rea = new RegistroEventosAdversos();
		rea.setIngreso(resumenAtencionesForm.getIdIngreso());
		resumenAtencionesForm.setEventosAdversos(rea.consultarDetalleXCuenta2(con, rea));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleEventosAdversos");
	}


	/****************************************************************************************
	 * Metodos adicionados por la tarea No 39076 de xplanner3 para imprimir las solicitudes
	 */
	/**
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param tipoSol
	 * @return
	 */
	public static ActionForward imprimirSolcitudes (Connection connection, int numeroSolicitud,ActionMapping mapping, UsuarioBasico usuario,PersonaBasica paciente, HttpServletRequest request, String tipoSol, int articulo, InstitucionBasica institucionActual)
	{
		System.out.print(" entro a imprimirSolcitudes numsol--> "+numeroSolicitud+"  tipo de solicitud -->"+tipoSol+" articulo--> "+articulo);
		
		try 
		{
			if (tipoSol.equals("procedimientos"))
			{
				return accionImprimirSolProc(connection, numeroSolicitud, mapping, usuario, paciente, request, institucionActual);
			}
			else
				if (tipoSol.equals("medicamentos"))
				{
					SolicitudMedicamentos mundo = new SolicitudMedicamentos();
					return accionImprimirSolMedic(connection, numeroSolicitud, mundo, usuario, paciente, request, mapping);
				}
				else
					if (tipoSol.equals("justificacion"))
					{
						SolicitudMedicamentos mundo = new SolicitudMedicamentos();
						SolicitudMedicamentosForm forma = new SolicitudMedicamentosForm();
						return accionImprimirNoPos(forma, mundo, paciente, usuario, request, connection, mapping,numeroSolicitud,articulo);
					}
					
				
				
		} 
		catch (Exception e) 
		{
		System.out.print("problema  imprimiendo las solicitudes "+e);	
		}
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("detalleOrdenesMedicas");
	}
	
	
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param con
	 * @param mapping
	 * @return
	 */
	private static ActionForward accionImprimirNoPos(SolicitudMedicamentosForm forma, SolicitudMedicamentos mundo, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request, Connection con, ActionMapping mapping, int numerosol, int articulo) 
	{
		System.out.print("entre a  accionImprimirNoPos articulo -->"+articulo+" numerosol -->"+numerosol);
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
    	int articulonopos=articulo;
    	mundo.setNumeroSolicitud(numerosol);
    	mundo.cargarSolicitudMedicamentos(con,usuario.getCodigoInstitucionInt());
    	SolicitudMedicamentosAction solAction = new SolicitudMedicamentosAction();
    	solAction.llenarForma(mundo, forma, paciente, usuario, new SolicitudMedicamentos());
    	solAction.llenarFormaMedicamentos(con, mundo, forma, usuario);
    	SolicitudMedicamentosPdf.pdfSolicitudMedicamentosNoPos(ValoresPorDefecto.getFilePath() + nombreArchivo, mundo, usuario,paciente,articulonopos,con);
    	request.setAttribute("nombreArchivo", nombreArchivo);
        request.setAttribute("nombreVentana", "Consulta Diagnósticos");
        UtilidadBD.closeConnection(con);
        return mapping.findForward("abrirPdf");
	}
	
	/**
	 * Metodo para la impresion de una solicitud exitosamente guardada
	 * @param con
	 * @param numeroSolicitud
	 * @param mapping
	 * @param usu
	 * @param pacienteActivo
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private static ActionForward  accionImprimirSolProc (Connection con, int numeroSolicitud, ActionMapping mapping,UsuarioBasico usu,PersonaBasica pacienteActivo, HttpServletRequest request, InstitucionBasica institucionActual)throws SQLException 
    {
		SolicitudProcedimiento solicitud=new SolicitudProcedimiento();
		solicitud.cargarSolicitudProcedimiento(con, numeroSolicitud);
		solicitud.getImpresion();
        String nombreArchivo;
		Random r=new Random();
		
		nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
		
		ResumenGuardarProcedimientoPdf.pdfImprimirResumen(con, ValoresPorDefecto.getFilePath() +	nombreArchivo, solicitud,usu, pacienteActivo,false, institucionActual);
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Procedimientos");
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("abrirPdf");
    }
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente
	 * @return
	 * @throws SQLException 
	 */
	private static ActionForward accionImprimirSolMedic(Connection con, int numeroSolicitud,SolicitudMedicamentos mundo, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping) throws SQLException 
	{
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
    	mundo.setNumeroSolicitud(numeroSolicitud);
    	SolicitudMedicamentosPdf.pdfSolicitudMedicamentos(ValoresPorDefecto.getFilePath() + nombreArchivo, mundo, usuario,paciente,con, new SolicitudMedicamentos());
    	request.setAttribute("nombreArchivo", nombreArchivo);
        request.setAttribute("nombreVentana", "Consulta Diagnósticos");
    	UtilidadBD.cerrarConexion(con);
        return mapping.findForward("abrirPdf");
	}
	
	/**
	 * Método que consulta el detalle de los servicios de la cita
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleServiciosCitas(Connection con, ResumenAtencionesForm forma, ActionMapping mapping) 
	{
		
		ResumenAtenciones resumenAt = new ResumenAtenciones();
		String codigoCita = forma.getCitas("codigoCita_"+forma.getPosCita()).toString();
		forma.setDetalleCita(resumenAt.listarServiciosCita(con, codigoCita));
		forma.setNumDetalleCita(Integer.parseInt(forma.getDetalleCita("numRegistros").toString()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleServiciosCitas");
	}

	/**
	 * Método implementado para cargar el detalle de un triage
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionResumenDetalleTriage(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping) 
	{
		//Se instancia objeto de triage
		Triage triage = new Triage();
		
		//logger.info("consecutivoTriage=> "+resumenAtencionesForm.getConsecutivoTriage());
		String[] consecutivo = resumenAtencionesForm.getConsecutivoTriage().split(ConstantesBD.separadorSplit);
		triage = triage.cargarTriage(con, consecutivo[0], consecutivo[1]);
		
		//Se carga el mapa de resumen de atenciones
		resumenAtencionesForm.setDetalleTriage("consecutivo", triage.getConsecutivo());
		resumenAtencionesForm.setDetalleTriage("consecutivoFecha",triage.getConsecutivo_fecha());
		resumenAtencionesForm.setDetalleTriage("fecha", UtilidadFecha.conversionFormatoFechaAAp(triage.getFecha()));
		resumenAtencionesForm.setDetalleTriage("hora", UtilidadFecha.convertirHoraACincoCaracteres(triage.getHora()));
		resumenAtencionesForm.setDetalleTriage("noRespondioLlamado", triage.getNoRespondeLlamado());
		resumenAtencionesForm.setDetalleTriage("nombrePaciente", 
			triage.getPrimer_apellido()+
			(triage.getSegundo_apellido().equals("")?"":" "+triage.getSegundo_apellido())+
			" "+triage.getPrimer_nombre()+
			(triage.getSegundo_nombre().equals("")?"":" "+triage.getSegundo_nombre()));
		resumenAtencionesForm.setDetalleTriage("tipoIdentificacion",triage.getTipo_id().getId());
		resumenAtencionesForm.setDetalleTriage("numeroIdentificacion", triage.getNumero_id());
		//CALCULO DE LA EDAD
		String edad=UtilidadFecha.conversionFormatoFechaAAp(triage.getFecha_nacimiento());
		if(!triage.getFecha_nacimiento().equals(""))
			resumenAtencionesForm.setDetalleTriage("edad", UtilidadFecha.calcularEdadDetallada(Integer.parseInt(edad.substring(6,10)),
											Integer.parseInt(edad.substring(3,5)),
											Integer.parseInt(edad.substring(0,2)),
											UtilidadFecha.getFechaActual().substring(0,2),
											UtilidadFecha.getFechaActual().substring(3,5),
											UtilidadFecha.getFechaActual().substring(6,10)));
		resumenAtencionesForm.setDetalleTriage("convenio", triage.getConvenio().getValue());
		resumenAtencionesForm.setDetalleTriage("otroConvenio", triage.getOtro_convenio());
		resumenAtencionesForm.setDetalleTriage("tipoAfiliado", triage.getTipo_afiliado().getValue());
		resumenAtencionesForm.setDetalleTriage("idCotizante", triage.getId_cotizante());
		resumenAtencionesForm.setDetalleTriage("signosVitales", triage.getSignosVitales());
		resumenAtencionesForm.setDetalleTriage("numSignos", triage.getNumSignos()+"");
		resumenAtencionesForm.setDetalleTriage("antecedentes", triage.getAntecedentes());
		resumenAtencionesForm.setDetalleTriage("motivoConsulta", triage.getMotivo_consulta());
		resumenAtencionesForm.setDetalleTriage("signoSintoma", triage.getSignosintoma());
		resumenAtencionesForm.setDetalleTriage("categoriaTriage", triage.getCategorias_triage().getValue());
		resumenAtencionesForm.setDetalleTriage("colorNombre", triage.getColornombre());
		resumenAtencionesForm.setDetalleTriage("destino", triage.getDestino().getValue());
		resumenAtencionesForm.setDetalleTriage("nombreSala", triage.getNombresala());
		resumenAtencionesForm.setDetalleTriage("observacionesGenerales", UtilidadTexto.observacionAHTML(triage.getObservaciones_generales()));
		resumenAtencionesForm.setDetalleTriage("usuario", triage.getUsuario());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenDetalleTriage");
	}

	/**
	 * Método que consulta el listado de triage de una paciente
	 * @param con
	 * @param resumenAtencionesForm
	 * @param paciente
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionResumenListadoTriage(Connection con, ResumenAtencionesForm resumenAtencionesForm, PersonaBasica paciente, ActionMapping mapping, UsuarioBasico usuario) 
	{
		
		resumenAtencionesForm.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		resumenAtencionesForm.setListadoTriage(ResumenAtenciones.cargarListadoTriage(con, paciente.getCodigoTipoIdentificacionPersona(), paciente.getNumeroIdentificacionPersona()));
		resumenAtencionesForm.setNumTriage(Integer.parseInt(resumenAtencionesForm.getListadoTriage("numRegistros").toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenListadoTriage");
	}

	/**
	 * Método implementado para consultar el detalle de un articulo en la hoja de administración
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleArticuloAdmin(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping) 
	{
		String fechaInicial = resumenAtencionesForm.getFechaInicial();
		String fechaFinal = resumenAtencionesForm.getFechaFinal();
		String horaInicial = resumenAtencionesForm.getHoraInicial();
		String horaFinal = resumenAtencionesForm.getHoraFinal();
		
		fechaInicial = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
		fechaFinal = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
		
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap campos = new HashMap();
		campos.put("ingreso",resumenAtencionesForm.getIdIngreso());
		campos.put("fechaInicial",fechaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("horaInicial",horaInicial);
		campos.put("horaFinal",horaFinal);
		//se envía el articulo
		
		
		logger.info("------------------**************--------------------");
		int codArticulo=ConstantesBD.codigoNuncaValido;
		if(resumenAtencionesForm.isCuentaAsociada())
		{
			codArticulo=Utilidades.convertirAEntero(resumenAtencionesForm.getAdminMedicamentosAsocio("articulo_"+resumenAtencionesForm.getPosArticulo())+"");
			campos.put("articulo", codArticulo+"");
			int idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
			campos.put("cuenta",idCuentaAsocio+"");
			resumenAtencionesForm.setDetalleArticuloAdmin(mundo.consultarDetalleArticuloAdmin(con,campos));
			
		}
		else
		{
			codArticulo=Utilidades.convertirAEntero(resumenAtencionesForm.getAdminMedicamentos("articulo_"+resumenAtencionesForm.getPosArticulo())+"");
			campos.put("articulo", codArticulo+"");
			int idCuenta = resumenAtencionesForm.getCuenta();
			campos.put("cuenta",idCuenta+"");
			resumenAtencionesForm.setDetalleArticuloAdmin(mundo.consultarDetalleArticuloAdmin(con,campos));
			
		}
		resumenAtencionesForm.setDetalleArticuloAdminE(mundo.consultarDetalleArticuloAdminE(con, codArticulo,resumenAtencionesForm.getIdIngreso()));
		
		//logger.info("MAPA DETALLEE ARTICULO ADMINN>>>>>>>>>>>>>"+resumenAtencionesForm.getDetalleArticuloAdmin());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleArticuloAdmin");
	}

	/**
	 * Método implementado para cargar el detalle de las consultas realizadas al paciente
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleCitas(Connection con, ResumenAtencionesForm forma, ActionMapping mapping) 
	{
		String fechaInicialBD = UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial());
		String fechaFinalBD = UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal());
		String horaInicialBD = forma.getHoraInicial();
		String horaFinalBD = forma.getHoraFinal();
		
		ResumenAtenciones resumenAt = new ResumenAtenciones();
		
		forma.setSolicitudesCita(resumenAt.listarSolicitudesCitas(con, forma.getMapaValidacionesInfoCuentas("idCuenta_"+forma.getIndiceMapaValInfoCuentas()).toString(), fechaInicialBD, fechaFinalBD, horaInicialBD, horaFinalBD));
		forma.setCitas(resumenAt.listarCitas(con, forma.getMapaValidacionesInfoCuentas("idCuenta_"+forma.getIndiceMapaValInfoCuentas()).toString(), fechaInicialBD, fechaFinalBD, horaInicialBD, horaFinalBD));
		forma.setNumCitas(Integer.parseInt(forma.getSolicitudesCita().get("numRegistros").toString()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleServiciosCitas");
	}

	/**
	 * Método que carga información de la respuesta de cirugías
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionDetalleCirugias(Connection con, ResumenAtencionesForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones  ();
		Integer numeroRegistros=0;
		
		String fechaInicialBD = UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial());
		String fechaFinalBD = UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal());
		String horaInicialBD = forma.getHoraInicial();
		String horaFinalBD = forma.getHoraFinal();
		int codigoInstitucionInt = usuario.getCodigoInstitucionInt();
		
		if(forma.isCuentaAsociada())
		{
			//---------- Método que consulta las peticiones y números de solicitud que existen de acuerdo a los parámetros de búsqueda --------------//
			forma.setMapaCodigosPeticionCirugiaAsocio(mundo.consultarPeticionesCirugiaImpresionHc(con, String.valueOf(forma.getCuenta()), forma.getCuentaAsocio(), fechaInicialBD, fechaFinalBD, horaInicialBD, horaFinalBD, "",""));
			//---------- Se crea una cadena con los códigos de petición separados por coma para la consulta de las secciones -----------//
			String listadoPeticiones="";
			String listadoSolicitudes="";
			
	        if(forma.getMapaCodigosPeticionCirugiaAsocio() != null)
	        {
	        	listadoPeticiones=obtenerListaSeparadasComa(forma.getMapaCodigosPeticionCirugiaAsocio(),"codigo_peticion_");
	        	listadoSolicitudes=obtenerListaSeparadasComa(forma.getMapaCodigosPeticionCirugiaAsocio(),"numero_solicitud_");
	        }
	        //----------Si no es vacío se consultan cada una de las secciones de hoja quirúrgica y hoja de anestesia -----//
	     
	        
	        
	        List<DtoCodigosCirugiaPeticiones> dtoPeticiones= new ArrayList<DtoCodigosCirugiaPeticiones>();
	        numeroRegistros=Integer.valueOf(String.valueOf(forma.getMapaCodigosPeticionCirugiaAsocio().get("numRegistros")));
	        
	        
	        for (int i = 0; i < numeroRegistros; i++) {
	        	DtoCodigosCirugiaPeticiones dtoTemp = new DtoCodigosCirugiaPeticiones();
	        	dtoTemp.setOrden(String.valueOf(forma.getMapaCodigosPeticionCirugiaAsocio().get("orden_"+i)));
	        	dtoTemp.setFechaOrden(String.valueOf(forma.getMapaCodigosPeticionCirugiaAsocio().get("fecha_solicitud_"+i)));
	        	dtoTemp.setFechaCirugia(String.valueOf(forma.getMapaCodigosPeticionCirugiaAsocio().get("fecha_cirugia_"+i)));
	        	dtoTemp.setMedicoSolicita(String.valueOf(forma.getMapaCodigosPeticionCirugiaAsocio().get("medico_solicita_"+i)));
	        	dtoTemp.setEstadoMedico(String.valueOf(forma.getMapaCodigosPeticionCirugiaAsocio().get("estadomedico_"+i)));
	        	dtoTemp.setNumeroSolicitud(String.valueOf(forma.getMapaCodigosPeticionCirugiaAsocio().get("numero_solicitud_"+i)));
	        	dtoPeticiones.add(dtoTemp);
			}
	        
	        forma.setListaDtoSOlcitudes(dtoPeticiones);
	        
	        
	        
	        if(UtilidadCadena.noEsVacio(listadoPeticiones))
	        {
	        	//------------ Se consultan los posibles encabezados de la hoja de anestesia -----------------//
	        	forma.setMapaEncabezadosHojaAnestesiaAsocio(mundo.consultarEncabezadosHojaAnestesia(con, listadoPeticiones));
	        	
	        	//------------- Se consultan los exámenes de laboratorio de la preanestesia ----------------------//
	        	forma.setMapaExamenesLaboratorioPreanestesiaAsocio(mundo.consultarExamenesLaboratorioPreanestesia(con, listadoPeticiones));
	        	
	        	//------------ Se consulta el histórico de los exámenes físico de tipo text ------------------------//
	        	forma.setMapaHistoExamenesFisicosTextAsocio(mundo.consultarHistoExamenesFisicos(con, listadoPeticiones, true));
	        	
	        	//------------ Se consulta el histórico de los exámenes físico de tipo text area ------------------------//
	        	forma.setMapaHistoExamenesFisicosTextAreaAsocio(mundo.consultarHistoExamenesFisicos(con, listadoPeticiones, false));
	        	
	        	//------------ Se consulta el histórico de las conclusiones de preanestesia------------------------//
	        	forma.setMapaHistoConclusionesAsocio(mundo.consultarHistoConclusiones(con, listadoPeticiones));
	        	
	        	//------------------ Hoja Anestesia ------------------------------------------
	        	cargarHojaAnestesia(con, listadoSolicitudes, forma,usuario);	 
	        	
	        	//------------------ Hoja Quirurgica ------------------------------------------
	        	cargarHojaQuirurgica(con, listadoSolicitudes, forma,usuario);  
	        }
		}
		else
		{
			//---------- Método que consulta las peticiones y números de solicitud que existen de acuerdo a los parámetros de búsqueda --------------//
			forma.setMapaCodigosPeticionCirugia(mundo.consultarPeticionesCirugiaImpresionHc(con, forma.getCuenta()+"", "0", fechaInicialBD, fechaFinalBD, horaInicialBD, horaFinalBD, "",""));
			//---------- Se crea una cadena con los códigos de petición separados por coma para la consulta de las secciones -----------//
			String listadoPeticiones="";
			String listadoSolicitudes="";
			
	        if(forma.getMapaCodigosPeticionCirugia() != null)
	        {
	        	listadoPeticiones=obtenerListaSeparadasComa(forma.getMapaCodigosPeticionCirugia(),"codigo_peticion_");
	        	listadoSolicitudes=obtenerListaSeparadasComa(forma.getMapaCodigosPeticionCirugia(),"numero_solicitud_");
	        }
	        //----------Si no es vacío se consultan cada una de las secciones de hoja quirúrgica y hoja de anestesia -----//
	       
	        List<DtoCodigosCirugiaPeticiones> dtoPeticiones= new ArrayList<DtoCodigosCirugiaPeticiones>();
	        numeroRegistros=Integer.valueOf(String.valueOf(forma.getMapaCodigosPeticionCirugia().get("numRegistros")));
	        
	        
	        for (int i = 0; i < numeroRegistros; i++) {
	        	DtoCodigosCirugiaPeticiones dtoTemp = new DtoCodigosCirugiaPeticiones();
	        	dtoTemp.setOrden(String.valueOf(forma.getMapaCodigosPeticionCirugia().get("orden_"+i)));
	        	dtoTemp.setFechaOrden(String.valueOf(forma.getMapaCodigosPeticionCirugia().get("fecha_solicitud_"+i)));
	        	dtoTemp.setFechaCirugia(String.valueOf(forma.getMapaCodigosPeticionCirugia().get("fecha_cirugia_"+i)));
	        	dtoTemp.setMedicoSolicita(String.valueOf(forma.getMapaCodigosPeticionCirugia().get("medico_solicita_"+i)));
	        	dtoTemp.setEstadoMedico(String.valueOf(forma.getMapaCodigosPeticionCirugia().get("estadomedico_"+i)));
	        	dtoTemp.setNumeroSolicitud(String.valueOf(forma.getMapaCodigosPeticionCirugia().get("numero_solicitud_"+i)));
	        	dtoPeticiones.add(dtoTemp);
			}
	        
	        forma.setListaDtoSOlcitudes(dtoPeticiones);
	        if(UtilidadCadena.noEsVacio(listadoPeticiones))
	        {
	        	//------------ Se consultan los posibles encabezados de la hoja de anestesia -----------------//
	        	forma.setMapaEncabezadosHojaAnestesia(mundo.consultarEncabezadosHojaAnestesia(con, listadoPeticiones));
	        	
	        	//------------- Se consultan los exámenes de laboratorio de la preanestesia ----------------------//
	        	forma.setMapaExamenesLaboratorioPreanestesia(mundo.consultarExamenesLaboratorioPreanestesia(con, listadoPeticiones));
	        	
	        	//------------ Se consulta el histórico de los exámenes físico de tipo text ------------------------//
	        	forma.setMapaHistoExamenesFisicosText(mundo.consultarHistoExamenesFisicos(con, listadoPeticiones, true));
	        	
	        	//------------ Se consulta el histórico de los exámenes físico de tipo text area ------------------------//
	        	forma.setMapaHistoExamenesFisicosTextArea(mundo.consultarHistoExamenesFisicos(con, listadoPeticiones, false));
	        	
	        	//------------ Se consulta el histórico de las conclusiones de preanestesia------------------------//
	        	forma.setMapaHistoConclusiones(mundo.consultarHistoConclusiones(con, listadoPeticiones));
	        		        	
	        	//------------------ Hoja Anestesia ------------------------------------------
	        	cargarHojaAnestesia(con, listadoSolicitudes, forma,usuario);	        	     	
	        	
	        	//------------------ Hoja Quirurgica ------------------------------------------
	        	cargarHojaQuirurgica(con, listadoSolicitudes, forma,usuario);  
	        }
	        
	        
	        
	        
	        
	        
	        
	        
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleListadoCirugias");
	}
	
	/**
	 * Método que carga informacion de la hoja Qx (SECCION CIRUGIAS)
	 * @param con
	 * @param solicitudes
	 * @param forma
	 */
	private void cargarHojaQuirurgica(Connection con, String solicitudes, ResumenAtencionesForm forma,UsuarioBasico usuario) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();
		HashMap mx = new HashMap();
		HashMap hojaQx = new HashMap();

		mp.put("solicitudes", solicitudes); 
		hojaQx.clear();
		
		//-- Consultar la informacion de encabezado.	
		
			mp.put("nroConsulta","28");
			mx = mundo.consultarInformacion(con, mp);
			hojaQx.put("numRegEnca", mx.get("numRegistros")+"");
			mx.remove("numRegistros");
			hojaQx.putAll(mx);

		//-- Consultar los diagnosticos del encabezado 	

			mp.put("nroConsulta","29");
			mx = mundo.consultarInformacion(con, mp);
			hojaQx.put("numRegDiag", mx.get("numRegistros")+"");
			mx.remove("numRegistros");
			hojaQx.putAll(mx);

		//-- Consultar la informacion de las cirugias (SERVICIOS) 	

			mp.put("nroConsulta","30");
			mp.put("institucion",usuario.getCodigoInstitucion());
			mx = mundo.consultarInformacion(con, mp);
			hojaQx.put("numRegCir", mx.get("numRegistros")+"");
			mx.remove("numRegistros");
			hojaQx.putAll(mx);

		//-- Consultar la informacion de las cirugias (SERVICIOS) 	

			mp.put("nroConsulta","31");
			mx = mundo.consultarInformacion(con, mp);
			hojaQx.put("numRegDs", mx.get("numRegistros")+"");
			mx.remove("numRegistros");
			hojaQx.putAll(mx);

		//-- Consultar la informacion Quirurgica de Todas Las Hojas 	

			mp.put("nroConsulta","32");
			mx = mundo.consultarInformacion(con, mp);
			hojaQx.put("numRegIq", mx.get("numRegistros")+"");
			mx.remove("numRegistros");
			hojaQx.putAll(mx);

		//-- Consultar la informacion de Observaciones Generales y Patologias 	

			mp.put("nroConsulta","33");
			mx = mundo.consultarInformacion(con, mp);
			hojaQx.put("numRegOb", mx.get("numRegistros")+"");
			mx.remove("numRegistros");
			hojaQx.putAll(mx);
		
		if(forma.isCuentaAsociada())
			forma.setMapaHojaQuirurAsocio(hojaQx);
		else
			forma.setMapaHojaQuirur(hojaQx);
	}

	/**
	 * Método que carga los valores de los signos vitales (SECCION CIRUGIAS)
	 * @param con
	 * @param listadoSolicitudes
	 * @param forma
	 * @param mundo
	 */
	private void cargarValoresSignosVitales(Connection con, String listadoSolicitudes, ResumenAtencionesForm forma, ImpresionResumenAtenciones mundo) 
	{
		Collection colValoresSVitales=null;
    	
    	try
		{
    		colValoresSVitales=mundo.consultarHistoSignosVitales (con, listadoSolicitudes, 2);
    		Iterator ite=colValoresSVitales.iterator();
    		int cont=0;
    		for (int i=0; i<colValoresSVitales.size(); i++)
    		{
    			if (ite.hasNext())
    			{
    				HashMap colVSVital=(HashMap) ite.next();
    				if(colVSVital.get("numero_tiempo") != null && !colVSVital.get("numero_tiempo").equals("") && colVSVital.get("signo_vital_inst") != null  && !colVSVital.get("signo_vital_inst").equals(""))
    				{
    					if(forma.isCuentaAsociada())
    					{
    						forma.setMapaHistoSignosVitalesAsocio("valorSigVital_"+colVSVital.get("signo_vital_inst")+"_"+colVSVital.get("numero_tiempo")+"_"+colVSVital.get("numero_solicitud"), colVSVital.get("valor_signo_vital"));
        					forma.setMapaHistoSignosVitalesAsocio("numero_solicitudValor_"+cont, colVSVital.get("numero_solicitud"));
    					}
    					else
    					{
    						forma.setMapaHistoSignosVitales("valorSigVital_"+colVSVital.get("signo_vital_inst")+"_"+colVSVital.get("numero_tiempo")+"_"+colVSVital.get("numero_solicitud"), colVSVital.get("valor_signo_vital"));
        					forma.setMapaHistoSignosVitales("numero_solicitudValor_"+cont, colVSVital.get("numero_solicitud"));
    					}
    					
    					cont++;
    				}
    			}//if
    		}//for
    		if(forma.isCuentaAsociada())
    			forma.setMapaHistoSignosVitalesAsocio("numRegistrosValores",(Object)cont);
    		else
    			forma.setMapaHistoSignosVitales("numRegistrosValores",(Object)cont);
		}//try
    	catch(Exception e)
		{
		  logger.warn("Error al Consultar los valores para cada signo vital en cada tiempo" +e.toString());
		  colValoresSVitales=null;
		}
	}

	
	/**
	 * Método que obtiene la lista de las peticiones o solicitudes, enviando el indice
	 * organiza en un String separado por comas y poderlo enviar a la consulta de cada una de las secciones
	 * @param mapaCodigosPeticionCirugia
	 * @param indiceMapa
	 * @return 
	 */
	private String obtenerListaSeparadasComa(HashMap mapaCodigosPeticionCirugia, String indice) 
	{
	StringBuffer listadoPeticiones=new StringBuffer();
	if(mapaCodigosPeticionCirugia != null && UtilidadCadena.noEsVacio(mapaCodigosPeticionCirugia.get("numRegistros")+""))
	{
		int numRegistros=Integer.parseInt(mapaCodigosPeticionCirugia.get("numRegistros")+"");
		int cont=0;
		
		for(int i=0; i<numRegistros; i++)
		{
			if(cont==0)
			{
				listadoPeticiones.append(mapaCodigosPeticionCirugia.get(indice+i)+"");
			}
			else
			{
				listadoPeticiones.append(","+mapaCodigosPeticionCirugia.get(indice+i)+"");
			}
		  cont++;

		}//for
		return listadoPeticiones.toString();
	}//if mapaCodigosPeticionCirugia!=nul
	else
		return "";
	}

	/**
	 * Método que carga informacion de los antecedentes
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	private ActionForward accionDetalleAntecedentes(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario) 
	{
		cargarAntecedentesAlergias(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesFamiliares(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesFamiliaresOculares(con,paciente,usuario,resumenAtencionesForm,1);
		cargarAntecedentesPersonalesOculares(con,paciente,usuario,resumenAtencionesForm,1);
		cargarAntecedentesGinecoObstetricos(con,paciente,usuario,resumenAtencionesForm,1);
		cargarAntecedentesMedicamentos(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesMedicos(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesTransfusionales(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesVacunas(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesToxicos(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesVarios(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesPediatricos(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesOdontologicos(con,paciente,usuario,resumenAtencionesForm,1);
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleAntecedentes");
	}

	/**
	 * Método que carga completamente la información de los antecedentes
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	private ActionForward accionPopupResumenAntecedentes(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario) 
	{
		resumenAtencionesForm.setEsPopupAntecedentes(true);
		cargarAntecedentesAlergias(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesFamiliares(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesFamiliaresOculares(con,paciente,usuario,resumenAtencionesForm,0);
		cargarAntecedentesPersonalesOculares(con,paciente,usuario,resumenAtencionesForm,0);
		cargarAntecedentesGinecoObstetricos(con,paciente,usuario,resumenAtencionesForm,0);
		cargarAntecedentesMedicamentos(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesMedicos(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesTransfusionales(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesVacunas(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesToxicos(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesVarios(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesPediatricos(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesOdontologicos(con,paciente,usuario,resumenAtencionesForm,0);

		cargarAntecedentesAlergias(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesFamiliares(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesFamiliaresOculares(con,paciente,usuario,resumenAtencionesForm,1);
		cargarAntecedentesPersonalesOculares(con,paciente,usuario,resumenAtencionesForm,1);
		cargarAntecedentesGinecoObstetricos(con,paciente,usuario,resumenAtencionesForm,1);
		cargarAntecedentesMedicamentos(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesMedicos(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesTransfusionales(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesVacunas(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesToxicos(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesVarios(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesPediatricos(con,paciente,resumenAtencionesForm,1);
		cargarAntecedentesOdontologicos(con,paciente,usuario,resumenAtencionesForm,1);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleAntecedentes");
	}

	/**
	 * Método que carga informacion de la respuesta/interpretacion de procedimientos
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @param UsuarioBasico usuario
	 * @return
	 */
	private ActionForward accionDetalleProcedimientos(
			Connection con, 
			ResumenAtencionesForm resumenAtencionesForm, 
			ActionMapping mapping,
			UsuarioBasico usuario) 
	{		
		String fechaInicial = resumenAtencionesForm.getFechaInicial();
		String fechaFinal = resumenAtencionesForm.getFechaFinal();
		String horaInicial = resumenAtencionesForm.getHoraInicial();
		String horaFinal = resumenAtencionesForm.getHoraFinal();
		
		fechaInicial = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
		fechaFinal = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
		
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones  ();
		HashMap campos = new HashMap();
		campos.put("ingreso",resumenAtencionesForm.getIdIngreso());
		campos.put("fechaInicial",fechaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("horaInicial",horaInicial);
		campos.put("horaFinal",horaFinal);
		
		if(resumenAtencionesForm.isCuentaAsociada())
		{
			int idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
			campos.put("cuenta",idCuentaAsocio+"");
			resumenAtencionesForm.setRespuestaInterpretacionProcedimientosAsocio(mundo.consultarRespuestaInterpretacionProcedimientos(con,campos));			
						
		}
		else
		{
			int idCuenta = resumenAtencionesForm.getCuenta();
			campos.put("cuenta",idCuenta+"");
			resumenAtencionesForm.setRespuestaInterpretacionProcedimientos(mundo.consultarRespuestaInterpretacionProcedimientos(con,campos));			
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleProcedimientos");
	}


	/**
	 * Metodo que carga informacion sobre la respuesta/interpretacion de interconsultas
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleInterconsultas(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping) 
	{
		String fechaInicial = resumenAtencionesForm.getFechaInicial();
		String fechaFinal = resumenAtencionesForm.getFechaFinal();
		String horaInicial = resumenAtencionesForm.getHoraInicial();
		String horaFinal = resumenAtencionesForm.getHoraFinal();
		
		fechaInicial = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
		fechaFinal = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
		
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones  ();
		HashMap campos = new HashMap();
		campos.put("ingreso",resumenAtencionesForm.getIdIngreso());
		campos.put("fechaInicial",fechaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("horaInicial",horaInicial);
		campos.put("horaFinal",horaFinal);
		
		if(resumenAtencionesForm.isCuentaAsociada())
		{
			int idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
			campos.put("cuenta",idCuentaAsocio+"");
			int[] codigosEstados={ConstantesBD.codigoEstadoHCRespondida, ConstantesBD.codigoEstadoHCInterpretada};
			resumenAtencionesForm.setSolicitudesInterConsultaAsocio(mundo.obtenerSolicitudesEstadoTipoFiltro(con, codigosEstados, ConstantesBD.codigoTipoSolicitudInterconsulta,campos));
			
		}
		else
		{
			int idCuenta = resumenAtencionesForm.getCuenta();
			campos.put("cuenta",idCuenta+"");
			int[] codigosEstados={ConstantesBD.codigoEstadoHCRespondida, ConstantesBD.codigoEstadoHCInterpretada};
			resumenAtencionesForm.setSolicitudesInterConsulta(mundo.obtenerSolicitudesEstadoTipoFiltro(con, codigosEstados, ConstantesBD.codigoTipoSolicitudInterconsulta,campos));
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleInterconsultas");
	}

	/**
	 * Método que carga información del consumo de insumos
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleInsumos(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping) 
	{
		String fechaInicial = resumenAtencionesForm.getFechaInicial();
		String fechaFinal = resumenAtencionesForm.getFechaFinal();
		String horaInicial = resumenAtencionesForm.getHoraInicial();
		String horaFinal = resumenAtencionesForm.getHoraFinal();
		
		fechaInicial = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
		fechaFinal = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
		
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones  ();
		HashMap campos = new HashMap();
		campos.put("ingreso",resumenAtencionesForm.getIdIngreso());
		campos.put("fechaInicial",fechaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("horaInicial",horaInicial);
		campos.put("horaFinal",horaFinal);
		
		if(resumenAtencionesForm.isCuentaAsociada())
		{
			int idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
			campos.put("cuenta",idCuentaAsocio+"");
			resumenAtencionesForm.setInsumosAsocio(mundo.consultarInsumos(con,campos));
			
		}
		else
		{
			int idCuenta = resumenAtencionesForm.getCuenta();
			campos.put("cuenta",idCuenta+"");
			resumenAtencionesForm.setInsumos(mundo.consultarInsumos(con,campos));
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleInsumos");
	}

	/**
	 * Método que carga informacion de la administracion de medicamentos
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleAdminMedicamentos(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping) 
	{
		//si se está regresando del detalle no es necesario volver a consultar
		if(!resumenAtencionesForm.getEstado().equals("volverAdminMedicamentos"))
		{
		
			String fechaInicial = resumenAtencionesForm.getFechaInicial();
			String fechaFinal = resumenAtencionesForm.getFechaFinal();
			String horaInicial = resumenAtencionesForm.getHoraInicial();
			String horaFinal = resumenAtencionesForm.getHoraFinal();
			
			fechaInicial = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
			fechaFinal = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
			
			ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones  ();
			HashMap campos = new HashMap();
			campos.put("ingreso",resumenAtencionesForm.getIdIngreso());
			campos.put("fechaInicial",fechaInicial);
			campos.put("fechaFinal",fechaFinal);
			campos.put("horaInicial",horaInicial);
			campos.put("horaFinal",horaFinal);
			
			if(resumenAtencionesForm.isCuentaAsociada())
			{
				int idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
				campos.put("cuenta",idCuentaAsocio+"");
				resumenAtencionesForm.setAdminMedicamentosAsocio(mundo.consultarAdminMedicamentos(con,campos,ConstantesBD.codigoNuncaValido));
				
			}
			else
			{
				int idCuenta = resumenAtencionesForm.getCuenta();
				campos.put("cuenta",idCuenta+"");
				resumenAtencionesForm.setAdminMedicamentos(mundo.consultarAdminMedicamentos(con,campos,ConstantesBD.codigoNuncaValido));
				
			}
		}
		else
			resumenAtencionesForm.setEstado("detalleAdminMedicamentos");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleAdminMedicamentos");
	}
	
	/**
	 * Método que carga informacion del perfil de farmacoterapia
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetallePerfilFarmacoterapia(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping) 
	{
		logger.info("---- ID INGRESO "+resumenAtencionesForm.getIdIngreso());
		resumenAtencionesForm.setPerfilFarmacoterapiaMap(UtilidadesHistoriaClinica.obtenerPerfilFarmacoterapia(con, Utilidades.convertirAEntero(resumenAtencionesForm.getIdIngreso())));
		resumenAtencionesForm.setPerfilFarmacoterapiaMesMap(cargarDatosMesActualPerfilFarmacoterapia(resumenAtencionesForm.getPerfilFarmacoterapiaMap()));
		
		//Utilidades.imprimirMapa(resumenAtencionesForm.getPerfilFarmacoterapiaMap());
		//Utilidades.imprimirMapa(resumenAtencionesForm.getPerfilFarmacoterapiaMesMap());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detallePerfilFarmacoterapia");
	}
	
	/**
	 * 
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCambiarMesPerfilFarmacoterapia(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping) 
	{
		resumenAtencionesForm.setPerfilFarmacoterapiaMesMap(cargarDatosMesActualPerfilFarmacoterapia(resumenAtencionesForm.getPerfilFarmacoterapiaMap()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detallePerfilFarmacoterapia");
	}
	
	/**
	 * 
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @param paciente 
	 * @return
	 */
	private void accionImprimirPerfilFarmacoterapia(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente, String nReportes) 
	{
		// Se imprime un solo reporte del mes actual
		if (nReportes.equals(ConstantesBD.acronimoNo)){
			String nombreRptDesign = "perfilDeFarmacoterapia.rptdesign";
			
			//EstadisticasServicios mundo = new EstadisticasServicios();
			
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			String viaIngreso="";
			String tipoPaciente="";
			Vector v;
			
			//***************** INFORMACIÓN DEL CABEZOTE
		    DesignEngineApi comp; 
		    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"historiaClinica/",nombreRptDesign);
		    
		    // Logo
		    comp.insertImageHeaderOfMasterPage1(1, 2, ins.getLogoReportes());
		    
		    // Nombre Institución, titulo y rango de fechas
		    comp.insertGridHeaderOfMasterPageWithName(0,0,1,2, "titulo");
		    v=new Vector();
		    v.add(ins.getRazonSocial());
		    v.add("PERFIL DE FARMACOTERAPIA - "+resumenAtencionesForm.getPerfilFarmacoterapiaMap("mes_actual"));
		    comp.insertLabelInGridOfMasterPage(0,0,v);
		    
		    // Paciente, entidad, nro Ingreso, fecha ingreso
		    comp.insertGridHeaderOfMasterPageWithName(1,0,1,4, "info1");
		    v=new Vector();
		    v.add("Paciente: "+paciente.getNombrePersona());
		    v.add("Entidad: "+paciente.getConvenioPersonaResponsable());
		    v.add("Nro Ingreso: "+paciente.getConsecutivoIngreso());
		    v.add("Fecha Ingreso: "+paciente.getFechaIngreso());
		    comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,"left");
		    
		    // Id, sexo, edad, cama
		    comp.insertGridHeaderOfMasterPageWithName(1,1,1,4, "info2");
		    v=new Vector();
		    v.add("Nro. Doc: "+paciente.getTipoIdentificacionPersona(false)+" "+paciente.getNumeroIdentificacionPersona());
		    v.add("sexo: "+paciente.getSexo());
		    v.add("Edad: "+paciente.getEdadDetallada());
		    v.add("Cama: "+paciente.getCama());
		    comp.insertLabelInGridOfMasterPageWithProperties(1,1,v,"left");
		    
		    // Fecha hora de proceso y usuario
		    comp.insertLabelInGridPpalOfFooter(0,1,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
		    comp.insertLabelInGridPpalOfFooter(0,0,"Usuario: "+usuario.getLoginUsuario());
		    //******************
			
		    //***************** NUEVA CONSULTA DEL REPORTE
		    String consultaSQL = ConsultasBirt.perfilDeFarmacoterapia(Utilidades.convertirAEntero(resumenAtencionesForm.getIdIngreso()), resumenAtencionesForm.getPerfilFarmacoterapiaMap("mes_actual").toString());
		   
		   //****************** MODIFICAR CONSULTA
		    comp.obtenerComponentesDataSet("dataSet");
		   String newquery=comp.obtenerQueryDataSet();
		   comp.modificarQueryDataSet(consultaSQL);
		   
		   logger.info("Query >>> "+consultaSQL);
		   
		   //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		   comp.updateJDBCParameters(newPathReport);        
		   
		   if(!newPathReport.equals(""))
		    {
		    	request.setAttribute("isOpenReport", "true");
		    	request.setAttribute("newPathReport", newPathReport);
		    }	
		}
		// Se imprimen los reportes de todos los meses en los cuales se le haya administrado medicamentos al paciente
		else
		{
		
			
		}
	}
	
	/**
	 * Método que carga en un nuevo mapa la información de un solo mes
	 * @param mapaCompleto
	 * @param mesActual
	 * @return
	 */
	private HashMap cargarDatosMesActualPerfilFarmacoterapia(HashMap mapaCompleto){
		HashMap mapaMes = new HashMap();
		int numRegistros=0;
		int cantidadmes=0;
		
		
		HashMap mapaFilas = new HashMap();
		mapaFilas.put("numRegistros", 0);
		int numFilas=0;
		String codAxioma="";
		String dosis="";
		String frecuencia="";
		String via="";
		
		String dia="";
		
		// dias del mes para los cuales hay informacion separados por el separador split
		String dias="";
		
		for(int i=0; i<Utilidades.convertirAEntero(mapaCompleto.get("numRegistros").toString()); i++){
			if (mapaCompleto.get("mes_"+i).toString().equals(mapaCompleto.get("mes_actual").toString())){
				mapaMes.put("via_"+numRegistros, mapaCompleto.get("via_"+i));
				mapaMes.put("dia_"+numRegistros, mapaCompleto.get("dia_"+i));
				mapaMes.put("codigo_art_"+numRegistros, mapaCompleto.get("codigo_art_"+i));
				mapaMes.put("num_dias_"+numRegistros, mapaCompleto.get("num_dias_"+i));
				mapaMes.put("cod_interfaz_art_"+numRegistros, mapaCompleto.get("cod_interfaz_art_"+i));
				mapaMes.put("frecuencia_"+numRegistros, mapaCompleto.get("frecuencia_"+i));
				mapaMes.put("mes_"+numRegistros, mapaCompleto.get("mes_"+i));
				mapaMes.put("dosis_"+numRegistros, mapaCompleto.get("dosis_"+i));
				mapaMes.put("nombre_art_"+numRegistros, mapaCompleto.get("nombre_art_"+i));
				mapaMes.put("cantidad_"+numRegistros, mapaCompleto.get("cantidad_"+i));
				numRegistros++;
				
				// mapa filas
				if(!ResumenAtenciones.existeRegistroMapa(
						mapaFilas,
						mapaCompleto.get("codigo_art_"+i).toString(),
						mapaCompleto.get("dosis_"+i).toString(),
						mapaCompleto.get("frecuencia_"+i).toString(),
						mapaCompleto.get("via_"+i).toString()))				
				{
					numFilas=Utilidades.convertirAEntero(mapaFilas.get("numRegistros").toString());
					mapaFilas.put("via_"+numFilas, mapaCompleto.get("via_"+i));
					mapaFilas.put("dia_"+numFilas, mapaCompleto.get("dia_"+i));
					mapaFilas.put("codigo_art_"+numFilas, mapaCompleto.get("codigo_art_"+i));
					mapaFilas.put("num_dias_"+numFilas, mapaCompleto.get("num_dias_"+i));
					mapaFilas.put("cod_interfaz_art_"+numFilas, mapaCompleto.get("cod_interfaz_art_"+i));
					mapaFilas.put("frecuencia_"+numFilas, mapaCompleto.get("frecuencia_"+i));
					mapaFilas.put("mes_"+numFilas, mapaCompleto.get("mes_"+i));
					mapaFilas.put("dosis_"+numFilas, mapaCompleto.get("dosis_"+i));
					mapaFilas.put("nombre_art_"+numFilas, mapaCompleto.get("nombre_art_"+i));
					mapaFilas.put("cantidad_"+numFilas, mapaCompleto.get("cantidad_"+i));
					mapaFilas.put("numRegistros", numFilas+1);
					
					codAxioma = mapaCompleto.get("codigo_art_"+i)+"";
					dosis = mapaCompleto.get("dosis_"+i)+"";
					frecuencia = mapaCompleto.get("frecuencia_"+i)+"";
					via = mapaCompleto.get("via_"+i)+"";
				}
				
				// string columnas (dias)
				if (!dia.equals(mapaCompleto.get("dia_"+i)+"")){
					dia = mapaCompleto.get("dia_"+i).toString();
					dias+= dia+ConstantesBD.separadorSplit;
				}
				
			}
		}
		
		logger.info("Dias -------------- "+dias);
		logger.info("mapaFilas --------- ");
		Utilidades.imprimirMapa(mapaFilas);
		
		mapaMes.put("dias", dias);
		mapaMes.put("mapaFilas", mapaFilas);
		mapaMes.put("numRegistros", numRegistros);
		return mapaMes;
	}
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarPerfilFarmacoterapia(ResumenAtencionesForm forma) {
	
		HashMap mapaAOrdenar = (HashMap)forma.getPerfilFarmacoterapiaMesMap().get("mapaFilas");
		int numReg = Integer.parseInt(mapaAOrdenar.get("numRegistros")+"");
		String[] indices = {"dia_","codigo_art_","num_dias_","cantidad_","dosis_","cod_interfaz_art_","nombre_art_","via_","frecuencia_",""};
		mapaAOrdenar = Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), mapaAOrdenar, numReg);
		forma.setUltimoPatron(forma.getPatronOrdenar());
		mapaAOrdenar.put("numRegistros",numReg+"");
		mapaAOrdenar.put("INDICES_MAPA",indices);
		forma.getPerfilFarmacoterapiaMesMap().put("mapaFilas", mapaAOrdenar);
		
	}
	
	
	/**
	 * Método que carga informacion de la hoja neurologica
	 */
	private ActionForward accionHojaNeurologica(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		String fechaInicial = resumenAtencionesForm.getFechaInicial();
		String fechaFinal = resumenAtencionesForm.getFechaFinal();
		String horaInicial = resumenAtencionesForm.getHoraInicial();
		String horaFinal = resumenAtencionesForm.getHoraFinal();
		
		fechaInicial = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
		fechaFinal = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
		
		RegistroEnfermeria mundoEnfer=new RegistroEnfermeria();
		/*********************************************************************************
		 * 
		 */
			String cuentas = UtilidadesManejoPaciente.obtenerCuentasXIngreso(con,resumenAtencionesForm.getIdIngreso());
			this.accionCargarSeccionHojaNeurologica(con,mundoEnfer,resumenAtencionesForm, usuario.getCodigoInstitucionInt(), cuentas,fechaInicial, fechaFinal, horaInicial, horaFinal);
		/*
		 * 
		 ********************************************************************************/
		
		/*
		if(resumenAtencionesForm.isCuentaAsociada())
		{
			int idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
			String area[] = UtilidadValidacion.getAreaPaciente(con,idCuentaAsocio).split(ConstantesBD.separadorSplit);
			this.accionCargarSeccionHojaNeurologica(con,mundoEnfer,resumenAtencionesForm, usuario.getCodigoInstitucionInt(), Integer.parseInt(area[0]), idCuentaAsocio,fechaInicial, fechaFinal, horaInicial, horaFinal, true);
		}
		else
		{
			int idCuenta = resumenAtencionesForm.getCuenta();
			String area[] = UtilidadValidacion.getAreaPaciente(con,idCuenta).split(ConstantesBD.separadorSplit);
			this.accionCargarSeccionHojaNeurologica(con,mundoEnfer,resumenAtencionesForm, usuario.getCodigoInstitucionInt(), Integer.parseInt(area[0]), idCuenta,fechaInicial, fechaFinal, horaInicial, horaFinal, false);
		}
		*/
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleHojaNeurologica");
	}

	/**
	 * Método que carga la informacion de las notas de enfermeria
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleNotasEnfermeria(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping) 
	{
		String fechaInicial = resumenAtencionesForm.getFechaInicial();
		String fechaFinal = resumenAtencionesForm.getFechaFinal();
		String horaInicial = resumenAtencionesForm.getHoraInicial();
		String horaFinal = resumenAtencionesForm.getHoraFinal();
		
		fechaInicial = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
		fechaFinal = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
		
		RegistroEnfermeria mundoEnfer=new RegistroEnfermeria();
		
		Collection col=mundoEnfer.consultarAnotacionesEnfermeriaImpresionHC(con,UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, resumenAtencionesForm.getIdIngreso()),UtilidadFecha.conversionFormatoFechaABD(fechaInicial),UtilidadFecha.conversionFormatoFechaABD(fechaFinal),horaInicial,horaFinal,"");
		logger.info("\n\n\n\n\n\nEN EL ACTION "+col.size()+"\n\n\n\n\n\n\n");
		resumenAtencionesForm.setHistoricoAnotacionesEnfermeriaAsocio(col);
		/*
		if(resumenAtencionesForm.isCuentaAsociada())
		{
			int idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
			resumenAtencionesForm.setHistoricoAnotacionesEnfermeriaAsocio(mundoEnfer.consultarAnotacionesEnfermeriaImpresionHC(con,idCuentaAsocio,0,UtilidadFecha.conversionFormatoFechaABD(fechaInicial),UtilidadFecha.conversionFormatoFechaABD(fechaFinal),horaInicial,horaFinal,""));
		}
		else
		{
			int idCuenta = resumenAtencionesForm.getCuenta();
			resumenAtencionesForm.setHistoricoAnotacionesEnfermeria(mundoEnfer.consultarAnotacionesEnfermeriaImpresionHC(con,idCuenta,0,UtilidadFecha.conversionFormatoFechaABD(fechaInicial),UtilidadFecha.conversionFormatoFechaABD(fechaFinal),horaInicial,horaFinal,""));
		}
		*/
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleNotasEnfermeria");
	}

	/**
	 * Método que carga informacion de cuidados especiales
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCuidadosEspeciales(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping) 
	{
		String fechaInicial = resumenAtencionesForm.getFechaInicial();
		String fechaFinal = resumenAtencionesForm.getFechaFinal();
		String horaInicial = resumenAtencionesForm.getHoraInicial();
		String horaFinal = resumenAtencionesForm.getHoraFinal();
		
		fechaInicial = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
		fechaFinal = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
		
		RegistroEnfermeria mundoEnfer=new RegistroEnfermeria();
		/*********************************************************************************
		 * 	
		 */
			String cuentas = UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, resumenAtencionesForm.getIdIngreso()); 
			this.accionCargarSeccionCuidadosEspeciales(con,mundoEnfer,resumenAtencionesForm,cuentas,fechaInicial,fechaFinal,horaInicial,horaFinal);
		/*
		 * 
		 *********************************************************************************/
		
		/*
		if(resumenAtencionesForm.isCuentaAsociada())
		{
			int idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
			this.accionCargarSeccionCuidadosEspeciales(con,mundoEnfer,resumenAtencionesForm,idCuentaAsocio,fechaInicial,fechaFinal,horaInicial,horaFinal,true);
		}
		else
		{
			int idCuenta = resumenAtencionesForm.getCuenta();
			this.accionCargarSeccionCuidadosEspeciales(con,mundoEnfer,resumenAtencionesForm,idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal,false);
		}
		*/
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleCuidadosEspeciales");
	}

	/**
	 * Método implementado para cargar infirmacion de cateter y sonda
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionDetalleCateterSonda(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		String fechaInicial = resumenAtencionesForm.getFechaInicial();
		String fechaFinal = resumenAtencionesForm.getFechaFinal();
		String horaInicial = resumenAtencionesForm.getHoraInicial();
		String horaFinal = resumenAtencionesForm.getHoraFinal();
		
		
		RegistroEnfermeria mundoEnfer=new RegistroEnfermeria();
		
		/******************************************************************
		 * 
		 */
		String cuentas = UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, resumenAtencionesForm.getIdIngreso());
		String [] cuentaTmp=cuentas.split(",");
		
		String area[] = UtilidadValidacion.getAreaPaciente(con,Utilidades.convertirAEntero(cuentaTmp[0])).split(ConstantesBD.separadorSplit);
		resumenAtencionesForm.setColCateteresSondaInstitucionCcosto(
			mundoEnfer.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(), cuentas,  3));
	 	resumenAtencionesForm.setColCateteresSondaInstitucionCcosto(
	 		Utilidades.coleccionSinRegistrosRepetidos(resumenAtencionesForm.getColCateteresSondaInstitucionCcosto(), "codigo_tipo"));
	 	resumenAtencionesForm.setCateterSondaFijosHisto(mundoEnfer.consultarCateterSondaFijosHistoImpresionHC(con, cuentas,  UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
	 	if(resumenAtencionesForm.getColCateteresSondaInstitucionCcosto().size()>0)
	 		resumenAtencionesForm.setCateterSondaParamHisto(mundoEnfer.consultarCateterSondaParamHistoImpresionHC(con, cuentas, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
	 	resumenAtencionesForm.setCateterSondaHistoTodos(mundoEnfer.consultarCateterSondaTodosHistoImpresionHC(con, cuentas ,UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
	 	
	 	HashMap mapaCateterHisto=new HashMap();
	 	mapaCateterHisto=formarHistoricoCateterSonda(resumenAtencionesForm,false);
	 	
	 	if(Integer.parseInt(mapaCateterHisto.get("numRegistros")+"")>0) 	
		 	ordenarHistoricoCateterSonda(resumenAtencionesForm, mapaCateterHisto,false);
	 	else
	 		resumenAtencionesForm.setMapaHistoricoCateterSonda(mapaCateterHisto);
		/*
		 * 		
		 *****************************************************************/
		/*
		if(resumenAtencionesForm.isCuentaAsociada())
		{			
			int idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
			String area[] = UtilidadValidacion.getAreaPaciente(con,idCuentaAsocio).split(ConstantesBD.separadorSplit);
			resumenAtencionesForm.setColCateteresSondaInstitucionCcostoAsocio(
				mundoEnfer.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(),  Integer.parseInt(area[0]), idCuentaAsocio, 0, 3));
		 	resumenAtencionesForm.setColCateteresSondaInstitucionCcostoAsocio(
		 		Utilidades.coleccionSinRegistrosRepetidos(resumenAtencionesForm.getColCateteresSondaInstitucionCcostoAsocio(), "codigo_tipo"));
		 	resumenAtencionesForm.setCateterSondaFijosHistoAsocio(mundoEnfer.consultarCateterSondaFijosHistoImpresionHC(con, idCuentaAsocio, 0, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
		 	if(resumenAtencionesForm.getColCateteresSondaInstitucionCcostoAsocio().size()>0)
		 		resumenAtencionesForm.setCateterSondaParamHistoAsocio(mundoEnfer.consultarCateterSondaParamHistoImpresionHC(con, idCuentaAsocio, 0, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
		 	resumenAtencionesForm.setCateterSondaHistoTodosAsocio(mundoEnfer.consultarCateterSondaTodosHistoImpresionHC(con, idCuentaAsocio, 0, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
		 	
		 	HashMap mapaCateterHisto=new HashMap();
		 	mapaCateterHisto=formarHistoricoCateterSonda(resumenAtencionesForm,true);
		 	
		 	if(Integer.parseInt(mapaCateterHisto.get("numRegistros")+"")>0) 	
			 	ordenarHistoricoCateterSonda(resumenAtencionesForm, mapaCateterHisto,true);
		 	else
		 		resumenAtencionesForm.setMapaHistoricoCateterSondaAsocio(mapaCateterHisto);
		}
		else
		{
			int idCuenta =resumenAtencionesForm.getCuenta();
			String area[] = UtilidadValidacion.getAreaPaciente(con,resumenAtencionesForm.getCuenta()).split(ConstantesBD.separadorSplit);
			resumenAtencionesForm.setColCateteresSondaInstitucionCcosto(
				mundoEnfer.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(),  Integer.parseInt(area[0]), idCuenta, 0, 3));
		 	resumenAtencionesForm.setColCateteresSondaInstitucionCcosto(
		 		Utilidades.coleccionSinRegistrosRepetidos(resumenAtencionesForm.getColCateteresSondaInstitucionCcosto(), "codigo_tipo"));
		 	resumenAtencionesForm.setCateterSondaFijosHisto(mundoEnfer.consultarCateterSondaFijosHistoImpresionHC(con, idCuenta, 0, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
		 	if(resumenAtencionesForm.getColCateteresSondaInstitucionCcosto().size()>0)
		 		resumenAtencionesForm.setCateterSondaParamHisto(mundoEnfer.consultarCateterSondaParamHistoImpresionHC(con, idCuenta, 0, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
		 	resumenAtencionesForm.setCateterSondaHistoTodos(mundoEnfer.consultarCateterSondaTodosHistoImpresionHC(con, idCuenta, 0, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
		 	
		 	HashMap mapaCateterHisto=new HashMap();
		 	mapaCateterHisto=formarHistoricoCateterSonda(resumenAtencionesForm,false);
		 	
		 	if(Integer.parseInt(mapaCateterHisto.get("numRegistros")+"")>0) 	
			 	ordenarHistoricoCateterSonda(resumenAtencionesForm, mapaCateterHisto,false);
		 	else
		 		resumenAtencionesForm.setMapaHistoricoCateterSonda(mapaCateterHisto);
		}
		*/
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleCateterSonda");
	}

	/**
	 * Método para cargar la informacion del soporte repsiratorio
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionSoporteRespiratorio(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping) 
	{
		String fechaInicial = resumenAtencionesForm.getFechaInicial();
		String fechaFinal = resumenAtencionesForm.getFechaFinal();
		String horaInicial = resumenAtencionesForm.getHoraInicial();
		String horaFinal = resumenAtencionesForm.getHoraFinal();
				
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones  ();
		HashMap campos = new HashMap();
		campos.put("ingreso",resumenAtencionesForm.getIdIngreso());
		
		campos.put("fechaInicial",fechaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("horaInicial",horaInicial);
		campos.put("horaFinal",horaFinal);
		
		if(resumenAtencionesForm.isCuentaAsociada())
		{
			campos.put("cuenta",resumenAtencionesForm.getCuentaAsocio());
			resumenAtencionesForm.setSoporteRespiratorioAsocio(mundo.consultarSoporteRespiratorio(con,campos));
		}
		else
		{
			campos.put("cuenta",resumenAtencionesForm.getCuenta());
			resumenAtencionesForm.setSoporteRespiratorio(mundo.consultarSoporteRespiratorio(con,campos));
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleSoporteRespiratorio");
	}

	/**
	 * Método implementado para cargar la informacion de signos vitales
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionDetalleSignosVitales(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		String fechaInicial = resumenAtencionesForm.getFechaInicial();
		String fechaFinal = resumenAtencionesForm.getFechaFinal();
		String horaInicial = resumenAtencionesForm.getHoraInicial();
		String horaFinal = resumenAtencionesForm.getHoraFinal();
		
		
		RegistroEnfermeria mundoEnfer=new RegistroEnfermeria();
		
		/***************************************************************
		 * 
		 */
		
		String cuentas =UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, resumenAtencionesForm.getIdIngreso());
		String [] cuentaTmp=cuentas.split(",");
		
		String area[] = UtilidadValidacion.getAreaPaciente(con,Utilidades.convertirAEntero(cuentaTmp[0])).split(ConstantesBD.separadorSplit);
		resumenAtencionesForm.setSignosVitalesInstitucionCcosto(
	 		mundoEnfer.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(), cuentas, 1));
	 	resumenAtencionesForm.setSignosVitalesInstitucionCcosto(
	 		Utilidades.coleccionSinRegistrosRepetidos(resumenAtencionesForm.getSignosVitalesInstitucionCcosto(), "codigo_tipo"));
	 	resumenAtencionesForm.setSignosVitalesFijosHisto(mundoEnfer.consultarSignosVitalesFijosHistoImpresionHC(con, cuentas, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
	 	if(resumenAtencionesForm.getSignosVitalesInstitucionCcosto().size()>0)
	 		resumenAtencionesForm.setSignosVitalesParamHisto(mundoEnfer.consultarSignosVitalesParamHistoImpresionHC(con, cuentas, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
	 	resumenAtencionesForm.setSignosVitalesHistoTodos(mundoEnfer.consultarSignosVitalesHistoTodosImpresionHC(con, cuentas, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
		
		/*
		 * 
		 *****************************************************************/
		
		/*
		if(resumenAtencionesForm.isCuentaAsociada())
		{
			
			int idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
			String area[] = UtilidadValidacion.getAreaPaciente(con,idCuentaAsocio).split(ConstantesBD.separadorSplit);
			resumenAtencionesForm.setSignosVitalesInstitucionCcostoAsocio(
		 		mundoEnfer.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(), Integer.parseInt(area[0]), idCuentaAsocio, 0, 1));
		 	resumenAtencionesForm.setSignosVitalesInstitucionCcostoAsocio(
		 		Utilidades.coleccionSinRegistrosRepetidos(resumenAtencionesForm.getSignosVitalesInstitucionCcostoAsocio(), "codigo_tipo"));
		 	resumenAtencionesForm.setSignosVitalesFijosHistoAsocio(mundoEnfer.consultarSignosVitalesFijosHistoImpresionHC(con, idCuentaAsocio, 0, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
		 	if(resumenAtencionesForm.getSignosVitalesInstitucionCcostoAsocio().size()>0)
		 		resumenAtencionesForm.setSignosVitalesParamHistoAsocio(mundoEnfer.consultarSignosVitalesParamHistoImpresionHC(con, idCuentaAsocio, 0, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
		 	resumenAtencionesForm.setSignosVitalesHistoTodosAsocio(mundoEnfer.consultarSignosVitalesHistoTodosImpresionHC(con, idCuentaAsocio, 0, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
		}
		else
		{
			int idCuenta = resumenAtencionesForm.getCuenta();
			String area[] = UtilidadValidacion.getAreaPaciente(con,resumenAtencionesForm.getCuenta()).split(ConstantesBD.separadorSplit);
			resumenAtencionesForm.setSignosVitalesInstitucionCcosto(
		 		mundoEnfer.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(), Integer.parseInt(area[0]), idCuenta, 0, 1));
		 	resumenAtencionesForm.setSignosVitalesInstitucionCcosto(
		 		Utilidades.coleccionSinRegistrosRepetidos(resumenAtencionesForm.getSignosVitalesInstitucionCcosto(), "codigo_tipo"));
		 	resumenAtencionesForm.setSignosVitalesFijosHisto(mundoEnfer.consultarSignosVitalesFijosHistoImpresionHC(con, idCuenta, 0, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
		 	if(resumenAtencionesForm.getSignosVitalesInstitucionCcosto().size()>0)
		 		resumenAtencionesForm.setSignosVitalesParamHisto(mundoEnfer.consultarSignosVitalesParamHistoImpresionHC(con, idCuenta, 0, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
		 	resumenAtencionesForm.setSignosVitalesHistoTodos(mundoEnfer.consultarSignosVitalesHistoTodosImpresionHC(con, idCuenta, 0, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaInicial, horaFinal, ""));
		}
		*/
		
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleSignosVitales");
	}


	/**
	 * Método implementado para cargar la informacion de ordenes médicas
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionOrdenesMedicas(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping, HttpServletRequest request) 
	{
		String fechaInicial = resumenAtencionesForm.getFechaInicial();
		String fechaFinal = resumenAtencionesForm.getFechaFinal();
		String horaInicial = resumenAtencionesForm.getHoraInicial();
		String horaFinal = resumenAtencionesForm.getHoraFinal();
		
		fechaInicial = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
		fechaFinal = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
		
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones  ();
		HashMap campos = new HashMap();
		campos.put("ingreso",resumenAtencionesForm.getIdIngreso());
		campos.put("fechaInicial",fechaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("horaInicial",horaInicial);
		campos.put("horaFinal",horaFinal);
		
		ActionErrors errores = new ActionErrors();
		
		if(resumenAtencionesForm.isCuentaAsociada())
		{
			campos.put("cuenta",resumenAtencionesForm.getCuentaAsocio()+"");
			Utilidades.imprimirMapa(campos);
			resumenAtencionesForm.setOrdenesMedicasAsocio(mundo.consultarOrdenMedica(con,campos,null));
			resumenAtencionesForm.setOrdenesCirugiaAsocio(mundo.consultarOrdenesCirugias(con,campos));
			resumenAtencionesForm.setOrdenesMedicamentosAsocio(mundo.consultarSolicitudesMedicamentoInsumos(con,campos));
			resumenAtencionesForm.setOrdenesProcedimientosAsocio(mundo.consultarSolicitudesProcedimientos(con,campos));
			resumenAtencionesForm.setOrdenesInterconsultasAsocio(mundo.consultarSolicitudesInterconsultas(con,campos));
			
			if (	Utilidades.convertirAEntero(resumenAtencionesForm.getOrdenesMedicasAsocio("numRegistros")+"") <= 0 &&
					Utilidades.convertirAEntero(resumenAtencionesForm.getOrdenesCirugiaAsocio("numRegistros")+"") <= 0 &&
					Utilidades.convertirAEntero(resumenAtencionesForm.getOrdenesMedicamentosAsocio("numRegistros")+"") <= 0 &&
					Utilidades.convertirAEntero(resumenAtencionesForm.getOrdenesProcedimientosAsocio("numRegistros")+"") <= 0 &&
					Utilidades.convertirAEntero(resumenAtencionesForm.getOrdenesInterconsultasAsocio("numRegistros")+"") <= 0		)
				errores.add("No se hallaron registros", new ActionMessage("error.turnos.Robservacion"));
		}
		else
		{
			campos.put("cuenta",resumenAtencionesForm.getCuenta()+"");
			Utilidades.imprimirMapa(campos);
			resumenAtencionesForm.setOrdenesMedicas(mundo.consultarOrdenMedica(con,campos,null));
			resumenAtencionesForm.setOrdenesCirugia(mundo.consultarOrdenesCirugias(con,campos));
			resumenAtencionesForm.setOrdenesMedicamentos(mundo.consultarSolicitudesMedicamentoInsumos(con,campos));
			resumenAtencionesForm.setOrdenesProcedimientos(mundo.consultarSolicitudesProcedimientos(con,campos));
			resumenAtencionesForm.setOrdenesInterconsultas(mundo.consultarSolicitudesInterconsultas(con,campos));
			
			if (	Utilidades.convertirAEntero(resumenAtencionesForm.getOrdenesMedicas("numRegistros")+"") <= 0 &&
					Utilidades.convertirAEntero(resumenAtencionesForm.getOrdenesCirugia("numRegistros")+"") <= 0 &&
					Utilidades.convertirAEntero(resumenAtencionesForm.getOrdenesMedicamentos("numRegistros")+"") <= 0 &&
					Utilidades.convertirAEntero(resumenAtencionesForm.getOrdenesProcedimientos("numRegistros")+"") <= 0 &&
					Utilidades.convertirAEntero(resumenAtencionesForm.getOrdenesInterconsultas("numRegistros")+"") <= 0		)
				errores.add("No se hallaron registros", new ActionMessage("error.turnos.Robservacion"));
		}
		
		if (!errores.isEmpty())
			saveErrors(request, errores);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleOrdenesMedicas");
	}

	/**
	 * Método implementado para cargar el detalle del registro de accidentes
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionDetalleRegistroAccidente(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		HashMap criteriosBusqueda= new HashMap();
		criteriosBusqueda.put("idIngreso", resumenAtencionesForm.getIdIngreso());
		criteriosBusqueda.put("codigoInstitucion", usuario.getCodigoInstitucion());
		criteriosBusqueda.put("codigoCentroAtencion", usuario.getCodigoCentroAtencion()+"");
		criteriosBusqueda.put("codigoPersona", usuario.getCodigoPersona()+"");
		
		resumenAtencionesForm.setRegistroAccidentesTransitoReporte1(RegistroAccidentesTransito.generarReporteCertificadoAtencionMedica(con, criteriosBusqueda ));
		
		resumenAtencionesForm.setRegistroAccidentesTransitoReporte2(RegistroAccidentesTransito.generarReporteFUSOAT01(con, criteriosBusqueda));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleRegistroAccidente");
	}

	/**
	 * Método implementado para listar los ingresos del paciente
	 * @param con
	 * @param resumenAtencionesForm
	 * @param resumenAtencionesMundo 
	 * @param paciente
	 * @param usuario 
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ResumenAtencionesForm resumenAtencionesForm, ResumenAtenciones resumenAtencionesMundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		int offs=0;
		if(resumenAtencionesForm.getOffset()!=0)
			offs=resumenAtencionesForm.getOffset();
		resumenAtencionesForm.reset();
		
		if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), ConstantesBD.permisoImprimirDetalleItemHC)){
			resumenAtencionesForm.setTienePermisoImprimirDetalleItemHC(true);
		}
		if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), ConstantesBD.permisoImprimirHC)){
			resumenAtencionesForm.setTienePermisoImprimirHC(true);
		}
		if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), ConstantesBD.permisoConsultarHistoriaDeAtenciones)){
			resumenAtencionesForm.setTienePermisoConsultarHistoriaDeAtenciones(true);
		}
		
		/*
		* Antes MT6313 
		* Tipo Modificacion: Segun incidencia MT6713
		* Autor: Jesús Darío Ríos
		* usuario: jesrioro
		* Fecha: 20/03/2013
		* Descripcion: hacer  la validacaion  de  vieneDeConsultaExterna=true          
		*/
		boolean vieneDeConsultaExterna= UtilidadTexto.getBoolean(request.getParameter("vieneDeConsultaExterna"));
		resumenAtencionesForm.setVieneDeConsultaExterna(vieneDeConsultaExterna);
		//MT6713
		
		
		
		resumenAtencionesForm.setOffset(offs);
		// Obteniendo las cuentas del pacientes (Ingresos)
		resumenAtencionesForm.setCuentas(resumenAtencionesMundo.busquedaCuentas(con, paciente.getCodigoPersona()));
		/**Datos necesarios para la adicion de la opcion de adjuntar Historias Clínicas previas**/
		try
		{
			resumenAtencionesForm.setNumDocumentosAdjuntos(resumenAtencionesMundo.existenAdjuntos(con, paciente.getCodigoPersona()));
			resumenAtencionesForm.setDocumentosAdjuntosGenerados(resumenAtencionesMundo.consultarDocumentosAdjuntos(con, paciente.getCodigoPersona()));
		}
		catch(SQLException e)
		{
			logger.error("Error cargando Documentos Adjuntos en accionEmpezar de ResumenAtencionesAction : "+e);
		}

		if(UtilidadValidacion.existeHojaObstetrica(con, paciente.getCodigoPersona()))
		{
			resumenAtencionesForm.setExisteHojaObstetrica("si");
		}
		else
		{
			resumenAtencionesForm.setExisteHojaObstetrica("no");
		}
		resumenAtencionesForm.setExisteTriage(ResumenAtenciones.existeTriage(con, paciente.getCodigoTipoIdentificacionPersona(), paciente.getNumeroIdentificacionPersona()));

		//*******+SECCIÓN PARA CARGAR LOS ANTECEDENTES*****************************************
		cargarAntecedentesAlergias(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesFamiliares(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesFamiliaresOculares(con,paciente,usuario,resumenAtencionesForm,0);
		cargarAntecedentesPersonalesOculares(con,paciente,usuario,resumenAtencionesForm,0);
		cargarAntecedentesGinecoObstetricos(con,paciente,usuario,resumenAtencionesForm,0);
		cargarAntecedentesMedicamentos(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesMedicos(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesTransfusionales(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesVacunas(con,paciente,resumenAtencionesForm,0);
		//***************************************************************************************


		//*******+SECCIÓN PARA CONSENTIMIENTO INFORMADO*****************************************
		HashMap parametros = new HashMap();
		parametros.put("ingreso",paciente.getCodigoIngreso());
		resumenAtencionesForm.setHistorialConsentimientoInfMap(ResumenAtenciones.consultaHistorialConsentimientoInf(con, parametros));

		if(Integer.parseInt(resumenAtencionesForm.getHistorialConsentimientoInfMap("numRegistros").toString())>0)
			resumenAtencionesForm.setExisteHistorialConsentimiento(ConstantesBD.acronimoSi);
		else
			resumenAtencionesForm.setExisteHistorialConsentimiento(ConstantesBD.acronimoNo);

		//***************************************************************************************


		ArrayList<HashMap> tmp = (ArrayList<HashMap>) resumenAtencionesForm.getCuentas();
		Collection tmp2=new ArrayList();
		Integer tamLista=tmp.size();
		for (int i = 0; i < tamLista; i++) {
			HashMap mapaTmp=tmp.get(i);
			mapaTmp.put("checkIngreso_"+i, "false");
			tmp2.add(mapaTmp);
		}
		
		
		
		
		
		
		
		
		List<DtoIngresoHistoriaClinica> listaDtoHc= new ArrayList<DtoIngresoHistoriaClinica>();
		for (int i = 0; i < tmp2.size(); i++) {
			HashMap mapaIngresosHC = tmp.get(i);
			DtoIngresoHistoriaClinica dtoHC = new DtoIngresoHistoriaClinica();
			dtoHC.setIngreso(false);


			
			
			int idCuentaAsocio = 0;
			int viaIngreso2=Utilidades.convertirAEntero(tmp.get(i).get("codigoviaingreso")+"");
			Boolean Asocio=false;
			
			if(!resumenAtencionesForm.isAsocio()||viaIngreso2==10||viaIngreso2==11||viaIngreso2==12 ||viaIngreso2==15)
			{
				if(viaIngreso2==10||viaIngreso2==11||viaIngreso2==12 || viaIngreso2==15)
				{
					idCuentaAsocio = Integer.parseInt(Utilidades.getCuentaAsociada(con,String.valueOf(tmp.get(i).get("cuenta")),false));
					if(viaIngreso2==15){
						viaIngreso2 = ConstantesBD.codigoViaIngresoUrgencias;
					}
					else{
						viaIngreso2 = ConstantesBD.codigoViaIngresoHospitalizacion;
					}
					Asocio=true;
				}
				else{
					Asocio=false;
				}
			}
			else{
				idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
			}
			
			
			
			
			
			if(Asocio){
				dtoHC.setAsocio("A");
			}
			
			
			
			try {
				dtoHC.setTipoPaciente(UtilidadesHistoriaClinica.obtenerTipoPaciente(con, String.valueOf(tmp.get(i).get("cuenta"))));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			// fehcahora ingreso
			int viaIngreso=Utilidades.convertirAEntero(tmp.get(i).get("codigoviaingreso")+"");
			String fechaIngreso="";
			String fecha="";
			if( tmp.get(i).get("fechaadmisionhospitalaria")!=null && ( viaIngreso==1 || viaIngreso==10 || viaIngreso==11 || viaIngreso==12 || viaIngreso==14)){
				fechaIngreso=UtilidadFecha.conversionFormatoFechaAAp(String.valueOf( tmp.get(i).get("fechaadmisionhospitalaria")));
				dtoHC.setFechaIngreso(fechaIngreso);
				fecha =	UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(tmp.get(i).get("fechaadmisionhospitalaria")))+" - "+UtilidadFecha.convertirHoraACincoCaracteres(String.valueOf(tmp.get(i).get("horaadmisionhospitalaria")));
			}else if( tmp.get(i).get("fechaadmisionurgencias")!=null && ( viaIngreso==3 || viaIngreso==13)){
				fechaIngreso=UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(tmp.get(i).get("fechaadmisionurgencias")));
				dtoHC.setFechaIngreso(fechaIngreso);
				fecha =		UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(tmp.get(i).get("fechaadmisionurgencias"))) +" - "+ UtilidadFecha.convertirHoraACincoCaracteres(String.valueOf(tmp.get(i).get("horaadmisionurgencias")));
			}else if( tmp.get(i).get("fechacuenta")!=null && (viaIngreso==2 || viaIngreso==4) ){
				fechaIngreso=UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(tmp.get(i).get("fechacuenta")));
				dtoHC.setFechaIngreso(fechaIngreso);
				fecha =	UtilidadFecha.conversionFormatoFechaAAp(String.valueOf( tmp.get(i).get("fechacuenta")))+" - "+ UtilidadFecha.convertirHoraACincoCaracteres(String.valueOf(tmp.get(i).get("horacuenta"))).subSequence(0,5);
			}
			dtoHC.setFechaHoraIngreso(fecha);


			//fecha de egreso
			String fechaEgreso="";
			if(tmp.get(i).get("fechaegreso")!=null){
				fechaEgreso=UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(tmp.get(i).get("fechaegreso")));
			}

			if((tmp.get(i).get("horaegreso")!=null)){
				fechaEgreso+=UtilidadFecha.convertirHoraACincoCaracteres(String.valueOf(tmp.get(i).get("horaegreso")));
			}
			dtoHC.setFechaHoraEgreso(fechaEgreso);

			//centro de atencion
			dtoHC.setCentroAtencion(String.valueOf(tmp.get(i).get("centroatencion")));
			try {
			//via de ingreso
			String viaIngresoSwitch="";
			switch(viaIngreso){
			case 1:
				viaIngresoSwitch="HOS";
				
					dtoHC.setEspecialidad(UtilidadesHistoriaClinica.obtenerTipoPacienteHosp(con, String.valueOf(tmp.get(i).get("cuenta"))));
				
				break;
			case 2:
				viaIngresoSwitch="AMB";
				dtoHC.setEspecialidad("");
				break;
			case 3:
				viaIngresoSwitch="URG";
				dtoHC.setEspecialidad(UtilidadesHistoriaClinica.especialidadUrgencia(con, String.valueOf(tmp.get(i).get("cuenta"))));
				break;
			case 4:
				viaIngresoSwitch="CE";
				dtoHC.setEspecialidad(UtilidadesHistoriaClinica.obtenerTipoPacienteConsultaExterna(con, String.valueOf(tmp.get(i).get("cuenta"))));
				break;
			case 10:
				viaIngresoSwitch="URG/HOS - HOSP";
				dtoHC.setEspecialidad(UtilidadesHistoriaClinica.obtenerTipoPacienteHosp(con, String.valueOf(tmp.get(i).get("cuenta"))));
				break;
			case 11:
				viaIngresoSwitch="URG/HOS - CX"; 
				dtoHC.setEspecialidad(UtilidadesHistoriaClinica.obtenerTipoPacienteHosp(con, String.valueOf(tmp.get(i).get("cuenta"))));
				break;
			case 12:
				viaIngresoSwitch="HOS/HOS - HOSP";
				dtoHC.setEspecialidad(UtilidadesHistoriaClinica.obtenerTipoPacienteHosp(con, String.valueOf(tmp.get(i).get("cuenta"))));
				break;
			case 13:
				viaIngresoSwitch="URG.";
				dtoHC.setEspecialidad(String.valueOf(tmp.get(i).get("especialidad")));
				break;
			case 14:
				viaIngresoSwitch="HOS.";
				dtoHC.setEspecialidad(String.valueOf(tmp.get(i).get("especialidad")));
				break;
			case 15:
				viaIngresoSwitch="	URG/URG";
				dtoHC.setEspecialidad(UtilidadesHistoriaClinica.obtenerTipoPacienteHosp(con, String.valueOf(tmp.get(i).get("cuenta"))));
				break;
			}
		
			dtoHC.setViaIng(viaIngresoSwitch);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//especialidad
			
//			if(tmp.get(i).get("especialidad")!=null){
//				dtoHC.setEspecialidad(String.valueOf(tmp.get(i).get("especialidad")));
//			}else{
//				dtoHC.setEspecialidad("");
//			}
			
			// ingreso
			if(tmp.get(i).get("consecutivoingreso")!=null){
				dtoHC.setIng(String.valueOf(tmp.get(i).get("consecutivoingreso")));
			}else{
				dtoHC.setIng("");
			}
			
			//estado ingreso
			if(tmp.get(i).get("estado_ingreso")!=null){
				dtoHC.setEstadoIngreso(String.valueOf(tmp.get(i).get("estado_ingreso")));
			}else{
				dtoHC.setEstadoIngreso("");
			}
			
			//estado cuenta 
			if(tmp.get(i).get("estadocuenta")!=null){
				dtoHC.setEstadoCuenta(String.valueOf(tmp.get(i).get("estadocuenta")));
			}
			
			
			if(tmp.get(i).get("cuenta")!=null){
				dtoHC.setCuenta(String.valueOf(tmp.get(i).get("cuenta")));
			}
			
			// ingreso
			if(tmp.get(i).get("cuenta")!=null){
				dtoHC.setIng2(String.valueOf(UtilidadValidacion.obtenerIngreso(con,Integer.valueOf(String.valueOf(tmp.get(i).get("cuenta"))))));
			}else{
				dtoHC.setIng("");
			}
			
			if(tmp.get(i).get("codigoviaingreso")!=null){
				dtoHC.setCodigoViaIngreso(String.valueOf(tmp.get(i).get("codigoviaingreso")));
			}
			 //UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(cuenta.get("fechacuenta")));%> -->
			
			if(tmp.get(i).get("viaingreso")!=null){
				dtoHC.setViaIngreso(String.valueOf(tmp.get(i).get("viaingreso")));
			}
			
			boolean tieneEgreso;
			try {
				tieneEgreso = util.UtilidadValidacion.tieneEgreso(con, Integer.parseInt(tmp.get(i).get("cuenta").toString()));
				
				if(tieneEgreso)
				{
					if(tmp.get(i).get("diagnostico")==null)
					{
						dtoHC.setDx("");
					}
					else
					{
						String[] codigoDxJsp=String.valueOf( tmp.get(i).get("diagnostico")).split(" ");
						if(codigoDxJsp.length>0){
							dtoHC.setDx(codigoDxJsp[0]);
						}else{
							dtoHC.setDx(String.valueOf( tmp.get(i).get("diagnostico")));
						}
					}
				}
				else
				{	
					dtoHC.setDx("");
				}
				
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			dtoHC.setReIngreso(String.valueOf(tmp.get(i).get("reingreso")));
			dtoHC.setPreIngreso(String.valueOf(tmp.get(i).get("preingreso")));
			
			dtoHC.setCodigoPkEntidadSubcontratada(String.valueOf(tmp.get(i).get("codigo_pk")));
			dtoHC.setRazonSocialEntidadSubcontratada(String.valueOf(tmp.get(i).get("razon_social")));
			dtoHC.setCodigoDx(String.valueOf(tmp.get(i).get("codigodx")));
			dtoHC.setCodigoCie(String.valueOf(tmp.get(i).get("codigocie")));
			dtoHC.setDescripcionDx(String.valueOf(tmp.get(i).get("descripciondx")));
			
			
			listaDtoHc.add(dtoHC);
			
		}

		resumenAtencionesForm.setListaDtoHc(listaDtoHc);
		request.getSession().setAttribute("listaIngresos", listaDtoHc);
		//Se valida el parametro guardado en base de datos para identificar si se imprime el reporte en PDF o en JSP
		resumenAtencionesForm.setEstadoHCCLiente( UtilidadesHistoriaClinica.consultarEstadoFuncionalidadHistoriaClincia(con));
		
		

		UtilidadBD.closeConnection(con);

		return mapping.findForward("paginaInicio");
	}
	
	
	public ActionForward ejecutarBusquedaFiltro(Connection con, ResumenAtencionesForm resumenAtencionesForm, ResumenAtenciones resumenAtencionesMundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request){
		
		resumenAtencionesForm.setCuentas(resumenAtencionesMundo.busquedaCuentasFiltro(con, paciente.getCodigoPersona(), 
				resumenAtencionesForm.getAnoSeleccionadoFiltroHC(),
				resumenAtencionesForm.getFechaIngresoInicialFiltro(), 
				resumenAtencionesForm.getFechaIngresoFinalFiltro(),
				String.valueOf(resumenAtencionesForm.getCentroAtencionFiltro())  ,
				resumenAtencionesForm.getViaIngresoSeleccionadaFiltroHc()  , 
				resumenAtencionesForm.getEspeSeleccioandaFiltroHc())
				);
		
		
		/**Datos necesarios para la adicion de la opcion de adjuntar Historias Clínicas previas**/
		try
		{
			resumenAtencionesForm.setNumDocumentosAdjuntos(resumenAtencionesMundo.existenAdjuntos(con, paciente.getCodigoPersona()));
			resumenAtencionesForm.setDocumentosAdjuntosGenerados(resumenAtencionesMundo.consultarDocumentosAdjuntos(con, paciente.getCodigoPersona()));
		}
		catch(SQLException e)
		{
			logger.error("Error cargando Documentos Adjuntos en accionEmpezar de ResumenAtencionesAction : "+e);
		}

		if(UtilidadValidacion.existeHojaObstetrica(con, paciente.getCodigoPersona()))
		{
			resumenAtencionesForm.setExisteHojaObstetrica("si");
		}
		else
		{
			resumenAtencionesForm.setExisteHojaObstetrica("no");
		}
		resumenAtencionesForm.setExisteTriage(ResumenAtenciones.existeTriage(con, paciente.getCodigoTipoIdentificacionPersona(), paciente.getNumeroIdentificacionPersona()));

		//*******+SECCIÓN PARA CARGAR LOS ANTECEDENTES*****************************************
		cargarAntecedentesAlergias(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesFamiliares(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesFamiliaresOculares(con,paciente,usuario,resumenAtencionesForm,0);
		cargarAntecedentesPersonalesOculares(con,paciente,usuario,resumenAtencionesForm,0);
		cargarAntecedentesGinecoObstetricos(con,paciente,usuario,resumenAtencionesForm,0);
		cargarAntecedentesMedicamentos(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesMedicos(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesTransfusionales(con,paciente,resumenAtencionesForm,0);
		cargarAntecedentesVacunas(con,paciente,resumenAtencionesForm,0);
		//***************************************************************************************


		//*******+SECCIÓN PARA CONSENTIMIENTO INFORMADO*****************************************
		HashMap parametros = new HashMap();
		parametros.put("ingreso",paciente.getCodigoIngreso());
		resumenAtencionesForm.setHistorialConsentimientoInfMap(ResumenAtenciones.consultaHistorialConsentimientoInf(con, parametros));

		if(Integer.parseInt(resumenAtencionesForm.getHistorialConsentimientoInfMap("numRegistros").toString())>0)
			resumenAtencionesForm.setExisteHistorialConsentimiento(ConstantesBD.acronimoSi);
		else
			resumenAtencionesForm.setExisteHistorialConsentimiento(ConstantesBD.acronimoNo);

		//***************************************************************************************


		ArrayList<HashMap> tmp = (ArrayList<HashMap>) resumenAtencionesForm.getCuentas();
		Collection tmp2=new ArrayList();
		Integer tamLista=tmp.size();
		for (int i = 0; i < tamLista; i++) {
			HashMap mapaTmp=tmp.get(i);
			mapaTmp.put("checkIngreso_"+i, "false");
			tmp2.add(mapaTmp);
		}
		
		
		
		
		
		
		
		
		List<DtoIngresoHistoriaClinica> listaDtoHc= new ArrayList<DtoIngresoHistoriaClinica>();
		for (int i = 0; i < tmp2.size(); i++) {
			HashMap mapaIngresosHC = tmp.get(i);
			DtoIngresoHistoriaClinica dtoHC = new DtoIngresoHistoriaClinica();
			dtoHC.setIngreso(false);


			
			
			int idCuentaAsocio = 0;
			int viaIngreso2=Utilidades.convertirAEntero(tmp.get(i).get("codigoviaingreso")+"");
			Boolean Asocio=false;
			
			if(!resumenAtencionesForm.isAsocio()||viaIngreso2==10||viaIngreso2==11||viaIngreso2==12 ||viaIngreso2==15)
			{
				if(viaIngreso2==10||viaIngreso2==11||viaIngreso2==12 || viaIngreso2==15)
				{
					idCuentaAsocio = Integer.parseInt(Utilidades.getCuentaAsociada(con,String.valueOf(tmp.get(i).get("cuenta")),false));
					if(viaIngreso2==15){
						viaIngreso2 = ConstantesBD.codigoViaIngresoUrgencias;
					}
					else{
						viaIngreso2 = ConstantesBD.codigoViaIngresoHospitalizacion;
					}
					Asocio=true;
				}
				else{
					Asocio=false;
				}
			}
			else{
				idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
			}
			
			
			
			
			
			if(Asocio){
				dtoHC.setAsocio("A");
			}
			
			
			
			
			
			
			
			// fehcahora ingreso
			int viaIngreso=Utilidades.convertirAEntero(tmp.get(i).get("codigoviaingreso")+"");
			String fechaIngreso="";
			String fecha="";
			if( tmp.get(i).get("fechaadmisionhospitalaria")!=null && ( viaIngreso==1 || viaIngreso==10 || viaIngreso==11 || viaIngreso==12 || viaIngreso==14)){
				fechaIngreso=UtilidadFecha.conversionFormatoFechaAAp(String.valueOf( tmp.get(i).get("fechaadmisionhospitalaria")));
				dtoHC.setFechaIngreso(fechaIngreso);
				fecha =	UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(tmp.get(i).get("fechaadmisionhospitalaria")))+" - "+UtilidadFecha.convertirHoraACincoCaracteres(String.valueOf(tmp.get(i).get("horaadmisionhospitalaria")));
			}else if( tmp.get(i).get("fechaadmisionurgencias")!=null && ( viaIngreso==3 || viaIngreso==13)){
				fechaIngreso=UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(tmp.get(i).get("fechaadmisionurgencias")));
				dtoHC.setFechaIngreso(fechaIngreso);
				fecha =		UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(tmp.get(i).get("fechaadmisionurgencias"))) +" - "+ UtilidadFecha.convertirHoraACincoCaracteres(String.valueOf(tmp.get(i).get("horaadmisionurgencias")));
			}else if( tmp.get(i).get("fechacuenta")!=null && (viaIngreso==2 || viaIngreso==4) ){
				fechaIngreso=UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(tmp.get(i).get("fechacuenta")));
				dtoHC.setFechaIngreso(fechaIngreso);
				fecha =	UtilidadFecha.conversionFormatoFechaAAp(String.valueOf( tmp.get(i).get("fechacuenta")))+" - "+ UtilidadFecha.convertirHoraACincoCaracteres(String.valueOf(tmp.get(i).get("horacuenta"))).subSequence(0,5);
			}
			dtoHC.setFechaHoraIngreso(fecha);


			//fecha de egreso
			String fechaEgreso="";
			if(tmp.get(i).get("fechaegreso")!=null){
				fechaEgreso=UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(tmp.get(i).get("fechaegreso")));
			}

			if((tmp.get(i).get("horaegreso")!=null)){
				fechaEgreso+=UtilidadFecha.convertirHoraACincoCaracteres(String.valueOf(tmp.get(i).get("horaegreso")));
			}
			dtoHC.setFechaHoraEgreso(fechaEgreso);

			//centro de atencion
			dtoHC.setCentroAtencion(String.valueOf(tmp.get(i).get("centroatencion")));

			//via de ingreso
			String viaIngresoSwitch="";
			switch(viaIngreso){
			case 1:
				viaIngresoSwitch="HOS";
				break;
			case 2:
				viaIngresoSwitch="AMB";
				break;
			case 3:
				viaIngresoSwitch="URG";
				break;
			case 4:
				viaIngresoSwitch="CE";
				break;
			case 10:
				viaIngresoSwitch="URG/HOS - HOSP";
				break;
			case 11:
				viaIngresoSwitch="URG/HOS - CX";
				break;
			case 12:
				viaIngresoSwitch="HOS/HOS - HOSP";
				break;
			case 13:
				viaIngresoSwitch="URG.";
				break;
			case 14:
				viaIngresoSwitch="HOS.";
				break;
			case 15:
				viaIngresoSwitch="	URG/URG";
				break;
			}
			
			dtoHC.setViaIng(viaIngresoSwitch);
			
			//especialidad
			
			if(tmp.get(i).get("especialidad")!=null){
				dtoHC.setEspecialidad(String.valueOf(tmp.get(i).get("especialidad")));
			}else{
				dtoHC.setEspecialidad("");
			}
			
			// ingreso
			if(tmp.get(i).get("consecutivoingreso")!=null){
				dtoHC.setIng(String.valueOf(tmp.get(i).get("consecutivoingreso")));
			}else{
				dtoHC.setIng("");
			}
			
			//estado ingreso
			if(tmp.get(i).get("estado_ingreso")!=null){
				dtoHC.setEstadoIngreso(String.valueOf(tmp.get(i).get("estado_ingreso")));
			}else{
				dtoHC.setEstadoIngreso("");
			}
			
			//estado cuenta 
			if(tmp.get(i).get("estadocuenta")!=null){
				dtoHC.setEstadoCuenta(String.valueOf(tmp.get(i).get("estadocuenta")));
			}
			
			
			if(tmp.get(i).get("cuenta")!=null){
				dtoHC.setCuenta(String.valueOf(tmp.get(i).get("cuenta")));
			}
			
			// ingreso
			if(tmp.get(i).get("cuenta")!=null){
				dtoHC.setIng2(String.valueOf(UtilidadValidacion.obtenerIngreso(con,Integer.valueOf(String.valueOf(tmp.get(i).get("cuenta"))))));
			}else{
				dtoHC.setIng("");
			}
			
			if(tmp.get(i).get("codigoviaingreso")!=null){
				dtoHC.setCodigoViaIngreso(String.valueOf(tmp.get(i).get("codigoviaingreso")));
			}
			 //UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(cuenta.get("fechacuenta")));%> -->
			
			if(tmp.get(i).get("viaingreso")!=null){
				dtoHC.setViaIngreso(String.valueOf(tmp.get(i).get("viaingreso")));
			}
			
			boolean tieneEgreso;
			try {
				tieneEgreso = util.UtilidadValidacion.tieneEgreso(con, Integer.parseInt(tmp.get(i).get("cuenta").toString()));
				
				if(tieneEgreso)
				{
					if(tmp.get(i).get("diagnostico")==null)
					{
						dtoHC.setDx("");
					}
					else
					{
						String[] codigoDxJsp=String.valueOf( tmp.get(i).get("diagnostico")).split(" ");
						if(codigoDxJsp.length>0){
							dtoHC.setDx(codigoDxJsp[0]);
						}else{
							dtoHC.setDx(String.valueOf( tmp.get(i).get("diagnostico")));
						}
					}
				}
				else
				{	
					dtoHC.setDx("");
				}
				
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			
			
			
			
			
			
			listaDtoHc.add(dtoHC);
			
		}

		resumenAtencionesForm.setListaDtoHc(listaDtoHc);
		request.getSession().setAttribute("listaIngresos", listaDtoHc);
		//Se valida el parametro guardado en base de datos para identificar si se imprime el reporte en PDF o en JSP
		resumenAtencionesForm.setEstadoHCCLiente( UtilidadesHistoriaClinica.consultarEstadoFuncionalidadHistoriaClincia(con));
		
		

		
		resumenAtencionesForm.setEstadoFiltroBusqueda("mostrarFiltro");
		resumenAtencionesForm.setEstadoHCCLiente( UtilidadesHistoriaClinica.consultarEstadoFuncionalidadHistoriaClincia(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaInicio");
	}
	
	
	private ActionForward accionBusquedaFiltro(Connection con, ResumenAtencionesForm resumenAtencionesForm, ResumenAtenciones resumenAtencionesMundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		int offs=0;
		if(resumenAtencionesForm.getOffset()!=0)
			offs=resumenAtencionesForm.getOffset();
		
		IConsultaConsolidadoCierresMundo consultaConsolidadoCierresMundo = TesoreriaFabricaMundo
		.crearConsolidadoCierreMundo();
		
		UtilidadTransaccion.getTransaccion().begin();
		;
		try {
			resumenAtencionesForm.setListaCentrosAtencion(consultaConsolidadoCierresMundo
					.consultarTodosCentrosAtencion());
			
			
			resumenAtencionesForm.setEspeSeleccioandaFiltroHc("");
			resumenAtencionesForm.setViasIngresoList( resumenAtencionesMundo.obtenerViasIngreso(con));
			
			resumenAtencionesForm.setEspecialdiades(resumenAtencionesMundo.obtenerEspecialidades(con));
			
			resumenAtencionesForm.setAnosPaciente( resumenAtencionesMundo.obtenerAnos(con, paciente.getCodigoPersona()));
			
			if(resumenAtencionesForm.getEstadoFiltroBusqueda().equals("mostrarFiltro")){
				resumenAtencionesForm.setEstadoFiltroBusqueda("");
			}else{
				resumenAtencionesForm.setEstadoFiltroBusqueda("mostrarFiltro");
			}
			
			
			
		}// CONTROL DE ERRORES
		catch (Exception e) {

			UtilidadTransaccion.getTransaccion().rollback();
		}

		UtilidadTransaccion.getTransaccion().commit();
		
		
		Utilidades.obtenerViasIngreso(con, "");
		
//		if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), ConstantesBD.permisoImprimirDetalleItemHC)){
//			resumenAtencionesForm.setTienePermisoImprimirDetalleItemHC(true);
//		}
//		if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), ConstantesBD.permisoImprimirHC)){
//			resumenAtencionesForm.setTienePermisoImprimirHC(true);
//		}
//		if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), ConstantesBD.permisoConsultarHistoriaDeAtenciones)){
//			resumenAtencionesForm.setTienePermisoConsultarHistoriaDeAtenciones(true);
//		}
//		
//		
//		
//		
//		resumenAtencionesForm.setOffset(offs);
//		// Obteniendo las cuentas del pacientes (Ingresos)
		
		
		
		
		
		
		return mapping.findForward("paginaInicio");
	}
	
	/**
	 * Método implementado para cargar los antecedentes de vacunas
	 * @param con
	 * @param paciente
	 * @param resumenAtencionesForm
	 * @param tipoInformacion
	 */
	private void cargarAntecedentesVacunas(Connection con, PersonaBasica paciente, ResumenAtencionesForm resumenAtencionesForm, int tipoInformacion) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();

		if(tipoInformacion == 0)
		{
			resumenAtencionesForm.getMapaVacunas().clear();
			
			mp.put("nroConsulta","20");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp = mundo.consultarInformacion(con, mp);
			
			resumenAtencionesForm.setMapaVacunas("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
				resumenAtencionesForm.setExistenAntecedentesVacunas(true);
			else
				resumenAtencionesForm.setExistenAntecedentesVacunas(false);
		
		}
		else
		{
			if(resumenAtencionesForm.getExistenAntecedentesVacunas())	
			{
				HashMap mapaParam = new HashMap(); 
				AntecedentesVacunas mundoVacu = new AntecedentesVacunas();


				mapaParam.put("codigoPaciente", paciente.getCodigoPersona()+"");
				mapaParam.put("nroConsulta","1"); 
				//----Consultar los tipos de inmunización de los antecedentes de vacunas
				try 
				{
					resumenAtencionesForm.setMapaTiposInmunizacion(mundoVacu.consultarInformacion(con, mapaParam));
				} 
				catch (SQLException e) 
				{
					logger.error("Error al consultar tipos de inmunizacion en cargarAntecedentesVacunas de ResumenAtencionesAction: "+e);
				}
				
				//---- Establecer el numero de la consulta 2.
				mapaParam.put("nroConsulta","2");
				mapaParam.put("codigoPaciente", paciente.getCodigoPersona() + "");
				
				HashMap mapaTemp=new HashMap();
				try 
				{
					mapaTemp.putAll(mundoVacu.consultarInformacion(con, mapaParam));
				} 
				catch (SQLException e) 
				{
					logger.error("Error al consultar informacion de Ant. Vacunas en cargarAntecedentesVacunas de ResumenAtencionesAction: "+e);
				}
				
				//------Si el número de registros es igual a 1 se realiza la consulta de los datos ------------//
				if(UtilidadCadena.noEsVacio(mapaTemp.get("numRegistros")+""))
				{
					if(Integer.parseInt(mapaTemp.get("numRegistros")+"")==1)
					{
						//------Se asignan las observaciones generales al form --------//
						resumenAtencionesForm.setMapaVacunas("observaciones", mapaTemp.get("observaciones_0")+"");
		
						mapaParam.put("nroConsulta","3");
						mapaParam.put("codigoPaciente", paciente.getCodigoPersona() + "");
		
						//----Se consulta la posible información guardada al paciente de dosis, refuerzo y comentarios de las vacunas ----//
						try 
						{
							resumenAtencionesForm.setMapaVacunas(mundoVacu.consultarInformacion(con, mapaParam));
						} 
						catch (SQLException e) 
						{
							logger.error("Error al consultar informacion de Ant. Vacunas (2) en cargarAntecedentesVacunas de ResumenAtencionesAction: "+e);
						}
					}
				}	
			}
		}
			
		
		
	}

	/**
	 * Método implementado para cargar los antecedentes transfusionales
	 * @param con
	 * @param paciente
	 * @param resumenAtencionesForm
	 * @param tipoInformacion 
	 */
	private void cargarAntecedentesTransfusionales(Connection con, PersonaBasica paciente, ResumenAtencionesForm resumenAtencionesForm, int tipoInformacion) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();

		if(tipoInformacion==0)
		{
			resumenAtencionesForm.getMapaAntTransfusionales().clear();
			
			mp.put("nroConsulta","18");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp = mundo.consultarInformacion(con, mp);
			
			resumenAtencionesForm.setMapaAntTransfusionales("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
				resumenAtencionesForm.setExistenAntecedentesTransfusionales(true);
			else
				resumenAtencionesForm.setExistenAntecedentesTransfusionales(false);
		}
		else
		{
			if(resumenAtencionesForm.isExistenAntecedentesTransfusionales())
			{
				mp.put("nroConsulta","19");
				mp.put("paciente",paciente.getCodigoPersona()+"");
				resumenAtencionesForm.setMapaAntTransfusionales(mundo.consultarInformacion(con, mp));
			}
		}
	}

	/**
	 * Método implementado para cargar antecedentes tóxicos (LA BUSQUEDA SE REALIZA SEGUN FILTRO DE FECHAS)
	 * @param con
	 * @param paciente
	 * @param resumenAtencionesForm
	 * @param tipoInformacion 
	 */
	private void cargarAntecedentesToxicos(Connection con, PersonaBasica paciente, ResumenAtencionesForm resumenAtencionesForm, int tipoInformacion) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();

		HashMap mp = new HashMap();
		mp.put("fechaInicio", UtilidadFecha.conversionFormatoFechaABD(resumenAtencionesForm.getFechaInicial())); 
		mp.put("fechaFinal", UtilidadFecha.conversionFormatoFechaABD(resumenAtencionesForm.getFechaFinal())); 
		mp.put("horaInicio", resumenAtencionesForm.getHoraInicial()); 
		mp.put("horaFinal", resumenAtencionesForm.getHoraFinal()); 

		if(tipoInformacion==0)
		{
			resumenAtencionesForm.getMapaAntToxicos().clear();
			
			mp.put("nroConsulta","16");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp = mundo.consultarInformacion(con, mp);
			
			resumenAtencionesForm.setMapaAntToxicos("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
				resumenAtencionesForm.setExistenAntecedentesToxicos(true);
			else
				resumenAtencionesForm.setExistenAntecedentesToxicos(false);
		}
		else
		{
			if(resumenAtencionesForm.isExistenAntecedentesToxicos())					  	
			{
				mp.put("nroConsulta","17");
				mp.put("paciente",paciente.getCodigoPersona()+"");
				resumenAtencionesForm.setMapaAntToxicos(mundo.consultarInformacion(con, mp));
			}
		}
	}

	/**
	 * Método implementado para cargar los antecedentes médicos
	 * @param con
	 * @param paciente
	 * @param resumenAtencionesForm
	 * @param tipoInformacion 
	 */
	private void cargarAntecedentesMedicos(Connection con, PersonaBasica paciente, ResumenAtencionesForm resumenAtencionesForm, int tipoInformacion) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();

		if(tipoInformacion==0)
		{
			resumenAtencionesForm.getMapaAntMedicos().clear();
			
			mp.put("nroConsulta","13");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp = mundo.consultarInformacion(con, mp);
			
			resumenAtencionesForm.setMapaAntMedicos("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
				resumenAtencionesForm.setExistenAntecedentesMedicos(true);
			else
				resumenAtencionesForm.setExistenAntecedentesMedicos(false);
		}
		else
		{
			if(resumenAtencionesForm.isExistenAntecedentesMedicos())	
			{
				mp.put("nroConsulta","14");
				mp.put("paciente",paciente.getCodigoPersona()+"");
				resumenAtencionesForm.getMapaAntMedicos().putAll(mundo.consultarInformacion(con, mp));
				
				mp.put("nroConsulta","15");  //-- consultar la seccion quirurgicos
				mp = mundo.consultarInformacion(con, mp);
				resumenAtencionesForm.setMapaAntMedicos("numRegQuirur",mp.get("numRegistros")+"");
				mp.remove("numRegistros");
				resumenAtencionesForm.getMapaAntMedicos().putAll(mp);   
			}
		}
	}

	/**
	 * Método implementado para cargar los antecedentes de medicamentos
	 * @param con
	 * @param paciente
	 * @param resumenAtencionesForm
	 * @param tipoInformacion 
	 */
	private void cargarAntecedentesMedicamentos(Connection con, PersonaBasica paciente, ResumenAtencionesForm resumenAtencionesForm, int tipoInformacion) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();
		
		if(tipoInformacion==0)
		{
			resumenAtencionesForm.getMapaAntMedicamento().clear();
			mp.put("nroConsulta","11");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp = mundo.consultarInformacion(con, mp);
			
			resumenAtencionesForm.setMapaAntMedicamento("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
				resumenAtencionesForm.setExistenAntecedentesMedicamentos(true);
			else
				resumenAtencionesForm.setExistenAntecedentesMedicamentos(false);
		}
		else
		{
			if(resumenAtencionesForm.isExistenAntecedentesMedicamentos())
			{
				mp.put("nroConsulta","12");
				mp.put("paciente",paciente.getCodigoPersona()+"");
				resumenAtencionesForm.getMapaAntMedicamento().putAll(mundo.consultarInformacion(con, mp));
			}
		}
		
	}

	/**
	 * Método implementado para cargar antecedentes gineco-obstétricos
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @param resumenAtencionesForm
	 * @param tipoInformacion 
	 */
	private void cargarAntecedentesGinecoObstetricos(Connection con, PersonaBasica paciente, UsuarioBasico usuario, ResumenAtencionesForm resumenAtencionesForm, int tipoInformacion) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap mp = new HashMap();

		if(tipoInformacion==0)
		{
		
			resumenAtencionesForm.getMapaAntGineco().clear();
			
			mp.put("nroConsulta","10");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp.put("institucion", usuario.getCodigoInstitucionInt() + "");
			
			mp = mundo.consultarInformacion(con, mp);
			
			resumenAtencionesForm.setMapaAntGineco("observaciones", mp.get("observaciones_0")+"");
	
			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
				resumenAtencionesForm.setExistenAntecedentesGinecoObstetricos(true);
			else
				resumenAtencionesForm.setExistenAntecedentesGinecoObstetricos(false);
		}
		else
		{
			if(resumenAtencionesForm.isExistenAntecedentesGinecoObstetricos())  		
			{
				AntecedentesGinecoObstetricosForm antecedentesBean = new AntecedentesGinecoObstetricosForm(); 			
				antecedentesBean.reset();
				if( logger.isDebugEnabled() )
				{
					logger.debug("Está mostrando el resumen de los antecedentes ginecobstétricos del paciente si los tiene ");
				}
				
				// Se carga toda la informaci?n de la bd en el momento para mostrar un resumen completo
				AntecedentesGinecoObstetricos antecedentes = new AntecedentesGinecoObstetricos();
				antecedentes.setPaciente(paciente);
				
				try 
				{
					antecedentes.cargar(con, 0);
				} catch (SQLException e) 
				{
					logger.error("Cargando los antecedentes gineco-obstétricos: "+e);
				}
				cargarBeanCompletoAntGineco(antecedentes, antecedentesBean, resumenAtencionesForm);
				
				//request.setAttribute("numeroEmbarazos", ""+antecedentesBean.getNumeroEmbarazos());
				//request.setAttribute("numeroEmbarazos", ""+forma.getMapaAntGineco("NumeroEmbarazos"));
				
				//request.setAttribute("numMetodosAnticonceptivos", ""+antecedentesBean.getNumMetodosAnticonceptivos());
				//request.setAttribute("numeroEmbarazos", ""+forma.getMapaAntGineco("NumMetodosAnticonceptivos"));

				//--
				int nroRows = UtilidadCadena.vInt(resumenAtencionesForm.getMapaAntGineco("NumeroEmbarazos")+"");
				for(int i=1; i<=nroRows; i++)
				{
					String nH = (String)antecedentesBean.getValue("numeroHijos_" +i);
					int numH = Integer.parseInt(nH);
					//request.setAttribute("numeroHijos_"+i, nH);
					resumenAtencionesForm.setMapaAntGineco("numeroHijos_"+i, nH);
					
					for(int j=1; j<=numH; j++)
					{
						//String numeroTiposPartoVaginalTemp=(String)antecedentesBean.getValue("numTiposPartoVaginal_"+i+"_"+j);
						String numeroTiposPartoVaginalTemp = resumenAtencionesForm.getMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j)+"";
						
						if (numeroTiposPartoVaginalTemp!=null)
						{
							//request.setAttribute("numTiposPartoVaginal_"+i+"_"+j, antecedentesBean.getValue("numTiposPartoVaginal_"+i+"_"+j));
							resumenAtencionesForm.setMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j, resumenAtencionesForm.getMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j)+"");
						}
						else
						{
							//request.setAttribute("numTiposPartoVaginal_"+i+"_"+j, "0");
							resumenAtencionesForm.setMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j, "0");
						}
						
					}
				}
			}
		}
		
		
		
	}

	/**
	 * Método auxiliar que carga la información de los antecedentes gineco-obstétricos
	 * @param antecedentes
	 * @param antecedentesBean
	 * @param resumenAtencionesForm
	 */
	private void cargarBeanCompletoAntGineco(AntecedentesGinecoObstetricos antecedentes, AntecedentesGinecoObstetricosForm bean, ResumenAtencionesForm forma) 
	{
		
		// carga los basicos del bean
		cargarBeanAntGineco(antecedentes, bean, forma);
		
		// los historicos
		ArrayList historicos = antecedentes.getAntecedentesHistoricos();
		bean.setHistoricos(historicos);
		forma.setMapaAntGineco("historicosAntGinecoResumenAtenciones",historicos);
		
		
	}

	/**
	 * Método auxiliar que carga la información de los antecedentes gineco-obstétricos
	 * @param antecedentes
	 * @param bean
	 * @param forma
	 */
	private void cargarBeanAntGineco(AntecedentesGinecoObstetricos antecedentes, AntecedentesGinecoObstetricosForm bean, ResumenAtencionesForm forma) 
	{
		//---No historicos y no modificables despues de grabados
		if( !antecedentes.getRangoEdadMenarquia().getAcronimo().equals("") )
		{
			bean.setRangoEdadMenarquia(antecedentes.getRangoEdadMenarquia().getCodigo());
			forma.setMapaAntGineco("RangoEdadMenarquia",antecedentes.getRangoEdadMenarquia().getCodigo()+"");

			if( antecedentes.getRangoEdadMenarquia().getCodigo() == -1 )
			{
				bean.setNombreRangoEdadMenarquia("No se ha grabado información");
				forma.setMapaAntGineco("RangoEdadMenarquia","-1");
				forma.setMapaAntGineco("NombreRangoEdadMenarquia","No se ha grabado información");
			}	
			else
			{
				forma.setMapaAntGineco("ExisteRangoEdadMenarquia","true");
				forma.setMapaAntGineco("NombreRangoEdadMenarquia",antecedentes.getRangoEdadMenarquia().getValue()+"");
				bean.setNombreRangoEdadMenarquia(antecedentes.getRangoEdadMenarquia().getValue());
				bean.setExisteRangoEdadMenarquia(true);
			}
		}

		forma.setMapaAntGineco("OtroEdadMenarquia",antecedentes.getOtroEdadMenarquia());
		bean.setOtraEdadMenarquia(antecedentes.getOtroEdadMenarquia());
		
		
		if( !antecedentes.getRangoEdadMenopausia().getAcronimo().equals("") )
		{
			bean.setRangoEdadMenopausia(antecedentes.getRangoEdadMenopausia().getCodigo());
			forma.setMapaAntGineco("RangoEdadMenopausia",antecedentes.getRangoEdadMenopausia().getCodigo()+"");
			
			if( antecedentes.getRangoEdadMenopausia().getCodigo() == -1 )
			{
				bean.setNombreRangoEdadMenopausia("No se ha grabado información");
				forma.setMapaAntGineco("NombreRangoEdadMenopausia","No se ha grabado información");
			}	
			else
			{
				forma.setMapaAntGineco("NombreRangoEdadMenopausia",antecedentes.getRangoEdadMenopausia().getValue()+"");
				forma.setMapaAntGineco("ExisteRangoEdadMenopausia","true");
				
				bean.setNombreRangoEdadMenopausia(antecedentes.getRangoEdadMenopausia().getValue());
				bean.setExisteRangoEdadMenopausia(true);
			}
		}
		bean.setOtraEdadMenopausia(antecedentes.getOtroEdadMenopausia());
		forma.setMapaAntGineco("OtraEdadMenopausia",antecedentes.getOtroEdadMenopausia()+"");
		
		if(antecedentes.getInicioVidaSexual() == 0 )
		{
			bean.setInicioVidaSexual("");
			forma.setMapaAntGineco("InicioVidaSexual","");
		}	
		else
		{
			bean.setInicioVidaSexual(""+antecedentes.getInicioVidaSexual());
			forma.setMapaAntGineco("InicioVidaSexual",""+antecedentes.getInicioVidaSexual());
		}	

		if(antecedentes.getInicioVidaObstetrica() == 0 ) 
		{
			bean.setInicioVidaObstetrica("");
			forma.setMapaAntGineco("InicioVidaObstetrica","");
		}	
		else
		{
			bean.setInicioVidaObstetrica(""+antecedentes.getInicioVidaObstetrica());
			forma.setMapaAntGineco("InicioVidaObstetrica",""+antecedentes.getInicioVidaObstetrica());
		}	
		
		String observacionesStr =  antecedentes.getObservaciones();
		
		if( observacionesStr == null )
			observacionesStr = "";
		
		forma.setMapaAntGineco("observacionesStr",observacionesStr);
				
		
		//if( bean.estado.equals("resumen") )
		{
			bean.setObservacionesViejas(observacionesStr.replaceAll("\n", "<br>"));
			forma.setMapaAntGineco("ObservacionesViejas",observacionesStr.replaceAll("\n", "<br>"));
		}	
		//else
			//bean.setObservacionesViejas(observacionesStr.replaceAll("<br>", "\n"));
		//		Fin no historicos y no modificables despues de grabados
		
		// 		M?todos anticonceptivos
		ArrayList metodosAnticonceptivos = antecedentes.getMetodosAnticonceptivos();
		bean.setNumMetodosAnticonceptivos(metodosAnticonceptivos.size());
		forma.setMapaAntGineco("NumMetodosAnticonceptivos",metodosAnticonceptivos.size()+"");
		

		for( int i=0; i < metodosAnticonceptivos.size(); i++ )
		{
			InfoDatos metodo = (InfoDatos)metodosAnticonceptivos.get(i);
			
			bean.setValue("metodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());
			forma.setMapaAntGineco("metodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());
			
			bean.setValue("nombreMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getValue());
			forma.setMapaAntGineco("nombreMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());

			bean.setValue("descMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getDescripcion());
			forma.setMapaAntGineco("descMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());

			bean.setValue("existeMetodoAnticonceptivo_"+metodo.getAcronimo(), "true");
			forma.setMapaAntGineco("existeMetodoAnticonceptivo_"+metodo.getAcronimo(), metodo.getAcronimo());
			
			if( !metodo.getDescripcion().trim().equals("") )
			{
				bean.setValue("existeDescMetodoAnticonceptivo_"+metodo.getAcronimo(), "true");
				forma.setMapaAntGineco("existeDescMetodoAnticonceptivo_"+metodo.getAcronimo(), metodo.getAcronimo());
			}	
		}
		//		Fin m?todos anticonceptivos
		
		//		Embarazos
		ArrayList embarazos = antecedentes.getEmbarazos();
		
		bean.setNumeroEmbarazos(embarazos.size());
		forma.setMapaAntGineco("NumeroEmbarazos", embarazos.size()+"");
		forma.setMapaAntGineco("NumGestaciones", embarazos.size()+"");
		
		bean.setNumGestaciones(bean.getNumeroEmbarazos());
		for(int i=1; i<=embarazos.size(); i++)
		{
			Embarazo embarazo = (Embarazo)embarazos.get(i-1);
			
			bean.setValue("codigo_"+i, embarazo.getCodigo()+"");
			forma.setMapaAntGineco("codigo_"+i, embarazo.getCodigo()+"");
			
			//Guardamos el n?mero de hijos por embarazo
			bean.setValue("numeroHijos_" + i, embarazo.getHijos().size() + "");
			forma.setMapaAntGineco("numeroHijos_" + i, embarazo.getHijos().size() + "");
						
			bean.setValue("mesesGestacion_"+i, Float.toString(embarazo.getMesesGestacion()));
			forma.setMapaAntGineco("mesesGestacion_"+i, Float.toString(embarazo.getMesesGestacion()));
			
			bean.setValue("fechaTerminacion_"+i, embarazo.getFechaTerminacion());
			forma.setMapaAntGineco("fechaTerminacion_"+i, embarazo.getFechaTerminacion());
			
			bean.setValue("duracion_"+i,embarazo.getDuracion());
			forma.setMapaAntGineco("duracion_"+i,embarazo.getDuracion());

			bean.setValue("ruptura_"+i,embarazo.getTiempoRupturaMembranas());
			forma.setMapaAntGineco("ruptura_"+i,embarazo.getTiempoRupturaMembranas());
			
			bean.setValue("legrado_"+i,embarazo.getLegrado());
			forma.setMapaAntGineco("legrado_"+i,embarazo.getLegrado());
			
			int compTempo[]=embarazo.getComplicacion();
			for(int y=0;y<compTempo.length;y++)
			{
				if(compTempo[y]!=0)
				{
					bean.setValue("complicacionEmbarazo_"+i+"_"+compTempo[y],compTempo[y]+"");
					forma.setMapaAntGineco("complicacionEmbarazo_"+i+"_"+compTempo[y],compTempo[y]+"");
				}
			}
			Vector nombresComplicaciones=embarazo.getNombresComplicaciones();
			for(int y=0;y<nombresComplicaciones.size();y++)
			{
				bean.setValue("nombreComplicacionEmbarazo_"+i+"_"+y,nombresComplicaciones.elementAt(y));
				forma.setMapaAntGineco("nombreComplicacionEmbarazo_"+i+"_"+y,nombresComplicaciones.elementAt(y)+"");
			}
			
			Vector otrasComplicaciones=embarazo.getOtraComplicacion();
			for(int j=0; j<otrasComplicaciones.size();j++)
			{
				bean.setValue("otraComplicacionEmbarazo_"+i+"_"+j, otrasComplicaciones.elementAt(j)+"");
				forma.setMapaAntGineco("otraComplicacionEmbarazo_"+i+"_"+j, otrasComplicaciones.elementAt(j)+"");
			}
			bean.setValue("numOtraComplicacion_"+i, new Integer(otrasComplicaciones.size()));
			forma.setMapaAntGineco("numOtraComplicacion_"+i, otrasComplicaciones.size()+"");
						
			bean.setValue("tipoTrabajoParto_"+i, Integer.toString(embarazo.getTrabajoParto().getCodigo()));
			forma.setMapaAntGineco("tipoTrabajoParto_"+i, embarazo.getTrabajoParto().getCodigo()+"");
			
			
			if( embarazo.getTrabajoParto().getCodigo() == -1 )
			{
				bean.setValue("nombreTipoTrabajoParto_"+i, "");
				forma.setMapaAntGineco("nombreTipoTrabajoParto_"+i, "");
			}
			else
			{
				bean.setValue("nombreTipoTrabajoParto_"+i, embarazo.getTrabajoParto().getValue());
				forma.setMapaAntGineco("nombreTipoTrabajoParto_"+i, embarazo.getTrabajoParto().getValue());
			}
				
			bean.setValue("otroTipoTrabajoParto_"+i, embarazo.getOtroTrabajoParto());
			forma.setMapaAntGineco("otroTipoTrabajoParto_"+i, embarazo.getOtroTrabajoParto());

			//	Hijos embarazo.
			ArrayList hijos = embarazo.getHijos();
			ArrayList formasPartoVaginal;
			bean.setValue("numeroHijos_"+i, ""+hijos.size());
			forma.setMapaAntGineco("numeroHijos_"+i, ""+hijos.size());

			for(int j=1; j<=hijos.size(); j++)
			{
				HijoBasico hijo = new HijoBasico(); 
				hijo = (HijoBasico)hijos.get(j-1);
				
				boolean partoVaginal = true;
				
				if( hijo.isVivo() )
				{
					bean.setNumVivos(bean.getNumVivos()+1);
					bean.setNumVivosGrabados(bean.getNumVivos());
					bean.setValue("vitalidad_"+i+"_"+j, "vivo");
					forma.setMapaAntGineco("vitalidad_"+i+"_"+j, "vivo");
				}
				else
				{
					bean.setNumMuertos(bean.getNumMuertos()+1);
					bean.setNumMuertosGrabados(bean.getNumMuertos());
					bean.setValue("vitalidad_"+i+"_"+j, "muerto");
					forma.setMapaAntGineco("vitalidad_"+i+"_"+j, "muerto");
				}
								
				if( hijo.isAborto() )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					forma.setMapaAntGineco("cargadoBD_"+i+"_"+j, "true");
					
					bean.setValue("tiposParto_"+i+"_"+j, new String("4"));
					forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("4"));

					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Mortinato"));
					forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Mortinato"));

					partoVaginal = false;
					bean.setNumAbortos(bean.getNumAbortos()+1);
					forma.setMapaAntGineco("NumAbortos", ""+bean.getNumAbortos()+1);

					bean.setNumAbortosGrabados(bean.getNumAbortos());
					forma.setMapaAntGineco("NumAbortosGrabados", ""+bean.getNumAbortos());
					
				} 
				else
				if( hijo.isCesarea() )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					forma.setMapaAntGineco("cargadoBD_"+i+"_"+j, "true");

					bean.setValue("tiposParto_"+i+"_"+j, new String("5"));
					forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("5"));

					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Cesarea"));
					forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Cesarea"));
					
					partoVaginal = false;
					bean.setNumCesareas(bean.getNumCesareas()+1);
					forma.setMapaAntGineco("NumCesareas", ""+(bean.getNumCesareas()+1));

					bean.setNumCesareasGrabadas(bean.getNumCesareas());
					forma.setMapaAntGineco("NumCesareasGrabadas", ""+bean.getNumCesareas());
				} 
				else
				if( hijo.getOtroTipoParto() != null && !hijo.getOtroTipoParto().equals("") )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					forma.setMapaAntGineco("cargadoBD_"+i+"_"+j, "true");
					
					bean.setValue("tiposParto_"+i+"_"+j, new String("0"));
					forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("0"));

					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Otro"));
					forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Otro"));
					
					bean.setValue("otroTipoParto_" +i+"_"+j, hijo.getOtroTipoParto());
					forma.setMapaAntGineco("otroTipoParto_" +i+"_"+j, hijo.getOtroTipoParto());
					
					partoVaginal = false;
				}
				else
				if( ( formasPartoVaginal = hijo.getFormasNacimientoVaginal() ).size() > 0 )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					forma.setMapaAntGineco("cargadoBD_"+i+"_"+j, "true");
					
					bean.setNumPartos(bean.getNumPartos()+1);
					forma.setMapaAntGineco("NumPartos", ""+(bean.getNumPartos()+1));
					
					bean.setNumPartosGrabados(bean.getNumPartos());
					forma.setMapaAntGineco("NumPartosGrabados", ""+bean.getNumPartos());
					
					boolean esvalido = true;					
					if(formasPartoVaginal.size()==1)
					{
						InfoDatos tipoPVInfo = (InfoDatos)formasPartoVaginal.get(0);
						// En este caso no debemos mostrarlo de forma tradicional
						if( tipoPVInfo.getCodigo() == -2 )
						{
							bean.setValue("tiposParto_"+i+"_"+j, new String("3"));
							forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("3"));

							bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
							forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));

							bean.setValue("numTiposPartoVaginal_"+i+"_"+j, "0");
							forma.setMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j, "0");

							esvalido = false;
						}
					}
					if( esvalido )
					{

						bean.setValue("tiposParto_"+i+"_"+j, new String("3"));
						forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("3"));

						bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
						forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));

						bean.setValue("numTiposPartoVaginal_"+i+"_"+j, ""+formasPartoVaginal.size());
						forma.setMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j, ""+formasPartoVaginal.size());
								
						for(int k=0; k<formasPartoVaginal.size(); k++)
						{
							InfoDatos tipoPVInfo = (InfoDatos)formasPartoVaginal.get(k);
							
							bean.setValue("tipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getAcronimo());
							forma.setMapaAntGineco("tipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getAcronimo());
								
							bean.setValue("nombreTipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getValue());
							forma.setMapaAntGineco("nombreTipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getValue());
														
						}

					}
					
				}
				if( partoVaginal && hijo.getOtraFormaNacimientoVaginal() != null && !hijo.getOtraFormaNacimientoVaginal().equals("") )
				{
					bean.setValue("otroTipoPartoVaginal_"+i+"_"+j, hijo.getOtraFormaNacimientoVaginal());
					forma.setMapaAntGineco("otroTipoPartoVaginal_"+i+"_"+j, hijo.getOtraFormaNacimientoVaginal());

					bean.setValue("tiposParto_"+i+"_"+j, new String("3"));
					forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("3"));

					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
					forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
				}
				String tempoSexo=hijo.getSexo()+"";
				if(tempoSexo!=null && !tempoSexo.equals("null") && (tempoSexo.equals(ConstantesBD.codigoSexoMasculino+"") || tempoSexo.equals(ConstantesBD.codigoSexoFemenino+"")))
				{
					bean.setValue("sexo_"+i+"_"+j,hijo.getSexo()+"");
					forma.setMapaAntGineco("sexo_"+i+"_"+j,hijo.getSexo()+"");
				}
				else
				{
					bean.setValue("sexo_"+i+"_"+j,"-1");
					forma.setMapaAntGineco("sexo_"+i+"_"+j,"-1");
				}
				bean.setValue("peso_"+i+"_"+j, hijo.getPeso());
				forma.setMapaAntGineco("peso_"+i+"_"+j, hijo.getPeso());

				bean.setValue("lugar_"+i+"_"+j, hijo.getLugar());
				forma.setMapaAntGineco("lugar_"+i+"_"+j, hijo.getLugar());
			}
			//	Fin hijos embarazo			
		}
		//		Fin embarazos
		
	}

	/**
	 * Método implementado para cargar antecedentes personales oculares
	 * 
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @param resumenAtencionesForm
	 * @param tipoInformacion 
	 */
	private void cargarAntecedentesPersonalesOculares(Connection con, PersonaBasica paciente, UsuarioBasico usuario, ResumenAtencionesForm resumenAtencionesForm, int tipoInformacion) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap mp = new HashMap();

		if(tipoInformacion == 0)
		{
		
			resumenAtencionesForm.getMapaAntPersoOftal().clear();
			mp.put("nroConsulta","7");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp.put("institucion", usuario.getCodigoInstitucionInt() + "");
			mp = mundo.consultarInformacion(con, mp);
			resumenAtencionesForm.setMapaAntPersoOftal("observaciones", mp.get("observaciones_0")+"");
			
			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
				resumenAtencionesForm.setExistenAntecedentesOftalPersonales(true);
			else
				resumenAtencionesForm.setExistenAntecedentesOftalPersonales(false);
		}
		else
		{
			if(resumenAtencionesForm.getExistenAntecedentesOftalPersonales())
			{
				mp.put("nroConsulta","8");
				mp.put("paciente",paciente.getCodigoPersona()+"");
				mp.put("institucion", usuario.getCodigoInstitucionInt() + "");
		
				resumenAtencionesForm.getMapaAntPersoOftal().putAll(mundo.consultarInformacion(con, mp));
		
				mp.put("nroConsulta","9");  //-- consultar la seccion quirurgicos
				mp = mundo.consultarInformacion(con, mp);
				
				resumenAtencionesForm.setMapaAntPersoOftal("numRegQuirur",mp.get("numRegistros")+"");
				mp.remove("numRegistros");
				resumenAtencionesForm.getMapaAntPersoOftal().putAll(mp); 
			}
		}

		
		
	}

	/**
	 * Método implementado para cargar antecedentes oculares familiares
	 * @param con
	 * @param paciente
	 * @param usuario 
	 * @param resumenAtencionesForm
	 * @param tipoInformacion 
	 */
	private void cargarAntecedentesFamiliaresOculares(Connection con, PersonaBasica paciente, UsuarioBasico usuario, ResumenAtencionesForm resumenAtencionesForm, int tipoInformacion) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();
		
		if(tipoInformacion==0)
		{
		
			resumenAtencionesForm.getMapaAntFamOftal().clear();
			mp.put("nroConsulta","5");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp.put("institucion", usuario.getCodigoInstitucionInt() + "");
			
			mp = mundo.consultarInformacion(con, mp);
			
			resumenAtencionesForm.setMapaAntFamOftal("observaciones", mp.get("observaciones_0")+"");
			
			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
				resumenAtencionesForm.setExistenAntecedentesOftalFamiliares(true);
			else
				resumenAtencionesForm.setExistenAntecedentesOftalFamiliares(false);
		}
		else
		{
			if(resumenAtencionesForm.getExistenAntecedentesOftalFamiliares())
			{
				mp.put("nroConsulta","6");
				mp.put("paciente",paciente.getCodigoPersona()+"");
				mp.put("institucion", usuario.getCodigoInstitucionInt() + "");
		
				resumenAtencionesForm.getMapaAntFamOftal().putAll(mundo.consultarInformacion(con, mp));
			}
		}
		
		
		
	}

	/**
	 * Método implementado para cargar los antecedentes familiares
	 * @param con
	 * @param paciente
	 * @param resumenAtencionesForm
	 * @param tipoInformacion 
	 */
	private void cargarAntecedentesFamiliares(Connection con, PersonaBasica paciente, ResumenAtencionesForm resumenAtencionesForm, int tipoInformacion) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();
		
		if(tipoInformacion==0)
		{
		
			resumenAtencionesForm.getMapaAntFamiliares().clear();
			mp.put("nroConsulta","3");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp = mundo.consultarInformacion(con, mp);
			resumenAtencionesForm.setMapaAntFamiliares("observaciones", mp.get("observaciones_0")+"");
			
			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
				resumenAtencionesForm.setExistenAntecedentesFamiliares(true);
			else
				resumenAtencionesForm.setExistenAntecedentesFamiliares(false);
		}
		else
		{
			if(resumenAtencionesForm.isExistenAntecedentesFamiliares())
			{
				mp.put("nroConsulta","4");
				mp.put("paciente",paciente.getCodigoPersona()+"");
				resumenAtencionesForm.getMapaAntFamiliares().putAll(mundo.consultarInformacion(con, mp));
			}
		}
		
		
	}

	/**
	 * Método implementado para cargar las antecedentes alergias
	 * @param con
	 * @param paciente
	 * @param resumenAtencionesForm
	 * @param tipoInformacion 
	 */
	private void cargarAntecedentesAlergias(Connection con, PersonaBasica paciente, ResumenAtencionesForm resumenAtencionesForm, int tipoInformacion) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();
		
		if(tipoInformacion==0)
		{  
			resumenAtencionesForm.getMapaAntAlergia().clear();
			
			mp.put("nroConsulta","1");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp = mundo.consultarInformacion(con, mp);
			
			if(mp.containsKey("observaciones_0"))
			{	
				resumenAtencionesForm.setMapaAntAlergia("observaciones", mp.get("observaciones_0")+"");
			}
			else
			{
				resumenAtencionesForm.setMapaAntAlergia("observaciones", "");
			}
			
			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
				resumenAtencionesForm.setExistenAntecedentesAlergias(true);
			else
				resumenAtencionesForm.setExistenAntecedentesAlergias(false);
		}
		else
		{
			if(resumenAtencionesForm.isExistenAntecedentesAlergias())
			{
				mp.put("nroConsulta","2");
				mp.put("paciente",paciente.getCodigoPersona()+"");
				resumenAtencionesForm.getMapaAntAlergia().putAll(mundo.consultarInformacion(con, mp));
			}
		}
		
		
			
	}

	/**
	 * Método implementado para ir al detalle del control de liquidos
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward acccionDetalleControlLiquidos(
			Connection con, 
			ResumenAtencionesForm resumenAtencionesForm,
			ActionMapping mapping, 
			UsuarioBasico usuario,
			int pos) 
	{
		RegistroEnfermeria mundoEnfer = new RegistroEnfermeria();
		//se verifica si el detalle es para la cuenta asociada
		if(resumenAtencionesForm.isCuentaAsociada())
		{
			int idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
			String area[] = UtilidadValidacion.getAreaPaciente(con,idCuentaAsocio).split(ConstantesBD.separadorSplit);
			cargarControlLiquidos(
				con,
				resumenAtencionesForm,
				resumenAtencionesForm.getFechaInicial(),
				resumenAtencionesForm.getFechaFinal(),
				resumenAtencionesForm.getHoraInicial(),
				resumenAtencionesForm.getHoraFinal(),
				idCuentaAsocio,
				area[0],
				usuario.getCodigoInstitucion(),
				mundoEnfer,true,1,pos);
		}
		else
		{
			int idCuenta = resumenAtencionesForm.getCuenta();
			String area[] = UtilidadValidacion.getAreaPaciente(con,idCuenta).split(ConstantesBD.separadorSplit);
			cargarControlLiquidos(
				con,
				resumenAtencionesForm,
				resumenAtencionesForm.getFechaInicial(),
				resumenAtencionesForm.getFechaFinal(),
				resumenAtencionesForm.getHoraInicial(),
				resumenAtencionesForm.getHoraFinal(),
				idCuenta,
				area[0],
				usuario.getCodigoInstitucion(),
				mundoEnfer,false,1,pos);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleControlLiquidos");
	}

	/**
	 * Método implementado para filtrar los 
	 * @param con
	 * @param resumenAtencionesForm
	 * @param resumenAtencionesMundo
	 * @param mapping
	 * @param usuariofalse
	 * @param paciente
	 * @return
	 */
	private ActionForward accionBuscarIngreso(
			Connection con, 
			ResumenAtencionesForm resumenAtencionesForm, 
			ResumenAtenciones resumenAtencionesMundo, 
			ActionMapping mapping, 
			UsuarioBasico usuario, 
			PersonaBasica paciente) 
	{
		//Se hace un reset de los indicadores
		resumenAtencionesForm.resetBusqueda();
		
		int viaIngreso=resumenAtencionesForm.getViaIngreso(); 
		int idCuenta = resumenAtencionesForm.getCuenta();
		
		
		//se consulta el ingreso
		int idIngreso = Integer.parseInt(resumenAtencionesForm.getIdIngreso());
		
		//Caso del asocio ******************************************
		int idCuentaAsocio = 0;
		if(!resumenAtencionesForm.isAsocio()||viaIngreso==10||viaIngreso==11||viaIngreso==12)
		{
			if(viaIngreso==10||viaIngreso==11||viaIngreso==12)
			{
				idCuentaAsocio = Integer.parseInt(Utilidades.getCuentaAsociada(con,idCuenta+"",false));
				viaIngreso = ConstantesBD.codigoViaIngresoHospitalizacion;
				resumenAtencionesForm.setViaIngreso(viaIngreso);
				resumenAtencionesForm.setAsocio(true);
				resumenAtencionesForm.setCuentaAsocio(idCuentaAsocio+"");
			}
			else
				resumenAtencionesForm.setAsocio(false);
		}
		else
			idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
		
				
		//Captura la información de las cuentas del paciente
		resumenAtencionesForm.setMapaValidacionesInfoCuentas(new HashMap()); //se resetesa mapa
		resumenAtencionesForm.setMapaValidacionesInfoCuentas("numRegistros","0");
		ArrayList<CuentasPaciente> cuentasPacienteArray = ResumenAtenciones.getCuentasPaciente(con, idIngreso);
		int tamano = cuentasPacienteArray.size();
		for(int i = 0; i<tamano; i++)
		{
			resumenAtencionesForm.setMapaValidacionesInfoCuentas("mostrar_"+i,ConstantesBD.acronimoNo);
			
			CuentasPaciente cpacientes = (CuentasPaciente)cuentasPacienteArray.get(i);
			resumenAtencionesForm.setMapaValidacionesInfoCuentas("codigoViaIngreso_"+i,cpacientes.getCodigoViaIngreso());
			
			//MT 6076 hermorhu
			if(resumenAtencionesForm.getFiltroAsocio().equals(""))
				resumenAtencionesForm.setFiltroAsocio("A");
			
			//*****SE CARGAN LOS DATOS DE LA CUENTA PRINCIPAL************************************************
			//Por defecto la primera posicion del array es la cuenta principal
			if(i == 0)			
			{
				if(resumenAtencionesForm.getFiltroAsocio().equals("A") || 
						(cpacientes.getCodigoViaIngreso().equals(String.valueOf(ConstantesBD.codigoViaIngresoHospitalizacion)) && cpacientes.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteAmbulatorio) && resumenAtencionesForm.getFiltroAsocio().equals("HA")) ||
						(cpacientes.getCodigoViaIngreso().equals(String.valueOf(ConstantesBD.codigoViaIngresoHospitalizacion)) && cpacientes.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado) && resumenAtencionesForm.getFiltroAsocio().equals("HH"))
						){
					
					this.cargarDatosCuentaPrincipal(con,
							resumenAtencionesForm,
							resumenAtencionesMundo,
							Integer.parseInt(cpacientes.getCodigoViaIngreso()),
							Integer.parseInt(cpacientes.getCodigoCuenta()),
							paciente,
							usuario,i);		
					
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("descripcionViaIngreso_"+i,cpacientes.getDescripcionViaIngreso()+" - "+cpacientes.getDescripcionTipoPaciente());				
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("codigoTipoPaciente_"+i,cpacientes.getCodigoTipoPaciente());
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("idCuenta_"+i,cpacientes.getCodigoCuenta());
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("numRegistros",i+1);
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("mostrar_"+i,ConstantesBD.acronimoSi);
					
				} else {
					if(Integer.parseInt(cpacientes.getCodigoViaIngreso()) == ConstantesBD.codigoViaIngresoHospitalizacion)
						resumenAtencionesForm.setMapaValidacionesInfoCuentas("datosAdmision_"+i,resumenAtencionesMundo.busquedaDatosAdmisionHospitalizacion(con, Integer.parseInt(cpacientes.getCodigoCuenta())));
					else
						resumenAtencionesForm.setMapaValidacionesInfoCuentas("datosAdmision_"+i,resumenAtencionesMundo.busquedaDatosAdmisionUrgencias(con, Integer.parseInt(cpacientes.getCodigoCuenta())));
					
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("numRegistros",i+1);
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("codigoTipoPaciente_"+i,cpacientes.getCodigoTipoPaciente());
				}
			}
			else
			{
				//*****SE CARGAN LOS DATOS DE LA CUENTA ASOCIADA (SI HAY ASOCIO)*******************************
				if(resumenAtencionesForm.isAsocio())
				{
					if(resumenAtencionesForm.getFiltroAsocio().equals("A") || resumenAtencionesForm.getFiltroAsocio().equals("U")) {

						this.cargarDatosCuentaAsociada(con,
								resumenAtencionesForm,
								resumenAtencionesMundo,
								Integer.parseInt(cpacientes.getCodigoViaIngreso()),
								Integer.parseInt(cpacientes.getCodigoCuenta()),
								usuario,
								i,paciente);
										
					if(resumenAtencionesForm.getFiltroAsocio().equals(""))
						resumenAtencionesForm.setFiltroAsocio("A");
					
						resumenAtencionesForm.setMapaValidacionesInfoCuentas("fechaIngreso_"+i,Utilidades.getFechaIngreso(con,cpacientes.getCodigoCuenta(),Integer.parseInt(cpacientes.getCodigoViaIngreso())));					
						resumenAtencionesForm.setMapaValidacionesInfoCuentas("idCuenta_"+i,cpacientes.getCodigoCuenta());
						resumenAtencionesForm.setMapaValidacionesInfoCuentas("descripcionViaIngreso_"+i,cpacientes.getDescripcionViaIngreso()+" - "+cpacientes.getDescripcionTipoPaciente());					
						resumenAtencionesForm.setMapaValidacionesInfoCuentas("codigoTipoPaciente_"+i,cpacientes.getCodigoTipoPaciente());
						resumenAtencionesForm.setMapaValidacionesInfoCuentas("numRegistros",i+1);
						resumenAtencionesForm.setMapaValidacionesInfoCuentas("mostrar_"+i,ConstantesBD.acronimoSi);
					
					} else {
						if(Integer.parseInt(cpacientes.getCodigoViaIngreso()) == ConstantesBD.codigoViaIngresoHospitalizacion)
							resumenAtencionesForm.setMapaValidacionesInfoCuentas("datosAdmision_"+i,resumenAtencionesMundo.busquedaDatosAdmisionHospitalizacion(con, Integer.parseInt(cpacientes.getCodigoCuenta())));					
						else		
							resumenAtencionesForm.setMapaValidacionesInfoCuentas("datosAdmision_"+i,resumenAtencionesMundo.busquedaDatosAdmisionUrgencias(con, Integer.parseInt(cpacientes.getCodigoCuenta())));
						
						resumenAtencionesForm.setMapaValidacionesInfoCuentas("numRegistros",i+1);
						resumenAtencionesForm.setMapaValidacionesInfoCuentas("codigoTipoPaciente_"+i,cpacientes.getCodigoTipoPaciente());
					}	
				}
				else
				{
					resumenAtencionesForm.resetAsocio();
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("fechaIngreso_"+i,Utilidades.getFechaIngreso(con,cpacientes.getCodigoCuenta(),Integer.parseInt(cpacientes.getCodigoViaIngreso())));
					//resumenAtencionesForm.setFechaIngreso(Utilidades.getFechaIngreso(con,idCuenta+"",viaIngreso) );
				}				
			}									
		}
		
		//MT6076 - hermorhu
		resumenAtencionesForm.setEsBusquedaResumen(true);
		
		//*******************************************************************************************
		
		resumenAtencionesForm.setEstado("resumenIngreso");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenIngreso");
	}

	/**
	 * Método implementado para cargar el detalle de un ingreso
	 * @param con
	 * @param resumenAtencionesForm
	 * @param resumenAtencionesMundo 
	 * @param mapping
	 * @param usuario
	 * @param paciente 
	 * @return
	 * @throws IPSException 
	 */
	private void accionResumenIngreso(
			Connection con, 
			ResumenAtencionesForm resumenAtencionesForm,
			ResumenAtenciones resumenAtencionesMundo, 
			ActionMapping mapping, 
			UsuarioBasico usuario, 
			PersonaBasica paciente) throws IPSException 
	{
		
		resumenAtencionesForm.chequearTodos();
		resumenAtencionesForm.setRowSpanSeccionUnoReporteHC(0);
		resumenAtencionesForm.setRowSpanSeccionDosReporteHC(0);
		resumenAtencionesForm.setRowSpanSeccionTresReporteHC(0);
		resumenAtencionesForm.setRowSpanSeccionCuatroReporteHC(0);
		resumenAtencionesForm.setRowSpanSeccionCincoReporteHC(0);
		resumenAtencionesForm.setRowSpanSeccionSeisReporteHC(0);
		resumenAtencionesForm.setRowSpanSeccionSieteReporteHC(0);
		resumenAtencionesForm.setRowSpanSeccionOchoReporteHC(0);
		resumenAtencionesForm.setRowSpanSeccionNueveReporteHC(0);
		resumenAtencionesForm.setRowSpanSeccionDiezReporteHC(0);
		///Se asigna el login de usuario
		resumenAtencionesForm.setUsuario(usuario.getLoginUsuario());
		//SE asigna los permisos de impresion
		if(Utilidades.tieneRolFuncionalidad(con,usuario.getLoginUsuario(),509))
			resumenAtencionesForm.setImpresionHistoriaClinica(true);
		else
			resumenAtencionesForm.setImpresionHistoriaClinica(false);
		/*
		 * En esta sección se llena el form con cada una de las opciones del resumen de atenciones
		 * Si una sección esta vacía, no se debe mostrar, lo mismo para los links de
		 * interconsultas, procedimientos, evoluciones, etc
		 */
		int viaIngreso=resumenAtencionesForm.getViaIngreso(); //si la vía de ingreso es 10,11,12 significa que es caso de asocio
		int idCuenta = resumenAtencionesForm.getCuenta();
		
		
		//se consulta el ingreso
		int idIngreso = UtilidadValidacion.obtenerIngreso(con,idCuenta);
		resumenAtencionesForm.setIdIngreso(idIngreso+"");
		
		
		//MT 4431: Se debe tomar la fecha de egreso de la tabla ingresos.
		/*
		 * Para evitar ir a la BD consulto la informacion de las Cuentas
		 */
		ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
		resumenAtencionesForm.setIngresoSelecccionado(manejoPacienteFacade.obtenerInfoIngreso(idIngreso));
		//Fin MT 4431
		
		
		resumenAtencionesForm.setFechaEgreso(Utilidades.getFechaEgreso(con,idCuenta));

		
		//*************+SECCION PARA CARGAR RESUMEN PARCIAL HISTORIA CLINICA *******************************
		RegistroResumenParcialHistoriaClinica rphc=new RegistroResumenParcialHistoriaClinica();
		HashMap mapa=new HashMap();
		HashMap mapaAsocio=new HashMap();
		
		mapa.put("ingreso",idIngreso);
		mapaAsocio.put("ingreso",idIngreso);
		mapa=rphc.consultarNotas(con, mapa);
		mapaAsocio=rphc.consultarNotasAsocio(con, mapaAsocio);
		resumenAtencionesForm.setMuestroResumenPHC(Utilidades.convertirAEntero(mapa.get("numRegistros")+""));
		resumenAtencionesForm.setMuestroResumenPHCAsocio(Utilidades.convertirAEntero(mapaAsocio.get("numRegistros")+""));
		
		//**************************************************************************************************
		
		//Caso del asocio ******************************************
		int idCuentaAsocio = 0;
		if(!resumenAtencionesForm.isAsocio()||viaIngreso==10||viaIngreso==11||viaIngreso==12 ||viaIngreso==15)
		{
			if(viaIngreso==10||viaIngreso==11||viaIngreso==12 || viaIngreso==15)
			{
				idCuentaAsocio = Integer.parseInt(Utilidades.getCuentaAsociada(con,idCuenta+"",false));
				if(viaIngreso==15)
					viaIngreso = ConstantesBD.codigoViaIngresoUrgencias;
				else
					viaIngreso = ConstantesBD.codigoViaIngresoHospitalizacion;
				resumenAtencionesForm.setViaIngreso(viaIngreso);
				resumenAtencionesForm.setAsocio(true);
				resumenAtencionesForm.setCuentaAsocio(idCuentaAsocio+"");
			}
			else
				resumenAtencionesForm.setAsocio(false);
		}
		else
			idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
		
				
		//Captura la información de las cuentas del paciente
		resumenAtencionesForm.setMapaValidacionesInfoCuentas(new HashMap()); //se resetesa mapa
		resumenAtencionesForm.setMapaValidacionesInfoCuentas("numRegistros","0");
		ArrayList<CuentasPaciente> cuentasPacienteArray = ResumenAtenciones.getCuentasPaciente(con, idIngreso);
		int tamano = cuentasPacienteArray.size();
		numRegistrosEncontrados = tamano;
		logger.info("NUMERO DE CUENTAS ENCONTRADAS: "+tamano);
		for(int i = 0; i<tamano; i++)
		{
			resumenAtencionesForm.setMapaValidacionesInfoCuentas("mostrar_"+i,ConstantesBD.acronimoNo);
			
			CuentasPaciente cpacientes = (CuentasPaciente)cuentasPacienteArray.get(i);
			resumenAtencionesForm.setMapaValidacionesInfoCuentas("codigoViaIngreso_"+i,cpacientes.getCodigoViaIngreso());
			
			
			//*****SE CARGAN LOS DATOS DE LA CUENTA PRINCIPAL************************************************
			//Por defecto la primera posicion del array es la cuenta principal
			if(i == 0)			
			{
				this.cargarDatosCuentaPrincipal(con,
						resumenAtencionesForm,
						resumenAtencionesMundo,
						Integer.parseInt(cpacientes.getCodigoViaIngreso()),
						Integer.parseInt(cpacientes.getCodigoCuenta()),
						paciente,
						usuario,i);		
			
				resumenAtencionesForm.setMapaValidacionesInfoCuentas("descripcionViaIngreso_"+i,cpacientes.getDescripcionViaIngreso()+" - "+cpacientes.getDescripcionTipoPaciente());				
				resumenAtencionesForm.setMapaValidacionesInfoCuentas("codigoTipoPaciente_"+i,cpacientes.getCodigoTipoPaciente());
				resumenAtencionesForm.setMapaValidacionesInfoCuentas("idCuenta_"+i,cpacientes.getCodigoCuenta());
				resumenAtencionesForm.setMapaValidacionesInfoCuentas("numRegistros",i+1);
				resumenAtencionesForm.setMapaValidacionesInfoCuentas("mostrar_"+i,ConstantesBD.acronimoSi);
				
				
				if(UtilidadTexto.isEmpty(String.valueOf(resumenAtencionesForm.getMapaValidacionesInfoCuentas("egresoAsocio_"+i)))){
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("egresoAsocio_"+i,"-4");
				}
				
				if(UtilidadTexto.isEmpty(String.valueOf(resumenAtencionesForm.getMapaValidacionesInfoCuentas("egreso_"+i)))){
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("egreso_"+i,"-4");
				}
				
			}
			else
			{
				//*****SE CARGAN LOS DATOS DE LA CUENTA ASOCIADA (SI HAY ASOCIO)*******************************
				if(resumenAtencionesForm.isAsocio())
				{
					this.cargarDatosCuentaAsociada(con,
							resumenAtencionesForm,
							resumenAtencionesMundo,
							Integer.parseInt(cpacientes.getCodigoViaIngreso()),
							Integer.parseInt(cpacientes.getCodigoCuenta()),
							usuario,
							i,paciente);
					
					
										
					if(resumenAtencionesForm.getFiltroAsocio().equals(""))
						resumenAtencionesForm.setFiltroAsocio("A");
					
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("fechaIngreso_"+i,Utilidades.getFechaIngreso(con,cpacientes.getCodigoCuenta(),Integer.parseInt(cpacientes.getCodigoViaIngreso())));					
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("idCuenta_"+i,cpacientes.getCodigoCuenta());
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("descripcionViaIngreso_"+i,cpacientes.getDescripcionViaIngreso()+" - "+cpacientes.getDescripcionTipoPaciente());					
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("codigoTipoPaciente_"+i,cpacientes.getCodigoTipoPaciente());
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("numRegistros",i+1);
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("mostrar_"+i,ConstantesBD.acronimoSi);
					if(UtilidadTexto.isEmpty(String.valueOf(resumenAtencionesForm.getMapaValidacionesInfoCuentas("egresoAsocio_"+i)))){
						resumenAtencionesForm.setMapaValidacionesInfoCuentas("egresoAsocio_"+i,"-4");
					}
					
					if(UtilidadTexto.isEmpty(String.valueOf(resumenAtencionesForm.getMapaValidacionesInfoCuentas("egreso_"+i)))){
						resumenAtencionesForm.setMapaValidacionesInfoCuentas("egreso_"+i,"-4");
					}
				}
				else
				{
					resumenAtencionesForm.resetAsocio();
					resumenAtencionesForm.setMapaValidacionesInfoCuentas("fechaIngreso_"+i,Utilidades.getFechaIngreso(con,cpacientes.getCodigoCuenta(),Integer.parseInt(cpacientes.getCodigoViaIngreso())));
					//resumenAtencionesForm.setFechaIngreso(Utilidades.getFechaIngreso(con,idCuenta+"",viaIngreso) );
				}				
			}									
		}
		
		//Utilidades.imprimirMapa(resumenAtencionesForm.getMapaValidacionesInfoCuentas());
	
		//**********SE CONSULTA LA INFORMACION DE REFERENCIA*****************************************
		//Se consulta la referencia externa------------------------------------------------------------------
		resumenAtencionesForm.setNumeroReferenciaExterna(UtilidadesHistoriaClinica.getNumeroReferenciaIngreso(con, idIngreso+"", ConstantesIntegridadDominio.acronimoExterna));
		if(!resumenAtencionesForm.getNumeroReferenciaExterna().equals(""))
			resumenAtencionesForm.setExisteReferenciaExterna(true);
		else
			resumenAtencionesForm.setExisteReferenciaExterna(false);
		
		//Se consulta la referencia interna--------------------------------------------------------------------
		resumenAtencionesForm.setNumeroReferenciaInterna(UtilidadesHistoriaClinica.getNumeroReferenciaIngreso(con, idIngreso+"", ConstantesIntegridadDominio.acronimoInterna));
		if(!resumenAtencionesForm.getNumeroReferenciaInterna().equals(""))
			resumenAtencionesForm.setExisteReferenciaInterna(true);
		else
			resumenAtencionesForm.setExisteReferenciaInterna(false);
		
		//Se verifica si existe contrarreferencia-------------------------------------------------------------
		if(resumenAtencionesForm.isExisteReferenciaExterna())
			resumenAtencionesForm.setExisteContrarreferencia(UtilidadesHistoriaClinica.existeContrarreferencia(con, resumenAtencionesForm.getNumeroReferenciaExterna()));
		else
			resumenAtencionesForm.setExisteContrarreferencia(false);
		
		//Si hay referencia interna y externa se debe mostrar mensaje
		if(resumenAtencionesForm.isExisteReferenciaExterna()&&resumenAtencionesForm.isExisteReferenciaInterna())
			resumenAtencionesForm.setEspecificarTipoReferencia(true);
		else
			resumenAtencionesForm.setEspecificarTipoReferencia(false);
		//*******************************************************************************************
	}

	/**
	 * Método implementado para cargar la informacion de la cuenta asaociada para el caso de asocio
	 * @param con
	 * @param resumenAtencionesForm
	 * @param resumenAtencionesMundo
	 * @param idCuentaAsocio
	 * @param usuario 
	 */
	private void cargarDatosCuentaAsociada(
			Connection con, 
			ResumenAtencionesForm forma, 
			ResumenAtenciones resumenAtencionesMundo, 
			int viaIngreso,
			int idCuentaAsocio, 
			UsuarioBasico usuario, 
			int pos,
			PersonaBasica paciente) 
	{
		forma.resetContadoresSecciones();
		String fechaInicial = forma.getFechaInicial();
		String fechaFinal = forma.getFechaFinal();
		String horaInicial = forma.getHoraInicial();
		String horaFinal = forma.getHoraFinal();
		
		fechaInicial = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
		fechaFinal   = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
		String idIngreso = forma.getIdIngreso();
		
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones  ();
		HashMap campos = new HashMap();
		campos.put("ingreso",forma.getIdIngreso());
		campos.put("cuenta",idCuentaAsocio+"");
		campos.put("fechaInicial",fechaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("horaInicial",horaInicial);
		campos.put("horaFinal",horaFinal);
		String area[] = UtilidadValidacion.getAreaPaciente(con,idCuentaAsocio).split(ConstantesBD.separadorSplit);
		
		///verificacion de la seccion de la Hoja de Administracion de medicamentos*************************************
		forma.setMapaValidacionesInfoCuentas("existeAdminMedicamentosAsocio_"+pos,resumenAtencionesMundo.existeAdminMedicamentos(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//resumenAtencionesForm.setExisteAdminMedicamentosAsocio(resumenAtencionesMundo.existeAdminMedicamentos(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeAdminMedicamentos(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			//forma.setRowSpanSeccionOchoReporteHC(forma.getRowSpanSeccionOchoReporteHC()+1);
			/*forma.contadorFilasOcho(pos);
			if(pos == 2){
				forma.getRowSpanSeccionOchoReporteHC_lista().set(pos,forma.getContadorOchoUrgencias().intValue());
				int valoractual = forma.getRowSpanSeccionOchoReporteHC_lista().get(pos).intValue();
			}
			else if(pos == 1){
				forma.getRowSpanSeccionOchoReporteHC_lista().set(pos,forma.getContadorOchoHospitalizacion().intValue());
			}*/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasOcho_nuevo(forma.getViaIngreso());
				if(forma.getViaIngreso() == 1){
					forma.getRowSpanSeccionOchoReporteHC_lista().set(1,forma.getContadorOchoUrgencias().intValue());
				}//else Hospitalizacion = 3
				else if(forma.getViaIngreso() == 3){
					forma.getRowSpanSeccionOchoReporteHC_lista().set(1,forma.getContadorOchoHospitalizacion().intValue());
				}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasOcho(pos);
				if(pos == 2){
					forma.getRowSpanSeccionOchoReporteHC_lista().set(pos,forma.getContadorOchoUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionOchoReporteHC_lista().set(pos,forma.getContadorOchoHospitalizacion().intValue());
				}
			}
		}
		//***************************************************************************************************
		//verificacion de la seccion de Consumo de Insumos*************************************
		forma.setMapaValidacionesInfoCuentas("existeConsumoInsumosAsocio_"+pos,resumenAtencionesMundo.existeConsumoInsumos(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//resumenAtencionesForm.setExisteConsumoInsumosAsocio(resumenAtencionesMundo.existeConsumoInsumos(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeConsumoInsumos(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			//forma.setRowSpanSeccionOchoReporteHC(forma.getRowSpanSeccionOchoReporteHC()+1);
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasOcho_nuevo(forma.getViaIngreso());
				if(forma.getViaIngreso() == 1){
					forma.getRowSpanSeccionOchoReporteHC_lista().set(1,forma.getContadorOchoUrgencias().intValue());
				}//else Hospitalizacion = 3
				else if(forma.getViaIngreso() == 3){
					forma.getRowSpanSeccionOchoReporteHC_lista().set(1,forma.getContadorOchoHospitalizacion().intValue());
				}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasOcho(pos);
				if(pos == 2){
					forma.getRowSpanSeccionOchoReporteHC_lista().set(pos,forma.getContadorOchoUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionOchoReporteHC_lista().set(pos,forma.getContadorOchoHospitalizacion().intValue());
				}
			}
		}
		
		
		//***************************************************************************************************
		//verificacion de la seccion de procedimientos********************************************************
		forma.setMapaValidacionesInfoCuentas("existenProcedimientosAsocio_"+pos,resumenAtencionesMundo.existeRespuestaProcedimiento(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//resumenAtencionesForm.setExistenProcedimientosAsocio(resumenAtencionesMundo.existeRespuestaProcedimiento(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeRespuestaProcedimiento(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasNueve_nuevo(forma.getViaIngreso());
					if(forma.getViaIngreso() == 1){
						forma.getRowSpanSeccionNueveReporteHC_lista().set(pos,forma.getContadorNueveUrgencias().intValue());
					}//else Hospitalizacion = 3
					else if(forma.getViaIngreso() == 3){
						forma.getRowSpanSeccionNueveReporteHC_lista().set(pos,forma.getContadorNueveHospitalizacion().intValue());
					}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasNueve(pos);
				if(pos == 2){
					forma.getRowSpanSeccionNueveReporteHC_lista().set(pos,forma.getContadorNueveUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionNueveReporteHC_lista().set(pos,forma.getContadorNueveHospitalizacion().intValue());
				}
			}
		}
		//****************************************************************************************************
		//verificacion de la seccion de interconsultas********************************************************
		forma.setMapaValidacionesInfoCuentas("existenInterconsultasAsocio_"+pos,resumenAtencionesMundo.existeRespIntInterconsulta(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeRespIntInterconsulta(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			//forma.setRowSpanSeccionSeisReporteHC(forma.getRowSpanSeccionSeisReporteHC()+1);
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasSeis_nuevo(forma.getViaIngreso());
					if(forma.getViaIngreso() == 1){
						forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisUrgencias().intValue());
					}//else Hospitalizacion = 3
					else if(forma.getViaIngreso() == 3){
						forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisHospitalizacion().intValue());
					}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasSeis(pos);
				if(pos == 2){
					forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisHospitalizacion().intValue());
					
				}
			}
		}
		//resumenAtencionesForm.setExistenInterconsultasAsocio(resumenAtencionesMundo.existeRespIntInterconsulta(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		
		//******************************************************************************************************
		//verificacion de la seccion de evoluciones**********************************************************
		forma.setMapaValidacionesInfoCuentas("codigosEvolucionesAsocio_"+pos,mundo.obtenerEvoluciones(con,campos));
		//resumenAtencionesForm.setCodigosEvolucionesAsocio(mundo.obtenerEvoluciones(con,campos));
		if(Integer.parseInt(((HashMap)forma.getMapaValidacionesInfoCuentas("codigosEvolucionesAsocio_"+pos)).get("numRegistros").toString())>0){
			forma.setMapaValidacionesInfoCuentas("existenEvolucionesAsocio_"+pos,true);
			//resumenAtencionesForm.setExistenEvolucionesAsocio(true);
			//forma.setRowSpanSeccionSeisReporteHC(forma.getRowSpanSeccionSeisReporteHC()+1);
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasSeis_nuevo(forma.getViaIngreso());
					if(forma.getViaIngreso() == 1){
						forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisUrgencias().intValue());
					}//else Hospitalizacion = 3
					else if(forma.getViaIngreso() == 3){
						forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisHospitalizacion().intValue());
					}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasSeis(pos);
				if(pos == 2){
					forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisHospitalizacion().intValue());
				}
			}
		}
		else{
			forma.setMapaValidacionesInfoCuentas("existenEvolucionesAsocio_"+pos,false);
			//resumenAtencionesForm.setExistenEvolucionesAsocio(false);
		}
		
		//verificacion de la seccion de cirugia************************************************************
		forma.setMapaValidacionesInfoCuentas("existenCirugiasAsocio_"+pos,resumenAtencionesMundo.existeRespuestaCirugias(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeRespuestaCirugias(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			//forma.setRowSpanSeccionSeisReporteHC(forma.getRowSpanSeccionSeisReporteHC()+1);
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasSeis_nuevo(forma.getViaIngreso());
					if(forma.getViaIngreso() == 1){
						forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisUrgencias().intValue());
					}//else Hospitalizacion = 3
					else if(forma.getViaIngreso() == 3){
						forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisHospitalizacion().intValue());
					}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasSeis(pos);
				if(pos == 2){
					forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisHospitalizacion().intValue());
					
				}
			}
		}
		
		forma.setMapaValidacionesInfoCuentas("existeValoracionEnfermeria_"+pos,resumenAtencionesMundo.existeValoracionEnfermeria(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeValoracionEnfermeria(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			/*forma.setRowSpanSeccionSieteReporteHC(forma.getRowSpanSeccionSieteReporteHC()+1);*/
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasSiete_nuevo(forma.getViaIngreso());
				if(forma.getViaIngreso() == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}//else Hospitalizacion = 3
				else if(forma.getViaIngreso() == 3){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
				}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasSiete(pos);
				if(pos == 2){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
					
				}
			}
		}
		
		//resumenAtencionesForm.setExistenCirugiasAsocio(resumenAtencionesMundo.existeRespuestaCirugias(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		
		//*****************************************************************************************************
		//verificacion de la seccion de ordenes medicas ***************************************************
		boolean existeOrdenMedica = resumenAtencionesMundo.existeOrdenesMedicas(con,idIngreso,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal);
		boolean existeCirugias = resumenAtencionesMundo.tieneSolicitudDe(con,idCuentaAsocio,ConstantesBD.codigoTipoSolicitudCirugia,fechaInicial,fechaFinal,horaInicial,horaFinal);
		boolean existeProcedimientos = resumenAtencionesMundo.tieneSolicitudDe(con,idCuentaAsocio,ConstantesBD.codigoTipoSolicitudProcedimiento,fechaInicial,fechaFinal,horaInicial,horaFinal);
		boolean existeMedicamentos = resumenAtencionesMundo.tieneSolicitudDe(con,idCuentaAsocio,ConstantesBD.codigoTipoSolicitudMedicamentos,fechaInicial,fechaFinal,horaInicial,horaFinal);
		boolean existeInterconsultas = resumenAtencionesMundo.tieneSolicitudDe(con,idCuentaAsocio,ConstantesBD.codigoTipoSolicitudInterconsulta,fechaInicial,fechaFinal,horaInicial,horaFinal);
		
		if(existeOrdenMedica||existeCirugias||existeProcedimientos||existeMedicamentos||existeInterconsultas){
			forma.setMapaValidacionesInfoCuentas("existenOrdenesMedicasAsocio_"+pos,true);
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasSeis_nuevo(forma.getViaIngreso());
					if(forma.getViaIngreso() == 1){
						forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisUrgencias().intValue());
					}//else Hospitalizacion = 3
					else if(forma.getViaIngreso() == 3){
						forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisHospitalizacion().intValue());
					}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasSeis(pos);
				if(pos == 2){
					forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisHospitalizacion().intValue());
				}
			}
		}
		else{
			forma.setMapaValidacionesInfoCuentas("existenOrdenesMedicasAsocio_"+pos,false);
			//resumenAtencionesForm.setExistenOrdenesMedicasAsocio(false);
		}
		
		//********************************************************************************************
		//Verificación de la seccion de Cargos Directos
		boolean existeCargoDirectoCirugias = resumenAtencionesMundo.tieneSolicitudCargoDirectoDe(
				con,
				idCuentaAsocio,
				ConstantesBD.codigoTipoSolicitudCirugia,
				fechaInicial,
				fechaFinal,
				horaInicial,
				horaFinal);
		
		boolean existeCargoDirectoServicios = resumenAtencionesMundo.tieneSolicitudCargoDirectoDe(
				con,
				idCuentaAsocio,
				ConstantesBD.codigoTipoSolicitudCargosDirectosServicios,
				fechaInicial,
				fechaFinal,
				horaInicial,
				horaFinal);
		
		boolean existeCargoDirectoArticulos = resumenAtencionesMundo.tieneSolicitudCargoDirectoDe(
				con,
				idCuentaAsocio,
				ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos,
				fechaInicial,
				fechaFinal,
				horaInicial,
				horaFinal);
		
		if(existeCargoDirectoCirugias || existeCargoDirectoServicios || existeCargoDirectoArticulos)
			forma.setMapaValidacionesInfoCuentas("existeCargosDirectos_"+pos,true);
			//resumenAtencionesForm.setExisteCargosDirectos(true);
		else
			forma.setMapaValidacionesInfoCuentas("existeCargosDirectos_"+pos,false);
			//resumenAtencionesForm.setExisteCargosDirectos(false);	
		//********************************************************************************************
		
		forma.setMapaValidacionesInfoCuentas("egresoAsocio_"+pos,resumenAtencionesMundo.codigoEvolucionGeneroEgreso(con, idCuentaAsocio,fechaInicial,fechaFinal,horaInicial,horaFinal));
		//resumenAtencionesForm.setEgresoAsocio(resumenAtencionesMundo.codigoEvolucionGeneroEgreso(con, idCuentaAsocio,fechaInicial,fechaFinal,horaInicial,horaFinal));
		
		
		//**********VERIFICACION SECCIONES DEL REGISTRO DE ENFERMERÍA********************************************************************
		RegistroEnfermeria mundoEnfer=new RegistroEnfermeria();
		//Anotaciones de enfermeria
		forma.setMapaValidacionesInfoCuentas("existeNotasEnfermeriaAsocio_"+pos,resumenAtencionesMundo.existeAnotacionesEnfermeria(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//resumenAtencionesForm.setExisteNotasEnfermeriaAsocio(resumenAtencionesMundo.existeAnotacionesEnfermeria(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeAnotacionesEnfermeria(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			//forma.setRowSpanSeccionSieteReporteHC(forma.getRowSpanSeccionSieteReporteHC()+1);
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasSiete_nuevo(forma.getViaIngreso());
				if(forma.getViaIngreso() == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}//else Hospitalizacion = 3
				else if(forma.getViaIngreso() == 3){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
				}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasSiete(pos);
				if(pos == 2){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
				}
			}
		}
		
		
		forma.setMapaValidacionesInfoCuentas("existenNotasGeneralesCirugiaAsocio_"+pos, resumenAtencionesMundo.existeNotasGeneralesAsocio(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal,paciente.getCodigoPersona()));
		if(resumenAtencionesMundo.existeNotasGeneralesAsocio(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal,paciente.getCodigoPersona())){
			//forma.setRowSpanSeccionSieteReporteHC(forma.getRowSpanSeccionSieteReporteHC()+1);
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasSiete_nuevo(forma.getViaIngreso());
				if(forma.getViaIngreso() == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}//else Hospitalizacion = 3
				else if(forma.getViaIngreso() == 3){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
				}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasSiete(pos);
				if(pos == 2){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
				}
			}
		}
		
		try {
			forma.setMapaValidacionesInfoCuentas("existenNotasRecuperacionAsocio_"+pos, resumenAtencionesMundo.existenNotasRecuperacionAsocio(con,idCuentaAsocio+"",paciente.getCodigoPersona()));
			if(resumenAtencionesMundo.existenNotasRecuperacionAsocio(con,idCuentaAsocio+"",paciente.getCodigoPersona())){
				//forma.setRowSpanSeccionSieteReporteHC(forma.getRowSpanSeccionSieteReporteHC()+1);
				/**
				* Tipo Modificacion: Segun incidencia 6480
				* Autor: Alejandro Aguirre Luna
				* usuario: aleagulu
				* Fecha: 19/02/2013
				* Descripcion: Se valida el número de seccionies que tiene el paciente, 
				* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
				* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
				* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
				* secciones se debe validar por la via de ingreso para establecer si la
				* sección que esta apareciendo es de Urgencias u Hospitalización. 
				**/
				if(numRegistrosEncontrados == 2){
					forma.contadorFilasSiete_nuevo(forma.getViaIngreso());
					if(forma.getViaIngreso() == 1){
						forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
					}//else Hospitalizacion = 3
					else if(forma.getViaIngreso() == 3){
						forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
					}
				}
				else if(numRegistrosEncontrados == 3){
					forma.contadorFilasSiete(pos);
					if(pos == 2){
						forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
					}
					else if(pos == 1){
						forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
						
					}
				}
			}
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Signos Vitales
		forma.setMapaValidacionesInfoCuentas("existeSignosVitalesAsocio_"+pos,resumenAtencionesMundo.existeSignosVitales(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//resumenAtencionesForm.setExisteSignosVitalesAsocio(resumenAtencionesMundo.existeSignosVitales(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeSignosVitales(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			//forma.setRowSpanSeccionSieteReporteHC(forma.getRowSpanSeccionSieteReporteHC()+1);
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasSiete_nuevo(forma.getViaIngreso());
				if(forma.getViaIngreso() == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}//else Hospitalizacion = 3
				else if(forma.getViaIngreso() == 3){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
				}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasSiete(pos);
				if(pos == 2){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
					
				}
			}
		}
		
		
		//Cateter y Sonda
		forma.setMapaValidacionesInfoCuentas("existeCateterSondaAsocio_"+pos,resumenAtencionesMundo.existeCateterSonda(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//resumenAtencionesForm.setExisteCateterSondaAsocio(resumenAtencionesMundo.existeCateterSonda(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeCateterSonda(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			//forma.setRowSpanSeccionSieteReporteHC(forma.getRowSpanSeccionSieteReporteHC()+1);
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasSiete_nuevo(forma.getViaIngreso());
				if(forma.getViaIngreso() == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}//else Hospitalizacion = 3
				else if(forma.getViaIngreso() == 3){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
				}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasSiete(pos);
				if(pos == 2){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
					
				}
			}
		}
		
		
		//Control de liquidos		
		this.cargarControlLiquidos(con,forma,fechaInicial,fechaFinal,horaInicial,horaFinal,idCuentaAsocio,area[0],usuario.getCodigoInstitucion(),mundoEnfer,true,0,pos);
		
		//Soporte Respiratorio
		forma.setMapaValidacionesInfoCuentas("existeSoporteRespiratorioAsocio_"+pos,resumenAtencionesMundo.existeSoporteRespiratorio(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//resumenAtencionesForm.setExisteSoporteRespiratorioAsocio(resumenAtencionesMundo.existeSoporteRespiratorio(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeSoporteRespiratorio(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			//forma.setRowSpanSeccionSieteReporteHC(forma.getRowSpanSeccionSieteReporteHC()+1);
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasSiete_nuevo(forma.getViaIngreso());
				if(forma.getViaIngreso() == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}//else Hospitalizacion = 3
				else if(forma.getViaIngreso() == 3){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
				}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasSiete(pos);
				if(pos == 2){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
					
				}
			}
		}
		//Cuidados Especiales
		forma.setMapaValidacionesInfoCuentas("existeCuidadosEspecialesAsocio_"+pos,resumenAtencionesMundo.existeCuidadosEspeciales(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//resumenAtencionesForm.setExisteCuidadosEspecialesAsocio(resumenAtencionesMundo.existeCuidadosEspeciales(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeCuidadosEspeciales(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			//forma.setRowSpanSeccionSieteReporteHC(forma.getRowSpanSeccionSieteReporteHC()+1);
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasSiete_nuevo(forma.getViaIngreso());
				if(forma.getViaIngreso() == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}//else Hospitalizacion = 3
				else if(forma.getViaIngreso() == 3){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
				}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasSiete(pos);
				if(pos == 2){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
					
				}
			}
		}
		//Hoja Neurológica
		forma.setMapaValidacionesInfoCuentas("existeHojaNeurologicaAsocio_"+pos,resumenAtencionesMundo.existeHojaNeurologica(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//resumenAtencionesForm.setExisteHojaNeurologicaAsocio(resumenAtencionesMundo.existeHojaNeurologica(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeHojaNeurologica(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			//forma.setRowSpanSeccionSieteReporteHC(forma.getRowSpanSeccionSieteReporteHC()+1);
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasSiete_nuevo(forma.getViaIngreso());
				if(forma.getViaIngreso() == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}//else Hospitalizacion = 3
				else if(forma.getViaIngreso() == 3){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
				}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasSiete(pos);
				if(pos == 2){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionSieteReporteHC_lista().set(pos,forma.getContadorSieteHospitalizacion().intValue());
					
				}
			}
		}
		
		
		//********************************************************************************************************************************
		
		
		//*****************VALIDACION DE CONSULTAS PYP****************************************************
		
		forma.setConsultasPYPAsocio("consultasPYPAsocio_"+pos,resumenAtencionesMundo.listarConsultasPYP(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		forma.setConsultasPYPAsocio("numConsultasPYPAsocio_"+pos,(Integer.parseInt(((HashMap)forma.getConsultasPYPAsocio("consultasPYPAsocio_"+pos)).get("numRegistros").toString())));
		if(forma.getNumConsultasPYPAsocio()>0){
			forma.setMapaValidacionesInfoCuentas("existeConsultasPYPAsocio",true);
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasSeis_nuevo(forma.getViaIngreso());
					if(forma.getViaIngreso() == 1){
						forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisUrgencias().intValue());
					}//else Hospitalizacion = 3
					else if(forma.getViaIngreso() == 3){
						forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisHospitalizacion().intValue());
					}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasSeis(pos);
				if(pos == 2){
					forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisHospitalizacion().intValue());
				}
			}
		}
		else{
			forma.setMapaValidacionesInfoCuentas("existeConsultasPYPAsocio",false);
			//forma.setExisteConsultasPYPAsocio(false);
		}
		//*************************************************************************************************
		
		
		//******************************************************************
		int codigoConducta =  UtilidadesHistoriaClinica.obtenerCodigoConductaValoracionUrgenciasCuenta(con,idCuentaAsocio+"");
		
		//SE verifica cual fue la conducta a seguir de la valoración de urgencias
		if(codigoConducta==ConstantesBD.codigoConductaSeguirSalaEspera||
			codigoConducta==ConstantesBD.codigoConductaSeguirInterconsulta||
			codigoConducta==ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria||
			codigoConducta==ConstantesBD.codigoConductaSeguirSalaReanimacion)
		{
			//se usa un método que consulte el acronimo del diagnostico principal para saber su hubo un semiEgreso
			if(!forma.getMapaValidacionesInfoCuentas("egresoAsocio_"+pos).toString().equals("0"))
				forma.setMapaValidacionesInfoCuentas("egresoAsocio_"+pos,resumenAtencionesMundo.busquedaDiagnosticoPrincipal(con, idCuentaAsocio));
				//resumenAtencionesForm.setEgresoAsocio(resumenAtencionesMundo.busquedaDiagnosticoPrincipal(con, idCuentaAsocio));
		}
		if(codigoConducta==ConstantesBD.codigoConductaSeguirHospitalizarPiso||
			codigoConducta==ConstantesBD.codigoConductaSeguirRemitirMayorComplejidad||
			codigoConducta==ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria||
			codigoConducta==ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial||
			codigoConducta==ConstantesBD.codigoConductaSeguirSalidaSinObservacion){
			//se usa el valor -2 para indicar que va a ser un link Egreso para las conductas a seguir
			if(!forma.getMapaValidacionesInfoCuentas("egresoAsocio_"+pos).toString().equals("0"))			
				forma.setMapaValidacionesInfoCuentas("egresoAsocio_"+pos,"-2");				
			}
		
		
		if(!String.valueOf(forma.getMapaValidacionesInfoCuentas("egresoAsocio_"+pos)).equals("0")){
			if(!String.valueOf(resumenAtencionesMundo.codigoEvolucionGeneroEgreso(con, idCuentaAsocio,fechaInicial,fechaFinal,horaInicial,horaFinal)).equals("0")
					&& !String.valueOf(resumenAtencionesMundo.codigoEvolucionGeneroEgreso(con, idCuentaAsocio,fechaInicial,fechaFinal,horaInicial,horaFinal)).equals("-1")
					&& !String.valueOf(resumenAtencionesMundo.codigoEvolucionGeneroEgreso(con, idCuentaAsocio,fechaInicial,fechaFinal,horaInicial,horaFinal)).equals("-2")){
				//forma.setRowSpanSeccionSeisReporteHC(forma.getRowSpanSeccionSeisReporteHC()+1);
			}

			if(String.valueOf(forma.getMapaValidacionesInfoCuentas("egresoAsocio_"+pos)).equals("-1")){
				//forma.setRowSpanSeccionSeisReporteHC(forma.getRowSpanSeccionSeisReporteHC()+1);
			}

			if(String.valueOf(forma.getMapaValidacionesInfoCuentas("egresoAsocio_"+pos)).equals("-2")){
				//forma.setRowSpanSeccionSeisReporteHC(forma.getRowSpanSeccionSeisReporteHC()+1);
			}

		}
		
		
		
		
		
		
		
		//******************************************************************
		//logger.info("consultare citas ");
		
		//resumenAtencionesForm.setExistenCitas(resumenAtencionesMundo.existenCitas(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal));
		
		if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
			forma.setMapaValidacionesInfoCuentas("datosAdmision_"+pos,resumenAtencionesMundo.busquedaDatosAdmisionHospitalizacion(con, idCuentaAsocio));					
		else		
			forma.setMapaValidacionesInfoCuentas("datosAdmision_"+pos,resumenAtencionesMundo.busquedaDatosAdmisionUrgencias(con, idCuentaAsocio));
		
		//resumenAtencionesForm.setDatosAdmisionAsocio(resumenAtencionesMundo.busquedaDatosAdmisionUrgencias(con, idCuentaAsocio));
		forma.setMapaValidacionesInfoCuentas("existeValoracionInicialAsocio_"+pos,resumenAtencionesMundo.tieneValoracionInicial(con, idCuentaAsocio, true,fechaInicial,fechaFinal,horaInicial,horaFinal));	
		forma.setMapaValidacionesInfoCuentas("existenCitasAsocio_"+pos,resumenAtencionesMundo.existenCitas(con, idCuentaAsocio,fechaInicial,fechaFinal,horaInicial,horaFinal));
		
		if(resumenAtencionesMundo.tieneValoracionInicial(con, idCuentaAsocio, true,fechaInicial,fechaFinal,horaInicial,horaFinal)  ||
				resumenAtencionesMundo.existenCitas(con, idCuentaAsocio,fechaInicial,fechaFinal,horaInicial,horaFinal)	){
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasSeis_nuevo(forma.getViaIngreso());
					if(forma.getViaIngreso() == 1){
						forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisUrgencias().intValue());
					}//else Hospitalizacion = 3
					else if(forma.getViaIngreso() == 3){
						forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisHospitalizacion().intValue());
					}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasSeis(pos);
				if(pos == 2){
					forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionSeisReporteHC_lista().set(pos,forma.getContadorSeisHospitalizacion().intValue());
					
				}
			}
		}
		
		/**
		* Tipo Modificacion: Segun incidencia 6510
		* Autor: Alejandro Aguirre Luna
		* usuario: aleagulu
		* Fecha: 19/02/2013
		* Descripcion: Se pone en comentario la siguiente lìnea que permitia 
		* visualizar permanentemente la seccion otros. Ahora es necesario que 
		* siempre aparezca la sección de Notas aclaratorias, pero no la de 
		* Resumen Paricial de HC (solo cuando tenga información).
		**/
		//forma.setMuestroResumenPHCAsocio(1);
		if(forma.getMuestroResumenPHCAsocio()>0){
			/**
			* Tipo Modificacion: Segun incidencia 6480
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 19/02/2013
			* Descripcion: Se valida el número de seccionies que tiene el paciente, 
			* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
			* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
			* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
			* secciones se debe validar por la via de ingreso para establecer si la
			* sección que esta apareciendo es de Urgencias u Hospitalización. 
			**/
			if(numRegistrosEncontrados == 2){
				forma.contadorFilasDiez_nuevo(forma.getViaIngreso());
					if(forma.getViaIngreso() == 1){
						forma.getRowSpanSeccionDiezReporteHC_lista().set(pos,forma.getContadorDiezUrgencias().intValue());
					}//else Hospitalizacion = 3
					else if(forma.getViaIngreso() == 3){
						forma.getRowSpanSeccionDiezReporteHC_lista().set(pos,forma.getContadorDiezHospitalizacion().intValue());
					}
			}
			else if(numRegistrosEncontrados == 3){
				forma.contadorFilasDiez(pos);
				if(pos == 2){
					forma.getRowSpanSeccionDiezReporteHC_lista().set(pos,forma.getContadorDiezUrgencias().intValue());
				}
				else if(pos == 1){
					forma.getRowSpanSeccionDiezReporteHC_lista().set(pos,forma.getContadorDiezHospitalizacion().intValue());
					
				}
			}
		}

		//resumenAtencionesForm.setExisteValoracionInicialAsocio(resumenAtencionesMundo.tieneValoracionInicial(con, idCuentaAsocio, true,fechaInicial,fechaFinal,horaInicial,horaFinal));
		
		forma.setMapaValidacionesInfoCuentas("numeroValoracionInicialAsocio_"+pos,resumenAtencionesMundo.numeroValoracionInicial(con, idCuentaAsocio));	
		//resumenAtencionesForm.setTipoValoracionInicialAsocio(resumenAtencionesMundo.tipoValoracionInicial(con, idCuentaAsocio));		
	}

	
	public void cargarDatosCirugia(Connection con, 
			ResumenAtencionesForm forma, 
			ResumenAtenciones resumenAtencionesMundo, 
			int viaIngreso, 
			int idCuenta, 
			PersonaBasica paciente, 
			UsuarioBasico usuario,
			int pos){
		String fechaInicial = forma.getFechaInicial();
		String fechaFinal = forma.getFechaFinal();
		String horaInicial = forma.getHoraInicial();
		String horaFinal = forma.getHoraFinal();
		
		
		fechaInicial = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
		fechaFinal = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
		String idIngreso = forma.getIdIngreso();
		
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones  ();
		HashMap campos = new HashMap();
		campos.put("ingreso",forma.getIdIngreso());
		campos.put("cuenta",idCuenta+"");
		campos.put("fechaInicial",fechaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("horaInicial",horaInicial);
		campos.put("horaFinal",horaFinal);
		String area[] = UtilidadValidacion.getAreaPaciente(con,idCuenta).split(ConstantesBD.separadorSplit);
		
		resumenAtencionesMundo.existeRespuestaCirugiasSolicitudes(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal);
		
		
		
	}
	
	
	
	/**
	 * Método implementado para cargar la informacion de la cuenta principal o única del ingreso
	 * @param con
	 * @param resumenAtencionesForm
	 * @param resumenAtencionesMundo
	 * @param viaIngreso
	 * @param idCuenta
	 * @param paciente
	 * @param usuario 
	 */
	private void cargarDatosCuentaPrincipal(
			Connection con, 
			ResumenAtencionesForm forma, 
			ResumenAtenciones resumenAtencionesMundo, 
			int viaIngreso, 
			int idCuenta, 
			PersonaBasica paciente, 
			UsuarioBasico usuario,
			int pos) 
	{
		String fechaInicial = forma.getFechaInicial();
		String fechaFinal = forma.getFechaFinal();
		String horaInicial = forma.getHoraInicial();
		String horaFinal = forma.getHoraFinal();
		
		
		fechaInicial = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
		fechaFinal = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
		String idIngreso = forma.getIdIngreso();
		
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones  ();
		HashMap campos = new HashMap();
		campos.put("ingreso",forma.getIdIngreso());
		campos.put("cuenta",idCuenta+"");
		campos.put("fechaInicial",fechaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("horaInicial",horaInicial);
		campos.put("horaFinal",horaFinal);
		String area[] = UtilidadValidacion.getAreaPaciente(con,idCuenta).split(ConstantesBD.separadorSplit);
		
		
		
		
		try{
		//logger.info("consultare admin medicamentos ");
		///verificacion de la seccion de la Hoja de Administracion de medicamentos*************************************
		forma.setMapaValidacionesInfoCuentas("existeAdminMedicamentos_"+pos,resumenAtencionesMundo.existeAdminMedicamentos(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeAdminMedicamentos(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			forma.setRowSpanSeccionTresReporteHC(forma.getRowSpanSeccionTresReporteHC()+1);
		}
		
		//resumenAtencionesForm.setExisteAdminMedicamentos(resumenAtencionesMundo.existeAdminMedicamentos(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//logger.info("consultare insumos ");
		//***************************************************************************************************
		//verificacion de la seccion de Consumo de Insumos*************************************
		forma.setMapaValidacionesInfoCuentas("existeConsumoInsumos_"+pos,resumenAtencionesMundo.existeConsumoInsumos(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		 if(resumenAtencionesMundo.existeConsumoInsumos(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
				forma.setRowSpanSeccionTresReporteHC(forma.getRowSpanSeccionTresReporteHC()+1);
			}
		//resumenAtencionesForm.setExisteConsumoInsumos(resumenAtencionesMundo.existeConsumoInsumos(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//logger.info("consultare respuesta procedimientos ");
		//***************************************************************************************************
		//verificacion de la seccion de procedimientos********************************************************
		forma.setMapaValidacionesInfoCuentas("existenProcedimientos_"+pos,resumenAtencionesMundo.existeRespuestaProcedimiento(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeRespuestaProcedimiento(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			forma.setRowSpanSeccionCuatroReporteHC(forma.getRowSpanSeccionCuatroReporteHC()+1);
		}
		
		//resumenAtencionesForm.setExistenProcedimientos(resumenAtencionesMundo.existeRespuestaProcedimiento(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//logger.info("consultare respuesta interconsultas ");
		//****************************************************************************************************
		//verificacion de la seccion de interconsultas********************************************************
		forma.setMapaValidacionesInfoCuentas("existenInterconsultas_"+pos,resumenAtencionesMundo.existeRespIntInterconsulta(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeRespIntInterconsulta(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			forma.setRowSpanSeccionUnoReporteHC(forma.getRowSpanSeccionUnoReporteHC()+1);
		}
		
		//resumenAtencionesForm.setExistenInterconsultas(resumenAtencionesMundo.existeRespIntInterconsulta(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		
		//logger.info("consultare evolciones ");
		//******************************************************************************************************
		//verificacion de la seccion de evoluciones**********************************************************
		forma.setMapaValidacionesInfoCuentas("codigosEvoluciones_"+pos,mundo.obtenerEvoluciones(con,campos));
		//resumenAtencionesForm.setCodigosEvoluciones(mundo.obtenerEvoluciones(con,campos));
		if(Integer.parseInt(((HashMap)forma.getMapaValidacionesInfoCuentas("codigosEvoluciones_"+pos)).get("numRegistros").toString())>0){
			forma.setMapaValidacionesInfoCuentas("existenEvoluciones_"+pos,true);

			if(Integer.parseInt(((HashMap)forma.getMapaValidacionesInfoCuentas("codigosEvoluciones_"+pos)).get("numRegistros").toString())>0){
				forma.setRowSpanSeccionUnoReporteHC(forma.getRowSpanSeccionUnoReporteHC()+1);
			}
		}	//resumenAtencionesForm.setExistenEvoluciones(true);
		else{
			forma.setMapaValidacionesInfoCuentas("existenEvoluciones_"+pos,false);
			//resumenAtencionesForm.setExistenEvoluciones(false);
		}
		//logger.info("consultare cirugias ");
		//verificacion de la seccion de cirugia************************************************************
		forma.setMapaValidacionesInfoCuentas("existenCirugias_"+pos,resumenAtencionesMundo.existeRespuestaCirugias(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		
		if(resumenAtencionesMundo.existeRespuestaCirugias(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			forma.setRowSpanSeccionUnoReporteHC(forma.getRowSpanSeccionUnoReporteHC()+1);
		}
		
		//resumenAtencionesForm.setExistenCirugias(resumenAtencionesMundo.existeRespuestaCirugias(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		
		//*****************************************************************************************************
		//verificacion de la seccion de ordenes medicas ***************************************************
		//logger.info("consultare ordenes ");
		boolean existeOrdenMedica = resumenAtencionesMundo.existeOrdenesMedicas(con,idIngreso,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal);
		boolean existeCirugias = resumenAtencionesMundo.tieneSolicitudDe(con,idCuenta,ConstantesBD.codigoTipoSolicitudCirugia,fechaInicial,fechaFinal,horaInicial,horaFinal);
		boolean existeProcedimientos = resumenAtencionesMundo.tieneSolicitudDe(con,idCuenta,ConstantesBD.codigoTipoSolicitudProcedimiento,fechaInicial,fechaFinal,horaInicial,horaFinal);
		boolean existeMedicamentos = resumenAtencionesMundo.tieneSolicitudDe(con,idCuenta,ConstantesBD.codigoTipoSolicitudMedicamentos,fechaInicial,fechaFinal,horaInicial,horaFinal);
		boolean existeInterconsultas = resumenAtencionesMundo.tieneSolicitudDe(con,idCuenta,ConstantesBD.codigoTipoSolicitudInterconsulta,fechaInicial,fechaFinal,horaInicial,horaFinal);
		
		if(existeOrdenMedica||existeCirugias||existeProcedimientos||existeMedicamentos||existeInterconsultas){
			forma.setMapaValidacionesInfoCuentas("existenOrdenesMedicas_"+pos,true);
			//resumenAtencionesForm.setExistenOrdenesMedicas(true);
			forma.setRowSpanSeccionUnoReporteHC(forma.getRowSpanSeccionUnoReporteHC()+1);
		}
		else{
			forma.setMapaValidacionesInfoCuentas("existenOrdenesMedicas_"+pos,false);
		}
			//resumenAtencionesForm.setExistenOrdenesMedicas(false);	
		
		//***************** Verificación Ordenes Ambulatorias *******************
		boolean existeOrdenesAmbulatorias = resumenAtencionesMundo.tieneOrdenesAmbulatorias(con,idIngreso,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal);
		forma.setMapaValidacionesInfoCuentas("existenOrdenesAmbulatorias_"+pos,existeOrdenesAmbulatorias);
		if(existeOrdenesAmbulatorias){
			forma.setRowSpanSeccionCincoReporteHC(forma.getRowSpanSeccionCincoReporteHC()+1);
		}
		
		
		
		
		//********************************************************************************************
		//Verificación de la seccion de Cargos Directos
		boolean existeCargoDirectoCirugias = resumenAtencionesMundo.tieneSolicitudCargoDirectoDe(
				con,
				idCuenta,
				ConstantesBD.codigoTipoSolicitudCirugia,
				fechaInicial,
				fechaFinal,
				horaInicial,
				horaFinal);
		
		boolean existeCargoDirectoServicios = resumenAtencionesMundo.tieneSolicitudCargoDirectoDe(
				con,
				idCuenta,
				ConstantesBD.codigoTipoSolicitudCargosDirectosServicios,
				fechaInicial,
				fechaFinal,
				horaInicial,
				horaFinal);
		
		boolean existeCargoDirectoArticulos = resumenAtencionesMundo.tieneSolicitudCargoDirectoDe(
				con,
				idCuenta,
				ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos,
				fechaInicial,
				fechaFinal,
				horaInicial,
				horaFinal);
		
		if(existeCargoDirectoCirugias || existeCargoDirectoServicios || existeCargoDirectoArticulos)
			forma.setMapaValidacionesInfoCuentas("existeCargosDirectos_"+pos,true);
			//resumenAtencionesForm.setExisteCargosDirectos(true);
		else
			forma.setMapaValidacionesInfoCuentas("existeCargosDirectos_"+pos,false);
			//resumenAtencionesForm.setExisteCargosDirectos(false);	
		//********************************************************************************************			
	

		
		
		forma.setMapaValidacionesInfoCuentas("existeValoracionEnfermeria_"+pos,resumenAtencionesMundo.existeValoracionEnfermeria(con,forma.getIdIngreso(),fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeValoracionEnfermeria(con,forma.getIdIngreso(),fechaInicial,fechaFinal,horaInicial,horaFinal)){
			forma.setRowSpanSeccionDosReporteHC(forma.getRowSpanSeccionDosReporteHC()+1);
		}
		
		
		forma.setMapaValidacionesInfoCuentas("existeResultadosLaboratorios_"+pos,resumenAtencionesMundo.existeResultadosLaboratorios(con,forma.getIdIngreso(),fechaInicial,fechaFinal,horaInicial,horaFinal));
		
		

		
		//logger.info("consultare egresos ");
		forma.setMapaValidacionesInfoCuentas("egreso_"+pos,resumenAtencionesMundo.codigoEvolucionGeneroEgreso(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal));
		
		
		
		//resumenAtencionesForm.setEgreso(resumenAtencionesMundo.codigoEvolucionGeneroEgreso(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal));		
		//**********VERIFICACION SECCIONES DEL REGISTRO DE ENFERMERÍA********************************************************************
		RegistroEnfermeria mundoEnfer=new RegistroEnfermeria();
		
		//logger.info("consultare notas enfemeria ");
		//Anotaciones de enfermeria
		forma.setMapaValidacionesInfoCuentas("existeNotasEnfermeria_"+pos,resumenAtencionesMundo.existeAnotacionesEnfermeria(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeAnotacionesEnfermeria(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			forma.setRowSpanSeccionDosReporteHC(forma.getRowSpanSeccionDosReporteHC()+1);
		}
		
		//resumenAtencionesForm.setExisteNotasEnfermeria(resumenAtencionesMundo.existeAnotacionesEnfermeria(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//logger.info("consultare signos vitales ");
		//Signos Vitales
		forma.setMapaValidacionesInfoCuentas("existeSignosVitales_"+pos,resumenAtencionesMundo.existeSignosVitales(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeSignosVitales(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			forma.setRowSpanSeccionDosReporteHC(forma.getRowSpanSeccionDosReporteHC()+1);
		}
		
		//resumenAtencionesForm.setExisteSignosVitales(resumenAtencionesMundo.existeSignosVitales(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		
		//logger.info("consultare cateter sondas ");
		//Cateter y Sonda
		forma.setMapaValidacionesInfoCuentas("existeCateterSonda_"+pos,resumenAtencionesMundo.existeCateterSonda(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		
		if(resumenAtencionesMundo.existeCateterSonda(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			forma.setRowSpanSeccionDosReporteHC(forma.getRowSpanSeccionDosReporteHC()+1);
		}
		
		//resumenAtencionesForm.setExisteCateterSonda(resumenAtencionesMundo.existeCateterSonda(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//logger.info("consultare control liquidos ");
		//Control de liquidos
		this.cargarControlLiquidos(con,forma,fechaInicial,fechaFinal,horaInicial,horaFinal,idCuenta,area[0],usuario.getCodigoInstitucion(),mundoEnfer,false,0,pos);
		//logger.info("consultare soprte respiratorio ");
		//Soporte Respiratorio
		forma.setMapaValidacionesInfoCuentas("existeSoporteRespiratorio_"+pos,resumenAtencionesMundo.existeSoporteRespiratorio(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		
		if(resumenAtencionesMundo.existeSoporteRespiratorio(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			forma.setRowSpanSeccionDosReporteHC(forma.getRowSpanSeccionDosReporteHC()+1);
		}
		
		//resumenAtencionesForm.setExisteSoporteRespiratorio(resumenAtencionesMundo.existeSoporteRespiratorio(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//logger.info("consultare cuidados especiales ");
		//Cuidados Especiales
		forma.setMapaValidacionesInfoCuentas("existeCuidadosEspeciales_"+pos,resumenAtencionesMundo.existeCuidadosEspeciales(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeCuidadosEspeciales(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			forma.setRowSpanSeccionDosReporteHC(forma.getRowSpanSeccionDosReporteHC()+1);
		}
		
		//resumenAtencionesForm.setExisteCuidadosEspeciales(resumenAtencionesMundo.existeCuidadosEspeciales(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//logger.info("consultare hoja neuro ");
		//Hoja Neurológica
		forma.setMapaValidacionesInfoCuentas("existeHojaNeurologica_"+pos,resumenAtencionesMundo.existeHojaNeurologica(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		if(resumenAtencionesMundo.existeHojaNeurologica(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal)){
			forma.setRowSpanSeccionDosReporteHC(forma.getRowSpanSeccionDosReporteHC()+1);
		}
		
		//resumenAtencionesForm.setExisteHojaNeurologica(resumenAtencionesMundo.existeHojaNeurologica(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//********************************************************************************************************************************
		
		//logger.info("consultare consultas pyp ");
		//*****************VALIDACION DE CONSULTAS PYP****************************************************
		forma.setMapaValidacionesInfoCuentas("consultasPYP_"+pos,resumenAtencionesMundo.listarConsultasPYP(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		//resumenAtencionesForm.setConsultasPYP(resumenAtencionesMundo.listarConsultasPYP(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
		forma.setMapaValidacionesInfoCuentas("numConsultasPYP_"+pos,((HashMap)forma.getMapaValidacionesInfoCuentas("consultasPYP_"+pos)).get("numRegistros").toString());
		if(Integer.parseInt(((HashMap)forma.getMapaValidacionesInfoCuentas("consultasPYP_"+pos)).get("numRegistros").toString()) > 0){
			forma.setMapaValidacionesInfoCuentas("existeConsultasPYP_"+pos,true);	
			
			if(Integer.parseInt(((HashMap)forma.getMapaValidacionesInfoCuentas("consultasPYP_"+pos)).get("numRegistros").toString()) > 0){
				forma.setRowSpanSeccionUnoReporteHC(forma.getRowSpanSeccionUnoReporteHC()+1);
			}
		}
		else{ 
			forma.setMapaValidacionesInfoCuentas("existeConsultasPYP_"+pos,false);
		}
		//*************************************************************************************************
		
		if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			//logger.info("datos admisnion hosp ");
			forma.setMapaValidacionesInfoCuentas("datosAdmision_"+pos,resumenAtencionesMundo.busquedaDatosAdmisionHospitalizacion(con, idCuenta));	
			//resumenAtencionesForm.setDatosAdmision(resumenAtencionesMundo.busquedaDatosAdmisionHospitalizacion(con, idCuenta));
			
			//MT-5571
			if(!resumenAtencionesMundo.valoracionHospEsCopiaValoracionUrg(con, idCuenta))
			{
				forma.setMapaValidacionesInfoCuentas("existeValoracionInicial_"+pos,resumenAtencionesMundo.tieneValoracionInicial(con, idCuenta, false,fechaInicial,fechaFinal,horaInicial,horaFinal));
			}
			else
			{
				forma.setMapaValidacionesInfoCuentas("existeValoracionInicial_"+pos,false);
				forma.setRowSpanSeccionUnoReporteHC(forma.getRowSpanSeccionUnoReporteHC()-1);
			}
			
			if(resumenAtencionesMundo.tieneValoracionInicial(con, idCuenta, false,fechaInicial,fechaFinal,horaInicial,horaFinal)
					|| resumenAtencionesMundo.existenCitas(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal)){
				forma.setRowSpanSeccionUnoReporteHC(forma.getRowSpanSeccionUnoReporteHC()+1);
				forma.setExistenCitasCE(resumenAtencionesMundo.existenCitas(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal));
			}
			
			//resumenAtencionesForm.setExisteValoracionInicial(resumenAtencionesMundo.tieneValoracionInicial(con, idCuenta, false,fechaInicial,fechaFinal,horaInicial,horaFinal));
		
			//hermorhu - MT6038
			//Se cargan los datos de las valoraciones (Inicial y/o Cuidados Especiales) para el ingreso de Hospitalizacion
			forma.setMapaValidacionesInfoCuentas("datosValoracion_"+pos, mundo.obtenerValoraciones(con, campos));
		
		}
		//Urgencias
		if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
		{
			//logger.info("consultare admision urg ");
			//******************************************************************
			int codigoConducta =  UtilidadesHistoriaClinica.obtenerCodigoConductaValoracionUrgenciasCuenta(con,idCuenta+"");
			
			//SE verifica cual fue la conducta a seguir de la valoración de urgencias
			if(codigoConducta==ConstantesBD.codigoConductaSeguirSalaEspera ||
				codigoConducta==ConstantesBD.codigoConductaSeguirInterconsulta||
				codigoConducta==ConstantesBD.codigoConductaSeguirSalaReanimacion)
			{
				//se usa un método que consulte el acronimo del diagnostico principal para saber su hubo un semiEgreso
				if(!forma.getMapaValidacionesInfoCuentas("egreso_"+pos).toString().equals("0"))
					forma.setMapaValidacionesInfoCuentas("egreso_"+pos,resumenAtencionesMundo.busquedaDiagnosticoPrincipal(con, idCuenta));
					//resumenAtencionesForm.setEgreso(resumenAtencionesMundo.busquedaDiagnosticoPrincipal(con, idCuenta));
			}
			if(codigoConducta==ConstantesBD.codigoConductaSeguirHospitalizarPiso||
				codigoConducta==ConstantesBD.codigoConductaSeguirRemitirMayorComplejidad||
				codigoConducta==ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria||
				codigoConducta==ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial||
				codigoConducta==ConstantesBD.codigoConductaSeguirSalidaSinObservacion)
			{
				//se usa el valor -2 para indicar que va a ser un link Egreso para las conductas a seguir
				if(!forma.getMapaValidacionesInfoCuentas("egreso_"+pos).toString().equals("0"))
					forma.setMapaValidacionesInfoCuentas("egreso_"+pos,"-2");
					//resumenAtencionesForm.setEgreso(-2);
			}
			//******************************************************************
			
			if(!String.valueOf(forma.getMapaValidacionesInfoCuentas("egreso_"+pos)).equals("0")){
				if(!String.valueOf(resumenAtencionesMundo.codigoEvolucionGeneroEgreso(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal)).equals("0")
						&& !String.valueOf(resumenAtencionesMundo.codigoEvolucionGeneroEgreso(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal)).equals("-1")
						&& !String.valueOf(resumenAtencionesMundo.codigoEvolucionGeneroEgreso(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal)).equals("-2")){
					//forma.setRowSpanSeccionUnoReporteHC(forma.getRowSpanSeccionUnoReporteHC()+1);
				}

				if(String.valueOf(forma.getMapaValidacionesInfoCuentas("egreso_"+pos)).equals("-1")){
					//forma.setRowSpanSeccionUnoReporteHC(forma.getRowSpanSeccionUnoReporteHC()+1);
				}

				if(String.valueOf(forma.getMapaValidacionesInfoCuentas("egreso_"+pos)).equals("-2")){
					//forma.setRowSpanSeccionUnoReporteHC(forma.getRowSpanSeccionUnoReporteHC()+1);
				}

			}
			forma.setMapaValidacionesInfoCuentas("datosAdmision_"+pos,resumenAtencionesMundo.busquedaDatosAdmisionUrgencias(con, idCuenta));
			//resumenAtencionesForm.setDatosAdmision(resumenAtencionesMundo.busquedaDatosAdmisionUrgencias(con, idCuenta));
			
			forma.setMapaValidacionesInfoCuentas("existeValoracionInicial_"+pos,resumenAtencionesMundo.tieneValoracionInicial(con, idCuenta, true,fechaInicial,fechaFinal,horaInicial,horaFinal));
			
			//logger.info("consultare citas ");
			forma.setMapaValidacionesInfoCuentas("existenCitas_"+pos,resumenAtencionesMundo.existenCitas(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal));
			//resumenAtencionesForm.setExistenCitas(resumenAtencionesMundo.existenCitas(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal));
			
			if(resumenAtencionesMundo.tieneValoracionInicial(con, idCuenta, true,fechaInicial,fechaFinal,horaInicial,horaFinal) || 
					resumenAtencionesMundo.existenCitas(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal)){
				forma.setExistenCitasCE(resumenAtencionesMundo.existenCitas(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal));
					forma.setRowSpanSeccionUnoReporteHC(forma.getRowSpanSeccionUnoReporteHC()+1);
			}
			
			//resumenAtencionesForm.setExisteValoracionInicial(resumenAtencionesMundo.tieneValoracionInicial(con, idCuenta, true,fechaInicial,fechaFinal,horaInicial,horaFinal));
			
		}
		
	
		if(forma.getMapaValidacionesInfoCuentas("existeValoracionInicial_"+pos)==null){
			if(resumenAtencionesMundo.existenCitas(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal)){
				Integer estadoFuncionalidad = UtilidadesHistoriaClinica.consultarEstadoFuncionalidadHistoriaClincia(con);


				if(estadoFuncionalidad.equals(IConstantesReporteHistoriaClinica.estadoParametroTipoHistoriaClinica)){
					forma.setMapaValidacionesInfoCuentas("existeValoracionInicial_"+pos,true);
				}
				forma.setRowSpanSeccionUnoReporteHC(forma.getRowSpanSeccionUnoReporteHC()+1);
				forma.setMapaValidacionesInfoCuentas("existenCitas_"+pos,resumenAtencionesMundo.existenCitas(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal));
				forma.setExistenCitasCE(resumenAtencionesMundo.existenCitas(con, idCuenta,fechaInicial,fechaFinal,horaInicial,horaFinal));
			}
		}
		
		//logger.info("consultare tipo valioracion ");
		forma.setMapaValidacionesInfoCuentas("numeroValoracionInicial_"+pos,resumenAtencionesMundo.numeroValoracionInicial(con, idCuenta));
		//resumenAtencionesForm.setTipoValoracionInicial(resumenAtencionesMundo.tipoValoracionInicial(con, idCuenta));
		
		//Se verifica si hay registro de accidentes de tránsito
		//logger.info("consultare accidente transito ");
		forma.setMapaValidacionesInfoCuentas("accidenteTransito_"+pos,resumenAtencionesMundo.tieneRegistroAccidenteTransito(con,forma.getIdIngreso(),ConstantesIntegridadDominio.acronimoEstadoProcesado,"","","",""));
		//resumenAtencionesForm.setAccidenteTransito(resumenAtencionesMundo.tieneRegistroAccidenteTransito(con,resumenAtencionesForm.getIdIngreso(),ConstantesIntegridadDominio.acronimoEstadoProcesado,"","","",""));

		//Se verifica si hay evento catastrofico
		//logger.info("consultare accidente transito ");
		forma.setMapaValidacionesInfoCuentas("eventoCatastrofico_"+pos,resumenAtencionesMundo.tieneRegistroEventoCatastrofico(con,forma.getIdIngreso(),ConstantesIntegridadDominio.acronimoEstadoFinalizado,"","","",""));
		//resumenAtencionesForm.setEventoCatastrofico(resumenAtencionesMundo.tieneRegistroEventoCatastrofico(con,resumenAtencionesForm.getIdIngreso(),ConstantesIntegridadDominio.acronimoEstadoFinalizado,"","","",""));

		//Se verifica si hay eventos adversos
		//logger.info("consultare eventos adversos ");
		forma.setMapaValidacionesInfoCuentas("eventoAdverso_"+pos,resumenAtencionesMundo.tieneRegistroEventoAdverso(con,forma.getIdIngreso()));
		
		forma.setMapaValidacionesInfoCuentas("existenEscalasXIngreso_"+pos, resumenAtencionesMundo.existenEscalasXIngreso(con, forma.getIdIngreso(), fechaInicial, horaInicial, fechaFinal, horaFinal));
		forma.setExistenEscalasXIngreso(UtilidadTexto.getBoolean(forma.getMapaValidacionesInfoCuentas("existenEscalasXIngreso_"+pos).toString()));
		
		forma.setMapaValidacionesInfoCuentas("existenValoracionesCuidadoEspecial_"+pos, resumenAtencionesMundo.existenValoracionesCuidadoEspecial(con, forma.getIdIngreso(), fechaInicial, horaInicial, fechaFinal, horaFinal));
		
		
		forma.setMapaValidacionesInfoCuentas("existenNotasGeneralesCirugia_"+pos, resumenAtencionesMundo.existeNotasGenerales(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal,paciente.getCodigoPersona()));
		if(resumenAtencionesMundo.existeNotasGenerales(con,idCuenta+"",fechaInicial,fechaFinal,horaInicial,horaFinal,paciente.getCodigoPersona())){
			forma.setRowSpanSeccionDosReporteHC(forma.getRowSpanSeccionDosReporteHC()+1);
		}
		forma.setMapaValidacionesInfoCuentas("existenNotasRecuperacion_"+pos, resumenAtencionesMundo.existenNotasRecuperacion(con,idCuenta+"",paciente.getCodigoPersona()));
		if(resumenAtencionesMundo.existenNotasRecuperacion(con,idCuenta+"",paciente.getCodigoPersona())){
			forma.setRowSpanSeccionDosReporteHC(forma.getRowSpanSeccionDosReporteHC()+1);
		}
		
		
		//ANTECEDENTES FILTRADOS POR FECHA
		//logger.info("consultare antecedentes ");
		cargarAntecedentesToxicos(con,paciente,forma,0);
		cargarAntecedentesVarios(con,paciente,forma,0);
		cargarAntecedentesPediatricos(con,paciente,forma,0);
		cargarAntecedentesOdontologicos(con,paciente,usuario,forma,0);
		//logger.info("terminó ");	
		
		if(forma.isHayAntecedentes()){
			forma.setRowSpanSeccionUnoReporteHC(forma.getRowSpanSeccionUnoReporteHC()+1);
		}
		
		if(forma.getMuestroResumenPHC()>0){
			forma.setRowSpanSeccionCincoReporteHC(forma.getRowSpanSeccionCincoReporteHC()+1);
		}
		if(forma.getMuestroResumenPHCAsocio()>0){
			forma.setRowSpanSeccionDiezReporteHC(forma.getRowSpanSeccionDiezReporteHC()+1);
		}
		
		}catch(Exception e ){
			logger.error("error consultando "+e.getMessage());
		}
	}

	

	/**
	 * Método implementado para cargar los antecedentes odontologicos
	 * @param con
	 * @param paciente
	 * @param usuario 
	 * @param resumenAtencionesForm
	 * @param tipoInformacion 
	 */
	private void cargarAntecedentesOdontologicos(Connection con, PersonaBasica paciente, UsuarioBasico usuario, ResumenAtencionesForm resumenAtencionesForm, int tipoInformacion) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();

		mp.put("fechaInicio", UtilidadFecha.conversionFormatoFechaABD(resumenAtencionesForm.getFechaInicial())); 
		mp.put("fechaFinal", UtilidadFecha.conversionFormatoFechaABD(resumenAtencionesForm.getFechaFinal())); 
		mp.put("horaInicio", resumenAtencionesForm.getHoraInicial()); 
		mp.put("horaFinal", resumenAtencionesForm.getHoraFinal()); 
		
		if(tipoInformacion==0)
		{
			resumenAtencionesForm.getMapaAntOdonto().clear();
			
			mp.put("nroConsulta","24");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp = mundo.consultarInformacion(con, mp);
			
			resumenAtencionesForm.setMapaAntOdonto("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
				resumenAtencionesForm.setExistenAntecedentesOdontologicos(true);
			else
			  
				resumenAtencionesForm.setExistenAntecedentesOdontologicos(false);
			  
		}
		else
		{
			if(resumenAtencionesForm.getExistenAntecedentesOdontologicos())	
			{
				mp.put("nroConsulta","25");
				mp.put("paciente",paciente.getCodigoPersona()+"");
				mp.put("institucion",usuario.getCodigoInstitucionInt()+"");
				resumenAtencionesForm.getMapaAntOdonto().putAll(mundo.consultarInformacion(con, mp));

				HashMap mx = new HashMap();
				mp.put("nroConsulta","26");
				mp.put("paciente",paciente.getCodigoPersona()+"");
				mp.put("institucion",usuario.getCodigoInstitucionInt()+"");
				mx = mundo.consultarInformacion(con, mp);
				resumenAtencionesForm.setMapaAntOdonto("numRegT", mx.get("numRegistros")+"");
				mx.remove("numRegistros");
				resumenAtencionesForm.getMapaAntOdonto().putAll(mx);
				
				mx.clear();
				mp.put("nroConsulta","27");
				mp.put("paciente",paciente.getCodigoPersona()+"");
				mx = mundo.consultarInformacion(con, mp);
				resumenAtencionesForm.setMapaAntOdonto("numRegP", mx.get("numRegistros")+"");
				mx.remove("numRegistros");
				resumenAtencionesForm.getMapaAntOdonto().putAll(mx);
			}
		}
	}

	/**
	 * Método implementado para cargar los antecedentes pediátricos
	 * @param con
	 * @param paciente
	 * @param resumenAtencionesForm
	 * @param tipoInformacion 
	 */
	private void cargarAntecedentesPediatricos(
			Connection con, 
			PersonaBasica paciente, 
			ResumenAtencionesForm resumenAtencionesForm, 
			int tipoInformacion) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		AntecedentePediatrico antecedente	= new AntecedentePediatrico();
		HashMap mp = new HashMap();

		mp.put("fechaInicio", UtilidadFecha.conversionFormatoFechaABD(resumenAtencionesForm.getFechaInicial())); 
		mp.put("fechaFinal", UtilidadFecha.conversionFormatoFechaABD(resumenAtencionesForm.getFechaFinal())); 
		mp.put("horaInicio", resumenAtencionesForm.getHoraInicial()); 
		mp.put("horaFinal", resumenAtencionesForm.getHoraFinal()); 
		
		if(tipoInformacion==0)
		{
			resumenAtencionesForm.getMapaAntPediatricos().clear();
			
			mp.put("nroConsulta","23");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp = mundo.consultarInformacion(con, mp);
			
			resumenAtencionesForm.setMapaAntPediatricos("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
				resumenAtencionesForm.setExistenAntecedentesPediatricos(true);
			else
				resumenAtencionesForm.setExistenAntecedentesPediatricos(false);
		}
		else
		{
			if(resumenAtencionesForm.isExistenAntecedentesPediatricos())	
			{
				//--Si el paciente tiene ya antecedente:
				//--boolean estaAntecedentePaciente = antecedente.cargar2( con, paciente.getCodigoPersona() );
				mp.put("paciente", paciente.getCodigoPersona() + "");
				boolean estaAntecedentePaciente = false;
				try 
				{
					estaAntecedentePaciente = antecedente.cargarResumenAtencion( con, mp );
				} 
				catch (SQLException e) 
				{
					logger.error("Error al cargarResumenAtencion en cargarAntecedentesPediatricos de ResumenAtencionAction: "+e);
				}
				
				if(estaAntecedentePaciente)
				{
					try 
					{
						antecedente.setInfoMadre(new InfoMadre() );
						antecedente.getInfoMadre().cargar(con, paciente.getCodigoPersona() );
						antecedente.setInfoPadre(new InfoPadre() );
						antecedente.getInfoPadre().cargar(con, paciente.getCodigoPersona() );
						antecedente.setPaciente(paciente);

						cargarAntPediatricos(antecedente, resumenAtencionesForm);

						resumenAtencionesForm.setAntPed(antecedente);
					} 
					catch (SQLException e) 
					{
						logger.error("Error al cargarResumenAtencion(2) en cargarAntecedentesPediatricos de ResumenAtencionAction: "+e);
					}
					
				}
			}
		}
		
	}
	
	/**
	 * Metodo para cargar los mapas de antecedentes Pediatricos 
	 *
	 */
	void cargarAntPediatricos(AntecedentePediatrico antecedente, ResumenAtencionesForm forma)
	{
		int size = 0;
		if(  antecedente.getTiposParto() != null )
		{
		 size = antecedente.getTiposParto().size();
		}

		for(int i = 0;  i < size; i++)
		{
			InfoDatos tipoParto	= (InfoDatosBD) antecedente.getTiposParto().get(i);
			String codigoParto	= tipoParto.getCodigo()+"";

			forma.getTiposParto().put(codigoParto + "", codigoParto + "-" + tipoParto.getValue() );
			forma.getMotivosTiposParto().put(codigoParto + "", tipoParto.getDescripcion() );
			
			forma.getTiposPartoList().add(tipoParto);
		}
		
		if( antecedente.getOtroTipoParto() != null )
		{
			//forma.get.otroTipoParto = otroTipoParto;

			/* Si tiene escrito un motivo de este tipo de parto se debe incluir en el Map */
			forma.getTiposParto().put("0", "0-Otro");

			/* Se debe meter su comentario en un cuadernito para que vea lo anterior que sabemos que no es modificable */
			forma.getMotivosTiposParto().put("0", antecedente.getMotivoTipoPartoOtro() );
			
			InfoDatos tipoParto	= new InfoDatosBD(0, "Otro", antecedente.getMotivoTipoPartoOtro());
			forma.getTiposPartoList().add(tipoParto);
		}
		
		
		forma.getTiposPartoCarga().putAll(forma.getTiposParto());
		forma.getMotivosTiposPartoCarga().putAll(forma.getMotivosTiposParto());
	}

	/**
	 * Método implementado para cargar información de la hoja neurológica del registro de enfermería
	 * @param con
	 * @param mundoEnfer
	 * @param resumenAtencionesForm
	 * @param institucion
	 * @param centroCosto
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param asocio
	 */
	private void accionCargarSeccionHojaNeurologica(Connection con, RegistroEnfermeria mundoEnfer, ResumenAtencionesForm resumenAtencionesForm, int institucion, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal) {
	 	//-----Se consulta el histórico de pupilas de acuerdo a los parámetros de búsqueda de la impresion hc-----//
	 	resumenAtencionesForm.setPupilaDerechaList(mundoEnfer.consultarPupilasHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, "", 'D'));
	 	resumenAtencionesForm.setPupilaIzquierdaList(mundoEnfer.consultarPupilasHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, "", 'I'));
	 	
	 	//----- Se consulta el histórico de convulsiones de acuerdo a los parámetros de búsqueda de la impresion hc-----//
	 	resumenAtencionesForm.setConvulsiones(mundoEnfer.consultarConvulsionesHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, ""));
	 	
	 	//----- Se consulta el histórico de control de esfinteres de acuerdo a los parámetros de búsqueda de la impresion hc-----//
	 	resumenAtencionesForm.setControlEsfinteresList(mundoEnfer.consultarControlEsfinteresHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, ""));
	 	
	 	//----- Se consulta el histórico de fuerza muscular de acuerdo a los parámetros de búsqueda de la impresion hc-----//
	 	resumenAtencionesForm.setFuerzaMuscularList(mundoEnfer.consultarFuerzaMuscularHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, ""));
	 	
	 	if(resumenAtencionesForm.getPupilaDerechaList().size() > 0 || resumenAtencionesForm.getPupilaIzquierdaList().size() > 0 
	 			|| resumenAtencionesForm.getConvulsiones().size() > 0 
	 			|| resumenAtencionesForm.getControlEsfinteresList().size() > 0 
	 			|| resumenAtencionesForm.getFuerzaMuscularList().size() > 0) {
	 		resumenAtencionesForm.setExisteHojaNeurologica(true);
	 	} else {
	 		resumenAtencionesForm.setExisteHojaNeurologica(false);
	 	}
	}

	/**
	 * Método implementado para cargar los cuidados especiales del registro de enfermería
	 * @param con
	 * @param mundoEnfer
	 * @param resumenAtencionesForm
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param asocio 
	 */
	private void accionCargarSeccionCuidadosEspeciales(Connection con, RegistroEnfermeria mundoEnfer, ResumenAtencionesForm resumenAtencionesForm, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal) 
	{
		HashMap mapaCuidadosEspecialesHisto=new HashMap();
		mapaCuidadosEspecialesHisto.put("numRegistros","0");
		//----------Se consultan los posibles históricos de cuidados especiales (presenta=true or (presenta=false and descripcion!='')------------------------//
		mapaCuidadosEspecialesHisto=mundoEnfer.consultarCuidadosEspecialesHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, "");
		
		String cuentasTmp[]=cuentas.split(",");
		
		HashMap mapaCuidadosEspecialesHistoDetalle=new HashMap();
		mapaCuidadosEspecialesHistoDetalle.put("numRegistros","0");
		if(Integer.parseInt(mapaCuidadosEspecialesHisto.get("numRegistros")+"")>0)
			mapaCuidadosEspecialesHistoDetalle=mundoEnfer.consultarCuidadosEspecialesDetalleHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, "");
		
		//----------Se crea el mapa histórico de cuidados especiales de enfermería para la impresión HC ---------//
	
		
		if(cuentasTmp.length>1){
			resumenAtencionesForm.setMapaHistoricoCuidadosEspecialesAsocio(this.formarHistoricoCuidadosEspeciales(mapaCuidadosEspecialesHisto, mapaCuidadosEspecialesHistoDetalle));
			resumenAtencionesForm.setCuentaAsociada(true);
		}
		else{
			resumenAtencionesForm.setMapaHistoricoCuidadosEspeciales(this.formarHistoricoCuidadosEspeciales(mapaCuidadosEspecialesHisto, mapaCuidadosEspecialesHistoDetalle));
			resumenAtencionesForm.setCuentaAsociada(false);
		}
		
	}

	/**
	 * Método auxiliar para organizar la informacion de cuidados especiales del registro de enfermería
	 * @param mapaCuidadosEspecialesHisto
	 * @param mapaCuidadosEspecialesHistoDetalle
	 * @return
	 */
	private HashMap formarHistoricoCuidadosEspeciales(HashMap mapaCuidadosEspecialesHisto, HashMap mapaCuidadosEspecialesHistoDetalle) 
	{
		HashMap mapaCuidadosEspeciales=new HashMap();
		
		//---------Se asigna el mapa -------------//
		mapaCuidadosEspeciales=mapaCuidadosEspecialesHisto;
		
		Utilidades.imprimirMapa(mapaCuidadosEspecialesHistoDetalle);
		
		if(mapaCuidadosEspeciales!=null)
		{
			int numRegistros=Integer.parseInt(mapaCuidadosEspeciales.get("numRegistros")+"");
			int numRegDetalle=Integer.parseInt(mapaCuidadosEspecialesHistoDetalle.get("numRegistros")+"");
			int contReg=0;
			String codigoEnca="",esMedico="";
			boolean entro=false;
			
			for(int i=0; i<numRegistros; i++)
			{
				contReg=0;
				entro=false;
				codigoEnca=mapaCuidadosEspeciales.get("codigo_enca_"+i)+"";
				esMedico=mapaCuidadosEspeciales.get("es_medico_"+i)+"";
				for(int j=0; j<numRegDetalle; j++)
				{
					if(codigoEnca.equals(mapaCuidadosEspecialesHistoDetalle.get("codigo_enca_"+j)+"") && esMedico.equals(mapaCuidadosEspecialesHistoDetalle.get("es_medico_"+j)+""))	
						{
						//------Se asigna el nombre del cuidado de enfermería ----------//
						mapaCuidadosEspeciales.put("nombre_cuidado_"+codigoEnca+"_"+contReg, mapaCuidadosEspecialesHistoDetalle.get("nombre_cuidado_"+j));
						
						//------Se asigna si presenta o no observaciones ----------//
						if(UtilidadTexto.getBoolean(mapaCuidadosEspecialesHistoDetalle.get("presenta_"+j)+""))
						{
							mapaCuidadosEspeciales.put("presenta_"+codigoEnca+"_"+contReg, "[SI]");
						}
						else
						{
							mapaCuidadosEspeciales.put("presenta_"+codigoEnca+"_"+contReg, "[NO]");
						}
						
						mapaCuidadosEspeciales.put("controlespecial_"+codigoEnca+"_"+contReg, mapaCuidadosEspecialesHistoDetalle.get("controlespecial_"+j));
						
						//-----------Se asignan las observaciones ----------------------//
						mapaCuidadosEspeciales.put("observaciones_"+codigoEnca+"_"+contReg, mapaCuidadosEspecialesHistoDetalle.get("observaciones_"+j));
						entro=true;
						contReg++;
						}
					else
					{
						if(entro)
						{
							mapaCuidadosEspeciales.put("numRegEnca_"+codigoEnca, contReg);
							entro=false;
							break;
						}
					}
					if(j==numRegDetalle-1)
					 {
					  mapaCuidadosEspeciales.put("numRegEnca_"+codigoEnca, contReg);
					 }
				}//for numRegDetalle
				
			}//for numRegistros
		}//if mapaCuidadosEspeciales!=null
		
		return mapaCuidadosEspeciales;
	}

	/**
	 * Método implementado para cargar los antecedentes varios
	 * @param con
	 * @param paciente
	 * @param resumenAtencionesForm
	 * @param tipoInformacion 
	 */
	private void cargarAntecedentesVarios(Connection con, PersonaBasica paciente, ResumenAtencionesForm resumenAtencionesForm, int tipoInformacion) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();

		/*mp.put("fechaInicio", UtilidadFecha.conversionFormatoFechaABD(resumenAtencionesForm.getFechaInicial())); 
		mp.put("fechaFinal", UtilidadFecha.conversionFormatoFechaABD(resumenAtencionesForm.getFechaFinal())); 
		mp.put("horaInicio", resumenAtencionesForm.getHoraInicial()); 
		mp.put("horaFinal", resumenAtencionesForm.getHoraFinal());*/
		
		//hermorhu - MT6866
		//caso en que la via ingreso del paciente sea Consulta Externa los antecendentes los debe traer hasta la fecha y hora de la ultima valoracion
		if(resumenAtencionesForm.getViaIngreso() == ConstantesBD.codigoViaIngresoConsultaExterna) {
			
			InfoIngresoDto ultimaValoracion = mundo.obtenerDatosUltimaValoracionConsultaExternaXCuenta(con, resumenAtencionesForm.getCuenta());
			
			if(ultimaValoracion != null && resumenAtencionesForm.getIngresoSelecccionado().getFechaEgreso() != null) {
				
				String fechaUltimaValoracion = UtilidadFecha.conversionFormatoFechaAAp(ultimaValoracion.getFechaEgreso());
				String fechaEgreso = UtilidadFecha.conversionFormatoFechaAAp(resumenAtencionesForm.getIngresoSelecccionado().getFechaEgreso());
				
				if(UtilidadFecha.compararFechas(fechaUltimaValoracion, ultimaValoracion.getHoraEgreso(), fechaEgreso, resumenAtencionesForm.getIngresoSelecccionado().getHoraEgreso()).isTrue()){		
					mp.put(SqlBaseImpresionResumenAtencionesDao.KEY_FECHA_EGRESO, ultimaValoracion);
				}else {
					mp.put(SqlBaseImpresionResumenAtencionesDao.KEY_FECHA_EGRESO, resumenAtencionesForm.getIngresoSelecccionado());
				}	
			}else {
				mp.put(SqlBaseImpresionResumenAtencionesDao.KEY_FECHA_EGRESO, resumenAtencionesForm.getIngresoSelecccionado());
			}
			
		}else {
			mp.put(SqlBaseImpresionResumenAtencionesDao.KEY_FECHA_EGRESO, resumenAtencionesForm.getIngresoSelecccionado());
		}
		
		if(tipoInformacion==0)
		{
			if(resumenAtencionesForm.getMapaAntOtros() != null){
				resumenAtencionesForm.getMapaAntOtros().clear();
			}			
			mp.put("nroConsulta","21");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp = mundo.consultarInformacion(con, mp);
			
			resumenAtencionesForm.setMapaAntOtros("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
				resumenAtencionesForm.setExistenAntecedentesVarios(true);
			else
				resumenAtencionesForm.setExistenAntecedentesVarios(false);
		}
		else
		{
			if(resumenAtencionesForm.isExistenAntecedentesVarios())
			{
				mp.put("nroConsulta","22");
				mp.put("paciente",paciente.getCodigoPersona()+"");
				resumenAtencionesForm.setMapaAntOtros(mundo.consultarInformacion(con, mp));
			}
		}
	}

	/**
	 * Método implementado para cargar el control de liquidos
	 * @param con
	 * @param resumenAtencionesForm
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param idCuenta
	 * @param area
	 * @param institucion
	 * @param mundoEnfer
	 * @param asocio
	 * @param tipoInformacion (Si es 0 significa que es solo para verificar si existe)
	 */
	private void cargarControlLiquidos(
			Connection con, 
			ResumenAtencionesForm resumenAtencionesForm, 
			String fechaInicial, 
			String fechaFinal, 
			String horaInicial, 
			String horaFinal, 
			int idCuenta, 
			String area,
			String institucion,
			RegistroEnfermeria mundoEnfer, 
			boolean asocio, 
			int tipoInformacion,
			int pos 
			) 
	{
		HashMap parametros = new HashMap();
		parametros.put("fechaInicio", UtilidadFecha.conversionFormatoFechaABD(fechaInicial)); 
		parametros.put("fechaFinal", UtilidadFecha.conversionFormatoFechaABD(fechaFinal)); 
		parametros.put("horaInicio", horaInicial); 
		parametros.put("horaFinal", horaFinal); 
		parametros.put("cuentas",idCuenta+"");
		parametros.put("centroCosto", area);
		parametros.put("institucion",institucion);

		HashMap mapaControlLiq = new HashMap();
		
		if(tipoInformacion==1)
		{
		
			HashMap mp = new HashMap();
			parametros.put("nroConsulta","6");
			mapaControlLiq = mundoEnfer.consultarControlLiquidos(con, parametros) ;  
			parametros.put("nroConsulta","2");
			mp = mundoEnfer.consultarControlLiquidos(con, parametros);
			mapaControlLiq.put("nroRegMedAdm", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			mapaControlLiq.putAll(mp);
			mp.clear();
			parametros.put("nroConsulta","3");
			mp = mundoEnfer.consultarControlLiquidos(con, parametros);			
			mapaControlLiq.put("nroRegMedElim", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			mapaControlLiq.putAll(mp);  
			mp.clear();
			parametros.put("nroConsulta","4");
			mp = mundoEnfer.consultarControlLiquidos(con, parametros);
			mapaControlLiq.put("nroRegBalLiqAdm", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			mapaControlLiq.putAll(mp);	
			mp.clear();
			parametros.put("nroConsulta","5");
			mp = mundoEnfer.consultarControlLiquidos(con, parametros);
			mapaControlLiq.put("nroRegBalLiqElim", mp.get("numRegistros")+"");
			mapaControlLiq.put("fechaHistoricoDieta","1");
			mapaControlLiq.put("paginadorLiqAdmin","0");
			mapaControlLiq.put("paginadorLiqElim","0");
		}
		else if(tipoInformacion==0)
		{
			//-- Determinar la natruraleza de la consulta.
			parametros.put("nroConsulta","1");

			//-- Enviar a Consultar.
			mapaControlLiq = mundoEnfer.consultarControlLiquidos(con, parametros);
			
			//-- Para Quitar 
			mapaControlLiq.put("fechaHistoricoDieta","0");
			mapaControlLiq.put("paginadorLiqAdmin","0");
			mapaControlLiq.put("paginadorLiqElim","0");
			
		}
		
		if(asocio)
		{
			resumenAtencionesForm.setMapaControlLiqAsocio(mapaControlLiq);
			if(UtilidadCadena.vIntIgual(mapaControlLiq.get("cantidad_0")+"")){
				resumenAtencionesForm.setMapaValidacionesInfoCuentas("existeControlLiquidosAsocio_"+pos,false);
				//resumenAtencionesForm.setExisteControlLiquidosAsocio(false);
			}
			else{
				resumenAtencionesForm.setMapaValidacionesInfoCuentas("existeControlLiquidosAsocio_"+pos,true);
				//resumenAtencionesForm.setRowSpanSeccionSieteReporteHC(resumenAtencionesForm.getRowSpanSeccionSieteReporteHC()+1);
				//resumenAtencionesForm.setExisteNotasEnfermeriaAsocio(resumenAtencionesMundo.existeAnotacionesEnfermeria(con,idCuentaAsocio+"",fechaInicial,fechaFinal,horaInicial,horaFinal));
				/**
				* Tipo Modificacion: Segun incidencia 6480
				* Autor: Alejandro Aguirre Luna
				* usuario: aleagulu
				* Fecha: 19/02/2013
				* Descripcion: Se valida el número de seccionies que tiene el paciente, 
				* estas pueden ser 2 ó 3. Si tiene 2 puede tener la sección Hospitalización - 
				* Hospitalizado o y alguna otra asociación. En caso de ser tres es la misma
				* Hospitalización Hospitalizado con otras dos secciones. Si solo tiene dos
				* secciones se debe validar por la via de ingreso para establecer si la
				* sección que esta apareciendo es de Urgencias u Hospitalización. 
				**/
				if(numRegistrosEncontrados == 2){
					resumenAtencionesForm.contadorFilasSiete_nuevo(resumenAtencionesForm.getViaIngreso());
						if(resumenAtencionesForm.getViaIngreso() == 1){
							resumenAtencionesForm.getRowSpanSeccionSieteReporteHC_lista().set(pos,resumenAtencionesForm.getContadorSieteUrgencias().intValue());
						}//else Hospitalizacion = 3
						else if(resumenAtencionesForm.getViaIngreso() == 3){
							resumenAtencionesForm.getRowSpanSeccionSieteReporteHC_lista().set(pos,resumenAtencionesForm.getContadorSieteHospitalizacion().intValue());
						}
					}
					else if(numRegistrosEncontrados == 3){
						resumenAtencionesForm.contadorFilasSiete(pos);
						if(pos == 2){
							resumenAtencionesForm.getRowSpanSeccionSieteReporteHC_lista().set(pos,resumenAtencionesForm.getContadorSieteUrgencias().intValue());
						}
						else if(pos == 1){
							resumenAtencionesForm.getRowSpanSeccionSieteReporteHC_lista().set(pos,resumenAtencionesForm.getContadorSieteHospitalizacion().intValue());
							
						}
					}
			}
				//resumenAtencionesForm.setExisteControlLiquidosAsocio(true);
		}
		else
		{
			resumenAtencionesForm.setMapaControlLiq(mapaControlLiq);
			if(UtilidadCadena.vIntIgual(mapaControlLiq.get("cantidad_0")+"")){
				resumenAtencionesForm.setMapaValidacionesInfoCuentas("existeControlLiquidos_"+pos,false);
				//resumenAtencionesForm.setExisteControlLiquidos(false);
			}
			else{
				resumenAtencionesForm.setMapaValidacionesInfoCuentas("existeControlLiquidos_"+pos,true);
				resumenAtencionesForm.setRowSpanSeccionDosReporteHC(resumenAtencionesForm.getRowSpanSeccionDosReporteHC()+1);
			}
			//resumenAtencionesForm.setExisteControlLiquidos(true);
		}		
	}

	/**
	 * Método que ordena el listado de Cateteres y Sondas (REGISTRO ENFERMERIA)
	 * @param resumenForm
	 * @param mapa
	 * @param asocio
	 */
	private void ordenarHistoricoCateterSonda(ResumenAtencionesForm resumenForm, HashMap mapa, boolean asocio) 
	{
		int indicesFijos=15;
		Collection colCateteresSondaInstitucionCcosto;
		Collection cateterSondaHistoTodos;
		
		if(asocio)
		{
			colCateteresSondaInstitucionCcosto = resumenForm.getColCateteresSondaInstitucionCcostoAsocio();
			cateterSondaHistoTodos = resumenForm.getCateterSondaHistoTodosAsocio();
		}
		else
		{
			colCateteresSondaInstitucionCcosto = resumenForm.getColCateteresSondaInstitucionCcosto();
			cateterSondaHistoTodos = resumenForm.getCateterSondaHistoTodos();
		}
		
		
		int numIndices=colCateteresSondaInstitucionCcosto.size()+indicesFijos;
		
		//------- Se construye el vector de strings con los indices del mapa para poder ordenarlo ----//
		String[] indices=new String[numIndices];
		
		indices[0]="cateterSondaRegEnfer_";
		indices[1]="nombreArticulo_";
		indices[2]="codigoArticuloCcIns_";
		indices[3]="viaInsercion_";
		indices[4]="fechaInsercion_";
		indices[5]="horaInsercion_";
		indices[6]="fechaRetiro_";
		indices[7]="horaRetiro_";
		indices[8]="curaciones_";
		indices[9]="observaciones_";
		indices[10]="fecha_registro_";
		indices[11]="hora_registro_";
		indices[12]="nombre_usuario_";
		
		if(colCateteresSondaInstitucionCcosto.size() > 0)
	       {
		       Iterator iterador1=colCateteresSondaInstitucionCcosto.iterator();
		       for (int numFila=0; numFila<colCateteresSondaInstitucionCcosto.size(); numFila++)
		       {
		    	   HashMap fila1=(HashMap)iterador1.next();
			       	indices[indicesFijos]="valorParam_"+fila1.get("codigo_tipo")+"_";
			       	indicesFijos++;
		       }
	       }
		
		//-----Numero de registros del mapa------------//
		int num = cateterSondaHistoTodos.size();
		
		if(asocio)
		{
			resumenForm.setMapaHistoricoCateterSondaAsocio(Listado.ordenarMapa(indices,"fechaInsercion_","",mapa,num));
			resumenForm.setMapaHistoricoCateterSondaAsocio("numRegistros", new Integer(num));
		}
		else
		{
			resumenForm.setMapaHistoricoCateterSonda(Listado.ordenarMapa(indices,"fechaInsercion_","",mapa,num));
			resumenForm.setMapaHistoricoCateterSonda("numRegistros", new Integer(num));
		}
		
        
        
        
       
		
	}

	/**
	 * Método que carga el historico de Cateteres y Sonda (REGISTRO ENFERMERIA)
	 * @param resumenForm
	 * @param asociada
	 * @return
	 */
	private HashMap formarHistoricoCateterSonda(ResumenAtencionesForm resumenForm, boolean asociada) 
	{
		//-------------- Se guarda en una matriz los cateteres sonda fijos -----------------------------//
		Vector[] matrizCateteresFijos;
		int numCateterSondaFijosHisto = 0;
		Collection cateterSondaFijosHisto;
		Collection cateterSondaParamHisto;
		Collection cateterSondaHistoTodos;
		Collection colCateteresSondaInstitucionCcosto;
		
		if(asociada)
		{
			matrizCateteresFijos = new Vector[resumenForm.getCateterSondaFijosHistoAsocio().size()];
			numCateterSondaFijosHisto = resumenForm.getCateterSondaFijosHistoAsocio().size();
			cateterSondaFijosHisto = resumenForm.getCateterSondaFijosHistoAsocio();
			cateterSondaParamHisto = resumenForm.getCateterSondaParamHistoAsocio();
			cateterSondaHistoTodos = resumenForm.getCateterSondaHistoTodosAsocio();
			colCateteresSondaInstitucionCcosto = resumenForm.getColCateteresSondaInstitucionCcostoAsocio();
		}
		else
		{
			matrizCateteresFijos = new Vector[resumenForm.getCateterSondaFijosHisto().size()];
			numCateterSondaFijosHisto = resumenForm.getCateterSondaFijosHisto().size();
			cateterSondaFijosHisto = resumenForm.getCateterSondaFijosHisto();
			cateterSondaParamHisto = resumenForm.getCateterSondaParamHisto();
			cateterSondaHistoTodos = resumenForm.getCateterSondaHistoTodos();
			colCateteresSondaInstitucionCcosto = resumenForm.getColCateteresSondaInstitucionCcosto();
		}
		
		if( numCateterSondaFijosHisto > 0)
	       {
		       Iterator iterador1 = cateterSondaFijosHisto.iterator();
		       for (int numFila=0; numFila<cateterSondaFijosHisto.size(); numFila++)
		       {
		    	   HashMap fila1=(HashMap)iterador1.next();
			       	matrizCateteresFijos[numFila]=new Vector();
			       	matrizCateteresFijos[numFila].add(fila1.get("cateter_sonda_reg_enfer"));
			       	matrizCateteresFijos[numFila].add(fila1.get("via_insercion"));
			       	
			       	if (UtilidadCadena.noEsVacio(fila1.get("fecha_insercion")+""))
			       		matrizCateteresFijos[numFila].add(fila1.get("fecha_insercion"));
			       	else
			       		matrizCateteresFijos[numFila].add("");
			       	
			    	if (UtilidadCadena.noEsVacio(fila1.get("hora_insercion")+""))
			       		matrizCateteresFijos[numFila].add(fila1.get("hora_insercion"));
			       	else
			       		matrizCateteresFijos[numFila].add("");
			       	
			       	if (UtilidadCadena.noEsVacio(fila1.get("fecha_retiro")+""))
			       		matrizCateteresFijos[numFila].add(fila1.get("fecha_retiro"));
			       	else
			       		matrizCateteresFijos[numFila].add("");
			       	
			       	if (UtilidadCadena.noEsVacio(fila1.get("hora_retiro")+""))
			       		matrizCateteresFijos[numFila].add(fila1.get("hora_retiro"));
			       	else
			       		matrizCateteresFijos[numFila].add("");
			       	
			       	matrizCateteresFijos[numFila].add(fila1.get("curaciones"));
			       	matrizCateteresFijos[numFila].add(fila1.get("observaciones"));
			       	
			       	matrizCateteresFijos[numFila].add(fila1.get("fecha_registro"));
			       	matrizCateteresFijos[numFila].add(fila1.get("hora_registro"));
			       	//matrizCateteresFijos[numFila].add(fila1.get("nombre_usuario"));
		       }
	       }
	
		//-------------- Se guarda en una matriz los cateteres sonda parametrizados -----------------------------//
		Vector[] matrizCateteresParam = new Vector[cateterSondaParamHisto.size()];
		if(cateterSondaParamHisto.size() > 0)
	       {
		       Iterator iterador2=cateterSondaParamHisto.iterator();
		       for (int numFila2=0; numFila2<cateterSondaParamHisto.size(); numFila2++)
		       {
		    	   HashMap fila2=(HashMap)iterador2.next();
			       	matrizCateteresParam[numFila2]=new Vector();
			       	matrizCateteresParam[numFila2].add(fila2.get("cateter_sonda_reg_enfer"));
			       	matrizCateteresParam[numFila2].add(fila2.get("codigo_tipo"));
			       	matrizCateteresParam[numFila2].add(fila2.get("col_cateter_sonda_cc_ins"));
			       	matrizCateteresParam[numFila2].add(fila2.get("valor"));
		       }
	       }
		
		//--------------- Se recorre el hisorico de cateteres fijos y parametrizados y se va guardando en el hashMap
		HashMap nuevoMapaCateterHisto=new HashMap();
		if(cateterSondaHistoTodos.size() > 0)
	       {
		       Iterator iterador3=cateterSondaHistoTodos.iterator();
		       
		       for (int numFila3=0; numFila3<cateterSondaHistoTodos.size(); numFila3++)
		       {
		    	   HashMap fila3=(HashMap)iterador3.next();
			    	
			    	nuevoMapaCateterHisto.put("cateterSondaRegEnfer_"+numFila3, fila3.get("cateter_sonda_reg_enfer"));
			    	nuevoMapaCateterHisto.put("nombreArticulo_"+numFila3, fila3.get("nombre_articulo"));
			    	nuevoMapaCateterHisto.put("codigoArticuloCcIns_"+numFila3, fila3.get("codigo_articulo_cc_ins"));
			    	nuevoMapaCateterHisto.put("nombre_usuario_"+numFila3, fila3.get("nombre_usuario"));
			    	for(int fil1=0; fil1<matrizCateteresFijos.length; fil1++)
						{
						if(matrizCateteresFijos[fil1].elementAt(0).equals(fila3.get("cateter_sonda_reg_enfer")))
							{
								nuevoMapaCateterHisto.put("viaInsercion_"+numFila3, matrizCateteresFijos[fil1].elementAt(1)+"");
								nuevoMapaCateterHisto.put("fechaInsercion_"+numFila3, matrizCateteresFijos[fil1].elementAt(2)+"");
								nuevoMapaCateterHisto.put("horaInsercion_"+numFila3,matrizCateteresFijos[fil1].elementAt(3)+"");
								nuevoMapaCateterHisto.put("fechaRetiro_"+numFila3,matrizCateteresFijos[fil1].elementAt(4)+"");
								nuevoMapaCateterHisto.put("horaRetiro_"+numFila3,matrizCateteresFijos[fil1].elementAt(5)+"");
								nuevoMapaCateterHisto.put("curaciones_"+numFila3,matrizCateteresFijos[fil1].elementAt(6)+"");
								nuevoMapaCateterHisto.put("observaciones_"+numFila3,matrizCateteresFijos[fil1].elementAt(7)+"");
								nuevoMapaCateterHisto.put("fecha_registro_"+numFila3,matrizCateteresFijos[fil1].elementAt(8)+"");
								nuevoMapaCateterHisto.put("hora_registro_"+numFila3,matrizCateteresFijos[fil1].elementAt(9)+"");
								//nuevoMapaCateterHisto.put("nombre_usuario_"+numFila3,matrizCateteresFijos[fil1].elementAt(12)+"");
								break;
							}
						//--------Si lleg? hasta aqu? entonces no tiene valor
						if(fil1==matrizCateteresFijos.length-1)
							{
							nuevoMapaCateterHisto.put("viaInsercion_"+numFila3, "");
							nuevoMapaCateterHisto.put("fechaInsercion_"+numFila3, "");
							nuevoMapaCateterHisto.put("horaInsercion_"+numFila3, "");
							nuevoMapaCateterHisto.put("fechaRetiro_"+numFila3,"");
							nuevoMapaCateterHisto.put("horaRetiro_"+numFila3,"");
							nuevoMapaCateterHisto.put("curaciones_"+numFila3,"");
							nuevoMapaCateterHisto.put("observaciones_"+numFila3,"");
							nuevoMapaCateterHisto.put("fecha_registro_"+numFila3,"");
							nuevoMapaCateterHisto.put("hora_registro_"+numFila3,"");
							nuevoMapaCateterHisto.put("nombre_usuario_"+numFila3,"");
							}
						}//for
			    	
			    	//----------- Se agregan los valores hist?ricos de los cateteres sonda paremetrizados -----------------// 
			    	if(colCateteresSondaInstitucionCcosto.size() > 0)
				       {
					       Iterator iterador4=colCateteresSondaInstitucionCcosto.iterator();
					       
					       for (int numFila4=0; numFila4<colCateteresSondaInstitucionCcosto.size(); numFila4++)
					       {
					    	   HashMap fila4=(HashMap)iterador4.next();
						       	for(int fil2=0; fil2<matrizCateteresParam.length; fil2++)
									{
										if(matrizCateteresParam[fil2].elementAt(0).equals(fila3.get("cateter_sonda_reg_enfer")) && matrizCateteresParam[fil2].elementAt(1).equals(fila4.get("codigo_tipo")))
											{
												nuevoMapaCateterHisto.put("valorParam_"+fila4.get("codigo_tipo")+"_"+numFila3, matrizCateteresParam[fil2].elementAt(3)+"");
												break;
											}
										//--------Si lleg? hasta aqu? entonces no tiene valor
										if(fil2==matrizCateteresParam.length-1)
											{
												nuevoMapaCateterHisto.put("valorParam_"+fila4.get("codigo_tipo")+"_"+numFila3, "");
											}
									}//for
					       }//for
				       } //if  
		       }//for numFila3
		       
		       nuevoMapaCateterHisto.put("numRegistros", new Integer(cateterSondaHistoTodos.size()));
	       }  //if 
		else
		{
			nuevoMapaCateterHisto.put("numRegistros", new Integer(0));
		}
		
		return nuevoMapaCateterHisto;
	}

	/**
	 * Método implementado para ordenar el listado de consultas PYP
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarConsultasPYP(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping) 
	{
		String[] indices={
				"numero_solicitud_",
				"fecha_solicitud_",
				"tipo_",
				"especialidad_",
				"centro_costo_solicitado_",
				"estado_medico_",
				"orden_",
				"servicio_"
			};
		
		
		if(resumenAtencionesForm.isCuentaAsociada())
		{
			resumenAtencionesForm.setConsultasPYPAsocio(Listado.ordenarMapa(indices,
					resumenAtencionesForm.getIndiceConsultasPYP(),
					resumenAtencionesForm.getUltimoIndiceConsultasPYP(),
					resumenAtencionesForm.getConsultasPYPAsocio(),
					resumenAtencionesForm.getNumConsultasPYPAsocio()));
			
			resumenAtencionesForm.setConsultasPYPAsocio("numRegistros",resumenAtencionesForm.getNumConsultasPYPAsocio()+"");
		}
		else
		{
			resumenAtencionesForm.setConsultasPYP(Listado.ordenarMapa(indices,
					resumenAtencionesForm.getIndiceConsultasPYP(),
					resumenAtencionesForm.getUltimoIndiceConsultasPYP(),
					resumenAtencionesForm.getConsultasPYP(),
					resumenAtencionesForm.getNumConsultasPYP()));
			
			resumenAtencionesForm.setConsultasPYP("numRegistros",resumenAtencionesForm.getNumConsultasPYP()+"");
		}
		resumenAtencionesForm.setUltimoIndiceConsultasPYP(resumenAtencionesForm.getIndiceConsultasPYP());
		
		resumenAtencionesForm.setEstado("consultasPYP");
		this.cerrarConexion(con);
		return mapping.findForward("listadoConsultasPYP");
	}

	/**
	 * Método implementado para listar las consultas PYP existentes de un cuenta
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionConsultasPYP(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		if(resumenAtencionesForm.isCuentaAsociada())
		{
			resumenAtencionesForm.setConsultasPYPAsocio((HashMap)resumenAtencionesForm.getMapaValidacionesInfoCuentas("consultasPYPAsocio_"+resumenAtencionesForm.getIndiceMapaValInfoCuentas()));
			resumenAtencionesForm.setNumConsultasPYPAsocio(Utilidades.convertirAEntero(resumenAtencionesForm.getConsultasPYPAsocio("numRegistros")+""));
		}
		else
		{
			resumenAtencionesForm.setConsultasPYP((HashMap)resumenAtencionesForm.getMapaValidacionesInfoCuentas("consultasPYP_"+resumenAtencionesForm.getIndiceMapaValInfoCuentas()));
			resumenAtencionesForm.setNumConsultasPYP(Utilidades.convertirAEntero(resumenAtencionesForm.getConsultasPYP("numRegistros")+""));
		}
		
		resumenAtencionesForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		this.cerrarConexion(con);
		return mapping.findForward("listadoConsultasPYP");
	}

	/**
	 * Método que ordena el listado de registros
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarCx(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping) 
	{
		//indices
    	String[] indices={   
    			"numero_solicitud_",
    			"orden_",
                "fecha_orden_",
                "fecha_cirugia_",
                "medico_solicita_",
                "estado_medico_",
				"detalle_"
           };
    	
    	int numeroElementos=resumenAtencionesForm.getNumeroSolicitudes();
		
		//se pasan las fechas a Formato BD
		for(int i=0;i<numeroElementos;i++)
		{
			resumenAtencionesForm.setSolicitudes("fecha_orden_"+i,UtilidadFecha.conversionFormatoFechaABD(resumenAtencionesForm.getSolicitudes("fecha_orden_"+i)+""));
			resumenAtencionesForm.setSolicitudes("fecha_cirugia_"+i,UtilidadFecha.conversionFormatoFechaABD(resumenAtencionesForm.getSolicitudes("fecha_cirugia_"+i)+""));
		}
		
		
		resumenAtencionesForm.setSolicitudes(Listado.ordenarMapa(indices,
				resumenAtencionesForm.getColumna(),
				resumenAtencionesForm.getUltimaPropiedad(),
				resumenAtencionesForm.getSolicitudes(),
				numeroElementos));
		
		resumenAtencionesForm.setSolicitudes("numRegistros",numeroElementos+"");
		
		//se pasan las fechas a formato aplicacion
		for(int i=0;i<numeroElementos;i++)
		{
			resumenAtencionesForm.setSolicitudes("fecha_orden_"+i,UtilidadFecha.conversionFormatoFechaAAp(resumenAtencionesForm.getSolicitudes("fecha_orden_"+i)+""));
			resumenAtencionesForm.setSolicitudes("fecha_cirugia_"+i,UtilidadFecha.conversionFormatoFechaAAp(resumenAtencionesForm.getSolicitudes("fecha_cirugia_"+i)+""));
		}
		resumenAtencionesForm.setUltimaPropiedad(resumenAtencionesForm.getColumna());
		
		this.cerrarConexion(con);
		return mapping.findForward("listadoSolicitudesCx");
	}

	/**
	 * Método implementado para paginar el listado de registros
	 * @param con
	 * @param resumenAtencionesForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, ResumenAtencionesForm resumenAtencionesForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
		    this.cerrarConexion(con);
			response.sendRedirect(resumenAtencionesForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de ResumenAtencionesAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ResumenAtencionesAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método implementado para cargar las cirugias del paciente
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCirugias(Connection con, ResumenAtencionesForm resumenAtencionesForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		
		//Se instancia objeto de solicitudesCX
		SolicitudesCx solicitudes = new SolicitudesCx();
		
		//se consultan las solicitudes
		resumenAtencionesForm.setSolicitudes(solicitudes.listadoGeneralSolicitudesCx(con,resumenAtencionesForm.getCuenta()));
		//tamaño de las solicitudes
		resumenAtencionesForm.setNumeroSolicitudes(Integer.parseInt(resumenAtencionesForm.getSolicitudes("numRegistros")+""));
		
		//MAX PAGE ITEMS
		resumenAtencionesForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		
		this.cerrarConexion(con);
		return mapping.findForward("listadoSolicitudesCx");
	}

	/**
	 * Adición sebastián
	 * Método usado para ordenar el listado de ingresos 
	 * @param resumenAtencionesForm
	 */
	private void accionOrdenarIngresos(Connection con, ResumenAtencionesForm resumenAtencionesForm, ResumenAtenciones resumenAtencionesMundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
        try
        {
        	resumenAtencionesForm.setCuentas(Listado.ordenarColumna(new ArrayList(resumenAtencionesForm.getCuentas()), resumenAtencionesForm.getUltimaPropiedad(),resumenAtencionesForm.getColumna()));
        	resumenAtencionesForm.setUltimaPropiedad(resumenAtencionesForm.getColumna());
        	try
    		{
    			resumenAtencionesForm.setNumDocumentosAdjuntos(resumenAtencionesMundo.existenAdjuntos(con, paciente.getCodigoPersona()));
    			resumenAtencionesForm.setDocumentosAdjuntosGenerados(resumenAtencionesMundo.consultarDocumentosAdjuntos(con, paciente.getCodigoPersona()));
    		
    			if(resumenAtencionesForm.isOrdenamiento()){
    				resumenAtencionesForm.setOrdenamiento(false);
    			}else{
    				resumenAtencionesForm.setOrdenamiento(true);
    			}
    		
    		}
    		catch(SQLException e)
    		{
    			logger.error("Error cargando Documentos Adjuntos en accionEmpezar de ResumenAtencionesAction : "+e);
    		}

    		if(UtilidadValidacion.existeHojaObstetrica(con, paciente.getCodigoPersona()))
    		{
    			resumenAtencionesForm.setExisteHojaObstetrica("si");
    		}
    		else
    		{
    			resumenAtencionesForm.setExisteHojaObstetrica("no");
    		}
    		resumenAtencionesForm.setExisteTriage(ResumenAtenciones.existeTriage(con, paciente.getCodigoTipoIdentificacionPersona(), paciente.getNumeroIdentificacionPersona()));

    		//*******+SECCIÓN PARA CARGAR LOS ANTECEDENTES*****************************************
    		cargarAntecedentesAlergias(con,paciente,resumenAtencionesForm,0);
    		cargarAntecedentesFamiliares(con,paciente,resumenAtencionesForm,0);
    		cargarAntecedentesFamiliaresOculares(con,paciente,usuario,resumenAtencionesForm,0);
    		cargarAntecedentesPersonalesOculares(con,paciente,usuario,resumenAtencionesForm,0);
    		cargarAntecedentesGinecoObstetricos(con,paciente,usuario,resumenAtencionesForm,0);
    		cargarAntecedentesMedicamentos(con,paciente,resumenAtencionesForm,0);
    		cargarAntecedentesMedicos(con,paciente,resumenAtencionesForm,0);
    		cargarAntecedentesTransfusionales(con,paciente,resumenAtencionesForm,0);
    		cargarAntecedentesVacunas(con,paciente,resumenAtencionesForm,0);
    		//***************************************************************************************


    		//*******+SECCIÓN PARA CONSENTIMIENTO INFORMADO*****************************************
    		HashMap parametros = new HashMap();
    		parametros.put("ingreso",paciente.getCodigoIngreso());
    		resumenAtencionesForm.setHistorialConsentimientoInfMap(ResumenAtenciones.consultaHistorialConsentimientoInf(con, parametros));

    		if(Integer.parseInt(resumenAtencionesForm.getHistorialConsentimientoInfMap("numRegistros").toString())>0)
    			resumenAtencionesForm.setExisteHistorialConsentimiento(ConstantesBD.acronimoSi);
    		else
    			resumenAtencionesForm.setExisteHistorialConsentimiento(ConstantesBD.acronimoNo);

    		//***************************************************************************************


    		ArrayList<HashMap> tmp = (ArrayList<HashMap>) resumenAtencionesForm.getCuentas();
    		Collection tmp2=new ArrayList();
    		Integer tamLista=tmp.size();
    		for (int i = 0; i < tamLista; i++) {
    			HashMap mapaTmp=tmp.get(i);
    			mapaTmp.put("checkIngreso_"+i, "false");
    			tmp2.add(mapaTmp);
    		}
    		
    		
    		
    		
    		
    		
    		
    		
    		List<DtoIngresoHistoriaClinica> listaDtoHc= new ArrayList<DtoIngresoHistoriaClinica>();
    		for (int i = 0; i < tmp2.size(); i++) {
    			HashMap mapaIngresosHC = tmp.get(i);
    			DtoIngresoHistoriaClinica dtoHC = new DtoIngresoHistoriaClinica();
    			dtoHC.setIngreso(false);
    			dtoHC.setOrdenamiento(resumenAtencionesForm.isOrdenamiento());

    			
    			
    			int idCuentaAsocio = 0;
    			int viaIngreso2=Utilidades.convertirAEntero(tmp.get(i).get("codigoviaingreso")+"");
    			Boolean Asocio=false;
    			
    			if(!resumenAtencionesForm.isAsocio()||viaIngreso2==10||viaIngreso2==11||viaIngreso2==12 ||viaIngreso2==15)
    			{
    				if(viaIngreso2==10||viaIngreso2==11||viaIngreso2==12 || viaIngreso2==15)
    				{
    					idCuentaAsocio = Integer.parseInt(Utilidades.getCuentaAsociada(con,String.valueOf(tmp.get(i).get("cuenta")),false));
    					if(viaIngreso2==15){
    						viaIngreso2 = ConstantesBD.codigoViaIngresoUrgencias;
    					}
    					else{
    						viaIngreso2 = ConstantesBD.codigoViaIngresoHospitalizacion;
    					}
    					Asocio=true;
    				}
    				else{
    					Asocio=false;
    				}
    			}
    			else{
    				idCuentaAsocio = Integer.parseInt(resumenAtencionesForm.getCuentaAsocio());
    			}
    			
    			
    			
    			
    			
    			if(Asocio){
    				dtoHC.setAsocio("A");
    			}
    			
    			
    			
    			try {
    				dtoHC.setTipoPaciente(UtilidadesHistoriaClinica.obtenerTipoPaciente(con, String.valueOf(tmp.get(i).get("cuenta"))));
    			} catch (Exception e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    			
    			
    			
    			// fehcahora ingreso
    			int viaIngreso=Utilidades.convertirAEntero(tmp.get(i).get("codigoviaingreso")+"");
    			String fechaIngreso="";
    			String fecha="";
    			if( tmp.get(i).get("fechaadmisionhospitalaria")!=null && ( viaIngreso==1 || viaIngreso==10 || viaIngreso==11 || viaIngreso==12 || viaIngreso==14)){
    				fechaIngreso=UtilidadFecha.conversionFormatoFechaAAp(String.valueOf( tmp.get(i).get("fechaadmisionhospitalaria")));
    				dtoHC.setFechaIngreso(fechaIngreso);
    				fecha =	UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(tmp.get(i).get("fechaadmisionhospitalaria")))+" - "+UtilidadFecha.convertirHoraACincoCaracteres(String.valueOf(tmp.get(i).get("horaadmisionhospitalaria")));
    			}else if( tmp.get(i).get("fechaadmisionurgencias")!=null && ( viaIngreso==3 || viaIngreso==13)){
    				fechaIngreso=UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(tmp.get(i).get("fechaadmisionurgencias")));
    				dtoHC.setFechaIngreso(fechaIngreso);
    				fecha =		UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(tmp.get(i).get("fechaadmisionurgencias"))) +" - "+ UtilidadFecha.convertirHoraACincoCaracteres(String.valueOf(tmp.get(i).get("horaadmisionurgencias")));
    			}else if( tmp.get(i).get("fechacuenta")!=null && (viaIngreso==2 || viaIngreso==4) ){
    				fechaIngreso=UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(tmp.get(i).get("fechacuenta")));
    				dtoHC.setFechaIngreso(fechaIngreso);
    				fecha =	UtilidadFecha.conversionFormatoFechaAAp(String.valueOf( tmp.get(i).get("fechacuenta")))+" - "+ UtilidadFecha.convertirHoraACincoCaracteres(String.valueOf(tmp.get(i).get("horacuenta"))).subSequence(0,5);
    			}
    			dtoHC.setFechaHoraIngreso(fecha);


    			//fecha de egreso
    			String fechaEgreso="";
    			if(tmp.get(i).get("fechaegreso")!=null){
    				fechaEgreso=UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(tmp.get(i).get("fechaegreso")));
    			}

    			if((tmp.get(i).get("horaegreso")!=null)){
    				fechaEgreso+=UtilidadFecha.convertirHoraACincoCaracteres(String.valueOf(tmp.get(i).get("horaegreso")));
    			}
    			dtoHC.setFechaHoraEgreso(fechaEgreso);

    			//centro de atencion
    			dtoHC.setCentroAtencion(String.valueOf(tmp.get(i).get("centroatencion")));
    			try {
    			//via de ingreso
    			String viaIngresoSwitch="";
    			switch(viaIngreso){
    			case 1:
    				viaIngresoSwitch="HOS";
    				
    					dtoHC.setEspecialidad(UtilidadesHistoriaClinica.obtenerTipoPacienteHosp(con, String.valueOf(tmp.get(i).get("cuenta"))));
    				
    				break;
    			case 2:
    				viaIngresoSwitch="AMB";
    				dtoHC.setEspecialidad("");
    				break;
    			case 3:
    				viaIngresoSwitch="URG";
    				dtoHC.setEspecialidad(UtilidadesHistoriaClinica.especialidadUrgencia(con, String.valueOf(tmp.get(i).get("cuenta"))));
    				break;
    			case 4:
    				viaIngresoSwitch="CE";
    				dtoHC.setEspecialidad(UtilidadesHistoriaClinica.obtenerTipoPacienteConsultaExterna(con, String.valueOf(tmp.get(i).get("cuenta"))));
    				break;
    			case 10:
    				viaIngresoSwitch="URG/HOS - HOSP";
    				dtoHC.setEspecialidad(UtilidadesHistoriaClinica.obtenerTipoPacienteHosp(con, String.valueOf(tmp.get(i).get("cuenta"))));
    				break;
    			case 11:
    				viaIngresoSwitch="URG/HOS - CX"; 
    				dtoHC.setEspecialidad(UtilidadesHistoriaClinica.obtenerTipoPacienteHosp(con, String.valueOf(tmp.get(i).get("cuenta"))));
    				break;
    			case 12:
    				viaIngresoSwitch="HOS/HOS - HOSP";
    				dtoHC.setEspecialidad(UtilidadesHistoriaClinica.obtenerTipoPacienteHosp(con, String.valueOf(tmp.get(i).get("cuenta"))));
    				break;
    			case 13:
    				viaIngresoSwitch="URG.";
    				dtoHC.setEspecialidad(String.valueOf(tmp.get(i).get("especialidad")));
    				break;
    			case 14:
    				viaIngresoSwitch="HOS.";
    				dtoHC.setEspecialidad(String.valueOf(tmp.get(i).get("especialidad")));
    				break;
    			case 15:
    				viaIngresoSwitch="	URG/URG";
    				dtoHC.setEspecialidad(UtilidadesHistoriaClinica.obtenerTipoPacienteHosp(con, String.valueOf(tmp.get(i).get("cuenta"))));
    				break;
    			}
    		
    			dtoHC.setViaIng(viaIngresoSwitch);
    			} catch (Exception e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    			//especialidad
    			
//    			if(tmp.get(i).get("especialidad")!=null){
//    				dtoHC.setEspecialidad(String.valueOf(tmp.get(i).get("especialidad")));
//    			}else{
//    				dtoHC.setEspecialidad("");
//    			}
    			
    			// ingreso
    			if(tmp.get(i).get("consecutivoingreso")!=null){
    				dtoHC.setIng(String.valueOf(tmp.get(i).get("consecutivoingreso")));
    			}else{
    				dtoHC.setIng("");
    			}
    			
    			//estado ingreso
    			if(tmp.get(i).get("estado_ingreso")!=null){
    				dtoHC.setEstadoIngreso(String.valueOf(tmp.get(i).get("estado_ingreso")));
    			}else{
    				dtoHC.setEstadoIngreso("");
    			}
    			
    			//estado cuenta 
    			if(tmp.get(i).get("estadocuenta")!=null){
    				dtoHC.setEstadoCuenta(String.valueOf(tmp.get(i).get("estadocuenta")));
    			}
    			
    			
    			if(tmp.get(i).get("cuenta")!=null){
    				dtoHC.setCuenta(String.valueOf(tmp.get(i).get("cuenta")));
    			}
    			
    			// ingreso
    			if(tmp.get(i).get("cuenta")!=null){
    				dtoHC.setIng2(String.valueOf(UtilidadValidacion.obtenerIngreso(con,Integer.valueOf(String.valueOf(tmp.get(i).get("cuenta"))))));
    			}else{
    				dtoHC.setIng("");
    			}
    			
    			if(tmp.get(i).get("codigoviaingreso")!=null){
    				dtoHC.setCodigoViaIngreso(String.valueOf(tmp.get(i).get("codigoviaingreso")));
    			}
    			 //UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(cuenta.get("fechacuenta")));%> -->
    			
    			if(tmp.get(i).get("viaingreso")!=null){
    				dtoHC.setViaIngreso(String.valueOf(tmp.get(i).get("viaingreso")));
    			}
    			
    			boolean tieneEgreso;
    			try {
    				tieneEgreso = util.UtilidadValidacion.tieneEgreso(con, Integer.parseInt(tmp.get(i).get("cuenta").toString()));
    				
    				if(tieneEgreso)
    				{
    					if(tmp.get(i).get("diagnostico")==null)
    					{
    						dtoHC.setDx("");
    					}
    					else
    					{
    						String[] codigoDxJsp=String.valueOf( tmp.get(i).get("diagnostico")).split(" ");
    						if(codigoDxJsp.length>0){
    							dtoHC.setDx(codigoDxJsp[0]);
    						}else{
    							dtoHC.setDx(String.valueOf( tmp.get(i).get("diagnostico")));
    						}
    					}
    				}
    				else
    				{	
    					dtoHC.setDx("");
    				}
    				
    				
    			} catch (NumberFormatException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}

    			
    			dtoHC.setReIngreso(String.valueOf(tmp.get(i).get("reingreso")));
    			dtoHC.setPreIngreso(String.valueOf(tmp.get(i).get("preingreso")));
    			
    			dtoHC.setCodigoPkEntidadSubcontratada(String.valueOf(tmp.get(i).get("codigo_pk")));
    			dtoHC.setRazonSocialEntidadSubcontratada(String.valueOf(tmp.get(i).get("razon_social")));
    			dtoHC.setCodigoDx(String.valueOf(tmp.get(i).get("codigodx")));
    			dtoHC.setCodigoCie(String.valueOf(tmp.get(i).get("codigocie")));
    			dtoHC.setDescripcionDx(String.valueOf(tmp.get(i).get("descripciondx")));
    			
    			
    			listaDtoHc.add(dtoHC);
    			
    		}

    		resumenAtencionesForm.setListaDtoHc(listaDtoHc);
    		request.getSession().setAttribute("listaIngresos", listaDtoHc);
    		//Se valida el parametro guardado en base de datos para identificar si se imprime el reporte en PDF o en JSP
    		resumenAtencionesForm.setEstadoHCCLiente( UtilidadesHistoriaClinica.consultarEstadoFuncionalidadHistoriaClincia(con));
    		
    		
        }
        catch (Exception e)
        {
            logger.warn("Error en el listado de documentos");
            e.printStackTrace();
        }	
	}
	/**
	 * @param resumenAtencionesForm
	 */
	private void accionOrdenar(ResumenAtencionesForm resumenAtencionesForm)
	{
        try
        {
        	resumenAtencionesForm.setColeccionSolicitudes(Listado.ordenarColumna(new ArrayList(resumenAtencionesForm.getColeccionSolicitudes()), resumenAtencionesForm.getUltimaPropiedad(),resumenAtencionesForm.getColumna()));
        	resumenAtencionesForm.setUltimaPropiedad(resumenAtencionesForm.getColumna());
        }
        catch (Exception e)
        {
            logger.warn("Error en el listado de documentos");
            e.printStackTrace();
        }	
	}

	
	
	/**
	 * 
	 * Metodo que consulta las solicitudes que han tenido administracion
	 * @param con
	 * @param resumenAtencionesForm
	 * @param paciente
	 */
	private void consultarSolicitudesConAdministracion(Connection con, ResumenAtencionesForm resumenAtencionesForm)
	{
		SolicitudMedicamentos mundo=new SolicitudMedicamentos();
		mundo.resetCompleto();
		Collection colTempo=null;
		colTempo=mundo.consultarSolicitudesMedicamentoConAdministracion(con,resumenAtencionesForm.getCuenta());
		try
		{
			resumenAtencionesForm.setColeccionSolicitudes(Listado.ordenarColumna(new ArrayList(colTempo), "","fechahorasolicitud"));
		}
		catch (IllegalAccessException e)
		{
			logger.warn("error mientras se ordenan las solicitudes"+e);
		}
		
	}
	
	/**
	 * Método para adjunta un documento de una cuenta
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 */
	private ActionForward accionAdjuntarDocumento(Connection con, ActionMapping mapping, HttpServletRequest request, ResumenAtencionesForm forma, ResumenAtenciones mundo, PersonaBasica paciente) throws Exception
	{
    	String archivo[]=forma.getNombreArchivo().split(ConstantesBD.separadorSplit);
    	String nombreArchivo=archivo[0];
    	String nombreOriginal=archivo[1];
    	
    	/*
    	if(paciente.getCodigoCuenta() <= 0)
    	{
    		UtilidadBD.closeConnection(con);
    		ActionErrors errores = new ActionErrors();
    		errores.add("descripcion", new ActionMessage("errors.notEspecific","El Paciente no Tiene Cuenta Activa para Asociar el Documento"));
    		saveErrors(request, errores);
			return mapping.findForward("paginaInicio");
    	}
    	*/
    	
    	int insercion=mundo.adjuntarDocumento(con, paciente.getCodigoPersona(), nombreOriginal, nombreArchivo);
    	
    	if(insercion<=0)
    	{
    		UtilidadBD.closeConnection(con);
			ArrayList atributosError = new ArrayList();
			atributosError.add(" No se logro adjuntar Documento");
			request.setAttribute("codigoDescripcionError", "errors.imposibleAdjuntarDocumento");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");	
    	}
    	else
    	{
    		forma.setNumDocumentosAdjuntos(mundo.existenAdjuntos(con, paciente.getCodigoPersona()));
			forma.setDocumentosAdjuntosGenerados(mundo.consultarDocumentosAdjuntos(con, paciente.getCodigoPersona()));
			UtilidadBD.cerrarConexion(con);
    		return mapping.findForward("paginaInicio");
    	}
	}
	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	public void cerrarConexion (Connection con)
	{
	    try
		{
	        UtilidadBD.cerrarConexion(con);
	    }
	    catch(Exception e)
		{
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}
	
	//*********************************************************************************************************************
	
	/**
	 * Carga la informacion que esta contenida en el mapa de cuentas a los mapa particulares para cada 
	 * seccion del resumen de atenciones  
	 * @param Connection con
	 * @param ResumenAtencionesForm forma
	 * @param String opcion
	 * */
	public void cargarDatosMapaCuentasForma(Connection con,ResumenAtencionesForm forma,String opcion)
	{
		if(opcion.equals("detalleEvoluciones"))
		{
			if(forma.getMapaValidacionesInfoCuentas().containsKey("codigosEvoluciones_0"))
				forma.setCodigosEvoluciones((HashMap)forma.getMapaValidacionesInfoCuentas("codigosEvoluciones_0"));			
			
			if(forma.getMapaValidacionesInfoCuentas().containsKey("codigosEvolucionesAsocio_"+forma.getIndiceMapaValInfoCuentas()))
				forma.setCodigosEvolucionesAsocio((HashMap)forma.getMapaValidacionesInfoCuentas("codigosEvolucionesAsocio_"+forma.getIndiceMapaValInfoCuentas()));						
		}
		
		if(opcion.equals("cargarCuenta"))
		{
			forma.setCuenta(Integer.parseInt(forma.getMapaValidacionesInfoCuentas("idCuenta_0").toString()));
			forma.setCuentaAsocio(forma.getMapaValidacionesInfoCuentas("idCuenta_"+forma.getIndiceMapaValInfoCuentas()).toString());
		}				
		
		if(opcion.equals("detalleValoracionInicial")) {
			if(forma.getMapaValidacionesInfoCuentas().containsKey("datosValoracion_"+forma.getIndiceMapaValInfoCuentas())) {
				forma.setDatosValoracionInicial((HashMap<String, Object>)forma.getMapaValidacionesInfoCuentas("datosValoracion_"+forma.getIndiceMapaValInfoCuentas()));
			}
		}
	}
	
	//*********************************************************************************************************************
	

	//----------------------------------------------------------------------------------------------
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/************************************************************************************************
	 * METODOS PARA CARGOS DIRECTOS. Anexo 550*******************************************************
	 * *********************************************************************************************/
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//-----------------------------------------------------------------------------------------------	
		
	/**
	 * Carga el detalle para la seccion de cargos directos
	 * @param Connection con
	 * @param ResumenAtencionesForm forma
	 * @param ResumenAtenciones mundo
	 * @param PersonaBasica paciente
	 * */
	public void accionCargoDirecto(
			Connection con,
			ResumenAtencionesForm forma,
			ResumenAtenciones mundo,
			PersonaBasica paciente,
			UsuarioBasico usuario)
	{
		HashMap solicitudesMap = new HashMap();				
		//Captura la información de la Cuenta 		
		String  cuenta = "";				
		
		if(forma.isCuentaAsociada())	
			cuenta = forma.getCuentaAsocio();		
		else
			cuenta = forma.getCuenta()+"";		
		
		//Consulta la información de todas las solicitudes del Grupo de Cargo Directo
		solicitudesMap = ResumenAtenciones.consultarListadoSolCargoDirectoDe(
				con, 
				cuenta,
				forma.getFechaInicial(), 
				forma.getFechaFinal(), 
				forma.getHoraInicial(), 
				forma.getHoraFinal());
		
		//Inicializa los valores de los mapa				
		forma.setSolCDirectosArticulosMap(
		ResumenAtenciones.armarMapaCargoDirectoDe(con,solicitudesMap,ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+"",""));
		
		Utilidades.imprimirMapa(forma.getSolCDirectosArticulosMap());
		
		forma.setSolCDirectosConsultasMap(
		ResumenAtenciones.armarMapaCargoDirectoDe(con,solicitudesMap,ConstantesBD.codigoTipoSolicitudCargosDirectosServicios+"",ConstantesBD.codigoServicioCargosConsultaExterna+""));
		
		forma.setSolCDirectosProcedimientosMap(
		ResumenAtenciones.armarMapaCargoDirectoDe(con,solicitudesMap,ConstantesBD.codigoTipoSolicitudCargosDirectosServicios+"",ConstantesBD.codigoServicioProcedimiento+""));
		
		Utilidades.imprimirMapa(forma.getSolCDirectosProcedimientosMap());
		
		forma.setSolCDirectosServiciosMap(
		ResumenAtenciones.armarMapaCargoDirectoDe(con,solicitudesMap,ConstantesBD.codigoTipoSolicitudCargosDirectosServicios+"",""));
		
		forma.setSolCDirectosCirugiaDyTMap(
		ResumenAtenciones.armarMapaCargoDirectoDe(con,solicitudesMap,ConstantesBD.codigoTipoSolicitudCirugia+"",""));
				
		Utilidades.imprimirMapa(forma.getSolCDirectosCirugiaDyTMap());		
		
		//Carga la informacion de las Hojas de Anestesia y Quirurgica
		if(Utilidades.convertirAEntero(forma.getSolCDirectosCirugiaDyTMap("numRegistros").toString()) > 0)
		{
			String listadoPeticiones = obtenerListaSeparadasComa(forma.getSolCDirectosCirugiaDyTMap(),"codigoPeticion_");
			String listadoSolicitudes = obtenerListaSeparadasComa(forma.getSolCDirectosCirugiaDyTMap(),"numeroSolicitud_");
			cargarInformacionHojas(con, forma, new ImpresionResumenAtenciones(), listadoPeticiones, listadoSolicitudes, usuario);
		}
	}	
	
	//*********************************************************************************************************************
	
	/**
	 * Carga la informacion de las Hojas de Anestesia y Quirurgica para los servicios de Cirugias de 
	 * cargo directo
	 * @param Connection con
	 * @param ResumenAtencionesForm forma			
	 * @param ImpresionResumenAtenciones mundo,
	 * @param String listadoPeticiones,
	 * @param String listadoSolicitudes,
	 * @param UsuarioBasico usuario
	 * */
	public void cargarInformacionHojas(
			Connection con,
			ResumenAtencionesForm forma,
			ImpresionResumenAtenciones mundo,
			String listadoPeticiones,
			String listadoSolicitudes,
			UsuarioBasico usuario)
	{				
		//----------- Se consultan los posibles encabezados de la hoja de anestesia -----------------//
		if(forma.isCuentaAsociada())
			forma.setMapaEncabezadosHojaAnestesiaAsocio(mundo.consultarEncabezadosHojaAnestesia(con, listadoPeticiones));
		else			
			forma.setMapaEncabezadosHojaAnestesia(mundo.consultarEncabezadosHojaAnestesia(con, listadoPeticiones));    	
    	
    	//------------- Se consultan los exámenes de laboratorio de la preanestesia ----------------------//
    	if(forma.isCuentaAsociada())
    		forma.setMapaExamenesLaboratorioPreanestesiaAsocio(mundo.consultarExamenesLaboratorioPreanestesia(con, listadoPeticiones));
    	else    	
    		forma.setMapaExamenesLaboratorioPreanestesia(mundo.consultarExamenesLaboratorioPreanestesia(con, listadoPeticiones));
    	
    	//------------ Se consulta el histórico de los exámenes físico de tipo text ------------------------//
    	if(forma.isCuentaAsociada())
    		forma.setMapaHistoExamenesFisicosTextAsocio(mundo.consultarHistoExamenesFisicos(con, listadoPeticiones, true));
    	else
    		forma.setMapaHistoExamenesFisicosText(mundo.consultarHistoExamenesFisicos(con, listadoPeticiones, true));
    	
    	//------------ Se consulta el histórico de los exámenes físico de tipo text area ------------------------//
    	if(forma.isCuentaAsociada())
    		forma.setMapaHistoExamenesFisicosTextAreaAsocio(mundo.consultarHistoExamenesFisicos(con, listadoPeticiones, false));
    	else
    		forma.setMapaHistoExamenesFisicosTextArea(mundo.consultarHistoExamenesFisicos(con, listadoPeticiones, false));
    	
    	//------------ Se consulta el histórico de las conclusiones de preanestesia------------------------//
    	if(forma.isCuentaAsociada())
    		forma.setMapaHistoConclusionesAsocio(mundo.consultarHistoConclusiones(con, listadoPeticiones));
    	else
    		forma.setMapaHistoConclusiones(mundo.consultarHistoConclusiones(con, listadoPeticiones));  	
    	
    	//-------------------------------- Hoja Quirurgica ------------------------------------------
    	cargarHojaQuirurgica(con,listadoSolicitudes,forma,usuario);    	
	}
	
	//*********************************************************************************************************************
	
	
	//----------------------------------------------------------------------------------------------
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/************************************************************************************************
	 * METODOS PARA HOJA DE ANESTESIA. Anexo 529*****************************************************
	 * *********************************************************************************************/
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//-----------------------------------------------------------------------------------------------	
	
	/**
	 * Método que carga información de la hoja de Anestesia (SECCION CIRUGIAS)
	 * @param Connection con
	 * @param String solicitudes, separados por coma
	 * @param ResumenAtencionesForm forma
	 * @param UsuarioBasico usuario
	 */
	private void cargarHojaAnestesia(Connection con, String solicitudes, ResumenAtencionesForm forma,UsuarioBasico usuario) 
	{
		HojaAnestesia hanes = new HojaAnestesia();
		forma.setMapaHojaAnestesia(new HashMap());
				
		logger.info("entro cargar hoja");
		String [] solicitudesArray;
		
		solicitudesArray = solicitudes.split(",");
		
		//Recorre las solicitudes 
		for(int i = 0; i <solicitudesArray.length; i++)
			if(!solicitudesArray[i].equals("")) 
				forma.setMapaHojaAnestesia(solicitudesArray[i],hanes.cargarDtoHojaAnestesia(
						con,
						solicitudesArray[i],
						true,
						true,
						true,
						true, 
						true, 
						true, 
						true,
						usuario.getCodigoInstitucionInt()));
	}
	
	//**********************************************************************
	
	/**
	 * Operaciones del estado 
	 * @param ResumenAtencionesForm forma
	 * */
	private void accionDetalleDatosCirugia(ResumenAtencionesForm forma)
	{
		forma.setPreanestesiaDto(new DtoPreanestesia());
		HashMap mapa = new HashMap();
		HashMap mapa1 = new HashMap();
		int numRegistros = 0;
		
		//evalua si tiene informacion de examen de laboratorio
		if(!forma.isCuentaAsociada())
			mapa = forma.getMapaExamenesLaboratorioPreanestesia();
		else
			mapa = forma.getMapaExamenesLaboratorioPreanestesiaAsocio();

		numRegistros = Utilidades.convertirAEntero(mapa.get("numRegistros").toString());
		
		for(int i=0; i<numRegistros && forma.getPreanestesiaDto().getExisteInfoExamenLaboratorio().equals(ConstantesBD.acronimoNo); i++)
			if(mapa.get("codigo_peticion_"+i).toString().equals(forma.getCodigoPeticionCx()))
				forma.getPreanestesiaDto().setExisteInfoExamenLaboratorio(ConstantesBD.acronimoSi);
		
		//***************************************************************
		
		//evalua si tiene informacion de examen fisico
		if(!forma.isCuentaAsociada())
		{
			mapa = forma.getMapaHistoExamenesFisicosText();
			mapa1 = forma.getMapaHistoExamenesFisicosTextArea();
		}
		else
		{
			mapa = forma.getMapaHistoExamenesFisicosTextAsocio();
			mapa1 = forma.getMapaHistoExamenesFisicosTextAreaAsocio();
		}

		numRegistros = Utilidades.convertirAEntero(mapa.get("numRegistros").toString());
		
		for(int i=0; i<numRegistros && forma.getPreanestesiaDto().getExisteInfoExamenLaboratorio().equals(ConstantesBD.acronimoNo); i++)		
			if(mapa.get("codigo_peticion_"+i).toString().equals(forma.getCodigoPeticionCx()))
				forma.getPreanestesiaDto().setExisteInfoExamenLaboratorio(ConstantesBD.acronimoSi);		
		
		numRegistros = Utilidades.convertirAEntero(mapa1.get("numRegistros").toString());
		
		for(int i=0; i<numRegistros && forma.getPreanestesiaDto().getExisteInfoExamenLaboratorio().equals(ConstantesBD.acronimoNo); i++)		
			if(mapa1.get("codigo_peticion_"+i).toString().equals(forma.getCodigoPeticionCx()))
				forma.getPreanestesiaDto().setExisteInfoExamenLaboratorio(ConstantesBD.acronimoSi);
			
	
		//***************************************************************
		
		//Evalua si posee informacion de conclusiones		
		if(!forma.isCuentaAsociada())
			mapa = forma.getMapaHistoConclusiones();
		else
			mapa = forma.getMapaHistoConclusionesAsocio();

		numRegistros = Utilidades.convertirAEntero(mapa.get("numRegistros").toString());
		
		for(int i=0; i<numRegistros && forma.getPreanestesiaDto().getExisteInfoConclusiones().equals(ConstantesBD.acronimoNo); i++)
			if(mapa.get("codigo_peticion_"+i).toString().equals(forma.getCodigoPeticionCx()))
				forma.getPreanestesiaDto().setExisteInfoConclusiones(ConstantesBD.acronimoSi);
		
		//***************************************************************
		
		//Evalua si posee informacion de las observaciones
		if(!forma.isCuentaAsociada())
			mapa = forma.getMapaEncabezadosHojaAnestesia();
		else
			mapa = forma.getMapaEncabezadosHojaAnestesiaAsocio();

		numRegistros = Utilidades.convertirAEntero(mapa.get("numRegistros").toString());
		
		for(int i=0; i<numRegistros && forma.getPreanestesiaDto().getExisteInfoObservGenerales().equals(ConstantesBD.acronimoNo); i++)
			if(mapa.get("codigo_peticion_"+i).toString().equals(forma.getCodigoPeticionCx()) 
					&& !mapa.get("observaciones_pre_anes_"+i).toString().equals(""))
				forma.getPreanestesiaDto().setExisteInfoObservGenerales(ConstantesBD.acronimoSi);			
	}
	
	/**
	 * Método para ingresar al registro de notas Aclaratorias
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionRegistroNotasAclaratorias(Connection con, ResumenAtencionesForm resumenAtencionesForm, 
								PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping) {
		try{
			HibernateUtil.beginTransaction();
			resumenAtencionesForm.getNotasAclaratorias().clear();
			INotaAclaratoriaMundo notaAclaratoriaMundo = HistoriaClinicaFabricaMundo.crearNotaAclaratoriaMundo();
			resumenAtencionesForm.setGuardarNotaAclaratoria(false);
			resumenAtencionesForm.setDetalleNotaAclaratoria(false);
			resumenAtencionesForm.setNuevaNotaAclaratoria(Utilidades.tieneRolFuncionalidad(con, usuario.getLoginUsuario(), ConstantesBD.codigoFuncionalidadRegistroNotasAclaratorias));
			resumenAtencionesForm.getNotasAclaratorias().addAll(notaAclaratoriaMundo.buscarNotasAclaratoriasPorIngreso(new Integer(resumenAtencionesForm.getIdIngreso())));
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			logger.error(e);
			HibernateUtil.abortTransaction();
		}
		return mapping.findForward("registroNotasAclaratorias");
	}
	
	/**
	 * Método para habilitar el registro de una nueva nota Aclaratoria
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionNuevaNotaAclaratoria(Connection con, ResumenAtencionesForm resumenAtencionesForm, 
								PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping) {
		resumenAtencionesForm.setGuardarNotaAclaratoria(true);
		resumenAtencionesForm.setDetalleNotaAclaratoria(true);
		resumenAtencionesForm.setNotaAclaratoriaDTO(new DtoNotaAclaratoria());
		return mapping.findForward("registroNotasAclaratorias");
	}
	
	/**
	 * Método para guardar el registro de una nueva nota Aclaratoria
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarNotaAclaratoria(Connection con, ResumenAtencionesForm resumenAtencionesForm, 
								PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, ActionErrors errores,
								HttpServletRequest request) {
		try{
			if(resumenAtencionesForm.getNotaAclaratoriaDTO().getDescripcion() == null
					|| resumenAtencionesForm.getNotaAclaratoriaDTO().getDescripcion().trim().isEmpty()){
				errores.add("La Descripción es Requerida", new ActionMessage("errors.required","La Nota Aclaratoria"));
				saveErrors(request, errores);
				return mapping.findForward("registroNotasAclaratorias");
			}
			else if(resumenAtencionesForm.getNotaAclaratoriaDTO().getDescripcion().length() > 4000){
				errores.add("La Descripción NO puede superar 4000 Caracteres", new ActionMessage("error.historiaClinica.tamanioTextoExcedido","Notas Aclaratorias Nuevas"));
				saveErrors(request, errores);
				return mapping.findForward("registroNotasAclaratorias");
			}
			HibernateUtil.beginTransaction();
			INotaAclaratoriaMundo notaAclaratoriaMundo=HistoriaClinicaFabricaMundo.crearNotaAclaratoriaMundo();
			IAdjuntoNotaAclaratoriaMundo adjuntoNotaAclaratoriaMundo=HistoriaClinicaFabricaMundo.crearAdjuntoNotaAclaratoriaMundo();
			NotaAclaratoria notaAclaratoria = new NotaAclaratoria();
			AdjuntoNotaAclaratoria adjuntoNotaAclaratoria = new AdjuntoNotaAclaratoria();
			Medicos medico= new Medicos();
			Ingresos ingreso= new Ingresos();
			
			Date fechaActual=UtilidadFecha.getFechaActualTipoBD();
			String horaActual=UtilidadFecha.getHoraActual(con);
			resumenAtencionesForm.setGuardarNotaAclaratoria(false);
			resumenAtencionesForm.setDetalleNotaAclaratoria(false);
			resumenAtencionesForm.getNotaAclaratoriaDTO().setFecha(fechaActual);
			resumenAtencionesForm.getNotaAclaratoriaDTO().setHora(horaActual);
			resumenAtencionesForm.getNotaAclaratoriaDTO().setCodigoProfesional(usuario.getCodigoPersona());
			resumenAtencionesForm.getNotaAclaratoriaDTO().setNombreCompletoProfesional(usuario.getNombreUsuario());
			resumenAtencionesForm.getNotaAclaratoriaDTO().setNumeroRegistroProfesional(usuario.getNumeroRegistroMedico());
			resumenAtencionesForm.getNotaAclaratoriaDTO().setEspecialidadesProfesional(usuario.getEspecialidadesMedico());
			
			notaAclaratoria.setDescripcion(resumenAtencionesForm.getNotaAclaratoriaDTO().getDescripcion());
			notaAclaratoria.setFechaRegistro(resumenAtencionesForm.getNotaAclaratoriaDTO().getFecha());
			notaAclaratoria.setHoraRegistro(resumenAtencionesForm.getNotaAclaratoriaDTO().getHora());
			ingreso.setId(new Integer(resumenAtencionesForm.getIdIngreso()));
			medico.setCodigoMedico(resumenAtencionesForm.getNotaAclaratoriaDTO().getCodigoProfesional());
			notaAclaratoria.setIngresos(ingreso);
			notaAclaratoria.setMedicos(medico);
			notaAclaratoria=notaAclaratoriaMundo.merge(notaAclaratoria);
			resumenAtencionesForm.getNotaAclaratoriaDTO().setCodigo(notaAclaratoria.getCodigo());
			if(resumenAtencionesForm.getNotaAclaratoriaDTO().getArchivosAdjuntos() != null
					&& !resumenAtencionesForm.getNotaAclaratoriaDTO().getArchivosAdjuntos().isEmpty()){
				ArrayList<DTOArchivoAdjunto> adjuntosSeleccionados = new ArrayList<DTOArchivoAdjunto>();
				for(DTOArchivoAdjunto adjunto:resumenAtencionesForm.getNotaAclaratoriaDTO().getArchivosAdjuntos()){
					if(adjunto.isActivo()){
						adjunto.setFecha(fechaActual);
						adjunto.setHora(horaActual);
						adjuntosSeleccionados.add(adjunto);
						adjuntoNotaAclaratoria = new AdjuntoNotaAclaratoria();
						adjuntoNotaAclaratoria.setFechaRegistro(fechaActual);
						adjuntoNotaAclaratoria.setHoraRegistro(horaActual);
						adjuntoNotaAclaratoria.setNombreGenerado(adjunto.getNombreGenerado());
						adjuntoNotaAclaratoria.setNombreOriginal(adjunto.getNombreOriginal());
						adjuntoNotaAclaratoria.setNotaAclaratoria(notaAclaratoria);
						adjuntoNotaAclaratoriaMundo.merge(adjuntoNotaAclaratoria);
					}
				}
				resumenAtencionesForm.getNotaAclaratoriaDTO().getArchivosAdjuntos().clear();
				resumenAtencionesForm.getNotaAclaratoriaDTO().getArchivosAdjuntos().addAll(adjuntosSeleccionados);
			}	
			resumenAtencionesForm.getNotasAclaratorias().add(resumenAtencionesForm.getNotaAclaratoriaDTO());
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
		return mapping.findForward("registroNotasAclaratorias");
	}
	
	/**
	 * Método para mostar el detalle de una nueva nota Aclaratoria
	 * @param con
	 * @param resumenAtencionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleNotaAclaratoria(Connection con, ResumenAtencionesForm resumenAtencionesForm, 
								PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping) {
		
		resumenAtencionesForm.setNotaAclaratoriaDTO(new DtoNotaAclaratoria());
		if(resumenAtencionesForm.getCodigoNotaAclaratoria() != null &&
				!resumenAtencionesForm.getCodigoNotaAclaratoria().trim().isEmpty()){
			long idNotaSeleccionado=new Long(resumenAtencionesForm.getCodigoNotaAclaratoria());
			for(DtoNotaAclaratoria dtoNota: resumenAtencionesForm.getNotasAclaratorias()){
				if(dtoNota.getCodigo()==idNotaSeleccionado){
					resumenAtencionesForm.setGuardarNotaAclaratoria(false);
					resumenAtencionesForm.setDetalleNotaAclaratoria(true);
					resumenAtencionesForm.setNotaAclaratoriaDTO(dtoNota);
					break;
				}
			}
		}
		return mapping.findForward("registroNotasAclaratorias");
	}
	
	public void inicializarListaChecks(ResumenAtencionesForm resumenAtencionesForm){
		resumenAtencionesForm.setHaSeleccionadoIngreso(false);
		resumenAtencionesForm.setCantidadIngresosSeleccionados(0);
		
		List<Boolean> tmp = new ArrayList<Boolean>();
		for (int i = 0; i < 30; i++) {
			tmp.add(true);
		}
		resumenAtencionesForm.setListaCheckGlobalHC(tmp);
		resumenAtencionesForm.setEstadoCheckTodos(true);
		
	}
	
}