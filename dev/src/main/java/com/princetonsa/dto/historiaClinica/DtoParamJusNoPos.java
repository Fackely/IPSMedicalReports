package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DtoParamJusNoPos implements Serializable
{
	private ArrayList<DtoParamSeccionesJusNoPos> secciones;
	private String estadoJus;
	private String subCuenta;
	private String solicitud;
	private String ordenAmbulatoria;
	private String codigoArticulo;
	private String cantidadArticulo;
	private String codigoJustificacion;
	
	// Variable utilizada provisionalmente para capturar los dx relacionados
	private HashMap<String, Object> dxRelacionadosMap;
	
	/**
	 * Resetea todas las variables del DTO
	 */
	public void clean()
	{
		this.secciones = new ArrayList<DtoParamSeccionesJusNoPos>();
		this.estadoJus="";
		this.subCuenta="";
		this.codigoArticulo="";
		this.cantidadArticulo="";
		this.solicitud="";
		this.ordenAmbulatoria="";
		this.codigoJustificacion="";
		this.dxRelacionadosMap = new HashMap<String, Object>();
	}
	
	/**
	 * 
	 */
	public DtoParamJusNoPos()
	{
		this.clean();
	}

	/**
	 * @return the secciones
	 */
	public ArrayList<DtoParamSeccionesJusNoPos> getSecciones() {
		return secciones;
	}

	/**
	 * @param secciones the secciones to set
	 */
	public void setSecciones(ArrayList<DtoParamSeccionesJusNoPos> secciones) {
		this.secciones = secciones;
	}
	
	/**
	 * @return the secciones
	 */
	public DtoParamSeccionesJusNoPos getSecciones(int pos) {
		return secciones.get(pos);
	}
	

	/**
	 * @return the subCuenta
	 */
	public String getSubCuenta() {
		return subCuenta;
	}

	/**
	 * @param subCuenta the subCuenta to set
	 */
	public void setSubCuenta(String subCuenta) {
		this.subCuenta = subCuenta;
	}

	/**
	 * @return the codigoArticulo
	 */
	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * @param codigoArticulo the codigoArticulo to set
	 */
	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * @return the cantidadArticulo
	 */
	public String getCantidadArticulo() {
		return cantidadArticulo;
	}

	/**
	 * @param cantidadArticulo the cantidadArticulo to set
	 */
	public void setCantidadArticulo(String cantidadArticulo) {
		this.cantidadArticulo = cantidadArticulo;
	}

	/**
	 * @return the solicitud
	 */
	public String getSolicitud() {
		return solicitud;
	}

	/**
	 * @param solicitud the solicitud to set
	 */
	public void setSolicitud(String solicitud) {
		this.solicitud = solicitud;
	}

	/**
	 * @return the ordenAmbulatoria
	 */
	public String getOrdenAmbulatoria() {
		return ordenAmbulatoria;
	}

	/**
	 * @param ordenAmbulatoria the ordenAmbulatoria to set
	 */
	public void setOrdenAmbulatoria(String ordenAmbulatoria) {
		this.ordenAmbulatoria = ordenAmbulatoria;
	}

	/**
	 * @return the codigoJustificacion
	 */
	public String getCodigoJustificacion() {
		return codigoJustificacion;
	}

	/**
	 * @param codigoJustificacion the codigoJustificacion to set
	 */
	public void setCodigoJustificacion(String codigoJustificacion) {
		this.codigoJustificacion = codigoJustificacion;
	}

	/**
	 * @return the dxRelacionados
	 */
	public HashMap<String, Object> getDxRelacionadosMap() {
		return dxRelacionadosMap;
	}

	/**
	 * @param dxRelacionados the dxRelacionados to set
	 */
	public void setDxRelacionadosMap(HashMap<String, Object> dxRelacionados) {
		this.dxRelacionadosMap = dxRelacionados;
	}

	/**
	 * @return the dxRelacionados
	 */
	public Object getDxRelacionados(String llave) {
		return dxRelacionadosMap.get(llave);
	}

	/**
	 * @param dxRelacionados the dxRelacionados to set
	 */
	public void setDxRelacionadosMap(String llave, Object obj) {
		this.dxRelacionadosMap.put(llave, obj);
	}

	/**
	 * @return the estadoJus
	 */
	public String getEstadoJus() {
		return estadoJus;
	}

	/**
	 * @param estadoJus the estadoJus to set
	 */
	public void setEstadoJus(String estadoJus) {
		this.estadoJus = estadoJus;
	}
	
	
}
