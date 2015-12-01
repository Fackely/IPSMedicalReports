package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;

import util.InfoDatosDouble;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.manejoPaciente.DtoCamposPerfilNed;
import com.princetonsa.dto.manejoPaciente.DtoPerfilNed;

/**
 * 
 * @author axioma
 *
 */
public class PerfilNED 
{
	
	public static DtoPerfilNed cargarPerfilNEDXPaciente(int codigoPaciente, int codigoInstitucion)
	{
		DtoPerfilNed dto= new DtoPerfilNed();
		dto.setCodigoPaciente(codigoPaciente);
		dto.setInstitucion(codigoInstitucion);
		ArrayList<DtoPerfilNed> arrayDto= cargar(dto);
		if(arrayDto.size()>0)
		{
			return arrayDto.get(0);
		}
		return new DtoPerfilNed();
	}
	
	/**
	 * 
	 * @param codigoPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean existePerfilNedPaciente(int codigoPaciente, int codigoInstitucion)
	{
		DtoPerfilNed dto= new DtoPerfilNed();
		dto.setCodigoPaciente(codigoPaciente);
		dto.setInstitucion(codigoInstitucion);
		return cargar(dto).size()>0;
	}
	
	public static ArrayList<DtoPerfilNed> cargar(DtoPerfilNed dtoWhere) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPerfilNEDDao().cargar(dtoWhere);
	}


	public static ArrayList<DtoCamposPerfilNed> cargarCampos(
			DtoCamposPerfilNed dtoWhere) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPerfilNEDDao().cargarCampos(dtoWhere);
	}


	public static double guardar(Connection con, DtoPerfilNed dto) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPerfilNEDDao().guardar(con, dto);
	}

	
	public static  double guardarCampo(Connection con, DtoCamposPerfilNed dto) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPerfilNEDDao().guardarCampo(con, dto);
	}

	
	public static boolean modificar(Connection con, DtoPerfilNed dtoNuevo, DtoPerfilNed dtoWhere) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPerfilNEDDao().modificar(con, dtoNuevo, dtoWhere);
	}
	
	
	public static boolean modificarCampos(Connection con, DtoCamposPerfilNed dtoNuevo, DtoCamposPerfilNed dtoWhere) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPerfilNEDDao().modificarCampos(con, dtoNuevo, dtoWhere);
	}
	
	/**
	 * 
	 * @param codigoPkPerfil
	 * @return
	 */
	public static InfoDatosDouble cargarTotalesFactoresPrediccion(double codigoPkPerfil)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPerfilNEDDao().cargarTotalesFactoresPrediccion(codigoPkPerfil);
	}
	
}
