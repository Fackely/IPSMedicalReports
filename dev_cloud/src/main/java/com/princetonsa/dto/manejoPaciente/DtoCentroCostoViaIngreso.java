package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import util.ConstantesBD;

/**
 * Clase encargada de contener los datos
 * de la entidad centro_costo_via_ingreso
 * @author Diana Carolina G
 */
public class DtoCentroCostoViaIngreso implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer codigoCentroCostoViaIngreso;
	private Integer	codigoCentroCosto; 
	private String nombreCentroCosto;
	private Integer codigoViaIngreso;
	private String nombreViaIngreso;
	private String acronimoTipoPaciente;
	private Integer institucion;
	
	/**
	 * Constructor
	 */
	public  DtoCentroCostoViaIngreso(){
		this.codigoCentroCostoViaIngreso	=	ConstantesBD.codigoNuncaValido;
		this.codigoCentroCosto				=	ConstantesBD.codigoNuncaValido;
		this.nombreCentroCosto 				= 	"";
		this.codigoViaIngreso				=	ConstantesBD.codigoNuncaValido;
		this.nombreViaIngreso				=	"";
		this.institucion					=	ConstantesBD.codigoNuncaValido;
		this.acronimoTipoPaciente			=	"";
		
		
	}

	public Integer getCodigoCentroCostoViaIngreso() {
		return codigoCentroCostoViaIngreso;
	}

	public void setCodigoCentroCostoViaIngreso(Integer codigoCentroCostoViaIngreso) {
		this.codigoCentroCostoViaIngreso = codigoCentroCostoViaIngreso;
	}

	public Integer getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	public void setCodigoCentroCosto(Integer codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}

	public String getNombreCentroCosto() {
		return nombreCentroCosto;
	}

	public void setNombreCentroCosto(String nombreCentroCosto) {
		this.nombreCentroCosto = nombreCentroCosto;
	}

	public Integer getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	public void setCodigoViaIngreso(Integer codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}

	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}

	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}

	public void setAcronimoTipoPaciente(String acronimoTipoPaciente) {
		this.acronimoTipoPaciente = acronimoTipoPaciente;
	}

	public String getAcronimoTipoPaciente() {
		return acronimoTipoPaciente;
	}

	public Integer getInstitucion() {
		return institucion;
	}

	public void setInstitucion(Integer institucion) {
		this.institucion = institucion;
	}

	
}
