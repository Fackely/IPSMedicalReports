package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Esta clase almacena el resultado de la consulta órdenes ambulatorias y órdenes médicas en el sistema 
 * para ser utilizado en el proceso cargos   (Anexo 1030)
 * 
 * @author Camilo Gómez
 */
public class DtoConsultaProcesoCargosCuenta implements Serializable{


	/** * */
	private static final long serialVersionUID = 1L;
	
	
	/** Tarifa del  articulo o servicio * */
	private BigDecimal tarifa;
	
	/** Código del Servicio * */
	private Integer codigoServicio;
	
	/** Código del Articulo  * */
	private Integer codigoArticulo;
	
	/** Nombre del Servicio * */
	private String nombreServicio;
	
	/** Nombre del Articulo  * */
	private String nombreArticulo;
	
	/** Nivel de atención del artículo * */
	private Long nivelAtencionArticulo;
	
	/** Nivel de atención del servicio * */
	private Long nivelAtencionServicio;
	
	/** Código del Grupo de servicio * */
	private Integer codigoGrupoServicio;
	
	/** Nombre del Grupo de servicio * */
	private String descripcionGrupoServicio;
	
	/** Código de la Clase de Inventario * */
	private Integer claseInventario;
	
	/** Cantidad ordenada  * */
	private Integer cantidad;
	
	/** Fecha en la que se generó la orden médica o ambulatoria * */
	private Date fecha;
	
	/**Numero de l convenio * */
	private Integer convenio;
	
	/**Numero del contrato * */
	private Integer contrato;
	
	/** Numero de la Solicitud * */
	private Integer numeroSolicitud;
	
	/** Numero de la Orden Ambulatoria * */
	private Long numeroOrdenAmbulatoria;
			
	/** Subgrupo del articulo * */
	private Integer subgrupoInventario;
	
	
	/**
	 * Constructor de la clase
	 */
	public DtoConsultaProcesoCargosCuenta() {
		this.reset();
	}
	
