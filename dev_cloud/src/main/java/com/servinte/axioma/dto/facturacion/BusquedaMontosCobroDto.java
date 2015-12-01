package com.servinte.axioma.dto.facturacion;

import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author diecorqu
 * @version 1.0
 * @created 05-jul-2012 12:48:54 p.m.
 */
public class BusquedaMontosCobroDto {

	private Integer viaIngresoID;
	private String viaIngresoNombre;
	private String tipoPacienteAcronimo;
	private String tipoPacienteNombre;
	private Integer estratoID;
	private String estratoDescripcion;	
	private Integer naturalezaID;
	private String naturalezaNombre;
	private Integer tipoMontoID;
	private String tipoMontoNombre;
	private String tipoDetalleAcronimo;
	private String tipoDetalle;
	private char tipoAfiliadoAcronimo;
	private String tipoAfiliadoNombre;
	private Integer detalleCodigo;
	private boolean manejaDetalle;	
	private Double valor;
	private String valorHelper;
	private Double porcentaje;
	private String porcentajeHelper;
	private Integer cantidadMonto;
	private boolean permiteEliminar;
	private Long idSubcuenta;
	private Integer idMontoCobro;
	private String usuarioRegistra; 
	private boolean activo;	
	
	

	public BusquedaMontosCobroDto() {}
	
	/**
	 * @param viaIngresoID
	 * @param viaIngresoNombre
	 * @param tipoPacienteAcronimo
	 * @param tipoPacienteNombre
	 * @param estratoID
	 * @param estratoDescripcion
	 * @param naturalezaID
	 * @param naturalezaNombre
	 * @param tipoMontoID
	 * @param tipoMontoNombre
	 * @param tipoDetalleAcronimo
	 * @param tipoDetalle
	 * @param tipoAfiliadoAcronimo
	 * @param tipoAfiliadoNombre
	 * @param valor
	 * @param porcentaje
	 * @param cantidadMonto
	 */
	public BusquedaMontosCobroDto(Integer viaIngresoID, String viaIngresoNombre,
			String tipoPacienteAcronimo, String tipoPacienteNombre,
			Integer estratoID, String estratoDescripcion, Integer naturalezaID,
			String naturalezaNombre, Integer tipoMontoID, String tipoMontoNombre,
			String tipoDetalle, Integer detalleCodigo,
			char tipoAfiliadoAcronimo, String tipoAfiliadoNombre, Double valor,
			Double porcentaje, Integer cantidadMonto) {
		this.viaIngresoID = viaIngresoID;
		this.viaIngresoNombre = viaIngresoNombre;
		this.tipoPacienteAcronimo = tipoPacienteAcronimo;
		this.tipoPacienteNombre = tipoPacienteNombre;
		this.estratoID = estratoID;
		this.estratoDescripcion = estratoDescripcion;
		this.naturalezaID = naturalezaID;
		this.naturalezaNombre = naturalezaNombre;
		this.tipoMontoID = tipoMontoID;
		this.tipoMontoNombre = tipoMontoNombre;
		this.tipoDetalle = tipoDetalle;
		this.detalleCodigo = detalleCodigo;
		this.tipoAfiliadoAcronimo = tipoAfiliadoAcronimo;
		this.tipoAfiliadoNombre = tipoAfiliadoNombre;
		this.setValor(valor);
		this.setPorcentaje(porcentaje);
		this.cantidadMonto = cantidadMonto;
	}
	
	/**
	 * @return the viaIngresoID
	 */
	public Integer getViaIngresoID() {
		return viaIngresoID;
	}

	/**
	 * @param viaIngresoID the viaIngresoID to set
	 */
	public void setViaIngresoID(Integer viaIngresoID) {
		this.viaIngresoID = viaIngresoID;
	}

	/**
	 * @return the viaIngresoNombre
	 */
	public String getViaIngresoNombre() {
		return viaIngresoNombre;
	}

	/**
	 * @param viaIngresoNombre the viaIngresoNombre to set
	 */
	public void setViaIngresoNombre(String viaIngresoNombre) {
		this.viaIngresoNombre = viaIngresoNombre;
	}

	/**
	 * @return the tipoPacienteAcronimo
	 */
	public String getTipoPacienteAcronimo() {
		return tipoPacienteAcronimo;
	}

