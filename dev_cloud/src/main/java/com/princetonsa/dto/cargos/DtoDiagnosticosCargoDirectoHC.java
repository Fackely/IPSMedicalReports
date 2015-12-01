package com.princetonsa.dto.cargos;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;

import com.princetonsa.mundo.atencion.Diagnostico;

/**
 * Data Transfer Object: que maneja los diagnosticos de la informacion de historia clínica
 * en los cargos directos
 * @author Sebastián Gómez
 *
 */
public class DtoDiagnosticosCargoDirectoHC implements Serializable
{
	/**
	 * Consecutivo del registro de diagnósticos
	 */
	private String codigo;
	
	/**
	 * Referencia a la tabla cargos_directos_hc
	 */
	private String codigoCargoDirecto;
	
	/**
	 * Diagnostico
	 */
	private Diagnostico diagnostico;
	
	/**
	 * Tipo de diagnóstico
	 */
	private InfoDatos tipoDiagnostico;
	
	/**
	 * Para saber si el diagnóstico es principal
	 */
	private boolean principal;
	
	/**
	 * Para saber si el diagnóstico es de complicación
	 */
	private boolean complicacion;
	
	/**
	 * Constructor
	 *
	 */
	public DtoDiagnosticosCargoDirectoHC()
	{
		this.clean();
	}
	
	/**
	 * Inicializa los datos
	 *
	 */
	private void clean() 
	{
		this.codigo = "";
		this.codigoCargoDirecto = "";
		this.diagnostico = new Diagnostico("",ConstantesBD.codigoNuncaValido);
		this.tipoDiagnostico = new InfoDatos("","");
		this.principal = false;
		this.complicacion = false;
		
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoCargoDirecto
	 */
	public String getCodigoCargoDirecto() {
		return codigoCargoDirecto;
	}

	/**
	 * @param codigoCargoDirecto the codigoCargoDirecto to set
	 */
	public void setCodigoCargoDirecto(String codigoCargoDirecto) {
		this.codigoCargoDirecto = codigoCargoDirecto;
	}

	/**
	 * @return the complicacion
	 */
	public boolean isComplicacion() {
		return complicacion;
	}

	/**
	 * @param complicacion the complicacion to set
	 */
	public void setComplicacion(boolean complicacion) {
		this.complicacion = complicacion;
	}

	/**
	 * @return the diagnostico
	 */
	public Diagnostico getDiagnostico() {
		return diagnostico;
	}

	/**
	 * @param diagnostico the diagnostico to set
	 */
	public void setDiagnostico(Diagnostico diagnostico) {
		this.diagnostico = diagnostico;
	}
	
	
	/**
	 * @return the diagnostico
	 */
	public String getAcronimoDiagnostico() {
		return diagnostico.getAcronimo();
	}

	/**
	 * @param diagnostico the diagnostico to set
	 */
	public void setAcronimoDiagnostico(String diagnostico) {
		this.diagnostico.setAcronimo(diagnostico);
	}
	
	/**
	 * @return the diagnostico
	 */
	public int getCieDiagnostico() {
		return diagnostico.getTipoCIE();
	}

	/**
	 * @param diagnostico the diagnostico to set
	 */
	public void setCieDiagnostico(int diagnostico) {
		this.diagnostico.setTipoCIE(diagnostico);
	}
	
	
	/**
	 * Método para asignar el nombre del diagnostico
	 * @param nombre
	 */
	public void setNombreDiagnostico(String nombre)
	{
		this.diagnostico.setNombre(nombre);
	}
	
	/**
	 * Método para obtener el nombre del diagnostico
	 * @return
	 */
	public String getNombreDiagnostico()
	{
		return this.diagnostico.getNombre();
	}

	/**
	 * @return the principal
	 */
	public boolean isPrincipal() {
		return principal;
	}

	/**
	 * @param principal the principal to set
	 */
	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	/**
	 * @return the tipoDiagnostico
	 */
	public InfoDatos getTipoDiagnostico() {
		return tipoDiagnostico;
	}

	/**
	 * @param tipoDiagnostico the tipoDiagnostico to set
	 */
	public void setTipoDiagnostico(InfoDatos tipoDiagnostico) {
		this.tipoDiagnostico = tipoDiagnostico;
	}
	
	/**
	 * @return the tipoDiagnostico
	 */
	public String getCodigoTipoDiagnostico() {
		return tipoDiagnostico.getId();
	}

	/**
	 * @param tipoDiagnostico the tipoDiagnostico to set
	 */
	public void setCodigoTipoDiagnostico(String tipoDiagnostico) {
		this.tipoDiagnostico.setId(tipoDiagnostico);
	}
	
	/**
	 * @return the tipoDiagnostico
	 */
	public String getNombreTipoDiagnostico() {
		return tipoDiagnostico.getValue();
	}

	/**
	 * @param tipoDiagnostico the tipoDiagnostico to set
	 */
	public void setNombreTipoDiagnostico(String tipoDiagnostico) {
		this.tipoDiagnostico.setValue(tipoDiagnostico);
	}
}
