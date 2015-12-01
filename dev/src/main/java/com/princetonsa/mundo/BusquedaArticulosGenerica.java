/*
 * @(#)BusquedaArticulosGenerica.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.Collection;
import org.apache.log4j.Logger;
import util.UtilidadBD;
import com.princetonsa.dao.BusquedaArticulosGenericaDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Clase para el manejo de la busqueda de articulos generica
 * @version 1.0, Dic 14, 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class BusquedaArticulosGenerica 
{
    /**
     * DAO utilizado por el objeto parra acceder a la fuente de datos
     */
    private static BusquedaArticulosGenericaDao busquedaDao = null;
    
    /**
     * Para hacer logs de debug / warn / error de esta funcionalidad.
     */
    private Logger logger = Logger.getLogger(BusquedaArticulosGenerica.class);

    /**
     * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
     * @param tipoBD el tipo de base de datos que va a usar este objeto
     * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
     * son los nombres y constantes definidos en <code>DaoFactory</code>.
     * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
     */
    public boolean init(String tipoBD)
    {
        boolean wasInited = false;
        DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
    
        if (myFactory != null)
        {
            busquedaDao = myFactory.getBusquedaArticulosGenericaDao();
            wasInited = (busquedaDao != null);
        }
        return wasInited;
    }
    
    /**
     * Metodo que realiza la busqueda avanzada de los articulos x codigos o descripcion
     * @param con
     * @param esBusquedaPorNombre
     * @param criterioBusqueda
     * @param codigosArticulosInsertados
     * @param filtrarXInventarios
     * @param codigoAlmacen
     * @param codigoTransaccion
     * @param codigoInstitucion
     * @param parejasClaseGrupo
     * @param b 
     * @param codigoEsquemaTarifario
     * @return
     */
    public Collection busquedaAvanzadaArticulos(    Connection con,
                                                                            boolean esBusquedaPorNombre,
                                                                            String criterioBusqueda,
                                                                            String codigosArticulosInsertados,
                                                                            boolean filtrarXInventarios,
                                                                            int codigoContrato,
                                                                            String codigoAlmacen,
                                                                            String codigoTransaccion,
                                                                            int codigoInstitucion,
																			String parejasClaseGrupo,
																			String esMedicamento,
																			String esPos,
																			String tipoConsignac,
																			boolean filtrarXSeccionSubseccion,
							                                                String codigosSecciones, //codigos secciones separadas por comas
							                                                String codigoSubseccion, //codigo de la subseccion
							                                                String codigoSeccion, //codigo de la seccion
							                                                boolean filtrarXClaseGrupoSub, 
							                                                String codigoClase, //codigo Clase
							                                                String codigoGrupo, //codigo del grupo
							                                                String codigoSubgrupo, //codigo del subgrupo
							                                                boolean filtrarXPrepPen,
							                                                boolean filtrarXAjusteInvFis,
							                                                String tipoDispositivo,
							                                                String tipoAccesoVascular, 
							                                                boolean soloAlmacen,
							                                                int categoria,
							                                                String formaFarmaceutica,
							                                                String unidadMedida,
							                                                boolean valorDefectoClasesInventario,
							                                                String idTercero,
							                                                String esMedicamentoControlEspecial,
							                                                boolean cargarEquivalentes,
							                                                boolean aplicaCargosDirectos,
							                                                boolean atencionOdontologica
                                                                       ) 
    {
        busquedaDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBusquedaArticulosGenericaDao();
        Collection coleccion=null;
        try
        {   
            coleccion=UtilidadBD.resultSet2Collection(busquedaDao.busquedaAvanzadaArticulos(con, esBusquedaPorNombre, criterioBusqueda, codigosArticulosInsertados, filtrarXInventarios, codigoContrato,codigoAlmacen, codigoTransaccion, codigoInstitucion,parejasClaseGrupo, esMedicamento, esPos, tipoConsignac,filtrarXSeccionSubseccion,codigosSecciones,codigoSubseccion,codigoSeccion, filtrarXClaseGrupoSub, codigoClase, codigoGrupo, codigoSubgrupo, filtrarXPrepPen, filtrarXAjusteInvFis, tipoDispositivo, tipoAccesoVascular,soloAlmacen, categoria, formaFarmaceutica, unidadMedida, valorDefectoClasesInventario, idTercero, esMedicamentoControlEspecial,cargarEquivalentes,aplicaCargosDirectos,atencionOdontologica));
            logger.info("\n\nCOLLECTION BUSQUEDA GENERICA------>"+coleccion.size());
        }
        catch(Exception e)
        {
            logger.warn("Error en busqueda avanzada articulos----> " +e.toString());
            e.printStackTrace();
            coleccion=null;
        }
        return coleccion;       
    }
    
    
}
