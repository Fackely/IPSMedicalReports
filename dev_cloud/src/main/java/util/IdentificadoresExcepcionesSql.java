
/*
 * Creado   6/04/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_06
 * author Joan Lopez
 */
package util;

/**
 * 
 * Clase que implementa las constantes de los 
 * identificadores de las excepciones SQL de la 
 * Base de Datos.
 * Cada Excepción define un número unico que identifica,
 * el tipo de excepción arrojado, el cual se captura,
 * para un manejo optimo de los errores que se presenten
 * en la ejecución de sentencias sobre la fuente de
 * datos. 
 *
 * @version 1.0, 6/04/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class IdentificadoresExcepcionesSql 
{
    /************************************************************************************
     *  SQL EXCEPCIONES
     ************************************************************************************/

    /*
    #23000  INTEGRITY CONSTRAINT VIOLATION
    #23001  RESTRICT VIOLATION
    #23502  NOT NULL VIOLATION
    #23503  FOREIGN KEY VIOLATION
    #23505  UNIQUE VIOLATION
    #23514  CHECK VIOLATION
    */
    /**
     * Excepcion violacion llave primaria.
     */
    public static final String codigoExcepcionSqlRegistroExistente = "23505";

    /**
     * Excepcion violacion llave foranea.
     */
    public static final String codigoExcepcionSqlViolacionForanea = "23503";
    
    /**
     * Excepcion violacion de integridad de una relacion
     */
    public static final String codigoExcepcionSqlViolacionIntegridadRelacion = "23000";
    
    /**
     * Excepcion de concurrencia cuando se maneja select for update y no wait
     */
    public static final String codigoExcepcionSqlNoWaitSelectForUpdate= "55P03"; 
}
