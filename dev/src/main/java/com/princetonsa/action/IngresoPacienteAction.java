/*
 * @(#)IngresoPacienteAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ElementoApResource;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.RespuestaInsercionPersona;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.ResultadoDouble;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilConversionMonedas;
import util.facturacion.ConstantesBDFacturacion;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.interfaces.ConstantesBDInterfaz;
import util.interfaces.UtilidadBDInterfaz;
import util.inventarios.UtilidadInventarios;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.IngresoPacienteForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.administracion.DtoTiposMoneda;
import com.princetonsa.dto.carteraPaciente.DtoAutorizacionSaldoMora;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.dto.facturacion.DtoValidacionTipoCobroPaciente;
import com.princetonsa.dto.historiaClinica.DtoTriage;
import com.princetonsa.dto.interfaz.DtoInterfazPaciente;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.dto.manejoPaciente.DtoReingresoSalidaHospiDia;
import com.princetonsa.dto.manejoPaciente.DtoRequsitosPaciente;
import com.princetonsa.dto.manejoPaciente.DtoResponsablePaciente;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.AdmisionHospitalaria;
import com.princetonsa.mundo.AdmisionUrgencias;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.TrasladoCamas;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Egreso;
import com.princetonsa.mundo.capitacion.SubirPaciente;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.cargos.MontosCobro;
import com.princetonsa.mundo.carteraPaciente.AutorizacionIngresoPacienteSaldoMora;
import com.princetonsa.mundo.carteraPaciente.DocumentosGarantia;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.facturacion.RequisitosPacientesXConvenio;
import com.princetonsa.mundo.historiaClinica.Referencia;
import com.princetonsa.mundo.manejoPaciente.Autorizaciones;
import com.princetonsa.mundo.manejoPaciente.CierreIngreso;
import com.princetonsa.mundo.manejoPaciente.ReingresoHospitalDia;
import com.princetonsa.mundo.manejoPaciente.ViasIngreso;
import com.princetonsa.mundo.presupuesto.PresupuestoPaciente;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.triage.ConsultaPacientesTriage;
import com.princetonsa.mundo.triage.Triage;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.dto.capitacion.DtoUsuariosXConvenio;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontosCobroServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IValidacionTipoCobroPacienteServicio;

/**
 * Action donde se maneja toda la dinamica del negocio en la
 * funcionalidad de ingresar paciente. (Anteriormente esto se
 * manejaba desde los JSP)
 *
 *	@version 1.0, Aug 19, 2003
 */
public class IngresoPacienteAction extends Action
{ 

    /**
	 * Objeto de tipo Logger para manejar los logs
	 * que genera esta clase
	 */
    private Logger logger = Logger.getLogger(IngresoPacienteAction.class);
    
   	/**
	 * MÔøΩtodo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception
		{
		Connection con= null;
		try{
			if (form instanceof IngresoPacienteForm)
			{
				if (response==null); //Para evitar que salga el warning
				if( logger.isDebugEnabled() )
				{
					logger.debug("Entro al Action de Egreso");
				}



				IngresoPacienteForm ingresoPacienteForm=(IngresoPacienteForm)form;
				ingresoPacienteForm.setMensaje(new ResultadoBoolean(false));
				String estado=ingresoPacienteForm.getEstado();



				try
				{
					String tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					//No se cierra conexiÔøΩn porque si llega aca ocurriÔøΩ un
					//error al abrirla
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}


				//Lo primero que vamos a hacer es validar que se
				//cumplan las condiciones.

				HttpSession session=request.getSession();
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				UsuarioBasico usuarioActual = (UsuarioBasico)session.getAttribute("usuarioBasico");


				logger.warn("[IngresoPacienteAction]  Estado  ["+ingresoPacienteForm.getEstado()+"] \n\n\n\n\n\n");

				//Primera CondiciÔøΩn: El usuario debe existir

				if( usuarioActual  == null )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
				}
				else
					if (estado==null||estado.equals(""))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion a finalizar no esta definida (IngresoPacienteAction)", "errors.estadoInvalido", true);
					}
				//Nota * El estado decisionIngresoPacienteSistema es cuando se pasa por primera vez a validar el paciente
				//despu√©s de haber ingresado su Tipo id y Numero Id.
				//El estado desicionIngresoPacienteSistemaCuenta sucede cuando ya se ingresÔøΩ un paciente y se desea ingresarCuenta
					else if (estado.equals("decisionIngresoPacienteSistema")||estado.equals("decisionIngresoPacienteSistemaCuenta"))
					{	
						return this.funcionalidadDecisionIngresoPacienteSistema(mapping, ingresoPacienteForm, request, paciente, usuarioActual, con, false,false);
					}			
					else if (estado.equals("anularIngreso")) //estado usado para anular el ingreso
					{
						return accionAnularIngreso(con,ingresoPacienteForm,request,paciente,usuarioActual,mapping);
					}
					else if(estado.equals("validarAutoIngEvento"))
					{
						//la unica forma de ingresar a este estado es que el anterior hubiera sido <<decisionIngresoPacienteSistema>>, 
						//lo actualizamos para que se continue con el flujo una vez ingresado el codigo de autorizacion
						ingresoPacienteForm.setEstado("decisionIngresoPacienteSistema");
						return this.funcionalidadDecisionIngresoPacienteSistema(mapping, ingresoPacienteForm, request, paciente, usuarioActual, con, false,true);
					}
				//********************ESTADOS INVOLUCRADOS CON LA CREACION/MODIFICACION PACIENTE***********************************************
				//Estado usado para guardar la informacion del paciente
					else if(estado.equals("guardarPaciente"))
					{
						return accionGuardarPaciente(con,ingresoPacienteForm,mapping,usuarioActual,paciente,request,response);
					}
				//Estado usado para efectuar una recarga cuando suceden mensajes de validacion donde el usuario debe tomar decisiones
					else if(estado.equals("revisionIngresoPaciente"))
					{
						return accionRevisionIngresoPaciente(con,ingresoPacienteForm,mapping,response);

					}
				//*******************************************************************************************************************************
				//******************ESTADOS INVOLUCRADOS CON LA CREACIÔøΩN DE LA CUENTA ***********************************************************
					else if(estado.equals("ingresarConvenioAdicional"))
					{
						return accionIngresarConvenioAdicional(con,ingresoPacienteForm,mapping,usuarioActual,request,paciente);
					}
					else if(estado.equals("guardarConvenioAdicional"))
					{
						return accionGuardarConvenioAdicional(con,ingresoPacienteForm,mapping,request);
					}
					else if(estado.equals("guardarCuenta"))
					{
						return accionGuardarCuenta(con,ingresoPacienteForm,mapping,request,usuarioActual,paciente);
					}
					else if (estado.equals("imprimirVerificacion"))
					{
						return accionImprimirVerificacion(con,ingresoPacienteForm,mapping,request,usuarioActual);
					}
					else if (estado.equals("imprimirVerificacionOtroConvenio"))
					{
						return accionImprimirVerificacionOtroConvenio(con,ingresoPacienteForm,mapping,request,usuarioActual);
					}
					else if (estado.equals("imprimirAdmision"))
					{
						return accionImprimirAdmision(con,ingresoPacienteForm,mapping,request,usuarioActual);
					}
				//********************************************************************************************************************************
				//******************ESTADOS INVOLUCRADOS CON LA CREACI√ìN DE LA ADMISION HOSPITALIZACION********************************************
					else if(estado.equals("busquedaCama"))
					{
						return accionBusquedaCama(con,ingresoPacienteForm,mapping,paciente,usuarioActual);
					}
					else if (estado.equals("guardarAdmisionHospitalaria"))
					{
						return accionGuardarAdmisionHospitalaria(con,ingresoPacienteForm,mapping,paciente,usuarioActual,request);
					}
				//********************************************************************************************************************************
				//*****************ESTADOS INVOLUCRADOS CON LA SELECCION DE CONVENIOS DEL PACIENTE************************************************
					else if (estado.equals("adicionarConvenioPaciente"))
					{
						return accionAdicionarConvenioPaciente(con,ingresoPacienteForm,mapping,request);
					}
					else if (estado.equals("eliminarConvenioPaciente"))
					{
						return accionEliminarConvenioPaciente(con,ingresoPacienteForm,mapping);
					}
					else if (estado.equals("guardarConvenioPaciente"))
					{

						ActionForward actionTmp =accionGuardarConvenioPaciente(con,ingresoPacienteForm,mapping,request,paciente,usuarioActual); 

						if(ingresoPacienteForm.getCuenta().get("codigoTipoAfiliado")==null){
							ingresoPacienteForm.getCuenta().put("codigoTipoAfiliado", "");
						}


						return actionTmp;
					}
				//*********************************************************************************************************************************
				//*****************ESTADOS INVOLUCRADOS CON LA ELECCION DE PRESUPUESTO****************************************************************
					else if (estado.equals("elegirPresupuesto"))
					{
						return accionElegirPresupuesto(con,mapping,paciente,request);
					}
					else if (estado.equals("actualizarPresupuesto"))
					{
						return accionActualizarPresupuesto(con,ingresoPacienteForm,mapping,paciente,request);
					}
				//**************************************************************************************************************************************

				//*****************ESTADOS INVOLUCRADOS CON EL PREINGRESO****************************************************************
					else if (estado.equals("reabrirPreingreso"))
					{
						paciente.cargarPacienteXingreso(con, ingresoPacienteForm.getCodIngresoPreingreso()+"", usuarioActual.getCodigoInstitucion(), usuarioActual.getCodigoCentroAtencion()+"");
						// CÔøΩdigo necesario para registrar este paciente como Observer
						Observable observable = (Observable) request.getSession().getServletContext().getAttribute("observable");
						if (observable != null) 
						{
							paciente.setObservable(observable);
							// Si ya lo hab√≠amos a√±adido, la siguiente l√≠nea no hace nada
							observable.addObserver(paciente);
						}	

						ingresoPacienteForm.setPreingreso("reabrirPreingreso");
						ingresoPacienteForm.setCuenta("codigoTipoPaciente", paciente.getCodigoTipoPaciente());
						ingresoPacienteForm.setCuenta("preingreso", "generado");
						ingresoPacienteForm.setCuenta("codigoArea", paciente.getCodigoArea());
						ingresoPacienteForm.setCodigoTipoPaciente(paciente.getCodigoTipoPaciente());
						casoViaIngresoHospitalizacion(con, ingresoPacienteForm, request, mapping, usuarioActual, paciente);

						return mapping.findForward("ingresarAdmisionHospitalaria");
					}
					else if (estado.equals("nuevoIngreso"))
					{
						if (ingresoPacienteForm.getPreingreso().equals("alerta"))
							ingresoPacienteForm.setPreingreso("nuevoIngreso");
						return mapping.findForward("ingresarPaciente");
					}
					else if (estado.equals("guardarInfoFaltante"))
					{
						return accionGuardarInfoFaltante(con, ingresoPacienteForm, request, mapping, usuarioActual, paciente);
					}
				//**************************************************************************************************************************************

				//**********************ESTADOS USADOS PARA FILTRO DE CAMPOS (USO DE AJAX O LLAMADO A POPUPS)******************************************
				//1) Filtros para el ingreso del paciente
					else if(estado.equals("filtroCiudadesId")||estado.equals("filtroCiudadesNacimiento")||estado.equals("filtroCiudadesResidencia"))
					{
						return accionFiltroCiudades(con,ingresoPacienteForm,response);
					}
				//2) Filtros para el ingreso de la cuenta
					else if (estado.equals("filtroViaIngreso"))
					{
						return accionFiltroViaIngreso(con,ingresoPacienteForm,usuarioActual,paciente,response);
					}
					else if (estado.equals("filtroConvenio"))
					{
						return accionFiltroConvenio(con,ingresoPacienteForm,usuarioActual,response,paciente);
					}
					else if (estado.equals("filtroFechaAfiliacion"))
					{
						return accionFiltroFechaAfiliacion(con,ingresoPacienteForm,response);
					}
					else if (estado.equals("filtroResponsable"))
					{
						return accionFiltroResponsable(con,ingresoPacienteForm,response,paciente,usuarioActual);
					}
					else if (estado.equals("filtroEdadResponsable"))
					{
						return accionFiltroEdadResponsable(con,ingresoPacienteForm,response,usuarioActual);
					}
					else if (estado.equals("filtroTipoPaciente"))
					{
						return accionFiltroTipoPaciente(con, ingresoPacienteForm, usuarioActual, paciente, response);
					}
				//3) Filtro para el ingreso de convenios
					else if(estado.equals("filtroEliminarConvenio"))
					{
						return accionFiltroEliminarConvenio(con,ingresoPacienteForm,response);
					}
				//******Estado para Mostrar los Tipos de Monitoreo Filtrados por Area
					else if(estado.equals("filtroTipoMonitoreo"))
					{
						return accionFiltroTiposMonitoreo(con,ingresoPacienteForm,response,paciente);
					}
					else if(estado.equals("filtroContrato"))
					{
						return accionFiltroContrato(con,ingresoPacienteForm,response);
					}
					else if(estado.equals("filtroTipoAfiliado"))
					{
						return filtroTipoAfiliado(con,ingresoPacienteForm,usuarioActual,response,paciente);
					}
					else if(estado.equals("filtroNaturalezaPaciente"))
					{
						return filtroNaturalezaPaciente(con,ingresoPacienteForm,usuarioActual,response,paciente);
					}
				//******Estado para Documentos de Garantia
					else if(estado.equals("refrescarPaciente"))
					{
						return accionRefrescarPaciente(con,ingresoPacienteForm,paciente,mapping,usuarioActual);

					}
					else if(estado.equals("cambiarPaisNacimiento") ||
							estado.equals("cambiarPaisExpedicion") || 
							estado.equals("cambiarPaisResidencia")){

						return listarCiudades(ingresoPacienteForm, con, mapping);

					}
				//****************************************

				//*******************************************************************************************************************
					else
					{
						//Si entra ac√° es porque el estado fu√© invalido
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion a finalizar no esta definida (ServiciosAction)", "errors.estadoInvalido", true);
					}
			}
			else
			{
				//Todav√≠a no existe conexiÔøΩn, por eso no se cierra
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
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
	 * M√©todo que realiza validaciones sobre el contrato seleccionado por v√≠a AJAX
	 * @param con
	 * @param ingresoPacienteForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroContrato(Connection con,
			IngresoPacienteForm ingresoPacienteForm,
			HttpServletResponse response) 
	{
		
		ResultadoBoolean resultado = validacionSinContratoControlAnticipos(con, Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoContrato()));
		
		if(ingresoPacienteForm.getCuenta("codigoContrato")!=null){
			verificarMontoCobro(ingresoPacienteForm,ingresoPacienteForm.getCuenta("codigoContrato").toString());
		}
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write("<alerta-contrato>"+resultado.getDescripcion()+"</alerta-contrato>");
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroContrato: "+e);
		}
		return null;
	}


	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private ActionForward filtroTipoAfiliado(Connection con, IngresoPacienteForm formaCuenta, UsuarioBasico usuarioActual, HttpServletResponse response, PersonaBasica paciente) {
		HashMap mapaRespuesta = new HashMap();
		String aux = "", codigoRegimen = "";
		String[] vecAux = new String[0];
		HashMap mapaAux = new HashMap();
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		/**
		 * [0] => codigo convenio
		 * [1] => codigo tipo contrato
		 * [2] => es pyp? (boolean)
		 * [3] => empresaInstitucion
		 */
		String[] datosConvenio = formaCuenta.getCodigoConvenio().split(ConstantesBD.separadorSplit);
		
		vecAux = Utilidades.obtenerTipoRegimenConvenio(con, datosConvenio[0]).split("-");
		codigoRegimen = vecAux[0];
		//se llena la informacion del mapa
		formaCuenta.setCuenta("codigoTipoRegimen", vecAux[0]);
		formaCuenta.setCuenta("nombreTipoRegimen", vecAux[1]);
		
		aux = "<tipo-regimen>";
		aux += "<codigo>"+vecAux[0]+"</codigo><nombre>"+vecAux[1]+"</nombre>";
		aux += "</tipo-regimen>";
		mapaRespuesta.put("respuestaTipoRegimen", aux);
		//********************************************************************************************++
		//*********CONSULTA DE LOS CONTRATOS VIGENTES***********************************************************
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("esConvenioCapitado").toString()))
			formaCuenta.setContratos(UtilidadesManejoPaciente.obtenerContratosVigentesUsuarioCapitado(con, paciente.getCodigoPersona(), Integer.parseInt(datosConvenio[0])));
		else
			formaCuenta.setContratos(Utilidades.obtenerContratos(con, Integer.parseInt(datosConvenio[0]), false, true));
		
		if(formaCuenta.getContratos().size()==1){
			final int unicoContrato=0;
			verificarMontoCobro(formaCuenta,formaCuenta.getContratos().get(unicoContrato).get("codigo").toString());
		}
		
		aux = "<contratos>";
		for(int i=0;i<formaCuenta.getContratos().size();i++)
		{
			mapaAux = (HashMap) formaCuenta.getContratos().get(i);
			aux += "" +
				"<contrato>" +
					"<codigo>"+mapaAux.get("codigo")+"</codigo>" +
					"<numero-contrato>"+mapaAux.get("numerocontrato")+"</numero-contrato>" +
				"</contrato>";
		}
		aux += "</contratos>";
		mapaRespuesta.put("respuestaContratos", aux);
		//*********CONSULTA DE LOS ESTRATOS SOCIALES*****************************************************
		formaCuenta.setEstratosSociales(
				UtilidadesFacturacion.cargarEstratosSociales(con, usuarioActual.getCodigoInstitucionInt(),
						ConstantesBD.acronimoSi, codigoRegimen,Integer.parseInt(datosConvenio[0]),
						Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()+""),
						Utilidades.capturarFechaBD()));
		//**********************************************************************************************
		
		HashMap tiposAfiliado = UtilidadesFacturacion.cargarTiposAfiliadoXEstrato(
				con, usuarioActual.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, 
				Utilidades.convertirAEntero(datosConvenio[0]),Utilidades.convertirAEntero(formaCuenta.getCodigoViaIngreso()),
				Utilidades.convertirAEntero(formaCuenta.getCodigoEstratoSocial()),
				Utilidades.capturarFechaBD());
		
		formaCuenta.setTiposAfiliado(tiposAfiliado);
		
		if (this.cargarDatosContratoCapitado(con, formaCuenta, response, usuarioActual, Integer.parseInt(datosConvenio[1]), codigoRegimen) == null) {
			return null;
		}
		
		//9) Se consultan las naturalezas del paciente
		formaCuenta.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
				con, codigoRegimen, Integer.parseInt(datosConvenio[0]),
				Utilidades.convertirAEntero(formaCuenta.getCodigoViaIngreso()), 
				(formaCuenta.getTiposAfiliado().get("acronimo_0")!= null && !UtilidadTexto.isEmpty(formaCuenta.getTiposAfiliado().get("acronimo_0").toString())) ? formaCuenta.getTiposAfiliado().get("acronimo_0").toString() : "" , 
				Utilidades.convertirAEntero(formaCuenta.getCodigoEstratoSocial()),
				Utilidades.capturarFechaBD()));
		
		aux = "<estratos-sociales>";
		for(int i=0;i<Utilidades.convertirAEntero(formaCuenta.getEstratosSociales("numRegistros").toString());i++)
			aux += "" +
				"<estrato-social>" +
					"<codigo>"+formaCuenta.getEstratosSociales("codigo_"+i)+"</codigo>" +
					"<nombre>"+formaCuenta.getEstratosSociales("descripcion_"+i)+"</nombre>" +
				"</estrato-social>";
		aux += "</estratos-sociales>";
		mapaRespuesta.put("respuestaEstratosSociales", aux);
		aux = "<tipos-afiliado>";
		for(int i=0;i<Utilidades.convertirAEntero(formaCuenta.getTiposAfiliado().get("numRegistros").toString());i++)
			aux += "" +
				"<tipo-afiliado>" +
					"<acronimo>"+formaCuenta.getTiposAfiliado().get("acronimo_"+i)+"</acronimo>" +
					"<nombre>"+formaCuenta.getTiposAfiliado().get("nombre_"+i)+"</nombre>" +
				"</tipo-afiliado>";
		aux += "</tipos-afiliado>";
		mapaRespuesta.put("respuestaTiposAfiliado", aux);

		aux = "<naturalezas-paciente>";
		for(int i=0;i<formaCuenta.getNaturalezasPaciente().size();i++)
			aux += "" +
				"<naturaleza-paciente>" +
					"<codigo>"+ formaCuenta.getNaturalezasPaciente().get(i).getCodigo()+ "</codigo>" +
					"<nombre>"+ formaCuenta.getNaturalezasPaciente().get(i).getNombre()+ "</nombre>" +
				"</naturaleza-paciente>";
		aux += "</naturalezas-paciente>";
		mapaRespuesta.put("respuestaNaturaleza", aux);
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write(mapaRespuesta.get("respuestaEstratosSociales").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaContratos").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaTipoRegimen").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaTiposAfiliado").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaNaturaleza").toString());
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
		}
		return null;
	}

	/**
	 * Solucion MT 4836, se verifica si el convenio NO maneja montos
	 * y si el contrato seleccionado obliga o no al paciente a pagar la atencion 
	 * 
	 * @param ingresoPacienteForm
	 * @author jeilones
	 * @created 11/09/2012
	 */
	private void verificarMontoCobro(IngresoPacienteForm ingresoPacienteForm,String codigoContratoSeleccionado) {
		HashMap<String, Object>contratoSeleccionado=null;
		for(HashMap<String, Object> contrato:ingresoPacienteForm.getContratos()){
			if(contrato.get("codigo")!=null
					&&ingresoPacienteForm.getCuenta("codigoContrato")!=null
					&&contrato.get("codigo").toString().equals(codigoContratoSeleccionado)){
				contratoSeleccionado=contrato;
				break;
			}
		}

		List<HashMap<String, Object>>montosCobro=new ArrayList<HashMap<String,Object>>(0);
		
		if(ingresoPacienteForm.getCuenta("isConvenioManejaMontos")!=null&&
				contratoSeleccionado!=null&&!UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("isConvenioManejaMontos").toString())){
			
			HashMap<String, Object>montoCobro=new HashMap<String, Object>(0);
			
			if(contratoSeleccionado.get("pacientepagaatencion")!=null&&UtilidadTexto.getBoolean(contratoSeleccionado.get("pacientepagaatencion").toString())){
				
				montoCobro.put("codigo", -2);
				montoCobro.put("porcentaje", "100");
				montoCobro.put("valor", "");
				
				montosCobro.add(montoCobro);
				
				//100
			}else{
				montoCobro.put("codigo", -3);
				montoCobro.put("porcentaje", "0");
				montoCobro.put("valor", "");
				
				montosCobro.add(montoCobro);
				//0
			}
			
		}
		
		ingresoPacienteForm.setMontosCobro((ArrayList<HashMap<String, Object>>) montosCobro);
	}



	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private ActionForward filtroNaturalezaPaciente(Connection con, IngresoPacienteForm formaCuenta, UsuarioBasico usuarioActual, HttpServletResponse response, PersonaBasica paciente) {
		HashMap mapaRespuesta = new HashMap();
		String aux = "", codigoRegimen = "";
		String[] vecAux = new String[0];
		HashMap mapaAux = new HashMap();
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		/**
		 * [0] => codigo convenio
		 * [1] => codigo tipo contrato
		 * [2] => es pyp? (boolean)
		 * [3] => empresaInstitucion
		 */
		String[] datosConvenio = formaCuenta.getCodigoConvenio().split(ConstantesBD.separadorSplit);
		
		vecAux = Utilidades.obtenerTipoRegimenConvenio(con, datosConvenio[0]).split("-");
		codigoRegimen = vecAux[0];
		//se llena la informacion del mapa
		formaCuenta.setCuenta("codigoTipoRegimen", vecAux[0]);
		formaCuenta.setCuenta("nombreTipoRegimen", vecAux[1]);
		
		aux = "<tipo-regimen>";
		aux += "<codigo>"+vecAux[0]+"</codigo><nombre>"+vecAux[1]+"</nombre>";
		aux += "</tipo-regimen>";
		mapaRespuesta.put("respuestaTipoRegimen", aux);
		//********************************************************************************************++
		//*********CONSULTA DE LOS CONTRATOS VIGENTES***********************************************************
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("esConvenioCapitado").toString()))
			formaCuenta.setContratos(UtilidadesManejoPaciente.obtenerContratosVigentesUsuarioCapitado(con, paciente.getCodigoPersona(), Integer.parseInt(datosConvenio[0])));
		else
			formaCuenta.setContratos(Utilidades.obtenerContratos(con, Integer.parseInt(datosConvenio[0]), false, true));
		
		if(formaCuenta.getContratos().size()==1){
			final int unicoContrato=0;
			verificarMontoCobro(formaCuenta,formaCuenta.getContratos().get(unicoContrato).get("codigo").toString());
		}
		
		aux = "<contratos>";
		for(int i=0;i<formaCuenta.getContratos().size();i++)
		{
			mapaAux = (HashMap) formaCuenta.getContratos().get(i);
			aux += "" +
				"<contrato>" +
					"<codigo>"+mapaAux.get("codigo")+"</codigo>" +
					"<numero-contrato>"+mapaAux.get("numerocontrato")+"</numero-contrato>" +
				"</contrato>";
		}
		aux += "</contratos>";
		mapaRespuesta.put("respuestaContratos", aux);
		//*********CONSULTA DE LOS ESTRATOS SOCIALES*****************************************************
		formaCuenta.setEstratosSociales(
				UtilidadesFacturacion.cargarEstratosSociales(con, usuarioActual.getCodigoInstitucionInt(),
						ConstantesBD.acronimoSi, codigoRegimen,Integer.parseInt(datosConvenio[0]),
						Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()+""),
						Utilidades.capturarFechaBD()));
		
		//**********************************************************************************************
		
		HashMap tiposAfiliado = UtilidadesFacturacion.cargarTiposAfiliadoXEstrato(
				con, usuarioActual.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, 
				Utilidades.convertirAEntero(datosConvenio[0]),Utilidades.convertirAEntero(formaCuenta.getCodigoViaIngreso()),
				Utilidades.convertirAEntero(formaCuenta.getCodigoEstratoSocial()),
				Utilidades.capturarFechaBD());
		
		formaCuenta.setTiposAfiliado(tiposAfiliado);
		
		if (this.cargarDatosContratoCapitado(con, formaCuenta, response, usuarioActual, Integer.parseInt(datosConvenio[1]), codigoRegimen) == null) {
			return null;
		}
		
		//9) Se consultan las naturalezas del paciente
		formaCuenta.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
				con, codigoRegimen, Integer.parseInt(datosConvenio[0]),
				Utilidades.convertirAEntero(formaCuenta.getCodigoViaIngreso()), 
				formaCuenta.getCodigoTipoAfiliado(), 
				Utilidades.convertirAEntero(formaCuenta.getCodigoEstratoSocial()),
				Utilidades.capturarFechaBD()));
		
		aux = "<estratos-sociales>";
		for(int i=0;i<Utilidades.convertirAEntero(formaCuenta.getEstratosSociales("numRegistros").toString());i++)
			aux += "" +
				"<estrato-social>" +
					"<codigo>"+formaCuenta.getEstratosSociales("codigo_"+i)+"</codigo>" +
					"<nombre>"+formaCuenta.getEstratosSociales("descripcion_"+i)+"</nombre>" +
				"</estrato-social>";
		aux += "</estratos-sociales>";
		mapaRespuesta.put("respuestaEstratosSociales", aux);
		aux = "<tipos-afiliado>";
		for(int i=0;i<Utilidades.convertirAEntero(formaCuenta.getTiposAfiliado().get("numRegistros").toString());i++)
			aux += "" +
				"<tipo-afiliado>" +
					"<acronimo>"+formaCuenta.getTiposAfiliado().get("acronimo_"+i)+"</acronimo>" +
					"<nombre>"+formaCuenta.getTiposAfiliado().get("nombre_"+i)+"</nombre>" +
				"</tipo-afiliado>";
		aux += "</tipos-afiliado>";
		mapaRespuesta.put("respuestaTiposAfiliado", aux);

		aux = "<naturalezas-paciente>";
		for(int i=0;i<formaCuenta.getNaturalezasPaciente().size();i++)
			aux += "" +
				"<naturaleza-paciente>" +
				"<codigo>"+ formaCuenta.getNaturalezasPaciente().get(i).getCodigo()+ "</codigo>" +
				"<nombre>"+ formaCuenta.getNaturalezasPaciente().get(i).getNombre()+ "</nombre>" +
				"</naturaleza-paciente>";
		aux += "</naturalezas-paciente>";
		mapaRespuesta.put("respuestaNaturaleza", aux);
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write(mapaRespuesta.get("respuestaEstratosSociales").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaContratos").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaTipoRegimen").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaTiposAfiliado").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaNaturaleza").toString());
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionGuardarInfoFaltante(Connection con, IngresoPacienteForm ingresoPacienteForm, HttpServletRequest request, ActionMapping mapping, UsuarioBasico usuarioActual, PersonaBasica paciente) {
		ActionErrors errores= new ActionErrors();
		
		//VALIDACION DE LA FECHA
		String fechaIngreso = Utilidades.getFechaIngreso(con, paciente.getCodigoCuenta()+"", ConstantesBD.codigoViaIngresoHospitalizacion);
		String horaIngreso = UtilidadesManejoPaciente.getHoraIngreso(con, paciente.getCodigoCuenta(), ConstantesBD.codigoViaIngresoHospitalizacion);
		logger.info("FECHA INGRESO INICIAL -> "+fechaIngreso+" HORA INGRESO INICIAL -> "+horaIngreso);
		
		if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(ingresoPacienteForm.getHospitalizacion("fecha").toString(), UtilidadFecha.getFechaActual())){
			if(ingresoPacienteForm.getHospitalizacion("fecha").equals(UtilidadFecha.getFechaActual())){
				if(UtilidadFecha.esHoraMenorQueOtraReferencia(UtilidadFecha.getHoraActual(), ingresoPacienteForm.getHospitalizacion("hora")+"")){
					if (!UtilidadFecha.getHoraActual().equals(ingresoPacienteForm.getHospitalizacion("hora")+"")){
						errores.add("",new ActionMessage("errors.horaPosteriorAOtraDeReferencia", "de admisiÔøΩn", "actual"));
					}	
				}	
			}
			if(!UtilidadFecha.esFechaMenorQueOtraReferencia(ingresoPacienteForm.getHospitalizacion("fecha").toString(), fechaIngreso)){
				if(fechaIngreso.equals(ingresoPacienteForm.getHospitalizacion("fecha").toString())){
					if(UtilidadFecha.esHoraMenorQueOtraReferencia(ingresoPacienteForm.getHospitalizacion("hora")+"", horaIngreso))
						errores.add("",new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "de admisiÔøΩn actual", "de la primera admisiÔøΩn"));
				}	
			} else
				errores.add("",new ActionMessage("errors.fechaAnteriorIgualActual", "de admisiÔøΩn actual", "de la primera admisiÔøΩn"));
		}	
		else
			errores.add("",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de admisiÔøΩn", "actual"));
		
		
		// VALIDACION DE LA CAMA
		//if(!formaCuenta.getCama("codigoCama").toString().equals(""))
		if(ingresoPacienteForm.getHospitalizacion("codigoCama") == "" && !ingresoPacienteForm.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
		//if(ingresoPacienteForm.getHospitalizacion("codigoCama") == "")
			errores.add("",new ActionMessage("errors.seleccion","cama"));
		
		
		
		// VALIDACION DEL PROFESIONAL
		if(ingresoPacienteForm.getHospitalizacion("codigoProfesional") == "" && !ingresoPacienteForm.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria) && ingresoPacienteForm.getHospitalizacion("codigoCama") != "")
			errores.add("",new ActionMessage("errors.required","Profesional"));
		
		if (!errores.isEmpty()){
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("ingresarAdmisionHospitalaria");
		}
		
		
		// *************************** ASIGNAR CAMA ************************************
		
		AdmisionHospitalaria admisionHospitalaria = new AdmisionHospitalaria();
		
		// **************************BLOQUEO DE LA CAMA****************************************************
		ArrayList filtro = new ArrayList();
		String codigoCama = ingresoPacienteForm.getHospitalizacion("codigoCama").toString();
		UtilidadBD.iniciarTransaccion(con);
		if (!codigoCama.equals("")){
		
			if(Utilidades.convertirAEntero(codigoCama, true)>0)
			{
				filtro.add(codigoCama);
				UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoCama,filtro);
				if(Utilidades.obtenerCodigoEstadoCama(con,Integer.parseInt(codigoCama))!=ConstantesBD.codigoEstadoCamaDisponible&&
					!codigoCama.equals(ingresoPacienteForm.getHospitalizacion("codigoCamaReserva").toString()))
				{  
	                errores.add("Cama estado diferente", new ActionMessage("error.cama.estadoDiferenteDisponible","SELECCIONADA"));
	                saveErrors(request, errores);
	                UtilidadBD.abortarTransaccion(con);
	                UtilidadBD.closeConnection(con);            
	                return mapping.findForward("ingresarAdmisionHospitalaria"); 
				}
			}
			//****************************************************************************************************
			
			int camaAnterior = Utilidades.getUltimaCamaTraslado(con, paciente.getCodigoCuenta());
			if(camaAnterior==0)
				camaAnterior = ConstantesBD.codigoNuncaValido;
			
			admisionHospitalaria.setCodigoCama(Integer.parseInt(codigoCama));
			admisionHospitalaria.setFecha(ingresoPacienteForm.getHospitalizacion("fecha")+"");
			admisionHospitalaria.setHora(ingresoPacienteForm.getHospitalizacion("hora")+"");
			
			TrasladoCamas objetoTrasladosCamas = new TrasladoCamas();
			
			//si no a habido un ingreso de cama
			if (camaAnterior == ConstantesBD.codigoNuncaValido){
				//************* ACTUALIZACION DE LA ADMISION HOSPITALARIA********************************************
				int exitoAdmisionHospitalaria = 0;
				exitoAdmisionHospitalaria = admisionHospitalaria.cambiarCama(con, Integer.parseInt(codigoCama), paciente.getCodigoCuenta());
				if (exitoAdmisionHospitalaria == 0)
					errores.add("",new ActionMessage("errors.notEspecific","Error al actualizar la admision Hospitalaria"));
			} else {
				try{
					objetoTrasladosCamas.actualizarFechaHoraFinalizacionNoTransaccional(con, paciente.getCodigoCuenta(), UtilidadFecha.conversionFormatoFechaABD(admisionHospitalaria.getFecha()), admisionHospitalaria.getHora(),"");
				} catch(SQLException e){
					logger.info("Error al actualizar la fecha y hora de finalizacion de la cama : "+e);
				}
			}
			
			//*******************INSERTAR EL TRASLADO DE LA CAMA********************************************************
			logger.info("codigo de la cama=> "+admisionHospitalaria.getCodigoCama());
			//Se verifica si se insertÔøΩ la cama
			//if(admisionHospitalaria.getCodigoCama()>0)
			//{
				//se carga la informacion del traslado
				objetoTrasladosCamas.setFechaAsignacion(UtilidadFecha.conversionFormatoFechaABD(admisionHospitalaria.getFecha()));
				objetoTrasladosCamas.setHoraAsignacion(admisionHospitalaria.getHora());
				objetoTrasladosCamas.setInstitucion(usuarioActual.getCodigoInstitucionInt());
				objetoTrasladosCamas.setCodigoPaciente(paciente.getCodigoPersona());
				objetoTrasladosCamas.setCuenta(paciente.getCodigoCuenta());
				objetoTrasladosCamas.setConvenioPaciente(new InfoDatosInt(paciente.getCodigoConvenio()));
				objetoTrasladosCamas.setFechaFinalizacion("");
				objetoTrasladosCamas.setHoraFinalizacion("");
				objetoTrasladosCamas.setCodigoNuevaCama(admisionHospitalaria.getCodigoCama());
				objetoTrasladosCamas.setCodigoCamaAntigua(camaAnterior);
				//se inserta el traslado inicial de la admision hospitalaria
				objetoTrasladosCamas.insertarTrasladoCamaTransaccional(con, usuarioActual.getLoginUsuario(), ConstantesBD.continuarTransaccion);
			//}
			//********************************************************
		}
		//****************************************************************************con*
		
		///CAMBIAMOS EL ESTADO DEL PREINGRESO A GENERADO
		UtilidadesManejoPaciente.actualizarEstadoPreingreso(con, paciente.getCodigoIngreso(), ConstantesIntegridadDominio.acronimoEstadoGenerado, usuarioActual.getLoginUsuario());
		
		//SE ACTUALIZA EL MEDICO EN LA ADMISI√ìN
		if(admisionHospitalaria.actualizarMedico(con, Integer.parseInt(ingresoPacienteForm.getHospitalizacion("codigoProfesional").toString()), paciente.getCodigoCuenta())!=1)
			errores.add("",new ActionMessage("errors.notEspecific","Error al actualizar el mÔøΩdico"));
		
		//RECARGAMOS LA FORMA CON LA NFORMACI√ìN QUE FALTA
		cargarFormResumenCuenta(con, ingresoPacienteForm, paciente.getCodigoCuenta()+"", paciente.getCodigoAdmision(), usuarioActual);
		
		if (!errores.isEmpty()){
			UtilidadBD.closeConnection(con);
			UtilidadBD.abortarTransaccion(con);
			return mapping.findForward("ingresarAdmisionHospitalaria");
		}
		
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenCuenta");
	}



	/**
	 * M√©todo que muestra e resumen de la cuenta desde documentos de garant√≠a
	 * @param con
	 * @param ingresoPacienteForm
	 * @param paciente
	 * @param mapping
	 * @param usuarioActual 
	 * @return
	 */
	private ActionForward accionRefrescarPaciente(Connection con, IngresoPacienteForm ingresoPacienteForm, PersonaBasica paciente, ActionMapping mapping, UsuarioBasico usuarioActual) 
	{
		ingresoPacienteForm.setDeboAbrirAccidentesTransito(false);
		ingresoPacienteForm.setDeboAbrirEventoCatastrofico(false);
		
		cargarFormResumenCuenta(con, ingresoPacienteForm, paciente.getCodigoCuenta()+"", paciente.getCodigoAdmision(),usuarioActual);
		
		
		//***********SE VERIFICA SI SE DEBE ABRIR EL REGISTRO DE ACCIDENTES DE TRANSITO****************+
		if(ingresoPacienteForm.getCuenta("codigoTipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoAccidenteTransito)&&
				Utilidades.tieneRolFuncionalidad(con,usuarioActual.getLoginUsuario(),505))
			ingresoPacienteForm.setDeboAbrirAccidentesTransito(true);
		else
			ingresoPacienteForm.setDeboAbrirAccidentesTransito(false);
		//**********************************************************************************************
		//**********SE VERIFICA SI SE DEBE ABRIR EL REGISTRO DE EVENTOS CATASTROFICOS********************
		if(ingresoPacienteForm.getCuenta("codigoTipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoEventoCatastrofico)&&
				Utilidades.tieneRolFuncionalidad(con,usuarioActual.getLoginUsuario(),555))
			ingresoPacienteForm.setDeboAbrirEventoCatastrofico(true);
		else
			ingresoPacienteForm.setDeboAbrirEventoCatastrofico(false);
		//************************************************************************************************
		
		logger.info("codigoTipoEvento=> "+ingresoPacienteForm.getCuenta("codigoTipoEvento"));
		logger.info("eboAbrirAccdenteTransito=> "+ingresoPacienteForm.isDeboAbrirAccidentesTransito());
		logger.info("deboAbrirEventoCatastrofico=> "+ingresoPacienteForm.isDeboAbrirEventoCatastrofico());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenCuenta");
	}



	/**
	 * M√©todo que realiza la impresion de la admision
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mapping
	 * @param request
	 * @param usuarioActual
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionImprimirAdmision(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual) 
	{
		String nombreRptDesign = "ConsultarAdmision.rptdesign";
		
		InstitucionBasica institucionBasica = new InstitucionBasica();
		int codigoConvenio= Integer.parseInt(ingresoPacienteForm.getCuenta("codigoConvenio")+"");
		institucionBasica.cargar(usuarioActual.getCodigoInstitucionInt(), codigoConvenio);
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucionBasica.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        
        Vector v=new Vector();
        v.add(institucionBasica.getRazonSocial());
        v.add(institucionBasica.getDescripcionTipoIdentificacion()+"         "+institucionBasica.getNit());     
        v.add(institucionBasica.getDireccion());
        v.add("Tels. "+institucionBasica.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(2,1, "HOJA DE INGRESO.");
        //comp.insertLabelInGridPpalOfHeader(2,0, "Fecha AdmisiÔøΩn: "+forma.getCuentaPacienteMap().get("fechaadmision_"+forma.getIndiceIngresoSeleccionado())+"   Hora de AdmisiÔøΩn: "+forma.getCuentaPacienteMap().get("horaadmision_"+forma.getIndiceIngresoSeleccionado()));
        
        comp.obtenerComponentesDataSet("numeroAutorizacion");
        comp.modificarQueryDataSet(ConsultasBirt.consultarNumeroAutorizacion(Utilidades.convertirAEntero(ingresoPacienteForm.getCuenta("idCuenta")+""), ConstantesBD.codigoNuncaValido));
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
        // ************* Impresion LOG
        comp.obtenerComponentesDataSet("documentosGarantia");
        logger.info("SQL / documentosGarantia "+comp.obtenerQueryDataSet()+"\n");
        comp.obtenerComponentesDataSet("entidadResponsable");
        logger.info("SQL / entidadResponsable "+comp.obtenerQueryDataSet()+"\n");
        comp.obtenerComponentesDataSet("ingresoCuenta");
        logger.info("SQL / ingresoCuenta "+comp.obtenerQueryDataSet()+"\n");
        comp.obtenerComponentesDataSet("numeroAutorizacion");
        logger.info("SQL / numeroAutorizacion "+comp.obtenerQueryDataSet()+"\n");
        comp.obtenerComponentesDataSet("paciente");
        logger.info("SQL / paciente "+comp.obtenerQueryDataSet()+"\n");
        comp.obtenerComponentesDataSet("responsablePaciente");
        logger.info("SQL / responsablePaciente "+comp.obtenerQueryDataSet()+"\n");
        
        logger.info("codigoPaciente > "+ingresoPacienteForm.getCuenta("codigoPaciente"));
        logger.info("cuenta > "+ingresoPacienteForm.getCuenta("idCuenta"));
        // ***************************
        
        // Se mandan los parametros al reporte
        newPathReport += 	"&codigoPaciente="+ingresoPacienteForm.getCuenta("codigoPaciente")+
        					"&codigoPaciente2="+ingresoPacienteForm.getCuenta("codigoPaciente")+
        					"&codigoPaciente3="+ingresoPacienteForm.getCuenta("codigoPaciente")+
        					"&cuenta="+ingresoPacienteForm.getCuenta("idCuenta");
        	
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenCuenta");
	}



	/***
	 * M√©todo implementado para realizar el filtro de la edad del responsable
	 * @param con
	 * @param ingresoPacienteForm
	 * @param response
	 * @param usuarioActual
	 * @return
	 */
	private ActionForward accionFiltroEdadResponsable(Connection con, IngresoPacienteForm ingresoPacienteForm, HttpServletResponse response, UsuarioBasico usuarioActual) 
	{
		String edadResponsable = "";
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarEdadResponsablePaciente(usuarioActual.getCodigoInstitucionInt()))&&
			UtilidadFecha.validarFecha(ingresoPacienteForm.getFechaNacimientoResponsable())&&
			UtilidadFecha.esFechaMenorIgualQueOtraReferencia(ingresoPacienteForm.getFechaNacimientoResponsable(), UtilidadFecha.getFechaActual(con)))
		{
			edadResponsable = UtilidadFecha.calcularEdadDetallada(ingresoPacienteForm.getFechaNacimientoResponsable(), UtilidadFecha.getFechaActual(con));
			ingresoPacienteForm.setResponsable("fechaNacimiento", ingresoPacienteForm.getFechaNacimientoResponsable());
		}
		
		ingresoPacienteForm.setResponsable("edadResponsable", edadResponsable);
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write("<edad-responsable>"+edadResponsable+"</edad-responsable>");
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroResponsable: "+e);
		}
		return null;
	}



	/**
	 * M√©todo implementado para realizar la impresion de la verificaciÔøΩn de derechos de otros convenios
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mapping
	 * @param request
	 * @param usuarioActual
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionImprimirVerificacionOtroConvenio(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual) 
	{
		String nombreRptDesign = "VerificacionDerechos.rptdesign";
		
		InstitucionBasica institucionBasica = new InstitucionBasica();
		int codigoConvenio= Integer.parseInt(ingresoPacienteForm.getCuenta("codigoConvenio")+"");
		institucionBasica.cargar(usuarioActual.getCodigoInstitucionInt(), codigoConvenio);
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucionBasica.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(institucionBasica.getRazonSocial());
        v.add(institucionBasica.getTipoIdentificacion()+"         "+institucionBasica.getNit());     
        v.add(institucionBasica.getDireccion());
        v.add("Tels. "+institucionBasica.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        //comp.insertLabelInGridPpalOfHeader(1,0, "");
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        //se mandan los par√°metros al rreporte
        newPathReport += "&idCuenta="+ingresoPacienteForm.getCuenta("idCuenta")+
        	"&idSubCuenta="+ingresoPacienteForm.getVariosConvenios("idSubCuenta_"+ingresoPacienteForm.getPosConvenio());
        	
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenCuenta");
	}



	/**
	 * M√©todo implementado para elegir un prsupuesto, pero antes se valida que el ingreso no tenga presupuestos ingresados previamente
	 * @param con
	 * @param mapping
	 * @param paciente
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private ActionForward accionElegirPresupuesto(Connection con, ActionMapping mapping, PersonaBasica paciente, HttpServletRequest request) 
	{
		//Se verifica que el ingreso no tenga presupuestos ya relacionados
		HashMap datosPresupuesto = PresupuestoPaciente.obtenerPresupuestoXIngreso(con, paciente.getCodigoIngreso()+"");
		
		
		UtilidadBD.closeConnection(con);
		if(Integer.parseInt(datosPresupuesto.get("numRegistros").toString())>0)
		{
			
			
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("errors.notEspecific","El ingreso del paciente ya tiene un presupuesto relacionado con el responsable "+datosPresupuesto.get("convenio")));
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresActionErrorsSinCabezote");
		}
		
		return mapping.findForward("elegirPresupuesto");
	}



	/**
	 * M√©todo implementado para actualizar el ingreso en un presupuesto de un paciente
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mapping
	 * @param paciente
	 * @param request 
	 * @return
	 */
	private ActionForward accionActualizarPresupuesto(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionMapping mapping, PersonaBasica paciente, HttpServletRequest request) 
	{
		//Se toma el consecutivo del presupuesto
		String consecutivoPresupuesto = ingresoPacienteForm.getPresupuestos("consecutivo_"+ingresoPacienteForm.getPosPresupuesto()).toString();
		
		int resp = PresupuestoPaciente.actualizarIngreso(con, paciente.getCodigoIngreso()+"", consecutivoPresupuesto);
		UtilidadBD.closeConnection(con);
		
		if(resp<=0)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACIÔøΩN DEL INGRESO EN EL PRESUPUESTO"));
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresActionErrorsSinCabezote");
		}
		
		return mapping.findForward("elegirPresupuesto");
	}



	/**
	 * M√©todo que realiza la impresiÔøΩn de la verificaciÔøΩn de derechos
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mapping
	 * @param request
	 * @param usuarioActual 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionImprimirVerificacion(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual) 
	{
		String nombreRptDesign = "VerificacionDerechos.rptdesign";
		
		InstitucionBasica institucionBasica = new InstitucionBasica();
		int codigoConvenio= Integer.parseInt(ingresoPacienteForm.getCuenta("codigoConvenio")+"");
		institucionBasica.cargar(usuarioActual.getCodigoInstitucionInt(), codigoConvenio);
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucionBasica.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(institucionBasica.getRazonSocial());
        v.add(institucionBasica.getTipoIdentificacion()+"         "+institucionBasica.getNit());     
        v.add(institucionBasica.getDireccion());
        v.add("Tels. "+institucionBasica.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        //comp.insertLabelInGridPpalOfHeader(1,0, "");
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        //se mandan los par√°metros al rreporte
        newPathReport += "&idCuenta="+ingresoPacienteForm.getCuenta("idCuenta")+
        	"&idSubCuenta="+ingresoPacienteForm.getCuenta("idSubCuenta");
        	
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenCuenta");
	}



	/**
	 * M√©todo implementado para a√±adir los convenios a la estructura actual
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @param usuarioActual
	 * @return
	 */
	private ActionForward accionGuardarConvenioPaciente(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuarioActual) throws IPSException 
	{
		
		int numRegistros = Integer.parseInt(ingresoPacienteForm.getConveniosPostulados("numRegistros").toString());
		
		if(numRegistros>0)
		{
			ingresoPacienteForm.setHuboSeleccionConvenio(true);
			
			//Se limpia la informacion de la cuenta
			ingresoPacienteForm.setCuenta("codigoConvenio","");
			int pos = 0;
			
			for(int i=0;i<numRegistros;i++)
			{
				if(ingresoPacienteForm.getCuenta("codigoConvenio").toString().equals(""))
				{
					//****************ADICION DEL CONVENIO PRINCIPAL**********************************
					//Se postula el convenio principal a la estructura de cuentas
					this.postularInfoConvenioPrincipal(con, ingresoPacienteForm, Integer.parseInt(ingresoPacienteForm.getConveniosPostulados("codigoConvenio_"+i).toString()), "", usuarioActual, paciente);
					//Se agrega la informacion adicional
					ingresoPacienteForm.setCuenta("codigoEstratoSocial", ingresoPacienteForm.getConveniosPostulados("codigoEstratoSocial_"+i));
					ingresoPacienteForm.setCuenta("fechaAfiliacion", ingresoPacienteForm.getConveniosPostulados("fechaAfiliacion_"+i));
					ingresoPacienteForm.setCuenta("semanasCotizacion", ingresoPacienteForm.getConveniosPostulados("semanasCotizacion_"+i));
					//*********************************************************************************
				}
				else
				{
					//************ADICION DEL CONVENIO ADICIONAL*****************************************
					//Se postula el convenio adicional
					this.postularInfoConvenioAdicional(con,ingresoPacienteForm,Integer.parseInt(ingresoPacienteForm.getConveniosPostulados("codigoConvenio_"+i).toString()),usuarioActual,pos,paciente);
					//Se agrega la informacion adicional
					ingresoPacienteForm.setVariosConvenios("codigoEstratoSocial_"+pos, ingresoPacienteForm.getConveniosPostulados("codigoEstratoSocial_"+i));
					ingresoPacienteForm.setVariosConvenios("fechaAfiliacion_"+pos, ingresoPacienteForm.getConveniosPostulados("fechaAfiliacion_"+i));
					ingresoPacienteForm.setVariosConvenios("semanasCotizacion_"+pos, ingresoPacienteForm.getConveniosPostulados("semanasCotizacion_"+i));
					//************************************************************************************
					pos ++;
				}
			} //Fin - For
			
			//Se actualiza la informacion de convenios
			ingresoPacienteForm.setVariosConvenios("numRegistros", pos+"");
			ingresoPacienteForm.setCuenta("numConvenios", pos+"");
		}
		else
			ingresoPacienteForm.setHuboSeleccionConvenio(false);
		
		ingresoPacienteForm.setEstado("decisionIngresoPacienteSistemaCuenta");
		//Si con el flujo normal de la creaciÔøΩn de la cuenta
		return this.funcionalidadDecisionIngresoPacienteSistema(mapping, ingresoPacienteForm, request, paciente, usuarioActual, con, true,false); //entrÔøΩ en seleccion convenios
	}


	/**
	 * M√©todo implementado para postular informacion de un convenio adicional
	 * @param con
	 * @param ingresoPacienteForm
	 * @param codigoConvenio
	 * @param usuarioActual
	 * @param pos
	 * @param paciente 
	 */
	@SuppressWarnings({ "rawtypes" })
	private void postularInfoConvenioAdicional(Connection con, IngresoPacienteForm ingresoPacienteForm, int codigoConvenio, UsuarioBasico usuarioActual, int pos, PersonaBasica paciente) throws IPSException 
	{
		Convenio mundoConvenio = new Convenio();
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		mundoConvenio.cargarResumen(con, codigoConvenio);
		
		//adicion de la informacion que viene de la seleccion
		ingresoPacienteForm.setVariosConvenios("codigoEstratoSocial_"+pos, "");
		
		ingresoPacienteForm.setVariosConvenios("existeConvenio_"+pos, ConstantesBD.acronimoNo); //indicador para el manejo de los convenios que vienen de urgencias
		ingresoPacienteForm.setVariosConvenios("codigoConvenio_"+pos, mundoConvenio.getCodigo());
		ingresoPacienteForm.setVariosConvenios("codigoContrato_"+pos, "");
		ingresoPacienteForm.setVariosConvenios("codigoViaIngreso_"+pos, ""); //se deja vac√≠a
		ingresoPacienteForm.setVariosConvenios("nombreConvenio_"+pos, mundoConvenio.getNombre());
		ingresoPacienteForm.setVariosConvenios("esConvenioPyp_"+pos, UtilidadTexto.getBoolean(mundoConvenio.getPyp())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setVariosConvenios("esConvenioCapitado_"+pos, mundoConvenio.getTipoContrato()==ConstantesBD.codigoTipoContratoCapitado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setVariosConvenios("esConvenioPoliza_"+pos, UtilidadTexto.getBoolean(mundoConvenio.getCheckInfoAdicCuenta())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setVariosConvenios("esConvenioSoat_"+pos, Convenio.esConvenioTipoConventioEventoCatTrans(mundoConvenio.getCodigo())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setVariosConvenios("codigoTipoRegimen_"+pos, mundoConvenio.getTipoRegimen());
		ingresoPacienteForm.setVariosConvenios("nombreTipoRegimen_"+pos, mundoConvenio.getDescripcionTipoRegimen());
		ingresoPacienteForm.setVariosConvenios("codigoNaturaleza_"+pos, ConstantesBD.codigoNaturalezaPacientesNinguno+"");
		ingresoPacienteForm.setVariosConvenios("requiereCarnet_"+pos, mundoConvenio.getRequiereNumeroCarnet());
		ingresoPacienteForm.setVariosConvenios("semanasMinimasCotizacion_"+pos, mundoConvenio.getSemanasMinimasCotizacion());
		ingresoPacienteForm.setVariosConvenios("manejoComplejidad_"+pos, mundoConvenio.getManejaComplejidad());
		ingresoPacienteForm.setVariosConvenios("fechaAfiliacion_"+pos, "");
		ingresoPacienteForm.setVariosConvenios("semanasCotizacion_"+pos, "");
		ingresoPacienteForm.setVariosConvenios("mesesCotizacion_"+pos, "");
		ingresoPacienteForm.setVariosConvenios("autorizacionIngreso_"+pos, "");
		ingresoPacienteForm.setVariosConvenios("numeroPoliza_"+pos, "");
		ingresoPacienteForm.setVariosConvenios("codigoTipoAfiliado_"+pos, "");
		ingresoPacienteForm.setVariosConvenios("esConvenioColsanitas_"+pos, UtilidadesFacturacion.esConvenioColsanitas(con, mundoConvenio.getCodigo(), usuarioActual.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setVariosConvenios("numeroCarnet_"+pos, ingresoPacienteForm.getFichaUsuarioCapitado());
		ingresoPacienteForm.setVariosConvenios("empresasInstitucion_"+pos,mundoConvenio.getEmpresaInstitucion());
		ingresoPacienteForm.setVariosConvenios("esReporteAtencionInicialUrgencias_"+pos,mundoConvenio.getReporte_atencion_ini_urg());
		ingresoPacienteForm.setVariosConvenios("esRequiereAutorizacionServicio_"+pos,UtilidadTexto.getBoolean(mundoConvenio.getRequiere_autorizacion_servicio())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		ingresoPacienteForm.setVariosConvenios("isConvenioManejaMontos",mundoConvenio.isConvenioManejaMontoCobro());
		
		if(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("esConvenioPoliza_"+pos).toString()))
		{
			ingresoPacienteForm.setVariosConvenios("apellidosPoliza_"+pos, paciente.getPrimerApellido()+" "+paciente.getSegundoApellido());
			ingresoPacienteForm.setVariosConvenios("nombresPoliza_"+pos, paciente.getPrimerNombre()+" "+paciente.getSegundoNombre());
			ingresoPacienteForm.setVariosConvenios("tipoIdPoliza_"+pos, paciente.getCodigoTipoIdentificacionPersona());
			ingresoPacienteForm.setVariosConvenios("numeroIdPoliza_"+pos, paciente.getNumeroIdentificacionPersona());
			ingresoPacienteForm.setVariosConvenios("direccionPoliza_"+pos, paciente.getDireccion());
			ingresoPacienteForm.setVariosConvenios("telefonoPoliza_"+pos, paciente.getTelefono());
		}
		
		//******************SE CARGAN LOS REQUISITOS DEL PACIENTE***********************************************
		arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
				con,
				codigoConvenio, 
				ConstantesIntegridadDominio.acronimoIngreso, 
				true, 
				usuarioActual.getCodigoInstitucionInt(),
				Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()));
		ingresoPacienteForm.setVariosConvenios("numReqIngreso_"+pos, arregloAux.size()+"");
			
		for(int j=0;j<arregloAux.size();j++)
		{
			HashMap elemento = (HashMap)arregloAux.get(j);
			ingresoPacienteForm.setVariosConvenios("codigoReqIngreso_"+pos+"_"+j, elemento.get("codigo"));
			ingresoPacienteForm.setVariosConvenios("descripcionReqIngreso_"+pos+"_"+j, elemento.get("descripcion"));
			ingresoPacienteForm.setVariosConvenios("cumplidoReqIngreso_"+pos+"_"+j, ConstantesBD.acronimoNo);
		}
			
		arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
				con,
				codigoConvenio, 
				ConstantesIntegridadDominio.acronimoEgreso, 
				true, 
				usuarioActual.getCodigoInstitucionInt(),
				Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()));
		ingresoPacienteForm.setVariosConvenios("numReqEgreso_"+pos, arregloAux.size()+"");
		for(int j=0;j<arregloAux.size();j++)
		{
			HashMap elemento = (HashMap)arregloAux.get(j);
			ingresoPacienteForm.setVariosConvenios("codigoReqEgreso_"+pos+"_"+j, elemento.get("codigo"));
			ingresoPacienteForm.setVariosConvenios("descripcionReqEgreso_"+pos+"_"+j, elemento.get("descripcion"));
			ingresoPacienteForm.setVariosConvenios("cumplidoReqEgreso_"+pos+"_"+j, ConstantesBD.acronimoNo);
		}
			
		ingresoPacienteForm.setVariosConvenios("tieneRequisitosPaciente_"+pos, 
			(Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numReqIngreso_"+pos).toString())>0||Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numReqEgreso_"+pos).toString())>0)?
				ConstantesBD.acronimoSi:
				ConstantesBD.acronimoNo);
		//**********************************************************************************************************************************
		
		//SE CARGA LOS DATOS POR DEFECTO DE LA VERIFICACION DE DERECHOS**********************************+
		ingresoPacienteForm.setVariosConvenios("personaSolicita_"+pos, usuarioActual.getNombreUsuario());
		ingresoPacienteForm.setVariosConvenios("fechaSolicitud_"+pos, UtilidadFecha.getFechaActual(con));
		ingresoPacienteForm.setVariosConvenios("horaSolicitud_"+pos, UtilidadFecha.getHoraActual(con));
		ingresoPacienteForm.setVariosConvenios("fechaVerificacion_"+pos, UtilidadFecha.getFechaActual(con));
		ingresoPacienteForm.setVariosConvenios("horaVerificacion_"+pos, UtilidadFecha.getHoraActual(con));
		ingresoPacienteForm.setVariosConvenios("tieneVerificacionDerechos_"+pos, ConstantesBD.acronimoNo);
		//*************************************************************************************************
		
	}



	/**
	 * M√©todo implementado para eliminar un convenio del listado de convenios seleccionados para el paciente
	 * antes de crear la cuenta
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarConvenioPaciente(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionMapping mapping) 
	{
		int pos = ingresoPacienteForm.getPosSeleccion();
		int numRegistros = Integer.parseInt(ingresoPacienteForm.getConveniosPostulados("numRegistros").toString());
		
		for(int i=pos;i<(numRegistros-1);i++)
		{
			ingresoPacienteForm.setConveniosPostulados("codigoConvenio_"+i, ingresoPacienteForm.getConveniosPostulados("codigoConvenio_"+(i+1)));
			ingresoPacienteForm.setConveniosPostulados("codigoEstratoSocial_"+i, ingresoPacienteForm.getConveniosPostulados("codigoEstratoSocial_"+(i+1)));
			ingresoPacienteForm.setConveniosPostulados("fechaAfiliacion_"+i, ingresoPacienteForm.getConveniosPostulados("fechaAfiliacion_"+(i+1)));
			ingresoPacienteForm.setConveniosPostulados("nombreConvenio_"+i, ingresoPacienteForm.getConveniosPostulados("nombreConvenio_"+(i+1)));
			ingresoPacienteForm.setConveniosPostulados("codigoTipoRegimen_"+i, ingresoPacienteForm.getConveniosPostulados("codigoTipoRegimen_"+(i+1)));
			ingresoPacienteForm.setConveniosPostulados("nombreTipoRegimen_"+i, ingresoPacienteForm.getConveniosPostulados("nombreTipoRegimen_"+(i+1)));
			ingresoPacienteForm.setConveniosPostulados("esPyp_"+i, ingresoPacienteForm.getConveniosPostulados("esPyp_"+(i+1)));
			ingresoPacienteForm.setConveniosPostulados("esCapitado_"+i, ingresoPacienteForm.getConveniosPostulados("esCapitado_"+(i+1)));
			ingresoPacienteForm.setConveniosPostulados("semanasCotizacion_"+i, ingresoPacienteForm.getConveniosPostulados("semanasCotizacion_"+(i+1)));
			
		}
		
		numRegistros--;
		ingresoPacienteForm.setConveniosPostulados("numRegistros", numRegistros+"");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("conveniosPaciente");
	}



	/**
	 * M√©todo qeu adiciona un nuevo convenio al lilstado de convenios que se puede seleccionar
	 * ante de crear la cuenta
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionAdicionarConvenioPaciente(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionMapping mapping, HttpServletRequest request) 
	{
		int pos = ingresoPacienteForm.getPosSeleccion();
		String codigoConvenio = ingresoPacienteForm.getConveniosPaciente("codigoConvenio_"+pos).toString();
		int numPostulados = Integer.parseInt(ingresoPacienteForm.getConveniosPostulados("numRegistros").toString());
		boolean fueSeleccionado = false;
		
		//*************SE VERIFICA QUE EL CONVENIO NO SE HAYA SELECCIONADO***************************++
		for(int i=0;i<numPostulados;i++)
		{
			if(ingresoPacienteForm.getConveniosPostulados("codigoConvenio_"+i).toString().equals(codigoConvenio))
				fueSeleccionado = true;
		}
		//*********************************************************************************************
		
		if(!fueSeleccionado)
		{
			//**************************SE AGREGA NUEVO CONVENIO*****************************************************
			//Se agrega la informacion del convenio
			ingresoPacienteForm.setConveniosPostulados("codigoConvenio_"+numPostulados, ingresoPacienteForm.getConveniosPaciente("codigoConvenio_"+pos));
			ingresoPacienteForm.setConveniosPostulados("codigoEstratoSocial_"+numPostulados, ingresoPacienteForm.getConveniosPaciente("codigoEstratoSocial_"+pos));
			ingresoPacienteForm.setConveniosPostulados("fechaAfiliacion_"+numPostulados, ingresoPacienteForm.getConveniosPaciente("fechaAfiliacion_"+pos));
			//Se calcula el numero de semanas de cotizacion
			if(!ingresoPacienteForm.getConveniosPostulados("fechaAfiliacion_"+numPostulados).toString().equals(""))
			{
				int numeroDias = UtilidadFecha.numeroDiasEntreFechas(ingresoPacienteForm.getConveniosPostulados("fechaAfiliacion_"+numPostulados).toString(), UtilidadFecha.getFechaActual());
				int numeroSemanas = (numeroDias/7) + (numeroDias%7!=0?1:0);
				ingresoPacienteForm.setConveniosPostulados("semanasCotizacion_"+numPostulados, numeroSemanas+"");
			}
			else
				ingresoPacienteForm.setConveniosPostulados("semanasCotizacion_"+numPostulados, "");
			ingresoPacienteForm.setConveniosPostulados("nombreConvenio_"+numPostulados, ingresoPacienteForm.getConveniosPaciente("nombreConvenio_"+pos));
			ingresoPacienteForm.setConveniosPostulados("codigoTipoRegimen_"+numPostulados, ingresoPacienteForm.getConveniosPaciente("codigoTipoRegimen_"+pos));
			ingresoPacienteForm.setConveniosPostulados("nombreTipoRegimen_"+numPostulados, ingresoPacienteForm.getConveniosPaciente("nombreTipoRegimen_"+pos));
			ingresoPacienteForm.setConveniosPostulados("esPyp_"+numPostulados, ingresoPacienteForm.getConveniosPaciente("esPyp_"+pos));
			ingresoPacienteForm.setConveniosPostulados("esCapitado_"+numPostulados, ingresoPacienteForm.getConveniosPaciente("esCapitado_"+pos));
			ingresoPacienteForm.setConveniosPostulados("empresasInstitucion_"+numPostulados, ingresoPacienteForm.getConveniosPaciente("empresasInstitucion_"+pos));
			
			//Si el convenio es capitado se pone el indicador
			if(UtilidadTexto.getBoolean(ingresoPacienteForm.getConveniosPostulados("esCapitado_"+numPostulados).toString()))
				ingresoPacienteForm.setUsuarioConvenio(true);
			
			numPostulados++;
			ingresoPacienteForm.setConveniosPostulados("numRegistros", numPostulados+"");
			//**************************************************************************************************************************
		}
		else
		{
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("errors.registroExistente","CONVENIO "+ingresoPacienteForm.getConveniosPaciente("nombreConvenio_"+pos)));
			saveErrors(request, errores);
			
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("conveniosPaciente");
	}




	/**
	 * M√©todo implementado que realiza la anulaciÔøΩn del ingreso 
	 * @param con
	 * @param ingresoPacienteForm
	 * @param request
	 * @param paciente
	 * @param usuarioActual
	 * @param mapping
	 * @return
	 */
	private ActionForward accionAnularIngreso(Connection con, IngresoPacienteForm ingresoPacienteForm, HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuarioActual, ActionMapping mapping) throws IPSException 
	{
		ActionErrors errores = new ActionErrors();
		
		UtilidadBD.iniciarTransaccion(con);
		//Realizo la anulacion del ingreso
		boolean exitoAnulacion = IngresoGeneral.anulacionIngresoIncompleto(
			con, 
			ingresoPacienteForm.getIngresoIncompleto("idIngreso").toString(), 
			Integer.parseInt(ingresoPacienteForm.getIngresoIncompleto("codigoViaIngreso").toString()), 
			ingresoPacienteForm.getIngresoIncompleto("idCuenta").toString(), 
			Integer.parseInt(ingresoPacienteForm.getIngresoIncompleto("codigoOrigenAdmision").toString()),
			ingresoPacienteForm.getIngresoIncompleto("hospitalDia").toString(),
			usuarioActual.getLoginUsuario());
		
		ResultadoBoolean resp = this.manejoInterfazTesoreria(con, paciente, Integer.parseInt(ingresoPacienteForm.getIngresoIncompleto("idIngreso").toString()), usuarioActual.getCodigoInstitucionInt(), true, null, usuarioActual);
		
		if(exitoAnulacion&&resp.isTrue())
		{
			UtilidadBD.finalizarTransaccion(con);
			ingresoPacienteForm.setEstado("decisionIngresoPacienteSistema");
			//Si es exitoso prosigo con la creaciÔøΩn de la cuenta
			return this.funcionalidadDecisionIngresoPacienteSistema(mapping, ingresoPacienteForm, request, paciente, usuarioActual, con, false,false);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, resp.getDescripcion(), resp.getDescripcion(), false);
			
		}
		
	}
	/**
	 * M√©todo para guardar la admision de hospitalizacion
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mapping
	 * @param paciente
	 * @param usuarioActual
	 * @param request 
	 * @return
	 */
	private ActionForward accionGuardarAdmisionHospitalaria(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuarioActual, HttpServletRequest request) 
	{
		logger.info("\n entre a accionGuardarAdmisionHospitalaria  portatil.-->"+ingresoPacienteForm.getCuenta("portatil"));
		
		String transplante="";
		transplante=ingresoPacienteForm.getCuenta("transplante")+"";
		
		
		//**********************VALIDACIONES ADMISION HOSPITALARIA*******************************************
		ActionErrors errores = validacionesAdmisionHospitalaria(con,ingresoPacienteForm,paciente,usuarioActual);
		//***************************************************************************************************
		
		String valorConsecutivoIngreso=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoIngresos, usuarioActual.getCodigoInstitucionInt());

		if(valorConsecutivoIngreso==null || valorConsecutivoIngreso.equals(ConstantesBD.codigoNuncaValido+""))
		{
			errores.add("consecutivo",new ActionMessage("errors.consecutivoNoDefinido","de ingreso"));
		}

		if(!errores.isEmpty())
		{
			UtilidadBD.closeConnection(con);
			saveErrors(request, errores);
			return mapping.findForward("ingresarAdmisionHospitalaria");
		}
		
		//************************SE CARGA EL MUNDO**************************************************************
		DtoCuentas dtoCuenta = new DtoCuentas();
		this.cargarMundoGuardarCuenta(con,dtoCuenta,paciente,usuarioActual,ingresoPacienteForm);
		//*******************************************************************************************************
		//*******************VALIDACIONES AL GUARDAR*************************************************************+
		
		
		
		// VERIFICAR SI EL CONVENIO ES EXCENTO DEUDOR 
		
		String excentoDeudor = "";
		
		excentoDeudor = UtilidadesFacturacion.esConvenioExcentoDeudor(con, dtoCuenta.getConvenios()[0].getConvenio().getCodigo(), usuarioActual.getCodigoInstitucionInt());
		
		logger.info(">>>>>> Valor Excento Deudor ->"+excentoDeudor);
		
		if(excentoDeudor.equals(ConstantesBD.acronimoSi))
		{
			logger.info("------ EL CONVENIO SI ES EXCENTO DEUDOR -----");
			ingresoPacienteForm.setRequeridoDeudor(false);
			ingresoPacienteForm.setRequeridoDocumentoGarantias(false);
			
		}
		else
		{
			logger.info("------ EL CONVENIO NO ES EXCENTO DEUDOR -----");
			// Se verifica si es requerido el ingreso de deudor
			for(int i=0;i<dtoCuenta.getConvenios().length;i++)
			{
				ingresoPacienteForm.setRequeridoDeudor(
					UtilidadesManejoPaciente.esRequeridoDeudor(
						con, 
						dtoCuenta.getConvenios()[i].getCodigoTipoRegimen(), 
						paciente.getCodigoTipoIdentificacionPersona(), 
						usuarioActual.getCodigoInstitucionInt()
					)
				);
				if(ingresoPacienteForm.isRequeridoDeudor())
					i = dtoCuenta.getConvenios().length;	
			}
			
			// VALIDAR EL EXCENTO DOCUMENTO GARANTIA POR CONVENIO
			
			String excentoDocGarantia ="";
			
			excentoDocGarantia = UtilidadesFacturacion.esConvenioExcentoDocGarantia(con, dtoCuenta.getConvenios()[0].getConvenio().getCodigo(), usuarioActual.getCodigoInstitucionInt());
			
			logger.info(">>>>>> Valor Excento Documento Garantia ->"+excentoDocGarantia);
			
			if(excentoDocGarantia.equals(ConstantesBD.acronimoSi))
			{
				logger.info("------ EL CONVENIO SI ES EXCENTO DOCUMENTO DE GARANTIA -----");
				ingresoPacienteForm.setRequeridoDocumentoGarantias(false);
			}
			else
			{
				logger.info("------ EL CONVENIO NO ES EXCENTO DOCUMENTO DE GARANTIA -----");
				// Se verifica si es requerido el documento de garant√≠as 
				ingresoPacienteForm.setRequeridoDocumentoGarantias(
						UtilidadesManejoPaciente.esRequeridoDocumentoGarantia(
							con, 
							dtoCuenta.getCodigoViaIngreso(), 
							dtoCuenta.getCodigoTipoPaciente(),
							paciente.getCodigoTipoIdentificacionPersona(), 
							usuarioActual.getCodigoInstitucionInt()
						)
					);
			}
		}
		
		//********************************************************************************************************
		
		
		//Se prepara el ingreso general
		String anioConsecutivoIngreso=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoIngresos, usuarioActual.getCodigoInstitucionInt(),valorConsecutivoIngreso);
		IngresoGeneral ingreso = new IngresoGeneral(
			usuarioActual.getCodigoInstitucion(), 
			paciente,ingresoPacienteForm.isRequeridoDocumentoGarantias()?ConstantesIntegridadDominio.acronimoEstadoIncompletoGarantias:ConstantesIntegridadDominio.acronimoEstadoAbierto,
			usuarioActual.getLoginUsuario(),
			valorConsecutivoIngreso+"",
			anioConsecutivoIngreso,
			usuarioActual.getCodigoCentroAtencion(),
			"","","",transplante); //sin codigo paciente entidad subcontratada
		ingreso.init(System.getProperty("TIPOBD"));
		//*******************************************************************************************************
		
		//************VALIDACIONES PRE-INGRESO***************************************************************************
		logger.info("INGRESO DEL PACIENTE=> "+paciente.getCodigoIngreso());
		//verificar si se puede crear asocio
		boolean esAsocioCuenta=(paciente.getCodigoIngreso()!=0&&UtilidadValidacion.puedoCrearCuentaAsocio(con, paciente.getCodigoIngreso()));
		//se verifica si se puede crear ingreso
		RespuestaValidacion resp1=UtilidadValidacion.validacionIngresarIngreso (con,  ingreso.getCodigoTipoIdentificacionPaciente(), ingreso.getNumeroIdentificacionPaciente(), ingreso.getInstitucion());
		//**********************************************************************************************************************
		
		if(esAsocioCuenta||resp1.puedoSeguir)
		{
			UtilidadBD.iniciarTransaccion(con);
			
			//***********SE INSERTA EL INGRESO *********************************************************************
			int idIngreso=0;
			//Si la cuenta esta asociada no se inserta el ingreso
			if (!esAsocioCuenta)
			{
				idIngreso=ingreso.insertarIngresoTransaccional(con, ConstantesBD.continuarTransaccion);
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoIngresos, usuarioActual.getCodigoInstitucionInt(),valorConsecutivoIngreso, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
				
			}
			else
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoIngresos, usuarioActual.getCodigoInstitucionInt(),valorConsecutivoIngreso, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				idIngreso=paciente.getCodigoIngreso();
			}
			
			logger.info("-----------------------------------------------------------------------------\n----------------------");
			
			//si es un preingreso se inserta el indicador 'PEN'
			if (ingresoPacienteForm.getCuenta().containsKey("preingreso")){
				if (ingresoPacienteForm.getCuenta("preingreso").equals("true"))
					UtilidadesManejoPaciente.actualizarEstadoPreingreso(con, idIngreso, ConstantesIntegridadDominio.acronimoEstadoPendiente, usuarioActual.getLoginUsuario());
			}
				
		    //se a√±ade el ingreso al resto de la estructura
			dtoCuenta.setIdIngreso(idIngreso+"");
			for(int i=0;i<dtoCuenta.getConvenios().length;i++)
				dtoCuenta.getConvenios()[i].setIngreso(idIngreso);
			
			//**********SE INSERTA LA CUENTA************************************************************************
			Cuenta mundoCuenta = new Cuenta(dtoCuenta);
			ResultadoBoolean resp2 = mundoCuenta.guardar(con);
			//Se verifica √©xito inserciÔøΩn
			if(resp2.isTrue())
			{
				String idCuenta = resp2.getDescripcion(); // se toma la cuenta ingresada
				
				//**************************BLOQUEO DE LA CAMA****************************************************
				ArrayList filtro = new ArrayList();
				String codigoCama = ingresoPacienteForm.getHospitalizacion("codigoCama").toString();
				
				if(Utilidades.convertirAEntero(codigoCama, true)>0)
				{
					filtro.add(codigoCama);
					UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoCama,filtro);
					if(Utilidades.obtenerCodigoEstadoCama(con,Integer.parseInt(codigoCama))!=ConstantesBD.codigoEstadoCamaDisponible&&
						!codigoCama.equals(ingresoPacienteForm.getHospitalizacion("codigoCamaReserva").toString()))
					{  
	                    errores.add("Cama estado diferente", new ActionMessage("error.cama.estadoDiferenteDisponible","SELECCIONADA"));
	                    saveErrors(request, errores);
	                    UtilidadBD.abortarTransaccion(con);
	                    UtilidadBD.closeConnection(con);            
	                    return mapping.findForward("ingresarAdmisionHospitalaria"); 
					}
				}
				
				//Si por alguna razon desconocida el paciente tiene una cama ocupada, esta se libera
				Utilidades.liberarUltimaCamaPaciente(con, paciente.getCodigoPersona());
				//****************************************************************************************************
				
				//*********SE MARCAN COMO ATENDIDOS LOS REGISTROS DE PACIENTES TRIAGE PENDIENTES*************************************
				Utilidades.actualizarPacienteParaTriageVencido(con, paciente.getCodigoPersona()+"");
				
				//****************INGRESO DE LA ADMISION HOSPITALARIA************************************************************
				AdmisionHospitalaria admisionHospitalaria = new AdmisionHospitalaria(
					Integer.parseInt(ingresoPacienteForm.getCuenta("codigoOrigenAdmision").toString()), 
					Integer.parseInt(ingresoPacienteForm.getHospitalizacion("codigoProfesional").toString()), 
					"", //numero de identificacion del medico (No se necesita)
					"", //tipo de identificacion del medico (No se necesita) 
					Utilidades.convertirAEntero(ingresoPacienteForm.getHospitalizacion("codigoCama").toString(),true), 
					"1", //Diagnostico no seleccionado (Ya no se est√° ingresando) 
					"0", 
					0, //Causa externa no seleccionada (Ya mp se est√° ingresando)  
					/*ingresoPacienteForm.getHospitalizacion("autorizacion").toString(),*/ 
					usuarioActual, 
					idCuenta, 
					ingresoPacienteForm.getHospitalizacion("hora").toString(), 
					ingresoPacienteForm.getHospitalizacion("fecha").toString());
				//***************************************************************************************************************
				
				//Se realiza la validacion de la admision hospitalaria
				RespuestaValidacion resp3=UtilidadValidacion.validacionIngresarAdmisionHospitalaria(con, paciente.getCodigoPersona(), Integer.parseInt(idCuenta), ingreso.getInstitucion());
				logger.info("¬øPUEDO CONTINUAR CON EL INGRESO DE LA ADMISION? "+resp3.puedoSeguir);
				if (resp3.puedoSeguir)
				{
					//***************LIBERAR CAMA RESERVADA****************************************************
					if(!ingresoPacienteForm.getHospitalizacion("codigoCamaReserva").toString().equals(""))
						UtilidadesManejoPaciente.liberarReservaCama(
							con, 
							ingresoPacienteForm.getHospitalizacion("codigoReserva").toString(), 
							ingresoPacienteForm.getHospitalizacion("codigoCamaReserva").toString(),
							//si la cama reservada fue la seleccionada se cambia el estado de la reserva a ocupada, de lo contrario se pasa el estado a cancelada
							ingresoPacienteForm.getHospitalizacion("codigoCama").toString().equals(ingresoPacienteForm.getHospitalizacion("codigoCamaReserva").toString())?ConstantesIntegridadDominio.acronimoEstadoOcupado:ConstantesIntegridadDominio.acronimoEstadoCancelado);
					//**********************************************************************************************
					
					//*******************INSERTAR ADMISION HOSPITALARIA********************************************************
					//fuera de insertar la admision se cambia el estado de la cama a ocupada
					int codigoAdmision = admisionHospitalaria.insertarTransaccional(con, ConstantesBD.continuarTransaccion,usuarioActual.getCodigoInstitucionInt());
					//*********************************************************************************************************
					logger.info("paso por aqui A CODIGO ADMISION=> "+codigoAdmision);
					if(codigoAdmision>0)
					{
					
						//*******************INSERTAR EL TRASLADO DE LA CAMA********************************************************
						int exitoTrasladoCama = 0;
						logger.info("codigo de la cama=> "+admisionHospitalaria.getCodigoCama());
						//Se verifica si se insertÔøΩ la cama
						if(admisionHospitalaria.getCodigoCama()>0)
						{
						
							TrasladoCamas objetoTrasladosCamas = new TrasladoCamas();
							//se carga la informacion del traslado
							objetoTrasladosCamas.setFechaAsignacion(admisionHospitalaria.getFecha());
							objetoTrasladosCamas.setHoraAsignacion(admisionHospitalaria.getHora());
							objetoTrasladosCamas.setInstitucion(usuarioActual.getCodigoInstitucionInt());
							objetoTrasladosCamas.setCodigoPaciente(paciente.getCodigoPersona());
							objetoTrasladosCamas.setCuenta(admisionHospitalaria.getIdCuenta());
							objetoTrasladosCamas.setConvenioPaciente(new InfoDatosInt(dtoCuenta.getConvenios()[0].getConvenio().getCodigo()));
							objetoTrasladosCamas.setFechaFinalizacion("");
							objetoTrasladosCamas.setHoraFinalizacion("");
							objetoTrasladosCamas.setCodigoNuevaCama(admisionHospitalaria.getCodigoCama());
							objetoTrasladosCamas.setCodigoCamaAntigua(ConstantesBD.codigoNuncaValido);
							//se inserta el traslado inicial de la admision hospitalaria
							exitoTrasladoCama = objetoTrasladosCamas.insertarTrasladoCamaTransaccional(con, usuarioActual.getLoginUsuario(), ConstantesBD.continuarTransaccion);
						}
						else
							exitoTrasladoCama = 1;
						//************************************************************************************************************
					
						
						if(exitoTrasladoCama>0)
						{
							//***************SE INSERTA SOLICITUD DE VALORACION INICIAL***********************************************
							//Se verifica si se insertÔøΩ la admision correctamente
					

							Solicitud solicitud=new Solicitud();
						    // ingresoPacienteForm.getHospitalizacion("autorizacion").toString(),
							solicitud.llenarSolicitudValoracionInicial(new InfoDatosInt(mundoCuenta.getCuenta().getCodigoArea(), "centroCostoSolicitante"), new InfoDatosInt (mundoCuenta.getCuenta().getCodigoArea(), "centroCostoSolicitado"), Integer.parseInt(mundoCuenta.getCuenta().getIdCuenta()), true, new InfoDatosInt (ConstantesBD.codigoTipoSolicitudInicialHospitalizacion,""));
							
							int numeroSolicitud=0;
							try
							{
								numeroSolicitud = solicitud.insertarSolicitudValoracionInicialTransaccional(con, ConstantesBD.continuarTransaccion);
								solicitud.insertarTratanteInicialTransaccional(con, numeroSolicitud,Integer.parseInt(ingresoPacienteForm.getHospitalizacion("codigoProfesional").toString()),mundoCuenta.getCuenta().getCodigoArea(), ConstantesBD.continuarTransaccion);
							}
							catch(Exception e)
							{
								logger.info("Error al insertar la valoracion: "+e);
								errores.add("Error ingresando solicitud valoracion hospitalizacion",new ActionMessage("errors.noSeGraboInformacion","DE LA SOLICITUD VALORACIÔøΩN DE HOSPITALIZACIÔøΩN"));
							}
							//*******************************************************************************************************************
							
							//******************COMPLETAR ASOCIO*******************************************************************************
							//A continuaciÔøΩn revisamos si el paciente est√° en
							//asocio de cuentas y si este es el caso, completamos
							//el asocio . La cuenta se encarga de llamar a Solicitud
							//para que esta actualize sus datos (solicitud, tratante,
							//adjunto)
							
							logger.info("\n\n\n\n\nvalor de existe asocio >> "+paciente.getExisteAsocio());
							
							if (paciente.getExisteAsocio())
							{
								
								int cuentaVieja=mundoCuenta.completarAsocioCuentaTransaccional(
										con, 
										idIngreso+"", 
										Integer.parseInt(idCuenta), 
										Integer.parseInt(ingresoPacienteForm.getHospitalizacion("codigoProfesional").toString()), 
										mundoCuenta.getCuenta().getCodigoArea(), 
										ConstantesBD.continuarTransaccion, 
										usuarioActual.getCodigoInstitucionInt(),
										paciente.getCodigoUltimaViaIngreso()+"",
										UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("existenMedicamentosXDespachar").toString()),
										Utilidades.convertirAEntero(ingresoPacienteForm.getHospitalizacion("codigoFarmacia").toString()));
								
								if(cuentaVieja>0)
								{
									logger.info("valor del codigo de via de ingreso >> "+paciente.getCodigoUltimaViaIngreso()+" >> "+ValoresPorDefecto.getAsignaValoracionCxAmbulaHospita(usuarioActual.getCodigoInstitucionInt())+" >> "+UtilidadTexto.getBoolean(ValoresPorDefecto.getAsignaValoracionCxAmbulaHospita(usuarioActual.getCodigoInstitucionInt())));
										
									if(
											//Verifica que la ultima via de ingreso sea HospitalizaciÔøΩn y si el asocio es Hospitalizacion - Hospitalizado
											//Se valida el par√°metro general ¬øAsigna ValoraciÔøΩn de Urgencias en Hospitalizacion?
											(
													paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoUrgencias&&
													UtilidadTexto.getBoolean(ValoresPorDefecto.getValoracionUrgenciasEnHospitalizacion(usuarioActual.getCodigoInstitucionInt()))
											)
											||
											//Verifica que la ultima via de ingreso sea HospitalizaciÔøΩn y si el asocio es Hospitalizacion - Hospitalizado
											//Se valida el parametro para pasar la ValoraciÔøΩn de Cirugia Ambulatoria a la ValoraciÔøΩn de la HospitalizaciÔøΩn
											(
													paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion&&
													ValoresPorDefecto.getAsignaValoracionCxAmbulaHospita(usuarioActual.getCodigoInstitucionInt()).toString().equals(ConstantesBD.acronimoSi)
											)
										)
									{
									
										//Todo lo anterior hace parte de una transacciÔøΩn diferente,
										//ins abajo NO es transaccional, ejemplo uso comentado
										if(mundoCuenta.copiarValoracionAsocioCuenta(
												con, 
												numeroSolicitud, 
												cuentaVieja, 
												idIngreso, 
												admisionHospitalaria.getFecha(), 
												admisionHospitalaria.getHora(),
												Utilidades.obtenerViaIngresoCuenta(con, cuentaVieja+""),
												paciente,
												usuarioActual)>0)
										{
										
											if(solicitud.cambiarEstadosSolicitudTransaccional(con, numeroSolicitud, ConstantesBD.codigoEstadoFExento, ConstantesBD.codigoEstadoHCRespondida, ConstantesBD.continuarTransaccion).isTrue())
											{
												if(Solicitud.interpretarSolicitud(con, ConstantesBD.textoInterpretacionAutomatica, Integer.parseInt(ingresoPacienteForm.getHospitalizacion("codigoProfesional").toString()), admisionHospitalaria.getFecha(), admisionHospitalaria.getHora(), numeroSolicitud+"")<=0)
													errores.add("",new ActionMessage("errors.problemasGenericos","ingresando la interpretaciÔøΩn de la valoraciÔøΩn"));
												
											}
											else
												errores.add("",new ActionMessage("errors.problemasGenericos","actualizando el estado mÔøΩdico y de facturaciÔøΩn de la valoraciÔøΩn"));
										}
										else
											errores.add("",new ActionMessage("errors.problemasGenericos","al copiar la valoraciÔøΩn  de la cuenta asociada a la cuenta activa"));
									}									
								}
								else
									errores.add("",new ActionMessage("errors.problemasGenericos","al completar el asocio de cuentas"));
									
							}
							//********************************************************************************************************************
							
							//*******SE VERIFICA SI SE ELIGI√ì ORIGEN DE ADMISION REMITIDO*********************************************
							boolean revisionOrigenAdmision = false;
							String numeroReferencia = "";
							if(dtoCuenta.getCodigoOrigenAdmision()==ConstantesBD.codigoOrigenAdmisionHospitalariaEsRemitido)
							{
								//Se verifica si ya hay una referencia externa en tramite para el paciente
								numeroReferencia = UtilidadesHistoriaClinica.getUltimaReferenciaPaciente(con, paciente.getCodigoPersona()+"", ConstantesIntegridadDominio.acronimoExterna, ConstantesIntegridadDominio.acronimoEstadoEnTramite);
								if(!numeroReferencia.equals(""))
								{
									//Si existe se actualiza el estado de la referencia
									revisionOrigenAdmision = UtilidadesHistoriaClinica.actualizarEstadoReferencia(con, numeroReferencia, idIngreso+"",ConstantesIntegridadDominio.acronimoEstadoAdmitido, usuarioActual.getLoginUsuario());
									ingresoPacienteForm.setDeboAbrirReferencia(false);
								}
								else
								{
									//De lo contrario se se√±ala que se debe abrir el formualrio de la referencia despu√©s de crear la cuenta
									revisionOrigenAdmision = true;
									ingresoPacienteForm.setDeboAbrirReferencia(UtilidadTexto.getBoolean(ValoresPorDefecto.getLlamadoAutomaticoReferencia(usuarioActual.getCodigoInstitucionInt())));
								}
							}
							else
								revisionOrigenAdmision = true;
							if(!revisionOrigenAdmision)
							{
								errores.add("Error actualizando el estado de le referencia externa del pacietne",new ActionMessage("error.historiaClinica.referencia.actualizarEstadoReferencia",numeroReferencia));
							}
							//*******************************************************************************************************************
							
							//*********************MANEJO INTERFAZ TESORERIA***************************************************************
							//ResultadoBoolean resp4 = this.manejoInterfazTesoreria(con,paciente,idIngreso,usuarioActual.getCodigoInstitucionInt(),false);
							ResultadoBoolean resp4 = this.manejoInterfazTesoreria(con,paciente,idIngreso,usuarioActual.getCodigoInstitucionInt(),false,dtoCuenta, usuarioActual);
							if(!resp4.isTrue() && UtilidadCadena.noEsVacio(resp4.getDescripcion()))
								errores.add("Error interfaz tesoreria",new ActionMessage("errors.notEspecific","No se grabÔøΩ informaciÔøΩn de la interfaz de tesorerÔøΩa: "+resp4.getDescripcion()));
							
							//**************************************************************************************************************
							
							//*******************ACTUALIZACION DE LA INFORMACION DEL RESPONSABLE EN LA ESTRUCTURA DE DEUDOR******************
							//Si el responsable fue modificado entonces se llama el m√©todo que verifica la informacion del deudor
							if(ingresoPacienteForm.isFueModificadoResponsable())
							{
								if(!DocumentosGarantia.actualizarDatosPersonaDocGarantia(
									con, 
									usuarioActual.getCodigoInstitucionInt(), 
									idIngreso, 
									paciente.getCodigoPersona(), 
									ConstantesIntegridadDominio.acronimoResponsablePaci, 
									mundoCuenta.getCuenta().getResponsablePaciente().getTipoIdentificacion(), 
									mundoCuenta.getCuenta().getResponsablePaciente().getNumeroIdentificacion()))
									errores.add("", new ActionMessage("errors.notEspecific","Problemas al actualizar los registros de deudor por la modificaciÔøΩn del responsable paciente"));
							}
							//***************************************************************************************************************
							
							
							//----------Verificacion e Insercion Ingreso Cuidados Especiales-----------//
							logger.info("\n\nCODIGO TIPO PACIENTE>>>>>>>>>>>>"+ingresoPacienteForm.getCuenta("codigoTipoPaciente").toString());
							if(ingresoPacienteForm.getCuenta("codigoTipoPaciente").toString().equals(ConstantesBD.tipoPacienteHospitalizado))
							{
								
								
								logger.info("\n\nEXISTE ASOCIO>>>>>>>>>>"+paciente.getExisteAsocio());
								if(paciente.getExisteAsocio()&&dtoCuenta.getCodigoTipoMonitoreo()>0)
								{
									logger.info("TIPO DE MONITOREO >>>>>>>>>>>"+dtoCuenta.getCodigoTipoMonitoreo());
									logger.info("INGRESOOOOOOOOOJEEEEEEEEE>>>>>>>>"+idIngreso);
									if(!Utilidades.insertarIngresoCuidadosEspeciales(con, idIngreso, ConstantesIntegridadDominio.acronimoEstadoActivo, ConstantesIntegridadDominio.acronimoAutomatica, dtoCuenta.getCodigoTipoMonitoreo(), usuarioActual.getLoginUsuario(), ingresoPacienteForm.getEvolucion(), ingresoPacienteForm.getValoracion(), mundoCuenta.getCuenta().getCodigoArea()+""))
										errores.add("Error ingresando la admison",new ActionMessage("errors.noSeGraboInformacion","DEL INGRESO A CUIDADOS ESPECIALES"));									
								}
								else
								{
									/*HashMap<String, Object> mapaCE = new HashMap<String, Object> ();
									int tipo=100, tipo2=0, prioridadObtenida;
									mapaCE=Utilidades.consultaTipoMonitoreoxCC(con, mundoCuenta.getCuenta().getCodigoArea());
									for(int v=0;v<Integer.parseInt(mapaCE.get("numRegistros").toString());v++)
									{
										//Puede que la prioridad no est√°
										prioridadObtenida = UtilidadTexto.isEmpty(mapaCE.get("prioridad_"+v)+"")?99:Integer.parseInt(mapaCE.get("prioridad_"+v).toString());
										
										if(prioridadObtenida<tipo)
										{
											tipo=prioridadObtenida;
											tipo2=Integer.parseInt(mapaCE.get("codigo_"+v).toString());
										}
									}
									if(tipo2>0)
									{
										if(!Utilidades.insertarIngresoCuidadosEspeciales(con, idIngreso, ConstantesIntegridadDominio.acronimoEstadoActivo, ConstantesIntegridadDominio.acronimoAutomatica, tipo2, usuarioActual.getLoginUsuario(),0,0))
											errores.add("Error ingresando la admison",new ActionMessage("errors.noSeGraboInformacion","DEL INGRESO A CUIDADOS ESPECIALES"));
									}*/
									if(ingresoPacienteForm.getCuenta().containsKey("codigoTipoMonitoreo") && ingresoPacienteForm.getCuenta().containsKey("requiereTipoM") && ingresoPacienteForm.getCuenta("requiereTipoM").toString().equals(ConstantesBD.acronimoSi))
									{
										if(!Utilidades.insertarIngresoCuidadosEspeciales(con, idIngreso, ConstantesIntegridadDominio.acronimoEstadoActivo, ConstantesIntegridadDominio.acronimoAutomatica, Integer.parseInt(ingresoPacienteForm.getCuenta("codigoTipoMonitoreo").toString()), usuarioActual.getLoginUsuario(),0,0,ingresoPacienteForm.getAreaSel()))
											errores.add("Error ingresando la admison",new ActionMessage("errors.noSeGraboInformacion","DEL INGRESO A CUIDADOS ESPECIALES"));
									}
								}
							}
							
							//AsociaciÔøΩn de la cuenta a autorizaciones que no tienen cuenta asociada
							if(Autorizaciones.asociarCuentaAAutorizacionesSinCuenta(con, Integer.parseInt(idCuenta), idIngreso)<=0)
							{
								errores.add("Error ingresando la admison",new ActionMessage("errors.noSeGraboInformacion","DEL REGISTRO DE AUTORIZACIÔøΩN DE ADMISIÔøΩN"));
							}
							//////----------------------------------------------------------------///////
							
							
							
							
							if(errores.isEmpty())
							{
								//*********SE CARGA LA INFORMACION PARA EL RESUMEN DE LA CUENTA*******************************************************+
								this.cargarFormResumenCuenta(con,ingresoPacienteForm,idCuenta,admisionHospitalaria.getCodigoAdmisionHospitalaria(),usuarioActual);
								//**********************************************************************************************************************
							}
						}
						else
							errores.add("Error ingresando la admison",new ActionMessage("errors.noSeGraboInformacion","DEL TRASLADO DE LA CAMA"));
						
					}
					else
						errores.add("Error ingresando la admison",new ActionMessage("errors.noSeGraboInformacion","DE LA ADMISIÔøΩN DE HOSPITALIZACIÔøΩN"));
					
				}
				else
					errores.add("Error ingresando la admison",new ActionMessage("errors.notEspecific",resp3.textoRespuesta));
					
			}
			else
				errores.add("Error ingresando la admison",new ActionMessage("errors.notEspecific",resp2.getDescripcion()));
			
			if(!errores.isEmpty())
				UtilidadBD.abortarTransaccion(con);
			else				
				UtilidadBD.finalizarTransaccion(con);
			
			
			//********************SE CARGA EL PACIENTE*******************************************
			ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
			try 
			{
				paciente.cargar(con, paciente.getCodigoPersona());
				paciente.cargarPaciente2(con, paciente.getCodigoPersona(), usuarioActual.getCodigoInstitucion(), usuarioActual.getCodigoCentroAtencion()+"");
			} 
			catch (Exception e) 
			{
				logger.info("Error en accionDetalle: "+e);
			}
			observable.addObserver(paciente);
			UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),servlet.getServletContext());
			//***********************************************************************************
		}
		
		
		if(!errores.isEmpty())
		{				
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("ingresarAdmisionHospitalaria");
		}
		
		//***********NO SE DEBE ABRIR LA ASIGNACION DE CITAS (no es consulta externa)******************************
		ingresoPacienteForm.setDeboAbrirAsignacionCitas(false);
		//***********SE VERIFICA SI SE DEBE ABRIR EL REGISTRO DE ACCIDENTES DE TRANSITO****************+
		if(ingresoPacienteForm.getCuenta("codigoTipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoAccidenteTransito)&&
				Utilidades.tieneRolFuncionalidad(con,usuarioActual.getLoginUsuario(),505)&&
				!ingresoPacienteForm.isRequeridoDocumentoGarantias())
			ingresoPacienteForm.setDeboAbrirAccidentesTransito(true);
		else
			ingresoPacienteForm.setDeboAbrirAccidentesTransito(false);
		//**********************************************************************************************
		//**********SE VERIFICA SI SE DEBE ABRIR EL REGISTRO DE EVENTOS CATASTROFICOS********************
		if(ingresoPacienteForm.getCuenta("codigoTipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoEventoCatastrofico)&&
				Utilidades.tieneRolFuncionalidad(con,usuarioActual.getLoginUsuario(),555)&&
				!ingresoPacienteForm.isRequeridoDocumentoGarantias())
			ingresoPacienteForm.setDeboAbrirEventoCatastrofico(true);
		else
			ingresoPacienteForm.setDeboAbrirEventoCatastrofico(false);
		//************************************************************************************************
		//*******SE VERIFICA SI SE DEBE ABRIR LA REFERENCIA*******************************************
		if(ingresoPacienteForm.isDeboAbrirReferencia())
		{
			String pathReferencia =  "../referenciaDummy/referenciaDummy.do?estado=empezarReferencia"+
				"&tipoIdentificacion="+paciente.getCodigoTipoIdentificacionPersona()+"-"+paciente.getTipoIdentificacionPersona(false)+
				"&numeroIdentificacion="+paciente.getNumeroIdentificacionPersona()+
				"&tipoReferencia="+ConstantesIntegridadDominio.acronimoExterna+
				"&deboAbrirAsignacionCita="+ingresoPacienteForm.isDeboAbrirAsignacionCitas();
			ingresoPacienteForm.setPathReferencia(pathReferencia);
		}
		//*******************************************************************************************
		//*******SE PUEDE IMPRIMIR LA ADMISION ************************************************
		if(!ingresoPacienteForm.isRequeridoDocumentoGarantias())
			ingresoPacienteForm.setDeboImprimirAdmision(true);
		else
			ingresoPacienteForm.setDeboImprimirAdmision(false);
		
		//*************SE GENERAN LAS ALERTAS (SI LAS HAY)************************************
		this.generarAlertasResumen(ingresoPacienteForm,request);
		//************************************************************************************
		
		//***********VERIFICACION DE PRESUPUESTOS (VENEZUELA)*********************************************
		if(!ingresoPacienteForm.isRequeridoDocumentoGarantias())
			this.revisarPresupuestos(con, ingresoPacienteForm);
		//************************************************************************************************
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenCuenta");
		
		
		
	}

	/**
	 * Validaciones admision hospitalaria
	 * @param con 
	 * @param ingresoPacienteForm
	 * @param paciente
	 * @param usuarioActual 
	 * @return
	 */
	private ActionErrors validacionesAdmisionHospitalaria(
			Connection con, IngresoPacienteForm ingresoPacienteForm, PersonaBasica paciente, UsuarioBasico usuarioActual) 
	{
		ActionErrors errores = new ActionErrors();
		String aux0 = "", aux1 = "";
		//Validar la fecha de admision
		boolean fechaValida = true;
		String fechaMesAtras = UtilidadFecha.incrementarMesesAFecha(UtilidadFecha.getFechaActual(), -1, false);
		aux0 = ingresoPacienteForm.getHospitalizacion("fecha").toString();
		if(aux0.equals(""))
		{
			errores.add("",new ActionMessage("errors.required","la fecha de admisiÔøΩn"));
			fechaValida = false;
		}
		else
		{
			if(!UtilidadFecha.validarFecha(aux0))
			{
				errores.add("",new ActionMessage("errors.formatoFechaInvalido","de admisiÔøΩn"));
				fechaValida = false;
			}
			else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(aux0, UtilidadFecha.getFechaActual()))
			{
				errores.add("",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de admisiÔøΩn", "actual"));
				fechaValida = false;
			}
			else if(UtilidadFecha.esFechaMenorQueOtraReferencia(aux0, fechaMesAtras))
			{
				errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual","de admisiÔøΩn ("+aux0+")",fechaMesAtras+" (un mes hacia atrÔøΩs de la fecha actual)"));
				fechaValida = false;
			}
			else if(paciente.getExisteAsocio()&&!ingresoPacienteForm.getHospitalizacion("fechaEgreso").toString().equals("")&&UtilidadFecha.esFechaMenorQueOtraReferencia(aux0,ingresoPacienteForm.getHospitalizacion("fechaEgreso").toString()))
			{
				errores.add("",new ActionMessage("errors.fechaAnteriorIgualActual", "de admisiÔøΩn", "de egreso de urgencias ("+ingresoPacienteForm.getHospitalizacion("fechaEgreso").toString()+")"));
				fechaValida = false;
			}
		}
		
		//Validar la hora de admisiÔøΩn
		aux1 = ingresoPacienteForm.getHospitalizacion("hora").toString();
		if(aux1.equals(""))
			errores.add("",new ActionMessage("errors.required","la hora de admisiÔøΩn"));
		else
		{
			if(!UtilidadFecha.validacionHora(aux1).puedoSeguir)
				errores.add("",new ActionMessage("errors.formatoHoraInvalido","de admisiÔøΩn"));
			else if(fechaValida&&!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), aux0, aux1).isTrue())
				errores.add("",new ActionMessage("errors.fechaHoraPosteriorIgualActual", "de admisiÔøΩn", "actual"));
			else if(fechaValida&&!UtilidadFecha.compararFechas(aux0, aux1,fechaMesAtras, UtilidadFecha.getHoraActual()).isTrue())
				errores.add("", new ActionMessage("errors.fechaHoraAnteriorIgualActual","de admisiÔøΩn ("+aux0+" - "+aux1+")",fechaMesAtras+" - "+UtilidadFecha.getHoraActual()+" (un mÔøΩs hacia atrÔøΩs de la fecha actual)"));
			else if(fechaValida&&paciente.getExisteAsocio()&&!ingresoPacienteForm.getHospitalizacion("fechaEgreso").toString().equals("")&&!UtilidadFecha.compararFechas(aux0, aux1,ingresoPacienteForm.getHospitalizacion("fechaEgreso").toString(), ingresoPacienteForm.getHospitalizacion("horaEgreso").toString()).isTrue())
				errores.add("",new ActionMessage("errors.fechaHoraAnteriorIgualActual", "de admisiÔøΩn", "de egreso de urgencias ("+ingresoPacienteForm.getHospitalizacion("fechaEgreso").toString()+" - "+ingresoPacienteForm.getHospitalizacion("horaEgreso").toString()+")"));
				
		}
		if(ingresoPacienteForm.getHospitalizacion("fecha").toString().equals(""))
		{
			
		}
		
		//validacion del m√©dico
		if(ingresoPacienteForm.getHospitalizacion("codigoProfesional").toString().equals(""))
			errores.add("",new ActionMessage("errors.seleccion","Profesional"));
		
		//validacion de la cama
		if (!ingresoPacienteForm.getCuenta("codigoTipoPaciente").equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
			if (!UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("preingreso")+""))
				if(ingresoPacienteForm.getHospitalizacion("codigoCama").toString().equals(""))
					errores.add("",new ActionMessage("errors.seleccion","cama"));
				//se verifica que la cama no se encuentre ocupada
				else if(fechaValida&&UtilidadValidacion.esCamaOcupadaRangoFechaHoraMayorIgualDado(con, UtilidadFecha.conversionFormatoFechaABD(aux0), aux1,  Integer.parseInt(ingresoPacienteForm.getHospitalizacion("codigoCama").toString())))
					errores.add("",new ActionMessage("error.trasladocama.camaOcupada"));
		
		//Validacion de la farmacia si aplica
		if(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("existenMedicamentosXDespachar")+"")&&
			UtilidadTexto.isEmpty(ingresoPacienteForm.getHospitalizacion("codigoFarmacia").toString()))
		{
			if(ingresoPacienteForm.getFarmacias().size()>0)
				errores.add("", new ActionMessage("errors.required","La farmacia"));
			else
				errores.add("", new ActionMessage("errors.noExiste2","parametrizaciÔøΩn de farmacias para el ÔøΩrea "+Utilidades.obtenerNombreCentroCosto(con,Integer.parseInt(ingresoPacienteForm.getCuenta("codigoArea")+""),usuarioActual.getCodigoInstitucionInt())));
		}
		
		return errores;
	}



	/**
	 * M√©todo que realiza el manejo de la interfaz de tesorer√≠a
	 * @param con
	 * @param paciente
	 * @param idIngreso
	 * @param institucion
	 * @param inactivar 
	 * @param dtoCuenta 
	 * @param usuarioActual 
	 * @return
	 */
	private ResultadoBoolean manejoInterfazTesoreria(Connection con, PersonaBasica paciente, int idIngreso, int institucion, boolean inactivar, DtoCuentas dtoCuenta, UsuarioBasico usuarioActual) 
	{
		ResultadoBoolean resp = new ResultadoBoolean(true,"");
		
		logger.info("******************INICIO INTERFAZ TESORERIA************************************");
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazPaciente(institucion)))
		{
			UtilidadBDInterfaz utilidadBD = new UtilidadBDInterfaz();
			
			String consecutivoIngreso = IngresoGeneral.getConsecutivoIngreso(con, idIngreso+"");
			//Si hay a√±o se debe quitar
			if(consecutivoIngreso.contains("-"))
				consecutivoIngreso = consecutivoIngreso.split("-")[0];
			
			//Se verifica si existe registro para el paciente
			DtoInterfazPaciente dto = utilidadBD.cargarPaciente(paciente.getCodigoPersona()+"", institucion);
			
			if (UtilidadTexto.getBoolean(dto.isError()+""))
			{
				return new ResultadoBoolean(false,dto.getMensaje());
			}
			
			dto.setInstitucion(institucion);
			
			logger.info("********INACTIVAR? : "+inactivar);
			logger.info("********EXISTE REGISTRO? : "+dto.getCodigo());
			
			if(!dto.getCodigo().equals(""))
			{
				//Si se va a inactivar solo se cambia el estado
				if(inactivar)
				{
					dto.setEstadoIngreso(ConstantesBDInterfaz.codigoEstadoIngresoInactivoPaciente);
					dto.setUsuario(usuarioActual.getLoginUsuario());
				}
				else
				{
					dto.setIngreso(consecutivoIngreso);
					dto.setEstadoIngreso(ConstantesBDInterfaz.codigoEstadoIngresoActivoPaciente);
					dto.setEstadoRegistro(ConstantesBDInterfaz.codigoEstadoPacienteNoProcesado+"");
					
					// SE carga el sexo del paciente
					if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoMasculino)
					{
						dto.setSexo(ConstantesBDInterfaz.sexoMasculino);
					}	
					else
					{	
						if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoFemenino)
							dto.setSexo(ConstantesBDInterfaz.sexoFemenino);
						else
							dto.setSexo(ConstantesBDInterfaz.sexoAmbos);
					}
					
					// 	Se carga el Login del usuario que modifica el paciente.
					dto.setUsuario(usuarioActual.getLoginUsuario());

					if(dtoCuenta!=null)
					{
						dto.setViaIngreso(dtoCuenta.getCodigoViaIngreso()+"");
						Cuenta mundoCuenta = new Cuenta();
						mundoCuenta.cargar(con, dtoCuenta.getIdCuenta());
						// cargar el codigo del convenio
						dto.setCodconv(String.valueOf(mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo()));
						// cargar el nombre del convenio
						dto.setNomconv(mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getNombre());					
						// se debe realizar una consulta para poder obtener el nit del tercero
						dto.setTercero(mundoCuenta.getCuenta().getConvenios()[0].getNit());
					}
					
				}
					
				//Se modifica el registro de la interfaz de tesoreria
				resp = utilidadBD.modificarPaciente(dto);
			}
			//La inserciÔøΩn solo aplica cuando no se va a inactivar
			else if(!inactivar)
			{
				dto.setCodigo(paciente.getCodigoPersona()+"");
				dto.setTipoIdentificiacion(paciente.getCodigoTipoIdentificacionPersona());
				dto.setNumeroIdentificacion(paciente.getNumeroIdentificacionPersona());
				dto.setPrimerNombre(paciente.getPrimerNombre());
				dto.setSegundoNombre(paciente.getSegundoNombre());
				dto.setPrimerApellido(paciente.getPrimerApellido());
				dto.setSegundoApellido(paciente.getSegundoApellido());
				dto.setFechaNacimiento(paciente.getFechaNacimiento());
				dto.setIngreso(consecutivoIngreso);
				dto.setEstadoIngreso(ConstantesBDInterfaz.codigoEstadoIngresoActivoPaciente);
				dto.setEstadoRegistro(ConstantesBDInterfaz.codigoEstadoPacienteNoProcesado+"");
				
				
				// campos adicionados el miercoles 12 de marzo para la funcionalidad de prestamos de historias clinicas de shaio
				// paciente.getCodigoUltimaViaIngreso()
				// paciente.getCodigoConvenio()
				dto.setNumhc(Integer.parseInt(paciente.getHistoriaClinica()));
				//dto.setCodconv(paciente.getCodigoConvenio()+"");
				
				// Se carga el Codigo de la Ultima Via Ingreso. Agregado el 21 de Agosto de 2008
				dto.setViaIngreso(dtoCuenta.getCodigoViaIngreso()+"");
				
				//SE carga el sexo del paciente
				if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoMasculino)
				{
					dto.setSexo(ConstantesBDInterfaz.sexoMasculino);
				}	
				else
				{	
					if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoFemenino)
						dto.setSexo(ConstantesBDInterfaz.sexoFemenino);
					else
						dto.setSexo(ConstantesBDInterfaz.sexoAmbos);
				}		
				
//			 	Se carga el Login del usuario que modifica el paciente.
				dto.setUsuario(usuarioActual.getLoginUsuario());
				
				// traer el objeto dtoCuentas para poder obtener los valores del convenio y entre otros.
				if (dtoCuenta != null)
				{
					Cuenta mundoCuenta = new Cuenta();
					mundoCuenta.cargar(con, dtoCuenta.getIdCuenta());
					// cargar el codigo del convenio
					dto.setCodconv(String.valueOf(mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo()));
					// cargar el nombre del convenio
					dto.setNomconv(mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getNombre());					
					// se debe realizar una consulta para poder obtener el nit del tercero
					dto.setTercero(mundoCuenta.getCuenta().getConvenios()[0].getNit());
				}

				
				
				//se inserta el registro de la interfaz de tesoreria
				resp = utilidadBD.insertarPaciente(dto);
			}
		}
		logger.info("******************FIN INTERFAZ TESORERIA resultado=>["+resp+"]************************************");
		return resp;
		
	}



	/**
	 * M√©todo implementado para generar las alertas del resumen de la cuenta
	 * @param ingresoPacienteForm
	 * @param request
	 */
	private void generarAlertasResumen(IngresoPacienteForm ingresoPacienteForm, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		//1) VALIDACION DEL DOCUMENTO DE GARANTIA**************************************************
		if(ingresoPacienteForm.isRequeridoDocumentoGarantias())
			errores.add("requerido documento de garantÔøΩas",new ActionMessage("errors.paciente.requeridoIngresoDe","del deudor y documento de garantÔøΩas"));
		
		//2) VALIDACION DEL DEUDOR**************************************************
		if(ingresoPacienteForm.isRequeridoDeudor())
			errores.add("requerido de deudor",new ActionMessage("errors.paciente.requeridoIngresoDe","del deudor"));
		
		//3) VALIDACION DE LAS SEMANAS M√çNIMAS DE COTIZACION ****************************
		for(int i=0;i<ingresoPacienteForm.getAlertasSemanasCotizacion().size();i++)
			errores.add("",new ActionMessage("errors.paciente.semanasMinimasCotizacion",ingresoPacienteForm.getAlertasSemanasCotizacion().get(i).toString()));
		
		if(!errores.isEmpty())
			saveErrors(request, errores);
		
	}




	/**
	 * M√©todo implementado para realizar la b√∫squeda de camas
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mapping
	 * @param usuarioActual 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionBusquedaCama(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuarioActual) 
	{
		
		/*
		 * javrammo
		 * MT 6547
		 * El area seleccionada la esta insertando en el mapa-key ingresoPacienteForm.getCuenta("codigoArea")
		 * pero este llega concatenado con el coidgo del tipo de monitoreo de la siguiente forma: cc.codigo || '-'  || tp.codigo donde
		 * cc es centros_costo y tp es tipo_monitoreo
		 * asi que cuando llega a la consulta puede llegar algo como 330-5 y el SQL lo asume como una resta, es decir que toma como
		 * centro de costo 325.
		 * Por eso es necesario hacer un split a la cadena
		 */
		String areaSeleccionada = ingresoPacienteForm.getCuenta("codigoArea") != null ? ingresoPacienteForm.getCuenta("codigoArea").toString(): null;
		if(areaSeleccionada !=  null && areaSeleccionada.indexOf("-") > 0){
			ingresoPacienteForm.setAreaSel(areaSeleccionada.split("-")[0]);
		}else{
			ingresoPacienteForm.setAreaSel(areaSeleccionada);
		}
		/*
		 *Fin MT 6547
		 */
		
		//Se consultan las camas segun filtro
		ingresoPacienteForm.setCamas(UtilidadesManejoPaciente.obtenerCamas(
			con, 
			usuarioActual.getCodigoInstitucion(), 
			paciente.getCodigoSexo()+"", 
			paciente.getFechaNacimiento(),
			ConstantesBD.codigoEstadoCamaDisponible+"", 
			ingresoPacienteForm.getAreaSel(), 
			usuarioActual.getCodigoCentroAtencion()+"", 
			ConstantesBD.codigoViaIngresoHospitalizacion+"",
			"",
			"",
			"",
			ingresoPacienteForm.getCuenta("codigoTipoPaciente").toString(),
			ConstantesBD.acronimoSi,			
			paciente.getCodigoPersona()+"",
			ConstantesBD.codigoEstadoCamaReservada+"",			
			ConstantesIntegridadDominio.acronimoEstadoActivo+""			
			)
		);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaCamas");
	}




	/**
	 * M√©todo implementado para realizar el filtro del responsable
	 * @param con
	 * @param ingresoPacienteForm
	 * @param response
	 * @param paciente 
	 * @param usuarioActual 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionFiltroResponsable(Connection con, IngresoPacienteForm ingresoPacienteForm, HttpServletResponse response, PersonaBasica paciente, UsuarioBasico usuarioActual) 
	{
		HashMap mapaRespuesta = new HashMap();
		String aux = "", aux0 = "", aux1 = "";
		boolean esPaciente = false;
		
		//1) Se verifica si el responsable es el mismo paciente
		if(ingresoPacienteForm.getCodigoTipoIdResponsable().equals(paciente.getCodigoTipoIdentificacionPersona())&&
			ingresoPacienteForm.getNumeroIdResponsable().equals(paciente.getNumeroIdentificacionPersona()))
		{
			aux+="<error>El paciente no puede ser el responsable. Por favor verifique</error>";
			esPaciente = true;
			ingresoPacienteForm.setResponsable("editable", ConstantesBD.acronimoNo);
		}
		mapaRespuesta.put("respuestaPaciente",aux);
		
		//2) Se verifica si el responsable ya existe
		if(!esPaciente)
		{
			DtoResponsablePaciente responsable = UtilidadesManejoPaciente.cargarResponsablePaciente(con, ingresoPacienteForm.getCodigoTipoIdResponsable(), ingresoPacienteForm.getNumeroIdResponsable());
			
			if(!responsable.getCodigo().equals(""))
			{
				String segundoApellido = "";
				String segundoNombre = "";
				String direccion = "";
				if (responsable.getSegundoApellido() != null && !responsable.getSegundoApellido().equals("null"))
				{
					segundoApellido = responsable.getSegundoApellido();
				}
				if (responsable.getSegundoNombre() != null && !responsable.getSegundoNombre().equals("null"))
				{
					segundoNombre = responsable.getSegundoNombre();
				}
				if (responsable.getDireccion() != null && !responsable.getDireccion().equals("null"))
				{
					direccion = responsable.getDireccion();
				}
				
				ingresoPacienteForm.setResponsable("existe", ConstantesBD.acronimoSi);
				ingresoPacienteForm.setResponsable("editable", ConstantesBD.acronimoSi);
				ingresoPacienteForm.setResponsable("codigo", responsable.getCodigo());
				ingresoPacienteForm.setResponsable("codigoTipoIdentificacion", responsable.getTipoIdentificacion());
				ingresoPacienteForm.setResponsable("numeroIdentificacion", responsable.getNumeroIdentificacion());
				ingresoPacienteForm.setResponsable("paisExpedicion", responsable.getCodigoPaisExpedicion());
				ingresoPacienteForm.setResponsable("ciudadExpedicion", responsable.getCodigoDeptoExpedicion()+ConstantesBD.separadorSplit+responsable.getCodigoCiudadExpedicion());
				ingresoPacienteForm.setResponsable("primerApellido", responsable.getPrimerApellido());
				ingresoPacienteForm.setResponsable("segundoApellido", segundoApellido);
				ingresoPacienteForm.setResponsable("primerNombre", responsable.getPrimerNombre());
				ingresoPacienteForm.setResponsable("segundoNombre", segundoNombre);
				ingresoPacienteForm.setResponsable("direccion", direccion);
				ingresoPacienteForm.setResponsable("pais", responsable.getCodigoPais());
				ingresoPacienteForm.setResponsable("ciudad", responsable.getCodigoDepto()+ConstantesBD.separadorSplit+responsable.getCodigoCiudad());
				ingresoPacienteForm.setResponsable("codigoBarrio", responsable.getCodigoBarrio());
				ingresoPacienteForm.setResponsable("nombreBarrio", responsable.getDescripcionBarrio());
				ingresoPacienteForm.setResponsable("telefono", responsable.getTelefono());
				ingresoPacienteForm.setResponsable("fechaNacimiento", responsable.getFechaNacimiento());
				ingresoPacienteForm.setResponsable("relacionPaciente", responsable.getRelacionPaciente());
				
				if(!ingresoPacienteForm.getResponsable("fechaNacimiento").toString().equals("")&&
					UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarEdadResponsablePaciente(usuarioActual.getCodigoInstitucionInt())))
					ingresoPacienteForm.setResponsable("edadResponsable", UtilidadFecha.calcularEdadDetallada(ingresoPacienteForm.getResponsable("fechaNacimiento").toString(), UtilidadFecha.getFechaActual(con)));
				else
					ingresoPacienteForm.setResponsable("edadResponsable","");
				
				aux = "<datos-responsable>"+
					"<codigo>"+ingresoPacienteForm.getResponsable("codigo")+"</codigo>"+
					"<tipo-identificacion>"+ingresoPacienteForm.getResponsable("codigoTipoIdentificacion")+"</tipo-identificacion>"+
					"<numero-identificacion>"+ingresoPacienteForm.getResponsable("numeroIdentificacion")+"</numero-identificacion>"+
					"<pais-expedicion>"+responsable.getCodigoPaisExpedicion()+"</pais-expedicion>"+
					"<ciudad-expedicion>"+responsable.getCodigoDeptoExpedicion()+ConstantesBD.separadorSplit+responsable.getCodigoCiudadExpedicion()+"</ciudad-expedicion>"+
					"<primer-apellido>"+responsable.getPrimerApellido()+"</primer-apellido>"+
					"<segundo-apellido>"+segundoApellido+"</segundo-apellido>"+
					"<primer-nombre>"+responsable.getPrimerNombre()+"</primer-nombre>"+
					"<segundo-nombre>"+segundoNombre+"</segundo-nombre>"+
					"<direccion>"+direccion+"</direccion>"+
					"<pais>"+responsable.getCodigoPais()+"</pais>"+
					"<ciudad>"+responsable.getCodigoDepto()+ConstantesBD.separadorSplit+responsable.getCodigoCiudad()+"</ciudad>"+
					"<codigo-barrio>"+responsable.getCodigoBarrio()+"</codigo-barrio>"+
					"<nombre-barrio>"+responsable.getDescripcionBarrio()+"</nombre-barrio>"+
					"<telefono>"+responsable.getTelefono()+"</telefono>"+
					"<fecha-nacimiento>"+responsable.getFechaNacimiento()+"</fecha-nacimiento>"+
					"<edad-responsable>"+ingresoPacienteForm.getResponsable("edadResponsable")+"</edad-responsable>"+
					"<relacion-paciente>"+responsable.getRelacionPaciente()+"</relacion-paciente>"+
				"</datos-responsable>";
				
				logger.info("DESCRIPCION RELACION PACIENTE CONSULTADO=> "+responsable.getRelacionPaciente());
				
				//Se cargan las ciudades del pasi de expedicion
				if(!responsable.getCodigoPaisExpedicion().equals(""))
				{
					ingresoPacienteForm.setCiudadesExp(Utilidades.obtenerCiudadesXPais(con, responsable.getCodigoPaisExpedicion()));
					
					aux0 = "<ciudades-expedicion>";
					for(int i=0;i<ingresoPacienteForm.getCiudadesExp().size();i++)
					{
						aux0 += "<ciudad>";
						HashMap elemento = (HashMap)ingresoPacienteForm.getCiudadesExp().get(i);
						aux0 += "<codigo>"+elemento.get("codigoDepartamento")+ConstantesBD.separadorSplit+elemento.get("codigoCiudad")+"</codigo>";
						aux0 += "<nombre>"+elemento.get("nombreCiudad")+" ("+elemento.get("nombreDepartamento")+")</nombre>";
						aux0 += "</ciudad>";
					}
					aux0 += "</ciudades-expedicion>";
				}
				
				
				//Se cargan las ciudades del pais de residencia
				if(!responsable.getCodigoPais().equals(""))
				{
					ingresoPacienteForm.setCiudades(Utilidades.obtenerCiudadesXPais(con, responsable.getCodigoPais()));
					aux1 = "<ciudades-residencia>";
					
					for(int i=0;i<ingresoPacienteForm.getCiudades().size();i++)
					{
						aux1 += "<ciudad>";
						HashMap elemento = (HashMap)ingresoPacienteForm.getCiudades().get(i);
						aux1 += "<codigo>"+elemento.get("codigoDepartamento")+ConstantesBD.separadorSplit+elemento.get("codigoCiudad")+"</codigo>";
						aux1 += "<nombre>"+elemento.get("nombreCiudad")+" ("+elemento.get("nombreDepartamento")+")</nombre>";
						aux1 += "</ciudad>";
					}
					aux1 += "</ciudades-residencia>";
				}
				
			}
			else
			{
				ingresoPacienteForm.setResponsable("existe", ConstantesBD.acronimoNo);
				ingresoPacienteForm.setResponsable("editable", ConstantesBD.acronimoSi);
			}
		}
		mapaRespuesta.put("respuestaResponsable", aux);
		mapaRespuesta.put("respuestaCiudadesExpedicion", aux0);
		mapaRespuesta.put("respuestaCiudadesResidencia", aux1);
		
		aux = "<editable>"+ingresoPacienteForm.getResponsable("editable")+"</editable>"+
			"<existe>"+ingresoPacienteForm.getResponsable("existe")+"</existe>";
		mapaRespuesta.put("respuestaValidacion",aux);
		
		
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write(mapaRespuesta.get("respuestaPaciente").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaResponsable").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaCiudadesExpedicion").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaCiudadesResidencia").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaValidacion").toString());
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroResponsable: "+e);
		}
		return null;
	}


	public synchronized void ingresarCuenta(Connection con, IngresoPacienteForm ingresoPacienteForm, PersonaBasica paciente, DtoCuentas dtoCuenta,UsuarioBasico usuarioActual, IngresoGeneral ingreso,String valorConsecutivoIngreso, ActionErrors errores )
	{
		RespuestaValidacion resp1;
		//Se valida que el paciente no tenga un ingreso abierto
		resp1 = UtilidadValidacion.validacionIngresarIngreso(con, ingreso.getCodigoTipoIdentificacionPaciente(), ingreso.getNumeroIdentificacionPaciente(), ingreso.getInstitucion() );
		if (resp1.puedoSeguir)
		{
				UtilidadBD.iniciarTransaccion(con);
				
				//***********SE INSERTA EL INGRESO *********************************************************************
			    int idIngreso=ingreso.insertarIngresoTransaccional(con, ConstantesBD.continuarTransaccion);
				logger.info("INGRESO RECIEN INSERTADO=> "+idIngreso);
				
			    //se a√±ade el ingreso al resto de la estructura
				dtoCuenta.setIdIngreso(idIngreso+"");
				for(int i=0;i<dtoCuenta.getConvenios().length;i++)
					dtoCuenta.getConvenios()[i].setIngreso(idIngreso);
				
				//************************** si es un preingreso se inserta el indicador 'PEN'
				if (ingresoPacienteForm.getCuenta().containsKey("preingreso")){
					if (ingresoPacienteForm.getCuenta("preingreso").equals("true"))
						UtilidadesManejoPaciente.actualizarEstadoPreingreso(con, idIngreso, ConstantesIntegridadDominio.acronimoEstadoPendiente, usuarioActual.getLoginUsuario());
				}
				
				//**********SE INSERTA LA CUENTA************************************************************************
				Cuenta mundoCuenta = new Cuenta(dtoCuenta);
				ResultadoBoolean resp2 = mundoCuenta.guardar(con);
				//Se verifica √©xito inserciÔøΩn
				if(resp2.isTrue())
				{
		        //Se incrementa el consecutivo de ingreso
				//	UtilidadBD.cambiarUsoFinalizadoConsecutivo(ConstantesBD.nombreConsecutivoIngresos, usuarioActual.getCodigoInstitucionInt(),valorConsecutivoIngreso, ConstantesBD.acronimoSi,ConstantesBD.acronimoSi ); //Se quita Julio 26-2010 TAREA 136415  
					
					//*********SE MARCAN COMO ATENDIDOS LOS REGISTROS DE PACIENTES TRIAGE PENDIENTES*************************************
					Utilidades.actualizarPacienteParaTriageVencido(con, paciente.getCodigoPersona()+"");
					
					//****************INGRESO DE LA ADMISION DE URGENCIAS (SI LA VIA DE INGRESO ES URGENCIAS)*************************
					if(mundoCuenta.getCuenta().getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
					{
						
						errores = insertarAdmisionUrgencias(con,ingresoPacienteForm,mundoCuenta,errores,paciente,ingreso,usuarioActual);
		
					}
					//********INGRESO DE LA ADMISION DE HOSPITALIZACION (SI ES HOSPITALIZACION D√çA)***************************
					else if(mundoCuenta.getCuenta().getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion&&
							mundoCuenta.getCuenta().isHospitalDia() ||ingresoPacienteForm.getCuenta().get("preingreso").equals("true"))
					{
						errores = insertarAdmisionHospitalDia(con,ingresoPacienteForm,mundoCuenta,errores,paciente,ingreso,usuarioActual);
						
					}
					//***********************************************************************************************************
					
				
					//*******SE VERIFICA SI SE ELIGIÔøΩ ORIGEN DE ADMISIÔøΩN REMITIDO*********************************************
					boolean revisionOrigenAdmision = false;
					String numeroReferencia = "";
					if(dtoCuenta.getCodigoOrigenAdmision()==ConstantesBD.codigoOrigenAdmisionHospitalariaEsRemitido)
					{
						//Se verifica si ya hay una referencia externa en tramite para el paciente
						numeroReferencia = UtilidadesHistoriaClinica.getUltimaReferenciaPaciente(con, paciente.getCodigoPersona()+"", ConstantesIntegridadDominio.acronimoExterna, ConstantesIntegridadDominio.acronimoEstadoEnTramite);
						if(!numeroReferencia.equals(""))
						{
							//Si existe se actualiza el estado de la referencia
							revisionOrigenAdmision = UtilidadesHistoriaClinica.actualizarEstadoReferencia(con, numeroReferencia, idIngreso+"",ConstantesIntegridadDominio.acronimoEstadoAdmitido, usuarioActual.getLoginUsuario());
							ingresoPacienteForm.setDeboAbrirReferencia(false);
						}
						else
						{
							//De lo contrario se se√±ala que se debe abrir el formualrio de la referencia despu√©s de crear la cuenta
							revisionOrigenAdmision = true;
							ingresoPacienteForm.setDeboAbrirReferencia(UtilidadTexto.getBoolean(ValoresPorDefecto.getLlamadoAutomaticoReferencia(usuarioActual.getCodigoInstitucionInt())));
						}
					}
					else
						revisionOrigenAdmision = true;
					if(!revisionOrigenAdmision)
					{
						errores.add("Error actualizando el estado de le referencia externa del pacietne",new ActionMessage("error.historiaClinica.referencia.actualizarEstadoReferencia",numeroReferencia));
					}
					//*******************************************************************************************************************
					//*********************MANEJO INTERFAZ TESORERIA***************************************************************
					ResultadoBoolean resp4 = this.manejoInterfazTesoreria(con,paciente,idIngreso,usuarioActual.getCodigoInstitucionInt(),false,dtoCuenta, usuarioActual);
					if(!resp4.isTrue() && UtilidadCadena.noEsVacio(resp4.getDescripcion()))
						errores.add("Error interfaz tesoreria",new ActionMessage("errors.notEspecific","No se grabÔøΩ informaciÔøΩn de la interfaz de tesorerÔøΩa: "+resp4.getDescripcion()));
					//**************************************************************************************************************
					
					//*******************ACTUALIZACION DE LA INFORMACION DEL RESPONSABLE EN LA ESTRUCTURA DE DEUDOR******************
					//Si el responsable fue modificado entonces se llama el m√©todo que verifica la informacion del deudor
					if(ingresoPacienteForm.isFueModificadoResponsable())
					{
						if(!DocumentosGarantia.actualizarDatosPersonaDocGarantia(
							con, 
							usuarioActual.getCodigoInstitucionInt(), 
							idIngreso, 
							paciente.getCodigoPersona(), 
							ConstantesIntegridadDominio.acronimoResponsablePaci, 
							mundoCuenta.getCuenta().getResponsablePaciente().getTipoIdentificacion(), 
							mundoCuenta.getCuenta().getResponsablePaciente().getNumeroIdentificacion()))
							errores.add("", new ActionMessage("errors.notEspecific","Problemas al actualizar los registros de deudor por la modificaciÔøΩn del responsable paciente"));
					}
					//***************************************************************************************************************
					/*if(errores.isEmpty())
					{
						//**************SE CARGA DE NUEVO LA INFORMACION DE LA CUENTA (para mostrar el resumen)************************
						//this.cargarFormResumenCuenta(con,ingresoPacienteForm,resp2.getDescripcion(),0,usuarioActual);
						//***********************************************************************************
					    
					}*/
				}
				else
				{					
					errores.add("Error ingresando la cuenta", new ActionMessage("errors.notEspecific",resp2.getDescripcion()));
				}
				
			
		
				if(!errores.isEmpty())
				{	
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoIngresos, usuarioActual.getCodigoInstitucionInt(),valorConsecutivoIngreso, ConstantesBD.acronimoNo,ConstantesBD.acronimoNo );
					UtilidadBD.abortarTransaccion(con);
				}	
				else
				{
					//Se incrementa el consecutivo de ingreso //Se pone Julio 26-2010 TAREA 136415
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoIngresos, usuarioActual.getCodigoInstitucionInt(),valorConsecutivoIngreso, ConstantesBD.acronimoSi,ConstantesBD.acronimoSi );
					UtilidadBD.finalizarTransaccion(con);
					//**************SE CARGA DE NUEVO LA INFORMACION DE LA CUENTA (para mostrar el resumen)************************
					this.cargarFormResumenCuenta(con,ingresoPacienteForm,resp2.getDescripcion(),0,usuarioActual);
					//***********************************************************************************
				}
				
				//********************SE CARGA EL PACIENTE*******************************************
				ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
				try 
				{
					paciente.cargar(con, paciente.getCodigoPersona());
					paciente.cargarPaciente2(con, paciente.getCodigoPersona(), usuarioActual.getCodigoInstitucion(), usuarioActual.getCodigoCentroAtencion()+"");
				} 
				catch (Exception e) 
				{
					logger.info("Error en accionDetalle: "+e);
					e.printStackTrace();
				}
				observable.addObserver(paciente);
				UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),servlet.getServletContext());
				//***********************************************************************************
		}
		else
			errores.add("Paciente ya tiene ingreso",new ActionMessage("errors.notEspecific",resp1.textoRespuesta));
	}

	/**
	 * M√©todo implementado para guardar los datos de la cuenta
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mapping
	 * @param request
	 * @param paciente 
	 * @param usuarioActual 
	 * @return
	 */
	private ActionForward accionGuardarCuenta(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual, PersonaBasica paciente) 
	{
		
		
		String transplante="";
		transplante=ingresoPacienteForm.getCuenta("transplante")+"";
		
		//En este caso el paciente si debe estar cargado
		if( paciente == null || paciente.getCodigoTipoIdentificacionPersona().equals("") )
		{
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
		}
		
		ActionErrors errores = new ActionErrors();
		//****************VALIDACIONES DE CAMPOS*****************************************************
		errores = this.validacionGuardarCuenta(con,ingresoPacienteForm,errores,usuarioActual);
		//**********************************************************************************************************************
		if(!errores.isEmpty())
		{
			UtilidadBD.closeConnection(con);
			saveErrors(request, errores);
			return mapping.findForward("ingresarCuenta");
		}
		
		
		/**
	     * Verificacion del campo Origen Admision:
	     * Si el campo origen admision es "Remitido" y no se tiene definido el par√°metro "Horas de Caducidad de referencias externas"
	     * se cancela el proceso
	     */
		String horasCaducidad = ValoresPorDefecto.getHorasCaducidadReferenciasExternas(usuarioActual.getCodigoInstitucionInt());
		
		if(Integer.parseInt(ingresoPacienteForm.getCuenta("codigoOrigenAdmision").toString())==ConstantesBD.codigoOrigenAdmisionHospitalariaEsRemitido)
		{
			if(horasCaducidad==null||horasCaducidad.equals(""))
			{
				errores.add("Horas de caducidad referencia externa",new ActionMessage("error.historiaClinica.referencia.faltaDefinirHorasCaducidad"));
				saveErrors(request, errores);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaErroresActionErrors");
			}
			else
				///Se realiza la anulacion de las referencias externas caducadas
				Referencia.anularReferenciasExternasCaducadas(con, paciente.getCodigoPersona()+"", horasCaducidad, usuarioActual.getLoginUsuario());
		}
		
		
		int viaIngreso=Utilidades.convertirAEntero((String)ingresoPacienteForm.getCuenta("codigoViaIngreso"));
		logger.info("viaIngreso "+viaIngreso);
		
		double saldoPaciente=UtilidadValidacion.consultarSaldoPaciente(con, paciente);
		boolean actualizarAutorizacionSaldoPendiente=false;
		if(saldoPaciente>0)
		{
			String numDiasMoraStr=ValoresPorDefecto.getDiasParaDefinirMoraXDeudaPacientes(usuarioActual.getCodigoInstitucionInt());
			if(numDiasMoraStr!=null && !numDiasMoraStr.trim().equals(""))
			{
				int numeroDiasMora=Utilidades.convertirAEntero(numDiasMoraStr);
				if(UtilidadValidacion.tieneCuotasPendientes(con, paciente, numeroDiasMora))
				{
					paciente.setCodigoTipoPaciente(ingresoPacienteForm.getCuenta("codigoTipoPaciente").toString());
					if(UtilidadValidacion.verificarBloqueoIngresoPacienteODeudor(con, paciente, viaIngreso))
					{
						if(!UtilidadValidacion.tieneAutorizacionIngresoSaldoPendiente(con, paciente, viaIngreso, paciente.getCodigoTipoPaciente()))
						{
							DtoTiposMoneda tipoMoneda=UtilConversionMonedas.obtenerTipoMonedaManejaInstitucion(usuarioActual.getCodigoInstitucionInt());
							String mensaje=getResources(request).getMessage("errors.ingreso.deudaPendiente", tipoMoneda.getSimbolo()+" "+saldoPaciente);
							ingresoPacienteForm.setMensajeSaldoPendiente(mensaje);
							errores.add("mensaje error", new ActionMessage("errors.ingreso.noAutorizacion"));
						}
						else
						{
							actualizarAutorizacionSaldoPendiente=true;
						}
					}
				}
			}
		}
		
		
		//*******************************************************************************************************
		
		//Segun la vÔøΩa de ingreso se toma la decisiÔøΩn respectiva
		if(ingresoPacienteForm.getCuenta("codigoViaIngreso").toString().equals(ConstantesBD.codigoViaIngresoAmbulatorios+"")||
			ingresoPacienteForm.getCuenta("codigoViaIngreso").toString().equals(ConstantesBD.codigoViaIngresoConsultaExterna+"")||
			ingresoPacienteForm.getCuenta("codigoViaIngreso").toString().equals(ConstantesBD.codigoViaIngresoUrgencias+"")||
			//AquÔøΩ se toma en cuenta el flujo de hospital dÔøΩa
			(
				ingresoPacienteForm.getCuenta("codigoViaIngreso").toString().equals(ConstantesBD.codigoViaIngresoHospitalizacion+"")&&
				(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("hospitalDia")+""))
			)
			//flujo de preingreso
				||ingresoPacienteForm.getCuenta().get("preingreso").equals("true")
			)
		{
			//************************SE CARGA EL MUNDO**************************************************************
			DtoCuentas dtoCuenta = new DtoCuentas();
			this.cargarMundoGuardarCuenta(con,dtoCuenta,paciente,usuarioActual,ingresoPacienteForm);
			//*******************************************************************************************************
			//*******************VALIDACIONES AL GUARDAR*************************************************************+
			
			
			
			//	VERIFICAR SI EL CONVENIO ES EXCENTO DEUDOR 
			
			String excentoDeudor = "";
			
			excentoDeudor = UtilidadesFacturacion.esConvenioExcentoDeudor(con, dtoCuenta.getConvenios()[0].getConvenio().getCodigo(), usuarioActual.getCodigoInstitucionInt());
			
			logger.info(">>>>>> Valor Excento Deudor ->"+excentoDeudor);
			
			if(excentoDeudor.equals(ConstantesBD.acronimoSi))
			{
				logger.info("------ EL CONVENIO SI ES EXCENTO DEUDOR -----");
				ingresoPacienteForm.setRequeridoDeudor(false);
				ingresoPacienteForm.setRequeridoDocumentoGarantias(false);
				
			}
			else
			{
				logger.info("------ EL CONVENIO NO ES EXCENTO DEUDOR -----");
				// Se verifica si es requerido el ingreso de deudor
				for(int i=0;i<dtoCuenta.getConvenios().length;i++)
				{
					ingresoPacienteForm.setRequeridoDeudor(
						UtilidadesManejoPaciente.esRequeridoDeudor(
							con, 
							dtoCuenta.getConvenios()[i].getCodigoTipoRegimen(), 
							paciente.getCodigoTipoIdentificacionPersona(), 
							usuarioActual.getCodigoInstitucionInt()
						)
					);
					if(ingresoPacienteForm.isRequeridoDeudor())
						i = dtoCuenta.getConvenios().length;	
				}
				
				// VALIDAR EL EXCENTO DOCUMENTO GARANTIA POR CONVENIO
				
				String excentoDocGarantia ="";
				
				excentoDocGarantia = UtilidadesFacturacion.esConvenioExcentoDocGarantia(con, dtoCuenta.getConvenios()[0].getConvenio().getCodigo(), usuarioActual.getCodigoInstitucionInt());
				
				logger.info(">>>>>> Valor Excento Documento Garantia ->"+excentoDocGarantia);
				
				if(excentoDocGarantia.equals(ConstantesBD.acronimoSi))
				{
					logger.info("------ EL CONVENIO SI ES EXCENTO DOCUMENTO DE GARANTIA -----");
					ingresoPacienteForm.setRequeridoDocumentoGarantias(false);
				}
				else
				{
					logger.info("------ EL CONVENIO NO ES EXCENTO DOCUMENTO DE GARANTIA -----");
					// Se verifica si es requerido el documento de garant√≠as 
					ingresoPacienteForm.setRequeridoDocumentoGarantias(
							UtilidadesManejoPaciente.esRequeridoDocumentoGarantia(
								con, 
								dtoCuenta.getCodigoViaIngreso(), 
								dtoCuenta.getCodigoTipoPaciente(),
								paciente.getCodigoTipoIdentificacionPersona(), 
								usuarioActual.getCodigoInstitucionInt()
							)
						);
				}
			}
			//********************************************************************************************************
			
			//Se prepara el ingreso general
			String valorConsecutivoIngreso=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoIngresos, usuarioActual.getCodigoInstitucionInt());
			String anioConsecutivoIngreso=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoIngresos, usuarioActual.getCodigoInstitucionInt(),valorConsecutivoIngreso);
			
			IngresoGeneral ingreso = new IngresoGeneral(
				usuarioActual.getCodigoInstitucion(), 
				paciente,ingresoPacienteForm.isRequeridoDocumentoGarantias()?ConstantesIntegridadDominio.acronimoEstadoIncompletoGarantias:ConstantesIntegridadDominio.acronimoEstadoAbierto,
				usuarioActual.getLoginUsuario(),
				valorConsecutivoIngreso,
				anioConsecutivoIngreso,
				usuarioActual.getCodigoCentroAtencion(),
				"","","",transplante); //sin codigo paciente entidad subcontratada
			ingreso.init(System.getProperty("TIPOBD"));
			
			//******************PROCESO DE GUARDAR CUENTA PARA AMBULATORIOS, CONSULTA EXTERNA, URGENCIAS**************************************
			
			this.ingresarCuenta(con, ingresoPacienteForm, paciente, dtoCuenta, usuarioActual, ingreso, valorConsecutivoIngreso, errores);
			
			if(!errores.isEmpty())
			{
				saveErrors(request, errores);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("ingresarCuenta");
			}
			
			if(actualizarAutorizacionSaldoPendiente)
			{
				DtoAutorizacionSaldoMora dtoAutorizacionSaldoMora=new DtoAutorizacionSaldoMora();
				AutorizacionIngresoPacienteSaldoMora.actualizarDatosAutoricacion(dtoAutorizacionSaldoMora, paciente);
			}
			
			//***********SE VERIFICA SI SE DEBE ABRIR LA ASIGNACION DE CITAS******************************
			String funcionalidad = UtilidadValidacion.funcionalidadADibujarNoEntradaDependencias(con,usuarioActual.getLoginUsuario(),46);
			if(Integer.parseInt(ingresoPacienteForm.getCuenta("codigoViaIngreso").toString())==ConstantesBD.codigoViaIngresoConsultaExterna&&
				funcionalidad.length()>0&&
				!ingresoPacienteForm.isRequeridoDocumentoGarantias())
			{
				ingresoPacienteForm.setPathAsignacionCitas("../" + funcionalidad.split(ConstantesBD.separadorSplit)[1]);
				ingresoPacienteForm.setDeboAbrirAsignacionCitas(true);
			}
			else
				ingresoPacienteForm.setDeboAbrirAsignacionCitas(false);
			//***********SE VERIFICA SI SE DEBE ABRIR EL REGISTRO DE ACCIDENTES DE TRANSITO****************+
			if(ingresoPacienteForm.getCuenta("codigoTipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoAccidenteTransito)&&
					Utilidades.tieneRolFuncionalidad(con,usuarioActual.getLoginUsuario(),505)&&
					!ingresoPacienteForm.isRequeridoDocumentoGarantias())
				ingresoPacienteForm.setDeboAbrirAccidentesTransito(true);
			else
				ingresoPacienteForm.setDeboAbrirAccidentesTransito(false);
			//**********************************************************************************************
			//**********SE VERIFICA SI SE DEBE ABRIR EL REGISTRO DE EVENTOS CATASTROFICOS********************
			if(ingresoPacienteForm.getCuenta("codigoTipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoEventoCatastrofico)&&
					Utilidades.tieneRolFuncionalidad(con,usuarioActual.getLoginUsuario(),555)&&
					!ingresoPacienteForm.isRequeridoDocumentoGarantias())
				ingresoPacienteForm.setDeboAbrirEventoCatastrofico(true);
			else
				ingresoPacienteForm.setDeboAbrirEventoCatastrofico(false);
			//************************************************************************************************
			//*******SE VERIFICA SI SE DEBE ABRIR LA REFERENCIA*******************************************
			if(ingresoPacienteForm.isDeboAbrirReferencia())
			{
				String pathReferencia =  "../referenciaDummy/referenciaDummy.do?estado=empezarReferencia"+
					"&tipoIdentificacion="+paciente.getCodigoTipoIdentificacionPersona()+"-"+paciente.getTipoIdentificacionPersona(false)+
					"&numeroIdentificacion="+paciente.getNumeroIdentificacionPersona()+
					"&tipoReferencia="+ConstantesIntegridadDominio.acronimoExterna+
					"&deboAbrirAsignacionCita="+ingresoPacienteForm.isDeboAbrirAsignacionCitas();
				ingresoPacienteForm.setPathReferencia(pathReferencia);
			}
			//*******************************************************************************************
			//*******SE VERIFICA SI SE DEBE IMPRIMIR LA ADMISION*****************************************
			if(!ingresoPacienteForm.isRequeridoDocumentoGarantias()&&
				(
						Integer.parseInt(ingresoPacienteForm.getCuenta("codigoViaIngreso").toString())==ConstantesBD.codigoViaIngresoHospitalizacion||
						Integer.parseInt(ingresoPacienteForm.getCuenta("codigoViaIngreso").toString())==ConstantesBD.codigoViaIngresoUrgencias||
						(Integer.parseInt(ingresoPacienteForm.getCuenta("codigoViaIngreso").toString())==ConstantesBD.codigoViaIngresoAmbulatorios&&
								ingresoPacienteForm.getCuenta("codigoTipoPaciente").toString().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
				)
			)
				ingresoPacienteForm.setDeboImprimirAdmision(true);
			else
				ingresoPacienteForm.setDeboImprimirAdmision(false);
			//*******************************************************************************************
			
			//*************SE GENERAN LAS ALERTAS (SI LAS HAY)************************************
			this.generarAlertasResumen(ingresoPacienteForm,request);
			//************************************************************************************
			
			//************SE REALIZA LA REVISION DE LOS PRESUPUESTOS (VENEZUELA)*******************
			if(!ingresoPacienteForm.isRequeridoDocumentoGarantias())
				this.revisarPresupuestos(con,ingresoPacienteForm);
			//*************************************************************************************
			
			UtilidadBD.closeConnection(con);
			
			return mapping.findForward("resumenCuenta");
			
		}
		else if (ingresoPacienteForm.getCuenta("codigoViaIngreso").toString().equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		{
			return casoViaIngresoHospitalizacion(con, ingresoPacienteForm, request, mapping, usuarioActual, paciente);
		}
		
		UtilidadBD.closeConnection(con);
		return null;
	}

	/**
	 * 
	 * @param con
	 * @param ingresoPacienteForm
	 * @param request
	 * @param mapping
	 * @param usuarioActual
	 * @param paciente
	 * @return
	 */
	private ActionForward casoViaIngresoHospitalizacion(Connection con, IngresoPacienteForm ingresoPacienteForm, HttpServletRequest request, ActionMapping mapping, UsuarioBasico usuarioActual, PersonaBasica paciente){
		ActionErrors errores = new ActionErrors();
		
		///************VALIDACION DE PAR√ÅMETROS M√âDICO ESPECIALISTA O M√âDICO****************** 
		String ocupacionEspecialista = ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(usuarioActual.getCodigoInstitucionInt(),true);
		String ocupacionGeneral = ValoresPorDefecto.getCodigoOcupacionMedicoGeneral(usuarioActual.getCodigoInstitucionInt(),true);
		
		if(ocupacionEspecialista.equals("")||ocupacionGeneral.equals(""))
		{
			errores.add("Sin ocupaciones definidas en parÔøΩmetros generales",new ActionMessage("errors.noOcupacionMedica"));
			UtilidadBD.closeConnection(con);
			saveErrors(request, errores);
	        return mapping.findForward("paginaErroresActionErrors");
		}
		//************************************************************************************
		
		//Se pregunta si existe asocio
		if(paciente.getExisteAsocio())
		{				
			//se consulta la fecha y hora del egreso de urgencias
			String fecha_hora=Utilidades.capturarFechayHoraEgresoUrgenciasEnAsocio(ingresoPacienteForm.getCuenta("codigoIngreso").toString(),paciente.getCodigoUltimaViaIngreso()+"");
			
			ingresoPacienteForm.setHospitalizacion("existeAsocio", ConstantesBD.acronimoSi);
			
			//Se verifica que existe fecha/hor egreso urgencias
			if(fecha_hora.split("-").length>1)
			{
				ingresoPacienteForm.setHospitalizacion("fecha", fecha_hora.split("-")[0]);
				ingresoPacienteForm.setHospitalizacion("hora", fecha_hora.split("-")[1]);
				ingresoPacienteForm.setHospitalizacion("fechaEgreso", fecha_hora.split("-")[0]);
				ingresoPacienteForm.setHospitalizacion("horaEgreso", fecha_hora.split("-")[1]);
			}
			else
			{
				ingresoPacienteForm.setHospitalizacion("fecha", UtilidadFecha.getFechaActual());
				ingresoPacienteForm.setHospitalizacion("hora", UtilidadFecha.getHoraActual());
				ingresoPacienteForm.setHospitalizacion("fechaEgreso", "");
				ingresoPacienteForm.setHospitalizacion("horaEgreso", "");
			}
			
			//***************VALIDACION DE MEDICAMENTOS PENDIENTES X DESPACHAR******************************
			ingresoPacienteForm.setHospitalizacion("codigoFarmacia", "");
			if(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("existenMedicamentosXDespachar").toString()))
			{
				String[] datosArea = ingresoPacienteForm.getCuenta("codigoArea").toString().split("-");	
				ingresoPacienteForm.setFarmacias(UtilidadInventarios.obtenerFarmaciasXCentroCosto(con, Integer.parseInt(datosArea[0]),usuarioActual.getCodigoCentroAtencion(),usuarioActual.getCodigoInstitucionInt()));
				//Si ya hab√≠a una se postula autom√°ticamente
				if(ingresoPacienteForm.getFarmacias().size()==1)
					ingresoPacienteForm.setHospitalizacion("codigoFarmacia", ingresoPacienteForm.getFarmacias().get(0).get("codigo").toString());
			}
			//***********************************************************************************************
		}
		else
		{
			ingresoPacienteForm.setHospitalizacion("existeAsocio", ConstantesBD.acronimoNo);
			ingresoPacienteForm.setHospitalizacion("fecha", UtilidadFecha.getFechaActual());
			ingresoPacienteForm.setHospitalizacion("hora", UtilidadFecha.getHoraActual());
			ingresoPacienteForm.setHospitalizacion("fechaEgreso", "");
			ingresoPacienteForm.setHospitalizacion("horaEgreso", "");
		}
		//*********SE CONSULTA LA CAMA RESERVADA DEL PACIENTE*******************************************
		ingresoPacienteForm.setHospitalizacion("reservaCama", UtilidadesManejoPaciente.consultarCamaReservada(con, paciente.getCodigoPersona()+"", usuarioActual.getCodigoCentroAtencion()+""));
		HashMap reserva = (HashMap)ingresoPacienteForm.getHospitalizacion("reservaCama");
		if(Integer.parseInt(reserva.get("numRegistros").toString())>0)
		{
			//Se postula la informacion de la cama
			ingresoPacienteForm.setHospitalizacion("codigoReserva", reserva.get("codigoReserva"));
			ingresoPacienteForm.setHospitalizacion("codigoCama", reserva.get("codigo"));
			ingresoPacienteForm.setHospitalizacion("codigoCamaReserva", reserva.get("codigo"));
			ingresoPacienteForm.setHospitalizacion("habitacion", reserva.get("habitacion"));
			ingresoPacienteForm.setHospitalizacion("piso", reserva.get("piso"));
			ingresoPacienteForm.setHospitalizacion("tipoHabitacion", reserva.get("tipoHabitacion"));
			ingresoPacienteForm.setHospitalizacion("numeroCama", reserva.get("numeroCama"));
			ingresoPacienteForm.setHospitalizacion("nombreCentroCosto", reserva.get("nombreCentroCosto"));
			ingresoPacienteForm.setHospitalizacion("tipoUsuario", reserva.get("tipoUsuario"));
		}
		else
			ingresoPacienteForm.setHospitalizacion("codigoCamaReserva", "");
		//***********************************************************************************************
		//la autorizacion va a ser la misma digitada en la pagina de la cuenta
		if (ingresoPacienteForm.getCuenta().containsKey("autorizacionIngreso"));
			ingresoPacienteForm.setHospitalizacion("autorizacion", ingresoPacienteForm.getCuenta("autorizacionIngreso"));
		
		//se cargan los profesionales activos segun el area
		String[] datosArea = ingresoPacienteForm.getCuenta("codigoArea").toString().split("-");	
		ingresoPacienteForm.setProfesionales(UtilidadesManejoPaciente.obtenerProfesionales(con, usuarioActual.getCodigoInstitucionInt(), true, true, datosArea[0],""));		
		logger.info("codigo areaa-- "+paciente.getCodigoArea());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingresarAdmisionHospitalaria");
	}


	/**
	 * M√©todo que realiza la admision de hospital d√≠a
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mundoCuenta
	 * @param errores
	 * @param paciente
	 * @param ingreso
	 * @param usuarioActual
	 * @return
	 */
	private ActionErrors insertarAdmisionHospitalDia(Connection con, IngresoPacienteForm ingresoPacienteForm, Cuenta mundoCuenta, ActionErrors errores, PersonaBasica paciente, IngresoGeneral ingreso, UsuarioBasico usuarioActual) 
	{
		//Ingresando la admision de hospitalizacion
		AdmisionHospitalaria admisionHospitalaria = new AdmisionHospitalaria(
				Integer.parseInt(ingresoPacienteForm.getCuenta("codigoOrigenAdmision").toString()), 
				0, //no se ingresa profesional de la salud 
				"", //numero de identificacion del medico (No se necesita)
				"", //tipo de identificacion del medico (No se necesita) 
				0, // no se ingresa cama 
				"1", //Diagnostico no seleccionado (Ya no se est√° ingresando) 
				"0", 
				0, //Causa externa no seleccionada (Ya mp se est√° ingresando)  
				/*mundoCuenta.getCuenta().getConvenios()[0].getNroAutorizacion(),*/
				usuarioActual, 
				mundoCuenta.getCuenta().getIdCuenta(), 
				ingresoPacienteForm.getCuenta("horaAdmision").toString(),
				ingresoPacienteForm.getCuenta("fechaAdmision").toString());
		//***************************************************************************************************************
		
		//Se realiza la validacion de la admision hospitalaria
		RespuestaValidacion resp3=UtilidadValidacion.validacionIngresarAdmisionHospitalaria(con, paciente.getCodigoPersona(), Integer.parseInt(mundoCuenta.getCuenta().getIdCuenta()), ingreso.getInstitucion());
		logger.info("¬øPUEDO CONTINUAT CON EL INGRESO DE LA ADMISION? "+resp3.puedoSeguir);
		if (resp3.puedoSeguir)
		{
			//*******************INSERTAR ADMISION HOSPITALARIA********************************************************
			//fuera de insertar la admision se cambia el estado de la cama a ocupada
			int codigoAdmision = admisionHospitalaria.insertarTransaccional(con, ConstantesBD.continuarTransaccion, usuarioActual.getCodigoInstitucionInt());
			//*********************************************************************************************************
			logger.info("paso por aqui A CODIGO ADMISION=> "+codigoAdmision);
			if(codigoAdmision>0)
			{
				boolean reingresoHospitalDia = false;
				if(mundoCuenta.getCuenta().isHospitalDia()){
					//************SE INSERTA EL REINGRESO PARA HOSPITAL D√çA********************************
					DtoReingresoSalidaHospiDia dtoReingreso = new DtoReingresoSalidaHospiDia();
					dtoReingreso.setTipo(ConstantesIntegridadDominio.acronimoReingreso);
					dtoReingreso.setPaciente(new InfoDatosInt(paciente.getCodigoPersona()));
					dtoReingreso.setCuenta(mundoCuenta.getCuenta().getIdCuenta());
					dtoReingreso.setIngreso(mundoCuenta.getCuenta().getIdIngreso());
					dtoReingreso.setLoginUsuarioIngreso(usuarioActual.getLoginUsuario());
					dtoReingreso.setFechaIngreso(admisionHospitalaria.getFecha());
					dtoReingreso.setHoraIngreso(admisionHospitalaria.getHora());
					dtoReingreso.setInstitucion(usuarioActual.getCodigoInstitucionInt());
					reingresoHospitalDia = ReingresoHospitalDia.insertar(con, dtoReingreso);
					//***********************************************************************************
				} else {
					reingresoHospitalDia = true;
				}
				
				if(reingresoHospitalDia)
				{
					//***************SE INSERTA SOLICITUD DE VALORACION INICIAL***********************************************
					//Se verifica si se insertÔøΩ la admision correctamente
					Solicitud solicitud=new Solicitud();
					//mundoCuenta.getCuenta().getConvenios()[0].getNroAutorizacion(),
					solicitud.llenarSolicitudValoracionInicial( new InfoDatosInt(mundoCuenta.getCuenta().getCodigoArea(), "centroCostoSolicitante"), new InfoDatosInt (mundoCuenta.getCuenta().getCodigoArea(), "centroCostoSolicitado"), Integer.parseInt(mundoCuenta.getCuenta().getIdCuenta()), true, new InfoDatosInt (ConstantesBD.codigoTipoSolicitudInicialHospitalizacion,""));
					
					int numeroSolicitud=0;
					try
					{
						numeroSolicitud = solicitud.insertarSolicitudValoracionInicialTransaccional(con, ConstantesBD.continuarTransaccion);
						solicitud.insertarTratanteInicialTransaccional(con, numeroSolicitud,0,mundoCuenta.getCuenta().getCodigoArea(), ConstantesBD.continuarTransaccion);
						if(numeroSolicitud<=0)
							errores.add("Error ingresando solicitud valoracion hospitalizacion",new ActionMessage("errors.noSeGraboInformacion","DE LA SOLICITUD VALORACI√ìN DE HOSPITALIZACI√ìN"));
					}
					catch(Exception e)
					{
						errores.add("Error ingresando solicitud valoracion hospitalizacion",new ActionMessage("errors.noSeGraboInformacion","DE LA SOLICITUD VALORACIÔøΩN DE HOSPITALIZACIÔøΩN"));
					}
				}
				else
					errores.add("Error ingresando la admison",new ActionMessage("errors.noSeGraboInformacion","DEL INGRESO A HOSPITAL D√çA"));
			}
			else
				errores.add("Error ingresando la admison",new ActionMessage("errors.noSeGraboInformacion","DE LA ADMISI√ìN DE HOSPITALIZACI√ìN"));
		}
		else
			errores.add("Error validando la admision de hospitalizacion",new ActionMessage("errors.notEspecific",resp3.textoRespuesta));
		
		return errores;
	}



	/**
	 * Insertar admision de urgencias
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mundoCuenta
	 * @param errores
	 * @param paciente
	 * @param usuarioActual 
	 * @param ingreso 
	 * @return
	 */
	private ActionErrors insertarAdmisionUrgencias(Connection con, IngresoPacienteForm ingresoPacienteForm, Cuenta mundoCuenta, ActionErrors errores, PersonaBasica paciente, IngresoGeneral ingreso, UsuarioBasico usuarioActual) 
	{
		//Verificando que no haya una admision hospitalaria abierta para el paciente en la institucion actual
		RespuestaValidacion resp3=UtilidadValidacion.validacionIngresarAdmisionHospitalaria(con, paciente.getCodigoPersona(), Integer.parseInt(mundoCuenta.getCuenta().getIdCuenta()), ingreso.getInstitucion());
		
		
		if (resp3.puedoSeguir)
		{
			// Ingresando la admisiÔøΩn de urgencias
			AdmisionUrgencias admisionUrg = new AdmisionUrgencias();
		
			admisionUrg.setCodigoOrigen(mundoCuenta.getCuenta().getCodigoOrigenAdmision());
			admisionUrg.setOrigen(mundoCuenta.getCuenta().getDescripcionOrigenAdmision());
			admisionUrg.setLoginUsuario(usuarioActual);
			admisionUrg.setIdCuenta(Integer.parseInt(mundoCuenta.getCuenta().getIdCuenta()));
			admisionUrg.setFecha(ingresoPacienteForm.getCuenta("fechaAdmision").toString());
			admisionUrg.setHora(ingresoPacienteForm.getCuenta("horaAdmision").toString());
			//admisionUrg.setNumeroAutorizacion(mundoCuenta.getCuenta().getConvenios()[0].getNroAutorizacion());
			admisionUrg.setConsecutivoTriage(ingresoPacienteForm.getConsecutivoTriage());
			admisionUrg.setConsecutivoFechaTriage(ingresoPacienteForm.getConsecutivoFechaTriage());
			
			int codigoAdmision = admisionUrg.insertarTransaccional(con, ConstantesBD.continuarTransaccion,usuarioActual.getCodigoInstitucionInt());
			
			//Se verifica si se insertÔøΩ la admision correctamente
			if(codigoAdmision>0)
			{
			
				Solicitud solicitud=new Solicitud();
				//  mundoCuenta.getCuenta().getConvenios()[0].getNroAutorizacion(),
				solicitud.llenarSolicitudValoracionInicial(new InfoDatosInt(mundoCuenta.getCuenta().getCodigoArea(), "centroCostoSolicitante"), new InfoDatosInt (mundoCuenta.getCuenta().getCodigoArea(), "centroCostoSolicitado"), Integer.parseInt(mundoCuenta.getCuenta().getIdCuenta()), true, new InfoDatosInt (ConstantesBD.codigoTipoSolicitudInicialUrgencias,""));
				
				int numeroSolicitud=0;
				try
				{
					numeroSolicitud = solicitud.insertarSolicitudValoracionInicialTransaccional(con, ConstantesBD.continuarTransaccion);
					solicitud.insertarTratanteInicialUrgenciasTransaccional(con, numeroSolicitud,mundoCuenta.getCuenta().getCodigoArea(), ConstantesBD.continuarTransaccion);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					logger.error("Error al tratar de generar la solicitud valoracion de urgencias: "+e);
					errores.add("Error ingresando solicitud valoracion urgencias",new ActionMessage("errors.noSeGraboInformacion","DE LA SOLICITUD VALORACI√ìN DE URGENCIAS"));
				}
				
			}
			else
				errores.add("Error ingresando la admison",new ActionMessage("errors.noSeGraboInformacion","DE LA ADMISI√ìN DE URGENCIAS"));
		}					
		else
			errores.add("Error validando admison urgencias",new ActionMessage("errors.notEspecific",resp3.textoRespuesta));
		
		return errores;
	}



	/**
	 * M√©todo implementado para hacer la revision de los presupuesto
	 * @param con
	 * @param ingresoPacienteForm
	 */
	private void revisarPresupuestos(Connection con, IngresoPacienteForm ingresoPacienteForm) 
	{
		String codigoPaciente = ingresoPacienteForm.getCuenta("codigoPaciente").toString();
		String listadoConvenios = ingresoPacienteForm.getCuenta("codigoConvenio").toString();
		
		for(int i=0;i<Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numRegistros").toString());i++)
			listadoConvenios += "," + ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i);
		
		ingresoPacienteForm.setPresupuestos(PresupuestoPaciente.obtenerPrespuestosSinIngreso(con, codigoPaciente, listadoConvenios));
		
		if(Integer.parseInt(ingresoPacienteForm.getPresupuestos("numRegistros").toString())>0)
			ingresoPacienteForm.setPresupuestoPaciente(true);
		else
			ingresoPacienteForm.setPresupuestoPaciente(false);
		
	}



	/**
	 * M√©todo que carga el resumen de la cuenta
	 * @param con
	 * @param ingresoPacienteForm
	 * @param idCuenta
	 * @param codigoAdmHosp 
	 * @param usuarioActual 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void cargarFormResumenCuenta(Connection con, IngresoPacienteForm ingresoPacienteForm, String idCuenta, int codigoAdmHosp, UsuarioBasico usuarioActual) 
	{
		//logger.info("\n entre a cargarFormResumenCuenta -->"+ingresoPacienteForm.getCuenta("transplante"));
		Cuenta mundoCuenta = new Cuenta();
		mundoCuenta.cargar(con, idCuenta);
		//Se mantiene la fecha admision, y hora admison de la cuentad e hospital d√≠a
		String fechaAdmision = ingresoPacienteForm.getCuenta("fechaAdmision")!=null?ingresoPacienteForm.getCuenta("fechaAdmision").toString():"";
		String horaAdmision = ingresoPacienteForm.getCuenta("horaAdmision")!=null?ingresoPacienteForm.getCuenta("horaAdmision").toString():"";
		String transplante = UtilidadCadena.noEsVacio(ingresoPacienteForm.getCuenta("transplante")+"")?(ingresoPacienteForm.getCuenta("transplante")+""):"";
		String preingreso = ingresoPacienteForm.getCuenta().get("preingreso")==null?ConstantesBD.acronimoNo:ingresoPacienteForm.getCuenta().get("preingreso").toString();
		ingresoPacienteForm.setCuenta(new HashMap<String, Object>());
		
		if(!mundoCuenta.getCuenta().isHospitalDia())
		{
			ingresoPacienteForm.setCuenta("fechaAdmision", mundoCuenta.getCuenta().getFechaApertura());
			ingresoPacienteForm.setCuenta("horaAdmision", mundoCuenta.getCuenta().getHoraApertura());
		}
		else
		{
			ingresoPacienteForm.setCuenta("fechaAdmision", fechaAdmision);
			ingresoPacienteForm.setCuenta("horaAdmision", horaAdmision);
		}
		ingresoPacienteForm.setVariosConvenios(new HashMap<String, Object>());
		ingresoPacienteForm.setVerificacion(new HashMap<String, Object>());
		ingresoPacienteForm.setHospitalizacion(new HashMap<String, Object>());
		
		ingresoPacienteForm.setCuenta("codigoInstitucion", usuarioActual.getCodigoInstitucion());
		ingresoPacienteForm.setCuenta("idCuenta", mundoCuenta.getCuenta().getIdCuenta());
		ingresoPacienteForm.setCuenta("idIngreso", mundoCuenta.getCuenta().getIdIngreso());
		ingresoPacienteForm.setCuenta("codigoPaciente", mundoCuenta.getCuenta().getCodigoPaciente());
		ingresoPacienteForm.setCuenta("codigoViaIngreso", mundoCuenta.getCuenta().getCodigoViaIngreso()+"");
		ingresoPacienteForm.setCuenta("nombreViaIngreso", mundoCuenta.getCuenta().getDescripcionViaIngreso());
		ingresoPacienteForm.setCuenta("hospitalDia", mundoCuenta.getCuenta().isHospitalDia()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("manejaHospitalDia", ValoresPorDefecto.getEntidadManejaHospitalDia(usuarioActual.getCodigoInstitucionInt()));
		ingresoPacienteForm.setCuenta("idSubCuenta", mundoCuenta.getCuenta().getConvenios()[0].getSubCuenta());
		ingresoPacienteForm.setCuenta("codigoConvenio", mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo());
		ingresoPacienteForm.setCuenta("nombreConvenio", mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getNombre());
		ingresoPacienteForm.setCuenta("numeroContrato", mundoCuenta.getCuenta().getConvenios()[0].getNumeroContrato());
		ingresoPacienteForm.setCuenta("valorUtilizadoSoat", mundoCuenta.getCuenta().getConvenios()[0].getValorUtilizadoSoat());
		ingresoPacienteForm.setCuenta("fechaAfiliacion", mundoCuenta.getCuenta().getConvenios()[0].getFechaAfiliacion());
		ingresoPacienteForm.setCuenta("semanasCotizacion", mundoCuenta.getCuenta().getConvenios()[0].getSemanasCotizacion()+"");
		ingresoPacienteForm.setCuenta("mesesCotizacion", mundoCuenta.getCuenta().getConvenios()[0].getMesesCotizacion()+"");
		ingresoPacienteForm.setCuenta("nombreTipoComplejidad", mundoCuenta.getCuenta().getDescripcionTipoComplejidad());
		ingresoPacienteForm.setCuenta("nombreEstratoSocial", mundoCuenta.getCuenta().getConvenios()[0].getDescripcionClasificacionSocioEconomica());
		ingresoPacienteForm.setCuenta("nombreTipoAfiliado", mundoCuenta.getCuenta().getConvenios()[0].getDescripcionTipoAfiliado());
		ingresoPacienteForm.setCuenta("nombreMontoCobro", mundoCuenta.getCuenta().getConvenios()[0].getDescripcionMontoCobro());
		ingresoPacienteForm.setCuenta("nombreTipoRegimen", Utilidades.obtenerTipoRegimenConvenio(con, mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo()+"").split("-")[1]);
		ingresoPacienteForm.setCuenta("codigoTipoCobertura", mundoCuenta.getCuenta().getConvenios()[0].getCodigoTipoCobertura());
		ingresoPacienteForm.setCuenta("nombreTipoCobertura", mundoCuenta.getCuenta().getConvenios()[0].getNombreTipoCobertura());
		ingresoPacienteForm.setCuenta("codigoTipoPaciente", mundoCuenta.getCuenta().getCodigoTipoPaciente());
		ingresoPacienteForm.setCuenta("nombreTipoPaciente", mundoCuenta.getCuenta().getDescripcionTipoPaciente());
		ingresoPacienteForm.setCuenta("nombreNaturaleza", mundoCuenta.getCuenta().getConvenios()[0].getDescripcionNaturalezaPaciente());
		ingresoPacienteForm.setCuenta("codigoTipoEvento", mundoCuenta.getCuenta().getCodigoTipoEvento());
		ingresoPacienteForm.setCuenta("nombreTipoEvento", mundoCuenta.getCuenta().getDescripcionTipoEvento());
		ingresoPacienteForm.setCuenta("nombreArpAfiliado", mundoCuenta.getCuenta().getDescripcionConvenioArpAfiliado());
		ingresoPacienteForm.setCuenta("nombreOrigenAdmision", mundoCuenta.getCuenta().getDescripcionOrigenAdmision());
		ingresoPacienteForm.setCuenta("nombreArea", mundoCuenta.getCuenta().getDescripcionArea());
		ingresoPacienteForm.setCuenta("numeroCarnet", mundoCuenta.getCuenta().getConvenios()[0].getNroCarnet());
		ingresoPacienteForm.setCuenta("numeroPoliza", mundoCuenta.getCuenta().getConvenios()[0].getNroPoliza());
		ingresoPacienteForm.setCuenta("autorizacionIngreso", mundoCuenta.getCuenta().getConvenios()[0].getNroAutorizacion());
		ingresoPacienteForm.setCuenta("preingreso", preingreso);
		ingresoPacienteForm.setCuenta("observaciones", mundoCuenta.getCuenta().getObservaciones());
		ingresoPacienteForm.setRegistroInconsistencias(UtilidadesFacturacion.esConvenioReporteInconsistenciasenBD(con, mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo()));
		///////////////////////////////////////////////////////////////////////////////////////////////
				//cambio por anexo 654 transplante
		if (UtilidadCadena.noEsVacio(transplante))
			ingresoPacienteForm.setCuenta("nombreTransplante", ValoresPorDefecto.getIntegridadDominio(transplante));
		else
			ingresoPacienteForm.setCuenta("nombreTransplante", "");
		//////////////////////////////////////////////////////////////////////////////////////////////
		ingresoPacienteForm.setCuenta("esConvenioColsanitas", 
			UtilidadesFacturacion.esConvenioColsanitas(
				con, 
				mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo(), 
				usuarioActual.getCodigoInstitucionInt()
			)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("numeroSolicitudVolante", mundoCuenta.getCuenta().getConvenios()[0].getNumeroSolicitudVolante());
		
		
/*		if(mundoCuenta.getCuenta().getConvenios()[0].isSubCuentaPoliza())
		{
			ingresoPacienteForm.setCuenta("esConvenioPoliza", ConstantesBD.acronimoSi);
			ingresoPacienteForm.setCuenta("apellidosPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getApellidos());
			ingresoPacienteForm.setCuenta("nombresPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getNombres());
			ingresoPacienteForm.setCuenta("descripcionTipoIdPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getDescripcionTipoIdentificacion());
			ingresoPacienteForm.setCuenta("numeroIdPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getNumeroIdentificacion());
			ingresoPacienteForm.setCuenta("direccionPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getDireccion());
			ingresoPacienteForm.setCuenta("telefonoPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getTelefono());
			ingresoPacienteForm.setCuenta("autorizacionPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getAutorizacionInformacionPoliza(0));
			ingresoPacienteForm.setCuenta("valorPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getValorInformacionPoliza(0));
		}
		else*/
		ingresoPacienteForm.setCuenta("esConvenioPoliza", ConstantesBD.acronimoNo);
		
		int numReqIngreso = 0;
		int numReqEgreso = 0;
		for(int i=0;i<mundoCuenta.getCuenta().getConvenios()[0].getRequisitosPaciente().size();i++)
		{
			DtoRequsitosPaciente requisitos = (DtoRequsitosPaciente)mundoCuenta.getCuenta().getConvenios()[0].getRequisitosPaciente().get(i);
			if(requisitos.getTipo().equals(ConstantesIntegridadDominio.acronimoIngreso))
			{
				ingresoPacienteForm.setCuenta("codigoReqIngreso_"+numReqIngreso, requisitos.getCodigo()+"");
				ingresoPacienteForm.setCuenta("descripcionReqIngreso_"+numReqIngreso, requisitos.getDescripcion());
				ingresoPacienteForm.setCuenta("cumplidoReqIngreso_"+numReqIngreso, requisitos.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				numReqIngreso++;
				
			}
			else if(requisitos.getTipo().equals(ConstantesIntegridadDominio.acronimoEgreso))
			{
				ingresoPacienteForm.setCuenta("codigoReqEgreso_"+numReqEgreso, requisitos.getCodigo()+"");
				ingresoPacienteForm.setCuenta("descripcionReqEgreso_"+numReqEgreso, requisitos.getDescripcion());
				ingresoPacienteForm.setCuenta("cumplidoReqEgreso_"+numReqEgreso, requisitos.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				numReqEgreso++;
			}
		}
		ingresoPacienteForm.setCuenta("numReqIngreso", numReqIngreso+"");
		ingresoPacienteForm.setCuenta("numReqEgreso", numReqEgreso+"");
		ingresoPacienteForm.setCuenta("tieneRequisitosPaciente", (numReqEgreso>0||numReqIngreso>0)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		
		int cont = 0;
		for(int i=1;i<mundoCuenta.getCuenta().getConvenios().length;i++)
		{
			if(!ingresoPacienteForm.isRegistroInconsistencias()){
				ingresoPacienteForm.setRegistroInconsistencias(UtilidadesFacturacion.esConvenioReporteInconsistenciasenBD(con, mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo()));	
			}
			
			ingresoPacienteForm.setVariosConvenios("idSubCuenta_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getSubCuenta());
			ingresoPacienteForm.setVariosConvenios("codigoConvenio_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo());
			ingresoPacienteForm.setVariosConvenios("nombreConvenio_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getNombre());
			ingresoPacienteForm.setVariosConvenios("numeroContrato_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getNumeroContrato());
			ingresoPacienteForm.setVariosConvenios("valorUtilizadoSoat_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getValorUtilizadoSoat());
			ingresoPacienteForm.setVariosConvenios("fechaAfiliacion_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getFechaAfiliacion());
			ingresoPacienteForm.setVariosConvenios("semanasCotizacion_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getSemanasCotizacion()+"");
			ingresoPacienteForm.setVariosConvenios("mesesCotizacion_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getMesesCotizacion()+"");
			ingresoPacienteForm.setVariosConvenios("nombreEstratoSocial_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getDescripcionClasificacionSocioEconomica());
			ingresoPacienteForm.setVariosConvenios("nombreTipoAfiliado_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getDescripcionTipoAfiliado());
			ingresoPacienteForm.setVariosConvenios("nombreMontoCobro_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getDescripcionMontoCobro());
			ingresoPacienteForm.setVariosConvenios("nombreTipoRegimen_"+cont, Utilidades.obtenerTipoRegimenConvenio(con, mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo()+"").split("-")[1]);
			ingresoPacienteForm.setVariosConvenios("codigoTipoCobertura_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getCodigoTipoCobertura());
			ingresoPacienteForm.setVariosConvenios("nombreTipoCobertura_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getNombreTipoCobertura());
			ingresoPacienteForm.setVariosConvenios("nombreNaturaleza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getDescripcionNaturalezaPaciente());
			ingresoPacienteForm.setVariosConvenios("numeroCarnet_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getNroCarnet());
			ingresoPacienteForm.setVariosConvenios("numeroPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getNroPoliza());
			ingresoPacienteForm.setVariosConvenios("autorizacionIngreso_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getNroAutorizacion());
			ingresoPacienteForm.setVariosConvenios("esConvenioColsanitas_"+cont, 
					UtilidadesFacturacion.esConvenioColsanitas(
						con, 
						mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo(), 
						usuarioActual.getCodigoInstitucionInt()
					)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			ingresoPacienteForm.setVariosConvenios("numeroSolicitudVolante_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getNumeroSolicitudVolante());
			
			
			/*if(mundoCuenta.getCuenta().getConvenios()[i].isSubCuentaPoliza())
			{
				ingresoPacienteForm.setVariosConvenios("esConvenioPoliza_"+cont, ConstantesBD.acronimoSi);
				ingresoPacienteForm.setVariosConvenios("apellidosPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getApellidos());
				ingresoPacienteForm.setVariosConvenios("nombresPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getNombres());
				ingresoPacienteForm.setVariosConvenios("descripcionTipoIdPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getDescripcionTipoIdentificacion());
				ingresoPacienteForm.setVariosConvenios("numeroIdPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getNumeroIdentificacion());
				ingresoPacienteForm.setVariosConvenios("direccionPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getDireccion());
				ingresoPacienteForm.setVariosConvenios("telefonoPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getTelefono());
				ingresoPacienteForm.setVariosConvenios("autorizacionPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getAutorizacionInformacionPoliza(0));
				ingresoPacienteForm.setVariosConvenios("valorPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getValorInformacionPoliza(0));
			}
			else*/
				ingresoPacienteForm.setVariosConvenios("esConvenioPoliza_"+cont, ConstantesBD.acronimoNo);
			
			
			//se llena la informacion de verificacion de derechos
			if(!mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getSubCuenta().equals(""))
			{
				ingresoPacienteForm.setVariosConvenios("tieneVerificacionDerechos_"+cont, ConstantesBD.acronimoSi);
				ingresoPacienteForm.setVariosConvenios("codigoEstado_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getCodigoEstado());
				ingresoPacienteForm.setVariosConvenios("nombreEstado_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getDescripcionEstado());
				ingresoPacienteForm.setVariosConvenios("nombreTipo_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getDescripcionTipo());
				ingresoPacienteForm.setVariosConvenios("numero_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getNumero());
				ingresoPacienteForm.setVariosConvenios("personaSolicita_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getPersonaSolicita());
				ingresoPacienteForm.setVariosConvenios("fechaSolicitud_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getFechaSolicitud());
				ingresoPacienteForm.setVariosConvenios("horaSolicitud_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getHoraSolicitud());
				ingresoPacienteForm.setVariosConvenios("personaContactada_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getPersonaContactada());
				ingresoPacienteForm.setVariosConvenios("fechaVerificacion_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getFechaVerificacion());
				ingresoPacienteForm.setVariosConvenios("horaVerificacion_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getHoraVerificacion());
				ingresoPacienteForm.setVariosConvenios("porcentajeCobertura_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getPorcentajeCobertura());
				ingresoPacienteForm.setVariosConvenios("cuotaVerificacion_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getCuotaVerificacion());
				ingresoPacienteForm.setVariosConvenios("observaciones_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getObservaciones());
			}
			else
				ingresoPacienteForm.setVariosConvenios("tieneVerificacionDerechos_"+cont, ConstantesBD.acronimoNo);
			
			numReqIngreso = 0;
			numReqEgreso = 0;
			for(int j=0;j<mundoCuenta.getCuenta().getConvenios()[i].getRequisitosPaciente().size();j++)
			{
				DtoRequsitosPaciente requisitos = (DtoRequsitosPaciente)mundoCuenta.getCuenta().getConvenios()[i].getRequisitosPaciente().get(j);
				if(requisitos.getTipo().equals(ConstantesIntegridadDominio.acronimoIngreso))
				{
					ingresoPacienteForm.setVariosConvenios("codigoReqIngreso_"+cont+"_"+numReqIngreso, requisitos.getCodigo()+"");
					ingresoPacienteForm.setVariosConvenios("descripcionReqIngreso_"+cont+"_"+numReqIngreso, requisitos.getDescripcion());
					ingresoPacienteForm.setVariosConvenios("cumplidoReqIngreso_"+cont+"_"+numReqIngreso, requisitos.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
					numReqIngreso++;
					
				}
				else if(requisitos.getTipo().equals(ConstantesIntegridadDominio.acronimoEgreso))
				{
					ingresoPacienteForm.setVariosConvenios("codigoReqEgreso_"+cont+"_"+numReqEgreso, requisitos.getCodigo()+"");
					ingresoPacienteForm.setVariosConvenios("descripcionReqEgreso_"+cont+"_"+numReqEgreso, requisitos.getDescripcion());
					ingresoPacienteForm.setVariosConvenios("cumplidoReqEgreso_"+cont+"_"+numReqEgreso, requisitos.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
					numReqEgreso++;
				}
			}
			ingresoPacienteForm.setVariosConvenios("numReqIngreso_"+cont, numReqIngreso+"");
			ingresoPacienteForm.setVariosConvenios("numReqEgreso_"+cont, numReqEgreso+"");
			ingresoPacienteForm.setVariosConvenios("tieneRequisitosPaciente_"+cont, (numReqEgreso>0||numReqIngreso>0)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			
			cont++;
		}
		ingresoPacienteForm.setVariosConvenios("numRegistros", cont+"");
		ingresoPacienteForm.setCuenta("seccionVariosConvenios", ConstantesBD.acronimoNo);
		//se llena la informacion de verificacion de derechos
		if(!mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getSubCuenta().equals(""))
		{
			ingresoPacienteForm.setCuenta("seccionVerificacionDerechos", ConstantesBD.acronimoNo);
			ingresoPacienteForm.setCuenta("tieneVerificacionDerechos", ConstantesBD.acronimoSi);
			ingresoPacienteForm.setVerificacion("codigoEstado", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getCodigoEstado());
			ingresoPacienteForm.setVerificacion("nombreEstado", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getDescripcionEstado());
			ingresoPacienteForm.setVerificacion("nombreTipo", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getDescripcionTipo());
			ingresoPacienteForm.setVerificacion("numero", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getNumero());
			ingresoPacienteForm.setVerificacion("personaSolicita", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getPersonaSolicita());
			ingresoPacienteForm.setVerificacion("fechaSolicitud", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getFechaSolicitud());
			ingresoPacienteForm.setVerificacion("horaSolicitud", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getHoraSolicitud());
			ingresoPacienteForm.setVerificacion("personaContactada", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getPersonaContactada());
			ingresoPacienteForm.setVerificacion("fechaVerificacion", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getFechaVerificacion());
			ingresoPacienteForm.setVerificacion("horaVerificacion", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getHoraVerificacion());
			ingresoPacienteForm.setVerificacion("porcentajeCobertura", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getPorcentajeCobertura());
			ingresoPacienteForm.setVerificacion("cuotaVerificacion", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getCuotaVerificacion());
			ingresoPacienteForm.setVerificacion("observaciones", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getObservaciones());
		}
		else
			ingresoPacienteForm.setCuenta("tieneVerificacionDerechos", ConstantesBD.acronimoNo);
		
		//Se verifica si se debe imprimir la verificaciÔøΩn de derechos
		if(!ingresoPacienteForm.isRequeridoDocumentoGarantias()&&
			UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("tieneVerificacionDerechos").toString())&&
			(ingresoPacienteForm.getVerificacion("codigoEstado").toString().equals(ConstantesIntegridadDominio.acronimoEstadoAceptado)||
					ingresoPacienteForm.getVerificacion("codigoEstado").toString().equals(ConstantesIntegridadDominio.acronimoEstadoRechazado)))
			ingresoPacienteForm.setCuenta("imprimirVerificacion", ConstantesBD.acronimoSi);
		else
			ingresoPacienteForm.setCuenta("imprimirVerificacion", ConstantesBD.acronimoNo);
		
		//Se llena el responsable del paciente
		if(mundoCuenta.getCuenta().isTieneResponsablePaciente())
		{
			ingresoPacienteForm.setCuenta("seccionResponsablePaciente", ConstantesBD.acronimoNo);
			ingresoPacienteForm.setCuenta("tieneResponsablePaciente", ConstantesBD.acronimoSi);
			ingresoPacienteForm.setResponsable("descripcionTipoIdentificacion", mundoCuenta.getCuenta().getResponsablePaciente().getDescripcionTipoIdentificacion());
			ingresoPacienteForm.setResponsable("numeroIdentificacion", mundoCuenta.getCuenta().getResponsablePaciente().getNumeroIdentificacion());
			ingresoPacienteForm.setResponsable("nombrePaisExpedicion", mundoCuenta.getCuenta().getResponsablePaciente().getDescripcionPaisExpedicion());
			ingresoPacienteForm.setResponsable("nombreCiudadExpedicion", mundoCuenta.getCuenta().getResponsablePaciente().getDescripcionCiudadExpedicion());
			ingresoPacienteForm.setResponsable("primerApellido", mundoCuenta.getCuenta().getResponsablePaciente().getPrimerApellido());
			ingresoPacienteForm.setResponsable("segundoApellido", mundoCuenta.getCuenta().getResponsablePaciente().getSegundoApellido());
			ingresoPacienteForm.setResponsable("primerNombre", mundoCuenta.getCuenta().getResponsablePaciente().getPrimerNombre());
			ingresoPacienteForm.setResponsable("segundoNombre", mundoCuenta.getCuenta().getResponsablePaciente().getSegundoNombre());
			ingresoPacienteForm.setResponsable("direccion", mundoCuenta.getCuenta().getResponsablePaciente().getDireccion());
			ingresoPacienteForm.setResponsable("nombrePais", mundoCuenta.getCuenta().getResponsablePaciente().getDescripcionPais());
			ingresoPacienteForm.setResponsable("nombreCiudad", mundoCuenta.getCuenta().getResponsablePaciente().getDescripcionCiudad());
			ingresoPacienteForm.setResponsable("nombreBarrio", mundoCuenta.getCuenta().getResponsablePaciente().getDescripcionBarrio());
			ingresoPacienteForm.setResponsable("telefono", mundoCuenta.getCuenta().getResponsablePaciente().getTelefono());
			ingresoPacienteForm.setResponsable("fechaNacimiento", mundoCuenta.getCuenta().getResponsablePaciente().getFechaNacimiento());
			ingresoPacienteForm.setResponsable("relacionPaciente", mundoCuenta.getCuenta().getResponsablePaciente().getRelacionPaciente());
			//se usa para llamado de la funcionalidad deudor
			ingresoPacienteForm.setCuenta("datosResponsablePaciente", 
				mundoCuenta.getCuenta().getResponsablePaciente().getTipoIdentificacion()+ConstantesBD.separadorSplit+mundoCuenta.getCuenta().getResponsablePaciente().getNumeroIdentificacion());
			
		}
		else
		{
			ingresoPacienteForm.setCuenta("tieneResponsablePaciente", ConstantesBD.acronimoNo);
			//se usa para llamado de la funcionalidad deudor
			ingresoPacienteForm.setCuenta("datosResponsablePaciente",ConstantesBD.codigoNuncaValido+"");
		}
		
		logger.info("PREINGRESO -->"+ingresoPacienteForm.getCuenta().get("preingreso"));
		
		//********SI LA VIA DE INGRESO ES HOSPITALIZACION (SE CARGA LA ADMISION)********************************************
		if(ingresoPacienteForm.getCuenta("codigoViaIngreso").toString().equals(ConstantesBD.codigoViaIngresoHospitalizacion+"")&&
			!UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("hospitalDia").toString())&&
			!UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("preingreso").toString()))
		{
			ingresoPacienteForm.setCuenta("seccionAdmisionHospitalaria", ConstantesBD.acronimoSi);
			
			AdmisionHospitalaria mundoAdmision = new AdmisionHospitalaria();
			mundoAdmision.setCodigoAdmisionHospitalaria(codigoAdmHosp);
			mundoAdmision.cargar(con);
			
			ingresoPacienteForm.setHospitalizacion("fecha", UtilidadFecha.conversionFormatoFechaAAp(mundoAdmision.getFecha()));
			ingresoPacienteForm.setHospitalizacion("hora", UtilidadFecha.convertirHoraACincoCaracteres(mundoAdmision.getHora()));
			ingresoPacienteForm.setHospitalizacion("codigoCama", mundoAdmision.getCodigoCama()+"");
			ingresoPacienteForm.setHospitalizacion("habitacion", mundoAdmision.getHabitacion());
			ingresoPacienteForm.setHospitalizacion("piso", mundoAdmision.getPiso());
			ingresoPacienteForm.setHospitalizacion("tipoHabitacion", mundoAdmision.getTipoHabitacion());
			ingresoPacienteForm.setHospitalizacion("numeroCama", mundoAdmision.getNombreCama());
			ingresoPacienteForm.setHospitalizacion("nombreCentroCosto", mundoAdmision.getNombreCentroCostoCama());
			ingresoPacienteForm.setHospitalizacion("tipoUsuario", mundoAdmision.getNombreTipoUsuarioCama());
			//ingresoPacienteForm.setHospitalizacion("autorizacion", mundoAdmision.getNumeroAutorizacion());
			ingresoPacienteForm.setHospitalizacion("nombreProfesional", mundoAdmision.getMedico().getNombrePersona(false));
			ingresoPacienteForm.setCuenta("fechaAdmision", UtilidadFecha.conversionFormatoFechaAAp(mundoAdmision.getFecha()));
			ingresoPacienteForm.setCuenta("horaAdmision", UtilidadFecha.convertirHoraACincoCaracteres(mundoAdmision.getHora()));
			
		}
		else if(ingresoPacienteForm.getCuenta("codigoViaIngreso").toString().equals(ConstantesBD.codigoViaIngresoUrgencias+""))
		{
			AdmisionUrgencias mundoAdmision = new AdmisionUrgencias();
			try 
			{
				mundoAdmision.cargar(con, Integer.parseInt(idCuenta));
				ingresoPacienteForm.setCuenta("fechaAdmision", UtilidadFecha.conversionFormatoFechaAAp(mundoAdmision.getFecha()));
				ingresoPacienteForm.setCuenta("horaAdmision", UtilidadFecha.convertirHoraACincoCaracteres(mundoAdmision.getHora()));
			} 
			catch (Exception e) 
			{
				logger.error("Error al tratar de  consultar la admision de urgencias: "+e);
				ingresoPacienteForm.setCuenta("fechaAdmision", "");
				ingresoPacienteForm.setCuenta("horaAdmision", "");
			} 
			ingresoPacienteForm.setCuenta("seccionAdmisionHospitalaria", ConstantesBD.acronimoNo);
		}
		else
			ingresoPacienteForm.setCuenta("seccionAdmisionHospitalaria", ConstantesBD.acronimoNo);
		//********************************************************************************************
		
	}




	/**
	 * M√©todo implementado para cargar la informacion de la cuenta al mundo
	 * @param con 
	 * @param dtoCuenta
	 * @param paciente
	 * @param usuarioActual
	 * @param ingresoPacienteForm
	 */
	private void cargarMundoGuardarCuenta(Connection con, DtoCuentas dtoCuenta, PersonaBasica paciente, UsuarioBasico usuarioActual, IngresoPacienteForm ingresoPacienteForm) 
	{
		DtoSubCuentas[] dtoSubCuentas = null;
		try {
			Contrato mundoContrato = new Contrato();
			int numConvenios =  0; 

			if(!ingresoPacienteForm.getCuenta("codigoConvenio").toString().equals(""))
				numConvenios++; //se suma 1 por el principal

			for(int i=0;i<Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numRegistros").toString());i++)
				if(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i)!=null)
					numConvenios++;

			dtoSubCuentas = new DtoSubCuentas[numConvenios];

			//1) Se llenan los datos de la cuenta
			dtoCuenta.setCodigoViaIngreso(Integer.parseInt(ingresoPacienteForm.getCuenta("codigoViaIngreso").toString()));
			dtoCuenta.setHospitalDia(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("hospitalDia")+""));
			dtoCuenta.setCodigoPaciente(paciente.getCodigoPersona()+"");
			dtoCuenta.setCodigoEstado(ConstantesBD.codigoEstadoCuentaActiva);
			dtoCuenta.setDesplazado(paciente.getCodigoGrupoPoblacional().equals(ConstantesIntegridadDominio.acronimoDesplazados)?true:false);
			dtoCuenta.setLoginUsuario(usuarioActual.getLoginUsuario());
			//tipoCobroPaciente

			if(!(ingresoPacienteForm.getCuenta("indicadorManejaComplejidad")!=null) &&
					UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("indicadorManejaComplejidad").toString())&&
					!ingresoPacienteForm.getCuenta("tipoComplejidad").toString().equals(""))
				dtoCuenta.setCodigoTipoComplejidad(Integer.parseInt(ingresoPacienteForm.getCuenta("tipoComplejidad").toString()));

			dtoCuenta.setCodigoTipoPaciente(ingresoPacienteForm.getCuenta("codigoTipoPaciente").toString());

			dtoCuenta.setCodigoTipoEvento(ingresoPacienteForm.getCuenta("codigoTipoEvento").toString());

			if(dtoCuenta.getCodigoTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTrabajo)&&
					!ingresoPacienteForm.getCuenta("codigoArpAfiliado").toString().equals(""))
				dtoCuenta.setCodigoConvenioArpAfiliado(Integer.parseInt(ingresoPacienteForm.getCuenta("codigoArpAfiliado").toString()));

			dtoCuenta.setCodigoOrigenAdmision(Integer.parseInt(ingresoPacienteForm.getCuenta("codigoOrigenAdmision").toString()));
			//Valdiacion si area tiene tipo de monitoreo
			String[] vectorAreas = ingresoPacienteForm.getCuenta("codigoArea").toString().split("-");
			dtoCuenta.setCodigoArea(Integer.parseInt(vectorAreas[0]));
			if(vectorAreas.length>1)
				dtoCuenta.setCodigoTipoMonitoreo(Integer.parseInt(vectorAreas[1]));

			dtoCuenta.setObservaciones(ingresoPacienteForm.getCuenta("observaciones").toString());

			//Se llena los datos del convenio principal

			int cont = 0;

			//Si se seleccionÔøΩ convenio principal
			if(!ingresoPacienteForm.getCuenta("codigoConvenio").toString().equals(""))
			{



				dtoSubCuentas[cont] = new DtoSubCuentas();
				String[] datosConvenio = ingresoPacienteForm.getCuenta("codigoConvenio").toString().split(ConstantesBD.separadorSplit);
				dtoSubCuentas[cont].setConvenio(new InfoDatosInt(Integer.parseInt(datosConvenio[0]),""));

				dtoSubCuentas[cont].setTipoCobroPaciente(ingresoPacienteForm.getCuenta("tipoCobroPaciente")+"");


				IValidacionTipoCobroPacienteServicio servicioValidacion=FacturacionServicioFabrica.crearValidacionTipoCobroPacienteServicio();
				DtoValidacionTipoCobroPaciente validacion=servicioValidacion.validarTipoCobroPacienteServicioConvenioContrato(Utilidades.convertirAEntero(ingresoPacienteForm.getCuenta("codigoContrato")+""));

				if(UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion()))
					dtoSubCuentas[cont].setClasificacionSocioEconomica(Integer.parseInt(ingresoPacienteForm.getCuenta("codigoEstratoSocial").toString()));
				else
					dtoSubCuentas[cont].setClasificacionSocioEconomica(ConstantesBD.codigoNuncaValido);
				if(UtilidadTexto.getBoolean(validacion.getMostrarTipoAfiliado()))
					dtoSubCuentas[cont].setTipoAfiliado(ingresoPacienteForm.getCuenta("codigoTipoAfiliado").toString());
				else
					dtoSubCuentas[cont].setTipoAfiliado("");
				if(UtilidadTexto.getBoolean(validacion.getMostrarNaturalezaPaciente()))
					dtoSubCuentas[cont].setNaturalezaPaciente(Utilidades.convertirAEntero(ingresoPacienteForm.getCuenta("codigoNaturaleza")+""));
				else
					dtoSubCuentas[cont].setNaturalezaPaciente(ConstantesBD.codigoNuncaValido);

				if(UtilidadTexto.getBoolean(validacion.getManejaMontos()))
				{
					int codigoMontoTempo=Integer.parseInt(ingresoPacienteForm.getCuenta("codigoMontoCobro").toString());
					IMontosCobroServicio servicioMontoCobro=FacturacionServicioFabrica.crearMontosCobroServicio();
					DTOResultadoBusquedaDetalleMontos dtoMonto=servicioMontoCobro.obtenerDetalleMontoCobroPorId(codigoMontoTempo);
					dtoSubCuentas[cont].setMontoCobro(dtoMonto.getDetalleCodigo());
					logger.info("\n\n\n\n\n"+dtoMonto.getTipoDetalleAcronimo()+"\n\n\n\n\n");
					dtoSubCuentas[cont].setTipoMontoCobro(dtoMonto.getTipoDetalleAcronimo());
					logger.info("\n\n\n\n\n"+dtoSubCuentas[cont].getTipoMontoCobro()+"\n\n\n\n\n");
				}
				else
				{
					dtoSubCuentas[cont].setMontoCobro(ConstantesBD.codigoNuncaValido);
					dtoSubCuentas[cont].setPorcentajeMontoCobro(validacion.getPorcentajeMontoCobro());
				}


				if(Utilidades.convertirAEntero(ingresoPacienteForm.getCuenta("codigoTipoCobertura")+"")>0)
				{
					dtoSubCuentas[cont].setCodigoTipoCobertura(Utilidades.convertirAEntero(ingresoPacienteForm.getCuenta("codigoTipoCobertura")+""));
				}
				//Captura de la informacion de la Empresa MultiInstitucion
				if(ValoresPorDefecto.getInstitucionMultiempresa(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
					dtoSubCuentas[cont].setEmpresasInstitucion(datosConvenio[3]);		

				dtoSubCuentas[cont].setCodigoTipoRegimen(ingresoPacienteForm.getCuenta("codigoTipoRegimen").toString());

				if(ingresoPacienteForm.getCuenta("idSubCuenta")!=null)
					dtoSubCuentas[cont].setSubCuenta(ingresoPacienteForm.getCuenta("idSubCuenta").toString());

				dtoSubCuentas[cont].setContrato(Integer.parseInt(ingresoPacienteForm.getCuenta("codigoContrato").toString()));

				mundoContrato.cargar(con, dtoSubCuentas[cont].getContrato()+"");

				if(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esConvenioSoat").toString()))
					dtoSubCuentas[cont].setValorUtilizadoSoat(ingresoPacienteForm.getCuenta("valorUtilizadoSoat").toString());

				dtoSubCuentas[cont].setFechaAfiliacion(ingresoPacienteForm.getCuenta("fechaAfiliacion").toString());

				if(!ingresoPacienteForm.getCuenta("semanasCotizacion").toString().equals(""))
				{
					dtoSubCuentas[cont].setSemanasCotizacion(Integer.parseInt(ingresoPacienteForm.getCuenta("semanasCotizacion").toString()));

					//Se aprovecha y se validan las semanas m√≠nimas de cotizacion---------------------------------------------
					//Se valida solo cuando no es asocio
					if(!UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esAsocio").toString())&& 
							(ingresoPacienteForm.getCuenta("semanasMinimasCotizacion")!= null ||
									!(ingresoPacienteForm.getCuenta("semanasMinimasCotizacion")+"").equals("")))
					{
						try
						{
							//Si las semanas de cotizacion del convenio son menores a las m√≠nimas que tiene establecidas entonces se geenra mensaje de alerta
							if(dtoSubCuentas[cont].getSemanasCotizacion()<Integer.parseInt(ingresoPacienteForm.getCuenta("semanasMinimasCotizacion").toString()))
								ingresoPacienteForm.setAlertaSemanaCotizacion(Utilidades.obtenerNombreConvenioOriginal(con, dtoSubCuentas[cont].getConvenio().getCodigo()));
						}
						catch(Exception e)
						{
							logger.error("Error al tratar de validar las semanas m√≠nimas de cotizacion: "+e);
						}
					}
					//-------------------------------------------------------------------------
				}
				if(!ingresoPacienteForm.getCuenta("mesesCotizacion").toString().equals(""))
					dtoSubCuentas[cont].setMesesCotizacion(Integer.parseInt(ingresoPacienteForm.getCuenta("mesesCotizacion").toString()));

				dtoSubCuentas[cont].setNroCarnet(ingresoPacienteForm.getCuenta("numeroCarnet").toString());
				dtoSubCuentas[cont].setNroPoliza(ingresoPacienteForm.getCuenta("numeroPoliza").toString());
				String autorizacionIngreso=ingresoPacienteForm.getCuenta("autorizacionIngreso")==null?"":ingresoPacienteForm.getCuenta("autorizacionIngreso").toString();
				dtoSubCuentas[cont].setNroAutorizacion(autorizacionIngreso);
				dtoSubCuentas[cont].setCodigoPaciente(paciente.getCodigoPersona());
				dtoSubCuentas[cont].setFacturado(ConstantesBD.acronimoNo);
				dtoSubCuentas[cont].setNroPrioridad(1);
				dtoSubCuentas[cont].setLoginUsuario(usuarioActual.getLoginUsuario());

				if(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esConvenioColsanitas")))
					dtoSubCuentas[cont].setNumeroSolicitudVolante(ingresoPacienteForm.getCuenta("numeroSolicitudVolante").toString());
				else
					dtoSubCuentas[cont].setNumeroSolicitudVolante("");

				//Seccion de informacion de la poliza si aplica
				if(!UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esAsocio")))
					dtoSubCuentas[cont].setSubCuentaPoliza(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esConvenioPoliza").toString()));
				else
					dtoSubCuentas[cont].setSubCuentaPoliza(false);

				if(dtoSubCuentas[cont].isSubCuentaPoliza())
				{
					dtoSubCuentas[cont].getTitularPoliza().setApellidos(ingresoPacienteForm.getCuenta("apellidosPoliza").toString());
					dtoSubCuentas[cont].getTitularPoliza().setNombres(ingresoPacienteForm.getCuenta("nombresPoliza").toString());
					dtoSubCuentas[cont].getTitularPoliza().setCodigoTipoIdentificacion(ingresoPacienteForm.getCuenta("tipoIdPoliza").toString());
					dtoSubCuentas[cont].getTitularPoliza().setNumeroIdentificacion(ingresoPacienteForm.getCuenta("numeroIdPoliza").toString());
					dtoSubCuentas[cont].getTitularPoliza().setDireccion(ingresoPacienteForm.getCuenta("direccionPoliza").toString());
					dtoSubCuentas[cont].getTitularPoliza().setTelefono(ingresoPacienteForm.getCuenta("telefonoPoliza").toString());
					dtoSubCuentas[cont].getTitularPoliza().setInformacionPoliza(
							"",
							UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()), 
							ingresoPacienteForm.getCuenta("autorizacionPoliza").toString(), 
							ingresoPacienteForm.getCuenta("valorPoliza").toString(),
							usuarioActual.getLoginUsuario(),
							false,
							false);


				}
				//Se llenan los requisitos del paciente
				for(int i=0;i<Integer.parseInt(ingresoPacienteForm.getCuenta("numReqIngreso").toString());i++)
				{
					DtoRequsitosPaciente requisitos = new DtoRequsitosPaciente();
					requisitos.setCodigo(Integer.parseInt(ingresoPacienteForm.getCuenta("codigoReqIngreso_"+i).toString()));
					requisitos.setDescripcion(ingresoPacienteForm.getCuenta("descripcionReqIngreso_"+i).toString());
					requisitos.setTipo(ConstantesIntegridadDominio.acronimoIngreso);
					requisitos.setCumplido(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("cumplidoReqIngreso_"+i).toString()));

					dtoSubCuentas[cont].getRequisitosPaciente().add(requisitos);
				}
				for(int i=0;i<Integer.parseInt(ingresoPacienteForm.getCuenta("numReqEgreso").toString());i++)
				{
					DtoRequsitosPaciente requisitos = new DtoRequsitosPaciente();
					requisitos.setCodigo(Integer.parseInt(ingresoPacienteForm.getCuenta("codigoReqEgreso_"+i).toString()));
					requisitos.setDescripcion(ingresoPacienteForm.getCuenta("descripcionReqEgreso_"+i).toString());
					requisitos.setTipo(ConstantesIntegridadDominio.acronimoEgreso);
					requisitos.setCumplido(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("cumplidoReqEgreso_"+i).toString()));

					dtoSubCuentas[cont].getRequisitosPaciente().add(requisitos);
				}
				cont ++;
			}





			//Se llenan los dem√°s convenios
			int posConvenioDefault = ConstantesBD.codigoNuncaValido; //posicion del convenio por default
			String codigoConvenioDefault = ingresoPacienteForm.getCuenta("codigoConvenioDefault").toString();


			for(int i=0;i<Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numRegistros").toString());i++)
			{
				if(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i)!=null)
				{
					//Se obtiene la posicion del convenio por defecto
					if(codigoConvenioDefault.equals(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i).toString()))
						posConvenioDefault = cont;

					logger.info("CODIGO DEL CONVENIO ["+ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i).toString()+"] EN LA POSICION=> "+cont);

					dtoSubCuentas[cont] = new DtoSubCuentas();
					dtoSubCuentas[cont].setConvenio(new InfoDatosInt(Integer.parseInt(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i).toString()),""));

					//Captura de la informacion de la Empresa MultiInstitucion
					if(ValoresPorDefecto.getInstitucionMultiempresa(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
						dtoSubCuentas[cont].setEmpresasInstitucion(ingresoPacienteForm.getVariosConvenios("empresasInstitucion_"+i).toString());		

					dtoSubCuentas[cont].setCodigoTipoRegimen(ingresoPacienteForm.getVariosConvenios("codigoTipoRegimen_"+i).toString());

					if(Utilidades.convertirAEntero(ingresoPacienteForm.getVariosConvenios("codigoTipoCobertura_"+i)+"")>0)
					{
						dtoSubCuentas[cont].setCodigoTipoCobertura(Utilidades.convertirAEntero(ingresoPacienteForm.getVariosConvenios("codigoTipoCobertura_"+i)+""));
					}

					dtoSubCuentas[cont].setContrato(Integer.parseInt(ingresoPacienteForm.getVariosConvenios("codigoContrato_"+i).toString()));
					mundoContrato = new Contrato();
					mundoContrato.cargar(con, dtoSubCuentas[cont].getContrato()+"");

					if(ingresoPacienteForm.getVariosConvenios("idSubCuenta_"+i)!=null)
						dtoSubCuentas[cont].setSubCuenta(ingresoPacienteForm.getVariosConvenios("idSubCuenta_"+i).toString());

					if(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("esConvenioSoat_"+i).toString()))
						dtoSubCuentas[cont].setValorUtilizadoSoat(ingresoPacienteForm.getVariosConvenios("valorUtilizadoSoat_"+i).toString());

					dtoSubCuentas[cont].setFechaAfiliacion(ingresoPacienteForm.getVariosConvenios("fechaAfiliacion_"+i).toString());

					if(!ingresoPacienteForm.getVariosConvenios("semanasCotizacion_"+i).toString().equals(""))
					{
						dtoSubCuentas[cont].setSemanasCotizacion(Integer.parseInt(ingresoPacienteForm.getVariosConvenios("semanasCotizacion_"+i).toString()));

						//Se aprovecha y se validan las semanas m√≠nimas de cotizacion---------------------------------------------
						if(!UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esAsocio").toString())&&
								!ingresoPacienteForm.getVariosConvenios("semanasMinimasCotizacion_"+i).toString().equals(""))
						{
							try
							{
								//Si las semanas de cotizacion del convenio son menores a las m√≠nimas que tiene establecidas entonces se geenra mensaje de alerta
								if(dtoSubCuentas[cont].getSemanasCotizacion()<Integer.parseInt(ingresoPacienteForm.getVariosConvenios("semanasMinimasCotizacion_"+i).toString()))
									ingresoPacienteForm.setAlertaSemanaCotizacion(ingresoPacienteForm.getVariosConvenios("nombreConvenio_"+i).toString());
							}
							catch(Exception e)
							{
								logger.error("Error al tratar de validar las semanas m√≠nimas de cotizacion: "+e);
							}
						}
						//-------------------------------------------------------------------------
					}
					if(!ingresoPacienteForm.getVariosConvenios("mesesCotizacion_"+i).toString().equals(""))
						dtoSubCuentas[cont].setMesesCotizacion(Integer.parseInt(ingresoPacienteForm.getVariosConvenios("mesesCotizacion_"+i).toString()));

					IValidacionTipoCobroPacienteServicio servicioValidacion=FacturacionServicioFabrica.crearValidacionTipoCobroPacienteServicio();
					DtoValidacionTipoCobroPaciente validacion=servicioValidacion.validarTipoCobroPacienteServicioConvenioContrato(Utilidades.convertirAEntero(ingresoPacienteForm.getVariosConvenios("codigoContrato_"+i)+""));

					dtoSubCuentas[cont].setTipoCobroPaciente(validacion.getTipoCobroPaciente());


					if(UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion()))
						dtoSubCuentas[cont].setClasificacionSocioEconomica(Integer.parseInt(ingresoPacienteForm.getVariosConvenios("codigoEstratoSocial_"+i).toString()));
					else
						dtoSubCuentas[cont].setClasificacionSocioEconomica(ConstantesBD.codigoNuncaValido);
					if(UtilidadTexto.getBoolean(validacion.getMostrarTipoAfiliado()))
						dtoSubCuentas[cont].setTipoAfiliado(ingresoPacienteForm.getVariosConvenios("codigoTipoAfiliado_"+i).toString());
					else
						dtoSubCuentas[cont].setTipoAfiliado("");
					if(UtilidadTexto.getBoolean(validacion.getMostrarNaturalezaPaciente())){
						/**
						 * inicio Mt 2595 Si se ingresa informaciÛn en la secciÛn otros convenios al momento de guardar el sistema muestra p·gina en blanco
						 * java.lang.NumberFormatException: For input string: ""
					     *   
						 */
						if(!ingresoPacienteForm.getVariosConvenios("codigoNaturaleza_"+i).toString().trim().equals("")){
							dtoSubCuentas[cont].setNaturalezaPaciente(Integer.parseInt(ingresoPacienteForm.getVariosConvenios("codigoNaturaleza_"+i).toString()));
						}
						/**
						 * fin Mt 2595 
						 */
						
					}
					else{
						dtoSubCuentas[cont].setNaturalezaPaciente(ConstantesBD.codigoNuncaValido);
					}
						

					if(UtilidadTexto.getBoolean(validacion.getManejaMontos()))
					{
						int montoCobroTempo=Integer.parseInt(ingresoPacienteForm.getVariosConvenios("codigoMontoCobro_"+i).toString());
						dtoSubCuentas[cont].setMontoCobro(montoCobroTempo);
						IMontosCobroServicio servicioMontoCobro=FacturacionServicioFabrica.crearMontosCobroServicio();
						DTOResultadoBusquedaDetalleMontos dtoMonto=servicioMontoCobro.obtenerDetalleMontoCobroPorId(montoCobroTempo);
						logger.info("\n\n\n\n\n"+dtoMonto.getTipoDetalleAcronimo()+"\n\n\n\n\n");
						dtoSubCuentas[cont].setTipoMontoCobro(dtoMonto.getTipoDetalleAcronimo());
						logger.info("\n\n\n\n\n"+dtoSubCuentas[cont].getTipoMontoCobro()+"\n\n\n\n\n");
					}
					else
					{
						dtoSubCuentas[cont].setMontoCobro(ConstantesBD.codigoNuncaValido);
						dtoSubCuentas[cont].setPorcentajeMontoCobro(validacion.getPorcentajeMontoCobro());
					}

					dtoSubCuentas[cont].setNroCarnet(ingresoPacienteForm.getVariosConvenios("numeroCarnet_"+i).toString());
					dtoSubCuentas[cont].setNroPoliza(ingresoPacienteForm.getVariosConvenios("numeroPoliza_"+i).toString());
					dtoSubCuentas[cont].setNroAutorizacion(ingresoPacienteForm.getVariosConvenios("autorizacionIngreso_"+i).toString());
					dtoSubCuentas[cont].setCodigoPaciente(paciente.getCodigoPersona());
					dtoSubCuentas[cont].setFacturado(ConstantesBD.acronimoNo);
					dtoSubCuentas[cont].setNroPrioridad(cont+1);
					dtoSubCuentas[cont].setLoginUsuario(usuarioActual.getLoginUsuario());
					if(ValoresPorDefecto.getInstitucionMultiempresa(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
						dtoSubCuentas[cont].setEmpresasInstitucion(ingresoPacienteForm.getVariosConvenios("empresasInstitucion_"+i).toString());

					if(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("esConvenioColsanitas_"+i)))
						dtoSubCuentas[cont].setNumeroSolicitudVolante(ingresoPacienteForm.getVariosConvenios("numeroSolicitudVolante_"+i).toString());
					else
						dtoSubCuentas[cont].setNumeroSolicitudVolante("" );

					//Seccion de informacion de la poliza si aplica
					//Para convenios ya existentes no se inserta esta informacion
					if(dtoSubCuentas[cont].getSubCuenta().equals(""))
						dtoSubCuentas[cont].setSubCuentaPoliza(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("esConvenioPoliza_"+i).toString()));
					else
						dtoSubCuentas[cont].setSubCuentaPoliza(false);

					if(dtoSubCuentas[cont].isSubCuentaPoliza())
					{
						dtoSubCuentas[cont].getTitularPoliza().setApellidos(ingresoPacienteForm.getVariosConvenios("apellidosPoliza_"+i).toString());
						dtoSubCuentas[cont].getTitularPoliza().setNombres(ingresoPacienteForm.getVariosConvenios("nombresPoliza_"+i).toString());
						dtoSubCuentas[cont].getTitularPoliza().setCodigoTipoIdentificacion(ingresoPacienteForm.getVariosConvenios("tipoIdPoliza_"+i).toString());
						dtoSubCuentas[cont].getTitularPoliza().setNumeroIdentificacion(ingresoPacienteForm.getVariosConvenios("numeroIdPoliza_"+i).toString());
						dtoSubCuentas[cont].getTitularPoliza().setDireccion(ingresoPacienteForm.getVariosConvenios("direccionPoliza_"+i).toString());
						dtoSubCuentas[cont].getTitularPoliza().setTelefono(ingresoPacienteForm.getVariosConvenios("telefonoPoliza_"+i).toString());
						dtoSubCuentas[cont].getTitularPoliza().setInformacionPoliza(
								"",
								UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()), 
								ingresoPacienteForm.getVariosConvenios("autorizacionPoliza_"+i).toString(), 
								ingresoPacienteForm.getVariosConvenios("valorPoliza_"+i).toString(),
								usuarioActual.getLoginUsuario(),
								false,
								false);


					}



					//Se llena la verificacion de derechos
					//cuando es asocio no se llena la verificacion de derechos
					if(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("ingresoVerificacionDerechos_"+i).toString())||
							UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("tieneVerificacionDerechos_"+i).toString()))
					{
						dtoSubCuentas[cont].setSubCuentaVerificacionDerechos(true);

						//Si ten√≠a verificacion de derechos se a√±ade el id de la subCuenta
						if(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("tieneVerificacionDerechos_"+i).toString()))
						{

							if(ingresoPacienteForm.getVariosConvenios("idSubCuenta_"+i)!=null)
								dtoSubCuentas[cont].getVerificacionDerechos().setSubCuenta(ingresoPacienteForm.getVariosConvenios("idSubCuenta_"+i).toString());

							//Si exist√≠a verificacion de derechos y se borrÔøΩ la informacion se debe eliminar
							if(!UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("ingresoVerificacionDerechos_"+i).toString()))
								dtoSubCuentas[cont].getVerificacionDerechos().setEliminar(true);
						}



						dtoSubCuentas[cont].getVerificacionDerechos().setCodigoEstado(ingresoPacienteForm.getVariosConvenios("codigoEstado_"+i).toString());
						dtoSubCuentas[cont].getVerificacionDerechos().setCodigoTipo(ingresoPacienteForm.getVariosConvenios("codigoTipo_"+i).toString());
						dtoSubCuentas[cont].getVerificacionDerechos().setNumero(ingresoPacienteForm.getVariosConvenios("numero_"+i).toString());
						dtoSubCuentas[cont].getVerificacionDerechos().setPersonaSolicita(ingresoPacienteForm.getVariosConvenios("personaSolicita_"+i).toString());
						dtoSubCuentas[cont].getVerificacionDerechos().setFechaSolicitud(ingresoPacienteForm.getVariosConvenios("fechaSolicitud_"+i).toString());
						dtoSubCuentas[cont].getVerificacionDerechos().setHoraSolicitud(ingresoPacienteForm.getVariosConvenios("horaSolicitud_"+i).toString());
						dtoSubCuentas[cont].getVerificacionDerechos().setPersonaContactada(ingresoPacienteForm.getVariosConvenios("personaContactada_"+i).toString());
						dtoSubCuentas[cont].getVerificacionDerechos().setFechaVerificacion(ingresoPacienteForm.getVariosConvenios("fechaVerificacion_"+i).toString());
						dtoSubCuentas[cont].getVerificacionDerechos().setHoraVerificacion(ingresoPacienteForm.getVariosConvenios("horaVerificacion_"+i).toString());
						dtoSubCuentas[cont].getVerificacionDerechos().setPorcentajeCobertura(ingresoPacienteForm.getVariosConvenios("porcentajeCobertura_"+i).toString());
						dtoSubCuentas[cont].getVerificacionDerechos().setCuotaVerificacion(ingresoPacienteForm.getVariosConvenios("cuotaVerificacion_"+i).toString());
						dtoSubCuentas[cont].getVerificacionDerechos().setObservaciones(ingresoPacienteForm.getVariosConvenios("observaciones_"+i).toString());
						dtoSubCuentas[cont].getVerificacionDerechos().setLoginUsuario(usuarioActual.getLoginUsuario());
					}
					else if(!UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("existeConvenio_"+i).toString()))
						dtoSubCuentas[cont].setSubCuentaVerificacionDerechos(false);


					for(int j=0;j<Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numReqIngreso_"+i).toString());j++)
					{
						DtoRequsitosPaciente requisitos = new DtoRequsitosPaciente();
						requisitos.setCodigo(Integer.parseInt(ingresoPacienteForm.getVariosConvenios("codigoReqIngreso_"+i+"_"+j).toString()));
						requisitos.setDescripcion(ingresoPacienteForm.getVariosConvenios("descripcionReqIngreso_"+i+"_"+j).toString());
						requisitos.setTipo(ConstantesIntegridadDominio.acronimoIngreso);
						requisitos.setCumplido(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("cumplidoReqIngreso_"+i+"_"+j).toString()));

						dtoSubCuentas[cont].getRequisitosPaciente().add(requisitos);
					}
					for(int j=0;j<Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numReqEgreso_"+i).toString());j++)
					{
						DtoRequsitosPaciente requisitos = new DtoRequsitosPaciente();
						requisitos.setCodigo(Integer.parseInt(ingresoPacienteForm.getVariosConvenios("codigoReqEgreso_"+i+"_"+j).toString()));
						requisitos.setDescripcion(ingresoPacienteForm.getVariosConvenios("descripcionReqEgreso_"+i+"_"+j).toString());
						requisitos.setTipo(ConstantesIntegridadDominio.acronimoEgreso);
						requisitos.setCumplido(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("cumplidoReqEgreso_"+i+"_"+j).toString()));

						dtoSubCuentas[cont].getRequisitosPaciente().add(requisitos);
					}
					cont++;
				}

			}

			//****************REASIGNACI√ìN DE PRIORIDADES POR INGRESO DE CONVENIO POR DEFECTO*********************************************
			cont = dtoSubCuentas.length; //posicion del convenio de ultima prioridad
			if(posConvenioDefault!=ConstantesBD.codigoNuncaValido&&posConvenioDefault<cont)
			{
				//Se intercambia la prioridad del convenio default por la del convenio de ultima prioridad
				int prioridad = Utilidades.convertirAEntero(ingresoPacienteForm.getVariosConvenios("prioridadDefault")+"", true);


				if(prioridad<dtoSubCuentas[posConvenioDefault].getNroPrioridad())
				{
					dtoSubCuentas[posConvenioDefault].setNroPrioridad(prioridad);

					int posTemp = posConvenioDefault;
					int prioridadTemp = prioridad;


					for(int i=0;i<dtoSubCuentas.length;i++)
					{
						if(i!=posTemp&&prioridadTemp==dtoSubCuentas[i].getNroPrioridad())
						{
							prioridadTemp++;
							dtoSubCuentas[i].setNroPrioridad(prioridadTemp);
							posTemp = i;
						}
					}
				}
				else if(prioridad>dtoSubCuentas[posConvenioDefault].getNroPrioridad())
				{
					dtoSubCuentas[posConvenioDefault].setNroPrioridad(prioridad);

					int posTemp = posConvenioDefault;
					int prioridadTemp = prioridad;


					for(int i=(dtoSubCuentas.length-1);i>=0;i--)
					{
						if(i!=posTemp&&prioridadTemp==dtoSubCuentas[i].getNroPrioridad())
						{
							prioridadTemp--;
							dtoSubCuentas[i].setNroPrioridad(prioridadTemp);
							posTemp = i;
						}
					}
				}
			}
			//*********************************************************************************************************************

			//Se llena la verificacion de derechos
			//cuando es asocio no se llena la verificacion de derechos
			if((UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("ingresoVerificacionDerechos").toString())||
					UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("tieneVerificacionDerechos").toString()))
					&&!ingresoPacienteForm.getCuenta("codigoConvenio").toString().equals(""))
			{
				dtoSubCuentas[0].setSubCuentaVerificacionDerechos(true);

				//Si ten√≠a verificacion de derechos se a√±ade el id de la subCuenta
				if(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("tieneVerificacionDerechos").toString()))
				{

					if(ingresoPacienteForm.getCuenta("idSubCuenta")!=null)
						dtoSubCuentas[0].getVerificacionDerechos().setSubCuenta(ingresoPacienteForm.getCuenta("idSubCuenta" ).toString());

					//Si exist√≠a verificacion de derechos y se borrÔøΩ la informacion se debe eliminar
					if(!UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("ingresoVerificacionDerechos").toString()))
						dtoSubCuentas[0].getVerificacionDerechos().setEliminar(true);
				}

				dtoSubCuentas[0].getVerificacionDerechos().setCodigoEstado(ingresoPacienteForm.getVerificacion("codigoEstado").toString());
				dtoSubCuentas[0].getVerificacionDerechos().setCodigoTipo(ingresoPacienteForm.getVerificacion("codigoTipo").toString());
				dtoSubCuentas[0].getVerificacionDerechos().setNumero(ingresoPacienteForm.getVerificacion("numero").toString());
				dtoSubCuentas[0].getVerificacionDerechos().setPersonaSolicita(ingresoPacienteForm.getVerificacion("personaSolicita").toString());
				dtoSubCuentas[0].getVerificacionDerechos().setFechaSolicitud(ingresoPacienteForm.getVerificacion("fechaSolicitud").toString());
				dtoSubCuentas[0].getVerificacionDerechos().setHoraSolicitud(ingresoPacienteForm.getVerificacion("horaSolicitud").toString());
				dtoSubCuentas[0].getVerificacionDerechos().setPersonaContactada(ingresoPacienteForm.getVerificacion("personaContactada").toString());
				dtoSubCuentas[0].getVerificacionDerechos().setFechaVerificacion(ingresoPacienteForm.getVerificacion("fechaVerificacion").toString());
				dtoSubCuentas[0].getVerificacionDerechos().setHoraVerificacion(ingresoPacienteForm.getVerificacion("horaVerificacion").toString());
				dtoSubCuentas[0].getVerificacionDerechos().setPorcentajeCobertura(ingresoPacienteForm.getVerificacion("porcentajeCobertura").toString());
				dtoSubCuentas[0].getVerificacionDerechos().setCuotaVerificacion(ingresoPacienteForm.getVerificacion("cuotaVerificacion").toString());
				dtoSubCuentas[0].getVerificacionDerechos().setObservaciones(ingresoPacienteForm.getVerificacion("observaciones").toString());
				dtoSubCuentas[0].getVerificacionDerechos().setLoginUsuario(usuarioActual.getLoginUsuario());
			}
			else if(!UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esAsocio").toString())&&!ingresoPacienteForm.getCuenta("codigoConvenio").toString().equals(""))
				dtoSubCuentas[0].setSubCuentaVerificacionDerechos(false);

			//Se llena el responsable paciente
			if(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("ingresoResponsablePaciente").toString()))
			{
				logger.info("DEBO INGRESAR RESOPNSABLE PACIENTE!!!!!!!!!!!!!!!");
				dtoCuenta.setTieneResponsablePaciente(true);
				dtoCuenta.getResponsablePaciente().setCodigo(ingresoPacienteForm.getResponsable("codigo")==null?"":ingresoPacienteForm.getResponsable("codigo").toString());
				dtoCuenta.getResponsablePaciente().setTipoIdentificacion(ingresoPacienteForm.getResponsable("codigoTipoIdentificacion").toString());
				dtoCuenta.getResponsablePaciente().setNumeroIdentificacion(ingresoPacienteForm.getResponsable("numeroIdentificacion").toString());
				dtoCuenta.getResponsablePaciente().setCodigoPaisExpedicion(ingresoPacienteForm.getResponsable("paisExpedicion").toString());
				String[] vector = ingresoPacienteForm.getResponsable("ciudadExpedicion").toString().split(ConstantesBD.separadorSplit);
				if(vector.length>1)
				{
					dtoCuenta.getResponsablePaciente().setCodigoDeptoExpedicion(vector[0]);
					dtoCuenta.getResponsablePaciente().setCodigoCiudadExpedicion(vector[1]);
				}
				dtoCuenta.getResponsablePaciente().setPrimerApellido(ingresoPacienteForm.getResponsable("primerApellido").toString());
				dtoCuenta.getResponsablePaciente().setSegundoApellido(ingresoPacienteForm.getResponsable("segundoApellido").toString());
				dtoCuenta.getResponsablePaciente().setPrimerNombre(ingresoPacienteForm.getResponsable("primerNombre").toString());
				dtoCuenta.getResponsablePaciente().setSegundoNombre(ingresoPacienteForm.getResponsable("segundoNombre").toString());
				dtoCuenta.getResponsablePaciente().setDireccion(ingresoPacienteForm.getResponsable("direccion").toString());
				dtoCuenta.getResponsablePaciente().setCodigoPais(ingresoPacienteForm.getResponsable("pais").toString());
				vector = ingresoPacienteForm.getResponsable("ciudad").toString().split(ConstantesBD.separadorSplit);
				if(vector.length>1)
				{
					dtoCuenta.getResponsablePaciente().setCodigoDepto(vector[0]);
					dtoCuenta.getResponsablePaciente().setCodigoCiudad(vector[1]);
				}
				dtoCuenta.getResponsablePaciente().setCodigoBarrio(ingresoPacienteForm.getResponsable("codigoBarrio").toString());
				dtoCuenta.getResponsablePaciente().setTelefono(ingresoPacienteForm.getResponsable("telefono").toString());
				dtoCuenta.getResponsablePaciente().setFechaNacimiento(ingresoPacienteForm.getResponsable("fechaNacimiento").toString());
				dtoCuenta.getResponsablePaciente().setRelacionPaciente(ingresoPacienteForm.getResponsable("relacionPaciente").toString());
				dtoCuenta.getResponsablePaciente().setLoginUsuario(usuarioActual.getLoginUsuario());


			}
			else
				dtoCuenta.setTieneResponsablePaciente(false);

			HibernateUtil.endTransaction();
		} catch (Exception e) {
			Log4JManager.error("Guardando cuenta paciente", e);
			HibernateUtil.abortTransaction();
		}
		dtoCuenta.setConvenios(dtoSubCuentas);
	}




	/**
	 * M√©todo que realiza la validacion de campos al guardar la cuenta
	 * @param con 
	 * @param ingresoPacienteForm
	 * @param errores
	 * @param usuarioActual 
	 * @return
	 */
	private ActionErrors validacionGuardarCuenta(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionErrors errores, UsuarioBasico usuarioActual) 
	{
		
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazPaciente(usuarioActual.getCodigoInstitucionInt())))
		{
			//logger.info("------------------>"+ingresoPacienteForm.getCuenta("codigoConvenio").toString());
			int codInterfaz = Utilidades.convertirAEntero(ingresoPacienteForm.getCuenta("codigoConvenio").toString().split(ConstantesBD.separadorSplit)[0]);
			//logger.info("\n\n------------------VALIDAR INTERFAZ PACIENTE POR CODIGO CONVENIO--------->"+codInterfaz+"<--\n");
			String strconv = "";
			strconv=Utilidades.obtenerCodigoInterfazConvenioDeCodigo(codInterfaz);
			//logger.info("\n\nCODIGO INTERFAZ CONVENIO --->"+strconv+"<---\n");
			if(UtilidadTexto.isEmpty(strconv))
			{
				//logger.info("entra error interfz!!!!!!!");
				errores.add("",new ActionMessage("errors.required","Se tiene activa la interfaz Paciente, por √©sta razÔøΩn el -CÔøΩdigo Interfaz Convenio- "));
			}
		}
		
		//Inicialmente se verifica si el convenio default es el convenio principal
		boolean convenioDefaultEsPrincipal = false;
		if(!ingresoPacienteForm.getCuenta("codigoConvenioDefault").toString().equals("")&&Utilidades.convertirAEntero(ingresoPacienteForm.getVariosConvenios("prioridadDefault")+"", true)==1)
			convenioDefaultEsPrincipal = true;
		
		String aux0 = "", aux1 = "";
		//V√≠a de ingreso
		if(ingresoPacienteForm.getCuenta("codigoViaIngreso").toString().equals(""))
			errores.add("",new ActionMessage("errors.required","La vÌa de ingreso"));
		//Convenio
		if(ingresoPacienteForm.getCuenta("codigoConvenio").toString().equals("")&&!convenioDefaultEsPrincipal)
			errores.add("",new ActionMessage("errors.required","El convenio"));
		//Contrato
		if(ingresoPacienteForm.getCuenta("codigoContrato").toString().equals(""))
			if(!convenioDefaultEsPrincipal)
				errores.add("",new ActionMessage("errors.required","El contrato"));
			else
				ingresoPacienteForm.setCuenta("codigoConvenio", "");
		
		//Valor utilizado soat
		aux0 = ingresoPacienteForm.getCuenta("valorUtilizadoSoat").toString();
		if(!aux0.equals(""))
		{
			try
			{
				Double.parseDouble(aux0);
			}
			catch(Exception e)
			{
				errores.add("",new ActionMessage("errors.float","El campo valor utilizado SOAT"));
			}
		}
		
		//Fecha Afiliacion
		aux0 = ingresoPacienteForm.getCuenta("fechaAfiliacion").toString();
		if(!aux0.equals("")&&!UtilidadFecha.validarFecha(aux0))
			errores.add("",new ActionMessage("errors.formatoFechaInvalido","de afiliaciÛn"));
		
		//Semanas Cotizacion
		aux0 = ingresoPacienteForm.getCuenta("semanasCotizacion").toString();
		if(!aux0.equals("")&&Utilidades.convertirAEntero(aux0)==ConstantesBD.codigoNuncaValido)
			errores.add("",new ActionMessage("errors.float","El campo semanas de cotizaciÛn"));
		
		//Meses cotizacion
		aux0 = ingresoPacienteForm.getCuenta("mesesCotizacion").toString();
		if(!aux0.equals("")&&Utilidades.convertirAEntero(aux0)==ConstantesBD.codigoNuncaValido)
			errores.add("",new ActionMessage("errors.float","El campo meses de cotizaciÛn"));
		
		
		//Clasificacion socioeconomica
		if(Utilidades.convertirAEntero(ingresoPacienteForm.getCuenta("codigoContrato")+"")>0)
		{
			IValidacionTipoCobroPacienteServicio servicioValidacion=FacturacionServicioFabrica.crearValidacionTipoCobroPacienteServicio();
			DtoValidacionTipoCobroPaciente validacion=servicioValidacion.validarTipoCobroPacienteServicioConvenioContrato(Utilidades.convertirAEntero(ingresoPacienteForm.getCuenta("codigoContrato")+""));
			
			if(UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion()))
			{
				if(ingresoPacienteForm.getCuenta("codigoEstratoSocial").toString().equals(""))
					if(!convenioDefaultEsPrincipal)
						errores.add("",new ActionMessage("errors.required","La clasificaciÛn socioeconÛmica"));
					else
						ingresoPacienteForm.setCuenta("codigoConvenio", "");
			}
			
			//Tipo Afiliado
			if(UtilidadTexto.getBoolean(validacion.getMostrarTipoAfiliado()))
			{
				if(ingresoPacienteForm.getCuenta("codigoTipoAfiliado").toString().equals(""))
					if(!convenioDefaultEsPrincipal)
						errores.add("",new ActionMessage("errors.required","el tipo afiliado"));
					else
						ingresoPacienteForm.setCuenta("codigoConvenio", "");
				//Monto Cobro
			}
			if(UtilidadTexto.getBoolean(validacion.getManejaMontos()))
			{
				if(ingresoPacienteForm.getCuenta("codigoMontoCobro").toString().equals(""))
					if(!convenioDefaultEsPrincipal)
						errores.add("",new ActionMessage("errors.required","el monto de cobro"));
					else
						ingresoPacienteForm.setCuenta("codigoConvenio", "");
			}
			
			if(UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion())&&Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales("numRegistros")+"")<=0
					&&UtilidadTexto.getBoolean(validacion.getMostrarTipoAfiliado())&&Utilidades.convertirAEntero(ingresoPacienteForm.getTiposAfiliado("numRegistros")+"")<=0
					&&UtilidadTexto.getBoolean(validacion.getMostrarNaturalezaPaciente())&&ingresoPacienteForm.getNaturalezasPaciente().size()<=0)
			{
				errores.add("",new ActionMessage("errors.notEspecific","No existe parametrizaciÛn de ClasificaciÛn Socio-EconÛmica, Tipo de Afiliado y/o Naturaleza de Paciente para el Convenio responsable, vÌa de Ingreso y vigencia de acuerdo a la informaciÛn de Montos de Cobro. Por favor verifique."));
			}
			
		}
		
		//Tipo de regimen
		if(ingresoPacienteForm.getCuenta("codigoTipoRegimen")==null||ingresoPacienteForm.getCuenta("codigoTipoRegimen").toString().equals(""))
			if(!convenioDefaultEsPrincipal)
				errores.add("",new ActionMessage("errors.required","el tipo de rÈgimen"));
			else
				ingresoPacienteForm.setCuenta("codigoConvenio", "");
		
		logger.info("\n Cod Convenio: "+ingresoPacienteForm.getCuenta("codigoConvenio").toString()+"  COD_cobertura : "+ingresoPacienteForm.getCuenta("codigoTipoCobertura").toString()+" Es reporteAtencion  "+ingresoPacienteForm.getCuenta("esReporteAtencionInicialUrgencias")+"");
		if(!ingresoPacienteForm.getCuenta("codigoConvenio").toString().equals("")&&
			ingresoPacienteForm.getCuenta("codigoTipoCobertura").toString().equals("") && ( (ingresoPacienteForm.getCuenta("esReporteAtencionInicialUrgencias") == null) || UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esReporteAtencionInicialUrgencias")+"") ))
		{
			errores.add("",new ActionMessage("errors.required","El tipo de cobertura"));
		}
		
		//Tipo de Paciente
		if(ingresoPacienteForm.getCuenta("codigoTipoPaciente")==null||ingresoPacienteForm.getCuenta("codigoTipoPaciente").toString().equals(""))
			errores.add("",new ActionMessage("errors.required","el tipo de paciente"));
		//N√∫mero de Carnet
		if(ingresoPacienteForm.getCuenta("requiereCarnet")!=null&&
			UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("requiereCarnet").toString())&&
			ingresoPacienteForm.getCuenta("numeroCarnet").toString().equals(""))
			if(!convenioDefaultEsPrincipal)
				errores.add("",new ActionMessage("errors.required","el n˙mero de carnet"));
			else
				ingresoPacienteForm.setCuenta("codigoConvenio", "");
		//Origen Admision
		if(ingresoPacienteForm.getCuenta("codigoOrigenAdmision").toString().equals(""))
			errores.add("",new ActionMessage("errors.required","el origen de la admisiÛn"));
		//√°rea
		if(ingresoPacienteForm.getCuenta("codigoArea").toString().equals(""))
			errores.add("",new ActionMessage("errors.required","el ·rea"));
		
		if(ingresoPacienteForm.getCuenta().containsKey("requiereTipoM") && ingresoPacienteForm.getCuenta("requiereTipoM").toString().equals("S"))
		{
			if(ingresoPacienteForm.getCuenta("codigoTipoMonitoreo").toString().equals("-1"))
				errores.add("",new ActionMessage("errors.required","El Tipo de Monitoreo "));
		}
		
		//Validaciones propias de la v√≠a de ingreso URGENCIAS
		if(ingresoPacienteForm.getCuenta("codigoViaIngreso").toString().equals(ConstantesBD.codigoViaIngresoUrgencias+"")||
			UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("hospitalDia").toString()))
		{
			
			//Validar la fecha de admision
			boolean fechaValida = true;
			String fechaMesAtras = UtilidadFecha.incrementarMesesAFecha(UtilidadFecha.getFechaActual(), -1, false);
			
			aux0 = ingresoPacienteForm.getCuenta("fechaAdmision").toString();
			if(aux0.equals(""))
			{
				errores.add("",new ActionMessage("errors.required","la fecha de admisiÛn"));
				fechaValida = false;
			}
			else
			{
				if(!UtilidadFecha.validarFecha(aux0))
				{
					errores.add("",new ActionMessage("errors.formatoFechaInvalido","de admisiÛn"));
					fechaValida = false;
				}
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(aux0, UtilidadFecha.getFechaActual()))
				{
					errores.add("",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de admisiÛn", "actual"));
					fechaValida = false;
				}
				else if(UtilidadFecha.esFechaMenorQueOtraReferencia(aux0, fechaMesAtras))
				{
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual","de admisiÛn ("+aux0+")",fechaMesAtras+" (un mes hacia atr√°s de la fecha actual)"));
					fechaValida = false;
				}
			}
			
			//Validar la hora de admisiÔøΩn
			aux1 = ingresoPacienteForm.getCuenta("horaAdmision").toString();
			if(aux1.equals(""))
				errores.add("",new ActionMessage("errors.required","la hora de admisiÔøΩn"));
			else
			{
				if(!UtilidadFecha.validacionHora(aux1).puedoSeguir)
					errores.add("",new ActionMessage("errors.formatoHoraInvalido","de admisiÔøΩn"));
				else if(fechaValida&&!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), aux0, aux1).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraPosteriorIgualActual", "de admisiÔøΩn", "actual"));
				else if(fechaValida&&!UtilidadFecha.compararFechas(aux0, aux1,fechaMesAtras, UtilidadFecha.getHoraActual()).isTrue())
					errores.add("", new ActionMessage("errors.fechaHoraAnteriorIgualActual","de admisiÔøΩn ("+aux0+" - "+aux1+")",fechaMesAtras+" - "+UtilidadFecha.getHoraActual()+" (un m√©s hacia atr√°s de la fecha actual)"));
				
					
			}
		}
		
		//Validacion de que debe ser numerico el numero de solicitud volante si se ingresÔøΩ
		if(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esConvenioColsanitas")+""))
			if(!ingresoPacienteForm.getCuenta("numeroSolicitudVolante").toString().equals(""))
			{
				try
				{
					Long.parseLong(ingresoPacienteForm.getCuenta("numeroSolicitudVolante").toString());
				}
				catch(Exception e)
				{
					logger.warn("Error al tratar de convertir a num√©rico el numero de solicitud volante: "+e);
					errores.add("",new ActionMessage("errors.integer","El N¬∞ Solicitud Volante"));
				}
			}
		
		//Validaciones propias de INFORMACION POLIZA (cuando es asocio no se valida)
		if(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esConvenioPoliza").toString())&&
			!UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esAsocio").toString()))
		{
			//Apellidos del titular
			if(ingresoPacienteForm.getCuenta("apellidosPoliza").toString().equals(""))
				errores.add("Apellidos poliza requerido",new ActionMessage("errors.required","El apellido del titular"));
			//Nombres del titular
			if(ingresoPacienteForm.getCuenta("nombresPoliza").toString().equals(""))
				errores.add("Nombres poliza requerido",new ActionMessage("errors.required","El nombre del titular"));
			//Tipo Id. titular
			if(ingresoPacienteForm.getCuenta("tipoIdPoliza").toString().equals(""))
				errores.add("Tipo ID poliza requerido",new ActionMessage("errors.required","El tipo de identificaciÔøΩn del titular"));
			//N√∫mero Id. titular
			if(ingresoPacienteForm.getCuenta("numeroIdPoliza").toString().equals(""))
				errores.add("Numero ID poliza requerido",new ActionMessage("errors.required","El n√∫mero de identificaciÔøΩn del titular"));
			//Direccion Titular
			if(ingresoPacienteForm.getCuenta("direccionPoliza").toString().equals(""))
				errores.add("direccion poliza requerido",new ActionMessage("errors.required","La direcciÔøΩn del titular"));
			//Autorizacion Poliza
			if(ingresoPacienteForm.getCuenta("autorizacionPoliza").toString().equals(""))
				errores.add("direccion poliza requerido",new ActionMessage("errors.required","El n√∫mero de autorizaciÔøΩn de la pÔøΩliza"));
			//valor Poliza
			if(ingresoPacienteForm.getCuenta("valorPoliza").toString().equals(""))
				errores.add("valor poliza requerido",new ActionMessage("errors.required","El valor para el monto de la pÔøΩliza"));
			else
			{
				try
				{
					if(Double.parseDouble(ingresoPacienteForm.getCuenta("valorPoliza").toString())<=0)
						errores.add("Debe ser num√©rico", new ActionMessage("errors.MayorQue","El valor para el monto de la pÔøΩliza","0"));
				}
				catch(Exception e)
				{
					errores.add("Debe ser num√©rico", new ActionMessage("errors.float","El valor para el monto de la pÔøΩliza"));
				}
			}
		}
		
		//************************VALIDACION CONVENIO DEFAULT***************************************************
		//verificar que se haya seleccionado el convenio por default de la v√≠a de ingreso
		logger.info("codigo convenio default: "+ingresoPacienteForm.getCuenta("codigoConvenioDefault"));
		if(!ingresoPacienteForm.getCuenta("codigoConvenioDefault").toString().equals(""))
		{
			boolean seleccionado = false;
			
			String codigoConvenioDefault = ingresoPacienteForm.getCuenta("codigoConvenioDefault").toString();
			
			//Se verifica si el convenio por defecto se seleccionÔøΩ en el convenio principal
			if(codigoConvenioDefault.equals(ingresoPacienteForm.getCuenta("codigoConvenio").toString().split(ConstantesBD.separadorSplit)[0]))
				seleccionado = true;
				
			logger.info("fueSeleccionado N¬∞ 1? => "+seleccionado);
				
			
			
			
			//Se verifica si el convenio se seleccionÔøΩ en los otros convenios
			if(!seleccionado)
			{
				for(int i=0;i<Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numRegistros").toString());i++)
				{
					if(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i)!=null&&
						codigoConvenioDefault.equals(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i).toString()))
						seleccionado=true;
					/*if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazPaciente(usuarioActual.getCodigoInstitucionInt())))
					{
						if(Utilidades.obtenerCodigoInterfazConvenioDeCodigo(Utilidades.convertirAEntero(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i)+"")).equals(""))
						{
							interfaz=true;
						}
					}*/
				}
			}
			
			if(!seleccionado)
			{
				errores.add("",new ActionMessage("errors.required","El convenio "+Utilidades.obtenerNombreConvenioOriginal(con, Integer.parseInt(codigoConvenioDefault))));
			}
			//Si ya fue seleccionado  y no existe en la base de datos se verifica su prioridad
			else if(!UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esAsocio").toString()))
			{
				//Se revisa que se haya ingresao prioridad
				if(UtilidadCadena.noEsVacio(ingresoPacienteForm.getVariosConvenios("prioridadDefault")+""))
				{
					int nroPrioridad = Utilidades.convertirAEntero(ingresoPacienteForm.getVariosConvenios("prioridadDefault").toString(), true);
					int totalConvenios = 1 + ingresoPacienteForm.getNumConveniosDefinitivos();
					
					if(nroPrioridad<=0||nroPrioridad>totalConvenios)
						errores.add("",new ActionMessage("errors.range","El N¬∞ prioridad del convenio por defecto","1",totalConvenios+""));
					
					
				}
				else
					errores.add("",new ActionMessage("errors.required","El campo prioridad del convenio "+Utilidades.obtenerNombreConvenioOriginal(con, Integer.parseInt(codigoConvenioDefault))));
				
			}
		}
		//***********************************************************************************
		
		//************************VALIDACIONES CONVENIOS ADICIONALES**************************************************************
		for(int i=0;i<Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numRegistros").toString());i++)
		{
			logger.info("***************PASO POR AQUI EN VSLAIDACIONES DE CONVENIO ADICIONALES!! ************************");
			logger.info("codigoConvenio["+i+"] "+ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i));
			logger.info("existeConvenio["+i+"] "+ingresoPacienteForm.getVariosConvenios("existeConvenio_"+i));
			logger.info("codigoViaIngresp["+i+"] "+ingresoPacienteForm.getVariosConvenios("codigoViaIngreso_"+i));
			logger.info("codigoViaIngresoCuenta "+ingresoPacienteForm.getCuenta("codigoViaIngreso"));
			///*****SE VERIFICA QUE TODOS LOS CONVENIOS TENGAN LA INFORMACION DE MONTOS CON LA MISMA VIA DE INGRESO*************+
			if(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i)!=null&&
				!UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("existeConvenio_"+i).toString())&&
				!UtilidadTexto.isEmpty(ingresoPacienteForm.getVariosConvenios("codigoViaIngreso_"+i).toString())&&
				!ingresoPacienteForm.getVariosConvenios("codigoViaIngreso_"+i).toString().equals(ingresoPacienteForm.getCuenta("codigoViaIngreso").toString()))
				errores.add("Error validacion montos X via ingreso",
					new ActionMessage(
						"errors.notEspecific",
						"El convenio "+ingresoPacienteForm.getVariosConvenios("nombreConvenio_"+i)+" tiene informaciÔøΩn de montos cobro diferente a la v√≠a de ingreso seleccionada. Por favor verifique"));
			//*******************************************************************************************************************
			//********SE VALIDA QUE SE HAYA PARAMETRIZADO INFORMACION DE LOS CONVENIOS ADICIONALES******************************
			/*
			if(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i)!=null&&
				!UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("existeConvenio_"+i).toString())&&
				!ingresoPacienteForm.getVariosConvenios("codigoViaIngreso_"+i).toString().equals(""))
				errores.add("Error convenio con info incompleta",
						new ActionMessage(
							"errors.notEspecific",
							"El convenio "+ingresoPacienteForm.getVariosConvenios("nombreConvenio_"+i)+" tiene informaciÔøΩn incompleta. Por favor verifique"));
			*/
			if(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i)!=null&&
					!UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("existeConvenio_"+i).toString())&&
					UtilidadTexto.isEmpty(ingresoPacienteForm.getVariosConvenios("codigoViaIngreso_"+i).toString()))
					errores.add("Error convenio con info incompleta",
							new ActionMessage(
								"errors.notEspecific",
								"Es requerido ingresar informaciÔøΩn para el convenio "+ingresoPacienteForm.getVariosConvenios("nombreConvenio_"+i)+" en la seccion (Otros Convenios)."));
			//*******************************************************************************************************************
				
		}
		//*******VALIDACIONES SECCION VERIFICACION DE DERECHOS**************************************************************
		//1) se verifica si la verificacion de derechos es requerida
		boolean esRequeridoVerificacion = false;
		if(!ingresoPacienteForm.getCuenta("codigoViaIngreso").toString().equals("")&&
				UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esViaIngresoVerificacion")+"")&&
				UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esConvenioVerificacion")+""))
			esRequeridoVerificacion = true;
		
		//2) Se verifica si se ingresÔøΩ informaciÔøΩn requerida de la verificacion de derechos
		boolean seIngresoVerificacion = false;
		if(!esRequeridoVerificacion)
		{
			//Si se editÔøΩ informacion en el estado y tipo de verificacion , quiere decir que se tuvo la intenciÔøΩn
			//de ingresar verificacion de derechos
			if(!ingresoPacienteForm.getVerificacion("codigoEstado").toString().equals("")||
				!ingresoPacienteForm.getVerificacion("codigoTipo").toString().equals(""))
				seIngresoVerificacion = true;
		}
		
		//Si la verificacion de derechos es requerida o sin ser requerida se intentÔøΩ ingresar,
		//se prosigue a realizar sus valdiaciones
		if(esRequeridoVerificacion||seIngresoVerificacion)
		{
			ingresoPacienteForm.setCuenta("ingresoVerificacionDerechos", ConstantesBD.acronimoSi);
			
			//estado de verificacion
			if(ingresoPacienteForm.getVerificacion("codigoEstado").toString().equals(""))
				errores.add("El estado es requerido",new ActionMessage("errors.required","El estado (VerificaciÔøΩn de Derechos)"));
			//tipo de verificacion
			if(ingresoPacienteForm.getVerificacion("codigoTipo").toString().equals(""))
				errores.add("El tipo es requerido", new ActionMessage("errors.required","El tipo (VerificaciÔøΩn de Derechos)"));
			//persona solicita
			if(ingresoPacienteForm.getVerificacion("personaSolicita").toString().equals(""))
				errores.add("la persona solicita es requerido", new ActionMessage("errors.required","Persona Solicita (VerificaciÔøΩn de Derechos)"));
			//persona contactada
			if(!ingresoPacienteForm.getVerificacion("codigoEstado").toString().equals("")&&
				!ingresoPacienteForm.getVerificacion("codigoEstado").toString().equals(ConstantesIntegridadDominio.acronimoEstadoPendientePorVerificar)&&
				ingresoPacienteForm.getVerificacion("personaContactada").toString().equals(""))
				errores.add("la persona contactada es requerido", new ActionMessage("errors.required","Persona contactada (VerificaciÔøΩn de Derechos)"));
			
			//Validar la fecha de verificacion
			boolean fechaValida = true;
			aux0 = ingresoPacienteForm.getVerificacion("fechaVerificacion").toString();
			if(aux0.equals(""))
			{
				errores.add("",new ActionMessage("errors.required","la fecha verificaciÔøΩn (VerificaciÔøΩn de Derechos)"));
				fechaValida = false;
			}
			else
			{
				if(!UtilidadFecha.validarFecha(aux0))
				{
					errores.add("",new ActionMessage("errors.formatoFechaInvalido","de verificaciÔøΩn (VerificaciÔøΩn de Derechos)"));
					fechaValida = false;
				}
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(aux0, UtilidadFecha.getFechaActual()))
				{
					errores.add("",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de verificaciÔøΩn (VerificaciÔøΩn de Derechos)", "actual"));
					fechaValida = false;
				}
			}
			
			//Validar la hora de verificaciÔøΩn
			aux1 = ingresoPacienteForm.getVerificacion("horaVerificacion").toString();
			if(aux1.equals(""))
				errores.add("",new ActionMessage("errors.required","la hora de verificaciÔøΩn (VerificaciÔøΩn de Derechos)"));
			else
			{
				if(!UtilidadFecha.validacionHora(aux1).puedoSeguir)
					errores.add("",new ActionMessage("errors.formatoHoraInvalido","de verificaciÔøΩn (VerificaciÔøΩn de Derechos)"));
				else if(fechaValida&&!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), aux0, aux1).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraPosteriorIgualActual", "de verificaciÔøΩn (VerificaciÔøΩn de Derechos)", "actual"));
					
			}
			
			//Validacion del porcentaje de cobertura
			if(!ingresoPacienteForm.getVerificacion("porcentajeCobertura").toString().equals(""))
			{
				try
				{
					if(Double.parseDouble(ingresoPacienteForm.getVerificacion("porcentajeCobertura").toString())>100)
						errores.add("",new ActionMessage("errors.MenorIgualQue","El porcentaje de cobertura (VerificaciÔøΩn de Derechos)","100%"));
				}
				catch(Exception e)
				{
					errores.add("",new ActionMessage("errors.float","El porcentaje de cobertura (VerificaciÔøΩn de Derechos)"));
				}
				
			}
		}
		else
			ingresoPacienteForm.setCuenta("ingresoVerificacionDerechos", ConstantesBD.acronimoNo);
		//***********************************************************************************************************
		//*****************VALIDACIONES SECCION RESPONSABLE PACIENTE*************************************************
		//Solo es requerido si se intentÔøΩ ingresar el responsable
		if(ingresoPacienteForm.getResponsable("codigoTipoIdentificacion")!=null&&!ingresoPacienteForm.getResponsable("codigoTipoIdentificacion").toString().equals("")&&
					ingresoPacienteForm.getResponsable("numeroIdentificacion")!=null&&!ingresoPacienteForm.getResponsable("numeroIdentificacion").toString().equals(""))
		{
			logger.info("SÔøΩ DEBO VALIDAR RESPONSABLE PACIENTE!!!!!!!!!!!!!!!!!!");
			ingresoPacienteForm.setCuenta("ingresoResponsablePaciente", ConstantesBD.acronimoSi);
			
			if(ingresoPacienteForm.getResponsable("primerApellido").toString().equals(""))
				errores.add("",new ActionMessage("errors.required","El primer apellido (Responsable Paciente)"));
			
			if(ingresoPacienteForm.getResponsable("primerNombre").toString().equals(""))
				errores.add("",new ActionMessage("errors.required","El primer nombre (Responsable Paciente)"));
			
			if(ingresoPacienteForm.getResponsable("telefono").toString().equals(""))
				errores.add("",new ActionMessage("errors.required","El telÔøΩfono (Responsable Paciente)"));
			
			if(ingresoPacienteForm.getResponsable("fechaNacimiento").toString().equals(""))
				errores.add("",new ActionMessage("errors.required","La fecha de nacimiento (Responsable Paciente)"));
			else
			{
				aux0 = ingresoPacienteForm.getResponsable("fechaNacimiento").toString();
				if(!UtilidadFecha.validarFecha(aux0))
					errores.add("",new ActionMessage("errors.formatoFechaInvalido","de nacimiento (Responsable Paciente)"));
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(aux0, UtilidadFecha.getFechaActual()))
					errores.add("",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de nacimiento (Responsable Paciente)", "actual"));
				else
				{
					boolean validarEdadResponsable = UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarEdadResponsablePaciente(usuarioActual.getCodigoInstitucionInt()));
					if(validarEdadResponsable)
					{
						int aniosBase = 0;
						try
						{
							aniosBase = Integer.parseInt(ValoresPorDefecto.getAniosBaseEdadAdulta(usuarioActual.getCodigoInstitucionInt()));
						}
						catch (Exception e) 
						{
							aniosBase = 0;
							logger.error("Error al tomar el parÔøΩmetro AÔøΩOS BASE EDAD ADULTA: "+e);
						}
						if(aniosBase>0)
						{
							int edad = UtilidadFecha.calcularEdad(aux0);
							if(edad<aniosBase)
								errores.add("",new ActionMessage("errors.debeSerNumeroMayorIgual","La edad del responsable paciente ("+edad+" aÔøΩos)",aniosBase+" aÔøΩos"));
						}
					}
				}
				
			}
			
			if(ingresoPacienteForm.getResponsable("relacionPaciente").toString().equals(""))
				errores.add("",new ActionMessage("errors.required","La relaciÔøΩn con el paciente (Responsable Paciente)"));
			
			//Si el responsable ya existÔøΩa se verifica si ya fue modificado
			if(UtilidadTexto.getBoolean(ingresoPacienteForm.getResponsable("existe").toString()))
			{
				DtoResponsablePaciente responsableOriginal = UtilidadesManejoPaciente.cargarResponsablePaciente(
					con, 
					ingresoPacienteForm.getResponsable("codigoTipoIdentificacion").toString(), 
					ingresoPacienteForm.getResponsable("numeroIdentificacion").toString());
				ingresoPacienteForm.setFueModificadoResponsable(huboModificacionResponsablePaciente(responsableOriginal,ingresoPacienteForm.getResponsable()));
			}
			else
				ingresoPacienteForm.setFueModificadoResponsable(false);
		}
		else
			ingresoPacienteForm.setCuenta("ingresoResponsablePaciente", ConstantesBD.acronimoNo);
		
		//************************************************************************************************************
		logger.info("errores is empty-->"+errores.isEmpty());
		
		return errores;
	}




	/**
	 * MÔøΩtodo que verifica si el responsable paciente fue modificado
	 * @param responsableOriginal
	 * @param responsable
	 * @return
	 */
	private boolean huboModificacionResponsablePaciente(DtoResponsablePaciente responsableOriginal, HashMap<String, Object> responsable) 
	{
		boolean modificado = false;
		
		//Se modificÔøΩ pais de expedicion?
		if(!responsable.get("paisExpedicion").toString().equals(responsableOriginal.getCodigoPaisExpedicion()))
			modificado = true;
		//Se modificÔøΩ ciudad de expedicion ?
		if(!responsable.get("ciudadExpedicion").toString().equals(responsableOriginal.getCodigoDeptoExpedicion()+ConstantesBD.separadorSplit+responsableOriginal.getCodigoCiudadExpedicion()))
			modificado = true;
		//Se modificÔøΩ el nombre o apellido ?
		if(!responsable.get("primerApellido").toString().equals(responsableOriginal.getPrimerApellido())||
			!responsable.get("segundoApellido").toString().equals(responsableOriginal.getSegundoApellido())||
			!responsable.get("primerNombre").toString().equals(responsableOriginal.getPrimerNombre())||
			!responsable.get("segundoNombre").toString().equals(responsableOriginal.getSegundoNombre()))
			modificado = true;
		//Se modificÔøΩ la direccion ?
		if(!responsable.get("direccion").toString().equals(responsableOriginal.getDireccion()))
			modificado = true;
		
		//Se modifcicÔøΩ el pais ?
		if(!responsable.get("pais").toString().equals(responsableOriginal.getCodigoPais()))
			modificado = true;
		//se modificÔøΩ la ciudad ?
		if(!responsable.get("ciudad").toString().equals(responsableOriginal.getCodigoDepto()+ConstantesBD.separadorSplit+responsableOriginal.getCodigoCiudad()))
			modificado = true;
		//se modificÔøΩ el barrio
		if(!responsable.get("codigoBarrio").toString().equals(responsableOriginal.getCodigoBarrio()))
			modificado = true;
		//se modifcÔøΩ el telÔøΩfono ?
		if(!responsable.get("telefono").toString().equals(responsableOriginal.getTelefono()))
			modificado = true;
		//se modificÔøΩ la fecha de nacimiento ?
		if(!responsable.get("fechaNacimiento").toString().equals(responsableOriginal.getFechaNacimiento()))
			modificado = true;
		//se modificÔøΩla relacion del paciente ?
		if(!responsable.get("relacionPaciente").toString().equals(responsableOriginal.getRelacionPaciente()))
			modificado = true;
		
		return modificado;
	}



	/**
	 * MÔøΩtodo que efectÔøΩa la eliminaciÔøΩn de un convenio borrÔøΩndolo del mapa
	 * @param con
	 * @param ingresoPacienteForm
	 * @param response 
	 * @return
	 */
	private ActionForward accionFiltroEliminarConvenio(Connection con, IngresoPacienteForm ingresoPacienteForm, HttpServletResponse response) 
	{
		logger.info("codigo convenio litsado: *"+ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+ingresoPacienteForm.getPosConvenio())+"*");
		logger.info("codigo convenio default: *"+ingresoPacienteForm.getCuenta("codigoConvenioDefault")+"*");
		
		//si el convenio es el convenio por defecto se elimina
		if(!UtilidadTexto.isEmpty(ingresoPacienteForm.getCuenta("codigoConvenioDefault")+"")&&
			ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+ingresoPacienteForm.getPosConvenio()).toString().equals(ingresoPacienteForm.getCuenta("codigoConvenioDefault").toString()))
			ingresoPacienteForm.setCuenta("codigoConvenioDefault", "");
		
		logger.info("codigo convenio default despues de eliminar: *"+ingresoPacienteForm.getCuenta("codigoConvenioDefault")+"*");
		
		//Se elimina el convenio
		ingresoPacienteForm.setVariosConvenios("codigoConvenio_"+ingresoPacienteForm.getPosConvenio(), null);
		
		//Se verifica si se maneja complejidad
		ingresoPacienteForm.setCuenta("indicadorManejaComplejidad", validacionManejoComplejidad(ingresoPacienteForm));
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write("<posicion>"+ingresoPacienteForm.getPosConvenio()+"</posicion>");
	        response.getWriter().write("<indicativo-complejidad>"+ingresoPacienteForm.getCuenta("indicadorManejaComplejidad")+"</indicativo-complejidad>");
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroFechaAfiliacion: "+e);
		}
		return null;
	}
	
	
	
	
	/**
	 * Metodo para Filtrar los Tipos de Monitoreo por Area Seleccionada
	 * @param con
	 * @param ingresoPacienteForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroTiposMonitoreo(Connection con, IngresoPacienteForm ingresoPacienteForm, HttpServletResponse response, PersonaBasica paciente) 
	{
		ingresoPacienteForm.setCuenta("requiereTipoM", "N");
		String respuesta = "<respuesta>";
		if(Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso())==ConstantesBD.codigoViaIngresoHospitalizacion && ingresoPacienteForm.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
		{
			if(!paciente.getExisteAsocio() || 
					(paciente.getExisteAsocio() && 
							UtilidadesHistoriaClinica.obtenerCodigoConductaValoracionUrgenciasCuenta(con, paciente.getCodigoCuenta()+"") != ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial &&
							Utilidades.obtenerDestinoSalidaEgresoEvolucion(con,paciente.getCodigoCuenta()) != ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial))
			{
				ingresoPacienteForm.setTiposMonitoreoMap(Utilidades.consultaTipoMonitoreoxCC(con, Utilidades.convertirAEntero(ingresoPacienteForm.getAreaSel())));
				
				if(Integer.parseInt(ingresoPacienteForm.getTiposMonitoreoMap("numRegistros").toString())==0)
				{
					respuesta += "No se encontraron tipos de monitoreo";
					ingresoPacienteForm.setCuenta("requiereTipoM", "N");
				}
				else
					ingresoPacienteForm.setCuenta("requiereTipoM", "S");
				
				for(int i=0;i<Integer.parseInt(ingresoPacienteForm.getTiposMonitoreoMap("numRegistros").toString());i++)
				{
					respuesta +="<tipom><codigo>"+ingresoPacienteForm.getTiposMonitoreoMap("codigo_"+i).toString()+"</codigo>" +
								"<nombre>"+ingresoPacienteForm.getTiposMonitoreoMap("nombre_"+i).toString()+"</nombre></tipom>";
				}
			}
		}
		else
		{
			respuesta += "No se encontraron tipos de monitoreo";
			ingresoPacienteForm.setCuenta("requiereTipoM", "N");
		}
		
		respuesta += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write(respuesta);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroTiposMonitoreo: "+e);
		}
		return null;
	}




	/**
	 * MÔøΩtodo implementado para adicionar un nuevo convenio al listado de la pÔøΩgina de la cuenta
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarConvenioAdicional(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		//***************VALIDACION DE CAMPOS*********************************************
		errores = validacionesGuardarConvenioAdicional(ingresoPacienteForm,errores);
		//*********************************************************************************
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			ingresoPacienteForm.setEstado("");
		}
		
		//si el convenio era nuevo, se suma en 1 el nÔøΩmero de convenios
		ingresoPacienteForm.setNumConvenios(ingresoPacienteForm.getNumConvenios()+1);
		ingresoPacienteForm.setVariosConvenios("numRegistros", ingresoPacienteForm.getNumConvenios());
		
		//se ejecuta la validacion del manejo de complejidad
		ingresoPacienteForm.setCuenta("indicadorManejaComplejidad", validacionManejoComplejidad(ingresoPacienteForm));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingresarConvenioAdicional");
	}



	/**
	 * MÔøΩtodo implementado para realizar las validaciones del convenio adicional
	 * @param ingresoPacienteForm
	 * @param errores
	 * @return
	 */
	private ActionErrors validacionesGuardarConvenioAdicional(IngresoPacienteForm ingresoPacienteForm, ActionErrors errores) 
	{
		int pos = ingresoPacienteForm.getPosConvenio();
		
		//Contrato
		if(ingresoPacienteForm.getVariosConvenios("codigoContrato_"+pos).toString().equals(""))
			errores.add("Contrato es requerido",new ActionMessage("errors.required","El contrato"));
		
		//Valor utilizado soat
		String aux0 = ingresoPacienteForm.getVariosConvenios("valorUtilizadoSoat_"+pos).toString();
		if(!aux0.equals(""))
		{
			try
			{
				Double.parseDouble(aux0);
			}
			catch(Exception e)
			{
				errores.add("",new ActionMessage("errors.float","El campo valor utilizado SOAT"));
			}
		}
		
		//Fecha Afiliacion
		aux0 = ingresoPacienteForm.getVariosConvenios("fechaAfiliacion_"+pos).toString();
		if(!aux0.equals("")&&!UtilidadFecha.validarFecha(aux0))
			errores.add("",new ActionMessage("errors.formatoFechaInvalido","de afiliaci&oacute;n"));
		
		//Semanas Cotizacion
		aux0 = ingresoPacienteForm.getVariosConvenios("semanasCotizacion_"+pos).toString();
		if(!aux0.equals("")&&Utilidades.convertirAEntero(aux0)==ConstantesBD.codigoNuncaValido)
			errores.add("",new ActionMessage("errors.float","El campo semanas de cotizaci&oacute;n"));
		//Meses cotizacion
		aux0 = ingresoPacienteForm.getVariosConvenios("mesesCotizacion_"+pos).toString();
		if(!aux0.equals("")&&Utilidades.convertirAEntero(aux0)==ConstantesBD.codigoNuncaValido)
			errores.add("",new ActionMessage("errors.float","El campo meses de cotizaci&oacute;n"));
		
		if(!ingresoPacienteForm.getVariosConvenios("codigoContrato_"+pos).toString().equals(""))
		{	
			IValidacionTipoCobroPacienteServicio servicioValidacion=FacturacionServicioFabrica.crearValidacionTipoCobroPacienteServicio();
			DtoValidacionTipoCobroPaciente validacion=servicioValidacion.validarTipoCobroPacienteServicioConvenioContrato(Utilidades.convertirAEntero(ingresoPacienteForm.getVariosConvenios("codigoContrato_"+pos)+""));
			if(UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion()))
			{
				//Clasificacion socioec
				if(ingresoPacienteForm.getVariosConvenios("codigoEstratoSocial_"+pos).toString().equals(""))
					errores.add("Clasificacion es requerido",new ActionMessage("errors.required","La clasificaci&oacute;n socioeconÔøΩmica"));
			}
			
			//Tipo Afiliado
			if(UtilidadTexto.getBoolean(validacion.getMostrarTipoAfiliado()))
			{
				//Tipo Afiliado
				if(ingresoPacienteForm.getVariosConvenios("codigoTipoAfiliado_"+pos).toString().equals(""))
					errores.add("Tipo Afiliado",new ActionMessage("errors.required","El tipo de afiliado"));
			}
			if(UtilidadTexto.getBoolean(validacion.getManejaMontos()))
			{
				//Monto de Cobro
				if(ingresoPacienteForm.getVariosConvenios("codigoMontoCobro_"+pos).toString().equals(""))
					errores.add("Monto Cobro",new ActionMessage("errors.required","El monto de cobro"));
			}
		}
		
		
		
		
		
		logger.info("CODIGO TIPO COBERTURA==>"+ingresoPacienteForm.getVariosConvenios("codigoTipoCobertura_"+pos)+"*");
		logger.info("es reporte==>"+ingresoPacienteForm.getVariosConvenios("esReporteAtencionInicialUrgencias_"+pos)+"*");
		//Cobertura salud
		if(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("esReporteAtencionInicialUrgencias_"+pos)+"")&&
			ingresoPacienteForm.getVariosConvenios("codigoTipoCobertura_"+pos).toString().equals(""))
		{
			errores.add("Tipo de cobertura",new ActionMessage("errors.required","El tipo de cobertura"));
		}
		
		//Carnet
		if(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("requiereCarnet_"+pos).toString())&&
		   ingresoPacienteForm.getVariosConvenios("numeroCarnet_"+pos).toString().trim().equals(""))
			errores.add("nÔøΩmero de carnet",new ActionMessage("errors.required","El nÔøΩmero de carnet"));
		
		//Validacion de que debe ser numerico el numero de solicitud volante si se ingresÔøΩ
		if(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("esConvenioColsanitas_"+pos)))
			if(!ingresoPacienteForm.getVariosConvenios("numeroSolicitudVolante_"+pos).toString().equals(""))
			{
				try
				{
					Long.parseLong(ingresoPacienteForm.getVariosConvenios("numeroSolicitudVolante_"+pos).toString());
				}
				catch(Exception e)
				{
					logger.warn("Error al tratar de convertir a num&uacute;rico el numero de solicitud volante: "+e);
					errores.add("",new ActionMessage("errors.integer","El  num&uacute;ro Solicitud Volante"));
				}
			}
		
		/*
		if(!UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esAsocio").toString())&&
			UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("esConvenioPoliza_"+pos).toString()))
		{
			//Apellidos del titular
			if(ingresoPacienteForm.getVariosConvenios("apellidosPoliza_"+pos).toString().equals(""))
				errores.add("Apellidos poliza requerido",new ActionMessage("errors.required","El apellido del titular"));
			//Nombres del titular
			if(ingresoPacienteForm.getVariosConvenios("nombresPoliza_"+pos).toString().equals(""))
				errores.add("Nombres poliza requerido",new ActionMessage("errors.required","El nombre del titular"));
			//Tipo Id. titular
			if(ingresoPacienteForm.getVariosConvenios("tipoIdPoliza_"+pos).toString().equals(""))
				errores.add("Tipo ID poliza requerido",new ActionMessage("errors.required","El tipo de identificaciÔøΩn del titular"));
			//NÔøΩmero Id. titular
			if(ingresoPacienteForm.getVariosConvenios("numeroIdPoliza_"+pos).toString().equals(""))
				errores.add("Numero ID poliza requerido",new ActionMessage("errors.required","El nÔøΩmero de identificaciÔøΩn del titular"));
			//Direccion Titular
			if(ingresoPacienteForm.getVariosConvenios("direccionPoliza_"+pos).toString().equals(""))
				errores.add("direccion poliza requerido",new ActionMessage("errors.required","La direcciÔøΩn del titular"));
			//Autorizacion Poliza
			if(ingresoPacienteForm.getVariosConvenios("autorizacionPoliza_"+pos).toString().equals(""))
				errores.add("direccion poliza requerido",new ActionMessage("errors.required","El nÔøΩmero de autorizaciÔøΩn de la pÔøΩliza"));
			//valor Poliza
			if(ingresoPacienteForm.getVariosConvenios("valorPoliza_"+pos).toString().equals(""))
				errores.add("valor poliza requerido",new ActionMessage("errors.required","El valor para el monto de la pÔøΩliza"));
			else
			{
				try
				{
					if(Double.parseDouble(ingresoPacienteForm.getVariosConvenios("valorPoliza_"+pos).toString())<=0)
						errores.add("Debe ser num√©rico", new ActionMessage("errors.MayorQue","El valor para el monto de la pÔøΩliza","0"));
				}
				catch(Exception e)
				{
					errores.add("Debe ser num√©rico", new ActionMessage("errors.float","El valor para el monto de la pÔøΩliza"));
				}
			}
			
		}*/
		
		//********************VERIFICACION DE DERECHOS********************************************************************************
		//Si la verificacion de derechos se intentÔøΩ ingresar,
		//se prosigue a realizar sus valdiaciones
		if(!ingresoPacienteForm.getVariosConvenios("codigoEstado_"+pos).toString().equals("")||
			!ingresoPacienteForm.getVariosConvenios("codigoTipo_"+pos).toString().equals(""))
		{
			ingresoPacienteForm.setVariosConvenios("ingresoVerificacionDerechos_"+pos, ConstantesBD.acronimoSi);
			
			//estado de verificacion
			if(ingresoPacienteForm.getVariosConvenios("codigoEstado_"+pos).toString().equals(""))
				errores.add("El estado es requerido",new ActionMessage("errors.required","El estado (Verificaci&oacute;n de Derechos)"));
			//tipo de verificacion
			if(ingresoPacienteForm.getVariosConvenios("codigoTipo_"+pos).toString().equals(""))
				errores.add("El tipo es requerido", new ActionMessage("errors.required","El tipo (Verificaci&oacute;n de Derechos)"));
			//persona solicita
			if(ingresoPacienteForm.getVariosConvenios("personaSolicita_"+pos).toString().equals(""))
				errores.add("la persona solicita es requerido", new ActionMessage("errors.required","Persona Solicita (Verificaci&oacute;n de Derechos)"));
			//persona contactada
			if(!ingresoPacienteForm.getVariosConvenios("codigoEstado_"+pos).toString().equals("")&&
				!ingresoPacienteForm.getVariosConvenios("codigoEstado_"+pos).toString().equals(ConstantesIntegridadDominio.acronimoEstadoPendientePorVerificar)&&
				ingresoPacienteForm.getVariosConvenios("personaContactada_"+pos).toString().equals(""))
				errores.add("la persona contactada es requerido", new ActionMessage("errors.required","Persona contactada (Verificaci&oacute;n de Derechos)"));
			
			//Validar la fecha de verificacion
			boolean fechaValida = true;
			aux0 = ingresoPacienteForm.getVariosConvenios("fechaVerificacion_"+pos).toString();
			if(aux0.equals(""))
			{
				errores.add("",new ActionMessage("errors.required","la fecha verificaciÔøΩn (Verificaci&oacute;n de Derechos)"));
				fechaValida = false;
			}
			else
			{
				if(!UtilidadFecha.validarFecha(aux0))
				{
					errores.add("",new ActionMessage("errors.formatoFechaInvalido","de verificaci&oacute;n (Verificaci&oacute;n de Derechos)"));
					fechaValida = false;
				}
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(aux0, UtilidadFecha.getFechaActual()))
				{
					errores.add("",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de verificaci&oacute;n (Verificaci&oacute;n de Derechos)", "actual"));
					fechaValida = false;
				}
			}
			
			//Validar la hora de verificaciÔøΩn
			String aux1 = ingresoPacienteForm.getVariosConvenios("horaVerificacion_"+pos).toString();
			if(aux1.equals(""))
				errores.add("",new ActionMessage("errors.required","la hora de verificaci&oacute;n (Verificaci&oacute;n de Derechos)"));
			else
			{
				if(!UtilidadFecha.validacionHora(aux1).puedoSeguir)
					errores.add("",new ActionMessage("errors.formatoHoraInvalido","de verificaci&oacute;n (Verificaci&oacute;n de Derechos)"));
				else if(fechaValida&&!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), aux0, aux1).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraPosteriorIgualActual", "de verificaci&oacute;n (VerificaciÔøΩn de Derechos)", "actual"));
					
			}
			
			//Validacion del porcentaje de cobertura
			if(!ingresoPacienteForm.getVariosConvenios("porcentajeCobertura_"+pos).toString().equals(""))
			{
				try
				{
					if(Double.parseDouble(ingresoPacienteForm.getVariosConvenios("porcentajeCobertura_"+pos).toString())>100)
						errores.add("",new ActionMessage("errors.MenorIgualQue","El porcentaje de cobertura (Verificaci&oacute;n de Derechos)","100%"));
				}
				catch(Exception e)
				{
					errores.add("",new ActionMessage("errors.float","El porcentaje de cobertura (Verificaci&oacute;n de Derechos)"));
				}
				
			}
		}
		else
			ingresoPacienteForm.setVariosConvenios("ingresoVerificacionDerechos_"+pos, ConstantesBD.acronimoNo);
		//**********************************************************************************************************************************
		
		return errores;
	}



	/**
	 * M√©todo implementado para ingresar un convenio adicional
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mapping
	 * @param usuarioActual 
	 * @param request 
	 * @param paciente 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionIngresarConvenioAdicional(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionMapping mapping, UsuarioBasico usuarioActual, HttpServletRequest request, PersonaBasica paciente) throws IPSException 
	{
		int pos = ingresoPacienteForm.getPosConvenio();
		//se inicializan los mensajes del convenio adicioanl
		ingresoPacienteForm.setMensajesConvenioAdicional(new ArrayList<ElementoApResource>());
		
		//Se verifica si se va ingresar un nuevo convenio o es uno ya existente
		if(ingresoPacienteForm.isNuevoConvenio())
		{
			pos = ingresoPacienteForm.getNumConvenios();
			ingresoPacienteForm.setPosConvenio(pos);
			
			
			//********SE VERIFICA SI FUE SELECCIONADO CONVENIO**********************
			if(fueSeleccionadoConvenio(ingresoPacienteForm,ingresoPacienteForm.getCodigoConvenio()))
			{
				ActionErrors errores = new ActionErrors();
				errores.add("Ya fue ingresado", new ActionMessage("errors.yaExiste","El convenio "+Utilidades.obtenerNombreConvenioOriginal(con, Integer.parseInt(ingresoPacienteForm.getCodigoConvenio()))));
				saveErrors(request, errores);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaErroresActionErrorsSinCabezote");
			}
			
			//Se postula la informacion del convenios adiciona
			this.postularInfoConvenioAdicional(con, ingresoPacienteForm, Integer.parseInt(ingresoPacienteForm.getCodigoConvenio()), usuarioActual, pos, paciente);
			//se a√±ade la via de ingreso
			ingresoPacienteForm.setVariosConvenios("codigoViaIngreso_"+pos, ingresoPacienteForm.getCodigoViaIngreso());
			//**********************************************************************************************************************************
			
			ingresoPacienteForm.setEstado("ingresarConvenioAdicionalNuevo");
			
		}
		else if(!UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("existeConvenio_"+pos).toString()))
		{
			//Si la v√≠a de ingreso es diferente a la que ten√≠a el convenio se vuelve a cargar los requisirtos
			if(Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso())!=Utilidades.convertirAEntero(ingresoPacienteForm.getVariosConvenios("codigoViaIngreso_"+pos)+""))
			{
				///******************SE CARGAN LOS REQUISITOS DEL PACIENTE***********************************************
				ArrayList<HashMap<String, Object>> arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
						con,
						Integer.parseInt(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+pos).toString()), 
						ConstantesIntegridadDominio.acronimoIngreso, 
						true, 
						usuarioActual.getCodigoInstitucionInt(),
						Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()));
				ingresoPacienteForm.setVariosConvenios("numReqIngreso_"+pos, arregloAux.size()+"");
					
				for(int j=0;j<arregloAux.size();j++)
				{
					HashMap elemento = (HashMap)arregloAux.get(j);
					ingresoPacienteForm.setVariosConvenios("codigoReqIngreso_"+pos+"_"+j, elemento.get("codigo"));
					ingresoPacienteForm.setVariosConvenios("descripcionReqIngreso_"+pos+"_"+j, elemento.get("descripcion"));
					ingresoPacienteForm.setVariosConvenios("cumplidoReqIngreso_"+pos+"_"+j, ConstantesBD.acronimoNo);
					ingresoPacienteForm.setVariosConvenios("existeReqIngreso_"+pos+"_"+j, ConstantesBD.acronimoNo);
				}
					
				arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
						con,
						Integer.parseInt(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+pos).toString()), 
						ConstantesIntegridadDominio.acronimoEgreso, 
						true, 
						usuarioActual.getCodigoInstitucionInt(),
						Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()));
				ingresoPacienteForm.setVariosConvenios("numReqEgreso_"+pos, arregloAux.size()+"");
				for(int j=0;j<arregloAux.size();j++)
				{
					HashMap elemento = (HashMap)arregloAux.get(j);
					ingresoPacienteForm.setVariosConvenios("codigoReqEgreso_"+pos+"_"+j, elemento.get("codigo"));
					ingresoPacienteForm.setVariosConvenios("descripcionReqEgreso_"+pos+"_"+j, elemento.get("descripcion"));
					ingresoPacienteForm.setVariosConvenios("cumplidoReqEgreso_"+pos+"_"+j, ConstantesBD.acronimoNo);
					ingresoPacienteForm.setVariosConvenios("existeReqEgreso_"+pos+"_"+j, ConstantesBD.acronimoNo);
				}
					
				ingresoPacienteForm.setVariosConvenios("tieneRequisitosPaciente_"+pos, 
					(Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numReqIngreso_"+pos).toString())>0||Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numReqEgreso_"+pos).toString())>0)?
						ConstantesBD.acronimoSi:
						ConstantesBD.acronimoNo);
				//**********************************************************************************************************************************
			}
			
			ingresoPacienteForm.setVariosConvenios("codigoViaIngreso_"+pos, ingresoPacienteForm.getCodigoViaIngreso());
		}
		
		///Se cargan los contratos del convenio
		if(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("esConvenioCapitado_"+pos).toString()))
			ingresoPacienteForm.setContratosConvenio(UtilidadesManejoPaciente.obtenerContratosVigentesUsuarioCapitado(con, paciente.getCodigoPersona(), Integer.parseInt(ingresoPacienteForm.getCodigoConvenio())));
		else
			ingresoPacienteForm.setContratosConvenio(Utilidades.obtenerContratos(con, Integer.parseInt(ingresoPacienteForm.getCodigoConvenio()), false, true));
		
		if(ingresoPacienteForm.getContratos().size()==1){
			final int unicoContrato=0;
			verificarMontoCobro(ingresoPacienteForm,ingresoPacienteForm.getContratos().get(unicoContrato).get("codigo").toString());
		}
		
		//se cargan los estratos sociales del contrato
		ingresoPacienteForm.setEstratosSocialesConvenio(
				UtilidadesFacturacion.cargarEstratosSociales(con, usuarioActual.getCodigoInstitucionInt(), 
						ConstantesBD.acronimoSi, ingresoPacienteForm.getVariosConvenios("codigoTipoRegimen_"+pos).toString(),
						Integer.parseInt(ingresoPacienteForm.getCodigoConvenio()),
						Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()),
						Utilidades.capturarFechaBD()));
		
		ingresoPacienteForm.setTiposAfiliadoConvenio(UtilidadesFacturacion.cargarTiposAfiliado(con, 
				usuarioActual.getCodigoInstitucionInt(),ConstantesBD.acronimoSi,
				Integer.parseInt(ingresoPacienteForm.getCodigoConvenio()),
				Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()),
				Utilidades.capturarFechaBD()));
		
		ingresoPacienteForm.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPaciente(con,ingresoPacienteForm.getVariosConvenios("codigoTipoRegimen_"+pos).toString(),Integer.parseInt(ingresoPacienteForm.getCodigoConvenio()),Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso())));
		
		ingresoPacienteForm.setPuedoGrabarConvenioAdicional(true);
		
		if(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("esConvenioCapitado_"+pos).toString()))
		{
			//si solo tiene un contrato vigente se debe postular la informacion
			if(ingresoPacienteForm.getContratosConvenio().size()==1)
			{
				int codClasi=Utilidades.convertirAEntero(ingresoPacienteForm.getContratosConvenio().get(0).get("codigoestratosocial")+"");
				if(codClasi>0)
				{
					String tipoRegimenCSE=Utilidades.obtenerTipoRegimenClasificacionSocioEconomica(con, codClasi);
					if(ingresoPacienteForm.getVariosConvenios("codigoTipoRegimen_"+pos).toString().equals(tipoRegimenCSE))
					{
						ingresoPacienteForm.setVariosConvenios("codigoEstratoSocial", codClasi);
						HashMap tempo=new HashMap();
						tempo.put("numRegistros", "0");
						HashMap tempo1=new HashMap();
						tempo1.put("numRegistros", "0");
						for(int i=0;i<Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSocialesConvenio().get("numRegistros")+"");i++)
						{
							if(Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSocialesConvenio().get("codigo_"+i)+"")==codClasi)
							{
								tempo.put("codigo_0", ingresoPacienteForm.getEstratosSocialesConvenio().get("codigo_"+i)+"");
								tempo.put("descripcion_0", ingresoPacienteForm.getEstratosSocialesConvenio().get("descripcion_"+i)+"");
								tempo.put("numRegistros", "1");
								break;
							}
						}
						String tipoAfil=ingresoPacienteForm.getContratosConvenio().get(0).get("tipoafiliado")+"";
						if(!UtilidadTexto.isEmpty(tipoAfil+""))
						{
							//{numRegistros=2, acronimo_0=B, nombre_1=Cotizante, nombre_0=Beneficiario, acronimo_1=C}
							ingresoPacienteForm.setVariosConvenios("codigoTipoAfiliado", tipoAfil);
							
							for(int i=0;i<Utilidades.convertirAEntero(ingresoPacienteForm.getTiposAfiliadoConvenio().get("numRegistros")+"");i++)
							{
								if((ingresoPacienteForm.getTiposAfiliadoConvenio().get("acronimo_"+i)+"").equals(tipoAfil+""))
								{
									tempo1.put("acronimo_0", ingresoPacienteForm.getTiposAfiliadoConvenio().get("acronimo_"+i)+"");
									tempo1.put("nombre_0", ingresoPacienteForm.getTiposAfiliadoConvenio().get("nombre_"+i)+"");
									tempo1.put("numRegistros", "1");
									break;
								}
							}
	
						}
						if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitados(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
						{
							ingresoPacienteForm.setEstratosSocialesConvenio(tempo);
							if(!UtilidadTexto.isEmpty(tipoAfil))
							{
								ingresoPacienteForm.setTiposAfiliadoConvenio(tempo1);
							}
						}	
						if(!UtilidadTexto.isEmpty(tipoAfil))
						{
							if(Utilidades.convertirAEntero(ingresoPacienteForm.getTiposAfiliadoConvenio().get("numRegistros")+"")<=0)
							{
								ingresoPacienteForm.setPuedoGrabarConvenioAdicional(false);
								ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
								mensaje.agregarAtributo("El Tipo Afiliado del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.");
								ingresoPacienteForm.getMensajesConvenioAdicional().add(mensaje);
							}
						}
						if(Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSocialesConvenio().get("numRegistros")+"")<=0)
						{
							ingresoPacienteForm.setPuedoGrabarConvenioAdicional(false);
							ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
							mensaje.agregarAtributo("La ClasificaciÛn socio econÛmica del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.");
							ingresoPacienteForm.getMensajesConvenioAdicional().add(mensaje);
						}
					}
					else
					{
							ingresoPacienteForm.setPuedoGrabarConvenioAdicional(false);
							ingresoPacienteForm.setEstratosSocialesConvenio(new HashMap());
							ingresoPacienteForm.setTiposAfiliadoConvenio(new HashMap());
						
							ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
							mensaje.agregarAtributo("El Tipo de rÈgimen de la ClasificaciÛn socio  econÛmica no corresponde con el Tipo de rÈgimen del Convenio. No se puede asignar este Convenio. Por favor verifique.");
							ingresoPacienteForm.getMensajesConvenioAdicional().add(mensaje);
							
					}
				}
				int natPaciente=Utilidades.convertirAEntero(ingresoPacienteForm.getContratosConvenio().get(0).get("naturalezapaciente")+"");
				if(natPaciente>0)
				{
					if(UtilidadValidacion.esNaturalezaValidaTipoRegimen(natPaciente,ingresoPacienteForm.getVariosConvenios("codigoTipoRegimen_"+pos).toString())) 
					{

						ingresoPacienteForm.setCuenta("codigoNaturaleza", natPaciente+"");
						Vector<InfoDatosString> naturalezaVector=new Vector<InfoDatosString>();
						for(int i=0;i<ingresoPacienteForm.getNaturalezasPaciente().size();i++)
						{
							if(Utilidades.convertirAEntero(ingresoPacienteForm.getNaturalezasPaciente().get(i).getCodigo()) ==natPaciente)
							{
								naturalezaVector.add(ingresoPacienteForm.getNaturalezasPaciente().get(i));
							}
						}
						if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitados(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
						{
							ingresoPacienteForm.setNaturalezasPaciente(naturalezaVector);
						}
						
						if(ingresoPacienteForm.getNaturalezasPaciente().size()<=0)
						{
							ingresoPacienteForm.setPuedoGrabarConvenioAdicional(false);
							ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
							mensaje.agregarAtributo("La Naturaleza Paciente del paciente no corresponde con los Montos Cobro del Convenio. No se puede asignar este Convenio. Por favor verifique.");
							ingresoPacienteForm.getMensajesConvenioAdicional().add(mensaje);
						}
						
					}
					else
					{
						ingresoPacienteForm.setPuedoGrabarConvenioAdicional(false);
						ingresoPacienteForm.setNaturalezasPaciente(new Vector<InfoDatosString>());
						ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
						mensaje.agregarAtributo("El Tipo de rÈgimen de la Naturaleza Paciente no corresponde con el Tipo de rÈgimen del Convenio. No se puede asignar este Convenio. Por favor verifique.");
						ingresoPacienteForm.getMensajesConvenioAdicional().add(mensaje);
					}
				}
				
			}
			
		}
		
		
		
		
		//se cargan las coberturas salud
		ingresoPacienteForm.setCoberturasSaludConvenio(UtilidadesManejoPaciente.obtenerCoberturasSaludXTipoRegimen(con, ingresoPacienteForm.getVariosConvenios("codigoTipoRegimen_"+pos).toString(), usuarioActual.getCodigoInstitucionInt()));
		//Se cargan los montos de cobro
		ingresoPacienteForm.setMontosCobroConvenio(UtilidadesFacturacion.obtenerMontosCobroXConvenio(con, ingresoPacienteForm.getCodigoConvenio(), UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
		
		
		//Edicion de advertencias************************************+
		//1) Requiere autorizacion servicio?
		if(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("esRequiereAutorizacionServicio_"+pos).toString())&&
			!UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("existeConvenio_"+pos).toString()))
		{
			ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
			mensaje.agregarAtributo("Para el convenio "+ingresoPacienteForm.getVariosConvenios("nombreConvenio_"+pos)+" se requiere autorizaciÔøΩn de admisiÔøΩn");
			ingresoPacienteForm.getMensajesConvenioAdicional().add(mensaje);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingresarConvenioAdicional");
	}




	/**
	 * M√©todo que verifica si un convenio ya fue seleccionado
	 * @param ingresoPacienteForm
	 * @param codigoConvenio
	 * @return
	 */
	private boolean fueSeleccionadoConvenio(IngresoPacienteForm ingresoPacienteForm, String codigoConvenio) 
	{
		boolean seleccionado = false;
		
		
		//logger.info("CONVENIO PRINCIPAL=> "+ingresoPacienteForm.getCuenta("codigoConvenio"));
		//logger.info("CONVENIO A COMPARAR=> "+codigoConvenio);
		//Se verifica el convenio de prioridad 1
		if(ingresoPacienteForm.getCuenta("codigoConvenio")!=null&&
		  ingresoPacienteForm.getCuenta("codigoConvenio").toString().split(ConstantesBD.separadorSplit)[0].equals(codigoConvenio))
			seleccionado = true;
		
		//Se verifica el convenio en los dem√°s convenios
		for(int i=0;i<ingresoPacienteForm.getNumConvenios();i++)
		{
			if(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i)!=null&&
				ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i).toString().equals(codigoConvenio))
				seleccionado = true;
		}
		
		
		return seleccionado;
	}




	/**
	 * M√©todo que realiza el calculo de las semanas de cotizacion a partir de la fecha de afiliacion
	 * @param con
	 * @param ingresoPacienteForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroFechaAfiliacion(Connection con, IngresoPacienteForm ingresoPacienteForm, HttpServletResponse response) 
	{
		String aux = "";
		
		if(UtilidadFecha.validarFecha(ingresoPacienteForm.getFechaAfiliacion()))
		{
			String fechaSistema = UtilidadFecha.getFechaActual(con);
			if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(ingresoPacienteForm.getFechaAfiliacion(), fechaSistema))
			{
				aux ="<fecha-valida>"+ConstantesBD.acronimoSi+"</fecha-valida>";
				
				int numeroDias = UtilidadFecha.numeroDiasEntreFechas(ingresoPacienteForm.getFechaAfiliacion(), fechaSistema);
				int numeroSemanas = (numeroDias/7) + (numeroDias%7!=0?1:0);
				aux += "<semanas>"+numeroSemanas+"</semanas>";
				aux += "<meses>"+UtilidadFecha.numeroMesesEntreFechas(ingresoPacienteForm.getFechaAfiliacion(), fechaSistema,false)+"</meses>";
			}
			else
			{
				aux ="<fecha-valida>"+ConstantesBD.acronimoNo+"</fecha-valida>";
				aux +="<mensaje>La fecha de afiliaciÔøΩn debe ser anterior o igual a la fecha actual.</mensaje>";
			}
			
		}
		else
		{
			aux ="<fecha-valida>"+ConstantesBD.acronimoNo+"</fecha-valida>";
			aux +="<mensaje>La fecha de afiliaciÔøΩn no es una fecha v√°lida. Debe estar en formato dd/mm/aaaa.</mensaje>";
		}
		
		
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write(aux);
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroFechaAfiliacion: "+e);
		}
		return null;
	}




	/**
	 * M√©todo implementado para realizar el filtro por convenio
	 * @param con
	 * @param ingresoPacienteForm
	 * @param usuarioActual
	 * @param response
	 * @param paciente 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionFiltroConvenio(Connection con, IngresoPacienteForm ingresoPacienteForm, UsuarioBasico usuarioActual, HttpServletResponse response, PersonaBasica paciente) throws IPSException 
	{
		HashMap mapaRespuesta = new HashMap();
		String aux = "", codigoRegimen = "";
		String[] vecAux = new String[0];
		HashMap mapaAux = new HashMap();
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		/**
		 * [0] => codigo convenio
		 * [1] => codigo tipo contrato
		 * [2] => es pyp? (boolean)
		 */
		String[] datosConvenio = ingresoPacienteForm.getCodigoConvenio().split(ConstantesBD.separadorSplit);
		boolean fueSeleccionadoConvenio = fueSeleccionadoConvenio(ingresoPacienteForm, datosConvenio[0]);
		
		if(fueSeleccionadoConvenio)
		{
			//Se pregunta si el convenio es igual al convenio por defecto
			//Porque si lo es, se debe preguntar si ya no se desea que sea el convenio de baja prioridad
			if(!UtilidadTexto.isEmpty(ingresoPacienteForm.getCuenta("codigoConvenioDefault")+"")&&Integer.parseInt(datosConvenio[0])==Integer.parseInt(ingresoPacienteForm.getCuenta("codigoConvenioDefault").toString()))
			{
				int posConvenioDefecto = ConstantesBD.codigoNuncaValido;
				//Se toma la posicion del convenio por defecto
				for(int i=0;i<ingresoPacienteForm.getNumConvenios();i++)
				{
					if(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i)!=null&&
						ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i).toString().equals(ingresoPacienteForm.getCuenta("codigoConvenioDefault")))
						posConvenioDefecto = i;
				}
				
				aux += "<alerta-convenio-defecto>"+posConvenioDefecto+"</alerta-convenio-defecto>";

			}
			else
			{
				
				aux = "<error>El convenio "+Utilidades.obtenerNombreConvenioOriginal(con, Integer.parseInt(datosConvenio[0]))+" ya fue ingresado</error>";
				UtilidadBD.closeConnection(con);
				//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
				try
				{
					response.setCharacterEncoding("UTF-8");
		            response.setContentType("text/xml");
					response.setHeader("Cache-Control", "no-cache");
					response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			        response.getWriter().write("<respuesta>");
			        response.getWriter().write(aux);
			        response.getWriter().write("</respuesta>");
				}
				catch(IOException e)
				{
					logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
				}
				return null;
			}
		}
		
		
		
		ingresoPacienteForm.setCuenta("codigoConvenio", ingresoPacienteForm.getCodigoConvenio()); //se actualiza en la forma
		//*************VERIFICACION DE INDICATIVO PYP Y CAPITACION***********************************
		aux += "<indicativo-pyp>";
		aux += UtilidadTexto.getBoolean(datosConvenio[2])?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo;
		aux += "</indicativo-pyp>";
		aux += "<indicativo-capitacion>";
		aux += Integer.parseInt(datosConvenio[1])==ConstantesBD.codigoTipoContratoCapitado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo;
		aux += "</indicativo-capitacion>";
		//se llena la informacion del mapa
		ingresoPacienteForm.setCuenta("esConvenioPyp", UtilidadTexto.getBoolean(datosConvenio[2])?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("esConvenioCapitado", Integer.parseInt(datosConvenio[1])==ConstantesBD.codigoTipoContratoCapitado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapaRespuesta.put("respuestaPypCapitacion", aux);
		//*******************************************************************************************
		//************VERIFICACION DEL TIPO DE REGIMEN************************************************
		vecAux = Utilidades.obtenerTipoRegimenConvenio(con, datosConvenio[0]).split("-");
		codigoRegimen = vecAux[0];
		//se llena la informacion del mapa
		ingresoPacienteForm.setCuenta("codigoTipoRegimen", vecAux[0]);
		ingresoPacienteForm.setCuenta("nombreTipoRegimen", vecAux[1]);
		aux = "<tipo-regimen>";
		aux += "<codigo>"+vecAux[0]+"</codigo><nombre>"+vecAux[1]+"</nombre>";
		aux += "</tipo-regimen>";
		mapaRespuesta.put("respuestaTipoRegimen", aux);
		//********************************************************************************************++
		//*********CONSULTA DE LOS CONTRATOS VIGENTES***********************************************************
		if(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esConvenioCapitado").toString()))
			ingresoPacienteForm.setContratos(UtilidadesManejoPaciente.obtenerContratosVigentesUsuarioCapitado(con, paciente.getCodigoPersona(), Integer.parseInt(datosConvenio[0])));
		else
			ingresoPacienteForm.setContratos(Utilidades.obtenerContratos(con, Integer.parseInt(datosConvenio[0]), false, true));
		
		aux = "<contratos>";
		for(int i=0;i<ingresoPacienteForm.getContratos().size();i++)
		{
			mapaAux = (HashMap) ingresoPacienteForm.getContratos().get(i);
			aux += "" +
				"<contrato>" +
					"<codigo>"+mapaAux.get("codigo")+"</codigo>" +
					"<numero-contrato>"+mapaAux.get("numerocontrato")+"</numero-contrato>" +
					"<paciente-paga-atencion>"+mapaAux.get("pacientepagaatencion")+"</paciente-paga-atencion>" +
				"</contrato>";
		}
		aux += "</contratos>";
		mapaRespuesta.put("respuestaContratos", aux);
		
		
		//*********CONSULTA DE LOS ESTRATOS SOCIALES*****************************************************
		ingresoPacienteForm.setEstratosSociales(UtilidadesFacturacion.cargarEstratosSociales(
			con, usuarioActual.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, codigoRegimen,
			Integer.parseInt(datosConvenio[0]),
			Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()), Utilidades.capturarFechaBD()));
		/*ingresoPacienteForm.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPaciente(con,codigoRegimen,Integer.parseInt(datosConvenio[0]),Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso())));
		
		//**********************************************************************************************
		
		//*********CONSULTA DE LOS TIPOS AFILIADO*****************************************************
		HashMap tiposAfiliado=UtilidadesFacturacion.cargarTiposAfiliado(
		con, usuarioActual.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, 
		Integer.parseInt(datosConvenio[0]),
		Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()));
		ingresoPacienteForm.setTiposAfiliado(tiposAfiliado);*/
		
		//*********CONSULTA DE LOS TIPOS AFILIADO*****************************************************
		int estratoSocial = ConstantesBD.codigoNuncaValido;
		String tipoAfiliado = "";
		if (ingresoPacienteForm.getEstratosSociales().get("codigo_0") != null) {
			estratoSocial = Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales().get("codigo_0").toString());
		}
		
		HashMap tiposAfiliado = UtilidadesFacturacion.cargarTiposAfiliadoXEstrato(
				con,  usuarioActual.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, 
				Utilidades.convertirAEntero(datosConvenio[0]),Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()),
				estratoSocial, Utilidades.capturarFechaBD());
		
		ingresoPacienteForm.setTiposAfiliado(tiposAfiliado);
		
		if (ingresoPacienteForm.getTiposAfiliado().get("acronimo_0") != null) {
			tipoAfiliado = ingresoPacienteForm.getTiposAfiliado().get("acronimo_0").toString();
		}
		
		//9) Se consultan las naturalezas del paciente
		ingresoPacienteForm.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
				con, codigoRegimen, Utilidades.convertirAEntero(datosConvenio[0]),
				Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()), 
				tipoAfiliado, 
				estratoSocial, Utilidades.capturarFechaBD()));
		
		
		
		if(Utilidades.convertirAEntero(datosConvenio[1])==ConstantesBD.codigoTipoContratoCapitado)
		{
			//si solo tiene un contrato vigente se debe postular la informacion
			if(ingresoPacienteForm.getContratos().size()==1)
			{
				int codClasi=Utilidades.convertirAEntero(ingresoPacienteForm.getContratos().get(0).get("codigoestratosocial")+"");
				if(codClasi>0)
				{
					String tipoRegimenCSE=Utilidades.obtenerTipoRegimenClasificacionSocioEconomica(con, codClasi);
					if(codigoRegimen.equals(tipoRegimenCSE))
					{
						ingresoPacienteForm.setCuenta("codigoEstratoSocial", codClasi);
						HashMap tempo=new HashMap();
						tempo.put("numRegistros", "0");
						HashMap tempo1=new HashMap();
						tempo1.put("numRegistros", "0");
						for(int i=0;i<Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales("numRegistros")+"");i++)
						{
							if(Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales("codigo_"+i)+"")==codClasi)
							{
								tempo.put("codigo_0", ingresoPacienteForm.getEstratosSociales("codigo_"+i)+"");
								tempo.put("descripcion_0", ingresoPacienteForm.getEstratosSociales("descripcion_"+i)+"");
								tempo.put("numRegistros", "1");
								break;
							}
						}
						String tipoAfil=ingresoPacienteForm.getContratos().get(0).get("tipoafiliado")+"";
						if(!UtilidadTexto.isEmpty(tipoAfil+""))
						{
							//{numRegistros=2, acronimo_0=B, nombre_1=Cotizante, nombre_0=Beneficiario, acronimo_1=C}
							ingresoPacienteForm.setCuenta("codigoTipoAfiliado", tipoAfil);
							
							for(int i=0;i<Utilidades.convertirAEntero(ingresoPacienteForm.getTiposAfiliado("numRegistros")+"");i++)
							{
								if((ingresoPacienteForm.getTiposAfiliado("acronimo_"+i)+"").equals(tipoAfil+""))
								{
									tempo1.put("acronimo_0", ingresoPacienteForm.getTiposAfiliado("acronimo_"+i)+"");
									tempo1.put("nombre_0", ingresoPacienteForm.getTiposAfiliado("nombre_"+i)+"");
									tempo1.put("numRegistros", "1");
									break;
								}
							}
	
						}
						if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitados(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
						{
							ingresoPacienteForm.setEstratosSociales(tempo);
							if(!UtilidadTexto.isEmpty(tipoAfil))
							{
								ingresoPacienteForm.setTiposAfiliado(tempo1);
							}
						}		
						
						HashMap<String, Object>cuenta=ingresoPacienteForm.getCuenta();
						if(cuenta.get("codigoContrato")!=null&&!((String)cuenta.get("codigoContrato")).trim().isEmpty()
								&&!((String)cuenta.get("codigoContrato")).trim().equals(ConstantesBD.codigoNuncaValido+"")){
							
							/**VERIFICAR CONTRATOS, si hay uno solo consultar validacion tipo cobro paciente para verificar la obligatoriedad los campos:
							 *  
							 * Clasificacion socioeconomica
							 * Tipo afiliado
							 * Naturaleza Paciente
							 * Porcentaje de cobertura
							 * Cuota Verificacion
							 * 
							 * MT 4836
							 * */
							DtoValidacionTipoCobroPaciente validacion=null;
							if(!ingresoPacienteForm.getContratos().isEmpty()&&ingresoPacienteForm.getContratos().size()==1){
								IValidacionTipoCobroPacienteServicio validacionTipoCobroPacienteServicio=FacturacionServicioFabrica.crearValidacionTipoCobroPacienteServicio();
								validacion=validacionTipoCobroPacienteServicio.validarTipoCobroPacienteServicioConvenioContrato(Integer.parseInt(cuenta.get("codigoContrato").toString()));
							}
							
							if(validacion!=null&&UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion())&&
									Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales().get("numRegistros")+"")<=0)
							{
								aux = "<error>La ClasificaciÛn socio econÛmica del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
								UtilidadBD.closeConnection(con);
								//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
								try
								{
									response.setCharacterEncoding("UTF-8");
						            response.setContentType("text/xml");
									response.setHeader("Cache-Control", "no-cache");
									response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
							        response.getWriter().write("<respuesta>");
							        response.getWriter().write(aux);
							        response.getWriter().write("</respuesta>");
								}
								catch(IOException e)
								{
									logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
								}
								return null;
							}
							if(validacion!=null&&UtilidadTexto.getBoolean(validacion.getMostrarTipoAfiliado())&&
									!UtilidadTexto.isEmpty(tipoAfil))
							{
								if(Utilidades.convertirAEntero(ingresoPacienteForm.getTiposAfiliado("numRegistros")+"")<=0)
								{
									aux = "<error>El Tipo Afiliado del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
									UtilidadBD.closeConnection(con);
									//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
									try
									{
										response.setCharacterEncoding("UTF-8");
							            response.setContentType("text/xml");
										response.setHeader("Cache-Control", "no-cache");
										response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
								        response.getWriter().write("<respuesta>");
								        response.getWriter().write(aux);
								        response.getWriter().write("</respuesta>");
									}
									catch(IOException e)
									{
										logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
									}
									return null;
									
								}
								
							}
						}
					}
					else
					{
							ingresoPacienteForm.setEstratosSociales(new HashMap());
							ingresoPacienteForm.setTiposAfiliado(new HashMap());
						
							aux = "<error>El Tipo de rÈgimen de la ClasificaciÛn socio  econÛmica no corresponde con el Tipo de rÈgimen del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
							UtilidadBD.closeConnection(con);
							//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
							try
							{
								response.setCharacterEncoding("UTF-8");
					            response.setContentType("text/xml");
								response.setHeader("Cache-Control", "no-cache");
								response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
						        response.getWriter().write("<respuesta>");
						        response.getWriter().write(aux);
						        response.getWriter().write("</respuesta>");
							}
							catch(IOException e)
							{
								logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
							}
							return null;
					}
				}
				int natPaciente=Utilidades.convertirAEntero(ingresoPacienteForm.getContratos().get(0).get("naturalezapaciente")+"");
				if(natPaciente>0)
				{
					if(UtilidadValidacion.esNaturalezaValidaTipoRegimen(natPaciente,codigoRegimen+"")) 
					{

						ingresoPacienteForm.setCuenta("codigoNaturaleza", natPaciente+"");
						Vector<InfoDatosString> naturalezaVector=new Vector<InfoDatosString>();
						for(int i=0;i<ingresoPacienteForm.getNaturalezasPaciente().size();i++)
						{
							if(Utilidades.convertirAEntero(ingresoPacienteForm.getNaturalezasPaciente().get(i).getCodigo()) ==natPaciente)
							{
								naturalezaVector.add(ingresoPacienteForm.getNaturalezasPaciente().get(i));
							}
						}
						if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitados(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
						{
							ingresoPacienteForm.setNaturalezasPaciente(naturalezaVector);
						}
						
						if(ingresoPacienteForm.getNaturalezasPaciente().size()<=0)
						{
							aux = "<error>La Naturaleza Paciente del paciente no corresponde con los Montos Cobro del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
							UtilidadBD.closeConnection(con);
							//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
							try
							{
								response.setCharacterEncoding("UTF-8");
					            response.setContentType("text/xml");
								response.setHeader("Cache-Control", "no-cache");
								response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
						        response.getWriter().write("<respuesta>");
						        response.getWriter().write(aux);
						        response.getWriter().write("</respuesta>");
							}
							catch(IOException e)
							{
								logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
							}
							return null;
						}
						
					}
					else
					{
						aux = "<error>El Tipo de rÈgimen de la Naturaleza Paciente no corresponde con el Tipo de rÈgimen del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
						UtilidadBD.closeConnection(con);
						//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
						try
						{
							response.setCharacterEncoding("UTF-8");
				            response.setContentType("text/xml");
							response.setHeader("Cache-Control", "no-cache");
							response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
					        response.getWriter().write("<respuesta>");
					        response.getWriter().write(aux);
					        response.getWriter().write("</respuesta>");
						}
						catch(IOException e)
						{
							logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
						}
						return null;
					}
				}
			}
			
		}
		aux = "<estratos-sociales>";
		for(int i=0;i<Integer.parseInt(ingresoPacienteForm.getEstratosSociales("numRegistros").toString());i++)
			aux += "" +
				"<estrato-social>" +
					"<codigo>"+ingresoPacienteForm.getEstratosSociales("codigo_"+i)+"</codigo>" +
					"<nombre>"+ingresoPacienteForm.getEstratosSociales("descripcion_"+i)+"</nombre>" +
				"</estrato-social>";
		aux += "</estratos-sociales>";
		mapaRespuesta.put("respuestaEstratosSociales", aux);
		aux = "<tipos-afiliado>";
		for(int i=0;i<Integer.parseInt(ingresoPacienteForm.getTiposAfiliado().get("numRegistros").toString());i++)
			aux += "" +
				"<tipo-afiliado>" +
					"<acronimo>"+ingresoPacienteForm.getTiposAfiliado().get("acronimo_"+i)+"</acronimo>" +
					"<nombre>"+ingresoPacienteForm.getTiposAfiliado().get("nombre_"+i)+"</nombre>" +
				"</tipo-afiliado>";
		aux += "</tipos-afiliado>";
		mapaRespuesta.put("respuestaTiposAfiliado", aux);
		aux = "<naturalezas-paciente>";
		for(int i=0;i<ingresoPacienteForm.getNaturalezasPaciente().size();i++)
			aux += "" +
				"<naturaleza-paciente>" +
					"<codigo>"+ingresoPacienteForm.getNaturalezasPaciente().get(i).getCodigo()+"</codigo>" +
					"<nombre>"+ingresoPacienteForm.getNaturalezasPaciente().get(i).getNombre()+"</nombre>" +
				"</naturaleza-paciente>";
		aux += "</naturalezas-paciente>";
		mapaRespuesta.put("respuestaNaturaleza", aux);
		
		/*
		//**********CONSULTA DE LOS MONTOS DE COBRO******************************************************
		ingresoPacienteForm.setMontosCobro(UtilidadesFacturacion.obtenerMontosCobroXConvenio(con, datosConvenio[0], UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
		
		aux = "<montos-cobro>";
		for(int i=0;i<ingresoPacienteForm.getMontosCobro().size();i++)
		{
			mapaAux = (HashMap) ingresoPacienteForm.getMontosCobro().get(i);
			aux += "<monto-cobro>";
			aux += "<codigo>"+mapaAux.get("codigo")+"</codigo>";
			aux += "<via-ingreso>"+mapaAux.get("viaIngreso")+"</via-ingreso>";
			aux += "<codigo-tipo-afiliado>"+mapaAux.get("codigoTipoAfiliado")+"</codigo-tipo-afiliado>";
			aux += "<nombre-tipo-afiliado>"+mapaAux.get("nombreTipoAfiliado")+"</nombre-tipo-afiliado>";
			aux += "<estrato-social>"+mapaAux.get("estratoSocial")+"</estrato-social>";
			aux += "<tipo-monto>"+mapaAux.get("tipoMonto")+"</tipo-monto>";
			aux += "<porcentaje>"+mapaAux.get("porcentaje")+"</porcentaje>";
			aux += "<valor>"+mapaAux.get("valor")+"</valor>";
			aux += "<activo>"+mapaAux.get("activo")+"</activo>";
			//aux += "<vigenciainicial>"+mapaAux.get("vigenciaInicial")+"</vigenciainicial>";
			aux += "</monto-cobro>";
		}
		aux += "</montos-cobro>";
		
		mapaRespuesta.put("respuestaMontosCobro", aux);
		//***********************************************************************************************
		*/
		
		
		//*****************	CONSULTA DE LOS REQUISITOS PACIENTE*******************************************
		//Se consultan los requisitos de ingreso
		arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
			con,
			Integer.parseInt(datosConvenio[0]), 
			ConstantesIntegridadDominio.acronimoIngreso, 
			true, 
			usuarioActual.getCodigoInstitucionInt(),
			Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()));
		ingresoPacienteForm.setCuenta("numReqIngreso", arregloAux.size()+"");
		aux = "<requisitos-ingreso>";
		for(int i=0;i<arregloAux.size();i++)
		{
			HashMap elemento = (HashMap)arregloAux.get(i);
			ingresoPacienteForm.setCuenta("codigoReqIngreso_"+i, elemento.get("codigo"));
			ingresoPacienteForm.setCuenta("descripcionReqIngreso_"+i, elemento.get("descripcion"));
			ingresoPacienteForm.setCuenta("existeReqIngreso_"+i, ConstantesBD.acronimoNo);
			ingresoPacienteForm.setCuenta("cumplidoReqIngreso_"+i, UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("cumplidoReqIngreso_"+i)+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			aux += "<requisito-ingreso>";
			aux += "<codigo>"+elemento.get("codigo")+"</codigo>";
			aux += "<descripcion>"+elemento.get("descripcion")+"</descripcion>";
			aux += "<cumplido>"+ingresoPacienteForm.getCuenta("cumplidoReqIngreso_"+i)+"</cumplido>";
			aux += "</requisito-ingreso>";
		}
		aux += "</requisitos-ingreso>";
		//Se consultan los requisitos del egreso
		arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
				con,
				Integer.parseInt(datosConvenio[0]), 
				ConstantesIntegridadDominio.acronimoEgreso, 
				true, 
				usuarioActual.getCodigoInstitucionInt(),
				Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()));
		ingresoPacienteForm.setCuenta("numReqEgreso", arregloAux.size()+"");
		aux += "<requisitos-egreso>";
		for(int i=0;i<arregloAux.size();i++)
		{
			HashMap elemento = (HashMap)arregloAux.get(i);
			ingresoPacienteForm.setCuenta("codigoReqEgreso_"+i, elemento.get("codigo"));
			ingresoPacienteForm.setCuenta("descripcionReqEgreso_"+i, elemento.get("descripcion"));
			ingresoPacienteForm.setCuenta("existeReqEgreso_"+i, ConstantesBD.acronimoNo);
			ingresoPacienteForm.setCuenta("cumplidoReqEgreso_"+i, UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("cumplidoReqEgreso_"+i)+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			aux += "<requisito-egreso>";
			aux += "<codigo>"+elemento.get("codigo")+"</codigo>";
			aux += "<descripcion>"+elemento.get("descripcion")+"</descripcion>";
			aux += "<cumplido>"+ingresoPacienteForm.getCuenta("cumplidoReqEgreso_"+i)+"</cumplido>";
			aux += "</requisito-egreso>";
		}
		aux += "</requisitos-egreso>";
		
		ingresoPacienteForm.setCuenta("tieneRequisitosPaciente", 
			(Integer.parseInt(ingresoPacienteForm.getCuenta("numReqIngreso").toString())>0||Integer.parseInt(ingresoPacienteForm.getCuenta("numReqEgreso").toString())>0)?
				ConstantesBD.acronimoSi:
				ConstantesBD.acronimoNo);
		aux += "<indicativo-requisitos>"+ingresoPacienteForm.getCuenta("tieneRequisitosPaciente")+"</indicativo-requisitos>";
		
		mapaRespuesta.put("respuestaRequisitosPaciente", aux);
		
		
		//***********************************************************************************************
		//*********VALIDACIONES VARIAS CONVENIOS****************************************************
		Convenio mundoConvenio = new Convenio();
		mundoConvenio.cargarResumen(con, Integer.parseInt(datosConvenio[0]));
		//se llena mapa
		ingresoPacienteForm.setCuenta("esConvenioSoat", Convenio.esConvenioTipoConventioEventoCatTrans(mundoConvenio.getCodigo())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("manejoComplejidad", mundoConvenio.getManejaComplejidad());
		ingresoPacienteForm.setCuenta("esConvenioPoliza", UtilidadTexto.getBoolean(mundoConvenio.getCheckInfoAdicCuenta())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("requiereCarnet", mundoConvenio.getRequiereNumeroCarnet());
		ingresoPacienteForm.setCuenta("semanasMinimasCotizacion", mundoConvenio.getSemanasMinimasCotizacion());
		ingresoPacienteForm.setCuenta("esConvenioVerificacion",mundoConvenio.isRequiereVerificacionDerechos()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("esConvenioColsanitas", UtilidadesFacturacion.esConvenioColsanitas(con, mundoConvenio.getCodigo(), usuarioActual.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("esReporteAtencionInicialUrgencias", mundoConvenio.getReporte_atencion_ini_urg());
		ingresoPacienteForm.setCuenta("esRequiereAutorizacionServicio", UtilidadTexto.getBoolean(mundoConvenio.getRequiere_autorizacion_servicio())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		ingresoPacienteForm.setCuenta("isConvenioManejaMontos",mundoConvenio.isConvenioManejaMontoCobro());
		
		if(ingresoPacienteForm.getContratos().size()==1){
			final int unicoContrato=0;
			verificarMontoCobro(ingresoPacienteForm,ingresoPacienteForm.getContratos().get(unicoContrato).get("codigo").toString());
		}
		
		//Se verifica si se maneja complejidad
		ingresoPacienteForm.setCuenta("indicadorManejaComplejidad", validacionManejoComplejidad(ingresoPacienteForm));
		
		aux = "<indicativo-soat>"+ingresoPacienteForm.getCuenta("esConvenioSoat")+"</indicativo-soat>";
		aux += "<indicativo-complejidad>"+ingresoPacienteForm.getCuenta("indicadorManejaComplejidad")+"</indicativo-complejidad>";
		aux += "<indicativo-poliza>"+ingresoPacienteForm.getCuenta("esConvenioPoliza")+"</indicativo-poliza>";
		aux += "<indicativo-colsanitas>"+ingresoPacienteForm.getCuenta("esConvenioColsanitas")+"</indicativo-colsanitas>";
		aux += "<indicativo-autorizacion>"+ingresoPacienteForm.getCuenta("esRequiereAutorizacionServicio")+"</indicativo-autorizacion>";
		aux += "<indicativo-manejamontocobro>"+ingresoPacienteForm.getCuenta("isConvenioManejaMontos")+"</indicativo-manejamontocobro>";
		mapaRespuesta.put("respuestaValidaciones", aux);
		//*******************************************************************************************
		//****************************	VALIDAR SIN CONTRATO Y CONTROLA ANTICIPOS************************
		ResultadoBoolean resultado = new ResultadoBoolean(false,""); 
		//Solo sirve si el convenio solo tiene 1 contrato
		if(ingresoPacienteForm.getContratos().size()==1)
			resultado = validacionSinContratoControlAnticipos(con, Integer.parseInt(ingresoPacienteForm.getContratos().get(0).get("codigo")+""));
		aux = "<alerta-contrato>";
		if(resultado.isTrue())
			aux += resultado.getDescripcion();
		aux += "</alerta-contrato>";
		mapaRespuesta.put("respuestaAlertaContrato", aux);
		//***********************************************************************************
		
		logger.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX888888");
		
		//************************** COBERTURAS SALUD ********************************************
		aux = "<coberturas-salud>";
		ingresoPacienteForm.setCoberturasSalud(UtilidadesManejoPaciente.obtenerCoberturasSaludXTipoRegimen(con, ingresoPacienteForm.getCuenta("codigoTipoRegimen").toString(), usuarioActual.getCodigoInstitucionInt()));
		for(HashMap<String, Object> elemento:ingresoPacienteForm.getCoberturasSalud())
		{
			aux += "<cobertura-salud>";
			aux += "<codigo>"+elemento.get("codigo")+"</codigo>";
			aux += "<nombre>"+elemento.get("nombre")+"</nombre>";
			
			aux += "</cobertura-salud>";
		}
		aux += "</coberturas-salud>";
		mapaRespuesta.put("respuestaCoberturasSalud", aux);
		//******************************************************************************************
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			
			response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write(mapaRespuesta.get("respuestaPypCapitacion").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaTipoRegimen").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaContratos").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaEstratosSociales").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaTiposAfiliado").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaNaturaleza").toString());
	        ///response.getWriter().write(mapaRespuesta.get("respuestaMontosCobro").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaRequisitosPaciente").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaValidaciones").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaAlertaContrato").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaCoberturasSalud").toString());
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
		}
		UtilidadTransaccion.getTransaccion().commit();
		return null;
		
	}

	
	/**
	 * M√©todo que valida si un contrato es sin contrato o de control de anticipos
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	private ResultadoBoolean validacionSinContratoControlAnticipos(Connection con,int codigoContrato)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(false,"");
		
		String sinContrato ="";
		sinContrato = UtilidadesFacturacion.esSinContrato(con, codigoContrato);
		
		String controlAnticipos ="";
		controlAnticipos = UtilidadesFacturacion.esControlAnticipos(con, codigoContrato);
		
		
		// PREGUNTO SI EL CONTRATO TIENE ACTIVO EL CAMPO SIN CONTRATO
		if(sinContrato.equals(ConstantesBD.acronimoSi))
		{
			// PREGUNTO SI EL CONTRATO TIENE ACTIVO EL CAMPO CONTROLA ANTICIPOS
			if(controlAnticipos.equals(ConstantesBD.acronimoSi))
			{
				logger.info("Es Sin Contrato y Control Anticipos");
				resultado = new ResultadoBoolean(true, "Convenio del Paciente Sin Contrato y Contrato del Paciente Requiere Anticipo. Por Favor Verificar. ");
			}
			else
			{
				logger.info("Es solo Sin contrato");
				resultado = new ResultadoBoolean(true, "Convenio del Paciente Sin Contrato. Por Favor Verificar. ");
			}
			
		}
		else
		{
			if(controlAnticipos.equals(ConstantesBD.acronimoSi))
			{
				logger.info("Es Sin Contrato y Control Anticipos");
				resultado = new ResultadoBoolean(true, "Contrato del Paciente Requiere Anticipo. Por Favor Verificar. ");
				
			}
		}
		
		return resultado;
	}


	/**
	 * Se verifica el manejo de la complejidad
	 * @param ingresoPacienteForm
	 * @return
	 */
	private String validacionManejoComplejidad(IngresoPacienteForm ingresoPacienteForm) 
	{
		String manejoComplejidad = ConstantesBD.acronimoNo;
		
		if(ingresoPacienteForm.getCuenta("manejoComplejidad")!=null&&UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("manejoComplejidad").toString()))
			manejoComplejidad = ConstantesBD.acronimoSi;
		
		//Se verifica el manejo de complejidad en los demÔøΩs convenios
		for(int i=0;i<ingresoPacienteForm.getNumConvenios();i++)
		{
			if(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i)!=null&&
				UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("manejoComplejidad_"+i)+""))
				manejoComplejidad = ConstantesBD.acronimoSi;
		}
			
		return manejoComplejidad;
	}

	/**
	 * MÔøΩtodo que realiza el filtro al seleccionar la via de ingreso
	 * @param con
	 * @param ingresoPacienteForm
	 * @param usuarioActual 
	 * @param paciente 
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionFiltroViaIngreso(
			Connection con, 
			IngresoPacienteForm ingresoPacienteForm, 
			UsuarioBasico usuarioActual, 
			PersonaBasica paciente, 
			HttpServletResponse response) throws IPSException 
	{
		HashMap mapaRespuesta = new HashMap();
		String aux = "";
		HashMap mapaAux = new HashMap();	
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		
		//***********CONSULTA DE LOS TIPOS DE PACIENTE****************************************************
		ingresoPacienteForm.setTiposPaciente(UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, ingresoPacienteForm.getCodigoViaIngreso()));
		aux = "<tipos-paciente>";

		for(int i=0;i<ingresoPacienteForm.getTiposPaciente().size();i++)
		{
			mapaAux = (HashMap)ingresoPacienteForm.getTiposPaciente().get(i);
			aux += "<tipo-paciente>";
			aux += "<codigo>"+mapaAux.get("codigoTipoPaciente")+"</codigo>";
			aux += "<nombre>"+mapaAux.get("nombreTipoPaciente")+"</nombre>";
			aux += "</tipo-paciente>";
		}
		
		aux += "</tipos-paciente>";
		mapaRespuesta.put("respuestaTipoPaciente", aux);
		//*******************************************************************************************************
		//**********CONSULTA DE LAS AREAS DEL PACIENTE************************************************************
		
		aux = "<areas>";
		
		if (ingresoPacienteForm.getTiposPaciente().size()==1)
		{
			mapaAux = (HashMap)ingresoPacienteForm.getTiposPaciente().get(0);
			ingresoPacienteForm.setAreas(Utilidades.getCentrosCostoXViaIngresoXCAtencion(con, Integer.parseInt(ingresoPacienteForm.getCodigoViaIngreso()), usuarioActual.getCodigoCentroAtencion(), usuarioActual.getCodigoInstitucionInt(), mapaAux.get("codigoTipoPaciente").toString()));
			for(int i=0;i<Integer.parseInt(ingresoPacienteForm.getAreas("numRegistros").toString());i++)
			{
				aux+="<area>";
				aux += "<codigo>"+ingresoPacienteForm.getAreas("codigo_"+i)+"</codigo>";
				aux += "<nombre>"+ingresoPacienteForm.getAreas("nombre_"+i)+"</nombre>";
				aux+="</area>";
			}
		}

		aux+="</areas>";
		mapaRespuesta.put("respuestaAreas", aux);
		//*********************************************************************************************************
		//********CONSULTA DE LOS CAMPOS ADICIONALES DE LA VIA DE INGRESO*****************************************
		ViasIngreso mundoViaIngreso = new ViasIngreso();
		HashMap datosViaIng = mundoViaIngreso.consultarViasIngresoEspecifico(con, Integer.parseInt(ingresoPacienteForm.getCodigoViaIngreso()));
		//1) Indicativo de la verificacion
		ingresoPacienteForm.setCuenta("esViaIngresoVerificacion",UtilidadTexto.getBoolean(datosViaIng.get("verificacion_0")+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		//2) Indicativo de requerido responsable paciente
		ingresoPacienteForm.setCuenta("esRequeridoResponsablePaciente",UtilidadTexto.getBoolean(datosViaIng.get("paciente_0")+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		//3) Se agrega el convenio por defecto
		ingresoPacienteForm.setCuenta("codigoConvenioDefault", datosViaIng.get("convenio_0").toString());
		if(this.agregaConvenioDefault(con,ingresoPacienteForm,datosViaIng.get("convenio_0").toString(),usuarioActual,paciente))
		{
			//se obtiene el nÔøΩmero de convenios adicionales
			int pos = Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numRegistros").toString());
			int nroPrioridad = obtenerPrioridadConvenioDefault(ingresoPacienteForm);
			
			pos--;
			aux = "" +
			"<convenio-default>" +
				"<fue-agregado>"+ConstantesBD.acronimoSi+"</fue-agregado>" +
				"<es-principal>"+ConstantesBD.acronimoNo+"</es-principal>" +
				"<posicion>"+pos+"</posicion>" + //se manda la posicion del mapa VariosConvenios
				"<nombre-convenio>"+ingresoPacienteForm.getVariosConvenios("nombreConvenio_"+pos)+"</nombre-convenio>" +
				"<codigo-convenio>"+ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+pos)+"</codigo-convenio>" +
				"<nro-prioridad>"+nroPrioridad+"</nro-prioridad>" +
			"</convenio-default>";
			
		}
		else
		{
			String codigoConvDefault = datosViaIng.get("convenio_0").toString();
			int nroPrioridad = obtenerPrioridadConvenioDefault(ingresoPacienteForm);
			
			aux = "" +
			"<convenio-default>" +
				"<fue-agregado>"+ConstantesBD.acronimoNo+"</fue-agregado>" +
				"<codigo-convenio>"+(UtilidadCadena.noEsVacio(codigoConvDefault)?codigoConvDefault:"")+"</codigo-convenio>" +
				"<nro-prioridad>"+nroPrioridad+"</nro-prioridad>" +
			"</convenio-default>";
		}
		
		aux += "<indicador-responsable>"+ingresoPacienteForm.getCuenta("esRequeridoResponsablePaciente")+"</indicador-responsable>";
		mapaRespuesta.put("respuestaAdicionales", aux);
		//********************************************************************************************************
		//*************FILTRO DE LOS REQUISITOS DEL PACIENTE DEL CONVENIO PRINCIPAL*********************************
		
		String[] datosConvenio = ingresoPacienteForm.getCodigoConvenio().split(ConstantesBD.separadorSplit);
		
		//Se consultan los requisitos de ingreso
		arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
			con,
			Utilidades.convertirAEntero(datosConvenio[0]), 
			ConstantesIntegridadDominio.acronimoIngreso, 
			true, 
			usuarioActual.getCodigoInstitucionInt(),
			Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()));
		ingresoPacienteForm.setCuenta("numReqIngreso", arregloAux.size()+"");
		aux = "<requisitos-ingreso>";
		for(int i=0;i<arregloAux.size();i++)
		{
			HashMap elemento = (HashMap)arregloAux.get(i);
			ingresoPacienteForm.setCuenta("codigoReqIngreso_"+i, elemento.get("codigo"));
			ingresoPacienteForm.setCuenta("descripcionReqIngreso_"+i, elemento.get("descripcion"));
			ingresoPacienteForm.setCuenta("cumplidoReqIngreso_"+i, UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("cumplidoReqIngreso_"+i)+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			aux += "<requisito-ingreso>";
			aux += "<codigo>"+elemento.get("codigo")+"</codigo>";
			aux += "<descripcion>"+elemento.get("descripcion")+"</descripcion>";
			aux += "<cumplido>"+ingresoPacienteForm.getCuenta("cumplidoReqIngreso_"+i)+"</cumplido>";
			aux += "</requisito-ingreso>";
		}
		aux += "</requisitos-ingreso>";
		//Se consultan los requisitos del egreso
		arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
				con,
				Utilidades.convertirAEntero(datosConvenio[0]), 
				ConstantesIntegridadDominio.acronimoEgreso, 
				true, 
				usuarioActual.getCodigoInstitucionInt(),
				Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()));
		ingresoPacienteForm.setCuenta("numReqEgreso", arregloAux.size()+"");
		aux += "<requisitos-egreso>";
		for(int i=0;i<arregloAux.size();i++)
		{
			HashMap elemento = (HashMap)arregloAux.get(i);
			ingresoPacienteForm.setCuenta("codigoReqEgreso_"+i, elemento.get("codigo"));
			ingresoPacienteForm.setCuenta("descripcionReqEgreso_"+i, elemento.get("descripcion"));
			ingresoPacienteForm.setCuenta("cumplidoReqEgreso_"+i, UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("cumplidoReqEgreso_"+i)+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			aux += "<requisito-egreso>";
			aux += "<codigo>"+elemento.get("codigo")+"</codigo>";
			aux += "<descripcion>"+elemento.get("descripcion")+"</descripcion>";
			aux += "<cumplido>"+ingresoPacienteForm.getCuenta("cumplidoReqEgreso_"+i)+"</cumplido>";
			aux += "</requisito-egreso>";
		}
		aux += "</requisitos-egreso>";
		
		ingresoPacienteForm.setCuenta("tieneRequisitosPaciente", 
			(Integer.parseInt(ingresoPacienteForm.getCuenta("numReqIngreso").toString())>0||Integer.parseInt(ingresoPacienteForm.getCuenta("numReqEgreso").toString())>0)?
				ConstantesBD.acronimoSi:
				ConstantesBD.acronimoNo);
		aux += "<indicativo-requisitos>"+ingresoPacienteForm.getCuenta("tieneRequisitosPaciente")+"</indicativo-requisitos>";
		
		mapaRespuesta.put("respuestaRequisitosPaciente", aux);
		
		//**********************************************************************************************************
		//*********CONSULTA DE LOS ESTRATOS SOCIALES*****************************************************
		if(Utilidades.convertirAEntero(datosConvenio[0])>0)
		{
			ingresoPacienteForm.setEstratosSociales(UtilidadesFacturacion.cargarEstratosSociales(
					con, 
					usuarioActual.getCodigoInstitucionInt(),
					ConstantesBD.acronimoSi, 
					Utilidades.obtenerCodigoTipoRegimenConvenio(con, datosConvenio[0]),
					Utilidades.convertirAEntero(datosConvenio[0]),
					Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()),
					Utilidades.capturarFechaBD()));

			if (Integer.valueOf(ingresoPacienteForm.getEstratosSociales().get("numRegistros").toString()) > 0) {

				int estratoSocial = ConstantesBD.codigoNuncaValido;
				String tipoAfiliado = "";
				if (ingresoPacienteForm.getEstratosSociales().get("codigo_0") != null) {
					estratoSocial = Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales().get("codigo_0").toString());
				}
				
				//*********CONSULTA DE LOS TIPOS AFILIADO*****************************************************
				HashMap tiposAfiliado = UtilidadesFacturacion.cargarTiposAfiliadoXEstrato(
						con,  usuarioActual.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, 
						Utilidades.convertirAEntero(datosConvenio[0]),Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()),
						estratoSocial, Utilidades.capturarFechaBD());
				
				ingresoPacienteForm.setTiposAfiliado(tiposAfiliado);

				if (ingresoPacienteForm.getTiposAfiliado().get("acronimo_0") != null) {
					tipoAfiliado = ingresoPacienteForm.getTiposAfiliado().get("acronimo_0").toString();
				}
				
				//9) Se consultan las naturalezas del paciente
				ingresoPacienteForm.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
						con, Utilidades.obtenerCodigoTipoRegimenConvenio(con, datosConvenio[0]), 
						Utilidades.convertirAEntero(datosConvenio[0]),
						Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()), 
						tipoAfiliado, 
						estratoSocial, Utilidades.capturarFechaBD()));

				/*HashMap tiposAfiliado=UtilidadesFacturacion.cargarTiposAfiliado(con, usuarioActual.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, Integer.parseInt(datosConvenio[0]),Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()));
				ingresoPacienteForm.setTiposAfiliado(tiposAfiliado);

				ingresoPacienteForm.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPaciente(
						con,Utilidades.obtenerCodigoTipoRegimenConvenio(con, datosConvenio[0]),
						Integer.parseInt(datosConvenio[0]),
						Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso())));	*/

				if(Utilidades.convertirAEntero(datosConvenio[1])==ConstantesBD.codigoTipoContratoCapitado)
				{
					//si solo tiene un contrato vigente se debe postular la informacion
					if(ingresoPacienteForm.getContratos().size()==1)
					{
						String  codigoRegimen = "";
						String[] vecAux = new String[0];

						vecAux = Utilidades.obtenerTipoRegimenConvenio(con, datosConvenio[0]).split("-");
						codigoRegimen = vecAux[0];

						int codClasi=Utilidades.convertirAEntero(ingresoPacienteForm.getContratos().get(0).get("codigoestratosocial")+"");
						if(codClasi>0)
						{
							String tipoRegimenCSE=Utilidades.obtenerTipoRegimenClasificacionSocioEconomica(con, codClasi);
							if(codigoRegimen.equals(tipoRegimenCSE))
							{
								//Estratos sociales
								ingresoPacienteForm.setCuenta("codigoEstratoSocial", codClasi);
								HashMap tempo=new HashMap();
								tempo.put("numRegistros", "0");
								HashMap tempo1=new HashMap();
								tempo1.put("numRegistros", "0");
								for(int i=0;i<Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales("numRegistros")+"");i++)
								{
									if(Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales("codigo_"+i)+"")==codClasi)
									{
										tempo.put("codigo_0", ingresoPacienteForm.getEstratosSociales("codigo_"+i)+"");
										tempo.put("descripcion_0", ingresoPacienteForm.getEstratosSociales("descripcion_"+i)+"");
										tempo.put("numRegistros", "1");
										break;
									}
								}

								if(Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales().get("numRegistros")+"")<=0)
								{
									aux = "<error>La ClasificaciÛn socio econÛmica del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
									UtilidadBD.closeConnection(con);
									//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
									try
									{
										response.setCharacterEncoding("UTF-8");
										response.setContentType("text/xml");
										response.setHeader("Cache-Control", "no-cache");
										response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
										response.getWriter().write("<respuesta>");
										response.getWriter().write(aux);
										response.getWriter().write("</respuesta>");
									}
									catch(IOException e)
									{
										logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
									}
									return null;
								}
								
								//Tipo afiliado
								String tipoAfil=ingresoPacienteForm.getContratos().get(0).get("tipoafiliado")+"";

								//{numRegistros=2, acronimo_0=B, nombre_1=Cotizante, nombre_0=Beneficiario, acronimo_1=C}
								ingresoPacienteForm.setCuenta("codigoTipoAfiliado", tipoAfil);

								for(int i=0;i<Utilidades.convertirAEntero(ingresoPacienteForm.getTiposAfiliado("numRegistros")+"");i++)
								{
									if((ingresoPacienteForm.getTiposAfiliado("acronimo_"+i)+"").equals(tipoAfil+""))
									{
										tempo1.put("acronimo_0", ingresoPacienteForm.getTiposAfiliado("acronimo_"+i)+"");
										tempo1.put("nombre_0", ingresoPacienteForm.getTiposAfiliado("nombre_"+i)+"");
										tempo1.put("numRegistros", "1");
										break;
									}
								}
								//MT 5470
								if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitados(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
								{
									ingresoPacienteForm.setEstratosSociales(tempo);
									if(!UtilidadTexto.isEmpty(tipoAfil))
									{
										ingresoPacienteForm.setTiposAfiliado(tempo1);
									}
								}	
								
								if(Utilidades.convertirAEntero(ingresoPacienteForm.getTiposAfiliado("numRegistros")+"")<=0)
								{
									aux = "<error>El Tipo Afiliado del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
									UtilidadBD.closeConnection(con);
									//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
									try
									{
										response.setCharacterEncoding("UTF-8");
										response.setContentType("text/xml");
										response.setHeader("Cache-Control", "no-cache");
										response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
										response.getWriter().write("<respuesta>");
										response.getWriter().write(aux);
										response.getWriter().write("</respuesta>");
									}
									catch(IOException e)
									{
										logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
									}
									return null;

								}
								
								//Naturaleza paciente
								int natPaciente = Utilidades.convertirAEntero(ingresoPacienteForm.getContratos().get(0).get("naturalezapaciente")+"");
								
								ingresoPacienteForm.setCuenta("codigoNaturaleza", natPaciente+"");
								
								if (!UtilidadTexto.isEmpty(codClasi) && !UtilidadTexto.isEmpty(tipoAfil)) {
									ingresoPacienteForm.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
											con, Utilidades.obtenerCodigoTipoRegimenConvenio(con, datosConvenio[0]),
											Integer.parseInt(datosConvenio[0]),
											Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()), 
											tipoAfil, 
											codClasi, Utilidades.capturarFechaBD()));
								}
								
								Vector<InfoDatosString> naturalezaVector=new Vector<InfoDatosString>();
								
								for(int i=0;i<ingresoPacienteForm.getNaturalezasPaciente().size();i++)
								{
									if(Utilidades.convertirAEntero(ingresoPacienteForm.getNaturalezasPaciente().get(i).getCodigo()) ==natPaciente)
									{
										naturalezaVector.add(ingresoPacienteForm.getNaturalezasPaciente().get(i));
									}
								}

								if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitados(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
								{
									/*if (!tempo.isEmpty()) {
										ingresoPacienteForm.setEstratosSociales(tempo);
									}
									if(!UtilidadTexto.isEmpty(tipoAfil)) {
										ingresoPacienteForm.setTiposAfiliado(tempo1);
									}
									if (!naturalezaVector.isEmpty()) {
										ingresoPacienteForm.setNaturalezasPaciente(naturalezaVector);
									}*/
								}
								
								if(ingresoPacienteForm.getNaturalezasPaciente().size()<=0) {
									ingresoPacienteForm.setCuenta("codigoNaturaleza", natPaciente+"");
									ingresoPacienteForm.setNaturalezasPaciente(new Vector<InfoDatosString>());
									aux = "<error>La Naturaleza Paciente del paciente no corresponde con los Montos Cobro del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
									UtilidadBD.closeConnection(con);
									//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
									try
									{
										response.setCharacterEncoding("UTF-8");
										response.setContentType("text/xml");
										response.setHeader("Cache-Control", "no-cache");
										response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
										response.getWriter().write("<respuesta>");
										response.getWriter().write(aux);
										response.getWriter().write("</respuesta>");
									}
									catch(IOException e)
									{
										logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
									}
									return null;
								}
								
								if (natPaciente != ConstantesBD.codigoNuncaValido && 
										!UtilidadValidacion.esNaturalezaValidaTipoRegimen(natPaciente,codigoRegimen+"")) {
									aux = "<error>El Tipo de rÈgimen de la Naturaleza Paciente no corresponde con el Tipo de rÈgimen del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
									UtilidadBD.closeConnection(con);
									//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
									try
									{
										response.setCharacterEncoding("UTF-8");
										response.setContentType("text/xml");
										response.setHeader("Cache-Control", "no-cache");
										response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
										response.getWriter().write("<respuesta>");
										response.getWriter().write(aux);
										response.getWriter().write("</respuesta>");
									}
									catch(IOException e)
									{
										logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
									}
									return null;

								}
							}
							else
							{
								ingresoPacienteForm.setEstratosSociales(new HashMap());
								ingresoPacienteForm.setTiposAfiliado(new HashMap());

								aux = "<error>El Tipo de rÈgimen de la ClasificaciÛn socio  econÛmica no corresponde con el Tipo de rÈgimen del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
								UtilidadBD.closeConnection(con);
								//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
								try
								{
									response.setCharacterEncoding("UTF-8");
									response.setContentType("text/xml");
									response.setHeader("Cache-Control", "no-cache");
									response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
									response.getWriter().write("<respuesta>");
									response.getWriter().write(aux);
									response.getWriter().write("</respuesta>");
								}
								catch(IOException e)
								{
									logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
								}
								return null;
							}
							
						}
					}
				}
			} else {
				//ingresoPacienteForm.setTiposAfiliado(new HashMap());
				//ingresoPacienteForm.setNaturalezasPaciente(new Vector<InfoDatosString>());
			}
			aux = "<estratos-sociales>";
			for(int i=0;i<Integer.parseInt(ingresoPacienteForm.getEstratosSociales("numRegistros").toString());i++)
				aux += "" +
					"<estrato-social>" +
						"<codigo>"+ingresoPacienteForm.getEstratosSociales("codigo_"+i)+"</codigo>" +
						"<nombre>"+ingresoPacienteForm.getEstratosSociales("descripcion_"+i)+"</nombre>" +
					"</estrato-social>";
			aux += "</estratos-sociales>";
			mapaRespuesta.put("respuestaEstratosSociales", aux);
			aux = "<tipos-afiliado>";
			for(int i=0;i<Integer.parseInt(ingresoPacienteForm.getTiposAfiliado().get("numRegistros").toString());i++)
				aux += "" +
					"<tipo-afiliado>" +
						"<acronimo>"+ingresoPacienteForm.getTiposAfiliado().get("acronimo_"+i)+"</acronimo>" +
						"<nombre>"+ingresoPacienteForm.getTiposAfiliado().get("nombre_"+i)+"</nombre>" +
					"</tipo-afiliado>";
			aux += "</tipos-afiliado>";
			mapaRespuesta.put("respuestaTiposAfiliado", aux);
			aux = "<naturalezas-paciente>";
			for(int i=0;i<ingresoPacienteForm.getNaturalezasPaciente().size();i++)
				aux += "" +
					"<naturaleza-paciente>" +
						"<codigo>"+ingresoPacienteForm.getNaturalezasPaciente().get(i).getCodigo()+"</codigo>" +
						"<nombre>"+ingresoPacienteForm.getNaturalezasPaciente().get(i).getNombre()+"</nombre>" +
					"</naturaleza-paciente>";
			aux += "</naturalezas-paciente>";
			mapaRespuesta.put("respuestaNaturaleza", aux);
		}
		else
		{
			aux = "<estratos-sociales>";
			aux += "</estratos-sociales>";
			mapaRespuesta.put("respuestaEstratosSociales", aux);
			aux = "<tipos-afiliado>";
			aux += "</tipos-afiliado>";
			mapaRespuesta.put("respuestaTiposAfiliado", aux);
			aux = "<naturalezas-paciente>";
			aux += "</naturalezas-paciente>";
			mapaRespuesta.put("respuestaNaturaleza", aux);
		}
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			response.getWriter().write("<respuesta>");
	        response.getWriter().write(mapaRespuesta.get("respuestaTipoPaciente").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaAreas").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaAdicionales").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaRequisitosPaciente").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaEstratosSociales").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaNaturaleza").toString());
	        if(mapaRespuesta.containsKey("respuestaTiposAfiliado"))
	        	response.getWriter().write(mapaRespuesta.get("respuestaTiposAfiliado").toString());
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroViasIngreso: "+e);
		}
		UtilidadTransaccion.getTransaccion().commit();
		return null;
	}


	
	/**
	 * MÔøΩtodo que realiza el filtro al seleccionar el tipo de paciente
	 * @param con
	 * @param ingresoPacienteForm
	 * @param usuarioActual 
	 * @param paciente 
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionFiltroTipoPaciente(Connection con, IngresoPacienteForm ingresoPacienteForm, UsuarioBasico usuarioActual, PersonaBasica paciente, HttpServletResponse response) 
	{
		HashMap mapaRespuesta = new HashMap();
		String aux = "";
		
		aux = "<areas>";

		ingresoPacienteForm.setAreas(Utilidades.getCentrosCostoXViaIngresoXCAtencion(con, Integer.parseInt(ingresoPacienteForm.getCodigoViaIngreso()), usuarioActual.getCodigoCentroAtencion(), usuarioActual.getCodigoInstitucionInt(), ingresoPacienteForm.getCodigoTipoPaciente()));
		for(int i=0;i<Integer.parseInt(ingresoPacienteForm.getAreas("numRegistros").toString());i++)
		{
			aux+="<area>";
			aux += "<codigo>"+ingresoPacienteForm.getAreas("codigo_"+i)+"</codigo>";
			aux += "<nombre>"+ingresoPacienteForm.getAreas("nombre_"+i)+"</nombre>";
			aux+="</area>";
		}

		aux+="</areas>";
		mapaRespuesta.put("respuestaAreas", aux);
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
	        response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			response.getWriter().write("<respuesta>");
	        
	        response.getWriter().write(mapaRespuesta.get("respuestaAreas").toString());
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroTipoPaciente: "+e);
		}
		return null;
	}


	/**
	 * MÔøΩtodo implementado para obtener la prioridad de un convenio default
	 * @param ingresoPacienteForm
	 * @return
	 */
	private int obtenerPrioridadConvenioDefault(IngresoPacienteForm ingresoPacienteForm) 
	{
		int nroPrioridad = ConstantesBD.codigoNuncaValido;
		int contador = 1;
		
		for(int i=0;i<Utilidades.convertirAEntero(ingresoPacienteForm.getVariosConvenios("numRegistros")+"", true);i++)
			if(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i)!=null)
			{
				contador ++;
				if(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i).toString().equals(ingresoPacienteForm.getCuenta("codigoConvenioDefault").toString()))
					nroPrioridad = contador;
			}
				
		
		return nroPrioridad;
	}



	/**
	 * MÔøΩtodo implementado para guardar la informacion de un paciente
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mapping
	 * @param usuarioActual
	 * @param paciente
	 * @param request
	 * @param response 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionGuardarPaciente(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionMapping mapping, UsuarioBasico usuarioActual, PersonaBasica paciente, HttpServletRequest request, HttpServletResponse response) throws IPSException 
	{ 
		ActionErrors errores = new ActionErrors();
	   
		//****************VALIDACION DE CAMPOS******************************************************
		//PaÔøΩs de expedicion
		if(ingresoPacienteForm.getCodigoPaisId().equals(""))
			errores.add("Pais de expedicion es requerido", new ActionMessage("errors.required","El paÌs de expediciÛn"));
		
		//Ciudad de expediciÔøΩn
		if(ingresoPacienteForm.getCodigoCiudadId().equals(""))
			errores.add("Ciudad de expedicion es requerido", new ActionMessage("errors.required","La ciudad de expediciÛn"));
		
		//Se quitan los espacios de los campos del nombre
		ingresoPacienteForm.setPrimerApellidoPersona(ingresoPacienteForm.getPrimerApellidoPersona().trim());
		ingresoPacienteForm.setSegundoApellidoPersona(ingresoPacienteForm.getSegundoApellidoPersona().trim());
		ingresoPacienteForm.setPrimerNombrePersona(ingresoPacienteForm.getPrimerNombrePersona().trim());
		ingresoPacienteForm.setSegundoNombrePersona(ingresoPacienteForm.getSegundoNombrePersona().trim());
		
		//Primer apellido
		if(ingresoPacienteForm.getPrimerApellidoPersona().equals(""))
			errores.add("primer apellido es requerido", new ActionMessage("errors.required","El primer apellido"));
		//Primer nombre
		if(ingresoPacienteForm.getPrimerNombrePersona().equals(""))
			errores.add("primer nombre es requerido", new ActionMessage("errors.required","El primer nombre"));
		
		//PaÔøΩs nacimiento
		if(ingresoPacienteForm.getCodigoPaisNacimiento().equals(""))
			errores.add("Pais de expedicion es nacimiento", new ActionMessage("errors.required","El paÌs de nacimiento"));
		
		//Ciudad nacimiento
		if(ingresoPacienteForm.getCodigoCiudadNacimiento().equals(""))
			errores.add("Ciudad de nacimiento es requerido", new ActionMessage("errors.required","La ciudad de nacimiento"));
		
		//Fecha de nacimiento
		if(ingresoPacienteForm.getFechaNacimiento().equals(""))
			errores.add("Fecha de nacimiento es requerido", new ActionMessage("errors.required","La fecha de nacimiento"));
		else
		{
			//Se verifica que la fecha sea vÔøΩlida
			if(!UtilidadFecha.validarFecha(ingresoPacienteForm.getFechaNacimiento()))
				errores.add("Fecha nacimiento invÔøΩlida", new ActionMessage("errors.formatoFechaInvalido","de nacimiento"));
			//Se verifica que la fecha sea menor o igual a la actual
			else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(ingresoPacienteForm.getFechaNacimiento(), UtilidadFecha.getFechaActual()))
					errores.add("FEcha nacimiento es mayor a fecha actual", new ActionMessage("errors.fechaPosteriorIgualActual","de nacimiento","actual"));
			
		}
		
		//Pais de residencia
		if(ingresoPacienteForm.getCodigoPaisResidencia().equals(""))
			errores.add("Pais de residencia es requerido", new ActionMessage("errors.required","El paÌs de residencia"));
		
		//Ciudad de residencia
		if(ingresoPacienteForm.getCodigoCiudadResidencia().equals(""))
			errores.add("Ciudad de residencia es requerido", new ActionMessage("errors.required","La ciudad de residencia"));
		
		//Barrio 
		if(ingresoPacienteForm.getCodigoBarrio().equals(""))
			errores.add("Barrios es requirido",new ActionMessage("errors.required","El barrio"));
		
		//Direccion
		ingresoPacienteForm.setDireccion(ingresoPacienteForm.getDireccion().trim());
		if(ingresoPacienteForm.getDireccion().equals(""))
			errores.add("DirecciÔøΩn es requerida",new ActionMessage("errors.required","La direcciÛn"));
		
		//TelÔøΩfono celular
		if(!ingresoPacienteForm.getTelefonoCelular().equals("")&&Utilidades.convertirADouble(ingresoPacienteForm.getTelefonoCelular())==ConstantesBD.codigoNuncaValidoDoubleNegativo)
		{
			errores.add("Telefono celular", new ActionMessage("errors.integer","El telÈfono celular"));
		}
		
		//Zona Domicilio
		if(ingresoPacienteForm.getZonaDomicilio().equals(""))
			errores.add("Zona domicilio es requerida",new ActionMessage("errors.required","La zona de domicilio"));
		
		//OcupaciÔøΩn
		if(ingresoPacienteForm.getOcupacion().equals(""))
			errores.add("OcupaciÛn es requerida",new ActionMessage("errors.required","La ocupaciÛn"));
		
		//Sexo
		if(ingresoPacienteForm.getSexo().equals(""))
			errores.add("Sexo es requerido",new ActionMessage("errors.required","El sexo"));
		
		if(UtilidadTexto.getBoolean(ingresoPacienteForm.getPacienteTriage())&&ingresoPacienteForm.getClasificacionesTriage().size()>0&&ingresoPacienteForm.getClasificacionTriage()<0)
     	{
			errores.add("clasi. triage",new ActionMessage("errors.required","La ClasificaciÛn Triage"));
     	}
		
		//******************************************************************************************
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("ingresarPaciente");
		}
		
		
		//Se toma la informacion del paciente para Triage
		ingresoPacienteForm.setEsPacienteTriage(UtilidadTexto.getBoolean(ingresoPacienteForm.getPacienteTriage()));
		
		
		//Se instancia objeto paciente
		Paciente mundoPaciente = new Paciente();
		
		//**********VERIFICACION NOMBRES PACIENTE*****************************************
		//Se consultan pacientes que tienen el mismo nombre, solo se consulta si el indicador no estÔøΩ activado
		if(!ingresoPacienteForm.isAvisoValidacionMismosNombres()&&!ingresoPacienteForm.isExistePaciente())
		{
			ingresoPacienteForm.setPacientesMismosNombres(mundoPaciente.validarPacienteIgualNombre(
				con, 
				ingresoPacienteForm.getPrimerNombrePersona(), 
				ingresoPacienteForm.getSegundoNombrePersona(), 
				ingresoPacienteForm.getPrimerApellidoPersona(), 
				ingresoPacienteForm.getSegundoApellidoPersona(),
				ingresoPacienteForm.getTipoIdentificacion(),
				ingresoPacienteForm.getNumeroIdentificacion()));
			
			if(ingresoPacienteForm.getPacientesMismosNombres().size()>0)
			{
				//se maneja una variable para mostrarlo en un pop-up
				//*************	SE AGREGA ADVERTENCIA SOBRE LOS PACIENTES CON MISMOS NOMBRES***********************
				//ElementoApResource alerta = new ElementoApResource("errors.notEspecific");
				String contenido = "Los nombres y apellidos <b>"+ingresoPacienteForm.getPrimerNombrePersona()+" "+ingresoPacienteForm.getSegundoNombrePersona()+" "+ingresoPacienteForm.getPrimerApellidoPersona()+" "+ingresoPacienteForm.getSegundoApellidoPersona()+"</b> ya han sido ingresados para los siguientes paciente: <br> ";
				contenido+=" <ul> ";

				for(HashMap elemento:ingresoPacienteForm.getPacientesMismosNombres())
					contenido += "<li>"+elemento.get("nombrePersona")+" "+elemento.get("tipoId") + ". " + elemento.get("numeroId") + " </li> ";
				contenido+=" </ul> ";
				contenido += "øDesea Continuar?";
				//alerta.agregarAtributo(contenido);
				//ingresoPacienteForm.getMensajesAlerta().add(alerta);
				//**************************************************************************************************
				
				ingresoPacienteForm.setMensajePacientesIgualNNombre(contenido);
				
				//ingresoPacienteForm.setAvisoValidacionMismosNombres(true);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("ingresarPaciente");
			}
		}
		//**********************************************************************************
		
		//Se llena el mundo del paciente con los datos del form
		mundoPaciente.setNumeroHistoriaClinica(ingresoPacienteForm.getNumeroHistoriaClinica());
		mundoPaciente.setAnioHistoriaClinica(ingresoPacienteForm.getAnioHistoriaClincia());
		mundoPaciente.setCodigoTipoIdentificacion(ingresoPacienteForm.getCodigoTipoIdentificacion());
		mundoPaciente.setNumeroIdentificacion(ingresoPacienteForm.getNumeroIdentificacion());
		mundoPaciente.setCodigoDepartamentoId(ingresoPacienteForm.getCodigoCiudadId().split(ConstantesBD.separadorSplit)[0]);
		mundoPaciente.setCodigoCiudadId(ingresoPacienteForm.getCodigoCiudadId().split(ConstantesBD.separadorSplit)[1]);
		mundoPaciente.setCodigoPaisId(ingresoPacienteForm.getCodigoPaisId());
		mundoPaciente.setPrimerApellidoPersona(ingresoPacienteForm.getPrimerApellidoPersona());
		mundoPaciente.setSegundoApellidoPersona(ingresoPacienteForm.getSegundoApellidoPersona());
		mundoPaciente.setPrimerNombrePersona(ingresoPacienteForm.getPrimerNombrePersona());
		mundoPaciente.setSegundoNombrePersona(ingresoPacienteForm.getSegundoNombrePersona());
		mundoPaciente.setCodigoPaisIdentificacion(ingresoPacienteForm.getCodigoPaisNacimiento());
		mundoPaciente.setCodigoDepartamentoIdentificacion(ingresoPacienteForm.getCodigoCiudadNacimiento().split(ConstantesBD.separadorSplit)[0]);
		mundoPaciente.setCodigoCiudadIdentificacion(ingresoPacienteForm.getCodigoCiudadNacimiento().split(ConstantesBD.separadorSplit)[1]);
		String[] fechaNac = ingresoPacienteForm.getFechaNacimiento().split("/"); 
		mundoPaciente.setAnioNacimiento(fechaNac[2]);
		mundoPaciente.setMesNacimiento(fechaNac[1]);
		mundoPaciente.setDiaNacimiento(fechaNac[0]);
		mundoPaciente.setCodigoPais(ingresoPacienteForm.getCodigoPaisResidencia());
		mundoPaciente.setCodigoDepartamento(ingresoPacienteForm.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[0]);
		mundoPaciente.setCodigoCiudad(ingresoPacienteForm.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[1]);
		mundoPaciente.setCodigoBarrio(ingresoPacienteForm.getCodigoBarrio());
		mundoPaciente.setCodigoLocalidad(ingresoPacienteForm.getCodigoLocalidad());
		mundoPaciente.setDireccion(ingresoPacienteForm.getDireccion());
		mundoPaciente.setTelefono(ingresoPacienteForm.getTelefono());
		mundoPaciente.setTelefonoFijo(ingresoPacienteForm.getTelefonoFijo());
		mundoPaciente.setTelefonoCelular(ingresoPacienteForm.getTelefonoCelular());
		mundoPaciente.setEmail(ingresoPacienteForm.getEmail());
		mundoPaciente.setCodigoZonaDomicilio(ingresoPacienteForm.getZonaDomicilio());
		mundoPaciente.setCodigoOcupacion(ingresoPacienteForm.getOcupacion());
		mundoPaciente.setCodigoSexo(ingresoPacienteForm.getSexo());
		mundoPaciente.setCodigoTipoSangre(ingresoPacienteForm.getTipoSangre());
		mundoPaciente.setCodigoTipoPersona(ingresoPacienteForm.getTipoPersona().split("-")[0]);
		mundoPaciente.setCodigoEstadoCivil(ingresoPacienteForm.getEstadoCivil());
		mundoPaciente.setCentro_atencion(ingresoPacienteForm.getCentroAtencion().equals("")?0:Integer.parseInt(ingresoPacienteForm.getCentroAtencion()));
		mundoPaciente.setEtnia(ingresoPacienteForm.getEtnia().equals("")?0:Integer.parseInt(ingresoPacienteForm.getEtnia()));
		mundoPaciente.setCodigoReligion(ingresoPacienteForm.getReligion());
		mundoPaciente.setLee_escribe(UtilidadTexto.getBoolean(ingresoPacienteForm.getLeeEscribe()));
		mundoPaciente.setEstudio(ingresoPacienteForm.getEstudio().equals("")?0:Integer.parseInt(ingresoPacienteForm.getEstudio()));
		mundoPaciente.setCodigoGrupoPoblacional(ingresoPacienteForm.getGrupoPoblacional());
		mundoPaciente.setFoto(ingresoPacienteForm.getFoto());
		
		mundoPaciente.setInfoHistoSistemaAnt(ingresoPacienteForm.isInfoHistoSistemaAnt());
		
		//***********SI EL PACIENTE NO EXISTE QUIERE DECIR QUE SE VA A REALIZAR INSERCIÔøΩN******************************
		if(!ingresoPacienteForm.isExistePaciente())
		{
			RespuestaValidacion respuesta = new RespuestaValidacion("Error en base datos al validar el ingreso del paciente",false);
			
	        try 
			{
	        	//Se valida si se puede ingresar el paciente
				respuesta = UtilidadValidacion.validacionIngresarPaciente(
				                con,
				                mundoPaciente.getCodigoTipoIdentificacion(),
				                mundoPaciente.getNumeroIdentificacion(),
				                mundoPaciente.getCodigoBarrio()
				        );
			} catch (SQLException e) 
			{
				logger.error("Error al validar el ingreso del paciente: "+e);
			}
			//Se verifica si se puede continuar con la inserciÔøΩn del paciente
			//Se obtiene el consecutivo de historia clÌnica del paciente,
			//permitiendo finalizar el uso del consecutivo TareaXplanner2010 30650
			String consecutivoHC= mundoPaciente.getNumeroHistoriaClinica();
			if(respuesta.puedoSeguir)
			{
				//Se inicia transaccion
				UtilidadBD.iniciarTransaccion(con);
				
								
				
				
				
				
				
			    //Se ponen en comentario estas 4 lines por tarea 30012 no se necesita obtener nuevamente el consecutivo.
				/*
				consecutivoHC=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoHistoriaClinica, usuarioActual.getCodigoInstitucionInt());
				String anioConsecutivoHC=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoHistoriaClinica, usuarioActual.getCodigoInstitucionInt(), consecutivoHC);
				mundoPaciente.setNumeroHistoriaClinica(consecutivoHC); 
				mundoPaciente.setAnioHistoriaClinica(anioConsecutivoHC);
				*/
				
				//2) Se realiza la inserciÔøΩn de paciente - persona ************************************************************************
				 RespuestaInsercionPersona respInsercion = new RespuestaInsercionPersona(false,false,"",0);
				try 
				{
					respInsercion = mundoPaciente.insertarPaciente(con, "scsone@servinte.com.co", usuarioActual.getCodigoInstitucion());
				} 
				catch (SQLException e) 
				{
					logger.error("Error al insertar el paciente: "+e);
				}
	             int codigoPaciente=respInsercion.getCodigoPersona();
	            
	             
	             if(respInsercion.isSalioBien())
	             {
	                 String nuevaIdentificacion=respInsercion.getNuevaIdentificacion();

	                 TipoNumeroId identificacion = new TipoNumeroId(mundoPaciente.getCodigoTipoIdentificacion(), mundoPaciente.getNumeroIdentificacion());
	                 
	                 //Si el paciente no tiene tipo de identificacion automÔøΩtica entonces se carga en sesiÔøΩn
	                 if (!Utilidades.esAutomaticoTipoId(con,mundoPaciente.getCodigoTipoIdentificacion(),usuarioActual.getCodigoInstitucionInt()) )
	                 {
	                 	
	                        try 
	                        {
								paciente.cargar(con, identificacion);
								paciente.cargarPaciente2(con, identificacion, usuarioActual.getCodigoInstitucion(),usuarioActual.getCodigoCentroAtencion()+"");
							} 
	                        catch (SQLException e) 
	                        {
								logger.error("Error al cargar el paciente: "+e);
							}
	                         

	                         Observable observable = (Observable) request.getSession().getAttribute("observable");
	                         if (observable != null)
	                         {
	                                 paciente.setObservable(observable);
	                                 // Si ya lo habamos aadido, la siguiente lnea no hace nada
	                                 observable.addObserver(paciente);
	                         }
	                 }
	                 else
	                	 mundoPaciente.setNumeroIdentificacion(nuevaIdentificacion);
	               
	                 
	                 //3) Se realiza la insercion del paciente en las tabla pacientes_institucion y pacientes_institucion2*******************************
	                RespuestaValidacion resp3 = new RespuestaValidacion("No se pudo asociar al paciente con la institucion",false);
					try 
					{
						resp3 = UtilidadValidacion.validacionPermisosInstitucionPaciente2
						 (
						         con,
						         codigoPaciente,
						         usuarioActual.getCodigoInstitucion(),
						         usuarioActual.getCodigoInstitucion()
						 );
					}
					catch (SQLException e) 
					{
						logger.error("Error en el mÔøΩtodo validacionPermisosInstitucionPaciente2: "+e);
					}

	                 if(!resp3.puedoSeguir)
	                	 errores.add("Problemas ingresando el paciente", new ActionMessage("errors.noSeGraboInformacion","DE LA RELACION DEL PACIENTE CON LA INSTITUCION"));
	                 else
	                 {
	                 	//4) Si el paciente va para Triage se hace el insert************************************************************************************
	                 	if(ingresoPacienteForm.isEsPacienteTriage())
	                 	{
	                 		mundoPaciente.setDatosTriage("codigoPaciente",codigoPaciente+"");
	                 		mundoPaciente.setDatosTriage("usuario",usuarioActual.getLoginUsuario());
	                 		mundoPaciente.setDatosTriage("centroAtencion",usuarioActual.getCodigoCentroAtencion()+"");
	                 		mundoPaciente.setDatosTriage("clasificacion",ingresoPacienteForm.getClasificacionTriage()+"");
	                 		if(mundoPaciente.insertarPacienteTriage(con)<1)
	                 			errores.add("Problemas ingresando el paciente para Triage", new ActionMessage("errors.noSeGraboInformacion","DEL PACIENTE PARA TRIAGE"));
	                 	}
	                 	//**************************************************************************************************************************
	                 	
	                 	//5) Aplicacion del proceso de capitacion**************************************************************
	                 	if(ingresoPacienteForm.isEsPacienteCapitado())
	                 	{
	                 		int rs0=0,rs1=0;
	                 		SubirPaciente subirPac = new SubirPaciente();
	                 		//5.1) Guardar registros usuario_x_convenio
	                 		for(int i=0;i<Integer.parseInt(ingresoPacienteForm.getDatosCapitacion("numRegistros").toString());i++)
	                 		{
	                 			subirPac.setFechaInicial(UtilidadFecha.conversionFormatoFechaABD(ingresoPacienteForm.getDatosCapitacion("fecha_inicial_"+i).toString()));
		                 		subirPac.setFechaFinal(UtilidadFecha.conversionFormatoFechaABD(ingresoPacienteForm.getDatosCapitacion("fecha_final_"+i).toString()));
		                 		subirPac.setTipoCargue(Integer.parseInt(ingresoPacienteForm.getDatosCapitacion("tipo_cargue_"+i).toString()));
		                 		subirPac.setUsuario(usuarioActual.getLoginUsuario());
		                 		//rs0=subirPac.ingresarUsuarioXConvenio(con,Integer.parseInt(ingresoPacienteForm.getDatosCapitacion("codigo_contrato_"+i).toString()),codigoPaciente,Utilidades.convertirAEntero(ingresoPacienteForm.getDatosCapitacion("clasificacion_"+i)+""),ingresoPacienteForm.getDatosCapitacion("tipoafiliado_"+i)+"");
		                 		rs0=subirPac.ingresarUsuarioXConvenio(con,cargarDtoDatosCapitacion(ingresoPacienteForm.getDatosCapitacion(),i,codigoPaciente,usuarioActual));
		                 		
		                 		if(rs0<=0)
		                 			i = Integer.parseInt(ingresoPacienteForm.getDatosCapitacion("numRegistros").toString());
	                 		}
	                 		
	                 		
	                 		//5.2) eliminar registro usuarios_capitados y conv_usuarios_capitados
	                 		HashMap campos = new HashMap();
	                 		campos.put("estado",ConstantesBD.continuarTransaccion);
	                 		campos.put("codigo",ingresoPacienteForm.getCodigoUsuarioCapitado());
	                 		rs1 = subirPac.eliminarUsuarioCapitado(con,campos);
	                 		
	                 		if(rs0<=0||rs1<=0)
	                 			errores.add("Problemas registrando el paciente como usuario capitado", new ActionMessage("errors.noSeGraboInformacion","DEL PACIENTE PARA CAPITACION"));
	                 	}
 	
	                 	//*******************************************************************************
	                 	//Inserta el log autorizacion en caso de que se hubiere pedido
	                 	if(ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)
	                 			&& !ingresoPacienteForm.getAutorizacionIngEvento().getCodigo().equals("") 
	                 				&& !ingresoPacienteForm.getAutorizacionIngEvento().isActivo() 
	                 					&& !ingresoPacienteForm.isIngresoDesdeReservaCita() 
                 							&& !ingresoPacienteForm.isIngresoDesdeReferencia())
	                 	{
	                 		if(UtilidadesManejoPaciente.insertarLogAutorizacionIngresoEvento(
	                 				con,
	                 				ingresoPacienteForm.getAutorizacionIngEvento().getCodigo()+"",
	                 				codigoPaciente+"",
	                 				ingresoPacienteForm.getNumeroIdentificacion()+"", 
	                 				ingresoPacienteForm.getCodigoTipoIdentificacion(),
	                 				usuarioActual,
	                 				3+""))
	                 			logger.info("\n\nINSERTO LOG DE AUTORIZACION INGRESO EVENTO");
	                 		else
	                 			logger.info("\n\n NO INSERTO LOG DE AUTORIZACION INGRESO EVENTO");	                 			
	                 	}
	                 	//*******************************************************************************
	                 }	                 
	             }
	             else
	            	 errores.add("Problemas ingresando el paciente", new ActionMessage("errors.noSeGraboInformacion","DEL PACIENTE"));
			}
			else
				errores.add("Error al validar el ingreso del paciente", new ActionMessage("errors.notEspecific",respuesta.textoRespuesta));
			
			//Se valida si hubo errores
			if(!errores.isEmpty())
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoHistoriaClinica, usuarioActual.getCodigoInstitucionInt(), consecutivoHC, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				UtilidadBD.abortarTransaccion(con);
				saveErrors(request, errores);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("ingresarPaciente");
				
			}
			else
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoHistoriaClinica, usuarioActual.getCodigoInstitucionInt(), consecutivoHC, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
				UtilidadBD.finalizarTransaccion(con);
				
				
				//SEGÔøΩN INDICADOR SE DECIDE A DONDE SE DEBE CONTINUAR EL FLUJO************************************************
				//1) Si el flujo es desde reserva de citas se retorna a la funcionalidad de reserva de citas
				if(ingresoPacienteForm.isIngresoDesdeReservaCita())
				{
					UtilidadBD.closeConnection(con);
					try 
					{
						response.sendRedirect("../reservaCita/cita.do?estado=reservarAutoIng");
					} catch (IOException e) 
					{
						logger.error("Error realizando redireccion a funcionalidad de reserva de citas: "+e);
					}
					return null;
				}
				//2) Si el flujo es desde la referencia se retorna el control a la funcionalidad de referencia
				if(ingresoPacienteForm.isIngresoDesdeReferencia())
				{
					UtilidadBD.closeConnection(con);
					try 
					{
						response.sendRedirect("../referencia/referencia.do?estado=empezar");
					} catch (IOException e) 
					{
						logger.error("Error realizando redireccion a funcionalidad de referencia: "+e);
					}
					return null;
				}
				
				//3) Si el paciente es para triage se muestra la pantalla de confirmaciÔøΩn del triage 
				if(ingresoPacienteForm.isEsPacienteTriage())
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("pacienteTriage");
				}
				
				//4) Si no se cumple ninguna validacion anterior se envia normalmente a la creaciÔøΩn de la cuenta
				ingresoPacienteForm.setEstado("decisionIngresoPacienteSistemaCuenta");
				return this.funcionalidadDecisionIngresoPacienteSistema(mapping, ingresoPacienteForm, request,  paciente, usuarioActual, con, false,false);
				//*********************************************************************************************************
			}
		}
		//**************SI EL PACIENTE YA EXISTE SE REALIZA LA MODIFICACION********************************************************
		else
		{
			mundoPaciente.setCodigoPersona(paciente.getCodigoPersona());
			RespuestaValidacion respuesta = new RespuestaValidacion("Error al validar la modificacion del paciente",false);
			try
			{
				respuesta = UtilidadValidacion.validacionModificarPaciente(con, System.getProperty("TIPOBD"),  mundoPaciente.getCodigoBarrio() );
			}
			catch(SQLException e)
			{
				logger.error("Error al validar la modificacion del paciente: "+e);
			}
			
			int resp = 0;
			String ls_mensaje = "";
			
			if(respuesta.puedoSeguir)
			{
				
				try
				{
					//Se carga el paciente con los datos antiguos
					Paciente pacienteAnterior = new Paciente();
					pacienteAnterior.cargarPaciente(con, mundoPaciente.getCodigoPersona());
					
					
					
					//Se modifica el paciente
					resp = mundoPaciente.modificarPaciente(con,usuarioActual,pacienteAnterior);					
					
					//*******************************************************************************
                 	//Inserta el log autorizacion en caso de que se hubiere pedido
                 	if(ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)
                 			&& !ingresoPacienteForm.getAutorizacionIngEvento().getCodigo().equals("") 
                 				&& !ingresoPacienteForm.getAutorizacionIngEvento().isActivo()
                 					&& !ingresoPacienteForm.isIngresoDesdeReservaCita() 
                 						&& !ingresoPacienteForm.isIngresoDesdeReferencia())
                 	{
                 		if(UtilidadesManejoPaciente.insertarLogAutorizacionIngresoEvento(
                 				con,
                 				ingresoPacienteForm.getAutorizacionIngEvento().getCodigo()+"",
                 				mundoPaciente.getCodigoPersona()+"",
                 				ingresoPacienteForm.getNumeroIdentificacion()+"", 
                 				ingresoPacienteForm.getCodigoTipoIdentificacion(),
                 				usuarioActual,
                 				3+""))
                 			logger.info("\n\nINSERTO LOG DE AUTORIZACION INGRESO EVENTO");
                 		else
                 			logger.info("\n\n NO INSERTO LOG DE AUTORIZACION INGRESO EVENTO");	                 			
                 	}
                 	//*******************************************************************************					
				}
				catch(Exception e)
				{
					resp = 0;
					logger.error("Error realizando la modificaciÔøΩn del paciente: "+e);
				}
				
				
				switch(resp)
				{
					case -1:
						ls_mensaje =
							"El documento de identificaciÔøΩn " + mundoPaciente.getCodigoTipoIdentificacion() +
							"-" + mundoPaciente.getNumeroIdentificacion() + " ya esta registrado en el sistema. No se puede modificar el paciente";
						break;
					case -2:
						ls_mensaje =
							"La fecha de nacimiento debe ser anterior a la fecha actual. No se puede modificar el paciente";
						break;
					case -3:
						ls_mensaje =
							"Error al actualizar la historia clÔøΩnica previa. No se puede modificar el paciente";
						break;
				}

				
				UtilidadSesion.notificarCambiosObserver(mundoPaciente.getCodigoPersona(),getServlet().getServletContext());
				
				if(resp>0)
				{
					//******Se registra el paciente para triage si fue chequeado triage si fue chequeado**************+
					if(ingresoPacienteForm.isEsPacienteTriage())
					{
						mundoPaciente.setDatosTriage("codigoPaciente",mundoPaciente.getCodigoPersona());
	            		mundoPaciente.setDatosTriage("usuario",usuarioActual.getLoginUsuario());
	            		mundoPaciente.setDatosTriage("centroAtencion",usuarioActual.getCodigoCentroAtencion());
                 		mundoPaciente.setDatosTriage("clasificacion",ingresoPacienteForm.getClasificacionTriage()+"");
	            		resp = mundoPaciente.insertarPacienteTriage(con);
	            		if(resp<1)
	            			ls_mensaje = "No se pudo registrar al paciente en Triage. Error procesando los datos";
	            		
					}
					//*********************************************************
				}
				
				//Se realiza la validacion del proceso de modificacion
				if(resp<=0)
					errores.add("Error al realizar la modificaciÔøΩn del paciente",new ActionMessage("errors.notEspecific",ls_mensaje));
				
			}
			else
				errores.add("Error al validar el la modificacion del paciente", new ActionMessage("errors.notEspecific",respuesta.textoRespuesta));
			
			
			//Se valida si hubo errores
			if(!errores.isEmpty())
			{
				UtilidadBD.abortarTransaccion(con);
				saveErrors(request, errores);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("ingresarPaciente");
				
			}
			else
			{
				//Si el paciente es para triage se muestra la pantalla de confirmaciÔøΩn del triage 
				if(ingresoPacienteForm.isEsPacienteTriage())
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("pacienteTriage");
				}
				
				//Si no se cumple ninguna validacion anterior se envia normalmente a la creaciÔøΩn de la cuenta
				ingresoPacienteForm.setEstado("decisionIngresoPacienteSistemaCuenta");
				return this.funcionalidadDecisionIngresoPacienteSistema(mapping, ingresoPacienteForm, request,  paciente, usuarioActual, con, false,false);
			}
		}
		
	}




	/**
	 * 
	 * @param datosCapitacion
	 * @param indice
	 * @param usuarioActual 
	 * @return
	 */
	private DtoUsuariosXConvenio cargarDtoDatosCapitacion(HashMap datosCapitacion,int indice,int codigoPaciente, UsuarioBasico usuarioActual)
	{
		DtoUsuariosXConvenio dto=new DtoUsuariosXConvenio();
		dto.setContrato(Utilidades.convertirAEntero(datosCapitacion.get("codigo_contrato_"+indice).toString()));
		dto.setCodigoPersona(codigoPaciente);
		dto.setClasificacionSE(Utilidades.convertirAEntero(datosCapitacion.get("clasificacion_"+indice)+""));
		dto.setTipoAfiliado(datosCapitacion.get("tipoafiliado_"+indice)+"");
		dto.setFechaInicial(UtilidadFecha.conversionFormatoFechaABD(datosCapitacion.get("fecha_inicial_"+indice).toString()));
		dto.setFechaFinal(UtilidadFecha.conversionFormatoFechaABD(datosCapitacion.get("fecha_final_"+indice).toString()));
		dto.setTipoCargue(datosCapitacion.get("tipo_cargue_"+indice)+"");
		dto.setUsuario(usuarioActual.getLoginUsuario());
		dto.setActivo(ConstantesBD.acronimoSi);
		dto.setNaturalezaPaciente(Utilidades.convertirAEntero(datosCapitacion.get("naturaleza_"+indice)+""));
		dto.setCentroAtencion(Utilidades.convertirAEntero(datosCapitacion.get("centroatencion_"+indice)+""));
		dto.setTipoIdEmpleador(datosCapitacion.get("tipoidempleador_"+indice)+"");
		dto.setNumeroIdEmpleador(datosCapitacion.get("numeroidempleador_"+indice)+"");
		dto.setRazonSocialEmpleador(datosCapitacion.get("razonsociempleador_"+indice)+"");
		dto.setTipoIdCotizante(datosCapitacion.get("tipoidcotizante_"+indice)+"");
		dto.setNumeroIdCotizante(datosCapitacion.get("numeroidcotizante_"+indice)+"");
		dto.setNombresCotizante(datosCapitacion.get("nombrescotizante_"+indice)+"");
		dto.setApellidosCotizante(datosCapitacion.get("apellidoscotizante_"+indice)+"");
		dto.setParentesco(Utilidades.convertirAEntero(datosCapitacion.get("parentesco_"+indice)+""));
		dto.setNumeroFicha(datosCapitacion.get("numeroficha_"+indice)+"");
		return dto;
	}
	



	/**
	 * MÔøΩtodo implementado para realizar el filtro de ciudades segun la eleccion del pais
	 * @param con
	 * @param ingresoPacienteForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroCiudades(Connection con, IngresoPacienteForm ingresoPacienteForm, HttpServletResponse response) 
	{
		String resultado = "<respuesta>";
		String codigoPais = "";
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		
		//Se filtran las ciudades segun estado
		if(ingresoPacienteForm.getEstado().equals("filtroCiudadesId"))
		{
			codigoPais = ingresoPacienteForm.getCodigoPaisId();
			ingresoPacienteForm.setCiudadesExp(Utilidades.obtenerCiudadesXPais(con, codigoPais));
			arregloAux = ingresoPacienteForm.getCiudadesExp();
		}
		else if(ingresoPacienteForm.getEstado().equals("filtroCiudadesNacimiento"))
		{
			codigoPais = ingresoPacienteForm.getCodigoPaisNacimiento();
			ingresoPacienteForm.setCiudadesNac(Utilidades.obtenerCiudadesXPais(con, codigoPais));
			arregloAux = ingresoPacienteForm.getCiudadesNac();
		}
		else if(ingresoPacienteForm.getEstado().equals("filtroCiudadesResidencia"))
		{
			codigoPais = ingresoPacienteForm.getCodigoPaisResidencia();
			ingresoPacienteForm.setCiudades(Utilidades.obtenerCiudadesXPais(con, codigoPais));
			arregloAux = ingresoPacienteForm.getCiudades();
		}
		
		logger.info("NÔøΩMERO DE ELEMENTO ARREGLO CIUDADES=> "+arregloAux.size());
		logger.info("CODIGO PAIS=>*"+codigoPais+"*");
		
		//Revision de las ciudades segun pais seleccionado
		for(int i=0;i<arregloAux.size();i++)
		{
			HashMap elemento = (HashMap)arregloAux.get(i);
			if(elemento.get("codigoPais").toString().equals(codigoPais))
				resultado += "<ciudad>" +
					"<codigo-departamento>"+elemento.get("codigoDepartamento")+"</codigo-departamento>"+
					"<codigo-ciudad>"+elemento.get("codigoCiudad")+"</codigo-ciudad>"+
					"<nombre-departamento>"+elemento.get("nombreDepartamento")+"</nombre-departamento>"+
					"<nombre-ciudad>"+elemento.get("nombreCiudad")+"</nombre-ciudad>"+
				 "</ciudad>";
		}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			
			
			response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroCiudades: "+e);
		}
		return null;
	}


	/**
	 * MÔøΩtodo que redirecciona la recarga del ingreso del paciente cuando pasa por las diferentes validaciones como
	 * 1. Usuario que va a ser ingresado como paciente.
	 * 2. Paciente que tiene los mismos nombres de un paciente ya existente
	 * 3. Paciente que tiene mismo numero de identificacion de otro paciente en el sistema
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mapping
	 * @param response 
	 * @return
	 */
	private ActionForward accionRevisionIngresoPaciente(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionMapping mapping, HttpServletResponse response) 
	{
		//Se verifica decisiÔøΩn tomada sobre la validacion de paciente con mismo numero de identificacion
		if(ingresoPacienteForm.isAvisoValidacionNumerosId())
		{
			//Se decidiÔøΩ continuar
			UtilidadBD.closeConnection(con);
			return mapping.findForward("ingresarPaciente");
		}
		else
		{
			UtilidadBD.closeConnection(con);
			logger.info("INGRESO DESDE RESERVA DE CITAS? "+ingresoPacienteForm.isIngresoDesdeReservaCita());
			//SEGÔøΩN INDICADOR SE DECIDE A DONDE SE DEBE CONTINUAR EL FLUJO************************************************
			//1) Si el flujo es desde reserva de citas se retorna a la funcionalidad de reserva de citas
			if(ingresoPacienteForm.isIngresoDesdeReservaCita())
			{
				try 
				{
					response.sendRedirect("../reservaCita/cita.do?estado=regresarReserva");
				} catch (IOException e) 
				{
					logger.error("Error realizando redireccion a funcionalidad de reserva de citas: "+e);
				}
				return null;
			}
			//2) Si el flujo es desde la referencia se retorna el control a la funcionalidad de referencia
			if(ingresoPacienteForm.isIngresoDesdeReferencia())
			{
				try 
				{
					response.sendRedirect("../referencia/referencia.do?estado=empezar");
				} catch (IOException e) 
				{
					logger.error("Error realizando redireccion a funcionalidad de referencia: "+e);
				}
				return null;
			}
			
			//3) Por defecto se envia al inicio de la funcionalidad de ingreso del paciente
			try 
			{
				logger.info("PASO POR AQUI REDIRECCION AL INICIO DE LA FUNCIONALIDAD!!!");
				response.sendRedirect("../ingresarPaciente/ingresoPacienteSistema.jsp");
			} catch (IOException e) 
			{
				logger.error("Error realizando redireccion al inicio de la funcionalidad de ingreso paciente: "+e);
			}
			return null;
		}
		
		
	
		
	}

	
	

	

	






	/**
	 * Este mÔøΩtodo se encarga de ejecutar toda la funcionalidad que antes
	 * realizaba decisionIngresoPacienteSistema.jsp. Se encarga dada la
	 * identificaciÔøΩn de un paciente (Numero y tipo IdentificaciÔøΩn) de
	 * decidir si es necesario ingresarlo por primera vez (no existe en el
	 * sistema) o simplemente crearle cuenta (no tenia previamente, tenia
	 * pero ya cumplieron su ciclo de vida o asocio de cuenta)
	 *
	 * @param mapping Mapping para acceder a los distintos puntos de
	 * salida (pÔøΩginas o do's) de esta funcionalidad, definidos en el struts-
	 * config.xml
	 * @param ingresoPacienteForm Forma de tipo IngresoPacienteForm con
	 * los datos llenados sobre el paciente
	 * @param request Objeto que da acceso a todos los parametros en scope
	 * Request
	 * @param paciente Objeto PersonaBasica con la informaciÔøΩn del paciente
	 * con quien se estÔøΩ trabajando
	 * @param usuarioActual Objeto UsuarioBasico con la informaciÔøΩn del
	 * usuario con quien se estÔøΩ trabajando
	 * @param con ConexiÔøΩn con la fuente de datos
	 * @param entroSeleccionConvenios 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes"})
	public ActionForward funcionalidadDecisionIngresoPacienteSistema(
								ActionMapping mapping,
								IngresoPacienteForm ingresoPacienteForm,
								HttpServletRequest request,
								PersonaBasica paciente,
								UsuarioBasico usuarioActual,
								Connection con, 
								boolean entroSeleccionConvenios,
								boolean entroAutorizacionIngEven) throws IPSException
	{

		
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getProduccionEnParaleloConSistemaAnterior(usuarioActual.getCodigoInstitucionInt())))
		{
			ResultadoBoolean resultado;
			UtilidadBDInterfaz interfaz = new UtilidadBDInterfaz();
			resultado = interfaz.verificarPacienteSistemaParalelo(ingresoPacienteForm.getTipoIdentificacion().split("-")[0], ingresoPacienteForm.getNumeroIdentificacion(), usuarioActual.getCodigoInstitucion());
			if(resultado.isTrue())
			{	
				UtilidadBD.closeConnection(con);
				request.setAttribute("descripcionError", resultado.getDescripcion());
				return mapping.findForward("paginaError");
			}
		}
		
		//Solo aplica cuando no se ha pasado por la pagina de seleccion de convenios
		if(!entroSeleccionConvenios && !entroAutorizacionIngEven)
		{
			//***********************************************************************************************************
			//*********SE TOMAN LOS PARÔøΩMETROS QUE VERIFICAN SI VIENE DE RESERVA DE CITAS Y REFERENCIA*********************
			boolean reservaCita = UtilidadTexto.getBoolean(request.getParameter("ingresoDesdeReservaCita"));
			boolean referencia = UtilidadTexto.getBoolean(request.getParameter("ingresoDesdeReferencia"));
			String estado = ingresoPacienteForm.getEstado();
			ingresoPacienteForm.reset(request);
			ingresoPacienteForm.setIb_ingresoDesdeReservaCita(reservaCita);
			ingresoPacienteForm.setIngresoDesdeReferencia(referencia);
			ingresoPacienteForm.setEstado(estado);
			//***********************************************************************************************************
			//**********************************************************************************************************
			
			if (ingresoPacienteForm.getPreingreso()==null)
				ingresoPacienteForm.setPreingreso("false");
		}
		
		///**********************************************************************************************************
		//**********PARÔøΩMETRO USADO PARA VALIDAR LA ASIGNACION DE CAMA DE URGENCIAS DEL ASOCIO**********************
		//Se revisa que durante el ingreso no se haya asignado una Cama
		if(request.getSession().getAttribute("camaDesdeIngreso")==null)
		{
			//ingresoPacienteForm.resetMapa();
		}
		else
		{
			if(ingresoPacienteForm.getTipoIdentificacion().equals("")&&ingresoPacienteForm.getNumeroIdentificacion().equals(""))
			{
				ingresoPacienteForm.setTipoIdentificacion(paciente.getCodigoTipoIdentificacionPersona()+"-"+paciente.getTipoIdentificacionPersona(false));
				ingresoPacienteForm.setNumeroIdentificacion(paciente.getNumeroIdentificacionPersona());
			}
			request.getSession().removeAttribute("camaDesdeIngreso");
		}
		//*********************************************************************************************************
		//***********************************************************************************************************
		
		//Se remueven atributos de alerta
		if(request.getSession().getAttribute("avisoAsocioAutomatico")!=null)
			request.getSession().removeAttribute("avisoAsocioAutomatico");
		
		
		//Arreglo de String usado en varias partes para separar codigo-nombre
		String resultados[];
		boolean esConsecutivo = false;
		
		
		if(!ingresoPacienteForm.getTipoIdentificacion().equals(""))
		{
			resultados=ingresoPacienteForm.getTipoIdentificacion().split("-");
			ingresoPacienteForm.setCodigoTipoIdentificacion(resultados[0]);
			ingresoPacienteForm.setNombreTipoIdentificacion(resultados[1]);
			if(resultados.length>=3)
				esConsecutivo = UtilidadTexto.getBoolean(resultados[2]); //para verificar si el tipo de identificacion es con consecutivo
		}
		
		
		//Caso cubierto previamente por decisionIngresoPacienteSistema.jsp
		//Primero validamos que los datos esperados hayan llegado, si no
		//mostramos el error
		if (ingresoPacienteForm.getTipoIdentificacion()==null||ingresoPacienteForm.getNumeroIdentificacion()==null||ingresoPacienteForm.getTipoIdentificacion().equals("")||(ingresoPacienteForm.getNumeroIdentificacion().equals("")&&!esConsecutivo))
		{
			logger.debug("En el estado decisionIngresoPacienteSistema venÔøΩan ciertos datos en nulo que no deberÔøΩan estarlo");
			UtilidadBD.closeConnection(con);
			request.setAttribute("descripcionError", "La pagina a la que intentÔøΩ acceder no recibio los datos requeridos, por favor utilize el boton submit y/o utilice los menÔøΩes");
			return mapping.findForward("paginaError");
		}
				
		
		
		//***************SE VERIFICA SI EL PACIENTE EXISTE ********************************************************************************************
		try 
		{			
			ingresoPacienteForm.setExistePaciente(UtilidadValidacion.existePaciente(con, ingresoPacienteForm.getCodigoTipoIdentificacion(), ingresoPacienteForm.getNumeroIdentificacion()));		
			
		} catch (SQLException e) 
		{
			logger.error("Error al verificar si el paciente existe: "+e);
			ingresoPacienteForm.setExistePaciente(false);
		}

		//***************************************************************************************************************************
		/*SE VERIFICA SI EL PACIENTE TIENE UN TRIAGE CON UNA ADMISION PENDIENTE DE URGENCIAS, 
		 * EN  CASO DE QUE EXISTA SE LLENAN LOS ATRIBUTOS DEL FORM*/
		String consecutivosTriage[]={"", ""};
		consecutivosTriage=UtilidadValidacion.getConsecutivosTriage(con, ingresoPacienteForm.getCodigoTipoIdentificacion(), ingresoPacienteForm.getNumeroIdentificacion(), usuarioActual.getCodigoCentroAtencion()+"", usuarioActual.getCodigoInstitucionInt());
		if(!consecutivosTriage[0].trim().equals(""))
			ingresoPacienteForm.setConsecutivoTriage(consecutivosTriage[0]);
		if(!consecutivosTriage[1].trim().equals(""))
			ingresoPacienteForm.setConsecutivoFechaTriage(consecutivosTriage[1]);
		//Si hay triage pendiente de urgencias se verifica si se identificÔøΩ accidente de trabajo
		if(!ingresoPacienteForm.getConsecutivoTriage().equals("")&&!ingresoPacienteForm.getConsecutivoFechaTriage().equals(""))
		{
			HashMap datosAccidenteTrabajo = UtilidadesHistoriaClinica.consultaDatosAccidenteTrabajoTriage(con, ingresoPacienteForm.getConsecutivoTriage(), ingresoPacienteForm.getConsecutivoFechaTriage());
			if(Integer.parseInt(datosAccidenteTrabajo.get("numRegistros").toString())>0&&UtilidadTexto.getBoolean(datosAccidenteTrabajo.get("accidenteTrabajo").toString()))
			{
				ingresoPacienteForm.setCuenta("codigoTipoEvento",ConstantesIntegridadDominio.acronimoAccidenteTrabajo);
				ingresoPacienteForm.setCuenta("codigoArpAfiliado",datosAccidenteTrabajo.get("codigoConvenioArp").toString());
			}
		}
		logger.info("consecutivo triage: "+ingresoPacienteForm.getConsecutivoTriage());
		logger.info("consecutivo fecha triage: "+ingresoPacienteForm.getConsecutivoFechaTriage());
		//*******************************************************************************************************************
		
		
		//****************ÔøΩEXISTE PACIENTE?**********************************************************************************
		if(ingresoPacienteForm.isExistePaciente())
		{
			
			//Se carga el paciente en PersonaBasica
			TipoNumeroId identificacion = new TipoNumeroId(ingresoPacienteForm.getCodigoTipoIdentificacion(), ingresoPacienteForm.getNumeroIdentificacion() );
			
			try 
			{
				paciente.cargar(con, identificacion);
				paciente.cargarPaciente2(con, identificacion, usuarioActual.getCodigoInstitucion(),usuarioActual.getCodigoCentroAtencion()+"");
			} 
			catch (SQLException e) 
			{
				logger.error("Error al tratar de cargar un paciente: "+e);
			}
			
			//*******************SE VERIFICA SI EL PACIENTE ESTÔøΩ MUERTO********************************************************
			if(UtilidadValidacion.esPacienteMuerto(con,paciente.getCodigoPersona()))
			{
				ActionErrors errores = new ActionErrors();
				String datoPaciente = paciente.getCodigoTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona()+" "+paciente.getNombrePersona(false);
				errores.add("PACIENTE MUERTO",new ActionMessage("errors.paciente.estaMuerto",datoPaciente));
				saveErrors(request,errores);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaErroresActionErrors");
			}
			//*****************************************************************************************************************
			

			///**********SE VERIFICA SI LA PERSONA ES UN PACIENTE PARA TRIAGE***************************************************************************
			ConsultaPacientesTriage consultaPaciente = new ConsultaPacientesTriage();
			HashMap datosPaciente = consultaPaciente.consultarDatosPacienteTriage(con,paciente.getCodigoPersona(),usuarioActual.getCodigoCentroAtencion());
			if(Integer.parseInt(datosPaciente.get("numRegistros").toString())>0)
				ingresoPacienteForm.setConsecutivoTriage("NoAplica");
			//*********************************************************************************************************************************************
				
			
			//*******************SE REALIZA VALIDACIÔøΩN DEL INGRESO DEL PACIENTE*****************************************************************************
			//Se verifica si al paciente se le puede crear un nuevo ingreso
			RespuestaValidacion respPrevia = UtilidadValidacion.validacionPreviaIngresoPaciente(con,ingresoPacienteForm.getCodigoTipoIdentificacion(),ingresoPacienteForm.getNumeroIdentificacion(),usuarioActual.getCodigoInstitucion());
			
			
			//***************EVALUACION DEL CASO DE ASOCIO DE CUENTAS******************************************************************
			ActionForward forwardAsocioCuentas = evaluacionAsocioCuentas(con,ingresoPacienteForm,paciente,request,usuarioActual,mapping,respPrevia);
			if(forwardAsocioCuentas!=null)
				return forwardAsocioCuentas;
			///************FIN EVALUACIÔøΩN DEL CASO DE ASOCIO CUENTAS**************************************************
			
			//***********NO SE PUEDE CREAR UN NUEVO INGRESO******************************************
			if(!respPrevia.puedoSeguir)
			{
				UtilidadBD.closeConnection(con);
				request.setAttribute("descripcionError", "No tiene que volver a crear el Ingreso. " + respPrevia.textoRespuesta);
				return mapping.findForward("paginaError");
			}
			//***********ÔøΩSE PUEDE CREAR INGRESO!***************************************************************
			else
			{				
				//***************SE VERIFICA SI SE DEBE VALIDAR LA VIGENCIA EN LA COMPROBACIÔøΩN DE DERECHOS CAPITADOS *******************************				
				if(ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)													
						&& !entroSeleccionConvenios	
							&& !entroAutorizacionIngEven 
								&& ingresoPacienteForm.getEstado().equals("decisionIngresoPacienteSistema")
									&& !ingresoPacienteForm.isIngresoDesdeReservaCita() 
										&& !ingresoPacienteForm.isIngresoDesdeReferencia())
				{
					ingresoPacienteForm.setAutorizacionIngEvento(UtilidadValidacion.esPacienteCapitadoVigente(con,UtilidadFecha.getFechaActual(),ingresoPacienteForm.getNumeroIdentificacion(),ingresoPacienteForm.getTipoIdentificacion().split("-")[0].trim()));
					
					//Valida que exista el paciente dentro de la vigencia capitada
					if(!ingresoPacienteForm.getAutorizacionIngEvento().isActivo())
					{
						ingresoPacienteForm.getAutorizacionIngEvento().setCodigo("");
						return mapping.findForward("autoIngEvento");
					}
				}
				//************************************************************************************************************************************
				
				
				//*******************VALIDACION DEL INGRESO INCOMPLETO POR GARANTIAS**************************************
				ingresoPacienteForm.setIngresoIncompleto(IngresoGeneral.cargarIngresoIncompleto(con, paciente.getCodigoPersona()+""));
				if(Integer.parseInt(ingresoPacienteForm.getIngresoIncompleto("numRegistros").toString())>0)
				{
					ActionErrors errores = new ActionErrors();
					errores.add("Ingreso Incompleto",
						new ActionMessage(
							"errors.paciente.ingresoIncompleto",
							ingresoPacienteForm.getIngresoIncompleto("fechaIngreso")+" - "+ingresoPacienteForm.getIngresoIncompleto("horaIngreso"),
							ingresoPacienteForm.getIngresoIncompleto("nombreViaIngreso").toString()));
					saveErrors(request, errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("avisoIngresoIncompleto");
				}
				//*******************************************************************************************************
				
				/*
				En este caso hay que tener en cuenta que el paciente puede existir, pero puede no estar autorizado en esta
				institucion, luego le hacemos una pequeÔøΩa validacion La validaciÔøΩn internamente genera el permiso si es necesario
				 */
				try 
				{
					respPrevia=UtilidadValidacion.validacionPermisosInstitucionPaciente(con, ingresoPacienteForm.getCodigoTipoIdentificacion(), ingresoPacienteForm.getNumeroIdentificacion(), usuarioActual.getCodigoInstitucion() );
				} 
				catch (SQLException e) 
				{
					logger.error("Error al realizar las validaciones de permisos de institucion por paciente: "+e);
					respPrevia = new RespuestaValidacion("Error al realizar las validaciones de permisos de institucion por paciente",false);
				}
			
				/*********************************************************************************
				 * Modificacion por anexo 747
				 * se valida que si el paciente tuvo una cuenta anterior, esta debe tener
				 * un egreso total (medico y administrativo).
				 */
					try 
					{
						logger.info("\n codigo persona -->"+paciente.getCodigoPersona());
						if (paciente.getCodigoPersona()>0)
						{
							int cuenta = Utilidades.obtenerIdUltimaCuenta(con, paciente.getCodigoPersona());
							//Se Modifica el Anexo 747 - Tarea 86710
							//No verificar la boleta de salida si no existe algun registro en la tabla egreso por la cuenta.
							//InfoDatosInt estadoCuenta=UtilidadesHistoriaClinica.obtenerEstadoCuenta(con, cuenta);
							
						
								//verificar que la cuenta no este en estado cerra, debe de estar en estado facturada o facturada parcial
							//verificar que la cuenta no este en estado cerrada, debe de estar en estado facturada o facturada parcial
							if (cuenta>0) 
							{
								if (Utilidades.existeAlgunRegistroEgreso(con, cuenta))
								{
								
									String viaing = Utilidades.obtenerViaIngresoCuenta(con, cuenta+"");
									String tipoPac="";
									logger.info("\n\n ############### entre a verificar si la cuenta anterior tiene egreso. cuenta -->"+cuenta);
									logger.info("\n\n ############### Via Ingreso -->"+ viaing);
									
									ingresoPacienteForm.setTiposPaciente(UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, viaing));
									try {
									int tam= ingresoPacienteForm.getTiposPaciente().size();
									tipoPac= ingresoPacienteForm.getTiposPaciente().get(tam-1).get("codigoTipoPaciente").toString();
									}
									catch (Exception e)
									{
										// el try catch es por si pasa una cuenta vacia 
									}
									
								//79516
								if( (Utilidades.convertirAEntero(viaing) != ConstantesBD.codigoViaIngresoAmbulatorios) && (Integer.parseInt(viaing) != ConstantesBD.codigoViaIngresoConsultaExterna) && (!tipoPac.equals(ConstantesBD.tipoPacienteHospitalizado)))
									if(!UtilidadValidacion.existeEgresoCompleto(con,cuenta))
										return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no tiene Egreso ", "El paciente no tiene Egreso", false);
										//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no Boleta de Salida", "El paciente no tiene Boleta de Salida", false);
							}
							}
						}

					} catch (Exception e)
					{
						logger.warn("\n problema verificando si el paciente tiene egreso en su cuenta anterior "+e);
					}
							
				
				/*
				 * 
				 **************************************************************************************/
				
				
				
				if(ingresoPacienteForm.getEstado().equals("decisionIngresoPacienteSistema"))
				{
					//********SE CONSULTA EL SALDO DE LA INTERFAZ*******************************************************************
					//modificacion por anexo 779
					ResultadoDouble resultadoDouble=this.consultaSaldoInterfaz(paciente.getNumeroIdentificacionPersona(), usuarioActual, ingresoPacienteForm);
					if (resultadoDouble!=null && UtilidadCadena.noEsVacio(resultadoDouble.getDescripcion()))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, resultadoDouble.getDescripcion(), resultadoDouble.getDescripcion(), false);
					}
					//**************************************************************************************************************
					
					//************VOY A MODIFICAR EL PACIENTE**********************
					//se prepara la informacion del paciente
					this.prepararInformacionPaciente(con, ingresoPacienteForm, usuarioActual);
					//Se carga la informaciÔøΩn del paciente
					this.llenarFormaPaciente(con,ingresoPacienteForm,paciente,usuarioActual);
					
					//Validacion del numero de historia clÔøΩnica (puede que no estÔøΩ)
					//******OBTENCION DEL ULTIMO NUMERO DE HISTORIA CLINICA*********************************
					ActionErrors errores = new ActionErrors();
					if(ingresoPacienteForm.getNumeroHistoriaClinica()==null||ingresoPacienteForm.getNumeroHistoriaClinica().equals(""))
						errores = validacionConsecutivoHistoriaClinica(con,usuarioActual,errores);
					//***************************************************************************************
					
					
					if(!errores.isEmpty())
					{
						UtilidadBD.closeConnection(con);
						saveErrors(request,errores);
						return mapping.findForward("paginaErroresActionErrors");
					}
					
					
					//ValidaciÔøΩn de paciente con preingreso ****************************************************
					ingresoPacienteForm.setCodIngresoPreingreso(UtilidadesManejoPaciente.tienePreingresoPendienteXCentroAtencion(con, usuarioActual.getCodigoInstitucionInt(), usuarioActual.getCodigoCentroAtencion(), paciente.getCodigoPersona()));
					ingresoPacienteForm.setPreingreso("false");
					
					if (ingresoPacienteForm.getCodIngresoPreingreso()!=ConstantesBD.codigoNuncaValido){
						//validar si el paciente tiene otros ingresos pendientes por facturar
						String ingresosCerradosPendientesXFacturar = UtilidadesManejoPaciente.ingresosCerradosPendientesXFacturar(con, paciente.getCodigoPersona());
						if (!ingresosCerradosPendientesXFacturar.equals("")){
							ingresoPacienteForm.setIngresosCerradosPendientesXFacturar(ingresosCerradosPendientesXFacturar);
							if(ingresoPacienteForm.getCodIngresoPreingreso()!=ConstantesBD.codigoNuncaValido){
								if(UtilidadesManejoPaciente.tieneIngresoManual(con, ingresoPacienteForm.getCodIngresoPreingreso())){
									ingresoPacienteForm.setPreingreso("alerta");
								}	
							}
							//*******************************************************************************************
							
							return mapping.findForward("alertasIngresarCedula");
						} 
					} 
					else
					{
						ingresoPacienteForm.setCodIngresoPreingreso(UtilidadesManejoPaciente.tienePreingresoPendiente(con, usuarioActual.getCodigoInstitucionInt(), paciente.getCodigoPersona()));
						logger.info("--------------------- "+ingresoPacienteForm.getCodIngresoPreingreso());
						if (ingresoPacienteForm.getCodIngresoPreingreso()!=ConstantesBD.codigoNuncaValido){
							ingresoPacienteForm.setPreingreso("nuevoIngreso");
						}
					}
					//******************************************************************************************
					
					
					//**************VALIDACIÔøΩN DE LOS INGRESOS CERRADOS DEL PACIENTE PENDIENTES POR FACTURAR****************
					ArrayList<HashMap<String, Object>> ingresosPendientes = CierreIngreso.obtenerIngresosCerradosPendientesXPaciente(con, paciente.getCodigoPersona());
					if(ingresosPendientes.size()>0)
					{
						ElementoApResource alerta = new ElementoApResource("errors.notEspecific");
						String contenido = "El paciente tiene ingresos cerrados con cuentas pendientes por facturar: ";
						for(HashMap<String, Object> elemento:ingresosPendientes)
							contenido += "<b>"+elemento.get("consecutivo") + (elemento.get("anioConsecutivo").toString().equals("")?"":"-"+elemento.get("anioConsecutivo")) + "</b>. ";
						alerta.agregarAtributo(contenido);
						ingresoPacienteForm.getMensajesAlerta().add(alerta);
					}
					//*******************************************************************************************************
					
					
					
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingresarPaciente");
					//***************************************************************
				}
				else
				{
					//************VOY A LA CREACIÔøΩN DE LA CUENTA********************************************
					
					//**********SE REALIZA LA VALIDACION DEL CONSECUTIVO DE INGRESO********************************
					 ActionErrors errores = new ActionErrors();
					 errores = validacionConsecutivoIngreso(con, usuarioActual, errores);
					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaErroresActionErrors");
					}
					//*************************************************************************************
					
					//***********SE HACE VERIFICACIÔøΩN DE LOS CONVENIOS DEL PACIENTE***************************
					//si no ha entrado se llama la verificacion
					if(!entroSeleccionConvenios)
					{
						ActionForward redireccion = this.verificacionConveniosPaciente(con,ingresoPacienteForm,paciente,mapping,usuarioActual);
						//Si no es NULL quiere decir que se deben seleccionar los convenios histÔøΩricos
						if(redireccion!=null)
							return redireccion;
					}
					//******************************************************************************************
					
					//***********PREPARAR INFORMACION PAGINA CUENTA*******************************************
					this.prepararInformacionCuenta(con,ingresoPacienteForm,usuarioActual,paciente);
					//*****************************************************************************************
					
					//como se ingresa por segunda vez ya se continua con la creaciÔøΩn de la cuenta
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingresarCuenta");
					//*******************************************************************************
				}
			}
			//***************************************************************************************************
			
			
		}
		//*************EL PACIENTE NO EXISTE**********************************************************************
		else
		{			
			//***************SE VERIFICA SI SE DEBE VALIDAR LA VIGENCIA EN LA COMPROBACIÔøΩN DE DERECHOS CAPITADOS *******************************				
			if(ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)													
					&& !entroSeleccionConvenios	
						&& !entroAutorizacionIngEven 
							&& ingresoPacienteForm.getEstado().equals("decisionIngresoPacienteSistema")
								&& !ingresoPacienteForm.isIngresoDesdeReservaCita() 
									&& !ingresoPacienteForm.isIngresoDesdeReferencia())
			{
				ingresoPacienteForm.setAutorizacionIngEvento(UtilidadValidacion.esPacienteCapitadoVigente(con,UtilidadFecha.getFechaActual(),ingresoPacienteForm.getNumeroIdentificacion(),ingresoPacienteForm.getTipoIdentificacion().split("-")[0].trim()));
				
				//Valida que exista el paciente dentro de la vigencia capitada
				if(!ingresoPacienteForm.getAutorizacionIngEvento().getActivo())
				{
					ingresoPacienteForm.getAutorizacionIngEvento().setCodigo("");
					return mapping.findForward("autoIngEvento");
				}
			}
			//************************************************************************************************************************************
						
			ActionErrors errores = new ActionErrors();
			
			
			//*****************************VALIDACION DE TIPO DE IDENTIFICACION AUTOMATICA****************************************
			//Si el paciente no existe se verifica si tiene tipo id automÔøΩtico para asignarle el numero de la secuencia
			if(esConsecutivo)
			{
				try 
				{
					ingresoPacienteForm.setNumeroIdentificacion((DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, "seq_personas_sin_id")+1)+"");
					//Se tiene que volver a verificar que no exista un paciente con el mismo tipo y numero de identificacion
					ingresoPacienteForm.setExistePaciente(UtilidadValidacion.existePaciente(con, ingresoPacienteForm.getCodigoTipoIdentificacion(), ingresoPacienteForm.getNumeroIdentificacion()));
					
					//Si ya existe un paciente con ese consecutivo, se debe continuar con la siguiente secuencia, hasta que haya una disponible
					while(ingresoPacienteForm.isExistePaciente())
					{
						DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).incrementarValorSecuencia(con, "seq_personas_sin_id");
						ingresoPacienteForm.setNumeroIdentificacion((DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, "seq_personas_sin_id")+1)+"");
						ingresoPacienteForm.setExistePaciente(UtilidadValidacion.existePaciente(con, ingresoPacienteForm.getCodigoTipoIdentificacion(), ingresoPacienteForm.getNumeroIdentificacion()));
					}
					
				} 
				catch (SQLException e) 
				{
					logger.error("Error al obtener AL REALIZAR LA VALIDACION DEL NUMERO DE IDENTIFICACION AUTOMATICA: "+e);
				}
				
				
				
				//**************SE AGREGA MENSAJE DE ADVERTENCIA PARA MOSTRAR EN LA PAGINA***************************************
				ingresoPacienteForm.setIdentificacionAutomatica(true);
				ElementoApResource advertencia = new ElementoApResource("errors.notEspecific");
				advertencia.agregarAtributo("Paciente sin identificaciÔøΩn, se le asignÔøΩ la siguiente: "+ingresoPacienteForm.getNombreTipoIdentificacion()+" NÔøΩ "+ingresoPacienteForm.getNumeroIdentificacion());
				ingresoPacienteForm.getMensajesAlerta().add(advertencia);
				//***********************************************************************************************************
			}
			else
				ingresoPacienteForm.setIdentificacionAutomatica(false);
			//****************************************************************************************************************
			
			
			//se asigna basuara al consecutivo de triage para deshabilitar la opcion "Paciente Triage"
			if(ingresoPacienteForm.isIngresoDesdeReservaCita()||ingresoPacienteForm.isIngresoDesdeReferencia())
				ingresoPacienteForm.setConsecutivoTriage("NoAplica");
			
			
			
			
			//******OBTENCION DEL ULTIMO NUMERO DE HISTORIA CLINICA*********************************
			errores = validacionConsecutivoHistoriaClinica(con,usuarioActual,errores);
			
			String consecutivoHC = UtilidadBD.obtenerValorConsecutivoDisponible(
					ConstantesBD.nombreConsecutivoHistoriaClinica, usuarioActual.getCodigoInstitucionInt());
			
			//Se realiza la validacion pero no se bloquea el flujo de creaciÛn del paciente 
			if(!errores.isEmpty() && UtilidadTexto.isEmpty(consecutivoHC)) {
				Log4JManager.error("ERROR OBTENIENDO EL CONSECUTIVO DE HISTORIA CLINICA");
			}
			
			ingresoPacienteForm.setAnioHistoriaClincia(
					UtilidadBD.obtenerAnioActualTablaConsecutivos(
							con, ConstantesBD.nombreConsecutivoHistoriaClinica, usuarioActual.getCodigoInstitucionInt()));
			ingresoPacienteForm.setNumeroHistoriaClinica(consecutivoHC);
			
			//***************************************************************************************
			
			//**********SE CARGA LA INFORMACION ADICIONAL QUE SE NECESITA PARA EL INGRESO/MODIFICACION DEL PACIENTE***************
			this.prepararInformacionPaciente(con,ingresoPacienteForm,usuarioActual);
			
			
			
			
			//***********************VERIFICACIÔøΩN DE USUARIO / PACIENTE******************************************
			//validaciÔøΩn de usuario/medico existente
			this.validacionUsuarioPaciente(con,ingresoPacienteForm);
			//***********************************************************
			
			//********SE CONSULTA EL SALDO DE LA INTERFAZ*******************************************************************
			//modificacion por anexo 779
			ResultadoDouble resultadoDouble=this.consultaSaldoInterfaz(ingresoPacienteForm.getNumeroIdentificacion(), usuarioActual, ingresoPacienteForm);
			if (resultadoDouble !=null && UtilidadCadena.noEsVacio(resultadoDouble.getDescripcion()))
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, resultadoDouble.getDescripcion(), resultadoDouble.getDescripcion(), false);
			}
			//**************************************************************************************************************
			
			//**************************************************************************************************************
			
			//********VALIDACION DE USUARIOS CAPITADOS*****************************************
			errores = this.validacionUsuariosCapitados(con,ingresoPacienteForm,errores,usuarioActual);
			//************************************************************************************
			
			//**********VALIDACION PACIENTE DE TRIAGE PARA URGENCIAS**************************
			this.validacionPacienteTriageUrgencias(con,ingresoPacienteForm);
			//*********************************************************************************

			
			if(!errores.isEmpty())
			{
				UtilidadBD.closeConnection(con);
				saveErrors(request,errores);
				return mapping.findForward("paginaErroresActionErrors");
			}
			
			//*********VALIDACION DE LOS NÔøΩMEROS DE IDENTIFICACION***********************************
			ingresoPacienteForm.setMapaNumerosId(Utilidades.personasConMismoNumeroId(
				con, 
				ingresoPacienteForm.getNumeroIdentificacion(),
				ingresoPacienteForm.isUsuarioPaciente()?ingresoPacienteForm.getCodigoTipoIdentificacion():""
				//Si es usuario el paciente que se debe ingresar se debe excluir de la validacion
				//de igual numero de identificacion enviando el codigo del tipo de identificacion
				)
			);
			if(Integer.parseInt(ingresoPacienteForm.getMapaNumerosId("numRegistros").toString())>0)
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("avisoNumerosId");
			}
			//****************************************************************************************
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("ingresarPaciente");
		}
		
	}

	
	/**
	 * MÔøΩtodo usado para cargar los datos bÔøΩsicos triage del paciente si tiene pendiente
	 * triage
	 * @param con
	 * @param ingresoPacienteForm
	 */
	private void validacionPacienteTriageUrgencias(Connection con,
			IngresoPacienteForm ingresoPacienteForm) 
	{
		//Primero se debe verificar que el triage sÔøΩ aplique y que se haya ingresadp
		if(!ingresoPacienteForm.getConsecutivoTriage().equals("")&&!ingresoPacienteForm.getConsecutivoTriage().equals("NoAplica"))
		{
			DtoTriage dtoTriage = Triage.obtenerDatosTriage(con, ingresoPacienteForm.getConsecutivoTriage(), ingresoPacienteForm.getConsecutivoFechaTriage());
			//Se verifica si sÔøΩ se encontrÔøΩ datos de triage
			if(!dtoTriage.getConsecutivoTriage().equals(""))
			{
				ingresoPacienteForm.setPrimerNombrePersona(dtoTriage.getPrimerNombre());
				ingresoPacienteForm.setSegundoNombrePersona(dtoTriage.getSegundoNombre());
				ingresoPacienteForm.setPrimerApellidoPersona(dtoTriage.getPrimerApellido());
				ingresoPacienteForm.setSegundoApellidoPersona(dtoTriage.getSegundoApellido());
				ingresoPacienteForm.setFechaNacimiento(dtoTriage.getFechaNacimiento());
			}
		}
		
	}



	/**
	 * MÔøΩtodo implementado para realizar la evaluaciÔøΩn del asocio de cuentas
	 * @param con
	 * @param ingresoPacienteForm
	 * @param paciente
	 * @param request
	 * @param usuarioActual
	 * @param respPrevia 
	 * @param mapping 
	 * @return
	 */
	private ActionForward evaluacionAsocioCuentas(Connection con, IngresoPacienteForm ingresoPacienteForm, PersonaBasica paciente, HttpServletRequest request, UsuarioBasico usuarioActual, ActionMapping mapping, RespuestaValidacion respPrevia) throws IPSException 
	{
		boolean puedoCrearAsocio = false;
		puedoCrearAsocio = UtilidadValidacion.puedoCrearCuentaAsocio(con, paciente.getCodigoIngreso());					
		
		
		
		
		//Verifica que la ultima via de ingreso sea Urgencias 
		if(paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoUrgencias)
		{			
			
			logger.info("\n\n");
			logger.info("\n\n");
			logger.info("---------------------------------------------------------------");
			logger.info("Asocio desde Urgencias a Hospitalizacion - Hospitalizado o Hospitalizacion - Cirugia Ambulatoria ");
			
			//SE VERIFICA CUANDO YA SE HIZO EL ASOCIO
			if ((paciente.getCodigoIngreso()!=0&&puedoCrearAsocio))
			{					
				try
				{
					//Se verifican los datos de la ÔøΩltima cuenta de Urgencias
					String idCuentaAsocio = paciente.getCodigoCuenta()+"";
					int codigoDestinoSalida = Utilidades.obtenerDestinoSalidaEgresoEvolucion(con,Integer.parseInt(idCuentaAsocio));
										
					//CASO A: ASOCIO DESDE CAMA DE OBSERVACIONES
					//Se verifica si en la cuenta entrÔøΩ como cama observaciÔøΩn y se le diÔøΩ como salida de egreso en la evoluciÔøΩn "Hospitalizacion" o Cirugia Ambulatoria
					if(UtilidadValidacion.estaEnCamaObservacion(con,Integer.parseInt(idCuentaAsocio))&&
						(codigoDestinoSalida==ConstantesBD.codigoDestinoSalidaHospitalizacion || 
							codigoDestinoSalida==ConstantesBD.codigoDestinoSalidaCirugiaAmbulatoria||
							codigoDestinoSalida==ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial))
					{
						return remitirAAdmisionHospitalaria(
								con,
								usuarioActual,
								request,
								mapping,
								ingresoPacienteForm,
								idCuentaAsocio,
								paciente,
								"evolucion",
								codigoDestinoSalida);
					}
					//CASO B: ASOCIO DESDE LA VALORACION
					//De lo contrario se hace otro tipo de verificaciÔøΩn
					else
					{						
						//Se carga la conducta de la valoracion de la cuenta antigua (Urgencias)
						int codigoConductaValoracion = UtilidadesHistoriaClinica.obtenerCodigoConductaValoracionUrgenciasCuenta(con, idCuentaAsocio);
						
						//Se revisa que la conducta a seguir de la valoraciÔøΩn
						//sea 'Hospitalizar en Piso' o Cirugia Ambulatoria o 'Traslado Cuidado Especial'
						if(codigoConductaValoracion==ConstantesBD.codigoConductaSeguirHospitalizarPiso 
								|| codigoConductaValoracion==ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria
								|| codigoConductaValoracion==ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial)
						{
							//definir responsable
							//definir ingreso
							//editar cuenta
							//forward a Admision Hospitalaria
							return remitirAAdmisionHospitalaria(
									con,
									usuarioActual,
									request,
									mapping,
									ingresoPacienteForm,
									idCuentaAsocio,
									paciente,
									"valoracion",
									codigoConductaValoracion);	
						}
					}
				}
				catch(Exception e)
				{
					logger.error("No era una cuenta de asocio con conducta a seguir de hopitalziacion "+e);
				}
				
			}
			//SE VERIFICA CUANDO AÔøΩN NO SE HA REALIZADO EL ASOCIO
			else if(!respPrevia.puedoSeguir)
			{
				try
				{
					//AquÔøΩ se revisa que la cuenta sea de Urgencias y de conducta 'Obersvacion de camas Urgencias' y se le haya
					//dicho en la evolucion que el destino de salida es Hospitalizacion o Cirugia Ambulatoria
					//****************************************************************************************************
					//CASO A: ASOCIO DESDE CAMA DE OBSERVACIONES
					if(UtilidadValidacion.estaEnCamaObservacion(con,paciente.getCodigoCuenta())&&
						paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias&&
						(Utilidades.obtenerDestinoSalidaEgresoEvolucion(con,paciente.getCodigoCuenta())==ConstantesBD.codigoDestinoSalidaHospitalizacion 
								|| Utilidades.obtenerDestinoSalidaEgresoEvolucion(con,paciente.getCodigoCuenta())==ConstantesBD.codigoDestinoSalidaCirugiaAmbulatoria
								|| Utilidades.obtenerDestinoSalidaEgresoEvolucion(con,paciente.getCodigoCuenta())==ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial))
					{
						
						String idCuentaAsocio = paciente.getCodigoCuenta()+"";
						
						//Realizar el Egreso AutomÔøΩtico
						if(!UtilidadValidacion.existeEgresoCompleto(con,paciente.getCodigoCuenta()))
						{
							//Se revisa que ya haya tenido una cama
							if(paciente.getCodigoCama()==0)
							{									
								AdmisionUrgencias admUrg=new AdmisionUrgencias();
								TipoNumeroId tipoId=new TipoNumeroId(paciente.getAnioAdmision()+"",paciente.getCodigoAdmision()+"");
								admUrg.cargar(con,tipoId);									
								
								//Se verifica si el usuario tiene permisos para ingresar a cama de observacion
								if(Utilidades.tieneRolFuncionalidad(con, usuarioActual.getLoginUsuario(), 31))
								{
									request.getSession().setAttribute("admision",admUrg);
									//indicador para cambiar la vista de datosCamaUrgencias
									request.getSession().setAttribute("camaDesdeIngreso","true");
									
									UtilidadBD.closeConnection(con);
									return mapping.findForward("asignarCama");
								}
								else
								{
									UtilidadBD.closeConnection(con);
									ActionErrors errores = new ActionErrors();
									errores.add("Usuario no tiene permiso para ingresar a asignacion cama observacion",new ActionMessage("errors.usuarioSinRolFuncionalidad",usuarioActual.getLoginUsuario()," AsignaciÔøΩn Cama ObservaciÔøΩn"));
									saveErrors(request, errores);
									return mapping.findForward("paginaErroresActionErrors");
								}
							}
							logger.info("No hay Egreso completo");
							boolean huboEgreso=hacerEgreso(con,request,paciente,usuarioActual);
							if(huboEgreso)
							{
								logger.info("Se generÔøΩ el egreso automÔøΩtico!!!");
								
								ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
								observable.addObserver(paciente);
								
								//Se genera un asocio automÔøΩtico
								boolean huboAsocio=hacerAsocio(con,paciente,usuarioActual);
								if(huboAsocio)
									//*******************************************************
									return remitirAAdmisionHospitalaria(
											con,
											usuarioActual,
											request,
											mapping,
											ingresoPacienteForm,
											idCuentaAsocio,
											paciente,
											"evolucion",
											Utilidades.obtenerDestinoSalidaEgresoEvolucion(con,paciente.getCodigoCuenta()));
								else
									logger.info("NOOO Se generÔøΩ el ASOCIO AUTO!");
								
							}
							else
								logger.info("NOOOOO Se generÔøΩ el egreso automÔøΩtico!!!");
						}
						//En el caso de que ya haya un Egreso
						else
						{
							//Se verifica si ya hubo un asocio
							if(!paciente.getExisteAsocio())
							{
								//Se genera un asocio automÔøΩtico
								boolean huboAsocio=hacerAsocio(con,paciente,usuarioActual);
								if(huboAsocio)
									return remitirAAdmisionHospitalaria(
											con,
											usuarioActual,
											request,
											mapping,
											ingresoPacienteForm,
											idCuentaAsocio,
											paciente,
											"evolucion",
											Utilidades.obtenerDestinoSalidaEgresoEvolucion(con,paciente.getCodigoCuenta()));
								else
									logger.info(" NOOO Se generÔøΩ el ASOCIO AUTO!");
							}
						}
						
					}
					//De lo contrario se hace otro tipo de verificaciÔøΩn
					//CASO B: ASOCIO DESDE LA VALORACION
					else
					{
						if(paciente.getCodigoCuenta()>0&&paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
						{
							//Se carga la conducta valoraciÔøΩn de la cuenta antigua (Urgencias)
							int codigoConductaValoracion = UtilidadesHistoriaClinica.obtenerCodigoConductaValoracionUrgenciasCuenta(con, paciente.getCodigoCuenta()+"");
							
							//Se revisa que la conducta a seguir de la valoraciÔøΩn
							//sea 'Hospitalizar en Piso'
							logger.info("Entonces la conducta a seguir es"+codigoConductaValoracion);
							if(codigoConductaValoracion==ConstantesBD.codigoConductaSeguirHospitalizarPiso || 
									codigoConductaValoracion==ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria||
									codigoConductaValoracion==ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial)
							{
								ActionErrors errores = new ActionErrors();
								errores.add("La cuenta no estÔøΩ asociada",new ActionMessage("error.asociocuenta.cuentaSinAsocio","Hospitalizar en Piso o Cirugia Ambulatoria "));
								saveErrors(request,errores);
								//*******************************************************
								UtilidadBD.closeConnection(con);
								return mapping.findForward("paginaErroresActionErrors");
							}
						}
					}
				
				}
				catch(Exception e)
				{
					logger.error("Error realizando validaciones del asocio: "+e);
				}					
			}
		}
		//***********************************************************************************************************************************
		//***********************************************************************************************************************************
		//***********************************************************************************************************************************
		//***********************************************************************************************************************************			
		//Verifica que la ultima via de ingreso sea HospitalizaciÔøΩn
		else if(paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion  && 
					(Utilidades.obtenerDestinoSalidaEgresoEvolucion(con,Integer.parseInt(paciente.getCodigoCuenta()+"")) == ConstantesBD.codigoDestinoSalidaHospitalizar||
					Utilidades.obtenerDestinoSalidaEgresoEvolucion(con,Integer.parseInt(paciente.getCodigoCuenta()+"")) == ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial))					
		{
			logger.info("\n\n");
			logger.info("\n\n");
			logger.info("---------------------------------------------------------------");
			logger.info("Asocio desde Hospitalizacion - Cirugia Ambulatoria a Hospitalizacion - Hospitalizado ");
			
			//SE VERIFICA CUANDO YA SE HIZO EL ASOCIO
			if ((paciente.getCodigoIngreso()!=0 && puedoCrearAsocio))
			{	
				logger.info("Indicador Puede Crear Asocio >> true");
				
				return remitirAAdmisionHospitalaria(
						con,
						usuarioActual,
						request,
						mapping,
						ingresoPacienteForm,
						paciente.getCodigoCuenta()+"",
						paciente,
						"evolucion",
						Utilidades.obtenerDestinoSalidaEgresoEvolucion(con,paciente.getCodigoCuenta()));
			}
			else
			{
				logger.info("Indicador Puede Crear Asocio >> false");
				
				try
				{					
					//Realizar el Egreso AutomÔøΩtico
					if(!UtilidadValidacion.existeEgresoCompleto(con,paciente.getCodigoCuenta()))
					{							
						logger.info("No hay Egreso completo");
						boolean huboEgreso=hacerEgreso(con,request,paciente,usuarioActual);
						if(huboEgreso)
						{
							logger.info("Se generÔøΩ el egreso automÔøΩtico!!!");
							
							ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
							observable.addObserver(paciente);
							
							//Se genera un asocio automÔøΩtico
							boolean huboAsocio=hacerAsocio(con,paciente,usuarioActual);
							if(huboAsocio)
								//*******************************************************
								return remitirAAdmisionHospitalaria(
										con,
										usuarioActual,
										request,
										mapping,
										ingresoPacienteForm,
										paciente.getCodigoCuenta()+"",
										paciente,
										"evolucion",
										Utilidades.obtenerDestinoSalidaEgresoEvolucion(con,paciente.getCodigoCuenta()));
							else
								logger.info("NOOO Se generÔøΩ el ASOCIO AUTO!");
							
														
						}
						else
							logger.info("NOOOOO Se generÔøΩ el egreso automÔøΩtico!!!");
					}
					//En el caso de que ya haya un Egreso
					else
					{	
						//Se verifica si ya hubo un asocio
						if(!paciente.getExisteAsocio())
						{
							//Se genera un asocio automÔøΩtico
							boolean huboAsocio=hacerAsocio(con,paciente,usuarioActual);
							if(huboAsocio)
								return remitirAAdmisionHospitalaria(
										con,
										usuarioActual,
										request,
										mapping,
										ingresoPacienteForm,
										paciente.getCodigoCuenta()+"",
										paciente,
										"evolucion",
										Utilidades.obtenerDestinoSalidaEgresoEvolucion(con,paciente.getCodigoCuenta()));
							else
								logger.info(" NOOO Se generÔøΩ el ASOCIO AUTO!");
						}
						
						
					}
				}
				catch(Exception e)
				{
					logger.error("Error al crear el asocio de Hospitalizacion - Hospitalizado "+e);
				}
			}
		}
		
		return null;
	}



	/**
	 * MÔøΩtodo que realiza la verificaciÔøΩn de los convenios del paciente
	 * @param con
	 * @param ingresoPacienteForm
	 * @param paciente
	 * @param mapping
	 * @param usuarioActual 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward verificacionConveniosPaciente(Connection con, IngresoPacienteForm ingresoPacienteForm, PersonaBasica paciente, ActionMapping mapping, UsuarioBasico usuarioActual) throws IPSException 
	{
		//Por defecto No hubo seleccion de convenios por la pagina de convenios del paciente
		ingresoPacienteForm.setHuboSeleccionConvenio(false);
		
		//*******Se consultan los convenios que tenga el paciente registrado en su histÔøΩrico de atencion y los capitados*********************************
		ingresoPacienteForm.setConveniosPaciente(UtilidadesManejoPaciente.consultarConveniosPaciente(con, paciente.getCodigoPersona()+""));
		int numRegistros = Integer.parseInt(ingresoPacienteForm.getConveniosPaciente("numRegistros").toString()); 
		 
		
		//Si la suma de los convenios del paciente y el capitado es mayor a 1 se muestra la pagina de seleccion de convenios
		if((numRegistros)>1)
		{
			/**
			 * Flujo que llama la pagina de seleccion de los convenios del historico, se incluye el flujo de capitacion
			 */
			//***********************************FLUJO CONVENIOS HISTORICO PACIENTE***********************************************
			ingresoPacienteForm.setConveniosPostulados("numRegistros", "0");
			
			//Se redirecciona a la pagina de seleccion de convenios
			UtilidadBD.closeConnection(con);
			return mapping.findForward("conveniosPaciente");
			//**********************************************************************************************************************
		}
		//Si el paciente solo tiene convenio de capitaciÔøΩn se postula 
		else if(numRegistros==1)
		{
			/**
			 * Flujo que postula el ÔøΩnico convenio de un paciente, bien sea histÔøΩrico o de capitacion  
			 */
			//******************************UN SOLO CONVENIO *************************************
			ingresoPacienteForm.setHuboSeleccionConvenio(true);// para indicar que el paciente tiene un convenio en historico
			
			if(UtilidadTexto.getBoolean(ingresoPacienteForm.getConveniosPaciente("esCapitado_0").toString()))
				ingresoPacienteForm.setUsuarioConvenio(true); // para indicar que el paciente es capitado o que tiene un convenio en historico
			
			//Se postula el convenio capitado a la estructura de cuentas
			this.postularInfoConvenioPrincipal(con,ingresoPacienteForm,Integer.parseInt(ingresoPacienteForm.getConveniosPaciente("codigoConvenio_0").toString()),"",usuarioActual,paciente);
			
			ingresoPacienteForm.setCuenta("codigoEstratoSocial", "");
			ingresoPacienteForm.setCuenta("codigoTipoAfiliado", "");
			ingresoPacienteForm.setCuenta("codigoNaturaleza", "");
			
			if(!UtilidadTexto.getBoolean(ingresoPacienteForm.getConveniosPaciente("esCapitado_0").toString()))
			{
				ingresoPacienteForm.setCuenta("codigoEstratoSocial", ingresoPacienteForm.getConveniosPaciente("codigoEstratoSocial_0"));
				ingresoPacienteForm.setCuenta("fechaAfiliacion", ingresoPacienteForm.getConveniosPaciente("fechaAfiliacion_0"));
				if(!ingresoPacienteForm.getCuenta("fechaAfiliacion").toString().equals(""))
				{
					int numeroDias = UtilidadFecha.numeroDiasEntreFechas(ingresoPacienteForm.getCuenta("fechaAfiliacion").toString().toString(), UtilidadFecha.getFechaActual());
					int numeroSemanas = (numeroDias/7) + (numeroDias%7!=0?1:0);
					ingresoPacienteForm.setCuenta("semanasCotizacion", numeroSemanas+"");
				}
			}
			else
			{
				/**VERIFICAR CONTRATOS, si hay uno solo consultar validacion tipo cobro paciente para verificar la obligatoriedad los campos:
				 *  
				 * Clasificacion socioeconomica
				 * Tipo afiliado
				 * Naturaleza Paciente
				 * Porcentaje de cobertura
				 * Cuota Verificacion
				 * 
				 * MT 4836
				 * */
				DtoValidacionTipoCobroPaciente validacion=null;
				if(!ingresoPacienteForm.getContratos().isEmpty()&&ingresoPacienteForm.getContratos().size()==1){
					IValidacionTipoCobroPacienteServicio validacionTipoCobroPacienteServicio=FacturacionServicioFabrica.crearValidacionTipoCobroPacienteServicio();
					final int unicoContrato=0;
					HashMap<String, Object>contrato=ingresoPacienteForm.getContratos().get(unicoContrato);
					validacion=validacionTipoCobroPacienteServicio.validarTipoCobroPacienteServicioConvenioContrato(Integer.parseInt(contrato.get("codigo").toString()));
				}
					//Se agrega la informacion adicional
				if(Utilidades.convertirAEntero(ingresoPacienteForm.getConveniosPaciente("codigoEstratoSocial_0")+"")>0)
				{
					String tipoRegimenCSE=Utilidades.obtenerTipoRegimenClasificacionSocioEconomica(con, Utilidades.convertirAEntero(ingresoPacienteForm.getConveniosPaciente("codigoEstratoSocial_0")+""));
					if((ingresoPacienteForm.getCuenta("codigoTipoRegimen")+"").equals(tipoRegimenCSE))
					{
						ingresoPacienteForm.setCuenta("codigoEstratoSocial", ingresoPacienteForm.getConveniosPaciente("codigoEstratoSocial_0"));
						HashMap tempo=new HashMap();
						tempo.put("numRegistros", "0");
						HashMap tempo1=new HashMap();
						tempo1.put("numRegistros", "0");
						for(int i=0;i<Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales("numRegistros")+"");i++)
						{
							if(Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales("codigo_"+i)+"")==Utilidades.convertirAEntero(ingresoPacienteForm.getConveniosPaciente("codigoEstratoSocial_0")+""))
							{
								tempo.put("codigo_0", ingresoPacienteForm.getEstratosSociales("codigo_"+i)+"");
								tempo.put("descripcion_0", ingresoPacienteForm.getEstratosSociales("descripcion_"+i)+"");
								tempo.put("numRegistros", "1");
								break;
							}
						}
						if(!UtilidadTexto.isEmpty(ingresoPacienteForm.getConveniosPaciente("tipoafiliado_0")+""))
						{
							//{numRegistros=2, acronimo_0=B, nombre_1=Cotizante, nombre_0=Beneficiario, acronimo_1=C}
							ingresoPacienteForm.setCuenta("codigoTipoAfiliado", ingresoPacienteForm.getConveniosPaciente("tipoafiliado_0"));
							
							for(int i=0;i<Utilidades.convertirAEntero(ingresoPacienteForm.getTiposAfiliado("numRegistros")+"");i++)
							{
								if((ingresoPacienteForm.getTiposAfiliado("acronimo_"+i)+"").equals(ingresoPacienteForm.getConveniosPaciente("tipoafiliado_0")+""))
								{
									tempo1.put("acronimo_0", ingresoPacienteForm.getTiposAfiliado("acronimo_"+i)+"");
									tempo1.put("nombre_0", ingresoPacienteForm.getTiposAfiliado("nombre_"+i)+"");
									tempo1.put("numRegistros", "1");
									break;
								}
							}

						}
						if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitados(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
						{
							ingresoPacienteForm.setEstratosSociales(tempo);
							if(!UtilidadTexto.isEmpty(ingresoPacienteForm.getConveniosPaciente("tipoafiliado_0")+""))
							{
								ingresoPacienteForm.setTiposAfiliado(tempo1);
							}
						}	
						
						
						if(validacion!=null&&UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion())&&
								Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales().get("numRegistros")+"")<=0)
						{
							ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
							mensaje.agregarAtributo("La ClasificaciÛn socio econÛmica del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.");
							ingresoPacienteForm.getMensajesAlerta().add(mensaje);
						}
						if(validacion!=null&&UtilidadTexto.getBoolean(validacion.getMostrarTipoAfiliado())&&
								!UtilidadTexto.isEmpty(ingresoPacienteForm.getConveniosPaciente("tipoafiliado_0")+""))
						{
							if(Utilidades.convertirAEntero(ingresoPacienteForm.getTiposAfiliado("numRegistros")+"")<=0)
							{
								ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
								mensaje.agregarAtributo("El Tipo Afiliado del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.");
								ingresoPacienteForm.getMensajesAlerta().add(mensaje);
							}
						}
					}
					else
					{
						ingresoPacienteForm.setEstratosSociales(new HashMap());
						ingresoPacienteForm.setTiposAfiliado(new HashMap());
						
						ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
						mensaje.agregarAtributo("El Tipo de rÈgimen de la ClasificaciÛn socio  econÛmica no corresponde con el Tipo de rÈgimen del Convenio. No se puede asignar este Convenio. Por favor verifique.");
						ingresoPacienteForm.getMensajesAlerta().add(mensaje);
					}	
					
					int natPaciente=Utilidades.convertirAEntero(ingresoPacienteForm.getConveniosPaciente("codigoNaturaleza_0")+"");
					ingresoPacienteForm.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPaciente(con,ingresoPacienteForm.getCuenta("codigoTipoRegimen")+"",Integer.parseInt(ingresoPacienteForm.getConveniosPaciente("codigoConvenio_0").toString()),Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso())));
					if(validacion!=null&&UtilidadTexto.getBoolean(validacion.getMostrarNaturalezaPaciente())&&
							natPaciente>0)
					{
						if(UtilidadValidacion.esNaturalezaValidaTipoRegimen(natPaciente,ingresoPacienteForm.getCuenta("codigoTipoRegimen")+"")) 
						{

							ingresoPacienteForm.setCuenta("codigoNaturaleza", natPaciente+"");
							Vector<InfoDatosString> naturalezaVector=new Vector<InfoDatosString>();
							for(int i=0;i<ingresoPacienteForm.getNaturalezasPaciente().size();i++)
							{
								if(Utilidades.convertirAEntero(ingresoPacienteForm.getNaturalezasPaciente().get(i).getCodigo()) ==natPaciente)
								{
									naturalezaVector.add(ingresoPacienteForm.getNaturalezasPaciente().get(i));
								}
							}
							if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitados(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
							{
								ingresoPacienteForm.setNaturalezasPaciente(naturalezaVector);
							}
							
							if(ingresoPacienteForm.getNaturalezasPaciente().size()<=0)
							{
								ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
								mensaje.agregarAtributo("La Naturaleza Paciente del paciente no corresponde con los Montos Cobro del Convenio. No se puede asignar este Convenio. Por favor verifique.");
								ingresoPacienteForm.getMensajesConvenioAdicional().add(mensaje);
							}
							
						}
						else
						{
							ingresoPacienteForm.setNaturalezasPaciente(new Vector<InfoDatosString>());
							ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
							mensaje.agregarAtributo("El Tipo de rÈgimen de la Naturaleza Paciente no corresponde con el Tipo de rÈgimen del Convenio. No se puede asignar este Convenio. Por favor verifique.");
							ingresoPacienteForm.getMensajesConvenioAdicional().add(mensaje);
						}
					}
				}				
			}
				
			
			ingresoPacienteForm.setVariosConvenios("numRegistros", "0"); //se toma que no hay mas convenios
			//*****************************************************************************************************************************
			
		}
		else
			ingresoPacienteForm.setUsuarioConvenio(false);
		
		
		return null;
	}




	/**
	 * MÔøΩtodo que postula la informacion del convenio principal en la estructura de cuentas
	 * @param con
	 * @param ingresoPacienteForm
	 * @param codigoConvenio
	 * @param codigoContrato
	 * @param usuarioActual 
	 * @param paciente 
	 */
	@SuppressWarnings("rawtypes")
	private void postularInfoConvenioPrincipal(Connection con, IngresoPacienteForm ingresoPacienteForm, int codigoConvenio, String codigoContrato, UsuarioBasico usuarioActual, PersonaBasica paciente) throws IPSException 
	{
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		Convenio mundoConvenio = new Convenio();
		mundoConvenio.cargarResumen(con, codigoConvenio);
		
		ingresoPacienteForm.setCuenta("codigoConvenio", 
			mundoConvenio.getCodigo()+ConstantesBD.separadorSplit+
			mundoConvenio.getTipoContrato()+ConstantesBD.separadorSplit+
			mundoConvenio.getPyp()+ConstantesBD.separadorSplit+
			mundoConvenio.getEmpresaInstitucion()
			);
		ingresoPacienteForm.setCuenta("nombreConvenio",mundoConvenio.getNombre());
		ingresoPacienteForm.setCuenta("codigoContrato",codigoContrato);
		ingresoPacienteForm.setCuenta("codigoEstratoSocial","");
		ingresoPacienteForm.setCuenta("esConvenioPyp", UtilidadTexto.getBoolean(mundoConvenio.getPyp())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("esConvenioCapitado", mundoConvenio.getTipoContrato()==ConstantesBD.codigoTipoContratoCapitado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("codigoTipoRegimen", mundoConvenio.getTipoRegimen());
		ingresoPacienteForm.setCuenta("nombreTipoRegimen", mundoConvenio.getDescripcionTipoRegimen());
		ingresoPacienteForm.setCuenta("esConvenioColsanitas", UtilidadesFacturacion.esConvenioColsanitas(con, mundoConvenio.getCodigo(), usuarioActual.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		ingresoPacienteForm.setCuenta("isConvenioManejaMontos",mundoConvenio.isConvenioManejaMontoCobro());
		
		if(mundoConvenio.getTipoContrato()==ConstantesBD.codigoTipoContratoEvento)
			ingresoPacienteForm.setContratos(Utilidades.obtenerContratos(con, mundoConvenio.getCodigo(), false, true));
		else
			ingresoPacienteForm.setContratos(UtilidadesManejoPaciente.obtenerContratosVigentesUsuarioCapitado(con, paciente.getCodigoPersona(), mundoConvenio.getCodigo()));
		
		if(ingresoPacienteForm.getContratos().size()==1){
			final int unicoContrato=0;
			verificarMontoCobro(ingresoPacienteForm,ingresoPacienteForm.getContratos().get(unicoContrato).get("codigo").toString());
		}
		
		if(ingresoPacienteForm.getMontosCobro()==null){
			ingresoPacienteForm.setMontosCobro(new ArrayList<HashMap<String, Object>>());
		}
		
		/*
		 * SecciÛn modificada MT-3783
		 */
		ingresoPacienteForm.setEstratosSociales(UtilidadesFacturacion.cargarEstratosSociales(
				con, usuarioActual.getCodigoInstitucionInt(),
				ConstantesBD.acronimoSi, mundoConvenio.getTipoRegimen(),mundoConvenio.getCodigo(),
				Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()),
				Utilidades.capturarFechaBD()));
		
		int estratoSocial = ConstantesBD.codigoNuncaValido;
		String tipoAfiliado = "";
		if (ingresoPacienteForm.getEstratosSociales().get("codigo_0") != null) {
			estratoSocial = Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales().get("codigo_0").toString());
		}
		//*********CONSULTA DE LOS TIPOS AFILIADO*****************************************************
		HashMap tiposAfiliado = UtilidadesFacturacion.cargarTiposAfiliadoXEstrato(
				con,  usuarioActual.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, 
				mundoConvenio.getCodigo(),Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()),
				estratoSocial, Utilidades.capturarFechaBD());
		
		ingresoPacienteForm.setTiposAfiliado(tiposAfiliado);

		if (ingresoPacienteForm.getTiposAfiliado().get("acronimo_0") != null) {
			tipoAfiliado = ingresoPacienteForm.getTiposAfiliado().get("acronimo_0").toString();
		}
		
		ingresoPacienteForm.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
				con, mundoConvenio.getTipoRegimen(), 
				mundoConvenio.getCodigo(),
				Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()), 
				tipoAfiliado, 
				estratoSocial, Utilidades.capturarFechaBD()));
		
		///////////
		
		//ingresoPacienteForm.setEstratosSociales(UtilidadesFacturacion.cargarEstratosSociales(con, usuarioActual.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, mundoConvenio.getTipoRegimen(),mundoConvenio.getCodigo(),Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso())));
		//ingresoPacienteForm.setTiposAfiliado(UtilidadesFacturacion.cargarTiposAfiliado(con, usuarioActual.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, mundoConvenio.getCodigo(),Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso())));
		//ingresoPacienteForm.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPaciente(con,mundoConvenio.getTipoRegimen(),mundoConvenio.getCodigo(),Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso())));	
		
		
		//Se cargan los requisitos del pacietne
		arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
				con,
				mundoConvenio.getCodigo(), 
				ConstantesIntegridadDominio.acronimoIngreso, 
				true, 
				usuarioActual.getCodigoInstitucionInt(),
				Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()));
		ingresoPacienteForm.setCuenta("numReqIngreso", arregloAux.size()+"");
			
		for(int i=0;i<arregloAux.size();i++)
		{
			HashMap elemento = (HashMap)arregloAux.get(i);
			ingresoPacienteForm.setCuenta("codigoReqIngreso_"+i, elemento.get("codigo"));
			ingresoPacienteForm.setCuenta("descripcionReqIngreso_"+i, elemento.get("descripcion"));
			ingresoPacienteForm.setCuenta("cumplidoReqIngreso_"+i, UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("cumplidoReqIngreso_"+i)+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			
		}
		
		//Se consultan los requisitos del egreso
		arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
				con,
				mundoConvenio.getCodigo(), 
				ConstantesIntegridadDominio.acronimoEgreso, 
				true, 
				usuarioActual.getCodigoInstitucionInt(),
				Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()));
		ingresoPacienteForm.setCuenta("numReqEgreso", arregloAux.size()+"");
		
		for(int i=0;i<arregloAux.size();i++)
		{
			HashMap elemento = (HashMap)arregloAux.get(i);
			ingresoPacienteForm.setCuenta("codigoReqEgreso_"+i, elemento.get("codigo"));
			ingresoPacienteForm.setCuenta("descripcionReqEgreso_"+i, elemento.get("descripcion"));
			ingresoPacienteForm.setCuenta("cumplidoReqEgreso_"+i, UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("cumplidoReqEgreso_"+i)+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		}
		
			
		ingresoPacienteForm.setCuenta("tieneRequisitosPaciente", 
			(Integer.parseInt(ingresoPacienteForm.getCuenta("numReqIngreso").toString())>0||Integer.parseInt(ingresoPacienteForm.getCuenta("numReqEgreso").toString())>0)?
				ConstantesBD.acronimoSi:
				ConstantesBD.acronimoNo);
		
		//Se adicionan los indicadores
		ingresoPacienteForm.setCuenta("esConvenioSoat", Convenio.esConvenioTipoConventioEventoCatTrans(mundoConvenio.getCodigo())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("manejoComplejidad", mundoConvenio.getManejaComplejidad());
		ingresoPacienteForm.setCuenta("esConvenioPoliza", UtilidadTexto.getBoolean(mundoConvenio.getCheckInfoAdicCuenta())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("requiereCarnet", mundoConvenio.getRequiereNumeroCarnet());
		ingresoPacienteForm.setCuenta("semanasMinimasCotizacion", mundoConvenio.getSemanasMinimasCotizacion());
		ingresoPacienteForm.setCuenta("esConvenioVerificacion",mundoConvenio.isRequiereVerificacionDerechos()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		//Se verifica si se maneja complejidad
		ingresoPacienteForm.setCuenta("indicadorManejaComplejidad", validacionManejoComplejidad(ingresoPacienteForm));
		ingresoPacienteForm.setCuenta("esReporteAtencionInicialUrgencias", mundoConvenio.getReporte_atencion_ini_urg());
		ingresoPacienteForm.setCuenta("esRequiereAutorizacionServicio", UtilidadTexto.getBoolean(mundoConvenio.getRequiere_autorizacion_servicio())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		//debemos cargar el tipo de cobertura
		logger.info("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFdebemos cargar el tipo de cobertura");
		ingresoPacienteForm.setCoberturasSalud(UtilidadesManejoPaciente.obtenerCoberturasSaludXTipoRegimen(con, ingresoPacienteForm.getCuenta("codigoTipoRegimen").toString(), usuarioActual.getCodigoInstitucionInt()));
	}



	/**
	 * MÔøΩtodo que prepara la informacion para mostrar la pagina de cuenta
	 * @param con
	 * @param ingresoPacienteForm
	 * @param usuarioActual 
	 * @param paciente 
	 * @param request 
	 */
	private void prepararInformacionCuenta(Connection con, IngresoPacienteForm ingresoPacienteForm, UsuarioBasico usuarioActual, PersonaBasica paciente) throws IPSException 
	{
		//***********INICIACION DE CAMPOS DEL MAPA********************************************************
		//Si el paciente NO es capitado se limpia la informacion que involucra al convenio
		if(!ingresoPacienteForm.isUsuarioConvenio()&&!ingresoPacienteForm.isHuboSeleccionConvenio())
		{
			ingresoPacienteForm.setCuenta("esConvenioPoliza", ConstantesBD.acronimoNo); //por defecto es en no
			ingresoPacienteForm.setCuenta("esConvenioColsanitas", ConstantesBD.acronimoNo);
			ingresoPacienteForm.setCuenta("codigoEstratoSocial", "");
			ingresoPacienteForm.setCuenta("codigoTipoAfiliado", "");
			ingresoPacienteForm.setCuenta("codigoNaturaleza", "");
			ingresoPacienteForm.setCuenta("tieneRequisitosPaciente", ConstantesBD.acronimoNo);
			ingresoPacienteForm.setCuenta("codigoContrato", "");
			ingresoPacienteForm.setCuenta("indicadorManejaComplejidad", ConstantesBD.acronimoNo); //se maneja por defecto en NO
			ingresoPacienteForm.setEstratosSociales(new HashMap());
			ingresoPacienteForm.setTiposAfiliado(new HashMap());
			ingresoPacienteForm.getEstratosSociales().put("numRegistros", "0");
			ingresoPacienteForm.setVariosConvenios("numRegistros", "0");
		}
		
		if(Utilidades.convertirAEntero(ingresoPacienteForm.getVariosConvenios("numRegistros")+"", true)>0)
			ingresoPacienteForm.setCuenta("seccionVariosConvenios", ConstantesBD.acronimoSi);
		else
			ingresoPacienteForm.setCuenta("seccionVariosConvenios", ConstantesBD.acronimoNo);
		
		
		ingresoPacienteForm.setCuenta("codigoViaIngreso", "");
		ingresoPacienteForm.setCuenta("codigoTipoAfiliado", "");
		//Se pregunta si se debe hacer la validacion de hospital dÔøΩa
		ingresoPacienteForm.setCuenta("manejaHospitalDia", ValoresPorDefecto.getEntidadManejaHospitalDia(usuarioActual.getCodigoInstitucionInt()));
		ingresoPacienteForm.setCuenta("hospitalDia", "");
		
		ingresoPacienteForm.setCuenta("codigoConvenioDefault", "");
		
		
		ingresoPacienteForm.setCuenta("fechaAdmision", UtilidadFecha.getFechaActual());
		ingresoPacienteForm.setCuenta("horaAdmision", UtilidadFecha.getHoraActual());
		
		//inicializacion de los campos de la verifiacion de derechos
		ingresoPacienteForm.setVerificacion("personaSolicita", usuarioActual.getNombreUsuario());
		ingresoPacienteForm.setVerificacion("fechaSolicitud", UtilidadFecha.getFechaActual());
		ingresoPacienteForm.setVerificacion("horaSolicitud", UtilidadFecha.getHoraActual());
		ingresoPacienteForm.setVerificacion("fechaVerificacion", UtilidadFecha.getFechaActual());
		ingresoPacienteForm.setVerificacion("horaVerificacion", UtilidadFecha.getHoraActual());
		ingresoPacienteForm.setCuenta("seccionVerificacionDerechos", ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("tieneVerificacionDerechos", ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("esAsocio", ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("codigoNaturaleza", null);//ConstantesBD.codigoNaturalezaPacientesNinguno+""); //Se postula por defecto el valor ninguno
		
		//**************SE CONSULTAN LAS VIAS DE INGRESO*****************************************************************
		ingresoPacienteForm.setViasIngreso((Vector<InfoDatosString>)Utilidades.obtenerViasIngreso(con, true));
		//**************SE CONSULTAN LAS CONVENIOS*****************************************************************
		ingresoPacienteForm.setConvenios(Utilidades.obtenerConvenios(con, "", ConstantesBD.codigoTipoContratoEvento+"", true, "", true));
		//Si el paciente es capitado, se agregan todos los convenios capitados al listado de convenios
		if(ingresoPacienteForm.isUsuarioConvenio())
		{
			//Se verifica si el convenio principal postulado es capitado
			if(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esConvenioCapitado")+""))
			{
				String[] vector = ingresoPacienteForm.getCuenta("codigoConvenio").toString().split(ConstantesBD.separadorSplit);
				
				HashMap<String,Object> elemento = new HashMap<String, Object>();
				elemento.put("codigoConvenio", vector[0]);
				elemento.put("nombreConvenio", ingresoPacienteForm.getCuenta("nombreConvenio"));
				elemento.put("codigoTipoContrato", vector[1]);
				elemento.put("pyp", vector[2]);
				elemento.put("empresasInstitucion", vector[3]);
				ingresoPacienteForm.getConvenios().add(elemento);
			}
			
			//Se verifican los otros convenios 
			for(int i=0;i<Utilidades.convertirAEntero(ingresoPacienteForm.getVariosConvenios("numRegistros")+"", true);i++)
			{
				//Se verifica si el convenio principal postulado es capitado
				if(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("esConvenioCapitado_"+i)+""))
				{
					HashMap<String,Object> elemento = new HashMap<String, Object>();
					elemento.put("codigoConvenio", ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i));
					elemento.put("nombreConvenio", ingresoPacienteForm.getVariosConvenios("nombreConvenio_"+i));
					elemento.put("codigoTipoContrato", UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("esConvenioCapitado_"+i).toString())?ConstantesBD.codigoTipoContratoCapitado:ConstantesBD.codigoTipoContratoEvento);
					elemento.put("pyp", UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("esConvenioPyp_"+i).toString()));
					elemento.put("empresasInstitucion", ingresoPacienteForm.getVariosConvenios("empresasInstitucion_"+i));
					ingresoPacienteForm.getConvenios().add(elemento);
				}
				
			}
			
		}
		//******************************************************************************************************************
		
		
		ingresoPacienteForm.setAreas(new HashMap());
		ingresoPacienteForm.getAreas().put("numRegistros", "0");
		
		
		//************SE CONSULTAN LOS TIPOS DE IDENTIFICACION (SOLO APLICA PARA POLIZA)**************************************
		ingresoPacienteForm.setTiposIdentificacion(Utilidades.obtenerTiposIdentificacion(con, "ingresoPoliza", usuarioActual.getCodigoInstitucionInt()));
		//*********************************************************************************************************************
		
		
		//*************SE CONSULTAN LOS TIPOS DE COMPLEJIDAD ****************************************************************
		ingresoPacienteForm.setTiposComplejidad(Utilidades.obtenerTiposComplejidad(con));
		//********************************************************************************************************************
		
		//****************SE CONSULTA LOS CONVENIOS ARP********************************************************************************
		ingresoPacienteForm.setConveniosArp(UtilidadesFacturacion.cargarConveniosXClasificacion(con, ConstantesBDFacturacion.codigoClasTipoConvenioARP));
		//*********************************************************************************************************************
		//**************SE CONSULTA LOS ORIGENES DE LA ADMISION***************************************************************
		ingresoPacienteForm.setOrigenesAdmisiones(UtilidadesManejoPaciente.obtenerOrigenesAdmision(con));
		//*******************************************************************************************
		
		//***********SE POSTULA INFORMACION POR DEFECTO DE LA POLIZA*************************************************
		ingresoPacienteForm.setCuenta("apellidosPoliza", paciente.getPrimerApellido()+" "+paciente.getSegundoApellido());
		ingresoPacienteForm.setCuenta("nombresPoliza", paciente.getPrimerNombre()+" "+paciente.getSegundoNombre());
		ingresoPacienteForm.setCuenta("tipoIdPoliza", paciente.getCodigoTipoIdentificacionPersona());
		ingresoPacienteForm.setCuenta("numeroIdPoliza", paciente.getNumeroIdentificacionPersona());
		ingresoPacienteForm.setCuenta("direccionPoliza", paciente.getDireccion());
		ingresoPacienteForm.setCuenta("telefonoPoliza", paciente.getTelefono());
		
		//**********SE CONSULTA/POSTULA INFORMACION DEL RESPONSABLE DEL PACIENTE************************************
		ingresoPacienteForm.setCriterioBarrio("");
		ingresoPacienteForm.setCuenta("esRequeridoResponsablePaciente", ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("seccionResponsablePaciente", ConstantesBD.acronimoNo);
		ingresoPacienteForm.setPaises(Utilidades.obtenerPaises(con));
		ingresoPacienteForm.setResponsable("editable", ConstantesBD.acronimoNo);
		ingresoPacienteForm.setResponsable("existe", ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCiudades(new ArrayList<HashMap<String,Object>>());
		ingresoPacienteForm.setCiudadesExp(new ArrayList<HashMap<String,Object>>());
		ingresoPacienteForm.setTiposIdResponsable(Utilidades.obtenerTiposIdentificacion(con, "ingresoPaciente", usuarioActual.getCodigoInstitucionInt()));
		ParametrizacionInstitucion mundoInstitucion = new ParametrizacionInstitucion();
		mundoInstitucion.cargar(con, usuarioActual.getCodigoInstitucionInt());

		
		if(!mundoInstitucion.getPais().getCodigo().equals(""))
		{
			ingresoPacienteForm.setResponsable("paisExpedicion", mundoInstitucion.getPais().getCodigo());
			ingresoPacienteForm.setResponsable("ciudadExpedicion", mundoInstitucion.getDepartamento().getCodigo()+ConstantesBD.separadorSplit+mundoInstitucion.getCiudad().getCodigo());
			ingresoPacienteForm.setResponsable("pais", mundoInstitucion.getPais().getCodigo());
			ingresoPacienteForm.setResponsable("ciudad", mundoInstitucion.getDepartamento().getCodigo()+ConstantesBD.separadorSplit+mundoInstitucion.getCiudad().getCodigo());
			ingresoPacienteForm.setCiudades(Utilidades.obtenerCiudadesXPais(con, mundoInstitucion.getPais().getCodigo()));
			ingresoPacienteForm.setCiudadesExp(Utilidades.obtenerCiudadesXPais(con, mundoInstitucion.getPais().getCodigo()));
		}
		//**********************************************************************************************
		
		//SE postula el nÔøΩmero de carnet si hay ficha usuario capitado
		ingresoPacienteForm.setCuenta("numeroCarnet", ingresoPacienteForm.getFichaUsuarioCapitado());
		
		
		//***********VALIDACION PACIENTE TRIAGE*********************************************************
		//Para saber si el paciente era un paciente pendiente de admision en triage el campo consecutivo triage debe estar lleno
		boolean pacienteRespondeLlamadoTriage= true;
		if(!ingresoPacienteForm.getConsecutivoTriage().equals("")&&!ingresoPacienteForm.getConsecutivoTriage().equals("NoAplica"))
		{
			Triage triage= new Triage();
			pacienteRespondeLlamadoTriage= !UtilidadTexto.getBoolean(triage.cargarTriage(con, ingresoPacienteForm.getConsecutivoTriage(), ingresoPacienteForm.getConsecutivoFechaTriage()).getNoRespondeLlamado()); 
		}
		
		if(!ingresoPacienteForm.getConsecutivoTriage().equals("")&&!ingresoPacienteForm.getConsecutivoTriage().equals("NoAplica") && pacienteRespondeLlamadoTriage)
		{
			ingresoPacienteForm.setCuenta("codigoViaIngreso", ConstantesBD.codigoViaIngresoUrgencias+"");
			ingresoPacienteForm.setCodigoViaIngreso(ConstantesBD.codigoViaIngresoUrgencias+"");
			
			//1) Solo se debe mostrar la vÔøΩa de ingreso urgencias
			ingresoPacienteForm.setViasIngreso(new Vector<InfoDatosString>());
			InfoDatosString viaIng = new InfoDatosString(ConstantesBD.codigoViaIngresoUrgencias+"",Utilidades.obtenerNombreViaIngreso(con, ConstantesBD.codigoViaIngresoUrgencias));
			ingresoPacienteForm.getViasIngreso().add(viaIng);
			
			prepararInformacionViaIngreso(con, ingresoPacienteForm, usuarioActual, paciente);
						
			
		}
		else
		{
			ingresoPacienteForm.setConsecutivoTriage("");
			ingresoPacienteForm.setConsecutivoFechaTriage("");
		}
		//************************************************************************************************
		
		//************CASO CUANDO SOLO HAY UNA VIA DE INGRESO PARAMETRIZADA**************************************
		if(ingresoPacienteForm.getViasIngreso().size()==1)
		{
			ingresoPacienteForm.setCuenta("codigoViaIngreso", ingresoPacienteForm.getViasIngreso().get(0).getAcronimo());
			ingresoPacienteForm.setCodigoViaIngreso(ingresoPacienteForm.getViasIngreso().get(0).getAcronimo());
			
			prepararInformacionViaIngreso(con, ingresoPacienteForm, usuarioActual, paciente);
			
		}
		//********************************************************************************************************
		
	}

	
	/**
	 * MÔøΩtodo para preparar la informacion de la via de ingreso
	 * @param con
	 * @param ingresoPacienteForm
	 * @param usuarioActual
	 * @param paciente
	 */
	@SuppressWarnings({ "rawtypes"})
	private void prepararInformacionViaIngreso(Connection con,IngresoPacienteForm ingresoPacienteForm,UsuarioBasico usuarioActual,PersonaBasica paciente) throws IPSException
	{
		///se pregunta si la institucion maneja hospital dÔøΩa
		if(ingresoPacienteForm.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		{
		
			ingresoPacienteForm.setCuenta("manejaHospitalDia", ValoresPorDefecto.getEntidadManejaHospitalDia(usuarioActual.getCodigoInstitucionInt()));
		}
		else
		{
			ingresoPacienteForm.setCuenta("manejaHospitalDia", ConstantesBD.acronimoNo);
		}
		
		//1) Se consultan los tipos de paciente x via de ingreso
		ingresoPacienteForm.setTiposPaciente(UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, ingresoPacienteForm.getCodigoViaIngreso()));
		
		//2) Se filtran las ÔøΩreas
		if(ingresoPacienteForm.getTiposPaciente().size()==1)
		{
			ingresoPacienteForm.setAreas(Utilidades.getCentrosCostoXViaIngresoXCAtencion(con, Integer.parseInt(ingresoPacienteForm.getCodigoViaIngreso()), usuarioActual.getCodigoCentroAtencion(), usuarioActual.getCodigoInstitucionInt(), ingresoPacienteForm.getTiposPaciente().get(0).get("codigoTipoPaciente").toString()));
		}
		
		//3) Consulta de los campos de validacion x via de ingreso
		ViasIngreso mundoViaIngreso = new ViasIngreso();
		HashMap datosViaIng = mundoViaIngreso.consultarViasIngresoEspecifico(con, Integer.parseInt(ingresoPacienteForm.getCodigoViaIngreso()));
		//A) Indicativo de la verificacion
		ingresoPacienteForm.setCuenta("esViaIngresoVerificacion",UtilidadTexto.getBoolean(datosViaIng.get("verificacion_0")+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		//B) Indicativo de requerido responsable paciente
		ingresoPacienteForm.setCuenta("esRequeridoResponsablePaciente",UtilidadTexto.getBoolean(datosViaIng.get("paciente_0")+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		//4) Consultar el convenio por defecto si la via de ingreso lo tiene parametrizado
		ingresoPacienteForm.setCuenta("codigoConvenioDefault",datosViaIng.get("convenio_0").toString());
		if(this.agregaConvenioDefault(con,ingresoPacienteForm,datosViaIng.get("convenio_0").toString(),usuarioActual,paciente))
		{
			ingresoPacienteForm.setCuenta("seccionVariosConvenios", ConstantesBD.acronimoSi);
			ingresoPacienteForm.setVariosConvenios("prioridadDefault", obtenerPrioridadConvenioDefault(ingresoPacienteForm));
		}
	}


	/**
	 * MÔøΩtodo implementado para agregar el convenio por default de la vÔøΩa de ingreso a la estructura de convenios
	 * @param con 
	 * @param ingresoPacienteForm
	 * @param codigoConvenio
	 * @param usuarioActual 
	 * @param paciente 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean agregaConvenioDefault(Connection con, IngresoPacienteForm ingresoPacienteForm, String codigoConvenio, UsuarioBasico usuarioActual, PersonaBasica paciente) throws IPSException 
	{
		boolean fueAgregado = false;
		int posDefault = ConstantesBD.codigoNuncaValido ; //posicion del convenio por defecto
		
		ingresoPacienteForm.setNumConvenios(Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numRegistros").toString()));
		
		//Se verifica que el convenio no haya sido seleccionado
		if(UtilidadCadena.noEsVacio(codigoConvenio))
		{
			
			if(!fueSeleccionadoConvenio(ingresoPacienteForm, codigoConvenio))
			{
				
				//Se toma el nÔøΩmero de convenios que hay hasta ahora
				int numConvenios = UtilidadCadena.noEsVacio(ingresoPacienteForm.getVariosConvenios("numRegistros")+"")?Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numRegistros").toString()):0;
				posDefault = numConvenios;
				//se postula el convenio adicional
				this.postularInfoConvenioAdicional(con, ingresoPacienteForm, Integer.parseInt(codigoConvenio), usuarioActual, numConvenios, paciente);
				
				numConvenios++;
				ingresoPacienteForm.setCuenta("numConvenios", numConvenios+"");
				ingresoPacienteForm.setVariosConvenios("numRegistros", numConvenios+"");
				fueAgregado = true;
				
				
			}
			else
			{
				for(int i=0;i<ingresoPacienteForm.getNumConvenios();i++)
				{
					if(ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i)!=null&&
						ingresoPacienteForm.getVariosConvenios("codigoConvenio_"+i).toString().equals(codigoConvenio))
						posDefault = i;
				}
				if(posDefault!=ConstantesBD.codigoNuncaValido)
					ingresoPacienteForm.setVariosConvenios("codigoViaIngreso_"+posDefault, ingresoPacienteForm.getCodigoViaIngreso());
			}
			
			if(posDefault!=ConstantesBD.codigoNuncaValido)
			{
				///**********SE TRATA DE POSTULAR DE UNA VEZ TODA LA INFORMACIÔøΩN DE LOS MONTOS DE COBRO Y CONTRATO****************************
				//se cargan los estratos sociales del convenio
				HashMap estratosSociales = UtilidadesFacturacion.cargarEstratosSociales(
					con, 
					usuarioActual.getCodigoInstitucionInt(), 
					ConstantesBD.acronimoSi, 
					ingresoPacienteForm.getVariosConvenios("codigoTipoRegimen_"+posDefault).toString(),
					Integer.parseInt(codigoConvenio),
					Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()),
					Utilidades.capturarFechaBD());
				
				ingresoPacienteForm.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPaciente(con,ingresoPacienteForm.getVariosConvenios("codigoTipoRegimen_"+posDefault).toString(),
						Integer.parseInt(codigoConvenio),
						Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso())));
				
				//Se cargan los contratos del convenio
				ArrayList<HashMap<String,Object>> arregloContratos = new ArrayList<HashMap<String,Object>>();
				
				if(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("esConvenioCapitado_"+posDefault).toString()))
					arregloContratos = UtilidadesManejoPaciente.obtenerContratosVigentesUsuarioCapitado(con, paciente.getCodigoPersona(), Integer.parseInt(codigoConvenio));
				else
					arregloContratos = Utilidades.obtenerContratos(con, Integer.parseInt(codigoConvenio), false, true);
				
				logger.info("codigo via ingreso: "+ingresoPacienteForm.getCodigoViaIngreso());
				logger.info("numero de estratos sociales encontrados: "+Utilidades.convertirAEntero(estratosSociales.get("numRegistros")+""));
				logger.info("numero de contratos encontrados: "+arregloContratos.size());
				
				//Si solo hay un estrato social se miran los montos de cobro
				if(Utilidades.convertirAEntero(estratosSociales.get("numRegistros")+"")==1&&arregloContratos.size()==1)
				{
					String codigoEstratoSocial = estratosSociales.get("codigo_0").toString();
					int numRegMonto = 0; //usada para contar cuantos registros de monto de cobro hay para el convenio
					int posicionMonto = 0; //elemento encontrado dentro del arreglo que cumple con las cvalidaciones
					//Se cargan los montos de cobro
					ArrayList<HashMap<String, Object>> montosCobro = UtilidadesFacturacion.obtenerMontosCobroXConvenio(con, codigoConvenio, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
					//Se iteran los montos de cobro para verificar si existe solo uno y postularlo de una vez
					for(int i=0;i<montosCobro.size();i++)
					{
						HashMap<String, Object> elemento = montosCobro.get(i);
						if(elemento.get("estratoSocial").toString().equals(codigoEstratoSocial)&&
							elemento.get("viaIngreso").toString().equals(ingresoPacienteForm.getCodigoViaIngreso()))
						{
							numRegMonto++;
							posicionMonto = i;
						}
					}
					
					
					
					//Si solo se encontrÔøΩ un registro se postula su informacion
					if(numRegMonto==1)
					{
						ingresoPacienteForm.setVariosConvenios("codigoViaIngreso_"+posDefault, ingresoPacienteForm.getCodigoViaIngreso());
						ingresoPacienteForm.setVariosConvenios("codigoEstratoSocial_"+posDefault, codigoEstratoSocial);
						ingresoPacienteForm.setVariosConvenios("codigoTipoAfiliado_"+posDefault, montosCobro.get(posicionMonto).get("codigoTipoAfiliado"));
						ingresoPacienteForm.setVariosConvenios("codigoMontoCobro_"+posDefault, montosCobro.get(posicionMonto).get("codigo"));
						ingresoPacienteForm.setVariosConvenios("codigoContrato_"+posDefault, arregloContratos.get(0).get("codigo"));
						ingresoPacienteForm.setVariosConvenios("ingresoVerificacionDerechos_"+posDefault, ConstantesBD.acronimoNo);
					}
				}
				
				//***********************************************************************************
			}
		}
		
		return fueAgregado;
		
	}



	/**
	 * MÔøΩtodo que realiza la verificacion de si el paciente que se va a ingresar es un usuario en el sistema
	 * @param con
	 * @param ingresoPacienteForm
	 */
	@SuppressWarnings({ "rawtypes"})
	private void validacionUsuarioPaciente(Connection con, IngresoPacienteForm ingresoPacienteForm) 
	{
		String tipoBD = System.getProperty("TIPOBD");
		boolean existeUsuario = false;
		try 
		{
			existeUsuario = UtilidadValidacion.existeUsuario(con,tipoBD,ingresoPacienteForm.getCodigoTipoIdentificacion(),ingresoPacienteForm.getNumeroIdentificacion());
		} catch (SQLException e) 
		{
			logger.error("Error al verificar si existe usuario para ese paciente: "+e);
		}
		
		if(existeUsuario)
		{
			//se consultan datos de la persona
			HashMap datosPersona = Utilidades.obtenerDatosPersona(
							con,
							ingresoPacienteForm.getCodigoTipoIdentificacion(),
							ingresoPacienteForm.getNumeroIdentificacion()
							);
			//Se llena la forma con los datos de la persona
			ingresoPacienteForm.setCodigoCiudadNacimiento(datosPersona.get("codigo_depto_nacimiento_0").toString()+ConstantesBD.separadorSplit+datosPersona.get("codigo_ciudad_nacimiento_0").toString());
			ingresoPacienteForm.setCodigoPaisNacimiento(datosPersona.get("codigo_pais_nacimiento_0").toString());
			ingresoPacienteForm.setFechaNacimiento(datosPersona.get("fecha_nacimiento_0").toString());
			ingresoPacienteForm.setEstadoCivil(datosPersona.get("codigo_estado_civil_0").toString());
			ingresoPacienteForm.setSexo(datosPersona.get("codigo_sexo_0").toString());
			ingresoPacienteForm.setPrimerNombrePersona(datosPersona.get("primer_nombre_0").toString());
			ingresoPacienteForm.setSegundoNombrePersona(datosPersona.get("segundo_nombre_0").toString());
			ingresoPacienteForm.setPrimerApellidoPersona(datosPersona.get("primer_apellido_0").toString());
			ingresoPacienteForm.setSegundoApellidoPersona(datosPersona.get("segundo_apellido_0").toString());
			ingresoPacienteForm.setDireccion(datosPersona.get("direccion_0").toString());
			ingresoPacienteForm.setCodigoPaisResidencia(datosPersona.get("codigo_pais_vivienda_0").toString());
			ingresoPacienteForm.setCodigoCiudadResidencia(datosPersona.get("codigo_depto_vivienda_0").toString()+ConstantesBD.separadorSplit+datosPersona.get("codigo_ciudad_vivienda_0").toString());
			ingresoPacienteForm.setCodigoBarrio(datosPersona.get("codigo_barrio_0").toString());
			ingresoPacienteForm.setNombreBarrio(datosPersona.get("barrio_0").toString());
			ingresoPacienteForm.setTelefono(datosPersona.get("telefono_0").toString());
			ingresoPacienteForm.setEmail(datosPersona.get("email_0").toString());
			ingresoPacienteForm.setCodigoPaisId(datosPersona.get("codigo_pais_id_0").toString());
			ingresoPacienteForm.setCodigoCiudadId(datosPersona.get("codigo_depto_id_0").toString()+ConstantesBD.separadorSplit+datosPersona.get("codigo_ciudad_id_0").toString());
			ingresoPacienteForm.setUsuarioPaciente(true);
			
			
			
			//Se cargan las localidades del paciente
			ingresoPacienteForm.setLocalidades(Utilidades.obtenerLocalidades(	
				con, 
				ingresoPacienteForm.getCodigoPaisResidencia(), 
				ingresoPacienteForm.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[0], 
				ingresoPacienteForm.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[1]));
			
			//Se consulta el login del usuario
			Usuario mundoUsuario = new Usuario();
			mundoUsuario.init(tipoBD);
			ingresoPacienteForm.setLoginUsuarioPaciente(mundoUsuario.buscarLogin(con, ingresoPacienteForm.getCodigoTipoIdentificacion(), ingresoPacienteForm.getNumeroIdentificacion(), ""));
			
			//********SE AGREGA MENSAJE DE ALERTA SOBRE EL USUARIO PACIENTE************************************
			ElementoApResource alerta = new ElementoApResource("errors.notEspecific");
			String contenido = "El paciente ingresado es un usuario del sistema con el login <b>"+ingresoPacienteForm.getLoginUsuarioPaciente()+"</b>";
			if(ingresoPacienteForm.isIngresoDesdeReservaCita())
				contenido += " ÔøΩDesea continuar con la reserva de cita?";
			else if(ingresoPacienteForm.isIngresoDesdeReferencia())
				contenido += " ÔøΩDesea continuar con la referencia?";
			else
				contenido += " ÔøΩDesea abrirle una cuenta como paciente?";
			alerta.agregarAtributo(contenido);
			ingresoPacienteForm.getMensajesAlerta().add(alerta);
			//****************************************************************************************************
			
		}
		else
			ingresoPacienteForm.setUsuarioPaciente(false);
		
	}




	/**
	 * Preparar informaciÔøΩn del paciente
	 * @param con
	 * @param ingresoPacienteForm
	 * @param usuarioActual
	 */
	@SuppressWarnings({"unchecked" })
	private void prepararInformacionPaciente(Connection con, IngresoPacienteForm ingresoPacienteForm, UsuarioBasico usuarioActual) 
	{
		//*****CARGAR ESTRUCTURAS***********************
		ingresoPacienteForm.setPaises(Utilidades.obtenerPaises(con));
		ingresoPacienteForm.setZonasDomicilio(Utilidades.consultarZonasDomicilio(con));
		ingresoPacienteForm.setOcupaciones(Utilidades.consultarOcupaciones(con));
		ingresoPacienteForm.setSexos(Utilidades.obtenerSexos(con));
		ingresoPacienteForm.setTiposSangre(Utilidades.consultarTiposSangre(con));
		ingresoPacienteForm.setEstadosCiviles(Utilidades.consultarEstadosCiviles(con));
		ingresoPacienteForm.setCentrosAtencion(Utilidades.obtenerCentrosAtencion(usuarioActual.getCodigoInstitucionInt()));
		ingresoPacienteForm.setEtnias(UtilidadesManejoPaciente.obtenerEtnias(con));
		ingresoPacienteForm.setReligiones(UtilidadesManejoPaciente.obtenerReligiones(con, usuarioActual.getCodigoInstitucionInt()));
		ingresoPacienteForm.setEstudios(UtilidadesManejoPaciente.obtenerEstudios(con));
		//********************************************
		
		
		
		//Se verifica si se debe ingresar edad en el formulario de ingreso del paciente
		ingresoPacienteForm.setIngresoEdad(UtilidadTexto.getBoolean(ValoresPorDefecto.getFechaNacimiento(usuarioActual.getCodigoInstitucionInt())));
		if(ingresoPacienteForm.isIngresoEdad())
			ingresoPacienteForm.setSeleccionEdad("anios");
		
		//**************SE POSTULA LA INFORMACION POR DEFECTO***********************************
		
		//Consulta la parametrizacion de paises de la institucin
		ParametrizacionInstitucion mundoInstitucion = new ParametrizacionInstitucion();
		mundoInstitucion.cargar(con, usuarioActual.getCodigoInstitucionInt());
		if(!mundoInstitucion.getPais().getCodigo().equals(""))
		{
			ingresoPacienteForm.setCodigoPaisId(mundoInstitucion.getPais().getCodigo());
			ingresoPacienteForm.setCodigoPaisNacimiento(mundoInstitucion.getPais().getCodigo());
			ingresoPacienteForm.setCodigoPaisResidencia(mundoInstitucion.getPais().getCodigo());
			
			ingresoPacienteForm.setCiudades(Utilidades.obtenerCiudadesXPais(con, mundoInstitucion.getPais().getCodigo()));
			ingresoPacienteForm.setCiudadesExp(Utilidades.obtenerCiudadesXPais(con, mundoInstitucion.getPais().getCodigo()));
			ingresoPacienteForm.setCiudadesNac(Utilidades.obtenerCiudadesXPais(con, mundoInstitucion.getPais().getCodigo()));
		}
		
		//Consulta del parametro paÔøΩs nacimiento
		String paisNacimiento = ValoresPorDefecto.getPaisNacimiento(usuarioActual.getCodigoInstitucionInt());
		String ciudadNacimiento = ValoresPorDefecto.getCiudadNacimiento(usuarioActual.getCodigoInstitucionInt());
		if(UtilidadCadena.noEsVacio(paisNacimiento)&&!paisNacimiento.equals(" - "))
		{
			ingresoPacienteForm.setCodigoPaisNacimiento(paisNacimiento.split("-")[0]);
			ingresoPacienteForm.setCiudadesNac(Utilidades.obtenerCiudadesXPais(con, paisNacimiento.split("-")[0]));
			if(UtilidadCadena.noEsVacio(ciudadNacimiento)&&!ciudadNacimiento.equals(" - - "))
				ingresoPacienteForm.setCodigoCiudadNacimiento(ciudadNacimiento.split("-")[0]+ConstantesBD.separadorSplit+ciudadNacimiento.split("-")[1]);
		}
		
		//Consulta del parametro paÔøΩs residencia
		String paisResidencia = ValoresPorDefecto.getPaisResidencia(usuarioActual.getCodigoInstitucionInt());
		String ciudadResidencia = ValoresPorDefecto.getCiudadVivienda(usuarioActual.getCodigoInstitucionInt());
		if(UtilidadCadena.noEsVacio(paisResidencia)&&!paisResidencia.equals(" - "))
		{
			ingresoPacienteForm.setCodigoPaisResidencia(paisResidencia.split("-")[0]);
			ingresoPacienteForm.setCiudades(Utilidades.obtenerCiudadesXPais(con, paisResidencia.split("-")[0]));

			if(UtilidadCadena.noEsVacio(ciudadResidencia)&&!ciudadResidencia.equals(" - - "))
			{
				
				ingresoPacienteForm.setCodigoCiudadResidencia(ciudadResidencia.split("-")[0]+ConstantesBD.separadorSplit+ciudadResidencia.split("-")[1]);
				//se llenan las localidades
				ingresoPacienteForm.setLocalidades(Utilidades.obtenerLocalidades(
						con, 
						ingresoPacienteForm.getCodigoPaisResidencia(), 
						ingresoPacienteForm.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[0], 
						ingresoPacienteForm.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[1]));
			}
		}
		
		//Consulta del barrio
		String barrio = ValoresPorDefecto.getBarrioResidencia(usuarioActual.getCodigoInstitucionInt());
		if(UtilidadCadena.noEsVacio(barrio))
		{
			ingresoPacienteForm.setCodigoBarrio(barrio.split("-")[2]);
			ingresoPacienteForm.setNombreBarrio(barrio.split("-")[0]+"-"+barrio.split("-")[1]);
		}
		
		//Se toma los valores de parametros generales
		ingresoPacienteForm.setDireccion(ValoresPorDefecto.getDireccionPaciente(usuarioActual.getCodigoInstitucionInt())==null?"":ValoresPorDefecto.getDireccionPaciente(usuarioActual.getCodigoInstitucionInt()));
		ingresoPacienteForm.setZonaDomicilio(ValoresPorDefecto.getZonaDomicilio(usuarioActual.getCodigoInstitucionInt())==null?"":ValoresPorDefecto.getZonaDomicilio(usuarioActual.getCodigoInstitucionInt()));
		ingresoPacienteForm.setOcupacion(ValoresPorDefecto.getOcupacion(usuarioActual.getCodigoInstitucionInt())==null?"":ValoresPorDefecto.getOcupacion(usuarioActual.getCodigoInstitucionInt()));
		ingresoPacienteForm.setLeeEscribe(ValoresPorDefecto.getValorTrueParaConsultas());
	}




	/**
	 * MÔøΩtodo usado para llenar los atributos de la forma del paciente con los datos del paciente en sesiÔøΩn
	 * @param con
	 * @param ingresoPacienteForm
	 * @param paciente
	 * @param usuarioActual 
	 */
	private void llenarFormaPaciente(Connection con, IngresoPacienteForm ingresoPacienteForm, PersonaBasica paciente, UsuarioBasico usuarioActual) 
	{
		//Se instancia mundo de paciente
		Paciente mundoPaciente = new Paciente();
		
		//Se carga paciente
		try 
		{
			mundoPaciente.cargarPaciente(con, paciente.getCodigoPersona());
		} 
		catch (SQLException e) 
		{
			logger.error("Error cargando paciente en llenarFormaPaciente: "+e);
		}
		
		ingresoPacienteForm.setNumeroHistoriaClinica(mundoPaciente.getNumeroHistoriaClinica());
		ingresoPacienteForm.setAnioHistoriaClincia(mundoPaciente.getAnioHistoriaClinica());
		ingresoPacienteForm.setTipoIdentificacion(mundoPaciente.getCodigoTipoIdentificacion()+"-"+mundoPaciente.getTipoIdentificacion());
		ingresoPacienteForm.setCodigoTipoIdentificacion(mundoPaciente.getCodigoTipoIdentificacion());
		ingresoPacienteForm.setNumeroIdentificacion(mundoPaciente.getNumeroIdentificacion());
		if(!mundoPaciente.getCodigoPaisId().equals(""))
			ingresoPacienteForm.setCodigoPaisId(mundoPaciente.getCodigoPaisId());
		if(!mundoPaciente.getCodigoCiudadId().equals("")&&!mundoPaciente.getCodigoDepartamentoId().equals(""))
			ingresoPacienteForm.setCodigoCiudadId(mundoPaciente.getCodigoDepartamentoId()+ConstantesBD.separadorSplit+mundoPaciente.getCodigoCiudadId());
		ingresoPacienteForm.setPrimerApellidoPersona(mundoPaciente.getPrimerApellidoPersona(false));
		ingresoPacienteForm.setSegundoApellidoPersona(mundoPaciente.getSegundoApellidoPersona(false));
		ingresoPacienteForm.setPrimerNombrePersona(mundoPaciente.getPrimerNombrePersona(false));
		ingresoPacienteForm.setSegundoNombrePersona(mundoPaciente.getSegundoNombrePersona(false));
		if(!mundoPaciente.getCodigoPaisIdentificacion().equals(""))
			ingresoPacienteForm.setCodigoPaisNacimiento(mundoPaciente.getCodigoPaisIdentificacion());
		if(!mundoPaciente.getCodigoDepartamentoIdentificacion().equals("")&&!mundoPaciente.getCodigoCiudadIdentificacion().equals(""))
			ingresoPacienteForm.setCodigoCiudadNacimiento(mundoPaciente.getCodigoDepartamentoIdentificacion()+ConstantesBD.separadorSplit+mundoPaciente.getCodigoCiudadIdentificacion());
		ingresoPacienteForm.setFechaNacimiento(mundoPaciente.getDiaNacimiento()+"/"+mundoPaciente.getMesNacimiento()+"/"+mundoPaciente.getAnioNacimiento());
		if(!mundoPaciente.getCodigoPais().equals(""))
			ingresoPacienteForm.setCodigoPaisResidencia(mundoPaciente.getCodigoPais());
		if(!mundoPaciente.getCodigoCiudad().equals("")&&!mundoPaciente.getCodigoDepartamento().equals(""))
			ingresoPacienteForm.setCodigoCiudadResidencia(mundoPaciente.getCodigoDepartamento()+ConstantesBD.separadorSplit+mundoPaciente.getCodigoCiudad());
		ingresoPacienteForm.setCodigoBarrio(mundoPaciente.getCodigoBarrio());
		ingresoPacienteForm.setNombreBarrio(mundoPaciente.getBarrio(false));
		ingresoPacienteForm.setCodigoLocalidad(mundoPaciente.getCodigoLocalidad());
		ingresoPacienteForm.setNombreLocalidad(mundoPaciente.getLocalidad());
		ingresoPacienteForm.setDireccion(mundoPaciente.getDireccion());
		ingresoPacienteForm.setTelefono(mundoPaciente.getTelefono());
		ingresoPacienteForm.setTelefonoFijo(mundoPaciente.getTelefonoFijo());
		ingresoPacienteForm.setTelefonoCelular(mundoPaciente.getTelefonoCelular());
		ingresoPacienteForm.setEmail(mundoPaciente.getEmail());
		ingresoPacienteForm.setZonaDomicilio(mundoPaciente.getCodigoZonaDomicilio());
		ingresoPacienteForm.setOcupacion(mundoPaciente.getCodigoOcupacion());
		ingresoPacienteForm.setSexo(mundoPaciente.getCodigoSexo());
		ingresoPacienteForm.setTipoSangre(mundoPaciente.getCodigoTipoSangre());
		ingresoPacienteForm.setTipoPersona(mundoPaciente.getCodigoTipoPersona());
		ingresoPacienteForm.setEstadoCivil(mundoPaciente.getCodigoEstadoCivil());
		ingresoPacienteForm.setCentroAtencion(mundoPaciente.getCentro_atencion()+"");
		ingresoPacienteForm.setEtnia(mundoPaciente.getEtnia()+"");
		ingresoPacienteForm.setReligion(mundoPaciente.getCodigoReligion());
		ingresoPacienteForm.setLeeEscribe(mundoPaciente.getLee_escribe().booleanValue()?ValoresPorDefecto.getValorTrueParaConsultas():ValoresPorDefecto.getValorFalseParaConsultas());
		ingresoPacienteForm.setEstudio(mundoPaciente.getEstudio()+"");
		ingresoPacienteForm.setGrupoPoblacional(mundoPaciente.getCodigoGrupoPoblacional());
		ingresoPacienteForm.setFoto(mundoPaciente.getFoto());
		ingresoPacienteForm.setExistePaciente(true);
		ingresoPacienteForm.setIdentificacionAutomatica(false);
		
		
		//Si no hay ocupacion se toma la de parÔøΩmetros generales
		if(ingresoPacienteForm.getOcupacion().equals(""))
			ingresoPacienteForm.setOcupacion(ValoresPorDefecto.getOcupacion(usuarioActual.getCodigoInstitucionInt())==null?"":ValoresPorDefecto.getOcupacion(usuarioActual.getCodigoInstitucionInt()));
			
		//Si no hay zona de domicilio se toma la de parÔøΩmetros generales
		if(ingresoPacienteForm.getZonaDomicilio().equals(""))
			ingresoPacienteForm.setZonaDomicilio(ValoresPorDefecto.getZonaDomicilio(usuarioActual.getCodigoInstitucionInt())==null?"":ValoresPorDefecto.getZonaDomicilio(usuarioActual.getCodigoInstitucionInt()));
		
		
		
		//Se cargan las localidades del paciente
		if(!ingresoPacienteForm.getCodigoCiudadResidencia().equals(""))
			ingresoPacienteForm.setLocalidades(Utilidades.obtenerLocalidades(
					con, 
					ingresoPacienteForm.getCodigoPaisResidencia(), 
					ingresoPacienteForm.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[0], 
					ingresoPacienteForm.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[1]));
		
		//se cargan las ciudades
		if(!ingresoPacienteForm.getCodigoPaisId().equals(""))
			ingresoPacienteForm.setCiudadesExp(Utilidades.obtenerCiudadesXPais(con, ingresoPacienteForm.getCodigoPaisId()));
		if(!ingresoPacienteForm.getCodigoPaisResidencia().equals(""))
			ingresoPacienteForm.setCiudades(Utilidades.obtenerCiudadesXPais(con, ingresoPacienteForm.getCodigoPaisResidencia()));
		if(!ingresoPacienteForm.getCodigoPaisNacimiento().equals(""))
			ingresoPacienteForm.setCiudadesNac(Utilidades.obtenerCiudadesXPais(con, ingresoPacienteForm.getCodigoPaisNacimiento()));
		
	}




	/**
	 * MÔøΩtodo implementado para efectuar las validaciones del consecutivo disponible de historia clinica
	 * @param con
	 * @param ingresoPacienteForm
	 * @param usuarioActual
	 * @param errores 
	 * @return
	 */
	private ActionErrors validacionConsecutivoHistoriaClinica(Connection con, UsuarioBasico usuarioActual, ActionErrors errores) 
	{
		
		String consecutivo = 
				UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoHistoriaClinica, usuarioActual.getCodigoInstitucionInt());
		
		
		if(!UtilidadCadena.noEsVacio(consecutivo) || consecutivo.equals("-1"))
			errores.add("Falta consecutivo disponible",new ActionMessage("error.paciente.faltaDefinirConsecutivo","la historia clÌnica"));
		else
		{
			try	{
				Integer.parseInt(consecutivo);
			}
			catch(Exception e) {
				logger.error("Error en validacionConsecutivoDisponible:  "+e);
				errores.add("Consecutivo no es entero", new ActionMessage("errors.integer","el consecutivo de la historia clÔøΩnica"));
			}
		}
		
		return errores;
	}
	
	
	/**
	 * MÔøΩtodo implementado para efectuar las validaciones del consecutivo disponible de ingreso	 * 
	 * @param con
	 * @param usuarioActual
	 * @param errores 
	 * @return
	 */
	private ActionErrors validacionConsecutivoIngreso(Connection con,UsuarioBasico usuarioActual, ActionErrors errores) 
	{
		
		String consecutivo=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoIngresos, usuarioActual.getCodigoInstitucionInt());
		
		
		if(!UtilidadCadena.noEsVacio(consecutivo) || consecutivo.equals("-1"))
			errores.add("Falta consecutivo disponible",new ActionMessage("error.paciente.faltaDefinirConsecutivo","el ingreso"));
		else
		{
			try
			{
				Integer.parseInt(consecutivo);
			}
			catch(Exception e)
			{
				logger.error("Error en validacionConsecutivoIngreso:  "+e);
				errores.add("Consecutivo no es entero", new ActionMessage("errors.integer","el consecutivo del ingreso"));
			}
		}
		
		return errores;
	}




	/**
	 * MÔøΩtodo para realizar la validacion de los usuarios capitados
	 * @param con
	 * @param ingresoPacienteForm
	 * @param request 
	 * @param request
	 * @param mapping 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionErrors validacionUsuariosCapitados(Connection con, IngresoPacienteForm ingresoPacienteForm, ActionErrors errores, UsuarioBasico usuario) 
	{
		//Se verifica si el paciente existe en la capita
		if(SubirPaciente.consultarCodigoUsuarioCapitado(con, ingresoPacienteForm.getCodigoTipoIdentificacion(), ingresoPacienteForm.getNumeroIdentificacion())>0)
		{
			HashMap campos = new HashMap();
			campos.put("tipoIdentificacion",ingresoPacienteForm.getCodigoTipoIdentificacion());
			campos.put("numeroIdentificacion",ingresoPacienteForm.getNumeroIdentificacion());			
	
			HashMap mapaDatosPaciente = SubirPaciente.consultarUsuarioCapitado(con,campos);
			logger.info("NUMREGISTROS USUARIO CAPITADO=> "+mapaDatosPaciente.get("numRegistros"));
			if(Integer.parseInt(mapaDatosPaciente.get("numRegistros").toString())>0)
			{
				ingresoPacienteForm.setEsPacienteCapitado(true);
				ingresoPacienteForm.setUsuarioSinCapitacionVigente(false);
				mapaDatosPaciente = SubirPaciente.removerComillasDatosUsuarioCapitado(mapaDatosPaciente);
			
				ingresoPacienteForm.setPrimerNombrePersona(mapaDatosPaciente.get("primer_nombre_0").toString());
				ingresoPacienteForm.setSegundoNombrePersona(mapaDatosPaciente.get("segundo_nombre_0").toString());
				ingresoPacienteForm.setPrimerApellidoPersona(mapaDatosPaciente.get("primer_apellido_0").toString());
				ingresoPacienteForm.setSegundoApellidoPersona(mapaDatosPaciente.get("segundo_apellido_0").toString());
				ingresoPacienteForm.setDireccion(mapaDatosPaciente.get("direccion_0").toString());
				ingresoPacienteForm.setTelefono(mapaDatosPaciente.get("telefono_0").toString());
				ingresoPacienteForm.setEmail(mapaDatosPaciente.get("email_0").toString());
				ingresoPacienteForm.setCodigoUsuarioCapitado(mapaDatosPaciente.get("codigo_0").toString());
				ingresoPacienteForm.setFechaNacimiento(mapaDatosPaciente.get("fecha_nacimiento_0").toString());
				ingresoPacienteForm.setSexo(mapaDatosPaciente.get("sexo_0").toString());
				ingresoPacienteForm.setFichaUsuarioCapitado(mapaDatosPaciente.get("numeroficha_0").toString());
				ingresoPacienteForm.setCentroAtencion(mapaDatosPaciente.get("centroatencion_0").toString());
				ingresoPacienteForm.setDatosCapitacion(mapaDatosPaciente); //Se almacenan los datos de la capitaciÔøΩn
				
				//**********VERIFICACION NOMBRES PACIENTE*****************************************
				Paciente mundoPaciente = new Paciente();
				//Se consultan pacientes que tienen el mismo nombre, solo se consulta si el indicador no estÔøΩ activado
				if(!ingresoPacienteForm.isAvisoValidacionMismosNombres())
				{
					ingresoPacienteForm.setPacientesMismosNombres(mundoPaciente.validarPacienteIgualNombre(
						con, 
						ingresoPacienteForm.getPrimerNombrePersona(), 
						ingresoPacienteForm.getSegundoNombrePersona(), 
						ingresoPacienteForm.getPrimerApellidoPersona(), 
						ingresoPacienteForm.getSegundoApellidoPersona(),
						ingresoPacienteForm.getTipoIdentificacion(),
						ingresoPacienteForm.getNumeroIdentificacion()));
					
					if(ingresoPacienteForm.getPacientesMismosNombres().size()>0)
					{
						///*************	SE AGREGA ADVERTENCIA SOBRE LOS PACIENTES CON MISMOS NOMBRES***********************
						ElementoApResource alerta = new ElementoApResource("errors.notEspecific");
						String contenido = "Ya existen pacientes con esos nombres y apellidos: ";
						for(HashMap elemento:ingresoPacienteForm.getPacientesMismosNombres())
							contenido += elemento.get("tipoId") + ". " + elemento.get("numeroId") + ", ";
						contenido += "Esta seguro que desea ingresar este paciente?";
						
						alerta.agregarAtributo(contenido);
						ingresoPacienteForm.getMensajesAlerta().add(alerta);
						//**************************************************************************************************
						
						ingresoPacienteForm.setAvisoValidacionMismosNombres(true);
					}
				}
				 //*************************************************************************
				//*****se verifica si el usuario tiene mas de 1 contrato vigente****************
				if(Integer.parseInt(mapaDatosPaciente.get("numRegistros").toString())>1)
				{
					int codigoEmpresa = 0;
					boolean diferentes = false;
					//Se verifica que los contratos pertenezcan a la misma empresa
					for(int i=0;i<Integer.parseInt(mapaDatosPaciente.get("numRegistros").toString());i++)
					{
						if(codigoEmpresa==0)
							codigoEmpresa = Utilidades.convertirAEntero(mapaDatosPaciente.get("codigo_empresa_"+i).toString(), true);
						
						if(codigoEmpresa!=Utilidades.convertirAEntero(mapaDatosPaciente.get("codigo_empresa_"+i).toString(), true))
							diferentes = true;
					}
					if(diferentes)
						errores.add("",new ActionMessage("errores.paciente.contratoCapitacion",ingresoPacienteForm.getPrimerApellidoPersona()+" "+ingresoPacienteForm.getSegundoApellidoPersona()+" "+ingresoPacienteForm.getPrimerNombrePersona()+" "+ingresoPacienteForm.getSegundoNombrePersona()));
				}
				
				
				
				//Consulta del parametro paÔøΩs residencia
				String codigoPaisResidencia=mapaDatosPaciente.get("pais_residencia_0")+"";
				String codigoCiudadResidencia=mapaDatosPaciente.get("ciudad_residencia_0")+"";
				String codigoDeptoResidencia=mapaDatosPaciente.get("departamento_residencia_0")+"";
				
				if(!UtilidadTexto.isEmpty(codigoPaisResidencia))
					ingresoPacienteForm.setCodigoPaisResidencia(codigoPaisResidencia+"");
				if(!UtilidadTexto.isEmpty(codigoCiudadResidencia)&&!UtilidadTexto.isEmpty(codigoDeptoResidencia))
				{
					ingresoPacienteForm.setCodigoCiudadResidencia(codigoDeptoResidencia+ConstantesBD.separadorSplit+codigoCiudadResidencia);
					ingresoPacienteForm.setLocalidades(Utilidades.obtenerLocalidades(
							con, 
							ingresoPacienteForm.getCodigoPaisResidencia(), 
							ingresoPacienteForm.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[0], 
							ingresoPacienteForm.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[1]));

				}
				
					
				int barrio=Utilidades.convertirAEntero(mapaDatosPaciente.get("barrio_residencia_0")+"");
				if(barrio>0)
				{
					ingresoPacienteForm.setCodigoBarrio(mapaDatosPaciente.get("barrio_residencia_0")+"");
					ingresoPacienteForm.setNombreBarrio(mapaDatosPaciente.get("descripcion_barrio_0")+"");
				}
				
				
				String codigoLocalidad=mapaDatosPaciente.get("localidad_residencia_0")+"";
				if(!UtilidadTexto.isEmpty(codigoLocalidad))
				{
					ingresoPacienteForm.setCodigoLocalidad(codigoLocalidad);
				}
				
				
			}
			else
			{
				//**********EL USUARIO NO ESTA VIGENTE EN LA CPAITACION!!!***************************************************
				mapaDatosPaciente = SubirPaciente.consultarUsuarioCapitado(con, ingresoPacienteForm.getCodigoTipoIdentificacion(), ingresoPacienteForm.getNumeroIdentificacion());
				
				ingresoPacienteForm.setPrimerNombrePersona(mapaDatosPaciente.get("primernombre_0").toString());
				ingresoPacienteForm.setSegundoNombrePersona(mapaDatosPaciente.get("segundonombre_0").toString());
				ingresoPacienteForm.setPrimerApellidoPersona(mapaDatosPaciente.get("primerapellido_0").toString());
				ingresoPacienteForm.setSegundoApellidoPersona(mapaDatosPaciente.get("segundoapellido_0").toString());
				ingresoPacienteForm.setDireccion(mapaDatosPaciente.get("direccion_0").toString());
				ingresoPacienteForm.setTelefono(mapaDatosPaciente.get("telefono_0").toString());
				ingresoPacienteForm.setEmail(mapaDatosPaciente.get("email_0").toString());
				ingresoPacienteForm.setFechaNacimiento(mapaDatosPaciente.get("fechanacimiento_0").toString());
				ingresoPacienteForm.setSexo(mapaDatosPaciente.get("codigosexo_0").toString());
				
				ingresoPacienteForm.setEsPacienteCapitado(false);
				ingresoPacienteForm.setUsuarioSinCapitacionVigente(true);
				ingresoPacienteForm.setFichaUsuarioCapitado("");
				
				//********COMO EL USUARIO NO TIENE CAPITACION VIGENTE SE GENERA MENSAJE********************************************
				ElementoApResource alerta = new ElementoApResource("errors.notEspecific");
				alerta.agregarAtributo("Usuario no estÔøΩ vigente en la capitaciÔøΩn");
				ingresoPacienteForm.getMensajesAlerta().add(alerta);
				//*****************************************************************************************************************
				
				//Consulta del parametro paÔøΩs residencia
				String codigoPaisResidencia=mapaDatosPaciente.get("pais_residencia_0")+"";
				String codigoCiudadResidencia=mapaDatosPaciente.get("ciudad_residencia_0")+"";
				String codigoDeptoResidencia=mapaDatosPaciente.get("departamento_residencia_0")+"";
				
				if(!UtilidadTexto.isEmpty(codigoPaisResidencia))
					ingresoPacienteForm.setCodigoPaisResidencia(codigoPaisResidencia+"");
				if(!UtilidadTexto.isEmpty(codigoCiudadResidencia)&&!UtilidadTexto.isEmpty(codigoDeptoResidencia))
				{
					ingresoPacienteForm.setCodigoCiudadResidencia(codigoDeptoResidencia+ConstantesBD.separadorSplit+codigoCiudadResidencia);
					ingresoPacienteForm.setLocalidades(Utilidades.obtenerLocalidades(
							con, 
							ingresoPacienteForm.getCodigoPaisResidencia(), 
							ingresoPacienteForm.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[0], 
							ingresoPacienteForm.getCodigoCiudadResidencia().split(ConstantesBD.separadorSplit)[1]));

				}
				
					
				int barrio=Utilidades.convertirAEntero(mapaDatosPaciente.get("barrio_residencia_0")+"");
				if(barrio>0)
				{
					ingresoPacienteForm.setCodigoBarrio(mapaDatosPaciente.get("barrio_residencia_0")+"");
					ingresoPacienteForm.setNombreBarrio(mapaDatosPaciente.get("descripcion_barrio_0")+"");
				}
				
				
				String codigoLocalidad=mapaDatosPaciente.get("localidad_residencia_0")+"";
				if(!UtilidadTexto.isEmpty(codigoLocalidad))
				{
					ingresoPacienteForm.setCodigoLocalidad(codigoLocalidad);
				}
			}
			
		}
		else
		{
			ingresoPacienteForm.setEsPacienteCapitado(false);
			ingresoPacienteForm.setUsuarioSinCapitacionVigente(false);
			ingresoPacienteForm.setFichaUsuarioCapitado("");
		}
		
		return errores;
		
	}


	/**
	 *AdiciÔøΩn SebastiÔøΩn
	 * MÔøΩtodo usado para hacer un asocio automÔøΩtico en el caso de que se intente ingresar un paciente
	 * que tenga una cuenta de Urgencias, posea una conducta a seguir de 'ObervaciÔøΩn en camas urgencias',
	 * posea un destino de salida 'Hospitalizar' en EvoluciÔøΩn, haya realizado un egreso y desee ingresar una admisiÔøΩn hospitalaria
	 * sin darle asocio de cuenta
	 * @param con
	 * @param paciente
	 * @param usuarioActual
	 * @return
	 */       
	private boolean hacerAsocio(Connection con, PersonaBasica paciente, UsuarioBasico usuarioActual) {
		//		Se hace un asocio automÔøΩtico
		Cuenta cuenta=new Cuenta();
		cuenta.init(System.getProperty("TIPOBD"));
		try
		{
			//logger.info("ÔøΩSE ASOCIA LA CUENTA!");
			if (cuenta.asociarCuenta(con, paciente.getCodigoCuenta(),paciente.getCodigoIngreso(), usuarioActual.getLoginUsuario()) <=0){
				logger.info("ÔøΩNO SE ASOCIA LA CUENTA!");
				return false;
			}
			else{
				// CÔøΩdigo necesario para notificar a todos los observadores que la cuentadel paciente en sesiÔøΩn pudo haber cambiado
				UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),servlet.getServletContext());
				//_________________________________________________________________________-
				logger.info("ÔøΩSE ASOCIA LA CUENTA!");
				return true;
				
			}
		}
		catch (Exception e)
		{
			logger.error("Error haciendo Asocio de Cuentra en IngresoPacienteAction=>"+e);
			return false;
		}
	}

	/**
	 * AdiciÔøΩn SebastiÔøΩn
	 * MÔøΩtodo usado para hacer un egreso automÔøΩtico en el caso de que se intente ingresar un paciente
	 * que tenga una cuenta de Urgencias, posea una conducta a seguir de 'ObervaciÔøΩn en camas urgencias',
	 * posea un destino de salida 'Hospitalizar' en EvoluciÔøΩn y desee ingresar una admisiÔøΩn hospitalaria
	 * sin darle egreso
	 * @param con
	 * @param request
	 * @param paciente
	 * @param usuarioActual
	 * @return
	 */
	private boolean hacerEgreso(Connection con, HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuarioActual) {
		RespuestaValidacion resp1;
		try{
			resp1=UtilidadValidacion.puedoFinalizarEgreso (con, paciente.getCodigoCuenta());
		}
		catch(SQLException e){
			logger.error("Error validando si se puede finalizar el egreso en IngresoPacienteAction=>"+e);
			return false;
		}
		if(resp1.puedoSeguir)
		{
			/*ActionForward validacionFechasObservacion=this.funcionalidadValidarFechasObservacion(con, mapping, egresoForm, request, paciente);
		
			if (validacionFechasObservacion!=null)
			{
				
				return validacionFechasObservacion;
			}*/
			
			//Si no se salio por allÔøΩ, es porque no habÔøΩa ningÔøΩn problema
			
			Egreso egreso=new Egreso();
			egreso.setNumeroCuenta(paciente.getCodigoCuenta());
			String fechaHoraEgreso[]=UtilidadValidacion.obtenerMaximaFechaYHoraEvolucion(con,paciente.getCodigoCuenta());
			egreso.setFechaEgreso(fechaHoraEgreso[0]);
			egreso.setFechaGrabacionEgreso(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			egreso.setHoraEgreso(fechaHoraEgreso[1]);
			egreso.setHoraGrabacionEgreso(UtilidadFecha.getHoraActual());
			egreso.setUsuarioGrabaEgreso(usuarioActual);
			egreso.setNumeroAutorizacion("");
			logger.info("Fecha Egreso=>"+egreso.getFechaEgreso()+" fechaGrabacion=>"+egreso.getFechaGrabacionEgreso());
			//Egreso egreso=new Egreso();
			int entero1;
			try{
				
				//egreso.cargarEgresoGeneral(con, paciente.getCodigoCuenta());
				entero1=egreso.modificarEgresoUsuarioFinalizar(con, "empezar");
			}
			catch(SQLException e){
				logger.error("Error modificando el Egreso del Usuario en IngresoPacienteAction: "+e);
				return false;
			}
			int entero2=0, entero3=0;
			AdmisionUrgencias admisionUrgencias=new AdmisionUrgencias();
			
			admisionUrgencias.init(System.getProperty("TIPOBD"));
			//y actualizar la admision hospitalaria con fecha y hora de observaciÔøΩn
			//de la evolucion - La fecha de la ultima evolucion la tomamos del egreso
			try{
				entero2=admisionUrgencias.actualizarPorOrdenSalidaTransaccional(con, paciente.getCodigoAdmision(), paciente.getAnioAdmision(), egreso.getFechaEgreso(), egreso.getHoraEgreso(), "continuar");
				entero3=admisionUrgencias.actualizarPorEgresoTransaccional(con, paciente.getCodigoAdmision(), paciente.getAnioAdmision(), "finalizar",usuarioActual.getCodigoInstitucionInt());
				//*********OperaciÔøΩn para deshabilitar la cama del paciente********************************************************
				
				//Quitar la cama en el encabezado de paciente
				PersonaBasica pacienteActivo = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				pacienteActivo.setCama("");
				//****************************************************************
			}
			catch(SQLException e){
				logger.error("Error actualizando admisiÔøΩn de urgencias del Usuario en IngresoPacienteAction: "+e);
				return false;
			}
			if (entero1<1||entero2<1||entero3<1)
			{
				return false;
			}
			else{
				return true;
			}
			

		}
		else{
			return false;
		}
	}

	
	/**
	 * AdiciÔøΩn SebastiÔøΩn
	 * MÔøΩtodo usado para remitir automÔøΩticamente a admisiÔøΩn hospitalaria
	 * en el caos de que haya un asocio de cuenta donde la valoraciÔøΩn
	 * fue 'Hospitalizar en Piso'
	 * @param con
	 * @param usuarioActual
	 * @param request
	 * @param mapping
	 * @param ingresoPacienteForm
	 * @param idCuentaAsocio 
	 * @param paciente 
	 * @return
	 */
	private ActionForward remitirAAdmisionHospitalaria(
			Connection con, 
			UsuarioBasico usuarioActual, 
			HttpServletRequest request, 
			ActionMapping mapping, 
			IngresoPacienteForm ingresoPacienteForm,
			String idCuentaAsocio, 
			PersonaBasica paciente,
			String origen,
			int indicadorTipoPaciente) throws IPSException 
	{
		ActionErrors errores = new ActionErrors();
		ArrayList<DtoRequsitosPaciente> arregloAux = new ArrayList<DtoRequsitosPaciente>();
		
		//**********SE REALIZA LA VALIDACION DEL CONSECUTIVO DE INGRESO********************************
		 errores = validacionConsecutivoIngreso(con, usuarioActual, errores);
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
		}
		//*************************************************************************************
		
		///********SE CONSULTA EL SALDO DE LA INTERFAZ*******************************************************************
		//modificacion por anexo 779
		ResultadoDouble resultadoDouble=this.consultaSaldoInterfaz(paciente.getNumeroIdentificacionPersona(),usuarioActual,ingresoPacienteForm);
		if (resultadoDouble !=null && UtilidadCadena.noEsVacio(resultadoDouble.getDescripcion()))
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, resultadoDouble.getDescripcion(), resultadoDouble.getDescripcion(), false);
		//**************************************************************************************************************
		
		//**************************************************************************************************************
		
		//****************SE CARGA LA CUENTA DE URGENCIAS***********************
		Cuenta mundoCuenta = new Cuenta();
		mundoCuenta.cargar(con, idCuentaAsocio);
		
		
		//*****************************CONSULTA DEL NUEVO MONTO DE COBRO*******************************************************************
		MontosCobro montos=new MontosCobro();
		Collection codigosmontos=montos.consultarMontosCobro(con,
				mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo(),
				mundoCuenta.getCuenta().getConvenios()[0].getTipoAfiliado(),
				mundoCuenta.getCuenta().getConvenios()[0].getClasificacionSocioEconomica(),
				ConstantesBD.codigoViaIngresoHospitalizacion);
		//Se verifica que hayan montos de cobro para hospitalizacion
		if(codigosmontos.size()==0)
		{
			errores.add("No hay montos para hospitalizacion",new ActionMessage("errors.paciente.avisoSinMontos","HospitalizaciÔøΩn",mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getNombre()));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
		}
		//**********************************************************************************************************************************
		
		
		//***********SE CARGA LA INFORMACION DE LA CUENTA DE URGENCIAS********************************************************
		ingresoPacienteForm.setCuenta("esAsocio", ConstantesBD.acronimoSi);
		
		/*************************CARGA DE LA PAGINA PRINCIPAL DE LA CUENTA*******************************************************************/
		//1) PreparaciÔøΩn de los datos de la v√≠a de ingreso-------------------------------------------------------------------------------
		ingresoPacienteForm.setCuenta("codigoViaIngreso", ConstantesBD.codigoViaIngresoHospitalizacion+"");
		ingresoPacienteForm.setCodigoViaIngreso(ConstantesBD.codigoViaIngresoHospitalizacion+"");
		//se pregunta si la institucion maneja hospital d√≠a
		ingresoPacienteForm.setCuenta("manejaHospitalDia", ValoresPorDefecto.getEntidadManejaHospitalDia(usuarioActual.getCodigoInstitucionInt())); 
		ingresoPacienteForm.setCuenta("hospitalDia", "");
		
		//solo se permite la seleccion de hospitalizacion
		ingresoPacienteForm.setViasIngreso(new Vector<InfoDatosString>());
		InfoDatosString viaIng = new InfoDatosString(ConstantesBD.codigoViaIngresoHospitalizacion+"",Utilidades.obtenerNombreViaIngreso(con, ConstantesBD.codigoViaIngresoHospitalizacion));
		ingresoPacienteForm.getViasIngreso().add(viaIng);

		
		//*******************************************************************************************************************
		InfoDatosString tipoAsocioInfo = new InfoDatosString();		
		
		if(paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoUrgencias)
			tipoAsocioInfo.setCodigo("Urgencias");			
		else
			tipoAsocioInfo.setCodigo("CirugÔøΩa Ambulatoria");			
		
		//Evalua si el indicador es Hospitalizar en Piso (se toma de Valoracion - Conducta a Seguir) o Hospitalizacion (se toma de Evolucion - Hospitalizacion)
		if((indicadorTipoPaciente == ConstantesBD.codigoDestinoSalidaHospitalizacion  && origen.equals("evolucion"))||
				(indicadorTipoPaciente == ConstantesBD.codigoDestinoSalidaHospitalizar  && origen.equals("evolucion"))||
					(indicadorTipoPaciente == ConstantesBD.codigoConductaSeguirHospitalizarPiso  && origen.equals("valoracion"))||
						(indicadorTipoPaciente == ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial  && origen.equals("evolucion"))||
							(indicadorTipoPaciente == ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial  && origen.equals("valoracion")))
		{						
			tipoAsocioInfo.setNombre("HospitalizaciÔøΩn");		
			
			//Se obtienen los tipos de paciente x la via de ingres
			ingresoPacienteForm.setTiposPaciente(UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, ConstantesBD.codigoViaIngresoHospitalizacion+"",ConstantesBD.tipoPacienteHospitalizado));
		}
		//Evalua si el indicador es Cirugia Ambulatoria (se toma de Valoracion - Conducta a Seguir) o Cirugia Ambulatoria (se toma de Evolucion - Destina Salida)
		else if( (indicadorTipoPaciente == ConstantesBD.codigoDestinoSalidaCirugiaAmbulatoria && origen.equals("evolucion"))
				|| (indicadorTipoPaciente == ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria && origen.equals("valoracion")))
		{
			tipoAsocioInfo.setNombre("CirugÔøΩa Ambulatoria");			
			
			//Se obtienen los tipos de paciente x la via de ingres
			ingresoPacienteForm.setTiposPaciente(UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, ConstantesBD.codigoViaIngresoHospitalizacion+"",ConstantesBD.tipoPacienteCirugiaAmbulatoria));
		}
		else
		{
			tipoAsocioInfo.setNombre("HospitalizaciÔøΩn");			
			
			//Se obtienen los tipos de paciente x la via de ingres
			ingresoPacienteForm.setTiposPaciente(UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, ConstantesBD.codigoViaIngresoHospitalizacion+"",""));
		}
		//*******************************************************************************************************************
		
		ingresoPacienteForm.getTipoAsocio().add(tipoAsocioInfo);
		
		logger.info("INDICADOR DEL TIPO DE PACIENTE >>>>>>>>>>>>>>>>>>>>>>>>"+indicadorTipoPaciente);
		logger.info("ORIGEN DEL ASOCIOOOOOOO>>>>>>>>>>>>>>>>"+origen);
		if((indicadorTipoPaciente == ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial  && origen.equals("evolucion")))
		{
			//logger.info("Entro 111111111");
			ingresoPacienteForm.setAreas(Utilidades.areasAsocioEspeciales(con, usuarioActual.getCodigoCentroAtencion(), Utilidades.convertirAEntero(mundoCuenta.getCuenta().getIdCuenta()), false));
			ingresoPacienteForm.setEvolucion(Utilidades.consultaEvolucionCuentaUrgencias(con, Utilidades.convertirAEntero(mundoCuenta.getCuenta().getIdCuenta())));
			logger.info("EVOLUCION ORDEN DE ICE>>>>>>>>>>>>"+ingresoPacienteForm.getEvolucion());
		}
		else if((indicadorTipoPaciente == ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial  && origen.equals("valoracion")))
		{
			//logger.info("Entro 222222222");
			ingresoPacienteForm.setAreas(Utilidades.areasAsocioEspeciales(con, usuarioActual.getCodigoCentroAtencion(), Utilidades.convertirAEntero(mundoCuenta.getCuenta().getIdCuenta()), true));
			ingresoPacienteForm.setValoracion(Utilidades.consultaValoracionCuentaUrgencias(con, Utilidades.convertirAEntero(mundoCuenta.getCuenta().getIdCuenta())));
			logger.info("VALORACION ORDEN DE ICE>>>>>>>>>>>>"+ingresoPacienteForm.getValoracion());
		}
		else
		{
			//logger.info("Entro 33333333");
			ingresoPacienteForm.setAreas(Utilidades.getCentrosCostoXViaIngresoXCAtencion(con, ConstantesBD.codigoViaIngresoHospitalizacion, usuarioActual.getCodigoCentroAtencion(), usuarioActual.getCodigoInstitucionInt(),""));
		}
		
		
		logger.info("MAPA DE LAS AREAS>>>>>>>>>>>>>>>>"+ingresoPacienteForm.getAreas());
		
		//Consulta de los campos de validacion x via de ingreso
		ViasIngreso mundoViaIngreso = new ViasIngreso();
		HashMap datosViaIng = mundoViaIngreso.consultarViasIngresoEspecifico(con, ConstantesBD.codigoViaIngresoHospitalizacion);
		//A) Indicativo de la verificacion
		ingresoPacienteForm.setCuenta("esViaIngresoVerificacion",UtilidadTexto.getBoolean(datosViaIng.get("verificacion_0")+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		//B) Indicativo de requerido responsable paciente
		ingresoPacienteForm.setCuenta("esRequeridoResponsablePaciente",UtilidadTexto.getBoolean(datosViaIng.get("paciente_0")+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		//NOTA* LA CONSULTA DEL CONVENIO POR DEFECTO SE HACE AL FINAL DEL M√âTODO !!!
		
		
		//2) PreparaciÔøΩn de los datos del convenio ---------------------------------------------------------------------------------------
		Convenio mundoConvenio = new Convenio();
		mundoConvenio.cargarResumen(con, mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo());
		
		ingresoPacienteForm.setCuenta("codigoConvenio", mundoConvenio.getCodigo()+ConstantesBD.separadorSplit+mundoConvenio.getTipoContrato()+ConstantesBD.separadorSplit+(UtilidadTexto.getBoolean(mundoConvenio.getPyp())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo));
		ingresoPacienteForm.setCuenta("nombreConvenio", mundoConvenio.getNombre());
		ingresoPacienteForm.setCuenta("esConvenioPyp", UtilidadTexto.getBoolean(mundoConvenio.getPyp())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("esConvenioCapitado", mundoConvenio.getTipoContrato()==ConstantesBD.codigoTipoContratoCapitado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("esConvenioColsanitas", UtilidadesFacturacion.esConvenioColsanitas(con, mundoConvenio.getCodigo(), usuarioActual.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("esRequiereAutorizacionServicio", UtilidadTexto.getBoolean(mundoConvenio.getRequiere_autorizacion_servicio())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("numeroSolicitudVolante", mundoCuenta.getCuenta().getConvenios()[0].getNumeroSolicitudVolante());
		
		//se asigna el tipo de regimen
		ingresoPacienteForm.setCuenta("codigoTipoRegimen", mundoConvenio.getTipoRegimen());
		ingresoPacienteForm.setCuenta("nombreTipoRegimen", mundoConvenio.getDescripcionTipoRegimen());
		//Tipo de cobertura
		ingresoPacienteForm.setCuenta("codigoTipoCobertura", mundoCuenta.getCuenta().getConvenios()[0].getCodigoTipoCobertura());
		ingresoPacienteForm.setCuenta("nombreTipoCobertura", mundoCuenta.getCuenta().getConvenios()[0].getNombreTipoCobertura());
		
		//se cargan los contratos del convenio
		ingresoPacienteForm.setCuenta("codigoContrato", mundoCuenta.getCuenta().getConvenios()[0].getContrato()+"");
		ingresoPacienteForm.setCuenta("numeroContrato", mundoCuenta.getCuenta().getConvenios()[0].getNumeroContrato());
		
		//Se toma el id de la sub cuenta
		ingresoPacienteForm.setCuenta("idSubCuenta", mundoCuenta.getCuenta().getConvenios()[0].getSubCuenta());
		
		ingresoPacienteForm.setCuenta("isConvenioManejaMontos",mundoConvenio.isConvenioManejaMontoCobro());
		
		//se cargan los requisitos del paciente
		//a. requisitos de ingreso---------------------------------------------------------------------------------
		arregloAux = mundoCuenta.getCuenta().getConvenios()[0].getRequisitosPaciente();
		int numReqIngreso = 0;
		for(int i=0;i<arregloAux.size();i++)
		{
			DtoRequsitosPaciente elemento = (DtoRequsitosPaciente)arregloAux.get(i);
			
			if(elemento.getTipo().equals(ConstantesIntegridadDominio.acronimoIngreso))
			{
				ingresoPacienteForm.setCuenta("codigoReqIngreso_"+numReqIngreso, elemento.getCodigo());
				ingresoPacienteForm.setCuenta("descripcionReqIngreso_"+numReqIngreso, elemento.getDescripcion());
				ingresoPacienteForm.setCuenta("cumplidoReqIngreso_"+numReqIngreso, elemento.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				ingresoPacienteForm.setCuenta("existeReqIngreso_"+numReqIngreso, ConstantesBD.acronimoSi);
				numReqIngreso++;
			}
			
		}
		
		//a.1 Se cargan los requisitos ingreso adicionales de hospitalizaciÔøΩn---------------------------------------------------------------
		ArrayList<HashMap<String, Object>> requisitosIngreso = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
				con,
				mundoConvenio.getCodigo(), 
				ConstantesIntegridadDominio.acronimoIngreso, 
				true, 
				usuarioActual.getCodigoInstitucionInt(),
				ConstantesBD.codigoViaIngresoHospitalizacion );
		for(HashMap<String, Object> elemento:requisitosIngreso)
		{
			boolean existeRequisito = false;
			
			//Se verifica que el requisito no se encuentre repetido
			for(DtoRequsitosPaciente elemReq:arregloAux)
				if(elemReq.getTipo().equals(ConstantesIntegridadDominio.acronimoIngreso)&&
					elemReq.getCodigo()==Integer.parseInt(elemento.get("codigo").toString()))
					existeRequisito = true;	
			
			//Si no existe se agrega
			if(!existeRequisito)
			{
				ingresoPacienteForm.setCuenta("codigoReqIngreso_"+numReqIngreso, elemento.get("codigo"));
				ingresoPacienteForm.setCuenta("descripcionReqIngreso_"+numReqIngreso, elemento.get("descripcion"));
				ingresoPacienteForm.setCuenta("cumplidoReqIngreso_"+numReqIngreso, ConstantesBD.acronimoNo);
				ingresoPacienteForm.setCuenta("existeReqIngreso_"+numReqIngreso, ConstantesBD.acronimoNo);
				numReqIngreso++;
			}
		}
		
		
		ingresoPacienteForm.setCuenta("numReqIngreso", numReqIngreso+"");
			
		//b. requisitos del egreso-----------------------------------------------------------------------------
		int numReqEgreso = 0;
		for(int i=0;i<arregloAux.size();i++)
		{
			DtoRequsitosPaciente elemento = (DtoRequsitosPaciente)arregloAux.get(i);
			
			if(elemento.getTipo().equals(ConstantesIntegridadDominio.acronimoEgreso))
			{
				ingresoPacienteForm.setCuenta("codigoReqEgreso_"+numReqEgreso, elemento.getCodigo());
				ingresoPacienteForm.setCuenta("descripcionReqEgreso_"+numReqEgreso, elemento.getDescripcion());
				ingresoPacienteForm.setCuenta("cumplidoReqEgreso_"+numReqEgreso, elemento.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				numReqEgreso++;
			}
			
		}
		
		//b.1 Se cargan los requisitos egreso adicionales de hospitalizaciÔøΩn---------------------------------------------------------------
		ArrayList<HashMap<String, Object>> requisitosEgreso = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
				con,
				mundoConvenio.getCodigo(), 
				ConstantesIntegridadDominio.acronimoEgreso, 
				true, 
				usuarioActual.getCodigoInstitucionInt(),
				ConstantesBD.codigoViaIngresoHospitalizacion );
		for(HashMap<String, Object> elemento:requisitosEgreso)
		{
			boolean existeRequisito = false;
			
			//Se verifica que el requisito no se encuentre repetido
			for(DtoRequsitosPaciente elemReq:arregloAux)
				if(elemReq.getTipo().equals(ConstantesIntegridadDominio.acronimoEgreso)&&
					elemReq.getCodigo()==Integer.parseInt(elemento.get("codigo").toString()))
					existeRequisito = true;	
			
			//Si no existe se agrega
			if(!existeRequisito)
			{
				ingresoPacienteForm.setCuenta("codigoReqEgreso_"+numReqIngreso, elemento.get("codigo"));
				ingresoPacienteForm.setCuenta("descripcionReqEgreso_"+numReqIngreso, elemento.get("descripcion"));
				ingresoPacienteForm.setCuenta("cumplidoReqEgreso_"+numReqIngreso, ConstantesBD.acronimoNo);
				ingresoPacienteForm.setCuenta("existeReqEgreso_"+numReqIngreso, ConstantesBD.acronimoNo);
				numReqEgreso++;
			}
		}
		
		
		ingresoPacienteForm.setCuenta("numReqEgreso", numReqEgreso+"");
		
		ingresoPacienteForm.setCuenta("tieneRequisitosPaciente", 
			(Integer.parseInt(ingresoPacienteForm.getCuenta("numReqIngreso").toString())>0||Integer.parseInt(ingresoPacienteForm.getCuenta("numReqEgreso").toString())>0)?
				ConstantesBD.acronimoSi:
				ConstantesBD.acronimoNo);
		
		//otras validaciones del convenio
		ingresoPacienteForm.setCuenta("esConvenioSoat", Convenio.esConvenioTipoConventioEventoCatTrans(mundoConvenio.getCodigo())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("manejoComplejidad", mundoConvenio.getManejaComplejidad());
		ingresoPacienteForm.setCuenta("esConvenioPoliza", UtilidadTexto.getBoolean(mundoConvenio.getCheckInfoAdicCuenta())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("requiereCarnet", mundoConvenio.getRequiereNumeroCarnet());
		ingresoPacienteForm.setCuenta("esConvenioVerificacion",mundoConvenio.isRequiereVerificacionDerechos()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		 
		ingresoPacienteForm.setCuenta("codigoNaturaleza", mundoCuenta.getCuenta().getConvenios()[0].getNaturalezaPaciente()+""); 
		ingresoPacienteForm.setCuenta("nombreNaturaleza", mundoCuenta.getCuenta().getConvenios()[0].getDescripcionNaturalezaPaciente());
		ingresoPacienteForm.setCuenta("seccionVariosConvenios", ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("numConvenios", "0");
		ingresoPacienteForm.setVariosConvenios("numRegistros", "0");
		ingresoPacienteForm.setNumConvenios(0);
		ingresoPacienteForm.setCuenta("codigoEstratoSocial", mundoCuenta.getCuenta().getConvenios()[0].getClasificacionSocioEconomica()+"");
		ingresoPacienteForm.setCuenta("nombreEstratoSocial", mundoCuenta.getCuenta().getConvenios()[0].getDescripcionClasificacionSocioEconomica());
		ingresoPacienteForm.setCuenta("codigoTipoAfiliado", mundoCuenta.getCuenta().getConvenios()[0].getTipoAfiliado());
		ingresoPacienteForm.setCuenta("nombreTipoAfiliado", mundoCuenta.getCuenta().getConvenios()[0].getDescripcionTipoAfiliado());
		ingresoPacienteForm.setCuenta("codigoMontoCobro", mundoCuenta.getCuenta().getConvenios()[0].getMontoCobro()+"");
		ingresoPacienteForm.setCuenta("nombreMontoCobro", mundoCuenta.getCuenta().getConvenios()[0].getDescripcionMontoCobro());
		ingresoPacienteForm.setCuenta("valorUtilizadoSoat", mundoCuenta.getCuenta().getConvenios()[0].getValorUtilizadoSoat());
		ingresoPacienteForm.setCuenta("fechaAfiliacion", mundoCuenta.getCuenta().getConvenios()[0].getFechaAfiliacion());
		ingresoPacienteForm.setCuenta("semanasCotizacion", mundoCuenta.getCuenta().getConvenios()[0].getSemanasCotizacion());
		ingresoPacienteForm.setCuenta("mesesCotizacion", mundoCuenta.getCuenta().getConvenios()[0].getMesesCotizacion());
		ingresoPacienteForm.setCuenta("tipoComplejidad", mundoCuenta.getCuenta().getCodigoTipoComplejidad()+"");
		
		logger.info("valores tipo paciente >> "+mundoCuenta.getCuenta().getCodigoTipoPaciente().toString()+indicadorTipoPaciente);
		
		if((indicadorTipoPaciente == ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial  && origen.equals("evolucion"))||
				(indicadorTipoPaciente == ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial  && origen.equals("valoracion")))
		{
			ingresoPacienteForm.setCuenta("codigoTipoPaciente",ConstantesBD.tipoPacienteHospitalizado+"");
			ingresoPacienteForm.setCuenta("modificatipop", "N");
		}
		else
		{
			ingresoPacienteForm.setCuenta("codigoTipoPaciente", mundoCuenta.getCuenta().getCodigoTipoPaciente());
		}
		
		ingresoPacienteForm.setCuenta("numeroCarnet", mundoCuenta.getCuenta().getConvenios()[0].getNroCarnet());
		ingresoPacienteForm.setCuenta("numeroPoliza", mundoCuenta.getCuenta().getConvenios()[0].getNroPoliza());
		ingresoPacienteForm.setCuenta("codigoTipoEvento", mundoCuenta.getCuenta().getCodigoTipoEvento());
		ingresoPacienteForm.setCuenta("codigoArpAfiliado", mundoCuenta.getCuenta().getCodigoConvenioArpAfiliado()+"");
		ingresoPacienteForm.setCuenta("codigoOrigenAdmision", mundoCuenta.getCuenta().getCodigoOrigenAdmision()+"");
		ingresoPacienteForm.setCuenta("autorizacionIngreso", mundoCuenta.getCuenta().getConvenios()[0].getNroAutorizacion());
		ingresoPacienteForm.setCuenta("codigoArea", mundoCuenta.getCuenta().getCodigoArea()+"");
		ingresoPacienteForm.setCuenta("codigoIngreso", mundoCuenta.getCuenta().getIdIngreso());
		ingresoPacienteForm.setCuenta("fechaAdmision", UtilidadFecha.getFechaActual());
		ingresoPacienteForm.setCuenta("horaAdmision", UtilidadFecha.getHoraActual());
		
		//se verifica si el convenio principal tiene informacion de poliza
		if(UtilidadTexto.getBoolean(ingresoPacienteForm.getCuenta("esConvenioPoliza").toString()))
		{
			ingresoPacienteForm.setCuenta("apellidosPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getApellidos());
			ingresoPacienteForm.setCuenta("nombresPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getNombres());
			ingresoPacienteForm.setCuenta("tipoIdPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getCodigoTipoIdentificacion());
			ingresoPacienteForm.setCuenta("descripcionTipoIdPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getDescripcionTipoIdentificacion());
			ingresoPacienteForm.setCuenta("numeroIdPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getNumeroIdentificacion());
			ingresoPacienteForm.setCuenta("direccionPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getDireccion());
			ingresoPacienteForm.setCuenta("telefonoPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getTelefono());
			ingresoPacienteForm.setCuenta("numDatosPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getSizeInformacionPoliza()+"");
			double saldoPoliza = 0;
			for(int i=0;i<mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getSizeInformacionPoliza();i++)
			{
				ingresoPacienteForm.setCuenta("fechaPoliza_"+i, mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getFechaInformacionPoliza(i));
				ingresoPacienteForm.setCuenta("autorizacionPoliza_"+i, mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getAutorizacionInformacionPoliza(i));
				ingresoPacienteForm.setCuenta("valorPoliza_"+i, mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getValorInformacionPoliza(i));
				saldoPoliza += Double.parseDouble(mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getValorInformacionPoliza(i));
			}
			ingresoPacienteForm.setCuenta("saldoPoliza",UtilidadTexto.formatearValores(saldoPoliza));
		}
		else
			ingresoPacienteForm.setCuenta("numDatosPoliza", "0");
		
		
		/**************************CARGA DE LOS DATOS DE CADA CONVENIO******************************************/
		//si hay mas convenios adicionales se carga su informacion en la estructura de VariosConvenios
		if(mundoCuenta.getCuenta().getConvenios().length>1)
		{
			int numConvenios = 0;
			
			for(int i=1;i<mundoCuenta.getCuenta().getConvenios().length;i++)
			{
				mundoConvenio = new Convenio();
				mundoConvenio.cargarResumen(con, mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo());
				ingresoPacienteForm.setVariosConvenios("existeConvenio_"+numConvenios, ConstantesBD.acronimoSi);
				ingresoPacienteForm.setVariosConvenios("idSubCuenta_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getSubCuenta());
				ingresoPacienteForm.setVariosConvenios("codigoConvenio_"+numConvenios, mundoConvenio.getCodigo()+"");
				ingresoPacienteForm.setVariosConvenios("nombreConvenio_"+numConvenios, mundoConvenio.getNombre());
				ingresoPacienteForm.setVariosConvenios("esConvenioPyp_"+numConvenios, UtilidadTexto.getBoolean(mundoConvenio.getPyp())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				ingresoPacienteForm.setVariosConvenios("esConvenioCapitado_"+numConvenios, mundoConvenio.getTipoContrato()==ConstantesBD.codigoTipoContratoCapitado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				ingresoPacienteForm.setVariosConvenios("codigoContrato_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getContrato()+"");
				ingresoPacienteForm.setVariosConvenios("numeroContrato_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getNumeroContrato());
				ingresoPacienteForm.setVariosConvenios("codigoViaIngreso_"+numConvenios, ConstantesBD.codigoViaIngresoHospitalizacion);
				ingresoPacienteForm.setVariosConvenios("esConvenioPyp_"+numConvenios, UtilidadTexto.getBoolean(mundoConvenio.getPyp())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				ingresoPacienteForm.setVariosConvenios("esConvenioCapitado_"+numConvenios, mundoConvenio.getTipoContrato()==ConstantesBD.codigoTipoContratoCapitado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				ingresoPacienteForm.setVariosConvenios("esConvenioPoliza_"+numConvenios, UtilidadTexto.getBoolean(mundoConvenio.getCheckInfoAdicCuenta())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				ingresoPacienteForm.setVariosConvenios("esConvenioSoat_"+numConvenios, Convenio.esConvenioTipoConventioEventoCatTrans(mundoConvenio.getCodigo())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				ingresoPacienteForm.setVariosConvenios("codigoTipoRegimen_"+numConvenios, mundoConvenio.getTipoRegimen());
				ingresoPacienteForm.setVariosConvenios("nombreTipoRegimen_"+numConvenios, mundoConvenio.getDescripcionTipoRegimen());
				ingresoPacienteForm.setVariosConvenios("codigoTipoCobertura_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getCodigoTipoCobertura());
				ingresoPacienteForm.setVariosConvenios("nombreTipoCobertura_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getNombreTipoCobertura());
				ingresoPacienteForm.setVariosConvenios("codigoNaturaleza_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getNaturalezaPaciente()+"");
				ingresoPacienteForm.setVariosConvenios("nombreNaturaleza_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getDescripcionNaturalezaPaciente());
				ingresoPacienteForm.setVariosConvenios("valorUtilizadoSoat_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getValorUtilizadoSoat());
				ingresoPacienteForm.setVariosConvenios("fechaAfiliacion_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getFechaAfiliacion());
				ingresoPacienteForm.setVariosConvenios("semanasCotizacion_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getSemanasCotizacion());
				ingresoPacienteForm.setVariosConvenios("mesesCotizacion_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getMesesCotizacion());
				ingresoPacienteForm.setVariosConvenios("requiereCarnet_"+numConvenios, mundoConvenio.getRequiereNumeroCarnet());				
				ingresoPacienteForm.setVariosConvenios("manejoComplejidad_"+numConvenios, mundoConvenio.getManejaComplejidad());
				ingresoPacienteForm.setVariosConvenios("codigoEstratoSocial_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getClasificacionSocioEconomica());
				ingresoPacienteForm.setVariosConvenios("nombreEstratoSocial_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getDescripcionClasificacionSocioEconomica());
				ingresoPacienteForm.setVariosConvenios("codigoTipoAfiliado_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getTipoAfiliado());
				ingresoPacienteForm.setVariosConvenios("nombreTipoAfiliado_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getDescripcionTipoAfiliado());
				ingresoPacienteForm.setVariosConvenios("codigoMontoCobro_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getMontoCobro()+"");
				ingresoPacienteForm.setVariosConvenios("nombreMontoCobro_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getDescripcionMontoCobro());
				ingresoPacienteForm.setVariosConvenios("numeroCarnet_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getNroCarnet());
				ingresoPacienteForm.setVariosConvenios("numeroPoliza_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getNroPoliza());
				ingresoPacienteForm.setVariosConvenios("autorizacionIngreso_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getNroAutorizacion());
				ingresoPacienteForm.setVariosConvenios("esConvenioColsanitas_"+numConvenios, UtilidadesFacturacion.esConvenioColsanitas(con, mundoConvenio.getCodigo(), usuarioActual.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				ingresoPacienteForm.setVariosConvenios("esRequiereAutorizacionServicio_"+numConvenios, UtilidadTexto.getBoolean(mundoConvenio.getRequiere_autorizacion_servicio())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				
				ingresoPacienteForm.setVariosConvenios("numeroSolicitudVolante_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getNumeroSolicitudVolante());
				
				ingresoPacienteForm.setVariosConvenios("isConvenioManejaMontos",mundoConvenio.isConvenioManejaMontoCobro());
				
				//Informacion de la poliza
				if(UtilidadTexto.getBoolean(ingresoPacienteForm.getVariosConvenios("esConvenioPoliza_"+numConvenios).toString()))
				{
					ingresoPacienteForm.setVariosConvenios("apellidosPoliza_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getApellidos());
					ingresoPacienteForm.setVariosConvenios("nombresPoliza_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getNombres());
					ingresoPacienteForm.setVariosConvenios("tipoIdPoliza_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getCodigoTipoIdentificacion());
					ingresoPacienteForm.setVariosConvenios("descripcionTipoIdPoliza_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getDescripcionTipoIdentificacion());
					ingresoPacienteForm.setVariosConvenios("numeroIdPoliza_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getNumeroIdentificacion());
					ingresoPacienteForm.setVariosConvenios("direccionPoliza_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getDireccion());
					ingresoPacienteForm.setVariosConvenios("telefonoPoliza_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getTelefono());
					
					ingresoPacienteForm.setVariosConvenios("numDatosPoliza_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getSizeInformacionPoliza()+"");
					
					double saldoPoliza = 0;
					for(int j=0;j<mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getSizeInformacionPoliza();j++)
					{
						
						ingresoPacienteForm.setVariosConvenios("fechaPoliza_"+numConvenios+"_"+j, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getFechaInformacionPoliza(j));
						ingresoPacienteForm.setVariosConvenios("autorizacionPoliza_"+numConvenios+"_"+j, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getAutorizacionInformacionPoliza(j));
						ingresoPacienteForm.setVariosConvenios("valorPoliza_"+numConvenios+"_"+j, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getValorInformacionPoliza(j));
						saldoPoliza += Double.parseDouble(mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getValorInformacionPoliza(j));
					}
					ingresoPacienteForm.setVariosConvenios("saldoPoliza_"+numConvenios,UtilidadTexto.formatearValores(saldoPoliza));
					
				}
				
				//inicializacion de los campos de la verifiacion de derechos
				ingresoPacienteForm.setVariosConvenios("tieneVerificacionDerechos_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].isSubCuentaVerificacionDerechos()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				ingresoPacienteForm.setVariosConvenios("ingresoVerificacionDerechos_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].isSubCuentaVerificacionDerechos()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				if(mundoCuenta.getCuenta().getConvenios()[i].isSubCuentaVerificacionDerechos())
				{
					ingresoPacienteForm.setVariosConvenios("codigoEstado_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getCodigoEstado());
					ingresoPacienteForm.setVariosConvenios("nombreEstado_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getDescripcionEstado());
					ingresoPacienteForm.setVariosConvenios("codigoTipo_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getCodigoTipo());
					ingresoPacienteForm.setVariosConvenios("nombreTipo_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getDescripcionTipo());
					ingresoPacienteForm.setVariosConvenios("numero_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getNumero());
					ingresoPacienteForm.setVariosConvenios("personaSolicita_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getPersonaSolicita());
					ingresoPacienteForm.setVariosConvenios("fechaSolicitud_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getFechaSolicitud());
					ingresoPacienteForm.setVariosConvenios("horaSolicitud_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getHoraSolicitud());
					ingresoPacienteForm.setVariosConvenios("personaContactada_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getPersonaContactada());
					ingresoPacienteForm.setVariosConvenios("fechaVerificacion_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getFechaVerificacion());
					ingresoPacienteForm.setVariosConvenios("horaVerificacion_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getHoraVerificacion());
					ingresoPacienteForm.setVariosConvenios("porcentajeCobertura_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getPorcentajeCobertura());
					ingresoPacienteForm.setVariosConvenios("cuotaVerificacion_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getCuotaVerificacion());
					ingresoPacienteForm.setVariosConvenios("observaciones_"+numConvenios, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getObservaciones());
				}
				else
				{
					ingresoPacienteForm.setVariosConvenios("personaSolicita_"+numConvenios, usuarioActual.getNombreUsuario());
					ingresoPacienteForm.setVariosConvenios("fechaSolicitud_"+numConvenios, UtilidadFecha.getFechaActual(con));
					ingresoPacienteForm.setVariosConvenios("horaSolicitud_"+numConvenios, UtilidadFecha.getHoraActual(con));
					ingresoPacienteForm.setVariosConvenios("fechaVerificacion_"+numConvenios, UtilidadFecha.getFechaActual(con));
					ingresoPacienteForm.setVariosConvenios("horaVerificacion_"+numConvenios, UtilidadFecha.getHoraActual(con));
				}
				
				//se cargan los requisitos del paciente
				//a. requisitos de ingreso
				arregloAux = mundoCuenta.getCuenta().getConvenios()[i].getRequisitosPaciente();
				numReqIngreso = 0;
				for(int j=0;j<arregloAux.size();j++)
				{
					DtoRequsitosPaciente elemento = (DtoRequsitosPaciente)arregloAux.get(j);
					
					if(elemento.getTipo().equals(ConstantesIntegridadDominio.acronimoIngreso))
					{
						ingresoPacienteForm.setVariosConvenios("codigoReqIngreso_"+numConvenios+"_"+numReqIngreso, elemento.getCodigo());
						ingresoPacienteForm.setVariosConvenios("descripcionReqIngreso_"+numConvenios+"_"+numReqIngreso, elemento.getDescripcion());
						ingresoPacienteForm.setVariosConvenios("cumplidoReqIngreso_"+numConvenios+"_"+numReqIngreso, elemento.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
						ingresoPacienteForm.setVariosConvenios("existeReqIngreso_"+numConvenios+"_"+numReqIngreso, ConstantesBD.acronimoSi);
						numReqIngreso++;
					}
					
				}
				//a.1 Se cargan requisitos ingreso adicionales de hospitalizacion
				requisitosIngreso.clear();
				requisitosIngreso = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
						con,
						mundoConvenio.getCodigo(), 
						ConstantesIntegridadDominio.acronimoIngreso, 
						true, 
						usuarioActual.getCodigoInstitucionInt(),
						ConstantesBD.codigoViaIngresoHospitalizacion );
				for(HashMap<String, Object> elemento:requisitosIngreso)
				{
					boolean existeRequisito = false;
					
					//Se verifica que el requisito no se encuentre repetido
					for(DtoRequsitosPaciente elemReq:arregloAux)
						if(elemReq.getTipo().equals(ConstantesIntegridadDominio.acronimoIngreso)&&
							elemReq.getCodigo()==Integer.parseInt(elemento.get("codigo").toString()))
							existeRequisito = true;	
					
					//Si no existe se agrega
					if(!existeRequisito)
					{
						ingresoPacienteForm.setVariosConvenios("codigoReqIngreso_"+numConvenios+"_"+numReqIngreso, elemento.get("codigo"));
						ingresoPacienteForm.setVariosConvenios("descripcionReqIngreso_"+numConvenios+"_"+numReqIngreso, elemento.get("descripcion"));
						ingresoPacienteForm.setVariosConvenios("cumplidoReqIngreso_"+numConvenios+"_"+numReqIngreso, ConstantesBD.acronimoNo);
						ingresoPacienteForm.setVariosConvenios("existeReqIngreso_"+numConvenios+"_"+numReqIngreso, ConstantesBD.acronimoNo);
						numReqIngreso++;
					}
				}
				
				ingresoPacienteForm.setVariosConvenios("numReqIngreso_"+numConvenios, numReqIngreso+"");
					
				//b. requisitos del egreso
				numReqEgreso = 0;
				for(int j=0;j<arregloAux.size();j++)
				{
					DtoRequsitosPaciente elemento = (DtoRequsitosPaciente)arregloAux.get(j);
					
					if(elemento.getTipo().equals(ConstantesIntegridadDominio.acronimoEgreso))
					{
						ingresoPacienteForm.setVariosConvenios("codigoReqEgreso_"+numConvenios+"_"+numReqEgreso, elemento.getCodigo());
						ingresoPacienteForm.setVariosConvenios("descripcionReqEgreso_"+numConvenios+"_"+numReqEgreso, elemento.getDescripcion());
						ingresoPacienteForm.setVariosConvenios("cumplidoReqEgreso_"+numConvenios+"_"+numReqEgreso, elemento.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
						ingresoPacienteForm.setVariosConvenios("existeReqEgreso_"+numConvenios+"_"+numReqIngreso, ConstantesBD.acronimoSi);
						numReqEgreso++;
					}
					
				}
				
				//b.1 Se cargan requisitos ingreso adicionales de hospitalizacion
				requisitosEgreso.clear();
				requisitosEgreso = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
						con,
						mundoConvenio.getCodigo(), 
						ConstantesIntegridadDominio.acronimoEgreso, 
						true, 
						usuarioActual.getCodigoInstitucionInt(),
						ConstantesBD.codigoViaIngresoHospitalizacion );
				for(HashMap<String, Object> elemento:requisitosEgreso)
				{
					boolean existeRequisito = false;
					
					//Se verifica que el requisito no se encuentre repetido
					for(DtoRequsitosPaciente elemReq:arregloAux)
						if(elemReq.getTipo().equals(ConstantesIntegridadDominio.acronimoEgreso)&&
							elemReq.getCodigo()==Integer.parseInt(elemento.get("codigo").toString()))
							existeRequisito = true;	
					
					//Si no existe se agrega
					if(!existeRequisito)
					{
						ingresoPacienteForm.setVariosConvenios("codigoReqEgreso_"+numConvenios+"_"+numReqIngreso, elemento.get("codigo"));
						ingresoPacienteForm.setVariosConvenios("descripcionReqEgreso_"+numConvenios+"_"+numReqIngreso, elemento.get("descripcion"));
						ingresoPacienteForm.setVariosConvenios("cumplidoReqEgreso_"+numConvenios+"_"+numReqIngreso, ConstantesBD.acronimoNo);
						ingresoPacienteForm.setVariosConvenios("existeReqEgreso_"+numConvenios+"_"+numReqIngreso, ConstantesBD.acronimoNo);
						numReqIngreso++;
					}
				}
				
				ingresoPacienteForm.setVariosConvenios("numReqEgreso_"+numConvenios, numReqEgreso+"");
				
				ingresoPacienteForm.setVariosConvenios("tieneRequisitosPaciente_"+numConvenios, 
					(Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numReqIngreso_"+numConvenios).toString())>0||Integer.parseInt(ingresoPacienteForm.getVariosConvenios("numReqEgreso_"+numConvenios).toString())>0)?
						ConstantesBD.acronimoSi:
						ConstantesBD.acronimoNo);
				
				numConvenios++;
			}
			
			
			ingresoPacienteForm.setCuenta("numConvenios", numConvenios+"");
			ingresoPacienteForm.setVariosConvenios("numRegistros", numConvenios+"");
			ingresoPacienteForm.setNumConvenios(numConvenios);
		}
		
		//Se verifica si se maneja complejidad
		ingresoPacienteForm.setCuenta("indicadorManejaComplejidad", validacionManejoComplejidad(ingresoPacienteForm));
		
		
		/*************************SE AGREGA EL CONVENIO POR DEFECTO********************************************************/
		ingresoPacienteForm.setCuenta("codigoConvenioDefault",datosViaIng.get("convenio_0").toString());
		if(this.agregaConvenioDefault(con,ingresoPacienteForm,datosViaIng.get("convenio_0").toString(),usuarioActual,paciente))
		{
			ingresoPacienteForm.setCuenta("seccionVariosConvenios", ConstantesBD.acronimoSi);
			ingresoPacienteForm.setVariosConvenios("prioridadDefault", obtenerPrioridadConvenioDefault(ingresoPacienteForm));
		}
		
		/**************************CARGA DE LOS DATOS DE VERIFICACION DE DERECHOS******************************************/
		//inicializacion de los campos de la verifiacion de derechos
		ingresoPacienteForm.setCuenta("tieneVerificacionDerechos", mundoCuenta.getCuenta().getConvenios()[0].isSubCuentaVerificacionDerechos()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		ingresoPacienteForm.setCuenta("ingresoVerificacionDerechos", mundoCuenta.getCuenta().getConvenios()[0].isSubCuentaVerificacionDerechos()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		if(mundoCuenta.getCuenta().getConvenios()[0].isSubCuentaVerificacionDerechos())
		{
			ingresoPacienteForm.setVerificacion("codigoEstado", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getCodigoEstado());
			ingresoPacienteForm.setVerificacion("nombreEstado", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getDescripcionEstado());
			ingresoPacienteForm.setVerificacion("codigoTipo", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getCodigoTipo());
			ingresoPacienteForm.setVerificacion("nombreTipo", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getDescripcionTipo());
			ingresoPacienteForm.setVerificacion("numero", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getNumero());
			ingresoPacienteForm.setVerificacion("personaSolicita", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getPersonaSolicita());
			ingresoPacienteForm.setVerificacion("fechaSolicitud", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getFechaSolicitud());
			ingresoPacienteForm.setVerificacion("horaSolicitud", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getHoraSolicitud());
			ingresoPacienteForm.setVerificacion("personaContactada", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getPersonaContactada());
			ingresoPacienteForm.setVerificacion("fechaVerificacion", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getFechaVerificacion());
			ingresoPacienteForm.setVerificacion("horaVerificacion", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getHoraVerificacion());
			ingresoPacienteForm.setVerificacion("porcentajeCobertura", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getPorcentajeCobertura());
			ingresoPacienteForm.setVerificacion("cuotaVerificacion", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getCuotaVerificacion());
			ingresoPacienteForm.setVerificacion("observaciones", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getObservaciones());
		}
		else
		{
			ingresoPacienteForm.setVerificacion("personaSolicita", usuarioActual.getNombreUsuario());
			ingresoPacienteForm.setVerificacion("fechaSolicitud", UtilidadFecha.getFechaActual());
			ingresoPacienteForm.setVerificacion("horaSolicitud", UtilidadFecha.getHoraActual());
			ingresoPacienteForm.setVerificacion("fechaVerificacion", UtilidadFecha.getFechaActual());
			ingresoPacienteForm.setVerificacion("horaVerificacion", UtilidadFecha.getHoraActual());
		}
		ingresoPacienteForm.setCuenta("seccionVerificacionDerechos", ConstantesBD.acronimoNo);
		
		//************SE CONSULTAN LOS TIPOS DE IDENTIFICACION (SOLO APLICA PARA POLIZA)**************************************
		ingresoPacienteForm.setTiposIdentificacion(Utilidades.obtenerTiposIdentificacion(con, "ingresoPoliza", usuarioActual.getCodigoInstitucionInt()));
		//*********************************************************************************************************************
		
		
		//*************SE CONSULTAN LOS TIPOS DE COMPLEJIDAD ****************************************************************
		ingresoPacienteForm.setTiposComplejidad(Utilidades.obtenerTiposComplejidad(con));
		//********************************************************************************************************************
		
		//****************SE CONSULTA LOS CONVENIOS ARP********************************************************************************
		ingresoPacienteForm.setConveniosArp(UtilidadesFacturacion.cargarConveniosXClasificacion(con, ConstantesBDFacturacion.codigoClasTipoConvenioARP));
		//*********************************************************************************************************************
		//**************SE CONSULTA LOS ORIGENES DE LA ADMISION***************************************************************
		ingresoPacienteForm.setOrigenesAdmisiones(UtilidadesManejoPaciente.obtenerOrigenesAdmision(con));
		//*******************************************************************************************
		
		//**************SE CONSULTAN LAS CONVENIOS*****************************************************************
		ingresoPacienteForm.setConvenios(Utilidades.obtenerConvenios(con, "", ConstantesBD.codigoTipoContratoEvento+"", true, "", true));
		//******************************************************************************************************************
		
		/*******************************************************************************************************
		 * Modificacion por tarea 82488
		 */
		
			//se cargan los estratos sociales
			ingresoPacienteForm.setEstratosSociales(
					UtilidadesFacturacion.cargarEstratosSociales(con, usuarioActual.getCodigoInstitucionInt(),
							ConstantesBD.acronimoSi, paciente.getCodigoTipoRegimen()+"",
							paciente.getCodigoConvenio(),
							Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()),
							Utilidades.capturarFechaBD()));
			ingresoPacienteForm.setTiposAfiliado(
					UtilidadesFacturacion.cargarTiposAfiliado(con, usuarioActual.getCodigoInstitucionInt(),
							ConstantesBD.acronimoSi, paciente.getCodigoConvenio(),
							Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso()),
							Utilidades.capturarFechaBD()));
			ingresoPacienteForm.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPaciente(con,paciente.getCodigoTipoRegimen()+"",paciente.getCodigoConvenio(),Utilidades.convertirAEntero(ingresoPacienteForm.getCodigoViaIngreso())));
			//se cargan los montos de cobro
			ingresoPacienteForm.setMontosCobro(UtilidadesFacturacion.obtenerMontosCobroXConvenio(con, paciente.getCodigoConvenio()+"",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
		
		/*
		 * 
		 ******************************************************************************************************/
	
		//**********SE CONSULTA/POSTULA INFORMACION DEL RESPONSABLE DEL PACIENTE************************************
		ingresoPacienteForm.setCriterioBarrio("");
		ingresoPacienteForm.setCuenta("seccionResponsablePaciente", ConstantesBD.acronimoNo);
		ingresoPacienteForm.setPaises(Utilidades.obtenerPaises(con));
		ingresoPacienteForm.setTiposIdResponsable(Utilidades.obtenerTiposIdentificacion(con, "ingresoPaciente", usuarioActual.getCodigoInstitucionInt()));
		
		//Se verifica si la cuenta de urgencias ten√≠a un responsable paciente existente para postularlo
		if(!mundoCuenta.getCuenta().getResponsablePaciente().getCodigo().equals(""))
		{
			ingresoPacienteForm.setResponsable("codigo", mundoCuenta.getCuenta().getResponsablePaciente().getCodigo());
			ingresoPacienteForm.setResponsable("codigoTipoIdentificacion", mundoCuenta.getCuenta().getResponsablePaciente().getTipoIdentificacion());
			ingresoPacienteForm.setResponsable("numeroIdentificacion", mundoCuenta.getCuenta().getResponsablePaciente().getNumeroIdentificacion());
			ingresoPacienteForm.setResponsable("existe", ConstantesBD.acronimoSi);
			ingresoPacienteForm.setResponsable("editable", ConstantesBD.acronimoSi);
			ingresoPacienteForm.setResponsable("paisExpedicion", mundoCuenta.getCuenta().getResponsablePaciente().getCodigoPaisExpedicion());
			ingresoPacienteForm.setResponsable("ciudadExpedicion", mundoCuenta.getCuenta().getResponsablePaciente().getCodigoDeptoExpedicion()+ConstantesBD.separadorSplit+mundoCuenta.getCuenta().getResponsablePaciente().getCodigoCiudadExpedicion());
			ingresoPacienteForm.setResponsable("primerApellido", mundoCuenta.getCuenta().getResponsablePaciente().getPrimerApellido());
			ingresoPacienteForm.setResponsable("segundoApellido", mundoCuenta.getCuenta().getResponsablePaciente().getSegundoApellido());
			ingresoPacienteForm.setResponsable("primerNombre", mundoCuenta.getCuenta().getResponsablePaciente().getPrimerNombre());
			ingresoPacienteForm.setResponsable("segundoNombre", mundoCuenta.getCuenta().getResponsablePaciente().getSegundoNombre());
			ingresoPacienteForm.setResponsable("direccion", mundoCuenta.getCuenta().getResponsablePaciente().getDireccion());
			ingresoPacienteForm.setResponsable("pais", mundoCuenta.getCuenta().getResponsablePaciente().getCodigoPais());
			ingresoPacienteForm.setResponsable("ciudad", mundoCuenta.getCuenta().getResponsablePaciente().getCodigoDepto()+ConstantesBD.separadorSplit+mundoCuenta.getCuenta().getResponsablePaciente().getCodigoCiudad());
			ingresoPacienteForm.setResponsable("codigoBarrio", mundoCuenta.getCuenta().getResponsablePaciente().getCodigoBarrio());
			ingresoPacienteForm.setResponsable("nombreBarrio", mundoCuenta.getCuenta().getResponsablePaciente().getDescripcionBarrio());
			ingresoPacienteForm.setResponsable("telefono", mundoCuenta.getCuenta().getResponsablePaciente().getTelefono());
			ingresoPacienteForm.setResponsable("fechaNacimiento", mundoCuenta.getCuenta().getResponsablePaciente().getFechaNacimiento());
			ingresoPacienteForm.setResponsable("relacionPaciente", mundoCuenta.getCuenta().getResponsablePaciente().getRelacionPaciente());
			
			//Se cargan las ciudades del pasi de expedicion
			if(!mundoCuenta.getCuenta().getResponsablePaciente().getCodigoPaisExpedicion().equals(""))
				ingresoPacienteForm.setCiudadesExp(Utilidades.obtenerCiudadesXPais(con, mundoCuenta.getCuenta().getResponsablePaciente().getCodigoPaisExpedicion()));
			
			
			//Se cargan las ciudades del pais de residencia
			if(!mundoCuenta.getCuenta().getResponsablePaciente().getCodigoPais().equals(""))
				ingresoPacienteForm.setCiudades(Utilidades.obtenerCiudadesXPais(con, mundoCuenta.getCuenta().getResponsablePaciente().getCodigoPais()));
			
		}
		else
		{
		
			ingresoPacienteForm.setResponsable("editable", ConstantesBD.acronimoNo);
			ingresoPacienteForm.setResponsable("existe", ConstantesBD.acronimoNo);
			ingresoPacienteForm.setCiudades(new ArrayList<HashMap<String,Object>>());
			ingresoPacienteForm.setCiudadesExp(new ArrayList<HashMap<String,Object>>());
			
			ParametrizacionInstitucion mundoInstitucion = new ParametrizacionInstitucion();
			mundoInstitucion.cargar(con, usuarioActual.getCodigoInstitucionInt());
			logger.info("PAIS DE INSTITUCION=> "+mundoInstitucion.getPais().getCodigo());
			if(!mundoInstitucion.getPais().getCodigo().equals(""))
			{
				ingresoPacienteForm.setResponsable("paisExpedicion", mundoInstitucion.getPais().getCodigo());
				ingresoPacienteForm.setResponsable("ciudadExpedicion", mundoInstitucion.getDepartamento().getCodigo()+ConstantesBD.separadorSplit+mundoInstitucion.getCiudad().getCodigo());
				ingresoPacienteForm.setResponsable("pais", mundoInstitucion.getPais().getCodigo());
				ingresoPacienteForm.setResponsable("ciudad", mundoInstitucion.getDepartamento().getCodigo()+ConstantesBD.separadorSplit+mundoInstitucion.getCiudad().getCodigo());
				ingresoPacienteForm.setCiudades(Utilidades.obtenerCiudadesXPais(con, mundoInstitucion.getPais().getCodigo()));
				ingresoPacienteForm.setCiudadesExp(Utilidades.obtenerCiudadesXPais(con, mundoInstitucion.getPais().getCodigo()));
			}
		}
		//**********************************************************************************************
		
		//***********VALIDACIONES SOLICITUDES MEDICAMENTOS PENDIENTES DE DESPACHAR*************************
		ingresoPacienteForm.setCuenta("existenMedicamentosXDespachar", UtilidadesOrdenesMedicas.existenSolicitudesMedicamentosPendientesDespachar(con, Integer.parseInt(idCuentaAsocio)));
		//***************************************************************************************************
		
		//******************MENSAJES DE ALERTA*****************************************
		ingresoPacienteForm.setMensajesAlerta(new ArrayList<ElementoApResource>());
		//1) Por defecto se pone el mensaje de asocio
		ElementoApResource elemento01 = new ElementoApResource("mensaje.cuenta.asociocuenta");
		elemento01.agregarAtributo(((InfoDatosString)ingresoPacienteForm.getTipoAsocio().get(0)).getCodigo()+"");
		elemento01.agregarAtributo(((InfoDatosString)ingresoPacienteForm.getTipoAsocio().get(0)).getNombre()+"");
		ingresoPacienteForm.getMensajesAlerta().add(elemento01);
		
		//2) Mensajes validacion solicitudes autorizacion
		//Se cosnultan los convenios del ingreso
		ArrayList<DtoAutorizacion> listadoAutorizaciones = UtilidadesManejoPaciente.obtenerConveniosIngresoAutorizacionesAdmision(con, Integer.parseInt(ingresoPacienteForm.getCuenta("codigoIngreso").toString()),Integer.parseInt(idCuentaAsocio),true);
		String conveniosSinRespuesta = "";
		String conveniosConRespuesta = "";
		boolean conveniosRequierenAutorizacion = false;
		
		for(DtoAutorizacion autorizacion:listadoAutorizaciones)
		{
			//Se verifica si hay autorizaciones ya registradas pero no se han respondido
			if(autorizacion.isTieneAutorizacionRegistrada()&&
				(autorizacion.getDetalle().get(0).getEstadoSolDetAuto().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado)||
				autorizacion.getDetalle().get(0).getEstadoSolDetAuto().equals(ConstantesIntegridadDominio.acronimoEstadoNegado)||
				autorizacion.getDetalle().get(0).getEstadoSolDetAuto().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado)||
				autorizacion.getDetalle().get(0).getEstadoSolDetAuto().equals("")))
			{
				conveniosSinRespuesta += (conveniosSinRespuesta.equals("")?"":", ") + autorizacion.getNombreConvenio();
			}
			//Se verifica si hay autoriszaciones que ya tienen respuesta
			if(autorizacion.isTieneAutorizacionRegistrada()&&autorizacion.getDetalle().get(0).getEstadoSolDetAuto().equals(ConstantesIntegridadDominio.acronimoAutorizado))
			{
				conveniosConRespuesta += (conveniosConRespuesta.equals("")?"":", ") + autorizacion.getNombreConvenio();
			}
			//Se verifica si un convenio no tiene autorizacion registrada y requiere
			if(autorizacion.isRequiereAutorizacionConvenio()&&!autorizacion.isTieneAutorizacionRegistrada())
			{
				conveniosRequierenAutorizacion = true;
			}
			
		}
		
		if(!conveniosSinRespuesta.equals(""))
		{
			ElementoApResource elemento02 = new ElementoApResource("errors.notEspecific");
			elemento02.agregarAtributo("El ingreso tiene solicitudes de autorizaciÔøΩn de admisiÔøΩn para los convenios: "+conveniosSinRespuesta+" pero aÔøΩn NO cuentan con respuesta de autorizaciÔøΩn.");
			ingresoPacienteForm.getMensajesAlerta().add(elemento02);
		}
		if(!conveniosConRespuesta.equals(""))
		{
			ElementoApResource elemento02 = new ElementoApResource("errors.notEspecific");
			elemento02.agregarAtributo("El ingreso cuenta con autorizaciÔøΩn de admisiÔøΩn para los convenios: "+conveniosConRespuesta+".");
			ingresoPacienteForm.getMensajesAlerta().add(elemento02);
		}
		if(conveniosSinRespuesta.equals("")&&conveniosConRespuesta.equals("")&&conveniosRequierenAutorizacion)
		{
			ElementoApResource elemento02 = new ElementoApResource("errors.notEspecific");
			elemento02.agregarAtributo("El ingreso NO cuenta con solicitudes de autorizaciÔøΩn de admisiÔøΩn.");
			ingresoPacienteForm.getMensajesAlerta().add(elemento02);
		}
		
		//*****************************************************************************
		
		//se cierra la conexiÔøΩn
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingresarCuenta");
		
		
	}



	/**
	 * M√©todo que realiza la consulta del saldo de un paciente si el parametro de interfaz cartera paciente est√° activado
	 * Modificado por anexo 779
	 * @param numeroIdentificacion
	 * @param usuarioActual
	 * @param ingresoPacienteForm
	 */
	private ResultadoDouble consultaSaldoInterfaz(String numeroIdentificacion, UsuarioBasico usuarioActual, IngresoPacienteForm ingresoPacienteForm) 
	{
		ResultadoDouble resultadoDouble = new ResultadoDouble(0,"");
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazCarteraPacientes(usuarioActual.getCodigoInstitucionInt())))
		{
			UtilidadBDInterfaz utilidadInterfaz = new UtilidadBDInterfaz();
			resultadoDouble = utilidadInterfaz.consultarSaldoPaciente(numeroIdentificacion, usuarioActual.getCodigoInstitucionInt());
			logger.info("saldo consultado "+resultadoDouble.getResultado());
			if(resultadoDouble.getResultado()>0)
				ingresoPacienteForm.setSaldoInterfaz(UtilidadTexto.formatearValores(resultadoDouble.getResultado()));
			
			if(!ingresoPacienteForm.getSaldoInterfaz().equals(""))
			{
				//*******SE AGREGA MENSAJE DE ADVERTENCIA**************************************
				ElementoApResource alerta = new ElementoApResource("errors.notEspecific");
				String contenido = "Paciente con deuda pendiente "+ingresoPacienteForm.getSaldoInterfaz()+". Verificar Cartera.";
				alerta.agregarAtributo(contenido);
				ingresoPacienteForm.getMensajesAlerta().add(alerta);
				
				//*****************************************************************************
			}
				
			return resultadoDouble; 
		}
		
		return null;
		
	}
	
	
	/**
	 * Este mÈtodo se encarga de listar las ciudades existentes en el sistema
	 * que pertenecen a un determinado paÌs.
	 * 
	 * @param forma 
	 * @author Angela Aguirre
	 */
	private ActionForward listarCiudades(IngresoPacienteForm forma,Connection con, ActionMapping mapping) {
		String codigoPais="";
		
		if(forma.getEstado().equals("cambiarPaisNacimiento")){
			codigoPais= (String)forma.getCodigoPaisNacimiento();
		}else if(forma.getEstado().equals("cambiarPaisExpedicion")){			
			codigoPais=(String)forma.getCodigoPaisId();
		}else if(forma.getEstado().equals("cambiarPaisResidencia")){			
			codigoPais=(String)forma.getCodigoPaisResidencia();
		}		
		
		if(!UtilidadTexto.isEmpty(codigoPais)){
			ArrayList<HashMap<String, Object>> listaCiudades= Utilidades.obtenerCiudadesXPais(con, codigoPais);
			
			if(listaCiudades!=null && listaCiudades.size()==1){
				
				String codigoCiudad = (listaCiudades.get(0)).get("codigoDepartamento")+ConstantesBD.separadorSplit +
				              (listaCiudades.get(0)).get("codigoCiudad");
				
				if(forma.getEstado().equals("cambiarPaisNacimiento")){
					forma.setCodigoCiudadNacimiento(codigoCiudad);
				}else  if(forma.getEstado().equals("cambiarPaisExpedicion")){			
					forma.setCodigoCiudadId(codigoCiudad);
				}else if(forma.getEstado().equals("cambiarPaisResidencia")){	
					forma.setCodigoCiudadResidencia(codigoCiudad);
				}
			}
			if(forma.getEstado().equals("cambiarPaisNacimiento")){
				forma.setCiudadesNac(listaCiudades);
			}else  if(forma.getEstado().equals("cambiarPaisExpedicion")){
				forma.setCiudadesExp(listaCiudades);
			}else  if(forma.getEstado().equals("cambiarPaisResidencia")){
				forma.setCiudades(listaCiudades);
			}
		}
		return mapping.findForward("ingresarPaciente");
	}

	/**
	 * 
	 * @param con
	 * @param ingresoPacienteForm
	 * @param response
	 * @param usuarioActual
	 * @param codigoTipoContrato
	 * @param codigoRegimen
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String cargarDatosContratoCapitado(Connection con, IngresoPacienteForm ingresoPacienteForm, 
			HttpServletResponse response, UsuarioBasico usuarioActual, int codigoTipoContrato, String codigoRegimen) {
		
		String aux = ""; 
		
		if(codigoTipoContrato == ConstantesBD.codigoTipoContratoCapitado)
		{
			//si solo tiene un contrato vigente se debe postular la informacion
			if(ingresoPacienteForm.getContratos().size()==1)
			{
				int codClasi=Utilidades.convertirAEntero(ingresoPacienteForm.getContratos().get(0).get("codigoestratosocial")+"");
				if(codClasi>0)
				{
					String tipoRegimenCSE=Utilidades.obtenerTipoRegimenClasificacionSocioEconomica(con, codClasi);
					if(codigoRegimen.equals(tipoRegimenCSE))
					{
						ingresoPacienteForm.setCuenta("codigoEstratoSocial", codClasi);
						HashMap tempo=new HashMap();
						tempo.put("numRegistros", "0");
						HashMap tempo1=new HashMap();
						tempo1.put("numRegistros", "0");
						for(int i=0;i<Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales("numRegistros")+"");i++)
						{
							if(Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales("codigo_"+i)+"")==codClasi)
							{
								tempo.put("codigo_0", ingresoPacienteForm.getEstratosSociales("codigo_"+i)+"");
								tempo.put("descripcion_0", ingresoPacienteForm.getEstratosSociales("descripcion_"+i)+"");
								tempo.put("numRegistros", "1");
								break;
							}
						}
						String tipoAfil=ingresoPacienteForm.getContratos().get(0).get("tipoafiliado")+"";
						if(!UtilidadTexto.isEmpty(tipoAfil+""))
						{
							//{numRegistros=2, acronimo_0=B, nombre_1=Cotizante, nombre_0=Beneficiario, acronimo_1=C}
							ingresoPacienteForm.setCuenta("codigoTipoAfiliado", tipoAfil);
							
							for(int i=0;i<Utilidades.convertirAEntero(ingresoPacienteForm.getTiposAfiliado("numRegistros")+"");i++)
							{
								if((ingresoPacienteForm.getTiposAfiliado("acronimo_"+i)+"").equals(tipoAfil+""))
								{
									tempo1.put("acronimo_0", ingresoPacienteForm.getTiposAfiliado("acronimo_"+i)+"");
									tempo1.put("nombre_0", ingresoPacienteForm.getTiposAfiliado("nombre_"+i)+"");
									tempo1.put("numRegistros", "1");
									break;
								}
							}
	
						}
						if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitados(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
						{
							ingresoPacienteForm.setEstratosSociales(tempo);
							if(!UtilidadTexto.isEmpty(tipoAfil))
							{
								ingresoPacienteForm.setTiposAfiliado(tempo1);
							}
						}		
						if(Utilidades.convertirAEntero(ingresoPacienteForm.getEstratosSociales().get("numRegistros")+"")<=0)
						{
							aux = "<error>La ClasificaciÛn socio econÛmica del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
							UtilidadBD.closeConnection(con);
							//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
							try
							{
								response.setCharacterEncoding("UTF-8");
					            response.setContentType("text/xml");
								response.setHeader("Cache-Control", "no-cache");
								response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
						        response.getWriter().write("<respuesta>");
						        response.getWriter().write(aux);
						        response.getWriter().write("</respuesta>");
							}
							catch(IOException e)
							{
								logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
							}
							return null;
						}
						if(!UtilidadTexto.isEmpty(tipoAfil))
						{
							if(Utilidades.convertirAEntero(ingresoPacienteForm.getTiposAfiliado("numRegistros")+"")<=0)
							{
								aux = "<error>El Tipo Afiliado del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
								UtilidadBD.closeConnection(con);
								//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
								try
								{
									response.setCharacterEncoding("UTF-8");
						            response.setContentType("text/xml");
									response.setHeader("Cache-Control", "no-cache");
									response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
							        response.getWriter().write("<respuesta>");
							        response.getWriter().write(aux);
							        response.getWriter().write("</respuesta>");
								}
								catch(IOException e)
								{
									logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
								}
								return null;
								
							}
							
						}
					}
					else
					{
							ingresoPacienteForm.setEstratosSociales(new HashMap());
							ingresoPacienteForm.setTiposAfiliado(new HashMap());
						
							aux = "<error>El Tipo de rÈgimen de la ClasificaciÛn socio  econÛmica no corresponde con el Tipo de rÈgimen del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
							UtilidadBD.closeConnection(con);
							//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
							try
							{
								response.setCharacterEncoding("UTF-8");
					            response.setContentType("text/xml");
								response.setHeader("Cache-Control", "no-cache");
								response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
						        response.getWriter().write("<respuesta>");
						        response.getWriter().write(aux);
						        response.getWriter().write("</respuesta>");
							}
							catch(IOException e)
							{
								logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
							}
							return null;
					}
				}
				int natPaciente=Utilidades.convertirAEntero(ingresoPacienteForm.getContratos().get(0).get("naturalezapaciente")+"");
				if(natPaciente>0)
				{
					if(UtilidadValidacion.esNaturalezaValidaTipoRegimen(natPaciente,codigoRegimen+"")) 
					{

						ingresoPacienteForm.setCuenta("codigoNaturaleza", natPaciente+"");
						Vector<InfoDatosString> naturalezaVector=new Vector<InfoDatosString>();
						for(int i=0;i<ingresoPacienteForm.getNaturalezasPaciente().size();i++)
						{
							if(Utilidades.convertirAEntero(ingresoPacienteForm.getNaturalezasPaciente().get(i).getCodigo()) ==natPaciente)
							{
								naturalezaVector.add(ingresoPacienteForm.getNaturalezasPaciente().get(i));
							}
						}
						if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitados(usuarioActual.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
						{
							ingresoPacienteForm.setNaturalezasPaciente(naturalezaVector);
						}
						
						if(ingresoPacienteForm.getNaturalezasPaciente().size()<=0)
						{
							aux = "<error>La Naturaleza Paciente del paciente no corresponde con los Montos Cobro del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
							UtilidadBD.closeConnection(con);
							//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
							try
							{
								response.setCharacterEncoding("UTF-8");
					            response.setContentType("text/xml");
								response.setHeader("Cache-Control", "no-cache");
								response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
						        response.getWriter().write("<respuesta>");
						        response.getWriter().write(aux);
						        response.getWriter().write("</respuesta>");
							}
							catch(IOException e)
							{
								logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
							}
							return null;
						}
						
					}
					else
					{
						aux = "<error>El Tipo de rÈgimen de la Naturaleza Paciente no corresponde con el Tipo de rÈgimen del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
						UtilidadBD.closeConnection(con);
						//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
						try
						{
							response.setCharacterEncoding("UTF-8");
				            response.setContentType("text/xml");
							response.setHeader("Cache-Control", "no-cache");
							response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
					        response.getWriter().write("<respuesta>");
					        response.getWriter().write(aux);
					        response.getWriter().write("</respuesta>");
						}
						catch(IOException e)
						{
							logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
						}
						return null;
					}
				}
			}
		}
		return "";
	}	
}


