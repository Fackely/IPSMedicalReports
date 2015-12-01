package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.ConsumosFacturadosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ConsumosFacturadosDao;

/**
 * @author Mauricio Jllo.
 * Fecha: Mayo de 2008
 */

public class ConsumosFacturados
{

	/**
     * Constructor de la Clase
     */
    public ConsumosFacturados()
    {
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static ConsumosFacturadosDao aplicacionDao;
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( aplicacionDao == null ) 
		{ 
	    	// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			aplicacionDao = myFactory.getConsumosFacturadosDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * Metodo que llena el HashMap con los datos de Consumos Facturados
	 * @param con
	 * @param forma
	 * @return
	 */
	public static HashMap generarArchivoPlano(Connection con, ConsumosFacturadosForm forma)
	{
		HashMap vo = new HashMap();
		vo.put("centroAtencion", forma.getCodigoCentroAtencion());
		vo.put("fechaInicial", forma.getFechaInicial());
		vo.put("fechaFinal", forma.getFechaFinal());
		vo.put("convenio", forma.getConvenioSeleccionado());
		vo.put("montoInicial", forma.getMontoBaseInicial());
		vo.put("montoFinal", forma.getMontoBaseFinal());
		vo.put("tope", forma.getTope());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsumosFacturadosDao().generarArchivoPlano(con, vo);
	}
	
	/**
	 * Metodo que organiza los datos del mapa para generar el archivo plano de Consumos Facturados
	 * @param mapa
	 * @param nombreReporte
	 * @param fechaReporte
	 * @param encabezado
	 * @return
	 */
	public static StringBuffer cargarMapaConsumosFacturados(HashMap<String, Object> mapa, String nombreReporte, String fechaReporte, String encabezado)
	{
		StringBuffer datos = new StringBuffer();
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("FECHA CORTE: "+fechaReporte+"\n");
		datos.append(encabezado+"\n");
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			datos.append(mapa.get("consecutivofactura_"+i)+", "+mapa.get("consecutivoingreso_"+i)+", "+mapa.get("fechafactura_"+i)+"-"+mapa.get("horafactura_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("nombrepaciente_"+i)+", "+mapa.get("numerohiscli_"+i)+", "+mapa.get("nombreconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorfactura_"+i)+""))+"\r\n");
		}
		
		return datos;
	}

	/**
	 * 
	 * @param codigoCentroAtencion
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenioSeleccionado
	 * @param montoBaseInicial
	 * @param montoBaseFinal
	 * @param tope
	 * @return
	 */
	public static String obtenerCondiciones(String codigoCentroAtencion,
			String fechaInicial, String fechaFinal,
			String convenioSeleccionado, String montoBaseInicial,
			String montoBaseFinal, String tope) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsumosFacturadosDao().obtenerCondiciones( codigoCentroAtencion,
				 fechaInicial,  fechaFinal,
				 convenioSeleccionado,  montoBaseInicial,
				 montoBaseFinal,  tope);
	}
	
}