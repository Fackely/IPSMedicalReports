package com.princetonsa.dao.oracle.consultaExterna;


import java.util.HashMap;
import com.princetonsa.dao.consultaExterna.MotivosAnulacionCondonacionDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseMotivosAnulacionCondonacionDao;

public class OracleMotivosAnulacionCondonacionDao  implements MotivosAnulacionCondonacionDao 
{
	public HashMap consultarMotivosAnulacionCondonacionMultas(){
		
		return SqlBaseMotivosAnulacionCondonacionDao.consultarMotivosAnulacionCondonacionMultas();
		}

	public boolean EliminarMotivosAnulacionCondonacionMultas(int concecutivo) {
		
		return SqlBaseMotivosAnulacionCondonacionDao.EliminarMotivosAnulacionCondonacionMultas(concecutivo);
	}

	public boolean InsertarMotivosAnulacionCondonacionMultas(String codigo, String descripcion,boolean check, String codigoinstitucion) {

		return SqlBaseMotivosAnulacionCondonacionDao.InsertarMotivosAnulacionCondonacionMultas( codigo,  descripcion, check, codigoinstitucion);
	}

	public boolean ModificarMotivosAnulacionCondonacionMultas(int concecutivo, String codigo,
			String descripcion, boolean check, String codigoinstitucion) {
	
		return SqlBaseMotivosAnulacionCondonacionDao.ModificarMotivosAnulacionCondonacionMultas( concecutivo, codigo,  descripcion, check, codigoinstitucion);
	}

	@Override
	public boolean consultarEliminacion(int consecutivo) {
		
		return SqlBaseMotivosAnulacionCondonacionDao.consultarEliminacion(consecutivo);
	}

}