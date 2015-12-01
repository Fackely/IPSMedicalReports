/**
 * 
 */
package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.inventario.DtoArticulos;
import com.princetonsa.dto.manejoPaciente.DTOProcesoAutorizacion;
import com.princetonsa.mundo.PersonaBasica;
import com.servinte.axioma.orm.CentrosCosto;

/**
 * Esta clase se encarga de manejar la informaci&oacute;n para generar
 * la autorizaci&oacute;n de capitaci&oacute;n subcontratada para ordenes ambulatorias,
 * y peticiones Qx. 
 * 
 * @author Diana Ruiz
 * @since 21/06/2011
 *
 */

public class DtoValidacionGeneracionAutorizacionCapitada implements Serializable
{
	
	/** */
	private static final long serialVersionUID = 1L;

	/** Atributo para almacenar la orden sea ambulatoria o peticiones*/
	private Integer OrdenAmbulatoria;	
	
	/** Atributo para almacenar el consecutivo orden sea ambulatoria o peticiones */
	private String consecutivoOrden;
			
	/** Array para almacenar los servicios*/
	private ArrayList<DtoServicios> servicios; 
	
	/** Array para almacenar los codigos de articulos*/
	private ArrayList<DtoArticulos> articulos;
	
	/** Atributo para almacenar el convenio responsable del paciente*/
	private Integer Convenio;
	
	/** Paciente*/
	private PersonaBasica paciente; 
	
	/**Atributo para almacenar el c&oacute;digo de la vía de Ingreso*/
	private String ViaIngreso;
	
	/**Atributo para almacenar el c&oacute;digo de la Entidad Subcontratada que se busca en el proceso 966*/
	private Long EntidadSubcontratada;	
	
	/**Atributo para almacenar el c&oacute;digo del centro de costo que responde*/
	private int CentroCostoResponde;	
	
	/**Atributo para almacenar el acronimo del diagnostico*/
	private String acronimoDiagnostico;
	
	/**Atributo para almacernar el c&oacute;digo del centro de costo*/
	private CentrosCosto centrosCostoSolicitante;
		
	/** Contiene un listado de advertencias a mostrar */
	private ArrayList<String> listaAdvertencias = new ArrayList<String>();
	
	/** Contiene la informaci&oacute;n del proceso 1106 */
	private DTOProcesoAutorizacion procesoAutorizacion;
	
	/**	Contiene la informaci&oacute;n de la entidad subcontratada que se autoriza en el proceso 1106*/
	private DtoEntidadSubcontratada dtoentidadSubcontratada;
	
	/** Contiene la descripci&oacute;n del convenio responsable*/
	private String descripcionConvenioResponsable;
	
	/** Contiene la descripci&oacute;n del tipo de contrato del convenio responsable*/
	private String tipoContrato;
	
	/** Contiene el tipo de orden que se autoriza(Ambulatoria-Petici&oacute;n)	 */
	public String tipoOrden;	
	
	/**
	 * Atributo que permite almacenar el contrato del convenio responsable de la orden ambulatoria
	 */
	public int contratoConvenioResponsable;
	
	/**
	 * Atributo que permite indicar si se autoriza o no la orden
	 */
	private boolean ordenAutorizada;
	
	/**
	 * Atributo que permite identificar si la orden es de pyp
	 */
	private boolean pyp;
	
	/**
	 * Atributo que almacena el codigo del ingreso
	 */
	private int codIngreso; 
	
	/**
	 * Atributo que permite almacenar el centro de atención que genera la autorizacion 
	 */
	private int centroAtencion;
	
	/**
	 * Atributo que almacena si la orden de medicamentos es urgente
	 */
	private boolean ordenMedicamentoUrgente;
	
	
	/**Constructor de la clase */
	public DtoValidacionGeneracionAutorizacionCapitada()
	{
		this.reset();		
	}
	
	
	private void reset()
	{
		this.setOrdenAmbulatoria(null);
		this.setServicios(new ArrayList<DtoServicios>());
		this.setArticulos(new ArrayList<DtoArticulos>());
		this.setConvenio(null);
		this.setPaciente(new PersonaBasica());
		this.setViaIngreso(null);
		this.setEntidadSubcontratada(null);
		this.setCentroCostoResponde(0);
		this.setAcronimoDiagnostico("");
		this.setCentrosCostoSolicitante(new CentrosCosto());
		this.setListaAdvertencias(new ArrayList<String>());
		this.setProcesoAutorizacion(new DTOProcesoAutorizacion());
		this.setDtoentidadSubcontratada(new DtoEntidadSubcontratada());
		this.setDescripcionConvenioResponsable("");
		this.setTipoContrato("");
		this.setTipoOrden("");
		this.setConsecutivoOrden("");
		
	}
	


