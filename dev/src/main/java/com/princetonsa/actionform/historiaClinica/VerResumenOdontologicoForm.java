package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.odontologia.InfoPlanTratamiento;

import com.princetonsa.dto.historiaClinica.componentes.DtoAntecedentesOdontologicosAnt;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.princetonsa.dto.odontologia.DtoAntecendenteOdontologico;
import com.princetonsa.dto.odontologia.DtoLogPlanTratamiento;
import com.princetonsa.dto.odontologia.DtoTratamientoExterno;

public class VerResumenOdontologicoForm extends ValidatorForm{

	private String estado;
	private DtoAntecedentesOdontologicosAnt antecedentesOdontoPrev; 
	private DtoAntecendenteOdontologico ultimoAntecedente;
	private DtoAntecendenteOdontologico detalleAntecedente;
	private DtoComponente componentePpal;
	private DtoComponente componenteDetalle;
	private ArrayList<InfoPlanTratamiento> arrayPlanTramientos;
	private ArrayList<DtoAntecendenteOdontologico> arrayAntecedentesActuales;
	private int posAntecedente;
	private String mostrarAnteriores; 
	private String mostrarActuales;
	private String datosGeneralesUltimoAnt;
	private String datosGeneralesDetalleAnt;
	private String mostrarPopUp = ConstantesBD.acronimoNo;
	private String volverA = "";
	private String fechaHora;
	private String nombreMedico;
	
	public VerResumenOdontologicoForm()
	{
		this.reset();
	}
	
	public void reset(HttpServletRequest request)
	{
		this.reset();
		this.mostrarPopUp = request.getParameter("mostrarPopUp")==null?ConstantesBD.acronimoNo:request.getParameter("mostrarPopUp");
		
	}
	
	public void reset()
	{
		this.estado=new String("");
		this.antecedentesOdontoPrev= new DtoAntecedentesOdontologicosAnt();
		this.arrayPlanTramientos= new ArrayList<InfoPlanTratamiento>();
		this.mostrarActuales= new String(ConstantesBD.acronimoSi);
		this.mostrarAnteriores= new String(ConstantesBD.acronimoNo);
		this.arrayAntecedentesActuales = new ArrayList<DtoAntecendenteOdontologico>();
		this.ultimoAntecedente= new DtoAntecendenteOdontologico();
		this.detalleAntecedente= new DtoAntecendenteOdontologico();
		this.datosGeneralesUltimoAnt=new String("");
		this.datosGeneralesDetalleAnt=new String("");
		this.posAntecedente= ConstantesBD.codigoNuncaValido;
		this.mostrarPopUp = ConstantesBD.acronimoNo;
		this.componentePpal= new DtoComponente();
		this.componenteDetalle = new DtoComponente();
		this.fechaHora = "";
		this.nombreMedico = "";
		
		
	}

	
	public void resetDetalleAntecedente()
	{
		this.detalleAntecedente= new DtoAntecendenteOdontologico();
		this.datosGeneralesDetalleAnt=new String("");
	}
	
