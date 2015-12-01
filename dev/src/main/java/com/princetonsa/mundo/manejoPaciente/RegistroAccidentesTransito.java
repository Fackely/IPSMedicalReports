package com.princetonsa.mundo.manejoPaciente;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.HashMap;
import util.UtilidadBD;
import com.princetonsa.actionform.manejoPaciente.RegistroAccidentesTransitoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.RegistroAccidentesTransitoDao;
import com.princetonsa.dto.manejoPaciente.DtoRegistroAccidentesTransito;
import com.princetonsa.mundo.PersonaBasica;

public class RegistroAccidentesTransito
{
	/**
	 * Objeto dao para manejar el acceso a la BD.
	 * 
	 */
	RegistroAccidentesTransitoDao objetoDao;
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( objetoDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			objetoDao= myFactory.getRegistroAccidentesTransitoDao();
			if( objetoDao!= null )
				return true;
		}
		return false;
	}
	
	public RegistroAccidentesTransito()
	{
		init(System.getProperty("TIPOBD"));
	}
	
	/////////////////////////////////////////////////////////////////////////
	//adicionado por anexo 485
	public static final String [] indicesListado={"fechaAccidente0_","departamentoAccidente1_","ciudadAccidente2_","lugarAccidente3_","asegurado4_",
		 										  "nombreConductor5_","nombrePropietario6_","codigo7_"};
	
	////////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertarRegistroAccidentesTransito(Connection con,DtoRegistroAccidentesTransito dto)
	{
		return objetoDao.insertarRegistroAccidentesTransito(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean modificarRegistroAccidentesTransito(Connection con,DtoRegistroAccidentesTransito dto)
	{
		return objetoDao.modificarRegistroAccidentesTransito(con, dto);
	}
	
	/**
	 * 
	 * Metodo que consulta un registro de accidente de transito dada la llave.
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static DtoRegistroAccidentesTransito consultarRegistroAccidentesTransitoLlave(Connection con,String codigo)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroAccidentesTransitoDao().consultarRegistroAccidentesTransitoLlave(con, codigo);
	}


	/**
	 * 
	 * Metodo que consulta un registro de accidentes de transito dado el codigo del ingreso asociado.
	 * @param con
	 * @param string
	 */
	public DtoRegistroAccidentesTransito consultarRegistroAccidentesTransitoIngreso(Connection con, String ingreso)
	{
		return objetoDao.consultarRegistroAccidentesTransitoIngreso(con, ingreso);
	}

	/**
	 * metodo que realiza la busqueda avanzada 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 */
	public HashMap busquedaAvanzada(Connection con, HashMap criteriosBusquedaMap) 
	{
		return objetoDao.busquedaAvanzada(con, criteriosBusquedaMap); 
	}
	
	/**
	 * Método implementado para actualizar el estado del registro de accidentes de transito,
	 * si el estado es anulación se ingresa la fecha, hora y usuario anulacion
	 * @param con
	 * @param dtoReg
	 * @return
	 */
	public int actualizarEstadoRegistroAccidenteTransito(Connection con,DtoRegistroAccidentesTransito dtoReg)
	{
		return objetoDao.actualizarEstadoRegistroAccidenteTransito(con,dtoReg);
	}
	
	/**
	 * Metodo encargado de modificar la seccion de maparos
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean modificarAmparos(Connection con,DtoRegistroAccidentesTransito dto)
	{
		return  objetoDao.modificarAmparos(con, dto); 
	}
	/**
	 * mapa de generarReporteCertificadoAtencionMedica
	 * @param con
	 * @param criteriosBusquedaMap, keys--> codigoCentroAtencion , codigoInstitucion, idIngreso
	 * @return
	 */
	public static HashMap generarReporteCertificadoAtencionMedica(Connection con, HashMap criteriosBusquedaMap) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroAccidentesTransitoDao().generarReporteCertificadoAtencionMedica(con, criteriosBusquedaMap);
	}
	
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap, keys--> codigoCentroAtencion , codigoInstitucion, idIngreso
	 * @return
	 */
	public static HashMap generarReporteFUSOAT01(Connection con, HashMap criteriosBusquedaMap)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroAccidentesTransitoDao().generarReporteFUSOAT01(con, criteriosBusquedaMap);
	}
	
	/**
	 * Metodo encargado de modificar los datos de la Reclamacion
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean modificarReclamacion(Connection con,DtoRegistroAccidentesTransito dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroAccidentesTransitoDao().modificarReclamacion(con, dto);
	}
	
	/////////////////////////////////////////////////////////////////////////
	//adicionado por anexo 485
	/**
	 * Metodo encargado de consultar todos los registros de
	 * accidentes de transito  filtrandolos por el estado. 
	 * @param connection
	 * @param criterios
	 * -------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -------------------------
	 * -- estado
	 * @return
	 * ---------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ---------------------------
	 * -- fechaAccidente0_
	 * -- departamentoAccidente1_
	 * -- ciudadAccidente2_
	 * -- lugarAccidente3_
	 * -- asegurado4_
	 * -- nombreConductor5
	 * -- nombrePropietario6
	 */
	public static HashMap cargarListadoAccidentesTransito (Connection connection,HashMap criterios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroAccidentesTransitoDao().cargarListadoAccidentesTransito(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de asociar un accidente de transito 
	 * a un ingreso
	 * @param connection
	 * @param ingreso
	 * @param codigoAccidente
	 * @return
	 */
	public static boolean asociarAcciedente (Connection connection, String ingreso,String codigoAccidente )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroAccidentesTransitoDao().asociarAcciedente(connection, ingreso, codigoAccidente);
	}
	
	
	public static boolean guardarAsosico (Connection connection, RegistroAccidentesTransitoForm forma, PersonaBasica paciente) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
	
		transacction=asociarAcciedente(connection, paciente.getCodigoIngreso()+"", forma.getListadoAccidentes(indicesListado[7]+forma.getIndex())+"");
		
				
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
				
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		
		return  transacction;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
}
