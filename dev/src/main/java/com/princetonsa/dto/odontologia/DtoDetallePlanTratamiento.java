package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;
import util.InfoDatosInt;

public class DtoDetallePlanTratamiento  implements Serializable, Cloneable{

	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;
	private double codigo ;
	private double planTratamiento;
	private int piezaDental;
	private int Superficie;
	private int Hallazgo;
	private String seccion;
	private String clasificacion;
	private DtoInfoFechaUsuario fechaUsuarioModifica;
	private int convencion;
	private String porConfirmar;
	private InfoDatosInt especialidad;
	private String activo; 
	private String path;
	
	private BigDecimal codigoCita;
	private BigDecimal valoracion;
	private BigDecimal evolucion;
	private String codigoTarifarioBusqServ;
	
	
	
	/**
	 * ATRIBUTO  EL CODIGO DEL HISTORICO DEL PLAN DE TRATAMIENTO
	 */
	private double  codigoPkDetalleHistorico;
	private double  codigoPkPlanTratmientoHistorico;
	
	
	
	
	
	public double getCodigoPkDetalleHistorico() {
		return codigoPkDetalleHistorico;
	}

	public void setCodigoPkDetalleHistorico(double codigoPkDetalleHistorico) {
		this.codigoPkDetalleHistorico = codigoPkDetalleHistorico;
	}

	public DtoDetallePlanTratamiento() {
		this.reset();
	}
	
	public void reset(){
		this.codigo =ConstantesBD.codigoNuncaValido;
		this.planTratamiento= ConstantesBD.codigoNuncaValido;
		this.piezaDental = ConstantesBD.codigoNuncaValido;
		this.Superficie= ConstantesBD.codigoNuncaValido;
		this.Hallazgo = ConstantesBD.codigoNuncaValido;
		this.seccion = "";
		this.clasificacion = "";
		this.fechaUsuarioModifica = new DtoInfoFechaUsuario();
		this.convencion = ConstantesBD.codigoNuncaValido;
		this.porConfirmar = "";
		this.especialidad = new InfoDatosInt();
		this.activo = ConstantesBD.acronimoSi;
		this.path = "";
		
		this.valoracion = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.evolucion = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.codigoCita = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.codigoPkDetalleHistorico = ConstantesBD.codigoNuncaValidoDouble;
		this.codigoTarifarioBusqServ="";
		this.codigoPkPlanTratmientoHistorico=ConstantesBD.codigoNuncaValidoDouble;
		
	}

	/**
	 * @return the codigo
	 */
	public double getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the planTratamiento
	 */
	public double getPlanTratamiento() {
		return planTratamiento;
	}

	/**
	 * @param planTratamiento the planTratamiento to set
	 */
	public void setPlanTratamiento(double planTratamiento) {
		this.planTratamiento = planTratamiento;
	}

	/**
	 * @return the piezaDental
	 */
	public int getPiezaDental() {
		return piezaDental;
	}

	/**
	 * @param piezaDental the piezaDental to set
	 */
	public void setPiezaDental(int piezaDental) {
		this.piezaDental = piezaDental;
	}

	/**
	 * @return the superficie
	 */
	public int getSuperficie() {
		return Superficie;
	}

	/**
	 * @param superficie the superficie to set
	 */
	public void setSuperficie(int superficie) {
		Superficie = superficie;
	}

	/**
	 * @return the hallazgo
	 */
	public int getHallazgo() {
		return Hallazgo;
	}

	/**
	 * @param hallazgo the hallazgo to set
	 */
	public void setHallazgo(int hallazgo) {
		Hallazgo = hallazgo;
	}

	/**
	 * @return the seccion
	 */
	public String getSeccion() {
		return seccion;
	}

	/**
	 * @param seccion the seccion to set
	 */
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}

	/**
	 * @return the clasificacion
	 */
	public String getClasificacion() {
		return clasificacion;
	}

	/**
	 * @param clasificacion the clasificacion to set
	 */
	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	/**
	 * @return the fechaUsuarioModifica
	 */
	public DtoInfoFechaUsuario getFechaUsuarioModifica() {
		return fechaUsuarioModifica;
	}

	/**
	 * @param fechaUsuarioModifica the fechaUsuarioModifica to set
	 */
	public void setFechaUsuarioModifica(DtoInfoFechaUsuario fechaUsuarioModifica) {
		this.fechaUsuarioModifica = fechaUsuarioModifica;
	}

	/**
	 * @return the convencion
	 */
	public int getConvencion() {
		return convencion;
	}

	/**
	 * @param convencion the convencion to set
	 */
	public void setConvencion(int convencion) {
		this.convencion = convencion;
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

	public InfoDatosInt getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(InfoDatosInt especialidad) {
		this.especialidad = especialidad;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the valoracion
	 */
	public BigDecimal getValoracion() {
		return valoracion;
	}

	/**
	 * @param valoracion the valoracion to set
	 */
	public void setValoracion(BigDecimal valoracion) {
		this.valoracion = valoracion;
	}

	/**
	 * @return the evolucion
	 */
	public BigDecimal getEvolucion() {
		return evolucion;
	}

	/**
	 * @param evolucion the evolucion to set
	 */
	public void setEvolucion(BigDecimal evolucion) {
		this.evolucion = evolucion;
	}

	public BigDecimal getCodigoCita() {
		return codigoCita;
	}

	public void setCodigoCita(BigDecimal codigoCita) {
		this.codigoCita = codigoCita;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		DtoDetallePlanTratamiento clon=new DtoDetallePlanTratamiento();
		clon.setCodigoCita(new BigDecimal(codigoCita.longValue()));
		clon.setFechaUsuarioModifica((DtoInfoFechaUsuario) this.getFechaUsuarioModifica().clone());
		return super.clone();
	}

	/**
	 * @return the codigoTarifarioBusqServ
	 */
	public String getCodigoTarifarioBusqServ() {
		return codigoTarifarioBusqServ;
	}

	/**
	 * @param codigoTarifarioBusqServ the codigoTarifarioBusqServ to set
	 */
	public void setCodigoTarifarioBusqServ(String codigoTarifarioBusqServ) {
		this.codigoTarifarioBusqServ = codigoTarifarioBusqServ;
	}

	public void setCodigoPkPlanTratmientoHistorico(
			double codigoPkPlanTratmientoHistorico) {
		this.codigoPkPlanTratmientoHistorico = codigoPkPlanTratmientoHistorico;
	}

	public double getCodigoPkPlanTratmientoHistorico() {
		return codigoPkPlanTratmientoHistorico;
	}
}
