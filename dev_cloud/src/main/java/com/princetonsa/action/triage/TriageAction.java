/*
 * Created on Feb 9, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.action.triage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.ConstantesBDFacturacion;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.triage.TriageForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.CategoriasTriage;
import com.princetonsa.mundo.triage.ConsultaPacientesTriage;
import com.princetonsa.mundo.triage.Triage;
import com.princetonsa.pdf.TriagePdf;

/**
 * @author sebastián gómez rivillas
 *
 * Action que controla las operaciones involucradas con el Triage
 */
public class TriageAction extends Action {
	/**
	 * Para hacer logs de esta funcionalidad.
	 * 
	 * @uml.property name="logger"
	 * @uml.associationEnd 
	 * @uml.property name="logger" multiplicity="(1 1)"
	 */
	private Logger logger = Logger.getLogger(TriageAction.class);

	
	/**
	 * <code>triage</code> del mundo
	 */
	private Triage triage=new Triage();
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */	
	public ActionForward execute(	ActionMapping mapping, 
														ActionForm form, 
														HttpServletRequest request,
														HttpServletResponse response) throws Exception
														{	
		String estado=""; //para verificar estado del Form
		Connection con=null;
		try {
			/* Revisión de instancia de Form */
			if( form instanceof TriageForm )
			{
				TriageForm triageForm = (TriageForm)form;

				String tipoBD;
				try
				{
					/* Conexión con un DaoFactory */
					tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
				}
				catch(Exception e)
				{
					e.printStackTrace(); 

					triageForm.reset();

					logger.warn("Problemas con la base de datos "+e);
					request.setAttribute("codigoDescripcionError", "errors.problemasBD");
					//Salir en caso de error (se busca cerrar bien la conexión)
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.problemasBD", "", true);
				}
				UsuarioBasico usu=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				estado=triageForm.getEstado();

				logger.warn("[TriageEstado] estado :"+estado);
				//*********VALIDACIONES ************************************************
				//Validaciones de Profesional de la salud
				if(!UtilidadValidacion.esProfesionalSalud(usu)&&(estado.equals("empezar")||estado.equals("guardar")))
				{
					triageForm.reset();
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No es profesional de la salud Tratante", "errors.usuario.noAutorizado", true);
				}
				if(!Utilidades.isDefinidoConsecutivo(con,usu.getCodigoInstitucionInt(),ConstantesBD.nombreConsecutivoTriage)&&(estado.equals("empezar")||estado.equals("guardar")))
				{
					triageForm.reset();
					return ComunAction.accionSalirCasoError(mapping,request, con, logger, "Sin Consecutivo Triage", "error.triage.faltaConsecutivo",true);
				}
				//****************************************************************************
				//__________________________________________________
				//Según el estado el Action va por diferentes caminos
				//**********************ESTADOS PARA OPCION INGRESAR**********************************************************
				if(estado.equals("empezar"))
				{
					return accionEmpezar(con,triageForm,mapping,usu, request);

				}
				else if(estado.equals("accionVerificarIdentificacion"))
				{
					return accionVerificarIdentificacion(con,triageForm,mapping,usu,request);
				}
				else if(estado.equals("validarIngreso"))
				{
					return accionValidarIngreso(con,triageForm,mapping,usu,request);
				}
				else if(estado.equals("guardar"))
				{	
					return accionGuardar(con,triageForm,usu,mapping,request);

				}
				else if(estado.equals("imprimir"))
				{
					return accionImprimir(con,triageForm,usu,mapping,request);
				}
				//************************************************************************************************************
				//***********************ESTADOS PARA OPCION CONSULTAR/MODIFICAR**********************************************

				else if(estado.equals("consultar"))
				{
					triageForm.reset();
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("menuConsulta");
				}
				else if(estado.equals("porRangos"))
				{
					triageForm.reset();
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaBusqueda");
				}
				else if(estado.equals("porPaciente"))
				{
					PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
					if(paciente!=null&&paciente.getCodigoPersona()>0)
					{	
						triageForm.setTriage(triage.buscarTriage(con, paciente.getTipoIdentificacionPersona(false),paciente.getNumeroIdentificacionPersona()));
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaConsulta");
					}
					else
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No hay ningún paciente cargado. Para acceder a esta funcionalidad debe cargar un paciente.", "No hay ningún paciente cargado. Para acceder a esta funcionalidad debe cargar un paciente.", false);
					}
				}
				else if(estado.equals("buscar"))
				{
					triageForm.setTriage(triage.buscarTriage(con, triageForm.getFechaInicial(),triageForm.getFechaFinal(), triageForm.getCodigoClasificacion(), triageForm.getCodigoDestino(), triageForm.getAdmision(), triageForm.getCodigoSala(), usu.getCodigoCentroAtencion() ));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaConsulta");
				}
				else if(estado.equals("ordenar")){
					return this.accionOrdenar(triageForm,mapping,request,con);
				}
				//Muestra del detalle de un triage
				else if(estado.equals("detalle"))
				{
					triage=triage.cargarTriage(con,triageForm.getConsecutivo(),triageForm.getConsecutivo_fecha());

					this.cargarForm(triageForm,triage,estado);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaDetalle");
				}
				//Modifica una observación general de la funcionalidad consultar/modificar
				else if(estado.equals("modificar"))
				{
					//MT 4745
					//if(!triageForm.getObservaciones_generales().isEmpty()){
					/**
					* Tipo Modificacion: Segun incidencia 4999
					* Autor: Jesús Darío Ríos
					* usuario: jesrioro
					* Fecha: 22/02/2013
					* Descripcion: validar  que  la  cadena  getNuevas_observaciones_generales()  no esté  vacia
					**/
					if(!((triageForm.getNuevas_observaciones_generales().trim()).equals(""))){
						
						triageForm.setObservaciones_generales(UtilidadTexto.agregarTextoAObservacion(triageForm.getObservaciones_generales(),triageForm.getNuevas_observaciones_generales(),usu,true));
					
					}
					//llamado a método del mundo
					int aux=triage.actualizarTriage(con,triageForm.getConsecutivo(),triageForm.getConsecutivo_fecha(),triageForm.getObservaciones_generales(), triageForm.getNoRespondioLlamado());
					if(aux!=1){

						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.noModificado", true);

					}
					
					else
					{
						triage=triage.cargarTriage(con,triageForm.getConsecutivo(),triageForm.getConsecutivo_fecha());
						this.cargarForm(triageForm,triage,estado);
						triageForm.setNuevas_observaciones_generales("");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaDetalle");
					}
					
				}
				//*********************************************************************************************
				//****************ESTADOS PARA OPCIÓN REPORTE************************************************************
				else if(estado.equals("reporte"))
				{
					triageForm.reset();
					triageForm.setCentroAtencion(usu.getCodigoCentroAtencion());
					triageForm.setInstitucion(usu.getCodigoInstitucion());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaReporte");
				}
				else if(estado.equals("imprimirreporte"))
				{
					return accionReporte(con,triageForm,usu,mapping,request);

				}
				//*********************************************************************************************************
				else
				{
					//con el objetivo de cerrar conexiones que se puedan quedar activas
					UtilidadBD.cerrarConexion(con);
				}
			}
			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	
	
	

	/**
	 * Método implementado para realizar el reporte de Triage
	 * @param con
	 * @param triageForm
	 * @param usu
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionReporte(Connection con, TriageForm triageForm, UsuarioBasico usu, ActionMapping mapping, HttpServletRequest request) 
	{
		triage.setTriage(triage.reporteTriage(con,triageForm.getFechaInicial(),triageForm.getFechaFinal(),(triageForm.getLogin().split("-"))[0],(triageForm.getConvenio().split("-"))[1],triageForm.getCentroAtencion()));
		if(triage.getTriage().size()>0)
		{
			//*******SE CARGA EL RANGO DE CATEGORIAS PARAMETRIZADAS***********
			CategoriasTriage categorias = new CategoriasTriage();
			categorias.cargarInformacion(con,usu.getCodigoInstitucionInt());
			
			//******SE CARGA EL NOMBRE DEL CENTRO DE ATENCION***************++
			try
			{
			
				triageForm.setNombreCentroAtencion(Utilidades.obtenerNombreCentroAtencion(con,triageForm.getCentroAtencion()));
			}
			catch(Exception e)
			{
				triageForm.setNombreCentroAtencion("");
			}
			
			
			//INICIO DE LA IMPRESIÓN
			String nombreArchivo;
	    	Random r=new Random();
	    	nombreArchivo="/Triage" + r.nextInt()  +".pdf";
	    	TriagePdf.pdfTriage(ValoresPorDefecto.getFilePath() + nombreArchivo,triageForm,usu,triage.getTriage(),categorias.getCategorias(), request);
	    	request.setAttribute("nombreArchivo", nombreArchivo);
	        request.setAttribute("nombreVentana", "Reporte Triage");
	        this.cerrarConexion(con);
	        return mapping.findForward("abrirPdf");
		}
		else
		{
			
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "error.triage.reporte", true);
		}
	}

	/**
	 * Método implementado para imprimir la información del resumen Triage
	 * @param con
	 * @param triageForm
	 * @param usu
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, TriageForm triageForm, UsuarioBasico usu, ActionMapping mapping, HttpServletRequest request) 
	{
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/Triage" + r.nextInt()  +".pdf";
    	TriagePdf.pdfTriage(ValoresPorDefecto.getFilePath() + nombreArchivo,triageForm,usu, request);
    	request.setAttribute("nombreArchivo", nombreArchivo);
        request.setAttribute("nombreVentana", "Reporte Triage");
        this.cerrarConexion(con);
        return mapping.findForward("abrirPdf");
	}

	@SuppressWarnings("rawtypes")
	private ActionForward accionGuardar(Connection con, TriageForm triageForm, UsuarioBasico usu, ActionMapping mapping, HttpServletRequest request) 
	{
		triage=new Triage();
		int aux1=0,aux2= 0;
		String noRespondioLlamado = triageForm.getNoRespondioLlamado();
		
		triage.setDatosTriage(new HashMap());
		//SE CARGAN LOS DATOS DE LA FORMA AL MAPA DEL MUNDO************************
		this.cargarMundo(con,triageForm,triage,usu);
		
		UtilidadBD.iniciarTransaccion(con);
		
		//SE REALIZA LA INSERCIÓN DEL TRIAGE**********************************
		String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible("consecutivo_triage",usu.getCodigoInstitucionInt());
		
		
		// se valida que existan consecutivos disponibles para el triage para no hacerle perder tiempo al medico
		// ya que solo se valida al momento de llenar el formulario y guardar la info id tarea 39746

		logger.info("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
		logger.info("Año del Consecutivo Triage " + UtilidadBD.obtenerAnioActualTablaConsecutivos(con, "consecutivo_triage", usu.getCodigoInstitucionInt()));
		logger.info("Año del Sistema: " + UtilidadFecha.getMesAnioDiaActual("anio"));
		
		//si el año del triage es vacio o actual al año del sistema se valida el consecutivo disponible
		Log4JManager.info("obtenerAnioActualTablaConsecutivos: "+UtilidadBD.obtenerAnioActualTablaConsecutivos(con, "consecutivo_triage", usu.getCodigoInstitucionInt()));
		Log4JManager.info("Actual: "+UtilidadFecha.getMesAnioDiaActual("anio"));
		
		if ( (UtilidadBD.obtenerAnioActualTablaConsecutivos(con, "consecutivo_triage", usu.getCodigoInstitucionInt()).equals("")) 
				|| UtilidadBD.obtenerAnioActualTablaConsecutivos(con, "consecutivo_triage", usu.getCodigoInstitucionInt()).equals(UtilidadFecha.getMesAnioDiaActual("anio")+"")  ) {
			
			//validar aca el valor de la cadena consecutivo si retorna -1 o null fue porque no encontro un consecutivo valido
			//entonces que aborte la transaccion task id 39746
			if(
			(!consecutivo.equals(ConstantesBD.codigoNuncaValido+"")) &&
				(!consecutivo.equals("")) ) {
			
				triage.setDatosTriage("consecutivo",consecutivo);
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,"consecutivo_triage",usu.getCodigoInstitucionInt(),consecutivo,ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		
				aux1=triage.insertarTriage(con);
				
				
				if(aux1>0)
				{
					//se verifica si el paciente fue registrado anteriormente para Triage
					if(triageForm.isTieneTriage())
					{
						aux2 = triage.actualizarPacienteParaTriage(con,triageForm.getCodigoPacienteTriage(),triage.getDatosTriage("consecutivo").toString());
					}
					else
						aux2 = 1;
				}
				
				if(aux1>0 && aux2>0)
				{
					UtilidadBD.finalizarTransaccion(con);
					triageForm.reset();
					triageForm.setNoRespondioLlamado(noRespondioLlamado);
					triageForm.setEstado("guardar");
					triage.resumenTriage(con,triage.getDatosTriage("consecutivo").toString());
					
					this.cargarForm(triageForm,triage,triageForm.getEstado());
					this.cerrarConexion(con);
					return mapping.findForward("resumen");
				}
				else
				{
					UtilidadBD.abortarTransaccion(con);
					logger.error(" consecutivo ->"+consecutivo);
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.noIngresado", true);
				}
			}
			
			//si el triage es invalido o no se encontro abortar
			else {
				UtilidadBD.abortarTransaccion(con);
				logger.error(" consecutivo ->"+consecutivo);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "error.triage.faltaConsecutivo", true);
			}
		}
		//si el año es invalido (menor o mayor a la fecha del sistema) mostrar error
		else {
			logger.info("888888888888888888");
			logger.info("AÑO DEL CONSECUTIVO INVALIDO");
			this.cerrarConexion(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "error.triage.faltaConsecutivo", true);
		}
	}

	
	
	private ActionForward accionVerificarIdentificacion(Connection con, TriageForm forma, ActionMapping mapping, UsuarioBasico usu, HttpServletRequest request)
	{	
		//*********VALIDACION DE LOS Nï¿½MEROS DE IDENTIFICACION***********************************
		forma.setMapaNumerosId(Utilidades.personasConMismoNumeroId(
			con, 
			forma.getNumero_id(),forma.getTipo_id().split(ConstantesBD.separadorSplit)[0])
		);
		if(Integer.parseInt(forma.getMapaNumerosId("numRegistros").toString())>0)
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("avisoNumerosId");
		}
		return accionValidarIngreso(con,forma,mapping,usu,request);
	}
	
	/**
	 * Método implementado para verificar tipo y numero Id ingresado:
	 * 1. Verificar si es un paciente del sistema (postular datos paciente)
	 * 1. Verificar si tiene entrada en pacientes_triage (postular datos paciente y actualizar registro en atendido)
	 * 2. Verificar no existe en el sistema (permitir ingresar campos en datos del paciente)
	 * @param con
	 * @param triageForm
	 * @param mapping
	 * @param usu
	 * @param request 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ActionForward accionValidarIngreso(Connection con, TriageForm triageForm, ActionMapping mapping, UsuarioBasico usu, HttpServletRequest request) 
	{
		//Se toman los datos del tipoIdentificacion y numero de Identificacion
		//***********MANTENER DATOS BÁSICOS**********************************
		String tipoId = triageForm.getTipo_id();
		String[] datosId = tipoId.split(ConstantesBD.separadorSplit);
		String numeroId = triageForm.getNumero_id();
		triageForm.reset();
		triageForm.setTipo_id(tipoId);
		triageForm.setNumero_id(numeroId);
		triageForm.setInstitucion(usu.getCodigoInstitucion());
		triageForm.setCentroAtencion(usu.getCodigoCentroAtencion());
		//**********************************************************************
		
		
		//*****VALIDAR SI YA EXISTE REGISTRO TRIAGE PARA ADMISION URGENCIAS****************
		String consecutivosTriage[]={"", ""};
		consecutivosTriage=UtilidadValidacion.getConsecutivosTriage(con, datosId[0], triageForm.getNumero_id(), usu.getCodigoCentroAtencion()+"", usu.getCodigoInstitucionInt());
		if(!consecutivosTriage[0].trim().equals("")&&!consecutivosTriage[1].trim().equals(""))
		{
			ActionErrors errores = new ActionErrors();
			errores.add("pendiente de Admision",new ActionMessage("error.triage.pendienteAdmision",datosId[0]+" "+triageForm.getNumero_id()));
			saveErrors(request,errores);
			this.cerrarConexion(con);
			return mapping.findForward("inicio");
			
		}
		//**********************************************************************************
		
		
			
		
		
		//*********VERIFICAR SI EXISTE PERSONA ************************************
		int codigoPersona = Paciente.obtenerCodigoPersona(con,numeroId,datosId[0]);
		triageForm.setCodigoPersona(codigoPersona);
		
		if(codigoPersona>0)
		{
			//******VALIDAR SI EL PACIENTE TIENE INGRESO ABIERTO****************************+
			if(UtilidadValidacion.pacienteTieneIngresoAbierto(con, codigoPersona))
			{
				ActionErrors errores = new ActionErrors();
				String centroAtencion = Utilidades.getNomCentroAtencionIngresoAbierto(con,codigoPersona+"");
				if(UtilidadValidacion.tieneCuentaUrgenciasAbierta(con,codigoPersona+""))
					errores.add("Admision de Urgencias Abierta",new ActionMessage("error.triage.cuentaUrgenciasAbierta",datosId[0]+" "+triageForm.getNumero_id(),centroAtencion));
				else
					errores.add("Ingreso Abierto",new ActionMessage("error.triage.ingresoAbierto",datosId[0]+" "+triageForm.getNumero_id(),centroAtencion));
				
				saveErrors(request,errores);
				this.cerrarConexion(con);
				return mapping.findForward("inicio");
			}
			
			//******************************************************************************
			
			//**********SE VERIFICA SI LA PERSONA ES UN PACIENTE PARA TRIAGE************
			ConsultaPacientesTriage consultaPaciente = new ConsultaPacientesTriage();
			HashMap datosPaciente = consultaPaciente.consultarDatosPacienteTriage(con,codigoPersona,usu.getCodigoCentroAtencion());
			if(Integer.parseInt(datosPaciente.get("numRegistros").toString())>0)
			{
				//Se llenan los datos del paciente desde el triage
				triageForm.setTieneTriage(true);
				triageForm.setCodigoClasificacionTriage(Utilidades.convertirAEntero(datosPaciente.get("codigoclastriage_0")+""));
				triageForm.setDescripcionClasificacionTriage(datosPaciente.get("descclatriage_0")+"");
				triageForm.setCodigoPacienteTriage(datosPaciente.get("codigo_0").toString());
			}
			//****************************************************************************
			
			//***SE POSTULAN LOS DATOS DE LA PERSONA*****************************
			PersonaBasica mundoPersona = new PersonaBasica();
			try
			{   
				mundoPersona.cargar(con,codigoPersona);
				triageForm.setPrimer_apellido(mundoPersona.getPrimerApellido());
				triageForm.setSegundo_apellido(mundoPersona.getSegundoApellido());
				triageForm.setPrimer_nombre(mundoPersona.getPrimerNombre());
				triageForm.setSegundo_nombre(mundoPersona.getSegundoNombre());
				triageForm.setFecha_nacimiento(UtilidadFecha.conversionFormatoFechaAAp(mundoPersona.getFechaNacimiento()));
				
			}
			catch(SQLException e)
			{
				logger.error("Error en accionValidarIngreso de TriageAction: "+e);
			}
			//***************************************************************************************
		}
		
		//Cargar los convenios
		triageForm.setConveniosArp(UtilidadesFacturacion.cargarConveniosXClasificacion(con, ConstantesBDFacturacion.codigoClasTipoConvenioARP));
		

		
		
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Método implementado para verificar el inicio del Triage
	 * @param con
	 * @param triageForm
	 * @param mapping
	 * @param usu 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, TriageForm triageForm, ActionMapping mapping, UsuarioBasico usu, HttpServletRequest request) 
	{
		triageForm.reset();
		triageForm.setInstitucion(usu.getCodigoInstitucion());

		logger.info("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
		logger.info("Año del Consecutivo Triage " + UtilidadBD.obtenerAnioActualTablaConsecutivos(con, "consecutivo_triage", usu.getCodigoInstitucionInt()));
		logger.info("Año del Sistema: " + UtilidadFecha.getMesAnioDiaActual("anio"));
		
		// se valida que existan consecutivos disponibles para el triage para no hacerle perder tiempo al medico
		// ya que solo se valida al momento de llenar el formulario y guardar la info id tarea 39746
		
		//si el año del triage es vacio o actual al año del sistema se valida el consecutivo disponible
		if ( (UtilidadBD.obtenerAnioActualTablaConsecutivos(con, "consecutivo_triage", usu.getCodigoInstitucionInt()).equals("")) || 
				(UtilidadBD.obtenerAnioActualTablaConsecutivos(con, "consecutivo_triage", usu.getCodigoInstitucionInt()).equals(UtilidadFecha.getMesAnioDiaActual("anio")+"")) ) {
			
			String consecuDisponible = ConstantesBD.codigoNuncaValido + "";
			consecuDisponible = UtilidadBD.obtenerValorActualTablaConsecutivos(con, "consecutivo_triage", usu.getCodigoInstitucionInt()) + "";

			logger.warn(" Año Valido. Valor Consecutivo Triage Encontrado: " + consecuDisponible);
			
			if( (consecuDisponible.equals(ConstantesBD.codigoNuncaValido+"")) || (UtilidadTexto.isEmpty(consecuDisponible)) ) {
				this.cerrarConexion(con);
				logger.error(" Consecutivo Invalido. Valor :" + consecuDisponible);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "error.triage.faltaConsecutivo", true);
			}
			else {
				this.cerrarConexion(con);
				return mapping.findForward("inicio");
			}
		}
		//si el año es invalido (menor o mayor a la fecha del sistema) mostrar error
		else {
			logger.info("888888888888888888");
			logger.info("AÑO DEL CONSECUTIVO INVALIDO");
			this.cerrarConexion(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "error.triage.faltaConsecutivo", true);
		}
	}

	
	/**
	 * Método implementado para cargarForm
	 * @param triageForm
	 * @param triage
	 * @param estado
	 */
	@SuppressWarnings("deprecation")
	public void cargarForm(TriageForm triageForm,Triage triage,String estado)
	{
			triageForm.setConsecutivo(triage.getConsecutivo());
			triageForm.setConsecutivo_fecha(triage.getConsecutivo_fecha());
			triageForm.setFecha(UtilidadFecha.conversionFormatoFechaAAp(triage.getFecha()));
			triageForm.setHora(triage.getHora().substring(0,5));
			triageForm.setPrimer_nombre(triage.getPrimer_nombre());
			triageForm.setSegundo_nombre(triage.getSegundo_nombre());
			triageForm.setPrimer_apellido(triage.getPrimer_apellido());
			triageForm.setSegundo_apellido(triage.getSegundo_apellido());
			if(!triage.getFecha_nacimiento().equals(""))
				triageForm.setFecha_nacimiento(UtilidadFecha.conversionFormatoFechaAAp(triage.getFecha_nacimiento()));
			triageForm.setTipo_id(triage.getTipo_id().getAcronimo()+"-"+triage.getTipo_id().getValue());
			triageForm.setNumero_id(triage.getNumero_id());
			triageForm.setOtro_convenio(triage.getOtro_convenio());
			triageForm.setId_cotizante(triage.getId_cotizante());
			triageForm.setMotivo_consulta(triage.getMotivo_consulta());
			if(triage.getCategorias_triage().getCodigo()>0)
				triageForm.setCategorias_triage(triage.getCategorias_triage().getCodigo()+ConstantesBD.separadorSplit+triage.getCategorias_triage().getValue());
			else
				triageForm.setCategorias_triage("");
			triageForm.setAntecedentes(triage.getAntecedentes());
			triageForm.setObservaciones_generales(triage.getObservaciones_generales());
			triageForm.setLogin(triage.getLogin());
			triageForm.setUsuario(triage.getUsuario());
			triageForm.setSignoSintoma(triage.getSignosintoma());
			triageForm.setSala(triageForm.getNombresala());
			//SIGNOS VITALES
			triageForm.setSignosVitales(triage.getSignosVitales());
			triageForm.setNumSignos(triage.getNumSignos());
			
			
			triageForm.setColornombre(triage.getColornombre());
			triageForm.setSignosintoma(triage.getSignosintoma());
			triageForm.setNombresala(triage.getNombresala());
			triageForm.setExisteAdmision(triage.isExisteAdmision());
			triageForm.setNoRespondioLlamado(triage.getNoRespondeLlamado());
			
			
			triageForm.setNombreCentroAtencion(triage.getNombreCentroAtencion());

			//CALCULO DE LA EDAD
			String edad=UtilidadFecha.conversionFormatoFechaAAp(triageForm.getFecha_nacimiento());
			
			
			if(!triageForm.getFecha_nacimiento().equals(""))
				triageForm.setEdad(UtilidadFecha.calcularEdadDetallada(Integer.parseInt(edad.substring(6,10)),
												Integer.parseInt(edad.substring(3,5)),
												Integer.parseInt(edad.substring(0,2)),
												UtilidadFecha.getFechaActual().substring(0,2),
												UtilidadFecha.getFechaActual().substring(3,5),
												UtilidadFecha.getFechaActual().substring(6,10)));
			
			
			//Estos set son diferentes, se usan para el resumen de inserción triage
			if(estado.equals("guardar")||estado.equals("detalle")||estado.equals("modificar"))
			{
				triageForm.setTipo_afiliado(triage.getTipo_afiliado().getValue());
				triageForm.setConvenio(triage.getConvenio().getValue());
				triageForm.setDestino(triage.getDestino().getValue());
			}
			
			triageForm.setAccidenteTrabajo(triage.getAccidenteTrabajo());
			triageForm.setArpAfiliado(triage.getNombreArpAfiliado());
			
			triageForm.setCodigoClasificacionTriage(triage.getCodigoClasificacionTriage());
			triageForm.setDescripcionClasificacionTriage(triage.getDescripcionClasificacionTriage());
			
		}
	/**
	 * Utilidad usada para cargar el mundo
	 * @param con 
	 * @param triageForm
	 * @param triage
	 */
	public void cargarMundo(Connection con, TriageForm triageForm,Triage triage,UsuarioBasico usu)
	{
		triage.setDatosTriage("primerNombre",triageForm.getPrimer_nombre());
		triage.setDatosTriage("segundoNombre",triageForm.getSegundo_nombre());
		triage.setDatosTriage("primerApellido",triageForm.getPrimer_apellido());
		triage.setDatosTriage("segundoApellido",triageForm.getSegundo_apellido());
		triage.setDatosTriage("fechaNacimiento",UtilidadFecha.conversionFormatoFechaABD(triageForm.getFecha_nacimiento()));
		String[] datosId = triageForm.getTipo_id().split(ConstantesBD.separadorSplit);
		triage.setDatosTriage("tipoId",datosId[0]);
		if(UtilidadTexto.getBoolean(datosId[2])&&triageForm.getCodigoPersona()<=0)
		{	
			try
			{
				String consecutivo=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, "seq_personas_sin_id")+"";
				triage.setDatosTriage("numeroId",consecutivo);
			}
			catch(Exception e)
			{
				triage.setDatosTriage("numeroId","");
				logger.error("Error al obtener identificacion automatica de paciente en cargarMundo de TriageAction: "+e);
			}
		
		}
		else
			triage.setDatosTriage("numeroId",triageForm.getNumero_id());
		
		triage.setDatosTriage("otroConvenio",triageForm.getOtro_convenio());
		triage.setDatosTriage("idCotizante",triageForm.getId_cotizante());
		triage.setDatosTriage("motivoConsulta",triageForm.getMotivo_consulta().equals("")?"":triageForm.getMotivo_consulta().split(ConstantesBD.separadorSplit)[0]);
		//no se verifica signos sintomas , sino la categoria Triage, pues es ahí donde está toda la informacion del signo sintoma x sistema
		triage.setDatosTriage("signoSintoma",triageForm.getCategorias_triage().equals("")?"":triageForm.getCategorias_triage().split(ConstantesBD.separadorSplit)[3]);
		triage.setDatosTriage("categoriaTriage",triageForm.getCategorias_triage().equals("")?"":triageForm.getCategorias_triage().split(ConstantesBD.separadorSplit)[0]);
		triage.setDatosTriage("destino",triageForm.getDestino().equals("")?"":triageForm.getDestino().split(ConstantesBD.separadorSplit)[0]);
		triage.setDatosTriage("antecedentes",triageForm.getAntecedentes());
		triage.setDatosTriage("observacionesGenerales",triageForm.getObservaciones_generales());
		triage.setDatosTriage("login",usu.getLoginUsuario());
		triage.setDatosTriage("usuario",usu.getNombreUsuario()+" "+usu.getNumeroRegistroMedico()+" "+usu.getEspecialidadesMedico());
		triage.setDatosTriage("centroAtencion",usu.getCodigoCentroAtencion()+"");
		triage.setDatosTriage("sala",triageForm.getSala().equals("")?"":triageForm.getSala().split(ConstantesBD.separadorSplit)[0]);
		//Signos Vitales
		triage.setSignosVitales(triageForm.getSignosVitales());
		triage.setNumSignos(triageForm.getNumSignos());
		
		
			
		//Se verifica si el convenio llegó vacío
		if(triageForm.getConvenio().equals("0- ")||triageForm.getConvenio().equals("0-Otra"))
			triage.setDatosTriage("convenio","");
		else
			triage.setDatosTriage("convenio",triageForm.getConvenio().split("-")[0]);
		//Se verifica si el tipo de afiliado es válido
		if(triageForm.getTipo_afiliado().equals("0- "))
		{
			triage.setDatosTriage("tipoAfiliado","");
		}
		else
		{
			triage.setDatosTriage("tipoAfiliado",triageForm.getTipo_afiliado().split("-")[0]);
		}
		
		triage.setNoRespondeLlamado(triageForm.getNoRespondioLlamado());
		triage.setDatosTriage("NoRespondioLlamado",triageForm.getNoRespondioLlamado());
		triage.setDatosTriage("institucion",usu.getCodigoInstitucion());	
		
		triage.setDatosTriage("accidenteTrabajo", triageForm.getAccidenteTrabajo());
		
		if(!triageForm.getArpAfiliado().equals(""))
		{
			String[] vector = triageForm.getArpAfiliado().split(ConstantesBD.separadorSplit);
			triage.setDatosTriage("codigoArpAfiliado", vector[0]);
			triage.setDatosTriage("nombreArpAfiliado", vector[1]);
			triage.setCodigoArpAfiliado(Integer.parseInt(vector[0]));
			triage.setNombreArpAfiliado(vector[1]);
		}
		else
		{
			triage.setDatosTriage("codigoArpAfiliado", "");
			triage.setDatosTriage("nombreArpAfiliado", "");
		}
	}
	
	
	/**
	 * Función usada para ordenar por columnas
	 * en el pager del triage
	 * @param triageForm
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionOrdenar(TriageForm triageForm,
			ActionMapping mapping,
			HttpServletRequest request, 
			Connection con) throws SQLException
			{
					try
					{
					triageForm.setTriage(Listado.ordenarColumna(new ArrayList(triageForm.getTriage()),triageForm.getUltimaPropiedad(),triageForm.getColumna()));
					triageForm.setUltimaPropiedad(triageForm.getColumna());
					UtilidadBD.cerrarConexion(con);
					}
					catch(Exception e)
					{
					logger.warn("Error en el listado de triage "+e);
					UtilidadBD.cerrarConexion(con);
					triageForm.reset();
					ArrayList atributosError = new ArrayList();
					atributosError.add(" Listado Triage");
					request.setAttribute("codigoDescripcionError", "errors.invalid");				
					request.setAttribute("atributosError", atributosError);
					return mapping.findForward("paginaError");		
					}
					return mapping.findForward("paginaConsulta")	;
			}
	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	private void cerrarConexion (Connection con)
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

}
