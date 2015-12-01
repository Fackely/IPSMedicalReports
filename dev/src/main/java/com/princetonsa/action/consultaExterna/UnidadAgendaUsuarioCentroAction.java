package com.princetonsa.action.consultaExterna;

//Autor: Diego Fernando Bedoya

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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.consultaExterna.UnidadAgendaUsuarioCentroForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.consultaExterna.UnidadAgendaUsuarioCentro;

public class UnidadAgendaUsuarioCentroAction extends Action
{
	Logger logger = Logger.getLogger(UnidadAgendaUsuarioCentroAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
	{
	
		Connection con = null;
		try {
		if(response==null);
		if(form instanceof UnidadAgendaUsuarioCentroForm)
		{
			
			con = UtilidadBD.abrirConexion();
			
			if(con == null)
			{	
				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
				return mapping.findForward("paginaError");
			}			
			UnidadAgendaUsuarioCentroForm forma=(UnidadAgendaUsuarioCentroForm)form;
			String estado = forma.getEstado();
			
			ActionErrors errores=new ActionErrors();
			
			forma.setMensaje(new ResultadoBoolean(false));
			
			logger.info("\n\n ESTADO UNIDAD DE AGENDA POR USUARIO POR CENTRO DE ATENCION---->"+estado+"\n\n");
			
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			if(estado == null)
			{
				forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());				
				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			
			else if(estado.equals("empezar"))
			{
				return this.accionEmpezar(forma, con, mapping, usuario);
			}
			else if(estado.equals("cargarConsultaPrincipal"))
			{
				return this.accionCargarConsultaPrincipal(forma, con, mapping, usuario, request);
			}
			else if(estado.equals("nuevoUnidad"))
			{
				return this.accionNuevoUnidad(forma, con, mapping, usuario, request);
			}
			else if(estado.equals("guardarTipoAtencion"))
			{
				for(int i=0;i<(Utilidades.convertirAEntero(forma.getUnidadesMap("numRegistros")+""));i++)
				{
					if(Utilidades.convertirAEntero(forma.getUnidadesMap("codigo_"+i)+"") == Utilidades.convertirAEntero(forma.getUnidad()))
						forma.setTipoAtencion(forma.getUnidadesMap("tipoatencion_"+i)+"");
				}
				UtilidadBD.closeConnection(con);
				return mapping.findForward("unidadAgendaUsuarioCentro");
			}
			else if(estado.equals("cargarUnidades"))
			{
				return this.accionCargarUnidades(forma, con, mapping, usuario);
			}
			else if(estado.equals("subirActividadN"))
			{
				return this.accionSubirActividad(forma, con, mapping, usuario, 1);
			}
			else if(estado.equals("subirActividadM"))
			{
				return this.accionSubirActividad(forma, con, mapping, usuario, 2);
			}
			else if(estado.equals("insertarUnidad"))
			{
				return this.accionInsertarUnidad(forma, con, mapping, usuario, request);
			}
			else if(estado.equals("eliminarUnidad"))
			{
				return this.accionEliminarUnidad(forma, con, mapping, usuario);
			}
			else if(estado.equals("modificarUnidad"))
			{
				return this.accionModificarUnidad(forma, con, mapping, usuario);
			}
			else if(estado.equals("guardarUnidad"))
			{
				return this.accionGuardarUnidad(forma, con, mapping, usuario);
			}
			else
				/*------------------------------
				* 		ESTADO ==> ORDENAR
				-------------------------------*/
				if (estado.equals("ordenar"))
				{
					accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("unidadAgendaUsuarioCentro");
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
	 * 
	 * @param forma
	 */
	private void accionOrdenarMapa(UnidadAgendaUsuarioCentroForm forma)
	{
		String[] indices = (String[])forma.getUnidadAgendaMap("INDICES");
		int numReg = Integer.parseInt(forma.getUnidadAgendaMap("numRegistros")+"");
		forma.setUnidadAgendaMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getUnidadAgendaMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setUnidadAgendaMap("numRegistros",numReg+"");
		forma.setUnidadAgendaMap("INDICES",indices);
	}
	
	
	/**
	 * Metodo para mostrar los registros de Unidad de Agenda
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(UnidadAgendaUsuarioCentroForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		
		forma.setUnidadesMap(UnidadAgendaUsuarioCentro.consultaUnidades(con, Integer.parseInt(forma.getCentroAtencion().toString())));
		forma.setUsuariosMap(UnidadAgendaUsuarioCentro.consultaUsuarios(con));
		forma.setActividadesMap(UnidadAgendaUsuarioCentro.consultaActividades(con,usuario));
		
		//forma.setUnidadAgendaMap(UnidadAgendaUsuarioCentro.consultaUA1(con, Integer.parseInt(forma.getCentroAtencionP().toString()),forma.getCodigoUsuarioP()));
		//forma.setUnidadAgenda2Map(UnidadAgendaUsuarioCentro.consultaUA(con, Integer.parseInt(forma.getCentroAtencionP().toString()),forma.getCodigoUsuarioP()));
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("unidadAgendaUsuarioCentro");
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarConsultaPrincipal(UnidadAgendaUsuarioCentroForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		boolean error=false;
		
		if(forma.getCodigoUsuarioP().equals("-1"))
    	{
			error=true;
			forma.setMensaje(new ResultadoBoolean(true,"El Usuario es Requerido."));
    	}
		
		if(!error)
		{
			forma.setUnidadAgendaMap(UnidadAgendaUsuarioCentro.consultaUA1(con, Integer.parseInt(forma.getCentroAtencionP().toString()),forma.getCodigoUsuarioP()));
			forma.setUnidadAgenda2Map(UnidadAgendaUsuarioCentro.consultaUA(con, Integer.parseInt(forma.getCentroAtencionP().toString()),forma.getCodigoUsuarioP()));
				
			forma.setLinkSiguiente("");
			UtilidadBD.closeConnection(con);
		}
		return mapping.findForward("unidadAgendaUsuarioCentro");
	}
	
	/**
	 * Metodo para mostrar seccion nuevo
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevoUnidad(UnidadAgendaUsuarioCentroForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		UsuarioBasico user=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		forma.setUnidad("");
		forma.setActividad(new String []{""});
		forma.resetAct();
		forma.setUnidadesMap(UnidadAgendaUsuarioCentro.consultaUnidades(con, Integer.parseInt(forma.getCentroAtencionP().toString())));
		forma.setActividadesMap(UnidadAgendaUsuarioCentro.consultaActividades(con, user));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("unidadAgendaUsuarioCentro");
	}
	
	/**
	 * Metodo para cargar las unidades de consulta segun centro de atencion
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarUnidades(UnidadAgendaUsuarioCentroForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setUnidadesMap(UnidadAgendaUsuarioCentro.consultaUnidades(con, Integer.parseInt(forma.getCentroAtencionP().toString())));
		forma.setEstado("nuevoUnidad");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("unidadAgendaUsuarioCentro");
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionSubirActividad(UnidadAgendaUsuarioCentroForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, int band) 
	{
		String nombre="";		
		int numReg = forma.getActividad().length;
		int pos = 0;
		
		if (numReg==1)
		{
			if(!(forma.getActividad()[0]).equals("") 
					&& !(forma.getActividad()[0]).equals(ConstantesBD.codigoNuncaValido+""))
			{
				pos = UnidadAgendaUsuarioCentro.buscarPosArrayActividades(forma.getActividadesMap(),forma.getActividad()[0]);
				
				nombre = forma.getActividadesMap("nombre_"+pos).toString();
				forma.setActividadesMap("selec_"+pos, ConstantesBD.acronimoSi);
				
				forma.setNumActividades(forma.getNumActividades()+1);				
				forma.setActividadesGenerados("codac_"+forma.getNumActividades(),forma.getActividad()[0]);
				forma.setActividadesGenerados("checkac_"+forma.getNumActividades(), "1");
				forma.setActividadesGenerados("activi_"+forma.getNumActividades(), nombre);
				forma.setActividadesGenerados("numRegistros", forma.getNumActividades());
			}
		}
		else if(numReg>1)
		{
			for (int i=0;i<numReg;i++)
			{
				if(!(forma.getActividad()[i]).equals("") 
						&& !(forma.getActividad()[i]).equals(ConstantesBD.codigoNuncaValido+""))
				{
					pos = UnidadAgendaUsuarioCentro.buscarPosArrayActividades(forma.getActividadesMap(),forma.getActividad()[i]);
					
					nombre = forma.getActividadesMap("nombre_"+pos).toString();
					forma.setActividadesMap("selec_"+pos, ConstantesBD.acronimoSi);
					
					forma.setNumActividades(forma.getNumActividades()+1);				
					forma.setActividadesGenerados("codac_"+forma.getNumActividades(),forma.getActividad()[i]);
					forma.setActividadesGenerados("checkac_"+forma.getNumActividades(), "1");
					forma.setActividadesGenerados("activi_"+forma.getNumActividades(), nombre);
					forma.setActividadesGenerados("numRegistros", forma.getNumActividades());
				}
			}
		}
		
		//reinicia la variable de actividades seleccionadas
		forma.setActividad(new String[]{""});
		
		if(band==2)
			forma.setEstado("modificarUnidad");
		else
			forma.setEstado("nuevoUnidad");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("unidadAgendaUsuarioCentro");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param est
	 */
	private void llenarMundo (UnidadAgendaUsuarioCentroForm forma, UnidadAgendaUsuarioCentro mundo, UsuarioBasico usuario, String est)
	{
		if(est.equals("insertar"))
		{
			mundo.setCentroAtencion(forma.getCentroAtencionP());
			mundo.setUnidad(forma.getUnidad());
			mundo.setUsuario(forma.getUsuario());
			mundo.setInstitucion(usuario.getCodigoInstitucion());
			mundo.setUsuarioM(usuario.getLoginUsuario());
			mundo.setTipoAtencion(forma.getTipoAtencion());
		}
		if(est.equals("modificar"))
		{
			mundo.setCentroAtencion(forma.getCentroAtencionP());
			mundo.setUnidad(forma.getUnidad());
			mundo.setUsuario(forma.getUsuario());
			mundo.setInstitucion(usuario.getCodigoInstitucion());
			mundo.setUsuarioM(usuario.getLoginUsuario());
			mundo.setCodigoM(forma.getUnidadAgendaMap("codigou_"+forma.getIndexMap()).toString());
		}
	}
	
	/**	
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionInsertarUnidad(UnidadAgendaUsuarioCentroForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		UnidadAgendaUsuarioCentro mundo = new UnidadAgendaUsuarioCentro();
		
		errores = validarIngresoDatos(forma);
		
		if(!errores.isEmpty())
		{			
			forma.setEstado("nuevoUnidad");
			saveErrors(request,errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("unidadAgendaUsuarioCentro");			
		}
		
		llenarMundo(forma, mundo, usuario, "insertar");
		if(UnidadAgendaUsuarioCentro.insertarUnidad(con, mundo))
		{
			for(int i=1;i<=Integer.parseInt(forma.getActividadesGenerados("numRegistros").toString());i++)
			{
				if(forma.getActividadesGenerados("checkac_"+i).equals("1"))
				{
					mundo.setActividad(forma.getActividadesGenerados("codac_"+i).toString());
					if(UnidadAgendaUsuarioCentro.insertarActividadxUnidad(con, mundo))
					{
						logger.info("bien>>>>>>"+i);
					}
					else
					{
						logger.info("maallll>>>>>>"+i);
					}
				}
			}
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		}
		
		forma.setUnidadAgendaMap(UnidadAgendaUsuarioCentro.consultaUA1(con, Integer.parseInt(forma.getCentroAtencionP().toString()),forma.getCodigoUsuarioP()));
		forma.setUnidadAgenda2Map(UnidadAgendaUsuarioCentro.consultaUA(con, Integer.parseInt(forma.getCentroAtencionP().toString()),forma.getCodigoUsuarioP()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("unidadAgendaUsuarioCentro");
	}
	
	private ActionErrors validarIngresoDatos(UnidadAgendaUsuarioCentroForm forma)
	{
		ActionErrors errores = new ActionErrors();
		for(int x=0; x<Integer.parseInt(forma.getUnidadAgendaMap().get("numRegistros")+"");x++)
		{
			logger.info("valor de comparacion Unidad >> "+forma.getUnidadAgendaMap().get("cuagenda_"+x).toString()+" >> "+forma.getUnidad());
			logger.info("valor de comparacion Usuario >> "+forma.getUnidadAgendaMap().get("uautorizado_"+x).toString()+" >> "+forma.getUsuario());
			if(forma.getUnidadAgendaMap().get("cuagenda_"+x).toString().equals(forma.getUnidad()) && forma.getUnidadAgendaMap().get("uautorizado_"+x).toString().equals(forma.getUsuario()))
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","El Registro con Unidad de Agenda "+forma.getUnidadAgendaMap().get("uagenda_"+x).toString()+" y Usuario Autorizado "+forma.getUnidadAgendaMap().get("apea_"+x).toString()+" "+forma.getUnidadAgendaMap().get("nombrea_"+x).toString()+" ya fue Ingresado."));
				return errores;
			}
		}
				
		return errores;
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarUnidad(UnidadAgendaUsuarioCentroForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		String [] indicesMap={"uagenda_","uautorizado_","actividad_","puedoEliminar_"};
		
		if(!forma.getIndexMap().equals(""))
    	{
			int pos = Integer.parseInt(forma.getIndexMap());
			if(UnidadAgendaUsuarioCentro.eliminar(con, Integer.parseInt(forma.getUnidadAgendaMap("codigou_"+forma.getIndexMap()).toString())))
			{
				Utilidades.generarLogGenerico(forma.getUnidadAgendaMap(), null, usuario.getLoginUsuario(), true,pos,ConstantesBD.logUnidadAgendaUsuarioCentroCodigo, indicesMap);
    			forma.setMensaje(new ResultadoBoolean(true,"Registro con Unidad de Agenda "+forma.getUnidadAgendaMap("uagenda_"+pos)+" y Usuario Autorizado "+forma.getUnidadAgendaMap("nombrea_"+pos)+" "+forma.getUnidadAgendaMap("apea_"+pos)+" eliminado con exito."));
			}
    	}
		
		forma.setUnidadAgendaMap(UnidadAgendaUsuarioCentro.consultaUA1(con, Integer.parseInt(forma.getCentroAtencionP().toString()),forma.getCodigoUsuarioP()));
		forma.setUnidadAgenda2Map(UnidadAgendaUsuarioCentro.consultaUA(con, Integer.parseInt(forma.getCentroAtencionP().toString()),forma.getCodigoUsuarioP()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("unidadAgendaUsuarioCentro");
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificarUnidad(UnidadAgendaUsuarioCentroForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setActividadesMap(UnidadAgendaUsuarioCentro.consultaActividades(con,usuario));
		int registros=0;
		
		String codigo=forma.getUnidadAgendaMap("codigou_"+forma.getIndexMap()).toString();
		if(!forma.getIndexMap().equals(""))
		{
			forma.setCentroAtencionP(forma.getUnidadAgendaMap("ccatencion_"+forma.getIndexMap()).toString());
			forma.setUnidadesMap(UnidadAgendaUsuarioCentro.consultaUnidades(con, Integer.parseInt(forma.getCentroAtencionP().toString())));
			forma.setUnidad(forma.getUnidadAgendaMap("cuagenda_"+forma.getIndexMap()).toString());
			forma.setUsuario(forma.getUnidadAgendaMap("uautorizado_"+forma.getIndexMap()).toString());
			for(int i=0;i<Integer.parseInt(forma.getUnidadAgenda2Map("numRegistros").toString());i++)
			{
				if(codigo.equals(forma.getUnidadAgenda2Map("codigou_"+i).toString()))
				{
					registros++;
					forma.setActividadesGenerados("codac_"+registros, forma.getUnidadAgenda2Map("cactividad_"+i));
					forma.setActividadesGenerados("checkac_"+registros, "1");
					forma.setActividadesGenerados("activi_"+registros, forma.getUnidadAgenda2Map("actividad_"+i));
					for(int z=0;z<Integer.parseInt(forma.getActividadesMap("numRegistros").toString());z++)
					{
						if(forma.getActividadesMap("codigo_"+z).equals(forma.getActividadesGenerados("codac_"+registros)))
						{
							forma.setActividadesMap("selec_"+z, ConstantesBD.acronimoSi);
						}
					}
				}
			}
			
			for(int i=0;i<(Utilidades.convertirAEntero(forma.getUnidadesMap("numRegistros")+""));i++)
			{
				if(Utilidades.convertirAEntero(forma.getUnidadesMap("codigo_"+i)+"") == Utilidades.convertirAEntero(forma.getUnidad()))
					forma.setTipoAtencion(forma.getUnidadesMap("tipoatencion_"+i)+"");
			}
			
			
			forma.setActividadesGenerados("numRegistros", registros);
			forma.setNumActividades(registros);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("unidadAgendaUsuarioCentro");
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarUnidad(UnidadAgendaUsuarioCentroForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		String [] indicesMap={"uagenda_","uautorizado_","actividad_","puedoEliminar_"};
		int pos = Integer.parseInt(forma.getIndexMap().toString());
		forma.setMensaje(new ResultadoBoolean(false,""));
		UnidadAgendaUsuarioCentro mundo=new UnidadAgendaUsuarioCentro();
		
		HashMap<String, Object> mapaM = new HashMap<String, Object> ();
		mapaM.put("uagenda_"+forma.getIndexMap(), forma.getUnidad());
		mapaM.put("uautorizado_"+forma.getIndexMap(), forma.getUsuario());
		mapaM.put("actividad_"+forma.getIndexMap(), forma.getActividad());
		
		HashMap<String, Object> mapaN = new HashMap<String, Object> ();
		mapaN.put("uagenda_0", forma.getUnidadAgendaMap("uagenda_"+forma.getIndexMap()));
		mapaN.put("uautorizado_0", forma.getUnidadAgendaMap("uautorizado_"+forma.getIndexMap()));
		mapaN.put("actividad_0", forma.getUnidadAgendaMap("actividad_"+forma.getIndexMap()));
		
		llenarMundo(forma, mundo, usuario, "modificar");
		
		if(mundo.modificar(con, mundo))
		{
			for(int p=1;p<=Integer.parseInt(forma.getActividadesGenerados("numRegistros").toString());p++)
			{
				if(forma.getActividadesGenerados("checkac_"+p).equals("0"))
				{
					mundo.eliminarActModificar(con, mundo, Integer.parseInt(forma.getActividadesGenerados("codac_"+p).toString()));
				}
				if(forma.getActividadesGenerados("checkac_"+p).equals("1"))
				{
					mundo.insertarActModificar(con, mundo, Integer.parseInt(forma.getActividadesGenerados("codac_"+p).toString()));
				}
			}
			Utilidades.generarLogGenerico(mapaM, mapaN, usuario.getLoginUsuario(), false,pos,ConstantesBD.logUnidadAgendaUsuarioCentroCodigo, indicesMap);
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"No se pudo realizar la Operaci�n con Exito"));
		}
		
		forma.setUnidadAgendaMap(UnidadAgendaUsuarioCentro.consultaUA1(con, Integer.parseInt(forma.getCentroAtencionP().toString()),forma.getCodigoUsuarioP()));
		forma.setUnidadAgenda2Map(UnidadAgendaUsuarioCentro.consultaUA(con, Integer.parseInt(forma.getCentroAtencionP().toString()),forma.getCodigoUsuarioP()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("unidadAgendaUsuarioCentro");
	}
}