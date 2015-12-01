/*
 * @(#)TiposUsuarioCamaAction.java
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

import com.princetonsa.actionform.manejoPaciente.TiposUsuarioCamaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.TiposUsuarioCama;



/**
 * Clase para el manejo del workflow de la funcionalidad
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */
public class TiposUsuarioCamaAction extends Action
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(TiposUsuarioCamaAction.class);
    
    private String[] indices={"codigo_", "nombre_", "codigosexo_", "indsexorestrictivo_", "edadinicial_","edadfinal_","estabd_", "codigoantesmod_"};
    
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
			if(form instanceof TiposUsuarioCamaForm)
			{

				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico"); 
				TiposUsuarioCamaForm forma =(TiposUsuarioCamaForm)form;
				String estado=forma.getEstado();
				logger.warn("\n\n\nEl estado en Tipos Usuario Cama es------->"+estado+"\n");

				if(estado == null)
				{
					forma.reset(); 
					logger.warn("Estado no valido dentro del flujo de Tipos Usuario Cama (null) ");
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
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getTiposUsuarioCamaMap("numRegistros").toString()), response, request, "tiposUsuarioCama.jsp",true);
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
     * @param con
     * @param forma
     * @param mapping
     * @return
     */
    private ActionForward accionEliminar(Connection con, TiposUsuarioCamaForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request) 
    {
    	logger.info("indice-->"+forma.getIndice());
    	boolean puedoEliminar= true;
    	if((forma.getTiposUsuarioCamaMap("estabd_"+forma.getIndice())+"").equals(ConstantesBD.acronimoSi))
    	{	
    		puedoEliminar= true; //ACA TOCA HACER UN METODO PARA EVALUAR SI SE PUEDE ELIMINAR O NO DE LA BD DE DATOS DEPENDIENDO DE QUE SE ESTE USANDO EN EL CONVENIO
		}
    	int ultimaPosMapa=(Integer.parseInt(forma.getTiposUsuarioCamaMap("numRegistros")+"")-1);
    	if(puedoEliminar)
    	{
    		int numRegMapEliminados=Integer.parseInt(forma.getTiposUsuarioCamaEliminadosMap("numRegistros")+"");
    		
    		//poner la informacion en el otro mapa.
    		for(int i=0;i<indices.length;i++)
    		{
    			//solo pasar al mapa los registros que son de BD
    			if((forma.getTiposUsuarioCamaMap("estabd_"+forma.getIndice())+"").trim().equals(ConstantesBD.acronimoSi))
    			{
    				forma.setTiposUsuarioCamaEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getTiposUsuarioCamaMap(indices[i]+""+forma.getIndice()));
    			}
    		}
    		if((forma.getTiposUsuarioCamaMap("estabd_"+forma.getIndice())+"").trim().equals(ConstantesBD.acronimoSi))
    		{
    			forma.setTiposUsuarioCamaEliminadosMap("numRegistros", (numRegMapEliminados+1));
    		}
    		
    		//acomodar los registros del mapa en su nueva posicion
    		for(int i=forma.getIndice();i<ultimaPosMapa;i++)
    		{
    			for(int j=0;j<indices.length;j++)
    			{
    				forma.setTiposUsuarioCamaMap(indices[j]+""+i,forma.getTiposUsuarioCamaMap(indices[j]+""+(i+1)));
    			}
    		}
    		
    		//ahora eliminamos el ultimo registro del mapa.
    		for(int j=0;j<indices.length;j++)
    		{
    			forma.getTiposUsuarioCamaMap().remove(indices[j]+""+ultimaPosMapa);
    		}
    		
    		//ahora actualizamos el numero de registros en el mapa.
    		forma.setTiposUsuarioCamaMap("numRegistros",ultimaPosMapa);
    	}
    	UtilidadBD.closeConnection(con);
    	return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getTiposUsuarioCamaMap("numRegistros").toString()), response, request, "tiposUsuarioCama.jsp",forma.getIndice()==ultimaPosMapa);
	}
	
    /**
     * @param con
     * @param
     */
    private ActionForward accionGuardar(Connection con, TiposUsuarioCamaForm forma,ActionMapping mapping,UsuarioBasico usuario)
    {
    	boolean transacction = UtilidadBD.iniciarTransaccion(con);
		TiposUsuarioCama mundo = new TiposUsuarioCama();
		
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getTiposUsuarioCamaEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarTiposUsuarioCama(con,Integer.parseInt((forma.getTiposUsuarioCamaEliminadosMap("codigoantesmod_"+i)+"").toString().trim()),usuario.getCodigoInstitucionInt()))			
			{				
				 transacction=true;						 
			}
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getTiposUsuarioCamaMap("numRegistros")+"");i++)
		{			
			//modificar			
			
			if((forma.getTiposUsuarioCamaMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{	
				if(this.existeModificacion(con, forma, mundo, Integer.parseInt(forma.getTiposUsuarioCamaMap("codigoantesmod_"+i).toString()),i, usuario ))
				{	
					transacction = mundo.modificarTiposUsuarioCama(con, llenarCrearMundo(forma,i,usuario), Integer.parseInt(forma.getTiposUsuarioCamaMap("codigoantesmod_"+i).toString()), usuario.getCodigoInstitucionInt());
				}	
			}
			
			//insertar
			if((forma.getTiposUsuarioCamaMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				transacction = mundo.insertarTiposUsuarioCama(con, llenarCrearMundo(forma,i,usuario), usuario.getCodigoInstitucionInt());
			}			
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);			
			logger.info("----->INSERTO 100% TIPOS DE USUARIO CAMA");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		return this.accionResumen(forma, mapping, con, usuario);
		
    }
    
	/**
	 * 
	 */
	private ActionForward accionEmpezar(TiposUsuarioCamaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario)
	{
		forma.reset();
		TiposUsuarioCama mundo= new TiposUsuarioCama();
		forma.setTiposUsuarioCamaMap(mundo.consultarTiposUsuarioCama(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	
	/**
     * 
     * @param forma
     */
    private void accionNuevo(TiposUsuarioCamaForm forma) 
    {
    	int pos=Integer.parseInt(forma.getTiposUsuarioCamaMap("numRegistros")+"");
		forma.setTiposUsuarioCamaMap("codigo_"+pos,"");
		forma.setTiposUsuarioCamaMap("nombre_"+pos,"");
		forma.setTiposUsuarioCamaMap("codigosexo_"+pos,"");
		forma.setTiposUsuarioCamaMap("indsexorestrictivo_"+pos,ConstantesBD.acronimoNo);
		forma.setTiposUsuarioCamaMap("edadinicial_"+pos,"");
		forma.setTiposUsuarioCamaMap("edadfinal_"+pos,"");
		forma.setTiposUsuarioCamaMap("estabd_"+pos, ConstantesBD.acronimoNo);
		forma.setTiposUsuarioCamaMap("codigoantesmod_"+pos,"");
		forma.setTiposUsuarioCamaMap("numRegistros", (pos+1)+"");
	}
    
    /**
	 * Unicializa un objeto TiposUsuarioCama con los datos.
	 * @param forma
	 * @param indice
	 * */
	private TiposUsuarioCama llenarCrearMundo(TiposUsuarioCamaForm forma, int indice, UsuarioBasico usuario)
	{
		TiposUsuarioCama mundo = new TiposUsuarioCama();
		
		mundo.setCodigo(Integer.parseInt(forma.getTiposUsuarioCamaMap().get("codigo_"+indice).toString()));
		mundo.setNombre(forma.getTiposUsuarioCamaMap().get("nombre_"+indice).toString());
		mundo.setCodigoSexo(Integer.parseInt(forma.getTiposUsuarioCamaMap("codigosexo_"+indice).toString()));
		mundo.setIndSexoRestrictivo(forma.getTiposUsuarioCamaMap().get("indsexorestrictivo_"+indice).toString());
		mundo.setEdadInicial(Integer.parseInt(forma.getTiposUsuarioCamaMap().get("edadinicial_"+indice).toString()));
		mundo.setEdadFinal(Integer.parseInt(forma.getTiposUsuarioCamaMap().get("edadfinal_"+indice).toString()));
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
	private ActionForward accionResumen(TiposUsuarioCamaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		forma.setEstado("resumen");
		TiposUsuarioCama mundo= new TiposUsuarioCama();
		forma.setTiposUsuarioCamaMap(mundo.consultarTiposUsuarioCama(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
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
	private boolean existeModificacion(Connection con,TiposUsuarioCamaForm forma,TiposUsuarioCama mundo,int codigo,int pos, UsuarioBasico usuario)
	{
		HashMap temp = mundo.consultarTiposUsuarioCamaEspecifico(con, usuario.getCodigoInstitucionInt(), codigo);	
		for(int i=0;i<indices.length;i++)
		{		
			if(temp.containsKey(indices[i]+"0")&&forma.getTiposUsuarioCamaMap().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getTiposUsuarioCamaMap(indices[i]+pos)).toString().trim())))
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
     */
    private void accionOrdenarMapa(TiposUsuarioCamaForm forma) 
    {
    	int numReg=Integer.parseInt(forma.getTiposUsuarioCamaMap("numRegistros")+"");
		forma.setTiposUsuarioCamaMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getTiposUsuarioCamaMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setTiposUsuarioCamaMap("numRegistros",numReg+"");	
	}
	
    /**
     * 
     * @param forma
     * @param mundo
     * @param usuario
     * @param isEliminacion
     * @param pos
     */
    private void generarLog(TiposUsuarioCamaForm forma, TiposUsuarioCama mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getTiposUsuarioCamaEliminadosMap(indices[i]+""+pos)+""):forma.getTiposUsuarioCamaEliminadosMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(mundo.getTiposUsuarioCamaMap().get(indices[i]+"0")+""):mundo.getTiposUsuarioCamaMap().get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getTiposUsuarioCamaMap(indices[i]+""+pos)+""):forma.getTiposUsuarioCamaMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logTiposUsuarioCamaCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
    
	
}
