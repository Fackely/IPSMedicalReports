package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Cristhian Murillo
 */
public class DtoProcesoFacturacionPresupuestoCapitacion implements Serializable
{
	
	/**  * */
	private static final long serialVersionUID = 1L;

	/** cédigo de la factura * */
	private Integer codigo;
	
	/** fecha de la factura  * */
	private Date fecha;
	
	/** hora de la factura * */
	private String hora;
	
	/** estado de la factura * */
	private Integer estado;
	
	/** consecutivo de la factura * */
	private Long consecutivoFactura;
	
	/** intitución de la factura * */
	private Integer institucion;
	
	/** Cantidad cargada en la factura * */
	private Integer cantidadCargada;
	
	/** Valor unitario cargado en la factura * */
	private BigDecimal valorUnitarioCargado;
	
	/** Resultado de la multiplicación de la cantidad por el valor unitario cargado en la factura * */
	private BigDecimal totalCantidadCargadaXvalorUnitarioCargado;
	
	/** Articulo facturado * */
	private Integer articuloDetCargo;
	
	/** Nombre Articulo facturado * */
	private String nombreArticuloDetCargo;
	
	/** Servicio facturado * */
	private Integer servicioDetCargo;
	
	/** Nombre Servicio facturado * */
	private String nombreServicioDetCargo;
	
	/** Nivel de atención del artículo * */
	private Long nivelAtencionArticulo;
	
	/** Nivel de atención del servicio * */
	private Long nivelAtencionServicio;
	
	/** Grupo de servicio del servicio * */
	private Integer grupoServicio;
	
	/** Subgrupo Inventario del Articulo * */
	private Integer subGrupoArticulo;
	
	/** Clase Inventario del Articulo * */
	private Integer claseInventarioArticulo;
	
	/** * Convenio busqueda */
	private Integer convenio;
	
	/** * contrato busqueda  */
	private Integer contrato;
	
	/** * Fecha Anulación de la factura  */
	private Date fechaAnulacion;
	
	
	/** Valor unitario cargado en la factura * */
	private Double valorUnitarioCargadoDouble;
	
	/** Resultado de la multiplicación de la cantidad por el valor unitario cargado en la factura * */
	private Double totalCantidadCargadaXvalorUnitarioCargadoDouble;
	
	
	
	/** constructor de la clase */
	public DtoProcesoFacturacionPresupuestoCapitacion (){
		rese();
	}
	
	
	/* Parametros de búsqueda para la factura */
	private Date fechaInicial;
	private Date fechafinal;
	private boolean excluiranuladas;
	private boolean soloAnuladas;
	
	
	
