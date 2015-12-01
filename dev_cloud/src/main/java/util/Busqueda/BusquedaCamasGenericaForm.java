package util.Busqueda;

import java.util.HashMap;
import org.apache.struts.validator.ValidatorForm;

public class BusquedaCamasGenericaForm extends ValidatorForm 
{
	
	//--- Atributos 
	
	/**
	 * Estado de la forma enviada al Action
	 * */
	private String estado;
	
	/**
	 * Estado de la cama
	 * */
	private String estadoCama;
	
	/**
	 * Codigo del Centro de Costos
	 * */
	private String centroCostos;
	
	/**
	 * Codigo del Centro de Atencion 
	 * */
	private String centroAtencion;	
	
	/**
	 * Codigo de la Via de Ingreso 
	 * */
	private String viaIngreso;
	
	/**
	 * Fecha del movimiento de la cama
	 */
	private String fechaMovimiento = "";
	
	/**
	 * Hora del movimiento de la cama
	 */
	private String horaMovimiento = "";
	
	/**
	 * Piso por el cual se buscará la cama
	 */
	private String piso = "";
	
	/**
	 * Mapa de Consulta  
	 */
	HashMap consultaMap = new HashMap();
	
	/**
	 * Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * Patron por el Cual ordenar
	 * */
	private String patronOrdenar;
	
	/**
	 * Proximo link en el Pager
	 * */
	private String linkSiguiente;
	
	/**
	 * Tipo Paciente
	 */
	private String tipoPaciente;
	
	/**
	 * Atributo para filtrar por camas que se pueden asignar en admisión
	 */
	private String asignableAdmision = "";
	
	
	//Atributos que envia el usuario
	private String hagoSubmit="";
	private String nombreForma="";
	private String nombreMapa="";
	private String idNumFilasMapa="";	
	
	//--- Fin Atributos 
	
	//--- Metodos 
	
	/**
	 * Inicializa los atributos de la forma
	 * */
	public void reset()
	{
		consultaMap = new HashMap();
		consultaMap.put("numRegistros",0);		
		this.ultimoPatron="";
		this.patronOrdenar="";
		this.linkSiguiente="";
	
	}
	
	
	
	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	} 

	/**
	 * @return the consultaMap
	 */
	public HashMap getConsultaMap() {
		return consultaMap;
	}

	/**
	 * @param consultaMap the consultaMap to set
	 */
	public void setConsultaMap(HashMap consultaMap) {
		this.consultaMap = consultaMap;
	}
	
	/**
	 * @return the Object
	 */
	public Object getConsultaMap(String key) {
		return consultaMap.get(key);
	}

	/**
	 * @param String key
	 * @param Object value
	 */
	public void setConsultaMap(String key, Object value) {
		this.consultaMap.put(key, value);
	}

	
	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	
	public String getHagoSubmit() {
		return hagoSubmit;
	}

	public void setHagoSubmit(String hagoSubmit) {
		this.hagoSubmit = hagoSubmit;
	}

	
	public String getNombreForma() {
		return nombreForma;
	}

	public void setNombreForma(String nombreForma) {
		this.nombreForma = nombreForma;
	}

	public String getNombreMapa() {
		return nombreMapa;
	}

	public void setNombreMapa(String nombreMapa) {
		this.nombreMapa = nombreMapa;
	}

	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the centroCostos
	 */
	public String getCentroCostos() {
		return centroCostos;
	}

	/**
	 * @param centroCostos the centroCostos to set
	 */
	public void setCentroCostos(String centroCostos) {
		this.centroCostos = centroCostos;
	}

	/**
	 * @return the estadoCama
	 */
	public String getEstadoCama() {
		return estadoCama;
	}

	/**
	 * @param estadoCama the estadoCama to set
	 */
	public void setEstadoCama(String estadoCama) {
		this.estadoCama = estadoCama;
	}

	/**
	 * @return the idNumFilasMapa
	 */
	public String getIdNumFilasMapa() {
		return idNumFilasMapa;
	}

	/**
	 * @param idNumFilasMapa the idNumFilasMapa to set
	 */
	public void setIdNumFilasMapa(String idNumFilasMapa) {
		this.idNumFilasMapa = idNumFilasMapa;
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
	 * @return the fechaMovimiento
	 */
	public String getFechaMovimiento() {
		return fechaMovimiento;
	}



	/**
	 * @param fechaMovimiento the fechaMovimiento to set
	 */
	public void setFechaMovimiento(String fechaMovimiento) {
		this.fechaMovimiento = fechaMovimiento;
	}



	/**
	 * @return the horaMovimiento
	 */
	public String getHoraMovimiento() {
		return horaMovimiento;
	}



	/**
	 * @param horaMovimiento the horaMovimiento to set
	 */
	public void setHoraMovimiento(String horaMovimiento) {
		this.horaMovimiento = horaMovimiento;
	}



	/**
	 * @return the piso
	 */
	public String getPiso() {
		return piso;
	}



	/**
	 * @param piso the piso to set
	 */
	public void setPiso(String piso) {
		this.piso = piso;
	}



	/**
	 * @return the tipoPaciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}



	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}



	/**
	 * @return the asignableAdmision
	 */
	public String getAsignableAdmision() {
		return asignableAdmision;
	}



	/**
	 * @param asignableAdmision the asignableAdmision to set
	 */
	public void setAsignableAdmision(String asignableAdmision) {
		this.asignableAdmision = asignableAdmision;
	}

	//--- Fin Metodos	
}