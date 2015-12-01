package com.princetonsa.mundo.consultaExterna;



import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.consultaExterna.MotivosAnulacionCondonacionDao;




public class MotivosAnulacionCondonacion {
	
	private MotivosAnulacionCondonacionDao motivosAnulacionCondonacionDao;
	public String[] indicesListado={
	"codigo_",
	"descripcion_",
	"activo_",
	};
	
		
	private static MotivosAnulacionCondonacionDao getMotivosAnulacionCondonacionDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosAnulacionCondonacionDao();
	}
		  
	public HashMap consultarMotivosAnulacionCondonacionMultas() {
	
		return getMotivosAnulacionCondonacionDao().consultarMotivosAnulacionCondonacionMultas();
	}

	public boolean eliminarMotivosAnulacionCondonacionMultas(int concecutivo) {
	
		return getMotivosAnulacionCondonacionDao().EliminarMotivosAnulacionCondonacionMultas(concecutivo);
		
	}


	public boolean InsertarMotivosAnulacionCondonacionMulta( String codigo, String descripcion,boolean check, String codigoinstitucion) {
			
 		return getMotivosAnulacionCondonacionDao().InsertarMotivosAnulacionCondonacionMultas(codigo, descripcion, check,codigoinstitucion);
		
	}

	public boolean ModificarMotivosAnulacionCondonacionMulta( int concecutivo, String codigo,
			String descripcion, boolean check, String codigoinstitucion) {
		
		return getMotivosAnulacionCondonacionDao().ModificarMotivosAnulacionCondonacionMultas(concecutivo, codigo, descripcion, check, codigoinstitucion);
		
	} 

	public void setCodigo(String codigo) {
	
		
	}

	public boolean consultarEliminacion(int consecutivo) {
		
		return getMotivosAnulacionCondonacionDao().consultarEliminacion(consecutivo);
		
	}
}


