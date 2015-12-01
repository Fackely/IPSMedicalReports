package com.servinte.axioma.dto.tesoreria;

import com.servinte.axioma.orm.ConcNotaPacCuentaCont;

public class DtoConcNotaPacCuentaCont extends ConcNotaPacCuentaCont{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long codigoCuentasContables;
	private long codigoInstituciones;
	private long codigoConceptoNotaPaciente;
	private long codigoEmpresasInstitucion;
	
	
	public DtoConcNotaPacCuentaCont() {
		super();
	}
	
	public DtoConcNotaPacCuentaCont(long codigoCuentasContables,
			long codigoInstituciones, long codigoConceptoNotaPaciente,
			long codigoEmpresasInstitucion) {
		super();
		this.codigoCuentasContables = codigoCuentasContables;
		this.codigoInstituciones = codigoInstituciones;
		this.codigoConceptoNotaPaciente = codigoConceptoNotaPaciente;
		this.codigoEmpresasInstitucion = codigoEmpresasInstitucion;
	}

	public long getCodigoCuentasContables() {
		return codigoCuentasContables;
	}

	public void setCodigoCuentasContables(long codigoCuentasContables) {
		this.codigoCuentasContables = codigoCuentasContables;
	}

	public long getCodigoInstituciones() {
		return codigoInstituciones;
	}

	public void setCodigoInstituciones(long codigoInstituciones) {
		this.codigoInstituciones = codigoInstituciones;
	}

	public long getCodigoConceptoNotaPaciente() {
		return codigoConceptoNotaPaciente;
	}

	public void setCodigoConceptoNotaPaciente(long codigoConceptoNotaPaciente) {
		this.codigoConceptoNotaPaciente = codigoConceptoNotaPaciente;
	}

	public long getCodigoEmpresasInstitucion() {
		return codigoEmpresasInstitucion;
	}

	public void setCodigoEmpresasInstitucion(long codigoEmpresasInstitucion) {
		this.codigoEmpresasInstitucion = codigoEmpresasInstitucion;
	}
	
}
