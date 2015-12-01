/*
 * @(#)AntecedentesGinecoObstetricosHistoricos.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.antecedentes;

import java.sql.Connection;
import java.sql.SQLException;

import util.InfoDatos;
import util.UtilidadCadena;
import util.UtilidadFecha;

import com.princetonsa.dao.AntecedentesGinecoObstetricosHistoricoDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Clase para el manejo de toda la información básica de un antecedente gineco
 * obstetrico histórico
 *
 * @version 1.0, Abril 14, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public class AntecedentesGinecoObstetricosHistorico 
{
	/**
	 * Fecha de la última citológia que se realizó la paciente. No tiene ningun
	 * formato, se pueden insertar fechas de tipo "dd/mm/aaaa" o cadenas con
	 * información sobre la fecha como "hace dos meses", etc.
	 */
	private String fechaUltimaCitologia = "";
  
	/**
	 * Descripción de la última mamografía que se hizó la paciente.
	 */
	private String descripcionUltimaMamografia = "";

	/**
	 * Fecha de la última ecografia que se realizó la paciente. No tiene ningun
	 * formato, se pueden insertar fechas de tipo "dd/mm/aaaa" o cadenas con
	 * información sobre la fecha como "hace dos meses", etc.
	 */
	private String fechaUltimaEcografia = "";
  
	/**
	 * Descripción de la última ecografia que se hizó la paciente.
	 */
	private String descripcionUltimaEcografia = "";
	
	/**
	 * Fecha de la última densimetria osea de la paciente, no tiene ningún
	 * formato.
	 */
	public String fechaUltimaDensimetriaOsea;
	
	/**
	 * Descripción de la última densimetria osea de la paciente.
	 */	 
	public String descUltimaDensimetriaOsea;	

  
	/**
	 * Descripción de los procedimientos ginecológicos que se ha hecho.
	 */
	private String descripcionProcedimientosGinecologicos = "";
		
	/**
	 * Fecha de la última mamografia que se realizó la paciente. No tiene ningun
	 * formato, se pueden insertar fechas de tipo "dd/mm/aaaa" o cadenas con
	 * información sobre la fecha como "hace dos meses", etc.
	 */  	
	private String fechaUltimaMamografia = "";
  
	/**
	 * Número de dias de duración de la menstruación
	 */
	private int duracionMenstruacion = 0;

	/**
	 * Fecha de la última regla, en formato "dd/mm/aaaa"
	 */
	private String fechaUltimaRegla = "";
  
	/**
	 * Bandera que indica si presenta (true) o no (false) dolor durante la
	 * menstruación.
	 */
	private String dolorMenstruacion;
  
	/**
	 * Descripción de la última citología.
	 */
	private String descripcionUltimaCitologia = "";
	
	/**
	 * Caracteristica "concepto" de la menstruación, pareja de datos de tipo
	 * (código, nombre)
	 */
	private InfoDatos conceptoMenstruacion;

	/**
	 * Nombre del concepto de menstruación 
	 */	
	private String nombreConceptoMenstruacion;
	
	/**
	 * Observaciones de las caracteristicas de la menstruacion
	 */
	private String observacionesMenstruacion = "";
	
	/**
	 * Ciclo menstrual
	 */
	private int cicloMenstrual;
	
	/**
	 * Información de embarazos, número de gestaciones
	 */
	public String gInfoEmbarazos;

	/**
	 * Información de embarazos, número de partos vaginales
	 */
	public String pInfoEmbarazos;
	
	/**
	 * Campo para ingresar el rango para partos // menor  2500 gramos  
	 */
	public String p2500 = "";

	/**
	 * Campo para ingresar el rango para partos // mayor  4000 gramos
	 */
	public String p4000 = "";

	/**
	 * Información de embarazos, número de abortos
	 */
	public String aInfoEmbarazos;

	/**
	 * Información de embarazos, número de cesáreas
	 */
	public String cInfoEmbarazos = "";

	/**
	 * Información de embarazos, número de hijos nacidos vivos
	 */
	public String vInfoEmbarazos = "";
	
	/**
	 * Información de embarazos, número de hijos nacidos muertos
	 */
	public String mInfoEmbarazos = "";


	/**
	 * Fecha de creación de este antecedente gineco obstetrico historico
	 */
	private String fechaCreacion="";
	
	/**
	 * Hora de creación de este antecedente gineco obstetrico historico
	 */
	private String horaCreacion="";
	
	/**
	 * El DAO usado por el objeto
	 * <code>AntecedenteGinecoObstetricoHistorico</code> para acceder a la
	 * fuente de datos.
	 */
	private static AntecedentesGinecoObstetricosHistoricoDao antecedentesGinecoObstetricosHistoricoDao=null;

	/**
	 *  Es para el campo de chequeo alfrente del campo de texto A (Abortos) 
	 */
	public String  mayorA2="";

	
	/**
	 * Campo para indicar Fin Embarazo Antertior 
	 */
	public String  finEmbarazoAnterior = "";
	
	/**
	 * Es para el campo de chequeo de Mayor 5 o menor  a 1
	 * Que aparece  al frente de fin embarazo anterior 
	 */
	public String finEmbarazoMayor1o5 = "";
	
	/**
	 * Campo String de Prematuros 
	 */
	public String prematuros = "";
	
	/**
	 * Campo String de ectropicos 
	 */
	public String ectropicos = "";
	/**
	 * Campo String de multiples 
	 */
	public String multiples = "";
	
	//---------------------------------------------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------------------------------------------
	//------------------------------Nuevos Campos para la seccion "Información Embarazos"
	//---------------------------------------------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------------------------------------------
	
	
	String tipoEmbarazo = "";
	String muertosAntes1Semana = "";
	String muertosDespues1Semana = "";
	String vivosActualmente = "";
	
	/**
	 * -- Vaginal
	 */
	int vag;			 	  		
	
	
	
	/**
	 * -- Retencion Placentaria
	 */
	String retencionPlacentaria = "";
	
	/**
	 * -- Infeccion Postparto
	 */
	String infeccionPostparto = "";
	
	/**
	 * -- Malformaciones
	 */
	String malformacion = "";
	
	/**
	 * -- Muerte Perinatal
	 */
	String muertePerinatal = "";
	

	//--- Para la Sección de Historia Mestrual 
	String sangradoAnormal;	
	String flujoVaginal;	
	String enferTransSexual;	
	String cualEnferTransSex;	
	String cirugiaGineco;	
	String cualCirugiaGineco;	
	String historiaInfertilidad;	
	String cualHistoInfertilidad;	
	
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init (String tipoBD) 
	{

		if (antecedentesGinecoObstetricosHistoricoDao  == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			antecedentesGinecoObstetricosHistoricoDao = myFactory.getAntecedentesGinecoObstetricosHistoricoDao();
		}

	}

	/**
	 * Constructora Vacía de Antecedentes GinecoObstetricos
	 * @see java.lang.Object#Object()
	 */	
	public AntecedentesGinecoObstetricosHistorico()
	{
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Constructora que recibe todos los datos necesarios para crear
	 * un antecedente ginecoobstetrico Historico
	 * @param fechaUltimaCitologia fecha de la última citologia
	 * @param descripcionUltimaMamografia Descripcion de la última mamografia
	 * @param descripcionProcedimientosGinecologicos
	 * @param fechaUltimaMamografia
	 * @param duracionMenstruacion
	 * @param fechaUltimaRegla
	 * @param dolorMenstruacion
	 * @param descripcionUltimaCitologia
	 * @param conceptoMenstruacion
	 */
	public AntecedentesGinecoObstetricosHistorico(String fechaUltimaCitologia, String descripcionUltimaMamografia, String descripcionProcedimientosGinecologicos, String fechaUltimaMamografia, int duracionMenstruacion, String fechaUltimaRegla, String dolorMenstruacion, String descripcionUltimaCitologia, InfoDatos conceptoMenstruacion, String descUltimaEcografia, String fechaUltimaEcografia, String gInfoEmbarazos, String pInfoEmbarazos, String aInfoEmbarazos, String cInfoEmbarazos, String vInfoEmbarazos, String mInfoEmbarazos, String fechaUltimaDensimetriaOsea, String descUltimaDensimetriaOsea)
	{
		init(System.getProperty("TIPOBD"));

		this.fechaUltimaDensimetriaOsea = fechaUltimaDensimetriaOsea;
		this.descUltimaDensimetriaOsea = descUltimaDensimetriaOsea;
		this.fechaUltimaEcografia = fechaUltimaEcografia;
		this.descripcionUltimaEcografia = descUltimaEcografia;
		this.fechaUltimaCitologia=fechaUltimaCitologia;
		this.descripcionUltimaCitologia=descripcionUltimaCitologia;
		this.fechaUltimaMamografia=fechaUltimaMamografia;
		this.descripcionUltimaMamografia=descripcionUltimaMamografia;
		this.descripcionProcedimientosGinecologicos=descripcionProcedimientosGinecologicos;
		this.duracionMenstruacion=duracionMenstruacion;
		this.fechaUltimaRegla=fechaUltimaRegla;
		this.dolorMenstruacion=dolorMenstruacion;
		this.descripcionUltimaCitologia=descripcionUltimaCitologia;
		this.conceptoMenstruacion=conceptoMenstruacion;
		
		this.gInfoEmbarazos = gInfoEmbarazos;
		this.pInfoEmbarazos = pInfoEmbarazos;
		this.aInfoEmbarazos = aInfoEmbarazos;
		this.cInfoEmbarazos = cInfoEmbarazos;
		this.mInfoEmbarazos = mInfoEmbarazos;
		this.vInfoEmbarazos = vInfoEmbarazos;
	}

	

	/**
	 * Método que inserta un antecedente ginecoobstetrico historico en la fuente
	 * de datos. Como es transaccional recibe un paràmetro donde se especifíca
	 * en que punto queda esta inserción dentro de la transacción (la empieza?,
	 * esta en el medio o la termina)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoPaciente Código de la paciente
	 * @param selecDolorMenstruacion
	 * @return int Número de antecedentes ginecoobstetricos historicos
	 * insertados (1 bien, 0 problemas en la inserción)
	 * @throws SQLException
	 */
	@SuppressWarnings("deprecation")
	public int insertarTransaccional (Connection con, int codigoPaciente, String estado) throws SQLException
	{
		if (!estanVacios())  //-Verificar que no se inserte un registro en antecedentes e antecedentes historicos cuando todo los valores estan vacios  
		{
		

			if (!esPseudoVacio())
			{
				return antecedentesGinecoObstetricosHistoricoDao.insertarTransaccional(con, codigoPaciente, duracionMenstruacion, dolorMenstruacion, this.reestructurarFecha(fechaUltimaRegla), 
																					   conceptoMenstruacion.getCodigo(), fechaUltimaMamografia, descripcionUltimaMamografia,
																					   fechaUltimaCitologia, descripcionUltimaCitologia, descripcionProcedimientosGinecologicos,
																					   estado, fechaUltimaEcografia, descripcionUltimaEcografia, observacionesMenstruacion, 
																					   cicloMenstrual, gInfoEmbarazos, pInfoEmbarazos, p2500, p4000, aInfoEmbarazos,
																					   mayorA2, cInfoEmbarazos, vInfoEmbarazos, mInfoEmbarazos, finEmbarazoAnterior,
																					   finEmbarazoMayor1o5, prematuros, ectropicos, multiples, fechaUltimaDensimetriaOsea,
																					   descUltimaDensimetriaOsea,this.vag, 
																					   this. retencionPlacentaria, this.infeccionPostparto,this.malformacion, this.muertePerinatal,
																					   this.sangradoAnormal, this.flujoVaginal, this.enferTransSexual, this.cualEnferTransSex, this.cirugiaGineco,	
																					   this.cualCirugiaGineco, this.historiaInfertilidad, this.cualHistoInfertilidad,
																					   this.tipoEmbarazo,this.muertosAntes1Semana,this.muertosDespues1Semana,this.vivosActualmente
																					   );
				
			}
			else
			{
				//No se inserta porque es vacio pero se retorna 1 de todas maneras
				return 1;
			}
		}
		else
		{
			//No se inserta porque es vacio pero se retorna 1 de todas maneras
			return 1;			
		}
	}

	/**
	 * Método que cambia el formato de la fecha manejado por el usuario (dd-mm-
	 * aaaa ->estándar Latinoamericano) al formato manejado por la fuente de
	 * datos (yyyy-mm-dd -> estándar Norteamericano)
	 * 
	 * @param fecha Fecha en el formato manejado por el usuario
	 * @return String Fecha en el formato manejado por la fuente de datos
	 */
	public String reestructurarFecha(String fecha)
	{
		/*if( fecha != null )
		{ 
			String[] arregloFecha = (fecha).split("/");
			if( arregloFecha.length == 3 )
			{
				fecha = new String();
				fecha = arregloFecha[2]+"/"+arregloFecha[1]+"/"+arregloFecha[0];
			}					
		}
		return fecha;*/
		return UtilidadFecha.conversionFormatoFechaABD(fecha);		
	}

	/**
	 * Retorna la fecha de la última citología
	 * @return 			String, fecha formato 
	 */
	public String getFechaUltimaCitologia() 
	{
		return fechaUltimaCitologia;
	}

	/**
	 * Asigna la fecha de la última citología
	 * @param 		String, fechaUltimaCitologia
	 */
	public void setFechaUltimaCitologia(String fechaUltimaCitologia) 
	{
		this.fechaUltimaCitologia = fechaUltimaCitologia;
	}

	/**
	 * Retorna la descripción de la última mamografía.
	 * @return 			String, descripción
	 */
	public String getDescripcionUltimaMamografia() 
	{
		return descripcionUltimaMamografia;
	}

	/**
	 * Asigna la descripción de la última mamografía
	 * @param 		String, descripcionUltimaMamografia
	 */
	public void setDescripcionUltimaMamografia(String descripcionUltimaMamografia) 
	{
		this.descripcionUltimaMamografia = descripcionUltimaMamografia;
	}

	/**
	 * Retorna la descripción de los últimos procedimientos ginecológicos
	 * @return 			String, descripción
	 */
	public String getDescripcionProcedimientosGinecologicos() 
	{
		return descripcionProcedimientosGinecologicos;
	}

	/**
	 * Asigna la descripción de los últimos procedimientos ginecológicos
	 * @param 		String, descripcionProcedimientosGinecologicos
	 */
	public void setDescripcionProcedimientosGinecologicos(String descripcionProcedimientosGinecologicos) 
	{
		this.descripcionProcedimientosGinecologicos =
			descripcionProcedimientosGinecologicos;
	}
	
	/**
	 * Retorna la fecha de la última mamografía
	 * @return 			String, fecha 
	 */
	public String getFechaUltimaMamografia()
	{
		return fechaUltimaMamografia;
	}

	/**
	 * Asigna la fecha de la última mamografía
	 * @param 		String, fechaUltimaMamografia
	 */
	public void setFechaUltimaMamografia(String fechaUltimaMamografia) 
	{
		this.fechaUltimaMamografia = fechaUltimaMamografia;
	}

	/**
	 * Retorna la duración en días de la menstruación
	 * @return 			int, dias de la última menstruación
	 */
	public int getDuracionMenstruacion()
	{
		return duracionMenstruacion;
	}

	/**
	 * Retorna la duración en días de la menstruación
	 * @param 		int, dias duracionMenstruacion
	 */
	public void setDuracionMenstruacion(int duracionMenstruacion) 
	{
		this.duracionMenstruacion = duracionMenstruacion;
	}
	
	/**
	 * Retorna la fecha de la última regla
	 * @return 			String, fecha última regla en formato "dd/mm/aaaa"
	 */
	public String getFechaUltimaRegla() 
	{
		return fechaUltimaRegla;
	}

	/**
	 * Retorna la fecha de la última regla
	 * @param 		String, fechaUltimaRegla en formato "dd/mm/aaaa"
	 */
	public void setFechaUltimaRegla(String fechaUltimaRegla) 
	{
		this.fechaUltimaRegla = fechaUltimaRegla;
	}

	
	/**
	 * @return Returns the dolorMenstruacion.
	 */
	public String getDolorMenstruacion()
	{
		return dolorMenstruacion;
	}
	/**
	 * @param dolorMenstruacion The dolorMenstruacion to set.
	 */
	public void setDolorMenstruacion(String dolorMenstruacion)
	{
		this.dolorMenstruacion = dolorMenstruacion;
	}
	/**
	 * Retorna la descripción de la última citología
	 * @return 			String, descripción
	 */
	public String getDescripcionUltimaCitologia() 
	{
		return descripcionUltimaCitologia;
	}

	/**
	 * Asigna la descripción de la última citología
	 * @param 		String, descripcionUltimaCitologia
	 */
	public void setDescripcionUltimaCitologia(String descripcionUltimaCitologia)
	{
		this.descripcionUltimaCitologia = descripcionUltimaCitologia;
	}		  		
	
	/**
	 * Retorna el concepto de la menstruación
	 * @return 			InfoDatos, caracteristica / concepto menstruacion
	 */
	public InfoDatos getConceptoMenstruacion() 
	{
		return conceptoMenstruacion;
	}

	/**
	 * Asigna el concepto de la menstruación
	 * @param 		InfoDatos, conceptoMenstruacion 
	 */
	public void setConceptoMenstruacion(InfoDatos conceptoMenstruacion) 
	{
		this.conceptoMenstruacion = conceptoMenstruacion;
	}	

	/**
	 * Retorna la fecha de creacion de este antecedente historico.
	 * @return String
	 */
	public String getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * Retorna la hora de creacion de este antecedente historico.
	 * @return String
	 */
	public String getHoraCreacion() {
		return horaCreacion.substring(0, 5);
	}

	/**
	 * Establece la fecha de creacion de este antecedente historico.
	 * @param fechaCreacion La fecha de creacion de este antecedente
	 * historico a establecer
	 */
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * Establece la hora de creacion de este antecedente historico.
	 * @param horaCreacion La la hora de creacion de este antecedente historico
	 * a establecer
	 */
	public void setHoraCreacion(String horaCreacion) {
		this.horaCreacion = horaCreacion;
	}

	/**
	 * Returns the nombreConceptoMenstruacion.
	 * @return String
	 */
	public String getNombreConceptoMenstruacion() 
	{
		return this.conceptoMenstruacion.getValue();
	}

	/**
	 * Sets the nombreConceptoMenstruacion.
	 * @param nombreConceptoMenstruacion The nombreConceptoMenstruacion to set
	 */
	public void setNombreConceptoMenstruacion(String nombreConceptoMenstruacion) 
	{
		this.nombreConceptoMenstruacion = nombreConceptoMenstruacion;
	}

	/**
	 * Método que revisa si el antecedente historico es casi vacio (Debido a que manejamos 
	 * como boolean el dolor)
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public boolean esPseudoVacio ()
	{

		if (fechaUltimaEcografia!=null&&descripcionUltimaEcografia!=null&&fechaUltimaCitologia!=null&&descripcionUltimaMamografia!=null&&descripcionProcedimientosGinecologicos!=null&&fechaUltimaMamografia!=null&&descripcionUltimaCitologia!=null&&observacionesMenstruacion!=null&&fechaUltimaRegla!=null)
		{
			if(observacionesMenstruacion.trim().equals("") && dolorMenstruacion.equals("") && duracionMenstruacion==0&&cicloMenstrual==0&&nombreConceptoMenstruacion==null&&conceptoMenstruacion.getCodigo()==-1&&(fechaUltimaCitologia.trim()).equals("")&&(descripcionUltimaMamografia.trim()).equals("")&&(descripcionProcedimientosGinecologicos.trim()).equals("")&&(fechaUltimaMamografia.trim()).equals("")&&(fechaUltimaRegla.trim()).equals("")&&(descripcionUltimaCitologia.trim()).equals("")&&(descripcionUltimaEcografia.trim()).equals("")&&(fechaUltimaEcografia.trim()).equals(""))
			{
				if( !UtilidadCadena.noEsVacio(this.gInfoEmbarazos) && !UtilidadCadena.noEsVacio(this.pInfoEmbarazos) && !UtilidadCadena.noEsVacio(this.aInfoEmbarazos) && !UtilidadCadena.noEsVacio(this.cInfoEmbarazos) && !UtilidadCadena.noEsVacio(this.vInfoEmbarazos) && !UtilidadCadena.noEsVacio(this.mInfoEmbarazos) )
				{
						if( !UtilidadCadena.noEsVacio(this.fechaUltimaDensimetriaOsea) && !UtilidadCadena.noEsVacio(this.descUltimaDensimetriaOsea) )
						{
							{
								return true;
							}
						}	
				}
			}
		}
		return false;
	}
	
	/**
	 * Método que revisa si se ingreso algun dato    
	 * 		
	 * @return SI TODOS ESTAN VACIOS RETORNA TRUE
	 */
	@SuppressWarnings("deprecation")
	public boolean estanVacios()
	{	
		
			

		if (  !UtilidadCadena.noEsVacio(fechaUltimaEcografia) && !UtilidadCadena.noEsVacio(descripcionUltimaEcografia) &&
			  !UtilidadCadena.noEsVacio(fechaUltimaCitologia) && !UtilidadCadena.noEsVacio(descripcionUltimaMamografia) &&
			  !UtilidadCadena.noEsVacio(descripcionProcedimientosGinecologicos) && !UtilidadCadena.noEsVacio(fechaUltimaMamografia) &&
			  !UtilidadCadena.noEsVacio(descripcionUltimaCitologia) &&  !UtilidadCadena.noEsVacio(observacionesMenstruacion) && 
			  !UtilidadCadena.noEsVacio(fechaUltimaRegla) && !UtilidadCadena.noEsVacio(observacionesMenstruacion) && 
			  !UtilidadCadena.noEsVacio(dolorMenstruacion) && (duracionMenstruacion==0) && (cicloMenstrual==0) && 
			  !UtilidadCadena.noEsVacio(nombreConceptoMenstruacion) && (conceptoMenstruacion.getCodigo()==-1) && 
			  !UtilidadCadena.noEsVacio(fechaUltimaCitologia) && !UtilidadCadena.noEsVacio(descripcionUltimaMamografia) && 
			  !UtilidadCadena.noEsVacio(descripcionProcedimientosGinecologicos) && !UtilidadCadena.noEsVacio(fechaUltimaMamografia) && 
			  !UtilidadCadena.noEsVacio(fechaUltimaRegla) && !UtilidadCadena.noEsVacio(descripcionUltimaCitologia) && 
			  !UtilidadCadena.noEsVacio(descripcionUltimaEcografia) && !UtilidadCadena.noEsVacio(fechaUltimaEcografia) &&
              !UtilidadCadena.noEsVacio(observacionesMenstruacion) && !UtilidadCadena.noEsVacio(dolorMenstruacion) &&
			  (duracionMenstruacion==0) && (cicloMenstrual==0) && !UtilidadCadena.noEsVacio(nombreConceptoMenstruacion) &&
			  conceptoMenstruacion.getCodigo()==-1 && !UtilidadCadena.noEsVacio(fechaUltimaCitologia) &&
			  !UtilidadCadena.noEsVacio(descripcionUltimaMamografia) && !UtilidadCadena.noEsVacio(descripcionProcedimientosGinecologicos) && 
			  !UtilidadCadena.noEsVacio(fechaUltimaMamografia) && !UtilidadCadena.noEsVacio(fechaUltimaRegla) && 
			  !UtilidadCadena.noEsVacio(descripcionUltimaCitologia) && !UtilidadCadena.noEsVacio(descripcionUltimaEcografia) &&
			  !UtilidadCadena.noEsVacio(fechaUltimaEcografia) && !UtilidadCadena.noEsVacio(gInfoEmbarazos) && 
			  !UtilidadCadena.noEsVacio(pInfoEmbarazos) && !UtilidadCadena.noEsVacio(aInfoEmbarazos) && 
			  !UtilidadCadena.noEsVacio(cInfoEmbarazos) && !UtilidadCadena.noEsVacio(vInfoEmbarazos)  && 
			  !UtilidadCadena.noEsVacio(mInfoEmbarazos) && !UtilidadCadena.noEsVacio(p2500) && 
			  !UtilidadCadena.noEsVacio(p4000) && !UtilidadCadena.noEsVacio(mayorA2) && 
			  !UtilidadCadena.noEsVacio(finEmbarazoMayor1o5) && !UtilidadCadena.noEsVacio(prematuros) && 
			  !UtilidadCadena.noEsVacio(ectropicos) && !UtilidadCadena.noEsVacio(multiples)  && 
			  !UtilidadCadena.noEsVacio(fechaUltimaDensimetriaOsea) && !UtilidadCadena.noEsVacio(descUltimaDensimetriaOsea) &&
			  (this.getVag()!=0) 											&&
			  !UtilidadCadena.noEsVacio(this.getTipoEmbarazo()) 				&&
			  !UtilidadCadena.noEsVacio(this.vivosActualmente) 				&&
			  !UtilidadCadena.noEsVacio(this.getMuertosAntes1Semana())				&&
			  !UtilidadCadena.noEsVacio(this.getMuertosDespues1Semana())		&&
			  !UtilidadCadena.noEsVacio(this.getRetencionPlacentaria())	&&
			  !UtilidadCadena.noEsVacio(this.getInfeccionPostparto())	&&
			  !UtilidadCadena.noEsVacio(this.getMalformacion())			&&
			  !UtilidadCadena.noEsVacio(this.getMuertePerinatal())
			   )							
			
		{
			return true;
		}

		return false;
	}

	
	/**
	 * Retorna la fecha de la última ecografia que se realizó la paciente. No
	 * tiene ningun formato, se pueden insertar fechas de tipo "dd/mm/aaaa" o
	 * cadenas con información sobre la fecha como "hace dos meses", etc.
	 * @return String
	 */
	public String getFechaUltimaEcografia() 
	{
		return fechaUltimaEcografia;
	}

	/**
	 * Asigna la fecha de la última ecografia que se realizó la paciente. No
	 * tiene ningun formato, se pueden insertar fechas de tipo "dd/mm/aaaa" o
	 * cadenas con información sobre la fecha como "hace dos meses", etc.
	 * @param fechaUltimaEcografia The fechaUltimaEcografia to set
	 */
	public void setFechaUltimaEcografia(String fechaUltimaEcografia) 
	{
		this.fechaUltimaEcografia = fechaUltimaEcografia;
	}

	/**
	 * Retorna la Descripción de la última ecografia que se hizó la paciente.
	 * @return String
	 */
	public String getDescripcionUltimaEcografia() 
	{
		return descripcionUltimaEcografia;
	}

	/**
	 * Asigna la Descripción de la última ecografia que se hizó la paciente.
	 * @param descripcionUltimaEcografia The descripcionUltimaEcografia to set
	 */
	public void setDescripcionUltimaEcografia(String descripcionUltimaEcografia) 
	{
		this.descripcionUltimaEcografia = descripcionUltimaEcografia;
	}

	/**
	 * Retorna las observaciones de las caracteristicas de la menstruacion
	 * @return String
	 */
	public String getObservacionesMenstruacion() 
	{
		return observacionesMenstruacion;
	}

	/**
	 * Asigna las observaciones de las caracteristicas de la menstruacion
	 * @param observacionesMenstruacion The observacionesMenstruacion to set
	 */
	public void setObservacionesMenstruacion(String observacionesMenstruacion) 
	{
		this.observacionesMenstruacion = observacionesMenstruacion;
	}

	/**
	 * Retorna la duración en dias del ciclo menstrual
	 * @return int
	 */
	public int getCicloMenstrual() 
	{
		return cicloMenstrual;
	}

	/**
	 * Asigna la duración en dias del ciclo menstrual
	 * @param cicloMenstrual The cicloMenstrual to set
	 */
	public void setCicloMenstrual(int cicloMenstrual) 
	{
		this.cicloMenstrual = cicloMenstrual;
	}

	/**
	 * Returns the aInfoEmbarazos.
	 * @return String
	 */
	public String getAInfoEmbarazos()
	{
		return aInfoEmbarazos;
	}

	/**
	 * Returns the cInfoEmbarazos.
	 * @return String
	 */
	public String getCInfoEmbarazos()
	{
		return cInfoEmbarazos;
	}

	/**
	 * Returns the gInfoEmbarazos.
	 * @return String
	 */
	public String getGInfoEmbarazos()
	{
		return gInfoEmbarazos;
	}

	/**
	 * Returns the mInfoEmbarazos.
	 * @return String
	 */
	public String getMInfoEmbarazos()
	{
		return mInfoEmbarazos;
	}

	/**
	 * Returns the pInfoEmbarazos.
	 * @return String
	 */
	public String getPInfoEmbarazos()
	{
		return pInfoEmbarazos;
	}

	/**
	 * Returns the vInfoEmbarazos.
	 * @return String
	 */
	public String getVInfoEmbarazos()
	{
		return vInfoEmbarazos;
	}

	/**
	 * Sets the aInfoEmbarazos.
	 * @param aInfoEmbarazos The aInfoEmbarazos to set
	 */
	public void setAInfoEmbarazos(String aInfoEmbarazos)
	{
		this.aInfoEmbarazos = aInfoEmbarazos;
	}

	/**
	 * Sets the cInfoEmbarazos.
	 * @param cInfoEmbarazos The cInfoEmbarazos to set
	 */
	public void setCInfoEmbarazos(String cInfoEmbarazos)
	{
		this.cInfoEmbarazos = cInfoEmbarazos;
	}

	/**
	 * Sets the gInfoEmbarazos.
	 * @param gInfoEmbarazos The gInfoEmbarazos to set
	 */
	public void setGInfoEmbarazos(String gInfoEmbarazos)
	{
		this.gInfoEmbarazos = gInfoEmbarazos;
	}

	/**
	 * Sets the mInfoEmbarazos.
	 * @param mInfoEmbarazos The mInfoEmbarazos to set
	 */
	public void setMInfoEmbarazos(String mInfoEmbarazos)
	{
		this.mInfoEmbarazos = mInfoEmbarazos;
	}

	/**
	 * Sets the pInfoEmbarazos.
	 * @param pInfoEmbarazos The pInfoEmbarazos to set
	 */
	public void setPInfoEmbarazos(String pInfoEmbarazos)
	{
		this.pInfoEmbarazos = pInfoEmbarazos;
	}

	/**
	 * Sets the vInfoEmbarazos.
	 * @param vInfoEmbarazos The vInfoEmbarazos to set
	 */
	public void setVInfoEmbarazos(String vInfoEmbarazos)
	{
		this.vInfoEmbarazos = vInfoEmbarazos;
	}

	/**
	 * Returns the descUltimaDensimetriaOsea.
	 * @return String
	 */
	public String getDescUltimaDensimetriaOsea()
	{
		return descUltimaDensimetriaOsea;
	}

	/**
	 * Returns the fechaUltimaDensimetriaOsea.
	 * @return String
	 */
	public String getFechaUltimaDensimetriaOsea()
	{
		return fechaUltimaDensimetriaOsea;
	}

	/**
	 * Sets the descUltimaDensimetriaOsea.
	 * @param descUltimaDensimetriaOsea The descUltimaDensimetriaOsea to set
	 */
	public void setDescUltimaDensimetriaOsea(String descUltimaDensimetriaOsea)
	{
		this.descUltimaDensimetriaOsea = descUltimaDensimetriaOsea;
	}

	/**
	 * Sets the fechaUltimaDensimetriaOsea.
	 * @param fechaUltimaDensimetriaOsea The fechaUltimaDensimetriaOsea to set
	 */
	public void setFechaUltimaDensimetriaOsea(String fechaUltimaDensimetriaOsea)
	{
		this.fechaUltimaDensimetriaOsea = fechaUltimaDensimetriaOsea;
	}
	
	/**
	 * @return Retorna p2500.
	 */
	public String getP2500() {
		return p2500;
	}
	/**
	 * @param Asigna p2500.
	 */
	public void setP2500(String p2500) {
		this.p2500 = p2500;
	}
	/**
	 * @return Retorna p4000.
	 */
	public String getP4000() {
		return p4000;
	}
	/**
	 * @param Asigna p4000.
	 */
	public void setP4000(String p4000) {
		this.p4000 = p4000;
	}

	
	/**
	 * @return Retorna ectropicos.
	 */
	public String getEctropicos() {
		return ectropicos;
	}
	/**
	 * @param Asigna ectropicos.
	 */
	public void setEctropicos(String ectropicos) {
		this.ectropicos = ectropicos;
	}
	/**
	 * @return Retorna finEmbarazoAnterior.
	 */
	public String getFinEmbarazoAnterior() {
		return finEmbarazoAnterior;
	}
	/**
	 * @param Asigna finEmbarazoAnterior.
	 */
	public void setFinEmbarazoAnterior(String finEmbarazoAnterior) {
		this.finEmbarazoAnterior = finEmbarazoAnterior;
	}
	/**
	 * @return Retorna finEmbarazoMayor1o5.
	 */
	public String getFinEmbarazoMayor1o5() {
		return finEmbarazoMayor1o5;
	}
	/**
	 * @param Asigna finEmbarazoMayor1o5.
	 */
	public void setFinEmbarazoMayor1o5(String finEmbarazoMayor1o5) {
		this.finEmbarazoMayor1o5 = finEmbarazoMayor1o5;
	}
	/**
	 * @return Retorna mayorA2.
	 */
	public String getMayorA2() {
		return mayorA2;
	}
	/**
	 * @param Asigna mayorA2.
	 */
	public void setMayorA2(String mayorA2) {
		this.mayorA2 = mayorA2;
	}

	/**
	 * @return Retorna multiples.
	 */
	public String getMultiples() {
		return multiples;
	}
	/**
	 * @param Asigna multiples.
	 */
	public void setMultiples(String multiples) {
		this.multiples = multiples;
	}
	/**
	 * @return Retorna prematuros.
	 */
	public String getPrematuros() {
		return prematuros;
	}
	/**
	 * @param Asigna prematuros.
	 */
	public void setPrematuros(String prematuros) {
		this.prematuros = prematuros;
	}


	
	public String getInfeccionPostparto() {
		return infeccionPostparto;
	}

	public void setInfeccionPostparto(String infeccion_postparto) {
		this.infeccionPostparto = infeccion_postparto;
	}

	public String getMalformacion() {
		return malformacion;
	}

	public void setMalformacion(String malformacion) {
		this.malformacion = malformacion;
	}

	public String getMuertePerinatal() {
		return muertePerinatal;
	}

	public void setMuertePerinatal(String muerte_perinatal) {
		this.muertePerinatal = muerte_perinatal;
	}

	
	public String getRetencionPlacentaria() {
		return retencionPlacentaria;
	}

	public void setRetencionPlacentaria(String retencion_placentaria) {
		this.retencionPlacentaria = retencion_placentaria;
	}

	

	public int getVag() {
		return vag;
	}

	public void setVag(int vag) {
		this.vag = vag;
	}

	public static AntecedentesGinecoObstetricosHistoricoDao getAntecedentesGinecoObstetricosHistoricoDao() {
		return antecedentesGinecoObstetricosHistoricoDao;
	}

	public static void setAntecedentesGinecoObstetricosHistoricoDao(
			AntecedentesGinecoObstetricosHistoricoDao antecedentesGinecoObstetricosHistoricoDao) {
		AntecedentesGinecoObstetricosHistorico.antecedentesGinecoObstetricosHistoricoDao = antecedentesGinecoObstetricosHistoricoDao;
	}

	public String getCirugiaGineco() {
		return cirugiaGineco;
	}

	public void setCirugiaGineco(String cirugia_gineco) {
		this.cirugiaGineco = cirugia_gineco;
	}

	public String getCualCirugiaGineco() {
		return cualCirugiaGineco;
	}

	public void setCualCirugiaGineco(String cual_cirugia_gineco) {
		this.cualCirugiaGineco = cual_cirugia_gineco;
	}

	public String getCualEnferTransSex() {
		return cualEnferTransSex;
	}

	public void setCualEnferTransSex(String cual_enfer_trans_sex) {
		this.cualEnferTransSex = cual_enfer_trans_sex;
	}

	public String getCualHistoInfertilidad() {
		return cualHistoInfertilidad;
	}

	public void setCualHistoInfertilidad(String cual_histo_infertilidad) {
		this.cualHistoInfertilidad = cual_histo_infertilidad;
	}

	public String getEnferTransSexual() {
		return enferTransSexual;
	}

	public void setEnferTransSexual(String enfer_trans_sexual) {
		this.enferTransSexual = enfer_trans_sexual;
	}

	public String getFlujoVaginal() {
		return flujoVaginal;
	}

	public void setFlujoVaginal(String flujo_vaginal) {
		this.flujoVaginal = flujo_vaginal;
	}

	public String getHistoriaInfertilidad() {
		return historiaInfertilidad;
	}

	public void setHistoriaInfertilidad(String historia_infertilidad) {
		this.historiaInfertilidad = historia_infertilidad;
	}

	public String getSangradoAnormal() {
		return sangradoAnormal;
	}

	public void setSangradoAnormal(String sangrado_anormal) {
		this.sangradoAnormal = sangrado_anormal;
	}
	public String getTipoEmbarazo()
	{
		return tipoEmbarazo;
	}

	public void setTipoEmbarazo(String tipoEmbarazo)
	{
		this.tipoEmbarazo = tipoEmbarazo;
	}
	public void setMuertosAntes1Semana(String muertosAntes1Semana)
	{
		this.muertosAntes1Semana = muertosAntes1Semana;
	}

	public void setMuertosDespues1Semana(String muertosDespues1Semana)
	{
		this.muertosDespues1Semana = muertosDespues1Semana;
	}

	public String getMuertosAntes1Semana()
	{
		return muertosAntes1Semana;
	}

	public String getMuertosDespues1Semana()
	{
		return muertosDespues1Semana;
	}

	public String getVivosActualmente()
	{
		return vivosActualmente;
	}

	public void setVivosActualmente(String vivosActualmente)
	{
		this.vivosActualmente = vivosActualmente;
	}
}
