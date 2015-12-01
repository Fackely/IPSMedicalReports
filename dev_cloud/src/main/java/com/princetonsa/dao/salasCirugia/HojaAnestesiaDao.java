package com.princetonsa.dao.salasCirugia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatos;

import com.servinte.axioma.dto.salascirugia.HojaAnestesiaDto;
import com.servinte.axioma.fwk.exception.BDException;

public interface HojaAnestesiaDao
{		
	/**
	 * Insertar Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean insertarEspecialidadesIntervienen(Connection con,HashMap parametros);
	
	/**
	 * Consultar Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarEspecialidadesIntervienen(Connection con,HashMap parametros);
	
	/**
	 * Actualizar Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarEspecialidadesIntervienen(Connection con,HashMap parametros);
	
	/**
	 * Eliminar Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean eliminarEspecialidadesIntervienen(Connection con,HashMap parametros);
	
	
	/**
	 * Insertar Cirujanos Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean insertarCirujanosIntervienen(Connection con, HashMap parametros);
	
	/**
	 * Consultar Cirujanos Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarCirujanosIntervienen(Connection con,HashMap parametros);
	
	
	/**
	 * Actualizar Cirujanos Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarCirujanosIntervienen(Connection con,HashMap parametros);
	
	/**
	 * Eliminar Especialidades Intervienen
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean eliminarCirujanosIntervienen(Connection con,HashMap parametros);
	
	
	
	/**
	 * Método para saber si la Hoja de anestesia o Hoja Quirúrgica está en estado finalizada y si esta Creada
	 * @param con -> conexion
	 * @param nroSolicitud
	 * @param consultarHojaQx true = hojaQuirurgica, false = hojaAnestesia
	 * @return InfoDatos. acronimo = finalizada (S,N), descripcion = creada (S,N)
	 */
	public InfoDatos esFinalizadaCreadaHoja(Connection con, int nroSolicitud, boolean consultarHojaQx);
	
	
	/**
	 * Insertar Anestesiologos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean insertarAnestesiologos(Connection con,HashMap parametros);
	
	/**
	 * Consultar los Anestesiologos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarAnestesiologos(Connection con,HashMap parametros);
	
	/**
	 * Actualizar Anestesiologos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarAnestesiologos(Connection con,HashMap parametros);
	
	
	/**
	 * Consulta si existe o no el Anestesiologo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean consultarExisteAnestesiologo(Connection con,HashMap parametros);	
	
	/**
	 * Actualizar el campo cobrable de la Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarCobrable(Connection con,HashMap parametros);
	
	/**
	 * Actualizar el campo definitivo para el cobro de honorarios
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarDefinitivoHonorarios(Connection con,HashMap parametros);
	
	/**
	 * Consultar la informacion de la Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public  HashMap consultarHojaAnestesia(Connection con,HashMap parametros);
	
	/**
	 * Metodo que consulta los datos de la hoja de anestesia relacionada a un numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 19/07/2013
	 */
	public HojaAnestesiaDto consultarHojaAnestesia(Connection con, int numeroSolicitud)throws BDException;
	
	
	/**
	 * Insertar Hoja de Anestesia 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean insertarHojaAnestesia(Connection con,HashMap parametros);
	
	/**
	 * Actualizar la fecha y la hora de ingreso a la Sala
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarFechaHoraIngreso(Connection con,HashMap parametros);
	
	/**
	 * Consultar Observaciones Generales
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarObservacionesGenerales(Connection con,HashMap parametros);
	
	/**
	 * Insertar Observaciones Generales
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean insertarObservacionesGenerales(Connection con,HashMap parametros);
	
	/**
	 * Insertar Administraciones Hoja de Anestesia
	 * @param Connection con
	 * @param String insertarAdminHojaAnes
	 * @param HashMap parametros
	 * @return int numero del consecutivo con que se genero el registro 
	 * */
	public int insertarAdminisHojaAnest(Connection con,HashMap parametros);
	
	/**
	 * Consultar Administraciones Hoja Anestesia 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarAdminisHojaAnest(Connection con,HashMap parametros);
	
	/**
	 * Insertar detalle de los medicamentos administrados
	 * @param Connection con
	 * @param String strInsertarDetAdminHojaAnestesia
	 * @param HashMap parametros
	 * */
	public boolean insertarDetaAdminisHojaAnest(Connection con,HashMap parametros);
	
	/**
	 * Consultar el detalle de Administraciones Hoja Anestesia 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarDetAdminisHojaAnest(Connection con,HashMap parametros);
	
	/**
	 * Actualizar detalle de los medicamentos administrados
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarDetaAdminisHojaAnest(Connection con,HashMap parametros);
	
	
	/**
	 * Consultar los Articulos de la Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList consultarArticulosHojaAnestesia(Connection con,HashMap parametros);
	
	
	/**
	 * Consultar Infusiones Hoja Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarInfusionesHojaAnestesia(Connection con,HashMap parametros);
	
	/**
	 * Consultar Infusiones Agrupadas por mezcla y articulo 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarInfusionesAgrupadas(Connection con,HashMap parametros);
	
	/**
	 * Insertar Infusiones Hoja de Anestesia
	 * @param Connection con
	 * @param String strInsertarInfusionesHojaAnestesia
	 * @param HashMap parametros
	 * */
	public int insertarInfusionesHojaAnes(Connection con,HashMap parametros);
	
