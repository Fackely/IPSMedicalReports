package com.princetonsa.action.manejoPaciente;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

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
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.manejoPaciente.RegistroEnvioInfAtencionIniUrgForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.manejoPaciente.RegistroEnvioInfAtencionIniUrg;
import com.princetonsa.pdf.AtencionInicialUrgenciasPdf;

/**
 * @author Jose Eduardo Arias Doncel 
 * 
 * ** Nota: Se solicita para proximas modificaciones, guardar la estructura del codigo y comentar debidamente los cambios y nuevas lineas.
 */
public class RegistroEnvioInfAtencionIniUrgAction extends Action
{	
	Logger logger = Logger.getLogger(RegistroEnvioInfAtencionIniUrgAction.class);
	
	
	/**
	 * Metodo execute del action
	 * */	
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, 
								 HttpServletRequest request, 
								 HttpServletResponse response) throws Exception
								 {	
		Connection con = null;
		try{
			if(response == null);

			if (form instanceof RegistroEnvioInfAtencionIniUrgForm) 
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

				//ActionErrors
				ActionErrors errores = new ActionErrors();

				RegistroEnvioInfAtencionIniUrgForm forma = (RegistroEnvioInfAtencionIniUrgForm)form;		
				String estado = forma.getEstado();		 

				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());			 
				logger.info("-------------------------------------");
				logger.info("-------------------------------------");

				if(estado.equals("menu"))
				{				 
					UtilidadBD.closeConnection(con);
					return mapping.findForward("menu");
				}
				if(estado.equals("menuInforme"))
				{
					estadoMenuInforme(con,forma,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("menuInforme");
				}
				else if(estado.equals("menuInformePaciente"))
				{ 
					estadoMenuInformeConvenios(con,forma,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("convenioReporte");
				}
				else if(estado.equals("menuInformePacienteConsulta"))
				{ 
					return estadoMenuInformePaciente(con, mapping, forma, usuario);
				}
				else if(estado.equals("empezarPaciente"))
				{
					forma.reset();
					errores = estadoEmpezarPacienteIngreso(con,forma,usuario,paciente,request);

					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("menu");
					}

					UtilidadBD.closeConnection(con);
					return mapping.findForward("paciente");
				}
				else if(estado.equals("empezarPacienteFiltros"))
				{				
					errores = estadoEmpezarPaciente(con,forma,usuario,paciente,request);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paciente");
				}
				else if(estado.equals("empezarRango"))
				{
					forma.reset();
					estadoEmpezarRango(con, forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("rango");
				}
				else if(estado.equals("buscarRango"))
				{
					estadoBuscarRango(con,forma,request,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("rango");
				}
				else if(estado.equals("generarInforme"))
				{
					estadoGenerarInforme(con,forma,usuario,request);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("menuInforme");
				}
				else if(estado.equals("filtroMedioEnvio"))
				{
					return accionFiltrarMediosEnvios(con, forma, response);
				}
				else if(estado.equals("enviarInforme"))
				{
					estadoEnviarInforme(con,forma,usuario,paciente,request);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("menuInforme");
				}else if(estado.equals("imprimir"))
				{
					if(paciente.getCodigoPersona()<0)
						paciente.cargar(con, Utilidades.convertirAEntero(forma.getDtoInformeAtencionXpacienteIngreso().getCodigoPersona()));
					return accionImprimir(con,mapping,usuario,paciente,request,Utilidades.convertirAEntero(forma.getCodigoPkInforme()));
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
	 * Método implementado para realizar la impresión del informe inicial de urgencias
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param id_informe
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, int cod_informe) 
	{
		UtilidadBD.closeConnection(con);
    	request.setAttribute("nombreArchivo", AtencionInicialUrgenciasPdf.pdfAtencionInicialUrgencia(usuario, request, paciente, cod_informe));
    	request.setAttribute("nombreVentana", "Atencion Inicial Urgencias");
    	return mapping.findForward("abrirPdf");
	}
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param cod_informe_inco
	 * @param forma
	 * @return
	 */
	private static String xmlReportatenIniUrg(Connection con, UsuarioBasico usuario,
			PersonaBasica paciente, HttpServletRequest request,
			int cod_informe, RegistroEnvioInfAtencionIniUrgForm forma) {
		
		String  archivoXMLReport = AtencionInicialUrgenciasPdf.xmlInformeAtenIniUrg(con, usuario, request, paciente, cod_informe, forma);
		return archivoXMLReport;
	}
	
	//*****************************************************************
	
	/**
	 * Operaciones del estado empezar paciente
	 * @param Connection con
	 * @param RegistroEnvioInfAtencionIniUrgForm forma
	 * @param HttpServletRequest request
	 * */
	public ActionErrors estadoEmpezarPaciente(Connection con,RegistroEnvioInfAtencionIniUrgForm forma,UsuarioBasico usuario,PersonaBasica paciente,HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();		
		errores = RegistroEnvioInfAtencionIniUrg.validarPaciente(con,paciente,usuario.getCodigoInstitucionInt());
		
		if(!errores.isEmpty())	
			return errores;		
		else
		{			
			boolean soloInfNoGen = false;
			if(forma.getParametrosBusqueda().containsKey("informesNoGen") && 
					!forma.getParametrosBusqueda().get("informesNoGen").toString().equals(""))
				soloInfNoGen = UtilidadTexto.getBoolean(forma.getParametrosBusqueda("informesNoGen").toString());
				
			forma.setListadoArray(
					RegistroEnvioInfAtencionIniUrg.getListadoInformeInicUrgeXPaciente(
							con,
							usuario.getCodigoInstitucionInt(),
							paciente.getCodigoPersona(),
							false,
							soloInfNoGen));						
		}		
		
		return errores;
	}
	
	//*****************************************************************
	
	/**
	 * Operaciones del estado empezar paciente por convenio
	 * @param Connection con
	 * @param RegistroEnvioInfAtencionIniUrgForm forma
	 * @param HttpServletRequest request
	 * */
	public ActionErrors estadoEmpezarPacienteIngreso(
			Connection con,
			RegistroEnvioInfAtencionIniUrgForm forma,
			UsuarioBasico usuario,PersonaBasica paciente,
			HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();		
		errores = RegistroEnvioInfAtencionIniUrg.validarPaciente(con,paciente,usuario.getCodigoInstitucionInt());
		
		if(!errores.isEmpty())	
			return errores;		
		else
		{		
			forma.setListadoArray(
					RegistroEnvioInfAtencionIniUrg.getListadoInformeInicUrgeXPacienteIngresos(
							con,
							usuario.getCodigoInstitucionInt(),
							paciente.getCodigoPersona()));
		}		
		
		return errores;
	}
	
	//*****************************************************************
	
	/**
	 * Operaciones del estado empezar rango
	 * @param Connection con
	 * @param RegistroEnvioInfAtencionIniUrgForm forma
	 * */
	public void estadoEmpezarRango(Connection con,RegistroEnvioInfAtencionIniUrgForm forma)
	{
		//Almacena la informacion de los convenios
		forma.setListadoArrayConvenio(Utilidades.obtenerConvenios(
				con,
				"",
				ConstantesBD.codigoTipoContratoEvento+"",
				false,
				"",
				false,
				false,
				"",
				true));				
		
		//Inicializa los parametros de busqueda
		forma.setParametrosBusqueda(RegistroEnvioInfAtencionIniUrg.inicializarParametrosBusquedaRango());				
	}
	
	//*****************************************************************
	
	/**
	 * Operaciones del estado busqueda por rango
	 * @param Connection con
	 * @param RegistroEnvioInfAtencionIniUrgForm forma
	 * @param HttpServletRequest request
	 * */
	public void estadoBuscarRango(Connection con,RegistroEnvioInfAtencionIniUrgForm forma,HttpServletRequest request,UsuarioBasico usuario)
	{
		//Validaciones para la busqueda por rango
		ActionErrors errores = new ActionErrors();
		errores = RegistroEnvioInfAtencionIniUrg.validacionBusquedaXRango(forma.getParametrosBusqueda());		
		
		if(errores.isEmpty())
		{
			forma.setListadoArray(
					RegistroEnvioInfAtencionIniUrg.getListadoInformeInicUrge(
						con, 
						forma.getParametrosBusqueda("fechaInicialValoracion").toString(),
						forma.getParametrosBusqueda("fechaFinalValoracion").toString(),
						forma.getParametrosBusqueda("fechaInicialGeneracion").toString(),
						forma.getParametrosBusqueda("fechaFinalGeneracion").toString(),
						forma.getParametrosBusqueda("fechaInicialEnvio").toString(), 
						forma.getParametrosBusqueda("fechaFinalEnvio").toString(),
						forma.getParametrosBusqueda("estadoEnvio").toString(), 
						forma.getParametrosBusqueda("convenio").toString(),
						forma.getParametrosBusqueda("informesNoGen").toString(),
						usuario.getCodigoInstitucionInt()));			
		}
		else
		{		
			saveErrors(request, errores);			
		}
	}	
	
	//*****************************************************************
	
	/**
	 * filtros para mostrar los menus
	 * @param Connection con
	 * @param RegistroEnvioInfAtencionIniUrgForm forma
	 * @param UsuarioBasico usuario
	 * */
	public void estadoMenuInforme(Connection con,RegistroEnvioInfAtencionIniUrgForm forma,UsuarioBasico usuario)
	{
		//Consulta la información del paciente
		logger.info("************************************************************************************\n");
		logger.info("codigo de la institucion: "+usuario.getCodigoInstitucionInt());
		logger.info("************************************************************************************\n");
		forma.setDtoInformeAtencionXpacienteIngreso(
				RegistroEnvioInfAtencionIniUrg.cargarInfoPaciente(
						con,
						usuario.getCodigoInstitucionInt(),
						forma.getParametrosExternos().containsKey("ingresoFiltro")?forma.getParametrosExternos("ingresoFiltro").toString():"",						
						forma.getParametrosExternos().containsKey("cuentaFiltro")?forma.getParametrosExternos("cuentaFiltro").toString():"", 
						forma.getParametrosExternos().containsKey("convenioFiltro")?forma.getParametrosExternos("convenioFiltro").toString():"",
						forma.getParametrosExternos().containsKey("codigoPkInforme")?forma.getParametrosExternos("codigoPkInforme").toString():"",
						forma.getParametrosExternos().containsKey("fechaInicialEnvio")?forma.getParametrosExternos("fechaInicialEnvio").toString():"",
						forma.getParametrosExternos().containsKey("fechaFinalEnvio")?forma.getParametrosExternos("fechaFinalEnvio").toString():"",
						false)
				);
		logger.info("************************************************************************************\n");
		logger.info("codigo del paciente de Ingreso: "+forma.getDtoInformeAtencionXpacienteIngreso().getCodigoPk());
		logger.info("************************************************************************************\n");
		
		if(forma.getDtoInformeAtencionXpacienteIngreso().getEstadoInforme().equals(ConstantesIntegridadDominio.acronimoEstadoEnviado) 
				|| forma.getDtoInformeAtencionXpacienteIngreso().getEstadoInforme().equals(ConstantesIntegridadDominio.acronimoEstadoGenerado))
		{
			forma.setArrayUtilitario(Utilidades.obtenerEmpresas(con,usuario.getCodigoInstitucionInt(),true));
			HashMap convenio = new HashMap();
			convenio.put("codigo",forma.getDtoInformeAtencionXpacienteIngreso().getCodigoConvenio());
			convenio.put("descripcion",forma.getDtoInformeAtencionXpacienteIngreso().getDescripcionConvenio());
			convenio.put("esConvenio",ConstantesBD.acronimoSi);
			forma.getArrayUtilitario().add(convenio);
						
			//Inicializa los medios de envio del convenio
			forma.setListadoArrayMediosEnvio(Convenio.cargarMediosEnvio(con, forma.getDtoInformeAtencionXpacienteIngreso().getCodigoConvenio()));
			forma.setParametrosExternos("convenio",forma.getDtoInformeAtencionXpacienteIngreso().getCodigoConvenio()+ConstantesBD.separadorSplit+ConstantesBD.acronimoSi);
			forma.setParametrosExternos("medio","");
		}
		else 
		{
			forma.setArrayUtilitario(new ArrayList<HashMap<String,Object>>());
		}
		
		forma.setParametrosExternos("operacionExitosa",ConstantesBD.acronimoNo);
	}
	//*****************************************************************
	
	/**
	 * Genera el informe a partir del dto cargado
	 * @param Connection con
	 * @param RegistroEnvioInfAtencionIniUrgForm forma
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest resquest
	 * */	
	public void estadoGenerarInforme(Connection con,RegistroEnvioInfAtencionIniUrgForm forma,UsuarioBasico usuario, HttpServletRequest request)
	{
		HashMap resultado = new HashMap();
		UtilidadBD.iniciarTransaccion(con);
		resultado = RegistroEnvioInfAtencionIniUrg.insertarInformeAtencionIniUrg(
				con,
				forma.getDtoInformeAtencionXpacienteIngreso().getIdIngreso()+"",
				forma.getDtoInformeAtencionXpacienteIngreso().getCodigoCuenta()+"",
				forma.getParametrosExternos().containsKey("convenioFiltro")?forma.getParametrosExternos("convenioFiltro").toString():"",
				usuario.getCodigoInstitucionInt(),
				usuario.getLoginUsuario());
		
		if(Utilidades.convertirAEntero(resultado.get("codigoPk").toString())>0)
		{
			UtilidadBD.finalizarTransaccion(con);
			
			forma.setParametrosExternos("informeGeneradoExitos",ConstantesBD.acronimoSi);
			forma.setDtoInformeAtencionXpacienteIngreso(RegistroEnvioInfAtencionIniUrg.cargarInfoPaciente(					
					con,
					usuario.getCodigoInstitucionInt(),
					forma.getParametrosExternos().containsKey("ingresoFiltro")?forma.getParametrosExternos("ingresoFiltro").toString():"",						
					forma.getParametrosExternos().containsKey("cuentaFiltro")?forma.getParametrosExternos("cuentaFiltro").toString():"", 
					forma.getParametrosExternos().containsKey("convenioFiltro")?forma.getParametrosExternos("convenioFiltro").toString():"",
					resultado.get("codigoPk").toString(),
					"",
					"",
					false
					));
			
			forma.setParametrosExternos("operacionExitosa",ConstantesBD.acronimoSi);
			forma.setParametrosExternos("mensaje","Informe Generado con Exito");
			forma.setArrayUtilitario(Utilidades.obtenerEmpresas(con,usuario.getCodigoInstitucionInt(),true));
			HashMap convenio = new HashMap();
			convenio.put("codigo",forma.getDtoInformeAtencionXpacienteIngreso().getCodigoConvenio());
			convenio.put("descripcion",forma.getDtoInformeAtencionXpacienteIngreso().getDescripcionConvenio());
			convenio.put("esConvenio",ConstantesBD.acronimoSi);
			forma.getArrayUtilitario().add(convenio);
			forma.setCodigoPkInforme(resultado.get("codigoPk").toString());
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			forma.setParametrosExternos("operacionExitosa",ConstantesBD.acronimoNo);
			ActionErrors errores = (ActionErrors)resultado.get("error");
			saveErrors(request, errores);			
		}			
	}	
	//*****************************************************************
	
	/**
	 * Inserta el envio del informe
	 * @param Connection con
	 * @param RegistroEnvioInfAtencionIniUrgForm forma
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	 public static void estadoEnviarInforme(Connection con,RegistroEnvioInfAtencionIniUrgForm forma,UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request)
	 {
		 String pathArchivoIncoxml = "" ;
		 UtilidadBD.iniciarTransaccion(con);
		 
		//********************************************************************************************************************
        // Generacion del archivo XML de Inconsitencias
		if(forma.getParametrosExternos("medio").equals(ConstantesIntegridadDominio.acronimoIntercambioElectDatos))
		{
        	if((pathArchivoIncoxml= xmlReportatenIniUrg(con, usuario, paciente, request, 
        			Utilidades.convertirAEntero(forma.getCodigoPkInforme()), forma))!=null){
        		forma.getDtoInformeAtencionXpacienteIngreso().setPathArchivoIncoXml(pathArchivoIncoxml);
        		forma.getDtoInformeAtencionXpacienteIngreso().setArchivoInconGenerado(ConstantesBD.acronimoSi);
        	}else
        		forma.getDtoInformeAtencionXpacienteIngreso().setArchivoInconGenerado(ConstantesBD.acronimoSi);
        }else
        	forma.getDtoInformeAtencionXpacienteIngreso().setArchivoInconGenerado(ConstantesBD.acronimoSi);
        // Fin Generacion del archivo XML de Inconsitencias
        //********************************************************************************************************************
		 
		 int codigo = RegistroEnvioInfAtencionIniUrg.insertarEnvioInformeAtencionIniUrg(
				 con,
				 forma.getDtoInformeAtencionXpacienteIngreso().getCodigoPk(),
				 forma.getParametrosExternos("convenio").toString().split(ConstantesBD.separadorSplit)[1].equals(ConstantesBD.acronimoSi)?forma.getParametrosExternos("convenio").toString().split(ConstantesBD.separadorSplit)[0]:"",
				 forma.getParametrosExternos("medio").toString(), 
				 forma.getParametrosExternos("convenio").toString().split(ConstantesBD.separadorSplit)[1].equals(ConstantesBD.acronimoNo)?forma.getParametrosExternos("convenio").toString().split(ConstantesBD.separadorSplit)[0]:"",
				 usuario.getLoginUsuario(),pathArchivoIncoxml);
		 
		 if(codigo > 0)
		 {
			 UtilidadBD.finalizarTransaccion(con);
			 forma.setParametrosExternos("operacionExitosa",ConstantesBD.acronimoSi);
			 forma.setParametrosExternos("mensaje","Envio Realizado con Exito");
			 
			 forma.setDtoInformeAtencionXpacienteIngreso(RegistroEnvioInfAtencionIniUrg.cargarInfoPaciente(					
						con,
						usuario.getCodigoInstitucionInt(),
						forma.getParametrosExternos().containsKey("ingresoFiltro")?forma.getParametrosExternos("ingresoFiltro").toString():"",						
						forma.getParametrosExternos().containsKey("cuentaFiltro")?forma.getParametrosExternos("cuentaFiltro").toString():"", 
						forma.getParametrosExternos().containsKey("convenioFiltro")?forma.getParametrosExternos("convenioFiltro").toString():"",
						forma.getDtoInformeAtencionXpacienteIngreso().getCodigoPk()+"",
						"",
						"",
						false
						));
			 
			forma.setCodigoPkInforme(forma.getDtoInformeAtencionXpacienteIngreso().getCodigoPk()+"");
		 }
		 else
		 {
			 ActionErrors errores = new ActionErrors();
			 errores.add("descripcion",new ActionMessage("errors.notEspecific","No fue posible enviar el Informe."));
			 forma.setParametrosExternos("operacionExitosa",ConstantesBD.acronimoNo);
			 UtilidadBD.abortarTransaccion(con);
		 }
			 
	 } 
	 
	//*****************************************************************
	 
	 /**
	 * Estado menu informe Convenios
	 * @param Connection con
	 * @param RegistroEnvioInfAtencionIniUrgForm forma
	 * @param UsuarioBasico usuario
	 * */
	 public void estadoMenuInformeConvenios(
			 	Connection con,
				RegistroEnvioInfAtencionIniUrgForm forma,
				UsuarioBasico usuario)
	 {
		 forma.setListadoConvenios(RegistroEnvioInfAtencionIniUrg.getConveniosPacienteReporte(
				con,
				forma.getParametrosExternos("cuentaFiltro").toString(),
				forma.getParametrosExternos("ingresoFiltro").toString()));
	 }
	//*****************************************************************
	 
	 /**
	 * Estado menu informe paciente
	 * @param Connection con
	 * @param ActionMapping mapping
	 * @param RegistroEnvioInfAtencionIniUrgForm forma
	 * @param UsuarioBasico usuario
	 * */
	public ActionForward estadoMenuInformePaciente(
													Connection con,
													ActionMapping mapping,
													RegistroEnvioInfAtencionIniUrgForm forma,
													UsuarioBasico usuario)
	{
		boolean cargarConvenios = false;
		forma.setParametrosExternos("operacionExitosa",ConstantesBD.acronimoNo);
		
		//Consulta la información del convenio
		if(forma.getParametrosExternos().containsKey("convenioFiltro") && 
				Utilidades.convertirAEntero(forma.getParametrosExternos("convenioFiltro").toString()) > 0)		
			cargarConvenios = false;
		else		
			cargarConvenios = true;
			
		//Consulta la información del paciente
		forma.setDtoInformeAtencionXpacienteIngreso(
				RegistroEnvioInfAtencionIniUrg.cargarInfoPaciente(
						con,
						usuario.getCodigoInstitucionInt(),
						forma.getParametrosExternos().containsKey("ingresoFiltro")?forma.getParametrosExternos("ingresoFiltro").toString():"",						
						forma.getParametrosExternos().containsKey("cuentaFiltro")?forma.getParametrosExternos("cuentaFiltro").toString():"", 
						forma.getParametrosExternos().containsKey("convenioFiltro")?forma.getParametrosExternos("convenioFiltro").toString():"",
						forma.getParametrosExternos().containsKey("codigoPkInforme")?forma.getParametrosExternos("codigoPkInforme").toString():"",
						forma.getParametrosExternos().containsKey("fechaInicialEnvio")?forma.getParametrosExternos("fechaInicialEnvio").toString():"",
						forma.getParametrosExternos().containsKey("fechaFinalEnvio")?forma.getParametrosExternos("fechaFinalEnvio").toString():"",
						cargarConvenios)
				);
		
		
		if(forma.getDtoInformeAtencionXpacienteIngreso().getEstadoInforme().equals(ConstantesIntegridadDominio.acronimoEstadoEnviado) 
				|| forma.getDtoInformeAtencionXpacienteIngreso().getEstadoInforme().equals(ConstantesIntegridadDominio.acronimoEstadoGenerado))
		{
			forma.setArrayUtilitario(Utilidades.obtenerEmpresas(con,usuario.getCodigoInstitucionInt(),true));
			HashMap convenio = new HashMap();
			convenio.put("codigo",forma.getDtoInformeAtencionXpacienteIngreso().getCodigoConvenio());
			convenio.put("descripcion",forma.getDtoInformeAtencionXpacienteIngreso().getDescripcionConvenio());
			convenio.put("esConvenio",ConstantesBD.acronimoSi);
			forma.getArrayUtilitario().add(convenio);
			
			
			//Inicializa los medios de envio del convenio
			forma.setListadoArrayMediosEnvio(Convenio.cargarMediosEnvio(con, forma.getDtoInformeAtencionXpacienteIngreso().getCodigoConvenio()));
			forma.setParametrosExternos("convenio",forma.getDtoInformeAtencionXpacienteIngreso().getCodigoConvenio()+ConstantesBD.separadorSplit+ConstantesBD.acronimoSi);
			forma.setParametrosExternos("medio","");
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("menuInforme");
		}
		else
		{
			forma.setArrayUtilitario(new ArrayList<HashMap<String,Object>>());
			
			if(cargarConvenios)
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("menuInformePaciente");				
			}
						
			UtilidadBD.closeConnection(con);
			return mapping.findForward("menuInforme");			
		}		
	}	
		
	//*****************************************************************
		
	/**
	 * Método para filtrar los Medios de Envio de un informe
	 * @param con
	 * @param RegistroEnvioInfAtencionIniUrgForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarMediosEnvios(
			Connection con,
			RegistroEnvioInfAtencionIniUrgForm forma, 
			HttpServletResponse response) 
	{

		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>medio</id-select>" +
				"<id-arreglo>medios</id-arreglo>" +
			"</infoid>" ;
		
		String convenioEnvioFiltro = forma.getParametrosExternos().containsKey("convenioEnvioFiltro")?forma.getParametrosExternos("convenioEnvioFiltro").toString():"";
		int codigo = ConstantesBD.codigoNuncaValido;
		boolean esConvenio = false;
				
		if(!convenioEnvioFiltro.equals(""))
		{
			if(convenioEnvioFiltro.split(ConstantesBD.separadorSplit).length > 1)
			{
				if((convenioEnvioFiltro.split(ConstantesBD.separadorSplit)[1]).equals(ConstantesBD.acronimoSi))
				{
					esConvenio = true;
					codigo = Utilidades.convertirAEntero(convenioEnvioFiltro.split(ConstantesBD.separadorSplit)[0]);					
				}
				else
				{
					esConvenio = false;
					codigo = Utilidades.convertirAEntero(convenioEnvioFiltro.split(ConstantesBD.separadorSplit)[0]);
				}
			}
			else
				codigo = Utilidades.convertirAEntero(convenioEnvioFiltro);
		}
		
		if(!esConvenio)
		{	
			//inicializo el array
			forma.setListadoArrayMediosEnvio(new ArrayList<HashMap<String,Object>>());
			
			HashMap mapa = new HashMap();
			mapa.put("codigo", ConstantesIntegridadDominio.acronimoFax);
			mapa.put("descripcion","FAX");
			forma.getListadoArrayMediosEnvio().add(mapa);
			
			mapa = new HashMap();		
			mapa.put("codigo", ConstantesIntegridadDominio.acronimoEmail);
			mapa.put("descripcion","Correo Electronico");
			forma.getListadoArrayMediosEnvio().add(mapa);
			
			mapa = new HashMap();
			mapa.put("codigo", ConstantesIntegridadDominio.acronimoIntercambioElectDatos);
			mapa.put("descripcion","Intercambio Electrónico de Datos (EDI)");
			forma.getListadoArrayMediosEnvio().add(mapa);
			
			resultado += "<medios>";
				resultado += "<codigo>"+ConstantesIntegridadDominio.acronimoFax+"</codigo>";
				resultado += "<descripcion>FAX</descripcion>";
			resultado += "</medios>";
			
			resultado += "<medios>";
				resultado += "<codigo>"+ConstantesIntegridadDominio.acronimoEmail+"</codigo>";
				resultado += "<descripcion>Correo Electronico</descripcion>";
			resultado += "</medios>";
				
			resultado += "<medios>";
				resultado += "<codigo>"+ConstantesIntegridadDominio.acronimoIntercambioElectDatos+"</codigo>";
				resultado += "<descripcion>Intercambio Electronico de Datos (EDI)</descripcion>";
			resultado += "</medios>";			
			
			resultado += "</respuesta>";
		}
		else
		{
			forma.setListadoArrayMediosEnvio(Convenio.cargarMediosEnvio(con, codigo));		
			for(HashMap elemento:forma.getListadoArrayMediosEnvio())
			{
				resultado += "<medios>";
					resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
					resultado += "<descripcion>"+elemento.get("descripcion")+"</descripcion>";
				resultado += "</medios>";
			}
			
			resultado += "</respuesta>";
		}
				
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
			logger.error("Error al enviar respuesta AJAX en accionFiltrarMediosEnvios: "+e);
		}
		return null;
	}
	
	//*****************************************************************
	
}