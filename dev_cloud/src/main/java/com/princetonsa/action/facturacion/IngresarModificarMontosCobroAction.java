package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.IngresarModificarMontosCobroForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionServicio;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoArticuloEspecifico;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoServicioEspecifico;
import com.princetonsa.dto.facturacion.DTOMontosCobro;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontoAgrupacionArticuloMundo;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.ExcepcionesNaturaleza;
import com.servinte.axioma.orm.GrupoInventario;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.NaturalezaArticulo;
import com.servinte.axioma.orm.SubgrupoInventario;
import com.servinte.axioma.orm.TiposRegimen;
import com.servinte.axioma.orm.TiposServicio;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.inventario.InventarioServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IEspecialidadesServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IDetalleMontoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IGruposServiciosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontoAgrupacionServiciosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontoArticuloEspecificoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontoServicioEspecificoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontosCobroServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ITiposMontoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ITiposServicioServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IClaseInventarioServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IGrupoInventarioServicio;
import com.servinte.axioma.servicio.interfaz.inventario.INaturalezaArticuloServicio;
import com.servinte.axioma.servicio.interfaz.inventario.ISubgrupoInventarioServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IEstratoSocialServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IExcepcionesNaturalezaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ITiposAfiliadoServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ITiposPacienteServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IViasIngresoServicio;

/**
 * 
 * Esta clase se encarga de manejar las solicitudes
 * hechas por el usuario										
 * 
 * @author Angela Maria Aguirre
 * @since 27/08/2010
 */
public class IngresarModificarMontosCobroAction extends Action {
	
