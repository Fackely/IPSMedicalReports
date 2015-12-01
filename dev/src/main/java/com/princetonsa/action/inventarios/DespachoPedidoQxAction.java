/*
 * Dic 05, 2007
 */
package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

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

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.IdentificadoresExcepcionesSql;
import util.Listado;
import util.ResultadoInteger;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.UtilidadBDInventarios;
import util.inventarios.ConstantesBDInventarios;
import util.inventarios.UtilidadInventarios;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.inventarios.DespachoPedidoQxForm;
import com.princetonsa.dto.interfaz.DtoInterfazTransaccionAxInv;
import com.princetonsa.mundo.ConsecutivosDisponibles;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.AlmacenParametros;
import com.princetonsa.mundo.inventarios.DespachoPedidoQx;
import com.princetonsa.mundo.pedidos.DespachoPedidos;
import com.princetonsa.mundo.salasCirugia.MaterialesQx;
import com.princetonsa.mundo.salasCirugia.Peticion;
import com.princetonsa.pdf.DespachoPedidosPdf;

/**
 * Clase que implementa los metodos para manejar el WorkFlow
 * @author Sebasti�n G�mez R.
 *
 */
public class DespachoPedidoQxAction extends Action 
{
	/**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(DespachoPedidoQxAction.class);
    /**
	 * M�todo execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
	{
		
		Connection con = null;
		try
		{
		
	    if(form instanceof DespachoPedidoQxForm)
	    {
	       DespachoPedidoQxForm forma=(DespachoPedidoQxForm)form; 
	       HttpSession sesion = request.getSession();			
	       
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(sesion);
			PersonaBasica paciente = Utilidades.getPersonaBasicaSesion(sesion);
			String estado=forma.getEstado();
			logger.warn("[DespachoPedidoQxAction] --> "+estado);
			
			
			con = UtilidadBD.abrirConexion();
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de DespachoPedidoQxAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			if(estado.equals("empezar"))
			{
			  return accionEmpezar(con,forma,mapping,usuario,request);			  		  
			}
			else if(estado.equals("empezarPaciente")) //Estado para la opcion por Paciente
			{
				return accionEmpezarPaciente(con,forma,paciente,mapping,request,usuario);
			}
			else if (estado.equals("empezarArea")) //Estado para la opcion por Area
			{
				return accionEmpezarArea(con,forma,mapping,usuario);
			}
			else if (estado.equals("consultarArea")) //Estado para la opcion por Area
			{
				return accionConsultarArea(con,forma,mapping,usuario,request);
			}
			else if (estado.equals("ordenar"))
			{
				return accionOrdenar(con,forma,mapping);
			}
			else if (estado.equals("redireccion"))
			{
				return accionRedireccion(con,forma,response,mapping,request);
			}
			else if (estado.equals("detalle"))
			{
				return accionDetalle(con,forma,mapping,usuario,paciente,request);
			}
			else if (estado.equals("volverPeticiones"))
			{
				return accionVolverPeticiones(con,forma,mapping,usuario,paciente);
				
				
			}
			else if (estado.equals("guardarFrame"))
			//Estado usado para recargar el listado de art�culos 
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("listadoArticulos");
			}
			else if (estado.equals("actualizarFrame"))
				//Actualiza datos Frame  
				{
				return mapping.findForward("listadoArticulos");
				}
			else if (estado.equals("guardarDespacho"))
			{
				
				return accionGuardarDespacho(con,forma,mapping,usuario, paciente,request);
			}
			else if (estado.equals("imprimir"))
			{
				return accionImprimir(con,forma,mapping,usuario,request);
			}
    		else
			{
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}

            
	    }
	    else
	    {
	        logger.error("El form no es compatible con el form de DespachoTrasladoAlmacenForm");
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
	 * M�todo implementado para realizar la impresi�n del despacho del pedido Qx.
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, DespachoPedidoQxForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		UtilidadBD.closeConnection(con);
		///se edita nombre del archivo PDF
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/despachoPedidoQx" + r.nextInt()  +".pdf";
    	
    	
    	DespachoPedidosPdf.pdfDespachoPedidosQx(ValoresPorDefecto.getFilePath()+nombreArchivo, forma, usuario, request);
    	
    	request.setAttribute("nombreArchivo", nombreArchivo);
    	request.setAttribute("nombreVentana", "Despacho Pedido Quir�rgico");
    	return mapping.findForward("abrirPdf");
	}


	/**
	 * M�todo implementado para volver a cargar el listado de peticiones
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionVolverPeticiones(Connection con, DespachoPedidoQxForm forma, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		//Se cargan las peticiones 
		if(forma.getCodigoCentroCosto()!=ConstantesBD.codigoNuncaValido)
			forma.setPeticiones(DespachoPedidoQx.consultarPeticiones(con, "", forma.getCodigoCentroCosto()+"", usuario.getCodigoCentroCosto()+""));
		else
			forma.setPeticiones(DespachoPedidoQx.consultarPeticiones(con, paciente.getCodigoPersona()+"", "", usuario.getCodigoCentroCosto()+""));
		forma.setNumPeticiones(Integer.parseInt(forma.getPeticiones("numRegistros").toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoPeticiones");
	}


	/**
	 * M�todo implementado para consultar las peticiones por el centro de costo seleccionado
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionConsultarArea(Connection con, DespachoPedidoQxForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//Se verifia que el centro de costo se haya seleccionado
		if(forma.getCodigoCentroCosto()==ConstantesBD.codigoNuncaValido)
		{
			forma.setEstado("empezarArea");
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("errors.required","El área"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("opciones");
		}
		
		//Se cargan las peticiones 
		forma.setPeticiones(DespachoPedidoQx.consultarPeticiones(con, "", forma.getCodigoCentroCosto()+"", usuario.getCodigoCentroCosto()+""));
		forma.setNumPeticiones(Integer.parseInt(forma.getPeticiones("numRegistros").toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoPeticiones");
	}


	/**
	 * M�todo para iniciar el flujo del despacho por �rea
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionEmpezarArea(Connection con, DespachoPedidoQxForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setCentrosCosto(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"", false, 0,false));
		forma.setNumCentrosCosto(Integer.parseInt(forma.getCentrosCosto("numRegistros").toString()));
		forma.setCodigoCentroCosto(ConstantesBD.codigoNuncaValido);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("opciones");
	}


	/**
	 * M�todo implementado para guardar el despacho de pedidos de la peticion 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param paciente 
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarDespacho(Connection con, DespachoPedidoQxForm forma, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		HashMap articulos = forma.getArticulos(); //se toman los articulos
		HashMap detalleDespacho = new HashMap(); //mapa para armar el detalle del despacho
		int cantidadPedido = 0, cantidadADespachar = 0, codigoArticulo = 0, existenciasArticulo = 0, numDetalleDespacho = 0, numeroPedido = 0, numPedidosChequeados = 0, contadorPedidos = 0;
		boolean manejaLote = false, exito = false;
		String lote = "", fechaVencimiento = "", descripcionArticulo = "", tipoDespacho = "", convenioProveedor = "", almacenConsignacion = "", listadoPedidos = "";
		ActionErrors errores = new ActionErrors();
		ActionErrors mensajesAlerta = new ActionErrors();
		//se toma el parametro que indica si se permiten existencias negativas
        boolean existenciasNegativas = AlmacenParametros.manejaExistenciasNegativas(con, usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt());
        
       
        //Se consulta el numero de pedidos chequeados
        for(int i=0;i<forma.getNumPedidos();i++)
        	if(UtilidadTexto.getBoolean(forma.getPedidos("chequeado_"+i).toString()))
        		numPedidosChequeados ++;
        		
		UtilidadBD.iniciarTransaccion(con);
		
		//***************ITERACION DE LOS PEDIDOS*******************************************************************************
		for(int i=0;i<forma.getNumPedidos();i++)
		{
			//solo se despacharan los pedidos que se encuentren chequeados
			if(UtilidadTexto.getBoolean(forma.getPedidos("chequeado_"+i).toString()))
			{
				contadorPedidos ++;
				detalleDespacho = new HashMap(); //mapa para armar el detalle del despacho
				numDetalleDespacho = 0;
				numeroPedido = Integer.parseInt(forma.getPedidos("codigoPedido_"+i).toString());
				
				//Se toma el codigo del pedido chequeado
				if(!listadoPedidos.equals(""))
					listadoPedidos += ",";
				listadoPedidos += numeroPedido;

				//****************ITERACION DE LOS ART�CULOS*****************************************************
				for(int j=0;j<forma.getNumArticulos();j++)
				{
					codigoArticulo = Integer.parseInt(articulos.get("codigosArt_"+j).toString());
					manejaLote = UtilidadTexto.getBoolean(articulos.get("manejaLote_"+j).toString());
					lote = articulos.get("lote_"+j).toString();
					fechaVencimiento = articulos.get("fechaVencimiento_"+j).toString();
					descripcionArticulo = articulos.get("articulo_"+j).toString();
					tipoDespacho = articulos.get("tipoDespacho_"+j) + "";
					almacenConsignacion = articulos.get("almacenConsignacion_"+j) + "";
					convenioProveedor = articulos.get("convenioProveedor_"+j) + "";
					
					//Se verifica que el art�culo se encuentre dentro del pedido
					cantidadPedido = Integer.parseInt(articulos.get("cantidadPedido_"+j+"_"+i).toString());
					cantidadADespachar = Integer.parseInt(articulos.get("cantidadADespachar_"+j).toString());
					if(cantidadPedido>0)
					{
						if(cantidadADespachar>0)
						{
							//Si el pedido tiene m�s de la cantidad a despachar o es el �ltimo pedido chequeado
							//entonces se despacha lo que queda de la cantidad a despachar
							if(cantidadPedido>cantidadADespachar||contadorPedidos==numPedidosChequeados)
								cantidadPedido = cantidadADespachar;
						}
						else
							cantidadPedido = 0;
							
						
						//Se recalcula la cantidad a despachar
						articulos.put("cantidadADespachar_"+j, (cantidadADespachar-cantidadPedido)+"");
						
						//Se llena la informaci�n del detalle del pedido
						detalleDespacho.put("codigosArt_"+numDetalleDespacho, codigoArticulo+"");
						detalleDespacho.put("descripcionArt_"+numDetalleDespacho, descripcionArticulo);
						detalleDespacho.put("cantidadADespachar_"+numDetalleDespacho, cantidadPedido+"");
						detalleDespacho.put("lote_"+numDetalleDespacho, lote);
						detalleDespacho.put("fechavencimiento_"+numDetalleDespacho, fechaVencimiento);
						
						
						//1) Se bloquea registro de almacen del art�culo ------------------------------------------------------
			        	ArrayList filtro=new ArrayList();
				        filtro.add(codigoArticulo);
				        filtro.add(usuario.getCodigoCentroCosto());
				        UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearArticulosAlmacen,filtro);
				        ArrayList<ActionErrors> arrayErrores = new ArrayList<ActionErrors>();
				        //2) Manejo de la interfaz de compras -----------------------------------------------------------------
				        if(UtilidadTexto.getBoolean(forma.getManejaInterfazCompras()))
				        {
				        	//Se agregan los campos de la interfaz compra al detalle del despacho
				        	detalleDespacho.put("tipodespacho_"+numDetalleDespacho,tipoDespacho);
				        	if(tipoDespacho.equals(ConstantesIntegridadDominio.acronimoTipoDespachoConsignacion))
				        	{
				        		detalleDespacho.put("almacenConsignacion_"+numDetalleDespacho,almacenConsignacion);
				        		detalleDespacho.put("proveedorCompra_"+numDetalleDespacho,convenioProveedor);
				        	}
				        	else if (tipoDespacho.equals(ConstantesIntegridadDominio.acronimoTipoDespachoCompraProveedor))
				        		detalleDespacho.put("proveedorCatalogo_"+numDetalleDespacho,convenioProveedor);
				        	
				        	arrayErrores = manejoInterfazComprasArticulo(con,articulos,j,errores,usuario,paciente,cantidadPedido,numeroPedido, forma.getCodigoCentroCostoSolicitante(),Utilidades.obtenerValor(detalleDespacho.get("proveedorCompra_"+numDetalleDespacho)),Utilidades.obtenerValor(detalleDespacho.get("proveedorCatalogo_"+numDetalleDespacho)));
				        }
				        if (arrayErrores.size() > 0){
				        	errores = arrayErrores.get(0);
					        if (arrayErrores.get(1)!=null)
					        	mensajesAlerta = arrayErrores.get(1);
				        }
				        //3) Actualizaci�n de las existencias del art�culo --------------------------------------------------------
			        	//Se toman las existencias actuales del art�culo
			        	if(manejaLote)
			        		existenciasArticulo = Integer.parseInt(UtilidadInventarios.existenciasArticuloAlmacenLote(codigoArticulo, usuario.getCodigoCentroCosto(),lote,UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento))+"");
			        	else
			        		existenciasArticulo = Integer.parseInt(UtilidadInventarios.existenciasArticuloAlmacen(codigoArticulo, usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt())+"");
			        	
			        	if(!existenciasNegativas)
			        	{
				        	if(cantidadPedido>existenciasArticulo)
				    		{
					            errores.add("error.inventarios.existenciasInsuficientes", 
					                    new ActionMessage("error.inventarios.existenciasInsuficientes", 
					                    		descripcionArticulo, 
					                    		existenciasArticulo+"", 
					                    		usuario.getCentroCosto()+" EN EL PEDIDO Nro "+numeroPedido ));
					            //Se actualizan las existencias del articulo en el HashMap
					            forma.setArticulos("existenciasArticulo_"+j, existenciasArticulo+"");
					    	}
				        	
			        	}	
			        	
			        	//se actualizan existencias
			        	try
			        	{
				        	if(manejaLote )
				        		exito=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,codigoArticulo,usuario.getCodigoCentroCosto(),false,cantidadPedido,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,lote,UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento));        		
				        	else
				        		exito=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,codigoArticulo,usuario.getCodigoCentroCosto(),false,cantidadPedido,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
			        	}
			        	catch (Exception e) 
			        	{
							logger.error("Error en la actualizacion de existencias de articulo: "+e);
							exito = false;
						}
			        	
			        	if(!exito)
			        		errores.add("",new ActionMessage("errors.problemasGenericos","al actualizar las existencias del artículo "+codigoArticulo+" en el pedido Nro "+numeroPedido));
			            
			        	
			        	//4) Obtenci�n del costo del art�culo-------------------------------------------------------------------------------------
			        	detalleDespacho.put("costo_"+numDetalleDespacho, UtilidadInventarios.costoActualArticulo(codigoArticulo));
			        	
				        numDetalleDespacho++;
					}
				}
				//***********************FIN ITERACION DE ARTICULOS************************************************************************
				detalleDespacho.put("numRegistros", numDetalleDespacho+"");
				
				
				//si no hay errores se continua con el proceso del despacho chequeado
				if(errores.isEmpty())
				{
					
					ArrayList<ActionErrors> arregloErrores = ingresarDespachoElegido(con,forma,usuario,errores,i,detalleDespacho);
					errores = (ActionErrors)arregloErrores.get(0);
					if (mensajesAlerta.size()>0)
					{
						Iterator it=arregloErrores.get(1).get();
						while(it.hasNext())
						{
							mensajesAlerta.add("alerta", (ActionMessage)it.next());
						}
					}
					else
						mensajesAlerta = arregloErrores.get(1);
				}
			}
			
		}
		//****************************FIN ITERACION DE PEDIDOS********************************************************************************************
		
		//Se verifica que se haya seleccionado al menos 1 pedido
		if(listadoPedidos.equals(""))
			errores.add("",new ActionMessage("errors.minimoCampos2","la selección de un pedido","realizar el despacho"));
		
		//Se verifica si hubo errores
		if(!errores.isEmpty())
		{
			UtilidadBD.abortarTransaccion(con);
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalle");
			
		}
		
		UtilidadBD.finalizarTransaccion(con);
		saveErrors(request,mensajesAlerta);
		
		
		
		
		
		//*****************SE CARGA EL RESUMEN DEL PROCESO*******************************************
		//INFORMACION DEL ENCABEZADO
		forma.setListadoPedidos(listadoPedidos);
		forma.setNombrePaciente(Peticion.getApellidosNombresPacientePeticion(con, Integer.parseInt(forma.getNumeroPeticion())));
		forma.setFechaDespacho(UtilidadFecha.getFechaActual(con));
		forma.setHoraDespacho(UtilidadFecha.getHoraActual(con));
		forma.setNombreUsuarioDespacho(usuario.getNombreUsuario());
		forma.setNombreFarmacia(usuario.getCentroCosto()+" ("+usuario.getCentroAtencion()+")");
		
		
		
		//INFORMACION DEL DETALLE
		ArrayList<HashMap> resultado = DespachoPedidoQx.cargarDetalleArticulosPeticion(con, Integer.parseInt(forma.getNumeroPeticion()), usuario,listadoPedidos);
		
		//Se asignan los art�culos
		forma.setArticulos((HashMap)resultado.get(0));
		forma.setNumArticulos(Integer.parseInt(forma.getArticulos("numRegistros").toString()));
		
		//Se asignan los pedidos
		forma.setPedidos((HashMap)resultado.get(1));
		forma.setNumPedidos(Integer.parseInt(forma.getPedidos("numRegistros").toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumen");
	}
	
	
	private ArrayList<ActionErrors> ingresarDespachoElegido(Connection con, DespachoPedidoQxForm forma, UsuarioBasico usuario, ActionErrors errores, int pos, HashMap detalleDespacho) 
	{
		DespachoPedidos mundo=new DespachoPedidos();
		int numeroPedido = Integer.parseInt(forma.getPedidos("codigoPedido_"+pos).toString());
		int numDetalleDespacho = Integer.parseInt(detalleDespacho.get("numRegistros").toString());
		int codigoArticulo = 0;
		String mensajesStockMinimo = "", mensajesStockMaximo = "", mensajesPuntoPedido = "";
		ActionErrors mensajesAlerta = new ActionErrors();
		
		//AQUI SE FINALIZA LA TRANSACCION DEL DESPACHO ********************************************************************
		int resp0 = 0;
		
		try
		{
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			for (int i = 0; i< Utilidades.convertirAEntero(forma.getPedidos("numRegistros")+"");i++ )
			{
				hashMap.put("codigoPedido_"+i,forma.getPedidos("codigoPedido_"+i)+"");
				hashMap.put("fechaPedido_"+i,forma.getPedidos("fechaPedido_"+i)+"");
				hashMap.put("horaPedido_"+i,forma.getPedidos("horaPedido_"+i)+"");
				
			}
			
			mundo.setFarmacia(forma.getArticulos("centroCostoSolicitado_0")+"");
			mundo.setFechaHoraPedidos(hashMap);
			mundo.setFechaDespacho(forma.getFechaDespacho());
			mundo.setHoraDespacho(forma.getHoraDespacho());
			mundo.setUsuario(usuario);
			mundo.setCentroCostoSolicitante(forma.getCodigoCentroCostoSolicitante()+"");

			resp0 = mundo.insertarDespacho(
				con,
				detalleDespacho, 
				numDetalleDespacho, 
				numeroPedido, 
				usuario.getLoginUsuario(),
				ConstantesBD.continuarTransaccion,
				"DespachoPedidoQxAction");
			
		}
		catch (Exception e) 
		{
			logger.error("Error en la inserción del despacho: "+e);
			e.printStackTrace();
		}
		
		if(resp0==(Integer.parseInt(IdentificadoresExcepcionesSql.codigoExcepcionSqlRegistroExistente)*-1))
			errores.add("",new ActionMessage("errors.excepcionSQL.registroYaActualizado","DESPACHO DE PEDIDO Nro."+numeroPedido));
		else if(resp0<=0)
			errores.add("",new ActionMessage("errors.problemasGenericos","insertando el despacho del pedido Nro. "+numeroPedido));
			
			
		//Si no han habido errores hasta ahora se continua
		if (errores.isEmpty())
		{
			//**************MENSAJES DE ALERTA*********************
			 for(int i=0;i<numDetalleDespacho;i++)
	        {
			 	//se toma el codigo del articulo
	        	codigoArticulo = Integer.parseInt(detalleDespacho.get("codigosArt_"+i)+"");
	        	
	        	
		 		//STOCK M�NIMO
	        	if(!UtilidadInventarios.existenciasArticuloMayorIgualStockMinimo(codigoArticulo,usuario.getCodigoInstitucionInt()))
	        	{
	        		if(!mensajesStockMinimo.equals(""))
	        			mensajesStockMinimo+=", ";
	        		mensajesStockMinimo += codigoArticulo;
	        	}
	        	//STOCK MAXIMO
	        	if(!UtilidadInventarios.existenciasArticuloMenorIgualStockMaximo(codigoArticulo,usuario.getCodigoInstitucionInt()))
	        	{
	        		if(!mensajesStockMaximo.equals(""))
	        			mensajesStockMaximo += ", ";
	        		mensajesStockMaximo += codigoArticulo;
	        	}
	        	//PUNTO PEDIDO
	        	if(!UtilidadInventarios.existenciasArticuloMayorIgualPuntoPedido(codigoArticulo,usuario.getCodigoInstitucionInt()))
	        	{
	        		if(!mensajesPuntoPedido.equals(""))
	        			mensajesPuntoPedido += ", ";
	        		mensajesPuntoPedido += codigoArticulo;
	        	}
	        }
			 //se revisa si hubo alertas
	        if(!mensajesStockMinimo.equals(""))
	        	mensajesAlerta.add("cantidades menores al stock mínimo",
	        		new ActionMessage("error.inventarios.quedanConCantidad",mensajesStockMinimo,"MENOR","STOCK MÍNIMO"));
	        
	        if(!mensajesStockMaximo.equals(""))
	        	mensajesAlerta.add("cantidades mayores al stock máximo",
	        		new ActionMessage("error.inventarios.quedanConCantidad",mensajesStockMaximo,"MAYOR","STOCK MÁXIMO"));
	        
	        if(!mensajesPuntoPedido.equals(""))
	        	mensajesAlerta.add("cantidades menores al punto pedido",
	        		new ActionMessage("error.inventarios.quedanConCantidad",mensajesPuntoPedido,"MENOR","PUNTO DE PEDIDO"));
				
		
		}
		
		ArrayList<ActionErrors> arregloErrores = new ArrayList<ActionErrors>();
		arregloErrores.add(errores);
		arregloErrores.add(mensajesAlerta);
		
		return arregloErrores;
	}


	/**
	 * M�todo implementado para el manejo de la interfaz de compras para cada articulo a despachar
	 * @param con
	 * @param articulos
	 * @param j
	 * @param errores
	 * @param usuario
	 * @param paciente 
	 * @param cantidadPedido
	 * @param numeroPedido 
	 * @param centroCostoSolicita 
	 * @return
	 */
	private ArrayList manejoInterfazComprasArticulo(Connection con, HashMap articulos, int j, ActionErrors errores, UsuarioBasico usuario, PersonaBasica paciente, int cantidadPedido, int numeroPedido, int centroCostoSolicita,String proveedorCompra,String proveedorCatalogo) 
	{
		ArrayList<ActionErrors> array = new ArrayList<ActionErrors>();
		ActionErrors alerta = new ActionErrors();
		int codigoArticulo = Integer.parseInt(articulos.get("codigosArt_"+j).toString());
		String lote = articulos.get("lote_"+j).toString();
		String fechaVencimiento = articulos.get("fechaVencimiento_"+j).toString();
		String convenioProveedor = articulos.get("convenioProveedor_"+j)+"";
		String almacenConsignacion = articulos.get("almacenConsignacion_"+j)+"";
		boolean manejaLote = UtilidadTexto.getBoolean(articulos.get("manejaLote_"+j).toString());
		double valorUnitario = 0;
		
		
    	//*******************************TIPO DESPACHO CONSIGNACION*********************************************************************************
    	if(articulos.get("tipoDespacho_"+j).toString().trim().equals(ConstantesIntegridadDominio.acronimoTipoDespachoConsignacion))
    	{
    		valorUnitario=UtilidadInventarios.obtenerValorArticuloProveedorConveProveedor(con,convenioProveedor,codigoArticulo);
    		
    		boolean transaccionExitosa=false;
    		
        	String tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
        	int tipoTransaccion1=UtilidadInventarios.obtenerTipoTransaccionInterfaz(con,ConstantesBD.codigoTransaccionSalidaConsignacionConsumos,usuario.getCodigoInstitucionInt());
        	
        	//1.0 Validaci�n del tipo de transacci�n de interfaz
        	if(tipoTransaccion1==ConstantesBD.codigoNuncaValido)
        		errores.add("", new ActionMessage("error.inventarios.faltaDefinirTipoTransInterfaz","DE SALIDA CONSIGNACIÓN CONSUMOS",codigoArticulo+" DEL PEDIDO Nro "+numeroPedido));
        	else
        	{
	        	
	            ConsecutivosDisponibles consec=new ConsecutivosDisponibles();
	            String consecutivo="";
	            
	            
	            int codigoAlmacen=Utilidades.convertirAEntero(almacenConsignacion); 
	            int codigoAlmacenConsecutivo=0;
	            
	            if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosUnicoSistema))
	            	codigoAlmacenConsecutivo=ConstantesBD.codigoNuncaValido;        
	            else if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen))
	            	codigoAlmacenConsecutivo=Utilidades.convertirAEntero(almacenConsignacion); 
	
	            //1.1 Validacion del consecutivo ---------------------------------------------------------------------------------------------------
	            consecutivo=consec.obtenerConsecutivoInventario(con,tipoTransaccion1,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt())+"";
	            
	            if(Integer.parseInt(consecutivo)==ConstantesBD.codigoNuncaValido)
	            	errores.add("falta consecutivo", new ActionMessage("error.inventarios.faltaDefinirConsecutivoTransAlmacen",UtilidadInventarios.obtenerNombreTipoTransaccion(con,tipoTransaccion1,usuario.getCodigoInstitucionInt()),Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
	            else
	            {
	            	//1.2 Bloqueo de tabla consecutivos_inventarios -------------------------------------------------------------------------
		            ArrayList filtro1=new ArrayList();
		            filtro1.add(tipoTransaccion1+"");
		            filtro1.add(usuario.getCodigoInstitucionInt()+"");
		            filtro1.add(codigoAlmacenConsecutivo+"");
		            UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoConsecutivoInventariosAlmacen,filtro1);
	
		            consecutivo=consec.obtenerConsecutivoInventario(con,tipoTransaccion1,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt())+"";
		            //------------------------------------------------------------------------------------------------------------------------------------------
		            
		            //1.3 Se inserta el encabezado de la transacci�n de inventarios-----------------------------------------------------------------------------------------------------------------------------------------------
		            int codTransaccion1=UtilidadInventarios.generarEncabezadoTransaccion(con,Integer.parseInt(consecutivo),tipoTransaccion1,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido,"",ConstantesBDInventarios.codigoEstadoTransaccionInventarioPendiente,codigoAlmacen,true);
		            
		            if(codTransaccion1>0)
		            {
				     //1.4 Se actualiza el consecutivo de invetarios a la siguiente secuencia -----------------------------------------------
			          if(consec.actualizarValorConsecutivoInventarios(con,tipoTransaccion1,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt()))
			          {
	
			        	//1.5 Insertando el detalle de la transacci�n de inventarios ----------------------------------------------------------------------------------------------------
			            transaccionExitosa=UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion1,codigoArticulo,cantidadPedido,valorUnitario+"",lote,UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento),proveedorCompra,proveedorCatalogo);
			            
			            if(transaccionExitosa)
			            {
			            
			            	//1.6 Generaci�n del registro de cierre de inventarios ----------------------------------------------------------------------------------------------------
				            transaccionExitosa=UtilidadInventarios.generarRegistroCierreTransaccion(con,codTransaccion1+"",usuario.getLoginUsuario(),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
				            
				            if(transaccionExitosa)
				            {
				            	
				            	//1.7 Actualizaci�n de las existencias del art�culo ----------------------------------------------------
				            	try
				            	{
								    if(manejaLote)
								    	transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,codigoArticulo,codigoAlmacen,false,cantidadPedido,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,lote,UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento));        		
						        	else
						        		transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,codigoArticulo,codigoAlmacen,false,cantidadPedido,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
				            	}
				            	catch(SQLException e)
				            	{
				            		logger.error("Error en la actualización de las existencias del articulo: "+e);
				            		transaccionExitosa = false;
				            	}
							    
							    if(transaccionExitosa)
							    {
			
						        	tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
						        	int tipoTransaccion2=UtilidadInventarios.obtenerTipoTransaccionInterfaz(con,ConstantesBD.codigoTransaccionEntradaCompraConsignacion,usuario.getCodigoInstitucionInt());
						        	
						        	//1.7.1 Validacion del tipo de transacci�n interfaz -------------------------------------------------------------------------------------------
						        	if(tipoTransaccion2==ConstantesBD.codigoNuncaValido)
						        		errores.add("", new ActionMessage("error.inventarios.faltaDefinirTipoTransInterfaz","DE ENTRADA COMPRA CONSIGNACIÓN",codigoArticulo+" DEL PEDIDO Nro "+numeroPedido));
						        	else
						        	{
							        	consecutivo="";
							            codigoAlmacen=usuario.getCodigoCentroCosto(); 
							            codigoAlmacenConsecutivo=0;
							            
							            if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosUnicoSistema))
							            	codigoAlmacenConsecutivo=ConstantesBD.codigoNuncaValido;        
							            else if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen))
							            	codigoAlmacenConsecutivo=usuario.getCodigoCentroCosto();
							            
							            //validacion consecutivo
							            consecutivo=consec.obtenerConsecutivoInventario(con,tipoTransaccion2,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt())+"";
							            
							            if(Integer.parseInt(consecutivo)==ConstantesBD.codigoNuncaValido)
							            	errores.add("falta consecutivo", new ActionMessage("error.inventarios.faltaDefinirConsecutivoTransAlmacen",UtilidadInventarios.obtenerNombreTipoTransaccion(con,tipoTransaccion1,usuario.getCodigoInstitucionInt()),Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
							            else
							            {
								            filtro1=new ArrayList();
								            filtro1.add(tipoTransaccion2+"");
								            filtro1.add(usuario.getCodigoInstitucionInt()+"");
								            filtro1.add(codigoAlmacenConsecutivo+"");
								            UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoConsecutivoInventariosAlmacen,filtro1);
						
								            consecutivo=consec.obtenerConsecutivoInventario(con,tipoTransaccion2,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt())+"";
								            
								            
								            //1.8 Inserci�n encabezado de transacci�n de inventarios ---------------------------------------------------------------------------------------------
								            int codTransaccion2=UtilidadInventarios.generarEncabezadoTransaccion(con,Integer.parseInt(consecutivo),tipoTransaccion2,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido,"",ConstantesBDInventarios.codigoEstadoTransaccionInventarioPendiente,codigoAlmacen,true);
										     
								            
								            if(codTransaccion2>0)
								            {
								            
								            	//1.9 Actualizacion del consecutivo de inventarios ---------------------------------------------------------------------------------
									            if(consec.actualizarValorConsecutivoInventarios(con,tipoTransaccion2,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt()))
									            {
									            	//1.10 Inserci�n del detalle de la transaccion de inventarios ------------------------------------------------------------------------------------------------------------------------	
										            transaccionExitosa=UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion2,codigoArticulo,cantidadPedido,valorUnitario+"",lote,UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento),proveedorCompra,proveedorCatalogo);
										            
										            if(transaccionExitosa)
										            {
										            	//1.11 Generaci�n del cierre de inventarios -------------------------------------------------------------------------------------------
											            transaccionExitosa=UtilidadInventarios.generarRegistroCierreTransaccion(con,codTransaccion2+"",usuario.getLoginUsuario(),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
											            
											            if(transaccionExitosa)
											            {
											            	//1.12 Actualizaci�n de las existencias del art�culo
											            	try
											            	{
															    if(manejaLote )
															    	transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,codigoArticulo,codigoAlmacen,true,cantidadPedido,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,lote,UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento));        		
													        	else
													        		transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,codigoArticulo,codigoAlmacen,true,cantidadPedido,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
											            	}
											            	catch (SQLException e) 
											            	{
																logger.error("Error en la actualizacion de exitencias: "+e);
																transaccionExitosa = false;
															}
														    
														    if(transaccionExitosa)
														    {
														    
															    if(UtilidadInventarios.actualizarPrecioUltimaCompra(con,codigoArticulo, valorUnitario))
															    {
															 
															    	//1.13 Generación del registro de interfaz de consignacion
																   ResultadoInteger resultadoInteger = generarRegistroTransaccionInterfazConsignacion(con,
																    													usuario.getCodigoInstitucionInt(),
																    													codigoArticulo,
																    													codTransaccion1,
																    													codTransaccion2,
																    													tipoTransaccion1,
																    													tipoTransaccion2,
																    													ConstantesBD.codigoTransaccionSalidaConsignacionConsumos,
																    													ConstantesBD.codigoTransaccionEntradaCompraConsignacion,
																    													cantidadPedido,
																    													valorUnitario,
																    													UtilidadInventarios.obtenerValorIvaArticuloProveedorConveProveedor(con,convenioProveedor,codigoArticulo),
																    													codigoAlmacen, centroCostoSolicita,
																    													Utilidades.convertirAEntero(almacenConsignacion),
																    													usuario.getLoginUsuario(), 
																    													convenioProveedor,
																    													paciente.getCodigoPersona(),
																    													Utilidades.convertirAEntero(paciente.getConsecutivoIngreso()));
																   
																    int ingresoInterfaz=resultadoInteger.getResultado();
																    
																    if(ingresoInterfaz<=0)
																    	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL REGISTRO DE INTERFAZ INVENTARIOS PARA EL ARTICULO "+codigoArticulo+" DEL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" EN EL PEDIDO Nro "+numeroPedido));
																    else
																    	alerta.add("",new ActionMessage("prompt.generico",resultadoInteger.getDescripcion()));
															    }
															    else
															    	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACIÓN DEL ULTIMO PRECIO DE COMPRA PARA EL ARTICULO "+codigoArticulo+" DEL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" EN EL PEDIDO Nro "+numeroPedido));
													            //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
														    }
														    else
														    	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACIÓN DE EXITENCIAS PARA EL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
												            //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
														    
											            }
											            else
											            	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL REGISTRO DE CIERRE DE INVENTARIOS DE LA TRANSACCIÓN DE INVENTARIOS PARA EL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
											            //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
											            
										            }
										            else
										            	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL DETALLE DE LA TRANSACCIÓN DE INVENTARIOS PARA EL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
										            //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
									            }
									            else
									            	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACIÓN DEL CONSECUTIVO DE INVENTARIOS PARA EL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
										         //---------------------------------------------------------------------------------------------------------------------------------------------------------
									            
								            }
								            else
								            	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA TRANSACCIÓN DE INVENTARIOS PARA EL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
								            //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
										    
							            }
						            
							    	}
							    }
							    else
							    	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACIÓN DE EXITENCIAS PARA EL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
					            //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
				            }
				            else
				            	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL REGISTRO DE CIERRE DE INVENTARIOS DE LA TRANSACCIÓN DE INVENTARIOS PARA EL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
				            //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			            }
			            else
			            	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL DETALLE DE LA TRANSACCIÓN DE INVENTARIOS PARA EL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
			            //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			          }
			          else
			        	  errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACIÓN DEL CONSECUTIVO DE INVENTARIOS PARA EL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
			          //---------------------------------------------------------------------------------------------------------------------------------------------------------
		            }
		            else
		            	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA TRANSACCIÓN DE INVENTARIOS PARA EL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
		            //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		            	
	            }
	            //--------------------------------------------------------------------------------------------------------------------
        	}
    	}
    	//******************************************TIPO DESPACHO COMPRA PROVEEDOR********************************************************************************
    	else if(articulos.get("tipoDespacho_"+j).toString().trim().equals(ConstantesIntegridadDominio.acronimoTipoDespachoCompraProveedor))
    	{
    		valorUnitario=UtilidadInventarios.obtenerValorArticuloProveedorCatalogoProveedor(con,convenioProveedor,codigoArticulo);
    		
    		boolean transaccionExitosa=false;
    		
        	String tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
        	int tipoTransaccion=UtilidadInventarios.obtenerTipoTransaccionInterfaz(con,ConstantesBD.codigoTransaccionEntradaComprasProveedor,usuario.getCodigoInstitucionInt());
        	
        	
        	//2.0 Validaci�n  del tipo de transacci�n de interfaz ----------------------------------------------------------------------------------
        	if(tipoTransaccion==ConstantesBD.codigoNuncaValido)
        		errores.add("", new ActionMessage("error.inventarios.faltaDefinirTipoTransInterfaz","DE ENTRADA COMPRA PROVEEDOR",codigoArticulo+" DEL PEDIDO Nro "+numeroPedido ));
        	else
        	{
        	
	            ConsecutivosDisponibles consec=new ConsecutivosDisponibles();
	            String consecutivo="";
	            
	            int codigoAlmacen=usuario.getCodigoCentroCosto(); 
	            int codigoAlmacenConsecutivo=0;
	            
	            if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosUnicoSistema))
	            	codigoAlmacenConsecutivo=ConstantesBD.codigoNuncaValido;        
	            else if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen))
	            	codigoAlmacenConsecutivo=usuario.getCodigoCentroCosto(); 
	
	            //validacion consecutivo
	            consecutivo=consec.obtenerConsecutivoInventario(con,tipoTransaccion,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt())+"";
	            
	            if(Integer.parseInt(consecutivo)==ConstantesBD.codigoNuncaValido)
	            	errores.add("falta consecutivo", new ActionMessage("error.inventarios.faltaDefinirConsecutivoTransAlmacen",UtilidadInventarios.obtenerNombreTipoTransaccion(con,tipoTransaccion,usuario.getCodigoInstitucionInt()),Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
	            else
	            {
	            	//2.1 Bloqueo del registro de consecutivo de inventarios -----------------------------------------------------------------
		            ArrayList filtro1=new ArrayList();
		            filtro1.add(tipoTransaccion+"");
		            filtro1.add(usuario.getCodigoInstitucionInt()+"");
		            filtro1.add(codigoAlmacenConsecutivo+"");
		            UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoConsecutivoInventariosAlmacen,filtro1);
	
		            consecutivo=consec.obtenerConsecutivoInventario(con,tipoTransaccion,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt())+"";
		            
		            //2.2 Inserci�n del encabezado de la transacci�n de inventarios ------------------------------------------------------------------------------------------------------------------------------------------------------------------
		            int codTransaccion=UtilidadInventarios.generarEncabezadoTransaccion(con,Integer.parseInt(consecutivo),tipoTransaccion,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido,"",ConstantesBDInventarios.codigoEstadoTransaccionInventarioPendiente,codigoAlmacen,true);
		            
		            if(codTransaccion>0)
		            {
		            	//2.3 Actualizaci�n del valor del consecutivo de inventarios ------------------------------------------------------------------------------------------------------------------------------------------------------------------
			            if(consec.actualizarValorConsecutivoInventarios(con,tipoTransaccion,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt()))
			            {
			            		
			            	//2.4 Inserci�n del detalle de la transacci�n ------------------------------------------------------------------------------------------------------------------------------------------------------------------
				            transaccionExitosa=UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion,codigoArticulo,cantidadPedido,valorUnitario+"",lote,UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento),proveedorCompra,proveedorCatalogo);
				            
				            if(transaccionExitosa)
				            {
				            	//2.5 Generaci�n del registro de cierre de inventarios ------------------------------------------------------------------------------------------------------------------------------------------------------------------
					            transaccionExitosa=UtilidadInventarios.generarRegistroCierreTransaccion(con,codTransaccion+"",usuario.getLoginUsuario(),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
					            
					            if(transaccionExitosa)
					            {
					            	//2.6 Actualizaci�n de las existencias del art�culo ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
					            	try
					            	{
									    if(manejaLote )
									    	transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,codigoArticulo,codigoAlmacen,true,cantidadPedido,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,lote,UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento));        		
							        	else
							        		transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,codigoArticulo,codigoAlmacen,true,cantidadPedido,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
					            	}
					            	catch(SQLException e)
					            	{
					            		logger.error("Error en la actualizacion de las existencias del articulo: "+e);
					            		transaccionExitosa = false;
					            	}
								    
								    if(transaccionExitosa)
								    {
								    	//2.7 Actualizaci�n del ultimo precion de compra ----------------------------------------------------------------------------------------------
									    if(UtilidadInventarios.actualizarPrecioUltimaCompra(con,codigoArticulo, valorUnitario))
									    {
									    	
									    
										    ResultadoInteger resultadoInteger = generarRegistroTransaccionInterfazCompraProveedor(con,
																								usuario.getCodigoInstitucionInt(),
																								codigoArticulo,
																								codTransaccion,
																								tipoTransaccion,
																								ConstantesBD.codigoTransaccionEntradaComprasProveedor,
																								cantidadPedido,
																								valorUnitario,
																								UtilidadInventarios.obtenerValorIvaArticuloProveedorCatalogoProveedor(con,convenioProveedor,codigoArticulo),
																								codigoAlmacen, centroCostoSolicita,
																								usuario.getLoginUsuario(),
																								convenioProveedor,
																								paciente.getCodigoPersona(),
																								Utilidades.convertirAEntero(paciente.getConsecutivoIngreso()));
										    
										    int ingresoInterfaz=resultadoInteger.getResultado();
										    
										    if(ingresoInterfaz<=0)
										    	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL REGISTRO DE INTERFAZ INVENTARIOS PARA EL ARTICULO "+codigoArticulo+" DEL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" EN EL PEDIDO Nro "+numeroPedido));
										    else
										    	alerta.add("",new ActionMessage("prompt.generico",resultadoInteger.getDescripcion()));
									    }
									    else
									    	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACIÓN DEL ULTIMO PRECIO DE COMPRA PARA EL ARTICULO "+codigoArticulo+" DEL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" EN EL PEDIDO Nro "+numeroPedido));
							            //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
									    
								    }
								    else
								    	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACIÓN DE EXITENCIAS PARA EL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacen, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
						            //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
					            }
					            else
					            	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL REGISTRO DE CIERRE DE INVENTARIOS DE LA TRANSACCIÓN DE INVENTARIOS PARA EL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
					            //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
				            }
				            else
				            	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL DETALLE DE LA TRANSACCIÓN DE INVENTARIOS PARA EL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
				            //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
				            
			            }
			            else
			            	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACIÓN DEL CONSECUTIVO DE INVENTARIOS PARA EL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
				         //---------------------------------------------------------------------------------------------------------------------------------------------------------
			            
		            }
		            else
		            	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA TRANSACCIÓN DE INVENTARIOS PARA EL ALMACEN "+Utilidades.obtenerNombreCentroCosto(con, codigoAlmacen, usuario.getCodigoInstitucionInt())+" DEL ARTICULO "+codigoArticulo+" EN EL PEDIDO Nro "+numeroPedido));
		            //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
										
	
	            }
        	}
    	}
    	
    	array.add(0, errores);
    	if (alerta.size()>0)
    		array.add(1, alerta);
    	
    	return array;
	        
		
	
	}
	
	
	/**
	 * M�todo para insertar el registro de transacci�n de interfaz para el tipo de despacho compra proveedor
	 * @param con
	 * @param institucion
	 * @param codigoArticulo
	 * @param forma
	 * @param codTransaccion
	 * @param tipoTransaccion
	 * @param tipoTransInterfaz
	 * @param cantidadDespacho
	 * @param valorUnitario
	 * @param valorIva
	 * @param codigoAlmacen 
	 * @param centroCostoSolicita 
	 * @param usuario
	 * @param tercero 
	 * @param codigoIngreso 
	 * @param codigoPersona 
	 */
	private ResultadoInteger generarRegistroTransaccionInterfazCompraProveedor(Connection con,int institucion, int codigoArticulo, int codTransaccion, int tipoTransaccion, int tipoTransInterfaz, int cantidadDespacho, double valorUnitario,double valorIva, int codigoAlmacen, int centroCostoSolicita, String usuario, String tercero, int codigoPersona, int codigoIngreso) 
	{
		UtilidadBDInventarios utilInterfaz=new UtilidadBDInventarios();
		DtoInterfazTransaccionAxInv dto= new DtoInterfazTransaccionAxInv();
		dto.setTipoTransAxioma(tipoTransaccion+"");
		dto.setNumeroTransaccionAxioma(codTransaccion+"");
		dto.setIndicativoTransaccion(tipoTransInterfaz+"");
		dto.setOrigenTransaccion("2");//alimentacion por axioma
		dto.setIndicativoCostoDonacion(ConstantesBD.acronimoNo);
		dto.setFechaTransaccion(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dto.setHoraTransaccion(UtilidadFecha.getHoraActual());
		dto.setCodigoArticuloInterfaz(UtilidadInventarios.obtenerCodigoInterfazArticulo(con,codigoArticulo));
		dto.setCodigoArticulo(codigoArticulo+"");
		dto.setCantidad(cantidadDespacho+"");
		
		logger.info("VALOR TOTAL --->"+UtilidadTexto.formatearValores(valorUnitario+"","#.######")+"<-");
		logger.info("VALOR IVA   --->"+UtilidadTexto.formatearValores(valorIva+"","#.######")+"<-");
		double resultado = valorUnitario-valorIva; 
		logger.info("VALOR RESULTADO --->"+UtilidadTexto.formatearValores(resultado+"","#.######")+"<-");
		
		dto.setValorUnitario(UtilidadTexto.formatearValores(resultado+"","#.######"));
		dto.setValorIva(valorIva+"");
		dto.setEstadoRegistro("0");//estado no procesado
		dto.setFechaRegistro(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dto.setHoraRegistro(UtilidadFecha.getHoraActual());
		dto.setIdentificacionProveedor(tercero);
		
		dto.setCodigoPaciente(codigoPersona+"");
		dto.setIngresoPaciente(codigoIngreso+"");
		
		dto.setAlmacenInterfaz(codigoAlmacen+"");
		dto.setAlmacendespacha(codigoAlmacen+"");
		dto.setAlmacenSolicita(centroCostoSolicita+"");
		
		dto.setUsuario(usuario);
		
		return utilInterfaz.insertarTransaccionInterfaz(dto,institucion,false);
	}
	
	/**
	 * M�todo para insertar un registro de transacci�n de interfaz de compras de consignacion
	 * @param con 
	 * @param codigoArticulo 
	 * @param forma
	 * @param codTransaccion 
	 * @param codTransaccion1 
	 * @param tipoTransaccion2 
	 * @param tipoTransaccion1 
	 * @param tipoTransInterfaz2 
	 * @param tipoTransInterfaz1 
	 * @param cantidadDespacho 
	 * @param codigoAlmacen 
	 * @param centroCostoSolicita
	 * @param codigoAlmacenConsignacion
	 * @param usuario 
	 * @param tercero 
	 * @param codigoIngreso 
	 * @param codigoPersona 
	 */
	private ResultadoInteger generarRegistroTransaccionInterfazConsignacion(Connection con,int institucion, int codigoArticulo, int codTransaccion1, int codTransaccion2, int tipoTransaccion1, int tipoTransaccion2, int tipoTransInterfaz1, int tipoTransInterfaz2, int cantidadDespacho, double valorUnitario,double valorIva, int codigoAlmacen, int centroCostoSolicita, int codigoAlmacenConsignacion, String usuario, String tercero, int codigoPersona, int codigoIngreso) 
	{
		int resp = 0;
		
		UtilidadBDInventarios utilInterfaz=new UtilidadBDInventarios();
		DtoInterfazTransaccionAxInv dto= new DtoInterfazTransaccionAxInv();
		dto.setTipoTransAxioma(tipoTransaccion1+"");
		dto.setNumeroTransaccionAxioma(codTransaccion1+"");
		dto.setIndicativoTransaccion(tipoTransInterfaz1+"");
		dto.setOrigenTransaccion("2");//alimentacion por axioma
		dto.setIndicativoCostoDonacion(ConstantesBD.acronimoNo);
		dto.setFechaTransaccion(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dto.setHoraTransaccion(UtilidadFecha.getHoraActual());
		dto.setCodigoArticuloInterfaz(UtilidadInventarios.obtenerCodigoInterfazArticulo(con,codigoArticulo));
		dto.setCodigoArticulo(codigoArticulo+"");
		dto.setCantidad(cantidadDespacho+"");
		
		logger.info("VALOR TOTAL --->"+UtilidadTexto.formatearValores(valorUnitario+"","#.######")+"<-");
		logger.info("VALOR IVA   --->"+UtilidadTexto.formatearValores(valorIva+"","#.######")+"<-");
		double resultado = valorUnitario-valorIva; 
		logger.info("VALOR RESULTADO --->"+UtilidadTexto.formatearValores(resultado+"","#.######")+"<-");
		
		dto.setValorUnitario(UtilidadTexto.formatearValores(resultado+"","#.######"));
		dto.setValorIva(valorIva+"");
		dto.setEstadoRegistro("0");//estado no procesado
		dto.setFechaRegistro(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dto.setHoraRegistro(UtilidadFecha.getHoraActual());
		dto.setIdentificacionProveedor(tercero);
		
		dto.setCodigoPaciente(codigoPersona+"");
		dto.setIngresoPaciente(codigoIngreso+"");
		
		
		dto.setAlmacenInterfaz(codigoAlmacen+"");
		dto.setAlmacendespacha(codigoAlmacen+"");
		dto.setAlmacenSolicita(centroCostoSolicita+"");
		dto.setAlmacenConsignacion(codigoAlmacenConsignacion+"");
		
		dto.setUsuario(usuario);
		
		return utilInterfaz.insertarTransaccionInterfaz(dto,institucion,false);
	}

	
	/**
	 * M�todo que entra al detalle del despacho de una petici�n
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param paciente 
	 * @param request 
	 * @return
	 */
	private ActionForward accionDetalle(Connection con, DespachoPedidoQxForm forma, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		//Proofs Mt 5595
		forma.setDespachoPendiente(0);
				
		//Se carga el paciente
		paciente.setCodigoPersona(forma.getCodigoPaciente());
		UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
		
		//Se consulta el centro de costo solicitante
		HashMap datosCentrosCosto = MaterialesQx.consultarCentrosCostoPedidosPeticion(con, Integer.parseInt(forma.getNumeroPeticion()));
		forma.setCodigoCentroCostoSolicitante(Integer.parseInt(datosCentrosCosto.get("centroCosto").toString()));
		forma.setNombreCentroCostoSolicitante(datosCentrosCosto.get("nombreCentroCosto").toString());
		
		//Se toma el par�metro de manejo interfaz compras
		forma.setManejaInterfazCompras(ValoresPorDefecto.getInterfazCompras(usuario.getCodigoInstitucionInt()));
		
		if(UtilidadTexto.getBoolean(forma.getManejaInterfazCompras()))
		{
			//Se cargan los almacenes de consignacion, el cat�logo de proveedores y los convenios proveedores
			forma.setAlmacenesConsignacion(UtilidadInventarios.obtenerAlmacenesConsignacion(usuario.getCodigoInstitucionInt()));
			forma.setConveniosProveedor(UtilidadInventarios.obtenerConveniosProveedor(usuario.getCodigoInstitucionInt()));
			forma.setProveedorCatalogo(UtilidadInventarios.obtenerProveedoresCatalogo(usuario.getCodigoInstitucionInt()));
		}
		
		
		ArrayList<HashMap> resultado = DespachoPedidoQx.cargarDetalleArticulosPeticion(con, Integer.parseInt(forma.getNumeroPeticion()), usuario,"");
		
		if(!resultado.isEmpty() ){
			//Se asignan los art�culos
			forma.setArticulos((HashMap)resultado.get(0));
			forma.setNumArticulos(Integer.parseInt(forma.getArticulos("numRegistros").toString()));
			
			//Se asignan los pedidos
			forma.setPedidos((HashMap)resultado.get(1));
			forma.setNumPedidos(Integer.parseInt(forma.getPedidos("numRegistros").toString()));
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalle");
		}		
		forma.setSinPedidos(true);
		return mapping.findForward("listadoPeticiones");
	}

	/**
	 * M�todo que realiza la redireccion del listado de peticiones
	 * @param con
	 * @param forma
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, DespachoPedidoQxForm forma, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    UtilidadBD.cerrarConexion(con);
			response.sendRedirect(forma.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de DespachoPedidoQxAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en DespachoPedidoQxAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * M�todo que realiza la ordenacion de peticiones 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, DespachoPedidoQxForm forma, ActionMapping mapping) 
	{

		String[] indices = (String[])forma.getPeticiones("INDICES_MAPA");
		
		forma.setPeticiones(Listado.ordenarMapa(indices,
				forma.getIndice(),
				forma.getUltimoIndice(),
				forma.getPeticiones(),
				forma.getNumPeticiones()));
		
		forma.setPeticiones("numRegistros",forma.getNumPeticiones()+"");
		forma.setPeticiones("INDICES_MAPA",indices);
		
		forma.setUltimoIndice(forma.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoPeticiones");
	}

	/**
	 * M�todo implementado para iniciar el flujo de la opcion por Paciente del despacho de pedidos quir�rgicos
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param mapping
	 * @param request 
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionEmpezarPaciente(Connection con, DespachoPedidoQxForm forma, PersonaBasica paciente, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		ActionErrors errores = new ActionErrors();
		
		//***********VALIDACIONES*****************************************************************
		//1) Se verifica que el paciente s ehaya cargado en sesi�n
		if(paciente == null || paciente.getCodigoPersona()<=0)
			errores.add("",new ActionMessage("errors.paciente.noCargado"));
		//*********************************************************************************************
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("opciones");
		}
		forma.setCodigoCentroCosto(ConstantesBD.codigoNuncaValido);
		
		//Se cargan las peticiones 
		forma.setPeticiones(DespachoPedidoQx.consultarPeticiones(con, paciente.getCodigoPersona()+"", "", usuario.getCodigoCentroCosto()+""));
		forma.setNumPeticiones(Integer.parseInt(forma.getPeticiones("numRegistros").toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoPeticiones");
	}

	/**
	 * M�todo que inicia el flujo del despacho de pedido Qx mostrando la opcion por paciente / por Area
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario 
	 * @param request 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, DespachoPedidoQxForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		forma.reset();
		UtilidadBD.closeConnection(con);
		
		//****SE VALIDA AUTORIZACION ALMACEN************************************
		if(!UtilidadInventarios.esAlmacenUsuarioAutorizado(usuario.getLoginUsuario(),usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt()))
		{
			ActionErrors errores = new ActionErrors(); 
           errores.add("usuario no autorizado", new ActionMessage("error.inventarios.usuarioNoAutorizado",usuario.getCentroCosto()));
           saveErrors(request, errores);
                      
           return mapping.findForward("paginaErroresActionErrors");
		}
		
		forma.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		
		return mapping.findForward("opciones");
	}
}
