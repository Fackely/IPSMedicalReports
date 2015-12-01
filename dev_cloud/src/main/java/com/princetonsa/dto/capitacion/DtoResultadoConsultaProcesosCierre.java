package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Esta clase almacena el resultado de la consulta �rdenes ambulatorias y �rdenes m�dicas en el sistema
 * para ser utilizado en el proceso �rdenes (Anexo 1028) y
 * para ser utilizado en el proceso cargos  (Anexo 1030)
 * 
 * @author Fabi�n Becerra
 */
public class DtoResultadoConsultaProcesosCierre implements Serializable{

	/** * */
	private static final long serialVersionUID = 1L;
	
	/** Tarifa del servicio o articulo * */
	private Double tarifa;
	
	private Date fechaVigenciaTarifaArticulo;
	
	/** C�digo del Servicio * */
	private Integer codigoServicio;
	
	/** Nombre del Servicio * */
	private String nombreServicio;
	
	/** C�digo del Articulo  * */
	private Integer codigoArticulo;
	
	/** C�digo del Articulo  * */
	private Integer esquemaTarifarioArticulo;
	
	/** Nombre del Articulo  * */
	private String nombreArticulo;
	
	/** Nivel de atenci�n del art�culo * */
	private Long nivelAtencionArticulo;
	
	/** C�digo Orden Ambulatoria* */
	private Long codigoOrden;
	
	/** Nivel de atenci�n del servicio * */
	private Long nivelAtencionServicio;
	
	/** C�digo del Grupo de servicio * */
	private Integer codigoGrupoServicio;
	
	/** Nombre del Grupo de servicio * */
	private String descripcionGrupoServicio;
	
	/** C�digo de la Clase de Inventario * */
	private Integer claseInventario;
	
	/** Cantidad ordenada de Articulos en las solicitudes * */
	private Integer cantidadArticulosSolicitudes;
	
	/** Cantidad ordenada de Servicios en las solicitudes * */
	private BigDecimal cantidadServiciosSolicitudes;
	
	/** Cantidad de servicios en las ordenes* */
	private Short cantidadServiciosOrdenes;
	
	/** Cantidad de articulos en las ordenes* */
	private Long cantidadArticulosOrdenes;
	
	/** Fecha en la que se gener� la orden m�dica o ambulatoria * */
	private Date fecha;
	
	/** Fecha en la que se anul� la orden m�dica o ambulatoria * */
	private Date fechaAnulacion;
	
	/** Valor de la Tarifa ISS * */
	private Integer convenio;
	
	/** Valor de la Tarifa ISS * */
	private Integer contrato;
	
	/** Numero de la Solicitud * */
	private Integer numeroSolicitud;
	
	/** Numero de la Peticion MT6578* */
	private Integer codigoPeticion;
	
	/** Numero de la Orden Ambulatoria * */
	private Long numeroOrdenAmbulatoria;
	
	/** Valor de la Tarifa ISS * */
	private Double valorTarifaISS;
	
	/** Fecha de Vigencia de la Tarifa ISS * */
	private Date fechaVigenciaTarifaISS;
	
	/** Valor de la tarifa SOAT * */
	private Double valorTarifaSOAT;
	
	/** Fecha de Vigencia de la tarifa SOAT * */
	private Date fechaVigenciaTarifaSOAT;
	
	/** Subgrupo del articulo * */
	private Integer subgrupoInventario;
	
	/** Cantidad servicios Peticiones **/
	private Number cantidadServArtPet;
	
	/** Valor tarifa peticiones articulos **/
	private Number tarifaArtPet;
	
	/** 
	 *  
	 * Atributo que devuelve la cantidad de servicios o articulos
	 * de peticiones, ordenes o solicitudes
	 */
	private Integer cantidadServArt;
	
	/** 
	 * Atributo que devuelve el tipo de liquidaci�n de la tarifa
	 * MT 6532-6438
	 */
	private Integer codigoTipoLiquidacion;
	
	/**
	 * Constructor de la clase
	 */
	public DtoResultadoConsultaProcesosCierre() {
		this.reset();
	}
	
