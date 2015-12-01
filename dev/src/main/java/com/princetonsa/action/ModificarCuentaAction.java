 /*
 * @(#)ModificarCuentaAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
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
import java.util.HashMap;
import java.util.List;
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

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.LogsAxioma;
import util.RespuestaHashMap;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.ConstantesBDFacturacion;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.interfaces.UtilidadBDInterfaz;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.ModificarCuentaForm;
import com.princetonsa.actionform.TrasladoCamasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.dto.facturacion.DtoValidacionTipoCobroPaciente;
import com.princetonsa.dto.interfaz.DtoInterfazPaciente;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.dto.manejoPaciente.DtoRegistroAccidentesTransito;
import com.princetonsa.dto.manejoPaciente.DtoRegistroEventosCatastroficos;
import com.princetonsa.dto.manejoPaciente.DtoRequsitosPaciente;
import com.princetonsa.dto.manejoPaciente.DtoResponsablePaciente;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.carteraPaciente.DocumentosGarantia;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.facturacion.RequisitosPacientesXConvenio;
import com.princetonsa.mundo.historiaClinica.Referencia;
import com.princetonsa.mundo.manejoPaciente.RegistroAccidentesTransito;
import com.princetonsa.mundo.manejoPaciente.RegistroEventosCatastroficos;
import com.princetonsa.mundo.manejoPaciente.ViasIngreso;
import com.princetonsa.mundo.presupuesto.PresupuestoPaciente;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontosCobroServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IValidacionTipoCobroPacienteServicio;

/**
 * Clase utilizada para modificar la cuenta del paciente
 * 
 * @author juanda
 * @version 1.0, 03-may-2004
 */
public class ModificarCuentaAction extends Action
{
	/**
	 * Para hacer los logs de la aplicaci�n
	 */
	protected Logger logger = Logger.getLogger(ModificarCuentaAction.class);
	
	
	
