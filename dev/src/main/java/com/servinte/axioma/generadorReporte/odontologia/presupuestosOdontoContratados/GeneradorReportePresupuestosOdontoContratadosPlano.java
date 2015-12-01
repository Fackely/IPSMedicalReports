package com.servinte.axioma.generadorReporte.odontologia.presupuestosOdontoContratados;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.odontologia.DtoConsolidadoPresupuestoContratadoPorEstado;
import com.princetonsa.dto.odontologia.DtoPresupuestosOdontologicosContratados;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratados;

public class GeneradorReportePresupuestosOdontoContratadosPlano extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado> listadoResultado;
	DtoReportePresupuestosOdontologicosContratados filtroIngresos;
	/*************  Planos *****************/
	private static String RUTA_REPORTE_GENERAL_PLANO = "com/servinte/axioma/generadorReporte/odontologia/presupuestosOdontoContratados/reportePresupuestosContratadosPlano.jasper";
	private static String RUTA_SUBREPORTE_PRESUPUESTOSPORESTADOSPLANO = "com/servinte/axioma/generadorReporte/odontologia/presupuestosOdontoContratados/subreportePresupuestosPorEstadosPlano.jasper";
	private static String NOMBRE_SUBREPORTE_PRESUPUESTOSPORESTADOSPLANO = "subreportePresupuestosPorEstadosPlano";
	/***********  Planos ******************/
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Diana Carolina G
	 */
	public GeneradorReportePresupuestosOdontoContratadosPlano() {
	
	}
	
	
	public GeneradorReportePresupuestosOdontoContratadosPlano(ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado> listadoResultado, 
			DtoReportePresupuestosOdontologicosContratados filtroIngresos){	
		
		this.listadoResultado = listadoResultado;
		this.filtroIngresos = filtroIngresos;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DtoConsolidadoPresupuestoContratadoPorEstado>  collectionDTOGeneral= new ArrayList();
		
		DtoConsolidadoPresupuestoContratadoPorEstado dtoGeneral = new DtoConsolidadoPresupuestoContratadoPorEstado();	
		
		if (listadoResultado != null) {
			
			if (filtroIngresos != null) {
				
				dtoGeneral.setFechaInicial(filtroIngresos.getFechaInicialFormateada());
				dtoGeneral.setFechaFinal(filtroIngresos.getFechaFinalFormateada());
				dtoGeneral.setTotalEstadoContratado(filtroIngresos.getTotalEstadoContratado());
				
				
				
				
				
			}
			
			 //Se crea el Datasource del Consolidado del Reporte
			ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado> listaEstados= new ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado>();
			DtoConsolidadoPresupuestoContratadoPorEstado dtoResultado = null;
			//int j=ConstantesBD.codigoNuncaValido;
			for (DtoConsolidadoPresupuestoContratadoPorEstado dto : listadoResultado) {			  //Se crea el Datasource del Consolidado por Estado
				
			//	int totalEstado = dto.getTotalEstadoContratado();
				
				for(DtoPresupuestosOdontologicosContratados dtoEstado: dto.getListadoConsolidadoPorEstado()){
					
					dtoResultado = new DtoConsolidadoPresupuestoContratadoPorEstado();
					
					dtoResultado.setDescCentroAtencionContrato(dtoEstado.getDescCentroAtencionContrato());
					dtoResultado.setDescripcionCiudad(dtoEstado.getDescripcionCiudad());
					dtoResultado.setDescripcionPais(dtoEstado.getDescripcionPais());
					dtoResultado.setDescripcionRegionCobertura(dtoEstado.getDescripcionRegionCobertura());
					dtoResultado.setDescripcionEmpresaInstitucion(dtoEstado.getDescripcionEmpresaInstitucion());
					dtoResultado.setNombreInstitucion(dtoEstado.getNombreInstitucion());
					
					dtoResultado.setDescCentroAtencionDuenio(dtoEstado.getDescCentroAtencionDuenio());
					dtoResultado.setFechaContrato(dtoEstado.getFechaContrato());
					dtoResultado.setNumeroContrato(dtoEstado.getNumeroContrato());
					dtoResultado.setTipoId(dtoEstado.getTipoId());
					dtoResultado.setNumeroId(dtoEstado.getNumeroId());
					dtoResultado.setPrimerApellido(dtoEstado.getPrimerApellido());
					dtoResultado.setSegundoApellido(dtoEstado.getSegundoApellido());
					dtoResultado.setPrimerNombre(dtoEstado.getPrimerNombre());
					dtoResultado.setSegundoNombre(dtoEstado.getSegundoNombre());
					dtoResultado.setPrimerNombreProfesionalContrato(dtoEstado.getPrimerNombreProfesionalContrato());
					dtoResultado.setPrimerApellidoProfesionalContrato(dtoEstado.getPrimerApellidoProfesionalContrato());
					dtoResultado.setPrimerNombreProfesionalValoro(dtoEstado.getPrimerNombreProfesionalValoro());
					dtoResultado.setPrimerApellidoProfesionalValoro(dtoEstado.getPrimerApellidoProfesionalValoro());
				
					dtoResultado.setCodigoPaquete(dtoEstado.getCodigoPaquete());
					dtoResultado.setCodigoPrograma(dtoEstado.getCodigoPrograma());
					dtoResultado.setValorPresupuesto(dtoEstado.getValorPresupuesto());
					dtoResultado.setAyudanteEstadoPresupuesto(dtoEstado.getAyudanteEstadoPresupuesto());
					dtoResultado.setCodigoPkPresupuesto(dtoEstado.getCodigoPkPresupuesto());
		
					listaEstados.add(dtoResultado); 
				
					
					// Trae los codigos por separado del presupuesto odontologico
					/*String [] codigoPrograma=null;
					
					if(!UtilidadTexto.isEmpty(dtoEstado.getCodigoPrograma())){
						codigoPrograma = dtoEstado.getCodigoPrograma().split("\n" );
						
					//	dtoResultado.setCodigoPrograma(codigoPrograma[0]);
						
						StringBuilder tmpProgramas= new StringBuilder();
						tmpProgramas.append(codigoPrograma);
						tmpProgramas.append("-");
						
						String tmp ="";
						if(tmpProgramas!=null)
						{
							tmp=tmpProgramas.toString();
						}
						
						dtoResultado.setCodigoPrograma(tmp);
						
						//Prueba
						dtoResultado.setDescCentroAtencionContrato(dtoEstado.getDescCentroAtencionContrato());
						dtoResultado.setDescripcionCiudad(dtoEstado.getDescripcionCiudad());
						dtoResultado.setDescripcionPais(dtoEstado.getDescripcionPais());
						dtoResultado.setDescPaisOriginalPlano(dtoEstado.getDescPaisOriginalPlano());
						dtoResultado.setDescripcionRegionCobertura(dtoEstado.getDescripcionRegionCobertura());
						dtoResultado.setDescripcionEmpresaInstitucion(dtoEstado.getDescripcionEmpresaInstitucion());
						dtoResultado.setNombreInstitucion(dtoEstado.getNombreInstitucion());
						
						dtoResultado.setDescCentroAtencionDuenio(dtoEstado.getDescCentroAtencionDuenio());
						dtoResultado.setFechaContrato(dtoEstado.getFechaContrato());
						dtoResultado.setNumeroContrato(dtoEstado.getNumeroContrato());
						dtoResultado.setTipoId(dtoEstado.getTipoId());
						dtoResultado.setNumeroId(dtoEstado.getNumeroId());
						dtoResultado.setPrimerApellido(dtoEstado.getPrimerApellido());
						dtoResultado.setSegundoApellido(dtoEstado.getSegundoApellido());
						dtoResultado.setPrimerNombre(dtoEstado.getPrimerNombre());
						dtoResultado.setSegundoNombre(dtoEstado.getSegundoNombre());
						dtoResultado.setPrimerNombreProfesionalContrato(dtoEstado.getPrimerNombreProfesionalContrato());
						dtoResultado.setPrimerApellidoProfesionalContrato(dtoEstado.getPrimerApellidoProfesionalContrato());
						dtoResultado.setPrimerNombreProfesionalValoro(dtoEstado.getPrimerNombreProfesionalValoro());
						dtoResultado.setPrimerApellidoProfesionalValoro(dtoEstado.getPrimerApellidoProfesionalValoro());
					
						dtoResultado.setCodigoPaquete(dtoEstado.getCodigoPaquete());
						dtoResultado.setValorPresupuesto(dtoEstado.getValorPresupuesto());
						dtoResultado.setAyudanteEstadoPresupuesto(dtoEstado.getAyudanteEstadoPresupuesto());
						dtoResultado.setCodigoPkPresupuesto(dtoEstado.getCodigoPkPresupuesto());
						//Prueba
						
					}
					listaEstados.add(dtoResultado); // Adiciona a la listaDtoConsolidado el codigo en la primera posicion
					
					if(codigoPrograma!=null && codigoPrograma.length>1){
						
						for(int i=1; i<codigoPrograma.length; i++)
							
						{
							
							dtoResultado = new DtoConsolidadoPresupuestoContratadoPorEstado();
							dtoResultado.setCodigoPrograma(codigoPrograma[i]); //Adiciona la listaDtoConsolidado las siguientes posiciones 
							
							//Prueba
							dtoResultado.setDescCentroAtencionContrato(dtoEstado.getDescCentroAtencionContrato());
							dtoResultado.setDescripcionCiudad(dtoEstado.getDescripcionCiudad());
							dtoResultado.setDescripcionPais(dtoEstado.getDescripcionPais());
							dtoResultado.setDescPaisOriginalPlano(dtoEstado.getDescPaisOriginalPlano());
							dtoResultado.setDescripcionRegionCobertura(dtoEstado.getDescripcionRegionCobertura());
							dtoResultado.setDescripcionEmpresaInstitucion(dtoEstado.getDescripcionEmpresaInstitucion());
							dtoResultado.setNombreInstitucion(dtoEstado.getNombreInstitucion());
							
							dtoResultado.setDescCentroAtencionDuenio(dtoEstado.getDescCentroAtencionDuenio());
							dtoResultado.setFechaContrato(dtoEstado.getFechaContrato());
							dtoResultado.setNumeroContrato(dtoEstado.getNumeroContrato());
							dtoResultado.setTipoId(dtoEstado.getTipoId());
							dtoResultado.setNumeroId(dtoEstado.getNumeroId());
							dtoResultado.setPrimerApellido(dtoEstado.getPrimerApellido());
							dtoResultado.setSegundoApellido(dtoEstado.getSegundoApellido());
							dtoResultado.setPrimerNombre(dtoEstado.getPrimerNombre());
							dtoResultado.setSegundoNombre(dtoEstado.getSegundoNombre());
							dtoResultado.setPrimerNombreProfesionalContrato(dtoEstado.getPrimerNombreProfesionalContrato());
							dtoResultado.setPrimerApellidoProfesionalContrato(dtoEstado.getPrimerApellidoProfesionalContrato());
							dtoResultado.setPrimerNombreProfesionalValoro(dtoEstado.getPrimerNombreProfesionalValoro());
							dtoResultado.setPrimerApellidoProfesionalValoro(dtoEstado.getPrimerApellidoProfesionalValoro());
						
							dtoResultado.setCodigoPaquete(dtoEstado.getCodigoPaquete());
							dtoResultado.setValorPresupuesto(dtoEstado.getValorPresupuesto());
							dtoResultado.setAyudanteEstadoPresupuesto(dtoEstado.getAyudanteEstadoPresupuesto());
							dtoResultado.setCodigoPkPresupuesto(dtoEstado.getCodigoPkPresupuesto());
							//Prueba
							
							listaEstados.add(dtoResultado); 
						}
						
					} */
				} 
				//listaEstados.add(dtoResultado); 
			} 
			
			dtoGeneral.setDsResultadoConsulta(new JRBeanCollectionDataSource(listaEstados)); 
			collectionDTOGeneral.add(dtoGeneral);
		}
		
		return collectionDTOGeneral;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        
		 try {
		        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_PRESUPUESTOSPORESTADOSPLANO);
		        if (myInFile == null) {
		           	myInFile = new FileInputStream(RUTA_SUBREPORTE_PRESUPUESTOSPORESTADOSPLANO);
		           	
				}else if (myInFile != null) {
		            Object mySubreportObj = JRLoader.loadObject(myInFile);
		            parametrosReporte.put(NOMBRE_SUBREPORTE_PRESUPUESTOSPORESTADOSPLANO, mySubreportObj);
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
		return RUTA_REPORTE_GENERAL_PLANO;
	}
	
}
