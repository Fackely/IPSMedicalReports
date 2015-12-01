/*
 * Created on 5/10/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;

import com.princetonsa.dto.tesoreria.DtoDevolRecibosCaja;

/**
 * @version 1.0, 5/10/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public interface ConsultaReciboCajaDao
{

    
    /**
     * Metodo que realiza la busqueda avanzada d elos recibos de caja.
     * @param fechaInicial
     * @param fechaFinal
     * @param institucion
     * @param numeroRecibosCajaInicial
     * @param numeroRecibosCajaFinal
     * @param buscarRecibosCaja
     * @param conceptoReciboCaja
     * @param busquedaConceptos
     * @param estadoReciboCaja
     * @param busquedaEstado
     * @param usuario
     * @param busquedaUsuario
     * @param caja
     * @param busquedaCaja
     * @param buscarFormaPago 
     * @param formaPago 
     * @return
     */
    public abstract HashMap busquedaAvanzada(String fechaInicial, String fechaFinal, int institucion, String numeroRecibosCajaInicial, 
            								String numeroRecibosCajaFinal, boolean buscarRecibosCaja, String conceptoReciboCaja, 
            								boolean busquedaConceptos, int estadoReciboCaja, boolean busquedaEstado, String usuario, 
            								boolean busquedaUsuario, int caja, boolean busquedaCaja, int formaPago, boolean buscarFormaPago, int codigoCentroAtencion,
            								String docSoporte, String tipoIdBeneficiario, String numeroIdBeneficiario);

    
    /**
     * @param institucion
     * @param numeroReciboCaja
     * @return
     */
    public abstract HashMap consultarConceptosReciboCaja(int institucion, String numeroReciboCaja);


    
    /**
     * @param institucion
     * @param numeroReciboCaja
     * @return
     */
    public abstract HashMap consultarFormasPagoReciboCaja(int institucion, String numeroReciboCaja);


    
    /**
     * @param institucion
     * @param numeroReciboCaja
     * @return
     */
    public abstract HashMap consultarAnulacionReciboCaja(int institucion, String numeroReciboCaja);


    
    /**
     * @param institucion
     * @param numeroReciboCaja
     * @return
     */
    public abstract HashMap consultarTotalesPagos(int institucion, String numeroReciboCaja);
    
    /**
     * metodo para generar la busqueda avanzada
     * de consulta anulaciones recibos de caja
     * @param con Connection
     * @param vo HashMap
     * @return HashMap 
     * @author jarloc
     */
    public abstract HashMap ejecutarBusquedaAvanzadaAnulacionesRC(Connection con, HashMap vo);

    /**
     * Consulta los recibos de caja para utilizarlos en las funcionalidades de cierre caja y arqueos
     * @param con
     * @param codigoInstitucion, codigoInstitucion
     * @param fechaDDMMYYYY, aca va la fecha generacion del arqueo o gecha generacion cierre
     * @param loginUsuarioCajero, login del usuario/cajero al que se va ha realizar  el arqueo o cierre 
     * @param consecutivoCaja, caja a la cual se le esta realizando el cierre o arqueo 
     * @param restriccionEstadosSeparadosXComas, restricciones por los estados eje 1,4 recaudado-anulado
     * @param loginUsuarioGenera
     * @param fechaGeneracionConsulta
     * @param horaGeneracionConsulta
     * @param bloquearRegistro, LE COLOCA UN SELECT FOR UPDATE EN CASO DE QUE VENGA EN TRUE, TENGA 
     * EN CUENTA QUE DEBE VENIR CONNECTION EN UNA TRANSACCION CUANDO bloquearRegistro=TRUE
     * @param consecutivoArqueoOCierre. en caso de que no sea vacio "" entonces busca por el consecutivo arqueo
     * @param codigoTipoArqueo
     * @return
     */
	public HashMap consultaRecibosCaja(	Connection con, 
										int codigoInstitucion, 
										String fechaDDMMYYYY, 
										String loginUsuarioCajero, 
										String consecutivoCaja, 
										String restriccionEstadosSeparadosXComas,
										String loginUsuarioGenera,
										String fechaGeneracionConsulta,
										String horaGeneracionConsulta,
										boolean bloquearRegistro,
										String consecutivoArqueoOCierre,
										int codigoTipoArqueo,
										String consecutivoCajaPpal);
	
	/**
	 * obtiene el codigo - nombre del centro de atencion de un recibo de caja
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public InfoDatosInt getCentroAtencionReciboCaja( Connection con, String numeroReciboCaja, int codigoInstitucion);
	
	/**
	 * 
	 */
	public ArrayList<DtoDevolRecibosCaja> consultarDevolucionesAprobadas(int codigoInstitucion,String numeroReciboCaja);
}