	/**
	 * Objetos usado para poder hacer el log en los requisitos del paciente
	 */
	RespuestaHashMap requisitos_antiguos;
	RespuestaHashMap requisitos_nuevos;
	/**
	 * M�todo encargado de el flujo y control de la funcionalidad
	 * @throws Exception
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		/**
		 * Conexi�n com�n para esta clase (Ahorro memoria)
		 */
		Connection con = null;
		try{
			if(form instanceof ModificarCuentaForm)
			{
				ModificarCuentaForm formaCuenta = (ModificarCuentaForm)form;
				String estado=formaCuenta.getEstado();
				HttpSession session = request.getSession();
				PersonaBasica paciente =(PersonaBasica)session.getAttribute("pacienteActivo");
				UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");


				logger.warn("[ModificarCuentaAction] estado=>"+formaCuenta.getEstado());

				try
				{
					String tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					//No se cierra conexi�n porque si llega aca ocurri� un
					//error al abrirla
					logger.error("Problemas abriendo la conexi�n en ServiciosAction");
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				/**
				 * Validar concurrencia
				 * Si ya est� en proceso de facturaci�n, no debe dejar entrar
				 **/
				if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
				/**
				 * Validar concurrencia
				 * Si ya est� en proceso de distribucion, no debe dejar entrar
				 **/
				else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), medico.getLoginUsuario()) )
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);

				//************************ESTADOS ASOCIADOS CON LA MODIFICACION DE LA CUENTA**********************************
				if(estado.equals("empezar"))
				{
					return this.accionEmpezar(con,paciente,formaCuenta,medico,mapping,request);
				}
				else if (estado.equals("seleccionCuentaAsocio")) //Caso en el que se selecciona una de las cuentas del asocio
				{
					return accionSeleccionCuentaAsocio(con,formaCuenta,mapping,medico,paciente);
				}
				else if (estado.equals("guardarCuenta"))
				{
					return accionGuardarCuenta(con, formaCuenta, medico, paciente, mapping, request, response);
				}
				else if (estado.equals("imprimirVerificacion"))
				{
					return accionImprimirVerificacion(con,formaCuenta,mapping,medico,request);
				}
				//***********************************************************************************************************
				//******************ESTADOS ASOCIADOS A LA MODIFICACION DE LOS CONVENIOS ADICIONALES***********************
				else if (estado.equals("convenioAdicional"))
				{
					return accionConvenioAdicional(con,mapping);
				}
				else if (estado.equals("guardarConvenioAdicional"))
				{
					return accionGuardarConvenioAdicional(con,formaCuenta, mapping, request);
				}
				//**********************************************************************************************************
				//*******************ESTADOS ASOCIADOS A LA RELACION DE PRESUPUESTOS (VENEZUELA)*****************************
				else if (estado.equals("elegirPresupuesto"))
				{
					return accionElegirPresupuesto(con,paciente,request,mapping);
				}
				else if (estado.equals("actualizarPresupuesto"))
				{
					return accionActualizarPresupuesto(con,formaCuenta,mapping,paciente,request);
				}
				//*************************************************************************************************************
				//***********************ESTADOS QUE MANEJAN LOS FILTROS (AJAX)***********************************************
				else if (estado.equals("filtroConvenio"))
				{
					return accionFiltroConvenio(con,formaCuenta,medico,response,paciente);
				}
				else if (estado.equals("filtroFechaAfiliacion"))
				{
					return accionFiltroFechaAfiliacion(con,formaCuenta,response);
				}
				else if (estado.equals("filtroResponsable"))
				{
					return accionFiltroResponsable(con,formaCuenta,response,paciente);
				}
				else if (estado.equals("filtroCiudadesId")||estado.equals("filtroCiudadesResidencia"))
				{
					return accionFiltroCiudades(con,formaCuenta,response);
				}
				else if (estado.equals("filtroAdicionarMonto"))
				{
					return accionFiltroAdicionarMonto(con,formaCuenta,response);
				}
				else if (estado.equals("filtroEliminarMonto"))
				{
					return accionFiltroEliminarMonto(con,formaCuenta,response);
				}
				else if (estado.equals("filtroTipoMonitoreo"))
				{
					return accionFiltroTipoMonitoreo(con , formaCuenta, response);
				}
				else if(estado.equals("filtroContrato"))
				{
					return accionFiltroContrato(con,formaCuenta,response);
				}
				else if(estado.equals("filtroTipoAfiliado"))
				{
					return filtroTipoAfiliado(con,formaCuenta,medico,response,paciente);
				}
				else if(estado.equals("filtroNaturalezaPaciente"))
				{
					return filtroNaturalezaPaciente(con,formaCuenta,medico,response,paciente);
				}
				
				//***********************************************************************************************************

				else
				{
					request.setAttribute("codigoDescripcionError","errors.invalid");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else if(form instanceof TrasladoCamasForm) // Flujo de selecci�n de camas
			{
				TrasladoCamasForm forma=(TrasladoCamasForm)form;
				String estado=forma.getEstado();
				if(estado.trim().isEmpty() || estado.equals("trasladoPaciente"))
				{
					TrasladoCamasAction tca=new TrasladoCamasAction();
					forma.setEstado("empezarPaciente");
					tca.execute(mapping, forma, request, response);
					forma.setOcultarEncabezado(true);
					forma.setLlamadoExterno(true);
					return mapping.findForward("trasladoPaciente");
				}
				else if(estado.equals("confirmarTrasladoCama"))
				{
					return mapping.findForward("confirmarTrasladoCama");
				}
				else if(estado.equals("cargarCama"))
				{
					ModificarCuentaForm modificarCuentaForm=(ModificarCuentaForm) request.getSession().getAttribute("ModificarCuentaForm");
					modificarCuentaForm.setTrasladoCamasForm(forma);
					return mapping.findForward("resultadoTrasladoCama");
				}
				else if(estado.equals("limpiarCama"))
				{
					ModificarCuentaForm modificarCuentaForm=(ModificarCuentaForm) request.getSession().getAttribute("ModificarCuentaForm");
					forma.reset();
					modificarCuentaForm.setTrasladoCamasForm(forma);
					return mapping.findForward("resultadoTrasladoCama");
				}
				
			}
			else{
				request.setAttribute("codigoDescripcionError","errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
		}catch (Exception e) {
			Log4JManager.error("Error", e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	

	/**
	 * M�todo que realiza validaciones sobre el contrato seleccionado por v�a AJAX
	 * @param con
	 * @param formaCuenta
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroContrato(Connection con,
			ModificarCuentaForm forma,
			HttpServletResponse response) 
	{
		
		ResultadoBoolean resultado = validacionSinContratoControlAnticipos(con, Utilidades.convertirAEntero(forma.getCodigoContrato()));
		
		if(forma.getCuenta("codigoContrato")!=null){
			verificarMontoCobro(forma,forma.getCuenta("codigoContrato").toString());
		}
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
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
	
	/**
	 * M�todo que realiza el filtro de los tipos de monitoreo del area seleccionada
	 * @param con
	 * @param formaCuenta
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroTipoMonitoreo(Connection con, ModificarCuentaForm formaCuenta, HttpServletResponse response) 
	{
		
		String respuesta = "<respuesta>";
		formaCuenta.setTiposMonitoreo(Utilidades.consultaTipoMonitoreoxCC(con, Utilidades.convertirAEntero(formaCuenta.getCodigoArea())));
		for(int i=0;i<Integer.parseInt(formaCuenta.getTiposMonitoreo("numRegistros").toString());i++)
		{
			respuesta +="" +
				"<tipom>" +
					"<codigo>"+formaCuenta.getTiposMonitoreo("codigo_"+i).toString()+"</codigo>" +
					"<nombre>"+formaCuenta.getTiposMonitoreo("nombre_"+i).toString()+"</nombre>" +
				"</tipom>";
		}
		respuesta += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(respuesta);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroTiposMonitoreo: "+e);
		}
		return null;
	}

	/**
	 * M�todo implementado para actualizar el ingreso de un presupuesto presupuesto
	 * @param con
	 * @param formaCuenta
	 * @param mapping
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionActualizarPresupuesto(Connection con, ModificarCuentaForm formaCuenta, ActionMapping mapping, PersonaBasica paciente, HttpServletRequest request) 
	{
		//Se toma el consecutivo del presupuesto
		String consecutivoPresupuesto = formaCuenta.getPresupuestos("consecutivo_"+formaCuenta.getPosPresupuesto()).toString();
		
		int resp = PresupuestoPaciente.actualizarIngreso(con, paciente.getCodigoIngreso()+"", consecutivoPresupuesto);
		UtilidadBD.closeConnection(con);
		
		if(resp<=0)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACI�N DEL INGRESO EN EL PRESUPUESTO"));
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresActionErrorsSinCabezote");
		}
		
		return mapping.findForward("elegirPresupuesto");
	}

	/**
	 * M�todo implementado para llevar a la p�gina de elecci�n de presupuesto
	 * @param con
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionElegirPresupuesto(Connection con, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping) 
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
	 * M�todo que realiza la impresion se la verificaci�n de derechos
	 * @param con
	 * @param formaCuenta
	 * @param mapping
	 * @param medico
	 * @param request
	 * @return
	 */
	private ActionForward accionImprimirVerificacion(Connection con, ModificarCuentaForm formaCuenta, ActionMapping mapping, UsuarioBasico medico, HttpServletRequest request) 
	{
		String nombreRptDesign = "VerificacionDerechos.rptdesign";
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
    	int codigoConvenio=Integer.parseInt(formaCuenta.getCuenta("codigoConvenio").toString().split(ConstantesBD.separadorSplit)[0]);
		
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
        //se mandan los par�metros al rreporte 
        newPathReport += "&idCuenta="+formaCuenta.getCodigoCuenta()+
        	"&idSubCuenta="+formaCuenta.getCodigoSubCuentaImpresion();
        	
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenCuenta");
	}

	/**
	 * M�todo que valida y almacena en memoria la informacion deverificacion de derechos de cada convenio
	 * @param con
	 * @param formaCuenta
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarConvenioAdicional(Connection con, ModificarCuentaForm formaCuenta, ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		String aux0 = "", aux1 = "";
		int pos = formaCuenta.getPosConvenio();
		
		//*******************VALIDACION DE CAMPOS REQUERIDOS***********************************
		//1) se verifica si la verificacion de derechos es requerida
		boolean esRequeridoVerificacion = false;
		if(!formaCuenta.getCuenta("codigoViaIngreso").toString().equals("")&&
				formaCuenta.getCuenta("esViaIngresoVerificacion").toString().equals(ConstantesBD.acronimoSi)&&
				formaCuenta.getVariosConvenios("esConvenioVerificacion_"+pos).toString().equals(ConstantesBD.acronimoSi))
			esRequeridoVerificacion = true;
		
		//2) Se verifica si se ingres� informaci�n requerida de la verificacion de derechos
		boolean seIngresoVerificacion = false;
		if(!esRequeridoVerificacion)
		{
			//Si se edit� informacion en el estado y tipo de verificacion , quiere decir que se tuvo la intenci�n
			//de ingresar verificacion de derechos
			if(!formaCuenta.getVariosConvenios("codigoEstado_"+pos).toString().equals("")||
				!formaCuenta.getVariosConvenios("codigoTipo_"+pos).toString().equals(""))
				seIngresoVerificacion = true;
		}
		
		//Si la verificacion de derechos es requerida o sin ser requerida se intent� ingresar,
		//se prosigue a realizar sus valdiaciones
		if(esRequeridoVerificacion||seIngresoVerificacion)
		{
			formaCuenta.setVariosConvenios("ingresoVerificacionDerechos_"+pos, ConstantesBD.acronimoSi);
			
			//estado de verificacion
			if(formaCuenta.getVariosConvenios("codigoEstado_"+pos).toString().equals(""))
				errores.add("El estado es requerido",new ActionMessage("errors.required","El estado (Verificaci�n de Derechos)"));
			//tipo de verificacion
			if(formaCuenta.getVariosConvenios("codigoTipo_"+pos).toString().equals(""))
				errores.add("El tipo es requerido", new ActionMessage("errors.required","El tipo (Verificaci�n de Derechos)"));
			//persona solicita
			if(formaCuenta.getVariosConvenios("personaSolicita_"+pos).toString().equals(""))
				errores.add("la persona solicita es requerido", new ActionMessage("errors.required","Persona Solicita (Verificaci�n de Derechos)"));
			//persona contactada
			if(!formaCuenta.getVariosConvenios("codigoEstado_"+pos).toString().equals("")&&
				!formaCuenta.getVariosConvenios("codigoEstado_"+pos).toString().equals(ConstantesIntegridadDominio.acronimoEstadoPendientePorVerificar)&&
				formaCuenta.getVariosConvenios("personaContactada_"+pos).toString().equals(""))
				errores.add("la persona contactada es requerido", new ActionMessage("errors.required","Persona contactada (Verificaci�n de Derechos)"));
			
			//Validar la fecha de verificacion
			boolean fechaValida = true;
			aux0 = formaCuenta.getVariosConvenios("fechaVerificacion_"+pos).toString();
			if(aux0.equals(""))
			{
				errores.add("",new ActionMessage("errors.required","la fecha verificaci�n (Verificaci�n de Derechos)"));
				fechaValida = false;
			}
			else
			{
				if(!UtilidadFecha.validarFecha(aux0))
				{
					errores.add("",new ActionMessage("errors.formatoFechaInvalido","de verificaci�n (Verificaci�n de Derechos)"));
					fechaValida = false;
				}
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(aux0, UtilidadFecha.getFechaActual()))
				{
					errores.add("",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de verificaci�n (Verificaci�n de Derechos)", "actual"));
					fechaValida = false;
				}
			}
			
			//Validar la hora de verificaci�n
			aux1 = formaCuenta.getVariosConvenios("horaVerificacion_"+pos).toString();
			if(aux1.equals(""))
				errores.add("",new ActionMessage("errors.required","la hora de verificaci�n (Verificaci�n de Derechos)"));
			else
			{
				if(!UtilidadFecha.validacionHora(aux1).puedoSeguir)
					errores.add("",new ActionMessage("errors.formatoHoraInvalido","de verificaci�n (Verificaci�n de Derechos)"));
				else if(fechaValida&&!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), aux0, aux1).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraPosteriorIgualActual", "de verificaci�n (Verificaci�n de Derechos)", "actual"));
					
			}
			
			//Validacion del porcentaje de cobertura
			if(!formaCuenta.getVariosConvenios("porcentajeCobertura_"+pos).toString().equals(""))
			{
				try
				{
					if(Double.parseDouble(formaCuenta.getVariosConvenios("porcentajeCobertura_"+pos).toString())>100)
						errores.add("",new ActionMessage("errors.MenorIgualQue","El porcentaje de cobertura (Verificaci�n de Derechos)","100%"));
				}
				catch(Exception e)
				{
					errores.add("",new ActionMessage("errors.float","El porcentaje de cobertura (Verificaci�n de Derechos)"));
				}
				
			}
		}
		else
			formaCuenta.setVariosConvenios("ingresoVerificacionDerechos_"+pos, ConstantesBD.acronimoNo);                                       
		//***************************************************************************************
		
		if(!errores.isEmpty())
		{
			formaCuenta.setEstado("convenioAdicional");
			saveErrors(request, errores);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("convenioAdicional");
	}

	/**
	 * M�todo que lleva a la p�gina de modificacion de un convenio espec�fico
	 * @param con
	 * @param mapping
	 * @return
	 */
	private ActionForward accionConvenioAdicional(Connection con, ActionMapping mapping) 
	{
		UtilidadBD.closeConnection(con);
		return mapping.findForward("convenioAdicional");
	}

	/**
	 * M�todo implementado para guardar la informacion de la cuenta
	 * @param con
	 * @param formaCuenta
	 * @param medico
	 * @param paciente
	 * @param mapping
	 * @param request
	 * @param response 
	 * @return
	 */
	private ActionForward accionGuardarCuenta(Connection con, ModificarCuentaForm formaCuenta, UsuarioBasico medico, PersonaBasica paciente, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws IPSException 
	{
		ActionErrors errores = new ActionErrors();
		
		
		//***************************VALIDACION DE CAMPOS************************************************
		errores = validacionesGuardarCuenta(con,formaCuenta,medico,errores);
		//************************************************************************************************************
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("cuenta");
		}
		
		UtilidadBD.iniciarTransaccion(con);
		
		
		
		DtoCuentas dtoCuenta = new DtoCuentas();
		//*******************SE CARGA EL MUNDO DE CUENTAS***************************************************
		this.cargarMundoGuardarCuenta(con,dtoCuenta,paciente,medico,formaCuenta);
		//************************************************************************************************
		
		
		
		
		Cuenta mundoCuenta = new Cuenta();
		
		//***************SE CARGA CUENTA ANTIGUA (para generacion de logs)**********************************************
		DtoCuentas dtoCuentaAntigua = new DtoCuentas();
		mundoCuenta.cargar(con, formaCuenta.getCuenta("idCuenta").toString());
		dtoCuentaAntigua = mundoCuenta.getCuenta();
		
		//************************************************************************************************************
		//****************SE GUARDA LA INFORMACION DE LA CUENTA*******************************
		mundoCuenta.setCuenta(dtoCuenta);
		ResultadoBoolean resp0 = mundoCuenta.guardar(con);
		//**************************************************************************************
		
		///////////////////////////////////////////////////////////////////////////////
		//adicionado por anexo 654 transplante
		//queda pendiente validacion:
		//se permite su modificacion si no tiene registros de translado de solicitudes
		if (dtoCuenta.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion || dtoCuenta.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios)
			if (!dtoCuenta.getTransplante().equals(dtoCuentaAntigua.getTransplante()))
			{
				IngresoGeneral ingresoGeneral = new IngresoGeneral();
				ingresoGeneral.actualizarTransplante(con, dtoCuenta.getTransplante(), dtoCuenta.getIdIngreso());
			}
		
		///////////////////////////////////////////////////////////////////////////////
		
		
		//***************************************************************************************************
		//******************************** INTERFAZ AX_PACIEN PRESTAMO DE HISTORIAS *************************
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazPaciente(medico.getCodigoInstitucionInt())))
		{
			UtilidadBDInterfaz utilidadBD = new UtilidadBDInterfaz();
			ResultadoBoolean res = new ResultadoBoolean(true,"");
			// se vuelve a cargar el objeto mundo de la clase cuentas para poder obtener los ultimos datos ingresados
			mundoCuenta.cargar(con, dtoCuenta.getIdCuenta());
			
			//Se verifica si existe registro para el paciente
			DtoInterfazPaciente dto = utilidadBD.cargarPaciente(paciente.getCodigoPersona()+"", medico.getCodigoInstitucionInt());

			if (UtilidadTexto.getBoolean(dto.isError()+""))
			{
				errores.add("", new ActionMessage("errors.notEspecific",dto.getMensaje()));
			
				saveErrors(request, errores);
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaErroresActionErrors");
			
			}
			
			
			dto.setInstitucion(medico.getCodigoInstitucionInt());
			
			// cargar los nuevos valores de la cuenta y los convenios modificados por el usuario
			// cargar el nuevo codigo del convenio
			dto.setCodconv(String.valueOf(mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo()));
			// cargar el nuevo nombre del convenio
			dto.setNomconv(mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getNombre());					
			// cargar el nit del tercero asociado al convenio 
			dto.setTercero(mundoCuenta.getCuenta().getConvenios()[0].getNit());
			
			//cargar usuario en sesion
			dto.setUsuario(medico.getLoginUsuario());
			
			//Se modifica el registro de la interfaz de tesoreria
			res = utilidadBD.modificarPaciente(dto);
			
			if (!res.isTrue())
			{
				errores.add("", new ActionMessage("errors.notEspecific",res.getDescripcion()));
			
				saveErrors(request, errores);
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaErroresActionErrors");
			
			}
		}
		//****************************************************************************************************
		//****************************************************************************************************
		
		
		if(resp0.isTrue())
		{
			boolean resp1 = true;
			if(dtoCuentaAntigua.getCodigoArea()!=dtoCuenta.getCodigoArea())
			{
				//*************SE ACTUALIZA CENTROS DE COSTO DE LA VALORACION********************************
				resp1 = this.actualizarCentrosCostoValoracion(con, formaCuenta);
				//***********************************************************************************
			}
			
			if(resp1)
			{
				//**************VALIDACION DEL TIPO EVENTO "ACCIDENTE DE TRANSITO"*****************************
				int resp2 = this.verificacionRegistroAccidenteTransito(con,formaCuenta,medico,paciente);
				//*********************************************************************************************
				
				if(resp2>0)
				{
					//********************VALIDACION DEL TIPO EVENTO "EVENTO CATASTROFICO"*************************
					int resp3 = this.verificacionRegistroEventoCatastrofico(con,formaCuenta,medico,paciente);
					//**********************************************************************************************
					
					if(resp3>0)
					{
						//******SE VERIFICA QUE SI SE HA SELECCIONADO TIPO DE EVENTO Y ES ASOCIO SE ACTUALICE EN LA OTRA CUENTA***********
						int resp4 = this.actualizacionTipoEventoAsocio(con,formaCuenta,paciente);
						//****************************************************************************************************************
						
						if(resp4>0)
						{
							//**************VERIFICACIONES DEL CAMBIO DE RESPONSABLE PACIENTE***************************************
							//Si el responsable fue eliminado o cambiado por otro se debe actualizar la informacion en el deudor
							//en el caso de que haya sido deudor
							if(formaCuenta.isFueEliminadoResponsable())
							{
								if(!DocumentosGarantia.actualizarDatosPersonaDocGarantia(
										con, 
										medico.getCodigoInstitucionInt(), 
										Integer.parseInt(dtoCuenta.getIdIngreso()), 
										paciente.getCodigoPersona(), 
										ConstantesIntegridadDominio.acronimoOtro, 
										mundoCuenta.getCuenta().getResponsablePaciente().getTipoIdentificacion(), 
										mundoCuenta.getCuenta().getResponsablePaciente().getNumeroIdentificacion()))
										errores.add("", new ActionMessage("errors.notEspecific","Problemas al actualizar los registros de deudor por la modificaci�n del responsable paciente"));
							}
							//Si al responsable se le modificaron los datos
							if(formaCuenta.isFueModificadoResponsable())
							{
								if(!DocumentosGarantia.actualizarDatosPersonaDocGarantia(
									con, 
									medico.getCodigoInstitucionInt(), 
									Integer.parseInt(dtoCuenta.getIdIngreso()), 
									paciente.getCodigoPersona(), 
									ConstantesIntegridadDominio.acronimoResponsablePaci, 
									mundoCuenta.getCuenta().getResponsablePaciente().getTipoIdentificacion(), 
									mundoCuenta.getCuenta().getResponsablePaciente().getNumeroIdentificacion()))
									errores.add("", new ActionMessage("errors.notEspecific","Problemas al actualizar los registros de deudor por la modificaci�n del responsable paciente"));
							}
							//*********************************************************************************************************
							
							
							//*********VERIFICACION DEL ORIGEN DE ADMISION (PARA SABER SI SE LLAMA A LA REFERENCIA)***********
							this.verificacionOrigenAdmision(con,formaCuenta,medico,paciente);
							//***************************************************************************************************
							
							//******SI EL CONVENIO ORIGINAL ES DIFERENTES AL CONVENIO NUEVO SE ACTUALIZA EL CONVENIO EN LOS CARGOS**********
							if(dtoCuentaAntigua.getConvenios()[0].getConvenio().getCodigo()!=dtoCuenta.getConvenios()[0].getConvenio().getCodigo())
								mundoCuenta.actualizarConvenioEnCargos(con, dtoCuenta.getConvenios()[0].getConvenio().getCodigo(), dtoCuenta.getConvenios()[0].getSubCuenta());
							//**************************************************************************************************************
							
							//********SI HUBO CAMBIO DE AREA SE VERIFICA SI FUE DE CUIDADOS ESPECIALES**********************************
							errores = this.verificacionAreaCuidadosEspeciales(con,formaCuenta,dtoCuenta,dtoCuentaAntigua,errores,paciente,medico);
							
							if(dtoCuentaAntigua.getCodigoArea()!=dtoCuenta.getCodigoArea() &&
								 !dtoCuenta.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria) &&
								  dtoCuenta.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
							//if(dtoCuentaAntigua.getCodigoArea()!=dtoCuenta.getCodigoArea())
							{
								TrasladoCamasAction trasladoCamasAction = new TrasladoCamasAction();
								try {
									if(formaCuenta.getTrasladoCamasForm()==null)
									{
										errores.add("seleccion cama",new ActionMessage("errors.notEspecific","La selecci�n de una cama para la nueva �rea es requerida"));
										saveErrors(request, errores);
										UtilidadBD.abortarTransaccion(con);
										UtilidadBD.closeConnection(con);
										return mapping.findForward("cuenta");
									}
									else
									{
										int cama=Utilidades.convertirAEntero(formaCuenta.getTrasladoCamasForm().getCama());
										
										if(cama<=0)
										{
											errores.add("seleccion cama",new ActionMessage("errors.notEspecific","La selecci�n de una cama para la nueva �rea es requerida"));
											saveErrors(request, errores);
											UtilidadBD.abortarTransaccion(con);
											UtilidadBD.closeConnection(con);
											return mapping.findForward("cuenta");
										}
									}
									formaCuenta.getTrasladoCamasForm().setEstado("guardarPaciente");
									
									ActionErrors erroresCama = formaCuenta.getTrasladoCamasForm().validate(mapping, request);
									if(!erroresCama.isEmpty())
									{
										if(!errores.isEmpty())
										{
											saveErrors(request, erroresCama);
											UtilidadBD.abortarTransaccion(con);
											UtilidadBD.closeConnection(con);
											return mapping.findForward("cuenta");
										}
									}
									
									trasladoCamasAction.accionGuardarTrasladoPaciente(formaCuenta.getTrasladoCamasForm(), mapping, con, paciente, medico, request, true, getServlet());
								} catch (Exception e) {
									errores.add("",new ActionMessage("errors.notEspecific","Error al actualizar el traslado de la cama. Proceso cancelado"));
									e.printStackTrace();
								}
							}
							//**********************************************************************************************************
						}
						else
							errores.add("",new ActionMessage("errors.notEspecific","Error al actualizar el tipo de evento en la cuenta asociada. Proceso cancelado"));
					}
					else
						errores.add("",new ActionMessage("errors.notEspecific","Error al realizar verificaciones y actualizaciones sobre la informaci�n de evento catastr�fico. Proceso cancelado"));
				}
				else
					errores.add("",new ActionMessage("errors.notEspecific","Error al realizar verificaciones y actualizaciones sobre la informaci�n de accidente de tr�nsito. Proceso cancelado"));
			}
			else
				errores.add("",new ActionMessage("errors.notEspecific","Error actualizando los centros de costo de la valoraci�n. Proceso cancelado"));
		}
		else
			errores.add("",new ActionMessage("errors.notEspecific",resp0.getDescripcion()));
		//****************************************************************************************************
		
		
		if(!errores.isEmpty())
		{
			
			saveErrors(request, errores);
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
		}
		else
		{
			logger.info("VOY A FINALIZAR TRANSACCION!!!");
			UtilidadBD.finalizarTransaccion(con);
			
			///**********SE CARGA DE NUEVO EL RESUMEN***************************************
			int idCuenta = formaCuenta.getCodigoCuenta();
			formaCuenta.reset();
			formaCuenta.setCodigoCuenta(idCuenta);
			//se carga el dto actualizado
			dtoCuenta = this.cargarFormCuenta(con, formaCuenta, medico, paciente);
			//*****************************************************************************
			
			//********************SE CARGA EL PACIENTE*******************************************
			ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
			try 
			{
				paciente.cargar(con, paciente.getCodigoPersona());
				paciente.cargarPaciente2(con, paciente.getCodigoPersona(), medico.getCodigoInstitucion(), medico.getCodigoCentroAtencion()+"");
			} 
			catch (Exception e) 
			{
				logger.info("Error en accionDetalle: "+e);
			}
			observable.addObserver(paciente);
			UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),servlet.getServletContext());
			//***********************************************************************************
			
			//*********+SE GENERA EL LOG *********************************************
			this.generarLog(con,dtoCuentaAntigua, dtoCuenta, medico);
			//***********************************************************************
			
			//************SE REVISAN LOS PRESUPUESTOS (VENEZUELA)*************************
			this.revisarPresupuestos(con, formaCuenta);
			//*************************************************************************++
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("resumenCuenta");
		}
		
	}

	

	/**
	 * M�todo que realiza la verificaci�n del cambio de area por flujo de cuidados especiales
	 * @param con
	 * @param formaCuenta
	 * @param dtoCuenta
	 * @param dtoCuentaAntigua 
	 * @param errores
	 * @param medico 
	 * @param paciente 
	 * @return
	 */
	private ActionErrors verificacionAreaCuidadosEspeciales(Connection con, ModificarCuentaForm formaCuenta, DtoCuentas dtoCuenta, DtoCuentas dtoCuentaAntigua, ActionErrors errores, PersonaBasica paciente, UsuarioBasico medico) 
	{
		if(dtoCuentaAntigua.getCodigoArea()!=dtoCuenta.getCodigoArea())
		{
			HashMap tiposMonitoreoAnt = Utilidades.consultaTipoMonitoreoxCC(con, dtoCuentaAntigua.getCodigoArea());
			HashMap tiposMonitoreo = Utilidades.consultaTipoMonitoreoxCC(con, dtoCuenta.getCodigoArea());
			
			//Si el �rea anterior ten�a tipos de monitoreo asociados quiere decir que es de area cuidado especial y si la nueva �rea
			//no ten�a tipos de monitoreo asociados no es de cuidado especial, en este caso, se debe revisar que si hay ingreso de cuidado
			//especial se debe reversar
			if(Utilidades.convertirAEntero(tiposMonitoreoAnt.get("numRegistros")+"")>0&&Utilidades.convertirAEntero(tiposMonitoreo.get("numRegistros")+"")<=0)
			{
				//Se verifica si existe ingreso cuidado especial activo
				if(UtilidadesManejoPaciente.existeIngresoCuidadoEspecialActivo(con, paciente.getCodigoIngreso(), ""))
					//Se finaliza el ingreso cuidado especial
					if(!Utilidades.actualizarEstadoCuidadosEspeciales(con, paciente.getCodigoIngreso(),medico.getLoginUsuario(),"",""))
						errores.add("",new ActionMessage("errors.notEspecific","Error al finalizar el ingreso cuidado especial actual. Proceso cancelado"));
						
			}
			
			
			//*****Si existe un ingreso cuidado especial y se modific� el area se actualiza el tipo de monitoreo********
			if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("existeIngresoCuidadoEspecial").toString())&&
				Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoTipoMonitoreo").toString())>0)
				if(UtilidadesManejoPaciente.actualizarTipoMonitoreoIngresoCuidadoEspecial(
					con, 
					Integer.parseInt(dtoCuenta.getIdIngreso()), 
					Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoTipoMonitoreo").toString()),
					dtoCuenta.getCodigoArea(),
					medico.getLoginUsuario())<=0)
					errores.add("",new ActionMessage("errors.notEspecific","Error al actualizar el tipo monitoreo en el ingreso cuidado especial actual. Proceso cancelado"));
			//***********************************************************************************************************
			
		}
		//Es posible que no se haya cambiado AREA pero s� se haya cambiado el tipo de monitoreo
		else if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("existeIngresoCuidadoEspecial").toString())&&
				Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoTipoMonitoreo").toString())!=Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoTipoMonitoreoAnterior").toString()))
			if(UtilidadesManejoPaciente.actualizarTipoMonitoreoIngresoCuidadoEspecial(
					con, 
					Integer.parseInt(dtoCuenta.getIdIngreso()), 
					Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoTipoMonitoreo").toString()),
					dtoCuenta.getCodigoArea(),
					medico.getLoginUsuario())<=0)
					errores.add("",new ActionMessage("errors.notEspecific","Error al actualizar el tipo monitoreo en el ingreso cuidado especial actual. Proceso cancelado"));
			
		return errores;
	}

	/**
	 * M�todo que realiza la actualizaci�n del tipo de evento en el asocio de cuentas
	 * @param con
	 * @param formaCuenta
	 * @param paciente
	 * @return
	 */
	private int actualizacionTipoEventoAsocio(Connection con, ModificarCuentaForm formaCuenta, PersonaBasica paciente) 
	{
		int resp = 1;
		
		if(paciente.getExisteAsocio()&&paciente.getCodigoCuentaAsocio()>0)
		{
			String cuentaAActualizar = "";
			if(formaCuenta.getCuenta("idCuenta").toString().equals(paciente.getCodigoCuenta()+""))
				cuentaAActualizar = paciente.getCodigoCuentaAsocio() + "";
			else
				cuentaAActualizar = paciente.getCodigoCuenta() + "";
			
			resp = Cuenta.actualizarTipoEventoCuenta(con, formaCuenta.getCuenta("codigoTipoEvento").toString(), cuentaAActualizar);
			
			//Se actualiza el convenio arp afiliado si es accidente de trabajo
			if(resp>0)
			{
				String convenioArpAfiliado = "";
				if(formaCuenta.getCuenta("codigoTipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoAccidenteTrabajo))
					convenioArpAfiliado = formaCuenta.getCuenta("codigoArpAfiliado").toString();
				
				resp = Cuenta.actualizarConvenioArpAfiliadoCuenta(con, cuentaAActualizar, convenioArpAfiliado);
			}
		}
		
		return resp;
	}

	/**
	 * M�todo implementado para verificar si al paciente se le puede relacionar presupuestos
	 * @param con
	 * @param formaCuenta
	 */
	private void revisarPresupuestos(Connection con, ModificarCuentaForm formaCuenta) 
	{
		//Se verifica que el ingreso del paciente no tenga un presupuesto asociado
		HashMap datosPresupuesto = PresupuestoPaciente.obtenerPresupuestoXIngreso(con, formaCuenta.getCuenta("idIngreso").toString());
		
		//Si no ten�a presupuesto el ingreso entonces se inicia la verificacion
		if(Integer.parseInt(datosPresupuesto.get("numRegistros").toString())<=0)
		{
		
			String codigoPaciente = formaCuenta.getCuenta("codigoPaciente").toString();
			String listadoConvenios = formaCuenta.getCuenta("codigoConvenio").toString().split(ConstantesBD.separadorSplit)[0];
			
			for(int i=0;i<Integer.parseInt(formaCuenta.getVariosConvenios("numRegistros").toString());i++)
				listadoConvenios += "," + formaCuenta.getVariosConvenios("codigoConvenio_"+i);
			
			formaCuenta.setPresupuestos(PresupuestoPaciente.obtenerPrespuestosSinIngreso(con, codigoPaciente, listadoConvenios));
			
			if(Integer.parseInt(formaCuenta.getPresupuestos("numRegistros").toString())>0)
				formaCuenta.setPresupuestoPaciente(true);
			else
				formaCuenta.setPresupuestoPaciente(false);
		}
		else
			formaCuenta.setPresupuestoPaciente(false);
		
	}

	/**
	 * M�todo que genera el log de la modificacion de la cuenta
	 * @param con 
	 * @param dtoCuentaAntigua
	 * @param dtoCuenta
	 * @param medico
	 */
	private void generarLog(Connection con, DtoCuentas dtoCuentaAntigua, DtoCuentas dtoCuenta, UsuarioBasico medico) 
	{
		String log = "", aux0 = "", aux1 = "";
		
	    //*****************************************************************************************************************
	    //******************************EDICION DE LA INFORMACION ANTIGUA**************************************************
		//*****************************************************************************************************************
	    String[] tipoRegimen = Utilidades.obtenerTipoRegimenConvenio(con, dtoCuentaAntigua.getConvenios()[0].getConvenio().getCodigo()+"").split("-");
	    
	    log="\n            ====INFORMACION ORIGINAL DE LA CUENTA "+dtoCuentaAntigua.getIdCuenta()+"===== " +
			"\n*  Convenio [" +dtoCuentaAntigua.getConvenios()[0].getConvenio().getNombre()+"] "+
			"\n*  Vr. utilizado Soat [" +dtoCuentaAntigua.getConvenios()[0].getValorUtilizadoSoat()+"] "+
			"\n*  Fecha Afiliaci�n [" +dtoCuentaAntigua.getConvenios()[0].getFechaAfiliacion()+"] "+
			"\n*  Semanas de Cotizaci�n [" +dtoCuentaAntigua.getConvenios()[0].getSemanasCotizacion()+"] "+
			"\n*  Tipo Complejidad [" +dtoCuentaAntigua.getDescripcionTipoComplejidad()+"] "+
			"\n*  Clasificaci�n socio Econ�mica ["+dtoCuentaAntigua.getConvenios()[0].getDescripcionClasificacionSocioEconomica()+"] " +
			"\n*  Tipo Afiliado ["+dtoCuentaAntigua.getConvenios()[0].getDescripcionTipoAfiliado()+"] " +
			"\n*  Monto Cobro ["+dtoCuentaAntigua.getConvenios()[0].getDescripcionMontoCobro()+"] " +
			"\n*  Tipo R�gimen ["+tipoRegimen[1]+"] " +
			"\n*  Tipo de cobertura ["+dtoCuentaAntigua.getConvenios()[0].getNombreTipoCobertura()+"] " +
			"\n*  Tipo Paciente ["+dtoCuentaAntigua.getDescripcionTipoPaciente()+"] " +
			"\n*  Naturaleza Paciente ["+dtoCuentaAntigua.getConvenios()[0].getDescripcionNaturalezaPaciente()+"] " +
			"\n*  Nro. Carnet ["+dtoCuentaAntigua.getConvenios()[0].getNroCarnet()+"] " +
			"\n*  Tipo Evento ["+dtoCuentaAntigua.getDescripcionTipoEvento()+"] " ;
			
	    if(dtoCuentaAntigua.getCodigoTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTrabajo)&&
	    	!dtoCuentaAntigua.getDescripcionConvenioArpAfiliado().equals(""))
	    	log+="\n*  C�digo Arp Afiliado ["+dtoCuentaAntigua.getDescripcionConvenioArpAfiliado()+"] " ;
		log+=
			"\n*  Nro. Poliza ["+dtoCuentaAntigua.getConvenios()[0].getNroPoliza()+"] " +
			"\n*  Origen Admisi�n ["+dtoCuentaAntigua.getDescripcionOrigenAdmision()+"] "+
			"\n*  Area ["+dtoCuentaAntigua.getDescripcionArea()+"] ";
	    
		//Edicion de los datos la poliza (si la hay)-----------------------------------------------------------------------
		/*if(dtoCuentaAntigua.getConvenios()[0].isSubCuentaPoliza())
		{
			log+="\n\n*  INFORMACI�N ADICIONAL P�LIZA " +
				"\n     Apellidos ["+dtoCuentaAntigua.getConvenios()[0].getTitularPoliza().getApellidos()+"]" +
				"\n     Nombres ["+dtoCuentaAntigua.getConvenios()[0].getTitularPoliza().getNombres()+"]" +
				"\n     Tipo Identificaci�n ["+dtoCuentaAntigua.getConvenios()[0].getTitularPoliza().getDescripcionTipoIdentificacion()+"]" +
				"\n     N�mero Identificaci�n ["+dtoCuentaAntigua.getConvenios()[0].getTitularPoliza().getNumeroIdentificacion()+"]" +
				"\n     Direcci�n ["+dtoCuentaAntigua.getConvenios()[0].getTitularPoliza().getDireccion()+"]" +
				"\n     Tel�fono ["+dtoCuentaAntigua.getConvenios()[0].getTitularPoliza().getTelefono()+"]" +
				"";
			
			log+="\n\n*  DATOS AUTORIZACI�N P�LIZA " ;
			log+="\n     Fecha       Autorizaci�n     Monto            " ;
			double saldo = 0;
			for(int i=0;i<dtoCuentaAntigua.getConvenios()[0].getTitularPoliza().getSizeInformacionPoliza();i++)
			{
				aux0 = dtoCuentaAntigua.getConvenios()[0].getTitularPoliza().getFechaInformacionPoliza(i);
				log+= "\n     "+UtilidadTexto.editarEspacios(aux0, aux0.length(), 12, false);
				aux0 = dtoCuentaAntigua.getConvenios()[0].getTitularPoliza().getAutorizacionInformacionPoliza(i);
				log+= UtilidadTexto.editarEspacios(aux0, aux0.length(), 17, false);
				aux0 = dtoCuentaAntigua.getConvenios()[0].getTitularPoliza().getValorInformacionPoliza(i);
				saldo += Double.parseDouble(aux0);
				log+= UtilidadTexto.formatearValores(aux0);
			}
			log+="\n\n     Saldo ["+ UtilidadTexto.formatearValores(saldo)+"]";
			
			
		}
		*/
		
		///Edicion de los requisitos antiguos del paciente----------------------------------------------------------------
		aux0 = "";
		aux1 = "";
	    for(int i=0;i<dtoCuentaAntigua.getConvenios()[0].getRequisitosPaciente().size();i++)
	    {
	    	DtoRequsitosPaciente requisito = (DtoRequsitosPaciente) dtoCuentaAntigua.getConvenios()[0].getRequisitosPaciente().get(i);
	    	if(requisito.getTipo().equals(ConstantesIntegridadDominio.acronimoIngreso))
	    	{
		    	aux0 += "\n     "+requisito.getDescripcion()+"";
		    	if(!requisito.isCumplido())
		    		aux0 += "  [no cumplido] ";
		    	else
		    		aux0 += "  [cumplido] ";
	    	}
	    	if(requisito.getTipo().equals(ConstantesIntegridadDominio.acronimoEgreso))
	    	{
		    	aux1 += "\n     "+requisito.getDescripcion()+"";
		    	if(!requisito.isCumplido())
		    		aux1 += "  [no cumplido] ";
		    	else
		    		aux1 += "  [cumplido] ";
	    	}
	    }
	    
	    if(aux0.length()>0||aux1.length()>0)
	    {
	    	log+="\n\n*  REQUISITOS PACIENTE ";
	    	
	    	if(aux0.length()>0)
	    		log += "\n     INGRESO: " + aux0;
	    	
	    	if(aux1.length()>0)
	    		log += "\n     EGRESO: " + aux1;
	    }
	    
	    
	    // VariosConvenios de derechos --------------------------------------------------------------------------------------
	    if(dtoCuentaAntigua.getConvenios()[0].isSubCuentaVerificacionDerechos())
	    {
	    	log+="\n\n*  VERIFICACI�N DE DERECHOS "+
	    	"\n     Estado ["+dtoCuentaAntigua.getConvenios()[0].getVerificacionDerechos().getDescripcionEstado()+"]" +
	    	"\n     Tipo ["+dtoCuentaAntigua.getConvenios()[0].getVerificacionDerechos().getDescripcionTipo()+"]" +
	    	"\n     N�mero ["+dtoCuentaAntigua.getConvenios()[0].getVerificacionDerechos().getNumero()+"]" +
	    	"\n     Persona Solicita ["+dtoCuentaAntigua.getConvenios()[0].getVerificacionDerechos().getPersonaSolicita()+"]" +
	    	"\n     Fecha Solicitud ["+dtoCuentaAntigua.getConvenios()[0].getVerificacionDerechos().getFechaSolicitud()+"]" +
	    	"\n     Hora Solicitud ["+dtoCuentaAntigua.getConvenios()[0].getVerificacionDerechos().getHoraSolicitud()+"]" +
	    	"\n     Persona Contactada ["+dtoCuentaAntigua.getConvenios()[0].getVerificacionDerechos().getPersonaContactada()+"]" +
	    	"\n     Fecha Verificaci�n ["+dtoCuentaAntigua.getConvenios()[0].getVerificacionDerechos().getFechaVerificacion()+"]" +
	    	"\n     Hora Verificaci�n ["+dtoCuentaAntigua.getConvenios()[0].getVerificacionDerechos().getHoraVerificacion()+"]" +
	    	"\n     Porcentaje Cobertura ["+dtoCuentaAntigua.getConvenios()[0].getVerificacionDerechos().getPorcentajeCobertura()+"]" +
	    	"\n     Cuota Verificaci�n ["+dtoCuentaAntigua.getConvenios()[0].getVerificacionDerechos().getCuotaVerificacion()+"]" +
	    	"\n     Observaciones ["+dtoCuentaAntigua.getConvenios()[0].getVerificacionDerechos().getObservaciones()+"]" ;
	    }
	    
	    //Repsonsable paciente ---------------------------------------------------------------------------------------
	    if(dtoCuentaAntigua.isTieneResponsablePaciente())
	    {
	    	log+="\n\n*  RESPONSABLE PACIENTE "+
	    	"\n     Tipo Identificaci�n ["+dtoCuentaAntigua.getResponsablePaciente().getDescripcionTipoIdentificacion()+"] " +
	    	"\n     N�mero Identificaci�n ["+dtoCuentaAntigua.getResponsablePaciente().getNumeroIdentificacion()+"] " +
	    	"\n     Pa�s expedici�n ["+dtoCuentaAntigua.getResponsablePaciente().getDescripcionPaisExpedicion()+"] " +
	    	"\n     Ciudad expedici�n ["+dtoCuentaAntigua.getResponsablePaciente().getDescripcionCiudadExpedicion()+"] " +
	    	"\n     Primer Apellido ["+dtoCuentaAntigua.getResponsablePaciente().getPrimerApellido()+"] " +
	    	"\n     Segundo Apellido ["+dtoCuentaAntigua.getResponsablePaciente().getSegundoApellido()+"] " +
	    	"\n     Primer Nombre ["+dtoCuentaAntigua.getResponsablePaciente().getPrimerNombre()+"] " +
	    	"\n     Segundo Nombre ["+dtoCuentaAntigua.getResponsablePaciente().getSegundoNombre()+"] " +
	    	"\n     Direcci�n ["+dtoCuentaAntigua.getResponsablePaciente().getDireccion()+"] " +
	    	"\n     Pa�s ["+dtoCuentaAntigua.getResponsablePaciente().getDescripcionPais()+"] " +
	    	"\n     Ciudad ["+dtoCuentaAntigua.getResponsablePaciente().getDescripcionCiudad()+"] " +
	    	"\n     Barrio ["+dtoCuentaAntigua.getResponsablePaciente().getDescripcionBarrio()+"] " +
	    	"\n     Tel�fono ["+dtoCuentaAntigua.getResponsablePaciente().getTelefono()+"] " +
	    	"\n     Fecha Nacimiento ["+dtoCuentaAntigua.getResponsablePaciente().getFechaNacimiento()+"] " +
	    	"\n     Relaci�n Paciente ["+dtoCuentaAntigua.getResponsablePaciente().getRelacionPaciente()+"] " +
	    	"";
	    }
	    
	    //**********************************************************************************************
	    //***********************GENERACION DE LOG VERIFICACIONES DERECHOS********************************
	    //************************************************************************************************
	    for(int i=1;i<dtoCuentaAntigua.getConvenios().length;i++)
	    {
	    	//Se verifica si el convenio tuvo cambio en su verificacion de derechos
	    	if(
	    		//1) El convenio no ten�a verificacion de derechos y se le ingres� una nueva
	    		(!dtoCuentaAntigua.getConvenios()[i].isSubCuentaVerificacionDerechos()&&
	    		dtoCuenta.getConvenios()[i].isSubCuentaVerificacionDerechos())
	    		||
	    		//2) El convenio ten�a verificacion de derechos y se le elimin�
	    		(dtoCuentaAntigua.getConvenios()[i].isSubCuentaVerificacionDerechos()&&
	    	    	!dtoCuenta.getConvenios()[i].isSubCuentaVerificacionDerechos())
	    	    ||
	    	    //3) El convenio ten�a verificacion de derechos pero se modific�
	    	    (dtoCuentaAntigua.getConvenios()[i].isSubCuentaVerificacionDerechos()&&
		    	    dtoCuenta.getConvenios()[i].isSubCuentaVerificacionDerechos())
	    	)
	    	{
	    		log+="\n\n====INFORMACION ORIGINAL VERIF. DERECHOS DE "+dtoCuentaAntigua.getConvenios()[i].getConvenio().getNombre()+"===== " ;
	    		
	    		if(dtoCuentaAntigua.getConvenios()[i].isSubCuentaVerificacionDerechos())
	    		{
	    		
		    		log += "\n     Estado ["+dtoCuentaAntigua.getConvenios()[i].getVerificacionDerechos().getDescripcionEstado()+"]" +
			    	"\n     Tipo ["+dtoCuentaAntigua.getConvenios()[i].getVerificacionDerechos().getDescripcionTipo()+"]" +
			    	"\n     N�mero ["+dtoCuentaAntigua.getConvenios()[i].getVerificacionDerechos().getNumero()+"]" +
			    	"\n     Persona Solicita ["+dtoCuentaAntigua.getConvenios()[i].getVerificacionDerechos().getPersonaSolicita()+"]" +
			    	"\n     Fecha Solicitud ["+dtoCuentaAntigua.getConvenios()[i].getVerificacionDerechos().getFechaSolicitud()+"]" +
			    	"\n     Hora Solicitud ["+dtoCuentaAntigua.getConvenios()[i].getVerificacionDerechos().getHoraSolicitud()+"]" +
			    	"\n     Persona Contactada ["+dtoCuentaAntigua.getConvenios()[i].getVerificacionDerechos().getPersonaContactada()+"]" +
			    	"\n     Fecha Verificaci�n ["+dtoCuentaAntigua.getConvenios()[i].getVerificacionDerechos().getFechaVerificacion()+"]" +
			    	"\n     Hora Verificaci�n ["+dtoCuentaAntigua.getConvenios()[i].getVerificacionDerechos().getHoraVerificacion()+"]" +
			    	"\n     Porcentaje Cobertura ["+dtoCuentaAntigua.getConvenios()[i].getVerificacionDerechos().getPorcentajeCobertura()+"]" +
			    	"\n     Cuota Verificaci�n ["+dtoCuentaAntigua.getConvenios()[i].getVerificacionDerechos().getCuotaVerificacion()+"]" +
			    	"\n     Observaciones ["+dtoCuentaAntigua.getConvenios()[i].getVerificacionDerechos().getObservaciones()+"]" ;
	    		}
	    		else
	    			log+= "\n     Sin informaci�n." ;
	    		
	    		
	    			
	    	}
	    	
	    	
	    	 
	    }
	    
	    //*****************************************************************************************************************
	    //******************************EDICION DE LA INFORMACION NUEVA**************************************************
		//*****************************************************************************************************************
	    tipoRegimen = Utilidades.obtenerTipoRegimenConvenio(con, dtoCuenta.getConvenios()[0].getConvenio().getCodigo()+"").split("-");
	    
	    log+="\n\n            ====INFORMACION DESPU�S DE LA MODIFICACI�N DE LA CUENTA "+dtoCuenta.getIdCuenta()+"===== " +
		    "\n*  Convenio [" +dtoCuenta.getConvenios()[0].getConvenio().getNombre()+"] "+
			"\n*  Vr. utilizado Soat [" +dtoCuenta.getConvenios()[0].getValorUtilizadoSoat()+"] "+
			"\n*  Fecha Afiliaci�n [" +dtoCuenta.getConvenios()[0].getFechaAfiliacion()+"] "+
			"\n*  Semanas de Cotizaci�n [" +dtoCuenta.getConvenios()[0].getSemanasCotizacion()+"] "+
			"\n*  Tipo Complejidad [" +dtoCuenta.getDescripcionTipoComplejidad()+"] "+
			"\n*  Clasificaci�n socio Econ�mica ["+dtoCuenta.getConvenios()[0].getDescripcionClasificacionSocioEconomica()+"] " +
			"\n*  Tipo Afiliado ["+dtoCuenta.getConvenios()[0].getDescripcionTipoAfiliado()+"] " +
			"\n*  Monto Cobro ["+dtoCuenta.getConvenios()[0].getDescripcionMontoCobro()+"] " +
			"\n*  Tipo R�gimen ["+tipoRegimen[1]+"] " +
			"\n*  Tipo de cobertura ["+dtoCuenta.getConvenios()[0].getNombreTipoCobertura()+"] " +
			"\n*  Tipo Paciente ["+dtoCuenta.getDescripcionTipoPaciente()+"] " +
			"\n*  Naturaleza Paciente ["+dtoCuenta.getConvenios()[0].getDescripcionNaturalezaPaciente()+"] " +
			"\n*  Nro. Carnet ["+dtoCuenta.getConvenios()[0].getNroCarnet()+"] " +
			"\n*  Tipo Evento ["+dtoCuenta.getDescripcionTipoEvento()+"] " ;
			
	    if(dtoCuenta.getCodigoTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTrabajo)&&
	    	!dtoCuenta.getDescripcionConvenioArpAfiliado().equals(""))
	    	log+="\n*  C�digo Arp Afiliado ["+dtoCuenta.getDescripcionConvenioArpAfiliado()+"] " ;
		log+=
			"\n*  Nro. Poliza ["+dtoCuenta.getConvenios()[0].getNroPoliza()+"] " +
			"\n*  Origen Admisi�n ["+dtoCuenta.getDescripcionOrigenAdmision()+"] "+
			"\n*  Area ["+dtoCuenta.getDescripcionArea()+"] ";
	    
	   //Edicion de los datos la poliza (si la hay)-----------------------------------------------------------------------
		if(dtoCuenta.getConvenios()[0].isSubCuentaPoliza())
		{
			log+="\n\n*  INFORMACI�N ADICIONAL P�LIZA " +
				"\n     Apellidos ["+dtoCuenta.getConvenios()[0].getTitularPoliza().getApellidos()+"]" +
				"\n     Nombres ["+dtoCuenta.getConvenios()[0].getTitularPoliza().getNombres()+"]" +
				"\n     Tipo Identificaci�n ["+dtoCuenta.getConvenios()[0].getTitularPoliza().getDescripcionTipoIdentificacion()+"]" +
				"\n     N�mero Identificaci�n ["+dtoCuenta.getConvenios()[0].getTitularPoliza().getNumeroIdentificacion()+"]" +
				"\n     Direcci�n ["+dtoCuenta.getConvenios()[0].getTitularPoliza().getDireccion()+"]" +
				"\n     Tel�fono ["+dtoCuenta.getConvenios()[0].getTitularPoliza().getTelefono()+"]" +
				"";
			
			log+="\n\n*  DATOS AUTORIZACI�N P�LIZA " ;
			log+="\n     Fecha       Autorizaci�n     Monto            " ;
			double saldo = 0;
			for(int i=0;i<dtoCuenta.getConvenios()[0].getTitularPoliza().getSizeInformacionPoliza();i++)
			{
				aux0 = dtoCuenta.getConvenios()[0].getTitularPoliza().getFechaInformacionPoliza(i);
				log+= "\n     "+UtilidadTexto.editarEspacios(aux0, aux0.length(), 12, false);
				aux0 = dtoCuenta.getConvenios()[0].getTitularPoliza().getAutorizacionInformacionPoliza(i);
				log+= UtilidadTexto.editarEspacios(aux0, aux0.length(), 17, false);
				aux0 = dtoCuenta.getConvenios()[0].getTitularPoliza().getValorInformacionPoliza(i);
				saldo += Double.parseDouble(aux0);
				log+= UtilidadTexto.formatearValores(aux0);
			}
			log+="\n\n     Saldo ["+ UtilidadTexto.formatearValores(saldo)+"]";
			
			
		}
		
		///Edicion de los requisitos antiguos del paciente----------------------------------------------------------------
		aux0 = "";
		aux1 = "";
	    for(int i=0;i<dtoCuenta.getConvenios()[0].getRequisitosPaciente().size();i++)
	    {
	    	DtoRequsitosPaciente requisito = (DtoRequsitosPaciente) dtoCuenta.getConvenios()[0].getRequisitosPaciente().get(i);
	    	if(requisito.getTipo().equals(ConstantesIntegridadDominio.acronimoIngreso))
	    	{
		    	aux0 += "\n     "+requisito.getDescripcion()+"";
		    	if(!requisito.isCumplido())
		    		aux0 += "  [no cumplido] ";
		    	else
		    		aux0 += "  [cumplido] ";
	    	}
	    	if(requisito.getTipo().equals(ConstantesIntegridadDominio.acronimoEgreso))
	    	{
		    	aux1 += "\n     "+requisito.getDescripcion()+"";
		    	if(!requisito.isCumplido())
		    		aux1 += "  [no cumplido] ";
		    	else
		    		aux1 += "  [cumplido] ";
	    	}
	    }
	    
	    if(aux0.length()>0||aux1.length()>0)
	    {
	    	log+="\n\n*  REQUISITOS PACIENTE ";
	    	
	    	if(aux0.length()>0)
	    		log += "\n     INGRESO: " + aux0;
	    	
	    	if(aux1.length()>0)
	    		log += "\n     EGRESO: " + aux1;
	    }
	    
	    
	    // Verificacion de derechos --------------------------------------------------------------------------------------
	    if(dtoCuenta.getConvenios()[0].isSubCuentaVerificacionDerechos())
	    {
	    	log+="\n\n*  VERIFICACI�N DE DERECHOS "+
	    	"\n     Estado ["+dtoCuenta.getConvenios()[0].getVerificacionDerechos().getDescripcionEstado()+"]" +
	    	"\n     Tipo ["+dtoCuenta.getConvenios()[0].getVerificacionDerechos().getDescripcionTipo()+"]" +
	    	"\n     N�mero ["+dtoCuenta.getConvenios()[0].getVerificacionDerechos().getNumero()+"]" +
	    	"\n     Persona Solicita ["+dtoCuenta.getConvenios()[0].getVerificacionDerechos().getPersonaSolicita()+"]" +
	    	"\n     Fecha Solicitud ["+dtoCuenta.getConvenios()[0].getVerificacionDerechos().getFechaSolicitud()+"]" +
	    	"\n     Hora Solicitud ["+dtoCuenta.getConvenios()[0].getVerificacionDerechos().getHoraSolicitud()+"]" +
	    	"\n     Persona Contactada ["+dtoCuenta.getConvenios()[0].getVerificacionDerechos().getPersonaContactada()+"]" +
	    	"\n     Fecha Verificaci�n ["+dtoCuenta.getConvenios()[0].getVerificacionDerechos().getFechaVerificacion()+"]" +
	    	"\n     Hora Verificaci�n ["+dtoCuenta.getConvenios()[0].getVerificacionDerechos().getHoraVerificacion()+"]" +
	    	"\n     Porcentaje Cobertura ["+dtoCuenta.getConvenios()[0].getVerificacionDerechos().getPorcentajeCobertura()+"]" +
	    	"\n     Cuota Verificaci�n ["+dtoCuenta.getConvenios()[0].getVerificacionDerechos().getCuotaVerificacion()+"]" +
	    	"\n     Observaciones ["+dtoCuenta.getConvenios()[0].getVerificacionDerechos().getObservaciones()+"]" ;
	    }
	    
	    //Repsonsable paciente ---------------------------------------------------------------------------------------
	    if(dtoCuenta.isTieneResponsablePaciente())
	    {
	    	log+="\n\n*  RESPONSABLE PACIENTE "+
	    	"\n     Tipo Identificaci�n ["+dtoCuenta.getResponsablePaciente().getDescripcionTipoIdentificacion()+"] " +
	    	"\n     N�mero Identificaci�n ["+dtoCuenta.getResponsablePaciente().getNumeroIdentificacion()+"] " +
	    	"\n     Pa�s expedici�n ["+dtoCuenta.getResponsablePaciente().getDescripcionPaisExpedicion()+"] " +
	    	"\n     Ciudad expedici�n ["+dtoCuenta.getResponsablePaciente().getDescripcionCiudadExpedicion()+"] " +
	    	"\n     Primer Apellido ["+dtoCuenta.getResponsablePaciente().getPrimerApellido()+"] " +
	    	"\n     Segundo Apellido ["+dtoCuenta.getResponsablePaciente().getSegundoApellido()+"] " +
	    	"\n     Primer Nombre ["+dtoCuenta.getResponsablePaciente().getPrimerNombre()+"] " +
	    	"\n     Segundo Nombre ["+dtoCuenta.getResponsablePaciente().getSegundoNombre()+"] " +
	    	"\n     Direcci�n ["+dtoCuenta.getResponsablePaciente().getDireccion()+"] " +
	    	"\n     Pa�s ["+dtoCuenta.getResponsablePaciente().getDescripcionPais()+"] " +
	    	"\n     Ciudad ["+dtoCuenta.getResponsablePaciente().getDescripcionCiudad()+"] " +
	    	"\n     Barrio ["+dtoCuenta.getResponsablePaciente().getDescripcionBarrio()+"] " +
	    	"\n     Tel�fono ["+dtoCuenta.getResponsablePaciente().getTelefono()+"] " +
	    	"\n     Fecha Nacimiento ["+dtoCuenta.getResponsablePaciente().getFechaNacimiento()+"] " +
	    	"\n     Relaci�n Paciente ["+dtoCuenta.getResponsablePaciente().getRelacionPaciente()+"] " +
	    	"";
	    }
	    
	    //**********************************************************************************************
	    //***********************GENERACION DE LOG VERIFICACIONES DERECHOS********************************
	    //************************************************************************************************
	    for(int i=1;i<dtoCuentaAntigua.getConvenios().length;i++)
	    {
	    	//Se verifica si el convenio tuvo cambio en su verificacion de derechos
	    	if(
	    		//1) El convenio no ten�a verificacion de derechos y se le ingres� una nueva
	    		(!dtoCuentaAntigua.getConvenios()[i].isSubCuentaVerificacionDerechos()&&
	    		dtoCuenta.getConvenios()[i].isSubCuentaVerificacionDerechos())
	    		||
	    		//2) El convenio ten�a verificacion de derechos y se le elimin�
	    		(dtoCuentaAntigua.getConvenios()[i].isSubCuentaVerificacionDerechos()&&
	    	    	!dtoCuenta.getConvenios()[i].isSubCuentaVerificacionDerechos())
	    	    ||
	    	    //3) El convenio ten�a verificacion de derechos pero se modific�
	    	    (dtoCuentaAntigua.getConvenios()[i].isSubCuentaVerificacionDerechos()&&
		    	    dtoCuenta.getConvenios()[i].isSubCuentaVerificacionDerechos())
	    	)
	    	{
	    		
	    		
	    		log += "\n\n====INFORMACION DESPU�S DE LA MODIFICACI�N VERIF. DERECHOS DE "+dtoCuenta.getConvenios()[i].getConvenio().getNombre()+"===== " ;
	    		
	    		if(dtoCuenta.getConvenios()[i].isSubCuentaVerificacionDerechos())
	    		{
		    		log += "\n     Estado ["+dtoCuenta.getConvenios()[i].getVerificacionDerechos().getDescripcionEstado()+"]" +
			    	"\n     Tipo ["+dtoCuenta.getConvenios()[i].getVerificacionDerechos().getDescripcionTipo()+"]" +
			    	"\n     N�mero ["+dtoCuenta.getConvenios()[i].getVerificacionDerechos().getNumero()+"]" +
			    	"\n     Persona Solicita ["+dtoCuenta.getConvenios()[i].getVerificacionDerechos().getPersonaSolicita()+"]" +
			    	"\n     Fecha Solicitud ["+dtoCuenta.getConvenios()[i].getVerificacionDerechos().getFechaSolicitud()+"]" +
			    	"\n     Hora Solicitud ["+dtoCuenta.getConvenios()[i].getVerificacionDerechos().getHoraSolicitud()+"]" +
			    	"\n     Persona Contactada ["+dtoCuenta.getConvenios()[i].getVerificacionDerechos().getPersonaContactada()+"]" +
			    	"\n     Fecha Verificaci�n ["+dtoCuenta.getConvenios()[i].getVerificacionDerechos().getFechaVerificacion()+"]" +
			    	"\n     Hora Verificaci�n ["+dtoCuenta.getConvenios()[i].getVerificacionDerechos().getHoraVerificacion()+"]" +
			    	"\n     Porcentaje Cobertura ["+dtoCuenta.getConvenios()[i].getVerificacionDerechos().getPorcentajeCobertura()+"]" +
			    	"\n     Cuota Verificaci�n ["+dtoCuenta.getConvenios()[i].getVerificacionDerechos().getCuotaVerificacion()+"]" +
			    	"\n     Observaciones ["+dtoCuenta.getConvenios()[i].getVerificacionDerechos().getObservaciones()+"]" ;
	    		}
	    		else
	    			log+= "\n     Sin informaci�n." ;
	    			
	    	}
	    	
	    	
	    	 
	    }
	    
	    log+="\n\n";
	    
		LogsAxioma.enviarLog(ConstantesBD.logModificarCuentaCodigo, log, ConstantesBD.tipoRegistroLogModificacion,medico.getLoginUsuario()); 
	    
		
	}

	/**
	 * M�todo que carga el mundo de cuenta
	 * @param con
	 * @param dtoCuenta
	 * @param paciente
	 * @param medico
	 * @param formaCuenta
	 */
	private void cargarMundoGuardarCuenta(Connection con, DtoCuentas dtoCuenta, PersonaBasica paciente, UsuarioBasico medico, ModificarCuentaForm formaCuenta) throws IPSException 
	{
		Contrato mundoContrato = new Contrato();
		int numConvenios =  1 + Integer.parseInt(formaCuenta.getVariosConvenios("numRegistros").toString()); 
		
		DtoSubCuentas[] dtoSubCuentas = new DtoSubCuentas[numConvenios];
		
		//****************************************************************************************************************************
		//**********************SE LLENAN LOS DATOS DE LA CUENTA*********************************************************************
		//*****************************************************************************************************************************
		dtoCuenta.setIdCuenta(formaCuenta.getCuenta("idCuenta").toString());
		dtoCuenta.setIdIngreso(formaCuenta.getCuenta("idIngreso").toString());
		dtoCuenta.setCodigoViaIngreso(Integer.parseInt(formaCuenta.getCuenta("codigoViaIngreso").toString()));
		dtoCuenta.setHospitalDia(UtilidadTexto.getBoolean(formaCuenta.getCuenta("hospitalDia").toString()));
		dtoCuenta.setCodigoPaciente(paciente.getCodigoPersona()+"");
		dtoCuenta.setDesplazado(paciente.getCodigoGrupoPoblacional().equals(ConstantesIntegridadDominio.acronimoDesplazados)?true:false);
		dtoCuenta.setLoginUsuario(medico.getLoginUsuario());
		dtoCuenta.setObservaciones(formaCuenta.getCuenta("observaciones").toString());
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		// adicionado por anexo 654 transplante	
		dtoCuenta.setTransplante(formaCuenta.getCuenta("transplante")+"");
		//////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("indicadorManejaComplejidad").toString())&&
			!formaCuenta.getCuenta("tipoComplejidad").toString().equals(""))
			dtoCuenta.setCodigoTipoComplejidad(Integer.parseInt(formaCuenta.getCuenta("tipoComplejidad").toString()));
		
		dtoCuenta.setCodigoTipoPaciente(formaCuenta.getCuenta("codigoTipoPaciente").toString());
		dtoCuenta.setCodigoTipoEvento(formaCuenta.getCuenta("codigoTipoEvento").toString());
		
		if(dtoCuenta.getCodigoTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTrabajo)&&
			!formaCuenta.getCuenta("codigoArpAfiliado").toString().equals(""))
			dtoCuenta.setCodigoConvenioArpAfiliado(Integer.parseInt(formaCuenta.getCuenta("codigoArpAfiliado").toString()));
		
		dtoCuenta.setCodigoOrigenAdmision(Integer.parseInt(formaCuenta.getCuenta("codigoOrigenAdmision").toString()));
		dtoCuenta.setCodigoArea(Integer.parseInt(formaCuenta.getCuenta("codigoArea").toString()));
		
		//****************************************************************************************************************************
		//**********************SE LLENAN LOS DATOS DEL CONVENIO PRINCIPAL*************************************************************
		//*****************************************************************************************************************************
		//Se llena los datos del convenio principal
		dtoSubCuentas[0] = new DtoSubCuentas();
		String[] datosConvenio = formaCuenta.getCuenta("codigoConvenio").toString().split(ConstantesBD.separadorSplit);
		dtoSubCuentas[0].setSubCuenta(formaCuenta.getCuenta("idSubCuenta").toString());
		dtoSubCuentas[0].setConvenio(new InfoDatosInt(Integer.parseInt(datosConvenio[0]),""));
		dtoSubCuentas[0].setIngreso(Integer.parseInt(formaCuenta.getCuenta("idIngreso").toString()));
		dtoSubCuentas[0].setCodigoTipoRegimen(formaCuenta.getCuenta("codigoTipoRegimen").toString());
		dtoSubCuentas[0].setCodigoTipoCobertura(Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoTipoCobertura").toString()));
		
		
		dtoSubCuentas[0].setContrato(Integer.parseInt(formaCuenta.getCuenta("codigoContrato").toString()));
		
		mundoContrato.cargar(con, dtoSubCuentas[0].getContrato()+"");
		
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("esConvenioSoat").toString()))
			dtoSubCuentas[0].setValorUtilizadoSoat(formaCuenta.getCuenta("valorUtilizadoSoat").toString());
		
		dtoSubCuentas[0].setFechaAfiliacion(formaCuenta.getCuenta("fechaAfiliacion").toString());
		
		if(!formaCuenta.getCuenta("semanasCotizacion").toString().equals(""))
			dtoSubCuentas[0].setSemanasCotizacion(Integer.parseInt(formaCuenta.getCuenta("semanasCotizacion").toString()));
		if(!formaCuenta.getCuenta("mesesCotizacion").toString().equals(""))
			dtoSubCuentas[0].setMesesCotizacion(Integer.parseInt(formaCuenta.getCuenta("mesesCotizacion").toString()));
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		IValidacionTipoCobroPacienteServicio servicioValidacion=FacturacionServicioFabrica.crearValidacionTipoCobroPacienteServicio();
		DtoValidacionTipoCobroPaciente validacion=servicioValidacion.validarTipoCobroPacienteServicioConvenioContrato(Integer.parseInt(formaCuenta.getCuenta("codigoContrato").toString()));
		
		dtoSubCuentas[0].setTipoCobroPaciente(validacion.getTipoCobroPaciente());
		
		if(UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion()))
			dtoSubCuentas[0].setClasificacionSocioEconomica(Integer.parseInt(formaCuenta.getCuenta("codigoEstratoSocial").toString()));
		else
			dtoSubCuentas[0].setClasificacionSocioEconomica(ConstantesBD.codigoNuncaValido);
		if(UtilidadTexto.getBoolean(validacion.getMostrarTipoAfiliado()))
			dtoSubCuentas[0].setTipoAfiliado(formaCuenta.getCuenta("codigoTipoAfiliado").toString());
		else
			dtoSubCuentas[0].setTipoAfiliado("");
		if(UtilidadTexto.getBoolean(validacion.getMostrarNaturalezaPaciente()))
			
			/**
			 * Inc 1851.
			 * Se valida si la naturaleza no tiene informaci�n
			 * Diana Ruiz
			 */
			
			if (UtilidadTexto.isEmpty(formaCuenta.getCuenta("codigoNaturaleza").toString()))
				dtoSubCuentas[0].setNaturalezaPaciente(-1);
			else		
				dtoSubCuentas[0].setNaturalezaPaciente(Integer.parseInt(formaCuenta.getCuenta("codigoNaturaleza").toString()));
		else
			dtoSubCuentas[0].setNaturalezaPaciente(ConstantesBD.codigoNuncaValido);
		
		if(UtilidadTexto.getBoolean(validacion.getManejaMontos()))
		{
			int codigoMontoTempo=Integer.parseInt(formaCuenta.getCuenta("codigoMontoCobro").toString());
			IMontosCobroServicio servicioMontoCobro=FacturacionServicioFabrica.crearMontosCobroServicio();
			DTOResultadoBusquedaDetalleMontos dtoMonto=servicioMontoCobro.obtenerDetalleMontoCobroPorId(codigoMontoTempo);
			dtoSubCuentas[0].setMontoCobro(dtoMonto.getDetalleCodigo());
			dtoSubCuentas[0].setTipoMontoCobro(dtoMonto.getTipoDetalleAcronimo());
		}
		else
		{
			dtoSubCuentas[0].setMontoCobro(ConstantesBD.codigoNuncaValido);
			dtoSubCuentas[0].setPorcentajeMontoCobro(validacion.getPorcentajeMontoCobro());
		}
		
		
		dtoSubCuentas[0].setNroCarnet(formaCuenta.getCuenta("numeroCarnet").toString());
		dtoSubCuentas[0].setNroPoliza(formaCuenta.getCuenta("numeroPoliza").toString());
		dtoSubCuentas[0].setNroAutorizacion(formaCuenta.getCuenta("autorizacionIngreso").toString());
		dtoSubCuentas[0].setCodigoPaciente(paciente.getCodigoPersona());
		dtoSubCuentas[0].setLoginUsuario(medico.getLoginUsuario());
		
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("esConvenioColsanitas").toString()))
			dtoSubCuentas[0].setNumeroSolicitudVolante(formaCuenta.getCuenta("numeroSolicitudVolante").toString());
		else
			dtoSubCuentas[0].setNumeroSolicitudVolante("");
		//Seccion de informacion de la poliza si aplica
		dtoSubCuentas[0].setSubCuentaPoliza(UtilidadTexto.getBoolean(formaCuenta.getCuenta("esConvenioPoliza").toString()));
		/*
		if(dtoSubCuentas[0].isSubCuentaPoliza())
		{
			dtoSubCuentas[0].getTitularPoliza().setApellidos(formaCuenta.getCuenta("apellidosPoliza").toString());
			dtoSubCuentas[0].getTitularPoliza().setNombres(formaCuenta.getCuenta("nombresPoliza").toString());
			dtoSubCuentas[0].getTitularPoliza().setCodigoTipoIdentificacion(formaCuenta.getCuenta("tipoIdPoliza").toString());
			dtoSubCuentas[0].getTitularPoliza().setNumeroIdentificacion(formaCuenta.getCuenta("numeroIdPoliza").toString());
			dtoSubCuentas[0].getTitularPoliza().setDireccion(formaCuenta.getCuenta("direccionPoliza").toString());
			dtoSubCuentas[0].getTitularPoliza().setTelefono(formaCuenta.getCuenta("telefonoPoliza").toString());
			dtoSubCuentas[0].getTitularPoliza().setExisteBd(UtilidadTexto.getBoolean(formaCuenta.getCuenta("existeTitular").toString()));
			
			
			for(int i=0;i<Integer.parseInt(formaCuenta.getCuenta("numDatosPoliza").toString());i++)
			{
				dtoSubCuentas[0].getTitularPoliza().setInformacionPoliza(
						formaCuenta.getCuenta("codigoPoliza_"+i).toString(),
						UtilidadFecha.conversionFormatoFechaABD(formaCuenta.getCuenta("fechaPoliza_"+i).toString()), 
						formaCuenta.getCuenta("autorizacionPoliza_"+i).toString(), 
						formaCuenta.getCuenta("valorPoliza_"+i).toString(),
						medico.getLoginUsuario(),
						UtilidadTexto.getBoolean(formaCuenta.getCuenta("existePoliza_"+i).toString()),
						UtilidadTexto.getBoolean(formaCuenta.getCuenta("eliminarPoliza_"+i).toString()));
			}
			
		}
		*/
		//Se llenan los requisitos del paciente
		for(int i=0;i<Integer.parseInt(formaCuenta.getCuenta("numReqIngreso").toString());i++)
		{
			DtoRequsitosPaciente requisitos = new DtoRequsitosPaciente();
			requisitos.setCodigo(Integer.parseInt(formaCuenta.getCuenta("codigoReqIngreso_"+i).toString()));
			requisitos.setDescripcion(formaCuenta.getCuenta("descripcionReqIngreso_"+i).toString());
			requisitos.setTipo(ConstantesIntegridadDominio.acronimoIngreso);
			requisitos.setCumplido(UtilidadTexto.getBoolean(formaCuenta.getCuenta("cumplidoReqIngreso_"+i).toString()));
			
			dtoSubCuentas[0].getRequisitosPaciente().add(requisitos);
		}
		for(int i=0;i<Integer.parseInt(formaCuenta.getCuenta("numReqEgreso").toString());i++)
		{
			DtoRequsitosPaciente requisitos = new DtoRequsitosPaciente();
			requisitos.setCodigo(Integer.parseInt(formaCuenta.getCuenta("codigoReqEgreso_"+i).toString()));
			requisitos.setDescripcion(formaCuenta.getCuenta("descripcionReqEgreso_"+i).toString());
			requisitos.setTipo(ConstantesIntegridadDominio.acronimoEgreso);
			requisitos.setCumplido(UtilidadTexto.getBoolean(formaCuenta.getCuenta("cumplidoReqEgreso_"+i).toString()));
			
			dtoSubCuentas[0].getRequisitosPaciente().add(requisitos);
		}
		
		
		
		
		//Se llena la verificacion de derechos
		//cuando es asocio no se llena la verificacion de derechos
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("tieneVerificacionDerechos").toString())||
			UtilidadTexto.getBoolean(formaCuenta.getCuenta("ingresoVerificacionDerechos").toString()))
		{
			
			dtoSubCuentas[0].setSubCuentaVerificacionDerechos(true);
			
			//Si ten�a verificacion de derechos se a�ade el id de la subCuenta
			if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("tieneVerificacionDerechos").toString()))
			{
				
				dtoSubCuentas[0].getVerificacionDerechos().setSubCuenta(formaCuenta.getCuenta("idSubCuenta").toString());
				
				//Si exist�a verificacion de derechos y se borr� la informacion se debe eliminar
				if(!UtilidadTexto.getBoolean(formaCuenta.getCuenta("ingresoVerificacionDerechos").toString()))
					dtoSubCuentas[0].getVerificacionDerechos().setEliminar(true);
			}
				
			dtoSubCuentas[0].getVerificacionDerechos().setCodigoEstado(formaCuenta.getVerificacion("codigoEstado").toString());
			dtoSubCuentas[0].getVerificacionDerechos().setCodigoTipo(formaCuenta.getVerificacion("codigoTipo").toString());
			dtoSubCuentas[0].getVerificacionDerechos().setNumero(formaCuenta.getVerificacion("numero").toString());
			dtoSubCuentas[0].getVerificacionDerechos().setPersonaSolicita(formaCuenta.getVerificacion("personaSolicita").toString());
			dtoSubCuentas[0].getVerificacionDerechos().setFechaSolicitud(formaCuenta.getVerificacion("fechaSolicitud").toString());
			dtoSubCuentas[0].getVerificacionDerechos().setHoraSolicitud(formaCuenta.getVerificacion("horaSolicitud").toString());
			dtoSubCuentas[0].getVerificacionDerechos().setPersonaContactada(formaCuenta.getVerificacion("personaContactada").toString());
			dtoSubCuentas[0].getVerificacionDerechos().setFechaVerificacion(formaCuenta.getVerificacion("fechaVerificacion").toString());
			dtoSubCuentas[0].getVerificacionDerechos().setHoraVerificacion(formaCuenta.getVerificacion("horaVerificacion").toString());
			dtoSubCuentas[0].getVerificacionDerechos().setPorcentajeCobertura(formaCuenta.getVerificacion("porcentajeCobertura").toString());
			dtoSubCuentas[0].getVerificacionDerechos().setCuotaVerificacion(formaCuenta.getVerificacion("cuotaVerificacion").toString());
			dtoSubCuentas[0].getVerificacionDerechos().setObservaciones(formaCuenta.getVerificacion("observaciones").toString());
			dtoSubCuentas[0].getVerificacionDerechos().setLoginUsuario(medico.getLoginUsuario());
		}
		
		//****************************************************************************************************************************
		//**********************SE LLENAN LOS DATOS DE LOS OTROS CONVENIOS*************************************************************
		//*****************************************************************************************************************************
		int cont = 1; //se inicia el contador en 1
		
		for(int i=0;i<Integer.parseInt(formaCuenta.getVariosConvenios("numRegistros").toString());i++)
		{
			
				dtoSubCuentas[cont] = new DtoSubCuentas();
				dtoSubCuentas[cont].setIngreso(Integer.parseInt(formaCuenta.getCuenta("idIngreso").toString()));
				dtoSubCuentas[cont].setSubCuenta(formaCuenta.getVariosConvenios("idSubCuenta_"+i).toString());
				dtoSubCuentas[cont].setConvenio(new InfoDatosInt(Integer.parseInt(formaCuenta.getVariosConvenios("codigoConvenio_"+i).toString()),""));
				dtoSubCuentas[cont].setCodigoTipoRegimen(formaCuenta.getVariosConvenios("codigoTipoRegimen_"+i).toString());
				dtoSubCuentas[cont].setCodigoTipoCobertura(Utilidades.convertirAEntero(formaCuenta.getVariosConvenios("codigoTipoCobertura_"+i).toString()));
				dtoSubCuentas[cont].setContrato(Integer.parseInt(formaCuenta.getVariosConvenios("codigoContrato_"+i).toString()));
				mundoContrato = new Contrato();
				mundoContrato.cargar(con, dtoSubCuentas[cont].getContrato()+"");
				
				if(UtilidadTexto.getBoolean(formaCuenta.getVariosConvenios("esConvenioSoat_"+i).toString()))
					dtoSubCuentas[cont].setValorUtilizadoSoat(formaCuenta.getVariosConvenios("valorUtilizadoSoat_"+i).toString());
				
				dtoSubCuentas[cont].setFechaAfiliacion(formaCuenta.getVariosConvenios("fechaAfiliacion_"+i).toString());
				
				if(!formaCuenta.getVariosConvenios("semanasCotizacion_"+i).toString().equals(""))
					dtoSubCuentas[cont].setSemanasCotizacion(Integer.parseInt(formaCuenta.getVariosConvenios("semanasCotizacion_"+i).toString()));
				if(!formaCuenta.getVariosConvenios("mesesCotizacion_"+i).toString().equals(""))
					dtoSubCuentas[cont].setMesesCotizacion(Integer.parseInt(formaCuenta.getVariosConvenios("mesesCotizacion_"+i).toString()));
				
				
				
				
				
				validacion=servicioValidacion.validarTipoCobroPacienteServicioConvenioContrato(Integer.parseInt(formaCuenta.getVariosConvenios("codigoContrato_"+i).toString()));

				dtoSubCuentas[cont].setTipoCobroPaciente(validacion.getTipoCobroPaciente());

				
				if(UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion()))
					dtoSubCuentas[cont].setClasificacionSocioEconomica(Integer.parseInt(formaCuenta.getVariosConvenios("codigoEstratoSocial_"+i).toString()));
				else
					dtoSubCuentas[cont].setClasificacionSocioEconomica(ConstantesBD.codigoNuncaValido);
				if(UtilidadTexto.getBoolean(validacion.getMostrarTipoAfiliado()))
					dtoSubCuentas[cont].setTipoAfiliado(formaCuenta.getVariosConvenios("codigoTipoAfiliado_"+i).toString());
				else
					dtoSubCuentas[cont].setTipoAfiliado("");
				if(UtilidadTexto.getBoolean(validacion.getMostrarNaturalezaPaciente()))
					dtoSubCuentas[cont].setNaturalezaPaciente(Integer.parseInt(formaCuenta.getVariosConvenios("codigoNaturaleza_"+i).toString()));
				else
					dtoSubCuentas[cont].setNaturalezaPaciente(ConstantesBD.codigoNuncaValido);
				
				if(UtilidadTexto.getBoolean(validacion.getManejaMontos()))
				{
					int montoCobroTempo=Integer.parseInt(formaCuenta.getVariosConvenios("codigoMontoCobro_"+i).toString());
					dtoSubCuentas[cont].setMontoCobro(montoCobroTempo);
					IMontosCobroServicio servicioMontoCobro=FacturacionServicioFabrica.crearMontosCobroServicio();
					DTOResultadoBusquedaDetalleMontos dtoMonto=servicioMontoCobro.obtenerDetalleMontoCobroPorId(montoCobroTempo);
					dtoSubCuentas[cont].setTipoMontoCobro(dtoMonto.getTipoDetalleAcronimo());
				}
				else
				{
					dtoSubCuentas[cont].setMontoCobro(ConstantesBD.codigoNuncaValido);
					dtoSubCuentas[cont].setPorcentajeMontoCobro(validacion.getPorcentajeMontoCobro());
				}
				
				
				
				
				dtoSubCuentas[cont].setNroCarnet(formaCuenta.getVariosConvenios("numeroCarnet_"+i).toString());
				dtoSubCuentas[cont].setNroPoliza(formaCuenta.getVariosConvenios("numeroPoliza_"+i).toString());
				dtoSubCuentas[cont].setNroAutorizacion(formaCuenta.getVariosConvenios("autorizacionIngreso_"+i).toString());
				dtoSubCuentas[cont].setCodigoPaciente(paciente.getCodigoPersona());
				dtoSubCuentas[cont].setFacturado(ConstantesBD.acronimoNo);
				dtoSubCuentas[cont].setLoginUsuario(medico.getLoginUsuario());
				
				
				if(UtilidadTexto.getBoolean(formaCuenta.getVariosConvenios("esConvenioColsanitas_"+i).toString()))
					dtoSubCuentas[cont].setNumeroSolicitudVolante(formaCuenta.getVariosConvenios("numeroSolicitudVolante_"+i).toString());
				else
					dtoSubCuentas[cont].setNumeroSolicitudVolante("");
				
				//Seccion de informacion de la poliza si aplica
				dtoSubCuentas[cont].setSubCuentaPoliza(UtilidadTexto.getBoolean(formaCuenta.getVariosConvenios("esConvenioPoliza_"+i).toString()));
				if(dtoSubCuentas[cont].isSubCuentaPoliza())
				{
					dtoSubCuentas[cont].getTitularPoliza().setExisteBd(true);
					dtoSubCuentas[cont].getTitularPoliza().setApellidos(formaCuenta.getVariosConvenios("apellidosPoliza_"+i).toString());
					dtoSubCuentas[cont].getTitularPoliza().setNombres(formaCuenta.getVariosConvenios("nombresPoliza_"+i).toString());
					dtoSubCuentas[cont].getTitularPoliza().setCodigoTipoIdentificacion(formaCuenta.getVariosConvenios("tipoIdPoliza_"+i).toString());
					dtoSubCuentas[cont].getTitularPoliza().setNumeroIdentificacion(formaCuenta.getVariosConvenios("numeroIdPoliza_"+i).toString());
					dtoSubCuentas[cont].getTitularPoliza().setDireccion(formaCuenta.getVariosConvenios("direccionPoliza_"+i).toString());
					dtoSubCuentas[cont].getTitularPoliza().setTelefono(formaCuenta.getVariosConvenios("telefonoPoliza_"+i).toString());
					
					for(int j=0;j<Integer.parseInt(formaCuenta.getVariosConvenios("numDatosPoliza_"+i).toString());j++)
					{
						dtoSubCuentas[cont].getTitularPoliza().setInformacionPoliza(
								formaCuenta.getVariosConvenios("codigoPoliza_"+i+"_"+j).toString(),
								UtilidadFecha.conversionFormatoFechaABD(formaCuenta.getVariosConvenios("fechaPoliza_"+i+"_"+j).toString()), 
								formaCuenta.getVariosConvenios("autorizacionPoliza_"+i+"_"+j).toString(), 
								formaCuenta.getVariosConvenios("valorPoliza_"+i+"_"+j).toString(),
								medico.getLoginUsuario(),
								true,
								false);
					}
					
					
					
				}
				for(int j=0;j<Integer.parseInt(formaCuenta.getVariosConvenios("numReqIngreso_"+i).toString());j++)
				{
					DtoRequsitosPaciente requisitos = new DtoRequsitosPaciente();
					requisitos.setCodigo(Integer.parseInt(formaCuenta.getVariosConvenios("codigoReqIngreso_"+i+"_"+j).toString()));
					requisitos.setDescripcion(formaCuenta.getVariosConvenios("descripcionReqIngreso_"+i+"_"+j).toString());
					requisitos.setTipo(ConstantesIntegridadDominio.acronimoIngreso);
					requisitos.setCumplido(UtilidadTexto.getBoolean(formaCuenta.getVariosConvenios("cumplidoReqIngreso_"+i+"_"+j).toString()));
					
					dtoSubCuentas[cont].getRequisitosPaciente().add(requisitos);
				}
				for(int j=0;j<Integer.parseInt(formaCuenta.getVariosConvenios("numReqEgreso_"+i).toString());j++)
				{
					DtoRequsitosPaciente requisitos = new DtoRequsitosPaciente();
					requisitos.setCodigo(Integer.parseInt(formaCuenta.getVariosConvenios("codigoReqEgreso_"+i+"_"+j).toString()));
					requisitos.setDescripcion(formaCuenta.getVariosConvenios("descripcionReqEgreso_"+i+"_"+j).toString());
					requisitos.setTipo(ConstantesIntegridadDominio.acronimoEgreso);
					requisitos.setCumplido(UtilidadTexto.getBoolean(formaCuenta.getVariosConvenios("cumplidoReqEgreso_"+i+"_"+j).toString()));
					
					dtoSubCuentas[cont].getRequisitosPaciente().add(requisitos);
				}
				
				//Se llena la verificacion de derechos
				if(UtilidadTexto.getBoolean(formaCuenta.getVariosConvenios("tieneVerificacionDerechos_"+i).toString())||
					UtilidadTexto.getBoolean(formaCuenta.getVariosConvenios("ingresoVerificacionDerechos_"+i).toString()))
				{
					
					dtoSubCuentas[cont].setSubCuentaVerificacionDerechos(true);
					
					//Si ten�a verificacion de derechos se a�ade el id de la subCuenta
					if(UtilidadTexto.getBoolean(formaCuenta.getVariosConvenios("tieneVerificacionDerechos_"+i).toString()))
					{
						
						dtoSubCuentas[cont].getVerificacionDerechos().setSubCuenta(formaCuenta.getVariosConvenios("idSubCuenta_"+i).toString());
						
						//Si exist�a verificacion de derechos y se borr� la informacion se debe eliminar
						if(!UtilidadTexto.getBoolean(formaCuenta.getVariosConvenios("ingresoVerificacionDerechos_"+i).toString()))
							dtoSubCuentas[cont].getVerificacionDerechos().setEliminar(true);
					}
						
					dtoSubCuentas[cont].getVerificacionDerechos().setCodigoEstado(formaCuenta.getVariosConvenios("codigoEstado_"+i).toString());
					dtoSubCuentas[cont].getVerificacionDerechos().setCodigoTipo(formaCuenta.getVariosConvenios("codigoTipo_"+i).toString());
					dtoSubCuentas[cont].getVerificacionDerechos().setNumero(formaCuenta.getVariosConvenios("numero_"+i).toString());
					dtoSubCuentas[cont].getVerificacionDerechos().setPersonaSolicita(formaCuenta.getVariosConvenios("personaSolicita_"+i).toString());
					dtoSubCuentas[cont].getVerificacionDerechos().setFechaSolicitud(formaCuenta.getVariosConvenios("fechaSolicitud_"+i).toString());
					dtoSubCuentas[cont].getVerificacionDerechos().setHoraSolicitud(formaCuenta.getVariosConvenios("horaSolicitud_"+i).toString());
					dtoSubCuentas[cont].getVerificacionDerechos().setPersonaContactada(formaCuenta.getVariosConvenios("personaContactada_"+i).toString());
					dtoSubCuentas[cont].getVerificacionDerechos().setFechaVerificacion(formaCuenta.getVariosConvenios("fechaVerificacion_"+i).toString());
					dtoSubCuentas[cont].getVerificacionDerechos().setHoraVerificacion(formaCuenta.getVariosConvenios("horaVerificacion_"+i).toString());
					dtoSubCuentas[cont].getVerificacionDerechos().setPorcentajeCobertura(formaCuenta.getVariosConvenios("porcentajeCobertura_"+i).toString());
					dtoSubCuentas[cont].getVerificacionDerechos().setCuotaVerificacion(formaCuenta.getVariosConvenios("cuotaVerificacion_"+i).toString());
					dtoSubCuentas[cont].getVerificacionDerechos().setObservaciones(formaCuenta.getVariosConvenios("observaciones_"+i).toString());
					dtoSubCuentas[cont].getVerificacionDerechos().setLoginUsuario(medico.getLoginUsuario());
				}
				
				
				
				cont++;
			
			
		}
		
		
		
		//****************************************************************************************************************************
		//**********************SE LLENAN LOS DATOS DEL RESPONSABLE*********************************************************************
		//*****************************************************************************************************************************
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("ingresoResponsablePaciente").toString())||
				UtilidadTexto.getBoolean(formaCuenta.getCuenta("tieneResponsablePaciente").toString()))
		{
			logger.info("DEBO INGRESAR RESOPNSABLE PACIENTE!!!!!!!!!!!!!!!");
			dtoCuenta.setTieneResponsablePaciente(true);
			
			//Si se ten�a responsable pero no se ingres� se debe eliminar de la cuenta
			if(!UtilidadTexto.getBoolean(formaCuenta.getCuenta("ingresoResponsablePaciente").toString())&&
					UtilidadTexto.getBoolean(formaCuenta.getCuenta("tieneResponsablePaciente").toString()))
				dtoCuenta.getResponsablePaciente().setEliminar(true);
				
			dtoCuenta.getResponsablePaciente().setCodigo(formaCuenta.getResponsable("codigo")==null?"":formaCuenta.getResponsable("codigo").toString());
			dtoCuenta.getResponsablePaciente().setTipoIdentificacion(formaCuenta.getResponsable("codigoTipoIdentificacion").toString());
			dtoCuenta.getResponsablePaciente().setNumeroIdentificacion(formaCuenta.getResponsable("numeroIdentificacion").toString());
			dtoCuenta.getResponsablePaciente().setCodigoPaisExpedicion(formaCuenta.getResponsable("paisExpedicion").toString());
			String[] vector = formaCuenta.getResponsable("ciudadExpedicion").toString().split(ConstantesBD.separadorSplit);
			if(vector.length>1)
			{
				dtoCuenta.getResponsablePaciente().setCodigoDeptoExpedicion(vector[0]);
				dtoCuenta.getResponsablePaciente().setCodigoCiudadExpedicion(vector[1]);
			}
			dtoCuenta.getResponsablePaciente().setPrimerApellido(formaCuenta.getResponsable("primerApellido").toString());
			dtoCuenta.getResponsablePaciente().setSegundoApellido(formaCuenta.getResponsable("segundoApellido").toString());
			dtoCuenta.getResponsablePaciente().setPrimerNombre(formaCuenta.getResponsable("primerNombre").toString());
			dtoCuenta.getResponsablePaciente().setSegundoNombre(formaCuenta.getResponsable("segundoNombre").toString());
			dtoCuenta.getResponsablePaciente().setDireccion(formaCuenta.getResponsable("direccion").toString());
			dtoCuenta.getResponsablePaciente().setCodigoPais(formaCuenta.getResponsable("pais").toString());
			vector = formaCuenta.getResponsable("ciudad").toString().split(ConstantesBD.separadorSplit);
			if(vector.length>1)
			{
				dtoCuenta.getResponsablePaciente().setCodigoDepto(vector[0]);
				dtoCuenta.getResponsablePaciente().setCodigoCiudad(vector[1]);
			}
			dtoCuenta.getResponsablePaciente().setCodigoBarrio(formaCuenta.getResponsable("codigoBarrio").toString());
			dtoCuenta.getResponsablePaciente().setTelefono(formaCuenta.getResponsable("telefono").toString());
			dtoCuenta.getResponsablePaciente().setFechaNacimiento(formaCuenta.getResponsable("fechaNacimiento").toString());
			dtoCuenta.getResponsablePaciente().setRelacionPaciente(formaCuenta.getResponsable("relacionPaciente").toString());
			dtoCuenta.getResponsablePaciente().setLoginUsuario(medico.getLoginUsuario());
			
			
		}
		else
			dtoCuenta.setTieneResponsablePaciente(false);
		
		
		dtoCuenta.setConvenios(dtoSubCuentas);
		UtilidadTransaccion.getTransaccion().commit();
	}

	/**
	 * M�todo que realiza las validaciones de campos antes de guardar la cuenta
	 * @param con 
	 * @param formaCuenta
	 * @param medico
	 * @param errores 
	 * @return
	 */
	private ActionErrors validacionesGuardarCuenta(Connection con, ModificarCuentaForm formaCuenta, UsuarioBasico medico, ActionErrors errores) 
	{
		String aux0 = "", aux1 = "";
		//Convenio
		if(formaCuenta.getCuenta("codigoConvenio").toString().equals(""))
			errores.add("", new ActionMessage("errors.required","El convenio"));
		
		//Contrato
		if(formaCuenta.getCuenta("codigoContrato").toString().equals(""))
			errores.add("", new ActionMessage("errors.required","El contrato"));
		
		//Semanas cotizacion
		if(!formaCuenta.getCuenta("semanasCotizacion").toString().equals("")&&Utilidades.convertirAEntero(formaCuenta.getCuenta("semanasCotizacion").toString())==ConstantesBD.codigoNuncaValido)
			errores.add("", new ActionMessage("errors.integer","El campo semanas de cotizaci�n"));
		
		//Meses cotizacion
		if(!formaCuenta.getCuenta("mesesCotizacion").toString().equals("")&&Utilidades.convertirAEntero(formaCuenta.getCuenta("mesesCotizacion").toString())==ConstantesBD.codigoNuncaValido)
			errores.add("", new ActionMessage("errors.integer","El campo meses de cotizaci�n"));
		
		//Tipo de regimen
		if(formaCuenta.getCuenta("codigoTipoRegimen").toString().equals(""))
			errores.add("", new ActionMessage("errors.required","El tipo de r�gimen"));
		
		IValidacionTipoCobroPacienteServicio servicioValidacion=FacturacionServicioFabrica.crearValidacionTipoCobroPacienteServicio();
		Log4JManager.warning(">>>>>>>>>>>>>>>>>>>>>>>>>> Codigo cont: " + formaCuenta.getCuenta("codigoContrato")+"<<<<<<<<<<<<<<<<<<");
		DtoValidacionTipoCobroPaciente validacion=servicioValidacion.validarTipoCobroPacienteServicioConvenioContrato(Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoContrato")+""));
		
		if(UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion()))
		{
			//CLasificacion Socio. Econ�mica
			if(formaCuenta.getCuenta("codigoEstratoSocial").toString().equals(""))
				errores.add("", new ActionMessage("errors.required","La clasificaci�n socioecon�mica"));
		}
		
		
		if(UtilidadTexto.getBoolean(validacion.getMostrarTipoAfiliado()))
		{
			//Tipo Afiliado
			if(formaCuenta.getCuenta("codigoTipoAfiliado").toString().equals(""))
				errores.add("", new ActionMessage("errors.required","El tipo de afiliado"));
		}
		
		if(UtilidadTexto.getBoolean(validacion.getManejaMontos()))
		{
			//Monto de cobro
			if(formaCuenta.getCuenta("codigoMontoCobro").toString().equals(""))
				errores.add("", new ActionMessage("errors.required","El monto de cobro"));
		}	
		//Tipo de cobertura
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("esReporteAtencionInicialUrgencias").toString())&&
			UtilidadTexto.getBoolean(formaCuenta.getCuenta("puedoModificarConvenio").toString())&&
			formaCuenta.getCuenta("codigoTipoCobertura").toString().equals(""))
		{
			errores.add("", new ActionMessage("errors.required","El tipo de cobertura"));
		}
		
		//Tipo Paciente
		if(formaCuenta.getCuenta("codigoTipoPaciente").toString().equals(""))
			errores.add("", new ActionMessage("errors.required","El tipo de paciente"));
		
		//N�mero de Carnet
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("requiereCarnet").toString())&&
			formaCuenta.getCuenta("numeroCarnet").toString().equals(""))
			errores.add("",new ActionMessage("errors.required","el n�mero de carnet"));
		//Origen Admision
		if(formaCuenta.getCuenta("codigoOrigenAdmision").toString().equals(""))
			errores.add("",new ActionMessage("errors.required","el origen de la admisi�n"));
		//�rea
		if(formaCuenta.getCuenta("codigoArea").toString().equals(""))
			errores.add("",new ActionMessage("errors.required","el �rea"));
		
		//Verificaci�n del tipo de monitoreo
		HashMap tiposMonitoreo = Utilidades.consultaTipoMonitoreoxCC(con, Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoArea").toString()));
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("existeIngresoCuidadoEspecial").toString())&&
			UtilidadTexto.isEmpty(formaCuenta.getCuenta("codigoTipoMonitoreo").toString())&&
			Utilidades.convertirAEntero(tiposMonitoreo.get("numRegistros")+"")>0
			)
			errores.add("",new ActionMessage("errors.required","Existe un ingreso a cuidados especiales activo, por tal motivo, el tipo de monitoreo"));
	
		//Validacion del n�mero de solicitud volante (S� aplica)
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("esConvenioColsanitas").toString()))
			if(!formaCuenta.getCuenta("numeroSolicitudVolante").toString().equals(""))
			{
				try
				{
					Long.parseLong(formaCuenta.getCuenta("numeroSolicitudVolante").toString());
				}
				catch(Exception e)
				{
					logger.warn("Error al convertir a num�rico el n�mero de solicitud volante: "+e);
					errores.add("",new ActionMessage("errors.integer","N�mero Solicitud Volante"));
				}
			}
		
		/*
		//Validaciones propias de INFORMACION POLIZA (cuando es asocio no se valida)
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("esConvenioPoliza").toString())&&
			UtilidadTexto.getBoolean(formaCuenta.getCuenta("puedoModificarConvenio").toString()))
		{
			//Apellidos del titular
			if(formaCuenta.getCuenta("apellidosPoliza").toString().equals(""))
				errores.add("Apellidos poliza requerido",new ActionMessage("errors.required","El apellido del titular"));
			//Nombres del titular
			if(formaCuenta.getCuenta("nombresPoliza").toString().equals(""))
				errores.add("Nombres poliza requerido",new ActionMessage("errors.required","El nombre del titular"));
			//Tipo Id. titular
			if(formaCuenta.getCuenta("tipoIdPoliza").toString().equals(""))
				errores.add("Tipo ID poliza requerido",new ActionMessage("errors.required","El tipo de identificaci�n del titular"));
			//N�mero Id. titular
			if(formaCuenta.getCuenta("numeroIdPoliza").toString().equals(""))
				errores.add("Numero ID poliza requerido",new ActionMessage("errors.required","El n�mero de identificaci�n del titular"));
			//Direccion Titular
			if(formaCuenta.getCuenta("direccionPoliza").toString().equals(""))
				errores.add("direccion poliza requerido",new ActionMessage("errors.required","La direcci�n del titular"));
			
			int cont = 1;
			for(int i=0;i<Integer.parseInt(formaCuenta.getCuenta("numDatosPoliza").toString());i++)
			{
				if(!UtilidadTexto.getBoolean(formaCuenta.getCuenta("eliminarPoliza_"+i).toString()))
				{
					//Autorizacion Poliza
					if(formaCuenta.getCuenta("autorizacionPoliza_"+i).toString().equals(""))
						errores.add("direccion poliza requerido",new ActionMessage("errors.required","El n�mero de autorizaci�n de la p�liza (registro No. "+cont+")"));
					//valor Poliza
					if(formaCuenta.getCuenta("valorPoliza_"+i).toString().equals(""))
						errores.add("valor poliza requerido",new ActionMessage("errors.required","El valor para el monto de la p�liza (registro No. "+cont+")"));
					else
					{
						try
						{
							if(Double.parseDouble(formaCuenta.getCuenta("valorPoliza_"+i).toString())<=0)
								errores.add("Debe ser num�rico", new ActionMessage("errors.MayorQue","El valor para el monto de la p�liza (registro No. "+cont+")","0"));
						}
						catch(Exception e)
						{
							errores.add("Debe ser num�rico", new ActionMessage("errors.float","El valor para el monto de la p�liza (registro No. "+cont+")"));
						}
					}
					
					cont ++;
				}
			}
			
		}
		
		*/
		//************************************************************************************************
		//*******VALIDACIONES SECCION VERIFICACION DE DERECHOS**************************************************************
		//1) se verifica si la verificacion de derechos es requerida
		boolean esRequeridoVerificacion = false;
		if(!formaCuenta.getCuenta("codigoViaIngreso").toString().equals("")&&
				formaCuenta.getCuenta("esViaIngresoVerificacion").toString().equals(ConstantesBD.acronimoSi)&&
				formaCuenta.getCuenta("esConvenioVerificacion").toString().equals(ConstantesBD.acronimoSi))
			esRequeridoVerificacion = true;
		
		//2) Se verifica si se ingres� informaci�n requerida de la verificacion de derechos
		boolean seIngresoVerificacion = false;
		if(!esRequeridoVerificacion)
		{
			//Si se edit� informacion en el estado y tipo de verificacion , quiere decir que se tuvo la intenci�n
			//de ingresar verificacion de derechos
			if(!formaCuenta.getVerificacion("codigoEstado").toString().equals("")||
				!formaCuenta.getVerificacion("codigoTipo").toString().equals(""))
				seIngresoVerificacion = true;
		}
		
		//Si la verificacion de derechos es requerida o sin ser requerida se intent� ingresar,
		//se prosigue a realizar sus valdiaciones
		if(esRequeridoVerificacion||seIngresoVerificacion)
		{
			formaCuenta.setCuenta("ingresoVerificacionDerechos", ConstantesBD.acronimoSi);
			
			//estado de verificacion
			if(formaCuenta.getVerificacion("codigoEstado").toString().equals(""))
				errores.add("El estado es requerido",new ActionMessage("errors.required","El estado (Verificaci�n de Derechos)"));
			//tipo de verificacion
			if(formaCuenta.getVerificacion("codigoTipo").toString().equals(""))
				errores.add("El tipo es requerido", new ActionMessage("errors.required","El tipo (Verificaci�n de Derechos)"));
			//persona solicita
			if(formaCuenta.getVerificacion("personaSolicita").toString().equals(""))
				errores.add("la persona solicita es requerido", new ActionMessage("errors.required","Persona Solicita (Verificaci�n de Derechos)"));
			//persona contactada
			if(!formaCuenta.getVerificacion("codigoEstado").toString().equals("")&&
				!formaCuenta.getVerificacion("codigoEstado").toString().equals(ConstantesIntegridadDominio.acronimoEstadoPendientePorVerificar)&&
				formaCuenta.getVerificacion("personaContactada").toString().equals(""))
				errores.add("la persona contactada es requerido", new ActionMessage("errors.required","Persona contactada (Verificaci�n de Derechos)"));
			
			//Validar la fecha de verificacion
			boolean fechaValida = true;
			aux0 = formaCuenta.getVerificacion("fechaVerificacion").toString();
			if(aux0.equals(""))
			{
				errores.add("",new ActionMessage("errors.required","la fecha verificaci�n (Verificaci�n de Derechos)"));
				fechaValida = false;
			}
			else
			{
				if(!UtilidadFecha.validarFecha(aux0))
				{
					errores.add("",new ActionMessage("errors.formatoFechaInvalido","de verificaci�n (Verificaci�n de Derechos)"));
					fechaValida = false;
				}
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(aux0, UtilidadFecha.getFechaActual()))
				{
					errores.add("",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de verificaci�n (Verificaci�n de Derechos)", "actual"));
					fechaValida = false;
				}
			}
			
			//Validar la hora de verificaci�n
			aux1 = formaCuenta.getVerificacion("horaVerificacion").toString();
			if(aux1.equals(""))
				errores.add("",new ActionMessage("errors.required","la hora de verificaci�n (Verificaci�n de Derechos)"));
			else
			{
				if(!UtilidadFecha.validacionHora(aux1).puedoSeguir)
					errores.add("",new ActionMessage("errors.formatoHoraInvalido","de verificaci�n (Verificaci�n de Derechos)"));
				else if(fechaValida&&!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), aux0, aux1).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraPosteriorIgualActual", "de verificaci�n (Verificaci�n de Derechos)", "actual"));
					
			}
			
			//Validacion del porcentaje de cobertura
			if(!formaCuenta.getVerificacion("porcentajeCobertura").toString().equals(""))
			{
				try
				{
					if(Double.parseDouble(formaCuenta.getVerificacion("porcentajeCobertura").toString())>100)
						errores.add("",new ActionMessage("errors.MenorIgualQue","El porcentaje de cobertura (Verificaci�n de Derechos)","100%"));
				}
				catch(Exception e)
				{
					errores.add("",new ActionMessage("errors.float","El porcentaje de cobertura (Verificaci�n de Derechos)"));
				}
				
			}
		}
		else
			formaCuenta.setCuenta("ingresoVerificacionDerechos", ConstantesBD.acronimoNo);
		//***********************************************************************************************************
		//*****************VALIDACIONES SECCION RESPONSABLE PACIENTE*************************************************
		//Solo es requerido si la via de ingreso lo tiene parametrizado o se intent� ingresar el responsable
		if(formaCuenta.getResponsable("codigoTipoIdentificacion")!=null&&!formaCuenta.getResponsable("codigoTipoIdentificacion").toString().equals("")&&
					formaCuenta.getResponsable("numeroIdentificacion")!=null&&!formaCuenta.getResponsable("numeroIdentificacion").toString().equals(""))
		{
			logger.info("S� DEBO VALIDAR RESPONSABLE PACIENTE!!!!!!!!!!!!!!!!!!");
			formaCuenta.setCuenta("ingresoResponsablePaciente", ConstantesBD.acronimoSi);
			
			if(formaCuenta.getResponsable("primerApellido").toString().equals(""))
				errores.add("",new ActionMessage("errors.required","El primer apellido (Responsable Paciente)"));
			
			if(formaCuenta.getResponsable("primerNombre").toString().equals(""))
				errores.add("",new ActionMessage("errors.required","El primer nombre (Responsable Paciente)"));
			
			if(formaCuenta.getResponsable("telefono").toString().equals(""))
				errores.add("",new ActionMessage("errors.required","El tel�fono (Responsable Paciente)"));
			
			if(formaCuenta.getResponsable("fechaNacimiento").toString().equals(""))
				errores.add("",new ActionMessage("errors.required","La fecha de nacimiento (Responsable Paciente)"));
			else
			{
				aux0 = formaCuenta.getResponsable("fechaNacimiento").toString();
				if(!UtilidadFecha.validarFecha(aux0))
					errores.add("",new ActionMessage("errors.formatoFechaInvalido","de nacimiento (Responsable Paciente)"));
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(aux0, UtilidadFecha.getFechaActual()))
					errores.add("",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de nacimiento (Responsable Paciente)", "actual"));
				else
				{
					boolean validarEdadResponsable = UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarEdadResponsablePaciente(medico.getCodigoInstitucionInt()));
					if(validarEdadResponsable)
					{
						int aniosBase = 0;
						try
						{
							aniosBase = Integer.parseInt(ValoresPorDefecto.getAniosBaseEdadAdulta(medico.getCodigoInstitucionInt()));
						}
						catch (Exception e) 
						{
							aniosBase = 0;
							logger.error("Error al tomar el par�metro A�OS BASE EDAD ADULTA: "+e);
						}
						if(aniosBase>0)
						{
							int edad = UtilidadFecha.calcularEdad(aux0);
							if(edad<aniosBase)
								errores.add("",new ActionMessage("errors.debeSerNumeroMayorIgual","La edad del responsable paciente ("+edad+" a�os)",aniosBase+" a�os"));
						}
					}
				}
				
			}
			
			if(formaCuenta.getResponsable("relacionPaciente").toString().equals(""))
				errores.add("",new ActionMessage("errors.required","La relaci�n con el paciente (Responsable Paciente)"));
			
			//Si el responsable fue cambiado por otro, se reporta como eliminado
			if(!formaCuenta.getResponsable("codigoOriginal").toString().equals("")&&
				!formaCuenta.getResponsable("codigo").toString().equals(formaCuenta.getResponsable("codigoOriginal").toString()))
				formaCuenta.setFueEliminadoResponsable(true);
			else
				formaCuenta.setFueEliminadoResponsable(false);
			
			//Si el responsable ya exist�a se verifica si ya fue modificado
			if(UtilidadTexto.getBoolean(formaCuenta.getResponsable("existe").toString()))
			{
				DtoResponsablePaciente responsableOriginal = UtilidadesManejoPaciente.cargarResponsablePaciente(
					con, 
					formaCuenta.getResponsable("codigoTipoIdentificacion").toString(), 
					formaCuenta.getResponsable("numeroIdentificacion").toString());
				formaCuenta.setFueModificadoResponsable(huboModificacionResponsablePaciente(responsableOriginal,formaCuenta.getResponsable()));
			}
			else
				formaCuenta.setFueModificadoResponsable(false);
		}
		else
		{
			formaCuenta.setCuenta("ingresoResponsablePaciente", ConstantesBD.acronimoNo);
			
			//Si el responsable exist�a quiere decir que fue eliminado
			if(UtilidadTexto.getBoolean(formaCuenta.getResponsable("existe").toString()))
				formaCuenta.setFueEliminadoResponsable(true);
			else
				formaCuenta.setFueEliminadoResponsable(false);
			
			formaCuenta.setFueModificadoResponsable(false);
		}
		
		return errores;
	}

	/**
	 * M�todo que verifica si hubo modificaci�n del responsable del paciente
	 * @param responsableOriginal
	 * @param responsable
	 * @return
	 */
	private boolean huboModificacionResponsablePaciente(DtoResponsablePaciente responsableOriginal, HashMap<String, Object> responsable) 
	{
		boolean modificado = false;
		
		//Se modific� pais de expedicion?
		if(!responsable.get("paisExpedicion").toString().equals(responsableOriginal.getCodigoPaisExpedicion()))
			modificado = true;
		//Se modific� ciudad de expedicion ?
		if(!responsable.get("ciudadExpedicion").toString().equals(responsableOriginal.getCodigoDeptoExpedicion()+ConstantesBD.separadorSplit+responsableOriginal.getCodigoCiudadExpedicion()))
			modificado = true;
		//Se modific� el nombre o apellido ?
		if(!responsable.get("primerApellido").toString().equals(responsableOriginal.getPrimerApellido())||
			!responsable.get("segundoApellido").toString().equals(responsableOriginal.getSegundoApellido())||
			!responsable.get("primerNombre").toString().equals(responsableOriginal.getPrimerNombre())||
			!responsable.get("segundoNombre").toString().equals(responsableOriginal.getSegundoNombre()))
			modificado = true;
		//Se modific� la direccion ?
		if(!responsable.get("direccion").toString().equals(responsableOriginal.getDireccion()))
			modificado = true;
		
		//Se modifcic� el pais ?
		if(!responsable.get("pais").toString().equals(responsableOriginal.getCodigoPais()))
			modificado = true;
		//se modific� la ciudad ?
		if(!responsable.get("ciudad").toString().equals(responsableOriginal.getCodigoDepto()+ConstantesBD.separadorSplit+responsableOriginal.getCodigoCiudad()))
			modificado = true;
		//se modific� el barrio
		if(!responsable.get("codigoBarrio").toString().equals(responsableOriginal.getCodigoBarrio()))
			modificado = true;
		//se modifc� el tel�fono ?
		if(!responsable.get("telefono").toString().equals(responsableOriginal.getTelefono()))
			modificado = true;
		//se modific� la fecha de nacimiento ?
		if(!responsable.get("fechaNacimiento").toString().equals(responsableOriginal.getFechaNacimiento()))
			modificado = true;
		//se modific�la relacion del paciente ?
		if(!responsable.get("relacionPaciente").toString().equals(responsableOriginal.getRelacionPaciente()))
			modificado = true;
		
		return modificado;
	}

	private ActionForward accionFiltroEliminarMonto(Connection con, ModificarCuentaForm formaCuenta, HttpServletResponse response) 
	{
		int pos = formaCuenta.getPosicion();
		
		//Se elimina el registro
		formaCuenta.setCuenta("eliminarPoliza_"+pos, ConstantesBD.acronimoSi);
		
		//Se agrega la posicion a eliminar
		String aux = "<posicion>"+pos+"</posicion>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write(aux);
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroResponsable: "+e);
		}
		return null;
	}

	/**
	 * M�todo implementado para a�adir un nuevo registro al monto autorizado de la poliza
	 * @param con
	 * @param formaCuenta
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroAdicionarMonto(Connection con, ModificarCuentaForm formaCuenta, HttpServletResponse response) 
	{
		String aux = "";
		int pos = Integer.parseInt(formaCuenta.getCuenta("numDatosPoliza").toString());
		
		//Se adiciona el nuevo registro
		formaCuenta.setCuenta("codigoPoliza_"+pos, "");
		formaCuenta.setCuenta("fechaPoliza_"+pos, UtilidadFecha.getFechaActual(con));
		formaCuenta.setCuenta("autorizacionPoliza_"+pos, "");
		formaCuenta.setCuenta("valorPoliza_"+pos, "");
		formaCuenta.setCuenta("saldoPoliza_"+pos, "");
		formaCuenta.setCuenta("eliminarPoliza_"+pos, ConstantesBD.acronimoNo);
		formaCuenta.setCuenta("existePoliza_"+pos, ConstantesBD.acronimoNo);
		
		pos++;
		formaCuenta.setCuenta("numDatosPoliza", pos);
		aux += "<num-datos-poliza>"+pos+"</num-datos-poliza>"+
			"<fecha-poliza>"+UtilidadFecha.getFechaActual(con)+"</fecha-poliza>";
		
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write(aux);
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroResponsable: "+e);
		}
		return null;
	}

	/**
	 * M�todo que realiza el filtro de ciudades
	 * @param con
	 * @param formaCuenta
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroCiudades(Connection con, ModificarCuentaForm formaCuenta, HttpServletResponse response) 
	{
		String resultado = "<respuesta>";
		String codigoPais = "";
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		
		//Se filtran las ciudades segun estado
		if(formaCuenta.getEstado().equals("filtroCiudadesId"))
		{
			codigoPais = formaCuenta.getCodigoPaisId();
			formaCuenta.setCiudadesExp(Utilidades.obtenerCiudadesXPais(con, codigoPais));
			arregloAux = formaCuenta.getCiudadesExp();
		}
		else if(formaCuenta.getEstado().equals("filtroCiudadesResidencia"))
		{
			codigoPais = formaCuenta.getCodigoPaisResidencia();
			formaCuenta.setCiudades(Utilidades.obtenerCiudadesXPais(con, codigoPais));
			arregloAux = formaCuenta.getCiudades();
		}
		
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
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroCiudades: "+e);
		}
		return null;
	}

	/**
	 * M�todo que realiza el filtro del responsable
	 * @param con
	 * @param formaCuenta
	 * @param response
	 * @param paciente 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionFiltroResponsable(Connection con, ModificarCuentaForm formaCuenta, HttpServletResponse response, PersonaBasica paciente) 
	{
		HashMap mapaRespuesta = new HashMap();
		String aux = "", aux0 = "", aux1 = "";
		boolean esPaciente = false;
		
		//1) Se verifica si el responsable es el mismo paciente
		if(formaCuenta.getCodigoTipoIdResponsable().equals(paciente.getCodigoTipoIdentificacionPersona())&&
			formaCuenta.getNumeroIdResponsable().equals(paciente.getNumeroIdentificacionPersona()))
		{
			aux+="<error>El paciente no puede ser el responsable. Por favor verifique</error>";
			esPaciente = true;
			formaCuenta.setResponsable("editable", ConstantesBD.acronimoNo);
		}
		mapaRespuesta.put("respuestaPaciente",aux);
		
		//2) Se verifica si el responsable ya existe
		if(!esPaciente)
		{
			DtoResponsablePaciente responsable = UtilidadesManejoPaciente.cargarResponsablePaciente(con, formaCuenta.getCodigoTipoIdResponsable(), formaCuenta.getNumeroIdResponsable());
			
			if(!responsable.getCodigo().equals(""))
			{
				formaCuenta.setResponsable("existe", ConstantesBD.acronimoSi);
				formaCuenta.setResponsable("editable", ConstantesBD.acronimoSi);
				formaCuenta.setResponsable("codigo", responsable.getCodigo());
				formaCuenta.setResponsable("codigoTipoIdentificacion", responsable.getTipoIdentificacion());
				formaCuenta.setResponsable("numeroIdentificacion", responsable.getNumeroIdentificacion());
				formaCuenta.setResponsable("paisExpedicion", responsable.getCodigoPaisExpedicion());
				formaCuenta.setResponsable("ciudadExpedicion", responsable.getCodigoDeptoExpedicion()+ConstantesBD.separadorSplit+responsable.getCodigoCiudadExpedicion());
				formaCuenta.setResponsable("primerApellido", responsable.getPrimerApellido());
				formaCuenta.setResponsable("segundoApellido", responsable.getSegundoApellido());
				formaCuenta.setResponsable("primerNombre", responsable.getPrimerNombre());
				formaCuenta.setResponsable("segundoNombre", responsable.getSegundoNombre());
				formaCuenta.setResponsable("direccion", responsable.getDireccion());
				formaCuenta.setResponsable("pais", responsable.getCodigoPais());
				formaCuenta.setResponsable("ciudad", responsable.getCodigoDepto()+ConstantesBD.separadorSplit+responsable.getCodigoCiudad());
				formaCuenta.setResponsable("codigoBarrio", responsable.getCodigoBarrio());
				formaCuenta.setResponsable("nombreBarrio", responsable.getDescripcionBarrio());
				formaCuenta.setResponsable("telefono", responsable.getTelefono());
				formaCuenta.setResponsable("fechaNacimiento", responsable.getFechaNacimiento());
				formaCuenta.setResponsable("relacionPaciente", responsable.getRelacionPaciente());
				
				aux = "<datos-responsable>"+
					"<codigo>"+formaCuenta.getResponsable("codigo")+"</codigo>"+
					"<tipo-identificacion>"+formaCuenta.getResponsable("codigoTipoIdentificacion")+"</tipo-identificacion>"+
					"<numero-identificacion>"+formaCuenta.getResponsable("numeroIdentificacion")+"</numero-identificacion>"+
					"<pais-expedicion>"+responsable.getCodigoPaisExpedicion()+"</pais-expedicion>"+
					"<ciudad-expedicion>"+responsable.getCodigoDeptoExpedicion()+ConstantesBD.separadorSplit+responsable.getCodigoCiudadExpedicion()+"</ciudad-expedicion>"+
					"<primer-apellido>"+responsable.getPrimerApellido()+"</primer-apellido>"+
					"<segundo-apellido>"+responsable.getSegundoApellido()+"</segundo-apellido>"+
					"<primer-nombre>"+responsable.getPrimerNombre()+"</primer-nombre>"+
					"<segundo-nombre>"+responsable.getSegundoNombre()+"</segundo-nombre>"+
					"<direccion>"+responsable.getDireccion()+"</direccion>"+
					"<pais>"+responsable.getCodigoPais()+"</pais>"+
					"<ciudad>"+responsable.getCodigoDepto()+ConstantesBD.separadorSplit+responsable.getCodigoCiudad()+"</ciudad>"+
					"<codigo-barrio>"+responsable.getCodigoBarrio()+"</codigo-barrio>"+
					"<nombre-barrio>"+responsable.getDescripcionBarrio()+"</nombre-barrio>"+
					"<telefono>"+responsable.getTelefono()+"</telefono>"+
					"<fecha-nacimiento>"+responsable.getFechaNacimiento()+"</fecha-nacimiento>"+
					"<relacion-paciente>"+responsable.getRelacionPaciente()+"</relacion-paciente>"+
				"</datos-responsable>";
				
				logger.info("DESCRIPCION RELACION PACIENTE CONSULTADO=> "+responsable.getRelacionPaciente());
				
				//Se cargan las ciudades del pasi de expedicion
				if(!responsable.getCodigoPaisExpedicion().equals(""))
				{
					formaCuenta.setCiudadesExp(Utilidades.obtenerCiudadesXPais(con, responsable.getCodigoPaisExpedicion()));
					
					aux0 = "<ciudades-expedicion>";
					for(int i=0;i<formaCuenta.getCiudadesExp().size();i++)
					{
						aux0 += "<ciudad>";
						HashMap elemento = (HashMap)formaCuenta.getCiudadesExp().get(i);
						aux0 += "<codigo>"+elemento.get("codigoDepartamento")+ConstantesBD.separadorSplit+elemento.get("codigoCiudad")+"</codigo>";
						aux0 += "<nombre>"+elemento.get("nombreCiudad")+" ("+elemento.get("nombreDepartamento")+")</nombre>";
						aux0 += "</ciudad>";
					}
					aux0 += "</ciudades-expedicion>";
				}
				
				
				//Se cargan las ciudades del pais de residencia
				if(!responsable.getCodigoPais().equals(""))
				{
					formaCuenta.setCiudades(Utilidades.obtenerCiudadesXPais(con, responsable.getCodigoPais()));
					aux1 = "<ciudades-residencia>";
					
					for(int i=0;i<formaCuenta.getCiudades().size();i++)
					{
						aux1 += "<ciudad>";
						HashMap elemento = (HashMap)formaCuenta.getCiudades().get(i);
						aux1 += "<codigo>"+elemento.get("codigoDepartamento")+ConstantesBD.separadorSplit+elemento.get("codigoCiudad")+"</codigo>";
						aux1 += "<nombre>"+elemento.get("nombreCiudad")+" ("+elemento.get("nombreDepartamento")+")</nombre>";
						aux1 += "</ciudad>";
					}
					aux1 += "</ciudades-residencia>";
				}
				
			}
			else
			{
				formaCuenta.setResponsable("existe", ConstantesBD.acronimoNo);
				formaCuenta.setResponsable("editable", ConstantesBD.acronimoSi);
			}
		}
		mapaRespuesta.put("respuestaResponsable", aux);
		mapaRespuesta.put("respuestaCiudadesExpedicion", aux0);
		mapaRespuesta.put("respuestaCiudadesResidencia", aux1);
		
		aux = "<editable>"+formaCuenta.getResponsable("editable")+"</editable>"+
			"<existe>"+formaCuenta.getResponsable("existe")+"</existe>";
		mapaRespuesta.put("respuestaValidacion",aux);
		
		
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
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

	/**
	 * M�todo que realiza el filtro de la fecha de afiliacion
	 * @param con
	 * @param formaCuenta
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroFechaAfiliacion(Connection con, ModificarCuentaForm formaCuenta, HttpServletResponse response) 
	{
		String aux = "";
		
		if(UtilidadFecha.validarFecha(formaCuenta.getFechaAfiliacion()))
		{
			String fechaSistema = UtilidadFecha.getFechaActual(con);
			if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(formaCuenta.getFechaAfiliacion(), fechaSistema))
			{
				aux ="<fecha-valida>"+ConstantesBD.acronimoSi+"</fecha-valida>";
				
				int numeroDias = UtilidadFecha.numeroDiasEntreFechas(formaCuenta.getFechaAfiliacion(), fechaSistema);
				int numeroSemanas = (numeroDias/7) + (numeroDias%7!=0?1:0);
				aux += "<semanas>"+numeroSemanas+"</semanas>";
				aux += "<meses>"+UtilidadFecha.numeroMesesEntreFechas(formaCuenta.getFechaAfiliacion(), fechaSistema,false)+"</meses>";
			}
			else
			{
				aux ="<fecha-valida>"+ConstantesBD.acronimoNo+"</fecha-valida>";
				aux +="<mensaje>La fecha de afiliaci�n debe ser anterior o igual a la fecha actual.</mensaje>";
			}
			
		}
		else
		{
			aux ="<fecha-valida>"+ConstantesBD.acronimoNo+"</fecha-valida>";
			aux +="<mensaje>La fecha de afiliaci�n no es una fecha v�lida. Debe estar en formato dd/mm/aaaa.</mensaje>";
		}
		
		
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
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
	 * M�todo que realiza el filtro por el cambio de convenio
	 * @param con
	 * @param formaCuenta
	 * @param medico
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionFiltroConvenio(Connection con, ModificarCuentaForm formaCuenta, UsuarioBasico medico, HttpServletResponse response, PersonaBasica paciente) throws IPSException 
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
		 * [3] => empresaInstitucion
		 */
		String[] datosConvenio = formaCuenta.getCodigoConvenio().split(ConstantesBD.separadorSplit);
		
		
		if(fueSeleccionadoConvenio(formaCuenta, datosConvenio[0]))
		{
			aux = "<error>El convenio "+Utilidades.obtenerNombreConvenioOriginal(con, Integer.parseInt(datosConvenio[0]))+" ya fue ingresado</error>";
			
			UtilidadBD.closeConnection(con);
			//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
			try
			{
				response.setContentType("text/xml");
				response.setHeader("Cache-Control", "no-cache");
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
		//Se verifica si el convenio elegido es excento de documento de garant�a
		else if(!UtilidadTexto.getBoolean(UtilidadesFacturacion.esConvenioExcentoDocGarantia(con, Integer.parseInt(datosConvenio[0]), medico.getCodigoInstitucionInt()))&&
				UtilidadesManejoPaciente.esRequeridoDocumentoGarantia(con, paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), paciente.getCodigoTipoIdentificacionPersona(), medico.getCodigoInstitucionInt())&&
				!DocumentosGarantia.existenDocumentosGarantiaXIngreso(con, paciente.getCodigoIngreso(), paciente.getCodigoPersona(), medico.getCodigoInstitucionInt()))
		{
			aux = "<error>El convenio "+Utilidades.obtenerNombreConvenioOriginal(con, Integer.parseInt(datosConvenio[0]))+" requiere ingresar deudor y documento de garant�a</error>";
			
			UtilidadBD.closeConnection(con);
			//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
			try
			{
				response.setContentType("text/xml");
				response.setHeader("Cache-Control", "no-cache");
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
		else
		{
			formaCuenta.setCuenta("codigoConvenio", formaCuenta.getCodigoConvenio()); //se actualiza en la forma
			//*************VERIFICACION DE INDICATIVO PYP Y CAPITACION***********************************
			aux = "<indicativo-pyp>";
			aux += UtilidadTexto.getBoolean(datosConvenio[2])?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo;
			aux += "</indicativo-pyp>";
			aux += "<indicativo-capitacion>";
			aux += Integer.parseInt(datosConvenio[1])==ConstantesBD.codigoTipoContratoCapitado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo;
			aux += "</indicativo-capitacion>";
			//se llena la informacion del mapa
			formaCuenta.setCuenta("esConvenioPyp", UtilidadTexto.getBoolean(datosConvenio[2])?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			formaCuenta.setCuenta("esConvenioCapitado", Integer.parseInt(datosConvenio[1])==ConstantesBD.codigoTipoContratoCapitado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			formaCuenta.setCuenta("esConvenioColsanitas", UtilidadesFacturacion.esConvenioColsanitas(con, Integer.parseInt(datosConvenio[0]), medico.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			aux += "<indicativo-colsanitas>"+formaCuenta.getCuenta("esConvenioColsanitas")+"</indicativo-colsanitas>";
			logger.info("respuestaPypCapitacion=> "+aux);
			mapaRespuesta.put("respuestaPypCapitacion", aux);
			//*******************************************************************************************
			//************VERIFICACION DEL TIPO DE REGIMEN************************************************
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
			
			aux = "<contratos>";
			for(int i=0;i<formaCuenta.getContratos().size();i++)
			{
				mapaAux = (HashMap) formaCuenta.getContratos().get(i);
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
			//*********CONSULTA DE LOS ESTRATOS SOCIALES*****************************************************
			String fechaReferencia = Utilidades.capturarFechaBD();
			
			if(!UtilidadTexto.isEmpty(formaCuenta.getCuenta("fechaApertura").toString())) {
				fechaReferencia = UtilidadFecha.conversionFormatoFechaABD(formaCuenta.getCuenta("fechaApertura").toString());
			}
			
			formaCuenta.setEstratosSociales(
					UtilidadesFacturacion.cargarEstratosSociales(con, medico.getCodigoInstitucionInt(),
							ConstantesBD.acronimoSi, codigoRegimen,Integer.parseInt(datosConvenio[0]),
							Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()+""),
							fechaReferencia));
			
			
			//*********CONSULTA DE LOS TIPOS AFILIADO*****************************************************
			HashMap tiposAfiliado = UtilidadesFacturacion.cargarTiposAfiliadoXEstrato(
					con, medico.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, 
					Utilidades.convertirAEntero(datosConvenio[0]),Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()+""),
					Utilidades.convertirAEntero(formaCuenta.getCuenta().get("codigoEstratoSocial").toString()),
					fechaReferencia);
			
			formaCuenta.setTiposAfiliado(tiposAfiliado);
			
			//9) Se consultan las naturalezas del paciente
			formaCuenta.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
					con, codigoRegimen, Integer.parseInt(datosConvenio[0]),
					Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()+""), 
					formaCuenta.getCodigoTipoAfiliado(), 
					Utilidades.convertirAEntero(formaCuenta.getCuenta().get("codigoEstratoSocial").toString()),
					fechaReferencia));
			
			
			//9) Se consultan las naturalezas del paciente
			/*formaCuenta.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPaciente(
					con, codigoRegimen,Integer.parseInt(datosConvenio[0]),
					Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()+"")));*/
			//**********************************************************************************************
			
			//*********CONSULTA DE LOS TIPOS AFILIADO*****************************************************
			/*HashMap tiposAfiliado=UtilidadesFacturacion.cargarTiposAfiliado(con, medico.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, Integer.parseInt(datosConvenio[0]),Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()+""));
			formaCuenta.setTiposAfiliado(tiposAfiliado);*/
			
			if(Utilidades.convertirAEntero(datosConvenio[1])==ConstantesBD.codigoTipoContratoCapitado)
			{
				//si solo tiene un contrato vigente se debe postular la informacion
				if(formaCuenta.getContratos().size()==1)
				{
					int codClasi=Utilidades.convertirAEntero(formaCuenta.getContratos().get(0).get("codigoestratosocial")+"");
					if(codClasi>0)
					{
						String tipoRegimenCSE=Utilidades.obtenerTipoRegimenClasificacionSocioEconomica(con, codClasi);
						if(codigoRegimen.equals(tipoRegimenCSE))
						{
							formaCuenta.setCuenta("codigoEstratoSocial", codClasi);
							HashMap tempo=new HashMap();
							tempo.put("numRegistros", "0");
							HashMap tempo1=new HashMap();
							tempo1.put("numRegistros", "0");
							for(int i=0;i<Utilidades.convertirAEntero(formaCuenta.getEstratosSociales("numRegistros")+"");i++)
							{
								if(Utilidades.convertirAEntero(formaCuenta.getEstratosSociales("codigo_"+i)+"")==codClasi)
								{
									tempo.put("codigo_0", formaCuenta.getEstratosSociales("codigo_"+i)+"");
									tempo.put("descripcion_0", formaCuenta.getEstratosSociales("descripcion_"+i)+"");
									tempo.put("numRegistros", "1");
									break;
								}
							}
							String tipoAfil=formaCuenta.getContratos().get(0).get("tipoafiliado")+"";
							if(!UtilidadTexto.isEmpty(tipoAfil+""))
							{
								//{numRegistros=2, acronimo_0=B, nombre_1=Cotizante, nombre_0=Beneficiario, acronimo_1=C}
								formaCuenta.setCuenta("codigoTipoAfiliado", tipoAfil);
								
								for(int i=0;i<Utilidades.convertirAEntero(formaCuenta.getTiposAfiliado("numRegistros")+"");i++)
								{
									if((formaCuenta.getTiposAfiliado("acronimo_"+i)+"").equals(tipoAfil+""))
									{
										tempo1.put("acronimo_0", formaCuenta.getTiposAfiliado("acronimo_"+i)+"");
										tempo1.put("nombre_0", formaCuenta.getTiposAfiliado("nombre_"+i)+"");
										tempo1.put("numRegistros", "1");
										break;
									}
								}
		
							}
							if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitadosModificarCuenta(medico.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
							{
								formaCuenta.setEstratosSociales(tempo);
								if(!UtilidadTexto.isEmpty(tipoAfil))
								{
									formaCuenta.setTiposAfiliado(tempo1);
								}
							}	
							
							HashMap<String, Object>cuenta=formaCuenta.getCuenta();
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
								if(!formaCuenta.getContratos().isEmpty()&&formaCuenta.getContratos().size()==1){
									IValidacionTipoCobroPacienteServicio validacionTipoCobroPacienteServicio=FacturacionServicioFabrica.crearValidacionTipoCobroPacienteServicio();
									validacion=validacionTipoCobroPacienteServicio.validarTipoCobroPacienteServicioConvenioContrato(Integer.parseInt(cuenta.get("codigoContrato").toString()));
								}
								if(validacion!=null&&UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion())&&
										Utilidades.convertirAEntero(formaCuenta.getEstratosSociales().get("numRegistros")+"")<=0)
								{
									aux = "<error>La Clasificaci�n socio econ�mica del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
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
									if(Utilidades.convertirAEntero(formaCuenta.getTiposAfiliado("numRegistros")+"")<=0)
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
							formaCuenta.setEstratosSociales(new HashMap());
							formaCuenta.setTiposAfiliado(new HashMap());
							
								aux = "<error>El Tipo de r�gimen de la Clasificaci�n socio  econ�mica no corresponde con el Tipo de r�gimen del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
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
					int natPaciente=Utilidades.convertirAEntero(formaCuenta.getContratos().get(0).get("naturalezapaciente")+"");
					if(natPaciente>0)
					{
						if(UtilidadValidacion.esNaturalezaValidaTipoRegimen(natPaciente,codigoRegimen+"")) 
						{

							formaCuenta.setCuenta("codigoNaturaleza", natPaciente+"");
							Vector<InfoDatosString> naturalezaVector=new Vector<InfoDatosString>();
							for(int i=0;i<formaCuenta.getNaturalezasPaciente().size();i++)
							{
								if(Utilidades.convertirAEntero(formaCuenta.getNaturalezasPaciente().get(i).getCodigo()) ==natPaciente)
								{
									naturalezaVector.add(formaCuenta.getNaturalezasPaciente().get(i));
								}
							}
							if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitadosModificarCuenta(medico.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
							{
								formaCuenta.setNaturalezasPaciente(naturalezaVector);
							}
							
							if(formaCuenta.getNaturalezasPaciente().size()<=0)
							{
								formaCuenta.setCuenta("codigoNaturaleza", natPaciente+"");
								formaCuenta.setNaturalezasPaciente(new Vector<InfoDatosString>());
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
							aux = "<error>El Tipo de r�gimen de la Naturaleza Paciente no corresponde con el Tipo de r�gimen del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
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
			for(int i=0;i<Integer.parseInt(formaCuenta.getEstratosSociales("numRegistros").toString());i++)
				aux += "" +
					"<estrato-social>" +
						"<codigo>"+formaCuenta.getEstratosSociales("codigo_"+i)+"</codigo>" +
						"<nombre>"+formaCuenta.getEstratosSociales("descripcion_"+i)+"</nombre>" +
					"</estrato-social>";
			aux += "</estratos-sociales>";
			mapaRespuesta.put("respuestaEstratosSociales", aux);
			aux = "<tipos-afiliado>";
			for(int i=0;i<Integer.parseInt(formaCuenta.getTiposAfiliado().get("numRegistros").toString());i++)
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
						"<codigo>"+formaCuenta.getNaturalezasPaciente().get(i).getCodigo()+"</codigo>" +
						"<nombre>"+formaCuenta.getNaturalezasPaciente().get(i).getNombre()+"</nombre>" +
					"</naturaleza-paciente>";
			aux += "</naturalezas-paciente>";
			mapaRespuesta.put("respuestaNaturaleza", aux);
			
			//**********************************************************************************************
			/*
			//**********CONSULTA DE LOS MONTOS DE COBRO******************************************************
			formaCuenta.setMontosCobro(UtilidadesFacturacion.obtenerMontosCobroXConvenio(con, datosConvenio[0], UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			aux = "<montos-cobro>";
			for(int i=0;i<formaCuenta.getMontosCobro().size();i++)
			{
				mapaAux = (HashMap) formaCuenta.getMontosCobro().get(i);
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
				aux += "</monto-cobro>";
			}
			aux += "</montos-cobro>";
			
			mapaRespuesta.put("respuestaMontosCobro", aux);
			*/
			//***********************************************************************************************
			//*****************	CONSULTA DE LOS REQUISITOS PACIENTE*******************************************
			//Se consultan los requisitos de ingreso
			arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
				con,
				Integer.parseInt(datosConvenio[0]), 
				ConstantesIntegridadDominio.acronimoIngreso, 
				true, 
				medico.getCodigoInstitucionInt(),
				Integer.parseInt(formaCuenta.getCuenta("codigoViaIngreso").toString()));
			formaCuenta.setCuenta("numReqIngreso", arregloAux.size()+"");
			aux = "<requisitos-ingreso>";
			for(int i=0;i<arregloAux.size();i++)
			{
				HashMap elemento = (HashMap)arregloAux.get(i);
				formaCuenta.setCuenta("codigoReqIngreso_"+i, elemento.get("codigo"));
				formaCuenta.setCuenta("descripcionReqIngreso_"+i, elemento.get("descripcion"));
				formaCuenta.setCuenta("cumplidoReqIngreso_"+i, UtilidadTexto.getBoolean(formaCuenta.getCuenta("cumplidoReqIngreso_"+i)+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				aux += "<requisito-ingreso>";
				aux += "<codigo>"+elemento.get("codigo")+"</codigo>";
				aux += "<descripcion>"+elemento.get("descripcion")+"</descripcion>";
				aux += "<cumplido>"+formaCuenta.getCuenta("cumplidoReqIngreso_"+i)+"</cumplido>";
				aux += "</requisito-ingreso>";
			}
			aux += "</requisitos-ingreso>";
			//Se consultan los requisitos del egreso
			arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
					con,
					Integer.parseInt(datosConvenio[0]), 
					ConstantesIntegridadDominio.acronimoEgreso, 
					true, 
					medico.getCodigoInstitucionInt(),
					Integer.parseInt(formaCuenta.getCuenta("codigoViaIngreso").toString()));
			formaCuenta.setCuenta("numReqEgreso", arregloAux.size()+"");
			aux += "<requisitos-egreso>";
			for(int i=0;i<arregloAux.size();i++)
			{
				HashMap elemento = (HashMap)arregloAux.get(i);
				formaCuenta.setCuenta("codigoReqEgreso_"+i, elemento.get("codigo"));
				formaCuenta.setCuenta("descripcionReqEgreso_"+i, elemento.get("descripcion"));
				formaCuenta.setCuenta("cumplidoReqEgreso_"+i, UtilidadTexto.getBoolean(formaCuenta.getCuenta("cumplidoReqEgreso_"+i)+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				aux += "<requisito-egreso>";
				aux += "<codigo>"+elemento.get("codigo")+"</codigo>";
				aux += "<descripcion>"+elemento.get("descripcion")+"</descripcion>";
				aux += "<cumplido>"+formaCuenta.getCuenta("cumplidoReqEgreso_"+i)+"</cumplido>";
				aux += "</requisito-egreso>";
			}
			aux += "</requisitos-egreso>";
			
			formaCuenta.setCuenta("tieneRequisitosPaciente", 
				(Integer.parseInt(formaCuenta.getCuenta("numReqIngreso").toString())>0||Integer.parseInt(formaCuenta.getCuenta("numReqEgreso").toString())>0)?
					ConstantesBD.acronimoSi:
					ConstantesBD.acronimoNo);
			aux += "<indicativo-requisitos>"+formaCuenta.getCuenta("tieneRequisitosPaciente")+"</indicativo-requisitos>";
			
			mapaRespuesta.put("respuestaRequisitosPaciente", aux);
			
			
			//***********************************************************************************************
			//*********VALIDACIONES VARIAS CONVENIOS****************************************************
			Convenio mundoConvenio = new Convenio();
			mundoConvenio.cargarResumen(con, Integer.parseInt(datosConvenio[0]));
			//se llena mapa
			formaCuenta.setCuenta("esConvenioSoat", Convenio.esConvenioTipoConventioEventoCatTrans(mundoConvenio.getCodigo())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			formaCuenta.setCuenta("manejoComplejidad", mundoConvenio.getManejaComplejidad());
			formaCuenta.setCuenta("esConvenioPoliza", UtilidadTexto.getBoolean(mundoConvenio.getCheckInfoAdicCuenta())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			formaCuenta.setCuenta("requiereCarnet", mundoConvenio.getRequiereNumeroCarnet());
			formaCuenta.setCuenta("semanasMinimasCotizacion", mundoConvenio.getSemanasMinimasCotizacion());
			formaCuenta.setCuenta("esConvenioVerificacion",mundoConvenio.isRequiereVerificacionDerechos()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			formaCuenta.setCuenta("esRequiereAutorizacionServicio",UtilidadTexto.getBoolean(mundoConvenio.getRequiere_autorizacion_servicio())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			
			//Se verifica si se maneja complejidad
			formaCuenta.setCuenta("indicadorManejaComplejidad", validacionManejoComplejidad(formaCuenta));
			
			formaCuenta.setCuenta("isConvenioManejaMontos",mundoConvenio.isConvenioManejaMontoCobro());
			
			if(formaCuenta.getContratos().size()==1){
				final int unicoContrato=0;
				verificarMontoCobro(formaCuenta,formaCuenta.getContratos().get(unicoContrato).get("codigo").toString());
			}
			
			aux = "<indicativo-soat>"+formaCuenta.getCuenta("esConvenioSoat")+"</indicativo-soat>";
			aux += "<indicativo-complejidad>"+formaCuenta.getCuenta("indicadorManejaComplejidad")+"</indicativo-complejidad>";
			aux += "<indicativo-poliza>"+formaCuenta.getCuenta("esConvenioPoliza")+"</indicativo-poliza>";
			aux += "<indicativo-autorizacion>"+formaCuenta.getCuenta("esRequiereAutorizacionServicio")+"</indicativo-autorizacion>";
			aux += "<indicativo-manejamontocobro>"+formaCuenta.getCuenta("isConvenioManejaMontos")+"</indicativo-manejamontocobro>";
			mapaRespuesta.put("respuestaValidaciones", aux);
			//*******************************************************************************************
			///****************************	VALIDAR SIN CONTRATO Y CONTROLA ANTICIPOS************************
			ResultadoBoolean resultado = new ResultadoBoolean(false,""); 
			//Solo sirve si el convenio solo tiene 1 contrato
			if(formaCuenta.getContratos().size()==1)
				resultado = validacionSinContratoControlAnticipos(con, Integer.parseInt(formaCuenta.getContratos().get(0).get("codigo")+""));
			aux = "<alerta-contrato>";
			if(resultado.isTrue())
				aux += resultado.getDescripcion();
			aux += "</alerta-contrato>";
			mapaRespuesta.put("respuestaAlertaContrato", aux);
			//***********************************************************************************
			//***************************************************************************************
			aux = "<coberturas-salud>";
			formaCuenta.setCoberturasSalud(UtilidadesManejoPaciente.obtenerCoberturasSaludXTipoRegimen(con, formaCuenta.getCuenta("codigoTipoRegimen").toString(), medico.getCodigoInstitucionInt()));
			for(HashMap<String, Object> elemento:formaCuenta.getCoberturasSalud())
			{
				aux += "<cobertura-salud>";
				aux += "<codigo>"+elemento.get("codigo")+"</codigo>";
				aux += "<nombre>"+elemento.get("nombre")+"</nombre>";
				aux += "</cobertura-salud>";
			}
			aux += "</coberturas-salud>";
			mapaRespuesta.put("respuestaCoberturasSalud", aux);
			//**************************************************************
			
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
		        //response.getWriter().write(mapaRespuesta.get("respuestaMontosCobro").toString());
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
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private ActionForward filtroTipoAfiliado(Connection con, ModificarCuentaForm formaCuenta, UsuarioBasico medico, HttpServletResponse response, PersonaBasica paciente) {
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
		String[] datosConvenio = formaCuenta.getCuenta("codigoConvenio").toString().split(ConstantesBD.separadorSplit);
		
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
		String fechaReferencia = Utilidades.capturarFechaBD();
		
		if(!UtilidadTexto.isEmpty(formaCuenta.getCuenta("fechaApertura").toString())) {
			fechaReferencia = UtilidadFecha.conversionFormatoFechaABD(formaCuenta.getCuenta("fechaApertura").toString());
		}
		
		formaCuenta.setEstratosSociales(UtilidadesFacturacion.cargarEstratosSociales(con, medico.getCodigoInstitucionInt(),
				ConstantesBD.acronimoSi, codigoRegimen,Integer.parseInt(datosConvenio[0]),
				Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()+""),
				fechaReferencia));
		//**********************************************************************************************
		
		HashMap tiposAfiliado = UtilidadesFacturacion.cargarTiposAfiliadoXEstrato(
				con, medico.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, 
				Utilidades.convertirAEntero(datosConvenio[0]),Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()+""),
				Utilidades.convertirAEntero(formaCuenta.getCodigoEstratoSocial()),
				fechaReferencia);
		
		formaCuenta.setTiposAfiliado(tiposAfiliado);
		
		if (this.cargarDatosContratoCapitado(con, formaCuenta, response, medico, Integer.parseInt(datosConvenio[1]), codigoRegimen) == null) {
			return null;
		}
		
		//9) Se consultan las naturalezas del paciente
		String acronimoTipoAfiliado="";
		if(formaCuenta.getTiposAfiliado().get("acronimo_0") != null){
			acronimoTipoAfiliado=formaCuenta.getTiposAfiliado().get("acronimo_0").toString();
		}
		formaCuenta.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
				con, codigoRegimen, Integer.parseInt(datosConvenio[0]),
				Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()+""), 
				acronimoTipoAfiliado, 
				Utilidades.convertirAEntero(formaCuenta.getCodigoEstratoSocial()),
				fechaReferencia));
		
		
		
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
	private void verificarMontoCobro(ModificarCuentaForm ingresoPacienteForm,String codigoContratoSeleccionado) {
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
				contratoSeleccionado!=null&&!(Boolean)ingresoPacienteForm.getCuenta("isConvenioManejaMontos")){
			
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
	private ActionForward filtroNaturalezaPaciente(Connection con, ModificarCuentaForm formaCuenta, UsuarioBasico medico, HttpServletResponse response, PersonaBasica paciente) {
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
		String fechaReferencia = Utilidades.capturarFechaBD();
		
		if(!UtilidadTexto.isEmpty(formaCuenta.getCuenta("fechaApertura").toString())) {
			fechaReferencia = UtilidadFecha.conversionFormatoFechaABD(formaCuenta.getCuenta("fechaApertura").toString());
		}
		
		formaCuenta.setEstratosSociales(
				UtilidadesFacturacion.cargarEstratosSociales(con, medico.getCodigoInstitucionInt(),
						ConstantesBD.acronimoSi, codigoRegimen,Integer.parseInt(datosConvenio[0]),
						Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()+""),
						fechaReferencia));
		
		//**********************************************************************************************
		
		//*********CONSULTA DE LOS TIPOS AFILIADO*****************************************************
		HashMap tiposAfiliado = UtilidadesFacturacion.cargarTiposAfiliadoXEstrato(
				con, medico.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, 
				Utilidades.convertirAEntero(datosConvenio[0]),Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()+""),
				Utilidades.convertirAEntero(formaCuenta.getCodigoEstratoSocial()),
				fechaReferencia);
		
		formaCuenta.setTiposAfiliado(tiposAfiliado);
		
		//9) Se consultan las naturalezas del paciente
		formaCuenta.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
				con, codigoRegimen, Integer.parseInt(datosConvenio[0]),
				Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()+""), 
				formaCuenta.getCodigoTipoAfiliado(), 
				Utilidades.convertirAEntero(formaCuenta.getCodigoEstratoSocial()),
				fechaReferencia));
		
		if (this.cargarDatosContratoCapitado(con, formaCuenta, response, medico, Integer.parseInt(datosConvenio[1]), codigoRegimen) == null) {
			return null;
		}
		
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
	 * M�todo que valida si un contrato es sin contrato o de control de anticipos
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
	 * M�todo que verifica si un convenio ya fue seleccionado
	 * @param formaCuenta
	 * @param codigoConvenio
	 * @return
	 */
	private boolean fueSeleccionadoConvenio(ModificarCuentaForm formaCuenta, String codigoConvenio) 
	{
		boolean seleccionado = false;
		
		//Se verifica el convenio de prioridad 1
		if(formaCuenta.getCuenta("codigoConvenio")!=null&&
		  formaCuenta.getCuenta("codigoConvenio").toString().split(ConstantesBD.separadorSplit)[0].equals(codigoConvenio))
			seleccionado = true;
		
		//Se verifica el convenio en los dem�s convenios
		for(int i=0;i<Integer.parseInt(formaCuenta.getVariosConvenios("numRegistros").toString());i++)
		{
			if(formaCuenta.getVariosConvenios("codigoConvenio_"+i)!=null&&
				formaCuenta.getVariosConvenios("codigoConvenio_"+i).toString().equals(codigoConvenio))
				seleccionado = true;
		}
		
		
		return seleccionado;
	}

	/**
	 * M�todo que realiza carga la informaci�n de una cuenta proveniente de un asocio
	 * @param con
	 * @param formaCuenta
	 * @param mapping
	 * @param medico
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionSeleccionCuentaAsocio(Connection con, ModificarCuentaForm formaCuenta, ActionMapping mapping, UsuarioBasico medico, PersonaBasica paciente) throws IPSException 
	{
		this.cargarFormCuenta(con,formaCuenta,medico,paciente);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("cuenta");
	}

	/**
	 * M�todo que inicia el flujo de la MODIFICACION DE LA CUENTA
	 * @param con
	 * @param paciente
	 * @param formaCuenta
	 * @param medico
	 * @param request 
	 * @param mapping 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, PersonaBasica paciente, ModificarCuentaForm formaCuenta, UsuarioBasico medico, ActionMapping mapping, HttpServletRequest request) throws IPSException 
	{
		//*****************************VALIDACIONES INICIALES**************************************************************
		/**
		 * Se realiza validacion general
		 */
		RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
		if(!resp.puedoSeguir)
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, resp.textoRespuesta, resp.textoRespuesta, true);
		
			//**********************************************************************************************************************
		
		//Se limpian los datos
		formaCuenta.reset();
		
		//******************VERIFICACION DEL ASOCIO DE CUENTAS ********************************************************
		//Si hay asocio de cuentas completo se debe llevar a la selecci�n de cuentas
		if(paciente.getExisteAsocio()&&paciente.getCodigoCuentaAsocio()>0)
		{
			formaCuenta.setCuentasAsocio(Cuenta.listarCuentasAsocio(con, paciente.getCodigoIngreso()+""));
			UtilidadBD.closeConnection(con);
			return mapping.findForward("inicio");
		}
		else
			formaCuenta.setCodigoCuenta(paciente.getCodigoCuenta());
		//*************************************************************************************************************
		
		this.cargarFormCuenta(con,formaCuenta,medico,paciente);
		
		if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion && paciente.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado) && paciente.getCodigoCama()==0)
		formaCuenta.setAsignarCama(true);
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("cuenta");
	}

	/**
	 * M�todo implementado para cargar los datos de la cuenta
	 * @param con
	 * @param formaCuenta
	 * @param medico 
	 * @param paciente 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private DtoCuentas cargarFormCuenta(Connection con, ModificarCuentaForm formaCuenta, UsuarioBasico medico, PersonaBasica paciente) throws IPSException 
	{
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		
		//**********SE CARGAN LOS DATOS DE LA CUENTA***************************************
		Cuenta mundoCuenta=new Cuenta();
		mundoCuenta.cargar(con, formaCuenta.getCodigoCuenta()+"");
		
		formaCuenta.setCuenta("idCuenta", mundoCuenta.getCuenta().getIdCuenta());
		formaCuenta.setCuenta("fechaApertura", mundoCuenta.getCuenta().getFechaApertura());
		formaCuenta.setCuenta("horaApertura", mundoCuenta.getCuenta().getHoraApertura());
		formaCuenta.setCuenta("codigoInstitucion", medico.getCodigoInstitucion());
		formaCuenta.setCuenta("codigoPaciente", mundoCuenta.getCuenta().getCodigoPaciente());
		formaCuenta.setCuenta("nombrePaciente", mundoCuenta.getCuenta().getDescripcionPaciente());
		formaCuenta.setCuenta("nombreEstadoCuenta", mundoCuenta.getCuenta().getDescripcionEstado());
		formaCuenta.setCuenta("idIngreso", mundoCuenta.getCuenta().getIdIngreso());
		formaCuenta.setCuenta("descEntidadesSubcontratadas", mundoCuenta.getCuenta().getDescEntidadesSubcontratadas());
		formaCuenta.setCuenta("loginUsuario", mundoCuenta.getCuenta().getLoginUsuario());
		formaCuenta.setCuenta("codigoViaIngreso", mundoCuenta.getCuenta().getCodigoViaIngreso()+"");
		formaCuenta.setCuenta("hospitalDia", mundoCuenta.getCuenta().isHospitalDia()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		formaCuenta.setCuenta("nombreViaIngreso", mundoCuenta.getCuenta().getDescripcionViaIngreso());
		formaCuenta.setCuenta("idSubCuenta", mundoCuenta.getCuenta().getConvenios()[0].getSubCuenta());
		formaCuenta.setCuenta("nombreConvenio", mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getNombre());
		formaCuenta.setCuenta("codigoContrato", mundoCuenta.getCuenta().getConvenios()[0].getContrato()+"");
		formaCuenta.setCuenta("numeroContrato", mundoCuenta.getCuenta().getConvenios()[0].getNumeroContrato());		
		formaCuenta.setCuenta("valorUtilizadoSoat", mundoCuenta.getCuenta().getConvenios()[0].getValorUtilizadoSoat());
		formaCuenta.setCuenta("fechaAfiliacion", mundoCuenta.getCuenta().getConvenios()[0].getFechaAfiliacion());
		formaCuenta.setCuenta("semanasCotizacion", mundoCuenta.getCuenta().getConvenios()[0].getSemanasCotizacion()>0?mundoCuenta.getCuenta().getConvenios()[0].getSemanasCotizacion()+"":"");
		formaCuenta.setCuenta("mesesCotizacion", mundoCuenta.getCuenta().getConvenios()[0].getMesesCotizacion()>0?mundoCuenta.getCuenta().getConvenios()[0].getMesesCotizacion()+"":"");
		formaCuenta.setCuenta("nombreTipoComplejidad", mundoCuenta.getCuenta().getDescripcionTipoComplejidad());
		formaCuenta.setCuenta("tipoComplejidad", mundoCuenta.getCuenta().getCodigoTipoComplejidad()>0?mundoCuenta.getCuenta().getCodigoTipoComplejidad()+"":"");
		formaCuenta.setCuenta("codigoEstratoSocial", mundoCuenta.getCuenta().getConvenios()[0].getClasificacionSocioEconomica()+"");
		formaCuenta.setCuenta("nombreEstratoSocial", mundoCuenta.getCuenta().getConvenios()[0].getDescripcionClasificacionSocioEconomica());
		formaCuenta.setCuenta("codigoTipoAfiliado", mundoCuenta.getCuenta().getConvenios()[0].getTipoAfiliado());
		formaCuenta.setCuenta("nombreTipoAfiliado", mundoCuenta.getCuenta().getConvenios()[0].getDescripcionTipoAfiliado());
		formaCuenta.setCuenta("codigoMontoCobro", mundoCuenta.getCuenta().getConvenios()[0].getMontoCobro()+"");
		formaCuenta.setCuenta("nombreMontoCobro", mundoCuenta.getCuenta().getConvenios()[0].getDescripcionMontoCobro());
		formaCuenta.setCuenta("nombreTipoPaciente", mundoCuenta.getCuenta().getDescripcionTipoPaciente());
		formaCuenta.setCuenta("codigoTipoPaciente", mundoCuenta.getCuenta().getCodigoTipoPaciente());
		formaCuenta.setCuenta("codigoTipoCobertura", mundoCuenta.getCuenta().getConvenios()[0].getCodigoTipoCobertura());
		formaCuenta.setCuenta("nombreTipoCobertura", mundoCuenta.getCuenta().getConvenios()[0].getNombreTipoCobertura());
		formaCuenta.setCuenta("codigoNaturaleza", (mundoCuenta.getCuenta().getConvenios()[0].getNaturalezaPaciente() == 0) ? -1 : mundoCuenta.getCuenta().getConvenios()[0].getNaturalezaPaciente());
		formaCuenta.setCuenta("nombreNaturaleza", mundoCuenta.getCuenta().getConvenios()[0].getDescripcionNaturalezaPaciente());
		
		formaCuenta.setCuenta("nombreNaturaleza", mundoCuenta.getCuenta().getConvenios()[0].getDescripcionNaturalezaPaciente());
		
		formaCuenta.setCodigoRegistroAccidenteTransito(mundoCuenta.getCuenta().getConvenios()[0].getCodigoRegistroAccidenteTransito());
		if(formaCuenta.getCodigoRegistroAccidenteTransito()==null)
		{
			formaCuenta.setCodigoRegistroAccidenteTransito(ConstantesBD.codigoNuncaValidoLong);
		}
		
		formaCuenta.setCuenta("codigoTipoEvento", mundoCuenta.getCuenta().getCodigoTipoEvento());
		formaCuenta.setCuenta("nombreTipoEvento", mundoCuenta.getCuenta().getDescripcionTipoEvento());
		formaCuenta.setCuenta("codigoTipoEventoAnterior", mundoCuenta.getCuenta().getCodigoTipoEvento());
		formaCuenta.setCuenta("codigoArpAfiliado", mundoCuenta.getCuenta().getCodigoConvenioArpAfiliado()+"");
		formaCuenta.setCuenta("nombreArpAfiliado", mundoCuenta.getCuenta().getDescripcionConvenioArpAfiliado());
		formaCuenta.setCuenta("codigoOrigenAdmision", mundoCuenta.getCuenta().getCodigoOrigenAdmision());
		formaCuenta.setCuenta("codigoOrigenAdmisionAnterior", mundoCuenta.getCuenta().getCodigoOrigenAdmision());
		formaCuenta.setCuenta("nombreOrigenAdmision", mundoCuenta.getCuenta().getDescripcionOrigenAdmision());
		formaCuenta.setCuenta("codigoArea", mundoCuenta.getCuenta().getCodigoArea());
		formaCuenta.setCuenta("nombreArea", mundoCuenta.getCuenta().getDescripcionArea());
		formaCuenta.setCuenta("observaciones", mundoCuenta.getCuenta().getObservaciones());
		formaCuenta.setCuenta("existeIngresoCuidadoEspecial", UtilidadesManejoPaciente.existeIngresoCuidadoEspecialActivo(con, Integer.parseInt(mundoCuenta.getCuenta().getIdIngreso()), "")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("existeIngresoCuidadoEspecial").toString()))
		{
			formaCuenta.setTiposMonitoreo(Utilidades.consultaTipoMonitoreoxCC(con, mundoCuenta.getCuenta().getCodigoArea()));
			formaCuenta.setCuenta("codigoTipoMonitoreo", UtilidadesManejoPaciente.obtenerCodigoTipoMonitoreoIngresoCuidadoEspecial(con, Integer.parseInt(mundoCuenta.getCuenta().getIdIngreso())));
			formaCuenta.setCuenta("codigoTipoMonitoreoAnterior", formaCuenta.getCuenta("codigoTipoMonitoreo"));
			formaCuenta.setCuenta("nombreTipoMonitoreo", Utilidades.consultaTipoMonitoreoxCE(Integer.parseInt(mundoCuenta.getCuenta().getIdIngreso())));
		}
		else	
		{
			formaCuenta.setCuenta("codigoTipoMonitoreo", "");
			formaCuenta.setCuenta("codigoTipoMonitoreoAnterior", "");
			formaCuenta.setCuenta("nombreTipoMonitoreo", "");
			formaCuenta.setTiposMonitoreo(new HashMap());
		}
		formaCuenta.setCuenta("numeroCarnet", mundoCuenta.getCuenta().getConvenios()[0].getNroCarnet());
		formaCuenta.setCuenta("numeroPoliza", mundoCuenta.getCuenta().getConvenios()[0].getNroPoliza());
		formaCuenta.setCuenta("autorizacionIngreso", mundoCuenta.getCuenta().getConvenios()[0].getNroAutorizacion());
		formaCuenta.setCuenta("numeroSolicitudVolante", mundoCuenta.getCuenta().getConvenios()[0].getNumeroSolicitudVolante());
		
		formaCuenta.setRegistroInconsistencias(UtilidadesFacturacion.esConvenioReporteInconsistenciasenBD(con, mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo()));
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		// adicionado por anexo 654 transplante
		if (UtilidadCadena.noEsVacio(mundoCuenta.getCuenta().getTransplante()))
			formaCuenta.setCuenta("transplante",mundoCuenta.getCuenta().getTransplante());
		else
			formaCuenta.setCuenta("transplante","");
		
		if (UtilidadCadena.noEsVacio(mundoCuenta.getCuenta().getTransplante()))
			formaCuenta.setCuenta("nombreTransplante", ValoresPorDefecto.getIntegridadDominio(mundoCuenta.getCuenta().getTransplante()));
		else
			formaCuenta.setCuenta("nombreTransplante","");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		//CARGAR INFORMACION ADICIONAL CONVENIO------------------------
		Convenio mundoConvenio = new Convenio();
		mundoConvenio.cargarResumen(con, mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo());
		
		formaCuenta.setCuenta("codigoConvenio", mundoConvenio.getCodigo()+ConstantesBD.separadorSplit+mundoConvenio.getTipoContrato()+ConstantesBD.separadorSplit+mundoConvenio.getPyp()+ConstantesBD.separadorSplit+mundoConvenio.getEmpresaInstitucion());
		formaCuenta.setCuenta("codigoTipoRegimen", mundoConvenio.getTipoRegimen());
		formaCuenta.setCuenta("nombreTipoRegimen", mundoConvenio.getDescripcionTipoRegimen());
		formaCuenta.setCuenta("manejoComplejidad", mundoConvenio.getManejaComplejidad());
		formaCuenta.setCuenta("esConvenioPyp", UtilidadTexto.getBoolean(mundoConvenio.getPyp())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		formaCuenta.setCuenta("esConvenioCapitado", mundoConvenio.getTipoContrato()==ConstantesBD.codigoTipoContratoCapitado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		formaCuenta.setCuenta("requiereCarnet", mundoConvenio.getRequiereNumeroCarnet());
		formaCuenta.setCuenta("esConvenioSoat", Convenio.esConvenioTipoConventioEventoCatTrans(mundoConvenio.getCodigo())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		formaCuenta.setCuenta("semanasMinimasCotizacion", mundoConvenio.getSemanasMinimasCotizacion());
		formaCuenta.setCuenta("esConvenioVerificacion",mundoConvenio.isRequiereVerificacionDerechos()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		formaCuenta.setCuenta("cambioConvenio",ConstantesBD.acronimoNo); //por defecto se toma que el convenio no ha cambiado
		formaCuenta.setCuenta("esConvenioColsanitas", UtilidadesFacturacion.esConvenioColsanitas(con, mundoConvenio.getCodigo(), medico.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		formaCuenta.setCuenta("esReporteAtencionInicialUrgencias", mundoConvenio.getReporte_atencion_ini_urg());
		formaCuenta.setCuenta("esConvenioPoliza", mundoConvenio.getCheckInfoAdicCuenta());
		
		formaCuenta.setCuenta("isConvenioManejaMontos",mundoConvenio.isConvenioManejaMontoCobro());
		
		//Se consultan todos los convenios
		formaCuenta.setConvenios(Utilidades.obtenerConvenios(con, "", ConstantesBD.codigoTipoContratoEvento+"", true, "", true));
		//Si el convenio es capitado se a�ade a la lista
		if(mundoConvenio.getTipoContrato()==ConstantesBD.codigoTipoContratoCapitado)
		{
			HashMap elemento = new HashMap();
			elemento.put("codigoConvenio", mundoConvenio.getCodigo()+"");
			elemento.put("nombreConvenio", mundoConvenio.getNombre());
			elemento.put("codigoTipoContrato", mundoConvenio.getTipoContrato()+"");
			elemento.put("pyp", mundoConvenio.getPyp());
			elemento.put("empresasInstitucion", mundoConvenio.getEmpresaInstitucion()+"");
			formaCuenta.getConvenios().add(elemento);
		}
		
		//Se consultan los convenios del paciente
		HashMap conveniosPaciente = UtilidadesManejoPaciente.consultarConveniosPaciente(con, paciente.getCodigoPersona()+"");
		for(int i=0;i<Integer.parseInt(conveniosPaciente.get("numRegistros").toString());i++)
		{
			if(UtilidadTexto.getBoolean(conveniosPaciente.get("esCapitado_"+i).toString()) && mundoConvenio.getCodigo()!=Integer.parseInt(conveniosPaciente.get("codigoConvenio_"+i).toString()))
			{
				HashMap elemento = new HashMap();
				elemento.put("codigoConvenio", conveniosPaciente.get("codigoConvenio_"+i));
				elemento.put("nombreConvenio", conveniosPaciente.get("nombreConvenio_"+i));
				elemento.put("codigoTipoContrato", ConstantesBD.codigoTipoContratoCapitado+"");
				elemento.put("pyp", conveniosPaciente.get("esPyp_"+i));
				elemento.put("empresasInstitucion", conveniosPaciente.get("empresasInstitucion_"+i));
				formaCuenta.getConvenios().add(elemento);
			}
		}
		
		formaCuenta.setCuenta("esConvenioPoliza", mundoCuenta.getCuenta().getConvenios()[0].isSubCuentaPoliza());
		
		//CARGA DE LOS DATOS DE LA POLIZA (SI TIENE) ------------------------------------------------
		/*
		if(mundoCuenta.getCuenta().getConvenios()[0].isSubCuentaPoliza())
		{
			formaCuenta.setCuenta("esConvenioPoliza", ConstantesBD.acronimoSi);
			formaCuenta.setCuenta("apellidosPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getApellidos());
			formaCuenta.setCuenta("nombresPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getNombres());
			formaCuenta.setCuenta("tipoIdPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getCodigoTipoIdentificacion());
			formaCuenta.setCuenta("descripcionTipoIdPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getDescripcionTipoIdentificacion());
			formaCuenta.setCuenta("numeroIdPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getNumeroIdentificacion());
			formaCuenta.setCuenta("direccionPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getDireccion());
			formaCuenta.setCuenta("telefonoPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getTelefono());
			formaCuenta.setCuenta("existeTitular", ConstantesBD.acronimoSi);
			formaCuenta.setCuenta("numDatosPoliza", mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getSizeInformacionPoliza()+"");
			double saldo = 0;
			
			for(int i=0;i<mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getSizeInformacionPoliza();i++)
			{
				formaCuenta.setCuenta("codigoPoliza_"+i, mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getCodigoInformacionPoliza(i));
				formaCuenta.setCuenta("fechaPoliza_"+i, mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getFechaInformacionPoliza(i));
				formaCuenta.setCuenta("autorizacionPoliza_"+i, mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getAutorizacionInformacionPoliza(i));
				formaCuenta.setCuenta("valorPoliza_"+i, mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getValorInformacionPoliza(i));
				formaCuenta.setCuenta("eliminarPoliza_"+i, ConstantesBD.acronimoNo);
				formaCuenta.setCuenta("existePoliza_"+i, ConstantesBD.acronimoSi);
				saldo += Double.parseDouble(mundoCuenta.getCuenta().getConvenios()[0].getTitularPoliza().getValorInformacionPoliza(i));
			}
			formaCuenta.setCuenta("saldoPoliza", UtilidadTexto.formatearValores(saldo,"0.00"));
			
			
		}
		else
		{
			formaCuenta.setCuenta("esConvenioPoliza", ConstantesBD.acronimoNo);
			
			//Se dejan los valores por defecto
			formaCuenta.setCuenta("apellidosPoliza",paciente.getPrimerApellido()+" "+paciente.getSegundoApellido());
			formaCuenta.setCuenta("nombresPoliza", paciente.getPrimerNombre()+" "+paciente.getSegundoNombre());
			formaCuenta.setCuenta("tipoIdPoliza", paciente.getCodigoTipoIdentificacionPersona());
			formaCuenta.setCuenta("descripcionTipoIdPoliza", paciente.getTipoIdentificacionPersona(false));
			formaCuenta.setCuenta("numeroIdPoliza", paciente.getNumeroIdentificacionPersona());
			formaCuenta.setCuenta("direccionPoliza", paciente.getDireccion());
			formaCuenta.setCuenta("telefonoPoliza", paciente.getTelefono());
			formaCuenta.setCuenta("existeTitular", ConstantesBD.acronimoNo);
			formaCuenta.setCuenta("numDatosPoliza", "1");
			formaCuenta.setCuenta("codigoPoliza_0", "");
			formaCuenta.setCuenta("fechaPoliza_0", UtilidadFecha.getFechaActual(con));
			formaCuenta.setCuenta("autorizacionPoliza_0", "");
			formaCuenta.setCuenta("valorPoliza_0", "");
			formaCuenta.setCuenta("saldoPoliza_0", "");
			formaCuenta.setCuenta("eliminarPoliza_0", ConstantesBD.acronimoNo);
			formaCuenta.setCuenta("existePoliza_0", ConstantesBD.acronimoNo);
		}
		*/
		//****************************SE CARGAN LOS REQUISITOS DEL PACIENTE********************************************************
		//1) Se cargan los requisitos del paciente que el convenio ten�a registrados
		int numReqIngreso = 0;
		int numReqEgreso = 0;
		for(int i=0;i<mundoCuenta.getCuenta().getConvenios()[0].getRequisitosPaciente().size();i++)
		{
			DtoRequsitosPaciente requisitos = (DtoRequsitosPaciente)mundoCuenta.getCuenta().getConvenios()[0].getRequisitosPaciente().get(i);
			if(requisitos.getTipo().equals(ConstantesIntegridadDominio.acronimoIngreso))
			{
				formaCuenta.setCuenta("codigoReqIngreso_"+numReqIngreso, requisitos.getCodigo()+"");
				formaCuenta.setCuenta("descripcionReqIngreso_"+numReqIngreso, requisitos.getDescripcion());
				formaCuenta.setCuenta("cumplidoReqIngreso_"+numReqIngreso, requisitos.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				numReqIngreso++;
				
			}
			else if(requisitos.getTipo().equals(ConstantesIntegridadDominio.acronimoEgreso))
			{
				formaCuenta.setCuenta("codigoReqEgreso_"+numReqEgreso, requisitos.getCodigo()+"");
				formaCuenta.setCuenta("descripcionReqEgreso_"+numReqEgreso, requisitos.getDescripcion());
				formaCuenta.setCuenta("cumplidoReqEgreso_"+numReqEgreso, requisitos.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				numReqEgreso++;
			}
		}
		//2) Se verifica si se han parametrizado mas requisitos para adicionarlos
		arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
				con,
				mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo(), 
				ConstantesIntegridadDominio.acronimoIngreso, 
				true, 
				medico.getCodigoInstitucionInt(),
				Integer.parseInt(formaCuenta.getCuenta("codigoViaIngreso").toString()));
		
		for(int i=0;i<arregloAux.size();i++)
		{
			HashMap elemento = (HashMap)arregloAux.get(i);
			boolean existe = false;
			
			//si el requisito no existe en el mapa se adiciona
			for(int j=0;j<numReqIngreso;j++)
				if(elemento.get("codigo").toString().equals(formaCuenta.getCuenta("codigoReqIngreso_"+j).toString()))
					existe = true;
			
			if(!existe)
			{
				formaCuenta.setCuenta("codigoReqIngreso_"+numReqIngreso, elemento.get("codigo"));
				formaCuenta.setCuenta("descripcionReqIngreso_"+numReqIngreso, elemento.get("descripcion"));
				formaCuenta.setCuenta("cumplidoReqIngreso_"+numReqIngreso, ConstantesBD.acronimoNo);
				numReqIngreso++;
			}
		}
		
		arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
				con,
				mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo(), 
				ConstantesIntegridadDominio.acronimoEgreso, 
				true, 
				medico.getCodigoInstitucionInt(),
				Integer.parseInt(formaCuenta.getCuenta("codigoViaIngreso").toString()));
		
		for(int i=0;i<arregloAux.size();i++)
		{
			HashMap elemento = (HashMap)arregloAux.get(i);
			boolean existe = false;
			
			//si el requisito no existe en el mapa se adiciona
			for(int j=0;j<numReqEgreso;j++)
				if(elemento.get("codigo").toString().equals(formaCuenta.getCuenta("codigoReqEgreso_"+j).toString()))
					existe = true;
			
			if(!existe)
			{
				formaCuenta.setCuenta("codigoReqEgreso_"+numReqEgreso, elemento.get("codigo"));
				formaCuenta.setCuenta("descripcionReqEgreso_"+numReqEgreso, elemento.get("descripcion"));
				formaCuenta.setCuenta("cumplidoReqEgreso_"+numReqEgreso, ConstantesBD.acronimoNo);
				numReqEgreso++;
			}
		}
		
		formaCuenta.setCuenta("numReqIngreso", numReqIngreso+"");
		formaCuenta.setCuenta("numReqEgreso", numReqEgreso+"");
		formaCuenta.setCuenta("tieneRequisitosPaciente", (numReqEgreso>0||numReqIngreso>0)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		//**********************************************************************************************************************************
		
		//****************************SE CARGAN LOS DEMAS CONVENIOS DE LA CUENTA*****************************************************+
		
		int cont = 0;
		for(int i=1;i<mundoCuenta.getCuenta().getConvenios().length;i++)
		{
			if(!formaCuenta.isRegistroInconsistencias()){
				formaCuenta.setRegistroInconsistencias(UtilidadesFacturacion.esConvenioReporteInconsistenciasenBD(con, mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo()));	
			}
			//se carga informacion adicional del convenios
			mundoConvenio.cargarResumen(con, mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo());
			
			formaCuenta.setVariosConvenios("idSubCuenta_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getSubCuenta());
			formaCuenta.setVariosConvenios("codigoConvenio_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo());
			formaCuenta.setVariosConvenios("nombreConvenio_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getNombre());
			formaCuenta.setVariosConvenios("esConvenioPyp_"+cont, UtilidadTexto.getBoolean(mundoConvenio.getPyp())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			formaCuenta.setVariosConvenios("esConvenioCapitado_"+cont, mundoConvenio.getTipoContrato()==ConstantesBD.codigoTipoContratoCapitado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			formaCuenta.setVariosConvenios("codigoContrato_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getContrato());
			formaCuenta.setVariosConvenios("numeroContrato_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getNumeroContrato());
			formaCuenta.setVariosConvenios("valorUtilizadoSoat_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getValorUtilizadoSoat());
			formaCuenta.setVariosConvenios("fechaAfiliacion_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getFechaAfiliacion());
			formaCuenta.setVariosConvenios("semanasCotizacion_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getSemanasCotizacion()>0?mundoCuenta.getCuenta().getConvenios()[i].getSemanasCotizacion()+"":"");
			formaCuenta.setVariosConvenios("mesesCotizacion_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getMesesCotizacion()>0?mundoCuenta.getCuenta().getConvenios()[i].getMesesCotizacion()+"":"");
			formaCuenta.setVariosConvenios("codigoEstratoSocial_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getClasificacionSocioEconomica());
			formaCuenta.setVariosConvenios("nombreEstratoSocial_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getDescripcionClasificacionSocioEconomica());
			formaCuenta.setVariosConvenios("codigoTipoAfiliado_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTipoAfiliado());
			formaCuenta.setVariosConvenios("nombreTipoAfiliado_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getDescripcionTipoAfiliado());
			formaCuenta.setVariosConvenios("codigoMontoCobro_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getMontoCobro());
			formaCuenta.setVariosConvenios("nombreMontoCobro_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getDescripcionMontoCobro());
			formaCuenta.setVariosConvenios("codigoTipoRegimen_"+cont, mundoConvenio.getTipoRegimen());
			formaCuenta.setVariosConvenios("nombreTipoRegimen_"+cont, mundoConvenio.getDescripcionTipoRegimen());
			formaCuenta.setVariosConvenios("codigoTipoCobertura_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getCodigoTipoCobertura());
			formaCuenta.setVariosConvenios("nombreTipoCobertura_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getNombreTipoCobertura());
			formaCuenta.setVariosConvenios("esReporteAtencionInicialUrgencias_"+cont, mundoConvenio.getReporte_atencion_ini_urg());
			formaCuenta.setVariosConvenios("esConvenioSoat_"+cont, Convenio.esConvenioTipoConventioEventoCatTrans(mundoConvenio.getCodigo())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			formaCuenta.setVariosConvenios("esConvenioVerificacion_"+cont,mundoConvenio.isRequiereVerificacionDerechos()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			formaCuenta.setVariosConvenios("manejoComplejidad_"+cont, mundoConvenio.getManejaComplejidad());
			formaCuenta.setVariosConvenios("semanasMinimasCotizacion_"+cont, mundoConvenio.getSemanasMinimasCotizacion());
			formaCuenta.setVariosConvenios("codigoNaturaleza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getNaturalezaPaciente());
			formaCuenta.setVariosConvenios("nombreNaturaleza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getDescripcionNaturalezaPaciente());
			formaCuenta.setVariosConvenios("numeroCarnet_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getNroCarnet());
			formaCuenta.setVariosConvenios("numeroPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getNroPoliza());
			formaCuenta.setVariosConvenios("autorizacionIngreso_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getNroAutorizacion());
			formaCuenta.setVariosConvenios("numeroSolicitudVolante_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getNumeroSolicitudVolante());
			formaCuenta.setVariosConvenios("esConvenioColsanitas_"+cont, UtilidadesFacturacion.esConvenioColsanitas(con, mundoConvenio.getCodigo(), medico.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			
			formaCuenta.setVariosConvenios("isConvenioManejaMontos",mundoConvenio.isConvenioManejaMontoCobro());
			
			//Se carga la informacion de poliza del convenio ----------------------------------------------------------------------------
			/*
			if(mundoCuenta.getCuenta().getConvenios()[i].isSubCuentaPoliza())
			{
				formaCuenta.setVariosConvenios("esConvenioPoliza_"+cont, ConstantesBD.acronimoSi);
				formaCuenta.setVariosConvenios("apellidosPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getApellidos());
				formaCuenta.setVariosConvenios("nombresPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getNombres());
				formaCuenta.setVariosConvenios("tipoIdPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getCodigoTipoIdentificacion());
				formaCuenta.setVariosConvenios("descripcionTipoIdPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getDescripcionTipoIdentificacion());
				formaCuenta.setVariosConvenios("numeroIdPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getNumeroIdentificacion());
				formaCuenta.setVariosConvenios("direccionPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getDireccion());
				formaCuenta.setVariosConvenios("telefonoPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getTelefono());
				
				formaCuenta.setVariosConvenios("numDatosPoliza_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getSizeInformacionPoliza()+"");
				double saldo = 0;
				for(int j=0;j<mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getSizeInformacionPoliza();j++)
				{
					formaCuenta.setVariosConvenios("codigoPoliza_"+cont+"_"+j, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getCodigoInformacionPoliza(j));
					formaCuenta.setVariosConvenios("fechaPoliza_"+cont+"_"+j, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getFechaInformacionPoliza(j));
					formaCuenta.setVariosConvenios("autorizacionPoliza_"+cont+"_"+j, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getAutorizacionInformacionPoliza(j));
					formaCuenta.setVariosConvenios("valorPoliza_"+cont+"_"+j, mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getValorInformacionPoliza(j));
					saldo += Double.parseDouble(mundoCuenta.getCuenta().getConvenios()[i].getTitularPoliza().getValorInformacionPoliza(j));
				}
				formaCuenta.setVariosConvenios("saldoPoliza_"+cont, UtilidadTexto.formatearValores(saldo,"0.00"));
				
			}
			else
			*/
				formaCuenta.setVariosConvenios("esConvenioPoliza_"+cont, ConstantesBD.acronimoNo);
			
			//Se cargan los requisitos del paciente del convenio -----------------------------------------------------------------
			//1) Se cargan los requisirtos que el convenio tiene hasta el momento registrados
			numReqIngreso = 0;
			numReqEgreso = 0;
			for(int j=0;j<mundoCuenta.getCuenta().getConvenios()[i].getRequisitosPaciente().size();j++)
			{
				DtoRequsitosPaciente requisitos = (DtoRequsitosPaciente)mundoCuenta.getCuenta().getConvenios()[i].getRequisitosPaciente().get(j);
				if(requisitos.getTipo().equals(ConstantesIntegridadDominio.acronimoIngreso))
				{
					formaCuenta.setVariosConvenios("codigoReqIngreso_"+cont+"_"+numReqIngreso, requisitos.getCodigo()+"");
					formaCuenta.setVariosConvenios("descripcionReqIngreso_"+cont+"_"+numReqIngreso, requisitos.getDescripcion());
					formaCuenta.setVariosConvenios("cumplidoReqIngreso_"+cont+"_"+numReqIngreso, requisitos.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
					numReqIngreso++;
					
				}
				else if(requisitos.getTipo().equals(ConstantesIntegridadDominio.acronimoEgreso))
				{
					formaCuenta.setVariosConvenios("codigoReqEgreso_"+cont+"_"+numReqEgreso, requisitos.getCodigo()+"");
					formaCuenta.setVariosConvenios("descripcionReqEgreso_"+cont+"_"+numReqEgreso, requisitos.getDescripcion());
					formaCuenta.setVariosConvenios("cumplidoReqEgreso_"+cont+"_"+numReqEgreso, requisitos.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
					numReqEgreso++;
				}
			}
			//2) Se verifica si se han parametrizado mas requisitos para adicionarlos
			arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
					con,
					mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo(), 
					ConstantesIntegridadDominio.acronimoIngreso, 
					true, 
					medico.getCodigoInstitucionInt(),
					Integer.parseInt(formaCuenta.getCuenta("codigoViaIngreso").toString()));
			
			for(int j=0;j<arregloAux.size();j++)
			{
				HashMap elemento = (HashMap)arregloAux.get(j);
				boolean existe = false;
				
				//si el requisito no existe en el mapa se adiciona
				for(int k=0;k<numReqIngreso;k++)
					if(elemento.get("codigo").toString().equals(formaCuenta.getVariosConvenios("codigoReqIngreso_"+cont+"_"+k).toString()))
						existe = true;
				
				if(!existe)
				{
					formaCuenta.setVariosConvenios("codigoReqIngreso_"+cont+"_"+numReqIngreso, elemento.get("codigo"));
					formaCuenta.setVariosConvenios("descripcionReqIngreso_"+cont+"_"+numReqIngreso, elemento.get("descripcion"));
					formaCuenta.setVariosConvenios("cumplidoReqIngreso_"+cont+"_"+numReqIngreso, ConstantesBD.acronimoNo);
					numReqIngreso++;
				}
			}
			
			arregloAux = RequisitosPacientesXConvenio.cargarRequisitosPacienteXConvenio(
					con,
					mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo(), 
					ConstantesIntegridadDominio.acronimoEgreso, 
					true, 
					medico.getCodigoInstitucionInt(),
					Integer.parseInt(formaCuenta.getCuenta("codigoViaIngreso").toString()));
			
			for(int j=0;j<arregloAux.size();j++)
			{
				HashMap elemento = (HashMap)arregloAux.get(j);
				boolean existe = false;
				
				//si el requisito no existe en el mapa se adiciona
				for(int k=0;k<numReqEgreso;k++)
					if(elemento.get("codigo").toString().equals(formaCuenta.getVariosConvenios("codigoReqEgreso_"+cont+"_"+k).toString()))
						existe = true;
				
				if(!existe)
				{
					formaCuenta.setVariosConvenios("codigoReqEgreso_"+cont+"_"+numReqEgreso, elemento.get("codigo"));
					formaCuenta.setVariosConvenios("descripcionReqEgreso_"+cont+"_"+numReqEgreso, elemento.get("descripcion"));
					formaCuenta.setVariosConvenios("cumplidoReqEgreso_"+cont+"_"+numReqEgreso, ConstantesBD.acronimoNo);
					numReqEgreso++;
				}
			}
			
			formaCuenta.setVariosConvenios("numReqIngreso_"+cont, numReqIngreso+"");
			formaCuenta.setVariosConvenios("numReqEgreso_"+cont, numReqEgreso+"");
			formaCuenta.setVariosConvenios("tieneRequisitosPaciente_"+cont, (numReqEgreso>0||numReqIngreso>0)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			
			//se llena la informacion de verificacion de derechos -----------------------------------------------------
			formaCuenta.setVariosConvenios("seccionVerificacionDerechos_"+cont, ConstantesBD.acronimoNo);
			formaCuenta.setVariosConvenios("tieneVerificacionDerechos_"+cont, 
					!mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getSubCuenta().equals("")?
							ConstantesBD.acronimoSi:
							ConstantesBD.acronimoNo);
			formaCuenta.setVariosConvenios("ingresoVerificacionDerechos_"+cont,formaCuenta.getVariosConvenios("tieneVerificacionDerechos_"+cont));
			
			if(mundoCuenta.getCuenta().getConvenios()[i].isSubCuentaVerificacionDerechos())
			{
				formaCuenta.setVariosConvenios("codigoEstado_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getCodigoEstado());
				formaCuenta.setVariosConvenios("nombreEstado_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getDescripcionEstado());
				formaCuenta.setVariosConvenios("codigoTipo_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getCodigoTipo());
				formaCuenta.setVariosConvenios("nombreTipo_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getDescripcionTipo());
				formaCuenta.setVariosConvenios("numero_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getNumero());
				formaCuenta.setVariosConvenios("personaSolicita_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getPersonaSolicita());
				formaCuenta.setVariosConvenios("fechaSolicitud_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getFechaSolicitud());
				formaCuenta.setVariosConvenios("horaSolicitud_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getHoraSolicitud());
				formaCuenta.setVariosConvenios("personaContactada_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getPersonaContactada());
				formaCuenta.setVariosConvenios("fechaVerificacion_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getFechaVerificacion());
				formaCuenta.setVariosConvenios("horaVerificacion_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getHoraVerificacion());
				formaCuenta.setVariosConvenios("porcentajeCobertura_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getPorcentajeCobertura());
				formaCuenta.setVariosConvenios("cuotaVerificacion_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getCuotaVerificacion());
				formaCuenta.setVariosConvenios("observaciones_"+cont, mundoCuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getObservaciones());
			}
			else
			{
				formaCuenta.setVariosConvenios("codigoEstado_"+cont, "");
				formaCuenta.setVariosConvenios("codigoTipo_"+cont, "");
				formaCuenta.setVariosConvenios("numero_"+cont, "");
				formaCuenta.setVariosConvenios("personaSolicita_"+cont, medico.getNombreUsuario());
				formaCuenta.setVariosConvenios("fechaSolicitud_"+cont, UtilidadFecha.getFechaActual(con));
				formaCuenta.setVariosConvenios("horaSolicitud_"+cont, UtilidadFecha.getHoraActual(con));
				formaCuenta.setVariosConvenios("fechaVerificacion_"+cont, UtilidadFecha.getFechaActual(con));
				formaCuenta.setVariosConvenios("horaVerificacion_"+cont, UtilidadFecha.getHoraActual(con));
			}
			
			
			cont++;
		}
		formaCuenta.setVariosConvenios("numRegistros", cont+"");
		formaCuenta.setCuenta("seccionVariosConvenios", ConstantesBD.acronimoNo);
		
		//*******************************SE CARGA LA VERIFICACION DE DERECHOS***************************************************************
		//se llena la informacion de verificacion de derechos
		formaCuenta.setCuenta("seccionVerificacionDerechos", ConstantesBD.acronimoNo);
		
		//Se verifica si hay verificacion de derechos
		if(!mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getSubCuenta().equals(""))
		{
			formaCuenta.setCuenta("tieneVerificacionDerechos",ConstantesBD.acronimoSi);
			formaCuenta.setVerificacion("codigoEstado", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getCodigoEstado());
			formaCuenta.setVerificacion("nombreEstado", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getDescripcionEstado());
			formaCuenta.setVerificacion("codigoTipo", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getCodigoTipo());
			formaCuenta.setVerificacion("nombreTipo", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getDescripcionTipo());
			formaCuenta.setVerificacion("numero", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getNumero());
			formaCuenta.setVerificacion("personaSolicita", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getPersonaSolicita());
			formaCuenta.setVerificacion("fechaSolicitud", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getFechaSolicitud());
			formaCuenta.setVerificacion("horaSolicitud", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getHoraSolicitud());
			formaCuenta.setVerificacion("personaContactada", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getPersonaContactada());
			formaCuenta.setVerificacion("fechaVerificacion", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getFechaVerificacion());
			formaCuenta.setVerificacion("horaVerificacion", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getHoraVerificacion());
			formaCuenta.setVerificacion("porcentajeCobertura", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getPorcentajeCobertura());
			formaCuenta.setVerificacion("cuotaVerificacion", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getCuotaVerificacion());
			formaCuenta.setVerificacion("observaciones", mundoCuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getObservaciones());
			
		}
		else
		{
			formaCuenta.setCuenta("tieneVerificacionDerechos",ConstantesBD.acronimoNo);
			formaCuenta.setVerificacion("codigoEstado", "");
			formaCuenta.setVerificacion("codigoTipo", "");
			formaCuenta.setVerificacion("personaSolicita", medico.getNombreUsuario());
			formaCuenta.setVerificacion("fechaSolicitud", UtilidadFecha.getFechaActual(con));
			formaCuenta.setVerificacion("horaSolicitud", UtilidadFecha.getHoraActual(con));
			formaCuenta.setVerificacion("fechaVerificacion", UtilidadFecha.getFechaActual(con));
			formaCuenta.setVerificacion("horaVerificacion", UtilidadFecha.getHoraActual(con));
		}
		
		
		
		//*********************************************************************************************************************************
		
		//***************************SE CARGA EL RESPONSABLE DEL PACIENTE*******************************************************************
		ParametrizacionInstitucion mundoInstitucion = new ParametrizacionInstitucion();
		mundoInstitucion.cargar(con, medico.getCodigoInstitucionInt());
		
		//Se llena el responsable del paciente
		formaCuenta.setCuenta("seccionResponsablePaciente", ConstantesBD.acronimoNo);
		formaCuenta.setCuenta("tieneResponsablePaciente", mundoCuenta.getCuenta().isTieneResponsablePaciente()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		//Edicion de los datos del responsable (APLICA PARA GARANTIAS)
		if(mundoCuenta.getCuenta().isTieneResponsablePaciente())
			formaCuenta.setCuenta("datosResponsablePaciente", mundoCuenta.getCuenta().getResponsablePaciente().getTipoIdentificacion()+ConstantesBD.separadorSplit+mundoCuenta.getCuenta().getResponsablePaciente().getNumeroIdentificacion());
		else
			formaCuenta.setCuenta("datosResponsablePaciente", ConstantesBD.codigoNuncaValido+"");
		
		formaCuenta.setResponsable("codigo", mundoCuenta.getCuenta().getResponsablePaciente().getCodigo());
		formaCuenta.setResponsable("codigoOriginal", mundoCuenta.getCuenta().getResponsablePaciente().getCodigo()); //se toma el codigo anterior del responsable
		
		
		if(mundoCuenta.getCuenta().getResponsablePaciente().getCodigo() != null && !mundoCuenta.getCuenta().getResponsablePaciente().getCodigo().equals(""))
		{
			formaCuenta.setResponsable("existe", ConstantesBD.acronimoSi);
			formaCuenta.setResponsable("editable", ConstantesBD.acronimoSi);
			
			//SE llena el pais de expedicion
			if(!mundoCuenta.getCuenta().getResponsablePaciente().getCodigoPaisExpedicion().equals(""))
			{
				formaCuenta.setResponsable("paisExpedicion", mundoCuenta.getCuenta().getResponsablePaciente().getCodigoPaisExpedicion());
				formaCuenta.setResponsable("nombrePaisExpedicion", mundoCuenta.getCuenta().getResponsablePaciente().getDescripcionPaisExpedicion());
				formaCuenta.setCiudadesExp(Utilidades.obtenerCiudadesXPais(con, mundoCuenta.getCuenta().getResponsablePaciente().getCodigoPaisExpedicion()));
			}
			
			//SE llena el pais 
			if(!mundoCuenta.getCuenta().getResponsablePaciente().getCodigoPais().equals(""))
			{
				formaCuenta.setResponsable("pais", mundoCuenta.getCuenta().getResponsablePaciente().getCodigoPais());
				formaCuenta.setResponsable("nombrePais", mundoCuenta.getCuenta().getResponsablePaciente().getDescripcionPais());
				formaCuenta.setCiudades(Utilidades.obtenerCiudadesXPais(con, mundoCuenta.getCuenta().getResponsablePaciente().getCodigoPais()));
			}
		}
		else
		{
			formaCuenta.setResponsable("editable", ConstantesBD.acronimoNo);
			formaCuenta.setResponsable("existe", ConstantesBD.acronimoNo);
			
			//Se llena el pais de expedicion
			if(!mundoInstitucion.getPais().getCodigo().equals(""))
			{
				formaCuenta.setResponsable("paisExpedicion", mundoInstitucion.getPais().getCodigo());
				formaCuenta.setCiudadesExp(Utilidades.obtenerCiudadesXPais(con, mundoInstitucion.getPais().getCodigo()));
			}
			else
			{
				formaCuenta.setResponsable("paisExpedicion", "");
				formaCuenta.setCiudadesExp(new ArrayList<HashMap<String,Object>>());
			}
			
			//Se llena el pais
			if(!mundoInstitucion.getPais().getCodigo().equals(""))
			{
				formaCuenta.setResponsable("pais", mundoInstitucion.getPais().getCodigo());
				formaCuenta.setCiudades(Utilidades.obtenerCiudadesXPais(con, mundoInstitucion.getPais().getCodigo()));
			}
			else
			{
				formaCuenta.setResponsable("pais", "");
				formaCuenta.setCiudades(new ArrayList<HashMap<String,Object>>());
			}
		}
		formaCuenta.setResponsable("codigoTipoIdentificacion", mundoCuenta.getCuenta().getResponsablePaciente().getTipoIdentificacion());
		formaCuenta.setResponsable("descripcionTipoIdentificacion", mundoCuenta.getCuenta().getResponsablePaciente().getDescripcionTipoIdentificacion());
		formaCuenta.setResponsable("numeroIdentificacion", mundoCuenta.getCuenta().getResponsablePaciente().getNumeroIdentificacion());
		
		if(!mundoCuenta.getCuenta().getResponsablePaciente().getCodigoCiudadExpedicion().equals(""))
		{
			formaCuenta.setResponsable("ciudadExpedicion", mundoCuenta.getCuenta().getResponsablePaciente().getCodigoDeptoExpedicion()+ConstantesBD.separadorSplit+mundoCuenta.getCuenta().getResponsablePaciente().getCodigoCiudadExpedicion());
			formaCuenta.setResponsable("nombreCiudadExpedicion", mundoCuenta.getCuenta().getResponsablePaciente().getDescripcionCiudadExpedicion());
		}
		else
			formaCuenta.setResponsable("ciudadExpedicion", "");
		
		formaCuenta.setResponsable("primerApellido", mundoCuenta.getCuenta().getResponsablePaciente().getPrimerApellido());
		formaCuenta.setResponsable("segundoApellido", mundoCuenta.getCuenta().getResponsablePaciente().getSegundoApellido());
		formaCuenta.setResponsable("primerNombre", mundoCuenta.getCuenta().getResponsablePaciente().getPrimerNombre());
		formaCuenta.setResponsable("segundoNombre", mundoCuenta.getCuenta().getResponsablePaciente().getSegundoNombre());
		formaCuenta.setResponsable("direccion", mundoCuenta.getCuenta().getResponsablePaciente().getDireccion());
		
		
		
		
		if(!mundoCuenta.getCuenta().getResponsablePaciente().getCodigoCiudad().equals(""))
		{
			formaCuenta.setResponsable("ciudad", mundoCuenta.getCuenta().getResponsablePaciente().getCodigoDepto()+ConstantesBD.separadorSplit+mundoCuenta.getCuenta().getResponsablePaciente().getCodigoCiudad());
			formaCuenta.setResponsable("nombreCiudad", mundoCuenta.getCuenta().getResponsablePaciente().getDescripcionCiudad());
		}
		else
			formaCuenta.setResponsable("ciudad", "");
		formaCuenta.setResponsable("codigoBarrio", mundoCuenta.getCuenta().getResponsablePaciente().getCodigoBarrio());
		formaCuenta.setResponsable("nombreBarrio", mundoCuenta.getCuenta().getResponsablePaciente().getDescripcionBarrio());
		formaCuenta.setResponsable("telefono", mundoCuenta.getCuenta().getResponsablePaciente().getTelefono());
		formaCuenta.setResponsable("fechaNacimiento", mundoCuenta.getCuenta().getResponsablePaciente().getFechaNacimiento());
		formaCuenta.setResponsable("relacionPaciente", mundoCuenta.getCuenta().getResponsablePaciente().getRelacionPaciente());
		
		//******************SE TOMAN CAMPOS VALIDACIONES SEGUN V�A DE INGRESO*****************************************
		//Consulta de los campos de validacion x via de ingreso
		ViasIngreso mundoViaIngreso = new ViasIngreso();
		HashMap datosViaIng = mundoViaIngreso.consultarViasIngresoEspecifico(con, Integer.parseInt(formaCuenta.getCuenta("codigoViaIngreso").toString()));
		//A) Indicativo de la verificacion
		formaCuenta.setCuenta("esViaIngresoVerificacion",UtilidadTexto.getBoolean(datosViaIng.get("verificacion_0")+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		//B) Indicativo de requerido responsable paciente
		formaCuenta.setCuenta("esRequeridoResponsablePaciente",UtilidadTexto.getBoolean(datosViaIng.get("paciente_0")+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		//**************VERIFICACION DE MANEJO DE COMPLEJIDAD************************************
		formaCuenta.setCuenta("indicadorManejaComplejidad", validacionManejoComplejidad(formaCuenta));
		
		//*************�PUEDO MODIFICAR AREA?**********************************************************
		formaCuenta.setPuedoModificarArea(Cuenta.puedoModificarAreaCuenta(
			con, 
			formaCuenta.getCuenta("idCuenta").toString(), 
			Integer.parseInt(formaCuenta.getCuenta("codigoViaIngreso").toString()), 
			paciente.getExisteAsocio()));
		
		
		//****************************VALIDACIONES QUE TIENEN QUE VER CON EL FOSYGA*************************************
		//se verifica si el ingreso tiene registro en accidentes de tr�nsito
		formaCuenta.setTieneRegistroAccidenteTransito(UtilidadValidacion.esAccidenteTransitoEstadoDado(con,Integer.parseInt(formaCuenta.getCuenta("idIngreso").toString()),
				ConstantesIntegridadDominio.acronimoEstadoPendiente+"','"+ConstantesIntegridadDominio.acronimoEstadoProcesado));
		if(formaCuenta.isTieneRegistroAccidenteTransito())
			formaCuenta.setTieneRegistroAccidenteTransitoAnulado(false);
		else
			formaCuenta.setTieneRegistroAccidenteTransitoAnulado(UtilidadValidacion.esAccidenteTransitoEstadoDado(con,Integer.parseInt(formaCuenta.getCuenta("idIngreso").toString()),ConstantesIntegridadDominio.acronimoEstadoAnulado));
		
		
		//Se verifica si hay permisos para ir a la funcionalidad de accidentes de tr�nsito
		formaCuenta.setPermisosAccidenteTransito(Utilidades.tieneRolFuncionalidad(con,medico.getLoginUsuario(),505));
		
		//se verifica si el ingreso tiene registro en eventos catastroficos
		formaCuenta.setTieneRegistroEventoCatastrofico(UtilidadValidacion.esEventoCatastroficoEstadoDado(con, Integer.parseInt(formaCuenta.getCuenta("idIngreso").toString()),
				ConstantesIntegridadDominio.acronimoEstadoPendiente+"','"+ConstantesIntegridadDominio.acronimoEstadoFinalizado));
		if(formaCuenta.isTieneRegistroEventoCatastrofico())
			formaCuenta.setTieneRegistroEventoCatastroficoAnulado(false);
		else
			formaCuenta.setTieneRegistroEventoCatastroficoAnulado(UtilidadValidacion.esEventoCatastroficoEstadoDado(con,Integer.parseInt(formaCuenta.getCuenta("idIngreso").toString()),ConstantesIntegridadDominio.acronimoEstadoAnulado));
		
		//Se verifica si hay permisos para ir a la funcionalidad de evento catastrofico
		formaCuenta.setPermisosEventoCatastrofico(Utilidades.tieneRolFuncionalidad(con,medico.getLoginUsuario(),555));
		//************************************************************************************************************
		
		//*************************** ASIGNAR CAMA *******************************************************************
		if (mundoCuenta.getCuenta().getCodigoViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion && mundoCuenta.getCuenta().getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
				formaCuenta.setPuedoModificarCama(UtilidadesManejoPaciente.obtenerCamaAdmision(con, Integer.parseInt(mundoCuenta.getCuenta().getIdCuenta())));
		formaCuenta.setCuenta("seccionAsignarCama", ConstantesBD.acronimoSi);
		formaCuenta.setCentroAtencion(medico.getCodigoCentroAtencion()+"");
		//************************************************************************************************************
		
		
		//***********************VALIDACIONES QUE TIENEN QUE VER CON REFERENCIA *****************************************
		//Se verifica si se puede modificar el origen de admision
		String estadoRefExt = UtilidadesHistoriaClinica.getEstadoReferenciaXIngreso(con, formaCuenta.getCuenta("idIngreso").toString(), ConstantesIntegridadDominio.acronimoExterna);
		if(!estadoRefExt.equals(ConstantesIntegridadDominio.acronimoEstadoAnulado)&&!estadoRefExt.equals("")&&
			formaCuenta.getCuenta("codigoOrigenAdmision").toString().equals(ConstantesBD.codigoOrigenAdmisionHospitalariaEsRemitido+""))
			formaCuenta.setPuedoModificarOrigenAdmision(false);
		else
			formaCuenta.setPuedoModificarOrigenAdmision(true);
		//****************************************************************************************************************
		
		//*********************************SE CARGAN ESTRUCTURAS**************************************************************************
		//1) Se cargan los convenios de ARP
		formaCuenta.setConveniosArp(UtilidadesFacturacion.cargarConveniosXClasificacion(con, ConstantesBDFacturacion.codigoClasTipoConvenioARP));
		
		//2) Se cargan Or�genes de admison -------------------------------------------------------------------------------
		formaCuenta.setOrigenesAdmisiones(UtilidadesManejoPaciente.obtenerOrigenesAdmision(con));
		//Se filtran los origenes de admision segun la cuenta
		int viaIngreso = Integer.parseInt(formaCuenta.getCuenta("codigoViaIngreso").toString());
		ArrayList<HashMap<String, Object>> nuevoArreglo = new ArrayList<HashMap<String,Object>>();
		for(int i=0;i<formaCuenta.getOrigenesAdmisiones().size();i++)
		{
			HashMap<String,Object> nuevoMapa = new HashMap<String,Object>();
			
			HashMap registro = (HashMap)formaCuenta.getOrigenesAdmisiones().get(i);
			int codigo = Integer.parseInt(registro.get("codigo").toString());
			
			if(viaIngreso!=ConstantesBD.codigoViaIngresoAmbulatorios&&
				codigo == ConstantesBD.codigoOrigenAdmisionHospitalariaEsUrgencias )
			{
				nuevoMapa.put("codigo",registro.get("codigo"));
				nuevoMapa.put("nombre",registro.get("nombre"));
				nuevoArreglo.add(nuevoMapa);
			}
			else if(viaIngreso!=ConstantesBD.codigoViaIngresoUrgencias&&
				codigo == ConstantesBD.codigoOrigenAdmisionHospitalariaEsConsultaExterna)
			{
				nuevoMapa.put("codigo",registro.get("codigo"));
				nuevoMapa.put("nombre",registro.get("nombre"));
				nuevoArreglo.add(nuevoMapa);
			}
			else if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion&&
					codigo == ConstantesBD.codigoOrigenAdmisionHospitalariaEsNacidoInstitucion)
			{
				nuevoMapa.put("codigo",registro.get("codigo"));
				nuevoMapa.put("nombre",registro.get("nombre"));
				nuevoArreglo.add(nuevoMapa);
			}
			else if(codigo == ConstantesBD.codigoOrigenAdmisionHospitalariaEsRemitido)
			{
				nuevoMapa.put("codigo",registro.get("codigo"));
				nuevoMapa.put("nombre",registro.get("nombre"));
				nuevoArreglo.add(nuevoMapa);
			}
		}
			
		//Se a�ade nuevamente el arreglo
		formaCuenta.setOrigenesAdmisiones(nuevoArreglo);
		
		//3) Se cargan los contratos
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("esConvenioCapitado").toString()))
			formaCuenta.setContratos(UtilidadesManejoPaciente.obtenerContratosUsuarioCapitado(con, paciente.getCodigoPersona(), mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo()));
		else
			formaCuenta.setContratos(Utilidades.obtenerContratos(con, mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo(), false, true));
		
		if(formaCuenta.getCuenta("codigoContrato")!=null){
			verificarMontoCobro(formaCuenta,formaCuenta.getCuenta("codigoContrato").toString());
		}
		
		/**
		 * [0] => codigo convenio
		 * [1] => codigo tipo contrato
		 * [2] => es pyp? (boolean)
		 * [3] => empresaInstitucion
		 */
		String[] datosConvenio = formaCuenta.getCuenta("codigoConvenio").toString().split(ConstantesBD.separadorSplit);
		
		//4) Se cargan los estratos sociales
		formaCuenta.setEstratosSociales(
			UtilidadesFacturacion.cargarEstratosSociales(
				con, 
				medico.getCodigoInstitucionInt(),
				ConstantesBD.acronimoSi, 
				formaCuenta.getCuenta("codigoTipoRegimen").toString(),
				Integer.parseInt(datosConvenio[0]),
				Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso")+""),
				UtilidadFecha.conversionFormatoFechaABD(mundoCuenta.getCuenta().getFechaApertura())));
		
		formaCuenta.setCodigoEstratoSocial(formaCuenta.getCuenta().get("codigoEstratoSocial").toString());
		
		/*formaCuenta.setTiposAfiliado(UtilidadesFacturacion.cargarTiposAfiliado(
				con, medico.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, 
				mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo(),
				Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()+"")));*/
		
		HashMap tiposAfiliado = UtilidadesFacturacion.cargarTiposAfiliadoXEstrato(
				con, medico.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, 
				Utilidades.convertirAEntero(datosConvenio[0]),
				Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()),
				Utilidades.convertirAEntero(formaCuenta.getCuenta().get("codigoEstratoSocial").toString()),
				UtilidadFecha.conversionFormatoFechaABD(mundoCuenta.getCuenta().getFechaApertura()));
		
		formaCuenta.setTiposAfiliado(tiposAfiliado);
		formaCuenta.setCodigoTipoAfiliado(formaCuenta.getCuenta().get("codigoTipoAfiliado").toString());
		
		//5) Se cargan los montos de cobro
		//formaCuenta.setMontosCobro(UtilidadesFacturacion.obtenerMontosCobroXConvenio(con, mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo()+"", UtilidadFecha.conversionFormatoFechaABD(mundoCuenta.getCuenta().getFechaApertura())));
		
		//6) Se cargan los tipos de paciente
		formaCuenta.setTiposPaciente(UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, formaCuenta.getCuenta("codigoViaIngreso").toString()));
		
		//7) Se cargan las �reas del paciente
		formaCuenta.setAreas(Utilidades.getCentrosCostoXViaIngresoXCAtencion(con, Integer.parseInt(formaCuenta.getCuenta("codigoViaIngreso").toString()), medico.getCodigoCentroAtencion(), medico.getCodigoInstitucionInt(), ""));
		
		//8) Se consultan los tipos de identificaion (solo aplica para la poliza)
		formaCuenta.setTiposIdentificacion(Utilidades.obtenerTiposIdentificacion(con, "ingresoPoliza", medico.getCodigoInstitucionInt()));
		
		//9) Se consultan las naturalezas del paciente,,,,,,,,,,,,
		/*formaCuenta.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPaciente(
				con,formaCuenta.getCuenta("codigoTipoRegimen").toString(),
				mundoCuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo(),
				Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso")+"")));*/
		
		formaCuenta.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
				con, formaCuenta.getCuenta("codigoTipoRegimen").toString(), 
				Utilidades.convertirAEntero(datosConvenio[0]),
				Utilidades.convertirAEntero(formaCuenta.getCuenta("codigoViaIngreso").toString()+""), 
				formaCuenta.getCuenta().get("codigoTipoAfiliado").toString(), 
				Utilidades.convertirAEntero(formaCuenta.getCuenta().get("codigoEstratoSocial").toString()),
				UtilidadFecha.conversionFormatoFechaABD(mundoCuenta.getCuenta().getFechaApertura())));
		
		
		
		
		
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("esConvenioCapitado").toString())) {
			//si solo tiene un contrato vigente se debe postular la informacion
			if(formaCuenta.getContratos().size()==1)
			{
				//int codClasi=Utilidades.convertirAEntero(formaCuenta.getContratos().get(0).get("codigoestratosocial")+"");
				int codClasi = Integer.parseInt(formaCuenta.getCuenta("codigoEstratoSocial").toString());
				if(codClasi > 0)
				{
					String tipoRegimenCSE = Utilidades.obtenerTipoRegimenClasificacionSocioEconomica(con, codClasi);
					if(formaCuenta.getCuenta("codigoTipoRegimen").toString().equals(tipoRegimenCSE))
					{
						formaCuenta.setCuenta("codigoEstratoSocial", codClasi);
						HashMap tempo=new HashMap();
						tempo.put("numRegistros", "0");
						HashMap tempo1=new HashMap();
						tempo1.put("numRegistros", "0");
						for(int i=0;i<Utilidades.convertirAEntero(formaCuenta.getEstratosSociales("numRegistros")+"");i++)
						{
							if(Utilidades.convertirAEntero(formaCuenta.getEstratosSociales("codigo_"+i)+"")==codClasi)
							{
								tempo.put("codigo_0", formaCuenta.getEstratosSociales("codigo_"+i)+"");
								tempo.put("descripcion_0", formaCuenta.getEstratosSociales("descripcion_"+i)+"");
								tempo.put("numRegistros", "1");
								break;
							}
						}
						String tipoAfil = formaCuenta.getCuenta("codigoTipoAfiliado").toString();
						if(!UtilidadTexto.isEmpty(tipoAfil+""))
						{
							//{numRegistros=2, acronimo_0=B, nombre_1=Cotizante, nombre_0=Beneficiario, acronimo_1=C}
							formaCuenta.setCuenta("codigoTipoAfiliado", tipoAfil);
							
							for(int i=0;i<Utilidades.convertirAEntero(formaCuenta.getTiposAfiliado("numRegistros")+"");i++)
							{
								if((formaCuenta.getTiposAfiliado("acronimo_"+i)+"").equals(tipoAfil+""))
								{
									tempo1.put("acronimo_0", formaCuenta.getTiposAfiliado("acronimo_"+i)+"");
									tempo1.put("nombre_0", formaCuenta.getTiposAfiliado("nombre_"+i)+"");
									tempo1.put("numRegistros", "1");
									break;
								}
							}
	
						}
						if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitadosModificarCuenta(medico.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
						{
							formaCuenta.setEstratosSociales(tempo);
							if(!UtilidadTexto.isEmpty(tipoAfil))
							{
								formaCuenta.setTiposAfiliado(tempo1);
							}
						}	
					}
					else
					{
						formaCuenta.setEstratosSociales(new HashMap());
						formaCuenta.setTiposAfiliado(new HashMap());
						
							
					}
				}
				int natPaciente=Utilidades.convertirAEntero(formaCuenta.getContratos().get(0).get("naturalezapaciente")+"");
				if(natPaciente>0)
				{
					if(UtilidadValidacion.esNaturalezaValidaTipoRegimen(natPaciente,formaCuenta.getCuenta("codigoTipoRegimen").toString())) 
					{

						formaCuenta.setCuenta("codigoNaturaleza", natPaciente+"");
						Vector<InfoDatosString> naturalezaVector=new Vector<InfoDatosString>();
						for(int i=0;i<formaCuenta.getNaturalezasPaciente().size();i++)
						{
							if(Utilidades.convertirAEntero(formaCuenta.getNaturalezasPaciente().get(i).getCodigo()) ==natPaciente)
							{
								naturalezaVector.add(formaCuenta.getNaturalezasPaciente().get(i));
							}
						}
						if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitadosModificarCuenta(medico.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
						{
							formaCuenta.setNaturalezasPaciente(naturalezaVector);
						}
						
					
						
					}
					
				}
			}
			
		}
		
		
		
		
		
		/*if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("esConvenioCapitado").toString()))
		{
			HashMap tempo=new HashMap();
			HashMap tempo1=new HashMap();
			for(int i=0;i<Utilidades.convertirAEntero(formaCuenta.getEstratosSociales().get("numRegistros")+"");i++)
			{
				if(Utilidades.convertirAEntero(formaCuenta.getEstratosSociales().get("codigo_"+i)+"")==Utilidades.convertirAEntero(formaCuenta.getCuenta().get("codigoEstratoSocial")+""))
				{
					tempo.put("codigo_0", formaCuenta.getEstratosSociales().get("codigo_"+i)+"");
					tempo.put("descripcion_0", formaCuenta.getEstratosSociales().get("descripcion_"+i)+"");
					tempo.put("numRegistros", "1");
					break;
				}
			}
			if(!UtilidadTexto.isEmpty(formaCuenta.getCuenta().get("codigoTipoAfiliado")+""))
			{
				
				for(int i=0;i<Utilidades.convertirAEntero(formaCuenta.getTiposAfiliado().get("numRegistros")+"");i++)
				{
					if((formaCuenta.getTiposAfiliado().get("acronimo_"+i)+"").equals(formaCuenta.getCuenta().get("codigoTipoAfiliado")+""))
					{
						tempo1.put("acronimo_0", formaCuenta.getTiposAfiliado().get("acronimo_"+i)+"");
						tempo1.put("nombre_0", formaCuenta.getTiposAfiliado().get("nombre_"+i)+"");
						tempo1.put("numRegistros", "1");
						break;
					}
				}

			}
			if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitadosModificarCuenta(medico.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
			{
				formaCuenta.setEstratosSociales(tempo);
				if(!UtilidadTexto.isEmpty(formaCuenta.getCuenta().get("codigoTipoAfiliado")+""))
				{
					formaCuenta.setTiposAfiliado(tempo1);
				}
			}
			

			Vector<InfoDatosString> naturalezaVector=new Vector<InfoDatosString>();
			for(int i=0;i<formaCuenta.getNaturalezasPaciente().size();i++)
			{
				if(Utilidades.convertirAEntero(formaCuenta.getNaturalezasPaciente().get(i).getCodigo()) ==Utilidades.convertirAEntero(formaCuenta.getCuenta().get("codigoNaturaleza")+""))
				{
					naturalezaVector.add(formaCuenta.getNaturalezasPaciente().get(i));
				}
			}
			if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitadosModificarCuenta(medico.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
			{
				formaCuenta.setNaturalezasPaciente(naturalezaVector);
			}
		}*/
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//10) Se consultan los tipos de complejidad
		formaCuenta.setTiposComplejidad(Utilidades.obtenerTiposComplejidad(con));
		
		
		
		//11) Se consultan los paises
		formaCuenta.setPaises(Utilidades.obtenerPaises(con));
		
		//12) Se cargan los tipos de identificacion para el responsable paciente
		formaCuenta.setTiposIdResponsable(Utilidades.obtenerTiposIdentificacion(con, "ingresoPaciente", medico.getCodigoInstitucionInt()));
		
		//13) S� solo hab�a un convenio se carga la estructura de coberturas salud
		if(mundoCuenta.getCuenta().getConvenios().length==1)
		{
			formaCuenta.setCoberturasSalud(UtilidadesManejoPaciente.obtenerCoberturasSaludXTipoRegimen(con, formaCuenta.getCuenta("codigoTipoRegimen").toString(), medico.getCodigoInstitucionInt()));
		}
		//********************************************************************************************************************
		
		//***********************************************************************************************************************************
		
		//************************VALIDAR SI SE PUEDE MODIFICAR LA INFORMACION DE CONVENIOS*************************************************
		//Si hay N Convenios entonces no se puede modificar la informacion de convenios
		if(Integer.parseInt(formaCuenta.getVariosConvenios("numRegistros").toString())>0)
			formaCuenta.setCuenta("puedoModificarConvenio", ConstantesBD.acronimoNo);
		else
			formaCuenta.setCuenta("puedoModificarConvenio", ConstantesBD.acronimoSi);
		//************************************************************************************************************************************
		
		//Se contruyen los mensajes de validaci�n necesarios
		this.editarMensajes(con,formaCuenta);
		
		return mundoCuenta.getCuenta();
		
	}

	/**
	 * M�todo implementado para construir los mensajes de validacion
	 * @param con 
	 * @param formaCuenta
	 */
	private void editarMensajes(Connection con, ModificarCuentaForm formaCuenta) 
	{
		ArrayList<String> arregloAux = new ArrayList<String>();
		
		//1) Mensaje por N convenios
		if(Integer.parseInt(formaCuenta.getVariosConvenios("numRegistros").toString())>0)
			arregloAux.add("La informaci�n de los convenios se modifica en la funcionalidad de Distribuci�n de la Cuenta.");
		
		//2) Mensaje es requerido el responsable del paciente
		if(UtilidadTexto.getBoolean(formaCuenta.getCuenta("esRequeridoResponsablePaciente").toString()))
		{
			//Verificar que la informacion del paciente no este ingresada ya; sino mostrar mensaje de atencion
			if(!UtilidadTexto.getBoolean(formaCuenta.getResponsable("editable").toString()))
				arregloAux.add("Es requerido el ingreso de la informaci�n del responsable del paciente.");
		}
		
		//3) Se muestra mensaje de las semanas m�nimas de cotizacion del convenio principal
		if(!formaCuenta.getCuenta("semanasCotizacion").toString().equals("")&&
			!formaCuenta.getCuenta("semanasMinimasCotizacion").toString().equals(""))
		{
			try
			{
				//Si las semanas de cotizacion del convenio son menores a las m�nimas que tiene establecidas entonces se geenra mensaje de alerta
				if(Integer.parseInt(formaCuenta.getCuenta("semanasCotizacion").toString())<Integer.parseInt(formaCuenta.getCuenta("semanasMinimasCotizacion").toString()))
					arregloAux.add("Las semanas de cotizaci�n del paciente son inferiores a las semanas m�nimas requeridas por el convenio "+formaCuenta.getCuenta("nombreConvenio")+".");
			}
			catch(Exception e)
			{
				logger.error("Error al tratar de validar las semanas m�nimas de cotizacion: "+e);
			}
		}
		
		//4) Se muestra mensaje de las semanas m�nimas de cotizacion de los demas convenios
		for(int i=0;i<Integer.parseInt(formaCuenta.getVariosConvenios("numRegistros").toString());i++)
		{
			if(!formaCuenta.getVariosConvenios("semanasCotizacion_"+i).toString().equals("")&&
				!formaCuenta.getVariosConvenios("semanasMinimasCotizacion_"+i).toString().equals(""))
			{
				try
				{
					//Si las semanas de cotizacion del convenio son menores a las m�nimas que tiene establecidas entonces se geenra mensaje de alerta
					if(Integer.parseInt(formaCuenta.getVariosConvenios("semanasCotizacion_"+i).toString())<Integer.parseInt(formaCuenta.getVariosConvenios("semanasMinimasCotizacion_"+i).toString()))
						arregloAux.add("Las semanas de cotizaci�n del paciente son inferiores a las semanas m�nimas requeridas por el convenio "+formaCuenta.getVariosConvenios("nombreConvenio_"+i)+".");
				}
				catch(Exception e)
				{
					logger.error("Error al tratar de validar las semanas m�nimas de cotizacion: "+e);
				}
				
			}
		}
		
		//5) Se muestra mensaje si el ingreso fue realizado por Ingreso Pacientes Entidades SubContratadas		
		if(!formaCuenta.getCuenta("descEntidadesSubcontratadas").toString().equals(""))
			arregloAux.add("Ingreso de Paciente por Entidad SubContratada "+formaCuenta.getCuenta("descEntidadesSubcontratadas").toString());
			
		
		//6) ***********Validaciones de la autorizacion******************************
		//Se cosnultan los convenios del ingreso
		ArrayList<DtoAutorizacion> listadoAutorizaciones = UtilidadesManejoPaciente.obtenerConveniosIngresoAutorizacionesAdmision(con, Integer.parseInt(formaCuenta.getCuenta("idIngreso").toString()),Integer.parseInt(formaCuenta.getCuenta("idCuenta").toString()),false);
		String conveniosSinRespuesta = "";
		String conveniosSinAutorizacion = "";
		
		for(DtoAutorizacion autorizacion:listadoAutorizaciones)
		{
			//Se verifica si hay autorizaciones ya registradas pero no se han respondido
			if(autorizacion.isTieneAutorizacionRegistrada()&&
				(autorizacion.getDetalle().get(0).getEstadoSolDetAuto().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado)||
				autorizacion.getDetalle().get(0).getEstadoSolDetAuto().equals(ConstantesIntegridadDominio.acronimoEstadoNegado)||
				autorizacion.getDetalle().get(0).getEstadoSolDetAuto().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado)||
				autorizacion.getDetalle().get(0).getEstadoSolDetAuto().equals(""))&&
				autorizacion.isRequiereAutorizacionConvenio())
			{
				conveniosSinRespuesta += (conveniosSinRespuesta.equals("")?"":", ") + autorizacion.getNombreConvenio();
			}
			//Se verifica si un convenio no tiene autorizacion registrada y requiere
			if(autorizacion.isRequiereAutorizacionConvenio()&&!autorizacion.isTieneAutorizacionRegistrada())
			{
				conveniosSinAutorizacion += (conveniosSinAutorizacion.equals("")?"":", ") + autorizacion.getNombreConvenio();
			}
			
		}
		
		if(!conveniosSinRespuesta.equals(""))
		{
			arregloAux.add("El ingreso tiene solicitudes de autorizaci�n de admisi�n para los convenios: "+conveniosSinRespuesta+" pero a�n NO cuentan con respuesta de autorizaci�n.");
			
		}
		if(!conveniosSinAutorizacion.equals(""))
		{
			arregloAux.add("El ingreso NO cuenta con solicitudes de autorizaci�n de admisi�n para los convenios: "+conveniosSinAutorizacion+".");
		}
		
		formaCuenta.setMensajes(arregloAux);
		
	}
	
	/**
	 * Se verifica el manejo de la complejidad
	 * @param formaCuenta
	 * @return
	 */
	private String validacionManejoComplejidad(ModificarCuentaForm formaCuenta) 
	{
		String manejoComplejidad = ConstantesBD.acronimoNo;
		
		if(formaCuenta.getCuenta("manejoComplejidad")!=null&&UtilidadTexto.getBoolean(formaCuenta.getCuenta("manejoComplejidad").toString()))
			manejoComplejidad = ConstantesBD.acronimoSi;
		
		//Se verifica el manejo de complejidad en los dem�s convenios
		for(int i=0;i<Integer.parseInt(formaCuenta.getVariosConvenios("numRegistros").toString());i++)
		{
			if(UtilidadTexto.getBoolean(formaCuenta.getVariosConvenios("manejoComplejidad_"+i)+""))
				manejoComplejidad = ConstantesBD.acronimoSi;
		}
			
		return manejoComplejidad;
	}

	
	
	

	/**
	 * M�todo implementado para verificar si se eligi� REmitido y no estaba seleccionado
	 * y as� abrir la funcionalidad de referencia
	 * @param con 
	 * @param formaCuenta
	 * @param usuario 
	 * @param paciente 
	 */
	private void verificacionOrigenAdmision(Connection con, ModificarCuentaForm formaCuenta, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		if(!formaCuenta.getCuenta("codigoOrigenAdmision").equals(formaCuenta.getCuenta("codigoOrigenAdmisionAnterior"))&&
			formaCuenta.getCuenta("codigoOrigenAdmision").toString().equals(ConstantesBD.codigoOrigenAdmisionHospitalariaEsRemitido+""))
		{
			
			/**
		     * Verificacion del campo Origen Admision:
		     * Si el campo origen admision es "Remitido" y no se tiene definido el par�metro "Horas de Caducidad de referencias externas"
		     * se cancela el proceso
		     */
			String horasCaducidad = ValoresPorDefecto.getHorasCaducidadReferenciasExternas(usuario.getCodigoInstitucionInt());
			if(horasCaducidad!=null&&!horasCaducidad.equals(""))
				///Se realiza la anulacion de las referencias externas caducadas
				Referencia.anularReferenciasExternasCaducadas(con, paciente.getCodigoPersona()+"", horasCaducidad, usuario.getLoginUsuario());
			
			///Se verifica si ya hay una referencia externa en tramite para el paciente
			String numeroReferencia = UtilidadesHistoriaClinica.getUltimaReferenciaPaciente(con, paciente.getCodigoPersona()+"", ConstantesIntegridadDominio.acronimoExterna, ConstantesIntegridadDominio.acronimoEstadoEnTramite);
			if(!numeroReferencia.equals(""))
			{
				//Si existe se actualiza el estado de la referencia
				UtilidadesHistoriaClinica.actualizarEstadoReferencia(con, numeroReferencia, paciente.getCodigoIngreso()+"",ConstantesIntegridadDominio.acronimoEstadoAdmitido, usuario.getLoginUsuario());
				formaCuenta.setDeboAbrirReferencia(false);
				formaCuenta.setPathReferencia("");
			}
			else
			{
				//De lo contrario se se�ala que se debe abrir el formualrio de la referencia despu�s de modificar la cuenta
				formaCuenta.setDeboAbrirReferencia(UtilidadTexto.getBoolean(ValoresPorDefecto.getLlamadoAutomaticoReferencia(usuario.getCodigoInstitucionInt())));
				String pathReferencia =  "../referenciaDummy/referenciaDummy.do?estado=empezarReferencia"+
					"&tipoIdentificacion="+paciente.getCodigoTipoIdentificacionPersona()+"-"+paciente.getTipoIdentificacionPersona(false)+
					"&numeroIdentificacion="+paciente.getNumeroIdentificacionPersona()+
					"&tipoReferencia="+ConstantesIntegridadDominio.acronimoExterna+
					"&deboAbrirAsignacionCita=false";
				formaCuenta.setPathReferencia(pathReferencia);
			}
			
			
		}
		else
		{
			formaCuenta.setDeboAbrirReferencia(false);
			formaCuenta.setPathReferencia("");
		}
		
		logger.info("deboAbrirREferencia=> "+formaCuenta.isDeboAbrirReferencia());
		logger.info("codigo Origen Admision=> "+formaCuenta.getCuenta("codigoOrigenAdmision"));
		logger.info("codigo Origen Admision Anterior=> "+formaCuenta.getCuenta("codigoOrigenAdmisionAnterior"));
	}

	/**
	 * M�todo que verifica el registro de evento catastrofico al guardar la cuenta
	 * @param con
	 * @param formaCuenta
	 * @param usuario
	 * @param paciente 
	 * @return
	 */
	private int verificacionRegistroEventoCatastrofico(Connection con, ModificarCuentaForm formaCuenta, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		int exitoVal0 = 1;
		boolean huboActualizacion = false;
		RegistroEventosCatastroficos mundoCatastrofico = new RegistroEventosCatastroficos();
		DtoRegistroEventosCatastroficos dtoCatastrofico = new DtoRegistroEventosCatastroficos();
		dtoCatastrofico.setFechaModifica(UtilidadFecha.getFechaActual());
		dtoCatastrofico.setHoraModifica(UtilidadFecha.getHoraActual());
		dtoCatastrofico.setUsuarioModifica(usuario.getLoginUsuario());
		dtoCatastrofico.setIngreso(Integer.parseInt(formaCuenta.getCuenta("idIngreso").toString()));
		//Se carga el dto antiguo
		DtoRegistroEventosCatastroficos dtoAntiguo = mundoCatastrofico.consultarRegistroEventoCatastroficoIngreso(con, dtoCatastrofico.getIngreso(),"");
		
		if(formaCuenta.isTieneRegistroEventoCatastrofico()&&
				!formaCuenta.getCuenta("codigoTipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoEventoCatastrofico))
		{
			dtoCatastrofico.setEstadoRegistro(ConstantesIntegridadDominio.acronimoEstadoAnulado);
			
			
			exitoVal0 = this.verificacionTipoEventoAsocios(con,formaCuenta,paciente,ConstantesIntegridadDominio.acronimoEventoCatastrofico);
			
			if(exitoVal0>0)
				exitoVal0 = mundoCatastrofico.actualizarEstadoRegistroEventoCatastrofico(con,dtoCatastrofico);
			huboActualizacion = true;
		}
		else if(formaCuenta.isTieneRegistroEventoCatastroficoAnulado()&&
			formaCuenta.getCuenta("codigoTipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoEventoCatastrofico))
		{
			dtoCatastrofico.setEstadoRegistro(ConstantesIntegridadDominio.acronimoEstadoPendiente);
			exitoVal0 = mundoCatastrofico.actualizarEstadoRegistroEventoCatastrofico(con,dtoCatastrofico);
			huboActualizacion = true;
		}
		if(exitoVal0<=0)
		{
			return exitoVal0;
		}
		else if(huboActualizacion)
		{
			//***********SE GENERA LOG***********************************************************************************************
			 String log="\n            ====INFORMACION ORIGINAL REGISTRO EVENTO CATASTROFICO DE LA CUENTA "+formaCuenta.getCuenta("idCuenta")+"===== " +
				"\n*  Estado [" +ValoresPorDefecto.getIntegridadDominio(dtoAntiguo.getEstadoRegistro())+"] ";
			 if(dtoAntiguo.getEstadoRegistro().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
			 {
				log += "\n*  Fecha Anulaci�n ["+dtoAntiguo.getFechaModifica()+"] " +
				"\n*  Hora Anulaci�n ["+dtoAntiguo.getHoraModifica()+"] "+
				"\n*  Usuario Anulaci�n ["+dtoAntiguo.getUsuarioModifica()+"] ";
			 }
			 log+="\n\n            ====INFORMACION DESPU�S DE LA MODIFICACI�N DE REGISTRO EVENTO CATASTROFICO EN LA CUENTA "+formaCuenta.getCuenta("idCuenta")+"===== " +
			 	"\n*  Estado [" +ValoresPorDefecto.getIntegridadDominio(dtoCatastrofico.getEstadoRegistro())+"] ";
			 if(dtoCatastrofico.getEstadoRegistro().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
			 {
				log += "\n*  Fecha Anulaci�n ["+dtoCatastrofico.getFechaModifica()+"] " +
				"\n*  Hora Anulaci�n ["+dtoCatastrofico.getHoraModifica()+"] "+
				"\n*  Usuario Anulaci�n ["+dtoCatastrofico.getUsuarioModifica()+"] ";
			 }
				
			 log+="\n========================================================\n\n\n " ;
			LogsAxioma.enviarLog(ConstantesBD.logModificarCuentaCodigo, log, ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
			//***************************************************************************************************************************************
			
		}
		//Se verifica si se cheque� evento catastrofico y no se ha ingresado registro********
		if(formaCuenta.getCuenta("codigoTipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoEventoCatastrofico)&&
			!formaCuenta.isTieneRegistroEventoCatastrofico()&&!formaCuenta.isTieneRegistroEventoCatastroficoAnulado())
			formaCuenta.setMostrarVentanaEventoCatastrofico(true);
		else
			formaCuenta.setMostrarVentanaEventoCatastrofico(false);
		
		return exitoVal0;
	}

	/**
	 * M�todo que verifica que un tipo evento anulado tambi�n se deseleccione en la cuenta asociadad del paciente
	 * si existe
	 * @param con
	 * @param formaCuenta
	 * @param paciente
	 * @param tipoEvento
	 * @return
	 */
	private int verificacionTipoEventoAsocios(Connection con, ModificarCuentaForm formaCuenta, PersonaBasica paciente,String tipoEvento) 
	{
		int resp = 1;
		///Se verifica si hay asocio. Si hay asocio entonces se verifica si la cuenta asociada tambi�n tiene
		//como el tipo evento deseleccionado para quitarlo
		if(paciente.getExisteAsocio())
		{
			String idCuentaAsocio = "";
			if(Integer.parseInt(formaCuenta.getCuenta("idCuenta").toString())!=paciente.getCodigoCuentaAsocio())
				idCuentaAsocio = paciente.getCodigoCuentaAsocio()+"";
			else
				idCuentaAsocio = paciente.getCodigoCuenta()+"";
		
			String tipoEventoAsocio = Cuenta.obtenerCodigoTipoEventoCuenta(con, idCuentaAsocio);
			
			
			if(tipoEvento.toString().equals(tipoEventoAsocio))
				resp = Cuenta.actualizarTipoEventoCuenta(con, "", idCuentaAsocio);
			
			
		}
		return resp;
	}

	/**
	 * M�todo que realiza la validacion del evento de registro de accidentes de tr�nsito
	 * @param con
	 * @param formaCuenta
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private int verificacionRegistroAccidenteTransito(Connection con, ModificarCuentaForm formaCuenta, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		int exitoVal0 = 1;
		boolean huboActualizacion = false;
		RegistroAccidentesTransito mundoTransito = new RegistroAccidentesTransito();
		DtoRegistroAccidentesTransito dtoTransito = new DtoRegistroAccidentesTransito();
		dtoTransito.setFechaAnulacion(UtilidadFecha.getFechaActual());
		dtoTransito.setHoraAnulacion(UtilidadFecha.getHoraActual());
		dtoTransito.setUsuarioAnulacion(usuario.getLoginUsuario());
		dtoTransito.setIngreso(formaCuenta.getCuenta("idIngreso").toString());
		
		//Se carga el dto antiguo
		DtoRegistroAccidentesTransito dtoAntiguo = mundoTransito.consultarRegistroAccidentesTransitoIngreso(con,dtoTransito.getIngreso());
		
		//Si se cambi� el tipo de evento a algo diferente a Accidente de Transito y ya se ten�a un registro de Accidente de Tr�nsito
		// Se debe anular
		if(formaCuenta.isTieneRegistroAccidenteTransito()&&
			!formaCuenta.getCuenta("codigoTipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoAccidenteTransito))
		{
			dtoTransito.setEstadoRegistro(ConstantesIntegridadDominio.acronimoEstadoAnulado);
			
			exitoVal0 = this.verificacionTipoEventoAsocios(con, formaCuenta, paciente, ConstantesIntegridadDominio.acronimoAccidenteTransito);
			
			if(exitoVal0>0)
				exitoVal0 = mundoTransito.actualizarEstadoRegistroAccidenteTransito(con,dtoTransito);
			huboActualizacion = true;
		}
		else if(formaCuenta.isTieneRegistroAccidenteTransitoAnulado()&&
				formaCuenta.getCuenta("codigoTipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoAccidenteTransito))
		{
			dtoTransito.setEstadoRegistro(ConstantesIntegridadDominio.acronimoEstadoPendiente);
			exitoVal0 = mundoTransito.actualizarEstadoRegistroAccidenteTransito(con,dtoTransito);
			huboActualizacion = true;
		}
		if(exitoVal0<=0)
		{
			return exitoVal0;
		}
		else if(huboActualizacion)
		{
			//***********SE GENERA LOG***********************************************************************************************
			 String log="\n            ====INFORMACION ORIGINAL REGISTRO ACCIDENTE TRANSITO DE LA CUENTA "+formaCuenta.getCuenta("idCuenta")+"===== " +
				"\n*  Estado [" +ValoresPorDefecto.getIntegridadDominio(dtoAntiguo.getEstadoRegistro())+"] ";
			 if(dtoAntiguo.getEstadoRegistro().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
			 {
				log += "\n*  Fecha Anulaci�n ["+dtoAntiguo.getFechaAnulacion()+"] " +
				"\n*  Hora Anulaci�n ["+dtoAntiguo.getHoraAnulacion()+"] "+
				"\n*  Usuario Anulaci�n ["+dtoAntiguo.getUsuarioAnulacion()+"] ";
			 }
			 log+="\n\n            ====INFORMACION DESPU�S DE LA MODIFICACI�N DE REGISTRO ACCIDENTE TRANSITO EN LA CUENTA "+formaCuenta.getCuenta("idCuenta")+"===== " +
			 	"\n*  Estado [" +ValoresPorDefecto.getIntegridadDominio(dtoTransito.getEstadoRegistro())+"] ";
			 if(dtoTransito.getEstadoRegistro().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
			 {
				log += "\n*  Fecha Anulaci�n ["+dtoTransito.getFechaAnulacion()+"] " +
				"\n*  Hora Anulaci�n ["+dtoTransito.getHoraAnulacion()+"] "+
				"\n*  Usuario Anulaci�n ["+dtoTransito.getUsuarioAnulacion()+"] ";
			 }
				
			 log+="\n========================================================\n\n\n " ;
			LogsAxioma.enviarLog(ConstantesBD.logModificarCuentaCodigo, log, ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
			//***************************************************************************************************************************************
			
		}
		//Se verifica si se cheque� accidente de transito y no se ha ingresado registro********
		if(formaCuenta.getCuenta("codigoTipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoAccidenteTransito)&&
			!formaCuenta.isTieneRegistroAccidenteTransito()&&
			!formaCuenta.isTieneRegistroAccidenteTransitoAnulado())
			formaCuenta.setMostrarVentanaAccidenteTransito(true);
		else
			formaCuenta.setMostrarVentanaAccidenteTransito(false);
		
		return exitoVal0;
	}
	
	/**
	 * M�todo implementado para actualizar los centros de costo de la valoraci�n
	 * @param con
	 * @param formaCuenta
	 * @param cuenta
	 * @param codigoCuenta
	 * @return
	 */
	private boolean actualizarCentrosCostoValoracion(Connection con, ModificarCuentaForm formaCuenta) 
	{
		int viaIngreso = Integer.parseInt(formaCuenta.getCuenta("codigoViaIngreso").toString());
		int codigoArea = Integer.parseInt(formaCuenta.getCuenta("codigoArea").toString());
		int idCuenta =  Integer.parseInt(formaCuenta.getCuenta("idCuenta").toString());
		
		boolean exitoVal = true;

/*		if(formaCuenta.isPuedoModificarArea()&&
			(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias||viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion))
			*/
		if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias||viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			
			int codValoracion;
			try 
			{
				codValoracion = UtilidadValidacion.getCodigoSolicitudValoracionInicial(con,idCuenta);
			} catch (Exception e) 
			{
				codValoracion = 0;
				logger.error("Error actualizando los centros de costo de la valoracion: "+e);
			} 
			Solicitud solicitud = new Solicitud();
			try {
				solicitud.cargar(con, codValoracion);
			} catch (SQLException e) {
				Log4JManager.error("Error actualizando los centros de costo de la valoracion: ", e);
			}
			if(solicitud.getEstadoHistoriaClinica().equals(ConstantesBD.codigoEstadoHCSolicitada))
			{
				solicitud.setNumeroSolicitud(codValoracion);
				if(!solicitud.cambiarCentroCostoSolicitado(con,codigoArea).isTrue())
				{
					Log4JManager.error("Error cambiando el centro de costo de la solicitud de valoraci�n");
					exitoVal = false;
				}
			}
		}
		return exitoVal;
	}

	/**
	 * 
	 * @param con
	 * @param formaCuenta
	 * @param response
	 * @param medico
	 * @param codigoTipoContrato
	 * @param codigoRegimen
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String cargarDatosContratoCapitado(Connection con, ModificarCuentaForm formaCuenta, 
			HttpServletResponse response, UsuarioBasico medico, int codigoTipoContrato, String codigoRegimen) {
		
		String aux = ""; 
		
		if(codigoTipoContrato == ConstantesBD.codigoTipoContratoCapitado) {
			//si solo tiene un contrato vigente se debe postular la informacion
			if(formaCuenta.getContratos().size()==1)
			{
				int codClasi=Utilidades.convertirAEntero(formaCuenta.getContratos().get(0).get("codigoestratosocial")+"");
				if(codClasi>0)
				{
					String tipoRegimenCSE=Utilidades.obtenerTipoRegimenClasificacionSocioEconomica(con, codClasi);
					if(codigoRegimen.equals(tipoRegimenCSE))
					{
						formaCuenta.setCuenta("codigoEstratoSocial", codClasi);
						HashMap tempo=new HashMap();
						tempo.put("numRegistros", "0");
						HashMap tempo1=new HashMap();
						tempo1.put("numRegistros", "0");
						for(int i=0;i<Utilidades.convertirAEntero(formaCuenta.getEstratosSociales("numRegistros")+"");i++)
						{
							if(Utilidades.convertirAEntero(formaCuenta.getEstratosSociales("codigo_"+i)+"")==codClasi)
							{
								tempo.put("codigo_0", formaCuenta.getEstratosSociales("codigo_"+i)+"");
								tempo.put("descripcion_0", formaCuenta.getEstratosSociales("descripcion_"+i)+"");
								tempo.put("numRegistros", "1");
								break;
							}
						}
						String tipoAfil=formaCuenta.getContratos().get(0).get("tipoafiliado")+"";
						if(!UtilidadTexto.isEmpty(tipoAfil+""))
						{
							//{numRegistros=2, acronimo_0=B, nombre_1=Cotizante, nombre_0=Beneficiario, acronimo_1=C}
							formaCuenta.setCuenta("codigoTipoAfiliado", tipoAfil);
							
							for(int i=0;i<Utilidades.convertirAEntero(formaCuenta.getTiposAfiliado("numRegistros")+"");i++)
							{
								if((formaCuenta.getTiposAfiliado("acronimo_"+i)+"").equals(tipoAfil+""))
								{
									tempo1.put("acronimo_0", formaCuenta.getTiposAfiliado("acronimo_"+i)+"");
									tempo1.put("nombre_0", formaCuenta.getTiposAfiliado("nombre_"+i)+"");
									tempo1.put("numRegistros", "1");
									break;
								}
							}
	
						}
						if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitadosModificarCuenta(medico.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
						{
							formaCuenta.setEstratosSociales(tempo);
							if(!UtilidadTexto.isEmpty(tipoAfil))
							{
								formaCuenta.setTiposAfiliado(tempo1);
							}
						}	
						if(Utilidades.convertirAEntero(formaCuenta.getEstratosSociales().get("numRegistros")+"")<=0)
						{
							aux = "<error>La Clasificaci�n socio econ�mica del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
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
							if(Utilidades.convertirAEntero(formaCuenta.getTiposAfiliado("numRegistros")+"")<=0)
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
						formaCuenta.setEstratosSociales(new HashMap());
						formaCuenta.setTiposAfiliado(new HashMap());
						
							aux = "<error>El Tipo de r�gimen de la Clasificaci�n socio  econ�mica no corresponde con el Tipo de r�gimen del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
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
				int natPaciente=Utilidades.convertirAEntero(formaCuenta.getContratos().get(0).get("naturalezapaciente")+"");
				if(natPaciente>0)
				{
					if(UtilidadValidacion.esNaturalezaValidaTipoRegimen(natPaciente,codigoRegimen+"")) 
					{

						formaCuenta.setCuenta("codigoNaturaleza", natPaciente+"");
						Vector<InfoDatosString> naturalezaVector=new Vector<InfoDatosString>();
						for(int i=0;i<formaCuenta.getNaturalezasPaciente().size();i++)
						{
							if(Utilidades.convertirAEntero(formaCuenta.getNaturalezasPaciente().get(i).getCodigo()) ==natPaciente)
							{
								naturalezaVector.add(formaCuenta.getNaturalezasPaciente().get(i));
							}
						}
						if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitadosModificarCuenta(medico.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
						{
							formaCuenta.setNaturalezasPaciente(naturalezaVector);
						}
						
						if(formaCuenta.getNaturalezasPaciente().size()<=0)
						{
							formaCuenta.setCuenta("codigoNaturaleza", natPaciente+"");
							formaCuenta.setNaturalezasPaciente(new Vector<InfoDatosString>());
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
						aux = "<error>El Tipo de r�gimen de la Naturaleza Paciente no corresponde con el Tipo de r�gimen del Convenio. No se puede asignar este Convenio. Por favor verifique.</error>";
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