	/**
	 * 
	 * Este Método se encarga de ejecutar las acciones sobre las páginas
	 * de Montos Cobro
	 * 
	 * 
	 * @param  ActionMapping mapping, HttpServletRequest request,
	 *         HttpServletResponse response
	 * @return ActionForward     
	 * @author Angela Maria Aguirre
	 *
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		Connection conn =null;
		try{
			if (form instanceof IngresarModificarMontosCobroForm) {
				IngresarModificarMontosCobroForm forma = (IngresarModificarMontosCobroForm)form;
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
						.getSession());
				String estado = forma.getEstado();
				Log4JManager.info("estado " + estado);
				forma.setMostrarMensaje("");

				conn = UtilidadPersistencia.getPersistencia().obtenerConexion();
				if(estado.equals("empezar")){
					return empezar(forma, mapping);
				}else{
					if(estado.equals("buscarMontos")){
						return buscarMontos(forma, usuario, mapping,request);
					}else{
						if(estado.equals("postularFecha")){
							return postularFecha(forma, mapping);
						}else{
							if(estado.equals("mostrarDetalle")){
								return mostrarDetalle(forma,mapping,usuario);
							}else{
								if(estado.equals("nuevo")){
									return nuevo(forma, mapping, request);
								}else{
									if(estado.equals("eliminar")){
										eliminar(forma, mapping,request);					
										return buscarMontos(forma, usuario, mapping,request);
									}else{
										if(estado.equals("guardar")){
											guardar(forma,mapping,usuario, request);
											if(forma.getMostrarMensaje().equals("resumen")){
												return buscarMontos(forma, usuario, mapping,request);
											}else{
												return mapping.findForward("listadoMontos");
											}					
										}else{
											if(estado.equals("mostrarPopupNuevoRegistro")){
												return mostrarPopupNuevoRegistro(forma,mapping);					 
											}else{
												if(estado.equals("recargarPagina")){
													return mapping.findForward("listadoMontos");
												}else{
													if(estado.equals("cambiaNaturaleza")){
														return mostrarCamposDetalleGeneral(forma,mapping,true);
													}else{
														if(estado.equals("cambiaTipoDetalle")){
															return mostrarCamposDetalleGeneral(forma,mapping,false);
														}else{
															if(estado.equals("guardarDetalle")){
																return guardarDetalle(forma, mapping,request,usuario);

															}else{
																if(estado.equals("recarga")){
																	DTOBusquedaMontoServicioEspecifico dto=new DTOBusquedaMontoServicioEspecifico();
																	dto.setCodigoServicio(forma.getCodigoServicio());
																	dto.setDescripcionServicio(forma.getNombreServicio());
																	if(forma.getDetalleSeleccionado().getListaServicioEspecifico()==null){
																		forma.getDetalleSeleccionado().setListaServicioEspecifico(
																				new ArrayList<DTOBusquedaMontoServicioEspecifico>());
																	}
																	forma.getDetalleSeleccionado().getListaServicioEspecifico().add(dto);
																	return mapping.findForward("mostrarDetalle");	
																}else{
																	if(estado.equals("recargaArticulo")){
																		DTOBusquedaMontoArticuloEspecifico dto=new DTOBusquedaMontoArticuloEspecifico();
																		dto.setArticuloCodigo(forma.getCodigoArticulo());
																		dto.setArticuloDescripcion(forma.getDescripcionArticulo());
																		if(forma.getDetalleSeleccionado().getListaArticuloEspecifico()==null){
																			forma.getDetalleSeleccionado().setListaArticuloEspecifico(
																					new ArrayList<DTOBusquedaMontoArticuloEspecifico>());
																		}
																		forma.getDetalleSeleccionado().getListaArticuloEspecifico().add(dto);
																		return mapping.findForward("mostrarDetalle");	
																	}else{
																		if(estado.equals("nuevoAgrupacionServicio")|| estado.equals("nuevoAgrupacionArticulo")){
																			return nuevoRegistroDetalle(forma, mapping,estado);
																		}else{
																			if(estado.equals("eliminarAgrupacionServicio")||
																					estado.equals("eliminarServicioEspecifico")||
																					estado.equals("eliminarAgrupacionArticulo")||
																					estado.equals("eliminarArticuloEspecifico")){
																				return eliminarDetalle(forma, mapping, estado, usuario);
																			}else{
																				if(estado.equals("cargarGrupo")){
																					return cargarGrupoInventario(forma,mapping);
																				}else{
																					if(estado.equals("cargarSubgrupo")){
																						return cargarSubgrupoInventario(forma,mapping);	
																					}else if(estado.equals("mostrarBotonDetalle")){
																						if(!forma.isMostrarBotonDetalle()){
																							forma.setMostrarBotonDetalle(true);
																						}
																						return mapping.findForward("empezar");
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			Log4JManager.error("Error en la transaccion de Monto Detalle", e);
		}finally{
			UtilidadTransaccion.getTransaccion().commit();
			//UtilidadBD.closeConnection(conn);
		}			
		return null;		
	}
	
	/**
	 * 
	 * Este Método se encarga de guardar la información de un monto
	 * de cobro y sus detalles
	 * 
	 * @param IngresarModificarMontosCobroForm forma,
			  ActionMapping mapping,UsuarioBasico usuario
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void guardar(IngresarModificarMontosCobroForm forma,
			ActionMapping mapping,UsuarioBasico usuario,HttpServletRequest request){
		IMontosCobroServicio montoServicio = FacturacionServicioFabrica.crearMontosCobroServicio();
		ActionErrors errores = new ActionErrors();
		ArrayList<DTOMontosCobro> listaMontos = forma.getListaMontosCobro();
		
		errores = validarDetalle(listaMontos);		
			
		if(errores!=null && errores.size()>0){
			saveErrors(request, errores);
		}else{
			for(DTOMontosCobro monto: listaMontos){
				monto.setConvenio(forma.getDtoConvenioSeleccionado().getConvenio());
				if(monto.getFechaVigenciaConvenio()==null){
					monto.setFechaVigenciaConvenio(forma.getDtoConvenioSeleccionado().getFechaVigenciaConvenio());
				}
				if(monto.getListaDetalles()!=null && monto.getListaDetalles().size()>0){
					montoServicio.guarDatosDetalleMontoCobro(monto, usuario);
				}
			}
			forma.setMostrarMensaje("resumen");
		}
	}
	
	/**
	 * 
	 * Este Método se encarga de mostrar el popup para la selección de 
	 * la vía de ingreso y el tipo de paciente necesario para un nuevo
	 * detalle del monto de cobro 
	 * 
	 * @param IngresarModificarMontosCobroForm forma,
			ActionMapping mapping
	 * @return ActionForward 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward mostrarPopupNuevoRegistro(IngresarModificarMontosCobroForm forma,
			ActionMapping mapping){
		
		IViasIngresoServicio viasIngresoServicio = ManejoPacienteServicioFabrica.
			crearViasIngresoServicio();
		ITiposPacienteServicio tipoPacienteServicio = ManejoPacienteServicioFabrica.
			crearTiposPacienteServicio();
		
		if(forma.getIndiceViaIngreso()>0){
			forma.setListadoTipoPaciente(tipoPacienteServicio.buscarTiposPacienteXViaIngreso(forma.getListadoViasIngreso().get(forma.getIndiceViaIngreso()-1).getCodigo()));
		}else{
			forma.setListadoViasIngreso(viasIngresoServicio.buscarViasIngreso());		
			forma.setListadoTipoPaciente(tipoPacienteServicio.buscarTiposPaciente());
			
			forma.setIndiceTipoPaciente(0);		
			forma.setIndiceViaIngreso(0);
		}
		
		if(!Utilidades.isEmpty(forma.getListadoTipoPaciente())){
			if(forma.getListadoTipoPaciente().size()==1)
				forma.setListarAutomatico(ConstantesBD.acronimoSi);
			else
				forma.setListarAutomatico(ConstantesBD.acronimoNo);
		}
		
		
		
			
		return mapping.findForward("mostrarPopupNuevoRegistro");
	}
	
	/**
	 * 
	 * Este Método se encarga de agregar un nuevo detalle al 
	 * monto de cobro seleccionado por el usuario
	 * 
	 * @param IngresarModificarMontosCobroForm forma,ActionMapping mapping
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward nuevo(IngresarModificarMontosCobroForm forma,
			ActionMapping mapping,HttpServletRequest request){	
		
		int indiceViaIngreso = forma.getIndiceViaIngreso();
		int indiceTipoPaciente = forma.getIndiceTipoPaciente();
		boolean agregaNuevo=false;
		ActionErrors errores = new ActionErrors();
		
		if((indiceViaIngreso)<=0){
			errores.add("La Vía de ingreso para el nuevo detalle es requerido", 
					new ActionMessage("errores.modFacturacionMontosCobroViaIngresoRequerida"));
		}
		if((indiceTipoPaciente)<=0){
			errores.add("El tipo de paciente para el nuevo detalle es requerido", 
				new ActionMessage("errores.modFacturacionMontosCobroTipoPacienteRequerido"));
		}
		if(errores!=null && errores.size()>0){
			saveErrors(request, errores);
		}else{
			String nombreViaIngreso=forma.getListadoViasIngreso().get(indiceViaIngreso-1).getNombre();
			String nombreTipoPaciente=forma.getListadoTipoPaciente().get(indiceTipoPaciente-1).getNombre();
			
			int codigoViaIngreso =forma.getListadoViasIngreso().get(indiceViaIngreso-1).getCodigo();
			String acrTipoPaciente = forma.getListadoTipoPaciente().get(indiceTipoPaciente-1).getAcronimo();			
			
			DTOResultadoBusquedaDetalleMontos detalle = new DTOResultadoBusquedaDetalleMontos();
			detalle.setManejaDetalle(false);
			detalle.setTipoDetalleAcronimo("");
			detalle.setPermiteEliminar(true);
			detalle.setViaIngresoID(codigoViaIngreso);
			detalle.setTipoPacienteAcronimo(acrTipoPaciente);	
			detalle.setIdMontoCobro(forma.getDtoConvenioSeleccionado().getMontoCobroID());
			detalle.setNaturalezaID(ConstantesBD.codigoNuncaValido);
			
			if(forma.getListaMontosCobro()!=null && forma.getListaMontosCobro().size()>0){
				for(DTOMontosCobro monto: forma.getListaMontosCobro()){
					if(codigoViaIngreso==monto.getViaIngresoID() 
							&&acrTipoPaciente.equals(monto.getTipoPacienteAcronimo())){					
						monto.getListaDetalles().add(detalle);
						agregaNuevo=true;
					}			
				}
			}else{
				forma.setListaMontosCobro(new ArrayList<DTOMontosCobro>());
			}
					
			if(!agregaNuevo){
				DTOMontosCobro monto = new DTOMontosCobro();			
				monto.setViaIngresoNombre(nombreViaIngreso);
				monto.setTipoPacienteNombre(nombreTipoPaciente);
				monto.setViaIngresoID(codigoViaIngreso);
				monto.setTipoPacienteAcronimo(acrTipoPaciente);
				monto.setListaDetalles(new ArrayList<DTOResultadoBusquedaDetalleMontos>());							
				monto.getListaDetalles().add(detalle);
				forma.getListaMontosCobro().add(monto);
			}				
		}				
		return mapping.findForward("listadoMontos");	
	}
	
	/**
	 * 
	 * Este Método se encarga de eliminar el monto de cobro
	 * seleccionado por el usuario
	 * 
	 * @param IngresarModificarMontosCobroForm forma, ActionMapping mapping
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void eliminar(IngresarModificarMontosCobroForm forma,ActionMapping mapping,
			HttpServletRequest request){
		IDetalleMontoServicio detalleServicio = FacturacionServicioFabrica.crearDetalleMontoServicio();
		int indiceDetalleMonto = forma.getIndiceDetalle();
		int j=0;
		boolean terminar=false;
		
		for(DTOMontosCobro monto: forma.getListaMontosCobro()){
			if(terminar){
				break;
			}
			for(int i =0; i<monto.getListaDetalles().size();i++){
				if(indiceDetalleMonto==j){
					if(monto.getListaDetalles().get(i).getDetalleCodigo()>0){
						detalleServicio.eliminarDetalleMontoCobro(monto.getListaDetalles().get(i)
									.getDetalleCodigo());												
						terminar=true;
						break;
					}else{
						monto.getListaDetalles().remove(i);
						terminar=true;
						break;
					}
				}
				j++;
			}
		}		
		forma.setMostrarMensaje("resumen");		
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los convenios registrados en el sistema
	 * 
	 * @param IngresarModificarMontosCobroForm forma,ActionMapping mapping
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward empezar(IngresarModificarMontosCobroForm forma,ActionMapping mapping){
		IConvenioServicio convenioServicio = FacturacionServicioFabrica.
			crearConvenioServicio();		
		forma.reset();
		forma.setListaConvenios(convenioServicio.obtenerConveniosManejanMonto());		
		return mapping.findForward("empezar");				
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar la fecha mayor que el 
	 * convenio seleccionado por el usuario tenga registrada en los 
	 * montos de cobro 
	 * 
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward postularFecha(IngresarModificarMontosCobroForm forma, ActionMapping mapping){
		IMontosCobroServicio montoCobroServicio = FacturacionServicioFabrica.crearMontosCobroServicio();
		IConvenioServicio convenioServicio = FacturacionServicioFabrica.crearConvenioServicio(); 
		
		if(forma.getDtoConvenioSeleccionado().getFechaVigenciaConvenio()!=null ){
			forma.getDtoConvenioSeleccionado().setFechaVigenciaConvenio(null);
		}
		if(forma.getDtoConvenioSeleccionado().getConvenio().getCodigo() != 
			ConstantesBD.codigoNuncaValido){
			DtoConvenio dtoConvenio = convenioServicio.buscarConvenio(
					forma.getDtoConvenioSeleccionado().getConvenio().getCodigo());
			if(dtoConvenio!=null){
				forma.getDtoConvenioSeleccionado().setConvenio(dtoConvenio);
				DTOMontosCobro montoSeleccionado= montoCobroServicio.obtenerFechaMaximaMonto(
						dtoConvenio.getCodigo());
				
				if(montoSeleccionado!= null ){
					montoSeleccionado.setConvenio(dtoConvenio);
					forma.setDtoConvenioSeleccionado(montoSeleccionado);
					forma.setMostrarBotonDetalle(true);				
				}else{
					forma.setMostrarBotonDetalle(false);	
				}
			}
		}else{
			forma.setMostrarBotonDetalle(false);	
		}
						
		return mapping.findForward("empezar");
	}	
		
	/**
	 * 
	 * Este Método se encarga de consultar los montos de cobro
	 * relacinados con el convenio seleccionado
	 * 
	 * @param IngresarModificarMontosCobroForm forma, 
			  UsuarioBasico usuario, ActionMapping mapping
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward buscarMontos(IngresarModificarMontosCobroForm forma, 
			UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request){
		
		IMontosCobroServicio montoServicio = FacturacionServicioFabrica.
			crearMontosCobroServicio();		
		
		ArrayList<DTOMontosCobro> listaMontosCobro = 
			montoServicio.obtenerMontosCobroEstructurado(forma.getDtoConvenioSeleccionado(),usuario);		
	
		if(listaMontosCobro!=null && listaMontosCobro.size()>0){
			forma.setLongitudListaInical(listaMontosCobro.size());
			forma.setListaMontosCobro(listaMontosCobro);	
			forma.getDtoConvenioSeleccionado().setMontoCobroID(listaMontosCobro.get(0).
					getListaDetalles().get(0).getIdMontoCobro());
		}else{
			forma.setMostrarMensaje("sinDatos");
			forma.setListaMontosCobro(null);
			forma.getDtoConvenioSeleccionado().setMontoCobroID(0);
		}		
		
		llenarListasEncabezadoMontoDetalle(forma);
		
		return mapping.findForward("listadoMontos");
	}
	
	
	/**
	 * 
	 * Este Método se encarga de llenar las listas neceserias para el
	 * registro de un nuevo monto
	 * 
	 * @param IngresarModificarMontosCobroForm forma
	 * @author, Angela Maria Aguirre
	 *
	 */
	private void llenarListasEncabezadoMontoDetalle(IngresarModificarMontosCobroForm forma){
		IEstratoSocialServicio estratoServicio = ManejoPacienteServicioFabrica.
			crearEstratoSocialServicio();
		
		ITiposAfiliadoServicio tiposAfiliadoServicio = ManejoPacienteServicioFabrica.crearTiposAfiliadoServicio();
		
		ITiposMontoServicio tipoMontoServicio = FacturacionServicioFabrica.crearTiposMontoServicio();
		
		String acronimoRegimenConvenio=forma.getDtoConvenioSeleccionado().getConvenio().getAcronimoTipoRegimen();
		
		Connection conn=UtilidadBD.abrirConexion();
				
		String[] listadoIntegridadDOminio=new String[]{ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroDET,
				ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN};
		
		ArrayList<DtoIntegridadDominio> listaTiposDetalleMonto=Utilidades.
		generarListadoConstantesIntegridadDominio(conn, listadoIntegridadDOminio, false);
		
		if(listaTiposDetalleMonto!=null && listaTiposDetalleMonto.size()>0){
			forma.setListaTiposDetalleMonto(listaTiposDetalleMonto);
		}		
		
		UtilidadBD.closeConnection(conn);	
		
		forma.setListadoTipoAfiliado(tiposAfiliadoServicio.obtenerTiposAfiliado());
		
		forma.setListadoEstratoSocial(estratoServicio.consultarEstratosSocilaesPorRegimen(acronimoRegimenConvenio));
				
		ListarNaturalezaPaciente(forma);
		
		forma.setListaTiposMonto(tipoMontoServicio.consultarTiposMonto());	
		
	}	
	
