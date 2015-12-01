package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;

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
import util.ValoresPorDefecto;

import com.princetonsa.actionform.manejoPaciente.MotivoCierreAperturaIngresosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.MotivoCierreAperturaIngresos;






public class MotivoCierreAperturaIngresosAction extends Action {
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(MotivoCierreAperturaIngresosAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con = null;
    	try{
    		if(response==null);
    		if(form instanceof MotivoCierreAperturaIngresosForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			MotivoCierreAperturaIngresosForm forma = (MotivoCierreAperturaIngresosForm)form;
    			String estado = forma.getEstado();

    			logger.info("\n\n ESTADO MOTIVO CIERRE APERTURA INGRESOS---->"+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			if(estado == null)
    			{
    				forma.reset(usuario.getCodigoInstitucionInt());
    				logger.warn("Estado no valido dentro del Flujo de Unidad de Procedimiento (null)");
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
    				return this.accionguardarNuevo(forma, con, mapping, usuario);

    			}
    			else if(estado.equals("insertar"))
    			{
    				return this.accioninsertar(forma,con,mapping,usuario, request);
    			}
    			else if(estado.equals("modificarRegistro"))
    			{
    				return this.accionModificarRegistro(forma,con,mapping,usuario);
    			}
    			else if(estado.equals("guardarModificacion"))
    			{
    				return this.accionGuardarModificacion(forma,con,mapping,usuario);
    			}
    			else if(estado.equals("eliminarRegistro"))
    			{
    				return this.accionEliminarRegistro(forma,con, mapping, usuario);
    			}
    			else
    				/*------------------------------
    				 * 		ESTADO ==> ORDENAR
				 -------------------------------*/
    				if (estado.equals("ordenar"))
    				{
    					accionOrdenarMapa(forma);
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("MotivoCierreAperturaIngresos");
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
     */    
    private void accionOrdenarMapa(MotivoCierreAperturaIngresosForm forma) {
    	
    	String[] indices = (String[])forma.getmotivoCierreAperturaIngresosMap("INDICES_MAPA");
		int numReg = Integer.parseInt(forma.getmotivoCierreAperturaIngresosMap("numRegistros")+"");
		forma.setMotivoCierreAperturaIngresosMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getMotivoCierreAperturaIngresosMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setmotivoCierreAperturaIngresosMap("numRegistros",numReg+"");
		forma.setmotivoCierreAperturaIngresosMap("INDICES_MAPA",indices);		

		
	}











/**
 * 
 * @param forma
 * @param con
 * @param mapping
 * @param usuario
 * @return
 */
	private ActionForward accionEliminarRegistro(MotivoCierreAperturaIngresosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
    MotivoCierreAperturaIngresos mundo= new MotivoCierreAperturaIngresos();
    String [] indicesMap={"codigo_","descripcion_","tipo_","activo_","institucion_","usuario_modifica_","fecha_modifica_","hora_modifica_","puedoEliminar_"};
    
    	if(!forma.getCodigom().equals(""))
    	{	
    		    	
             
       	int pos = ConstantesBD.codigoNuncaValido;
        	        	
       	for(int i = 0; i< Integer.parseInt(forma.getMotivoCierreAperturaIngresosMap().get("numRegistros").toString()) ; i++)
        		if(forma.getMotivoCierreAperturaIngresosMap().get("codigo_"+i).equals(forma.getCodigom()))
        			pos = i;
        					
        	 	
    		
    		
    		if(MotivoCierreAperturaIngresos.eliminar(con, forma.getCodigom()))
        	{
    			Utilidades.generarLogGenerico(forma.getMotivoCierreAperturaIngresosMap(), null, usuario.getLoginUsuario(), true,pos,ConstantesBD.logMotivoCierreAperturaIngresoCodigo, indicesMap);
        	}
        	
        	    		
    	 	forma.setMotivoCierreAperturaIngresosMap(MotivoCierreAperturaIngresos.consultar(con, mundo));
    	 	UtilidadBD.closeConnection(con);
        	return mapping.findForward("MotivoCierreAperturaIngresos");
        	
    	}
    	
    	
    	forma.setMotivoCierreAperturaIngresosMap(MotivoCierreAperturaIngresos.consultar(con, mundo));
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("MotivoCierreAperturaIngresos");
    	
	}












	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionGuardarModificacion(MotivoCierreAperturaIngresosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {
		MotivoCierreAperturaIngresos mundo= new MotivoCierreAperturaIngresos();
       	llenarMundo(forma, mundo, usuario);
		if(mundo.modificar(con, mundo))
		{
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(false));
		}
    	forma.setMotivoCierreAperturaIngresosMap(MotivoCierreAperturaIngresos.consultar(con, mundo));
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("MotivoCierreAperturaIngresos");
	}



    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionModificarRegistro(MotivoCierreAperturaIngresosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
    
   
		if(!forma.getCodigom().equals(""))
    	{	
    		
    		for(int w=0; w<Integer.parseInt(forma.getMotivoCierreAperturaIngresosMap().get("numRegistros").toString());w++)
    		{
    		
    			if(forma.getMotivoCierreAperturaIngresosMap().get("codigo_"+w).toString().equals(forma.getCodigom()+""))
    			{
    		   				
    				forma.setCodigo(forma.getMotivoCierreAperturaIngresosMap().get("codigo_"+w).toString());
    				forma.setDescripcion(forma.getMotivoCierreAperturaIngresosMap().get("descripcion_"+w).toString());
    				forma.setTipo(forma.getMotivoCierreAperturaIngresosMap().get("tipo_"+w).toString());
    				forma.setActivo(forma.getMotivoCierreAperturaIngresosMap().get("activo_"+w).toString());
    				forma.setCodigom(forma.getMotivoCierreAperturaIngresosMap().get("codigo_"+w).toString());
    				UtilidadBD.closeConnection(con);
    		    	return mapping.findForward("MotivoCierreAperturaIngresos");
    			}
    		}
    	}
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("MotivoCierreAperturaIngresos");
	}




	/**
     * 
     * @param forma
     * @param mundo
     * @param usuario 
     */
	private void llenarMundo(MotivoCierreAperturaIngresosForm forma, MotivoCierreAperturaIngresos mundo, UsuarioBasico usuario) 
	{
		mundo.setActivo(forma.getActivo());
		mundo.setCodigo(forma.getCodigo());
		mundo.setDescripcion(forma.getDescripcion());
		mundo.setTipo(forma.getTipo());
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		mundo.setUsuarioModifica(usuario.getLoginUsuario());
		mundo.setCodigom(forma.getCodigom());
	}

	private ActionForward accioninsertar(MotivoCierreAperturaIngresosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		
		MotivoCierreAperturaIngresos mundo=new MotivoCierreAperturaIngresos();
		
		errores = validarIngresoDatos(forma);
		
		if(!errores.isEmpty())
		{			
			forma.setEstado("guardarNuevo");
			saveErrors(request,errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("MotivoCierreAperturaIngresos");			
		}
		
		llenarMundo(forma, mundo, usuario);
		

		if(MotivoCierreAperturaIngresos.insertar(con, mundo))
		{
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));	
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(false));
		}
			
		forma.setMotivoCierreAperturaIngresosMap(MotivoCierreAperturaIngresos.consultar(con, mundo));

		UtilidadBD.closeConnection(con);
		return mapping.findForward("MotivoCierreAperturaIngresos");
	}
	
	/**
	 * 
	 * */
	private ActionErrors validarIngresoDatos(MotivoCierreAperturaIngresosForm forma)
	{
		ActionErrors errores = new ActionErrors();
		for(int x=0; x<Integer.parseInt(forma.getMotivoCierreAperturaIngresosMap().get("numRegistros")+"");x++)
		{
			logger.info("valor de comparacion >> "+forma.getMotivoCierreAperturaIngresosMap().get("codigo_"+x).toString()+" >> "+forma.getCodigo());
			if(forma.getMotivoCierreAperturaIngresosMap().get("codigo_"+x).toString().equals(forma.getCodigo()))
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","El codigo '"+forma.getMotivoCierreAperturaIngresosMap().get("codigo_"+x)+"' ya fue Ingresado."));
				return errores;
			}
			if(forma.getMotivoCierreAperturaIngresosMap().get("descripcion_"+x).toString().equals(forma.getDescripcion()) && forma.getMotivoCierreAperturaIngresosMap().get("tipo_"+x).toString().equals(forma.getTipo()))
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","El Registro con Descripcion '"+forma.getMotivoCierreAperturaIngresosMap().get("descripcion_"+x)+"' y Tipo '"+ValoresPorDefecto.getIntegridadDominio(forma.getMotivoCierreAperturaIngresosMap().get("tipo_"+x).toString())+"' ya fue Ingresado."));
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
	private ActionForward accionguardarNuevo(MotivoCierreAperturaIngresosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
    	
		
		logger.info("ENTRO AL ACCION");
		forma.setMensaje(new ResultadoBoolean(false));
		forma.setCodigo("");
		forma.setDescripcion("");
		forma.setTipo("");
		forma.setActivo("1");
		UtilidadBD.closeConnection(con);
    	return mapping.findForward("MotivoCierreAperturaIngresos");
	}



	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(MotivoCierreAperturaIngresosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		MotivoCierreAperturaIngresos mundo= new MotivoCierreAperturaIngresos();
    	
	
		forma.setMotivoCierreAperturaIngresosMap(MotivoCierreAperturaIngresos.consultar(con, mundo));
		
			
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("MotivoCierreAperturaIngresos");
		
	}	
    
	
	
	
}