package com.servinte.axioma.generadorReporte.odontologia.tiempoEsperaAtencionCitasOdontologicas;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.ConstantesIntegridadDominio;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.odontologia.DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoReporteTiempoEsperaAtencionCitasOdontoGen;

public class GeneradorReporteTiempoEsperaAtencionCitasOdonto extends GeneradorReporte  {
	/** Parametros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	
	private ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> listadoResultado;
	private DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas filtroTiempoEspera;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/tiempoEsperaAtencionCitasOdontologicas/reporteTiempoEsperaAtencionCitaOdonto.jasper";
	private static String RUTA_SUBREPORTE_RESULTADOCONSULTA = "com/servinte/axioma/generadorReporte/odontologia/tiempoEsperaAtencionCitasOdontologicas/subReporteTiempoEsperaAtencionCitaOdonto.jasper";
	private static String NOMBRE_SUBREPORTE_RESULTADOCONSULTA = "subReporteTiempoEsperaAtencionCitaOdonto";
	private static String RUTA_SUBREPORTE_ESPECIALIDAD = "com/servinte/axioma/generadorReporte/odontologia/tiempoEsperaAtencionCitasOdontologicas/subReporteEspecialidades.jasper";
	private static String NOMBRE_SUBREPORTE_ESPECIALIDAD = "subReporteEspecialidades";
	@SuppressWarnings("unused")
	private String RUTA_LOGO = "";
	
	@SuppressWarnings("unused")
	private  String NOMBRE_LOGO = "nombreLogo";
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Fabian Becerra
	 */
	public GeneradorReporteTiempoEsperaAtencionCitasOdonto() {
	}
	
	/**
	 * M&eacute;todo constructor de la clase 
	 * @param listadoResultado
	 * @param filtroCambioSer
	 *
	 * @author Fabian Becerra
	 */
	public GeneradorReporteTiempoEsperaAtencionCitasOdonto(ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> listadoResultado, 
			DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas filtroTiempoEspera){	
		
		this.listadoResultado = listadoResultado;
		this.filtroTiempoEspera = filtroTiempoEspera;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection obtenerDatosReporte() {
Collection<DtoReporteTiempoEsperaAtencionCitasOdontoGen>  collectionDTOGeneral= new ArrayList();
		
DtoReporteTiempoEsperaAtencionCitasOdontoGen dtoGeneral = new  DtoReporteTiempoEsperaAtencionCitasOdontoGen();	
		RUTA_LOGO = "../"+ filtroTiempoEspera.getRutaLogo();
		
		if (listadoResultado != null) {
			
			if (filtroTiempoEspera != null) {
				
				String ubicacionLogo = filtroTiempoEspera.getUbicacionLogo();
				String rutaLogo = "../"+filtroTiempoEspera.getRutaLogo();
		
				dtoGeneral.setRazonSocial(filtroTiempoEspera.getRazonSocial());
				dtoGeneral.setUsuarioProcesa(filtroTiempoEspera.getNombreUsuario());
				dtoGeneral.setFechaInicial(filtroTiempoEspera.getFechaInicialF());
				dtoGeneral.setFechaFinal(filtroTiempoEspera.getFechaFinalF());
				
				StringBuilder tmpServicios= new StringBuilder();
				for(int i=0;i<filtroTiempoEspera.getServicios().size();i++)
				{
					String tmp = filtroTiempoEspera.getServicios().get(i).getDescripcionPropietarioServicio();
					tmpServicios.append(tmp);
					if(i!=filtroTiempoEspera.getServicios().size()-1)	
					tmpServicios.append("\n"); // para hacer un enter
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
								
				boolean existeLogo = existeLogo(rutaLogo);
				
				if (existeLogo) {
					if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
						dtoGeneral.setLogoDerecha(rutaLogo);
						dtoGeneral.setLogoIzquierda(null);
					} else if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
						dtoGeneral.setLogoIzquierda(rutaLogo);
						dtoGeneral.setLogoDerecha(null);
					}
				}else{
					dtoGeneral.setLogoDerecha(null);
					dtoGeneral.setLogoIzquierda(null);
				}
				
			}
			
			for (DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto registro : listadoResultado) {
				registro.setDsResultadoEspecialidades(new JRBeanCollectionDataSource(registro.getListaTiempoEsperaEspecialidad()));
			}
			
			dtoGeneral.setDsResultadoTiempoEspera(new JRBeanCollectionDataSource(listadoResultado));
			collectionDTOGeneral.add(dtoGeneral);
		}
		
		return collectionDTOGeneral;
	}

	@SuppressWarnings("unused")
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
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ESPECIALIDAD);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_ESPECIALIDAD);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_ESPECIALIDAD, mySubreportObj);
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
