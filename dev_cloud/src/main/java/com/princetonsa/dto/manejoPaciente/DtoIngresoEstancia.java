package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

import com.princetonsa.dto.capitacion.DTOPacienteCapitado;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;

/**
 * Dto encargado de almacenar los 
 * datos de Ingreso Estancia
 * @author Diana Carolina G
 * @since 17/11/2010
 */
public class DtoIngresoEstancia implements Serializable {
	
	/** * Serial  */
	private static final long serialVersionUID = 1L;
	
	/** * Llave primaria  */
	private long codigoPk;
	
	// Datos Paciente -----------------------
	private DTOPacienteCapitado dtoPaciente;
	// ---------------------------------------
	
	// Datos Admisi&oacute;n Paciente----------------
	private Date fechaAdmision;
	private String horaAdmision;
	private String viaIngreso;
	private int codigoViaIngreso;
	
	private DtoDiagnostico dxPrincipal;
	private DtoDiagnostico dxComplicacion;
		
	private String medicoSolicitante;
	private String observaciones;
	private String insitucionAutorizada;
	// ---------------------------------------
	
	/**
	 * Atributo que determina la entidad a recobrar
	 */
	private int codigoConvenioRecobro;
	
	/**
	 * Atributo que almacena el nombre del convenio de la entidad de 
	 * recobro, si esta no existe como convenio en el sistema
	 */
	private String otroConvenioRecobro;
	
	/**
	 * Entidad Subcontratada
	 */
	private DtoEntidadSubcontratada entidadSubcontratada;
		
	
	
	public DtoIngresoEstancia(){
		this.codigoPk=ConstantesBD.codigoNuncaValidoLong;
		this.fechaAdmision						=	null;
		this.horaAdmision						=	"";
		this.viaIngreso							=	"";
		this.medicoSolicitante					=	"";
		this.observaciones						=	"";
		this.entidadSubcontratada               = new DtoEntidadSubcontratada();
		this.dxPrincipal						= new DtoDiagnostico();
		this.dxComplicacion						= new DtoDiagnostico();
		this.dtoPaciente                        = new DTOPacienteCapitado();
		this.entidadSubcontratada               = new DtoEntidadSubcontratada();
		this.codigoViaIngreso                   = ConstantesBD.codigoNuncaValido;
	}

	/**
	 * constructor
	 */
	public long getCodigoPk() {
		return codigoPk;
	}


	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}
	
	public Date getFechaAdmision() {
		return fechaAdmision;
	}

	public void setFechaAdmision(Date fechaAdmision) {
		this.fechaAdmision = fechaAdmision;
	}
	public String getHoraAdmision() {
		return horaAdmision;
	}

	public void setHoraAdmision(String horaAdmision) {
		this.horaAdmision = horaAdmision;
	}

	public String getViaIngreso() {
		return viaIngreso;
	}


	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}



	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoConvenioRecobro
	
	 * @return retorna la variable codigoConvenioRecobro 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoConvenioRecobro() {
		return codigoConvenioRecobro;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoConvenioRecobro
	
	 * @param valor para el atributo codigoConvenioRecobro 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoConvenioRecobro(int codigoConvenioRecobro) {
		this.codigoConvenioRecobro = codigoConvenioRecobro;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo otroConvenioRecobro
	
	 * @return retorna la variable otroConvenioRecobro 
	 * @author Angela Maria Aguirre 
	 */
	public String getOtroConvenioRecobro() {
		return otroConvenioRecobro;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo otroConvenioRecobro
	
	 * @param valor para el atributo otroConvenioRecobro 
	 * @author Angela Maria Aguirre 
	 */
	public void setOtroConvenioRecobro(String otroConvenioRecobro) {
		this.otroConvenioRecobro = otroConvenioRecobro;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo entidadSubcontratada
	
	 * @return retorna la variable entidadSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public DtoEntidadSubcontratada getEntidadSubcontratada() {
		return entidadSubcontratada;
	}

	public DTOPacienteCapitado getDtoPaciente() {
		return dtoPaciente;
	}

	public void setDtoPaciente(DTOPacienteCapitado dtoPaciente) {
		this.dtoPaciente = dtoPaciente;
	}

	public DtoDiagnostico getDxPrincipal() {
		return dxPrincipal;
	}

	public void setDxPrincipal(DtoDiagnostico dxPrincipal) {
		this.dxPrincipal = dxPrincipal;
	}

	public DtoDiagnostico getDxComplicacion() {
		return dxComplicacion;
	}

	public void setDxComplicacion(DtoDiagnostico dxComplicacion) {
		this.dxComplicacion = dxComplicacion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo entidadSubcontratada
	
	 * @param valor para el atributo entidadSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public void setEntidadSubcontratada(DtoEntidadSubcontratada entidadSubcontratada) {
		this.entidadSubcontratada = entidadSubcontratada;
	}

	public String getInsitucionAutorizada() {
		return insitucionAutorizada;
	}

	public void setInsitucionAutorizada(String insitucionAutorizada) {
		this.insitucionAutorizada = insitucionAutorizada;
	}

	/**
	 * @return the codigoViaIngreso
	 */
	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}

	public String getMedicoSolicitante() {
		return medicoSolicitante;
	}

	public void setMedicoSolicitante(String medicoSolicitante) {
		this.medicoSolicitante = medicoSolicitante;
	}


		
}
