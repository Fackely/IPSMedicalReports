/**
 * 
 */
package com.servinte.axioma.dto.administracion;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;

import net.sf.jasperreports.engine.JRDataSource;

import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;

/**
 * @author jeilones
 * @created 30/10/2012
 *
 */
public class ProfesionalSaludDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5068119000233270966L;
	
	private int codigoMedico;
	
	private String tipoIdentificacion;
	private String numeroIdentificacion;
	
	private String loginUsuario;
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	private String especializaciones;
	
	private String nombreCompleto;
	
	private int cantidadOrdenesAmbGeneradas;
	private int cantidadCitasAtendidas;
	
	private double promedioOrdenesAmbXCitas;
	private double promedioServMedOrdenAmb;
	private double promedioServMedCita;
	
	private double totalCostoPromedio;
	
	
	private Map<Integer,DtoUnidadesConsulta>mapaUnidadesConsultas;
	private List<DtoUnidadesConsulta>unidadesConsultas;
	private JRDataSource grupoUnidadesConsulta;
	
	
	
	/**
	 * 
	 * @author jeilones
	 * @created 30/10/2012
	 */
	public ProfesionalSaludDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	

	/**
	 * @param codigoMedico
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @param loginUsuario
	 * @param primerNombre
	 * @param segundoNombre
	 * @param primerApellido
	 * @param segundoApellido
	 * @author jeilones
	 * @created 15/11/2012
	 */
	public ProfesionalSaludDto(int codigoMedico, String tipoIdentificacion,
			String numeroIdentificacion, String loginUsuario,
			String primerNombre, String segundoNombre, String primerApellido,
			String segundoApellido) {
		super();
		this.codigoMedico = codigoMedico;
		this.tipoIdentificacion = tipoIdentificacion;
		this.numeroIdentificacion = numeroIdentificacion;
		this.loginUsuario = loginUsuario;
		this.primerNombre = primerNombre;
		this.segundoNombre = segundoNombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
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
	 * @return the cantidadCitasAtendias
	 */
	public int getCantidadCitasAtendidas() {
		return cantidadCitasAtendidas;
	}


	/**
	 * @param cantidadCitasAtendias the cantidadCitasAtendias to set
	 */
	public void setCantidadCitasAtendidas(int cantidadCitasAtendias) {
		this.cantidadCitasAtendidas = cantidadCitasAtendias;
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
	 * Retorna el valor total del costo promedio de la cita en formato de miles y decimales (#,##0.00)
	 * 
	 * @return totalCostoPromedio
	 * @author jeilones
	 * @created 16/01/2013
	 */
	public String getTotalCostoPromedioString() {
		String totalCostoPromedioString="";
		try{
			DecimalFormat format=new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
			totalCostoPromedioString=format.format(totalCostoPromedio);
			
			totalCostoPromedioString=totalCostoPromedioString.replace(",", ";");
			totalCostoPromedioString=totalCostoPromedioString.replace(".", ",");
			totalCostoPromedioString=totalCostoPromedioString.replace(";", ".");
			
			if(totalCostoPromedio==0.0){
				totalCostoPromedioString=totalCostoPromedioString.substring(2,totalCostoPromedioString.length());
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return totalCostoPromedioString;
	}
	
	/**
	 * @return the unidadesConsultas
	 */
	public List<DtoUnidadesConsulta> getUnidadesConsultas() {
		return unidadesConsultas;
	}


	/**
	 * @param unidadesConsultas the unidadesConsultas to set
	 */
	public void setUnidadesConsultas(List<DtoUnidadesConsulta> unidadesConsultas) {
		this.unidadesConsultas = unidadesConsultas;
	}


	/**
	 * @return the grupoUnidadesConsulta
	 */
	public JRDataSource getGrupoUnidadesConsulta() {
		return grupoUnidadesConsulta;
	}


	/**
	 * @param grupoUnidadesConsulta the grupoUnidadesConsulta to set
	 */
	public void setGrupoUnidadesConsulta(JRDataSource grupoUnidadesConsulta) {
		this.grupoUnidadesConsulta = grupoUnidadesConsulta;
	}


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
	 * @return the nombreCompleto
	 */
	public String getNombreCompletoPrimeroApellidos() {
		StringBuffer stringBuffer=new StringBuffer();
		if(this.primerApellido!=null){
			stringBuffer.append(this.primerApellido);
		}
		if(this.segundoApellido!=null){
			stringBuffer.append(" ");
			stringBuffer.append(this.segundoApellido);
		}
		if(this.primerNombre!=null){
			stringBuffer.append(" ");
			stringBuffer.append(this.primerNombre);
		}
		if(this.segundoNombre!=null){
			stringBuffer.append(" ");
			stringBuffer.append(this.segundoNombre);
		}
		return stringBuffer.toString();
	}

	/**
	 * @param nombreCompleto the nombreCompleto to set
	 */
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}


	/**
	 * @return the mapaUnidadesConsultas
	 */
	public Map<Integer, DtoUnidadesConsulta> getMapaUnidadesConsultas() {
		return mapaUnidadesConsultas;
	}


	/**
	 * @param mapaUnidadesConsultas the mapaUnidadesConsultas to set
	 */
	public void setMapaUnidadesConsultas(
			Map<Integer, DtoUnidadesConsulta> mapaUnidadesConsultas) {
		this.mapaUnidadesConsultas = mapaUnidadesConsultas;
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
	 * @return the codigoMedico
	 */
	public int getCodigoMedico() {
		return codigoMedico;
	}


	/**
	 * @param codigoMedico the codigoMedico to set
	 */
	public void setCodigoMedico(int codigoMedico) {
		this.codigoMedico = codigoMedico;
	}




	/**
	 * @return the cantidadOrdenesAmbulatorias
	 */
	public int getCantidadOrdenesAmbGeneradas() {
		return cantidadOrdenesAmbGeneradas;
	}




	/**
	 * @param cantidadOrdenesAmbulatorias the cantidadOrdenesAmbulatorias to set
	 */
	public void setCantidadOrdenesAmbGeneradas(int cantidadOrdenesAmbulatorias) {
		this.cantidadOrdenesAmbGeneradas = cantidadOrdenesAmbulatorias;
	}




	/**
	 * @return the promedioOrdenesAmbXCitas
	 */
	public double getPromedioOrdenesAmbXCitas() {
		return promedioOrdenesAmbXCitas;
	}


	/**
	 * Retorna el promedio de ordenes ambulatorias por cita en formato de miles y decimales (#,##0.00)
	 * 
	 * @return the promedioOrdenesAmbXCitas
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
			
			if(promedioOrdenesAmbXCitas==0){
				promedioOrdenesAmbXCitasString=promedioOrdenesAmbXCitasString.substring(2,promedioOrdenesAmbXCitasString.length());
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return promedioOrdenesAmbXCitasString;
	}

	/**
	 * @param promedioOrdenesAmbXCitas the promedioOrdenesAmbXCitas to set
	 */
	public void setPromedioOrdenesAmbXCitas(double promedioOrdenesAmbXCitas) {
		this.promedioOrdenesAmbXCitas = promedioOrdenesAmbXCitas;
	}




	/**
	 * @return the promedioServMedOrdenAmb
	 */
	public double getPromedioServMedOrdenAmb() {
		return promedioServMedOrdenAmb;
	}


	/**
	 * Retorna el promedio de servicios/medicamentos por ordenes ambulatorias en formato de miles y decimales (#,##0.00)
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
			
			if(promedioServMedOrdenAmb==0){
				promedioServMedOrdenAmbString=promedioServMedOrdenAmbString.substring(2,promedioServMedOrdenAmbString.length());
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
	 * @return the promedioServMedCita
	 */
	public double getPromedioServMedCita() {
		return promedioServMedCita;
	}

	/**
	 * Retorna el promedio de servicios/medicamentos por cita en formato de miles y decimales (#,##0.00)
	 * 
	 * @return the promedioServMedCita
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
			
			if(promedioServMedCita==0){
				promedioServMedCitaString=promedioServMedCitaString.substring(2,promedioServMedCitaString.length());
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
	
	
}