	/**
	 * @param tipoPacienteAcronimo the tipoPacienteAcronimo to set
	 */
	public void setTipoPacienteAcronimo(String tipoPacienteAcronimo) {
		this.tipoPacienteAcronimo = tipoPacienteAcronimo;
	}

	/**
	 * @return the tipoPacienteNombre
	 */
	public String getTipoPacienteNombre() {
		return tipoPacienteNombre;
	}

	/**
	 * @param tipoPacienteNombre the tipoPacienteNombre to set
	 */
	public void setTipoPacienteNombre(String tipoPacienteNombre) {
		this.tipoPacienteNombre = tipoPacienteNombre;
	}

	/**
	 * @return the estratoID
	 */
	public Integer getEstratoID() {
		return estratoID;
	}

	/**
	 * @param estratoID the estratoID to set
	 */
	public void setEstratoID(Integer estratoID) {
		this.estratoID = estratoID;
	}

	/**
	 * @return the estratoDescripcion
	 */
	public String getEstratoDescripcion() {
		return estratoDescripcion;
	}

	/**
	 * @param estratoDescripcion the estratoDescripcion to set
	 */
	public void setEstratoDescripcion(String estratoDescripcion) {
		this.estratoDescripcion = estratoDescripcion;
	}

	/**
	 * @return the naturalezaID
	 */
	public Integer getNaturalezaID() {
		return naturalezaID;
	}

	/**
	 * @param naturalezaID the naturalezaID to set
	 */
	public void setNaturalezaID(Integer naturalezaID) {
		this.naturalezaID = naturalezaID;
	}

	/**
	 * @return the naturalezaNombre
	 */
	public String getNaturalezaNombre() {
		return naturalezaNombre;
	}

	/**
	 * @param naturalezaNombre the naturalezaNombre to set
	 */
	public void setNaturalezaNombre(String naturalezaNombre) {
		this.naturalezaNombre = naturalezaNombre;
	}

	/**
	 * @return the tipoMontoID
	 */
	public Integer getTipoMontoID() {
		return tipoMontoID;
	}

	/**
	 * @param tipoMontoID the tipoMontoID to set
	 */
	public void setTipoMontoID(Integer tipoMontoID) {
		this.tipoMontoID = tipoMontoID;
	}

	/**
	 * @return the tipoMontoNombre
	 */
	public String getTipoMontoNombre() {
		return tipoMontoNombre;
	}

	/**
	 * @param tipoMontoNombre the tipoMontoNombre to set
	 */
	public void setTipoMontoNombre(String tipoMontoNombre) {
		this.tipoMontoNombre = tipoMontoNombre;
	}

	/**
	 * @return the tipoDetalleAcronimo
	 */
	public String getTipoDetalleAcronimo() {
		return tipoDetalleAcronimo;
	}

	/**
	 * @param tipoDetalleAcronimo the tipoDetalleAcronimo to set
	 */
	public void setTipoDetalleAcronimo(String tipoDetalleAcronimo) {
		this.tipoDetalleAcronimo = tipoDetalleAcronimo;
	}

	/**
	 * @return the tipoDetalle
	 */
	public String getTipoDetalle() {
		return tipoDetalle;
	}

	/**
	 * @param tipoDetalle the tipoDetalle to set
	 */
	public void setTipoDetalle(String tipoDetalle) {
		this.tipoDetalle = tipoDetalle;
	}

	/**
	 * @return the tipoAfiliadoAcronimo
	 */
	public char getTipoAfiliadoAcronimo() {
		return tipoAfiliadoAcronimo;
	}

	/**
	 * @param tipoAfiliadoAcronimo the tipoAfiliadoAcronimo to set
	 */
	public void setTipoAfiliadoAcronimo(char tipoAfiliadoAcronimo) {
		this.tipoAfiliadoAcronimo = tipoAfiliadoAcronimo;
	}

	/**
	 * @return the tipoAfiliadoNombre
	 */
	public String getTipoAfiliadoNombre() {
		return tipoAfiliadoNombre;
	}

	/**
	 * @param tipoAfiliadoNombre the tipoAfiliadoNombre to set
	 */
	public void setTipoAfiliadoNombre(String tipoAfiliadoNombre) {
		this.tipoAfiliadoNombre = tipoAfiliadoNombre;
	}

