package com.servinte.axioma.generadorReporte.capitacion.GeneradorReporteTotalServiciosArticulosValorizadosPorConvenio;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.Utilidades;
import util.reportes.dinamico.DataSource;
import util.reportes.dinamico.complex.ReportData;

import com.princetonsa.dto.capitacion.DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.dto.capitacion.DtoMesesTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.dto.capitacion.DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio;
import com.servinte.axioma.generadorReporte.comun.IConstantesReporte;

/**
 * @author Cristhian Murillo
*/
public class GeneradorReporteTotalServArtPorConvenioDinamicoDatos implements ReportData 
{

	//Data Source general
	private DataSource dataSourceNiveles;
	
	/** * Datos del reporte */
	private DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio datosReporte = null;
	
	
	/**
	 * Constructor de la clase
	 * 
	 * @author Cristhian Murillo
	 */
	public GeneradorReporteTotalServArtPorConvenioDinamicoDatos(DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio datosReporte) 
	{
		this.datosReporte = datosReporte;
		obtenerRutaLogo();
	}
	
	
	/**
	 * Genera el datarource necesario con los datos organizados a mostrar en el reporte.
	 * 
	 * @author Cristhian Murillo
	 */
	public JRDataSource createDataSource() 
	{
		DataSource dataSource = new DataSource("nivelAtencion", "nombreMes", "tipo", "cantidad", "valor", "numeroMes", "agrupacion");
		ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> listaDatos = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		if(this.datosReporte.getListaNivelesAtencionServicios() != null &&
				!this.datosReporte.getListaNivelesAtencionServicios().isEmpty()){
			listaDatos.addAll(this.datosReporte.getListaNivelesAtencionServicios());
		}
		if(this.datosReporte.getListaNivelesAtencionArticulos() != null &&
				!this.datosReporte.getListaNivelesAtencionArticulos().isEmpty()){
			listaDatos.addAll(this.datosReporte.getListaNivelesAtencionArticulos());
		}
		
		// Si existen datos, se llena el DataSource general para evitar errores de páginas en blanco
		if(!Utilidades.isEmpty(listaDatos)){
			 dataSource.add("nivel", "mes", "", 
					new Integer(0), new BigDecimal(0), new Integer(0), 
					ConstantesIntegridadDominio.acronimoTipoTotalServicio);
		}
		
		return dataSource;
	}
	
	
	/**
	 * Obtiene la ruta del logo
	 */
	private void obtenerRutaLogo()
	{
		if(!UtilidadTexto.isEmpty(this.datosReporte.getRutaLogo())&& !UtilidadTexto.isEmpty(this.datosReporte.getUbicacionLogo()))
		{
			String ubicacionLogo = this.datosReporte.getUbicacionLogo();
			String rutaLogo = "../"+this.datosReporte.getRutaLogo();			
			boolean existeLogo = existeLogo(rutaLogo);
					
			if (existeLogo) 
			{
				if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) 
				{
					this.datosReporte.setLogoDerecha(rutaLogo);
					this.datosReporte.setLogoIzquierda(null);
				} 
				else if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) 
				{
					this.datosReporte.setLogoIzquierda(rutaLogo);
					this.datosReporte.setLogoDerecha(null);
				}
			}
			else{
				this.datosReporte.setLogoDerecha(null);
				this.datosReporte.setLogoIzquierda(null);
			}			
		}
	}
	
	
	/** 
	 * Este metodo se encarga de retornar true si la imagen del logo se encuentra almacenada 
	 * en la ruta indicada. 
	 *
	 * @param rutaLogo
	 * @return
	 *
	 */
	public boolean existeLogo (String rutaLogo)
	{
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        boolean existe = false;

        try {
	        myInFile = loader.getResourceAsStream(rutaLogo);
	        
	        if (myInFile != null) {
	           existe = true;
	            myInFile.close();
	        }
        }
        catch (Exception e) {
        	e.printStackTrace();
		}
        
        return existe;
	}
	
	

	/**
	 * Genera el datarource necesario con los datos organizados a mostrar en el reporte.
	 * 
	 * @author Cristhian Murillo
	*/
	public JRDataSource createDataSourceServicio(DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio datosReporte) 
	{
		this.dataSourceNiveles = new DataSource("nivelAtencion", "nombreMes", "tipo", "cantidad", "valor", "numeroMes");
		this.datosReporte = datosReporte;
		
		if(this.datosReporte.getIdFormatoServicios() == IConstantesReporte.formatoA){
			this.datosReporte.setNombreAgrupacion(IConstantesReporte.nivelAten);
			this.dataSourceNiveles = (DataSource) createDataSourceServicioA();
		}
		
		else if(this.datosReporte.getIdFormatoServicios() == IConstantesReporte.formatoB){
			this.datosReporte.setNombreAgrupacion(IConstantesReporte.grupoServicio);
			this.dataSourceNiveles = (DataSource) createDataSourceServicioB();
		}
		
		else if(this.datosReporte.getIdFormatoServicios() == IConstantesReporte.formatoC){
			this.datosReporte.setNombreAgrupacion(IConstantesReporte.servicio);
			this.dataSourceNiveles = (DataSource) createDataSourceServicioC();
		}
		
		return this.dataSourceNiveles;
	}

	
	
	
	
	/**
	 * Genera el datarource necesario con los datos organizados a mostrar en el reporte.
	 * 
	 * @author Cristhian Murillo
	*/
	public JRDataSource createDataSourceArticulo(DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio datosReporte) 
	{
		this.dataSourceNiveles = new DataSource("nivelAtencion", "nombreMes", "tipo", "cantidad", "valor", "numeroMes");
		this.datosReporte = datosReporte;
		
		if(this.datosReporte.getIdFormatoArticulos() == IConstantesReporte.formatoA){
			this.datosReporte.setNombreAgrupacion(IConstantesReporte.nivelAten);
			this.dataSourceNiveles = (DataSource) createDataSourceArticuloA();
		}
		
		else if(this.datosReporte.getIdFormatoArticulos() == IConstantesReporte.formatoB){
			this.datosReporte.setNombreAgrupacion(IConstantesReporte.claseInv);
			this.dataSourceNiveles = (DataSource) createDataSourceArticuloB();
		}
		
		else if(this.datosReporte.getIdFormatoArticulos() == IConstantesReporte.formatoC){
			this.datosReporte.setNombreAgrupacion(IConstantesReporte.articulo);
			this.dataSourceNiveles = (DataSource) createDataSourceArticuloC();
		}
		
		return this.dataSourceNiveles;
	}
	
	
	
	/**
	 * Genera el datarource necesario con los datos organizados a mostrar en el reporte.
	 * 
	 * @author Cristhian Murillo
	 */
	public JRDataSource createDataSourceArticuloA() 
	{
		if(!this.datosReporte.isFormatoSinEncabezado())
		{
			DataSource dataSourceNiveles = new DataSource("nivelAtencion", "nombreMes", "tipo", "cantidad", "valor", "numeroMes");
			
			for (DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio nivel : this.datosReporte.getListaNivelesAtencionArticulos()) 
			{
				for (DtoMesesTotalServiciosArticulosValorizadosPorConvenio mes : nivel.getListaMesesTotales()) 
				{
					dataSourceNiveles.add(nivel.getNivelAtencion(),  mes.getNombreMes(),
							IConstantesReporte.medInsumo, mes.getCantidadArticulos(),   mes.getValorArticulos(), mes.getNumeroMes());
				}
			}
			
			return dataSourceNiveles;
		}
		else
		{
			String[] keyData = new String[this.datosReporte.getCantidadMesesMostrar()*2+3];
			keyData[0]="convenio";
			keyData[1]="nivelAtencion";
			keyData[2]="tipo";
			int numCol=3;
			
			ArrayList<Integer> listaMeses = new ArrayList<Integer>();
			for(DtoMesesTotalServiciosArticulosValorizadosPorConvenio keyMes:this.datosReporte.getListaNivelesAtencionArticulos().get(0).getListaMesesTotales())
			{
				if(!listaMeses.contains(keyMes.getNumeroMes()))
				{
					keyData[numCol]=keyMes.getNombreMes()+"Cant";
					numCol +=1;
					keyData[numCol]=keyMes.getNombreMes()+"Vlr";
					numCol +=1;
					listaMeses.add(keyMes.getNumeroMes());
				}
			}
				
			this.dataSourceNiveles = new DataSource(keyData);
			DataSource dataSourceNiveles = new DataSource(keyData);
			
			for (DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio nivel : this.datosReporte.getListaNivelesAtencionArticulos()) 
			{
				Object[] valueData = new Object[this.datosReporte.getCantidadMesesMostrar()*2+3];
				valueData[0]=this.datosReporte.getNombreConvenio();
				valueData[1]=nivel.getNivelAtencion();
				numCol=3;
				
				for (DtoMesesTotalServiciosArticulosValorizadosPorConvenio mes : nivel.getListaMesesTotales()) 
				{
					if(mes.getCantidadArticulos() != null && mes.getValorArticulos() != null)
					{
						valueData[2]=IConstantesReporte.medInsumo;
						
						valueData[numCol]=mes.getCantidadArticulos();
						
						numCol +=1;
						
						valueData[numCol]=mes.getValorArticulos();
						
						numCol +=1;
					}
				}
				dataSourceNiveles.add(valueData);
			}
			
			return dataSourceNiveles;
		}
	}
	
	
	/**
	 * Genera el datarource necesario con los datos organizados a mostrar en el reporte.
	 * 
	 * @author Cristhian Murillo
	*/
	public JRDataSource createDataSourceServicioA() 
	{
		if(!this.datosReporte.isFormatoSinEncabezado())
		{
			DataSource dataSourceNiveles = new DataSource("nivelAtencion", "nombreMes", "tipo", "cantidad", "valor", "numeroMes");
			
			for (DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio nivel : this.datosReporte.getListaNivelesAtencionServicios()) 
			{
				for (DtoMesesTotalServiciosArticulosValorizadosPorConvenio mes : nivel.getListaMesesTotales()) 
				{
					dataSourceNiveles.add(nivel.getNivelAtencion(),  mes.getNombreMes(),
							IConstantesReporte.servicio, mes.getCantidadServicios(),  mes.getValorServicios(), mes.getNumeroMes());
				}
			}
			return dataSourceNiveles;
		}
		else
		{
			String[] keyData = new String[this.datosReporte.getCantidadMesesMostrar()*2+3];
			keyData[0]="convenio";
			keyData[1]="nivelAtencion";
			keyData[2]="tipo";
			int numCol=3;
			
			ArrayList<Integer> listaMeses = new ArrayList<Integer>();
			for(DtoMesesTotalServiciosArticulosValorizadosPorConvenio keyMes:this.datosReporte.getListaNivelesAtencionServicios().get(0).getListaMesesTotales())
			{
				if(!listaMeses.contains(keyMes.getNumeroMes()))
				{
					keyData[numCol]=keyMes.getNombreMes()+"Cant";
					
					numCol +=1;
					
					keyData[numCol]=keyMes.getNombreMes()+"Vlr";
					
					numCol +=1;
					
					listaMeses.add(keyMes.getNumeroMes());
				}
			}
			
			this.dataSourceNiveles = new DataSource(keyData);
			DataSource dataSourceNiveles = new DataSource(keyData);
			
			for (DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio nivel : this.datosReporte.getListaNivelesAtencionServicios()) 
			{
				Object[] valueData = new Object[this.datosReporte.getCantidadMesesMostrar()*2+3];
				valueData[0]=this.datosReporte.getNombreConvenio(); 
				valueData[1]=nivel.getNivelAtencion();
				numCol=3;
				
				for (DtoMesesTotalServiciosArticulosValorizadosPorConvenio mes : nivel.getListaMesesTotales()) 
				{
					if(mes.getCantidadServicios() != null && mes.getValorServicios() != null)
					{
						valueData[2]=IConstantesReporte.servicio;
						valueData[numCol]=mes.getCantidadServicios();
						
						numCol +=1;
						
						valueData[numCol]=mes.getValorServicios();
						
						numCol +=1;
					}
				}
				dataSourceNiveles.add(valueData);
			}
			
			return dataSourceNiveles;
		}
		
		
	}

	
	/**
	 * Genera el datarource necesario con los datos organizados a mostrar en el reporte.
	 * 
	 * @author Cristhian Murillo
	 */
	public JRDataSource createDataSourceArticuloB() 
	{
		if(!this.datosReporte.isFormatoSinEncabezado())
		{
			DataSource dataSource = new DataSource("nivelAtencion", "nombreMes", "tipo", "cantidad", "valor", "numeroMes", "agrupacion");
			
			for (DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio nivel : this.datosReporte.getListaNivelesAtencionArticulos()) 
			{
				for (DtoMesesTotalServiciosArticulosValorizadosPorConvenio mes : nivel.getListaMesesTotales()) 
				{
					//Clase Inventario
					dataSource.add(nivel.getNivelAtencion(),  mes.getNombreMes(), mes.getClaseInventario(), 
							mes.getCantidadArticulos(), mes.getValorArticulos(), mes.getNumeroMes(), 
							ConstantesIntegridadDominio.acronimoTipoTotalArticulo);
				}
			}
			
			return dataSource;
		}
		else
		{
			String[] keyData = new String[this.datosReporte.getCantidadMesesMostrar()*2+3];
			keyData[0]="tipo";
			keyData[1]="nivelAtencion";
			keyData[2]="descripcion";
			int numCol=3;
			
			ArrayList<Integer> listaMeses = new ArrayList<Integer>();
			for(DtoMesesTotalServiciosArticulosValorizadosPorConvenio keyMes:this.datosReporte.getListaNivelesAtencionArticulos().get(0).getListaMesesTotales())
			{
				if(!listaMeses.contains(keyMes.getNumeroMes()))
				{
					keyData[numCol]=keyMes.getNombreMes()+"Cant";
					
					numCol +=1;
					
					keyData[numCol]=keyMes.getNombreMes()+"Vlr";
					
					numCol +=1;
					
					listaMeses.add(keyMes.getNumeroMes());
				}
			}
			
			this.dataSourceNiveles = new DataSource(keyData);
			DataSource dataSourceNiveles = new DataSource(keyData);
			
			HashMap<String, Object[]> mapaKeyData = new HashMap<String, Object[]>();
			ArrayList<String> listaKeys = new ArrayList<String>();
			HashMap<String, Integer> mapaKeyDataPos = new HashMap<String, Integer>();
			
			for (DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio nivel : this.datosReporte.getListaNivelesAtencionArticulos()) 
			{
				String key 	= "";
				listaKeys = new ArrayList<String>();
				mapaKeyData = new HashMap<String, Object[]>();
				mapaKeyDataPos = new HashMap<String, Integer>();
				
				for (DtoMesesTotalServiciosArticulosValorizadosPorConvenio mes : nivel.getListaMesesTotales()) 
				{
					if(mes.getCantidadArticulos() != null && mes.getValorArticulos() != null)
					{
						key = mes.getClaseInventario();
						Object[] valueData = new Object[this.datosReporte.getCantidadMesesMostrar()*2+3];
						valueData[0]=this.datosReporte.getNombreAgrupacion(); 
						valueData[1]=nivel.getNivelAtencion();
						
						if(!mapaKeyData.containsKey(key))
						{
							numCol=3;
							
							valueData[2]= mes.getClaseInventario();
							
							valueData[numCol]=mes.getCantidadArticulos();
							
							numCol +=1;
							
							valueData[numCol]=mes.getValorArticulos();
							
							numCol +=1;
							
							mapaKeyData.put(key, valueData);
							listaKeys.add(key);
							mapaKeyDataPos.put(key, numCol);
						}
						else
						{
							valueData = mapaKeyData.get(key);
							numCol = mapaKeyDataPos.get(key);
							
							valueData[numCol]=mes.getCantidadArticulos();
							
							numCol +=1;
							
							valueData[numCol]=mes.getValorArticulos();
							
							numCol +=1;
							
							mapaKeyData.put(key, valueData);
							mapaKeyDataPos.put(key, numCol);
						}
					}
				}
				
				for (String stringKey : listaKeys) {
					dataSourceNiveles.add(mapaKeyData.get(stringKey));
				}
			}
			
			return dataSourceNiveles;
		}
		
	}
	
	
	/**
	 * Genera el datarource necesario con los datos organizados a mostrar en el reporte.
	 * 
	 * @author Cristhian Murillo
	 */
	public JRDataSource createDataSourceServicioB() 
	{
		if(!this.datosReporte.isFormatoSinEncabezado())
		{
			DataSource dataSource = new DataSource("nivelAtencion", "nombreMes", "tipo", "cantidad", "valor", "numeroMes", "agrupacion");
			
			for (DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio nivel : this.datosReporte.getListaNivelesAtencionServicios()) 
			{
				for (DtoMesesTotalServiciosArticulosValorizadosPorConvenio mes : nivel.getListaMesesTotales()) 
				{
					//Grupo Servicios
					dataSource.add(nivel.getNivelAtencion(),  mes.getNombreMes(), mes.getGrupoServicio(), 
							mes.getCantidadServicios(), mes.getValorServicios(), mes.getNumeroMes(), 
							ConstantesIntegridadDominio.acronimoTipoTotalServicio);
				}
			}
			return dataSource;
		}
		else
		{
			String[] keyData = new String[this.datosReporte.getCantidadMesesMostrar()*2+3];
			keyData[0]="tipo";
			keyData[1]="nivelAtencion";
			keyData[2]="descripcion";
			int numCol=3;
			
			ArrayList<Integer> listaMeses = new ArrayList<Integer>();
			for(DtoMesesTotalServiciosArticulosValorizadosPorConvenio keyMes:this.datosReporte.getListaNivelesAtencionServicios().get(0).getListaMesesTotales())
			{
				if(!listaMeses.contains(keyMes.getNumeroMes()))
				{
					keyData[numCol]=keyMes.getNombreMes()+"Cant";
					
					numCol +=1;
					
					keyData[numCol]=keyMes.getNombreMes()+"Vlr";
					
					numCol +=1;
					
					listaMeses.add(keyMes.getNumeroMes());
				}
			}
			
			this.dataSourceNiveles = new DataSource(keyData);
			DataSource dataSourceNiveles = new DataSource(keyData);
			
			HashMap<String, Object[]> mapaKeyData = new HashMap<String, Object[]>();
			HashMap<String, Integer> mapaKeyDataPos = new HashMap<String, Integer>();
			ArrayList<String> listaKeys = new ArrayList<String>();
			
			for (DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio nivel : this.datosReporte.getListaNivelesAtencionServicios()) 
			{
				String key 	= "";
				listaKeys = new ArrayList<String>();
				mapaKeyData = new HashMap<String, Object[]>();
				mapaKeyDataPos = new HashMap<String, Integer>();
				
				for (DtoMesesTotalServiciosArticulosValorizadosPorConvenio mes : nivel.getListaMesesTotales()) 
				{
					if(mes.getCantidadServicios() != null && mes.getValorServicios() != null)
					{
						key = mes.getGrupoServicio();
						Object[] valueData = new Object[this.datosReporte.getCantidadMesesMostrar()*2+3];
						valueData[0]=this.datosReporte.getNombreAgrupacion(); 
						valueData[1]=nivel.getNivelAtencion();
						
						if(!mapaKeyData.containsKey(key))
						{
							numCol=3;
							
							valueData[2]=mes.getGrupoServicio();
							
							valueData[numCol]=mes.getCantidadServicios();
							
							numCol +=1;
							
							valueData[numCol]=mes.getValorServicios();
							
							numCol +=1;
							
							mapaKeyData.put(key, valueData);
							listaKeys.add(key);
							mapaKeyDataPos.put(key, numCol);
						}
						else
						{
							valueData = mapaKeyData.get(key);
							numCol = mapaKeyDataPos.get(key);
							
							valueData[numCol]=mes.getCantidadServicios();
							
							numCol +=1;
							
							valueData[numCol]=mes.getValorServicios();
							
							numCol +=1;
							
							mapaKeyData.put(key, valueData);
							mapaKeyDataPos.put(key, numCol);
						}
					}
				}
				
				for (String stringKey : listaKeys) {
					dataSourceNiveles.add(mapaKeyData.get(stringKey));
				}
			}
			
			return dataSourceNiveles;
		}
	}
	
	
	/**
	 * Genera el datarource necesario con los datos organizados a mostrar en el reporte.
	 * 
	 * @author Cristhian Murillo
	 */
	public JRDataSource createDataSourceArticuloC() 
	{
		if(!this.datosReporte.isFormatoSinEncabezado())
		{
			DataSource dataSource = new DataSource("nivelAtencion", "nombreMes", "tipo", "cantidad", "valor", "numeroMes", "agrupacion");
			
			for (DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio nivel : this.datosReporte.getListaNivelesAtencionArticulos()) 
			{
				for (DtoMesesTotalServiciosArticulosValorizadosPorConvenio mes : nivel.getListaMesesTotales()) 
				{
					//Articulo
					dataSource.add(nivel.getNivelAtencion(),  mes.getNombreMes(), mes.getArticulo(), 
							mes.getCantidadArticulos(), mes.getValorArticulos(), mes.getNumeroMes(), 
							ConstantesIntegridadDominio.acronimoTipoTotalArticulo);
				}
			}
			
			return dataSource;
		}
		else
		{

			String[] keyData = new String[this.datosReporte.getCantidadMesesMostrar()*2+3];
			keyData[0]="tipo";
			keyData[1]="nivelAtencion";
			keyData[2]="descripcion";
			int numCol=3;
			
			ArrayList<Integer> listaMeses = new ArrayList<Integer>();
			for(DtoMesesTotalServiciosArticulosValorizadosPorConvenio keyMes:this.datosReporte.getListaNivelesAtencionArticulos().get(0).getListaMesesTotales())
			{
				if(!listaMeses.contains(keyMes.getNumeroMes()))
				{
					keyData[numCol]=keyMes.getNombreMes()+"Cant";
					
					numCol +=1;
					
					keyData[numCol]=keyMes.getNombreMes()+"Vlr";
					
					numCol +=1;
					
					listaMeses.add(keyMes.getNumeroMes());
				}
			}
			
			this.dataSourceNiveles = new DataSource(keyData);
			DataSource dataSourceNiveles = new DataSource(keyData);
			
			HashMap<String, Object[]> mapaKeyData = new HashMap<String, Object[]>();
			HashMap<String, Integer> mapaKeyDataPos = new HashMap<String, Integer>();
			ArrayList<String> listaKeys = new ArrayList<String>();
			
			for (DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio nivel : this.datosReporte.getListaNivelesAtencionArticulos()) 
			{
				String key 	= "";
				listaKeys = new ArrayList<String>();
				mapaKeyData = new HashMap<String, Object[]>();
				mapaKeyDataPos = new HashMap<String, Integer>();
				
				for (DtoMesesTotalServiciosArticulosValorizadosPorConvenio mes : nivel.getListaMesesTotales()) 
				{
					if(mes.getCantidadArticulos() != null && mes.getValorArticulos() != null)
					{
						key = mes.getArticulo();
						Object[] valueData = new Object[this.datosReporte.getCantidadMesesMostrar()*2+3];
						valueData[0]=this.datosReporte.getNombreAgrupacion(); 
						valueData[1]=nivel.getNivelAtencion();
						
						if(!mapaKeyData.containsKey(key))
						{
							numCol=3;
							
							valueData[2]= mes.getArticulo();
							
							valueData[numCol]=mes.getCantidadArticulos();
							
							numCol +=1;
							
							valueData[numCol]=mes.getValorArticulos();
							
							numCol +=1;
							
							mapaKeyData.put(key, valueData);
							listaKeys.add(key);
							mapaKeyDataPos.put(key, numCol);
						}
						else
						{
							valueData = mapaKeyData.get(key);
							numCol = mapaKeyDataPos.get(key);
							
							valueData[numCol]=mes.getCantidadArticulos();
							
							numCol +=1;
							
							valueData[numCol]=mes.getValorArticulos();
							
							numCol +=1;
							
							mapaKeyData.put(key, valueData);
							mapaKeyDataPos.put(key, numCol);
						}
					}
				}
				
				for (String stringKey : listaKeys) {
					dataSourceNiveles.add(mapaKeyData.get(stringKey));
				}
			}
			
			return dataSourceNiveles;
		
		}
	}
	
	
	/**
	 * Genera el datarource necesario con los datos organizados a mostrar en el reporte.
	 * 
	 * @author Cristhian Murillo
	 */
	public JRDataSource createDataSourceServicioC() 
	{
		if(!this.datosReporte.isFormatoSinEncabezado())
		{
			DataSource dataSource = new DataSource("nivelAtencion", "nombreMes", "tipo", "cantidad", "valor", "numeroMes", "agrupacion");
			
			for (DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio nivel : this.datosReporte.getListaNivelesAtencionServicios()) 
			{
				for (DtoMesesTotalServiciosArticulosValorizadosPorConvenio mes : nivel.getListaMesesTotales()) 
				{
					//Servicio
					dataSource.add(nivel.getNivelAtencion(),  mes.getNombreMes(), mes.getServicio(), 
							mes.getCantidadServicios(), mes.getValorServicios(), mes.getNumeroMes(), 
							ConstantesIntegridadDominio.acronimoTipoTotalServicio);
				}
			}
			
			return dataSource;
		}
		else
		{
			String[] keyData = new String[this.datosReporte.getCantidadMesesMostrar()*2+3];
			keyData[0]="tipo";
			keyData[1]="nivelAtencion";
			keyData[2]="descripcion";
			int numCol=3;
			
			ArrayList<Integer> listaMeses = new ArrayList<Integer>();
			for(DtoMesesTotalServiciosArticulosValorizadosPorConvenio keyMes:this.datosReporte.getListaNivelesAtencionServicios().get(0).getListaMesesTotales())
			{
				if(!listaMeses.contains(keyMes.getNumeroMes()))
				{
					keyData[numCol]=keyMes.getNombreMes()+"Cant";
					
					numCol +=1;
					
					keyData[numCol]=keyMes.getNombreMes()+"Vlr";
					
					numCol +=1;
					
					listaMeses.add(keyMes.getNumeroMes());
				}
			}
			
			this.dataSourceNiveles = new DataSource(keyData);
			DataSource dataSourceNiveles = new DataSource(keyData);
			
			HashMap<String, Object[]> mapaKeyData = new HashMap<String, Object[]>();
			ArrayList<String> listaKeys = new ArrayList<String>();
			HashMap<String, Integer> mapaKeyDataPos = new HashMap<String, Integer>();
			
			for (DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio nivel : this.datosReporte.getListaNivelesAtencionServicios()) 
			{
				String key 	= "";
				listaKeys = new ArrayList<String>();
				mapaKeyData = new HashMap<String, Object[]>();
				mapaKeyDataPos = new HashMap<String, Integer>();
				
				for (DtoMesesTotalServiciosArticulosValorizadosPorConvenio mes : nivel.getListaMesesTotales()) 
				{
					if(mes.getCantidadServicios() != null && mes.getValorServicios() != null)
					{
						key = mes.getServicio();
						Object[] valueData = new Object[this.datosReporte.getCantidadMesesMostrar()*2+3];
						valueData[0]=this.datosReporte.getNombreAgrupacion(); 
						valueData[1]=nivel.getNivelAtencion();
						
						if(!mapaKeyData.containsKey(key))
						{
							numCol=3;
							
							valueData[2]=mes.getServicio();
							
							valueData[numCol]=mes.getCantidadServicios();
							
							numCol +=1;
							
							valueData[numCol]=mes.getValorServicios();
							
							numCol +=1;
							
							mapaKeyData.put(key, valueData);
							listaKeys.add(key);
							mapaKeyDataPos.put(key, numCol);
						}
						else
						{
							valueData = mapaKeyData.get(key);
							numCol = mapaKeyDataPos.get(key);
							
							valueData[numCol]=mes.getCantidadServicios();
							
							numCol +=1;
							
							valueData[numCol]=mes.getValorServicios();
							
							numCol +=1;
							
							mapaKeyData.put(key, valueData);
							mapaKeyDataPos.put(key, numCol);
						}
					}
				}
				
				for (String stringKey : listaKeys) {
					dataSourceNiveles.add(mapaKeyData.get(stringKey));
				}
			}
			
			return dataSourceNiveles;
		}
		
	}
	
	
	
	
	/**
	 * Genera el datarource necesario con los datos organizados a mostrar en el reporte.
	 * 
	 * @author Cristhian Murillo
	 */
	public JRDataSource createDataSourceServicioArticuloFormatoA(DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio datosReporte) 
	{
		this.datosReporte = datosReporte;
		this.datosReporte.setNombreAgrupacion(IConstantesReporte.nivelAten);
		
		ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> listaArticulosServicios = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		listaArticulosServicios.addAll(this.datosReporte.getListaNivelesAtencionArticulos());
		listaArticulosServicios.addAll(this.datosReporte.getListaNivelesAtencionServicios());
		
		if(!this.datosReporte.isFormatoSinEncabezado())
		{
			this.dataSourceNiveles = new DataSource("nivelAtencion", "nombreMes", "tipo", "cantidad", "valor", "numeroMes");
			DataSource dataSourceNiveles = new DataSource("nivelAtencion", "nombreMes", "tipo", "cantidad", "valor", "numeroMes");
			
			for (DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio nivel : listaArticulosServicios) 
			{
				for (DtoMesesTotalServiciosArticulosValorizadosPorConvenio mes : nivel.getListaMesesTotales()) 
				{
					dataSourceNiveles.add(nivel.getNivelAtencion(),  mes.getNombreMes(),
							IConstantesReporte.servicio, mes.getCantidadServicios(),  mes.getValorServicios(), mes.getNumeroMes());
					
					dataSourceNiveles.add(nivel.getNivelAtencion(),  mes.getNombreMes(),
							IConstantesReporte.medInsumo, mes.getCantidadArticulos(),   mes.getValorArticulos(), mes.getNumeroMes());
				}
			}
			
			return dataSourceNiveles;
		}
		else
		{
			String[] keyData = new String[this.datosReporte.getCantidadMesesMostrar()*2+3];
			keyData[0]="convenio";
			keyData[1]="nivelAtencion";
			keyData[2]="tipo";
			int numCol=3;
			
			ArrayList<Integer> listaMeses = new ArrayList<Integer>();
			for(DtoMesesTotalServiciosArticulosValorizadosPorConvenio keyMes:listaArticulosServicios.get(0).getListaMesesTotales())
			{
				if(!listaMeses.contains(keyMes.getNumeroMes()))
				{
					keyData[numCol]=keyMes.getNombreMes()+"Cant";
					numCol +=1;
					keyData[numCol]=keyMes.getNombreMes()+"Vlr";
					numCol +=1;
					listaMeses.add(keyMes.getNumeroMes());
				}
			}
				
			this.dataSourceNiveles = new DataSource(keyData);
			DataSource dataSourceNiveles = new DataSource(keyData);
			
			for (DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio nivel : listaArticulosServicios) 
			{
				Object[] valueData = new Object[this.datosReporte.getCantidadMesesMostrar()*2+3];
				valueData[0]=this.datosReporte.getNombreConvenio();
				valueData[1]=nivel.getNivelAtencion();
				numCol=3;
				for (DtoMesesTotalServiciosArticulosValorizadosPorConvenio mes : nivel.getListaMesesTotales()) 
				{
					if(mes.getCantidadServicios() != null && mes.getValorServicios() != null)
					{
						valueData[2]=IConstantesReporte.servicio;
						valueData[numCol]=mes.getCantidadServicios();
						numCol +=1;
						valueData[numCol]=mes.getValorServicios();
						numCol +=1;
					}
					if(mes.getCantidadArticulos() != null && mes.getValorArticulos() != null)
					{
						valueData[2]=IConstantesReporte.medInsumo;
						valueData[numCol]=mes.getCantidadArticulos();
						numCol +=1;
						valueData[numCol]=mes.getValorArticulos();
						numCol +=1;
					}
				}
				dataSourceNiveles.add(valueData);
			}
			
			return dataSourceNiveles;
		}
		
	}
	
	
	
	
}
