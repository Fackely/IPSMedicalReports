package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.odontologia.InfoAntecedenteOdonto;
import util.odontologia.InfoOdontograma;

public class DtoEvolucionOdontologica  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double codigoPk;
	
	private double cita;
	
	private String fechaModifica;
	
	private String hora;
	
	private String usuario;
	
	
	private int codigoPaciente;
	
	private int plantilla;
	
	
	//**********ATRIBUTO ADICIONAKL**************************
	private BigDecimal codigoPlantillaEvolucion;
	
	//*********++ATRIBUTOS DE LOS DISTINTOS COMPONENTES********************
	private InfoAntecedenteOdonto infoAntecedentesOdo ;
	private DtoComponenteIndicePlaca dtoIndicePlaca;
	private InfoOdontograma infoOdontograma;
	//**********************************************************************************************
	
	private String usuarioCreacion;
	
	
	/**
	 * Constructor 
	 */
	public DtoEvolucionOdontologica()
	{
		this.reset();
	}
	
	
	void reset()
	{
		this.codigoPk=ConstantesBD.codigoNuncaValidoDouble;
		this.cita=ConstantesBD.codigoNuncaValidoDouble;
		this.fechaModifica="";
		this.hora="";
		this.usuario="";
		this.codigoPaciente=ConstantesBD.codigoNuncaValido;
		this.plantilla=ConstantesBD.codigoNuncaValido;
		this.codigoPlantillaEvolucion = new BigDecimal(ConstantesBD.codigoNuncaValido);
		
		this.infoAntecedentesOdo = new InfoAntecedenteOdonto();
		this.dtoIndicePlaca = new DtoComponenteIndicePlaca();
		this.infoOdontograma = new InfoOdontograma();
		this.usuarioCreacion="";
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
	

    	return errores;
    	
	}

	public double getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(double codigoPk) {
		this.codigoPk = codigoPk;
	}

	public double getCita() {
		return cita;
	}

	public void setCita(double cita) {
		this.cita = cita;
	}

	public String getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public int getPlantilla() {
		return plantilla;
	}

	public void setPlantilla(int plantilla) {
		this.plantilla = plantilla;
	}

	/**
	 * @return the codigoPlantillaEvolucion
	 */
	public BigDecimal getCodigoPlantillaEvolucion() {
		return codigoPlantillaEvolucion;
	}

	/**
	 * @param codigoPlantillaEvolucion the codigoPlantillaEvolucion to set
	 */
	public void setCodigoPlantillaEvolucion(BigDecimal codigoPlantillaEvolucion) {
		this.codigoPlantillaEvolucion = codigoPlantillaEvolucion;
	}


	/**
	 * @return the infoAntecedentesOdo
	 */
	public InfoAntecedenteOdonto getInfoAntecedentesOdo() {
		return infoAntecedentesOdo;
	}


	/**
	 * @param infoAntecedentesOdo the infoAntecedentesOdo to set
	 */
	public void setInfoAntecedentesOdo(InfoAntecedenteOdonto infoAntecedentesOdo) {
		this.infoAntecedentesOdo = infoAntecedentesOdo;
	}


	/**
	 * @return the dtoIndicePlaca
	 */
	public DtoComponenteIndicePlaca getDtoIndicePlaca() {
		return dtoIndicePlaca;
	}


	/**
	 * @param dtoIndicePlaca the dtoIndicePlaca to set
	 */
	public void setDtoIndicePlaca(DtoComponenteIndicePlaca dtoIndicePlaca) {
		this.dtoIndicePlaca = dtoIndicePlaca;
	}


	/**
	 * @return the infoOdontograma
	 */
	public InfoOdontograma getInfoOdontograma() {
		return infoOdontograma;
	}


	/**
	 * @param infoOdontograma the infoOdontograma to set
	 */
	public void setInfoOdontograma(InfoOdontograma infoOdontograma) {
		this.infoOdontograma = infoOdontograma;
	}


	/**
	 * @return the usuarioCreacion
	 */
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}


	/**
	 * @param usuarioCreacion the usuarioCreacion to set
	 */
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	
	
	
	
}