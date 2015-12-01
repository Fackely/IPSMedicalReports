package com.servinte.axioma.generadorReporte.manejoPaciente.usuariosConsumidoresPresupuesto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axioma.util.log.Log4JManager;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.ConstantesIntegridadDominio;
import util.reportes.GeneradorReporte;

import com.princetonsa.mundo.InstitucionBasica;
import com.servinte.axioma.dto.manejoPaciente.EncabezadoRepUsuConDto;
import com.servinte.axioma.dto.manejoPaciente.InfoUsuariosConsumidoresArchivoPlanoDto;
import com.servinte.axioma.dto.manejoPaciente.PacienteConsumidorPresupuestoDto;
import com.servinte.axioma.dto.manejoPaciente.UsuariosConsumidoresArchivoPlanoDto;
import com.servinte.axioma.dto.manejoPaciente.UsuariosConsumidoresGeneralDto;
import com.servinte.axioma.dto.manejoPaciente.UsuariosConsumidoresPresupuestoDto;
import com.servinte.axioma.dto.manejoPaciente.ValorClaseInventariosPacienteDto;
import com.servinte.axioma.dto.manejoPaciente.ValorGruposServicioPacienteDto;
import com.sun.istack.logging.Logger;

public class GeneradorReporteUsuariosConsumidoresPresupuesto extends GeneradorReporte{

	
	/** Parametros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	
	private List<UsuariosConsumidoresPresupuestoDto> dtoResultado=new ArrayList<UsuariosConsumidoresPresupuestoDto>();
	private EncabezadoRepUsuConDto dtoEncabezado;
	private String autorizado;
	private String tipoSalida= null;
	private String criteriosBusqueda;
	private String rango;
	private InfoUsuariosConsumidoresArchivoPlanoDto infoUsuariosConsumidoresArchivoPlanoDto;
	
	/**
	 * M&eacute;todo constructor de la clase 
	 * @param listadoResultado
	 * 	 *
	 * @author Camilo Gómez
	 */
	public GeneradorReporteUsuariosConsumidoresPresupuesto(List<UsuariosConsumidoresPresupuestoDto> dtoResultado, EncabezadoRepUsuConDto dtoEncabezado, String autorizado, String tipoReporte, String criteriosBusqueda){	
		
		this.dtoResultado  = dtoResultado;
		this.dtoEncabezado = dtoEncabezado;
		this.autorizado= autorizado;
		this.tipoSalida=tipoReporte;
		this.criteriosBusqueda=criteriosBusqueda;
		
	}
	public GeneradorReporteUsuariosConsumidoresPresupuesto(List<UsuariosConsumidoresPresupuestoDto> dtoResultado, String autorizado, String tipoSalida, String rango){	
		
		this.dtoResultado  = dtoResultado;
		this.dtoEncabezado = null;
		this.autorizado= autorizado;
		this.tipoSalida= tipoSalida;
		this.rango=rango;
	}
  
	/**
	 * 
	 * @param dtoResultado
	 * @param autorizado
	 * @param tipoSalida
	 * @param rango
	 * @param infoUsuariosConsumidoresArchivoPlanoDto
	 * @author hermorhu
	 * @created 22-Abr-2013 
	 */
	public GeneradorReporteUsuariosConsumidoresPresupuesto(List<UsuariosConsumidoresPresupuestoDto> dtoResultado, String autorizado, String tipoSalida, String rango, InfoUsuariosConsumidoresArchivoPlanoDto infoUsuariosConsumidoresArchivoPlanoDto){	
		
		this.dtoResultado  = dtoResultado;
		this.dtoEncabezado = null;
		this.autorizado= autorizado;
		this.tipoSalida= tipoSalida;
		this.rango=rango;
		this.infoUsuariosConsumidoresArchivoPlanoDto = infoUsuariosConsumidoresArchivoPlanoDto;
	}
	
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/manejoPaciente/usuariosConsumidoresPresupuesto/usuarioConsumidorPresupuesto.jasper";
	
