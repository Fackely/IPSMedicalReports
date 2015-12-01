/*
 * Created on 11-oct-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.action.medicamentos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.UtilidadBDInventarios;
import util.inventarios.ConstantesBDInventarios;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.medicamentos.RecepcionDevolucionMedicamentosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.interfaz.DtoInterfazTransaccionAxInv;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.ConsecutivosDisponibles;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.medicamentos.RecepcionDevolucionMedicamentos;
import com.princetonsa.pdf.RecepcionDevolucionMedicamentosPdf;

/**
 * @author armando
 *
 * Princeton 11-oct-2004
 * Action para manejar el flujo de la funcionalidad recepcion
 * devolucion de medicamentos.
 */
public class RecepcionDevolucionMedicamentosAction extends Action 
{

	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger loger = Logger.getLogger(RecepcionDevolucionMedicamentos.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		Connection con = null;
		try {
			if (form instanceof RecepcionDevolucionMedicamentosForm)
			{
				RecepcionDevolucionMedicamentosForm forma=(RecepcionDevolucionMedicamentosForm)form;
				RecepcionDevolucionMedicamentos mundo=new RecepcionDevolucionMedicamentos();
				String estado=forma.getEstado();

				try
				{
					String tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
					HttpSession session=request.getSession();
					UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
					loger.warn("[RecepcionDevolucionMedicamentosAction] estado  --->" +estado);
					PersonaBasica paciente= Utilidades.getPersonaBasicaSesion(request.getSession());
					forma.setInterfazCompras(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazCompras(usuario.getCodigoInstitucionInt())));

					if(estado.equals("empezar"))
					{
						//validacion de acceso x almacen
						ActionForward validacionesGenerales = validacionesAccesoUsuario(mapping, request, usuario);
						if (validacionesGenerales != null)
						{
							UtilidadBD.cerrarConexion(con);
							return validacionesGenerales ;		
						}
						forma.reset(usuario.getCodigoInstitucionInt());
						mundo.reset();
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("principal");
					}
					else if(estado.equals("listadoPaciente"))
					{
						ActionForward forward= validacionAccesoPaciente(paciente, request, mapping);
						if(forward!=null)
						{
							UtilidadBD.closeConnection(con);
							return forward;
						}

						forma.reset(usuario.getCodigoInstitucionInt());
						mundo.reset();
						forma.setPorPaciente(true);
						forma.setColeccion(mundo.consultarDevoluciones(con,usuario.getCodigoCentroCosto(),paciente.getCodigoPersona(),null));
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("listadoDevoluciones");
					}
					else if(estado.equals("rango"))
					{
						forma.reset(usuario.getCodigoInstitucionInt());
						mundo.reset();
						forma.setPorPaciente(false);
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("filtro");
					}
					else if(estado.equals("listadoRango"))
					{
						mundo.reset();
						forma.setPorPaciente(false);
						HashMap<String, Object> vo=new HashMap<String, Object>();
						vo.put("areaFiltro",forma.getAreaFiltro());
						vo.put("pisoFiltro",forma.getPisoFiltro());
						vo.put("habitacionFiltro", forma.getHabitacionFiltro());
						vo.put("camaFiltro", forma.getCamaFiltro());
						forma.setColeccion(mundo.consultarDevoluciones(con,usuario.getCodigoCentroCosto(),ConstantesBD.codigoNuncaValido,vo));
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("listadoDevoluciones");
					}
					else if(estado.equals("detalle"))
					{	
						forma.setPaciente(paciente.getNombrePersona());
						//paciente.cargar(con,forma.getCodigoPaciente());
						Cuenta cuenta=new Cuenta();
						cuenta.cargarCuenta(con,forma.getCuenta()+"");
						paciente=cuenta.getPaciente();
						forma.setPaciente(paciente.getNombrePersona());
						forma.setColeccionDetalle(mundo.consultarUnaDevolucion(con,forma.getNumeroDevolucion()));
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("detalleRecepcion");
					}
					//estado que se tiene para conservar los datos del hashmap cuando se utiliza el pager.
					else if(estado.equals("continuar"))
					{
						UtilidadBD.cerrarConexion(con);
						response.sendRedirect(forma.getDir());
					}
					else if(estado.equals("volver"))
					{
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("listadoDevoluciones");
					}
					else if(estado.equals("guardar"))
					{
						myFactory.beginTransaction(con);
						llenarMaestroMundo(forma,mundo,usuario);
						int resp=mundo.insertarRecepcionMaestro(con);
						if(resp>=0)
						{
							if(mundo.actualizarEstadoDevolucion(con,ConstantesBD.codigoEstadoDevolucionRecibida)>=0)
							{
								forma.setEstadoDevolucion("Recibida");
								int i;
								for(i=0;i<forma.getColeccionDetalle().size();i++)
								{
									llenarDetalleMundo(forma,mundo,i);


									//	////////////////////////////////	/
									if(forma.isInterfazCompras()&&forma.getArticulos().containsKey("tiporecepcion_"+i)&&!UtilidadTexto.isEmpty(forma.getArticulos("tiporecepcion_"+i)+""))
									{
										ActionErrors errores = new ActionErrors();
										boolean errorValidacion = false;

										if((forma.getArticulos("tiporecepcion_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDevolucionConsignacion))
										{
											//double valorUnitario=UtilidadInventarios.costoActualArticulo(mundo.getArticulo());
											double valorUnitario=UtilidadInventarios.obtenerValorArticuloProveedorConveProveedor(con, forma.getArticulos().get("proveedorCompra_"+i)+"",mundo.getArticulo());


											boolean transaccionExitosa=false;

											String tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
											int tipoTransaccion1=UtilidadInventarios.obtenerTipoTransaccionInterfaz(con,ConstantesBD.codigoTransaccionSalidaDevolucionCompraConsignacion,usuario.getCodigoInstitucionInt());
											ConsecutivosDisponibles consec=new ConsecutivosDisponibles();
											String consecutivo="";


											int codigoAlmacen=forma.getCodFarmacia(); 
											int codigoAlmacenConsecutivo=0;

											if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosUnicoSistema))
												codigoAlmacenConsecutivo=ConstantesBD.codigoNuncaValido;        
											else if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen))
												codigoAlmacenConsecutivo=usuario.getCodigoCentroCosto(); 

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

												transaccionExitosa=UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion1,mundo.getArticulo(),mundo.getCantidadRecibida(),valorUnitario+"",mundo.getLote(),UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaVencimiento()),forma.getArticulos().get("proveedorCompra_"+i)+"",forma.getArticulos().get("proveedorCatalogo_"+i)+"");
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

													transaccionExitosa=UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion2,mundo.getArticulo(),mundo.getCantidadRecibida(),valorUnitario+"",mundo.getLote(),UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaVencimiento()),forma.getArticulos().get("proveedorCompra_"+i)+"",forma.getArticulos().get("proveedorCatalogo_"+i)+"");
													transaccionExitosa=UtilidadInventarios.generarRegistroCierreTransaccion(con,codTransaccion2+"",usuario.getLoginUsuario(),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
													if(Articulo.articuloManejaLote(con, mundo.getArticulo(), usuario.getCodigoInstitucionInt()))
														transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,mundo.getArticulo(),codigoAlmacen,true,mundo.getCantidadRecibida(),usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,mundo.getLote(),UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaVencimiento()));        		
													else
														transaccionExitosa=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,mundo.getArticulo(),codigoAlmacen,true,mundo.getCantidadRecibida(),usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
													////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
													//Modificado por el ancexo 779
													ResultadoInteger resultadoInteger= generarRegistroTransaccionInterfazConsignacion(con,
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
															forma.getArticulos().get("proveedorCompra_"+i)+"",paciente,
															forma.getCodFarmacia(), //CENTRO COSTO QUE EJECUTA 
															forma.getCodigoCentroCosto(), //CENTRO DE COSTO QUE SOLICITA
															codigoAlmacen,
															usuario.getLoginUsuario()); 


													if (UtilidadCadena.noEsVacio(resultadoInteger.getDescripcion()))
														if (UtilidadCadena.noEsVacio(forma.getMensajesAdvertenciaMap("numRegistros")+""))
														{
															forma.setMensajesAdvertenciaMap("mensaje_"+forma.getMensajesAdvertenciaMap("numRegistros"), resultadoInteger.getDescripcion());
															forma.setMensajesAdvertenciaMap("numRegistros", Utilidades.convertirAEntero(forma.getMensajesAdvertenciaMap("numRegistros")+"")+1);
														}
														else
														{
															forma.setMensajesAdvertenciaMap("mensaje_0", resultadoInteger.getDescripcion());
															forma.setMensajesAdvertenciaMap("numRegistros", 0);
														}
													/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

												transaccionExitosa=UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion,mundo.getArticulo(),mundo.getCantidadRecibida(),valorUnitario+"",mundo.getLote(),UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaVencimiento()),forma.getArticulos().get("proveedorCompra_"+i)+"",forma.getArticulos().get("proveedorCatalogo_"+i)+"");
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
														forma.getArticulos().get("proveedorCatalogo_"+i)+"",paciente, 
														forma.getCodFarmacia(), //CENTRO COSTO QUE EJECUTA 
														forma.getCodigoCentroCosto(),//CENTRO DE COSTO QUE SOLICITA
														usuario.getLoginUsuario()); 

											}
										}

										//si hay un error de validacion no se genera el despacho
										// y se muestran los errores.
										if(errorValidacion)
										{
											saveErrors(request,errores);
											UtilidadBD.abortarTransaccion(con);
											UtilidadBD.closeConnection(con);
											return mapping.findForward("detalleRecepcion");
										}

									}



									if(mundo.insertarRecepcionDetalle(con)<0)
									{
										i=forma.getColeccionDetalle().size()+1;
										myFactory.abortTransaction(con);
									}
								}

								//SE HACE LA GENERACION DEL NUEVO COSTO PROMEDIO
								double costoPromedioTemp=0;
								boolean inserto=false;
								for(i=0; i<forma.getColeccionDetalle().size(); i++)
								{    
									if(Integer.parseInt(forma.getArticulos("recibidos_"+i)+"")>0)
									{
										costoPromedioTemp=UtilidadInventarios.calcularCostoPromedioArticulo(Integer.parseInt(forma.getArticulos("articulo_"+i)+""),Integer.parseInt(forma.getArticulos("recibidos_"+i)+""), UtilidadInventarios.costoActualArticulo(Integer.parseInt(forma.getArticulos("articulo_"+i)+"")), ConstantesBDInventarios.codigoTipoConceptoInventarioEntrada, usuario.getCodigoInstitucionInt(), forma.getCodFarmacia() );
										inserto=UtilidadInventarios.actualizarCostoPromedioArticulo(con, Integer.parseInt(forma.getArticulos("articulo_"+i)+""), costoPromedioTemp);
										if(!inserto)
										{
											myFactory.abortTransaction(con);
											loger.warn("Error en la genracion del nuevo costo promedio");
										}
									}    
								}

								//SE HACE LA ACTUALIZACION DE LAS EXISTENCIAS
								for(i=0; i<forma.getColeccionDetalle().size(); i++)
								{    
									if(Integer.parseInt(forma.getArticulos("recibidos_"+i)+"")>0)
									{
										if(Articulo.articuloManejaLote(con, Integer.parseInt(forma.getArticulos("articulo_"+i)+""), usuario.getCodigoInstitucionInt()))
											inserto=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con, Integer.parseInt(forma.getArticulos("articulo_"+i)+""), forma.getCodFarmacia(), true, Integer.parseInt(forma.getArticulos("recibidos_"+i)+""), usuario.getCodigoInstitucionInt(), ConstantesBD.continuarTransaccion,forma.getArticulos("lote_"+i)+"",UtilidadFecha.conversionFormatoFechaABD(forma.getArticulos("fechavencimiento_"+i)+""));
										else
											inserto=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con, Integer.parseInt(forma.getArticulos("articulo_"+i)+""), forma.getCodFarmacia(), true, Integer.parseInt(forma.getArticulos("recibidos_"+i)+""), usuario.getCodigoInstitucionInt(), ConstantesBD.continuarTransaccion );

										if(!inserto)
										{
											myFactory.abortTransaction(con);
											loger.warn("Error en la genracion del nuevo costo promedio");
										}
									}    
								}

								if(i==forma.getColeccionDetalle().size())
								{
									myFactory.endTransaction(con);
									forma.setEstado("resumen");
									this.cargarMensajesStockMaximoMinimo(con, forma, usuario.getCodigoInstitucionInt());
									UtilidadBD.cerrarConexion(con);
									response.sendRedirect("recepcionDevolucionMedicamentos.do?estado=resumen");
									return null;
								}
							}
							else
							{
								myFactory.abortTransaction(con);
							}
						}
						else if(resp==Integer.parseInt(IdentificadoresExcepcionesSql.codigoExcepcionSqlRegistroExistente)*-1)
						{
							myFactory.abortTransaction(con);
							UtilidadBD.cerrarConexion(con);
							return ComunAction.accionSalirCasoError(mapping, request, con, loger, "Error en la inserccion", "LA RECEPCION DEVOLUCION DE MEDICAMENTOS HA SIDO MODIFICADA POR OTRO USUARIO", false);
						}
						else
						{
							myFactory.abortTransaction(con);
						}
						UtilidadBD.cerrarConexion(con);
						/*
						 * Solución de la Tarea 45591
						 * El mensaje debe de ser:
						 * "la devolucion ya fue modificada por otro usuario "
						 */
						//return ComunAction.accionSalirCasoError(mapping, request, con, loger, "Error en la inserccion", "Error Durante la insercion, transaccion abortada.", false);
						return ComunAction.accionSalirCasoError(mapping, request, con, loger, "Error en la inserccion", "La devolucion ya fue modificada por otro usuario ", false);
					}
					else if(estado.equals("resumen"))
					{
						if(cargarResumen(con,mundo,forma))
						{
							UtilidadBD.cerrarConexion(con);
							return mapping.findForward("resumenRecepcion");
						}
						else
						{
							UtilidadBD.cerrarConexion(con);
							return ComunAction.accionSalirCasoError(mapping, request, con, loger, "Error en la consulta", "Error durante la consulta de la reciente insercion, posible error durante la insercion.", false);
						}
					}
					else if(estado.equals("ordenar"))
					{
						this.accionOrdenar(forma);
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("listadoDevoluciones");
					}
					else if(estado.equals("imprimir"))
					{
						String nombreArchivo;
						Random r=new Random();
						nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
						RecepcionDevolucionMedicamentosPdf.pdfRecepcionDevolucionMedicamentos(ValoresPorDefecto.getFilePath() + nombreArchivo, forma, usuario, paciente.getCodigoConvenio());
						UtilidadBD.cerrarConexion(con);
						request.setAttribute("nombreArchivo", nombreArchivo);
						request.setAttribute("nombreVentana", "Consulta Diagnósticos");
						return mapping.findForward("abrirPdf");
					}
					else
					{
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaError");
					}


				}
				catch(Exception e)
				{
					e.printStackTrace();
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					UtilidadBD.closeConnection(con);
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
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward validacionAccesoPaciente(PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping) 
	{
		if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
		{
			loger.warn("Paciente no válido (null)");			
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return mapping.findForward("paginaError");
		}
		return null;
	}

	/**
	 * @param forma
	 */
	private void accionOrdenar(RecepcionDevolucionMedicamentosForm forma)
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
	 */
	private boolean cargarResumen(Connection con, RecepcionDevolucionMedicamentos mundo, RecepcionDevolucionMedicamentosForm forma) 
	{
		ResultSetDecorator rs;
		mundo.setNumeroDevolucion(forma.getNumeroDevolucion());
		rs=mundo.consultarMaestroRecepcion(con);
		try {
			if(rs.next())
			{
				forma.setEstadoDevolucion(rs.getString("estadodevolucion"));
				String[] fecha=rs.getString("fechahoradevolucion").split(" ");
				forma.setFechaDevolucion(fecha[0]);
				forma.setHoraDevolucion(fecha[1]);
				fecha=rs.getString("fechahorarecepcion").split(" ");
				forma.setFechaRecepcion(fecha[0]);
				forma.setHoraRecepcion(fecha[1]);
				forma.setPaciente(rs.getString("apellidos")+" "+rs.getString("nombres"));
				forma.setCuenta(rs.getInt("cuenta"));
				forma.setCentroCosto(rs.getString("nombrecentrocosto"));
				forma.setUsuario(rs.getString("usuariodevuelve"));
				forma.setFarmacia(rs.getString("nombrefarmacia"));
				forma.setUsuarioRecibe(rs.getString("usuariorecibe"));
				forma.setMotivo(rs.getString("motivo"));
				forma.setColeccionDetalle(mundo.consultarDetalleRecepcion(con));
				return true;
			}
			else
			{
				return false;
			}
		} catch (SQLException e) {
			loger.warn("Se produjo error consultando los datos para el resumen de la insercion"+e);
			return false;
		}
		
	}

	/**
	 * Metodo para llenar el detalle mundo con los datos de la forma
	 * @param forma, forma con la que se esta trabajando
	 * @param mundo. objeto de la recepcion
	 * @param i,<b>int</b>, variable que me maneja el indice del HashMap
	 */
	private void llenarDetalleMundo(RecepcionDevolucionMedicamentosForm forma, RecepcionDevolucionMedicamentos mundo, int i) 
	{
		mundo.setCodigo(Integer.parseInt(forma.getArticulos("codigo_"+i)+""));//es el codigo de la devolucion
		mundo.setArticulo(Integer.parseInt(forma.getArticulos("articulo_"+i)+""));
		mundo.setNumeroDevolucion(Integer.parseInt(forma.getArticulos("numeroDevolucion_"+i)+""));
		mundo.setCantidadRecibida(Integer.parseInt(forma.getArticulos("recibidos_"+i)+""));
		mundo.setLote(forma.getArticulos("lote_"+i)+"");
		mundo.setFechaVencimiento(forma.getArticulos("fechavencimiento_"+i)+"");
		mundo.setTipoRecepcion(forma.getArticulos("tiporecepcion_"+i)+"");
		mundo.setAlmacenConsignacion(forma.getArticulos("almacenConsignacion_"+i)+"");
		mundo.setProveedorCompra(forma.getArticulos("proveedorCompra_"+i)+"");
		mundo.setProveedorCatalogo(forma.getArticulos("proveedorCatalogo_"+i)+"");
	}

	/**
	 * Metodo para llenar el objeto maestro de la recepcion de devolucion de pedidos
	 * @param forma, forma que contiene los datos
	 * @param mundo. objero recepcion devolucion de Medicamentos
	 * @param usuario, Usuario que esta manipulando el sistema. 
	 */
	private void llenarMaestroMundo(RecepcionDevolucionMedicamentosForm forma, RecepcionDevolucionMedicamentos mundo, UsuarioBasico usuario) 
	{
		mundo.setNumeroDevolucion(forma.getNumeroDevolucion());
		mundo.setFechaRecepcion(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mundo.setHoraRecepcion(UtilidadFecha.getHoraActual());
		mundo.setUsuarioRecibe(usuario.getLoginUsuario());
	}

	 /**
     * metodo que realiza las validaciones de acceso a usuario
     * @param paciente
     * @param map
     * @param req
     * @param user
     * @return
     */
    protected ActionForward validacionesAccesoUsuario(  ActionMapping map, HttpServletRequest req,
                                                                                    UsuarioBasico user)
    {
        /*validaciones de inventarios*/
        if(!UtilidadInventarios.esAlmacenUsuarioAutorizado(user.getLoginUsuario(),user.getCodigoCentroCosto(), user.getCodigoInstitucionInt()))
        {
           ActionErrors errores = new ActionErrors(); 
           errores.add("usuario no autorizado", new ActionMessage("error.inventarios.usuarioNoAutorizado",user.getCentroCosto()));
           saveErrors(req, errores);
           return map.findForward("paginaErroresActionErrors");
        }
        return null;
    }

    /**
     * metodo que carga el mapa para mostrar los mensajes de los articulos 
     * que no cumplen con el stock maxiom - minimo y punto pedido
     * @param cargosForm
     * @return
     */
    private boolean cargarMensajesStockMaximoMinimo (Connection con, RecepcionDevolucionMedicamentosForm forma, int codigoInstitucion)
    {
        int index=0;
        String descripcionesArticulosStockMin="", descripcionesArticulosStockMaximo="", descripcionesArticulosPuntoPedido="";
        Articulo objectArticulo= new Articulo();
        for(int i=0; i<forma.getColeccionDetalle().size(); i++)
        {    
            if(Integer.parseInt(forma.getArticulos("recibidos_"+i)+"")>=0)
            {
                try
                {
                    objectArticulo.cargarArticulo(con, Integer.parseInt(forma.getArticulos("articulo_"+i)+""));
                    if(!UtilidadInventarios.existenciasArticuloMayorIgualStockMinimo(Integer.parseInt(forma.getArticulos("articulo_"+i)+""), codigoInstitucion))
                        descripcionesArticulosStockMin+= objectArticulo.getCodigo()+", ";
                    if(!UtilidadInventarios.existenciasArticuloMenorIgualStockMaximo(Integer.parseInt(forma.getArticulos("articulo_"+i)+""), codigoInstitucion))
                        descripcionesArticulosStockMaximo+= objectArticulo.getCodigo()+", ";
                    if(!UtilidadInventarios.existenciasArticuloMayorIgualPuntoPedido(Integer.parseInt(forma.getArticulos("articulo_"+i)+""), codigoInstitucion) )
                        descripcionesArticulosPuntoPedido+= objectArticulo.getCodigo()+", ";
                }  
                catch(NumberFormatException e)
                {
                    loger.warn("Error en el parseInt del codigo del articulo con indice ="+i +"   error-->"+e);
                    return false;
                }
            }
        }    
        if(!descripcionesArticulosStockMin.equals(""))
        {    
            forma.setMensajesAdvertenciaMap("mensaje_"+index, "ARTICULOS [ "+descripcionesArticulosStockMin+"] QUEDAN CON CANTIDAD MENOR AL STOCK MINIMO");
            index++;
        }
        if(!descripcionesArticulosStockMaximo.equals(""))
        {    
            forma.setMensajesAdvertenciaMap("mensaje_"+index, "ARTICULOS [ "+descripcionesArticulosStockMaximo+"] QUEDAN CON CANTIDAD MAYOR AL STOCK MAXIMO");
            index++;
        }
        if(!descripcionesArticulosPuntoPedido.equals(""))
        {    
            forma.setMensajesAdvertenciaMap("mensaje_"+index, "ARTICULOS [ "+descripcionesArticulosPuntoPedido+"] QUEDAN CON CANTIDAD MENOR AL PUNTO PEDIDO");
            index++;
        }
        forma.setMensajesAdvertenciaMap("numRegistros", index+"");
        return true;
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
	 * @param paciente 
	 * @param proveedor 
	 * @param codigoAlmacen 
	 * @param centroCostoSolicita 
	 * @param usuario 
	 */
	private ResultadoInteger generarRegistroTransaccionInterfazCompraProveedor(Connection con,int institucion, int codigoArticulo, int codTransaccion, int tipoTransaccion, int tipoTransInterfaz, int cantidadDespacho, double valorUnitario,double valorIva, String proveedor, PersonaBasica paciente, int codigoAlmacen, int centroCostoSolicita, String usuario) 
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
		dto.setIdentificacionProveedor(proveedor);
		dto.setCodigoPaciente(paciente.getCodigoPersona()+"");
		dto.setIngresoPaciente(paciente.getConsecutivoIngreso());	
		
		//CAMBIOS Oct 27 de 2008
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
	 */
	private ResultadoInteger generarRegistroTransaccionInterfazConsignacion(Connection con,int institucion, int codigoArticulo, int codTransaccion1, int codTransaccion2, int tipoTransaccion1, int tipoTransaccion2, int tipoTransInterfaz1, int tipoTransInterfaz2, int cantidadDespacho, double valorUnitario,double valorIva,String proveedor,PersonaBasica paciente, int codigoAlmacen, int centroCostoSolicita, int codigoAlmacenConsignacion, String usuario) 
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
		dto.setIdentificacionProveedor(proveedor);
		dto.setCodigoPaciente(paciente.getCodigoPersona()+"");
		dto.setIngresoPaciente(paciente.getConsecutivoIngreso());
		
		dto.setAlmacenInterfaz(codigoAlmacen+"");
		dto.setAlmacendespacha(codigoAlmacen+"");
		dto.setAlmacenSolicita(centroCostoSolicita+"");
		dto.setAlmacenConsignacion(codigoAlmacenConsignacion+"");
		
		dto.setUsuario(usuario);
		
		return utilInterfaz.insertarTransaccionInterfaz(dto,institucion,false);
		
		
	}

    
}
