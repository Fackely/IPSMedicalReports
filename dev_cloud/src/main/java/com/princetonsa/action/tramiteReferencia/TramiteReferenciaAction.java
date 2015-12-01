package com.princetonsa.action.tramiteReferencia;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;

import util.Listado;
import util.UtilidadBD;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import java.util.Vector;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.Medico;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.tramiteReferencia.TramiteReferencia;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.princetonsa.actionform.tramiteReferencia.TramiteReferenciaForm;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public class TramiteReferenciaAction extends Action
{
	
	//----------------Atributos
	
	Logger logger = Logger.getLogger(TramiteReferenciaAction.class);
	
	//----------------Fina Atributos
	
	//----------------Metodos
		
	/**
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest request
	 * @param HttpServletResponse resposen
	 * */
	public ActionForward execute (ActionMapping mapping, 
								  ActionForm form,
								  HttpServletRequest request,
								  HttpServletResponse response) throws Exception
								  
	{
		
		Connection con = null;
		try {
		if(response == null);
		
		if(form instanceof TramiteReferenciaForm)
		{
			con = UtilidadBD.abrirConexion();
			
			if (con == null)
			{
				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			TramiteReferenciaForm forma = (TramiteReferenciaForm)form;
			String estado = forma.getEstado();
			logger.info("Estado ==> "+estado);
			logger.info("\n\n");
			
			if(estado == null)
			{
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de Tramite Referencias (null)");
				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			//********** Pager				
			
			else if(estado.equals("ordenarTramite"))
			 {				 
				 forma.updateListarTramiteMap(this.accionOrdenarMapa(forma.getListarTramiteMap(),forma));				 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("principal");					
			}			
			else if (estado.equals("redireccion"))
			{
				UtilidadBD.closeConnection(con);
				response.sendRedirect(forma.getLinkSiguiente());
				return null;
			}			
			//********** Fin Pager
						
			
			//********** Tramite
			else if (estado.equals("volverListarReferencia"))
			{
				UtilidadBD.closeConnection(con);
				forma.iniciar();
				return UtilidadSesion.redireccionar("consultarReferencia.jsp",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getListarTramiteMap("numRegistros").toString()), response, request,"consultarReferencia.jsp",true);				
			}
			else if(estado.equals("listarReferencia"))
			{		
				forma.setIndexCentroAtencion(usuario.getCodigoCentroAtencion()+""); 				
				return this.ListarReferencia(con,forma,usuario,mapping);								
			}
			else if(estado.equals("listarReferenciaCAtencion"))
			{					 				
				return this.ListarReferencia(con,forma,usuario,mapping);								
			}
			else if(estado.equals("irTramitar"))
			{				
				//carga la informacion del paciente 
				this.cargarPaciente(con,paciente,Integer.parseInt(forma.getListarTramiteMap("codigopaciente_"+forma.getIndexTramiteMap()).toString()), usuario);
										
				//llamado de funciones principales de carga de datos en Tramite Referencia
				this.accionCargarTramiteReferencia(con,forma,usuario);			
				
				UtilidadBD.closeConnection(con);
				return UtilidadSesion.redireccionar("tramitarReferencia.jsp",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getReferenciaMap("numRegistros").toString()), response, request,"tramitarReferencia.jsp",false);				
			}						
			else if(estado.equals("guardarTramite"))
			{
				ActionForward redireccion = this.accionGuardarTramiteReferencia(con,forma,usuario,"guardado",request,mapping);
				
				//Se verifica si se debe hacer redireccion
				if(redireccion!=null)
					return redireccion;
				
				// La accion no es un Resumen de Tramite Referencia 
				forma.setIdIngresoPaciente(ConstantesBD.codigoNuncaValido);
								
				//llamado de funciones principales de carga de datos en Tramite Referencia
				this.accionCargarTramiteReferencia(con,forma,usuario);		
				
				UtilidadBD.closeConnection(con);
				
				//llamado al resumen de Tramite de Referencia
				return mapping.findForward("resumenTramite");										
			}
			//********** Fin Tramite
			
			//******************************
			else if(estado.equals("resumenTramiteReferencia"))
			{
				//llamado accion carga el resumen de la referencia
				return this.accionCargarResumenTramiteReferencia(request,mapping, con, forma,usuario,forma.getIdIngresoPaciente(), paciente);				
			}
			//******************************
			
			//***********Servicios SIRC
			else if(estado.equals("volverModificarTramitar"))
			{
				return mapping.findForward("tramite");				
			}
			else if(estado.equals("volverTramitar"))
			{
				UtilidadBD.closeConnection(con);								
				return mapping.findForward("tramite");
			}
			else if(estado.equals("irServiciosSir"))
			{	
				
				/* Indica Cual es el Index de la institucion a mostrar servicios y
				 * verifica si posee servicios institucion referencia cargados para mostrar   
				*/
				logger.info("INDEX INSTITUCIONTRAMITEMAP=> "+forma.getIndexInstitucionTramiteMap());
				logger.info("CAMPO INSTITUCON=> "+forma.getInstitucionTramiteMap("institucion_"+forma.getIndexInstitucionTramiteMap()));
				this.accionBuscarServiciosForInstitucion(forma,usuario.getCodigoInstitucionInt());
				forma.setServiciosInstitucionHistorialMap(TramiteReferencia.consultarHistorialServiciosInstitucionesReferencia(
					con, 
					forma.getInstitucionTramiteMap("institucionreferir_"+forma.getIndexInstitucionTramiteMap()).toString(), 
					Integer.parseInt(forma.getInstitucionTramiteMap("institucion_"+forma.getIndexInstitucionTramiteMap()).toString()),
					forma.getInstitucionTramiteMap("numeroreferenciatramite_"+forma.getIndexInstitucionTramiteMap()).toString())
				);
				
				UtilidadBD.closeConnection(con);
				return mapping.findForward("serviciosSirc");							
			}
			//***********Fin Servicios SIRC
			
			//***********Traslado Paciente
			else if(estado.equals("irTraslado"))
			{
				forma.setEstadoPopupTraslado("mostrarTraslado");
				this.accionBuscarTraslado(forma,usuario.getCodigoInstitucionInt());
				
				forma.setTrasladoPacienteHistorialMap(TramiteReferencia.consultarHistorialTrasladoPaciente(con,
						Integer.parseInt(forma.getTramiteMap("numeroreferenciatramite").toString()),
						forma.getTrasladoPacienteMap("institucionsirc_"+forma.getIndexInstTrasladoPacienteMap()).toString(),
						usuario.getCodigoInstitucionInt()));
				
				UtilidadBD.closeConnection(con);				
				return mapping.findForward("trasladoPaciente");
			}
			else if(estado.equals("cerrarTraslado"))
			{
				UtilidadBD.closeConnection(con);
				ActionErrors errores = new ActionErrors();
				errores = this.accionValidateTraslado(forma, errores);				
				forma.setEstadoPopupTraslado("cerrarTraslado");			
				
				
				if(!errores.isEmpty())
				{
					forma.setEstadoPopupTraslado("errorTraslado");
					saveErrors(request,errores);					
				}			
				
				return mapping.findForward("trasladoPaciente");
			}
			else if(estado.equals("verTrasladoHistorial"))
			{
				forma.setTrasladoPacienteHistorialMap(TramiteReferencia.consultarHistorialTrasladoPaciente(con,
						Integer.parseInt(forma.getTramiteMap("numeroreferenciatramite").toString()),
						forma.getCodigoInst(),
						usuario.getCodigoInstitucionInt()));		
				
				return mapping.findForward("historialTrasladoPaciente");
			}
			else if(estado.equals("consultaCiudadReferir"))
			{
				forma.setEstado("empezar");
				forma.setInstitucionTramiteMap("departamentoreferir_", " "+ConstantesBD.separadorSplit+" "+ConstantesBD.separadorSplit+" ");
				return mapping.findForward("serviciosSirc");
			}
			else if(estado.equals("imprimir"))
			{
				 this.generarReporte(con, forma, usuario, request);                
				 UtilidadBD.cerrarConexion(con);
				 return mapping.findForward("resumenTramite");
			}
			//***********Fin Traslado Paciente
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
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 */
	private void generarReporte(Connection con, TramiteReferenciaForm forma, UsuarioBasico usuario, HttpServletRequest request) {
        DesignEngineApi comp;
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

		String firmaDigital = Medico.obtenerFirmaDigitalMedico(Utilidades.convertirAEntero(forma.getReferenciaMap("codigoProfesional").toString()));
		String pathFirmaDigital = ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+firmaDigital;
		
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"historiaClinica/referenciaContrareferencia/","ReferenciaContrareferencia.rptdesign");

		//RA - Se mantiene el formato Original
		if(forma.getInstitucionTramiteMap("tipored_" + forma.getIndexTramiteMap()).toString().equals(ConstantesIntegridadDominio.acronimoRA)) {

			//imagenes
			comp.insertImageHeaderOfMasterPage1(0, 0, ValoresPorDefecto.getDirectorioImagenes()+"secretaria_salud.gif");
	        comp.insertImageHeaderOfMasterPage1(0, 2, institucion.getLogoReportes());

	        //comp.insertLabelInGridPpalOfHeader(1,0,institucion.getRazonSocial());
	        comp.insertLabelInGridPpalOfHeader(0,1, "Secretaria Distrital de Salud" + "\n" + "SOLICITUD DE SERVICIOS" + "\n" + "SISTEMA INTEGRAL DE REFERENCIA Y CONTRAREFERENCIA");
		}
		
		else { //RN - 68183
	        comp.insertImageHeaderOfMasterPage1(0, 2, institucion.getLogoReportes());
	        comp.insertGridHeaderOfMasterPage(0,1,1,4);
	
	        //institucion.getLogoReportes()
		        
	        Vector v = new Vector();
	        v.add(institucion.getRazonSocial());
	        v.add(Utilidades.getDescripcionTipoIdentificacion(con, institucion.getTipoIdentificacion()) + "  -  " + institucion.getNit());
	        v.add(institucion.getDireccion());
	        v.add("Tels. " + institucion.getTelefono());

	        comp.insertLabelInGridOfMasterPage(0, 1, v);
	        comp.insertLabelInGridPpalOfHeader(1, 1, "SISTEMA INTEGRAL DE REFERENCIA Y CONTRAREFERENCIA ");
		}
		
		if (!UtilidadTexto.isEmpty(firmaDigital))
        	comp.insertImageBodyPage(0, 0, pathFirmaDigital, "firma");
		
		comp.insertLabelBodyPage(0, 0, institucion.getPieHistoriaClinica(), "piehiscli");

        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
    
        if(!newPathReport.equals("")) {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
       	}
        comp.updateJDBCParameters(newPathReport);
	}



	/************************************************************
	/*********************************Grupo de Metodos "Administracion" Tramite Referencia
	
	/**
	 * Carga el tramite para el resumen del Tramite de Referencia 
	 * @param Connection con
	 * @param TramiteReferenciaForm forma
	 * @param UsuarioBasico usuario
	 * @param int idIngresoPaciente 
	 */
	public ActionForward accionCargarResumenTramiteReferencia(HttpServletRequest request, ActionMapping mapping, Connection con, TramiteReferenciaForm forma, UsuarioBasico usuario, int idIngresoPaciente, PersonaBasica paciente )
	{
		int idIngreso = forma.getIdIngresoPaciente();
		
		/*cargo el paciente en sesion*/
		logger.info("cidugipersona->"+paciente.getCodigoPersona());
		IngresoGeneral i=new IngresoGeneral();
		try 
	    {
			i.cargarIngreso(con, idIngreso+"");
			i.cargarPacienteDadoIngreso(con, idIngreso+"");
			logger.info("cod persona del ingreso->"+i.getPaciente().getCodigoPersona());
			paciente.setCodigoPersona(i.getPaciente().getCodigoPersona());
		    paciente.cargar(con, i.getPaciente().getCodigoPersona());
			paciente.cargarPaciente(con, i.getPaciente().getCodigoPersona(), usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
		} 
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
		/*fin cargar pac en sesion*/
		
		String botonVolver="";
		if(!UtilidadTexto.isEmpty(forma.getBotonVolver()))
			botonVolver=forma.getBotonVolver();
		forma.reset();
		forma.setBotonVolver(botonVolver);
		ActionErrors errores = new ActionErrors();
		HashMap parametrosBusqueda = new HashMap();
		
		//parametros de la busqueda consultarTramite		
		forma.setIndexTramiteMap("0");
		forma.setListarTramiteMap("codigopaciente_0","0");
		forma.setListarTramiteMap("ingreso_0",idIngreso); //cargo el Id del Ingreso del paciente dado por parametro a la forma
		forma.setListarTramiteMap("referencia_0",ConstantesIntegridadDominio.acronimoInterna);		
		
		//llamado de funciones principales de carga de datos en Tramite Referencia
		if(!this.accionCargarTramiteReferencia(con,forma,usuario))
			errores.add("descripcion",new ActionMessage("errors.invalid","No Se Encontro Tramites de Referencia a Partir de la Informacion Ingresada"));
		
		UtilidadBD.closeConnection(con);		

		if(!errores.isEmpty())
		{
			saveErrors(request,errores);
			return mapping.findForward("paginaErroresActionErrors");
		}
		else
			return mapping.findForward("resumenTramite");		
	}
	
	/**
	 * Guarda la informacion del tramite de Referencia
	 * @param request 
	 * @param mapping 
	 * @param Connection con
	 * @param TramiteReferenciaForm forma
	 * @param UsuarioBasico usuario
	 * */
	public ActionForward accionGuardarTramiteReferencia(Connection con, TramiteReferenciaForm forma, UsuarioBasico usuario,String estado, HttpServletRequest request, ActionMapping mapping)
	{
		ActionErrors errores = new ActionErrors();
		
		//***************VALIDACIONES QUE APLICAN AL FINALIZAR EL TRÁMITE*************************************************
		errores = this.validacionFinalizar(forma,errores);
		//******************************************************************************************************************
		
		if(!errores.isEmpty())
		{
			forma.setEstado("irTramitar");
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("tramite");
		}
		
		logger.info("**********************************");		
		logger.info("INICIO GUARDAR TRAMITE REFERENCIA");
		logger.info("**********************************");
		
		HashMap parametros = new HashMap();
		String vestado  = "ninguno";
		String transacction_str="";
		boolean transacction = UtilidadBD.iniciarTransaccion(con);
				
		//Guarda o Actualiza Registros de Tramite Referencia 
		parametros.put("numeroreferenciatramite",forma.getTramiteMap("numeroreferenciatramite"));		
		transacction_str = accionGuardarTramiteReferencia(con,forma.getTramiteMap(),parametros,usuario); 
		if(!transacction_str.equals(ConstantesBD.acronimoNo))
		{
			transacction = UtilidadTexto.getBoolean(transacction_str);
			vestado = estado;
		}		
		
		logger.info("valor transacction guardar Tramite Referencia "+transacction+"************************************");
		
		//Guarda o Actualiza Registros de Instituciones Tramite Referencia
		parametros = new HashMap();	
		transacction_str = accionGuardarInstitucionesTramiteReferencia(con,forma,parametros,usuario);
		if(!transacction_str.equals(ConstantesBD.acronimoNo))
		{
			transacction = UtilidadTexto.getBoolean(transacction_str);
			vestado = estado;
		}	
		
		logger.info("valor transacction guardar Instituciones "+transacction+"*********************************");	
		
					
		//Guarda o Actualiza Servicios Institucion Referencia		
		parametros = new HashMap();
		transacction_str = accionGuardarServiciosInstitucionReferencia(con,forma,parametros,usuario);
		if(!transacction_str.equals(ConstantesBD.acronimoNo))
		{
			transacction = UtilidadTexto.getBoolean(transacction_str);
			vestado = estado;
		}	
		
		logger.info("valor transacction guardar Servicios Instituciones Referencencia "+transacction+"*********************************");
		
		
		//Guarda o Actualiza Registros de Traslado Paciente		
		parametros = new HashMap();
		if(forma.getTramiteMap("requiereAmbulancia").toString().equals(ConstantesBD.acronimoSi))
		{	
			transacction_str = accionGuardarTrasladoPaciente(con,forma,parametros,usuario);
			if(!transacction_str.equals(ConstantesBD.acronimoNo))
			{
				transacction = UtilidadTexto.getBoolean(transacction_str);
				vestado = estado;	
			}
			
			logger.info("valor transacction guardar Traslado Paciente "+transacction+"*********************************");
		}		
		
		
		//Actualizar estado de la referencia
		if(transacction)
		{
			transacction = UtilidadesHistoriaClinica.actualizarEstadoReferencia(
				con, 
				forma.getReferenciaMap("numeroReferencia").toString(), 
				forma.getReferenciaMap("idIngreso").toString(), 
				UtilidadTexto.getBoolean(forma.getTramiteMap("finalizar").toString())?
					ConstantesIntegridadDominio.acronimoEstadoFinalizado:ConstantesIntegridadDominio.acronimoEstadoEnTramite, 
				usuario.getLoginUsuario());
			
			vestado = estado;
		}
		
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);			
			logger.info("----->INSERTO 100% TRAMITE REFERENCIA");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		
		forma.setEstado(vestado);
		
		logger.info("**********************************");		
		logger.info("FIN GUARDAR TRAMITE REFERENCIA");
		logger.info("**********************************\n\n");
		
		return null;
	}	
	
	/***
	 * Método que realiza las validaciones para saber si se puede finalizar el tramite
	 * @param forma
	 * @param errores
	 * @return
	 */
	private ActionErrors validacionFinalizar(TramiteReferenciaForm forma, ActionErrors errores) 
	{
		//**************************************************************************************************************
		//*************************VALIDACION DE LOS SERVICIOS***********************************************************
		//**************************************************************************************************************
		//registro de los servicios
		HashMap servicios = new HashMap();
		int cont = 0, cont0 = 0;
		String aux = "";
		
		//Se verifica si se desea finalizar el trámite
		if(UtilidadTexto.getBoolean(forma.getTramiteMap("finalizar").toString()))
		{
			//SE REALIZA LA CUENTA DE INSTANCIAS DE CADA SERVICIO EN CADA INSTITUCION****************************************
			for(int i=0;i<Integer.parseInt(forma.getServiciosReferencia("numRegistros").toString());i++)
			{
				//Se toman los datos del servicio SIRC
				servicios.put("codigoServicioSirc_"+cont,forma.getServiciosReferencia("codigoServicioSirc_"+i));
				servicios.put("codigoServicio_"+cont,forma.getServiciosReferencia("codigoServicio_"+i));
				servicios.put("nombreServicioSirc_"+cont,forma.getServiciosReferencia("nombreServicioSirc_"+i));
				servicios.put("nombreCups_"+cont,forma.getServiciosReferencia("nombreCups_"+i));
				
				cont0 = 0; //variable para contar las ocurrencias de un servicio en la estructura
				aux = ""; //variable que almacena las instituciones donde se encuentra el servicio
				//Se iteran las instituciones
				for(int j=0;j<Integer.parseInt(forma.getInstitucionTramiteMap("numRegistros").toString());j++)
				{
					//se iteran los servicios de cada institucion
					for(int k=0;k<Integer.parseInt(forma.getInstitucionTramiteMap("numRegistrosServicios_"+j).toString());k++)
					{
						if(servicios.get("codigoServicioSirc_"+cont).toString().equals(forma.getInstitucionTramiteMap("codigoserviciosirc_"+j+"_"+k).toString())&&
							servicios.get("codigoServicio_"+cont).toString().equals(forma.getInstitucionTramiteMap("codigoservicio_"+j+"_"+k).toString())&&
							UtilidadTexto.getBoolean(forma.getInstitucionTramiteMap("activo_"+j+"_"+k).toString())&&
							(UtilidadTexto.getBoolean(forma.getInstitucionTramiteMap("estabd_"+j+"_"+k).toString())||UtilidadTexto.getBoolean(forma.getInstitucionTramiteMap("editar_"+j+"_"+k).toString())))
						{
							cont0 ++;
							if(!aux.equals(""))
								aux += ", ";
							aux += forma.getInstitucionTramiteMap("descripcioninstitucionreferir_"+j);
						}
					}
				}
				
				//Se asignan las cuentas 
				servicios.put("ocurrencias_"+cont, cont0+"");
				servicios.put("instituciones_"+cont, aux);
				cont++;
			}
			servicios.put("numRegistros",cont+"");
			
			
			//SE VERIFICA SI EL SERVICIO ESTÁ EN ALGUNA INSTITUCION Ó ESTÁ EN MAS DE UNA PARA EDITAR EL ERROR*********************
			for(int i=0;i<Integer.parseInt(servicios.get("numRegistros").toString());i++)
			{
				//1) Si el servicio no está en ninguna institucion es error
				if(Integer.parseInt(servicios.get("ocurrencias_"+i).toString())<=0)
					errores.add("Servicio no asignado", new ActionMessage(
						"error.historiaClinica.tramitarReferencia.servicioSinInstitucion",
						servicios.get("nombreServicioSirc_"+i)+" ("+servicios.get("nombreCups_"+i)+")"));
				
				//2) Si el servicio está en 2 o más instituciones es error
				if(Integer.parseInt(servicios.get("ocurrencias_"+i).toString())>1)
					errores.add("Servicio no asignado", new ActionMessage(
						"error.historiaClinica.tramitarReferencia.servicioMasInstituciones",
						servicios.get("nombreServicioSirc_"+i)+" ("+servicios.get("nombreCups_"+i)+")",
						servicios.get("instituciones_"+i)));
			}
			//***********************************************************************************************************************
			//**************************************************************************************************************
			//*****************************VALIDACION DEL TRASLADO**********************************************************
			//Solo aplica si es requerido ambulancia
			if(forma.getTramiteMap("requiereAmbulancia").toString().equals(ConstantesBD.acronimoSi))
			{
				//Sólo puede haber una institucion para esta seccion
				cont = 0;
				for(int i=0;i<Integer.parseInt(forma.getTrasladoPacienteMap("numRegistros").toString());i++)
				{
					if(UtilidadTexto.getBoolean(forma.getTrasladoPacienteMap("activo_"+i).toString()))
						cont ++;
				}
				//1) No existe institucion para el traslado del paciente
				if(cont<=0)
					errores.add("Traslado sin instituciones",new ActionMessage("error.historiaClinica.tramitarReferencia.trasladoSinInstitucion"));
				//2) Hay 2 o mas instituciones activas para el traslado
				if(cont>1)
					errores.add("Traslado con mas instituciones", new ActionMessage("error.historiaClinica.tramitarReferencia.trasladoMasInstituciones"));
			}
			//**************************************************************************************************************
			//**************************************************************************************************************
		}
		
		
		return errores;
	}



	/**
	 * Guarda datos en la tabla Servicios Institucion Referencia 
	 * @param Connection con
	 * @param HashMap tramiteMap 
	 * @param UsuarioBasico usuario 
	 * */
	public String accionGuardarServiciosInstitucionReferencia(Connection con, TramiteReferenciaForm forma, HashMap parametros,UsuarioBasico usuario)
	{
		String transacction = ConstantesBD.acronimoNo;
		
		
		//numero de Instituciones Tramite Referencia
		int numRegistrosInstituciones = Integer.parseInt(forma.getInstitucionTramiteMap("numRegistros").toString());
		int numServiciosInstituciones = 0; 
		
		//recorre el HashMap de Instituciones Tramite Referencia
		for(int inst = 0; inst<numRegistrosInstituciones;inst++)
		{
			//si la Institucion posee Servicios cargados 
			if(forma.getInstitucionTramiteMap("cargado_"+inst).toString().equals(ConstantesBD.acronimoSi))
			{
				//numero de servicios cargados a la institucion 
				numServiciosInstituciones = Integer.parseInt(forma.getInstitucionTramiteMap("numRegistrosServicios_"+inst).toString());
				
				//recorre el HashMap de Servicios Institucion Referencia 
				for(int serv = 0; serv < numServiciosInstituciones; serv++)
				{
					parametros = new HashMap();
					parametros.put("codigoserviciosirc",forma.getInstitucionTramiteMap("codigoserviciosirc_"+inst+"_"+serv).toString());
					parametros.put("codigoservicio",forma.getInstitucionTramiteMap("codigoservicio_"+inst+"_"+serv).toString());				 
					parametros.put("institucionreferir",forma.getInstitucionTramiteMap("institucionreferir_"+inst+"_"+serv).toString());
					parametros.put("institucion",forma.getInstitucionTramiteMap("institucion_"+inst+"_"+serv).toString());
					parametros.put("numeroreferenciatramite",forma.getInstitucionTramiteMap("numeroreferenciatramite_"+inst+"_"+serv).toString());					
					parametros.put("activo",forma.getInstitucionTramiteMap("activo_"+inst+"_"+serv).toString());
					parametros.put("estado",forma.getInstitucionTramiteMap("estado_"+inst+"_"+serv).toString());
					
					if(!forma.getInstitucionTramiteMap("motivo_"+inst+"_"+serv).toString().equals(ConstantesBD.codigoNuncaValido+""))
						parametros.put("motivo",forma.getInstitucionTramiteMap("motivo_"+inst+"_"+serv).toString());
					else
						parametros.put("motivo",ConstantesBD.codigoNuncaValido);
					
					parametros.put("motivo",forma.getInstitucionTramiteMap("motivo_"+inst+"_"+serv).toString());
					parametros.put("funcionariocontactado",forma.getInstitucionTramiteMap("funcionariocontactado_"+inst+"_"+serv).toString());					 
					parametros.put("cargo",forma.getInstitucionTramiteMap("cargo_"+inst+"_"+serv).toString());					 
					parametros.put("fechatramite",UtilidadFecha.conversionFormatoFechaABD(forma.getInstitucionTramiteMap("fechatramite_"+inst+"_"+serv).toString().toString()));
					parametros.put("horatramite",forma.getInstitucionTramiteMap("horatramite_"+inst+"_"+serv).toString());
					parametros.put("numeroverificacion",forma.getInstitucionTramiteMap("numeroverificacion_"+inst+"_"+serv).toString());
					parametros.put("observaciones",forma.getInstitucionTramiteMap("observaciones_"+inst+"_"+serv).toString());
					parametros.put("estabd",forma.getInstitucionTramiteMap("estabd_"+inst+"_"+serv).toString());
					parametros.put("INDICES_MAPA",forma.getInstitucionTramiteMap("INDICES_MAPA_SERV"));
										
					if(parametros.get("estabd").toString().equals(ConstantesBD.acronimoSi))
					{
						if(TramiteReferencia.existeModificacion(con, parametros, parametros,"",3))
						{
							parametros.put("usuariomodifica",usuario.getLoginUsuario());
							parametros.put("fechamodifica",Utilidades.capturarFechaBD());
							parametros.put("horamodifica",UtilidadFecha.getHoraActual());
							
							transacction = TramiteReferencia.actualizarServiciosInstitucionesReferencia(con, parametros)+"";							
						}
					}					
					else if (forma.getInstitucionTramiteMap("estabd_"+inst+"_"+serv).toString().equals(ConstantesBD.acronimoNo))
					{
						if (forma.getInstitucionTramiteMap("activo_"+inst+"_"+serv).toString().equals(ConstantesBD.acronimoSi))
						{
							parametros.put("usuariomodifica",usuario.getLoginUsuario());
							parametros.put("fechamodifica",Utilidades.capturarFechaBD());
							parametros.put("horamodifica",UtilidadFecha.getHoraActual());
							
							transacction = TramiteReferencia.insertarServiciosInstitucionesReferencia(con, parametros)+"";
						}												
					}						
				}				
			}	
		}	
		
		return transacction;
	}
	
	/**
	 * Guarda datos en la tabla Tramite Referencia 
	 * @param Connection con
	 * @param HashMap tramiteMap
	 * @param HashMap parametros
	 * @param UsuarioBasico usuario 
	 * */
	public String accionGuardarInstitucionesTramiteReferencia(Connection con, TramiteReferenciaForm forma, HashMap parametros, UsuarioBasico usuario)
	{
		int numRegistros = Integer.parseInt(forma.getInstitucionTramiteMap("numRegistros").toString());
		String transacction = ConstantesBD.acronimoNo;					
		
		for(int i = 0; i < numRegistros; i++)
		{	
			if(buscarServicosQGuardar(forma,i))
			{
				parametros.put("numeroreferenciatramite",forma.getInstitucionTramiteMap("numeroreferenciatramite_"+i));
				parametros.put("institucionreferir",forma.getInstitucionTramiteMap("institucionreferir_"+i));
				parametros.put("institucion",forma.getInstitucionTramiteMap("institucion_"+i));
				parametros.put("INDICES_MAPA",forma.getInstitucionTramiteMap("INDICES_MAPA"));
				
				parametros.put("departamentoreferir",forma.getInstitucionTramiteMap("departamentoreferir_"+i).toString().split(ConstantesBD.separadorSplit)[0]);
				parametros.put("ciudadreferir",forma.getInstitucionTramiteMap("departamentoreferir_"+i).toString().split(ConstantesBD.separadorSplit)[1]);
				parametros.put("paisreferir",forma.getInstitucionTramiteMap("paisreferir_"+i));
						
				if(forma.getInstitucionTramiteMap("estabd_"+i).equals(ConstantesBD.acronimoSi))
				{
					if(TramiteReferencia.existeModificacion(con,parametros,parametros,"0",2))
					{
						transacction = TramiteReferencia.actualizarInstitucionesTramiteReferencia(con, parametros)+"";						
					}	
				}	
				else if(forma.getInstitucionTramiteMap("estabd_"+i).equals(ConstantesBD.acronimoNo))			
					transacction = TramiteReferencia.insertarInstitucionesTramiteReferencia(con,parametros)+"";					
			}	
		}			
		return transacction;		
	}
	
	/**
	 * Guarda datos en la tabla Tramite Referencia 
	 * @param Connection con
	 * @param HashMap tramiteMap
	 * @param HashMap parametros
	 * @param UsuarioBasico usuario 
	 * */
	public String accionGuardarTramiteReferencia(Connection con, HashMap tramiteMap, HashMap parametros, UsuarioBasico usuario)
	{
		String estado = ConstantesBD.acronimoNo;
						
		//Guarda Tabla Tramite de Referencia	
		if(TramiteReferencia.existeModificacion(con,tramiteMap,parametros,"",1))
		{				
			tramiteMap.put("usuariomodifica",usuario.getLoginUsuario());
			tramiteMap.put("fechamodifica",Utilidades.capturarFechaBD());
			tramiteMap.put("horamodifica",UtilidadFecha.getHoraActual());
			estado =   TramiteReferencia.actualizarTramiteReferencia(con,tramiteMap)+"";
		}	
		else if(tramiteMap.get("estabd").equals(ConstantesBD.acronimoNo))
		{
			tramiteMap.put("usuariomodifica",usuario.getLoginUsuario());
			tramiteMap.put("fechamodifica",Utilidades.capturarFechaBD());
			tramiteMap.put("horamodifica",UtilidadFecha.getHoraActual());			
			estado  =  TramiteReferencia.insertarTramiteReferencia(con,tramiteMap)+"";		
		}			
		return estado;
	}
	
	
	/**
	 * Guarda Traslado Paciente 
	 * @param Connection con
	 * @param TramiteReferenciaForm forma
	 * @param HashMap parametros
	 * @param UsuarioBasico 
	 * */
	public String accionGuardarTrasladoPaciente(Connection con, TramiteReferenciaForm forma, HashMap parametros, UsuarioBasico usuario)
	{
		String estado = ConstantesBD.acronimoNo;
		int numRegistrosTrasladoPaciente = Integer.parseInt(forma.getTrasladoPacienteMap("numRegistros").toString());
					
		for(int i=0; i<numRegistrosTrasladoPaciente; i++ )			
		{			
			//limpia parametros
			parametros = new HashMap();
			
			
			if(forma.getTrasladoPacienteMap().containsKey("estado_"+i))
			{				
				if(!forma.getTrasladoPacienteMap("estado_"+i).equals(ConstantesBD.codigoNuncaValido+"") && forma.getTrasladoPacienteMap("hayerrores_"+i).equals(ConstantesBD.acronimoNo))
				{
					//carga los parametros de busqueda			
					parametros.put("numeroreferenciatramite",forma.getTramiteMap("numeroreferenciatramite"));
					parametros.put("institucionsirc",forma.getTrasladoPacienteMap("institucionsirc_"+i));
					parametros.put("institucion",usuario.getCodigoInstitucionInt());
					parametros.put("INDICES_MAPA",forma.getTrasladoPacienteMap("INDICES_MAPA"));
					
					if(forma.getTrasladoPacienteMap("activo_"+i).toString().equals(ConstantesBD.acronimoNo))
					{
						parametros.put("activo",forma.getTrasladoPacienteMap("activo_"+i));
					}
					else
					{
						parametros.put("activo",forma.getTrasladoPacienteMap("activo_"+i));
						parametros.put("estado",forma.getTrasladoPacienteMap("estado_"+i));
						parametros.put("motivo",forma.getTrasladoPacienteMap("motivo_"+i));			
						parametros.put("funcionariocontactado",forma.getTrasladoPacienteMap("funcionariocontactado_"+i));
						parametros.put("cargo",forma.getTrasladoPacienteMap("cargo_"+i));
						parametros.put("fechatramite",forma.getTrasladoPacienteMap("fechatramite_"+i));
						parametros.put("horatramite",forma.getTrasladoPacienteMap("horatramite_"+i));
						parametros.put("responsabletrasambulancia",forma.getTrasladoPacienteMap("responsabletrasambulancia_"+i));
						parametros.put("numeromovil",forma.getTrasladoPacienteMap("numeromovil_"+i));
						parametros.put("placa",forma.getTrasladoPacienteMap("placa_"+i));
						parametros.put("observaciones",forma.getTrasladoPacienteMap("observaciones_"+i));				
					}
					
					if(forma.getTrasladoPacienteMap("hayerrores_"+i).equals(ConstantesBD.acronimoNo))
					{
						//Guarda Traslado Paciente
						if(forma.getTrasladoPacienteMap("estabd_"+i).toString().equals(ConstantesBD.acronimoSi))
						{				
							if(TramiteReferencia.existeModificacion(con,parametros, parametros,"",4))
							{	
								parametros.put("usuariomodifica",usuario.getLoginUsuario());
								parametros.put("fechamodifica",Utilidades.capturarFechaBD());
								parametros.put("horamodifica",UtilidadFecha.getHoraActual());
								estado= TramiteReferencia.actualizarTrasladoPaciente(con, parametros)+"";				
							}
						}		
						else if (forma.getTrasladoPacienteMap("estabd_"+i).toString().equals(ConstantesBD.acronimoNo) && (forma.getTrasladoPacienteMap("activo_"+i).toString().equals(ConstantesBD.acronimoSi)))
						{
							parametros.put("usuariomodifica",usuario.getLoginUsuario());
							parametros.put("fechamodifica",Utilidades.capturarFechaBD());
							parametros.put("horamodifica",UtilidadFecha.getHoraActual());
							estado = TramiteReferencia.insertarTrasladoPaciente(con, parametros)+"";				
						}
					}	
				}	
			}				
		}		
		
		return estado;	
	}
	/************************************************************
	/*********************************Grupo de Metodos Referencia
	 
	 /**
	 * Consulta la informacion de la Referencia
	 * @param Connection con
	 * @param TramiteReferenciaForm forma
	 * @param ActionMapping mapping 
	 * @param UsuarioBasico usuario
	 * @param String estado
	 * */
	public void accionConsultarReferencia(Connection con,TramiteReferenciaForm forma,UsuarioBasico usuario)
	{
		final int MaxLength = 3998; //tamaño del campo en tabla original
		final int AllowLength = 1000; //maximo tamaño para modificar
		
		
		//carga la informacion en el hashmap Master - referencia		
		forma.setReferenciaMap(TramiteReferencia.cargarReferencia(con,Integer.parseInt(forma.getListarTramiteMap("codigopaciente_"+forma.getIndexTramiteMap()).toString()),Integer.parseInt(forma.getListarTramiteMap("ingreso_"+forma.getIndexTramiteMap()).toString()),forma.getListarTramiteMap("referencia_"+forma.getIndexTramiteMap()).toString()));			
		
		if(Integer.parseInt(forma.getReferenciaMap("numRegistros").toString())>0)
		{
			//carga la informacion en los hasmap de historia clinica		
			forma.setSignosVitalesMap((HashMap)forma.getReferenciaMap("mapaSignosVitales"));
			forma.setResultadosMap((HashMap)forma.getReferenciaMap("mapaResultados"));
			forma.setDiagnosticoMap((HashMap)forma.getReferenciaMap("mapaDiagnosticos"));		
			forma.setOtrosHistoriaClinicaMap("anamnesis",forma.getReferenciaMap("anamnesis").toString());
			forma.setOtrosHistoriaClinicaMap("antecedentes",forma.getReferenciaMap("antecedentes").toString());
			forma.setOtrosHistoriaClinicaMap("codigoEstadoConciencia",forma.getReferenciaMap("codigoEstadoConciencia").toString());
			forma.setOtrosHistoriaClinicaMap("nombreEstadoConciencia",forma.getReferenciaMap("nombreEstadoConciencia").toString());
			forma.setOtrosHistoriaClinicaMap("examenFisico",forma.getReferenciaMap("examenFisico").toString());
			forma.setOtrosHistoriaClinicaMap("pacienteConOxigeno",forma.getReferenciaMap("pacienteConOxigeno").toString());
			forma.setOtrosHistoriaClinicaMap("saturacionOxigeno",forma.getReferenciaMap("saturacionOxigeno").toString());
			forma.setOtrosHistoriaClinicaMap("tratamientoComplicaciones",forma.getReferenciaMap("tratamientoComplicaciones").toString());
			forma.setOtrosHistoriaClinicaMap("resumenHistoriaClinica",forma.getReferenciaMap("resumenHistoriaClinica").toString());
			forma.setOtrosHistoriaClinicaMap("observaciones",forma.getReferenciaMap("observaciones").toString());
			forma.setOtrosHistoriaClinicaMap("documentosAnexos",forma.getReferenciaMap("documentosAnexos").toString());
			
			if((MaxLength - forma.getOtrosHistoriaClinicaMap("documentosAnexos").toString().length())>AllowLength)
				forma.setOtrosHistoriaClinicaMap("tamanoDocumentosAnexosPlus","998");			
			else if((MaxLength - forma.getOtrosHistoriaClinicaMap("documentosAnexos").toString().length())<=AllowLength)
				forma.setOtrosHistoriaClinicaMap("tamanoDocumentosAnexosPlus",MaxLength - forma.getOtrosHistoriaClinicaMap("documentosAnexos").toString().length());
	
			
			forma.setOtrosHistoriaClinicaMap("documentosAnexosPlus","");
			
			//cargo el mapa de servicios de la referencia 
			forma.setServiciosReferencia((HashMap)forma.getReferenciaMap("mapaServicios"));			
		}	
	}

	/**
	 * Carga la consulta de tramites
	 * @param TramiteReferenciaForm forma
	 * @param Connection con
	 * @param ActionMapping mapping
	 * @return ActionForward
	 * */
	public ActionForward ListarReferencia(Connection con, TramiteReferenciaForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.reset();
		HashMap parametrosBusqueda = new HashMap();		
		
		//parametros de la busqueda consultarTramite		
		parametrosBusqueda.put("institucion",usuario.getCodigoInstitucion());
		parametrosBusqueda.put("referencia",ConstantesIntegridadDominio.acronimoInterna);
		parametrosBusqueda.put("estado1",ConstantesIntegridadDominio.acronimoEstadoAnulado);		
		parametrosBusqueda.put("estado2",ConstantesIntegridadDominio.acronimoEstadoFinalizado);					
		parametrosBusqueda.put("centroatencion",forma.getIndexCentroAtencion());				
	
		forma.setListarTramiteMap(TramiteReferencia.consultarListadoReferencia(con, parametrosBusqueda));
		
		//inicia los centros de Atencion 	
		forma.iniciarCentroAtencion(usuario.getCodigoInstitucionInt());
		
		return mapping.findForward("principal");				
	}
	
	/**
	 * Carga el paciente en session
	 * Connection con
	 * int codigoPersona
	 * PersonaBasica paciente
	 * UsuarioBasico usuario
	 * HttpServletRequest request
	 * @param paciente 
	 * */
	public void cargarPaciente(Connection con,PersonaBasica paciente, int codigoPersona,UsuarioBasico usuario)
	{
		ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
		
		try 
		{			
			paciente.cargar(con,codigoPersona);
			paciente.cargarPaciente2(con, paciente.getCodigoPersona(), usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
		} 
		catch (Exception e) 
		{
			logger.info("Error en accionDetalle: "+e);
		}
		
		observable.addObserver(paciente);
		UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),servlet.getServletContext());
	}
	
	/**
	 *  Ordena Un Mapa HashMap a partir del patron de ordenamiento
	 *  @param HashMap mapaOrdenar
	 *  @param String patronOrdenar
	 *  @param String ultimoPatron
	 *  @return Mapa Ordenado
	 **/
	public HashMap accionOrdenarMapa(HashMap mapaOrdenar,TramiteReferenciaForm forma)
	{			
		String[] indices = (String[])mapaOrdenar.get("INDICES_MAPA");
		int numReg = Integer.parseInt(mapaOrdenar.get("numRegistros")+"");		
		mapaOrdenar = (Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),mapaOrdenar,numReg));		
		forma.setUltimoPatron(forma.getPatronOrdenar());
		mapaOrdenar.put("numRegistros",numReg+"");
		mapaOrdenar.put("INDICES_MAPA",indices);		
		return mapaOrdenar;
	}	
	
	/*********************************************************
	/*********************************Grupo de Metodos Tramite	
	
	/**
	 * Llamado a todas las funciones que encargadas de consultar e iniciar el tramite
	 * @param Connection con
	 * @param TramiteReferencia forma
	 * @param UsuarioBasico usuario 
	 * */
	public boolean accionCargarTramiteReferencia(Connection con, TramiteReferenciaForm forma, UsuarioBasico usuario)
	{
		//carga la informacion de la referencia de la cual se desprende el Tramite
		this.accionConsultarReferencia(con,forma,usuario);
		
		if(Integer.parseInt(forma.getReferenciaMap("numRegistros").toString())>0)
		{
			//carga la inforacion del Tramite referencia e Instituciones Tramite Referencia
			this.accionConsultarTramiteReferencia(con,forma,usuario);
			//carga la informacion de Servicios Institucion Referencia 
			this.accionConsultarServiciosSIRC(con,forma,usuario);
			
			if(forma.getReferenciaMap("requiereAmbulancia").toString().equals(ConstantesBD.acronimoSi))
			{
				//carga la informacion de Traslado paciente
				this.accionConsultarTrasladosPaciente(con,forma,usuario);
			}	
			
			//cargo los datos
			return true;
		}				
		return false;
	}
	
	/**
	 * Carga la informacion de Tramite de Referencia
	 * @param Connection con
	 * @param TramiteReferenciaForm forma
	 * @param String estado
	 * */
	public void accionConsultarTramiteReferencia(Connection con, TramiteReferenciaForm forma,UsuarioBasico usuario)
	{
		HashMap parametros = new HashMap();
		
		//Consulta Tramite Referencia
		forma.setTramiteMap(TramiteReferencia.consultarTramiteReferencia(con,Integer.parseInt(forma.getReferenciaMap("numeroReferencia").toString())));		
		
		//almacena los valores externos al tramite de referencia necesarios para la seccion 
		forma.setTramiteMap("requiereAmbulancia",forma.getReferenciaMap("requiereAmbulancia").toString());
		forma.setTramiteMap("codigoTipoAmbulancia",forma.getReferenciaMap("codigoTipoAmbulancia").toString());
		forma.setTramiteMap("nombreTipoAmbulancia",forma.getReferenciaMap("nombreTipoAmbulancia").toString());
		forma.setTramiteMap("usuarioTramite",usuario.getNombreUsuario());
		forma.setTramiteMap("fechaTramite",UtilidadFecha.getFechaActual());
		forma.setTramiteMap("horaTramite",UtilidadFecha.getHoraActual());
		
		//en caso de no existir informacion en tramite referencia cargar valores por defecto
		if(forma.getTramiteMap("numRegistros").toString().equals("0"))
		{
			forma.setTramiteMap("estabd",ConstantesBD.acronimoNo);
			forma.setTramiteMap("finalizar",ConstantesBD.acronimoNo);
			forma.setInstitucionTramiteMap("numRegistros","0");
			forma.setTramiteMap("numeroreferenciatramite",forma.getReferenciaMap("numeroReferencia").toString());
			forma.setCodigosInstitucionesInsertados("");
		}	
		//carga la informacion de Instituciones Tramite Referencia existentes
		else
		{
			parametros.put("numeroreferenciatramite",forma.getTramiteMap("numeroreferenciatramite").toString());
			parametros.put("institucion",usuario.getCodigoInstitucionInt());		
			
			//Consulta Instituciones Tramite Referencia 
			forma.setInstitucionTramiteMap(TramiteReferencia.consultarInstitucionesTramiteReferencia(con, parametros));			
			forma.setCodigosInstitucionesInsertados(forma.getInstitucionTramiteMap("codigosInstitucionesInsertados").toString());			
		}		
	}
	
	/***************************************************************
	/*********************************Grupo de Metodos Servicios SIRC
	
	/**
	 * buscar el Index del Mapa Servicios Institucion Referencia de una Institucion dada
	 * si no lo encuentra carga los Servicios SIRC de la Referencia y se los da a la Institucion dada 
	 * @param con 
	 * @param TramiteReferenciaForm forma
	 * */	 		 
	public void accionBuscarServiciosForInstitucion(TramiteReferenciaForm forma, int institucion)
	{		
		int serv = 0;		
		//int pos_inst = Integer.parseInt(forma.getInstitucionTramiteMap("numRegistros").toString())-1;
		int pos_inst = Integer.parseInt(forma.getIndexInstitucionTramiteMap());
				
		if(forma.getInstitucionTramiteMap("cargado_"+forma.getIndexInstitucionTramiteMap()).toString().equals(ConstantesBD.acronimoNo))
		{
			//recorre el HashMap de Servicios de la Referencia para añadir los faltantes en el HashMap de Servicios Institucion Tramite  
			 for(int k=0; k<Integer.parseInt(forma.getServiciosReferencia("numRegistros").toString());k++)
			 {				 			 
				 /* En el mapa Institucion Tramite se inserta los registros de los Servicios Institucion Referencia
				  * con las llaves NombreCampo _ IndexdelaInstitucion _ IndexdelServicio
				  * */					  
				 
				 //campos de la tabla					 
				 forma.setInstitucionTramiteMap("codigoserviciosirc_"+pos_inst+"_"+serv,forma.getServiciosReferencia("codigoServicioSirc_"+k));
				 forma.setInstitucionTramiteMap("codigoservicio_"+pos_inst+"_"+serv,forma.getServiciosReferencia("codigoServicio_"+k));				 
				 forma.setInstitucionTramiteMap("institucionreferir_"+pos_inst+"_"+serv,forma.getInstitucionTramiteMap("institucionreferir_"+pos_inst));
				 forma.setInstitucionTramiteMap("institucion_"+pos_inst+"_"+serv,forma.getServiciosReferencia("institucion_"+k));
				 forma.setInstitucionTramiteMap("numeroreferenciatramite_"+pos_inst+"_"+serv,forma.getServiciosReferencia("numeroReferencia_"+k));
				 forma.setInstitucionTramiteMap("activo_"+pos_inst+"_"+serv, ConstantesBD.acronimoNo);
				 forma.setInstitucionTramiteMap("estado_"+pos_inst+"_"+serv,ConstantesBD.codigoNuncaValido);
				 forma.setInstitucionTramiteMap("motivo_"+pos_inst+"_"+serv,ConstantesBD.codigoNuncaValido);
				 forma.setInstitucionTramiteMap("funcionariocontactado_"+pos_inst+"_"+serv,"");					 
				 forma.setInstitucionTramiteMap("cargo_"+pos_inst+"_"+serv,"");					 
				 forma.setInstitucionTramiteMap("fechatramite_"+pos_inst+"_"+serv,UtilidadFecha.getFechaActual());				 
				 forma.setInstitucionTramiteMap("horatramite_"+pos_inst+"_"+serv,UtilidadFecha.getHoraActual());				 
				 forma.setInstitucionTramiteMap("numeroverificacion_"+pos_inst+"_"+serv,"");
				 forma.setInstitucionTramiteMap("observaciones_"+pos_inst+"_"+serv,"");
				 forma.setInstitucionTramiteMap("usuariomodifica_"+pos_inst+"_"+serv,"");
				 forma.setInstitucionTramiteMap("fechamodifica_"+pos_inst+"_"+serv,"");
				 forma.setInstitucionTramiteMap("horamodifica_"+pos_inst+"_"+serv,"");
					 
				 //campos adiccionales
				 forma.setInstitucionTramiteMap("funcionariocontactadoold_"+pos_inst+"_"+serv,"");
				 forma.setInstitucionTramiteMap("cargoold_"+pos_inst+"_"+serv,"");
				 forma.setInstitucionTramiteMap("nombreserviciosirc_"+pos_inst+"_"+serv,forma.getServiciosReferencia("nombreServicioSirc_"+k));
				 forma.setInstitucionTramiteMap("codigocups_"+pos_inst+"_"+serv,forma.getServiciosReferencia("codigoCups_"+k));
				 forma.setInstitucionTramiteMap("nombrecups_"+pos_inst+"_"+serv,forma.getServiciosReferencia("nombreCups_"+k));
				 forma.setInstitucionTramiteMap("observacionesreferencia_"+pos_inst+"_"+serv,forma.getServiciosReferencia("observaciones_"+k));
				
				 
				 //campos para manejo interno 
				 forma.setInstitucionTramiteMap("estabd_"+pos_inst+"_"+serv,ConstantesBD.acronimoNo);
				 forma.setInstitucionTramiteMap("seccion_"+pos_inst+"_"+serv, ConstantesBD.acronimoNo);					 
				 forma.setInstitucionTramiteMap("editar_"+pos_inst+"_"+serv, ConstantesBD.acronimoNo);					 
				 forma.setInstitucionTramiteMap("numRegistrosServicios_"+pos_inst,(serv+1)+"");
				 				 
				 serv++;			
			 }
					 
		forma.setInstitucionTramiteMap("cargado_"+pos_inst,ConstantesBD.acronimoSi);
		forma.setInstitucionTramiteMap("numeroreferenciatramite_"+pos_inst,forma.getTramiteMap("numeroreferenciatramite").toString());
		forma.setInstitucionTramiteMap("institucion_"+pos_inst,institucion);
		forma.setInstitucionTramiteMap("departamentoreferir_"+pos_inst,"");		
		forma.setInstitucionTramiteMap("ciudadreferir_"+pos_inst,"");
		forma.setInstitucionTramiteMap("paisreferir_"+pos_inst,"");
		}		
	}
	
		 
	/**
	 * Consulta de Servicios Institucion Referencia
	 * @param Connection con
	 * @param TramiteReferenciaForm forma
	 * @param UsuarioBasico usuario  
	 * */
	 public void accionConsultarServiciosSIRC(Connection con, TramiteReferenciaForm forma, UsuarioBasico usuario)
	 {
		 HashMap parametros = new HashMap();
		 //Guardara los Servicios Institucion Referencia almacenados en la base de datos 
		 HashMap serviciosAlmacenados = new HashMap();
		 int numRegistros_institucion=0;
		 int numRegistros_servicios=0;
		 int serv=0, esta=0; 		 	 
		 
		 //Carga el numero de Instituciones Tramite Referencia 
		 numRegistros_institucion = Integer.parseInt(forma.getInstitucionTramiteMap("numRegistros").toString());
		
		 
		 //Recorre el mapa de instituciones y carga los Servicios Institucion Referencia Cargados en tablas
		 for(int inst =0; inst<numRegistros_institucion; inst++)
		 {
			 //Parametros fijos de la busqueda de Servicios Instituciones Referencia 
			 parametros.put("numeroreferenciatramite",forma.getTramiteMap("numeroreferenciatramite").toString());
			 parametros.put("institucion",usuario.getCodigoInstitucionInt());
			 //Carga el numero de la institucion referir a la cual se le buscara los Servicios Institucion Referencia Cargados
			 parametros.put("institucionreferir",forma.getInstitucionTramiteMap("institucionreferir_"+inst));
			 
			 //Consulta Servivios Institucion Referencia por cada Institucion Tramite Referencia
			 serviciosAlmacenados = new HashMap();
			 serv = 0;
			 serviciosAlmacenados = (TramiteReferencia.consultarServiciosInstitucionesReferencia(con, parametros));		 
			 numRegistros_servicios = Integer.parseInt(serviciosAlmacenados.get("numRegistros").toString());
			 
			 //Carga el Indice de los Servicios Institucion Referencia
			 forma.setInstitucionTramiteMap("INDICES_MAPA_SERV",serviciosAlmacenados.get("INDICES_MAPA"));
					 
			 parametros = new HashMap();
			 
			 //	almacena en el mapa parametros las llaves de los servicios contenidos en el HashMap 
			 // de Servicios Institucion Referencia por la institucion Consultada  
			 for(int k=0; k<numRegistros_servicios; k++)
				 parametros.put("codigos_"+k,serviciosAlmacenados.get("codigoserviciosirc_"+k).toString()+"_"+serviciosAlmacenados.get("codigoservicio_"+k).toString());
			 
			 
			 // recorre el HashMap de Servicios de la Referencia para añadir los faltantes en el HashMap de Servicios Institucion Tramite  
			 for(int k=0; k<Integer.parseInt(forma.getServiciosReferencia("numRegistros").toString());k++)
			 {			 
				 esta = TramiteReferencia.existeDato(forma.getServiciosReferencia("codigoServicioSirc_"+k).toString()+"_"+forma.getServiciosReferencia("codigoServicio_"+k).toString(),"codigos_",parametros,numRegistros_servicios);
				 
				 //no se encuentra entre los Servicios Institucion Referencia cargados en Base de Datos
				 if(esta==ConstantesBD.codigoNuncaValido)
				 {			 
					 /* En el mapa Institucion Tramite se inserta los registros de los Servicios Institucion Referencia
					  * con las llaves NombreCampo _ IndexdelaInstitucion _ IndexdelServicio
					  * */					  
					 
					 //campos de la tabla					 
					 forma.setInstitucionTramiteMap("codigoserviciosirc_"+inst+"_"+serv,forma.getServiciosReferencia("codigoServicioSirc_"+k));
					 forma.setInstitucionTramiteMap("codigoservicio_"+inst+"_"+serv,forma.getServiciosReferencia("codigoServicio_"+k));				 
					 forma.setInstitucionTramiteMap("institucionreferir_"+inst+"_"+serv,forma.getInstitucionTramiteMap("institucionreferir_"+inst));
					 forma.setInstitucionTramiteMap("institucion_"+inst+"_"+serv,forma.getServiciosReferencia("institucion_"+k));
					 forma.setInstitucionTramiteMap("numeroreferenciatramite_"+inst+"_"+serv,forma.getServiciosReferencia("numeroReferencia_"+k));
					 forma.setInstitucionTramiteMap("activo_"+inst+"_"+serv, ConstantesBD.acronimoNo);
					 forma.setInstitucionTramiteMap("estado_"+inst+"_"+serv,ConstantesBD.codigoNuncaValido);
					 forma.setInstitucionTramiteMap("motivo_"+inst+"_"+serv,ConstantesBD.codigoNuncaValido);
					 forma.setInstitucionTramiteMap("funcionariocontactado_"+inst+"_"+serv,"");					 
					 forma.setInstitucionTramiteMap("cargo_"+inst+"_"+serv,"");					 
					 forma.setInstitucionTramiteMap("fechatramite_"+inst+"_"+serv,UtilidadFecha.getFechaActual());
					 forma.setInstitucionTramiteMap("horatramite_"+inst+"_"+serv,UtilidadFecha.getHoraActual());
					 forma.setInstitucionTramiteMap("numeroverificacion_"+inst+"_"+serv,"");
					 forma.setInstitucionTramiteMap("observaciones_"+inst+"_"+serv,"");
					 forma.setInstitucionTramiteMap("usuariomodifica_"+inst+"_"+serv,"");
					 forma.setInstitucionTramiteMap("fechamodifica_"+inst+"_"+serv,"");
					 forma.setInstitucionTramiteMap("horamodifica_"+inst+"_"+serv,"");
					 
					 //campos adiccionales
					 forma.setInstitucionTramiteMap("funcionariocontactadoold_"+inst+"_"+serv,"");
					 forma.setInstitucionTramiteMap("cargoold_"+inst+"_"+serv,"");
					 forma.setInstitucionTramiteMap("nombreserviciosirc_"+inst+"_"+serv,forma.getServiciosReferencia("nombreServicioSirc_"+k));
					 forma.setInstitucionTramiteMap("codigocups_"+inst+"_"+serv,forma.getServiciosReferencia("codigoCups_"+k));
					 forma.setInstitucionTramiteMap("nombrecups_"+inst+"_"+serv,forma.getServiciosReferencia("nombreCups_"+k));
					 forma.setInstitucionTramiteMap("observacionesreferencia_"+inst+"_"+serv,forma.getServiciosReferencia("observaciones_"+k));
					
					 
					 //campos para manejo interno 
					 forma.setInstitucionTramiteMap("estabd_"+inst+"_"+serv,ConstantesBD.acronimoNo);
					 forma.setInstitucionTramiteMap("seccion_"+inst+"_"+serv, ConstantesBD.acronimoNo);					 
					 forma.setInstitucionTramiteMap("editar_"+inst+"_"+serv, ConstantesBD.acronimoNo);					 
					 forma.setInstitucionTramiteMap("numRegistrosServicios_"+inst,(serv+1)+"");					 
					 serv++;			
				 }
				 //se encuentra en 
				 else if (esta!=ConstantesBD.codigoNuncaValido)
				 {					 
					 //campos de la tabla entre los Servicios Institucion Referencia cargados en Base de Datos				 
					 forma.setInstitucionTramiteMap("codigoserviciosirc_"+inst+"_"+serv,serviciosAlmacenados.get("codigoserviciosirc_"+esta));
					 forma.setInstitucionTramiteMap("codigoservicio_"+inst+"_"+serv,serviciosAlmacenados.get("codigoservicio_"+esta));				 
					 forma.setInstitucionTramiteMap("institucionreferir_"+inst+"_"+serv,serviciosAlmacenados.get("institucionreferir_"+esta));
					 forma.setInstitucionTramiteMap("institucion_"+inst+"_"+serv,serviciosAlmacenados.get("institucion_"+esta));
					 forma.setInstitucionTramiteMap("numeroreferenciatramite_"+inst+"_"+serv,serviciosAlmacenados.get("numeroreferenciatramite_"+esta));
					 forma.setInstitucionTramiteMap("activo_"+inst+"_"+serv,serviciosAlmacenados.get("activo_"+esta));
					 forma.setInstitucionTramiteMap("estado_"+inst+"_"+serv,serviciosAlmacenados.get("estado_"+esta));
					 forma.setInstitucionTramiteMap("motivo_"+inst+"_"+serv,serviciosAlmacenados.get("motivo_"+esta));
					 forma.setInstitucionTramiteMap("funcionariocontactado_"+inst+"_"+serv,serviciosAlmacenados.get("funcionariocontactado_"+esta));					 
					 forma.setInstitucionTramiteMap("cargo_"+inst+"_"+serv,serviciosAlmacenados.get("cargo_"+esta));					 
					 forma.setInstitucionTramiteMap("fechatramite_"+inst+"_"+serv,UtilidadFecha.conversionFormatoFechaAAp(serviciosAlmacenados.get("fechatramite_"+esta).toString()));
					 forma.setInstitucionTramiteMap("horatramite_"+inst+"_"+serv,serviciosAlmacenados.get("horatramite_"+esta));
					 forma.setInstitucionTramiteMap("numeroverificacion_"+inst+"_"+serv,serviciosAlmacenados.get("numeroverificacion_"+esta));
					 forma.setInstitucionTramiteMap("observaciones_"+inst+"_"+serv,serviciosAlmacenados.get("observaciones_"+esta));
					 forma.setInstitucionTramiteMap("usuariomodifica_"+inst+"_"+serv,serviciosAlmacenados.get("usuariomodifica_"+esta));
					 forma.setInstitucionTramiteMap("fechamodifica_"+inst+"_"+serv,serviciosAlmacenados.get("fechamodifica_"+esta));
					 forma.setInstitucionTramiteMap("horamodifica_"+inst+"_"+serv,serviciosAlmacenados.get("horamodifica_"+esta));
					 
					 //campos adiccionales
					 forma.setInstitucionTramiteMap("funcionariocontactadoold_"+inst+"_"+serv,serviciosAlmacenados.get("funcionariocontactado_"+esta));
					 forma.setInstitucionTramiteMap("cargoold_"+inst+"_"+serv,serviciosAlmacenados.get("cargo_"+esta));
					 forma.setInstitucionTramiteMap("nombreserviciosirc_"+inst+"_"+serv,serviciosAlmacenados.get("nombreserviciosirc_"+esta));
					 forma.setInstitucionTramiteMap("codigocups_"+inst+"_"+serv,serviciosAlmacenados.get("codigocups_"+esta));
					 forma.setInstitucionTramiteMap("nombrecups_"+inst+"_"+serv,serviciosAlmacenados.get("nombrecups_"+esta));			
					 //observaciones asignadas a cada procedimiento SIRC
					 forma.setInstitucionTramiteMap("observacionesreferencia_"+inst+"_"+serv,forma.getServiciosReferencia("observaciones_"+k));
					 
					 //campos para manejo interno 
					 forma.setInstitucionTramiteMap("estabd_"+inst+"_"+serv,ConstantesBD.acronimoSi);
					 forma.setInstitucionTramiteMap("seccion_"+inst+"_"+serv, ConstantesBD.acronimoNo);					 
					 forma.setInstitucionTramiteMap("editar_"+inst+"_"+serv, ConstantesBD.acronimoNo);	
					 forma.setInstitucionTramiteMap("numRegistrosServicios_"+inst,(serv+1)+"");
					 serv++;
				 }			
			 }
			
			 //se indica que la institucion tramite referencia posee Servicios Instituciones Cargados
			 forma.setInstitucionTramiteMap("cargado_"+inst,ConstantesBD.acronimoSi);
		 }		 
			
	 	 //	cargar las ciudades y departamentos de la seccion 
	 	 forma.setCiudades(Utilidades.obtenerCiudades(con));		 
	 	 //cargar los motivos SIRC de la seccion 
	 	 forma.setMotivosSirc(TramiteReferencia.cargarMotivosSirc(con,usuario.getCodigoInstitucionInt(),ConstantesBD.acronimoTipoMotivoCancelacion+"','"+ConstantesBD.acronimoTipoMotivoNegacion,ConstantesBD.acronimoSi));

	 }
	 
	 /**
	  * Buscar si existe servicios Sirc dependientes de la institucion para guardar 
	  * @param TramiteReferenciaForm forma
	  * @param int indexInstitucionTramite
	  * @return true -> existen servicios para guardar, false -> no existen servicios para guardar
	  * */
	 public boolean buscarServicosQGuardar(TramiteReferenciaForm forma, int indexInstitucionTramite)
	 {
		 int numRegistrosServicios = Integer.parseInt(forma.getInstitucionTramiteMap("numRegistrosServicios_"+indexInstitucionTramite).toString());
		 
		 for (int serv = 0; serv< numRegistrosServicios; serv++)
		 {
			 if(forma.getInstitucionTramiteMap("activo_"+indexInstitucionTramite+"_"+serv).toString().equals(ConstantesBD.acronimoSi))
				 return true;			 		 
		 }
		 
		 return false;
	 }
	 
	 /***************************************************************
	 /*********************************Grupo de Metodos Traslado Paciente
	 
	 /**
	  * consulta los datos de traslados paciente
	  * @param Connection con
	  * @param TTramiteReferenciaForm forma
	  * @param UsuarioBasico usuario)
	  * */
	 public void accionConsultarTrasladosPaciente(Connection con,TramiteReferenciaForm forma, UsuarioBasico usuario)
	 {
		 HashMap parametros = new HashMap();
		 
		 parametros.put("numeroreferenciatramite",forma.getTramiteMap("numeroreferenciatramite"));
		 parametros.put("institucion",usuario.getCodigoInstitucionInt());	 
		 
		 forma.setTrasladoPacienteMap(TramiteReferencia.consultarTrasladoPaciente(con, parametros));
		 forma.setCodigosInsTrasladosInsertados(forma.getTrasladoPacienteMap("codigosInsTransladosInsertados").toString());		 
	 }
	 
	 /**
	  * Carga elementos de Traslado Paciente a una institucion nueva o vacia
	  * @param TramiteReferenciaForm forma
	  * @param int institucion
	  * */
	 public void accionBuscarTraslado(TramiteReferenciaForm forma,int institucion)
	 {		 
		 int pos = Integer.parseInt(forma.getIndexInstTrasladoPacienteMap());
		 
		 //verifica que los datos del traslado no existan en el HashMap, esto en caso de una nueva institucion SIRC Traslado Paciente
		 if(forma.getEstabdTraslado().equals(ConstantesBD.acronimoNo) && !forma.getTrasladoPacienteMap().containsKey("activo_"+pos))
		 {				 
			 //carga los valores 
			 forma.setTrasladoPacienteMap("numeroreferenciatramite_"+pos,forma.getTramiteMap("numeroreferenciatramite"));
			 forma.setTrasladoPacienteMap("institucionsirc_"+pos,forma.getCodigoInst());
			 forma.setTrasladoPacienteMap("descripcioninstitucionsirc_"+pos,forma.getNombreInst());
			 forma.setTrasladoPacienteMap("institucion_"+pos,institucion);			 
			 forma.setTrasladoPacienteMap("estado_"+pos,ConstantesBD.codigoNuncaValido);
			 forma.setTrasladoPacienteMap("motivo_"+pos,ConstantesBD.codigoNuncaValido);
			 forma.setTrasladoPacienteMap("funcionariocontactado_"+pos,"");			 
			 forma.setTrasladoPacienteMap("cargo_"+pos,"");			 
			 forma.setTrasladoPacienteMap("fechatramite_"+pos,UtilidadFecha.getFechaActual());
			 forma.setTrasladoPacienteMap("horatramite_"+pos,UtilidadFecha.getHoraActual());
			 forma.setTrasladoPacienteMap("responsabletrasambulancia_"+pos,"");
			 forma.setTrasladoPacienteMap("numeromovil_"+pos,"");
			 forma.setTrasladoPacienteMap("placa_"+pos,"");
			 forma.setTrasladoPacienteMap("observaciones_"+pos,"");
			 forma.setTrasladoPacienteMap("usuariomodifica_"+pos,"");
			 forma.setTrasladoPacienteMap("fechamodifica_"+pos,"");			 
			 forma.setTrasladoPacienteMap("horamodifica_"+pos,"");		 
			 
			 //valores adicionales
			 forma.setTrasladoPacienteMap("funcionariocontactadoold_"+pos,"");
			 forma.setTrasladoPacienteMap("cargoold_"+pos,"");
			 
			 forma.setTrasladoPacienteMap("activo_"+pos,forma.getCheckTraslado());
			 forma.setTrasladoPacienteMap("hayerrores_"+pos,ConstantesBD.acronimoNo);
		 }			 
	 }
	 
	 /**
	  * validate del popup Traslado Paciente 
	  * @param TramiteReferenciaForm forma
	  * @param ActionErrors errores
	  * */
	 public ActionErrors accionValidateTraslado(TramiteReferenciaForm forma, ActionErrors errores)
	 {			 
				
		if(forma.getTrasladoPacienteMap("activo_"+forma.getIndexInstTrasladoPacienteMap()).toString().equals(ConstantesBD.acronimoSi))
		{
			if(forma.getTrasladoPacienteMap("estado_"+forma.getIndexInstTrasladoPacienteMap()).toString().equals(ConstantesBD.codigoNuncaValido+""))
				errores.add("descripcion",new ActionMessage("errors.required","El Estado del Traslado "));
			
			if((forma.getTrasladoPacienteMap("estado_"+forma.getIndexInstTrasladoPacienteMap()).toString().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado)) || (forma.getTrasladoPacienteMap("estado_"+forma.getIndexInstTrasladoPacienteMap()).toString().equals(ConstantesIntegridadDominio.acronimoEstadoNegado)))
			{
				if(forma.getTrasladoPacienteMap("motivo_"+forma.getIndexInstTrasladoPacienteMap()).toString().equals(ConstantesBD.codigoNuncaValido+""))
					errores.add("descripcion",new ActionMessage("errors.required","El Motivo del Traslado "));
			}
			
			if(forma.getTrasladoPacienteMap("funcionariocontactado_"+forma.getIndexInstTrasladoPacienteMap()).toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El Funcionario Contactado del Traslado "));
			
			if(forma.getTrasladoPacienteMap("cargo_"+forma.getIndexInstTrasladoPacienteMap()).toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El Cargo Contactado del Traslado "));
			
			if((!forma.getTrasladoPacienteMap("fechatramite_"+forma.getIndexInstTrasladoPacienteMap()).toString().equals("")) && (!forma.getTrasladoPacienteMap("horatramite_"+forma.getIndexInstTrasladoPacienteMap()).toString().equals("")))
			{
				if(!UtilidadFecha.validarFecha(forma.getTrasladoPacienteMap("fechatramite_"+forma.getIndexInstTrasladoPacienteMap()).toString()))
					errores.add("descripcion",new ActionMessage("errors.invalid",forma.getTrasladoPacienteMap("fechatramite_"+forma.getIndexInstTrasladoPacienteMap()).toString()+"  "+forma.getTrasladoPacienteMap("horatramite_"+forma.getIndexInstTrasladoPacienteMap()).toString()+" En el Traslado Paciente  "));
				
				if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),forma.getTrasladoPacienteMap("fechatramite_"+forma.getIndexInstTrasladoPacienteMap()).toString(),forma.getTrasladoPacienteMap("horatramite_"+forma.getIndexInstTrasladoPacienteMap()).toString()).isTrue())
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",forma.getTrasladoPacienteMap("fechatramite_"+forma.getIndexInstTrasladoPacienteMap()).toString()+"  "+forma.getTrasladoPacienteMap("horatramite_"+forma.getIndexInstTrasladoPacienteMap()).toString()+" En el Traslado Paciente ",UtilidadFecha.getFechaActual()+" "+UtilidadFecha.getHoraActual()));
			}
			else
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha del Traslado "));
		}		
		
		//verificacion utilizada en caso de que existan errores y el usuario cierre la ventana con la opcion cerrar de la paleta de la ventana
		if(!errores.isEmpty())
			forma.setTrasladoPacienteMap("hayerrores_"+forma.getIndexInstTrasladoPacienteMap(),ConstantesBD.acronimoSi);
		else
			forma.setTrasladoPacienteMap("hayerrores_"+forma.getIndexInstTrasladoPacienteMap(),ConstantesBD.acronimoNo);
		
		return errores;	 		 
	 }
	//----------------Fin Metodos
}