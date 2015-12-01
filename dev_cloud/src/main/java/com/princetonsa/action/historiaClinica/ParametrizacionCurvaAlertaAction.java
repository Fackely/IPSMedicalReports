/*
 * @(#)ParametrizacionCurvaAlertaAction
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.historiaClinica;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
import util.LogsAxioma;
import util.UtilidadBD;

import com.princetonsa.actionform.historiaClinica.ParametrizacionCurvaAlertaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ParametrizacionCurvaAlerta;

/**
 *   Action, controla todas las opciones 
 *  , incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @author wrios
 */
public class ParametrizacionCurvaAlertaAction extends Action
{

    /**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ParametrizacionCurvaAlertaAction.class);
	
	/**
	 * manejo de errores
	 */
	final int ningunError=1, errorUnique=2, errorCodigo=3;
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response ) throws Exception
									{

		Connection con=null;
		try {
			if (response==null); //Para evitar que salga el warning
			if(form instanceof ParametrizacionCurvaAlertaForm)
			{
				con = UtilidadBD.abrirConexion();	
				ParametrizacionCurvaAlertaForm forma =(ParametrizacionCurvaAlertaForm)form;
				String estado=forma.getEstado(); 
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				logger.warn("\n ESTADO ParametrizacionCurvaAlertaAction==========="+estado);

				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());	
					logger.warn("Estado no valido dentro del flujo de  ParametrizacionCurvaAlerta(null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return this.accionEmpezar(forma,mapping, usuario.getCodigoInstitucionInt(), con, false);
				}
				else if(estado.equals("ingresarNuevoElementoMapa"))
				{
					return this.accionIngresarNuevoElementoMapa(forma,con, request, response);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("eliminarElementoMapa"))
				{
					return this.accionEliminarElementoMapa(forma, mapping, con,  response);
				}
				else if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarColumna(con, mapping, forma);
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(forma,mapping,request,usuario,con);
				}
				else
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					logger.warn("Estado no valido dentro del flujo de ParametrizacionCurvaAlerta ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}			
			return null;	
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param codigoInstitucionInt
	 * @param con
	 * @return
	 */
	private ActionForward accionEmpezar(ParametrizacionCurvaAlertaForm forma, ActionMapping mapping, int codigoInstitucionInt, Connection con, boolean mostrarMensaje) 
	{
		//Limpiamos lo que venga del form
		forma.reset(codigoInstitucionInt);
		
		// se hace la simulacion de los tags
		forma.inicializarTags();
		
		//cargamos la informacion existente de la BD
		ParametrizacionCurvaAlerta mundo= new ParametrizacionCurvaAlerta();
	    forma.setMapa(mundo.listado(con,codigoInstitucionInt));
	    forma.setMapaNoModificado((HashMap)forma.getMapa().clone());
	    
	    forma.setMostrarMensaje(mostrarMensaje);
	    
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");	
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionIngresarNuevoElementoMapa(ParametrizacionCurvaAlertaForm forma, Connection con, HttpServletRequest request, HttpServletResponse response) 
	{
		int numRegistros= Integer.parseInt(forma.getMapa("numRegistros").toString());
		forma.setMostrarMensaje(false);
	    forma.setMapa("codigo_"+numRegistros,"-2");
	    forma.setMapa("codigoposicion_"+numRegistros,"");
	    forma.setMapa("nombreposicion_"+numRegistros,"");
	    forma.setMapa("codigoparidad_"+numRegistros,"");
	    forma.setMapa("nombreparidad_"+numRegistros,"");
	    forma.setMapa("codigomembrana_"+numRegistros,"");
	    forma.setMapa("nombremembrana_"+numRegistros,"");
	    forma.setMapa("rangoinicial_"+numRegistros,"");
	    forma.setMapa("rangofinal_"+numRegistros,"");
	    forma.setMapa("valor_"+numRegistros,"");
	    forma.setMapa("activo_"+numRegistros,"true");
	    forma.setMapa("estabd_"+numRegistros,"false");
	    forma.setMapa("eseliminada_"+numRegistros,"false");
	    numRegistros+=1;
	    forma.setMapa("numRegistros", numRegistros+"");
	    UtilidadBD.closeConnection(con);
	    
	    if(request.getParameter("ultimaPagina")==null)
	    {
	        if(numRegistros>(forma.getOffsetHash()+forma.getMaxPageItems()))
		        forma.setOffsetHash(forma.getOffsetHash()+forma.getMaxPageItems());
	        
	        try 
	        {
				response.sendRedirect("parametrizacionCurvaAlerta.jsp?pager.offset="+forma.getOffsetHash());
			} 
	        catch (IOException e) 
			{
				logger.warn("no hizo el send ");
				e.printStackTrace();
			}
	    }
	    else
	    {    
		    String ultimaPagina=request.getParameter("ultimaPagina");
		    int posOffSet=0;
		    posOffSet=ultimaPagina.indexOf("offset=")+7;
		    forma.setOffsetHash((Integer.parseInt(ultimaPagina.substring(posOffSet, ultimaPagina.length() ))));
		    
		    if(numRegistros>(forma.getOffsetHash()+forma.getMaxPageItems()))
		        forma.setOffsetHash(forma.getOffsetHash()+forma.getMaxPageItems());
		    
		    try
		    {
		    	response.sendRedirect(ultimaPagina.substring(0, posOffSet)+forma.getOffsetHash());
		    }
		    catch (IOException e) 
			{
				logger.warn("no hizo el send 2 ");
				e.printStackTrace();
			}
	    }
	    return null;
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param response
	 * @return
	 */
	private ActionForward accionEliminarElementoMapa(ParametrizacionCurvaAlertaForm forma, ActionMapping mapping, Connection con, HttpServletResponse response) 
	{
		forma.setMapa("eseliminada_"+forma.getIndexMapa(),"true");
	    UtilidadBD.closeConnection(con);
	    
	    if(!forma.getLinkSiguiente().equals(""))
	    {	
	        try 
	        {
				response.sendRedirect(forma.getLinkSiguiente());
			} 
	        catch (IOException e) 
	        {
	        	logger.warn("no hizo el send 3 ");
				e.printStackTrace();
			}
	    }    
	    else
	        return mapping.findForward("paginaPrincipal");		
		return null;
	}
	
	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenarColumna(Connection con, ActionMapping mapping, ParametrizacionCurvaAlertaForm forma) 
	{
		forma.setMostrarMensaje(false);
        String[] indices = {
				            "codigo_", 
				            "codigoposicion_", 
				            "nombreposicion_",
				            "codigoparidad_",
				            "nombreparidad_",
				            "codigomembrana_",
				            "nombremembrana_",
				            "rangoinicial_",
				            "rangofinal_",
				            "valor_",
				            "activo_",
				            "estabd_",
				            "eseliminada_"
				           };
        
        int tmp = Integer.parseInt(forma.getMapa("numRegistros")+"");
        forma.setMapa(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapa(),Integer.parseInt(forma.getMapa("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapa("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);
        return mapping.findForward("paginaPrincipal");  
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param con
	 * @return
	 */
	private ActionForward accionGuardar(ParametrizacionCurvaAlertaForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, Connection con) 
	{
		UtilidadBD.iniciarTransaccion(con);
	    int[] validarActualizacionTransaccional=actualizarGuardarNuevosEliminarViejosBD(forma,usuario,con);
	    
	    if(validarActualizacionTransaccional[0]==errorCodigo)
	    {
	        ActionErrors errores = new ActionErrors();
			errores.add("actualización", new ActionMessage("errors.ingresoDatos","los datos de Parametrización Curva de Alerta - Partograma (Transacción)"));
			logger.warn("error en la actualización de Parametrización Curva de Alerta - Partograma con indice-->"+validarActualizacionTransaccional[1]);
			saveErrors(request, errores);
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);									
			return mapping.findForward("paginaPrincipal");
	    }
	    /*else if(validarActualizacionTransaccional[0]== errorUnique)
	    {
	        ActionErrors errores = new ActionErrors();
	        errores.add("actualización (Unique)", new ActionMessage("error.historiaClinica.parametrosCurva.yaExisteUnique", validarActualizacionTransaccional[1]+""));
			logger.warn("error en la actualización de los datos de Parametrización Curva de Alerta - Partograma   (Unique) con indice -->"+validarActualizacionTransaccional[1]);
			saveErrors(request, errores);	
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);									
			return mapping.findForward("paginaPrincipal");
	    }*/
	    else if(validarActualizacionTransaccional[0]== ConstantesBD.excepcionViolacionLlaveForanea)
	    {
	    	ActionErrors errores = new ActionErrors();
	    	//@todo mandar los mensajes
	        errores.add("insert Parametrización Curva de Alerta - Partograma", new ActionMessage("error.registroUtilizado",validarActualizacionTransaccional[1]+"", "en otra funcionalidad"));
			logger.warn("error en la actualización de los datos de Parametrización Curva de Alerta - Partograma   (Unique)");
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
	        return mapping.findForward("paginaPrincipal");
	    }
	    UtilidadBD.finalizarTransaccion(con);
	    
	    return this.accionEmpezar(forma, mapping, usuario.getCodigoInstitucionInt(), con, true);
	}

	/**
	 * Método que comparando el HashMap Original y el modificado,
	 * entonces inserta en BD los que en el hash Map tienen como key del
	 * estaBD y esEliminada los nuevos, y los que han sido eliminados que
	 * estan en BD  estaBD y esEliminada, por otra parte modifica los datos 
	 * pasandolos a un nuevo mapa que contiene solo la info modificada para enviar 
	 * a la BD. 
	 * 
	 * @param forma
	 * @param con
	 * @throws SQLException
	 * 
	 * @return  1-->ningun error 
	 * 			2-->error en unique 
	 * 			3--->error de codigo	
	 */
	private int[] actualizarGuardarNuevosEliminarViejosBD(ParametrizacionCurvaAlertaForm forma, UsuarioBasico usuario, Connection con) 
	{
		ParametrizacionCurvaAlerta mundo= new ParametrizacionCurvaAlerta();
	    
	    boolean insertados=false;
	    int numInsertados=0;
	    
	    int [] combinadoErrorIndice= {ningunError, -1};
	    
	    for(int k=0; k<Integer.parseInt(forma.getMapa("numRegistros").toString()); k++)
	    {
	        /*PRIMERA PARTE PARA LA ELIMINACIÓN DE DATOS QUE ESTAN EN BD*/
	        if(  (forma.getMapa("eseliminada_"+k)+"").equals("true"))
	         {
	             if((forma.getMapa("estabd_"+k)+"").equals("true"))
	             {
	                 insertados=false;
	                    
	                 numInsertados=mundo.eliminar(con, forma.getMapa("codigo_"+k).toString());
	                 if(numInsertados==ConstantesBD.excepcionViolacionLlaveForanea)
	                 {	 
	                	 combinadoErrorIndice[0]=ConstantesBD.excepcionViolacionLlaveForanea;
	                	 combinadoErrorIndice[1]=k;
	                     return combinadoErrorIndice;
	                 }    
	                 else if(numInsertados<=0)
	                 {
	                	 logger.warn("error en la eliminacion de los datos ");
	                	 combinadoErrorIndice[0]=errorCodigo;
	                	 combinadoErrorIndice[1]=k;
	                     return combinadoErrorIndice;
	                 }    
	                 else
	                     generarLog(forma, k, usuario, false);
	             }
	         }
	    } 
	    
	    /*SEGUNDA PARTE PARA LA MODIFICACION DE LOS DATOS QUE ESTAN EN BD*/
		//en este punto se carga el mapa solo con los valores que han sido modificados
		forma.comparar2HashMap();
		
		logger.info("MapaAux-->"+forma.getMapaAux());
		
		if(Integer.parseInt(forma.getMapaAux("numRegistros").toString())>0)
		{
		     for(int k=0; k<Integer.parseInt(forma.getMapaNoModificado("numRegistros").toString());k++)
		     {
		         if(  (forma.getMapa("eseliminada_"+k)+"").equals("false"))
		         {
				     if((forma.getMapa("estabd_"+k)+"").equals("true"))
			         {
				         String tempoCod=forma.getMapaAux("codigo_"+k)+"";
				         if(tempoCod!=null && !tempoCod.equals("") && !tempoCod.equals("null") && !tempoCod.equals("-1"))
				         {
				             insertados=false;
				             
				             ///////se evalua que no exista un registro igual
				             //if(mundo.existeRegistro(con, forma.getCodigoMotivoConsulta(), forma.getMapaAux("consecutivosignosintoma_"+k).toString(), forma.getMapaAux("consecutivocategoriatriage_"+k).toString(), usuario.getCodigoInstitucionInt(), forma.getMapaAux("codigo_"+k).toString()))
				            		 //return errorUnique;
				             numInsertados= mundo.modificar(con, forma.getMapaAux("codigoposicion_"+k).toString(),  forma.getMapaAux("codigoparidad_"+k).toString(), forma.getMapaAux("codigomembrana_"+k).toString(), forma.getMapaAux("rangoinicial_"+k).toString(), forma.getMapaAux("rangofinal_"+k).toString(), forma.getMapaAux("valor_"+k).toString(), forma.getMapaAux("activo_"+k).toString(), usuario.getCodigoInstitucionInt(), forma.getMapaAux("codigo_"+k).toString());
				             
				             if(numInsertados<=0)
                             {
                                 logger.warn("error en la modificacion (Unique)");
                                 combinadoErrorIndice[0]=errorCodigo;
        	                	 combinadoErrorIndice[1]=k;
        	                     return combinadoErrorIndice;
                             }    
			                 generarLog(forma, k, usuario, true);
			             }
			         }  
		         }
		      }
		}    
        
		for(int k=0; k<Integer.parseInt(forma.getMapa("numRegistros").toString()); k++)
		{      
	       /*TERCERA PARTE PARA LA INSERCIÓN DE LOS NUEVOS DATOS*/
		    if(  (forma.getMapa("eseliminada_"+k)+"").equals("false"))
		    {
		        if((forma.getMapa("estabd_"+k)+"").equals("false"))
	            {
	                insertados=false;
	                
	                
	                //////se evalua que no exista un registro igual
	                //if(mundo.existeRegistro(con, forma.getCodigoMotivoConsulta(), forma.getMapa("consecutivosignosintoma_"+k).toString(), forma.getMapa("consecutivocategoriatriage_"+k).toString(), usuario.getCodigoInstitucionInt(), forma.getMapa("codigo_"+k).toString()))
		            	 //return errorUnique;
		            
	                insertados=mundo.insertar(con, forma.getMapa("codigoposicion_"+k).toString(), forma.getMapa("codigoparidad_"+k).toString(), forma.getMapa("codigomembrana_"+k).toString(), forma.getMapa("rangoinicial_"+k).toString(), forma.getMapa("rangofinal_"+k).toString(), forma.getMapa("valor_"+k).toString(), forma.getMapa("activo_"+k).toString(), usuario.getCodigoInstitucionInt() );
	                if(!insertados)
                    {
	                    logger.warn("error en la insercion de los datos");
	                    combinadoErrorIndice[0]=errorCodigo;
	                	combinadoErrorIndice[1]=k;
	                    return combinadoErrorIndice;
                    }     
	                
	             }
	         }
	    }
	    return combinadoErrorIndice;
	}
	
	/**
	 * Método que genera los Logs de Modificación y borrado 
	 * @param forma
	 * @param indexKeyCodigoMapaMod, indice de la llave.
	 * @param usuario, user
	 */
	private void generarLog(	ParametrizacionCurvaAlertaForm forma, int indexKeyCodigoMapaMod, UsuarioBasico usuario, boolean esModificacion)
	{
	    String log;
	        		    
		log="\n           ======INFORMACION ORIGINAL PARAMETROS CURVA DE ALERTA - PARTOGRAMA ===== " +
		"\n*  Código [" +forma.getMapaNoModificado("codigo_"+indexKeyCodigoMapaMod) +"] "+
		"\n*  Código Posicion ["+forma.getMapaNoModificado("codigoposicion_"+indexKeyCodigoMapaMod)+"] "+
		"\n*  Código Paridad ["+forma.getMapaNoModificado("codigoparidad_"+indexKeyCodigoMapaMod)+"] " +
		"\n*  Código Membrana ["+forma.getMapaNoModificado("codigomembrana_"+indexKeyCodigoMapaMod)+"] "+
		"\n*  Rango Inicial ["+forma.getMapaNoModificado("rangoinicial_"+indexKeyCodigoMapaMod)+"] "+
		"\n*  Rango Final ["+forma.getMapaNoModificado("rangofinal_"+indexKeyCodigoMapaMod)+"] "+
		"\n*  Valor ["+forma.getMapaNoModificado("valor_"+indexKeyCodigoMapaMod)+"] "+
		"\n*  Activo ["+forma.getMapaNoModificado("activo_"+indexKeyCodigoMapaMod)+"] "+
				"";
		
		log+="\n========================================================\n\n\n " ;
		
		if(esModificacion)
	    {   	
			log+="\n          =====INFORMACION DESPUES DE LA MODIFICACION PARAMETROS CURVA DE ALERTA - PARTOGRAMA===== " +
			"\n*  Código [" +forma.getMapaAux("codigo_"+indexKeyCodigoMapaMod) +"] "+
			"\n*  Código Posicion ["+forma.getMapaAux("codigoposicion_"+indexKeyCodigoMapaMod)+"] "+
			"\n*  Código Paridad ["+forma.getMapaAux("codigoparidad_"+indexKeyCodigoMapaMod)+"] " +
			"\n*  Código Membrana ["+forma.getMapaAux("codigomembrana_"+indexKeyCodigoMapaMod)+"] "+
			"\n*  Rango Inicial ["+forma.getMapaAux("rangoinicial_"+indexKeyCodigoMapaMod)+"] "+
			"\n*  Rango Final ["+forma.getMapaAux("rangofinal_"+indexKeyCodigoMapaMod)+"] "+
			"\n*  Valor ["+forma.getMapaAux("valor_"+indexKeyCodigoMapaMod)+"] "+
			"\n*  Activo ["+forma.getMapaAux("activo_"+indexKeyCodigoMapaMod)+"] "+
					"";
			log+="\n========================================================\n\n\n " ;	
			
			LogsAxioma.enviarLog(ConstantesBD.logParametrizacionCurvaAlertaNombreCodigo, log, ConstantesBD.tipoRegistroLogModificacion,usuario.getInformacionGeneralPersonalSalud()); 
	    }
	    else
	    {
	        log+="\n========================================> FUE ELIMINADO\n\n\n " ;	
			LogsAxioma.enviarLog(ConstantesBD.logParametrizacionCurvaAlertaNombreCodigo, log, ConstantesBD.tipoRegistroLogEliminacion,usuario.getInformacionGeneralPersonalSalud());
	    }
	}
		
}