	public void resetComponenteDetalle()
	{
		this.componenteDetalle= new DtoComponente();
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
	 * @return the antecedentesOdontoPrev
	 */
	public DtoAntecedentesOdontologicosAnt getAntecedentesOdontoPrev() {
		return antecedentesOdontoPrev;
	}

	/**
	 * @param antecedentesOdontoPrev the antecedentesOdontoPrev to set
	 */
	public void setAntecedentesOdontoPrev(DtoAntecedentesOdontologicosAnt antecedentesOdontoPrev) {
		this.antecedentesOdontoPrev = antecedentesOdontoPrev;
	}

	/**
	 * @return the mostrarAnteriores
	 */
	public String getMostrarAnteriores() {
		return mostrarAnteriores;
	}

	/**
	 * @param mostrarAnteriores the mostrarAnteriores to set
	 */
	public void setMostrarAnteriores(String mostrarAnteriores) {
		this.mostrarAnteriores = mostrarAnteriores;
	}

	/**
	 * @return the mostrarActuales
	 */
	public String getMostrarActuales() {
		return mostrarActuales;
	}

	/**
	 * @param mostrarActuales the mostrarActuales to set
	 */
	public void setMostrarActuales(String mostrarActuales) {
		this.mostrarActuales = mostrarActuales;
	}

	/**
	 * @return the arrayPlanTramientos
	 */
	public ArrayList<InfoPlanTratamiento> getArrayPlanTramientos() {
		return arrayPlanTramientos;
	}

	/**
	 * @param arrayPlanTramientos the arrayPlanTramientos to set
	 */
	public void setArrayPlanTramientos(
			ArrayList<InfoPlanTratamiento> arrayPlanTramientos) {
		this.arrayPlanTramientos = arrayPlanTramientos;
	}

	/**
	 * @return the arrayAntecedentesActuales
	 */
	public ArrayList<DtoAntecendenteOdontologico> getArrayAntecedentesActuales() {
		return arrayAntecedentesActuales;
	}

	/**
	 * @param arrayAntecedentesActuales the arrayAntecedentesActuales to set
	 */
	public void setArrayAntecedentesActuales(
			ArrayList<DtoAntecendenteOdontologico> arrayAntecedentesActuales) {
		this.arrayAntecedentesActuales = arrayAntecedentesActuales;
	}

	/**
	 * @return the ultimoAntecedente
	 */
	public DtoAntecendenteOdontologico getUltimoAntecedente() {
		return ultimoAntecedente;
	}

	/**
	 * @param ultimoAntecedente the ultimoAntecedente to set
	 */
	public void setUltimoAntecedente(DtoAntecendenteOdontologico ultimoAntecedente) {
		this.ultimoAntecedente = ultimoAntecedente;
	}

	/**
	 * @return the datosGeneralesUltimoAnt
	 */
	public String getDatosGeneralesUltimoAnt() {
		return datosGeneralesUltimoAnt;
	}

	/**
	 * @param datosGeneralesUltimoAnt the datosGeneralesUltimoAnt to set
	 */
	public void setDatosGeneralesUltimoAnt(String datosGeneralesUltimoAnt) {
		this.datosGeneralesUltimoAnt = datosGeneralesUltimoAnt;
	}

	/**
	 * @return the posAntecedente
	 */
	public int getPosAntecedente() {
		return posAntecedente;
	}

	/**
	 * @param posAntecedente the posAntecedente to set
	 */
	public void setPosAntecedente(int posAntecedente) {
		this.posAntecedente = posAntecedente;
	}

	/**
	 * @return the detalleAntecedente
	 */
	public DtoAntecendenteOdontologico getDetalleAntecedente() {
		return detalleAntecedente;
	}

	/**
	 * @param detalleAntecedente the detalleAntecedente to set
	 */
	public void setDetalleAntecedente(DtoAntecendenteOdontologico detalleAntecedente) {
		this.detalleAntecedente = detalleAntecedente;
	}

	/**
	 * @return the datosGeneralesDetalleAnt
	 */
	public String getDatosGeneralesDetalleAnt() {
		return datosGeneralesDetalleAnt;
	}

	/**
	 * @param datosGeneralesDetalleAnt the datosGeneralesDetalleAnt to set
	 */
	public void setDatosGeneralesDetalleAnt(String datosGeneralesDetalleAnt) {
		this.datosGeneralesDetalleAnt = datosGeneralesDetalleAnt;
	}

	
	/**
	 * @return the mostrarPopUp
	 */
	public String getMostrarPopUp() {
		return mostrarPopUp;
	}

	/**
	 * @param mostrarPopUp the mostrarPopUp to set
	 */
	public void setMostrarPopUp(String mostrarPopUp) {
		this.mostrarPopUp = mostrarPopUp;
	}

	/**
	 * @return the componentePpal
	 */
	public DtoComponente getComponentePpal() {
		return componentePpal;
	}

	/**
	 * @param componentePpal the componentePpal to set
	 */
	public void setComponentePpal(DtoComponente componentePpal) {
		this.componentePpal = componentePpal;
	}

	/**
	 * @return the componenteDetalle
	 */
	public DtoComponente getComponenteDetalle() {
		return componenteDetalle;
	}

	/**
	 * @param componenteDetalle the componenteDetalle to set
	 */
	public void setComponenteDetalle(DtoComponente componenteDetalle) {
		this.componenteDetalle = componenteDetalle;
	}

	/**
	 * @return the volverA
	 */
	public String getVolverA() {
		return volverA;
	}

	/**
	 * @param volverA the volverA to set
	 */
	public void setVolverA(String volverA) {
		this.volverA = volverA;
	}

	/**
	 * @return the fechaHora
	 */
	public String getFechaHora() {
		return fechaHora;
	}

	/**
	 * @param fechaHora the fechaHora to set
	 */
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}

	/**
	 * @return the nombreMedico
	 */
	public String getNombreMedico() {
		return nombreMedico;
	}

	/**
	 * @param nombreMedico the nombreMedico to set
	 */
	public void setNombreMedico(String nombreMedico) {
		this.nombreMedico = nombreMedico;
	}

}