	private static String RUTA_SUBREPORTE_GRUPO_SERVICIOS_AUTORIZADOS = "com/servinte/axioma/generadorReporte/manejoPaciente/usuariosConsumidoresPresupuesto/subreporteGruposServicioAutorizados.jasper";
	private static String NOMBRE_SUBREPORTE_GRUPO_SERVICIOS_AUTORIZADOS= "subreporteGruposServicioAutorizados";
	private static String RUTA_SUBREPORTE_CLASE_INVENTARIOS_AUTORIZADAS = "com/servinte/axioma/generadorReporte/manejoPaciente/usuariosConsumidoresPresupuesto/subreporteClaseInventarioAutorizadas.jasper";
	private static String NOMBRE_SUBREPORTE_CLASE_INVENTARIOS_AUTORIZADAS = "subreporteClaseInventarioAutorizadas";
	private static String RUTA_SUBREPORTE_GRUPO_SERVICIOS_FACTURADOS =   "com/servinte/axioma/generadorReporte/manejoPaciente/usuariosConsumidoresPresupuesto/subreporteGruposServiciosFacturados.jasper";
	private static String NOMBRE_SUBREPORTE_GRUPO_SERVICIOS_FACTURADOS = "subreporteGruposServiciosFacturados";
	private static String RUTA_SUBREPORTE_CLASE_INVENTARIOS_FACTURADAS = "com/servinte/axioma/generadorReporte/manejoPaciente/usuariosConsumidoresPresupuesto/subreporteClaseInventariosFacturada.jasper";
	private static String NOMBRE_SUBREPORTE_CLASE_INVENTARIOS_FACTURADAS = "subreporteClaseInventariosFacturada";
	
	private static String RUTA_SUBREPORTE_USUARIOS_CONSUMIDORES_PRESUPUESTO_PLANO = "com/servinte/axioma/generadorReporte/manejoPaciente/usuariosConsumidoresPresupuesto/usuariosConsumidoresPresupuestoPlano.jasper";
	private static String NOMBRE_SUBREPORTE_USUARIOS_CONSUMIDORES_PRESUPUESTO_PLANO = "usuariosConsumidoresPresupuestoPlano";
	
	/**FIXME ------->AQUI VA LA RUTA Y EL NOMBRE DE LOS SUBREPORTES*/
	
	@SuppressWarnings("unused")
	private String RUTA_LOGO = "";
	
	@SuppressWarnings("unused")
	private  String NOMBRE_LOGO = "nombreLogo";
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Camilo Gómez
	 */
	public GeneradorReporteUsuariosConsumidoresPresupuesto() {
	
	}
	

	
	
