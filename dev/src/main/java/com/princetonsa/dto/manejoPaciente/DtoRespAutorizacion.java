package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadFecha;

import com.princetonsa.mundo.UsuarioBasico;

public class DtoRespAutorizacion implements Serializable
{
	private String det_autorizacion;
	private String vigencia;
	private InfoDatosInt tipo_vigencia;	
	private String nombreTipoVigencia;
	private String numero_autorizacion;
	private String persona_autoriza;
	private String valor_cobertura;
	private String tipo_cobertura;
	private String valor_pago_paciente;
	private String tipo_pago_paciente;
	private String persona_recibe;
	private String cargo_pers_recibe;
	private String nombrePersonaRegistro;
	private String persona_registro;
	private String codCargoPersRegistro;
	private String fecha_registro;
	private String hora_registro;
	private String fecha_autorizacion;
	private String hora_autorizacion;
	private String fecha_anulacion;
	private String hora_anulacion;
	private UsuarioBasico usuario_anulacion;
	private String motivo_anulacion;
	private String cantidad_solicitada;  
	private String cantidad_autorizada;
	private String fecha_ini_autorizada;
	private String fecha_fin_autorizada;
	private String observacion;
	private String nombreCargoPersRecibe;
	private String nombreCargoPersRegistro;
	private boolean tieneActivaVigencia;
	private boolean esOrdenMedicaActiva;
	
	/**
	 * estado de la respuesta de la autorizacion 
	 * */
	private String estadorespauto;
	
	/**
	 * observaciones de la autorizacion 
	 * */
	private String numeroSolicitud;
	
	/**
	 * Cargos Persona Autoriza 
	 * */
	private String cargoPerRegistra;
	
	/**
	 * Cargos Persona Autoriza 
	 * */
	private InfoDatosString servicio;
	
	/**
	 * Estado de Ingreso Respuesta   
	 * */
	private boolean estadoIngresoResp;

	/**
	 * Adjuntos de la Respuesta del Detalle Autorizacion
	 */
	private ArrayList<DtoAdjAutorizacion> adjuntos;
	
	
	/**
	 * posicion adjuntos
	 */
	private int posicionAdjuntos;
	
	/**
	 * Constructor
	 */
	public DtoRespAutorizacion()
	{
		this.clean();
	}
	
	public void clean()
	{
		this.det_autorizacion = "";
		this.vigencia = "";
		this.tipo_vigencia = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");		
		this.numero_autorizacion = "";
		this.persona_autoriza = "";
		this.valor_cobertura = "";
		this.tipo_cobertura = "";
		this.valor_pago_paciente = "";
		this.tipo_pago_paciente = "";
		this.persona_recibe = "";
		this.cargo_pers_recibe = "";
		this.persona_registro = "";
		this.codCargoPersRegistro = "";
		this.fecha_registro = "";
		this.hora_registro = "";
		this.fecha_autorizacion = "";
		this.hora_autorizacion = "";
		this.fecha_anulacion = "";
		this.hora_anulacion = "";
		this.usuario_anulacion = new UsuarioBasico();
		this.motivo_anulacion = "";
		this.cantidad_solicitada = "" ;  
		this.cantidad_autorizada = "" ;
		this.fecha_ini_autorizada = "";
		this.fecha_fin_autorizada = ""; 
		this.observacion ="";
		this.estadorespauto = "";
		this.numeroSolicitud = "";
		this.cargoPerRegistra = "";
		this.servicio = new InfoDatosString("","");
		this.nombreCargoPersRecibe = "";
		this.adjuntos = new ArrayList<DtoAdjAutorizacion>();
		this.estadoIngresoResp = false;		
		this.nombrePersonaRegistro = "";
		this.nombreCargoPersRegistro = "";
		this.posicionAdjuntos = ConstantesBD.codigoNuncaValido;
		this.tieneActivaVigencia = false;
		this.esOrdenMedicaActiva = true;
	}

	/**
	 * @return the det_autorizacion
	 */
	public String getDetAutorizacion() {
		return det_autorizacion;
	}

	/**
	 * @param det_autorizacion the det_autorizacion to set
	 */
	public void setDetAutorizacion(String det_autorizacion) {
		this.det_autorizacion = det_autorizacion;
	}

