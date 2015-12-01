package util.manejoPaciente;

import java.io.Serializable;

/**
 * 
 * @author wilson
 *
 */
public class RutasArchivosFURIPS implements Serializable 
{
	/**
	 * 
	 */
	private String rutaGeneral;
	
	/**
	 * 
	 */
	private String rutaFURIPS1;
	
	/**
	 * 
	 */
	private String nombreArchivoFURIPS1;
	
	/**
	 * 
	 */
	private boolean existeFURIPS1;
	
	/**
	 * 
	 */
	private String nombreArchivoInconsistenciasFURIPS1;
	
	/**
	 * 
	 */
	private boolean creoArchivoInconsistenciasFURIPS1;
	
	/**
	 * 
	 */
	private String rutaZipUpload;
	
	/**
	 * 
	 */
	private StringBuffer stringBufferFURIPS1;
	
	/**
	 * 
	 */
	private StringBuffer stringBufferInconsistenciasFURIPS1;
	
	/**
	 * 
	 */
	private String rutaFURIPS2;
	
	/**
	 * 
	 */
	private String nombreArchivoFURIPS2;
	
	/**
	 * 
	 */
	private boolean existeFURIPS2;
	
	
	/**
	 * 
	 */
	private String nombreArchivoInconsistenciasFURIPS2;
	
	/**
	 * 
	 */
	private boolean creoArchivoInconsistenciasFURIPS2;
	
	/**
	 * 
	 */
	private StringBuffer stringBufferFURIPS2;
	
	/**
	 * 
	 */
	private StringBuffer stringBufferInconsistenciasFURIPS2;
	
	/**
	 * 
	 */
	private String rutaFURTRAN;
	
	/**
	 * 
	 */
	private String nombreArchivoFURTRAN;
	
	/**
	 * 
	 */
	private boolean existeFURTRAN;
	
	/**
	 * 
	 */
	private String nombreArchivoInconsistenciasFURTRAN;
	
	/**
	 * 
	 */
	private boolean creoArchivoInconsistenciasFURTRAN;
	
	/**
	 * 
	 */
	private StringBuffer stringBufferFURTRAN;
	
	/**
	 * 
	 */
	private StringBuffer stringBufferInconsistenciasFURTRAN;
	
	/**
	 * 
	 */
	private String rutaFURPRO;
	
	/**
	 * 
	 */
	private String nombreArchivoFURPRO;
	
	/**
	 * 
	 */
	private boolean existeFURPRO;
	
	/**
	 * 
	 */
	private String nombreArchivoInconsistenciasFURPRO;
	
	/**
	 * 
	 */
	private boolean creoArchivoInconsistenciasFURPRO;
	
	/**
	 * 
	 */
	private StringBuffer stringBufferFURPRO;
	
	/**
	 * 
	 */
	private StringBuffer stringBufferInconsistenciasFURPRO;
	
	
	/**
	 * 
	 */
	private boolean procesoExitoso;
	
	/**
	 * 
	 */
	public RutasArchivosFURIPS() 
	{
		this.existeFURIPS1 = false;
		this.existeFURIPS2 = false;
		this.existeFURPRO = false;
		this.existeFURTRAN = false;
		this.rutaFURIPS1 = "";
		this.rutaFURIPS2 = "";
		this.rutaFURPRO = "";
		this.nombreArchivoInconsistenciasFURPRO = "";
		this.rutaFURTRAN = "";
		this.nombreArchivoInconsistenciasFURTRAN = "";
		this.rutaGeneral = "";
		this.nombreArchivoInconsistenciasFURIPS1 = "";
		this.nombreArchivoInconsistenciasFURIPS2 = "";
		
		this.nombreArchivoFURIPS1="";
		this.nombreArchivoFURIPS2="";
		this.nombreArchivoFURPRO="";
		this.nombreArchivoFURTRAN="";
		
		this.creoArchivoInconsistenciasFURIPS1=false;
		this.creoArchivoInconsistenciasFURIPS2=false;
		this.creoArchivoInconsistenciasFURPRO=false;
		this.creoArchivoInconsistenciasFURTRAN=false;
		
		this.rutaZipUpload="";
		
		this.stringBufferFURIPS1=new StringBuffer();
		this.stringBufferFURIPS2=new StringBuffer();
		this.stringBufferFURPRO=new StringBuffer();
		this.stringBufferFURTRAN= new StringBuffer();
		this.stringBufferInconsistenciasFURIPS1= new StringBuffer();
		this.stringBufferInconsistenciasFURIPS2= new StringBuffer();
		this.stringBufferInconsistenciasFURPRO= new StringBuffer();
		this.stringBufferInconsistenciasFURTRAN= new StringBuffer();
		
		this.procesoExitoso=false;
	}