	/**
	 * 
	 * Este Método se encarga de consutlar las naturalezas de paciente
	 * asociadas al tipo de régimen del convenio seleccionado por el usuario
	 * 
	 * @param IngresarModificarMontosCobroForm forma
	 * @author, Angela Maria Aguirre
	 *
	 */
	private void ListarNaturalezaPaciente(IngresarModificarMontosCobroForm forma){		
		IExcepcionesNaturalezaServicio excepcionServicio = ManejoPacienteServicioFabrica.
		crearExcepcionNaturalezaServicio();
		ExcepcionesNaturaleza excepcion= new ExcepcionesNaturaleza();
		TiposRegimen tipoRegimen = new TiposRegimen();
	
		tipoRegimen.setAcronimo(forma.getDtoConvenioSeleccionado()
				.getConvenio().getAcronimoTipoRegimen());
		excepcion.setTiposRegimen(tipoRegimen);
		forma.setListaExcepcionesNaturaleza(excepcionServicio.
				consultarExcepcionNaturalezaPorCampos(excepcion));
		
	}
	
	/**
	 * 
	 * Este Método se encarga de validar la cantidad de montos, el valor o el porcentaje
	 * del detalle general sean diferentes de nulos y mayores a 0.
	 * 
	 * @param DTOResultadoBusquedaDetalleMontos registroValidar
	 * @return ActionErrors
	 * @author, Angela Maria Aguirre
	 *
	 */
	private ActionErrors validarDetalleGeneral(DTOResultadoBusquedaDetalleMontos registroValidar,
			ActionErrors errores, int indice){
				
		if(registroValidar.getCantidadMonto()==null || registroValidar.getCantidadMonto()<=0){
			errores.add("error cantidad requerida", new ActionMessage(
			"errores.modFacturacionMontosCobroCantidadRequerida",indice+1));
		}		
		if((registroValidar.getValor()==null ||(registroValidar.getValor().doubleValue())<0)
				&& (registroValidar.getPorcentaje()==null || (registroValidar.getPorcentaje().doubleValue())<0)){
			errores.add("error valor o porcentaje requerido", new ActionMessage(
					"errores.modFacturacionMontosCobroValorPorcentajeRequerido",indice+1));
		}
		if((registroValidar.getValor()!=null && registroValidar.getValor()>=0)
				&& (registroValidar.getPorcentaje()!=null && registroValidar.getPorcentaje()>=0)){
			errores.add("error valor o porcentaje requerido", new ActionMessage(
					"errores.modFacturacionMontosCobroValorYPorcentajeNoRequerido",indice+1));
		}
		if(registroValidar.getPorcentaje()!=null && registroValidar.getPorcentaje()>100){
			errores.add("error porcentaje mayor a 100", new ActionMessage(
			"errores.modFacturacionMontosCobroPorcentajeMayorACien",indice+1));
		}
				
		return errores;		
	}
	
