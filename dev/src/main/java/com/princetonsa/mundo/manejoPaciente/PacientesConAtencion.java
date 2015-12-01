package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.manejoPaciente.PacientesConAtencionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.PacientesConAtencionDao;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class PacientesConAtencion
{

	/**
     * Constructor de la Clase
     */
    public PacientesConAtencion()
    {
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static PacientesConAtencionDao aplicacionDao;
	
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
			aplicacionDao = myFactory.getPacientesConAtencionDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * Metodo que consulta los Pacientes con Atencion
	 * por convenio y por valor
	 * @param con
	 * @param forma
	 * @return
	 */
	public HashMap consultarPacientesConAtencion(Connection con, PacientesConAtencionForm forma)
	{
		HashMap criterios = new HashMap();
		HashMap tempo = new HashMap();
		tempo = this.tiposSolicitudesEscogidas(con, forma);
		criterios.put("centroAtencion", forma.getCodigoCentroAtencion());
		criterios.put("convenio", forma.getConvenioSeleccionado());
		criterios.put("viaingreso", forma.getViaIngreso());
		criterios.put("tipoPaciente", forma.getTipoPaciente());
		criterios.put("tipoSolicitud", tempo.get("codigoSolicitudes"));
		return aplicacionDao.consultarPacientesConAtencion(con, criterios);
	}
	
	/**
	 * Metodo que permite organizar en un string separado por coma
	 * los tipo de solicitud escogidos para realizar el filtrado de 
	 * la consulta
	 * @param con
	 * @param forma
	 * @return
	 */
	public HashMap tiposSolicitudesEscogidas(Connection con, PacientesConAtencionForm forma)
	{
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		String codigoSolicitudes = "";
		String nombreSolicitudes = "";
		//Recorremos la cadena de string para visualizar y separar que tipo de solicitudes se tomaron para hacer el filtrado
		int numReg = forma.getTipoSolicitud().length;
		if (numReg == 1)
		{
			if (!(forma.getTipoSolicitud()[0]).equals(""))
			{
				codigoSolicitudes = forma.getTipoSolicitud()[0];
				nombreSolicitudes = Utilidades.obtenerNombreTipoSolicitud(con, forma.getTipoSolicitud()[0]+"");
			}
		}
		else
		{
			if (numReg > 1)
			{
				for (int i=0; i<numReg; i++)
				{
					if (i == 0)
					{
						codigoSolicitudes = forma.getTipoSolicitud()[i];
						nombreSolicitudes = Utilidades.obtenerNombreTipoSolicitud(con, forma.getTipoSolicitud()[i]+"");
					}
					else
					{
						codigoSolicitudes += ","+forma.getTipoSolicitud()[i];
						nombreSolicitudes += ", "+Utilidades.obtenerNombreTipoSolicitud(con, forma.getTipoSolicitud()[i]+"");
					}
				}
			}
		}
		mapa.put("codigoSolicitudes", codigoSolicitudes);
		mapa.put("nombreSolicitudes", nombreSolicitudes);
		return mapa;
	}
	
	/**
	 * Metodo que organiza los datos del mapa para generar el archivo plano
	 * de Pacientes con Atencion por Convenio y Valor
	 * @param mapa
	 * @param nombreReporte
	 * @param fechaReporte
	 * @param encabezado
	 * @return
	 */
	public static StringBuffer cargarMapaPacientesConAtencion(HashMap<String, Object> mapa, String nombreReporte, String encabezado, String usuario)
	{
		StringBuffer datos = new StringBuffer();
		double totalFacturado = 0;
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("FECHA REPORTE: "+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()+"\n");
		datos.append("USUARIO: "+usuario+"\n");
		datos.append(encabezado+"\n");
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			if(i==0)
			{
				totalFacturado = Utilidades.convertirADouble(mapa.get("totalcuenta_"+i)+"") - Utilidades.convertirADouble(mapa.get("totalpendiente_"+i)+"");
				datos.append(mapa.get("nomtiposolicitud_"+i)+"\n");
				datos.append(mapa.get("numerocama_"+i)+", "+mapa.get("nombrepaciente_"+i)+", "+mapa.get("diagnostico_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("numerohiscli_"+i)+", "+mapa.get("numeroingreso_"+i)+", "+mapa.get("fechaingreso_"+i)+"-"+mapa.get("horaingreso_"+i)+", "+mapa.get("nomconvenio_"+i)+", "+Utilidades.convertirADouble(mapa.get("totalcuenta_"+i)+"")+", "+totalFacturado+", "+Utilidades.convertirADouble(mapa.get("totalpendiente_"+i)+"")+"\n");
			}
			else if((mapa.get("tiposolicitud_"+i)+"").equals((mapa.get("tiposolicitud_"+(i-1))+"")))
			{
				totalFacturado = Utilidades.convertirADouble(mapa.get("totalcuenta_"+i)+"") - Utilidades.convertirADouble(mapa.get("totalpendiente_"+i)+"");
				datos.append(mapa.get("numerocama_"+i)+", "+mapa.get("nombrepaciente_"+i)+", "+mapa.get("diagnostico_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("numerohiscli_"+i)+", "+mapa.get("numeroingreso_"+i)+", "+mapa.get("fechaingreso_"+i)+"-"+mapa.get("horaingreso_"+i)+", "+mapa.get("nomconvenio_"+i)+", "+Utilidades.convertirADouble(mapa.get("totalcuenta_"+i)+"")+", "+totalFacturado+", "+Utilidades.convertirADouble(mapa.get("totalpendiente_"+i)+"")+"\n");
			}
			else
			{
				totalFacturado = Utilidades.convertirADouble(mapa.get("totalcuenta_"+i)+"") - Utilidades.convertirADouble(mapa.get("totalpendiente_"+i)+"");
				datos.append(mapa.get("nomtiposolicitud_"+i)+"\n");
				datos.append(mapa.get("numerocama_"+i)+", "+mapa.get("nombrepaciente_"+i)+", "+mapa.get("diagnostico_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("numerohiscli_"+i)+", "+mapa.get("numeroingreso_"+i)+", "+mapa.get("fechaingreso_"+i)+"-"+mapa.get("horaingreso_"+i)+", "+mapa.get("nomconvenio_"+i)+", "+Utilidades.convertirADouble(mapa.get("totalcuenta_"+i)+"")+", "+totalFacturado+", "+Utilidades.convertirADouble(mapa.get("totalpendiente_"+i)+"")+"\n");
			}
		}
		
		return datos;
	}
	
}