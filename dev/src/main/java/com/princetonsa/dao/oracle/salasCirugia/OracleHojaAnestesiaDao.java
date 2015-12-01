package com.princetonsa.dao.oracle.salasCirugia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatos;

import com.princetonsa.dao.salasCirugia.HojaAnestesiaDao;
import com.princetonsa.dao.sqlbase.salasCirugia.SqlBaseHojaAnestesiaDao;
import com.servinte.axioma.dto.salascirugia.HojaAnestesiaDto;
import com.servinte.axioma.fwk.exception.BDException;


public class OracleHojaAnestesiaDao implements HojaAnestesiaDao
{	
	
	//**********************************************
	//Atributos*************************************
	//**********************************************
	
	
	private static String strInsertarHojaAnestesia = "INSERT INTO " +
			"cirujanos_esp_int_solcx (consecutivo,numero_solicitud,especialidad,profesional,asignada,usuario_modifica,fecha_modifica,hora_modifica) " +
			"VALUES(seq_cirujanos_esp_int.NEXTVAL,?,?,?,?,?,?,?)";
	
	private static String strInsertarObservacionesGenerales = "INSERT INTO " +
			"observaciones_hoja_anes(codigo,numero_solicitud,descripcion,fecha,hora,usuario) VALUES(seq_observa_hoja_anes.NEXTVAL,?,?,?,?,?)";
	
	private static String strInsertarAdminHojaAnestesia = "INSERT INTO admin_hoja_anestesia(codigo,numero_solicitud,articulo,tipo_liquido,otro_medicamento,cantidad,seccion) " +
			"VALUES(seq_admin_hoja_anes.NEXTVAL,?,?,?,?,?,?)";
	
	private static String strInsertarDetAdminHojaAnestesia = "INSERT INTO det_admin_hoja_anes(codigo,admin_hoja_anes,dosis,fecha,hora,graficar,genero_consumo,fecha_modifica,hora_modifica,usuario_modifica,sellocalidad) " +
	"VALUES(seq_admin_hoja_anes.NEXTVAL,?,?,?,?,?,?,?,?,?,?)";
	
	private static String strInsertarInfusionesHojaAnestesia = "INSERT INTO infusiones_hoja_anes (codigo,numero_solicitud,codigo_mezcla,otra_infusion,usuario_modifica,fecha_modifica,hora_modifica) " +
	"VALUES(seq_infusiones_hoja_anes.NEXTVAL,?,?,?,?,?,?)";
	
	private static String strInsertarAdmInfusionesHojaAnestesia = "INSERT INTO adm_infusiones_hoja_anes (codigo,cod_info_hoja_anes,suspendido,graficar,fecha,hora,usuario_modifica,fecha_modifica,hora_modifica) " +
	"VALUES(seq_adm_infusiones_hoja_anes.NEXTVAL,?,?,?,?,?,?,?,?)";
	
	private static String strInsertarDetInfusionesHojaAnestesia = "INSERT INTO det_infusiones_hoja_anes (codigo,cod_adm_info_hoja_anes,articulo,dosis,usuario_modifica,fecha_modifica,hora_modifica) " +
	"VALUES(seq_det_infu_hoja_anes.NEXTVAL,?,?,?,?,?,?)" ;
	
	
	
	//**********************************************
	//Metodos***************************************
	//**********************************************
	
	/**
	 * Método para saber si la Hoja de anestesia o Hoja Quirúrgica está en estado finalizada y si esta Creada
	 * @param con -> conexion
	 * @param nroSolicitud
	 * @param consultarHojaQx true = hojaQuirurgica, false = hojaAnestesia
	 * @return InfoDatos. acronimo = finalizada (S,N), descripcion = creada (S,N)
	 */
	public InfoDatos esFinalizadaCreadaHoja(Connection con, int nroSolicitud, boolean consultarHojaQx)
	{
		return SqlBaseHojaAnestesiaDao.esFinalizadaCreadaHoja(con, nroSolicitud, consultarHojaQx);
	}
	

