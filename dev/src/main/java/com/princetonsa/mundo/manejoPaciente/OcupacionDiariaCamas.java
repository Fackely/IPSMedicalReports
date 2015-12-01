package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.CsvFile;
import util.TxtFile;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;
import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.OcupacionDiariaCamasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.OcupacionDiariaCamasDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.OcupacionDiariaCamas;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
/**
 * Fecha: Febrero - 2008
 * @author Jhony Alexander Duque A.
 *
 */

public class OcupacionDiariaCamas 
{
	/**
	 * logger ocupacion diraria de camas
	 */
	public static Logger logger = Logger.getLogger(OcupacionDiariaCamas.class);
	
	/*--------------------------------------------------------
	 *  	INDICES QUE SE MANEJARAN EN ESTA PALICACION
	 --------------------------------------------------------*/
		
	//----------INDICES PARA EL MANEJO DE LOS CRITERIOS DE BUSQUEDA --------------------------------
	
	public static final String indicesCriterios [] = {"centroAtencion0","fechaInicial1","fechaFinal2","rompimientoPorPiso3",
													   "incluirCamasUrgencias4","estadosCama5","institucion6","tipoSalida7"};
	
	public static final String indicesEstados [] = {"codigo_","nombre_","check_","graficar_"};
	/*--------------------------------------------------------
	 *    FIN INDICES QUE SE MANEJARAN EN ESTA PALICACION
	 --------------------------------------------------------*/
	
	
	/*--------------------------------------------------------------------------------------------------------
	 *         							METODOS DE OCUPACION DIARIA DE CAMAS
	 ---------------------------------------------------------------------------------------------------------*/
	/**
	 * Se inicializa el Dao
	 */
	