	/** 
	 * Este método inicializa los valores de los atributos de la clase 
	 */
	private void reset() 
	{ 
		this.tarifa							= null;
		this.codigoServicio					= null;
		this.nombreServicio					= "";
		this.codigoArticulo					= null;
		this.nombreArticulo					= "";
		this.nivelAtencionArticulo			= null;
		this.nivelAtencionServicio			= null;
		this.codigoGrupoServicio			= null;
		this.descripcionGrupoServicio		= "";
		this.claseInventario				= null;
		this.fecha							= null;
		this.convenio						= null;
		this.contrato						= null;
		this.numeroSolicitud				= null;
		this.subgrupoInventario				= null;
	}

	
	/**
	 * Método que retorna el valor del atributo codigoServicio
	 * @return codigoServicio
	 * @author Camilo Gómez
	 */
	public Integer getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo codigoServicio
	 * @param codigoServicio valor que se va a almacenar en el atributo codigoServicio
	 * @author Camilo Gómez
	 */
	public void setCodigoServicio(Integer codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * Método que retorna el valor del atributo nombreServicio
	 * @return nombreServicio
	 * @author Camilo Gómez
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo nombreServicio
	 * @param nombreServicio valor que se va a almacenar en el atributo nombreServicio
	 * @author Camilo Gómez
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	/**
	 * Método que retorna el valor del atributo codigoArticulo
	 * @return codigoArticulo
	 * @author Camilo Gómez
	 */
	public Integer getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo codigoArticulo
	 * @param codigoArticulo valor que se va a almacenar en el atributo codigoArticulo
	 * @author Camilo Gómez
	 */
	public void setCodigoArticulo(Integer codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * Método que retorna el valor del atributo nombreArticulo
	 * @return nombreArticulo
	 * @author Camilo Gómez
	 */
	public String getNombreArticulo() {
		return nombreArticulo;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo nombreArticulo
	 * @param nombreArticulo valor que se va a almacenar en el atributo nombreArticulo
	 * @author Camilo Gómez
	 */
	public void setNombreArticulo(String nombreArticulo) {
		this.nombreArticulo = nombreArticulo;
	}

	/**
	 * Método que retorna el valor del atributo nivelAtencionArticulo
	 * @return nivelAtencionArticulo
	 * @author Camilo Gómez
	 */
	public Long getNivelAtencionArticulo() {
		return nivelAtencionArticulo;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo nivelAtencionArticulo
	 * @param nivelAtencionArticulo valor que se va a almacenar en el atributo nivelAtencionArticulo
	 * @author Camilo Gómez
	 */
	public void setNivelAtencionArticulo(Long nivelAtencionArticulo) {
		this.nivelAtencionArticulo = nivelAtencionArticulo;
	}

	/**
	 * Método que retorna el valor del atributo nivelAtencionServicio
	 * @return nivelAtencionServicio
	 * @author Camilo Gómez
	 */
	public Long getNivelAtencionServicio() {
		return nivelAtencionServicio;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo nivelAtencionServicio
	 * @param nivelAtencionServicio valor que se va a almacenar en el atributo nivelAtencionServicio
	 * @author Camilo Gómez
	 */
	public void setNivelAtencionServicio(Long nivelAtencionServicio) {
		this.nivelAtencionServicio = nivelAtencionServicio;
	}

	/**
	 * Método que retorna el valor del atributo codigoGrupoServicio
	 * @return codigoGrupoServicio
	 * @author Camilo Gómez
	 */
	public Integer getCodigoGrupoServicio() {
		return codigoGrupoServicio;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo codigoGrupoServicio
	 * @param codigoGrupoServicio valor que se va a almacenar en el atributo codigoGrupoServicio
	 * @author Camilo Gómez
	 */
	public void setCodigoGrupoServicio(Integer codigoGrupoServicio) {
		this.codigoGrupoServicio = codigoGrupoServicio;
	}

	/**
	 * Método que retorna el valor del atributo descripcionGrupoServicio
	 * @return descripcionGrupoServicio
	 * @author Camilo Gómez
	 */
	public String getDescripcionGrupoServicio() {
		return descripcionGrupoServicio;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo descripcionGrupoServicio
	 * @param descripcionGrupoServicio valor que se va a almacenar en el atributo descripcionGrupoServicio
	 * @author Camilo Gómez
	 */
	public void setDescripcionGrupoServicio(String descripcionGrupoServicio) {
		this.descripcionGrupoServicio = descripcionGrupoServicio;
	}

	/**
	 * Método que retorna el valor del atributo claseInventario
	 * @return claseInventario
	 * @author Camilo Gómez
	 */
	public Integer getClaseInventario() {
		return claseInventario;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo claseInventario
	 * @param claseInventario valor que se va a almacenar en el atributo claseInventario
	 * @author Camilo Gómez
	 */
	public void setClaseInventario(Integer claseInventario) {
		this.claseInventario = claseInventario;
	}


	/**
	 * Método que retorna el valor del atributo fecha
	 * @return fecha
	 * @author Camilo Gómez
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo fecha
	 * @param fecha valor que se va a almacenar en el atributo fecha
	 * @author Camilo Gómez
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * Método que retorna el valor del atributo convenio
	 * @return convenio
	 * @author Camilo Gómez
	 */
	public Integer getConvenio() {
		return convenio;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo convenio
	 * @param convenio valor que se va a almacenar en el atributo convenio
	 * @author Camilo Gómez
	 */
	public void setConvenio(Integer convenio) {
		this.convenio = convenio;
	}

	/**
	 * Método que retorna el valor del atributo contrato
	 * @return contrato
	 * @author Camilo Gómez
	 */
	public Integer getContrato() {
		return contrato;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo contrato
	 * @param contrato valor que se va a almacenar en el atributo contrato
	 * @author Camilo Gómez
	 */
	public void setContrato(Integer contrato) {
		this.contrato = contrato;
	}

	/**
	 * Método que retorna el valor del atributo numeroSolicitud
	 * @return numeroSolicitud
	 * @author Camilo Gómez
	 */
	public Integer getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo numeroSolicitud
	 * @param numeroSolicitud valor que se va a almacenar en el atributo numeroSolicitud
	 * @author Camilo Gómez
	 */
	public void setNumeroSolicitud(Integer numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}


	/**
	 * Método que retorna el valor del atributo subgrupoInventario
	 * @return subgrupoInventario
	 * @author Camilo Gómez
	 */
	public Integer getSubgrupoInventario() {
		return subgrupoInventario;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo subgrupoInventario
	 * @param subgrupoInventario valor que se va a almacenar en el atributo subgrupoInventario
	 * @author Camilo Gómez
	 */
	public void setSubgrupoInventario(Integer subgrupoInventario) {
		this.subgrupoInventario = subgrupoInventario;
	}

	public void setNumeroOrdenAmbulatoria(Long numeroOrdenAmbulatoria) {
		this.numeroOrdenAmbulatoria = numeroOrdenAmbulatoria;
	}

	public Long getNumeroOrdenAmbulatoria() {
		return numeroOrdenAmbulatoria;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setTarifa(BigDecimal tarifa) {
		this.tarifa = tarifa;
	}

	public BigDecimal getTarifa() {
		return tarifa;
	}

	

}
