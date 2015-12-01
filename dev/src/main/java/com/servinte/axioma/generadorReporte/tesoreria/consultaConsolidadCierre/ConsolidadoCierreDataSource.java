package com.servinte.axioma.generadorReporte.tesoreria.consultaConsolidadCierre;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import util.UtilidadTexto;
import util.reportes.dinamico.DataSource;

import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreReporte;
import com.servinte.axioma.orm.FormasPago;

public class ConsolidadoCierreDataSource {


	/** 
	 *  lista de consolidados  de cierre
	 */
	private ArrayList<DtoConsolidadoCierreReporte> consolidadoCierre;
	private List<FormasPago> formasPago;
 
	public ConsolidadoCierreDataSource(ArrayList<DtoConsolidadoCierreReporte> consolidadoCierre,List<FormasPago> formasPago) {
		this.formasPago=formasPago;
		this.consolidadoCierre=consolidadoCierre;
	}


	/**
	 * Método encargado de crear el datasource de los cierres  
	 * @return JRDataSource
	 */
	public JRDataSource crearDataSource(){
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		int tamanio=this.formasPago.size();
		String colums[]= new String[tamanio+2];
		
		
		
		colums[0]="centroAtencion";
		
		for (int i = 1; i < tamanio+1; i++) {
			String columna=UtilidadTexto.primeraLetraMayuscula(formasPago.get(i-1).getDescripcion());
			colums[i]=columna;
		}
		
		colums[tamanio+1]="totalCentroAtencion";
		
		
		
		//set de las columnas del reporte
		DataSource dataSource = new DataSource(colums);
		
		
		for (DtoConsolidadoCierreReporte dto:consolidadoCierre) {
			Object[] tem = new Object[tamanio+2];
			tem[0]=dto.getCentroAtencion();
			
			for (int j = 0; j < dto.getTotalesPorFormaPago().size(); j++) {
				tem[j+1]=dto.getTotalesPorFormaPago().get(j);
			}
			tem[tamanio+1]=dto.getTotalCentroAtencion();
			if (!dto.isEsTotalJsp()) {
				dataSource.add(tem);
			}
				
		}
		

		return dataSource;
	}



	/**
	 * Método encargado de crear el datasource de los cierres para el reporte
	 * plano 
	 * @return JRDataSource
	 */
	public JRDataSource crearDataSourcePlano(){
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		int tamanio=this.formasPago.size();
		String colums[]= new String[tamanio+2];
		
		
		
		colums[0]="Intitucion";
		colums[1]="centroAtencion";
		
		for (int i = 2; i < tamanio+2; i++) {
			String columna=UtilidadTexto.primeraLetraMayuscula(formasPago.get(i-2).getDescripcion());
			colums[i]=columna;
		}
		
		//set de las columnas del reporte
		DataSource dataSource = new DataSource(colums);
		
		
		for (DtoConsolidadoCierreReporte dto:consolidadoCierre) {
			Object[] tem = new Object[tamanio+2];
			tem[0]=dto.getIntitucion();
			tem[1]=dto.getCentroAtencion();
			
			for (int j = 0; j < dto.getTotalesPorFormaPago().size(); j++) {
				tem[j+2]=dto.getTotalesPorFormaPago().get(j);
			}
			if (!dto.isEsTotalJsp()) {
			dataSource.add(tem);	
			}
		}
		

		return dataSource;
	}






}
