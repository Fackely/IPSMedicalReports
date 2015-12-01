package com.princetonsa.dto.administracion;

import java.io.Serializable;
import java.math.BigDecimal;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import util.ConstantesBD;
import util.InfoDatosInt;

/**
 * <code>DtoConsecutivoCentroAtencion</code>
 * Dto PARA CARGAR LOS CONSECUTIVOS DE LOS CENTROS DE ATENCION
 * 
 * @author axioma
 *
 */
public class DtoConsecutivoCentroAtencion implements Serializable, Cloneable

{

	/**
	 * 
	 */
	
	
	
	private BigDecimal codigoPk;
	private String nombreConsecutivo;
	private String nombreConsecutivoInterfazGrafica;
	private String anio;
	private BigDecimal consecutivo;
	
	private InfoDatosInt centroAtencion;
	
	private String idAnual;
	private int codigoIndiceArray;
	
	private DtoInfoFechaUsuario usuarioModifica;
	private String activo;
	
	
	/**
	 * 
	 */
	private String esVisible;
	private String esConsecutivoVisible;
	private String consecutivoInterfaz; // muestra el Consecutivo en pantalla
	
	/**
	 * Código de la Institución
	 */
	private int codigoInstitucion;
	
	
	
	/**
	 * 
	 */
	public 	DtoConsecutivoCentroAtencion()
	{
		this.idAnual=ConstantesBD.acronimoNo;
		this.anio="";
		this.setConsecutivo(BigDecimal.ZERO);
		this.nombreConsecutivo="";
		this.codigoPk= BigDecimal.ZERO;
		this.setCentroAtencion(new InfoDatosInt());
		this.nombreConsecutivoInterfazGrafica="";
		this.codigoIndiceArray= ConstantesBD.codigoNuncaValido;
		this.usuarioModifica = new DtoInfoFechaUsuario();
		this.activo= "";
		this.esVisible=ConstantesBD.acronimoNo;
		this.esConsecutivoVisible=ConstantesBD.acronimoSi;
		this.consecutivoInterfaz=""; // Ayudante para imprimir consecutivos
		
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
	}
	
	
	
	/**
	 * CLONAR EL CONSECUTIVO
	 * 
	 */
	public DtoConsecutivoCentroAtencion clone(){
         
		DtoConsecutivoCentroAtencion   obj=null;
		
		try{
			obj =(DtoConsecutivoCentroAtencion) super.clone();
			obj.activo = this.activo;
        	obj.consecutivo= this.consecutivo;
        	obj.nombreConsecutivoInterfazGrafica = this.nombreConsecutivoInterfazGrafica;
        	obj.nombreConsecutivo= this.nombreConsecutivo;
        	obj.anio=this.anio;
        	obj.centroAtencion =  (InfoDatosInt)this.centroAtencion.clone();
        	obj.codigoIndiceArray=this.codigoIndiceArray;
        	obj.codigoPk=this.codigoPk;
        	obj.esConsecutivoVisible= this.esConsecutivoVisible;
        	obj.idAnual=this.idAnual;
        	obj.esVisible= this.esVisible;
        	obj.usuarioModifica= (DtoInfoFechaUsuario)usuarioModifica.clone();
        	
        	
        	
        }catch(CloneNotSupportedException ex){
        	Log4JManager.error(" no se puede duplicar");
        }
       return obj;
    }
	
	
	
	/**
	 * 
	 * @return
	 */
	
	public String getIdAnual() {
		return idAnual;
	}
	
	public void setIdAnual(String idAnual) {
		this.idAnual = idAnual;
	}
	
	public String getAnio() {
		return anio;
	}
	
	public void setAnio(String anio) {
		this.anio = anio;
	}
	
	




	public void setNombreConsecutivo(String nombreConsecutivo) {
		this.nombreConsecutivo = nombreConsecutivo;
	}




	public String getNombreConsecutivo() {
		return nombreConsecutivo;
	}




	




	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}




	public BigDecimal getCodigoPk() {
		return codigoPk;
	}




	public void setCentroAtencion(InfoDatosInt centroAtencion) {
		this.centroAtencion = centroAtencion;
	}




	public InfoDatosInt getCentroAtencion() {
		return centroAtencion;
	}




	public void setConsecutivo(BigDecimal consecutivo) {
		this.consecutivo = consecutivo;
	}




	public BigDecimal getConsecutivo() {
		return consecutivo;
	}




	public void setNombreConsecutivoInterfazGrafica(
			String nombreConsecutivoInterfazGrafica) {
		this.nombreConsecutivoInterfazGrafica = nombreConsecutivoInterfazGrafica;
	}




	public String getNombreConsecutivoInterfazGrafica() {
		return nombreConsecutivoInterfazGrafica;
	}




	public void setCodigoIndiceArray(int codigoIndiceArray) {
		this.codigoIndiceArray = codigoIndiceArray;
	}




	public int getCodigoIndiceArray() {
		return codigoIndiceArray;
	}




	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}




	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}




	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}




	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}


	public void setEsVisible(String esVisible) {
		this.esVisible = esVisible;
	}


	public String getEsVisible() {
		return esVisible;
	}



	public void setEsConsecutivoVisible(String esConsecutivoVisible) {
		this.esConsecutivoVisible = esConsecutivoVisible;
	}



	public String getEsConsecutivoVisible() {
		return esConsecutivoVisible;
	}



	public void setConsecutivoInterfaz(String consecutivoInterfaz) {
		this.consecutivoInterfaz = consecutivoInterfaz;
	}


	/**
	 * getConsecutivoInterfaz
	 * @return
	 */
	public String getConsecutivoInterfaz() 
	{
		
		if(this.getConsecutivo().doubleValue()>0)
		{
			this.consecutivoInterfaz=this.getConsecutivo().toString();
		}
		else
		{
			this.consecutivoInterfaz="";
		}
		
		return consecutivoInterfaz;
	}
	
	

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}



	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}
	
	




	


	
	
	
	
}
