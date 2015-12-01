package com.servinte.axioma.generadorReporte.odontologia.tiempoEsperaAtencionCitasOdontologicas;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.odontologia.DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoReporteTiempoEsperaAtencionCitasOdontoGen;

public class GeneradorReportePlanoTiempoEsperaAtencionCitasOdonto extends GeneradorReporte{
	/** Parametros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	
	private ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> listadoResultado;
	private DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas filtroTiempoEspera;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/tiempoEsperaAtencionCitasOdontologicas/reporteTiempoEsperaAtencionCitaOdontoPlano.jasper";
	private static String RUTA_SUBREPORTE_RESULTADOCONSULTA = "com/servinte/axioma/generadorReporte/odontologia/tiempoEsperaAtencionCitasOdontologicas/subReporteTiempoEsperaAtencionCitaOdontoPlano.jasper";
	private static String NOMBRE_SUBREPORTE_RESULTADOCONSULTA = "subReporteTiempoEsperaAtencionCitaOdontoPlano";
	
	/**
	 * 
	 * Méodo constructor de la clase 
	 *
	 * @author Fabian Becerra
	 */
	public GeneradorReportePlanoTiempoEsperaAtencionCitasOdonto() {
	}
	
	/**
	 * Método constructor de la clase 
	 * @param listadoResultado
	 * @param filtroTiempoEspera
	 *
	 * @author Fabian Becerra
	 */
	public GeneradorReportePlanoTiempoEsperaAtencionCitasOdonto(ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> listadoResultado, 
			DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas filtroTiempoEspera){	
		
		this.listadoResultado = listadoResultado;
		this.filtroTiempoEspera = filtroTiempoEspera;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection obtenerDatosReporte() {
Collection<DtoReporteTiempoEsperaAtencionCitasOdontoGen>  collectionDTOGeneral= new ArrayList();
		
DtoReporteTiempoEsperaAtencionCitasOdontoGen dtoGeneral = new  DtoReporteTiempoEsperaAtencionCitasOdontoGen();	
		
		if (listadoResultado != null) {
			
			if (filtroTiempoEspera != null) {
				
				dtoGeneral.setFechaInicial(filtroTiempoEspera.getFechaInicialF());
				dtoGeneral.setFechaFinal(filtroTiempoEspera.getFechaFinalF());
				StringBuilder tmpServicios= new StringBuilder();
				for(int i=0;i<filtroTiempoEspera.getServicios().size();i++)
				{
					String tmp = filtroTiempoEspera.getServicios().get(i).getDescripcionPropietarioServicio();
					tmpServicios.append(tmp);
					if(i!=filtroTiempoEspera.getServicios().size()-1)	
					tmpServicios.append("-"); // para hacer un enter
				}
				String servicios ="";
				if(tmpServicios!=null)
				{
					servicios=tmpServicios.toString();
				}
				if(servicios.equals(""))
				{
					servicios="Todos";
				}
				dtoGeneral.setServicios(servicios);
				
			}
			
			
			dtoGeneral.setDsResultadoTiempoEspera(new JRBeanCollectionDataSource(listadoResultado));
			collectionDTOGeneral.add(dtoGeneral);
		}
		
		return collectionDTOGeneral;
	}

	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        

        try {
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_RESULTADOCONSULTA);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_RESULTADOCONSULTA);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_RESULTADOCONSULTA, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        
		}catch (Exception e) {
			e.printStackTrace();
		}
		
        return parametrosReporte;
	}

	@Override
	public String obtenerRutaPlantilla() {
		return RUTA_REPORTE_GENERAL;
	}

	
}
