package util.reportes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author Edgar Carvajal
 * 	UTILIDAD FACHADA PARA CARGAR EN PRESENTACION LA INFORMACION  RESPECTIVA DEL ENCABEZADO DEL REPORTE
 * 
 */
public class InfoEncabezadoReporte implements Serializable
{
	
	
	/**
	 * Informacion Instucion Basica
	 */
	private String logoInstiucion;
	
	private String ubicacionLogo;
	
	private String razonSocial;
	
	private String direccion;
	
	private String telefono;
	
	private String nit;
	
	private String digitoVerficacion;
	
	
	
	/**
	 * 	INFORMACION PACIENTE
	 */
	private String nombrePaciente;
	
	private String edadPaciente;
	
	private String sexoPaciente;
	
	private String identificacionPaciente;
	
	private String nombreViaIngreso;
	
	
	private String fechaNacimiento;
	
	private String ciudad;
	
	private String telefonoFijo;
	
	private String telefonoCelular;
	
	private String direccionPaciente;
	
	
	
	
	
	
	
	/**
	 * CENTRO ATENCION
	 */
	private String nombreCentroAtencion;
	
	/**
	 * FECHA REGISTRO
	 */
	private  String fechaRegistro;
	
	
	/**
	 * HORA REGISTRO
	 */
	private String horaRegistro;
	
	/**
	 * NOMBRE PROFESIONAL SALUD
	 */
	private String nombreProfesionalSalud;
	
	/**
	 * 
	 */
	private ArrayList<String> especialidadesProfesional;
	
	
	private String ingresoPaciente;
	
	private String responsables;

	
	
	
	/**
	 * 
	 */
	public InfoEncabezadoReporte() 
	{
		super();
		this.ubicacionLogo = "";
		this.razonSocial = "";
		this.direccion = "";
		this.telefono = "";
		this.nit = "";
		this.nombrePaciente = "";
		this.edadPaciente = "";
		this.sexoPaciente = "";
		this.identificacionPaciente = "";
		this.nombreCentroAtencion = "";
		this.fechaRegistro = "";
		this.horaRegistro = "";
		this.nombreProfesionalSalud = "";
		this.especialidadesProfesional = new ArrayList<String>();
		this.logoInstiucion="";
		this.digitoVerficacion="";
		this.nombreViaIngreso="";
		this.ingresoPaciente="";
		this.fechaNacimiento="";
		this.ciudad="";
		this.telefonoCelular="";
		this.telefonoFijo="";
		this.direccionPaciente="";
		this.responsables="";
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
	 * @return the nombrePaciente
	 */
	public String getNombrePaciente() {
		return nombrePaciente;
	}



	/**
	 * @param nombrePaciente the nombrePaciente to set
	 */
	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}



	/**
	 * @return the edadPaciente
	 */
	public String getEdadPaciente() {
		return edadPaciente;
	}



	/**
	 * @param edadPaciente the edadPaciente to set
	 */
	public void setEdadPaciente(String edadPaciente) {
		this.edadPaciente = edadPaciente;
	}



	/**
	 * @return the sexoPaciente
	 */
	public String getSexoPaciente() {
		return sexoPaciente;
	}



	/**
	 * @param sexoPaciente the sexoPaciente to set
	 */
	public void setSexoPaciente(String sexoPaciente) {
		this.sexoPaciente = sexoPaciente;
	}



	/**
	 * @return the identificacionPaciente
	 */
	public String getIdentificacionPaciente() {
		return identificacionPaciente;
	}



	/**
	 * @param identificacionPaciente the identificacionPaciente to set
	 */
	public void setIdentificacionPaciente(String identificacionPaciente) {
		this.identificacionPaciente = identificacionPaciente;
	}



	/**
	 * @return the nombreCentroAtencion
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}



	/**
	 * @param nombreCentroAtencion the nombreCentroAtencion to set
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}



	/**
	 * @return the fechaRegistro
	 */
	public String getFechaRegistro() {
		return fechaRegistro;
	}