	/**
	 * Consultar Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarEspecialidadesIntervienen(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarEspecialidadesIntervienen(con, parametros);
	}
	
	/**
	 * Insertar Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean insertarEspecialidadesIntervienen(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.insertarEspecialidadesIntervienen(con, parametros);
	}
	
	/**
	 * Actualizar Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarEspecialidadesIntervienen(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.actualizarEspecialidadesIntervienen(con, parametros);
	}
	
	/**
	 * Eliminar Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean eliminarEspecialidadesIntervienen(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.eliminarEspecialidadesIntervienen(con, parametros);
	}
	
	
	/**
	 * Insertar Cirujanos Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean insertarCirujanosIntervienen(Connection con, HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.insertarCirujanosIntervienen(con, strInsertarHojaAnestesia, parametros);
	}
	
	/**
	 * Consultar Cirujanos Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarCirujanosIntervienen(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarCirujanosIntervienen(con, parametros);
	}
	
	
	/**
	 * Actualizar Cirujanos Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarCirujanosIntervienen(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.actualizarCirujanosIntervienen(con, parametros);
	}
	
	/**
	 * Eliminar Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean eliminarCirujanosIntervienen(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.eliminarCirujanosIntervienen(con, parametros);
	}
	
	/**
	 * Insertar Anestesiologos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean insertarAnestesiologos(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.insertarAnestesiologos(con, parametros);
	}
	
	
	/**
	 * Consultar los Anestesiologos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarAnestesiologos(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarAnestesiologos(con, parametros); 
	}
	
	/**
	 * Actualizar Anestesiologos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarAnestesiologos(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.actualizarAnestesiologos(con, parametros);
	}
	
	/**
	 * Consulta si existe o no el Anestesiologo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean consultarExisteAnestesiologo(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarExisteAnestesiologo(con, parametros);
	}
	
	
	/**
	 * Actualizar el campo cobrable de la Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarCobrable(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.actualizarCobrable(con, parametros);
	}
	
	
	/**
	 * Actualizar el campo definitivo para el cobro de honorarios
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarDefinitivoHonorarios(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.actualizarDefinitivoHonorarios(con, parametros);
	}
	
	/**
	 * Consultar la informacion de la Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarHojaAnestesia(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarHojaAnestesia(con, parametros);
	}
	
	/**
	 * Insertar Hoja de Anestesia 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean insertarHojaAnestesia(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.insertarHojaAnestesia(con, parametros);
	}
	
	
	/**
	 * Actualizar la fecha y la hora de ingreso a la Sala
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarFechaHoraIngreso(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.actualizarFechaHoraIngreso(con, parametros);
	}
	
	
	/**
	 * Consultar Observaciones Generales
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarObservacionesGenerales(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarObservacionesGenerales(con, parametros);
	}
	
	
	/**
	 * Insertar Observaciones Generales
	 * @param Connection con
	 * @param String insertarObservaciones
	 * @param HashMap parametros
	 * */
	public boolean insertarObservacionesGenerales(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.insertarObservacionesGenerales(con, strInsertarObservacionesGenerales, parametros);
	}	
	
	/**
	 * Insertar Administraciones Hoja de Anestesia
	 * @param Connection con
	 * @param String insertarAdminHojaAnes
	 * @param HashMap parametros
	 * @return int numero del consecutivo con que se genero el registro
	 * */
	public int insertarAdminisHojaAnest(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.insertarAdminisHojaAnest(con,strInsertarAdminHojaAnestesia, parametros);
	}
	
	/**
	 * Consultar Administraciones Hoja Anestesia 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarAdminisHojaAnest(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarAdminisHojaAnest(con, parametros);		
	}
	
	/**
	 * Insertar detalle de los medicamentos administrados
	 * @param Connection con
	 * @param String strInsertarDetAdminHojaAnestesia
	 * @param HashMap parametros
	 * */
	public boolean insertarDetaAdminisHojaAnest(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.insertarDetaAdminisHojaAnest(con, strInsertarDetAdminHojaAnestesia, parametros);		
	}
	
