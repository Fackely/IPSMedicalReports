/**
 * 
 */
package com.servinte.axioma.dto.manejoPaciente;

import util.ConstantesBD;
import util.ValoresPorDefecto;

/**
 * @author axioma
 *
 */
public class DtoReclamacionesAccEveFact 
{
	/**
	 * 
	*/
	private int codigoPk;

	/**
	 * 
	 */
	private int codigoFactura;
	
	/**
	 * 
	 */
	private String consecutivoFactura;

	/**
	 * 
	 */
	private String fechaReclamacion;

	/**
	 * 
	 */
	private String horaReclamacion;

	/**
	 * 
	 */
	private String nroReclamacion;

	/**
	 * 
	 */

	/**
	 * 
	 */
	private String anioConsReclamacion;

	/**
	 * 
	 */
	private String respuestaGlosa;

	/**
	 * 
	 */
	private int ingreso;

	/**
	 * 
	 */
	private String numRadicacionAnterior;


	/**
	 * 
	 */
	private String tipoReclamacion;

	/**
	 * 
	 */
	private String tipoEvento;

	/**
	 * 
	 */
	private String usuarioRegistro;

	/**
	 * 
	 */
	private String fechaRegistro;

	/**
	 * 
	 */
	private String horaRegistro;

	/**
	 * 
	 */
	private String usuarioModifica;

	/**
	 * 
	 */
	private String fechaModifica;

	/**
	 * 
	 */
	private String horaModifica;


	/**
	 * 
	 */
	private DtoAmparoXReclamar amparoXReclamar;
	

	/**
	 * 
	 */
	private DtoCertAtenMedicaFurips certAtenMedicaFurips;
	

	/**
	 * 
	 */
	private DtoCertAtenMedicaFurpro certAtenMedicaFurpro;
	

	/**
	 * 
	 */
	private DtoServiciosReclamados serviciosReclamados;

	
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private String nroRadicado;
	
	/**
	 * 
	 */
	private String fechaRadicacion;

	/**
	 * 
	 */
	private String horaRadicacion;

	/**
	 * 
	 */
	private String usuarioRadicacion;
	
	/**
	 * 
	 */
	private String fechaAnulacion;

	/**
	 * 
	 */
	private String horaAnulacion;

	/**
	 * 
	 */
	private String usuarioAnulacion;
	
	/**
	 * 
	 */
	private String motivoAnulacion;
	
	/**
	 * 
	 */
	private int codigoEvento;
	
	/**
	 * 
	 */
	private int codigoAccidente;
	
	
	/**
	 * 
	 */
	private String imprimirFuripsFurpro;
	
	
	/**
	 * 
	 */
	private int codigoPaciente;
	
	/**
	 * 
	 */
	private String nombresPaciente;
	
	/**
	 * 
	 */
	private String tipoIdPaciente;
	
	/**
	 * 
	 */
	private String numeroIdPaciente;
	
	/**
	 * 
	 */
	private int codigoConvenio;
	
	/**
	 * 
	 */
	private String nombreConvenio;
	
	
	/**
	 * 
	 */
	private String consecutivoIngreso;

