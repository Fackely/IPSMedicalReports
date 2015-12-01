package com.princetonsa.dao.carterapaciente;

import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBasePazYSalvoCarteraPaciente;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.dto.carteraPaciente.DtoPazYSalvoCarteraPaciente;


public interface PazYSalvoCarteraPacienteDao{
	
	/**
	 * Consulta los tipod de identificacion
	 */
	ArrayList<HashMap> consultarTiposId();
	
	/**
	 * Consulta los documentos por deudor segun criterios ingresados 
	 */
	ArrayList<DtoPazYSalvoCarteraPaciente> consultarDocPorDeudor(HashMap criterios);

	int insertarPazYSalvo(DtoPazYSalvoCarteraPaciente dto, String usuario);
	
	DtoPazYSalvoCarteraPaciente consultarPazYSalvo(int codigopk);

	boolean actualizarDocumentoGarantia(int ingreso, String consecutivo,String anioConsecutivo, String tipoDocumento);
}