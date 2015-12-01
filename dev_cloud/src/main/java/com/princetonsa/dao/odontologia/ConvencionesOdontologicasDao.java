package com.princetonsa.dao.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoConvencionesOdontologicas;

public interface ConvencionesOdontologicasDao {

	ArrayList<DtoConvencionesOdontologicas> consultarConvencionesOdontologicas(int codInstitucion);

	boolean eliminarConvencion(int codigoConvencion);

	boolean modificarConvencionOdontologica(Connection con,DtoConvencionesOdontologicas nuevaConvencion,int consecutivoConvencion, String loginUsuario);

	int crearNuevaConvencionOdontologica(Connection con,DtoConvencionesOdontologicas nuevaConvencion, String loginUsuario,int codInstitucion);



}
