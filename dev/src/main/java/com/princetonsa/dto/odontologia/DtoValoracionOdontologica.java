package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;

import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.mundo.atencion.Diagnostico;

public class DtoValoracionOdontologica extends DtoValoracion implements Serializable{

	/**
	 * Serial versionUID
	 */
	private static final long serialVersionUID = 1L;

	private int codigo;
	
	private Boolean estadoLlegada;
	private String descripcionEstadoLlegada;
	private InfoDatosString autorizador;
	private InfoDatosString autorizadorAnterior;
	private InfoDatosInt estadoConcienciaLlegada;
	private String descripcionEstadoConcienciaLlegada;
	private InfoDatosInt estadoConciencia;
	private String descripcionEstadoConciencia;
	private InfoDatosInt conductaValoracion;
	private InfoDatosInt conductaValoracionAnterior;
	private String descripcionConductaValoracion;
	private InfoDatosInt categoriaTriage;
	private String descripcionCategoriaTriage;
	private Boolean estadoEmbriaguez;
	private InfoDatosString informacionTriage;
	private String especialidadResponde;
	private String nomEspecialidadResponde;
	
	//Atributos cuando se da salida paciente si observacion o remitir
	private String estadoSalida;
	private Diagnostico diagnosticoMuerte;
	private String fechaMuerte;
	private String horaMuerte;
	private String certificadoDefuncion;
	
	//Atributos cuando se da Traslado cuidado especial
	private InfoDatosInt tipoMonitoreo;
	
	private String fechaModifica;
	private String horaModifica;
	
	/**
	 * Contiene el histórico las conductas valoración
	 */
	private StringBuffer historicoConductasValoracion;
	
	/**
	 * Método que incializa los datos
	 */
	public void clean()
	{
		super.clean();
		this.estadoLlegada = null;
		this.descripcionEstadoLlegada = "";
		this.autorizador = new InfoDatosString("","");
		this.autorizadorAnterior = new InfoDatosString("","");
		this.estadoConcienciaLlegada = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.descripcionEstadoConcienciaLlegada = "";
		this.estadoConciencia = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.descripcionEstadoConciencia = "";
		this.conductaValoracion = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.conductaValoracionAnterior = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.descripcionConductaValoracion = "";
		this.categoriaTriage = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.descripcionCategoriaTriage = "";
		this.estadoEmbriaguez = null;
		this.informacionTriage = new InfoDatosString();
		this.especialidadResponde = "";
		this.nomEspecialidadResponde="";
		
		//Atributos cuando se da salid apaciente sin observacion o remitir
		this.estadoSalida = "";
		this.diagnosticoMuerte = new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado,ConstantesBD.codigoCieDiagnosticoNoSeleccionado);
		this.fechaMuerte = "";
		this.horaMuerte = "";
		this.certificadoDefuncion = "";
		//Atributos cuando se da Traslado cuidado especial
		this.tipoMonitoreo = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		
		this.historicoConductasValoracion = new StringBuffer();
		
		this.fechaModifica = "";
		this.horaModifica = "";
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
	 * @return the estadoLlegada
	 */
	public Boolean getEstadoLlegada() {
		return estadoLlegada;
	}

	/**
	 * @param estadoLlegada the estadoLlegada to set
	 */
	public void setEstadoLlegada(Boolean estadoLlegada) {
		this.estadoLlegada = estadoLlegada;
	}

	/**
	 * @return the descripcionEstadoLlegada
	 */
	public String getDescripcionEstadoLlegada() {
		return descripcionEstadoLlegada;
	}

	/**
	 * @param descripcionEstadoLlegada the descripcionEstadoLlegada to set
	 */
	public void setDescripcionEstadoLlegada(String descripcionEstadoLlegada) {
		this.descripcionEstadoLlegada = descripcionEstadoLlegada;
	}

	/**
	 * @return the autorizador
	 */
	public InfoDatosString getAutorizador() {
		return autorizador;
	}

	/**
	 * @param autorizador the autorizador to set
	 */
	public void setAutorizador(InfoDatosString autorizador) {
		this.autorizador = autorizador;
	}

