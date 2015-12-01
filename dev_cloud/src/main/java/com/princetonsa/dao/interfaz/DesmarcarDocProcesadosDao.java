package com.princetonsa.dao.interfaz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.interfaz.DtoDocumentoDesmarcar;
import com.princetonsa.dto.interfaz.DtoLogInterfaz1E;

public interface DesmarcarDocProcesadosDao {

	ArrayList<DtoDocumentoDesmarcar> consultarDocumentosXtipoMvto(String tipoMov);
   
	//Ventas y Honorarios
	
	HashMap desmarcarFacturasPacientes(Connection con, HashMap parametros);

	HashMap desmarcarAnulacionFacturasPacientes(Connection con,HashMap parametrosBusqueda);

	HashMap desmarcarFacturasVarias(Connection con, HashMap parametrosBusqueda,boolean anulacion);

	HashMap desmarcarAjustesFacturasVarias(Connection con,HashMap parametrosBusqueda);

	HashMap desmarcarCuentasCobroCapitacion(Connection con,HashMap parametrosBusqueda);

	HashMap desmarcarAjustesCuentasCobroCapitacion(Connection con,HashMap parametrosBusqueda);
   
	// Recuados
	
	HashMap desmarcarRecibosdeCaja(Connection con, HashMap parametrosBusqueda);

	HashMap desmarcarAnulacionRecibosdeCaja(Connection con,HashMap parametrosBusqueda);
	
	HashMap desmarcarDevolucionRecibosdeCaja(Connection con,HashMap parametrosBusqueda);

	//Ajustes y Reclasificaciones
	
	HashMap desmarcarAjustesFacuturasPacientes(Connection con,HashMap parametrosBusqueda);
	
	HashMap desmarcarRegistroGlosas(Connection con, HashMap parametrosBusqueda);

	// Servicios Entidades Externas
	
	HashMap desmarcarAutorServEntidadesSub(Connection con, HashMap parametrosBusqueda, boolean anulacion);
	
	HashMap desmarcarDespachoMedicamentos(Connection con, HashMap parametrosBusqueda);

	HashMap desmarcarDevolucionMedicamentos(Connection con,	HashMap parametrosBusqueda);

	HashMap desmarcarDespachoPedidos(Connection con, HashMap parametrosBusqueda, boolean esQuirurgico);

	HashMap desmarcarDevolucionPedidos(Connection con,HashMap parametrosBusqueda, boolean esQuirurgico);

	HashMap desmarcarCargosDirectosArticulos(Connection con,HashMap parametrosBusqueda);

	HashMap desmarcarAnulacionCargosArticulos(Connection con,HashMap parametrosBusqueda);

	
	// Creacion del LOG Interfaz 1E
	HashMap guardarLogInterfaz1E(Connection con, DtoLogInterfaz1E dto);

	//Creacion del LOG Interfaz Tipos Documentos 1E
	HashMap guardarLogInterfazTiposDoc1E(Connection con, int codLogInterfaz, String tipoDocumento);

	

	

	

}
