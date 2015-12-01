package com.princetonsa.dao.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import util.InfoPacienteBonoPresupuesto;

import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoEmisionBonosDesc;
import com.princetonsa.dto.odontologia.administracion.DtoBonoDescuento;

/**
 * 
 * @author axioma
 *
 */
public interface  EmisionBonosDescDao {
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double  guardar( DtoEmisionBonosDesc dto);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoEmisionBonosDesc> cargar(DtoEmisionBonosDesc dto);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean modificar(DtoEmisionBonosDesc dto);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean eliminar(DtoEmisionBonosDesc dto);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean  existeCruceSerialesIdYConvenio(DtoEmisionBonosDesc dto,  double codigoPkNotIn);

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean existeCruceFechasConvenioPrograma(DtoEmisionBonosDesc dto , double codigoPkNotIn);

	/**
	 * M�todo que valida si un serial de un bono es v�lido, verifica
	 * la vigencia y que el serian no halla sido utilizado
	 * @param con Conexi�n con la BD
	 * @param bono Serial del bono
	 * @param codigoConvenio C�digo del convenio
	 * @return true en caso de que el serial est� vigente y no se halla utilizado, false de lo contrario
	 */
	public boolean esSerialVigenteDisponible(Connection con, Integer bono, int codigoConvenio);
	

	/**
	 * 
	 * @param dto
	 * @param aplicaProgramas
	 * @return
	 */
	public  ArrayList<InfoPacienteBonoPresupuesto> consultaEmisionBonos(InfoPacienteBonoPresupuesto dto  , boolean aplicaProgramas );
	
	
	/**
	 * 
	 * @param serialInicial
	 * @param serialFinal
	 * @return
	 */
	public boolean existeSerialSubCuentas(BigDecimal serialInicial , BigDecimal serialFinal, int convenio);
	
	/**
	 * M�todo que valida si un serial est� vigente o disponible
	 * @param con
	 * @param bono
	 * @param codigoConvenio
	 * @param dtoSubcuentas 
	 * @return Mensaje de error, "" en caso de no existir error
 	 * @author Juan David
 	 */
	public String esSerialVigenteDisponibleMensajeError(Connection con, Integer bono, int codigoConvenio, DtoSubCuentas dtoSubcuentas);

	/**
	 * Marcar los bonos como utilizados = 'S' para que no sean utilizados de nuevo en el paciente.
	 * Adicionalmente genera una relaci�n entre el programa donde se utiliz� y el bono.
	 * @param con Conexi�n con la BD (Abierta)
	 * @param bono {@link DtoBonoDescuento} Bono que se desea asociar
	 * @return true en caso de hacer la modificaci�n correctamente
	 */
	public boolean marcarBonoUtilizado(Connection con, DtoBonoDescuento bono);

	/**
	 * Generar una relaci�n entre el programa donde se utiliz� y el bono.
	 * @param con Conexi�n con la BD (Abierta)
	 * @param bono {@link DtoBonoDescuento} Bono que se desea asociar
	 * @return true en caso de generar la relaci�n correctamente
	 */
	public boolean asociarBonoProgramaConvenio(Connection con, DtoBonoDescuento bono);

	
	
}