	/**
	 * @return the autorizadorAnterior
	 */
	public InfoDatosString getAutorizadorAnterior() {
		return autorizadorAnterior;
	}

	/**
	 * @param autorizadorAnterior the autorizadorAnterior to set
	 */
	public void setAutorizadorAnterior(InfoDatosString autorizadorAnterior) {
		this.autorizadorAnterior = autorizadorAnterior;
	}

	/**
	 * @return the estadoConcienciaLlegada
	 */
	public InfoDatosInt getEstadoConcienciaLlegada() {
		return estadoConcienciaLlegada;
	}

	/**
	 * @param estadoConcienciaLlegada the estadoConcienciaLlegada to set
	 */
	public void setEstadoConcienciaLlegada(InfoDatosInt estadoConcienciaLlegada) {
		this.estadoConcienciaLlegada = estadoConcienciaLlegada;
	}

	/**
	 * @return the descripcionEstadoConcienciaLlegada
	 */
	public String getDescripcionEstadoConcienciaLlegada() {
		return descripcionEstadoConcienciaLlegada;
	}

	/**
	 * @param descripcionEstadoConcienciaLlegada the descripcionEstadoConcienciaLlegada to set
	 */
	public void setDescripcionEstadoConcienciaLlegada(
			String descripcionEstadoConcienciaLlegada) {
		this.descripcionEstadoConcienciaLlegada = descripcionEstadoConcienciaLlegada;
	}

	/**
	 * @return the estadoConciencia
	 */
	public InfoDatosInt getEstadoConciencia() {
		return estadoConciencia;
	}

	/**
	 * @param estadoConciencia the estadoConciencia to set
	 */
	public void setEstadoConciencia(InfoDatosInt estadoConciencia) {
		this.estadoConciencia = estadoConciencia;
	}

	/**
	 * @return the descripcionEstadoConciencia
	 */
	public String getDescripcionEstadoConciencia() {
		return descripcionEstadoConciencia;
	}

	/**
	 * @param descripcionEstadoConciencia the descripcionEstadoConciencia to set
	 */
	public void setDescripcionEstadoConciencia(String descripcionEstadoConciencia) {
		this.descripcionEstadoConciencia = descripcionEstadoConciencia;
	}

	/**
	 * @return the conductaValoracion
	 */
	public InfoDatosInt getConductaValoracion() {
		return conductaValoracion;
	}

	/**
	 * @param conductaValoracion the conductaValoracion to set
	 */
	public void setConductaValoracion(InfoDatosInt conductaValoracion) {
		this.conductaValoracion = conductaValoracion;
	}

	/**
	 * @return the conductaValoracionAnterior
	 */
	public InfoDatosInt getConductaValoracionAnterior() {
		return conductaValoracionAnterior;
	}

	/**
	 * @param conductaValoracionAnterior the conductaValoracionAnterior to set
	 */
	public void setConductaValoracionAnterior(
			InfoDatosInt conductaValoracionAnterior) {
		this.conductaValoracionAnterior = conductaValoracionAnterior;
	}

	/**
	 * @return the descripcionConductaValoracion
	 */
	public String getDescripcionConductaValoracion() {
		return descripcionConductaValoracion;
	}

	/**
	 * @param descripcionConductaValoracion the descripcionConductaValoracion to set
	 */
	public void setDescripcionConductaValoracion(
			String descripcionConductaValoracion) {
		this.descripcionConductaValoracion = descripcionConductaValoracion;
	}

	/**
	 * @return the categoriaTriage
	 */
	public InfoDatosInt getCategoriaTriage() {
		return categoriaTriage;
	}

	/**
	 * @param categoriaTriage the categoriaTriage to set
	 */
	public void setCategoriaTriage(InfoDatosInt categoriaTriage) {
		this.categoriaTriage = categoriaTriage;
	}

	/**
	 * @return the descripcionCategoriaTriage
	 */
	public String getDescripcionCategoriaTriage() {
		return descripcionCategoriaTriage;
	}

	/**
	 * @param descripcionCategoriaTriage the descripcionCategoriaTriage to set
	 */
	public void setDescripcionCategoriaTriage(String descripcionCategoriaTriage) {
		this.descripcionCategoriaTriage = descripcionCategoriaTriage;
	}

