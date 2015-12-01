package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosInt;

public class DtoOdontograma implements Serializable
{
	
	private Double codigoPk;
	private Double consecutivo;
	private int codigoPaciente;
	private InfoDatosInt ingreso;
	private int valoracion;
	private String indicativo;
	private Double evolucion;
	private int institucion;
	private InfoDatosInt centroAtencion;
	private DtoInfoFechaUsuario usuarioModifica;
	private String especialidadesMedico;
	private String imagen;
	
	private DTOHistoricoOdontograma historico;
	
	
	public DtoOdontograma()
	{
		reset();
	}
	
	public void reset()
	{
		codigoPk = ConstantesBD.codigoNuncaValidoDouble;
		consecutivo = ConstantesBD.codigoNuncaValidoDouble;
		codigoPaciente = ConstantesBD.codigoNuncaValido;
		ingreso = new InfoDatosInt();
		valoracion = ConstantesBD.codigoNuncaValido;
		indicativo = "";
		evolucion = ConstantesBD.codigoNuncaValidoDouble;
		institucion = ConstantesBD.codigoNuncaValido;
		centroAtencion = new InfoDatosInt();
		usuarioModifica = new DtoInfoFechaUsuario();
		this.especialidadesMedico = "";
		this.imagen="";
		this.historico=null;
		
	}

	public Double getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(Double codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Double getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Double consecutivo) {
		this.consecutivo = consecutivo;
	}

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	

	public int getValoracion() {
		return valoracion;
	}

	public void setValoracion(int valoracion) {
		this.valoracion = valoracion;
	}

	public String getIndicativo() {
		return indicativo;
	}

	public void setIndicativo(String indicativo) {
		this.indicativo = indicativo;
	}

	public Double getEvolucion() {
		return evolucion;
	}

	public void setEvolucion(Double evolucion) {
		this.evolucion = evolucion;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	

	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the ingreso
	 */
	public InfoDatosInt getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(InfoDatosInt ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the centroAtencion
	 */
	public InfoDatosInt getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(InfoDatosInt centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the especialidadesMedico
	 */
	public String getEspecialidadesMedico() {
		return especialidadesMedico;
	}

	/**
	 * @param especialidadesMedico the especialidadesMedico to set
	 */
	public void setEspecialidadesMedico(String especialidadesMedico) {
		this.especialidadesMedico = especialidadesMedico;
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
	 * Obtiene el valor del atributo historico
	 *
	 * @return Retorna atributo historico
	 */
	public DTOHistoricoOdontograma getHistorico()
	{
		return historico;
	}

	/**
	 * Establece el valor del atributo historico
	 *
	 * @param valor para el atributo historico
	 */
	public void setHistorico(DTOHistoricoOdontograma historico)
	{
		this.historico = historico;
	}	
}