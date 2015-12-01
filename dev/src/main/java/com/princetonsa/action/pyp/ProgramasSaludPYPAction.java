/*
 * @author armando
 */
package com.princetonsa.action.pyp;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.pyp.ProgramasSaludPYPForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pyp.ProgramasSaludPYP;

/**
 * 
 * @author armando
 *
 */
public class ProgramasSaludPYPAction extends Action 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ProgramasSaludPYPAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {
		Connection con = null;
		try{

			if(form instanceof ProgramasSaludPYPForm)
			{

				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion(); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ProgramasSaludPYPForm forma=(ProgramasSaludPYPForm)form;
				ProgramasSaludPYP mundo=new ProgramasSaludPYP();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				String estado = forma.getEstado();
				logger.warn("estado [ProgramasSaludPYPAction.java]-->"+estado);
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				else if (estado.equals("empezar"))
				{
					forma.reset();
					this.accionCargarMapaBaseDatos(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("nuevo"))
				{
					forma.reset();
					forma.setActivo(true);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaIngresarNuevo");
				}
				else if(estado.equals("detalle"))
				{
					this.cargarRegistroParaDetalle(forma);
					mundo.cargarTipoPrograma(con,forma.getCodigo(),forma.getInstitucion());

					forma.setDiagnosticos(mundo.getDiagnosticos());
					forma.setPuedoEliminar(mundo.puedoEliminarPrograma(con, forma.getCodigo(), forma.getInstitucion()));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaIngresarNuevo");
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("modificar"))
				{
					this.modificarRegistro(con,forma,mundo,usuario);
					forma.reset();
					mundo.reset();
					this.accionCargarMapaBaseDatos(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("eliminar"))
				{
					this.eliminarRegistro(con,forma,mundo,usuario);
					forma.reset();
					mundo.reset();
					this.accionCargarMapaBaseDatos(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("guardarNuevo"))
				{
					this.accionGuardarRegistro(con,forma,mundo,usuario);
					forma.reset();
					mundo.reset();
					this.accionCargarMapaBaseDatos(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("redireccion"))
				{
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("eliminarDiagnostico"))
				{
					this.accionEliminarDiagnostico(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaIngresarNuevo");
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			logger.error("El form no es compatible con el form de ProgramasSaludPYPForm");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
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
	 */
	private void accionEliminarDiagnostico(ProgramasSaludPYPForm forma) 
	{
		forma.setDiagnosticos("eliminado_"+forma.getDiagEliminar(),"true");
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 */
	private void modificarRegistro(Connection con, ProgramasSaludPYPForm forma, ProgramasSaludPYP mundo, UsuarioBasico usuario) 
	{
		if(!forma.getArchivo().getFileName().trim().equals(""))
		{
			forma.setDescArchivo(forma.getArchivo().getFileName());
			UtilidadFileUpload.guadarArchivo((FormFile)forma.getArchivo());
		}
		this.generarLog(forma,mundo,usuario,false);
		this.cargarMundo(forma,mundo);
		mundo.modificarRegistro(con);
	}


	/**
	 * 
	 * @param forma
	 * @param mundo 
	 */
	private void cargarRegistroParaDetalle(ProgramasSaludPYPForm forma) 
	{
		forma.setInstitucion(Integer.parseInt(forma.getProgramas("institucion_"+forma.getIndex())+""));
		forma.setCodigo(forma.getProgramas("codigo_"+forma.getIndex())+"");
		forma.setDescripcion(forma.getProgramas("descripcion_"+forma.getIndex())+"");
		forma.setTipoPrograma(forma.getProgramas("tipoprograma_"+forma.getIndex())+"");
		forma.setGrupoEtareo(forma.getProgramas("grupoetareo_"+forma.getIndex())+"");
		forma.setEmbarazo(UtilidadTexto.getBoolean(forma.getProgramas("embarazo_"+forma.getIndex())+""));
		forma.setFormato(forma.getProgramas("formato_"+forma.getIndex())+"");
		forma.setDescArchivo((forma.getProgramas("archivo_"+forma.getIndex())+"").equals("null")?"":forma.getProgramas("archivo_"+forma.getIndex())+"");
		forma.setActivo(UtilidadTexto.getBoolean(forma.getProgramas("activo_"+forma.getIndex())+""));
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarRegistro(Connection con, ProgramasSaludPYPForm forma, ProgramasSaludPYP mundo, UsuarioBasico usuario)
	{
		forma.setInstitucion(usuario.getCodigoInstitucionInt());
		forma.setDescArchivo(forma.getArchivo().getFileName());
		this.cargarMundo(forma,mundo);
		mundo.insertarRegistro(con);
		if(!forma.getArchivo().getFileName().trim().equals(""))
			UtilidadFileUpload.guadarArchivo((FormFile)forma.getArchivo());
	}

	/**
	 * 
	 * @param forma
	 * @param mundo
	 */
	private void cargarMundo(ProgramasSaludPYPForm forma, ProgramasSaludPYP mundo) 
	{
		mundo.reset();
		mundo.setCodigo(forma.getCodigo());
		mundo.setDescripcion(forma.getDescripcion());
		mundo.setTipoPrograma(forma.getTipoPrograma());
		mundo.setGrupoEtareo(forma.getGrupoEtareo());
		mundo.setEmbarazo(forma.isEmbarazo());
		mundo.setFormato(forma.getFormato());
		mundo.setArchivo(forma.getDescArchivo());
		mundo.setActivo(forma.isActivo());
		mundo.setInstitucion(forma.getInstitucion());
		mundo.setDiagnosticos(forma.getDiagnosticos());
	}


	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param b
	 * @param el
	 */
	private void generarLog(ProgramasSaludPYPForm forma, ProgramasSaludPYP mundo, UsuarioBasico usuario,  boolean isEliminacion) 
	{
		String log = "";
		int tipoLog=0;
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== "+
			 "\n*  Código [" +forma.getCodigo()+"] "+
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n*  Descripción ["+forma.getDescripcion()+"] " +
			 "\n*  Tipo Programa ["+forma.getTipoPrograma()+"] " +
			 "\n*  Grupo Etareo ["+forma.getGrupoEtareo()+"] " +
			 "\n*  Embarazo ["+forma.isActivo()+"] " +
			 "\n*  Formato ["+forma.getFormato()+"] " +
			 "\n*  Archivo ["+forma.getDescArchivo()+"] " +
			 "\n*  Activo ["+forma.isActivo()+"] " +
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
			 "\n*  Código [" +mundo.getCodigo()+""+"] "+
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n*  Descripción ["+mundo.getDescripcion()+"] " +
			 "\n*  Tipo Programa ["+mundo.getTipoPrograma()+"] " +
			 "\n*  Grupo Etareo ["+mundo.getGrupoEtareo()+"] " +
			 "\n*  Embarazo ["+mundo.isEmbarazo()+"] " +
			 "\n*  Formato ["+mundo.getFormato()+"] " +
			 "\n*  Archivo ["+mundo.getArchivo()+"] " +
			 "\n*  Activo ["+mundo.isActivo()+"] " +
			 "\n   ===========INFORMACION MODIFICADA========== 	" +
			 "\n*  Código [" +forma.getCodigo()+"] "+
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n*  Descripción ["+forma.getDescripcion()+"] " +
			 "\n*  Tipo Programa ["+forma.getTipoPrograma()+"] " +
			 "\n*  Grupo Etareo ["+forma.getGrupoEtareo()+"] " +
			 "\n*  Embarazo ["+forma.isActivo()+"] " +
			 "\n*  Formato ["+forma.getFormato()+"] " +
			 "\n*  Archivo ["+forma.getDescArchivo()+"] " +
			 "\n*  Activo ["+forma.isActivo()+"] " +
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logProgramaPYPCodigo,log,tipoLog,usuario.getLoginUsuario());
	}

	/**
	 * 
	 * @param con 
	 * @param forma
	 * @param mapping 
	 * @param request 
	 * @param response 
	 */
	private void eliminarRegistro(Connection con, ProgramasSaludPYPForm forma, ProgramasSaludPYP mundo, UsuarioBasico usuario) 
	{
		this.generarLog(forma,mundo,usuario,true);
		mundo.eliminarRegistro(con,forma.getCodigo(),usuario.getCodigoInstitucionInt());
	}
	
    
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(ProgramasSaludPYPForm forma)
	{
		int numReg=Integer.parseInt(forma.getProgramas("numRegistros")==null?"0":forma.getProgramas("numRegistros")+"");
		String[] indices={"codigo_","institucion_","descripcion_","tipoprograma_","desctipoprograma_","grupoetareo_","embarazo_","formato_","archivo_","activo_","tiporegistro_"};
		forma.setProgramas(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getProgramas(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setProgramas("numRegistros",numReg+"");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionCargarMapaBaseDatos(Connection con, ProgramasSaludPYPForm forma, ProgramasSaludPYP mundo, UsuarioBasico usuario) 
	{
		mundo.cargarInformacion(con,usuario.getCodigoInstitucionInt());
		forma.setProgramas((HashMap)mundo.getProgramas().clone());
	}
}
