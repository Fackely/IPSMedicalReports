/**
 * 
 */
package com.servinte.axioma.dto.consultaExterna;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;

import net.sf.jasperreports.engine.JRDataSource;

import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.servinte.axioma.dto.administracion.ProfesionalSaludDto;

/**
 * @author armando
 *
 */
public class DtoUnidadesConsulta implements Serializable 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1167562919808360302L;

	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private String descripcion;
	
	/**
	 * 
	 */
	private boolean activa;
	
	
	/**
	 * 
	 */
	private String tipoAtencion;
	
	/**
	 * 
	 */
	private DtoEspecialidades especialidades;
	
	/**
	 * 
	 */
	private DtoCentrosAtencion centroAtencion;
	
	/**
	 * 
	 */
	private String color;

	/**
	 * Citas atendidas en la unidad de agenda
	 */
	private int cantidadCitasAtendidas;
	private int cantidadOrdenesAmbGeneradas;

	private Double valorEstCita;
	private Double valorEstServMedOrdenAmb;
	private Double valorEstServMedCita;
	
	private double promedioCantidadOrdenesAmbGeneradas;
	private double promedioCantidadCitasAtendidas;
	private double promedioTotalOrdenesAmbXCitas;
	private double promedioTotalServMedOrdenAmb;
	private double promedioTotalServMedCita;
	
	/**
	 * Lista de consulta externa atendidas en la unidad de consulta
	 */
	private List<OrdenadoresConsultaExternaDto>listaOrdenadoresConsultaExterna;
	private JRDataSource grupoOrdenadoresConsultaExterna;
	
	/**
	 * Lista de profesionales que atendieron citas en la unidad de consulta
	 */
	private List<ProfesionalSaludDto>listaProfesionales;
	private JRDataSource grupoProfesionales;
	
	/**
	 * Costo promedio de las citas atendidas en la unidad de consulta
	 */
	private double totalCostoPromedioCita;
	
	public DtoUnidadesConsulta(){
	
	}
	
	public DtoUnidadesConsulta( int codigo, String descripcion ){
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	public String getTipoAtencion() {
		return tipoAtencion;
	}

	public void setTipoAtencion(String tipoAtencion) {
		this.tipoAtencion = tipoAtencion;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public DtoEspecialidades getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(DtoEspecialidades especialidades) {
		this.especialidades = especialidades;
	}

	public DtoCentrosAtencion getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(DtoCentrosAtencion centroAtencion) {
		this.centroAtencion = centroAtencion;
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
	 * @return the listaCitasAtendidas
	 */
	public List<OrdenadoresConsultaExternaDto> getListaOrdenadoresConsultaExterna() {
		return listaOrdenadoresConsultaExterna;
	}

	/**
	 * @param listaCitasAtendidas the listaCitasAtendidas to set
	 */
	public void setListaOrdenadoresConsultaExterna(List<OrdenadoresConsultaExternaDto> listaOrdenadoresConsultaExterna) {
		this.listaOrdenadoresConsultaExterna = listaOrdenadoresConsultaExterna;
	}

	/**
	 * @return the totalCostoPromedio
	 */
	public double getTotalCostoPromedioCita() {
		return totalCostoPromedioCita;
	}

	/**
	 * @param totalCostoPromedio the totalCostoPromedio to set
	 */
	public void setTotalCostoPromedioCita(double totalCostoPromedio) {
		this.totalCostoPromedioCita = totalCostoPromedio;
	}

	/**
	 * Retorna el valor total del costo promedio de la cita en formato de miles y decimales (#,##0.00)
	 * 
	 * @return totalCostoPromedioCita
	 * @author jeilones
	 * @created 16/01/2013
	 */
	public String getTotalCostoPromedioCitaString() {
		String totalCostoPromedioString="";
		try{
			DecimalFormat format=new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
			totalCostoPromedioString=format.format(totalCostoPromedioCita);
			
			totalCostoPromedioString=totalCostoPromedioString.replace(",", ";");
			totalCostoPromedioString=totalCostoPromedioString.replace(".", ",");
			totalCostoPromedioString=totalCostoPromedioString.replace(";", ".");
			
			if(totalCostoPromedioCita<10){
				totalCostoPromedioString=totalCostoPromedioString.substring(2,totalCostoPromedioString.length());
			}else{
				if(totalCostoPromedioCita<100){
					totalCostoPromedioString=totalCostoPromedioString.substring(1,totalCostoPromedioString.length());
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return totalCostoPromedioString;
	}

	/**
	 * @return the grupoOrdenadoresConsultaExterna
	 */
	public JRDataSource getGrupoOrdenadoresConsultaExterna() {
		return grupoOrdenadoresConsultaExterna;
	}

	/**
	 * @param grupoOrdenadoresConsultaExterna the grupoOrdenadoresConsultaExterna to set
	 */
	public void setGrupoOrdenadoresConsultaExterna(
			JRDataSource grupoOrdenadoresConsultaExterna) {
		this.grupoOrdenadoresConsultaExterna = grupoOrdenadoresConsultaExterna;
	}

	/**
	 * @return the listaProfesionales
	 */
	public List<ProfesionalSaludDto> getListaProfesionales() {
		return listaProfesionales;
	}

	/**
	 * @param listaProfesionales the listaProfesionales to set
	 */
	public void setListaProfesionales(List<ProfesionalSaludDto> listaProfesionales) {
		this.listaProfesionales = listaProfesionales;
	}

	/**
	 * @return the grupoProfesionales
	 */
	public JRDataSource getGrupoProfesionales() {
		return grupoProfesionales;
	}

	/**
	 * @param grupoProfesionales the grupoProfesionales to set
	 */
	public void setGrupoProfesionales(JRDataSource grupoProfesionales) {
		this.grupoProfesionales = grupoProfesionales;
	}

	/**
	 * @return the valorEstCita
	 */
	public Double getValorEstCita() {
		return valorEstCita;
	}

	/**
	 * Retorna el valor estandar por cita en formato de miles y decimales (#,##0.00)
	 * 
	 * @return the valorEstCita
	 * @author jeilones
	 * @created 25/01/2013
	 */
	public String getValorEstCitaString() {
		String valorEstCitaString="";
		try{
			if(valorEstCita!=null){
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
	 * @return the valorEstServMedOrdenAmb
	 */
	public Double getValorEstServMedOrdenAmb() {
		return valorEstServMedOrdenAmb;
	}

	/**
	 * Retorna el valor estandar de servicios/medicamentos por orden ambulatoria en formato de miles y decimales (#,##0.00)
	 * 
	 * @return the valorEstServMedOrdenAmb
	 * @author jeilones
	 * @created 25/01/2013
	 */
	public String getValorEstServMedOrdenAmbString() {
		String valorEstServMedOrdenAmbString="";
		try{
			if(valorEstServMedOrdenAmb!=null){
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
	 * @return the valorEstServMedCita
	 */
	public Double getValorEstServMedCita() {
		return valorEstServMedCita;
	}
	
	/**
	 * Retorna el valor estandar de servicios/medicamentos por cita en formato de miles y decimales (#,##0.00)
	 * 
	 * @return the valorEstServMedCita
	 * @author jeilones
	 * @created 25/01/2013
	 */
	public String getValorEstServMedCitaString() {
		String valorEstServMedCitaString="";
		try{
			if(valorEstServMedCita!=null){
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

	public double getPromedioCantidadOrdenesAmbGeneradas() {
		return promedioCantidadOrdenesAmbGeneradas;
	}
	
	/**
	 * Retorna el promedio de la cantidad de ordenes ambulatorias generadas en formato de miles y decimales (#,##0.00)
	 * 
	 * @return the promedioCantidadOrdenesAmbGeneradas
	 * @author jeilones
	 * @created 25/01/2013
	 */
	public String getPromedioCantidadOrdenesAmbGeneradasString() {
		String promedioCantidadOrdenesAmbGeneradasString="";
		try{
			DecimalFormat format=new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
			promedioCantidadOrdenesAmbGeneradasString=format.format(promedioCantidadOrdenesAmbGeneradas);
			
			promedioCantidadOrdenesAmbGeneradasString=promedioCantidadOrdenesAmbGeneradasString.replace(",", ";");
			promedioCantidadOrdenesAmbGeneradasString=promedioCantidadOrdenesAmbGeneradasString.replace(".", ",");
			promedioCantidadOrdenesAmbGeneradasString=promedioCantidadOrdenesAmbGeneradasString.replace(";", ".");
			
			if(promedioCantidadOrdenesAmbGeneradas==0){
				promedioCantidadOrdenesAmbGeneradasString=promedioCantidadOrdenesAmbGeneradasString.substring(2,promedioCantidadOrdenesAmbGeneradasString.length());
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return promedioCantidadOrdenesAmbGeneradasString;
	}

	public void setPromedioCantidadOrdenesAmbGeneradas(
			double promedioCantidadOrdenesAmbGeneradas) {
		this.promedioCantidadOrdenesAmbGeneradas = promedioCantidadOrdenesAmbGeneradas;
	}

	public double getPromedioCantidadCitasAtendidas() {
		return promedioCantidadCitasAtendidas;
	}

	/**
	 * Retorna el promedio de la cantidad de citas atendidas en formato de miles y decimales (#,##0.00)
	 * 
	 * @return the promedioCantidadCitasAtendidas
	 * @author jeilones
	 * @created 25/01/2013
	 */
	public String getPromedioCantidadCitasAtendidasString() {
		String promedioCantidadCitasAtendidasString="";
		try{
			DecimalFormat format=new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
			promedioCantidadCitasAtendidasString=format.format(promedioCantidadCitasAtendidas);
			
			promedioCantidadCitasAtendidasString=promedioCantidadCitasAtendidasString.replace(",", ";");
			promedioCantidadCitasAtendidasString=promedioCantidadCitasAtendidasString.replace(".", ",");
			promedioCantidadCitasAtendidasString=promedioCantidadCitasAtendidasString.replace(";", ".");
			
			if(promedioCantidadCitasAtendidas==0){
				promedioCantidadCitasAtendidasString=promedioCantidadCitasAtendidasString.substring(2,promedioCantidadCitasAtendidasString.length());
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return promedioCantidadCitasAtendidasString;
	}
	
	public void setPromedioCantidadCitasAtendidas(
			double promedioCantidadCitasAtendidas) {
		this.promedioCantidadCitasAtendidas = promedioCantidadCitasAtendidas;
	}

	public double getPromedioTotalOrdenesAmbXCitas() {
		return promedioTotalOrdenesAmbXCitas;
	}
	
	/**
	 * Retorna el promedio de la cantidad de ordenes ambulatorias generadas por citas atendidas en formato de miles y decimales (#,##0.00)
	 * 
	 * @return the promedioTotalOrdenesAmbXCitas
	 * @author jeilones
	 * @created 25/01/2013
	 */
	public String getPromedioTotalOrdenesAmbXCitasString() {
		String promedioTotalOrdenesAmbXCitasString="";
		try{
			DecimalFormat format=new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
			promedioTotalOrdenesAmbXCitasString=format.format(promedioTotalOrdenesAmbXCitas);
			
			promedioTotalOrdenesAmbXCitasString=promedioTotalOrdenesAmbXCitasString.replace(",", ";");
			promedioTotalOrdenesAmbXCitasString=promedioTotalOrdenesAmbXCitasString.replace(".", ",");
			promedioTotalOrdenesAmbXCitasString=promedioTotalOrdenesAmbXCitasString.replace(";", ".");
			
			if(promedioTotalOrdenesAmbXCitas==0){
				promedioTotalOrdenesAmbXCitasString=promedioTotalOrdenesAmbXCitasString.substring(2,promedioTotalOrdenesAmbXCitasString.length());
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return promedioTotalOrdenesAmbXCitasString;
	}

	public void setPromedioTotalOrdenesAmbXCitas(
			double promedioTotalOrdenesAmbXCitas) {
		this.promedioTotalOrdenesAmbXCitas = promedioTotalOrdenesAmbXCitas;
	}

	public double getPromedioTotalServMedOrdenAmb() {
		return promedioTotalServMedOrdenAmb;
	}

	/**
	 * Retorna el promedio de la cantidad de servicios/medicamentos por ordenes ambulatorias en formato de miles y decimales (#,##0.00)
	 * 
	 * @return the promedioTotalServMedOrdenAmb
	 * @author jeilones
	 * @created 25/01/2013
	 */
	public String getPromedioTotalServMedOrdenAmbString() {
		String promedioTotalServMedOrdenAmbString="";
		try{
			DecimalFormat format=new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
			promedioTotalServMedOrdenAmbString=format.format(promedioTotalServMedOrdenAmb);
			
			promedioTotalServMedOrdenAmbString=promedioTotalServMedOrdenAmbString.replace(",", ";");
			promedioTotalServMedOrdenAmbString=promedioTotalServMedOrdenAmbString.replace(".", ",");
			promedioTotalServMedOrdenAmbString=promedioTotalServMedOrdenAmbString.replace(";", ".");
			
			if(promedioTotalServMedOrdenAmb==0){
				promedioTotalServMedOrdenAmbString=promedioTotalServMedOrdenAmbString.substring(2,promedioTotalServMedOrdenAmbString.length());
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return promedioTotalServMedOrdenAmbString;
	}

	public void setPromedioTotalServMedOrdenAmb(double promedioTotalServMedOrdenAmb) {
		this.promedioTotalServMedOrdenAmb = promedioTotalServMedOrdenAmb;
	}

	public double getPromedioTotalServMedCita() {
		return promedioTotalServMedCita;
	}
	
	/**
	 * Retorna el promedio de la cantidad de servicios/medicamentos por citas atendidas en formato de miles y decimales (#,##0.00)
	 * 
	 * @return the promedioTotalServMedOrdenAmb
	 * @author jeilones
	 * @created 25/01/2013
	 */
	public String getPromedioTotalServMedCitaString() {
		String promedioTotalServMedCitaString="";
		try{
			DecimalFormat format=new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
			promedioTotalServMedCitaString=format.format(promedioTotalServMedCita);
			
			promedioTotalServMedCitaString=promedioTotalServMedCitaString.replace(",", ";");
			promedioTotalServMedCitaString=promedioTotalServMedCitaString.replace(".", ",");
			promedioTotalServMedCitaString=promedioTotalServMedCitaString.replace(";", ".");
			
			if(promedioTotalServMedCita==0){
				promedioTotalServMedCitaString=promedioTotalServMedCitaString.substring(2,promedioTotalServMedCitaString.length());
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		return promedioTotalServMedCitaString;
	}

	public void setPromedioTotalServMedCita(double promedioTotalServMedCita) {
		this.promedioTotalServMedCita = promedioTotalServMedCita;
	}

}
