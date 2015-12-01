/* Princeton S.A (Parquesoft-Manizales)
*  Andrés Mauricio Ruiz Vélez
*  Creado 14-nov-2006 11:34:44
*/


package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadValidacion;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.CrecimientoDesarrolloForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.CrecimientoDesarrollo;


public class CrecimientoDesarrolloAction extends Action	
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(CrecimientoDesarrolloAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con = null;
		try {
			if (form instanceof CrecimientoDesarrolloForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				CrecimientoDesarrolloForm forma = (CrecimientoDesarrolloForm) form;
				String estado=forma.getEstado();
				logger.warn("\n\n  El Estado en CrecimientoDesarrolloAction [" + estado + "] \n\n ");

				if(estado == null)
				{
					forma.reset();	
					logger.warn("Estado no valido dentro del flujo de CrecimientoDesarrolloAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else
				{
					//Valida que el  paciente esté cargado
					if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
					}
					//Validar que el usuario sea un profesional de la salud
					if(!UtilidadValidacion.esProfesionalSalud(usuario))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No es profesional de la salud", "errors.usuario.noAutorizado", true);
					}
					if (estado.equals("empezar"))
					{
						return accionEmpezar(mapping,con,forma, paciente, usuario);
					}
					else
						if(estado.equals("graficar"))
						{
							return accionGraficar(mapping,con,forma, paciente, usuario);
						}
						else
						{
							request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
							UtilidadBD.cerrarConexion(con);
							return mapping.findForward("paginaError");
						}
				}//else
			}//if
			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}

	/**
	 * Metodo para graficar de acuerdo al perfil del paciente y al boton pulsado
	 * @param mapping
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGraficar(ActionMapping mapping, Connection con, CrecimientoDesarrolloForm forma, PersonaBasica paciente, UsuarioBasico usuario) throws SQLException 
	{
		CrecimientoDesarrollo mundo = new CrecimientoDesarrollo();
		HashMap parametros = new HashMap();
		HashMap mpx = new HashMap();
		
		
		//------------------------------------------------ Consultar la información parametrizada. -------------------------------------------------
		//---NOTA: IMPORTANTE, este codigo se debe descomentarear para generar denuevo las plantillas, en caso de algun cambio en alguna de ellas. 
		//--- Para menores a 36 Meses
		int[] vEdad=UtilidadFecha.calcularVectorEdad(UtilidadFecha.conversionFormatoFechaABD(paciente.getFechaNacimiento()),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		float em = (vEdad[0]*12)+vEdad[1]+((float)vEdad[2]/30);
		if ( em <= 36  )  
		{
			if ( paciente.getCodigoSexo() == ConstantesBD.codigoSexoFemenino )
			{
				if (forma.getTipoGrafica() == 1) 
				{
					//---- Edad X Peso
					//parametros.put("nroConsulta", "2");
					//forma.setMapaEdadPeso( crearVectores(mundo.consultarInformacion(con, parametros)) );
					
					//---- Consultar los ejes EDAD X PESO  
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloF036_EdXP);
					forma.getMapaEdadPeso().putAll(mundo.consultarInformacion(con, parametros));
					
					//---- Edad X Estatura
					//parametros.put("nroConsulta", "8");
					//forma.setMapaEdadEstatura( crearVectores(mundo.consultarInformacion(con, parametros)) );

					//---- Consultar los ejes EDAD X ESTATURA  
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloF036_EXE);
					forma.getMapaEdadEstatura().putAll(mundo.consultarInformacion(con, parametros));
				}	
				if (forma.getTipoGrafica() == 2) 
				{
					//---- Estatura X Peso Percentil
					//parametros.put("nroConsulta", "10");
					//forma.setMapaPesoEstatura( crearVectores(mundo.consultarInformacion(con, parametros)) );

					//---- Consultar los ejes ESTATURA X PESO PERCENTIL  
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloF220_EsXP);
					forma.getMapaPesoEstatura().putAll(mundo.consultarInformacion(con, parametros));
				}	
	
				if (forma.getTipoGrafica() == 3) 
				{
					//---- Edad X Perimetro Cefalico 
					//parametros.put("nroConsulta", "14");
					//forma.setMapaEdadPerCefalico( crearVectores(mundo.consultarInformacion(con, parametros)) );

					//---- Consultar los ejes Edad X Perimetro Cefalico   
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloF036_PCE);
					forma.getMapaEdadPerCefalico().putAll(mundo.consultarInformacion(con, parametros));
					
					//---- Estatura X Peso HC
					//parametros.put("nroConsulta", "13");
					//forma.setMapaPesoEstatura( crearVectores(mundo.consultarInformacion(con, parametros)) );

					//---- Consultar los ejes ESTATURA X PESO HC   
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloF036_EsXP);
					forma.getMapaPesoEstatura().putAll(mundo.consultarInformacion(con, parametros));

				}	
	
				//---- Edad X IMC 
				//parametros.put("nroConsulta", "16");
				//forma.setMapaEdadImc( crearVectores(mundo.consultarInformacion(con, parametros)) );
			}//if sexo femenino
			else  //0-36 Masculino
			{
				if (forma.getTipoGrafica() == 1) 
				{
					//---- Edad X Peso
					//parametros.put("nroConsulta", "4");
					//forma.setMapaEdadPeso( crearVectores(mundo.consultarInformacion(con, parametros)) );

					//---- Consultar los ejes EDAD X PESO  
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloM036_EdXP);
					forma.getMapaEdadPeso().putAll(mundo.consultarInformacion(con, parametros));
					
					//---- Edad X Estatura
					//parametros.put("nroConsulta", "6");
					//forma.setMapaEdadEstatura( crearVectores(mundo.consultarInformacion(con, parametros)) );

					//---- Consultar los ejes EDAD X ESTATURA  
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloM036_EXE);
					forma.getMapaEdadEstatura().putAll(mundo.consultarInformacion(con, parametros));
				
				}	
				if (forma.getTipoGrafica() == 2) 
				{
					//---- Estatura X Peso Percentil
					//parametros.put("nroConsulta", "11");
					//forma.setMapaPesoEstatura( crearVectores(mundo.consultarInformacion(con, parametros)) );

					//---- Consultar los ejes ESTATURA X PESO Percentil  
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloM220_EsXP);
					forma.getMapaPesoEstatura().putAll(mundo.consultarInformacion(con, parametros));
				}	

				if (forma.getTipoGrafica() == 3) 
				{
					//---- Edad X Perimetro Cefalico 
					//parametros.put("nroConsulta", "15");
					//forma.setMapaEdadPerCefalico( crearVectores(mundo.consultarInformacion(con, parametros)) );
					
					//---- Consultar los ejes Edad X Perimetro Cefalico   
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloM036_PCE);
					forma.getMapaEdadPerCefalico().putAll(mundo.consultarInformacion(con, parametros));
					
					//---- Estatura X Peso HC Niño
					//parametros.put("nroConsulta", "12");
					//forma.setMapaPesoEstatura( crearVectores(mundo.consultarInformacion(con, parametros)) );

					//---- Consultar los ejes ESTATURA X PESO HC Niño   
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloM036_EsXP);
					forma.getMapaPesoEstatura().putAll(mundo.consultarInformacion(con, parametros));
				}
	
				//---- Edad X IMC 
				//parametros.put("nroConsulta", "17");
				//forma.setMapaEdadImc( crearVectores(mundo.consultarInformacion(con, parametros)) );
			}	
				
		}//if de 0 a 36
		else  //-- DE 3 A 22
		{

			if ( paciente.getCodigoSexo() == ConstantesBD.codigoSexoFemenino )
			{
				if (forma.getTipoGrafica() == 1) 
				{
					//---- Edad X Peso
					//parametros.put("nroConsulta", "3");
					//forma.setMapaEdadPeso( crearVectores(mundo.consultarInformacion(con, parametros)) );
					
					//---- Consultar los ejes EDAD X PESO  
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloF220_EdXP);
					forma.getMapaEdadPeso().putAll(mundo.consultarInformacion(con, parametros));
							
					//---- Edad X Estatura
					//parametros.put("nroConsulta", "9");
					//forma.setMapaEdadEstatura( crearVectores(mundo.consultarInformacion(con, parametros)) );
					
					//---- Consultar los ejes EDAD X ESTATURA  
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloF220_EXE);
					forma.getMapaEdadEstatura().putAll(mundo.consultarInformacion(con, parametros));					
				}	
				if (forma.getTipoGrafica() == 2) 
				{
					//---- Estatura X Peso Percentil
					//parametros.put("nroConsulta", "10");
					//forma.setMapaPesoEstatura( crearVectores(mundo.consultarInformacion(con, parametros)) );
					
					//---- Consultar los ejes ESTATURA X PESO   
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloF220_EsXP);
					forma.getMapaPesoEstatura().putAll(mundo.consultarInformacion(con, parametros));					
				}	
				if (forma.getTipoGrafica() == 3) 
				{
					//---- Edad X Perimetro Cefalico 
					//parametros.put("nroConsulta", "14");
					//forma.setMapaEdadPerCefalico( crearVectores(mundo.consultarInformacion(con, parametros)) );

					//---- Consultar los ejes Edad X Perimetro Cefalico   
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloF036_PCE);
					forma.getMapaEdadPerCefalico().putAll(mundo.consultarInformacion(con, parametros));
					
					//---- Estatura X Peso HC
					//parametros.put("nroConsulta", "13");
					//forma.setMapaPesoEstatura( crearVectores(mundo.consultarInformacion(con, parametros)) );

					//---- Consultar los ejes ESTATURA X PESO HC   
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloF036_EsXP);
					forma.getMapaPesoEstatura().putAll(mundo.consultarInformacion(con, parametros));
					
				}	
				if (forma.getTipoGrafica() == 4) 
				{
					//---- Edad X IMC 
					//parametros.put("nroConsulta", "16");
					//forma.setMapaEdadImc( crearVectores(mundo.consultarInformacion(con, parametros)) );

					//---- Consultar los ejes Edad X Perimetro Cefalico   
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloF220_IMCXE);
					forma.getMapaEdadImc().putAll(mundo.consultarInformacion(con, parametros));					
				}	
			}//if sexo femenino
			else
			{
				if (forma.getTipoGrafica() == 1) 
				{
					//---- Edad X Peso
					//parametros.put("nroConsulta", "5");
					//forma.setMapaEdadPeso( crearVectores(mundo.consultarInformacion(con, parametros)) );
		
					//---- Consultar los ejes EDAD X PESO  
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloM220_EdXP);
					forma.getMapaEdadPeso().putAll(mundo.consultarInformacion(con, parametros));
					
					//---- Edad X Estatura
					//parametros.put("nroConsulta", "7");
					//forma.setMapaEdadEstatura( crearVectores(mundo.consultarInformacion(con, parametros)) );
					
					//---- Consultar los ejes EDAD X ESTATURA  
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloM220_EXE);
					forma.getMapaEdadEstatura().putAll(mundo.consultarInformacion(con, parametros));										
				}	

				if (forma.getTipoGrafica() == 2) 
				{
					//---- Estatura X Peso Percentil Niño
					//parametros.put("nroConsulta", "11");
					//forma.setMapaPesoEstatura( crearVectores(mundo.consultarInformacion(con, parametros)) );

					//---- Consultar los ejes ESTATURA X PESO   
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloM220_EsXP);
					forma.getMapaPesoEstatura().putAll(mundo.consultarInformacion(con, parametros));					
				}	
				if (forma.getTipoGrafica() == 3) 
				{
					//---- Edad X Perimetro Cefalico Niño
					/*parametros.put("nroConsulta", "15");
					HashMap mp = mundo.consultarInformacion(con, parametros);
					forma.setMapaEdadPerCefalico( crearVectores(mundo.consultarInformacion(con, parametros)) );
					*/
					
					//---- Consultar los ejes Edad X Perimetro Cefalico   
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloM036_PCE);
					forma.getMapaEdadPerCefalico().putAll(mundo.consultarInformacion(con, parametros));
					
					//---- Estatura X Peso HC Niño
					//parametros.put("nroConsulta", "12");
					//forma.setMapaPesoEstatura( crearVectores(mundo.consultarInformacion(con, parametros)) );

					//---- Consultar los ejes ESTATURA X PESO HC   
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloM036_EsXP);
					forma.getMapaPesoEstatura().putAll(mundo.consultarInformacion(con, parametros));

				}	
				if (forma.getTipoGrafica() == 4) 
				{
					//---- Edad X IMC 
					//parametros.put("nroConsulta", "17");
					//forma.setMapaEdadImc( crearVectores(mundo.consultarInformacion(con, parametros)) );

					//---- Consultar los ejes Edad X Perimetro Cefalico   
					parametros.put("nroConsulta", "18");
					parametros.put("plantilla",""+ConstantesBD.codTpGrafCurvaCreciDlloM220_IMCXE);
					forma.getMapaEdadImc().putAll(mundo.consultarInformacion(con, parametros));					
				}	
			}	
		}

		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("grafica");
	}

	/**
	 * Metodo para cargar la informacion del paciente 
	 * @param mapping
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, Connection con, CrecimientoDesarrolloForm forma, PersonaBasica paciente, UsuarioBasico usuario) throws SQLException
	{
		CrecimientoDesarrollo mundo = new CrecimientoDesarrollo();
		HashMap parametros = new HashMap();
		HashMap mpx = new HashMap();
		forma.reset();
		
		//----- Consultar la informacion del paciente relacionada con los examenes fisicos.
		parametros.put("nroConsulta", "1");
		parametros.put("codigoPaciente", paciente.getCodigoPersona()+"");
		parametros.put("institucion", usuario.getCodigoInstitucionInt()+"");
		
		parametros.put("fechaCorte", System.getProperty("FECHACORTECURVASCRECIMIENTO"));
		parametros.put("horaCorte", System.getProperty("HORACORTECURVASCRECIMIENTO"));
		
		forma.setMapaCrecimientoDesarrollo(mundo.consultarInformacion(con, parametros));

		//-- Colocar la información del paciente de forma adecuada para que se pueda 
		//-- graficar facilmente en JavaScritp.

		   int numRows = util.UtilidadCadena.vInt(forma.getMapaCrecimientoDesarrollo("numRegistros")+""); 
		   String aux="";
		   HashMap mp = new HashMap();
		   int indice = 0;
		   
		   logger.info("\n\n NUMERO DE REGISTROS [" + numRows +"]  \n\n");
		   
		   for (int i=0;i<numRows;i++)
			{
			    //-- Bloque para verificar que compare una sola vez cada numero de solicitud con el bucle interno.
			   	if ( i == 0) 
			   		{ aux = forma.getMapaCrecimientoDesarrollo("solicitud_" + i)+""; }
			   	else
			   		{
						if ( aux.equals(forma.getMapaCrecimientoDesarrollo("solicitud_" + i)+"") )
						{
							aux = forma.getMapaCrecimientoDesarrollo("solicitud_" + i)+""; 
							continue;
						}
						else 
				   			{
								aux = forma.getMapaCrecimientoDesarrollo("solicitud_" + i)+"";
								indice++;
				   			}
			   		}
			   	
				for (int j=0;j<numRows;j++)
				{
					if ( aux.equals(forma.getMapaCrecimientoDesarrollo("solicitud_" + j)+"") )
					{
						String fv = forma.getMapaCrecimientoDesarrollo("fecha_" + j)+"";
						String fn = forma.getMapaCrecimientoDesarrollo("fecha_nacimiento_" + j)+"";
						String edadAnios = forma.getMapaCrecimientoDesarrollo("edad_anios_" + j)+"";
						
						mp.put("fecha_"+indice, fv);
						mp.put("fecha_nacimiento_"+indice, fn);
						//mp.put("edad_anios_"+indice, edadAnios);
						String[] fnTemp=fn.split("-");
						String[] fvTemp=fv.split("-");
						
						int[] vEdad=UtilidadFecha.calcularVectorEdad(Integer.parseInt(fnTemp[0]), Integer.parseInt(fnTemp[1]), Integer.parseInt(fnTemp[2]), Integer.parseInt(fvTemp[2]), Integer.parseInt(fvTemp[1]), Integer.parseInt(fvTemp[0]));
						
						float em = (vEdad[0]*12)+vEdad[1]+((float)vEdad[2]/30);
						float ea = vEdad[0]+((float)vEdad[1]/12)+((float)vEdad[2]/365);
						mp.put("edad_meses_"+indice, em+"");
						mp.put("edad_anios_"+indice, ea+"");
						
						if ( util.UtilidadCadena.vInt(forma.getMapaCrecimientoDesarrollo("cod_signo_vital_" + j)+"") == ConstantesBD.codigoSignoVitalTalla )
						{
							mp.put("talla_"+indice, forma.getMapaCrecimientoDesarrollo("valor_" + j)+"");
						}
						if ( util.UtilidadCadena.vInt(forma.getMapaCrecimientoDesarrollo("cod_signo_vital_" + j)+"") == ConstantesBD.codigoSignoVitalPeso )
						{
							mp.put("peso_"+indice, forma.getMapaCrecimientoDesarrollo("valor_" + j)+"");
						}
						if ( util.UtilidadCadena.vInt(forma.getMapaCrecimientoDesarrollo("cod_signo_vital_" + j)+"") == ConstantesBD.codigoSignoVitalIMC )
						{
							mp.put("imc_"+indice, forma.getMapaCrecimientoDesarrollo("valor_" + j)+"");
						}
						if ( util.UtilidadCadena.vInt(forma.getMapaCrecimientoDesarrollo("cod_signo_vital_" + j)+"") == ConstantesBD.codigoSignoVitalPerimetroCefalico)
						{
							mp.put("perCefalico_"+indice, forma.getMapaCrecimientoDesarrollo("valor_" + j)+"");
						}
					}
				}
			}
		   
		   
		   if(!forma.getTallaActual().equals("")||!forma.getPesoActual().equals("")||!forma.getImcActual().equals("")||!forma.getPerCefalicoActual().trim().equals(""))
		   {
			   String[] fnTemp=UtilidadFecha.conversionFormatoFechaABD(paciente.getFechaNacimiento()).split("-");
			   String[] fvTemp=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()).split("-");
	
			   int[] vEdad=UtilidadFecha.calcularVectorEdad(Integer.parseInt(fnTemp[0]), Integer.parseInt(fnTemp[1]), Integer.parseInt(fnTemp[2]), Integer.parseInt(fvTemp[2]), Integer.parseInt(fvTemp[1]), Integer.parseInt(fvTemp[0]));
			   
			   float em = (vEdad[0]*12)+vEdad[1]+((float)vEdad[2]/30);
			   float ea = vEdad[0]+((float)vEdad[1]/12)+((float)vEdad[2]/365);
			   if(numRows>0)
				   indice++;
			   mp.put("fecha_"+indice, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			   mp.put("fecha_nacimiento_"+indice, UtilidadFecha.conversionFormatoFechaABD(paciente.getFechaNacimiento()));
			   mp.put("talla_"+indice,forma.getTallaActual());
			   mp.put("peso_"+indice,forma.getPesoActual());
			   mp.put("imc_"+indice,forma.getImcActual());
			   mp.put("perCefalico_"+indice,forma.getPerCefalicoActual());
			   mp.put("edad_meses_"+indice, em+"");
			   mp.put("edad_anios_"+indice, ea+"");
			   numRows=numRows+1;
			   
			   
			   //limpiar los valores nuevamente.
			   forma.setTallaActual("");
			   forma.setPesoActual("");
			   forma.setImcActual("");
			   forma.setPerCefalicoActual("");
		   }
		   
		   
		   
		   
		   
		   
		   
		//-- colocar la información para listarla en el JSP.
		forma.setMapaCrecimientoDesarrollo(mp);   
		if (numRows > 0)
		{
			forma.setMapaCrecimientoDesarrollo("numRegistros", (indice+1) +"");
		}
		else
		{
			forma.setMapaCrecimientoDesarrollo("numRegistros","0");
		}
		
		logger.info("\n\n  setMapaCrecimientoDesarrollo NUMROW [" + forma.getMapaCrecimientoDesarrollo("numRegistros") +"]  \n\n"); 

		  //-- Colocar la informacion en forma de cadena. 	   
		   String peso = "", talla = "", perCefalico= "", imc="", edadAnios="", edadMeses ="";
		   for (int i=0;i<=indice;i++)
			{	
			   if (i==0)
			   {
					talla += UtilidadCadena.vString(forma.getMapaCrecimientoDesarrollo("talla_" + i)+"");
					peso += UtilidadCadena.vString(forma.getMapaCrecimientoDesarrollo("peso_" + i)+"");
					imc += UtilidadCadena.vString(forma.getMapaCrecimientoDesarrollo("imc_" + i)+"");
					perCefalico += UtilidadCadena.vString(forma.getMapaCrecimientoDesarrollo("perCefalico_" + i)+"");
					edadAnios += UtilidadCadena.vString(forma.getMapaCrecimientoDesarrollo("edad_anios_" + i)+"");
					edadMeses += UtilidadCadena.vString(forma.getMapaCrecimientoDesarrollo("edad_meses_" + i)+"");
			   }
			   else
			   {
					talla += "&" + UtilidadCadena.vString(forma.getMapaCrecimientoDesarrollo("talla_" + i)+"");
					peso += "&" + UtilidadCadena.vString(forma.getMapaCrecimientoDesarrollo("peso_" + i)+"");
					imc += "&" + UtilidadCadena.vString(forma.getMapaCrecimientoDesarrollo("imc_" + i)+"");
					perCefalico += "&" + UtilidadCadena.vString(forma.getMapaCrecimientoDesarrollo("perCefalico_" + i)+"");
					edadAnios += "&" + UtilidadCadena.vString(forma.getMapaCrecimientoDesarrollo("edad_anios_" + i)+"");
					edadMeses += "&" + UtilidadCadena.vString(forma.getMapaCrecimientoDesarrollo("edad_meses_" + i)+"");
			   }
			}
		
		  		
		//-- Colocar la información a graficar
		forma.setMapaCrecimientoDesarrollo("talla", talla);   
		forma.setMapaCrecimientoDesarrollo("peso", peso);   
		forma.setMapaCrecimientoDesarrollo("imc", imc);   
		forma.setMapaCrecimientoDesarrollo("perCefalico", perCefalico);   
		forma.setMapaCrecimientoDesarrollo("edadAnios", edadAnios);   
		forma.setMapaCrecimientoDesarrollo("edadMeses", edadMeses);   

		  
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Funcion para crear Vectores.
	 * @param mapaEdadPeso
	 * @return
	 */
	private HashMap crearVectores(HashMap mapa) 
	{
		HashMap mp = new HashMap();
        int nroRows = util.UtilidadCadena.vInt(mapa.get("numRegistros")+"");
        String ejeX = "", ejeY = "", percentil = "";
        int nroPercentil = 0;
        
        percentil = mapa.get("p_O")+""; 

        
         for (int j=0; j<nroRows;j++ ) 
         { 
       	    if ( !percentil.equals(mapa.get("p_" + j)+"") )
        	{
        		mp.put("nroPercentil_"+(nroPercentil++), ejeY);
        		ejeY="";
        		
        		if (nroPercentil < 3)
        		{
	        		mp.put("ejeX", ejeX);
	        		ejeX="";
        		}	
        	}

    		if (nroPercentil < 3)
    		{
	       	    if (ejeX.equals("")) 
	       	    	ejeX = mapa.get("x_" + j)+"";
	           	else 
	           		ejeX += "&" + mapa.get("x_" + j)+"";
    		}    
        	       
 	      if (ejeY.equals("")) 
 	    	   ejeY = mapa.get("y_" + j)+""; 
 	      else
 	      	   ejeY +=  "&" + mapa.get("y_" + j);
         	      

 	      percentil = mapa.get("p_" + j)+""; 
		 } 

 		mp.put("nroPercentil_"+(nroPercentil++), ejeY);
 		
         
         mp.remove("nroPercentil_0");
         return mp;
	}
}
