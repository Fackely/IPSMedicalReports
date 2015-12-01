package com.servinte.axioma.dto.ordenes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import util.ConstantesBD;

import com.servinte.axioma.common.ErrorMessage;
import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;

/**
 * Dto Necesario para mapear los atributos de los Medicamentos/Insumos
 * a Autorizar
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public class ServicioAutorizacionOrdenDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5315659662066694957L;

	/**
	 * Atributo que representa el codigoPk del Servicio
	 */
	private int codigo;
	
	/**
	 * Atributo que representa la descripción del Servicio
	 */
	private String descripcion;
	
	/**
	 * Atributo que representa el codigo porpietario del Servicio
	 */
	private String codigoPropietario;
	
	/**
	 * Atributo que representa el número de dias Urgentes parametrizados para el grupo del Servicio
	 */
	private Byte numeroDiasUrgente;
	
	/**
	 * Atributo que representa el número de dias Normales parametrizados para el grupo del Servicio
	 */
	private Byte numeroDiasNormal;
	
	/**
	 * Atributo que representa el codigo del Grupo de Servicios al cual pertenece el Servicio
	 */
	private Integer codigoGrupoServicio;
	
	/**
	 * Atributo que representa el codigo de la especialidad a la cual pertenece el Servicio
	 */
	private Integer codigoEspecialidad;
	
	/**
	 * Atributo que representa el codigo del Tipo de Servicio al cual pertenece el Servicio
	 */
	private String acronimoTipoServicio;
	
	/**
	 * Atributo que representa el codigoPk del Nivel de Atención al cual pertenece el Servicio
	 */
	private Long consecutivoNivelAtencion;
	
	/**
	 * Atributo que representa la descripción del Nivel de Atención al cual pertenece el Servicio
	 */
	private String descripcionNivelAtencion;
	
	/**
	 * Atributo para almacenar el el valor de la tarifa del servicio calculada en la autorización 
	 */
	private BigDecimal valorTarifa;
	
	/**
	 * Atributo para almacenar el mensaje del fallo autorización para el servicio
	 */
	private ErrorMessage mensajeError;

	/**
	 * Atributo para almacenar el indicativo que permite identificar si el servicio se debe autorizar
	 */
	private boolean autorizar;
	
	/**
	 * Atributo para almacenar
	 */
	private boolean autorizado;
	
	/**
	 * Atributo que almacena la cantidad de servicios que se van a autorizar
	 */
	
	
	/**
	 * Atributo que indica si la orden es urgente
	 */
	private char urgente;
	
	/**
	 * Atributo que almacena la finalidad del servicio de la orden
	 */
	private Integer finalidad;
	
	/**
	 * Atributo que almacena los niveles de autorización que tiene el servicio
	 */
	private List<NivelAutorizacionDto> nivelesAutorizacion;
	
	/**
	 * Atributo que almacena el nivel de autorización para el servicio
	 */
	private NivelAutorizacionDto nivelAutorizacion;
	
	/**
	 * Atributo que almacena si se puede autorizar el servicio dado que tiene nivel de autorización
	 */
	private boolean puedeAutorizar;
	
	/**
	 * Atributo que almacena si se puede autorizar el servicioo
	 */
	private boolean validoAutorizar;
	
	/**
	 * Atributo que almacena el tipo de monto asociado al grupo del servicio
	 */
	private Integer tipoMonto;
	
	/**
	 * Atributo que almacena el acronimo del diagnostico asociado a la orden
	 */
	private String acronimoDx;
	
	/**
	 * Atributo que almacena el tipo Cie del diagnostico asociado a la orden
	 */
	private Integer tipoCieDx;
	
	/**
	 * Atributo que almacena el nombre del diagnostico asociado a la orden
	 */
	private String nombreDx;
	
	/**
	 * Atributo que representa la cantidad de medicamento ordenado
	 */
	private Long cantidad;
	
	/**
	 * Atributo que representa si el servicio de cirguia es el principal
	 */
	private boolean cirugiaPrincipal;
	
	/**
	 * Atributo que representa el contrato capitado que cubre el servicio de cirugía
	 */
	private Integer codigoContrato;
	
	/**
	 * Atributo que representa si el servicio esta cubierto
	 */
	private boolean cubierto;
	
	
	public ServicioAutorizacionOrdenDto(){
		
	}
	
	/**
	 * Constructor necesario para mapear la consulta de Servicios
	 * pendientes por autorizar para ordenes
	 * 
	 * @param codigo
	 * @param descripcion
	 * @param codigoPropietario
	 * @param numeroDiasUrgente
	 * @param numeroDiasNormal
	 * @param codigoGrupoServicio
	 * @param codigoEspecialidad
	 * @param acronimoTipoServicio
	 */
	public ServicioAutorizacionOrdenDto(int codigo, String descripcion,
			String codigoPropietario, Byte numeroDiasUrgente,
			Byte numeroDiasNormal, Integer codigoGrupoServicio,
			int codigoEspecialidad, String acronimoTipoServicio,
			Long consecutivoNivelAtencion, String descripcionNivelAtencion,
			String acronimoDx, Integer tipoCieDx, 
			String nombreDx, Integer codigoTipoMonto) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.codigoPropietario = codigoPropietario;
		this.numeroDiasUrgente = numeroDiasUrgente;
		this.numeroDiasNormal = numeroDiasNormal;
		this.codigoGrupoServicio = codigoGrupoServicio;
		this.codigoEspecialidad = codigoEspecialidad;
		this.acronimoTipoServicio = acronimoTipoServicio;
		this.consecutivoNivelAtencion=consecutivoNivelAtencion;
		this.descripcionNivelAtencion=descripcionNivelAtencion;
		this.acronimoDx=acronimoDx;
		this.tipoCieDx=tipoCieDx;
		this.nombreDx=nombreDx;
		this.cantidad=1L;
		this.tipoMonto=codigoTipoMonto;
	}
	
	
	/**
	 * Constructor necesario para mapear la consulta de Servicios
	 * pendientes por autorizar para ordenes
	 * 
	 * @param codigo
	 * @param descripcion
	 * @param codigoPropietario
	 * @param numeroDiasUrgente
	 * @param numeroDiasNormal
	 * @param codigoGrupoServicio
	 * @param codigoEspecialidad
	 * @param acronimoTipoServicio
	 */
	public ServicioAutorizacionOrdenDto(int codigo, String descripcion,
			String codigoPropietario, Byte numeroDiasUrgente,
			Byte numeroDiasNormal, Integer codigoGrupoServicio,
			int codigoEspecialidad, String acronimoTipoServicio,
			Long consecutivoNivelAtencion, String descripcionNivelAtencion,
			String acronimoDx, Integer tipoCieDx, 
			String nombreDx, Integer codigoTipoMonto, Short cantidad) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.codigoPropietario = codigoPropietario;
		this.numeroDiasUrgente = numeroDiasUrgente;
		this.numeroDiasNormal = numeroDiasNormal;
		this.codigoGrupoServicio = codigoGrupoServicio;
		this.codigoEspecialidad = codigoEspecialidad;
		this.acronimoTipoServicio = acronimoTipoServicio;
		this.consecutivoNivelAtencion=consecutivoNivelAtencion;
		this.descripcionNivelAtencion=descripcionNivelAtencion;
		this.acronimoDx=acronimoDx;
		this.tipoCieDx=tipoCieDx;
		this.nombreDx=nombreDx;
		if(cantidad!=null){
			this.cantidad=cantidad.longValue();
		}else{
			this.cantidad=1L;
		}
		
		this.tipoMonto=codigoTipoMonto;
	}
	
	/**
	 * Constructor necesario para mapear la consulta de Servicios
	 * pendientes por autorizar para ordenes medicas o cargos directos
	 * o peticiones de Cirugias
	 * 
	 * @param codigo
	 * @param descripcion
	 * @param codigoPropietario
	 * @param numeroDiasUrgente
	 * @param numeroDiasNormal
	 * @param codigoGrupoServicio
	 * @param codigoEspecialidad
	 * @param acronimoTipoServicio
	 * @param acronimoTipoServicio
	 * @param numeroServicio
	 */
	public ServicioAutorizacionOrdenDto(int codigo, String descripcion,
			String codigoPropietario, Byte numeroDiasUrgente,
			Byte numeroDiasNormal, Integer codigoGrupoServicio,
			int codigoEspecialidad, String acronimoTipoServicio,
			Long consecutivoNivelAtencion, String descripcionNivelAtencion,
			String acronimoDx, Integer tipoCieDx, 
			String nombreDx, Integer codigoTipoMonto, int numeroServicio,
			Integer codigoContrato, String cubierto) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.codigoPropietario = codigoPropietario;
		this.numeroDiasUrgente = numeroDiasUrgente;
		this.numeroDiasNormal = numeroDiasNormal;
		this.codigoGrupoServicio = codigoGrupoServicio;
		this.codigoEspecialidad = codigoEspecialidad;
		this.acronimoTipoServicio = acronimoTipoServicio;
		this.consecutivoNivelAtencion=consecutivoNivelAtencion;
		this.descripcionNivelAtencion=descripcionNivelAtencion;
		this.acronimoDx=acronimoDx;
		this.tipoCieDx=tipoCieDx;
		this.nombreDx=nombreDx;
		this.cantidad=1L;
		this.tipoMonto=codigoTipoMonto;
		if(numeroServicio==1){
			this.cirugiaPrincipal=true;
		}
		else{
			this.cirugiaPrincipal=false;
		}
		this.codigoContrato=codigoContrato;
		if(cubierto != null && cubierto.equals(ConstantesBD.acronimoSi)){
			this.cubierto=true;
		}
		else{
			this.cubierto=false;	
		}
	}

	/**
	 * @param codigo
	 * @param descripcion
	 * @param codigoPropietario
	 * @param numeroDiasUrgente
	 * @param numeroDiasNormal
	 * @param codigoGrupoServicio
	 * @param codigoEspecialidad
	 * @param acronimoTipoServicio
	 * @param consecutivoNivelAtencion
	 * @param descripcionNivelAtencion
	 * @param acronimoDx
	 * @param tipoCieDx
	 * @param nombreDx
	 * @param codigoTipoMonto
	 * @param cubierto
	 */
	public ServicioAutorizacionOrdenDto(int codigo, String descripcion,
			String codigoPropietario, Byte numeroDiasUrgente,
			Byte numeroDiasNormal, Integer codigoGrupoServicio,
			int codigoEspecialidad, String acronimoTipoServicio,
			Long consecutivoNivelAtencion, String descripcionNivelAtencion,
			String acronimoDx, Integer tipoCieDx, 
			String nombreDx, Integer codigoTipoMonto, String cubierto) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.codigoPropietario = codigoPropietario;
		this.numeroDiasUrgente = numeroDiasUrgente;
		this.numeroDiasNormal = numeroDiasNormal;
		this.codigoGrupoServicio = codigoGrupoServicio;
		this.codigoEspecialidad = codigoEspecialidad;
		this.acronimoTipoServicio = acronimoTipoServicio;
		this.consecutivoNivelAtencion=consecutivoNivelAtencion;
		this.descripcionNivelAtencion=descripcionNivelAtencion;
		this.acronimoDx=acronimoDx;
		this.tipoCieDx=tipoCieDx;
		this.nombreDx=nombreDx;
		this.cantidad=1L;
		this.tipoMonto=codigoTipoMonto;
		
		if(cubierto != null && cubierto.equals(ConstantesBD.acronimoSi)){
			this.cubierto=true;
		}
		else{
			this.cubierto=false;	
		}
	}
	
	/**
	 * Constructor necesario para mapear la consulta de Servicios
	 * autorizados para consultar el valor de la tarifa
	 * 
	 * @param codigo
	 * @param codigoContrato
	 * @param consecutivoNivelAtencion
	 * @param codigoGrupoServicio
	 * @param valorTarifa
	 */
	public ServicioAutorizacionOrdenDto(int codigo, Integer codigoContrato,
			Long consecutivoNivelAtencion, Integer codigoGrupoServicio, BigDecimal valorTarifa) {
		this.codigo = codigo;
		this.codigoContrato = codigoContrato;
		this.consecutivoNivelAtencion=consecutivoNivelAtencion;
		this.codigoGrupoServicio = codigoGrupoServicio;
		this.valorTarifa = valorTarifa;		
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
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the codigoPropietario
	 */
	public String getCodigoPropietario() {
		return codigoPropietario;
	}

	/**
	 * @param codigoPropietario the codigoPropietario to set
	 */
	public void setCodigoPropietario(String codigoPropietario) {
		this.codigoPropietario = codigoPropietario;
	}

	/**
	 * @return the numeroDiasUrgente
	 */
	public Byte getNumeroDiasUrgente() {
		return numeroDiasUrgente;
	}

	/**
	 * @param numeroDiasUrgente the numeroDiasUrgente to set
	 */
	public void setNumeroDiasUrgente(Byte numeroDiasUrgente) {
		this.numeroDiasUrgente = numeroDiasUrgente;
	}

	/**
	 * @return the numeroDiasNormal
	 */
	public Byte getNumeroDiasNormal() {
		return numeroDiasNormal;
	}

	/**
	 * @param numeroDiasNormal the numeroDiasNormal to set
	 */
	public void setNumeroDiasNormal(Byte numeroDiasNormal) {
		this.numeroDiasNormal = numeroDiasNormal;
	}

	/**
	 * @return the codigoGrupoServicio
	 */
	public Integer getCodigoGrupoServicio() {
		return codigoGrupoServicio;
	}

	/**
	 * @param codigoGrupoServicio the codigoGrupoServicio to set
	 */
	public void setCodigoGrupoServicio(Integer codigoGrupoServicio) {
		this.codigoGrupoServicio = codigoGrupoServicio;
	}

	/**
	 * @return the codigoEspecialidad
	 */
	public Integer getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	/**
	 * @param codigoEspecialidad the codigoEspecialidad to set
	 */
	public void setCodigoEspecialidad(Integer codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}

	/**
	 * @return the acronimoTipoServicio
	 */
	public String getAcronimoTipoServicio() {
		return acronimoTipoServicio;
	}

	/**
	 * @param acronimoTipoServicio the acronimoTipoServicio to set
	 */
	public void setAcronimoTipoServicio(String acronimoTipoServicio) {
		this.acronimoTipoServicio = acronimoTipoServicio;
	}

	/**
	 * @return the consecutivoNivelAtencion
	 */
	public Long getConsecutivoNivelAtencion() {
		return consecutivoNivelAtencion;
	}

	/**
	 * @param consecutivoNivelAtencion the consecutivoNivelAtencion to set
	 */
	public void setConsecutivoNivelAtencion(Long consecutivoNivelAtencion) {
		this.consecutivoNivelAtencion = consecutivoNivelAtencion;
	}

	/**
	 * @return the descripcionNivelAtencion
	 */
	public String getDescripcionNivelAtencion() {
		return descripcionNivelAtencion;
	}

	/**
	 * @param descripcionNivelAtencion the descripcionNivelAtencion to set
	 */
	public void setDescripcionNivelAtencion(String descripcionNivelAtencion) {
		this.descripcionNivelAtencion = descripcionNivelAtencion;
	}
	
	/**
	 * @return valorTarifa
	 */
	public BigDecimal getValorTarifa() {
		return valorTarifa;
	}
	
	/**
	 * @param valorTarifa
	 */
	public void setValorTarifa(BigDecimal valorTarifa) {
		this.valorTarifa = valorTarifa;
	}
	
	/**
	 * @return mensajeError
	 */
	public ErrorMessage getMensajeError() {
		return mensajeError;
	}
	
	/**
	 * @param mensajeError
	 */
	public void setMensajeError(ErrorMessage mensajeError) {
		this.mensajeError = mensajeError;
	}
	
	/**
	 * @return autorizar
	 */
	public boolean isAutorizar() {
		return autorizar;
	}
	
	/**
	 * @param autorizar
	 */
	public void setAutorizar(boolean autorizar) {
		this.autorizar = autorizar;
	}
	
	/**
	 * @return autorizado
	 */
	public boolean isAutorizado() {
		return autorizado;
	}
	
	/**
	 * @param autorizado
	 */
	public void setAutorizado(boolean autorizado) {
		this.autorizado = autorizado;
	}
	
	/**
	 * @return cantidadSolicitada
	 */
		
	/**
	 * @return urgente
	 */
	public char getUrgente() {
		return urgente;
	}
	
	/**
	 * @param urgente
	 */
	public void setUrgente(char urgente) {
		this.urgente = urgente;
	}
	
	/**
	 * @return finalidad
	 */
	public Integer getFinalidad() {
		return finalidad;
	}
	
	/**
	 * @param finalidad
	 */
	public void setFinalidad(Integer finalidad) {
		this.finalidad = finalidad;
	}
	
	/**
	 * @return the nivelesAutorizacion
	 */
	public List<NivelAutorizacionDto> getNivelesAutorizacion() {
		return nivelesAutorizacion;
	}

	/**
	 * @param nivelesAutorizacion the nivelesAutorizacion to set
	 */
	public void setNivelesAutorizacion(List<NivelAutorizacionDto> nivelesAutorizacion) {
		this.nivelesAutorizacion = nivelesAutorizacion;
	}

	/**
	 * @return nivelAutorizacion
	 */
	public NivelAutorizacionDto getNivelAutorizacion() {
		return nivelAutorizacion;
	}
	
	/**
	 * @param nivelAutorizacion
	 */
	public void setNivelAutorizacion(NivelAutorizacionDto nivelAutorizacion) {
		this.nivelAutorizacion = nivelAutorizacion;
	}
	
	/**
	 * @return the puedeAutorizar
	 */
	public boolean isPuedeAutorizar() {
		return puedeAutorizar;
	}

	/**
	 * @param puedeAutorizar the puedeAutorizar to set
	 */
	public void setPuedeAutorizar(boolean puedeAutorizar) {
		this.puedeAutorizar = puedeAutorizar;
	}

	/**
	 * @return tipoMonto
	 */
	public Integer getTipoMonto() {
		return tipoMonto;
	}
	
	/**
	 * @param tipoMonto
	 */
	public void setTipoMonto(Integer tipoMonto) {
		this.tipoMonto = tipoMonto;
	}


	/**
	 * @return the acronimoDx
	 */
	public String getAcronimoDx() {
		return acronimoDx;
	}

	/**
	 * @param acronimoDx the acronimoDx to set
	 */
	public void setAcronimoDx(String acronimoDx) {
		this.acronimoDx = acronimoDx;
	}

	/**
	 * @return the tipoCieDx
	 */
	public Integer getTipoCieDx() {
		return tipoCieDx;
	}

	/**
	 * @param tipoCieDx the tipoCieDx to set
	 */
	public void setTipoCieDx(Integer tipoCieDx) {
		this.tipoCieDx = tipoCieDx;
	}

	/**
	 * @return the nombreDx
	 */
	public String getNombreDx() {
		return nombreDx;
	}

	/**
	 * @param nombreDx the nombreDx to set
	 */
	public void setNombreDx(String nombreDx) {
		this.nombreDx = nombreDx;
	}

	/**
	 * @return the cantidad
	 */
	public Long getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the cirugiaPrincipal
	 */
	public boolean isCirugiaPrincipal() {
		return cirugiaPrincipal;
	}

	/**
	 * @param cirugiaPrincipal the cirugiaPrincipal to set
	 */
	public void setCirugiaPrincipal(boolean cirugiaPrincipal) {
		this.cirugiaPrincipal = cirugiaPrincipal;
	}

	/**
	 * @return the validoAutorizar
	 */
	public boolean isValidoAutorizar() {
		return validoAutorizar;
	}

	/**
	 * @param validoAutorizar the validoAutorizar to set
	 */
	public void setValidoAutorizar(boolean validoAutorizar) {
		this.validoAutorizar = validoAutorizar;
	}

	/**
	 * @return the codigoContrato
	 */
	public Integer getCodigoContrato() {
		return codigoContrato;
	}

	/**
	 * @param codigoContrato the codigoContrato to set
	 */
	public void setCodigoContrato(Integer codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	/**
	 * @return the cubierto
	 */
	public boolean isCubierto() {
		return cubierto;
	}

	/**
	 * @param cubierto the cubierto to set
	 */
	public void setCubierto(boolean cubierto) {
		this.cubierto = cubierto;
	}

}