package com.princetonsa.dto.interfaz;

import java.io.Serializable;

import com.princetonsa.enu.interfaz.TipoInconsistencia;
import com.princetonsa.mundo.interfaz.GeneracionInterfaz;
import com.princetonsa.mundo.interfaz.GeneracionInterfaz1E;


public class DtoInterfazCampoS1E implements Serializable
{
	private String descripcion;
	
	private String tipo;
	
	private boolean requerido;
	
	private int tamano;
	
	private String valor;
	
	private boolean inconsistencia;
	
	//Campos para la inconsistencia
	private String tipoDocumento;
	private String numeroDocumento;
	private String consecutivoDocumento;
	private TipoInconsistencia tipoInconsistencia;
	private String descripcionIncon;
	private String indicadorLinea;
	
	public DtoInterfazCampoS1E()
	{
		reset();
	}
	
	public void reset()
	{
		descripcion = "";
		tipo = "";
		requerido = false;
		inconsistencia = false;
		tamano = 0;
		valor = "";
		indicadorLinea = "";
		
		tipoDocumento = "";
		numeroDocumento = "";
		consecutivoDocumento = "";
		tipoInconsistencia = TipoInconsistencia.NINGUNO;
		descripcionIncon = "";
	}
	
	public String getDescripcionLinea()
	{
		return getDescripcionLinea(this.indicadorLinea);
	}

	public static String getDescripcionLinea(String indicador)
	{
		
		if(indicador.equals(GeneracionInterfaz1E.indicadorLineaDetCxC))
			return "Linea Detalle Cuentas por Cobrar";
		if(indicador.equals(GeneracionInterfaz1E.indicadorLineaDetCxP))
			return "Linea Detalle Cuentas por Pagar";
		if(indicador.equals(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable))
			return "Linea Detalle Movimiento Contable";
		if(indicador.equals(GeneracionInterfaz1E.indicadorLineaDocumentoContable))
			return "Linea Documento Contable";
		if(indicador.equals(GeneracionInterfaz1E.indicadorLineaFinal))
			return "Linea Final";
		if(indicador.equals(GeneracionInterfaz1E.indicadorLineaInicio))
			return "Linea Inicio";
		if(indicador.equals(GeneracionInterfaz1E.indicadorLineaEventoCartera))
			return "Linea Evento Cartera";
		
		return "";
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isRequerido() {
		return requerido;
	}

	public void setRequerido(boolean requerido) {
		this.requerido = requerido;
	}

	public int getTamano() {
		return tamano;
	}

	public void setTamano(int tamano) {
		this.tamano = tamano;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public boolean isInconsistencia() {
		return inconsistencia;
	}

	public void setInconsistencia(boolean inconsistencia) {
		this.inconsistencia = inconsistencia;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public TipoInconsistencia getTipoInconsistencia() {
		return tipoInconsistencia;
	}

	public void setTipoInconsistencia(TipoInconsistencia tipoInconsistencia) {
		this.tipoInconsistencia = tipoInconsistencia;
	}

	public String getDescripcionIncon() {
		return descripcionIncon;
	}

	public void setDescripcionIncon(String descripcionIncon) {
		this.descripcionIncon = descripcionIncon;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getIndicadorLinea() {
		return indicadorLinea;
	}

	public void setIndicadorLinea(String indicadorLinea) {
		this.indicadorLinea = indicadorLinea;
	}

	/**
	 * @param consecutivoDocumento the consecutivoDocumento to set
	 */
	public void setConsecutivoDocumento(String consecutivoDocumento) {
		this.consecutivoDocumento = consecutivoDocumento;
	}

	/**
	 * @return the consecutivoDocumento
	 */
	public String getConsecutivoDocumento() {
		return consecutivoDocumento;
	}
}