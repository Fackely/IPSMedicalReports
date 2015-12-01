/*
 * @(#)AsociosTipoServicioAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.salasCirugia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;
import util.LogsAxioma;
import util.UtilidadBD;

import com.princetonsa.actionform.salasCirugia.AsociosTipoServicioForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.AsociosTipoServicio;

/**
 *   Action, controla todas las opciones dentro de los asocios x tipo de servicio, 
 *   incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Sep 21, 2005
 * @author wrios
 */
public class AsociosTipoServicioAction extends Action
{
    /**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(AsociosTipoServicioAction.class);
	
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
		try{
		if (response==null); //Para evitar que salga el warning
		if(form instanceof AsociosTipoServicioForm)
		{
		    
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			}
			AsociosTipoServicioForm forma =(AsociosTipoServicioForm)form;
			String estado=forma.getEstado(); 
			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			logger.warn("ESTADO ASOCIOS X TIPO SERV==========="+estado);
			
			if(estado == null)
			{
				forma.reset();	
				logger.warn("Estado no valido dentro del flujo de la Asocios X Tipo Servicio (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				 UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			else if (estado.equals("empezar"))
			{
				return this.accionEmpezar(forma, mapping, usuario.getCodigoInstitucionInt(), con);
			}
			else if(estado.equals("ingresarNuevoElementoMapa"))
			{
				return this.accionIngresarNuevoElementoMapa(forma, mapping, con);
			}
			else if(estado.equals("eliminarElementoMapa"))
			{
				return this.accionEliminarElementoMapa(forma, mapping, con);
			}
			else if(estado.equals("guardar"))
			{
				return this.accionGuardar(forma,mapping,request,usuario,con);
			}
			else if(estado.equals("reload"))
			{
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("paginaPrincipal");	
			}
			else
			{
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de Asocios X Tipo Servicio (null) ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				 UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
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
	 * Este método especifica las acciones a realizar en el estado
	 * empezarIngresarModificarEliminar.
	 * 
	 * @param forma AsociosTipoServicioForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param int codigoInstitucion
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "asociosTipoServicio.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(	AsociosTipoServicioForm forma, 
																ActionMapping mapping,
																int codigoInstitucion,
																Connection con) throws SQLException
	{
	    forma.reset();
		forma.resetMapa();
		AsociosTipoServicio mundo= new AsociosTipoServicio();
		//mundo.setEsquemaTarifario(new InfoDatosInt(forma.getCodigoEsquemaTarifario(), ""));
		forma.setMapaAsociosTipoServicio(mundo.listadoMapaAsociosTipoServicio(con, codigoInstitucion ));
		forma.setMapaAsociosTipoServicioNoModificado((HashMap)forma.getMapaAsociosTipoServicio().clone());
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * ingresarNuevoElementoMapa.
	 * 
	 * @param AsociosTipoServicioForm forma
	 * 				para pre-llenar datos si es necesario
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal 
	 * @throws SQLException
	 */
	private ActionForward accionIngresarNuevoElementoMapa(		AsociosTipoServicioForm forma, 
	        																					ActionMapping mapping,
	        																					Connection con
																						  ) throws Exception
	{
	    int tempoTamanioRealMapa=forma.getNumeroRealFilasMapa();
	    forma.setMapaAsociosTipoServicio("codigo_"+tempoTamanioRealMapa,"-1");
	    forma.setMapaAsociosTipoServicio("codigoEsquemaTarifarioBoolEsGeneral_"+tempoTamanioRealMapa,"-1@@@false");
	    forma.setMapaAsociosTipoServicio("nombreEsquemaTarifario_"+tempoTamanioRealMapa,"");
	    forma.setMapaAsociosTipoServicio("acronimoTipoServicio_"+tempoTamanioRealMapa,"");
	    forma.setMapaAsociosTipoServicio("nombreTipoServicio_"+tempoTamanioRealMapa,"");
	    forma.setMapaAsociosTipoServicio("codigoTipoAsocio_"+tempoTamanioRealMapa,"-1");
	    forma.setMapaAsociosTipoServicio("acronimoTipoServicioAsocio_"+tempoTamanioRealMapa,"");
	    forma.setMapaAsociosTipoServicio("nombreTipoServicioAsocio_"+tempoTamanioRealMapa,"");
	    forma.setMapaAsociosTipoServicio("codigoServicio_"+tempoTamanioRealMapa,"-1");
	    forma.setMapaAsociosTipoServicio("descripcionServicio_"+tempoTamanioRealMapa,"");
	    forma.setMapaAsociosTipoServicio("activo_"+tempoTamanioRealMapa,"true");
	    forma.setMapaAsociosTipoServicio("estaBD_"+tempoTamanioRealMapa,"f");
	    forma.setMapaAsociosTipoServicio("esEliminada_"+tempoTamanioRealMapa,"f");
	    UtilidadBD.cerrarConexion(con);
	    return mapping.findForward("paginaPrincipal");		
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * eliminarElementoMapa.
	 * 
	 * @param AsociosTipoServicioForm forma, 
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "asociosTipoServicio.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEliminarElementoMapa(		AsociosTipoServicioForm forma, 
	    																				ActionMapping mapping,
																						Connection con
																				) throws Exception
	{
	    forma.setMapaAsociosTipoServicio("esEliminada_"+forma.getIndexMapa(),"t");
	    UtilidadBD.cerrarConexion(con);
	    return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * guardar.
	 * 
	 * @param AsociosTipoServicioForm forma
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward 
	 * @throws SQLException
	 */
	private ActionForward accionGuardar (	AsociosTipoServicioForm forma,
																ActionMapping mapping,
																HttpServletRequest request, 
																UsuarioBasico usuario,
																Connection con) throws SQLException
	{
	    boolean validarActualizacionTransaccional=actualizarGuardarNuevosEliminarViejosBDTransaccional(forma,usuario,con);
	    
	    if(!validarActualizacionTransaccional)
	    {
	        ActionErrors errores = new ActionErrors();
			errores.add("actualización Asocios x Tipo Servicio", new ActionMessage("error.salasCirugia.yaExisteUnique1","los datos unique de asocios x tipo servicio (Transacción)"));
			logger.warn("error en la actualización de los datos de asocios x tipo servicio");
			saveErrors(request, errores);	
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("paginaPrincipal");
	    }
	    forma.setEstado("empezar");
	   return this.accionEmpezar(forma, mapping, usuario.getCodigoInstitucionInt(), con);
	}
	
	/**
	 * Método transaccional que comparando el HashMap Original y el modificado,
	 * entonces inserta en BD los que en el hash Map tienen como key del
	 * estaBD= 'f' y esEliminada='f' los nuevos, por otra parte modifica los datos 
	 * pasandolos a un nuevo mapa que contiene solo la info modificada para enviar 
	 * a la BD. 
	 * 
	 * @param AsociosTipoServicioForm forma
	 * @param con
	 * @throws SQLException
	 * 
	 * @return true cuando todo salió bien 
	 */
	private boolean actualizarGuardarNuevosEliminarViejosBDTransaccional(	AsociosTipoServicioForm forma,
	        																										UsuarioBasico usuario,
	        																										Connection con) throws SQLException
	{
	    AsociosTipoServicio mundo= new AsociosTipoServicio();
	    //mundo.setEsquemaTarifario(new InfoDatosInt(forma.getCodigoEsquemaTarifario(), ""));
        boolean yaComenzoTransaccion=false;
	    boolean insertados=false;
	    
	    /*PRIMERA PARTE PARA LA MODIFICACION DE LOS DATOS QUE ESTAN EN BD*/
		//en este punto se carga el mapa solo con los valores que han sido modificados
		forma.comparar2HashMap();
		
		if(forma.getNumeroRealFilasMapaAux()>0)
		{
		     for(int k=0; k<forma.getNumeroRealFilasMapaNoMod();k++)
		     {
		         if(  (forma.getMapaAsociosTipoServicio("esEliminada_"+k)+"").equals("f"))
		         {
				     if((forma.getMapaAsociosTipoServicio("estaBD_"+k)+"").equals("t"))
			         {
				         insertados=false;
				         String tempoCodPK=forma.getMapaAsociosTipoServicioAux("codigo_"+k)+"";
				         if(tempoCodPK!=null && !tempoCodPK.trim().equals("") && !tempoCodPK.equals("null") && !tempoCodPK.equals("-1"))
				         {
				             mundo.setCodigo(Integer.parseInt(forma.getMapaAsociosTipoServicioAux("codigo_"+k)+""));
				             mundo.setServicio(new InfoDatosInt(Integer.parseInt(forma.getMapaAsociosTipoServicioAux("codigoServicio_"+k)+""), ""));
				             mundo.setActivo(forma.getMapaAsociosTipoServicioAux("activo_"+k)+"");
				             if(((forma.getMapaAsociosTipoServicioAux("codigoEsquemaTarifarioBoolEsGeneral_"+k)+"").split("@@@")[1]).equals("true"))
				                 mundo.setEsEsquemaTarifarioGeneral(true);
				             else
				                 mundo.setEsEsquemaTarifarioGeneral(false);
				             mundo.setEsquemaTarifario(new InfoDatosInt( Integer.parseInt((forma.getMapaAsociosTipoServicioAux("codigoEsquemaTarifarioBoolEsGeneral_"+k)+"").split("@@@")[0]), ""));
				             InfoDatos infoDatos=new InfoDatos();
					         infoDatos.setAcronimo(forma.getMapaAsociosTipoServicioAux("acronimoTipoServicio_"+k)+"");
					         infoDatos.setNombre("");
					         mundo.setTipoServicio(infoDatos);
					         infoDatos= new InfoDatos();
					         infoDatos.setCodigo(Integer.parseInt(forma.getMapaAsociosTipoServicioAux("codigoTipoAsocio_"+k)+""));
					         mundo.setTipoAsocio(infoDatos);
				             
					         if(!yaComenzoTransaccion)
					         {    
					             insertados=mundo.modificarAsocioTipoSertvicioTransaccional(con, ConstantesBD.inicioTransaccion, usuario.getCodigoInstitucionInt()); 
					             if(!insertados)
					                 return false;
					             else
				                     generarLog(forma, k, usuario);
					             yaComenzoTransaccion=true;
					         }    
					         else
					         {    
					             insertados=mundo.modificarAsocioTipoSertvicioTransaccional(con, ConstantesBD.continuarTransaccion, usuario.getCodigoInstitucionInt());
					             if(!insertados)
					                 return false;
					             else
					                 generarLog(forma, k, usuario);
					          }
				         }    
			         }  
		         }
		     }
		}    
        
		for(int k=0; k<forma.getNumeroRealFilasMapa(); k++)
		{      
	       /*SEGUNDA PARTE PARA LA INSERCIÓN DE LOS NUEVOS DATOS*/
		    if(  (forma.getMapaAsociosTipoServicio("esEliminada_"+k)+"").equals("f"))
		    {
		        if((forma.getMapaAsociosTipoServicio("estaBD_"+k)+"").equals("f"))
	            {
		            insertados=false;
		            
		            mundo.setEsquemaTarifario(new InfoDatosInt( Integer.parseInt((forma.getMapaAsociosTipoServicio("codigoEsquemaTarifarioBoolEsGeneral_"+k)+"").split("@@@")[0]), ""));
		            if(((forma.getMapaAsociosTipoServicio("codigoEsquemaTarifarioBoolEsGeneral_"+k)+"").split("@@@")[1]).equals("true"))
		                mundo.setEsEsquemaTarifarioGeneral(true);
		            else
		                mundo.setEsEsquemaTarifarioGeneral(false);
		            InfoDatos infoDatos=new InfoDatos();
		            infoDatos.setAcronimo(forma.getMapaAsociosTipoServicio("acronimoTipoServicio_"+k)+"");
		            infoDatos.setNombre("");
		            mundo.setTipoServicio(infoDatos);
		            infoDatos= new InfoDatos();
		            infoDatos.setCodigo(Integer.parseInt(forma.getMapaAsociosTipoServicio("codigoTipoAsocio_"+k)+""));
		            mundo.setTipoAsocio(infoDatos);
		            mundo.setServicio(new InfoDatosInt(Integer.parseInt(forma.getMapaAsociosTipoServicio("codigoServicio_"+k)+""),""));
		            mundo.setActivo(forma.getMapaAsociosTipoServicio("activo_"+k)+"");
		            
	                 if(!yaComenzoTransaccion)
	                 {    
	                     insertados=mundo.insertarAsocioTipoServicioTransaccional(con, usuario.getCodigoInstitucionInt(), ConstantesBD.inicioTransaccion);
	                     if(!insertados)
	                         return false;
	                     yaComenzoTransaccion=true;
	                 }    
	                 else
	                 {    
	                     insertados=mundo.insertarAsocioTipoServicioTransaccional(con, usuario.getCodigoInstitucionInt(), ConstantesBD.continuarTransaccion);
	                     if(!insertados)
	                         return false;
	                 }    
	             }
	         }
	    }
	    // SI LA TRANSACCIÓN YA FUÉ INICIADA ENTONCES QUE LA FINALICE
	    if(yaComenzoTransaccion)
	        mundo.terminarTransaccion(con); 
	    return true;
	}
	
	/**
	 * Método que genera los Logs de Modificación
	 * @param forma
	 * @param indexKeyCodigoMapaMod
	 * @param usuario
	 * @param esModificacion
	 */
	private void generarLog(AsociosTipoServicioForm forma,  int indexKey, UsuarioBasico usuario)
	{
	    String log;
	    
	    log="\n            =========INFORMACION ORIGINAL ASOCIOS X TIPO SERVICIO======== " +
		"\n*  Código [" +forma.getMapaAsociosTipoServicioNoModificado("codigo_"+indexKey) +"] ";
	    if(   ( (forma.getMapaAsociosTipoServicioNoModificado("codigoEsquemaTarifarioBoolEsGeneral_"+indexKey)+"").split("@@@")[1]).equals("true"))
	        log+="\n*  Código Esquema Tarifario GENERAL ["+(forma.getMapaAsociosTipoServicioNoModificado("codigoEsquemaTarifarioBoolEsGeneral_"+indexKey)+"").split("@@@")[0]+"] ";
	    else
	        log+="\n*  Código Esquema Tarifario PARTICULAR ["+(forma.getMapaAsociosTipoServicioNoModificado("codigoEsquemaTarifarioBoolEsGeneral_"+indexKey)+"").split("@@@")[0]+"] ";
	    log+= "\n*  Acrónimo Tipo Servicio ["+forma.getMapaAsociosTipoServicioNoModificado("acronimoTipoServicio_"+indexKey)+"] " +
		"\n*  Código Asocio ["+forma.getMapaAsociosTipoServicioNoModificado("codigoTipoAsocio_"+indexKey)+"] " +
		"\n*  Código Servicio ["+forma.getMapaAsociosTipoServicioNoModificado("codigoServicio_"+indexKey)+"] " +
		"\n*  Activo ["+forma.getMapaAsociosTipoServicioNoModificado("activo_"+indexKey)+"] " +
		"\n*  Institucion ["+usuario.getCodigoInstitucion()+"] " ;
		
	    log+="\n          =====INFORMACION DESPUES DE LA MODIFICACION ASOCIOS X TIPO SERVICIO ===== " +
	    "\n*  Código [" +forma.getMapaAsociosTipoServicioAux("codigo_"+indexKey) +"] ";
	    if(   ( (forma.getMapaAsociosTipoServicioAux("codigoEsquemaTarifarioBoolEsGeneral_"+indexKey)+"").split("@@@")[1]).equals("true"))
	        log+="\n*  Código Esquema Tarifario GENERAL ["+(forma.getMapaAsociosTipoServicioAux("codigoEsquemaTarifarioBoolEsGeneral_"+indexKey)+"").split("@@@")[0]+"] ";
	    else
	        log+="\n*  Código Esquema Tarifario PARTICULAR ["+(forma.getMapaAsociosTipoServicioAux("codigoEsquemaTarifarioBoolEsGeneral_"+indexKey)+"").split("@@@")[0]+"] ";
	    log+= "\n*  Acrónimo Tipo Servicio ["+forma.getMapaAsociosTipoServicioAux("acronimoTipoServicio_"+indexKey)+"] " +
		"\n*  Código Asocio ["+forma.getMapaAsociosTipoServicioAux("codigoTipoAsocio_"+indexKey)+"] " +
		"\n*  Código Servicio ["+forma.getMapaAsociosTipoServicioAux("codigoServicio_"+indexKey)+"] " +
		"\n*  Activo ["+forma.getMapaAsociosTipoServicioAux("activo_"+indexKey)+"] " +
		"\n*  Institucion ["+usuario.getCodigoInstitucion()+"] " ;
		log+="\n========================================================\n\n\n " ;	
		
		LogsAxioma.enviarLog(ConstantesBD.logAsociosTipoServicioCodigo, log, ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario()); 
	}
	
	
}
