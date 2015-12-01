package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;
import com.princetonsa.actionform.manejoPaciente.ConsultarIngresosPorTransplantesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ConsultarIngresosPorTransplantesDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;






/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class ConsultarIngresosPorTransplantes
{

	private static Logger logger = Logger.getLogger(ConsultarIngresosPorTransplantes.class);
	
	//indices
	public static final String [] indicesCriterios={"centAten0","viaIngreso1","estIngreso2","tipoTransp3","fechaIniIngre4","fechaFinIngre5","institucion6"}; 
	
	public static final String [] indicesListado = {"centroAtencion0_","nomCentroCosto1_","idIngreso2_","consecutivo3_","fechaIngreso4_",
													"viaIngreso5_","nomViaIngreso6_","nomTransplante7_","paciente8_","nomPersona9_","identPaciente10_",
													"sexoPaciente11_","fechaNacimiento12_","transplante13_","tieneTrasplantes14_","menasaje15_"};
	
	public static final String [] indicesDetalleEncabezado={"nombreCampoEnc0","valorCampoEnc1","nombreCampoSub2"};	
	
	public static final String [] indicesDetalleSubDetalle={"consecutivo0_","valorCampoSub1_","traslados2_"};
	
	public static final String [] indicesDetalle ={"solicitudTrasladada0_","solicitudGenerada1_","fechaTraslado2_","horaTraslado3_"};
	
	/*-----------------------------------------------------------
	 *       METODOS TRANSLADO SOLICITUDES POR TRANSPLANTES
	 ------------------------------------------------------------*/
	
	
	/**
	 * Se inicializa el Dao
	 */
	
	public static ConsultarIngresosPorTransplantesDao consultarIngresosPorTransplantesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultarIngresosPorTransplantesDao();
	}
	
	
	public static void empezar (Connection connection, UsuarioBasico usuario, ConsultarIngresosPorTransplantesForm forma)
	{
		//se cargan los centros de atencion
		forma.setCentrosAtent(UtilidadesManejoPaciente.obtenerCentrosAtencion(connection, usuario.getCodigoInstitucionInt(),""));
		//se postula el centro de atencion de la session
		forma.setCriterios(indicesCriterios[0], usuario.getCodigoCentroAtencion());
		
	}
	
	/**
	 * Metodo encargado de consultar los ingrsos por transplantes
	 * @param connection
	 * @param criterios
	 * -----------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -----------------------------
	 * -- centAten0
	 * -- viaIngreso1
	 * -- estIngreso2
	 * -- tipoTransp3
	 * -- fechaIniIngre4
	 * -- fechaFinIngre5
	 * -- institucion6
	 * @return mapa
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * centroAtencion0_,nomCentroCosto1_,
	 * idIngreso2_,consecutivo3_,
	 * fechaIngreso4_,viaIngreso5_,
	 * nomViaIngreso6_,transplante7_,
	 * paciente8_,nomPersona9_,
	 * identPaciente10_,sexoPaciente11_,
	 * fechaNacimiento12_
	 */
	private static  HashMap consultarIngresosTransplante (Connection connection, HashMap criterios)
	{
		return consultarIngresosPorTransplantesDao().consultarIngresosTransplante(connection, criterios);
	}
	
	
	/**
	 * Metodo  encargado de consultar del detalle de los traslados de solicitudes
	 * @param connection
	 * @param idTraslado
	 * @return mapa
	 * -------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------
	 * -- solicitudTrasladada0_
	 * -- solicitudGenerada1
	 * -- fechaTraslado2_
	 * -- horaTraslado3_
	 */
	private static HashMap consultarTrasladoSolicitudes (Connection connection,String idTraslado)
	{
		return consultarIngresosPorTransplantesDao().consultarTrasladoSolicitudes(connection, idTraslado);
	}
	
	/**
	 * Metodo encargado de consultar los traslados de ingrespos a un paciente
	 * @param connection
	 * @param institucion
	 * @param tipoTransplante
	 * @param ingresoBuscar
	 * @return mapa
	 * -------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------
	 * -- consecutivo0_
	 * -- valorCampoSub1_
	 */
	private static HashMap consultarTraslado (Connection connection,String institucion,String tipoTransplante, String ingresoBuscar)
	{
		return consultarIngresosPorTransplantesDao().consultarTraslado(connection, institucion, tipoTransplante, ingresoBuscar);
	}
	
	
	
	
	
	private static  HashMap consultarIngresosTransplante (Connection connection, String centroAtencion,String viaIngreso,String estadoIngreso,
														  String tipoTransplante,String fechaIniIngreso,String fechaFinIngreso,String institucion)
	{
		HashMap criterios = new HashMap ();
		HashMap result = new HashMap ();
		criterios.put(indicesCriterios[0], centroAtencion);
		if (viaIngreso.equals(ConstantesBD.codigoNuncaValido+""))
			criterios.put(indicesCriterios[1], "");
		else
			criterios.put(indicesCriterios[1], viaIngreso);
		criterios.put(indicesCriterios[2], estadoIngreso);
		criterios.put(indicesCriterios[3], tipoTransplante);
		criterios.put(indicesCriterios[4], fechaIniIngreso);
		criterios.put(indicesCriterios[5], fechaFinIngreso);
		criterios.put(indicesCriterios[6], institucion);
		
		result = consultarIngresosTransplante(connection, criterios);
		int numReg = Utilidades.convertirAEntero(result.get("numRegistros")+"");
		for (int i=0;i<numReg;i++)
			result.put(indicesListado[12]+i, UtilidadFecha.calcularEdadDetallada(result.get(indicesListado[12]+i)+"", UtilidadFecha.getFechaActual()));
		
		
		return result;
	}
	
	public  HashMap buscar (Connection connection, ConsultarIngresosPorTransplantesForm forma,UsuarioBasico usuario)
	{
		
		HashMap transplantes = new HashMap ();
		HashMap tmp = new HashMap ();
		tmp.put("numRegistros", 0);
		transplantes=consultarIngresosTransplante(connection, forma.getCriterios(indicesCriterios[0])+"", forma.getCriterios(indicesCriterios[1])+"",
				forma.getCriterios(indicesCriterios[2])+"", forma.getCriterios(indicesCriterios[3])+"", forma.getCriterios(indicesCriterios[4])+"",
				forma.getCriterios(indicesCriterios[5])+"", usuario.getCodigoInstitucion());
		
		int numReg=Utilidades.convertirAEntero(transplantes.get("numRegistros")+"");
		
		for (int i=0;i<numReg;i++)
		{
			tmp=consultarTraslado(connection, usuario.getCodigoInstitucion(), transplantes.get(indicesListado[13]+i)+"", transplantes.get(indicesListado[2]+i)+"");
			 if (Utilidades.convertirAEntero(tmp.get("numRegistros")+"")>0)
				 transplantes.put(indicesListado[14]+i, ConstantesBD.acronimoSi);
			 else
			 {
				 transplantes.put(indicesListado[14]+i, ConstantesBD.acronimoNo);
				 transplantes.put(indicesListado[15]+i, "No tiene Traslados");
			 }
		}
		return transplantes;
	}
		
	
	
	
	
	public static void accionOrdenarMapa(ConsultarIngresosPorTransplantesForm forma)
	{
		int numReg = Integer.parseInt(forma.getListadoPaciente("numRegistros")+"");
		forma.setListadoPaciente(Listado.ordenarMapa(indicesListado, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getListadoPaciente(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setListadoPaciente("numRegistros",numReg+"");
	}
	
	
	public static void cargarDetalle (Connection connection,ConsultarIngresosPorTransplantesForm forma,UsuarioBasico usuario)
	{
		//se cargan los datos del encabezado
		cargarEncabezado(forma);
		
		//se encarga de cargar los traslados
		cargarTraslado(connection, forma, usuario.getCodigoInstitucion());
		
	}
	
	public static void cargarEncabezado (ConsultarIngresosPorTransplantesForm forma)
	{
		if ((forma.getListadoPaciente(indicesListado[13]+forma.getIndex())+"").equals(ConstantesIntegridadDominio.acronimoIndicativoDonante))
		{
			forma.setEncabezado(indicesDetalleEncabezado[0], "Donante: ");
			forma.setEncabezado(indicesDetalleEncabezado[2], "Receptor: ");
		}
		else
		{
			forma.setEncabezado(indicesDetalleEncabezado[0], "Receptor: ");
			forma.setEncabezado(indicesDetalleEncabezado[2], "Donante: ");
		}
		//nombre paciente
		forma.setEncabezado(indicesDetalleEncabezado[1], forma.getListadoPaciente(indicesListado[9]+forma.getIndex()));
	}
	
	
	public static void cargarTraslado (Connection connection,ConsultarIngresosPorTransplantesForm forma,String institucion)
	{
			HashMap traslado = new HashMap ();
			traslado.put("numRegistros", 0);
			HashMap detalle = new HashMap ();
			detalle.put("numRegistros", 0);
		traslado=consultarTraslado(connection, institucion, forma.getListadoPaciente(indicesListado[13]+forma.getIndex())+"", forma.getListadoPaciente(indicesListado[2]+forma.getIndex())+"");
		
		int numReg = Utilidades.convertirAEntero(traslado.get("numRegistros")+"");
		for (int i=0;i<numReg;i++)
		{
			traslado.put(indicesDetalleSubDetalle[2]+i, consultarTrasladoSolicitudes(connection, traslado.get(indicesDetalleSubDetalle[0]+i)+""));
		}		
		
		forma.setDetalle(traslado);
	}
	
	
	
	
	
	
	
	public static void accionImprimir(Connection con, ConsultarIngresosPorTransplantesForm  forma, UsuarioBasico usuario ,InstitucionBasica institucion, HttpServletRequest request)
	{
		String nombreRptDesign = "ConsultarIngresosPorTransplantes.rptdesign";
		String condiciones = "";
		
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(2,0, "PACIENTES DE TRANSPLANTES");
        Vector v2=new Vector();
        v2.add("Centro Atención: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCriterios(indicesCriterios[0])+"")));
        if (!(forma.getCriterios(indicesCriterios[1])+"").equals(ConstantesBD.codigoNuncaValido+""))
        	v2.add("Via Ingreso: "+Utilidades.obtenerNombreViaIngreso(con, Utilidades.convertirAEntero(forma.getCriterios(indicesCriterios[1])+"")));
        if (UtilidadCadena.noEsVacio(forma.getCriterios(indicesCriterios[2])+""))
        	v2.add("Estado Ingreso: "+ValoresPorDefecto.getIntegridadDominio(forma.getCriterios(indicesCriterios[2])+""));
        if (UtilidadCadena.noEsVacio(forma.getCriterios(indicesCriterios[3])+""))
        	v2.add("Tipo Transplante: "+ValoresPorDefecto.getIntegridadDominio(forma.getCriterios(indicesCriterios[3])+""));
        
    	v2.add("Fecha Inicial Ingreso: "+forma.getCriterios(indicesCriterios[4]));
    	v2.add("Fecha Final Ingreso: "+forma.getCriterios(indicesCriterios[5]));
    	  comp.insertLabelInGridOfMasterPage(4,0,v2);
        
        comp.obtenerComponentesDataSet("ConsultarIngresosPorTransplantes");
        
        String where=" i.institucion="+usuario.getCodigoInstitucion()+" AND c.estado_cuenta IN ('"+ConstantesBD.codigoEstadoCuentaActiva+"','"+ConstantesBD.codigoEstadoCuentaFacturadaParcial+"','"+ConstantesBD.codigoEstadoCuentaAsociada+"') ";
		
		//centro atencion --Opcional
		if (UtilidadCadena.noEsVacio(forma.getCriterios(indicesCriterios[0])+""))
			where+=" AND i.centro_atencion="+forma.getCriterios(indicesCriterios[0])+"";
			
		//via ingreso --Opcional
		if (UtilidadCadena.noEsVacio(forma.getCriterios(indicesCriterios[1])+"") && !(forma.getCriterios(indicesCriterios[1])+"").equals(ConstantesBD.codigoNuncaValido+"") )
			where+=" AND c.via_ingreso="+forma.getCriterios(indicesCriterios[1])+"";
		else
			where+=" AND c.via_ingreso IN ('"+ConstantesBD.codigoViaIngresoHospitalizacion+"','"+ConstantesBD.codigoViaIngresoAmbulatorios+"')";
		
		//Estado del ingreso --Opcional
		if (UtilidadCadena.noEsVacio(forma.getCriterios(indicesCriterios[2])+""))
			where+=" AND i.estado='"+forma.getCriterios(indicesCriterios[2])+"'";
		else
			where+=" AND i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"')";
			
		//Tipo de transplante -- Requerido
		if (UtilidadCadena.noEsVacio(forma.getCriterios(indicesCriterios[3])+""))
			where+=" AND i.transplante='"+forma.getCriterios(indicesCriterios[3])+"'";
		else
			where+=" AND i.transplante IN ('"+ConstantesIntegridadDominio.acronimoIndicativoDonante+"','"+ConstantesIntegridadDominio.acronimoIndicativoReceptor+"')";
		
		//		Valida por un rango de fechas
		if (UtilidadCadena.noEsVacio(forma.getCriterios(indicesCriterios[4])+"") && UtilidadCadena.noEsVacio(forma.getCriterios(indicesCriterios[5])+"") )
			where+=" AND i.fecha_ingreso BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(forma.getCriterios(indicesCriterios[4])+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(forma.getCriterios(indicesCriterios[5])+"")+"'" ;
		
		
		where+=" ORDER BY i.fecha_ingreso ";
		
        
        
        
        
        
        String newQuery = comp.obtenerQueryDataSet().replace("1=2", where);
        logger.info("\nConsulta en el BIRT con Condiciones: "+newQuery);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery);
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
   
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}