	/**
	 * Consultar el detalle de Administraciones Hoja Anestesia 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarDetAdminisHojaAnest(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarDetAdminisHojaAnest(con, parametros);
	}
	
	/**
	 * Actualizar detalle de los medicamentos administrados
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarDetaAdminisHojaAnest(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.actualizarDetaAdminisHojaAnest(con, parametros);
	}
	
	
	/**
	 * Consultar los Articulos de la Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList consultarArticulosHojaAnestesia(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarArticulosHojaAnestesia(con, parametros);
	}
	
	
	/**
	 * Consultar Infusiones Hoja Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarInfusionesHojaAnestesia(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarInfusionesHojaAnestesia(con, parametros);
	}
	
	/**
	 * Actualiza el indicador de genero consumo para los articulos de un mezcla
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarGenConsuArtiMezcla(Connection con, HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.actualizarGenConsuArtiMezcla(con, parametros);
	}
	
	/**
	 * Insertar Infusiones Hoja de Anestesia
	 * @param Connection con
	 * @param String strInsertarInfusionesHojaAnestesia
	 * @param HashMap parametros
	 * */
	public int insertarInfusionesHojaAnes(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.insertarInfusionesHojaAnes(con, strInsertarInfusionesHojaAnestesia, parametros);
	}
	
	
	/**
	 * Actualiza la informacion de infusiones de la Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarInfusionesHojaAnes(Connection con, HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.actualizarInfusionesHojaAnes(con, parametros);
	}
	
	/**
	 * Consulta la informacion de las administraciones de las infusiones de la hoja de anestesia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultarAdmInfusionesHojaAnes(Connection con, HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarAdmInfusionesHojaAnes(con, parametros);
	}
	
	
	/**
	 * Actualiza la informacion de las administraciones de la hoja de anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarAdmInfusionesHojaAnes(Connection con, HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.actualizarAdmInfusionesHojaAnes(con, parametros);
	}
	
	
	/**
	 * Insertar Admisiones Infusiones Hoja de Anestesia
	 * @param Connection con
	 * @param String strInsertarAdmInfusionesHojaAnestesia
	 * @param HashMap parametros
	 * */
	public int insertarAdmInfusionesHojaAnes(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.insertarAdmInfusionesHojaAnes(con, strInsertarAdmInfusionesHojaAnestesia, parametros);
	}
	
