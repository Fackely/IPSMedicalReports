package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import util.InfoDatosInt;



public class DtoPresupuestoOdontologico implements Serializable, Cloneable{

	
	
	
	private BigDecimal codigoPK ;
	private String estado;
	private InfoDatosInt motivo ;
	private InfoDatosInt especialidad;
    
	private DtoInfoFechaUsuario  usuarioModifica;
	private DtoInfoFechaUsuario  fechaUsuarioGenera;
	private BigDecimal consecutivo;
	private InfoDatosInt codigoPaciente;
	private BigDecimal ingreso ;
	private BigDecimal cuenta ;
	private BigDecimal planTratamiento;
	private int	institucion ;
	private InfoDatosInt centroAtencion ;
	private int convenioSort;
	
	
	
	

	/**
	 * Helper puedo Contratar o Precontratar. Nota solo se utilza en las actividades
	 * Atributo para mostrar en presentacion. si se puede contratar o precontratar presupuesto 
	 */
	private boolean puedoContratarPrecontratar;

	
	
	/**
	 * Centro de costo para asignar la nueva cita programada
	 */
	private int centroCosto;
	
	/**
	 * DETALLE PRESUPUESTO PROGRAMA SERVICIO 
	 */	
	private ArrayList<DtoPresuOdoProgServ> dtoPresuOdoProgServ = new ArrayList<DtoPresuOdoProgServ>();
	/**
	 * 
	 * 
	 */
	
	private ArrayList<DtoPresupuestoTotalConvenio> listaTarifas = new ArrayList<DtoPresupuestoTotalConvenio>();
	
	/**
	 * 
	 */
	public DtoPresupuestoOdontologico(){
		reset();
	}	
	
	/**
	 * 
	 */
	public DtoPresupuestoOdontologico(BigDecimal codigoPk)
	{
		reset();
		this.codigoPK=codigoPk;
	}	
	
	
	
	/**
	 * 
	 */
	public void reset(){
		this.codigoPK= new BigDecimal(0);
		this.estado="";
		this.motivo= new InfoDatosInt();
		this.especialidad= new InfoDatosInt();
		this.usuarioModifica = new DtoInfoFechaUsuario();
		this.consecutivo = new BigDecimal(0);
		this.codigoPaciente = new InfoDatosInt();
		this.ingreso = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.cuenta = new BigDecimal(ConstantesBD.codigoNuncaValido) ;
	    this.planTratamiento  = new BigDecimal(0);
		this.institucion= ConstantesBD.codigoNuncaValido ;
	    this.centroAtencion = new InfoDatosInt() ;
	    this.dtoPresuOdoProgServ = new ArrayList<DtoPresuOdoProgServ>();
	    this.listaTarifas = new ArrayList<DtoPresupuestoTotalConvenio>();
	    this.fechaUsuarioGenera = new DtoInfoFechaUsuario();
	    this.convenioSort=0;
	    
	    this.puedoContratarPrecontratar=Boolean.FALSE;
	    }





	public BigDecimal getCodigoPK() {
		return codigoPK;
	}





	public void setCodigoPK(BigDecimal codigoPK) {
		this.codigoPK = codigoPK;
	}





	public String getEstado() {
		return estado;
	}





	public void setEstado(String estado) {
		this.estado = estado;
	}





	public InfoDatosInt getMotivo() {
		return motivo;
	}





	public void setMotivo(InfoDatosInt motivo) {
		this.motivo = motivo;
	}





	public InfoDatosInt getEspecialidad() {
		return especialidad;
	}





