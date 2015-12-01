
/*
 * Creado   29/11/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
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
import util.UtilidadFecha;

import com.princetonsa.actionform.pooles.MedicosXPoolForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pooles.MedicosXPool;


/**
 * Clase para el manejo de Medicos por pool, 
 * ingreso, modificación, eliminación y consulta 
 *
 * @version 1.0, 29/11/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class MedicosXPoolAction extends Action 
{
    /**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(MedicosXPoolAction.class);
	
	/**
	 * Método execute del action
	 */	
	
	public ActionForward execute(	ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response ) throws Exception
									{
		Connection con = null;
		try{

			if (response==null); //Para evitar que salga el warning
			if(form instanceof MedicosXPoolForm)
			{


				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión -MedicosXPoolAction"+e.toString());
				}

				MedicosXPoolForm medicosPoolForm = (MedicosXPoolForm)form;
				//UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				HttpSession session=request.getSession();
				String estado=medicosPoolForm.getEstado();
				logger.warn("estado ="+estado);
				String accion=medicosPoolForm.getAccion();
				logger.warn("accion ="+accion);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de MedicosXPool (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				if(estado.equals("empezar"))// estado para inicializar 
				{
					MedicosXPool mundo = new MedicosXPool ();
					mundo.reset();
					medicosPoolForm.reset();
					medicosPoolForm.resetMaps(true);
					medicosPoolForm.setAccion("empezar");
					this.cerrarConexion(con);
					return mapping.findForward("ingresarModificar");//Accion que envia a la pagina de insertar/modificar. 
				}
				if(estado.equals("listarMedicos"))// estado para cargar los medicos pertenecientes al pool
				{
					return this.accionListar(con,medicosPoolForm,mapping);// accion cargar el HashMap con los registros de la BD.
				}
				if(estado.equals("ingresarNuevo"))// estado para adicionar un nuevo registro en el hashMap
				{
					return this.accionAdicionar(con,medicosPoolForm,request, response);// accion para reservar el registro en el HashMap					   
				}

				else if (estado.equals("redireccion"))// estado para mantener los datos del pager
				{
					UtilidadBD.cerrarConexion(con);
					String pager=medicosPoolForm.getLinkSiguiente();
					int indice=pager.indexOf("=");
					medicosPoolForm.setOffset(Integer.parseInt(pager.charAt(indice+1)+""));
					response.sendRedirect(medicosPoolForm.getLinkSiguiente());
					this.cerrarConexion(con);
					return null;
				}
				if(estado.equals("salirGuardar"))// estado para insertar y mofdificar
				{
					return this.accionSalirGuardar(con,medicosPoolForm,mapping,request,session);// accion para realizar las modificaciones e inserciones				   
				}
				if(estado.equals("busquedaAvanzada"))// estado para insertar y mofdificar
				{
					this.cerrarConexion(con);
					return mapping.findForward("ingresarModificar");			   			   
				}
				if(estado.equals("buscar"))// estado para realizar busqueda avanzada
				{
					return this.accionBusquedaAvanzada(con,medicosPoolForm,mapping);					   			   
				}
				if(estado.equals("empezarConsulta"))// estado para consultar
				{
					medicosPoolForm.reset();
					this.cerrarConexion(con);
					return mapping.findForward("consultar");						   			   
				}
				if(estado.equals("cancelar"))// estado para consultar
				{
					this.cerrarConexion(con);
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
     * @param con
     * @param medicosPoolForm
     * @param request
     * @param response
     * @return
     */
    private ActionForward accionBusquedaAvanzada(Connection con, 
													            MedicosXPoolForm medicosPoolForm, 
													            ActionMapping mapping) 
    {
        MedicosXPool mundo = new MedicosXPool ();
        mundo.reset();
        medicosPoolForm.resetMaps(false);
        int medico = 0;
        double porcentaje = 0.0;
        
        if(!(medicosPoolForm.getMapFiltros("medicoBuscar")+"").equals(""))
        {
            medico = Integer.parseInt(medicosPoolForm.getMapFiltros("medicoBuscar")+""); 
        }
        if(!(medicosPoolForm.getMapFiltros("porcentajeBuscar")+"").equals(""))
        {
            porcentaje = Double.parseDouble(medicosPoolForm.getMapFiltros("porcentajeBuscar")+""); 
        }
        
        medicosPoolForm.setMapPool(mundo.consultarAvanzada(con,
											                medicosPoolForm.getMapFiltros("fechaIBuscar")+"",
											        		medicosPoolForm.getMapFiltros("FechaFBuscar")+"",
											        		medico,
											        		medicosPoolForm.getCodigoPool(),
											        		porcentaje));
        
        medicosPoolForm.setBusqueda(true);
        
        clonHashMap(medicosPoolForm);
        
        for(int k = 0; k < medicosPoolForm.getNumeroElementos(); k ++)
        {
            medicosPoolForm.setMapPool("fechaIngreso_"+k,UtilidadFecha.conversionFormatoFechaAAp(medicosPoolForm.getMapPool("fechaIngreso_"+k)+""));  
            medicosPoolForm.setMapPool("fechaRetiro_"+k,UtilidadFecha.conversionFormatoFechaAAp(medicosPoolForm.getMapPool("fechaRetiro_"+k)+""));
        }
        
        medicosPoolForm.setEstado("datosAvanzada");
	    this.cerrarConexion(con);
        return mapping.findForward("ingresarModificar");
        
    }

    /**
	 * Metodo implementado para cargar el HasMap con los tregistro que seran 
	 * listados en el formulario.
     * @param con, Connection con la fuente de datos
     * @param medicosPoolForm, MedicosXPoolForm
     * @param mapping
     * @return findForward, ingresarModificar
     */
    private ActionForward accionListar(Connection con, MedicosXPoolForm medicosPoolForm, ActionMapping mapping) 
    {       
        MedicosXPool mundo = new MedicosXPool ();
        mundo.reset();
        medicosPoolForm.resetMaps(true);
        medicosPoolForm.esPrimero = true;
        
        medicosPoolForm.setMapPool(mundo.consultarMedicosPool(con,medicosPoolForm.getCodigoPool(),true));
        clonHashMap(medicosPoolForm);
                
        for(int k = 0; k < medicosPoolForm.getNumeroElementos(); k ++)
        {
            medicosPoolForm.setMapPool("fechaIngreso_"+k,UtilidadFecha.conversionFormatoFechaAAp(medicosPoolForm.getMapPool("fechaIngreso_"+k)+""));  
            medicosPoolForm.setMapPool("fechaRetiro_"+k,UtilidadFecha.conversionFormatoFechaAAp(medicosPoolForm.getMapPool("fechaRetiro_"+k)+""));
        }
        
        if(medicosPoolForm.getEstado().equals("listarMedicos") && medicosPoolForm.getAccion().equals("consultar"))
        {
            this.cerrarConexion(con);
            return mapping.findForward("consultar");   
        }
                   
        this.cerrarConexion(con);
        return mapping.findForward("ingresarModificar");      
    }

    /**
     * Metodo para realizar una copia del hashMap original con
     * los datos de la BD y realizar la suma de porcentajes
     * @param medicosPoolForm
     */
    
    private void clonHashMap (MedicosXPoolForm medicosPoolForm)
    {
        double sumaPorcentaje = 0.0;
        if(!medicosPoolForm.getMapPool().isEmpty())
        {
	        medicosPoolForm.setNumeroElementos((medicosPoolForm.getMapPool().size() / 12));
	               
	        for(int k = 0; k < medicosPoolForm.getNumeroElementos(); k ++)
	        {
	            medicosPoolForm.setMapFiltros("tipoRegistro_"+k,"BD");
	            medicosPoolForm.setMapInfo("codigo_"+k,medicosPoolForm.getMapPool("codigo_"+k));
	            medicosPoolForm.setMapInfo("pool_"+k,medicosPoolForm.getMapPool("pool_"+k));
	            medicosPoolForm.setMapInfo("medico_"+k,medicosPoolForm.getMapPool("medico_"+k));
	            medicosPoolForm.setMapInfo("fechaIngreso_"+k,medicosPoolForm.getMapPool("fechaIngreso_"+k));
	            medicosPoolForm.setMapInfo("horaIngreso_"+k,medicosPoolForm.getMapPool("horaIngreso_"+k));
	            medicosPoolForm.setMapInfo("fechaRetiro_"+k,medicosPoolForm.getMapPool("fechaRetiro_"+k));
	            medicosPoolForm.setMapInfo("horaRetiro_"+k,medicosPoolForm.getMapPool("horaRetiro_"+k));
	            medicosPoolForm.setMapInfo("porcentaje_"+k,medicosPoolForm.getMapPool("porcentaje_"+k));
	            medicosPoolForm.setMapInfo("primerNombre_"+k,medicosPoolForm.getMapPool("primerNombre_"+k));
	            medicosPoolForm.setMapInfo("segundoNombre_"+k,medicosPoolForm.getMapPool("segundoNombre_"+k));
	            medicosPoolForm.setMapInfo("primerApellido_"+k,medicosPoolForm.getMapPool("primerApellido_"+k));
	            medicosPoolForm.setMapInfo("segundoApellido_"+k,medicosPoolForm.getMapPool("segundoApellido_"+k));  
	            
	            sumaPorcentaje=validarPorcentaje(	sumaPorcentaje,
	                    							Double.parseDouble(medicosPoolForm.getMapPool("porcentaje_"+k)+""),
	                    							String.valueOf(medicosPoolForm.getMapPool("fechaRetiro_"+k)) );
	        }
	        	        
	        medicosPoolForm.setMapFiltros("sumaPorcentaje",sumaPorcentaje+"");
	        medicosPoolForm.setMapFiltros("sumaPorcentajeTemp",sumaPorcentaje+"");
	        
	        medicosPoolForm.setMapInfo("numRegistros",medicosPoolForm.getMapPool("numRegistros"));	        
        }
        
        if(medicosPoolForm.getMapPool().isEmpty())
        {
            medicosPoolForm.setNumeroElementos(0);
            medicosPoolForm.setMapInfo("numRegistros", 0+"");
            medicosPoolForm.setMapFiltros("sumaPorcentaje",0.0+"");
            medicosPoolForm.setMapFiltros("sumaPorcentajeTemp",0.0+"");
        }
    }
    
    /**
	 * Metodo para realizar las validaciones sobre los datos que seran enviados
	 * a modificar o a insertar.
     * @param con, Connection con la fuente de datos
     * @param medicosPoolForm MedicosXPoolForm
     * @param mapping,
     * @return findForward,
     */
    private ActionForward accionSalirGuardar(Connection con, 
												            MedicosXPoolForm medicosPoolForm, 
												            ActionMapping mapping,
												            HttpServletRequest request,
												            HttpSession session) 
    {
        MedicosXPool mundo = new MedicosXPool ();
        mundo.reset();
        boolean siInsertoModifico = false;
        boolean esInsertar = false,esModificar = false;
        double sumaPorcentaje = 0.0;
                     
        for(int j = 0; j < medicosPoolForm.getNumeroElementos(); j ++)
        {
            if(String.valueOf(medicosPoolForm.getMapFiltros("tipoRegistro_"+j)).equals("nuevo"))
            {
                esInsertar = true;
            }
            
            verificarModificados(medicosPoolForm,Integer.parseInt(medicosPoolForm.getMapPool("medico_"+j)+""),medicosPoolForm.getCodigoPool());
            
            if( (medicosPoolForm.getMapFiltros("tipoRegistro_"+j)+"").equals("Mod") )
            {
                esModificar = true;
            }
                                 
            if( esInsertar || esModificar )
            {
               if(esModificar)
	            {
	                llenarloghistorial(medicosPoolForm,j);
	            }
	            try 
	            {
	                siInsertoModifico = mundo.InsertarModificar(con,
	                			                      String.valueOf(medicosPoolForm.getMapPool("fechaIngreso_"+j)),
	                			                      String.valueOf(medicosPoolForm.getMapPool("fechaRetiro_"+j)),
	                			                      String.valueOf(medicosPoolForm.getMapPool("horaIngreso_"+j)),
	                			                      String.valueOf(medicosPoolForm.getMapPool("horaRetiro_"+j)),
	                			                      Integer.parseInt(medicosPoolForm.getMapPool("medico_"+j)+""),
	                			                      medicosPoolForm.getCodigoPool(),
	                			                      Double.parseDouble(medicosPoolForm.getMapPool("porcentaje_"+j)+""),
	                			                      esInsertar);
	            } 
	            catch (NumberFormatException e) 
	            {
	                logger.warn("Error de formato de número (MedicosXPoolAction - accionSalirGuardar)");
	                e.printStackTrace();
	            } 
	            catch (SQLException e) 
	            {
	                logger.warn("Error SQL (MedicosXPoolAction - accionSalirGuardar)");
	                e.printStackTrace();
	                logger.warn("Error insertando ó modificando medicos por pool (MedicosXPoolAction)"); 
	                ActionErrors errores = new ActionErrors();
	                errores.add("salirGuardar MedicosXPool", new ActionMessage("prompt.noSeGraboInformacion"));
	                logger.warn("No se guardo información");
	                saveErrors(request, errores);   
	                this.cerrarConexion(con);                                                                       
	                return mapping.findForward("ingresarModificar");
	            }  
	            
	            if(siInsertoModifico)
	            {
	                if(esModificar)
	                {
	                    generarLog(medicosPoolForm,j,session);
	                }
	            }
            }
        }
        
        if(siInsertoModifico)
        {
            if(esModificar)
            {
                medicosPoolForm.setEstado("resumen");               
                medicosPoolForm.resetMaps(true);
                medicosPoolForm.setMapPool(mundo.consultarMedicosPool(con,medicosPoolForm.getCodigoPool(),true));
                medicosPoolForm.setNumeroElementos((medicosPoolForm.getMapPool().size() / 12));
                for(int k = 0; k < medicosPoolForm.getNumeroElementos(); k ++)
    	        {
    	           sumaPorcentaje=validarPorcentaje(	sumaPorcentaje,
    	                    							Double.parseDouble(medicosPoolForm.getMapPool("porcentaje_"+k)+""),
    	                    							String.valueOf(medicosPoolForm.getMapPool("fechaRetiro_"+k)) );
    	        }
    	        medicosPoolForm.setMapFiltros("sumaPorcentaje",sumaPorcentaje+"");
    	        medicosPoolForm.setMapFiltros("sumaPorcentajeTemp",sumaPorcentaje+"");
            }
            if(esInsertar)
            {
                medicosPoolForm.setEstado("resumen");               
                medicosPoolForm.resetMaps(true);
                sumaPorcentaje = 0.0; //para que no almacene lo anterior si hubo modificacion
                medicosPoolForm.setMapPool(mundo.consultarMedicosPool(con,medicosPoolForm.getCodigoPool(),true));
                medicosPoolForm.setNumeroElementos((medicosPoolForm.getMapPool().size() / 12));
                for(int k = 0; k < medicosPoolForm.getNumeroElementos(); k ++)
    	        {
    	           sumaPorcentaje=validarPorcentaje(	sumaPorcentaje,
    	                    							Double.parseDouble(medicosPoolForm.getMapPool("porcentaje_"+k)+""),
    	                    							String.valueOf(medicosPoolForm.getMapPool("fechaRetiro_"+k)) );
    	        }
    	        medicosPoolForm.setMapFiltros("sumaPorcentaje",sumaPorcentaje+"");                                
    	        medicosPoolForm.setMapFiltros("sumaPorcentajeTemp",sumaPorcentaje+"");
            }
        }
        
        medicosPoolForm.setAccion("salirGuardar");
        this.cerrarConexion(con);       
        return mapping.findForward("ingresarModificar");
    }
    
    
    /**
     * Metodo para verificar que registros fueron modificados (HashMap)
     * @param medicosPoolForm, MedicosXPoolForm
     * @param codigoMedico, código para comparar si fue modificado
     * @param codigoPool, código para comparar si fue modificado
     */
    public void verificarModificados (MedicosXPoolForm medicosPoolForm, int codigoMedico, int codigoPool)
    {
        boolean esModificado = false;
        
        for(int k = 0; k < Integer.parseInt(medicosPoolForm.getMapInfo("numRegistros")+""); k++)
        {
            if( codigoMedico == Integer.parseInt(medicosPoolForm.getMapInfo("medico_"+k)+"") && codigoPool == Integer.parseInt(medicosPoolForm.getMapInfo("pool_"+k)+""))
            {
               
                if( !(medicosPoolForm.getMapInfo("fechaIngreso_"+k)+"").equals(
                        UtilidadFecha.conversionFormatoFechaABD(medicosPoolForm.getMapPool("fechaIngreso_"+k)+"")) )
                {
                    esModificado = true;           
                }
               if( !(medicosPoolForm.getMapInfo("fechaRetiro_"+k)+"").equals(
                       UtilidadFecha.conversionFormatoFechaABD(medicosPoolForm.getMapPool("fechaRetiro_"+k)+"")) )
                {
                    esModificado = true;                  
                }
                if( !(medicosPoolForm.getMapInfo("porcentaje_"+k)+"").equals(medicosPoolForm.getMapPool("porcentaje_"+k)+"") )
                {
                    esModificado = true;   
                }
                if(esModificado)
                {
                    medicosPoolForm.setMapFiltros("tipoRegistro_"+k,"Mod");
                    esModificado = false;
                }
            }                
        }       
    }

    /**
	 * Metodo implementado para ingresar una linea de registro en blanco, con sus
	 * correspondientes campos perteneciente al HashMap, para insertar los
	 * datos en la forma, manteniendo la integridad del HashMap.
     * @param con, Connection con la fuente de datos
     * @param medicosPoolForm, MedicosXPoolForm 
     * @param mapping,
     * @return findForward, ingresarModificar página de ingresar y modificar.
     * @throws IOException
     */
    private ActionForward accionAdicionar(Connection con, 
            											MedicosXPoolForm medicosPoolForm,
            											HttpServletRequest request,
            											HttpServletResponse response) 
    { 
        MedicosXPool mundo = new MedicosXPool ();
        mundo.reset();
        
        registroFantasma(medicosPoolForm);
        
        int posicionNuevo = medicosPoolForm.getNumElementosBD();
        int posicionAnterior_2 = medicosPoolForm.getNumeroElementos()-1;
       
        double suma = 0.0;
        
        medicosPoolForm.setMapFiltros("tipoRegistro_"+posicionNuevo,"nuevo");
        
        medicosPoolForm.setNumeroElementos((medicosPoolForm.getMapPool().size() / 12));
        
        if(medicosPoolForm.getMapPool().size() == 1 )
        {
            if(! (medicosPoolForm.getMapPool("porcentaje_"+posicionAnterior_2)+"").equals("") && ! (medicosPoolForm.getMapPool("porcentaje_"+posicionAnterior_2)+"").equals("null"))
	        {
		        suma = 	Double.parseDouble(medicosPoolForm.getMapPool("porcentaje_"+posicionAnterior_2)+"") + 
		        				Double.parseDouble(medicosPoolForm.getMapFiltros("sumaPorcentaje")+"");
		        medicosPoolForm.setMapFiltros("sumaPorcentaje",suma+"");
	        }
        }
        
        if(medicosPoolForm.getMapPool().size() != 1 )
        {
            
          for(int k = 0; k < medicosPoolForm.getNumeroElementos(); k ++)
  	      {
            if(! (medicosPoolForm.getMapPool("porcentaje_"+k)+"").equals("") && 
               ! (medicosPoolForm.getMapPool("porcentaje_"+k)+"").equals("null"))
            {
                
    	      suma=validarPorcentaje(	suma,
    	              						Double.parseDouble(medicosPoolForm.getMapPool("porcentaje_"+k)+""),
    	                    				String.valueOf(medicosPoolForm.getMapPool("fechaRetiro_"+k)) );   	                        
         	}  
  	      }
          medicosPoolForm.setMapFiltros("sumaPorcentaje",suma+"");
          medicosPoolForm.setMapFiltros("sumaPorcentajeTemp",suma+"");
        }        
        
        medicosPoolForm.setAccion("ingresarNuevo");
        return this.redireccion(con,medicosPoolForm,response,request);
     }
    
    /**
     * Metodo implementado para posicionarse en la ultima
     * pagina del pager.
     * @param con
     * @param medicosPoolForm
     * @param response
     * @param request
     * @return null
     */
    public ActionForward redireccion (	Connection con,
            					MedicosXPoolForm medicosPoolForm,
            					HttpServletResponse response,
            					HttpServletRequest request)
    {
        
        if(request.getParameter("ultimaPage")==null)
        {
           if(medicosPoolForm.getNumeroElementos()>(medicosPoolForm.getOffset()+5))
                medicosPoolForm.setOffset(medicosPoolForm.getOffset()+5);
           
            try 
            {    
                this.cerrarConexion(con);
                response.sendRedirect("ingresarModificarMedicosXPool.jsp?pager.offset="+medicosPoolForm.getOffset());
            } 
            catch (IOException e) 
            {                
                e.printStackTrace();
            }
        }
        else
        {    
            String ultimaPagina=request.getParameter("ultimaPage");
                        
            int posOffSet=ultimaPagina.indexOf("offset=")+7;
            medicosPoolForm.setOffset((Integer.parseInt(ultimaPagina.substring(posOffSet,ultimaPagina.length() ))));
            
            if(medicosPoolForm.getNumeroElementos()>(medicosPoolForm.getOffset()+5))
                medicosPoolForm.setOffset(medicosPoolForm.getOffset()+5);
             
            try 
            {   
                this.cerrarConexion(con);
                response.sendRedirect(ultimaPagina.substring(0,posOffSet)+medicosPoolForm.getOffset());
            } 
            catch (IOException e) 
            {
              e.printStackTrace();
            }
     }       
       return null;
    }

   /**
	 * Metodo para adicionar un nuevo registro en blanco en 
	 * el HashMap (mapPool) 
	 * @param medicosPoolForm
	 */
	public void registroFantasma (MedicosXPoolForm medicosPoolForm)
	{
	    
	    int posicion = medicosPoolForm.getNumeroElementos();
	   	   
	    medicosPoolForm.setMapPool("codigo_"+posicion,"");//para mantener el número de objetos cte en el HashMap
        medicosPoolForm.setMapPool("pool_"+posicion,"-1");//para mantener el número de objetos cte en el HashMap
        medicosPoolForm.setMapPool("medico_"+posicion,"");//reservado para ingresar en la forma
        medicosPoolForm.setMapPool("fechaIngreso_"+posicion,UtilidadFecha.getFechaActual()+"");//reservado para ingresar en la forma
        medicosPoolForm.setMapPool("horaIngreso_"+posicion,"");//para mantener el número de objetos cte en el HashMap
        medicosPoolForm.setMapPool("fechaRetiro_"+posicion,"");//reservado para ingresar en la forma
        medicosPoolForm.setMapPool("horaRetiro_"+posicion,"");//para mantener el número de objetos cte en el HashMap
        medicosPoolForm.setMapPool("porcentaje_"+posicion,"");//reservado para ingresar en la forma
        medicosPoolForm.setMapPool("primerNombre_"+posicion,"Seleccione");//para mantener el número de objetos cte en el HashMap
        medicosPoolForm.setMapPool("segundoNombre_"+posicion,"");//para mantener el número de objetos cte en el HashMap
        medicosPoolForm.setMapPool("primerApellido_"+posicion,"");//para mantener el número de objetos cte en el HashMap
        medicosPoolForm.setMapPool("segundoApellido_"+posicion,"");//para mantener el número de objetos cte en el HashMap
                
        medicosPoolForm.setNumElementosBD(medicosPoolForm.getNumeroElementos());
	}
	
	/**
	 * Metodo para realizar la suma de los porcentajes de los
	 * médicos pertenecientes a un pool, sin fecha de retiro vencida
	 * @param porcentaje
	 * @param fechaRetiro
	 * @param medicosPoolForm
	 */
	public double validarPorcentaje (double sumaPorcentaje,double porcentaje,String fechaRetiro)
	{
	    boolean fechaMenor = false;
	    
	    if(!fechaRetiro.equals(""))
	    {
	        fechaMenor = UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(fechaRetiro),UtilidadFecha.getFechaActual());
	    
	        if(!fechaMenor)
	        {
	            sumaPorcentaje = sumaPorcentaje + porcentaje;	            
	        }
	    }
	    
	    else if(fechaRetiro.equals(""))
	    {
	        sumaPorcentaje = sumaPorcentaje + porcentaje;	        
	    }
	    	    	    
	    return sumaPorcentaje;
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
	  * @param medicosPoolForm, MedicosXPoolForm Instancia del Form ParamInstitucionForm.
	  * @param posicion, Posicion en el HashMap del elemento modificado
	  */
	 private void llenarloghistorial(MedicosXPoolForm medicosPoolForm,int posicion)
	    {
	     String nombre ="";
	     
	     nombre+=  	String.valueOf(medicosPoolForm.getMapInfo("primerApellido_"+posicion))+" "+
			String.valueOf(medicosPoolForm.getMapInfo("segundoApellido_"+posicion))+" "+
			String.valueOf(medicosPoolForm.getMapInfo("primerNombre_"+posicion))+" "+
			String.valueOf(medicosPoolForm.getMapInfo("segundoNombre_"+posicion));
	     
	     medicosPoolForm.setLog("\n            ====INFORMACION ORIGINAL===== " +
	            "\n*  Código Registro [" +medicosPoolForm.getMapInfo("codigo_"+posicion)+""+"] "+
	 			"\n*  Código Pool ["+medicosPoolForm.getMapInfo("pool_"+posicion)+""+"] " +
	 			"\n*  Código del Médico  ["+medicosPoolForm.getMapInfo("medico_"+posicion)+""+"] "+
	 			"\n*  Nombre del Médico ["+nombre+"] "+
	 			"\n*  Fecha Ingreso ["+UtilidadFecha.conversionFormatoFechaAAp(medicosPoolForm.getMapInfo("fechaIngreso_"+posicion)+"")+"] "+
	 			"\n*  Fecha Retiro ["+UtilidadFecha.conversionFormatoFechaAAp(medicosPoolForm.getMapInfo("fechaRetiro_"+posicion)+"")+"] "+
	 			"\n*  Porcentaje ["+medicosPoolForm.getMapInfo("porcentaje_"+posicion)+""+"] "+
	 			"\n\n");
	        
	    }
	 
	 /**
	  * Metodo para generar el Log, y añadir los cambios realizados.
	  * @param medicosPoolForm, Instancia del Form MedicosXPoolForm.
	  * @param session, Session para obtener el usuario.
	  */
	 private void generarLog( MedicosXPoolForm medicosPoolForm,int posicion, HttpSession session)
	    {
	        UsuarioBasico usuario;
	        
	        String nombre ="";
		     
		     nombre+=  	String.valueOf(medicosPoolForm.getMapPool("primerApellido_"+posicion))+" "+
				String.valueOf(medicosPoolForm.getMapPool("segundoApellido_"+posicion))+" "+
				String.valueOf(medicosPoolForm.getMapPool("primerNombre_"+posicion))+" "+
				String.valueOf(medicosPoolForm.getMapPool("segundoNombre_"+posicion));
	        
	        String log=medicosPoolForm.getLog() +
			"\n            ====INFORMACION DESPUES DE LA MODIFICACION===== " +
			"\n*  Código Registro [" +medicosPoolForm.getMapPool("codigo_"+posicion)+""+"] "+
 			"\n*  Código Pool ["+medicosPoolForm.getMapPool("pool_"+posicion)+""+"] " +
 			"\n*  Código del Médico  ["+medicosPoolForm.getMapPool("medico_"+posicion)+""+"] "+
 			"\n*  Nombre del Médico ["+nombre+"] "+
 			"\n*  Fecha Ingreso ["+medicosPoolForm.getMapPool("fechaIngreso_"+posicion)+""+"] "+
 			"\n*  Fecha Retiro ["+medicosPoolForm.getMapPool("fechaRetiro_"+posicion)+""+"] "+
 			"\n*  Porcentaje ["+medicosPoolForm.getMapPool("porcentaje_"+posicion)+""+"] "+
			"\n========================================================\n\n\n " ;
	        usuario=(UsuarioBasico)session.getAttribute("usuarioBasico");
	        LogsAxioma.enviarLog(ConstantesBD.logMedicosXPoolCodigo,log,ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
	        
	    }
}
