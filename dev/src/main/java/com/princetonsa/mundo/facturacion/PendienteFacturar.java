package com.princetonsa.mundo.facturacion;


import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.PendienteFacturarDao;

public class PendienteFacturar 
{

	
	private PendienteFacturarDao objetoDao;
	
	
	private HashMap consulta;
	
	private String fechaCorte;
	
	private String medico;
	
	
	/**
	 * 
	 *
	 */
	public PendienteFacturar() 
	{
		init(System.getProperty("TIPOBD"));
		this.reset();
		
	}
	
	/**
	 * 
	 * @param tipoBD
	 * @return
	 */
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getPendienteFacturarDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}
	
	/**
	 * 
	 *
	 */
	private void reset() 
	{
		consulta=new HashMap();
    	consulta.put("numRegistros","0");
    	
    	this.fechaCorte="";
    	this.medico="";
   	}


	

	/**
	 * 
	 * @return
	 */
	public HashMap getConsulta() {
		return consulta;
	}

	/**
	 * 
	 * @param consulta
	 */
	public void setConsulta(HashMap consulta) {
		this.consulta = consulta;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaCorte() {
		return fechaCorte;
	}

	/**
	 * 
	 * @param fechaCorte
	 */
	public void setFechaCorte(String fechaCorte) {
		this.fechaCorte = fechaCorte;
	}

	/**
	 * 
	 * @return
	 */
	public String getMedico() {
		return medico;
	}

	/**
	 * 
	 * @param medico
	 */
	public void setMedico(String medico) {
		this.medico = medico;
	}

	
	
	/**
	 * 
	 * @param con
	 * @param buscarEstado
	 * @return
	 */
	public HashMap consultarHonorariosPendientes(Connection con, boolean buscarEstado) 
	{
		return objetoDao.consultarHonorariosPendientes(con,getFechaCorte(),getMedico());
	}

		
	
}