	/**
	 * @return the rutaGeneral
	 */
	public String getRutaGeneral() {
		return rutaGeneral;
	}

	/**
	 * @param rutaGeneral the rutaGeneral to set
	 */
	public void setRutaGeneral(String rutaGeneral) {
		this.rutaGeneral = rutaGeneral;
	}

	/**
	 * @return the rutaFURIPS1
	 */
	public String getRutaFURIPS1() {
		return rutaFURIPS1;
	}

	/**
	 * @param rutaFURIPS1 the rutaFURIPS1 to set
	 */
	public void setRutaFURIPS1(String rutaFURIPS1) {
		this.rutaFURIPS1 = rutaFURIPS1;
	}

	/**
	 * @return the stringBufferFURIPS1
	 */
	public StringBuffer getStringBufferFURIPS1() {
		return stringBufferFURIPS1;
	}

	/**
	 * @param stringBufferFURIPS1 the stringBufferFURIPS1 to set
	 */
	public void setStringBufferFURIPS1(StringBuffer stringBufferFURIPS1) {
		this.stringBufferFURIPS1 = stringBufferFURIPS1;
	}

	/**
	 * @return the stringBufferInconsistenciasFURIPS1
	 */
	public StringBuffer getStringBufferInconsistenciasFURIPS1() {
		return stringBufferInconsistenciasFURIPS1;
	}

	/**
	 * @param stringBufferInconsistenciasFURIPS1 the stringBufferInconsistenciasFURIPS1 to set
	 */
	public void setStringBufferInconsistenciasFURIPS1(
			StringBuffer stringBufferInconsistenciasFURIPS1) {
		this.stringBufferInconsistenciasFURIPS1 = stringBufferInconsistenciasFURIPS1;
	}

	/**
	 * @return the stringBufferFURIPS2
	 */
	public StringBuffer getStringBufferFURIPS2() {
		return stringBufferFURIPS2;
	}

	/**
	 * @param stringBufferFURIPS2 the stringBufferFURIPS2 to set
	 */
	public void setStringBufferFURIPS2(StringBuffer stringBufferFURIPS2) {
		this.stringBufferFURIPS2 = stringBufferFURIPS2;
	}

	/**
	 * @return the stringBufferInconsistenciasFURIPS2
	 */
	public StringBuffer getStringBufferInconsistenciasFURIPS2() {
		return stringBufferInconsistenciasFURIPS2;
	}

	/**
	 * @param stringBufferInconsistenciasFURIPS2 the stringBufferInconsistenciasFURIPS2 to set
	 */
	public void setStringBufferInconsistenciasFURIPS2(
			StringBuffer stringBufferInconsistenciasFURIPS2) {
		this.stringBufferInconsistenciasFURIPS2 = stringBufferInconsistenciasFURIPS2;
	}

	/**
	 * @return the stringBufferFURTRAN
	 */
	public StringBuffer getStringBufferFURTRAN() {
		return stringBufferFURTRAN;
	}

	/**
	 * @param stringBufferFURTRAN the stringBufferFURTRAN to set
	 */
	public void setStringBufferFURTRAN(StringBuffer stringBufferFURTRAN) {
		this.stringBufferFURTRAN = stringBufferFURTRAN;
	}

