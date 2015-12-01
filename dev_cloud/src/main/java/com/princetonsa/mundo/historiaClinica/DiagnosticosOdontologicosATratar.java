package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.Logger;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.TxtFile;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.glosas.ConsultarImprimirGlosasSinRespuestaForm;
import com.princetonsa.actionform.historiaClinica.DiagnosticosOdontologicosATratarForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.DiagnosticosOdontologicosATratarDao;
import com.princetonsa.mundo.UsuarioBasico;





public class DiagnosticosOdontologicosATratar
{


	
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(ServiciosXTipoTratamientoOdontologico.class);

	
	/**
	 * 
	 */
	DiagnosticosOdontologicosATratarDao objetoDao;
	
	/**
	 * 
	 */
	private HashMap mapaDiagnosticos;
	
	
	/**
	 * 
	 */
	public DiagnosticosOdontologicosATratar()
	{
		init(System.getProperty("TIPOBD"));
		this.reset();
	}
	

	/**
	 *  
	 *
	 */
	public void reset()
	{
		this.mapaDiagnosticos=new HashMap();
		this.mapaDiagnosticos.put("numRegistros", "0");
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
			objetoDao=myFactory.getDiagnosticosOdontologicosATratarDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
	}
	

	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public HashMap consultarDiagnosticosATratar(int institucion) 
	{
		return objetoDao.consultarDiagnosticosATratar(institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarDiagnosticosATratarEspecifico(Connection con,int codigo) 
	{
		return objetoDao.consultarDiagnosticosATratarEspecifico(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificar(Connection con, HashMap vo) 
	{
		return objetoDao.modificar(con, vo);
	}
       
		
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertar(Connection con, HashMap vo) 
	{
		return objetoDao.insertar(con, vo);
	}


	public HashMap getMapaDiagnosticos() {
		return mapaDiagnosticos;
	}


	public void setMapaDiagnosticos(HashMap mapaDiagnosticos) {
		this.mapaDiagnosticos = mapaDiagnosticos;
	}

    
	
	
}