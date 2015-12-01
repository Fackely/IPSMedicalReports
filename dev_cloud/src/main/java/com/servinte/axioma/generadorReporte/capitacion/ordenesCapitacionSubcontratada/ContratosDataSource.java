package com.servinte.axioma.generadorReporte.capitacion.ordenesCapitacionSubcontratada;

import java.text.NumberFormat;
import java.util.ArrayList;

import org.apache.struts.util.MessageResources;

import net.sf.jasperreports.engine.JRDataSource;
import util.reportes.dinamico.DataSource;

import com.servinte.axioma.dto.capitacion.DtoContratoReporte;
import com.servinte.axioma.dto.capitacion.DtoGrupoClaseReporte;
import com.servinte.axioma.dto.capitacion.DtoNivelReporte;

/**
 * Clase para generar el data source de los contratos 
 *  necesario en el formato B
 * 
 * @version 1.0, May 11, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class ContratosDataSource {
	
	
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
	 * Atributo que representa la lista de contratos a la cual se le obtiene
	 * el datasource
	 */
	private ArrayList<DtoContratoReporte> contratos;
	
	/**
	 * Constructor de la clase
	 * @param contratos
	 */
	public ContratosDataSource(ArrayList<DtoContratoReporte> contratos) {
		this.setContratos(contratos);
	}
	
	/**
	 * Método encargado de crear el datasource de los contratos 
	 * para el formato B
	 * @return
	 */
	public JRDataSource crearDataSource(){
		NumberFormat nf = NumberFormat.getInstance();
	    nf.setMaximumFractionDigits(2);
		DataSource dataSource = new DataSource("numeroContrato", "nombreNivel",
										"nombreGrupoClase", "totalPresupuestado", "totalOrdenado", "porcentajeOrdenado", 
										"totalAutorizado", "porcentajeAutorizado", "totalCargosCuenta", 
										"porcentajeCargos", "totalFacturado", "porcentajeFacturado");
		for(DtoContratoReporte contrato:this.getContratos()){
			for(DtoNivelReporte nivel:contrato.getNivelesAtencion()){
				for(DtoGrupoClaseReporte grupoClase:nivel.getGruposClases()){
					dataSource.add(messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.contrato")
							+espacio+contrato.getNumeroContrato()+espacioLargo
							+messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.valorContrato")+espacio
							+nf.format(contrato.getValorMensual().doubleValue())+espacioLargo
							+messageResource.getMessage("generadorReporteOrdenCapitacionSubcontratada.porcentajeValorGasto")+espacio
							+nf.format(contrato.getPorcentajeGastoMensual().doubleValue())
							+"% - "+nf.format(contrato.getValorGastoMensual().doubleValue()),
							nivel.getNombre(), grupoClase.getNombre(), 
							grupoClase.getTotalPresupuestado(), grupoClase.getTotalOrdenado(), 
							grupoClase.getPorcentajeOrdenado(), grupoClase.getTotalAutorizado(), 
							grupoClase.getPorcentajeAutorizado(), grupoClase.getTotalCargosCuenta(), 
							grupoClase.getPorcentajeCargos(), grupoClase.getTotalFacturado(), 
							grupoClase.getPorcentajeFacturado());
				}
				
			}
		}
		return dataSource;
	}
	
	/**
	 * Método encargado de crear el datasource de los contratos 
	 * para el reporte plano en el formato B
	 * @return
	 */
	public JRDataSource crearDataSourcePlano(){
		NumberFormat nf = NumberFormat.getInstance();
	    nf.setMaximumFractionDigits(2);
		DataSource dataSource = new DataSource("numeroContrato", "nombreNivel",
										"nombreGrupoClase", "totalPresupuestado", "totalOrdenado", "porcentajeOrdenado", 
										"totalAutorizado", "porcentajeAutorizado", "totalCargosCuenta", 
										"porcentajeCargos", "totalFacturado", "porcentajeFacturado");
		for(DtoContratoReporte contrato:this.getContratos()){
			for(DtoNivelReporte nivel:contrato.getNivelesAtencion()){
				for(DtoGrupoClaseReporte grupoClase:nivel.getGruposClases()){
					dataSource.add(contrato.getNumeroContrato(), nivel.getNombre(), grupoClase.getNombre(), 
							grupoClase.getTotalPresupuestado(), grupoClase.getTotalOrdenado(), 
							grupoClase.getPorcentajeOrdenado(), grupoClase.getTotalAutorizado(), 
							grupoClase.getPorcentajeAutorizado(), grupoClase.getTotalCargosCuenta(), 
							grupoClase.getPorcentajeCargos(), grupoClase.getTotalFacturado(), 
							grupoClase.getPorcentajeFacturado());
				}
				
			}
		}
		return dataSource;
	}

	/**
	 * @return the contratos
	 */
	public ArrayList<DtoContratoReporte> getContratos() {
		return contratos;
	}

	/**
	 * @param contratos the contratos to set
	 */
	public void setContratos(ArrayList<DtoContratoReporte> contratos) {
		this.contratos = contratos;
	}
}