	/**
	 * 
	 * @param codigoPk
	 * @param codigoFactura
	 * @param fechaReclamacion
	 * @param horaReclamacion
	 * @param nroReclamacion
	 * @param anioConsReclamacion
	 * @param respuestaGlosa
	 * @param ingreso
	 * @param numRadicacionAnterior
	 * @param anioConsRadicacionAnterior
	 * @param tipoReclamacion
	 * @param tipoEvento
	 * @param usuarioRegistro
	 * @param fechaRegistro
	 * @param horaRegistro
	 * @param usuarioModifica
	 * @param fechaModifica
	 * @param horaModifica
	 * @param amparoXReclamar
	 * @param certAtenMedicaFurips
	 * @param certAtenMedicaFurpro
	 * @param serviciosReclamados
	 */
	public DtoReclamacionesAccEveFact() 
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.codigoFactura = ConstantesBD.codigoNuncaValido;
		this.fechaReclamacion = "";
		this.horaReclamacion = "";
		this.nroReclamacion = "";
		this.anioConsReclamacion = "";
		this.respuestaGlosa = "";
		this.ingreso = ConstantesBD.codigoNuncaValido;
		this.numRadicacionAnterior = "";
		this.tipoReclamacion = "";
		this.tipoEvento = "";
		this.usuarioRegistro = "";
		this.fechaRegistro = "";
		this.horaRegistro = "";
		this.usuarioModifica = "";
		this.fechaModifica = "";
		this.horaModifica = "";
		this.amparoXReclamar = new DtoAmparoXReclamar();
		this.certAtenMedicaFurips = new DtoCertAtenMedicaFurips();
		this.certAtenMedicaFurpro = new DtoCertAtenMedicaFurpro();
		this.serviciosReclamados = new DtoServiciosReclamados();
		this.estado="";
		this.codigoEvento=ConstantesBD.codigoNuncaValido;
		this.codigoAccidente=ConstantesBD.codigoNuncaValido;
		this.consecutivoFactura="";
		this.imprimirFuripsFurpro=ConstantesBD.acronimoNo;
		this.codigoPaciente=ConstantesBD.codigoNuncaValido;
		this.nombresPaciente="";
		this.tipoIdPaciente="";
		this.numeroIdPaciente="";
		this.codigoConvenio=ConstantesBD.codigoNuncaValido;
		this.nombreConvenio="";
		this.consecutivoIngreso="";
		
	}


	public int getCodigoPk() {
		return codigoPk;
	}


	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}


	public int getCodigoFactura() {
		return codigoFactura;
	}


	public void setCodigoFactura(int codigoFactura) {
		this.codigoFactura = codigoFactura;
	}


	public String getFechaReclamacion() {
		return fechaReclamacion;
	}


	public void setFechaReclamacion(String fechaReclamacion) {
		this.fechaReclamacion = fechaReclamacion;
	}


	public String getHoraReclamacion() {
		return horaReclamacion;
	}


	public void setHoraReclamacion(String horaReclamacion) {
		this.horaReclamacion = horaReclamacion;
	}


	public String getNroReclamacion() {
		return nroReclamacion;
	}


	public void setNroReclamacion(String nroReclamacion) {
		this.nroReclamacion = nroReclamacion;
	}


	public String getAnioConsReclamacion() {
		return anioConsReclamacion;
	}


	public void setAnioConsReclamacion(String anioConsReclamacion) {
		this.anioConsReclamacion = anioConsReclamacion;
	}


	public String getRespuestaGlosa() {
		return respuestaGlosa;
	}


	public void setRespuestaGlosa(String respuestaGlosa) {
		this.respuestaGlosa = respuestaGlosa;
	}


	public int getIngreso() {
		return ingreso;
	}


	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}


	public String getNumRadicacionAnterior() {
		return numRadicacionAnterior;
	}


	public void setNumRadicacionAnterior(String numRadicacionAnterior) {
		this.numRadicacionAnterior = numRadicacionAnterior;
	}


	public String getTipoReclamacion() {
		return tipoReclamacion;
	}


	public void setTipoReclamacion(String tipoReclamacion) {
		this.tipoReclamacion = tipoReclamacion;
	}


	public String getTipoEvento() {
		return tipoEvento;
	}

	public String getDescTipoEvento()
	{
		return ValoresPorDefecto.getIntegridadDominio(this.tipoEvento)+"";
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}


	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}


	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}


	public String getFechaRegistro() {
		return fechaRegistro;
	}


	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}


	public String getHoraRegistro() {
		return horaRegistro;
	}


	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}


	public String getUsuarioModifica() {
		return usuarioModifica;
	}


	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}


	public String getFechaModifica() {
		return fechaModifica;
	}


	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}


	public String getHoraModifica() {
		return horaModifica;
	}


	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}


	public DtoAmparoXReclamar getAmparoXReclamar() {
		return amparoXReclamar;
	}


	public void setAmparoXReclamar(DtoAmparoXReclamar amparoXReclamar) {
		this.amparoXReclamar = amparoXReclamar;
	}


	public DtoCertAtenMedicaFurips getCertAtenMedicaFurips() {
		return certAtenMedicaFurips;
	}


	public void setCertAtenMedicaFurips(DtoCertAtenMedicaFurips certAtenMedicaFurips) {
		this.certAtenMedicaFurips = certAtenMedicaFurips;
	}


	public DtoCertAtenMedicaFurpro getCertAtenMedicaFurpro() {
		return certAtenMedicaFurpro;
	}


	public void setCertAtenMedicaFurpro(DtoCertAtenMedicaFurpro certAtenMedicaFurpro) {
		this.certAtenMedicaFurpro = certAtenMedicaFurpro;
	}


	public DtoServiciosReclamados getServiciosReclamados() {
		return serviciosReclamados;
	}


	public void setServiciosReclamados(DtoServiciosReclamados serviciosReclamados) {
		this.serviciosReclamados = serviciosReclamados;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getNroRadicado() {
		return nroRadicado;
	}


	public void setNroRadicado(String nroRadicado) {
		this.nroRadicado = nroRadicado;
	}


	public String getFechaRadicacion() {
		return fechaRadicacion;
	}


	public void setFechaRadicacion(String fechaRadicacion) {
		this.fechaRadicacion = fechaRadicacion;
	}


	public String getHoraRadicacion() {
		return horaRadicacion;
	}


	public void setHoraRadicacion(String horaRadicacion) {
		this.horaRadicacion = horaRadicacion;
	}


	public String getUsuarioRadicacion() {
		return usuarioRadicacion;
	}


	public void setUsuarioRadicacion(String usuarioRadicacion) {
		this.usuarioRadicacion = usuarioRadicacion;
	}


	public String getFechaAnulacion() {
		return fechaAnulacion;
	}


	public void setFechaAnulacion(String fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}


	public String getHoraAnulacion() {
		return horaAnulacion;
	}


	public void setHoraAnulacion(String horaAnulacion) {
		this.horaAnulacion = horaAnulacion;
	}


	public String getUsuarioAnulacion() {
		return usuarioAnulacion;
	}


	public void setUsuarioAnulacion(String usuarioAnulacion) {
		this.usuarioAnulacion = usuarioAnulacion;
	}


	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}


	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}


	public int getCodigoEvento() {
		return codigoEvento;
	}


	public void setCodigoEvento(int codigoEvento) {
		this.codigoEvento = codigoEvento;
	}


	public int getCodigoAccidente() {
		return codigoAccidente;
	}


	public void setCodigoAccidente(int codigoAccidente) {
		this.codigoAccidente = codigoAccidente;
	}


	public String getConsecutivoFactura() {
		return consecutivoFactura;
	}


	public void setConsecutivoFactura(String consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}


	public String getImprimirFuripsFurpro() {
		return imprimirFuripsFurpro;
	}


	public void setImprimirFuripsFurpro(String imprimirFuripsFurpro) {
		this.imprimirFuripsFurpro = imprimirFuripsFurpro;
	}


	public int getCodigoPaciente() {
		return codigoPaciente;
	}


	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}


	public String getNombresPaciente() {
		return nombresPaciente;
	}


	public void setNombresPaciente(String nombresPaciente) {
		this.nombresPaciente = nombresPaciente;
	}


	public String getTipoIdPaciente() {
		return tipoIdPaciente;
	}


	public void setTipoIdPaciente(String tipoIdPaciente) {
		this.tipoIdPaciente = tipoIdPaciente;
	}


	public String getNumeroIdPaciente() {
		return numeroIdPaciente;
	}


	public void setNumeroIdPaciente(String numeroIdPaciente) {
		this.numeroIdPaciente = numeroIdPaciente;
	}


	public int getCodigoConvenio() {
		return codigoConvenio;
	}


	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}


	public String getNombreConvenio() {
		return nombreConvenio;
	}


	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}


	public String getConsecutivoIngreso() {
		return consecutivoIngreso;
	}


	public void setConsecutivoIngreso(String consecutivoIngreso) {
		this.consecutivoIngreso = consecutivoIngreso;
	}
	
	



	
	
}
