package com.princetonsa.mundo.manejoPaciente;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.HashMap;
import util.UtilidadBD;
import com.princetonsa.actionform.manejoPaciente.RegistroEventosCatastroficosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.RegistroEventosCatastroficosDao;
import com.princetonsa.dto.manejoPaciente.DtoRegistroEventosCatastroficos;
import com.princetonsa.mundo.PersonaBasica;

public class RegistroEventosCatastroficos
{
	/**
	 * 
	 */
	RegistroEventosCatastroficosDao objetoDao;

	
	/**
	 * 
	 * @param tipoBD
	 * @return
	 */
	public boolean init(String tipoBD)
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao= myFactory.getRegistroEventosCatastroficosDao();
			if( objetoDao!= null )
				return true;
		}
		return false;
		
	}
	
	/**
	 * 
	 *
	 */
	public RegistroEventosCatastroficos()
	{
		init(System.getProperty("TIPOBD"));
	}
	 
	
/////////////////////////////////////////////////////////////////////////
	//adicionado por anexo 485
	public static final String [] indicesListado={"fechaEvento0_","departamentoEvento1_","ciudadEvento2_","direccionEvento3_","tipoEvento4_",
		 										  "codigo5_"};
	
////////////////////////////////////////////////////////////////////////

	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public DtoRegistroEventosCatastroficos consultarRegistroEventoCatastroficoIngreso(Connection con, int codigoIngreso,String codigoEvento)
	{
		return objetoDao.consultarRegistroEventoCatastroficoIngreso(con,codigoIngreso,codigoEvento);
	}

	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @param institucion
	 * @return
	 */
	public boolean modificarRegistroAccidentesTransito(Connection con, DtoRegistroEventosCatastroficos dto, int institucion)
	{
		return objetoDao.modificarRegistroAccidentesTransito(con, dto, institucion);
	}

	/**
	 * 
	 * @param con
	 * @param dto
	 */
	public boolean insertarRegistroAccidentesTransito(Connection con, DtoRegistroEventosCatastroficos dto, int institucion)
	{
		return objetoDao.insertarRegistroAccidentesTransito(con, dto, institucion);
	}
	
	/**
	 * Método implementado para realizar la actualizacion del estado de un evento catastrofico
	 * @param con
	 * @param dtoCatastrofico
	 * @return
	 */
	public int actualizarEstadoRegistroEventoCatastrofico(Connection con,DtoRegistroEventosCatastroficos dtoCatastrofico)
	{
		return objetoDao.actualizarEstadoRegistroEventoCatastrofico(con, dtoCatastrofico);
	}
	
/////////////////////////////////////////////////////////////////////////
	//adicionado por anexo 485
	/**
	 * Metodo encargado de consultar todos los registros de
	 * eventos catastroficos en estado finalizado 
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
	 * -- fechaEvento0_
	 * -- departamentoEvento1_
	 * -- ciudadEvento2_
	 * -- direccionEvento3_
	 * -- tipoEvento4_
	 * -- codigo5_
	 */
	public HashMap cargarListadoEventosCatastroficos (Connection connection,HashMap criterios)
	{
		return objetoDao.cargarListadoEventosCatastroficos(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de asociar un evento catastrofico
	 * a un ingreso
	 * @param connection
	 * @param ingreso
	 * @param codigoEvento
	 * @return
	 */
	public  boolean asociarEvento (Connection connection, String ingreso,String codigoEvento )
	{
		return objetoDao.asociarEvento(connection, ingreso, codigoEvento);	
	}
	
	
	public  boolean guardarAsosico (Connection connection, RegistroEventosCatastroficosForm forma, PersonaBasica paciente) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
	
		transacction=asociarEvento(connection, paciente.getCodigoIngreso()+"", forma.getListadoEventos(indicesListado[5]+forma.getIndex())+"");
		
				
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
/////////////////////////////////////////////////////////////////////////

	/**
	 * Método que actualiza los valores de la Sección Amparos
	 */
	public boolean actualizarAmparos(Connection con, DtoRegistroEventosCatastroficos dto, int institucion)
	{
		return objetoDao.actualizarAmparos(con, dto, institucion);
	}
	
	/**
	 * Método que actualiza los valores de la Sección Datos de la Reclamación
	 * @param con
	 * @param dto
	 * @param institucion
	 * @return
	 */
	public boolean actualizarDatosReclamacion(Connection con, DtoRegistroEventosCatastroficos dto, int institucion)
	{
		return objetoDao.actualizarDatosReclamacion(con, dto, institucion);
		
	}
	
	/**
	 * Método que actualiza los valores de la Sección de Servicios Reclamados
	 * @param con
	 * @param dto
	 * @param institucion
	 * @return
	 */
	public boolean actualizarServiciosReclamados(Connection con, DtoRegistroEventosCatastroficos dto)
	{
		return objetoDao.actualizarServiciosReclamados(con, dto);
		
	}
}