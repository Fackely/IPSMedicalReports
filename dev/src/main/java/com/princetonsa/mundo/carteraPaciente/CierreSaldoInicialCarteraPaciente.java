package com.princetonsa.mundo.carteraPaciente;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.carteraPaciente.DtoCierreCarteraPaciente;
import com.princetonsa.dto.carteraPaciente.DtoDetCierreSaldoInicialCartera;

public class CierreSaldoInicialCarteraPaciente {

	/**
	 * 
	 * @param anioCierre
	 * @param mesCierre
	 * @return
	 */
	public static ArrayList<DtoDetCierreSaldoInicialCartera> consultarPosibleListadoDocumentosCierre(String anioCierre, String mesCierre) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCierreSaldoInicialCarteraPacienteDao().consultarPosibleListadoDocumentosCierre(anioCierre,mesCierre);
	}

	/**
	 * 
	 */
	public static int insertarCierreSaldoInicial(DtoCierreCarteraPaciente cierreCarteraPaciente) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCierreSaldoInicialCarteraPacienteDao().insertarCierreSaldoInicial(cierreCarteraPaciente);
		
	}

	public static DtoCierreCarteraPaciente consultarCierreInicial(int institucion) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCierreSaldoInicialCarteraPacienteDao().consultarCierreInicial(institucion);
	}

}
