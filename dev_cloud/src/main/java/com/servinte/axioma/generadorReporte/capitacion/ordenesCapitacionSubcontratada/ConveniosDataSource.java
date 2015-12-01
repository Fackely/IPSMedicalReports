package com.servinte.axioma.generadorReporte.capitacion.ordenesCapitacionSubcontratada;

import java.text.NumberFormat;
import java.util.ArrayList;

import org.apache.struts.util.MessageResources;


import util.reportes.dinamico.DataSource;

import net.sf.jasperreports.engine.JRDataSource;

import com.servinte.axioma.dto.capitacion.DtoContratoReporte;
import com.servinte.axioma.dto.capitacion.DtoConvenioReporte;
import com.servinte.axioma.dto.capitacion.DtoNivelReporte;

/**
 * Clase para generar el data source de los convenios 
 *  necesario en el formato A
 * 
 * @version 1.0, May 11, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class ConveniosDataSource{
	
	/** * Contiene los mensajes correspondiente a este Generador*/
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.capitacion.GeneradorReporteOrdenCapitacionSubcontratada");
	
	/**
	 * Atributo que representa un espacio
	 */
	private static final String espacio=" ";
	
	/**
	 * Atributo que representa 4 espacio 
	 */
	private static final String espacioLargo="    ";
	
	
	/**
	 * Atributo que representa la lista de convenios a la cual se le obtiene
	 * el datasource
	 */
	private ArrayList<DtoConvenioReporte> convenios;
	
	/**
	 * Atributo que representa la lista de convenios no autorizados
	 * utilizado unicamente para el datasource del reporte plano
	 */
	private ArrayList<DtoConvenioReporte> conveniosNoAutorizados;
	
	/**
	 * Constructor de la clase
	*/
	public ConveniosDataSource() {

	}
	
	/**
	 * Constructor de la clase para los reportes PDF, Excel
	 * @param convenios
	 */
	public ConveniosDataSource(ArrayList<DtoConvenioReporte> convenios) {
		this.setConvenios(convenios);
	}
	
	/**
	 * Constructor de la clase para el reporte Plano
	 * @param convenios
	 */
	public ConveniosDataSource(ArrayList<DtoConvenioReporte> convenios, ArrayList<DtoConvenioReporte> conveniosNoAutorizados) {
		this.setConvenios(convenios);
		this.setConveniosNoAutorizados(conveniosNoAutorizados);
	}
	
	
	/**
	 * Método encargado de crear el datasource de los convenios autorizados 
	 * para el formato A
	 * @return
	 */
	public JRDataSource crearDataSourceAutorizado(){
		NumberFormat nf = NumberFormat.getInstance();
	    nf.setMaximumFractionDigits(2);
		DataSource dataSource = new DataSource("tipoConvenio","nombreConvenio", "numeroContrato",
										"nombre", "totalPresupuestado", "totalOrdenado", "porcentajeOrdenado", 
										"totalAutorizado", "porcentajeAutorizado", "totalCargosCuenta", 
										"porcentajeCargos", "totalFacturado", "porcentajeFacturado");
		for(DtoConvenioReporte convenio:this.getConvenios()){
			for(DtoContratoReporte contrato:convenio.getContratos()){
				for(DtoNivelReporte nivel:contrato.getNivelesAtencion()){
					dataSource.add(convenio.getTipoConvenio(), convenio.getNombre(), 
							messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contrato")+espacio
							+contrato.getNumeroContrato()+espacioLargo+messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.valorContrato")+espacio
							+nf.format(contrato.getValorMensual().doubleValue())+espacioLargo
							+messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.porcentajeValorGasto")+espacio
							+nf.format(contrato.getPorcentajeGastoMensual().doubleValue())
							+"% - "+nf.format(contrato.getValorGastoMensual().doubleValue()),nivel.getNombre(), 
							nivel.getTotalPresupuestado(), nivel.getTotalOrdenado(), nivel.getPorcentajeOrdenado(), 
							nivel.getTotalAutorizado(), nivel.getPorcentajeAutorizado(), 
							nivel.getTotalCargosCuenta(), nivel.getPorcentajeCargos(), 
							nivel.getTotalFacturado(), nivel.getPorcentajeFacturado());
				}
				
			}
		}
		return dataSource;
	}
	
	/**
	 * Método encargado de crear el datasource de los convenios NO autorizados 
	 * para el formato A
	 * @return
	 */
	public JRDataSource crearDataSourceNoAutorizado(){
		NumberFormat nf = NumberFormat.getInstance();
	    nf.setMaximumFractionDigits(2);
		DataSource dataSource = new DataSource("tipoConvenio","nombreConvenio", "numeroContrato",
										"nombre", "totalPresupuestado", "totalOrdenado", "porcentajeOrdenado", 
										"totalCargosCuenta", "porcentajeCargos", "totalFacturado", "porcentajeFacturado");
		for(DtoConvenioReporte convenio:this.getConvenios()){
			for(DtoContratoReporte contrato:convenio.getContratos()){
				for(DtoNivelReporte nivel:contrato.getNivelesAtencion()){
					dataSource.add(convenio.getTipoConvenio(), convenio.getNombre(), 
							messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contrato")+espacio
							+contrato.getNumeroContrato()+espacioLargo+messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.valorContrato")+espacio
							+nf.format(contrato.getValorMensual().doubleValue())+espacioLargo
							+messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.porcentajeValorGasto")+espacio
							+nf.format(contrato.getPorcentajeGastoMensual().doubleValue())
							+"% - "+nf.format(contrato.getValorGastoMensual().doubleValue()),nivel.getNombre(), 
							nivel.getTotalPresupuestado(), nivel.getTotalOrdenado(),
							nivel.getPorcentajeOrdenado(), nivel.getTotalCargosCuenta(), 
							nivel.getPorcentajeCargos(), nivel.getTotalFacturado(),
							nivel.getPorcentajeFacturado());
				}
				
			}
		}
		return dataSource;
	}
	
	
	/**
	 * Método encargado de crear el datasource de los convenios para el reporte
	 * plano para el formato A
	 * @return
	 */
	public JRDataSource crearDataSourcePlano(){
		NumberFormat nf = NumberFormat.getInstance();
	    nf.setMaximumFractionDigits(2);
		DataSource dataSource = new DataSource("tipoConvenio","nombreConvenio", "numeroContrato",
										"nombre", "totalPresupuestado", "totalOrdenado", "porcentajeOrdenado", 
										"totalAutorizado", "porcentajeAutorizado", "totalCargosCuenta", 
										"porcentajeCargos", "totalFacturado", "porcentajeFacturado");
		for(DtoConvenioReporte convenio:this.getConvenios()){
			for(DtoContratoReporte contrato:convenio.getContratos()){
				for(DtoNivelReporte nivel:contrato.getNivelesAtencion()){
					dataSource.add(convenio.getTipoConvenio(), convenio.getNombre(), 
							contrato.getNumeroContrato(), nivel.getNombre(), 
							nivel.getTotalPresupuestado(), nivel.getTotalOrdenado(), nivel.getPorcentajeOrdenado(), 
							nivel.getTotalAutorizado(), nivel.getPorcentajeAutorizado(), 
							nivel.getTotalCargosCuenta(), nivel.getPorcentajeCargos(), 
							nivel.getTotalFacturado(), nivel.getPorcentajeFacturado());
				}
				
			}
		}
		for(DtoConvenioReporte convenio:this.getConveniosNoAutorizados()){
			for(DtoContratoReporte contrato:convenio.getContratos()){
				for(DtoNivelReporte nivel:contrato.getNivelesAtencion()){
					dataSource.add(convenio.getTipoConvenio(), convenio.getNombre(), 
							contrato.getNumeroContrato(), nivel.getNombre(), 
							nivel.getTotalPresupuestado(), nivel.getTotalOrdenado(), nivel.getPorcentajeOrdenado(), 
							null, null, 
							nivel.getTotalCargosCuenta(), nivel.getPorcentajeCargos(), 
							nivel.getTotalFacturado(), nivel.getPorcentajeFacturado());
				}
				
			}
		}
		return dataSource;
	}

	
	/**
	 * @return the convenios
	 */
	public ArrayList<DtoConvenioReporte> getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList<DtoConvenioReporte> convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the conveniosNoAutorizados
	 */
	public ArrayList<DtoConvenioReporte> getConveniosNoAutorizados() {
		return conveniosNoAutorizados;
	}

	/**
	 * @param conveniosNoAutorizados the conveniosNoAutorizados to set
	 */
	public void setConveniosNoAutorizados(
			ArrayList<DtoConvenioReporte> conveniosNoAutorizados) {
		this.conveniosNoAutorizados = conveniosNoAutorizados;
	}
}