	@SuppressWarnings({ "unchecked", "rawtypes", "null" })
	@Override	
	public Collection obtenerDatosReporte() 
	{
		
		Collection<UsuariosConsumidoresGeneralDto>  collectionDTOGeneral= new ArrayList();
//		Collection<UsuariosConsumidoresArchivoPlanoDto> collectionDtoPlano= new ArrayList();
		Collection<InfoUsuariosConsumidoresArchivoPlanoDto> collectionDtoPlano = new ArrayList<InfoUsuariosConsumidoresArchivoPlanoDto>();
		UsuariosConsumidoresGeneralDto dtoGeneral = new  UsuariosConsumidoresGeneralDto();	
		UsuariosConsumidoresArchivoPlanoDto dtoPlano = new UsuariosConsumidoresArchivoPlanoDto();
		List<UsuariosConsumidoresArchivoPlanoDto> listUsuariosConsumidoresArchivoPlanoDto = new ArrayList<UsuariosConsumidoresArchivoPlanoDto>();
		
		//Genrea Reportes en formato Archivo Plano
		if (tipoSalida.equals("2"))
		{		
			if (dtoPlano != null) 	{
			collectionDtoPlano=new ArrayList<InfoUsuariosConsumidoresArchivoPlanoDto>();
			
			for (UsuariosConsumidoresPresupuestoDto usuarios: dtoResultado)
			{  
			List<ValorGruposServicioPacienteDto> listaGrupos= null;
			 listaGrupos =  usuarios.getListaValoresGrupoServicio();
			 for (ValorGruposServicioPacienteDto grupo: listaGrupos)
			 {	String nombrePacientes=usuarios.getPrimerNombrePersona();
				try {if (!usuarios.getSegundoNombrePersona().equals(null))
				    nombrePacientes= nombrePacientes+" "+usuarios.getSegundoNombrePersona();}
					catch(Exception e){ 				
					}
					 nombrePacientes= nombrePacientes+" "+usuarios.getPrimerApellidoPersona();
					try {if (!usuarios.getSegundoApellidoPersona().equals(null))
					nombrePacientes= nombrePacientes+" "+usuarios.getSegundoApellidoPersona();}
					catch(Exception e){ 				
					}
				dtoPlano=new UsuariosConsumidoresArchivoPlanoDto();	
			 	dtoPlano.setNombrePaciente(nombrePacientes);
			 	dtoPlano.setNombreTipoIdentificacion(usuarios.getNombreTipoIdentificacion());
				dtoPlano.setNumeroIdentificacion(usuarios.getNumeroIdentificacion());
				dtoPlano.setNombreGrupoServicio(grupo.getNombreGrupoServicio());
				dtoPlano.setCantidadAutorizadaServ(grupo.getCantidadAutorizada());
				dtoPlano.setValorAutorizadoServ(grupo.getValorAutorizado());
				if (autorizado.equals("2"))
				{
				dtoPlano.setValorFacturadoServ(grupo.getValorFacturado());
				}
				dtoPlano.setCantidadAutorizada(usuarios.getCantidadAutorizada());
				dtoPlano.setCantidadIngresos(usuarios.getCantidadIngreso());
				dtoPlano.setRangoMeses(rango);
				dtoPlano.setAutorizado(autorizado);
//				collectionDtoPlano.add(dtoPlano);
				listUsuariosConsumidoresArchivoPlanoDto.add(dtoPlano);
			 }
	         List<ValorClaseInventariosPacienteDto> listaClase= null;
			 listaClase= usuarios.getListaValoresClaseInventario();
			 for (ValorClaseInventariosPacienteDto grupo: listaClase)
			 {	String nombrePacientes=usuarios.getPrimerNombrePersona();
				try {if (!usuarios.getSegundoNombrePersona().equals(null))
				    nombrePacientes= nombrePacientes+" "+usuarios.getSegundoNombrePersona();}
					catch(Exception e){ 				
					}
					 nombrePacientes= nombrePacientes+" "+usuarios.getPrimerApellidoPersona();
					try {if (!usuarios.getSegundoApellidoPersona().equals(null))
					nombrePacientes= nombrePacientes+" "+usuarios.getSegundoApellidoPersona();}
					catch(Exception e){ 				
					}
				dtoPlano=new UsuariosConsumidoresArchivoPlanoDto();	
			 	dtoPlano.setNombrePaciente(nombrePacientes);
			 	dtoPlano.setNombreTipoIdentificacion(usuarios.getNombreTipoIdentificacion());
				dtoPlano.setNumeroIdentificacion(usuarios.getNumeroIdentificacion());
				dtoPlano.setNombreGrupoServicio(grupo.getnombreClaseInventario());
				dtoPlano.setCantidadAutorizadaServ(grupo.getCantidadAutorizada());
				dtoPlano.setValorAutorizadoServ(grupo.getValorAutorizado());
				if (autorizado.equals("2"))
				{
				dtoPlano.setValorFacturadoServ(grupo.getValorFacturado());
				}
				dtoPlano.setCantidadAutorizada(usuarios.getCantidadAutorizada());
				dtoPlano.setCantidadIngresos(usuarios.getCantidadIngreso());
				dtoPlano.setRangoMeses(rango);
				dtoPlano.setAutorizado(autorizado);
//				collectionDtoPlano.add(dtoPlano);
				listUsuariosConsumidoresArchivoPlanoDto.add(dtoPlano);
						 }
		}
			
		//hermorhu - MT5819	
		//Se envian los para el reporte en archivo plano
		//los criterios de busqueda se setean desde el Action
		infoUsuariosConsumidoresArchivoPlanoDto.setJRDUsuariosConsumidoresPresupuesto(new JRBeanCollectionDataSource(listUsuariosConsumidoresArchivoPlanoDto));
		collectionDtoPlano.add(infoUsuariosConsumidoresArchivoPlanoDto);
		
		}
		return collectionDtoPlano;
					
					
		}
		
		// Genera Reportes en formato Hoja de Calculo Excel
		else if (tipoSalida.equals("3"))
		{	
			if (dtoResultado != null) 
			{ String name="";
			collectionDtoPlano=new ArrayList();
			double gAutT=0, gFactT=0, gCantT=0;
			
							for (UsuariosConsumidoresPresupuestoDto usuarios: dtoResultado)
			{   dtoGeneral = new UsuariosConsumidoresGeneralDto();	
			    double autT=0, factT=0, cantT=0;
				String nombrePacientes=usuarios.getPrimerNombrePersona();
				try {if (!usuarios.getSegundoNombrePersona().equals(null))
					nombrePacientes= nombrePacientes+" "+usuarios.getSegundoNombrePersona();}
				catch(Exception e){ 				
				}
				nombrePacientes= nombrePacientes+" "+usuarios.getPrimerApellidoPersona();
				try {if (!usuarios.getSegundoApellidoPersona().equals(null))
					nombrePacientes= nombrePacientes+" "+usuarios.getSegundoApellidoPersona();}
				catch(Exception e){ 				
				}
				
			    if (!name.equals(nombrePacientes)){
			    	name=nombrePacientes;
			    String rutaLogo = "../"+dtoEncabezado.getRutaLogo();
				dtoGeneral.setRazonSocial(dtoEncabezado.getRazonSocial());	
				dtoGeneral.setCentroAtencion("Centro de Atencion: " + dtoEncabezado.getCentroAtencion());
				dtoGeneral.setTelefono("Teléfono: "+dtoEncabezado.getTelefono());			
				dtoGeneral.setUsuario(dtoEncabezado.getUsuario());			
				dtoGeneral.setDireccion("Direccion: "+dtoEncabezado.getDireccion());			
				dtoGeneral.setActividadEconomica(dtoEncabezado.getActividadEconomica());			
				dtoGeneral.setNit("NIT: "+dtoEncabezado.getNit());
				dtoGeneral.setTipoImpresion(dtoEncabezado.getTipoImpresion());
					
				boolean existeLogo = existeLogo(rutaLogo);
				
				if (existeLogo) {
					if (dtoEncabezado.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
						dtoGeneral.setLogoDerecha(rutaLogo);
						dtoGeneral.setLogoIzquierda(null);
					} else if (dtoEncabezado.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
						dtoGeneral.setLogoIzquierda(rutaLogo);
						dtoGeneral.setLogoDerecha(null);
					}
				}else{
					dtoGeneral.setLogoDerecha(null);
					dtoGeneral.setLogoIzquierda(null);
				}
				dtoGeneral.setCriteriosBusqueda(criteriosBusqueda);
				dtoGeneral.setNombrePaciente(nombrePacientes);
				dtoGeneral.setNumeroIdentificacion(usuarios.getNumeroIdentificacion());
				dtoGeneral.setNombreTipoIdentificacion(usuarios.getNombreTipoIdentificacion()); 
				dtoGeneral.setListaValoresClaseInventario(new JRBeanCollectionDataSource(usuarios.getListaValoresClaseInventario()));
				dtoGeneral.setListaValoresGrupoServicio(new JRBeanCollectionDataSource(usuarios.getListaValoresGrupoServicio()));
				dtoGeneral.setCantidadAutorizada(usuarios.getCantidadAutorizada());
				dtoGeneral.setCantidadIngresos(usuarios.getCantidadIngreso());
				
				try{
					if (usuarios.getListaValoresClaseInventario()!= null)
					{
					for (ValorClaseInventariosPacienteDto val: usuarios.getListaValoresClaseInventario())
					{
						autT= autT + val.getValorAutorizado();
						factT=factT + val.getValorFacturado();
						cantT=cantT + val.getCantidadAutorizada();
						
					}
					}
					}catch (Exception e){}
					
					try{
					if (usuarios.getListaValoresGrupoServicio() != null)
					{
					for (ValorGruposServicioPacienteDto val: usuarios.getListaValoresGrupoServicio())
					{
						autT= autT + val.getValorAutorizado();
						factT=factT + val.getValorFacturado();
						cantT=cantT + val.getCantidadAutorizada();
						
					}
					}
					} catch (Exception e){}
					
				dtoGeneral.setTotalAutUsu(autT);
				dtoGeneral.setTotalFacUsu(factT);
				dtoGeneral.setTotalCantUsu(cantT);
				dtoGeneral.setTipoSeleccion(autorizado);
								
				gAutT=gAutT + autT;
				gFactT=gFactT + factT;
				gCantT= gCantT+cantT;
				dtoGeneral.setGranTotalAut(gAutT);
				dtoGeneral.setGranTotalFac(gFactT);
				dtoGeneral.setGranTotalCant(gCantT);
				
				collectionDTOGeneral.add(dtoGeneral);
			}
				}
				
				
			}
			
			return collectionDTOGeneral;
						
		}
		
		// Genera Reportes en formato PDF
		else if (tipoSalida.equals("1"))
		{	
		if (dtoResultado != null) 
		{ String name="";
		collectionDtoPlano=new ArrayList();
		double gAutT=0, gFactT=0, gCantT=0;
			for (UsuariosConsumidoresPresupuestoDto usuarios: dtoResultado)
		{   dtoGeneral = new UsuariosConsumidoresGeneralDto();	
		     double autT=0, factT=0, cantT=0;
			String nombrePacientes=usuarios.getPrimerNombrePersona();
			try {if (!usuarios.getSegundoNombrePersona().equals(null))
				nombrePacientes= nombrePacientes+" "+usuarios.getSegundoNombrePersona();}
			catch(Exception e){ 				
			}
			nombrePacientes= nombrePacientes+" "+usuarios.getPrimerApellidoPersona();
			try {if (!usuarios.getSegundoApellidoPersona().equals(null))
				nombrePacientes= nombrePacientes+" "+usuarios.getSegundoApellidoPersona();}
			catch(Exception e){ 				
			}
			
		    if (!name.equals(nombrePacientes))
		    {
		    	name=nombrePacientes;
		   	String rutaLogo = "../"+dtoEncabezado.getRutaLogo();
			dtoGeneral.setRazonSocial(dtoEncabezado.getRazonSocial());	
			dtoGeneral.setCentroAtencion("Centro de Atencion: " + dtoEncabezado.getCentroAtencion());
			dtoGeneral.setTelefono("Teléfono: "+dtoEncabezado.getTelefono());			
			dtoGeneral.setUsuario(dtoEncabezado.getUsuario());			
			dtoGeneral.setDireccion("Direccion: "+dtoEncabezado.getDireccion());			
			dtoGeneral.setActividadEconomica(dtoEncabezado.getActividadEconomica());			
			dtoGeneral.setNit("NIT: "+dtoEncabezado.getNit());
			dtoGeneral.setTipoImpresion(dtoEncabezado.getTipoImpresion());
				
			boolean existeLogo = existeLogo(rutaLogo);
			
			if (existeLogo) {
				if (dtoEncabezado.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
					dtoGeneral.setLogoDerecha(rutaLogo);
					dtoGeneral.setLogoIzquierda(null);
				} else if (dtoEncabezado.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
					dtoGeneral.setLogoIzquierda(rutaLogo);
					dtoGeneral.setLogoDerecha(null);
				}
			}else{
				dtoGeneral.setLogoDerecha(null);
				dtoGeneral.setLogoIzquierda(null);
			}
			
			dtoGeneral.setCriteriosBusqueda(criteriosBusqueda);
			dtoGeneral.setNombrePaciente(nombrePacientes);
			dtoGeneral.setNumeroIdentificacion(usuarios.getNumeroIdentificacion());
			dtoGeneral.setNombreTipoIdentificacion(usuarios.getNombreTipoIdentificacion()); 
			dtoGeneral.setListaValoresClaseInventario(new JRBeanCollectionDataSource(usuarios.getListaValoresClaseInventario()));
			dtoGeneral.setListaValoresGrupoServicio(new JRBeanCollectionDataSource(usuarios.getListaValoresGrupoServicio()));
			dtoGeneral.setCantidadAutorizada(usuarios.getCantidadAutorizada());
			dtoGeneral.setCantidadIngresos(usuarios.getCantidadIngreso());
		
			try{
			if (usuarios.getListaValoresClaseInventario()!= null)
			{
			for (ValorClaseInventariosPacienteDto val: usuarios.getListaValoresClaseInventario())
			{
				autT= autT + val.getValorAutorizado();
				factT=factT + val.getValorFacturado();
				cantT=cantT + val.getCantidadAutorizada();
				
			}
			}
			}catch (Exception e){}
			
			try{
			if (usuarios.getListaValoresGrupoServicio() != null)
			{
			for (ValorGruposServicioPacienteDto val: usuarios.getListaValoresGrupoServicio())
			{
				autT= autT + val.getValorAutorizado();
				factT=factT + val.getValorFacturado();
				cantT=cantT + val.getCantidadAutorizada();
				
			}
			}
			} catch (Exception e){}
			dtoGeneral.setTotalAutUsu(autT);
			dtoGeneral.setTotalFacUsu(factT);
			dtoGeneral.setTotalCantUsu(cantT);
			dtoGeneral.setTipoSeleccion(autorizado);
			
			gAutT=gAutT + autT;
			gFactT=gFactT + factT;
			gCantT= gCantT+cantT;
			dtoGeneral.setGranTotalAut(gAutT);
			dtoGeneral.setGranTotalFac(gFactT);
			dtoGeneral.setGranTotalCant(gCantT);
			collectionDTOGeneral.add(dtoGeneral);
		}
			}
			
			
		}
		
		return collectionDTOGeneral;
		}
		else {
			return null;
		}
	}