	/**
	 * Constructor utilizado para traer las peticiones de servicios
	 * @param codigoServicio
	 * @param nombreServicio
	 * @param nivelAtencionServicio
	 * @param contrato
	 * @param convenio
	 * @param fecha
	 * @param cantidadServicioPet
	 * MT6578 se adiciona codigoPeticion
	 * @param estadoPeticion
	 */
	public DtoResultadoConsultaProcesosCierre(Integer codigoServicio, Integer codigoPeticion, String nombreServicio, Long nivelAtencionServicio,
			Integer contrato, Integer convenio, Date fecha, Number cantidadServArtPet, Date fechaAnulacion){
		this.codigoServicio= codigoServicio;
		this.nombreServicio= nombreServicio;
		this.nivelAtencionServicio= nivelAtencionServicio;
		this.contrato= contrato;
		this.convenio= convenio;
		this.fecha = fecha;
		this.cantidadServArtPet= cantidadServArtPet;
		this.fechaAnulacion=fechaAnulacion;
		this.codigoPeticion=codigoPeticion;
	}
	
	/**
	 * Constructor utilizado para traer las peticiones de articulos
	 * @param codigoArticulo
	 * @param nombreArticulo
	 * @param nivelAtencionArticulo
	 * @param contrato
	 * @param convenio
	 * @param fecha
	 * @param cantidadServArtPet
	 * @param tarifaArtPet
	 * @param esquemaTarifarioArticulo
	 * @param fechaVigenciaTarifaArticulo
	 * @param codigoPeticion
	 * MT6578 se adiciona codigoPeticion
	 */
	public DtoResultadoConsultaProcesosCierre(Integer codigoArticulo, Integer codigoPeticion, String nombreArticulo, Long nivelAtencionArticulo,
			Integer contrato, Integer convenio, Date fecha, Number cantidadServArtPet, Double tarifa, Integer esquemaTarifarioArticulo,
			Date fechaVigenciaTarifaArticulo, Date fechaAnulacion){
		this.codigoArticulo= codigoArticulo;
		this.nombreArticulo= nombreArticulo;
		this.nivelAtencionArticulo= nivelAtencionArticulo;
		this.contrato= contrato;
		this.convenio= convenio;
		this.fecha = fecha;
		this.cantidadServArtPet= cantidadServArtPet;
		this.tarifa= tarifa;
		this.esquemaTarifarioArticulo= esquemaTarifarioArticulo;
		this.fechaVigenciaTarifaArticulo=fechaVigenciaTarifaArticulo;
		this.fechaAnulacion=fechaAnulacion;
		this.codigoPeticion=codigoPeticion;
	}
	
	/** 
	 * Constructor utilizado para traer las peticiones de articulos anulados en SolicitudCx
	 * @param codigoArticulo
	 * @param nombreArticulo
	 * @param nivelAtencionArticulo
	 * @param contrato
	 * @param convenio
	 * @param cantidadServArtPet
	 * @param tarifaArtPet
	 * @param esquemaTarifarioArticulo
	 * @param fechaVigenciaTarifaArticulo
	 * @param codigoPeticion
	 * MT6578 se adiciona codigoPeticion
	 * @author hermorhu
	 */
	public DtoResultadoConsultaProcesosCierre(Integer codigoArticulo, Integer codigoPeticion, String nombreArticulo, Long nivelAtencionArticulo,
			Integer contrato, Integer convenio, Number cantidadServArtPet, Double tarifa, Integer esquemaTarifarioArticulo,
			Date fechaVigenciaTarifaArticulo, Date fechaAnulacion){
		this.codigoArticulo= codigoArticulo;
		this.nombreArticulo= nombreArticulo;
		this.nivelAtencionArticulo= nivelAtencionArticulo;
		this.contrato= contrato;
		this.convenio= convenio;
		this.cantidadServArtPet= cantidadServArtPet;
		this.tarifa= tarifa;
		this.esquemaTarifarioArticulo= esquemaTarifarioArticulo;
		this.fechaVigenciaTarifaArticulo=fechaVigenciaTarifaArticulo;
		this.fechaAnulacion=fechaAnulacion;
		this.codigoPeticion=codigoPeticion;
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
		this.codigoOrden					= null;
		this.codigoGrupoServicio			= null;
		this.descripcionGrupoServicio		= "";
		this.claseInventario				= null;
		this.cantidadArticulosSolicitudes	= null;
		this.cantidadServiciosSolicitudes	= null;
		this.cantidadServiciosOrdenes		= null;
		this.cantidadArticulosOrdenes		= null;
		this.fecha							= null;
		this.fechaAnulacion					= null;
		this.convenio						= null;
		this.contrato						= null;
		this.numeroSolicitud				= null;
		this.valorTarifaISS					= null;
		this.fechaVigenciaTarifaISS			= null;
		this.valorTarifaSOAT				= null;
		this.fechaVigenciaTarifaSOAT		= null;
		this.subgrupoInventario				= null;
		this.esquemaTarifarioArticulo		= null;
		this.fechaVigenciaTarifaArticulo	= null;
		this.cantidadServArtPet				= null;
		this.tarifaArtPet					= null;
		this.cantidadServArt				= null;
		this.codigoTipoLiquidacion			= null;
		this.codigoPeticion					= null;
		
	}

