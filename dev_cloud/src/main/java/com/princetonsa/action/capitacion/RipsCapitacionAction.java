/*
 * 23 de Abril, 2007
 */
package com.princetonsa.action.capitacion;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
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

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.capitacion.RipsCapitacionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.rips.Rips;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Sebastián Gómez R.
 *
 * Controlador para la creación de los archivos RIPS
 */
public class RipsCapitacionAction extends Action 
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 * 
	 * @uml.property name="logger"
	 * @uml.associationEnd 
	 * @uml.property name="logger" multiplicity="(1 1)"
	 */
	private Logger logger = Logger.getLogger(RipsCapitacionAction.class);
	

	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */	
	public ActionForward execute(	ActionMapping mapping, 
														ActionForm form, 
														HttpServletRequest request,
														HttpServletResponse response) throws Exception
	{	
		Connection con=null;
		try{
		String estado=""; //para verificar estado del Form
		/* Revisión de instancia de Form */
		if( form instanceof RipsCapitacionForm )
		{
			RipsCapitacionForm ripsForm = (RipsCapitacionForm)form;
			
			
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
				e.printStackTrace(); /* es lo mismo que un logger.info */
				
				ripsForm.reset();
				
				logger.warn("Problemas con la base de datos "+e);
				request.setAttribute("codigoDescripcionError", "errors.problemasBD");
				//Salir en caso de error (se busca cerrar bien la conexión)
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.problemasBD", "", true);
			}
			
			//*****OBJETOS QUE SE VAN A USAR*****************************************
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			estado=ripsForm.getEstado();
			logger.warn("Estado RipsCapitacionAction=> "+estado);
			//__________________________________________________
			//Según el estado el Action va por diferentes caminos
			
			if(estado.equals("empezar"))
			{
				return accionEmpezar(con,ripsForm,mapping);
				
			}
			//estado usado para consultar los datos de la cuenta de cobro
			else if(estado.equals("buscarConvenio"))
			{
				return accionBuscarConvenio(con,ripsForm,response,usuario);
			}
			else if(estado.equals("generar"))
			{
				return accionGenerar(con,ripsForm,mapping,request,usuario);
			}
			
			else if(estado.equals("detalle"))
			{
				return accionDetalle(mapping,usuario,con,ripsForm);
			}
			else
			{
				//con el objetivo de cerrar conexiones que se puedan quedar activas
				UtilidadBD.cerrarConexion(con);
			}
			UtilidadBD.cerrarConexion(con);
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
	 * Método implementado para cargar el detalle de un archivo RIPS generado
	 * @param mapping
	 * @param usuario
	 * @param con
	 * @param ripsForm
	 * @return
	 * @throws IPSException 
	 */
	private ActionForward accionDetalle(ActionMapping mapping, UsuarioBasico usuario, Connection con, RipsCapitacionForm ripsForm) throws IPSException 
	{
		Rips rips=new Rips();
		this.llenarMundo(ripsForm,rips);
		
		//se consulta el contenido del archivo
		ripsForm.setContenidoArchivo(
				rips.cargarArchivo(
						con,
						usuario.getCodigoInstitucionInt(),
						ripsForm.getArchivo()
									)
						);
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumen");
	}


	/**
	 * Método que genera los RIPS CAPITACION
	 * @param con
	 * @param ripsForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 * @throws IPSException 
	 */
	private ActionForward accionGenerar(Connection con, RipsCapitacionForm ripsForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) throws IPSException 
	{
		//manejo de errores
		ActionErrors errores = new ActionErrors();
		
		//Instanciación del mundo de RIPS
		Rips rips=new Rips();
		//llenar parámetros de generación rips
		this.llenarMundo(ripsForm,rips);
		rips.setLoginUsuario(usuario.getLoginUsuario());
		
		//llamado a método del mundo para generar los archivos RIPS
		ripsForm.setResultados(rips.generarArchivos(con,usuario.getCodigoInstitucionInt()));
		
		ripsForm.setNumeroRemision(rips.getNumeroRemision());
		UtilidadBD.closeConnection(con);
		
		//se verifica si se encontraron registros en la generación RIPS
		if(rips.isHuboRegistros())
		{
			//Se verifica si hubo errores en la generación de RIPS
			if(ripsForm.getResultados("error")!=null)
			{
				errores.add("Error al generar Rips",new ActionMessage(ripsForm.getResultados("error").toString()));
				saveErrors(request,errores);
				
			}
			else
			{
				ripsForm.setHuboInconsistencias(rips.isHuboInconsistencias());
				ripsForm.setPathGeneracion(rips.getPathGeneracion());
				ripsForm.setBackupArchivo(rips.getNomZip());
				this.generarLog(ripsForm,usuario);
				return mapping.findForward("resumen");
			}
		}
		else
		{
			
			errores.add("no se enonctró información",new ActionMessage("errors.notEspecific","No se encontró información para generar RIPS"));
			saveErrors(request,errores);
		}
		
		return mapping.findForward("principal");
	}


	/**
	 * Método implementado para generar el LOG tipo archivo de los RIPS de capitación
	 * @param ripsForm
	 * @param usuario
	 */
	private void generarLog(RipsCapitacionForm ripsForm, UsuarioBasico usuario) 
	{
		String log;
		log="\n            ====ARCHIVOS RIPS GENERADOS===== ";
		
			//archivos básicos
			if(ripsForm.getResultados(ConstantesBD.ripsCT)!=null)
				log+="\n "+ConstantesBD.ripsCT+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAF)!=null)
				log+="\n "+ConstantesBD.ripsAF+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAD)!=null)
				log+="\n "+ConstantesBD.ripsAD+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsUS)!=null)
				log+="\n "+ConstantesBD.ripsUS+ripsForm.getNumeroRemision()+".txt";
			
			//archivos de seleccion
			if(ripsForm.getResultados(ConstantesBD.ripsAC)!=null)
				log+="\n "+ConstantesBD.ripsAC+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAH)!=null)
				log+="\n "+ConstantesBD.ripsAH+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAM)!=null)
				log+="\n "+ConstantesBD.ripsAM+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAN)!=null)
				log+="\n "+ConstantesBD.ripsAN+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAP)!=null)
				log+="\n "+ConstantesBD.ripsAP+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAT)!=null)
				log+="\n "+ConstantesBD.ripsAT+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAU)!=null)
				log+="\n "+ConstantesBD.ripsAU+ripsForm.getNumeroRemision()+".txt";
			
			//archivo inconsistencias
			if(ripsForm.getResultados(ConstantesBD.ripsInconsistencias)!=null)
				log+="\n "+ConstantesBD.ripsInconsistencias+ripsForm.getNumeroRemision()+".txt";
			
		
		log+="\n========================================================\n\n\n " ;	
		
		LogsAxioma.enviarLog(ConstantesBD.logRipsCapitacionCodigo, log, ConstantesBD.tipoRegistroLogInsercion,usuario.getLoginUsuario());
		
	}


	/**
	 * Método implementado para llenar el mundo de los RIPS
	 * @param ripsForm
	 * @param rips
	 */
	private void llenarMundo(RipsCapitacionForm ripsForm, Rips rips) 
	{
		rips.setConvenio(Integer.parseInt(ripsForm.getCodigoConvenio()));
		rips.setTipoCodigo(Integer.parseInt(ripsForm.getTipoCodigo()));
		rips.setFechaInicial(ripsForm.getFechaInicial());
		rips.setFechaFinal(ripsForm.getFechaFinal());
		rips.setFechaRemision(ripsForm.getFechaRemision());
		rips.setFechaFactura(ripsForm.getFechaElaboracion());
		rips.setNumeroRemision(ripsForm.getNumeroRemision());
		rips.setNumeroCuentaCobro(Double.parseDouble(ripsForm.getNumeroCuentaCobro()));
		rips.setTipoRips("Capitacion");
		rips.setSeleccionArchivos(ripsForm.getSeleccion());
		rips.setRipsConFactura(true);
		
		//http://xplanner.princeton:9090/xplanner2008/do/view/task?oid=3005
		Vector<Object> contratos= new Vector<Object>();
		Vector<Object> numeroContratos= new Vector<Object>();
		if(!UtilidadTexto.isEmpty(ripsForm.getContrato()))
		{	
			//ESO QUIERE DECIR QUE SELECCIONARON UNO
			contratos.add(ripsForm.getContrato().split(ConstantesBD.separadorSplit)[0]);
			numeroContratos.add(ripsForm.getContrato().split(ConstantesBD.separadorSplit)[1]);
		}
		else
		{
			for(int i=0;i<Integer.parseInt(ripsForm.getContratos("numRegistros").toString());i++)
			{
				contratos.add(ripsForm.getContratos("codigo_"+i));
				numeroContratos.add(ripsForm.getContratos("numero_"+i));
			}	
		}
	
		logger.info("\n contratos-->"+contratos);
		logger.info("\n numerocontratos-->"+numeroContratos);
		
		rips.setCodigosContrato(contratos);
		rips.setNumerosContrato(numeroContratos);
		rips.setValorCuenta(ripsForm.getValorCuenta());
	}


	/**
	 * Método que consulta el convenio y otra informacion dela cuenta de cobro
	 * @param con
	 * @param ripsForm
	 * @param response
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionBuscarConvenio(Connection con, RipsCapitacionForm ripsForm, HttpServletResponse response, UsuarioBasico usuario) 
	{
		//Se consultan los datos de la cuenta de cobro de capitacion
		boolean exito = false;
		HashMap resultado = Rips.consultarDatosCxCCapitacion(con, ripsForm.getNumeroCuentaCobro(), usuario.getCodigoInstitucionInt());
		
		//se verifican los resultados
		if(Integer.parseInt(resultado.get("numRegistros").toString())>0)
		{
			ripsForm.setCodigoConvenio(resultado.get("codigoConvenio").toString());
			ripsForm.setNombreConvenio(resultado.get("nombreConvenio").toString());
			ripsForm.setFechaInicial(resultado.get("fechaInicial").toString());
			ripsForm.setFechaFinal(resultado.get("fechaFinal").toString());
			ripsForm.setFechaElaboracion(resultado.get("fechaElaboracion").toString());
			ripsForm.setValorCuenta(UtilidadTexto.formatearValores(resultado.get("valorCuenta").toString(), "0.00"));
			ripsForm.setContratos(new HashMap((HashMap)resultado.get("contratos")));
			exito = true;
		}
		else
		{
			ripsForm.setCodigoConvenio("");
			ripsForm.setNombreConvenio("");
			ripsForm.setFechaInicial("");
			ripsForm.setFechaFinal("");
			ripsForm.setValorCuenta("");
			ripsForm.setFechaElaboracion("");
			ripsForm.setContratos(new HashMap());
		}
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			String respuesta = "<respuesta>";
			if(exito)
			{
				respuesta += "<codigo-convenio>"+ripsForm.getCodigoConvenio()+"</codigo-convenio>"+
				"<nombre-convenio>"+ripsForm.getNombreConvenio()+"</nombre-convenio>" +
				"<numero-contratos>"+ripsForm.getContratos("numRegistros")+"</numero-contratos>" +
				"<contratos>";
				for(int i=0;i<Integer.parseInt(ripsForm.getContratos("numRegistros").toString());i++)
					respuesta += "<contrato>"+ripsForm.getContratos("codigo_"+i)+ConstantesBD.separadorSplit+ripsForm.getContratos("numero_"+i)+"</contrato>";
				respuesta +="</contratos>";
			}
			else
			{
				respuesta += "<codigo-convenio>0</codigo-convenio>"+
				"<nombre-convenio>VACIO</nombre-convenio>" +
				"<numero-contratos>0</numero-contratos>";
			}
			respuesta += "</respuesta>";
	        response.getWriter().write(respuesta);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionBuscarConvenio: "+e);
		}
		return null;
	}


	/**
	 * Método implementado para iniciar el flujo de RIPS
	 * @param con
	 * @param ripsForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, RipsCapitacionForm ripsForm, ActionMapping mapping) 
	{
		ripsForm.reset();
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
}
