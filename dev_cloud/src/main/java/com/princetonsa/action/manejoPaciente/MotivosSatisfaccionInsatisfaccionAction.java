package com.princetonsa.action.manejoPaciente;

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

import com.princetonsa.actionform.manejoPaciente.MotivosSatisfaccionInsatisfaccionForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.MotivosSatisfaccionInsatisfaccion;

/**
 * Clase para el manejo de la parametrizacion 
 * de los motivos de satisfacción e insatisfacción
 * en la atención de pacientes
 * Date: 2008-05-9
 * @author garias@princetonsa.com
 */
public class MotivosSatisfaccionInsatisfaccionAction extends Action 
{
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(MotivosSatisfaccionInsatisfaccionAction.class);
	
	public HashMap mapaOriginal=new HashMap();
	
	String[] indicesMotivosMap = {"codigopk_", "codigo_", "descripcion_", "tipo_", "desctipo_","eliminado_","sepuedeeliminar_",""};
	
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
    		if(form instanceof MotivosSatisfaccionInsatisfaccionForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			MotivosSatisfaccionInsatisfaccionForm forma = (MotivosSatisfaccionInsatisfaccionForm)form;
    			String estado = forma.getEstado();

    			ActionErrors errores = new ActionErrors();

    			logger.info("\n\n\n ESTADO (PARAMETRIZACIÓN MOTIVOS) ---> "+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

    			if(paciente==null)
    				errores.add("Paciente", new ActionMessage("errors.required","Paciente"));

    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado No Valido Dentro Del Flujo de la Parametrización de los Motivos de Satisafacción e Insatisafacción (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else 
    				/*------------------------------
    				 * 		ESTADO > empezar
    				 *-------------------------------*/
    				if(estado.equals("empezar"))
    				{   
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("inicio");	
    				}
    				else
    					/*------------------------------
    					 * 		ESTADO > ingresarModificar
    					 *-------------------------------*/
    					if(estado.equals("ingresarModificar"))
    					{  
    						forma.setMensaje(new ResultadoBoolean(false));
    						forma.setEstadoAux("ingresarModificar");
    						accionConsultar(con, forma, usuario.getCodigoInstitucion());
    						UtilidadBD.closeConnection(con);
    						return mapping.findForward("ingresarModificar");	
    					}
    			/*------------------------------
    			 * 		ESTADO > ingresar
    			 *-------------------------------*/
    			if(estado.equals("ingresar"))
    			{
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("ingresarModificar");	
    			}
    			/*------------------------------
    			 * 		ESTADO > guardarIngreso
    			 *-------------------------------*/
    			if(estado.equals("guardarIngreso"))
    			{
    				errores = accionValidarDatos(forma, errores, Integer.parseInt((forma.getMotivosMap().get("numRegistros").toString())));
    				if(errores.isEmpty())
    					errores = accionGuardarIngreso(con, forma, usuario.getCodigoInstitucion(), usuario.getLoginUsuario(), errores);

    				if(!errores.isEmpty()){
    					saveErrors(request,errores);
    					forma.setEstado("ingresar");
    				}	

    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("ingresarModificar");	
    			}
    			else
    				/*------------------------------
    				 * 		ESTADO > consultar
    				 *-------------------------------*/
    				if(estado.equals("consultar"))
    				{
    					forma.setMensaje(new ResultadoBoolean(false));
    					forma.setEstadoAux("consultar");
    					accionConsultar(con, forma, usuario.getCodigoInstitucion());
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("ingresarModificar");	
    				}
    			/*------------------------------
    			 * 		ESTADO > modificar
    			 *-------------------------------*/
    			if(estado.equals("modificar"))
    			{
    				mapaOriginal=(HashMap)forma.getMotivosMap().clone();
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("ingresarModificar");	
    			}
    			/*------------------------------
    			 * 		ESTADO > guardarCambios
    			 *-------------------------------*/
    			if(estado.equals("guardarCambios"))
    			{
    				errores = accionValidarDatos(forma, errores, forma.getPosMap());
    				if(errores.isEmpty())
    					errores = accionGuardarCambios(con, forma, usuario.getCodigoInstitucion(), usuario.getLoginUsuario(), errores);

    				if(!errores.isEmpty()){
    					saveErrors(request,errores);
    					forma.setEstado("modificar");
    				}	

    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("ingresarModificar");	
    			}
    			/*------------------------------
    			 * 		ESTADO > eliminar
    			 *-------------------------------*/
    			if(estado.equals("eliminar"))
    			{
    				eliminar(con, forma, usuario.getLoginUsuario());
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("ingresarModificar");	
    			}
    			/*------------------------------
    			 * 		ESTADO > ordenar
			 -------------------------------*/
    			if (estado.equals("ordenar"))
    			{
    				accionOrdenar(forma);
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("ingresarModificar");
    			}
    			/*------------------------------
    			 * 		ESTADO > redireccion
			 -------------------------------*/
    			else if (estado.equals("redireccion"))
    			{
    				forma.setEstado(forma.getEstadoAux());
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


	private void accionOrdenar(MotivosSatisfaccionInsatisfaccionForm forma) {
		int numReg = Integer.parseInt(forma.getMotivosMap("numRegistros")+"");
		forma.setMotivosMap(Listado.ordenarMapa(indicesMotivosMap, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getMotivosMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setMotivosMap("numRegistros",numReg+"");
		forma.setMotivosMap("INDICES_MAPA",indicesMotivosMap);
		forma.setEstado(forma.getEstadoAux());
	}


	private ActionErrors accionValidarDatos(MotivosSatisfaccionInsatisfaccionForm forma, ActionErrors errores, int pos) {
		if(forma.getMotivosMap("codigo_"+pos).toString().equals(""))
    		errores.add("Validación codigo", new ActionMessage("errors.required","Código "));
		if(forma.getMotivosMap("descripcion_"+pos).toString().equals(""))
    		errores.add("Validación Descripción", new ActionMessage("errors.required","Descripción "));
		if(forma.getMotivosMap("tipo_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
    		errores.add("Validación Tipo de motivo", new ActionMessage("errors.required","Tipo de motivo "));
		return errores;
	}


	private ActionErrors accionGuardarIngreso(Connection con, MotivosSatisfaccionInsatisfaccionForm forma, String codigoInstitucion, String usuario, ActionErrors errores) {
    	int pos=Integer.parseInt((forma.getMotivosMap().get("numRegistros").toString()));
    	MotivosSatisfaccionInsatisfaccion mundo = new MotivosSatisfaccionInsatisfaccion();
		mundo.setMotivosMap("codigo",forma.getMotivosMap("codigo_"+pos));
		mundo.setInstitucion(codigoInstitucion);
		mundo.setMotivosMap("descripcion",forma.getMotivosMap("descripcion_"+pos));
		mundo.setMotivosMap("tipo",forma.getMotivosMap("tipo_"+pos));
		mundo.setUsuario(usuario);
		if(mundo.ingresar(con, mundo)){
			forma.setMotivosMap(mundo.consultar(con, mundo));
		}
		else
			errores.add("Validación Codigo del Motivo", new ActionMessage("errors.yaExisteAmplio","El código del motivo ",""));
		return errores;
    }	

	/**
     * 
     * @param con
     * @param forma
     */
	private void eliminar(Connection con, MotivosSatisfaccionInsatisfaccionForm forma, String usuario) {
		MotivosSatisfaccionInsatisfaccion mundo = new MotivosSatisfaccionInsatisfaccion();
		mundo.setMotivosMap("codigopk",forma.getMotivosMap("codigopk_"+forma.getPosMap()));
		if(mundo.eliminar(con, mundo))
			forma.setMotivosMap("eliminado_"+forma.getPosMap(), ConstantesBD.acronimoSi);
		Utilidades.generarLogGenerico(forma.getMotivosMap(), null, usuario, true,forma.getPosMap(),ConstantesBD.logMotivosSatisfaccionInsatisfaccionCodigo, indicesMotivosMap);
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param codigoInstitucion
	 * @param usuario
	 * @param errores 
	 */
	private ActionErrors accionGuardarCambios(Connection con, MotivosSatisfaccionInsatisfaccionForm forma, String codigoInstitucion, String usuario, ActionErrors errores) {
		
		MotivosSatisfaccionInsatisfaccion mundo = new MotivosSatisfaccionInsatisfaccion();
		mundo.setMotivosMap("codigo",forma.getMotivosMap("codigo_"+forma.getPosMap()));
		mundo.setMotivosMap("descripcion",forma.getMotivosMap("descripcion_"+forma.getPosMap()));
		mundo.setMotivosMap("tipo",forma.getMotivosMap("tipo_"+forma.getPosMap()));
		mundo.setUsuario(usuario);
		mundo.setMotivosMap("codigopk",forma.getMotivosMap("codigopk_"+forma.getPosMap()));
		if(mundo.modificar(con, mundo)){
			
			forma.setEstadoAux("guardarCambios");
			accionConsultar(con, forma, codigoInstitucion);
			
			forma.setEstado("ingresarModificar");
			
			// Generar Log
			Utilidades.generarLogGenerico(forma.getMotivosMap(), forma.getPosMap(), mapaOriginal, usuario, false, forma.getPosMap(), ConstantesBD.logMotivosSatisfaccionInsatisfaccionCodigo, indicesMotivosMap);
			
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(false));
			errores.add("Validación Codigo del Motivo", new ActionMessage("errors.yaExisteAmplio","El código del motivo ",""));
		}
		return errores;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param codigoInstitucion
	 */
	private void accionConsultar(Connection con, MotivosSatisfaccionInsatisfaccionForm forma, String codigoInstitucion) {
		MotivosSatisfaccionInsatisfaccion mundo = new MotivosSatisfaccionInsatisfaccion();
		mundo.setInstitucion(codigoInstitucion);
		forma.setMotivosMap(mundo.consultar(con, mundo));
		
		//ordeno por codigo
		forma.setUltimoPatron("descripcion_");
		forma.setPatronOrdenar("codigo_");
		accionOrdenar(forma);
	}
    
}