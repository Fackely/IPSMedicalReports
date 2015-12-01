/*
 * @(#)PisosAction.java
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

import com.princetonsa.actionform.manejoPaciente.PisosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.Pisos;

/**
 * Clase para el manejo del workflow de la funcionalidad
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */

public class PisosAction extends Action
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(PisosAction.class);
    
    private String[] indices={"codigo_", "codigopiso_", "nombre_", "codigocentroatencion_", "estabd_"};
	
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
			if(form instanceof PisosForm)
			{

				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico"); 
				PisosForm forma =(PisosForm)form;
				String estado=forma.getEstado();
				logger.warn("\n\n\nEl estado en Pisos es------->"+estado+"\n");

				if(estado == null)
				{
					forma.reset(usuario.getCodigoCentroAtencion()); 
					logger.warn("Estado no valido dentro del flujo de Pisos (null) ");
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
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getPisosMap("numRegistros").toString()), response, request, "pisos.jsp",true);
				}
				else if(estado.equals("eliminar"))
				{
					return this.accionEliminar(con, forma, mapping, usuario, response, request );
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
					return this.accionGuardar(con,forma,mapping,usuario);
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

//	------------------------- METODOS----------------------------------
	
	/**
     * @param con
     * @param
     */
    private ActionForward accionGuardar(Connection con, PisosForm forma,ActionMapping mapping,UsuarioBasico usuario)
    {
    	boolean transacction = UtilidadBD.iniciarTransaccion(con);
		Pisos mundo = new Pisos();
		
		
		logger.info("PISOS DEL MAPA=> "+forma.getPisosMap());
		
		//eliminar

		
		for(int i=0;i<Integer.parseInt(forma.getPisosEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarPisos(con,Integer.parseInt((forma.getPisosEliminadosMap("codigo_"+i)+"").toString().trim())))			
			{				
				 transacction=true;						 
			}
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getPisosMap("numRegistros")+"");i++)
		{			
			//modificar
			if((forma.getPisosMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{	
				if(this.existeModificacion(con, forma, mundo, Integer.parseInt(forma.getPisosMap("codigo_"+i).toString()), i ))
				{	
					transacction = mundo.modificarPisos(con,llenarCrearMundo(forma,i,usuario));
				}	
			}
			
			//insertar
			
			if((forma.getPisosMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				transacction = mundo.insertarPisos(con, llenarCrearMundo(forma,i,usuario), usuario.getCodigoInstitucionInt());
			}			
    
		}
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);			
			logger.info("----->INSERTO 100% DE PISOS");
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
    private ActionForward accionEliminar(Connection con, PisosForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request) 
    {
    	logger.info("indice-->"+forma.getIndice());
    	boolean puedoEliminar= true;
    	if(forma.getPisosMap("estabd_"+forma.getIndice()).toString().equals(ConstantesBD.acronimoSi))
    	{	
    		puedoEliminar= true; //ACA TOCA HACER UN METODO PARA EVALUAR SI SE PUEDE ELIMINAR O NO DE LA BD DE DATOS DEPENDIENDO DE QUE SE ESTE USANDO EN EL CONVENIO
		}
    	int ultimaPosMapa=(Integer.parseInt(forma.getPisosMap("numRegistros")+"")-1);
    	if(puedoEliminar)
    	{
    		int numRegMapEliminados=Integer.parseInt(forma.getPisosEliminadosMap("numRegistros")+"");
    		
    		//poner la informacion en el otro mapa.
    		for(int i=0;i<indices.length;i++)
    		{
    			//solo pasar al mapa los registros que son de BD
    			if((forma.getPisosMap("estabd_"+forma.getIndice())+"").trim().equals(ConstantesBD.acronimoSi))
    			{
    				forma.setPisosEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getPisosMap(indices[i]+""+forma.getIndice()));
    			}
    		}
    		if((forma.getPisosMap("estabd_"+forma.getIndice())+"").trim().equals(ConstantesBD.acronimoSi))
    		{
    			forma.setPisosEliminadosMap("numRegistros", (numRegMapEliminados+1));
    		}
    		
    		//acomodar los registros del mapa en su nueva posicion
    		for(int i=forma.getIndice();i<ultimaPosMapa;i++)
    		{
    			for(int j=0;j<indices.length;j++)
    			{
    				forma.setPisosMap(indices[j]+""+i,forma.getPisosMap(indices[j]+""+(i+1)));
    			}
    		}
    		
    		//ahora eliminamos el ultimo registro del mapa.
    		for(int j=0;j<indices.length;j++)
    		{
    			forma.getPisosMap().remove(indices[j]+""+ultimaPosMapa);
    		}
    		
    		//ahora actualizamos el numero de registros en el mapa.
    		forma.setPisosMap("numRegistros",ultimaPosMapa);
    	}
    	UtilidadBD.closeConnection(con);
    	return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getPisosMap("numRegistros").toString()), response, request, "pisos.jsp",forma.getIndice()==ultimaPosMapa);
	}

    
    /**
	 * Inicializa un objeto Pisos con los datos.
	 * @param forma
	 * @param indice
	 * */
	private Pisos llenarCrearMundo(PisosForm forma, int indice, UsuarioBasico usuario)
	{
		Pisos mundo = new Pisos();		
		mundo.setCentroAtencion(forma.getCentroAtencion());
		mundo.setCodigo(Integer.parseInt(forma.getPisosMap().get("codigo_"+indice).toString()));
		mundo.setCodigopiso(forma.getPisosMap().get("codigopiso_"+indice).toString());
		logger.info("NOMBRE DEL PISO EN MUNDO=> "+forma.getPisosMap().get("nombre_"+indice).toString()+", EN INDICE=> "+indice);
		mundo.setNombre(forma.getPisosMap().get("nombre_"+indice).toString());
		mundo.setUsuarioModifica(usuario.getLoginUsuario());
		return mundo;		
	}
	
	
	/**
	 * 
	 * @param forma
	 */
	private void accionNuevo(PisosForm forma) 
	{
		int pos=Integer.parseInt(forma.getPisosMap("numRegistros")+"");
		forma.setPisosMap("codigo_"+pos,ConstantesBD.codigoNuncaValido+"");
		forma.setPisosMap("codigopiso_"+pos,"");
		forma.setPisosMap("nombre_"+pos,"");
		forma.setPisosMap("estabd_"+pos, ConstantesBD.acronimoNo);
		forma.setPisosMap("numRegistros", (pos+1)+"");
	}

	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request 
	 * @param response 
	 * @return
	 */
	private ActionForward accionEmpezar(PisosForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request)
	{
		forma.reset(usuario.getCodigoCentroAtencion());
		forma.inicializarTags(usuario.getCodigoInstitucionInt());
		return this.accionDetalle(forma, mapping, con, usuario, response, request);
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request 
	 * @param response 
	 * @return
	 */
	private ActionForward accionDetalle(PisosForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request) 
	{
		forma.setPisosMap(Pisos.pisosXCentroAtencionTipo(con, forma.getCentroAtencion(), usuario.getCodigoInstitucionInt()));
		forma.setLinkSiguiente("");
		forma.setPatronOrdenar("");
		UtilidadBD.closeConnection(con);
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getPisosMap("numRegistros").toString()), response, request, "pisos.jsp",false);
	}

	
	/**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionResumen(PisosForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		int centroAtencionTemp= forma.getCentroAtencion();
		//limpiamos el form
		forma.reset(usuario.getCodigoCentroAtencion());
		forma.setCentroAtencion(centroAtencionTemp);
		forma.setEstado("resumen");
		forma.inicializarTags(usuario.getCodigoInstitucionInt());
		forma.setPisosMap(Pisos.pisosXCentroAtencionTipo(con, forma.getCentroAtencion(), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
     * 
     * @param forma
     */
    private void accionOrdenarMapa(PisosForm forma) 
    {
    	int numReg=Integer.parseInt(forma.getPisosMap("numRegistros")+"");
		forma.setPisosMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getPisosMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setPisosMap("numRegistros",numReg+"");	
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
	private boolean existeModificacion(Connection con, PisosForm forma, Pisos mundo, int codigo, int pos)
	{
		HashMap temp = mundo.consultarPisosEspecifico(con, codigo);
		
		logger.info("mapaTemp-->"+temp);
		
		for(int i=0;i<indices.length;i++)
		{		
			if(temp.containsKey(indices[i]+"0")&&forma.getPisosMap().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getPisosMap(indices[i]+pos)).toString().trim())))
				{
					logger.info("temp->"+temp.get(indices[i]+"0").toString().trim()+" forma-->"+(forma.getPisosMap(indices[i]+pos)).toString().trim());
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
	private void generarLog(PisosForm forma, Pisos mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getPisosEliminadosMap(indices[i]+""+pos)+""):forma.getPisosEliminadosMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(mundo.getPisosMap().get(indices[i]+"0")+""):mundo.getPisosMap().get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getPisosMap(indices[i]+""+pos)+""):forma.getPisosMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logPisosCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
	

}
	    
	    
	    
	    