package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;
import util.UtilidadTexto;

/**
 * Dto que contiene informacion del responsable de la factura Odontologica 
 * @author axioma
 *
 */
public class DtoResponsableFacturaOdontologica implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * subcuenta 
	 */
	private BigDecimal subCuenta;
	
	/**
	 * convenio
	 */
	private InfoDatosInt convenio;
	
	/**
	 * contrato
	 */
	private InfoDatosInt contrato;

	/**
	 * lista de solicitudes
	 */
	private ArrayList<DtoSolicitudesResponsableFacturaOdontologica> listaSolicitudes;
	
	/**
	 * Paciente paga la atencion X monto cobro
	 */
	private boolean pacientePagaAtencionXMontoCobro;
	
	/**
	 * si el contrato controla anticipos
	 */
	private boolean controlaAnticipos;
	
	/**
	 * empresa institucion
	 */
	private double empresaInstitucion;
	
	/**
	 * entidad subcontratada
	 */
	private double entidadSubContratada;
	
	/**
	 * pac
	 */
	private double pacienteEntidadSubContratada;
	
	/**
	 * indica si el tipo regimen del convenio es particular
	 */
	private boolean esParticular;
	
	/**
	 * 
	 */
	private InfoDatosInt estratoSocial;
	
	/**
	 * 
	 */
	private DtoMontoCobroFactura montoCobro;
	
	/**
	 * 
	 */
	private String fechaVigenciaTopeCuenta;
	
	/**
	 * 
	 */
	private InfoDatosInt formatoImpresion;
	
	/**
	 * 
	 */
	private String prefijoFactura;
	
	/**
	 * 
	 */
	private String resolucion;
	
	/**
	 * 
	 */
	private InfoDatos tipoRegimen;
	
	/**
	 * 
	 */
	private InfoDatosInt viaIngreso;
	
	/**
	 * Nombre del responsable (empresa) del convenio
	 * 
	 */
	private String nombreResponsable;
	
	/**
	 * NIT del responsable
	 */
	private String nitResponsable;
	
	private String nombreSimpleConvenio;
	/**
	 * 
	 * @param subCuenta
	 * @param convenio
	 * @param contrato
	 */
	public DtoResponsableFacturaOdontologica() 
	{
		super();
		this.subCuenta = BigDecimal.ZERO;
		this.convenio = new InfoDatosInt();
		this.contrato = new InfoDatosInt();
		this.listaSolicitudes= new ArrayList<DtoSolicitudesResponsableFacturaOdontologica>();
		this.pacientePagaAtencionXMontoCobro= false;
		this.controlaAnticipos= false;
		this.empresaInstitucion= ConstantesBD.codigoNuncaValido;
		this.pacienteEntidadSubContratada= ConstantesBD.codigoNuncaValido;
		this.entidadSubContratada= ConstantesBD.codigoNuncaValido;
		this.esParticular= false;
		this.estratoSocial= new InfoDatosInt();
		this.montoCobro= new DtoMontoCobroFactura();
		this.fechaVigenciaTopeCuenta="";
		this.formatoImpresion= new InfoDatosInt();
		this.prefijoFactura="";
		this.resolucion="";
		this.tipoRegimen=new InfoDatos();
		this.viaIngreso= new InfoDatosInt();
		this.nombreResponsable = "";
		this.setNitResponsable("");
		this.setNombreSimpleConvenio("");
	}

	/**
	 * @return the subCuenta
	 */
	public BigDecimal getSubCuenta() {
		return subCuenta;
	}

	/**
	 * @param subCuenta the subCuenta to set
	 */
	public void setSubCuenta(BigDecimal subCuenta) {
		this.subCuenta = subCuenta;
	}

	/**
	 * @return the convenio
	 */
	public InfoDatosInt getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(InfoDatosInt convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the contrato
	 */
	public InfoDatosInt getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(InfoDatosInt contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return the listaSolicitudes
	 */
	public ArrayList<DtoSolicitudesResponsableFacturaOdontologica> getListaSolicitudes() {
		return listaSolicitudes;
	}

	/**
	 * @param listaSolicitudes the listaSolicitudes to set
	 */
	public void setListaSolicitudes(
			ArrayList<DtoSolicitudesResponsableFacturaOdontologica> listaSolicitudes) {
		this.listaSolicitudes = listaSolicitudes;
	}
	
	/**
	 * Valor neto (es decir que tiene dctos)
	 * @return
	 */
	public BigDecimal getValorTotalNetoCargosEstadoCargado()
	{
		BigDecimal valor= BigDecimal.ZERO;
		for(DtoSolicitudesResponsableFacturaOdontologica dto: this.getListaSolicitudes())
		{
			if(dto.getDetalleCargo().getEstado()==ConstantesBD.codigoEstadoFCargada)
			{	
				valor= valor.add(dto.getDetalleCargo().getValorTotalNeto());
			}	
		}
		return valor;
	}

	/**
	 * Valor neto (es decir que tiene dctos)
	 * @return
	 */
	public String getValorTotalNetoCargosEstadoCargadoFormateado()
	{
		return UtilidadTexto.formatearValores(this.getValorTotalNetoCargosEstadoCargado()+"");
	}
	
	/**
	 * Valor neto (es decir que tiene dctos)
	 * @return
	 */
	public BigDecimal getValorTotalBrutoCargosEstadoCargado()
	{
		BigDecimal valor= BigDecimal.ZERO;
		for(DtoSolicitudesResponsableFacturaOdontologica dto: this.getListaSolicitudes())
		{
			if(dto.getDetalleCargo().getEstado()==ConstantesBD.codigoEstadoFCargada)
			{	
				valor= valor.add(dto.getDetalleCargo().getValorTotalBrutoMasRecargos());
			}	
		}
		return valor;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getValorTotalBrutoCargosEstadoCargadoFormateado()
	{
		return UtilidadTexto.formatearValores(this.getValorTotalBrutoCargosEstadoCargado()+"");
	}
	
	/**
	 * Valor de la suma de todos los decuentos (comerciales - odontologicos - promociones - bonos) x la cantidad
	 * @return
	 */
	public BigDecimal getValorTotalDctosTodos()
	{
		BigDecimal valor= BigDecimal.ZERO;
		for(DtoSolicitudesResponsableFacturaOdontologica dto: this.getListaSolicitudes())
		{
			if(dto.getDetalleCargo().getEstado()==ConstantesBD.codigoEstadoFCargada)
			{	
				valor= valor.add(dto.getDetalleCargo().getValorTotalDctoTodos());
			}	
		}
		return valor;
	}
	
	public String getValorTotalDctosTodosFormateado()
	{
		return UtilidadTexto.formatearValores(this.getValorTotalDctosTodos()+"");
	}
	
	
	/**
	 * @return the pacientePagaAtencionXMontoCobro
	 */
	public boolean isPacientePagaAtencionXMontoCobro() {
		return pacientePagaAtencionXMontoCobro;
	}

	/**
	 * @return the pacientePagaAtencionXMontoCobro
	 */
	public boolean getPacientePagaAtencionXMontoCobro() {
		return pacientePagaAtencionXMontoCobro;
	}
	
	/**
	 * @param pacientePagaAtencionXMontoCobro the pacientePagaAtencionXMontoCobro to set
	 */
	public void setPacientePagaAtencionXMontoCobro(
			boolean pacientePagaAtencionXMontoCobro) {
		this.pacientePagaAtencionXMontoCobro = pacientePagaAtencionXMontoCobro;
	}

	/**
	 * @return the controlaAnticipos
	 */
	public boolean isControlaAnticipos() {
		return controlaAnticipos;
	}

	/**
	 * @return the controlaAnticipos
	 */
	public boolean getControlaAnticipos() {
		return controlaAnticipos;
	}
	
	/**
	 * @param controlaAnticipos the controlaAnticipos to set
	 */
	public void setControlaAnticipos(boolean controlaAnticipos) {
		this.controlaAnticipos = controlaAnticipos;
	}

	/**
	 * 
	 * @return
	 */
	public String getTituloAnticipoOAbonoAplicado()
	{
		String titulo="";
		if(this.isPacientePagaAtencionXMontoCobro())
		{
			titulo= "Abono Aplicado: ";
		}
		else
		{
			if(this.isControlaAnticipos())
			{
				titulo="Anticipo Aplicado: ";
			}
		}
		return titulo;
	}

	/**
	 * @return the empresaInstitucion
	 */
	public double getEmpresaInstitucion() {
		return empresaInstitucion;
	}

	/**
	 * @param empresaInstitucion the empresaInstitucion to set
	 */
	public void setEmpresaInstitucion(double empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}

	/**
	 * @return the entidadSubContratada
	 */
	public double getEntidadSubContratada() {
		return entidadSubContratada;
	}

	/**
	 * @param entidadSubContratada the entidadSubContratada to set
	 */
	public void setEntidadSubContratada(double entidadSubContratada) {
		this.entidadSubContratada = entidadSubContratada;
	}

	/**
	 * @return the esParticular
	 */
	public boolean isEsParticular() {
		return esParticular;
	}

	/**
	 * @param esParticular the esParticular to set
	 */
	public void setEsParticular(boolean esParticular) {
		this.esParticular = esParticular;
	}

	/**
	 * @return the estratoSocial
	 */
	public InfoDatosInt getEstratoSocial() {
		return estratoSocial;
	}

	/**
	 * @param estratoSocial the estratoSocial to set
	 */
	public void setEstratoSocial(InfoDatosInt estratoSocial) {
		this.estratoSocial = estratoSocial;
	}

	/**
	 * @return the montoCobro
	 */
	public DtoMontoCobroFactura getMontoCobro() {
		return montoCobro;
	}

	/**
	 * @param montoCobro the montoCobro to set
	 */
	public void setMontoCobro(DtoMontoCobroFactura montoCobro) {
		this.montoCobro = montoCobro;
	}

	/**
	 * @return the fechaVigenciaTopeCuenta
	 */
	public String getFechaVigenciaTopeCuenta() {
		return fechaVigenciaTopeCuenta;
	}

	/**
	 * @param fechaVigenciaTopeCuenta the fechaVigenciaTopeCuenta to set
	 */
	public void setFechaVigenciaTopeCuenta(String fechaVigenciaTopeCuenta) {
		this.fechaVigenciaTopeCuenta = fechaVigenciaTopeCuenta;
	}

	/**
	 * @return the formatoImpresion
	 */
	public InfoDatosInt getFormatoImpresion() {
		return formatoImpresion;
	}

	/**
	 * @param formatoImpresion the formatoImpresion to set
	 */
	public void setFormatoImpresion(InfoDatosInt formatoImpresion) {
		this.formatoImpresion = formatoImpresion;
	}

	/**
	 * @return the pacienteEntidadSubContratada
	 */
	public double getPacienteEntidadSubContratada() {
		return pacienteEntidadSubContratada;
	}

	/**
	 * @param pacienteEntidadSubContratada the pacienteEntidadSubContratada to set
	 */
	public void setPacienteEntidadSubContratada(double pacienteEntidadSubContratada) {
		this.pacienteEntidadSubContratada = pacienteEntidadSubContratada;
	}

	/**
	 * @return the prefijoFactura
	 */
	public String getPrefijoFactura() {
		return prefijoFactura;
	}

	/**
	 * @param prefijoFactura the prefijoFactura to set
	 */
	public void setPrefijoFactura(String prefijoFactura) {
		this.prefijoFactura = prefijoFactura;
	}

	/**
	 * @return the resolucion
	 */
	public String getResolucion() {
		return resolucion;
	}

	/**
	 * @param resolucion the resolucion to set
	 */
	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}

	/**
	 * @return the tipoRegimen
	 */
	public InfoDatos getTipoRegimen() {
		return tipoRegimen;
	}

	/**
	 * @param tipoRegimen the tipoRegimen to set
	 */
	public void setTipoRegimen(InfoDatos tipoRegimen) {
		this.tipoRegimen = tipoRegimen;
	}

	/**
	 * @return the viaIngreso
	 */
	public InfoDatosInt getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(InfoDatosInt viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @param nombreResponsable the nombreResponsable to set
	 */
	public void setNombreResponsable(String nombreResponsable) {
		this.nombreResponsable = nombreResponsable;
	}

	/**
	 * @return the nombreResponsable
	 */
	public String getNombreResponsable() {
		return nombreResponsable;
	}

	/**
	 * @param nitResponsable the nitResponsable to set
	 */
	public void setNitResponsable(String nitResponsable) {
		this.nitResponsable = nitResponsable;
	}

	/**
	 * @return the nitResponsable
	 */
	public String getNitResponsable() {
		return nitResponsable;
	}

	public void setNombreSimpleConvenio(String nombreSimpleConvenio) {
		this.nombreSimpleConvenio = nombreSimpleConvenio;
	}

	public String getNombreSimpleConvenio() {
		return nombreSimpleConvenio;
	}

	
}
