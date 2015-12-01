package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;

import com.sysmedica.dao.FichaSivimDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaSivimDao;

public class PostgresqlFichaSivimDao implements FichaSivimDao {

	private String secuenciaStr = "SELECT nextval('epidemiologia.seq_fichas')";
	
	public int insertarFicha(Connection con,
								String login,
								int codigoPaciente,
								String codigoDiagnostico,
								int estado,
								String nombreProfesional)
	{
		return SqlBaseFichaSivimDao.insertarFicha(con,
													login,
													codigoPaciente,
													codigoDiagnostico,
													estado,
													nombreProfesional,
												    secuenciaStr);
	}
	
	
	
	public int modificarFicha(Connection con,
								String loginUsuario,
							    int codigoFichaSivim,
							    int estado,
							    
							    int escolaridad,
							    boolean mujerGestante,
							    boolean habitanteCalle,
							    boolean personaDiscapacitada,
							    int tipoAtencion,
							    boolean solo,
							    boolean padre,
							    boolean madre,
							    boolean padrastro,
							    boolean madrastra,
							    boolean hermanos,
							    boolean conyuge,
							    boolean hijos,
							    boolean abuelos,
							    boolean otros,
							    boolean violenciaFisica,
							    boolean violenciaEmocional,
							    boolean violenciaSexual,
							    boolean violenciaEconomica,
							    boolean violenciaNegligencia,
							    boolean violenciaAbandono,
							    boolean ocurrioAntesFisica,
							    boolean ocurrioAntesEmocional,
							    boolean ocurrioAntesSexual,
							    boolean ocurrioAntesEconomica,
							    boolean ocurrioAntesNegligencia,
							    boolean ocurrioAntesAbandono,
							    int lugarFisica,
							    int lugarEmocional,
							    int lugarSexual,
							    int lugarEconomica,
							    int lugarNegligencia,
							    int lugarAbandono,
							    String observaciones,
							    
							    String lugarProcedencia,
							    String fechaConsultaGeneral,
							    int unidadGeneradora
							)
	{
		return SqlBaseFichaSivimDao.modificarFicha(con,
													loginUsuario,
												    codigoFichaSivim,
												    estado,
												    
												    escolaridad,
												    mujerGestante,
												    habitanteCalle,
												    personaDiscapacitada,
												    tipoAtencion,
												    solo,
												    padre,
												    madre,
												    padrastro,
												    madrastra,
												    hermanos,
												    conyuge,
												    hijos,
												    abuelos,
												    otros,
												    violenciaFisica,
												    violenciaEmocional,
												    violenciaSexual,
												    violenciaEconomica,
												    violenciaNegligencia,
												    violenciaAbandono,
												    ocurrioAntesFisica,
												    ocurrioAntesEmocional,
												    ocurrioAntesSexual,
												    ocurrioAntesEconomica,
												    ocurrioAntesNegligencia,
												    ocurrioAntesAbandono,
												    lugarFisica,
												    lugarEmocional,
												    lugarSexual,
												    lugarEconomica,
												    lugarNegligencia,
												    lugarAbandono,
												    observaciones,
												    
												    lugarProcedencia,
												    fechaConsultaGeneral,
												    unidadGeneradora);
	}
	
	
	
	public ResultSet consultarTodoFichaSivim(Connection con, int codigo)
	{
		return SqlBaseFichaSivimDao.consultarTodoFichaSivim(con,codigo);
	}
}
