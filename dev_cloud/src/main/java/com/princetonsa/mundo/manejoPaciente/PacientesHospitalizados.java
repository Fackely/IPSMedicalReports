package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.actionform.manejoPaciente.PacientesHospitalizadosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.PacientesHospitalizadosDao;

/**
 * @author Mauricio Jllo
 * Fecha: Mayo de 2008
 */

public class PacientesHospitalizados
{

	/**
     * Constructor de la Clase
     */
    public PacientesHospitalizados()
    {
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static PacientesHospitalizadosDao aplicacionDao;
	
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
			aplicacionDao = myFactory.getPacientesHospitalizadosDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}

	/**
	 * Metodo que carga los centros de costo segun el tipo de paciente y el centro de atencion seleccionado 
	 * @param con
	 * @param forma
	 * @return
	 */
	public HashMap obtenerCentrosCosto(Connection con, int institucion, String tipoArea, String tipoPaciente, String codigoCentroAtencion)
	{
		return aplicacionDao.obtenerCentrosCosto(con, institucion, tipoArea, tipoPaciente, codigoCentroAtencion);
	}
	
	/**
	 * Metodo que consulta los Pacientes Hospitalizados
	 * @param con
	 * @param forma
	 * @return
	 */
	public HashMap consultarPacientesHospitalizados(Connection con, PacientesHospitalizadosForm forma)
	{
		HashMap vo = new HashMap();
		this.separarDatos(forma);
		vo.put("centroAtencion", forma.getCodigoCentroAtencion());
		vo.put("tipoPaciente", forma.getTipo());
		vo.put("centroCosto", forma.getCodigoCentroCosto());
		vo.put("convenio", forma.getConvenioSeleccionado());
		vo.put("piso", forma.getPisoSeleccionado());
		vo.put("indicativoEgreso", forma.getIndicativoEgreso());
		vo.put("fechaInicial", forma.getFechaInicial());
		vo.put("fechaFinal", forma.getFechaFinal());
		return aplicacionDao.consultarPacientesHospitalizados(con, vo);
	}
	
	/**
	 * 
	 */
	public static void separarDatos(PacientesHospitalizadosForm forma)
	{
		//Realizar el split del centro de atencion
		String centroAtencion[];
		centroAtencion = forma.getCodigoCentroAtencion().split(ConstantesBD.separadorSplit);
		forma.setCodigoCentroAtencion(centroAtencion[0]);
		forma.setNombreCentroAtencion(centroAtencion[1]);
		
		//Realizar el split del convenio
		if(!forma.getConvenioSeleccionado().equals("") && !forma.getConvenioSeleccionado().equals("null"))
		{
			String convenio[];
			convenio = forma.getConvenioSeleccionado().split(ConstantesBD.separadorSplit);
			forma.setConvenioSeleccionado(convenio[0]);
			forma.setNombreConvenioSeleccionado(convenio[1]);
		}
		
		//Realizar el split del centro de costo
		if(!forma.getCodigoCentroCosto().equals("") && !forma.getCodigoCentroCosto().equals("null"))
		{
			String centroCosto[];
			centroCosto = forma.getCodigoCentroCosto().split(ConstantesBD.separadorSplit);
			forma.setCodigoCentroCosto(centroCosto[0]);
			forma.setNombreCentroCosto(centroCosto[1]);
		}
	}
	
}