	/**
	 * @return the detalleCodigo
	 */
	public Integer getDetalleCodigo() {
		return detalleCodigo;
	}

	/**
	 * @param detalleCodigo the detalleCodigo to set
	 */
	public void setDetalleCodigo(Integer detalleCodigo) {
		this.detalleCodigo = detalleCodigo;
	}

	/**
	 * @return the manejaDetalle
	 */
	public boolean isManejaDetalle() {
		return manejaDetalle;
	}

	/**
	 * @param manejaDetalle the manejaDetalle to set
	 */
	public void setManejaDetalle(boolean manejaDetalle) {
		this.manejaDetalle = manejaDetalle;
	}

	/**
	 * @return the valor
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
		if(valor!=null && valor.doubleValue()>=0){
			this.valor = valor;
			this.valorHelper = valor.toString();
		}else{
			this.valor = null;
		}			
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo valorHelper
	
	 * @return retorna la variable valorHelper 
	 * @author Angela Maria Aguirre 
	 */
	public String getValorHelper() {
		return valorHelper;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo valorHelper
	
	 * @param valor para el atributo valorHelper 
	 * @author Angela Maria Aguirre 
	 */
	public void setValorHelper(String valorHelper) {
		this.valorHelper=valorHelper;
		if(!UtilidadTexto.isEmpty(valorHelper)){
			Double valorAux = Utilidades.convertirADouble(valorHelper, false);
			this.valor = valorAux;			
		}else{
			this.valor = null;
		}
	}	

	/**
	 * @return the porcentaje
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
		if(porcentaje!=null && porcentaje.doubleValue()>=0){
			this.porcentaje = porcentaje;
			this.porcentajeHelper = porcentaje.toString();
		}else{
			this.porcentaje = null;
		}
	}

	/**
	 * @return the porcentajeHelper
	 */
	public String getPorcentajeHelper() {
		return porcentajeHelper;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo porcentajeHelper
	
	 * @param valor para el atributo porcentajeHelper 
	 * @author Angela Maria Aguirre 
	 */
	public void setPorcentajeHelper(String porcentajeHelper) {
		this.porcentajeHelper = porcentajeHelper;
		if(!UtilidadTexto.isEmpty(porcentajeHelper)){
			Double porcentajeAux = Utilidades.convertirADouble(porcentajeHelper, false);
			this.porcentaje = porcentajeAux;			
		}else{
			this.porcentaje = null;
		}
		
	}	

	/**
	 * @return the cantidadMonto
	 */
	public Integer getCantidadMonto() {
		return cantidadMonto;
	}

	/**
	 * @param cantidadMonto the cantidadMonto to set
	 */
	public void setCantidadMonto(Integer cantidadMonto) {
		this.cantidadMonto = cantidadMonto;
	}

	/**
	 * @return the permiteEliminar
	 */
	public boolean isPermiteEliminar() {
		return permiteEliminar;
	}

	/**
	 * @param permiteEliminar the permiteEliminar to set
	 */
	public void setPermiteEliminar(boolean permiteEliminar) {
		this.permiteEliminar = permiteEliminar;
	}

	/**
	 * @return the idSubcuenta
	 */
	public Long getIdSubcuenta() {
		return idSubcuenta;
	}

	/**
	 * @param idSubcuenta the idSubcuenta to set
	 */
	public void setIdSubcuenta(Long idSubcuenta) {
		this.idSubcuenta = idSubcuenta;
	}

	/**
	 * @return the idMontoCobro
	 */
	public Integer getIdMontoCobro() {
		return idMontoCobro;
	}

	/**
	 * @param idMontoCobro the idMontoCobro to set
	 */
	public void setIdMontoCobro(Integer idMontoCobro) {
		this.idMontoCobro = idMontoCobro;
	}

	/**
	 * @return the usuarioRegistra
	 */
	public String getUsuarioRegistra() {
		return usuarioRegistra;
	}

	/**
	 * @param usuarioRegistra the usuarioRegistra to set
	 */
	public void setUsuarioRegistra(String usuarioRegistra) {
		this.usuarioRegistra = usuarioRegistra;
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

}