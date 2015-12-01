/*
 * @(#)TipoHabitacionAction.java
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

import com.princetonsa.actionform.manejoPaciente.TipoHabitacionForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.TipoHabitacion;

/**
 * Clase para el manejo del workflow de la funcionalidad
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */

public class TipoHabitacionAction extends Action
{

	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(TipoHabitacionAction.class);
    
    private String[] indices={"codigo_", "nombre_", "estabd_", "codigoantesmod_"};
    
    
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
			if(form instanceof TipoHabitacionForm)
			{

				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico"); 
				TipoHabitacionForm forma =(TipoHabitacionForm)form;
				String estado=forma.getEstado();
				logger.warn("\n\n\nEl estado en Tipo Habitacion es------->"+estado+"\n");

				if(estado == null)
				{
					forma.reset(); 
					logger.warn("Estado no valido dentro del flujo de Tipo Habitacion (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, mapping, con, usuario);
				}
				else if(estado.equals("nuevo"))
				{
					this.accionNuevo(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getTipoHabitacionMap("numRegistros").toString()), response, request, "tipoHabitacion.jsp",true);
				}
				else if(estado.equals("eliminar"))
				{
					return this.accionEliminar(con, forma, mapping, usuario, response, request );
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(con, forma, mapping, usuario);
				}
				else if(estado.equals("resumen"))
				{
					return this.accionResumen(forma, mapping, con, usuario);
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
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;  
	}

	
//---------------------------- METODOS----------------------------------
	
	
	/**
	 * 
	 */
	private ActionForward accionEmpezar(TipoHabitacionForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario)
	{
		forma.reset();
		TipoHabitacion mundo= new TipoHabitacion();
		forma.setTipoHabitacionMap(mundo.consultarTipoHabitacion(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
     * 
     * @param forma
     */
    private void accionNuevo(TipoHabitacionForm forma) 
    {
    	int pos=Integer.parseInt(forma.getTipoHabitacionMap("numRegistros")+"");
		forma.setTipoHabitacionMap("codigo_"+pos,"");
		forma.setTipoHabitacionMap("nombre_"+pos,"");
		forma.setTipoHabitacionMap("estabd_"+pos, ConstantesBD.acronimoNo);
		forma.setTipoHabitacionMap("codigoantesmod_"+pos,"");
		forma.setTipoHabitacionMap("numRegistros", (pos+1)+"");
	}
    
    /**
	 * Unicializa un objeto TipoHabitacion con los datos.
	 * @param forma
	 * @param indice
	 * */
	private TipoHabitacion llenarCrearMundo(TipoHabitacionForm forma, int indice, UsuarioBasico usuario)
	{
		TipoHabitacion mundo = new TipoHabitacion();		
		mundo.setCodigo(forma.getTipoHabitacionMap().get("codigo_"+indice).toString());
		mundo.setNombre(forma.getTipoHabitacionMap().get("nombre_"+indice).toString());
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
	private ActionForward accionResumen(TipoHabitacionForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		forma.setEstado("resumen");
		TipoHabitacion mundo= new TipoHabitacion();
		forma.setTipoHabitacionMap(mundo.consultarTipoHabitacion(con, usuario.getCodigoInstitucionInt()));
		//consultarTipoHabitacion(con, usuario.getCodigoInstitucionInt(), ));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
     * 
     * @param forma
     */
    private void accionOrdenarMapa(TipoHabitacionForm forma) 
    {
    	int numReg=Integer.parseInt(forma.getTipoHabitacionMap("numRegistros")+"");
		forma.setTipoHabitacionMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getTipoHabitacionMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setTipoHabitacionMap("numRegistros",numReg+"");	
	}
	
	/**
	 * verifica si el registro se ha modificado
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param String codigo
	 * @param int institucion
	 * @param int pos
	 * */
	private boolean existeModificacion(Connection con,TipoHabitacionForm forma,TipoHabitacion mundo,String codigo,int pos, UsuarioBasico usuario)
	{
		HashMap temp = mundo.consultarTipoHabitacionEspecifico(con, usuario.getCodigoInstitucionInt(), codigo);	
		
		for(int i=0;i<indices.length;i++)
		{		
			if(temp.containsKey(indices[i]+"0")&&forma.getTipoHabitacionMap().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getTipoHabitacionMap(indices[i]+pos)).toString().trim())))
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
     * @param con
     * @param forma
     * @param mapping
     * @return
     */
    private ActionForward accionEliminar(Connection con, TipoHabitacionForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request) 
    {
    	logger.info("indice-->"+forma.getIndice());
    	boolean puedoEliminar= true;
    	if((forma.getTipoHabitacionMap("estabd_"+forma.getIndice())+"").equals(ConstantesBD.acronimoSi))
    	{	
    		puedoEliminar= true; //ACA TOCA HACER UN METODO PARA EVALUAR SI SE PUEDE ELIMINAR O NO DE LA BD DE DATOS DEPENDIENDO DE QUE SE ESTE USANDO EN EL CONVENIO
		}
    	int ultimaPosMapa=(Integer.parseInt(forma.getTipoHabitacionMap("numRegistros")+"")-1);
    	if(puedoEliminar)
    	{
    		int numRegMapEliminados=Integer.parseInt(forma.getTipoHabitacionEliminadosMap("numRegistros")+"");
    		
    		//poner la informacion en el otro mapa.
    		for(int i=0;i<indices.length;i++)
    		{
    			//solo pasar al mapa los registros que son de BD
    			if((forma.getTipoHabitacionMap("estabd_"+forma.getIndice())+"").trim().equals(ConstantesBD.acronimoSi))
    			{
    				forma.setTipoHabitacionEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getTipoHabitacionMap(indices[i]+""+forma.getIndice()));
    			}
    		}
    		if((forma.getTipoHabitacionMap("estabd_"+forma.getIndice())+"").trim().equals(ConstantesBD.acronimoSi))
    		{
    			forma.setTipoHabitacionEliminadosMap("numRegistros", (numRegMapEliminados+1));
    		}
    		
    		//acomodar los registros del mapa en su nueva posicion
    		for(int i=forma.getIndice();i<ultimaPosMapa;i++)
    		{
    			for(int j=0;j<indices.length;j++)
    			{
    				forma.setTipoHabitacionMap(indices[j]+""+i,forma.getTipoHabitacionMap(indices[j]+""+(i+1)));
    			}
    		}
    		
    		//ahora eliminamos el ultimo registro del mapa.
    		for(int j=0;j<indices.length;j++)
    		{
    			forma.getTipoHabitacionMap().remove(indices[j]+""+ultimaPosMapa);
    		}
    		
    		//ahora actualizamos el numero de registros en el mapa.
    		forma.setTipoHabitacionMap("numRegistros",ultimaPosMapa);
    	}
    	UtilidadBD.closeConnection(con);
    	return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getTipoHabitacionMap("numRegistros").toString()), response, request, "tipoHabitacion.jsp",forma.getIndice()==ultimaPosMapa);
	}
    
    /**
     * @param con
     * @param
     */
    private ActionForward accionGuardar(Connection con, TipoHabitacionForm forma,ActionMapping mapping,UsuarioBasico usuario)
    {
    	boolean transacction = UtilidadBD.iniciarTransaccion(con);
		TipoHabitacion mundo = new TipoHabitacion();
		
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getTipoHabitacionEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarTipoHabitacion(con,(forma.getTipoHabitacionEliminadosMap("codigoantesmod_"+i)+"").toString().trim(),usuario.getCodigoInstitucionInt()))			
			{				
				 transacction=true;						 
			}
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getTipoHabitacionMap("numRegistros")+"");i++)
		{			
			//modificar			
			
			if((forma.getTipoHabitacionMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{	
				if(this.existeModificacion(con, forma, mundo, forma.getTipoHabitacionMap("codigoantesmod_"+i).toString(),i, usuario ))
				{	
					transacction = mundo.modificarTipoHabitacion(con, llenarCrearMundo(forma,i,usuario), forma.getTipoHabitacionMap("codigoantesmod_"+i).toString(), usuario.getCodigoInstitucionInt());
				}	
			}
			
			//insertar
			if((forma.getTipoHabitacionMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				transacction = mundo.insertarTipoHabitacion(con, llenarCrearMundo(forma,i,usuario), usuario.getCodigoInstitucionInt());
			}			
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);			
			logger.info("----->INSERTO 100% TIPOS DE HABITACION");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		
		return this.accionResumen(forma, mapping, con, usuario);
		
    }
    
    /**
     * 
     * @param forma
     * @param mundo
     * @param usuario
     * @param isEliminacion
     * @param pos
     */
    private void generarLog(TipoHabitacionForm forma, TipoHabitacion mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getTipoHabitacionEliminadosMap(indices[i]+""+pos)+""):forma.getTipoHabitacionEliminadosMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(mundo.getTipoHabitacionMap().get(indices[i]+"0")+""):mundo.getTipoHabitacionMap().get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getTipoHabitacionMap(indices[i]+""+pos)+""):forma.getTipoHabitacionMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logTipoHabitacionCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
    
}
