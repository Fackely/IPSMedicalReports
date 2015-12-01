package com.princetonsa.mundo.cartera;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.TxtFile;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import com.princetonsa.actionform.cartera.RecaudoCarteraEventoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.cartera.RecaudoCarteraEventoDao;
import com.princetonsa.dao.sqlbase.cartera.SqlBaseRecaudoCarteraEventoDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;





public class RecaudoCarteraEvento 
{

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
		public static Logger logger = Logger.getLogger(RecaudoCarteraEvento.class);
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
		

		/**
		 *instancia el dao 
		 */
		public static RecaudoCarteraEventoDao recaudoCarteraEventoDao()
		{
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRecaudoCarteraEventoDao();
		}
		
		
		
		
		
		/*------------------------------------------------------------------------------------------------------
		 * INDICES PARA EL MANEJO DE LOS KEYS EN LOS MAPAS
		 * ----------------------------------------------------------------------------------------------------*/
		/**
		 * indices para el manejo de los criterios de busqueda
		 */
		public static String [] indicesCriterios = {"fechaIni0","fechaFin1","tipoReporte2","tipoConvenio3","convenio4","tipoSalida5","institucion6","reporte7","nombreReporte8","usuario9" };
		
		/**
		 * indices para el manejo de el resultado
		 */
		
		public static String [] indicesResultado = {"tipoConvenio0_","tipoDoc1_","documento2_","fechaRecaudo3_","valorRecaudo4_","convenio5_","factura6_","fechaFactura7_"};
		/*------------------------------------------------------------------------------------------------------
		 * FIN INDICES PARA EL MANEJO DE LOS KEYS EN LOS MAPAS
		 * ----------------------------------------------------------------------------------------------------*/
		
		/**
		 * Metodo encargado de incializar los convenios
		 */
		public static void inicializar (Connection connection, RecaudoCarteraEventoForm forma, UsuarioBasico usuario)
		{
			
			forma.setTiposConvenio(Utilidades.obtenerTiposConvenio(connection, usuario.getCodigoInstitucion()));
			//se encarga de cargar el select de los convenios
			filtrarConvenio(connection, forma, usuario);
			
			logger.info("\n los convenios --> "+forma.getConvenios().size());
			
		}
		