	/**
	 * M�todo que retorna el valor del atributo tarifa
	 * @return tarifa
	 * @author Fabi�n Becerra
	 */
	public Double getTarifa() {
		return tarifa;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo tarifa
	 * @param tarifa valor que se va a almacenar en el atributo tarifa
	 * @author Fabi�n Becerra
	 */
	public void setTarifa(Double tarifa) {
		this.tarifa = tarifa;
	}

	/**
	 * M�todo que retorna el valor del atributo codigoServicio
	 * @return codigoServicio
	 * @author Fabi�n Becerra
	 */
	public Integer getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo codigoServicio
	 * @param codigoServicio valor que se va a almacenar en el atributo codigoServicio
	 * @author Fabi�n Becerra
	 */
	public void setCodigoServicio(Integer codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * M�todo que retorna el valor del atributo nombreServicio
	 * @return nombreServicio
	 * @author Fabi�n Becerra
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo nombreServicio
	 * @param nombreServicio valor que se va a almacenar en el atributo nombreServicio
	 * @author Fabi�n Becerra
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	/**
	 * M�todo que retorna el valor del atributo codigoArticulo
	 * @return codigoArticulo
	 * @author Fabi�n Becerra
	 */
	public Integer getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo codigoArticulo
	 * @param codigoArticulo valor que se va a almacenar en el atributo codigoArticulo
	 * @author Fabi�n Becerra
	 */
	public void setCodigoArticulo(Integer codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * M�todo que retorna el valor del atributo nombreArticulo
	 * @return nombreArticulo
	 * @author Fabi�n Becerra
	 */
	public String getNombreArticulo() {
		return nombreArticulo;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo nombreArticulo
	 * @param nombreArticulo valor que se va a almacenar en el atributo nombreArticulo
	 * @author Fabi�n Becerra
	 */
	public void setNombreArticulo(String nombreArticulo) {
		this.nombreArticulo = nombreArticulo;
	}

	/**
	 * M�todo que retorna el valor del atributo nivelAtencionArticulo
	 * @return nivelAtencionArticulo
	 * @author Fabi�n Becerra
	 */
	public Long getNivelAtencionArticulo() {
		return nivelAtencionArticulo;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo nivelAtencionArticulo
	 * @param nivelAtencionArticulo valor que se va a almacenar en el atributo nivelAtencionArticulo
	 * @author Fabi�n Becerra
	 */
	public void setNivelAtencionArticulo(Long nivelAtencionArticulo) {
		this.nivelAtencionArticulo = nivelAtencionArticulo;
	}

	/**
	 * M�todo que retorna el valor del atributo nivelAtencionServicio
	 * @return nivelAtencionServicio
	 * @author Fabi�n Becerra
	 */
	public Long getNivelAtencionServicio() {
		return nivelAtencionServicio;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo nivelAtencionServicio
	 * @param nivelAtencionServicio valor que se va a almacenar en el atributo nivelAtencionServicio
	 * @author Fabi�n Becerra
	 */
	public void setNivelAtencionServicio(Long nivelAtencionServicio) {
		this.nivelAtencionServicio = nivelAtencionServicio;
	}

	/**
	 * M�todo que retorna el valor del atributo codigoGrupoServicio
	 * @return codigoGrupoServicio
	 * @author Fabi�n Becerra
	 */
	public Integer getCodigoGrupoServicio() {
		return codigoGrupoServicio;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo codigoGrupoServicio
	 * @param codigoGrupoServicio valor que se va a almacenar en el atributo codigoGrupoServicio
	 * @author Fabi�n Becerra
	 */
	public void setCodigoGrupoServicio(Integer codigoGrupoServicio) {
		this.codigoGrupoServicio = codigoGrupoServicio;
	}

	/**
	 * M�todo que retorna el valor del atributo descripcionGrupoServicio
	 * @return descripcionGrupoServicio
	 * @author Fabi�n Becerra
	 */
	public String getDescripcionGrupoServicio() {
		return descripcionGrupoServicio;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo descripcionGrupoServicio
	 * @param descripcionGrupoServicio valor que se va a almacenar en el atributo descripcionGrupoServicio
	 * @author Fabi�n Becerra
	 */
	public void setDescripcionGrupoServicio(String descripcionGrupoServicio) {
		this.descripcionGrupoServicio = descripcionGrupoServicio;
	}

	/**
	 * M�todo que retorna el valor del atributo claseInventario
	 * @return claseInventario
	 * @author Fabi�n Becerra
	 */
	public Integer getClaseInventario() {
		return claseInventario;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo claseInventario
	 * @param claseInventario valor que se va a almacenar en el atributo claseInventario
	 * @author Fabi�n Becerra
	 */
	public void setClaseInventario(Integer claseInventario) {
		this.claseInventario = claseInventario;
	}

	/**
	 * M�todo que retorna el valor del atributo cantidadArticulosSolicitudes
	 * @return cantidadArticulosSolicitudes
	 * @author Fabi�n Becerra
	 */
	public Integer getCantidadArticulosSolicitudes() {
		return cantidadArticulosSolicitudes;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo cantidadArticulosSolicitudes
	 * @param cantidadArticulosSolicitudes valor que se va a almacenar en el atributo cantidadArticulosSolicitudes
	 * @author Fabi�n Becerra
	 */
	public void setCantidadArticulosSolicitudes(Integer cantidadArticulosSolicitudes) {
		this.cantidadArticulosSolicitudes = cantidadArticulosSolicitudes;
	}

	/**
	 * M�todo que retorna el valor del atributo cantidadServiciosSolicitudes
	 * @return cantidadServiciosSolicitudes
	 * @author Fabi�n Becerra
	 */
	public BigDecimal getCantidadServiciosSolicitudes() {
		return cantidadServiciosSolicitudes;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo cantidadServiciosSolicitudes
	 * @param cantidadServiciosSolicitudes valor que se va a almacenar en el atributo cantidadServiciosSolicitudes
	 * @author Fabi�n Becerra
	 */
	public void setCantidadServiciosSolicitudes(
			BigDecimal cantidadServiciosSolicitudes) {
		this.cantidadServiciosSolicitudes = cantidadServiciosSolicitudes;
	}

	/**
	 * M�todo que retorna el valor del atributo cantidadServiciosOrdenes
	 * @return cantidadServiciosOrdenes
	 * @author Fabi�n Becerra
	 */
	public Short getCantidadServiciosOrdenes() {
		return cantidadServiciosOrdenes;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo cantidadServiciosOrdenes
	 * @param cantidadServiciosOrdenes valor que se va a almacenar en el atributo cantidadServiciosOrdenes
	 * @author Fabi�n Becerra
	 */
	public void setCantidadServiciosOrdenes(Short cantidadServiciosOrdenes) {
		this.cantidadServiciosOrdenes = cantidadServiciosOrdenes;
	}

	/**
	 * M�todo que retorna el valor del atributo cantidadArticulosOrdenes
	 * @return cantidadArticulosOrdenes
	 * @author Fabi�n Becerra
	 */
	public Long getCantidadArticulosOrdenes() {
		return cantidadArticulosOrdenes;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo cantidadArticulosOrdenes
	 * @param cantidadArticulosOrdenes valor que se va a almacenar en el atributo cantidadArticulosOrdenes
	 * @author Fabi�n Becerra
	 */
	public void setCantidadArticulosOrdenes(Long cantidadArticulosOrdenes) {
		this.cantidadArticulosOrdenes = cantidadArticulosOrdenes;
	}

	/**
	 * M�todo que retorna el valor del atributo fecha
	 * @return fecha
	 * @author Fabi�n Becerra
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo fecha
	 * @param fecha valor que se va a almacenar en el atributo fecha
	 * @author Fabi�n Becerra
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * M�todo que retorna el valor del atributo convenio
	 * @return convenio
	 * @author Fabi�n Becerra
	 */
	public Integer getConvenio() {
		return convenio;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo convenio
	 * @param convenio valor que se va a almacenar en el atributo convenio
	 * @author Fabi�n Becerra
	 */
	public void setConvenio(Integer convenio) {
		this.convenio = convenio;
	}

	/**
	 * M�todo que retorna el valor del atributo contrato
	 * @return contrato
	 * @author Fabi�n Becerra
	 */
	public Integer getContrato() {
		return contrato;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo contrato
	 * @param contrato valor que se va a almacenar en el atributo contrato
	 * @author Fabi�n Becerra
	 */
	public void setContrato(Integer contrato) {
		this.contrato = contrato;
	}

	/**
	 * M�todo que retorna el valor del atributo numeroSolicitud
	 * @return numeroSolicitud
	 * @author Fabi�n Becerra
	 */
	public Integer getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo numeroSolicitud
	 * @param numeroSolicitud valor que se va a almacenar en el atributo numeroSolicitud
	 * @author Fabi�n Becerra
	 */
	public void setNumeroSolicitud(Integer numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * M�todo que retorna el valor del atributo valorTarifaISS
	 * @return valorTarifaISS
	 * @author Fabi�n Becerra
	 */
	public Double getValorTarifaISS() {
		return valorTarifaISS;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo valorTarifaISS
	 * @param valorTarifaISS valor que se va a almacenar en el atributo valorTarifaISS
	 * @author Fabi�n Becerra
	 */
	public void setValorTarifaISS(Double valorTarifaISS) {
		this.valorTarifaISS = valorTarifaISS;
	}

	/**
	 * M�todo que retorna el valor del atributo fechaVigenciaTarifaISS
	 * @return fechaVigenciaTarifaISS
	 * @author Fabi�n Becerra
	 */
	public Date getFechaVigenciaTarifaISS() {
		return fechaVigenciaTarifaISS;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo fechaVigenciaTarifaISS
	 * @param fechaVigenciaTarifaISS valor que se va a almacenar en el atributo fechaVigenciaTarifaISS
	 * @author Fabi�n Becerra
	 */
	public void setFechaVigenciaTarifaISS(Date fechaVigenciaTarifaISS) {
		this.fechaVigenciaTarifaISS = fechaVigenciaTarifaISS;
	}

	/**
	 * M�todo que retorna el valor del atributo valorTarifaSOAT
	 * @return valorTarifaSOAT
	 * @author Fabi�n Becerra
	 */
	public Double getValorTarifaSOAT() {
		return valorTarifaSOAT;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo valorTarifaSOAT
	 * @param valorTarifaSOAT valor que se va a almacenar en el atributo valorTarifaSOAT
	 * @author Fabi�n Becerra
	 */
	public void setValorTarifaSOAT(Double valorTarifaSOAT) {
		this.valorTarifaSOAT = valorTarifaSOAT;
	}

	/**
	 * M�todo que retorna el valor del atributo fechaVigenciaTarifaSOAT
	 * @return fechaVigenciaTarifaSOAT
	 * @author Fabi�n Becerra
	 */
	public Date getFechaVigenciaTarifaSOAT() {
		return fechaVigenciaTarifaSOAT;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo fechaVigenciaTarifaSOAT
	 * @param fechaVigenciaTarifaSOAT valor que se va a almacenar en el atributo fechaVigenciaTarifaSOAT
	 * @author Fabi�n Becerra
	 */
	public void setFechaVigenciaTarifaSOAT(Date fechaVigenciaTarifaSOAT) {
		this.fechaVigenciaTarifaSOAT = fechaVigenciaTarifaSOAT;
	}

	/**
	 * M�todo que retorna el valor del atributo subgrupoInventario
	 * @return subgrupoInventario
	 * @author Fabi�n Becerra
	 */
	public Integer getSubgrupoInventario() {
		return subgrupoInventario;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo subgrupoInventario
	 * @param subgrupoInventario valor que se va a almacenar en el atributo subgrupoInventario
	 * @author Fabi�n Becerra
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

	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}

	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}

	public void setCodigoOrden(Long codigoOrden) {
		this.codigoOrden = codigoOrden;
	}

	public Long getCodigoOrden() {
		return codigoOrden;
	}

	public void setEsquemaTarifarioArticulo(Integer esquemaTarifarioArticulo) {
		this.esquemaTarifarioArticulo = esquemaTarifarioArticulo;
	}

	public Integer getEsquemaTarifarioArticulo() {
		return esquemaTarifarioArticulo;
	}

	public void setFechaVigenciaTarifaArticulo(
			Date fechaVigenciaTarifaArticulo) {
		this.fechaVigenciaTarifaArticulo = fechaVigenciaTarifaArticulo;
	}

	public Date getFechaVigenciaTarifaArticulo() {
		return fechaVigenciaTarifaArticulo;
	}

	public void setCantidadServArtPet(Number cantidadServArtPet) {
		this.cantidadServArtPet = cantidadServArtPet;
	}

	public Number getCantidadServArtPet() {
		return cantidadServArtPet;
	}

	public void setTarifaArtPet(Number tarifaArtPet) {
		this.tarifaArtPet = tarifaArtPet;
	}

	public Number getTarifaArtPet() {
		return tarifaArtPet;
	}

	public void setCantidadServArt(Integer cantidadServArt) {
		this.cantidadServArt = cantidadServArt;
	}

	public Integer getCantidadServArt() {
		if(this.cantidadArticulosOrdenes!=null)
			cantidadServArt=cantidadArticulosOrdenes.intValue();
		else
			if(this.cantidadServiciosOrdenes!=null)
				cantidadServArt=cantidadServiciosOrdenes.intValue();
		else
			if(this.cantidadArticulosSolicitudes!=null)
				cantidadServArt=cantidadArticulosSolicitudes;
		else
			if(this.cantidadServiciosSolicitudes!=null)
				cantidadServArt=cantidadServiciosSolicitudes.intValue();
		else
			if(this.cantidadServArtPet!=null)
				cantidadServArt=cantidadServArtPet.intValue();
		return cantidadServArt;
	}
	/**
	 * M�todo que retorna el valor del atributo codigoTipoLiquidacion
	 * @param codigoTipoLiquidacion
	 * @author Sandra Barreto
	 * MT 6532-6438
	 */


	public Integer getCodigoTipoLiquidacion() {
		return codigoTipoLiquidacion;
	}


	/**
	 * M�todo que se encarga de establecer el valor del atributo codigoTipoLiquidacion
	 * @param codigoTipoLiquidacion
	 * @author Sandra Barreto
	 * MT 6532-6438
	 */

	public void setCodigoTipoLiquidacion(Integer codigoTipoLiquidacion) {
		this.codigoTipoLiquidacion = codigoTipoLiquidacion;
	}


//MT6578 codigoPeticion

	public Integer getCodigoPeticion() {
		return codigoPeticion;
	}




	public void setCodigoPeticion(Integer numeroPeticion) {
		this.codigoPeticion = numeroPeticion;
	}

}
