package com.princetonsa.action.inventarios;


import java.sql.Connection;
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
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.inventarios.TiposInventarioForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.TiposInventario;

public class TiposInventarioAction extends Action 
{
	/**
	 * Cadena que contiene los indices del mapa.
	 */
	private static final String indicesMapa="codigo_,nombre_,institucion_,cuenta_inventario_,cuenta_costo_,tiporegistro_,rubro_,codigo_interfaz_"; 
	private static final String indicesMapaGrupos="codigo_,clase_,nombre_,institucion_,cuenta_inventario_,cuenta_costo_,tiporegistro_,rubro_,aplica_cd_";		
	private static final String indicesMapaSubGrupos="codigo_,clase_,nombre_,institucion_,cuenta_inventario_,cuenta_costo_,tiporegistro_,subgrupo_,grupo_,rubro_";
	
	
	 /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(TiposInventarioAction.class);
	
	public ActionForward execute(	ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	{

		Connection con = null;
		try{

			if(form instanceof TiposInventarioForm)
			{

				con=UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				TiposInventarioForm forma = (TiposInventarioForm) form;

				UsuarioBasico usuario = null;
				usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());

				TiposInventario mundo = new TiposInventario();
				String estado = forma.getEstado();

				logger.warn("estado --> "+estado);

				//lo inicio en false, por que apenas empieza la ejecucion del proceso.
				forma.setProcesoExitoso(false);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de TiposInventarioAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}			
				else if (estado.equals("empezar"))
				{
					forma.reset();
					ValoresPorDefecto.cargarValoresIniciales(con);
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionConsultarTiposInventario(con,forma,mundo,usuario,mapping);
					UtilidadBD.cerrarConexion(con);

					return mapping.findForward("principal");
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("nuevo"))
				{
					this.accionNuevaClase(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getClaseInventario("numRegistros").toString()), response, request, "inventarios.jsp",true);
				}			
				else if (estado.equals("guardarClaseInventario"))
				{
					this.accionGuardarRegistros(con,forma,mundo,usuario);
					int codClaseActual =-1;
					codClaseActual = forma.getIndexDetalle();

					forma.reset();
					forma.setIndexDetalle(codClaseActual);

					forma.setProcesoExitoso(true);
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionConsultarTiposInventario(con,forma,mundo,usuario,mapping);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}		
				else if(estado.equals("eliminar"))
				{
					UtilidadBD.closeConnection(con);
					return accionEliminarClase(forma,request,response);
				}
				else if(estado.equals("eliminarGrupo"))
				{
					UtilidadBD.closeConnection(con);
					return accionEliminarGrupo(forma,request,response);
				}
				else if(estado.equals("eliminarSubGrupo"))
				{
					UtilidadBD.closeConnection(con);
					return accionEliminarSubGrupo(forma,request,response);
				}
				else if (estado.equals("eliminarRubroClase"))  
				{
					accionEliminarRubroClase(mapping, forma, mundo);
					this.accionConsultarTiposInventario(con,forma,mundo,usuario,mapping);
					return mapping.findForward("principal");
				}
				else if (estado.equals("eliminarRubroGrupo"))  
				{
					accionEliminarRubroGrupo(mapping, forma, mundo);
					int mapaCodClase = forma.getIndexDetalle();
					this.accionConsultarTiposGrupoInventario(con,forma,mundo,usuario,mapping, mapaCodClase);
					return mapping.findForward("principalGrupos");
				}
				else if (estado.equals("eliminarRubroSubGrupo"))  
				{
					accionEliminarRubroSubGrupo(mapping, forma, mundo);
					int mapaCodClase = forma.getIndexDetalle();				
					int mapaCodGrupo = forma.getIndexDetalleGrupo();
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionConsultarTiposSubGrupoInventario(con,forma,mundo,usuario,mapping, mapaCodClase, mapaCodGrupo);

					return mapping.findForward("principalSubGrupos");
				}
				else if(estado.equals("destinosGrupos"))/////////////////////////////////////////////////////////GRUPOS//
				{
					int mapaCodClase = forma.getIndexDetalle();
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionConsultarTiposGrupoInventario(con,forma,mundo,usuario,mapping, mapaCodClase);
					UtilidadBD.cerrarConexion(con);
					forma.setOffset(0);
					return UtilidadSesion.redireccionar("grupos.jsp",forma.getMaxPageItems(),Integer.parseInt(forma.getGrupoInventario("numRegistros").toString()), response, request, "grupos.jsp",false);
					//return mapping.findForward("principalGrupos");
				}
				else if(estado.equals("ordenarGrupos"))
				{
					this.accionOrdenarMapaGrupos(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principalGrupos");
				}		
				else if (estado.equals("guardarGrupoInventario"))
				{
					return accionGuardarGrupoInventario(con,forma,mundo,usuario,mapping,request);				
				}		
				else if(estado.equals("nuevoGrupo"))
				{
					this.accionNuevoGrupo(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getGrupoInventario("numRegistros").toString()), response, request, "grupos.jsp",true);
				}		
				else if(estado.equals("destinosSubGrupos"))/////////////////////////////////////////////////////////GRUPOS//
				{
					int mapaCodClase = forma.getIndexDetalle();				
					int mapaCodGrupo = forma.getIndexDetalleGrupo();
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionConsultarTiposSubGrupoInventario(con,forma,mundo,usuario,mapping, mapaCodClase, mapaCodGrupo);
					UtilidadBD.cerrarConexion(con);
					forma.setOffset(0);
					return UtilidadSesion.redireccionar("subgrupos.jsp",forma.getMaxPageItems(),Integer.parseInt(forma.getGrupoInventario("numRegistros").toString()), response, request, "subgrupos.jsp",false);
					//return mapping.findForward("principalSubGrupos");
				}			
				else if(estado.equals("ordenarSubGrupos"))
				{
					this.accionOrdenarMapaGrupos(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principalSubGrupos");
				}				
				else if (estado.equals("guardarSubGrupoInventario"))
				{
					this.accionGuardarRegistrosSubGrupos(con,forma,mundo,usuario);
					int codClaseActual = forma.getIndexDetalle();
					int codGrupoActual = forma.getIndexDetalleGrupo();

					forma.reset();
					forma.setIndexDetalle(codClaseActual);
					forma.setIndexDetalleGrupo(codGrupoActual);

					forma.setProcesoExitoso(true);

					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionConsultarTiposSubGrupoInventario(con, forma, mundo, usuario, mapping, forma.getIndexDetalle(), forma.getIndexDetalleGrupo() );

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principalSubGrupos");
				}		
				else if(estado.equals("nuevoSubGrupo"))
				{
					this.accionNuevoSubGrupo(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getSubgrupoInventario("numRegistros").toString()), response, request, "subgrupos.jsp",true);
				}	
				else if(estado.equals("volver"))
				{  
					forma.reset();
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionConsultarTiposInventario(con,forma,mundo,usuario,mapping);
					UtilidadBD.cerrarConexion(con);

					response.sendRedirect("inventarios.do?estado=empezar");
					return null;
					//return mapping.findForward("principal");

				}			
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de TiposInventario");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
			}
			else
			{
				logger.error("El form no es compatible con el form de TiposInventarioForm");
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
	 * Metodo para eliminar el rubro de un codigo subgrupo especifico
	 * @param mapping
	 * @param forma
	 * @param mundo
	 */
	private void accionEliminarRubroSubGrupo(ActionMapping mapping, TiposInventarioForm forma, TiposInventario mundo) 
	{
		mundo.eliminarRubro(forma.getCodigoEliminar(), "subgrupo");
	}

	/**
	 * Metodo para eliminar el rubro de un codigo grupo especifico
	 * @param mapping
	 * @param forma
	 * @param mundo
	 */
	private void accionEliminarRubroGrupo(ActionMapping mapping, TiposInventarioForm forma, TiposInventario mundo) 
	{
		mundo.eliminarRubro(forma.getCodigoEliminar(), "grupo");
	}


	/**
	 * Metodo para eliminar el rubro de un codigo clase especifico
	 * @param mapping
	 * @param forma
	 * @param mundo 
	 * @return
	 */
	private void accionEliminarRubroClase(ActionMapping mapping, TiposInventarioForm forma, TiposInventario mundo) 
	{
		mundo.eliminarRubro(forma.getCodigoEliminar(), "clase");
	}



	/**
	 * 
	 * @param forma
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionEliminarSubGrupo(TiposInventarioForm forma, HttpServletRequest request, HttpServletResponse response)
	{
		int numRegMapEliminados=Integer.parseInt(forma.getSubgrupoInventarioEliminado("numRegistros")+"");
		int ultimaPosMapa=(Integer.parseInt(forma.getSubgrupoInventario("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		String[] indices=indicesMapaSubGrupos.split(",");
		if((forma.getSubgrupoInventario("tiporegistro_"+forma.getPosEliminar())+"").trim().equalsIgnoreCase("BD"))
		{
			for(int i=0;i<indices.length;i++)
			{
				forma.setSubgrupoInventarioEliminado(indices[i]+""+numRegMapEliminados, forma.getSubgrupoInventario(indices[i]+""+forma.getPosEliminar()));
			}
			forma.setSubgrupoInventarioEliminado("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getPosEliminar();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				
				forma.setSubgrupoInventario(indices[j]+""+i,forma.getSubgrupoInventario(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getSubgrupoInventario().remove(indices[j]+""+ultimaPosMapa);
		}
		//ahora actualizamo el numero de registros en el mapa.
		
		forma.setSubgrupoInventario("numRegistros",ultimaPosMapa);
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente().indexOf(".do")>0?"subgrupos.jsp":forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getSubgrupoInventario("numRegistros").toString()), response, request, "subgrupos.jsp",false);

	}



	/**
	 * 
	 * @param forma
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionEliminarGrupo(TiposInventarioForm forma, HttpServletRequest request, HttpServletResponse response)
	{
		int numRegMapEliminados=Integer.parseInt(forma.getGrupoInventarioEliminado("numRegistros")+"");
		int ultimaPosMapa=(Integer.parseInt(forma.getGrupoInventario("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		String[] indices=indicesMapaGrupos.split(",");
		if((forma.getGrupoInventario("tiporegistro_"+forma.getPosEliminar())+"").trim().equalsIgnoreCase("BD"))
		{
			for(int i=0;i<indices.length;i++)
			{
				forma.setGrupoInventarioEliminado(indices[i]+""+numRegMapEliminados, forma.getGrupoInventario(indices[i]+""+forma.getPosEliminar()));
			}
			forma.setGrupoInventarioEliminado("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getPosEliminar();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				
				forma.setGrupoInventario(indices[j]+""+i,forma.getGrupoInventario(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getGrupoInventario().remove(indices[j]+""+ultimaPosMapa);
		}
		//ahora actualizamo el numero de registros en el mapa.
		
		forma.setGrupoInventario("numRegistros",ultimaPosMapa);
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente().indexOf(".do")>0?"grupos.jsp":forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getGrupoInventario("numRegistros").toString()), response, request, "grupos.jsp",false);

	}


	/**
	 * 
	 * @param forma
	 * @param response 
	 * @param request 
	 * @return
	 */
	private ActionForward accionEliminarClase(TiposInventarioForm forma, HttpServletRequest request, HttpServletResponse response)
	{
		int numRegMapEliminados=Integer.parseInt(forma.getClaseInventarioEliminado("numRegistros")+"");
		int ultimaPosMapa=(Integer.parseInt(forma.getClaseInventario("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		String[] indices=indicesMapa.split(",");
		if((forma.getClaseInventario("tiporegistro_"+forma.getPosEliminar())+"").trim().equalsIgnoreCase("BD"))
		{
			for(int i=0;i<indices.length;i++)
			{
				forma.setClaseInventarioEliminado(indices[i]+""+numRegMapEliminados, forma.getClaseInventario(indices[i]+""+forma.getPosEliminar()));
			}
			forma.setClaseInventarioEliminado("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getPosEliminar();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				
				forma.setClaseInventario(indices[j]+""+i,forma.getClaseInventario(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getClaseInventario().remove(indices[j]+""+ultimaPosMapa);
		}
		//ahora actualizamo el numero de registros en el mapa.
		
		forma.setClaseInventario("numRegistros",ultimaPosMapa);
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente().indexOf(".do")>0?"inventarios.jsp":forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getClaseInventario("numRegistros").toString()), response, request, "inventarios.jsp",false);

	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarGrupoInventario(Connection con, TiposInventarioForm forma, TiposInventario mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores= new ActionErrors();
		int numReg=Integer.parseInt(forma.getGrupoInventario("numRegistros")+"");
		
		for(int i=0;i<numReg;i++)
		{
			if((forma.getGrupoInventario("codigo_"+i)+"").trim().equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Codigo del registro "+(i+1)));
			}
			else
			{
				for(int j=0;j<i;j++)
				{
					if((forma.getGrupoInventario("codigo_"+i)+"").equalsIgnoreCase(forma.getGrupoInventario("codigo_"+j)+""))
					{
						errores.add("", new ActionMessage("errors.yaExiste","El código "+forma.getGrupoInventario("codigo_"+i)));
					}
				}
			}
			if((forma.getGrupoInventario("nombre_"+i)+"").trim().equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El nombre del registro "+(i+1)));
			}
		}				
		
		if(errores.isEmpty())
		{
			///////////////////////////////////
			this.accionGuardarRegistrosGrupos(con,forma,mundo,usuario);
			int codClaseActual =-1;
			codClaseActual = forma.getIndexDetalle();
			int codGrupoActual =-1;
			codGrupoActual = forma.getIndexDetalleGrupo();

			forma.reset();
			forma.setIndexDetalle(codClaseActual);
			forma.setIndexDetalleGrupo(codGrupoActual);
			
			forma.setProcesoExitoso(true);
			
			forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
			this.accionConsultarTiposGrupoInventario(con, forma, mundo, usuario, mapping, forma.getIndexDetalle());////
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principalGrupos");
			/////////////////////////////////////
		}
		else
		{
			saveErrors(request,errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principalGrupos");
		}
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 */
	private void accionConsultarTiposInventario(Connection con, TiposInventarioForm forma, TiposInventario mundo, UsuarioBasico usuario, ActionMapping  mapping)
	{
		HashMap vo=new HashMap();
		vo.put("institucion", usuario.getCodigoInstitucion());
		forma.setClaseInventario(mundo.consultarClaseInventario(con, vo));
	}

	/**
	 * 
	 */
	private void llenarMundo (TiposInventarioForm forma, TiposInventario mundo)
	{
		mundo.setMapaClaseInventarioTemp(forma.getClaseInventario());
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarRegistros(Connection con, TiposInventarioForm forma, TiposInventario mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		//eliminar
		logger.info("********************************************************************************************************");
		logger.info("numReg elim->"+forma.getClaseInventarioEliminado("numRegistros"));
		
		for(int i=0;i<Integer.parseInt(forma.getClaseInventarioEliminado("numRegistros")+"");i++)
		{
			if(mundo.eliminarClaseInventario(con,forma.getClaseInventarioEliminado("codigo_"+i)+"",usuario.getCodigoInstitucion()))
			{
				this.generarLogClase(forma, mundo, usuario, ConstantesBD.tipoRegistroLogEliminacion, i);
				transaccion=true;
			}
		}
		
		logger.info("numReg elim->"+forma.getClaseInventario("numRegistros"));
		
		Utilidades.imprimirMapa(forma.getClaseInventario());
		
		for(int i=0;i<Integer.parseInt(forma.getClaseInventario("numRegistros")+"");i++)
		{
			if((forma.getClaseInventario("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma,mundo,forma.getClaseInventario("codigo_"+i)+"",i,usuario))
			{
				HashMap vo=new HashMap();
				vo.put("nombre",forma.getClaseInventario("nombre_"+i));
				vo.put("cuenta_inventario",forma.getClaseInventario("cuenta_inventario_"+i));
				vo.put("cuenta_costo",forma.getClaseInventario("cuenta_costo_"+i));
				vo.put("rubro",forma.getClaseInventario("rubro_"+i));
				vo.put("codigo",forma.getClaseInventario("codigo_"+i));
				vo.put("codigo_interfaz",forma.getClaseInventario("codigo_interfaz_"+i));
				
				vo.put("institucion", usuario.getCodigoInstitucion());
				
				transaccion=mundo.modificarClaseInventario(con, vo);
				this.generarLogClase(forma, mundo, usuario, ConstantesBD.tipoRegistroLogModificacion, i);
			}
			else if((forma.getClaseInventario("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getClaseInventario("codigo_"+i));
				vo.put("codigo_interfaz",forma.getClaseInventario("codigo_interfaz_"+i));
				vo.put("nombre",forma.getClaseInventario("nombre_"+i));
				vo.put("cuenta_inventario",forma.getClaseInventario("cuenta_inventario_"+i));
				vo.put("cuenta_costo",forma.getClaseInventario("cuenta_costo_"+i));
				vo.put("rubro",forma.getClaseInventario("rubro_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				
				transaccion=mundo.insertarClaseInventario(con, vo);
			}
		}
		
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
	}	

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param codigo
	 * @param pos
	 * @param usuario
	 * @return
	 */
	private boolean existeModificacion(Connection con, TiposInventarioForm forma, TiposInventario mundo,String codigo,int pos, UsuarioBasico usuario)
	{
		
		logger.info("existeModificacion--> codigo: "+codigo);
		
		HashMap temp=mundo.consultarClaseInventarioEspecifico(con, codigo,usuario.getCodigoInstitucion());
		logger.info("indices->"+indicesMapa);
		String[] indices=indicesMapa.split(",");
		for(int i=0;i<indices.length;i++)
		{
			logger.info("indices[i]0-->"+indices[i]+"  pos->"+pos+" temp contiene key->"+temp.containsKey(indices[i]+"0")+" && forma contiene key-->"+forma.getClaseInventario().containsKey(indices[i]+""+pos));
			if(temp.containsKey(indices[i]+"0")&&forma.getClaseInventario().containsKey(indices[i]+""+pos))
			{
				logger.info("temp.get(indices[i]0)-->"+temp.get(indices[i]+"0")+" forma.getClaseInventario(indices[i]pos))->"+forma.getClaseInventario(indices[i]+""+pos));
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getClaseInventario(indices[i]+""+pos)+"").trim())))
				{
					logger.info("iguales->false");
					mundo.setMapaClaseInventarioTemp(temp); //setMapaMotivosSircTemp(temp);
					//this.generarLog(forma, mundo, usuario, false, pos);
					return true;
				}
			}
		}
		logger.info("sale con false");
		return false;
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionNuevaClase(TiposInventarioForm forma)
	{
		int pos=Integer.parseInt(forma.getClaseInventario("numRegistros")+"");
		forma.setClaseInventario("codigo_"+pos,"");
		forma.setClaseInventario("nombre_"+pos,"");
		forma.setClaseInventario("cuenta_inventario_"+pos,"");
		forma.setClaseInventario("cuenta_costo_"+pos,"");
		forma.setClaseInventario("tiporegistro_"+pos,"MEM");
		forma.setClaseInventario("numRegistros", (pos+1)+"");
	}

	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarMapa(TiposInventarioForm forma)
	{
		String[] indices=indicesMapa.split(",");
		int numReg=Integer.parseInt(forma.getClaseInventario("numRegistros")+"");
		forma.setClaseInventario(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getClaseInventario(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setClaseInventario("numRegistros",numReg+"");
	}
	
	/**
	 * 
	 */
	private void accionConsultarTiposGrupoInventario(Connection con, TiposInventarioForm forma, TiposInventario mundo, UsuarioBasico usuario, ActionMapping  mapping, int mapaCodClase)
	{
		HashMap vo=new HashMap();
		vo.put("institucion", usuario.getCodigoInstitucion());
		vo.put("codigoClase", mapaCodClase) ;//forma.getClaseInventario("codigo_"+mapaCodClase)+"");
		forma.setGrupoInventario(mundo.consultarGrupoInventario(con, vo));
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarMapaGrupos(TiposInventarioForm forma)
	{
		String[] indices=indicesMapaGrupos.split(",");
		int numReg=Integer.parseInt(forma.getGrupoInventario("numRegistros")+"");
		forma.setGrupoInventario(Listado.ordenarMapa(indices,forma.getPatronOrdenarGrupo(),forma.getUltimoPatronGrupo(),forma.getGrupoInventario(),numReg));
		forma.setUltimoPatronGrupo(forma.getPatronOrdenarGrupo());
		forma.setGrupoInventario("numRegistros",numReg+"");
	}	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarRegistrosGrupos(Connection con, TiposInventarioForm forma, TiposInventario mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getGrupoInventarioEliminado("numRegistros")+"");i++)
		{
			if(mundo.eliminarGrupoInventario(con,forma.getGrupoInventarioEliminado("codigo_"+i)+"",usuario.getCodigoInstitucion(),forma.getGrupoInventarioEliminado("clase_"+i)+""))
			{
				this.generarLogGrupo(forma, mundo, usuario, ConstantesBD.tipoRegistroLogEliminacion, i);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getGrupoInventario("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getGrupoInventario("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacionGrupo(con,forma,mundo,forma.getGrupoInventario("codigo_"+i)+"",i,usuario))
			{

				HashMap vo=new HashMap();
				vo.put("nombre",forma.getGrupoInventario("nombre_"+i));
				vo.put("clase",forma.getGrupoInventario("clase_"+i));
				vo.put("cuenta_inventario",forma.getGrupoInventario("cuenta_inventario_"+i));
				vo.put("cuenta_costo",forma.getGrupoInventario("cuenta_costo_"+i));
				vo.put("rubro", forma.getGrupoInventario("rubro_"+i));
				vo.put("codigo",forma.getGrupoInventario("codigo_"+i));
				vo.put("aplica_cd",forma.getGrupoInventario("aplica_cd_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				transaccion=mundo.modificarGrupoInventario(con, vo);
				this.generarLogGrupo(forma, mundo, usuario, ConstantesBD.tipoRegistroLogModificacion, i);
			}
			//insertar
			else if((forma.getGrupoInventario("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getGrupoInventario("codigo_"+i));
				vo.put("nombre",forma.getGrupoInventario("nombre_"+i));
				vo.put("cuenta_inventario",forma.getGrupoInventario("cuenta_inventario_"+i));
				vo.put("cuenta_costo",forma.getGrupoInventario("cuenta_costo_"+i));
				vo.put("rubro", forma.getGrupoInventario("rubro_"+i));
				vo.put("aplica_cd",forma.getGrupoInventario("aplica_cd_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("clase",forma.getGrupoInventario("clase_"+i));
				transaccion=mundo.insertarGrupoInventario(con, vo);
			}
		}
		
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
	}	


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param codigo
	 * @param pos
	 * @param usuario
	 * @return
	 */
	private boolean existeModificacionGrupo(Connection con, TiposInventarioForm forma, TiposInventario mundo,String codigo,int pos, UsuarioBasico usuario)
	{
		
		HashMap temp=mundo.consultarGrupoInventarioEspecifico(con, codigo) ;//(con, codigo,usuario.getCodigoInstitucion());
		
		String[] indices=indicesMapaGrupos.split(",");
		for(int i=0;i<indices.length;i++)
		{
			if(temp.containsKey(indices[i]+"0")&&forma.getGrupoInventario().containsKey(indices[i]+""+pos))
			{
				if(!((temp.get(indices[i]+"0")+"").trim().equals(forma.getGrupoInventario(indices[i]+""+pos)+"")))
				{
					mundo.setMapaGrupoInventarioTemp(temp); 
					//this.generarLog(forma, mundo, usuario, false, pos);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionNuevoGrupo(TiposInventarioForm forma)
	{
		int pos=Integer.parseInt(forma.getGrupoInventario("numRegistros")+"");
		forma.setGrupoInventario("codigo_"+pos,"");
		forma.setGrupoInventario("clase_"+pos, forma.getIndexDetalle());
		forma.setGrupoInventario("nombre_"+pos,"");
		forma.setGrupoInventario("cuenta_inventario_"+pos,"");
		forma.setGrupoInventario("cuenta_costo_"+pos,"");
		forma.setGrupoInventario("rubro_"+pos,"");
		forma.setGrupoInventario("tiporegistro_"+pos,"MEM");
		forma.setGrupoInventario("numRegistros", (pos+1)+"");
		forma.setGrupoInventario("aplica_cd_"+pos, ConstantesBD.acronimoNo);
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param mapaCodClase
	 * @param mapaCodGrupo
	 */
	private void accionConsultarTiposSubGrupoInventario(Connection con, TiposInventarioForm forma, TiposInventario mundo, UsuarioBasico usuario, ActionMapping  mapping, int mapaCodClase, int mapaCodGrupo)
	{
		HashMap vo=new HashMap();
		vo.put("institucion", usuario.getCodigoInstitucion());
		vo.put("codigoClase", mapaCodClase ); //forma.getClaseInventario("codigo_"+mapaCodClase)+"");
		vo.put("codigoGrupo", mapaCodGrupo ); //forma.getGrupoInventario("codigo_"+mapaCodGrupo)+"");
		//vo.put("codigoSubGrupo", forma.getClaseInventario("codigo_"+mapaCodClase)+"");

		forma.setSubgrupoInventario(mundo.consultarSubGrupoInventario(con, vo));
		
		
	}
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarMapaSubGrupos(TiposInventarioForm forma)
	{
		String[] indices=indicesMapaSubGrupos.split(",");
		int numReg=Integer.parseInt(forma.getSubgrupoInventario("numRegistros")+"");
		forma.setSubgrupoInventario(Listado.ordenarMapa(indices,forma.getPatronOrdenarSubGrupo(),forma.getUltimoPatronSubGrupo(),forma.getSubgrupoInventario(),numReg));
		forma.setUltimoPatronSubGrupo(forma.getPatronOrdenarGrupo());
		forma.setSubgrupoInventario("numRegistros",numReg+"");
	}	


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarRegistrosSubGrupos(Connection con, TiposInventarioForm forma, TiposInventario mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		//		eliminar
		for(int i=0;i<Integer.parseInt(forma.getSubgrupoInventarioEliminado("numRegistros")+"");i++)
		{
			if(mundo.eliminarSubGrupoInventario(con,forma.getSubgrupoInventarioEliminado("codigo_"+i)+"",usuario.getCodigoInstitucion(),forma.getSubgrupoInventarioEliminado("subgrupo_"+i)+"",forma.getSubgrupoInventarioEliminado("grupo_"+i)+"",forma.getSubgrupoInventarioEliminado("clase_"+i)+""))
			{
				this.generarLogSubGrupo(forma, mundo, usuario, ConstantesBD.tipoRegistroLogEliminacion, i);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getSubgrupoInventario("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getSubgrupoInventario("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacionSubGrupo(con,forma,mundo,forma.getGrupoInventario("codigo_"+i)+"",i,usuario))
			{

				HashMap vo=new HashMap();
				vo.put("nombre",forma.getSubgrupoInventario("nombre_"+i));
				vo.put("clase",forma.getSubgrupoInventario("clase_"+i));
				vo.put("cuenta_inventario",forma.getSubgrupoInventario("cuenta_inventario_"+i));
				vo.put("cuenta_costo",forma.getSubgrupoInventario("cuenta_costo_"+i));
				vo.put("codigo",forma.getSubgrupoInventario("codigo_"+i));
				
				vo.put("rubro",forma.getSubgrupoInventario("rubro_"+i));
				vo.put("subgrupo",forma.getSubgrupoInventario("subgrupo_"+i));
				vo.put("grupo",forma.getSubgrupoInventario("grupo_"+i));
				
				vo.put("institucion", usuario.getCodigoInstitucion());
				transaccion=mundo.modificarSubGrupoInventario(con, vo);
				this.generarLogSubGrupo(forma, mundo, usuario,  ConstantesBD.tipoRegistroLogModificacion, i);
			}
			//insertar
			else if((forma.getSubgrupoInventario("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getSubgrupoInventario("codigo_"+i));
				vo.put("nombre",forma.getSubgrupoInventario("nombre_"+i));
				vo.put("cuenta_inventario",forma.getSubgrupoInventario("cuenta_inventario_"+i));
				vo.put("cuenta_costo",forma.getSubgrupoInventario("cuenta_costo_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("clase",forma.getSubgrupoInventario("clase_"+i));
				vo.put("subgrupo",forma.getSubgrupoInventario("subgrupo_"+i));
				vo.put("grupo",forma.getSubgrupoInventario("grupo_"+i));
				vo.put("rubro",forma.getSubgrupoInventario("rubro_"+i));
				transaccion=mundo.insertarSubGrupoInventario(con, vo) ;
			}
		}
		
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param tipoLog
	 * @param pos
	 */
	private void generarLogClase(TiposInventarioForm forma, TiposInventario mundo, UsuarioBasico usuario, int tipoLog, int pos)
	{
		

		String log="";
		String[] indices=indicesMapa.split(",");
		if(tipoLog==ConstantesBD.tipoRegistroLogModificacion)
		{

		    log="\n            ====INFORMACION ORIGINAL CLASE-GRUPOS-SUBGRUPO-INVEENTARIO ===== ";
		    for(int i=0;i<indices.length;i++)
		    {
		    	log+="\n* "+indices[i]+" ["+mundo.getMapaClaseInventarioTemp().get(indices[i]+""+pos)+"]";
		    }

		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓNCLASE-GRUPOS-SUBGRUPO-INVEENTARIO ===== ";
		    
		    for(int i=0;i<indices.length;i++)
		    {
		    	log+="\n* "+indices[i]+" ["+forma.getClaseInventario(indices[i]+""+pos)+"]";
		    }
		}
		else if(tipoLog==ConstantesBD.tipoRegistroLogEliminacion)
		{
		
		    log="\n            ====INFORMACION ELIMINADA DE CLASE-GRUPOS-SUBGRUPO-INVEENTARIO ===== " ;
		    for(int i=0;i<indices.length;i++)
		    {
		    	log+="\n* "+indices[i]+" ["+forma.getClaseInventarioEliminado(indices[i]+""+pos)+"]";
		    }
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logModificarClaseGrupoSubgrupoInvCodigo, log, tipoLog,usuario.getLoginUsuario());
			
	}
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param tipoLog
	 * @param pos
	 */
	private void generarLogGrupo(TiposInventarioForm forma, TiposInventario mundo, UsuarioBasico usuario, int tipoLog, int pos)
	{
		

		String log="";
		String[] indices=indicesMapaGrupos.split(",");
		if(tipoLog==ConstantesBD.tipoRegistroLogModificacion)
		{

		    log="\n            ====INFORMACION ORIGINAL CLASE-GRUPOS-SUBGRUPO-INVEENTARIO ===== ";
		    for(int i=0;i<indices.length;i++)
		    {
		    	log+="\n* "+indices[i]+" ["+mundo.getMapaGrupoInventarioTemp().get(indices[i]+""+pos)+"]";
		    }

		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓNCLASE-GRUPOS-SUBGRUPO-INVEENTARIO ===== ";
		    
		    for(int i=0;i<indices.length;i++)
		    {
		    	log+="\n* "+indices[i]+" ["+forma.getGrupoInventario(indices[i]+""+pos)+"]";
		    }
		}
		else if(tipoLog==ConstantesBD.tipoRegistroLogEliminacion)
		{
		
		    log="\n            ====INFORMACION ELIMINADA DE CLASE-GRUPOS-SUBGRUPO-INVEENTARIO ===== " ;
		    for(int i=0;i<indices.length;i++)
		    {
		    	log+="\n* "+indices[i]+" ["+forma.getGrupoInventarioEliminado(indices[i]+""+pos)+"]";
		    }
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logModificarClaseGrupoSubgrupoInvCodigo, log, tipoLog,usuario.getLoginUsuario());
			
	}
	


	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param tipoLog
	 * @param pos
	 */
	private void generarLogSubGrupo(TiposInventarioForm forma, TiposInventario mundo, UsuarioBasico usuario, int tipoLog, int pos)
	{
		

		String log="";
		String[] indices=indicesMapaSubGrupos.split(",");
		if(tipoLog==ConstantesBD.tipoRegistroLogModificacion)
		{

		    log="\n            ====INFORMACION ORIGINAL CLASE-GRUPOS-SUBGRUPO-INVEENTARIO ===== ";
		    for(int i=0;i<indices.length;i++)
		    {
		    	log+="\n* "+indices[i]+" ["+mundo.getMapaSubGrupoInventarioTemp().get(indices[i]+""+pos)+"]";
		    }

		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓNCLASE-GRUPOS-SUBGRUPO-INVEENTARIO ===== ";
		    
		    for(int i=0;i<indices.length;i++)
		    {
		    	log+="\n* "+indices[i]+" ["+forma.getSubgrupoInventario(indices[i]+""+pos)+"]";
		    }
		}
		else if(tipoLog==ConstantesBD.tipoRegistroLogEliminacion)
		{
		
		    log="\n            ====INFORMACION ELIMINADA DE CLASE-GRUPOS-SUBGRUPO-INVEENTARIO ===== " ;
		    for(int i=0;i<indices.length;i++)
		    {
		    	log+="\n* "+indices[i]+" ["+forma.getSubgrupoInventarioEliminado(indices[i]+""+pos)+"]";
		    }
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logModificarClaseGrupoSubgrupoInvCodigo, log, tipoLog,usuario.getLoginUsuario());
			
	}



	private boolean existeModificacionSubGrupo(Connection con, TiposInventarioForm forma, TiposInventario mundo,String codigo,int pos, UsuarioBasico usuario)
	{
		
		HashMap temp=mundo.consultarSubGrupoInventarioEspecifico(con, codigo) ;//(con, codigo,usuario.getCodigoInstitucion());
		
		String[] indices=indicesMapaSubGrupos.split(",");
		for(int i=0;i<indices.length;i++)
		{
			if(temp.containsKey(indices[i]+"0")&&forma.getSubgrupoInventario().containsKey(indices[i]+""+pos))
			{
				if(!((temp.get(indices[i]+"0")+"").trim().equals(forma.getSubgrupoInventario(indices[i]+""+pos)+"")))
				{
					mundo.setMapaSubGrupoInventarioTemp(temp); //XXXInventarioTemp(temp); 
					return true;
				}
			}
		}
		return false;
	}	


	
	private void accionNuevoSubGrupo(TiposInventarioForm forma)
	{
		int pos=Integer.parseInt(forma.getSubgrupoInventario("numRegistros")+"");
		forma.setSubgrupoInventarioEliminado("codigo_"+pos,"");
		forma.setSubgrupoInventario("clase_"+pos, forma.getIndexDetalle());
		forma.setSubgrupoInventario("grupo_"+pos, forma.getIndexDetalleGrupo());
		forma.setSubgrupoInventario("nombre_"+pos,"");
		forma.setSubgrupoInventario("cuenta_inventario_"+pos,"");
		forma.setSubgrupoInventario("cuenta_costo_"+pos,"");
		forma.setSubgrupoInventario("tiporegistro_"+pos,"MEM");
		forma.setSubgrupoInventario("numRegistros", (pos+1)+"");
		
	
	}	
}
