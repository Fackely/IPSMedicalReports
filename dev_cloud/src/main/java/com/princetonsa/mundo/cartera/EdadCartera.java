package com.princetonsa.mundo.cartera;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.cartera.EdadCarteraDao;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * 
 * @author wilson
 *
 */
public class EdadCartera 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static EdadCarteraDao edadDao;
	
	 /**
     * Para manejar la fecha inicial del rango de Reporte del estado de la Cartera.
     */
    private String fechaCorte;
    
    
    private String tipoReporte;
    
    /**
     * Tipo Contrato para el Campo Convenio
     */
    private int convenioNormal;
    
    /**
     * 
     */
    private int tipoConvenio;
    
    /**
     * 
     */
    private String esFactura="";
   
    /**
     * 
     */
    private boolean manejaConversionMoneda;
    
    /**
     * 
     */
    private HashMap tiposMonedaTagMap;
    
    /**
     * 
     */
    private int index;
    
    /**
     * 
     */
    private double empresaInstitucion;
    
    /**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			edadDao = myFactory.getEdadCarteraDao();
			wasInited = (edadDao != null);
		}
		return wasInited;
	}
    
    /**
     * reset de la forma
     *
     */
    public void reset()
    {
        this.tipoReporte="";
        this.fechaCorte="";
        this.convenioNormal=ConstantesBD.codigoNuncaValido;
        this.tipoConvenio=ConstantesBD.codigoNuncaValido;
        this.esFactura=ConstantesBD.acronimoNo;
        this.manejaConversionMoneda=false;
        this.tiposMonedaTagMap=new HashMap();
        this.index=ConstantesBD.codigoNuncaValido;
        this.empresaInstitucion=ConstantesBD.codigoNuncaValidoDoubleNegativo;
    }

    /**
     * 
     * @param mundo
     * @param oldQuery
     * @return
     */
    public static String armarConsultaReporte(EdadCartera mundo, String oldQuery) 
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEdadCarteraDao().armarConsultaReporte(mundo, oldQuery);
	}
    
    /**
     * 
     * @param query
     * @return
     */
    public static HashMap ejecutarConsultaReporte(Connection con, String query) 
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEdadCarteraDao().ejecutarConsultaReporte(con, query);
	}
    
    /**
     * A continuacion se describen los metodos que organizan los datos para ser imprimidos
     * en el archivo de texto; se generanron funciones independientes para cada uno de los
     * tipos de reportes y para cada uno de los nombre de reportes; gracias a que cada uno
     * maneja una estructura de generacion de archivo diferente 
     */
    
    //********************************************** INICIO EDAD CARTERA POR FECHA FACTURA ******************************************
    
    /**
     * Metodo que carga los datos para generar el archivo plano segun el tipo de reporte "Resumido" por edad fecha factura
     * @param mapa
     * @param nombreReporte
     * @param tipoReporte
     * @param fechaReporte
     * @param encabezado
     * @return
     */
    public static StringBuffer cargarMapaResumidoFactura(HashMap<String, Object> mapa, String nombreReporte, String tipoReporte, String fechaReporte, String encabezado)
	{
		StringBuffer datos = new StringBuffer();
		double totalCarteraVencida, totalColumn0 = 0, totalColumn1_30 = 0, totalColumn31_60 = 0, totalColumn61_90 = 0, totalColumn91_120 = 0, totalColumn121_150 = 0, totalColumn151_180 = 0, totalColumn181_360 = 0, totalColumn360 = 0, totalCartera = 0;
		double totalGeneralColumn0 = 0, totalGeneralColumn1_30 = 0, totalGeneralColumn31_60 = 0, totalGeneralColumn61_90 = 0, totalGeneralColumn91_120 = 0, totalGeneralColumn121_150 = 0, totalGeneralColumn151_180 = 0, totalGeneralColumn181_360 = 0, totalGeneralColumn360 = 0, totalGeneralCartera = 0;
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("TIPO REPORTE: "+tipoReporte+"\n");
		datos.append("FECHA CORTE: "+fechaReporte+"\n");
		datos.append(encabezado+"\n");
		
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			//Se suma el valor de las filas para mostrarlo en totalCarteraVencida
			totalCarteraVencida = Utilidades.convertirADouble(mapa.get("column_0_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			//Si es el primer registro se muestra el convenio y se realiza el salto de linea para mostrar los otros datos del primer registro
			if(i == 0)
			{
				datos.append(mapa.get("nombretipoconvenio_"+i)+"\n");
				datos.append(mapa.get("nombreconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_0_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(totalCarteraVencida)+"\n");
				//Suma las filas la primera que entro
				totalColumn0 = Utilidades.convertirADouble(mapa.get("column_0_"+i)+"");
				totalColumn1_30 = Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"");
				totalColumn31_60 = Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"");
			 	totalColumn61_90 = Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"");
			 	totalColumn91_120 = Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"");
			 	totalColumn121_150 = Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"");
			 	totalColumn151_180 = Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"");
			 	totalColumn181_360 = Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"");
			 	totalColumn360 = Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			 	totalCartera = totalCarteraVencida;
			 	if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
			 		datos.append("Total "+mapa.get("nombretipoconvenio_"+(i))+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
			 		//Como solo llega un registro el totalColumn es igual al totalGeneral y no se hace necesario sumarlo
			 		datos.append("Total General, "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
			 	}
			}
			//Si el nombretipoconvenio viene igual se muestra los otros datos sin el nombretipoconvenio
			else if ((mapa.get("nombretipoconvenio_"+i)+"").equals(mapa.get("nombretipoconvenio_"+(i-1))+""))
			{
				datos.append(mapa.get("nombreconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_0_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(totalCarteraVencida)+"\n");
				//Suma las filas para los otros registros del mismo nombretipoconvenio
				totalColumn0 = totalColumn0 + Utilidades.convertirADouble(mapa.get("column_0_"+i)+"");
				totalColumn1_30 = totalColumn1_30 + Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"");
				totalColumn31_60 = totalColumn31_60 + Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"");
			 	totalColumn61_90 = totalColumn61_90 + Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"");
			 	totalColumn91_120 = totalColumn91_120 + Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"");
			 	totalColumn121_150 = totalColumn121_150 + Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"");
			 	totalColumn151_180 = totalColumn151_180 + Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"");
			 	totalColumn181_360 = totalColumn181_360 + Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"");
			 	totalColumn360 = totalColumn360 + Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			 	totalCartera = totalCartera + totalCarteraVencida;
			 	//Si es la ultima fila con mas de un registro por nombretipoconvenio se muestra el total del ultimo nombretipoconvenio y se realiza el totalizado general
			 	if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
			 		datos.append("Total "+mapa.get("nombretipoconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
			 		//Sumo el total de la ultima columna para tomar el valor general
				 	totalGeneralColumn0 = totalGeneralColumn0 + totalColumn0;
				 	totalGeneralColumn1_30 = totalGeneralColumn1_30 + totalColumn1_30;
				 	totalGeneralColumn31_60 = totalGeneralColumn31_60 + totalColumn31_60;
				 	totalGeneralColumn61_90 = totalGeneralColumn61_90 + totalColumn61_90;
				 	totalGeneralColumn91_120 = totalGeneralColumn91_120 + totalColumn91_120;
				 	totalGeneralColumn121_150 = totalGeneralColumn121_150 + totalColumn121_150;
				 	totalGeneralColumn151_180 = totalGeneralColumn151_180 + totalColumn151_180;
				 	totalGeneralColumn181_360 = totalGeneralColumn181_360 + totalColumn181_360;
				 	totalGeneralColumn360 = totalGeneralColumn360 + totalColumn360;
				 	totalGeneralCartera = totalGeneralCartera + totalCartera;
				 	datos.append("Total General, "+UtilidadTexto.formatearExponenciales(totalGeneralColumn0)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralCartera)+"\n");
			 	}
			}
			//Si viene un nuevo nombretipoconvenio
			else
			{
				//Sumo el total general antes de imprimir el registro total por nombretipoconvenio
			 	totalGeneralColumn0 = totalGeneralColumn0 + totalColumn0;
			 	totalGeneralColumn1_30 = totalGeneralColumn1_30 + totalColumn1_30;
			 	totalGeneralColumn31_60 = totalGeneralColumn31_60 + totalColumn31_60;
			 	totalGeneralColumn61_90 = totalGeneralColumn61_90 + totalColumn61_90;
			 	totalGeneralColumn91_120 = totalGeneralColumn91_120 + totalColumn91_120;
			 	totalGeneralColumn121_150 = totalGeneralColumn121_150 + totalColumn121_150;
			 	totalGeneralColumn151_180 = totalGeneralColumn151_180 + totalColumn151_180;
			 	totalGeneralColumn181_360 = totalGeneralColumn181_360 + totalColumn181_360;
			 	totalGeneralColumn360 = totalGeneralColumn360 + totalColumn360;
			 	totalGeneralCartera = totalGeneralCartera + totalCartera;
			 	//Imprimo el Total por nombretipoconvenio antes de imprimir el otro registro de nombretipoconvenio
			 	datos.append("Total "+mapa.get("nombretipoconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
				datos.append(mapa.get("nombretipoconvenio_"+i)+"\n");
				datos.append(mapa.get("nombreconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_0_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(totalCarteraVencida)+"\n");
				//Voy sumando el valor total por nombretipoconvenio
				totalColumn0 = Utilidades.convertirADouble(mapa.get("column_0_"+i)+"");
				totalColumn1_30 = Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"");
				totalColumn31_60 = Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"");
			 	totalColumn61_90 = Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"");
			 	totalColumn91_120 = Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"");
			 	totalColumn121_150 = Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"");
			 	totalColumn151_180 = Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"");
			 	totalColumn181_360 = Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"");
			 	totalColumn360 = Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			 	//Si es la ultima fila con solo un registro por nombretipoconvenio se muestra el total del ultimo nombretipoconvenio y se realiza el totalizado general
			 	if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
			 		datos.append("Total "+mapa.get("nombretipoconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
			 		//Sumo el total de la ultima columna para tomar el valor general
				 	totalGeneralColumn0 = totalGeneralColumn0 + totalColumn0;
				 	totalGeneralColumn1_30 = totalGeneralColumn1_30 + totalColumn1_30;
				 	totalGeneralColumn31_60 = totalGeneralColumn31_60 + totalColumn31_60;
				 	totalGeneralColumn61_90 = totalGeneralColumn61_90 + totalColumn61_90;
				 	totalGeneralColumn91_120 = totalGeneralColumn91_120 + totalColumn91_120;
				 	totalGeneralColumn121_150 = totalGeneralColumn121_150 + totalColumn121_150;
				 	totalGeneralColumn151_180 = totalGeneralColumn151_180 + totalColumn151_180;
				 	totalGeneralColumn181_360 = totalGeneralColumn181_360 + totalColumn181_360;
				 	totalGeneralColumn360 = totalGeneralColumn360 + totalColumn360;
				 	totalGeneralCartera = totalGeneralCartera + totalCartera;
			 		datos.append("Total General, "+UtilidadTexto.formatearExponenciales(totalGeneralColumn0)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralCartera)+"\n");
			 	}
			}
		}
		return datos;
	}
	
	/**
	 * Metodo que carga los datos para generar el archivo plano segun el tipo de reporte "Detallado por Factura" por edad fecha de factura
	 * @param mapa
	 * @param nombreReporte
	 * @param tipoReporte
	 * @param fechaReporte
	 * @param encabezado
	 * @return
	 */
	public static StringBuffer cargarMapaDetalladoFactura(HashMap<String, Object> mapa, String nombreReporte, String tipoReporte, String fechaReporte, String encabezado)
	{
		StringBuffer datos = new StringBuffer();
		double totalCarteraVencida, totalColumn0 = 0, totalColumn1_30 = 0, totalColumn31_60 = 0, totalColumn61_90 = 0, totalColumn91_120 = 0, totalColumn121_150 = 0, totalColumn151_180 = 0, totalColumn181_360 = 0, totalColumn360 = 0, totalCartera = 0;
		double totalGeneralColumn0 = 0, totalGeneralColumn1_30 = 0, totalGeneralColumn31_60 = 0, totalGeneralColumn61_90 = 0, totalGeneralColumn91_120 = 0, totalGeneralColumn121_150 = 0, totalGeneralColumn151_180 = 0, totalGeneralColumn181_360 = 0, totalGeneralColumn360 = 0, totalGeneralCartera = 0;
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("TIPO REPORTE: "+tipoReporte+"\n");
		datos.append("FECHA CORTE: "+fechaReporte+"\n");
		datos.append(encabezado+"\n");
		
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			//Se suma el valor de las filas para mostrarlo en totalCarteraVencida
			totalCarteraVencida = Utilidades.convertirADouble(mapa.get("column_0_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"") + Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			//Si es el primer registro se muestra el convenio y se realiza el salto de linea para mostrar los otros datos del primer registro
			if(i == 0)
			{
				datos.append(mapa.get("nombretipoconvenio_"+i)+"\n");
				datos.append(mapa.get("nombreconvenionumerocxc_"+i)+"\n");
				datos.append(mapa.get("factura_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_0_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(totalCarteraVencida)+"\n");
				//Suma las filas la primera vez que entro
				totalColumn0 = Utilidades.convertirADouble(mapa.get("column_0_"+i)+"");
				totalColumn1_30 = Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"");
				totalColumn31_60 = Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"");
			 	totalColumn61_90 = Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"");
			 	totalColumn91_120 = Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"");
			 	totalColumn121_150 = Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"");
			 	totalColumn151_180 = Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"");
			 	totalColumn181_360 = Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"");
			 	totalColumn360 = Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			 	totalCartera = totalCarteraVencida;
			 	if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
			 		datos.append("Total "+mapa.get("nombretipoconvenio_"+(i))+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
			 		//Como solo llega un registro el totalColumn es igual al totalGeneral y no se hace necesario sumarlo
			 		datos.append("Total General, "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
			 	}
			}
			//Si el nombretipoconvenio viene igual y la cuenta de cobro es igual se muestra los otros datos sin el nombretipoconvenio, y sin la cuenta de cobro
			else if ((mapa.get("nombretipoconvenio_"+i)+"").equals(mapa.get("nombretipoconvenio_"+(i-1))+"") && (mapa.get("nombreconvenionumerocxc_"+i)+"").equals(mapa.get("nombreconvenionumerocxc_"+(i-1))+""))
			{
				//Suma las filas para los otros registros del mismo nombretipoconvenio
				totalColumn0 = totalColumn0 + Utilidades.convertirADouble(mapa.get("column_0_"+i)+"");
				totalColumn1_30 = totalColumn1_30 + Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"");
				totalColumn31_60 = totalColumn31_60 + Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"");
			 	totalColumn61_90 = totalColumn61_90 + Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"");
			 	totalColumn91_120 = totalColumn91_120 + Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"");
			 	totalColumn121_150 = totalColumn121_150 + Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"");
			 	totalColumn151_180 = totalColumn151_180 + Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"");
			 	totalColumn181_360 = totalColumn181_360 + Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"");
			 	totalColumn360 = totalColumn360 + Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			 	totalCartera = totalCartera + totalCarteraVencida;
				datos.append(mapa.get("factura_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_0_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(totalCarteraVencida)+"\n");
				//Si es la ultima fila con mas de un registro por nombretipoconvenio y con mas de un nombreconvenionumerocxc se muestra el total del ultimo nombretipoconvenio y se realiza el totalizado general
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
			 		datos.append("Total "+mapa.get("nombretipoconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
			 		//Sumo el total de la ultima columna para tomar el valor general
				 	totalGeneralColumn0 = totalGeneralColumn0 + totalColumn0;
				 	totalGeneralColumn1_30 = totalGeneralColumn1_30 + totalColumn1_30;
				 	totalGeneralColumn31_60 = totalGeneralColumn31_60 + totalColumn31_60;
				 	totalGeneralColumn61_90 = totalGeneralColumn61_90 + totalColumn61_90;
				 	totalGeneralColumn91_120 = totalGeneralColumn91_120 + totalColumn91_120;
				 	totalGeneralColumn121_150 = totalGeneralColumn121_150 + totalColumn121_150;
				 	totalGeneralColumn151_180 = totalGeneralColumn151_180 + totalColumn151_180;
				 	totalGeneralColumn181_360 = totalGeneralColumn181_360 + totalColumn181_360;
				 	totalGeneralColumn360 = totalGeneralColumn360 + totalColumn360;
				 	totalGeneralCartera = totalGeneralCartera + totalCartera;
				 	datos.append("Total General, "+UtilidadTexto.formatearExponenciales(totalGeneralColumn0)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralCartera)+"\n");
			 	}
			}
			//Si el nombretipoconvenio viene igual y la cuenta de cobro es diferente se muestra los otros datos sin el nombretipoconvenio, pero con la cuenta de cobro
			else if((mapa.get("nombretipoconvenio_"+i)+"").equals(mapa.get("nombretipoconvenio_"+(i-1))+"") && !(mapa.get("nombreconvenionumerocxc_"+i)+"").equals(mapa.get("nombreconvenionumerocxc_"+(i-1))+""))
			{
				//Suma las filas para los otros registros del mismo nombretipoconvenio
				totalColumn0 = totalColumn0 + Utilidades.convertirADouble(mapa.get("column_0_"+i)+"");
				totalColumn1_30 = totalColumn1_30 + Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"");
				totalColumn31_60 = totalColumn31_60 + Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"");
			 	totalColumn61_90 = totalColumn61_90 + Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"");
			 	totalColumn91_120 = totalColumn91_120 + Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"");
			 	totalColumn121_150 = totalColumn121_150 + Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"");
			 	totalColumn151_180 = totalColumn151_180 + Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"");
			 	totalColumn181_360 = totalColumn181_360 + Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"");
			 	totalColumn360 = totalColumn360 + Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			 	totalCartera = totalCartera + totalCarteraVencida;
				datos.append(mapa.get("nombreconvenionumerocxc_"+i)+"\n");
				datos.append(mapa.get("factura_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_0_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(totalCarteraVencida)+"\n");
				//Si es la ultima fila con mas de un registro por nombretipoconvenio y sin mas de un nombreconvenionumerocxc se muestra el total del ultimo nombretipoconvenio y se realiza el totalizado general
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
			 		datos.append("Total "+mapa.get("nombretipoconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
			 		//Sumo el total de la ultima columna para tomar el valor general
				 	totalGeneralColumn0 = totalGeneralColumn0 + totalColumn0;
				 	totalGeneralColumn1_30 = totalGeneralColumn1_30 + totalColumn1_30;
				 	totalGeneralColumn31_60 = totalGeneralColumn31_60 + totalColumn31_60;
				 	totalGeneralColumn61_90 = totalGeneralColumn61_90 + totalColumn61_90;
				 	totalGeneralColumn91_120 = totalGeneralColumn91_120 + totalColumn91_120;
				 	totalGeneralColumn121_150 = totalGeneralColumn121_150 + totalColumn121_150;
				 	totalGeneralColumn151_180 = totalGeneralColumn151_180 + totalColumn151_180;
				 	totalGeneralColumn181_360 = totalGeneralColumn181_360 + totalColumn181_360;
				 	totalGeneralColumn360 = totalGeneralColumn360 + totalColumn360;
				 	totalGeneralCartera = totalGeneralCartera + totalCartera;
				 	datos.append("Total General, "+UtilidadTexto.formatearExponenciales(totalGeneralColumn0)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralCartera)+"\n");
			 	}
			}
			//Si el nombretipoconvenio es diferente se muestra un nuevo registro con el nombretipoconvenio y todos los datos
			else
			{
				//Sumo el total general antes de imprimir el registro total por nombretipoconvenio
			 	totalGeneralColumn0 = totalGeneralColumn0 + totalColumn0;
			 	totalGeneralColumn1_30 = totalGeneralColumn1_30 + totalColumn1_30;
			 	totalGeneralColumn31_60 = totalGeneralColumn31_60 + totalColumn31_60;
			 	totalGeneralColumn61_90 = totalGeneralColumn61_90 + totalColumn61_90;
			 	totalGeneralColumn91_120 = totalGeneralColumn91_120 + totalColumn91_120;
			 	totalGeneralColumn121_150 = totalGeneralColumn121_150 + totalColumn121_150;
			 	totalGeneralColumn151_180 = totalGeneralColumn151_180 + totalColumn151_180;
			 	totalGeneralColumn181_360 = totalGeneralColumn181_360 + totalColumn181_360;
			 	totalGeneralColumn360 = totalGeneralColumn360 + totalColumn360;
			 	totalGeneralCartera = totalGeneralCartera + totalCartera;
				//Imprimo el Total por nombretipoconvenio antes de imprimir el otro registro de nombretipoconvenio
			 	datos.append("Total "+mapa.get("nombretipoconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
				datos.append(mapa.get("nombretipoconvenio_"+i)+"\n");
				datos.append(mapa.get("nombreconvenionumerocxc_"+i)+"\n");
				datos.append(mapa.get("factura_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_0_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(totalCarteraVencida)+"\n");
				//Voy sumando el valor total por nombretipoconvenio
				totalColumn0 = Utilidades.convertirADouble(mapa.get("column_0_"+i)+"");
				totalColumn1_30 = Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"");
				totalColumn31_60 = Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"");
			 	totalColumn61_90 = Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"");
			 	totalColumn91_120 = Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"");
			 	totalColumn121_150 = Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"");
			 	totalColumn151_180 = Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"");
			 	totalColumn181_360 = Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"");
			 	totalColumn360 = Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			 	//Si es la ultima fila con solo un registro por nombretipoconvenio se muestra el total del ultimo nombretipoconvenio y se realiza el totalizado general
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
			 		datos.append("Total "+mapa.get("nombretipoconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
			 		//Sumo el total de la ultima columna para tomar el valor general
				 	totalGeneralColumn0 = totalGeneralColumn0 + totalColumn0;
				 	totalGeneralColumn1_30 = totalGeneralColumn1_30 + totalColumn1_30;
				 	totalGeneralColumn31_60 = totalGeneralColumn31_60 + totalColumn31_60;
				 	totalGeneralColumn61_90 = totalGeneralColumn61_90 + totalColumn61_90;
				 	totalGeneralColumn91_120 = totalGeneralColumn91_120 + totalColumn91_120;
				 	totalGeneralColumn121_150 = totalGeneralColumn121_150 + totalColumn121_150;
				 	totalGeneralColumn151_180 = totalGeneralColumn151_180 + totalColumn151_180;
				 	totalGeneralColumn181_360 = totalGeneralColumn181_360 + totalColumn181_360;
				 	totalGeneralColumn360 = totalGeneralColumn360 + totalColumn360;
				 	totalGeneralCartera = totalGeneralCartera + totalCartera;
				 	datos.append("Total General, "+UtilidadTexto.formatearExponenciales(totalGeneralColumn0)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralCartera)+"\n");
			 	}
			}
		}
		
		return datos;
	}
	
	//********************************************** FIN EDAD CARTERA POR FECHA FACTURA ******************************************
	
	
	//********************************************** INICIO EDAD CARTERA POR FECHA RADICACION ******************************************
	
	/**
	 * Metodo que carga los datos para generar el archivo plano segun el tipo de reporte "Resumido" por edad fecha radicacion 
	 * @param mapa
	 * @param nombreReporte
	 * @param tipoReporte
	 * @param fechaReporte
	 * @param encabezado
	 * @return
	 */
	public static StringBuffer cargarMapaResumidoRadicacion(HashMap<String, Object> mapa, String nombreReporte, String tipoReporte, String fechaReporte, String encabezado)
	{
		StringBuffer datos = new StringBuffer();
		double totalPorRadicar = 0, totalColumn0 = 0, totalColumn1_30 = 0, totalColumn31_60 = 0, totalColumn61_90 = 0, totalColumn91_120 = 0, totalColumn121_150 = 0, totalColumn151_180 = 0, totalColumn181_360 = 0, totalColumn360 = 0, totalRadicado = 0, totalCartera = 0;
		double totalGeneralPorRadicar = 0, totalGeneralColumn0 = 0, totalGeneralColumn1_30 = 0, totalGeneralColumn31_60 = 0, totalGeneralColumn61_90 = 0, totalGeneralColumn91_120 = 0, totalGeneralColumn121_150 = 0, totalGeneralColumn151_180 = 0, totalGeneralColumn181_360 = 0, totalGeneralColumn360 = 0, totalGeneralRadicado = 0,totalGeneralCartera = 0;
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("TIPO REPORTE: "+tipoReporte+"\n");
		datos.append("FECHA CORTE: "+fechaReporte+"\n");
		datos.append(encabezado+"\n");
		
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			//Si es el primer registro se muestra el convenio y se realiza el salto de linea para mostrar los otros datos del primer registro
			if(i == 0)
			{
				datos.append(mapa.get("nombretipoconvenio_"+i)+"\n");
				datos.append(mapa.get("nombreconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("porradicar_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_0_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalradicado_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalcarteravencida_"+i)+""))+"\n");
				//Suma las filas la primera que entro
				totalPorRadicar = Utilidades.convertirADouble(mapa.get("porradicar_"+i)+"");
				totalColumn0 = Utilidades.convertirADouble(mapa.get("column_0_"+i)+"");
				totalColumn1_30 = Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"");
				totalColumn31_60 = Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"");
			 	totalColumn61_90 = Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"");
			 	totalColumn91_120 = Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"");
			 	totalColumn121_150 = Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"");
			 	totalColumn151_180 = Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"");
			 	totalColumn181_360 = Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"");
			 	totalColumn360 = Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			 	totalRadicado = Utilidades.convertirADouble(mapa.get("totalradicado_"+i)+"");
			 	totalCartera = Utilidades.convertirADouble(mapa.get("totalcarteravencida_"+i)+"");
			 	if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
			 		datos.append("Total "+mapa.get("nombretipoconvenio_"+(i))+", "+UtilidadTexto.formatearExponenciales(totalPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalRadicado)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
			 		//Como solo llega un registro el totalColumn es igual al totalGeneral y no se hace necesario sumarlo
			 		datos.append("Total General, "+UtilidadTexto.formatearExponenciales(totalPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalRadicado)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
			 	}
			}
			//Si el nombretipoconvenio viene igual se muestra los otros datos sin el nombretipoconvenio
			else if ((mapa.get("nombretipoconvenio_"+i)+"").equals(mapa.get("nombretipoconvenio_"+(i-1))+""))
			{
				datos.append(mapa.get("nombreconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("porradicar_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_0_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalradicado_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalcarteravencida_"+i)+""))+"\n");
				//Suma las filas para los otros registros del mismo nombretipoconvenio
				totalPorRadicar = totalPorRadicar + Utilidades.convertirADouble(mapa.get("porradicar_"+i)+"");
				totalColumn0 = totalColumn0 + Utilidades.convertirADouble(mapa.get("column_0_"+i)+"");
				totalColumn1_30 = totalColumn1_30 + Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"");
				totalColumn31_60 = totalColumn31_60 + Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"");
			 	totalColumn61_90 = totalColumn61_90 + Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"");
			 	totalColumn91_120 = totalColumn91_120 + Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"");
			 	totalColumn121_150 = totalColumn121_150 + Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"");
			 	totalColumn151_180 = totalColumn151_180 + Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"");
			 	totalColumn181_360 = totalColumn181_360 + Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"");
			 	totalColumn360 = totalColumn360 + Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			 	totalRadicado = totalRadicado + Utilidades.convertirADouble(mapa.get("totalradicado_"+i)+"");
			 	totalCartera = totalCartera + Utilidades.convertirADouble(mapa.get("totalcarteravencida_"+i)+"");
			 	if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
			 		datos.append("Total "+mapa.get("nombretipoconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalRadicado)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
			 		//Sumo el total de la ultima columna para tomar el valor general
			 		totalGeneralPorRadicar = totalGeneralPorRadicar + totalPorRadicar;
					totalGeneralColumn0 = totalGeneralColumn0 + totalColumn0;
				 	totalGeneralColumn1_30 = totalGeneralColumn1_30 + totalColumn1_30;
				 	totalGeneralColumn31_60 = totalGeneralColumn31_60 + totalColumn31_60;
				 	totalGeneralColumn61_90 = totalGeneralColumn61_90 + totalColumn61_90;
				 	totalGeneralColumn91_120 = totalGeneralColumn91_120 + totalColumn91_120;
				 	totalGeneralColumn121_150 = totalGeneralColumn121_150 + totalColumn121_150;
				 	totalGeneralColumn151_180 = totalGeneralColumn151_180 + totalColumn151_180;
				 	totalGeneralColumn181_360 = totalGeneralColumn181_360 + totalColumn181_360;
				 	totalGeneralColumn360 = totalGeneralColumn360 + totalColumn360;
				 	totalGeneralRadicado = totalGeneralRadicado + totalRadicado;
				 	totalGeneralCartera = totalGeneralCartera + totalCartera;
			 		datos.append("Total General, "+UtilidadTexto.formatearExponenciales(totalGeneralPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn0)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralRadicado)+", "+UtilidadTexto.formatearExponenciales(totalGeneralCartera)+"\n");
			 	}
			}
			//Si viene un nuevo nombretipoconvenio
			else
			{
				//Sumo el total general antes de imprimir el registro total por nombretipoconvenio
			 	totalGeneralPorRadicar = totalGeneralPorRadicar + totalPorRadicar;
				totalGeneralColumn0 = totalGeneralColumn0 + totalColumn0;
			 	totalGeneralColumn1_30 = totalGeneralColumn1_30 + totalColumn1_30;
			 	totalGeneralColumn31_60 = totalGeneralColumn31_60 + totalColumn31_60;
			 	totalGeneralColumn61_90 = totalGeneralColumn61_90 + totalColumn61_90;
			 	totalGeneralColumn91_120 = totalGeneralColumn91_120 + totalColumn91_120;
			 	totalGeneralColumn121_150 = totalGeneralColumn121_150 + totalColumn121_150;
			 	totalGeneralColumn151_180 = totalGeneralColumn151_180 + totalColumn151_180;
			 	totalGeneralColumn181_360 = totalGeneralColumn181_360 + totalColumn181_360;
			 	totalGeneralColumn360 = totalGeneralColumn360 + totalColumn360;
			 	totalGeneralRadicado = totalGeneralRadicado + totalRadicado;
			 	totalGeneralCartera = totalGeneralCartera + totalCartera;
				//Imprimo el Total por nombretipoconvenio antes de imprimir el otro registro de nombretipoconvenio
				datos.append("Total "+mapa.get("nombretipoconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalRadicado)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
				datos.append(mapa.get("nombretipoconvenio_"+i)+"\n");
				datos.append(mapa.get("nombreconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("porradicar_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_0_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalradicado_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalcarteravencida_"+i)+""))+"\n");
				//Voy sumando el valor total por nombretipoconvenio
				totalPorRadicar = Utilidades.convertirADouble(mapa.get("porradicar_"+i)+"");
				totalColumn0 = Utilidades.convertirADouble(mapa.get("column_0_"+i)+"");
				totalColumn1_30 = Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"");
				totalColumn31_60 = Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"");
			 	totalColumn61_90 = Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"");
			 	totalColumn91_120 = Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"");
			 	totalColumn121_150 = Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"");
			 	totalColumn151_180 = Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"");
			 	totalColumn181_360 = Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"");
			 	totalColumn360 = Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			 	totalRadicado = Utilidades.convertirADouble(mapa.get("totalradicado_"+i)+"");
			 	totalCartera = Utilidades.convertirADouble(mapa.get("totalcarteravencida_"+i)+"");
			 	if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
			 		datos.append("Total "+mapa.get("nombretipoconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalRadicado)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
			 		//Sumo el total de la ultima columna para tomar el valor general
			 		totalGeneralPorRadicar = totalGeneralPorRadicar + totalPorRadicar;
					totalGeneralColumn0 = totalGeneralColumn0 + totalColumn0;
				 	totalGeneralColumn1_30 = totalGeneralColumn1_30 + totalColumn1_30;
				 	totalGeneralColumn31_60 = totalGeneralColumn31_60 + totalColumn31_60;
				 	totalGeneralColumn61_90 = totalGeneralColumn61_90 + totalColumn61_90;
				 	totalGeneralColumn91_120 = totalGeneralColumn91_120 + totalColumn91_120;
				 	totalGeneralColumn121_150 = totalGeneralColumn121_150 + totalColumn121_150;
				 	totalGeneralColumn151_180 = totalGeneralColumn151_180 + totalColumn151_180;
				 	totalGeneralColumn181_360 = totalGeneralColumn181_360 + totalColumn181_360;
				 	totalGeneralColumn360 = totalGeneralColumn360 + totalColumn360;
				 	totalGeneralRadicado = totalGeneralRadicado + totalRadicado;
				 	totalGeneralCartera = totalGeneralCartera + totalCartera;
			 		datos.append("Total General, "+UtilidadTexto.formatearExponenciales(totalGeneralPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn0)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralRadicado)+", "+UtilidadTexto.formatearExponenciales(totalGeneralCartera)+"\n");
			 	}
			}
		}
		return datos;
	}
	
	/**
	 * Metodo que carga los datos para generar el archivo plano segun el tipo de reporte
	 * "Detallado por Factura" y "Detallado por Cxc" por edad fecha de radicacion.
	 * Como el "Detallado por Factura" y el "Detallado por Cxc" solo cambia en el nombre
	 * de las llaves del convenio y de la factura y con la intencion de no modificar la
	 * consulta en el birt y de no generar una nueva funcion se manda como parametros 
	 * dichas llaves desde el action y se imprime la que se necesite
	 * @param mapa
	 * @param nombreReporte
	 * @param tipoReporte
	 * @param fechaReporte
	 * @param encabezado
	 * @param indiceConvenio: Trae el indice del convenio. En caso "Detallado por Factura"
	 * seria (nombreconveniocuentacobro_). En caso "Detallado por Cxc" seria (nombreconvenio_)
	 * @param indiceFactura: Trae el indice de la factura o cuenta de cobro. En caso 
	 * "Detallado por Factura" seria (factura_). En caso "Detallado por Cxc" seria (numerocuentacobro_)
	 * @return
	 */
	public static StringBuffer cargarMapaDetalladoFacturaCxcRadicacion(HashMap<String, Object> mapa, String nombreReporte, String tipoReporte, String fechaReporte, String encabezado, String indiceConvenio, String indiceFactura)
	{
		StringBuffer datos = new StringBuffer();
		double totalPorRadicar = 0, totalColumn0 = 0, totalColumn1_30 = 0, totalColumn31_60 = 0, totalColumn61_90 = 0, totalColumn91_120 = 0, totalColumn121_150 = 0, totalColumn151_180 = 0, totalColumn181_360 = 0, totalColumn360 = 0, totalRadicado = 0, totalCartera = 0;
		double totalGeneralPorRadicar = 0, totalGeneralColumn0 = 0, totalGeneralColumn1_30 = 0, totalGeneralColumn31_60 = 0, totalGeneralColumn61_90 = 0, totalGeneralColumn91_120 = 0, totalGeneralColumn121_150 = 0, totalGeneralColumn151_180 = 0, totalGeneralColumn181_360 = 0, totalGeneralColumn360 = 0, totalGeneralRadicado = 0,totalGeneralCartera = 0;
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("TIPO REPORTE: "+tipoReporte+"\n");
		datos.append("FECHA CORTE: "+fechaReporte+"\n");
		datos.append(encabezado+"\n");
		
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			//Si es el primer registro se muestra el convenio y se realiza el salto de linea para mostrar los otros datos del primer registro
			if(i == 0)
			{
				datos.append(mapa.get("nombretipoconvenio_"+i)+"\n");
				datos.append(mapa.get(indiceConvenio+i)+"\n");
				datos.append(mapa.get(indiceFactura+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("porradicar_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_0_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalradicado_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalcarteravencida_"+i)+""))+"\n");
				//Suma las filas la primera vez que entro
				totalPorRadicar = Utilidades.convertirADouble(mapa.get("porradicar_"+i)+"");
				totalColumn0 = Utilidades.convertirADouble(mapa.get("column_0_"+i)+"");
				totalColumn1_30 = Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"");
				totalColumn31_60 = Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"");
			 	totalColumn61_90 = Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"");
			 	totalColumn91_120 = Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"");
			 	totalColumn121_150 = Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"");
			 	totalColumn151_180 = Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"");
			 	totalColumn181_360 = Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"");
			 	totalColumn360 = Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			 	totalRadicado = Utilidades.convertirADouble(mapa.get("totalradicado_"+i)+"");
			 	totalCartera = Utilidades.convertirADouble(mapa.get("totalcarteravencida_"+i)+"");
			 	if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
			 		datos.append("Total "+mapa.get("nombretipoconvenio_"+(i))+", "+UtilidadTexto.formatearExponenciales(totalPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalRadicado)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
					//Como solo llega un registro el totalColumn es igual al totalGeneral y no se hace necesario sumarlo
			 		datos.append("Total General, "+UtilidadTexto.formatearExponenciales(totalPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalRadicado)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
			 	}
			}
			//Si el nombretipoconvenio viene igual y la cuenta de cobro es igual se muestra los otros datos sin el nombretipoconvenio, y sin la cuenta de cobro
			else if ((mapa.get("nombretipoconvenio_"+i)+"").equals(mapa.get("nombretipoconvenio_"+(i-1))+"") && (mapa.get(indiceConvenio+i)+"").equals(mapa.get(indiceConvenio+(i-1))+""))
			{
				//Suma las filas para los otros registros del mismo nombretipoconvenio
				totalPorRadicar = totalPorRadicar + Utilidades.convertirADouble(mapa.get("porradicar_"+i)+"");
				totalColumn0 = totalColumn0 + Utilidades.convertirADouble(mapa.get("column_0_"+i)+"");
				totalColumn1_30 = totalColumn1_30 + Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"");
				totalColumn31_60 = totalColumn31_60 + Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"");
			 	totalColumn61_90 = totalColumn61_90 + Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"");
			 	totalColumn91_120 = totalColumn91_120 + Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"");
			 	totalColumn121_150 = totalColumn121_150 + Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"");
			 	totalColumn151_180 = totalColumn151_180 + Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"");
			 	totalColumn181_360 = totalColumn181_360 + Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"");
			 	totalColumn360 = totalColumn360 + Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			 	totalRadicado = totalRadicado + Utilidades.convertirADouble(mapa.get("totalradicado_"+i)+"");
			 	totalCartera = totalCartera + Utilidades.convertirADouble(mapa.get("totalcarteravencida_"+i)+"");
			 	datos.append(mapa.get(indiceFactura+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("porradicar_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_0_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalradicado_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalcarteravencida_"+i)+""))+"\n");
				//Si es la ultima fila con mas de un registro por nombretipoconvenio y con mas de un nombreconvenionumerocxc se muestra el total del ultimo nombretipoconvenio y se realiza el totalizado general
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Total "+mapa.get("nombretipoconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalRadicado)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
					//Sumo el total de la ultima columna para tomar el valor general
			 		totalGeneralPorRadicar = totalGeneralPorRadicar + totalPorRadicar;
					totalGeneralColumn0 = totalGeneralColumn0 + totalColumn0;
				 	totalGeneralColumn1_30 = totalGeneralColumn1_30 + totalColumn1_30;
				 	totalGeneralColumn31_60 = totalGeneralColumn31_60 + totalColumn31_60;
				 	totalGeneralColumn61_90 = totalGeneralColumn61_90 + totalColumn61_90;
				 	totalGeneralColumn91_120 = totalGeneralColumn91_120 + totalColumn91_120;
				 	totalGeneralColumn121_150 = totalGeneralColumn121_150 + totalColumn121_150;
				 	totalGeneralColumn151_180 = totalGeneralColumn151_180 + totalColumn151_180;
				 	totalGeneralColumn181_360 = totalGeneralColumn181_360 + totalColumn181_360;
				 	totalGeneralColumn360 = totalGeneralColumn360 + totalColumn360;
				 	totalGeneralRadicado = totalGeneralRadicado + totalRadicado;
				 	totalGeneralCartera = totalGeneralCartera + totalCartera;
			 		datos.append("Total General, "+UtilidadTexto.formatearExponenciales(totalGeneralPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn0)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralRadicado)+", "+UtilidadTexto.formatearExponenciales(totalGeneralCartera)+"\n");
			 	}
			}
			//Si el nombretipoconvenio viene igual y la cuenta de cobro es diferente se muestra los otros datos sin el nombretipoconvenio, pero con la cuenta de cobro
			else if((mapa.get("nombretipoconvenio_"+i)+"").equals(mapa.get("nombretipoconvenio_"+(i-1))+"") && !(mapa.get(indiceConvenio+i)+"").equals(mapa.get(indiceConvenio+(i-1))+""))
			{
				//Suma las filas para los otros registros del mismo nombretipoconvenio
				totalPorRadicar = totalPorRadicar + Utilidades.convertirADouble(mapa.get("porradicar_"+i)+"");
				totalColumn0 = totalColumn0 + Utilidades.convertirADouble(mapa.get("column_0_"+i)+"");
				totalColumn1_30 = totalColumn1_30 + Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"");
				totalColumn31_60 = totalColumn31_60 + Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"");
			 	totalColumn61_90 = totalColumn61_90 + Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"");
			 	totalColumn91_120 = totalColumn91_120 + Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"");
			 	totalColumn121_150 = totalColumn121_150 + Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"");
			 	totalColumn151_180 = totalColumn151_180 + Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"");
			 	totalColumn181_360 = totalColumn181_360 + Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"");
			 	totalColumn360 = totalColumn360 + Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			 	totalRadicado = totalRadicado + Utilidades.convertirADouble(mapa.get("totalradicado_"+i)+"");
			 	totalCartera = totalCartera + Utilidades.convertirADouble(mapa.get("totalcarteravencida_"+i)+"");
				datos.append(mapa.get(indiceConvenio+i)+"\n");
				datos.append(mapa.get(indiceFactura+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("porradicar_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_0_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalradicado_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalcarteravencida_"+i)+""))+"\n");
				//Si es la ultima fila con mas de un registro por nombretipoconvenio y sin mas de un nombreconvenionumerocxc se muestra el total del ultimo nombretipoconvenio y se realiza el totalizado general
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Total "+mapa.get("nombretipoconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalRadicado)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
					//Sumo el total de la ultima columna para tomar el valor general
			 		totalGeneralPorRadicar = totalGeneralPorRadicar + totalPorRadicar;
					totalGeneralColumn0 = totalGeneralColumn0 + totalColumn0;
				 	totalGeneralColumn1_30 = totalGeneralColumn1_30 + totalColumn1_30;
				 	totalGeneralColumn31_60 = totalGeneralColumn31_60 + totalColumn31_60;
				 	totalGeneralColumn61_90 = totalGeneralColumn61_90 + totalColumn61_90;
				 	totalGeneralColumn91_120 = totalGeneralColumn91_120 + totalColumn91_120;
				 	totalGeneralColumn121_150 = totalGeneralColumn121_150 + totalColumn121_150;
				 	totalGeneralColumn151_180 = totalGeneralColumn151_180 + totalColumn151_180;
				 	totalGeneralColumn181_360 = totalGeneralColumn181_360 + totalColumn181_360;
				 	totalGeneralColumn360 = totalGeneralColumn360 + totalColumn360;
				 	totalGeneralRadicado = totalGeneralRadicado + totalRadicado;
				 	totalGeneralCartera = totalGeneralCartera + totalCartera;
			 		datos.append("Total General, "+UtilidadTexto.formatearExponenciales(totalGeneralPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn0)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralRadicado)+", "+UtilidadTexto.formatearExponenciales(totalGeneralCartera)+"\n");
			 	}
			}
			//Si el nombretipoconvenio es diferente se muestra un nuevo registro con el nombretipoconvenio y todos los datos
			else
			{
				//Sumo el total general antes de imprimir el registro total por nombretipoconvenio
				totalGeneralPorRadicar = totalGeneralPorRadicar + totalPorRadicar;
				totalGeneralColumn0 = totalGeneralColumn0 + totalColumn0;
			 	totalGeneralColumn1_30 = totalGeneralColumn1_30 + totalColumn1_30;
			 	totalGeneralColumn31_60 = totalGeneralColumn31_60 + totalColumn31_60;
			 	totalGeneralColumn61_90 = totalGeneralColumn61_90 + totalColumn61_90;
			 	totalGeneralColumn91_120 = totalGeneralColumn91_120 + totalColumn91_120;
			 	totalGeneralColumn121_150 = totalGeneralColumn121_150 + totalColumn121_150;
			 	totalGeneralColumn151_180 = totalGeneralColumn151_180 + totalColumn151_180;
			 	totalGeneralColumn181_360 = totalGeneralColumn181_360 + totalColumn181_360;
			 	totalGeneralColumn360 = totalGeneralColumn360 + totalColumn360;
			 	totalGeneralRadicado = totalGeneralRadicado + totalRadicado;
			 	totalGeneralCartera = totalGeneralCartera + totalCartera;
				//Imprimo el Total por nombretipoconvenio antes de imprimir el otro registro de nombretipoconvenio
			 	datos.append("Total "+mapa.get("nombretipoconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalRadicado)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
				datos.append(mapa.get("nombretipoconvenio_"+i)+"\n");
				datos.append(mapa.get(indiceConvenio+i)+"\n");
				datos.append(mapa.get(indiceFactura+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("porradicar_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_0_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalradicado_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalcarteravencida_"+i)+""))+"\n");
				//Voy sumando el valor total por nombretipoconvenio
				totalPorRadicar = Utilidades.convertirADouble(mapa.get("porradicar_"+i)+"");
				totalColumn0 = Utilidades.convertirADouble(mapa.get("column_0_"+i)+"");
				totalColumn1_30 = Utilidades.convertirADouble(mapa.get("column_1_30_"+i)+"");
				totalColumn31_60 = Utilidades.convertirADouble(mapa.get("column_31_60_"+i)+"");
			 	totalColumn61_90 = Utilidades.convertirADouble(mapa.get("column_61_90_"+i)+"");
			 	totalColumn91_120 = Utilidades.convertirADouble(mapa.get("column_91_120_"+i)+"");
			 	totalColumn121_150 = Utilidades.convertirADouble(mapa.get("column_121_150_"+i)+"");
			 	totalColumn151_180 = Utilidades.convertirADouble(mapa.get("column_151_180_"+i)+"");
			 	totalColumn181_360 = Utilidades.convertirADouble(mapa.get("column_181_360_"+i)+"");
			 	totalColumn360 = Utilidades.convertirADouble(mapa.get("column_mayor_360_"+i)+"");
			 	totalRadicado = Utilidades.convertirADouble(mapa.get("totalradicado_"+i)+"");
			 	totalCartera = Utilidades.convertirADouble(mapa.get("totalcarteravencida_"+i)+"");
			 	//Si es la ultima fila con solo un registro por nombretipoconvenio se muestra el total del ultimo nombretipoconvenio y se realiza el totalizado general
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Total "+mapa.get("nombretipoconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalColumn0)+", "+UtilidadTexto.formatearExponenciales(totalColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalColumn360)+", "+UtilidadTexto.formatearExponenciales(totalRadicado)+", "+UtilidadTexto.formatearExponenciales(totalCartera)+"\n");
			 		//Sumo el total de la ultima columna para tomar el valor general
					totalGeneralPorRadicar = totalGeneralPorRadicar + totalPorRadicar;
					totalGeneralColumn0 = totalGeneralColumn0 + totalColumn0;
				 	totalGeneralColumn1_30 = totalGeneralColumn1_30 + totalColumn1_30;
				 	totalGeneralColumn31_60 = totalGeneralColumn31_60 + totalColumn31_60;
				 	totalGeneralColumn61_90 = totalGeneralColumn61_90 + totalColumn61_90;
				 	totalGeneralColumn91_120 = totalGeneralColumn91_120 + totalColumn91_120;
				 	totalGeneralColumn121_150 = totalGeneralColumn121_150 + totalColumn121_150;
				 	totalGeneralColumn151_180 = totalGeneralColumn151_180 + totalColumn151_180;
				 	totalGeneralColumn181_360 = totalGeneralColumn181_360 + totalColumn181_360;
				 	totalGeneralColumn360 = totalGeneralColumn360 + totalColumn360;
				 	totalGeneralRadicado = totalGeneralRadicado + totalRadicado;
				 	totalGeneralCartera = totalGeneralCartera + totalCartera;
				 	datos.append("Total General, "+UtilidadTexto.formatearExponenciales(totalGeneralPorRadicar)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn0)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn1_30)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn31_60)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn61_90)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn91_120)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn121_150)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn151_180)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn181_360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralColumn360)+", "+UtilidadTexto.formatearExponenciales(totalGeneralRadicado)+", "+UtilidadTexto.formatearExponenciales(totalGeneralCartera)+"\n");
			 	}
			}
		}
		return datos;
	}
	
	//********************************************** FIN EDAD CARTERA POR FECHA RADICACION ******************************************
	
    /**
	 * 
	 */
	public EdadCartera() 
	{
		this.reset();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * @return the convenioNormal
	 */
	public int getConvenioNormal() {
		return convenioNormal;
	}

	/**
	 * @param convenioNormal the convenioNormal to set
	 */
	public void setConvenioNormal(int convenioNormal) {
		this.convenioNormal = convenioNormal;
	}

	/**
	 * @return the esFactura
	 */
	public String getEsFactura() {
		return esFactura;
	}

	/**
	 * @param esFactura the esFactura to set
	 */
	public void setEsFactura(String esFactura) {
		this.esFactura = esFactura;
	}

	/**
	 * @return the fechaCorte
	 */
	public String getFechaCorte() {
		return fechaCorte;
	}

	/**
	 * @param fechaCorte the fechaCorte to set
	 */
	public void setFechaCorte(String fechaCorte) {
		this.fechaCorte = fechaCorte;
	}

	/**
	 * @return the tipoConvenio
	 */
	public int getTipoConvenio() {
		return tipoConvenio;
	}

	/**
	 * @param tipoConvenio the tipoConvenio to set
	 */
	public void setTipoConvenio(int tipoConvenio) {
		this.tipoConvenio = tipoConvenio;
	}

	/**
	 * @return the tipoReporte
	 */
	public String getTipoReporte() {
		return tipoReporte;
	}

	/**
	 * @param tipoReporte the tipoReporte to set
	 */
	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	/**
	 * @return the manejaConversionMoneda
	 */
	public boolean getManejaConversionMoneda() {
		return manejaConversionMoneda;
	}

	/**
	 * @param manejaConversionMoneda the manejaConversionMoneda to set
	 */
	public void setManejaConversionMoneda(boolean manejaConversionMoneda) {
		this.manejaConversionMoneda = manejaConversionMoneda;
	}

	/**
	 * @return the tiposMonedaTagMap
	 */
	public HashMap getTiposMonedaTagMap() {
		return tiposMonedaTagMap;
	}

	/**
	 * @param tiposMonedaTagMap the tiposMonedaTagMap to set
	 */
	public void setTiposMonedaTagMap(HashMap tiposMonedaTagMap) {
		this.tiposMonedaTagMap = tiposMonedaTagMap;
	}

	/**
	 * @return the tiposMonedaTagMap
	 */
	public Object getTiposMonedaTagMap(Object key) {
		return tiposMonedaTagMap.get(key);
	}

	/**
	 * @param tiposMonedaTagMap the tiposMonedaTagMap to set
	 */
	public void setTiposMonedaTagMap(Object key, Object value) {
		this.tiposMonedaTagMap.put(key, value);
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the empresaInstitucion
	 */
	public double getEmpresaInstitucion() {
		return empresaInstitucion;
	}

	/**
	 * @param empresaInstitucion the empresaInstitucion to set
	 */
	public void setEmpresaInstitucion(double empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}

	
}