	public void setOrdenAmbulatoria(Integer ordenAmbulatoria) {
		OrdenAmbulatoria = ordenAmbulatoria;
	}


	public Integer getOrdenAmbulatoria() {
		return OrdenAmbulatoria;
	}

	public void setConvenio(Integer convenio) {
		Convenio = convenio;
	}


	public Integer getConvenio() {
		return Convenio;
	}


	public void setPaciente(PersonaBasica paciente) {
		this.paciente = paciente;
	}


	public PersonaBasica getPaciente() {
		return paciente;
	}	

	public void setViaIngreso(String viaIngreso) {
		ViaIngreso = viaIngreso;
	}


	public String getViaIngreso() {
		return ViaIngreso;
	}


	public void setEntidadSubcontratada(Long entidadSubcontratada) {
		EntidadSubcontratada = entidadSubcontratada;
	}


	public Long getEntidadSubcontratada() {
		return EntidadSubcontratada;
	}


	public void setCentroCostoResponde(int centroCostoResponde) {
		CentroCostoResponde = centroCostoResponde;
	}


	public int getCentroCostoResponde() {
		return CentroCostoResponde;
	}


	public void setServicios(ArrayList<DtoServicios> servicios) {
		this.servicios = servicios;
	}


	public ArrayList<DtoServicios> getServicios() {
		return servicios;
	}


	public void setArticulos(ArrayList<DtoArticulos> articulos) {
		this.articulos = articulos;
	}


	public ArrayList<DtoArticulos> getArticulos() {
		return articulos;
	}


	public void setAcronimoDiagnostico(String acronimoDiagnostico) {
		this.acronimoDiagnostico = acronimoDiagnostico;
	}


	public String getAcronimoDiagnostico() {
		return acronimoDiagnostico;
	}


	public void setCentrosCostoSolicitante(CentrosCosto centrosCostoSolicitante) {
		this.centrosCostoSolicitante = centrosCostoSolicitante;
	}


	public CentrosCosto getCentrosCostoSolicitante() {
		return centrosCostoSolicitante;
	}


	public void setListaAdvertencias(ArrayList<String> listaAdvertencias) {
		this.listaAdvertencias = listaAdvertencias;
	}


	public ArrayList<String> getListaAdvertencias() {
		return listaAdvertencias;
	}


	public void setProcesoAutorizacion(DTOProcesoAutorizacion procesoAutorizacion) {
		this.procesoAutorizacion = procesoAutorizacion;
	}


	public DTOProcesoAutorizacion getProcesoAutorizacion() {
		return procesoAutorizacion;
	}


	public void setDtoentidadSubcontratada(DtoEntidadSubcontratada dtoentidadSubcontratada) {
		this.dtoentidadSubcontratada = dtoentidadSubcontratada;
	}


	public DtoEntidadSubcontratada getDtoentidadSubcontratada() {
		return dtoentidadSubcontratada;
	}


	public void setDescripcionConvenioResponsable(
			String descripcionConvenioResponsable) {
		this.descripcionConvenioResponsable = descripcionConvenioResponsable;
	}


	public String getDescripcionConvenioResponsable() {
		return descripcionConvenioResponsable;
	}


	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}


	public String getTipoContrato() {
		return tipoContrato;
	}


	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}


	public String getTipoOrden() {
		return tipoOrden;
	}


	public void setConsecutivoOrden(String consecutivoOrden) {
		this.consecutivoOrden = consecutivoOrden;
	}


	public String getConsecutivoOrden() {
		return consecutivoOrden;
	}
	
	
	public void setContratoConvenioResponsable(int contratoConvenioResponsable) {
		this.contratoConvenioResponsable = contratoConvenioResponsable;
	}


	public int getContratoConvenioResponsable() {
		return contratoConvenioResponsable;
	}

	public void setOrdenAutorizada(boolean ordenAutorizada) {
		this.ordenAutorizada = ordenAutorizada;
	}


	public boolean isOrdenAutorizada() {
		return ordenAutorizada;
	}


	public void setPyp(boolean pyp) {
		this.pyp = pyp;
	}


	public boolean isPyp() {
		return pyp;
	}


	public int getCodIngreso() {
		return codIngreso;
	}


	public void setCodIngreso(int codIngreso) {
		this.codIngreso = codIngreso;
	}


	public int getCentroAtencion() {
		return centroAtencion;
	}


	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	public boolean isOrdenMedicamentoUrgente() {
		return ordenMedicamentoUrgente;
	}


	public void setOrdenMedicamentoUrgente(boolean ordenMedicamentoUrgente) {
		this.ordenMedicamentoUrgente = ordenMedicamentoUrgente;
	}
	
}
