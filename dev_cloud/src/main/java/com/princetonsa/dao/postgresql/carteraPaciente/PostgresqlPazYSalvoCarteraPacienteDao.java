package com.princetonsa.dao.postgresql.carteraPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.carterapaciente.PazYSalvoCarteraPacienteDao;
import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBasePazYSalvoCarteraPaciente;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.dto.carteraPaciente.DtoPazYSalvoCarteraPaciente;


public class PostgresqlPazYSalvoCarteraPacienteDao implements PazYSalvoCarteraPacienteDao{
	
	/**
	 * Consulta los tipod de identificacion
	 */
	public ArrayList<HashMap> consultarTiposId()
	{
		return SqlBasePazYSalvoCarteraPaciente.consultarTiposId();
	}
	
	/**
	 * Consulta los documentos por deudor segun criterios ingresados 
	 */
	public ArrayList<DtoPazYSalvoCarteraPaciente> consultarDocPorDeudor(HashMap criterios)
	{
		return SqlBasePazYSalvoCarteraPaciente.consultarDocPorDeudor(criterios);
	}
	
	public int insertarPazYSalvo(DtoPazYSalvoCarteraPaciente dto, String usuario)
	{
		return SqlBasePazYSalvoCarteraPaciente.insertarPazYSalvo(dto, usuario);
	}
	
	public DtoPazYSalvoCarteraPaciente consultarPazYSalvo(int codigopk)
	{
		return SqlBasePazYSalvoCarteraPaciente.consultarPazYSalvo(codigopk);
	}
	
	public boolean actualizarDocumentoGarantia(int ingreso, String consecutivo,String anioConsecutivo, String tipoDocumento)
	{
		return SqlBasePazYSalvoCarteraPaciente.actualizarDocumentoGarantia(ingreso, consecutivo, anioConsecutivo, tipoDocumento);
	}
}