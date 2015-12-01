package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.actionform.facturacion.EntidadesSubContratadasForm;
import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoCentroCostosVista;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroCostosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEstanciaViaIngCentroCostoServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IViasIngresoServicio;


/**
 * 
 * @author Jhony Alexander Duque A.
 * 02/01/2008
 *
 */



public class EntidadesSubContratadasAction extends Action
{
	
	/**
	 * Para manjar los logger de la clase CensoCamasAction
	 */
	Logger logger = Logger.getLogger(EntidadesSubContratadasAction.class);
	
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection connection = null;
		try{
			if (form instanceof EntidadesSubContratadasForm) 
			{


				connection = UtilidadBD.abrirConexion();

				//se verifica si la conexion esta nula
				if (connection == null)
				{
					// de ser asi se envia a una pagina de error. 
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//optenemos el valor de la forma.
				EntidadesSubContratadasForm forma = (EntidadesSubContratadasForm) form;

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();
				ActionErrors errores = new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				logger.info("\n\n***************************************************************************");
				logger.info(" 	           EL ESTADO DE EntidadesSubContratadasForm ES ====>> "+forma.getEstado());
				logger.info("\n***************************************************************************");

				/*----------------------------------------------
				 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/

				if (estado == null)
				{

					//se asigana un error a mostar por ser un estado invalido
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					//se cierra la conexion con la BD
					UtilidadBD.closeConnection(connection);

					//se retorna el error al forward paginaError.
					return mapping.findForward("paginaError");
				}
				else
					/*----------------------------------------------
					 * ESTADO =================>>>>>  INICIAL
				 ---------------------------------------------*/
					if (estado.equals("inicial"))
					{
						EntidadesSubContratadas.inicial(forma);
						forma.setActivo(true);
						forma.setUsuarios(ConstantesBD.codigoNuncaValido+"");

						logger.info("valor de seccionInsertar: "+forma.getEntidadesSubContratadas("seccionInsertar"));
						ActionForward retorno=EntidadesSubContratadas.busqueda(connection, forma, usuario, mapping,true);
						forma.setEntidadesSubContratadas("seccionInsertar", ConstantesBD.acronimoNo);
						UtilidadBD.closeConnection(connection);
						return retorno;


					}
					else/*----------------------------------------------
					 * ESTADO =================>>>>>  BUSQUEDA TERCERO
					 ---------------------------------------------*/
						if(estado.equals("busquedaTercero"))
						{
							forma.resetTerceros();
							UtilidadBD.closeConnection(connection);
							return mapping.findForward("consultaEntidad");
						}
						else /*----------------------------------------------
						 * ESTADO =================>>>>>  BUSCAR ENTIDAD
						 ---------------------------------------------*/
							if(estado.equals("buscarEntidad"))
							{

								EntidadesSubContratadas.buscarTercero(connection, forma);
								UtilidadBD.closeConnection(connection);
								return mapping.findForward("consultaEntidad");
							}
							else/*----------------------------------------------
							 * ESTADO =================>>>>>  GUARDAR
							 ---------------------------------------------*/
								if (estado.equals("guardar"))
								{
									UtilidadBD.closeConnection(connection);
									String estancia=	forma.getEntidadesSubContratadas().get("estancia35_0").toString();
									if(estancia.equals(ConstantesBD.acronimoSi)){
										request.setAttribute("mostrar", "block");
									}
									ActionForward retorno=EntidadesSubContratadas.accionGuardarRegistros(forma, mapping, usuario, forma.getListaEstanciaviaIngCentroCosto());
									forma.setViasIngreso(String.valueOf(ConstantesBD.codigoNuncaValido));
									forma.setCentroCosto(String.valueOf(ConstantesBD.codigoNuncaValido));
									return retorno;
								}
								else/*----------------------------------------------
								 * ESTADO =================>>>>>  detalle
								 ---------------------------------------------*/
									if (estado.equals("detalle"))
									{
										errores = EntidadesSubContratadas.accionValidar(forma, errores);
										if(!errores.isEmpty())
										{
											saveErrors(request,errores);	
											UtilidadBD.closeConnection(connection);
											return mapping.findForward("detalleVig");
										}
										ActionForward retorno= EntidadesSubContratadas.initDetalle(connection, forma, usuario, mapping, request, response);
										UtilidadBD.closeConnection(connection);
										return retorno;

									}
									else/*----------------------------------------------
									 * ESTADO =================>>>>>  NUEVAVIG
									 ---------------------------------------------*/
										if (estado.equals("nuevaVig"))
										{

											ActionForward retorno=EntidadesSubContratadas.nuevaVig(connection, forma, mapping, usuario, response, request);
											UtilidadBD.closeConnection(connection);
											return retorno;
										}
										else/*----------------------------------------------
										 * ESTADO =================>>>>>  GUARDARVIG
										 ---------------------------------------------*/
											if (estado.equals("guardarVig"))
											{
												errores = EntidadesSubContratadas.accionValidar(forma, errores);
												if(!errores.isEmpty())
												{
													saveErrors(request,errores);	
													UtilidadBD.closeConnection(connection);
													return mapping.findForward("detalleVig");
												}

												ActionForward retorno= EntidadesSubContratadas.accionGuardarRegistrosVig(connection, forma, mapping, usuario, request, response);
												UtilidadBD.closeConnection(connection);
												return retorno;
											}
											else/*----------------------------------------------
											 * ESTADO =================>>>>>  ELIMINARVIG
											 ---------------------------------------------*/
												if (estado.equals("eliminarvig"))
												{

													ActionForward retorno= EntidadesSubContratadas.accionEliminarCampoVig(forma, request, response, usuario.getCodigoInstitucionInt(), mapping);
													UtilidadBD.closeConnection(connection);
													return retorno;
												}
												else/*----------------------------------------------
												 * ESTADO =================>>>>>  BUSQUEDA
												 ---------------------------------------------*/
													if (estado.equals("busqueda"))
													{

														ActionForward retorno=  EntidadesSubContratadas.busqueda(connection, forma, usuario, mapping,true);
														UtilidadBD.closeConnection(connection);
														return retorno;
													}
													else/*----------------------------------------------
													 * ESTADO =================>>>>>  CARGAR
													 ---------------------------------------------*/
														if (estado.equals("cargar"))
														{

															ActionForward retorno=   EntidadesSubContratadas.cargarEntidad(connection, forma, usuario, mapping);

															try{


																if(forma.getEntidadesSubContratadas().get("estancia35_0")!=null){

																	String estancia=	forma.getEntidadesSubContratadas().get("estancia35_0").toString();

																	if(estancia.equals(ConstantesBD.acronimoSi)){
																		request.setAttribute("mostrar", "block");
																	}
																	else{
																		request.setAttribute("mostrar", "");
																	}
																}
																else{
																	request.setAttribute("mostrar", "");
																}



																if( forma.getEntidadesSubContratadas().get("nombreTercero20_0") !=null)
																{
																	forma.setNombreTemporalTercero(forma.getEntidadesSubContratadas().get("nombreTercero20_0").toString());
																	forma.setCodigoTemporalTercero(forma.getEntidadesSubContratadas().get("tercero4_0").toString());
																}




															}catch(Exception e){

															}



															//

															UtilidadBD.closeConnection(connection);
															return retorno;
														}
														else/*----------------------------------------------
														 * ESTADO =================>>>>>  ELIMINARCAMPO
														 ---------------------------------------------*/
															if (estado.equals("eliminarCampo"))
															{

																ActionForward retorno=   EntidadesSubContratadas.accionEliminarCampo(forma, request, response, usuario.getCodigoInstitucionInt() , mapping);
																UtilidadBD.closeConnection(connection);
																return retorno;
															}
															else/*----------------------------------------------
															 * ESTADO =================>>>>>  ELIMINARCAMPO
															 ---------------------------------------------*/
																if (estado.equals("eliminar"))
																{

																	ActionForward retorno=   EntidadesSubContratadas.accionEliminar(connection, forma, mapping, usuario);
																	UtilidadBD.closeConnection(connection);
																	return retorno;
																}
																else/*----------------------------------------------
																 * ESTADO =================>>>>>  volver
																 ---------------------------------------------*/
																	if (estado.equals("volver"))
																	{
																		UtilidadBD.closeConnection(connection);
																		return mapping.findForward("principal");
																	}
																	else /*----------------------------------------------
																	 * ESTADO =================>>>>>  ordenar
																	 ---------------------------------------------*/
																		if (estado.equals("ordenarbusqueda"))//estado encargado de ordenar el HashMap del censo.
																		{
																			forma.setResultBusqueda(EntidadesSubContratadas.accionOrdenarMapa(forma.getResultBusqueda(),forma));				 
																			UtilidadBD.closeConnection(connection);
																			return mapping.findForward("principal");	

																		}
																		else /*----------------------------------------------
																		 * ESTADO =================>>>>>  REDIRECCION
																 ---------------------------------------------*/
																			if (estado.equals("redireccionbusqueda"))
																			{
																				UtilidadBD.closeConnection(connection);
																				response.sendRedirect(forma.getLinkSiguiente());
																				return null;
																			}
																			else /*----------------------------------------------
																			 * ESTADO =================>>>>>  REDIRECCION
																	 ---------------------------------------------*/
																				if (estado.equals("redireccion"))
																				{
																					UtilidadBD.closeConnection(connection);
																					response.sendRedirect(forma.getLinkSiguiente());
																					return null;
																				}
																				else if(estado.equals("asignarUsuarios"))
																				{
																					if (EntidadesSubContratadas.existeUsuario(connection, forma, usuario, mapping))
																					{
																						errores.add("",	new ActionMessage("errors.notEspecific","Ya agregó el usuario seleccionado. Porfavor seleccione otro."));
																						saveErrors(request, errores);
																					}
																					else
																						forma.setUsuariosSelMap(EntidadesSubContratadas.asignarUsuarios(connection, forma, usuario, mapping));

																					UtilidadBD.closeConnection(connection);
																					return mapping.findForward("principal");
																				}
																				else if(estado.equals("eliminarUsuario"))
																				{
																					forma.setUsuariosSelMap(EntidadesSubContratadas.eliminarUsuario(connection, forma, usuario, mapping));
																					UtilidadBD.closeConnection(connection);
																					return mapping.findForward("principal");
																				}


				/////////////////////////////////////////////////
																				else if(estado.equals("cargarCentroCostoxViaIngreso")){


																					String codigoViaIngreso= forma.getViasIngreso();

																					int consecutivoViaIngreso = ConstantesBD.codigoNuncaValido;

																					ICentroCostosServicio costoServicio=AdministracionFabricaServicio.crearCentroCostosServicio();

																					if(!UtilidadTexto.isEmpty(codigoViaIngreso)) {

																						try{

																							consecutivoViaIngreso= Integer.parseInt(codigoViaIngreso);
																							if( consecutivoViaIngreso>0){

																								forma.setListaCentroCosto(costoServicio.obtenerCentrosCostoPorViaIngreso(consecutivoViaIngreso));
																								request.setAttribute("mostrar", "block");

																							}

																						}
																						catch (Exception e) {
																							Log4JManager.error(e);
																						}

																					}
																					return mapping.findForward("principal");

																				}

																				else if(forma.getEstado().equals("limpiarCamposDiv")){
																					forma.setViasIngreso(null);
																					forma.setListaCentroCosto(new ArrayList<DtoCentroCostosVista>());
																					forma.setListaEstanciaviaIngCentroCosto(new ArrayList<DTOEstanciaViaIngCentroCosto>());

																					return mapping.findForward("principal");
																				}	

																				else if(forma.getEstado().equals("adicionarCentroCostos")){

																					IEstanciaViaIngCentroCostoServicio estanciaServicio= FacturacionServicioFabrica.crearEstanciaViaIngCentroCosto();


																					/*
																					 * 2 llenar la informacion del dto.
																					 * 
																					 */
																					DTOEstanciaViaIngCentroCosto dtoEstancia = new DTOEstanciaViaIngCentroCosto();

																					/*
																					 *Validar que no existan registros iguales 
																					 */

																					int codCentroCosto=ConstantesBD.codigoNuncaValido;
																					int codVia = ConstantesBD.codigoNuncaValido;
																					codCentroCosto= Integer.parseInt(forma.getCentroCosto());
																					codVia= Integer.parseInt(forma.getViasIngreso());
																					List<DTOEstanciaViaIngCentroCosto> listaEstancia=  forma.getListaEstanciaviaIngCentroCosto();


																					if((codCentroCosto == ConstantesBD.codigoNuncaValido) && (codVia == ConstantesBD.codigoNuncaValido )){
																						errores.add("",	new ActionMessage("errors.notEspecific","Debe seleccionar via de ingreso y centro de costo."));
																						saveErrors(request, errores);
																						request.setAttribute("mostrar", "block");
																						return mapping.findForward("principal");

																					}

																					if(codCentroCosto == ConstantesBD.codigoNuncaValido)
																					{
																						errores.add("",	new ActionMessage("errors.notEspecific","Debe seleccionar un centro de costo."));
																						saveErrors(request, errores);
																						request.setAttribute("mostrar", "block");
																						return mapping.findForward("principal");

																					}

																					if (codVia == ConstantesBD.codigoNuncaValido ){

																						errores.add("",	new ActionMessage("errors.notEspecific","Debe seleccionar una via de ingreso."));
																						saveErrors(request, errores);
																						request.setAttribute("mostrar", "block");
																						return mapping.findForward("principal");

																					}

																					boolean existe=estanciaServicio.validarExistencia(codVia, codCentroCosto, listaEstancia);
																					if(existe){


																						errores.add("",	new ActionMessage("errors.notEspecific","Ya agregó via de ingreso por centro de costo. Porfavor seleccione otro."));
																						saveErrors(request, errores);
																						request.setAttribute("mostrar", "block");
																						return mapping.findForward("principal");
																					}
																					else{

																						/*
																						 * Construir objeto
																						 */
																						//dtoEstancia.setCentroCosto(forma.getCodCentroCosto());
																						dtoEstancia.setCentroCosto(Integer.parseInt(forma.getCentroCosto()));

																						ICentroCostosServicio servicioCentroCosto=AdministracionFabricaServicio.crearCentroCostosServicio();
																						String tmpNombreCentroCosto=servicioCentroCosto.findById(codCentroCosto).getNombre();
																						dtoEstancia.setNombreCentroCosto(tmpNombreCentroCosto);

																						dtoEstancia.setViaIngreso(Integer.parseInt(forma.getViasIngreso()));
																						IViasIngresoServicio servicioVia=ManejoPacienteServicioFabrica.crearViasIngresoServicio();
																						String tmNombreVia=servicioVia.findbyId(codVia).getNombre();
																						dtoEstancia.setNombreViaIngreso(tmNombreVia); 

																						/*
																						 * Adicionar a la lista
																						 */
																						forma.getListaEstanciaviaIngCentroCosto().add(dtoEstancia);

																						request.setAttribute("mostrar", "block");
																						forma.setCodCentroCosto(Integer.parseInt(forma.getCentroCosto()));
																						//forma.setViasIngreso(null);
																						//forma.setListaCentroCosto(new ArrayList<DtoCentroCostosVista>());
																						//forma.setCentroCosto(null);
																						//forma.setViasIngreso(null);


																						return mapping.findForward("principal");

																					}

																				}
																				else if(forma.getEstado().equals("eliminarViasCentro")){


																					if( forma.getListaEstanciaviaIngCentroCosto().get(forma.getIndiceEliminarViasCentros()).getCodigoPk()<=0){
																						forma.getListaEstanciaviaIngCentroCosto().remove(forma.getIndiceEliminarViasCentros());
																					}
																					else {

																						forma.getListaEstanciaviaIngCentroCosto().get(forma.getIndiceEliminarViasCentros()).setActivo(Boolean.FALSE) ;

																					}


																					request.setAttribute("mostrar", "block");
																					return mapping.findForward("principal");

																				}



			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
		return null;
	}
	
}