	/**
	 * Actualiza la informacion de infusiones de la Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarInfusionesHojaAnes(Connection con, HashMap parametros);
	
	/**
	 * Consulta la informacion de las administraciones de las infusiones de la hoja de anestesia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultarAdmInfusionesHojaAnes(Connection con, HashMap parametros);
	
	
	/**
	 * Actualiza la informacion de las administraciones de la hoja de anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarAdmInfusionesHojaAnes(Connection con, HashMap parametros);
	
	/**
	 * Actualiza el indicador de genero consumo para los articulos de un mezcla
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarGenConsuArtiMezcla(Connection con, HashMap parametros);
	
	
	/**
	 * Insertar Admisiones Infusiones Hoja de Anestesia
	 * @param Connection con
	 * @param String strInsertarAdmInfusionesHojaAnestesia
	 * @param HashMap parametros
	 * */
	public int insertarAdmInfusionesHojaAnes(Connection con,HashMap parametros);
	
	
	/**
	 * Actualiza el indicativo de genero consumo del detalle de los medicamentos administrados
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarGenConsumoDetaAdminis(Connection con,HashMap parametros);
	
	/**
	 * Consulta la informacion de los detalle de  administraciones de las infusiones de la hoja de anestesia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultarDetInfusionesHojaAnes(Connection con, HashMap parametros);
	
	
	/**
	 * Insertar Detalle de Admisiones Infusiones Hoja de Anestesia
	 * @param Connection con
	 * @param String strInsertarDetInfusionesHojaAnestesia
	 * @param HashMap parametros
	 * */
	public int insertarDetInfusionesHojaAnes(Connection con,HashMap parametros);
	
	
	/**
	 * Actualiza la informacion del Detalle de la administracion de infusiones de la Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarDetInfusionesHojaAnes(Connection con, HashMap parametros);
	
	
	/**
	 * Consultar las Mezclas parametrizadas en el sistema
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList consultarMezclas(Connection con,HashMap parametros);
	
	
	/**
	 * Consultar la informacion de los articulos asociados a una mezcla
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarArticulosMezcla(Connection con,HashMap parametros);	
	
	/**
	 * Insertar Hoja Anestesia Balance Liquidos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean insertarBalancesLiquidosHojaAnes(Connection con,HashMap parametros);
	
	/**
	 * Consulta la informacion de la Hoja de Anestesia Balance Liquidos
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultarBalanceLiquidos(Connection con, HashMap parametros);
	
	/**
	 * Actualiza la informacion de los balances de liquidos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarBalancesLiquidos(Connection con, HashMap parametros);
	
	/**
	 * Elimina la informacion de los balances de liquidos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean eliminarBalancesLiquidos(Connection con, HashMap parametros);
	
	/**
	 * Consultar otros Liquidos Balance Liquidos Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList consultarOtrosLiquidosBalanceLiq(Connection con,HashMap parametros);	
	
	/**
	 * Insertar Salidas de Paciente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int insertarSalidaSalaPaciente(Connection con,HashMap parametros);
	
	
	/**
	 * Consultar la informacion de la Hoja de Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarSalidasPaciente(Connection con,HashMap parametros);
	
	/**
	 * Consultar Salidas de paciente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList consultarSalidasPacienteInstCCosto(Connection con,HashMap parametros);
	
	
	/**
	 * Verifica si los servicios de la solicitud poseen indicativo requiere interpretacion en Si
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public String esRequerioInterServicioSoli(Connection con,HashMap parametros);
	
	
	/**
	 * Actualiza la informacion de la Hoja de Anestesia en la salida del paciente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarHojaAnestesiaSalidaPaciemte(Connection con, HashMap parametros);
	
	
	/**
	 * Consultar cantidades detalle via aerea
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarCantidadesDetalleViaAerea(Connection con,HashMap parametros);
	
	/**
	 * Actualizar el indicativo del detalle de la via aerea
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarGenConsuDetViaArea(Connection con,HashMap parametros);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param graficar
	 * @return
	 */
	public HashMap<Object, Object> cargarGraficaHojaAnestesia(Connection con, int numeroSolicitud, String graficar, boolean liquidos);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param graficar
	 * @return
	 */
	public HashMap<Object, Object> cargarGraficaInfusionesHA(Connection con, int numeroSolicitud, String graficar);
	
	
	/**
	 * Actualiza las cantidades del detalle de materiales qx
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarCantidadesDetMatQx(Connection con, HashMap parametros);

	/**
	 * Consultar el Indicativo de Registro
	 * 
	 * @param Connection con
	 * @param int numeroSolicitud
	 */
	public String consultarIndicativoRegistroDesde(Connection con,int numeroSolicitud);
	
	/**
	 * Actualizar Indicativo registro desde
	 * 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public boolean actualizarIndicativoRegistroDesde(Connection con, HashMap parametros);
	
	/**
	 * Consultar Cirujanos Intervienen solicitud
	 * @param Connection con
	 * @param String solicitud
	 * @param String especialidad
	 * */
	public HashMap consultarCirujanosSolicitud(Connection con,String solicitud);
}