	/**
	 * 
	 * Este Método se encarga de validar los campos requeridos para el detalle
	 * del monto seleccionado.
	 * 
	 * @param ArrayList<DTOMontosCobro> listaMontos
	 * @return ActionErrors
	 * @author, Angela Maria Aguirre
	 *
	 */
	private ActionErrors validarDetalle(ArrayList<DTOMontosCobro> listaMontos){
		ActionErrors errores = new ActionErrors();
		int indice =0;
		for(DTOMontosCobro monto: listaMontos){
			if(monto.getListaDetalles()!=null && monto.getListaDetalles().size()>0){
				for(DTOResultadoBusquedaDetalleMontos detalle : monto.getListaDetalles()){
					
					if(!UtilidadTexto.isEmpty(detalle.getTipoDetalleAcronimo())){
						if(detalle.getTipoDetalleAcronimo().equals(
							ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN)){						
							validarDetalleGeneral(detalle,errores,indice);							
						}						
					}else{
						errores.add("Tipo de detalle de monto es requerido", new ActionMessage(
						"errores.modFacturacionMontosCobroTipoDetalleMontoRequerido",indice+1));
					}					
					
					if((detalle.getTipoAfiliadoAcronimo()!=ConstantesBD.codigoTipoAfiliadoCotizante.charAt(0))
							&& (detalle.getTipoAfiliadoAcronimo()!=ConstantesBD.codigoTipoAfiliadoBeneficiario.charAt(0))){
						errores.add("Tipo de afiliado es requerido", new ActionMessage(
						"errores.modFacturacionMontosCobroTipoAfiliadoRequerido",indice+1));
					}
					if(detalle.getEstratoID()==ConstantesBD.codigoNuncaValido){
						errores.add("El estrato es requerido", new ActionMessage(
						"errores.modFacturacionMontosCobroEstratoRequerido",indice+1));
					}
					if(detalle.getTipoMontoID()==ConstantesBD.codigoNuncaValido){
						errores.add("Tipo de monto es requerido", new ActionMessage(
						"errores.modFacturacionMontosCobroTipoMontoRequerido",indice+1));
					}
					indice++;
				}
			}
		}
		boolean igual = false;
		int j=0;
		int i=0;
		if(errores.isEmpty()){
			for(DTOMontosCobro monto: listaMontos){
				j=0;
				for(DTOResultadoBusquedaDetalleMontos detalleValidar : monto.getListaDetalles()){
					if(igual){
						break;
					}
					i=0;					
					for(DTOResultadoBusquedaDetalleMontos detalle : monto.getListaDetalles()){
						if(igual){
							break;
						}
						if(i!=j){
							if(detalleValidar.getTipoAfiliadoAcronimo() == detalle.getTipoAfiliadoAcronimo()&&
									detalleValidar.getEstratoID()==detalle.getEstratoID()&&									
									detalleValidar.getTipoMontoID() == detalle.getTipoMontoID() &&
									detalleValidar.getTipoDetalleAcronimo().equals(detalle.getTipoDetalleAcronimo())){	
								if(detalleValidar.getTipoDetalleAcronimo().equals(
										ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN)){
									
									if(detalleValidar.getCantidadMonto().equals(detalle.getCantidadMonto())){
										
										if(detalleValidar.getValor()==null ){
											if(detalle.getValor()==null){
												if(detalleValidar.getPorcentaje()==null){
													if(detalle.getPorcentaje()==null){
														igual=true;
													}
												}else{
													if(detalle.getPorcentaje()!=null){
														if(detalleValidar.getPorcentaje().equals(detalle.getPorcentaje())){
															igual=true;
														}
													}
												}
											}
										}else{										
											if(detalle.getValor()!=null){
												if(detalleValidar.getValor().equals(detalle.getValor())){
													igual=true;
												}
											}													
										}
									}
									
									if(detalleValidar.getNaturalezaID()!=null && detalle.getNaturalezaID()!=null){
										if(!(detalleValidar.getNaturalezaID().equals(detalle.getNaturalezaID()))){
											if(igual){
												igual=false;
											}
										}										
									}else{
										if((detalleValidar.getNaturalezaID()==null && detalle.getNaturalezaID()!=null)||
											(detalleValidar.getNaturalezaID()!=null && detalle.getNaturalezaID()==null)){
											if(igual){
												igual=false;
											}
										}
									}
									
								}else{
									igual=true;
								}
							}
						}	
						if(igual){
							break;
						}
						i++;
					}					
					j++;
				}
					
			}
		}			
		if(igual){
			errores.add("El monto es igual a otro existente", new ActionMessage(
			"errores.modFacturacionMontosCobroMontoIgual"));
		}
		return errores;
	}
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward mostrarCamposDetalleGeneral(IngresarModificarMontosCobroForm forma,
			ActionMapping mapping,boolean cambiaNaturaleza){
		
		int indiceDetalleMonto = forma.getIndiceDetalle();
		int j=0;
		boolean terminar=false;
		
		for(DTOMontosCobro monto: forma.getListaMontosCobro()){
			if(terminar){
				break;
			}
			for(int i =0; i<monto.getListaDetalles().size();i++){
				if(indiceDetalleMonto==j){					
					if(cambiaNaturaleza){
						if(monto.getListaDetalles().get(i).getNaturalezaID()!=
							ConstantesBD.codigoNuncaValido){
							monto.getListaDetalles().get(i).setTipoDetalleAcronimo(
									ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN);		
							monto.getListaDetalles().get(i).setTipoDetalle((String)ValoresPorDefecto.getIntegridadDominio(
									ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN));
							
						}else{
							monto.getListaDetalles().get(i).setTipoDetalleAcronimo(null);
							monto.getListaDetalles().get(i).setTipoDetalle(null);
						}
					}else{
						if(monto.getListaDetalles().get(i).getTipoDetalleAcronimo().equals(
								ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN)){
							monto.getListaDetalles().get(i).setTipoDetalle((String)ValoresPorDefecto.getIntegridadDominio(
									ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN));							
							
						}else{
							if(monto.getListaDetalles().get(i).getTipoDetalleAcronimo().equals(
									ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroDET)){
								monto.getListaDetalles().get(i).setTipoDetalle((String)ValoresPorDefecto.getIntegridadDominio(
										ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroDET));									
								
							}else{
								monto.getListaDetalles().get(i).setTipoDetalleAcronimo(null);
								monto.getListaDetalles().get(i).setTipoDetalle(null);
							}																					
						}
					}																												
					terminar=true;
					break;					
				}
				j++;
			}
		}		
		return mapping.findForward("listadoMontos");
	}
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward mostrarDetalle(IngresarModificarMontosCobroForm forma,
			ActionMapping mapping, UsuarioBasico usuarioSesion){
		IDetalleMontoServicio detalleServicio = FacturacionServicioFabrica.crearDetalleMontoServicio();
		int indiceDetalleMonto = forma.getIndiceDetalle();
		int j=0;
		boolean terminar=false;
		forma.setDetalleSeleccionado(new DTOResultadoBusquedaDetalleMontos());
		DTOResultadoBusquedaDetalleMontos detalleSeleccionado= new DTOResultadoBusquedaDetalleMontos();
		DTOResultadoBusquedaDetalleMontos detalleSeleccionadoAuxiliar= null;
		
		for(DTOMontosCobro monto: forma.getListaMontosCobro()){
			if(terminar){
				break;
			}
			for(int i =0; i<monto.getListaDetalles().size();i++){
				if(indiceDetalleMonto==j){
					detalleSeleccionado=monto.getListaDetalles().get(i);
					detalleSeleccionadoAuxiliar = detalleServicio.buscarDetalleMonto(
							detalleSeleccionado.getDetalleCodigo(), usuarioSesion);
					if(detalleSeleccionadoAuxiliar!=null){
						detalleSeleccionado.setListaAgrupacionArticulo(
								detalleSeleccionadoAuxiliar.getListaAgrupacionArticulo());
						detalleSeleccionado.setListaAgrupacionServicios(
								detalleSeleccionadoAuxiliar.getListaAgrupacionServicios());
						detalleSeleccionado.setListaArticuloEspecifico(
								detalleSeleccionadoAuxiliar.getListaArticuloEspecifico());
						detalleSeleccionado.setListaServicioEspecifico(
								detalleSeleccionadoAuxiliar.getListaServicioEspecifico());
					}
					forma.setDetalleSeleccionado(detalleSeleccionado);					
					terminar=true;
					break;
				}
				j++;
			}
		}
		llenarListasMontoDetallado(forma);	
		return mapping.findForward("mostrarDetalle");		
	}
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward guardarDetalle(IngresarModificarMontosCobroForm forma,
			ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioSesion){
		IDetalleMontoServicio detalleServicio = FacturacionServicioFabrica.crearDetalleMontoServicio();
		ActionErrors errores = new ActionErrors();
		DTOResultadoBusquedaDetalleMontos dtoDetalle = forma.getDetalleSeleccionado();
		DTOResultadoBusquedaDetalleMontos detalleSeleccionadoAuxiliar = null;
		
		errores = validarRegistro(forma);
		
		if(errores!=null && errores.size()>0){
			saveErrors(request, errores);
		}else{
			detalleServicio.guardarDetalleMontoCobro(dtoDetalle,usuarioSesion);
			UtilidadTransaccion.getTransaccion().commit();
			detalleSeleccionadoAuxiliar = detalleServicio.buscarDetalleMonto(
					dtoDetalle.getDetalleCodigo(), usuarioSesion);
			if(detalleSeleccionadoAuxiliar!=null){
				dtoDetalle.setListaAgrupacionArticulo(
						detalleSeleccionadoAuxiliar.getListaAgrupacionArticulo());
				dtoDetalle.setListaAgrupacionServicios(
						detalleSeleccionadoAuxiliar.getListaAgrupacionServicios());
				dtoDetalle.setListaArticuloEspecifico(
						detalleSeleccionadoAuxiliar.getListaArticuloEspecifico());
				dtoDetalle.setListaServicioEspecifico(
						detalleSeleccionadoAuxiliar.getListaServicioEspecifico());				
				dtoDetalle.setManejaDetalle(true);								
			}
			forma.setDetalleSeleccionado(dtoDetalle);
			forma.setMostrarMensaje("resumen");			
		}		
		return mapping.findForward("mostrarDetalle");		
	}
	
	
	/**
	 * 
	 * Este Método se encarga de Validar que no exista otro registro
	 * igual al que se está tratando de guardar
	 * 
	 * @param IngresarModificarMontosCobroForm forma
	 * @return ActionErrors
	 *   
	 * @author, Angela Maria Aguirre
	 *
	 */
	private ActionErrors validarRegistro(IngresarModificarMontosCobroForm forma){		
		ActionErrors errores = new ActionErrors();
		MessageResources mensajes=MessageResources.getMessageResources(
				"com.servinte.mensajes.facturacion.ParametrizarDetalleMontoCobroForm");
		int indice=0;
		
		DTOResultadoBusquedaDetalleMontos dto= forma.getDetalleSeleccionado();
		
		if((dto.getListaAgrupacionArticulo()!=null && dto.getListaAgrupacionArticulo().size()>0)||
				(dto.getListaAgrupacionServicios()!=null && dto.getListaAgrupacionServicios().size()>0)||
				(dto.getListaArticuloEspecifico()!=null && dto.getListaArticuloEspecifico().size()>0)||
				(dto.getListaServicioEspecifico()!=null && dto.getListaServicioEspecifico().size()>0)){
			
			if(dto.getListaAgrupacionArticulo()!=null && dto.getListaAgrupacionArticulo().size()>0){
				for( DTOBusquedaMontoAgrupacionArticulo registroValidar :  dto.getListaAgrupacionArticulo()){
					
					if(registroValidar.getCantidadArticulo() !=null && 
							registroValidar.getCantidadArticulo()<=0){
						errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
							mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroAgrupacionArticuloCantidadArticuloNoPermitida",indice+1)));
					}					
					
					if(registroValidar.getCantidadMonto()==null){
						errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroAgrupacionArticuloCantidadMontoVacio",indice+1)));
						
					}else{
						if(registroValidar.getCantidadMonto()<=0){
							errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroAgrupacionArticuloCantidadMontoNoPermitida",indice+1)));
						}
					}
					
					if(registroValidar.getValorMonto()==null || registroValidar.getValorMonto()== 0.0){
						errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroAgrupacionArticuloValorMontoVacio",indice+1)));
					}else{
						if(registroValidar.getValorMonto()<0){
							errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroAgrupacionArticuloValorMontoNoPermitido",indice+1)));
						}
					}
					
					int j=0;
					for( DTOBusquedaMontoAgrupacionArticulo registro :  dto.getListaAgrupacionArticulo()){
						if(j>indice){
							if(registroValidar.equals(registro)){
								errores.add("Registro Repetido", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroAgrupacionArticuloRepetido",j+1)));
							}
						}
						j++;
					}						
					indice++;				
				}
				indice=0;	
			}	
			
			if(dto.getListaAgrupacionServicios()!=null && dto.getListaAgrupacionServicios().size()>0){
				for( DTOBusquedaMontoAgrupacionServicio registroValidar : dto.getListaAgrupacionServicios()){
					
					if(registroValidar.getCantidadServicio()!=null && 
							registroValidar.getCantidadServicio()<=0){
						errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroAgrupacionServicioCantidadServicioNoPermitida",indice+1)));
					}					
					
					if(registroValidar.getCantidadMonto()==null){
						errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
							mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroAgrupacionServicioCantidadMontoVacio",indice+1)));						
					}else{
						if(registroValidar.getCantidadMonto()<=0){
							errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroAgrupacionServicioCantidadMontoNoPerimitda",indice+1)));
						}
					}
					
					if(registroValidar.getValorMonto()==null){
						errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
							mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroAgrupacionServicioValorMontoVacio",indice+1)));						
					}else{
						if(registroValidar.getValorMonto()<0){
							errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroAgrupacionServicioValorMontoNoPermitido",indice+1)));
						}
					}
					
					int j=0;
					for( DTOBusquedaMontoAgrupacionServicio registro : dto.getListaAgrupacionServicios()){
						if(j>indice){
							if(registroValidar.equals(registro)){
								errores.add("Registro Repetido", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroAgrupacionServicioRepetido",j+1)));
							}
						}
						j++;
					}					
					indice++;
				}		
				indice=0;
			}
			
			if(dto.getListaServicioEspecifico()!=null && dto.getListaServicioEspecifico().size()>0){
				for( DTOBusquedaMontoServicioEspecifico registroValidar : dto.getListaServicioEspecifico()){
					
					if(registroValidar.getCantidadServicio()!=null &&
							registroValidar.getCantidadServicio()<=0){
						errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroServicioEspCantidadServicioEspNoPermitido",indice+1)));
					}
						
					
					if(registroValidar.getCantidadMonto()==null){
						errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroServicioEspCantidadMontoVacio",indice+1)));
					}else{
						if(registroValidar.getCantidadMonto()<=0){
							errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroServicioEspCantidadMontoNoPermitido",indice+1)));
						}
					}
					
					if(registroValidar.getValorMonto()==null){
						errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroServicioEspValorMontoVacio",indice+1)));
					}else{
						if(registroValidar.getValorMonto()<0){
							errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroServicioEspValorMontoNoPermitido",indice+1)));
						}
					}					
					
					int j=0;
					for( DTOBusquedaMontoServicioEspecifico registro : dto.getListaServicioEspecifico()){
						if(j>indice){
							if(registroValidar.equals(registro)){
								errores.add("Registro Repetido", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("parametrizarNivelAutorizacionServicioArticuloForm.registroServicioEspRepetido",j+1)));
							}
						}
						j++;
					}
					indice++;					
				}
				indice=0;
			}
			
			if(dto.getListaArticuloEspecifico()!=null && dto.getListaArticuloEspecifico().size()>0){
				for( DTOBusquedaMontoArticuloEspecifico registroValidar : dto.getListaArticuloEspecifico()){					
					
					if(registroValidar.getCantidadArticulos()!=null && 
							registroValidar.getCantidadArticulos()<=0){
						errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroArticuloEspCantidadArticuloNoPermitido",indice+1)));
					}					
					
					if(registroValidar.getCantidadMonto()==null){
						errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroArticuloEspCantidadMonto",indice+1)));
					}else{
						if(registroValidar.getCantidadMonto()<=0){
							errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroArticuloEspCantidadMontoNoPermitido",indice+1)));
						}
					}	
					
					if(registroValidar.getValorMonto()==null){
						errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroArticuloEspValorMonto",indice+1)));
					}else{
						if(registroValidar.getValorMonto()<0){
							errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroArticuloEspValorMontoNoPermitido",indice+1)));
						}
					}					
					
					int j=0;
					for( DTOBusquedaMontoArticuloEspecifico registro : dto.getListaArticuloEspecifico()){
						if(j>indice){
							if(registroValidar.equals(registro)){
								errores.add("Registro Repetido", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroArticuloEspRepetido",j+1)));
							}
						}
						j++;
					}
					indice++;					
				}
				indice=0;
			}
			
		}else{
			errores.add("Registro Vacío", new ActionMessage("errors.notEspecific", 
					mensajes.getMessage("parametrizarDetalleMontoCobroForm.registroVacio")));
		}		
		return errores;
	}
	
	
	
	/**
	 * 
	 * Este Método se encarga de 
	 * @author, Angela Maria Aguirre
	 *
	 */
	private void llenarListasMontoDetallado(IngresarModificarMontosCobroForm forma ){
		IGruposServiciosServicio gruposServicio = FacturacionServicioFabrica
			.crearGruposServiciosServicio();
		ITiposServicioServicio tipoServicio = FacturacionServicioFabrica.crearTipoServicioServicio();
		IEspecialidadesServicio especialidadServicio = AdministracionFabricaServicio.crearEspecialidadesServicio();
		IClaseInventarioServicio claseInventarioServicio = InventarioServicioFabrica.crearClaseInventarioServicio();
		IGrupoInventarioServicio grupoInventarioServicio = InventarioServicioFabrica.crearGrupoInventarioServicio();
		ISubgrupoInventarioServicio subgrupoInventarioServicio = InventarioServicioFabrica.crearSubgrupoInventarioServicio();
		INaturalezaArticuloServicio naturalezaArticuloServicio = InventarioServicioFabrica.crearNaturalezaArticuloServicio();
						
		ArrayList<GruposServicios> listaGruposServicios = 
			gruposServicio.buscarGruposServicioActivos();
		forma.setListaGruposServicios(listaGruposServicios);
		
		ArrayList<TiposServicio> listaTiposServicio = tipoServicio.buscarTiposServicio();
		forma.setListaTiposServicio(listaTiposServicio);
		
		ArrayList<Especialidades> listaEspecialidades = especialidadServicio.buscarEspecialidades();
		forma.setListaEspecialidades(listaEspecialidades);		
		
		ArrayList<ClaseInventario> listaClaseInventario = claseInventarioServicio.buscarClaseInventario();
		forma.setListaClaseInventario(listaClaseInventario);
		
		ArrayList<GrupoInventario> listaGrupoInventario = grupoInventarioServicio.buscarGrupoInventario();
		forma.setListaGrupoInventario(listaGrupoInventario);
		
		ArrayList<SubgrupoInventario> listaSubgrupoInventario = subgrupoInventarioServicio.buscarSubgrupoInventario();
		forma.setListaSubgrupoInventario(listaSubgrupoInventario);
		
		ArrayList<NaturalezaArticulo> listaNaturalezaArticulo = naturalezaArticuloServicio.buscarNaturalezaArticulo();
		forma.setListaNaturalezaArticulo(listaNaturalezaArticulo);	
	}
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward nuevoRegistroDetalle(IngresarModificarMontosCobroForm forma,
			ActionMapping mapping, String accion){
		int codigoDetalle = forma.getDetalleSeleccionado().getDetalleCodigo();
		if(accion.equals("nuevoAgrupacionServicio")){
			DTOBusquedaMontoAgrupacionServicio agrupacionServicio = new DTOBusquedaMontoAgrupacionServicio();
			agrupacionServicio.setDetalleCodigo(codigoDetalle);
			agrupacionServicio.setCodigoEspecialidad(-1);
			
			if(forma.getDetalleSeleccionado().getListaAgrupacionServicios() == null){
				forma.getDetalleSeleccionado().setListaAgrupacionServicios(
						new ArrayList<DTOBusquedaMontoAgrupacionServicio>());
			}
			
			forma.getDetalleSeleccionado().getListaAgrupacionServicios()
				.add(agrupacionServicio);
		}else{
			if(accion.equals("nuevoAgrupacionArticulo")){
				DTOBusquedaMontoAgrupacionArticulo agrupacionArticulo = new DTOBusquedaMontoAgrupacionArticulo();
				agrupacionArticulo.setDetalleCodigo(codigoDetalle);
				
				if(forma.getDetalleSeleccionado().getListaAgrupacionArticulo()==null){
					forma.getDetalleSeleccionado().setListaAgrupacionArticulo(
							new ArrayList<DTOBusquedaMontoAgrupacionArticulo>());
				}
				
				forma.getDetalleSeleccionado().getListaAgrupacionArticulo().add(
						agrupacionArticulo);
			}
		}		
		return mapping.findForward("mostrarDetalle");	
	}
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward eliminarDetalle(IngresarModificarMontosCobroForm forma,
			ActionMapping mapping, String accion, UsuarioBasico usuarioSesion){
		
		DTOResultadoBusquedaDetalleMontos dtoDetalle = forma.getDetalleSeleccionado();
		DTOResultadoBusquedaDetalleMontos detalleSeleccionadoAuxiliar = null;
		IDetalleMontoServicio detalleServicio = FacturacionServicioFabrica.crearDetalleMontoServicio();
		if(accion.equals("eliminarAgrupacionServicio")){
			
			DTOBusquedaMontoAgrupacionServicio registro=forma.getDetalleSeleccionado()
				.getListaAgrupacionServicios().get(forma.getIndiceDetalle());
			
			if(registro.getCodigoAgrupacionServicio()>0){
				IMontoAgrupacionServiciosServicio agrupacionSerservicio = 
					FacturacionServicioFabrica.crearMontoAgrupacionServicio();
				agrupacionSerservicio.eliminarAgrupacionServicio(registro.getCodigoAgrupacionServicio());
			}
			
			forma.getDetalleSeleccionado().getListaAgrupacionServicios().remove(forma.getIndiceDetalle());						
		}		
		if(accion.equals("eliminarServicioEspecifico")){
			
			DTOBusquedaMontoServicioEspecifico registro=forma.getDetalleSeleccionado()
			.getListaServicioEspecifico().get(forma.getIndiceDetalle());
			
			if(registro.getCodigoServicioEspecifico()>0){
				IMontoServicioEspecificoServicio serEspecificoServicio = FacturacionServicioFabrica
					.crearMontoServicioEspecificoServicio();
				
				serEspecificoServicio.eliminarServicioEspecifico(registro.getCodigoServicioEspecifico());
			}
			
			forma.getDetalleSeleccionado().getListaServicioEspecifico().remove(forma.getIndiceDetalle());
		}
		if(accion.equals("eliminarAgrupacionArticulo")){
			DTOBusquedaMontoAgrupacionArticulo registro= forma.getDetalleSeleccionado()
					.getListaAgrupacionArticulo().get(forma.getIndiceDetalle());
			if(registro.getCodigoAgrupacionArticulo()>0){
				IMontoAgrupacionArticuloMundo agrupacionArticuloMundo = FacturacionFabricaMundo
				.crearMontoAgrupacionArticuloMundo();
				DTOBusquedaMontoAgrupacionArticulo dto = new DTOBusquedaMontoAgrupacionArticulo();
				dto.setCodigoAgrupacionArticulo(registro.getCodigoAgrupacionArticulo());
				agrupacionArticuloMundo.eliminarMontoAgrupacionArticulo(dto);
				
			}
				
			forma.getDetalleSeleccionado().getListaAgrupacionArticulo().remove(forma.getIndiceDetalle());
			
		}
		if(accion.equals("eliminarArticuloEspecifico")){
			DTOBusquedaMontoArticuloEspecifico registro= forma.getDetalleSeleccionado()
			.getListaArticuloEspecifico().get(forma.getIndiceDetalle());
			if(registro.getCodigo()>0){
				IMontoArticuloEspecificoServicio articuloEspecificoServicio = 
					FacturacionServicioFabrica.crearMontoArticuloEspecificoServicio();
				
				articuloEspecificoServicio.eliminarArticuloEspecifico(registro.getCodigo());
			}
				
			forma.getDetalleSeleccionado().getListaArticuloEspecifico().remove(forma.getIndiceDetalle());						
		}
		UtilidadTransaccion.getTransaccion().commit();
		
		/*detalleSeleccionadoAuxiliar = detalleServicio.buscarDetalleMonto(
				dtoDetalle.getDetalleCodigo(), usuarioSesion);
		if(detalleSeleccionadoAuxiliar!=null){
			dtoDetalle.setListaAgrupacionArticulo(
					detalleSeleccionadoAuxiliar.getListaAgrupacionArticulo());
			dtoDetalle.setListaAgrupacionServicios(
					detalleSeleccionadoAuxiliar.getListaAgrupacionServicios());
			dtoDetalle.setListaArticuloEspecifico(
					detalleSeleccionadoAuxiliar.getListaArticuloEspecifico());
			dtoDetalle.setListaServicioEspecifico(
					detalleSeleccionadoAuxiliar.getListaServicioEspecifico());
		}
		forma.setDetalleSeleccionado(dtoDetalle);*/
		
		if((Utilidades.isEmpty(forma.getDetalleSeleccionado().getListaAgrupacionServicios())) && 
				(Utilidades.isEmpty(forma.getDetalleSeleccionado().getListaAgrupacionArticulo())) && 
				(Utilidades.isEmpty(forma.getDetalleSeleccionado().getListaArticuloEspecifico())) && 
				(Utilidades.isEmpty(forma.getDetalleSeleccionado().getListaServicioEspecifico()))){
			
			forma.getDetalleSeleccionado().setManejaDetalle(false);
		}
		
		forma.setMostrarMensaje("resumen");
		return mapping.findForward("mostrarDetalle");
	}	
	
	/**
	 * 
	 * Este Método se encarga de consultar los grupos de 
	 * inventario por la clase de inventario seleccionada
	 * 
	 * @param ParametrizarNivelAutorizacionServicioArticuloForm, 
	 * ActionMapping mapping
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	private ActionForward cargarGrupoInventario(IngresarModificarMontosCobroForm forma, 
			ActionMapping mapping){
		IGrupoInventarioServicio grupoInventarioServicio = InventarioServicioFabrica.crearGrupoInventarioServicio();
		int idClaseSeleccionada = forma.getDetalleSeleccionado()
			.getListaAgrupacionArticulo().get(forma.getIndiceDetalle()).getClaseInventarioCodigo();
		
		forma.getDetalleSeleccionado().getListaAgrupacionArticulo().get(forma.getIndiceDetalle())
			.setGrupoCodigoConcatenado("");		
		forma.getDetalleSeleccionado().getListaAgrupacionArticulo().get(forma.getIndiceDetalle())
			.setCodigoSubgrupoInventario(ConstantesBD.codigoNuncaValido);
		
		if(idClaseSeleccionada!=ConstantesBD.codigoNuncaValido){
			ArrayList<GrupoInventario> listaGrupoInventario = grupoInventarioServicio.buscarGrupoInventarioPorClase(idClaseSeleccionada);
			forma.getDetalleSeleccionado().getListaAgrupacionArticulo().get(forma.getIndiceDetalle())
				.setListaGrupoInventario(listaGrupoInventario);
		}else{			
			forma.getDetalleSeleccionado().getListaAgrupacionArticulo()
			.get(forma.getIndiceDetalle()).setListaSubgrupoInventario(new ArrayList<SubgrupoInventario>());
			
			forma.getDetalleSeleccionado().getListaAgrupacionArticulo()
			.get(forma.getIndiceDetalle()).setListaGrupoInventario(new ArrayList<GrupoInventario>());
		}		
		return mapping.findForward("mostrarDetalle");	
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los subgrupos de 
	 * inventario por el grupo de inventario seleccionada
	 * 
	 * @param ParametrizarNivelAutorizacionServicioArticuloForm, 
	 * ActionMapping mapping
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	private ActionForward cargarSubgrupoInventario(IngresarModificarMontosCobroForm forma, 
			ActionMapping mapping){
		ISubgrupoInventarioServicio subgrupoInventarioServicio = InventarioServicioFabrica.crearSubgrupoInventarioServicio();
		
		DTOBusquedaMontoAgrupacionArticulo dtoAgrArticulo = forma.getDetalleSeleccionado()
			.getListaAgrupacionArticulo().get(forma.getIndiceDetalle());
		
		forma.getDetalleSeleccionado().getListaAgrupacionArticulo().get(forma.getIndiceDetalle())
			.setCodigoSubgrupoInventario(ConstantesBD.codigoNuncaValido);
	
		
		if(!UtilidadTexto.isEmpty(dtoAgrArticulo.getGrupoCodigoConcatenado())){
			String[] codigos = new String[2];
			codigos = (dtoAgrArticulo.getGrupoCodigoConcatenado()).split("-");
			int codigoGrupo=Integer.valueOf(codigos[0]).intValue();
			
			ArrayList<SubgrupoInventario> listaSrupoInventario = subgrupoInventarioServicio.buscarSubgrupoInventarioPorGrupoID(codigoGrupo);
			forma.getDetalleSeleccionado().getListaAgrupacionArticulo()
				.get(forma.getIndiceDetalle()).setListaSubgrupoInventario(listaSrupoInventario);
		}else{
			forma.getDetalleSeleccionado().getListaAgrupacionArticulo()
			.get(forma.getIndiceDetalle()).setListaSubgrupoInventario(new ArrayList<SubgrupoInventario>());
		}
		return mapping.findForward("mostrarDetalle");	
	}	

}
