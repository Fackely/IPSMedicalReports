package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.Date;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 13/09/2010
 */
public class DTOHistoMontoAgrupacionArticulo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int codigo;
	private int detalleCodigo;
	private int subgrupoInventario;
	private String acronimoNaturaleza;
	private int cantidadArticulo;
	private int cantidadMonto;
	private Double valorMonto;
	private int codigoInstitucion;
	private Date fechaRegistro;
	private String horaRegistro;
	private String accionRealizada;
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigo
	
	 * @return retorna la variable codigo 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigo
	
	 * @param valor para el atributo codigo 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo detalleCodigo
	
	 * @return retorna la variable detalleCodigo 
	 * @author Angela Maria Aguirre 
	 */
	public int getDetalleCodigo() {
		return detalleCodigo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo detalleCodigo
	
	 * @param valor para el atributo detalleCodigo 
	 * @author Angela Maria Aguirre 
	 */
	public void setDetalleCodigo(int detalleCodigo) {
		this.detalleCodigo = detalleCodigo;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo subgrupoInventario
	
	 * @return retorna la variable subgrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public int getSubgrupoInventario() {
		return subgrupoInventario;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo subgrupoInventario
	
	 * @param valor para el atributo subgrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public void setSubgrupoInventario(int subgrupoInventario) {
		this.subgrupoInventario = subgrupoInventario;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo acronimoNaturaleza
	
	 * @return retorna la variable acronimoNaturaleza 
	 * @author Angela Maria Aguirre 
	 */
	public String getAcronimoNaturaleza() {
		return acronimoNaturaleza;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo acronimoNaturaleza
	
	 * @param valor para el atributo acronimoNaturaleza 
	 * @author Angela Maria Aguirre 
	 */
	public void setAcronimoNaturaleza(String acronimoNaturaleza) {
		this.acronimoNaturaleza = acronimoNaturaleza;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo cantidadArticulo
	
	 * @return retorna la variable cantidadArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public int getCantidadArticulo() {
		return cantidadArticulo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo cantidadArticulo
	
	 * @param valor para el atributo cantidadArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public void setCantidadArticulo(int cantidadArticulo) {
		this.cantidadArticulo = cantidadArticulo;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo cantidadMonto
	
	 * @return retorna la variable cantidadMonto 
	 * @author Angela Maria Aguirre 
	 */
	public int getCantidadMonto() {
		return cantidadMonto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo cantidadMonto
	
	 * @param valor para el atributo cantidadMonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setCantidadMonto(int cantidadMonto) {
		this.cantidadMonto = cantidadMonto;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoInstitucion
	
	 * @return retorna la variable codigoInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoInstitucion
	
	 * @param valor para el atributo codigoInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaRegistro
	
	 * @return retorna la variable fechaRegistro 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaRegistro
	
	 * @param valor para el atributo fechaRegistro 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo horaRegistro
	
	 * @return retorna la variable horaRegistro 
	 * @author Angela Maria Aguirre 
	 */
	public String getHoraRegistro() {
		return horaRegistro;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo horaRegistro
	
	 * @param valor para el atributo horaRegistro 
	 * @author Angela Maria Aguirre 
	 */
	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo accionRealizada
	
	 * @return retorna la variable accionRealizada 
	 * @author Angela Maria Aguirre 
	 */
	public String getAccionRealizada() {
		return accionRealizada;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo accionRealizada
	
	 * @param valor para el atributo accionRealizada 
	 * @author Angela Maria Aguirre 
	 */
	public void setAccionRealizada(String accionRealizada) {
		this.accionRealizada = accionRealizada;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo valorMonto
	
	 * @return retorna la variable valorMonto 
	 * @author Angela Maria Aguirre 
	 */
	public Double getValorMonto() {
		return valorMonto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo valorMonto
	
	 * @param valor para el atributo valorMonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setValorMonto(Double valorMonto) {
		this.valorMonto = valorMonto;
	}
	
}