		/**
		 * Metodo encargado de consultar los convenios, cargando los dependiendo el tipo de convenio
		 * @param connection
		 * @param forma
		 * @param usuario
		 */
		public static void filtrarConvenio (Connection connection,RecaudoCarteraEventoForm forma,UsuarioBasico usuario)
		{
			
			if ( forma.getCriterios().containsKey(indicesCriterios[3]) && !(forma.getCriterios(indicesCriterios[3])+"").equals("") && !(forma.getCriterios(indicesCriterios[3])+"").equals("null"))
				forma.setConvenios(Utilidades.obtenerConvenios(connection, "",  ConstantesBD.codigoTipoContratoEvento+"", false, "", true, forma.getCriterios(indicesCriterios[3])+""));
			else
				forma.setConvenios(Utilidades.obtenerConvenios(connection , "", ConstantesBD.codigoTipoContratoEvento+"", false, "", true));
		}
		
		
		/**
		 * Metodo encargado de consultar el reporte 
		 * Resumido por tipo de convenio.
		 * @param criterios
		 * -----------------------------------
		 * KEY'S DEL MAPA CRITERIOS
		 * -----------------------------------
		 * -- fechaIni0 --> Requerido
		 * -- fechaFin1 --> Requerido
		 * -- tipoReporte2 --> Requerido
		 * -- tipoConvenio3 --> Opcional
		 * -- convenio4 --> Opcional
		 * -- tipoSalida5 --> Requerido
		 * -- institucion6 --> Requerido
		 * ----------------------------------
		 * KEY'S DEL MAPA RESULTADO
		 * ----------------------------------
		 * tipoConvenio0_, tipoDoc1_,documento2_,
		 * fechaRecaudo3_,valorRecaudo4_
		 */
		public static HashMap consultaReporteResumidoXTipoConvenio (Connection connection, HashMap criterios)
		{
			return recaudoCarteraEventoDao().consultaReporteResumidoXTipoConvenio(connection, criterios);
		}
		
		
		/**
		 * Metodo encargado de consultar el reporte 
		 * Resumido por convenio.
		 * @param criterios
		 * -----------------------------------
		 * KEY'S DEL MAPA CRITERIOS
		 * -----------------------------------
		 * -- fechaIni0 --> Requerido
		 * -- fechaFin1 --> Requerido
		 * -- tipoReporte2 --> Requerido
		 * -- tipoConvenio3 --> Opcional
		 * -- convenio4 --> Opcional
		 * -- tipoSalida5 --> Requerido
		 * -- institucion6 --> Requerido
		 * ----------------------------------
		 * KEY'S DEL MAPA RESULTADO
		 * ----------------------------------
		 * tipoConvenio0_, tipoDoc1_,documento2_,
		 * fechaRecaudo3_,valorRecaudo4_,convenio5_
		 */
		public static HashMap consultarReporteResumidoXConvenio (Connection connection, HashMap criterios)
		{
			return recaudoCarteraEventoDao().consultarReporteResumidoXConvenio(connection, criterios);
		}
		
		
		/**
		 * Metodo encargado de insertar un log cuango
		 * se genera un reporte
		 * -----------------------------------------
		 * KEY'S DEL MAPA CRITERIOS
		 * ------------------------------------------
		 * -- reporte7 --> Requerido
		 * -- tipoReporte2 --> Requerido
		 * -- tipoSalida5 --> Requerido
		 * -- fechaIni0 --> Requerido
		 * -- fechaFin1 --> Requerido
		 * -- tipoConvenio3 --> Requerido
		 * -- convenio4 --> Requerido
		 * -- nombreReporte8 --> Requerido
		 * -- institucion6 --> Requerido
		 * -- usuario9 --> Requerido
		 */
		public static boolean insertarLog (Connection connection,HashMap criterios)
		{
			return recaudoCarteraEventoDao().insertarLog(connection, criterios);
		}
		
		
		/**
		 * Metodo encargado de de consultar los datos del reporte
		 * detallado por factura
		 * @param criterios
		 * @param connection
		 * -----------------------------------
		 * KEY'S DEL MAPA CRITERIOS
		 * -----------------------------------
		 * -- fechaIni0 --> Requerido
		 * -- fechaFin1 --> Requerido
		 * -- tipoReporte2 --> Requerido
		 * -- tipoConvenio3 --> Opcional
		 * -- convenio4 --> Opcional
		 * -- tipoSalida5 --> Requerido
		 * -- institucion6 --> Requerido
		 * ----------------------------------
		 * KEY'S DEL MAPA RESULTADO
		 * ----------------------------------
		 * tipoConvenio0_, tipoDoc1_,documento2_,
		 * fechaRecaudo3_,valorRecaudo4_,convenio5_
		 * factura6_,fechaFactura7_
		 * @return
		 */
		public static  HashMap consultarReporteDetalladoXFactura (Connection connection, HashMap criterios)
		{
			return SqlBaseRecaudoCarteraEventoDao.consultarReporteDetalladoXFactura(connection, criterios);
		}
		
