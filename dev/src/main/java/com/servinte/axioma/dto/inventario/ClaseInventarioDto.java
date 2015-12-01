package com.servinte.axioma.dto.inventario;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;

import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;


/**
 * Dto para mapear datos de llas Clases de Inventario
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:00 p.m.
 */
public class ClaseInventarioDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4886896990772932630L;
	
	/**
	 * Atributo que representa el códigoPK de la clase de inventario
	 */
	private int codigo;
	
	/**
	 * Atributo que representa el nombre de la clase de inventario
	 */
	private String nombre;

	/**
	 * Atributo que representa el codigoPk del grupo de inventario
	 */
	private Integer codigoGrupo;

	private int cantidadOrdenesAmbGeneradas;
	
	private List<DtoUnidadesConsulta>listaUnidadesConsulta;
	private JRDataSource grupoUnidadesConsulta;
	private HashMap<Integer,DtoUnidadesConsulta>mapaUnidadesConsulta;

	/**
	 * Constructor de la clase
	 */
	public ClaseInventarioDto(){
		
	}
	
	/**
	 * Constructor para mapear la consulta de clase de inventario por subgrupo
	 * 
	 * @param codigo
	 * @param nombre
	 * @param grupo
	 */
	public ClaseInventarioDto(int codigo, String nombre, Integer grupo) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.codigoGrupo = grupo;
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

	public Integer getCodigoGrupo() {
		return codigoGrupo;
	}

	public void setCodigoGrupo(Integer codigoGrupo) {
		this.codigoGrupo = codigoGrupo;
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
