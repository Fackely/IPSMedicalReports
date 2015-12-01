package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;

public interface FichaSivimDao {

	public int insertarFicha(Connection con,
									String login,
									int codigoPaciente,
									String codigoDiagnostico,
									int estado,
									String nombreProfesional);
	
	
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
									);
	
	
	public ResultSet consultarTodoFichaSivim(Connection con, int codigo);
}
