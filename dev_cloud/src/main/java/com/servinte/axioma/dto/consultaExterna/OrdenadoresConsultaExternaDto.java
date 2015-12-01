/**
 * 
 */
package com.servinte.axioma.dto.consultaExterna;

import java.io.Serializable;
import java.text.DecimalFormat;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;


/**
 * Informacion sobre las ordenes generadas por los profesionales de la salud y sus citas asociadas 
 * 
 * @author jeilones
 * @created 30/10/2012
 *
 */
public class OrdenadoresConsultaExternaDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6171345286961017466L;
	private Integer codigoGrupoServicio; 
	private String grupoServicio;
	private Integer codigoClaseInventario;
	private String claseInventario;
	
	private int cantidadOrdenesAmbGeneradas;
	private int cantidadCitasAtendidas;
	private double promedioOrdenesAmbXCitas;
	private Double valorEstCita;
	private double promedioServMedOrdenAmb;
	private Double valorEstServMedOrdenAmb;
	private double promedioServMedCita;
	private Double valorEstServMedCita;
	private double costoPromedioCita;
	
	private boolean esServicio;
	
	/**
	 * 
	 * @author jeilones
	 * @created 30/10/2012
	 */
	public OrdenadoresConsultaExternaDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the cantidadOrdenesAmbGeneradas
	 */
	public int getCantidadOrdenesAmbGeneradas() {
		return cantidadOrdenesAmbGeneradas;
	}
	
	/**
	 * @param cantidadOrdenesAmbGeneradas the cantidadOrdenesAmbGeneradas to set
	 */
	public void setCantidadOrdenesAmbGeneradas(int cantidadOrdenesAmbGeneradas) {
		this.cantidadOrdenesAmbGeneradas = cantidadOrdenesAmbGeneradas;
	}
	/**
	 * @return the promedioOrdenesAmbCitas
	 */
	public double getPromedioOrdenesAmbXCitas() {
		return promedioOrdenesAmbXCitas;
	}
	
	/**
	 * Retorna el promedio de ordenes ambulatorias por citas atendidas en formato de miles y decimales (#,##0.00)
	 *  
	 * @return the promedioOrdenesAmbXCitasString
	 * @author jeilones
	 * @created 25/01/2013
	 */
	public String getPromedioOrdenesAmbXCitasString() {
		String promedioOrdenesAmbXCitasString="";
		try{
			DecimalFormat format=new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
			promedioOrdenesAmbXCitasString=format.format(promedioOrdenesAmbXCitas);
			
			promedioOrdenesAmbXCitasString=promedioOrdenesAmbXCitasString.replace(",", ";");
			promedioOrdenesAmbXCitasString=promedioOrdenesAmbXCitasString.replace(".", ",");
			promedioOrdenesAmbXCitasString=promedioOrdenesAmbXCitasString.replace(";", ".");
			
			if(promedioOrdenesAmbXCitas<10){
				promedioOrdenesAmbXCitasString=promedioOrdenesAmbXCitasString.substring(2,promedioOrdenesAmbXCitasString.length());
			}else{
				if(promedioOrdenesAmbXCitas<100){
					promedioOrdenesAmbXCitasString=promedioOrdenesAmbXCitasString.substring(1,promedioOrdenesAmbXCitasString.length());
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return promedioOrdenesAmbXCitasString;
	}
	
	/**
	 * @param promedioOrdenesAmbCitas the promedioOrdenesAmbCitas to set
	 */
	public void setPromedioOrdenesAmbXCitas(double promedioOrdenesAmbCitas) {
		this.promedioOrdenesAmbXCitas = promedioOrdenesAmbCitas;
	}
	/**
	 * @return the valorEstCita
	 */
	public Double getValorEstCita() {
		return valorEstCita;
	}
	
	/**
	 * Retorna el valor estandar de citas en formato de miles y decimales (#,##0.00)
	 * 
	 * @return the valorEstCitaString
	 * @author jeilones
	 * @created 25/01/2013
	 */
	public String getValorEstCitaString() {
		String valorEstCitaString="";
		try{
			DecimalFormat format=new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
			valorEstCitaString=format.format(valorEstCita);
			
			valorEstCitaString=valorEstCitaString.replace(",", ";");
			valorEstCitaString=valorEstCitaString.replace(".", ",");
			valorEstCitaString=valorEstCitaString.replace(";", ".");
			
			if(valorEstCita<10){
				valorEstCitaString=valorEstCitaString.substring(2,valorEstCitaString.length());
			}else{
				if(valorEstCita<100){
					valorEstCitaString=valorEstCitaString.substring(1,valorEstCitaString.length());
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return valorEstCitaString;
	}
	
	/**
	 * @param valorEstCita the valorEstCita to set
	 */
	public void setValorEstCita(Double valorEstCita) {
		this.valorEstCita = valorEstCita;
	}
	/**
	 * @return the promedioServMedOrdenAmb
	 */
	public double getPromedioServMedOrdenAmb() {
		return promedioServMedOrdenAmb;
	}
	
	/**
	 * Retorna el promedio de servicios/medicamentos por ordenes en formato de miles y decimales (#,##0.00)
	 * 
	 * @return the promedioServMedOrdenAmb
	 * @author jeilones
	 * @created 25/01/2013
	 */
	public String getPromedioServMedOrdenAmbString() {
		String promedioServMedOrdenAmbString="";
		try{
			DecimalFormat format=new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
			promedioServMedOrdenAmbString=format.format(promedioServMedOrdenAmb);
			
			promedioServMedOrdenAmbString=promedioServMedOrdenAmbString.replace(",", ";");
			promedioServMedOrdenAmbString=promedioServMedOrdenAmbString.replace(".", ",");
			promedioServMedOrdenAmbString=promedioServMedOrdenAmbString.replace(";", ".");
			
			if(promedioServMedOrdenAmb<10){
				promedioServMedOrdenAmbString=promedioServMedOrdenAmbString.substring(2,promedioServMedOrdenAmbString.length());
			}else{
				if(promedioServMedCita<100){
					promedioServMedOrdenAmbString=promedioServMedOrdenAmbString.substring(1,promedioServMedOrdenAmbString.length());
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return promedioServMedOrdenAmbString;
	}
	
	/**
	 * @param promedioServMedOrdenAmb the promedioServMedOrdenAmb to set
	 */
	public void setPromedioServMedOrdenAmb(double promedioServMedOrdenAmb) {
		this.promedioServMedOrdenAmb = promedioServMedOrdenAmb;
	}
	/**
	 * @return the valorEstServMedOrdenAmb
	 */
	public Double getValorEstServMedOrdenAmb() {
		return valorEstServMedOrdenAmb;
	}
	
	/**
	 * Retorna el valor estandar de servicios/medicamentos en formato de miles y decimales (#,##0.00)
	 * 
	 * @return the valorEstServMedOrdenAmbString
	 * @author jeilones
	 * @created 25/01/2013
	 */
	public String getValorEstServMedOrdenAmbString() {
		String valorEstServMedOrdenAmbString="";
		try{
			DecimalFormat format=new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
			valorEstServMedOrdenAmbString=format.format(valorEstServMedOrdenAmb);
			
			valorEstServMedOrdenAmbString=valorEstServMedOrdenAmbString.replace(",", ";");
			valorEstServMedOrdenAmbString=valorEstServMedOrdenAmbString.replace(".", ",");
			valorEstServMedOrdenAmbString=valorEstServMedOrdenAmbString.replace(";", ".");
			
			if(valorEstServMedOrdenAmb<10){
				valorEstServMedOrdenAmbString=valorEstServMedOrdenAmbString.substring(2,valorEstServMedOrdenAmbString.length());
			}else{
				if(valorEstServMedOrdenAmb<100){
					valorEstServMedOrdenAmbString=valorEstServMedOrdenAmbString.substring(1,valorEstServMedOrdenAmbString.length());
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return valorEstServMedOrdenAmbString;
	}
	
	/**
	 * @param valorEstServMedOrdenAmb the valorEstServMedOrdenAmb to set
	 */
	public void setValorEstServMedOrdenAmb(Double valorEstServMedOrdenAmb) {
		this.valorEstServMedOrdenAmb = valorEstServMedOrdenAmb;
	}
	/**
	 * @return the promedioServMedCita
	 */
	public double getPromedioServMedCita() {
		return promedioServMedCita;
	}
	
	/**
	 * Retorna el promedio de servicios/medicamentos por cita en formato de miles y decimales (#,##0.00)
	 * 
	 * @return the promedioServMedCitaString
	 * @author jeilones
	 * @created 25/01/2013
	 */
	public String getPromedioServMedCitaString() {
		String promedioServMedCitaString="";
		try{
			DecimalFormat format=new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
			promedioServMedCitaString=format.format(promedioServMedCita);
			
			promedioServMedCitaString=promedioServMedCitaString.replace(",", ";");
			promedioServMedCitaString=promedioServMedCitaString.replace(".", ",");
			promedioServMedCitaString=promedioServMedCitaString.replace(";", ".");
			
			if(promedioServMedCita<10){
				promedioServMedCitaString=promedioServMedCitaString.substring(2,promedioServMedCitaString.length());
			}else{
				if(promedioServMedCita<100){
					promedioServMedCitaString=promedioServMedCitaString.substring(1,promedioServMedCitaString.length());
				}
			}
			
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return promedioServMedCitaString;
	}
	
	/**
	 * @param promedioServMedCita the promedioServMedCita to set
	 */
	public void setPromedioServMedCita(double promedioServMedCita) {
		this.promedioServMedCita = promedioServMedCita;
	}
	/**
	 * @return the valorEstServMedCita
	 */
	public Double getValorEstServMedCita() {
		return valorEstServMedCita;
	}
	
	/**
	 * Retorna el valor estandar de servicios/medicamentos por cita en formato de miles y decimales (#,##0.00)
	 * 
	 * @return the promedioServMedCitaString
	 * @author jeilones
	 * @created 25/01/2013
	 */
	public String getValorEstServMedCitaString() {
		String valorEstServMedCitaString="";
		try{
			DecimalFormat format=new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
			valorEstServMedCitaString=format.format(valorEstServMedCita);
			
			valorEstServMedCitaString=valorEstServMedCitaString.replace(",", ";");
			valorEstServMedCitaString=valorEstServMedCitaString.replace(".", ",");
			valorEstServMedCitaString=valorEstServMedCitaString.replace(";", ".");
			
			if(valorEstServMedCita<10){
				valorEstServMedCitaString=valorEstServMedCitaString.substring(2,valorEstServMedCitaString.length());
			}else{
				if(valorEstServMedCita<100){
					valorEstServMedCitaString=valorEstServMedCitaString.substring(1,valorEstServMedCitaString.length());
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return valorEstServMedCitaString;
	}
	
	/**
	 * @param valorEstServMedCita the valorEstServMedCita to set
	 */
	public void setValorEstServMedCita(Double valorEstServMedCita) {
		this.valorEstServMedCita = valorEstServMedCita;
	}
	/**
	 * @return the costoPromedioCita
	 */
	public double getCostoPromedioCita() {
		return costoPromedioCita;
	}
	
	/**
	 * Retorna el valor del costo promedio de la cita en formato de miles y decimales (#,##0.00)
	 * 
	 * @return costoPromedioCita
	 * @author jeilones
	 * @created 16/01/2013
	 */
	public String getCostoPromedioCitaString() {
		String costoPromedioCitaString="";
		try{
			DecimalFormat format=new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
			costoPromedioCitaString=format.format(costoPromedioCita);
			
			costoPromedioCitaString=costoPromedioCitaString.replace(",", ";");
			costoPromedioCitaString=costoPromedioCitaString.replace(".", ",");
			costoPromedioCitaString=costoPromedioCitaString.replace(";", ".");
			
			if(costoPromedioCita<10){
				costoPromedioCitaString=costoPromedioCitaString.substring(2,costoPromedioCitaString.length());
			}else{
				if(costoPromedioCita<100){
					costoPromedioCitaString=costoPromedioCitaString.substring(1,costoPromedioCitaString.length());
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return costoPromedioCitaString;
	}
	
	/**
	 * @param costoPromedioCita the costoPromedioCita to set
	 */
	public void setCostoPromedioCita(double costoPromedioCita) {
		this.costoPromedioCita = costoPromedioCita;
	}


	/**
	 * @return the cantidadCitasAtendidas
	 */
	public int getCantidadCitasAtendidas() {
		return cantidadCitasAtendidas;
	}

	/**
	 * @param cantidadCitasAtendidas the cantidadCitasAtendidas to set
	 */
	public void setCantidadCitasAtendidas(int cantidadCitasAtendidas) {
		this.cantidadCitasAtendidas = cantidadCitasAtendidas;
	}

	/**
	 * @return the esServicio
	 */
	public boolean isEsServicio() {
		return esServicio;
	}

	/**
	 * @param esServicio the esServicio to set
	 */
	public void setEsServicio(boolean esServicio) {
		this.esServicio = esServicio;
	}

	/**
	 * @return the codigoGrupoServicio
	 */
	public Integer getCodigoGrupoServicio() {
		return codigoGrupoServicio;
	}

	/**
	 * @param codigoGrupoServicio the codigoGrupoServicio to set
	 */
	public void setCodigoGrupoServicio(Integer codigoGrupoServicio) {
		this.codigoGrupoServicio = codigoGrupoServicio;
	}

	/**
	 * @return the grupoServicio
	 */
	public String getGrupoServicio() {
		return grupoServicio;
	}

	/**
	 * @param grupoServicio the grupoServicio to set
	 */
	public void setGrupoServicio(String grupoServicio) {
		this.grupoServicio = grupoServicio;
	}

	/**
	 * @return the codigoClaseInventario
	 */
	public Integer getCodigoClaseInventario() {
		return codigoClaseInventario;
	}

	/**
	 * @param codigoClaseInventario the codigoClaseInventario to set
	 */
	public void setCodigoClaseInventario(Integer codigoClaseInventario) {
		this.codigoClaseInventario = codigoClaseInventario;
	}

	/**
	 * @return the claseInventario
	 */
	public String getClaseInventario() {
		return claseInventario;
	}

	/**
	 * @param claseInventario the claseInventario to set
	 */
	public void setClaseInventario(String claseInventario) {
		this.claseInventario = claseInventario;
	}
	
}
