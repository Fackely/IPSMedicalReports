package com.princetonsa.mundo.consultaExterna;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.carterapaciente.PazYSalvoCarteraPacienteDao;
import com.princetonsa.dao.consultaExterna.MotivosCitaDao;
import com.princetonsa.dto.consultaExterna.DtoMotivosCita;


public class MotivosCita 
{
	Logger logger = Logger.getLogger(MotivosCita.class);
	
	private static MotivosCitaDao getMotivosCitaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosCitaDao();
	}

	public ArrayList<DtoMotivosCita> consultarMotivosCita() {
		return getMotivosCitaDao().consultarMotivosCita();
	}

	public int guardarMotivoCita(String codigo, String descripcion,String activo, int institucion, String usuario, String tipoMotivo) 
	{
		return getMotivosCitaDao().insertarMotivoCita(codigo, descripcion, activo, institucion, usuario, tipoMotivo);
	}

	public boolean modificarMotivoCita(String codigo, String descripcion,String activo, int codigoPk, String usuario, String tipoMotivo)
	{
		return getMotivosCitaDao().modificarMotivoCita(codigo, descripcion, activo, codigoPk, usuario, tipoMotivo);
	}

	public boolean insertarLogMotivoCita(String codigo, String descripcion, String activo, String eliminado, String usuario, int motivoCita, String tipoMotivo) 
	{
		return getMotivosCitaDao().insertarLogMotivoCita(codigo, descripcion, activo, eliminado, usuario, motivoCita, tipoMotivo);
	}

	public boolean eliminarMotivoCita(int codigoPk) {
		return getMotivosCitaDao().eliminarMotivoCita(codigoPk);
	}
}