package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Esta clase se encarga de Almacenar los datos 
 * para el cambio de responsable del detalle de
 * faltante / sobrante
 *
 * @author Angela Maria Aguirre
 * @since 22/07/2010
 */
public class DTOCambioResponsableDetFaltanteSobrante extends DtoFaltanteSobrante implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo que almacena la fecha en la cual se realiza el 
	 * cambio de responsable de faltante sobrante caja.
	 */
	private Date fechaProceso;
	
	/**
	 * Atributo que almacena la hora en la cual se realiza el 
	 * cambio de responsable de faltante sobrante caja.
	 */
	private String horaProceso;
	
	/**
	 * Atributo en el cual se almacena el motivo por el cual es realizado
	 * el cambio de responsable faltante sobrante de caja.
	 */
	private String motivo;
	
	/**
	 * Atributo en el cual se almacena el n&uacute;mero que identifica el
	 * faltante sobrante de caja.
	 */
	private long idFaltanteSobrante;
	
	/**
	 * Atributo que almacena la fecha inicial desde la cual se realiza la 
	 * b&uacute;squeda de los registros generados de faltante sobrante de caja.
	 */
	private Date fechaGeneracionInicial;
	
	/**
	 * Atributo que almacena la hora en la que se generó el registro de Faltante / Sobrante
	 */
	private String horaModifica;
	
	/**
	 * Atributo que almacena la fecha final desde la cual se realiza la 
	 * b&uacute;squeda de los registros generados de faltante sobrante de caja.
	 */
	private Date fechaGeneracionFin;
	
	/**
	 * Atributo que identifica el consecutivo de caja generado.
	 */
	private int consecutivoCaja;
	
	/**
	 * Atributo que almacena el c&oacute;digo que identifica en que momento
	 * fue generado el faltante sobrante.
	 */
	private int generadoEn;
	
	/**
	 * Atributo que almacena la informaci&oacute;n a cerca de si el
	 * faltante sobrante fue generado en la apertura o cierre de caja.
	 */
	private String nombreGeneradoEn;
	
	/**
	 * Atributo que almacena el estado del faltante sobrante.
	 */
	private String estadoFaltanteSobrante;
	
	/**
	 * Atriburo que almacena el estado de contabilizaci&oacute;n del 
	 * faltante sobrante.
	 */
	private Character contabilizado;
	
	/**
	 * Atributo que almacena el consecutivo del centro de atenci&oacute;n.
	 */
	private int consecutivoCA;
	
	/**
	 * Atributo que almacena el consecutivo del faltante sobrante de caja.
	 */
	private BigDecimal consecutivo;
	
	/**
	 * Atributo que almacena el login del usuario responsable del faltante sobrante de caja.
	 */
	private String loginUsuarioResponsable;
	
	/**
	 * Atributo que almacena el valor en pesos del faltante sobrante generado.
	 */
	private BigDecimal valor;
	
	/**
	 * Atributo que almacena el login del usuario que realiza el cambio
	 * de responsable del faltante sobrante.
	 */
	private String loginUsuarioModifica;
	
	/**
	 * Atributo que almacena el login del usuario cajero.
	 */
	private String loginCajero;
	
	/**
	 * Instancia del DTO DtoUsuarioPersona.
	 */
	private DtoUsuarioPersona usuarioResponsable;
	
	/**
	 * Atributo ayudante que almacena el nombre de la persona responsable
	 * del faltante sobrante.
	 */
	private String ayundanteNombre;
	
	/**
	 * Atributo que almacena el n&uacute;mero de identificaci&oacute;n 
	 * del responsable del faltante sobrante.
	 */
	private String ayundateNumeroId;
	
	/**
	 * Atributo que determina la funcionalidad para la cual se esta realizando la consulta
	 * para el cambio de responsable o la busqueda del hist&oacute;rico del faltante sobrante
	 */
	private String funcionalidadConsultaHistorico;
	
	/**
	 * Atributo que almacena el estado de contabilizado del
	 * faltante / sobrante
	 */
	private String descripcionContabilizado;
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo fechaProceso
	
	 * @return retorna la variable fechaProceso 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaProceso() {
		return fechaProceso;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo fechaProceso
	
	 * @param valor para el atributo fechaProceso 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo horaProceso
	
	 * @return retorna la variable horaProceso 
	 * @author Angela Maria Aguirre 
	 */
	public String getHoraProceso() {
		return horaProceso;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo horaProceso
	
	 * @param valor para el atributo horaProceso 
	 * @author Angela Maria Aguirre 
	 */
	public void setHoraProceso(String horaProceso) {
		this.horaProceso = horaProceso;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo motivo
	
	 * @return retorna la variable motivo 
	 * @author Angela Maria Aguirre 
	 */
	public String getMotivo() {
		return motivo;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo motivo
	
	 * @param valor para el atributo motivo 
	 * @author Angela Maria Aguirre 
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo idFaltanteSobrante
	
	 * @return retorna la variable idFaltanteSobrante 
	 * @author Angela Maria Aguirre 
	 */
	public long getIdFaltanteSobrante() {
		return idFaltanteSobrante;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo idFaltanteSobrante
	
	 * @param valor para el atributo idFaltanteSobrante 
	 * @author Angela Maria Aguirre 
	 */
	public void setIdFaltanteSobrante(long idFaltanteSobrante) {
		this.idFaltanteSobrante = idFaltanteSobrante;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo fechaGeneracionInicial
	
	 * @return retorna la variable fechaGeneracionInicial 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaGeneracionInicial() {
		return fechaGeneracionInicial;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo fechaGeneracionInicial
	
	 * @param valor para el atributo fechaGeneracionInicial 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaGeneracionInicial(Date fechaGeneracionInicial) {
		this.fechaGeneracionInicial = fechaGeneracionInicial;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo fechaGeneracionFin
	
	 * @return retorna la variable fechaGeneracionFin 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaGeneracionFin() {
		return fechaGeneracionFin;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo fechaGeneracionFin
	
	 * @param valor para el atributo fechaGeneracionFin 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaGeneracionFin(Date fechaGeneracionFin) {
		this.fechaGeneracionFin = fechaGeneracionFin;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo consecutivoCaja
	
	 * @return retorna la variable consecutivoCaja 
	 * @author Angela Maria Aguirre 
	 */
	public int getConsecutivoCaja() {
		return consecutivoCaja;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo consecutivoCaja
	
	 * @param valor para el atributo consecutivoCaja 
	 * @author Angela Maria Aguirre 
	 */
	public void setConsecutivoCaja(int consecutivoCaja) {
		this.consecutivoCaja = consecutivoCaja;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo loginCajero
	
	 * @return retorna la variable loginCajero 
	 * @author Angela Maria Aguirre 
	 */
	public String getLoginCajero() {
		return loginCajero;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo loginCajero
	
	 * @param valor para el atributo loginCajero 
	 * @author Angela Maria Aguirre 
	 */
	public void setLoginCajero(String loginCajero) {
		this.loginCajero = loginCajero;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo generadoEn
	
	 * @return retorna la variable generadoEn 
	 * @author Angela Maria Aguirre 
	 */
	public int getGeneradoEn() {
		return generadoEn;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo generadoEn
	
	 * @param valor para el atributo generadoEn 
	 * @author Angela Maria Aguirre 
	 */
	public void setGeneradoEn(int generadoEn) {
		this.generadoEn = generadoEn;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo nombreGeneradoEn
	
	 * @return retorna la variable nombreGeneradoEn 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreGeneradoEn() {
		return nombreGeneradoEn;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo nombreGeneradoEn
	
	 * @param valor para el atributo nombreGeneradoEn 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreGeneradoEn(String nombreGeneradoEn) {
		this.nombreGeneradoEn = nombreGeneradoEn;
	}	
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo estadoFaltanteSobrante
	
	 * @return retorna la variable estadoFaltanteSobrante 
	 * @author Angela Maria Aguirre 
	 */
	public String getEstadoFaltanteSobrante() {
		return estadoFaltanteSobrante;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo estadoFaltanteSobrante
	
	 * @param valor para el atributo estadoFaltanteSobrante 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstadoFaltanteSobrante(String estadoFaltanteSobrante) {
		this.estadoFaltanteSobrante = estadoFaltanteSobrante;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo contabilizado
	
	 * @return retorna la variable contabilizado 
	 * @author Angela Maria Aguirre 
	 */
	public Character getContabilizado() {
		return contabilizado;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo contabilizado
	
	 * @param valor para el atributo contabilizado 
	 * @author Angela Maria Aguirre 
	 */
	public void setContabilizado(Character contabilizado) {
		this.contabilizado = contabilizado;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo consecutivoCA
	
	 * @return retorna la variable consecutivoCA 
	 * @author Angela Maria Aguirre 
	 */
	
	public int getConsecutivoCA() {
		return consecutivoCA;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo consecutivoCA
	
	 * @param valor para el atributo consecutivoCA 
	 * @author Angela Maria Aguirre 
	 */
	public void setConsecutivoCA(int consecutivoCA) {
		this.consecutivoCA = consecutivoCA;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo consecutivo
	
	 * @return retorna la variable consecutivo 
	 * @author Angela Maria Aguirre 
	 */
	public BigDecimal getConsecutivo() {
		return consecutivo;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo consecutivo
	
	 * @param valor para el atributo consecutivo 
	 * @author Angela Maria Aguirre 
	 */
	public void setConsecutivo(BigDecimal consecutivo) {
		this.consecutivo = consecutivo;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo usuarioResponsable
	
	 * @return retorna la variable usuarioResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public DtoUsuarioPersona getUsuarioResponsable() {
		return usuarioResponsable;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo usuarioResponsable
	
	 * @param valor para el atributo usuarioResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public void setUsuarioResponsable(DtoUsuarioPersona usuarioResponsable) {
		this.usuarioResponsable = usuarioResponsable;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo loginUsuarioResponsable
	
	 * @return retorna la variable loginUsuarioResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public String getLoginUsuarioResponsable() {
		return loginUsuarioResponsable;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo loginUsuarioResponsable
	
	 * @param valor para el atributo loginUsuarioResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public void setLoginUsuarioResponsable(String loginUsuarioResponsable) {
		this.loginUsuarioResponsable = loginUsuarioResponsable;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo valor
	
	 * @return retorna la variable valor 
	 * @author Angela Maria Aguirre 
	 */
	public BigDecimal getValor() {
		return valor;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo valor
	
	 * @param valor para el atributo valor 
	 * @author Angela Maria Aguirre 
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	/**
	 * @return M&eacute;todo que se encarga de retornar el atributo ayundanteNombre
	 */
	public String getAyundanteNombre() {
		
		if( this.getUsuarioResponsable()!=null)
		{
			this.ayundanteNombre=this.getUsuarioResponsable().getNombre()+" "+this.getUsuarioResponsable().getSegundoNombre()+" "+this.getUsuarioResponsable().getApellido()+" "+this.getUsuarioResponsable().getSegundoApellido(); 
		}
		else
		{
			ayundanteNombre="";
		}
		
		return ayundanteNombre;
	}
	
	/**
	 * @return M&eacute;todo que se encarga de retornar el atributo ayundateNumeroId
	 */
	public String getAyundateNumeroId() {
		if( this.getUsuarioResponsable()!=null)
		{
			this.ayundateNumeroId= this.getUsuarioResponsable().getNumeroID();
		}
		
		return ayundateNumeroId;
	}
	
	/**
	 * M&eacute;todo que recibe por par&aacute;metro el atributo ayundanteNombre y 
	 * se encarga de asignarlo a la variable ayundanteNombre
	 * @param String
	 */
	public void setAyundanteNombre(String ayundanteNombre) {
		this.ayundanteNombre = ayundanteNombre;
	}
	
	/**
	 * M&eacute;todo que recibe por par&aacute;metro el atributo ayundateNumeroId y 
	 * se encarga de asignarlo a la variable ayundateNumeroId
	 * @param String
	 */
	public void setAyundateNumeroId(String ayundateNumeroId) {
		this.ayundateNumeroId = ayundateNumeroId;
	}	
	
	/** Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo loginUsuarioModifica
	
	 * @return retorna la variable loginUsuarioModifica 
	 * @author Angela Maria Aguirre 
	 */
	public String getLoginUsuarioModifica() {
		return loginUsuarioModifica;
	}
	
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo loginUsuarioModifica
	
	 * @param valor para el atributo loginUsuarioModifica 
	 * @author Angela Maria Aguirre 
	 */
	public void setLoginUsuarioModifica(String loginUsuarioModifica) {
		this.loginUsuarioModifica = loginUsuarioModifica;
	}	

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo descripcionContabilizado
	
	 * @return retorna la variable descripcionContabilizado 
	 * @author Angela Maria Aguirre 
	 */
	public String getDescripcionContabilizado() {
		return descripcionContabilizado;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo descripcionContabilizado
	
	 * @param valor para el atributo descripcionContabilizado 
	 * @author Angela Maria Aguirre 
	 */
	public void setDescripcionContabilizado(String descripcionContabilizado) {
		this.descripcionContabilizado = descripcionContabilizado;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo funcionalidadConsultaHistorico
	
	 * @return retorna la variable funcionalidadConsultaHistorico 
	 * @author Angela Maria Aguirre 
	 */
	public String getFuncionalidadConsultaHistorico() {
		return funcionalidadConsultaHistorico;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo funcionalidadConsultaHistorico
	
	 * @param valor para el atributo funcionalidadConsultaHistorico 
	 * @author Angela Maria Aguirre 
	 */
	public void setFuncionalidadConsultaHistorico(
			String funcionalidadConsultaHistorico) {
		this.funcionalidadConsultaHistorico = funcionalidadConsultaHistorico;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo horaModifica
	
	 * @param valor para el atributo horaModifica 
	 * @author Angela Maria Aguirre 
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo horaModifica
	
	 * @return retorna la variable horaModifica 
	 * @author Angela Maria Aguirre 
	 */
	public String getHoraModifica() {
		return horaModifica;
	}
	
}
