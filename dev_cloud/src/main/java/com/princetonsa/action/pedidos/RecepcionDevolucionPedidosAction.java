/*
 * Created on 27-sep-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.action.pedidos;

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

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.pedidos.RecepcionDevolucionPedidosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.interfaz.DtoInterfazTransaccionAxInv;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.ConsecutivosDisponibles;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.MotDevolucionInventarios;
import com.princetonsa.mundo.inventarios.RegistroTransacciones;
import com.princetonsa.mundo.pedidos.RecepcionDevolucionPedidos;
import com.princetonsa.pdf.RecepcionDevolucionPedidosPdf;


/**
 * @author armando
 *
 * Princeton 27-sep-2004
 */
public class RecepcionDevolucionPedidosAction extends Action 
{
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger loger = Logger.getLogger(RecepcionDevolucionPedidosAction.class);
	
	public ActionForward execute(ActionMapping mapping,
			 ActionForm form,
			 HttpServletRequest request,
			 HttpServletResponse response)
	throws Exception
	{

		Connection con=null;
		try{
			if (form instanceof RecepcionDevolucionPedidosForm)
			{
				ActionErrors  alertas = new ActionErrors();
				ArrayList<ActionErrors> arrayErrores = new ArrayList<ActionErrors>();
				RecepcionDevolucionPedidosForm forma=(RecepcionDevolucionPedidosForm)form;
				RecepcionDevolucionPedidos mundo=new RecepcionDevolucionPedidos();
				String estado=forma.getEstado();

				try
				{

					String tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
					HttpSession session=request.getSession();
					UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
					loger.warn("Estado [RecepcionDevolucionPedidosAction]  -->" + estado);
					forma.setInterfazCompras(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazCompras(usuario.getCodigoInstitucionInt())));

					if(estado.equals("empezar"))
					{
						ActionErrors errores = new ActionErrors(); 
						//****SE VALIDA AUTORIZACION ALMACEN************************************
						if(!UtilidadInventarios.esAlmacenUsuarioAutorizado(usuario.getLoginUsuario(),usuario.getCodigoCentroCosto(),usuario.getCodigoInstitucionInt()))
							errores.add("usuario no autorizado", new ActionMessage("error.inventarios.usuarioNoAutorizado",usuario.getCentroCosto()));
						//***SE VALIDA CONSECUTIVO DE REGISTRO TRANSACCIONES****************************** 
						if(!Utilidades.isDefinidoConsecutivo(con,usuario.getCodigoInstitucionInt(),ConstantesBD.nombreConsecutivoAjusteCostoInv)) 
							errores.add("NO HAY CONSECUTIVO DEFINIDO", new ActionMessage("error.inventarios.faltaDefinirConsecutivoAjusteCostoInv"));
						//******************************************************************************

						if(!errores.isEmpty())
						{
							saveErrors(request, errores);
							this.cerrarConexion(con);
							return mapping.findForward("paginaErroresActionErrors");
						}

						forma.reset(usuario.getCodigoInstitucionInt());
						mundo.reset();
						forma.setColeccion (mundo.consultarDevoluciones(con,usuario.getCodigoCentroCosto(),usuario.getCodigoInstitucionInt()));
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("listadoDevoluciones");
					}
					else if(estado.equals("detalle"))
					{	

						//Se verifica si la devolucion es quirurgica o no para saber que campos consultar
						if(UtilidadTexto.getBoolean(forma.getEsQuirurgico()))
							forma.setColeccionDetalle(mundo.consultarUnaDevolucionQx(con,forma.getNumeroDevolucion()));
						else
							forma.setColeccionDetalle(mundo.consultarUnaDevolucion(con,forma.getNumeroDevolucion()));
						//****se consulta la descripcion del motivo de la devolucion*********
						MotDevolucionInventarios motivo = new MotDevolucionInventarios();
						forma.setMotivo(motivo.consultarDescripcion(con,forma.getMotivo(),usuario.getCodigoInstitucionInt()));
						//***************************************************************
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("detalleRecepcion");
					}
					//estado que se tiene para conservar los datos del hashmap cuando se utiliza el pager.
					else if(estado.equals("continuar"))
					{
						UtilidadBD.cerrarConexion(con);
						response.sendRedirect(forma.getDir());
					}
					else if(estado.equals("resumen"))
					{
						loger.info("en resumen");
						cargarResumen(con,mundo,forma,usuario.getCodigoInstitucionInt());
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("resumenRecepcion");

					}
					else if(estado.equals("guardar"))
					{
						//*****VALIDACION DE CIERRE DE INVENTARIOS*****************
						if(UtilidadInventarios.existeCierreInventarioParaFecha(UtilidadFecha.getFechaActual(),usuario.getCodigoInstitucionInt()))
						{
							//si existe cierre hay error!!!
							ActionErrors errores = new ActionErrors();
							errores.add("Existe cierre de inventarios para la fecha", new ActionMessage("error.inventarios.existeCierreInventarios",UtilidadFecha.getFechaActual()));
							saveErrors(request, errores);
							UtilidadBD.cerrarConexion(con);
							return mapping.findForward("detalleRecepcion");
						}
						//*********************************************************+

						/**Atributos para los mensajes de alerta**/
						String mensajesStockMinimo = "";
						String mensajesStockMaximo = "";
						String mensajesPuntoPedido = "";

						double nuevoCosto = 0; //variable que almacena el nuevo costo
						double costoUnitario = 0; //variable que almacena el costo unitario
						myFactory.beginTransaction(con);
						llenarMaestroMundo(forma,mundo,usuario);
						if(mundo.insertarRecepcionMaestro(con)>=0)
						{
							if(mundo.actualizarEstadoDevolucion(con,ConstantesBD.codigoEstadoDevolucionRecibida)>=0)
							{
								forma.setEstadoDevolucion("Recibida");
								int i;

								for(i=0;i<forma.getColeccionDetalle().size();i++)
								{
									llenarDetalleMundo(forma,mundo,i);

									///////////////////////////////////
									if(forma.isInterfazCompras()&&forma.getArticulos().containsKey("tiporecepcion_"+i)&&!UtilidadTexto.isEmpty(forma.getArticulos("tiporecepcion_"+i)+""))
									{
										ActionErrors errores = new ActionErrors();

										arrayErrores=this.accionInterfazCompras(con,errores,mundo,forma,usuario,i);

										errores =arrayErrores.get(0);
										if (arrayErrores.get(1)!=null)
											alertas = arrayErrores.get(1);

										//si hay un error de validacion no se genera el despacho
										// y se muestran los errores.
										if(!errores.isEmpty())
										{
											saveErrors(request,errores);
											UtilidadBD.abortarTransaccion(con);
											this.cerrarConexion(con);
											return mapping.findForward("detalleRecepcion");
										}
									}

									//se consulta el costo unitario del articulo
									mundo.setCosto(mundo.obtenerCostoUnitarioDespacho(con));

									//se asigna el costo unitario
									costoUnitario = mundo.getCosto();

									//**SE CALCULA EL NUEVO COSTO*****************
									nuevoCosto = UtilidadInventarios.calcularCostoPromedioArticulo(
											mundo.getArticulo(),
											mundo.getCantidadRecibida(),
											mundo.getCosto()*mundo.getCantidadRecibida(),
											ConstantesBD.codigoTipoConceptoEntradaInv,
											usuario.getCodigoInstitucionInt(),
											forma.getCodFarmacia());
									//*******************************************

									//se actualiza el nuevo costo promedio
									if(UtilidadInventarios.actualizarCostoPromedioArticulo(con,mundo.getArticulo(),nuevoCosto))
									{
										//se actualizan las existencias del articulo
										boolean exitoExistencias=false;
										if(Articulo.articuloManejaLote(con, mundo.getArticulo(), usuario.getCodigoInstitucionInt()))
											exitoExistencias=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,mundo.getArticulo(),usuario.getCodigoCentroCosto(),true,mundo.getCantidadRecibida(),usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,mundo.getLote(),UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaVencimiento()));
										else
											exitoExistencias=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,mundo.getArticulo(),usuario.getCodigoCentroCosto(),true,mundo.getCantidadRecibida(),usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);

										if(exitoExistencias)
										{

											//se inserta el detalle de la recepcion de la devolucion
											if(mundo.insertarRecepcionDetalle(con)<=0)
												i=forma.getColeccionDetalle().size()+1;
											else
											{
												//se actualiza costo unitario de la devolucion
												if(mundo.actualizarCostoUnitarioDevolucion(con)<=0)
													i=forma.getColeccionDetalle().size()+1;
												else
												{
													//********REGISTRO TRANSACCION********************************
													RegistroTransacciones registroTran = new RegistroTransacciones();

													boolean exitoRegitroTrans = registroTran.generarRegistroAjusteTrans(
															con,
															mundo.getArticulo(),
															usuario.getCodigoCentroCosto(),
															ConstantesBDInventarios.codigoTipoConceptoInventarioEntrada,
															forma.getNumeroDevolucion()+"",
															costoUnitario,
															nuevoCosto,
															ConstantesBDInventarios.codigoTipoAjusteInventarioManual,
															usuario.getCodigoInstitucionInt(),true);

													if(!exitoRegitroTrans)
														i=forma.getColeccionDetalle().size()+1;
													//************************************************************
												}
											}
											if(forma.getEsQuirurgico().equals(ConstantesBD.acronimoSi))
											{
												//ACTUALIZAMOS LA CANTIDAD DEVUELTA POR LA CANTIDAD A LA CUAL SE LE HIZO RECEPCION
												mundo.actualizarDevolucion(con);
											}
										}
										else
											i=forma.getColeccionDetalle().size()+1;
									}
								}
								if(i==forma.getColeccionDetalle().size())
								{
									myFactory.endTransaction(con);
									forma.setEstado("resumen");
									//Realiza la consulta para mostrar en el resumen.
									if(cargarResumen(con,mundo,forma,usuario.getCodigoInstitucionInt()))
									{

										for(int j=0;j<forma.getColeccionDetalle().size();j++)
										{
											llenarDetalleMundo(forma,mundo,j);
											//******VERIFICACIONES STOCK MÍNIMO, MÁXIMO Y PUNTO PEDIDO *************
											//STOCK MÍNIMO
											if(!UtilidadInventarios.existenciasArticuloMayorIgualStockMinimo(
													mundo.getArticulo(),usuario.getCodigoInstitucionInt()))
											{
												if(!mensajesStockMinimo.equals(""))
													mensajesStockMinimo+=", ";
												mensajesStockMinimo += mundo.getDescripcionArticulo();
											}
											//STOCK MAXIMO
											if(!UtilidadInventarios.existenciasArticuloMenorIgualStockMaximo(
													mundo.getArticulo(),usuario.getCodigoInstitucionInt()))
											{
												if(!mensajesStockMaximo.equals(""))
													mensajesStockMaximo += ", ";
												mensajesStockMaximo += mundo.getDescripcionArticulo();
											}
											//PUNTO PEDIDO
											if(!UtilidadInventarios.existenciasArticuloMayorIgualPuntoPedido(
													mundo.getArticulo(),usuario.getCodigoInstitucionInt()))
											{
												if(!mensajesPuntoPedido.equals(""))
													mensajesPuntoPedido += ", ";
												mensajesPuntoPedido += mundo.getDescripcionArticulo();
											}
											//************************************************************************
										}
										//***SE REVISA SI HAY MENSAJES DE ALERTA*******
										ActionErrors errores = new ActionErrors();
										if(!mensajesStockMinimo.equals(""))
											errores.add("cantidades menores al stock mínimo",
													new ActionMessage("error.inventarios.quedanConCantidad",mensajesStockMinimo,"MENOR","STOCK MÍNIMO"));

										if(!mensajesStockMaximo.equals(""))
											errores.add("cantidades mayores al stock máximo",
													new ActionMessage("error.inventarios.quedanConCantidad",mensajesStockMaximo,"MAYOR","STOCK MÁXIMO"));

										if(!mensajesPuntoPedido.equals(""))
											errores.add("cantidades menores al punto pedido",
													new ActionMessage("error.inventarios.quedanConCantidad",mensajesPuntoPedido,"MENOR","PUNTO DE PEDIDO"));


										if (alertas.size()>0)
										{
											Iterator it=alertas.get();
											while(it.hasNext())
											{
												errores.add("alerta", (ActionMessage)it.next());
											}
										}
										saveErrors(request,errores);
										//*********************************************

										UtilidadBD.cerrarConexion(con);
										return mapping.findForward("resumenRecepcion");
									}
									else
									{
										UtilidadBD.cerrarConexion(con);
										return ComunAction.accionSalirCasoError(mapping, request, con, loger, "Error en la consulta", "Otro usuario acaba de realizar la recepción devolución del pedido.", false);
									}
								}
							}
						}
						myFactory.abortTransaction(con);
						UtilidadBD.cerrarConexion(con);
						return ComunAction.accionSalirCasoError(mapping, request, con, loger, "Error en la inserccion", "Otro usuario acaba de realizar la recepción devolución del pedido.", false);
					}
					else if(estado.equals("ordenar"))
					{
						this.accionOrdenar(forma);
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("listadoDevoluciones");
					}
					else if(estado.equals("imprimir"))
					{
						llenarMundo(mundo,forma,usuario);
						String nombreArchivo;
						Random r=new Random();
						nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
						RecepcionDevolucionPedidosPdf.pdfRecepcionDevolucionPedidos(ValoresPorDefecto.getFilePath() + nombreArchivo, mundo, usuario, request);
						UtilidadBD.cerrarConexion(con);
						request.setAttribute("nombreArchivo", nombreArchivo);
						request.setAttribute("nombreVentana", "Consulta Diagnósticos");
						return mapping.findForward("abrirPdf");
					}
					//MT 5498 Cantidad devolucion paginacion
					else if(estado.equals("faltan"))
					{
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("detalleRecepcion");
					}
					else
					{
						UtilidadBD.cerrarConexion(con);
						return null;
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
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
	 * 
	 * @param con
	 * @param errores
	 * @param mundo
	 * @param forma
	 * @param i 
	 * @param usuario 
	 * @return
	 */
	private ArrayList<ActionErrors> accionInterfazCompras(Connection con, ActionErrors errores, RecepcionDevolucionPedidos mundo, RecepcionDevolucionPedidosForm forma, UsuarioBasico usuario, int i) throws SQLException  
	{
		ArrayList<ActionErrors> array = new ArrayList<ActionErrors>();
		ActionErrors alerta = new ActionErrors();
		boolean errorValidacion = false;
		String ingreso="";
		String CodPaciente="";
		
		if(UtilidadTexto.getBoolean(forma.getEsQuirurgico()))
        {
        	HashMap ingresoYPersona = Utilidades.consultarIngresoYpersonadeunPedidoQx(con, Utilidades.convertirAEntero(forma.getArticulos().get("pedido_"+i)+""));
		    
			int numDatosPaciente = Utilidades.convertirAEntero(ingresoYPersona.get("numRegistros").toString());
			
			if(numDatosPaciente>0)
			{
				ingreso = ingresoYPersona.get("consecutivo_0").toString();
				CodPaciente= ingresoYPersona.get("codigo_paciente_0").toString();
			}
			else
			{
				errores.add("Pedido QX sin Paciente Asignado", new ActionMessage("errors.required","El Pedido Quirurgico no tiene un Paciente Asociado"));
	            errorValidacion = true;	
			}
        }
	
		
		
    	if((forma.getArticulos("tiporecepcion_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDevolucionConsignacion))
    	{
    		//double valorUnitario=UtilidadInventarios.costoActualArticulo(mundo.getArticulo());
    		double valorUnitario=UtilidadInventarios.obtenerValorArticuloProveedorConveProveedor(con, forma.getArticulos().get("proveedorCompra_"+i)+"",mundo.getArticulo());
    		
    		boolean transaccionExitosa=false;
    		
        	String tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
        	int tipoTransaccion1=UtilidadInventarios.obtenerTipoTransaccionInterfaz(con,ConstantesBD.codigoTransaccionSalidaDevolucionCompraConsignacion,usuario.getCodigoInstitucionInt());
            ConsecutivosDisponibles consec=new ConsecutivosDisponibles();
            String consecutivo="";
            
            
            int codigoAlmacen=usuario.getCodigoCentroCosto(); 
            int codigoAlmacenConsecutivo=0;
            
            if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosUnicoSistema))
            	codigoAlmacenConsecutivo=ConstantesBD.codigoNuncaValido;        
            else if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen))
            	codigoAlmacenConsecutivo=forma.getCodArea(); 

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

	            transaccionExitosa=UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion1,mundo.getArticulo(),mundo.getCantidadRecibida(),valorUnitario+"",mundo.getLote(),UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaVencimiento()),forma.getArticulos().get("proveedorCompra_"+i)+"",forma.getArticulos("proveedorCatalogo_"+i)+"");
	            transaccionExitosa=UtilidadInventarios.generarRegistroCierreTransaccion(con,codTransaccion1+"",usuario.getLoginUsuario(),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
	            if(Articulo.articuloManejaLote(con, mundo.getArticulo(), usuario.getCodigoInstitucionInt()))
			    	transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,mundo.getArticulo(),codigoAlmacen,false,mundo.getCantidadRecibida(),usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,mundo.getLote(),UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaVencimiento()));        		
	        	else
	        		transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,mundo.getArticulo(),codigoAlmacen,false,mundo.getCantidadRecibida(),usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
			    
	            
	            
	            
	            
	            
	            
	        	tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
	        	
	        	
	        	
	        	
	        	int tipoTransaccion2=UtilidadInventarios.obtenerTipoTransaccionInterfaz(con,ConstantesBD.codigoTransaccionEtradaDevolucionComprasConsignacion,usuario.getCodigoInstitucionInt());
	            
	        	consecutivo="";
	            codigoAlmacen=Utilidades.convertirAEntero(forma.getArticulos("almacenConsignacion_"+i)+""); 
	            codigoAlmacenConsecutivo=0;
	            
	            if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosUnicoSistema))
	            	codigoAlmacenConsecutivo=ConstantesBD.codigoNuncaValido;        
	            else if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen))
	            	codigoAlmacenConsecutivo=Utilidades.convertirAEntero(forma.getArticulos("almacenConsignacion_"+i)+""); 
	            
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

		            transaccionExitosa=UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion2,mundo.getArticulo(),mundo.getCantidadRecibida(),valorUnitario+"",mundo.getLote(),UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaVencimiento()),forma.getArticulos().get("proveedorCompra_"+i)+"",forma.getArticulos("proveedorCatalogo_"+i)+"");
		            transaccionExitosa=UtilidadInventarios.generarRegistroCierreTransaccion(con,codTransaccion2+"",usuario.getLoginUsuario(),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
		            if(Articulo.articuloManejaLote(con, mundo.getArticulo(), usuario.getCodigoInstitucionInt()))
				    	transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,mundo.getArticulo(),codigoAlmacen,true,mundo.getCantidadRecibida(),usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,mundo.getLote(),UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaVencimiento()));        		
		        	else
		        		transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,mundo.getArticulo(),codigoAlmacen,true,mundo.getCantidadRecibida(),usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
				    
		            
		            ResultadoInteger resultadoInteger=generarRegistroTransaccionInterfazConsignacion(con,
							usuario.getCodigoInstitucionInt(),
							mundo.getArticulo(),
							codTransaccion1,
							codTransaccion2,
							tipoTransaccion1,
							tipoTransaccion2,
							ConstantesBD.codigoTransaccionSalidaDevolucionCompraConsignacion,
							ConstantesBD.codigoTransaccionEtradaDevolucionComprasConsignacion,
							mundo.getCantidadRecibida(),
							valorUnitario,
							UtilidadInventarios.obtenerValorIvaArticuloProveedorConveProveedor(con,forma.getArticulos().get("proveedorCompra_"+i)+"",mundo.getArticulo()),
							codigoAlmacen, codigoAlmacen, Utilidades.convertirAEntero(forma.getArticulos("almacenConsignacion_"+i)+""),
							usuario.getLoginUsuario(),
							forma.getArticulos().get("proveedorCompra_"+i)+"",
							CodPaciente, ingreso);
		            
		            alerta.add("alerta", new ActionMessage("prompt.generico",resultadoInteger.getDescripcion()));

	            }
            }
    	}
    	else if((forma.getArticulos("tiporecepcion_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDevolucionCompraProveedor))
    	{
    		//double valorUnitario=UtilidadInventarios.costoActualArticulo(mundo.getArticulo());
    		double valorUnitario=UtilidadInventarios.obtenerValorArticuloProveedorCatalogoProveedor(con,forma.getArticulos().get("proveedorCatalogo_"+i)+"",mundo.getArticulo());
    		
    		boolean transaccionExitosa=false;
    		
        	String tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
        	int tipoTransaccion=UtilidadInventarios.obtenerTipoTransaccionInterfaz(con,ConstantesBD.codigoTransaccionSalidaDevolucionComprasProveedor,usuario.getCodigoInstitucionInt());
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

	            transaccionExitosa=UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion,mundo.getArticulo(),mundo.getCantidadRecibida(),valorUnitario+"",mundo.getLote(),UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaVencimiento()),forma.getArticulos().get("proveedorCompra_"+i)+"",forma.getArticulos("proveedorCatalogo_"+i)+"");
	            transaccionExitosa=UtilidadInventarios.generarRegistroCierreTransaccion(con,codTransaccion+"",usuario.getLoginUsuario(),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
	            if(Articulo.articuloManejaLote(con, mundo.getArticulo(), usuario.getCodigoInstitucionInt()))
			    	transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,mundo.getArticulo(),codigoAlmacen,false,mundo.getCantidadRecibida(),usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,mundo.getLote(),UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaVencimiento()));        		
	        	else
	        		transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,mundo.getArticulo(),codigoAlmacen,false,mundo.getCantidadRecibida(),usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
			    
	            generarRegistroTransaccionInterfazCompraProveedor(con,
						usuario.getCodigoInstitucionInt(),
						mundo.getArticulo(),
						codTransaccion,
						tipoTransaccion,
						ConstantesBD.codigoTransaccionSalidaDevolucionComprasProveedor,
						mundo.getCantidadRecibida(),
						valorUnitario,
						UtilidadInventarios.obtenerValorIvaArticuloProveedorCatalogoProveedor(con,forma.getArticulos().get("proveedorCatalogo_"+i)+"",mundo.getArticulo()),
						codigoAlmacen, codigoAlmacen, usuario.getLoginUsuario(),
						forma.getArticulos("proveedorCatalogo_"+i)+"",
						CodPaciente, ingreso);

            }
    	}
    	array.add(0, errores);
    	if (alerta.size()>0)
    		array.add(1, alerta);
    	
    	return array;
	}

	/**
	 * Metodo para llenar el mundo completo, los articulos se cargan en una coleccion.
	 * @param mundo
	 * @param forma
	 * @param usuario
	 */
	private void llenarMundo(RecepcionDevolucionPedidos mundo, RecepcionDevolucionPedidosForm forma, UsuarioBasico usuario) 
	{
		this.llenarMaestroMundo(forma,mundo,usuario);
		
		mundo.setColeccionDetalle(forma.getColeccionDetalle());
	}

	/**
	 * Procedimiento que ordena los datos del resumen segun la columna seleccionada
	 * @param forma, Forma que se ordenara.
	 */
	private void accionOrdenar(RecepcionDevolucionPedidosForm forma)
	{
		try
        {
            forma.setColeccion(Listado.ordenarColumna(new ArrayList(forma.getColeccion()), forma.getUltimaPropiedad(),forma.getColumna()));
            forma.setUltimaPropiedad(forma.getColumna());
        }
        catch (Exception e)
        {
            loger.warn("Error en el listado de documentos");
            e.printStackTrace();
        }	
	}

	/**
	 * Metodo para realizar la consulta de los datos recien insertados y mostrarlos nuevamente
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param institucion
	 * 
	 */
	private boolean  cargarResumen(Connection con, RecepcionDevolucionPedidos mundo, RecepcionDevolucionPedidosForm forma, int institucion) 
	{
		ResultSetDecorator rs;
		//se instancia objeto de motivos de devolucion
		MotDevolucionInventarios motivo = new MotDevolucionInventarios();
		rs=mundo.consultarMaestroRecepcion(con);
		try {
			if(rs.next())
			{
				forma.setArea(rs.getString("area"));
				String[] fecha=rs.getString("fechaHoraDevolucion").split(" ");
				forma.setFechaRecepcion(fecha[0]);
				forma.setHoraRecepcion(fecha[1]);
				forma.setUsuarioRecibe(rs.getString("usuario"));
				forma.setMotivo(motivo.consultarDescripcion(con,rs.getString("motivo"),institucion));
				forma.setEsQuirurgico(rs.getString("es_qx"));
				if(UtilidadTexto.getBoolean(forma.getEsQuirurgico()))
					forma.setColeccionDetalle(mundo.consultarDetalleRecepcionQx(con));
				else
					forma.setColeccionDetalle(mundo.consultarDetalleRecepcion(con));
				
			}
			else
			{
				return false;
			}
		} catch (SQLException e) {
			loger.warn("Se produjo error consultando los datos para el resumen de la insercion"+e);
			return false;
		}
		return true;
	}

	/**
	 * Metodo para llenar el objeto maestro de la recepcion de devolucion de pedidos
	 * @param forma, forma que contiene los datos
	 * @param mundo. objero recepcion devolucion de pedidos
	 * @param usuario, Usuario que esta manipulando el sistema. 
	 */
	private void llenarMaestroMundo(RecepcionDevolucionPedidosForm forma, RecepcionDevolucionPedidos mundo, UsuarioBasico usuario)  
	{
		
		mundo.setNumeroDevolucion(forma.getNumeroDevolucion());
		mundo.setAreaNombre(forma.getArea());
		mundo.setFechaRecepcion(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mundo.setHoraRecepcion(UtilidadFecha.getHoraActual());
		mundo.setUsuarioRecibe(usuario.getLoginUsuario());
		mundo.setMotivo(forma.getMotivo());
		mundo.setHoraDevolucion(forma.getHoraDevolucion());
		mundo.setFechaDevolucion(forma.getFechaDevolucion());
		mundo.setEstadoDevolucion(forma.getEstadoDevolucion());
		mundo.setUsuarioDevolucion(forma.getUsuario());
		mundo.setFarmacia(forma.getCodFarmacia());
		mundo.setNombreFarmacia(forma.getFarmacia());
		mundo.setCentroAtencion(forma.getCentroAtencion());
		mundo.setEsQuirurgico(forma.getEsQuirurgico());
		
		
	}

	/**
	 * Metodo para llenar el detalle mundo con los datos de la forma
	 * @param forma, forma con la que se esta trabajando
	 * @param mundo. objeto de la recepcion
	 * @param i,<b>int</b>, variable que me maneja el indice del HashMap
	 */
	private void llenarDetalleMundo(RecepcionDevolucionPedidosForm forma, RecepcionDevolucionPedidos mundo,int i) 
	{
		mundo.setCodigo(Integer.parseInt(forma.getArticulos("codigo_"+i)+""));
		mundo.setCantidadRecibida(Integer.parseInt(forma.getArticulos("recibidos_"+i)+""));
		mundo.setArticulo(Integer.parseInt(forma.getArticulos("articulo_"+i)+""));
		mundo.setPedido(Integer.parseInt(forma.getArticulos("pedido_"+i)+""));
		mundo.setDescripcionArticulo(forma.getArticulos("descripcionArticulo_"+i)+"");
		mundo.setLote(forma.getArticulos("lote_"+i)+"");
		mundo.setFechaVencimiento(forma.getArticulos("fechavencimiento_"+i)+"");
		mundo.setTipoRecepcion(forma.getArticulos("tiporecepcion_"+i)+"");
		mundo.setAlmacenConsignacion(forma.getArticulos("almacenConsignacion_"+i)+"");
		mundo.setProveedorCompra(forma.getArticulos("proveedorCompra_"+i)+"");
		mundo.setProveedorCatalogo(forma.getArticulos("proveedorCatalogo_"+i)+"");
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
	 * @param proveedor 
	 * @param ingreso 
	 * @param codPaciente 
	 */
	private ResultadoInteger generarRegistroTransaccionInterfazCompraProveedor(Connection con,int institucion, int codigoArticulo, int codTransaccion, int tipoTransaccion, int tipoTransInterfaz, int cantidadDespacho, double valorUnitario,double valorIva, int codigoAlmacen, int centroCostoSolicita, String usuario, String proveedor, String codPaciente, String ingreso) 
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
		loger.info("VALOR TOTAL --->"+UtilidadTexto.formatearValores(valorUnitario+"","#.######")+"<-");
		loger.info("VALOR IVA   --->"+UtilidadTexto.formatearValores(valorIva+"","#.######")+"<-");
		double resultado = valorUnitario-valorIva; 
		loger.info("VALOR RESULTADO --->"+UtilidadTexto.formatearValores(resultado+"","#.######")+"<-");
		
		dto.setValorUnitario(UtilidadTexto.formatearValores(resultado+"","#.######"));
		dto.setValorIva(valorIva+"");
		dto.setEstadoRegistro("0");//estado no procesado
		dto.setFechaRegistro(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dto.setHoraRegistro(UtilidadFecha.getHoraActual());
		dto.setIdentificacionProveedor(proveedor.trim());
		
		dto.setCodigoPaciente(codPaciente);
		dto.setIngresoPaciente(ingreso);
		
		dto.setAlmacenInterfaz(codigoAlmacen+"");
		dto.setAlmacenSolicita(centroCostoSolicita+"");
		dto.setAlmacendespacha(codigoAlmacen+"");
		
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
	 * @param proveedor 
	 * @param ingreso 
	 * @param codPaciente 
	 */
	private ResultadoInteger generarRegistroTransaccionInterfazConsignacion(Connection con,int institucion, int codigoArticulo, int codTransaccion1, int codTransaccion2, int tipoTransaccion1, int tipoTransaccion2, int tipoTransInterfaz1, int tipoTransInterfaz2, int cantidadDespacho, double valorUnitario,double valorIva, int codigoAlmacen, int centroCostoSolicita, int codigoAlmacenConsignacion, String usuario, String proveedor, String codPaciente, String ingreso) 
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
		
		loger.info("VALOR TOTAL --->"+UtilidadTexto.formatearValores(valorUnitario+"","#.######")+"<-");
		loger.info("VALOR IVA   --->"+UtilidadTexto.formatearValores(valorIva+"","#.######")+"<-");
		double resultado = valorUnitario-valorIva; 
		loger.info("VALOR RESULTADO --->"+UtilidadTexto.formatearValores(resultado+"","#.######")+"<-");
		
		dto.setValorUnitario(UtilidadTexto.formatearValores(resultado+"","#.######"));
		dto.setValorIva(valorIva+"");
		dto.setEstadoRegistro("0");//estado no procesado
		dto.setFechaRegistro(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dto.setHoraRegistro(UtilidadFecha.getHoraActual());
		dto.setIdentificacionProveedor(proveedor.trim());
		
		
		dto.setCodigoPaciente(codPaciente);
		dto.setIngresoPaciente(ingreso);
		
		dto.setAlmacenInterfaz(codigoAlmacen+"");
		dto.setAlmacenSolicita(centroCostoSolicita+"");
		dto.setAlmacendespacha(codigoAlmacen+"");
		dto.setAlmacenConsignacion(codigoAlmacenConsignacion+"");
		
		dto.setUsuario(usuario);
		
		return utilInterfaz.insertarTransaccionInterfaz(dto,institucion,false);
		
		
	}

}