	@SuppressWarnings("unused")
	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        
   if (tipoSalida.equals("1")||tipoSalida.equals("3"))
   {
        try {
	      	 if (autorizado.equals("1"))
        	    {
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_GRUPO_SERVICIOS_AUTORIZADOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_GRUPO_SERVICIOS_AUTORIZADOS);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_GRUPO_SERVICIOS_AUTORIZADOS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_CLASE_INVENTARIOS_AUTORIZADAS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_CLASE_INVENTARIOS_AUTORIZADAS);
	           	
			}else if (myInFile != null) 
			{
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_CLASE_INVENTARIOS_AUTORIZADAS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }}
        	 if (autorizado.equals("2"))
        	 {
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_GRUPO_SERVICIOS_FACTURADOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_GRUPO_SERVICIOS_FACTURADOS);
	           	
			}else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_GRUPO_SERVICIOS_FACTURADOS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_CLASE_INVENTARIOS_FACTURADAS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_CLASE_INVENTARIOS_FACTURADAS);
	           	
			}else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_CLASE_INVENTARIOS_FACTURADAS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }}
	        

	        /**FIXME ------->AQUI VA CARGA DE LOS SUBREPORTES*/
	        
		}catch (Exception e) {
			e.printStackTrace();
		}
   } else {
	   try {
		   myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_USUARIOS_CONSUMIDORES_PRESUPUESTO_PLANO);
		   
			if(myInFile == null) {
				myInFile = new FileInputStream(RUTA_SUBREPORTE_USUARIOS_CONSUMIDORES_PRESUPUESTO_PLANO);
			} else {
				Log4JManager.info(myInFile.getClass());
				Object mySubreportObj = JRLoader.loadObject(myInFile);
		        parametrosReporte.put(NOMBRE_SUBREPORTE_USUARIOS_CONSUMIDORES_PRESUPUESTO_PLANO, mySubreportObj);
		        Log4JManager.info(myInFile.getClass()+""+mySubreportObj);
		    }
		 }catch (JRException e) {
			Log4JManager.error(e);
		} catch (FileNotFoundException e) {
			Log4JManager.error(e);
		} finally {
			try {
				if(myInFile != null) {
					myInFile.close();
					myInFile=null;
				}
			} catch (IOException e) {
				Log4JManager.error(e);
			}	
		}  	
   }
		
        return parametrosReporte;
	}

	
	@Override
	public String obtenerRutaPlantilla() {
		if (tipoSalida.equals("2"))
		{
//			RUTA_REPORTE_GENERAL= "com/servinte/axioma/generadorReporte/manejoPaciente/usuariosConsumidoresPresupuesto/usuariosConsumidoresPresupuestoPlano.jasper";
			RUTA_REPORTE_GENERAL= "com/servinte/axioma/generadorReporte/manejoPaciente/usuariosConsumidoresPresupuesto/reporteUsuariosConsumidoresPresupuestoPlano.jasper";
			
		}
		else
		{RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/manejoPaciente/usuariosConsumidoresPresupuesto/usuarioConsumidorPresupuesto.jasper";
		}
	return RUTA_REPORTE_GENERAL;
	}
}
