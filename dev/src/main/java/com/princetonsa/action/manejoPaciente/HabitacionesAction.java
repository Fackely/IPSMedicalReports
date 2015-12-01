/*
 * @(#)HabitacionesAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */

package com.princetonsa.action.manejoPaciente;

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
import util.UtilidadSesion;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.manejoPaciente.HabitacionesForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.Habitaciones;



/**
 * Clase para el manejo del workflow de la funcionalidad
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */

public class HabitacionesAction extends Action
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(HabitacionesAction.class);
    
    private String[] indices={"codigo_", "piso_", "codigotipohabitac_","codigohabitac_", "nombre_", "codigocentroatencion_", "estabd_"};
    /**
	 * Metodo execute del Action
	 */
	public ActionForward execute(   ActionMapping mapping,
	        						ActionForm form,
	        						HttpServletRequest request,
	        						HttpServletResponse response ) throws Exception
	        						{

		Connection con=null;
		try{
			if (response==null);
			if(form instanceof HabitacionesForm)
			{

				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico"); 
				HabitacionesForm forma =(HabitacionesForm)form;
				String estado=forma.getEstado();
				logger.warn("\n\n\nEl estado en Habitaciones es------->"+estado+"\n");

				if(estado == null)
				{
					forma.reset(usuario.getCodigoCentroAtencion()); 
					logger.warn("Estado no valido dentro del flujo de Habitaciones (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, mapping, con, usuario, response, request);
				}
				else if(estado.equals("detalle"))
				{
					return this.accionDetalle(forma, mapping, con, usuario, response, request);
				}   
				else if(estado.equals("nuevo"))
				{
					this.accionNuevo(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getHabitacionesMap("numRegistros").toString()), response, request, "habitaciones.jsp",true);
				}
				else if(estado.equals("eliminar"))
				{
					return this.accionEliminar(con, forma, mapping, usuario, response, request);
				}

				else if(estado.equals("ordenar"))
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
				else if(estado.equals("guardar"))
				{
					//guardamos en BD
					return this.accionGuardar(con, forma, mapping, usuario);
				}
				else if(estado.equals("resumen"))
				{
					return this.accionResumen(forma, mapping, con, usuario);
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
	    
//-------------------------------------------------- METODOS----------------------------------	    
	    
	/**
     * @param con
     * @param
     */
    private ActionForward accionGuardar(Connection con, HabitacionesForm forma,ActionMapping mapping,UsuarioBasico usuario)
    {
    	boolean transacction = UtilidadBD.iniciarTransaccion(con);
		Habitaciones mundo = new Habitaciones();
		
		
		logger.info("HABITACIONES DEL MAPA=> "+forma.getHabitacionesMap());
		
		//eliminar

		
		for(int i=0;i<Integer.parseInt(forma.getHabitacionesEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarHabitaciones(con,Integer.parseInt((forma.getHabitacionesEliminadosMap("codigo_"+i)+"").toString().trim())))				
			{				
				 transacction=true;						 
			}
		
		}	
		
		for(int i=0;i<Integer.parseInt(forma.getHabitacionesMap("numRegistros")+"");i++)
		{			
			//modificar
			
			if((forma.getHabitacionesMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{	
				if(this.existeModificacion(con,forma,mundo,Integer.parseInt(forma.getHabitacionesMap("codigo_"+i).toString()),i))
				{	
					transacction = mundo.modificarHabitaciones(con,llenarCrearMundo(forma,i,usuario));
				}	
			}
			
			//insertar
			
			if((forma.getHabitacionesMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				transacction = mundo.insertarHabitaciones(con, llenarCrearMundo(forma,i,usuario), usuario.getCodigoInstitucionInt());
			}			
    
		}
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);			
			logger.info("----->INSERTO 100% DE HABITACIONES");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		
		return this.accionResumen(forma, mapping, con, usuario);
		
    }
	
	
	/**
     * 
     * @param con
     * @param forma
     * @param mapping
     * @return
     */
    private ActionForward accionEliminar(Connection con, HabitacionesForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request) 
    {
    	logger.info("indice-->"+forma.getIndice());
    	boolean puedoEliminar= true;
    	if(forma.getHabitacionesMap("estabd_"+forma.getIndice()).toString().equals(ConstantesBD.acronimoSi))
    	{	
    		puedoEliminar= true; //ACA TOCA HACER UN METODO PARA EVALUAR SI SE PUEDE ELIMINAR O NO DE LA BD DE DATOS DEPENDIENDO DE QUE SE ESTE USANDO EN EL CONVENIO
		}
    	int ultimaPosMapa=(Integer.parseInt(forma.getHabitacionesMap("numRegistros")+"")-1);
    	if(puedoEliminar)
    	{
    		int numRegMapEliminados=Integer.parseInt(forma.getHabitacionesEliminadosMap("numRegistros")+"");
    		
    		//poner la informacion en el otro mapa.
    		for(int i=0;i<indices.length;i++)
    		{
    			//solo pasar al mapa los registros que son de BD
    			if((forma.getHabitacionesMap("estabd_"+forma.getIndice())+"").trim().equals(ConstantesBD.acronimoSi))
    			{
    				forma.setHabitacionesEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getHabitacionesMap(indices[i]+""+forma.getIndice()));
    			}
    		}
    		if((forma.getHabitacionesMap("estabd_"+forma.getIndice())+"").trim().equals(ConstantesBD.acronimoSi))
    		{
    			forma.setHabitacionesEliminadosMap("numRegistros", (numRegMapEliminados+1));
    		}
    		
    		//acomodar los registros del mapa en su nueva posicion
    		for(int i=forma.getIndice();i<ultimaPosMapa;i++)
    		{
    			for(int j=0;j<indices.length;j++)
    			{
    				forma.setHabitacionesMap(indices[j]+""+i,forma.getHabitacionesMap(indices[j]+""+(i+1)));
    			}
    		}
    		
    		//ahora eliminamos el ultimo registro del mapa.
    		for(int j=0;j<indices.length;j++)
    		{
    			forma.getHabitacionesMap().remove(indices[j]+""+ultimaPosMapa);
    		}
    		
    		//ahora actualizamos el numero de registros en el mapa.
    		forma.setHabitacionesMap("numRegistros",ultimaPosMapa);
    	}
    	UtilidadBD.closeConnection(con);
    	logger.info("LLego");
    	return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getHabitacionesMap("numRegistros").toString()), response, request, "habitaciones.jsp",forma.getIndice()==ultimaPosMapa);
	}
	
    /**
	 * Inicializa un objeto Habitaciones con los datos.
	 * @param forma
	 * @param indice
	 * */
	private Habitaciones llenarCrearMundo(HabitacionesForm forma, int indice, UsuarioBasico usuario)
	{
		Habitaciones mundo = new Habitaciones();		
		mundo.setCentroAtencion(forma.getCentroAtencion());
		mundo.setCodigo(Integer.parseInt(forma.getHabitacionesMap().get("codigo_"+indice).toString()));
		mundo.setPiso(Integer.parseInt(forma.getHabitacionesMap().get("piso_"+indice).toString()));
		mundo.setCodigotipohabitac(forma.getHabitacionesMap().get("codigotipohabitac_"+indice).toString());
		mundo.setCodigohabitac(forma.getHabitacionesMap().get("codigohabitac_"+indice).toString());
		mundo.setNombre(forma.getHabitacionesMap().get("nombre_" + indice).toString());
		mundo.setUsuarioModifica(usuario.getLoginUsuario());
		return mundo;		
	}
	
	/**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionResumen(HabitacionesForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		int centroAtencionTemp= forma.getCentroAtencion();
		//limpiamos el form
		forma.reset(usuario.getCodigoCentroAtencion());
		forma.setCentroAtencion(centroAtencionTemp);
		forma.setEstado("resumen");
		forma.inicializarTags(usuario.getCodigoInstitucionInt());
		forma.setHabitacionesMap(Habitaciones.habitacionesXCentroAtencionTipo(con, forma.getCentroAtencion(), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
     * 
     * @param forma
     */
    private void accionOrdenarMapa(HabitacionesForm forma) 
    {
    	int numReg=Integer.parseInt(forma.getHabitacionesMap("numRegistros")+"");
		forma.setHabitacionesMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getHabitacionesMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setHabitacionesMap("numRegistros",numReg+"");	
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(HabitacionesForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario,HttpServletResponse response, HttpServletRequest request)
	{
		forma.reset(usuario.getCodigoCentroAtencion());
		forma.inicializarTags(usuario.getCodigoInstitucionInt());
		return this.accionDetalle(forma, mapping, con, usuario, response, request);
		//UtilidadBD.closeConnection(con);
		//return mapping.findForward("principal");
		
	}
	
	/**
	 * 
	 * @param forma
	 */
	private void accionNuevo(HabitacionesForm forma) 
	{
		int pos=Integer.parseInt(forma.getHabitacionesMap("numRegistros")+"");
		forma.setHabitacionesMap("codigo_"+pos,ConstantesBD.codigoNuncaValido+"");
		forma.setHabitacionesMap("piso_"+pos,"");
		forma.setHabitacionesMap("codigotipohabitac_"+pos,"");
		forma.setHabitacionesMap("codigohabitac_"+pos,"");
		forma.setHabitacionesMap("nombre_"+pos,"");
		forma.setHabitacionesMap("estabd_"+pos, ConstantesBD.acronimoNo);
		forma.setHabitacionesMap("numRegistros", (pos+1)+"");
		
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionDetalle(HabitacionesForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request) 
	{
		forma.setHabitacionesMap(Habitaciones.habitacionesXCentroAtencionTipo(con, forma.getCentroAtencion(), usuario.getCodigoInstitucionInt()));
		forma.setLinkSiguiente("");
		forma.setPatronOrdenar("");
		UtilidadBD.closeConnection(con);
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getHabitacionesMap("numRegistros").toString()), response, request, "habitaciones.jsp",false);
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param pos
	 * @param usuario
	 * @return
	 */
	private boolean existeModificacion(Connection con, HabitacionesForm forma, Habitaciones mundo, int codigo, int pos)
	{
		HashMap temp = mundo.consultarHabitacionesEspecifico(con, codigo);
		
		for(int i=0;i<indices.length;i++)
		{		
			if(temp.containsKey(indices[i]+"0")&&forma.getHabitacionesMap().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getHabitacionesMap(indices[i]+pos)).toString().trim())))
				{
					logger.info("retorna true");
					return true;
				}				
			}
		}		
		logger.info("retorna false");
		return false;
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param isEliminacion
	 * @param pos
	 */
	private void generarLog(HabitacionesForm forma, Habitaciones mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getHabitacionesEliminadosMap(indices[i]+""+pos)+""):forma.getHabitacionesEliminadosMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(mundo.getHabitacionesMap().get(indices[i]+"0")+""):mundo.getHabitacionesMap().get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getHabitacionesMap(indices[i]+""+pos)+""):forma.getHabitacionesMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logHabitacionesCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
}
