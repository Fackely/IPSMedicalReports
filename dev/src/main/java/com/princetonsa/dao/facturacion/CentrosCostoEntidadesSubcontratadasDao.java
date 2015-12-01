package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author Angela María Angel amangel@axioma-md.com
 *
 */

public interface CentrosCostoEntidadesSubcontratadasDao
{
	public HashMap consultarCentrosCostoEntiSub(int centroAtencion);
	
	public int insertarNuevoRegistro(HashMap criterios);
	
	public boolean actualizarCentroCostoEntiSub(HashMap criterios);
	
	public boolean eliminarCentroCostoEntiSub(int consecutivoeliminarCentroCostoEntiSub);
	
	public boolean guardarLogCentrosCostoEntiSub(HashMap criterios);
	
	public HashMap obtenerPrioridadCentrosCostoEntiSub(int centroAtencion);
	
	public HashMap consultarEntidadesSubSinInterna(Connection con, int codigoEntidadSubInterna);
} 
