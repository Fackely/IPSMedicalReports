package com.servinte.axioma.generadorReporte.capitacion.ordenesCapitacionSubcontratada;

import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;
import util.reportes.dinamico.DataSource;

import com.servinte.axioma.dto.capitacion.DtoGrupoClaseReporte;
import com.servinte.axioma.dto.capitacion.DtoNivelReporte;
import com.servinte.axioma.dto.capitacion.DtoProductoServicioReporte;


/**
 * Clase para generar el data source de los niveles de atención
 *  necesario en el formato C
 * 
 * @version 1.0, May 11, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class NivelesDataSource {
	
	/**
	 * Atributo que representa la lista de niveles de atención a la cual se le obtiene
	 * el datasource
	 */
	private ArrayList<DtoNivelReporte> niveles;
	
	/**
	 * Constructor de la clase
	 * @param niveles
	 */
	public NivelesDataSource(ArrayList<DtoNivelReporte> niveles) {
		this.setNiveles(niveles);
	}
	
	/**
	 * Método encargado de crear el datasource de los niveles de atención 
	 * para el formato C
	 * @return
	 */
	public JRDataSource crearDataSource(){
		DataSource dataSource = new DataSource("nombreNivel", "nombreGrupoClase",
										"nombreProductoServicio", "totalPresupuestado", "totalOrdenado", "porcentajeOrdenado", 
										"totalAutorizado", "porcentajeAutorizado", "totalCargosCuenta", 
										"porcentajeCargos", "totalFacturado", "porcentajeFacturado");
		for(DtoNivelReporte nivel:this.getNiveles()){
			for(DtoGrupoClaseReporte grupoClase:nivel.getGruposClases()){
				for(DtoProductoServicioReporte productoServicio:grupoClase.getProductosServicios()){
					dataSource.add(nivel.getNombre(), grupoClase.getNombre(),
							productoServicio.getNombre(), productoServicio.getTotalPresupuestado(), 
							productoServicio.getTotalOrdenado(), productoServicio.getPorcentajeOrdenado(), 
							productoServicio.getTotalAutorizado(), productoServicio.getPorcentajeAutorizado(), 
							productoServicio.getTotalCargosCuenta(), productoServicio.getPorcentajeCargos(), 
							productoServicio.getTotalFacturado(), productoServicio.getPorcentajeFacturado());
				}
				
			}
		}
		return dataSource;
	}

		
	/**
	 * @return the niveles
	 */
	public ArrayList<DtoNivelReporte> getNiveles() {
		return niveles;
	}

	/**
	 * @param niveles the niveles to set
	 */
	public void setNiveles(ArrayList<DtoNivelReporte> niveles) {
		this.niveles = niveles;
	}
}
