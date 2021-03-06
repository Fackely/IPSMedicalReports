package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;

import com.sysmedica.dao.FichaBrotesDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaBrotesDao;

public class PostgresqlFichaBrotesDao implements FichaBrotesDao {

	private String secuenciaStr = "SELECT nextval('epidemiologia.seq_fichas')";
	
	public int insertarFicha(Connection con,
								String login,
								int estado,
							    
							    String sire,
								String loginUsuario,
							    int codigoFichaBrotes,
							    
							    int evento,
								String fechaNotificacion,
								String paisNotifica,
								int departamentoNotifica,
								int ciudadNotifica,
								String lugarNotifica,
								int unidadGeneradora,
								
								int pacientesGrupo1,
								int pacientesGrupo2,
								int pacientesGrupo3,
								int pacientesGrupo4,
								int pacientesGrupo5,
								int pacientesGrupo6,
								int probables,
								int confirmadosLaboratorio,
								int confirmadosClinica,
								int confirmadosNexo,
								int hombres,
								int mujeres,
								int vivos,
								int muertos,
								int departamentoProcedencia,
								int ciudadProcedencia,
								String lugarProcedencia,
								String nombreProfesional,
								String telefonoContacto,
								
								int muestraBiologica,
								String agenteEtiologico1,
								String alimentosImplicados,
								int muestraAlimentos,
								String agenteEtiologico2,
								String lugarConsumo,
								String observaciones,
								
								String fechaInvestigacion, 
								int muestraSuperficies,
								int estudioManipuladores, 
								int agenteBiologica1, 
								int agenteBiologica2, 
								int agenteBiologica3, 
								int agenteBiologica4, 
								int agenteAlimentos1, 
								int agenteAlimentos2, 
								int agenteAlimentos3, 
								int agenteAlimentos4, 
								int agenteSuperficies1, 
								int agenteSuperficies2, 
								int agenteSuperficies3, 
								int agenteSuperficies4, 
								int agenteManipuladores1,
								int agenteManipuladores2,
								int agenteManipuladores3,
								int agenteManipuladores4,
								int lugarConsumoImplicado,
								int factorDeterminante,
								int medidaSanitaria
													)
	{
		return SqlBaseFichaBrotesDao.insertarFicha(con,
													login,
													estado,
												    secuenciaStr,
												    
												    sire,
													loginUsuario,
												    codigoFichaBrotes,
												    
												    evento,
													fechaNotificacion,
													paisNotifica,
													departamentoNotifica,
													ciudadNotifica,
													lugarNotifica,
													unidadGeneradora,
													
													pacientesGrupo1,
													pacientesGrupo2,
													pacientesGrupo3,
													pacientesGrupo4,
													pacientesGrupo5,
													pacientesGrupo6,
													probables,
													confirmadosLaboratorio,
													confirmadosClinica,
													confirmadosNexo,
													hombres,
													mujeres,
													vivos,
													muertos,
													departamentoProcedencia,
													ciudadProcedencia,
													lugarProcedencia,
													nombreProfesional,
													telefonoContacto,
													
													muestraBiologica,
													agenteEtiologico1,
													alimentosImplicados,
													muestraAlimentos,
													agenteEtiologico2,
													lugarConsumo,
													observaciones,
													
													fechaInvestigacion, 
													muestraSuperficies,
													estudioManipuladores, 
													agenteBiologica1, 
													agenteBiologica2, 
													agenteBiologica3, 
													agenteBiologica4, 
													agenteAlimentos1, 
													agenteAlimentos2, 
													agenteAlimentos3, 
													agenteAlimentos4, 
													agenteSuperficies1, 
													agenteSuperficies2, 
													agenteSuperficies3, 
													agenteSuperficies4, 
													agenteManipuladores1,
													agenteManipuladores2,
													agenteManipuladores3,
													agenteManipuladores4,
													lugarConsumoImplicado,
													factorDeterminante,
													medidaSanitaria
													);
	}


	public int modificarFicha(Connection con,
								String sire,
								String loginUsuario,
							    int codigoFichaBrotes,
							    int estado,
							    
							    int evento,
								String fechaNotificacion,
								String paisNotifica,
								int departamentoNotifica,
								int ciudadNotifica,
								String lugarNotifica,
								int unidadGeneradora,
								
								int pacientesGrupo1,
								int pacientesGrupo2,
								int pacientesGrupo3,
								int pacientesGrupo4,
								int pacientesGrupo5,
								int pacientesGrupo6,
								int probables,
								int confirmadosLaboratorio,
								int confirmadosClinica,
								int confirmadosNexo,
								int hombres,
								int mujeres,
								int vivos,
								int muertos,
								int departamentoProcedencia,
								int ciudadProcedencia,
								String lugarProcedencia,
								String nombreProfesional,
								String telefonoContacto,
								
								int muestraBiologica,
								String agenteEtiologico1,
								String alimentosImplicados,
								int muestraAlimentos,
								String agenteEtiologico2,
								String lugarConsumo,
								String observaciones,
								
								String fechaInvestigacion, 
								int muestraSuperficies,
								int estudioManipuladores, 
								int agenteBiologica1, 
								int agenteBiologica2, 
								int agenteBiologica3, 
								int agenteBiologica4, 
								int agenteAlimentos1, 
								int agenteAlimentos2, 
								int agenteAlimentos3, 
								int agenteAlimentos4, 
								int agenteSuperficies1, 
								int agenteSuperficies2, 
								int agenteSuperficies3, 
								int agenteSuperficies4, 
								int agenteManipuladores1,
								int agenteManipuladores2,
								int agenteManipuladores3,
								int agenteManipuladores4,
								int lugarConsumoImplicado,
								int factorDeterminante,
								int medidaSanitaria
							)
	{
		return SqlBaseFichaBrotesDao.modificarFicha(con,
														sire,
														loginUsuario,
													    codigoFichaBrotes,
													    estado,
													    
													    evento,
														fechaNotificacion,
														paisNotifica,
														departamentoNotifica,
														ciudadNotifica,
														lugarNotifica,
														unidadGeneradora,
														
														pacientesGrupo1,
														pacientesGrupo2,
														pacientesGrupo3,
														pacientesGrupo4,
														pacientesGrupo5,
														pacientesGrupo6,
														probables,
														confirmadosLaboratorio,
														confirmadosClinica,
														confirmadosNexo,
														hombres,
														mujeres,
														vivos,
														muertos,
														departamentoProcedencia,
														ciudadProcedencia,
														lugarProcedencia,
														nombreProfesional,
														telefonoContacto,
														
														muestraBiologica,
														agenteEtiologico1,
														alimentosImplicados,
														muestraAlimentos,
														agenteEtiologico2,
														lugarConsumo,
														observaciones,
														
														fechaInvestigacion, 
														muestraSuperficies,
														estudioManipuladores, 
														agenteBiologica1, 
														agenteBiologica2, 
														agenteBiologica3, 
														agenteBiologica4, 
														agenteAlimentos1, 
														agenteAlimentos2, 
														agenteAlimentos3, 
														agenteAlimentos4, 
														agenteSuperficies1, 
														agenteSuperficies2, 
														agenteSuperficies3, 
														agenteSuperficies4, 
														agenteManipuladores1,
														agenteManipuladores2,
														agenteManipuladores3,
														agenteManipuladores4,
														lugarConsumoImplicado,
														factorDeterminante,
														medidaSanitaria);
	}



	public ResultSet consultaTodoFicha(Connection con, int codigo)
	{
		return SqlBaseFichaBrotesDao.consultarTodoFichaBrotes(con,codigo);
	}
}
