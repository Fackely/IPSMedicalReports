package com.servinte.axioma.dto.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.DetalleValorizacionArt;
import com.servinte.axioma.orm.DetalleValorizacionServ;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.NivelAtencion;

/**
 * Dto que contiene un nivel de atención con sus respectivas
 * listas de grupos de servicio y clases de inventario 
 * asociadas
 * @author diecorqu
 *
 */
public class DtoNivelesAtencionPresupuestoParametrizacionGeneral {
	
	/**
	 * Nivel de atención
	 */
	private NivelAtencion nivelAtencion;
	
	/**
	 * determina si para el nivel hay grupos de servicios asociados
	 */
	private boolean existenServicios;
	
	/**
	 * determina si para el nivel hay clases de inventarios
	 */
	private boolean existenArticulos;
	
	/**
	 * Determina si se guardo el grupo de servicio
	 */
	private boolean guardoNivelAtencionGrupoServicio;
	
	/**
	 * Determina si se guardo la clase de inventario
	 */
	private boolean guardoNivelAtencionClaseInventario;
	
	/**
	 * Lista con los grupos de servicios asociados al nivel
	 */
	private ArrayList<GruposServicios> listaGruposServicios;
	
	/**
	 * Lista con las clases de inventario asociadas al nivel
	 */
	private ArrayList<ClaseInventario> listaClasesInventario;
	
	/**
	 * Lista con la valorización para el grupo de servicio del nivel
	 */
	private ArrayList<DetalleValorizacionServ> detalleValorizacionServicios;
	
	/**
	 * Lista con la valorización para la clase de inventario del nivel
	 */
	private ArrayList<DetalleValorizacionArt> detalleValorizacionArticulos;

	
	
	public DtoNivelesAtencionPresupuestoParametrizacionGeneral() {}
	
	
	public DtoNivelesAtencionPresupuestoParametrizacionGeneral(
			NivelAtencion nivelAtencion, boolean existenServicios,
			boolean existenArticulos, boolean guardoNivelAtencionGrupoServicio,
			boolean guardoNivelAtencionClaseInventario,
			ArrayList<GruposServicios> listaGruposServicios,
			ArrayList<ClaseInventario> listaClasesInventario,
			ArrayList<DetalleValorizacionServ> detalleValorizacionServicios,
			ArrayList<DetalleValorizacionArt> detalleValorizacionArticulos) {
		super();
		this.nivelAtencion = nivelAtencion;
		this.existenServicios = existenServicios;
		this.existenArticulos = existenArticulos;
		this.guardoNivelAtencionGrupoServicio = guardoNivelAtencionGrupoServicio;
		this.guardoNivelAtencionClaseInventario = guardoNivelAtencionClaseInventario;
		this.listaGruposServicios = listaGruposServicios;
		this.listaClasesInventario = listaClasesInventario;
		this.detalleValorizacionServicios = detalleValorizacionServicios;
		this.detalleValorizacionArticulos = detalleValorizacionArticulos;
	}

	public NivelAtencion getNivelAtencion() {
		return nivelAtencion;
	}

	public void setNivelAtencion(NivelAtencion nivelAtencion) {
		this.nivelAtencion = nivelAtencion;
	}

	public boolean isExistenServicios() {
		return existenServicios;
	}

	public void setExistenServicios(boolean existenServicios) {
		this.existenServicios = existenServicios;
	}

	public boolean isExistenArticulos() {
		return existenArticulos;
	}

	public void setExistenArticulos(boolean existenArticulos) {
		this.existenArticulos = existenArticulos;
	}

	public boolean isGuardoNivelAtencionGrupoServicio() {
		return guardoNivelAtencionGrupoServicio;
	}

	public void setGuardoNivelAtencionGrupoServicio(
			boolean guardoNivelAtencionGrupoServicio) {
		this.guardoNivelAtencionGrupoServicio = guardoNivelAtencionGrupoServicio;
	}

	public boolean isGuardoNivelAtencionClaseInventario() {
		return guardoNivelAtencionClaseInventario;
	}

	public void setGuardoNivelAtencionClaseInventario(
			boolean guardoNivelAtencionClaseInventario) {
		this.guardoNivelAtencionClaseInventario = guardoNivelAtencionClaseInventario;
	}

	public ArrayList<GruposServicios> getListaGruposServicios() {
		return listaGruposServicios;
	}

	public void setListaGruposServicios(
			ArrayList<GruposServicios> listaGruposServicios) {
		this.listaGruposServicios = listaGruposServicios;
	}

	public ArrayList<ClaseInventario> getListaClasesInventario() {
		return listaClasesInventario;
	}

	public void setListaClasesInventario(
			ArrayList<ClaseInventario> listaClasesInventario) {
		this.listaClasesInventario = listaClasesInventario;
	}

	public ArrayList<DetalleValorizacionServ> getDetalleValorizacionServicios() {
		return detalleValorizacionServicios;
	}

	public void setDetalleValorizacionServicios(
			ArrayList<DetalleValorizacionServ> detalleValorizacionServicios) {
		this.detalleValorizacionServicios = detalleValorizacionServicios;
	}

	public ArrayList<DetalleValorizacionArt> getDetalleValorizacionArticulos() {
		return detalleValorizacionArticulos;
	}

	public void setDetalleValorizacionArticulos(
			ArrayList<DetalleValorizacionArt> detalleValorizacionArticulos) {
		this.detalleValorizacionArticulos = detalleValorizacionArticulos;
	}
	
}
