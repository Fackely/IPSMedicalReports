package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesIntegridadDominio;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ContrarreferenciaDao;


public class Contrarreferencia 
{
	
	
	ContrarreferenciaDao objetoDao;
	
	private HashMap contrarreferenciaMap;
	
	
	public boolean init(String tipoBD)
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao= myFactory.getContrarreferenciaDao();
			if( objetoDao!= null )
				return true;
		}
		return false;
		
	}
	
	
	
	public void reset()
	{
		contrarreferenciaMap=new HashMap();
		contrarreferenciaMap.put("numRegistros", "0");
	}
	
	
	
	public Contrarreferencia()
	{
		init(System.getProperty("TIPOBD"));
	}
	

	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	/*public int actualizarEstadoContrarreferrencia(Connection con,HashMap vo)
	{
		return objetoDao.actualizarEstadoContrarreferencia(con, vo);
		
	}*/

	
	
	
		
	/**
	 * 
	 * @return
	 */
	public HashMap getContrarreferenciaMap() {
		return contrarreferenciaMap;
	}



	/**
	 * 
	 * @param contrarreferenciaMap
	 */
	public void setContrarreferenciaMap(HashMap contrarreferenciaMap) {
		this.contrarreferenciaMap = contrarreferenciaMap;
	}



	public HashMap cargarConductasSeguirContrareferencia(Connection con, int numeroReferenciaContra) 
	{
		return objetoDao.cargarConductasSeguirContrareferencia(con,numeroReferenciaContra);
	}
	
	
	
	/*public HashMap busquedaInstitucionesSirc(Connection con,String tipoReferencia,int institucion)
	{
		HashMap vo = new HashMap();
		vo.put("tipoReferencia",tipoReferencia);
		vo.put("institucion", institucion+"");
		return ContrarreferenciaDao.busquedaInstitucionesSirc(con, vo);
	}*/
	
	
	public boolean insertar(Connection con,HashMap vo)
	{
		return objetoDao.insertar(con, vo);
	}
	

	
	
	/**
	 * Modifica un paquete Existente 
	 * @param con
	 * @param vo
	 * @return
	 */
	
	public boolean modificar(Connection con,HashMap vo)
	{
		return objetoDao.modificar(con, vo);
	}
	
	
	
	public int actualizarEstadoContrarreferencia(Connection con,HashMap vo)
	{
		return objetoDao.actualizarEstadoContrarreferencia(con, vo);
		
	}
	
	
	
	
	
	
	public HashMap consultarReferenciaPaciente(Connection con,String tipoReferencia,String estadoReferencia, int codigoPaciente)
	{
		HashMap vo=new HashMap();
		vo.put("tipoReferencia", ConstantesIntegridadDominio.acronimoExterna);
		vo.put("estadoReferencia", ConstantesIntegridadDominio.acronimoEstadoAdmitido);
		vo.put("codigoPaciente", codigoPaciente+"");
		return (HashMap)objetoDao.consultarReferenciaPaciente(con,vo).clone();
	}
	
	
	
	public HashMap consultarReferenciaArea(Connection con,String tipoReferencia,String estadoReferencia, int centrosCosto)
	{
		HashMap vo=new HashMap();
		vo.put("tipoReferencia", ConstantesIntegridadDominio.acronimoExterna);
		vo.put("estadoReferencia", ConstantesIntegridadDominio.acronimoEstadoAdmitido);
		vo.put("centrosCosto", centrosCosto);
		
		return (HashMap)objetoDao.consultarReferenciaArea(con,vo).clone();
	}
	
	
	public void consultarContrarreferencia(Connection con, int institucion, String numeroContrareferencia) 
	{
		HashMap vo=new HashMap();
		vo.put("institucion", institucion);
		vo.put("numeroContrarreferencia", numeroContrareferencia);
		this.contrarreferenciaMap=objetoDao.consultarContrarreferencia(con,vo);
	}



	public HashMap consultarResultadosProcedimiento(Connection con, int numeroReferencia) 
	{
		return (HashMap)objetoDao.consultarResultadosProcedimiento(con,numeroReferencia).clone();
	}
	
	
	
	public boolean insertarProcedimientos(Connection con,HashMap vo)
	{
		return objetoDao.insertarProcedimientos(con, vo);
	}
	
	
	public boolean insertarDiagnosticos(Connection con,HashMap vo)
	{
		return objetoDao.insertarDiagnosticos(con, vo);
	}



	/**
	 * 
	 * @param con
	 * @param numeroContrarreferencia
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean eliminarProcedimientos(Connection con, String numeroContrarreferencia, String numeroSolicitud) 
	{
		return objetoDao.eliminarProcedimientos(con,numeroContrarreferencia,numeroSolicitud);
	}



	/**
	 * 
	 * @param numeroReferenciaContra
	 */
	public boolean eliminarDiagnosticos(Connection con,int numeroReferenciaContra) 
	{
		return objetoDao.eliminarDiagnosticos(con,numeroReferenciaContra);
	}



	/**
	 * 
	 * @param con
	 * @param numeroReferenciaContra
	 * @return
	 */
	public HashMap consultarDiagnosticos(Connection con, int numeroReferenciaContra) 
	{
		return objetoDao.consultarDiagnosticos(con,numeroReferenciaContra);
	}



	/**
	 * 
	 * @param con
	 * @param numeroReferenciaContra
	 * @param codigoConducta
	 * @return
	 */
	public boolean eliminarConductaSeguir(Connection con, int numeroReferenciaContra, String codigoConducta) 
	{
		return objetoDao.eliminarConductaSeguir(con,numeroReferenciaContra,codigoConducta);
	}



	/**
	 * 
	 * @param con
	 * @param numeroReferenciaContra
	 * @param codigoConducta
	 * @param valor
	 * @return
	 */
	public boolean insertarConductaSeguir(Connection con, int numeroReferenciaContra, String codigoConducta, String valor) 
	{
		return objetoDao.insertarConductaSeguir(con,numeroReferenciaContra,codigoConducta,valor);
	}



	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public HashMap getUltimosDiagnosticosIngreso(Connection con, int ingreso) 
	{
		return objetoDao.getUltimosDiagnosticosIngreso(con,ingreso);
	}



	
	
}
