package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Esta clase almacena el resultado de la consulta �rdenes ambulatorias y �rdenes m�dicas en el sistema 
 * para ser utilizado en el proceso cargos   (Anexo 1030)
 * 
 * @author Camilo G�mez
 */
public class DtoConsultaProcesoCargosCuenta implements Serializable{


	/** * */
	private static final long serialVersionUID = 1L;
	
	
	/** Tarifa del  articulo o servicio * */
	private BigDecimal tarifa;
	
	/** C�digo del Servicio * */
	private Integer codigoServicio;
	
	/** C�digo del Articulo  * */
	private Integer codigoArticulo;
	
	/** Nombre del Servicio * */
	private String nombreServicio;
	
	/** Nombre del Articulo  * */
	private String nombreArticulo;
	
	/** Nivel de atenci�n del art�culo * */
	private Long nivelAtencionArticulo;
	
	/** Nivel de atenci�n del servicio * */
	private Long nivelAtencionServicio;
	
	/** C�digo del Grupo de servicio * */
	private Integer codigoGrupoServicio;
	
	/** Nombre del Grupo de servicio * */
	private String descripcionGrupoServicio;
	
	/** C�digo de la Clase de Inventario * */
	private Integer claseInventario;
	
	/** Cantidad ordenada  * */
	private Integer cantidad;
	
	/** Fecha en la que se gener� la orden m�dica o ambulatoria * */
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
	 * Este m�todo inicializa los valores de los atributos de la clase 
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
	 * M�todo que retorna el valor del atributo codigoServicio
	 * @return codigoServicio
	 * @author Camilo G�mez
	 */
	public Integer getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo codigoServicio
	 * @param codigoServicio valor que se va a almacenar en el atributo codigoServicio
	 * @author Camilo G�mez
	 */
	public void setCodigoServicio(Integer codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * M�todo que retorna el valor del atributo nombreServicio
	 * @return nombreServicio
	 * @author Camilo G�mez
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo nombreServicio
	 * @param nombreServicio valor que se va a almacenar en el atributo nombreServicio
	 * @author Camilo G�mez
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	/**
	 * M�todo que retorna el valor del atributo codigoArticulo
	 * @return codigoArticulo
	 * @author Camilo G�mez
	 */
	public Integer getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo codigoArticulo
	 * @param codigoArticulo valor que se va a almacenar en el atributo codigoArticulo
	 * @author Camilo G�mez
	 */
	public void setCodigoArticulo(Integer codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * M�todo que retorna el valor del atributo nombreArticulo
	 * @return nombreArticulo
	 * @author Camilo G�mez
	 */
	public String getNombreArticulo() {
		return nombreArticulo;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo nombreArticulo
	 * @param nombreArticulo valor que se va a almacenar en el atributo nombreArticulo
	 * @author Camilo G�mez
	 */
	public void setNombreArticulo(String nombreArticulo) {
		this.nombreArticulo = nombreArticulo;
	}

	/**
	 * M�todo que retorna el valor del atributo nivelAtencionArticulo
	 * @return nivelAtencionArticulo
	 * @author Camilo G�mez
	 */
	public Long getNivelAtencionArticulo() {
		return nivelAtencionArticulo;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo nivelAtencionArticulo
	 * @param nivelAtencionArticulo valor que se va a almacenar en el atributo nivelAtencionArticulo
	 * @author Camilo G�mez
	 */
	public void setNivelAtencionArticulo(Long nivelAtencionArticulo) {
		this.nivelAtencionArticulo = nivelAtencionArticulo;
	}

	/**
	 * M�todo que retorna el valor del atributo nivelAtencionServicio
	 * @return nivelAtencionServicio
	 * @author Camilo G�mez
	 */
	public Long getNivelAtencionServicio() {
		return nivelAtencionServicio;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo nivelAtencionServicio
	 * @param nivelAtencionServicio valor que se va a almacenar en el atributo nivelAtencionServicio
	 * @author Camilo G�mez
	 */
	public void setNivelAtencionServicio(Long nivelAtencionServicio) {
		this.nivelAtencionServicio = nivelAtencionServicio;
	}

	/**
	 * M�todo que retorna el valor del atributo codigoGrupoServicio
	 * @return codigoGrupoServicio
	 * @author Camilo G�mez
	 */
	public Integer getCodigoGrupoServicio() {
		return codigoGrupoServicio;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo codigoGrupoServicio
	 * @param codigoGrupoServicio valor que se va a almacenar en el atributo codigoGrupoServicio
	 * @author Camilo G�mez
	 */
	public void setCodigoGrupoServicio(Integer codigoGrupoServicio) {
		this.codigoGrupoServicio = codigoGrupoServicio;
	}

	/**
	 * M�todo que retorna el valor del atributo descripcionGrupoServicio
	 * @return descripcionGrupoServicio
	 * @author Camilo G�mez
	 */
	public String getDescripcionGrupoServicio() {
		return descripcionGrupoServicio;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo descripcionGrupoServicio
	 * @param descripcionGrupoServicio valor que se va a almacenar en el atributo descripcionGrupoServicio
	 * @author Camilo G�mez
	 */
	public void setDescripcionGrupoServicio(String descripcionGrupoServicio) {
		this.descripcionGrupoServicio = descripcionGrupoServicio;
	}

	/**
	 * M�todo que retorna el valor del atributo claseInventario
	 * @return claseInventario
	 * @author Camilo G�mez
	 */
	public Integer getClaseInventario() {
		return claseInventario;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo claseInventario
	 * @param claseInventario valor que se va a almacenar en el atributo claseInventario
	 * @author Camilo G�mez
	 */
	public void setClaseInventario(Integer claseInventario) {
		this.claseInventario = claseInventario;
	}


	/**
	 * M�todo que retorna el valor del atributo fecha
	 * @return fecha
	 * @author Camilo G�mez
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo fecha
	 * @param fecha valor que se va a almacenar en el atributo fecha
	 * @author Camilo G�mez
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * M�todo que retorna el valor del atributo convenio
	 * @return convenio
	 * @author Camilo G�mez
	 */
	public Integer getConvenio() {
		return convenio;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo convenio
	 * @param convenio valor que se va a almacenar en el atributo convenio
	 * @author Camilo G�mez
	 */
	public void setConvenio(Integer convenio) {
		this.convenio = convenio;
	}

	/**
	 * M�todo que retorna el valor del atributo contrato
	 * @return contrato
	 * @author Camilo G�mez
	 */
	public Integer getContrato() {
		return contrato;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo contrato
	 * @param contrato valor que se va a almacenar en el atributo contrato
	 * @author Camilo G�mez
	 */
	public void setContrato(Integer contrato) {
		this.contrato = contrato;
	}

	/**
	 * M�todo que retorna el valor del atributo numeroSolicitud
	 * @return numeroSolicitud
	 * @author Camilo G�mez
	 */
	public Integer getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo numeroSolicitud
	 * @param numeroSolicitud valor que se va a almacenar en el atributo numeroSolicitud
	 * @author Camilo G�mez
	 */
	public void setNumeroSolicitud(Integer numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}


	/**
	 * M�todo que retorna el valor del atributo subgrupoInventario
	 * @return subgrupoInventario
	 * @author Camilo G�mez
	 */
	public Integer getSubgrupoInventario() {
		return subgrupoInventario;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo subgrupoInventario
	 * @param subgrupoInventario valor que se va a almacenar en el atributo subgrupoInventario
	 * @author Camilo G�mez
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