	/**
	 * @return the vigencia
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia the vigencia to set
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return the tipo_vigencia
	 */
	public InfoDatosInt getTipoVigencia() {
		return tipo_vigencia;
	} 

	/**
	 * @param tipo_vigencia the tipo_vigencia to set
	 */
	public void setTipoVigencia(InfoDatosInt tipo_vigencia) {
		this.tipo_vigencia = tipo_vigencia;
	}
	
	/**
	 * @return the codigo tipo_vigencia
	 */
	public int getCodigoTipoVigencia() {
		return tipo_vigencia.getCodigo();
	}

	/**
	 * @param tipo_vigencia the tipo_vigencia to set
	 */
	public void setCodigoTipoVigencia(int tipo_vigencia) {
		this.tipo_vigencia.setCodigo(tipo_vigencia);
	}
	
	/**
	 * @return the numero_autorizacion
	 */
	public String getNumeroAutorizacion() {
		return numero_autorizacion;
	}

	/**
	 * @param numero_autorizacion the numero_autorizacion to set
	 */
	public void setNumeroAutorizacion(String numero_autorizacion) {
		this.numero_autorizacion = numero_autorizacion;
	}

	/**
	 * @return the persona_autoriza
	 */
	public String getPersonaAutoriza() {
		return persona_autoriza;
	}

	/**
	 * @param persona_autoriza the persona_autoriza to set
	 */
	public void setPersonaAutoriza(String persona_autoriza) {
		this.persona_autoriza = persona_autoriza;
	}

	/**
	 * @return the valor_cobertura
	 */
	public String getValorCobertura() {
		return valor_cobertura;
	}

	/**
	 * @param valor_cobertura the valor_cobertura to set
	 */
	public void setValorCobertura(String valor_cobertura) {
		this.valor_cobertura = valor_cobertura;
	}

	/**
	 * @return the tipo_cobertura
	 */
	public String getTipoCobertura() {
		return tipo_cobertura;
	}

	/**
	 * @param tipo_cobertura the tipo_cobertura to set
	 */
	public void setTipoCobertura(String tipo_cobertura) {
		this.tipo_cobertura = tipo_cobertura;
	}

	/**
	 * @return the valor_pago_paciente
	 */
	public String getValorPagoPaciente() {
		return valor_pago_paciente;
	}

	/**
	 * @param valor_pago_paciente the valor_pago_paciente to set
	 */
	public void setValorPagoPaciente(String valor_pago_paciente) {
		this.valor_pago_paciente = valor_pago_paciente;
	}

	/**
	 * @return the tipo_pago_paciente
	 */
	public String getTipoPagoPaciente() {
		return tipo_pago_paciente;
	}

	/**
	 * @param tipo_pago_paciente the tipo_pago_paciente to set
	 */
	public void setTipoPagoPaciente(String tipo_pago_paciente) {
		this.tipo_pago_paciente = tipo_pago_paciente;
	}

	/**
	 * @return the persona_recibe
	 */
	public String getPersonaRecibe() {
		return persona_recibe;
	}

	/**
	 * @param persona_recibe the persona_recibe to set
	 */
	public void setPersonaRecibe(String persona_recibe) {
		this.persona_recibe = persona_recibe;
	}

	/**
	 * @return the cargo_pers_recibe
	 */
	public String getCargoPersRecibe() {
		return cargo_pers_recibe;
	}

	/**
	 * @param cargo_pers_recibe the cargo_pers_recibe to set
	 */
	public void setCargoPersRecibe(String cargo_pers_recibe) {
		this.cargo_pers_recibe = cargo_pers_recibe;
	}

	/**
	 * @return the persona_registro
	 */
	public String getPersonaRegistro() {
		return persona_registro;
	}

	/**
	 * @param persona_registro the persona_registro to set
	 */
	public void setPersonaRegistro(String persona_registro) {
		this.persona_registro = persona_registro;
	}

	/**
	 * @return the fecha_registro
	 */
	public String getFechaRegistro() {
		return fecha_registro;
	}

