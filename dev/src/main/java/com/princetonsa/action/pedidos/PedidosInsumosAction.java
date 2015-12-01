/*
 * Creado  27/09/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.action.pedidos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

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
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.actionform.pedidos.PedidosInsumosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pedidos.PedidosInsumos;
import com.princetonsa.pdf.PedidosInsumosPdf;



/**
 * Action, controla todas las opciones dentro de Pedidos Insumos
 * incluyendo los posibles casos de error y los flujos.
 *
 * @version 1.0, 27/09/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Arlen L&oacute;pez Correa.</a>
 */
public class PedidosInsumosAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(PedidosInsumosAction.class);
	
	public ActionForward execute(	ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response ) throws Exception
									{

		Connection con=null;
		try{

			if (response==null); //Para evitar que salga el warning

			if(form instanceof PedidosInsumosForm)
			{


				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				PedidosInsumosForm pedidosForm=(PedidosInsumosForm)form;
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				String estado=pedidosForm.getEstado();
				logger.warn("estado ="+estado);

				if(estado == null)
				{
					pedidosForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Pedidos Insumos (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				else if (estado.equals("empezar"))
				{
					return this.accionEmpezar(con,mapping,pedidosForm,usuario,request);			    
				}

				//estado usado al seleccionar otro centro de costo
				else if (estado.equals("nuevoCentroCosto"))
				{
					return this.accionNuevoCentroCosto(con,mapping,pedidosForm,usuario,request);
				}

				else if (estado.equals("iniciarPedido"))
				{
					return this.accionIniciarPedido(con,mapping,pedidosForm);
				}

				else if (estado.equals("agregar"))
				{
					pedidosForm.setDbAction("add");
					accionAgregarOtro(pedidosForm);
					this.cerrarConexion(con);
					return this.redireccion(con,pedidosForm,response,request);
					//return mapping.findForward("pedidosInsumos");
				}
				else if (estado.equals("eliminarInsumo"))
				{
					accionEliminarInsumo(pedidosForm);
					this.cerrarConexion(con);
					return mapping.findForward("pedidosInsumos");
				}
				else if (estado.equals("salirGuardar"))
				{
					return this.accionSalirGuardar(pedidosForm,mapping,con,usuario,request);
				}
				else if (estado.equals("Imprimir"))
				{
					return accionImprimir(con,mapping,pedidosForm,request,usuario);
				}
				else if (estado.equals("redireccion"))// estado para mantener los datos del pager
				{
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect(pedidosForm.getLinkSiguiente());
					return null;
				}
				else
				{
					pedidosForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Pedidos Insumos (null) ");
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
   * Método que imprime el resumen de la solicitud de un pedido
	 * @param con
	 * @param mapping
	 * @param pedidosForm
 * @param request
 * @param usuario
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, ActionMapping mapping, PedidosInsumosForm pedidosForm, HttpServletRequest request, UsuarioBasico usuario) 
	{
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/Pedido de Insumos" + r.nextInt()  +".pdf";
    	
    	PedidosInsumosPdf.pdfPedidosInsumos(ValoresPorDefecto.getFilePath() + nombreArchivo, pedidosForm, usuario,null, request);
    	this.cerrarConexion(con);
    	request.setAttribute("nombreArchivo", nombreArchivo);
    	request.setAttribute("nombreVentana", "Pedidos Insumos");
    	return mapping.findForward("abrirPdf");
	}


/**
   * Método que realiza las validaciones pertinentes cuando se cambia
   * de centro de costo
	 * @param con
	 * @param mapping
	 * @param pedidosForm
	 * @param usuario
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionNuevoCentroCosto(Connection con, ActionMapping mapping, PedidosInsumosForm pedidosForm, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//se reinician datos del listado de subalmacenes
		pedidosForm.setNumAlmacenes(0);
		pedidosForm.setListaAlmacenes(new HashMap());
		
		//se verifica que sea centro costo válido
		if(pedidosForm.getCentroCosto()>0)
		{
			//se carga la nueva lista de subalmacenes
			//if(UtilidadValidacion.esCentroCostoSubalmacen(pedidosForm.getCentroCosto()))
			{
				this.cargarListaAlmacenes(pedidosForm,usuario,pedidosForm.getCentroCosto(),request);
			}
			/*
			else
			{
				ActionErrors errores = new ActionErrors();
				errores.add("no es almacen",new ActionMessage("error.inventarios.centroCostoUsuarioNoDefinidoAlmacen"));
				saveErrors(request, errores);
			}
			*/
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("listadoAlmacenes");
	}


/**
   * Método implementado para iniciar el pedido
	 * @param con
	 * @param mapping
	 * @param pedidosForm
	 * @return
	 */
	private ActionForward accionIniciarPedido(Connection con, ActionMapping mapping, PedidosInsumosForm pedidosForm) 
	{
		try
		{
			//se cargan las parejas Clase-Grupo
			pedidosForm.setParejasClaseGrupo(
				UtilidadInventarios.obtenerInterseccionClaseGrupo(
					pedidosForm.getCentroCosto(),
					pedidosForm.getFarmacia()));
			
			
			//capturar nombre del centro de costo
			pedidosForm.setNombreCentroCosto(UtilidadValidacion.getNombreCentroCosto(con,pedidosForm.getCentroCosto()));
			//capturar nombre del almacen
			pedidosForm.setNombreFarmacia(UtilidadValidacion.getNombreCentroCosto(con,pedidosForm.getFarmacia()));
		}
		catch(SQLException e)
		{
			logger.warn("Error al cargar el nombre del centro de costo en Accion Iniciar Pedido: "+e);
		}
		
		
		pedidosForm.setFechaPedido(UtilidadFecha.getFechaActual());
        pedidosForm.setHoraPedido(UtilidadFecha.getHoraActual());
        pedidosForm.setFechaHoraPedido(UtilidadFecha.getFechaActual()+"-"+UtilidadFecha.getHoraActual());
        this.cerrarConexion(con);		
        return mapping.findForward("pedidosInsumos");
	}


/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer
	 * un forward
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
				logger.error("Error al tratar de cerrar la conexion con la fuente de datos en PedidosInsumosAction. \n Excepcion: " +e);
			}
	}
	
	

    /**
     * Metodo para ingresar insumos dinamicamente al HashMap 
     * que luego son mostrados en la vista.
     * @param pedidosForm
     */
    private void accionAgregarOtro(PedidosInsumosForm pedidosForm)
    {
        int k=pedidosForm.getNumeroIngresos();
        String codigosInsertados = pedidosForm.getCodigosArticulosInsertados(); 
        
        
        codigosInsertados+=pedidosForm.getPedidosMap("codigoArticulo_"+k) + ",";
        pedidosForm.setCodigosArticulosInsertados(codigosInsertados);
        
        
        
        //se acomoda el campo artículo
        pedidosForm.setPedidosMap("articulo_"+k,
        	pedidosForm.getPedidosMap("codigoArticulo_"+k)+"-"+pedidosForm.getPedidosMap("descripcionArticulo_"+k));
        
       	pedidosForm.setPedidosMap("existe_"+k, new Boolean(pedidosForm.getDbExist()));
       	pedidosForm.setPedidosMap("accion_"+k, pedidosForm.getDbAction());
       	
        pedidosForm.setNumeroIngresos(k+1);
        
        
        pedidosForm.setDbExist(false);
        pedidosForm.setDbAction("add");
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
						            					PedidosInsumosForm pedidosInsumosForm,
						            					HttpServletResponse response,
						            					HttpServletRequest request)
    {
        if(request.getParameter("ultimaPage")==null)
        {
           if(pedidosInsumosForm.getNumeroIngresos() > (pedidosInsumosForm.getOffset()+pedidosInsumosForm.getMaxPageItems()))
               pedidosInsumosForm.setOffset(pedidosInsumosForm.getOffset()+pedidosInsumosForm.getMaxPageItems());
            
            try 
            {
                this.cerrarConexion(con);
                response.sendRedirect("pedidosInsumos.jsp?pager.offset="+pedidosInsumosForm.getOffset());
            } catch (IOException e) 
            {
                
                e.printStackTrace();
            }
        }
        else
        {    
            String ultimaPagina=request.getParameter("ultimaPage");
            
            int posOffSet=ultimaPagina.indexOf("offset=")+7;
            pedidosInsumosForm.setOffset((Integer.parseInt(ultimaPagina.substring(posOffSet,ultimaPagina.length() ))));
                
            if(pedidosInsumosForm.getNumeroIngresos()>(pedidosInsumosForm.getOffset()+pedidosInsumosForm.getMaxPageItems()))
                pedidosInsumosForm.setOffset(pedidosInsumosForm.getOffset()+pedidosInsumosForm.getMaxPageItems());
             
            try 
            {
                this.cerrarConexion(con);
                response.sendRedirect(ultimaPagina.substring(0,posOffSet)+pedidosInsumosForm.getOffset());
            } 
            catch (IOException e) 
            {
                
                e.printStackTrace();
            }
     }
       //this.cerrarConexion(con);
       return null;
    }
    
    /**
     * @param pedidosForm
     */
    private void accionEliminarInsumo(PedidosInsumosForm pedidosForm)
    {
        eliminarMemoria(pedidosForm);        
    }

     
    /**
     * Metodo para eliminar el registro del 
     * HashMap.
     * @param pedidosForm
     */
    private void eliminarMemoria(PedidosInsumosForm pedidosForm)
    {
       
        int k=0;
        int esUltimo=pedidosForm.getNumeroIngresos()-1;
        String codigoInsertados = "";
        
        //logger.info("Action eliminar memoria(**********getNumeroIngresos******)->"+pedidosForm.getNumeroIngresos());
        
        while(k < pedidosForm.getNumeroIngresos())
        {
           
            String [] dato= String.valueOf(pedidosForm.getPedidosMap("articulo_"+k)).split("-");
                                         
            if(pedidosForm.getCodigoArticulo().equals(dato[0]))
           {              
              if(k == esUltimo)
              {
              	pedidosForm.getPedidosMap().remove("articulo_"+k);
            	pedidosForm.getPedidosMap().remove("codigoArticulo_"+k);
            	pedidosForm.getPedidosMap().remove("descripcionArticulo_"+k);  
            	pedidosForm.getPedidosMap().remove("unidadMedidaArticulo_"+k);
            	pedidosForm.getPedidosMap().remove("cantidadDespachadaArticulo_"+k);
            	pedidosForm.getPedidosMap().remove("autorizacionArticulo_"+k);
            	pedidosForm.getPedidosMap().remove("fueEliminadoArticulo_"+k);
            	pedidosForm.getPedidosMap().remove("tipoPosArticulo_"+k);
            	pedidosForm.getPedidosMap().remove("existenciaXAlmacen_"+k);
            	pedidosForm.getPedidosMap().remove("cantidadDespacho_"+k);
            	pedidosForm.getPedidosMap().remove("existe_"+k);
            	pedidosForm.getPedidosMap().remove("accion_"+k);
              }
              else
              {
              	for(int j=k;j<pedidosForm.getNumeroIngresos()-1;j++)
              	{
                  pedidosForm.setPedidosMap("articulo_"+j,pedidosForm.getPedidosMap("articulo_"+(j+1)));
                  pedidosForm.setPedidosMap("codigoArticulo_"+j,pedidosForm.getPedidosMap("codigoArticulo_"+(j+1)));
                  pedidosForm.setPedidosMap("descripcionArticulo_"+j,pedidosForm.getPedidosMap("descripcionArticulo_"+(j+1)));
                  pedidosForm.setPedidosMap("unidadMedidaArticulo_"+j,pedidosForm.getPedidosMap("unidadMedidaArticulo_"+(j+1)));
                  pedidosForm.setPedidosMap("cantidadDespachadaArticulo_"+j,pedidosForm.getPedidosMap("cantidadDespachadaArticulo_"+(j+1)));
                  pedidosForm.setPedidosMap("autorizacionArticulo_"+j,pedidosForm.getPedidosMap("autorizacionArticulo_"+(j+1)));
                  pedidosForm.setPedidosMap("fueEliminadoArticulo_"+j,pedidosForm.getPedidosMap("fueEliminadoArticulo_"+(j+1)));
                  pedidosForm.setPedidosMap("tipoPosArticulo_"+j,pedidosForm.getPedidosMap("tipoPosArticulo_"+(j+1)));
                  pedidosForm.setPedidosMap("existenciaXAlmacen_"+j,pedidosForm.getPedidosMap("existenciaXAlmacen_"+(j+1)));
                  pedidosForm.setPedidosMap("cantidadDespacho_"+j,pedidosForm.getPedidosMap("cantidadDespacho_"+(j+1)));
                  pedidosForm.setPedidosMap("existe_"+j,pedidosForm.getPedidosMap("existe_"+(j+1)));
                  pedidosForm.setPedidosMap("accion_"+j,pedidosForm.getPedidosMap("accion_"+(j+1)));
              	}
              	
              	pedidosForm.getPedidosMap().remove("articulo_"+esUltimo);
            	pedidosForm.getPedidosMap().remove("codigoArticulo_"+esUltimo);
            	pedidosForm.getPedidosMap().remove("descripcionArticulo_"+esUltimo);  
            	pedidosForm.getPedidosMap().remove("unidadMedidaArticulo_"+esUltimo);
            	pedidosForm.getPedidosMap().remove("cantidadDespachadaArticulo_"+esUltimo);
            	pedidosForm.getPedidosMap().remove("autorizacionArticulo_"+esUltimo);
            	pedidosForm.getPedidosMap().remove("fueEliminadoArticulo_"+esUltimo);
            	pedidosForm.getPedidosMap().remove("tipoPosArticulo_"+esUltimo);
            	pedidosForm.getPedidosMap().remove("existenciaXAlmacen_"+esUltimo);
            	pedidosForm.getPedidosMap().remove("cantidadDespacho_"+esUltimo);
            	pedidosForm.getPedidosMap().remove("existe_"+esUltimo);
            	pedidosForm.getPedidosMap().remove("accion_"+esUltimo);
              	
              }   
              pedidosForm.setNumeroIngresos(pedidosForm.getNumeroIngresos()-1);
            }
            //si no es el articulo se añade a la lista de insertados
            
            k ++;
        }
        
        for(int i = 0 ; i < pedidosForm.getNumeroIngresos(); i++)
        {       	
        	codigoInsertados +=pedidosForm.getPedidosMap("codigoArticulo_"+i);
        	
        	if((i+1) != pedidosForm.getNumeroIngresos())
        		codigoInsertados += ",";
        }
        
        //se asignan los nuevos articulos ya insertados
        pedidosForm.setCodigosArticulosInsertados(codigoInsertados);
        
        
    }


    /**
     * Metodo para inicializar el formulario.
     * @param con Connection con la fuente de datos
     * @param usuario
     * @param request
     * @return findForward <code>(pedidosInsumos)</code>
     * @see struts-config
     */
    @SuppressWarnings("unchecked")
	private ActionForward accionEmpezar(Connection con,ActionMapping mapping,PedidosInsumosForm pedidosForm, UsuarioBasico usuario, HttpServletRequest request)
    {
    	//variables auxiliares
    	int auxI0 = 0;
    	
    	pedidosForm.setModificarFecha(ConstantesBD.acronimoNo);
    	logger.info("XXXXXXXXXXXXXXXXXXXXXXXXXXX "+ValoresPorDefecto.getPermitirModificarFechaSolicitudPedidos(usuario.getCodigoInstitucionInt()));
    	
    	if(ValoresPorDefecto.getPermitirModificarFechaSolicitudPedidos(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
    		pedidosForm.setModificarFecha(ConstantesBD.acronimoSi);
    	
    	logger.info("XXXXXXXXXXXXXXXXXXXXXXXXXXX "+pedidosForm.getModificarFecha());
    	
    	//se limpia formulario
    	pedidosForm.reset();
    	pedidosForm.setInstitucion(usuario.getCodigoInstitucion());
    	pedidosForm.setCentroAtencion(usuario.getCodigoCentroAtencion()+"");
    	pedidosForm.setCentroCosto(usuario.getCodigoCentroCosto());
    	pedidosForm.setTipoTransaccionPedido(ValoresPorDefecto.getCodigoTransaccionPedidos(usuario.getCodigoInstitucionInt(), true));
    	
    	//Se verifica que el tipo de transaccion pedido se encuentre
    	//definida en parametros generales
    	if(!pedidosForm.getTipoTransaccionPedido().equals(""))
    	{
	    	//***SE VERIFICA SI HAY CENTROS DE COSTO VALIDOS PARA PEDIDOS**********
	    	Vector restricciones= new Vector();
	        restricciones.add(0, ValoresPorDefecto.getCodigoTransaccionPedidos(usuario.getCodigoInstitucionInt(), true));
	        
	        //se carga el registro de la transaccion válidad para todos los centros costo
	        //y con el tipo transaccion PEDIDOS
	        HashMap mapaTransValidas=UtilidadInventarios.transaccionesValidasCentroCosto( 
	        		usuario.getCodigoInstitucionInt(), 
					ConstantesBD.codigoCentroCostoTodos, 
					restricciones, true);
	        //se asigna tamaño del mapa
	        auxI0 = Integer.parseInt(mapaTransValidas.get("numRegistros").toString());
	        pedidosForm.setNumCentrosCosto(auxI0);
	        
	        if(auxI0<=0)
	        {
	        	ActionErrors errores = new ActionErrors(); 
	            errores.add("No existen centros costo permitidos", new ActionMessage("error.inventarios.noExistenCentrosCostoPermitidos","PEDIDO DE INSUMOS"));
	            saveErrors(request, errores);
	        }
	        else if(auxI0==1)
	        {
	        	//**SE VERIFICA SI EL CENTRO DE COSTO ENCONTRADO TIENE TRANSACCION VÁLIDA*******
	        	this.cargarListaAlmacenes(pedidosForm,usuario,Integer.parseInt(mapaTransValidas.get("centro_costo_0")+""),request);
	        	//*******************************************************************************
	        }
			else
	        {
	        	///***SE VERIFICA SI EL CENTRO DE COSTO DEL USUARIO TIENE TRANSACCION VALIDA****
	        	this.cargarListaAlmacenes(pedidosForm,usuario,usuario.getCodigoCentroCosto(),request);
	            //*************************************************************************************************	
	            
	        }
    	}
    	else
    	{
    		ActionErrors errores = new ActionErrors(); 
            errores.add("sin definir tipo transaccion", new ActionMessage("error.inventarios.sinDefinirTipoTransaccion","DE PEDIDOS"));
            saveErrors(request, errores);
    		
    	}
        
    	this.cerrarConexion(con);
    	return mapping.findForward("listadoAlmacenes");
    }



    
    /**
     * Método implementado para cargar la lista de almacenes
	 * @param pedidosForm
	 * @param usuario
     * @param centroCosto
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	private void cargarListaAlmacenes(PedidosInsumosForm pedidosForm, UsuarioBasico usuario, int centroCosto, HttpServletRequest request) 
	{
		//variables auxiliares
		int auxI0 = 0;
		int auxI1 = 0;
		int auxI2 = 0;
		int auxI3 = 0;
		String auxS0 = "";
		
		Vector restricciones= new Vector();
        restricciones.add(0, ValoresPorDefecto.getCodigoTransaccionPedidos(usuario.getCodigoInstitucionInt(), true));
		
		
		//se carga el registro de la transaccion válidad para el centro costo
        //del usuario y con el tipo transaccion PEDIDOS
        HashMap mapaTransValidas=UtilidadInventarios.transaccionesValidasCentroCosto( 
        		usuario.getCodigoInstitucionInt(), 
				centroCosto, 
				restricciones, true);
        //se asigna tamaño del mapa
        auxI0 = Integer.parseInt(mapaTransValidas.get("numRegistros").toString());
        
        //si se encontraron registros se inicia verificacion
        if(auxI0>0)
        {
            //se iteran los registros
            for(int i=0;i<auxI0;i++)
            {
            	if(i>0)
            		auxS0+= ",";
            	
            	//se toma el grupo
            	auxI1 = Integer.parseInt(mapaTransValidas.get("grupo_"+i)+"");
            	//se toma la clase
            	auxI2 = Integer.parseInt(mapaTransValidas.get("clase_"+i)+"");
            	
            	auxS0 += "'"+auxI2+"-"+auxI1+"'";
            }
            	
        	//se carga el listado de almacenes
        	pedidosForm.setListaAlmacenes(
        		UtilidadInventarios.listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(
        			usuario.getCodigoInstitucionInt(),
					usuario.getLoginUsuario(),
					ValoresPorDefecto.getCodigoTransaccionPedidos(usuario.getCodigoInstitucionInt(),true),
					auxS0,ConstantesBD.codigoNuncaValido, false));
        	
        	//se asigna tamaño del listado
        	pedidosForm.setNumAlmacenes(Integer.parseInt(pedidosForm.getListaAlmacenes("numRegistros")+""));
        	
        	//se verifica si hay almacenes
        	if(pedidosForm.getNumAlmacenes()<=0)
        	{
        		ActionErrors errores = new ActionErrors(); 
        		
        		//se revisa si el usuario tenía almacenes validos
        		HashMap mapaAux = UtilidadInventarios.listadoAlmacenesUsuarios(usuario.getCodigoInstitucionInt(),usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido);
        		logger.info("mapaAux->"+mapaAux);
        		auxI3 = Integer.parseInt(mapaAux.get("numRegistros")+"");
        		
        		if(auxI3<=0)
        		{
        			//errores.add("No existen almacenes permitidos", new ActionMessage("error.inventarios.noExistenAlmacenesPermitidos","PEDIDO DE INSUMOS"));
        			errores.add("Usuario no esta definido en ningun almacen",new ActionMessage("error.inventarios.UsuarioNoDefinidoEnAlmacen"));
        		}
				else
				{
					//el mensaje no aplica, debería decir que el centro de costo no esta parametrizado como almacen.
					//errores.add("Usuario no esta definido en ningun almacen",new ActionMessage("error.inventarios.UsuarioNoDefinidoEnAlmacen"));
					//errores.add("Usuario no esta definido en ningun almacen",new ActionMessage("error.inventarios.AlmacenSinTransaccionesValidasPedidos"));
					errores.add("No existen almacenes permitidos", new ActionMessage("error.inventarios.noExistenAlmacenesPermitidos","PEDIDO DE INSUMOS"));
				}
			    saveErrors(request, errores);
        	}
        	
        	
        }
		
	}


  	
  	/**
  	 * Metodo para controlar el estado de guardar insumos. 
       * @param pedidosForm
       * @param mapping
       * @param con Connection  con la fuente de datos.
       * @param usuario UsuarioBasico
       * @param request HttpServletRequest 
       * @return findForward
  	 * @throws SQLException, para manejar la Connection
       */
      @SuppressWarnings("unchecked")
	private ActionForward accionSalirGuardar(PedidosInsumosForm pedidosForm, 
  																            	ActionMapping mapping, 
  																            	Connection con, 
  																            	UsuarioBasico usuario,
  																            	HttpServletRequest request) throws SQLException
      {
         PedidosInsumos mundo= new PedidosInsumos();
         
         ArrayList arrayListCodigos = new ArrayList (); 
         ArrayList arrayListCantidad = new ArrayList ();
         boolean esUrgente=false;
         boolean esTerminado=false;

         
         //*****VALIDACION DE CIERRE DE INVENTARIOS*****************
         if(UtilidadInventarios.existeCierreInventarioParaFecha(pedidosForm.getFechaPedido(),usuario.getCodigoInstitucionInt()))
         {
         	//si existe cierre hay error!!!
         	ActionErrors errores = new ActionErrors(); 
            errores.add("Existe cierre de inventarios para la fecha", new ActionMessage("error.inventarios.existeCierreInventarios",pedidosForm.getFechaPedido()));
            saveErrors(request, errores);
            
            this.cerrarConexion(con);
            return mapping.findForward("pedidosInsumos");
         }
         //*********************************************************+
         
         
         for (int k = 0 ;k < pedidosForm.getNumeroIngresos(); k++)
         {
         		String [] soloCodigoArticulo= String.valueOf(pedidosForm.getPedidosMap("articulo_"+k)).split("-");
              arrayListCodigos.add(k,soloCodigoArticulo[0]);
              arrayListCantidad.add(k,pedidosForm.getPedidosMap("cantidadDespacho_"+k));
              //  **********LLenar HashMap con los datos para mostrar en el resumen. 
              pedidosForm.setPedidosMap("fechaHoraGrabacion_"+0,UtilidadFecha.getFechaActual()+"-"+UtilidadFecha.getHoraActual());
              if(pedidosForm.getCheckTerminarPedido().equals("on"))
              	pedidosForm.setPedidosMap("esTerminado_"+0,"Terminado");
              else
                 	pedidosForm.setPedidosMap("esTerminado_"+0,"Solicitado");
              if(pedidosForm.getCheckPrioridadPedido().equals("on"))
                 	pedidosForm.setPedidosMap("prioridad_"+0,"Si");
              else
                 	pedidosForm.setPedidosMap("prioridad_"+0,"No");
         }
         
         if(pedidosForm.getCheckPrioridadPedido().equals("on"))
             esUrgente=true;
         else
             esUrgente=false;
         
         if(pedidosForm.getCheckTerminarPedido().equals("on"))
             esTerminado=true;
         else
             esTerminado=false;
         
         //logger.info("Centro de costo->"+pedidosForm.getNombreCentroCosto());
        //logger.info("Centro de costo->"+pedidosForm.getNombreFarmacia());
         
         int validar=mundo.insertarPedidoYDetalle(con,
  									               pedidosForm.getCentroCosto(),
  									               pedidosForm.getFarmacia(),
  									               esUrgente,
  									               //UtilidadTexto.agregarTextoAObservacion("",pedidosForm.getObservacionesGenerales(),usuario, true), 
  									               pedidosForm.getObservacionesGenerales(),
  									               usuario,
  									               esTerminado,
  									               arrayListCodigos,
  									               arrayListCantidad,
  									               ConstantesBD.inicioTransaccion,
  									               pedidosForm.getFechaPedido(),
  									               pedidosForm.getHoraPedido(),
  									               ConstantesBD.acronimoNo,
									               ConstantesBD.acronimoNo); //auto_por_subcontratacion - Anexo 71 cambio 1.5
         
         if (validar <1)
  	     {
  				logger.warn("Error en la transacción de Inserción en BD");
  				this.cerrarConexion(con);
  				pedidosForm.reset();
  				ArrayList atributosError = new ArrayList();
  				atributosError.add("No se pudo realizar la inserción, uno de los datos ");
  				request.setAttribute("codigoDescripcionError", "errors.invalid");				
  				request.setAttribute("atributosError", atributosError);
  				return mapping.findForward("paginaError");
  		}
         
         if (validar > 0)
         {
             pedidosForm.setNumeroPedido(mundo.siguientePedido(con));
             pedidosForm.setColeccionPedidos(mundo.listarPedidos(con, pedidosForm.getNumeroPedido()));
             //logger.info("*****la coleccion de pedidos->"+pedidosForm.getColeccionPedidos().size());
         }
         else
             logger.info("No se pudo generar la consulta para el resumen");
        
         
         this.cerrarConexion(con);
         return mapping.findForward("resumenInsumos");       
      }
    
}
