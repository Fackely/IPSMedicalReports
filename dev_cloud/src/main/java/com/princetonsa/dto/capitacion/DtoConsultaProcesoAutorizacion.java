package com.princetonsa.dto.capitacion;

import java.util.Date;

import java.math.BigDecimal;

public class DtoConsultaProcesoAutorizacion {

	
	/** * */
	private static final long serialVersionUID = 1L;
			
	/**
	 * Atributo que almacena la tarifa del articulo de autorizaciones
	 */
	private BigDecimal tarifaArticulo;

	/**
	 * Atributo que almacena la tarifa del servicio de autorizaciones
	 */
	private BigDecimal tarifaServicio;
		
	/**
	 * Atributo que almacena el código del Servicio 
	 */
	private Integer codigoServicio;
	
	/**
	 * Atributo que almacena el código del Articulo 
	 */
	private Integer codigoArticulo;	
	
	/**
	 * Atributo que almacena el nombre del Articulo 
	 */
	private String nombreArticulo;
	
	/**
	 * Atributo que almacena el nombre del Servicio 
	 */
	private String nombreServicio;
	
	/**
	 * Atributo que almacena el nivel de atención del servicio o articulo
	 */
	private String nivelAtencionArticulo;
	
	/**
	 * Atributo que almacena el consecutivo del nivel de atención del servicio o articulo
	 */
	private Long codNivelAtencionArticulo;
	
	/**
	 * Atributo que almacena el nivel de atención del servicio o articulo
	 */
	private String nivelAtencionServicio;
	
	/**
	 * Atributo que almacena el consecutivo del nivel de atención del servicio o articulo
	 */
	private Long codNivelAtencionServicio;
		
	/**
	 * Atributo que almacena el grupo del servicio
	 */
	private String grupoServicio;
	
	/**
	 * Atributo que almacena el codigo del grupo del servicio
	 */
	private Integer codGrupoServicio;
	
	/**
	 * Atributo que almacena el codigo del subgrupo de inventario de un articulo
	 */
	private Integer subGrupoInventario;
	
	/**
	 * Atributo que almacena el nombre de clase de inventario de un articulo
	 */
	private String claseInventario;
	
	/**
	 * Atributo que almacena el codigo de clase de inventario de un articulo
	 */
	private Integer codClaseInventario;
		
	/**
	 * Atributo que almacena la cantidad del articulo
	 */
	private Integer cantidadArticulo;
	
	/**
	 * Atributo que almacena la cantidad del servicio 
	 */
	private Integer cantidadServicio;
		
	/**
	 * Atributo que almacena la fecha en la cual fue generada la autorización,
	 * la orden médica o ambulatoria, cargos a la cuenta o la factura 
	 */
	private Date fecha;
	
	/**
	 * Atributo que almacena la fecha en la cual se anuló la autorización,
	 * la orden médica o ambulatoria, cargos a la cuenta o la factura 
	 */
	private Date fechaAnulacion;
	
	/**
	 * Atributo que almacena el convenio
	 */
	private Integer convenio;
	
	/**
	 * Atributo que almacena el contrato
	 */
	private Integer contrato;
	
	/**
	 * Atributo que almacena el estado de la autorizacion
	 */
	private String estadoAutorizacion;	
	
	/**
	 * Atributo que almacena la suma de las autorizaciones
	 * MT6715
	 *  */
	private Integer sumaCantidad;	
	private Integer sumaValor;	
	
	//MT6715 se crea constructor para traer el cantidad de las ordenes
	public DtoConsultaProcesoAutorizacion(Long sumaCantidad, BigDecimal sumaValor ) {
		this.sumaCantidad=	sumaCantidad.intValue();
		this.sumaValor=sumaValor.intValue();
	}
	
	/**
	 * Constructor de la clase
	 */
	public DtoConsultaProcesoAutorizacion() {
		this.reset();	
	}
	
