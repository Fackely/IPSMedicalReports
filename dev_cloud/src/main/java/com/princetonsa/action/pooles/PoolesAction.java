/*
 * PoolesAction.java 
 * Autor		:  jarloc
 * Creado el	:  16-dic-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.action.pooles;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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

import com.princetonsa.actionform.pooles.PoolesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pooles.Pooles;

/**
 * Clase para el manejo de registro de pooles, 
 * ingreso, modificación, eliminación y consulta 
 *
 * @version 1.0, 16-dic-2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class PoolesAction  extends Action{
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(PoolesAction.class);

		
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {

		Connection con = null;
		try{

			if( form instanceof PoolesForm )
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				PoolesForm poolesForm = (PoolesForm)form;
				HttpSession sesion = request.getSession();

				UsuarioBasico usuario = null;
				usuario = getUsuarioBasicoSesion(sesion);
				if(usuario == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				String estado = poolesForm.getEstado();
				logger.warn("estado->"+estado);
				String accion = poolesForm.getAccion();
				logger.warn("accion->"+accion);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de Poolesaction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))//estado para listar los pooles y enviar a la pagina principal
				{
					Pooles mundoPooles = new Pooles ();
					mundoPooles.reset();
					poolesForm.reset();
					poolesForm.setMapGeneral(mundoPooles.consultarTerceros(con));

					return this.accionListarPooles(con,poolesForm,mapping,usuario,response,true,false);//accion para listar (metodos)			    
				}
				else if (estado.equals("redireccion"))// estado para mantener los datos del pager
				{
					UtilidadBD.cerrarConexion(con);
					poolesForm.getLinkSiguiente();
					response.sendRedirect(poolesForm.getLinkSiguiente());
					return null;
				}
				else if (estado.equals("nuevoNit"))//estado para cargar el número de identificación del responsable seleccionado en la vista
				{
					return this.accionCargarNit(con,poolesForm,mapping);//accion para consultar el nit del responsable	
				}
				else if (estado.equals("ingresarNuevo"))
				{
					return this.accionIngresarNuevo(con,poolesForm,request,response,usuario);//accion para listar (metodos)	
				}
				else if (estado.equals("eliminarRegistro"))
				{
					return this.accionEliminar(con,poolesForm,mapping,request);//accion para eliminar un registro de memoria o BD
				}
				else if (estado.equals("salirGuardar"))
				{
					return this.accionSalirGuardar(con,poolesForm,mapping,usuario,response,sesion,request);//accion para eliminar un registro de memoria o BD
				}
				else if (estado.equals("busquedaAvanzada"))//estado para cargar la página de Busqueda Avanzada sobre la principal
				{
					this.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if (estado.equals("buscar"))
				{
					return this.accionBuscar(con,poolesForm,mapping,usuario,response);//acción para realizar la busqueda Avanzada		    
				}
				else if (estado.equals("consultar"))//estado para enviar a lapágina de consulta y busqueda avanzada
				{
					Pooles mundoPooles = new Pooles ();
					mundoPooles.reset();
					poolesForm.reset();
					poolesForm.setCodigoInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
					poolesForm.setColPooles(mundoPooles.consultaPooles(con, Integer.parseInt(usuario.getCodigoInstitucion())));
					this.cerrarConexion(con);
					return mapping.findForward("paginaConsulta"); 		    
				}
				else if (estado.equals("busquedaAvanzadaConsultar"))//estado para cargar la página de Busqueda Avanzada de consulta sobre la principal
				{
					this.cerrarConexion(con);
					return mapping.findForward("paginaConsulta");		    
				}
				else if (estado.equals("buscarConsultar"))//estado para realizar la consulta avanzada de la página de consultar
				{
					Pooles mundoPooles = new Pooles ();
					mundoPooles.reset();
					poolesForm.setColPooles(mundoPooles.consultaAvanzadaPoolesConsultar(con,
							poolesForm.getMapGeneral("descripcionBuscar")+"",
							poolesForm.getMapGeneral("terceroBuscar_"+0)+"",
							poolesForm.getMapGeneral("nitBuscar")+"",
							Integer.parseInt(poolesForm.getMapGeneral("activoBuscar_"+0)+"") ));


					poolesForm.setAccion("");  //inicializar la accion, sobre todo para que no ver nuevamente la parte de busquedaAvanzada en la vista
					this.cerrarConexion(con);
					return mapping.findForward("paginaConsulta");		    
				}
				else
				{
					this.cerrarConexion(con);
				}

			}
			else
			{
				logger.error("El form no es compatible con el form de Pooles");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
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
	 * Metodo para realizar la busqueda Avanzada
     * @param con, Connection con la fuente de datos
     * @param poolesForm, PoolesForm
     * @param mapping     
     * @return findForward, paginaPrincipal
     */
    private ActionForward accionBuscar(Connection con, 
											            PoolesForm poolesForm, 
											            ActionMapping mapping,
											            UsuarioBasico usuario,
											            HttpServletResponse response) 
    {
        return this.accionListarPooles(con,poolesForm,mapping,usuario,response,false,true);       
    }


    /**
     * Metodo para ingresar nuevo, modificar ó eliminar
     * segun el evento en el formulario
     * @param con, Connection  con la fuente de datos
     * @param poolesForm, PoolesForm
     * @param mapping, ActionMapping
     * @param request, HttpServletRequest
     * @param session, HttpSession
     * @return findForward, paginaPrincipal
     */
    private ActionForward accionSalirGuardar(Connection con, 
												            PoolesForm poolesForm, 
												            ActionMapping mapping, 
												            UsuarioBasico usuario,
												            HttpServletResponse response,
												            HttpSession session,
												            HttpServletRequest request) //@todo accionSalirGuardar
    {
        Pooles mundoPooles = new Pooles ();
        mundoPooles.reset();
        
        int posHash = poolesForm.getIterAccionesRegistros();
        
        for( int i = 0; i < poolesForm.getCodigosEliminar().size(); i ++ )//para eliminar los que pertenecen a la BD
        {
            
            try 
            {
                logEliminados(con,poolesForm,Integer.parseInt(poolesForm.getCodigosEliminar().get(i)+""));//llenar el log de eliminación
                
                boolean siElimino = mundoPooles.insertarModificarEliminarTransaccion(con,"",-1,-1,
																			                        Integer.parseInt(poolesForm.getCodigosEliminar().get(i)+""),
																			                        false,false,true);
                if(siElimino)
            	{
            	    poolesForm.setMapGeneral("valorRegistro_"+posHash,poolesForm.getMapRegistros("codigo_"+i));//almacena los registros eliminados (códigos)
            	    poolesForm.setMapGeneral("estadoRegistro_"+posHash,"drop");//el estado del registro (eliminado)
            	    poolesForm.setIterAccionesRegistros(+1);
            	    
            	    usuario=(UsuarioBasico)session.getAttribute("usuarioBasico");// generar el log de la eliminación
    	            LogsAxioma.enviarLog(ConstantesBD.logRegistroPoolesCodigo,poolesForm.getLog(),ConstantesBD.tipoRegistroLogEliminacion,usuario.getLoginUsuario());            	    
            	}
            } 
            catch (SQLException e) 
            {
                logger.warn("error eliminando el pool con el código "+poolesForm.getCodigosEliminar().get(i));
            }
        }
                
        for(int j = 0;j < poolesForm.getNumRegistros(); j ++)
        {
            if((poolesForm.getMapRegistros("tipo_"+j)+"").equals("MEM"))//para guardar solo los nuevos
            {
                boolean existeRegistro = existeRegistroEnBD(con,usuario,poolesForm,poolesForm.getMapRegistros("descripcion_"+j)+"");
                
                if(!existeRegistro)
                {
	                try {
		                	String temp;
	                		if (poolesForm.getMapRegistros("diasVencimiento_"+j).equals(""))
	                			temp = ConstantesBD.codigoNuncaValido+"";
	                		else
	                			temp = poolesForm.getMapRegistros("diasVencimiento_"+j).toString();
	                		mundoPooles.setDiasVencFactura(Utilidades.convertirAEntero(temp));
	                		mundoPooles.setCuentaXpagar(Utilidades.convertirAEntero(poolesForm.getMapRegistros("cuentaXPagar_"+j)+""));
	                    	boolean siInserto=mundoPooles.insertarModificarEliminarTransaccion(con,
													                            (poolesForm.getMapRegistros("descripcion_"+j)+"").toUpperCase(),
													                            Integer.parseInt(poolesForm.getMapRegistros("tercero_"+j)+""),
													                            Integer.parseInt(poolesForm.getMapRegistros("activo_"+j)+""),
													                            -1,//solo para eliminar (por eso va en -1)
													                            true,false,false );
	                    	if(siInserto)
	                    	{
	                    	    poolesForm.setMapGeneral("valorRegistro_"+posHash,poolesForm.getMapRegistros("codigo_"+j));//almacena los registros nuevos (códigos)
	                    	    poolesForm.setMapGeneral("estadoRegistro_"+posHash,"new");//el estado del registro (nuevo)
	                    	    poolesForm.setIterAccionesRegistros(+1);
	                    	}
	                
	                } 
	                catch (SQLException e) 
	                {
	                    logger.warn("error insertando el pool con el código "+poolesForm.getMapRegistros("codigo_"+j));
	                }   
                }
                else if(existeRegistro)
                {
                  logger.warn("No se puede adicionar el registro con la descripción "+poolesForm.getMapRegistros("descripcion_"+j)); 
          	      ActionErrors errores = new ActionErrors();
          	      errores.add("existeRegistroEnBD", new ActionMessage("error.pooles.existeDescripcion",poolesForm.getMapRegistros("descripcion_"+j)+""));
          	      saveErrors(request, errores);          	      
                }
            }
        }     
        
        verificarModificados(con,poolesForm);  
        
        for(int l = 0;l < poolesForm.getNumRegistros(); l ++)//para modificar los que pertenecen a la BD
        {
           if((poolesForm.getMapRegistros("tipoModificado_"+l)+"").equals("Mod"))
            {
                try {
                		String temp;
                		if (poolesForm.getMapRegistros("diasVencimiento_"+l).equals(""))
                			temp = ConstantesBD.codigoNuncaValido+"";
                		else
                			temp = poolesForm.getMapRegistros("diasVencimiento_"+l).toString();
                		mundoPooles.setDiasVencFactura(Utilidades.convertirAEntero(temp));
                		mundoPooles.setCuentaXpagar(Utilidades.convertirAEntero(poolesForm.getMapRegistros("cuentaXPagar_"+l)+""));
                		
                    	boolean siModifico=mundoPooles.insertarModificarEliminarTransaccion(con,
												                            poolesForm.getMapRegistros("descripcion_"+l)+"",
												                            Integer.parseInt(poolesForm.getMapRegistros("tercero_"+l)+""),
												                            Integer.parseInt(poolesForm.getMapRegistros("activo_"+l)+""),
												                            Integer.parseInt(poolesForm.getMapRegistros("codigo_"+l)+""),
												                            false,true,false );
                    	if(siModifico)
                    	{
                    	    llenarloghistorial(poolesForm,l);
                    	    generarLog(poolesForm,l,session);
                    	}
                    	
                    	if(!siModifico)
                    	{
                    	    logger.warn("no modifico el registro pooles");
                    	}
                
                } 
                catch (SQLException e) {
                    logger.warn("error insertando el pool con el código "+poolesForm.getMapRegistros("codigo_"+l));
                }   
            }
        }
        
        return this.accionListarPooles(con,poolesForm,mapping,usuario,response,true,false);
    }
    
    /**
     * Metodo para verificar sin un registro ya se encuentra en la BD,
     * por medio de la descripción del mismo.
     * @param con Connection con la fuente de datos
     * @param usuario, UsuarioBasico
     * @param poolesForm, PoolesForm
     * @param descripcion, String del registro
     * @return
     */
    public boolean existeRegistroEnBD (Connection con,
            										UsuarioBasico usuario,
										            PoolesForm poolesForm,
										            String descripcion)
    {
        Pooles mundoPooles = new Pooles ();
        mundoPooles.reset();   
        boolean existeRegistro = false;
        poolesForm.setCodigoInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
        poolesForm.setMapRegistrosSinModificar(mundoPooles.consultarRegPooles( con,poolesForm.getCodigoInstitucion())); //un clon de los datos que existen      
        																												//en la BD, para no sobreescribir
        for(int j = 0;j < Integer.parseInt(poolesForm.getMapRegistrosSinModificar("numRegistros")+""); j ++)			//lo que se han ingresado como nuevos
        {
	         if( (poolesForm.getMapRegistrosSinModificar("descripcion_"+j)+"").equals(descripcion) )
	         {
	             existeRegistro = true;  
	         }
	         if(existeRegistro)
	             break ;
        }
       
       return existeRegistro;
    }
    
    /**
     * Metodo para generar el Log de los registro eliminados
     * @param poolesForm, PoolesForm
     * @param codigo, Código del registro eliminado
     */
    public void logEliminados (Connection con,
									            PoolesForm poolesForm, 
									            int codigo)
    {
        String activo ="";
        
        Pooles mundoPooles = new Pooles ();
        mundoPooles.reset();
        poolesForm.setMapRegistrosSinModificar(mundoPooles.consultarRegPooles( con,poolesForm.getCodigoInstitucion()));//un clon del mapa original sin modificaciones
        
       for ( int k = 0; k < Integer.parseInt(poolesForm.getMapRegistrosSinModificar("numRegistros")+""); k ++)
       {
            
           if( codigo == Integer.parseInt(poolesForm.getMapRegistrosSinModificar("codigo_"+k)+"") )
           {
                if( (poolesForm.getMapRegistrosSinModificar("activo_"+k)+"").equals("1") )
	      	         activo = "si";
	      	     else
	      	         activo = "no";  
	               
	            poolesForm.setLog(
						   		  	"\n            =====REGISTRO ELIMINADO===== " +
						   		  	"\n*  Código Pool ["+poolesForm.getMapRegistrosSinModificar("codigo_"+k)+""+"] " +
						   		  	"\n*  Descripción  ["+poolesForm.getMapRegistrosSinModificar("descripcion_"+k)+""+"] "+
						   		  	"\n*  Nombre del Responsable ["+poolesForm.getMapRegistrosSinModificar("nombreResponsable_"+k)+""+"] "+
						   		  	"\n*  Nit del Responsable ["+poolesForm.getMapRegistrosSinModificar("nit_"+k)+""+"] "+
						   		  	"\n*  Activo ["+activo+"] "+	 	
						   		  	"\n========================================================\n\n\n "
	   		  					);	                 
           }           
       }            
    }

    /**
     * Metodo para verificar que registros fueron modificados (HashMap)
     * @param poolesForm, PoolesForm      
     */
    public void verificarModificados (Connection con,PoolesForm poolesForm)//@todo verificarModificados
    {
        boolean esModificado = false;
        Pooles mundoPooles = new Pooles ();
        mundoPooles.reset();
        
        int codigo=0;
        poolesForm.setMapRegistrosSinModificar(mundoPooles.consultarRegPooles( con,poolesForm.getCodigoInstitucion()));//un clon del mapa original sin modificaciones
        for(int l = 0;l < poolesForm.getNumRegistros(); l ++)
        {
            codigo = Integer.parseInt(poolesForm.getMapRegistros("codigo_"+l)+"");
            for(int k = 0; k < Integer.parseInt(poolesForm.getMapRegistrosSinModificar("numRegistros")+""); k++)        
	        {
	            if( codigo == Integer.parseInt(poolesForm.getMapRegistrosSinModificar("codigo_"+k)+"") )
	            {
	                
	               if( !(poolesForm.getMapRegistrosSinModificar("descripcion_"+k)+"").equals(poolesForm.getMapRegistros("descripcion_"+k)+"") )
	                {
	                    esModificado = true;	  
	                }
	               if( !(poolesForm.getMapRegistrosSinModificar("tercero_"+k)+"").equals(poolesForm.getMapRegistros("tercero_"+k)+"") )
	                {
	                    esModificado = true;	
	                }
	               if( !(poolesForm.getMapRegistrosSinModificar("activo_"+k)+"").equals(poolesForm.getMapRegistros("activo_"+k)+"") )
	                {
	                    esModificado = true;	
	                }
	               if( !(poolesForm.getMapRegistrosSinModificar("diasVencimiento_"+k)+"").equals(poolesForm.getMapRegistros("diasVencimiento_"+k)+"") )
	                {
	                    esModificado = true;	
	                }
	               if( !(poolesForm.getMapRegistrosSinModificar("cuentaXPagar_"+k)+"").equals(poolesForm.getMapRegistros("cuentaXPagar_"+k)+"") )
	                {
	                    esModificado = true;	
	                }
	               if(!esModificado)
	               {
	                   poolesForm.setMapRegistros("tipoModificado_"+l,"noMod");	                     
	               }
	               
	               if(esModificado)
	                {
	                    poolesForm.setMapRegistros("tipoModificado_"+l,"Mod");
	                    esModificado = false;
	                }	              
	            }	           
	        }
        } 
    }
    /**
	 * Metodo implementado para eliminar un registro de 
	 * la BD o de la memoria
     * @param con, Connection con la fuente de datos
     * @param poolesForm,  PoolesForm
     * @param mapping, ActionMapping
     * @param request, HttpServletRequest
     * @return findForward, paginaPrincipal
     */
    private ActionForward accionEliminar(Connection con, 
											            PoolesForm poolesForm, 
											            ActionMapping mapping,
											            HttpServletRequest request) //@todo accionEliminar
    {
        Pooles mundoPooles = new Pooles ();
        mundoPooles.reset();
        boolean existeRelacion = false;
        
        //se adiciona el codigo verdadero que sera eliminado, el anterior era una referencia en el HashMap
        
        if( (poolesForm.getMapRegistros("tipo_"+poolesForm.getCodigoEliminar())+"").equals("BD") )
        {
	        existeRelacion = mundoPooles.existeRelacionPoolToMedicosXPool(con,Integer.parseInt( poolesForm.getMapRegistros("codigo_"+poolesForm.getCodigoEliminar())+""));	       
        }      
       
        if(existeRelacion)
	    {
	      logger.warn("No se puede eliminar el registro con el código "+poolesForm.getCodigoEliminar()); 
	      ActionErrors errores = new ActionErrors();
	      errores.add("violaRelacionMedicosXPooles", new ActionMessage("error.pooles.existeRelacionConMedicosXPool",poolesForm.getMapRegistros("descripcion_"+poolesForm.getCodigoEliminar())+""));
	      saveErrors(request, errores);	
	      this.cerrarConexion(con);									
	      return mapping.findForward("paginaPrincipal");
	    }	
        else if(!existeRelacion)
            eliminarRegistro(poolesForm,poolesForm.getCodigoEliminar());     
       
        this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");   
    }


    /**
	 * Metodo para adicionar un nuevo registro al HashMap
     * @param con, Connection con la fuente de datos
     * @param poolesForm
     * @param mapping
     * @return findForward, paginaPrincipal
     */
    private ActionForward accionIngresarNuevo(Connection con, 
													            PoolesForm poolesForm, 
													            HttpServletRequest request, 
														        HttpServletResponse response,
														        UsuarioBasico usuario) //@todo accionIngresarNuevo
    {
        //esta validación en el caso de realizar busquedaAvanzada y que no se listen resultados, y se ingrese uno nuevo continuar con la secuencia de los que hay en la BD
        if( Integer.parseInt(poolesForm.getMapGeneral("numRegistros")+"") == 0 && ( poolesForm.getAccion().equals("busquedaAvanzada") || poolesForm.getAccion().equals("nuevoNitBuscar")) ) 
        {
            listarEspecial(con,poolesForm,usuario);
        }
        //esta validación en el caso de tener resultado en la busqueda y se va adicionar uno nuevo, listar los que están en la BD, y continuar con la secuencia
        if( poolesForm.getEstado().equals("ingresarNuevo") && poolesForm.getAccion().equals("busquedaAvanzada") )
        {
            listarEspecial(con,poolesForm,usuario);
        }
                
        int posicion = Integer.parseInt(poolesForm.getMapRegistros("numRegistros")+"") ;
               
        //se adiciona el nuevo registro dentro del HashMap 
        poolesForm.setMapRegistros("registroNumero_"+posicion,poolesForm.getMapRegistros("numRegistros")); //reemplazr el codigo por este, para eliminar y todo lo demas
        poolesForm.setMapRegistros("codigo_"+posicion,-1+"");        
        poolesForm.setMapRegistros("descripcion_"+posicion,"");
        poolesForm.setMapRegistros("tercero_"+posicion,-1+"");
        poolesForm.setMapRegistros("activo_"+posicion,-1+"");
        poolesForm.setMapRegistros("nit_"+posicion,"");
        poolesForm.setMapRegistros("nombreResponsable_"+posicion,"seleccione");
        poolesForm.setMapRegistros("diasVencimiento_"+posicion,"");
        poolesForm.setMapRegistros("cuentaXPagar_"+posicion,"-1");
        
        adicionarTipoRegistro(poolesForm,false,posicion);
        
        posicion ++;
        poolesForm.setMapRegistros("numRegistros",posicion+"");//se incrementa el nuevo registro en el HashMap
        
        poolesForm.setNumRegistros(Integer.parseInt(poolesForm.getMapRegistros("numRegistros")+""));//controlar el número de registros
        
        poolesForm.setAccion("");  //inicializar la accion, sobre todo para que no ver nuevamente la parte de busquedaAvanzada en la vista
        return this.redireccion(con,poolesForm,response,request);           
    }

    /**
     * Metodo para consultar pooles 
     * @param con, Connection con la fuente de datos
     * @param poolesForm, PoolesForm
     * @param usuario, UsuarioBasico
     */
    public void listarEspecial (Connection con,PoolesForm poolesForm,UsuarioBasico usuario)
    {
        Pooles mundoPooles = new Pooles ();
        mundoPooles.reset();
        poolesForm.setCodigoInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
        poolesForm.setMapRegistros(mundoPooles.consultarRegPooles( con,poolesForm.getCodigoInstitucion()));        
        poolesForm.setNumRegistros(Integer.parseInt(poolesForm.getMapRegistros("numRegistros")+""));
        
        int ultimoCodigo = Integer.parseInt(poolesForm.getMapRegistros("numRegistros")+"")-1;
        poolesForm.setMapGeneral("registroNumero_"+ultimoCodigo,ultimoCodigo+"");
        
        adicionarTipoRegistro(poolesForm,true,0);	       
    }

    /**
	 * Metodo para cargar el nuevo numero del nit 
     * @param con, Connection con la fuente de daros
     * @param poolesForm
     * @param request
     * @param mapping
     * @return findForward, paginaPrincipal
     */
    private ActionForward accionCargarNit(Connection con, 
												            PoolesForm poolesForm, 
												            ActionMapping mapping) 
    {
       for(int k = 0; k < Integer.parseInt(poolesForm.getMapGeneral("numRegistros")+""); k ++)
        {
            if( Integer.parseInt(poolesForm.getMapGeneral("codigo_"+k)+"") == Integer.parseInt(poolesForm.getMapRegistros("tercero_"+poolesForm.getPosActual())+"") )
            {
                poolesForm.setMapRegistros("nit_"+poolesForm.getPosActual(),poolesForm.getMapGeneral("numeroIdentificacion_"+k));
                poolesForm.setMapRegistros("nombreResponsable_"+poolesForm.getPosActual(),poolesForm.getMapGeneral("descripcion_"+k));
            }
        }       
        
        this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");   
    }


    /**
     * Metodo para cargar los registros de pooles
     * @param con, Connection con la fuente de datos
     * @param poolesForm
     * @param request
     * @param mapping
     * @return findForward, paginaPrincipal
     */
    private ActionForward accionListarPooles(Connection con, 
												            PoolesForm poolesForm, 
												            ActionMapping mapping,
												            UsuarioBasico usuario,
												            HttpServletResponse response,
												            boolean esListar,
												            boolean esAvanzada)//@todo accionListarPooles
    {
        Pooles mundoPooles = new Pooles ();
        mundoPooles.reset();
                  
        if(esListar)
        {
	        poolesForm.setCodigoInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
	        poolesForm.setMapRegistros(mundoPooles.consultarRegPooles( con,poolesForm.getCodigoInstitucion()));	 
	        
        }
        else if(esAvanzada)
        {
            poolesForm.setMapRegistros(mundoPooles.consultaAvanzadaPooles(con,
														                    poolesForm.getMapGeneral("descripcionBuscar")+"",
														                    poolesForm.getMapGeneral("terceroBuscar_"+0)+"",
														                    poolesForm.getMapGeneral("nitBuscar")+"",
														                    Integer.parseInt(poolesForm.getMapGeneral("activoBuscar_"+0)+"") ));            
        }
        
        poolesForm.setNumRegistros(Integer.parseInt(poolesForm.getMapRegistros("numRegistros")+""));
        
        adicionarTipoRegistro(poolesForm,true,0);

        this.cerrarConexion(con);
        
        if( poolesForm.getEstado().equals("buscar") && poolesForm.getAccion().equals("busquedaAvanzada") )//para redireccionar a la primera pagina del
        {																									//pager, cuando es busquedaAvanzada
            //poolesForm.setAccion("");  //inicializar la accion, sobre todo para que no ver nuevamente la parte de busquedaAvanzada en la vista
            try 
            {                
             response.sendRedirect("registroPooles.jsp?pager.offset="+0);
            } 
            catch (IOException e) 
            {
                
                e.printStackTrace();
            }
        }
        else
            return mapping.findForward("paginaPrincipal");
        
        return null;
    }
    
    
    /**
     * Metodo implementado para posicionarse en la ultima
     * pagina del pager.
     * @param con, Connection con la fuente de datos.
     * @param poolesForm
     * @param response
     * @param request
     * @return null
     */
    public ActionForward redireccion (	Connection con,
						            					PoolesForm poolesForm,
						            					HttpServletResponse response,
						            					HttpServletRequest request)
    {
        
        if(request.getParameter("ultimaPage")==null)
        {
           if(poolesForm.getNumRegistros() > (poolesForm.getOffset()+poolesForm.getMaxPageItems()))
                poolesForm.setOffset(poolesForm.getOffset()+poolesForm.getMaxPageItems());
            
            try 
            {
                
                response.sendRedirect("registroPooles.jsp?pager.offset="+poolesForm.getOffset());
            } catch (IOException e) 
            {
                
                e.printStackTrace();
            }
        }
        else
        {    
            String ultimaPagina=request.getParameter("ultimaPage");
            
            int posOffSet=ultimaPagina.indexOf("offset=")+7;
            poolesForm.setOffset((Integer.parseInt(ultimaPagina.substring(posOffSet,ultimaPagina.length() ))));
                
            if(poolesForm.getNumRegistros()>(poolesForm.getOffset()+poolesForm.getMaxPageItems()))
                poolesForm.setOffset(poolesForm.getOffset()+poolesForm.getMaxPageItems());
             
            try 
            {
                
                response.sendRedirect(ultimaPagina.substring(0,posOffSet)+poolesForm.getOffset());
            } 
            catch (IOException e) 
            {
                
                e.printStackTrace();
            }
     }
       this.cerrarConexion(con);
       return null;
    }

    /**
     * Metodo para adicionar el tipo de registro
     * si es de BD o de Memoria
     * @param poolesForm, PoolesForm
     * @param esBD, boolean true si es de BD, false si es de memoria
     * @param posMEM, int posicion del nuevo registro
     */
    public void adicionarTipoRegistro(PoolesForm poolesForm, boolean esBD, int posMEM )
    {
        if(esBD)
        {
	        for(int k = 0; k < Integer.parseInt(poolesForm.getMapRegistros("numRegistros")+""); k ++)
	        {
	            poolesForm.setMapRegistros("tipo_"+k,"BD");
	            poolesForm.setMapRegistros("registroNumero_"+k,k+""); 
	        }
        }
        else if(!esBD)
        {
            poolesForm.setMapRegistros("tipo_"+posMEM,"MEM");  
            poolesForm.setMapRegistros("registroNumero_"+posMEM,posMEM+"");
        }
    }
    
    /**
     * Metodo para eliminar el registro del HashMap ó 
     * de la Base de datos si no esta relacionado con MedicosXPool
     * @param poolesForm, PoolesForm
     */
    protected void eliminarRegistro(PoolesForm poolesForm,int codigo)
    {
        int k=0;
        int esUltimo=poolesForm.getNumRegistros()-1;
        boolean esPrimero = true;
        
        while(k < poolesForm.getNumRegistros())
        {
            if(codigo == Integer.parseInt(poolesForm.getMapRegistros("registroNumero_"+k)+""))
            {
              if(k == esUltimo && esPrimero)
              {
                  if((poolesForm.getMapRegistros("tipo_"+k)+"").equals("BD"))
                  {
                      poolesForm.setCodigosEliminar(poolesForm.getMapRegistros("codigo_"+k));
                  }
                  poolesForm.getMapRegistros().remove("codigo_"+k);
                  poolesForm.getMapRegistros().remove("descripcion_"+k);
                  poolesForm.getMapRegistros().remove("tercero_"+k);  
                  poolesForm.getMapRegistros().remove("activo_"+k);
                  poolesForm.getMapRegistros().remove("nit_"+k);
                  poolesForm.getMapRegistros().remove("nombreResponsable_"+k);
                  poolesForm.getMapRegistros().remove("tipo_"+k);
                  poolesForm.getMapRegistros().remove("registroNumero_"+k);
                  
                  poolesForm.setMapRegistros("numRegistros",poolesForm.getNumRegistros()-1+"");
                  poolesForm.setNumRegistros(poolesForm.getNumRegistros()-1);
              }
              else
              {    
                  if((poolesForm.getMapRegistros("tipo_"+k)+"").equals("BD"))
                  {
                      poolesForm.setCodigosEliminar(poolesForm.getMapRegistros("codigo_"+k));
                  }
                  poolesForm.getMapRegistros().remove("codigo_"+k);
                  poolesForm.getMapRegistros().remove("descripcion_"+k);
                  poolesForm.getMapRegistros().remove("tercero_"+k);  
                  poolesForm.getMapRegistros().remove("activo_"+k);
                  poolesForm.getMapRegistros().remove("nit_"+k);
                  poolesForm.getMapRegistros().remove("nombreResponsable_"+k);
                  poolesForm.getMapRegistros().remove("tipo_"+k);
                  poolesForm.getMapRegistros().remove("registroNumero_"+k);
                  
                  poolesForm.setMapRegistros("numRegistros",poolesForm.getNumRegistros()-1+"");
                  poolesForm.setNumRegistros(poolesForm.getNumRegistros()-1);
                  moverHashMap(k,poolesForm);
                  esPrimero = false;
              }           
            }
            
            k ++;
        }
    }
    
    /**
     * Metodo para mover las posiciones del 
     * HashMap despues de eliminar un registro
     * @param pos, int con la posicion desde donde se va amover el hash
     * @param poolesForm, PoolesForm
     */
    public void moverHashMap (int pos,PoolesForm poolesForm)
    {
        for(int l = pos; l < poolesForm.getNumRegistros(); l ++)
        {
            poolesForm.setMapRegistros("codigo_"+l,poolesForm.getMapRegistros("codigo_"+(l+1)));
            poolesForm.setMapRegistros("descripcion_"+l,poolesForm.getMapRegistros("descripcion_"+(l+1)));
            poolesForm.setMapRegistros("tercero_"+l,poolesForm.getMapRegistros("tercero_"+(l+1)));
            poolesForm.setMapRegistros("activo_"+l,poolesForm.getMapRegistros("activo_"+(l+1)));
            poolesForm.setMapRegistros("nit_"+l,poolesForm.getMapRegistros("nit_"+(l+1)));
            poolesForm.setMapRegistros("nombreResponsable_"+l,poolesForm.getMapRegistros("nombreResponsable_"+(l+1)));
            poolesForm.setMapRegistros("tipo_"+l,poolesForm.getMapRegistros("tipo_"+(l+1)));
            poolesForm.setMapRegistros("registroNumero_"+l,poolesForm.getMapRegistros("registroNumero_"+(l+1)));
        } 
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
				logger.error("Error al tratar de cerrar la conexion con la fuente de datos MedicosXPoolAction. \n Excepcion: " +e);
			}
	}
	
	 /**
	  * Metodo para cargar el Log con los datos actuales, antes de la 
	  * modificación.
	  * @param poolesForm, PoolesForm Instancia del Form PoolesForm.
	  * @param posicion, Posicion en el HashMap del elemento modificado
	  */
	 private void llenarloghistorial(PoolesForm poolesForm,int posicion)
	    {
	     String activo ="";
	     
	     if( (poolesForm.getMapRegistrosSinModificar("activo_"+posicion)+"").equals("1") )
	         activo = "si";
	     else
	         activo = "no";
	     
	     poolesForm.setLog("\n            ====INFORMACION ORIGINAL===== " +
	            	 			"\n*  Código Pool ["+poolesForm.getMapRegistrosSinModificar("codigo_"+posicion)+""+"] " +
	            	 			"\n*  Descripción  ["+poolesForm.getMapRegistrosSinModificar("descripcion_"+posicion)+""+"] "+
	            	 			"\n*  Nombre del Responsable ["+poolesForm.getMapRegistrosSinModificar("nombreResponsable_"+posicion)+""+"] "+
	            	 			"\n*  Nit del Responsable ["+poolesForm.getMapRegistrosSinModificar("nit_"+posicion)+""+"] "+
	            	 			"\n*  Activo ["+activo+"] "+	 			
	 						"\n\n");
	        
	    }
	 
	 /**
	  * Metodo para generar el Log, y añadir los cambios realizados.
	  * @param poolesForm, Instancia del Form PoolesForm.
	  * @param session, Session para obtener el usuario.
	  */
	 private void generarLog( PoolesForm poolesForm,int posicion, HttpSession session)
	    {
	        UsuarioBasico usuario;
	        
	        String activo ="";
	        
	        if( (poolesForm.getMapRegistros("activo_"+posicion)+"").equals("1") )
		         activo = "si";
		     else
		         activo = "no";
        
	        String log=poolesForm.getLog() +
												"\n            ====INFORMACION DESPUES DE LA MODIFICACION===== " +
												"\n*  Código Pool ["+poolesForm.getMapRegistros("codigo_"+posicion)+""+"] " +
					            	 			"\n*  Descripción  ["+poolesForm.getMapRegistros("descripcion_"+posicion)+""+"] "+
					            	 			"\n*  Nombre del Responsable ["+poolesForm.getMapRegistros("nombreResponsable_"+posicion)+""+"] "+
					            	 			"\n*  Nit del Responsable ["+poolesForm.getMapRegistros("nit_"+posicion)+""+"] "+
					            	 			"\n*  Activo ["+activo+"] "+	
												"\n========================================================\n\n\n " ;
	        usuario=(UsuarioBasico)session.getAttribute("usuarioBasico");
	        LogsAxioma.enviarLog(ConstantesBD.logRegistroPoolesCodigo,log,ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
	        
	    }
	
/**
 * 
 * @param session
 * @return
 */
	 private UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
	 {
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
	    if(usuario == null)
				logger.warn("El usuario no esta cargado (null)");
			
	    return usuario;
	  }
	 
/**
 * 
 * @param con
 * @return
 */
	 public Connection openDBConnection(Connection con)
		{

			if(con != null)
				return con;
			
			try{
				String tipoBD = System.getProperty("TIPOBD");
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				con = myFactory.getConnection();
			}
			catch(Exception e)
			{
				logger.warn("Problemas con la base de datos al abrir la conexion");
				return null;
			}
		
			return con;
		}
		
}
