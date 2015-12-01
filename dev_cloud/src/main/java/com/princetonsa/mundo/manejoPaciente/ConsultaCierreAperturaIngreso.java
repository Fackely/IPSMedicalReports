package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.actionform.manejoPaciente.ConsultaCierreAperturaIngresoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ConsultaCierreAperturaIngresoDao;

/**
 * @author Mauricio Jllo
 * Fecha: Mayo de 2008
 */

public class ConsultaCierreAperturaIngreso
{

	/**
     * Constructor de la Clase
     */
    public ConsultaCierreAperturaIngreso()
    {
        this.reset();
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static ConsultaCierreAperturaIngresoDao aplicacionDao;
	
	/**
	 * Método que limpia este objeto
	 */
	public void reset()
	{

	}
	
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
			aplicacionDao = myFactory.getConsultaCierreAperturaIngresoDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}

	/**
	 * Metodo que consulta los Cierre/Apertura de los Ingresos
	 * @param con
	 * @param forma
	 * @return
	 */
	public HashMap consultarCierreAperturaIngresos(Connection con, ConsultaCierreAperturaIngresoForm forma)
	{
		HashMap vo = new HashMap();
		String motivoIngreso[];
		motivoIngreso = forma.getMotivoCierre().split(ConstantesBD.separadorSplit);
		vo.put("centroAtencion", forma.getCodigoCentroAtencion());
		vo.put("tipo", forma.getTipoConsulta());
		vo.put("fechaInicial", forma.getFechaInicial());
		vo.put("fechaFinal", forma.getFechaFinal());
		vo.put("motivo", motivoIngreso[0]);
		vo.put("usuario", forma.getUsuario());
		return aplicacionDao.consultarCierreAperturaIngresos(con, vo);
	}
	
	/**
	 * Metodo que consulta el detalle del Cierre/Apertura Ingresos
	 * @param con
	 * @param forma
	 * @return
	 */
	public HashMap detalleCierreAperturaIngreso(Connection con, int codigoCierreApertura, String motivo)
	{
		return aplicacionDao.detalleCierreAperturaIngreso(con, codigoCierreApertura, motivo);
	}
	
}