	/**
	 * Constructor para la consulta de Autorizaciones de Capitacion Sub
	 * DCU 1027
	 * 
	 * @param convenioArti
	 * @param convenioServi
	 * @param contratoArti
	 * @param contratoServi
	 * @param fechaAutorizacion
	 * @param codidoArt
	 * @param nombreArt
	 * @param tarifaArt
	 * @param nivelArticulo
	 * @param codidoSer
	 * @param nombreSer
	 * @param tarifaSer
	 * 
	 * @author Camilo Gómez
	 */
	public DtoConsultaProcesoAutorizacion(Integer convenioArti, Integer convenioServi, Integer contratoArti, Integer contratoServi,
			Date fechaAutorizacion, Integer codidoArt, String nombreArt,BigDecimal tarifaArt, Long nivelArticulo, 
			Integer codidoSer, String nombreSer, BigDecimal tarifaSer) {
		
		if(convenioArti != null)
			this.convenio	=	convenioArti;
		else
			this.convenio	=	convenioServi;
		if(contratoArti != null)
			this.contrato	=	contratoArti;
		else
			this.contrato	=	contratoServi;
		this.fecha				= 	fechaAutorizacion;
		this.codigoArticulo		= 	codidoArt;
		this.nombreArticulo		=	nombreArt;
		this.tarifaArticulo		=	tarifaArt;
		this.codNivelAtencionArticulo	=	nivelArticulo;
		this.codigoServicio		= 	codidoSer;
		this.nombreServicio		=	nombreSer;
		this.tarifaServicio		=	tarifaSer;
	}
	
	/**
	 * Constructor para la consulta de Autorizaciones de Capitacion Sub de Articulos y Servicios
	 * en estado AUTORIZADA
	 * DCU 1027
	 * 
	 * @param convenio
	 * @param contrato
	 * @param fechaAutorizacion
	 * @param fechaAnulacion
	 * @param codidoServArt
	 * @param nombreServArt
	 * @param nivelServArt
	 * @param cantidadServArt
	 * @param tarifaServArt
	 * @param isArticulo
	 * 
	 * @author Camilo Gómez
	 */
	public DtoConsultaProcesoAutorizacion(Integer convenio, Integer contrato,Date fechaAutorizacion, Date fechaAnulacion,			
			Integer codido, String nombre, Long nivel, Number cantidad, BigDecimal tarifa, String isArticulo) {
		
		this.convenio	=	convenio;
		this.contrato	=	contrato;
		this.fecha				= 	fechaAutorizacion;
		this.fechaAnulacion		= 	fechaAnulacion;
		if(isArticulo.equals("true"))
		{
			this.codigoArticulo		= 	codido;
			this.nombreArticulo		=	nombre;
			this.codNivelAtencionArticulo	=	nivel;
			this.cantidadArticulo	=	cantidad.intValue();
			this.tarifaArticulo		=	tarifa;
		}else{
			this.codigoServicio		= 	codido;
			this.nombreServicio		=	nombre;
			this.codNivelAtencionServicio	=	nivel;
			this.cantidadServicio	=	cantidad.intValue();
			this.tarifaServicio		=	tarifa;
		}
	}

	/**
	 * Constructor para la consulta de Autorizaciones de Capitacion Sub de Articulos y Servicios
	 * en Estado ANULADA
	 * DCU 1027
	 * 
	 * @param fechaAnulacion
	 * @param cantidad
	 * @param tarifa
	 * @param isArticulo
	 * 
	 * @author Camilo Gómez
	 */
	public DtoConsultaProcesoAutorizacion(Date fechaAnulacion,Number cantidad, BigDecimal tarifa, String isArticulo) {
		
		this.fechaAnulacion		= 	fechaAnulacion;
		if(isArticulo.equals("true"))
		{
			this.cantidadArticulo	=	cantidad.intValue();
			this.tarifaArticulo		=	tarifa;
		}else{
			this.cantidadServicio	=	cantidad.intValue();
			this.tarifaServicio		=	tarifa;
		}
	}
	
