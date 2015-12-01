package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionArticulo;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionServicio;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorArticuloEspecifico;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorServicioEspecifico;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionServicioArticulo;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionAgrupacionServicioDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionServicioArticuloDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionServicioEspecificoDAO;
import com.servinte.axioma.dao.interfaz.inventario.IGrupoInventarioDAO;
import com.servinte.axioma.dao.interfaz.inventario.ISubgrupoInventarioDAO;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionAgrupacionArticuloMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionArticuloEspecificoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionServicioArticuloMundo;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.GrupoInventario;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.NivelAutorAgrServ;
import com.servinte.axioma.orm.NivelAutorArticulo;
import com.servinte.axioma.orm.NivelAutorServMedic;
import com.servinte.axioma.orm.NivelAutorServicio;
import com.servinte.axioma.orm.NivelAutorizacion;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.orm.SubgrupoInventario;
import com.servinte.axioma.orm.TiposServicio;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización de Servicios y Artículos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionServicioArticuloMundo implements
		INivelAutorizacionServicioArticuloMundo {
	
	INivelAutorizacionServicioArticuloDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionServicioArticuloMundo(){
		dao = CapitacionFabricaDAO.crearNivelAutorizacionServicioArticuloDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * de servicios y artículos registrados en el sistema
	 * 
	 * @return DTOBusquedaNivelAutorizacionServicioArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOBusquedaNivelAutorizacionServicioArticulo buscarNivelAutorizacionServicioArticulo(int nivelAutorizacionID){
		return dao.buscarNivelAutorizacionServicioArticulo(nivelAutorizacionID);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * de servicios / artículos con sus detalles
	 * 
	 * @return DTOBusquedaNivelAutorizacionServicioArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOBusquedaNivelAutorizacionServicioArticulo buscarNivelAutorizacionServicioArticuloDetallado(
			int nivelAutorizacionID, UsuarioBasico usuarioSesion){
		
		DTOBusquedaNivelAutorizacionServicioArticulo dto = dao.buscarNivelAutorizacionServicioArticulo(nivelAutorizacionID);
		
		if(dto!=null){
			int codigoTipoTarifario=0;
			int codigoInstitucion = usuarioSesion.getCodigoInstitucionInt();
			
			INivelAutorizacionAgrupacionArticuloMundo agrArticuloMundo = 
				CapitacionFabricaMundo.crearNivelAutorizacionAgrupacionArticuloMundo();
			
			INivelAutorizacionArticuloEspecificoMundo articuloMundo= 
				CapitacionFabricaMundo.crearNivelAutorizacionArticuloEspecificoMundo();
			
			INivelAutorizacionAgrupacionServicioDAO servicioDAO = 
				CapitacionFabricaDAO.crearNivelAutorizacionAgrupacionServicioDAO();
			
			INivelAutorizacionServicioEspecificoDAO servicioEspDAO = 
				CapitacionFabricaDAO.crearNivelAutorizacionServicioEspecificoDAO();
				
			
			DTOBusquedaNivelAutorAgrupacionArticulo dtoAgrArticulo = new DTOBusquedaNivelAutorAgrupacionArticulo();
			dtoAgrArticulo.setNivelAutoriSerArt(dto.getCodigoPk());
			ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> listaAgrArticulo = 
				agrArticuloMundo.buscarNivelAutorizacionAgrupacionArticulo(dtoAgrArticulo);
			
			if(listaAgrArticulo!=null && listaAgrArticulo.size()>0){
			
				IGrupoInventarioDAO grupoInventarioDAO = 
					InventarioDAOFabrica.crearGrupoInventarioDAO();
				
				ISubgrupoInventarioDAO subgrupoInventarioDAO = 
					InventarioDAOFabrica.crearSubgrupoInventarioDAO();
				
				ArrayList<GrupoInventario> listaGrupoInventario = null;
				ArrayList<SubgrupoInventario> listaSrupoInventario=null;
				
				for(DTOBusquedaNivelAutorAgrupacionArticulo registro : listaAgrArticulo){
					if(registro.getClaseInventario()!=null && registro.getClaseInventario()>0){
						listaGrupoInventario = 
							grupoInventarioDAO.buscarGrupoInventarioPorClase(registro.getClaseInventario());
						registro.setListaGrupoInventario(listaGrupoInventario);
					}					
					if(registro.getGrupoInventario()!=null && registro.getGrupoInventario()>0){
						listaSrupoInventario = subgrupoInventarioDAO.buscarSubgrupoInventarioPorGrupoID(
								registro.getGrupoInventario());
						registro.setListaSubgrupoInventario(listaSrupoInventario);						
					}					
				}				
				dto.setListaAgrArticulo(listaAgrArticulo);
			}			
			
			DTOBusquedaNivelAutorArticuloEspecifico dtoArticulo = new DTOBusquedaNivelAutorArticuloEspecifico();
			dtoArticulo.setNivelAutoriSerArt(dto.getCodigoPk());
			ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> listaArticulo = 
				articuloMundo.buscarNivelAutorizacionArticuloEspecifico(dtoArticulo);
			if(listaArticulo!=null && listaArticulo.size()>0){
				dto.setListaArticuloEsp(listaArticulo);
			}
									
			ArrayList<DTOBusquedaNivelAutorAgrupacionServicio> listaAgrServicio = 
				servicioDAO.buscarNivelAutorizacionServicioArticulo(dto.getCodigoPk());
			if(listaAgrServicio!=null && listaAgrServicio.size()>0){
				dto.setListaAgrServicio(listaAgrServicio);
			}	
			
			String codigoTarifario = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion);
			if(UtilidadTexto.isNumber(codigoTarifario)){				
				codigoTipoTarifario = Integer.parseInt(codigoTarifario);
			}						
			ArrayList<DTOBusquedaNivelAutorServicioEspecifico> listaServicioEsp = 
				servicioEspDAO.buscarNivelAutorizacionServicios(dto.getCodigoPk(), codigoTipoTarifario);
			if(listaServicioEsp!=null && listaServicioEsp.size()>0){
				
				for(DTOBusquedaNivelAutorServicioEspecifico registro : listaServicioEsp){
					registro.setPermiteEliminar(true);
				}
				
				dto.setListaServicioEsp(listaServicioEsp);
			}
			
		}
		
		return dto;		
		
	}
	

	/**
	 * 
	 * Este Método se encarga de guardar los niveles de autorización 
	 * de servicios y artículos registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionServicioArticulo(NivelAutorServMedic registro){
		return dao.guardarNivelAutorizacionServicioArticulo(registro);
	}
	
	/**
	 * 
	 * Este Método se encarga de guardar los niveles de autorización 
	 * de servicios - artículos y sus detalles
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionServicioArticuloDetallado(DTOBusquedaNivelAutorizacionServicioArticulo dto){
		
		NivelAutorServMedic nivelAutorServArt = new NivelAutorServMedic();
		NivelAutorizacion nivelAutorizacion = new NivelAutorizacion();
		boolean procesoExitoso = true;
		nivelAutorizacion.setCodigoPk(dto.getNivelAutorizacionID());
		
		Date fechaRegistro = Calendar.getInstance().getTime();
		String horaRegistro= UtilidadFecha.conversionFormatoHoraABD(Calendar
				.getInstance().getTime());
		
		
		if(dto.getListaAgrServicio()!=null && dto.getListaAgrServicio().size()>0){
			NivelAutorAgrServ agrServicio = null;
			HashSet<NivelAutorAgrServ> setServicioEspecifico = new HashSet<NivelAutorAgrServ>(0);	
			
			for( DTOBusquedaNivelAutorAgrupacionServicio registro : dto.getListaAgrServicio()){
				agrServicio= new NivelAutorAgrServ();
				agrServicio = poblarAgrupacionServicio(registro);
				agrServicio.setNivelAutorServMedic(nivelAutorServArt);
				agrServicio.setFechaRegistro(fechaRegistro);
				agrServicio.setHoraRegistro(horaRegistro);
				agrServicio.setUsuarios(dto.getUsuarios());
				setServicioEspecifico.add(agrServicio);
			}
			nivelAutorServArt.setNivelAutorAgrServs(setServicioEspecifico);
		}
		
		if(dto.getListaServicioEsp()!=null && dto.getListaServicioEsp().size()>0){
			NivelAutorServicio servicioEsp = null;
			HashSet<NivelAutorServicio> setServicioEspecifico = new HashSet<NivelAutorServicio>(0);
			
			for( DTOBusquedaNivelAutorServicioEspecifico registro : dto.getListaServicioEsp()){
				servicioEsp = new NivelAutorServicio();
				servicioEsp = poblarServicioEspecifico(registro);
				servicioEsp.setNivelAutorServMedic(nivelAutorServArt);
				servicioEsp.setFechaRegistro(fechaRegistro);
				servicioEsp.setHoraRegistro(horaRegistro);
				servicioEsp.setUsuarios(dto.getUsuarios());
				setServicioEspecifico.add(servicioEsp);
			}
			nivelAutorServArt.setNivelAutorServicios(setServicioEspecifico);
		}
		
		if(dto.getListaArticuloEsp()!=null && dto.getListaArticuloEsp().size()>0){
			NivelAutorArticulo articulo = null;
			HashSet<NivelAutorArticulo> setArticuloEspecifico = new HashSet<NivelAutorArticulo>();
				
			for( DTOBusquedaNivelAutorArticuloEspecifico registro : dto.getListaArticuloEsp()){
				articulo = new NivelAutorArticulo();
				articulo = poblarArticuloEspecifico(registro);
				articulo.setNivelAutorServMedic(nivelAutorServArt);
				articulo.setFechaRegistro(fechaRegistro);
				articulo.setHoraRegistro(horaRegistro);
				articulo.setUsuarios(dto.getUsuarios());
				
				setArticuloEspecifico.add(articulo);				
			}
			nivelAutorServArt.setNivelAutorArticulos(setArticuloEspecifico);
		}
		
		nivelAutorServArt.setFechaRegistro(fechaRegistro);
		nivelAutorServArt.setHoraRegistro(horaRegistro);
		nivelAutorServArt.setUsuarios(dto.getUsuarios());		
		nivelAutorServArt.setNivelAutorizacion(nivelAutorizacion);
		
		if(dto.getCodigoPk()!=ConstantesBD.codigoNuncaValido){
			nivelAutorServArt.setCodigoPk(dto.getCodigoPk());
			procesoExitoso = dao.actualizarNivelAutorizacionServicioArticulo(nivelAutorServArt);
		}else{
			procesoExitoso = dao.guardarNivelAutorizacionServicioArticulo(nivelAutorServArt);
		}
		if(procesoExitoso){
			UtilidadTransaccion.getTransaccion().commit();
			UtilidadTransaccion.getTransaccion().begin();		
			
			if(dto.getListaAgrArticulo()!=null && dto.getListaAgrArticulo().size()>0){
				INivelAutorizacionAgrupacionArticuloMundo agrArticuloMundo = 
					CapitacionFabricaMundo.crearNivelAutorizacionAgrupacionArticuloMundo();
				
				for( DTOBusquedaNivelAutorAgrupacionArticulo registro : dto.getListaAgrArticulo()){
					
					registro.setNivelAutoriSerArt(nivelAutorServArt.getCodigoPk());
					registro.setLoginRegistra(dto.getUsuarios().getLogin());
					
					if((registro.getSubgrupoInventario()!=null) && (registro.getSubgrupoInventario()!=0) &&
							(registro.getSubgrupoInventario()!=ConstantesBD.codigoNuncaValido)){
						
						registro.setGrupoCodigoConcatenado(null);
						registro.setClaseInventario(ConstantesBD.codigoNuncaValido);
					}
					
					if(registro.getCodigoPk()!=ConstantesBD.codigoNuncaValido){					
						agrArticuloMundo.actualizarMontoAgrupacionArticulo(registro);
					}else{
						agrArticuloMundo.insertarMontoAgrupacionArticulo(registro);
					}				
				}
			}
		}
				
		return procesoExitoso;
	}
	

	/**
	 * 
	 * Este Método se encarga de actualizar los niveles de autorización
	 * de servicios y artículos registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarNivelAutorizacionServicioArticulo(NivelAutorServMedic registro){
		return dao.actualizarNivelAutorizacionServicioArticulo(registro);
	}


	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización de servicios y artículos
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionServicioArticulo(int idNivelAutorizacionUsuario){
		return dao.eliminarNivelAutorizacionServicioArticulo(idNivelAutorizacionUsuario);
	}
	
	/**
	 * 
	 * Este Método se encarga de 
	 * @author, Angela Maria Aguirre
	 *
	 */
	private NivelAutorAgrServ poblarAgrupacionServicio(
			DTOBusquedaNivelAutorAgrupacionServicio registro){
			
		NivelAutorAgrServ agrupacionServicio = null;
		
		agrupacionServicio = new NivelAutorAgrServ();
				
		if(registro.getCodigoPk()!=ConstantesBD.codigoNuncaValido){
			agrupacionServicio.setCodigoPk(registro.getCodigoPk());				
		}
		if(registro.getEspecialidad()!=ConstantesBD.codigoNuncaValido){
			Especialidades especialidad = new Especialidades();
			especialidad.setCodigo(registro.getEspecialidad());
			agrupacionServicio.setEspecialidades(especialidad);
		}
		if(!registro.getTipoServicio().equals("")){
			TiposServicio tipoServicio = new TiposServicio();
			tipoServicio.setAcronimo(registro.getTipoServicio());
			agrupacionServicio.setTiposServicio(tipoServicio);
		}
		if(registro.getGrupoServicio()!=ConstantesBD.codigoNuncaValido){
			GruposServicios grupoServicio = new GruposServicios();
			grupoServicio.setCodigo(registro.getGrupoServicio());
			agrupacionServicio.setGruposServicios(grupoServicio);
		}					
		return agrupacionServicio;		
	}
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	private NivelAutorServicio poblarServicioEspecifico(DTOBusquedaNivelAutorServicioEspecifico registro){
		NivelAutorServicio servicio = new NivelAutorServicio();
		
		if(registro.getCodigoPk()!=ConstantesBD.codigoNuncaValido){
			servicio.setCodigoPk(registro.getCodigoPk());
		}
		if(registro.getCodigoServicio()!=ConstantesBD.codigoNuncaValido){
			Servicios servicios= new Servicios(); 
			servicios.setCodigo(registro.getCodigoServicio());
			servicio.setServicios(servicios);						
		}		
		return servicio;
	}	
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	private NivelAutorArticulo poblarArticuloEspecifico(DTOBusquedaNivelAutorArticuloEspecifico registro){
		NivelAutorArticulo articuloEspecifico = new NivelAutorArticulo();
		
		if(registro.getCodigo()!=ConstantesBD.codigoNuncaValido){
			articuloEspecifico.setCodigoPk(registro.getCodigo());
		}		
		if(registro.getArticuloCodigo()!=ConstantesBD.codigoNuncaValido){
			articuloEspecifico.setArticulo(registro.getArticuloCodigo());
		}		
		return articuloEspecifico;		
	}

}
