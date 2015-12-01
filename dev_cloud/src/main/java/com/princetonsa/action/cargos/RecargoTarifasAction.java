
/*
 * Creado   14/10/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.action.cargos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.cargos.RecargoTarifasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.RecargoTarifas;


/**
 * Clase para el manejo de los recargos de las tarifas , ingreso, modificación, eliminación 
 * y consulta 
 *
 * @version 1.0, 17/08/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public class RecargoTarifasAction extends Action {
    
    /**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(RecargoTarifasAction.class);
	
	/**
	 * Método execute del action
	 */	
	public ActionForward execute(	ActionMapping mapping,
															ActionForm form,
															HttpServletRequest request,
															HttpServletResponse response ) throws Exception
{
		
		 Connection con=null;
		 try{
		
	    if (response==null); //Para evitar que salga el warning
	    
	    if(form instanceof RecargoTarifasForm)
		{
	       
	        
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			}
			
			RecargoTarifasForm recargoForm =  (RecargoTarifasForm)form;
			HttpSession session=request.getSession();
			String estado=recargoForm.getEstado();
			logger.warn("estado ="+estado);
			
			if(estado == null)
			{
				recargoForm.reset(true,true,true,true);	
				logger.warn("Estado no valido dentro del flujo de Recargo Tarifas (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			
			else if (estado.equals("empezar") || estado.equals("buscarConsultar"))//Accion principal manda al filtro de convenio y contrato.
			{
				return this.accionEmpezar(con,mapping,recargoForm);			    
			}
			else if(estado.equals("empezarInsertar"))//Accion que envia a la pagina de insertar.
			{
				return this.accionEmpezarInsertar(mapping, con, recargoForm);
			}
			
			else if (estado.equals("buscarModificar"))//Envia a la pagina de Busqueda Avanzada de Modificar.
			{
			    return this.accionBuscar(mapping, con, recargoForm);
			}
			
			else if (estado.equals("salirGuardar"))//Accion para Guaradar datos del formulario.
			{
				return this.accionSalirGuardar(con,mapping,recargoForm,request);			    
			}
			
			else if (estado.equals("busquedaAvanzadaModificar"))//Consulta Avanzada para Modificar.
			{
				return this.accionBusquedaAvanzada(con,mapping,recargoForm);			    
			}
			
			else if (estado.equals("busquedaAvanzadaConsultar"))//Consulta Avanzada para Consultar/Imprimir.
			{
			    return this.accionBusquedaAvanzadaConsultar(con,mapping,recargoForm);
			}
			
			else if (estado.equals("modificar"))//Accion para modificar
			{
			    return this.accionModificar(con,mapping,recargoForm,request,session);
			}
			
			else if (estado.equals("eliminarRecargo"))//Accion para eliminar
			{
			    return this.accionEliminar(con,mapping,recargoForm,request,session);
			}
						
			else if (estado.equals("empezarConsultar"))//Envia a la pagina de Consulta Avanzada de Consultar/Imprimir.
			{
			    recargoForm.reset(true,false,true,true);	
				this.cerrarConexion(con);
				return mapping.findForward("consultar");
			}
			
			else if (estado.equals("redireccion"))
			{
				UtilidadBD.cerrarConexion(con);
				response.sendRedirect(recargoForm.getLinkSiguiente());
				return null;
			}
			else if (estado.equals("cargarTipoPaciente"))
			{
				return mapping.findForward("recargoTarifas");
			}
			else if (estado.equals("cargarTipoPaciente1"))
			{
				return mapping.findForward("consultar");
			}
			else if (estado.equals("cargarTipoPaciente2"))
			{
				return mapping.findForward("busquedaModificar");
			}
			else
			{
			    recargoForm.reset(true,true,true,true);	
				logger.warn("Estado no valido dentro del flujo Recargo de Tarifas (null) ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				this.cerrarConexion(con);
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
     * Metodo para eliminar un recargo tarifa por medio del codigo
     * en la BD.
     * @param con, Connection con la fuente de datos.
     * @param mapping, Mapping para manejar la navegación.
     * @param recargoForm, RecargoTarifasForm
     * @param request, HttpServletRequest para obtener datos de la session 
     * @return findForward, buscar 
     * @throws SQLException
     * @see struts-config, ACTION RECARGOS
     */
    private ActionForward accionEliminar(Connection con, ActionMapping mapping, 
										            							RecargoTarifasForm recargoForm,
										            							HttpServletRequest request,
										            							HttpSession session) throws SQLException 
    {
        RecargoTarifas mundo = new RecargoTarifas ();
        mundo.reset();
        
        mundo.setCodigoRecargo(recargoForm.getCodigoRecargo());
        
        boolean siElimino = mundo.eliminar(con);
        
        if(siElimino)
		{
            recargoForm.setColeccionAvanzadaModificar( mundo.consultaAvanzadaRecargo(con, 
																			                recargoForm.getPorcentaje(),
																			                recargoForm.getValor(),
																			                recargoForm.getCodigoViaIngreso(),
																			                recargoForm.getTipoPaciente(),
																			                recargoForm.getCodigoEspecialidad(),
																			                recargoForm.getCodigoContrato(),
																			                recargoForm.getCodigoServicio(),
																			                recargoForm.getCodigoTipoRecargo(),
																			                recargoForm.getNombreEspecialidad(),
																			                recargoForm.getNombreServicio()));
            
            UsuarioBasico usuario;
	        String log=recargoForm.getLog() +
			"\n            =====REGISTRO ELIMINADO===== " +
			"\n========================================================\n\n\n " +
			"\n Recargo Tarifa número-> "+recargoForm.getCodigoRecargo()+"\n" +
			"\n========================================================\n\n\n ";
	        usuario=(UsuarioBasico)session.getAttribute("usuarioBasico");
		    LogsAxioma.enviarLog(ConstantesBD.logRecargosTarifasCodigo,log,ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
            
            recargoForm.setEstado("busquedaAvanzada");
            recargoForm.reset(true,false,false,true);
            this.cerrarConexion(con);
    		return mapping.findForward("busquedaModificar");
		}
        else
		{
			ActionErrors errores = new ActionErrors();
			errores.add("eliminar recargo", new ActionMessage("error.excepcion.eliminacion.recargo",recargoForm.getCodigoRecargo()+""));
			logger.warn("Error eliminando el Recargo de Tarifa con codigo->"+recargoForm.getCodigoRecargo());
			saveErrors(request, errores);	
			recargoForm.setEstado("busquedaAvanzada");
			this.cerrarConexion(con);									
			return mapping.findForward("busquedaModificar");
		}
       
    }

    /**
     * Metodo encargado realizar la modificacion, de los (n) recargos
     * del formulario, por medio de un HashMap.
     * @param con, Connection con la fuente de datos.
     * @param mapping Mapping para manejar la navegación
     * @param recargoForm RecargoTarifasForm
     * @param request, HttpServletRequest para obtener datos de la session 
     * @return findForward,  resumenModificar
     * @see struts-config, ACTION RECARGOS
     */
    private ActionForward accionModificar(Connection con, 
												            ActionMapping mapping, 
												            RecargoTarifasForm recargoForm,
												            HttpServletRequest request,
												            HttpSession session) throws Exception 
    {
        
        int tempCodigo = -1, tempTipoRecargo = -1, tempViaIngreso = -1; 
        int tempCodigoServicio = -1, tempCodigoEspecialidad = -1, indexVector=0;;
        double tempPorcentaje = -1, tempValor = -1;
        boolean siModifico = false, hayModificacion = false;
        String tempTipoPaciente="-1";
        
        recargoForm.codigosRecargosModificados.clear();
        
        RecargoTarifas mundo = new RecargoTarifas ();
        mundo.reset();
        logger.info("\n Mapa pa modificar >>>"+recargoForm.getHashRecargos());
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
        for(int posHash=0; posHash < recargoForm.getColeccionAvanzadaModificar().size(); posHash++) 
	    {
            //solo se valida si es null o vacio, porque por la utilizacion del HashMap en el pager,
            //si no se cambia de pagina, el HashMap para ese registro(s) todos los campos vienen en null,
            //y si no hubo ningun cambio vienen vacios.
            
            if( ! (recargoForm.getHashRecargos("codigoRecargo_"+posHash)+"").equals("null"))
            {
                   
                if( ! (recargoForm.getHashRecargos("tipoRecargoForm_"+posHash)+"").equals("null") )
	            {
		            if( Integer.parseInt((recargoForm.getHashRecargos("tipoRecargoForm_"+posHash)+"")) != -1)
		            {
		                tempTipoRecargo = Integer.parseInt((recargoForm.getHashRecargos("tipoRecargoForm_"+posHash)+""));
		                hayModificacion = true;
		            }
	            }
	            else
	                tempTipoRecargo = -1;
	            
	            if( ! (recargoForm.getHashRecargos("via_ingreso_"+posHash)+"").equals("null") )
	            {
		            if( Integer.parseInt((recargoForm.getHashRecargos("via_ingreso_"+posHash)+"")) != -1)
		            {
		                tempViaIngreso = Integer.parseInt((recargoForm.getHashRecargos("via_ingreso_"+posHash)+""));
		                hayModificacion = true;
		            }
	            }
	            else
	                tempViaIngreso = -1;

	            if( ! (recargoForm.getHashRecargos("tipoPacienteForm_"+posHash)+"").equals("null") )
	            {
		            if( !recargoForm.getHashRecargos("tipoPacienteForm_"+posHash).equals("-1"))
		            {
		                tempTipoPaciente = ((recargoForm.getHashRecargos("tipoPacienteForm_"+posHash)+""))+"";
		                hayModificacion = true;
		            }
	            }
	            else
	                tempTipoPaciente = "-1";	            
	            
	            if( ! (recargoForm.getHashRecargos("codigoServicioForm_"+posHash)+"").equals("null") )
	            {
		            if( ! (recargoForm.getHashRecargos("codigoServicioForm_"+posHash)+"").equals("") )
		            {
		                tempCodigoServicio = Integer.parseInt((recargoForm.getHashRecargos("codigoServicioForm_"+posHash)+""));
		                hayModificacion = true;
		            }
	            }
	            else
	                tempCodigoServicio = -1;
	            
	            if( ! (recargoForm.getHashRecargos("codigoEspecialidadForm_"+posHash)+"").equals("null") )
	            {
		            if( ! (recargoForm.getHashRecargos("codigoEspecialidadForm_"+posHash)+"").equals("") )
		            {
		                tempCodigoEspecialidad = Integer.parseInt((recargoForm.getHashRecargos("codigoEspecialidadForm_"+posHash)+""));
		                hayModificacion = true;
		            }
	            }
	            else
	                tempCodigoEspecialidad = -1;
	            
	            if( ! (recargoForm.getHashRecargos("porcentajeForm_"+posHash)+"").equals("null") )
	            {
		            if( ! (recargoForm.getHashRecargos("porcentajeForm_"+posHash)+"").equals("") )
		            {
		                tempPorcentaje = Double.parseDouble((recargoForm.getHashRecargos("porcentajeForm_"+posHash)+""));
		                hayModificacion = true;
		            }
	            }
	            else
	                tempPorcentaje = -1;
	            
	            if( ! (recargoForm.getHashRecargos("valorForm_"+posHash)+"").equals("null") )
	            {
		            if( ! (recargoForm.getHashRecargos("valorForm_"+posHash)+"").equals("") )
		            {
		                tempValor = Double.parseDouble((recargoForm.getHashRecargos("valorForm_"+posHash)+""));
		                hayModificacion = true;
		            }
	            }
	            else
	                tempValor = -1;
	            
	              
	            try
	            {
	            logger.info("HAY MODIFICARCION EN EL ACCION >>>"+hayModificacion);
	            	if(hayModificacion)//para verificar si hubo modificacion en algun atributo (true efectivo)
	                {
	                    logger.info("DATOS DESPUES DE SI HAY QUE MODIFICAR TIPO PACIENTE >>>"+tempTipoPaciente+ "VIA INGRESO >>"+tempViaIngreso);
	            		tempCodigo = Integer.parseInt((recargoForm.getHashRecargos("codigoRecargo_"+posHash)+""));//Codigo del registro modificado
	                    
	                    siModifico = mundo.modificarTransaccional(con, 
													                    tempCodigo,
													                    tempPorcentaje,
													                    tempValor,
													                    tempViaIngreso,
													                    tempTipoPaciente,
													                    tempCodigoEspecialidad,
													                    tempCodigoServicio,
													                    tempTipoRecargo,
													                    ConstantesBD.inicioTransaccion);
		                
		                hayModificacion = false;//se reinicia el boolean para verificar otro registro modificado.
		                
	                }
	            }
	            catch (SQLException e)
	            {
	                e.printStackTrace();
	                logger.warn("Error modificando el Recargo de Tarifa con codigo -> " + tempCodigo );  
	            }
	            
	            if(siModifico)// true es efectiva la modificacion
	            {
	                //se cargan todos los codigos que fueron modificados en la clase Vector.
	                String[] registrosModificados =  null;
	                recargoForm.setCodigosRecargosModificados(indexVector, tempCodigo + "");
	                indexVector ++;
	                llenarloghistorial(recargoForm,tempCodigo);
	                recargoForm.setColeccionTodos(mundo.consultaUnRecargoTarifa(con,tempCodigo));
	                registrosModificados=pasarColHashMap(recargoForm,false,false,true);
	                generarLog(recargoForm,tempCodigo,session,registrosModificados);
	            }
	            
	            else if( ! siModifico && hayModificacion ) // se valida si por algun motivo no se realiza la modificacion en el mundo 
	                									  //o no hay ningun registro modificado.
	            {
	                ActionErrors errores = new ActionErrors();
					errores.add("modificar recargo", new ActionMessage("error.excepcion.modificacion.recargo", tempCodigo +""));
					logger.warn("entra al error NO modificar Recargo de Tarifas");
					saveErrors(request, errores);	
					
					this.cerrarConexion(con);									
					return mapping.findForward("busquedaModificar");
	            }
            }
            
	    }
        myFactory.endTransaction(con);
        return this.accionListarModificacion(con,mapping,recargoForm);
        
    }

    /**
     * Metodo empleado para realizar la consulta de los registros
     * modificados.
     * @param con Connection con la fuente de datos.
     * @param mapping Mapping para manejar la navegación
     * @param recargoForm RecargoTarifasForm
     * @param request
     * @return findForward listarModificar
     * @see struts-config, ACTION RECARGOS
     */
    private ActionForward accionListarModificacion(Connection con, 
														            ActionMapping mapping, 
														            RecargoTarifasForm recargoForm) 
    {
        RecargoTarifas mundo = new RecargoTarifas ();
        mundo.reset();
        
        recargoForm.reset(true,false,false,true); 
        
        if( ! recargoForm.getCodigosRecargosModificados().isEmpty() )
        {    
            recargoForm.setColeccionRecargo(mundo.cargarResumenModificacion(con, recargoForm.getCodigosRecargosModificados()));
            this.cerrarConexion(con);
    		return mapping.findForward("listarModificar");	   
        }
        else
        {
            recargoForm.setEstado("busquedaAvanzadaModificar");
            this.cerrarConexion(con);
    		return mapping.findForward("busquedaModificar");
        }
               
    }
    
    /**
	  * Metodo para cargar el Log con los datos actuales, antes de la 
	  * modificación.
	  * @param recargoForm, Instancia del Form RecargoTarifasForm.
	  */
	 private void llenarloghistorial(RecargoTarifasForm recargoForm, int codigoRecargo)
	 {
	        	     
	     for(int posHash=0; posHash < recargoForm.getColeccionAvanzadaModificar().size(); posHash++) 
		 	{
			 	if( codigoRecargo ==  Integer.parseInt(recargoForm.getHashRecargos("codigo_"+posHash)+"")) 
			 	{
			 	   recargoForm.setLog("\n            ====INFORMACION ORIGINAL===== " +
			        				  "\n*  Código Recargo Tarifa [" +(recargoForm.getHashRecargos("codigo_"+posHash)+"")+"] " +
			        				  "\n*  Tipo de Recargo ["+(recargoForm.getHashRecargos("nombre_recargo_"+posHash)+"")+"] " +
			        				  "\n*  Via de Ingreso ["+(recargoForm.getHashRecargos("nombre_via_"+posHash)+"")+"] "+
			        				  "\n*  Especialidad ["+(recargoForm.getHashRecargos("nombre_especialidad_"+posHash)+"")+"] " +
			        				  "\n*  Servicio ["+(recargoForm.getHashRecargos("nombre_servicio_"+posHash)+"")+"] " + 
			        				  "\n*  Porcentaje ["+(recargoForm.getHashRecargos("porcentaje_"+posHash)+"")+"] " +
			        				  "\n*  Valor ["+(recargoForm.getHashRecargos("valor_"+posHash)+"")+"] ");
			 	 	    
			 	}
			    
		 	}
	 }
	 
	 /**
	  * Metodo para generar el Log, y añadir los cambios realizados.
	  * @param recargoForm, Instancia del Form RecargoTarifasForm.
	  * @param session, Session para obtener el usuario.
	  * @param registrosModificados, vector de String que contiene 
	  * 	   los datos despues de la modificacion.
	  */
	 private void generarLog(RecargoTarifasForm recargoForm, int codigoRecargo,HttpSession session,String[] registrosModificados)
	    {
	        UsuarioBasico usuario;
	       
	        
	       
		        if( codigoRecargo ==  Integer.parseInt(registrosModificados[0]+"") ) 
			 	{
		            
			        String log=recargoForm.getLog() +
														"\n            ====INFORMACION DESPUES DE LA MODIFICACION===== " +
														"\n*  Código Recargo Tarifa [" +(registrosModificados[0]+"")+"] " +
								        				"\n*  Tipo de Recargo ["+(registrosModificados[11]+"")+"] " +
								        				"\n*  Via de Ingreso ["+(registrosModificados[10]+"")+"] "+
								        				"\n*  Especialidad ["+(registrosModificados[12]+"")+"] " +
								        				"\n*  Servicio ["+(registrosModificados[13]+"")+"] " + 
								        				"\n*  Porcentaje ["+(registrosModificados[1]+"")+"] " +
								        				"\n*  Valor ["+(registrosModificados[2]+"")+"] "+
														"\n========================================================\n\n\n " ;
			        usuario=(UsuarioBasico)session.getAttribute("usuarioBasico");
			        LogsAxioma.enviarLog(ConstantesBD.logRecargosTarifasCodigo,log,ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
			 	}
		 	
	        
	    }

    /**
     * Metodo para realizar la busqueda Avanzada para Consultar/Imprimir
     * @param con, Connection con la fuente de datos.
     * @param mapping Mapping para manejar la navegación
     * @param recargoForm RecargoTarifasForm
     * @return findForward, consultar
     * @see struts-config, ACTION RECARGOS
     */
    private ActionForward accionBusquedaAvanzadaConsultar(Connection con, ActionMapping mapping, RecargoTarifasForm recargoForm) 
    {
        RecargoTarifas mundo = new RecargoTarifas ();
        mundo.reset();
        boolean es=false;
        boolean se=false;
        
        if(recargoForm.getCheckEspecialidad().equals("on"))
           recargoForm.setCodigoEspecialidad(Integer.parseInt(recargoForm.getNombreEspecialidad()));
               
        if(recargoForm.getCheckServicio().equals("on"))
           recargoForm.setCodigoServicio(Integer.parseInt(recargoForm.getNombreServicio()));
                
        recargoForm.setColeccionAvanzadaConsultar( mundo.consultaAvanzadaRecargo(con, 
																                recargoForm.getPorcentaje(),
																                recargoForm.getValor(),
																                recargoForm.getCodigoViaIngreso(),
																                recargoForm.getTipoPaciente(),
																                recargoForm.getCodigoEspecialidad(),
																                recargoForm.getCodigoContrato(),
																                recargoForm.getCodigoServicio(),
																                recargoForm.getCodigoTipoRecargo(),
																                recargoForm.getNombreEspecialidad(),
																                recargoForm.getNombreServicio()));
       
        recargoForm.reset(false,false,false,true);
        if(recargoForm.getNombreEspecialidad().equals(""))
            es=true;
        if(recargoForm.getNombreServicio().equals(""))
            se=true;
        recargoForm.resetConsulta(es,se,false);
        this.cerrarConexion(con);
		return mapping.findForward("consultar");
    }

    /**
     * Metodo para realizar la consulta avanzada para Modificar segun los parametros elegidos.
     * @param con, Connection con la fuente de datos.
     * @param mapping Mapping para manejar la navegación
     * @param recargoForm RecargoTarifasForm
     * @return findForward, buscar
     * @see struts-config, ACTION RECARGOS
     */
    private ActionForward accionBusquedaAvanzada(Connection con, ActionMapping mapping, RecargoTarifasForm recargoForm) 
    {
        RecargoTarifas mundo = new RecargoTarifas ();
        mundo.reset();
        if(recargoForm.getCheckEspecialidad().equals("on"))
            recargoForm.setCodigoEspecialidad(Integer.parseInt(recargoForm.getNombreEspecialidad()));
                
        if(recargoForm.getCheckServicio().equals("on"))
           recargoForm.setCodigoServicio(Integer.parseInt(recargoForm.getNombreServicio()));
        
        recargoForm.setColeccionAvanzadaModificar( mundo.consultaAvanzadaRecargo(con, 
																                recargoForm.getPorcentaje(),
																                recargoForm.getValor(),
																                recargoForm.getCodigoViaIngreso(),
																                recargoForm.getTipoPaciente(),
																                recargoForm.getCodigoEspecialidad(),
																                recargoForm.getCodigoContrato(),
																                recargoForm.getCodigoServicio(),
																                recargoForm.getCodigoTipoRecargo(),
																                recargoForm.getNombreEspecialidad(),
																                recargoForm.getNombreServicio()));
        //  HashMap hashRecargos=new HashMap();
        // recargoForm.setHashRecargos(hashRecargos);
        pasarColHashMap(recargoForm,false,true,false);
        recargoForm.setNumeroRegistrosRecargo(recargoForm.getColeccionAvanzadaModificar().size());
        recargoForm.reset(true,false,false,true);
        
        
        logger.info("MAPA DESPUES DE LA CONSULTA Y CONVERSION >>>> "+recargoForm.getHashRecargos());

        this.cerrarConexion(con);
        return mapping.findForward("busquedaModificar");
    }

    /**
     * Metodo que inserta en los datos del formularion en la BD.
     * @param con, Connection con la fuente de datos
     * @param mapping Mapping para manejar la navegación
     * @param recargoForm RecargoTarifasForm
     * @return findForward, recargoTarifas
     * @see struts-config, ACTION RECARGOS
     * @throws SQLException
     */
    private ActionForward accionSalirGuardar(Connection con, 
												            ActionMapping mapping, 
												            RecargoTarifasForm recargoForm,
												            HttpServletRequest request) throws SQLException 
    {
       int resp = 0, existeRecargo;
       String forward="";
     
       RecargoTarifas mundo = new RecargoTarifas ();
       mundo.reset();
       
       existeRecargo=verificarRecargoExistente(con,recargoForm);
       
       recargoForm.setExisteRecargo(false);
       
       if (existeRecargo == 0)
       {
           resp=mundo.insertarRecargoTarifas(con, 
								               recargoForm.getPorcentaje(),
								               recargoForm.getValor(),
								               recargoForm.getCodigoViaIngreso(),
								               recargoForm.getTipoPaciente(),
								               recargoForm.getCodigoEspecialidad(),
								               recargoForm.getCodigoContrato(),
								               recargoForm.getCodigoServicio(),
								               recargoForm.getCodigoTipoRecargo());
           if(resp < 1)
               logger.warn("Error insertando Recargo Tarifas(RecargoTarifasAction - accionSalirGuardar");
           
           else
           {
               recargoForm.setColeccionRecargo(mundo.consultaUnRecargoTarifa(con,mundo.recargoActual(con)));
               forward="paginaResumen";
           }
       }
       
       else if(existeRecargo == 1)
       {
           ActionErrors errores = new ActionErrors();
           errores.add("inserción recargo", new ActionMessage("error.tarifa.existe"));
           logger.warn("Recargo de Tarifa ya existe");
           saveErrors(request, errores);   
           recargoForm.setEstado("empezar");
           this.cerrarConexion(con);                                                                       
           return mapping.findForward("recargoTarifas");

       }
                 
       this.cerrarConexion(con);
       return mapping.findForward(forward);
    }

    /**
     * Metodo de inicialización del formulario.
     * @param con, Connection con la fuente de datos
     * @param mapping Mapping para manejar la navegación
     * @param recargoForm RecargoTarifasForm
     * @return findForward, recargoTarifas
     * @see struts-config, ACTION RECARGOS
     */
    private ActionForward accionEmpezar(Connection con, ActionMapping mapping, RecargoTarifasForm recargoForm) 
    {
        recargoForm.reset(true,true,true,true);	
        this.cerrarConexion(con);
        return mapping.findForward("paginaFiltro");        
    }
    
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezarInsertar.
	 * 
	 * @param recargoForm RecargoTarifasForm para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "recargoTarifas.jsp"
	 * @see struts-config, ACTION RECARGOS
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarInsertar		(ActionMapping mapping, 
															Connection con, RecargoTarifasForm recargoForm) throws SQLException
	{
	    recargoForm.setEstado("empezarInsertar");
	    recargoForm.reset(true,false,true,true);
		this.cerrarConexion(con);
		return mapping.findForward("recargoTarifas");		
	}
    
	/**
	 * Este metodo especifica las acciones a realizar en el estado 
	 * buscarModificar
     * @param mapping Mapping para manejar la navegación
     * @param con, Connection con la fuente de datos
     * @param recargoForm  RecargoTarifasForm
     * @return findForward, buscar
     * @see struts-config, ACTION RECARGOS
     */
    private ActionForward accionBuscar(ActionMapping mapping, Connection con, RecargoTarifasForm recargoForm) 
    {
       
        recargoForm.reset(true,false,false,true);
        this.cerrarConexion(con);
		return mapping.findForward("busquedaModificar");	
    }

	
    /**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	public void cerrarConexion (Connection con)
	{
			try{
				if (con!=null&&!con.isClosed())
				{
					UtilidadBD.closeConnection(con);
				}
			}
			catch(Exception e){
				logger.error("Error al tratar de cerrar la conexion con la fuente de datos en RecargoTarifasAction. \n Excepcion: " +e);
			}
	}
	
	
	/**
	 * Metodo encargado de verificar la existencia de recargo de tarifas.
	 * @param con, Connection con la fuente de datos
	 * @param recargoForm. RecargoTarifasForm
	 * @return int, 1 si existe recargo, 0 de lo contrario.
	 */
	public int verificarRecargoExistente (Connection con,RecargoTarifasForm recargoForm)
	{
	    RecargoTarifas mundo = new RecargoTarifas ();
	    mundo.reset();
	    
	    int resp=0;
	    
	    recargoForm.setColeccionTodos(mundo.consultarTodosRecargos(con));
	    pasarColHashMap(recargoForm,true,false,false);
	    
	    for(int posHash=0; posHash < recargoForm.getColeccionTodos().size(); posHash++) 
	    {
	        if(recargoForm.getCodigoContrato() == Utilidades.convertirAEntero(recargoForm.getHashRecargos("contrato_"+posHash)+""))
	            if(recargoForm.getCodigoEspecialidad() == Utilidades.convertirAEntero(recargoForm.getHashRecargos("especialidad_"+posHash)+""))
	                if( recargoForm.getCodigoServicio() == Utilidades.convertirAEntero(recargoForm.getHashRecargos("servicio_"+posHash)+""))
	                    if(recargoForm.getCodigoTipoRecargo() == Utilidades.convertirAEntero(recargoForm.getHashRecargos("tipoRecargo_"+posHash)+""))
	                        if(recargoForm.getCodigoViaIngreso() == Utilidades.convertirAEntero(recargoForm.getHashRecargos("viaIngreso_"+posHash)+""))
		                        if(recargoForm.getTipoPaciente() == recargoForm.getHashRecargos("tipoPaciente_"+posHash)+"")
		                            resp = 1;
	    }
	       
	    return resp;
	}
	
	/**
	 * Metodo para pasar los datos de una colecci&oacute;n
	 * a un HashMap.
	 * @param adminForm AdminMedicamentosForm
	 * @param con Connection con la fuente de datos.
	 * 
	 */
	protected String[] pasarColHashMap(RecargoTarifasForm recargoForm, boolean map_1,boolean map_2,boolean map_3)
	{
	    if(map_1)
	    {
		    String[] stringArray =  null; 
		    String[] columns =  {
			   	      "contrato",
			   	      "especialidad",
			   	      "servicio",
			   	      "tipo_recargo",
			   	      "via_ingreso",
			   	      "tipo_paciente"
				};
		 
		    stringArray=getDataFromCollection(columns,recargoForm.getColeccionTodos());
		    
		 	int pos=0;
		 	
		 	for(int posHash=0; posHash < recargoForm.getColeccionTodos().size(); posHash++) 
		    {
		 	   recargoForm.setHashRecargos("contrato_"+posHash,stringArray[pos]); pos ++;
		 	   
		 	   if((stringArray[pos]+"").equals("null"))
		 	   {
		 	      recargoForm.setHashRecargos("especialidad_"+posHash,0 +""); pos ++;
		 	   }
		 	   else
		 	   {
		 	      recargoForm.setHashRecargos("especialidad_"+posHash,stringArray[pos]); pos ++;
		 	   }
		 	   if((stringArray[pos]+"").equals("null"))
		 	   {
		 	      recargoForm.setHashRecargos("servicio_"+posHash,0 +"");pos ++; 
		 	   }
		 	   else
		 	   {
		 	       recargoForm.setHashRecargos("servicio_"+posHash,stringArray[pos]);pos ++;
		 	   }
		 	   
		 	   recargoForm.setHashRecargos("tipoRecargo_"+posHash,stringArray[pos]);pos ++;
		 	  
		 	   if((stringArray[pos]+"").equals("null"))
		 	  {
		 	     recargoForm.setHashRecargos("viaIngreso_"+posHash,0 +"");pos ++;
		 	  }
		 	  else
		 	  {
		 	      recargoForm.setHashRecargos("viaIngreso_"+posHash,stringArray[pos]);pos ++;
		 	  }
		 	  logger.info("TIPO PACIENTE EN LOS IF >>>>>"+stringArray[pos]); 
		 	  if((stringArray[pos]+"").equals("null"))
		 	  {
		 	     recargoForm.setHashRecargos("tipoPaciente_"+posHash,0 +"");pos ++;
		 	  }
		 	  else
		 	  {
		 	      recargoForm.setHashRecargos("tipoPaciente_"+posHash,stringArray[pos]);pos ++;
		 	  }
		    }
	    }
	    
	    if(map_2)
	    {
	        String[] stringAvanzada =  null; 
	        
		    String[] columns =  {
						   	      "codigo",
						   	      "porcentaje",
						   	      "valor",
						   	      "via_ingreso",
						   	      "tipo_paciente",
						   	      "especialidad",
						   	      "contrato",
						   	      "servicio",
						   	      "tipo_recargo",
						   	      "numero_contrato",
						   	      "nombre_convenio",
						   	      "nombre_via",
						   	      "nombre_recargo",
						   	      "nombre_especialidad",
						   	      "nombre_servicio"
						   	    };
		 
		    stringAvanzada=getDataFromCollection(columns,recargoForm.getColeccionAvanzadaModificar()); 
		    
		    int pos=0;
		 	
		 	for(int posHash=0; posHash < recargoForm.getColeccionAvanzadaModificar().size(); posHash++) 
		    {
		 	   
		 		
		 	   recargoForm.setHashRecargos("codigo_"+posHash,stringAvanzada[pos]); 				pos ++;
		 	   recargoForm.setHashRecargos("porcentaje_"+posHash,stringAvanzada[pos]); 			pos ++;
		 	   recargoForm.setHashRecargos("valor_"+posHash,stringAvanzada[pos]); 				pos ++;
		 	   recargoForm.setHashRecargos("via_ingreso_"+posHash,stringAvanzada[pos]); 		pos ++;
		 	   logger.info("TIPO PACIENTE EN EL FOR POSHASH >>>>>>> "+stringAvanzada[pos]);
		 	   recargoForm.setHashRecargos("tipo_paciente_"+posHash,stringAvanzada[pos]); 		pos ++;
		 	   recargoForm.setHashRecargos("especialidad_"+posHash,stringAvanzada[pos]); 		pos ++;
		 	   recargoForm.setHashRecargos("contrato_"+posHash,stringAvanzada[pos]); 			pos ++;
		 	   recargoForm.setHashRecargos("servicio_"+posHash,stringAvanzada[pos]); 			pos ++;
		 	   recargoForm.setHashRecargos("tipo_recargo_"+posHash,stringAvanzada[pos]); 		pos ++;
		 	   recargoForm.setHashRecargos("numero_contrato_"+posHash,stringAvanzada[pos]); 	pos ++;
		 	   recargoForm.setHashRecargos("nombre_convenio_"+posHash,stringAvanzada[pos]);		pos ++;
		 	   recargoForm.setHashRecargos("nombre_via_"+posHash,stringAvanzada[pos]); 			pos ++;
		 	   recargoForm.setHashRecargos("nombre_recargo_"+posHash,stringAvanzada[pos]); 		pos ++;
		 	   recargoForm.setHashRecargos("nombre_especialidad_"+posHash,stringAvanzada[pos]); pos ++;
		 	   recargoForm.setHashRecargos("nombre_servicio_"+posHash,stringAvanzada[pos]); 	pos ++;
		    }
	    }
	    
	    if(map_3)
	    {
	        String[] stringModificados =  null; 
	        
		    String[] columns =  {
						   	      "codigo",					//0
						   	      "porcentaje",				//1
						   	      "valor",					//2
						   	      "via_ingreso", 			//3
						   	      "tipo_paciente",			//4
						   	      "especialidad",			//5
						   	      "contrato",				//6
						   	      "servicio",				//7
						   	      "tipo_recargo",			//8
						   	      "numero_contrato",		//9
						   	      "nombre_convenio",		//10
						   	      "nombre_via",				//11	
						   	      "nombre_recargo",			//12
						   	      "nombre_especialidad",	//13
						   	      "nombre_servicio"			//14
						   	    };
		 
		    stringModificados=getDataFromCollection(columns,recargoForm.getColeccionTodos()); 
		    
		    return stringModificados;
	    }
        
	    return null;
	 	
	}
	
	/**
	   * Método que dado un arreglo con los elementos
	   * necesarios y una colección de HashMaps,
	   * devuelve un arreglo con los datos presentes
	   * en los HashMap
	   * 
	   * @param selectedColumns Columnas a consultar
	   * dentro del HashMap
	   * @param col Colección de HashMaps
	   * @return stringArray
	   */
	  public String[] getDataFromCollection(String[] selectedColumns, Collection col)
	  {
		 ArrayList arrayList = new ArrayList();
		 String[] stringArray =  null; 		
		 HashMap dyn;
		  int i, k;
		  if (col==null)
		  {
		  	return new String[0];
		  }
		  
		  Iterator it=col.iterator();
		  k=0;
		  while (it.hasNext())
		  {
	      	dyn=(HashMap)it.next();
		    for(i=0; i<selectedColumns.length; i++)
		    {
		      	arrayList.add(k, dyn.get(selectedColumns[i]));
		      	k++;
		    }
		  }
		  
		   stringArray = new String[arrayList.size()];
			
		   for(i=0; i<arrayList.size(); i++)
		     stringArray[i] = arrayList.get(i) + "";
		 
		   return stringArray;
	  }

}