	/**
	 * Consultar Infusiones Agrupadas por mezcla y articulo 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarInfusionesAgrupadas(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarInfusionesAgrupadas(con, parametros);
	}
	
	/**
	 * Actualiza el indicativo de genero consumo del detalle de los medicamentos administrados
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarGenConsumoDetaAdminis(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.actualizarGenConsumoDetaAdminis(con, parametros);
	}
	
	/**
	 * Consulta la informacion de los detalle de  administraciones de las infusiones de la hoja de anestesia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultarDetInfusionesHojaAnes(Connection con, HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarDetInfusionesHojaAnes(con, parametros);
	}
		
	/**
	 * Insertar Detalle de Admisiones Infusiones Hoja de Anestesia
	 * @param Connection con
	 * @param String strInsertarDetInfusionesHojaAnestesia
	 * @param HashMap parametros
	 * */
	public int insertarDetInfusionesHojaAnes(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.insertarDetInfusionesHojaAnes(con, strInsertarDetInfusionesHojaAnestesia, parametros);
	}
	
	
	/**
	 * Actualiza la informacion del Detalle de la administracion de infusiones de la Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarDetInfusionesHojaAnes(Connection con, HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.actualizarDetInfusionesHojaAnes(con, parametros);
	}
	
	
	/**
	 * Consultar las Mezclas parametrizadas en el sistema
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList consultarMezclas(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarMezclas(con, parametros);
	}
	
	
	/**
	 * Consultar la informacion de los articulos asociados a una mezcla
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarArticulosMezcla(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarArticulosMezcla(con, parametros);
	}
	
	
	/**
	 * Insertar Hoja Anestesia Balance Liquidos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean insertarBalancesLiquidosHojaAnes(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.insertarBalancesLiquidosHojaAnes(con, parametros);
	}
	
	
	/**
	 * Consulta la informacion de la Hoja de Anestesia Balance Liquidos
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultarBalanceLiquidos(Connection con, HashMap parametros)

	{
		return SqlBaseHojaAnestesiaDao.consultarBalanceLiquidos(con, parametros);
	}
	
	
	/**
	 * Actualiza la informacion de los balances de liquidos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarBalancesLiquidos(Connection con, HashMap parametros)

	{
		return SqlBaseHojaAnestesiaDao.actualizarBalancesLiquidos(con, parametros);
	}
	
	/**
	 * Elimina la informacion de los balances de liquidos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean eliminarBalancesLiquidos(Connection con, HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.eliminarBalancesLiquidos(con, parametros);
	}
	
	/**
	 * Consultar otros Liquidos Balance Liquidos Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList consultarOtrosLiquidosBalanceLiq(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarOtrosLiquidosBalanceLiq(con, parametros);
	}
	
	/**
	 * Insertar Salidas de Paciente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int insertarSalidaSalaPaciente(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.insertarSalidaSalaPaciente(con, parametros);
	}
	
	
	/**
	 * Consultar la informacion de la Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarSalidasPaciente(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarSalidasPaciente(con, parametros);
	}
	
	
	/**
	 * Consultar Salidas de paciente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList consultarSalidasPacienteInstCCosto(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarSalidasPacienteInstCCosto(con, parametros);
	}
	
	
	/**
	 * Verifica si los servicios de la solicitud poseen indicativo requiere interpretacion en Si
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public String esRequerioInterServicioSoli(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.esRequerioInterServicioSoli(con, parametros);
	}
	
	
	/**
	 * Actualiza la informacion de la Hoja de Anestesia en la salida del paciente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarHojaAnestesiaSalidaPaciemte(Connection con, HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.actualizarHojaAnestesiaSalidaPaciemte(con, parametros);
	}
	
	/**
	 * Consultar cantidades detalle via aerea
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarCantidadesDetalleViaAerea(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.consultarCantidadesDetalleViaAerea(con, parametros);
	}
	
	/**
	 * Actualizar el indicativo del detalle de la via aerea
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarGenConsuDetViaArea(Connection con,HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.actualizarGenConsuDetViaArea(con, parametros);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param graficar
	 * @return
	 */
	public HashMap<Object, Object> cargarGraficaHojaAnestesia(Connection con, int numeroSolicitud, String graficar, boolean liquidos)
	{
		return SqlBaseHojaAnestesiaDao.cargarGraficaHojaAnestesia(con, numeroSolicitud, graficar, liquidos);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param graficar
	 * @return
	 */
	public HashMap<Object, Object> cargarGraficaInfusionesHA(Connection con, int numeroSolicitud, String graficar)
	{
		return SqlBaseHojaAnestesiaDao.cargarGraficaInfusionesHA(con, numeroSolicitud, graficar);
	}
	
	/**
	 * Actualiza las cantidades del detalle de materiales qx
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarCantidadesDetMatQx(Connection con, HashMap parametros)
	{
		return SqlBaseHojaAnestesiaDao.actualizarCantidadesDetMatQx(con, parametros);
	}	
	
	/**
	 * Consultar el Indicativo de Registro
	 * 
	 * @param Connection con
	 * @param int numeroSolicitud
	 */
	public String consultarIndicativoRegistroDesde(Connection con,int numeroSolicitud){
		return SqlBaseHojaAnestesiaDao.consultarIndicativoRegistroDesde(con, numeroSolicitud);		
	}

	/**
	 * Actualizar Indicativo registro desde
	 * 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public boolean actualizarIndicativoRegistroDesde(Connection con, HashMap parametros) {
		return SqlBaseHojaAnestesiaDao.actualizarIndicativoRegistroDesde(con, parametros);
	}


	/**
	 * Consultar Cirujanos Intervienen solicitud
	 * @param Connection con
	 * @param String solicitud
	 * @param String especialidad
	 * */
	public HashMap consultarCirujanosSolicitud(Connection con, String solicitud) {
		return SqlBaseHojaAnestesiaDao.consultarCirujanosSolicitud(con, solicitud);
	}


	@Override
	public HojaAnestesiaDto consultarHojaAnestesia(Connection con,int numeroSolicitud) throws BDException {
		return SqlBaseHojaAnestesiaDao.consultarHojaAnestesia(con, numeroSolicitud);
	}
}