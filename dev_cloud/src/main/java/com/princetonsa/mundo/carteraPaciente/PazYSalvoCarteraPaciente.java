package com.princetonsa.mundo.carteraPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.carterapaciente.PazYSalvoCarteraPacienteDao;
import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBasePazYSalvoCarteraPaciente;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.dto.carteraPaciente.DtoPazYSalvoCarteraPaciente;
import com.princetonsa.mundo.UsuarioBasico;


public class PazYSalvoCarteraPaciente{
	
	Logger logger = Logger.getLogger(PazYSalvoCarteraPaciente.class);
	
	private static PazYSalvoCarteraPacienteDao getPazYSalvoCarteraPacienteDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPazYSalvoCarteraPacienteDao();
	}
	
	/**
	 * Consulta los tipod de identificacion
	 */
	public ArrayList<HashMap> consultarTiposId()
	{
		return getPazYSalvoCarteraPacienteDao().consultarTiposId();
	}
	/**
	 * Consulta los documentos por deudor segun criterios ingresados 
	 * @param string10 
	 * @param string9 
	 * @param string8 
	 * @param string7 
	 * @param string6 
	 * @param string5 
	 * @param string4 
	 * @param string3 
	 * @param string2 
	 * @param string 
	 */
	public ArrayList<DtoPazYSalvoCarteraPaciente> consultarDocPorDeudor(String tipoIdDeudor, String numIdDeudor, String nombreDeudor, String apellidoDeudor, String tipoIdPaciente, String numIdPaciente, String nombrePaciente, String apellidoPaciente, String codGarantia, String estadoGarantia, String numFactura)
	{
		HashMap criterios= new HashMap();
		
		criterios.put("tipoIdDeudor",tipoIdDeudor);
		criterios.put("numIdDeudor",numIdDeudor);
		criterios.put("nombreDeudor",nombreDeudor);
		criterios.put("apellidoDeudor",apellidoDeudor);
		criterios.put("tipoIdPaciente",tipoIdPaciente);
		criterios.put("numIdPaciente",numIdPaciente);
		criterios.put("nombrePaciente",nombrePaciente);
		criterios.put("apellidoPaciente",apellidoPaciente);
		criterios.put("codGarantia",codGarantia);
		criterios.put("estadoGarantia",estadoGarantia);
		criterios.put("numFactura",numFactura);			
		
		return getPazYSalvoCarteraPacienteDao().consultarDocPorDeudor(criterios);
	}

	public int generarPazYSalvo(DtoPazYSalvoCarteraPaciente dto, String usuario) 
	{
		int seq=getPazYSalvoCarteraPacienteDao().insertarPazYSalvo(dto, usuario);
		 if(seq > 0)
		 {			 
			 if(getPazYSalvoCarteraPacienteDao().actualizarDocumentoGarantia(dto.getDocumentosGarantia().getIngreso(),dto.getDocumentosGarantia().getConsecutivo(),dto.getDocumentosGarantia().getAnioConsecutivo(),dto.getDocumentosGarantia().getTipoDocumento()))
		 		 return seq;
		 }
		 return 0;
	}
	
	public DtoPazYSalvoCarteraPaciente consultarPazYSalvo(int codigopk)
	{
		return getPazYSalvoCarteraPacienteDao().consultarPazYSalvo(codigopk);
	}
}
