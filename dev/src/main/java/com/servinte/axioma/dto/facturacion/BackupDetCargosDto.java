package com.servinte.axioma.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hermorhu
 * @created 26-Nov-2012 
 */
public class BackupDetCargosDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1758629339342602841L;
	private long logDistribucionCuenta;
	private Integer subCuenta;
	private int convenio;
	private int contrato;
	private Integer esquemaTarifario;
	private Integer cantidadCargada;
	private BigDecimal valorUnitarioTarifa;
	private BigDecimal valorUnitarioCargado;
	private BigDecimal valorTotalCargado;
	private BigDecimal porcentajeCargado;
	private BigDecimal porcentajeRecargo;
	private BigDecimal valorUnitarioRecargo;
	private BigDecimal porcentajeDcto;
	private BigDecimal valorUnitarioDcto;
	private BigDecimal valorUnitarioIva;
	private String requiereAutorizacion;
	private String nroAutorizacion;
	private int estado;
	private String cubierto;
	private String tipoDistribucion;
	private int solicitud;
	private Integer servicio;
	private Integer articulo;
	private Integer servicioCx;
	private Integer tipoAsocio;
	private String facturado;
	private Integer tipoSolicitud;
	private String paquetizado;
	private Long cargoPadre;
	private String usuarioModifica;
	private Date fechaModifica;
	private String horaModifica;
	private long codSolSubcuenta;
	private String observaciones;
	private Integer codigoFactura;
	private Character eliminado;
	private Integer detCxHonorarios;
	private Integer detAsocioCxSalasMat;
	private char esPortatil;
	private char dejarExcento;
	private BigDecimal porcentajeDctoPromServ;
	private BigDecimal valorDescuentoPromServ;
	private BigDecimal porcHonorarioPromServ;
	private BigDecimal valorHonorarioPromServ;
	private Long programa;
	private BigDecimal porcentajeDctoBonoServ;
	private BigDecimal valorDescuentoBonoServ;
	private BigDecimal porcentajeDctoOdontologico;
	private BigDecimal valorDescuentoOdontologico;
	private Integer detPaqOdonConvenio;
	private long idDetCargo;
	
	/**
	 * 
	 */
	public BackupDetCargosDto() {
		super();
	}


	/**
	 * @param idDetCargo
	 * @param codigoFactura
	 * @param detPaqOdonConvenio
	 * @param estado
	 * @param cargoPadre
	 * @param articulo
	 * @param programa
	 * @param convenio
	 * @param codSolSubcuenta
	 * @param servicioCx
	 * @param contrato
	 * @param subCuenta
	 * @param solicitud
	 * @param usuarioModifica
	 * @param servicio
	 * @param esquemaTarifario
	 * @param cantidadCargada
	 * @param valorUnitarioTarifa
	 * @param valorUnitarioCargado
	 * @param valorTotalCargado
	 * @param porcentajeCargado
	 * @param porcentajeRecargo
	 * @param valorUnitarioRecargo
	 * @param porcentajeDcto
	 * @param valorUnitarioDcto
	 * @param valorUnitarioIva
	 * @param requiereAutorizacion
	 * @param nroAutorizacion
	 * @param cubierto
	 * @param tipoDistribucion
	 * @param tipoAsocio
	 * @param facturado
	 * @param tipoSolicitud
	 * @param paquetizado
	 * @param fechaModifica
	 * @param horaModifica
	 * @param observaciones
	 * @param eliminado
	 * @param detCxHonorarios
	 * @param detAsocioCxSalasMat
	 * @param esPortatil
	 * @param dejarExcento
	 * @param porcentajeDctoPromServ
	 * @param valorDescuentoPromServ
	 * @param porcHonorarioPromServ
	 * @param valorHonorarioPromServ
	 * @param porcentajeDctoBonoServ
	 * @param valorDescuentoBonoServ
	 * @param porcentajeDctoOdontologico
	 * @param valorDescuentoOdontologico
	 */
	public BackupDetCargosDto(long idDetCargo, Integer codigoFactura, Integer detPaqOdonConvenio,
			int estado, Long cargoPadre, Integer articulo, Long programa, int convenio, long codSolSubcuenta,
			Integer servicioCx, int contrato, Integer subCuenta, int solicitud, String usuarioModifica,
			Integer servicio, Integer esquemaTarifario, Integer cantidadCargada, BigDecimal valorUnitarioTarifa, 
			BigDecimal valorUnitarioCargado, BigDecimal valorTotalCargado, BigDecimal porcentajeCargado,
			BigDecimal porcentajeRecargo, BigDecimal valorUnitarioRecargo, BigDecimal porcentajeDcto,
			BigDecimal valorUnitarioDcto, BigDecimal valorUnitarioIva, String requiereAutorizacion,
			String nroAutorizacion, String cubierto, String tipoDistribucion, Integer tipoAsocio,
			String facturado, Integer tipoSolicitud, String paquetizado, Date fechaModifica, String horaModifica, 
			String observaciones, Character eliminado, Integer detCxHonorarios, Integer detAsocioCxSalasMat,
			char esPortatil, char dejarExcento,	BigDecimal porcentajeDctoPromServ, BigDecimal valorDescuentoPromServ,
			BigDecimal porcHonorarioPromServ, BigDecimal valorHonorarioPromServ, BigDecimal porcentajeDctoBonoServ,
			BigDecimal valorDescuentoBonoServ, BigDecimal porcentajeDctoOdontologico, BigDecimal valorDescuentoOdontologico) {
		
		this.idDetCargo = idDetCargo;
		this.codigoFactura = codigoFactura;
		this.detPaqOdonConvenio = detPaqOdonConvenio;
		this.estado = estado;
		this.cargoPadre = cargoPadre;
		this.articulo = articulo;
		this.programa = programa;
		this.convenio = convenio;
		this.codSolSubcuenta = codSolSubcuenta;
		this.servicioCx = servicioCx;
		this.contrato = contrato;
		this.subCuenta = subCuenta;
		this.solicitud = solicitud;
		this.usuarioModifica = usuarioModifica;
		this.servicio = servicio;
		this.esquemaTarifario = esquemaTarifario;
		this.cantidadCargada = cantidadCargada;
		this.valorUnitarioTarifa = valorUnitarioTarifa;
		this.valorUnitarioCargado = valorUnitarioCargado;
		this.valorTotalCargado = valorTotalCargado;
		this.porcentajeCargado = porcentajeCargado;
		this.porcentajeRecargo = porcentajeRecargo;
		this.valorUnitarioRecargo = valorUnitarioRecargo;
		this.porcentajeDcto = porcentajeDcto;
		this.valorUnitarioDcto = valorUnitarioDcto;
		this.valorUnitarioIva = valorUnitarioIva;
		this.requiereAutorizacion = requiereAutorizacion;
		this.nroAutorizacion = nroAutorizacion;
		this.cubierto = cubierto;
		this.tipoDistribucion = tipoDistribucion;
		this.tipoAsocio = tipoAsocio;
		this.facturado = facturado;
		this.tipoSolicitud = tipoSolicitud;
		this.paquetizado = paquetizado;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.observaciones = observaciones;
		this.eliminado = eliminado;
		this.detCxHonorarios = detCxHonorarios;
		this.detAsocioCxSalasMat = detAsocioCxSalasMat;
		this.esPortatil = esPortatil;
		this.dejarExcento = dejarExcento;
		this.porcentajeDctoPromServ = porcentajeDctoPromServ;
		this.valorDescuentoPromServ = valorDescuentoPromServ;
		this.porcHonorarioPromServ = porcHonorarioPromServ;
		this.valorHonorarioPromServ = valorHonorarioPromServ;
		this.porcentajeDctoBonoServ = porcentajeDctoBonoServ;
		this.valorDescuentoBonoServ = valorDescuentoBonoServ;
		this.porcentajeDctoOdontologico = porcentajeDctoOdontologico;
		this.valorDescuentoOdontologico = valorDescuentoOdontologico;		
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
	 * @return the subCuenta
	 */
	public Integer getSubCuenta() {
		return subCuenta;
	}

	/**
	 * @param subCuenta the subCuenta to set
	 */
	public void setSubCuenta(Integer subCuenta) {
		this.subCuenta = subCuenta;
	}

	/**
	 * @return the convenio
	 */
	public int getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the contrato
	 */
	public int getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(int contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return the esquemaTarifario
	 */
	public Integer getEsquemaTarifario() {
		return esquemaTarifario;
	}

	/**
	 * @param esquemaTarifario the esquemaTarifario to set
	 */
	public void setEsquemaTarifario(Integer esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}

	/**
	 * @return the cantidadCargada
	 */
	public Integer getCantidadCargada() {
		return cantidadCargada;
	}

	/**
	 * @param cantidadCargada the cantidadCargada to set
	 */
	public void setCantidadCargada(Integer cantidadCargada) {
		this.cantidadCargada = cantidadCargada;
	}

	/**
	 * @return the valorUnitarioTarifa
	 */
	public BigDecimal getValorUnitarioTarifa() {
		return valorUnitarioTarifa;
	}

	/**
	 * @param valorUnitarioTarifa the valorUnitarioTarifa to set
	 */
	public void setValorUnitarioTarifa(BigDecimal valorUnitarioTarifa) {
		this.valorUnitarioTarifa = valorUnitarioTarifa;
	}

	/**
	 * @return the valorUnitarioCargado
	 */
	public BigDecimal getValorUnitarioCargado() {
		return valorUnitarioCargado;
	}

	/**
	 * @param valorUnitarioCargado the valorUnitarioCargado to set
	 */
	public void setValorUnitarioCargado(BigDecimal valorUnitarioCargado) {
		this.valorUnitarioCargado = valorUnitarioCargado;
	}

	/**
	 * @return the valorTotalCargado
	 */
	public BigDecimal getValorTotalCargado() {
		return valorTotalCargado;
	}

	/**
	 * @param valorTotalCargado the valorTotalCargado to set
	 */
	public void setValorTotalCargado(BigDecimal valorTotalCargado) {
		this.valorTotalCargado = valorTotalCargado;
	}

	/**
	 * @return the porcentajeCargado
	 */
	public BigDecimal getPorcentajeCargado() {
		return porcentajeCargado;
	}

	/**
	 * @param porcentajeCargado the porcentajeCargado to set
	 */
	public void setPorcentajeCargado(BigDecimal porcentajeCargado) {
		this.porcentajeCargado = porcentajeCargado;
	}

	/**
	 * @return the porcentajeRecargo
	 */
	public BigDecimal getPorcentajeRecargo() {
		return porcentajeRecargo;
	}

	/**
	 * @param porcentajeRecargo the porcentajeRecargo to set
	 */
	public void setPorcentajeRecargo(BigDecimal porcentajeRecargo) {
		this.porcentajeRecargo = porcentajeRecargo;
	}

	/**
	 * @return the valorUnitarioRecargo
	 */
	public BigDecimal getValorUnitarioRecargo() {
		return valorUnitarioRecargo;
	}

	/**
	 * @param valorUnitarioRecargo the valorUnitarioRecargo to set
	 */
	public void setValorUnitarioRecargo(BigDecimal valorUnitarioRecargo) {
		this.valorUnitarioRecargo = valorUnitarioRecargo;
	}

	/**
	 * @return the porcentajeDcto
	 */
	public BigDecimal getPorcentajeDcto() {
		return porcentajeDcto;
	}

	/**
	 * @param porcentajeDcto the porcentajeDcto to set
	 */
	public void setPorcentajeDcto(BigDecimal porcentajeDcto) {
		this.porcentajeDcto = porcentajeDcto;
	}

	/**
	 * @return the valorUnitarioDcto
	 */
	public BigDecimal getValorUnitarioDcto() {
		return valorUnitarioDcto;
	}

	/**
	 * @param valorUnitarioDcto the valorUnitarioDcto to set
	 */
	public void setValorUnitarioDcto(BigDecimal valorUnitarioDcto) {
		this.valorUnitarioDcto = valorUnitarioDcto;
	}

	/**
	 * @return the valorUnitarioIva
	 */
	public BigDecimal getValorUnitarioIva() {
		return valorUnitarioIva;
	}

	/**
	 * @param valorUnitarioIva the valorUnitarioIva to set
	 */
	public void setValorUnitarioIva(BigDecimal valorUnitarioIva) {
		this.valorUnitarioIva = valorUnitarioIva;
	}

	/**
	 * @return the requiereAutorizacion
	 */
	public String getRequiereAutorizacion() {
		return requiereAutorizacion;
	}

	/**
	 * @param requiereAutorizacion the requiereAutorizacion to set
	 */
	public void setRequiereAutorizacion(String requiereAutorizacion) {
		this.requiereAutorizacion = requiereAutorizacion;
	}

	/**
	 * @return the nroAutorizacion
	 */
	public String getNroAutorizacion() {
		return nroAutorizacion;
	}

	/**
	 * @param nroAutorizacion the nroAutorizacion to set
	 */
	public void setNroAutorizacion(String nroAutorizacion) {
		this.nroAutorizacion = nroAutorizacion;
	}

	/**
	 * @return the estado
	 */
	public int getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(int estado) {
		this.estado = estado;
	}

	/**
	 * @return the cubierto
	 */
	public String getCubierto() {
		return cubierto;
	}

	/**
	 * @param cubierto the cubierto to set
	 */
	public void setCubierto(String cubierto) {
		this.cubierto = cubierto;
	}

	/**
	 * @return the tipoDistribucion
	 */
	public String getTipoDistribucion() {
		return tipoDistribucion;
	}

	/**
	 * @param tipoDistribucion the tipoDistribucion to set
	 */
	public void setTipoDistribucion(String tipoDistribucion) {
		this.tipoDistribucion = tipoDistribucion;
	}

	/**
	 * @return the solicitud
	 */
	public int getSolicitud() {
		return solicitud;
	}

	/**
	 * @param solicitud the solicitud to set
	 */
	public void setSolicitud(int solicitud) {
		this.solicitud = solicitud;
	}

	/**
	 * @return the servicio
	 */
	public Integer getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(Integer servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the articulo
	 */
	public Integer getArticulo() {
		return articulo;
	}

	/**
	 * @param articulo the articulo to set
	 */
	public void setArticulo(Integer articulo) {
		this.articulo = articulo;
	}

	/**
	 * @return the servicioCx
	 */
	public Integer getServicioCx() {
		return servicioCx;
	}

	/**
	 * @param servicioCx the servicioCx to set
	 */
	public void setServicioCx(Integer servicioCx) {
		this.servicioCx = servicioCx;
	}

	/**
	 * @return the tipoAsocio
	 */
	public Integer getTipoAsocio() {
		return tipoAsocio;
	}

	/**
	 * @param tipoAsocio the tipoAsocio to set
	 */
	public void setTipoAsocio(Integer tipoAsocio) {
		this.tipoAsocio = tipoAsocio;
	}

	/**
	 * @return the facturado
	 */
	public String getFacturado() {
		return facturado;
	}

	/**
	 * @param facturado the facturado to set
	 */
	public void setFacturado(String facturado) {
		this.facturado = facturado;
	}

	/**
	 * @return the tipoSolicitud
	 */
	public Integer getTipoSolicitud() {
		return tipoSolicitud;
	}

	/**
	 * @param tipoSolicitud the tipoSolicitud to set
	 */
	public void setTipoSolicitud(Integer tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	/**
	 * @return the paquetizado
	 */
	public String getPaquetizado() {
		return paquetizado;
	}

	/**
	 * @param paquetizado the paquetizado to set
	 */
	public void setPaquetizado(String paquetizado) {
		this.paquetizado = paquetizado;
	}

	/**
	 * @return the cargoPadre
	 */
	public Long getCargoPadre() {
		return cargoPadre;
	}

	/**
	 * @param cargoPadre the cargoPadre to set
	 */
	public void setCargoPadre(Long cargoPadre) {
		this.cargoPadre = cargoPadre;
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
	 * @return the codSolSubcuenta
	 */
	public long getCodSolSubcuenta() {
		return codSolSubcuenta;
	}

	/**
	 * @param codSolSubcuenta the codSolSubcuenta to set
	 */
	public void setCodSolSubcuenta(long codSolSubcuenta) {
		this.codSolSubcuenta = codSolSubcuenta;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the codigoFactura
	 */
	public Integer getCodigoFactura() {
		return codigoFactura;
	}

	/**
	 * @param codigoFactura the codigoFactura to set
	 */
	public void setCodigoFactura(Integer codigoFactura) {
		this.codigoFactura = codigoFactura;
	}

	/**
	 * @return the eliminado
	 */
	public Character getEliminado() {
		return eliminado;
	}

	/**
	 * @param eliminado the eliminado to set
	 */
	public void setEliminado(Character eliminado) {
		this.eliminado = eliminado;
	}

	/**
	 * @return the detCxHonorarios
	 */
	public Integer getDetCxHonorarios() {
		return detCxHonorarios;
	}

	/**
	 * @param detCxHonorarios the detCxHonorarios to set
	 */
	public void setDetCxHonorarios(Integer detCxHonorarios) {
		this.detCxHonorarios = detCxHonorarios;
	}

	/**
	 * @return the detAsocioCxSalasMat
	 */
	public Integer getDetAsocioCxSalasMat() {
		return detAsocioCxSalasMat;
	}

	/**
	 * @param detAsocioCxSalasMat the detAsocioCxSalasMat to set
	 */
	public void setDetAsocioCxSalasMat(Integer detAsocioCxSalasMat) {
		this.detAsocioCxSalasMat = detAsocioCxSalasMat;
	}

	/**
	 * @return the esPortatil
	 */
	public char getEsPortatil() {
		return esPortatil;
	}

	/**
	 * @param esPortatil the esPortatil to set
	 */
	public void setEsPortatil(char esPortatil) {
		this.esPortatil = esPortatil;
	}

	/**
	 * @return the dejarExcento
	 */
	public char getDejarExcento() {
		return dejarExcento;
	}

	/**
	 * @param dejarExcento the dejarExcento to set
	 */
	public void setDejarExcento(char dejarExcento) {
		this.dejarExcento = dejarExcento;
	}

	/**
	 * @return the porcentajeDctoPromServ
	 */
	public BigDecimal getPorcentajeDctoPromServ() {
		return porcentajeDctoPromServ;
	}

	/**
	 * @param porcentajeDctoPromServ the porcentajeDctoPromServ to set
	 */
	public void setPorcentajeDctoPromServ(BigDecimal porcentajeDctoPromServ) {
		this.porcentajeDctoPromServ = porcentajeDctoPromServ;
	}

	/**
	 * @return the valorDescuentoPromServ
	 */
	public BigDecimal getValorDescuentoPromServ() {
		return valorDescuentoPromServ;
	}

	/**
	 * @param valorDescuentoPromServ the valorDescuentoPromServ to set
	 */
	public void setValorDescuentoPromServ(BigDecimal valorDescuentoPromServ) {
		this.valorDescuentoPromServ = valorDescuentoPromServ;
	}

	/**
	 * @return the porcHonorarioPromServ
	 */
	public BigDecimal getPorcHonorarioPromServ() {
		return porcHonorarioPromServ;
	}

	/**
	 * @param porcHonorarioPromServ the porcHonorarioPromServ to set
	 */
	public void setPorcHonorarioPromServ(BigDecimal porcHonorarioPromServ) {
		this.porcHonorarioPromServ = porcHonorarioPromServ;
	}

	/**
	 * @return the valorHonorarioPromServ
	 */
	public BigDecimal getValorHonorarioPromServ() {
		return valorHonorarioPromServ;
	}

	/**
	 * @param valorHonorarioPromServ the valorHonorarioPromServ to set
	 */
	public void setValorHonorarioPromServ(BigDecimal valorHonorarioPromServ) {
		this.valorHonorarioPromServ = valorHonorarioPromServ;
	}

	/**
	 * @return the programa
	 */
	public Long getPrograma() {
		return programa;
	}

	/**
	 * @param programa the programa to set
	 */
	public void setPrograma(Long programa) {
		this.programa = programa;
	}

	/**
	 * @return the porcentajeDctoBonoServ
	 */
	public BigDecimal getPorcentajeDctoBonoServ() {
		return porcentajeDctoBonoServ;
	}

	/**
	 * @param porcentajeDctoBonoServ the porcentajeDctoBonoServ to set
	 */
	public void setPorcentajeDctoBonoServ(BigDecimal porcentajeDctoBonoServ) {
		this.porcentajeDctoBonoServ = porcentajeDctoBonoServ;
	}

	/**
	 * @return the valorDescuentoBonoServ
	 */
	public BigDecimal getValorDescuentoBonoServ() {
		return valorDescuentoBonoServ;
	}

	/**
	 * @param valorDescuentoBonoServ the valorDescuentoBonoServ to set
	 */
	public void setValorDescuentoBonoServ(BigDecimal valorDescuentoBonoServ) {
		this.valorDescuentoBonoServ = valorDescuentoBonoServ;
	}

	/**
	 * @return the porcentajeDctoOdontologico
	 */
	public BigDecimal getPorcentajeDctoOdontologico() {
		return porcentajeDctoOdontologico;
	}

	/**
	 * @param porcentajeDctoOdontologico the porcentajeDctoOdontologico to set
	 */
	public void setPorcentajeDctoOdontologico(BigDecimal porcentajeDctoOdontologico) {
		this.porcentajeDctoOdontologico = porcentajeDctoOdontologico;
	}

	/**
	 * @return the valorDescuentoOdontologico
	 */
	public BigDecimal getValorDescuentoOdontologico() {
		return valorDescuentoOdontologico;
	}

	/**
	 * @param valorDescuentoOdontologico the valorDescuentoOdontologico to set
	 */
	public void setValorDescuentoOdontologico(BigDecimal valorDescuentoOdontologico) {
		this.valorDescuentoOdontologico = valorDescuentoOdontologico;
	}

	/**
	 * @return the detPaqOdonConvenio
	 */
	public Integer getDetPaqOdonConvenio() {
		return detPaqOdonConvenio;
	}

	/**
	 * @param detPaqOdonConvenio the detPaqOdonConvenio to set
	 */
	public void setDetPaqOdonConvenio(Integer detPaqOdonConvenio) {
		this.detPaqOdonConvenio = detPaqOdonConvenio;
	}

	/**
	 * @return the idDetCargo
	 */
	public long getIdDetCargo() {
		return idDetCargo;
	}

	/**
	 * @param idDetCargo the idDetCargo to set
	 */
	public void setIdDetCargo(long idDetCargo) {
		this.idDetCargo = idDetCargo;
	}

}
