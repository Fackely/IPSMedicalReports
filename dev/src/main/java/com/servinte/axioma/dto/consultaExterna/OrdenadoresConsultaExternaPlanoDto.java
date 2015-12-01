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
 * para ser mostradas en un archivo plano
 * 
 * @author jeilones
 * @created 2/11/2012
 *
 */
public class OrdenadoresConsultaExternaPlanoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8876057686294540017L;
	
	/**Informacion del profesional**/
	private String tipoIdentificacion;
	private String numeroIdentificacion;
	
	private String loginUsuario;
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	private String especializaciones;
	private String nombreCompleto;
	private double totalCostoPromedio;
	/**Fin Informacion del profesional**/

	/**Informacion de la unidad de consulta**/
	private Integer codigoUnidadConsulta;
	private String descripcionUnidadConsulta;
	/**Fin Informacion de la unidad de consulta**/
	
	/**Informacion de ordenador de consultas**/
	private int cantidadCitasAtendidas;

	private boolean esServicio;
	
	private Integer codigoGrupoServicio; 
	private String grupoServicio;
	private Integer codigoClaseInventario;
	private String claseInventario;
	
	private int cantidadOrdenesAmbGeneradas;
	
	private int cantidadOrdenesAmbGeneradasXGrupoClase;
	private int cantidadOrdenesAmbGeneradasXUnidadConsulta;
	
	private double promedioOrdenesAmbXCitas;
	private Double valorEstCita;
	private double promedioServMedOrdenAmb;
	private Double valorEstServMedOrdenAmb;
	private double promedioServMedCita;
	private Double valorEstServMedCita;
	private double costoPromedioCita;
	
	/**
	 * 
	 * @author jeilones
	 * @created 14/11/2012
	 */
	public OrdenadoresConsultaExternaPlanoDto() {
		super();
	}
	
	/**Fin Informacion de ordenador de consultas**/
	/**
	 * @return the loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}
	/**
	 * @param loginUsuario the loginUsuario to set
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}
	/**
	 * @return the primerNombre
	 */
	public String getPrimerNombre() {
		return primerNombre;
	}
	/**
	 * @param primerNombre the primerNombre to set
	 */
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}
	/**
	 * @return the segundoNombre
	 */
	public String getSegundoNombre() {
		return segundoNombre;
	}
	/**
	 * @param segundoNombre the segundoNombre to set
	 */
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}
	/**
	 * @return the primerApellido
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}
	/**
	 * @param primerApellido the primerApellido to set
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	/**
	 * @return the segundoApellido
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}
	/**
	 * @param segundoApellido the segundoApellido to set
	 */
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	/**
	 * @return the especializaciones
	 */
	public String getEspecializaciones() {
		return especializaciones;
	}
	/**
	 * @param especializaciones the especializaciones to set
	 */
	public void setEspecializaciones(String especializaciones) {
		this.especializaciones = especializaciones;
	}
	/**
	 * @return the nombreCompleto
	 */
	public String getNombreCompleto() {
		nombreCompleto="";
		StringBuffer stringBuffer=new StringBuffer();
		if(this.primerNombre!=null){
			stringBuffer.append(this.primerNombre);
		}
		if(this.segundoNombre!=null){
			stringBuffer.append(" ");
			stringBuffer.append(this.segundoNombre);
		}
		if(this.primerApellido!=null){
			stringBuffer.append(" ");
			stringBuffer.append(this.primerApellido);
		}
		if(this.segundoApellido!=null){
			stringBuffer.append(" ");
			stringBuffer.append(this.segundoApellido);
		}
		nombreCompleto=stringBuffer.toString();
		return nombreCompleto;
	}
	/**
	 * @param nombreCompleto the nombreCompleto to set
	 */
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	/**
	 * @return the totalCostoPromedio
	 */
	public double getTotalCostoPromedio() {
		return totalCostoPromedio;
	}
	/**
	 * @param totalCostoPromedio the totalCostoPromedio to set
	 */
	public void setTotalCostoPromedio(double totalCostoPromedio) {
		this.totalCostoPromedio = totalCostoPromedio;
	}
	/**
	 * @return the codigoUnidadConsulta
	 */
	public Integer getCodigoUnidadConsulta() {
		return codigoUnidadConsulta;
	}
	/**
	 * @param codigoUnidadConsulta the codigoUnidadConsulta to set
	 */
	public void setCodigoUnidadConsulta(Integer codigoUnidadConsulta) {
		this.codigoUnidadConsulta = codigoUnidadConsulta;
	}
	/**
	 * @return the descripcionUnidadConsulta
	 */
	public String getDescripcionUnidadConsulta() {
		return descripcionUnidadConsulta;
	}
	/**
	 * @param descripcionUnidadConsulta the descripcionUnidadConsulta to set
	 */
	public void setDescripcionUnidadConsulta(String descripcionUnidadConsulta) {
		this.descripcionUnidadConsulta = descripcionUnidadConsulta;
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
	 * @return the promedioOrdenesAmbXCitas
	 */
	public double getPromedioOrdenesAmbXCitas() {
		return promedioOrdenesAmbXCitas;
	}
	/**
	 * @param promedioOrdenesAmbXCitas the promedioOrdenesAmbXCitas to set
	 */
	public void setPromedioOrdenesAmbXCitas(double promedioOrdenesAmbXCitas) {
		this.promedioOrdenesAmbXCitas = promedioOrdenesAmbXCitas;
	}
	/**
	 * @return the valorEstCita
	 */
	public Double getValorEstCita() {
		return valorEstCita;
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
	 * @param costoPromedioCita the costoPromedioCita to set
	 */
	public void setCostoPromedioCita(double costoPromedioCita) {
		this.costoPromedioCita = costoPromedioCita;
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
			
			if(costoPromedioCita==0.0){
				costoPromedioCitaString=costoPromedioCitaString.substring(2,costoPromedioCitaString.length());
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return costoPromedioCitaString;
	}
	
	/**
	 * @return the tipoIdentificacion
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * @param tipoIdentificacion the tipoIdentificacion to set
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * @param numeroIdentificacion the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * @return the cantidadOrdenesAmbGeneradasXUnidadConsulta
	 */
	public int getCantidadOrdenesAmbGeneradasXUnidadConsulta() {
		return cantidadOrdenesAmbGeneradasXUnidadConsulta;
	}

	/**
	 * @param cantidadOrdenesAmbGeneradasXUnidadConsulta the cantidadOrdenesAmbGeneradasXUnidadConsulta to set
	 */
	public void setCantidadOrdenesAmbGeneradasXUnidadConsulta(
			int cantidadOrdenesAmbGeneradasXUnidadConsulta) {
		this.cantidadOrdenesAmbGeneradasXUnidadConsulta = cantidadOrdenesAmbGeneradasXUnidadConsulta;
	}

	/**
	 * @return the cantidadOrdenesAmbGeneradasXGrupoClase
	 */
	public int getCantidadOrdenesAmbGeneradasXGrupoClase() {
		return cantidadOrdenesAmbGeneradasXGrupoClase;
	}

	/**
	 * @param cantidadOrdenesAmbGeneradasXGrupoClase the cantidadOrdenesAmbGeneradasXGrupoClase to set
	 */
	public void setCantidadOrdenesAmbGeneradasXGrupoClase(
			int cantidadOrdenesAmbGeneradasXGrupoClase) {
		this.cantidadOrdenesAmbGeneradasXGrupoClase = cantidadOrdenesAmbGeneradasXGrupoClase;
	}
}