	/**
	 * @param fechaRegistro the fechaRegistro to set
	 */
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}



	/**
	 * @return the horaRegistro
	 */
	public String getHoraRegistro() {
		return horaRegistro;
	}



	/**
	 * @param horaRegistro the horaRegistro to set
	 */
	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}



	/**
	 * @return the nombreProfesionalSalud
	 */
	public String getNombreProfesionalSalud() {
		return nombreProfesionalSalud;
	}



	/**
	 * @param nombreProfesionalSalud the nombreProfesionalSalud to set
	 */
	public void setNombreProfesionalSalud(String nombreProfesionalSalud) {
		this.nombreProfesionalSalud = nombreProfesionalSalud;
	}



	/**
	 * @return the especialidadesProfesional
	 */
	public ArrayList<String> getEspecialidadesProfesional() {
		return especialidadesProfesional;
	}



	/**
	 * @param especialidadesProfesional the especialidadesProfesional to set
	 */
	public void setEspecialidadesProfesional(
			ArrayList<String> especialidadesProfesional) {
		this.especialidadesProfesional = especialidadesProfesional;
	}



	public void setLogoInstiucion(String logoInstiucion) {
		this.logoInstiucion = logoInstiucion;
	}



	public String getLogoInstiucion() {
		return logoInstiucion;
	}



	public void setDigitoVerficacion(String digitoVerficacion) {
		this.digitoVerficacion = digitoVerficacion;
	}



	public String getDigitoVerficacion() {
		return digitoVerficacion;
	}



	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}



	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}



	public void setIngresoPaciente(String ingresoPaciente) {
		this.ingresoPaciente = ingresoPaciente;
	}



	public String getIngresoPaciente() {
		return ingresoPaciente;
	}



	/**
	 * Obtiene el valor del atributo fechaNacimiento
	 *
	 * @return Retorna atributo fechaNacimiento
	 */
	public String getFechaNacimiento()
	{
		return fechaNacimiento;
	}



	/**
	 * Establece el valor del atributo fechaNacimiento
	 *
	 * @param valor para el atributo fechaNacimiento
	 */
	public void setFechaNacimiento(String fechaNacimiento)
	{
		this.fechaNacimiento = fechaNacimiento;
	}



	/**
	 * Obtiene el valor del atributo ciudad
	 *
	 * @return Retorna atributo ciudad
	 */
	public String getCiudad()
	{
		return ciudad;
	}



	/**
	 * Establece el valor del atributo ciudad
	 *
	 * @param valor para el atributo ciudad
	 */
	public void setCiudad(String ciudad)
	{
		this.ciudad = ciudad;
	}



	/**
	 * Obtiene el valor del atributo telefonoFijo
	 *
	 * @return Retorna atributo telefonoFijo
	 */
	public String getTelefonoFijo()
	{
		return telefonoFijo;
	}



	/**
	 * Establece el valor del atributo telefonoFijo
	 *
	 * @param valor para el atributo telefonoFijo
	 */
	public void setTelefonoFijo(String telefonoFijo)
	{
		this.telefonoFijo = telefonoFijo;
	}



	/**
	 * Obtiene el valor del atributo telefonoCelular
	 *
	 * @return Retorna atributo telefonoCelular
	 */
	public String getTelefonoCelular()
	{
		return telefonoCelular;
	}



	/**
	 * Establece el valor del atributo telefonoCelular
	 *
	 * @param valor para el atributo telefonoCelular
	 */
	public void setTelefonoCelular(String telefonoCelular)
	{
		this.telefonoCelular = telefonoCelular;
	}



	/**
	 * Establece el valor del atributo direccionPaciente
	 *
	 * @param valor para el atributo direccionPaciente
	 */
	public void setDireccionPaciente(String direccionPaciente)
	{
		this.direccionPaciente = direccionPaciente;
	}



	/**
	 * Obtiene el valor del atributo direccionPaciente
	 *
	 * @return Retorna atributo direccionPaciente
	 */
	public String getDireccionPaciente()
	{
		return direccionPaciente;
	}



	/**
	 * Establece el valor del atributo responsables
	 *
	 * @param valor para el atributo responsables
	 */
	public void setResponsables(String responsables)
	{
		this.responsables = responsables;
	}



	/**
	 * Obtiene el valor del atributo responsables
	 *
	 * @return Retorna atributo responsables
	 */
	public String getResponsables()
	{
		return responsables;
	}
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	

}