	/** Reset de la forma  * */
	private void rese() 
	{
		this.codigo					= null;
		this.fecha					= null;
		this.hora					= null;
		this.estado					= null;
		this.consecutivoFactura		= null;
		this.institucion			= null;
		
		this.fechaInicial			= null;
		this.fechafinal				= null;
		this.excluiranuladas		= false;
		this.soloAnuladas			= false;
		
		this.cantidadCargada		= null;
		this.valorUnitarioCargado	= null;
		this.totalCantidadCargadaXvalorUnitarioCargado	= null;
		
		this.articuloDetCargo		= null;
		this.servicioDetCargo		= null;
		
		this.nivelAtencionArticulo	= null;
		this.nivelAtencionServicio	= null;
		
		this.nombreArticuloDetCargo = null;
		this.nombreServicioDetCargo = null;
		
		this.convenio 				= null;
		this.contrato 				= null;
		
		this.grupoServicio			= null;
		this.subGrupoArticulo		= null;
		this.claseInventarioArticulo= null;
		
		this.fechaAnulacion			= null;
		
		this.valorUnitarioCargadoDouble = null;
		this.totalCantidadCargadaXvalorUnitarioCargadoDouble = null;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo codigo
	 * @return retorna la variable codigo 
	 * @author Cristhian Murillo
	 */
	public Integer getCodigo() {
		return codigo;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo codigo
	 * @param valor para el atributo codigo 
	 * @author Cristhian Murillo
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo fecha
	 * @return retorna la variable fecha 
	 * @author Cristhian Murillo
	 */
	public Date getFecha() {
		return fecha;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo fecha
	 * @param valor para el atributo fecha 
	 * @author Cristhian Murillo
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo hora
	 * @return retorna la variable hora 
	 * @author Cristhian Murillo
	 */
	public String getHora() {
		return hora;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo hora
	 * @param valor para el atributo hora 
	 * @author Cristhian Murillo
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo estado
	 * @return retorna la variable estado 
	 * @author Cristhian Murillo
	 */
	public Integer getEstado() {
		return estado;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo estado
	 * @param valor para el atributo estado 
	 * @author Cristhian Murillo
	 */
	public void setEstado(Integer estado) {
		this.estado = estado;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo consecutivoFactura
	 * @return retorna la variable consecutivoFactura 
	 * @author Cristhian Murillo
	 */
	public Long getConsecutivoFactura() {
		return consecutivoFactura;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo consecutivoFactura
	 * @param valor para el atributo consecutivoFactura 
	 * @author Cristhian Murillo
	 */
	public void setConsecutivoFactura(Long consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo institucion
	 * @return retorna la variable institucion 
	 * @author Cristhian Murillo
	 */
	public Integer getInstitucion() {
		return institucion;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo institucion
	 * @param valor para el atributo institucion 
	 * @author Cristhian Murillo
	 */
	public void setInstitucion(Integer institucion) {
		this.institucion = institucion;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo cantidadCargada
	 * @return retorna la variable cantidadCargada 
	 * @author Cristhian Murillo
	 */
	public Integer getCantidadCargada() {
		return cantidadCargada;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo cantidadCargada
	 * @param valor para el atributo cantidadCargada 
	 * @author Cristhian Murillo
	 */
	public void setCantidadCargada(Integer cantidadCargada) {
		this.cantidadCargada = cantidadCargada;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo valorUnitarioCargado
	 * @return retorna la variable valorUnitarioCargado 
	 * @author Cristhian Murillo
	 */
	public BigDecimal getValorUnitarioCargado() {
		return valorUnitarioCargado;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo valorUnitarioCargado
	 * @param valor para el atributo valorUnitarioCargado 
	 * @author Cristhian Murillo
	 */
	public void setValorUnitarioCargado(BigDecimal valorUnitarioCargado) {
		this.valorUnitarioCargado = valorUnitarioCargado;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo totalCantidadCargadaXvalorUnitarioCargado
	 * @return retorna la variable totalCantidadCargadaXvalorUnitarioCargado 
	 * @author Cristhian Murillo
	 */
	public BigDecimal getTotalCantidadCargadaXvalorUnitarioCargado() {
		return totalCantidadCargadaXvalorUnitarioCargado;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo totalCantidadCargadaXvalorUnitarioCargado
	 * @param valor para el atributo totalCantidadCargadaXvalorUnitarioCargado 
	 * @author Cristhian Murillo
	 */
	public void setTotalCantidadCargadaXvalorUnitarioCargado(
			BigDecimal totalCantidadCargadaXvalorUnitarioCargado) {
		this.totalCantidadCargadaXvalorUnitarioCargado = totalCantidadCargadaXvalorUnitarioCargado;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo articuloDetCargo
	 * @return retorna la variable articuloDetCargo 
	 * @author Cristhian Murillo
	 */
	public Integer getArticuloDetCargo() {
		return articuloDetCargo;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo articuloDetCargo
	 * @param valor para el atributo articuloDetCargo 
	 * @author Cristhian Murillo
	 */
	public void setArticuloDetCargo(Integer articuloDetCargo) {
		this.articuloDetCargo = articuloDetCargo;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo nombreArticuloDetCargo
	 * @return retorna la variable nombreArticuloDetCargo 
	 * @author Cristhian Murillo
	 */
	public String getNombreArticuloDetCargo() {
		return nombreArticuloDetCargo;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo nombreArticuloDetCargo
	 * @param valor para el atributo nombreArticuloDetCargo 
	 * @author Cristhian Murillo
	 */
	public void setNombreArticuloDetCargo(String nombreArticuloDetCargo) {
		this.nombreArticuloDetCargo = nombreArticuloDetCargo;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo servicioDetCargo
	 * @return retorna la variable servicioDetCargo 
	 * @author Cristhian Murillo
	 */
	public Integer getServicioDetCargo() {
		return servicioDetCargo;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo servicioDetCargo
	 * @param valor para el atributo servicioDetCargo 
	 * @author Cristhian Murillo
	 */
	public void setServicioDetCargo(Integer servicioDetCargo) {
		this.servicioDetCargo = servicioDetCargo;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo nombreServicioDetCargo
	 * @return retorna la variable nombreServicioDetCargo 
	 * @author Cristhian Murillo
	 */
	public String getNombreServicioDetCargo() {
		return nombreServicioDetCargo;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo nombreServicioDetCargo
	 * @param valor para el atributo nombreServicioDetCargo 
	 * @author Cristhian Murillo
	 */
	public void setNombreServicioDetCargo(String nombreServicioDetCargo) {
		this.nombreServicioDetCargo = nombreServicioDetCargo;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo nivelAtencionArticulo
	 * @return retorna la variable nivelAtencionArticulo 
	 * @author Cristhian Murillo
	 */
	public Long getNivelAtencionArticulo() {
		return nivelAtencionArticulo;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo nivelAtencionArticulo
	 * @param valor para el atributo nivelAtencionArticulo 
	 * @author Cristhian Murillo
	 */
	public void setNivelAtencionArticulo(Long nivelAtencionArticulo) {
		this.nivelAtencionArticulo = nivelAtencionArticulo;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo nivelAtencionServicio
	 * @return retorna la variable nivelAtencionServicio 
	 * @author Cristhian Murillo
	 */
	public Long getNivelAtencionServicio() {
		return nivelAtencionServicio;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo nivelAtencionServicio
	 * @param valor para el atributo nivelAtencionServicio 
	 * @author Cristhian Murillo
	 */
	public void setNivelAtencionServicio(Long nivelAtencionServicio) {
		this.nivelAtencionServicio = nivelAtencionServicio;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo convenio
	 * @return retorna la variable convenio 
	 * @author Cristhian Murillo
	 */
	public Integer getConvenio() {
		return convenio;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo convenio
	 * @param valor para el atributo convenio 
	 * @author Cristhian Murillo
	 */
	public void setConvenio(Integer convenio) {
		this.convenio = convenio;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo contrato
	 * @return retorna la variable contrato 
	 * @author Cristhian Murillo
	 */
	public Integer getContrato() {
		return contrato;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo contrato
	 * @param valor para el atributo contrato 
	 * @author Cristhian Murillo
	 */
	public void setContrato(Integer contrato) {
		this.contrato = contrato;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo fechaInicial
	 * @return retorna la variable fechaInicial 
	 * @author Cristhian Murillo
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo fechaInicial
	 * @param valor para el atributo fechaInicial 
	 * @author Cristhian Murillo
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo fechafinal
	 * @return retorna la variable fechafinal 
	 * @author Cristhian Murillo
	 */
	public Date getFechafinal() {
		return fechafinal;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo fechafinal
	 * @param valor para el atributo fechafinal 
	 * @author Cristhian Murillo
	 */
	public void setFechafinal(Date fechafinal) {
		this.fechafinal = fechafinal;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo excluiranuladas
	 * @return retorna la variable excluiranuladas 
	 * @author Cristhian Murillo
	 */
	public boolean isExcluiranuladas() {
		return excluiranuladas;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo excluiranuladas
	 * @param valor para el atributo excluiranuladas 
	 * @author Cristhian Murillo
	 */
	public void setExcluiranuladas(boolean excluiranuladas) {
		this.excluiranuladas = excluiranuladas;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo grupoServicio
	 * @return retorna la variable grupoServicio 
	 * @author Cristhian Murillo
	 */
	public Integer getGrupoServicio() {
		return grupoServicio;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo grupoServicio
	 * @param valor para el atributo grupoServicio 
	 * @author Cristhian Murillo
	 */
	public void setGrupoServicio(Integer grupoServicio) {
		this.grupoServicio = grupoServicio;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo subGrupoArticulo
	 * @return retorna la variable subGrupoArticulo 
	 * @author Cristhian Murillo
	 */
	public Integer getSubGrupoArticulo() {
		return subGrupoArticulo;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo subGrupoArticulo
	 * @param valor para el atributo subGrupoArticulo 
	 * @author Cristhian Murillo
	 */
	public void setSubGrupoArticulo(Integer subGrupoArticulo) {
		this.subGrupoArticulo = subGrupoArticulo;
	}



	/**
	 * Este Método se encarga de obtener el valor del atributo claseInventarioArticulo
	 * @return retorna la variable claseInventarioArticulo 
	 * @author Cristhian Murillo
	 */
	public Integer getClaseInventarioArticulo() {
		return claseInventarioArticulo;
	}



	/**
	 * Este Método se encarga de establecer el valor del atributo claseInventarioArticulo
	 * @param valor para el atributo claseInventarioArticulo 
	 * @author Cristhian Murillo
	 */
	public void setClaseInventarioArticulo(Integer claseInventarioArticulo) {
		this.claseInventarioArticulo = claseInventarioArticulo;
	}



	/**
	 * @return valor de fechaAnulacion
	 */
	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}



	/**
	 * @param fechaAnulacion el fechaAnulacion para asignar
	 */
	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}



	/**
	 * @return valor de soloAnuladas
	 */
	public boolean isSoloAnuladas() {
		return soloAnuladas;
	}



	/**
	 * @param soloAnuladas el soloAnuladas para asignar
	 */
	public void setSoloAnuladas(boolean soloAnuladas) {
		this.soloAnuladas = soloAnuladas;
	}



	/**
	 * @return valor de valorUnitarioCargadoDouble
	 */
	public Double getValorUnitarioCargadoDouble() {
		return valorUnitarioCargadoDouble;
	}



	/**
	 * @param valorUnitarioCargadoDouble el valorUnitarioCargadoDouble para asignar
	 */
	public void setValorUnitarioCargadoDouble(Double valorUnitarioCargadoDouble) {
		this.valorUnitarioCargadoDouble = valorUnitarioCargadoDouble;
		this.valorUnitarioCargado = new BigDecimal(this.valorUnitarioCargadoDouble);
	}



	/**
	 * @return valor de totalCantidadCargadaXvalorUnitarioCargadoDouble
	 */
	public Double getTotalCantidadCargadaXvalorUnitarioCargadoDouble() {
		return totalCantidadCargadaXvalorUnitarioCargadoDouble;
	}



	/**
	 * @param totalCantidadCargadaXvalorUnitarioCargadoDouble el totalCantidadCargadaXvalorUnitarioCargadoDouble para asignar
	 */
	public void setTotalCantidadCargadaXvalorUnitarioCargadoDouble(
			Double totalCantidadCargadaXvalorUnitarioCargadoDouble) {
		this.totalCantidadCargadaXvalorUnitarioCargadoDouble = totalCantidadCargadaXvalorUnitarioCargadoDouble;
		this.totalCantidadCargadaXvalorUnitarioCargado = new BigDecimal(this.totalCantidadCargadaXvalorUnitarioCargadoDouble);
	}



	
}
