package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

public interface ConsultaImpresionDevolucionDao
{
	public HashMap<String, Object> consultaCajas (Connection con);
	
	public HashMap<String, Object> consultaMotivos (Connection con);
	
	public HashMap<String, Object> consultaDevoluciones (Connection con, String fechaIni, String fechaFin, String devolucionI, String devolucionF, String motivoD, String estadoD, String tipoId, String numeroId, String centroAtencion, String caja);
	
	public HashMap<String, Object> consultaDetalleD (Connection con, String codigoD);
}