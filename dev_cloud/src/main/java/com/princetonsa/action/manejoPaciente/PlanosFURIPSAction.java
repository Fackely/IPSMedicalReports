package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;

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

import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.action.seccionesparametrizables.AccesosVascularesHAAction;
import com.princetonsa.actionform.manejoPaciente.PlanosFURIPSForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.PlanosFURIPS;

/**
 * 
 * @author wilson
 *
 */
public class PlanosFURIPSAction extends Action 
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(AccesosVascularesHAAction.class);
    
    /**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con=null;
    	try{
    		if (response==null); 
    		if(form instanceof PlanosFURIPSForm)
    		{

    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    			PlanosFURIPSForm forma =(PlanosFURIPSForm)form;
    			String estado=forma.getEstado();

    			logger.info("\n\n\n************************************************************");
    			logger.warn("El estado en PLANOS FURIPS es------->"+estado);
    			logger.info("************************************************************\n\n\n");

    			//*******VALIDACION DEL CAMPO CONVENIO FISALUD***********************************
    			//Se verifica que se haya parametrizado el campo de convenio FISALUD
    			if( UtilidadTexto.isEmpty(ValoresPorDefecto.getConvenioFisalud(usuario.getCodigoInstitucionInt()))
    					|| Utilidades.convertirAEntero(ValoresPorDefecto.getConvenioFisalud(usuario.getCodigoInstitucionInt()))<=0)
    			{		
    				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "FALTA PARAMETRO GENERAL", "error.forecat.parametroGeneral", true);
    			}	
    			//*******VALIDACION DEL PATH DE LOS ARCHIVOS***********************************
    			//Se verifica que se haya parametrizado el campo de convenio FISALUD
    			if( UtilidadTexto.isEmpty(ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFurips(usuario.getCodigoInstitucionInt())))
    			{		
    				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "FALTA PARAMETRO GENERAL", "error.manejoPaciente.rutaFurips", true);
    			}	
    			//***************************************************************


    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de PLANOS FURIPS (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar"))
    			{
    				return this.accionEmpezar(forma,mapping, con, usuario, institucionBasica);
    			}
    			else if(estado.equals("generarArchivos") || estado.equals("sobrescribirArchivos"))
    			{
    				return this.accionGenerarArchivos(forma,mapping, con, usuario, institucionBasica, request);
    			}
    			else if(estado.equals("continuar") || estado.equals("resumen"))
    			{
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("planosFURIPS");
    			}
    			else if(estado.equals("mostrarArchivoGenerado"))
    			{
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("mostrarArchivoGenerado");
    			}
    			else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de PLANOS FURIPS");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
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
     * 
     * @param forma
     * @param request
     * @param mapping
     * @param con
     * @param usuario
     */
	private ActionForward accionEmpezar(	PlanosFURIPSForm forma,
											ActionMapping mapping, 
											Connection con,
											UsuarioBasico usuario,
											InstitucionBasica institucionBasica) 
	{
		forma.reset();
		llenarForm(forma, con, usuario, institucionBasica);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("planosFURIPS");
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 */
	public void llenarForm( PlanosFURIPSForm forma, Connection con, UsuarioBasico usuario, InstitucionBasica institucionBasica)
	{
		forma.setTarifariosOficiales(Utilidades.obtenerTarifariosOficiales(con, ""));
		forma.resetearNombreArchivos(usuario, institucionBasica);
	}
	
	/**
	 * 
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGenerarArchivos(PlanosFURIPSForm forma,
												ActionMapping mapping, Connection con,
												UsuarioBasico usuario,
												InstitucionBasica institucionBasica,
												HttpServletRequest request) 
	{
		PlanosFURIPS mundo= new PlanosFURIPS();
		boolean existeRegistros=false;
		
		//FURIPS1
		if(UtilidadTexto.getBoolean(forma.getMapaBusqueda(PlanosFURIPS.archivosEnum.Furips1+"")+""))
    	{
			forma.setRutasArchivos(mundo.consultaFURIPS1(con, forma.getMapaBusqueda(), institucionBasica, usuario.getCodigoInstitucionInt(), forma.getRutasArchivos(),""));
			
			if(!forma.getRutasArchivos().isProcesoExitoso())
			{
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("error.file.noGenerado", "FURIPS1"));
				logger.warn("NO CREO EL ARCHIVO FURIPS1");
				saveErrors(request, errores);	
				UtilidadBD.closeConnection(con);									
				return mapping.findForward("planosFURIPS");
			}
			if(!UtilidadTexto.isEmpty(forma.getRutasArchivos().getStringBufferFURIPS1().toString().trim()) || !UtilidadTexto.isEmpty(forma.getRutasArchivos().getStringBufferInconsistenciasFURIPS1().toString().trim()))
			{
				existeRegistros=true;
			}
		}
		
		//FURIPS2
		if(UtilidadTexto.getBoolean(forma.getMapaBusqueda(PlanosFURIPS.archivosEnum.Furips2+"")+""))
    	{
			forma.setRutasArchivos(mundo.consultaFURIPS2(con, forma.getMapaBusqueda(), institucionBasica, usuario.getCodigoInstitucionInt(), forma.getRutasArchivos(),""));
			
			if(!forma.getRutasArchivos().isProcesoExitoso())
			{
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("error.file.noGenerado", "FURIPS2"));
				logger.warn("NO CREO EL ARCHIVO FURIPS2");
				saveErrors(request, errores);	
				UtilidadBD.closeConnection(con);									
				return mapping.findForward("planosFURIPS");
			}
			if(!UtilidadTexto.isEmpty(forma.getRutasArchivos().getStringBufferFURIPS2().toString().trim()) || !UtilidadTexto.isEmpty(forma.getRutasArchivos().getStringBufferInconsistenciasFURIPS2().toString().trim()))
			{
				existeRegistros=true;
			}
		}
		
		//FURPRO
		if(UtilidadTexto.getBoolean(forma.getMapaBusqueda(PlanosFURIPS.archivosEnum.Furpro1+"")+""))
    	{
			forma.setRutasArchivos(mundo.consultaFURPRO(con, forma.getMapaBusqueda(), institucionBasica, usuario.getCodigoInstitucionInt(), forma.getRutasArchivos(),""));
			
			if(!forma.getRutasArchivos().isProcesoExitoso())
			{
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("error.file.noGenerado", "FURPRO"));
				logger.warn("NO CREO EL ARCHIVO FURPRO");
				saveErrors(request, errores);	
				UtilidadBD.closeConnection(con);									
				return mapping.findForward("planosFURIPS");
			}
			if(!UtilidadTexto.isEmpty(forma.getRutasArchivos().getStringBufferFURPRO().toString().trim()) || !UtilidadTexto.isEmpty(forma.getRutasArchivos().getStringBufferInconsistenciasFURPRO().toString().trim()))
			{
				existeRegistros=true;
			}
		}
		
		
		if(!existeRegistros)
		{
			ActionErrors errores = new ActionErrors();
			forma.setEstado("continuar");
			errores.add("", new ActionMessage("errors.noExiste2", "facturas para el periodo seleccionado. Por favor verifique"));
			logger.warn("NO existen facturas");
			saveErrors(request, errores);	
			UtilidadBD.closeConnection(con);									
			return mapping.findForward("planosFURIPS");
		}
		
		//generamos el zip
		forma.setRutasArchivos(mundo.generarArchivoZip(forma.getRutasArchivos(), forma.getMapaBusqueda()));
		
		//finalmente insertamos el log de bd
		UtilidadBD.iniciarTransaccion(con);
		if(!mundo.insertarLogBD(con, forma.getRutasArchivos(), forma.getMapaBusqueda(), usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario()))
		{
			UtilidadBD.abortarTransaccion(con);
		}
		UtilidadBD.finalizarTransaccion(con);
		
		forma.setEstado("resumen");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("planosFURIPS");
	}

}
