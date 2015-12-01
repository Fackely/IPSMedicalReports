package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;

import util.ConstantesBD;

/**
 * DTO para el manejo de la información por 
 * centro de atenci&oacute;n de los recibos 
 * de caja por concepto de Anticipos 
 * Recibidos del Convenio asociados 
 * @author Diana Carolina G
 */


public class DtoAnticiposRecibidosConvenio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo que almacena el consecutivo
	 * del centro de atenci&oacute;n
	 */ 
	 
	private int consCentroAtencion;
	
	
	/**
	 * Atributo que almacena la descripci&oacute;n
	 * del centro de atenci&oacute;n
	 */
	 
	private String descCentroAtencion;
	
	
	/**
	 * Atributo que almacena la descripci&oacute;n
	 * del Pa&iacute;s
	 */
	private String descripcionPais;
	
	/**
	 * Atributo que almacena la descripci&oacute;n
	 * de la ciudad
	 */
	private String descripcionCiudad;
	
	/**
	 * Atributo que almacena la descripci&oacute;n de 
	 * una regi&oacute;n de cobertura.
	 */
	private String descripcionRegionCobertura;
	
	/**
	 * Atributo que almacena el codigo
	 * de la institucion. Depende del 
	 * par&aacute;metro Maneja Multiempresa.
	 * S = codigoEmpresaInstitucion
	 * N = codigoInstitucion 
	 */
	private long codigoIns;
	
	
	/**
	 * Atributo que almacena la descripci&oacute;n
	 * de la Instituci&oacute;n
	 */
	private String nombreInstitucion;
	
	
	/**
	 * Atributo que almacena la lista de 
	 * Recibos de caja por centro de atenci&oacute;n
	 */
	private ArrayList<DtoRecibosConceptoAnticiposRecibidosConvenio>  listaRecibosCajaXCentroAtencion;
	
	
	/** Objeto jasper para el subreporte del consolidado por centro de atencion */
    transient private JRDataSource dsListadoRecibosCajaXCentroAtencion;
    
    /**
	 * Atributo que permite indicar si se debe mostrar la columna de concepto
	 * en el reporte.
	 */
	private boolean mostrarConceptos;
	
    
	public DtoAnticiposRecibidosConvenio(){
		reset();
	}

	public DtoAnticiposRecibidosConvenio(String numeroRC){
		reset();
	
	}

	private void reset() {
		
		this.consCentroAtencion=ConstantesBD.codigoNuncaValido;
		this.descCentroAtencion="";
		this.codigoIns=ConstantesBD.codigoNuncaValidoLong;
		this.descripcionPais="";
		this.descripcionCiudad="";
		this.descripcionRegionCobertura="";
		this.nombreInstitucion="";
		this.listaRecibosCajaXCentroAtencion= new ArrayList<DtoRecibosConceptoAnticiposRecibidosConvenio>();
		this.mostrarConceptos= true;
		
	}
	
	public int getConsCentroAtencion() {
		return consCentroAtencion;
	}

	public void setConsCentroAtencion(int consCentroAtencion) {
		this.consCentroAtencion = consCentroAtencion;
	}

	public String getDescCentroAtencion() {
		return descCentroAtencion;
	}

	public void setDescCentroAtencion(String descCentroAtencion) {
		this.descCentroAtencion = descCentroAtencion;
	}
	
	public void setListaRecibosCajaXCentroAtencion(
			ArrayList<DtoRecibosConceptoAnticiposRecibidosConvenio> listaRecibosCajaXCentroAtencion) {
		this.listaRecibosCajaXCentroAtencion = listaRecibosCajaXCentroAtencion;
	}

	public ArrayList<DtoRecibosConceptoAnticiposRecibidosConvenio> getListaRecibosCajaXCentroAtencion() {
		return listaRecibosCajaXCentroAtencion;
	}

	public String getDescripcionPais() {
		return descripcionPais;
	}

	public void setDescripcionPais(String descripcionPais) {
		this.descripcionPais = descripcionPais;
	}

	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}

	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}

	public String getDescripcionRegionCobertura() {
		return descripcionRegionCobertura;
	}

	public void setDescripcionRegionCobertura(String descripcionRegionCobertura) {
		this.descripcionRegionCobertura = descripcionRegionCobertura;
	}

	public String getNombreInstitucion() {
		return nombreInstitucion;
	}

	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}

	public void setCodigoIns(long codigoIns) {
		this.codigoIns = codigoIns;
	}

	public long getCodigoIns() {
		return codigoIns;
	}

	public void setDsListadoRecibosCajaXCentroAtencion(
			JRDataSource dsListadoRecibosCajaXCentroAtencion) {
		this.dsListadoRecibosCajaXCentroAtencion = dsListadoRecibosCajaXCentroAtencion;
	}

	public JRDataSource getDsListadoRecibosCajaXCentroAtencion() {
		return dsListadoRecibosCajaXCentroAtencion;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  mostrarConceptos
	 *
	 * @return retorna la variable mostrarConceptos
	 */
	public boolean isMostrarConceptos() {
		return mostrarConceptos;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo mostrarConceptos
	 * @param mostrarConceptos es el valor para el atributo mostrarConceptos 
	 */
	public void setMostrarConceptos(boolean mostrarConceptos) {
		this.mostrarConceptos = mostrarConceptos;
	}

}
