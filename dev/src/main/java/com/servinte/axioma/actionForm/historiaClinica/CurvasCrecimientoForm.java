package com.servinte.axioma.actionForm.historiaClinica;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import com.servinte.axioma.dto.historiaClinica.CoordenadasCurvaCrecimientoDto;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoPacienteDto;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoParametrizabDto;

/**
 * @author hermorhu
 * @created 09-Oct-2012 
 */
public class CurvasCrecimientoForm extends ValidatorForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Atributo que identifica si se debe mostrar la ruta del a JSP
	 */
	private boolean mostrarRutaJsp;
	
	/**
	 * Atributo que almacena la talla actual del paciente
	 */
	private String talla;
	
	/**
	 * Atributo que almacena el peso actual del paciente
	 */
	private String peso;
	
	/**
	 * Atributo que almacena el Indice de Masa Corporal actual del paciente
	 */
	private String imc;
	
	/**
	 * Atributo que almacena el perimetro cefalico actual del paciente
	 */
	private String perimetroCefalico;
	
	/**
	 * Atributo que almacena la fecha de atencion del paciente
	 */
	private String fechaAtencion;
	
	/**
	 * Atributo que almacena la edad actual del paciente
	 */
	private String edadPaciente;
	
	/**
	 * Atributo con la lista de las Curvas para el Paciente
	 */
	private List<CurvaCrecimientoPacienteDto> listaCurvasCrecimientoPaciente;
	
	/**
	 * Atributo que almacena el index de la curva a graficar
	 */
	private String indexCurvaSeleccionada;
	
	/**Atributo con la Curva de Crecimiento seleccionada
	 * 
	 */
	private CurvaCrecimientoParametrizabDto curvaCrecimientoSeleccionada;
	
	/**
	 * Atributo que alamcena la imagen editable
	 */
	private String imagenCurvaCrecimientoSeleccionada;
	
	/**
	 * Atributo que almacena la imagen solo de consulta
	 */
	private String imagenCurvaCrecimientoBloqueada;
	
	/**
	 * Atributo que almacena las coordenadas de los puntos de la curva de crecimiento alamcenados
	 */
	private List<CoordenadasCurvaCrecimientoDto> coordenadasPuntos;
	
	/**
	 * Atributo que almacena bandera para mostrar la grafica seleccionada
	 */
	private boolean mostrarGrafica = false ;

	/**
	 * Atributo que almacena las coordenadas en X de los puntos de una curva
	 */
	private String coorX;
	
	/**
	 * Atributo que almacena las coordenadas en Y de los puntos de una curva
	 */
	private String coorY;
	
	/**
	 * Atributo que almacena la imagen de la curva e n Base64
	 */
	private String imagenBase64;
	
	/**
	 * Atributo que almacena la bandera para mostrar el mensaje de curva guardada
	 */
	private boolean mensajeCurvaGuardada;
	
	/**
	 * Atributo que almacena las coordenadas de los errores de la curva de crecimiento 
	 */
	private List<CoordenadasCurvaCrecimientoDto> coordenadasErrores;
	
	/**
	 * Atributo que almacena las coordenadas en X de los errores de los puntos de una curva
	 */
	private String coorErrorX;
	
	/**
	 * Atributo que almacena las coordenadas en Y de los errores de los puntos de una curva
	 */
	private String coorErrorY;
	
	/**
	 * Atributo que almacena el nombre  de el pdf generado con la Curva de Crecimiento
	 */
	private String nombreArchivoGenerado;
	
	/**
	 * Atributo que almacena la bandera para mostrar  o no las Curvas anteriores al nuevo desarrollo
	 */
	private boolean mostrarCurvasAnteriores = false;
	
	/**
	 * @return the mostrarRutaJsp
	 */
	public boolean isMostrarRutaJsp() {
		return mostrarRutaJsp;
	}
	
	/**
	 * 
	 */
	public void reset(){
		this.coordenadasErrores = new ArrayList<CoordenadasCurvaCrecimientoDto>();
		this.coordenadasPuntos = new ArrayList<CoordenadasCurvaCrecimientoDto>();
		this.coorErrorX = "";
		this.coorErrorY = "";
		this.coorX = "";
		this.coorY = "";
		this.curvaCrecimientoSeleccionada = new CurvaCrecimientoParametrizabDto();
		this.edadPaciente = "";
		this.fechaAtencion = "";
		this.imagenBase64 = "";
		this.imagenCurvaCrecimientoBloqueada = "";
		this.imagenCurvaCrecimientoSeleccionada = "";
		this.indexCurvaSeleccionada = "";
		this.listaCurvasCrecimientoPaciente = new ArrayList<CurvaCrecimientoPacienteDto>();
		this.mensajeCurvaGuardada = false;
		this.mostrarGrafica = false;
		this.mostrarRutaJsp = false;
		this.nombreArchivoGenerado = "";
		this.mostrarCurvasAnteriores = false;
	}
	
	/**
	 * 
	 */
	public void resetGraficaCurvaCrecimiento(){
		this.indexCurvaSeleccionada = "";
		this.curvaCrecimientoSeleccionada = new CurvaCrecimientoParametrizabDto();
		this.imagenCurvaCrecimientoSeleccionada = "";
		this.imagenCurvaCrecimientoBloqueada = "";
		this.mostrarGrafica = false;
		this.coordenadasPuntos = new ArrayList<CoordenadasCurvaCrecimientoDto>();
		this.coorX = "";
		this.coorY = "";
		this.coordenadasErrores = new ArrayList<CoordenadasCurvaCrecimientoDto>();
		this.coorErrorX = "";
		this.coorErrorY = "";
		this.imagenBase64 = "";
	}
	
	/**
	 * 
	 */
	public void resetMensajes(){
		this.mensajeCurvaGuardada = false;
		this.nombreArchivoGenerado = "";
	}

	public void resetCurvaSeleccionada(){
		this.imagenCurvaCrecimientoSeleccionada = "";
		this.imagenCurvaCrecimientoBloqueada = "";
		this.coordenadasPuntos = new ArrayList<CoordenadasCurvaCrecimientoDto>();
		this.coordenadasErrores = new ArrayList<CoordenadasCurvaCrecimientoDto>();
	}
	
	/**
	 * @param mostrarRutaJsp the mostrarRutaJsp to set
	 */
	public void setMostrarRutaJsp(boolean mostrarRutaJsp) {
		this.mostrarRutaJsp = mostrarRutaJsp;	
	}

	/**
	 * @return the talla
	 */
	public String getTalla() {
		return talla;
	}

	/**
	 * @param talla the talla to set
	 */
	public void setTalla(String talla) {
		this.talla = talla;
	}

	/**
	 * @return the peso
	 */
	public String getPeso() {
		return peso;
	}

	/**
	 * @param peso the peso to set
	 */
	public void setPeso(String peso) {
		this.peso = peso;
	}

	/**
	 * @return the imc
	 */
	public String getImc() {
		return imc;
	}

	/**
	 * @param imc the imc to set
	 */
	public void setImc(String imc) {
		this.imc = imc;
	}

	/**
	 * @return the perimetroCefalico
	 */
	public String getPerimetroCefalico() {
		return perimetroCefalico;
	}

	/**
	 * @param perimetroCefalico the perimetroCefalico to set
	 */
	public void setPerimetroCefalico(String perimetroCefalico) {
		this.perimetroCefalico = perimetroCefalico;
	}

	/**
	 * @return the fechaAtencion
	 */
	public String getFechaAtencion() {
		return fechaAtencion;
	}

	/**
	 * @param fechaAtencion the fechaAtencion to set
	 */
	public void setFechaAtencion(String fechaAtencion) {
		this.fechaAtencion = fechaAtencion;
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
	 * @return the indexCurvaSeleccionada
	 */
	public String getIndexCurvaSeleccionada() {
		return indexCurvaSeleccionada;
	}

	/**
	 * @param indexCurvaSeleccionada the indexCurvaSeleccionada to set
	 */
	public void setIndexCurvaSeleccionada(String indexCurvaSeleccionada) {
		this.indexCurvaSeleccionada = indexCurvaSeleccionada;
	}

	/**
	 * @return the curvaCrecimientoSeleccionada
	 */
	public CurvaCrecimientoParametrizabDto getCurvaCrecimientoSeleccionada() {
		return curvaCrecimientoSeleccionada;
	}

	/**
	 * @param curvaCrecimientoSeleccionada the curvaCrecimientoSeleccionada to set
	 */
	public void setCurvaCrecimientoSeleccionada(CurvaCrecimientoParametrizabDto curvaCrecimientoSeleccionada) {
		this.curvaCrecimientoSeleccionada = curvaCrecimientoSeleccionada;
	}

	/**
	 * @return the mostrarGrafica
	 */
	public boolean isMostrarGrafica() {
		return mostrarGrafica;
	}

	/**
	 * @param mostrarGrafica the mostrarGrafica to set
	 */
	public void setMostrarGrafica(boolean mostrarGrafica) {
		this.mostrarGrafica = mostrarGrafica;
	}

	/**
	 * @return the listaCurvasCrecimientoPaciente
	 */
	public List<CurvaCrecimientoPacienteDto> getListaCurvasCrecimientoPaciente() {
		return listaCurvasCrecimientoPaciente;
	}

	/**coordenadasPuntos
	 * @param listaCurvasCrecimientoPaciente the listaCurvasCrecimientoPaciente to set
	 */
	public void setListaCurvasCrecimientoPaciente(
			List<CurvaCrecimientoPacienteDto> listaCurvasCrecimientoPaciente) {
		this.listaCurvasCrecimientoPaciente = listaCurvasCrecimientoPaciente;
	}

	/**
	 * @return the imagenCurvaCrecimientoSeleccionada
	 */
	public String getImagenCurvaCrecimientoSeleccionada() {
		return imagenCurvaCrecimientoSeleccionada;
	}

	/**
	 * @param imagenCurvaCrecimientoSeleccionada the imagenCurvaCrecimientoSeleccionada to set
	 */
	public void setImagenCurvaCrecimientoSeleccionada(
			String imagenCurvaCrecimientoSeleccionada) {
		this.imagenCurvaCrecimientoSeleccionada = imagenCurvaCrecimientoSeleccionada;
	}

	/**
	 * @return the imagenCurvaCrecimientoBloqueada
	 */
	public String getImagenCurvaCrecimientoBloqueada() {
		return imagenCurvaCrecimientoBloqueada;
	}

	/**
	 * @param imagenCurvaCrecimientoBloqueada the imagenCurvaCrecimientoBloqueada to set
	 */
	public void setImagenCurvaCrecimientoBloqueada(
			String imagenCurvaCrecimientoBloqueada) {
		this.imagenCurvaCrecimientoBloqueada = imagenCurvaCrecimientoBloqueada;
	}

	/**
	 * @return the coordenadasPuntos
	 */
	public List<CoordenadasCurvaCrecimientoDto> getCoordenadasPuntos() {
		return coordenadasPuntos;
	}

	/**
	 * @param coordenadasPuntos the coordenadasPuntos to set
	 */
	public void setCoordenadasPuntos(
			List<CoordenadasCurvaCrecimientoDto> coordenadasPuntos) {
		this.coordenadasPuntos = coordenadasPuntos;
	}

	/**
	 * @return the coorX
	 */
	public String getCoorX() {
		return coorX;
	}

	/**
	 * @param coorX the coorX to set
	 */
	public void setCoorX(String coorX) {
		this.coorX = coorX;
	}

	/**
	 * @return the coorY
	 */
	public String getCoorY() {
		return coorY;
	}

	/**
	 * @param coorY the coorY to set
	 */
	public void setCoorY(String coorY) {
		this.coorY = coorY;
	}

	/**
	 * @return the imagenBase64
	 */
	public String getImagenBase64() {
		return imagenBase64;
	}

	/**
	 * @param imagenBase64 the imagenBase64 to set
	 */
	public void setImagenBase64(String imagenBase64) {
		this.imagenBase64 = imagenBase64;
	}

	/**
	 * @return the mensajeCurvaGuardada
	 */
	public boolean isMensajeCurvaGuardada() {
		return mensajeCurvaGuardada;
	}

	/**
	 * @param mensajeCurvaGuardada the mensajeCurvaGuardada to set
	 */
	public void setMensajeCurvaGuardada(boolean mensajeCurvaGuardada) {
		this.mensajeCurvaGuardada = mensajeCurvaGuardada;
	}

	/**
	 * @return the coordenadasErrores
	 */
	public List<CoordenadasCurvaCrecimientoDto> getCoordenadasErrores() {
		return coordenadasErrores;
	}

	/**
	 * @param coordenadasErrores the coordenadasErrores to set
	 */
	public void setCoordenadasErrores(
			List<CoordenadasCurvaCrecimientoDto> coordenadasErrores) {
		this.coordenadasErrores = coordenadasErrores;
	}

	/**
	 * @return the coorErrorX
	 */
	public String getCoorErrorX() {
		return coorErrorX;
	}

	/**
	 * @param coorErrorX the coorErrorX to set
	 */
	public void setCoorErrorX(String coorErrorX) {
		this.coorErrorX = coorErrorX;
	}

	/**
	 * @return the coorErrorY
	 */
	public String getCoorErrorY() {
		return coorErrorY;
	}

	/**
	 * @param coorErrorY the coorErrorY to set
	 */
	public void setCoorErrorY(String coorErrorY) {
		this.coorErrorY = coorErrorY;
	}

	/**
	 * @return the nombreArchivoGenerado
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	/**
	 * @param nombreArchivoGenerado the nombreArchivoGenerado to set
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}
	
	/**
	 * @return the mostrarCurvasAnteriores
	 */
	public boolean isMostrarCurvasAnteriores() {
		return mostrarCurvasAnteriores;
	}

	/**
	 * @param mostrarCurvasAnteriores the mostrarCurvasAnteriores to set
	 */
	public void setMostrarCurvasAnteriores(boolean mostrarCurvasAnteriores) {
		this.mostrarCurvasAnteriores = mostrarCurvasAnteriores;
	}
	
}
