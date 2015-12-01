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
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;

import com.princetonsa.dao.ConsultaReciboCajaDao;
import com.princetonsa.dao.sqlbase.SqlBaseConsultaRecibosCajaDao;
import com.princetonsa.dto.tesoreria.DtoDevolRecibosCaja;

/**
 * @version 1.0, 5/10/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class PostgresqlConsultaRecibosCajaDao implements ConsultaReciboCajaDao
{
 
    /**
     * Cadena para realizar la consulta de la anulacion de un recibo de caja.
     */
    private static String cadenaConsultaAnulacionReciboCaja="SELECT " +
                                                                "rc.consecutivo_recibo as consecutivorc," +
                                                                "arc.numero_recibo_caja as numero_recibo_caja," +
                                                                "to_number(arc.numero_anulacion_rc,'99999999999999999999') as numeroanulacion," +
                                                                "to_number(arc.consecutivo_anulacion,'99999999999999999999') as consecutivoanulacion," +
                                                                "arc.usuario as loginusuario," +
                                                        		"administracion.getnombremedico(u.codigo_persona) as nomusuario," +
                                                                "arc.fecha as fecha," +
                                                                "to_char(arc.hora, '"+ConstantesBD.formatoHora24BD+"') as hora," +
        														"arc.motivo_anulacion as codmotivo," +
                                                                "marc.descripcion as descmotivoanulacion," +
        														"arc.observaciones as observaciones," +
                                                                "getTotalReciboCaja(arc.numero_recibo_caja,arc.institucion) as valortotalrc," +
                                                                "rc.fecha as fechaelaboracionrc," +
                                                                "rc.hora as horaelaboracionrc," +
                                                                "rc.recibido_de as recibido_de," +
                                                                "getnomcentroatencion(c.centro_atencion) as nombrecentroatencion " +
    														"from anulacion_recibos_caja arc " +
    														"inner join usuarios u on(u.login=arc.usuario) " +
    														"inner join motivos_anulacion_rc marc on (marc.codigo=arc.motivo_anulacion) " +
                                                            "inner join recibos_caja rc on (rc.numero_recibo_caja=arc.numero_recibo_caja) " +
                                                            "inner join cajas c on (rc.caja= c.consecutivo) " ;

    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(PostgresqlConsultaRecibosCajaDao.class);
	        
    public HashMap busquedaAvanzada(String fechaInicial, String fechaFinal, int institucion, String numeroRecibosCajaInicial, 
			String numeroRecibosCajaFinal, boolean buscarRecibosCaja, String conceptoReciboCaja, 
			boolean busquedaConceptos, int estadoReciboCaja, boolean busquedaEstado, String usuario, 
			boolean busquedaUsuario, int caja, boolean busquedaCaja, int formaPago, boolean buscarFormaPago, int codigoCentroAtencion,
			String docSoporte, String tipoIdBeneficiario, String numeroIdBeneficiario)
    {
        Connection con=PostgresqlUtilidadesBDDao.abrirConexion();
        HashMap mapa=new HashMap();
        if(con==null)
        {
            logger.error("Error abriedo la conexion ");
        }
        else
        {
            mapa=SqlBaseConsultaRecibosCajaDao.busquedaAvanzada(con,fechaInicial,fechaFinal,institucion,numeroRecibosCajaInicial,numeroRecibosCajaFinal,
                    										buscarRecibosCaja,conceptoReciboCaja,busquedaConceptos,estadoReciboCaja,busquedaEstado,
                    										usuario,busquedaUsuario,caja,busquedaCaja,formaPago,buscarFormaPago, codigoCentroAtencion,
                    										docSoporte, tipoIdBeneficiario, numeroIdBeneficiario);
            PostgresqlUtilidadesBDDao.cerrarConexion(con);
        }
        return mapa;
    }
    
    /**
     * @param institucion
     * @param numeroReciboCaja
     * @return
     */
    public HashMap consultarConceptosReciboCaja(int institucion, String numeroReciboCaja)
    {
        Connection con=PostgresqlUtilidadesBDDao.abrirConexion();
        HashMap mapa=new HashMap();
        if(con==null)
        {
            logger.error("Error abriedo la conexion ");
        }
        else
        {
            mapa=SqlBaseConsultaRecibosCajaDao.consultarConceptosReciboCaja(con,institucion,numeroReciboCaja);
            PostgresqlUtilidadesBDDao.cerrarConexion(con);
        }
        return mapa;
    }
    
    
    /**
     * @param institucion
     * @param numeroReciboCaja
     * @return
     */
    public HashMap consultarFormasPagoReciboCaja(int institucion, String numeroReciboCaja)
    {
        Connection con=PostgresqlUtilidadesBDDao.abrirConexion();
        HashMap mapa=new HashMap();
        if(con==null)
        {
            logger.error("Error abriedo la conexion ");
        }
        else
        {
            mapa=SqlBaseConsultaRecibosCajaDao.consultarFormasPagoReciboCaja(con,institucion,numeroReciboCaja);
            PostgresqlUtilidadesBDDao.cerrarConexion(con);
        }
        return mapa;
    }
    
    /**
     * @param institucion
     * @param numeroReciboCaja
     * @return
     */
    public HashMap consultarAnulacionReciboCaja(int institucion, String numeroReciboCaja)
    {
        Connection con=PostgresqlUtilidadesBDDao.abrirConexion();
        HashMap mapa=new HashMap();
        if(con==null)
        {
            logger.error("Error abriedo la conexion ");
        }
        else
        {
            mapa=SqlBaseConsultaRecibosCajaDao.consultarAnulacionReciboCaja(con,institucion,numeroReciboCaja, cadenaConsultaAnulacionReciboCaja);
            PostgresqlUtilidadesBDDao.cerrarConexion(con);
        }
        return mapa;
    }
    
    
    /**
     * @param institucion
     * @param numeroReciboCaja
     * @return
     */
    public HashMap consultarTotalesPagos(int institucion, String numeroReciboCaja)
    {
        Connection con=PostgresqlUtilidadesBDDao.abrirConexion();
        HashMap mapa=new HashMap();
        if(con==null)
        {
            logger.error("Error abriedo la conexion ");
        }
        else
        {
            mapa=SqlBaseConsultaRecibosCajaDao.consultarTotalesPagos(con,institucion,numeroReciboCaja);
            PostgresqlUtilidadesBDDao.cerrarConexion(con);
        }
        return mapa;
    }
    
    /**
     * metodo para generar la busqueda avanzada
     * de consulta anulaciones recibos de caja
     * @param con Connection
     * @param vo HashMap
     * @return HashMap 
     * @author jarloc
     */
    public HashMap ejecutarBusquedaAvanzadaAnulacionesRC(Connection con, HashMap vo)
    {
        return SqlBaseConsultaRecibosCajaDao.ejecutarBusquedaAvanzadaAnulacionesRC(con, vo, cadenaConsultaAnulacionReciboCaja);
    }
    
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
										String consecutivoCajaPpal)
	{
		return SqlBaseConsultaRecibosCajaDao.consultaRecibosCaja(con, codigoInstitucion, fechaDDMMYYYY, loginUsuarioCajero, consecutivoCaja, restriccionEstadosSeparadosXComas, loginUsuarioGenera, fechaGeneracionConsulta, horaGeneracionConsulta, bloquearRegistro, consecutivoArqueoOCierre, codigoTipoArqueo, consecutivoCajaPpal);
	}
	
	/**
	 * obtiene el codigo - nombre del centro de atencion de un recibo de caja
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public InfoDatosInt getCentroAtencionReciboCaja( Connection con, String numeroReciboCaja, int codigoInstitucion)
	{
		return SqlBaseConsultaRecibosCajaDao.getCentroAtencionReciboCaja(con, numeroReciboCaja, codigoInstitucion);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoDevolRecibosCaja> consultarDevolucionesAprobadas(int institucion, String numeroReciboCaja)
	{
		return SqlBaseConsultaRecibosCajaDao.consultarDevolucionesAprobadas(institucion, numeroReciboCaja);
	}
}
