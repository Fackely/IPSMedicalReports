package com.servinte.axioma.generadorReporte.odontologia.dtoReportes;

import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesBD;

public class DtoReporteIngresosVentaTarjetaArchivoPlano {
	
	
	private String descripcionEmpresaInstitucion;
	private String fechaInicial;
	private String fechaFinal;
	private String edad;
	private String sexoComprador;	
	private String descripcionCentroAtencion;	
	private String descripcionCiudadCA;	
	private String descripcionRegionCA;
	private String descripcionPais;	
	private String descripcionDepartamento;	
	private String descripcionTipoTarjeta;
	private String claseTarjeta;
	private String serialInicial;
	private String serialFinal;
	private int cantidad;
	private String fechaVenta;
	private String nroFactura;
	private String convenioTarifa;
	private String primerNombreComprador;
	private String primerApellidoComprador;
	private double valorTotalVenta;
	private String primerNombreVendedor;
	private String primerApellidoVendedor;
	transient private JRDataSource dsListadoResultado;
	private int codigoEmpresaInstitucion;
	private int consecutivoCentroAtencion;
	private String descripcionClaseTarjeta;
	private String valorTotalFormateado;
	private String valorTotalPorCAFormateado;
	private String valorPorInstitucionFormateado;
	private int codigoTipoTarjeta;
	
	public DtoReporteIngresosVentaTarjetaArchivoPlano() {
		
		this.descripcionEmpresaInstitucion = "";
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.edad = "";
		this.sexoComprador = "";		
		this.descripcionCentroAtencion = "";		
		this.descripcionCiudadCA = "";		
		this.descripcionRegionCA = "";		
		this.descripcionPais = "";		
		this.descripcionDepartamento = "";		
		this.descripcionTipoTarjeta = "";
		this.claseTarjeta = "";
		this.serialInicial = "";
		this.serialFinal = "";
		this.cantidad = ConstantesBD.codigoNuncaValido;
		this.fechaVenta = "";
		this.nroFactura = "";
		this.convenioTarifa = "";
		this.primerNombreComprador = "";
		this.primerApellidoComprador = "";
		this.valorTotalVenta = ConstantesBD.codigoNuncaValidoDouble;
		this.primerNombreVendedor = "";
		this.primerApellidoVendedor = "";
		this.codigoEmpresaInstitucion=ConstantesBD.codigoNuncaValido;
		this.consecutivoCentroAtencion = ConstantesBD.codigoNuncaValido;
		this.descripcionClaseTarjeta = "";
		this.valorTotalFormateado = "";
		this.valorTotalPorCAFormateado = "";
		this.valorPorInstitucionFormateado = "";
		this.codigoTipoTarjeta=ConstantesBD.codigoNuncaValido;
	}
	
	public int getCodigoTipoTarjeta() {
		return codigoTipoTarjeta;
	}

	public void setCodigoTipoTarjeta(int codigoTipoTarjeta) {
		this.codigoTipoTarjeta = codigoTipoTarjeta;
	}

	public String getValorPorInstitucionFormateado() {
		return valorPorInstitucionFormateado;
	}

	public void setValorPorInstitucionFormateado(
			String valorPorInstitucionFormateado) {
		this.valorPorInstitucionFormateado = valorPorInstitucionFormateado;
	}

	public String getValorTotalPorCAFormateado() {
		return valorTotalPorCAFormateado;
	}

	public void setValorTotalPorCAFormateado(String valorTotalPorCAFormateado) {
		this.valorTotalPorCAFormateado = valorTotalPorCAFormateado;
	}

	public String getValorTotalFormateado() {
		return valorTotalFormateado;
	}

	public void setValorTotalFormateado(String valorTotalFormateado) {
		this.valorTotalFormateado = valorTotalFormateado;
	}

	public String getDescripcionClaseTarjeta() {
		return descripcionClaseTarjeta;
	}

	public void setDescripcionClaseTarjeta(String descripcionClaseTarjeta) {
		this.descripcionClaseTarjeta = descripcionClaseTarjeta;
	}

