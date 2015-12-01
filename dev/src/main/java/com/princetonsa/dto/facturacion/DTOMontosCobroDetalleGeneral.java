package com.princetonsa.dto.facturacion;

import java.util.Date;

import com.servinte.axioma.orm.DetalleMonto;
import com.servinte.axioma.orm.TiposMonto;
import com.servinte.axioma.orm.Usuarios;

/**
 * Esta clase se encarga de almacenar temporalmente
 * los datos del detalle general de un monto de cobro 
 * 
 * @author Angela Maria Aguirre
 * @since 2/09/2010
 */
public class DTOMontosCobroDetalleGeneral {
	
	private int detalleCodigo;
	private DetalleMonto detalleMonto;
	private Usuarios usuarios;
	private Double valor;
	private Double porcentaje;
	private int cantidadMonto;
	private Date fechaRegistro;
	private String horaRegistro;
	
	
	public DTOMontosCobroDetalleGeneral(){
		
	}
	
	/**
	 * Constructor para el Delegate.
	 * 
	 * Constructor de la clase
	 * @param tipoMonto
	 * @param nombreTipoMonto
	 * @param valorMonto
	 * @param porcentajeMonto
	 * @param cantidadMonto
	 */
	public DTOMontosCobroDetalleGeneral(int tipoMonto, String nombreTipoMonto, Double valorMonto, Double porcentajeMonto, int cantidadMonto)
	{
		detalleMonto = new DetalleMonto();
		detalleMonto.setTiposMonto(new TiposMonto());
		detalleMonto.getTiposMonto().setCodigo(tipoMonto);
		detalleMonto.getTiposMonto().setNombre(nombreTipoMonto);
		this.valor 			= valorMonto;
		this.porcentaje 	= porcentajeMonto;
		this.cantidadMonto 	= cantidadMonto;
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
	 * del atributo detalleMonto
	
	 * @return retorna la variable detalleMonto 
	 * @author Angela Maria Aguirre 
	 */
	public DetalleMonto getDetalleMonto() {
		return detalleMonto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo detalleMonto
	
	 * @param valor para el atributo detalleMonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setDetalleMonto(DetalleMonto detalleMonto) {
		this.detalleMonto = detalleMonto;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo usuarios
	
	 * @return retorna la variable usuarios 
	 * @author Angela Maria Aguirre 
	 */
	public Usuarios getUsuarios() {
		return usuarios;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo usuarios
	
	 * @param valor para el atributo usuarios 
	 * @author Angela Maria Aguirre 
	 */
	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo valor
	
	 * @return retorna la variable valor 
	 * @author Angela Maria Aguirre 
	 */
	public Double getValor() {
		return valor;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo valor
	
	 * @param valor para el atributo valor 
	 * @author Angela Maria Aguirre 
	 */
	public void setValor(Double valor) {
		this.valor = valor;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo porcentaje
	
	 * @return retorna la variable porcentaje 
	 * @author Angela Maria Aguirre 
	 */
	public Double getPorcentaje() {
		return porcentaje;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo porcentaje
	
	 * @param valor para el atributo porcentaje 
	 * @author Angela Maria Aguirre 
	 */
	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
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
	
}
