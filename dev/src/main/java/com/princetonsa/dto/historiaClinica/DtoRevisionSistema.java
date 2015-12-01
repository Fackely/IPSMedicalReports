/*
 * Abr 24, 2008
 */
package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadTexto;

/**
 * Data Transfer Object: Revisión Sistemas
 * @author Sebastián Gómez R.
 *
 */
public class DtoRevisionSistema implements Serializable
{
	/**
	 * Consecutivo BD de la revision por sistema
	 */
	private int codigo;
	
	
	private String nombre;
	private String unidadMedida;
	private String valorVerdadero;
	private String valorFalso;
	private String valor;
	private Boolean estadoNormal;
	private String descripcion;
	private int tipoComponente;
	/**
	 * Identifica si el valor capturado de la revision es por seleccion múltiple o campo de captura
	 */
	private boolean multiple;
	/**
	 * Opciones elegidas en el caso de que la revision por sistema sea de tipo múltiple
	 */
	private ArrayList<InfoDatosInt> opciones; 
	
	public DtoRevisionSistema()
	{
		this.clean();
	}
	
	public void clean()
	{
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.nombre = "";
		this.unidadMedida = "";
		this.valorVerdadero = "";
		this.valorFalso = "";
		this.valor = "";
		this.estadoNormal = null;
		this.multiple = false;
		this.opciones = new ArrayList<InfoDatosInt>();
		this.descripcion = "";
		this.tipoComponente = ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the estadoNormal
	 */
	public Boolean getEstadoNormal() {
		return estadoNormal;
	}

	/**
	 * @param estadoNormal the estadoNormal to set
	 */
	public void setEstadoNormal(Boolean estadoNormal) {
		this.estadoNormal = estadoNormal;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the unidadMedida
	 */
	public String getUnidadMedida() {
		return unidadMedida;
	}

	/**
	 * @param unidadMedida the unidadMedida to set
	 */
	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	/**
	 * @return the valorFalso
	 */
	public String getValorFalso() {
		return valorFalso;
	}

	/**
	 * @param valorFalso the valorFalso to set
	 */
	public void setValorFalso(String valorFalso) {
		this.valorFalso = valorFalso;
	}

	/**
	 * @return the valorVerdadero
	 */
	public String getValorVerdadero() {
		return valorVerdadero;
	}

	/**
	 * @param valorVerdadero the valorVerdadero to set
	 */
	public void setValorVerdadero(String valorVerdadero) {
		this.valorVerdadero = valorVerdadero;
	}

	/**
	 * @return the multiple
	 */
	public boolean isMultiple() {
		return multiple;
	}

	/**
	 * @param multiple the multiple to set
	 */
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	/**
	 * @return the opciones
	 */
	public ArrayList<InfoDatosInt> getOpciones() {
		return opciones;
	}

	/**
	 * @param opciones the opciones to set
	 */
	public void setOpciones(ArrayList<InfoDatosInt> opciones) {
		this.opciones = opciones;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the tipoComponente
	 */
	public int getTipoComponente() {
		return tipoComponente;
	}

	/**
	 * @param tipoComponente the tipoComponente to set
	 */
	public void setTipoComponente(int tipoComponente) {
		this.tipoComponente = tipoComponente;
	}
	
	/**
	 * Método que verifica si la revision por sistema tiene opciones activadas
	 * @return
	 */
	public boolean tieneOpcionesActivadas()
	{
		boolean activadas = false;
		
		for(InfoDatosInt opcion:this.opciones)
			if(UtilidadTexto.getBoolean(opcion.getActivoStr()))
				activadas = true;
		
		if(!this.descripcion.equals("")||this.estadoNormal!=null)
			activadas = true;
		
		return activadas;
	}
	
}