	/**
	 * @return the stringBufferInconsistenciasFURTRAN
	 */
	public StringBuffer getStringBufferInconsistenciasFURTRAN() {
		return stringBufferInconsistenciasFURTRAN;
	}

	/**
	 * @param stringBufferInconsistenciasFURTRAN the stringBufferInconsistenciasFURTRAN to set
	 */
	public void setStringBufferInconsistenciasFURTRAN(
			StringBuffer stringBufferInconsistenciasFURTRAN) {
		this.stringBufferInconsistenciasFURTRAN = stringBufferInconsistenciasFURTRAN;
	}

	/**
	 * @return the stringBufferFURPRO
	 */
	public StringBuffer getStringBufferFURPRO() {
		return stringBufferFURPRO;
	}

	/**
	 * @param stringBufferFURPRO the stringBufferFURPRO to set
	 */
	public void setStringBufferFURPRO(StringBuffer stringBufferFURPRO) {
		this.stringBufferFURPRO = stringBufferFURPRO;
	}

	/**
	 * @return the stringBufferInconsistenciasFURPRO
	 */
	public StringBuffer getStringBufferInconsistenciasFURPRO() {
		return stringBufferInconsistenciasFURPRO;
	}

	/**
	 * @param stringBufferInconsistenciasFURPRO the stringBufferInconsistenciasFURPRO to set
	 */
	public void setStringBufferInconsistenciasFURPRO(
			StringBuffer stringBufferInconsistenciasFURPRO) {
		this.stringBufferInconsistenciasFURPRO = stringBufferInconsistenciasFURPRO;
	}

	/**
	 * @return the existeFURIPS1
	 */
	public boolean isExisteFURIPS1() {
		return existeFURIPS1;
	}

	/**
	 * @param existeFURIPS1 the existeFURIPS1 to set
	 */
	public void setExisteFURIPS1(boolean existeFURIPS1) {
		this.existeFURIPS1 = existeFURIPS1;
	}

	/**
	 * @return the rutaFURIPS2
	 */
	public String getRutaFURIPS2() {
		return rutaFURIPS2;
	}

	/**
	 * @param rutaFURIPS2 the rutaFURIPS2 to set
	 */
	public void setRutaFURIPS2(String rutaFURIPS2) {
		this.rutaFURIPS2 = rutaFURIPS2;
	}

	/**
	 * @return the existeFURIPS2
	 */
	public boolean isExisteFURIPS2() {
		return existeFURIPS2;
	}

	/**
	 * @param existeFURIPS2 the existeFURIPS2 to set
	 */
	public void setExisteFURIPS2(boolean existeFURIPS2) {
		this.existeFURIPS2 = existeFURIPS2;
	}

	/**
	 * @return the rutaFURTRAN
	 */
	public String getRutaFURTRAN() {
		return rutaFURTRAN;
	}

	/**
	 * @param rutaFURTRAN the rutaFURTRAN to set
	 */
	public void setRutaFURTRAN(String rutaFURTRAN) {
		this.rutaFURTRAN = rutaFURTRAN;
	}

	/**
	 * @return the existeFURTRAN
	 */
	public boolean isExisteFURTRAN() {
		return existeFURTRAN;
	}

	/**
	 * @param existeFURTRAN the existeFURTRAN to set
	 */
	public void setExisteFURTRAN(boolean existeFURTRAN) {
		this.existeFURTRAN = existeFURTRAN;
	}

	/**
	 * @return the rutaFURPRO
	 */
	public String getRutaFURPRO() {
		return rutaFURPRO;
	}

	/**
	 * @param rutaFURPRO the rutaFURPRO to set
	 */
	public void setRutaFURPRO(String rutaFURPRO) {
		this.rutaFURPRO = rutaFURPRO;
	}

	/**
	 * @return the existeFURPRO
	 */
	public boolean isExisteFURPRO() {
		return existeFURPRO;
	}

	/**
	 * @param existeFURPRO the existeFURPRO to set
	 */
	public void setExisteFURPRO(boolean existeFURPRO) {
		this.existeFURPRO = existeFURPRO;
	}

