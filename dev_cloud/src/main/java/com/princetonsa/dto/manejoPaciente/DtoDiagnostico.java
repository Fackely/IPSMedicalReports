/**
 * 
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import util.ConstantesBD;
import util.UtilidadTexto;

/**
 * @author armando
 */
public class DtoDiagnostico implements Serializable
{
	/** */
	private static final long serialVersionUID = -136190275067584614L;

	/** * tipo cie Diagnostico */
	private String tipoCieDiagnostico;
	
	/** * tipo cie Diagnostico (Int)*/
	private Integer tipoCieDiagnosticoInt;
	
	/** * Acrónimo */
	private String acronimoDiagnostico;
	
	/** * Descripción */
	private String descripcionDiagnostico;
	
	/** */
	private String nombreDiagnostico;
	
	
	/** * Cuenta */
	private Integer idCuenta;
	
	
	/** * Ingreso */
	private Integer idIngreso;
	
	
	/** * Nombre formateado listo para mostrar */
	private String nombreCompletoDiagnostico;
	
	private boolean checkDiagRelacionado;
	
	private int codigoDxPreoperatorio;
	
	private int codigoDxPostOperatorio;
	
	/** Constructor de la clase * */
	public DtoDiagnostico()
	{
		this.tipoCieDiagnosticoInt		= null;
		this.nombreDiagnostico			= "";
		this.tipoCieDiagnostico			= "";		
		this.acronimoDiagnostico		= "";
		this.descripcionDiagnostico		= "";
		this.idCuenta					= null;
		this.idIngreso					= null;
		this.nombreCompletoDiagnostico 	= null;
		this.checkDiagRelacionado		= true;
	}

	
	/**
	 * Organiza el nombre completo del diagnostico para mostrar en el formato:
	 * (ACRONIMO - TIPO_CIE) NOMBRE 
	 * 
	 * @autor Cristhian Murillo
	 */
	public void organizarNombreCompletoDiagnostico(){
		nombreCompletoDiagnostico = acronimoDiagnostico	+ "-" + tipoCieDiagnosticoInt + " " + nombreDiagnostico;
	}
	
	
	/**
	 * @return valor de tipoCieDiagnostico
	 */
	public String getTipoCieDiagnostico() {
		return tipoCieDiagnostico;
	}

	public String getTipoCieDiagnosticoConCIE() 
	{
		if(UtilidadTexto.isEmpty(tipoCieDiagnostico))
			return tipoCieDiagnostico;
		else
			return "CIE "+tipoCieDiagnostico;
	}
	
	/**
	 * @param tipoCieDiagnostico el tipoCieDiagnostico para asignar
	 */
	public void setTipoCieDiagnostico(String tipoCieDiagnostico) {
		this.tipoCieDiagnostico = tipoCieDiagnostico;
	}


	/**
	 * @return valor de acronimoDiagnostico
	 */
	public String getAcronimoDiagnostico() {
		return acronimoDiagnostico;
	}

	public void setAcronimoDiagnostico(String acronimoDiagnostico) {
		this.acronimoDiagnostico = acronimoDiagnostico;
	}

	public String getDescripcionDiagnostico() {
		return descripcionDiagnostico;
	}

	public void setDescripcionDiagnostico(String descripcionDiagnostico) {
		this.descripcionDiagnostico = descripcionDiagnostico;
	}

	/**
	 * @return the nombreDiagnostico
	 */
	public String getNombreDiagnostico() {
		return nombreDiagnostico;
	}

	/**
	 * @param nombreDiagnostico the nombreDiagnostico to set
	 */
	public void setNombreDiagnostico(String nombreDiagnostico) {
		this.nombreDiagnostico = nombreDiagnostico;
	}

	
	/**
	 * @return valor de tipoCieDiagnosticoInt
	 */
	public Integer getTipoCieDiagnosticoInt() {
		if(this.tipoCieDiagnosticoInt == null){
			return Integer.parseInt(this.tipoCieDiagnostico);
		}
		else{
			return this.tipoCieDiagnosticoInt;
		}
	}


	/**
	 * @param tipoCieDiagnosticoInt el tipoCieDiagnosticoInt para asignar
	 */
	public void setTipoCieDiagnosticoInt(Integer tipoCieDiagnosticoInt) {
		this.tipoCieDiagnosticoInt = tipoCieDiagnosticoInt;
		this.tipoCieDiagnostico = this.tipoCieDiagnosticoInt+"";
	}


	/**
	 * @return valor de idCuenta
	 */
	public Integer getIdCuenta() {
		return idCuenta;
	}


