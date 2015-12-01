package com.servinte.axioma.generadorReporte.tesoreria.consultaConsolidadCierre;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.MessageResources;

import net.sf.jasperreports.engine.JRDataSource;
import util.UtilidadTexto;
import util.reportes.dinamico.DataSource;

import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreReporte;
import com.servinte.axioma.orm.FormasPago;
 
public class ConsolidadoCierreDataSourceCajaCajero {


	/**
	 *  lista de consolidados  de cierre por caja cajero
	 */
	private ArrayList<DtoConsolidadoCierreReporte> consolidadoCierre;
	private List<FormasPago> formasPago;

	public ConsolidadoCierreDataSourceCajaCajero(ArrayList<DtoConsolidadoCierreReporte> consolidadoCierreCajaCajero,List<FormasPago> formasPago) {
		this.formasPago=formasPago;
		this.consolidadoCierre=consolidadoCierreCajaCajero;
	}

	/**
	 * Mensajes parametrizados del reporte.
	 */
	private MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.ConsultarConsolidadosCierres");


	/**
	 * Método encargado de crear el datasource de los cierres  
	 * @return JRDataSource
	 */
	public JRDataSource crearDataSource(){
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		int tamanio=this.formasPago.size();
		String cols[]= new String[tamanio+6];


		cols[0]="Intitucion";
		cols[1]="centroAtencion";
		cols[2]="caja";
		cols[3]= "Cajero";
		cols[4]="horaTurnoCajero";


		for (int i = 5; i < tamanio+5; i++) {
			String columna=UtilidadTexto.primeraLetraMayuscula(formasPago.get(i-5).getDescripcion());
			cols[i]=columna;
		}

		cols[tamanio+5]="totalCentroAtencion";

		//set de las columnas del reporte
		DataSource dataSource = new DataSource(cols);


		for (DtoConsolidadoCierreReporte dto:consolidadoCierre) {
			Object[] tem = new Object[tamanio+6];
			tem[0]=dto.getIntitucion();
			tem[1]=dto.getCentroAtencion();
			tem[2]=dto.getCaja();
			tem[3]=dto.getCajero();
			tem[4]=dto.getHoraTurnoCajero();

			for (int j = 0; j < dto.getTotalesPorFormaPago().size(); j++) {
				tem[j+5]=dto.getTotalesPorFormaPago().get(j);
			}
			tem[tamanio+5]=dto.getTotalCentroAtencion();

			if (!dto.isEsTotalJsp() && dto.getIntitucion()!=null) {
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
		String cols[]= new String[tamanio+5];


		cols[0]="Intitucion";
		cols[1]="centroAtencion";
		cols[2]="caja";
		cols[3]= "Cajero";
		cols[4]="horaTurnoCajero";


		for (int i = 5; i < tamanio+5; i++) {
			String columna=UtilidadTexto.primeraLetraMayuscula(formasPago.get(i-5).getDescripcion());
			cols[i]=columna;
		}


		//set de las columnas del reporte
		DataSource dataSource = new DataSource(cols);


		for (DtoConsolidadoCierreReporte dto:consolidadoCierre) {
			Object[] tem = new Object[tamanio+5];
			tem[0]=dto.getIntitucion();
			tem[1]=dto.getCentroAtencion();
			tem[2]=dto.getCaja();
			tem[3]=dto.getCajero();
			tem[4]=dto.getHoraTurnoCajero();

			for (int j = 0; j < dto.getTotalesPorFormaPago().size(); j++) {
				tem[j+5]=dto.getTotalesPorFormaPago().get(j);
			}
			if (!dto.isEsTotalJsp() && dto.getIntitucion()!=null) {
				dataSource.add(tem);	
			}
		}

		return dataSource;
	}


}