	/**
	 * @return the nombreArchivoFURIPS1
	 */
	public String getNombreArchivoFURIPS1() {
		return nombreArchivoFURIPS1.trim().toUpperCase();
	}

	/**
	 * @param nombreArchivoFURIPS1 the nombreArchivoFURIPS1 to set
	 */
	public void setNombreArchivoFURIPS1(String nombreArchivoFURIPS1) {
		this.nombreArchivoFURIPS1 = nombreArchivoFURIPS1;
	}

	/**
	 * @return the nombreArchivoFURIPS2
	 */
	public String getNombreArchivoFURIPS2() {
		return nombreArchivoFURIPS2.trim().toUpperCase();
	}

	/**
	 * @param nombreArchivoFURIPS2 the nombreArchivoFURIPS2 to set
	 */
	public void setNombreArchivoFURIPS2(String nombreArchivoFURIPS2) {
		this.nombreArchivoFURIPS2 = nombreArchivoFURIPS2;
	}

	/**
	 * @return the nombreArchivoFURTRAN
	 */
	public String getNombreArchivoFURTRAN() {
		return nombreArchivoFURTRAN.trim().toUpperCase();
	}

	/**
	 * @param nombreArchivoFURTRAN the nombreArchivoFURTRAN to set
	 */
	public void setNombreArchivoFURTRAN(String nombreArchivoFURTRAN) {
		this.nombreArchivoFURTRAN = nombreArchivoFURTRAN;
	}

	/**
	 * @return the nombreArchivoFURPRO
	 */
	public String getNombreArchivoFURPRO() {
		return nombreArchivoFURPRO.trim().toUpperCase();
	}

	/**
	 * @param nombreArchivoFURPRO the nombreArchivoFURPRO to set
	 */
	public void setNombreArchivoFURPRO(String nombreArchivoFURPRO) {
		this.nombreArchivoFURPRO = nombreArchivoFURPRO;
	}

	/**
	 * @return the nombreArchivoInconsistenciasFURIPS1
	 */
	public String getNombreArchivoInconsistenciasFURIPS1() {
		return nombreArchivoInconsistenciasFURIPS1.trim().toUpperCase();
	}

	/**
	 * @param nombreArchivoInconsistenciasFURIPS1 the nombreArchivoInconsistenciasFURIPS1 to set
	 */
	public void setNombreArchivoInconsistenciasFURIPS1(
			String nombreArchivoInconsistenciasFURIPS1) {
		this.nombreArchivoInconsistenciasFURIPS1 = nombreArchivoInconsistenciasFURIPS1;
	}

	/**
	 * @return the nombreArchivoInconsistenciasFURIPS2
	 */
	public String getNombreArchivoInconsistenciasFURIPS2() {
		return nombreArchivoInconsistenciasFURIPS2.trim().toUpperCase();
	}

	/**
	 * @param nombreArchivoInconsistenciasFURIPS2 the nombreArchivoInconsistenciasFURIPS2 to set
	 */
	public void setNombreArchivoInconsistenciasFURIPS2(
			String nombreArchivoInconsistenciasFURIPS2) {
		this.nombreArchivoInconsistenciasFURIPS2 = nombreArchivoInconsistenciasFURIPS2;
	}

	/**
	 * @return the nombreArchivoInconsistenciasFURTRAN
	 */
	public String getNombreArchivoInconsistenciasFURTRAN() {
		return nombreArchivoInconsistenciasFURTRAN.trim().toUpperCase();
	}

	/**
	 * @param nombreArchivoInconsistenciasFURTRAN the nombreArchivoInconsistenciasFURTRAN to set
	 */
	public void setNombreArchivoInconsistenciasFURTRAN(
			String nombreArchivoInconsistenciasFURTRAN) {
		this.nombreArchivoInconsistenciasFURTRAN = nombreArchivoInconsistenciasFURTRAN;
	}