	/**
	 * Reset
	 */
	public void reset()
	{		
		this.fecha						= null;
		this.convenio					= null;
		this.nivelAtencionArticulo		= null;
		this.nivelAtencionServicio		= null;
		this.fechaAnulacion				= null;
		this.estadoAutorizacion			= null;
	}
	
		
	public String getGrupoServicio() {
		return grupoServicio;
	}
	public void setGrupoServicio(String grupoServicio) {
		this.grupoServicio = grupoServicio;
	}
	public String getClaseInventario() {
		return claseInventario;
	}
	public void setClaseInventario(String claseInventario) {
		this.claseInventario = claseInventario;
	}	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setConvenio(Integer convenio) {
		this.convenio = convenio;
	}
	public Integer getConvenio() {
		return convenio;
	}
	public void setContrato(Integer contrato) {
		this.contrato = contrato;
	}
	public Integer getContrato() {
		return contrato;
	}	
	public Integer getCodigoServicio() {
		return codigoServicio;
	}
	public void setCodigoServicio(Integer codigoServicio) {
		this.codigoServicio = codigoServicio;
	}
	public Integer getCodigoArticulo() {
		return codigoArticulo;
	}
	public void setCodigoArticulo(Integer codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}	
	public void setNombreArticulo(String nombreArticulo) {
		this.nombreArticulo = nombreArticulo;
	}
	public String getNombreArticulo() {
		return nombreArticulo;
	}
	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	public void setNivelAtencionArticulo(String nivelAtencionArticulo) {
		this.nivelAtencionArticulo = nivelAtencionArticulo;
	}

	public String getNivelAtencionArticulo() {
		return nivelAtencionArticulo;
	}

	public void setNivelAtencionServicio(String nivelAtencionServicio) {
		this.nivelAtencionServicio = nivelAtencionServicio;
	}

	public String getNivelAtencionServicio() {
		return nivelAtencionServicio;
	}

	public void setCantidadArticulo(Integer cantidadArticulo) {
		this.cantidadArticulo = cantidadArticulo;
	}

	public Integer getCantidadArticulo() {
		return cantidadArticulo;
	}

	public void setCantidadServicio(Integer cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}

	public Integer getCantidadServicio() {
		return cantidadServicio;
	}

	public void setTarifaArticulo(BigDecimal tarifaArticulo) {
		this.tarifaArticulo = tarifaArticulo;
	}

	public BigDecimal getTarifaArticulo() {
		return tarifaArticulo;
	}

	public void setTarifaServicio(BigDecimal tarifaServicio) {
		this.tarifaServicio = tarifaServicio;
	}

	public BigDecimal getTarifaServicio() {
		return tarifaServicio;
	}

	public void setCodGrupoServicio(Integer codGrupoServicio) {
		this.codGrupoServicio = codGrupoServicio;
	}

	public Integer getCodGrupoServicio() {
		return codGrupoServicio;
	}

	public void setCodNivelAtencionServicio(Long codNivelAtencionServicio) {
		this.codNivelAtencionServicio = codNivelAtencionServicio;
	}

	public Long getCodNivelAtencionServicio() {
		return codNivelAtencionServicio;
	}

	public void setCodNivelAtencionArticulo(Long codNivelAtencionArticulo) {
		this.codNivelAtencionArticulo = codNivelAtencionArticulo;
	}

	public Long getCodNivelAtencionArticulo() {
		return codNivelAtencionArticulo;
	}

	public void setCodClaseInventario(Integer codClaseInventario) {
		this.codClaseInventario = codClaseInventario;
	}

	public Integer getCodClaseInventario() {
		return codClaseInventario;
	}

	public void setSubGrupoInventario(Integer subGrupoInventario) {
		this.subGrupoInventario = subGrupoInventario;
	}

	public Integer getSubGrupoInventario() {
		return subGrupoInventario;
	}

	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}

	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}

	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}

	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}
	
	//MT6715
	public Integer getSumaCantidad() {
		return sumaCantidad;
	}

	public void setSumaCantidad(Integer sumaCantidad) {
		this.sumaCantidad = sumaCantidad;
	}

	public Integer getSumaValor() {
		return sumaValor;
	}

	public void setSumaValor(Integer sumaValor) {
		this.sumaValor = sumaValor;
	}
	
}
