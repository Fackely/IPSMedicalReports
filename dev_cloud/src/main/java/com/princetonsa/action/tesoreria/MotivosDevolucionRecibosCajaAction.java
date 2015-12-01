package com.princetonsa.action.tesoreria;

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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.tesoreria.MotivosDevolucionRecibosCajaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.tesoreria.MotivosDevolucionRecibosCaja;

public class MotivosDevolucionRecibosCajaAction extends Action
{
	Logger logger = Logger.getLogger(MotivosDevolucionRecibosCajaAction.class);	
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{

		Connection con = null;
		try{


			if(response==null);
			if(form instanceof MotivosDevolucionRecibosCajaForm)
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{	
					request.setAttribute("CodigoDescripcionError","errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				MotivosDevolucionRecibosCajaForm forma = (MotivosDevolucionRecibosCajaForm)form;
				String estado = forma.getEstado();

				ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				logger.info("\n\n ESTADO MOTIVOS DEVOLUCION RECIBOS CAJA---->"+estado+"\n\n");

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, con, mapping, usuario);
				}
				else if(estado.equals("guardarNuevo"))
				{
					return this.accionGuardarNuevo(con,mapping,forma,usuario);
				}
				else if(estado.equals("insertar"))
				{
					return this.accionInsertar(con, request,mapping,forma,usuario);
				}
				else if(estado.equals("modificarRegistro"))
				{
					return this.accionModificarMotivoD(con,mapping,forma,usuario);
				}
				else if(estado.equals("guardarModificacion"))
				{
					return this.accionGuardarModificacion(con,mapping,forma,usuario);
				}
				else if(estado.equals("eliminarRegistro"))
				{
					return this.accionEliminarMotivoD(con,mapping,forma,usuario);
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
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(MotivosDevolucionRecibosCajaForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		
		forma.setMotDevRCMap(MotivosDevolucionRecibosCaja.consultaMotivosD(con, usuario.getCodigoInstitucionInt()));
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("MotivosDevolucionRecibosCaja");
		
	}
	
	/**
	 * 
	 */
	private ActionForward accionGuardarNuevo(Connection con, ActionMapping mapping, MotivosDevolucionRecibosCajaForm forma, UsuarioBasico usuario)
	{
		forma.setCodigo("");
		forma.setDescripcion("");
		forma.setMotDevRCMap(MotivosDevolucionRecibosCaja.consultaMotivosD(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("MotivosDevolucionRecibosCaja");
	}
	
	public void llenarmundo(MotivosDevolucionRecibosCajaForm forma, MotivosDevolucionRecibosCaja mundo, UsuarioBasico usuario, String estadol)
	{
		mundo.setCodigo(forma.getCodigo());
		mundo.setDescripcion(forma.getDescripcion());
		mundo.setActivo(forma.getActivo());
		mundo.setInstitucion(usuario.getCodigoInstitucion());
		mundo.setUsuarioModifica(usuario.getLoginUsuario());
		if(estadol.equals("modificar"))
			mundo.setCodigoAux(forma.getMotDevRCMap().get("codigo_"+forma.getIndexMap()).toString());
	}
	
	
	/**
	 * 
	 * */
	private ActionErrors validarIngresoDatos(MotivosDevolucionRecibosCajaForm forma)
	{
		ActionErrors errores = new ActionErrors();
		for(int x=0; x<Integer.parseInt(forma.getMotDevRCMap().get("numRegistros")+"");x++)
		{
			logger.info("valor de comparacion >> "+forma.getMotDevRCMap().get("codigo_"+x).toString()+" >> "+forma.getCodigo());
			if(forma.getMotDevRCMap().get("codigo_"+x).toString().equals(forma.getCodigo()))
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","El codigo "+forma.getMotDevRCMap().get("codigo_"+x)+" ya fue Ingresado."));
				return errores;
			}
		}
				
		return errores;		
	}
	
	/**
	 * 
	 */
	private ActionForward accionInsertar(Connection con, HttpServletRequest request, ActionMapping mapping, MotivosDevolucionRecibosCajaForm forma, UsuarioBasico usuario){		
		ActionErrors errores = new ActionErrors();
		
		MotivosDevolucionRecibosCaja mundo = new MotivosDevolucionRecibosCaja();
		
		errores = validarIngresoDatos(forma);
		
		if(!errores.isEmpty())
		{			
			forma.setEstado("guardarNuevo");
			saveErrors(request,errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("MotivosDevolucionRecibosCaja");			
		}			
		
		llenarmundo(forma, mundo, usuario, "insertar");
		
		if(MotivosDevolucionRecibosCaja.insertar(con, mundo))		
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		
		forma.setMotDevRCMap(MotivosDevolucionRecibosCaja.consultaMotivosD(con, usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("MotivosDevolucionRecibosCaja");
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionModificarMotivoD(Connection con, ActionMapping mapping, MotivosDevolucionRecibosCajaForm forma, UsuarioBasico usuario) {
		if(!forma.getIndexMap().equals(""))
    	{
    		for(int w=0; w<Integer.parseInt(forma.getMotDevRCMap().get("numRegistros").toString());w++)
    		{
    			if(Integer.parseInt(forma.getIndexMap().toString()) == w)
    			{
    				forma.setCodigo(forma.getMotDevRCMap().get("codigo_"+w).toString());
    				forma.setDescripcion(forma.getMotDevRCMap().get("descripcion_"+w).toString());
    				if(forma.getMotDevRCMap().get("activo_"+w).equals("S")){
    					forma.setActivo("1");
    				}
    				else{
    					forma.setActivo("0");
    				}
    				UtilidadBD.closeConnection(con);
    		    	return mapping.findForward("MotivosDevolucionRecibosCaja");
    			}
    		}
    	}
		UtilidadBD.closeConnection(con);
    	return mapping.findForward("MotivosDevolucionRecibosCaja");
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionGuardarModificacion(Connection con, ActionMapping mapping, MotivosDevolucionRecibosCajaForm forma, UsuarioBasico usuario){
		String [] indicesMap={"codigo_","descripcion_","activo_","puedo_eliminar_"};
		int pos = Integer.parseInt(forma.getIndexMap().toString());
		forma.setMensaje(new ResultadoBoolean(false,""));
		MotivosDevolucionRecibosCaja mundo = new MotivosDevolucionRecibosCaja();
		
		HashMap<String, Object> mapaM = new HashMap<String, Object> ();
		mapaM.put("codigo_"+forma.getIndexMap(), forma.getCodigo());
		mapaM.put("descripcion_"+forma.getIndexMap(), forma.getDescripcion());
		if(forma.getActivo().equals("1"))
		{
			mapaM.put("activo_"+forma.getIndexMap(), "S");
		}
		else{
			mapaM.put("activo_"+forma.getIndexMap(), "N");
		}
		mapaM.put("INDICES",indicesMap);
		
		HashMap<String, Object> mapaN = new HashMap<String, Object> ();
		mapaN.put("codigo_0", forma.getMotDevRCMap("codigo_"+forma.getIndexMap()));
		mapaN.put("descripcion_0", forma.getMotDevRCMap("descripcion_"+forma.getIndexMap()));
		mapaN.put("activo_0", forma.getMotDevRCMap("activo_"+forma.getIndexMap()));
		mapaN.put("INDICES",indicesMap);
		
		llenarmundo(forma, mundo, usuario, "modificar");
		if(mundo.modificar(con, mundo))
		{
			Utilidades.generarLogGenerico(mapaM, mapaN, usuario.getLoginUsuario(), false,pos,ConstantesBD.logMotivosDevolucionRecibosCajaCodigo, indicesMap);
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas Con Exito"));
		}
		
		forma.setMotDevRCMap(MotivosDevolucionRecibosCaja.consultaMotivosD(con, usuario.getCodigoInstitucionInt()));			
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("MotivosDevolucionRecibosCaja");
	}
	
	/**
	 * Metodo utilizado para la eliminacion de tipos de tratamientos
	 * @param con
	 * @param mapping
	 * @param sustitutosNoPosForm
	 * @return
	 */
	private ActionForward accionEliminarMotivoD(Connection con, ActionMapping mapping, MotivosDevolucionRecibosCajaForm forma, UsuarioBasico usuario){
		String [] indicesMap={"codigo_","descripcion_","activo_","puedo_eliminar_"};
		
		if(!forma.getIndexMap().equals(""))
    	{
			int pos = Integer.parseInt(forma.getIndexMap());			
						
			if(MotivosDevolucionRecibosCaja.eliminar(con, forma.getMotDevRCMap("codigo_"+forma.getIndexMap()).toString()))
        	{
    			Utilidades.generarLogGenerico(forma.getMotDevRCMap(), null, usuario.getLoginUsuario(), true,pos,ConstantesBD.logMotivosDevolucionRecibosCajaCodigo, indicesMap);
    			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
        	}
        	
			forma.setMotDevRCMap(MotivosDevolucionRecibosCaja.consultaMotivosD(con, usuario.getCodigoInstitucionInt()));
    	 	UtilidadBD.closeConnection(con);
        	return mapping.findForward("MotivosDevolucionRecibosCaja");
    	}
		forma.setMotDevRCMap(MotivosDevolucionRecibosCaja.consultaMotivosD(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("MotivosDevolucionRecibosCaja");
	}
}