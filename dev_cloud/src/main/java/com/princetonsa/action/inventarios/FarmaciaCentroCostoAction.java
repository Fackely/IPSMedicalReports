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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.inventarios.FarmaciaCentroCostoForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.FarmaciaCentroCosto;

/**
 * Clase para el manejo del workflow de Farmacia Centro Costo
 * Date: 2008-01-22
 * @author lgchavez@princetonsa.com
 */

public class FarmaciaCentroCostoAction extends Action 
{
	
	
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(FarmaciaCentroCostoAction.class);
	
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
    		if(form instanceof FarmaciaCentroCostoForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			FarmaciaCentroCostoForm forma = (FarmaciaCentroCostoForm)form;
    			String estado = forma.getEstado();

    			logger.info("\n\n ESTADO FARMACIA CENTRO COSTO---->"+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			forma.setMensaje(new ResultadoBoolean(false));

    			if(estado == null)
    			{
    				forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), con);
    				logger.warn("Estado no valido dentro del Flujo de Unidad de Procedimiento (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar"))
    			{    			
    				return this.accionEmpezar(forma, con, mapping, usuario);    			
    			}
    			else if(estado.equals("cargarFarmaciaCentroCosto"))
    			{    			
    				return this.accioncargarFarmaciaCentroCosto(forma, con, mapping, usuario); 			
    			}
    			else if(estado.equals("cargarCentrosCosto"))
    			{
    				return this.accionCargarCentroCosto(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("nuevoRegistro"))
    			{
    				return this.accionNuevoRegistro(forma, con, mapping, usuario);
    			}

    			else if(estado.equals("guardarNuevo"))
    			{
    				return this.accionGuardarNuevo(request, forma, con, mapping, usuario);
    			}
    			else if(estado.equals("eliminar"))
    			{
    				return this.accioneliminar(request, forma, con, mapping, usuario);
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
    
    
    
    
    

    private ActionForward accioneliminar(HttpServletRequest request,FarmaciaCentroCostoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {


    	logger.info("ELIMINAR FARM X CC -----> "+forma.getCodigoFarmaciaCc()+"          "+forma.getCodigo()+"         ");
    	
     	
    	
    	String[] indices = {"codigodet_","nomfarmacia_","codcentrocosto_","nombrecc_","codigo_","nombreca_"};//?
    	
    	int pos = ConstantesBD.codigoNuncaValido;
    	
    	for(int i = 0; i< Integer.parseInt(forma.getFarmaciaCentroCostoMap().get("numRegistros").toString()) ; i++)
    		if(forma.getFarmaciaCentroCostoMap().get("codigodet_"+i).equals(forma.getCodigoFarmaciaCc()) && 
    				forma.getFarmaciaCentroCostoMap().get("codigo_"+i).equals(forma.getCodigo()))
    			pos = i;    	
    					
    	logger.info("valor de la posicion eliminada >> "+pos+"MAPA ENTERO"+forma.getFarmaciaCentroCostoMap());
    	    	
    	if(FarmaciaCentroCosto.eliminar(con, forma.getCodigoFarmaciaCc(), forma.getCodigo()))
    	{
    		Utilidades.generarLogGenerico(forma.getFarmaciaCentroCostoMap(), null, usuario.getLoginUsuario(), true,pos,ConstantesBD.logFarmaciaCentroCostoCodigo, indices);
    		forma.setMensaje(new ResultadoBoolean(true,"Operación Realizada con Exito"));
    	}
    	else
    	{
    		ActionErrors errores = new ActionErrors();
    		errores.add("", new ActionMessage("errors.notEspecific", "ERROR EN LA INSERCION"));
    		if(!errores.isEmpty())
    		{
    			saveErrors(request, errores);
    			UtilidadBD.closeConnection(con);
    			return mapping.findForward("farmaciacentrocosto");
    		}
    	}
    		
    		FarmaciaCentroCosto mundo= new FarmaciaCentroCosto();
    		
    		mundo.setCentroAtencion(forma.getCentroAtencion());
    		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
    		mundo.setCentroCosto(forma.getCentroCosto());
    		
    		forma.setFarmaciaCentroCostoMap(FarmaciaCentroCosto.consultar(con, mundo));
        	UtilidadBD.closeConnection(con);
        	return mapping.findForward("farmaciacentrocosto");
    			
    	
	}






	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionGuardarNuevo(HttpServletRequest request, FarmaciaCentroCostoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {

    	boolean siinserta=false;
    	FarmaciaCentroCosto mundo= new FarmaciaCentroCosto();
    	llenarMundo(forma, mundo, usuario);
		siinserta=FarmaciaCentroCosto.insertartrans(con, mundo);
    	boolean exito=false;
		
    	logger.info("\n\n\n\n [EXISTE] --->"+siinserta);
    	
		if(siinserta==true)
		{
			logger.info("\n\n\n\n [Insertar] ");
			exito=FarmaciaCentroCosto.insertar(con, mundo);
		}
		else
		{
			logger.info("\n\n\n\n [Insertar Detalle] ");
			exito=FarmaciaCentroCosto.insertardet(con, mundo);
		}
		
    	mundo.setCentroAtencion(forma.getCentroAtencion());
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		mundo.setCentroCosto(forma.getCentroCosto());
		
		ActionErrors errores = new ActionErrors();
		
		if(exito)
			{
				forma.setMensaje(new ResultadoBoolean(true,"Operación Realizada con Exito"));
			}
		else
			{
				errores.add("", new ActionMessage("errors.notEspecific", "ERROR EN LA INSERCION"));
			}
		
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("farmaciacentrocosto");
		}
		forma.setFarmaciaCentroCostoMap(FarmaciaCentroCosto.consultar(con, mundo));
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("farmaciacentrocosto");
    	
    	
	}





	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */    
    private ActionForward accionNuevoRegistro(FarmaciaCentroCostoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {

    	forma.setCodigo(ConstantesBD.codigoNuncaValido);
		UtilidadBD.closeConnection(con);
    	return mapping.findForward("farmaciacentrocosto");
    	
    	
	}





/**
 * 
 * @param forma
 * @param con
 * @param mapping
 * @param usuario
 * @return
 */
	private ActionForward accionCargarCentroCosto(FarmaciaCentroCostoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	forma.setCentroCostoMap(Utilidades.obtenerCentrosCosto(usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+ConstantesBD.separadorSplit+ConstantesBD.codigoTipoAreaIndirecto, false, forma.getCentroAtencion(),true));
    	 
    	
    	forma.setFarmaciaCentroCostoMap(new HashMap());
    	forma.getFarmaciaCentroCostoMap().put("numRegistros", 0);
    	
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("farmaciacentrocosto");
	}






	private ActionForward accioncargarFarmaciaCentroCosto(FarmaciaCentroCostoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
	
    	FarmaciaCentroCosto mundo= new FarmaciaCentroCosto();
    	
    	mundo.setCentroAtencion(forma.getCentroAtencion());
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		mundo.setCentroCosto(forma.getCentroCosto());
		
		logger.info("forma.getCentroAtencion"+forma.getCentroAtencion());
		
		forma.setFarmaciaCentroCostoMap(FarmaciaCentroCosto.consultar(con, mundo));
		UtilidadBD.closeConnection(con);
    	return mapping.findForward("farmaciacentrocosto");
    
    }




	/**
     * 
     * @param forma
     * @param mundo
     * @param usuario 
     */
	private void llenarMundo(FarmaciaCentroCostoForm forma, FarmaciaCentroCosto mundo, UsuarioBasico usuario) 
	{
		logger.info("valor del codigo >> "+forma.getCodigo()+" "+forma.getCentroAtencion());
		
		mundo.setCodigo(forma.getCodigo());
		mundo.setCentroAtencion(forma.getCentroAtencion());
		mundo.setCentroCosto(forma.getCentroCosto());
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		mundo.setUsuarioModifica(usuario.getLoginUsuario());
		mundo.setCodigo_farmacia_cc(forma.getCodigoFarmaciaCc());
		mundo.setFarmacia(forma.getFarmacia());
		
	}

	
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(FarmaciaCentroCostoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), con);
//		logger.info("MAPA getCentroCostoMap=> "+forma.getCentroCostoMap());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("farmaciacentrocosto");
	}	
}