		public static void buscarReporte (Connection connection, RecaudoCarteraEventoForm forma,UsuarioBasico usuario,HttpServletRequest request)
		{
			String tipoReporte=forma.getCriterios(indicesCriterios[2])+"";
			String salidaReporte=forma.getCriterios(indicesCriterios[5])+"";
			boolean OperacionTrue=false,existeTxt=false;
			int ban=ConstantesBD.codigoNuncaValido;
			HashMap criterios = forma.getCriterios();
			criterios.put(indicesCriterios[6], usuario.getCodigoInstitucion());		
			HashMap resultado= new HashMap ();
			//reporte resumido por tipo convenio
			if (tipoReporte.equals(ConstantesIntegridadDominio.acronimoReporteResumidoXTipoConvenio))
			{
				
				
				if (salidaReporte.equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
				{
					//se llama el reporte en birt
					generarReporte(connection, forma,usuario, request);
					//se inserta el log
					insertarLog(connection, organzarDatosLog(criterios, usuario, ""));
					
				}else
					if (salidaReporte.equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
					{					
						//arma el nombre del reporte
						String nombreReport=TxtFile.armarNombreArchivoConPeriodo("", forma.getCriterios(indicesCriterios[2])+"", UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[0])+"")+"--"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[1])+""));
						//se genera el documento con la informacion
						resultado=consultaReporteResumidoXTipoConvenio(connection, criterios);
						if (Utilidades.convertirAEntero(resultado.get("numRegistros")+"")>0)
							OperacionTrue=TxtFile.generarTxt(cargarMapa(resultado, criterios), nombreReport, ValoresPorDefecto.getReportPath()+"cartera/", ".txt");
						
							if (OperacionTrue)
						{
							//se genera el archivo en formato Zip
							ban=BackUpBaseDatos.EjecutarComandoSO("zip -j "+ValoresPorDefecto.getReportPath()+"cartera/"+nombreReport+".zip"+" "+ValoresPorDefecto.getReportPath()+"cartera/"+nombreReport+".txt");
							//se ingresa la direccion donde se almaceno el archivo
							forma.setRuta(ValoresPorDefecto.getReportPath()+"cartera/"+nombreReport+".txt");
							//se ingresa la ruta para poder descargar el archivo
							forma.setUrlArchivo(ValoresPorDefecto.getReportUrl()+"cartera/"+nombreReport+".zip");
							

							//se valida si existe el txt
							existeTxt=UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getReportPath()+"cartera/", nombreReport+".txt");
							//se valida si existe el zip
							forma.setExisteArchivo(UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getReportPath()+"cartera/", nombreReport+".zip"));
													
