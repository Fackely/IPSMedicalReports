package com.princetonsa.action.capitacion;

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
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.capitacion.ParametrizarNivelAutorizacionServicioArticuloForm;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionArticulo;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionServicio;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorArticuloEspecifico;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorServicioEspecifico;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionServicioArticulo;
import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.bl.administracion.facade.AdministracionFacade;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.GrupoInventario;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.NaturalezaArticulo;
import com.servinte.axioma.orm.SubgrupoInventario;
import com.servinte.axioma.orm.TiposServicio;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.capitacion.CapitacionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.inventario.InventarioServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IEspecialidadesServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionAgrupacionArticuloServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionAgrupacionServicioServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionArticuloEspecificoServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionServicioArticuloServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionServicioEspecificoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IGruposServiciosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ITiposServicioServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IClaseInventarioServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IGrupoInventarioServicio;
import com.servinte.axioma.servicio.interfaz.inventario.INaturalezaArticuloServicio;
import com.servinte.axioma.servicio.interfaz.inventario.ISubgrupoInventarioServicio;

/**
 * Esta clase se encarga de procesar las solicitudes de 
 * la parametrización de los niveles de autorización por
 * servicios y artículos
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class ParametrizarNivelAutorizacionServicioArticuloAction extends Action {
	
	/**
	 * 
	 * Este Método se encarga de ejecutar las acciones sobre las páginas
	 * de parametrización de niveles de autorización por servicios y
	 * artículos
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
		
		ActionForward forward=null;
		if (form instanceof ParametrizarNivelAutorizacionServicioArticuloForm) {
			ParametrizarNivelAutorizacionServicioArticuloForm forma = (ParametrizarNivelAutorizacionServicioArticuloForm)form;
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);
			forma.setMensaje("");
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());
			UtilidadTransaccion.getTransaccion().begin();
			try{
				if(estado.equals("empezar")){
					forma.reset();					
					llenarListas(forma);					
					forward= empezar(forma, mapping);
				}else{
					if(estado.equals("buscar")){
						forward= buscarNivelAutorizacionServicioArticulo(usuario,forma, mapping);
					}else{			
						
						if((estado.equals("nuevoAgrArticulo"))||(estado.equals("nuevoAgrServicio"))){
							forward= nuevoRegistro(forma, mapping);
						}else{
							if((estado.equals("eliminarAgrServicio")) || (estado.equals("eliminarAgrArticulo")) ||
									(estado.equals("eliminarArticuloEsp")) || (estado.equals("eliminarServicioEsp")) ){
								forward= eliminarRegistro(forma, mapping,usuario);
							}else{
								if(estado.equals("guardar")){
									forward= guardarRegistro(forma, mapping,request,usuario);
								}else{
									if(estado.equals("recarga"))
									{
										DTOBusquedaNivelAutorServicioEspecifico dto=new DTOBusquedaNivelAutorServicioEspecifico();
										dto.setCodigoServicio(forma.getCodigoServicio());
										dto.setNombreServicio(forma.getNombreServicio());
										dto.setPermiteEliminar(true);
										dto.setNivelAutoriSerArt(forma.getNivelAtuorizacionServArtSeleccionado().getCodigoPk());
										dto.setCodigoPk(ConstantesBD.codigoNuncaValido);
										if(forma.getNivelAtuorizacionServArtSeleccionado().getListaServicioEsp()==null){
											forma.getNivelAtuorizacionServArtSeleccionado().setListaServicioEsp(
													new ArrayList<DTOBusquedaNivelAutorServicioEspecifico>());
										}
										forma.getNivelAtuorizacionServArtSeleccionado().getListaServicioEsp().add(dto);
										forward= mapping.findForward("paginaParametrizacion");	
									}else{
										if(estado.equals("recargaArticulo"))
										{
											DTOBusquedaNivelAutorArticuloEspecifico dto=new DTOBusquedaNivelAutorArticuloEspecifico();
											dto.setArticuloCodigo(forma.getCodigoArticulo());
											dto.setArticuloDescripcion(forma.getDescripcionArticulo());
											dto.setNivelAutoriSerArt(forma.getNivelAtuorizacionServArtSeleccionado().getCodigoPk());
											dto.setCodigo(ConstantesBD.codigoNuncaValido);
											if(forma.getNivelAtuorizacionServArtSeleccionado().getListaArticuloEsp()==null){
												forma.getNivelAtuorizacionServArtSeleccionado().setListaArticuloEsp(
														new ArrayList<DTOBusquedaNivelAutorArticuloEspecifico>());
											}
											forma.getNivelAtuorizacionServArtSeleccionado().getListaArticuloEsp().add(dto);
											forward= mapping.findForward("paginaParametrizacion");	
										}else{
											if(estado.equals("cargarGrupo")){
												forward= cargarGrupoInventario(forma,mapping);
											}else{
												if(estado.equals("cargarSubgrupo")){
													forward= cargarSubgrupoInventario(forma,mapping);													
												}
											}
										}
									}									
								}
							}
						}
					}
				}
				UtilidadTransaccion.getTransaccion().commit();
			}catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error en la parametrización del Nivel de Autorización de Servicios / Medicamentos", e);
			}
		}
		return forward;
	}
	
	

	/**
	 /**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * registrados en el sistema
	 * 
	 * @param ParametrizarNivelAutorizacionServicioArticuloForm forma, ActionMapping mapping
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward empezar(ParametrizarNivelAutorizacionServicioArticuloForm forma,ActionMapping mapping){
		INivelAutorizacionServicio autorizacionServicio = 
			CapitacionFabricaServicio.crearNivelAutorizacionServicio();
		
		ArrayList<DTONivelAutorizacion> listaNivelAutorizacion = autorizacionServicio.buscarNivelAutorizacion();
		forma.setListaNivelAutorizacion(listaNivelAutorizacion);
		
		return mapping.findForward("principal");	
	}	
	
	/**
	 * 
	 * Este método se encarga de consultar los niveles de 
	 * autorización de servicios y artículos registrados en el sistema
	 * 
	 * @param ParametrizarNivelAutorizacionServicioArticuloForm forma,ActionMapping mapping, 
	 *        UsuarioBasico usuario
	 * @return ActionForward
	 * @author Angela Maria Aguirre
	 */
	public ActionForward buscarNivelAutorizacionServicioArticulo( UsuarioBasico usuarioSesion,
			ParametrizarNivelAutorizacionServicioArticuloForm forma,ActionMapping mapping){		
		INivelAutorizacionServicioArticuloServicio nivelServicioArticuloServicio = 
			CapitacionFabricaServicio.crearNivelAutorizacionServicioArticuloServicio();
		
		identificarNivelAutorizacionSeleccionada(forma);
		forma.setIndice(0);
		DTOBusquedaNivelAutorizacionServicioArticulo dto = null;
		
		dto = nivelServicioArticuloServicio.buscarNivelAutorizacionServicioArticuloDetallado(
				forma.getNivelAutorizacion().getCodigoPk(), usuarioSesion);
		
		if(dto==null){
			dto = new DTOBusquedaNivelAutorizacionServicioArticulo();
			dto.setNivelAutorizacionID(forma.getNivelAutorizacion().getCodigoPk());
			dto.setDescripcionNivelAutorizacion(forma.getNivelAutorizacion().getDescripcion());
			dto.setCodigoPk(ConstantesBD.codigoNuncaValido);
		}
		
		forma.setNivelAtuorizacionServArtSeleccionado(dto);
		return mapping.findForward("paginaParametrizacion");	
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los listados necesarios
	 * para la página de parametrización de Niveles de Autorización
	 * por servicios y medicamentos
	 * 
	 * @param ParametrizarNivelAutorizacionServicioArticuloForm
	 * @author, Angela Maria Aguirre
	 *
	 */
	private void llenarListas(ParametrizarNivelAutorizacionServicioArticuloForm forma){
		try{
			HibernateUtil.beginTransaction();
			IGruposServiciosServicio gruposServicio = FacturacionServicioFabrica
				.crearGruposServiciosServicio();
			ITiposServicioServicio tipoServicio = FacturacionServicioFabrica.crearTipoServicioServicio();
			IClaseInventarioServicio claseInventarioServicio = InventarioServicioFabrica.crearClaseInventarioServicio();
			
			ISubgrupoInventarioServicio subgrupoInventarioServicio = InventarioServicioFabrica.crearSubgrupoInventarioServicio();
			INaturalezaArticuloServicio naturalezaArticuloServicio = InventarioServicioFabrica.crearNaturalezaArticuloServicio();
							
			ArrayList<GruposServicios> listaGruposServicios = 
				gruposServicio.buscarGruposServicioActivos();
			forma.setListaGruposServicios(listaGruposServicios);
			
			ArrayList<TiposServicio> listaTiposServicio = tipoServicio.buscarTiposServicio();
			forma.setListaTiposServicio(listaTiposServicio);
			
			ArrayList<ClaseInventario> listaClaseInventario = claseInventarioServicio.buscarClaseInventario();
			forma.setListaClaseInventario(listaClaseInventario);
			
			ArrayList<SubgrupoInventario> listaSubgrupoInventario = subgrupoInventarioServicio.buscarSubgrupoInventario();
			forma.setListaSubgrupoInventario(listaSubgrupoInventario);
			
			ArrayList<NaturalezaArticulo> listaNaturalezaArticulo = naturalezaArticuloServicio.buscarNaturalezaArticulo();
			forma.setListaNaturalezaArticulo(listaNaturalezaArticulo);
			HibernateUtil.endTransaction();
			//Dado que COn la nueva  arquitectura la Transacción se maneja en el Mundo por esta razón
			//no se deja dentro de la transacción anterior
			AdministracionFacade administracionFacade= new AdministracionFacade();
			forma.setListaEspecialidades(administracionFacade.consultarEspecialidadesValidas());	
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
			HibernateUtil.abortTransaction();
		}
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
	private ActionForward cargarGrupoInventario(ParametrizarNivelAutorizacionServicioArticuloForm forma, 
			ActionMapping mapping){
		IGrupoInventarioServicio grupoInventarioServicio = InventarioServicioFabrica.crearGrupoInventarioServicio();
		int idClaseSeleccionada = forma.getNivelAtuorizacionServArtSeleccionado()
			.getListaAgrArticulo().get(forma.getIndice()).getClaseInventario();
		
		forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrArticulo().get(forma.getIndice())
			.setGrupoCodigoConcatenado("");		
		forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrArticulo().get(forma.getIndice())
			.setSubgrupoInventario(ConstantesBD.codigoNuncaValido);
		
		if(idClaseSeleccionada!=ConstantesBD.codigoNuncaValido){
			ArrayList<GrupoInventario> listaGrupoInventario = grupoInventarioServicio.buscarGrupoInventarioPorClase(idClaseSeleccionada);
			
			forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrArticulo().get(forma.getIndice())
				.setListaGrupoInventario(listaGrupoInventario);
		}else{			
			forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrArticulo()
			.get(forma.getIndice()).setListaSubgrupoInventario(new ArrayList<SubgrupoInventario>());
			
			forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrArticulo()
			.get(forma.getIndice()).setListaGrupoInventario(new ArrayList<GrupoInventario>());
		}
		
		return mapping.findForward("paginaParametrizacion");	
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
	private ActionForward cargarSubgrupoInventario(ParametrizarNivelAutorizacionServicioArticuloForm forma, 
			ActionMapping mapping){
		ISubgrupoInventarioServicio subgrupoInventarioServicio = InventarioServicioFabrica.crearSubgrupoInventarioServicio();
		
		DTOBusquedaNivelAutorAgrupacionArticulo dtoAgrArticulo = forma.getNivelAtuorizacionServArtSeleccionado()
			.getListaAgrArticulo().get(forma.getIndice());
		
		forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrArticulo().get(forma.getIndice())
			.setSubgrupoInventario(ConstantesBD.codigoNuncaValido);
		
		if(!UtilidadTexto.isEmpty(dtoAgrArticulo.getGrupoCodigoConcatenado())){
			String[] codigos = new String[2];
			codigos = (dtoAgrArticulo.getGrupoCodigoConcatenado()).split("-");
			int codigoGrupo=Integer.valueOf(codigos[0]).intValue();
			
			ArrayList<SubgrupoInventario> listaSrupoInventario = subgrupoInventarioServicio.buscarSubgrupoInventarioPorGrupoID(codigoGrupo);
			forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrArticulo()
				.get(forma.getIndice()).setListaSubgrupoInventario(listaSrupoInventario);
		}else{
			forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrArticulo()
			.get(forma.getIndice()).setListaSubgrupoInventario(new ArrayList<SubgrupoInventario>());
		}
		return mapping.findForward("paginaParametrizacion");	
	}
	
	/**
	 * 
	 * Este Método se encarga de crear un nuevo registro de nivel
	 * de autorización
	 * 
	 * @return ActionForward
	 * @param ParametrizarNivelAutorizacionForm forma
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward nuevoRegistro(ParametrizarNivelAutorizacionServicioArticuloForm forma,
			ActionMapping mapping){
		
		DTOBusquedaNivelAutorizacionServicioArticulo nivelAutoriSerArt = forma.getNivelAtuorizacionServArtSeleccionado();
		
		if(forma.getEstado().equals("nuevoAgrArticulo")){
			DTOBusquedaNivelAutorAgrupacionArticulo dto = new DTOBusquedaNivelAutorAgrupacionArticulo();
			dto.setNivelAutoriSerArt(nivelAutoriSerArt.getCodigoPk());
			dto.setCodigoPk(ConstantesBD.codigoNuncaValido);
			
			if(forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrArticulo() ==null){
				forma.getNivelAtuorizacionServArtSeleccionado().setListaAgrArticulo(
						new ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo>());
			}
			
			forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrArticulo().add(dto);			
		}else{
			if(forma.getEstado().equals("nuevoAgrServicio")){
				DTOBusquedaNivelAutorAgrupacionServicio dto = new DTOBusquedaNivelAutorAgrupacionServicio();
				dto.setNivelAutoriSerArt(nivelAutoriSerArt.getCodigoPk());
				dto.setCodigoPk(ConstantesBD.codigoNuncaValido);
				
				if(forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrServicio()==null){
					forma.getNivelAtuorizacionServArtSeleccionado().setListaAgrServicio(
							new ArrayList<DTOBusquedaNivelAutorAgrupacionServicio>());
				}
				
				forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrServicio().add(dto);
			}
		}
		return mapping.findForward("paginaParametrizacion");			
	}			
	
	
	/**
	 * 
	 * Este Método se encarga de guardar un registro de nivel
	 * de autorización
	 * 
	 * @return ActionForward 
	 * @param ParametrizarNivelAutorizacionForm forma
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward guardarRegistro(ParametrizarNivelAutorizacionServicioArticuloForm forma, 
			ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioSesion){
		INivelAutorizacionServicioArticuloServicio servicioArticuloServicio = 
			CapitacionFabricaServicio.crearNivelAutorizacionServicioArticuloServicio();		
		DTOBusquedaNivelAutorizacionServicioArticulo dto = new DTOBusquedaNivelAutorizacionServicioArticulo();
		ActionErrors errores = new ActionErrors();		
		
		dto = forma.getNivelAtuorizacionServArtSeleccionado();
		errores = validarRegistro(forma);
		if(errores.isEmpty()){			
			Usuarios usuario = new Usuarios();
			usuario.setLogin(usuarioSesion.getLoginUsuario());
			dto.setUsuarios(usuario);
			
			if(servicioArticuloServicio.guardarNivelAutorizacionServicioArticuloDetallado(dto)){
				UtilidadTransaccion.getTransaccion().commit();				
				forma.setMensaje("exitoso");
			}
			return buscarNivelAutorizacionServicioArticulo(usuarioSesion, forma, mapping);
		}else{
			saveErrors(request, errores);
		}
		
		return mapping.findForward("paginaParametrizacion");
	}
	
	/**
	 * 
	 * Este Método se encarga de eliminar un registro de nivel
	 * de autorización
	 * 
	 * @return ActionForward 
	 * @param ParametrizarNivelAutorizacionForm forma
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward eliminarRegistro(ParametrizarNivelAutorizacionServicioArticuloForm forma, 
			ActionMapping mapping, UsuarioBasico usuarioSesion){
		
		if(forma.getEstado().equals("eliminarAgrServicio")){
			INivelAutorizacionAgrupacionServicioServicio agrServicio = 
				CapitacionFabricaServicio.crearNivelAutorizacionAgrupacionServicioServicio();
			int i=0;
			for( DTOBusquedaNivelAutorAgrupacionServicio registro : forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrServicio()){				
				if(forma.getIndice()==i){
					if(registro.getCodigoPk()!=ConstantesBD.codigoNuncaValido){
						if(agrServicio.eliminarNivelAutorizacionServicioArticulo(registro.getCodigoPk())){
							UtilidadTransaccion.getTransaccion().commit();				
							forma.setMensaje("exitoso");
						}
						return buscarNivelAutorizacionServicioArticulo(usuarioSesion, forma, mapping);
					}else{
						forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrServicio().remove(i);
						break;
					}					
				}
				i++;
			}
		}else{
			if(forma.getEstado().equals("eliminarAgrArticulo")){
				INivelAutorizacionAgrupacionArticuloServicio agrArticuloServicio =
					CapitacionFabricaServicio.crearNivelAutorizacionAgrupacionArticuloServicio();
				int i=0;
				for( DTOBusquedaNivelAutorAgrupacionArticulo registro : forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrArticulo()){				
					if(forma.getIndice()==i){
						if(registro.getCodigoPk()!=ConstantesBD.codigoNuncaValido){
							if(agrArticuloServicio.eliminarNivelAutorizacionAgrupacionArticulo(registro)){
								UtilidadTransaccion.getTransaccion().commit();				
								forma.setMensaje("exitoso");
							}
							return buscarNivelAutorizacionServicioArticulo(usuarioSesion, forma, mapping);
						}else{
							forma.getNivelAtuorizacionServArtSeleccionado().getListaAgrArticulo().remove(i);
							break;
						}					
					}
					i++;
				}
			}else{
				if(forma.getEstado().equals("eliminarArticuloEsp")){
					int i=0;
					INivelAutorizacionArticuloEspecificoServicio articuloEspServicio =
						CapitacionFabricaServicio.crearNivelAutorizacionArticuloEspecificoServicio();
					for( DTOBusquedaNivelAutorArticuloEspecifico registro : forma.getNivelAtuorizacionServArtSeleccionado().getListaArticuloEsp()){				
						if(forma.getIndice()==i){
							if(registro.getCodigo()!=ConstantesBD.codigoNuncaValido){
								if(articuloEspServicio.eliminarNivelAutorizacionArticuloEspecifico(registro.getCodigo())){
									UtilidadTransaccion.getTransaccion().commit();				
									forma.setMensaje("exitoso");
								}
								return buscarNivelAutorizacionServicioArticulo(usuarioSesion, forma, mapping);
							}else{
								forma.getNivelAtuorizacionServArtSeleccionado().getListaArticuloEsp().remove(i);
								break;
							}					
						}
						i++;
					}
				}else{
					if(forma.getEstado().equals("eliminarServicioEsp")){
						int i=0;
						INivelAutorizacionServicioEspecificoServicio servicioEspServicio =
							CapitacionFabricaServicio.crearNivelAutorizacionServicioEspecificoServicio();
						for( DTOBusquedaNivelAutorServicioEspecifico registro : forma.getNivelAtuorizacionServArtSeleccionado().getListaServicioEsp()){				
							if(forma.getIndice()==i){
								if(registro.getCodigoPk()!=ConstantesBD.codigoNuncaValido){
									if(servicioEspServicio.eliminarServicioEspecifico(registro.getCodigoPk())){
										UtilidadTransaccion.getTransaccion().commit();				
										forma.setMensaje("exitoso");
									}
									return buscarNivelAutorizacionServicioArticulo(usuarioSesion, forma, mapping);
								}else{
									forma.getNivelAtuorizacionServArtSeleccionado().getListaServicioEsp().remove(i);
									break;
								}					
							}
							i++;
						}
					}
				}
			}
		}
		forma.setMensaje("exitoso");
		return mapping.findForward("paginaParametrizacion");
	}
	
	/**
	 * 
	 * Este Método se encarga de Validar que no exista otro registro
	 * igual al que se está tratando de guardar
	 * 
	 * @param ParametrizarNivelAutorizacionServicioArticuloForm forma
	 * @return ActionErrors
	 *   
	 * @author, Angela Maria Aguirre
	 *
	 */
	private ActionErrors validarRegistro(ParametrizarNivelAutorizacionServicioArticuloForm forma){		
		ActionErrors errores = new ActionErrors();
		MessageResources mensajes=MessageResources.getMessageResources(
				"com.servinte.mensajes.capitacion.ParametrizarNivelAutorizacionServicioArticuloForm");
		int indice=0;
		boolean registroVacio = false;
		DTOBusquedaNivelAutorizacionServicioArticulo dto = forma.getNivelAtuorizacionServArtSeleccionado();
		
		if((dto.getListaAgrArticulo()!=null && dto.getListaAgrArticulo().size()>0)||
				(dto.getListaAgrServicio()!=null && dto.getListaAgrServicio().size()>0)||
				(dto.getListaArticuloEsp()!=null && dto.getListaArticuloEsp().size()>0)||
				(dto.getListaServicioEsp()!=null && dto.getListaServicioEsp().size()>0)){
			
			
			if(dto.getListaAgrArticulo()!=null && dto.getListaAgrArticulo().size()>0){
				for( DTOBusquedaNivelAutorAgrupacionArticulo registroValidar :  dto.getListaAgrArticulo()){
					if(registroValidar.getClaseInventario()==ConstantesBD.codigoNuncaValido){
						if(UtilidadTexto.isEmpty(registroValidar.getGrupoCodigoConcatenado())){
							if((registroValidar.getSubgrupoInventario()==null) ||
									(registroValidar.getSubgrupoInventario()==0) ||
									(registroValidar.getSubgrupoInventario()==ConstantesBD.codigoNuncaValido)){
								if(UtilidadTexto.isEmpty(registroValidar.getAcronimoNaturaleza())){
									errores.add("Registro Repetido", new ActionMessage("errors.notEspecific", 
											mensajes.getMessage("parametrizarNivelAutorizacionServicioArticuloForm.registroAgrupacionArticuloVacio",indice+1)));
									registroVacio=true;
								}								
							}						
						}
					}
					if(!registroVacio){
						int j=0;
						for( DTOBusquedaNivelAutorAgrupacionArticulo registro :  dto.getListaAgrArticulo()){
							if(j>indice){
								if(registroValidar.equals(registro)){
									errores.add("Registro Repetido", new ActionMessage("errors.notEspecific", 
										mensajes.getMessage("parametrizarNivelAutorizacionServicioArticuloForm.registroAgrupacionArticuloRepetido",j+1)));
								}
							}
							j++;
						}					
					}
					indice++;				
				}
				indice=0;
				registroVacio = false;
			}
												
			if(dto.getListaAgrServicio()!=null && dto.getListaAgrServicio().size()>0){
				for( DTOBusquedaNivelAutorAgrupacionServicio registroValidar : dto.getListaAgrServicio()){
					
					if(registroValidar.getGrupoServicio()==ConstantesBD.codigoNuncaValido){
						if(registroValidar.getTipoServicio().equals("")){
							if(registroValidar.getEspecialidad()==ConstantesBD.codigoNuncaValido){
								errores.add("Registro Repetido", new ActionMessage("errors.notEspecific", 
										mensajes.getMessage("parametrizarNivelAutorizacionServicioArticuloForm.registroAgrupacionServicioVacio",indice+1)));
								registroVacio=true;
							}						
						}
					}
					if(!registroVacio){
						int j=0;
						for( DTOBusquedaNivelAutorAgrupacionServicio registro : dto.getListaAgrServicio()){
							if(j>indice){
								if(registroValidar.equals(registro)){
									errores.add("Registro Repetido", new ActionMessage("errors.notEspecific", 
										mensajes.getMessage("parametrizarNivelAutorizacionServicioArticuloForm.registroAgrupacionServicioRepetido",j+1)));
								}
							}
							j++;
						}
					}
					indice++;
				}		
				indice=0;
			}
			
			if(dto.getListaServicioEsp()!=null && dto.getListaServicioEsp().size()>0){
				for( DTOBusquedaNivelAutorServicioEspecifico registroValidar : dto.getListaServicioEsp()){
					int j=0;
					for( DTOBusquedaNivelAutorServicioEspecifico registro : dto.getListaServicioEsp()){
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
			
			if(dto.getListaArticuloEsp()!=null && dto.getListaArticuloEsp().size()>0){
				for( DTOBusquedaNivelAutorArticuloEspecifico registroValidar : dto.getListaArticuloEsp()){
					int j=0;
					for( DTOBusquedaNivelAutorArticuloEspecifico registro : dto.getListaArticuloEsp()){
						if(j>indice){
							if(registroValidar.equals(registro)){
								errores.add("Registro Repetido", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("parametrizarNivelAutorizacionServicioArticuloForm.registroArticuloEsp",j+1)));
							}
						}
						j++;
					}
					indice++;
				}		
				indice=0;
			}			
		}else{
			errores.add("Registro Repetido", new ActionMessage("errors.notEspecific", 
					mensajes.getMessage("parametrizarNivelAutorizacionServicioArticuloForm.registroServicioArticuloVacio")));
			
		}		
		return errores;
	}
	
	/**
	 * 
	 * Este Método se encarga de identificar el nivel de autorización 
	 * seleccionada
	 * @param ParametrizarNivelAutorizacionServicioArticuloForm
	 * @author, Angela Maria Aguirre
	 *
	 */
	private void identificarNivelAutorizacionSeleccionada(ParametrizarNivelAutorizacionServicioArticuloForm forma){
		DTONivelAutorizacion nivelAutorizacion=new DTONivelAutorizacion();
		for(DTONivelAutorizacion registro :forma.getListaNivelAutorizacion()){
			if(registro.getCodigoPk()==forma.getIndice()){
				nivelAutorizacion.setCodigoPk(registro.getCodigoPk());
				nivelAutorizacion.setDescripcion(registro.getDescripcion());
				forma.setNivelAutorizacion(nivelAutorizacion);
				break;
			}
		}		
	}

}
