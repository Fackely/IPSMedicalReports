package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public interface FichaEasvDao {

	public int insertarFichaCompleta(Connection con,
						    		int numeroSolicitud,
									String login,
									int codigoPaciente,
									String codigoDiagnostico,
									int estado,
									int codigoAseguradora,
									String nombreProfesional,
								    
								    String sire,
									boolean notificar,
									
									int codigoFichaIntoxicacion,										    
								    String lugarProcedencia,
								    String fechaConsultaGeneral,
								    String fechaInicioSintomasGeneral,
								    int tipoCaso,
								    boolean hospitalizadoGeneral,
								    String fechaHospitalizacionGeneral,
								    boolean estaVivoGeneral,
								    String fechaDefuncion,
								    String lugarNoti,
								    int unidadGeneradora,
								    
								    int vacuna1,
								    int vacuna2,
								    int vacuna3,
								    int vacuna4,
								    int dosis1,
								    int dosis2,
								    int dosis3,
								    int dosis4,
								    int via1,
								    int via2,
								    int via3,
								    int via4,
								    int sitio1,
								    int sitio2,
								    int sitio3,
								    int sitio4,
								    String fechaVacunacion1,
								    String fechaVacunacion2,
								    String fechaVacunacion3,
								    String fechaVacunacion4,
								    String fabricante1,
								    String fabricante2,
								    String fabricante3,
								    String fabricante4,
								    String lote1,
								    String lote2,
								    String lote3,
								    String lote4,
								    String otroHallazgo,
								    String tiempo,
								    int unidadTiempo,
								    String lugarVacunacion,
								    String codDepVacunacion,
								    String codMunVacunacion,
								    int estadoSalud,
								    int recibiaMedicamentos,
								    String medicamentos,
								    int antPatologicos,
								    String cualesAntPatologicos,
								    int antAlergicos,
								    String cualesAntAlergicos,
								    int antEasv,
								    String cualesAntEasv,
								    int biologico1,
								    String fabricanteMuestra1,
								    String loteMuestra1,
								    String cantidadMuestra1,
								    String fechaEnvioMuestra1,
								    int biologico2,
								    String fabricanteMuestra2,
								    String loteMuestra2,
								    String cantidadMuestra2,
								    String fechaEnvioMuestra2,
								    int estadoFinal,
								    String telefonoContacto,
								    
								    boolean activa,
								    String pais,
								    int areaProcedencia,
								    
								    HashMap hallazgos,
								    HashMap vacunas,
								    String lugarVac
								   );
	
	
	

	public int modificarFicha(Connection con,
									String sire,
									boolean notificar,
								    String loginUsuario,
								    int codigoFichaEasv,
								    int codigoPaciente,
								    String codigoDiagnostico,
								    int codigoNotificacion,
								    int numeroSolicitud,
								    int estado,
								    
								    String lugarProcedencia,
								    String fechaConsultaGeneral,
								    String fechaInicioSintomasGeneral,
								    int tipoCaso,
								    boolean hospitalizadoGeneral,
								    String fechaHospitalizacionGeneral,
								    boolean estaVivoGeneral,
								    String fechaDefuncion,
								    String lugarNoti,
								    int unidadGeneradora,

								    int vacuna1,
								    int vacuna2,
								    int vacuna3,
								    int vacuna4,
								    int dosis1,
								    int dosis2,
								    int dosis3,
								    int dosis4,
								    int via1,
								    int via2,
								    int via3,
								    int via4,
								    int sitio1,
								    int sitio2,
								    int sitio3,
								    int sitio4,
								    String fechaVacunacion1,
								    String fechaVacunacion2,
								    String fechaVacunacion3,
								    String fechaVacunacion4,
								    String fabricante1,
								    String fabricante2,
								    String fabricante3,
								    String fabricante4,
								    String lote1,
								    String lote2,
								    String lote3,
								    String lote4,
								    String otroHallazgo,
								    String tiempo,
								    int unidadTiempo,
								    String lugarVacunacion,
								    String codDepVacunacion,
								    String codMunVacunacion,
								    int estadoSalud,
								    int recibiaMedicamentos,
								    String medicamentos,
								    int antPatologicos,
								    String cualesAntPatologicos,
								    int antAlergicos,
								    String cualesAntAlergicos,
								    int antEasv,
								    String cualesAntEasv,
								    int biologico1,
								    String fabricanteMuestra1,
								    String loteMuestra1,
								    String cantidadMuestra1,
								    String fechaEnvioMuestra1,
								    int biologico2,
								    String fabricanteMuestra2,
								    String loteMuestra2,
								    String cantidadMuestra2,
								    String fechaEnvioMuestra2,
								    int estadoFinal,
								    String telefonoContacto,
									String pais,
								    int areaProcedencia,
								    HashMap hallazgos,
								    HashMap vacunas,
								    String lugarVac
								    );
	
	
	
	
	public ResultSet consultaTodoFicha(Connection con, int codigo);
	
	
	
	
	public ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo);
	
	
	
	
	public ResultSet consultarHallazgos(Connection con, int codigo);
	
	
	
	
	public ResultSet consultarVacunas(Connection con, int codigo);
}
