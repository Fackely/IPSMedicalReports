/*
 * @(#)SignosSintomasXSistemaAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.historiaClinica;

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
import util.UtilidadTexto;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.actionform.historiaClinica.SignosSintomasXSistemaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.SignosSintomasXSistema;

/**
 *   Action, controla todas las opciones dentro 
 * 	de los signos sintomas x sistema 
 *  , incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, May 31, 2006
 * @author wrios
 */
public class SignosSintomasXSistemaAction extends Action
{

    /**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(SignosSintomasXSistemaAction.class);
	
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
			if(form instanceof SignosSintomasXSistemaForm)
			{
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}
				SignosSintomasXSistemaForm forma =(SignosSintomasXSistemaForm)form;
				String estado=forma.getEstado(); 
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				logger.warn("\n ESTADO signos sintomas x sistema==========="+estado);

				if(estado == null)
				{
					forma.reset(true);	
					logger.warn("Estado no valido dentro del flujo de Signos y Sintomas x Sistema (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return this.accionEmpezar(forma,mapping, usuario.getCodigoInstitucionInt(), con);
				}
				else if (estado.equals("empezarIngresarModificarEliminar"))
				{
					return this.accionEmpezarIngresarModificarEliminar(forma,mapping, usuario.getCodigoInstitucionInt(), con, false);
				}
				else if(estado.equals("ingresarNuevoElementoMapa"))
				{
					return this.accionIngresarNuevoElementoMapa(forma,con, request, response);
				}
				else if(estado.equals("eliminarElementoMapa"))
				{
					return this.accionEliminarElementoMapa(forma, mapping, con,  response);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(forma,mapping,request,usuario,con);
				}
				else if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarColumna(con, mapping, forma);
				}
				else
				{
					forma.reset(true);
					logger.warn("Estado no valido dentro del flujo de Sintomas y signos x sistema (null) ");
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
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param forma 
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "signosSintomasXSistema.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(	SignosSintomasXSistemaForm forma, 
											ActionMapping mapping,
											int codigoInstitucion,
											Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		forma.reset(true);
		forma.setEstado("empezar");
		forma.setMotivosConsultaUrg(UtilidadesHistoriaClinica.obtenerMotivosConsultaUrgencias(con, codigoInstitucion));
		forma.setCalificacionTriaje(UtilidadesHistoriaClinica.obtenerCategoriasTriage(con, codigoInstitucion));
		forma.setSignosYSintomas(UtilidadesHistoriaClinica.obtenerSignosSintomas(con, codigoInstitucion));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezarIngresarModificarEliminar.
	 * 
	 * @param forma 
	 * para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "signosSintomasXSistema.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarIngresarModificarEliminar(	SignosSintomasXSistemaForm forma, 
																	ActionMapping mapping,
																	int codigoInstitucion,
																	Connection con,
																	boolean mostrarMensajeGrabacion) throws SQLException
	{
	    SignosSintomasXSistema mundo= new SignosSintomasXSistema();
	    forma.setMapa(mundo.listado(con, forma.getCodigoMotivoConsulta(), codigoInstitucion));
		forma.setMapaNoModificado((HashMap)forma.getMapa().clone());
		forma.setMostrarMensaje(mostrarMensajeGrabacion);
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * ingresarNuevoElementoMapa.
	 * 
	 * @param forma
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "signosSintomasXSistema.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionIngresarNuevoElementoMapa(	SignosSintomasXSistemaForm forma, 
	        												Connection con,
															HttpServletRequest request,
															HttpServletResponse response) throws Exception
	{
	    int tempoTamanioRealMapa=forma.getNumeroRealFilasMapa();
	    
	    forma.setMapa("codigo_"+tempoTamanioRealMapa,"-2");
	    forma.setMapa("consecutivosignosintoma_"+tempoTamanioRealMapa,"");
	    forma.setMapa("consecutivocategoriatriage_"+tempoTamanioRealMapa,"");
	    forma.setMapa("descripcionsignosintoma_"+tempoTamanioRealMapa,"");
	    forma.setMapa("descripcioncategoriatriage_"+tempoTamanioRealMapa,"");
	    forma.setMapa("modificando_"+tempoTamanioRealMapa,ConstantesBD.acronimoSi);
	    forma.setMapa("estabd_"+tempoTamanioRealMapa,"false");
	    forma.setMapa("eseliminada_"+tempoTamanioRealMapa,"false");
	    forma.setMapa("estautilizada_"+tempoTamanioRealMapa,"false");
	    
	    UtilidadBD.cerrarConexion(con);
	    forma.setNumeroRealFilasMapa(forma.getNumeroRealFilasMapa()+1);
	    
	    forma.setMostrarMensaje(false);
	    if(request.getParameter("ultimaPagina")==null)
	    {
	        if(forma.getNumeroRealFilasMapa()>(forma.getOffsetHash()+forma.maxPagesItemsHash))
		        forma.setOffsetHash(forma.getOffsetHash()+forma.maxPagesItemsHash);
	        
	        response.sendRedirect("signosSintomasXSistema.jsp?pager.offset="+forma.getOffsetHash());
	    }
	    else
	    {    
		    String ultimaPagina=request.getParameter("ultimaPagina");
		    int posOffSet=0;
		    posOffSet=ultimaPagina.indexOf("offset=")+7;
		    forma.setOffsetHash((Integer.parseInt(ultimaPagina.substring(posOffSet, ultimaPagina.length() ))));
		    
		    if(forma.getNumeroRealFilasMapa()>(forma.getOffsetHash()+forma.maxPagesItemsHash))
		        forma.setOffsetHash(forma.getOffsetHash()+forma.maxPagesItemsHash);
		    
		    response.sendRedirect(ultimaPagina.substring(0, posOffSet)+forma.getOffsetHash());
	    }
	    return null;
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * eliminarNuevoElementoMapa.
	 * 
	 * @param forma, 
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "signosSintomasXSistema.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEliminarElementoMapa(		SignosSintomasXSistemaForm forma, 
	    													ActionMapping mapping,
															Connection con,
															HttpServletResponse response) throws Exception
	{
	    forma.setMapa("eseliminada_"+forma.getIndexMapa(),"true");
	    forma.setMostrarMensaje(false);
	    UtilidadBD.cerrarConexion(con);
	    
	    if(!forma.getLinkSiguiente().equals(""))
	        response.sendRedirect(forma.getLinkSiguiente());
	    else
	        return mapping.findForward("paginaPrincipal");		
		return null;
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * guardar.
	 * 
	 * @param forma
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward 
	 * @throws SQLException
	 */
	private ActionForward accionGuardar (	SignosSintomasXSistemaForm forma,
											ActionMapping mapping,
											HttpServletRequest request, 
											UsuarioBasico usuario,
											Connection con) throws SQLException
	{
		UtilidadBD.iniciarTransaccion(con);
	    int validarActualizacionTransaccional=actualizarGuardarNuevosEliminarViejosBD(forma,usuario,con);
	    
	    if(validarActualizacionTransaccional==errorCodigo)
	    {
	        ActionErrors errores = new ActionErrors();
			errores.add("actualización Signos Sintomas X Sistema", new ActionMessage("errors.ingresoDatos","los datos de Signos y Sintomas por Sistema (Transacción)"));
			logger.warn("error en la actualización de los datos de Signos Sintomas X Sistema");
			saveErrors(request, errores);
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("paginaPrincipal");
	    }
	    else if(validarActualizacionTransaccional== errorUnique)
	    {
	        ActionErrors errores = new ActionErrors();
	        errores.add("actualización Signos Sintomas X Sistema (Unique)", new ActionMessage("error.signosSintomasXSistema.yaExisteUnique"));
			logger.warn("error en la actualización de los datos de Signos y Sintomas X Sistema   (Unique)");
			saveErrors(request, errores);	
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("paginaPrincipal");
	    }
	    else if(validarActualizacionTransaccional== ConstantesBD.excepcionViolacionLlaveForanea)
	    {
	    	ActionErrors errores = new ActionErrors();
	    	//@todo mandar los mensajes
	        errores.add("insert Signos Sintomas X Sistema", new ActionMessage("error.registroUtilizado","xxxx", "xxxx"));
			logger.warn("error en la actualización de los datos de Signos y Sintomas X Sistema   (Unique)");
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
	        return mapping.findForward("paginaPrincipal");
	    }
	    UtilidadBD.finalizarTransaccion(con);
	    return this.accionEmpezarIngresarModificarEliminar(forma, mapping, usuario.getCodigoInstitucionInt(), con, true);
	}
	