	/**
	 * @return the estadoEmbriaguez
	 */
	public Boolean getEstadoEmbriaguez() {
		return estadoEmbriaguez;
	}

	/**
	 * @param estadoEmbriaguez the estadoEmbriaguez to set
	 */
	public void setEstadoEmbriaguez(Boolean estadoEmbriaguez) {
		this.estadoEmbriaguez = estadoEmbriaguez;
	}

	/**
	 * @return the informacionTriage
	 */
	public InfoDatosString getInformacionTriage() {
		return informacionTriage;
	}

	/**
	 * @param informacionTriage the informacionTriage to set
	 */
	public void setInformacionTriage(InfoDatosString informacionTriage) {
		this.informacionTriage = informacionTriage;
	}

	/**
	 * @return the especialidadResponde
	 */
	public String getEspecialidadResponde() {
		return especialidadResponde;
	}

	/**
	 * @param especialidadResponde the especialidadResponde to set
	 */
	public void setEspecialidadResponde(String especialidadResponde) {
		this.especialidadResponde = especialidadResponde;
	}

	/**
	 * @return the nomEspecialidadResponde
	 */
	public String getNomEspecialidadResponde() {
		return nomEspecialidadResponde;
	}

	/**
	 * @param nomEspecialidadResponde the nomEspecialidadResponde to set
	 */
	public void setNomEspecialidadResponde(String nomEspecialidadResponde) {
		this.nomEspecialidadResponde = nomEspecialidadResponde;
	}

	/**
	 * @return the estadoSalida
	 */
	public String getEstadoSalida() {
		return estadoSalida;
	}

	/**
	 * @param estadoSalida the estadoSalida to set
	 */
	public void setEstadoSalida(String estadoSalida) {
		this.estadoSalida = estadoSalida;
	}

	/**
	 * @return the diagnosticoMuerte
	 */
	public Diagnostico getDiagnosticoMuerte() {
		return diagnosticoMuerte;
	}

	/**
	 * @param diagnosticoMuerte the diagnosticoMuerte to set
	 */
	public void setDiagnosticoMuerte(Diagnostico diagnosticoMuerte) {
		this.diagnosticoMuerte = diagnosticoMuerte;
	}

	/**
	 * @return the fechaMuerte
	 */
	public String getFechaMuerte() {
		return fechaMuerte;
	}

	/**
	 * @param fechaMuerte the fechaMuerte to set
	 */
	public void setFechaMuerte(String fechaMuerte) {
		this.fechaMuerte = fechaMuerte;
	}

	/**
	 * @return the horaMuerte
	 */
	public String getHoraMuerte() {
		return horaMuerte;
	}

	/**
	 * @param horaMuerte the horaMuerte to set
	 */
	public void setHoraMuerte(String horaMuerte) {
		this.horaMuerte = horaMuerte;
	}

	/**
	 * @return the certificadoDefuncion
	 */
	public String getCertificadoDefuncion() {
		return certificadoDefuncion;
	}

	/**
	 * @param certificadoDefuncion the certificadoDefuncion to set
	 */
	public void setCertificadoDefuncion(String certificadoDefuncion) {
		this.certificadoDefuncion = certificadoDefuncion;
	}

	/**
	 * @return the tipoMonitoreo
	 */
	public InfoDatosInt getTipoMonitoreo() {
		return tipoMonitoreo;
	}

	/**
	 * @param tipoMonitoreo the tipoMonitoreo to set
	 */
	public void setTipoMonitoreo(InfoDatosInt tipoMonitoreo) {
		this.tipoMonitoreo = tipoMonitoreo;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
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
	 * @return the historicoConductasValoracion
	 */
	public StringBuffer getHistoricoConductasValoracion() {
		return historicoConductasValoracion;
	}

	/**
	 * @param historicoConductasValoracion the historicoConductasValoracion to set
	 */
	public void setHistoricoConductasValoracion(
			StringBuffer historicoConductasValoracion) {
		this.historicoConductasValoracion = historicoConductasValoracion;
	}

}
