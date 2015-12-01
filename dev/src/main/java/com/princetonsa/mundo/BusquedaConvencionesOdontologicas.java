package com.princetonsa.mundo;

import java.util.ArrayList;

import util.InfoDatosStr;

import com.princetonsa.dao.BusquedaConvencionesOdontologicasDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.enu.general.CarpetasArchivos;

public class BusquedaConvencionesOdontologicas {

	private static final String rutaImagen = CarpetasArchivos.IMAGENES.getRuta();
	private static final String rutaTrama = CarpetasArchivos.TRAMAS.getRuta();
	private static final String rutaConvencion = CarpetasArchivos.CONVENCION.getRuta();
	
	
	
	public static BusquedaConvencionesOdontologicasDao getBusquedaConvencionesOdontologicasDao()
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBusquedaConvencionesOdontologicasDao();
		
	}

	public ArrayList<InfoDatosStr> consultarConvencionesOdontologicas(int codigoInstitucionInt,boolean porTipo,String tipo) {
		
		return getBusquedaConvencionesOdontologicasDao().consultarConvencionesOdontologicas(codigoInstitucionInt,porTipo,tipo);
	}

	public ArrayList<InfoDatosStr> consultarImagenesConvencionesOdontologicas() {
		
		return getBusquedaConvencionesOdontologicasDao().consultarImagenesConvencionesOdontologicas();
	}

	public ArrayList<InfoDatosStr> consultarTramasConvencionesOdontologicas() {
		
		return getBusquedaConvencionesOdontologicasDao().consultarTramasConvencionesOdontologicas();
	}
	
	/**
	 * cadena que consulta las imagenes base de una institucion
	 * @param institucion
	 * @return
	 */
	public ArrayList<InfoDatosStr> consultarImagenesBase(int institucion)
	{
		return getBusquedaConvencionesOdontologicasDao().consultarImagenesBase(institucion);
	}
	
}