							if (existeTxt )
							{
								forma.setOperacionTrue(true);
								//se inserta el log
								insertarLog(connection, organzarDatosLog(criterios, usuario, forma.getRuta()));
							}
						}
					}
			}
			else//reporte resumido por convenio
				if (tipoReporte.equals(ConstantesIntegridadDominio.acronimoReporteResumidoXConvenio))
				{
					logger.info("\n reporte resumido por convenio");	
					if (salidaReporte.equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
					{
						//se llama el reporte en birt
						generarReporte(connection, forma,usuario, request);
						//se inserta el log
						insertarLog(connection, organzarDatosLog(criterios, usuario, ""));
						
					}else
						if (salidaReporte.equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
						{
							//arma el nombre del reporte
							String nombreReport=TxtFile.armarNombreArchivoConPeriodo("", forma.getCriterios(indicesCriterios[2])+"", UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[0])+"")+"--"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[1])+""));
							//se genera el documento con la informacion
							resultado=consultarReporteResumidoXConvenio(connection, criterios);
							if (Utilidades.convertirAEntero(resultado.get("numRegistros")+"")>0)
								OperacionTrue=TxtFile.generarTxt(cargarMapaXConvenio(resultado, criterios),nombreReport, ValoresPorDefecto.getReportPath()+"cartera/", ".txt");
							
							if (OperacionTrue)
							{
								//se genera el archivo en formato Zip
								ban=BackUpBaseDatos.EjecutarComandoSO("zip -j "+ValoresPorDefecto.getReportPath()+"cartera/"+nombreReport+".zip"+" "+ValoresPorDefecto.getReportPath()+"cartera/"+nombreReport+".txt");
								//se ingresa la direccion donde se almaceno el archivo
								forma.setRuta(ValoresPorDefecto.getReportPath()+"cartera/"+nombreReport+".txt");
								//se ingresa la ruta para poder descargar el archivo
								forma.setUrlArchivo(ValoresPorDefecto.getReportUrl()+"cartera/"+nombreReport+".zip");
								
								//se valida si existe el txt
								existeTxt=UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getReportPath()+"cartera/", nombreReport+".txt");
								//se valida si existe el zip
								forma.setExisteArchivo(UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getReportPath()+"cartera/", nombreReport+".zip"));
								
								if (existeTxt)
								{
									forma.setOperacionTrue(true);
									//se inserta el log
									insertarLog(connection, organzarDatosLog(criterios, usuario, forma.getRuta()));
								}
								
								
							}	
						}
					
				}
				else//Detallado por Factura
					if (tipoReporte.equals(ConstantesIntegridadDominio.acronimoReporteDetalladoXFactura))
					{
						logger.info("\n Detallado por factura");	
					
						if (salidaReporte.equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
						{
							//se llama el reporte en birt
							generarReporte(connection, forma,usuario, request);
							//se inserta el log
							insertarLog(connection, organzarDatosLog(criterios, usuario, ""));
							
							
						}else
							if (salidaReporte.equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
							{
								//arma el nombre del reporte
								String nombreReport=TxtFile.armarNombreArchivoConPeriodo("", forma.getCriterios(indicesCriterios[2])+"", UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[0])+"")+"--"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[1])+""));
								//se genera el documento con la informacion
								resultado=consultarReporteDetalladoXFactura(connection, criterios);
								if (Utilidades.convertirAEntero(resultado.get("numRegistros")+"")>0)
									OperacionTrue=TxtFile.generarTxt(cargarMapaXFactura(resultado, criterios),nombreReport, ValoresPorDefecto.getReportPath()+"cartera/", ".txt");
							
								if (OperacionTrue)
								{
									//se genera el archivo en formato Zip
									ban=BackUpBaseDatos.EjecutarComandoSO("zip -j "+ValoresPorDefecto.getReportPath()+"cartera/"+nombreReport+".zip"+" "+ValoresPorDefecto.getReportPath()+"cartera/"+nombreReport+".txt");
									//se ingresa la direccion donde se almaceno el archivo
									forma.setRuta(ValoresPorDefecto.getReportPath()+"cartera/"+nombreReport+".txt");
									//se ingresa la ruta para poder descargar el archivo
									forma.setUrlArchivo(ValoresPorDefecto.getReportUrl()+"cartera/"+nombreReport+".zip");
									
									//se valida si existe el txt
									existeTxt=UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getReportPath()+"cartera/", nombreReport+".txt");
									//se valida si existe el zip
									forma.setExisteArchivo(UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getReportPath()+"cartera/", nombreReport+".zip"));
									
									if (existeTxt )
									{
										forma.setOperacionTrue(true);
										//se inserta el log
										insertarLog(connection, organzarDatosLog(criterios, usuario, forma.getRuta()));
									}
									
								}	
								
							}
						
					}
					
					
			
		}
		
		
		/**
		 * Netodo encargado de hacer el llamado al reporte en birt
		 * @param con
		 * @param forma
		 * @param usuarioActual
		 * @param request
		 */
		public static void generarReporte(Connection con, RecaudoCarteraEventoForm forma,  UsuarioBasico usuarioActual,HttpServletRequest request) 
		{
			String acronimoTipoResorte=forma.getCriterios(indicesCriterios[2])+"";
			String nombreRptDesign="";
			String pathReport="";
			if (acronimoTipoResorte.equals(ConstantesIntegridadDominio.acronimoReporteResumidoXTipoConvenio))
			{
				nombreRptDesign = "resumidoXTipoConvenio.rptdesign";
				pathReport=ParamsBirtApplication.getReportsPath()+"cartera/resumidoXTipoConvenio/";
			}
			else
				if (acronimoTipoResorte.equals(ConstantesIntegridadDominio.acronimoReporteResumidoXConvenio))
				{
					nombreRptDesign = "resumidoXConvenio.rptdesign";
					pathReport=ParamsBirtApplication.getReportsPath()+"cartera/resumidoXConvenio/";
				}
				else
					if (acronimoTipoResorte.equals(ConstantesIntegridadDominio.acronimoReporteDetalladoXFactura))
					{
						nombreRptDesign = "detalladoXFactura.rptdesign";
						pathReport=ParamsBirtApplication.getReportsPath()+"cartera/detalladoXFactura/";
					}
			
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			HashMap criterios = new HashMap();
			criterios.putAll(forma.getCriterios());
			//Informacion del Cabezote
	        DesignEngineApi comp; 
	        comp = new DesignEngineApi (pathReport,nombreRptDesign);
	        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
	        comp.insertGridHeaderOfMasterPage(0,1,1,4);
	        Vector v=new Vector();
	        v.add(ins.getRazonSocial());
	       
	        v.add( Utilidades.getDescripcionTipoIdentificacion(con, ins.getTipoIdentificacion())+" "+ins.getNit());     
	        v.add(ins.getDireccion());
	        v.add("Tels. "+ins.getTelefono());
	        comp.insertLabelInGridOfMasterPage(0,1,v);
	        comp.insertLabelInGridPpalOfHeader(1,1, "CUENTAS POR COBRAR");
	        comp.insertLabelInGridPpalOfHeader(2,1, "RECAUDO CARTERA EVENTO");
	        comp.insertLabelInGridPpalOfHeader(1,2, "Tipo: "+ValoresPorDefecto.getIntegridadDominio(acronimoTipoResorte));
	        comp.insertLabelInGridPpalOfHeader(2,2, "Período: "+criterios.get(indicesCriterios[0])+" - "+criterios.get(indicesCriterios[1]));

	        //---------------------------------------------------------------------------------------------------------------------------------
	        //-------------------------------------SE ORGANIZAN LOS FILTROS PARA LA CONSULTA --------------------------------------------------
	        //---------------------------------------------------------------------------------------------------------------------------------
			String cadena ="  pge.institucion="+usuarioActual.getCodigoInstitucion();
			// Rango Entre Fechas (Fecha Inicial  - Fecha Final )
			if (!(criterios.get(indicesCriterios[0])+"").equals("") && !(criterios.get(indicesCriterios[0])+"").equals("null")
				&& !(criterios.get(indicesCriterios[1])+"").equals("") && !(criterios.get(indicesCriterios[1])+"").equals("null"))	
				cadena+=" AND to_char(pge.fecha_documento,'yyyy-mm-dd') between '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[0])+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[1])+"")+"'";
			
			// Tipo Convenio
			if (!(criterios.get(indicesCriterios[3])+"").equals("") && !(criterios.get(indicesCriterios[3])+"").equals("null"))
				cadena += " AND gettipoconvenio(pge.convenio)='"+criterios.get(indicesCriterios[3])+"'";
			
			//convenio
			if (!(criterios.get(indicesCriterios[4])+"").equals("") && !(criterios.get(indicesCriterios[4])+"").equals("null"))
				cadena+=" AND pge.convenio="+criterios.get(indicesCriterios[4]);
			  
			cadena+=" order by getacronimotipodocumento(pge.tipo_doc),  documento";
			
			comp.obtenerComponentesDataSet("ListadoCamas");
			  
	        String newQuery=comp.obtenerQueryDataSet().replace("1=2", cadena);
	        //--------------------------------------------------------------------------------------------------------------------------------------
	        
	        
	        logger.info("\n la consuta ----->>"+newQuery);
	        
	        //Se modifica el query
	        comp.modificarQueryDataSet(newQuery);
	                
	        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	        comp.updateJDBCParameters(newPathReport);
	        
	       
	        
	        if(!newPathReport.equals(""))
	        {
	        	request.setAttribute("isOpenReport", "true");
	        	request.setAttribute("newPathReport", newPathReport);
	        }
	       
		}
		
		/**
		 * Metodo encargado de organizar los datos con ","
		 * para el reporte resumido por tipo convenio
		 * @param datos
		 * @param criterios
		 * @return
		 */
		public static StringBuffer cargarMapa(HashMap datos, HashMap criterios)
		{
			StringBuffer cadena = new StringBuffer();
			
			cadena.append("Reporte: Recaudo Cartera Evento \n");
			cadena.append("Tipo Reporte: "+ValoresPorDefecto.getIntegridadDominio(criterios.get(indicesCriterios[2])+"")+"\n");
			cadena.append("Período: "+criterios.get(indicesCriterios[0])+" al "+criterios.get(indicesCriterios[1])+"\n");
			cadena.append("Tipo Convenio, Tipo Documento, Documento, Fecha Recaudo, Valor Recaudo"+"\n");
			
			for(int i=0; i<Utilidades.convertirAEntero(datos.get("numRegistros")+""); i++)
			{
				cadena.append(datos.get(indicesResultado[0]+i)+","+datos.get(indicesResultado[1]+i)+","+datos.get(indicesResultado[2]+i)+","
							  +datos.get(indicesResultado[3]+i)+","+UtilidadTexto.formatearExponenciales((Utilidades.convertirADouble(datos.get(indicesResultado[4]+i)+"")))+"\n");
			}
			
			return cadena;
		}
		
		/**
		 * Metodo encargado de organizar los datos con ","
		 * para el reporte resumido por convenio
		 * @param datos
		 * @param criterios
		 * @return
		 */
		public static StringBuffer cargarMapaXConvenio(HashMap datos, HashMap criterios)
		{
			StringBuffer cadena = new StringBuffer();
			
			cadena.append("Reporte: Recaudo Cartera Evento \n");
			cadena.append("Tipo Reporte: "+ValoresPorDefecto.getIntegridadDominio(criterios.get(indicesCriterios[2])+"")+"\n");
			cadena.append("Período: "+criterios.get(indicesCriterios[0])+" al "+criterios.get(indicesCriterios[1])+"\n");
			cadena.append("Tipo Convenio, Convenio, Tipo Documento, Documento, Fecha Recaudo, Valor Recaudo"+"\n");
			
			for(int i=0; i<Utilidades.convertirAEntero(datos.get("numRegistros")+""); i++)
			{
				cadena.append(datos.get(indicesResultado[0]+i)+","+datos.get(indicesResultado[5]+i)+","+datos.get(indicesResultado[1]+i)+","+datos.get(indicesResultado[2]+i)+","
							  +datos.get(indicesResultado[3]+i)+","+UtilidadTexto.formatearExponenciales((Utilidades.convertirADouble(datos.get(indicesResultado[4]+i)+"")))+"\n");
			}
			
			return cadena;
		}
		
		
		public static StringBuffer cargarMapaXFactura (HashMap datos, HashMap criterios)
		{
			StringBuffer cadena = new StringBuffer();
			
			cadena.append("Reporte: Recaudo Cartera Evento \n");
			cadena.append("Tipo Reporte: "+ValoresPorDefecto.getIntegridadDominio(criterios.get(indicesCriterios[2])+"")+"\n");
			cadena.append("Período: "+criterios.get(indicesCriterios[0])+" al "+criterios.get(indicesCriterios[1])+"\n");
			cadena.append("Tipo Convenio, Convenio,Factura,Fecha Fact., Tipo y No. Doc, Fecha Recaudo, Valor Recaudo"+"\n");
			
			for(int i=0; i<Utilidades.convertirAEntero(datos.get("numRegistros")+""); i++)
			{
				cadena.append(datos.get(indicesResultado[0]+i)+","+datos.get(indicesResultado[5]+i)+","+datos.get(indicesResultado[6]+i)+","+datos.get(indicesResultado[7]+i)+","
							  +datos.get(indicesResultado[1]+i)+" - "+datos.get(indicesResultado[2]+i)+","+datos.get(indicesResultado[3]+i)+","+UtilidadTexto.formatearExponenciales((Utilidades.convertirADouble(datos.get(indicesResultado[4]+i)+"")))+"\n");
				
				
			}
			
			return cadena;
		}
		
		
		/**
		 * Metodo encargado de organizar los datos para
		 * enviarlos al log
		 * @param criterios
		 * @param usuario
		 * @param nombreReporte
		 * @return
		 */
		public static HashMap organzarDatosLog (HashMap criterios, UsuarioBasico usuario,String nombreReporte)
		{
			HashMap result = new HashMap ();
			
			result=(HashMap)criterios.clone();
			//reporte
			result.put(indicesCriterios[7], "Recaudo Cartera Evento");
			//nombre reporte (aqui va incluido la ruta)
			result.put(indicesCriterios[8], nombreReporte);
			//institucion
			result.put(indicesCriterios[6], usuario.getCodigoInstitucion());
			//usuario
			result.put(indicesCriterios[9], usuario.getLoginUsuario());

				
			return result;
		}
		
				
		
}