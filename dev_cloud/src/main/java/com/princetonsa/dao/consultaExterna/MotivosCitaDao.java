package com.princetonsa.dao.consultaExterna;

import java.util.ArrayList;

import com.princetonsa.dto.consultaExterna.DtoMotivosCita;


public interface MotivosCitaDao 
{
	ArrayList<DtoMotivosCita> consultarMotivosCita();

	int insertarMotivoCita(String codigo, String descripcion, String activo, int institucion, String usuario, String tipoMotivo);

	boolean modificarMotivoCita(String codigo, String descripcion,String activo, int codigoPk, String usuario, String tipoMotivo);

	boolean insertarLogMotivoCita(String codigo, String descripcion,String activo, String eliminado, String usuario, int motivoCita, String tipoMotivo);

	boolean eliminarMotivoCita(int codigoPk);
}