	/**
	 * @return the nombreArchivoInconsistenciasFURPRO
	 */
	public String getNombreArchivoInconsistenciasFURPRO() {
		return nombreArchivoInconsistenciasFURPRO.trim().toUpperCase();
	}

	/**
	 * @param nombreArchivoInconsistenciasFURPRO the nombreArchivoInconsistenciasFURPRO to set
	 */
	public void setNombreArchivoInconsistenciasFURPRO(
			String nombreArchivoInconsistenciasFURPRO) {
		this.nombreArchivoInconsistenciasFURPRO = nombreArchivoInconsistenciasFURPRO;
	}

	/**
	 * @return the creoArchivoInconsistenciasFURIPS1
	 */
	public boolean isCreoArchivoInconsistenciasFURIPS1() {
		return creoArchivoInconsistenciasFURIPS1;
	}

	/**
	 * @param creoArchivoInconsistenciasFURIPS1 the creoArchivoInconsistenciasFURIPS1 to set
	 */
	public void setCreoArchivoInconsistenciasFURIPS1(
			boolean creoArchivoInconsistenciasFURIPS1) {
		this.creoArchivoInconsistenciasFURIPS1 = creoArchivoInconsistenciasFURIPS1;
	}

	/**
	 * @return the creoArchivoInconsistenciasFURIPS2
	 */
	public boolean isCreoArchivoInconsistenciasFURIPS2() {
		return creoArchivoInconsistenciasFURIPS2;
	}

	/**
	 * @param creoArchivoInconsistenciasFURIPS2 the creoArchivoInconsistenciasFURIPS2 to set
	 */
	public void setCreoArchivoInconsistenciasFURIPS2(
			boolean creoArchivoInconsistenciasFURIPS2) {
		this.creoArchivoInconsistenciasFURIPS2 = creoArchivoInconsistenciasFURIPS2;
	}

	/**
	 * @return the creoArchivoInconsistenciasFURTRAN
	 */
	public boolean isCreoArchivoInconsistenciasFURTRAN() {
		return creoArchivoInconsistenciasFURTRAN;
	}

	/**
	 * @param creoArchivoInconsistenciasFURTRAN the creoArchivoInconsistenciasFURTRAN to set
	 */
	public void setCreoArchivoInconsistenciasFURTRAN(
			boolean creoArchivoInconsistenciasFURTRAN) {
		this.creoArchivoInconsistenciasFURTRAN = creoArchivoInconsistenciasFURTRAN;
	}

	/**
	 * @return the creoArchivoInconsistenciasFURPRO
	 */
	public boolean isCreoArchivoInconsistenciasFURPRO() {
		return creoArchivoInconsistenciasFURPRO;
	}

	/**
	 * @param creoArchivoInconsistenciasFURPRO the creoArchivoInconsistenciasFURPRO to set
	 */
	public void setCreoArchivoInconsistenciasFURPRO(
			boolean creoArchivoInconsistenciasFURPRO) {
		this.creoArchivoInconsistenciasFURPRO = creoArchivoInconsistenciasFURPRO;
	}

	/**
	 * @return the procesoExitoso
	 */
	public boolean isProcesoExitoso() {
		return procesoExitoso;
	}

	/**
	 * @param procesoExitoso the procesoExitoso to set
	 */
	public void setProcesoExitoso(boolean procesoExitoso) {
		this.procesoExitoso = procesoExitoso;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isGeneroArchivosDeInconsistencias()
	{
		if(this.isCreoArchivoInconsistenciasFURIPS1()||this.isCreoArchivoInconsistenciasFURIPS2()||this.isCreoArchivoInconsistenciasFURPRO()||this.isCreoArchivoInconsistenciasFURTRAN())
			return true;
		return false;
	}

	/**
	 * @return the rutaZipUpload
	 */
	public String getRutaZipUpload() {
		return rutaZipUpload;
	}

	/**
	 * @param rutaZipUpload the rutaZipUpload to set
	 */
	public void setRutaZipUpload(String rutaZipUpload) {
		this.rutaZipUpload = rutaZipUpload;
	}
	
}