	/**
	 * @param fecha_registro the fecha_registro to set
	 */
	public void setFechaRegistro(String fecha_registro) {
		this.fecha_registro = fecha_registro;
	}

	/**
	 * @return the hora_registro
	 */
	public String getHoraRegistro() {
		return hora_registro;
	}

	/**
	 * @param hora_registro the hora_registro to set
	 */
	public void setHoraRegistro(String hora_registro) {
		this.hora_registro = hora_registro;
	}

	/**
	 * @return the fecha_autorizacion
	 */
	public String getFechaAutorizacion() {
		return fecha_autorizacion;
	}

	/**
	 * @param fecha_autorizacion the fecha_autorizacion to set
	 */
	public void setFechaAutorizacion(String fecha_autorizacion) {
		this.fecha_autorizacion = fecha_autorizacion;
	}

	/**
	 * @return the hora_autorizacion
	 */
	public String getHoraAutorizacion() {
		return hora_autorizacion;
	}

	/**
	 * @param hora_autorizacion the hora_autorizacion to set
	 */
	public void setHoraAutorizacion(String hora_autorizacion) {
		this.hora_autorizacion = hora_autorizacion;
	}

	/**
	 * @return the fecha_anulacion
	 */
	public String getFechaAnulacion() {
		return fecha_anulacion;
	}

	/**
	 * @param fecha_anulacion the fecha_anulacion to set
	 */
	public void setFechaAnulacion(String fecha_anulacion) {
		this.fecha_anulacion = fecha_anulacion;
	}

	/**
	 * @return the hora_anulacion
	 */
	public String getHoraAnulacion() {
		return hora_anulacion;
	}

	/**
	 * @param hora_anulacion the hora_anulacion to set
	 */
	public void setHoraAnulacion(String hora_anulacion) {
		this.hora_anulacion = hora_anulacion;
	}

	/**
	 * @return the usuario_anulacion
	 */
	public UsuarioBasico getUsuarioAnulacion() {
		return usuario_anulacion;
	}

	/**
	 * @param usuario_anulacion the usuario_anulacion to set
	 */
	public void setUsuarioAnulacion(UsuarioBasico usuario_anulacion) {
		this.usuario_anulacion = usuario_anulacion;
	}

	/**
	 * @return the motivo_anulacion
	 */
	public String getMotivoAnulacion() {
		return motivo_anulacion;
	}

	/**
	 * @param motivo_anulacion the motivo_anulacion to set
	 */
	public void setMotivoAnulacion(String motivo_anulacion) {
		this.motivo_anulacion = motivo_anulacion;
	}
	
	/**
	 * @return the cantidad_solicitada
	 */
	public String getCantidadSolicitada() {
		return cantidad_solicitada;
	}

	/**
	 * @param cantidad_solicitada the cantidad_solicitada to set
	 */
	public void setCantidadSolicitada(String cantidad_solicitada) {
		this.cantidad_solicitada = cantidad_solicitada;
	}

	/**
	 * @return the cantidad_autorizada
	 */
	public String getCantidadAutorizada() {
		return cantidad_autorizada;
	}

	/**
	 * @param cantidad_autorizada the cantidad_autorizada to set
	 */
	public void setCantidadAutorizada(String cantidad_autorizada) {
		this.cantidad_autorizada = cantidad_autorizada;
	}

	/**
	 * @return the fecha_ini_autorizada
	 */
	public String getFechaInicialAutorizada() {
		return fecha_ini_autorizada;
	}

	/**
	 * @param fecha_ini_autorizada the fecha_ini_autorizada to set
	 */
	public void setFechaInicialAutorizada(String fecha_ini_autorizada) {
		this.fecha_ini_autorizada = fecha_ini_autorizada;
	}
 
	/**
	 * @return the fecha_fin_autorizada
	 */
	public String getFechaFinalAutorizada() {
		return fecha_fin_autorizada;
	}

