package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosDouble;



public class DtoHallazgoVsProgramaServicio implements Serializable , Cloneable {

	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;
	
	private double codigo;
	private InfoDatosDouble hallazgo;
	private int institucion;
	private DtoInfoFechaUsuario datosfechaUsuarioModifica= new DtoInfoFechaUsuario();
	private String exiteEnPlan;

	
	/**
	 * aplica a
	 */
	private String aplicaSuperificie;
	
	/**
	 * 
	 */
	public DtoHallazgoVsProgramaServicio(){
		 reset();
	}
	
	/**
	 * 
	 */
	public void reset()
	{
		
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.hallazgo =new InfoDatosDouble();
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.datosfechaUsuarioModifica = new DtoInfoFechaUsuario();
		this.aplicaSuperificie="";
		this.exiteEnPlan="";
		
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
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the datosfechaUsuarioModifica
	 */
	public DtoInfoFechaUsuario getDatosfechaUsuarioModifica() {
		return datosfechaUsuarioModifica;
	}

	/**
	 * @param datosfechaUsuarioModifica the datosfechaUsuarioModifica to set
	 */
	public void setDatosfechaUsuarioModifica(
			DtoInfoFechaUsuario datosfechaUsuarioModifica) {
		this.datosfechaUsuarioModifica = datosfechaUsuarioModifica;
	}

	/**
	 * @return the hallazgo
	 */
	public InfoDatosDouble getHallazgo() {
		return hallazgo;
	}

	/**
	 * @param hallazgo the hallazgo to set
	 */
	public void setHallazgo(InfoDatosDouble hallazgo) {
		this.hallazgo = hallazgo;
	}
	
	/***
	 * 
	 */
	public Object clone(){
        DtoHallazgoVsProgramaServicio obj=null;
        try{
            obj= (DtoHallazgoVsProgramaServicio)super.clone();
            obj.setHallazgo((InfoDatosDouble)this.hallazgo.clone());
        }catch(CloneNotSupportedException ex){
        	Log4JManager.error(" no se puede duplicar");
        }
        return obj;
    }
	
	public Double getCodigoHallazgo(){
		return this.getHallazgo().getCodigo();
	}
	
	public String getCodigoHallazgoAlfa(){
		return this.getHallazgo().getDescripcion();
	}
	
	public String getNombreHallazgo(){
		return this.getHallazgo().getNombre();
	}

	public void setAplicaSuperificie(String aplicaSuperificie) {
		this.aplicaSuperificie = aplicaSuperificie;
	}

	public String getAplicaSuperificie() {
		return aplicaSuperificie;
	}

	public void setExiteEnPlan(String exiteEnPlan) {
		this.exiteEnPlan = exiteEnPlan;
	}

	public String getExiteEnPlan() {
		return exiteEnPlan;
	}
}
