package com.mercury.mundo.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.mercury.dao.odontologia.CartaDentalDao;
import com.mercury.dto.odontologia.DtoCartaDental;
import com.mercury.dto.odontologia.DtoDiagCartaDental;
import com.mercury.dto.odontologia.DtoTratamientoCartaDental;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;

public class CartaDental
{	
	
	private static boolean existeTratCirugia=false;
	private static boolean existeTratOperatoria=false;
	private static boolean existeTratDientNotrat=false;
	private static boolean existeTratEndodoncia=false;
	private static boolean existeTratOper_Endo=false;
	
	static Logger logger =  Logger.getLogger(CartaDental.class);
	
	public static ActionErrors errores = new ActionErrors();
	
	public static CartaDentalDao getCartaDentalDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCartaDentalDao();
	}
	
	/**
	 * Inserta o Modica la carta dental a partir del dto de carta dental
	 * @param Connection con
	 * @param DtoCartaDental dto
	 * @param String usuarioModifica
	 * */
	public static int insertarModificarCartaDentalDto(
			Connection con,
			DtoCartaDental dto,
			UsuarioBasico usuarioModifica)
	{
		logger.info("valor del dto >>  "+dto.getCodigoPk()+" "+dto.getCodPkTratamientoOdo());
		//Se verifica si la carta dental se encuentra creada
		if(dto.getCodigoPk() > 0)
		{
			//Se recorre la informacion por cada diente de la carta dental
			for(int d=0; d<dto.getArrayDtoDienteCartaDental().size(); d++)
			{
				//Inserta/Modifica los diagnosticos dentro del diente
				for(int diag = 0; diag < dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDental().size(); diag++)
				{
					//Insertar nuevos
					if(dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getCodigoPk() < 0 &&
							dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getActivo().equals(ConstantesBD.acronimoSi))
					{
						if(insertarDiagnosticoCartaDental(
								con,
								dto.getCodigoPk(),
								dto.getArrayDtoDienteCartaDental(d).getNumeroDiente(),
								dto.getArrayDtoDienteCartaDental(d).isPermanente(),
								dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getCodigoPkEstSecDienteInst(),
								dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getCodigoPkSuperficieDental()) < 0)
						{
							logger.info("error insertando la información de los diagnosticos del diente >> "+dto.getArrayDtoDienteCartaDental(d).getNumeroDiente());
							return ConstantesBD.codigoNuncaValido;
						}
					}
					
					//Modificar
					if(dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getCodigoPk() > 0 &&
							dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getActivo().equals(ConstantesBD.acronimoSi))
					{
						if(dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getCodigoPkSuperficieDental() != dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getCodigoPkSuperficieDentalAnterior())
						{
							if(!actualizarSuperficieDiagTrataCartaDental(
									con,
									dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getCodigoPkSuperficieDental(),
									dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getCodigoPk()))
							{
								logger.info("error en la actualizacion de las superificies del diente para el diagnostico");
								return ConstantesBD.codigoNuncaValido;
							}
						}
					}
					
					//Eliminar
					logger.info("valor del diagnostico a eliminar >> "+dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getCodigoPk()+" "+dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getActivo());
					if(dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getCodigoPk() > 0 &&
							!dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getActivo().equals(ConstantesBD.acronimoSi))
					{
						if(!eliminarDiagnosticoCartaDental(con,dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getCodigoPk()))
						{
							logger.info("error eliminando la informacion de los diagnostivos del diente >> "+dto.getArrayDtoDienteCartaDental(d).getNumeroDiente());
							return ConstantesBD.codigoNuncaValido;
						}
					}
				}
				
				//Inserta los tratamientos dentro del diente
				for(int trat = 0; trat < dto.getArrayDtoDienteCartaDental(d).getArrayDtoTratamientoCartaDental().size(); trat++)
				{
					if(dto.getArrayDtoDienteCartaDental(d).getArrayDtoTratamientoCartaDentalPos(trat).getCodigoPk() < 0 &&
							dto.getArrayDtoDienteCartaDental(d).getArrayDtoTratamientoCartaDentalPos(trat).isActivo())
					{
						if(insertarTratamientoCartaDental(
								con,
								dto.getCodigoPk(),
								dto.getArrayDtoDienteCartaDental(d).getNumeroDiente(),
								dto.getArrayDtoDienteCartaDental(d).isPermanente(),
								dto.getArrayDtoDienteCartaDental(d).getArrayDtoTratamientoCartaDentalPos(trat).getCodigoPkTipoTratOdoInst(),
								dto.getArrayDtoDienteCartaDental(d).getArrayDtoTratamientoCartaDentalPos(trat).isActivo(),
								usuarioModifica) < 0)									
						{
							logger.info("error insertando la información de los diagnosticos del diente >> "+dto.getArrayDtoDienteCartaDental(d).getNumeroDiente());
							return ConstantesBD.codigoNuncaValido;
						}
					}
					
					if(dto.getArrayDtoDienteCartaDental(d).getArrayDtoTratamientoCartaDentalPos(trat).getCodigoPk() > 0 &&
							!dto.getArrayDtoDienteCartaDental(d).getArrayDtoTratamientoCartaDentalPos(trat).isActivo())
					{
						if(!eliminarTratamientosCartaDental(con,dto.getArrayDtoDienteCartaDental(d).getArrayDtoTratamientoCartaDentalPos(trat).getCodigoPk()))
						{
							logger.info("error eliminando un tratamiento del diente >> "+dto.getArrayDtoDienteCartaDental(d).getNumeroDiente());
							return ConstantesBD.codigoNuncaValido;							
						}							
					}
				}
				
				//verifica si se cambio el numero del diente
				if(dto.getArrayDtoDienteCartaDental(d).getNumeroDiente() != dto.getArrayDtoDienteCartaDental(d).getNumeroDienteAnterior())
				{
					logger.info("cambia el numero del diente se actualizara >> "+dto.getArrayDtoDienteCartaDental(d).getNumeroDiente()+" >> "+dto.getArrayDtoDienteCartaDental(d).getNumeroDienteAnterior());
					if(!actualizarDienteDiagTrataCartaDental(
							con,
							dto.getArrayDtoDienteCartaDental(d).getNumeroDiente(),
							dto.getArrayDtoDienteCartaDental(d).isPermanente(),
							dto.getCodigoPk(),							
							dto.getArrayDtoDienteCartaDental(d).getNumeroDienteAnterior(),
							dto.getArrayDtoDienteCartaDental(d).isPermanenteAnterior()))
					{
						logger.info("error no actualizo informacion del diente");
						return ConstantesBD.codigoNuncaValido;
					}
				}
			}
		}
		else if(dto.getCodigoPk() < 0)
		{
			//Se inserta el encabezado de la carta dental
			int codigoPkCartaDental = insertarCartaDental(
					con, 
					dto.getCodPkTratamientoOdo(),					
					dto.getCodMedicoRegistra(),
					usuarioModifica.getLoginUsuario(),
					dto.getOtrosHallazgos());
			
			if(codigoPkCartaDental > 0)
			{
				//Se recorre la informacion por cada diente de la carta dental
				for(int d=0; d<dto.getArrayDtoDienteCartaDental().size(); d++)
				{
					//Inserta los diagnosticos dentro del diente
					for(int diag = 0; diag < dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDental().size(); diag++)
					{
						if(dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getCodigoPk() < 0 &&
								dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getActivo().equals(ConstantesBD.acronimoSi))
						{
							if(insertarDiagnosticoCartaDental(
									con,
									codigoPkCartaDental,
									dto.getArrayDtoDienteCartaDental(d).getNumeroDiente(),
									dto.getArrayDtoDienteCartaDental(d).isPermanente(),
									dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getCodigoPkEstSecDienteInst(),
									dto.getArrayDtoDienteCartaDental(d).getArrayDtoDiagCartaDentalPos(diag).getCodigoPkSuperficieDental()) < 0)
							{
								logger.info("error insertando la información de los diagnosticos del diente >> "+dto.getArrayDtoDienteCartaDental(d).getNumeroDiente());
								return ConstantesBD.codigoNuncaValido;
							}
						}
					}
					
					//Inserta los tratamientos dentro del diente
					for(int trat = 0; trat < dto.getArrayDtoDienteCartaDental(d).getArrayDtoTratamientoCartaDental().size(); trat++)
					{
						if(dto.getArrayDtoDienteCartaDental(d).getArrayDtoTratamientoCartaDentalPos(trat).getCodigoPk() < 0 &&
								dto.getArrayDtoDienteCartaDental(d).getArrayDtoTratamientoCartaDentalPos(trat).isActivo())
						{
							if(insertarTratamientoCartaDental(
									con,
									codigoPkCartaDental,
									dto.getArrayDtoDienteCartaDental(d).getNumeroDiente(),
									dto.getArrayDtoDienteCartaDental(d).isPermanente(),
									dto.getArrayDtoDienteCartaDental(d).getArrayDtoTratamientoCartaDentalPos(trat).getCodigoPkTipoTratOdoInst(),
									dto.getArrayDtoDienteCartaDental(d).getArrayDtoTratamientoCartaDentalPos(trat).isActivo(),
									usuarioModifica) < 0)									
							{
								logger.info("error insertando la información de los diagnosticos del diente >> "+dto.getArrayDtoDienteCartaDental(d).getNumeroDiente());
								return ConstantesBD.codigoNuncaValido;
							}
						}
					}
				}
			}
			else
			{
				logger.info("error insertando la información del encabezado del carta dental");
				return ConstantesBD.codigoNuncaValido;
			}
		}
		
		return 1;
	}
	
	/**
	 * Inserta la informacion de la carta dental
	 * @param Connection con
	 * @param int codTratamientoOdo
	 * @param int codMedicoRegistra
	 * @param String usuarioModifica
	 * @param String otrosHallazgos
	 * */
	public static int insertarCartaDental(
			Connection con,
			int codTratamientoOdo,
			int codMedicoRegistra,
			String usuarioModifica,
			String otrosHallazgos)
	{
		HashMap parametros = new HashMap();
		parametros.put("codTratamientoOdo",codTratamientoOdo);
		parametros.put("codMedicoRegistra",codMedicoRegistra);
		parametros.put("usuarioModifica",usuarioModifica);
		parametros.put("otrosHallazgos",otrosHallazgos);
		return getCartaDentalDao().insertarCartaDental(con, parametros);
	}
	
	/**
	 * Inserta la informacion del diagnostico de la carta dental
	 *  @param Connection con
	 *  @param int codigoPkCartaDental
	 *  @param int diente
	 *  @param boolean permanente
	 *  @param int estSecDienteInst 
	 *  @param int codSuperficieDental 
	 * */
	public static int insertarDiagnosticoCartaDental(
			Connection con, 
			int codigoPkCartaDental,
			int diente,
			boolean permanente,
			int estSecDienteInst,
			int codSuperficieDental)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoPkCartaDental", codigoPkCartaDental);
		parametros.put("diente", diente);
		parametros.put("permanente", permanente?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("estSecDienteInst", estSecDienteInst);
		parametros.put("codSuperficieDental", codSuperficieDental);
		return getCartaDentalDao().insertarDiagnosticoCartaDental(con, parametros);
	}
	
	/**
	 * Adiciona un nuevo diagnostico a partir de un Array
	 * @param ArrayList<DtoDiagCartaDental> array
	 * @param String [] datos
	 * */
	public static ArrayList<DtoDiagCartaDental> addDiagnosticoArray(ArrayList<DtoDiagCartaDental> array,String [] datos)	
	{			
		errores = new ActionErrors();
		for(int i=0; i<datos.length; i++)
		{
			if(!(datos[i]+"").equals("") 
					&&  (datos[i]+"").split(ConstantesBD.separadorSplit).length > 1)
			{	
				DtoDiagCartaDental dto = new DtoDiagCartaDental();
				dto.setCodigoPk(ConstantesBD.codigoNuncaValido);
				dto.setCodigoPkCartaDental(ConstantesBD.codigoNuncaValido);
				dto.setCodigoPkEstSecDienteInst(Utilidades.convertirAEntero(datos[i].toString().split(ConstantesBD.separadorSplit)[0]));
				dto.setCodigoPkSuperficieDental(ConstantesBD.codigoNuncaValido);
				dto.setNombreEstSecDiente(datos[i].toString().split(ConstantesBD.separadorSplit)[1]);
				dto.setNombreSuperficieDental("");
				array.add(dto);
				
				
			}
		}
		
		return array;
	}
	
	/**
	 * Adiciona un nuevo tratamiento a partir de un Array
	 * @param ArrayList<DtoDiagCartaDental> array
	 * @param String [] datos
	 * */
	public static ArrayList<DtoTratamientoCartaDental> addTratamientoArray(ArrayList<DtoTratamientoCartaDental> array,String [] datos)	
	{		
		errores = new ActionErrors();
		existenciaTratamientos(array);
		for(int i=0; i<datos.length; i++)
		{
			if(!(datos[i]+"").equals("")
					&&  (datos[i]+"").split(ConstantesBD.separadorSplit).length > 1)
			{
				if(!esErrorTratCirugia(Utilidades.convertirAEntero(datos[i].toString().split(ConstantesBD.separadorSplit)[0])))
					
				  
				   {	
				    DtoTratamientoCartaDental dto = new DtoTratamientoCartaDental();
					dto.setCodigoPk(ConstantesBD.codigoNuncaValido);
					dto.setCodigoPkCartaDental(ConstantesBD.codigoNuncaValido);
					dto.setCodigoPkTipoTratOdoInst(Utilidades.convertirAEntero(datos[i].toString().split(ConstantesBD.separadorSplit)[0]));
					dto.setNombreTratamiento(datos[i].toString().split(ConstantesBD.separadorSplit)[1]);				
					array.add(dto);
					}	
			}
		 }
			
		return array;
	}
	
	/**
	 * Inserta la informacion del tratamiento de la carta dental
	 *  @param Connection con
	 *  @param int codigoPkCartaDental
	 *  @param int diente
	 *  @param boolean permanente
	 *  @param int estSecDienteInst
	 *  @param int codSuperficieDental  
	 * */
	public static int insertarTratamientoCartaDental(
			Connection con,
			int codigoPkCartaDental,
			int diente,
			boolean permanente,		
			int tipoTratOdoInst,
			boolean activo,
			UsuarioBasico usuario)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoPkCartaDental", codigoPkCartaDental);
		parametros.put("diente", diente);
		parametros.put("permanente",permanente?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);		
		parametros.put("tipoTratOdoInst", tipoTratOdoInst);
		
		parametros.put("activo",activo?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("usuarioModifica",usuario.getLoginUsuario());
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		return getCartaDentalDao().insertarTratamientoCartaDental(con, parametros);
	}
	
	/**
	 * Consulta la informacion de la carta dental
	 * @param Connection con
	 * @param int codTratamientoOdo
	 * */
	public static DtoCartaDental cargarCartaDental(Connection con,int codTratamientoOdo)
	{
		HashMap parametros = new HashMap();
		parametros.put("codTratamientoOdo", codTratamientoOdo);
		return getCartaDentalDao().cargarCartaDental(con, parametros);
	}
	
	/**
	 * Elimina la informarcion de diagnosticos de la carta dental 
	 * @param Connection con
	 * @param int codigoPkDiagCartaDental 
	 * */
	public static boolean eliminarDiagnosticoCartaDental(Connection con,int codigoPkDiagCartaDental)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoPk",codigoPkDiagCartaDental);
		return getCartaDentalDao().eliminarDiagnosticoCartaDental(con, parametros);	
	}
	
	/**
	 * Elimina la informarcion de tratamientos de la carta dental 
	 * @param Connection con
	 * @param int codigoPkTratamientoCartaDental 
	 * */
	public static boolean eliminarTratamientosCartaDental(Connection con,int codigoPkTratamientoCartaDental)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoPk",codigoPkTratamientoCartaDental);
		return getCartaDentalDao().eliminarDiagnosticoCartaDental(con, parametros);
	}
	
	/**
	 * Actualiza la informacion del diente en los tratamientos y los diagnosticos
	 * @param Connection con
	 * @param int nuevoDiente
	 * @param boolean permanente
	 * @param int codigopkCartaDental
	 * @param int dienteAnterior
	 * @param boolean permanenteAnterior 
	 * */
	public static boolean actualizarDienteDiagTrataCartaDental(
			Connection con,
			int nuevoDiente,
			boolean permanente,
			int codigopkCartaDental,
			int dienteAnterior,
			boolean permanenteAnterior)
	{
		HashMap parametros = new HashMap();
		parametros.put("diente", nuevoDiente);
		parametros.put("permanente", permanente?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("codigopkCartaDental", codigopkCartaDental);
		parametros.put("dienteAnterior", dienteAnterior);
		parametros.put("permanente", permanenteAnterior?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		return getCartaDentalDao().actualizarDienteDiagTrataCartaDental(con, parametros);
	}
	
	/**
	 * Actualiza la informacion de la superficie de un diagnostico de la carta dental
	 * @param Connection con
	 * @param int codSuperficieDental
	 * @param int codigoPkDiag
	 * */
	public static boolean actualizarSuperficieDiagTrataCartaDental(Connection con, int codSuperficieDental,int codigoPkDiag)
	{
		HashMap parametros = new HashMap();
		parametros.put("codSuperficieDental",codSuperficieDental);
		parametros.put("codigoPk",codigoPkDiag);
		return getCartaDentalDao().actualizarSuperficieDiagTrataCartaDental(con, parametros);
	}
   
	/**
	 * Metodo que me devuelve true si existe error de Exclusion de Cirugia 
	 * con Endodoncia y Operatoria
	 * @param ArrayList<DtoTratamientoCartaDental> array
	 * @param int posActual
	 * @return esErrorCirugia
	 */
	public static boolean esErrorTratCirugia(int codigoTrata)
	{
	boolean esErrorCirugia=false;
	// Verifica si el tratamiento actual es Cirugia y valida que no existan los Tratamientos de Operatoria o Endodoncia
	if(codigoTrata==ConstantesBD.codigoTipoTratamientoCirugia)
	 {
		// verifica si existe el tratamiento Cirugia para evitar repetidos
		if(isExisteTratCirugia()){			
			esErrorCirugia=true;
		}else{
			setExisteTratCirugia(true);
			if(isExisteTratOper_Endo())
				{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","Cuando se tiene el tratamiento Operatoria o Endodoncia ,el tratamiento Cirugia no se puede Agregar"));
				esErrorCirugia=true;
				}
			}
	  }else{
		 if(codigoTrata==ConstantesBD.codigoTipoTratamientoDientesNoTrat)
		 	{
			// verifica si existe el tratamiento Dientes No Tratados para evitar repetidos
			 if(isExisteTratDientNotrat())
			 	{
			 		esErrorCirugia=true;	 
			 	}
		 	}else{
		 		// Verifica si el tratamiento actual es Operatoria o Endodoncia y valida que no exista el tratamiento Cirugia	  
		 		if((codigoTrata==ConstantesBD.codigoTipoTratamientoOperatoria) || (codigoTrata==ConstantesBD.codigoTipoTratamientoEndodoncia))
		 			{
		 		     // verifica si existe el tratamiento Operatoria para evitar repetidos
		 			 if((codigoTrata==ConstantesBD.codigoTipoTratamientoOperatoria) && isExisteTratOperatoria())
		 			 	{
		 				 esErrorCirugia=true; 
		 			 	}
		 			// verifica si existe el tratamiento Endondoncia para evitar repetidos
		 			 if((codigoTrata==ConstantesBD.codigoTipoTratamientoEndodoncia) && isExisteTratEndodoncia())
		 			 	{
		 				 esErrorCirugia=true; 
		 			 	}
		 			 
		 			 setExisteTratOper_Endo(true);	 
		 			 if(isExisteTratCirugia())
		 				{
		 				 errores.add("descripcion",new ActionMessage("errors.notEspecific","Cuando se tiene el tratamiento Cirugia, los tratamientos de Operatoria y/o Endodoncia no se pueden Agregar"));
		 				 esErrorCirugia=true;
		 				}
		 			}
	  			}
	  		}
	return esErrorCirugia;
	}
		
	/**
	 * Metodo que verifica la existencia de Tratamientos agragados a un diente
	 * en la carta dental
	 * @param
	 */
	public static void existenciaTratamientos(ArrayList<DtoTratamientoCartaDental> array){
		int j=0;
		setExisteTratCirugia(false);
		setExisteTratOper_Endo(false);	
		setExisteTratDientNotrat(false);
		setExisteTratOperatoria(false);
		
		while(j<array.size())		
		{	
		 if(array.get(j).getCodigoPkTipoTratOdoInst()==ConstantesBD.codigoTipoTratamientoCirugia)
		  { 
			 setExisteTratCirugia(true);
			 
		  }
		 
		 if(array.get(j).getCodigoPkTipoTratOdoInst()==ConstantesBD.codigoTipoTratamientoDientesNoTrat)
		  { 
			 setExisteTratDientNotrat(true);
			 
		  }
		 
		 if(array.get(j).getCodigoPkTipoTratOdoInst()==ConstantesBD.codigoTipoTratamientoOperatoria)
		  { 
			 setExisteTratOperatoria(true);
			 setExisteTratOper_Endo(true);	
			 
		  }
		 
		  if((array.get(j).getCodigoPkTipoTratOdoInst()==ConstantesBD.codigoTipoTratamientoEndodoncia))
		   {
			  setExisteTratEndodoncia(true);
			  setExisteTratOper_Endo(true);
			 
		   }
		  j++;
		}
		
	}
	
	
	
    /**
     * Metodo que retorna True si existe el Tratamiento cirugia en el arreglo 
     * de tratamientos adicionados a un diente en la carta dental 
     * @return
     */
	public static boolean isExisteTratCirugia() {
		return existeTratCirugia;
	}
	/**
	 * Metodo que actualiza el estado de existencia del tratamiento Cirugia en el 
	 * arreglo de tratamientos adicionados a un diente en la carta dental
	 * @param existeTratCirugia
	 */
	public static void setExisteTratCirugia(boolean existeTratCirugia) {
		CartaDental.existeTratCirugia = existeTratCirugia;
	}

	/**
	 * Metodo que retorna True si existe el Tratamiento Operatoria o Endodoncia en el arreglo 
     * de tratamientos adicionados a un diente en la carta dental 
	 * @return existeTratOper_Endo
	 */
	public static boolean isExisteTratOper_Endo() {
		return existeTratOper_Endo;
	}

	/**
	 * Metodo que actualiza el estado de existencia de los tratamientos Operatoria o Endodoncia en el 
	 * arreglo de tratamientos adicionados a un diente en la carta dental
	 * 
	 */
	public static void setExisteTratOper_Endo(boolean existeTratOper_Endo) {
		CartaDental.existeTratOper_Endo = existeTratOper_Endo;
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isExisteTratOperatoria() {
		return existeTratOperatoria;
	}
	
	/**
	 * 	
	 * @param existeTratOperatoria
	 */
	public static void setExisteTratOperatoria(boolean existeTratOperatoria) {
		CartaDental.existeTratOperatoria = existeTratOperatoria;
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isExisteTratDientNotrat() {
		return existeTratDientNotrat;
	}

	/**
	 * 	
	 * @param existeTratDientNotrat
	 */
	public static void setExisteTratDientNotrat(boolean existeTratDientNotrat) {
		CartaDental.existeTratDientNotrat = existeTratDientNotrat;
	}
	
	/**
	 * 	
	 * @return
	 */
	public static boolean isExisteTratEndodoncia() {
		return existeTratEndodoncia;
	}
	
	/**
	 * 
	 * @param existeTratEndodoncia
	 */
	public static void setExisteTratEndodoncia(boolean existeTratEndodoncia) {
		CartaDental.existeTratEndodoncia = existeTratEndodoncia;
	}
	
	
		
		
	
}