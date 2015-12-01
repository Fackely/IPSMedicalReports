package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.CentrosCostoEntidadesSubcontratadasDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * 
 * @author Angela María Angel amangel@axioma-md.com
 *
 */

public class CentrosCostoEntidadesSubcontratadas
{
	Logger logger = Logger.getLogger(CentrosCostoEntidadesSubcontratadas.class);
	
	public static String[] indicesCentrosCostoEntiSub = {
		"nombreCentroCosto_",
		"nombreEntidadSub_",
		"nroPrioridad_",
		"respOtrosUsuarios_"
	};
	
	private static CentrosCostoEntidadesSubcontratadasDao getCentrosCostoEntidadesSubcontratadasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCentrosCostoEntidadesSubcontratadasDao();
	}
	
	public static HashMap consultarCentrosCostoEntiSub(int centroAtencion) {
		
		return getCentrosCostoEntidadesSubcontratadasDao().consultarCentrosCostoEntiSub(centroAtencion);
	}	
	
	public static int insertarNuevoRegistro(int centroCosto, int entidadSub, String prioridad, String respOtrosUsu, UsuarioBasico usuario)
	{	
		HashMap criterios = new HashMap();
			
		criterios.put("centroCosto", centroCosto);
		criterios.put("entidadSubcontratada", entidadSub);
		criterios.put("nroPrioridad", prioridad);
		criterios.put("respOtrosUsuarios", respOtrosUsu);
		criterios.put("usuario", usuario.getLoginUsuario());
		criterios.put("institucion", usuario.getCodigoInstitucionInt());
		
		return getCentrosCostoEntidadesSubcontratadasDao().insertarNuevoRegistro(criterios);
	}
	
	public static boolean actualizarCentroCostoEntiSub(String prioridad, String respOtrosUsu, UsuarioBasico usuario, int consecutivo)
	{
		HashMap criterios = new HashMap();
			
		criterios.put("nroPrioridad", prioridad);
		criterios.put("respOtrosUsuarios", respOtrosUsu);
		criterios.put("usuario", usuario.getLoginUsuario());
		criterios.put("consecutivo", consecutivo);
		
		return getCentrosCostoEntidadesSubcontratadasDao().actualizarCentroCostoEntiSub(criterios);
	}
	
	public boolean eliminarCentroCostoEntiSub(int consecutivo)
	{		
		return getCentrosCostoEntidadesSubcontratadasDao().eliminarCentroCostoEntiSub(consecutivo);
	}
	
	public boolean guardarLogCentrosCostoEntiSub (String centroCosto, String entidadSubcontratada, String eliminar, String nroPrioridadAnterior, String nroPrioridadNueva, String respOtrosUsuariosAnterior, String respOtrosUsuariosNueva, UsuarioBasico usuario)
	{
		HashMap criterios= new HashMap();
				
		criterios.put("centroCosto", Utilidades.convertirAEntero(centroCosto));
		criterios.put("entidadSubcontratada", Utilidades.convertirAEntero(entidadSubcontratada));
		criterios.put("eliminar", eliminar);
		criterios.put("nroPrioridadAnterior", nroPrioridadAnterior);
		criterios.put("nroPrioridadNueva", nroPrioridadNueva);
		criterios.put("respOtrosUsuariosAnterior", respOtrosUsuariosAnterior);
		criterios.put("respOtrosUsuariosNueva", respOtrosUsuariosNueva);
		criterios.put("usuario", usuario.getLoginUsuario());		
		
		return getCentrosCostoEntidadesSubcontratadasDao().guardarLogCentrosCostoEntiSub(criterios);
	}
	
	public static HashMap obtenerPrioridadCentrosCostoEntiSub(int centroAtencion) {
		
		return getCentrosCostoEntidadesSubcontratadasDao().obtenerPrioridadCentrosCostoEntiSub(centroAtencion);
	}
	
	public HashMap consultarEntidadesSubSinInterna(Connection con, int codigoEntidadSubInterna){
		return getCentrosCostoEntidadesSubcontratadasDao().consultarEntidadesSubSinInterna(con, codigoEntidadSubInterna);
	}
}