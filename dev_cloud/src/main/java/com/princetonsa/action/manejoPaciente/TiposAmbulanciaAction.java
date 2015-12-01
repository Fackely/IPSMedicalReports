/*
 * @(#)TiposAmbulanciaAction.java
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
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.manejoPaciente.TiposAmbulanciaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.TiposAmbulancia;


/**
 * Clase para el manejo del workflow de la funcionalidad
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */
public class TiposAmbulanciaAction extends Action 
{

	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(TiposAmbulanciaAction.class);
    
    private String[] indices={"codigo_", "descripcion_", "servicio_", "nomservicio_", "estabd_", "codigoantesmod_"};
    
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
			if(form instanceof TiposAmbulanciaForm)
			{

				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico"); 
				TiposAmbulanciaForm forma =(TiposAmbulanciaForm)form;
				String estado=forma.getEstado();
				logger.warn("\n\n\nEl estado en Tipos Ambulancia es------->"+estado+"\n");

				if(estado == null)
				{
					forma.reset(); 
					logger.warn("Estado no valido dentro del flujo de Tipos Ambulancia (null) ");
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
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getTiposAmbulanciaMap("numRegistros").toString()), response, request, "tiposAmbulancia.jsp",true);
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
					if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
						return mapping.findForward("principal");
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
     * @param con
     * @param
     */
    private ActionForward accionGuardar(Connection con, TiposAmbulanciaForm forma,ActionMapping mapping,UsuarioBasico usuario)
    {
    	boolean transacction = UtilidadBD.iniciarTransaccion(con);
		TiposAmbulancia mundo = new TiposAmbulancia();
		
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getTiposAmbulanciaEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarTiposAmbulancia(con,(forma.getTiposAmbulanciaEliminadosMap("codigoantesmod_"+i)+"").toString().trim(),usuario.getCodigoInstitucionInt()))			
			{				
				 transacction=true;						 
			}
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getTiposAmbulanciaMap("numRegistros")+"");i++)
		{			
			//modificar			
			
			if((forma.getTiposAmbulanciaMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{	
				if(this.existeModificacion(con, forma, mundo, forma.getTiposAmbulanciaMap("codigoantesmod_"+i).toString(),i, usuario ))
				{	
					transacction = mundo.modificarTiposAmbulancia(con, llenarCrearMundo(forma,i,usuario), forma.getTiposAmbulanciaMap("codigoantesmod_"+i).toString(), usuario.getCodigoInstitucionInt());
				}	
			}
			
			//insertar
			if((forma.getTiposAmbulanciaMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				transacction = mundo.insertarTiposAmbulancia(con, llenarCrearMundo(forma,i,usuario), usuario.getCodigoInstitucionInt());
			}			
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);			
			logger.info("----->INSERTO 100% TIPOS DE AMBULANCIA");
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
    private ActionForward accionEliminar(Connection con, TiposAmbulanciaForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request) 
    {
    	logger.info("indice-->"+forma.getIndice());
    	boolean puedoEliminar= true;
    	if((forma.getTiposAmbulanciaMap("estabd_"+forma.getIndice())+"").equals(ConstantesBD.acronimoSi))
    	{	
    		puedoEliminar= true; //ACA TOCA HACER UN METODO PARA EVALUAR SI SE PUEDE ELIMINAR O NO DE LA BD DE DATOS DEPENDIENDO DE QUE SE ESTE USANDO EN EL CONVENIO
		}
    	int ultimaPosMapa=(Integer.parseInt(forma.getTiposAmbulanciaMap("numRegistros")+"")-1);
    	if(puedoEliminar)
    	{
    		int numRegMapEliminados=Integer.parseInt(forma.getTiposAmbulanciaEliminadosMap("numRegistros")+"");
    		
    		//poner la informacion en el otro mapa.
    		for(int i=0;i<indices.length;i++)
    		{
    			//solo pasar al mapa los registros que son de BD
    			if((forma.getTiposAmbulanciaMap("estabd_"+forma.getIndice())+"").trim().equals(ConstantesBD.acronimoSi))
    			{
    				forma.setTiposAmbulanciaEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getTiposAmbulanciaMap(indices[i]+""+forma.getIndice()));
    			}
    		}
    		if((forma.getTiposAmbulanciaMap("estabd_"+forma.getIndice())+"").trim().equals(ConstantesBD.acronimoSi))
    		{
    			forma.setTiposAmbulanciaEliminadosMap("numRegistros", (numRegMapEliminados+1));
    		}
    		
    		//acomodar los registros del mapa en su nueva posicion
    		for(int i=forma.getIndice();i<ultimaPosMapa;i++)
    		{
    			for(int j=0;j<indices.length;j++)
    			{
    				forma.setTiposAmbulanciaMap(indices[j]+""+i,forma.getTiposAmbulanciaMap(indices[j]+""+(i+1)));
    			}
    		}
    		
    		//ahora eliminamos el ultimo registro del mapa.
    		for(int j=0;j<indices.length;j++)
    		{
    			forma.getTiposAmbulanciaMap().remove(indices[j]+""+ultimaPosMapa);
    		}
    		
    		//ahora actualizamos el numero de registros en el mapa.
    		forma.setTiposAmbulanciaMap("numRegistros",ultimaPosMapa);
    	}
    	UtilidadBD.closeConnection(con);
    	return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getTiposAmbulanciaMap("numRegistros").toString()), response, request, "tiposAmbulancia.jsp",forma.getIndice()==ultimaPosMapa);
	}
	
	
	/**
	 * 
	 */
	private ActionForward accionEmpezar(TiposAmbulanciaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario)
	{
		forma.reset();
		TiposAmbulancia mundo= new TiposAmbulancia();
		forma.setTiposAmbulanciaMap(mundo.consultarTiposAmbulancia(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
     * 
     * @param forma
     */
    private void accionNuevo(TiposAmbulanciaForm forma) 
    {
    	int pos=Integer.parseInt(forma.getTiposAmbulanciaMap("numRegistros")+"");
		forma.setTiposAmbulanciaMap("codigo_"+pos,"");
		forma.setTiposAmbulanciaMap("nombre_"+pos,"");
		forma.setTiposAmbulanciaMap("servicio_"+pos,"");
		forma.setTiposAmbulanciaMap("nomservicio_"+pos,"");
		forma.setTiposAmbulanciaMap("estabd_"+pos, ConstantesBD.acronimoNo);
		forma.setTiposAmbulanciaMap("codigoantesmod_"+pos,"");
		forma.setTiposAmbulanciaMap("numRegistros", (pos+1)+"");
	}
    
    /**
	 * Unicializa un objeto TiposAmbulancia con los datos.
	 * @param forma
	 * @param indice
	 * */
	private TiposAmbulancia llenarCrearMundo(TiposAmbulanciaForm forma, int indice, UsuarioBasico usuario)
	{
		TiposAmbulancia mundo = new TiposAmbulancia();		
		mundo.setCodigo(forma.getTiposAmbulanciaMap().get("codigo_"+indice).toString());
		mundo.setDescripcion(forma.getTiposAmbulanciaMap().get("descripcion_"+indice).toString());
		mundo.setCodigoServicio(Integer.parseInt(forma.getTiposAmbulanciaMap().get("servicio_"+indice).toString()));
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
	private ActionForward accionResumen(TiposAmbulanciaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		forma.setEstado("resumen");
		TiposAmbulancia mundo= new TiposAmbulancia();
		forma.setTiposAmbulanciaMap(mundo.consultarTiposAmbulancia(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
     * 
     * @param forma
     */
    private void accionOrdenarMapa(TiposAmbulanciaForm forma) 
    {
    	int numReg=Integer.parseInt(forma.getTiposAmbulanciaMap("numRegistros")+"");
		forma.setTiposAmbulanciaMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getTiposAmbulanciaMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setTiposAmbulanciaMap("numRegistros",numReg+"");	
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
	private boolean existeModificacion(Connection con,TiposAmbulanciaForm forma,TiposAmbulancia mundo,String codigo,int pos, UsuarioBasico usuario)
	{
		HashMap temp = mundo.consultarTiposAmbulanciaEspecifico(con, usuario.getCodigoInstitucionInt(), codigo);	
		
		for(int i=0;i<indices.length;i++)
		{		
			if(temp.containsKey(indices[i]+"0")&&forma.getTiposAmbulanciaMap().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getTiposAmbulanciaMap(indices[i]+pos)).toString().trim())))
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
    private void generarLog(TiposAmbulanciaForm forma, TiposAmbulancia mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getTiposAmbulanciaEliminadosMap(indices[i]+""+pos)+""):forma.getTiposAmbulanciaEliminadosMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(mundo.getTiposAmbulanciaMap().get(indices[i]+"0")+""):mundo.getTiposAmbulanciaMap().get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getTiposAmbulanciaMap(indices[i]+""+pos)+""):forma.getTiposAmbulanciaMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logTiposAmbulanciaCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
    
}
