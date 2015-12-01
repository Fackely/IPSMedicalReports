package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.ConsumosPorFacturarPacientesHospitalizadosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ConsumosPorFacturarPacientesHospitalizadosDao;

/**
 * @author Juan Alejandro Cardona
 * Fecha: Septiembre 2008
 */

public class ConsumosPorFacturarPacientesHospitalizados
{
	/**
     * Constructor de la Clase
     */
    public ConsumosPorFacturarPacientesHospitalizados()
    {
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static ConsumosPorFacturarPacientesHospitalizadosDao aplicacionDao;
	
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
			aplicacionDao = myFactory.getConsumosPorFacturarPacientesHospitalizadosDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * Metodo que llena el HashMap con los datos de Consumos Por Facturar Pacientes Hospitalizados
	 * @param con
	 * @param forma
	 * @return
	 */
	public static HashMap generarArchivoPlano(Connection con, ConsumosPorFacturarPacientesHospitalizadosForm forma)
	{
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("centroAtencion", forma.getCodigoCentroAtencion());
		criterios.put("fechaInicial", forma.getFechaInicial());
		criterios.put("fechaFinal", forma.getFechaFinal());
		criterios.put("convenio", forma.getConvenioSeleccionado());
		criterios.put("montoInicial", forma.getMontoBaseInicial());
		criterios.put("montoFinal", forma.getMontoBaseFinal());
		criterios.put("tope", forma.getTope());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsumosPorFacturarPacientesHospitalizadosDao().generarArchivoPlano(con, criterios);
	}
	
	/**
	 * Metodo que organiza los datos del mapa para generar el archivo plano de ConsumosPorFacturarPacientesHospitalizados
	 * @param mapa
	 * @param nombreReporte
	 * @param fechaReporte
	 * @param encabezado
	 * @param string 
	 * @param encabezado2 
	 * @return
	 */
	public static StringBuffer cargarMapaConsumosPorFacturarPacientesHospitalizados(HashMap<String, Object> mapa, String nombreReporte, String fechaInicial, String fechaFinal, String encabezado, String usuario)
	{
		StringBuffer datos = new StringBuffer();
		String nameTmp = ""; //String para almacenar nombre del Convenio temporalmente
		
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("PERIODO: "+fechaInicial+" - "+fechaFinal+"\n");
		datos.append("USUARIO: "+usuario+"\n");
		datos.append(encabezado+"\n");

		
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			if(i==0)
			{
				datos.append(mapa.get("codconvenio_"+i)+" - "+mapa.get("nomconvenio_"+i)+"\n");
				datos.append(mapa.get("numingreso_"+i)+", "+mapa.get("fechaingreso_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("nompersona_"+i)+", "+mapa.get("cama_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorconsumos_"+i)+""))+"\n");
				nameTmp = mapa.get("codconvenio_"+i) + "";
			}
			
			else {
				if(nameTmp.compareTo(mapa.get("codconvenio_"+i) + "") != 0 )
					datos.append(mapa.get("codconvenio_"+i)+" - "+mapa.get("nomconvenio_"+i)+"\n");

				datos.append(mapa.get("numingreso_"+i)+", "+mapa.get("fechaingreso_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("nompersona_"+i)+", "+mapa.get("cama_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorconsumos_"+i)+""))+"\n");
			}
			nameTmp = mapa.get("codconvenio_"+i) + "";
		}
		return datos;
	}

	/**
	 * Metodo que retorna en una llave del mapa las condiciones para la consulta
	 * y en otra llave los parametros de búsqueda impresos
	 * @param con
	 * @param forma
	 * @return
	 */
	public HashMap<String, Object> consultarCondicionesConsumosPacientesHospitalizados(Connection con, ConsumosPorFacturarPacientesHospitalizadosForm forma)
	{
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("centroAtencion", forma.getCodigoCentroAtencion());
		criterios.put("fechaInicial", forma.getFechaInicial());
		criterios.put("fechaFinal", forma.getFechaFinal());
		criterios.put("convenio", forma.getConvenioSeleccionado());
		criterios.put("montoInicial", forma.getMontoBaseInicial());
		criterios.put("montoFinal", forma.getMontoBaseFinal());
		criterios.put("tope", forma.getTope());
		criterios.put("tipoSalida", forma.getTipoSalida());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsumosPorFacturarPacientesHospitalizadosDao().consultarCondicionesConsumosPacientesHospitalizados(con, criterios);
	}
}