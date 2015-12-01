/*
 * @(#)DespachoPedidosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.pedidos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

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

import com.princetonsa.actionform.pedidos.DespachoPedidosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.interfaz.DtoInterfazTransaccionAxInv;
import com.princetonsa.mundo.ConsecutivosDisponibles;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.inventarios.AlmacenParametros;
import com.princetonsa.mundo.pedidos.DespachoPedidos;
import com.princetonsa.pdf.DespachoPedidosPdf;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 *   Action, controla todas las opciones dentro del despacho de pedidos 
 * 	 incluyendo los posibles casos de error y los casos de flujo.
 * @version 1.0, Septiembre 30, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */
public class DespachoPedidosAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(DespachoPedidosAction.class);

	/**
	 * M�todo encargado de el flujo y control de la funcionalidad
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
			if(form instanceof DespachoPedidosForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				DespachoPedidosForm despachoForm =(DespachoPedidosForm)form;

				String estado=despachoForm.getEstado();
				logger.warn("EL ESTADO = "+estado);

				if(estado == null)
				{
					despachoForm.reset(usuario.getCodigoInstitucionInt());
					logger.warn("Estado no valido dentro del flujo de Despacho Pedidos (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				despachoForm.setInterfazCompras(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazCompras(usuario.getCodigoInstitucionInt())));
				if (estado.equals("listarPedidos"))
				{
					despachoForm.reset(usuario.getCodigoInstitucionInt());
					return this.accionListarPedidos(despachoForm, mapping, con, estado, usuario, request);
				}
				else if(estado.equals("ordenarPedidos"))
				{
					return this.accionOrdenarPedidos(despachoForm, usuario, mapping, request, con);
				}
				else if(estado.equals("detallePedido"))
				{
					return this.accionDetallePedido(despachoForm,mapping,request,usuario, con);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect(despachoForm.getLinkSiguiente());
					return null;
				}
				else if (estado.equals("salirGuardar"))
				{
					return this.accionSalirGuardar(despachoForm,mapping,request,usuario,con);
				}
				else if (estado.equals("resumen"))
				{
					return this.accionResumen(despachoForm,usuario,mapping,request,con);
				}
				else if(estado.equals("imprimir"))
				{
					String nombreArchivo;
					Random r=new Random();
					nombreArchivo="/Despacho_Pedidos_" + r.nextInt()  +".pdf";
					nombreArchivo=nombreArchivo.replace("-","");
					DespachoPedidosPdf.pdfDespachoPedidos(ValoresPorDefecto.getFilePath() + nombreArchivo,despachoForm,usuario, request);
					UtilidadBD.cerrarConexion(con);
					request.setAttribute("nombreArchivo", nombreArchivo);
					request.setAttribute("nombreVentana", "Administracion Medicamentos");
					return mapping.findForward("abrirPdf");
				}
				else
				{
					despachoForm.reset(usuario.getCodigoInstitucionInt());
					logger.warn("Estado no valido dentro del flujo de Despacho Pedidos (null) ");
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
	 * salir guardar.
	 * 
	 * @param despachoForm DespachoPedidosForm
	 * @param request HttpServletRequest para obtener 
	 * 					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * 
	 * @return ActionForward "resumenPedidos"
	 * @throws SQLException
	*/
	private ActionForward accionSalirGuardar		(DespachoPedidosForm despachoForm,
																		 ActionMapping mapping,
																		 HttpServletRequest request,
																		 UsuarioBasico usuario,
																		 Connection con) throws SQLException, IPSException
	{
		boolean errorValidacion = false;
		boolean existenciasNegativas = false;
		boolean exito = true;
		int cantidadDespacho = 0;
		int existencias = 0;
		String descripcion = "";
		int codigo = 0;
		ActionErrors errores = new ActionErrors(); 
		String mensajesStockMinimo = "";
		String mensajesStockMaximo = "";
		String mensajesPuntoPedido = "";
		
		//*****VALIDACION DE CIERRE DE INVENTARIOS*****************
        if(UtilidadInventarios.existeCierreInventarioParaFecha(UtilidadFecha.getFechaActual(),usuario.getCodigoInstitucionInt()))
        {
        	//si existe cierre hay error!!!
        	
           errores.add("Existe cierre de inventarios para la fecha", new ActionMessage("error.inventarios.existeCierreInventarios",UtilidadFecha.getFechaActual()));
           saveErrors(request, errores);
           errorValidacion = true;
           
        }
        //*********************************************************+
        
        //se inicia transaccion
        UtilidadBD.iniciarTransaccion(con);
        
        //****VALIDACIONES DE DESPACHO******************************
        //se toma el parametro que indica si se permiten existencias negativas
        existenciasNegativas = AlmacenParametros.manejaExistenciasNegativas(con, usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt());
        String codigosArticulos="";
        String valoresExistencias="";
        
        for(int i=0;i<despachoForm.getColSize();i++)
        {
        	//se toma el codigo del articulo
        	codigo = Integer.parseInt(despachoForm.getDespachoPedidosMap("codigosArt_"+i)+"");
        	//se toma la cantidad a despachar
        	cantidadDespacho = Integer.parseInt(despachoForm.getDespachoPedidosMap("cantidadADespachar_"+i)+"");
        	double valorUnitario=0;
        	
        	ArrayList filtro=new ArrayList();
	        filtro.add(codigo);
	        filtro.add(usuario.getCodigoCentroCosto());
	        UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearArticulosAlmacen,filtro);
	        
	        
	        
	        ///////////////////////////////////
	        if(despachoForm.isInterfazCompras()&&despachoForm.getDespachoPedidosMap().containsKey("tipodespacho_"+i)&&!UtilidadTexto.isEmpty(despachoForm.getDespachoPedidosMap("tipodespacho_"+i)+""))
	        {
	        	
	        	
	        	if((despachoForm.getDespachoPedidosMap("tipodespacho_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDespachoConsignacion))
	        	{
	        		valorUnitario=UtilidadInventarios.obtenerValorArticuloProveedorConveProveedor(con,despachoForm.despachoPedidosMap.get("proveedorCompra_"+i)+"",codigo);
	        		
	        		boolean transaccionExitosa=false;
	        		
		        	String tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
		        	int tipoTransaccion1=UtilidadInventarios.obtenerTipoTransaccionInterfaz(con,ConstantesBD.codigoTransaccionSalidaConsignacionConsumos,usuario.getCodigoInstitucionInt());
		            ConsecutivosDisponibles consec=new ConsecutivosDisponibles();
		            String consecutivo="";
		            
		            
		            int codigoAlmacen=Utilidades.convertirAEntero(despachoForm.getDespachoPedidosMap("almacenConsignacion_"+i)+""); 
		            int codigoAlmacenConsecutivo=0;
		            
		            if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosUnicoSistema))
		            	codigoAlmacenConsecutivo=ConstantesBD.codigoNuncaValido;        
		            else if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen))
		            	codigoAlmacenConsecutivo=Utilidades.convertirAEntero(despachoForm.getDespachoPedidosMap("almacenConsignacion_"+i)+""); 

		            //validacion consecutivo
		            consecutivo=consec.obtenerConsecutivoInventario(con,tipoTransaccion1,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt())+"";
		            
		            if(Integer.parseInt(consecutivo)==ConstantesBD.codigoNuncaValido)
		            {
		            	errores.add("falta consecutivo", new ActionMessage("error.inventarios.faltaDefinirConsecutivoTransAlmacen",UtilidadInventarios.obtenerNombreTipoTransaccion(con,tipoTransaccion1,usuario.getCodigoInstitucionInt()),Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())));
			            errorValidacion = true;		                      
		            }
		            else
		            {
			            ArrayList filtro1=new ArrayList();
			            filtro1.add(tipoTransaccion1+"");
			            filtro1.add(usuario.getCodigoInstitucionInt()+"");
			            filtro1.add(codigoAlmacenConsecutivo+"");
			            UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoConsecutivoInventariosAlmacen,filtro1);
	
			            consecutivo=consec.obtenerConsecutivoInventario(con,tipoTransaccion1,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt())+"";
			            
			            
			            int codTransaccion1=UtilidadInventarios.generarEncabezadoTransaccion(con,Integer.parseInt(consecutivo),tipoTransaccion1,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido,"",ConstantesBDInventarios.codigoEstadoTransaccionInventarioPendiente,codigoAlmacen,true);
					     
			            consec.actualizarValorConsecutivoInventarios(con,tipoTransaccion1,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt());
	
			            transaccionExitosa=UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion1,codigo,cantidadDespacho,valorUnitario+"",(despachoForm.getDespachoPedidosMap("lote_"+i)+""),UtilidadFecha.conversionFormatoFechaABD(despachoForm.getDespachoPedidosMap("fechavencimiento_"+i)+""),despachoForm.despachoPedidosMap.get("proveedorCompra_"+i)+"",despachoForm.despachoPedidosMap.get("proveedorCatalogo_"+i)+"");
			            transaccionExitosa=UtilidadInventarios.generarRegistroCierreTransaccion(con,codTransaccion1+"",usuario.getLoginUsuario(),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
					    if(UtilidadTexto.getBoolean(despachoForm.getDespachoPedidosMap("manejaLoteArticulo_"+i)+"") )
					    	transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,codigo,codigoAlmacen,false,cantidadDespacho,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,(despachoForm.getDespachoPedidosMap("lote_"+i)+""),UtilidadFecha.conversionFormatoFechaABD(despachoForm.getDespachoPedidosMap("fechavencimiento_"+i)+""));        		
			        	else
			        		transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,codigo,codigoAlmacen,false,cantidadDespacho,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
					    
					    
		        		
	
			        	tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
			        	int tipoTransaccion2=UtilidadInventarios.obtenerTipoTransaccionInterfaz(con,ConstantesBD.codigoTransaccionEntradaCompraConsignacion,usuario.getCodigoInstitucionInt());
			            
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
			            {
			            	errores.add("falta consecutivo", new ActionMessage("error.inventarios.faltaDefinirConsecutivoTransAlmacen",UtilidadInventarios.obtenerNombreTipoTransaccion(con,tipoTransaccion2,usuario.getCodigoInstitucionInt()),Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())));
				            errorValidacion = true;		                      
			            }
			            else
			            {
				            filtro1=new ArrayList();
				            filtro1.add(tipoTransaccion2+"");
				            filtro1.add(usuario.getCodigoInstitucionInt()+"");
				            filtro1.add(codigoAlmacenConsecutivo+"");
				            UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoConsecutivoInventariosAlmacen,filtro1);
		
				            consecutivo=consec.obtenerConsecutivoInventario(con,tipoTransaccion2,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt())+"";
				            
				            int codTransaccion2=UtilidadInventarios.generarEncabezadoTransaccion(con,Integer.parseInt(consecutivo),tipoTransaccion2,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido,"",ConstantesBDInventarios.codigoEstadoTransaccionInventarioPendiente,codigoAlmacen,true);
						     
		
				            consec.actualizarValorConsecutivoInventarios(con,tipoTransaccion2,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt());
		
				            transaccionExitosa=UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion2,codigo,cantidadDespacho,valorUnitario+"",(despachoForm.getDespachoPedidosMap("lote_"+i)+""),UtilidadFecha.conversionFormatoFechaABD(despachoForm.getDespachoPedidosMap("fechavencimiento_"+i)+""),despachoForm.despachoPedidosMap.get("proveedorCompra_"+i)+"",despachoForm.despachoPedidosMap.get("proveedorCatalogo_"+i)+"");
				            transaccionExitosa=UtilidadInventarios.generarRegistroCierreTransaccion(con,codTransaccion2+"",usuario.getLoginUsuario(),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
						    if(UtilidadTexto.getBoolean(despachoForm.getDespachoPedidosMap("manejaLoteArticulo_"+i)+"") )
						    	transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,codigo,codigoAlmacen,true,cantidadDespacho,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,(despachoForm.getDespachoPedidosMap("lote_"+i)+""),UtilidadFecha.conversionFormatoFechaABD(despachoForm.getDespachoPedidosMap("fechavencimiento_"+i)+""));        		
				        	else
				        		transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,codigo,codigoAlmacen,true,cantidadDespacho,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
						    
						    UtilidadInventarios.actualizarPrecioUltimaCompra(con,codigo, valorUnitario);
						    
						    generarRegistroTransaccionInterfazConsignacion(con,
						    													usuario.getCodigoInstitucionInt(),
						    													codigo,
						    													codTransaccion1,
						    													codTransaccion2,
						    													tipoTransaccion1,
						    													tipoTransaccion2,
						    													ConstantesBD.codigoTransaccionSalidaConsignacionConsumos,
						    													ConstantesBD.codigoTransaccionEntradaCompraConsignacion,
						    													cantidadDespacho,
						    													valorUnitario,
						    													UtilidadInventarios.obtenerValorIvaArticuloProveedorConveProveedor(con,despachoForm.despachoPedidosMap.get("proveedorCompra_"+i)+"",codigo),
						    													codigoAlmacen, despachoForm.getCentroCostoSolicitante(),
						    													Utilidades.convertirAEntero(despachoForm.getDespachoPedidosMap("almacenConsignacion_"+i)+""),
						    													usuario.getLoginUsuario());
						    
			            }
		            }
	        	}
	        	else if((despachoForm.getDespachoPedidosMap("tipodespacho_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDespachoCompraProveedor))
	        	{
	        		valorUnitario=UtilidadInventarios.obtenerValorArticuloProveedorCatalogoProveedor(con,despachoForm.despachoPedidosMap.get("proveedorCatalogo_"+i)+"",codigo);
	        		
	        		boolean transaccionExitosa=false;
	        		
		        	String tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
		        	int tipoTransaccion=UtilidadInventarios.obtenerTipoTransaccionInterfaz(con,ConstantesBD.codigoTransaccionEntradaComprasProveedor,usuario.getCodigoInstitucionInt());
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
		            {
		            	errores.add("falta consecutivo", new ActionMessage("error.inventarios.faltaDefinirConsecutivoTransAlmacen",UtilidadInventarios.obtenerNombreTipoTransaccion(con,tipoTransaccion,usuario.getCodigoInstitucionInt()),Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())));
			            errorValidacion = true;		                      
		            }
		            else
		            {
			            ArrayList filtro1=new ArrayList();
			            filtro1.add(tipoTransaccion+"");
			            filtro1.add(usuario.getCodigoInstitucionInt()+"");
			            filtro1.add(codigoAlmacenConsecutivo+"");
			            UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoConsecutivoInventariosAlmacen,filtro1);
	
			            consecutivo=consec.obtenerConsecutivoInventario(con,tipoTransaccion,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt())+"";
			            
			            int codTransaccion=UtilidadInventarios.generarEncabezadoTransaccion(con,Integer.parseInt(consecutivo),tipoTransaccion,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido,"",ConstantesBDInventarios.codigoEstadoTransaccionInventarioPendiente,codigoAlmacen,true);
					     
			            consec.actualizarValorConsecutivoInventarios(con,tipoTransaccion,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt());
	
			            transaccionExitosa=UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion,codigo,cantidadDespacho,valorUnitario+"",(despachoForm.getDespachoPedidosMap("lote_"+i)+""),UtilidadFecha.conversionFormatoFechaABD(despachoForm.getDespachoPedidosMap("fechavencimiento_"+i)+""),despachoForm.despachoPedidosMap.get("proveedorCompra_"+i)+"",despachoForm.despachoPedidosMap.get("proveedorCatalogo_"+i)+"");
			            transaccionExitosa=UtilidadInventarios.generarRegistroCierreTransaccion(con,codTransaccion+"",usuario.getLoginUsuario(),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
					    if(UtilidadTexto.getBoolean(despachoForm.getDespachoPedidosMap("manejaLoteArticulo_"+i)+"") )
					    	transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,codigo,codigoAlmacen,true,cantidadDespacho,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,(despachoForm.getDespachoPedidosMap("lote_"+i)+""),UtilidadFecha.conversionFormatoFechaABD(despachoForm.getDespachoPedidosMap("fechavencimiento_"+i)+""));        		
			        	else
			        		transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,codigo,codigoAlmacen,true,cantidadDespacho,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
					    
					    
					    UtilidadInventarios.actualizarPrecioUltimaCompra(con,codigo, valorUnitario);
					    
					    generarRegistroTransaccionInterfazCompraProveedor(con,
																			usuario.getCodigoInstitucionInt(),
																			codigo,
																			codTransaccion,
																			tipoTransaccion,
																			ConstantesBD.codigoTransaccionEntradaComprasProveedor,
																			cantidadDespacho,
																			valorUnitario,
																			UtilidadInventarios.obtenerValorIvaArticuloProveedorCatalogoProveedor(con,despachoForm.despachoPedidosMap.get("proveedorCompra_"+i)+"",codigo),
																			codigoAlmacen, despachoForm.getCentroCostoSolicitante(),
																			usuario.getLoginUsuario());
											

		            }
	        	}
	        }
	        
	        
        	//se toma las existencias del articulo
        	existencias = Integer.parseInt(UtilidadInventarios.existenciasArticuloAlmacen(codigo, usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt())+"");
        	if(UtilidadTexto.getBoolean(despachoForm.getDespachoPedidosMap("manejaLoteArticulo_"+i)+"") )
        		existencias = Integer.parseInt(UtilidadInventarios.existenciasArticuloAlmacenLote(codigo, usuario.getCodigoCentroCosto(),(despachoForm.getDespachoPedidosMap("lote_"+i)+""),UtilidadFecha.conversionFormatoFechaABD(despachoForm.getDespachoPedidosMap("fechavencimiento_"+i)+""))+"");
        		//Integer.parseInt(despachoForm.getDespachoPedidosMap("existencias_"+i)+"");
        	//se toma la descripcion del articulo
        	descripcion = despachoForm.getDespachoPedidosMap("descripcionArt_"+i)+"";
        	
        	
        	if(!existenciasNegativas)
        	{
	        	if(cantidadDespacho>existencias)
	    		{
		            errores.add("error.inventarios.existenciasInsuficientes", 
		                    new ActionMessage("error.inventarios.existenciasInsuficientes", 
		                    		descripcion, 
		                    		existencias+"", 
		                    		usuario.getCentroCosto() ));
		            despachoForm.setDespachoPedidosMap("existencias_"+i, existencias);
		            errorValidacion = true;
		    	}
	        	
        	}	
        	
        	//calcular las existencias
        	/*existenciasCalculadas = existencias - cantidadDespacho;
        	
        	/****VALIDACIONES EXISTENCIAS**********************
        	if(existenciasNegativas)
        	{
        		if(existenciasCalculadas<0)
        		{
	        		codigosArticulos+=codigo+",";
	        		valoresExistencias+=existencias+",";
        		}	
        	}
        	else
        	{
        		//se verifica si la cantidad del despacho
        		//excede las existencias
        		if(cantidadDespacho>existencias)
        		{
        			errores.add("La cantidad del despacho excede las existencias",
        				new ActionMessage("error.inventarios.articuloConExistenciaInferiorACantidad",
        					descripcion,
							existencias+"",
							despachoForm.getNombreAlmacen()));
        			errorValidacion = true;
        		}
        	}*/
        	//se actualizan existencias
        	if(UtilidadTexto.getBoolean(despachoForm.getDespachoPedidosMap("manejaLoteArticulo_"+i)+"") )
        		exito=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,codigo,usuario.getCodigoCentroCosto(),false,cantidadDespacho,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,(despachoForm.getDespachoPedidosMap("lote_"+i)+""),UtilidadFecha.conversionFormatoFechaABD(despachoForm.getDespachoPedidosMap("fechavencimiento_"+i)+""));        		
        	else
        		exito=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,codigo,usuario.getCodigoCentroCosto(),false,cantidadDespacho,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
            	
        	/****************************************************/
        	
        	/**OBTENCION DEL COSTO DEL ARTICULO***************/
        	despachoForm.setDespachoPedidosMap("costo_"+i,UtilidadInventarios.costoActualArticulo(codigo)+"");
        	/*****************************************************/
        }
        
        if(!codigosArticulos.trim().equals(""))
        {
			errores.add("Quedaron existencias negativas",
				new ActionMessage("error.inventarios.articuloQuedaConExistenciaNegativa",
					codigosArticulos,
					valoresExistencias));
		}
        
        //**********************************************************
        
        //si hay un error de validacion no se genera el despacho
        // y se muestran los errores.
        if(errorValidacion)
        {
        	saveErrors(request,errores);
        	UtilidadBD.abortarTransaccion(con);
	        this.cerrarConexion(con);
	        return mapping.findForward("paginaDetalle");
        }
		
		DespachoPedidos mundo=new DespachoPedidos();
		boolean validar = false;
		//si las transacciones de validaciones tuvieron exito
		//se continua con la inserción del despacho
		if(exito)
		{
			//AQUI SE FINALIZA LA TRANSACCION DEL DESPACHO ********************************************************************
			int resp0 = mundo.insertarDespacho(		con,
																			despachoForm.despachoPedidosMap, 
																			despachoForm.getColSize(), 
																			despachoForm.getNumeroPedido(), 
																			usuario.getLoginUsuario(),
																			ConstantesBD.continuarTransaccion,
																			"DespachoPedidosAction");
			if(resp0==(Integer.parseInt(IdentificadoresExcepcionesSql.codigoExcepcionSqlRegistroExistente)*-1))
			{
				errores.add("",new ActionMessage("errors.excepcionSQL.registroYaActualizado","DESPACHO DE PEDIDO"));
				saveErrors(request,errores);
	        	UtilidadBD.abortarTransaccion(con);
		        this.cerrarConexion(con);
		        return mapping.findForward("paginaDetalle");
			}
			else if(resp0>0)
			{
				CargosEntidadesSubcontratadas cargosEntidadesSubcontratadas = new CargosEntidadesSubcontratadas();
				String[] FechaHora = despachoForm.getFechaHoraPedido().split("-");
				try {logger.info("entro\n"+despachoForm.getInstitucion());
					cargosEntidadesSubcontratadas.generarCargoArticulo(con, Utilidades.convertirAEntero(despachoForm.getCodCentroCostoSolicitante()+""), Utilidades.convertirAEntero(despachoForm.despachoPedidosMap.get("codigosArt_"+0)+""), ConstantesBD.codigoNuncaValido, "", despachoForm.getNumeroPedido()+"", FechaHora[0], UtilidadFecha.convertirHoraACincoCaracteres(FechaHora[1]), false, usuario,"","");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				validar = true;
			}
			if(validar)
				UtilidadBD.finalizarTransaccion(con);
		}
		else
			UtilidadBD.abortarTransaccion(con);
		if (!validar)
		{
			logger.warn("Error en la transacción de Inserción en BD, con el número de pedido"+despachoForm.getNumeroPedido());
			this.cerrarConexion(con);
			despachoForm.reset(usuario.getCodigoInstitucionInt());
			ArrayList atributosError = new ArrayList();
			atributosError.add("Número de pedido");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");
		}
		else
		{
			//**************MENSAJES DE ALERTA*********************
			 for(int i=0;i<despachoForm.getColSize();i++)
		        {
				 	//se toma el codigo del articulo
		        	codigo = Integer.parseInt(despachoForm.getDespachoPedidosMap("codigosArt_"+i)+"");
		        	//se toma la descripcion del articulo
		        	descripcion = despachoForm.getDespachoPedidosMap("descripcionArt_"+i)+"";
		        	
			 		//STOCK MÍNIMO
		        	if(!UtilidadInventarios.existenciasArticuloMayorIgualStockMinimo(codigo,usuario.getCodigoInstitucionInt()))
		        	{
		        		if(!mensajesStockMinimo.equals(""))
		        			mensajesStockMinimo+=", ";
		        		mensajesStockMinimo += codigo;
		        	}
		        	//STOCK MAXIMO
		        	if(!UtilidadInventarios.existenciasArticuloMenorIgualStockMaximo(codigo,usuario.getCodigoInstitucionInt()))
		        	{
		        		if(!mensajesStockMaximo.equals(""))
		        			mensajesStockMaximo += ", ";
		        		mensajesStockMaximo += codigo;
		        	}
		        	//PUNTO PEDIDO
		        	if(!UtilidadInventarios.existenciasArticuloMayorIgualPuntoPedido(codigo,usuario.getCodigoInstitucionInt()))
		        	{
		        		if(!mensajesPuntoPedido.equals(""))
		        			mensajesPuntoPedido += ", ";
		        		mensajesPuntoPedido += codigo;
		        	}
		        }
			 //se revisa si hubo alertas
	        if(!mensajesStockMinimo.equals(""))
	        	errores.add("cantidades menores al stock mínimo",
	        		new ActionMessage("error.inventarios.quedanConCantidad",mensajesStockMinimo,"MENOR","STOCK MÍNIMO"));
	        
	        if(!mensajesStockMaximo.equals(""))
	        	errores.add("cantidades mayores al stock máximo",
	        		new ActionMessage("error.inventarios.quedanConCantidad",mensajesStockMaximo,"MAYOR","STOCK MÁXIMO"));
	        
	        if(!mensajesPuntoPedido.equals(""))
	        	errores.add("cantidades menores al punto pedido",
	        		new ActionMessage("error.inventarios.quedanConCantidad",mensajesPuntoPedido,"MENOR","PUNTO DE PEDIDO"));
				
			saveErrors(request,errores);
			//******************************************************
			return accionResumen(despachoForm,usuario,mapping,request, con);
		}
	}
	
	
	/**
	 * 
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
	 */
	private ResultadoInteger generarRegistroTransaccionInterfazCompraProveedor(Connection con,int institucion, int codigoArticulo, int codTransaccion, int tipoTransaccion, int tipoTransInterfaz, int cantidadDespacho, double valorUnitario,double valorIva, int codigoAlmacen, String centroCostoSolicita, String usuario) 
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
		dto.setValorUnitario((valorUnitario-valorIva)+"");
		dto.setValorIva(valorIva+"");
		dto.setEstadoRegistro("0");//estado no procesado
		dto.setFechaRegistro(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dto.setHoraRegistro(UtilidadFecha.getHoraActual());
		
		dto.setAlmacenInterfaz(codigoAlmacen+"");
		dto.setAlmacendespacha(codigoAlmacen+"");
		dto.setAlmacenSolicita(centroCostoSolicita);
		
		dto.setUsuario(usuario);
		
		return utilInterfaz.insertarTransaccionInterfaz(dto,institucion,false);
	}
	
	/**
	 * 
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
	 */
	private ResultadoInteger  generarRegistroTransaccionInterfazConsignacion(Connection con,int institucion, int codigoArticulo, int codTransaccion1, int codTransaccion2, int tipoTransaccion1, int tipoTransaccion2, int tipoTransInterfaz1, int tipoTransInterfaz2, int cantidadDespacho, double valorUnitario,double valorIva, int codigoAlmacen, String centroCostoSolicita, int codigoAlmacenConsignacion, String usuario) 
	{
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
		dto.setValorUnitario((valorUnitario-valorIva)+"");
		dto.setValorIva(valorIva+"");
		dto.setEstadoRegistro("0");//estado no procesado
		dto.setFechaRegistro(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dto.setHoraRegistro(UtilidadFecha.getHoraActual());
		
		dto.setAlmacenInterfaz(codigoAlmacen+"");
		dto.setAlmacendespacha(codigoAlmacen+"");
		dto.setAlmacenSolicita(centroCostoSolicita);
		dto.setAlmacenConsignacion(codigoAlmacenConsignacion+"");
		
		dto.setUsuario(usuario);
		
		return utilInterfaz.insertarTransaccionInterfaz(dto,institucion,false);
		
		
	}

	/**
	 * Envía el resumen según un número de pedido
	 * @param despachoForm
	 * @param usuario 
	 * @param mundo
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionResumen (	DespachoPedidosForm despachoForm,
																 UsuarioBasico usuario, ActionMapping mapping,
																 HttpServletRequest request,
																 Connection con) throws SQLException
	{
		DespachoPedidos mundo=new DespachoPedidos();
		mundo.setNumeroPedido(despachoForm.getNumeroPedido());
		despachoForm.reset(usuario.getCodigoInstitucionInt());
		boolean validarCargar= mundo.cargarDetallePedidoPart2(con,mundo.getNumeroPedido());
		if(validarCargar)
		{
		        llenarForm(despachoForm,mundo);
		        despachoForm.setCol(mundo.resumen(con,mundo.getNumeroPedido()));
				this.cerrarConexion(con);									
				return mapping.findForward("resumenDespacho");
		}
		else
		{
			  logger.warn("Número de pedido inválido "+despachoForm.getNumeroPedido());
			  this.cerrarConexion(con);
			  despachoForm.reset(usuario.getCodigoInstitucionInt());
			  ArrayList atributosError = new ArrayList();
			  atributosError.add("Número de solicitud");
			  request.setAttribute("codigoDescripcionError", "errors.invalid");				
			  request.setAttribute("atributosError", atributosError);
			  return mapping.findForward("paginaError");
		}
	}
	
	/**
	 * Este m�todo especifica las acciones a realizar en el estado
	 * detalle pedido
	 * @param despachoForm DespachoPedidosForm
	 * @param mapping Mapping para manejar la navegaci�n
	 * @param usuario
	 * @param con Conexi�n con la fuente de datos
	 * @return ActionForward  a la p�gina "detalleDespachoPedidos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionDetallePedido(		DespachoPedidosForm despachoForm, 
																		ActionMapping mapping,
																		HttpServletRequest request,
																		UsuarioBasico usuario, Connection con) throws SQLException 
	{
	    DespachoPedidos mundo= new DespachoPedidos();
	    
	    boolean validarCargar= mundo.cargarDetallePedidoPart2(con,despachoForm.getNumeroPedido());
	    
	    if(validarCargar)
	    {
	        llenarForm(despachoForm,mundo);
	        despachoForm.setCol(mundo.detallePedidoPart1(con,despachoForm.getNumeroPedido(),usuario.getCodigoCentroCosto(),usuario.getCodigoInstitucionInt()));
	        
	        /*valores por defecto del HashMap */
	        for(int i=0; i<despachoForm.getColSize();i++)
		    {
		       despachoForm.setDespachoPedidosMap("cantidadADespachar_"+i,"0");
		    }
	        
	        despachoForm.setNombreAlmacen(usuario.getCentroCosto());
	        
	        this.cerrarConexion(con);		
			return mapping.findForward("paginaDetalle");
	    }
	    else
	    {
	        logger.warn("Número de pedido inválido "+despachoForm.getNumeroPedido());
			this.cerrarConexion(con);
			despachoForm.reset(usuario.getCodigoInstitucionInt());
			ArrayList atributosError = new ArrayList();
			atributosError.add("Número de solicitud");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");
	    }
	}

	/**
	 * Este método carga los datos pertinentes a la forma 
	 * @param depachoForm (form)
	 * @param mundo
	 */
	protected void llenarForm(DespachoPedidosForm despachoForm, DespachoPedidos mundo)
	{	
		/*info del encabezado*/
		despachoForm.setFechaHoraPedido(mundo.getFechaHoraPedido());
		despachoForm.setFechaHoraGrabacion(mundo.getFechaHoraGrabacion());
		despachoForm.setNumeroPedido(mundo.getNumeroPedido());
		despachoForm.setUsuarioSolicitante(mundo.getUsuarioSolicitante());
		despachoForm.setObservacionesGenerales(mundo.getObservacionesGenerales());
		despachoForm.setCentroCostoSolicitante(mundo.getCentroCostoSolicitante());
		despachoForm.setCodCentroCostoSolicitante(mundo.getCodCentroCostoSolicitante());
		despachoForm.setIdentificadorPrioridad(mundo.getIdentificadorPrioridad());
		despachoForm.setEstadoPedido(mundo.getEstadoPedido());
		despachoForm.setFarmacia(mundo.getFarmacia());
		despachoForm.setFechaHoraDespacho(mundo.getFechaHoraDespacho());
	}	
	

    /**
	 * Este método especifica las acciones a realizar en el estado
	 * listar DespachoPedidos
	 * @param despachoForm DespachoPedidosForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
     * @param request
	 * @return ActionForward  a la página "listadoSolicitudesPedidos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarPedidos		(	DespachoPedidosForm despachoForm,
																			ActionMapping mapping,
																			Connection con,
																			String estado,
																			UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		//****SE VALIDA AUTORIZACION ALMACEN************************************
		if(!UtilidadInventarios.esAlmacenUsuarioAutorizado(usuario.getLoginUsuario(),usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt()))
		{
			ActionErrors errores = new ActionErrors(); 
           errores.add("usuario no autorizado", new ActionMessage("error.inventarios.usuarioNoAutorizado",usuario.getCentroCosto()));
           saveErrors(request, errores);
           UtilidadBD.cerrarConexion(con);           
           return mapping.findForward("paginaErroresActionErrors");
		}
		
		
		DespachoPedidos mundo= new DespachoPedidos();
		despachoForm.setEstado(estado);
		
		Collection listadoPedidos = mundo.listadoPedidos(con,usuario.getCodigoCentroCosto());
		this.cerrarConexion(con);
		
		if(listadoPedidos!=null && listadoPedidos.size()>0){
			despachoForm.setCol(listadoPedidos);
			despachoForm.setMostrarListaPedidos(true);			
		}else{
			despachoForm.setMostrarListaPedidos(false);
		}		
		return mapping.findForward("listarPedidos")	;	
	}	

	/**
	 * Método de ordenamiento del pager, según la columna que sea seleccionada. 
	 * @param despachoForm
	 * @param usuario 
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionOrdenarPedidos	(	DespachoPedidosForm despachoForm,
																			UsuarioBasico usuario, ActionMapping mapping,
																			HttpServletRequest request, 
																			Connection con) throws SQLException
	{
		try
		{
			despachoForm.setCol(Listado.ordenarColumna(new ArrayList(despachoForm.getCol()),despachoForm.getUltimaPropiedad(),despachoForm.getColumna()));
			despachoForm.setUltimaPropiedad(despachoForm.getColumna());
			this.cerrarConexion(con);
		}
		catch(Exception e)
		{
			logger.warn("Error en el listado de pedidos ");
			this.cerrarConexion(con);
			despachoForm.reset(usuario.getCodigoInstitucionInt());
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Listado Pedidos");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
		return mapping.findForward("listarPedidos")	;
	}

	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer
	 * un forward
	 * @param con Conexión con la fuente de datos
	 * @throws SQLException
	 */
	public void cerrarConexion (Connection con) throws SQLException
	{
			if (con!=null&&!con.isClosed())
			{
				UtilidadBD.closeConnection(con);
			}
	}

}
