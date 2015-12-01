package com.princetonsa.dao.consultaExterna;

import java.util.HashMap;

public interface MotivosAnulacionCondonacionDao {

	public HashMap consultarMotivosAnulacionCondonacionMultas();

	public boolean EliminarMotivosAnulacionCondonacionMultas(int concecutivo);

	public boolean InsertarMotivosAnulacionCondonacionMultas(String codigo, String descripcion,boolean check, String codigoinstitucion);

	public boolean ModificarMotivosAnulacionCondonacionMultas(int concencutivo,String codigo,
			String descripcion, boolean check, String codigoinstitucion );

	public boolean consultarEliminacion(int consecutivo);

}