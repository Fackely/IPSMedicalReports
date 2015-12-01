package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoResultadoConsultaIngresosOdonto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo que almacena el nombre de un pa&iacute;s
	 */
	private String descripcionPais;
	
	/**
	 * Atributo que almacena el nombre de una ciudad
	 */
	private String descripcionCiudad;
	
	/**
	 * atributo que almacena la descripci&oacute;n de 
	 * una regi&oacute;n de cobertura.
	 */
	private String descripcionRegionCobertura;
	
	/**
	 * Atributo que almacena el nombre de un centro de atenci&oacute;n
	 */
	private String descripcionCentroAtencion;
	
	/**
	 * Atributo que almacena el nombre de la empresa
	 * instituci&oacute;n.
	 */
	private String descripcionEmpresaInstitucion;
	
	/**
	 * permite obtener la informaci&oacute;n de los ingresos consultados.
	 */
	private ArrayList<DtoIngresosOdontologicos> listaIngresosSinValIni;
	
	/**
	 * Atributo que almacena el c&oacute;digo de la instituci&oacute;n a
	 * la que pertenece el centro de atenci&oacute;n.
	 */
	private Integer codigoInstitucion;
	
	/**
	 * permite definir si la instituci&oacute;n asociada al centro de 
	 * atenci&oacute; es multiempresa o no.
	 */
	private String esMultiempresa;
	
	/**
	 * Atributo que almacena los datos de los ingresos con
	 * cita de valoraci&oacute;n inicial.
	 */
	private ArrayList<DtoIngresosOdontologicos> listaIngresosConValIni;
	
	/**
	 * Atributo que almacena el consecutivo del centro de atenci&oacute;n.
	 */
	private Integer consecutivoCentroAtencion;
	
	/**
	 * Atributo que almacena un listado con el consolidado para los ingresos
	 * odontol&oacute;gicos hallados en la consulta.
	 */
	private DtoConsolidadoReporteIngresosOdonto consolidadoIngresosOdonto;
	
	/** Objeto jasper para el subreporte de los ingresos con valoración inicial */
    private JRDataSource dsIngresosConValIni;
    
    /** Objeto jasper para el subreporte de los ingresos con valoración inicial */
    private JRDataSource dsIngresosSinValIni;
    
    /**
     * ayudante para enviar el dto a ireport
     */
    private ArrayList<DtoConsolidadoReporteIngresosOdonto> consolidado;

    /** Objeto jasper para el subreporte del consolidado de ingresos */
    private JRDataSource dsConsolidado;
    
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo descripcionPais
	 * 
	 * @return  Retorna la variable descripcionPais
	 */
	public String getDescripcionPais() {
		return descripcionPais;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo descripcionCiudad
	 * 
	 * @return  Retorna la variable descripcionCiudad
	 */
	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo descripcionRegionCobertura
	 * 
	 * @return  Retorna la variable descripcionRegionCobertura
	 */
	public String getDescripcionRegionCobertura() {
		return descripcionRegionCobertura;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo descripcionCentroAtencion
	 * 
	 * @return  Retorna la variable descripcionCentroAtencion
	 */
	public String getDescripcionCentroAtencion() {
		return descripcionCentroAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo descripcionEmpresaInstitucion
	 * 
	 * @return  Retorna la variable descripcionEmpresaInstitucion
	 */
	public String getDescripcionEmpresaInstitucion() {
		return descripcionEmpresaInstitucion;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo descripcionPais
	 * 
	 * @param  valor para el atributo descripcionPais 
	 */
	public void setDescripcionPais(String descripcionPais) {
		this.descripcionPais = descripcionPais;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo descripcionCiudad
	 * 
	 * @param  valor para el atributo descripcionCiudad 
	 */
	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo descripcionRegionCobertura
	 * 
	 * @param  valor para el atributo descripcionRegionCobertura 
	 */
	public void setDescripcionRegionCobertura(String descripcionRegionCobertura) {
		this.descripcionRegionCobertura = descripcionRegionCobertura;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo descripcionCentroAtencion
	 * 
	 * @param  valor para el atributo descripcionCentroAtencion 
	 */
	public void setDescripcionCentroAtencion(String descripcionCentroAtencion) {
		this.descripcionCentroAtencion = descripcionCentroAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo descripcionEmpresaInstitucion
	 * 
	 * @param  valor para el atributo descripcionEmpresaInstitucion 
	 */
	public void setDescripcionEmpresaInstitucion(
			String descripcionEmpresaInstitucion) {
		this.descripcionEmpresaInstitucion = descripcionEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaIngresosSinValIni
	 * 
	 * @return  Retorna la variable listaIngresosSinValIni
	 */
	public ArrayList<DtoIngresosOdontologicos> getListaIngresosSinValIni() {
		return listaIngresosSinValIni;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaIngresosSinValIni
	 * 
	 * @param  valor para el atributo listaIngresosSinValIni 
	 */
	public void setListaIngresosSinValIni(
			ArrayList<DtoIngresosOdontologicos> listaIngresosSinValIni) {
		this.listaIngresosSinValIni = listaIngresosSinValIni;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoInstitucion
	 * 
	 * @return  Retorna la variable codigoInstitucion
	 */
	public Integer getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoInstitucion
	 * 
	 * @param  valor para el atributo codigoInstitucion 
	 */
	public void setCodigoInstitucion(Integer codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo esMultiempresa
	 * 
	 * @return  Retorna la variable esMultiempresa
	 */
	public String getEsMultiempresa() {
		return esMultiempresa;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo esMultiempresa
	 * 
	 * @param  valor para el atributo esMultiempresa 
	 */
	public void setEsMultiempresa(String esMultiempresa) {
		this.esMultiempresa = esMultiempresa;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaIngresosConValIni
	 * 
	 * @return  Retorna la variable listaIngresosConValIni
	 */
	public ArrayList<DtoIngresosOdontologicos> getListaIngresosConValIni() {
		return listaIngresosConValIni;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaIngresosConValIni
	 * 
	 * @param  valor para el atributo listaIngresosConValIni 
	 */
	public void setListaIngresosConValIni(
			ArrayList<DtoIngresosOdontologicos> listaIngresosConValIni) {
		this.listaIngresosConValIni = listaIngresosConValIni;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo consecutivoCentroAtencion
	 * 
	 * @return  Retorna la variable consecutivoCentroAtencion
	 */
	public Integer getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo consecutivoCentroAtencion
	 * 
	 * @param  valor para el atributo consecutivoCentroAtencion 
	 */
	public void setConsecutivoCentroAtencion(Integer consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo consolidadoIngresosOdonto
	 * 
	 * @return  Retorna la variable consolidadoIngresosOdonto
	 */
	public DtoConsolidadoReporteIngresosOdonto getConsolidadoIngresosOdonto() {
		return consolidadoIngresosOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo consolidadoIngresosOdonto
	 * 
	 * @param  valor para el atributo consolidadoIngresosOdonto 
	 */
	public void setConsolidadoIngresosOdonto(
			DtoConsolidadoReporteIngresosOdonto consolidadoIngresosOdonto) {
		
		this.consolidadoIngresosOdonto = consolidadoIngresosOdonto;
		
		consolidado = new ArrayList<DtoConsolidadoReporteIngresosOdonto>();
		consolidado.add(this.consolidadoIngresosOdonto);
		
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo dsIngresosConValIni
	 * 
	 * @return  Retorna la variable dsIngresosConValIni
	 */
	public JRDataSource getDsIngresosConValIni() {
		return dsIngresosConValIni;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo dsIngresosConValIni
	 * 
	 * @param  valor para el atributo dsIngresosConValIni 
	 */
	public void setDsIngresosConValIni(JRDataSource dsIngresosConValIni) {
		this.dsIngresosConValIni = dsIngresosConValIni;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo dsIngresosSinValIni
	 * 
	 * @return  Retorna la variable dsIngresosSinValIni
	 */
	public JRDataSource getDsIngresosSinValIni() {
		return dsIngresosSinValIni;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo dsIngresosSinValIni
	 * 
	 * @param  valor para el atributo dsIngresosSinValIni 
	 */
	public void setDsIngresosSinValIni(JRDataSource dsIngresosSinValIni) {
		this.dsIngresosSinValIni = dsIngresosSinValIni;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo consolidado
	 * 
	 * @return  Retorna la variable consolidado
	 */
	public ArrayList<DtoConsolidadoReporteIngresosOdonto> getConsolidado() {
		return consolidado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo consolidado
	 * 
	 * @param  valor para el atributo consolidado 
	 */
	public void setConsolidado(
			ArrayList<DtoConsolidadoReporteIngresosOdonto> consolidado) {
		this.consolidado = consolidado;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo dsConsolidado
	 * 
	 * @return  Retorna la variable dsConsolidado
	 */
	public JRDataSource getDsConsolidado() {
		return dsConsolidado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo dsConsolidado
	 * 
	 * @param  valor para el atributo dsConsolidado 
	 */
	public void setDsConsolidado(JRDataSource dsConsolidado) {
		this.dsConsolidado = dsConsolidado;
	}
}
