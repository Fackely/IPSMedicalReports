package com.princetonsa.dao.consultaExterna;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.consultaExterna.DtoServiciosAdicionalesProfesionales;

public interface ServiAdicionalesXProfAtenOdontoDao {

	ArrayList<DtoServiciosAdicionalesProfesionales> consultarServiciosAdicionalesProfesionales(int codProfesional,int codEstandar);

	boolean insertarServiciosAdicionales(Connection con,HashMap<String, Object> listServiciosSel, int codProfesional,String loginUsuario, int codInstitucion);

	boolean eliminarServicioExistente(int codigoServicio, int codMedico, int codInstitucion);

}
