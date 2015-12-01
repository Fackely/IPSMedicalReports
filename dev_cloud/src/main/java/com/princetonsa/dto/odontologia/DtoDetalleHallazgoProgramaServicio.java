package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosDouble;



public class DtoDetalleHallazgoProgramaServicio implements Serializable , Cloneable {

	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;
	
	private double codigo;
	private double hallazgoVsProgramaServicio;
	private double orden;
	private InfoDatosDouble programa;
	private InfoDatosDouble servicio;
	private double numeroSuperficies;
	private String porDefecto;
	private ArrayList<DtoDetalleProgramas> listaServiciosPrograma;
	private ArrayList<DtoEquivalentesHallazgoProgramaServicio> listaEquivalentes;
	private DtoInfoFechaUsuario datosfechaUsuarioModifica= new DtoInfoFechaUsuario();
	private String codigoPrograma;
	
	/**
	 * Atributo que define si el Hallazgo/Programa permite tratar varias veces sobre una relación de 
	 * 'pieza dental/hallazgo/programa'  o  'boca/hallazgo/programa previamente realizada.
	 */
	private String permiteTratarVariasVeces;
	
	public DtoDetalleHallazgoProgramaServicio() {
		reset();
	}
	
	public void reset(){
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.hallazgoVsProgramaServicio = ConstantesBD.codigoNuncaValido;
		this.orden = ConstantesBD.codigoNuncaValido;
		this.programa =  new InfoDatosDouble();
		this.servicio =  new InfoDatosDouble();
		this.numeroSuperficies = ConstantesBD.codigoNuncaValido;
		this.porDefecto = "";
		this.datosfechaUsuarioModifica = new DtoInfoFechaUsuario();
		this.listaServiciosPrograma = new ArrayList<DtoDetalleProgramas>();
		this.listaEquivalentes = new ArrayList<DtoEquivalentesHallazgoProgramaServicio>();
		this.codigoPrograma="";
		this.setPermiteTratarVariasVeces(ConstantesBD.acronimoNo);
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
	 * @return the hallazgoVsProgramaServicio
	 */
	public double getHallazgoVsProgramaServicio() {
		return hallazgoVsProgramaServicio;
	}

	/**
	 * @param hallazgoVsProgramaServicio the hallazgoVsProgramaServicio to set
	 */
	public void setHallazgoVsProgramaServicio(double hallazgoVsProgramaServicio) {
		this.hallazgoVsProgramaServicio = hallazgoVsProgramaServicio;
	}

	

	/**
	 * @return the numeroSuperficies
	 */
	public double getNumeroSuperficies() {
		return numeroSuperficies;
	}

	/**
	 * @param numeroSuperficies the numeroSuperficies to set
	 */
	public void setNumeroSuperficies(double numeroSuperficies) {
		this.numeroSuperficies = numeroSuperficies;
	}

	/**
	 * @return the porDefecto
	 */
	public String getPorDefecto() {
		return porDefecto;
	}

	/**
	 * @param porDefecto the porDefecto to set
	 */
	public void setPorDefecto(String porDefecto) {
		this.porDefecto = porDefecto;
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
	 * @return the programa
	 */
	public InfoDatosDouble getPrograma() {
		return programa;
	}

	/**
	 * @param programa the programa to set
	 */
	public void setPrograma(InfoDatosDouble programa) {
		this.programa = programa;
	}

	/**
	 * @return the servicio
	 */
	public InfoDatosDouble getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(InfoDatosDouble servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the orden
	 */
	public double getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(double orden) {
		this.orden = orden;
	}

	/**
	 * @return the listaServiciosPrograma
	 */
	public ArrayList<DtoDetalleProgramas> getListaServiciosPrograma() {
		return listaServiciosPrograma;
	}

	/**
	 * @param listaServiciosPrograma the listaServiciosPrograma to set
	 */
	public void setListaServiciosPrograma(
			ArrayList<DtoDetalleProgramas> listaServiciosPrograma) {
		this.listaServiciosPrograma = listaServiciosPrograma;
	}
	
	/***
	 * 
	 *  
	 */
	public Object clone(){
	        DtoDetalleHallazgoProgramaServicio obj=null;
	        try{
	            obj=(DtoDetalleHallazgoProgramaServicio)super.clone();
	            obj.setPrograma((InfoDatosDouble)this.programa.clone());
	            obj.setServicio((InfoDatosDouble)this.servicio.clone());
	           
	        }catch(CloneNotSupportedException ex){
	        	Log4JManager.error(" no se puede duplicar");
	        }
	        return obj;
	    }

	/**
	 * @return the listaEquivalentes
	 */
	public ArrayList<DtoEquivalentesHallazgoProgramaServicio> getListaEquivalentes() {
		return listaEquivalentes;
	}

	/**
	 * @param listaEquivalentes the listaEquivalentes to set
	 */
	public void setListaEquivalentes(
			ArrayList<DtoEquivalentesHallazgoProgramaServicio> listaEquivalentes) {
		this.listaEquivalentes = listaEquivalentes;
	}

	public String getCodigoPrograma() {
		return codigoPrograma;
	}

	public void setCodigoPrograma(String codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	/**
	 * @param permiteTratarVariasVeces the permiteTratarVariasVeces to set
	 */
	public void setPermiteTratarVariasVeces(String permiteTratarVariasVeces) {
		this.permiteTratarVariasVeces = permiteTratarVariasVeces;
	}

	/**
	 * @return the permiteTratarVariasVeces
	 */
	public String getPermiteTratarVariasVeces() {
		return permiteTratarVariasVeces;
	}
	
	
	
	
}
