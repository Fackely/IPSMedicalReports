/*
 * @(#)BusquedaServiciosGenerica.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import util.UtilidadBD;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.dao.BusquedaServiciosGenericaDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.facturacion.Cobertura;

/**
 * Clase para el manejo de la busqueda de servicios generica
 * @version 1.0, Oct 31, 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
@SuppressWarnings("unchecked")
public class BusquedaServiciosGenerica 
{
    /**
     * DAO utilizado por el objeto parra acceder a la fuente de datos
     */
    private static BusquedaServiciosGenericaDao busquedaDao = null;
    
    /**
     * Para hacer logs de debug / warn / error de esta funcionalidad.
     */
    private Logger logger = Logger.getLogger(BusquedaServiciosGenerica.class);
        
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
            busquedaDao = myFactory.getBusquedaServiciosGenericaDao();
            wasInited = (busquedaDao != null);
        }
        return wasInited;
    }
    
    /**
     * busqueda de servicios dado el codigo axioma o codigo cups o codigo iss o codigo soat o descripcion cups
     * @param con
     * @param codigo
     * @param descripcionServicio
     * @param codigoSexo
     * @param codigosServiciosInsertados
     * @param filtrarTipoServicio
     * @param codigoContrato
     * @param tipoTarifario
     * @param filtrarNopos
     * @param codigoAxioma
     * @param codigoFormulario
     * @return
     */
	public Collection busquedaAvanzadaServiciosXCodigos( 	Connection con,
	                                                        String codigo,
	                                                        String descripcionServicio,
	                                                        int codigoSexo,
	                                                        String codigosServiciosInsertados,
	                                                        String filtrarTipoServicio,
															int codigoContrato,
															String tipoTarifario,
															boolean filtrarNopos,
															String codigoAxioma,
															int codigoFormulario,
															UsuarioBasico usuario,
															int codigoIngreso,
															int codigoViaIngreso,
															String tipoPaciente,
															boolean atencionOdontologica,
															String tipoAtencion,
															String nombreForma,
															String codigoPrograma,
															String filtrarNaturalezaServicio
														  )
    {
    	busquedaDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBusquedaServiciosGenericaDao();
        logger.info("entro atencion ODONTOLOGICA >>"+atencionOdontologica);
    	Collection coleccion=null;
        try
        {   
                coleccion=UtilidadBD.resultSet2Collection(busquedaDao.busquedaAvanzadaServiciosXCodigos(con,
												                                                        codigo,
												                                                        descripcionServicio, 
												                                                        codigoSexo,
												                                                        codigosServiciosInsertados,
												                                                        filtrarTipoServicio,
																										codigoContrato,
																										tipoTarifario,
                																						filtrarNopos,
                																						codigoAxioma,
                																						codigoFormulario,
                																						atencionOdontologica,
                																						tipoAtencion,
                																						nombreForma,
                																						codigoPrograma,
                																						filtrarNaturalezaServicio));
                
                boolean valProfesionalSalud = UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, false);
                
                Iterator iterador = coleccion.iterator();
                while(iterador.hasNext())
                {
                	HashMap elemento = (HashMap)iterador.next();
                	String justificar="";

            		//Evaluamos si el elemento es No POS
                	if (elemento.get("espos").toString().equals("NOPOS") && codigoIngreso>0){
                		
                		//Evaluamos la cobertura del Servicio
                		InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
                		infoResponsableCobertura = Cobertura.validacionCoberturaServicio(con, codigoIngreso+"", codigoViaIngreso, tipoPaciente, Integer.parseInt(elemento.get("codigoaxioma").toString()), usuario.getCodigoInstitucionInt(), false, "" /*subCuentaCoberturaOPCIONAL*/);
                		elemento.put("cobertura", infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo()+"");
                		elemento.put("subcuenta", infoResponsableCobertura.getDtoSubCuenta().getSubCuenta()+"");

                		//Evaluamos si el convenio que cubre el servicio requiere de justificación de servicios
                		if (UtilidadesFacturacion.requiereJustificacioServ(con, infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo())){
                			justificar="true";
                			
                			// Validacion 'Especialidad profesional de la salud'
                			if (!valProfesionalSalud){
                				justificar="pendiente";
                			}
                		} else {
                			justificar="false";
                		}
                		
                	}
                	
                	elemento.put("justificar", justificar);
                	//logger.info("Se requiere Justificar? - "+justificar);
                }
        }
        catch(Exception e)
        {
            logger.warn("Error en busqueda avanzada servicios----> " ,e);
            coleccion=null;
        }
        return coleccion;
    }
    
    
    
    /* @param con
    * @param codigo
    * @param descripcionServicio
    * @param codigoSexo
    * @param codigosServiciosInsertados
    * @param filtrarTipoServicio
    * @param codigoContrato
    * @param tipoTarifario
    * @param filtrarNopos
    * @param codigoAxioma
    * @param codigoFormulario
    * @return
    */
   public Collection busquedaAvanzadaServiciosXDescripcion( 	Connection con,
	                                                        String codigo,
	                                                        String descripcionServicio,
	                                                        int codigoSexo,
	                                                        String codigosServiciosInsertados,
	                                                        String filtrarTipoServicio,
															int codigoContrato,
															String tipoTarifario,
															boolean filtrarNopos,
															String codigoAxioma,
															int codigoFormulario,
															UsuarioBasico usuario,
															int codigoIngreso,
															int codigoViaIngreso,
															String tipoPaciente,
															boolean atencionOdontologica,
															String tipoAtencion,
															String nombreForma,
															String codigoPrograma,
															String filtrarNaturalezaServicio
														  )
   {
   	busquedaDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBusquedaServiciosGenericaDao();
       logger.info("entro atencion ODONTOLOGICA >>"+atencionOdontologica);
   	Collection coleccion=null;
       try
       {   
               coleccion=UtilidadBD.resultSet2Collection(busquedaDao.busquedaAvanzadaServiciosXCodigos(con,
												                                                        codigo,
												                                                        descripcionServicio, 
												                                                        codigoSexo,
												                                                        codigosServiciosInsertados,
												                                                        filtrarTipoServicio,
																										codigoContrato,
																										tipoTarifario,
               																						filtrarNopos,
               																						codigoAxioma,
               																						codigoFormulario,
               																						atencionOdontologica,
               																						tipoAtencion,
               																						nombreForma,
               																						codigoPrograma,
               																						filtrarNaturalezaServicio));
               
               boolean valProfesionalSalud = UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, false);
               
               Iterator iterador = coleccion.iterator();
               while(iterador.hasNext())
               {
               	HashMap elemento = (HashMap)iterador.next();
               	String justificar="";

           		//Evaluamos si el elemento es No POS
               	if (elemento.get("espos").toString().equals("NOPOS") && codigoIngreso>0){
               		
               		//Evaluamos la cobertura del Servicio
               		InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
               		infoResponsableCobertura = Cobertura.validacionCoberturaServicio(con, codigoIngreso+"", codigoViaIngreso, tipoPaciente, Integer.parseInt(elemento.get("codigoaxioma").toString()), usuario.getCodigoInstitucionInt(), false, "" /*subCuentaCoberturaOPCIONAL*/);
               		elemento.put("cobertura", infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo()+"");
               		elemento.put("subcuenta", infoResponsableCobertura.getDtoSubCuenta().getSubCuenta()+"");

               		//Evaluamos si el convenio que cubre el servicio requiere de justificación de servicios
               		if (UtilidadesFacturacion.requiereJustificacioServ(con, infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo())){
               			justificar="true";
               			
               			// Validacion 'Especialidad profesional de la salud'
               			if (!valProfesionalSalud){
               				justificar="pendiente";
               			}
               		} else {
               			justificar="false";
               		}
               		
               	}
               	
               	elemento.put("justificar", justificar);
               	//logger.info("Se requiere Justificar? - "+justificar);
               }
       }
       catch(Exception e)
       {
           logger.warn("Error en busqueda avanzada servicios----> " ,e);
           coleccion=null;
       }
       return coleccion;
   }
    
    
   
}