package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.manejoPaciente.TotalOcupacionCamasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.TotalOcupacionCamasDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.TotalOcupacionCamas;
import com.princetonsa.pdf.TotalOcupacionCamasPdf;

/**
 * Fecha: Febrero - 2008
 * @author Mauricio Jaramillo
 *
 */

public class TotalOcupacionCamas 
{
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	static Logger logger =Logger.getLogger(TotalOcupacionCamas.class);
	
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private TotalOcupacionCamasDao objetoDao;
	
	/**
	 * 
	 *
	 */
	public TotalOcupacionCamas()
	{
		init(System.getProperty("TIPOBD"));
		this.reset();
	}
	
	/**
	 * 
	 * @param tipoBD
	 * @return
	 */
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getTotalOcupacionCamasDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
	}
	
	/**
     * Reset de la forma
     */
	public void reset()
	{

	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap consultarEstados(Connection con) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTotalOcupacionCamasDao().consultarEstados(con);
	}

	/**
	 * Metodo encargado de consultar las camas en los diferentes estados
	 * @author Jhony Alexander Duque
	 * @param criterios
	 * -------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -------------------------------
	 * -- centroAtencion --> Requerido
	 * -- institucion --> Requerido
	 * -- estadosCama --> Requerido
	 * -- incluirCamasUrg --> Requerido
	 * @param con
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------
	 * numeroCama0_,codigoCama1_,
	 * estadoCama2_,tipoHabitacion3_,
	 * nombrePiso4_
	 */
	public static HashMap consultarTotalEstados(Connection connection,HashMap criterios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTotalOcupacionCamasDao().consultarTotalEstados(connection, criterios);
	}
	
	
	public static HashMap consultarCamas (Connection connection,TotalOcupacionCamasForm forma,UsuarioBasico usuario)
	{
		logger.info("\n incluir camas -->"+forma.getIncluirCamas());
		
		HashMap criterios = new HashMap ();
		//los estado de las camas por los cuales se debe filtrar
		criterios.put("estadosCama", organizarEstadosBuscar(forma.getInicialMap()));
		//incluir clamas de urgencias
		criterios.put("incluirCamasUrg", forma.getIncluirCamas());
		//institucion
		criterios.put("institucion", usuario.getCodigoInstitucion());
		//centroAtencion
		criterios.put("centroAtencion", forma.getCodigoCentroAtencion());
		//detalle 
		criterios.put("detalle", organizarDetalle(forma.getInicialMap()));
		
		
		return consultarTotalEstados(connection, criterios);
		
	}
	
	/**
	 * Metodo encargado de organizar los estados
	 * de las camas para enviarlos a la consulta.
	 * @param mapa
	 * @return
	 */
	public static String organizarEstadosBuscar (HashMap mapa)
	{
		logger.info("\n entre a organizarEstadosBuscar");
		String estados ="";
		int numReg = Utilidades.convertirAEntero(mapa.get("numRegistros")+""); 
		
		for (int i=0;i<numReg;i++) 
		{
			if (UtilidadTexto.getBoolean(mapa.get("checkcodigo_"+i)+""))
				if (!UtilidadCadena.noEsVacio(estados))
					estados+=mapa.get("codigo_"+i)+"";
				else
					estados+=","+mapa.get("codigo_"+i);
		}
		
		return estados;
	}
	
	
	public static ArrayList organizarDetalle (HashMap mapa)
	{
		logger.info("\n entre a organizarDetalle ");	
		ArrayList detalle = new ArrayList ();
		int numReg = Utilidades.convertirAEntero(mapa.get("numRegistros")+""); 
		
		for (int i=0;i<numReg;i++) 
		{
			if (UtilidadTexto.getBoolean(mapa.get("detHabitacion_"+i)+""))
				detalle.add(mapa.get("codigo_"+i)); 
		}
		
		return detalle;
	}
	
	
	public static boolean esDetallado (ArrayList detalle ,String estado)
	{
		for (int i=0;i<detalle.size();i++)
			if((detalle.get(i)+"").equals(estado))
				return true;
		
		return false;
	}
	
	
	public static StringBuffer cargarMapa(Connection connection, TotalOcupacionCamasForm forma,InstitucionBasica institucion,UsuarioBasico usuario)
	{
		StringBuffer cadena = new StringBuffer();
		String criterios = ""; 
		
		//razon social institucion
		cadena.append(institucion.getRazonSocial()+"\n");
		//nit
		cadena.append("NIT: "+institucion.getNit()+"\n");
		//direccion
		cadena.append("DIRECCION: "+institucion.getDireccion()+"\n");
		//telefono
		cadena.append("TELEFONO: "+institucion.getTelefono()+"\n");
		cadena.append("Reporte: TOTAL OCUPACION CAMAS \n");  
		    
		criterios=TotalOcupacionCamasPdf.cargarCriteriosMostrar(connection, forma, usuario);

		cadena.append(criterios+"\n");  
		
		logger.info("\n entre a ------------ genera el archivo plano------------");
		int numRegistros = Utilidades.convertirAEntero(forma.getDatosConsulta("numRegistros")+"");
	
		// semiran los estados por los cuales se busco
		//-----------------------------------------------------------------------
		String tmp=TotalOcupacionCamas.organizarEstadosBuscar(forma.getInicialMap());
    	String estadosCama [] = tmp.split(",");
    	//-----------------------------------------------------------------------
    	//se miran ciantos tienen detalle
    	ArrayList detallado = TotalOcupacionCamas.organizarDetalle(forma.getInicialMap());
    	//se miran cuales son los tipos de habitacion
    	ArrayList tiposHabitacion= Utilidades.obtenerTiposHabitacion(connection, usuario.getCodigoInstitucion());
    	
        /*-----------------------------------------------------------------------------------------------------
	     *							 SECCION UTILIZADA PARA EL MONTAJE DEL ENCABEZADO
	     ------------------------------------------------------------------------------------------------------*/
    	String encabezado="PISO,TOTAL",subEncabezado=" , ";;
		 
		    //se colocan las columnas con los nombres los estados de la cama (dinamico)
		    for (int i=0;i<estadosCama.length;i++)
		    	encabezado+=", "+Utilidades.obtenerNombreEstadoCama(connection,estadosCama[i]).toUpperCase();
			cadena.append(encabezado+"\n");  
		    
		    
		    for (int i=0;i<estadosCama.length;i++)
		    { 
		    	//se pregunta si es detallado 
	    		if(TotalOcupacionCamas.esDetallado(detallado, estadosCama[i]))
	    		{
	    			//se crea una tabla en donde se va a crear el detalle
	    			
	    			for (int j=0;j<tiposHabitacion.size();j++)
	    			{
	    				//se añaden las celdas con los nombres del tipo de habitacion
	    				HashMap dato = (HashMap)tiposHabitacion.get(j);
	    				String tipoHabitacion = (dato.get("nombre")+"").toUpperCase();
	    				if (tipoHabitacion.length()>3)
	    					tipoHabitacion=tipoHabitacion.substring(0, 3);
	    				subEncabezado+=", "+tipoHabitacion;
	    			}
	    			//se añade la celda %
	    			subEncabezado+=", %";
	    		}
	    		else
	    			subEncabezado+=", TOTAL, %";
		    }
		    
		    cadena.append(subEncabezado+"\n");

		    /*-----------------------------------------------------------------------------------------------------
		     * 						FIN SECCION UTILIZADA PARA EL MONTAJE DEL ENCABEZADO
		     ------------------------------------------------------------------------------------------------------*/
        
		    /*-----------------------------------------------------------------------------------------------------
		     * 							SECCION DONDE SE CALCULAN LOS TOTALES
		     ------------------------------------------------------------------------------------------------------*/
        
		    
        
		    for (int k=0; k<numRegistros;k++)
		    {
		    	 int totalpiso=0;
		    	 float porcentaje=0;
		    	 
				
				    totalpiso=TotalOcupacionCamasPdf.cantidadCamasPorPiso(estadosCama, detallado, tiposHabitacion, forma.getDatosConsulta(), k);
				  
				    forma.setDatosConsulta("totalPiso_"+k, totalpiso);
				   for (int i=0;i<estadosCama.length;i++)
				    { 
					   float cantEstCama=0;
				    	//se pregunta si es detallado 
			    		if(TotalOcupacionCamas.esDetallado(detallado, estadosCama[i]))
			    		{
			    			//se crea una tabla en donde se va a crear el detalle
			    			for (int j=0;j<tiposHabitacion.size();j++)
			    			{
			    				
			    				cantEstCama=cantEstCama+Utilidades.convertirAEntero(forma.getDatosConsulta("cantidad_"+i+"_"+j+"_"+k)+"");
			    				
			    			}
			    			
			    			if (totalpiso>0)
			    				porcentaje=((cantEstCama*100)/totalpiso);
			    			else
			    				porcentaje=0;
			    			
			    			forma.setDatosConsulta("porcentaje_"+i+"_"+k, UtilidadTexto.formatearValores(porcentaje,"0.00")+" %");
			    		}
			    		else
			    		{
		    				cantEstCama=Utilidades.convertirAEntero(forma.getDatosConsulta("cantidad_"+i+"_"+k)+"");
			    			
			    			if (totalpiso>0)
			    				porcentaje=((cantEstCama*100)/totalpiso);
			    			else
			    				porcentaje=0;
			    			
			    			forma.setDatosConsulta("porcentaje_"+i+"_"+k,UtilidadTexto.formatearValores(porcentaje,"0.00")+" %");
			    		}
				    }
		    	
		   
		    }
		    
		    TotalOcupacionCamasPdf.cantidadCamasPorPiso2(estadosCama, detallado, tiposHabitacion, forma);
		    /*-----------------------------------------------------------------------------------------------------
		     * 							FIN SECCION DONDE SE CALCULAN LOS TOTALES
		     ------------------------------------------------------------------------------------------------------*/
		    
		    
		    
		    
		    /*-----------------------------------------------------------------------------------------------------
		     * 						EN ESTA SECCION DE AGREGAN LOS DATOS AL REPORTE
		     ------------------------------------------------------------------------------------------------------*/
		  
		    //se itera el resultado de la consulta
		    logger.info("\n los dtos son -->"+forma.getDatosConsulta());
		    for (int k=0; k<numRegistros;k++)
		    {
		    	String fila="";
		    	fila= (forma.getDatosConsulta("nombre_piso1_"+k)+"").toLowerCase()+", "+forma.getDatosConsulta("totalPiso_"+k);
				  
		    	for (int i=0;i<estadosCama.length;i++)
				    { 
				    	//se pregunta si es detallado 
			    		if(TotalOcupacionCamas.esDetallado(detallado, estadosCama[i]))
			    		{
			    			//se crea una tabla en donde se va a crear el detalle
			    			for (int j=0;j<tiposHabitacion.size();j++)
			    				fila+=", "+forma.getDatosConsulta("cantidad_"+i+"_"+j+"_"+k);

			    			fila+=", "+forma.getDatosConsulta("porcentaje_"+i+"_"+k);
			    		}
			    		else
			    		{
			    			//esto en el caso en que no se seleccion detallado por tipo habitacion
			    			//se crea una tabla
			    			//se añaden las celdas total y %
			    			fila+=", "+forma.getDatosConsulta("cantidad_"+i+"_"+k)+", "+forma.getDatosConsulta("porcentaje_"+i+"_"+k);
			    		}
				    }
		    	cadena.append(fila+"\n");
		    }
		    

		   	 // -------------------------------------------------------------------
   		//aqui se mete la seccion de los totales
   	   //-------------------------------------------------------------------
   		
		    String totales="TOTALES, "+TotalOcupacionCamasPdf.totalPiso(estadosCama, detallado, tiposHabitacion, forma.getDatosConsulta());
   		
   		   for (int i=0;i<estadosCama.length;i++)
			    { 
			    	//se pregunta si es detallado 
		    		if(TotalOcupacionCamas.esDetallado(detallado, estadosCama[i]))
		    		{
		    			//se crea una tabla en donde se va a crear el detalle
		    			
		    			for (int j=0;j<tiposHabitacion.size();j++)
		    				totales+=", "+forma.getDatosConsulta("totales_"+i+"_"+j+"_"+numRegistros);
		    			
		    			totales+=", "+forma.getDatosConsulta("porcentaje_"+i+"_"+numRegistros );
		    		}
		    		else
		    		{
		    			totales+=", "+forma.getDatosConsulta("totales_"+i+"_"+numRegistros)+", "+forma.getDatosConsulta("porcentaje_"+i+"_"+numRegistros);
		    		}
		    		//se añade la nueva tabla a la tabla padre.
			    }
		    
   		   cadena.append(totales);
		    /*-----------------------------------------------------------------------------------------------------
		     * 						FIN DE SECCION DONDE SE AGREGRAN LOS DATOS AL REPORTE
		     ------------------------------------------------------------------------------------------------------*/
		
	    
	    logger.info("\n *********** fin reporte ------------");
		
		
		
		
		
		
		
		
		
		
		return cadena;
	}
	
	
	

}