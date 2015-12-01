/*
 * Abr 24, 2008
 */
package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;

import com.princetonsa.mundo.atencion.Diagnostico;

/**
 * Data Transfer Object: Valoración Urgencias (extensión del DtoValoracion)
 * @author Sebastián Gómez R.
 *
 */
public class DtoValoracionUrgencias extends DtoValoracion implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(DtoValoracionUrgencias.class);
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
	 * Constructor del dto
	 *
	 */
	public DtoValoracionUrgencias()
	{
		
		this.clean();
	}

	/**
	 * @return the autorizador
	 */
	@SuppressWarnings("deprecation")
	public String getNombreAutorizador() {
		return autorizador.getCodigo();
	}

	/**
	 * @param autorizador the autorizador to set
	 */
	public void setNombreAutorizador(String autorizador) {
		this.autorizador.setCodigo(autorizador);
	}
	
	/**
	 * @return the autorizador
	 */
	public String getRelacionAutorizador() {
		return autorizador.getDescripcion();
	}

	/**
	 * @param autorizador the autorizador to set
	 */
	public void setRelacionAutorizador(String autorizador) {
		this.autorizador.setDescripcion(autorizador);
	}
	
	
	/**
	 * @return the autorizador
	 */
	@SuppressWarnings("deprecation")
	public String getNombreAutorizadorAnterior() {
		return autorizadorAnterior.getCodigo();
	}

	/**
	 * @param autorizador the autorizador to set
	 */
	public void setNombreAutorizadorAnterior(String autorizador) {
		this.autorizadorAnterior.setCodigo(autorizador);
	}
	
	/**
	 * @return the autorizador
	 */
	public String getRelacionAutorizadorAnterior() {
		return autorizadorAnterior.getDescripcion();
	}

	/**
	 * @param autorizador the autorizador to set
	 */
	public void setRelacionAutorizadorAnterior(String autorizador) {
		this.autorizadorAnterior.setDescripcion(autorizador);
	}

	/**
	 * @return the categoriaTriage
	 */
	public int getCodigoCategoriaTriage() {
		return categoriaTriage.getCodigo();
	}

	/**
	 * @param categoriaTriage the categoriaTriage to set
	 */
	public void setCodigoCategoriaTriage(int categoriaTriage) {
		this.categoriaTriage.setCodigo(categoriaTriage);
	}
	
	/**
	 * @return the categoriaTriage
	 */
	public String getNombreCategoriaTriage() {
		return categoriaTriage.getNombre();
	}

	/**
	 * @param categoriaTriage the categoriaTriage to set
	 */
	public void setNombreCategoriaTriage(String categoriaTriage) {
		this.categoriaTriage.setNombre(categoriaTriage);
	}

	/**
	 * @return the conductaValoracion
	 */
	public int getCodigoConductaValoracion() {
		return conductaValoracion.getCodigo();
	}

	/**
	 * @param conductaValoracion the conductaValoracion to set
	 */
	public void setCodigoConductaValoracion(int conductaValoracion) {
		this.conductaValoracion.setCodigo(conductaValoracion);
	}
	
	/**
	 * @return the conductaValoracion
	 */
	public String getNombreConductaValoracion() {
		return conductaValoracion.getNombre();
	}

	/**
	 * @param conductaValoracion the conductaValoracion to set
	 */
	public void setNombreConductaValoracion(String conductaValoracion) {
		this.conductaValoracion.setNombre(conductaValoracion);
	}
	
	/**
	 * @return the conductaValoracion
	 */
	public int getCodigoConductaValoracionAnterior() {
		return conductaValoracionAnterior.getCodigo();
	}

	/**
	 * @param conductaValoracion the conductaValoracion to set
	 */
	public void setCodigoConductaValoracionAnterior(int conductaValoracion) {
		this.conductaValoracionAnterior.setCodigo(conductaValoracion);
	}
	
	/**
	 * @return the conductaValoracion
	 */
	public String getNombreConductaValoracionAnterior() {
		return conductaValoracionAnterior.getNombre();
	}

	/**
	 * @param conductaValoracion the conductaValoracion to set
	 */
	public void setNombreConductaValoracionAnterior(String conductaValoracion) {
		this.conductaValoracionAnterior.setNombre(conductaValoracion);
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
	 * @return the estadoConciencia
	 */
	public int getCodigoEstadoConciencia() {
		return estadoConciencia.getCodigo();
	}

	/**
	 * @param estadoConciencia the estadoConciencia to set
	 */
	public void setCodigoEstadoConciencia(int estadoConciencia) {
		this.estadoConciencia.setCodigo(estadoConciencia);
	}
	
	/**
	 * @return the estadoConciencia
	 */
	public String getNombreEstadoConciencia() {
		return estadoConciencia.getNombre();
	}

	/**
	 * @param estadoConciencia the estadoConciencia to set
	 */
	public void setNombreEstadoConciencia(String estadoConciencia) {
		this.estadoConciencia.setNombre(estadoConciencia);
	}

	/**
	 * @return the estadoConcienciaLlegada
	 */
	public int getCodigoEstadoConcienciaLlegada() {
		return estadoConcienciaLlegada.getCodigo();
	}

	/**
	 * @param estadoConcienciaLlegada the estadoConcienciaLlegada to set
	 */
	public void setCodigoEstadoConcienciaLlegada(int estadoConcienciaLlegada) {
		this.estadoConcienciaLlegada.setCodigo(estadoConcienciaLlegada);
	}
	
	/**
	 * @return the estadoConcienciaLlegada
	 */
	public String getNombreEstadoConcienciaLlegada() {
		return estadoConcienciaLlegada.getNombre();
	}

	/**
	 * @param estadoConcienciaLlegada the estadoConcienciaLlegada to set
	 */
	public void setNombreEstadoConcienciaLlegada(String estadoConcienciaLlegada) {
		this.estadoConcienciaLlegada.setNombre(estadoConcienciaLlegada);
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
	 * @return the informacionTriage
	 */
	public String getClasificacionTriage() {
		return informacionTriage.getNombre();
	}

	/**
	 * @param informacionTriage the informacionTriage to set
	 */
	public void setClasificacionTriage(String informacionTriage) {
		this.informacionTriage.setNombre(informacionTriage);
	}
	
	/**
	 * @return the informacionTriage
	 */
	@SuppressWarnings("deprecation")
	public String getColorClasificacionTriage() {
		return informacionTriage.getCodigo();
	}

	/**
	 * @param informacionTriage the informacionTriage to set
	 */
	public void setColorClasificacionTriage(String informacionTriage) {
		this.informacionTriage.setCodigo(informacionTriage);
	}
	
	/**
	 * @return the informacionTriage
	 */
	public String getObservacionesClasificacionTriage() {
		return informacionTriage.getDescripcion();
	}

	/**
	 * @param informacionTriage the informacionTriage to set
	 */
	public void setObservacionesClasificacionTriage(String informacionTriage) {
		this.informacionTriage.setDescripcion(informacionTriage);
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
	 * @return the tipoMonitoreo
	 */
	public int getCodigoTipoMonitoreo() {
		return tipoMonitoreo.getCodigo();
	}

	/**
	 * @param tipoMonitoreo the tipoMonitoreo to set
	 */
	public void setCodigoTipoMonitoreo(int tipoMonitoreo) {
		this.tipoMonitoreo.setCodigo(tipoMonitoreo);
	}
	
	/**
	 * @return the tipoMonitoreo
	 */
	public String getNombreTipoMonitoreo() {
		return tipoMonitoreo.getNombre();
	}

	/**
	 * @param tipoMonitoreo the tipoMonitoreo to set
	 */
	public void setNombreTipoMonitoreo(String tipoMonitoreo) {
		this.tipoMonitoreo.setNombre(tipoMonitoreo);
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
	
	//***********METODOS USADOS EN LA JSP PARA VERIFICAR SI SE DEBE MOSTRAR LA INFORMACION DE LA VALROACION*******************
	/**
	 * Método para verificar si existe informacion de examen fisico
	 * @return
	 */
	public boolean isExisteInformacionExamenFisico()
	{
		boolean existe = false;
		
		if(this.isExisteInformacionEstadoConciencia()||!this.getClasificacionTriage().trim().equals(""))
			existe = true;
		
		return existe;
	}
	
	/**
	 * Método para verifica si se ingresó informacion del estado de conciencia
	 * @return
	 */
	public boolean isExisteInformacionEstadoConciencia()
	{
		if(this.getCodigoEstadoConciencia()>0||!this.getDescripcionEstadoConciencia().trim().equals(""))
			return true;
		else
			return false;
	}
	//**********************************************************************************************************************

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
		logger.info("Asigna valor: "+especialidadResponde);
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



	public InfoDatosString getAutorizadorAnterior() {
		return autorizadorAnterior;
	}



	public void setAutorizadorAnterior(InfoDatosString autorizadorAnterior) {
		this.autorizadorAnterior = autorizadorAnterior;
	}



	public InfoDatosInt getConductaValoracionAnterior() {
		return conductaValoracionAnterior;
	}



	public void setConductaValoracionAnterior(
			InfoDatosInt conductaValoracionAnterior) {
		this.conductaValoracionAnterior = conductaValoracionAnterior;
	}



	public InfoDatosInt getTipoMonitoreo() {
		return tipoMonitoreo;
	}



	public void setTipoMonitoreo(InfoDatosInt tipoMonitoreo) {
		this.tipoMonitoreo = tipoMonitoreo;
	}

	
}
