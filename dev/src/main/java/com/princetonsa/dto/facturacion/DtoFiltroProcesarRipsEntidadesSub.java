package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;

import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase utilizada para enviar los datos del proceso rips de entidades subcontratadas
 * @author Fabián Becerra
 */
public class DtoFiltroProcesarRipsEntidadesSub implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Atributo que almacena el archivo CT
	 */
	private transient FormFile archivoCT;
	
	/**
	 * Atributo que almacena el nombre del archivo CT
	 */
	private String nombreArchivoCT;
	
	/**
	 * Atributo que almacena el archivo AF
	 */
	private transient FormFile archivoAF;
	
	/**
	 * Atributo que almacena el nombre del archivo AF
	 */
	private String nombreArchivoAF;
	
	/**
	 * Atributo que almacena el archivo US
	 */
	private transient FormFile archivoUS;
	
	/**
	 * Atributo que almacena el nombre del archivo US
	 */
	private String nombreArchivoUS;
	
	/**
	 * Atributo que almacena el archivo AD
	 */
	private transient FormFile archivoAD;
	
	/**
	 * Atributo que almacena el nombre del archivo AD
	 */
	private String nombreArchivoAD;
	
	/**
	 * Atributo que almacena el archivo AC
	 */
	private transient FormFile archivoAC;
	
	/**
	 * Atributo que almacena el nombre del archivo AC
	 */
	private String nombreArchivoAC;
	
	/**
	 * Atributo que almacena el archivo AP
	 */
	private transient FormFile archivoAP;
	
	/**
	 * Atributo que almacena el nombre del archivo AP
	 */
	private String nombreArchivoAP;
	
	/**
	 * Atributo que almacena el archivo AH
	 */
	//private transient FormFile archivoAH;
	
	/**
	 * Atributo que almacena el nombre del archivo AH
	 */
	//private String nombreArchivoAH;
	
	/**
	 * Atributo que almacena el archivo AU
	 */
	//private transient FormFile archivoAU;
	
	/**
	 * Atributo que almacenArchivoa el nombre del archivo AU
	 */
	//private String nombreArchivoAU;
	
	/**
	 * Atributo que almacena el archivo AM
	 */
	private transient FormFile archivoAM;
	
	/**
	 * Atributo que almacena el nombre del archivo AM
	 */
	private String nombreArchivoAM;
	
	/**
	 * Atributo que almacena el archivo AT
	 */
	private transient FormFile archivoAT;
	
	/**
	 * Atributo que almacena el nombre del archivo AT
	 */
	private String nombreArchivoAT;
	
	
	/**
	 * Atributo que almacena el codigo pk de la entidad subcontratada seleccionada
	 */
	private long codigoPkEntidadSub;
	
	/**
	 * Atributo que almacena el acronimo del tipo de codificación de medicamentos seleccionado
	 */
	private String acronimoCodMedicInsum;
	
	/**
	 * Atributo que almacena tarifario oficial seleccionado para el tipo de codificación en la
	 * información de servicios
	 */
	private int tarifarioSeleccionadoCodServicios;
	
	/**
	 * Atributo que almacena la institucion
	 */
	private int institucion;
	
	/**
	 * Atributo que almacena las finalidades de servicio existentes en el sistema
	 */
	private ArrayList<Integer> finalidadesServicioEnSistema= new ArrayList<Integer>();
	
	/**
	 * Atributo que almacena las finalidades de consulta existentes en el sistema
	 */
	private ArrayList<String> finalidadesConsultaEnSistema= new ArrayList<String>();
	
	/**
	 * Atributo que almacena las causas externas existentes en el sistema
	 */
	private ArrayList<Integer> causasExternasEnSistema= new ArrayList<Integer>();
	
	/**
	 * Atributo que almacena los diagnosticos existentes en el sistema
	 */
	private List<String> diagnosticosEnSistema = new ArrayList<String>();
	
	/** 
	 * Atributo que almacena el usuario en session
	 */
	private UsuarioBasico usuario;

	
	public FormFile getArchivoCT() {
		return archivoCT;
	}

	public void setArchivoCT(FormFile archivoCT) {
		this.archivoCT = archivoCT;
	}

	public FormFile getArchivoAF() {
		return archivoAF;
	}

	public void setArchivoAF(FormFile archivoAF) {
		this.archivoAF = archivoAF;
	}

	public FormFile getArchivoUS() {
		return archivoUS;
	}

	public void setArchivoUS(FormFile archivoUS) {
		this.archivoUS = archivoUS;
	}

	public FormFile getArchivoAD() {
		return archivoAD;
	}

	public void setArchivoAD(FormFile archivoAD) {
		this.archivoAD = archivoAD;
	}

	public FormFile getArchivoAC() {
		return archivoAC;
	}

	public void setArchivoAC(FormFile archivoAC) {
		this.archivoAC = archivoAC;
	}

	public FormFile getArchivoAP() {
		return archivoAP;
	}

	public void setArchivoAP(FormFile archivoAP) {
		this.archivoAP = archivoAP;
	}

	/*public FormFile getArchivoAH() {
		return archivoAH;
	}

	public void setArchivoAH(FormFile archivoAH) {
		this.archivoAH = archivoAH;
	}

	public FormFile getArchivoAU() {
		return archivoAU;
	}

	public void setArchivoAU(FormFile archivoAU) {
		this.archivoAU = archivoAU;
	}*/

	public FormFile getArchivoAM() {
		return archivoAM;
	}

	public void setArchivoAM(FormFile archivoAM) {
		this.archivoAM = archivoAM;
	}

	public FormFile getArchivoAT() {
		return archivoAT;
	}

	public void setArchivoAT(FormFile archivoAT) {
		this.archivoAT = archivoAT;
	}

	public long getCodigoPkEntidadSub() {
		return codigoPkEntidadSub;
	}

	public void setCodigoPkEntidadSub(long codigoPkEntidadSub) {
		this.codigoPkEntidadSub = codigoPkEntidadSub;
	}

	public String getAcronimoCodMedicInsum() {
		return acronimoCodMedicInsum;
	}

	public void setAcronimoCodMedicInsum(String acronimoCodMedicInsum) {
		this.acronimoCodMedicInsum = acronimoCodMedicInsum;
	}


	public void setTarifarioSeleccionadoCodServicios(
			int tarifarioSeleccionadoCodServicios) {
		this.tarifarioSeleccionadoCodServicios = tarifarioSeleccionadoCodServicios;
	}

	public int getTarifarioSeleccionadoCodServicios() {
		return tarifarioSeleccionadoCodServicios;
	}

	public String getNombreArchivoCT() {
		return nombreArchivoCT;
	}

	public void setNombreArchivoCT(String nombreArchivoCT) {
		this.nombreArchivoCT = nombreArchivoCT;
	}

	public String getNombreArchivoAF() {
		return nombreArchivoAF;
	}

	public void setNombreArchivoAF(String nombreArchivoAF) {
		this.nombreArchivoAF = nombreArchivoAF;
	}

	public String getNombreArchivoUS() {
		return nombreArchivoUS;
	}

	public void setNombreArchivoUS(String nombreArchivoUS) {
		this.nombreArchivoUS = nombreArchivoUS;
	}

	public String getNombreArchivoAD() {
		return nombreArchivoAD;
	}

	public void setNombreArchivoAD(String nombreArchivoAD) {
		this.nombreArchivoAD = nombreArchivoAD;
	}

	public String getNombreArchivoAC() {
		return nombreArchivoAC;
	}

	public void setNombreArchivoAC(String nombreArchivoAC) {
		this.nombreArchivoAC = nombreArchivoAC;
	}

	public String getNombreArchivoAP() {
		return nombreArchivoAP;
	}

	public void setNombreArchivoAP(String nombreArchivoAP) {
		this.nombreArchivoAP = nombreArchivoAP;
	}

	/*public String getNombreArchivoAH() {
		return nombreArchivoAH;
	}

	public void setNombreArchivoAH(String nombreArchivoAH) {
		this.nombreArchivoAH = nombreArchivoAH;
	}

	public String getNombreArchivoAU() {
		return nombreArchivoAU;
	}

	public void setNombreArchivoAU(String nombreArchivoAU) {
		this.nombreArchivoAU = nombreArchivoAU;
	}*/

	public String getNombreArchivoAM() {
		return nombreArchivoAM;
	}

	public void setNombreArchivoAM(String nombreArchivoAM) {
		this.nombreArchivoAM = nombreArchivoAM;
	}

	public String getNombreArchivoAT() {
		return nombreArchivoAT;
	}

	public void setNombreArchivoAT(String nombreArchivoAT) {
		this.nombreArchivoAT = nombreArchivoAT;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public int getInstitucion() {
		return institucion;
	}

	public UsuarioBasico getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioBasico usuario) {
		this.usuario = usuario;
	}

	public void setFinalidadesServicioEnSistema(
			ArrayList<Integer> finalidadesServicioEnSistema) {
		this.finalidadesServicioEnSistema = finalidadesServicioEnSistema;
	}

	public ArrayList<Integer> getFinalidadesServicioEnSistema() {
		return finalidadesServicioEnSistema;
	}

	public void setFinalidadesConsultaEnSistema(
			ArrayList<String> finalidadesConsultaEnSistema) {
		this.finalidadesConsultaEnSistema = finalidadesConsultaEnSistema;
	}

	public ArrayList<String> getFinalidadesConsultaEnSistema() {
		return finalidadesConsultaEnSistema;
	}

	public void setCausasExternasEnSistema(ArrayList<Integer> causasExternasEnSistema) {
		this.causasExternasEnSistema = causasExternasEnSistema;
	}

	public ArrayList<Integer> getCausasExternasEnSistema() {
		return causasExternasEnSistema;
	}

	public void setDiagnosticosEnSistema(List<String> diagnosticosEnSistema) {
		this.diagnosticosEnSistema = diagnosticosEnSistema;
	}

	public List<String> getDiagnosticosEnSistema() {
		return diagnosticosEnSistema;
	}
	
	
}
