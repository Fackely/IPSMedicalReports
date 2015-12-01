/**
 * 
 */
package com.servinte.axioma.dto.facturacion;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;

import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;

/**
 * Dto para Grupo de Servicio
 * 
 * @author diego
 *
 */
public class GrupoServicioDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6368038890331986405L;

	private int codigo;
	
	private Integer tiposMonto;
	
	private int institucion;
	
	private String descripcion;
	
	private String acronimo;
	
	private boolean activo;
	
	private String tipo;
	
	private Character multiple;
	
	private Integer tipoSalaStandar;
	
	private Byte numDiasUrgente;
	
	private String acroDiasUrgente;
	
	private Byte numDiasNormal;
	
	private String acroDiasNormal;
	
	private int cantidadOrdenesAmbGeneradas;
	
	private List<DtoUnidadesConsulta>listaUnidadesConsulta;
	private JRDataSource grupoUnidadesConsulta;
	private HashMap<Integer,DtoUnidadesConsulta>mapaUnidadesConsulta;
	
	/**
	 * 
	 */
	public GrupoServicioDto() {
		
	}
	
	
	
	/**
	 * @param codigo
	 */
	public GrupoServicioDto(int codigo) {
		this.codigo = codigo;
	}

	public GrupoServicioDto (int codigo, String descripcion) {
		this.codigo=codigo;
		this.descripcion=descripcion;
	}


	/**
	 * @param codigo
	 * @param tiposMonto
	 * @param institucion
	 * @param descripcion
	 * @param acronimo
	 * @param activo
	 * @param tipo
	 * @param multiple
	 * @param tipoSalaStandar
	 * @param numDiasUrgente
	 * @param acroDiasUrgente
	 * @param numDiasNormal
	 * @param acroDiasNormal
	 */
	public GrupoServicioDto(int codigo, Integer tiposMonto, int institucion,
			String descripcion, String acronimo, boolean activo, String tipo,
			Character multiple, Integer tipoSalaStandar, Byte numDiasUrgente,
			String acroDiasUrgente, Byte numDiasNormal, String acroDiasNormal) {
		this.codigo = codigo;
		this.tiposMonto = tiposMonto;
		this.institucion = institucion;
		this.descripcion = descripcion;
		this.acronimo = acronimo;
		this.activo = activo;
		this.tipo = tipo;
		this.multiple = multiple;
		this.tipoSalaStandar = tipoSalaStandar;
		this.numDiasUrgente = numDiasUrgente;
		this.acroDiasUrgente = acroDiasUrgente;
		this.numDiasNormal = numDiasNormal;
		this.acroDiasNormal = acroDiasNormal;
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
	 * @return the tiposMonto
	 */
	public Integer getTiposMonto() {
		return tiposMonto;
	}

	/**
	 * @param tiposMonto the tiposMonto to set
	 */
	public void setTiposMonto(Integer tiposMonto) {
		this.tiposMonto = tiposMonto;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
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
	 * @return the acronimo
	 */
	public String getAcronimo() {
		return acronimo;
	}

	/**
	 * @param acronimo the acronimo to set
	 */
	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the multiple
	 */
	public Character getMultiple() {
		return multiple;
	}

	/**
	 * @param multiple the multiple to set
	 */
	public void setMultiple(Character multiple) {
		this.multiple = multiple;
	}

	/**
	 * @return the tipoSalaStandar
	 */
	public Integer getTipoSalaStandar() {
		return tipoSalaStandar;
	}

	/**
	 * @param tipoSalaStandar the tipoSalaStandar to set
	 */
	public void setTipoSalaStandar(Integer tipoSalaStandar) {
		this.tipoSalaStandar = tipoSalaStandar;
	}

	/**
	 * @return the numDiasUrgente
	 */
	public Byte getNumDiasUrgente() {
		return numDiasUrgente;
	}

	/**
	 * @param numDiasUrgente the numDiasUrgente to set
	 */
	public void setNumDiasUrgente(Byte numDiasUrgente) {
		this.numDiasUrgente = numDiasUrgente;
	}

	/**
	 * @return the acroDiasUrgente
	 */
	public String getAcroDiasUrgente() {
		return acroDiasUrgente;
	}

	/**
	 * @param acroDiasUrgente the acroDiasUrgente to set
	 */
	public void setAcroDiasUrgente(String acroDiasUrgente) {
		this.acroDiasUrgente = acroDiasUrgente;
	}

	/**
	 * @return the numDiasNormal
	 */
	public Byte getNumDiasNormal() {
		return numDiasNormal;
	}

	/**
	 * @param numDiasNormal the numDiasNormal to set
	 */
	public void setNumDiasNormal(Byte numDiasNormal) {
		this.numDiasNormal = numDiasNormal;
	}

	/**
	 * @return the acroDiasNormal
	 */
	public String getAcroDiasNormal() {
		return acroDiasNormal;
	}

	/**
	 * @param acroDiasNormal the acroDiasNormal to set
	 */
	public void setAcroDiasNormal(String acroDiasNormal) {
		this.acroDiasNormal = acroDiasNormal;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GrupoServicioDto [codigo=" + codigo + ", tiposMonto="
				+ tiposMonto + ", institucion=" + institucion
				+ ", descripcion=" + descripcion + ", acronimo=" + acronimo
				+ ", activo=" + activo + ", tipo=" + tipo + ", multiple="
				+ multiple + ", tipoSalaStandar=" + tipoSalaStandar
				+ ", numDiasUrgente=" + numDiasUrgente + ", acroDiasUrgente="
				+ acroDiasUrgente + ", numDiasNormal=" + numDiasNormal
				+ ", acroDiasNormal=" + acroDiasNormal + "]";
	}



	/**
	 * @return the listaUnidadesConsulta
	 */
	public List<DtoUnidadesConsulta> getListaUnidadesConsulta() {
		return listaUnidadesConsulta;
	}



	/**
	 * @param listaUnidadesConsulta the listaUnidadesConsulta to set
	 */
	public void setListaUnidadesConsulta(
			List<DtoUnidadesConsulta> listaUnidadesConsulta) {
		this.listaUnidadesConsulta = listaUnidadesConsulta;
	}



	/**
	 * @return the mapaUnidadesConsulta
	 */
	public HashMap<Integer, DtoUnidadesConsulta> getMapaUnidadesConsulta() {
		return mapaUnidadesConsulta;
	}



	/**
	 * @param mapaUnidadesConsulta the mapaUnidadesConsulta to set
	 */
	public void setMapaUnidadesConsulta(
			HashMap<Integer, DtoUnidadesConsulta> mapaUnidadesConsulta) {
		this.mapaUnidadesConsulta = mapaUnidadesConsulta;
	}



	/**
	 * @return the grupoUnidadesConsulta
	 */
	public JRDataSource getGrupoUnidadesConsulta() {
		return grupoUnidadesConsulta;
	}



	/**
	 * @param grupoUnidadesConsulta the grupoUnidadesConsulta to set
	 */
	public void setGrupoUnidadesConsulta(JRDataSource grupoUnidadesConsulta) {
		this.grupoUnidadesConsulta = grupoUnidadesConsulta;
	}


	/**
	 * @return the cantidadOrdenesAmbGeneradas
	 */
	public int getCantidadOrdenesAmbGeneradas() {
		return cantidadOrdenesAmbGeneradas;
	}



	/**
	 * @param cantidadOrdenesAmbGeneradas the cantidadOrdenesAmbGeneradas to set
	 */
	public void setCantidadOrdenesAmbGeneradas(int cantidadOrdenesAmbGeneradas) {
		this.cantidadOrdenesAmbGeneradas = cantidadOrdenesAmbGeneradas;
	}

}
