package com.servinte.axioma.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hermorhu
 * @created 26-Nov-2012 
 */
public class BackupDetCargosArtConsumoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8508852589367063100L;
	private long logDistribucionCuenta;
	private long codigo;
	private long detCargo;
	private int articulo;
	private int cantidad;
	private BigDecimal valorUnitario;
	private BigDecimal valorTotal;
	private BigDecimal porcentaje;
	private String usuarioModifica;
	private Date fechaModifica;
	private String horaModifica;
	
	/**
	 * 
	 */
	public BackupDetCargosArtConsumoDto() {
		super();
	}

	/**
	 * @param logDistribucionCuenta
	 * @param detCargo
	 * @param articulo
	 * @param cantidad
	 * @param valorUnitario
	 * @param valorTotal
	 * @param porcentaje
	 * @param usuarioModifica
	 * @param fechaModifica
	 * @param horaModifica
	 */
	public BackupDetCargosArtConsumoDto(long codigo, String usuarioModifica, 
			long detCargo, int articulo, int cantidad, BigDecimal valorUnitario,
			BigDecimal valorTotal, BigDecimal porcentaje,
			Date fechaModifica, String horaModifica) {
		
		this.codigo = codigo;
		this.usuarioModifica = usuarioModifica;
		this.detCargo = detCargo;
		this.articulo = articulo;
		this.cantidad = cantidad;
		this.valorUnitario = valorUnitario;
		this.valorTotal = valorTotal;
		this.porcentaje = porcentaje;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the logDistribucionCuenta
	 */
	public long getLogDistribucionCuenta() {
		return logDistribucionCuenta;
	}

	/**
	 * @param logDistribucionCuenta the logDistribucionCuenta to set
	 */
	public void setLogDistribucionCuenta(long logDistribucionCuenta) {
		this.logDistribucionCuenta = logDistribucionCuenta;
	}

	/**
	 * @return the detCargo
	 */
	public long getDetCargo() {
		return detCargo;
	}

	/**
	 * @param detCargo the detCargo to set
	 */
	public void setDetCargo(long detCargo) {
		this.detCargo = detCargo;
	}

	/**
	 * @return the articulo
	 */
	public int getArticulo() {
		return articulo;
	}

	/**
	 * @param articulo the articulo to set
	 */
	public void setArticulo(int articulo) {
		this.articulo = articulo;
	}

	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the valorUnitario
	 */
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	/**
	 * @param valorUnitario the valorUnitario to set
	 */
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	/**
	 * @return the valorTotal
	 */
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * @return the porcentaje
	 */
	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	/**
	 * @param porcentaje the porcentaje to set
	 */
	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the fechaModifica
	 */
	public Date getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the codigo
	 */
	public long getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

}