	/**
	 * @param idCuenta el idCuenta para asignar
	 */
	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}


	/**
	 * @return valor de idIngreso
	 */
	public Integer getIdIngreso() {
		return idIngreso;
	}


	/**
	 * @param idIngreso el idIngreso para asignar
	 */
	public void setIdIngreso(Integer idIngreso) {
		this.idIngreso = idIngreso;
	}


	/**
	 * @return valor de nombreCompletoDiagnostico
	 */
	public String getNombreCompletoDiagnostico() {
		return nombreCompletoDiagnostico;
	}


	/**
	 * @param nombreCompletoDiagnostico el nombreCompletoDiagnostico para asignar
	 */
	public void setNombreCompletoDiagnostico(String nombreCompletoDiagnostico) {
		this.nombreCompletoDiagnostico = nombreCompletoDiagnostico;
	}

	public void asignarValoresSeparados(String idDiagnostico){
		String[] valores = idDiagnostico.split(ConstantesBD.separadorSplit);
		this.acronimoDiagnostico = valores[0];
		this.tipoCieDiagnosticoInt = Integer.parseInt(valores[1]);
		this.nombreDiagnostico =  valores[2];
	}

	public boolean isCheckDiagRelacionado() {
		return checkDiagRelacionado;
	}
	public void setCheckDiagRelacionado(boolean checkDiagRelacionado) {
		this.checkDiagRelacionado = checkDiagRelacionado;
	}


	/**
	 * @return the codigoDxPreoperatorio
	 */
	public int getCodigoDxPreoperatorio() {
		return codigoDxPreoperatorio;
	}


	/**
	 * @param codigoDxPreoperatorio the codigoDxPreoperatorio to set
	 */
	public void setCodigoDxPreoperatorio(int codigoDxPreoperatorio) {
		this.codigoDxPreoperatorio = codigoDxPreoperatorio;
	}


	/**
	 * @return the codigoDxPostOperatorio
	 */
	public int getCodigoDxPostOperatorio() {
		return codigoDxPostOperatorio;
	}


	/**
	 * @param codigoDxPostOperatorio the codigoDxPostOperatorio to set
	 */
	public void setCodigoDxPostOperatorio(int codigoDxPostOperatorio) {
		this.codigoDxPostOperatorio = codigoDxPostOperatorio;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public DtoDiagnostico clone() throws CloneNotSupportedException {
		DtoDiagnostico diagnostico=new DtoDiagnostico();
		diagnostico.setAcronimoDiagnostico(this.getAcronimoDiagnostico());
		diagnostico.setCheckDiagRelacionado(this.isCheckDiagRelacionado());
		diagnostico.setDescripcionDiagnostico(this.getDescripcionDiagnostico());
		diagnostico.setIdCuenta(this.getIdCuenta());
		diagnostico.setIdIngreso(this.getIdIngreso());
		diagnostico.setNombreCompletoDiagnostico(this.getNombreCompletoDiagnostico());
		diagnostico.setNombreDiagnostico(this.getNombreDiagnostico());
		diagnostico.setTipoCieDiagnostico(this.getTipoCieDiagnostico());
		diagnostico.setTipoCieDiagnosticoInt(this.getTipoCieDiagnosticoInt());
		diagnostico.setCodigoDxPostOperatorio(this.getCodigoDxPostOperatorio());
		diagnostico.setCodigoDxPreoperatorio(this.getCodigoDxPreoperatorio());
		
		return diagnostico;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((acronimoDiagnostico == null) ? 0 : acronimoDiagnostico
						.hashCode());
		result = prime * result + (checkDiagRelacionado ? 1231 : 1237);
		result = prime * result + codigoDxPostOperatorio;
		result = prime * result + codigoDxPreoperatorio;
		result = prime
				* result
				+ ((descripcionDiagnostico == null) ? 0
						: descripcionDiagnostico.hashCode());
		result = prime * result
				+ ((idCuenta == null) ? 0 : idCuenta.hashCode());
		result = prime * result
				+ ((idIngreso == null) ? 0 : idIngreso.hashCode());
		result = prime
				* result
				+ ((nombreCompletoDiagnostico == null) ? 0
						: nombreCompletoDiagnostico.hashCode());
		result = prime
				* result
				+ ((nombreDiagnostico == null) ? 0 : nombreDiagnostico
						.hashCode());
		result = prime
				* result
				+ ((tipoCieDiagnostico == null) ? 0 : tipoCieDiagnostico
						.hashCode());
		result = prime
				* result
				+ ((tipoCieDiagnosticoInt == null) ? 0 : tipoCieDiagnosticoInt
						.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DtoDiagnostico)) {
			return false;
		}
		DtoDiagnostico other = (DtoDiagnostico) obj;
		if (acronimoDiagnostico == null) {
			if (other.acronimoDiagnostico != null) {
				return false;
			}
		} else if (!acronimoDiagnostico.equals(other.acronimoDiagnostico)) {
			return false;
		}
		if (checkDiagRelacionado != other.checkDiagRelacionado) {
			return false;
		}
		if (descripcionDiagnostico == null) {
			if (other.descripcionDiagnostico != null) {
				return false;
			}
		} else if (!descripcionDiagnostico.equals(other.descripcionDiagnostico)) {
			return false;
		}
		if (idCuenta == null) {
			if (other.idCuenta != null) {
				return false;
			}
		} else if (!idCuenta.equals(other.idCuenta)) {
			return false;
		}
		if (idIngreso == null) {
			if (other.idIngreso != null) {
				return false;
			}
		} else if (!idIngreso.equals(other.idIngreso)) {
			return false;
		}
		if (nombreCompletoDiagnostico == null) {
			if (other.nombreCompletoDiagnostico != null) {
				return false;
			}
		} else if (!nombreCompletoDiagnostico
				.equals(other.nombreCompletoDiagnostico)) {
			return false;
		}
		if (nombreDiagnostico == null) {
			if (other.nombreDiagnostico != null) {
				return false;
			}
		} else if (!nombreDiagnostico.equals(other.nombreDiagnostico)) {
			return false;
		}
		if (tipoCieDiagnostico == null) {
			if (other.tipoCieDiagnostico != null) {
				return false;
			}
		} else if (!tipoCieDiagnostico.equals(other.tipoCieDiagnostico)) {
			return false;
		}
		if (tipoCieDiagnosticoInt == null) {
			if (other.tipoCieDiagnosticoInt != null) {
				return false;
			}
		} else if (!tipoCieDiagnosticoInt.equals(other.tipoCieDiagnosticoInt)) {
			return false;
		}
		return true;
	}


	
	
}
