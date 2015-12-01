package com.princetonsa.action.agendaProcedimiento;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.agendaProcedimiento.ExamenCondiTomaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agendaProcedimiento.ExamenCondiToma;

/**
 * Clase para el manejo del workflow de la funcionalidad
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 */

public class ExamenCondiTomaAction extends Action 
{
	//--- Atributos
	
	/**
	 * Objeto maneja los log de la clase 
	 * */
	Logger logger = Logger.getLogger(ExamenCondiTomaAction.class);
	
	//--- Fin Atributos
	
	//--- Metodos
	
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
		if(response==null);
		if(form instanceof ExamenCondiTomaForm)
		{
			con = UtilidadBD.abrirConexion();
			
			if(con == null)
			{
				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			ExamenCondiTomaForm forma = (ExamenCondiTomaForm)form;
			String estado = forma.getEstado();
			
			logger.warn("\n\n El Estado en ExamenCondiTomaAction es ----> "+estado);
			
			if(estado== null)
			{
				forma.reset();
				logger.warn("\n\n Estado no Valido dentro del Flujo de ExamenCondiTomaAction(null)");
				request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");				
				UtilidadBD.cerrarConexion(con);				
				mapping.findForward("paginaError");
			}
			else
			{
				if(estado.equals("empezar"))
				{
					return this.accionEmpezarConsulta(con,forma, mapping,usuario,"consultar");					
				}
				
				else if(estado.equals("nuevo"))
				{
					UtilidadBD.closeConnection(con);
					this.accionNuevo(forma);	
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getExamenCtMap("numRegistros").toString()), response, request, "examenCondiToma.jsp",true);					
				}
				
				else if(estado.equals("guardar"))
				{
					return this.accionGuardarRegistro(con,forma, mapping,usuario);					
				}
				
				else if(estado.equals("eliminar"))
				{
					return this.accionEliminarCampo(forma, request, response, mapping, usuario.getCodigoInstitucionInt());					
				}
				
				else if(estado.equals("ordenarConsultar"))
				{
					 this.accionOrdenarMapa(forma);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("principal");					
				}
				
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de ExamenCondiTurno -> "+estado);
					request.setAttribute("CodigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.closeConnection(con);
					mapping.findForward("paginaError");
				 }
				
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
	 * Connection con
	 * ExamenCondiTomaForm form
	 * ActionMapping mapping
	 * UsuarioBasico usuario
	 * */
	public ActionForward accionEmpezarConsulta(Connection con, ExamenCondiTomaForm forma, ActionMapping mapping, UsuarioBasico usuario, String estado)
	{		
		forma.reset();
		forma.setExamenCtMap("numRegistros","0");
		forma.setExamenCtEliminadosMap("numRegistros","0");	
		
		forma.setExamenCtMap(ExamenCondiToma.consultarExamentCtBasica(con,"",usuario.getCodigoInstitucionInt()));
		forma.setEstado(estado);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");		
	}
	
	
	/**
	 *@param forma
	 * */
	private void accionNuevo(ExamenCondiTomaForm forma)
	{		
		int pos = Integer.parseInt(forma.getExamenCtMap("numRegistros")+"");
		forma.setExamenCtMap("codigo_"+pos,"");		
		forma.setExamenCtMap("descripcion_"+pos,"");				
		forma.setExamenCtMap("activo_"+pos,ConstantesBD.acronimoSi);
		forma.setExamenCtMap("estabd_"+pos,ConstantesBD.acronimoNo);
		forma.setExamenCtMap("numRegistros",(pos+1)+"");
	}
	
	/**
	 * Connection con
	 * ExamenCondiTomaForm form
	 * ActionMapping mapping
	 * UsuarioBasico usuario
	 * */
	public ActionForward accionGuardarRegistro(Connection con, ExamenCondiTomaForm forma, ActionMapping mapping, UsuarioBasico usuario)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(con);
		String estadoEliminar="ninguno";
		ExamenCondiToma mundo = new ExamenCondiToma();		

		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getExamenCtEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarExamenCt(con,forma.getExamenCtEliminadosMap("codigo_"+i).toString().trim(),usuario.getCodigoInstitucionInt()))			
			{				
				 this.generarlog(forma, new HashMap(), usuario, true, i);
				 estadoEliminar="operacionTrue";
				 transacction=true;						 
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getExamenCtMap("numRegistros")+"");i++)
		{			
			//modificar			
			if((forma.getExamenCtMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi)&&
					this.existeModificacion(con,forma,forma.getExamenCtMap("codigo_"+i).toString(),Integer.parseInt(usuario.getCodigoInstitucion()),i,usuario))
			{				
				transacction = mundo.modificarExamenCt(con,llenarCrearMundo(forma,i,usuario));
				estadoEliminar="operacionTrue";
			}
			
			//insertar
			else if((forma.getExamenCtMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				transacction = mundo.insertarExamenCt(con,llenarCrearMundo(forma,i,usuario));
				estadoEliminar="operacionTrue";
			}			
		}		
	
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);
			logger.info("----->INSERTO 100% ExamenCondiToma");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);			
		}
		
		return accionEmpezarConsulta(con,forma, mapping,usuario,estadoEliminar);	
	}
	
	/**
	 * 
	 * */
	private ActionForward accionResumen(Connection con, ActionMapping mapping, ExamenCondiTomaForm forma, String estadoEliminar)
	{
		forma.reset();
		forma.setExamenCtMap("numRegistros",0);
		forma.setExamenCtEliminadosMap("numRegistro",0);
		forma.setEstado(estadoEliminar);
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("principal");		
	}
	
	
	/**
	 * Inicializa un objeto ExamenCondiToma con los datos.
	 * @param forma
	 * @param indice
	 * */
	private ExamenCondiToma llenarCrearMundo(ExamenCondiTomaForm forma, int indice, UsuarioBasico usuario)
	{
		ExamenCondiToma mundo = new ExamenCondiToma();		
		mundo.setCodigoExamenCt(forma.getExamenCtMap().get("codigo_"+indice).toString());
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		mundo.setDescripcionExamenCt(forma.getExamenCtMap().get("descripcion_"+indice).toString());
		
		mundo.setActivoExamenCt(forma.getExamenCtMap().get("activo_"+indice).toString());		
		mundo.setUsuarioModifica(usuario.getLoginUsuario());		
		mundo.setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mundo.setHoraModifica(UtilidadFecha.getHoraActual());
		return mundo;		
	}
	
	
	/**
	 * verifica si el registro se ha modificado
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param String codigoExamenCT
	 * @param int institucion
	 * @param int pos
	 * */
	private boolean existeModificacion(Connection con,ExamenCondiTomaForm forma, String codigoExamenCt, int institucion, int pos, UsuarioBasico usuario)
	{
		HashMap temp = ExamenCondiToma.consultarExamentCtBasica(con, codigoExamenCt, institucion);
		String[] indices = (String[])forma.getExamenCtMap("INDICES_MAPA");		
				
		for(int i=0;i<indices.length;i++)
		{		
			if(temp.containsKey(indices[i]+"0")&&forma.getExamenCtMap().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getExamenCtMap(indices[i]+pos)).toString().trim())))
				{
					this.generarlog(forma, temp, usuario, false, pos);
					return true;
				}				
			}
		}		
		return false;		
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarCampo(ExamenCondiTomaForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping, int codigoInstitucion) 
	{
		int numRegMapEliminados=Integer.parseInt(forma.getExamenCtEliminadosMap("numRegistros")+"");
		
		int ultimaPosMapa=(Integer.parseInt(forma.getExamenCtMap("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		String[] indices= (String[])forma.getExamenCtMap("INDICES_MAPA");
		
		for(int i=0;i<indices.length;i++)
		{ 
			//solo pasar al mapa los registros que son de BD
			if((forma.getExamenCtMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				forma.setExamenCtEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getExamenCtMap(indices[i]+""+forma.getIndexEliminado()));
			}
		}		
		
		if((forma.getExamenCtMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			forma.setExamenCtEliminadosMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getIndexEliminado();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setExamenCtMap(indices[j]+""+i,forma.getExamenCtMap(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getExamenCtMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setExamenCtMap("numRegistros",ultimaPosMapa);		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getExamenCtMap("numRegistros").toString()), response, request, "examenCondiToma.jsp",forma.getIndexEliminado()==ultimaPosMapa);
	}
	
	
	/**
	 * @param forma
	 * */
	private void accionOrdenarMapa(ExamenCondiTomaForm forma)
	{
		String[] indices = (String[])forma.getExamenCtMap("INDICES_MAPA");
		int numReg = Integer.parseInt(forma.getExamenCtMap("numRegistros")+"");
		forma.setExamenCtMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getExamenCtMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setExamenCtMap("numRegistros",numReg+"");
		forma.setExamenCtMap("INDICES_MAPA",indices);		
	}
	
	/**
	 * @param forma
	 * @param HashMap temp
	 * @param UsuarioBasico usuario
	 * @param isEliminacion
	 * @param pos
	 * */
	private void generarlog(ExamenCondiTomaForm forma, HashMap mapaExamenCtTemp, UsuarioBasico usuario, boolean isEliminacion ,int pos )
	{
		String log = "";
		int tipoLog=0;
		String[] indices= (String[])forma.getExamenCtMap("INDICES_MAPA");
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(forma.getExamenCtEliminadosMap(indices[i]+""+pos)+""):forma.getExamenCtEliminadosMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(mapaExamenCtTemp.get(indices[i]+"0")+""):mapaExamenCtTemp.get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(forma.getExamenCtMap(indices[i]+""+pos)+""):forma.getExamenCtMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logExamenCondiTomaCodigo,log,tipoLog,usuario.getLoginUsuario());	
	}
	//--- Fin Metodos	
}