	public int getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}

	public void setConsecutivoCentroAtencion(int consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}

	public int getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}

	public void setCodigoEmpresaInstitucion(int codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  descripcionEmpresaInstitucion
	 *
	 * @return retorna la variable descripcionEmpresaInstitucion
	 */
	public String getDescripcionEmpresaInstitucion() {
		return descripcionEmpresaInstitucion;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo descripcionEmpresaInstitucion
	 * @param descripcionEmpresaInstitucion es el valor para el atributo descripcionEmpresaInstitucion 
	 */
	public void setDescripcionEmpresaInstitucion(
			String descripcionEmpresaInstitucion) {
		this.descripcionEmpresaInstitucion = descripcionEmpresaInstitucion;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaInicial
	 *
	 * @return retorna la variable fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaInicial
	 * @param fechaInicial es el valor para el atributo fechaInicial 
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaFinal
	 *
	 * @return retorna la variable fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaFinal
	 * @param fechaFinal es el valor para el atributo fechaFinal 
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  edad
	 *
	 * @return retorna la variable edad
	 */
	public String getEdad() {
		return edad;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo edad
	 * @param edad es el valor para el atributo edad 
	 */
	public void setEdad(String edad) {
		this.edad = edad;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  sexoComprador
	 *
	 * @return retorna la variable sexoComprador
	 */
	public String getSexoComprador() {
		return sexoComprador;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo sexoComprador
	 * @param sexoComprador es el valor para el atributo sexoComprador 
	 */
	public void setSexoComprador(String sexoComprador) {
		this.sexoComprador = sexoComprador;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  descripcionCentroAtencion
	 *
	 * @return retorna la variable descripcionCentroAtencion
	 */
	public String getDescripcionCentroAtencion() {
		return descripcionCentroAtencion;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo descripcionCentroAtencion
	 * @param descripcionCentroAtencion es el valor para el atributo descripcionCentroAtencion 
	 */
	public void setDescripcionCentroAtencion(String descripcionCentroAtencion) {
		this.descripcionCentroAtencion = descripcionCentroAtencion;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  descripcionCiudadCA
	 *
	 * @return retorna la variable descripcionCiudadCA
	 */
	public String getDescripcionCiudadCA() {
		return descripcionCiudadCA;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo descripcionCiudadCA
	 * @param descripcionCiudadCA es el valor para el atributo descripcionCiudadCA 
	 */
	public void setDescripcionCiudadCA(String descripcionCiudadCA) {
		this.descripcionCiudadCA = descripcionCiudadCA;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  descripcionRegionCA
	 *
	 * @return retorna la variable descripcionRegionCA
	 */
	public String getDescripcionRegionCA() {
		return descripcionRegionCA;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo descripcionRegionCA
	 * @param descripcionRegionCA es el valor para el atributo descripcionRegionCA 
	 */
	public void setDescripcionRegionCA(String descripcionRegionCA) {
		this.descripcionRegionCA = descripcionRegionCA;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  descripcionPais
	 *
	 * @return retorna la variable descripcionPais
	 */
	public String getDescripcionPais() {
		return descripcionPais;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo descripcionPais
	 * @param descripcionPais es el valor para el atributo descripcionPais 
	 */
	public void setDescripcionPais(String descripcionPais) {
		this.descripcionPais = descripcionPais;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  descripcionDepartamento
	 *
	 * @return retorna la variable descripcionDepartamento
	 */
	public String getDescripcionDepartamento() {
		return descripcionDepartamento;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo descripcionDepartamento
	 * @param descripcionDepartamento es el valor para el atributo descripcionDepartamento 
	 */
	public void setDescripcionDepartamento(String descripcionDepartamento) {
		this.descripcionDepartamento = descripcionDepartamento;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  descripcionTipoTarjeta
	 *
	 * @return retorna la variable descripcionTipoTarjeta
	 */
	public String getDescripcionTipoTarjeta() {
		return descripcionTipoTarjeta;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo descripcionTipoTarjeta
	 * @param descripcionTipoTarjeta es el valor para el atributo descripcionTipoTarjeta 
	 */
	public void setDescripcionTipoTarjeta(String descripcionTipoTarjeta) {
		this.descripcionTipoTarjeta = descripcionTipoTarjeta;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  claseTarjeta
	 *
	 * @return retorna la variable claseTarjeta
	 */
	public String getClaseTarjeta() {
		return claseTarjeta;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo claseTarjeta
	 * @param claseTarjeta es el valor para el atributo claseTarjeta 
	 */
	public void setClaseTarjeta(String claseTarjeta) {
		this.claseTarjeta = claseTarjeta;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  serialInicial
	 *
	 * @return retorna la variable serialInicial
	 */
	public String getSerialInicial() {
		return serialInicial;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo serialInicial
	 * @param serialInicial es el valor para el atributo serialInicial 
	 */
	public void setSerialInicial(String serialInicial) {
		this.serialInicial = serialInicial;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  serialFinal
	 *
	 * @return retorna la variable serialFinal
	 */
	public String getSerialFinal() {
		return serialFinal;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo serialFinal
	 * @param serialFinal es el valor para el atributo serialFinal 
	 */
	public void setSerialFinal(String serialFinal) {
		this.serialFinal = serialFinal;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  cantidad
	 *
	 * @return retorna la variable cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo cantidad
	 * @param cantidad es el valor para el atributo cantidad 
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaVenta
	 *
	 * @return retorna la variable fechaVenta
	 */
	public String getFechaVenta() {
		return fechaVenta;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaVenta
	 * @param fechaVenta es el valor para el atributo fechaVenta 
	 */
	public void setFechaVenta(String fechaVenta) {
		this.fechaVenta = fechaVenta;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  nroFactura
	 *
	 * @return retorna la variable nroFactura
	 */
	public String getNroFactura() {
		return nroFactura;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo nroFactura
	 * @param nroFactura es el valor para el atributo nroFactura 
	 */
	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  convenioTarifa
	 *
	 * @return retorna la variable convenioTarifa
	 */
	public String getConvenioTarifa() {
		return convenioTarifa;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo convenioTarifa
	 * @param convenioTarifa es el valor para el atributo convenioTarifa 
	 */
	public void setConvenioTarifa(String convenioTarifa) {
		this.convenioTarifa = convenioTarifa;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  primerNombreComprador
	 *
	 * @return retorna la variable primerNombreComprador
	 */
	public String getPrimerNombreComprador() {
		return primerNombreComprador;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo primerNombreComprador
	 * @param primerNombreComprador es el valor para el atributo primerNombreComprador 
	 */
	public void setPrimerNombreComprador(String primerNombreComprador) {
		this.primerNombreComprador = primerNombreComprador;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  primerApellidoComprador
	 *
	 * @return retorna la variable primerApellidoComprador
	 */
	public String getPrimerApellidoComprador() {
		return primerApellidoComprador;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo primerApellidoComprador
	 * @param primerApellidoComprador es el valor para el atributo primerApellidoComprador 
	 */
	public void setPrimerApellidoComprador(String primerApellidoComprador) {
		this.primerApellidoComprador = primerApellidoComprador;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  valorTotalVenta
	 *
	 * @return retorna la variable valorTotalVenta
	 */
	public double getValorTotalVenta() {
		return valorTotalVenta;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo valorTotalVenta
	 * @param valorTotalVenta es el valor para el atributo valorTotalVenta 
	 */
	public void setValorTotalVenta(double valorTotalVenta) {
		this.valorTotalVenta = valorTotalVenta;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  dsListadoResultado
	 *
	 * @return retorna la variable dsListadoResultado
	 */
	public JRDataSource getDsListadoResultado() {
		return dsListadoResultado;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo dsListadoResultado
	 * @param dsListadoResultado es el valor para el atributo dsListadoResultado 
	 */
	public void setDsListadoResultado(JRDataSource dsListadoResultado) {
		this.dsListadoResultado = dsListadoResultado;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  primerNombreVendedor
	 *
	 * @return retorna la variable primerNombreVendedor
	 */
	public String getPrimerNombreVendedor() {
		return primerNombreVendedor;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo primerNombreVendedor
	 * @param primerNombreVendedor es el valor para el atributo primerNombreVendedor 
	 */
	public void setPrimerNombreVendedor(String primerNombreVendedor) {
		this.primerNombreVendedor = primerNombreVendedor;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  primerApellidoVendedor
	 *
	 * @return retorna la variable primerApellidoVendedor
	 */
	public String getPrimerApellidoVendedor() {
		return primerApellidoVendedor;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo primerApellidoVendedor
	 * @param primerApellidoVendedor es el valor para el atributo primerApellidoVendedor 
	 */
	public void setPrimerApellidoVendedor(String primerApellidoVendedor) {
		this.primerApellidoVendedor = primerApellidoVendedor;
	}
	

}
