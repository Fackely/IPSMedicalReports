package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;

import util.InfoDatosDouble;

import com.princetonsa.dto.manejoPaciente.DtoCamposPerfilNed;
import com.princetonsa.dto.manejoPaciente.DtoPerfilNed;

/**
 * 
 * @author axioma
 *
 */
public interface PerfilNEDDao 
{

	/***
	 * 
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(Connection con, DtoPerfilNed dto);
	
	/**
	 * 
	 * 
	 * @param dto
	 * @return
	 */
	public  double guardarCampo(Connection con, DtoCamposPerfilNed dto) ;
	/**
	 * 
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public boolean modificar(Connection con, DtoPerfilNed dtoNuevo, DtoPerfilNed dtoWhere);
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public  ArrayList<DtoPerfilNed> cargar(DtoPerfilNed dtoWhere) ;
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public  ArrayList<DtoCamposPerfilNed> cargarCampos(DtoCamposPerfilNed dtoWhere) ;
	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public  boolean modificarCampos(Connection con, DtoCamposPerfilNed dtoNuevo, DtoCamposPerfilNed dtoWhere) ;
	
	/**
	 * 
	 * @param codigoPkPerfil
	 * @return
	 */
	public InfoDatosDouble cargarTotalesFactoresPrediccion(double codigoPkPerfil);
}
