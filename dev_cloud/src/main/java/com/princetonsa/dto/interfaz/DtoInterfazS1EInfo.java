package com.princetonsa.dto.interfaz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.mundo.InstitucionBasica;

public class DtoInterfazS1EInfo extends DtoInterfazParamContaS1E implements Serializable
{
	private ArrayList<DtoTipoMovimiento> tipoMovimientos;
	/**
	 * Corresonde al tipo de movimiento por el cual se desa realizar la consulta de los movimientos contables
	 */
	DtoTipoMovimiento tipoMovimientoSeleccionado;
	
	private String fechaProceso;
	
	private boolean preguntarArchivosSobreEsc;
	private boolean existeInfoArchivos;
	private boolean existeInconsistenciArchivos;
	/**
	 * Atributo para almacenar la posicion en la que va la línea
	 */
	private int posicion;
	
	
	private ArrayList<HashMap> tipoDocXTipoMov;
	
	/**
	 * Institucion básica de la sesión
	 */
	private InstitucionBasica institucionBasica;
	
	public DtoInterfazS1EInfo()
	{
		super.reset();
		reset();
	}
	
	public void reset()
	{
		this.tipoMovimientos  = new ArrayList<DtoTipoMovimiento>();
		this.tipoMovimientoSeleccionado = new DtoTipoMovimiento();
		this.tipoDocXTipoMov = new ArrayList<HashMap>();
		this.fechaProceso = "";
		this.preguntarArchivosSobreEsc = false;
		this.existeInfoArchivos = false;
		this.existeInconsistenciArchivos = false;
		this.institucionBasica = new InstitucionBasica();
		this.posicion = 1;
	}
	
	public String getFechaProcesoDdMmAaaa()
	{
		return this.fechaProceso.replace("/","");
	}
	

	public String getFechaProceso() {
		return fechaProceso;
	}

	public void setFechaProceso(String fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	public boolean isPreguntarArchivosSobreEsc() {
		return preguntarArchivosSobreEsc;
	}

	public void setPreguntarArchivosSobreEsc(boolean preguntarArchivosSobreEsc) {
		this.preguntarArchivosSobreEsc = preguntarArchivosSobreEsc;
	}

	public ArrayList<DtoTipoMovimiento> getTipoMovimientos() {
		return tipoMovimientos;
	}

	public void setTipoMovimientos(ArrayList<DtoTipoMovimiento> tipoMovimientos) {
		this.tipoMovimientos = tipoMovimientos;
	}

	public ArrayList<HashMap> getTipoDocXTipoMov() {
		return tipoDocXTipoMov;
	}

	public void setTipoDocXTipoMov(ArrayList<HashMap> tipoDocXTipoMov) {
		this.tipoDocXTipoMov = tipoDocXTipoMov;
	}
	
	public ArrayList<String> getTipoDocXTipoMov(String tipoMovimiento)
	{
		ArrayList<String> array = new ArrayList<String>();
		
		for(HashMap mapa : tipoDocXTipoMov)
			array.add(mapa.get("tipo_documento").toString());
		
		return array;
	}
	
	/**
	 * 
	 * */
	public String getTiposMovimientosXComa()
	{
		String resultado = "";
		
		for(DtoTipoMovimiento dto : tipoMovimientos)
		{
			if(dto.isSeleccionado())			
				resultado += "'"+dto.getCodigo()+"',";
		}
		
		if(!resultado.equals(""))
		{
			resultado = resultado.substring(0,resultado.length()-1);
		}
		
		return resultado;
	}

	public boolean isExisteInfoArchivos() {
		return existeInfoArchivos;
	}

	public void setExisteInfoArchivos(boolean existeInfoArchivos) {
		this.existeInfoArchivos = existeInfoArchivos;
	}

	public boolean isExisteInconsistenciArchivos() {
		return existeInconsistenciArchivos;
	}

	public void setExisteInconsistenciArchivos(boolean existeInconsistenciArchivos) {
		this.existeInconsistenciArchivos = existeInconsistenciArchivos;
	}

	/**
	 * @return the tipoMovimientoSeleccionado
	 */
	public DtoTipoMovimiento getTipoMovimientoSeleccionado() {
		return tipoMovimientoSeleccionado;
	}

	/**
	 * @param tipoMovimientoSeleccionado the tipoMovimientoSeleccionado to set
	 */
	public void setTipoMovimientoSeleccionado(
			DtoTipoMovimiento tipoMovimientoSeleccionado) {
		this.tipoMovimientoSeleccionado = tipoMovimientoSeleccionado;
	}

	

	/**
	 * @return the institucionBasica
	 */
	public InstitucionBasica getInstitucionBasica() {
		return institucionBasica;
	}

	/**
	 * @param institucionBasica the institucionBasica to set
	 */
	public void setInstitucionBasica(InstitucionBasica institucionBasica) {
		this.institucionBasica = institucionBasica;
	}

	/**
	 * @return the posicion
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
}