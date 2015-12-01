package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.GeneracionArchivoPlanoIndicadoresCalidadDao;
import com.princetonsa.dao.historiaClinica.RegistroResumenParcialHistoriaClinicaDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao;

public class RegistroResumenParcialHistoriaClinica extends ValidatorForm {
	
	/**
	 * Intefaz para acceder y comunicarse con la fuente de datos
	 */
	private RegistroResumenParcialHistoriaClinicaDao objetoDao;
	
	
	/**
	 *  Constructor de la clase
	 */
	public RegistroResumenParcialHistoriaClinica() 
	{
		init(System.getProperty("TIPOBD"));
		this.reset();
	}
	
	/**
	 * Inicializa el acceso a la base de datos de este objeto, obteniendo su respectivo DAO.
	 * param tipoBD el tipo de bases de datos que va a usar este objeto.
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD.
	 * son los nombres y constantes definidos en <code>DaoFactory</code>
	 * @return <b>true</b> si la inicializacion fue exitosa, <code>false</code> si no.
	 */
	
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getRegistroResumenParcialHistoriaClinicaDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}
	
	/**
	 * resetea los atributos del objeto.
	 *
	 */
	private void reset() 
	{
		
   	}
	
	
	public HashMap consultarNotas(Connection con, HashMap mapa) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroResumenParcialHistoriaClinicaDao().consultarNotas(con, mapa);
			
	}
	
	public int insertarNotas(Connection con, HashMap mapa) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroResumenParcialHistoriaClinicaDao().insertarNotas(con, mapa);
			
	}
	
	public  HashMap consultarNotasAsocio(Connection con, HashMap mapa){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroResumenParcialHistoriaClinicaDao().consultarNotasAsocio(con, mapa);
	}

}