	public void setEspecialidad(InfoDatosInt especialidad) {
		this.especialidad = especialidad;
	}





	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}





	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}





	public BigDecimal getConsecutivo() {
		return consecutivo;
	}





	public void setConsecutivo(BigDecimal consecutivo) {
		this.consecutivo = consecutivo;
	}





	public InfoDatosInt getCodigoPaciente() {
		return codigoPaciente;
	}





	public void setCodigoPaciente(InfoDatosInt codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}





	public BigDecimal getIngreso() {
		return ingreso;
	}





	public void setIngreso(BigDecimal ingreso) {
		this.ingreso = ingreso;
	}





	public BigDecimal getCuenta() {
		return cuenta;
	}





	public void setCuenta(BigDecimal cuenta) {
		this.cuenta = cuenta;
	}





	public int getInstitucion() {
		return institucion;
	}





	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}





	public InfoDatosInt getCentroAtencion() {
		return centroAtencion;
	}





	public void setCentroAtencion(InfoDatosInt centroAtencion) {
		this.centroAtencion = centroAtencion;
	}



	public void setDtoPresuOdoProgServ(ArrayList<DtoPresuOdoProgServ> dtoPresuOdoProgServ) {
		this.dtoPresuOdoProgServ = dtoPresuOdoProgServ;
	}



	public ArrayList<DtoPresuOdoProgServ> getDtoPresuOdoProgServ() {
		return dtoPresuOdoProgServ;
	}







	/**
	 * @param listaTarifas the listaTarifas to set
	 */
	public void setListaTarifas(ArrayList<DtoPresupuestoTotalConvenio> listaTarifas) {
		this.listaTarifas = listaTarifas;
	}



	/**
	 * @return the listaTarifas
	 */
	public ArrayList<DtoPresupuestoTotalConvenio> getListaTarifas() {
		return listaTarifas;
	}



	/**
	 * @return the planTratamiento
	 */
	public BigDecimal getPlanTratamiento() {
		return planTratamiento;
	}



	/**
	 * @param planTratamiento the planTratamiento to set
	 */
	public void setPlanTratamiento(BigDecimal planTratamiento) {
		this.planTratamiento = planTratamiento;
	}




	
	/***
	 * 
	 *  
	 */
	
	public Object clone()
	{
	        DtoPresupuestoOdontologico obj=null;
	        try{
	            obj= (DtoPresupuestoOdontologico)super.clone();
	            obj.setMotivo((InfoDatosInt)this.motivo.clone());
	            obj.setEspecialidad((InfoDatosInt)this.especialidad.clone());
	       
	        }catch(CloneNotSupportedException ex){
	        	Log4JManager.error(" no se puede duplicar");
	        }
	        return obj;
	  }



	/**
	 * @return the fechaUsuarioGenera
	 */
	public DtoInfoFechaUsuario getFechaUsuarioGenera() {
		return fechaUsuarioGenera;
	}



	/**
	 * @param fechaUsuarioGenera the fechaUsuarioGenera to set
	 */
	public void setFechaUsuarioGenera(DtoInfoFechaUsuario fechaUsuarioGenera) {
		this.fechaUsuarioGenera = fechaUsuarioGenera;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaBd()
	{
		return this.getUsuarioModifica().getFechaModificaFromatoBD();
	}

	/**
	 * @return the convenioSort
	 */
	public int getConvenioSort() {
		return convenioSort;
	}

	/**
	 * @param convenioSort the convenioSort to set
	 */
	public void setConvenioSort(int convenioSort) {
		this.convenioSort = convenioSort;
	}

	/**
	 * 
	 * @return
	 */
	public BigDecimal getSortVrTotalConvenio()
	{
		BigDecimal valor= BigDecimal.ZERO;
		for(DtoPresupuestoTotalConvenio dtoTotal: this.getListaTarifas())
		{
			if(dtoTotal.getConvenio().getCodigo()==convenioSort)
			{	
				if(this.getEstado().equals(ConstantesIntegridadDominio.acronimoContratadoContratado))
				{
					valor=dtoTotal.getValorSubTotalContratado();
				}
				else
				{
					valor=dtoTotal.getValorSubTotalSinContratar();
				}
				break;
			}	
		}
		return valor;
	}

	/**
	 * @return Retorna atributo centroCosto
	 */
	public int getCentroCosto()
	{
		return centroCosto;
	}

	/**
	 * @param centroCosto Asigna atributo centroCosto
	 */
	public void setCentroCosto(int centroCosto)
	{
		this.centroCosto = centroCosto;
	}

	public void setPuedoContratarPrecontratar(boolean puedoContratarPrecontratar) {
		this.puedoContratarPrecontratar = puedoContratarPrecontratar;
	}

	public boolean isPuedoContratarPrecontratar() {
		return puedoContratarPrecontratar;
	}
}
