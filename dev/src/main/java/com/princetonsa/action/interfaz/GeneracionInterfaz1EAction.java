package com.princetonsa.action.interfaz;
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

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.actionform.interfaz.GeneracionInterfaz1EForm;
import com.princetonsa.dto.interfaz.DtoInterfazS1EInfo;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.interfaz.GeneracionInterfaz1E;
import com.princetonsa.mundo.interfaz.ParamInterfazSistema1E;

/** * 
 * ** Nota: Se solicita para proximas modificaciones, guardar la estructura del codigo y comentar debidamente los cambios y nuevas lineas.
 */
public class GeneracionInterfaz1EAction extends Action
{
	Logger logger = Logger.getLogger(GeneracionInterfaz1EAction.class);
	
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

			if (form instanceof GeneracionInterfaz1EForm) 
			{			 

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//ActionErrors
				//ActionErrors errores = new ActionErrors();

				GeneracionInterfaz1EForm forma = (GeneracionInterfaz1EForm)form;		
				String estado = forma.getEstado();		 

				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());
				logger.info("-------------------------------------");
				logger.info("-------------------------------------");

				if(estado.equals("inicio"))
				{
					accionInicio(forma,con);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda"); 
				}
				else if(estado.equals("generarArchivo"))
				{
					String forward = accionValidarGenerarArchivo(forma,con,request);

					if(!forward.equals(""))
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward(forward);
					}

					forward = accionGenerarArchivo(forma, con, request,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward(forward);
				}
				else if(estado.equals("cargarArchivo"))
				{
					accionCargarArchivo(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenFile");
				}
				else if(estado.equals("volverBusqueda"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda");
				}
				else if(estado.equals("continuarGenerarArchivo"))
				{
					String forward = accionGenerarArchivo(forma,con,request,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward(forward);
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
	 * @param  GeneracionInterfaz1EForm forma
	 * @param Connection con
	 * */
	public void accionInicio(GeneracionInterfaz1EForm forma,Connection con)
	{
		forma.reset();
		forma.setDtoInfoInterfaz((DtoInterfazS1EInfo)ParamInterfazSistema1E.consultarParamGenerales(con,"S1EInfo"));
		forma.getDtoInfoInterfaz().setTipoMovimientos(GeneracionInterfaz1E.cargarTiposMovimientos());
	}
	
	/**
	 * @param GeneracionInterfaz1EForm forma
	 * @param Connection con
	 * @param HttpServletRequest request
	 * */
	public String accionValidarGenerarArchivo(GeneracionInterfaz1EForm forma,Connection con,HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();
		errores = GeneracionInterfaz1E.validarCamposGeneracionArchivo(forma.getDtoInfoInterfaz());
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			return "busqueda";
		}
		
		//Captura la información de los nombres de archivos
		forma.setDtoInfoInterfaz(GeneracionInterfaz1E.getNombreArchivos(forma.getDtoInfoInterfaz()));
		if(forma.getDtoInfoInterfaz().isPreguntarArchivosSobreEsc())
			return "sobreescribir";
		
		return "";
	}
	
	/**
	 * @param GeneracionInterfaz1EForm forma
	 * @param Connection con
	 * @param HttpServletRequest request
	 * */
	public String accionGenerarArchivo(GeneracionInterfaz1EForm forma,Connection con,HttpServletRequest request,UsuarioBasico usuario)
	{ 	
		//carga los tipos de documentos para los movimientos seleccionados
		forma.getDtoInfoInterfaz().setTipoDocXTipoMov(
				GeneracionInterfaz1E.consultarTipoDocXTipoMov1E(con,forma.getDtoInfoInterfaz().getTiposMovimientosXComa()));
		/*
		if(forma.getDtoInfoInterfaz().getTipoDocXTipoMov().size()<=0)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("descripcion",new ActionMessage("errors.notEspecific","No existe parametrizacion de Tipos Documento / Tipos Movimientos"));
			saveErrors(request, errores);
			return "busqueda";
		}*/
		
		GeneracionInterfaz1E mundo = new GeneracionInterfaz1E();
		forma.getDtoInfoInterfaz().setExisteInfoArchivos(false);
		forma.getDtoInfoInterfaz().setInstitucion(usuario.getCodigoInstitucion());
		
		InstitucionBasica institucion = new InstitucionBasica();		
		institucion.cargarGeneral(usuario.getCodigoInstitucionInt(),ConstantesBD.codigoNuncaValidoDouble);
		
		forma.getDtoInfoInterfaz().setInstitucionBasica(institucion);
		forma.setDtoInfoInterfaz(mundo.dep_generarArchivosInterfaz(con,forma.getDtoInfoInterfaz()));
		
		if(!forma.getDtoInfoInterfaz().isExisteInfoArchivos())
		{
			ActionErrors errores = new ActionErrors();
			errores.add("descripcion",new ActionMessage("errors.notEspecific","No existe Información de Movimientos a procesar en la Interfaz"));
			saveErrors(request, errores);
			return "busqueda";
		}
		else
		{
			if(!GeneracionInterfaz1E.guardarLogInterfaz1E(con,forma.getDtoInfoInterfaz(),usuario))
			{
				ActionErrors errores = new ActionErrors();
				errores.add("descripcion",new ActionMessage("errors.notEspecific","No Guardo Información del Log de Generación de Interfaz"));
				saveErrors(request, errores);
			}
		}
		
		return "resultado";
	}
	
	/**
	 * carga un archivo 
	 * @param GeneracionInterfaz1EForm forma
	 * */
	public void accionCargarArchivo(GeneracionInterfaz1EForm forma)
	{		
		forma.setContenidoArchivo(GeneracionInterfaz1E.cargarArchivo(forma.getRutaInd(),forma.getNombreInd()));
	}
 }