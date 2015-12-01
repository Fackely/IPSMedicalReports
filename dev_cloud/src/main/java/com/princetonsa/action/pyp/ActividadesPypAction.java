/*
 * Ago 02, 2006
 */
package com.princetonsa.action.pyp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

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
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.pyp.ActividadesPypForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pyp.ActividadesPyp;

/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Parametrización de Actividades de Promoción y Prevención
 */
public class ActividadesPypAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ActividadesPypAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{

		Connection con=null;
		try{

			if (response==null); //Para evitar que salga el warning
			if(form instanceof ActividadesPypForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				ActividadesPypForm actividadesForm =(ActividadesPypForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=actividadesForm.getEstado(); 
				logger.warn("\n\n En ActividadesPypAction el Estado ["+estado+"] \n\n");

				if(estado == null)
				{
					actividadesForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Actividades PYP (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				//**********ESTADOS FUNC. ACTIVIDADES PROMOCIÓN Y PREVENCIÓN *****************************************
				else if (estado.equals("empezar"))
				{
					//Se resetea forma
					actividadesForm.reset();
					actividadesForm.setEstado("empezar");
					this.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if (estado.equals("parametrizar"))
				{
					return accionParametrizar(con,actividadesForm,mapping,usuario);
				}
				//estado usado para ingresar una nueva actividad
				else if (estado.equals("nuevo"))
				{
					return accionNuevo(con,actividadesForm,mapping,usuario);
				}
				//estado usado para insertar/modificar actividades
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,actividadesForm,mapping,usuario,request);
				}
				//estado usado para la eliminacion de una actividad
				else if (estado.equals("eliminar"))
				{
					return accionEliminar(con,actividadesForm,mapping,usuario);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,actividadesForm,mapping);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,actividadesForm,response,mapping,request);
				}


				//--------------------------------------------------------------------------------
				//--------------------------------------------------------------------------------
				//-- Acciones de Actividades de Promoción y Prevención Por Centros De Atencion.  
				//--------------------------------------------------------------------------------
				//--------------------------------------------------------------------------------

				else if (estado.equals("empezarCA"))
				{
					return accionEmpezarCA(con,actividadesForm,mapping,usuario,request);
				}
				else if (estado.equals("insertarFilaCA"))  		//-- Para Insertar una Nueva Fila en el mapa.
				{
					if ( UtilidadCadena.noEsVacio(actividadesForm.getMapaCA("nroRegistrosNv")+"") )
					{
						actividadesForm.setMapaCA("nroRegistrosNv", Integer.parseInt(actividadesForm.getMapaCA("nroRegistrosNv")+"")+1 +"");
					}
					else
					{
						actividadesForm.setMapaCA("nroRegistrosNv", "1");
					}
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if (estado.equals("eliminarFilaCA"))  		  //-- Para Eliminar Una Fila de los nuevos registros del Mapa.
				{
					return eliminarFilaActividad(con, mapping, actividadesForm);
				}
				else if (estado.equals("guardarCA"))  	  		  //-- Para Insertar una Nueva Fila en el mapa.
				{
					return accionGuardarCA(con, mapping, actividadesForm, usuario, request);
				}
				else if (estado.equals("listarActividadesCA"))    //-- Para Listar las Actividades Especificas De Un Centro Atencion. 
				{
					return accionListarCA(con, false, actividadesForm,mapping,usuario,request);
				}
				else if (estado.equals("eliminarActividadCA"))    //-- Para eliminar una actividad de un centro de Atencion especifico. 
				{
					return eliminarActividadCA(con,actividadesForm,mapping,usuario,request);
				}
				else if (estado.equals("redireccionA"))  //--Estado para mantener los datos del pager
				{			    
					UtilidadBD.cerrarConexion(con);

					//-- Si se esta insertando un nuevo registro.	
					if (actividadesForm.getEnviarUltimaPagina())
					{
						if ( UtilidadCadena.noEsVacio(actividadesForm.getMapaCA("nroRegistrosNv")+"") )
						{
							actividadesForm.setMapaCA("nroRegistrosNv", Integer.parseInt(actividadesForm.getMapaCA("nroRegistrosNv")+"")+1 +"");
						}
						else
						{
							actividadesForm.setMapaCA("nroRegistrosNv", "1");
						}
					}
					actividadesForm.setEnviarUltimaPagina(false);	
					response.sendRedirect(actividadesForm.getLinkSiguiente());
					return null;
				}
				else
				{
					actividadesForm.reset();
					logger.warn("Estado no valido dentro del flujo de ActividadesPypAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
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
	 * Action para redireccionar y que me deje en la pagina del pager correspondiente
	 * @param con
	 * @param forma
	 * @param response
	 * @param request
	 * @param enlace
	 * @return
	 */
	/*public ActionForward redireccionColumnaNuevo(Connection con, ActividadesPypForm forma, HttpServletResponse response, HttpServletRequest request, String enlace)
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
    }*/

	
	
	/**
	 * Método implementado para paginar el listado de actividades
	 * @param con
	 * @param actividadesForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, ActividadesPypForm actividadesForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    this.cerrarConexion(con);
			response.sendRedirect(actividadesForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de ActividadesPypAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ActividadesPypAction", "errors.problemasDatos", true);
		}
	}


	/**
	 * Método implementado para ordenar el listado de actividades
	 * @param con
	 * @param actividadesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, ActividadesPypForm actividadesForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"consecutivo_",
				"codigo_",
				"descripcion_",
				"cod_cumplimiento_",
				"activo_",
				"institucion_",
				"es_usado_"
			};

		actividadesForm.setActividades(Listado.ordenarMapa(indices,
				actividadesForm.getIndice(),
				actividadesForm.getUltimoIndice(),
				actividadesForm.getActividades(),
				actividadesForm.getNumRegistros()));
		
		actividadesForm.setActividades("numRegistros",actividadesForm.getNumRegistros());
		
		actividadesForm.setUltimoIndice(actividadesForm.getIndice());
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para eliminar una actividad
	 * @param con
	 * @param actividadesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, ActividadesPypForm actividadesForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se instancia objeto de actividades
		ActividadesPyp actividades = new ActividadesPyp();
		
		//se toma posición del registro a eliminar
		int pos = actividadesForm.getPosicion();
		int resp = 1;
		String auxS0 = actividadesForm.getActividades("consecutivo_"+pos).toString();
		
		//se verifica si el registro existe en la BD
		if(!auxS0.equals("0"))
		{
			//El registro existe por tal motivo se debe eliminar
			this.llenarMundo(actividades,actividadesForm,pos);
			resp = actividades.eliminar(con);
			if(resp>0)
				this.generarLog(actividadesForm,pos,usuario);
		}
		
		//si el proceso es exitoso entonces se elimina del mapa y se envía el LOG
		if(resp>0)
		{
			//Se borra el registro del mapa
			for(int i=pos;i<(actividadesForm.getNumRegistros()-1);i++)
			{
				actividadesForm.setActividades("consecutivo_"+i,actividadesForm.getActividades("consecutivo_"+(i+1)));
				actividadesForm.setActividades("codigo_"+i,actividadesForm.getActividades("codigo_"+(i+1)));
				actividadesForm.setActividades("descripcion_"+i,actividadesForm.getActividades("descripcion_"+(i+1)));
				actividadesForm.setActividades("cod_cumplimiento_"+i,actividadesForm.getActividades("cod_cumplimiento_"+(i+1)));
				actividadesForm.setActividades("activo_"+i,actividadesForm.getActividades("activo_"+(i+1)));
				actividadesForm.setActividades("institucion_"+i,actividadesForm.getActividades("institucion_"+(i+1)));
				actividadesForm.setActividades("es_usado_"+i,actividadesForm.getActividades("es_usado_"+(i+1)));
			}
			
			//se elimina ultima posicion
			pos = actividadesForm.getNumRegistros();
			actividadesForm.getActividades().remove("consecutivo_"+pos);
			actividadesForm.getActividades().remove("codigo_"+pos);
			actividadesForm.getActividades().remove("descripcion_"+pos);
			actividadesForm.getActividades().remove("cod_cumplimiento_"+pos);
			actividadesForm.getActividades().remove("activo_"+pos);
			actividadesForm.getActividades().remove("institucion_"+pos);
			actividadesForm.getActividades().remove("es_usado_"+pos);
			
			pos--;
			actividadesForm.setNumRegistros(pos);
			actividadesForm.setActividades("numRegistros",pos);
			actividadesForm.setEstado("guardar");
		}
		else
		{
			ActionErrors errores = new ActionErrors();
			errores.add("Error al eliminar",new ActionMessage("error.pyp.actividadesPYP.error","al modificar",actividadesForm.getActividades("descripcion_"+pos)));
			actividadesForm.setEstado("parametrizar");
		}
		
		//se verifica el offset
		if(actividadesForm.getNumRegistros()==actividadesForm.getOffset()&&actividadesForm.getOffset()>0)
		{
			int offset = actividadesForm.getOffset();
			offset = offset - actividadesForm.getMaxPageItems();
			actividadesForm.setOffset(offset);
		}
		this.tomarCodigosExistentes(actividadesForm);
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para generar un LOG en la eliminacion de actividades
	 * @param forma
	 * @param pos
	 * @param usuario
	 */
	private void generarLog(ActividadesPypForm forma, int pos, UsuarioBasico usuario) 
	{
		String log="\n            ====INFORMACION ELIMINADA DE ACTIVIDADES PYP===== " +
			"\n*  Consecutivo [" +forma.getActividades("consecutivo_"+pos)+"] "+
			"\n*  "+(forma.getTipoActividad().equals(ConstantesBD.tipoActividadPYPArticulo)?"Artículo":"Servicio")+
				" ["+forma.getActividades("descripcion_"+pos)+"] " +
			"\n*  Código de Cumplimiento [" + forma.getActividades("cod_cumplimiento_"+pos) +"]"+
			"\n*  Estado [" + (UtilidadTexto.getBoolean(forma.getActividades("activo_"+pos).toString())?"Activo":"Inactivo") +"]"+
		    "\n*  Institucion ["+usuario.getInstitucion()+"] ";
		
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logActividadesPYPCodigo, log, ConstantesBD.tipoRegistroLogEliminacion,usuario.getLoginUsuario());		
	}

	/**
	 * Método implementado para insertar/modificar
	 * @param con
	 * @param actividadesForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, ActividadesPypForm actividadesForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//variables auxiliares
		String auxS0 = "";
		int resp = 0;
		ActionErrors errores = new ActionErrors();
		
		
		//Se instancia objeto de ActividadesPyp
		ActividadesPyp actividades = new ActividadesPyp();
		
		Utilidades.imprimirMapa(actividadesForm.getActividades());
		
		for(int i=0;i<actividadesForm.getNumRegistros();i++)
		{
			auxS0 = actividadesForm.getActividades("consecutivo_"+i).toString();
			
			//Se verifica estado del registro
			if(auxS0.equals("0"))
			{
				//******INSERCIÓN DE REGISTRO *********************************
				this.llenarMundo(actividades,actividadesForm,i);
				resp = actividades.insertar(con);
				
				if(resp<=0)
				{
					//Se verifica si la actividad ya fue insertada (CONCURRENCIA)
					actividades.clean();
					actividades.setCampos("institucion",usuario.getCodigoInstitucion());
					if(actividadesForm.getTipoActividad().equals(ConstantesBD.tipoActividadPYPServicio))
						actividades.setCampos("servicio",actividadesForm.getActividades("codigo_"+i));
					else
						actividades.setCampos("articulo",actividadesForm.getActividades("codigo_"+i));
					
					HashMap registro = actividades.consultar(con);
					
					if(Integer.parseInt(registro.get("numRegistros").toString())>0)
						errores.add("actividad ya existe",new ActionMessage("error.pyp.actividadesPYP.yaExiste",actividadesForm.getActividades("descripcion_"+i)));
					else
						errores.add("Error insertando actividad",new ActionMessage("error.pyp.actividadesPYP.error","al ingresar",actividadesForm.getActividades("descripcion_"+i)));
						
				}
				else
					actividadesForm.setActividades("consecutivo_"+i,resp);
				//*************************************************************
			}
			else
			{
				//*****MODIFICACION DE REGISTRO******************************
				this.llenarMundo(actividades,actividadesForm,i);
				resp = actividades.modificar(con);
				if(resp<=0)
					errores.add("Error insertando actividad",new ActionMessage("error.pyp.actividadesPYP.error","al modificar",actividadesForm.getActividades("descripcion_"+i)));
				//***********************************************************
			}
		}
		
		if(errores.isEmpty())
		{
			actividadesForm.setEstado("guardar");
		}
		else
		{
			saveErrors(request,errores);
			actividadesForm.setEstado("parametrizar");
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para llenar mudo de actividades PYP 
	 * @param actividades
	 * @param actividadesForm
	 * @param pos (posicion del mapa)
	 */
	private void llenarMundo(ActividadesPyp actividades, ActividadesPypForm actividadesForm, int pos) 
	{
		actividades.clean();
		actividades.setCampos("consecutivo",actividadesForm.getActividades("consecutivo_"+pos));
		actividades.setCampos("codigo",actividadesForm.getActividades("codigo_"+pos));
		actividades.setCampos("codigoCumplimiento",actividadesForm.getActividades("cod_cumplimiento_"+pos));
		actividades.setCampos("activo",actividadesForm.getActividades("activo_"+pos));
		actividades.setCampos("institucion",actividadesForm.getActividades("institucion_"+pos));
		actividades.setCampos("tipoActividad",actividadesForm.getTipoActividad());
		
	}

	/**
	 * Método implementado para ingresar una nueva actividad en el mapa de actividades
	 * @param con
	 * @param actividadesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevo(Connection con, ActividadesPypForm actividadesForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//se toma la ultima posicion del mapa (tamaño del mapa)
		int pos = actividadesForm.getNumRegistros();
		
		//se añaden los campos restantes del registro
		actividadesForm.setActividades("consecutivo_"+pos,"0");
		actividadesForm.setActividades("cod_cumplimiento_"+pos,"");
		actividadesForm.setActividades("activo_"+pos,ValoresPorDefecto.getValorTrueParaConsultas());
		actividadesForm.setActividades("institucion_"+pos,usuario.getCodigoInstitucion());
		actividadesForm.setActividades("es_usado_"+pos,"false");
		
		//se aumenta el tamaño del mapa
		pos++;
		actividadesForm.setActividades("numRegistros",pos);
		actividadesForm.setNumRegistros(pos);
		
		//se reanuda el estado a parametrizar
		actividadesForm.setEstado("parametrizar");
		
		//se toman los codigos existentes
		this.tomarCodigosExistentes(actividadesForm);
		
		//se ubica el pager en el último registro
		if(actividadesForm.getNumRegistros()>actividadesForm.getMaxPageItems())
		{
			int numReg = actividadesForm.getNumRegistros();
			int maxPag = actividadesForm.getMaxPageItems();
			actividadesForm.setOffset(numReg-(numReg%maxPag==0?maxPag:numReg%maxPag));
		}
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para postular la parametrizacion de las actividades
	 * @param con
	 * @param actividadesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionParametrizar(Connection con, ActividadesPypForm actividadesForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se instancia objeto de actividadesPyp
		ActividadesPyp actividades = new ActividadesPyp();
		actividades.setCampos("institucion",usuario.getCodigoInstitucion());
		actividades.setCampos("tipoActividad",actividadesForm.getTipoActividad());
		
		//Se realiza consulta de las actividades parametrizadas
		actividadesForm.setActividades(actividades.consultar(con));
		actividadesForm.setNumRegistros(Integer.parseInt(actividadesForm.getActividades("numRegistros").toString()));
		
		//se toma el maxPageItems
		actividadesForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		actividadesForm.setOffset(0);
		
		//se toman los codigos ya existentes
		this.tomarCodigosExistentes(actividadesForm);
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método impelemtado para tomar los códigos existentes
	 * @param actividadesForm
	 */
	private void tomarCodigosExistentes(ActividadesPypForm forma) 
	{
		String codigos = "";
		
		for(int i=0;i<forma.getNumRegistros();i++)
		{
			if(!codigos.equals(""))
				codigos += ",";
			codigos += forma.getActividades("codigo_"+i);
		}
		
		forma.setCodigosExistentes(codigos);
		
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



	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------Funciones de La funcionalidad de Actividades de Promocion Y Prevencion por Centros de Atención-------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------


	/**
	 * Metodo para eliminar una Actividad PYP para un centro de Atencion Especifico.  
	 * @param con
	 * @param actividadesForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward eliminarActividadCA(Connection con, ActividadesPypForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		ActividadesPyp mundo = new ActividadesPyp();
		int res = mundo.eliminarActividadesCentroAtencion( con, forma.getPosicion(), forma.getMapaCA(), forma.getCentroAtencion(), forma.getCodigoActividad(), usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario() );
		logger.info("mostrar mensaje antes: "+forma.getIsGuardoCA());
		if(res>0)
			forma.setIsGuardoCA(ConstantesBD.acronimoSi);
		else
			forma.setIsGuardoCA(ConstantesBD.acronimoNo);
		logger.info("mostrar mensaje despues: "+forma.getIsGuardoCA());
		return accionListarCA(con,true, forma, mapping, usuario, request); 
	}


	/**
	 * Metodo para
	 * @throws SQLException 
	 */
	private ActionForward accionListarCA(Connection con, boolean borrarNuevos, ActividadesPypForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		ActividadesPyp mundo = new ActividadesPyp();
		String nr = "";
		HashMap mpCA = new HashMap();
		
		//-- Limpiar el Mapa  	  
		if (borrarNuevos)
		{
			//-- Tener la informacion de los registros nuevos. Para que no se elimine. 
			nr = forma.getMapaCA("nroRegistrosNv")+"";
			if ( UtilidadCadena.noEsVacio(nr) )  
			{
				mpCA.put("nroRegistrosNv",nr);
				for (int i=0; i<Integer.parseInt(nr); i++)
				{
					mpCA.put("cod_act_nv_"+ i, forma.getMapaCA("cod_act_nv_"+ i)+"");
					mpCA.put("activo_act_nv_"+ i, forma.getMapaCA("activo_act_nv_"+ i)+"");
				}
			}
		}
		forma.getMapaCA().clear();


		//-- Mapa de parametros
		HashMap mapaParam = new HashMap();
		mapaParam.put("nroConsulta","1");
		mapaParam.put("codigoInstitucion",""+usuario.getCodigoInstitucionInt());
		
		//-- Cargar los Centros de Atencion
		forma.setMapaCA( mundo.consultarInformcion(con, mapaParam )  );

		//-- Limpiar el Mapa  	  
		if (borrarNuevos)
		{
			forma.getMapaCA().putAll(mpCA); 
		}	
		else
		{
			forma.setMapaCA("nroRegistrosNv","0");
		}
			
		
		//-- Consultar las Actividades De Promocion y Prevencion.
		mapaParam.clear();
		mapaParam.put("nroConsulta","2");
		mapaParam.put("codigoInstitucion",""+usuario.getCodigoInstitucionInt());
		
		HashMap mp = new HashMap();
		mp = mundo.consultarInformcion(con, mapaParam);
		forma.setMapaCA("numRegistrosAct", mp.get("numRegistros")+"");
		mp.remove("numRegistros");
		forma.getMapaCA().putAll(mp);
		
		//-- Consultar las Actividades De Promocion y Prevencion por Centro de Atencion ya Registradas.
		mapaParam.clear();
		mapaParam.put("nroConsulta","3");
		mapaParam.put("codigoInstitucion",""+usuario.getCodigoInstitucionInt());
		mapaParam.put("centroAtencion",""+ forma.getCentroAtencion() );
		
		mp = new HashMap();
		mp = mundo.consultarInformcion(con, mapaParam);
		forma.setMapaCA("numRegistrosActReg", mp.get("numRegistros")+"");
		mp.remove("numRegistros");
		forma.getMapaCA().putAll(mp);

		//-- 
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	
	/**
	 * Metodo para guardar La informacion del las actividades de Promocion y prevencion de un centro de Atencion.
	 * @param usuario 
	 * @param request 
	 * @throws SQLException 
	 */
	private ActionForward accionGuardarCA(Connection con, ActionMapping mapping, ActividadesPypForm forma, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		ActividadesPyp mundo = new ActividadesPyp();
		int res = 0;
		if((res = mundo.insertarActividadesCentroAtencion( con, forma.getMapaCA(), forma.getCentroAtencion(), usuario.getCodigoInstitucionInt(), usuario ))>0)
			forma.setIsGuardoCA(ConstantesBD.acronimoSi);
		else
			forma.setIsGuardoCA(ConstantesBD.acronimoNo);
		logger.info("valor de la respuesta: "+res);
		return accionListarCA(con, false, forma, mapping, usuario, request);
	}



	/**
	 * Metodo para eliminar las filas nuevas .. 
	 */
	private ActionForward eliminarFilaActividad(Connection con, ActionMapping mapping, ActividadesPypForm forma) throws SQLException 
	{
		int indice = 0, nroRows = 0;
		
		if ( UtilidadCadena.noEsVacio(forma.getMapaCA("nroFilaEliminar")+"") )
		{
			indice = Integer.parseInt(forma.getMapaCA("nroFilaEliminar")+"");
			nroRows = Integer.parseInt(forma.getMapaCA("nroRegistrosNv")+""); 
			boolean encontro = false;
			int k = 0;	

			
			HashMap mp  = forma.getMapaCA();
			
			for (int i = 0; i < nroRows; i++)
			{
				if ( i == indice )
				{
					mp.remove("cod_act_nv_"+ i);
					mp.remove("activo_act_nv_"+ i);
					encontro = true;
				}
				else
				{
					mp.put("cod_act_nv_"+ k, forma.getMapaCA("cod_act_nv_"+ i)+"");
					mp.put("activo_act_nv_"+ k, forma.getMapaCA("activo_act_nv_"+ i)+"");
					k++;	
				}
			}  
			

			forma.setMapaCA(mp);
			
			//-- Si encontro el indice puede Actualizar el Contador
			if ( encontro )
			{
				forma.getMapaCA().remove("nroFilaEliminar");
				forma.setMapaCA("nroRegistrosNv", Integer.parseInt(forma.getMapaCA("nroRegistrosNv")+"")-1 +"");
			}	
		}
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}



	/**
	 * Metodo para iniciar la Funcionalidad de Actividades de Promocion Y Prevencion por Centros de Atención.
	 * 
	 * Centros Costo
	 * @param con
	 * @param actividadesForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEmpezarCA(Connection con, ActividadesPypForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		ActividadesPyp mundo = new ActividadesPyp();
		
		//-- Limpìar e inicializar las variables.
		forma.resetCA();
		
		//-- Mapa de parametros
		HashMap mapaParam = new HashMap();
		mapaParam.put("nroConsulta","1");
		mapaParam.put("codigoInstitucion",""+usuario.getCodigoInstitucionInt());
		
		//-- Cargar los Centros de Atencion
		forma.setMapaCA( mundo.consultarInformcion(con, mapaParam )  );
		forma.setMapaCA("nroRegistrosNv","0"); 
		
		//-- Consultar las Actividades De Promocion y Prevencion.
		mapaParam.clear();
		mapaParam.put("nroConsulta","2");
		mapaParam.put("codigoInstitucion",""+usuario.getCodigoInstitucionInt());
		
		HashMap mp = new HashMap();
		mp = mundo.consultarInformcion(con, mapaParam);
		forma.setMapaCA("numRegistrosAct", mp.get("numRegistros")+"");
		mp.remove("numRegistros");
		forma.getMapaCA().putAll(mp);
		
		//-- Consultar las Actividades De Promocion y Prevencion por Centro de Atencion ya Registradas.
		mapaParam.clear();
		mapaParam.put("nroConsulta","3");
		mapaParam.put("codigoInstitucion",""+usuario.getCodigoInstitucionInt());
		mapaParam.put("centroAtencion",""+ forma.getCentroAtencion() );
		
		mp = new HashMap();
		mp = mundo.consultarInformcion(con, mapaParam);
		forma.setMapaCA("numRegistrosActReg", mp.get("numRegistros")+"");
		mp.remove("numRegistros");
		forma.getMapaCA().putAll(mp);

		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

}
