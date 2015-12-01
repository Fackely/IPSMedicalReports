/**
 * 
 */
package com.princetonsa.mundo.historiaClinica;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.HistoricoAtencionesDao;
import com.princetonsa.dto.historiaClinica.historicoAtenciones.DtoHistoricosHC;

/**
 * @author axioma
 *
 */
public class HistoricoAtenciones 
{
	
	private HistoricoAtencionesDao objetoDao;
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(HistoricoAtenciones.class);
			
	
	/**
	 * 
	 */
	public HistoricoAtenciones() 
	{
		reset();
		//this.init(System.getProperty("TIPOBD"));
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * 
	 * @param codigoPac
	 */
	public HistoricoAtenciones(int codigoPac) 
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
		this.codigoPaciente=codigoPac;
		this.historicoAtenciones=objetoDao.cargarHistoricoAtenciones(this.codigoPaciente);
	}
	
	/**
	 * 
	 */
	private void reset() 
	{
		this.codigoPaciente=ConstantesBD.codigoNuncaValido;
		this.historicoAtenciones=new ArrayList<DtoHistoricosHC>();
	}
	
	/**
	 * 
	 * @param codigoPac
	 */
	public void cargar(int codigoPac)
	{
		this.codigoPaciente=codigoPac;
		this.historicoAtenciones=objetoDao.cargarHistoricoAtenciones(this.codigoPaciente);
	}

	/**
	 * 
	 */
	public void cargar()
	{
		this.historicoAtenciones=objetoDao.cargarHistoricoAtenciones(this.codigoPaciente);
	}

	/**
	 * 
	 * @param tipoBD
	 * @return
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			objetoDao = myFactory.getHistoricoAtencionesDao();
			wasInited = (objetoDao != null);
		}
		return wasInited;
	}
	

	/**
	 * 
	 */
	private int codigoPaciente;
	
	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() 
	{
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) 
	{
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the historicoAtenciones
	 */
	public ArrayList<DtoHistoricosHC> getHistoricoAtenciones() 
	{
		return historicoAtenciones;
	}

	/**
	 * @param historicoAtenciones the historicoAtenciones to set
	 */
	public void setHistoricoAtenciones(ArrayList<DtoHistoricosHC> historicoAtenciones) 
	{
		this.historicoAtenciones = historicoAtenciones;
	}


	/**
	 * 
	 */
	private ArrayList<DtoHistoricosHC> historicoAtenciones;

}
