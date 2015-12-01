/*
 * Mayo 6, 2008
 */
package com.princetonsa.dto.historiaClinica.parametrizacion;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * Data Transfer Object: Seccion PARAMETRIZABLE
 * @author Sebastián Gómez R.
 *
 */
public class DtoSeccionParametrizable extends DtoElementoParam implements Serializable
{
	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;

	private int columnasSeccion;
	
	private String sexoSeccion;
	
	private String rangoInicialEdad;
	
	private String rangoFInalEdad;
	
	private String tipoSeccion;
	
	private String codigoSeccionPadre;
	
	private String codigoPkDetSeccion;
	
	
	//********ATRIBUTOS QUE SOLO APLICAN PARA SECCIONES DE ESCALAS***************
	private String tipoRespuesta;
	
	//*******ATRIBUTOS QUE SOLO APLICAN PARA EL MANEJO DE SECCIONES CON RESTRICCION VALOR**************
	/**
	 * Campo que me permite saber si la seccion viene de plantilla o viene de componente
	 */
	private boolean vieneDePlantilla;
	
	/**
	 * 
	 * */
	private String indicativoRestriccionValCamp; 
	
	/**
	 * Se resetan datos del DTO
	 *
	 */
	public void clean()
	{
		super.clean();
		
		this.columnasSeccion = 10;		
		
		this.tipoRespuesta = "";
		
		this.tipoSeccion = "";
		
		this.codigoSeccionPadre = "";
		
		this.sexoSeccion = "";
		
		this.rangoFInalEdad = "";
		
		this.rangoInicialEdad = "";
		
		this.vieneDePlantilla = true;
		
		this.indicativoRestriccionValCamp = "";		
		
		this.codigoPkDetSeccion = "";
	}
	
	/**
	 * Cosntructor del DTO
	 *
	 */
	public DtoSeccionParametrizable()
	{
		this.clean();
	}
	
	/**
	 * @return the columnasSeccion
	 */
	public int getColumnasSeccion() {
		return columnasSeccion;
	}

	/**
	 * @param columnasSeccion the columnasSeccion to set
	 */
	public void setColumnasSeccion(int columnasSeccion) {
		this.columnasSeccion = columnasSeccion;
	}
	
	/**
	 * @return the tipoRespuesta
	 */
	public String getTipoRespuesta() {
		return tipoRespuesta;
	}

	/**
	 * @param tipoRespuesta the tipoRespuesta to set
	 */
	public void setTipoRespuesta(String tipoRespuesta) {
		this.tipoRespuesta = tipoRespuesta;
	}
	

	/**
	 * @return the tipoSeccion
	 */
	public String getTipoSeccion() {
		return tipoSeccion;
	}

	/**
	 * @param tipoSeccion the tipoSeccion to set
	 */
	public void setTipoSeccion(String tipoSeccion) {
		this.tipoSeccion = tipoSeccion;
	}

	/**
	 * @return the codigoSeccionPadre
	 */
	public String getCodigoSeccionPadre() {
		return codigoSeccionPadre;
	}

	/**
	 * @param codigoSeccionPadre the codigoSeccionPadre to set
	 */
	public void setCodigoSeccionPadre(String codigoSeccionPadre) {
		this.codigoSeccionPadre = codigoSeccionPadre;
	}	
	
	
	/**
	 * Método para obtener el número de campos visibles
	 * @return
	 */
	public int numeroCamposVisibles()
	{
		int contador = 0;
		
		for(DtoCampoParametrizable campo:this.getCampos())
			if(campo.isMostrar())
				contador++;
		
		return contador;
	}
	
	/**
	 * Se verifica si una subseccion tiene campos con informacion
	 * @return
	 */
	public boolean tieneSubSeccionInformacion()
	{
		boolean tieneInformacion = false;
		
		for(DtoCampoParametrizable campo:this.getCampos())
			if(campo.isHistorico())
				tieneInformacion = true;
		
		return tieneInformacion;
	}
	
	/**
	 * Se verifica si una sección tiene subsecciones o campos con informacion
	 * @return
	 */
	public boolean tieneSeccionInformacion()
	{
		boolean tieneInformacion = false;
		
		for(DtoCampoParametrizable campo:this.getCampos())
			if(campo.isHistorico())
				tieneInformacion = true;
		
		if(this.getSecciones().size()>0)
			tieneInformacion = true;
		
		return tieneInformacion;
	}

	/**
	 * @return the sexoSeccion
	 */
	public String getSexoSeccion() {
		return sexoSeccion;
	}

	/**
	 * @param sexoSeccion the sexoSeccion to set
	 */
	public void setSexoSeccion(String sexoSeccion) {
		this.sexoSeccion = sexoSeccion;
	}

	/**
	 * @return the rangoInicialEdad
	 */
	public String getRangoInicialEdad() {
		return rangoInicialEdad;
	}

	/**
	 * @param rangoInicialEdad the rangoInicialEdad to set
	 */
	public void setRangoInicialEdad(String rangoInicialEdad) {
		this.rangoInicialEdad = rangoInicialEdad;
	}

	/**
	 * @return the rangoFInalEdad
	 */
	public String getRangoFInalEdad() {
		return rangoFInalEdad;
	}

	/**
	 * @param rangoFInalEdad the rangoFInalEdad to set
	 */
	public void setRangoFInalEdad(String rangoFInalEdad) {
		this.rangoFInalEdad = rangoFInalEdad;
	}

	/**
	 * @return the vieneDePlantilla
	 */
	public boolean isVieneDePlantilla() {
		return vieneDePlantilla;
	}

	/**
	 * @param vieneDePlantilla the vieneDePlantilla to set
	 */
	public void setVieneDePlantilla(boolean vieneDePlantilla) {
		this.vieneDePlantilla = vieneDePlantilla;
	}
	
	/**
	 * Método para verificar si una seccion valor opcion está activa
	 * @param listadoSeccionesValorActivas
	 * @return
	 */
	public boolean estaSeccionValorOpcionActiva(String listadoSeccionesValorActivas)
	{
		boolean activa = false;
		String[] codigos = listadoSeccionesValorActivas.split(ConstantesBD.separadorSplit);
		for(int i=0;i<codigos.length;i++)
			if(codigos[i].split("[$][$]")[0].equals(this.getCodigoPK()))
				activa = true;
		return activa;
	}

	/**
	 * @return the indicativoRestriccionValCamp
	 */
	public String getIndicativoRestriccionValCamp() {
		return indicativoRestriccionValCamp;
	}

	/**
	 * @param indicativoRestriccionValCamp the indicativoRestriccionValCamp to set
	 */
	public void setIndicativoRestriccionValCamp(String indicativoRestriccionValCamp) {
		this.indicativoRestriccionValCamp = indicativoRestriccionValCamp;
	}

	/**
	 * @return the codigoPkDetSeccion
	 */
	public String getCodigoPkDetSeccion() {
		return codigoPkDetSeccion;
	}

	/**
	 * @param codigoPkDetSeccion the codigoPkDetSeccion to set
	 */
	public void setCodigoPkDetSeccion(String codigoPkDetSeccion) {
		this.codigoPkDetSeccion = codigoPkDetSeccion;
	}
}