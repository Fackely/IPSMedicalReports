package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;

/**
 * @author Víctor Hugo Gómez L. 
 */

public class DtoComponenteIndicePlaca implements Serializable
{
	/**
	 * default serial version UID
	 */
	private static final long serialVersionUID = 1L;
	private int codigoPk;
	private String imagen;
	private BigDecimal porcentaje;
	private String interpretacion;
	private int plantillaIngreso;
	private int plantillaEvolucionOdo;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	private String porConfirmar;
	private ArrayList<DtoDetalleIndicePlaca> detIndicePlaca;
	
	// Atributo para el SWF
	private String activoDienteAdulto;
	private String activoDienteNino;
	private String xmlRangos;
	private String xmlIndicePlaca;
	private int codigoInstitucion;
	private int edadPaciente;
	private int codigoPaciente;
	
	// Atributo forward
	private String forward; 
	
	// Atributo validacion
	private String porActualizar;
	
	public DtoComponenteIndicePlaca()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.imagen = "";
		this.porcentaje = new BigDecimal(0);
		this.interpretacion = "";
		this.plantillaIngreso = ConstantesBD.codigoNuncaValido;
		this.plantillaEvolucionOdo = ConstantesBD.codigoNuncaValido;
		this.fechaModifica = "";
		this.horaModifica = "";
		this.usuarioModifica = "";
		this.porConfirmar = "";
		this.detIndicePlaca = null;
		
		// Atributo para el SWF
		this.activoDienteAdulto = ConstantesBD.acronimoNo;
		this.activoDienteNino = ConstantesBD.acronimoNo;
		this.xmlRangos = "";
		this.xmlIndicePlaca = "";
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.edadPaciente = ConstantesBD.codigoNuncaValido;
		this.codigoPaciente = ConstantesBD.codigoNuncaValido;
		
		// Atributo forward
		this.forward = "";
		
		// Atributo validacion
		this.porActualizar = ConstantesBD.acronimoNo;
		
	}

	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the imagen
	 */
	public String getImagen() {
		return imagen;
	}

	/**
	 * @param imagen the imagen to set
	 */
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	/**
	 * @return the porcentaje
	 */
	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	/**
	 * @param porcentaje the porcentaje to set
	 */
	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

	/**
	 * @return the interpretacion
	 */
	public String getInterpretacion() {
		return interpretacion;
	}

	/**
	 * @param interpretacion the interpretacion to set
	 */
	public void setInterpretacion(String interpretacion) {
		this.interpretacion = interpretacion;
	}

	/**
	 * @return the plantillaIngreso
	 */
	public int getPlantillaIngreso() {
		return plantillaIngreso;
	}

	/**
	 * @param plantillaIngreso the plantillaIngreso to set
	 */
	public void setPlantillaIngreso(int plantillaIngreso) {
		this.plantillaIngreso = plantillaIngreso;
	}

	/**
	 * @return the plantillaEvolucionOdo
	 */
	public int getPlantillaEvolucionOdo() {
		return plantillaEvolucionOdo;
	}

	/**
	 * @param plantillaEvolucionOdo the plantillaEvolucionOdo to set
	 */
	public void setPlantillaEvolucionOdo(int plantillaEvolucionOdo) {
		this.plantillaEvolucionOdo = plantillaEvolucionOdo;
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
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the porConfirmar
	 */
	public String getPorConfirmar() {
		return porConfirmar;
	}

	/**
	 * @param porConfirmar the porConfirmar to set
	 */
	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
	}

	/**
	 * @return the detIndicePlaca
	 */
	public ArrayList<DtoDetalleIndicePlaca> getDetIndicePlaca() {
		if(detIndicePlaca == null)
		{
			detIndicePlaca = new ArrayList<DtoDetalleIndicePlaca>();
		}
		return detIndicePlaca;
	}

	/**
	 * @param detIndicePlaca the detIndicePlaca to set
	 */
	public void setDetIndicePlaca(ArrayList<DtoDetalleIndicePlaca> detIndicePlaca) {
		this.detIndicePlaca = detIndicePlaca;
	}

	/**
	 * @return the xmlIndicePlaca
	 */
	public String getXmlIndicePlaca() {
		return xmlIndicePlaca;
	}

	/**
	 * @param xmlIndicePlaca the xmlIndicePlaca to set
	 */
	public void setXmlIndicePlaca(String xmlIndicePlaca) {
		this.xmlIndicePlaca = xmlIndicePlaca;
	}

	/**
	 * @return the xmlRangos
	 */
	public String getXmlRangos() {
		return xmlRangos;
	}

	/**
	 * @param xmlRangos the xmlRangos to set
	 */
	public void setXmlRangos(String xmlRangos) {
		this.xmlRangos = xmlRangos;
	}

	/**
	 * @return the activoDienteAdulto
	 */
	public String getActivoDienteAdulto() {
		return activoDienteAdulto;
	}

	/**
	 * @param activoDienteAdulto the activoDienteAdulto to set
	 */
	public void setActivoDienteAdulto(String activoDienteAdulto) {
		this.activoDienteAdulto = activoDienteAdulto;
	}

	/**
	 * @return the activoDienteNino
	 */
	public String getActivoDienteNino() {
		return activoDienteNino;
	}

	/**
	 * @param activoDienteNino the activoDienteNino to set
	 */
	public void setActivoDienteNino(String activoDienteNino) {
		this.activoDienteNino = activoDienteNino;
	}

	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @return the edadPaciente
	 */
	public int getEdadPaciente() {
		return edadPaciente;
	}

	/**
	 * @param edadPaciente the edadPaciente to set
	 */
	public void setEdadPaciente(int edadPaciente) {
		this.edadPaciente = edadPaciente;
	}

	/**
	 * @return the forward
	 */
	public String getForward() {
		return forward;
	}

	/**
	 * @param forward the forward to set
	 */
	public void setForward(String forward) {
		this.forward = forward;
	}

	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public String getPorActualizar() {
		return porActualizar;
	}

	public void setPorActualizar(String porActualizar) {
		this.porActualizar = porActualizar;
	}

}