	/**
	 * Método que comparando el HashMap Original y el modificado,
	 * entonces inserta en BD los que en el hash Map tienen como key del
	 * estaBD=  y esEliminada= los nuevos, y los que han sido eliminados que
	 * estan en BD  estaBD=  y esEliminada=, por otra parte modifica los datos 
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
	private int actualizarGuardarNuevosEliminarViejosBD(	SignosSintomasXSistemaForm forma,
	        															UsuarioBasico usuario,
	        															Connection con) throws SQLException
	{
		SignosSintomasXSistema mundo= new SignosSintomasXSistema();
	    
	    boolean insertados=false;
	    int numInsertados=0;
	    
	    for(int k=0; k<forma.getNumeroRealFilasMapa(); k++)
	    {
	        /*PRIMERA PARTE PARA LA ELIMINACIÓN DE DATOS QUE ESTAN EN BD*/
	        if(  UtilidadTexto.getBoolean(forma.getMapa("eseliminada_"+k)+""))
	         {
	             if( UtilidadTexto.getBoolean((forma.getMapa("estabd_"+k)+"")))
	             {
	                 insertados=false;
	                    
	                 numInsertados=mundo.eliminar(con, forma.getMapa("codigo_"+k).toString());
	                 if(numInsertados==ConstantesBD.excepcionViolacionLlaveForanea)
	                 {	 
	                     return ConstantesBD.excepcionViolacionLlaveForanea;
	                 }    
	                 else if(numInsertados<=0)
	                 {
	                	 logger.warn("error en la eliminacion de los datos ");
	                         return errorCodigo;
	                 }    
	                 else
	                     generarLog(forma, k, usuario, false);
	             }
	         }
	    }  
	    /*SEGUNDA PARTE PARA LA MODIFICACION DE LOS DATOS QUE ESTAN EN BD*/
		//en este punto se carga el mapa solo con los valores que han sido modificados
		forma.comparar2HashMap();
		
		if(forma.getNumeroRealFilasMapaAux()>0)
		{
		     for(int k=0; k<forma.getNumeroRealFilasMapaNoMod();k++)
		     {
		         if(  !UtilidadTexto.getBoolean(forma.getMapa("eseliminada_"+k)+""))
		         {
				     if( UtilidadTexto.getBoolean(forma.getMapa("estabd_"+k)+""))
			         {
				         String tempoCod=forma.getMapaAux("codigo_"+k)+"";
				         if(tempoCod!=null && !tempoCod.equals("") && !tempoCod.equals("null") && !tempoCod.equals("-1"))
				         {
				             insertados=false;
				             
				             ///////se evalua que no exista un registro igual
				             if(mundo.existeRegistro(con, forma.getCodigoMotivoConsulta(), forma.getMapaAux("consecutivosignosintoma_"+k).toString(), forma.getMapaAux("consecutivocategoriatriage_"+k).toString(), usuario.getCodigoInstitucionInt(), forma.getMapaAux("codigo_"+k).toString()))
				            		 return errorUnique;
				             
				             numInsertados= mundo.modificar(con, forma.getCodigoMotivoConsulta(), forma.getMapaAux("consecutivosignosintoma_"+k).toString(), forma.getMapaAux("consecutivocategoriatriage_"+k).toString(), usuario.getCodigoInstitucionInt(), forma.getMapaAux("codigo_"+k).toString());
				             
				             if(numInsertados<=0)
                             {
                                 logger.warn("error en la modificacion (Unique)");
			                     return errorCodigo;
                             }    
			                 generarLog(forma, k, usuario, true);
			             }
			         }  
		         }
		      }
		}    
        
		for(int k=0; k<forma.getNumeroRealFilasMapa(); k++)
		{      
	       /*TERCERA PARTE PARA LA INSERCIÓN DE LOS NUEVOS DATOS*/
		    if(  !UtilidadTexto.getBoolean((forma.getMapa("eseliminada_"+k)+"")))
		    {
		        if( !UtilidadTexto.getBoolean((forma.getMapa("estabd_"+k)+"")))
	            {
	                insertados=false;
	                
	                
	                //////se evalua que no exista un registro igual
		            if(mundo.existeRegistro(con, forma.getCodigoMotivoConsulta(), forma.getMapa("consecutivosignosintoma_"+k).toString(), forma.getMapa("consecutivocategoriatriage_"+k).toString(), usuario.getCodigoInstitucionInt(), forma.getMapa("codigo_"+k).toString()))
		            	 return errorUnique;
		            
	                insertados=mundo.insertar(con, forma.getCodigoMotivoConsulta(), forma.getMapa("consecutivosignosintoma_"+k).toString(), forma.getMapa("consecutivocategoriatriage_"+k).toString(), usuario.getCodigoInstitucionInt());
	                if(!insertados)
                    {
	                    logger.warn("error en la insercion de los datos");
	                    return errorCodigo;
                    }     
	                
	             }
	         }
	    }
	    return ningunError;
	}
	
	
	/**
	 * Método que genera los Logs de Modificación y borrado 
	 * @param forma
	 * @param indexKeyCodigoMapaMod, indice de la llave.
	 * @param usuario, user
	 */
	private void generarLog(	SignosSintomasXSistemaForm forma, int indexKeyCodigoMapaMod, UsuarioBasico usuario, boolean esModificacion)
	{
	    String log;
	        		    
		log="\n           ======INFORMACION ORIGINAL SIGNOS Y SINTOMAS POR SISTEMA===== " +
		"\n*  Código Signo Síntoma[" +forma.getMapaNoModificado("consecutivosignosintoma_"+indexKeyCodigoMapaMod) +"] "+
		"\n*  Código Tipo Liquidación ["+forma.getMapaNoModificado("consecutivocategoriatriage_"+indexKeyCodigoMapaMod)+"] ";
		log+="\n========================================================\n\n\n " ;
		
		if(esModificacion)
	    {   	
			log+="\n          =====INFORMACION DESPUES DE LA MODIFICACION SIGNOS Y SINTOMAS POR SISTEMA===== " +
			"\n*  Código Signo Síntoma [" +forma.getMapaAux("consecutivosignosintoma_"+indexKeyCodigoMapaMod) +"] "+
			"\n*  Código Tipo Liquidación ["+forma.getMapaAux("consecutivocategoriatriage_"+indexKeyCodigoMapaMod)+"] ";
			log+="\n========================================================\n\n\n " ;	
			
			LogsAxioma.enviarLog(ConstantesBD.logSignosSintomasXSistemaCodigo, log, ConstantesBD.tipoRegistroLogModificacion,usuario.getInformacionGeneralPersonalSalud()); 
	    }
	    else
	    {
	        log+="\n========================================> FUE ELIMINADO\n\n\n " ;	
			LogsAxioma.enviarLog(ConstantesBD.logSignosSintomasXSistemaCodigo, log, ConstantesBD.tipoRegistroLogEliminacion,usuario.getInformacionGeneralPersonalSalud());
	    }
	}
	
	/**
	 * Action para permitir el ordenamiento por cada una de las columnas
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenarColumna(Connection con, ActionMapping mapping, SignosSintomasXSistemaForm forma) 
    {
		Utilidades.imprimirMapa(forma.getMapa());
		forma.setMostrarMensaje(false);
        String[] indices = {
				            "codigo_", 
				            "consecutivosignosintoma_", 
				            "consecutivocategoriatriage_", 
							"estabd_",
				            "eseliminada_",
				            "estautilizada_",
				            "descripcionsignosintoma_",
				            "descripcioncategoriatriage_",
				            "modificando_"
	            		   };
        
        int tmp = Integer.parseInt(forma.getMapa("numRegistros")+"");
        forma.setMapa(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapa(),Integer.parseInt(forma.getMapa("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapa("numRegistros",tmp+"");
        Utilidades.imprimirMapa(forma.getMapa());
        UtilidadBD.closeConnection(con);
        return mapping.findForward("paginaPrincipal");  
    }
	
}