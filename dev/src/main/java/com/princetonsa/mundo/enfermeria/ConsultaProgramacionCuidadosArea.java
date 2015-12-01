package com.princetonsa.mundo.enfermeria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.enfermeria.ConsultaProgramacionCuidadosAreaDao;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;


public class ConsultaProgramacionCuidadosArea {

	private static ConsultaProgramacionCuidadosAreaDao ObjetoDao;
	
	
	public ConsultaProgramacionCuidadosArea(){
		init(System.getProperty("TIPOBD"));
		
	}
	
	private boolean init(String tipoBD){
		if(ObjetoDao==null){
			DaoFactory myFactory= DaoFactory.getDaoFactory(tipoBD);
			ObjetoDao = myFactory.getConsultaProgramacionCuidadosAreaDao();
			if(ObjetoDao!=null){
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Metodo para consultar los Pisos segun centro atencion
	 */
	public static HashMap<String, Object> listarPisos (Connection con, int centroAtencion)
	{
		
		return ObjetoDao.listaPisos(con, centroAtencion);
	}
	
	
	/**
	 * Metodo para consultar las Habitaciones segun piso
	 */
	public static HashMap<String, Object> listarHabitaciones (Connection con, int piso)
	{
		return ObjetoDao.listaHabitaciones(con, piso);
	}
	
	/**
	 * 
	 * @param con
	 * @param areaFiltro
	 * @param pisoFiltro
	 * @param habitacionFiltro
	 * @return
	 */
	public HashMap consultarListadoPacientes(Connection con, String areaFiltro,	String pisoFiltro, String habitacionFiltro,String fechaProg, String horaProg) 
	{
		return ObjetoDao.consultarListadoPacientes(con, areaFiltro, pisoFiltro, habitacionFiltro,fechaProg, horaProg);
	}

	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @param codigoPkFrecCuidadoEnfer
	 * @param activo
	 * @return
	 */
	public ArrayList<DtoFrecuenciaCuidadoEnferia> consultarFrecuenciaCuidado(Connection con, String ingreso,int codigoPkFrecCuidadoEnfer, boolean activo) {
		
		HashMap parametros = new HashMap();
		parametros.put("ingreso",ingreso);
		parametros.put("codigoPkFrecCuidadoEnfer",codigoPkFrecCuidadoEnfer);
		parametros.put("activo",activo?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		return ObjetoDao.consultarFrecuenciaCuidado(con,parametros);
	}

	/**
	 * 
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public  ArrayList<HashMap<String,Object>> consultarTipoFrecuenciaInst(Connection con,int codigoInstitucionInt) {
		
		HashMap parametros = new HashMap();
		parametros.put("institucion", codigoInstitucionInt);
		return ObjetoDao.consultarTipoFrecuenciaInst(con, parametros);
	}
	
	public String cadenaConsultaCuidados(Connection con, String ingreso,int codigoPkFrecCuidadoEnfer, boolean activo ){
		HashMap parametros = new HashMap();
		parametros.put("ingreso",ingreso);
		parametros.put("codigoPkFrecCuidadoEnfer",codigoPkFrecCuidadoEnfer);
		parametros.put("activo",activo?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		return ObjetoDao.cadenaConsultaCuidados(con, parametros);
	}
	
	
	public ActionErrors validacionBusquedaporRango(String fecha,String hora,String area, String piso, String habitacion)
	{
		ActionErrors errores = new ActionErrors();
		
		if(fecha.equals("")) 
		{
			errores.add("descripcion",new ActionMessage("errors.required","La Fecha Progaramacion Inicial  "));
		}
		if(hora.equals("")) 
		{
			errores.add("descripcion",new ActionMessage("errors.required","La Hora Progaramacion Inicial  "));
		}
		
		if(area.equals(""))
		 {
			if(piso.equals(""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","EL Piso o el Area"));
			}
				
		 }else
		 {
			 
		 }
			 
		
		return errores;
	}
	
		
	
}
