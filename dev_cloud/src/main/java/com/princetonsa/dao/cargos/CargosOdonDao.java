package com.princetonsa.dao.cargos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dto.odontologia.DtoProgramaServicio;

import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.InfoTarifa;
import util.odontologia.InfoBonoDcto;
import util.odontologia.InfoDctoOdontologicoPresupuesto;
import util.odontologia.InfoTarifaServicioPresupuesto;

import com.servinte.axioma.fwk.exception.BDException;

/**
 * 
 * @author axioma
 *
 */
public interface CargosOdonDao {

	/**
	 * Metodo para obtener el esquema tarifario ligado al presupuesto
	 * @param servicio
	 * @param contrato
	 * @param fechaCalculoVigencia
	 * @return
	 */
	public InfoDatosInt obtenerEsquemaTarifario(int servicio, int contrato, String fechaCalculoVigencia);
	
	/**
	 * 
	 * @param cuenta
	 * @return
	 */
	public HashMap<String, String> obtenerFiltrosPromociones(final BigDecimal cuenta);
	
	/**
	 * 
	 * @param ingreso
	 * @param convenio
	 * @return
	 */
	public int obtenerContrato(int ingreso, int convenio);

	/**
	 * Obtener 
	 * @param cuenta
	 * @param institucion
	 * @param valorTarifa
	 * @param programaExcluyente
	 * @param servicioExcluyente
	 * @param convenio
	 * @param fechaApp
	 * @return
	 */
	public InfoBonoDcto obtenerDescuentosBono(final int cuenta, final int institucion, BigDecimal valorTarifa, double programaExcluyente, int servicioExcluyente, final int convenio, String fechaApp);
	
	/*
	 * 
	 * @param convenio
	 * @param ingreso
	 * @return
	 * /
	public BigDecimal obtenerSerialBono(final int convenio, final int cuenta);*/
	
	/**
	 * 
	 * @param convenio
	 * @param programa Programa asociado al bono
	 * @param cuenta
	 * @return
	 */
	public boolean liberarSerialBono(Connection con, final int convenio, final int ingreso, BigDecimal serial, int programa);
	
	/**
	 * en la pos 1 codigodetalleDctoOdon
	 * en la pos 2 el valor calculado de descuento
	 * @param centroAtencion
	 * @param fechaCalculoVigencia
	 * @param valorPresupuestoXConvenio
	 * @return
	 */
	public InfoDctoOdontologicoPresupuesto obtenerDescuentoOdon(final int centroAtencion, final String fechaCalculoVigencia, BigDecimal valorPresupuesto);
	
	/**
	 * Método para cargar la información del presupuesto contratado de un servicio especifico
	 * @param con
	 * @param codigoPresuOdoProgSer
	 * @param infoRespCobertura
	 * @param infoTarifa
	 * @param utilizaProgramasInstitucion 
	 * @param servicio 
	 */
	public void obtenerInfoPresupuestoContratadoProgSer(Connection con,int codigoPresuOdoProgSer,InfoResponsableCobertura infoRespCobertura,InfoTarifaServicioPresupuesto infoTarifa, boolean utilizaProgramasInstitucion, int servicio, BigDecimal codigoPkPlanTratamiento);
	
	/**
	 * Método para saber si la cita procesda es la priemra cita del plan de tratamietno
	 * @param con
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public boolean esPrimeraCitaPlanTratamiento(Connection con,int codigoPlanTratamiento);
	
	/**
	 * Método para actualizar el número de pacientes atendidos por contrato
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public ResultadoBoolean actualizarNumeroPacientesAtendidosContrato(Connection con,int codigoContrato);

	/**
	 * 
	 * @param con
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param codigoContrato
	 * @param codigoPrograma
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia
	 * @return
	 */
	public InfoTarifa obtenerDescuentoComercialXConvenioPrograma(Connection con, int codigoViaIngreso, String tipoPaciente,int codigoContrato, Double codigoPrograma, int codigoInstitucion,String fechaCalculoVigencia) throws BDException;

	/**
	 * Retorna el código PK de la tabla presupuesto_odo_prog_serv
	 * @param con Conexión con la BD
	 * @param presupuesto Presupuesto asociado
	 * @param programa Programa buscado
	 * @return entero con el codigo_pk buscado, -1 de lo contrario
	 */
	public int obtenerPresupuestoOdoProgSer(Connection con, int presupuesto, DtoProgramaServicio programa);
}
