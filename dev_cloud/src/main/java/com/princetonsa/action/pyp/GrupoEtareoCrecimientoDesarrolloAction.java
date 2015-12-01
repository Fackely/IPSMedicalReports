/*
 * @(#)GruposEtareosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.pyp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.pyp.GrupoEtareoCrecimientoDesarrolloForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pyp.GrupoEtareoCrecimientoDesarrollo;

/**
 * Clase encargada del control de la funcionalidad de Grupo Etareo
 * de Crecimiento y Desarrollo

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 28 /Jul/ 2006
 */
public class GrupoEtareoCrecimientoDesarrolloAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(GrupoEtareoCrecimientoDesarrolloAction.class);
	
	int maxPageItems = 0;
	
	/**
	 * manejo de errores
	 */
	final int ningunError = 1, errorCodigo = 4;
	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{

		Connection con = null;
		try{

			if(form instanceof GrupoEtareoCrecimientoDesarrolloForm)
			{


				/**Intentamos abrir una conexion con la fuente de datos**/ 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				HttpSession session = request.getSession();
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");

				maxPageItems = Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt()));
				GrupoEtareoCrecimientoDesarrollo mundo = new GrupoEtareoCrecimientoDesarrollo();
				GrupoEtareoCrecimientoDesarrolloForm forma = (GrupoEtareoCrecimientoDesarrolloForm)form;

				String estado = forma.getEstado();
				logger.warn("[GrupoEtareoCrecimientoDesarrolloAction] estado->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de GrupoEtareoCrecimientoDesarrolloAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, mapping, con, mundo, usuario);
				}
				else if(estado.equals("eliminarDelMapa"))
				{
					return this.accionEliminarDelMapa(forma, mapping, con, response);
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(forma, mapping, request, usuario,con, mundo);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("nuevo"))
				{
					return this.accionAgregarNuevo(forma, response, request, con, usuario);
				}
				else if (estado.equals("continuar"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");	
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de Grupos Etareos");
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
	 * Action que me resulta de la busuqeda de grupos etareos de crecimiento y desarrollo
	 * En caso de que la consulta no arroje ningun resultdo llenamos el mapa con un registro
	 * para poder ingresar nuevos datos
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(GrupoEtareoCrecimientoDesarrolloForm forma, ActionMapping mapping, Connection con, GrupoEtareoCrecimientoDesarrollo mundo, UsuarioBasico usuario) throws SQLException
	{
		forma.reset();
		forma.setMapaGruposEtareos(mundo.consultarGruposEtareos(con, usuario.getCodigoInstitucionInt()));
		forma.setMapaGruposEtareosNoModificado(mundo.consultarGruposEtareos(con, usuario.getCodigoInstitucionInt()));
		
		if(Integer.parseInt(forma.getMapaGruposEtareos("numRegistros")+"") <= 0)
		{
			forma.setMapaGruposEtareos("numRegistros", 1);
			forma.getMapaGruposEtareos().put("codigo_0", "");
			forma.getMapaGruposEtareosNoModificado().put("codigointerno_0", "0");
			forma.getMapaGruposEtareos().put("descripcion_0", "");
			forma.getMapaGruposEtareos().put("codigounidadmedida_0", "-1");
			forma.getMapaGruposEtareos().put("rangofinal_0", "");
			forma.getMapaGruposEtareos().put("rangoinicial_0", "");
			forma.getMapaGruposEtareos().put("codigosexo_0", "-1");
			forma.getMapaGruposEtareos().put("activo_0", "true");
			forma.getMapaGruposEtareos().put("institucion_0", usuario.getCodigoInstitucionInt());
			forma.getMapaGruposEtareos().put("existebd_0", "no");
			forma.getMapaGruposEtareos().put("eseliminado_0", "false");
  		}
		if(Integer.parseInt(forma.getMapaGruposEtareosNoModificado("numRegistros")+"") <= 0)
		{
			forma.setMapaGruposEtareosNoModificado("numRegistros", 1);
			forma.getMapaGruposEtareosNoModificado().put("codigo_0", "");
			forma.getMapaGruposEtareosNoModificado().put("codigointerno_0", "0");
			forma.getMapaGruposEtareosNoModificado().put("descripcion_0", "");
			forma.getMapaGruposEtareosNoModificado().put("codigounidadmedida_0", "-1");
			forma.getMapaGruposEtareosNoModificado().put("rangofinal_0", "");
			forma.getMapaGruposEtareosNoModificado().put("rangoinicial_0", "");
			forma.getMapaGruposEtareosNoModificado().put("codigosexo_0", "-1");
			forma.getMapaGruposEtareosNoModificado().put("activo_0", "true");
			forma.getMapaGruposEtareosNoModificado().put("institucion_0", usuario.getCodigoInstitucionInt());
			forma.getMapaGruposEtareosNoModificado().put("existebd_0", "no");
			forma.getMapaGruposEtareosNoModificado().put("eseliminado_0", "false");
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	
	/**
	 * Action para eliminar un elemento delmapa
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEliminarDelMapa(GrupoEtareoCrecimientoDesarrolloForm forma, ActionMapping mapping, Connection con, HttpServletResponse response) throws Exception
	{
		forma.setMapaGruposEtareos("eseliminado_"+forma.getPosicionMapa(),"true");
		UtilidadBD.cerrarConexion(con);
		
		if(!forma.getLinkSiguiente().equals(""))
		{
			response.sendRedirect(forma.getLinkSiguiente());
		}
		else
		{
			return mapping.findForward("paginaPrincipal");
		}
		return null;
	}
	
	
	
	/**
	 * Action para agregar un nuevo registro
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionAgregarNuevo(GrupoEtareoCrecimientoDesarrolloForm forma, HttpServletResponse response, HttpServletRequest request, Connection con, UsuarioBasico usuario) throws SQLException
	{
		forma.setResultado(new ResultadoBoolean(false));
		forma.setMensaje("");
		int index = Integer.parseInt(forma.getMapaGruposEtareos("numRegistros")+"");
		forma.getMapaGruposEtareos().put("codigo_"+index, "");
		forma.getMapaGruposEtareos().put("codigointerno_"+index, "0");
		forma.getMapaGruposEtareos().put("descripcion_"+index, "");
		forma.getMapaGruposEtareos().put("codigounidadmedida_"+index, "-1");
		forma.getMapaGruposEtareos().put("rangofinal_"+index, "");
		forma.getMapaGruposEtareos().put("rangoinicial_"+index, "");
		forma.getMapaGruposEtareos().put("codigosexo_"+index, "-1");
		forma.getMapaGruposEtareos().put("activo_"+index, "true");
		forma.getMapaGruposEtareos().put("institucion_"+index, usuario.getCodigoInstitucionInt());
		forma.getMapaGruposEtareos().put("existebd_"+index, "no");
		forma.getMapaGruposEtareos().put("eseliminado_"+index, "false");
		index = index+1;
		forma.setMapaGruposEtareos("numRegistros", index+"");
		
				
		UtilidadBD.closeConnection(con);
		return this.redireccionColumnaNuevo(con, forma, response, request, "ingresarModificarGrupoEtareoCrecimientoDesarrollo.do?estado=continuar");
	}
	
	/**
	 * Action para redireccionar y que me deje en la pagina del pager correspondiente
	 * @param con
	 * @param forma
	 * @param response
	 * @param request
	 * @param enlace
	 * @return
	 */
	public ActionForward redireccionColumnaNuevo(Connection con, GrupoEtareoCrecimientoDesarrolloForm forma, HttpServletResponse response, HttpServletRequest request, String enlace)
	{
		int numRegistros = Integer.parseInt(forma.getMapaGruposEtareos("numRegistros").toString());
		forma.setOffset(((int)((numRegistros-1)/maxPageItems))*maxPageItems);
		if(request.getParameter("ultimaPage") == null)
		{
			if(numRegistros > (forma.getOffset()+maxPageItems))
				forma.setOffset(((int)(numRegistros)/maxPageItems)*maxPageItems);
			
			try 
			{
				response.sendRedirect(enlace+"&pager.offset="+forma.getOffset());
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{    
			String ultimaPagina = request.getParameter("ultimaPage");
			String tempOffset = "offset=";
			int posOffSet = ultimaPagina.indexOf(tempOffset)+tempOffset.length();
			if(numRegistros > (forma.getOffset()+maxPageItems))
				forma.setOffset(forma.getOffset()+maxPageItems);
			try 
			{
				response.sendRedirect(ultimaPagina.substring(0,posOffSet)+forma.getOffset());
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		try 
		{
			UtilidadBD.cerrarConexion(con);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
    }
	
	
	/**
	 * Action para guardar los datos que correspondan en la base de datos
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param con
	 * @param mundo
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardar (GrupoEtareoCrecimientoDesarrolloForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, Connection con, GrupoEtareoCrecimientoDesarrollo mundo) throws SQLException
	{
		int validarActualizacionTransaccional = actualizarGuardarNuevosEliminarViejosBDTransaccional(forma, usuario, con, mundo);
		if(validarActualizacionTransaccional == errorCodigo)
	    {
	        ActionErrors errores = new ActionErrors();
			errores.add("actualización Grupos Etarios de Crecimiento y Desarrollo", new ActionMessage("errors.ingresoDatos","los datos de Grupos Etarios de Crecimiento y Desarrollo (Transacción)"));
			logger.warn("error en la actualización de los datos de Grupos Etareos de Crecimiento y Desarrollo");
			saveErrors(request, errores);	
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("paginaPrincipal");
	    }
		else
		{
			forma.setResultado(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
		}
		//Volvemos a la pagina principal
		return this.accionResumen(forma, mapping, con, "empezar", usuario, mundo);
	}

	
	private ActionForward accionResumen (GrupoEtareoCrecimientoDesarrolloForm forma, ActionMapping mapping, Connection con, String estado, UsuarioBasico usuario, GrupoEtareoCrecimientoDesarrollo mundo) throws SQLException 
	{
		forma.setEstado(estado);
		forma.setMapaGruposEtareos(mundo.consultarGruposEtareos(con, usuario.getCodigoInstitucionInt()));
		forma.setMapaGruposEtareosNoModificado(mundo.consultarGruposEtareos(con, usuario.getCodigoInstitucionInt()));
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * Método que genera los Logs de Modificación y borrado 
	 * @param forma
	 * @param indexKeyCodigoMapaMod, indice de la llave.
	 * @param usuario, user
	 */
	private void generarLog(GrupoEtareoCrecimientoDesarrolloForm forma, int indexKeyCodigoMapaMod, UsuarioBasico usuario, boolean esModificacion)
	{
	    String log;
	        		    
		log="\n           ======INFORMACION ORIGINAL GRUPOS ESTAREOS DE CRECIMIENTO Y DESARROLLO ===== " +
		"\n*  Código [" +forma.getMapaGruposEtareosNoModificado("codigo_"+indexKeyCodigoMapaMod) +"] "+
		"\n*  Descripcion ["+forma.getMapaGruposEtareosNoModificado("descripcion_"+indexKeyCodigoMapaMod)+"] "+
		"\n*  Unidad de Medida [" +forma.getMapaGruposEtareosNoModificado("nombreunidadmedida_"+indexKeyCodigoMapaMod) +"] "+
		"\n*  Rango Inicial [" +forma.getMapaGruposEtareosNoModificado("rangoinicial_"+indexKeyCodigoMapaMod) +"] "+
		"\n*  Rango Final [" +forma.getMapaGruposEtareosNoModificado("rangofinal_"+indexKeyCodigoMapaMod) +"] "+
		"\n*  Sexo [" +forma.getMapaGruposEtareosNoModificado("nombresexo_"+indexKeyCodigoMapaMod) +"] "+
		"\n*  Activo [" +forma.getMapaGruposEtareosNoModificado("activo_"+indexKeyCodigoMapaMod) +"] ";
		
	    
		log+="\n========================================================\n\n\n " ;
		
		if(esModificacion)
	    {   	
			log="\n           ======INFORMACION DESPUES DE LA MODIFICAION DE GRUPOS ESTAREOS DE CRECIMIENTO Y DESARROLLO ===== " +
			"\n*  Código [" +forma.getMapaGruposEtareosAux("codigo_"+indexKeyCodigoMapaMod) +"] "+
			"\n*  Descripcion ["+forma.getMapaGruposEtareosAux("descripcion_"+indexKeyCodigoMapaMod)+"] "+
			"\n*  Unidad de Medida [" +forma.getMapaGruposEtareosAux("nombreunidadmedida_"+indexKeyCodigoMapaMod) +"] "+
			"\n*  Rango Inicial [" +forma.getMapaGruposEtareosAux("rangoinicial_"+indexKeyCodigoMapaMod) +"] "+
			"\n*  Rango Final [" +forma.getMapaGruposEtareosAux("rangofinal_"+indexKeyCodigoMapaMod) +"] "+
			"\n*  Sexo [" +forma.getMapaGruposEtareosAux("nombresexo_"+indexKeyCodigoMapaMod) +"] "+
			"\n*  Activo [" +forma.getMapaGruposEtareosAux("activo_"+indexKeyCodigoMapaMod) +"] ";
			
			log+="\n========================================================\n\n\n " ;	
			
			LogsAxioma.enviarLog(ConstantesBD.logGruposEtareosCredDesaCodigo, log, ConstantesBD.tipoRegistroLogModificacion,usuario.getInformacionGeneralPersonalSalud()); 
	    }
	    else
	    {
	        log+="\n========================================> FUE ELIMINADO\n\n\n " ;	
			LogsAxioma.enviarLog(ConstantesBD.logGruposEtareosCredDesaCodigo, log, ConstantesBD.tipoRegistroLogEliminacion,usuario.getInformacionGeneralPersonalSalud());
	    }
	}
	
	
	
	/**
	 * Abrir la conceccion con la Base de Datos
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con)
	{
		if(con != null)
		{
			return con;
		}
		try
		{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
		}
		catch(Exception e)
		{
			logger.warn("Problemas con la base de datos al abrir la conexion "+e.toString());
			return null;
		}
	
		return con;
	}
	
	
	/**
	 * Método para actualizar - guardar los datos de los Grupos Etareos de Crecimiento y Desarrollo
	 * @param forma
	 * @param usuario
	 * @param con
	 * @param mundo
	 * @return
	 * @throws SQLException
	 */
	private int actualizarGuardarNuevosEliminarViejosBDTransaccional(GrupoEtareoCrecimientoDesarrolloForm forma, UsuarioBasico usuario, Connection con, GrupoEtareoCrecimientoDesarrollo mundo) throws SQLException
	{
		//mundo.setCodigoPool(forma.getCodigoPool());
		boolean yaComenzoTransaccion = false;
		boolean insertados = false;
		String estadoTransaccionTemp = "";
		
		for(int k = 0 ; k < Integer.parseInt(forma.getMapaGruposEtareos("numRegistros")+"") ; k++)
		{
			/*PRIMERA PARTE PARA LA ELIMINACIÓN DE DATOS QUE ESTAN EN BD*/
			if(  (forma.getMapaGruposEtareos("eseliminado_"+k)+"").equals("true"))
			{
				if((forma.getMapaGruposEtareos("existebd_"+k)+"").equals("si"))
				{
					insertados = false;
		
					if(!yaComenzoTransaccion)
					{
						estadoTransaccionTemp =	ConstantesBD.inicioTransaccion;
					}
					else
					{
						estadoTransaccionTemp = ConstantesBD.continuarTransaccion;
					}
					
					insertados = mundo.eliminarGrupoEtareoTransaccional(con, estadoTransaccionTemp, Integer.parseInt(forma.getMapaGruposEtareos("codigointerno_"+k)+""));
					
					if(!insertados)
					{
						return errorCodigo;
					}
					
					if(!insertados)
					{
						logger.warn("error en la eliminacion de los datos de Grupos Etareos ");
						return errorCodigo;
					}    
					else
					{
						generarLog(forma, k, usuario, false);
						yaComenzoTransaccion = true;
					}
				}
			}
		}  
		
		/*SEGUNDA PARTE PARA LA MODIFICACION DE LOS DATOS QUE ESTAN EN BD*/
		//en este punto se carga el mapa solo con los valores que han sido modificados
		forma.comparar2HashMap();
		if(Integer.parseInt(forma.getMapaGruposEtareosAux("numRegistros")+"") > 0)
		{
			for(int k = 0 ; k < Integer.parseInt(forma.getMapaGruposEtareosNoModificado("numRegistros")+"") ; k++)
			{
				if(  (forma.getMapaGruposEtareos("eseliminado_"+k)+"").equals("false"))
				{
					if((forma.getMapaGruposEtareos("existebd_"+k)+"").equals("si"))
					{
						String tempoCod = forma.getMapaGruposEtareosAux("codigo_"+k)+"";
						if(tempoCod != null && !tempoCod.equals("") && !tempoCod.equals("null") && !tempoCod.equals("-1"))
						{
							insertados=false;
		
							if(!yaComenzoTransaccion)
							{
								estadoTransaccionTemp =	ConstantesBD.inicioTransaccion;
							}
							else
							{
								estadoTransaccionTemp = ConstantesBD.continuarTransaccion;
							}
		
							insertados = mundo.modificarGrupoEtareoTransaccional(con, estadoTransaccionTemp, Integer.parseInt(forma.getMapaGruposEtareosAux("codigointerno_"+k)+""), Integer.parseInt(forma.getMapaGruposEtareosAux("codigo_"+k)+""), forma.getMapaGruposEtareosAux("descripcion_"+k)+"",Integer.parseInt(forma.getMapaGruposEtareosAux("codigounidadmedida_"+k)+"") ,Integer.parseInt(forma.getMapaGruposEtareosAux("rangoinicial_"+k)+""), Integer.parseInt(forma.getMapaGruposEtareosAux("rangofinal_"+k)+""), Integer.parseInt(forma.getMapaGruposEtareosAux("codigosexo_"+k)+""), forma.getMapaGruposEtareosAux("activo_"+k)+"");
							
							if(!insertados)
							{
								logger.warn("error en la modificacion de los datos de Grupos Etareos de Crecimiento y Desarrollo");
								return errorCodigo;
							}    
							else
							{
								insertados = mundo.modificarGrupoEtareoTransaccional(con, ConstantesBD.continuarTransaccion, Integer.parseInt(forma.getMapaGruposEtareosAux("codigointerno_"+k)+""), Integer.parseInt(forma.getMapaGruposEtareosAux("codigo_"+k)+""), forma.getMapaGruposEtareosAux("descripcion_"+k)+"",Integer.parseInt(forma.getMapaGruposEtareosAux("codigounidadmedida_"+k)+"") ,Integer.parseInt(forma.getMapaGruposEtareosAux("rangoinicial_"+k)+""), Integer.parseInt(forma.getMapaGruposEtareosAux("rangofinal_"+k)+""), Integer.parseInt(forma.getMapaGruposEtareosAux("codigosexo_"+k)+""), forma.getMapaGruposEtareosAux("activo_"+k)+"");
		
								if(!insertados)
								{
									logger.warn("error en la modificacion de los datos de Grupos Etareos de Crecimiento y Desarrollo");
									return errorCodigo;
								}    
								else
									generarLog(forma, k, usuario, true);
								yaComenzoTransaccion=true;
							}
						}
					}  
				}
			}
		}    
		
		for(int k = 0 ; k < Integer.parseInt(forma.getMapaGruposEtareos("numRegistros")+"") ; k++)
		{      
			/*TERCERA PARTE PARA LA INSERCIÓN DE LOS NUEVOS DATOS*/
			if((forma.getMapaGruposEtareos("eseliminado_"+k)+"").equals("false"))
			{
				if((forma.getMapaGruposEtareos("existebd_"+k)+"").equals("no"))
				{
					insertados = false;
		
					if(!yaComenzoTransaccion)
					{
						estadoTransaccionTemp =	ConstantesBD.inicioTransaccion;
					}
					else
					{
						estadoTransaccionTemp = ConstantesBD.continuarTransaccion;
					}
		
					int codigoPK = mundo.insertarGrupoEtareoTransaccional(con, estadoTransaccionTemp, Integer.parseInt(forma.getMapaGruposEtareos("codigo_"+k)+""), forma.getMapaGruposEtareos("descripcion_"+k)+"",Integer.parseInt(forma.getMapaGruposEtareos("codigounidadmedida_"+k)+"") ,Integer.parseInt(forma.getMapaGruposEtareos("rangoinicial_"+k)+""), Integer.parseInt(forma.getMapaGruposEtareos("rangofinal_"+k)+""), Integer.parseInt(forma.getMapaGruposEtareos("codigosexo_"+k)+""), forma.getMapaGruposEtareos("activo_"+k)+"", usuario.getCodigoInstitucionInt());
					if(codigoPK <= 0)
					{
						logger.warn("error en la insercion de los datos de Grupos Etareos de Crecimiento y Desarrollo ");
						return errorCodigo;
					}     
					yaComenzoTransaccion = true;    
				}
			}
		}
		// SI LA TRANSACCIÓN YA FUÉ INICIADA ENTONCES QUE LA FINALICE
		if(yaComenzoTransaccion)
			UtilidadBD.finalizarTransaccion(con);
		return ningunError;
	}
	
	
}