	/**
	 * @param fecha_fin_autorizada the fecha_fin_autorizada to set
	 */
	public void setFechaFinalAutorizada(String fecha_fin_autorizada) {
		this.fecha_fin_autorizada = fecha_fin_autorizada;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public String getCargoPerRegistra() {
		return cargoPerRegistra;
	}

	public void setCargoPerRegistra(String cargoPerRegistra) {
		this.cargoPerRegistra = cargoPerRegistra;
	}

	public String getEstadorespauto() {
		return estadorespauto;
	}

	public void setEstadorespauto(String estadorespauto) {
		this.estadorespauto = estadorespauto;
	}
	
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	public InfoDatosString getServicio() {
		return servicio;
	}

	public void setServicio(InfoDatosString servicio) {
		this.servicio = servicio;
	}

	public String getNombreCargoPersRecibe() {
		return nombreCargoPersRecibe;
	}

	public void setNombreCargoPersRecibe(String nombreCargoPersRecibe) {
		this.nombreCargoPersRecibe = nombreCargoPersRecibe;
	}

	public boolean isEstadoIngresoResp() {
		return estadoIngresoResp;
	}
	
	public void setEstadoIngresoResp(boolean estadoIngresoResp) {
		this.estadoIngresoResp = estadoIngresoResp;
	}
	
	public int getPosAdjuntos() {
		return this.adjuntos.size();
	}

	/**
	 * Método para saber el número de archivos adjuntos de la autorizacion
	 * @return
	 */
	public int getNumAdjuntos()
	{
		return this.adjuntos.size();
	}

	public String getDet_autorizacion() {
		return det_autorizacion;
	}

	public void setDet_autorizacion(String det_autorizacion) {
		this.det_autorizacion = det_autorizacion;
	}

	public InfoDatosInt getTipo_vigencia() {
		return tipo_vigencia;
	}

	public void setTipo_vigencia(InfoDatosInt tipo_vigencia) {
		this.tipo_vigencia = tipo_vigencia;
	}

	public String getNumero_autorizacion() {
		return numero_autorizacion;
	}

	public void setNumero_autorizacion(String numero_autorizacion) {
		this.numero_autorizacion = numero_autorizacion;
	}

	public String getPersona_autoriza() {
		return persona_autoriza;
	}

	public void setPersona_autoriza(String persona_autoriza) {
		this.persona_autoriza = persona_autoriza;
	}

	public String getValor_cobertura() {
		return valor_cobertura;
	}

	public void setValor_cobertura(String valor_cobertura) {
		this.valor_cobertura = valor_cobertura;
	}

	public String getTipo_cobertura() {
		return tipo_cobertura;
	}

	public void setTipo_cobertura(String tipo_cobertura) {
		this.tipo_cobertura = tipo_cobertura;
	}

	public String getValor_pago_paciente() {
		return valor_pago_paciente;
	}

	public void setValor_pago_paciente(String valor_pago_paciente) {
		this.valor_pago_paciente = valor_pago_paciente;
	}

	public String getTipo_pago_paciente() {
		return tipo_pago_paciente;
	}

	public void setTipo_pago_paciente(String tipo_pago_paciente) {
		this.tipo_pago_paciente = tipo_pago_paciente;
	}

	public String getPersona_recibe() {
		return persona_recibe;
	}

	public void setPersona_recibe(String persona_recibe) {
		this.persona_recibe = persona_recibe;
	}

	public String getCargo_pers_recibe() {
		return cargo_pers_recibe;
	}

	public void setCargo_pers_recibe(String cargo_pers_recibe) {
		this.cargo_pers_recibe = cargo_pers_recibe;
	}

	public String getPersona_registro() {
		return persona_registro;
	}

	public void setPersona_registro(String persona_registro) {
		this.persona_registro = persona_registro;
	}

	public String getFecha_registro() {
		return fecha_registro;
	}

	public void setFecha_registro(String fecha_registro) {
		this.fecha_registro = fecha_registro;
	}

	public String getHora_registro() {
		return hora_registro;
	}

	public void setHora_registro(String hora_registro) {
		this.hora_registro = hora_registro;
	}

	public String getFecha_autorizacion() {
		return fecha_autorizacion;
	}

	public void setFecha_autorizacion(String fecha_autorizacion) {
		this.fecha_autorizacion = fecha_autorizacion;
	}

	public String getHora_autorizacion() {
		return hora_autorizacion;
	}

	public void setHora_autorizacion(String hora_autorizacion) {
		this.hora_autorizacion = hora_autorizacion;
	}

	public String getFecha_anulacion() {
		return fecha_anulacion;
	}

	public void setFecha_anulacion(String fecha_anulacion) {
		this.fecha_anulacion = fecha_anulacion;
	}

	public String getHora_anulacion() {
		return hora_anulacion;
	}

	public void setHora_anulacion(String hora_anulacion) {
		this.hora_anulacion = hora_anulacion;
	}

	public UsuarioBasico getUsuario_anulacion() {
		return usuario_anulacion;
	}

	public void setUsuario_anulacion(UsuarioBasico usuario_anulacion) {
		this.usuario_anulacion = usuario_anulacion;
	}

	public String getMotivo_anulacion() {
		return motivo_anulacion;
	}

	public void setMotivo_anulacion(String motivo_anulacion) {
		this.motivo_anulacion = motivo_anulacion;
	}

	public String getCantidad_solicitada() {
		return cantidad_solicitada;
	}

	public void setCantidad_solicitada(String cantidad_solicitada) {
		this.cantidad_solicitada = cantidad_solicitada;
	}

	public String getCantidad_autorizada() {
		return cantidad_autorizada;
	}

	public void setCantidad_autorizada(String cantidad_autorizada) {
		this.cantidad_autorizada = cantidad_autorizada;
	}

	public String getFecha_ini_autorizada() {
		return fecha_ini_autorizada;
	}

	public void setFecha_ini_autorizada(String fecha_ini_autorizada) {
		this.fecha_ini_autorizada = fecha_ini_autorizada;
	}

	public String getFecha_fin_autorizada() {
		return fecha_fin_autorizada;
	}

	public void setFecha_fin_autorizada(String fecha_fin_autorizada) {
		this.fecha_fin_autorizada = fecha_fin_autorizada;
	}

	public ArrayList<DtoAdjAutorizacion> getAdjuntos() {
		return adjuntos;
	}

	public void setAdjuntos(ArrayList<DtoAdjAutorizacion> adjuntos) {
		this.adjuntos = adjuntos;
	}
	
	/**
	 * @return the codCargoPersRegistro
	 */
	public String getCodCargoPersRegistro() {
		return codCargoPersRegistro;
	}

	/**
	 * @param codCargoPersRegistro the codCargoPersRegistro to set
	 */
	public void setCodCargoPersRegistro(String codCargoPersRegistro) {
		this.codCargoPersRegistro = codCargoPersRegistro;
	}

	public String getNombrePersonaRegistro() {
		return nombrePersonaRegistro;
	}

	public void setNombrePersonaRegistro(String nombrePersonaRegistro) {
		this.nombrePersonaRegistro = nombrePersonaRegistro;
	}

	public String getNombreCargoPersRegistro() {
		return nombreCargoPersRegistro;
	}

	public void setNombreCargoPersRegistro(String nombreCargoPersRegistro) {
		this.nombreCargoPersRegistro = nombreCargoPersRegistro;
	}	
	
	
	/**
	 * @return the posicionAdjuntos
	 */
	public int getPosicionAdjuntos() {
		return posicionAdjuntos;
	}

	/**
	 * @param posicionAdjuntos the posicionAdjuntos to set
	 */
	public void setPosicionAdjuntos(int posicionAdjuntos) {
		this.posicionAdjuntos = posicionAdjuntos;
	}
	
	public boolean isTieneActivaVigencia() {
		return tieneActivaVigencia;
	}

	public void setTieneActivaVigencia(boolean tieneActivaVigencia) {
		this.tieneActivaVigencia = tieneActivaVigencia;
	}

	public boolean isEsOrdenMedicaActiva() {
		return esOrdenMedicaActiva;
	}

	public void setEsOrdenMedicaActiva(boolean esOrdenMedicaActiva) {
		this.esOrdenMedicaActiva = esOrdenMedicaActiva;
	}

	public String getNombreTipoVigencia() {
		return nombreTipoVigencia;
	}

	public void setNombreTipoVigencia(String nombreTipoVigencia) {
		this.nombreTipoVigencia = nombreTipoVigencia;
	}
}