	public static OcupacionDiariaCamasDao ocupacionDiariaCamasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOcupacionDiariaCamasDao();
	}
	
	
	
	
	/**
	 * Metodo encargado de cargar los datos a mostrar en la vista
	 * de ocuapacion diaria de camas.
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void empezar (Connection connection,OcupacionDiariaCamasForm forma,UsuarioBasico usuario)
	{
		forma.reset();
		//se cargan los centros de atencion a mostrar en el select
		forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(connection, usuario.getCodigoInstitucionInt(),""));
		//se postula el centro de atencion del usuario en session 
		forma.setCriterios(indicesCriterios[0], usuario.getCodigoCentroAtencion());
		//por defecto el campo rompimiento por piso debe estar checkqado.
		forma.setCriterios(indicesCriterios[3],ConstantesBD.acronimoSi);
		//se cargan los estados de la cama
		forma.setEstadosCamas(TotalOcupacionCamas.consultarEstados(connection)) ;
		
	}
	
	
	/**
	 * Metodo encargado de generar las clausulas 
	 * where para la consulta en el birt.
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- institucion6 --> Requerido
	 * -- centroAtencion0 --> Requerido
	 * -- fechaInicial1 --> Requerido
	 * -- fechaFinal2 --> Requerido
	 * -- incluirCamasUrgencias4 --> Opcional
	 * -- rompimientoPorPiso3 --> Opcional
	 * @param estadosCama debe ir ejm. 1,2,3
	 * @return cadena Where
	 */
	public static String obtenerWhere (HashMap criterios, String estadosCama)
	{
		return ocupacionDiariaCamasDao().obtenerWhere(criterios, estadosCama);
	}
	
	
	/**
	 * Metodo encargado de consultar
	 * la informacion para el archivo plano
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- institucion6 --> Requerido
	 * -- centroAtencion0 --> Requerido
	 * -- fechaInicial1 --> Requerido
	 * -- fechaFinal2 --> Requerido
	 * -- incluirCamasUrgencias4 --> Opcional
	 * -- rompimientoPorPiso3 --> Opcional
	 * @param estadosCama debe ir ejm. 1,2,3
	 * @return mapa 
	 * -----------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -----------------------------
	 * cantidad0, codigoEstado1,
	 * nomEstadoCama2,piso3,
	 * fecha4
	 */
	public static HashMap consulta (Connection connection, HashMap criterios,String estadosCama)
	{
		return ocupacionDiariaCamasDao().consulta(connection, criterios, estadosCama);
	}
	
	
	/**
	 * Metodo encargado de identificar que tipo se salida se eligio
	 * y retorna el archivo plano o en pdf.
	 * @param connection
	 * @param usuario
	 * @param forma
	 * @param request
	 * @param institucion
	 * @param mapping
	 * @return
	 * @throws SQLException
	 */
	public static ActionForward generar (Connection connection,UsuarioBasico usuario, OcupacionDiariaCamasForm forma, HttpServletRequest request,InstitucionBasica institucion,ActionMapping mapping) throws SQLException
	{
		forma.setCriterios(indicesCriterios[6], usuario.getCodigoInstitucion());
		forma.setCriterios(indicesCriterios[4], ConstantesBD.acronimoSi);
		if ((forma.getCriterios(indicesCriterios[7])+"").equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			return generarReporte(connection, usuario, forma, request,mapping);
		else
			return archivoPlano(connection, forma, usuario, institucion, request, mapping);
				
	}
	
	
	/**
	 * Metodo encargado de generar el archivo plano.
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param institucion
	 * @param request
	 * @param mapping
	 * @return
	 * @throws SQLException
	 */
	public static ActionForward archivoPlano (Connection connection, OcupacionDiariaCamasForm forma,UsuarioBasico usuario, InstitucionBasica institucion,HttpServletRequest  request,ActionMapping mapping ) throws SQLException
	{
		HashMap tmp = new HashMap();
		tmp.putAll(consulta(connection, forma.getCriterios(), organizarEstadosCama(forma.getEstadosCamas())));
		boolean OperacionTrue=false,existeTxt=false;
		int ban=ConstantesBD.codigoNuncaValido;
		//se llama al garbage collector
		System.gc();
		
		//Iniciamos Transaccion
		UtilidadBD.iniciarTransaccion(connection);
		
		//Cargamos en path la ruta definida en parametros generales para validar si esta ruta no esta vacia
		String path = ValoresPorDefecto.getArchivosPlanosReportes(usuario.getCodigoInstitucionInt());
		logger.info("====>Path Valor por Defecto: "+path);
		//Validamos si el path esta vacio o lleno
    	if(UtilidadTexto.isEmpty(path))
		{
    		
    		forma.setOperacionTrue(false);
    		forma.setExisteArchivo(false);
    		UtilidadBD.abortarTransaccion(connection);
	    	UtilidadBD.closeConnection(connection);
	    	return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "Inventarios", "error.manejoPacientes.rutaNoDefinida", true);
		}
		
		
		//arma el nombre del reporte
		String nombreReport=CsvFile.armarNombreArchivo("Ocupacion-Diaria-Camas", usuario);
		//se genera el documento con la informacion
		
		if (Utilidades.convertirAEntero(tmp.get("numRegistros")+"")>0)
		{
			if (UtilidadTexto.getBoolean(forma.getCriterios(indicesCriterios[3])+""))
				OperacionTrue=TxtFile.generarTxt(cargarMapaConRompimiento(connection, forma, institucion, usuario), nombreReport, ValoresPorDefecto.getReportPath()+path, ".csv");
			else
				OperacionTrue=TxtFile.generarTxt(cargarMapaSinRompimiento(connection, forma, institucion, usuario), nombreReport, ValoresPorDefecto.getReportPath()+path, ".csv");
		}
		if (OperacionTrue)
		{
			//se genera el archivo en formato Zip
			ban=BackUpBaseDatos.EjecutarComandoSO("zip -j "+ValoresPorDefecto.getReportPath()+path+nombreReport+".zip"+" "+ValoresPorDefecto.getReportPath()+path+nombreReport+".csv");
			//se ingresa la direccion donde se almaceno el archivo
			forma.setRuta(ValoresPorDefecto.getReportPath()+path+nombreReport+".csv");
			//se ingresa la ruta para poder descargar el archivo
			forma.setUrlArchivo(ValoresPorDefecto.getReportUrl()+path+nombreReport+".zip");
			

			//se valida si existe el csv
			existeTxt=UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getReportPath()+path, nombreReport+".csv");
			//se valida si existe el zip
			forma.setExisteArchivo(UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getReportPath()+path, nombreReport+".zip"));
									
			if (existeTxt )
				forma.setOperacionTrue(true);
		
		}
			UtilidadBD.cerrarConexion(connection);
			return mapping.findForward("principal");
	
}
	
	/**
	 * Metodo encargado de organizarlos datos de la consulta
	 * para mostrarlos en el archivo plano
	 * @param connection
	 * @param forma
	 * @param institucion
	 * @param usuario
	 * @return
	 */
	public static StringBuffer cargarMapaSinRompimiento(Connection connection, OcupacionDiariaCamasForm forma,InstitucionBasica institucion,UsuarioBasico usuario)
	{
		StringBuffer cadena = new StringBuffer();
		Vector criterios = new Vector(); 
		
		//razon social institucion
		cadena.append(institucion.getRazonSocial()+"\n");
		//nit
		cadena.append(institucion.getNit()+"\n");
		//direccion
		cadena.append(institucion.getDireccion()+"\n");
		//telefono
		cadena.append(institucion.getTelefono()+"\n");
		//titulo del reporte
		cadena.append("Reporte: OCUPACION DIARIA CAMAS \n\n");  
		//criterios de busqueda del reporte
		cadena.append(organizarCriterios(connection, forma)+"\n ");
		//----------------------------------------------------------------------------///
		//se consultan los datos
		HashMap datos = new HashMap();
		datos.putAll(consulta(connection, forma.getCriterios(), organizarEstadosCama(forma.getEstadosCamas())));
		//------------------------------------------------------------------------------------////
		int numReg = Utilidades.convertirAEntero(datos.get("numRegistros")+"");
		//titulo de la tabla
		cadena.append("Ocupacion total diaria camas \n");
		//se crea el titulo dinamico
		cadena.append(crearTitulo(datos)+", Total, % \n");
		//se añaden las columnas
		cadena.append(crearColumnas(connection,datos, forma.getEstadosCamas()));
		//se añaden la columna de totales por dia
		cadena.append(columnaTotalXDia(datos));
		//se coloca otra seccion con los datos de los porcentajes
		cadena.append("\n\n\n");
		//se pone el titulo de los porcentajes
		cadena.append("Porcentajes Ocupacion Total Diaria Camas\n");
		//se crean los titulos
		cadena.append(crearTitulo(datos)+", Total \n");
		//porcentajes
		cadena.append(crearColumnasPorcentaje(connection, datos, forma.getEstadosCamas()));
		
		
		return cadena;
	}
	
	public static StringBuffer cargarMapaConRompimiento(Connection connection, OcupacionDiariaCamasForm forma,InstitucionBasica institucion,UsuarioBasico usuario)
	{
		logger.info("\n entro a cargarMapaConRompimiento");
		StringBuffer cadena = new StringBuffer();
		Vector criterios = new Vector(); 
		
		//razon social institucion
		cadena.append(institucion.getRazonSocial()+"\n");
		//nit
		cadena.append(institucion.getNit()+"\n");
		//direccion
		cadena.append(institucion.getDireccion()+"\n");
		//telefono
		cadena.append(institucion.getTelefono()+"\n");
		//titulo del reporte
		cadena.append("Reporte: OCUPACION DIARIA CAMAS \n\n");  
		//criterios de busqueda del reporte
		cadena.append(organizarCriterios(connection, forma)+"\n ");
		//----------------------------------------------------------------------------///
		//se consultan los datos
		HashMap datos = new HashMap();
		datos.putAll(consulta(connection, forma.getCriterios(), organizarEstadosCama(forma.getEstadosCamas())));
		//------------------------------------------------------------------------------------////
		//titulo de la tabla
		cadena.append("Ocupacion total diaria de camas por Pisos\n");
		//se crea el titulo dinamico
		cadena.append(crearTituloRompimiento(forma.getCriterios())+", Total \n");
		//se añaden las columnas
		cadena.append(CrearColumnasRompimiento(connection,datos, forma.getEstadosCamas(),forma.getCriterios()));
		//se coloca otra seccion con los datos de los porcentajes
		cadena.append("\n\n\n");
		//se pone el titulo de los porcentajes
		cadena.append("Porcentajes Ocupacion Total Diaria Camas\n");
		//se crea el titulo dinamico
		cadena.append(crearTituloRompimiento(forma.getCriterios())+", Total \n");
		//se colocan las columnas de porcentajes
		cadena.append(CrearColumnasRompimientoPorcentajes(connection, datos, forma.getEstadosCamas(), forma.getCriterios()));
		
		
		return cadena;
	}
	
	
	/**
	 * Metodo encargado de consultar los porcentajes decada
	 * piso por tipo habitacion y dia
	 * @param connection
	 * @param datos
	 * @param estadosCama
	 * @param criterios
	 * @return
	 */
	public static String CrearColumnasRompimientoPorcentajes (Connection connection,HashMap datos,HashMap estadosCama,HashMap criterios)
	{
		//logger.info("\n entro a CrearColumnasRompimiento ");
		int numReg=Utilidades.convertirAEntero(datos.get("numRegistros")+"");
		String cadena = "";
		String estados=organizarEstadosCama(estadosCama);
		String [] estado = estados.split(",");
		String dias = crearTituloRompimiento(criterios)+"";
		String [] dia = dias.split(",");
		for (int k=0;k<numReg;k++)
		{
			//logger.info("\n entro con k== "+k);
			if(!(datos.get("codigoPiso4_"+k)+"").equals(datos.get("codigoPiso4_"+(k-1))+""))
			{
				//logger.info("\n paso 1 con k== "+k);
				cadena+=Utilidades.obtenerNombrePiso(connection, datos.get("codigoPiso4_"+k)+"")+" \n";
				String subtotales=totalColumnaDia(connection, datos, estadosCama, criterios, datos.get("codigoPiso4_"+k)+"");
				String [] totales=subtotales.split(",");
				for (int i=0;i<estado.length;i++)
				{
					//logger.info("\n paso 2 con i== "+datos.get("codigoPiso4_"+k));		
					cadena+=Utilidades.obtenerNombreEstadoCama(connection, estado[i]);
					//logger.info("\n el estado -->"+estado[i]);
					float cantidadTotal=0;
					for (int j=0;j<dia.length;j++)
					{
						//logger.info("\n el dia -->"+dia[j]);
						if (UtilidadCadena.noEsVacio(dia[j]))
						{
							//logger.info("\n con el piso == "+cantidad(datos, estado[i], dia[j],datos.get("codigoPiso4_"+k)+""));		
							String cantidad=cantidad(datos, estado[i], dia[j],datos.get("codigoPiso4_"+k)+"");
							float cant=Float.parseFloat(cantidad);
							if (cant>0)
							{
								cantidadTotal+=cant;
								cadena+=", "+UtilidadTexto.formatearValores((cant/Float.parseFloat(totales[j-1]))*100);
							}
							else
								cadena+=", 0";
								
						}
					}
					cadena+=","+UtilidadTexto.formatearValores((cantidadTotal/totalesTotalPorcentajes(datos, datos.get("codigoPiso4_"+k)+"", estadosCama, criterios))*100)+" \n";
				}
				
			}
		}
		//logger.info("\n cadena -->"+cadena);
		return cadena;
	}
	
	
	public static int totalesTotalPorcentajes (HashMap datos,String piso,HashMap estadosCama,HashMap criterios)
	{
		//logger.info("\n entro a CrearColumnasRompimiento ");
		String estados=organizarEstadosCama(estadosCama);
		String [] estado = estados.split(",");
		String dias = crearTituloRompimiento(criterios)+"";
		String [] dia = dias.split(",");
		int total=0;
		for (int i=0;i<estado.length;i++)
		{
				int subTotal=0;
				for (int j=0;j<dia.length;j++)
				{
					//logger.info("\n el dia -->"+dia[j]);
					if (UtilidadCadena.noEsVacio(dia[j]))
					{
						//logger.info("\n con el piso == "+cantidad(datos, estado[i], dia[j],datos.get("codigoPiso4_"+k)+""));		
						String cantidad=cantidad(datos, estado[i], dia[j],piso);
						int cant=Utilidades.convertirAEntero(cantidad);
						if (cant>0)
							subTotal+=cant;
					}
				}
				total+=subTotal;
		}
				
			
		
		return total;
	}
	
	
	/**
	 * Metodo encargado de calcular la cantidad de 
	 * de camas por cada  piso y tipo de habitacion
	 * @param connection
	 * @param datos
	 * @param estadosCama
	 * @param criterios
	 * @return
	 */
	public static String CrearColumnasRompimiento(Connection connection,HashMap datos,HashMap estadosCama,HashMap criterios)
	{
		//logger.info("\n entro a CrearColumnasRompimiento ");
		int numReg=Utilidades.convertirAEntero(datos.get("numRegistros")+"");
		String cadena = "";
		String estados=organizarEstadosCama(estadosCama);
		String [] estado = estados.split(",");
		String dias = crearTituloRompimiento(criterios)+"";
		String [] dia = dias.split(",");
		for (int k=0;k<numReg;k++)
		{
			//logger.info("\n entro con k== "+k);
			if(!(datos.get("codigoPiso4_"+k)+"").equals(datos.get("codigoPiso4_"+(k-1))+""))
			{
				////logger.info("\n paso 1 con k== "+k);
				cadena+=Utilidades.obtenerNombrePiso(connection, datos.get("codigoPiso4_"+k)+"")+" \n";
			
				for (int i=0;i<estado.length;i++)
				{
					//logger.info("\n paso 2 con i== "+datos.get("codigoPiso4_"+k));		
					cadena+=Utilidades.obtenerNombreEstadoCama(connection, estado[i]);
					//logger.info("\n el estado -->"+estado[i]);
					float totalDer=0;
					for (int j=0;j<dia.length;j++)
					{
						//logger.info("\n el dia -->"+dia[j]);
						if (UtilidadCadena.noEsVacio(dia[j]))
						{
							//logger.info("\n con el piso == "+cantidad(datos, estado[i], dia[j],datos.get("codigoPiso4_"+k)+""));		
							String cantidad=cantidad(datos, estado[i], dia[j],datos.get("codigoPiso4_"+k)+"");
							cadena+=", "+cantidad;
							int cant=Utilidades.convertirAEntero(cantidad);
							if (cant>0)
								totalDer=totalDer+cant;
						}
					}
					cadena+=", "+totalDer+", \n";
				}
				cadena+="Total, "+totalColumnaDiaPorcentaje(connection, datos, estadosCama, criterios, datos.get("codigoPiso4_"+k)+"")+"\n";
			}
		}
		//logger.info("\n cadena -->"+cadena);
		return cadena;
	}
	
	
	
	public static String totalColumnaDiaPorcentaje(Connection connection,HashMap datos,HashMap estadosCama,HashMap criterios,String piso)
	{
		//logger.info("\n entro a totalColumnaDiaPorcentaje ");
		String cadena = "";
		String estados=organizarEstadosCama(estadosCama);
		String [] estado = estados.split(",");
		String dias = crearTituloRompimiento(criterios)+"";
		String [] dia = dias.split(",");
		int cantDer=0;
					for (int j=0;j<dia.length;j++)
					{
						int total=0;
						//logger.info("\n el dia -->"+dia[j]);
						if (UtilidadCadena.noEsVacio(dia[j]))
						{
							for (int i=0;i<estado.length;i++)
							{
								//logger.info("\n con el piso == "+cantidad(datos, estado[i], dia[j],datos.get("codigoPiso4_"+k)+""));		
								String cantidad=cantidad(datos, estado[i], dia[j],piso);
								int cant=Utilidades.convertirAEntero(cantidad);
								if (cant>0)
									total=total+cant;
							}
							cantDer+=total;
							if(!UtilidadCadena.noEsVacio(cadena))
								cadena+=total;
							else
								cadena+=","+total;
						}
						
					}
		return cadena+","+cantDer;
	}
	
	
	public static String porcentajeColumnaDia(Connection connection,HashMap datos,HashMap estadosCama,HashMap criterios,String piso)
	{
		logger.info("\n entro a totalColumnaDia ");
		String cadena = "";
		String estados=organizarEstadosCama(estadosCama);
		String [] estado = estados.split(",");
		String dias = crearTituloRompimiento(criterios)+"";
		String [] dia = dias.split(",");
		int cantDer=0;
					for (int j=0;j<dia.length;j++)
					{
						int total=0;
						//logger.info("\n el dia -->"+dia[j]);
						if (UtilidadCadena.noEsVacio(dia[j]))
						{
							for (int i=0;i<estado.length;i++)
							{
								//logger.info("\n con el piso == "+cantidad(datos, estado[i], dia[j],datos.get("codigoPiso4_"+k)+""));		
								String cantidad=cantidad(datos, estado[i], dia[j],piso);
								int cant=Utilidades.convertirAEntero(cantidad);
								if (cant>0)
									total=total+cant;
							}
							cantDer+=total;
							if(!UtilidadCadena.noEsVacio(cadena))
								cadena+=total;
							else
								cadena+=","+total;
						}
					}
		return cadena+","+cantDer;
	}
	
	
	
	/**
	 * Metodo encargado de calcular el total de 
	 * la columna de cada piso por tipo de habitacion
	 * @param connection
	 * @param datos
	 * @param estadosCama
	 * @param criterios
	 * @param piso
	 * @return
	 */
	public static String totalColumnaDia(Connection connection,HashMap datos,HashMap estadosCama,HashMap criterios,String piso)
	{
		//logger.info("\n entro a totalColumnaDia ");
		String cadena = "";
		String estados=organizarEstadosCama(estadosCama);
		String [] estado = estados.split(",");
		String dias = crearTituloRompimiento(criterios)+"";
		String [] dia = dias.split(",");
		int cantDer=0;
					for (int j=0;j<dia.length;j++)
					{
						int total=0;
						//logger.info("\n el dia -->"+dia[j]);
						if (UtilidadCadena.noEsVacio(dia[j]))
						{
							for (int i=0;i<estado.length;i++)
							{
								//logger.info("\n con el piso == "+cantidad(datos, estado[i], dia[j],datos.get("codigoPiso4_"+k)+""));		
								String cantidad=cantidad(datos, estado[i], dia[j],piso);
								int cant=Utilidades.convertirAEntero(cantidad);
								if (cant>0)
									total=total+cant;
							}
							cantDer+=total;
							if(!UtilidadCadena.noEsVacio(cadena))
								cadena+=total;
							else
								cadena+=","+total;
						}
					}
		return cadena+","+cantDer;
	}
	
	
	
	
	/**
	 * Metodo encargado de crear el titulo de las columnas
	 * puesto que esta son dinamicas
	 * @param datos
	 * @return
	 */
	public static String crearColumnas (Connection connection,HashMap datos,HashMap estadosCama)
	{
		String columna = "";
		//int numReg = Utilidades.convertirAEntero(datos.get("numRegistros")+"");
		String estados=organizarEstadosCama(estadosCama);
		String [] estado = estados.split(",");
		String dias = crearTitulo(datos)+"";
		String [] dia = dias.split(",");
		int totalDeTotales=totalDeTotales(datos);
		for (int i=0;i<estado.length;i++)
		{
			
			columna+=Utilidades.obtenerNombreEstadoCama(connection, estado[i]);
			//logger.info("\n el estado -->"+estado[i]);
			float totalDer=0;
			for (int j=0;j<dia.length;j++)
			{
			//	logger.info("\n el dia -->"+dia[j]);
				if (UtilidadCadena.noEsVacio(dia[j]))
				{
					String cantidad=cantidad(datos, estado[i], dia[j]);
					//logger.info("\n cantidad --> "+cantidad(datos, estado[i], dia[j]));
					columna+=", "+cantidad;
					int cant=Utilidades.convertirAEntero(cantidad);
					if (cant>0)
						totalDer=totalDer+cant;
				}
				
				
				
			}
		
			columna+=", "+totalDer+", "+UtilidadTexto.formatearValores(((totalDer/totalDeTotales)*100))+" \n";
		}
		
		return columna;
	}
	
	
	
	/**
	 * Metodo encargado de calcular el porcentaje
	 * de cada dia por estado de cama. 
	 * @param connection
	 * @param datos
	 * @param estadosCama
	 * @return
	 */
	public static String crearColumnasPorcentaje (Connection connection,HashMap datos,HashMap estadosCama)
	{
		String columna = "";
		String estados=organizarEstadosCama(estadosCama);
		String [] estado = estados.split(",");
		String dias = crearTitulo(datos)+"";
		String [] dia = dias.split(",");
		int totalDeTotales=totalDeTotales(datos);
		for (int i=0;i<estado.length;i++)
		{
			
			columna+=Utilidades.obtenerNombreEstadoCama(connection, estado[i]);
			//logger.info("\n el estado -->"+estado[i]);
			float totalDer=0;
			for (int j=0;j<dia.length;j++)
			{
				int totalXdia=totalxDia(datos, dia[j]);
				//logger.info("\n el dia -->"+dia[j]);
				if (UtilidadCadena.noEsVacio(dia[j]))
				{
					String cantidad=cantidad(datos, estado[i], dia[j]);
					int cant=Utilidades.convertirAEntero(cantidad);
					if(cant>0)
					{
						columna+=", "+UtilidadTexto.formatearValores((Float.parseFloat(cantidad)/Float.parseFloat(totalXdia+""))*100);
						totalDer=totalDer+cant;
					}
					else
						columna+=", 0";
				}
				
			}
			
			columna+=", "+UtilidadTexto.formatearValores(((totalDer/totalDeTotales)*100))+" \n";
		}
		
		return columna;
	}
	
	
	
	
	/**
	 * Metodo encargado de obtener el
	 * total de totales
	 * @param datos
	 * @return
	 */
	public static String columnaTotalXDia (HashMap datos)
	{
		String dias = crearTitulo(datos)+"";
		String [] dia = dias.split(",");
		String total=" Total ";
		for (int j=0;j<dia.length;j++)
		{
			if (UtilidadCadena.noEsVacio(dia[j]))
				total+=", "+totalxDia(datos, dia[j]);
		}
		
		return total+", "+totalDeTotales(datos);
	}
	
	
	
	
	/**
	 * Metodo encargado de devolver la cantidad total
	 * de camas por dia
	 * @param datos
	 * @param dia
	 * @return
	 */
	public static int totalxDia (HashMap datos, String dia)
	{
		int cant = 0;
		int numReg=Utilidades.convertirAEntero(datos.get("numRegistros")+"");

		for (int i=0;i<numReg;i++)
			if ((datos.get("fecha3_"+i)+"").equals(dia.trim()))
			{
				int tmp=Utilidades.convertirAEntero(datos.get("cantidad0_"+i)+"");
				if (tmp>0)
					cant=cant+tmp;
			}
		return cant;
	}
	
	/**
	 * Metodo encargado de calcular el total de totales
	 * @param datos
	 * @return
	 */
	public static int totalDeTotales (HashMap datos)
	{
		int total=0;
		String dias = crearTitulo(datos)+"";
		String [] dia = dias.split(",");
		for (int j=0;j<dia.length;j++)
		{
			if (UtilidadCadena.noEsVacio(dia[j]))
				total=total+totalxDia(datos, dia[j]);
		}
		
		return total;
	}
	
	/**
	 * Metodo encargado de calcular la cantidad de acuerdo
	 * al dia y el estado de la cama
	 * @param datos
	 * @param estado
	 * @param dia
	 * @return
	 */
	public static String cantidad (HashMap datos, String estado,String dia)
	{
		//logger.info("\n entre a verificar las cantidad estado-->"+estado+" dia-->"+dia +"\n mapa -->"+datos);
		int numReg = Utilidades.convertirAEntero(datos.get("numRegistros")+"");
		
		for (int i=0;i<numReg;i++)
		{
			if ((datos.get("codigoEstado1_"+i)+"").equals(estado.trim()) && (datos.get("fecha3_"+i)+"").equals(dia.trim()))
				return datos.get("cantidad0_"+i)+"";
		}
		return "";
	}
	
	
	
	public static String cantidad (HashMap datos, String estado,String dia,String piso)
	{
		//logger.info("\n entre a verificar las cantidad estado-->"+estado+"dia-->"+dia+"piso -->"+piso+"-");
		int numReg = Utilidades.convertirAEntero(datos.get("numRegistros")+"");
		
		for (int i=0;i<numReg;i++)
		{
			
			if (Utilidades.convertirAEntero(datos.get("codigoEstado1_"+i)+"")==Utilidades.convertirAEntero(estado) && 
				Utilidades.convertirAEntero(datos.get("fecha3_"+i)+"")==Utilidades.convertirAEntero(dia.trim()) &&
				Utilidades.convertirAEntero(datos.get("codigoPiso4_"+i)+"")==Utilidades.convertirAEntero(piso))
			{
			
				return datos.get("cantidad0_"+i)+"";
			}
		}
		return "0";
	}
	
	
	/**
	 * Metodo encargado de crear el titulo de las columnas
	 * puesto que esta son dinamicas
	 * @param datos
	 * @return
	 */
	public static StringBuffer crearTitulo (HashMap datos)
	{
		StringBuffer titulo = new StringBuffer();
		int numReg = Utilidades.convertirAEntero(datos.get("numRegistros")+"");
		
		for (int i=0;i<numReg;i++)
		{
			if (i==0)
				titulo.append(" , "+datos.get("fecha3_"+i));
			else
				if (!(datos.get("fecha3_"+i)+"").equals(datos.get("fecha3_"+(i-1))+""))
					titulo.append(", "+datos.get("fecha3_"+i));
		}
		
		return titulo;
	}
	
	public static StringBuffer crearTituloRompimiento (HashMap criterios) 
	{
		StringBuffer titulo = new StringBuffer();
	
		int diaInicial =UtilidadFecha.diaMes(criterios.get(indicesCriterios[1])+"");
		int diaFinal =UtilidadFecha.diaMes(criterios.get(indicesCriterios[2])+"");
		
		for (int i=diaInicial;i<=diaFinal;i++)
			titulo.append(", "+i);
		return titulo;
	}
	
	/**
	 * Metodo encargado de generar el reporte en pdf.
	 * @param connection
	 * @param usuario
	 * @param forma
	 * @param request
	 * @throws SQLException 
	 */
	public static ActionForward generarReporte (Connection connection,UsuarioBasico usuario,OcupacionDiariaCamasForm forma, HttpServletRequest request,ActionMapping mapping) throws SQLException
	{
		//se hace una llamada al recolector de basura
		System.gc();
		
		DesignEngineApi comp;
		String codigoAImprimir = "";
		String reporte="",DataSet="";
		if (UtilidadTexto.getBoolean(forma.getCriterios(indicesCriterios[3])+""))
		{
			//logger.info("\n ---> ConRompimiento");
			DataSet="ConRompimiento";
			reporte="OcupacionCamasRompimiento.rptdesign";
		}
		else
		{
			//logger.info("\n ---> SinRompimiento");
			DataSet="SinRompimiento";
			reporte="OcupacionCamas.rptdesign";
		}
		
		//LLamamos al reporte
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",reporte);
        //Imprimimos el encabezado del reporte
		armarEncabezado(comp, connection, usuario, forma, request);
		
		//se evalue el si tiene o no rompimiento
		//logger.info("\n con rompimiento "+forma.getCriterios(indicesCriterios[3]));
		
		comp.obtenerComponentesDataSet(DataSet);
		//Modificamos el DataSet con las validaciones comunes para todos
        String newQuery = comp.obtenerQueryDataSet().replace("WHERE", obtenerWhere(forma.getCriterios(), organizarEstadosCama(forma.getEstadosCamas())));
        logger.info("=====>Consulta en el BIRT Detallado por Tipo Transaccion: "+newQuery);
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
		
    	UtilidadBD.cerrarConexion(connection);
        return mapping.findForward("principal");
	
	}
	
	
	/**
	 * Metodo encargado de organizar el encabezado 
	 * para el birt
	 * @param comp
	 * @param connection
	 * @param usuario
	 * @param forma
	 * @param request
	 */
	private static void armarEncabezado(DesignEngineApi comp, Connection connection, UsuarioBasico usuario, OcupacionDiariaCamasForm forma, HttpServletRequest request)
	{
		//Insertamos la información de la Institución
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(connection,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        //Insertamos el nombre de la funcionalidad en el reporte 
        comp.insertLabelInGridPpalOfHeader(1,1, "OCUPACION DIARIA DE CAMAS");
        
        String criterios="";
                     
        if (UtilidadCadena.noEsVacio(forma.getCriterios(indicesCriterios[0])+""))
        	criterios+="Centro Atención: "+Utilidades.obtenerNombreCentroAtencion(connection, Utilidades.convertirAEntero(forma.getCriterios(indicesCriterios[0])+""));
    
        if(UtilidadCadena.noEsVacio(forma.getCriterios(indicesCriterios[1])+"") && UtilidadCadena.noEsVacio(forma.getCriterios(indicesCriterios[2])+""))
        	criterios+="  Periodo: "+forma.getCriterios(indicesCriterios[1])+" - "+forma.getCriterios(indicesCriterios[2]);
      

    	
		int numReg = Utilidades.convertirAEntero(forma.getEstadosCamas("numRegistros")+"");
        if (numReg==1)
		{
        	if ((forma.getEstadosCamas(indicesEstados[2]+"0")+"").equals(ConstantesBD.acronimoSi))
				if((forma.getEstadosCamas(indicesEstados[3]+"0")+"").equals(ConstantesBD.acronimoSi))
					criterios+="  Estado Cama: "+Utilidades.obtenerNombreEstadoCama(connection,forma.getEstadosCamas(indicesEstados[0]+"0")+"")+" [graficar]";
				else
					criterios+="  Estado Cama: "+Utilidades.obtenerNombreEstadoCama(connection,forma.getEstadosCamas(indicesEstados[0]+"0")+"");
			
		}
		else
		{
			if (numReg>1)
				for (int i=0;i<numReg;i++)
					if (i==0)
					{
						if ((forma.getEstadosCamas(indicesEstados[2]+i)+"").equals(ConstantesBD.acronimoSi))
							if((forma.getEstadosCamas(indicesEstados[3]+i)+"").equals(ConstantesBD.acronimoSi))
								criterios+="  Estados Cama: "+Utilidades.obtenerNombreEstadoCama(connection,forma.getEstadosCamas(indicesEstados[0]+i)+"")+" [detallado]";
							else
								criterios+="  Estados Cama: "+Utilidades.obtenerNombreEstadoCama(connection,forma.getEstadosCamas(indicesEstados[0]+i)+"");
					}
					else
					{
						if ((forma.getEstadosCamas(indicesEstados[2]+i)+"").equals(ConstantesBD.acronimoSi))
							if((forma.getEstadosCamas(indicesEstados[3]+i)+"").equals(ConstantesBD.acronimoSi))
								criterios+=", "+Utilidades.obtenerNombreEstadoCama(connection,forma.getEstadosCamas(indicesEstados[0]+i)+"")+" [detallado]";
							else
								criterios+=", "+Utilidades.obtenerNombreEstadoCama(connection,forma.getEstadosCamas(indicesEstados[0]+i)+"");
							
					}
					
		}
        comp.insertLabelInGridPpalOfHeader(3, 0, criterios);
     
        
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
    }
	
	
	
	/**
	 * Metodo encargado de organizar los criterios
	 * de busqueda para ser mostrados en el cabezado 
	 * del birt.
	 * @param connection
	 * @param forma
	 * @return
	 */
	public static String organizarCriterios (Connection connection,OcupacionDiariaCamasForm forma)
	{
		String criterios="";
		  if (UtilidadCadena.noEsVacio(forma.getCriterios(indicesCriterios[0])+""))
	        	criterios+="Centro Atención: "+Utilidades.obtenerNombreCentroAtencion(connection, Utilidades.convertirAEntero(forma.getCriterios(indicesCriterios[0])+""));
	    
	        if(UtilidadCadena.noEsVacio(forma.getCriterios(indicesCriterios[1])+"") && UtilidadCadena.noEsVacio(forma.getCriterios(indicesCriterios[2])+""))
	        	criterios+="  Periodo: "+forma.getCriterios(indicesCriterios[1])+" - "+forma.getCriterios(indicesCriterios[2]);
	      

	    	
			int numReg = Utilidades.convertirAEntero(forma.getEstadosCamas("numRegistros")+"");
	        if (numReg==1)
			{
	        	if ((forma.getEstadosCamas(indicesEstados[2]+"0")+"").equals(ConstantesBD.acronimoSi))
					if((forma.getEstadosCamas(indicesEstados[3]+"0")+"").equals(ConstantesBD.acronimoSi))
						criterios+="  Estado Cama: "+Utilidades.obtenerNombreEstadoCama(connection,forma.getEstadosCamas(indicesEstados[0]+"0")+"")+" [graficar]";
					else
						criterios+="  Estado Cama: "+Utilidades.obtenerNombreEstadoCama(connection,forma.getEstadosCamas(indicesEstados[0]+"0")+"");
				
			}
			else
			{
				if (numReg>1)
					for (int i=0;i<numReg;i++)
						if (i==0)
						{
							if ((forma.getEstadosCamas(indicesEstados[2]+i)+"").equals(ConstantesBD.acronimoSi))
								if((forma.getEstadosCamas(indicesEstados[3]+i)+"").equals(ConstantesBD.acronimoSi))
									criterios+="  Estados Cama: "+Utilidades.obtenerNombreEstadoCama(connection,forma.getEstadosCamas(indicesEstados[0]+i)+"")+" [detallado]";
								else
									criterios+="  Estados Cama: "+Utilidades.obtenerNombreEstadoCama(connection,forma.getEstadosCamas(indicesEstados[0]+i)+"");
						}
						else
						{
							if ((forma.getEstadosCamas(indicesEstados[2]+i)+"").equals(ConstantesBD.acronimoSi))
								if((forma.getEstadosCamas(indicesEstados[3]+i)+"").equals(ConstantesBD.acronimoSi))
									criterios+=" "+Utilidades.obtenerNombreEstadoCama(connection,forma.getEstadosCamas(indicesEstados[0]+i)+"")+" [detallado]";
								else
									criterios+=" "+Utilidades.obtenerNombreEstadoCama(connection,forma.getEstadosCamas(indicesEstados[0]+i)+"");
								
						}
			}
	        
	      return criterios;
	}
	
	
	/**
	 * Metodo encargado de organizar los estados
	 * de las camas para enviarlos a la consulta.
	 * ejm. 1,2,3
	 * @param mapa
	 * @return
	 */
	public static String organizarEstadosCama (HashMap mapa)
	{
		//logger.info("\n entre a organizarEstadosCama");
		String estados ="";
		int numReg = Utilidades.convertirAEntero(mapa.get("numRegistros")+""); 
		
		for (int i=0;i<numReg;i++) 
		{
			if (UtilidadTexto.getBoolean(mapa.get("check_"+i)+""))
				if (!UtilidadCadena.noEsVacio(estados))
					estados+=mapa.get("codigo_"+i)+"";
				else
					estados+=","+mapa.get("codigo_"+i);
		}
		
		return estados;
	}
	
	/*--------------------------------------------------------------------------------------------------------
	 *         							FIN METODOS DE OCUPACION DIARIA DE CAMAS
	 ---------------------------------------------------------------------------------------------------------*/
	
}