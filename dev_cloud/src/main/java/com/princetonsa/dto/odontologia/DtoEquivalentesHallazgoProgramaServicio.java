package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosDouble;

public class DtoEquivalentesHallazgoProgramaServicio implements Serializable , Cloneable {

	private double codigo;
	private InfoDatosDouble detallehallazgo;
	private InfoDatosDouble detallehallazgo2; // Utilizado para Cargar los Equivalentes
	private boolean activo;

	private DtoInfoFechaUsuario datosfechaUsuarioModifica= new DtoInfoFechaUsuario();
	
	
	
	/*
	 *LISTA UTILIZADA PARA CARGAR LOS SUB-EQUIVALENTES DE LOS PROGRAMAS SERVICIOS 
	 */
	private ArrayList<DtoEquivalentesHallazgoProgramaServicio> listaSubEquivalentes = new ArrayList<DtoEquivalentesHallazgoProgramaServicio>();
	
	
	public void reset(){
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.detallehallazgo = new InfoDatosDouble();
	   	this.detallehallazgo2 = new InfoDatosDouble();
	   	this.datosfechaUsuarioModifica = new DtoInfoFechaUsuario();
		this.activo = false;
		this.setListaSubEquivalentes(new ArrayList<DtoEquivalentesHallazgoProgramaServicio>());
	}
	
	public DtoEquivalentesHallazgoProgramaServicio(){
		reset();
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
	 * @return the detallehallazgo
	 */
	public InfoDatosDouble getDetallehallazgo() {
		return detallehallazgo;
	}

	/**
	 * @param detallehallazgo the detallehallazgo to set
	 */
	public void setDetallehallazgo(InfoDatosDouble detallehallazgo) {
		this.detallehallazgo = detallehallazgo;
	}

	/**
	 * @return the detallehallazgo2
	 */
	public InfoDatosDouble getDetallehallazgo2() {
		return detallehallazgo2;
	}

	/**
	 * @param detallehallazgo2 the detallehallazgo2 to set
	 */
	public void setDetallehallazgo2(InfoDatosDouble detallehallazgo2) {
		this.detallehallazgo2 = detallehallazgo2;
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	
	/**
	 * 
	 */
	public Object clone(){
	        
	        DtoEquivalentesHallazgoProgramaServicio obj = null;
	        
	        try
	        {
	            obj=(DtoEquivalentesHallazgoProgramaServicio)super.clone();
	            obj.codigo= this.codigo;
	            obj.detallehallazgo=(InfoDatosDouble)this.detallehallazgo.clone();
	            obj.detallehallazgo2=(InfoDatosDouble)this.detallehallazgo2.clone();
	        }
	        catch(CloneNotSupportedException ex)
	        {
	        	Log4JManager.error(" no se puede duplicar");
	        }
	        return obj;
	    }

	public void setListaSubEquivalentes(ArrayList<DtoEquivalentesHallazgoProgramaServicio> listaSubEquivalentes) {
		this.listaSubEquivalentes = listaSubEquivalentes;
	}

	public ArrayList<DtoEquivalentesHallazgoProgramaServicio> getListaSubEquivalentes() {
		return listaSubEquivalentes;
	}

}
