package com.servinte.axioma.generadorReporte.ordenes.solicitudMedicamentos;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * Dto con los datos de la Solicitud de Medicamentos a imprimir
 * @author hermorhu
 * @created 08-Mar-2013 
 */
public class ImpresionSolicitudMedicamentosDto {

	private String razonSocial;
	private String nit;
	private String direccion;
	private String telefono;
	
	private String ubicacionLogo;
	private String rutaLogo;
	private String logoIzquierda;
	private String logoDerecha;
	
	private String paciente;
	private String documentoIdentificacion;
	private String edad;
	private String telefonoPaciente;
	private String viaIngreso;
	private String cama;
	private String area;
	private String ingreso;
	private String responsable;
	private String centroSolicitante;
	private String farmacia;
	private String fechaHoraOrden;
	private String orden;
	
	private boolean esAnulada;
	private String motivoAnulacion;
	
	private String firmaDigital;
	private String datosMedico;
	
	private String fechaEntrega;
	private String horaEntrega;
	private String usuario;
	
	private List<ImpresionMedicamentoInsumoSolicitadoDto> listaMedicamentosInsumosSolicitados;
	private JRDataSource JRDMedicamentosSolicitados;
	private JRDataSource JRDInsumosSolicitados;
	
	/**
	 * 
	 */
	public ImpresionSolicitudMedicamentosDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * @param razonSocial the razonSocial to set
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * @return the nit
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * @param nit the nit to set
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the logoIzquierda
	 */
	public String getLogoIzquierda() {
		return logoIzquierda;
	}

	/**
	 * @param logoIzquierda the logoIzquierda to set
	 */
	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}

	/**
	 * @return the logoDerecha
	 */
	public String getLogoDerecha() {
		return logoDerecha;
	}

	/**
	 * @param logoDerecha the logoDerecha to set
	 */
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}

	/**
	 * @return the paciente
	 */
	public String getPaciente() {
		return paciente;
	}

	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	/**
	 * @return the documentoIdentificacion
	 */
	public String getDocumentoIdentificacion() {
		return documentoIdentificacion;
	}

	/**
	 * @param documentoIdentificacion the documentoIdentificacion to set
	 */
	public void setDocumentoIdentificacion(String documentoIdentificacion) {
		this.documentoIdentificacion = documentoIdentificacion;
	}

	/**
	 * @return the edad
	 */
	public String getEdad() {
		return edad;
	}

	/**
	 * @param edad the edad to set
	 */
	public void setEdad(String edad) {
		this.edad = edad;
	}

	/**
	 * @return the viaIngreso
	 */
	public String getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return the cama
	 */
	public String getCama() {
		return cama;
	}

	/**
	 * @param cama the cama to set
	 */
	public void setCama(String cama) {
		this.cama = cama;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the responsable
	 */
	public String getResponsable() {
		return responsable;
	}

	/**
	 * @param responsable the responsable to set
	 */
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	/**
	 * @return the centroSolicitante
	 */
	public String getCentroSolicitante() {
		return centroSolicitante;
	}

	/**
	 * @param centroSolicitante the centroSolicitante to set
	 */
	public void setCentroSolicitante(String centroSolicitante) {
		this.centroSolicitante = centroSolicitante;
	}

	/**
	 * @return the farmacia
	 */
	public String getFarmacia() {
		return farmacia;
	}

	/**
	 * @param farmacia the farmacia to set
	 */
	public void setFarmacia(String farmacia) {
		this.farmacia = farmacia;
	}

	/**
	 * @return the fechaHoraOrden
	 */
	public String getFechaHoraOrden() {
		return fechaHoraOrden;
	}

	/**
	 * @param fechaHoraOrden the fechaHoraOrden to set
	 */
	public void setFechaHoraOrden(String fechaHoraOrden) {
		this.fechaHoraOrden = fechaHoraOrden;
	}

	/**
	 * @return the orden
	 */
	public String getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(String orden) {
		this.orden = orden;
	}

	/**
	 * @return the datosMedico
	 */
	public String getDatosMedico() {
		return datosMedico;
	}

	/**
	 * @param datosMedico the datosMedico to set
	 */
	public void setDatosMedico(String datosMedico) {
		this.datosMedico = datosMedico;
	}

	/**
	 * @return the listaMedicamentosInsumosSolicitados
	 */
	public List<ImpresionMedicamentoInsumoSolicitadoDto> getListaMedicamentosInsumosSolicitados() {
		return listaMedicamentosInsumosSolicitados;
	}

	/**
	 * @param listaMedicamentosInsumosSolicitados the listaMedicamentosInsumosSolicitados to set
	 */
	public void setListaMedicamentosInsumosSolicitados(
			List<ImpresionMedicamentoInsumoSolicitadoDto> listaMedicamentosInsumosSolicitados) {
		this.listaMedicamentosInsumosSolicitados = listaMedicamentosInsumosSolicitados;
	}

	/**
	 * @return the JRDMedicamentosSolicitados
	 */
	public JRDataSource getJRDMedicamentosSolicitados() {
		return JRDMedicamentosSolicitados;
	}

	/**
	 * @param JRDMedicamentosSolicitados the JRDMedicamentosSolicitados to set
	 */
	public void setJRDMedicamentosSolicitados(
			JRDataSource JRDMedicamentosSolicitados) {
		this.JRDMedicamentosSolicitados = JRDMedicamentosSolicitados;
	}

	/**
	 * @return the ubicacionLogo
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	/**
	 * @param ubicacionLogo the ubicacionLogo to set
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}

	/**
	 * @return the rutaLogo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}

	/**
	 * @param rutaLogo the rutaLogo to set
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}

	/**
	 * @return the firmaDigital
	 */
	public String getFirmaDigital() {
		return firmaDigital;
	}

	/**
	 * @param firmaDigital the firmaDigital to set
	 */
	public void setFirmaDigital(String firmaDigital) {
		this.firmaDigital = firmaDigital;
	}

	/**
	 * @return the fechaEntrega
	 */
	public String getFechaEntrega() {
		return fechaEntrega;
	}

	/**
	 * @param fechaEntrega the fechaEntrega to set
	 */
	public void setFechaEntrega(String fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	/**
	 * @return the horaEntrega
	 */
	public String getHoraEntrega() {
		return horaEntrega;
	}

	/**
	 * @param horaEntrega the horaEntrega to set
	 */
	public void setHoraEntrega(String horaEntrega) {
		this.horaEntrega = horaEntrega;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the esAnulada
	 */
	public boolean isEsAnulada() {
		return esAnulada;
	}

	/**
	 * @param esAnulada the esAnulada to set
	 */
	public void setEsAnulada(boolean esAnulada) {
		this.esAnulada = esAnulada;
	}

	/**
	 * @return the motivoAnulacion
	 */
	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}

	/**
	 * @param motivoAnulacion the motivoAnulacion to set
	 */
	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}

	/**
	 * @return the telefonoPaciente
	 */
	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}

	/**
	 * @param telefonoPaciente the telefonoPaciente to set
	 */
	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
	}

	/**
	 * @return the ingreso
	 */
	public String getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the jRDInsumosSolicitados
	 */
	public JRDataSource getJRDInsumosSolicitados() {
		return JRDInsumosSolicitados;
	}

	/**
	 * @param jRDInsumosSolicitados the jRDInsumosSolicitados to set
	 */
	public void setJRDInsumosSolicitados(JRDataSource jRDInsumosSolicitados) {
		JRDInsumosSolicitados = jRDInsumosSolicitados;
	}

}
